package com.mycompany.rest;

import org.apache.cxf.jaxrs.reactor.client.ReactorInvoker;

import javax.enterprise.context.RequestScoped;
import javax.json.JsonObject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

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

    @GET
    @Path("sse/{id}")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void forBook(@Context Sse sse, @Context SseEventSink sink, @PathParam("id") final String id,
                        @HeaderParam(HttpHeaders.LAST_EVENT_ID_HEADER) @DefaultValue("0") final String lastEventId) {

        new Thread() {
            public void run() {
                try {
                    final Integer id = Integer.valueOf(lastEventId);
                    final OutboundSseEvent.Builder builder = sse.newEventBuilder();

                    sink.send(createStatsEvent(builder.name("book"), id + 1));
                    Thread.sleep(200);
                    sink.send(createStatsEvent(builder.name("book"), id + 2));
                    Thread.sleep(200);
                    sink.send(createStatsEvent(builder.name("book"), id + 3));
                    Thread.sleep(200);
                    sink.send(createStatsEvent(builder.name("book"), id + 4));
                    Thread.sleep(200);
                    sink.close();
                } catch (final InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    private static OutboundSseEvent createStatsEvent(final OutboundSseEvent.Builder builder, final int eventId) {
        return builder
                .id(Integer.toString(eventId))
                .data(Book.class, new Book("New Book #" + eventId, eventId))
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
