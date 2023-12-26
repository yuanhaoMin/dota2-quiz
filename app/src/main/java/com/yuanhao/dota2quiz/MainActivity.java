package com.yuanhao.dota2quiz;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yuanhao.dota2quiz.model.Option;
import com.yuanhao.dota2quiz.model.Question;

import java.util.List;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {

    private final Button[] optionButtons = new Button[4];
    private QuizManager quizManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupButtons();
        quizManager = new QuizManager(this);
        updateView();
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
                    }
            );
            final int correctOptionId = quizManager.getCurrentQuestion().getCorrectAnswerId();
            optionButtons[correctOptionId].setSelected(true);
        });
        final Button continueButton = findViewById(R.id.continue_button);
        continueButton.setOnClickListener(v -> {
            quizManager.moveToNextQuestion();
            updateView();
        });
        final Button resetButton = findViewById(R.id.reset_button);
        resetButton.setOnClickListener(v -> {
            quizManager.resetLevel();
            updateView();
        });
    }


    private void updateView() {
        // update option buttons
        Stream.of(optionButtons).forEach(button -> {
            button.setEnabled(true);
            button.setSelected(false);
        });
        // update info
        final TextView levelQuestionInfoText = findViewById(R.id.level_question_info_text);
        levelQuestionInfoText.setText(quizManager.getLevelQuestionInfo());
        // update image
        final Question currentQuestion = quizManager.getCurrentQuestion();
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
}