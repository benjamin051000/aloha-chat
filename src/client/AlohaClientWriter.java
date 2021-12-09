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
    public AlohaClientWriter(Socket socketObj, AlohaClient clientAloha) 
    {
        this.clientAloha = clientAloha;
        this.socketObj = socketObj;
 
        try 
        {
            OutputStream output = socketObj.getOutputStream();
            writerPrinter = new PrintWriter(output, true);
        } catch (IOException ex) 
        {
            out.println("Error writing...");
        }
    }
 
    public void run() 
    {
        var console = System.console();
 
        String username;
        do {
            username = console.readLine();
        } while(username == null || username.trim().isEmpty()); // Ensure user types something for their name

        clientAloha.setUserName(username);
        // Send username to server
        writerPrinter.println(username);
 
        while (true)
        {
            // Get keyboard input from user
            var text = console.readLine("~" + username + ": ");
            
            if(text.equals("/quit")) {
                writerPrinter.println(text); // Send "/quit" so server knows you're leaving
                break;
            }
            else if(text.equals("/help")) 
            {
                out.println("Current commands are... /quit, /help");
                continue; // Don't broadcast command to others
            }

            // Send message to server socket
            writerPrinter.println(text);
        }
 
        try 
        {
            socketObj.close();
        } catch (IOException e) 
        {
            out.println("Error writing to server: " + e.getMessage());
        }
    }

    private Socket socketObj;
    private AlohaClient clientAloha;
    private PrintWriter writerPrinter;
}