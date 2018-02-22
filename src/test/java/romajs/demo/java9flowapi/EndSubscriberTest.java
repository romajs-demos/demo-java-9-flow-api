package romajs.demo.java9flowapi;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;


public class EndSubscriberTest {

    @Test
    public void whenSubscribeToIt_thenShouldConsumeAll() {

        // given
        final SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
        final EndSubscriber<String> subscriber = new EndSubscriber<>();
        publisher.subscribe(subscriber);
        final List<String> items = List.of("1", "x", "2", "x", "3", "x");

        // when
        assertThat(publisher.getNumberOfSubscribers()).isEqualTo(1);
        items.forEach(publisher::submit);
        publisher.close();

        // then
        await().atMost(1000, TimeUnit.MILLISECONDS).until(() ->
                assertThat(subscriber.getConsumedElements()).containsExactlyElementsOf(items));
    }


    @Test
    public void whenSubscribeToIt_thenShouldConsumeOne() {

        // given
        final SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
        final EndSubscriber<String> subscriber = new EndSubscriber<>(1);
        publisher.subscribe(subscriber);
        final List<String> items = List.of("1", "x", "2", "x", "3", "x");
        final List<String> expected = List.of("1");

        // when
        assertThat(publisher.getNumberOfSubscribers()).isEqualTo(1);
        items.forEach(publisher::submit);
        publisher.close();

        // then
        await().atMost(1000, TimeUnit.MILLISECONDS).until(() ->
                assertThat(subscriber.getConsumedElements()).containsExactlyElementsOf(expected));
    }
}
