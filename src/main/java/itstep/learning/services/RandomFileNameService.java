package itstep.learning.services;

import com.google.inject.Singleton;

import java.util.Random;

@Singleton
public class RandomFileNameService {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int DEFAULT_ENTROPY_BITS = 64;
    private static final Random RANDOM = new Random();

    public String generateFilename(Integer length) {
        if (length == null) {
            length = calculateDefaultLength();
        }

        StringBuilder fileName = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            fileName.append(CHARACTERS.charAt(index));
        }

        return fileName.toString();
    }

    private int calculateDefaultLength() {
        double bitsPerCharacter = Math.log(CHARACTERS.length()) / Math.log(2);
        return (int) Math.ceil(RandomFileNameService.DEFAULT_ENTROPY_BITS / bitsPerCharacter);
    }
}

