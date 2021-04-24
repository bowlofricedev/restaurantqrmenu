package com.bowlofricedev.restaurantqrmenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.AndroidRuntimeException;
import android.view.View;
import android.view.contentcapture.DataRemovalRequest;
import android.widget.Button;
import android.widget.Toast;

import com.bowlofricedev.restaurantqrmenu.beans.Enlace;
import com.bowlofricedev.restaurantqrmenu.tools.DatabaseHelper;
import com.budiyev.android.codescanner.CodeScanner;

import java.util.ArrayList;

public class CodeScannerActivity extends AppCompatActivity {
    private static final int RC_PERMISSION = 10;
    private CodeScanner mCodeScanner;
    private boolean mPermissionGranted;
    private Button btnDisplayList;
    DatabaseHelper mDatabaseHelper;

    //TODO: PERSONALIZAR VISTA LAND - HORIZONTAL

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_scanner);

        initComponents();

        mCodeScanner = new CodeScanner(this, findViewById(R.id.scanner));
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {

            //lo cambiamos a que cuando tenga el result lo guarde en bd y se abra

            //guardamos en bbdd
            //preparamos el enlace que acabamos de crear
            Enlace enlace = new Enlace(result.getText(), result.getText(), "HTTP", "n", System.currentTimeMillis());

            if (result.getText().contains(".pdf")) {

                enlace.setType("PDF");

            }
            mDatabaseHelper = new DatabaseHelper(getApplicationContext());
            Enlace enlaceRepetido = checkRepetido(result.getText());
            if (enlaceRepetido == null) {
                //inicializamos db

                boolean insertData = mDatabaseHelper.addData(enlace);

                if (insertData) {

                    //si se ha insertado bien lo abrimos
                    //si contiene .pdf, abrimos pdfviewer
                    if (enlace.getType().equals("PDF")) {

                        openPdf(enlace.getUrl());

                    } else {
                        //si no contiene .pdf abrimos WB
                        openLink(enlace.getUrl());

                    }

                } else {

                }
            } else {
                boolean updateEnlace = mDatabaseHelper.updateEnlace(enlaceRepetido.getId(), enlaceRepetido);

                if (updateEnlace) {

                    //si se ha insertado bien lo abrimos
                    //si contiene .pdf, abrimos pdfviewer
                    if (enlaceRepetido.getType().equals("PDF")) {

                        openPdf(enlaceRepetido.getUrl());

                    } else {
                        //si no contiene .pdf abrimos WB
                        openLink(enlaceRepetido.getUrl());

                    }

                } else {

                }
            }
        }));
        mCodeScanner.setErrorCallback(error -> runOnUiThread(
                () -> Toast.makeText(this, getString(R.string.scanner_error, error), Toast.LENGTH_LONG).show()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = false;
                requestPermissions(new String[]{Manifest.permission.CAMERA}, RC_PERMISSION);
            } else {
                mPermissionGranted = true;
            }
        } else {
            mPermissionGranted = true;
        }


    }

    private Enlace checkRepetido(String url) {
        Enlace repetido = null;
        Cursor data = mDatabaseHelper.getData(DatabaseHelper.COL1, "ASC");


        while (data.moveToNext()) {


            if (data.getString(2).equalsIgnoreCase(url)) {
                repetido = new Enlace();

                repetido.setId(data.getInt(0));
                repetido.setName(data.getString(1));
                repetido.setUrl(data.getString(2));
                repetido.setType(data.getString(3));
                repetido.setFav(data.getString(4));
                repetido.setTimeMillis(System.currentTimeMillis());
            }

        }

        return repetido;

    }

    private void openPdf(String url) {

        Intent intentPDF = new Intent(getBaseContext(), PdfViewerActivity.class);
        intentPDF.putExtra("url", url);
        try {
            getApplicationContext().startActivity(intentPDF);
        } catch (AndroidRuntimeException e) {
            intentPDF.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intentPDF);
        }

    }

    //metodo que abre en webview el enlace
    private void openLink(String url) {
        Intent intentWB = new Intent(getBaseContext(), WebviewActivity.class);
        intentWB.putExtra("url", url);
        try {
            getApplicationContext().startActivity(intentWB);
        } catch (AndroidRuntimeException e) {
            intentWB.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intentWB);
        }

    }

    private void initComponents() {

        btnDisplayList = findViewById(R.id.btnDisplayList);
        btnDisplayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentLista = new Intent(CodeScannerActivity.this, ListDataActivity.class);
                CodeScannerActivity.this.startActivity(intentLista);

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == RC_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = true;
                mCodeScanner.startPreview();
            } else {
                mPermissionGranted = false;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPermissionGranted) {
            mCodeScanner.startPreview();
        }
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}