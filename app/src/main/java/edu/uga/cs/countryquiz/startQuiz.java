package edu.uga.cs.countryquiz;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class startQuiz {
    final String TAG = "Quiz Activity";
    private static final int NUM_QUESTIONS = 6;
    private CountriesData countriesData;

    public startQuiz(Context context) {
        // Open countriesData
        countriesData = new CountriesData(context);
        countriesData.open();

        // get all the countries
        List<Country> allCountries = countriesData.retrieveAllCountries();

        // make quiz
        Quiz quiz = makeQuiz(allCountries);

        // close countriesData
        countriesData.close();
    }

    // Make quiz
    private Quiz makeQuiz(List<Country> allCountries) {
        Quiz quiz = new Quiz();

        // Shuffle countries for randomization
        Collections.shuffle(allCountries);

        // Pick the first six countries in the shuffle
        List<Country> firstSix = new ArrayList<>(NUM_QUESTIONS);
        firstSix = allCountries.subList(0,6);

        for (Country i : firstSix) {
            String correctCapital = i.getCapital();

            // wrong capitals
            List<String> wrongCapitals = getWrongCapitals(allCountries, correctCapital);

            Question question = new Question( i, correctCapital, wrongCapitals.get(0), wrongCapitals.get(1));

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
