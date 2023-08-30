/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.language.component;

public class Argument {

    private String name;
    private String type;

    public Argument(String nom, String typ) {
        name = nom;
        type = typ;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

}
