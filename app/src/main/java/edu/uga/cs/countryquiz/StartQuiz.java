package edu.uga.cs.countryquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StartQuiz extends AppCompatActivity {
    private CountriesData countriesData;
    private Quiz quiz;
    private ViewPager2 viewPager;


    // testing it out
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_pager);

        countriesData = new CountriesData(this);
        countriesData.open();

        List<Country> allCountries = countriesData.retrieveAllCountries();
        if (allCountries.size() < 6) {
            Log.e("StartQuiz", "Not enough countries loaded! Check CSV and database.");
            finish();
            return;
        }

        quiz = makeQuiz(allCountries);

        countriesData.close();

        viewPager = findViewById(R.id.viewPager);

        if (savedInstanceState != null) {
            int index = savedInstanceState.getInt("currentIndex");
            int score = savedInstanceState.getInt("score");

            quiz.setCurrentScore(score);

            viewPager.setCurrentItem(index);
        }

        QuizPagerAdapter adapter = new QuizPagerAdapter(this, quiz);
        viewPager.setAdapter(adapter);

        // detect last swipe
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == quiz.getNumberOfQuestions() - 1) {
                    calculateScore();
                    goToResults();
                }
            }
        });
    }

    private void calculateScore() {
        int score = 0;
        for (Question q : quiz.getQuestions()) {
            if (q.isAnswerCorrect())
                score++;
        }
        quiz.setCurrentScore(score);
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

    // save state
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("currentIndex", viewPager.getCurrentItem());
        outState.putInt("score", quiz.getCurrentScore());
    }
}
