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
 
        Console console = System.console();
 
        String userName = console.readLine("\nAloha! Your name?: ");
        clientAloha.setUserName(userName);
        writerPrinter.println(userName);
 
        String text;
 
        do 
        {
            text = console.readLine("~" + userName + ": ");
            writerPrinter.println(text);
 
        } while (!text.equals("aloha"));
 
        try 
        {
            socketObj.close();
        } catch (IOException ex) 
        {
 
            out.println("Error writing to server: " + ex.getMessage());
        }
    }

    private Socket socketObj;
    private AlohaClient clientAloha;
    private PrintWriter writerPrinter;
}