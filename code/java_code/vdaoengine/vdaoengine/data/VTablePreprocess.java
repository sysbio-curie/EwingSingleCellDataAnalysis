package vdaoengine.data;

/**
 * Title:        VDAO Engine and testing enviroment
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0
 */

public interface VTablePreprocess {
  public void process(float row[]);
  public void unprocess(float row[]);
}
