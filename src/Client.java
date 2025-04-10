import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

public class Client extends JFrame
{
    int port;
    String address;
    Socket socket;
    PrintWriter out;
    BufferedReader in;

    String username;
    Set<String> users;

    JPanel panel;
    JButton button;
    JTextField textField;
    JTextArea textArea1, textArea2;
    JScrollPane scrollPane1, scrollPane2;
    JSplitPane splitPane;

    public Client(int port, String address) throws IOException
    {
        username = JOptionPane.showInputDialog("Enter your user name");
        if(username == null)
            System.exit(0);
        users = new HashSet<String>();

        this.port = port;
        this.address = address;

        socket = new Socket(InetAddress.getByName(address), port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setTitle("Chatt " + username);

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                String message = "LEAVE:" + username;
                out.println(message);
                try
                {
                    socket.close();
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        });

        panel = new JPanel(new BorderLayout());

        button = new JButton("Koppla ner");
        button.addActionListener(e ->
        {
            String message = "LEAVE:" + username;
            out.println(message);
            try
            {
                socket.close();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
            System.exit(0);
        });

        textField = new JTextField();
        textField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    String message = "MSG:" + username + ": " + textField.getText();
                    out.println(message);
                    textField.setText("");
                }
            }
        });

        textArea1 = new JTextArea();
        textArea1.setEditable(false);
        textArea1.setLineWrap(true);
        textArea1.setWrapStyleWord(true);
        textArea2 = new JTextArea("I chatten just nu:\n");
        textArea2.setEditable(false);
        textArea2.setLineWrap(true);
        textArea2.setWrapStyleWord(true);

        scrollPane1 = new JScrollPane(textArea1);
        scrollPane2 = new JScrollPane(textArea2);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane1, scrollPane2);
        splitPane.setResizeWeight(1);

        panel.add(button, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.SOUTH);
        panel.add(splitPane, BorderLayout.CENTER);

        add(panel);

        setVisible(true);
    }
    public static void main(String[] args) throws IOException
    {
        if(args.length < 2)
        {
            System.out.println("Required arguments: <port> <address>");
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
        Client window = new Client(portNumber, args[1]);
        Protocol protocol = new Protocol();

        String message = "JOIN:" + window.username;

        window.out.println(message);

        String receivedData;
        while ((receivedData = window.in.readLine()) != null)
        {
            protocol.processInput(receivedData, window);
        }
    }
}
