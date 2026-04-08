Feature: End-to-End Student Journey
  As a student
  I want to login, search, enroll, drop, and view courses
  So that I can manage my course enrollment smoothly

  Background:
    Given the student has valid credentials

  Scenario Outline: Complete student journey from login to enrollment management
    # Step 1: Login and get token
    When the student logs in
    Then the student should receive a valid authorization token

    # Step 2: Search courses
    When the student searches for courses by title "<Course Title>"
    Then the response should include courses with the title "<Course Title>"

    When the student searches for courses by instructor "<Course Instructor>"
    Then the response should include courses taught by "<Course Instructor>"

    # Step 3: Check course availability
    When the student checks availability for course code "<Course Code>"
    Then the course should have available slots greater than 0

    # Step 4: Enroll in a course
    When the student enrolls in course code "<Course Code>"
    Then the enrollment should be successful

    # Step 5: Drop the course
    When the student drops course code "<Course Code>"
    Then the course "<Course Code>" enrollment status should be updated to "dropped"

    Examples:
      | Course Title                 | Course Code    | Course Instructor        |
      | Data Structures & Algorithms | DSA_1775553866 | instructor_sarahhamilton |
