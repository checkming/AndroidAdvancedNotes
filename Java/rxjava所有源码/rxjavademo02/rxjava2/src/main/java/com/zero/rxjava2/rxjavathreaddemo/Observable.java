package com.zero.rxjava2.rxjavathreaddemo;

public abstract class Observable<T> implements ObservableSource<T>{

    public static <T> Observable<T> create(ObservableOnSubscribe<T> source) {//通过Observable(被观察者)的create方法创建一个被观察者
        return new ObservableCreate<T>(source);//Observable是一个abstract的类，
    }

    @Override
    public void subscribe(Observer<? super T> observer) {
        subscribeActual(observer);
    }

    public abstract void subscribeActual(Observer<? super T> observer);

}
