package com.qft.socketconnect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.elf.socket.TCPReceiver;
import com.elf.socket.TCPSend;
import com.elf.socket.UDP;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onSend(View view) {
        UDP udp = new UDP(this, 8000);
        udp.sendBroadCast1("dhi");
    }

    public void onRevice(View view) {

        new Thread() {
            @Override
            public void run() {
                UDP udp = new UDP(MainActivity.this, 8000);
                final UDP.UDPData udpData = udp.receive();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast(udpData.toString());
                    }
                });
            }
        }.start();


    }

    private void showToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    TCPSend tcpSend;

    public void onTcpSend(View view) {
        new Thread() {
            @Override
            public void run() {
                if (tcpSend == null) {
                    tcpSend = new TCPSend("192.168.1.119", 8001);
                }
                tcpSend.sendMessage("哈哈哈");
            }
        }.start();
    }

    TCPReceiver receiver;

    public void onTcpRevice(View view) {
        receiver = new TCPReceiver(8001);
    }

    @Override
    protected void onDestroy() {

        receiver.close();
        tcpSend.close();

        super.onDestroy();
    }
}
