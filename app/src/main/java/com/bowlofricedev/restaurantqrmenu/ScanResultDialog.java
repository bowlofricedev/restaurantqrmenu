package com.bowlofricedev.restaurantqrmenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

public class ScanResultDialog extends AppCompatDialog {
    public ScanResultDialog(@NonNull Context context, @NonNull Result result) {
        super(context, resolveDialogTheme(context));
        setTitle(R.string.scan_result);
        setContentView(R.layout.activity_scan_result_dialog);
        //noinspection ConstantConditions
        ((TextView) findViewById(R.id.result)).setText(result.getText());
        //noinspection ConstantConditions
        ((TextView) findViewById(R.id.format)).setText(String.valueOf(result.getBarcodeFormat()));
        //noinspection ConstantConditions
        findViewById(R.id.copy).setOnClickListener(v -> {
            //noinspection ConstantConditions
            ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE))
                    .setPrimaryClip(ClipData.newPlainText(null, result.getText()));
            Toast.makeText(context, R.string.copied_to_clipboard, Toast.LENGTH_LONG).show();
            dismiss();
        });
        //noinspection ConstantConditions
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //volvemos a inicio
                Intent myIntent = new Intent(getContext(), MainActivity.class);
                getContext().startActivity(myIntent);
            }
        });
//        findViewById(R.id.close).setOnClickListener(v -> dismiss());

        findViewById(R.id.open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //abrimos url en el navegador
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getText()));
//                getContext().startActivity(browserIntent);

                //abrimos en app embedida la url
                Intent intentWB = new Intent(getContext(), WebviewActivity.class);
                intentWB.putExtra("url", result.getText());
                getContext().startActivity(intentWB);
            }
        });


    }

    private static int resolveDialogTheme(@NonNull Context context) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(androidx.appcompat.R.attr.alertDialogTheme, outValue, true);
        return outValue.resourceId;
    }
}