/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * ListValuedMap.java
 *
 * Created on February 13, 2003, 6:01 PM
 */
//##63 package coins.aflow.util;
package coins.flow;

import java.util.ArrayList;
import java.util.List;


/**
 * Map a key to a list of objects.
 * @author  hasegawa
 */
public class ListValuedMap extends java.util.HashMap
{

/** Creates a new instance of ListValuedMap */
public
ListValuedMap()
{
}

/** Get the list corresponding to the key pKey.
 *  If pKey is not contained in the map, then add pKey as a new key
 *  having an empty list.
 *  @return the list corresponding to pKey.
 */
public Object
get(Object pKey)
{
  if (!containsKey(pKey)){
     List lList = new ArrayList();
     put(pKey, lList);
  }
  return super.get(pKey);
} // get

/** Add pNewEntry to the list corresponding to pKey.
 *  If the list already contains pNewEntry then addition is not done
 *  and false is returned.
 * @return true if added, false if not added (already cantained).
 */
public boolean
addUnique(Object pKey, Object pNewEntry)
{
  List lList = (List) get(pKey);
  if (lList.contains(pNewEntry)) {
    return false;
  }
  return lList.add(pNewEntry);
} // addUnique

//##65 BEGIN
public boolean
removeIfContained( Object pKey, Object pEntry )
{
  if (!containsKey(pKey))
    return false;
  List lList = (List) get(pKey);
  int lIndex = lList.indexOf(pEntry);
  if (lIndex >= 0) {
    lList.remove(lIndex);
    return true;
  }
  return false;
} // removeIfContained

public boolean
  removeAllEntries( Object pKey )
{
  if (!containsKey(pKey))
    return false;
  List lList = (List) get(pKey);
  lList.clear();
  return true;
}
} // ListValueMap
