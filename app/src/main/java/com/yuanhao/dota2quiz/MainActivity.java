package com.yuanhao.dota2quiz;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yuanhao.dota2quiz.model.Level;
import com.yuanhao.dota2quiz.model.Option;
import com.yuanhao.dota2quiz.model.Question;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {
    private final Button[] optionButtons = new Button[4];

    private List<Level> levels;
    private Iterator<Level> levelIterator;
    private Iterator<Question> questionIterator;
    private Level currentLevel;
    private Question currentQuestion;

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
        for (Button optionButton : optionButtons) {
            optionButton.setOnClickListener(view -> {
                for (final Button b : optionButtons) {
                    b.setSelected(b == view);
                }
            });
        }
        final Button revealButton = findViewById(R.id.reveal_button);
        revealButton.setOnClickListener(v -> {
            Stream.of(optionButtons).forEach(button -> {
                button.setEnabled(false);
                button.setSelected(false);
            });
            optionButtons[currentQuestion.getCorrectAnswerId()].setSelected(true);
        });
        final Button continueButton = findViewById(R.id.continue_button);
        continueButton.setOnClickListener(v -> moveToNextQuestion());
        final Button resetButton = findViewById(R.id.reset_button);
        resetButton.setOnClickListener(v -> resetLevel());
    }

    private void initializeQuiz() {
        final LoadQuizLogic loadQuizLogic = new LoadQuizLogic(this);
        try {
            levels = loadQuizLogic.loadLevelsFromXML(R.xml.quiz_bank);
            resetLevel();
        } catch (Exception e) {
            Log.e("MainActivity", "Error initializing quiz", e);
        }
    }


    private void updateView() {
        // update option buttons
        Stream.of(optionButtons).forEach(button -> {
            button.setEnabled(true);
            button.setSelected(false);
        });
        // update info
        final TextView levelQuestionInfoText = findViewById(R.id.level_question_info_text);
        levelQuestionInfoText.setText(getString( //
                R.string.level_question_info, //
                levels.indexOf(currentLevel) + 1, //
                currentLevel.getQuestions().indexOf(currentQuestion) + 1, //
                currentLevel.getQuestions().size()));
        // update image
        final ImageView imageView = findViewById(R.id.question_image);
        final int drawableId = currentQuestion.getImageId() != null //
                ? getResources().getIdentifier(currentQuestion.getImageId(), "drawable", getPackageName()) //
                : R.drawable.dota2_icon;
        imageView.setImageResource(drawableId);
        // update question
        final TextView questionText = findViewById(R.id.question_text);
        questionText.setText(currentQuestion.getQuestionText());
        final List<Option> options = currentQuestion.getOptions();
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i].setText(options.get(i).getText());
        }
    }

    private void resetLevel() {
        levelIterator = levels.iterator();
        moveToNextLevel();
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