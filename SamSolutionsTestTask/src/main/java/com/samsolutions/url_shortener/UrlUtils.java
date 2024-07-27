package com.samsolutions.url_shortener;

import com.samsolutions.model.Url;
import com.samsolutions.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class UrlUtils {
    private static final int MIN_CODE_POINT = '0';
    private static final int MAX_CODE_POINT = 'z';
    private static final int SHORT_URL_LENGTH = 7;

    private final UrlRepository urlRepository;

    public String generateUniqueShortUrl() {
        String shortUrl = buildRandomStringGenerator().generate(SHORT_URL_LENGTH);
        while (urlRepository.existsByShortUrl(shortUrl)) {
            shortUrl = buildRandomStringGenerator().generate(SHORT_URL_LENGTH);
        }
        return shortUrl;
    }

    public boolean isUrlExpired(Url url) {
        if (url.getTimeToLive() == null) {
            return false;
        }
        return new Date(url.getCreatedAt() + url.getTimeToLive()).before(new Date());
    }

    private RandomStringGenerator buildRandomStringGenerator() {
        return new RandomStringGenerator.Builder()
                .withinRange(MIN_CODE_POINT, MAX_CODE_POINT)
                .filteredBy(CharacterPredicates.ASCII_ALPHA_NUMERALS, CharacterPredicates.DIGITS)
                .build();
    }
}