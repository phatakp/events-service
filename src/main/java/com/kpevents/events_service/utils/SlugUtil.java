package com.kpevents.events_service.utils;

import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.regex.Pattern;

@Component
public class SlugUtil {
    public static String toSlug(String text) {
        String normalizedText = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String slug = pattern.matcher(normalizedText).replaceAll("");
        slug = slug.toLowerCase();
        slug = slug.replaceAll("[^a-z0-9\\s-]", ""); // Remove invalid characters
        slug = slug.replaceAll("\\s+", "-"); // Replace spaces with hyphens
        slug = slug.replaceAll("^-|-$", ""); // Trim hyphens from start/end
        return slug;
    }
}

