import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server extends Thread
{
    Socket socket;
    static List<PrintWriter> writers = Collections.synchronizedList(new ArrayList<PrintWriter>());

    public Server(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
        try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true))
        {
            writers.add(out);
            String message;
            while ((message = in.readLine()) != null)
            {
                broadcast(message);
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    synchronized private void broadcast(String message)
    {
        for(PrintWriter writer : writers)
            writer.println(message);
    }
}