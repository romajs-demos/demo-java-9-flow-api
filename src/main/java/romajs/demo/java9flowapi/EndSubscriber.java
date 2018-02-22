package romajs.demo.java9flowapi;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicInteger;

public class EndSubscriber<T> implements Flow.Subscriber<T> {

    private Flow.Subscription subscription;

    private List<T> consumedElements = new LinkedList<>();

    private AtomicInteger howMuchMessagesConsume;

    public EndSubscriber() {
    }

    public EndSubscriber(Integer howMuchMessagesConsume) {
        this.howMuchMessagesConsume = new AtomicInteger(howMuchMessagesConsume);
    }

    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    public void onNext(T item) {
        System.out.println("Got: " + item);
        consumedElements.add(item);
        if(howMuchMessagesConsume == null || howMuchMessagesConsume.decrementAndGet() > 0) {
            subscription.request(1);
        }
    }

    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    public void onComplete() {
        System.out.println("Done");
    }

    public List<T> getConsumedElements() {
        return consumedElements;
    }
}
