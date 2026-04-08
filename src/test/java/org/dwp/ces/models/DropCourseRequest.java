package org.dwp.ces.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DropCourseRequest {

    private String username;
    private String courseCode;

    public void setStudent(String username) {this.username = username;}
    public String getStudent() { return username; }
    public void setCourseCode(String courseCode) {this.courseCode = courseCode;}
    public String getCourseCode() { return courseCode; }
}
