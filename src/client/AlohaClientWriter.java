/*
    Aloha! Chat Service
    John Shoemaker
    Benjamin Wheeler
*/

package client;
// package net.codejava.networking.chat.client;
import static java.lang.System.out;
import java.io.*;
import java.net.*;
 
public class AlohaClientWriter extends Thread 
{
    public AlohaClientWriter(Socket socket_, AlohaClient clientAloha) 
    {
        client = clientAloha;
        socket = socket_;
 
        try 
        {
            // Writes messages from the client to the server
            writer = new PrintWriter(socket_.getOutputStream(), true);
        } catch (IOException ex) 
        {
            out.println("Error creating writer thread.");
        }
    }
 
    public void run() 
    {
        final var console = System.console();
 
        String username;
        do {
            username = console.readLine();
        } while(username == null || username.trim().isEmpty()); // Ensure user types something for their name

        client.setusername(username);
        // Send username to server
        writer.println(username);
 
        while (true)
        {
            // Get keyboard input from user
            final var text = console.readLine("~" + username + ": ");
            
            // Client-side commands
            if(text.equals("/quit")) {
                writer.println(text); // Send "/quit" so server knows you're leaving
                break;
            }
            else if(text.equals("/help")) 
            {
                out.println("Current commands are... /quit, /help, /online, /numOnline, /ping");
                continue; // Don't broadcast command to others
            }

            // Send message/command to server socket
            writer.println(text);
        }
 
        try 
        {
            socket.close();
        } 
        catch (IOException e) 
        {
            out.println("Error writing to server.");
        }
    }

    private Socket socket;
    private AlohaClient client;
    private PrintWriter writer;
}