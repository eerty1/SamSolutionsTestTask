package com.samsolutions.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "url_seq", name = "url_seq")
    private Long id;

    @Column(columnDefinition = "TEXT")
    @URL(message = "must be a valid url address")
    private String originalUrl;

    @Size(min = 3, max = 255, message = "not in range for {min} to {max}")
    private String shortUrl;

    @JsonIgnore
    private Long createdAt;
    private Long timeToLive;

    @PrePersist
    private void setCreatedAt() {
        createdAt = new Date().getTime();
    }
}
