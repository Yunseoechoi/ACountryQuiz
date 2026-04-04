package edu.uga.cs.countryquiz;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/* Adapter for swiping questions */
public class QuizPagerAdapter extends FragmentStateAdapter {

    private Quiz quiz;

    public QuizPagerAdapter(@NonNull FragmentActivity fa, Quiz quiz) {
        super(fa);
        this.quiz = quiz;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        QuestionFragment fragment = QuestionFragment.newInstance(position);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return quiz.getNumberOfQuestions();
    }
}
