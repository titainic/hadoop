package com.netty.chapter1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by titanic on 17-6-21.
 */
public class BlockingIoExample
{
    public void serve(int portNumber) throws IOException
    {
        //服务端
        ServerSocket serverSocket = new ServerSocket(portNumber);

        //客户端
        Socket clientSocket = serverSocket.accept();//accept会一直阻塞，直到一个client链接上

        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintStream out = new PrintStream(clientSocket.getOutputStream(), true);

        String request,response;
        while ((request = in.readLine()) != null)
        {
            if ("Done".equals(request))
            {
                break;
            }
        }
        response = processRequest(request);
        out.print(response);
    }

    private String processRequest(String request)
    {
        return "Processed";
    }
}
