package com.enel.hrgdms.batch.report.bean;

public class Class {
    private Property[] property;

    public Property[] getProperty ()
    {
        return property;
    }

    public void setProperty (Property[] property)
    {
        this.property = property;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [property = "+property+"]";
    }
}
