package stepdefinitions;


import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.dwp.ces.models.Course;
import org.dwp.ces.models.EnrollCourseRequest;
import org.testng.Assert;

import java.util.List;

import static io.restassured.RestAssured.given;

public class CheckCourseAvailabilitySteps {

    private String token;
    private Response response;
    private Course course;

    private static final String BASE_URL = "https://courseenrollmentapimanagementsystem.onrender.com";

    public CheckCourseAvailabilitySteps() {
        RestAssured.baseURI = BASE_URL;
    }

    // ------------------ SEARCH COURSE ------------------
    @When("I check availability of valid course by sending the {string}")
    public void checkAvailabilityCourseByCourseCode(String courseCode) {

        response =
                    given()
                            .when()
                            .get("/courses/availability/{courseCode}", courseCode)
                            .then()
                            .statusCode(200)
                            .extract().response();

            course = response.as(Course.class);
    }

    @Then("course code should match with {string} and course title should match with {string}")
    public void validateCourseByCourseCodeAndTitle(String courseCode, String title) {
        Assert.assertTrue(
                course.getCourseCode().equalsIgnoreCase(courseCode) &&
                course.getTitle().equalsIgnoreCase(title),
                "No course found with title: " + title
        );
    }
}
