package com.samsolutions.url_shortener;

import com.samsolutions.model.Url;
import com.samsolutions.repository.UrlRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class UrlService {
    private static final Logger EXPIRED_URLS_STATISTICS_LOGGER = LoggerFactory.getLogger("EXPIRED_URLS_STATISTICS_LOGGER");
    private static final String URL_ALIAS_CONFLICT_MESSAGE = "This url alias already exists";
    private static final String URL_NOT_FOUND_MESSAGE = "Requested url wasn't found";
    private static final String URL_EXPIRED_MESSAGE = "Requested url is expired";
    private final UrlRepository urlRepository;
    private final UrlUtils urlUtils;

    @Value("${url-shortener-domain}")
    private String urlShortenerDomain;

    @Transactional
    public ResponseEntity<String> shortenUrl(Url url) {
        if (url.getShortUrl() == null) {
            String shortUrl = urlUtils.generateUniqueShortUrl();
            url.setShortUrl(shortUrl);
            urlRepository.save(url);
            return new ResponseEntity<>(urlShortenerDomain + shortUrl, CREATED);
        } else {
            if (!urlRepository.existsByShortUrl(url.getShortUrl())) {
                urlRepository.save(url);
                return new ResponseEntity<>(urlShortenerDomain + url.getShortUrl(), CREATED);
            } else {
                return new ResponseEntity<>(URL_ALIAS_CONFLICT_MESSAGE, CONFLICT);
            }
        }
    }

    @Transactional
    public ResponseEntity<String> redirectToOriginalUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl).orElseThrow(
                () -> new EntityNotFoundException(URL_NOT_FOUND_MESSAGE)
        );

        if (urlUtils.isUrlExpired(url)) {
            urlRepository.deleteById(url.getId());
            return new ResponseEntity<>(URL_EXPIRED_MESSAGE, GONE);
        }

        return ResponseEntity.status(MOVED_PERMANENTLY).location(URI.create(url.getOriginalUrl())).build();
    }

    @Transactional
    public void deleteByShortUrl(String shortUrl) {
        urlRepository.deleteByShortUrl(shortUrl);
    }

    /*
        Potentially, there could be a lot of urls, that are already expired, but not deleted,
        since they haven't been accessed for a long time period.
        They are clogging the database, which is why the method below will select all the urls,
        find the expired ones and delete them, simultaneously keeping statistics on the number of "dead" urls.
    */

    @Scheduled(fixedRateString = "${scheduler.rate}", initialDelayString = "${scheduler.initial-delay}")
    public void deleteExpiredUrls() {
        List<Url> expiredUrls = new ArrayList<>();

        urlRepository.findAll().forEach(url -> {
            if (urlUtils.isUrlExpired(url)) expiredUrls.add(url);
        });

        urlRepository.deleteAll(expiredUrls);

        EXPIRED_URLS_STATISTICS_LOGGER.info(expiredUrls.size() + " expired urls have been deleted");
    }
}
