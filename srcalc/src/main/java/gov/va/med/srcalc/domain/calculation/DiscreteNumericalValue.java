package gov.va.med.srcalc.domain.calculation;

import gov.va.med.srcalc.ConfigurationException;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.domain.model.DiscreteNumericalVariable.Category;

/**
 * A value of a {@link DiscreteNumericalVariable}.
 */
public final class DiscreteNumericalValue implements DiscreteValue
{
    private final DiscreteNumericalVariable fVariable;
    private final Category fSelectedCategory;
    private final float fNumericalValue;
    
    /**
     * Business code should call {@link #fromCategory(DiscreteNumericalVariable, Category)}
     * or {@link #fromNumerical(DiscreteNumericalVariable, float)}.
     * @param variable the DiscreteNumericalVariable to which this value belongs
     * @param selectedCategory the category that was selected for this variable
     * @param numericalValue the numerical value for this variable (can be {@link Float#NaN}
     *          if no numerical value is present
     */
    private DiscreteNumericalValue(
            final DiscreteNumericalVariable variable,
            final Category selectedCategory,
            final float numericalValue)
    {
        fVariable = variable;
        fSelectedCategory = selectedCategory;
        fNumericalValue = numericalValue;
    }
    
    /**
     * Constructs an instance from the selected category.
     * @param variable
     * @param selectedCategory
     */
    public static DiscreteNumericalValue fromCategory(
            final DiscreteNumericalVariable variable, final Category selectedCategory)
    {
        return new DiscreteNumericalValue(variable, selectedCategory, Float.NaN);
    }
    
    /**
     * Constructs an instance from the given actual numerical value.
     * @param variable
     * @param numericalValue
     * @throws ValueTooHighException 
     * @throws ValueTooLowException 
     * @throws ConfigurationException if the value is not in any of the
     * categories but is within the valid range
     */
    public static DiscreteNumericalValue fromNumerical(
            final DiscreteNumericalVariable variable, final float numericalValue)
                    throws ValueTooLowException, ValueTooHighException
    {
        // Ensure the value is in within range.
        variable.getValidRange().checkValue(numericalValue);

        final Category category = variable.getContainingCategory(numericalValue);
        if (category == null)
        {
            throw new ConfigurationException("No matching range for valid numerical value");
        }
        return new DiscreteNumericalValue(variable, category, numericalValue);
    }

    @Override
    public DiscreteNumericalVariable getVariable()
    {
        return fVariable;
    }
    
    /**
     * Returns the Category value, either explicitly selected or the Category that matched
     * the numerical value.
     */
    public Category getCategory()
    {
        return fSelectedCategory;
    }
    
    @Override
    public MultiSelectOption getSelectedOption()
    {
        return fSelectedCategory.getOption();
    }

    /**
     * Returns the Category's String value.
     */
    @Override
    public String getValue()
    {
        return getSelectedOption().getValue();
    }
    
    /**
     * Returns the actual numerical value if one was provided. Otherwise returns
     * {@link Float#NaN}.
     */
    public float getNumericalValue()
    {
        return fNumericalValue;
    }

    @Override
    public String getDisplayString()
    {
        // If a numerical value was given, write the category with the actual
        // value. Otherwise, just write "Presumed" followed by the category.
        if (Float.isNaN(fNumericalValue))
        {
            return "Presumed " + fSelectedCategory.getOption().getValue();
        }
        else
        {
            return String.format(
                    "%s (Actual Value: %s)",
                    fSelectedCategory.getOption().getValue(),
                    Float.toString(fNumericalValue));
        }
    }
    
    @Override
    public void accept(final ValueVisitor valueVisitor)
    {
        valueVisitor.visitDiscreteNumerical(this);
    }
    
    @Override
    public String toString()
    {
        return String.format("%s = %s", getVariable(), getValue());
    }
}
