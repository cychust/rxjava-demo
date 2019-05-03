package cn.cychust.rxjava;

/**
 * @program: rxjava
 * @description: 订阅源
 * @author: Yichao Chen
 * @create: 2019-05-03 12:31
 **/
public class Observable<T> {
    final OnSubscribe<T> mOnSubscribe;

    private Observable(OnSubscribe<T> onSubscribe) {
        this.mOnSubscribe = onSubscribe;
    }

    public static <T> Observable<T> create(OnSubscribe<T> onSubscribe) {
        return new Observable<T>(onSubscribe);
    }

    @SuppressWarnings("unchecked")
    public <R> Observable<R> map(Transformer<? super T, ? extends R> transformer) {
        return create(new MapOnSubscribe<T, R>(this, transformer));
    }

    public void subscribe(Subscriber<? super T> subscriber) {
        subscriber.start();
        mOnSubscribe.call(subscriber);
    }

    public Observable<T> subscribeOn(final Scheduler scheduler) {
        return Observable.create(new OnSubscribe<T>() {
            public void call(final Subscriber<? super T> subscriber) {
                subscriber.start();
                scheduler.createWorker().schedule(new Runnable() {
                    public void run() {
                        Observable.this.mOnSubscribe.call(subscriber);
                    }
                });
            }
        });
    }

    public Observable<T> observeOn(final Scheduler scheduler) {
        return Observable.create(new OnSubscribe<T>() {
            public void call(final Subscriber<? super T> subscriber) {
                subscriber.start();
                final Scheduler.Worker worker = scheduler.createWorker();
                Observable.this.mOnSubscribe.call(new Subscriber<T>() {
                    public void onCompleted() {
                        worker.schedule(new Runnable() {
                            public void run() {
                                subscriber.onCompleted();
                            }
                        });
                    }

                    public void onError(final Throwable r) {
                        worker.schedule(new Runnable() {
                            public void run() {
                                subscriber.onError(r);
                            }
                        });
                    }

                    public void onNext(final T t) {
                        worker.schedule(new Runnable() {
                            public void run() {
                                subscriber.onNext(t);
                            }
                        });
                    }
                });
            }
        });
    }

    public interface OnSubscribe<T> {
        void call(Subscriber<? super T> subscriber);
    }

    public interface Transformer<T, R> {
        R call(T from);
    }
}
