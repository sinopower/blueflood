package com.rackspacecloud.blueflood.service.udp.functions;

import com.google.common.util.concurrent.ListenableFuture;
import com.rackspacecloud.blueflood.concurrent.FunctionWithThreadPool;
import com.rackspacecloud.blueflood.concurrent.NoOpFuture;
import com.rackspacecloud.blueflood.service.ScheduleContext;
import com.rackspacecloud.blueflood.types.Metric;
import com.rackspacecloud.blueflood.utils.Util;

import java.util.Collection;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Simple demonstratin of a FunctionWithThreadPool that does not use its threadpool.  It does things on
 * whichever thread apply() was called on.
 */
public class ContextUpdater extends FunctionWithThreadPool<Collection<Metric>, ListenableFuture<Collection<Metric>>> {
    
    private final ScheduleContext context;
    
    public ContextUpdater(ThreadPoolExecutor threadPool, ScheduleContext context) {
        super(threadPool);
        this.context = context;
    }

    public ListenableFuture<Collection<Metric>> apply(Collection<Metric> input) throws Exception {
        // this is a quick operation, so do not use the threadpool.  Just do the work and return a NoOpFuture.
        for (Metric metric : input) {
            context.update(metric.getCollectionTime(), Util.computeShard(metric.getLocator().toString()));
        }
        return new NoOpFuture<Collection<Metric>>(input);
    }
}
