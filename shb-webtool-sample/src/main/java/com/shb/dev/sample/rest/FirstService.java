package com.shb.dev.sample.rest;

import javax.ws.rs.core.Response;

/**
 * @author Mohammad Rahmati, 5/7/2017 1:29 PM
 */
public class FirstService {
    public static Response getHelloWorld() {
        return Response.ok("hello world!").build();
    }

    public static Response getHelloName(String name) {
        return Response.ok("hello ".concat(name))
                .build();
    }

    public static Response getHelloNameFamily(
            String name, String family, String message) {
        return Response.ok(message.concat(" ")
                .concat(name)
                .concat(" ").concat(family))
                .build();
    }
}
