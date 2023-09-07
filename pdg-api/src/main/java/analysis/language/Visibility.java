/*
 * Copyright (c) Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.language;

import org.apache.commons.lang3.StringUtils;

/**
 * Enumerates standard visibility types in Java.
 * 
 * @author Sung Ho Yoon
 * @since 2.0
 */
public enum Visibility {
    /** 
     * Visibility that corresponds to the {@code public} access modifier.
     */
    PUBLIC(0, "public", "+"),
    /** 
     * Visibility that corresponds to the {@code private} access modifier.
     */
    PRIVATE(1, "private", "-"),
    /**
     * Visibility that corresponds to the {@code protected} access modifier.
     */
    PROTECTED(2, "protected", "#"),
    /**
     * Visibility that corresponds to the default (package-private) access modifier.
     */
    PACKAGE(3, StringUtils.EMPTY, "?");

    /** Integer representation of this visibility */
    private final int val;
    /** Java keyword that corresponds to this visibility */
    private final String repr;
    /** Representation used in the diagram */
    private final String dotRepr;

    /**
     * Constructs a new {@code Visibility}.
     * 
     * @param val     integer representation
     * @param repr    string representation
     * @param dotRepr representation used in diagram
     */
    private Visibility(int val, String repr, String dotRepr) {
        this.val = val;
        this.repr = repr;
        this.dotRepr = dotRepr;
    }

    /**
     * Returns the integer value that represents this visibility.
     * 
     * @return an integer representation of this visibility
     */
    public int getIntValue() {
        return this.val;
    }

    /**
     * Returns the notation used for this visibility in the diagram.
     * 
     * @return the symbol used in the generated diagram
     */
    public String getDotRepr() {
        return this.dotRepr;
    }

    /**
     * Returns the string representation of this visibility.
     * 
     * @return the string representation of this visibility
     */
    @Override
    public String toString() {
        return this.repr;
    }

    /**
     * Returns the visibility represented by the specified int value.
     * 
     * @param val an integer
     * @return the {@code Visibility} constant represented by the argument
     * 
     * @throws IllegalArgumentException if there is no constant represented by the
     *                                  argument
     */
    public static Visibility valueOf(int val) {
        for (Visibility vis : values()) {
            if (vis.val == val) {
                return vis;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + val);
    }
}
