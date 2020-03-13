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

