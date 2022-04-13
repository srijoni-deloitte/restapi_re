import Helper.ExtractData;
import Helper.User;
import Operation.TaskOperation;
import Operation.UserSignupController;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.RestAssured;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

@Listeners(Listener.class)

public class TestClass {
    Logger log = extentController.log;
    ExtentReports extent = extentController.extent;
    @Test (priority = 1)
    void verifyFetched() {
        RestAssured.useRelaxedHTTPSValidation();
        ExtentTest regUserTest = extent.createTest("Fetch from REST-API");
        TaskOperation op = new TaskOperation(extentController.baseUrl,  regUserTest,extentController.log);
        op.getResponseVerify();
    }
    @Test (priority = 2)
    void verifyGender() {
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.useRelaxedHTTPSValidation();
        ExtentTest regUserTest = extent.createTest("Verify fetched Gender");
        TaskOperation op = new TaskOperation(extentController.baseUrl,  regUserTest,extentController.log);
        assert (op.getVerifyGender()==true);
    }
    @Test (priority = 3)
    void verifyUniqueUid() {
        RestAssured.useRelaxedHTTPSValidation();
        ExtentTest regUserTest = extent.createTest("Verify fetched Unique ID");
        TaskOperation op = new TaskOperation(extentController.baseUrl,  regUserTest,extentController.log);

        assert (op.getVerifyUnique_id()==true);
    }
    @Test (priority = 4)
    void post_regster_user() {
        RestAssured.useRelaxedHTTPSValidation();
        ExtentTest regUserTest = extent.createTest("Registering new User");
        TaskOperation op = new TaskOperation(extentController.baseUrl,  regUserTest,extentController.log);
        List<User> users = new ArrayList<>();
        users=ExtractData.getAllUsers();
        for (int i=0;i<users.size();i++){
            System.out.println(users.get(i).getFullUserJson());
            UserSignupController opPost = new UserSignupController(extentController.baseUrl,  regUserTest, extentController.log);
            //opPost.registerUser();
            assert (opPost.registerUser(users.get(i))==true);
        }
    }
    @Test (priority = 5)
    void post_alreadyPresent() {
        RestAssured.useRelaxedHTTPSValidation();
        ExtentTest regUserTest = extent.createTest("Posting Already present user");
        TaskOperation op = new TaskOperation(extentController.baseUrl,  regUserTest,extentController.log);
        assert (op.post_alreadyPresentUser()==true);


    }
}
