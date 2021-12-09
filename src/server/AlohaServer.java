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
 
    //constantly runs
    public void run() 
    {
        try (ServerSocket serverSocket = new ServerSocket(port)) 
        {
 
            System.out.println("Server listening on port " + port);
 
            while (true) 
            {
                Socket s = serverSocket.accept();

                System.out.println("New user connected. Awaiting name...");
 
                AlohaUser newUser = new AlohaUser(s, this);
                userThreads.add(newUser);
                newUser.start();
 
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
        return this.userNames;
    }

    //send message to users
    void broadcast(String message, AlohaUser excludeUser) 
    {
        for (var aUser : userThreads)
        {
            if (aUser != excludeUser) 
                aUser.send(message);
        }
    }

    // Add the user to the online list
    void bring_user_online(AlohaUser user) 
    {
        userNames.add(user.username);
        // Broadcast to everyone that a new user has connected
        final String msg = user.username + " joined the AlohaChat!";
        broadcast(msg, user);
    }

    int num_online() 
    {
        // Use userNames here, since you aren't "online" until you've supplied a name.
        return this.userNames.size();
    }
 
    //delete user
    void removeUser(String userName, AlohaUser user) 
    {
        boolean removed = userNames.remove(userName);
        if (removed) 
        {
            userThreads.remove(user);
            System.out.println(userName + " has left the AlohaChat");
        }
    }

    private int port;
    private ArrayList<String> userNames = new ArrayList<>();
    private ArrayList<AlohaUser> userThreads = new ArrayList<>();
}
