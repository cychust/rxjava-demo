package cn.cychust.rxjava;

import java.util.concurrent.Executors;

/**
 * @program: rxjava
 * @description:
 * @author: Yichao Chen
 * @create: 2019-05-03 14:10
 **/
public class Schedulers {
    private static final Scheduler ioScheduler = new Scheduler(Executors.newSingleThreadExecutor());

    private static final Scheduler computation = new Scheduler(Executors.newCachedThreadPool());

    public static Scheduler io() {
        return ioScheduler;
    }

    public static Scheduler computation() {
        return computation;
    }
}
