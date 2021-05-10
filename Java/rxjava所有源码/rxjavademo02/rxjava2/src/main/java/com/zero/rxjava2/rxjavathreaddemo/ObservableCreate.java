package com.zero.rxjava2.rxjavathreaddemo;

public class ObservableCreate<T> extends Observable<T> {

    final ObservableOnSubscribe<T> source;

    public ObservableCreate(ObservableOnSubscribe<T> source) {
        this.source = source;
    }


    @Override
    public void subscribeActual(Observer<? super T> observer) {

        CreateEmitter<T> parent = new CreateEmitter<T>(observer);//创建一个发射器，并把观察者传入到发射器
        observer.onSubscribe(parent);//订阅动作，当观察者订阅被观察者的时候回调，并传入一个Disposable回调参数，当需要中断的时候 可以中断事件传递

        try {
            source.subscribe(parent);//调用ObservableOnSubscribe的subscribe，这里的source就是调用Observable的create方法传递进来的ObservableOnSubscribe接口的匿名实现类
        } catch (Throwable ex) {
            parent.onError(ex);
        }
    }

    static final class CreateEmitter<T>
            implements ObservableEmitter<T>, Disposable {

        private static final long serialVersionUID = -3434801548987643227L;

        final Observer<? super T> observer;

        CreateEmitter(Observer<? super T> observer) {
            this.observer = observer;
        }

        @Override
        public void onNext(T t) {
            if (t == null) {
                onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
                return;
            }
            if (!isDisposed()) {//判断是否需要中断
                observer.onNext(t);
            }
        }

        @Override
        public void onError(Throwable t) {
            if (!tryOnError(t)) {
            }
        }

        @Override
        public boolean tryOnError(Throwable t) {
            if (t == null) {
                t = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
            }
            if (!isDisposed()) {
                try {
                    observer.onError(t);
                } finally {
                    dispose();
                }
                return true;
            }
            return false;
        }

        @Override
        public void onComplete() {
            if (!isDisposed()) {
                try {
                    observer.onComplete();
                } finally {
                    dispose();
                }
            }
        }

        @Override
        public void setDisposable(Disposable d) {
        }

        @Override
        public void setCancellable(Cancellable c) {
        }

        @Override
        public ObservableEmitter<T> serialize() {
            return null;
        }

        @Override
        public void dispose() {
        }

        @Override
        public boolean isDisposed() {
            return false;
        }

        @Override
        public String toString() {
            return String.format("%s{%s}", getClass().getSimpleName(), super.toString());
        }
    }
}
