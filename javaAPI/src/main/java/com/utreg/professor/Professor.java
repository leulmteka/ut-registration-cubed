package com.utreg.professor;

import java.util.List;

public class Professor {

    private String ID;
    private String name;
    private String school;
    private String department;
    private float difficulty;
    private float rating;
    private int wouldTakeAgain;
    private List<String> tags;
    private int numRatings;

    public Professor() {
    }

    public Professor(String ID, String name, String school, String department, float difficulty, float rating, int wouldTakeAgain, List<String> tags, int numRatings) {
        this.ID = ID;
        this.name = name;
        this.school = school;
        this.department = department;
        this.difficulty = difficulty;
        this.rating = rating;
        this.wouldTakeAgain = wouldTakeAgain;
        this.tags = tags;
        this.numRatings = numRatings;
    }

    // Getters
    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getSchool() {
        return school;
    }

    public String getDepartment() {
        return department;
    }

    public float getDifficulty() {
        return difficulty;
    }

    public float getRating() {
        return rating;
    }

    public int getWouldTakeAgain() {
        return wouldTakeAgain;
    }

    public List<String> getTags() {
        return tags;
    }

    public int getNumRatings() {
        return numRatings;
    }

    // Setters
    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setDifficulty(float difficulty) {
        this.difficulty = difficulty;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setWouldTakeAgain(int wouldTakeAgain) {
        this.wouldTakeAgain = wouldTakeAgain;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setNumRatings(int numRatings) {
        this.numRatings = numRatings;
    }
}