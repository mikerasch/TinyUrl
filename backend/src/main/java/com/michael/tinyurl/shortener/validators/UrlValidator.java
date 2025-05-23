package com.michael.tinyurl.shortener.validators;

import com.michael.tinyurl.shortener.exceptions.InvalidUrlException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class UrlValidator {
    private UrlValidator() {}

    public static void validateUrl(String url) {
        try {
            new URL(url).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new InvalidUrlException("Url provided is invalid: " + url);
        }
    }
}
