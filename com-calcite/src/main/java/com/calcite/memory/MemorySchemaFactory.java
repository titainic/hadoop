package com.calcite.memory;

import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;

import java.util.Map;

/**
 * Created by titanic on 16-5-24.
 */
public class MemorySchemaFactory implements SchemaFactory
{
    public Schema create(SchemaPlus schemaPlus, String s, Map<String, Object> map)
    {
        System. out.println( "param1 : " + map.get( "param1"));
        System. out.println( "param2 : " + map.get( "param2"));

        System. out.println( "Get database " + s);
        return new MemorySchema( s);
    }
}
