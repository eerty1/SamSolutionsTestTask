package com.samsolutions.repository;

import com.samsolutions.model.Url;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends ListCrudRepository<Url, Long> {
    Optional<Url> findByShortUrl(String shortUrl);

    boolean existsByShortUrl(String shortUrl);

    void deleteByShortUrl(String shortUrl);
}
