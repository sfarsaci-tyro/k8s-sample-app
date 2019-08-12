package com.tyro.sfarsaci.experiments.greetingmessageprovider

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

@Path("/hello-message")
class GreetingMessageResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun getHelloMessage(@QueryParam("language") language: String?) = when (language) {
        "it" -> "Ciao"
        "es" -> "Hola"
        "en_AU" -> "G'day"
        else -> "Hello"
    }
}
