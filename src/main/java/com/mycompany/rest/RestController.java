package com.mycompany.rest;

import org.apache.cxf.jaxrs.reactor.client.ReactorInvoker;

import javax.enterprise.context.RequestScoped;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/")
public class RestController {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void doGet(@Suspended AsyncResponse asyncResponse) {
        ClientBuilder.newClient()
                .register(MyAppFeature.class)
                .target("https://postman-echo.com/get")
                .queryParam("arg1", "arg1")
                .queryParam("arg2", "arg2")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .rx(ReactorInvoker.class)
                .get(JsonObject.class)
                .subscribe(new SingularSubscriber<>(asyncResponse));
    }
}
