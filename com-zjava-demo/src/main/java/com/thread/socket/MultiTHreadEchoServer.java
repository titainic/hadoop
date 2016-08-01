package com.thread.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * socket server编写
 */
public class MultiTHreadEchoServer
{
    private static ExecutorService tp = Executors.newCachedThreadPool();

    static class HandleMsg implements Runnable
    {
        Socket cllentSocket;

        public HandleMsg(Socket cllentSocket)
        {
            this.cllentSocket = cllentSocket;
        }

        public void run()
        {
            BufferedReader is = null;
            PrintWriter os = null;
            try
            {
                is = new BufferedReader(new InputStreamReader(cllentSocket.getInputStream()));
                os = new PrintWriter(cllentSocket.getOutputStream(), true);
                String inputLine = null;
                long b = System.currentTimeMillis();
                while ((inputLine = is.readLine()) != null)
                {
                    os.print(inputLine);
                }
                long e = System.currentTimeMillis();
                System.out.println("spend:" + (e - b) + "ms");

            } catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    if (is != null) is.close();
                    if (os != null) os.close();
                    cllentSocket.close();

                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        }
    }

    public static void main(String[] args)
    {
        ServerSocket echoServer = null;
        Socket clientSocket = null;

        try
        {
            echoServer = new ServerSocket(8000);
        } catch (IOException e)
        {
            e.printStackTrace();
        }


        while (true)
        {
            try
            {
                clientSocket = echoServer.accept();
                System.out.println(clientSocket.getRemoteSocketAddress() + "connect");
                tp.execute(new HandleMsg(clientSocket));
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
