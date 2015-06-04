package gov.va.med.srcalc.web.view;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.calculation.RetrievedValue;
import gov.va.med.srcalc.domain.calculation.SampleCalculations;
import gov.va.med.srcalc.domain.calculation.ValueRetriever;
import gov.va.med.srcalc.domain.model.*;

import java.util.*;

import org.junit.Test;

public class VariableEntryTest
{
    @Test
    public final void testToString()
    {
        final VariableEntry entry = new VariableEntry(
                Collections.<Variable>emptyList());
        entry.getDynamicValues().put("one", "1");
        entry.getDynamicValues().put("two", "2");
        assertThat(
                entry.toString(),
                allOf(containsString("one=1"), containsString("two=2")));
    }
    
    @Test
    public final void testDefaults()
    {
        // Contains multiple variables, but only one DiscreteNumericalVariable.
        final List<AbstractVariable> vars = SampleModels.sampleVariableList();
        
        // Behavior verification.
        final VariableEntry entry = new VariableEntry(vars);
        final HashMap<String, String> expected = new HashMap<>();
        expected.put(vars.get(5).getKey(), VariableEntry.SPECIAL_NUMERICAL);
        assertEquals(expected, entry.getDynamicValues());
    }
    
    @Test
    public final void testWithRetrievedValues()
    {
    	// Contains multiple variables, but only one DiscreteNumericalVariable.
        final List<AbstractVariable> vars = SampleModels.sampleVariableList();
        
        // Behavior verification.
        final Patient patient = SampleCalculations.dummyPatient(1);
        final VariableEntry entry = VariableEntry.withRetrievedValues(vars, patient);
        final HashMap<String, String> expected = new HashMap<>();
        expected.put(vars.get(1).getKey(), String.valueOf(patient.getAge()));
        expected.put(vars.get(2).getKey(), patient.getGender());
        expected.put(vars.get(5).getKey(), VariableEntry.SPECIAL_NUMERICAL);
        assertEquals(expected, entry.getDynamicValues());
    }
    
    @Test
    public final void testRetrievedCardiacAge()
    {
    	final List<AbstractVariable> vars = SampleModels.sampleCardiacCABGVariableList();
    	final Patient patient = SampleCalculations.dummyPatient(1);
    	final VariableEntry entry = VariableEntry.withRetrievedValues(vars, patient);
    	final HashMap<String, String> expected = new HashMap<>();
    	expected.put(VariableEntry.getNumericalInputName((DiscreteNumericalVariable)vars.get(0)),
    			String.valueOf(patient.getAge()));
    	expected.put(vars.get(0).getKey(), VariableEntry.SPECIAL_NUMERICAL);
    	expected.put(vars.get(1).getKey(), patient.getGender());
    	
    	assertEquals(expected, entry.getDynamicValues());
    }
    
    @Test
    public final void testWithRetrievedLabs()
    {
        final List<AbstractVariable> vars = SampleModels.sampleVariableList();
        final Patient patient = SampleCalculations.dummyPatientWithLabs(1);
        final VariableEntry entry = VariableEntry.withRetrievedValues(vars, patient);
        final HashMap<String, String> expected = new HashMap<>();
        
        expected.put(vars.get(1).getKey(), String.valueOf(patient.getAge()));
        expected.put(vars.get(2).getKey(), patient.getGender());
        expected.put(vars.get(5).getKey(), VariableEntry.SPECIAL_NUMERICAL);
        
        final String numericalString = VariableEntry.SEPARATOR + VariableEntry.SPECIAL_NUMERICAL;
        final String dateString = numericalString + VariableEntry.SEPARATOR + VariableEntry.RETRIEVAL_STRING;
        final RetrievedValue labValue = patient.getLabs().get("WBC");
        
        final String retrievalString = ValueRetriever.makeRetrievalString(
                labValue.getValue(),
                labValue.getMeasureDate(),
                labValue.getUnits());
        
        expected.put(vars.get(5).getKey() + numericalString,
                String.valueOf(labValue.getValue()));
        expected.put(vars.get(5).getKey() + dateString, retrievalString);
        assertEquals(expected, entry.getDynamicValues());
    }
}
