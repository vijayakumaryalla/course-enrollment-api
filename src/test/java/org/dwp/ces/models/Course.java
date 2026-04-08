package org.dwp.ces.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Course {

    private String title;
    private String instructor;
    private String courseCode;
    private String category;
    private int totalCapacity;
    private int availableSlots;
    private String startDate;
    private String endDate;

    @JsonProperty("_id")
    private String id;

    @JsonProperty("__v")
    private int version;
}
