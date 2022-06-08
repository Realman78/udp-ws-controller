package com.example.controllerdriver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        StringBuilder msg = new StringBuilder("");
        JoystickView joystick = findViewById(R.id.joystickView);

        DatagramSocket s = null;
        try {
            s = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        DatagramSocket finalS = s;
        joystick.setOnMoveListener((angle, strength) -> {
            msg.setLength(0);
            msg.append(angle);
            executor.execute(() -> {
                try {
                    DatagramPacket dp = new DatagramPacket(msg.toString().getBytes(), msg.length(), InetAddress.getByName("10.20.10.179"), 5000);
                    finalS.send(dp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        });

    }
}