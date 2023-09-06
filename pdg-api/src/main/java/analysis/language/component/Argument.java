/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.language.component;

import java.util.Objects;

/**
 * A representation of an argument.
 * 
 * @author Ada Clevinger
 * @author Sung Ho Yoon
 * 
 * @since 1.0
 */
public class Argument {

    /** The name of this argument */
    private String name;
    /** The type of this argument */
    private String type;

    /**
     * Constructs a new {@code Argument}.
     * 
     * @param name the name of this argument
     * @param type the type of this argument
     */
    public Argument(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Returns the name of this argument.
     * 
     * @return the name of this argument
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the type of this argument.
     * 
     * @return the type of this argument
     */
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
