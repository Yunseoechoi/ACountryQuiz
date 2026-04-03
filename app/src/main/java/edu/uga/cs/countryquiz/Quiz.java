package edu.uga.cs.countryquiz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Quiz{
    private long quizId;
    private List<Question> questions;
    private int currentScore;
    private int questionsAnswered;
    private Date quizDate;
    private static final int NUMBER_OF_QUESTIONS = 6;

    public Quiz() {
        this.quizId = -1; // not saved yet
        this.questions = new ArrayList<>();
        this.currentScore = 0;
        this.questionsAnswered = 0;
        this.quizDate = new Date();

    }

    public void setCurrentScore(int score) {
        this.currentScore = score;
    }

    public Quiz(long quizId, Date quizDate) {
        this.quizId = quizId;
        this.questions = new ArrayList<>();
        this.currentScore = 0;
        this.questionsAnswered = 0;
        this.quizDate = quizDate;
    }

    public long getQuizId() {
        return quizId;
    }

    public void setQuizId(long quizId) {
        this.quizId = quizId;
    }
    public List<Question> getQuestions() {
        return questions;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getQuestionsAnswered() {
        return questionsAnswered;
    }

    public Date getQuizDate() {
        return quizDate;
    }

    public void setQuizDate(Date quizDate) {
        this.quizDate = quizDate;
    }

    public int getNumberOfQuestions() {
        return NUMBER_OF_QUESTIONS;
    }

    @Override
    public String toString()
    {
        return  "quizId: " + quizId + " questionsAnswered: " + questionsAnswered
                + " currentScore: " + currentScore + " quizDate: " + quizDate;
    }
}
