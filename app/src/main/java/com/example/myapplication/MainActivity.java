package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ArrayList<TransactionHistoryModel> transactionHistoryModel = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.tsRecyclerView);

        setUpTransactionHistoryModel();

        TransactionHistoryRecyclerViewAdapter adapter = new TransactionHistoryRecyclerViewAdapter(this,transactionHistoryModel);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setUpTransactionHistoryModel() {
        String[] transactionHistoryDate = getResources().getStringArray(R.array.transaction_history_date);
        String[] transactionHistoryStatus = getResources().getStringArray(R.array.transaction_history_status);
        String[] transactionHistoryCSName = getResources().getStringArray(R.array.transaction_history_cs_name);
        String[] transactionHistoryCSLocation = getResources().getStringArray(R.array.transaction_history_cs_location);
        String[] transactionHistoryAmount = getResources().getStringArray(R.array.transaction_history_amount);

        SimpleDateFormat parserFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

        // Create a list to hold TransactionHistoryModel objects before sorting
        ArrayList<TransactionHistoryModel> tempList = new ArrayList<>();

        for (int i = 0; i < transactionHistoryCSName.length; i++) {
            try {
                Date date = parserFormatter.parse(transactionHistoryDate[i]);
                String formattedDate = outputFormatter.format(date);

                tempList.add(new TransactionHistoryModel(formattedDate,
                        transactionHistoryStatus[i], transactionHistoryCSName[i],
                        transactionHistoryCSLocation[i], transactionHistoryAmount[i]));
            } catch (ParseException e) {
                e.printStackTrace(); // Handle parsing error
            }
        }

        // Sort the tempList based on the transactionHistoryDate
        Collections.sort(tempList, new Comparator<TransactionHistoryModel>() {
            @Override
            public int compare(TransactionHistoryModel lhs, TransactionHistoryModel rhs) {
                return rhs.getTransactionHistoryDate().compareTo(lhs.getTransactionHistoryDate());
            }
        });

        // Assign the sorted tempList to the transactionHistoryModel
        transactionHistoryModel.clear();
        transactionHistoryModel.addAll(tempList);
    }

}