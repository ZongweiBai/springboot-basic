package com.baymin.springboot.common.logging;

import java.util.concurrent.atomic.AtomicLong;

public class LoggingMessage {

    private static AtomicLong requestId = new AtomicLong(1);

    public static String nextId() {
        return String.valueOf(requestId.incrementAndGet());
    }

}
