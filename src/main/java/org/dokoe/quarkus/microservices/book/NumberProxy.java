package org.dokoe.quarkus.microservices.book;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.logging.Logger;

import java.time.Instant;
import java.util.Random;

@RegisterRestClient(configKey = "number.proxy")
@Path("/api/numbers")
@Tag(name = "Number Rest Endpoint")
public interface NumberProxy {


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    IsbnThirteen generateNumbers();

}
