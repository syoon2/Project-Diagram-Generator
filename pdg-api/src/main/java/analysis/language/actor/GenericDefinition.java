/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.language.actor;

import java.util.ArrayList;
import java.util.List;

import analysis.language.Visibility;
import analysis.language.component.Constructor;
import analysis.language.component.Function;

/**
 * A generic definition of a class / interface.
 * 
 * @author Ada Clevinger
 * @author Sung Ho Yoon
 * 
 * @since 1.0
 */
public abstract class GenericDefinition implements Comparable<GenericDefinition> {

    // Instance Variables

    /** The name of this entity. */
    private String name;
    /** The context that this entity is in */
    private String context;
    /** The associates of this entity. */
    private List<GenericDefinition> associates;
    /** The realization relationships that this entity has. */
    private List<GenericDefinition> realizations; // dotted line, empty arrowhead
    /** Functions defined in this entity. */
    private List<Function> functions;

    // Constructors

    /**
     * Constructs a new {@code GenericDefinition}.
     * 
     * @param name    the name of this entity
     * @param context the context that this entity is in
     */
    public GenericDefinition(String name, String context) {
        this.name = name;
        this.context = context;
        associates = new ArrayList<GenericDefinition>();
        functions = new ArrayList<Function>();
        realizations = new ArrayList<GenericDefinition>();
    }

    // Operations

    /**
     * Adds an associate to this {@code GenericDefinition}.
     * 
     * @param ref an associate
     */
    public void addAssociation(GenericDefinition ref) {
        if (ref == null) {
            return;
        }
        associates.add(ref);
    }

    /**
     * Adds a function to this {@code GenericDefinition}.
     * 
     * @param in a function
     */
    public void addFunction(Function in) {
        functions.add(in);
    }

    /**
     * Adds a function to this {@code GenericDefinition}.
     * 
     * @param vis          the visibility of the function
     * @param funcName     the name of the function
     * @param ret          the return type of the function
     * @param argName      the names of the arguments
     * @param argType      the types of the arguments
     * @param statStatic   whether the function is static
     * @param statAbstract whether the function is abstract
     * @param isFinal      whether the function is declared as final
     */
    public void addFunction(Visibility vis, String funcName, String ret, List<String> argName, List<String> argType,
            boolean statStatic, boolean statAbstract, boolean isFinal) {
        Function in = new Function(vis, funcName, ret, argName, argType);
        in.setAbstract(statAbstract);
        in.setStatic(statStatic);
        in.setFinal(isFinal);
        addFunction(in);
    }

    /**
     * Adds a realization relationship to this {@code GenericDefinition}.
     * 
     * @param in a realization relationship
     */
    public void addRealization(GenericDefinition in) {
        realizations.add(in);
    }

    /**
     * Adds a constructor to this {@code GenericDefinition}.
     * 
     * @param vis     the visibility of the constructor
     * @param name    the name of the constructor
     * @param argName the names of the arguments
     * @param argType the types of the arguments
     */
    public void addConstructor(Visibility vis, String name, List<String> argName, List<String> argType) {
        Constructor in = new Constructor(vis, name, argName, argType);
        addFunction(in);
    }

    // Getter Methods

    // Functions

    /**
     * Returns the list of functions stored in this {@code GenericDefinition}.
     * 
     * @return the list of functions
     */
    public List<Function> getFunctions() {
        return functions;
    }

    /**
     * Returns the function stored in this {@code GenericDefinition} at the
     * specified index.
     * 
     * @param index an index
     * @return the function stored at the specified index
     * @throws IndexOutOfBoundsException if argument is out of bounds
     */
    public Function getFunctionAt(int index) {
        return getFunctions().get(index);
    }

    /**
     * Returns the number of functions stored in this {@code GenericDefinition}.
     * 
     * @return the number of functions
     */
    public int getNumberFunctions() {
        return getFunctions().size();
    }

    /**
     * Returns the names of the arguments that the function at the specified index
     * takes.
     * 
     * @param index an index
     * @return an array of argument names
     * 
     * @throws IndexOutOfBoundsException if argument is out of bounds
     */
    public String[] getFunctionArgumentNamesAt(int index) {
        Function func = getFunctionAt(index);
        String[] argNom = new String[func.getNumberArguments()];
        for (int i = 0; i < func.getNumberArguments(); i++) {
            argNom[i] = func.getArgumentNameAt(i);
        }
        return argNom;
    }

    /**
     * Returns the types of the arguments that the function at the specified index
     * takes.
     * 
     * @param index an index
     * @return an array of argument types
     * 
     * @throws IndexOutOfBoundsException if argument is out of bounds
     */
    public String[] getFunctionArgumentTypesAt(int index) {
        Function func = getFunctionAt(index);
        String[] argNom = new String[func.getNumberArguments()];
        for (int i = 0; i < func.getNumberArguments(); i++) {
            argNom[i] = func.getArgumentTypeAt(i);
        }
        return argNom;
    }

