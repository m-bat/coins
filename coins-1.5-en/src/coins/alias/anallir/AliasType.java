/* ---------------------------------------------------------------------
%   Copyright (C) 2012 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.alias.anallir;

/*
 * Created on 2005/11/12
 */

/**
 * @author K_Yoshiba
 */
public class AliasType {

    protected boolean bottom;

    public AliasType() {
        bottom = true;
    }

    public boolean isBottom() {
        return bottom;
    }

    public void unify(AliasType type) {
    }

    public String toString() {
        return "BOT";
    }

}
