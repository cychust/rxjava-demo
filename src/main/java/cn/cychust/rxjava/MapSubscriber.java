package cn.cychust.rxjava;

/**
 * @program: rxjava
 * @description:
 * @author: Yichao Chen
 * @create: 2019-05-03 13:43
 **/
public class MapSubscriber<T, R> extends Subscriber<R> {

    final Subscriber<? super T> actual;
    final Observable.Transformer<? super R, ? extends T> transformer;


    public MapSubscriber(Subscriber<? super T> actual, Observable.Transformer<? super R, ? extends T> transformer) {
        this.actual = actual;
        this.transformer = transformer;
    }

    public void onCompleted() {
        actual.onCompleted();
    }

    public void onError(Throwable r) {
        actual.onError(r);
    }

    public void onNext(R r) {
        actual.onNext(transformer.call(r));
    }
}
