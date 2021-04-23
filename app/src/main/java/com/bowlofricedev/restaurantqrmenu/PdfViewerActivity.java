package com.bowlofricedev.restaurantqrmenu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;


//Activity para visualizar PDF
public class PdfViewerActivity extends AppCompatActivity implements DownloadFile.Listener{

    private static final String TAG = "ActivityPdfViewer";
    private String pdfUrl;
    private PDFPagerAdapter adapter;
    private RemotePDFViewPager remotePDFViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        //recogemos la url del documento
        pdfUrl = getIntent().getStringExtra("url");


        remotePDFViewPager = new RemotePDFViewPager(getApplicationContext(), pdfUrl, this);

    }
    @Override
    public void onSuccess(String url, String destinationPath) {

        //si la descarga del pdf ha ido bien, lo pintamos

        adapter = new PDFPagerAdapter(this, destinationPath);
        remotePDFViewPager.setAdapter(adapter);
        setContentView(remotePDFViewPager);
    }

    @Override
    public void onFailure(Exception e) {
        // Si la descarga del PDF ha ido mal...
        String h = e.getMessage();
        Log.d(TAG, "onLoadFailure: ");
        Toast.makeText(getApplicationContext(), String.valueOf(R.string.downloadPDFerror), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressUpdate(int progress, int total) {

        //Metodo en caso de estar en progreso de la descarga del pdf, util para actualizar la vista
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        adapter.close();
    }
}