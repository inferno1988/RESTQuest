package org.ifno.restquest;

import java.util.HashMap;

/**
 * Created by Maksym.Palamarchuk (Maksym.Palamarchuk@infopulse.com.ua)
 */
public class TestClass {
    private final HashMap<String, String> headers = new HashMap<String, String>();

    public void headers(java.lang.String userAgent, java.lang.String auth) {
        headers.put("Accept-Encoding", "gzip,deflate");
        headers.put("User-Agent", auth);
        headers.put(userAgent, auth);
    }

    public void data(java.lang.String appToken, java.lang.String devToken, java.lang.String userId) { }
}
