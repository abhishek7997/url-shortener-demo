package com.projects.system.urlshortener.util;

import java.security.SecureRandom;

public class UniqueIdGenerator {
    private final long start = 1773683938791L; // 41 bits of timestamp
    private final long instance = new SecureRandom().nextLong(0, 1 << 10L); // 10 bits of machine id
    private static final long maxSequence = 0xFFF; // 12 bits of sequence id

    private long sequence = 0L;
    private long prevTimestamp = -1L;

    public UniqueIdGenerator() {}

    /**
    Generate unique ID based on snowflake algorithm
    */
    public long get() {
        long id = 0L;

        long currentTime = System.currentTimeMillis();
        if (currentTime < start) {
            throw new RuntimeException("Clock moved backwards");
        }

        if (currentTime == prevTimestamp) {
            sequence = (sequence + 1) & maxSequence;
            if (sequence == 0) {
                // this means that we have generated more than 2^12 sequence ids in the same millisecond and overflowed, hence we will now wait to get to the next millisecond
                long newTimestamp;
                do {
                    newTimestamp = System.currentTimeMillis();
                } while (newTimestamp <= currentTime);
                currentTime = newTimestamp;
            }
        } else {
            sequence = 0L;
        }

        prevTimestamp = currentTime;

        id = id | ((currentTime - start) << 22); // shift timestamp by 10+12 = 22 bits
        id = id | (instance << 12);
        id = id | sequence;

        return id;
    }
}
