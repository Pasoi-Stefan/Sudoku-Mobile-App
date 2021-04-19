package com.example.sudoku;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class OptionsFragment extends Fragment {
    private static final String TAG = "OptionsFragment";

    private TextView textNumber;
    private TextView textStatus;
    private TextView textName;
    private TextView textEnterName;
    private TextView textWaring;
    private Button btnDone;
    private Button buttonChangeName;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_options, container, false);
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textNumber = (TextView)view.findViewById(R.id.textNumber);
        textStatus = (TextView)view.findViewById(R.id.textStatus);
        textName = (TextView)view.findViewById(R.id.textName);
        buttonChangeName = (Button) view.findViewById(R.id.buttonChangeName);

        textNumber.setText(String.valueOf(MainActivity.user.getNumber()));
        textStatus.setText(MainActivity.user.status());
        textName.setText(MainActivity.user.getName());

        buttonChangeName.setOnClickListener(v -> {
            View popupView = (View)getLayoutInflater().inflate(R.layout.popup_name, null);

            // create the popup window
            int width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            ConstraintLayout layout = view.findViewById(R.id.constraint);
            layout.setForeground(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.SemiTransparentGray)));
            boolean focusable = true; // lets taps outside the popup also dismiss it
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            btnDone = (Button) popupView.findViewById(R.id.buttonDone);
            textEnterName = (TextView) popupView.findViewById(R.id.textEnterName);
            textWaring = (TextView) popupView.findViewById(R.id.textWarning);
            btnDone.setOnClickListener(v1 -> {
                Log.d(TAG, "onViewCreated: ");
                String userName = textEnterName.getText().toString();
                if (userName.equals("")) {
                    textWaring.setVisibility(View.VISIBLE);
                } else {
                    MainActivity.user.setName(userName);
                    textName.setText(userName);
                    popupWindow.dismiss();
                }
            });

            popupWindow.setOnDismissListener(() -> {
                layout.setForeground(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.TransparentGray)));
            });
        });

        view.findViewById(R.id.button_easy).setOnClickListener(view1 -> NavHostFragment.findNavController(OptionsFragment.this)
                .navigate(R.id.action_OptionsFragment_to_EasyStatsFragment));
        view.findViewById(R.id.button_medium).setOnClickListener(view1 -> NavHostFragment.findNavController(OptionsFragment.this)
                .navigate(R.id.action_OptionsFragment_to_MediumStatsFragment));
        view.findViewById(R.id.button_hard).setOnClickListener(view1 -> NavHostFragment.findNavController(OptionsFragment.this)
                .navigate(R.id.action_OptionsFragment_to_HardStatsFragment));

        view.findViewById(R.id.button_back_main).setOnClickListener(view1 ->{
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        });
    }
}