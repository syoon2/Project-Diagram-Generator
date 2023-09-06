/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.language.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import analysis.language.Visibility;

/**
 * A representation of functions in a class.
 * 
 * @author Ada Clevinger
 * @author Sung Ho Yoon
 * 
 * @since 1.0
 */
public class Function extends ClassComponent {

    // Instance Variables

    /** Arguments that this function takes. */
    private List<Argument> arguments;
    /** Whether this function is abstract. */
    private boolean isAbstract; // italics
    /** Whether this function is static. */
    private boolean isStatic; // underline
    /** Whether this function is marked as final. */
    private boolean isFinal;

    // Constructors

    /**
     * Constructs a new {@code Function}.
     *
     * @param vis  the visibility of this function
     * @param name the name of this function
     * @param arg  the list of arguments that this function takes
     * @param ret  the return type of this function
     */
    public Function(Visibility vis, String name, List<Argument> arg, String ret) {
        super(ret, name, vis);
        arguments = arg;
    }

    /**
     * Constructs a new {@code Function}.
     *
     * @param vis     the visibility of this function
     * @param name    the name of this function
     * @param ret     the return type of this function
     * @param argName the names of the arguments that this function takes
     * @param argType the types of the arguments that this function takes
     */
    public Function(Visibility vis, String name, String ret, List<String> argName, List<String> argType) {
        super(ret, name, vis);
        arguments = new ArrayList<Argument>();
        for (int i = 0; i < argName.size(); i++) {
            arguments.add(new Argument(argName.get(i), argType.get(i)));
        }
    }

    // Setter Methods

    /**
     * Marks whether this function is abstract or not.
     * 
     * @param in whether this function is abstract
     */
    public void setAbstract(boolean in) {
        isAbstract = in;
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
     * Marks whether this function is marked as final or not.
     * 
     * @param in whether this function is marked as final
     */
    public void setFinal(boolean in) {
        isFinal = in;
    }

    // Getter Methods

    /**
     * Returns the list of arguments that this function takes.
     * 
     * @return the list of arguments
     */
    public List<Argument> getArguments() {
        return arguments;
    }

    /**
     * Returns the number of arguments that this function takes.
     * 
     * @return the number of arguments
     */
    public int getNumberArguments() {
        return getArguments().size();
    }

    /**
     * Returns the argument at the specified index.
     * 
     * @param index an index
     * @return an argument
     * 
     * @throws IndexOutOfBoundsException if argument is out of bounds
     */
    public Argument getArgumentAt(int index) {
        return arguments.get(index);
    }

    /**
     * Returns the name of the argument at the specified index.
     * 
     * @param index an index
     * @return the name of the argument
     * 
     * @throws IndexOutOfBoundsException if argument is out of bounds
     */
    public String getArgumentNameAt(int index) {
        return getArgumentAt(index).getName();
    }

    /**
     * Returns the type of the argument at the specified index
     * 
     * @param index an index
     * @return the type of the argument
     * 
     * @throws IndexOutOfBoundsException if argument is out of bounds
     */
    public String getArgumentTypeAt(int index) {
        return getArgumentAt(index).getType();
    }

    /**
     * Checks whether this function is abstract.
     * 
     * @return {@code true} if this function is abstract
     */
    public boolean getAbstract() {
        return isAbstract;
    }

    /**
     * Checks whether this function is static.
     * 
     * @return {@code true} if this function is static
     */
    public boolean getStatic() {
        return isStatic;
    }

    /**
     * Checks whether this function is marked as final.
     * 
     * @return {@code true} if this function is marked as final
     */
    public boolean getFinal() {
        return isFinal;
    }

    /**
     * Returns the return type of this function.
     * 
     * @return the return type of this function
     */
    @Override
    public String getType() {
        return super.getType();
    }

    /**
     * Checks whether an object is "equal to" this function.
     * 
     * @param obj an object to compare with
     * @return {@code true} if argument is "equal to" this function
     * 
     * @since 2.0
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        else if (super.equals(obj) && obj instanceof Function) {
            Function func = (Function) obj;
            return this.isAbstract == func.isAbstract && this.isStatic == func.isStatic && this.isFinal == func.isFinal
                    && this.arguments.equals(func.arguments);
        } else
            return false;
    }

    /**
     * Returns a hash code value for this function.
     * 
     * @return a hash code value
     * 
     * @since 2.0
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.getName(), super.getType(), super.getVisibility(), arguments, isAbstract, isStatic,
                isFinal);
    }

}
