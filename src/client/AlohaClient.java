/*
    Aloha! Chat Service
    John Shoemaker
    Benjamin Wheeler
*/

package client;
// package net.codejava.networking.chat.client;
 
import java.net.*;
import java.io.*;
 
public class AlohaClient 
{
    private String hostname;
    private int port;
    private String userName;
 
    public AlohaClient(String hostname, int port) 
    {
        this.hostname = hostname;
        this.port = port;
    }
 
    public void execute() 
    {
        try 
        {
            Socket socket = new Socket(hostname, port);
 
            System.out.println("Connected to the chat server");
 
            new AlohaClientReader(socket, this).start();
            new AlohaClientWriter(socket, this).start();
 
        } catch (UnknownHostException ex) 
        {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) 
        {
            System.out.println("I/O Error: " + ex.getMessage());
        }
 
    }
 
    void setUserName(String userName) 
    {
        this.userName = userName;
    }
 
    String getUserName() 
    {
        return this.userName;
    }
 
    public static void main(String[] args) 
    {
        if (args.length < 2) return;
 
        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
 
        AlohaClient client = new AlohaClient(hostname, port);
        client.execute();
    }
}