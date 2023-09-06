/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.language.component;

import java.util.Objects;

import analysis.language.Visibility;

public class InstanceVariable extends ClassComponent {

    private boolean isStatic;

    private boolean isFinal;

    public InstanceVariable(Visibility vis, String name, String type) {
        super(type, name, vis);
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        else if (super.equals(obj) && obj instanceof InstanceVariable) {
            InstanceVariable instVar = (InstanceVariable) obj;
            return this.isStatic == instVar.isStatic && this.isFinal == instVar.isFinal;
        } else
            return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getName(), super.getType(), super.getVisibility(), isStatic, isFinal);
    }

}
