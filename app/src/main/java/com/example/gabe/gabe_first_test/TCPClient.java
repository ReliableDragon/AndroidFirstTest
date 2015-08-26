package com.example.gabe.gabe_first_test;

/**
 * Created by Gabe on 2/2/2015.
 */
import java.io.*;
import java.net.*;

class TCPClient
{
    public static String getMessage() throws IOException
    {
        String IPADDRESS = "172.16.248.248";
        String sentence;
        String modifiedSentence;
        BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
        Socket clientSocket = new Socket(IPADDRESS, 3000);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        sentence = inFromUser.readLine();
        outToServer.writeBytes(sentence + '\n');
        modifiedSentence = inFromServer.readLine();

        clientSocket.close();

        return modifiedSentence;
    }
}