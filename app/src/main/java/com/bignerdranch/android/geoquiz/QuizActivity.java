package com.bignerdranch.android.geoquiz;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";

    private static final String KEY_TOT_ANS = "totAnswers";
    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private TextView mQuestionTextView;
    private static Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas,true),
            new Question(R.string.question_asia, true)};
    private int mCurrentIndex = 0;
    private int mNumberCorrect = 0;
    private int mTotalQuestionsAnswered = 0;
    private static final String[] KEY_ANS_CLICKED_ARR = new String[mQuestionBank.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);




        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        mNextButton = (ImageButton)findViewById(R.id.next_button);
        mPreviousButton = (ImageButton)findViewById(R.id.previous_button);
        mTrueButton = (Button)findViewById(R.id.true_button);
        mFalseButton = (Button)findViewById(R.id.false_button);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            for(int i = 0; i < mQuestionBank.length; i++) {
                mQuestionBank[i].setIsAnswered(savedInstanceState.getBoolean(KEY_ANS_CLICKED_ARR[i], false));
                setClickedButtons(mTrueButton, mFalseButton, mQuestionBank[i].getIsAnswered());
                System.out.println("ans clicked arr is  " + KEY_ANS_CLICKED_ARR[i]);
            }

            mTotalQuestionsAnswered = savedInstanceState.getInt(KEY_TOT_ANS, 0);
        }

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentIndex == 0)
                    mCurrentIndex = mQuestionBank.length - 1;
                else
                    mCurrentIndex = mCurrentIndex - 1;
                updateQuestion();
                //mAnswerClicked = false;

                setClickedButtons(mTrueButton, mFalseButton, mQuestionBank[mCurrentIndex].getIsAnswered());
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
                //mAnswerClicked = false;
                setClickedButtons(mTrueButton, mFalseButton, mQuestionBank[mCurrentIndex].getIsAnswered());
            }
        });


        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                    //mAnswerClicked = true;
                    mQuestionBank[mCurrentIndex].setIsAnswered(true);
                    setClickedButtons(mTrueButton, mFalseButton, true);
                    //Toast toast = checkAnswer(true);
                    //toast.setGravity(Gravity.TOP, Gravity.CENTER_HORIZONTAL, Resources.getSystem().getDisplayMetrics().heightPixels / 3);
                    checkAnswer(true);

                }
            });


        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuestionBank[mCurrentIndex].setIsAnswered(true);
                //mAnswerClicked = true;
                setClickedButtons(mTrueButton, mFalseButton, true);
                checkAnswer(false);
            }
        });

        updateQuestion();
        setClickedButtons(mTrueButton, mFalseButton, mQuestionBank[mCurrentIndex].getIsAnswered());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        for(int i = 0; i < mQuestionBank.length; i++) {
            KEY_ANS_CLICKED_ARR[i] = "index" + i;
            savedInstanceState.putBoolean(KEY_ANS_CLICKED_ARR[i], mQuestionBank[i].getIsAnswered());
            System.out.println("on save instance state set TF buttons to " + mQuestionBank[i].getIsAnswered());
        }
        savedInstanceState.putInt(KEY_TOT_ANS, mTotalQuestionsAnswered);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();

        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        double percentCorrect = 0.0;
        String pattern = "###.##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);

        if(userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            mNumberCorrect++;
        }
        else
            messageResId = R.string.incorrect_toast;

        mTotalQuestionsAnswered++;
        //System.out.println(mNumberCorrect);

        percentCorrect = ((double)mNumberCorrect / mQuestionBank.length) * 100;
        String formattedPercent = decimalFormat.format(percentCorrect);

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        if(mTotalQuestionsAnswered >= mQuestionBank.length) {
            Toast.makeText(this, "Final Score: " + formattedPercent + "%", Toast.LENGTH_SHORT).show();
            for(int i = 0; i < mQuestionBank.length; i++)
                mQuestionBank[i].setIsAnswered(false);
            setClickedButtons(mTrueButton, mFalseButton, false);
            mCurrentIndex = 0;
            mTotalQuestionsAnswered = 0;
            mNumberCorrect = 0;
        }
    }

    private void enableTrueFalseButtons(Button trueButton, Button falseButton) {
        trueButton.setEnabled(true);
        falseButton.setEnabled(true);
    }

    private void disableTrueFalseButtons(Button trueButton, Button falseButton) {
        trueButton.setEnabled(false);
        falseButton.setEnabled(false);
    }

    private void setClickedButtons(Button trueButton, Button falseButton, boolean answerClicked) {
        if(answerClicked)
            disableTrueFalseButtons(trueButton, falseButton);
        else
            enableTrueFalseButtons(trueButton, falseButton);
    }
}
