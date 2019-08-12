package com.tyro.sfarsaci.experiments.greetingmessageprovider.greeting

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test
import java.lang.System.currentTimeMillis

@QuarkusTest
open class GreetingResourceTest {
    private val salutation = MockGreetingMessageService.message

    @Test
    fun testHelloEndpoint() {
        given()
                .`when`().get("/hello")
                .then()
                .statusCode(200)
                .body(`is`(salutation))
    }

    @Test
    fun `Given a name it should say hello to that name`() {
        given()
                .`when`().get("/hello?userName=Test User")
                .then()
                .statusCode(200)
                .body(`is`("$salutation Test User"))
    }

    @Test
    fun `should have a busy endpoint that takes at least one second to reply`() {
        currentTimeMillis().apply {
            given()
                    .`when`().get("/hello/busy")
                    .then()
                    .statusCode(200)
                    .body(`is`(salutation))
        }.also {
            assert(currentTimeMillis() - it >= 1000)
        }
    }

}