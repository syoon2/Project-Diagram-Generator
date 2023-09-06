/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.language.actor;

/**
 * A representation of interfaces.
 * 
 * @author Ada Clevinger
 * @author Sung Ho Yoon
 * 
 * @since 1.0
 */
public class GenericInterface extends GenericDefinition {

    /**
     * Constructs a new {@code GenericInterface}.
     * 
     * @param name    the name of this interface
     * @param context the context that this interface is in
     */
    public GenericInterface(String name, String context) {
        super(name, context);
    }

}
