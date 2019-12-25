package vdaoengine;

/**
 * <p>Title: VDAO Engine and testing enviroment</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: IHES</p>
 * @author Andrey Zinovyev
 * @version 1.0
 */

import vdaoengine.trees.*;

public class DrawingTrees {

  public static void main(String[] args) {
    try{
    SimpleBinaryTree tr = new SimpleBinaryTree();
    tr.loadFromFile("treefile");
    //System.out.println(tr.getRoot().toString());
    }catch(Exception e){
      e.printStackTrace();
    }

  }
}