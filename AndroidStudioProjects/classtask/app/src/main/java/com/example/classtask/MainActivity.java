package com.example.classtask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

Button myBtn;

TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myBtn = findViewById(R.id.btnadd);
        txt = findViewById(R.id.txt);

        Intent myIntent  = new Intent( this,MainActivity.class);

        myBtn.setText("Ajai Kamaraj");
        txt.setText("best student");
        myBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              startActivity(myIntent);
            }
        });

    }
}