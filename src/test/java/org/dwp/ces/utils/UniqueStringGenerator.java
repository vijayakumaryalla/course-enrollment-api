package org.dwp.ces.utils;

import java.util.UUID;

public class UniqueStringGenerator {
    public static String generate7CharString() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 7);
    }
}
