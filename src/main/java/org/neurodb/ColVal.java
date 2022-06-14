package org.neurodb;

import java.util.List;

public class ColVal{
    Object val;
    int type;
    int aryLen=0;
    double getNum(){
        return Double.parseDouble(val.toString());
    }

    double[] getNumArray(){
        return (double[])val;
    }

    String getString(){
        return val.toString();
    }

    String[] getStringArry(){
        return (String[])val;
    }

    RecordSet.Node getNode(){
        return (RecordSet.Node)val;
    }

    RecordSet.Link getLink(){
        return (RecordSet.Link)val;
    }

    List getPath(){
        return (List)val;
    }

    public int getAryLen() {
        return aryLen;
    }

    public void setAryLen(int aryLen) {
        this.aryLen = aryLen;
    }

    public Object getVal() {
        return val;
    }

    public void setVal(Object val) {
        this.val = val;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
