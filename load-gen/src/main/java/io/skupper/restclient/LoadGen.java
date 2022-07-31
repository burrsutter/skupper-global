package io.skupper.restclient;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.vertx.axle.core.Vertx;
import io.vertx.axle.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

@Path("/sendload")
public class LoadGen {

    int concurrency = 0;
    int inFlight    = 0;
    int total       = 0;
    int failures    = 0;
    String lastStatus = "<none>";

    @ConfigProperty(name = "targethost", defaultValue = "localhost" ) 
    String targethost;

    @ConfigProperty(name = "targetport",  defaultValue = "8081") 
    int targetport;


    @Inject
    Vertx vertx;

    private WebClient client;

    @PostConstruct
    void initialize() {
        client = WebClient.create(vertx,
            new WebClientOptions()
                .setDefaultHost(targethost)
                .setDefaultPort(targetport));
    }

    private void sendRequest() {
        inFlight++;
        total++;
        
        client.get(String.format("/%d", total - 1))
            .send()
            .whenComplete((resp, exception) -> {
                inFlight--;
                if (exception == null) {
                    lastStatus = resp.statusMessage();
                } else {
                    failures++;
                }
                if (inFlight < concurrency) {
                    sendRequest();
                }
            });
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{val}")
    public String setLoad(@PathParam("val") String val) {
        int newVal;
        try {
            newVal = Integer.parseInt(val);
        } catch (Exception e) {
            newVal = concurrency;
        }

        concurrency = newVal;

        while (concurrency > inFlight) {
            sendRequest();
        }

        return String.format("Load set to %d (in-flight: %d, total: %d, failures: %d, last_status: %s)",
            concurrency, inFlight, total, failures, lastStatus);
    }
}

