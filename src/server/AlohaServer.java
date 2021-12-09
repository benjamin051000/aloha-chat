package server;
// package net.codejava.networking.chat.server;
 
import java.io.*;
import java.net.*;
import java.util.ArrayList;
 
/**
 * This is the chat server program.
 * Press Ctrl + C to terminate the program.
 *
 * @author www.codejava.net
 */
public class AlohaServer {
    private int port;
    private ArrayList<String> userNames = new ArrayList<>();
    private ArrayList<User> userThreads = new ArrayList<>();
 
    public AlohaServer(int port_) {
        port = port_;
    }
 
    /**
     * Runs the server.
     */
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
 
            System.out.println("Server listening on port " + port);
 
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");
 
                User newUser = new User(socket, this);
                userThreads.add(newUser);
                newUser.start();
 
            }
 
        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
 
    public static void main(String[] args) {
        AlohaServer server = new AlohaServer(3001);
        server.run();
    }
 
    /**
     * Delivers a message from one user to others (broadcasting)
     */
    void broadcast(String message, User excludeUser) {
        for (User aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }
 
    /**
     * Stores username of the newly connected client.
     */
    void addUserName(String userName) {
        userNames.add(userName);
    }
 
    /**
     * When a client is disconneted, removes the associated username and UserThread
     */
    void removeUser(String userName, User aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(aUser);
            System.out.println(userName + " left");
        }
    }
 
    Set<String> getUserNames() {
        return this.userNames;
    }
 
    /**
     * Returns true if there are other users connected (not count the currently connected user)
     */
    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }
}