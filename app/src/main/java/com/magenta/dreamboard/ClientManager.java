package com.magenta.dreamboard;

import android.nfc.Tag;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class ClientManager implements Runnable {

    Socket socket;

    private static String host = "";
    private static int port = 0;
    private static String output = "";

    MainActivity mainActivity = new MainActivity();

    public void setOutput(String newOutput) {
        output = newOutput;
    }

    public void setSocket(String newHost, int newPort) {
        host = newHost;
        port = newPort;
    }

    @Override
    public void run() {
        try {
            Log.d("TCP", "run: Connecting to (" + host + ", " + port + ")...");
            socket = new Socket(host, port);
            try {
                sendMessage();
                Log.d("TCP", "sent: " + output);
            } catch (Exception e) {
                Log.e("TCP", "error: ", e);
            } finally {
                socket.close();
            }
        } catch (Exception e) {
            Log.e("TCP", "error: ", e);
        }
    }

    //Send msg to pc
    public void sendMessage() {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            out.print(output);
            Log.d("TCP", "sent: " + output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

/*
public class ClientManager extends Thread{
    public void run() {
        Socket socket = new Socket();
        String dominName = args[0];

    }
}
*/

/*

public class ClientManager extends Thread{

    public InputStream dataInputStream;
    public OutputStream dataOutputStream;
    private Socket socket;
    String host = "";
    int port = 0;

    MainActivity mainActivity = new MainActivity();

    //Socket start
    @Override
    public void run() {
        try {
            socket = new Socket();
            dataInputStream = socket.getInputStream();
            dataOutputStream = socket.getOutputStream();
            mainActivity.connection = true;
        } catch (IOException e) {
            e.printStackTrace();
            mainActivity.connection = false;
        }
        byte[] buffer = new byte[1024];
        int bytes;
        mainActivity.host = host;
        while (true) {
            try {
                bytes = dataInputStream.read(buffer);
                String tmp = new String(buffer, 0, bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Send msg to pc
    public void sendMessage(String message) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(message);
            outputStream.flush();
            Log.d("ClientThread", "Send Complete");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Get msg to pc
    public Object getMessage() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            final Object input = (String) inputStream.readObject();
            Log.d("ClientManager", "Get data: " + input);
            return input;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
*/

