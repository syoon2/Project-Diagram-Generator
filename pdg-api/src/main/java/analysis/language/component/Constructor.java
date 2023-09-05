/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.language.component;

import java.util.List;

import analysis.language.Visibility;

public class Constructor extends Function{

    public Constructor(Visibility vis, String nom, List<Argument> arg) {
        super(vis, nom, arg, null);
    }

    public Constructor(Visibility vis, String nom, List<String> argNom, List<String> argTyp) {
        super(vis, nom, null, argNom, argTyp);
    }

    public String getReturnType() {
        return "";
    }

}
