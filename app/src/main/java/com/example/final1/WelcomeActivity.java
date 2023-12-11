package com.example.final1;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.widget.Spinner;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    Button predictButton;
    TextView textViewData;

    Spinner spinnerSelection;


    public int responseCode = 0;
    public String responseMessage = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        textViewData = findViewById(R.id.textViewData);
        spinnerSelection = findViewById(R.id.spinnerSelection);
        predictButton = findViewById(R.id.predict);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.selection_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelection.setAdapter(adapter);

        predictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selection = spinnerSelection.getSelectedItem().toString();
                if (selection.equals("QQQ")) {
                    makeApiCall("http://54.227.31.155:5000/get_qqq_reg"); // Replace with actual QQQ API
                } else if (selection.equals("SPY")) {
                    makeApiCall("http://54.227.31.155:5000/get_data"); // Replace with actual SPY API
                }
            }
        });

//        predictButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                makeApiCall("http://54.227.31.155:5000/get_data");
//                Toast.makeText(WelcomeActivity.this, "Button Clicked", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void makeApiCall(String urlString) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                String response = null;
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    responseCode = urlConnection.getResponseCode();
                    responseMessage = urlConnection.getResponseMessage();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();
                        response = stringBuilder.toString();
                    } else {
                        response = "Error: " + responseMessage;
                    }
                } catch (Exception e) {
                    response = "Exception: " + e.getMessage();
                } finally {
                    String finalResponse = response;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // Update UI with response
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                textViewData.setText(finalResponse);
                                Toast.makeText(WelcomeActivity.this, "Request Successful", Toast.LENGTH_LONG).show();
                            } else {
                                textViewData.setText(finalResponse);
                                Toast.makeText(WelcomeActivity.this, "Request Failed: " + responseMessage, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }


}

