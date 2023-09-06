/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.language.component;

import java.util.List;

import analysis.language.Visibility;

/**
 * A representation of constructors in a class.
 * 
 * @author Ada Clevinger
 * @author Sung Ho Yoon
 * 
 * @since 1.0
 */
public class Constructor extends Function {

    /**
     * Constructs a new {@code Constructor}.
     *
     * @param vis the visibility of this constructor
     * @param name the name of this constructor
     * @param arg the list of arguments that this constructor takes
     */
    public Constructor(Visibility vis, String name, List<Argument> arg) {
        super(vis, name, arg, null);
    }

    /**
     * Constructs a new {@code Constructor}.
     *
     * @param vis the visibility of this constructor
     * @param name the name of this constructor
     * @param argName the names of the arguments that this constructor takes
     * @param argType the types of the arguments that this constructor takes
     */
    public Constructor(Visibility vis, String name, List<String> argName, List<String> argType) {
        super(vis, name, null, argName, argType);
    }

}
