package com.owasptopten.secure.security.sanitization;

import lombok.experimental.UtilityClass;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

@UtilityClass
public class Sanitizers {

    public static String sanitizeHtml(String html) {
        return Jsoup.clean(html, Safelist.basic());
    }

}
