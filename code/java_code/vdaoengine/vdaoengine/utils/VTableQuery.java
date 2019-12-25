package vdaoengine.utils;

import java.io.*;
import java.util.*;
import vdaoengine.data.*;

public class VTableQuery {

  public VDataTable table = null;
  public HashMap hm = null;

  public void hashTable(String keyField){
    hm = new HashMap();
    int id = table.fieldNumByName(keyField);
    if(id!=-1){
      for(int i=0;i<table.rowCount;i++)
          hm.put(table.stringTable[i][id],new Integer(i));
    }
  }

  public int queryHash(String key){
    int res = -1;
    Integer I = (Integer)hm.get(key);
    if(I!=null) res = I.intValue();
    return res;
  }

}