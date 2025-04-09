import javax.swing.*;

public class Protocol
{
    public void processInput(String receivedData, Client client)
    {
        if(receivedData.startsWith("MSG:"))
            client.textArea1.append(receivedData.substring(4) + "\n");

        else if(receivedData.startsWith("HERE:"))
        {
            if(!System.getProperty("user.name").equals(receivedData.substring(5)))
            {
                client.textArea2.append(receivedData.substring(5) + "\n");
            }
        }
        else if(receivedData.startsWith("LEAVE:"))
        {
            String user = receivedData.substring(6);
            StringBuilder newTextArea = new StringBuilder();

            for(String s : client.textArea2.getText().split("\n"))
                if(!s.trim().equals(user) && !s.trim().isEmpty())
                    newTextArea.append(s).append("\n");

            client.textArea2.setText(newTextArea.toString());
        }
        else if(receivedData.startsWith("JOIN:"))
        {
            if(System.getProperty("user.name").equals(receivedData.substring(5).trim()))
                client.textArea2.append(receivedData.substring(5) + "\n");

            String message = "HERE:" + System.getProperty("user.name");

            client.out.println(message);
        }
    }
}