package edu.uga.cs.countryquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a SQLiteOpenHelper class, which Android uses to create, upgrade, delete an SQLite database
 * in an app.
 * This class is a singleton, following the Singleton Design Pattern.
 * Only one instance of this class will exist.  To make sure, the
 * only constructor is private.
 * Access to the only instance is via the getInstance method.
 */
public class QuizDBHelper extends SQLiteOpenHelper {
    private static final String DEBUG_TAG = "QuizDBHelper";

    private static final String DB_NAME = "quiz.db";
    private static final int DB_VERSION = 1;

    // Define all names (strings) for table and column names.
    // This will be useful if we want to change these names later.
    public static final String TABLE_COUNTRIES = "countries";
    public static final String COUNTRIES_COLUMN_ID = "_id";
    public static final String COUNTRIES_COLUMN_NAME = "name";
    public static final String COUNTRIES_COLUMN_CAPITAL = "capital";
    public static final String COUNTRIES_COLUMN_CONTINENT = "continent";

    // This is a reference to the only instance for the helper.
    private static QuizDBHelper helperInstance;

    // A Create table SQL statement to create a table for job leads.
    // Note that _id is an auto increment primary key, i.e. the database will
    // automatically generate unique id values as keys.
    private static final String CREATE_COUNTRIES =
            "create table " + TABLE_COUNTRIES + " ("
                    + COUNTRIES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COUNTRIES_COLUMN_NAME + " TEXT, "
                    + COUNTRIES_COLUMN_CAPITAL + " TEXT, "
                    + COUNTRIES_COLUMN_CONTINENT + " TEXT"
                    + ")";

    public static final String TABLE_QUIZZES = "quizzes";
    public static final String QUIZZES_COLUMN_ID = "_id";
    public static final String QUIZZES_COLUMN_DATE = "date";
    public static final String QUIZZES_COLUMN_SCORE = "score";

    // Create table SQL statement for quizzes
    private static final String CREATE_QUIZZES =
            "create table " + TABLE_QUIZZES + " ("
                    + QUIZZES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + QUIZZES_COLUMN_DATE + " TEXT NOT NULL, "
                    + QUIZZES_COLUMN_SCORE + " INTEGER NOT NULL"
                    + ")";

    // Note that the constructor is private
    // So, it can be called only from
    // this class, in the getInstance method.
    private QuizDBHelper( Context context ) {
        super( context, DB_NAME, null, DB_VERSION );
    }

    // Access method to the single instance of the class.
    // It is synchronized, so that only one thread can executes this method, at a time.
    public synchronized static QuizDBHelper getInstance( Context context ) {
        // check if the instance already exists and if not, create the instance
        if( helperInstance == null ) {
            helperInstance = new QuizDBHelper( context.getApplicationContext() );
        }
        return helperInstance;
    }

    // We must override onCreate method, which will be used to create the database if
    // it does not exist yet.
    @Override
    public void onCreate( SQLiteDatabase db ) {
        db.execSQL( CREATE_COUNTRIES );
        Log.d( DEBUG_TAG, "Table " + TABLE_COUNTRIES + " created" );
        db.execSQL( CREATE_QUIZZES );
        Log.d( DEBUG_TAG, "Table " + TABLE_QUIZZES + " created" );
    }

    // We should override onUpgrade method, which will be used to upgrade the database if
    // its version (DB_VERSION) has changed.  This will be done automatically by Android
    // if the version will be bumped up, as we modify the database schema.
    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
        db.execSQL( "drop table if exists " + TABLE_QUIZZES );
        db.execSQL( "drop table if exists " + TABLE_COUNTRIES );
        onCreate( db );
        Log.d( DEBUG_TAG, "Tables are upgraded" );
    }

    public boolean isCountriesTableEmpty(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_COUNTRIES, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count == 0;
    }

    // save the quiz and info
    public void saveQuiz(Quiz quiz) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues quizValues = new ContentValues();
        quizValues.put(QUIZZES_COLUMN_DATE, String.valueOf(quiz.getQuizDate().getTime()));
        quizValues.put(QUIZZES_COLUMN_SCORE, quiz.getCurrentScore());

        long quizId = db.insert(TABLE_QUIZZES, null, quizValues);
        quiz.setQuizId(quizId);
    }

    // grab all the past data
    public List<Quiz> getAllQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id, date, score FROM " + TABLE_QUIZZES + " ORDER BY date DESC", null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(0);
                Date date = new Date(cursor.getLong(1));
                int score = cursor.getInt(2);

                Quiz quiz = new Quiz(id, date);
                quiz.setCurrentScore(score);
                quizzes.add(quiz);
            }
            cursor.close();
        }
        return quizzes;
    }
}
