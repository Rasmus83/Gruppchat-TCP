public class Protocol
{
    public void processInput(String receivedData, Client client)
    {
        if(receivedData.startsWith("MSG:"))
            client.textArea1.append(receivedData.substring(4) + "\n");

        else if(receivedData.startsWith("HERE:"))
        {
            if(!client.users.contains(receivedData.substring(5)))
            {
                client.textArea2.append(receivedData.substring(5) + "\n");
                client.users.add(receivedData.substring(5));
            }
        }
        else if(receivedData.startsWith("LEAVE:"))
        {
            String user = receivedData.substring(6);
            StringBuilder newTextArea = new StringBuilder();

            if(client.users.contains(user))
                client.users.remove(user);

            for(String s : client.textArea2.getText().split("\n"))
                if(!s.trim().equals(user) && !s.trim().isEmpty())
                    newTextArea.append(s).append("\n");

            client.textArea2.setText(newTextArea.toString());
        }
        else if(receivedData.startsWith("JOIN:"))
        {
            if(client.username.equals(receivedData.substring(5).trim()))
            {
                client.textArea2.append(receivedData.substring(5) + "\n");
                if(!client.users.contains(receivedData.substring(5).trim()))
                    client.users.add(receivedData.substring(5));
            }

            String message = "HERE:" + client.username;

            client.out.println(message);
        }
    }
}
