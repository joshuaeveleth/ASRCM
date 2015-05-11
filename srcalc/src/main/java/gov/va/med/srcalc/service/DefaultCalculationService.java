package gov.va.med.srcalc.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import gov.va.med.srcalc.db.SpecialtyDao;
import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.calculation.*;
import gov.va.med.srcalc.domain.model.Specialty;
import gov.va.med.srcalc.vista.VistaPatientDao;
import gov.va.med.srcalc.util.MissingValuesException;

public class DefaultCalculationService implements CalculationService
{
    private static final Logger fLogger = LoggerFactory.getLogger(DefaultCalculationService.class);
    
    private final SpecialtyDao fSpecialtyDao;
    private final VistaPatientDao fPatientDao;
    
    /**
     * Constructs an instance.
     * @param specialtyDao DAO to access specialties
     * @param patientDao DAO to access patient information
     */
    @Inject
    public DefaultCalculationService(
            final SpecialtyDao specialtyDao, final VistaPatientDao patientDao)
    {
        fSpecialtyDao = specialtyDao;
        fPatientDao = patientDao;
    }
    
    @Override
    @Transactional
    public List<Specialty> getValidSpecialties()
    {
        return fSpecialtyDao.getAllSpecialties();
    }

    @Override
    @Transactional
    public Calculation startNewCalculation(final int patientId)
    {
        final Patient patient = fPatientDao.getPatient(patientId);

        fLogger.debug("Starting calculation for patient {}.", patient);

        return Calculation.forPatient(patient);
    }
    
    @Override
    @Transactional
    public void setSpecialty(final Calculation calculation, final String specialtyName)
        throws InvalidIdentifierException
    {
        fLogger.debug("Setting specialty to {}.", specialtyName);
        
        final Specialty specialty = fSpecialtyDao.getByName(specialtyName);
        if (specialty == null)
        {
            throw new InvalidIdentifierException(
                    specialtyName + " is not a valid specialty name.");
        }
        calculation.setSpecialty(specialty);
    }
    
    @Override
    @Transactional
    public CalculationResult runCalculation(
            final Calculation calculation, final List<Value> variableValues)
            throws MissingValuesException
    {
        fLogger.debug("Running calculation with values: {}", variableValues);
        
        final CalculationResult result = calculation.calculate(variableValues);
        
        // Log something at INFO level for running a calculation, but don't log
        // too much to avoid PHI in the log file.
        fLogger.info( "Ran a {} calculation.", calculation.getSpecialty());
        
        return result;
    }

    @Override
    public VistaPatientDao.SaveNoteCode saveRiskCalculationNote(
            CalculationResult result, String electronicSignature)
    {
        return fPatientDao.saveRiskCalculationNote(
                result.getPatientDfn(), electronicSignature, result.buildNoteBody());
    }
    
}
