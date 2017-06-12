/* ---------------------------------------------------------------------
%   Copyright (C) 2012 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.alias.anallir;

/*
 * Created on 2005/11/13
 */

/**
 * @author K_Yoshiba
 */
public class AlphaType extends AliasType {

	private ECR tau;

	private ECR lambda;

	public AlphaType(ECR tau, ECR lambda) {
		this.tau = tau;
		this.lambda = lambda;
		bottom = false;
	}

	public AlphaType() {
		this.tau = new ECR(null, new TauType());
		this.lambda = new ECR(null, new LambdaType());
		bottom = false;
	}

	public ECR getTau() {
		return (ECR) tau.find();
	}

	public ECR getLambda() {
		return (ECR) lambda.find();
	}

	public void unify(AlphaType type) {
		ECR tau1 = getTau();
		ECR tau2 = type.getTau();
		if (!tau1.equivalent(tau2)) {
			tau1.join(tau2);
		}

		ECR lambda1 = getLambda();
		ECR lambda2 = type.getLambda();
		if (!lambda1.equivalent(lambda2)) {
			lambda1.join(lambda2);
		}
	}

}
