package com.projects.system.urlshortener.util;

import com.norconex.commons.lang.url.URLNormalizer;

import java.net.URL;

public class UrlNormalizer {
    public static URL normalize(String url) {
        return new URLNormalizer(url)
            .lowerCaseSchemeHost()
            .removeDefaultPort()
            .removeDotSegments()
            .sortQueryParameters()
            .removeFragment()
            .toURL();
    }
}
