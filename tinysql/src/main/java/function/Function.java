package function; /**
 * Created by Tao on 11/18/2016.
 */
import java.util.ArrayList;

import javafx.scene.control.Tab;
import storageManager.*;
import expression.SearchCondition;

import javax.swing.plaf.nimbus.AbstractRegionPainter;
import java.util.Arrays;

public class Function {

    static MainMemory mem=null;
    static Disk disk=null;
    public static SchemaManager schema_manager=null;
    public Function() {
        mem = new MainMemory();
        disk = new Disk();
        // System.out.print("The memory contains " + mem.getMemorySize() + " blocks" + "\n");
        // System.out.print(mem + "\n" + "\n");
        schema_manager = new SchemaManager(mem, disk);
        System.out.println("hello world");
    }



    private void clearMem()
    {
        int i=0;
        Block block=mem.getBlock(i++);
        block.clear();
        while(i<Config.NUM_OF_BLOCKS_IN_MEMORY)
        {
            block=mem.getBlock(i++);
            block.clear();
        }
    }
    public void createTable(String TableName, ArrayList<String> DataNames, ArrayList<String> Datatypes)
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

    public void dropTable(String TableName)
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
    private void flushToRelation(Relation relation_reference,ArrayList<ArrayList<String>>DataName,ArrayList<ArrayList<String>>DataValues,int start)
    {
        clearMem();
        int blockindex=0;
        Block block_ptr=mem.getBlock(blockindex);
        for(int i=start;i<DataName.size();++i)
        {
            if(block_ptr.isFull())
            {
                ++blockindex;
                if(blockindex>=Config.NUM_OF_BLOCKS_IN_MEMORY)break;
                block_ptr=mem.getBlock(blockindex);
            }
            insertIntoTable(relation_reference,DataName.get(i),DataValues.get(i),block_ptr);
        }
        for (int memory_block_index = 0; memory_block_index < Config.NUM_OF_BLOCKS_IN_MEMORY; ++memory_block_index) {
            if (!mem.getBlock(memory_block_index).isEmpty())
                relation_reference.setBlock(relation_reference.getNumOfBlocks(), memory_block_index);
            else break;
        }
    }
    public void insertIntoTableNtimes(String TableName,ArrayList<String>DataName1,ArrayList<ArrayList<String>>DataValues)
    {
        ArrayList<ArrayList<String>>DataName=new ArrayList<ArrayList<String>>();
        for(int j=0;j<DataValues.size();++j)DataName.add(DataName1);
        Relation relation_reference=schema_manager.getRelation(TableName);
        int capacity=relation_reference.getSchema().getTuplesPerBlock()*Config.NUM_OF_BLOCKS_IN_MEMORY;
        int times=DataName.size()/capacity+1;
        int start=0;
        while(--times>=0)
        {
            flushToRelation(relation_reference,DataName,DataValues,start);
            start+=capacity;
        }
    }

    private void getTuplesFromRelation(Relation relation_reference,int start,int number, SearchCondition sc)
    {
        clearMem();
       relation_reference.getBlocks(start,0,number>Config.NUM_OF_BLOCKS_IN_MEMORY?Config.NUM_OF_BLOCKS_IN_MEMORY:number);
        Block block=null;
        for(int i=0;i<Config.NUM_OF_BLOCKS_IN_MEMORY;++i)
        {
            block=mem.getBlock(i);
            if(block.isEmpty())break;
            for(int j=0;j<block.getNumTuples();++j)
            {
                if(sc.isEmpty||sc.satisfy(block.getTuple(j)))
                {
                    System.out.println(block.getTuple(j));
                }
            }
        }
    }

