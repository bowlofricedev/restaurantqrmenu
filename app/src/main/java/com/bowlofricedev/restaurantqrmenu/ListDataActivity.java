package com.bowlofricedev.restaurantqrmenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bowlofricedev.restaurantqrmenu.adapter.AdapterEnlacesSwipe;
import com.bowlofricedev.restaurantqrmenu.beans.Enlace;
import com.bowlofricedev.restaurantqrmenu.tools.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nambimobile.widgets.efab.FabOption;

import java.util.ArrayList;

public class ListDataActivity extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";
    DatabaseHelper mDatabaseHelper;
    private RecyclerView rvEnlaces;
    private RecyclerView.Adapter adapter;
    private TextView emptyEnlaces;

    private FloatingActionButton fab, fab2, fab3;
    private boolean isFABOpen = false;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private boolean isfab3Open = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);

        initComponents();

        rvEnlaces = (RecyclerView) findViewById(R.id.rvEnlaces);
        emptyEnlaces = findViewById(R.id.emptyEnlaces);
        mDatabaseHelper = new DatabaseHelper(this);

        fab = findViewById(R.id.fab);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFAB();
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                animateFab3();
                Toast toast1 = Toast.makeText(getApplicationContext(), "HISTORY CLEARED", Toast.LENGTH_SHORT);
                toast1.show();
                Boolean clearedHistory = mDatabaseHelper.deleteAllRecords();
                if (clearedHistory) {
                    goToScan();

                }else{
                    Toast toast2 = Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT);
                    toast2.show();
                }


            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                animateFab3();
                Toast toast1 = Toast.makeText(getApplicationContext(), "NEW SCAN", Toast.LENGTH_SHORT);
                toast1.show();
//                goToScan();
                finish();

            }
        });

        populateRV();

    }

    private void initComponents() {

    }

    private void goToScan() {

        Intent intentScanner = new Intent(getApplicationContext(), CodeScannerActivity.class);
        startActivity(intentScanner);
    }

    private void populateRV() {
        //recogemos datos y llenamos la lista
        Cursor data = mDatabaseHelper.getData();
        ArrayList<Enlace> enlacesList = new ArrayList<>();

        while (data.moveToNext()) {

            Enlace enlace = new Enlace();

            enlace.setId(data.getInt(0));
            enlace.setName(data.getString(1));
            enlace.setUrl(data.getString(2));
            enlace.setType(data.getString(3));
            enlace.setFav(data.getString(4));

            enlacesList.add(enlace);

        }
        //creamos listadapter
//        ListAdapter adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);

        //Enlace a mano
//        Enlace prueba = new Enlace("google", "https://www.google.es", "URL", "n");
//        enlacesList.add(prueba);

        if (enlacesList.size() > 0) {
            adapter = new AdapterEnlacesSwipe(getApplicationContext(), enlacesList);
            rvEnlaces.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rvEnlaces.setAdapter(adapter);
            emptyEnlaces.setVisibility(View.INVISIBLE);
        } else {
            emptyEnlaces.setVisibility(View.VISIBLE);
        }


    }


    private void animateFab3() {
        if (isfab3Open) {
            //  fab1.startAnimation(rotate_backward);
            fab3.startAnimation(fab_close);
            fab3.setClickable(false);
            isfab3Open = false;
        } else {
            //   fab1.startAnimation(rotate_forward);
            fab3.startAnimation(fab_open);
            fab3.setClickable(true);
            isfab3Open = true;
        }
    }

    public void animateFAB() {

        if (isFABOpen && isfab3Open) {

            fab.startAnimation(rotate_backward);
            fab2.startAnimation(fab_close);
            fab2.setClickable(false);
            isFABOpen = false;
            fab3.startAnimation(fab_close);
            fab3.setClickable(false);
            isfab3Open = false;

        } else {

            fab.startAnimation(rotate_forward);
            fab2.startAnimation(fab_open);
            fab2.setClickable(true);
            isFABOpen = true;
            fab3.startAnimation(fab_open);
            fab3.setClickable(true);
            isfab3Open = true;

        }

//        animateFab3();

    }
}