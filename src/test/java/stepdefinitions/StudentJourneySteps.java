package stepdefinitions;


import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.dwp.ces.models.*;
import org.dwp.ces.utils.ConfigUtil;
import org.testng.Assert;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;

public class StudentJourneySteps {

    private String token;
    private Response response;
    private Course course;
    private EnrollCourseRequest courseEnrollRequest;
    private List<Course> courses;

    private static final String BASE_URL = "https://courseenrollmentapimanagementsystem.onrender.com";

    public StudentJourneySteps() {
        RestAssured.baseURI = BASE_URL;
    }

    // ------------------ LOGIN ------------------
    @Given("the student has valid credentials")
    public void studentHasValidCredentials() {
        String username = ConfigUtil.get("student.username");
        String password = ConfigUtil.get("student.password");

        Assert.assertNotNull(username);
        Assert.assertNotNull(password);
    }

    @When("the student logs in")
    public void studentLogsIn() {

        LoginRequest requestBody = new LoginRequest(
                ConfigUtil.get("student.username"),
                ConfigUtil.get("student.password")
        );

        response =
                given()
                        .contentType("application/json")
                        .body(requestBody)
                        .when()
                        .post("/student/login")
                        .then()
                        .statusCode(200)
                        .extract().response();

        TokenResponse tokenResponse = response.as(TokenResponse.class);
        token = tokenResponse.getToken();
    }

    @Then("the student should receive a valid authorization token")
    public void validateToken() {
        Assert.assertNotNull(token, "Token should not be null");
    }

    // ------------------ SEARCH COURSES ------------------
    @When("the student searches for courses by title {string}")
    public void searchCoursesByTitle(String title) {

            response =
                    given()
                            .header("Authorization", "Bearer " + token)
                            .when()
                            .get("/courses/title/{title}", title)
                            .then()
                            .statusCode(200)
                            .extract().response();

            courses = Arrays.asList(response.as(Course[].class));
    }

    @Then("the response should include courses with the title {string}")
    public void validateCoursesByTitle(String title) {
        Assert.assertTrue(
                courses.stream().anyMatch(c -> c.getTitle().equalsIgnoreCase(title)),
                "No course found with title: " + title
        );
    }

    @When("the student searches for courses by instructor {string}")
    public void searchCoursesByInstructor(String instructor) {

        response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .get("/courses/instructor/{instructor}", instructor)
                        .then()
                        .statusCode(200)
                        .extract().response();

        courses = Arrays.asList(response.as(Course[].class));
    }

    @Then("the response should include courses taught by {string}")
    public void validateCoursesByInstructor(String instructor) {
        Assert.assertTrue(
                courses.stream().anyMatch(c -> c.getInstructor().equalsIgnoreCase(instructor)),
                "No course found for instructor: " + instructor
        );
    }

    // ------------------ CHECK AVAILABILITY ------------------
    @When("the student checks availability for course code {string}")
    public void checkAvailability(String courseCode) {

        response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .when()
                        .get("/courses/availability/{courseCode}",courseCode)
                        .then()
                        .statusCode(200)
                        .extract().response();

        course = response.as(Course.class);
    }

    @Then("the course should have available slots greater than {int}")
    public void the_course_should_have_available_slots_greater_than(Integer numOfAvailableSlots) {
        Assert.assertTrue(course.getAvailableSlots() > numOfAvailableSlots,
                "No available slots for course: " + course.getCourseCode());
    }

    // ------------------ ENROLL ------------------
    @When("the student enrolls in course code {string}")
    public void enrollCourse(String courseCode) {
        String username = ConfigUtil.get("student.username");
        courseEnrollRequest = new EnrollCourseRequest();
        courseEnrollRequest.setUsername(username);
        courseEnrollRequest.setCourseCode(courseCode);

        response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .body(courseEnrollRequest)
                        .when()
                        .post("/enrolments/enrol")
                        .then()
                        .statusCode(201)
                        .extract().response();

        String body = response.asString();

        Assert.assertTrue(
                body.contains("enrolled") || body.contains("success"),
                "Enrollment failed for course: " + courseEnrollRequest.getCourseCode()
        );
    }

    @Then("the enrollment should be successful")
    public void the_enrollment_should_be_successful() {
        CourseStatusResponse enrollmentResponse = response.as(CourseStatusResponse.class);
        EnrolmentJson enrolmentJson = enrollmentResponse.getEnrolmentJson();

        Assert.assertTrue(
                enrolmentJson.getCourseCode().equalsIgnoreCase(courseEnrollRequest.getCourseCode()) &&
                        enrolmentJson.getStatus().equalsIgnoreCase("active"),
                "Course enrollment is successfull: " + courseEnrollRequest.getCourseCode());
    }

    // ------------------ DROP COURSE ------------------
    @When("the student drops course code {string}")
    public void dropCourse(String courseCode) {
        String username = ConfigUtil.get("student.username");
        DropCourseRequest dropCourseRequest = new DropCourseRequest();
        dropCourseRequest.setUsername(username);
        dropCourseRequest.setCourseCode(courseCode);

        response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .body(dropCourseRequest)
                        .when()
                        .post("/enrolments/drop")
                        .then()
                        .statusCode(200)
                        .extract().response();

        String body = response.asString();

        Assert.assertTrue(
                body.contains("dropped") || body.contains("success"),
                "Drop failed for course: " + courseCode
        );
    }

    @Then("the course {string} enrollment status should be updated to {string}")
    public void the_course_enrollment_status_should_be_updated_to(String courseCode, String status) {
        CourseStatusResponse enrollmentResponse = response.as(CourseStatusResponse.class);
        EnrolmentJson enrolmentJson = enrollmentResponse.getEnrolmentJson();

        Assert.assertTrue(
                enrolmentJson.getCourseCode().equalsIgnoreCase(courseCode) &&
                        enrolmentJson.getStatus().equalsIgnoreCase(status),
                "Course dropped successfully: " + courseCode);
    }
}
