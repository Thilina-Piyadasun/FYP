package org.abithana.utill;

import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by minudika on 8/8/16.
 */
public class Converter implements Serializable {

    public ArrayList<ArrayList> convert(DataFrame df){
        ArrayList<ArrayList> records = new ArrayList<ArrayList>();
        String FnTs[] = extract(df);

        List<String> types = new ArrayList();
        for(String s : FnTs){
            types.add(s.split(":")[1].trim().toString());
        }

        System.out.println(types.toString());

        List list = df.collectAsList();

        for(Object row:list){
            ArrayList<Object> record = new ArrayList<Object>();
            String data[] = extract(row);
            for(int i=0; i<data.length; i++){
                record.add(convertEntry(types.get(i),data[i]));
            }
            records.add(record);
        }
        return records;
    }


    public List<String> singleColtoList(DataFrame df){


        List<String> list=df.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                String items = row.getString(0);
                return items;
            }
        }).collect();

        return list;
    }
    private String[] extract(Object row){
        String str = row.toString();
        return str.substring(1,str.length()-1).split(",");
    }

    private Object convertEntry(String type,String ob){
        if("string".equals(type.toString().trim())){
            return ob.toString();
        }
        else if("int".equals(type.toString().trim())){
            return Integer.parseInt(ob);
        }
        else if("double".equals(type.toString().trim())){
            return Double.parseDouble(ob);
        }
        else{
            return ob.toString();
        }

        //TODO : handle exceptions
    }
}
