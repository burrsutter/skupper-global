package com.burrsutter;

import java.util.Random;

import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class Worker {
    int cnt = 1;

    private String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
    private String location = System.getenv().getOrDefault("LOCATION", "unknown");
    private int bound = Integer.parseInt(System.getenv().getOrDefault("BOUND", "1000"));

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String index() {        
        System.out.println("hostname: " + hostname + " location: " + location + " bound: " + bound);

        return location + " needs a number";
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String jobs(@PathParam("id") Long id) {
        Random random = new Random();
        
        for (int i = 1; i <= id; i++) {
            long wait = random.nextInt(bound);

            System.out.println("job:" + i + " waiting: " + wait);            

            try {
                long start = System.currentTimeMillis();
                Thread.sleep((long) wait);
                long end = System.currentTimeMillis();
                long duration = end - start;

                System.out.println("wait: " + wait + " duration: " + duration);

                Work item = new Work();
                item.result = wait * (random.nextInt(10) + 1);
                item.message = "Processed Recs: " + duration * (random.nextInt(5) + 1); 
                item.processedby = hostname;
                item.location = location;
                item.processingtime = duration;
                item.persist();

            } catch (InterruptedException ie) {
                System.err.println(ie);
            }
            
        }

        return "Jobs: " + id ;
    }

    @GET
    @Path("/delete")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String deleteAll() {

        Work.deleteAll();
        
        return "Deleted All";
    }

   

}