import Helper.ExtractData;
import Helper.User;
import Operation.TaskOperation;
import Operation.UserSignupController;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
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
        ExtentTest regUserTest = extent.createTest("Fetch from REST-API");
        TaskOperation op = new TaskOperation(extentController.baseUrl,  regUserTest,extentController.log);
        op.getResponseVerify();
    }
    @Test (priority = 2)
    void verifyGender() {
        ExtentTest regUserTest = extent.createTest("Verify fetched Gender");
        TaskOperation op = new TaskOperation(extentController.baseUrl,  regUserTest,extentController.log);
        assert (op.getVerifyGender()==true);
    }
    @Test (priority = 3)
    void verifyUniqueUid() {
        ExtentTest regUserTest = extent.createTest("Verify fetched Unique ID");
        TaskOperation op = new TaskOperation(extentController.baseUrl,  regUserTest,extentController.log);

        assert (op.getVerifyGender()==true);
    }
    @Test (priority = 4)
    void VerifyContainsEmail() {
        ExtentTest regUserTest = extent.createTest("Verify email consistency ");
        TaskOperation op = new TaskOperation(extentController.baseUrl,  regUserTest,extentController.log);

        assert (op.getVerifyContainsEmail()==true);
    }
    @Test (priority = 5)
    void post_regster_user() {
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
    @Test (priority = 6)
    void post_alreadyPresent() {
        ExtentTest regUserTest = extent.createTest("Posting Already present user");
        TaskOperation op = new TaskOperation(extentController.baseUrl,  regUserTest,extentController.log);
        assert (op.post_alreadyPresentUser()==true);


    }
}
