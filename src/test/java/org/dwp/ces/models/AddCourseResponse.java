package org.dwp.ces.models;

import lombok.Data;

@Data
public class AddCourseResponse {
    private String message;
    private Course newCourse;
}
