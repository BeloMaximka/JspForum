package itstep.learning.services;

import java.text.Normalizer;

public class SlugService {
    public String slugify(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        String slug = input.toLowerCase().trim();
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        slug = slug.replaceAll("[^a-z0-9]+", "-");
        slug = slug.replaceAll("^-|-$", "");
        return slug;
    }
}
