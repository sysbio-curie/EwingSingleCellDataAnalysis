package vdaoengine.image;

/*
 * Title:        VDAO Engine and testing enviroment
 * Description:  Generic interface for visualization procedure
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0

*/

import vdaoengine.*;
import vdaoengine.data.*;

import java.util.*;
import java.lang.*;

public abstract class VDataImageElement{

public String name;
public boolean visible = true;

public VDataImageElement(String nam){
name = new String(nam);
}

public String toString(){
return name;
}

}