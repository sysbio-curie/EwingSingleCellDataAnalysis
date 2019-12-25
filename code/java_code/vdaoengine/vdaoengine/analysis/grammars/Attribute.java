package vdaoengine.analysis.grammars;

/**
 * Simple internal M implementation of node and edge attribute 
 *
 */
public class Attribute {

  public String name = "";
  public String value = "";

  public Attribute(String _name, String _val){
    name = _name;
    value = _val;
  }
  
  public Attribute clone(){
	  return new Attribute(name,value);
  }

}