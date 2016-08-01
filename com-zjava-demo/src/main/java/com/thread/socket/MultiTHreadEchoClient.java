package com.thread.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * socket client编写
 */
public class MultiTHreadEchoClient
{
    public static void main(String[] args)
    {
        Socket client = null;
        PrintWriter writer = null;
        BufferedReader reader = null;

        try
        {
            client = new Socket();
            client.connect(new InetSocketAddress("127.0.0.1", 8000));
            writer = new PrintWriter(client.getOutputStream(), true);
            writer.print("Hello");
            writer.flush();

            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            System.out.println("form server :"+reader.readLine());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {

                try
                {
                    if (writer != null)
                    {
                        writer.close();
                    }

                    if (reader != null)
                    {
                        reader.close();
                    }
                    if (client != null)
                    {
                        client.close();
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
        }
    }
}
