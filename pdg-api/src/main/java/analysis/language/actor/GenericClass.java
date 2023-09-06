/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.language.actor;

import java.util.ArrayList;
import java.util.List;

import analysis.language.Visibility;
import analysis.language.component.InstanceVariable;

/**
 * The platonic ideal of a Class
 * 
 * @author Ada Clevinger
 */
public class GenericClass extends GenericDefinition {

    // Instance Variables

    private boolean isAbstract;

    private GenericDefinition inheritance;

    private List<InstanceVariable> instanceVariables;

    // Constructors

    public GenericClass(String name, String context) {
        super(name, context);
        instanceVariables = new ArrayList<InstanceVariable>();
        inheritance = null;
    }

    // Setter Methods

    public void addInstanceVariable(InstanceVariable in) {
        instanceVariables.add(in);
    }

    public void addInstanceVariable(Visibility vis, String name, String type, boolean isStatic, boolean isFinal) {
        InstanceVariable iv = new InstanceVariable(vis, name, type);
        iv.setStatic(isStatic);
        iv.setFinal(isFinal);
        addInstanceVariable(iv);
    }

    @Override
    public void addAssociation(GenericDefinition gd) {
        if (inheritance == null || !gd.equals(inheritance)) { // TODO: While I don't allow multiple associations
            super.addAssociation(gd);
        }
    }

    public void setAbstract(boolean in) {
        isAbstract = in;
    }

    public void setInheritance(GenericDefinition ref) {
        inheritance = ref;
    }

    // Getter Methods

    // Instance Variables

    public List<InstanceVariable> getInstanceVariables() {
        return instanceVariables;
    }

    public InstanceVariable getInstanceVariableAt(int index) {
        return getInstanceVariables().get(index);
    }

    public int getNumberInstanceVariables() {
        return getInstanceVariables().size();
    }

    public String getInstanceVariableTypeAt(int index) {
        return getInstanceVariableAt(index).getType();
    }

    public Visibility getInstanceVariableVisibilityAt(int index) {
        return getInstanceVariableAt(index).getVisibility();
    }

    public String getInstanceVariableNameAt(int index) {
        return getInstanceVariableAt(index).getName();
    }

    public boolean getInstanceVariableStaticAt(int index) {
        return getInstanceVariableAt(index).getStatic();
    }

    public boolean getInstanceVariableFinalAt(int index) {
        return getInstanceVariableAt(index).getFinal();
    }

    // GenericClass

    public GenericDefinition getInheritance() {
        return inheritance;
    }

    public boolean getAbstract() {
        return isAbstract;
    }

}