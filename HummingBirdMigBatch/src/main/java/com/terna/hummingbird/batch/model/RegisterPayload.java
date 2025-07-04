package com.terna.hummingbird.batch.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterPayload {

    public String code;
    public String description;
    public String email;
    @JsonProperty("system_ID")
    public int systemId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSystemId() {
        return systemId;
    }

    public void setSystemId(int systemId) {
        this.systemId = systemId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
