/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.language.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import analysis.language.Visibility;

public class Function extends ClassComponent {

    // Instance Variables

    private List<Argument> arguments;
    private boolean isAbstract; // italics
    private boolean isStatic; // underline
    private boolean isFinal;

    // Constructors

    public Function(Visibility vis, String name, List<Argument> arg, String ret) {
        super(ret, name, vis);
        arguments = arg;
    }

    public Function(Visibility vis, String name, String ret, List<String> argName, List<String> argType) {
        super(ret, name, vis);
        arguments = new ArrayList<Argument>();
        for (int i = 0; i < argName.size(); i++) {
            arguments.add(new Argument(argName.get(i), argType.get(i)));
        }
    }

    // Setter Methods

    public void setAbstract(boolean in) {
        isAbstract = in;
    }

    public void setStatic(boolean in) {
        isStatic = in;
    }

    public void setFinal(boolean in) {
        isFinal = in;
    }

    // Getter Methods

    public List<Argument> getArguments() {
        return arguments;
    }

    public int getNumberArguments() {
        return getArguments().size();
    }

    public Argument getArgumentAt(int index) {
        return arguments.get(index);
    }

    public String getArgumentNameAt(int index) {
        return getArgumentAt(index).getName();
    }

    public String getArgumentTypeAt(int index) {
        return getArgumentAt(index).getType();
    }

    public boolean getAbstract() {
        return isAbstract;
    }

    public boolean getStatic() {
        return isStatic;
    }

    public boolean getFinal() {
        return isFinal;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        else if (super.equals(obj) && obj instanceof Function) {
            Function func = (Function) obj;
            return this.isAbstract == func.isAbstract && this.isStatic == func.isStatic && this.isFinal == func.isFinal
                    && this.arguments.equals(func.arguments);
        } else
            return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getName(), super.getType(), super.getVisibility(), arguments, isAbstract, isStatic,
                isFinal);
    }

}
