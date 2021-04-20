package com.bowlofricedev.restaurantqrmenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);

        initComponents();

        rvEnlaces = (RecyclerView) findViewById(R.id.rvEnlaces);
        emptyEnlaces = findViewById(R.id.emptyEnlaces);
        mDatabaseHelper = new DatabaseHelper(this);

        populateRV();

    }

    private void initComponents() {





    }



    private void populateRV() {
        //recogemos datos y llenamos la lista
        Cursor data = mDatabaseHelper.getData();
        ArrayList<Enlace> enlacesList = new ArrayList<>();

        while(data.moveToNext()){

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

        if(enlacesList.size() > 0){
            adapter = new AdapterEnlacesSwipe(getApplicationContext(), enlacesList);
            rvEnlaces.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rvEnlaces.setAdapter(adapter);
            emptyEnlaces.setVisibility(View.INVISIBLE);
        }else{
            emptyEnlaces.setVisibility(View.VISIBLE);
        }



    }
}