package com.example.kinso.testmarket;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by kinso on 2019-02-19.
 */

public class NewClient extends AppCompatActivity {
    private String html = "";
    private Handler mHandler;
    private Socket socket;
    private String name;
    private BufferedReader networkReader;
    private BufferedWriter networkWriter;
    private String ip ="192.168.0.6";
    private int port =9999;
    @Override
    protected void onStop(){
        super.onStop();
        try{
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        final EditText ed = (EditText)findViewById(R.id.ed);
        Button btn = (Button)findViewById(R.id.btn);
        final TextView tv = (TextView)findViewById(R.id.tv);
        mHandler = new Handler();
        checkUpdate.start();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ed.getText().toString()!=null || !ed.getText().toString().equals("")){
                    PrintWriter out = new PrintWriter(networkWriter, true);
                    String return_msg = ed.getText().toString();
                    out.println(return_msg);

                }
            }
        });

    }

    private Thread checkUpdate = new Thread(){
        public void run(){
            try{
                setSocket(ip,port);
            }catch (IOException e){
                e.printStackTrace();
            }
            try{
                String line;
                Log.w("ChattingStart","Start Thread");
                while(true){
                    Log.w("ChattingStart", "chatting is running");
                    line = networkReader.readLine();
                    html = line;
                    mHandler.post(showUpdate);
                }

            }catch (Exception e){}
        }
    };
    private Runnable showUpdate = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(NewClient.this, "Coming word : " + html, Toast.LENGTH_SHORT).show();
        }
    };
    public void setSocket(String ip, int port) throws IOException{
        try{
            socket = new Socket(ip,port);
            networkWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            networkReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        }catch (IOException e){
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
