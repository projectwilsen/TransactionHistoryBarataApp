package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements TransactionHistoryRecyclerViewInterface{
    private TransactionHistoryRecyclerViewAdapter adapter;
    ArrayList<TransactionHistoryModel> transactionHistoryModel = new ArrayList<>();
    EditText etToken;
    SearchView searchView;

    String[] status = {"Semua Status","Selesai","Belum Selesai"};
    String[] location = {"Semua Lokasi", "Surabaya", "Jakarta"};
    String[] date = {"Semua Tanggal", "7 Hari Terakhir", "30 Hari Terakhir", "90 Hari Terakhir"};
    AutoCompleteTextView statusAutoCompleteTxt, locationAutoCompleteTxt, dateAutoCompleteTxt;
    ArrayAdapter<String> adapterItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ============ SEARCH VIEW ===========
        searchView = findViewById(R.id.searchview);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText,"name","search");
                return true;
            }
        });

//        ==================================


        statusAutoCompleteTxt = findViewById(R.id.status_auto_complete_txt);
        setupAutoCompleteTextView(statusAutoCompleteTxt, status, "status");

        locationAutoCompleteTxt = findViewById(R.id.location_auto_complete_txt);
        setupAutoCompleteTextView(locationAutoCompleteTxt, location, "location");

        dateAutoCompleteTxt = findViewById(R.id.date_auto_complete_txt);
        setupAutoCompleteTextView(dateAutoCompleteTxt, date, "date");


//        ============ FCM ===========

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

//        ==================================

        RecyclerView recyclerView = findViewById(R.id.tsRecyclerView);

        setUpTransactionHistoryModel();

        adapter = new TransactionHistoryRecyclerViewAdapter(this,transactionHistoryModel,this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupAutoCompleteTextView(AutoCompleteTextView autoCompleteTextView, String[] data, final String filterType) {
        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(this, R.layout.filter_list, data);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                filterList(item, filterType, "dropdown");
            }
        });
    }


    private void filterList(String text, String what, String type) {
        ArrayList<TransactionHistoryModel> filteredList = new ArrayList<>();

        for (TransactionHistoryModel item : transactionHistoryModel) {
            boolean isMatch = false;

            if (text.toLowerCase().contains("semua")){
                filteredList.add(item);
            }

            if (type.equals("search")) {
                isMatch = item.transactionHistoryCSName.toLowerCase().contains(text.toLowerCase());
            } else if (type.equals("dropdown")) {
                if (what.equals("name")) {
                    isMatch = item.transactionHistoryCSName.toLowerCase().equals(text.toLowerCase());
                } else if (what.equals("status")) {
                    isMatch = item.transactionHistoryStatus.toLowerCase().equals(text.toLowerCase());
                } else if (what.equals("location")) {
                    isMatch = item.transactionHistoryCSLocation.toLowerCase().contains(text.toLowerCase());
                } else if (what.equals("date")) {
                    Calendar currentDate = Calendar.getInstance();

                    // Get the date from the transaction history model and parse it
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                    Date transactionDate;

                    try {
                        transactionDate = dateFormatter.parse(item.getTransactionHistoryDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        continue; // Skip this item if date parsing fails
                    }

                    // Calculate the difference in days between the current date and the transaction date
                    long diffInDays = TimeUnit.MILLISECONDS.toDays(currentDate.getTimeInMillis() - transactionDate.getTime());

                    // Check if the transaction date falls within the selected period (e.g., last 30 days)
                    if (text.equalsIgnoreCase("7 Hari Terakhir") && diffInDays <= 7){
                        isMatch = true;
                    } else if (text.equalsIgnoreCase("30 Hari Terakhir") && diffInDays <= 30) {
                        isMatch = true;
                    } else if (text.equalsIgnoreCase("90 Hari Terakhir") && diffInDays <= 90) {
                        isMatch = true;
                    }
                }
            }

            if (isMatch) {
                filteredList.add(item);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(this, "Total " + filteredList.size() + " data", Toast.LENGTH_SHORT).show();
            adapter.setFilteredList(filteredList);
        }
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