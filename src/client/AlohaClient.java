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
    public AlohaClient(String hostname_, int port_) 
    {
        hostname = hostname_;
        port = port_;
    }
 
    public void run() 
    {
        try
        {
            var s = new Socket(hostname, port);
 
            out.println("Connected to the Aloha! server");
 
            new AlohaClientReader(s, this).start();
            new AlohaClientWriter(s, this).start();

        } 
        catch (UnknownHostException e) 
        {
            out.println("Server not found...");
        } 
        catch (IOException e) 
        {
            out.println("I/O Error");
        }
    }
 
    void setusername(String username_) 
    {
        username = username_;
    }
 
    String getusername() 
    {
        return username;
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
    private String username;
}