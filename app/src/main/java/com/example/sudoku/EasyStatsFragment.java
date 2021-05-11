package com.example.sudoku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class EasyStatsFragment extends Fragment {

    private TextView textNumberEasy;
    private final String difficulty = "Easy";
    private Button buttonContinueGame;
    private TextView textTimeEasy;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_easy_stats, container, false);
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textNumberEasy = (TextView) view.findViewById(R.id.textNumberEasy);
        textTimeEasy = (TextView) view.findViewById(R.id.textTimeEasy);

        textNumberEasy.setText(String.valueOf(MainActivity.user.getNumberDifficulty(difficulty)));
        textTimeEasy.setText(String.valueOf(MainActivity.user.getTimeDifficulty(difficulty)));

        view.findViewById(R.id.button_back).setOnClickListener(view1 -> NavHostFragment.findNavController(EasyStatsFragment.this)
                .navigate(R.id.action_EasyStatsFragment_to_OptionsFragment));

        buttonContinueGame = (Button) view.findViewById(R.id.button_continueEasy);

        view.findViewById(R.id.button_restartEasy).setOnClickListener(v ->{
            String level = MainActivity.user.status();
            String difficultyM = "";

            if (level.equals("Beginner")){
                difficultyM = "Easy";
            } else if (level.equals("Intermediate")){
                difficultyM = "Medium";
            } else if (level.equals("Advanced")){
                difficultyM= "Hard";
            }
            if (MainActivity.user.getChallengeCompleted().equals("InProgress") && difficultyM.equals(difficulty)){
                MainActivity.user.setChalengeCompleted("Aborted");
            }
            MainActivity.user.deleteSudokuGame(difficulty);
            buttonContinueGame.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(getContext(), SudokuActivity.class);
            intent.putExtra(SudokuActivity.sudokuExtra, difficulty);
            startActivity(intent);
        });
        if(MainActivity.user.checkSudokuGame(difficulty)){
            buttonContinueGame.setVisibility(View.VISIBLE);
        }
        buttonContinueGame.setOnClickListener(v ->{
            Intent intent = new Intent(getContext(), SudokuActivity.class);
            intent.putExtra(SudokuActivity.sudokuExtra, difficulty);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(MainActivity.user.checkSudokuGame(difficulty)){
            buttonContinueGame.setVisibility(View.VISIBLE);
        }
    }
}