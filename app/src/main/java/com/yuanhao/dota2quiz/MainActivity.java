package com.yuanhao.dota2quiz;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yuanhao.dota2quiz.model.Option;
import com.yuanhao.dota2quiz.model.Question;

import java.util.List;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "quizLog";
    private final Button[] optionButtons = new Button[4];
    private CountDownTimer countDownTimer;
    private TextView countDownTimerText;
    private QuizManager quizManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupView();
        quizManager = new QuizManager(this);
        updateView();
    }

    private void setupView() {
        // count down timer
        countDownTimerText = findViewById(R.id.count_down_timer_text);
        // option buttons
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
        // other buttons
        final Button revealButton = findViewById(R.id.reveal_button);
        revealButton.setOnClickListener(v -> {
            Stream.of(optionButtons).forEach(button -> {
                        button.setEnabled(false);
                        button.setSelected(false);
                    }
            );
            final int correctOptionId = quizManager.getCurrentQuestion().getCorrectAnswerId();
            optionButtons[correctOptionId].setSelected(true);
            countDownTimer.cancel();
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
        // update timer
        startOrRestartTimer();
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
                : R.drawable.dota;
        imageView.setImageResource(drawableId);
        Log.i(TAG, String.valueOf(currentQuestion.getImageId()));
        // update question
        final TextView questionText = findViewById(R.id.question_text);
        questionText.setText(currentQuestion.getQuestionText());
        final List<Option> options = currentQuestion.getOptions();
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i].setText(options.get(i).getText());
        }
    }

    private void startOrRestartTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                countDownTimerText.setText(getString(R.string.count_down_timer, millisUntilFinished / 1000));
            }

            public void onFinish() {
            }
        }.start();
    }
}