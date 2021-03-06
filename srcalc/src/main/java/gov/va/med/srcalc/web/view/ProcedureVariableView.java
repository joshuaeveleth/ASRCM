package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.model.ProcedureVariable;

/**
 * This class represents the visible attributes of a {@link ProcedureVariable} in order to
 * provide other attributes that are only necessary for display.
 */
public class ProcedureVariableView extends VariableView
{
    private static final String PROCEDURE_FRAGMENT = "procedureInputs.jsp";
    
    /**
     * Constructs an instance.
     * @param variable the ProcedureVariable to copy information from
     * @param referenceInfo the reference information for this ProcedureVariable
     */
    public ProcedureVariableView(final ProcedureVariable variable, final String referenceInfo)
    {
        super(variable, referenceInfo);
    }

    @Override
    public String getFragmentName()
    {
        return PROCEDURE_FRAGMENT;
    }
}
