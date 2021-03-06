package gov.va.med.srcalc.domain.calculation;

import gov.va.med.srcalc.domain.model.*;

/**
 * A value of a {@link MultiSelectVariable}.
 */
public class MultiSelectValue implements DiscreteValue
{
    private final MultiSelectVariable fVariable;
    private final MultiSelectOption fSelectedOption;
    
    /**
     * Constructs an instance from the selected option.
     * @param variable
     * @param selectedOption
     */
    public MultiSelectValue(final MultiSelectVariable variable, final MultiSelectOption selectedOption)
    {
        fVariable = variable;
        fSelectedOption = selectedOption;
    }
    
    @Override
    public MultiSelectVariable getVariable()
    {
        return fVariable;
    }
    
    /**
     * Returns the selected MultiSelectOption.
     */
    public MultiSelectOption getSelectedOption()
    {
        return fSelectedOption;
    }
    
    /**
     * Since a {@link MultiSelectOption} simply wraps a String, return just the
     * String for convenience.
     */
    @Override
    public String getValue()
    {
        return getSelectedOption().getValue();
    }
    
    @Override
    public String getDisplayString()
    {
        return getSelectedOption().getValue();
    }
    
    @Override
    public void accept(final ValueVisitor visitor)
    {
        visitor.visitMultiSelect(this);
    }
    
    @Override
    public String toString()
    {
        return String.format("%s = %s", getVariable(), getSelectedOption());
    }
}
