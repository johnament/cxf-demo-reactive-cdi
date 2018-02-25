package com.mycompany.rest;

import org.apache.cxf.cdi.extension.JAXRSServerFactoryCustomizationExtension;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.provider.StreamingResponseProvider;
import org.apache.cxf.jaxrs.reactor.client.ReactorInvokerProvider;
import org.apache.cxf.jaxrs.reactor.server.ReactorInvoker;
import org.apache.johnzon.jaxrs.JsrProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import java.util.Collections;

@ApplicationScoped
public class MyAppFeature implements Feature, JAXRSServerFactoryCustomizationExtension {
    public boolean configure(FeatureContext featureContext) {
        featureContext.register(JsrProvider.class);
        featureContext.register(ReactorInvokerProvider.class);
        return true;
    }

    @Override
    public void customize(JAXRSServerFactoryBean jaxrsServerFactoryBean) {
        ReactorInvoker invoker = new ReactorInvoker();
        invoker.setUseStreamingSubscriberIfPossible(false);
        jaxrsServerFactoryBean.setInvoker(invoker);
        StreamingResponseProvider<JsonObject> streamProvider = new StreamingResponseProvider<>();
        streamProvider.setProduceMediaTypes(Collections.singletonList("application/json"));
        jaxrsServerFactoryBean.setProvider(streamProvider);
    }
}
