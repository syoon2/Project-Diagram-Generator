/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.language.component;

import java.util.Objects;

import analysis.language.Visibility;

public abstract class ClassComponent {

    private String name;
    private String type;
    private Visibility visibility;

    public ClassComponent(String type, String name, Visibility visibility) {
        this.type = type;
        this.name = name;
        this.visibility = visibility;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        else if (obj instanceof ClassComponent) {
            ClassComponent clsComp = (ClassComponent) obj;
            return this.name.equals(clsComp.name) && this.type.equals(clsComp.type)
                    && this.visibility.equals(clsComp.visibility);
        } else
            return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, visibility);
    }
}
