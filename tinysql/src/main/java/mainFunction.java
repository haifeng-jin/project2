/**
 * Created by Tao on 11/18/2016.
 */
import java.util.ArrayList;
import storageManager.*;
import java.util.regex.*;
public class mainFunction {

    MainMemory mem=null;
    Disk disk=null;
    SchemaManager schema_manager=null;
    mainFunction()
    {
         mem=new MainMemory();
         disk=new Disk();
       // System.out.print("The memory contains " + mem.getMemorySize() + " blocks" + "\n");
       // System.out.print(mem + "\n" + "\n");
        schema_manager=new SchemaManager(mem,disk);
        System.out.println("hello world");
    }

    private boolean isNumeric(String str)
    {
        Pattern pattern=Pattern.compile("[0-9]*");
        Matcher isNum=pattern.matcher(str);
        return isNum.matches();
    }
    private static void appendTupleToRelation(Relation relation_reference, MainMemory mem, int memory_block_index, Tuple tuple) {
        Block block_reference;
        if (relation_reference.getNumOfBlocks()==0) {
            System.out.print("The relation is empty" + "\n");
            System.out.print("Get the handle to the memory block " + memory_block_index + " and clear it" + "\n");
            block_reference=mem.getBlock(memory_block_index);
            block_reference.clear(); //clear the block
            block_reference.appendTuple(tuple); // append the tuple
            System.out.print("Write to the first block of the relation" + "\n");
            relation_reference.setBlock(relation_reference.getNumOfBlocks(),memory_block_index);
        } else {
            System.out.print("Read the last block of the relation into memory block 5:" + "\n");
            relation_reference.getBlock(relation_reference.getNumOfBlocks()-1,memory_block_index);
            block_reference=mem.getBlock(memory_block_index);

            if (block_reference.isFull()) {
                System.out.print("(The block is full: Clear the memory block and append the tuple)" + "\n");
                block_reference.clear(); //clear the block
                block_reference.appendTuple(tuple); // append the tuple
                System.out.print("Write to a new block at the end of the relation" + "\n");
                relation_reference.setBlock(relation_reference.getNumOfBlocks(),memory_block_index); //write back to the relation
            } else {
                System.out.print("(The block is not full: Append it directly)" + "\n");
                block_reference.appendTuple(tuple); // append the tuple
                System.out.print("Write to the last block of the relation" + "\n");
                relation_reference.setBlock(relation_reference.getNumOfBlocks()-1,memory_block_index); //write back to the relation
            }
        }
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

    private void insertIntoTable(String TableName,ArrayList<String> DataNames,ArrayList<String> DataValues)
    {
        Relation relation_reference=schema_manager.getRelation(TableName);
        Tuple tuple = relation_reference.createTuple(); //The only way to create a tuple is to call "Relation"
        int n=DataNames.size();
        for(int i=0;i<n;++i)
        {
            if(isNumeric(DataValues.get(i)))tuple.setField(DataNames.get(i),Integer.parseInt(DataValues.get(i)));
            else tuple.setField(DataNames.get(i),DataValues.get(i));
        }
        System.out.print("Created a tuple " + tuple + " of"+ TableName +"through the relation" + "\n");
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
        System.out.print("\n");
        System.out.flush();
    }
    public static void main(String[] args) {
        //create table
        ArrayList<String> field_names=new ArrayList<String>();
        ArrayList<String> field_types=new ArrayList<String>();
        field_names.add("f1");
        field_names.add("f2");
        field_names.add("f3");
        field_names.add("f4");
        field_types.add("STR20");
        field_types.add("STR20");
        field_types.add("INT");
        field_types.add("STR20");
        mainFunction mf=new mainFunction();

        String TableName="wangtao";
        //create table
        mf.createTable(TableName,field_names,field_types);
        mf.createTable("hello",field_names,field_types);//
        //insert into table

        ArrayList<String>DataValues=new ArrayList<String>();
        DataValues.add("bupt");
        DataValues.add("tamu");
        DataValues.add("20");
        DataValues.add("china");

        System.out.println("Insert into table");
        mf.insertIntoTable(TableName,field_names,DataValues);

        System.out.println("------------------------------------");
        System.out.println("Drop table");
        mf.dropTable(TableName);
        System.out.println("done!");

    }
}
