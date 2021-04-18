package com.bowlofricedev.restaurantqrmenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bowlofricedev.restaurantqrmenu.beans.Enlace;
import com.bowlofricedev.restaurantqrmenu.tools.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private Button btnScanNow, btnDisplayList;

    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();


    }

    private void initComponents() {

        mDatabaseHelper = new DatabaseHelper(this);

        btnScanNow = findViewById(R.id.btnScanNow);
        btnDisplayList = findViewById(R.id.btnDisplayListMain);

        btnScanNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //abrimos scanner
                Intent myIntent = new Intent(MainActivity.this, CodeScannerActivity.class);
                MainActivity.this.startActivity(myIntent);

                //abrimos webview
//                Intent myIntent2 = new Intent(MainActivity.this, WebviewActivity.class);
//                MainActivity.this.startActivity(myIntent2);

            }
        });


        btnDisplayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //crear en db
                //TODO: AÑADIR SOLO SI NO ESTÁ YA CREADO
//                Enlace enlace = new Enlace(1, "google", "https://www.google.es", "URL", "n");
//                AddData(enlace);

                Intent intentLista = new Intent(MainActivity.this, ListDataActivity.class);
                MainActivity.this.startActivity(intentLista);

            }
        });

    }


    public void AddData(Enlace newEnlace) {
        boolean insertData = mDatabaseHelper.addData(newEnlace);

        if (insertData) {
            toastMessage("OK");



        } else {
            toastMessage("ERROR");
        }

    }

    //toast message
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}