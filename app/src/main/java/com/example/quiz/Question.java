package com.example.quiz;

public class Question {
    private int _questionID;
    private boolean _trueAnswer;

    public Question(int id, boolean answer) {
        this._questionID = id;
        this._trueAnswer = answer;
    }

    public boolean getTrueAnswer() {
        return _trueAnswer;
    }

    public int getQuestionId() {
        return _questionID;
    }
}
