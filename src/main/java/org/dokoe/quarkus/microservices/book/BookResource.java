package org.dokoe.quarkus.microservices.book;

import jakarta.inject.Inject;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.Form;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Instant;

@Path("/api/books")
public class BookResource {

    @Inject
    Logger logger;

    @Inject
    @RestClient
    NumberProxy proxy;



    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Fallback(fallbackMethod = "fallingBackOnCreateABook")
    @Retry(
            maxRetries = 3, delay = 3000
    )
    public Response createABook(@FormParam("title") String title, @FormParam("author") String author, @FormParam("year") int yearOfPublication, @FormParam("genre") String genre) {
        Book book = new Book();
        book.isbn13 = proxy.generateNumbers().isbn13;
        book.title =title;
        book.author = author;
        book.yearOfPublication = yearOfPublication;
        book.genre = genre;
        book.creationDate = Instant.now();
        logger.info("Book created : " + book);
        return Response.status(201).entity(book).build();
    }


    public Response fallingBackOnCreateABook(String title, String author, int yearOfPublication, String genre) throws FileNotFoundException {
        Book book = new Book();
        book.isbn13 = "Will be set later";
        book.title =title;
        book.author = author;
        book.yearOfPublication = yearOfPublication;
        book.genre = genre;
        book.creationDate = Instant.now();
        saveBookOnDisk(book);
        logger.warn("Book saved on disk: " + book);
        logger.info("Book created : " + book);
        return Response.status(206).entity(book).build();
    }

    private void saveBookOnDisk(Book book) throws FileNotFoundException {
        String bookJson = JsonbBuilder.create().toJson(book);
        try (PrintWriter out = new PrintWriter("book-" + Instant.now().toEpochMilli() + ".json")) {
            out.println(bookJson);
        }

    }
}
