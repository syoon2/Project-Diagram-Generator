/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.language.component;

import java.util.Objects;

public class Argument {

    private String name;
    private String type;

    public Argument(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        else if (obj instanceof Argument) {
            Argument arg = (Argument) obj;
            return this.name.equals(arg.name) && this.type.equals(arg.type);
        } else
            return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

}
