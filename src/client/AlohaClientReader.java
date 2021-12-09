/*
    Aloha! Chat Service
    John Shoemaker
    Benjamin Wheeler
*/

package client;
// package net.codejava.networking.chat.client;
 
import java.io.*;
import java.net.*;
 
public class AlohaClientReader extends Thread 
{
    private BufferedReader reader;
    private Socket socket;
    private AlohaClient client;
 
    public AlohaClientReader(Socket socket, AlohaClient client) 
    {
        this.socket = socket;
        this.client = client;
 
        try 
        {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) 
        {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
 
    public void run() 
    {
        while (true) 
        {
            try 
            {
                String response = reader.readLine();
                System.out.println("\n" + response);
 
                // prints the username after displaying the server's message
                if (client.getUserName() != null) 
                {
                    System.out.print("[" + client.getUserName() + "]: ");
                }
            } catch (IOException ex) 
            {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
}