package org.dwp.ces.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CourseStatusResponse {
    private String message;
    @JsonProperty("enrolmentJson")
    private EnrolmentJson enrolmentJson;
}
