package stepdefinitions;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.dwp.ces.models.*;
import org.dwp.ces.utils.ConfigUtil;
import org.dwp.ces.utils.UniqueStringGenerator;
import org.testng.Assert;

import java.util.*;

import static io.restassured.RestAssured.given;

public class AddAndDeleteCourseSteps {

    private String token;
    private Response response;
    private Map<String, Object> requestBody = null;
    private List<Map<String, Object>> courses = null;

    private static final String BASE_URL = "https://courseenrollmentapimanagementsystem.onrender.com";

    public AddAndDeleteCourseSteps() {
        RestAssured.baseURI = BASE_URL;
        requestBody = new HashMap<>();
    }

    // ------------------ LOGIN ------------------
    @Given("the instructor has valid credentials")
    public void instructorHasValidCredentials() {
        String username = ConfigUtil.get("instructor.username");
        String password = ConfigUtil.get("instructor.password");

        Assert.assertNotNull(username);
        Assert.assertNotNull(password);
    }

    @When("the instructor logs in")
    public void instructorLogsIn() {

        LoginRequest requestBody = new LoginRequest(
                ConfigUtil.get("instructor.username"),
                ConfigUtil.get("instructor.password")
        );

        response =
                given()
                        .contentType("application/json")
                        .body(requestBody)
                        .when()
                        .post("/instructor/login")
                        .then()
                        .statusCode(200)
                        .extract().response();

        TokenResponse tokenResponse = response.as(TokenResponse.class);
        token = tokenResponse.getToken();
    }

    @Then("the instructor should receive a valid authorization token")
    public void validateToken() {
        Assert.assertNotNull(token, "Token should not be null");
    }

    // ------------------ Add COURSES ------------------
    @When("the instructor add new courses by entering {string}, {string}, {string}, {string}, {string}, {string}")
    public void addNewCourses(String title, String code, String category, String capacity, String startDate, String endDate) {

        String uniqueCode = new StringBuilder()
                .append(code)
                .append("_")
                .append(UniqueStringGenerator.generate7CharString())
                .toString();

        requestBody.put("title", title);
        requestBody.put("instructor", ConfigUtil.get("instructor.username"));
        requestBody.put("courseCode", uniqueCode);
        requestBody.put("category", category);
        requestBody.put("totalCapacity", Integer.parseInt(capacity));
        requestBody.put("startDate", startDate);
        requestBody.put("endDate", endDate);

            response =
                    given()
                            .log().all()
                            .header("Authorization", "Bearer " + token)
                            .header("Content-Type", "application/json")
                            .body(requestBody)
                            .when()
                            .post("/courses")
                            .then()
                            .statusCode(201)
                            .extract().response();
    }

    @Then("the response should include the message {string}")
    public void theResponseShouldIncludeTheMessage(String expectedMessage) {
        AddCourseResponse responseObj = response.as(AddCourseResponse.class);

        Assert.assertEquals(responseObj.getMessage(), expectedMessage, "Message mismatch");
        Assert.assertEquals(
                responseObj.getNewCourse().getTitle(),
                requestBody.get("title"),
                "Course title mismatch"
        );
    }


    // ------------------ Delete COURSES ------------------
    @When("the instructor filter courses owned by me")
    public void filterCoursesOwnedByMe() {
        String instructorId = ConfigUtil.get("instructor.username");

        response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .get("/courses/all")
                        .then()
                        .extract().response();

        // Filter courses by instructor
        courses = response.jsonPath().getList("findAll { it.instructor == '" + instructorId + "' }");
        System.out.println("Courses owned by instructor: " + courses.size());
    }

    @When("delete one course randomly")
    public void deleteOneCourseRandomly() {
        if (courses.isEmpty()) {
            System.out.println("No courses found to delete.");
            return;
        }

        Random random = new Random();
        Map<String, Object> courseToDelete = courses.get(random.nextInt(courses.size()));
        String courseId = courseToDelete.get("_id").toString();

        response = given()
                .log().all()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/courses/" + courseId)
                .then()
                .extract().response();

        Assert.assertEquals(response.statusCode(), 200);
    }

    @Then("the response should contain the message {string}")
    public void theResponseShouldContainTheMessage(String expectedMessage) {
        String message = response.jsonPath().getString("message");
        System.out.println("Delete response message: " + message);
        Assert.assertEquals(message, expectedMessage);
    }
}
