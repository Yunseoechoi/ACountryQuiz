package edu.uga.cs.countryquiz;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    private static final String TAG = "ResultsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        int score = getIntent().getIntExtra("score", 0);
        long dateMillis = getIntent().getLongExtra("date", 0);

        Quiz quiz = new Quiz();
        quiz.setQuizDate(new java.util.Date(dateMillis));
        quiz.setCurrentScore(score);

        displayQuizResult(quiz);

        // pass the LinearLayout where past quizzes will be displayed
        LinearLayout pastLayout = findViewById(R.id.linear_past_quizzes);
        loadPastQuizzes(pastLayout);

        saveQuizToDatabase(quiz);
    }

    private void displayQuizResult(Quiz quiz) {
        TextView scoreView = findViewById(R.id.text_score);
        TextView dateView = findViewById(R.id.text_date);

        scoreView.setText("Score: " + quiz.getCurrentScore() + "/" + quiz.getNumberOfQuestions());
        dateView.setText("Date: " + quiz.getQuizDate().toString());

        Log.d(TAG, "Displayed quiz results: " + quiz);
    }

    private void saveQuizToDatabase(Quiz quiz) {
        Context context = this;

        new ImportAsync<Quiz, Void>() {
            @Override
            protected Void doInBackground(Quiz... quizzes) {
                Quiz q = quizzes[0];
                QuizDBHelper dbHelper = QuizDBHelper.getInstance(context);
                dbHelper.saveQuiz(q);
                Log.d(TAG, "Quiz saved to database: " + q);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                Log.d(TAG, "Quiz saving complete");
            }
        }.execute(quiz);
    }

}