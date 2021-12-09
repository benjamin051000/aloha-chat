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
    private Socket socket;
    private AlohaServer server;
    private PrintWriter writer;
    public String username;
 
    public AlohaUser(Socket socket_, AlohaServer server_) 
    {
        socket = socket_;
        server = server_;
    }
 
    public void run() 
    {
        try 
        {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
 
            if (server.has_users_online()) 
                writer.println("Connected users: " + server.getUserNames());
            else 
                writer.println("No other users connected");
 
            username = reader.readLine();
            server.add_username(this);
 
            String serverMessage;
            // String serverMessage = "New user connected: " + username;
            // server.broadcast(this);
 
            String clientMessage;
 
            do 
            {
                clientMessage = reader.readLine();
                serverMessage = "[" + username + "]: " + clientMessage;
                server.broadcast(serverMessage, this);
 
            } while (!clientMessage.equals("bye"));
 
            server.removeUser(username, this);
            socket.close();
 
            serverMessage = username + " has quit.";
            server.broadcast(serverMessage, this);
 
        } catch (IOException ex) 
        {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    void sendMessage(String message) 
    {
        writer.println(message);
    }
 
}