package com.bowlofricedev.restaurantqrmenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnScanNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();



    }

    private void initComponents() {

        btnScanNow = findViewById(R.id.btnScanNow);

        btnScanNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(MainActivity.this, CodeScannerActivity.class);
                MainActivity.this.startActivity(myIntent);



            }
        });

    }
}