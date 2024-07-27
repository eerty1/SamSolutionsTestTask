package com.samsolutions.web;

import com.samsolutions.model.Url;
import com.samsolutions.url_shortener.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UrlController {
    private static final String API_PREFIX = "/api";
    private final UrlService urlService;

    @GetMapping(path = "/{shortUrl}")
    @ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
    @Operation(summary = "access original url by redirect")
    public ResponseEntity<String> redirectToUrl(@PathVariable String shortUrl) {
        return urlService.redirectToOriginalUrl(shortUrl);
    }

    @PostMapping(path = API_PREFIX + "/url-shortener")
    @Operation(
            summary = "create short url",
            description = "shortUrl - optional \n\n timeToLive - optional"
    )
    public ResponseEntity<String> createShortUrl(@Valid @RequestBody Url url) {
        return urlService.shortenUrl(url);
    }

    @DeleteMapping(path = API_PREFIX + "/{shortUrl}")
    @Operation(summary = "delete short url")
    public void deleteByShortUrl(@PathVariable String shortUrl) {
        urlService.deleteByShortUrl(shortUrl);
    }
}
