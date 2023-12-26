package com.yuanhao.dota2quiz;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.yuanhao.dota2quiz.model.Level;
import com.yuanhao.dota2quiz.model.Option;
import com.yuanhao.dota2quiz.model.Question;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QuizManager {
    private final Context context;

    private List<Level> levels;
    private Iterator<Level> levelIterator;
    private Iterator<Question> questionIterator;
    private Level currentLevel;
    private Question currentQuestion;

    public QuizManager(Context context) {
        this.context = context;
        try {
            levels = loadLevelsFromXML(R.xml.quiz_bank);
            resetLevel();
        } catch (Exception e) {
            Log.e("MainActivity", "Error initializing quiz", e);
        }
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public String getLevelQuestionInfo() {
        return context.getString( //
                R.string.level_question_info, //
                levels.indexOf(currentLevel) + 1, //
                currentLevel.getQuestions().indexOf(currentQuestion) + 1, //
                currentLevel.getQuestions().size());
    }

    public void resetLevel() {
        levelIterator = levels.iterator();
        moveToNextLevel();
    }

    public void moveToNextLevel() {
        if (!levelIterator.hasNext()) {
            //TODO Add completion logic here
            levelIterator = levels.iterator();
        }
        currentLevel = levelIterator.next();
        questionIterator = currentLevel.getQuestions().iterator();
        moveToNextQuestion();
    }

    public void moveToNextQuestion() {
        if (questionIterator.hasNext()) {
            currentQuestion = questionIterator.next();
        } else {
            moveToNextLevel();
        }
    }

    private List<Level> loadLevelsFromXML(int xmlResourceId) throws XmlPullParserException, IOException {
        XmlResourceParser xrp = context.getResources().getXml(xmlResourceId);
        final ArrayList<Level> levels = new ArrayList<>();
        List<Question> questions = null;
        Question currentQuestion;
        String imageId = null;
        String questionText = null;
        ArrayList<Option> options = new ArrayList<>();
        int correctAnswerId = -1;

        int eventType = xrp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String tagName = xrp.getName();
                Log.d("XMLParsing", tagName);
                switch (tagName) {
                    case "level":
                        questions = new ArrayList<>();
                        break;
                    case "question":
                        options = new ArrayList<>();
                        break;
                    case "img_id":
                        imageId = xrp.nextText();
                        break;
                    case "text":
                        questionText = xrp.nextText();
                        break;
                    case "option":
                        int optionId = Integer.parseInt(xrp.getAttributeValue(null, "id"));
                        String optionText = xrp.nextText();
                        options.add(new Option(optionId, optionText));
                        break;
                    case "answer":
                        correctAnswerId = Integer.parseInt(xrp.getAttributeValue(null, "optionId"));
                        break;
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                String tagName = xrp.getName();
                if ("question".equalsIgnoreCase(tagName)) {
                    currentQuestion = new Question(imageId, questionText, options, correctAnswerId);
                    if (questions != null) {
                        questions.add(currentQuestion);
                    }
                } else if ("level".equalsIgnoreCase(tagName)) {
                    if (questions != null) {
                        levels.add(new Level(questions));
                    }
                }
            }
            eventType = xrp.next();
        }
        return levels;
    }
}
