/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.language.component;

import analysis.language.Visibility;

public class InstanceVariable extends ClassComponent{

    private boolean isStatic;

    private boolean isFinal;

    public InstanceVariable(Visibility vis, String nom, String typ) {
        super(typ, nom, vis);
    }

    public void setStatic(boolean in) {
        isStatic = in;
    }

    public boolean getStatic() {
        return isStatic;
    }

    public void setFinal(boolean in) {
        isFinal = in;
    }

    public boolean getFinal() {
        return isFinal;
    }

}
