package khrushchev.ilya.messanger.network;

import java.io.*;
import java.net.Socket;

public class TCPConnection {
    private final Thread thread;
    private final Socket socket;
    private final TCPConnectionListener eventListener;
    private final BufferedReader in;
    private final BufferedWriter out;

    public  TCPConnection(TCPConnectionListener eventListener, String ip, int port) throws IOException{
        this(eventListener, new Socket(ip,port));
    }

    public TCPConnection(TCPConnectionListener eventListener, Socket socket) throws IOException{
        this.socket = socket;
        this.eventListener = eventListener;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                eventListener.OnConnectionReady(TCPConnection.this);
                try {
                    while (!thread.isInterrupted()) {
                        eventListener.onReciveMessage(TCPConnection.this, in.readLine());
                    }
                } catch (IOException e){
                    eventListener.onExeption(TCPConnection.this, e);
                } finally {
                    eventListener.onDisconnect(TCPConnection.this);
                }
            }
        });
        thread.start();
    }

    public synchronized void sendString(String message){
        try {
            out.write(message + "\r\n");
            out.flush();
        } catch (IOException exception){
            eventListener.onExeption(TCPConnection.this, exception);
            disconnect();
        }
    }

    public synchronized void disconnect(){
        thread.interrupt();
        try {
            socket.close();
        } catch (IOException exception) {
            eventListener.onExeption(TCPConnection.this, exception);
        }
    }

    @Override
    public String toString(){
        return "TCPConnection: " + socket.getInetAddress() + " port: " + socket.getPort();
    }
}
