package edu.uga.cs.countryquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class startQuiz extends AppCompatActivity {
    final String TAG = "Quiz Activity";
    private static final int NUM_QUESTIONS = 6;
    private CountriesData countriesData;
    private Quiz quiz;
    private int currentIndex = 0;


    // testing it out
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        countriesData = new CountriesData(this);
        countriesData.open();

        List<Country> allCountries = countriesData.retrieveAllCountries();
        quiz = makeQuiz(allCountries);

        countriesData.close();

        displayQuestion();
        RadioGroup rg = findViewById(R.id.radioGroup);
        rg.clearCheck();

        View view = findViewById(android.R.id.content);

        view.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeLeft() {
                saveAnswer();
                currentIndex++;

                if (currentIndex < quiz.getNumberOfQuestions()) {
                    displayQuestion();
                } else {
                    goToResults();
                }
            }
        });
    }

    // display questions
    private void displayQuestion() {
        Question q = quiz.getQuestions().get(currentIndex);

        TextView question = findViewById(R.id.textView4);
        RadioButton c1 = findViewById(R.id.radioButton);
        RadioButton c2 = findViewById(R.id.radioButton2);
        RadioButton c3 = findViewById(R.id.radioButton3);

        question.setText("What is the capital of " + q.getCountryName() + "?");

        c1.setText(q.getAnswerChoice(0));
        c2.setText(q.getAnswerChoice(1));
        c3.setText(q.getAnswerChoice(2));
    }
    // save answers
    private void saveAnswer() {
        RadioGroup rg = findViewById(R.id.radioGroup);
        int selectedId = rg.getCheckedRadioButtonId();

        if (selectedId != -1) {
            RadioButton selected = findViewById(selectedId);
            String answer = selected.getText().toString();

            Question q = quiz.getQuestions().get(currentIndex);
            q.setUserAnswer(answer);

            if (q.isAnswerCorrect()) {
                quiz.setCurrentScore(quiz.getCurrentScore() + 1);
            }
        }
    }

    // go to results
    private void goToResults() {
        Intent intent = new Intent(this, ResultsActivity.class);

        intent.putExtra("score", quiz.getCurrentScore());
        intent.putExtra("date", quiz.getQuizDate().getTime());

        startActivity(intent);
    }

    // Make quiz
    private Quiz makeQuiz(List<Country> allCountries) {
        Quiz quiz = new Quiz();

        // Shuffle countries for randomization
        Collections.shuffle(allCountries);

        // Pick the first six countries in the shuffle
        List<Country> selected = allCountries.subList(0, 6);
        for (Country i : selected) {

            // wrong capitals
            List<String> wrongCapitals = getWrongCapitals(allCountries, i.getCapital());

            Question question = new Question( i, i.getCapital(), wrongCapitals.get(0), wrongCapitals.get(1));

            // add question to the quiz
            quiz.getQuestions().add(question);
        }


        return quiz;
    }

    // Generate wrong answers
    private List<String> getWrongCapitals(List<Country> allCountries, String correctCapital) {
        List<String> wrongCapitals = new ArrayList<>();

        // Shuffle countries for randomization
        Collections.shuffle(allCountries);

        for (Country i : allCountries) {
            if (!i.getCapital().equals(correctCapital)) { // It's not the correct capital
                if ( !wrongCapitals.contains(i.getCapital())) { // the array doesn't already have the capital
                    wrongCapitals.add(i.getCapital());
                }
            }

            if (wrongCapitals.size() >= 2) // Take 2 Incorrect answers
                break;
        }

        return  wrongCapitals;
    }
}
