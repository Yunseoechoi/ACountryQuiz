package edu.uga.cs.countryquiz;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class PastResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_results);

        LinearLayout pastLayout = findViewById(R.id.linear_past_quizzes);
        loadPastQuizzes(pastLayout);
    }

    private void loadPastQuizzes(LinearLayout layout) {
        Context context = this;
        new ImportAsync<Void, List<Quiz>>() {
            @Override
            protected List<Quiz> doInBackground(Void... voids) {
                return QuizDBHelper.getInstance(context).getAllQuizzes();
            }
            @Override
            protected void onPostExecute(List<Quiz> quizzes) {
                layout.removeAllViews();
                for (Quiz q : quizzes) {
                    TextView tv = new TextView(context);
                    tv.setText("Date: " + q.getQuizDate() + " | Score: " + q.getCurrentScore() + "/6");
                    layout.addView(tv);
                }
            }
        }.execute();
    }
}