package edu.uga.cs.countryquiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class QuestionFragment extends Fragment {

    private static final String ARG_INDEX = "index";
    public static QuestionFragment newInstance(int index) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_quiz, container, false);
        Quiz quiz = new ViewModelProvider(requireActivity()).get(QuizViewModel.class).quiz;
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

        if (q.getUserAnswer() != null) {
            if (q.getUserAnswer().equals(c1.getText().toString())) c1.setChecked(true);
            else if (q.getUserAnswer().equals(c2.getText().toString())) c2.setChecked(true);
            else if (q.getUserAnswer().equals(c3.getText().toString())) c3.setChecked(true);
        }

        rg.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selected = view.findViewById(checkedId);
            String answer = selected.getText().toString();

            q.setUserAnswer(answer);

            // recalculate score every time an answer is selected
            int score = 0;
            for (Question i : quiz.getQuestions()) {
                if (i.isAnswerCorrect())
                    score++;
            }
            quiz.setCurrentScore(score);
        });

        return view;
    }
}