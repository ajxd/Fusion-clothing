package com.example.ajai_kamaraj_lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayPyramid();
    }

    private void displayPyramid() {
        for (int i = 1; i <= 5; i++) {
            StringBuilder line = new StringBuilder();
            for (int j = 1; j <= i; j++) {
                line.append("*");
            }
            Log.d("PyramidTest", line.toString());
        }
    }
}
