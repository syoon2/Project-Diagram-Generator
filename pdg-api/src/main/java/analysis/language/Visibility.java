/*
 * Copyright (c) Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.language;

/**
 * Enumerates standard visibility types in Java.
 * 
 * @author Sung Ho Yoon
 * @since 2.0
 */
public enum Visibility {
    PUBLIC(0, "public", "+"),
    PRIVATE(1, "private", "-"),
    PROTECTED(2, "protected", "#"),
    PACKAGE(3, "", "?");

    private final int val;
    private final String repr;
    private final String dotRepr;

    private Visibility(int val, String repr, String dotRepr) {
        this.val = val;
        this.repr = repr;
        this.dotRepr = dotRepr;
    }

    public int getIntValue() {
        return this.val;
    }

    public String getDotRepr() {
        return this.dotRepr;
    }

    public String toString() {
        return this.repr;
    }

    public static Visibility valueOf(int val) {
        for (Visibility vis : values()) {
            if (vis.val == val) {
                return vis;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + val);
    }
}
