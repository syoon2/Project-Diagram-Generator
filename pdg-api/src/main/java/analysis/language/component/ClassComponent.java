/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.language.component;

import analysis.language.Visibility;

public abstract class ClassComponent {

    private String name;
    private String type;
    private Visibility visibility;

    public ClassComponent(String typ, String nom, Visibility vis) {
        type = typ;
        name = nom;
        visibility = vis;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
