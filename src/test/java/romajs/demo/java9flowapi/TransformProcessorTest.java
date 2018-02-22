package romajs.demo.java9flowapi;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;

public class TransformProcessorTest {

    @Test
    public void whenSubscribeAndTransformElements_thenShouldConsumeAll() {

        // given
        final SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
        final TransformProcessor<String, Integer> transformProcessor = new TransformProcessor<>(Integer::parseInt);
        final EndSubscriber<Integer> subscriber = new EndSubscriber<>();
        final List<String> items = List.of("1", "2", "3");
        final List<Integer> expectedResult = List.of(1, 2, 3);

        // when
        publisher.subscribe(transformProcessor);
        transformProcessor.subscribe(subscriber);
        items.forEach(publisher::submit);
        publisher.close();

        // then
        await().atMost(1000, TimeUnit.MILLISECONDS).until(() ->
                assertThat(subscriber.getConsumedElements()).containsExactlyElementsOf(expectedResult));
    }
}
