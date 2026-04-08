package org.dwp.ces.models;

import lombok.Data;

@Data
public class EnrolmentJson {

    private String username;
    private String courseCode;
    private String courseTitle;
    private String enrolDate;
    private String completionDate;
    private String status;
}
