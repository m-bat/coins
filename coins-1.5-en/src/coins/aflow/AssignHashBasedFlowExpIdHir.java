/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * AssignHashBasedFlowExpIdHir.java
 *
 * Created on August 14, 2002, 2:17 PM
 *
 * HIR implementation of <code>AssignHashBasedFlowExpId</code>.
 */
package coins.aflow;

import coins.ir.IR;
import coins.ir.hir.HIR;


/**
 *
 * @author  hasegawa
 */
public class AssignHashBasedFlowExpIdHir extends AssignHashBasedFlowExpId {
    AssignHashBasedFlowExpIdHir(SubpFlow pSubpFlow) {
        super(pSubpFlow);
    }

    /*        HashBasedFlowExpId newHashBasedFlowExpId(IR pIR, int pIndex, FlowResults pResults)
            {
                    return new HashBasedFlowExpIdHir((HIR)pIR, pIndex, pResults);
            }
    */
    HashBasedFlowExpId newHashBasedFlowExpId(IR pIR, int pIndex,
        SubpFlow pSubpFlow) {
        return new HashBasedFlowExpIdHir((HIR) pIR, pIndex, pSubpFlow);
    }
}
