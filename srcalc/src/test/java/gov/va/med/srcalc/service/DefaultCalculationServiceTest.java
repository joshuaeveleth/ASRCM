package gov.va.med.srcalc.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.va.med.srcalc.db.SpecialtyDao;
import gov.va.med.srcalc.domain.calculation.Calculation;
import gov.va.med.srcalc.domain.model.SampleModels;
import gov.va.med.srcalc.domain.model.Specialty;
import gov.va.med.srcalc.vista.MockVistaPatientDao;

import org.joda.time.DateTime;
import org.junit.Test;

public class DefaultCalculationServiceTest
{
    protected static final int SAMPLE_PATIENT_DFN = 1;

    public SpecialtyDao mockSpecialtyDao()
    {
        final SpecialtyDao dao = mock(SpecialtyDao.class);
        when(dao.getAllSpecialties()).thenReturn(SampleModels.specialtyList());
        final Specialty specialty = SampleModels.thoracicSpecialty();
        when(dao.getByName(specialty.getName())).thenReturn(specialty);
        return dao;
    }
    
    public DefaultCalculationService defaultCalculationService()
    {
        return new DefaultCalculationService(mockSpecialtyDao(), new MockVistaPatientDao());
    }
    
    @Test
    public final void testGetValidSpecialties()
    {
        // Create the class under test.
        final DefaultCalculationService s = defaultCalculationService();

        // Behavior verification.
        assertEquals(SampleModels.specialtyList(), s.getValidSpecialties());
    }

    @Test
    public final void testStartNewCalculation()
    {
        final DateTime testStartDateTime = new DateTime();

        // Create the class under test.
        final DefaultCalculationService s = defaultCalculationService();
        
        // Behavior verification.
        final Calculation calc = s.startNewCalculation(SAMPLE_PATIENT_DFN);
        assertEquals(SAMPLE_PATIENT_DFN, calc.getPatient().getDfn());
        assertTrue("start date not in the past",
                // DateTime has millisecond precision, so the current time may
                // still be the same. Use "less than or equal to".
                calc.getStartDateTime().compareTo(new DateTime()) <= 0);
        assertTrue("start date not after test start",
                calc.getStartDateTime().compareTo(testStartDateTime) >= 0);
    }
    
    @Test
    public final void testSetValidSpecialty() throws InvalidIdentifierException
    {
        final Specialty thoracicSpecialty = SampleModels.thoracicSpecialty();
        
        // Create the class under test.
        final DefaultCalculationService s = defaultCalculationService();
        final Calculation calc = s.startNewCalculation(SAMPLE_PATIENT_DFN);
        
        // Behavior verification.
        s.setSpecialty(calc, thoracicSpecialty.getName());
        assertEquals(thoracicSpecialty, calc.getSpecialty());
    }
    
    @Test(expected = InvalidIdentifierException.class)
    public final void testSetInvalidSpecialty() throws InvalidIdentifierException
    {
        final int PATIENT_DFN = 1;
        
        // Create the class under test.
        final DefaultCalculationService s = defaultCalculationService();
        final Calculation calc = s.startNewCalculation(PATIENT_DFN);
        
        // Behavior verification.
        s.setSpecialty(calc, "invalid specialty");
    }
}
