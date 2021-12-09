/*
    Aloha! Chat Service
    John Shoemaker
    Benjamin Wheeler
*/
package server;

import java.io.IOException; // For socket errors
import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;


public class AlohaServer 
{
    private int port;
    private ArrayList<String> userNames = new ArrayList<>();
    private ArrayList<AlohaUser> userThreads = new ArrayList<>();
 
    //constructor for the class
    public AlohaServer(int port_) 
    {
        port = port_;
    }
 
    //constantly runs
    public void run() 
    {
        try (ServerSocket serverSocket = new ServerSocket(port)) 
        {
 
            System.out.println("Server listening on port " + port);
 
            while (true) 
            {
                Socket s = serverSocket.accept();
                System.out.println("New user connected.");
 
                AlohaUser newUser = new AlohaUser(s, this);
                userThreads.add(newUser);
                newUser.start();
 
            }
 
        } catch (IOException ex) 
        {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
 
    public static void main(String[] args) 
    {
        AlohaServer server = new AlohaServer(3001);
        server.run();
    }

    ArrayList<String> getUserNames() 
    {
        
        return this.userNames;
    }

    //send message to users
    void broadcast(String message, AlohaUser excludeUser) 
    {
        for (AlohaUser aUser : userThreads) 
        {
            if (aUser != excludeUser) 
            {
                aUser.sendMessage(message);
            }
        }
    }

    //new user
    void addUserName(String userName) 
    {
        userNames.add(userName);
    }

    boolean has_users_online() 
    {
        return this.userNames.size() > 0;
    }
 
    //delete user
    void removeUser(String userName, AlohaUser user) 
    {
        boolean removed = userNames.remove(userName);
        if (removed) 
        {
            userThreads.remove(user);
            System.out.println(userName + " left");
        }
    }
}
