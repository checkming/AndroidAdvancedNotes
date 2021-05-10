package com.xiangxue.network.rxbus;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * RxBus2 is a central publish/subscribe event system based on RxJava2 for Android. Events are posted
 * ({@link #post(Object)}) to subscribers that have a matching handler method for the event type. To receive events,
 * subscribers must register themselves to the bus using {@link #register(Object)}. Once registered, subscribers
 * receive events until {@link #unRegister(Object)} is called. Event handling methods must be annotated by
 * {@link Subscribe}, must be public, return nothing (void), and have exactly one parameter the event).
 *
 * @author KingJA
 */
public class RxBus {
    private volatile static RxBus mRxBus;
    private final FlowableProcessor<Object> mFlowableProcessor;
    private final SubscriberMethodFinder mSubscriberMethodFinder;
    private static Map<Class<?>, Map<Class<?>, Disposable>> mDisposableMap = new HashMap<>();

    private RxBus() {
        mFlowableProcessor = PublishProcessor.create().toSerialized();
        mSubscriberMethodFinder = new SubscriberMethodFinder();
    }

    public static RxBus getDefault() {
        if (mRxBus == null) {
            synchronized (RxBus.class) {
                if (mRxBus == null) {
                    mRxBus = new RxBus();
                }
            }
        }
        return mRxBus;
    }

    /**
     * Registers the given subscriber to receive events. Subscribers must call {@link #unRegister(Object)} once they
     * are no longer interested in receiving events.
     * Subscribers have event handling methods that must be annotated by {@link Subscribe}.
     * The {@link Subscribe} annotation also allows configuration like {@link ThreadMode}.
     */
    public void register(Object subsciber) {
        Class<?> subsciberClass = subsciber.getClass();
        List<SubscriberMethod> subscriberMethods = mSubscriberMethodFinder.findSubscriberMethods(subsciberClass);
        if(subscriberMethods != null) {
            for (SubscriberMethod subscriberMethod : subscriberMethods) {
                addSubscriber(subsciber, subscriberMethod);
            }
        }
    }

    /**
     * translate the subscriberMethod to a subscriber,and put it in a cancleable container .
     */
    private void addSubscriber(final Object subsciber, final SubscriberMethod subscriberMethod) {
        Class<?> subsciberClass = subsciber.getClass();
        Class<?> eventType = subscriberMethod.getEventType();
        Disposable disposable = mFlowableProcessor.ofType(eventType).observeOn(subscriberMethod.getThreadMode())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        invokeMethod(subsciber, subscriberMethod, o);
                    }
                });
        Map<Class<?>, Disposable> disposableMap = mDisposableMap.get(subsciberClass);
        if (disposableMap == null) {
            disposableMap = new HashMap<>();
            mDisposableMap.put(subsciberClass, disposableMap);
        }
        disposableMap.put(eventType, disposable);
    }

    /**
     * call the subscriber method annotationed with receiverd event.
     */
    private void invokeMethod(Object subscriber, SubscriberMethod subscriberMethod, Object obj) {
        try {
            subscriberMethod.getMethod().invoke(subscriber, obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Posts the given event to the RxBus.
     */
    public void post(Object obj) {
        if (mFlowableProcessor.hasSubscribers()) {
            mFlowableProcessor.onNext(obj);
        }
    }


    /**
     * Unregisters the given subscriber from all event classes.
     */
    public void unRegister(Object subscriber) {
        Class<?> subscriberClass = subscriber.getClass();
        Map<Class<?>, Disposable> disposableMap = mDisposableMap.get(subscriberClass);
        if (disposableMap == null) {
           return;
        }
        Set<Class<?>> keySet = disposableMap.keySet();
        for (Class<?> evenType : keySet) {
            Disposable disposable = disposableMap.get(evenType);
            disposable.dispose();
        }
        mDisposableMap.remove(subscriberClass);
    }

    /**
     * Unregisters the given subscriber of eventType from all event classes.
     */
    public void unRegister(Object subscriber, Class<?> eventType) {
        Class<?> subscriberClass = subscriber.getClass();
        Map<Class<?>, Disposable> disposableMap = mDisposableMap.get(subscriberClass);
        if (disposableMap == null) {
            throw new IllegalArgumentException(subscriberClass.getSimpleName() + " haven't registered RxBus");
        }
        if (!disposableMap.containsKey(eventType)) {
            throw new IllegalArgumentException("The event with type of " + subscriberClass.getSimpleName() + " is not" +
                    " required in " + subscriberClass.getSimpleName());
        }
        Disposable disposable = disposableMap.get(eventType);
        disposable.dispose();
        mDisposableMap.remove(eventType);
    }

}
