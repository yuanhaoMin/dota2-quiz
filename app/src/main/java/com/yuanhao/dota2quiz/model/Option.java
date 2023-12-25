package com.yuanhao.dota2quiz.model;

public final class Option {
    private final int id;
    private final String text;

    public Option(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
