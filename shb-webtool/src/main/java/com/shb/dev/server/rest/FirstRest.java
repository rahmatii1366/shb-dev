package com.shb.dev.server.rest;

import javax.ws.rs.core.*;

/**
 * @author Mohammad Rahmati, 5/3/2017 12:09 PM
 */
public class FirstRest {
    public Response getHello() {
        return Response.ok("hello").build();
    }
}
