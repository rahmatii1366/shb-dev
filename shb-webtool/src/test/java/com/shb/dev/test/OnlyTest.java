package com.shb.dev.test;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * @author Mohammad Rahmati, 5/3/2017 8:42 PM
 */
public class OnlyTest {
    @Test
    public void stringProcessing() {
        String route = "/hello-world/:id/:name";
        String url = route.substring(0,
                route.indexOf(":"));
        System.out.println(url);

        int count = StringUtils.countMatches(route, ":");
        int colon = route.indexOf(":");
        String pathParams = route.substring(colon);


        System.out.println(count);
        System.out.println(colon);
        System.out.println(pathParams);
    }
}
