package com.example.animaladoptionapp;

public class UserFeedbackClass {

    String idFeedback, textFeedback;

    public UserFeedbackClass() {
    }

    public UserFeedbackClass(String idFeedback, String textFeedback) {
        this.idFeedback = idFeedback;
        this.textFeedback = textFeedback;
    }

    public String getIdFeedback() {
        return idFeedback;
    }

    public void setIdFeedback(String idFeedback) {
        this.idFeedback = idFeedback;
    }

    public String getTextFeedback() {
        return textFeedback;
    }

    public void setTextFeedback(String textFeedback) {
        this.textFeedback = textFeedback;
    }
}
