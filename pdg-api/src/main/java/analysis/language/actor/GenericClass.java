/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.language.actor;

import java.util.ArrayList;
import java.util.List;

import analysis.language.Visibility;
import analysis.language.component.InstanceVariable;

/**
 * The platonic ideal of a class.
 * 
 * @author Ada Clevinger
 * @author Sung Ho Yoon
 * 
 * @since 1.0
 */
public class GenericClass extends GenericDefinition {

    // Instance Variables

    /** Whether this class is an abstract class. */
    private boolean isAbstract;
    /** The inheritance relationship that this class has. */
    private GenericDefinition inheritance;
    /** Instance variables that this class has. */
    private List<InstanceVariable> instanceVariables;

    // Constructors

    /**
     * Constructs a new {@code GenericClass}.
     * 
     * @param name    the name of this class
     * @param context the context that this class is in
     */
    public GenericClass(String name, String context) {
        super(name, context);
        instanceVariables = new ArrayList<InstanceVariable>();
        inheritance = null;
    }

    // Setter Methods

    /**
     * Adds an instance variable to this class.
     * 
     * @param in the instance variable to add
     */
    public void addInstanceVariable(InstanceVariable in) {
        instanceVariables.add(in);
    }

    /**
     * Adds an instance variable to this class.
     * 
     * @param vis      the visibility of the instance variable
     * @param name     the name of the instance variable
     * @param type     the type of the instance variable
     * @param isStatic whether the variable is static (i.e., a class variable)
     * @param isFinal  whether the variable is declared as final
     */
    public void addInstanceVariable(Visibility vis, String name, String type, boolean isStatic, boolean isFinal) {
        InstanceVariable iv = new InstanceVariable(vis, name, type);
        iv.setStatic(isStatic);
        iv.setFinal(isFinal);
        addInstanceVariable(iv);
    }

    /**
     * Adds an associate to this class.
     * 
     * @param gd {@inheritDoc}
     */
    @Override
    public void addAssociation(GenericDefinition gd) {
        if (inheritance == null || !gd.equals(inheritance)) { // TODO: While I don't allow multiple associations
            super.addAssociation(gd);
        }
    }

    /**
     * Marks whether this class is abstract or not.
     * 
     * @param in sets whether this class is abstract
     */
    public void setAbstract(boolean in) {
        isAbstract = in;
    }

    /**
     * Sets the inheritance relationship for this class.
     * 
     * @param ref the parent of this class
     */
    public void setInheritance(GenericDefinition ref) {
        inheritance = ref;
    }

    // Getter Methods

    // Instance Variables

    /**
     * Returns the list of instance variables in this class.
     * 
     * @return the list of instance variables
     */
    public List<InstanceVariable> getInstanceVariables() {
        return instanceVariables;
    }

    /**
     * Returns the instance variable stored at the specified index.
     * 
     * @param index an index
     * @return the instance variable stored at the specified index
     * 
     * @throws IndexOutOfBoundsException if argument is out of bounds
     */
    public InstanceVariable getInstanceVariableAt(int index) {
        return getInstanceVariables().get(index);
    }

    /**
     * Returns the number of instance variables stored in this class.
     * 
     * @return the number of instance variables
     */
    public int getNumberInstanceVariables() {
        return getInstanceVariables().size();
    }

    /**
     * Returns the type of the instance variable stored at the specified index.
     * 
     * @param index an index
     * @return the type of the instance variable stored at the specified index
     * 
     * @throws IndexOutOfBoundsException if argument is out of bounds
     */
    public String getInstanceVariableTypeAt(int index) {
        return getInstanceVariableAt(index).getType();
    }

    /**
     * Returns the visibility of the instance variable stored at the specified
     * index.
     * 
     * @param index an index
     * @return the visibility of the instance variable stored at the specified index
     * 
     * @throws IndexOutOfBoundsException if argument is out of bounds
     */
    public Visibility getInstanceVariableVisibilityAt(int index) {
        return getInstanceVariableAt(index).getVisibility();
    }

    /**
     * Returns the name of the instance variable stored at the specified
     * index.
     * 
     * @param index an index
     * @return the name of the instance variable stored at the specified index
     * 
     * @throws IndexOutOfBoundsException if argument is out of bounds
     */
    public String getInstanceVariableNameAt(int index) {
        return getInstanceVariableAt(index).getName();
    }

    /**
     * Checks whether the variable stored at the specified index is static.
     * 
     * @param index an index
     * @return whether the variable at the specified index is static
     * 
     * @throws IndexOutOfBoundsException if argument is out of bounds
     */
    public boolean getInstanceVariableStaticAt(int index) {
        return getInstanceVariableAt(index).getStatic();
    }

    /**
     * Checks whether the variable stored at the specified index is marked as final.
     * 
     * @param index an index
     * @return whether the variable at the specified index is marked as final
     * 
     * @throws IndexOutOfBoundsException if argument is out of bounds
     */
    public boolean getInstanceVariableFinalAt(int index) {
        return getInstanceVariableAt(index).getFinal();
    }

    // GenericClass

    /**
     * Returns the inheritance relationship (i.e., the "parent") that this class
     * has.
     * 
     * @return the parent
     */
    public GenericDefinition getInheritance() {
        return inheritance;
    }

    /**
     * Checks whether this class represents an abstract class.
     * 
     * @return {@code true} if this class represents an abstract class
     */
    public boolean getAbstract() {
        return isAbstract;
    }

}