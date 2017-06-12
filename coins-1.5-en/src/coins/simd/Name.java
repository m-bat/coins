/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import java.lang.Character;
import java.lang.Integer;
import java.lang.String;
import java.util.HashMap;

public class Name {
  private HashMap nameMap;
  public Name() {
    nameMap = new HashMap();
  }
  public String refName(String oname) {
    return (String)nameMap.get(oname);
  }
  public String newName(String oname) {
    String nname;
    String cname=(String)nameMap.get(oname);
    if(cname==null) {
      nname=oname+"_1";
    } else {
      int nNum=nameNumber(cname);
      nname=origName(cname)+"_"+(++nNum);
    }
    nameMap.put(oname, nname);
    return nname;
  }
  public String origName(String name) {
    int i=name.lastIndexOf('_');
    if(i<0) return name;
    return name.substring(0, i);
  }
  private int nameNumber(String name) {
    int i=name.lastIndexOf('_');
    String nstr=name.substring(++i);
    return Integer.decode(nstr).intValue();
  }
  public static void main(String[] args) {
    Name nm=new Name();
    String oname="a";
    String nname=nm.newName(oname);
    System.out.println(oname+":newName="+nname+"  "+nm.refName(oname));
    String nname1=nm.newName(oname);
    System.out.println(oname+":newName="+nname1+"  "+nm.refName(oname));
    System.out.println("origName("+nname1+")="+nm.origName(nname1));
  }
}
