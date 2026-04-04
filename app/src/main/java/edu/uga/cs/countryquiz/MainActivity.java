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
    private Button startQuizButton;
    private Button viewResultsButton;
    private boolean dbReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "MainActivity Created");
        startQuizButton = findViewById(R.id.button);
        viewResultsButton = findViewById(R.id.button2);

        startQuizButton.setEnabled(true);
        startQuizButton.setOnClickListener(v -> {
            if (dbReady) StartQuiz();
            else Log.w(TAG, "Database not ready yet!");
        });

        viewResultsButton.setOnClickListener(v -> viewResults());


        initializeDatabase();
    }

    private void initializeDatabase() {
        Log.d(TAG, "Initializing database");
        new DatabaseStart() {
            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                dbReady = true;  // now database is ready
                Log.d(TAG, "Database ready");
            }
        }.execute(this);

    }

    private void StartQuiz() {
        Log.d(TAG, "Start button clicked");
        Intent intent = new Intent(MainActivity.this, StartQuiz.class);
        startActivity(intent);

    }

    private void viewResults() {
        Log.d(TAG, "View results button clicked");
        Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
        startActivity(intent);
    }

}