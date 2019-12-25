package vdaoengine.analysis.grammars;

import java.io.*;
import java.util.*;

public abstract class GraphElement {
	
	public String label = "";
	public float weight = 1f; 
	
	  public Vector Attributes = new Vector();	
		
	  /**
	   * Returns all attributes attached with a given name
	   * @param nam
	   * @return
	   */
	  public Vector getAttributeValues(String nam){
	    Vector res = new Vector();
	    for(int i=0;i<Attributes.size();i++){
	      Attribute attr = (Attribute)Attributes.get(i);
	      if(attr.value!=null)
	      if(attr.name.equals(nam))
	        res.add(attr.value);
	    }
	    return res;
	  }
	  /**
	   * Finds first in the list attribute with a given name
	   * @param nam
	   * @return
	   */
	  public String getFirstAttributeValue(String nam){
	    String s = null;
	    Vector res = getAttributeValues(nam);
	    if(res.size()>0)
	      s = (String)res.get(0);
	    return s;
	  }

	  /**
	   * Checks if the attribute with such a name already attached and if yes,
	   * then changes its value, if no then creates a new one
	   * @param nam
	   * @param val
	   */
	  public void setAttributeValueUnique(String nam, String val){
	    boolean found = false;
	    for(int i=0;i<Attributes.size();i++){
	      Attribute attr = (Attribute)Attributes.get(i);
	      if(attr.name.equals(nam)){
	        found = true;
	        attr.value = val;
	      }
	    }
	    if(!found)
	      Attributes.add(new Attribute(nam,val));
	  }
	  /**
	   * Finds all attributes in whose name the substring is contained
	   * @param substring
	   * @return
	   */
	  public Vector getAttributesWithSubstringInName(String substring){
		  Vector res = new Vector();
		  for(int i=0;i<this.Attributes.size();i++){
			  Attribute at = (Attribute)this.Attributes.get(i);
			  if(at.name.toLowerCase().indexOf(substring.toLowerCase())>=0)
				  res.add(at);
		  }
		  return res;
	  }	
	  
	  public void copyProperties(GraphElement ge){
		  ge.label = label;
		  ge.weight = weight;
		  for(int i=0;i<Attributes.size();i++){
			Attribute at = (Attribute)Attributes.get(i);
			Attribute atc = at.clone();
			ge.Attributes.add(atc);
		  }
	  }
	
}
