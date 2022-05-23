package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    static int portNumber= 1234;
    static ServerSocket serverSocket= null;
    private static Socket clientSocket= null;

    public static void main(String[] args) {
        /**ArrayList<Product> products = new ArrayList<>();
        products.add(new Product(13,"Don Perignon Vintage Moet & Chandon 2008",225.94, "white"));
        products.add(new Product(14,"Pignoli Radikon Radikon 2009",133.94, "red"));
        products.add(new Product(15,"Pinto Nero elena Walch Elena Walch 20018",43.94, "red"));

        DatabaseHandler db=new DatabaseHandler();
        db.init(products);**/
        serverSocket=openServer();
        if (serverSocket== null) {
            return;
        }

        System.out.println("Server started!");

        while (true) {
            clientSocket=openClientSocket();
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            clientHandler.handle();
            closeClientSocket();
        }
    }

    private static ServerSocket openServer() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } return serverSocket;
    }

    private static Socket openClientSocket() {
        try {
            clientSocket=serverSocket.accept();
            System.out.println("Server accepted");
        } catch (IOException e) {
            System.out.println("Accept failed" + e);
            return null;
        } return clientSocket;
    }

    private static void closeClientSocket() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
