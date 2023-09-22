/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.language.actor;

import java.util.List;

import analysis.language.Visibility;

/**
 * A representation of enumerations.
 * 
 * @see java.lang.Enum
 * 
 * @author Ada Clevinger
 * @author Sung Ho Yoon
 * 
 * @since 1.0
 */
public class GenericEnum extends GenericClass {

    /**
     * Constructs a new {@code GenericEnum}.
     * 
     * @param name    the name of this enum
     * @param context the context that this enum is in
     */
    public GenericEnum(String name, String context) {
        super(name, context);
    }

    /**
     * Adds a constructor to this {@code GenericEnum}.
     * 
     * @param vis     {@inheritDoc}
     * @param name    {@inheritDoc}
     * @param argName {@inheritDoc}
     * @param argType {@inheritDoc}
     * 
     * @throws IllegalArgumentException if {@code vis} is {@link Visibility#PUBLIC
     *                                  public} or {@link Visibility#PROTECTED
     *                                  protected}
     * 
     * @since 2.0
     */
    @Override
    public void addConstructor(Visibility vis, String name, List<String> argName, List<String> argType) {
        if (vis == Visibility.PUBLIC || vis == Visibility.PROTECTED)
            /* 
             * An enum constructor is always private.
             * It is a compile-time error if an enum constructor is
             * public or protected.
             */ 
            throw new IllegalArgumentException("Invalid visibility: " + vis);
        super.addConstructor(Visibility.PRIVATE, name, argName, argType);
    }

    /**
     * Throws {@link UnsupportedOperationException} as enums cannot be
     * abstract.
     * 
     * @throws UnsupportedOperationException always
     * 
     * @since 2.0
     */
    @Override
    public void setAbstract(boolean in) {
        throw new UnsupportedOperationException("An enum cannot be abstract.");
    }

}
