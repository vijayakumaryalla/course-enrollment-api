# Course Enrollment System API Automation

This project contains automated API tests for the **Course Enrollment System**.  
It is built using **Java (latest), Maven**, and **RestAssured** for API testing.

---

## 🛠 Technology Stack

- **Java:** 26
- **Maven:** Build and dependency management
- **RestAssured:** API testing framework
- **TestNG:** Test framework for assertions and test execution
- **Lombok:** Reduce boilerplate for POJOs

---

## 📁 Project Structure
CourseEnrollmentAPI/
│
├─ src/main/java/
├─ src/test
|    /java/
|       ├─ org.dwp.ces.models/ # POJOs for API request and responses
│       ├─ org.dwp.ces.utils/ # Utility classes (e.g., UniqueStringGenerator)
|       ├─ runner/ # Utility classes (e.g., UniqueStringGenerator)
│       ├─ stepdefinitions/ # Test classes (AddCourse, DeleteCourse, ListCourses)
│   /resources/
|   ├─ features/ # Test classes (AddCourse)
|   ├─ config.properties
├─ pom.xml # Maven configuration
└─ README.md

---

## ⚙️ Setup Instructions

1. **Clone the repository**
```bash
git clone https://github.com/yallav/course-enrollment-api.git
cd course-enrollment-api
```
2. **Install Latest Java**
3. **Build project with Maven**
```bash
mvn clean install
```
4. **Run tests**
```bash
mvn test
```
## 📝 Sample Test Flow
1. Login as student → get auth token 
2. List courses by title, instructor, and course code 
3. Check the availability 
4. Enroll to a course
5. Drop from a course