package liveProject;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;


;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class GitHub_RestAssured_Project {

    String sshkey = "public ssh key - uhave to replace";
    int sshid ;
    RequestSpecification rs;

    @BeforeClass
    public void setUp(){
     rs = new RequestSpecBuilder()
            .setBaseUri("https://api.github.com").addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "token access token -replace with correct")
            .build();

   }
   @Test(priority = 1)
    public void PostTest(){

       Map<String , String> reqBody = new HashMap<>();
       reqBody.put("title","testkey");
       reqBody.put("key",sshkey);

       Response res = given().spec(rs).body(reqBody)
               .when().post("/user/keys");
       System.out.println(res.getBody().asPrettyString());
       sshid = res.then().extract().path("id");
       res.then().statusCode(201).body("key",equalTo(sshkey));


   }

   @Test(priority = 2)
    public void getTest(){

       Response res = given().spec(rs).pathParam("keyId",sshid)
               .when().get("/user/keys/{keyId}");
       System.out.println(res.getBody().asPrettyString());
       res.then().statusCode(200).body("key",equalTo(sshkey));

   }
   @Test(priority = 3)
    public void deleteTest() {

       Response res = given().spec(rs).pathParam("keyId", sshid)
               .when().delete("/user/keys/{keyId}");
       System.out.println(res.getBody().asPrettyString());
       res.then().statusCode(204);

   }
}
