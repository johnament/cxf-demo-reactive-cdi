package com.mycompany.rest;

import org.apache.cxf.jaxrs.reactivestreams.server.StreamingAsyncSubscriber;

import javax.ws.rs.container.AsyncResponse;

public class SingularSubscriber<T> extends StreamingAsyncSubscriber<T> {
    public SingularSubscriber(AsyncResponse asyncResponse) {
        super(asyncResponse, "", "", "");
    }
}
