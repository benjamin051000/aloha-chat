/*
    Aloha! Chat Service
    John Shoemaker
    Benjamin Wheeler
*/
package server;

import java.net.Socket;
import java.io.*;

import static java.lang.System.out;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class AlohaUser extends Thread 
{
    public AlohaUser(Socket socket_, AlohaServer server_) 
    {
        socket = socket_;
        server = server_;
        
        // Setup read-from and write-to client socket
        try {
            // Reads data from the client socket via socket's input stream
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Sends data to the client socket via socket's output stream
            writer = new PrintWriter(socket.getOutputStream(), true);
        }
        catch (IOException ex) {
            out.println("Error occurred with user...");
        }
    }

    // Send a message of the day to the client when they first connect.
    private void send_motd() {
        // Get current date and time
        final var datetime_fmt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        final var now = LocalDateTime.now();

        var motd = "AlohaChat! " + datetime_fmt.format(now); // Message of the day
        
        // Send client number of users currently online
        if (server.num_online() > 0) {
            motd += "\n" + Integer.toString(server.num_online()) +  " user(s) online: " + server.list();
        }
        else {
            motd += "\nNo users online.";
        }

        motd += "\n-----------------------------------------";
        motd += "\nAloha! Enter your name?: ";
        
        // Send message of the day to the client.
        writer.println(motd);
    }

    // Runs the thread. This happens concurrently to other threads on the server.
    public void run() 
    {
        try 
        {
            send_motd();

            // Get username from client 
            username = reader.readLine();
            
            // Server registers this user as online and saves their username.
            server.bring_user_online(this);

            while(true) 
            {
                // Get a message from client
                String clientMessage = reader.readLine();

                if(clientMessage.equals(quit_message)) {
                    break;
                }

                // Append username
                var serverMessage = "~" + username + ": " + clientMessage;

                // Log message
                out.println("[Client msg] " + serverMessage);

                // Broadcast the message to all online clients                
                server.broadcast(serverMessage, this);

            } // end of while(true)

            // The user typed the quit message. Remove their entry from the server
            server.removeUser(this);
            socket.close();

            server.broadcast(username + " has left the AlohaChat!", this);
        } 
        catch (IOException ex) 
        {
            out.println("Error occurred with user...");
        }
    }

    // API for server to send chat messages to clients
    public void send(String msg) 
    {
        writer.println(msg);
    }
    
    // Constant command for exiting the chat.
    static final String quit_message = "/quit";
    
    // Reference to the server instance
    private AlohaServer server;

    // Socket which connects to the client socket
    private Socket socket;
    // Write to the client socket
    private PrintWriter writer;
    // Read from the client socket
    private BufferedReader reader;
    
    public String username;
}