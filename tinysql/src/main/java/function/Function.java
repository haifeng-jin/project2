package function; /**
 * Created by Tao on 11/18/2016.
 */
import java.util.ArrayList;

import storageManager.*;
import expression.SearchCondition;

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


    public static ArrayList<String> tupleToString(Tuple tuple) {
        ArrayList<String> ret = new ArrayList<String>();
        for (int i = 0; i < tuple.getNumOfFields(); i++) {
            ret.add(tuple.getField(i).toString());
        }
        return ret;
    }

    public ArrayList<Tuple> getAllTuplesFromTable(String TableName)
    {
        Relation relation_reference=schema_manager.getRelation(TableName);
        int start=0,total=relation_reference.getNumOfBlocks(),number=total;
        ArrayList<Tuple>res=new ArrayList<Tuple>();
        while(start<total)
        {
            clearMem();

            if(number>0)
            {
                relation_reference.getBlocks(start,0,number>Config.NUM_OF_BLOCKS_IN_MEMORY?Config.NUM_OF_BLOCKS_IN_MEMORY:number);
                start+=Math.min(number,Config.NUM_OF_BLOCKS_IN_MEMORY);
                number-=Math.min(number,Config.NUM_OF_BLOCKS_IN_MEMORY);
                for(int i=0;i<Config.NUM_OF_BLOCKS_IN_MEMORY;++i)
                {
                    Block block=mem.getBlock(i);
                    for(int j=0;j<block.getNumTuples();++j)
                    {
                        if(!block.isEmpty() && !block.getTuple(j).isNull())
                        {
                            res.add(block.getTuple(j));
                        }
                    }
                }
            }
        }
        return res;
    }

    public Relation createTable(String TableName, ArrayList<String> DataNames, ArrayList<String> Datatypes)
    {
        System.out.println("Creating table " + TableName);
        ArrayList<FieldType> field_types=new ArrayList<FieldType>();
        for(String str:Datatypes)field_types.add(str.equals("STR20")?FieldType.STR20:FieldType.INT);
        Schema schema=new Schema(DataNames,field_types);
        Relation relation_reference=schema_manager.createRelation(TableName,schema);
        return relation_reference;
    }

    public void dropTable(String TableName)
    {
        System.out.println("Deleting Table "+TableName);
        schema_manager.deleteRelation(TableName);
    }

    private void insertIntoTable(Relation relation_reference,ArrayList<String> DataNames,ArrayList<String> DataValues,Block block)
    {
        Tuple tuple = relation_reference.createTuple(); //The only way to create a tuple is to call "Relation"
        int n=DataNames.size();
        Schema sc=relation_reference.getSchema();
        for(int i=0;i<n;++i)
        {
            if (sc.getFieldType(DataNames.get(i)).equals(FieldType.STR20))
                tuple.setField(DataNames.get(i),DataValues.get(i));
            else tuple.setField(DataNames.get(i),Integer.parseInt(DataValues.get(i)));
        }
        Schema tuple_schema = tuple.getSchema();
        block.appendTuple(tuple);
        //System.out.print("The table currently have " + relation_reference.getNumOfTuples() + " tuples" + "\n" + "\n");
    }
    private void flushToRelation(Relation relation_reference, ArrayList<String>DataName, ArrayList<String> DataValues, int start)
    {
        clearMem();
        Block block_ptr=mem.getBlock(0);
        if (relation_reference.getNumOfBlocks() == 0) {
            insertIntoTable(relation_reference,DataName,DataValues,block_ptr);
            relation_reference.setBlock(relation_reference.getNumOfBlocks(), 0);
            return;
        }
        int last_index = relation_reference.getNumOfBlocks() - 1;
        relation_reference.getBlock(last_index, 0);
        block_ptr=mem.getBlock(0);
        if (block_ptr.isFull()) {
            block_ptr = mem.getBlock(1);
            insertIntoTable(relation_reference,DataName,DataValues,block_ptr);
            relation_reference.setBlock(relation_reference.getNumOfBlocks(), 1);
            return;
        }
        insertIntoTable(relation_reference,DataName,DataValues,block_ptr);
        relation_reference.setBlock(relation_reference.getNumOfBlocks() - 1, 0);
    }

    public Relation insertIntoTableNtimes(String TableName, ArrayList<String>DataName, ArrayList<String> DataValues)
    {
        Relation relation_reference=schema_manager.getRelation(TableName);
        int capacity=relation_reference.getSchema().getTuplesPerBlock()*Config.NUM_OF_BLOCKS_IN_MEMORY;
        int times=(DataName.size() - 1)/capacity+1;
        int start=0;
        while(--times>=0)
        {
            flushToRelation(relation_reference,DataName,DataValues,start);
            start+=capacity;
        }
        return relation_reference;
    }

    String getTuplesFromRelation(Relation relation_reference, int start, int number, ArrayList<String> dataNames, SearchCondition sc)
    {
        String ret = "";
        clearMem();
        if (number == 0)
            return ret;
        relation_reference.getBlocks(start,0,number>Config.NUM_OF_BLOCKS_IN_MEMORY?Config.NUM_OF_BLOCKS_IN_MEMORY:number);
        Block block=null;
        for(int i=0;i<Config.NUM_OF_BLOCKS_IN_MEMORY;++i)
        {
            block=mem.getBlock(i);
            if(block.isEmpty())
                continue;
            for(int j=0;j<block.getNumTuples();++j)
            {
                Tuple tuple = block.getTuple(j);
                if(sc.isEmpty||sc.satisfy(tuple))
                {
                    if (dataNames.get(0).equals("*")) {
                        System.out.println(tuple);
                        ret += tuple + "\n";
                        continue;
                    }
                    String temp = "";
                    for (String name : dataNames) {
                        if (name.contains("."))
                            name = name.split("\\.")[1];
                        temp += tuple.getField(name) + "\t";
                    }
                    System.out.println(temp);
                    ret += temp + "\n";
                }
            }
        }
        return ret;
    }

    public String selectFromTable(String TableName, ArrayList<String> dataNames, SearchCondition sc)
    {
        System.out.println("SelectFromTable is called.");
        String ret = "";
        Relation relation_reference=schema_manager.getRelation(TableName);
        int times=(relation_reference.getNumOfBlocks() - 1)/Config.NUM_OF_BLOCKS_IN_MEMORY + 1;
        int start=0,number=relation_reference.getNumOfBlocks();
        while(--times>=0)
        {
            ret += getTuplesFromRelation(relation_reference,start,number,dataNames,sc);
            start+=Config.NUM_OF_BLOCKS_IN_MEMORY;
            number-=Config.NUM_OF_BLOCKS_IN_MEMORY;
        }
        return ret;
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
        System.out.println("Delete is called.");
        Relation relation_reference=schema_manager.getRelation(TableName);
        if(sc.isEmpty)
        {
            relation_reference.deleteBlocks(0);
            return;
        }
        int total=relation_reference.getNumOfBlocks();
        int start=0;
        while(start<total)
        {
            int block_num = Math.min(Config.NUM_OF_BLOCKS_IN_MEMORY, relation_reference.getNumOfBlocks());
            if (block_num == 0)
                return;
            relation_reference.getBlocks(start, 0, block_num);

            deleteTuplesInMemory(block_num, sc);
            relation_reference.setBlocks(start, 0, block_num);

            start+=block_num;
        }
    }

    private void deleteTuplesInMemory(int block_num, SearchCondition sc) {
        for(int i=1;i<block_num;++i) {
            Block block=mem.getBlock(i);
            for(int j=0;j<block.getNumTuples();++j) {
                if (sc.satisfy(block.getTuple(j))) {
                    block.invalidateTuple(j);
                }
            }
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
//        mf.insertIntoTableNtimes(TableName,field_name,DataValues);
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

    public static String getField(String[] tableArray, String name, ArrayList<String> tuple) {
        int indexCount = getFieldIndex(tableArray, name);
        if (indexCount == -1)
            return name;
        return tuple.get(indexCount);
    }

    public static int getFieldIndex(String[] tableArray, String name) {
        String tableName = tableArray[0];
        String columnName;
        if (name.contains(".")) {
            String[] splited = name.split("\\.");
            tableName = splited[0];
            columnName = splited[1];
        } else {
            columnName = name;
            for (String temp : tableArray) {
                if (schema_manager.getRelation(temp).getSchema().getFieldNames().contains(columnName)) {
                    tableName = temp;
                    break;
                }
            }
        }
        int indexCount = 0;
        for (String table : tableArray) {
            if (table.equals(tableName)) {
                break;
            }
            indexCount += schema_manager.getRelation(table).getSchema().getNumOfFields();
        }
        boolean found = false;
        for (String field : schema_manager.getRelation(tableName).getSchema().getFieldNames()) {
            if (columnName.equals(field)) {
                found = true;
                break;
            }
            indexCount++;
        }
        if (!found) {
            return -1;
        }
        return indexCount;
    }
}
