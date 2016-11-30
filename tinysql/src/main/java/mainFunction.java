/**
 * Created by Tao on 11/18/2016.
 */
import java.lang.reflect.Array;
import java.util.ArrayList;
import storageManager.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.*;
public class mainFunction {

    static MainMemory mem=null;
    static Disk disk=null;
    static SchemaManager schema_manager=null;
    mainFunction() {
        mem = new MainMemory();
        disk = new Disk();
        // System.out.print("The memory contains " + mem.getMemorySize() + " blocks" + "\n");
        // System.out.print(mem + "\n" + "\n");
        schema_manager = new SchemaManager(mem, disk);
        System.out.println("hello world");
    }

    private void createTable(String TableName,ArrayList<String> DataNames,ArrayList<String> Datatypes)
    {
        System.out.println("Creating a schema");
        ArrayList<FieldType> field_types=new ArrayList<FieldType>();
        for(String str:Datatypes)field_types.add(str.equals("STR20")?FieldType.STR20:FieldType.INT);
        Schema schema=new Schema(DataNames,field_types);
        System.out.println("Creating table " + TableName);
        Relation relation_reference=schema_manager.createRelation(TableName,schema);
        System.out.print("The table has name " + relation_reference.getRelationName() + "\n");
        System.out.print("The table has schema:" + "\n");
        System.out.print(relation_reference.getSchema() + "\n");
        System.out.print("The table currently have " + relation_reference.getNumOfBlocks() + " blocks" + "\n");
        System.out.print("The table currently have " + relation_reference.getNumOfTuples() + " tuples" + "\n" + "\n");
        System.out.flush();
    }

    private void dropTable(String TableName)
    {
        System.out.println("Deleting Table "+TableName);
        schema_manager.deleteRelation(TableName);
        System.out.println("After deleting a realtion, current schemas and relations: " );
        System.out.print(schema_manager + "\n" + "\n");
        System.out.flush();
    }

    private void insertIntoTable(Relation relation_reference,ArrayList<String> DataNames,ArrayList<String> DataValues,Block block)
    {
        Tuple tuple = relation_reference.createTuple(); //The only way to create a tuple is to call "Relation"
        int n=DataNames.size();
        Schema sc=relation_reference.getSchema();
        for(int i=0;i<n;++i)
        {
            if(sc.getFieldType(i).equals(FieldType.STR20))tuple.setField(DataNames.get(i),DataValues.get(i));
            else tuple.setField(DataNames.get(i),Integer.parseInt(DataValues.get(i)));
        }
        System.out.print("Created a tuple " + tuple + " of"+"through the relation" + "\n");
        System.out.print("The tuple is invalid? " + (tuple.isNull()?"TRUE":"FALSE") + "\n");
        Schema tuple_schema = tuple.getSchema();
        System.out.print("The tuple has schema" + "\n");
        System.out.print(tuple_schema + "\n");
        System.out.print("A block can allow at most " + tuple.getTuplesPerBlock() + " such tuples" + "\n");

        System.out.print("The tuple has fields: " + "\n");
        for (int i=0; i<tuple.getNumOfFields(); i++) {
            if (tuple_schema.getFieldType(i)==FieldType.INT)
                System.out.print(tuple.getField(i) + "\t");
            else
                System.out.print(tuple.getField(i) + "\t");
        }
        block.appendTuple(tuple);
        //System.out.print("The table currently have " + relation_reference.getNumOfTuples() + " tuples" + "\n" + "\n");
        System.out.print("\n");
        System.out.flush();
    }
    private void flushToRelation(Relation relation_reference,ArrayList<ArrayList<String>>DataName,ArrayList<ArrayList<String>>DataValues)
    {
        int blockindex=0;
        Block block_ptr=mem.getBlock(blockindex);
        block_ptr.clear();
        for(int i=0;i<DataName.size();++i)
        {
            if(block_ptr.isFull())
            {
                block_ptr=mem.getBlock(++blockindex);
                block_ptr.clear();
            }
            insertIntoTable(relation_reference,DataName.get(i),DataValues.get(i),block_ptr);
        }
        for (int memory_block_index = 0; memory_block_index < Config.NUM_OF_BLOCKS_IN_MEMORY; ++memory_block_index)
            if (!mem.getBlock(memory_block_index).isEmpty())relation_reference.setBlock(relation_reference.getNumOfBlocks(), memory_block_index);
    }
    private void insertIntoTableNtimes(String TableName,ArrayList<ArrayList<String>>DataName,ArrayList<ArrayList<String>>DataValues)
    {
        Relation relation_reference=schema_manager.getRelation(TableName);
        int capacity=relation_reference.getSchema().getTuplesPerBlock()*Config.NUM_OF_BLOCKS_IN_MEMORY;
        int times=DataName.size()/capacity+1;
        while(--times>=0)flushToRelation(relation_reference,DataName,DataValues);
    }


    public static void main(String[] args) {
        //create table
        String []name={"f1","f2","f3","f4"};
        String []type={"STR20", "INT", "INT", "STR20"};
        String [][]names={{ "f1", "f2", "f3", "f4" }, { "f1", "f2", "f3", "f4" }, { "f1", "f2", "f3", "f4" }, { "f1", "f2", "f3", "f4" }};
        String [][]values={{ "h1", "20", "30", "h2" }, { "h3", "40", "50", "h4" }, { "h5", "60", "70", "h6" }, { "h7", "80", "90", "h8" }};
        ArrayList<String> field_name=new ArrayList<String>(Arrays.asList(name));
        ArrayList<String> field_type=new ArrayList<String>(Arrays.asList(type));
        ArrayList<ArrayList<String>>DataNames=new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>>DataValues=new ArrayList<ArrayList<String>>();
        for(int i=0;i<4;++i)
        {
            DataNames.add(new ArrayList<String>(Arrays.asList(names[i])));
            DataValues.add(new ArrayList<String>(Arrays.asList(values[i])));
        }
        mainFunction mf=new mainFunction();

        String TableName="wangtao";
        //create table
        mf.createTable(TableName,field_name,field_type);
        //insert into table
        System.out.println("Insert into table");
        mf.insertIntoTableNtimes(TableName,DataNames,DataValues);
        Relation relation_reference=schema_manager.getRelation(TableName);
        System.out.println("-----------relation-------------------------");
        System.out.print(relation_reference + "\n");
        System.out.println("----------memory--------------------------");
        System.out.print(mem + "\n");


        System.out.println("------------------------------------");
        System.out.println("Drop table");
        TableName="taobupt";
        mf.createTable(TableName,field_name,field_type);
        mf.dropTable(TableName);
        System.out.println("done!");

    }
}
