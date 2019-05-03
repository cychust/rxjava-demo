package cn.cychust.rxjava;

/**
 * @program: rxjava
 * @description:
 * @author: Yichao Chen
 * @create: 2019-05-03 12:38
 **/
public class Main {
  public static void main(String[] args) {
    Observable.create(new Observable.OnSubscribe<Integer>() {
      public void call(Subscriber<? super Integer> subscriber) {
        for (int i = 0; i < 10; i++) {
          subscriber.onNext(i);
        }
        subscriber.onCompleted();
      }
    })
              .subscribeOn(Schedulers.io())
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
              .observeOn(Schedulers.computation())
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
  }
}
