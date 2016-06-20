package com.titanic.hive;

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by titanic on 16-6-15.
 */
public class HiveCmdBulider
{


    private String clientMode = "BEELINE";
    final private List<String> statements = Lists.newArrayList();

    public HiveCmdBulider(String mode)
    {
        clientMode = mode;
    }

    public String build()
    {
        StringBuffer buf = new StringBuffer();

        if (clientMode.equals("CLI"))
        {
            buf.append("hive -e\"");
            for (String statement : statements)
            {
                buf.append(statement).append("\n");
            }
            buf.append("\"");

        } else if (clientMode.equals("BEELINE"))
        {
            BufferedWriter bw = null;
            try
            {
                File tmpHql = File.createTempFile("beeline_", ".hql");
                StringBuffer hqlBuf = new StringBuffer();
                bw = new BufferedWriter(new FileWriter(tmpHql));
                for (String statement : statements)
                {
                    bw.write(statement);
                    bw.newLine();
                    hqlBuf.append(statement).append("\n");
                }
                buf.append("beeline ");
                // 'jdbc:hive2://localhost:10000'
                buf.append("-u jdbc:hive2://192.9.11.92:10000/default -n root -w 12345678");
                buf.append(" -f ");
                buf.append(tmpHql.getAbsolutePath());
                buf.append(";rm -f ");
                buf.append(tmpHql.getAbsoluteFile());

                System.out.println("The statements to execute in beeline: \n" + hqlBuf);
            } catch (IOException e)
            {
                e.printStackTrace();
            } finally
            {
                IOUtils.closeQuietly(bw);
            }

        } else
        {
            throw new RuntimeException("Hive client cannot be recognized: " + clientMode);
        }
        return buf.toString();
    }

    public void reset()
    {
        statements.clear();
    }

    public void addStatement(String statement)
    {
        statements.add(statement);
    }

    public void addStatmenets(String[] stats)
    {
        for (String s : stats)
        {
            statements.add(s);
        }
    }

    public String toString()
    {
        return build();
    }

}
