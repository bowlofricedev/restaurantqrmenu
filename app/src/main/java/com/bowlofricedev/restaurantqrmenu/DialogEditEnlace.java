package com.bowlofricedev.restaurantqrmenu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;


import com.bowlofricedev.restaurantqrmenu.beans.Enlace;
import com.bowlofricedev.restaurantqrmenu.databinding.DialogEditEnlaceBinding;
import com.bowlofricedev.restaurantqrmenu.tools.DatabaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class DialogEditEnlace {

    private Context context;
    private static final String TAG = "DialogEditEnlace  ";
    private Enlace enlaceDatos;
    Dialog dialog;
    private BottomSheetDialog bottomSheetDialogTitulo, bottomSheetDialogDescripcion, bottomSheetDialogURL, bottomSheetDialogImageURL;
    DialogEditEnlaceBinding binding;
    private ImageButton btnDelete, btnSaveEnlace, btnCancel;
    private TextView txtNombreEnlaceEdit, txtUrlEnlaceEdit, txtTypeEnlace;
    private DatabaseHelper mDatabaseHelper;


    public DialogEditEnlace(Context context, Enlace enlace) {
        this.context = context;
        this.enlaceDatos = enlace;
        initComponents();
    }


    public void initComponents() {

        dialog = new Dialog(context);
        binding = DataBindingUtil.inflate(dialog.getLayoutInflater(), R.layout.dialog_edit_enlace, null, false);


        btnSaveEnlace = binding.btnSaveEnlace;
        btnCancel = binding.btnCreateCancelEnlace;
        btnDelete = binding.btnDeleteEnlace;
        txtNombreEnlaceEdit = binding.txtNombreEnlaceEdit;
        txtTypeEnlace = binding.txtTipoUrlEdit;
        txtUrlEnlaceEdit = binding.txtUrlEnlaceEdit;


        txtNombreEnlaceEdit.setText(enlaceDatos.getName());
        txtTypeEnlace.setText(enlaceDatos.getType());
        txtUrlEnlaceEdit.setText(enlaceDatos.getUrl());

        mDatabaseHelper = new DatabaseHelper(context);
        initialize();
    }

    public void initialize() {

        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR); // before
        dialog.setContentView(binding.getRoot());


        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);


        txtNombreEnlaceEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialogDescripcion = new BottomSheetDialog(context);
                showEditUrlNameDialog();

            }
        });


        btnSaveEnlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean ok = mDatabaseHelper.updateEnlace(enlaceDatos.getId(), enlaceDatos);

                //TODO: hacer mas limpio
                if (ok) {
                    Toast toast1 = Toast.makeText(context, "update successful", Toast.LENGTH_SHORT);
                    toast1.show();

                    updateView();

                } else {
                    Toast toast1 = Toast.makeText(context, "update ERROR", Toast.LENGTH_SHORT);
                    toast1.show();
                }


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new AlertDialog.Builder(context)
                        .setIcon(R.drawable.ic_baseline_delete_24)
                        .setTitle("Eliminar Enlace")
                        .setMessage("¿Estas seguro de eliminar el enlace: '" + enlaceDatos.getName() + "'?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO:  deleteEnlace(enlaceDatos);
                                boolean ok = mDatabaseHelper.deleteEnlace(enlaceDatos.getId());
                                //TODO: hacer mas limpio
                                if (ok) {
                                    Toast toast1 = Toast.makeText(context, "delete successful", Toast.LENGTH_SHORT);
                                    toast1.show();

                                    updateView();

                                } else {
                                    Toast toast1 = Toast.makeText(context, "delete ERROR", Toast.LENGTH_SHORT);
                                    toast1.show();
                                }
                            }

                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();

            }
        });


        dialog.show();
    }

    private void updateView() {

        Intent intentLista = new Intent(context, ListDataActivity.class);
        context.startActivity(intentLista);
        ((Activity) context).finish();

    }

    private void showEditUrlNameDialog() {

        @SuppressLint("InflateParams") View view = dialog.getLayoutInflater().inflate(R.layout.layout_edicion_nombre_enlace, null);

        //listener del boton cancelar
        ((View) view.findViewById(R.id.btn_cancelEditURL)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogURL.dismiss();
            }
        });

        //listener del boton actualizar
        ((View) view.findViewById(R.id.btn_saveEditURL)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //capturamos el nombre del et y lo pasamos al updateName
                EditText ed_EditURL = view.findViewById(R.id.ed_EditURL);

                if (TextUtils.isEmpty(ed_EditURL.getText().toString())) {
                    Toast.makeText(context, "No se ha introducido un nombre válido", Toast.LENGTH_SHORT).show();

                } else {

                    binding.txtNombreEnlaceEdit.setText(ed_EditURL.getText());
//                    enlaceDatos.setName(ed_EditURL.getText().toString());
                    enlaceDatos.setName(ed_EditURL.getText().toString());

                    bottomSheetDialogURL.dismiss();


                }

            }
        });

        bottomSheetDialogURL = new BottomSheetDialog(context);
        bottomSheetDialogURL.setContentView(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Objects.requireNonNull(bottomSheetDialogURL.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        bottomSheetDialogURL.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomSheetDialogURL = null;
            }
        });

        bottomSheetDialogURL.show();

    }


    //metodo que nos devuelve fecha formateada
    private String getDateFromMillis(Long millisDate, String dateFormat) {


        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisDate);
        return formatter.format(calendar.getTime());

    }


}
