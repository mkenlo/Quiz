package com.mkenlo.quiz;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import static com.mkenlo.quiz.R.raw.questions;

public class MainActivity extends AppCompatActivity {

    List<Question> quiz;
    TextView questionTextview;
    RadioButton radioButton;
    CheckBox checkBox;
    EditText editable;
    int score = 0;
    int currentQuestionId = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateQuiz();
        displayQuestion(getBaseContext());
        displayProgress();
    }

    public void displayQuestion(final Context context) {

        final Question oneQuestion = quiz.get(currentQuestionId - 1);
        questionTextview = (TextView) findViewById(R.id.question);
        questionTextview.setText(oneQuestion.getName());

        RadioGroup optionGroup = (RadioGroup) findViewById(R.id.optionGroup);
        optionGroup.removeAllViews();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        switch (oneQuestion.getButtonType()) {
            case "TextView":
                editable = new EditText(context);
                editable.setLayoutParams(params);
                editable.setLines(1);
                optionGroup.addView(editable);
                break;
            case "RadioButton":
                for (final String option : oneQuestion.getOptions()) {
                    radioButton = new RadioButton(context);
                    radioButton.setText(option);
                    radioButton.setLayoutParams(params);
                    radioButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            if (option.equalsIgnoreCase(oneQuestion.getSolution()[0])) {
                                updateScore();
                                Toast.makeText(context, oneQuestion.getSolution()[0], Toast.LENGTH_LONG).show();
                            } else showFace("angry");
                        }
                    });
                    optionGroup.addView(radioButton);
                }
                break;
            case "CheckBox":
                for (final String option : oneQuestion.getOptions()) {
                    checkBox = new CheckBox(context);
                    checkBox.setText(option);
                    checkBox.setLayoutParams(params);
                    checkBox.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            if (option.equalsIgnoreCase(oneQuestion.getSolution()[0])) {
                                updateScore();
                            }
                        }
                    });
                    optionGroup.addView(checkBox);
                }
                break;
        }

    }

    public void populateQuiz() {
        String jsonQuestions = loadJSONFromAsset();
        Gson gson = new Gson();
        Question[] questionList = gson.fromJson(jsonQuestions, Question[].class);

        quiz = new ArrayList<>(Arrays.asList(questionList));
    }

    public void getNextQuestion(View v) {

        currentQuestionId++;
        if (currentQuestionId <= quiz.size()) {
            displayQuestion(getBaseContext());
        } else {
            Button but = (Button) findViewById(R.id.nextButton);
            but.setActivated(false);
            currentQuestionId = quiz.size();
        }
        displayProgress();
        showFace("neutral");
    }

    public void getPreviousQuestion(View v) {

        currentQuestionId--;
        if (currentQuestionId > 0) {
            displayQuestion(getBaseContext());
        } else {
            Button but = (Button) findViewById(R.id.prevButton);
            but.setActivated(false);
            currentQuestionId = 0;
        }
        displayProgress();
        showFace("neutral");
    }

    public void verifyQuestion(View v){
        RadioGroup optionGroup = (RadioGroup) findViewById(R.id.optionGroup);
        int test = optionGroup.getCheckedRadioButtonId();
        Log.d("VERIFY-QUESTION", String.valueOf(test));
    }

    private String loadJSONFromAsset() {
        String json;
        try {

            InputStream is = getResources().openRawResource(questions);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public void displayProgress() {

        ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setProgress(currentQuestionId);

        TextView progressText = (TextView) findViewById(R.id.progressText);
        StringBuilder text = new StringBuilder();
        text.append(currentQuestionId);
        text.append("/");
        text.append(quiz.size());
        progressText.setText(text);
    }

    public void updateScore() {
        score++;
        TextView scoreTextView = (TextView) findViewById(R.id.score);
        scoreTextView.setText(String.valueOf(score));
        showFace("happy");
        playNotif();

    }

    public void playNotif() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFace(String mood) {
        ImageView done = (ImageView) findViewById(R.id.resultImage);
        if (mood.equalsIgnoreCase("happy"))
            done.setImageResource(R.drawable.ic_satisfied);
        else if (mood.equalsIgnoreCase("angry"))
            done.setImageResource(R.drawable.ic_dissatisfied);
        else done.setImageResource(R.drawable.ic_neutral);

    }


}