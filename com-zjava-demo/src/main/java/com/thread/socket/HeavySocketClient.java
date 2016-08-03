package com.thread.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by titanic on 16-8-3.
 */
public class HeavySocketClient
{
    private static ExecutorService tp = Executors.newCachedThreadPool();
    private static final int SLEEP_TIME = 1000 * 1000 * 1000;

    public static class EchoClient implements Runnable
    {
        public void run()
        {
            Socket client = null;
            PrintWriter write = null;
            BufferedReader reader = null;

            try
            {
                client = new Socket();
                client.connect(new InetSocketAddress("127.0.0.1", 8000));
                write = new PrintWriter(client.getOutputStream(), true);
                write.print("H");
                LockSupport.parkNanos(SLEEP_TIME);
                write.print("e");
                LockSupport.parkNanos(SLEEP_TIME);
                write.print("l");
                LockSupport.parkNanos(SLEEP_TIME);
                write.print("l");
                LockSupport.parkNanos(SLEEP_TIME);
                write.print("o");
                LockSupport.parkNanos(SLEEP_TIME);
                write.print("!");
                LockSupport.parkNanos(SLEEP_TIME);
                write.println();
                write.flush();

                reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                System.out.println("feom server" + reader.readLine());

            } catch (IOException e)
            {
                e.printStackTrace();
            } finally
            {

                try
                {
                    if (write != null)
                    {
                        write.close();
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

    public static void main(String[] args)
    {
        EchoClient ec = new EchoClient();
        for (int i = 0; i <10 ; i++)
        {
            tp.execute(ec);
        }
    }
}
