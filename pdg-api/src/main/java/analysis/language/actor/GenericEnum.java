/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.language.actor;

/**
 * A representation of enumerations.
 * 
 * @see java.lang.Enum
 * 
 * @author Ada Clevinger
 * @author Sung Ho Yoon
 * 
 * @since 1.0
 */
public class GenericEnum extends GenericDefinition {

    /**
     * Constructs a new {@code GenericEnum}.
     * 
     * @param name    the name of this enum
     * @param context the context that this enum is in
     */
    public GenericEnum(String name, String context) {
        super(name, context);
    }

}
