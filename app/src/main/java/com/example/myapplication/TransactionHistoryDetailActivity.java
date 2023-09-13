package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class TransactionHistoryDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history_detail);

        String name = getIntent().getStringExtra("NAME");
        String location = getIntent().getStringExtra("LOCATION");
        String status = getIntent().getStringExtra("STATUS");
        String date = getIntent().getStringExtra("DATE");
        String amount = getIntent().getStringExtra("AMOUNT");

        TextView nameTextView = findViewById(R.id.name);
        TextView locationTextView = findViewById(R.id.location);
        TextView statusTextView = findViewById(R.id.status);
        TextView dateTextView = findViewById(R.id.date);
        TextView amountTextView = findViewById(R.id.amount);

        nameTextView.setText(name);
        locationTextView.setText(location);
        statusTextView.setText(status);
        dateTextView.setText(date);
        amountTextView.setText(amount);

    }
}