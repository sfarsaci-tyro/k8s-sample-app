package com.tyro.sfarsaci.experiments.greetingmessageprovider.greeting


import org.eclipse.microprofile.rest.client.inject.RestClient
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

@Path("/hello")
class GreetingResource {

    private val log: org.slf4j.Logger = LoggerFactory.getLogger("GreetingResource")

    @Inject
    @field: RestClient
    lateinit var greetingMessageService : GreetingMessageService

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun hello(@QueryParam("userName") userName: String?): String {
        return "${greetingMessageService.getHelloMessage()}${userName?.prependIndent(" ") ?: ""}"
    }

    @GET
    @Path("/busy")
    @Produces(MediaType.TEXT_PLAIN)
    fun busy() = Thread.sleep(1000).run{ greetingMessageService.getHelloMessage() }
}
