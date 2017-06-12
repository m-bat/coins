/* ---------------------------------------------------------------------
%   Copyright (C) 2012 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.alias.anallir;

import java.util.Vector;

/*
 * Created on 2005/11/12
 */

/**
 * @author K_Yoshiba
 */
public class AliasAnalysis {

    //x = y
    public void assign(ECR var1, ECR var2) {
        AlphaType type1 = (AlphaType) var1.getECR().getType();
        AlphaType type2 = (AlphaType) var2.getECR().getType();

        ECR tau1 = type1.getTau();
        ECR tau2 = type2.getTau();
        ECR lam1 = type1.getLambda();
        ECR lam2 = type2.getLambda();

        if (!tau1.equivalent(tau2)) {
            tau1.cjoin(tau2);
        }
        if (!lam1.equivalent(lam2)) {
            lam1.cjoin(lam2);
        }
    }

    //x = &y
    public void assignAddr(ECR var1, ECR var2) {
        AlphaType type1 = (AlphaType) var1.getECR().getType();
        ECR tau1 = type1.getTau();
        ECR tau2 = var2.getECR();
        if (!tau1.equivalent(tau2)) {
            tau1.join(tau2);
        }
    }

    //x = *y
    public void assignPtr(ECR var1, ECR var2) {
        AlphaType type1 = (AlphaType) var1.getECR().getType();
        AlphaType type2 = (AlphaType) var2.getECR().getType();

        ECR tau1 = type1.getTau();
        ECR lam1 = type1.getLambda();
        ECR tau2 = type2.getTau();

        if (tau2.getType().isBottom()) {
            tau2.setType(type1);
        } else {
            ECR tau3 = ((AlphaType) tau2.getType()).getTau();
            ECR lam3 = ((AlphaType) tau2.getType()).getLambda();

            if (!tau1.equivalent(tau3)) {
                tau1.cjoin(tau3);
            }
            if (!lam1.equivalent(lam3)) {
                lam1.cjoin(lam3);
            }
        }
    }

    //*x = y
    public void assignToPtr(ECR var1, ECR var2) {
        AlphaType type1 = (AlphaType) var1.getECR().getType();
        AlphaType type2 = (AlphaType) var2.getECR().getType();
        ECR tau1 = type1.getTau();
        ECR tau2 = type2.getTau();
        ECR lambda2 = type2.getLambda();

        if (tau1.getType().isBottom()) {
            tau1.setType(type2);
        } else {
            ECR tau3 = ((AlphaType) tau1.getType()).getTau();
            ECR lambda3 = ((AlphaType) tau1.getType()).getLambda();
            if (!tau2.equivalent(tau3)) {
                tau3.cjoin(tau2);
            }
            if (!lambda2.equivalent(lambda3)) {
                lambda3.cjoin(lambda2);
            }
        }
    }

    //x = op(...)
    public void assignOp(ECR var1, Vector operands) {
        AlphaType type1 = (AlphaType) var1.getECR().getType();
        ECR tau1 = type1.getTau();
        ECR lambda1 = type1.getLambda();
        int size = operands.size();

        for (int i = 0; i < size; i++) {
            AlphaType type2 = (AlphaType) ((ECR) operands.get(i)).getECR()
                    .getType();
            ECR tau2 = type2.getTau();
            ECR lambda2 = type2.getLambda();
            if (!tau1.equivalent(tau2)) {
                tau1.cjoin(tau2);
            }
            if (!lambda1.equivalent(lambda2)) {
                lambda1.cjoin(lambda2);
            }
        }
    }

    public void functionDefinition(ECR func, Vector args, ECR retVal) {
        ECR lambda = ((AlphaType) func.getECR().getType()).getLambda();
        int n = args.size();

        if (lambda.getType().isBottom()) {
            Vector vec = new Vector(n);
            for (int i = 0; i < n; i++) {
                ECR ecr = (ECR) ((ECR) args.get(i)).find();
                vec.add(new ECR(null, ecr.getType()));
            }
            ECR r = null;
            if (retVal != null) {
                r = new ECR(null, retVal.getType());
            }
            lambda.setType(new LambdaType(vec, r));

        } else {

            Vector ecrs = ((LambdaType) lambda.getType()).getArgs();
            for (int i = 0; i < n; i++) {
                AlphaType aType1 = (AlphaType) ((ECR) ecrs.get(i)).getType();
                ECR tau1 = aType1.getTau();
                ECR lambda1 = aType1.getLambda();
                AlphaType aType2 = (AlphaType) ((ECR) args.get(i)).getType();
                ECR tau2 = aType2.getTau();
                ECR lambda2 = aType2.getLambda();
                if (!tau1.equivalent(tau2)) {
                    tau2.join(tau1);
                }
                if (!lambda1.equivalent(lambda2)) {
                    lambda2.join(lambda1);
                }
            }

            if (retVal != null) {
                AlphaType aType1 = (AlphaType) ((LambdaType) lambda.getType())
                        .getRetVal().getType();
                ECR tau1 = aType1.getTau();
                ECR lambda1 = aType1.getLambda();
                AlphaType aType2 = (AlphaType) retVal.getType();
                ECR tau2 = aType2.getTau();
                ECR lambda2 = aType2.getLambda();
                if (!tau1.equivalent(tau2)) {
                    tau1.join(tau2);
                }
                if (!lambda1.equivalent(lambda2)) {
                    lambda1.join(lambda2);
                }
            }
        }
    }

    public void functionCall(ECR func, Vector args, ECR retVal) {
        ECR lambda = ((AlphaType) func.getECR().getType()).getLambda();
        int n = args.size();

        if (lambda.getType().isBottom()) {
            Vector vec = new Vector(n);
            for (int i = 0; i < n; i++) {
                vec.add(new ECR(null, new AlphaType()));
            }
            lambda.setType(new LambdaType(vec, new ECR(null, new AlphaType())));
        }

        LambdaType lType = (LambdaType) lambda.getType();
        for (int i = 0; i < n; i++) {
            AlphaType aType1 = (AlphaType) ((ECR) lType.getArgs().get(i))
                    .getECR().getType();
            ECR tau1 = aType1.getTau();
            ECR lambda1 = aType1.getLambda();
            AlphaType aType2 = (AlphaType) ((ECR) args.get(i)).getECR()
                    .getType();
            ECR tau2 = aType2.getTau();
            ECR lambda2 = aType2.getLambda();
            if (!tau1.equivalent(tau2)) {
                tau1.cjoin(tau2);
            }
            if (!lambda1.equivalent(lambda2)) {
                lambda1.cjoin(lambda2);
            }
        }

        if ((retVal != null) && (lType.getRetVal() != null)) {
            AlphaType aType1 = (AlphaType) lType.getRetVal().getECR().getType();
            ECR tau1 = aType1.getTau();
            ECR lambda1 = aType1.getLambda();
            AlphaType aType2 = (AlphaType) retVal.getECR().getType();
            ECR tau2 = aType2.getTau();
            ECR lambda2 = aType2.getLambda();
            if (!tau1.equivalent(tau2)) {
                tau2.cjoin(tau1);
            }
            if (!lambda1.equivalent(lambda2)) {
                lambda2.cjoin(lambda1);
            }
        }
    }

    public void assignAlloc(ECR ecr) {
        ECR tau = ((AlphaType) ecr.getECR().getType()).getTau();
        if (tau.getType().isBottom()) {
            ECR tmpEcr = new ECR(null, new AlphaType());
            tau.setType(tmpEcr.getECR().getType());
        }
    }
}
