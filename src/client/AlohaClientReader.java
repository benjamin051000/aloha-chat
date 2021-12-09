/*
    Aloha! Chat Service
    John Shoemaker
    Benjamin Wheeler
*/
package client;

import static java.lang.System.out;

import java.io.*;
import java.net.*;


public class AlohaClientReader extends Thread 
{
    public AlohaClientReader(Socket socket_, AlohaClient client_) 
    {
        socket = socket_;
        client = client_;
 
        try 
        {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } 
        catch (IOException e) 
        {
            out.println("Error creating reader thread.");
        }
    }
 

    public void run() 
    {
        while (true) 
        {
            try 
            {
                // Read a message from the server socket (waits until one is sent)
                var response = reader.readLine();
                out.println("\n" + response);
 
                // prints the username after displaying the server's message
                if (client.getusername() != null) 
                {
                    out.print("~" + client.getusername() + ": ");
                }
            } 
            // If the write socket closes, this one will throw an exception.
            catch (IOException e)
            {
                out.println("Connection terminated... goodbye!");
                break;
            }
        }
    }

    private Socket socket;
    private AlohaClient client;
    private BufferedReader reader;
}