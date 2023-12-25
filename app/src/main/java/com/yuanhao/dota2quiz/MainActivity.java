package com.yuanhao.dota2quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuanhao.dota2quiz.model.Level;
import com.yuanhao.dota2quiz.model.Option;
import com.yuanhao.dota2quiz.model.Question;

import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final Button[] optionButtons = new Button[4];
    private int selectedOptionIndex = -1;

    private List<Level> levels;
    private Iterator<Level> levelIterator;
    private Iterator<Question> questionIterator;
    private Level currentLevel;
    private Question currentQuestion;

    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupButtons();
        initializeQuiz();
    }

    private void setupButtons() {
        optionButtons[0] = findViewById(R.id.option_button_0);
        optionButtons[1] = findViewById(R.id.option_button_1);
        optionButtons[2] = findViewById(R.id.option_button_2);
        optionButtons[3] = findViewById(R.id.option_button_3);
        for (int i = 0; i < optionButtons.length; i++) {
            final int index = i;
            optionButtons[i].setOnClickListener(view -> {
                selectedOptionIndex = index;
                for (Button b : optionButtons) {
                    b.setSelected(b == view);
                }
                updateSubmitButtonState();
            });
        }
        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(v -> updateScoreAndMoveNext());
    }

    private void updateSubmitButtonState() {
        Button submitButton = findViewById(R.id.submit_button);
        if (selectedOptionIndex == -1) {
            submitButton.setEnabled(false);
            submitButton.setBackgroundColor(Color.GRAY);
        } else {
            submitButton.setEnabled(true);
            submitButton.setBackgroundColor(Color.parseColor("#7E10E1"));
        }
    }

    private void initializeQuiz() {
        LoadQuizLogic loadQuizLogic = new LoadQuizLogic(this);
        try {
            levels = loadQuizLogic.loadLevelsFromXML(R.xml.quiz_bank);
            levelIterator = levels.iterator();
            moveToNextLevel();
        } catch (Exception e) {
            Log.e("MainActivity", "Error initializing quiz", e);
        }
    }

    private void updateScoreAndMoveNext() {
        // Update score
        if (currentQuestion.getCorrectAnswerId() == selectedOptionIndex) {
            score++;
            Toast.makeText(this, "正确!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "错误", Toast.LENGTH_SHORT).show();
        }
        // Move next
        moveToNextQuestion();
    }


    private void updateView() {
        // Reset choice
        if (selectedOptionIndex >= 0) {
            optionButtons[selectedOptionIndex].setSelected(false);
            selectedOptionIndex = -1;
        }
        updateSubmitButtonState();
        // update info
        TextView levelQuestionInfoText = findViewById(R.id.level_question_info_text);
        levelQuestionInfoText.setText(getString( //
                R.string.level_question_info, //
                levels.indexOf(currentLevel) + 1, //
                currentLevel.getQuestions().indexOf(currentQuestion) + 1, //
                currentLevel.getQuestions().size()));
        // update image
        ImageView imageView = findViewById(R.id.question_image);
        int drawableId = currentQuestion.getImageId() != null //
                ? getResources().getIdentifier(currentQuestion.getImageId(), "drawable", getPackageName()) //
                : R.drawable.dota2_icon;
        imageView.setImageResource(drawableId);
        // update question
        TextView questionText = findViewById(R.id.question_text);
        questionText.setText(currentQuestion.getQuestionText());
        List<Option> options = currentQuestion.getOptions();
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i].setText(options.get(i).getText());
        }
    }

    private void moveToNextLevel() {
        if (!levelIterator.hasNext()) {
            //TODO Add completion logic here
            levelIterator = levels.iterator();
        }
        currentLevel = levelIterator.next();
        questionIterator = currentLevel.getQuestions().iterator();
        moveToNextQuestion();
    }

    private void moveToNextQuestion() {
        if (questionIterator.hasNext()) {
            currentQuestion = questionIterator.next();
            updateView();
        } else {
            moveToNextLevel();
        }
    }
}