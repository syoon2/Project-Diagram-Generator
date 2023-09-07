/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package image;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

public class DotComponent {


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

    public static String dotArgument(String nom, String typ) {
        return nom + " : " + typ;
    }

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

}