    /**
     * Returns the return type of the function stored at the specified index.
     * 
     * @param index an index
     * @return the return type
     * 
     * @throws IndexOutOfBoundsException if argument is out of bounds
     */
    public String getFunctionTypeAt(int index) {
        return getFunctionAt(index).getType();
    }

    /**
     * Returns the visibility of the function stored at the specified index.
     * 
     * @param index an index
     * @return the visibility
     * 
     * @throws IndexOutOfBoundsException if argument is out of bounds
     */
    public Visibility getFunctionVisibilityAt(int index) {
        return getFunctionAt(index).getVisibility();
    }

    /**
     * Returns the name of the function stored at the specified index.
     * 
     * @param index an index
     * @return the name
     * 
     * @throws IndexOutOfBoundsException if argument is out of bounds
     */
    public String getFunctionNameAt(int index) {
        return getFunctionAt(index).getName();
    }

    /**
     * Checks whether the function stored at the specified index is static.
     * 
     * @param index an index
     * @return whether the function at the specified index is static
     * 
     * @throws IndexOutOfBoundsException if argument is out of bounds
     */
    public boolean getFunctionStaticAt(int index) {
        return getFunctionAt(index).getStatic();
    }

    /**
     * Checks whether the function stored at the specified index is abstract.
     * 
     * @param index an index
     * @return whether the function at the specified index is abstract
     * 
     * @throws IndexOutOfBoundsException if argument is out of bounds
     */
    public boolean getFunctionAbstractAt(int index) {
        return getFunctionAt(index).getAbstract();
    }

    /**
     * Checks whether the function stored at the specified index is declared as
     * final.
     * 
     * @param index an index
     * @return whether the function at the specified index is declared as final
     * 
     * @throws IndexOutOfBoundsException if argument is out of bounds
     */
    public boolean getFunctionFinalAt(int index) {
        return getFunctionAt(index).getFinal();
    }

    // GenericDefinition

    /**
     * Returns the name of this {@code GenericDefinition}
     * 
     * @return the name of this {@code GenericDefinition}
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the associates of this {@code GenericDefinition}.
     * 
     * @return the associates of this {@code GenericDefinition}
     */
    public List<GenericDefinition> getClassAssociates() {
        return associates;
    }

    /**
     * Returns the realization relationships of this {@code GenericDefinition}.
     * 
     * @return the realization relationships of this {@code GenericDefinition}
     */
    public List<GenericDefinition> getRealizations() {
        return realizations;
    }

    /**
     * Returns the context that this {@code GenericDefinition} is in.
     * 
     * @return the context
     */
    public String getContext() {
        return context;
    }

    /**
     * Returns the context hierarchy that this {@code GenericDefinition} is in.
     * 
     * @return the context hierarchy as an array
     */
    public String[] getContextArray() {
        return context.split("\\.");
    }

    /**
     * Returns the full name of this {@code GenericDefinition}.
     * 
     * @return the full name of this {@code GenericDefinition}
     */
    public String getFullName() {
        return getContext() + "/" + getName();
    }

    /**
     * Checks whether this {@code GenericDefinition} has an association
     * relationship with another {@code GenericDefinition}.
     * 
     * @param gc a {@code GenericDefinition}
     * @return {@code true} if this {@code GenericDefinition} has an association
     *         relationship with the specified {@code GenericDefinition}
     */
    public boolean hasAssociate(GenericDefinition gc) {
        for (GenericDefinition c : associates) {
            if (c.compareTo(gc) == 0) {
                return true;
            }
        }
        return false;
    }

    // Mechanics

    /**
     * Compares this {@code GenericDefinition} with another
     * {@code GenericDefinition}.
     * 
     * @param o another {@code GenericDefinition}
     * @return a positive integer if this {@code GenericDefinition} is "greater
     *         than" the argument,
     *         zero if this {@code GenericDefinition} is "equal to" the argument, or
     *         a negative integer if
     *         this {@code GenericDefinition} is "less than" the argument
     * 
     * @throws NullPointerException if argument is {@code null}
     */
    @Override
    public int compareTo(GenericDefinition o) {
        return getFullName().compareTo(o.getFullName());
    }

    /**
     * Checks whether an object is "equal to" this {@code GenericDefinition}.
     * 
     * @param obj an object to compare with
     * @return {@code true} if argument is "equal to" this {@code GenericDefinition}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        else if (obj instanceof GenericDefinition) {
            return getFullName().equals(((GenericDefinition) obj).getFullName());
        } else
            return false;
    }

    /**
     * Returns a hash code value for this {@code GenericDefinition}.
     * 
     * @return a hash code value
     * 
     * @since 2.0
     */
    @Override
    public int hashCode() {
        return getFullName().hashCode();
    }

}
