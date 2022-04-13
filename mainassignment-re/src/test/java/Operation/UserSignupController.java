package Operation;

import Helper.User;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class UserSignupController {
    ExtentTest test;
    Logger log;
    String baseUrl;
    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;
    public String token="1e3fbfc04d510423f16db6b1976f8d49a339e4e486ef5006ebe118b552a2a32";


    public UserSignupController(String baseUrl,ExtentTest test, Logger log ) {
        RestAssured.useRelaxedHTTPSValidation();
        this.test = test;
        this.log = log;
        this.baseUrl = baseUrl;

        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setBaseUri(baseUrl).addHeader("Content-Type", "application/json").addHeader("authorization","Bearer "+token);
        requestSpecification = RestAssured.with().spec(reqBuilder.build());

        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.expectContentType(ContentType.JSON);
        responseSpecification = resBuilder.build();
    }

    public boolean registerUser(User user) {
        log.debug(user.getFullUserJson());
        test.info("User Registration test");
        test.debug(user.getFullUserJson().toString());

        Response response = given().spec(requestSpecification).body(user.getFullUserJson().toString()).post().then().
                spec(responseSpecification).extract().response();

        log.info("User successfully registered");
        test.log(Status.PASS, "User successfully registered");
        return true;
     }
}
