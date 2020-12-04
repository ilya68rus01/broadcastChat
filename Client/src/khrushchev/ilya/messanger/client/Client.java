package khrushchev.ilya.messanger.client;

import khrushchev.ilya.messanger.network.TCPConnection;
import khrushchev.ilya.messanger.network.TCPConnectionListener;

import java.io.IOException;
import java.util.Scanner;

public class Client implements TCPConnectionListener {

    private static final String IP_ADDRESS = "localhost";
    private static final int PORT = 8080;
    private TCPConnection connection;

    private Client(){
        try {
            connection = new TCPConnection(this,IP_ADDRESS,PORT);
            Scanner in = new Scanner(System.in);
            String msg = in.nextLine();
            connection.sendString(msg);
            msg = in.nextLine();
            connection.sendString(msg);
        } catch (IOException exception){

        }

    }

    @Override
    public void OnConnectionReady(TCPConnection connection) {
        System.out.println("Connection ready");
        connection.sendString("Hello, i'm connected!");
    }

    @Override
    public void onDisconnect(TCPConnection connection) {
        System.out.println("Connection close");
        connection.sendString("Goodbuy i'm going out");

    }

    @Override
    public void onReciveMessage(TCPConnection connection, String message) {
        System.out.println(connection.toString() + " send: " + message);
    }

    @Override
    public void onExeption(TCPConnection connection, Exception e) {
        System.out.println(e);
    }

    public static void main(String[] args) {
        new Client();
    }

}
