import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener
{
    public static void main(String[] args) throws IOException
    {
        if(args.length < 1)
        {
            System.out.println("Required arguments: <port>");
            return;
        }
        int portNumber;
        try
        {
            portNumber = Integer.parseInt(args[0]);
        }
        catch(NumberFormatException e)
        {
            System.out.println("Invalid port number: " + args[0]);
            return;
        }

        try (ServerSocket serverSocket = new ServerSocket(portNumber))
        {
            while(true)
            {
                Socket socket = serverSocket.accept();
                Server server = new Server(socket);
                server.start();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}