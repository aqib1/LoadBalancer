package com.revolut.io;

import com.revolut.io.model.BackendInstance;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestUtils {
    public static final List<BackendInstance> INSTANCES = List.of(
            new BackendInstance("127.0.0.1", 4),
            new BackendInstance("127.0.0.2", 3),
            new BackendInstance("127.0.0.3", 2)
    );

    public static final List<BackendInstance> WEIGHTED_ROUND_ROBIN_INSTANCE = List.of(
            new BackendInstance("127.0.0.1", 4),
            new BackendInstance("127.0.0.1", 4),
            new BackendInstance("127.0.0.2", 3),
            new BackendInstance("127.0.0.1", 4),
            new BackendInstance("127.0.0.2", 3),
            new BackendInstance("127.0.0.3", 2),
            new BackendInstance("127.0.0.1", 4),
            new BackendInstance("127.0.0.2", 3),
            new BackendInstance("127.0.0.3", 2)
    );

    public static final List<BackendInstance> ROUND_ROBIN_INSTANCE = Stream.concat(
            INSTANCES.stream(), INSTANCES.stream()
    ).collect(Collectors.toList());

    private TestUtils() {

    }
}
