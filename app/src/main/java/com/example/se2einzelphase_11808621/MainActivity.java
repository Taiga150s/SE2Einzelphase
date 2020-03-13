package com.example.se2einzelphase_11808621;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {


    EditText editText;
    Button btn;
    Button btn2;
    TextView textView;
    TextView textView2;
    TextView textView3;
    String serverAnswer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        btn = (Button) findViewById(R.id.button2);
        btn2 = (Button) findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               sendToServer(editText.getText().toString());


            }
        });
        btn2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

            calculation(editText.getText().toString());
            }
        });
    }

    public void calculation(String number){
       char[] arr = number.toCharArray();
       StringBuilder gerade = new StringBuilder();
       StringBuilder ungerade = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] % 2 == 0) {
                gerade.append(arr[i]);
                gerade.append(" ");
            } else {
                ungerade.append(arr[i]);
                ungerade.append(" ");
            }
        }
        String[] g = gerade.toString().split(" ");
        String[] u = ungerade.toString().split(" ");
        Arrays.sort(g);
        Arrays.sort(u);
        final StringBuilder sorted = new StringBuilder();
        for (int i = 0; i < g.length; i++) {
            sorted.append(g[i]);

        }
        for (int i = 0; i < u.length; i++) {
            sorted.append(u[i]);

        }
        System.out.println(sorted.toString());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView3.setText(sorted.toString());
            }
        });
    }


    public void sendToServer(final String txt) {

        new Thread(new Runnable() {
            @Override
            public void run() {


                try {

                    Socket socket = new Socket("se2-isys.aau.at", 53212);
                    DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
                    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String send = txt;
                    outToServer.writeBytes(send + '\n');
                    serverAnswer = inFromServer.readLine();
                    System.out.println(serverAnswer);
                    socket.close();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView3.setText(serverAnswer);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}

