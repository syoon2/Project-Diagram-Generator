/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package image;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import analysis.language.actor.GenericClass;
import analysis.language.actor.GenericDefinition;
import analysis.process.Cluster;
import analysis.process.Explore;

public class DotProcess {

    // Instance Variables

    private static Explore exp;

    // Static Assignment

    public static void setProject(Explore in) {
        exp = in;
    }

    // Operations

    public static String generateDot() {
        return new DotProcessor(exp).generateDot();
    }

    private static class DotProcessor {

        Explore explore;
        Map<String, Integer> reference;
        int count;

        DotProcessor(Explore explore) {
            this.explore = explore;
        }

        String generateDot() {
            reference = new HashMap<String, Integer>();

            count = 0;
            StringBuilder out = new StringBuilder(processInitiation());

            out.append(processClasses());

            out.append(processInterfaces());

            out.append(processEnums());

            out = processClusters(out, explore.getClusterRoot(), 1, 30, 1);

            out.append(processAssociations());

            out.append("\n}");

            return out.toString();
        }

        private String processInitiation() { // Can manipulate here for adjusting draw settings
            String out = "digraph G {\n";
            out += "\tnode[shape=record,style=filled,fillcolor=gray95];\r\n" +
                    "\tedge[concentrate=true];\n" +
                    "\tgraph[splines = ortho, ranksep = 1, ratio = fill, color=blue];\n" +
                    "\trankdir = TB;\n"; // splines = ortho, nodesep = 1 for straight lines, looks rough, let user
                                         // change how lines are displayed
            out += StringUtils.LF;
            return out;
        }

        private String processClasses() {
            StringBuilder out = new StringBuilder();
            for (GenericDefinition gc : explore.getClasses()) {
                reference.put(gc.getFullName(), count++);
                out.append(generateClassDot((GenericClass) gc, reference.get(gc.getFullName())));
            }
            return out.toString();
        }

        private String processInterfaces() {
            StringBuilder out = new StringBuilder();
            for (GenericDefinition gc : explore.getInterfaces()) {
                reference.put(gc.getFullName(), count++);
                out.append(generateInterfaceDot(gc, reference.get(gc.getFullName())));
            }
            return out.toString();
        }

        private String processEnums() {
            StringBuilder out = new StringBuilder();
            for (GenericDefinition ge : explore.getEnums()) {
                reference.put(ge.getFullName(), count++);
                out.append(generateEnumDot(ge, reference.get(ge.getFullName())));
            }
            return out.toString();
        }

        private String processAssociations() {
            StringBuilder out = new StringBuilder();
            for (GenericDefinition c : explore.getClasses()) {
                out.append(generateDotClassAssociations((GenericClass) c));
            }
            for (GenericDefinition c : explore.getInterfaces()) {
                out.append(generateDotInterfaceAssociations(c));
            }
            for (GenericDefinition c : explore.getEnums()) {
                out.append(generateDotEnumAssociations(c));
            }
            return out.toString();
        }

        // -- GenericClass ----------------------------------------

        public String generateClassDot(GenericClass gc, int val) {
            String pref = "\tn" + val + " [label = <{";
            StringBuilder out = new StringBuilder(formDotName(gc));
            out.append('|');
            for (int i = 0; i < gc.getNumberInstanceVariables(); i++) {
                out.append(DotComponent.dotInstanceVariable(gc.getInstanceVariableAt(i)));
                if (i + 1 < gc.getNumberInstanceVariables())
                    out.append("<BR/>");
            }
            out.append('|');
            out.append(getFunctionDot(gc));
            String post = "}>];\n";
            return out.insert(0, pref).append(post).toString();
        };

        public String generateDotClassAssociations(GenericClass gc) {
            int val = reference.get(gc.getFullName());
            StringBuilder out = new StringBuilder();
            if (gc.getInheritance() != null) {
                out.append("\tn" + val + " -> n" + reference.get(gc.getInheritance().getFullName()));
                out.append("[arrowhead=onormal];\n");
            }
            out.append(generateDotAssociations(gc));
            for (GenericDefinition i : gc.getRealizations()) {
                out.append("\tn" + val + " -> n" + reference.get(i.getFullName()));
                out.append("[arrowhead=onormal, style=dashed];\n");
            }
            return out.toString();
        }

