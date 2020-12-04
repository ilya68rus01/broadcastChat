package khrushchev.ilya.messanger.network;

public interface TCPConnectionListener {
    void OnConnectionReady(TCPConnection connection);
    void onDisconnect(TCPConnection connection);
    void onReciveMessage(TCPConnection connection, String message);
    void onExeption(TCPConnection connection, Exception e);
}
