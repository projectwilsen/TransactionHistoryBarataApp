package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TransactionHistoryDetailDownloadActivity extends AppCompatActivity {

    final static int REQUEST_CODE = 1232;
    ImageButton downloadPDF;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history_detail_download);

        String name = getIntent().getStringExtra("NAME");
        String location = getIntent().getStringExtra("LOCATION");
        String date = getIntent().getStringExtra("DATE");
        String amount = getIntent().getStringExtra("AMOUNT");

        TextView nameTextView = findViewById(R.id.name);
        TextView locationTextView = findViewById(R.id.location);
        TextView dateTextView = findViewById(R.id.date);
        TextView amountTextView = findViewById(R.id.amount);

        nameTextView.setText(name);
        locationTextView.setText(location);
//        statusTextView.setText(status);
        dateTextView.setText(date);
        amountTextView.setText(amount);

        askPermissions();
        downloadPDF = findViewById(R.id.downloadButton);
        downloadPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convertXMLtoPDF(date);
            }
        });
    }

//    private void convertXMLtoPDF(String date) {
//        View view = findViewById(R.id.pdf);
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            this.getDisplay().getRealMetrics(displayMetrics);
//        } else this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//
//        view.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.EXACTLY),
//                View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, View.MeasureSpec.EXACTLY));
//
//
//        view.layout(0,0,displayMetrics.widthPixels, displayMetrics.heightPixels);
//
//        PdfDocument document = new PdfDocument();
//
//        int viewWidth = view.getMeasuredWidth();
//        int viewHeight = view.getMeasuredHeight();
//
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(viewWidth,viewHeight,1).create();
//        PdfDocument.Page page = document.startPage(pageInfo);
//
//        Canvas canvas = page.getCanvas();
//        view.draw(canvas);
//
//        document.finishPage(page);
//
//        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//        String filename = "transaction_" + date + ".pdf";
//        File file = new File(downloadsDir, filename);
//        try{
//            FileOutputStream fos = new FileOutputStream(file);
//            document.writeTo(fos);
//            document.close();
//            fos.close();
//            Toast.makeText(this, "Download PDF Successfully", Toast.LENGTH_SHORT).show();
//            Log.d("mylog","Download Successfully");
//        } catch (FileNotFoundException e) {
//            Log.d("mylog","Error while writing" + e.toString());
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }


//    HANYA PART YANG DIPILIH AJA JADI HASILNYA KECIL ( CONSTRAINT DENGAN ID PDF) - jangan dibuang ini lebih rapi hasilnya, tapi kalo yang atas itu contoh setting ukuran pdf nya. kalo butuh hasilnya harus a4 bisa pakek ide yang atas
    private void convertXMLtoPDF(String date) {

        View pdfContentView = findViewById(R.id.pdf);

        // Measure the dimensions of the PDF content view
        pdfContentView.measure(View.MeasureSpec.makeMeasureSpec(pdfContentView.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(pdfContentView.getHeight(), View.MeasureSpec.EXACTLY));

        int viewWidth = pdfContentView.getMeasuredWidth();
        int viewHeight = pdfContentView.getMeasuredHeight();

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(viewWidth, viewHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        pdfContentView.draw(canvas);

        document.finishPage(page);

        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String filename = "transaction_" + date + ".pdf";
        File file = new File(downloadsDir, filename);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            fos.close();
            Toast.makeText(this, "Download PDF Successfully", Toast.LENGTH_SHORT).show();
            Log.d("mylog", "Download Successfully");
        } catch (FileNotFoundException e) {
            Log.d("mylog", "Error while writing" + e.toString());
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void askPermissions(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }
}