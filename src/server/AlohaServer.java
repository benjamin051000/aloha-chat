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
    //constructor for the class
    public AlohaServer(int port_) 
    {
        port = port_;
    }
 
    // main server thread
    public void run() 
    {
        // Create a server socket, which will wait for/act on network requests.
        try (ServerSocket serverSocket = new ServerSocket(port)) 
        {
 
            System.out.println("Server listening on port " + port);
 
            while (true) 
            {
                // Accept an incoming connection to the server
                // (func blocks until connection made)
                Socket s = serverSocket.accept();

                System.out.println("New client connected");
                
                // Create a new thread that represents this client
                var user = new AlohaUser(s, this);
                userthreads.add(user);
                user.start();
            }
        } 
        catch (IOException ex) 
        {
            System.out.println("Error");
        }
    }
 
    public static void main(String[] args) 
    {
        AlohaServer server = new AlohaServer(3001);
        server.run();
    }

    // List all online clients.
    ArrayList<String> list() 
    {
        return this.usernames;
    }

    //send message to users
    void broadcast(String message, AlohaUser excludeUser) 
    {
        for (var aUser : userthreads)
        {
            if (aUser != excludeUser) 
                aUser.send(message);
        }
    }

    // Add the user to the online list
    void bring_user_online(AlohaUser user) 
    {
        usernames.add(user.username);
        // Broadcast to everyone that a new user has connected
        final String msg = user.username + " joined the AlohaChat!";
        broadcast(msg, user);
    }

    int num_online() 
    {
        // Use userNames here, since you aren't "online" until you've supplied a name.
        return this.usernames.size();
    }
 
    //delete user
    void removeUser(AlohaUser user) 
    {
        userthreads.remove(user);
        usernames.remove(user.username);
        System.out.println(user.username + " has left the AlohaChat");
    }

    private int port;
    // List of "online" users (users who have supplied a username and are in the chatroom)
    private ArrayList<String> usernames = new ArrayList<>();
    // Array of thread objects representing connections to clients.
    private ArrayList<AlohaUser> userthreads = new ArrayList<>();
}
