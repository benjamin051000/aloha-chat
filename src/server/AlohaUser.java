/*
    Aloha! Chat Service
    John Shoemaker
    Benjamin Wheeler
*/

package server; 
import java.net.Socket;
import java.io.*;

public class AlohaUser extends Thread 
{
    public AlohaUser(Socket socket_, AlohaServer server_) 
    {
        socket = socket_;
        server = server_;
    }
 
    public void run() 
    {
        try 
        {
            // Reads data from the client socket
            var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
 
            // Sends data to the client socket
            writer = new PrintWriter(socket.getOutputStream(), true);
 
            // Send client number of users currently online
            if (server.num_online() > 0) 
                writer.println(Integer.toString(server.num_online()) +  " users online: " + server.list());
            else 
                writer.println("0 users online.");
 
            // Get username from client 
            username = reader.readLine();
            // Server registers this user as online and saves their username.
            server.bring_user_online(this);
 
            String serverMessage;
            String clientMessage;
            do 
            {
                // Get a message from client
                clientMessage = reader.readLine();
                // Broadcast the message to all online clients
                serverMessage = "~" + username + ": " + clientMessage;
                System.out.println("[Client message]: " + serverMessage);

                server.broadcast(serverMessage, this);
 
            } while (!clientMessage.equals(quit_message)); // aloha means goodbye, too!
 
            // The user typed the quit message. Remove their entry from the server
            server.removeUser(username, this);
            socket.close();
 
            serverMessage = username + " has left the AlohaChat!";
            server.broadcast(serverMessage, this);
 
        } catch (IOException ex) 
        {
            System.out.println("Error occurred with user...");
        }
    }

    void send(String msg) 
    {
        writer.println(msg);
    }
    
    // Constant command for exiting the chat.
    static final String quit_message = "aloha";
    
    private Socket socket;
    private AlohaServer server;
    private PrintWriter writer;
    public String username;
}