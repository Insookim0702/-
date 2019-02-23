package com.example.kinso.testmarket;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class Chat1 extends AppCompatActivity {
    TextView txtMessage;
    Button btnConnect, btnSend;
    EditText editIp, editPort, editMessage;
    Handler msgHandler;
    SocketClient client;
    ReceiveThread  receive;
    SendThread send;
    Socket socket;
    Context context;
    String mac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat1);

        context = this;
        editIp = (EditText)findViewById(R.id.editIp);
        editPort = (EditText)findViewById(R.id.editPort);
        editMessage = (EditText)findViewById(R.id.editMessage);
        btnConnect = (Button)findViewById(R.id.btnConnect);
        btnSend = (Button)findViewById(R.id.btnSend);
        txtMessage = (TextView)findViewById(R.id.txtMessage);
        //핸들러 작성
        msgHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what ==1111){
                    //채팅서버로부터 수신한 메시지를 텍스트뷰에 추가
                    txtMessage.append(msg.obj.toString()+"\n");
                }
            }
        };
        //서버에 접속하는 버튼
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client = new SocketClient(editIp.getText().toString(), editPort.getText().toString());
                client.start();
            }
        });
        //메시지 전송 버튼
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editMessage.getText().toString();
                if(message!=null || !message.equals("")){
                    send = new SendThread(socket);
                    send.start();
                    editMessage.setText("");
                }
            }
        });
    }

    //내부 클래스
    class SocketClient extends Thread{
        boolean threadAlive;
        String ip;
        int port;

        OutputStream outputStream =null;
        BufferedReader br = null;
        DataOutputStream output = null;
        public SocketClient(String ip, String port){
            threadAlive =true;
            this.ip = ip;
            this.port = Integer.parseInt(port);
        }

        public void run(){
            try{
                System.out.println("SocketClient 시작");
                System.out.println("SocketClient가 받은 IP : " + ip);
                System.out.println("SocketClient가 받은 IP : " + port);
                //채팅서버에 접속
                //InetAddress serverAddr = InetAddress.getByAddress(ip);
                socket = new Socket(ip, port);

                System.out.println(socket+"소켓 생성 완료!");
                //서버에 메시지를 전달하기 위한 스트림 생성
                output = new DataOutputStream(socket.getOutputStream());
                //메시지 수신용 스레드 생성
                receive = new ReceiveThread(socket);
                receive.start();
                //와이파이 정보 관리자 객체로부터 폰의 mac address를 가져와서 채팅서버에 전달한다.
                WifiManager mng = (WifiManager)context.getSystemService(WIFI_SERVICE);
                WifiInfo info = mng.getConnectionInfo();
                mac = info.getMacAddress();// 식별하기 위한 것
                System.out.println("내 핸드폰 WIFI의 물리 주소 : " + mac);
                output.writeUTF(mac);//물리주소를 서버로 전송
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }//end of SocketClient
    //내부 클래스
    class ReceiveThread extends Thread{ //서버로부터 받은 메시지를 다루는 클래스(수신용 스레드)
        Socket socket =null;
        DataInputStream input = null;
        public ReceiveThread(Socket socket){
            this.socket = socket;
            try{
                //채팅서버로부터 메시지를 받기 위한 스트림 생성
                input = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            System.out.println("ReceiveThread 시작");
            try{
                while(input !=null){
                    String msg = input.readUTF();//서버에서 받은 메시지
                    System.out.println("서버에서 받은 inputStream에 는 값이 있었다. : " + msg);
                    if(msg !=null){
                        //헨들러에게 전달할 메시지 객체
                        Message hdmsg = msgHandler.obtainMessage();
                        System.out.println("ReceiveThread에서 Handler를 통해 나타내려는 서버에서 받은 msg : " + hdmsg);
                        hdmsg.what=1111;//메시지 식별자
                        hdmsg.obj =msg;//메시지의 본문
                        //핸들러에게 메시지 전달(화면 변경 요청)
                        msgHandler.sendMessage(hdmsg);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }//end of Receive
    //내부클래스

    class SendThread extends Thread{
        Socket socket;
        String sendmsg = editMessage.getText().toString();
        DataOutputStream output;
        public SendThread(Socket socket){
            this.socket = socket;
            try{
                //채팅 서버로 메시지를 보내기 위한 스트림 생성
                output = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void run(){
            try{
                if(output !=null){
                    if(sendmsg !=null){
                        //채팅 서버에 메시지 전달
                        output.writeUTF(mac + " : " + sendmsg);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
