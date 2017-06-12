/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * AliasDriver.java
 *
 * Created on July 18, 2003, 11:36 AM
 */

package coins.alias.alias2;

import java.util.Iterator;
import coins.HirRoot;
import coins.alias.AliasAnal;
//import coins.alias.AliasDriver;
import coins.driver.CoinsOptions;
import coins.ir.hir.Program;
import coins.ir.hir.SubpDefinition;

/**
 *
 * @author  hasegawa
 */
public class AliasDriver extends coins.alias.AliasDriver
{
    
    
    public void makeHirAliasAnalysis(HirRoot hirRoot)
    {
        CoinsOptions lCoinsOpts = hirRoot.ioRoot.getCompileSpecification().getCoinsOptions();
        
        coins.ir.IrList subpDefList //##12
        = ((Program)hirRoot.programRoot).getSubpDefinitionList();
        Iterator subpDefIterator = subpDefList.iterator();
        while (subpDefIterator.hasNext())
        {
            SubpDefinition subpDef = (SubpDefinition)(subpDefIterator.next());
            hirRoot.symRoot.useSymTableOfSubpDefinition(subpDef);
            AliasAnal lAlias = new AliasAnalHir2(hirRoot);
            lAlias.prepareForAliasAnalHir(subpDef);
            if (lCoinsOpts.isSet("aliasopt"))
                testAliasByOptimizing(lAlias, subpDef, hirRoot);
            else
                lAlias.printAliasPairs(subpDef);
//                new coins.alias.AliasDriver.MyVisitor(lAlias, subpDef, hirRoot);
        }
    }
    
    
    
   
    
    public static void main(String[] args)
    {
        AliasDriver lAliasDriver = new AliasDriver();
        lAliasDriver.myName = "AliasDriver2";
        lAliasDriver.go(args);
    }
}


