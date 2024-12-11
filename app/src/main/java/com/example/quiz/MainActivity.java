package com.example.quiz;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private Button _trueButton;
    private Button _falseButton;
    private Button _nextButton;
    private Button _hintButton;
    private TextView _questionTextView;
    private Question[] _questions = new Question[] {
            new Question(R.string.q_1, false),
            new Question(R.string.q_2, true),
            new Question(R.string.q_3, true),
            new Question(R.string.q_4, false),
            new Question(R.string.q_5, false)
    };
    private int _currentIndex = 0;
    private int _correctAnswers = 0;
    private static final String QUIZ_TAG = "QuizActivity";
    private static final String KEY_CURRENT_INDEX = "currentIndex";
    private static final int REQUEST_CODE_PROMPT = 0;
    public static final String KEY_EXTRA_ANSWER = "com.example.quiz.correctAnswer";
    public boolean answerWasShown = false;


    private void checkAnswerCorrectness(boolean userAnswer) {
        boolean correctAnswer = _questions[_currentIndex].getTrueAnswer();
        int resultMessageId = 0;
        if (answerWasShown) {
            resultMessageId = R.string.answer_was_shown;
        } else {
            if (userAnswer == correctAnswer) {
                resultMessageId = R.string.correct_answer;
                ++_correctAnswers;
            } else {
                resultMessageId = R.string.incorrect_answer;
            }
        }

        Toast.makeText(this, resultMessageId, Toast.LENGTH_SHORT).show();
    }

    private void setNextQuestion() {
        if (_currentIndex == 5) {
            String scoreMessage = getString(R.string.score_message, _correctAnswers, _questions.length);
            _questionTextView.setText(scoreMessage);
            _nextButton.setVisibility(View.GONE);
            _falseButton.setVisibility(View.GONE);
            _trueButton.setVisibility(View.GONE);
            _hintButton.setVisibility(View.GONE);
        } else if (_currentIndex < 5) {
            _questionTextView.setText(_questions[_currentIndex].getQuestionId());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(QUIZ_TAG, "Wywolana zostala metoda: onSaveInstanceState");
        outState.putInt(KEY_CURRENT_INDEX, _currentIndex);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(QUIZ_TAG, "onStart wywołane");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(QUIZ_TAG, "onResume wywołane");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(QUIZ_TAG, "onPause wywołane");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(QUIZ_TAG, "onStop wywołane");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(QUIZ_TAG, "onDestroy wywołane");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(QUIZ_TAG, "onCreate wywołane");
        if (savedInstanceState != null) {
            _currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX, 0);
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        _trueButton = findViewById(R.id.true_button);
        _falseButton = findViewById(R.id.false_button);
        _nextButton = findViewById(R.id.next_button);
        _hintButton = findViewById(R.id.hint_button);
        _questionTextView = findViewById(R.id.question_text_view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        _trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswerCorrectness(true);
            }
        });

        _falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswerCorrectness(false);
            }
        });

        _nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_currentIndex < 5) ++ _currentIndex;
                answerWasShown = false;
                setNextQuestion();
            }
        });

        _hintButton.setOnClickListener((v) -> {
            Intent intent = new Intent(MainActivity.this, PromptActivity.class);
            boolean correctAnswer = _questions[_currentIndex].getTrueAnswer();
            intent.putExtra(KEY_EXTRA_ANSWER, correctAnswer);
            startActivityForResult(intent, REQUEST_CODE_PROMPT);
        });
        setNextQuestion();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {return; }
        if (requestCode == REQUEST_CODE_PROMPT) {
            if (data == null) {return; }
            answerWasShown = data.getBooleanExtra(PromptActivity.KEY_EXTRA_ANSWER_SHOWN, false);
        }
    }
}