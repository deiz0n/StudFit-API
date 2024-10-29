package com.deiz0n.studfit.infrastructure.config;

import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class AlgorithmGenerateNumber {

    private static final Random RANDOM = ThreadLocalRandom.current();

    private static char randomLetter() {
        int randomIndex = RANDOM.nextInt(26);
        return (char) ('A' + randomIndex);
    }

    private static int randomNumber() {
        return RANDOM.nextInt(10);
    }

    public static String generateCode() {
        var builder = new StringBuilder(6);
        for (int i=0; i<3; i++) {
            builder.append(randomLetter())
                    .append(randomNumber());
        }
        return builder.toString();
    }

}
