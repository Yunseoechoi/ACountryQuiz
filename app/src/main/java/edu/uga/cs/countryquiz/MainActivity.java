package edu.uga.cs.countryquiz;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;


/**
 * Displays the app purpose, rules, and buttons.
 * It initalizes the databases on the first app launch.
 */
public class MainActivity extends AppCompatActivity {

    final String TAG = "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "MainActivity Created");

        initalizeDatabase();

        Button startQuizButton = findViewById(R.id.button);
        Button viewResultsButton = findViewById(R.id.button2);

        startQuizButton.setOnClickListener(v -> startQuiz());
        viewResultsButton.setOnClickListener(v -> viewResults());
    }

    private void initalizeDatabase() {
        Log.d(TAG, "Initializing database");

        QuizDBHelper dbHelper = QuizDBHelper.getInstance(this);
    }

    private void startQuiz() {
        Log.d(TAG, "Start button clicked");
        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
        startActivity(intent);
    }

    private void viewResults() {
        Log.d(TAG, "View results button clicked");
        Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
        startActivity(intent);
    }
}