package com.tyro.sfarsaci.experiments.greetingmessageprovider

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test

@QuarkusTest
open class GreetingMessageResourceTest {

    @Test
    fun `Should default to english language`() {
        given()
          .`when`().get("/hello-message")
          .then()
             .statusCode(200)
             .body(`is`("Hello"))
    }

    @Test
    fun `Should return greeting in Italian`() {
        given()
                .`when`().get("/hello-message?language=it")
                .then()
                .statusCode(200)
                .body(`is`("Ciao"))
    }

    @Test
    fun `Should return greeting in Spanish`() {
        given()
                .`when`().get("/hello-message?language=es")
                .then()
                .statusCode(200)
                .body(`is`("Hola"))
    }

    @Test
    fun `Should return greeting in Australian`() {
        given()
                .`when`().get("/hello-message?language=en_AU")
                .then()
                .statusCode(200)
                .body(`is`("G'day"))
    }
}