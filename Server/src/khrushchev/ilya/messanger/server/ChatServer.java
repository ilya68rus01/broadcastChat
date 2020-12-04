package khrushchev.ilya.messanger.server;

import khrushchev.ilya.messanger.network.TCPConnection;
import khrushchev.ilya.messanger.network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCPConnectionListener {

    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    ChatServer(){
        System.out.println("Server running ...");
        try(ServerSocket serverSocket = new ServerSocket(8080)) {
            while (true){
                try{
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException connectionError){
                    System.out.println("Server exception: " + connectionError);
                }

            }
        }
        catch (IOException creatingError){
            throw new RuntimeException(creatingError);
        }
    }

    public static void main(String[] args) {
        new ChatServer();
    }

    @Override
    public synchronized void OnConnectionReady(TCPConnection connection) {
        connections.add(connection);
        sendToAllConnections("Client connected: " + connection);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection connection) {
        sendToAllConnections("Client disconnect: " + connection);
        connections.remove(connection);
    }

    @Override
    public synchronized void onReciveMessage(TCPConnection connection, String message) {
        System.out.println(message);
        sendToAllConnections(connection + " send: " + message);
    }

    @Override
    public void onExeption(TCPConnection connection, Exception e) {
        System.out.println("TCPConnection exeption: " + e);
    }

    private void sendToAllConnections(String str){
        for (TCPConnection connection: connections) connection.sendString(str);
    }
}
