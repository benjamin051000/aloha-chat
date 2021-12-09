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
    public AlohaClientReader(Socket socketObj, AlohaClient clientAloha) 
    {
        this.socketObj = socketObj;
        this.clientAloha = clientAloha;
 
        try 
        {
            InputStream input = socketObj.getInputStream();
            readerBuffer = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) 
        {
            System.out.println("Error with reading...");
        }
    }
 
    public void run() 
    {
        while (true) 
        {
            try 
            {
                String response = readerBuffer.readLine();
                System.out.println("\n" + response);
 
                // prints the username after displaying the server's message
                if (clientAloha.getUserName() != null) 
                {
                    System.out.print("~" + clientAloha.getUserName() + ": ");
                }
            } catch (IOException ex) 
            {
                System.out.println("Connection terminated... goodbye!");
                break;
            }
        }
    }

    private Socket socketObj;
    private AlohaClient clientAloha;
    private BufferedReader readerBuffer;
}