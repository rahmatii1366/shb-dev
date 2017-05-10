package com.shb.dev.sample.rest;

import com.shb.dev.server.response.ShbResponse;
import com.shb.dev.server.session.ShbSession;
import static javax.ws.rs.core.Response.*;

/**
 * @author Mohammad Rahmati, 5/7/2017 1:29 PM
 */
public class FirstService {
    public static ShbResponse getHelloWorld(
            ShbSession session) {
        return new ShbResponse(Status.OK, "hello world!");
    }

    public static ShbResponse getHelloName(
            ShbSession session, String name) {
        return new ShbResponse(Status.OK,
                "hello ".concat(name));
    }

    public static ShbResponse getHelloNameFamily(
            ShbSession session,
            String name, String family, String message) {

        if(message == null)
            message = "hi";
        ShbResponse response =
                new ShbResponse(Status.OK, message
                        .concat(" ")
                        .concat(name)
                        .concat(" ").concat(family));
        return response;
    }
}
