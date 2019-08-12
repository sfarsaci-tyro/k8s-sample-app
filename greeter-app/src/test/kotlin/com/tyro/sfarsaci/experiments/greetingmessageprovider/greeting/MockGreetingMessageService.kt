package com.tyro.sfarsaci.experiments.greetingmessageprovider.greeting

import org.eclipse.microprofile.rest.client.inject.RestClient
import javax.annotation.Priority
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Alternative

@Alternative()
@Priority(1)
@ApplicationScoped
@RestClient
class MockGreetingMessageService : GreetingMessageService {
    override fun getHelloMessage() = message

    companion object {
        val message = "G'day"
    }
}