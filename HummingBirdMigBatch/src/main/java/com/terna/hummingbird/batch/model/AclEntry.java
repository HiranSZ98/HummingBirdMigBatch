package com.terna.hummingbird.batch.model;

public class AclEntry {
    private long systemID;
    private String groupID;
    private String description;


    public long getSystemID() {
        return systemID;
    }

    public void setSystemID(long systemID) {
        this.systemID = systemID;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
