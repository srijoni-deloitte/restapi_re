package Operation;

import Helper.ExtractData;
import Helper.JSONParser;
import Helper.User;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;

public class TaskOperation  {
    private RequestSpecification requestSpecification;
    private ResponseSpecification responseSpecification;
    private List<String> tasks;
    private JSONArray tasksJsonArray;
    public String token="1e3fbfc04d510423f16db6b1976f8d49a339e4e486ef5006ebe118b552a2a32", baseUrl;
    public ExtentTest test;
    public Logger log;

    public TaskOperation(String baseUrl, ExtentTest test, Logger log) {
        RestAssured.useRelaxedHTTPSValidation();
        this.baseUrl = baseUrl;
        this.test = test;
        this.log = log;
        RestAssured.baseURI=baseUrl;
        requestSpecification = RestAssured.given();
        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setBaseUri(baseUrl).addHeader("Content-Type", "application/json").addHeader("authorization","Bearer "+token);
        requestSpecification = RestAssured.with().spec(reqBuilder.build());

        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.expectContentType(ContentType.JSON);
        responseSpecification = resBuilder.build();
    }

    public void getResponseVerify() {
        test.info("Getting users data from , " + baseUrl);
        log.info("Getting users data from , " + baseUrl);
        // start HTTP GET to get an entry
try{
        Response response=given().spec(requestSpecification)
                .when().get().then().spec(responseSpecification)
                .extract().response();

        JSONObject obj = new JSONObject(response.asString());
        tasksJsonArray = new JSONArray(obj.getJSONArray("data"));
        System.out.println(tasksJsonArray);
    }
        catch (Exception e){
        log.debug("Request Failed");
        test.fail("Request Failed");
    }
        log.debug("Fetched data");
        test.pass("Successfully fetched JSON ");
    }

    public boolean getVerifyGender() {
        test.info("Verifying users gender data from , " + baseUrl);
        log.info("Verifying users gender data from , " + baseUrl);
        // start HTTP GET to get an entry
        try{
        Response response=given().spec(requestSpecification)
                .when().get().then().spec(responseSpecification)
                .extract().response();

        JSONObject obj = new JSONObject(response.asString());
        tasksJsonArray = new JSONArray(obj.getJSONArray("data"));

        for(int i=0;i<tasksJsonArray.length();i++){
            System.out.println(tasksJsonArray.getJSONObject(i));
            if(tasksJsonArray.getJSONObject(i).getString("gender").equals("female")==false && tasksJsonArray.getJSONObject(i).getString("gender").equals("male")==false) {
                test.fail("Gender not only MALE or FEMALE");
                return false;
            }
        }
        }
        catch (Exception e){
            log.debug("Request Failed");
            test.fail("Request Failed");
        }


        log.debug("Fetched gender data");
        test.pass("Successfully verified gender consistency from JSON ");
        return true;
    }
    public boolean getVerifyContainsEmail() {
        test.info("Verifying users email data from , " + baseUrl);
        log.info("Verifying users email data from , " + baseUrl);
        // start HTTP GET to get an entry
        Response response=given().spec(requestSpecification)
                .when().get().then().spec(responseSpecification)
                .extract().response();

        JSONObject obj = new JSONObject(response.asString());
        tasksJsonArray = new JSONArray(obj.getJSONArray("data"));
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        try{
        for(int i=0;i<tasksJsonArray.length();i++){
            System.out.println(tasksJsonArray.getJSONObject(i));
            if(!tasksJsonArray.getJSONObject(i).has("email")) {
                log.debug("Email Key not present");
                test.fail("Email Key not present");
                return false;
            }
            else{
                String email=tasksJsonArray.getJSONObject(i).getString("email");
                Matcher matcher = pattern.matcher(email);
                if (!matcher.matches()){
                    log.debug("Email id present, but not valid");
                    test.fail("Email id present, but not valid");
                    return false;
                }
            }
        }
    }
        catch (Exception e){
        log.debug("Request Failed");
        test.fail("Request Failed");
    }

        log.debug("Email'id present and in perfect shape");
        test.pass("Email'id present and in perfect shape");
        return true;
    }
    public boolean getVerifyUnique_id() {
        test.info("Verifying users with Unique ID data from , " + baseUrl);
        log.info("Verifying users with Unique ID data from , " + baseUrl);
        // start HTTP GET to get an entry
        Response response = given().spec(requestSpecification)
                .when().get().then().spec(responseSpecification)
                .extract().response();

        JSONObject obj = new JSONObject(response.asString());
        tasksJsonArray = new JSONArray(obj.getJSONArray("data"));
        ArrayList<Integer> uid_store = new ArrayList<Integer>(tasksJsonArray.length());
        try{
        for (int i = 0; i < tasksJsonArray.length(); i++) {
            System.out.println(tasksJsonArray.getJSONObject(i));

            if (uid_store.contains(tasksJsonArray.getJSONObject(i).getInt("id"))) {

                test.fail("UID not unique");
                return false;
            } else
                uid_store.add(tasksJsonArray.getJSONObject(i).getInt("id"));
        }
    }
        catch (Exception e){
        log.debug("Request Failed");
        test.fail("Request Failed");
    }

    int max_uid=Collections.max(uid_store);
    new User(max_uid);

    log.debug("Fetched UID data");
    test.pass("Successfully verified UIDs are unique from JSON ");
    return true;
    }


    public boolean post_alreadyPresentUser() {
        test.info("Posting already present mail-id , " + baseUrl);
        log.info("Posting already present mail-id , " + baseUrl);

        Response response = given().spec(requestSpecification).when().get().then().spec(responseSpecification)
                .extract().response();

        JSONObject obj = new JSONObject(response.asString());
        tasksJsonArray = new JSONArray(obj.getJSONArray("data"));
        try{
        System.out.println("Posting"+tasksJsonArray.getJSONObject(0).toString());
        log.debug("Posting"+tasksJsonArray.getJSONObject(0).toString());
        Response response1 = given().spec(requestSpecification).body(tasksJsonArray.getJSONObject(0).toString())
                    .when().post().then().spec(responseSpecification)
                    .extract().response();
            if (response1.statusCode() != 201) {
                String msg = "User already present with same email i.e. " ;
                log.warn(msg);
                test.warning(msg);
                test.fail("User already present with same email i.e.");
                return false;
            }
         }
        catch (Exception e){
        log.debug("Request Failed");
        test.fail("Request Failed");
    }
        log.debug("Fetched UID data");
        test.pass("Successfully verified UIDs are unique from JSON ");
        return true;
    }
}

