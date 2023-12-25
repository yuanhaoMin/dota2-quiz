package com.yuanhao.dota2quiz.model;

import java.util.List;

public final class Level {
    private final List<Question> questions;

    public Level(List<Question> questions) {
        this.questions = questions;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
