package com.yuanhao.dota2quiz.model;

import java.util.List;

public final class Question {
    private final String imageId;
    private final String questionText;
    private final List<Option> options;
    private final int correctAnswerId;

    public Question(String imageId, String questionText, List<Option> options, int correctAnswerId) {
        this.imageId = imageId;
        this.questionText = questionText;
        this.options = options;
        this.correctAnswerId = correctAnswerId;
    }

    public String getImageId() {
        return imageId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<Option> getOptions() {
        return options;
    }

    public int getCorrectAnswerId() {
        return correctAnswerId;
    }
}
