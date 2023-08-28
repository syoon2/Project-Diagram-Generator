/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.language.actor;

import java.util.ArrayList;
import java.util.List;

import analysis.language.component.InstanceVariable;

/**
 * The platonic ideal of a Class
 * @author Ada Clevinger
 *
 */

public class GenericClass extends GenericDefinition{

//---  Instance Variables   -------------------------------------------------------------------

    private boolean isAbstract;

    private GenericDefinition inheritance;

    private List<InstanceVariable> instanceVariables;

//---  Constructors   -------------------------------------------------------------------------

    public GenericClass(String inName, String inContext) {
        super(inName, inContext);
        instanceVariables = new ArrayList<InstanceVariable>();
        inheritance = null;
    }

//---  Setter Methods   -----------------------------------------------------------------------

    public void addInstanceVariable(InstanceVariable in) {
        instanceVariables.add(in);
    }

    public void addInstanceVariable(String vis, String nom, String typ, boolean isStatic, boolean isFinal) {
        InstanceVariable iv = new InstanceVariable(vis, nom, typ);
        iv.setStatic(isStatic);
        iv.setFinal(isFinal);
        addInstanceVariable(iv);
    }

    @Override
    public void addAssociation(GenericDefinition gd) {
        if(inheritance == null || !gd.equals(inheritance)) {	//TODO: While I don't allow multiple associations
            super.addAssociation(gd);
        }
    }

    public void setAbstract(boolean in) {
        isAbstract = in;
    }

    public void setInheritance(GenericDefinition ref) {
        inheritance = ref;
    }

//---  Getter Methods   -----------------------------------------------------------------------

    //-- Instance Variables  ----------------------------------

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

    public String getInstanceVariableVisibilityAt(int index) {
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

    //-- GenericClass  ----------------------------------------

    public GenericDefinition getInheritance() {
        return inheritance;
    }

    public boolean getAbstract() {
        return isAbstract;
    }

}