package edu.uga.cs.countryquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StartQuiz extends AppCompatActivity {
    private CountriesData countriesData;
    private Quiz quiz;
    private ViewPager2 viewPager;
    private Button btnSubmit;
    private boolean resultsSent = false;
    private int lastPosition = 0;


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

        QuizViewModel vm = new ViewModelProvider(this).get(QuizViewModel.class);
        if (vm.quiz == null) {
            vm.quiz = makeQuiz(allCountries); // only build once
        }
        quiz = vm.quiz;

        countriesData.close();

        viewPager = findViewById(R.id.viewPager);

        QuizPagerAdapter adapter = new QuizPagerAdapter(this, quiz);
        viewPager.setAdapter(adapter);

        if (savedInstanceState != null) {
            int index = savedInstanceState.getInt("currentIndex");
            viewPager.setCurrentItem(index, false);
            lastPosition = index;
        }

        // detect last swipe
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Detect when the user tries to swipe right from the last page
                // When on the last page and scroll settles back (over-scroll attempt),
                // we go to results
                if (state == ViewPager2.SCROLL_STATE_DRAGGING
                        && lastPosition == quiz.getNumberOfQuestions() - 1) {
                    // user started a drag on last page — flag it
                }
                if (state == ViewPager2.SCROLL_STATE_SETTLING
                        && lastPosition == quiz.getNumberOfQuestions() - 1
                        && !resultsSent) {
                    resultsSent = true;
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
    }
}
