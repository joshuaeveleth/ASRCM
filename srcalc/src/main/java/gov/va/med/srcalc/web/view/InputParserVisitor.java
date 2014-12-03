package gov.va.med.srcalc.web.view;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// Using StringUtils instead of commons-lang to ease the dependencies.
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import gov.va.med.srcalc.domain.Procedure;
import gov.va.med.srcalc.domain.variable.*;

/**
 * <p>A {@link VariableVisitor} that produces a {@link Value} for each Variable
 * based on a {@link VariableEntry}.</p>
 * 
 * <p>Tightly coupled with {@link InputGeneratorVisitor}.</p>
 */
public class InputParserVisitor implements VariableVisitor
{
    private static final Logger fLogger = LoggerFactory.getLogger(InputParserVisitor.class);

    private final VariableEntry fVariableEntry;
    private final Errors fErrors;
    private final ArrayList<Value> fValues;
    
    public InputParserVisitor(final VariableEntry variableEntry, final Errors errors)
    {
        fLogger.debug(
                "Creating InputParserVisitor for the given request values: {}",
                variableEntry);

        fVariableEntry = variableEntry;
        fValues = new ArrayList<>();
        fErrors = errors;
    }

    /**
     * Returns the given variable's value from the {@link VariableEntry}, or
     * null if there is no value.
     */
    protected String getVariableValue(final Variable variable)
    {
        return fVariableEntry.getDynamicValues().get(variable.getDisplayName());
    }
    
    /**
     * Calls {@link Errors#rejectValue(String, String, String)} specifying the
     * proper nested field name.
     * @param variableName the key in the dynamicValues map
     * @param errorCode
     * @param defaultMessage
     */
    protected void rejectDynamicValue(
            final String variableName, final String errorCode, final String defaultMessage)
    {
        fErrors.rejectValue(
                VariableEntry.makeDynamicValuePath(variableName),
                errorCode,
                defaultMessage);
    }
    
    /**
     * Calls {@link Errors#rejectValue(String, String, Object[], String)} specifying the
     * proper nested field name.
     * @param variableName the key in the dynamicValues map
     * @param errorCode
     * @param errorArgs
     * @param defaultMessage
     */
    protected void rejectDynamicValue(
            final String variableName,
            final String errorCode,
            final Object[] errorArgs,
            final String defaultMessage)
    {
        fErrors.rejectValue(
                VariableEntry.makeDynamicValuePath(variableName),
                errorCode,
                errorArgs,
                defaultMessage);
    }
    
    /**
     * Returns a Map of value to {@link MultiSelectOption} for all of the options.
     */
    public Map<String, MultiSelectOption> buildOptionMap(final Iterable<MultiSelectOption> options)
    {
        final HashMap<String, MultiSelectOption> map = new HashMap<>();
        for (final MultiSelectOption option : options)
        {
            map.put(option.getValue(), option);
        }
        return map;
    }
    
    @Override
    public void visitMultiSelect(final MultiSelectVariable variable)
    {
        fLogger.debug("Parsing MultiSelectVariable {}", variable);

        final String value = getVariableValue(variable);
        if (StringUtils.isEmpty(value))
        {
            rejectDynamicValue(variable.getDisplayName(), "noSelection", "no selection");
            return;
        }
        // Find the selected option.
        final Map<String, MultiSelectOption> optionMap = buildOptionMap(variable.getOptions());
        final MultiSelectOption selectedOption = optionMap.get(value);
        if (selectedOption == null)
        {
            rejectDynamicValue(variable.getDisplayName(), "invalid", "not a valid selection");
        }
        else
        {
            fValues.add(new MultiSelectValue(variable, selectedOption));
        }
    }
    
    @Override
    public void visitNumerical(final NumericalVariable variable)
    {
        fLogger.debug("Parsing NumericalVariable {}", variable);

        final String stringValue = getVariableValue(variable);
        if (StringUtils.isEmpty(stringValue))
        {
            rejectDynamicValue(variable.getDisplayName(), "noInput.int", "no input");
            return;
        }
        try
        {
            final int intValue = Integer.parseInt(stringValue);
            if (intValue < variable.getMinValue())
            {
                rejectDynamicValue(
                        variable.getDisplayName(),
                        "tooLow",
                        new Object[]{ variable.getMinValue() },
                        "must be greater than or equal to {0}");
            }
            else if (intValue > variable.getMaxValue())
            {
                rejectDynamicValue(
                        variable.getDisplayName(),
                        "tooHigh",
                        new Object[]{ variable.getMaxValue() },
                        "must be less than or equal to {0}");
            }
            else
            {
                fValues.add(new NumericalValue(variable, intValue));
            }
        }
        catch (NumberFormatException ex)
        {
            rejectDynamicValue(variable.getDisplayName(), "typeMismatch.int", ex.getMessage());
        }
    }
    
    @Override
    public void visitProcedure(ProcedureVariable variable) throws Exception
    {
        fLogger.debug("Parsing ProcedureVariable {}", variable);

        final String selectedCpt = getVariableValue(variable);
        if (StringUtils.isEmpty(selectedCpt))
        {
            rejectDynamicValue(variable.getDisplayName(), "noSelection", "no selection");
            return;
        }
        final Procedure selectedProcedure =
                variable.getProcedureMap().get(selectedCpt);
        if (selectedProcedure == null)
        {
            rejectDynamicValue(variable.getDisplayName(), "invalid", "not a valid procedure");
        }
        else
        {
            fValues.add(new ProcedureValue(variable, selectedProcedure));
        }
    }
    
    public List<Value> getValues()
    {
        return fValues;
    }
}