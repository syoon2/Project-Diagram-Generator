/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package image;

import java.util.HashMap;
import java.util.Map;

import analysis.language.actor.GenericClass;
import analysis.language.actor.GenericDefinition;
import analysis.process.Cluster;
import analysis.process.Explore;

public class DotProcess {

    private static final String HTML_LT = "&lt;";
    private static final String HTML_GT = "&gt;";

//---  Instance Variables   -------------------------------------------------------------------

    private static Explore exp;

//---  Static Assignment   --------------------------------------------------------------------

    public static void setProject(Explore in) {
        exp = in;
    }

//---  Operations   ---------------------------------------------------------------------------

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
            String out = processInitiation();

            out += processClasses();

            out += processInterfaces();

            out += processEnums();

            out = processClusters(out, explore.getClusterRoot(), 1, 30, 1);

            out += processAssociations();

            out += "\n}";

            return out;
        }

        private String processInitiation() {	//Can manipulate here for adjusting draw settings
            String out = "digraph G {\n";
            out += 	"\tnode[shape=record,style=filled,fillcolor=gray95];\r\n" +
                    "\tedge[concentrate=true];\n" +
                    "\tgraph[splines = ortho, ranksep = 1, ratio = fill, color=blue];\n" +
                    "\trankdir = TB;\n";	//splines = ortho, nodesep = 1 for straight lines, looks rough, let user change how lines are displayed
            out += "\n";
            return out;
        }

        private String processClasses() {
            String out = "";
            for(GenericDefinition gc : explore.getClasses()) {
                reference.put(gc.getFullName(), count++);
                out += generateClassDot((GenericClass)gc, reference.get(gc.getFullName()));
            }
            return out;
        }

        private String processInterfaces() {
            String out = "";
            for(GenericDefinition gc : explore.getInterfaces()) {
                reference.put(gc.getFullName(), count++);
                out += generateInterfaceDot(gc, reference.get(gc.getFullName()));
            }
            return out;
        }

        private String processEnums() {
            String out = "";
            for(GenericDefinition ge : explore.getEnums()) {
                reference.put(ge.getFullName(), count++);
                out += generateEnumDot(ge, reference.get(ge.getFullName()));
            }
            return out;
        }

        private String processAssociations() {
            String out = "";
            for(GenericDefinition c : explore.getClasses()) {
                out += generateDotClassAssociations((GenericClass)c);
            }
            for(GenericDefinition c : explore.getInterfaces()) {
                out += generateDotInterfaceAssociations(c);
            }
            for(GenericDefinition c : explore.getEnums()) {
                out += generateDotEnumAssociations(c);
            }
            return out;
        }

        //-- GenericClass  ----------------------------------------

        public String generateClassDot(GenericClass gc, int val) {
            String pref = "\tn" + val + " [label = <{";
            String out = formDotName(gc) + "|";
            for(int i = 0; i < gc.getNumberInstanceVariables(); i++) {
                out += DotComponent.dotInstanceVariable(gc.getInstanceVariableVisibilityAt(i).getDotRepr(), gc.getInstanceVariableNameAt(i), gc.getInstanceVariableTypeAt(i), gc.getInstanceVariableStaticAt(i), gc.getInstanceVariableFinalAt(i)) + (i + 1 < gc.getNumberInstanceVariables() ? "<BR/>" : "");
            }
            out += "|";
            out += getFunctionDot(gc);
            String post = "}>];\n";
            return pref + out + post;
        };

        public String generateDotClassAssociations(GenericClass gc) {
            int val = reference.get(gc.getFullName());
            String out = "";
            if(gc.getInheritance() != null) {
                out = "\tn" + val + " -> n" + reference.get(gc.getInheritance().getFullName()) + "[arrowhead=onormal];\n";
            }
            out += generateDotAssociations(gc);
            for(GenericDefinition i : gc.getRealizations()) {
                out += "\tn" + val + " -> n" + reference.get(i.getFullName()) + "[arrowhead=onormal, style=dashed];\n";
            }
            return out;
        }

        private static String formDotName(GenericClass gc) {
            String out = gc.getName();
            if(gc.getAbstract()){
                out = "<i>" + out + "</i>";
            }
            return out;
        }

        //-- GenericInterface  ------------------------------------

        public String generateInterfaceDot(GenericDefinition gi, int val) {
            String pref = "\tn" + val + " [label = <{";
            String out = formInterfaceName() + "<BR/>" + gi.getName() + "|";
            out += "|";
            out += getFunctionDot(gi);
            String post = "}>];\n";
            return pref + out + post;
        }

        private String formInterfaceName() {
            return HTML_LT + HTML_LT + "interface" + HTML_GT + HTML_GT ;
        }

        public String generateDotInterfaceAssociations(GenericDefinition gi) {
            int val = reference.get(gi.getFullName());
            String out = "";
            for(GenericDefinition i : gi.getRealizations()) {
                out += "\tn" + val + " -> n" + reference.get(i.getFullName()) + "[arrowhead=onormal, style=solid];\n";
            }
            return out + generateDotAssociations(gi);
        }

        //-- Generic Enum  ----------------------------------------

        public String generateEnumDot(GenericDefinition gi, int val) {
            String pref = "\tn" + val + " [label = <{";
            String out = formEnumName() + "<BR/>" + gi.getName() + "|";
            out += "|";
            out += getFunctionDot(gi);
            String post = "}>];\n";
            return pref + out + post;
        }

        private String formEnumName() {
            return HTML_LT + HTML_LT + "enumeration" + HTML_GT + HTML_GT ;
        }

        public String generateDotEnumAssociations(GenericDefinition gi) {
            int val = reference.get(gi.getFullName());
            String out = "";
            for(GenericDefinition i : gi.getRealizations()) {
                out += "\tn" + val + " -> n" + reference.get(i.getFullName()) + "[arrowhead=onormal, style=dotted];\n";
            }
            return out + generateDotAssociations(gi);
        }

        //-- GenericDefinition  -----------------------------------

        protected String generateDotAssociations(GenericDefinition gd) {
            String out = "";
            for(GenericDefinition c : gd.getClassAssociates()) {
                int mV = reference.get(gd.getFullName());
                int yV = reference.get(c.getFullName());
                if(!c.hasAssociate(gd) || mV <= yV) {  //Processes numerically, so if mutual, only draw if first time seeing
                    out += "\tn" + mV + " -> n" + yV;
                    if(c.hasAssociate(gd)) {
                        out += "[arrowhead=none]";
                    }
                    else {
                        out += "[arrowhead=normal]";
                    }
                    out += ";\n";
                }
            }
            return out;
        }

        //-- Helper  ----------------------------------------------

        private String getFunctionDot(GenericDefinition gd) {
            String out = "";
            for(int i = 0; i < gd.getNumberFunctions(); i++) {
                String[] argNom = gd.getFunctionArgumentNamesAt(i);
                String[] argTyp = gd.getFunctionArgumentTypesAt(i);
                out += DotComponent.dotFunction(gd.getFunctionVisibilityAt(i).getDotRepr(), gd.getFunctionNameAt(i), gd.getFunctionTypeAt(i), argNom, argTyp, gd.getFunctionAbstractAt(i), gd.getFunctionStaticAt(i), gd.getFunctionFinalAt(i)) + (i + 1 < gd.getNumberFunctions() ? "<BR/>" : "");
            }
            return out;
        }

        //-- Clusters  --------------------------------------------

        private String processClusters(String out, Cluster next, int depth, int fontSize, int penWidth) {
            if(next == null)
                return out;
            String address = next.getAddress();
            out += tabBuffer(depth) + "subgraph cluster_" + address.replaceAll("\\.",  "_") + "{\n";
            out += tabBuffer(depth + 1) + "label = \"" + address + "\";\n";
            out += tabBuffer(depth + 1) + "fontsize = " + fontSize + ";\n";
            out += tabBuffer(depth + 1) + "penwidth = " + penWidth + ";\n";
            for(String gd : next.getComponents()) {
                out += tabBuffer(depth + 1) + "n" + reference.get(gd) + ";\n";
            }
            for(Cluster c : next.getChildren()) {
                out = processClusters(out, c, depth + 1, fontSize - 4, penWidth + 1);
            }
            out += tabBuffer(depth) + "}\n";
            return out;
        }

        private String tabBuffer(int in) {
            String out = "";
            while(in-- > 0) {
                out += "\t";
            }
            return out;
        }

    }
}
