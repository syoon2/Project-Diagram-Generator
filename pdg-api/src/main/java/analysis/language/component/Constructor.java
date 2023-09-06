/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.language.component;

import java.util.List;

import analysis.language.Visibility;

public class Constructor extends Function{

    public Constructor(Visibility vis, String name, List<Argument> arg) {
        super(vis, name, arg, null);
    }

    public Constructor(Visibility vis, String name, List<String> argName, List<String> argType) {
        super(vis, name, null, argName, argType);
    }

}
