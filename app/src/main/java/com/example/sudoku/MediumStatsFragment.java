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

public class MediumStatsFragment extends Fragment {
    private TextView textNumberMedium;
    private final String difficulty = "Medium";
    private Button buttonContinueGame;
    private TextView textTimeMedium;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_medium_stats, container, false);
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textNumberMedium = (TextView) view.findViewById(R.id.textNumberMedium);
        textTimeMedium = (TextView) view.findViewById(R.id.textTimeMedium);

        textNumberMedium.setText(String.valueOf(MainActivity.user.getNumberDifficulty(difficulty)));
        textTimeMedium.setText(String.valueOf(MainActivity.user.getTimeDifficulty(difficulty)));

        view.findViewById(R.id.button_back).setOnClickListener(view1 -> NavHostFragment.findNavController(MediumStatsFragment.this)
                .navigate(R.id.action_MediumStatsFragment_to_OptionsFragment));

        buttonContinueGame = (Button) view.findViewById(R.id.button_continueMedium);

        view.findViewById(R.id.button_restartMedium).setOnClickListener(v ->{
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