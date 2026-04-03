package edu.uga.cs.countryquiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabaseStart extends ImportAsync<Context, Void> {

    private static final String TAG = "DBInitializer";

    @Override
    protected Void doInBackground(Context... params) {
        Context context = params[0];

        QuizDBHelper dbHelper = QuizDBHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (dbHelper.isCountriesTableEmpty(db)) {
            Log.d(TAG, "Loading CSV into database...");

            try {
                InputStream is = context.getAssets().open("countries.csv");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");

                    String name = parts[0];
                    String capital = parts[1];
                    String continent = parts[2];

                    db.execSQL("INSERT INTO countries (name, capital, continent) VALUES (?, ?, ?)",
                            new Object[]{name, capital, continent});
                }

                reader.close();

            } catch (Exception e) {
                Log.e(TAG, "Error reading CSV", e);
            }
        } else {
            Log.d(TAG, "Database already initialized");
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        Log.d(TAG, "Database initialization complete");
    }
}