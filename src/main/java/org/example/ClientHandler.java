package org.example;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler {
    private Socket clientSocket;
    private static BufferedReader in = null;
    private static PrintWriter out = null;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void handle() {
        in = allocateReader(clientSocket);
        out = allocateWriter(clientSocket);
        buildProductList();
        handleInput();
    }

    public void handleInput() {
        String userInput;
        try {
            while ((userInput = in.readLine()) != null) {
                System.out.println("Client: " + (userInput));
                out.println(process(userInput));
            }
        } catch (IOException e) {
            System.out.println("Client disconnected");
        }
    }

    static ArrayList<Product> products = new ArrayList<>();
    static void buildProductList() {
        products.add(new Product(13,"Don Perignon Vintage Moet & Chandon 2008",225.94, "white"));
        products.add(new Product(14,"Pignoli Radikon Radikon 2009",133.94, "red"));
        products.add(new Product(15,"Pinto Nero elena Walch Elena Walch 20018",43.94, "red"));
    }



    public String process(String input) {

        Gson gson = new Gson();

        String intro="";
        String result="";
        switch(input){
            case "red":
                //intro = "The red wines: ";
                result=redManagement(gson);
                break;
            case "white":
               // intro = "The white wines: ";
                result=whiteManagement(gson);

                break;
            case "cheapest":
               // intro = "The cheapest product: ";

                cheapest();
                Product cheapest = products.get(0);
                result = gson.toJson(cheapest);
                break;
            case "sorted_by_name":
               // intro = "List of all the products: ";
                sort();
                result = gson.toJson(products);
                break;

            case "sorted_by_price":
              //  intro = "List of all the products sorted from cheapest: ";

                cheapest();
                result = gson.toJson(products);
                break;
            default:
                result="Insert: 'cheapest' -> cheapest product | 'sorted_by_name' -> sort by name | 'white' -> list of all white wines |'red' -> list of all red wines | 'sorted_by_price' -> list of all products sorted";
        }
        return result;


    }

    public String redManagement(Gson gson){
        ArrayList<Product> red = new ArrayList<>();
        red(red);
        String result=gson.toJson(red);
        return result;
    }
    public String whiteManagement(Gson gson){
        ArrayList<Product> white = new ArrayList<>();
        white(white);
        String result=gson.toJson(white);
        return result;
    }

    public void cheapest(){
        products.sort((Product o1, Product o2) -> {
            return o1.price.compareTo(o2.price);
        });
    }

    public void sort(){
        products.sort((Product o1,Product o2)->{return o1.getName().compareTo(o2.getName());});
    }

    public ArrayList red(ArrayList red) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).type == "red") {
                red.add(products.get(i));
            }
        }
        return red;
    }

    public ArrayList white(ArrayList white) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).type == "white") {
                white.add(products.get(i));
            }
        }
        return white;
    }






    //Server functions.

    private BufferedReader allocateReader(Socket clientSocket) {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Reader failed" + e);
            return null;
        } return in;
    }

    private PrintWriter allocateWriter(Socket clientSocket) {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } return out;
    }

    // END: Server functions.
}
