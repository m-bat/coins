/* ---------------------------------------------------------------------
%   Copyright (C) 2012 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.alias.anallir;

/*
 * Created on 2005/11/13
 */

import java.util.Vector;

/**
 * @author K_Yoshiba
 */
public class LambdaType extends AliasType {

    private Vector arguments;

    private ECR retVal;

    public LambdaType(Vector args, ECR retVal) {
        this.arguments = args;
        this.retVal = retVal;
        bottom = false;
    }

    public LambdaType() {
        this.arguments = null;
        this.retVal = null;
        bottom = true;
    }

    public Vector getArgs() {
        return arguments;
    }

    public ECR getRetVal() {
        return retVal;
    }

    public void unify(LambdaType type) {
        int len = arguments.size();
        for (int i = 0; i < len; i++) {
            AlphaType a1 = (AlphaType) ((ECR) arguments.get(i)).getType();
            AlphaType a2 = (AlphaType) ((ECR) type.arguments.get(i)).getType();
            ECR tau1 = a1.getTau();
            ECR tau2 = a2.getTau();
            ECR lambda1 = a1.getLambda();
            ECR lambda2 = a2.getLambda();
            if (!tau1.equivalent(tau2)) {
                tau1.join(tau2);
            }
            if (!lambda1.equivalent(lambda2)) {
                lambda1.join(lambda2);
            }
        }

        if (retVal != null) {
            AlphaType a1 = (AlphaType) retVal.getType();
            AlphaType a2 = (AlphaType) type.retVal.getType();
            ECR tau1 = a1.getTau();
            ECR tau2 = a2.getTau();
            ECR lambda1 = a1.getLambda();
            ECR lambda2 = a2.getLambda();
            if (!tau1.equivalent(tau2)) {
                tau1.join(tau2);
            }
            if (!lambda1.equivalent(lambda2)) {
                lambda1.join(lambda2);
            }
        }
    }
}
