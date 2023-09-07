/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.language.component;

import java.util.Objects;

import analysis.language.Visibility;

/**
 * Representation of an instance variable.
 * 
 * @author Ada Clevinger
 * @author Sung Ho Yoon
 * 
 * @since 1.0
 */
public class InstanceVariable extends ClassComponent {

    /** Whether this variable is static */
    private boolean isStatic;
    /** Whether this variable is marked as final */
    private boolean isFinal;

    /**
     * Constructs a new {@code InstanceVariable}.
     * 
     * @param vis  the visibility of this instance variable
     * @param name the name of this instance variable
     * @param type the type of this instance variable
     */
    public InstanceVariable(Visibility vis, String name, String type) {
        super(type, name, vis);
    }

    /**
     * Marks whether this function is static or not.
     * 
     * @param in whether this function is static
     */
    public void setStatic(boolean in) {
        isStatic = in;
    }

    /**
     * Checks whether this variable is static.
     * 
     * @return {@code true} if this variable is static
     */
    public boolean getStatic() {
        return isStatic;
    }

    /**
     * Marks whether this variable is marked as final or not.
     * 
     * @param in whether this variable is marked as final
     */
    public void setFinal(boolean in) {
        isFinal = in;
    }

    /**
     * Checks whether this variable is marked as final.
     * 
     * @return {@code true} if this variable is marked as final
     */
    public boolean getFinal() {
        return isFinal;
    }

    /**
     * Returns the data type of this variable.
     * 
     * @return the data type of this variable
     */
    @Override
    public String getType() {
        return super.getType();
    }

    /**
     * Checks whether an object is "equal to" this {@code InstanceVariable}.
     * 
     * @param obj an object to compare with
     * @return {@code true} if argument is "equal to" this {@code InstanceVariable}
     * 
     * @since 2.0
     */
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

    /**
     * Returns a hash code value for this {@code InstanceVariable}.
     * 
     * @return a hash code value
     * 
     * @since 2.0
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.getName(), super.getType(), super.getVisibility(), isStatic, isFinal);
    }

}
