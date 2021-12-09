/*
    Aloha! Chat Service
    John Shoemaker
    Benjamin Wheeler
*/
package client;

import static java.lang.System.out;
import java.net.*;
import java.io.*;

public class AlohaClient 
{
    public AlohaClient(String hostname, int port) 
    {
        this.hostname = hostname;
        this.port = port;
    }
 
    public void run() 
    {
        try
        {
            var s = new Socket(hostname, port);
 
            out.println("Connected to the chat server");
 
            new AlohaClientReader(s, this).start();
            new AlohaClientWriter(s, this).start();
 
        } catch (UnknownHostException ex) 
        {
            out.println("Server not found.");
        } catch (IOException ex) 
        {
            out.println("I/O Error");
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
        if (args.length == 0) {   
            return;
        }

        final String hostname = args[0];
 
        var client = new AlohaClient(hostname, 3001); //hardcode the port
        client.run();
    }

    private String hostname;
    private int port;
    private String userName;
}