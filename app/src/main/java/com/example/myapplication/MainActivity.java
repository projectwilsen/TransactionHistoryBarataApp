package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TransactionHistoryRecyclerViewInterface{

    ArrayList<TransactionHistoryModel> transactionHistoryModel = new ArrayList<>();
    EditText etToken;

//    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        etToken = findViewById(R.id.token);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed");
                            return;
                        }

                        String token = task.getResult();

                        System.out.println(token);
//                        Toast.makeText(MainActivity.this, "Device registration token" + token, Toast.LENGTH_SHORT).show();
//                        etToken.setText(token);
                    }
                });

        RecyclerView recyclerView = findViewById(R.id.tsRecyclerView);

        setUpTransactionHistoryModel();

        TransactionHistoryRecyclerViewAdapter adapter = new TransactionHistoryRecyclerViewAdapter(this,transactionHistoryModel,this);

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

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this,TransactionHistoryDetailActivity.class);

        intent.putExtra("NAME", transactionHistoryModel.get(position).getTransactionHistoryCSName());
        intent.putExtra("LOCATION", transactionHistoryModel.get(position).getTransactionHistoryCSLocation());
        intent.putExtra("STATUS", transactionHistoryModel.get(position).getTransactionHistoryStatus());
        intent.putExtra("DATE", transactionHistoryModel.get(position).getTransactionHistoryDate());
        intent.putExtra("AMOUNT", transactionHistoryModel.get(position).getTransactionHistoryAmount());

        startActivity(intent);
    }
}