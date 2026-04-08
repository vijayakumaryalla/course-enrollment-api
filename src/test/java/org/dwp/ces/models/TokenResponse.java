package org.dwp.ces.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenResponse {
    private String token;
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
