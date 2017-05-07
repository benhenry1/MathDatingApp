package com.example.ChoiceImpl;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Ben on 5/5/2017.
 * Ideally each Date object will be created by a form with all these fields on it
 */

public class Date implements Serializable {
    private int weight = -1, age = -1;
    private String name, occupation = "", height = "";
    private File picture = null; //Can be added later from viewDates?

    private int dateId; //Just which date this was in chronological order



    public Date(String name, int dateId) {
        this.name = name;
        this.dateId = dateId;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getName() {
        return name;
    }

    public File getPicture() {
        return picture;
    }

    public void setPicture(File picture) {
        this.picture = picture;
    }

    public int getDateId() { return dateId; } //The order in which the dates were added
}
