package com.revolut.io.domain;

import com.revolut.io.exceptions.EmptyInstanceListException;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestUtils {

    public static final List<String> BACKEND_INSTANCES = List.of(
            "127.0.0.1",
            "127.0.0.2",
            "127.0.0.3"
    );

    public static final List<String> BACKEND_ROUND_ROBIN_INSTANCES = Stream.concat(
            BACKEND_INSTANCES.stream(),
            BACKEND_INSTANCES.subList(0, BACKEND_INSTANCES.size() - 1).stream()
    ).collect(Collectors.toList());

    public static Stream<Arguments> invalidSelectInstanceParams() {
        return Stream.of(
                Arguments.of(null, NullPointerException.class),
                Arguments.of(List.of(), EmptyInstanceListException.class)
        );
    }

    private TestUtils() {

    }
}
