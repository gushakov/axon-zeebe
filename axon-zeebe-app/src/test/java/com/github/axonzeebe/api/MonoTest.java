package com.github.axonzeebe.api;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import reactor.core.publisher.Mono;

public class MonoTest {

    @Test
    public void testCombine() {

        final String[] bar = new String[]{null};

        Object answer = Mono.fromSupplier(() -> "foo")
                .concatWith(Mono.fromRunnable(() -> bar[0] = "bar"))
                .blockLast();

        Assertions.assertThat(bar).containsExactly("bar");
        Assertions.assertThat(answer).isEqualTo("foo");
    }
}
