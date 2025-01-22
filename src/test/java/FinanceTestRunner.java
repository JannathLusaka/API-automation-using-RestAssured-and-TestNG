import com.github.javafaker.Faker;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FinanceTestRunner extends Setup {

    @Test(priority = 1,description = "Check if new user can register successfully")
    public void register() throws ConfigurationException {
        FinanceController financeController = new FinanceController(prop);
        UserModel userModel = new UserModel();
    Faker faker=new Faker();
    String firstName=faker.name().firstName();
    String lastName=faker.name().lastName();
    String email=firstName+"1@gmail.com";
    String phoneNumber="0130"+Utils.generateRandomId(1000000,9999999);

        userModel.setFirstName(firstName);
        userModel.setLastName(lastName);
        userModel.setEmail(email);
        userModel.setPassword("1234");
        userModel.setPhoneNumber(phoneNumber);
        userModel.setAddress("dhaka");
        userModel.setGender("Female");
        userModel.setTermsAccepted(true);

        Response response =financeController.userRegistration(userModel);
        System.out.println(response.asString());

    JsonPath jsonPath=response.jsonPath();
    String userId=jsonPath.get("_id");
    System.out.println(userId);
    Utils.setEnvVar("userId",userId);

    String userLastName=jsonPath.get("lastName");
    Utils.setEnvVar("userLastName",userLastName);

 String userEmail=jsonPath.get("email");
 Utils.setEnvVar("userEmail",userEmail);

    }
    @Test(priority = 2, description = "Check if user can register with duplicate creds")
    public void userRegDuplicate(){
        FinanceController financeController = new FinanceController(prop);
        UserModel userModel = new UserModel();
        Faker faker=new Faker();
        String firstName=faker.name().firstName();
        String phoneNumber="0130"+Utils.generateRandomId(1000000,9999999);

        userModel.setFirstName(firstName);
        userModel.setLastName(prop.getProperty("userLastName"));
        userModel.setEmail(prop.getProperty("userEmail"));
        userModel.setPassword("1234");
        userModel.setPhoneNumber(phoneNumber);
        userModel.setAddress("dhaka");
        userModel.setGender("Female");
        userModel.setTermsAccepted(true);

        Response response =financeController.userRegistration(userModel);
        System.out.println(response.asString());
        JsonPath jsonPath=response.jsonPath();
        String msgActual=jsonPath.get("message");
        Assert.assertTrue(msgActual.contains("User already exists"));
    }
    @Test(priority = 3, description = "Check if admin can login with wrong creds")
    public void adminLoginWrongCreds(){
        FinanceController financeController=new FinanceController(prop);
        UserModel userModel=new UserModel();
        userModel.setEmail("admin@test.com");
        userModel.setPassword("habijabi");
        Response response=financeController.adminLogin(userModel);
        System.out.println(response.asString());

        JsonPath jsonPath=response.jsonPath();
        String msgActual=jsonPath.get("message");
        Assert.assertTrue(msgActual.contains("Invalid"));
    }
   @Test(priority = 4, description = "Check if admin can login successfuully")
    public void adminLogin() throws ConfigurationException {
    FinanceController financeController=new FinanceController(prop);
    UserModel userModel=new UserModel();
    userModel.setEmail("admin@test.com");
    userModel.setPassword("admin123");
    Response response=financeController.adminLogin(userModel);
        System.out.println(response.asString());

    JsonPath jsonPath=response.jsonPath();
    String adminToken=jsonPath.get("token");
    System.out.println(adminToken);

    Utils.setEnvVar("adminToken",adminToken);
    }

   @Test(priority = 5, description = "Check user list")
    public void userList(){
        FinanceController financeController=new FinanceController(prop);
        Response response=financeController.userList();
        System.out.println(response.asString());
    }

    @Test(priority = 6, description = "Search user with wrong id")
    public void searchUserWrongId(){
        FinanceController financeController=new FinanceController(prop);
        Response response=financeController.searchUser("123");
        System.out.println(response.asString());
        JsonPath jsonPath=response.jsonPath();
        String msgActual=jsonPath.get("message");
        Assert.assertTrue(msgActual.contains("User not found"));
    }
@Test (priority = 7, description = "Search user")
public void searchUser(){
   FinanceController financeController=new FinanceController(prop);
   Response response=financeController.searchUser(prop.getProperty("userId"));
    System.out.println(response.asString());
}


    @Test (priority = 8, description = "Check if admin can edit user info")
    public void editUserIndo(){
    FinanceController financeController = new FinanceController(prop);
    Response response = financeController.searchUser(prop.getProperty("userId"));
    UserModel userModel=new UserModel();
    userModel.setFirstName("TestUser");
    userModel.setLastName(prop.getProperty("userLastName"));
    userModel.setEmail(prop.getProperty("userEmail"));
    userModel.setPassword("1234");
    userModel.setPhoneNumber("0160"+Utils.generateRandomId(1000000,9999999));
    userModel.setAddress("dhaka");
    userModel.setGender("Female");
    userModel.setTermsAccepted(true);

    Response responseNew= financeController.editUserInfo(prop.getProperty("userId"), userModel);
    System.out.println(responseNew.asString());
}


    @Test(priority = 9,description = "Check if user can login")
     public void userLogin() throws ConfigurationException {
        FinanceController financeController=new FinanceController(prop);
        UserModel userModel=new UserModel();
        userModel.setEmail("lucyni@gmail.com");
        userModel.setPassword("1234");
       Response response= financeController.userLogin(userModel);
    System.out.println(response.asString());

    JsonPath jsonPath=response.jsonPath();
    String userToken=jsonPath.get("token");
    System.out.println(userToken);

    Utils.setEnvVar("userToken",userToken);
}

@Test(priority = 10, description = "Check item list")
public void itemList(){
        FinanceController financeController=new FinanceController(prop);
       Response response= financeController.itemList();
    System.out.println(response.asString());
}

@Test(priority = 11, description = "check if user can add new item to the list")
public void addItem() throws ConfigurationException {
        FinanceController financeController=new FinanceController(prop);
        ItemModel itemModel=new ItemModel();
        itemModel.setItemName("bed");
        itemModel.setQuantity("3");
        itemModel.setAmount("50");
        itemModel.setPurchaseDate("2025-01-19");
        itemModel.setMonth("January");
        itemModel.setRemarks("done");

        Response response=financeController.addItem(itemModel);
    System.out.println(response.asString());

    JsonPath jsonPath=response.jsonPath();
    String itemId=jsonPath.get("_id");
    System.out.println(itemId);
    Utils.setEnvVar("itemId",itemId);
}

@Test(priority = 12, description = "Check if item with no value can be added")
public void addItemNoValue(){
        FinanceController financeController=new FinanceController(prop);
        ItemModel itemModel=new ItemModel();
        itemModel.setItemName("");
        itemModel.setQuantity("");
        itemModel.setAmount("");
        itemModel.setPurchaseDate("");
        itemModel.setMonth("");
        itemModel.setRemarks("");

        Response response=financeController.addItem(itemModel);
        System.out.println(response.asString());

        JsonPath jsonPath=response.jsonPath();
        String msgActual=jsonPath.get("message");
    Assert.assertTrue(msgActual.contains("error"));

}

@Test(priority = 13, description = "Check if user can edit item")
public void editItem(){
        FinanceController financeController=new FinanceController(prop);
    Response response = financeController.searchItem(prop.getProperty("itemId"));
        ItemModel itemModel=new ItemModel();
        itemModel.setItemName("Mirror");
        itemModel.setQuantity("3");
        itemModel.setAmount("50");
        itemModel.setPurchaseDate("2025-01-19");
        itemModel.setMonth("January");
        itemModel.setRemarks("done");

        Response responseNew=financeController.editItem(prop.getProperty("itemId"),itemModel);
    System.out.println(responseNew.asString());
}

@Test(priority = 14, description = "Check if item can be deleted by user")
public void deleteItem(){
        FinanceController financeController=new FinanceController(prop);
        Response response=financeController.deleteItem(prop.getProperty("itemId"));
    System.out.println(response.asString());
}
}
