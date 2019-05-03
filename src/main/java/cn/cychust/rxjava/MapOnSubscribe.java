package cn.cychust.rxjava;

/**
 * @program: rxjava
 * @description:
 * @author: Yichao Chen
 * @create: 2019-05-03 13:30
 **/
public class MapOnSubscribe<T, R> implements Observable.OnSubscribe<R> {

    final Observable<T> source;
    final Observable.Transformer<? super T, ? extends R> mTransformer;

    public MapOnSubscribe(Observable<T> source, Observable.Transformer<? super T, ? extends R> transformer) {
        this.source = source;
        this.mTransformer = transformer;
    }

    public void call(Subscriber<? super R> subscriber) {
        source.subscribe(new MapSubscriber<R, T>(subscriber, mTransformer));
    }

}

