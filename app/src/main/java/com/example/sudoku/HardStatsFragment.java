package com.example.sudoku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class HardStatsFragment extends Fragment {
    private TextView textNumberHard;
    private final String difficulty = "Hard";
    private Button buttonContinueGame;
    private TextView textTimeHard;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hard_stats, container, false);
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textNumberHard = (TextView) view.findViewById(R.id.textNumberHard);
        textTimeHard = (TextView) view.findViewById(R.id.textTimeHard);

        textNumberHard.setText(String.valueOf(MainActivity.user.getNumberDifficulty(difficulty)));
        textTimeHard.setText(String.valueOf(MainActivity.user.getTimeDifficulty(difficulty)));

        view.findViewById(R.id.button_back).setOnClickListener(view1 -> NavHostFragment.findNavController(HardStatsFragment.this)
                .navigate(R.id.action_HardStatsFragment_to_OptionsFragment));

        buttonContinueGame = (Button) view.findViewById(R.id.button_continueHard);

        view.findViewById(R.id.button_restartHard).setOnClickListener(v ->{
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