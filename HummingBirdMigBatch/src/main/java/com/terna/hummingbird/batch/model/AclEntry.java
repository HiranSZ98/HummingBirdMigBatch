package com.terna.hummingbird.batch.model;

public class AclEntry {
    private long systemID;
    private long groupID;
    private String description;


    public long getSystemID() {
        return systemID;
    }

    public void setSystemID(long systemID) {
        this.systemID = systemID;
    }

    public long getGroupID() {
        return groupID;
    }

    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
