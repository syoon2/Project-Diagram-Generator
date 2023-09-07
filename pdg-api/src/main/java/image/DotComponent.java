/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package image;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import analysis.language.component.Argument;
import analysis.language.component.Function;
import analysis.language.component.InstanceVariable;

public class DotComponent {

    /**
     * Private constructor.
     * 
     * @since 2.0
     */
    private DotComponent() {

    }

    /**
     * Converts a function to its dot representation.
     * 
     * @param vis        the visibility of the function
     * @param name       the name of the function
     * @param type       the (return) type of the function
     * @param argName    the names of the arguments that the function takes
     * @param argType    the types of the arguments that the function takes
     * @param isAbstract whether the function is abstract
     * @param isStatic   whether the function is static
     * @param isFinal    whether the function is declared as final
     * @return the dot representation of the function
     * 
     * @deprecated Use {@link #dotFunction(Function)} instead.
     */
    @Deprecated(since = "2.0", forRemoval = true)
    public static String dotFunction(String vis, String name, String type, String[] argName, String[] argType,
            boolean isAbstract, boolean isStatic, boolean isFinal) {
        String out = vis + name + "(";
        for (int i = 0; i < argName.length; i++) {
            out += dotArgument(argName[i], argType[i]) + (i + 1 < argName.length ? ", " : StringUtils.EMPTY);
        }
        out += ")";
        String ret = type;

        if (ret != null) {
            out += " : " + ret;
        }

        out = StringEscapeUtils.escapeHtml4(out);

        if (isAbstract) {
            out = "<u>" + out + "</u>";
        }
        if (isStatic) {
            out = "<i>" + out + "</i>";
        }
        if (isFinal) {
            out = "<b>" + out + "</b>";
        }
        return out;
    }

    /**
     * Converts a function to its dot representation.
     * 
     * @param func a function
     * @return the dot representation of the supplied function
     * 
     * @throws NullPointerException if argument is {@code null}
     * 
     * @since 2.0
     */
    public static String dotFunction(Function func) {
        Objects.requireNonNull(func);
        String out = func.getVisibility().getDotRepr() + func.getName() + "(";
        for (int i = 0; i < func.getNumberArguments(); i++) {
            out += dotArgument(func.getArgumentAt(i)) + (i + 1 < func.getNumberArguments() ? ", " : StringUtils.EMPTY);
        }
        out += ")";
        String ret = func.getType();

        if (ret != null) {
            out += " : " + ret;
        }

        out = StringEscapeUtils.escapeHtml4(out);

        if (func.getAbstract()) {
            out = "<u>" + out + "</u>";
        }
        if (func.getStatic()) {
            out = "<i>" + out + "</i>";
        }
        if (func.getFinal()) {
            out = "<b>" + out + "</b>";
        }
        return out;
    }

    /**
     * Converts an argument to its dot representation.
     * 
     * @param nom the name of the argument
     * @param typ the type of the argument
     * @return the dot representation of the argument
     * 
     * @deprecated Use {@link #dotArgument(Argument)} instead.
     */
    @Deprecated(since = "2.0", forRemoval = true)
    public static String dotArgument(String nom, String typ) {
        return nom + " : " + typ;
    }

    /**
     * Converts an argument to its dot representation.
     * 
     * @param arg an argument
     * @return the dot representation of {@code arg}
     * 
     * @throws NullPointerException if {@code arg == null}
     * 
     * @since 2.0
     */
    public static String dotArgument(Argument arg) {
        Objects.requireNonNull(arg);
        return arg.getName() + " : " + arg.getType();
    }

    /**
     * Converts an instance variable to its dot representation.
     * 
     * @param vis      the visibility of the instance variable
     * @param name     the name of the instance variable
     * @param type     the type of the instance variable
     * @param isStatic whether the instance variable is static
     * @param isFinal  whether the instance variable is declared as final
     * @return the dot representation of the instance variable
     * 
     * @deprecated Use {@link #dotInstanceVariable(InstanceVariable)} instead.
     */
    @Deprecated(since = "2.0", forRemoval = true)
    public static String dotInstanceVariable(String vis, String name, String type, boolean isStatic, boolean isFinal) {
        String out = vis + name + " : " + type;
        out = StringEscapeUtils.escapeHtml4(out);
        if (isStatic) {
            out = "<i>" + out + "</i>";
        }
        if (isFinal) {
            out = "<b>" + out + "</b>";
        }
        return out;
    }

    /**
     * Converts an instance variable to its dot representation.
     * 
     * @param instVar an instance variable
     * @return the dot representation of the supplied instance variable
     * 
     * @throws NullPointerException if argument is {@code null}
     * 
     * @since 2.0
     */
    public static String dotInstanceVariable(InstanceVariable instVar) {
        Objects.requireNonNull(instVar);
        String out = instVar.getVisibility().getDotRepr() + instVar.getName() + " : " + instVar.getType();
        out = StringEscapeUtils.escapeHtml4(out);
        if (instVar.getStatic()) {
            out = "<i>" + out + "</i>";
        }
        if (instVar.getFinal()) {
            out = "<b>" + out + "</b>";
        }
        return out;
    }

}
