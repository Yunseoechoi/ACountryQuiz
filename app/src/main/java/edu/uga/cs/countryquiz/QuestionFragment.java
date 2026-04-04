package edu.uga.cs.countryquiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class QuestionFragment extends Fragment {

    private static final String ARG_INDEX = "index";
    private Quiz quiz;

    public static QuestionFragment newInstance(int index) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_quiz, container, false);

        int index = getArguments().getInt(ARG_INDEX);
        Question q = quiz.getQuestions().get(index);

        TextView question = view.findViewById(R.id.textView4);
        RadioButton c1 = view.findViewById(R.id.radioButton);
        RadioButton c2 = view.findViewById(R.id.radioButton2);
        RadioButton c3 = view.findViewById(R.id.radioButton3);
        RadioGroup rg = view.findViewById(R.id.radioGroup);

        question.setText("What is the capital of " + q.getCountryName() + "?");

        c1.setText(q.getAnswerChoice(0));
        c2.setText(q.getAnswerChoice(1));
        c3.setText(q.getAnswerChoice(2));

        rg.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selected = view.findViewById(checkedId);
            String answer = selected.getText().toString();

            q.setUserAnswer(answer);
        });

        return view;
    }
}