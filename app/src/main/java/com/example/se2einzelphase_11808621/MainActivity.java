package com.example.se2einzelphase_11808621;

import androidx.appcompat.app.AppCompatActivity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity<name> extends AppCompatActivity {


   private EditText editText;
   private Button btn;
   private TextView textView;
    private TextView textView2;
    private TextView textView3;

    final String ServerURL = "https://se2-isys.aau.at:53212";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        btn = (Button) findViewById(R.id.button2);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internetAvailable()){
                    sendToServer(editText);
                } else {
                    Toast.makeText(getApplicationContext(), "Internet not available", Toast.LENGTH_SHORT).show();
                }
            }

            }
        );
    }
    public void sendToServer(final EditText text){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL serverURL = new URL(ServerURL);
                    HttpURLConnection connection = (HttpURLConnection) serverURL.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
                    outputStreamWriter.write(String.valueOf(text));
                    outputStreamWriter.flush();
                    outputStreamWriter.close();

                    InputStream inputStream = connection.getInputStream();
                   final String answer = serverAnswer(inputStream);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView3.setText(answer);
                        }
                    });
                    inputStream.close();



                }catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }
    public String serverAnswer (InputStream inputStreamServerAnswer){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStreamServerAnswer));
        StringBuilder stringBuilder = new StringBuilder();

        String currentRow;
        try {
            while ((currentRow = reader.readLine()) != null) {
                stringBuilder.append(currentRow);
                stringBuilder.append("\n");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return stringBuilder.toString().trim();
    }

        public boolean internetAvailable(){
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    }