    public void selectFromTable(String TableName,SearchCondition sc)
    {
        Relation relation_reference=schema_manager.getRelation(TableName);
        int times=relation_reference.getNumOfBlocks()/Config.NUM_OF_BLOCKS_IN_MEMORY;
        int start=0,number=relation_reference.getNumOfBlocks();
        if(sc.isEmpty)return ;
        else {
        while(--times>=0)
        {
            getTuplesFromRelation(relation_reference,start,number,sc);
            start+=Config.NUM_OF_BLOCKS_IN_MEMORY;
            number-=Config.NUM_OF_BLOCKS_IN_MEMORY;
        }
        }
    }

//    private int writeBackToRelation(Relation relation_reference,ArrayList<Tuple>ans,int start,int total)
//    {
//        clearMem();
//        int index=0;
//        Block block=mem.getBlock(index);
//        for(int i=start;i<ans.size();++i)
//        {
//            block.appendTuple(ans.get(i));
//            if(block.isFull()||i==ans.size()-1)
//            {
//                relation_reference.setBlock(total++,index);
//                ++index;
//                if(index>=Config.NUM_OF_BLOCKS_IN_MEMORY)break;
//                block=mem.getBlock(index);
//            }
//        }
//        return total;
//    }

    public void deleteFromTable(String TableName,SearchCondition sc)
    {
       int totaltuple=0;
        int tuplepos=0;
        Relation relation_reference=schema_manager.getRelation(TableName);
        if(sc.isEmpty)
        {
            relation_reference.deleteBlocks(0);
            return;
        }else
        {
            int total=relation_reference.getNumOfBlocks();
            int start=0;
            int relation_block_index=0;
            while(start<total)
            {
                int blockpos=0;
                if(start==0)
                {
                    relation_reference.getBlocks(start,0,Config.NUM_OF_BLOCKS_IN_MEMORY);
                    Block block=null;
                    for(int i=0;i<Config.NUM_OF_BLOCKS_IN_MEMORY;++i)
                    {
                        block=mem.getBlock(i);
                        if(block.isEmpty())break;
                        for(int j=0;j<block.getNumTuples();++j)
                        {
                            if(!sc.satisfy(block.getTuple(j)))
                            {
                                totaltuple++;
                                if(tuplepos==relation_reference.getSchema().getTuplesPerBlock())
                                {
                                    blockpos++;
                                    tuplepos=0;
                                }
                                mem.getBlock(blockpos).setTuple(tuplepos++,block.getTuple(j));
                            }
                        }
                    }
                    if(blockpos>0)
                    {
                        relation_reference.setBlocks(relation_block_index,0,blockpos);
                        relation_block_index+=blockpos;
                    }
                    if(tuplepos!=0)
                    {
                        mem.setBlock(0,mem.getBlock(blockpos+1));
                        for(int j=tuplepos;j<relation_reference.getSchema().getTuplesPerBlock();++j)
                        {
                            mem.getBlock(0).invalidateTuple(j);
                        }
                    }
                    start+=Config.NUM_OF_BLOCKS_IN_MEMORY;
                }else
                {
                    relation_reference.getBlocks(start,1,Config.NUM_OF_BLOCKS_IN_MEMORY);
                    Block block=null;
                    for(int i=1;i<Config.NUM_OF_BLOCKS_IN_MEMORY;++i)
                    {
                        block=mem.getBlock(i);
                        if(block.isEmpty())break;
                        for(int j=0;j<block.getNumTuples();++j)
                        {
                            if(!sc.satisfy(block.getTuple(j)))
                            {
                                totaltuple++;
                                if(tuplepos==relation_reference.getSchema().getTuplesPerBlock())
                                {
                                    blockpos++;
                                    tuplepos=0;
                                }
                                mem.getBlock(blockpos).setTuple(tuplepos++,block.getTuple(j));
                            }
                        }
                        if(blockpos>0)
                        {
                            relation_reference.setBlocks(relation_block_index,0,blockpos);
                            relation_block_index+=blockpos;
                        }
                        if(tuplepos!=0)
                        {
                            mem.setBlock(0,mem.getBlock(blockpos+1));
                            for(int j=tuplepos;j<relation_reference.getSchema().getTuplesPerBlock();++j)
                            {
                                mem.getBlock(0).invalidateTuple(j);
                            }
                        }
                        start+=Config.NUM_OF_BLOCKS_IN_MEMORY-1;
                    }
                }
            }
            relation_reference.deleteBlocks(totaltuple/relation_reference.getSchema().getTuplesPerBlock());
            if(!mem.getBlock(0).isEmpty())relation_reference.setBlock(relation_reference.getNumOfBlocks(),0);
        }

    }
//    public void updateTable(String TableName, SearchCondition sc, ArrayList<String>fieldName,ArrayList<String>newValue)
//    {
//        Relation relation_reference=schema_manager.getRelation(TableName);
//        ArrayList<Tuple>res=getAllTuplesFromRelation(relation_reference);
//        Schema sch=relation_reference.getSchema();
//        for(int i=0;i<res.size();++i)
//        {
//            if(sc.isEmpty||sc.satisfy(res.get(i)))
//            {
//                for(int j=0;j<fieldName.size();++j)
//                {
//                    int index=sch.getFieldOffset(fieldName.get(j));
//                    if(sch.getFieldType(fieldName.get(j))==FieldType.STR20)res.get(i).setField(index,newValue.get(j));
//                    else res.get(i).setField(index,Integer.parseInt(newValue.get(j)));
//                }
//            }
//        }
//        int capacity = relation_reference.getSchema().getTuplesPerBlock()*Config.NUM_OF_BLOCKS_IN_MEMORY;
//        int times = res.size() / capacity + 1;// times we need to flush to relation;
//        int start = 0, total = 0;
//        while (--times >= 0)
//        {
//            total=writeBackToRelation(relation_reference, res, start, total);
//            start += capacity;
//        }
//    }
    public static void main(String[] args) {
        //create table
        String []name={"f1","f2","f3","f4"};
        String []type={"STR20", "INT", "INT", "STR20"};
        String [][]names={{ "f1", "f2", "f3", "f4" }, { "f1", "f2", "f3", "f4" }, { "f1", "f2", "f3", "f4" }, { "f1", "f2", "f3", "f4" }};
        String [][]values={{ "h1", "20", "30", "h2" }, { "h3", "40", "50", "h4" }, { "h5", "60", "70", "h6" }, { "h7", "80", "90", "h8" },{ "h1", "20", "30", "h2" }, { "h3", "40", "50", "h4" }, { "h5", "60", "70", "h6" }, { "h7", "80", "90", "h8" },{ "h1", "20", "30", "h2" }, { "h3", "40", "50", "h4" }, { "h5", "60", "70", "h6" }, { "h7", "80", "90", "h8" },{ "h1", "20", "30", "h2" }, { "h3", "40", "50", "h4" }, { "h5", "60", "70", "h6" }, { "h7", "80", "90", "h8" },{ "h1", "20", "30", "h2" }, { "h3", "40", "50", "h4" }, { "h5", "60", "70", "h6" }, { "h7", "80", "90", "h8" },{ "h1", "20", "30", "h2" }, { "h3", "40", "50", "h4" }, { "h5", "60", "70", "h6" }, { "h7", "80", "90", "h8" }};
        ArrayList<String> field_name=new ArrayList<String>(Arrays.asList(name));
        ArrayList<String> field_type=new ArrayList<String>(Arrays.asList(type));
        ArrayList<ArrayList<String>>DataNames=new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>>DataValues=new ArrayList<ArrayList<String>>();
        for(int i=0;i<24;++i)
        {
            //DataNames.add(new ArrayList<String>(Arrays.asList(names[i])));
            DataValues.add(new ArrayList<String>(Arrays.asList(values[i])));
        }
        Function mf=new Function();

        String TableName="wangtao";
        //create table
        mf.createTable(TableName,field_name,field_type);
        //insert into table
        System.out.println("Insert into table");
        mf.insertIntoTableNtimes(TableName,field_name,DataValues);
        Relation relation_reference=schema_manager.getRelation(TableName);
        System.out.println("-----------relation-------------------------");
        System.out.print(relation_reference + "\n");

        System.out.println("------------------test select------------------");

        SearchCondition SC=new SearchCondition();



        ArrayList<String>fieldName=new ArrayList<String>();
        ArrayList<String>newValue=new ArrayList<String>();
        fieldName.add("f1");
        fieldName.add("f4");
        newValue.add("haha");
        newValue.add("hehe");
       // for(int i=0;i<res.size();++i)System.out.println(res.get(i));






    }
}
