package liveProject;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {

    //Headers object
    Map<String, String> headers = new HashMap<>();


    //Resource path
    String resourcePath = "/api/users";

    //Generate a contract
    @Pact(consumer = "UserConsumer", provider = "UserProvider")
    public RequestResponsePact createPact(PactDslWithProvider builder){

        // Add headers
        headers.put("Content-Type", "application/json");

        //create json body for request and response
        DslPart requestResponseBody = new PactDslJsonBody()
                .numberType("id", 123)
                .stringType("firstName", "Rinky")
                .stringType("lastName","Joshy")
                .stringType("email","rinky@example.com");

                //Write the fragment to pact
                 return builder.given("A request to create a user")
                         .uponReceiving("A request to create a user")
                         .method("POST")
                         .headers(headers)
                         .path(resourcePath)
                         .body(requestResponseBody)
                         .willRespondWith()
                         .status(201)
                         .body(requestResponseBody)
                         .toPact();
    }

    @Test
    @PactTestFor(providerName = "UserProvider", port = "8282")
    public void consumerTest(){
        //BaseURI
        String requestURI = "http://localhost:8282"+resourcePath;

        //request body
        Map<String , Object> reqBody = new HashMap<>();
        reqBody.put("id", 123);
        reqBody.put("firstName", "Rinky");
        reqBody.put("lastName", "Joshy");
        reqBody.put("email", "rinky@example.com");

        //generate

        given().headers(headers).body(reqBody).log().all().
                when().post(requestURI).
                then().statusCode(201).log().all();
    }
}
