/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.language.component;

import java.util.Objects;

import analysis.language.Visibility;

/**
 * Components of a class.
 * 
 * @see analysis.language.actor.GenericClass
 * 
 * @author Ada Clevinger
 * @author Sung Ho Yoon
 */
public abstract class ClassComponent {

    /** The name of this component */
    private String name;
    /** The type of this component */
    private String type;
    /** The visibility of this component */
    private Visibility visibility;

    /**
     * Constructs a new {@code ClassComponent}.
     * 
     * @param type       the type of this component
     * @param name       the name of this component
     * @param visibility the visibility of this component
     */
    public ClassComponent(String type, String name, Visibility visibility) {
        this.type = type;
        this.name = name;
        this.visibility = visibility;
    }

    /**
     * Returns the visibility of this component.
     * 
     * @return the visibility of this component
     */
    public Visibility getVisibility() {
        return visibility;
    }

    /**
     * Returns the type of this component. A subclass must specify
     * what the type means in the context of the subclass.
     * 
     * @return the type of this component
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the name of this component.
     * @return the name of this component
     */
    public String getName() {
        return name;
    }

    /**
     * Checks whether an object is "equal to" this {@code ClassComponent}.
     * 
     * @param obj an object to compare with
     * @return {@code true} if argument is "equal to" this {@code ClassComponent}
     * 
     * @since 2.0
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        else if (obj instanceof ClassComponent) {
            ClassComponent clsComp = (ClassComponent) obj;
            return this.name.equals(clsComp.name) && this.type.equals(clsComp.type)
                    && this.visibility.equals(clsComp.visibility);
        } else
            return false;
    }

    /**
     * Returns a hash code value for this {@code ClassComponent}.
     * 
     * @return a hash code value
     * 
     * @since 2.0
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, type, visibility);
    }
}
