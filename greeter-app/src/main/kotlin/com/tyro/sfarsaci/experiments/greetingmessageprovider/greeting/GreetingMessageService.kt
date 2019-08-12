package com.tyro.sfarsaci.experiments.greetingmessageprovider.greeting

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType


@Path("/")
@RegisterRestClient
interface GreetingMessageService {

    @GET
    @Path("/hello-message")
    @Produces(MediaType.TEXT_PLAIN)
    fun getHelloMessage(): String
}
