package com.mkenlo.quiz;

/**
 * Created by Melanie on 7/14/2017.
 */

public class Question {
    private int id;
    private String name;
    private String[] options;
    private String buttonType;
    private String[] solution;

    public Question(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getButtonType() {
        return buttonType;
    }

    public void setButtonType(String buttonType) {
        this.buttonType = buttonType;
    }

    public String[] getSolution() {
        return solution;
    }

    public void setSolution(String[] solution) {
        this.solution = solution;
    }
}
