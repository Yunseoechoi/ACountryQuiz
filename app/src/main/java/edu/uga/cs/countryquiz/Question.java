package edu.uga.cs.countryquiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class (a POJO) represents a single question, including the country, the correct capital,
 * and 2 incorrect capitals.
 * The answers are randomized for display
 */
public class Question{
    private Country country;
    private String correctCapital;
    private List<String> answerChoices; // All answer choices
    private String userAnswer;
    private int score; // 0 or 1 pt for each question

    public Question(Country country, String correctCapital, String wrongCapital1, String wrongCapital2 ) {
        this.country = country;
        this.correctCapital = correctCapital;
        this.userAnswer = null;
        this.score = 0;

        // Create the answer choices and randomize
        this.answerChoices = new ArrayList<>();
        this.answerChoices.add(correctCapital);
        this.answerChoices.add(wrongCapital1);
        this.answerChoices.add(wrongCapital2);
        Collections.shuffle(this.answerChoices);
    }

    public Country getCountry()
    {
        return country;
    }

    public String getCountryName()
    {
        return country.getName();
    }

    public String getCorrectCapital()
    {
        return correctCapital;
    }

    public String getUserAnswer()
    {
        return userAnswer;
    }

    public List<String> getAnswerChoices()
    {
        return answerChoices;
    }

    public int getScore()
    {
        return score;
    }

    public String getCapital() {
        return country.getCapital();
    }

    public String getContinent() {
        return country.getContinent();
    }

    public boolean isCorrect() {
        return isAnswerCorrect();
    }

    // get the userAnswer
    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
        // set the score on accuracy
        if (userAnswer != null && userAnswer.equals(correctCapital))
            this.score = 1;
        else
            this.score = 0;
    }

    // Check if the userAnswer is correct
    public boolean isAnswerCorrect() {
        return userAnswer != null && userAnswer.equals(correctCapital);
    }

    // Get the answer choices at a specific position
    public String getAnswerChoice(int index) {
        if (index > -1 && index < answerChoices.size())
            return answerChoices.get(index);
        return null;
    }

    @Override
    public String toString()
    {
        return  "Country: " + country.getName() + " " + "correctCapital: " + correctCapital + " "
                + "userAnswer: " + userAnswer + " " + "score: " + score;
    }
}
