package cn.cychust.rxjava;

/**
 * @program: rxjava
 * @description: 观察者
 * @author: Yichao Chen
 * @create: 2019-05-03 12:29
 **/
public interface Observer<T> {
    void onCompleted();

    void onError(Throwable r);

    void onNext(T t);
}
