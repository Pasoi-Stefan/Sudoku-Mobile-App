package com.example.sudoku;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class OptionsFragment extends Fragment {
    private static final String TAG = "OptionsFragment";
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    private TextView textNumber;
    private TextView textStatus;
    private TextView textName;
    private TextView textEnterName;
    private TextView textWaring;
    private Button btnDone;
    private Button buttonChangeName;
    private ImageView imageViewProfile;
    private Button buttonPicture;
    private Button buttonChallenge;

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

        imageViewProfile = (ImageView) view.findViewById(R.id.imageViewProfile);
        Bitmap myBitmap = BitmapFactory.decodeFile(MainActivity.user.getProfilePicture());
        imageViewProfile.setImageBitmap(myBitmap);
        buttonPicture = (Button) view.findViewById(R.id.buttonPicture);

        buttonPicture.setOnClickListener(view1 -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED) {
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissions, PERMISSION_CODE);
                } else {
                    pickImageFromGallery();
                }
            } else {
                pickImageFromGallery();
            }
        });

        buttonChallenge = (Button) view.findViewById(R.id.buttonChallenge);
        buttonChallenge.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), ChallengeActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
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
            getActivity().overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        });
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            //imageViewProfile.setImageURI(data.getData());
            Uri selectedImageUri = data.getData();
            String path = getPath(selectedImageUri);
            Log.d(TAG, "onActivityResult: " + path);
            //File imgFile = new File(selectedImageUri.getPath());
            //Log.d(TAG, "onActivityResult: " + imgFile.toString());
            Bitmap myBitmap = BitmapFactory.decodeFile(path);
            Log.d(TAG, "onActivityResult: " + myBitmap);
            imageViewProfile.setImageBitmap(myBitmap);

            MainActivity.user.setProfilePicture(path);
        }
    }
    public String getPath(Uri uri) {
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            //yourSelectedImage = BitmapFactory.decodeFile(filePath);
            return filePath;
        } catch(Exception e) {
            Log.e("Path Error", e.toString());
            return null;
        }
    }
}