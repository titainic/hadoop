package com.calcite.memory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import org.apache.calcite.schema.Function;
import org.apache.calcite.schema.ScalarFunction;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;
import org.apache.calcite.schema.impl.ScalarFunctionImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by titanic on 16-5-24.
 */
public class MemorySchema extends AbstractSchema
{

    private String dbName;

    public MemorySchema(String dbName)
    {
        this.dbName = dbName;
    }

    public Map<String,Table> getTableMap()
    {
        Map<String, Table> table = new HashMap<String, Table>();
        MemoryData.Database db = MemoryData.MAP.get(this.dbName);
        if(db == null)
        {
            return table;
        }

        for (MemoryData.Table t : db.tables)
        {
            table.put(t.tableName,new MemoryTable(t));
        }
        return table;
    }

    protected Multimap<String, Function> getFunctionMultimap()
    {
        ImmutableMultimap<String, ScalarFunction> funcs = ScalarFunctionImpl.createAll(TimeOperator.class);
        Multimap<String, Function> functions = HashMultimap.create();
        for (String key : funcs.keySet())
        {
            for (ScalarFunction func : funcs.get(key))
            {
                functions.put(key, func);
            }
        }
        return functions;
    }


}