        private static String formDotName(GenericClass gc) {
            String out = gc.getName();
            if (gc.getAbstract()) {
                out = "<i>" + out + "</i>";
            }
            return out;
        }

        // -- GenericInterface ------------------------------------

        public String generateInterfaceDot(GenericDefinition gi, int val) {
            String pref = "\tn" + val + " [label = <{";
            String out = formInterfaceName() + "<BR/>" + gi.getName() + "|";
            out += "|";
            out += getFunctionDot(gi);
            String post = "}>];\n";
            return pref + out + post;
        }

        private String formInterfaceName() {
            return StringEscapeUtils.escapeHtml4("<<interface>>");
        }

        public String generateDotInterfaceAssociations(GenericDefinition gi) {
            int val = reference.get(gi.getFullName());
            StringBuilder out = new StringBuilder();
            for (GenericDefinition i : gi.getRealizations()) {
                out.append("\tn" + val + " -> n" + reference.get(i.getFullName()));
                out.append("[arrowhead=onormal, style=solid];\n");
            }
            return out.append(generateDotAssociations(gi)).toString();
        }

        // -- Generic Enum ----------------------------------------

        public String generateEnumDot(GenericDefinition gi, int val) {
            String pref = "\tn" + val + " [label = <{";
            String out = formEnumName() + "<BR/>" + gi.getName() + "|";
            out += "|";
            out += getFunctionDot(gi);
            String post = "}>];\n";
            return pref + out + post;
        }

        private String formEnumName() {
            return StringEscapeUtils.escapeHtml4("<<enumeration>>");
        }

        public String generateDotEnumAssociations(GenericDefinition gi) {
            int val = reference.get(gi.getFullName());
            StringBuilder out = new StringBuilder();
            for (GenericDefinition i : gi.getRealizations()) {
                out.append("\tn" + val + " -> n" + reference.get(i.getFullName()));
                out.append("[arrowhead=onormal, style=dotted];\n");
            }
            return out.append(generateDotAssociations(gi)).toString();
        }

        // -- GenericDefinition -----------------------------------

        protected String generateDotAssociations(GenericDefinition gd) {
            StringBuilder out = new StringBuilder();
            for (GenericDefinition c : gd.getClassAssociates()) {
                int mV = reference.get(gd.getFullName());
                int yV = reference.get(c.getFullName());
                if (!c.hasAssociate(gd) || mV <= yV) { // Processes numerically, so if mutual, only draw if first time
                                                       // seeing
                    out.append("\tn" + mV + " -> n" + yV);
                    if (c.hasAssociate(gd)) {
                        out.append("[arrowhead=none]");
                    } else {
                        out.append("[arrowhead=normal]");
                    }
                    out.append(";\n");
                }
            }
            return out.toString();
        }

        // -- Helper ----------------------------------------------

        private String getFunctionDot(GenericDefinition gd) {
            StringBuilder out = new StringBuilder();
            for (int i = 0; i < gd.getNumberFunctions(); i++) {
                out.append(DotComponent.dotFunction(gd.getFunctionAt(i)));
                if (i + 1 < gd.getNumberFunctions())
                    out.append("<BR/>");
            }
            return out.toString();
        }

        // -- Clusters --------------------------------------------

        private StringBuilder processClusters(StringBuilder out, Cluster next, int depth, int fontSize, int penWidth) {
            if (next == null)
                return out;
            String address = next.getAddress();
            out.append(tabBuffer(depth) + "subgraph cluster_" + address.replaceAll("\\.", "_") + "{\n");
            out.append(tabBuffer(depth + 1) + "label = \"" + address + "\";\n");
            out.append(tabBuffer(depth + 1) + "fontsize = " + fontSize + ";\n");
            out.append(tabBuffer(depth + 1) + "penwidth = " + penWidth + ";\n");
            for (String gd : next.getComponents()) {
                out.append(tabBuffer(depth + 1) + "n" + reference.get(gd) + ";\n");
            }
            for (Cluster c : next.getChildren()) {
                out = processClusters(out, c, depth + 1, fontSize - 4, penWidth + 1);
            }
            out.append(tabBuffer(depth) + "}\n");
            return out;
        }

        private String tabBuffer(int in) {
            return "\t".repeat(in);
        }

    }
}
