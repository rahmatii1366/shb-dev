package com.shb.dev.server.response;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

/**
 * @author Mohammad Rahmati, 5/10/2017 10:52 AM
 */
public class ShbResponse {
    private Status responseStatus;
    private Object entity;
    private String mediaType;

    public ShbResponse() {
        this.responseStatus = Status.NO_CONTENT;
        this.entity = null;
        this.mediaType = MediaType.APPLICATION_JSON;
    }

    public ShbResponse(Status responseStatus,
                       Object entity) {
        this.responseStatus = responseStatus;
        this.entity = entity;
        this.mediaType = MediaType.APPLICATION_JSON;
    }

    public ShbResponse(Status responseStatus,
                       Object entity,
                       String mediaType) {
        this.responseStatus = responseStatus;
        this.entity = entity;
        this.mediaType = mediaType;
    }

    public Status getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Status responseStatus) {
        this.responseStatus = responseStatus;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
}
