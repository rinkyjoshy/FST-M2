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

    String sshkey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCRm0emB/s1uTDPGei5FoG2YIRirwh31Viodx9KFllGF/Wvw6eu4GLRC1UGxJum/6Q6tPemYmS8+12muzb9uVe/6GJhgWAyK5pe0mj0SU+cnwd9+CbzfeNs6cz7fkPljeYV/6fjYnumfwz+UtgW+wnCDKpjMW/y8TyUzMtZPNpNEXezjUVnPwvndSS/c1cXeo03OnF3Ab8erfaqpGLA5Li85j9FudSukJQYIalRkiGVrj6s8LK0X+/Dw9/m7rU1z3FzV4gyfvF/mg+on+xw7iza3T4zo2ijL9RsvvFq5rcz1+G4e8V6FQAHQyvh23FJZ+DO++aTdygg/xcpXeyiLwbd";
    int sshid ;
    RequestSpecification rs;

    @BeforeClass
    public void setUp(){
     rs = new RequestSpecBuilder()
            .setBaseUri("https://api.github.com").addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "token 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")
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
