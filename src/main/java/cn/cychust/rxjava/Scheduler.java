package cn.cychust.rxjava;

import java.util.concurrent.Executor;

/**
 * @program: rxjava
 * @description:
 * @author: Yichao Chen
 * @create: 2019-05-03 14:07
 **/
public class Scheduler {
    final Executor mExcetor;

    public Scheduler(Executor executor) {
        this.mExcetor = executor;
    }

    public Worker createWorker() {
        return new Worker(mExcetor);
    }

    public static class Worker {
        final Executor mExecutor;

        public Worker(Executor executor) {
            this.mExecutor = executor;
        }

        public void schedule(Runnable runnable) {
            mExecutor.execute(runnable);
        }
    }
}
