# Rxjava实现基本流程



### `Subscriber`观察者



`Observer` 接口

```java
public interface Observer<T> {
    void onCompleted();
    void onError(Throwable t);
    void onNext(T var1);
}
```

`SubScriber` 简化：

```java
public abstract class Subscriber<T> implements Observer<T> {
    public void start() {
    }
}
```



### `Observable`订阅源



Observable（订阅源）
--------------------- 



在RxJava里面是一个大而杂的类，拥有很多工厂方法和各式各样的操作符。每个Observable里面有一个OnSubscribe对象，只有一个方法（void call(Subscriber<? super T> subscriber);），用来产生数据流，这是典型的命令模式。



```java
public class Observable<T> {
    final OnSubscribe<T> onSubscribe;

    private Observable(OnSubscribe<T> onSubscribe) {
        this.onSubscribe = onSubscribe;
    }

    public static <T> Observable<T> create(OnSubscribe<T> onSubscribe) {
        return new Observable<T>(onSubscribe);
    }

    public void subscribe(Subscriber<? super T> subscriber) {
        subscriber.start();
        onSubscribe.call(subscriber);
    }

    public interface OnSubscribe<T> {
        void call(Subscriber<? super T> subscriber);
    }
}
```



这样一个大致的框架就出来了

测试

```java
        Observable.create(new Observable.OnSubscribe<Integer>() {
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 10; i++) {
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        }).subscribe(new Subscriber<String>() {
                public void onCompleted() {
                  System.out.println("complete");
                }

                public void onError(Throwable r) {

                }

                public void onNext(String string) {
                  System.out.println(Thread.currentThread().getName());
                  System.out.println(string);
                }
              });
```



下面实现map，起始map是就是对结果再包装一层Observe.



实现结果测试



```java
Observable.create(new Observable.OnSubscribe<Integer>() {
      public void call(Subscriber<? super Integer> subscriber) {
        for (int i = 0; i < 10; i++) {
          subscriber.onNext(i);
        }
        subscriber.onCompleted();
      }
    })
              .map(new Observable.Transformer<Integer, String>() {
                public String call(Integer from) {
                  System.out.println("subsc1@ " + Thread.currentThread().getName());
                  return "maping " + from;
                }
              })
              .map(new Observable.Transformer<String, String>() {
                public String call(String from) {
                  System.out.println("subsc2@ " + Thread.currentThread().getName());
                  return "maping2 " + from;
                }
              })
              .subscribe(new Subscriber<String>() {
                public void onCompleted() {
                  System.out.println("complete");
                }

                public void onError(Throwable r) {

                }

                public void onNext(String string) {
                  System.out.println(Thread.currentThread().getName());
                  System.out.println(string);
                }
              });
```



至于线程切换，就是在指定的线程调用`call` 函数、或调用`subscriber`里的onNext()等函数