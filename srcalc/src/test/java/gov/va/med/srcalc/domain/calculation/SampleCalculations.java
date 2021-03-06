package gov.va.med.srcalc.domain.calculation;

import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.VistaLabs;
import gov.va.med.srcalc.domain.VistaPerson;
import gov.va.med.srcalc.domain.Patient.Gender;
import gov.va.med.srcalc.domain.model.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

/**
 * Constructs sample instances of {@link Calculation}s and related objects.
 */
public class SampleCalculations
{
    /**
     * Returns a sample radiologist VistA user.
     */
    public static VistaPerson radiologistPerson()
    {
        return new VistaPerson("500", "11716",
                "RADIOLOGIST,ONE",
                Optional.of("Physicians (M.D. and D.O.)"));
    }

    /**
     * Returns a sample patient with minimal attributes populated.
     */
    public static Patient dummyPatient(final int dfn)
    {
        return new Patient(dfn, "Zach Smith", Gender.Male, 40);
    }
    
    /**
     * Returns a sample patient with vitals (height, weight, bmi) populated.
     */
    public static Patient dummyPatientWithVitals(final int dfn)
    {
        final Patient patient = dummyPatient(dfn);
        final DateTime vitalsMeasureDate = new DateTime(2014, 5, 12, 13, 21);
        patient.setBmi(new RetrievedValue(20.0, vitalsMeasureDate.toDate(), ""));
        patient.setHeight(new RetrievedValue(100.0, vitalsMeasureDate.toDate(), "in"));
        patient.setWeight(new RetrievedValue(190.0, vitalsMeasureDate.toDate(), "lbs."));
        patient.setWeight6MonthsAgo(new RetrievedValue(
                180.0, vitalsMeasureDate.minusMonths(4).toDate(), "lbs."));
        return patient;
    }

    public static Patient dummyPatientWithLabs(final int dfn)
    {
        final Patient patient = dummyPatient(dfn);
        final Map<VistaLabs, RetrievedValue> labs = new HashMap<VistaLabs, RetrievedValue>();
        
        labs.put(VistaLabs.ALBUMIN, new RetrievedValue(1.0, new Date(), ""));
        labs.put(VistaLabs.ALKALINE_PHOSPHATASE, new RetrievedValue(1.0, new Date(), ""));
        labs.put(VistaLabs.BILIRUBIN, new RetrievedValue(1.0, new Date(), ""));
        labs.put(VistaLabs.BUN, new RetrievedValue(1.0, new Date(), ""));
        labs.put(VistaLabs.CREATININE, new RetrievedValue(1.0, new Date(), ""));
        labs.put(VistaLabs.GLUCOSE, new RetrievedValue(1.0, new Date(), ""));
        labs.put(VistaLabs.HEMATOCRIT, new RetrievedValue(1.0, new Date(), ""));
        labs.put(VistaLabs.HGA1C, new RetrievedValue(1.0, new Date(), ""));
        labs.put(VistaLabs.INR, new RetrievedValue(1.0, new Date(), ""));
        labs.put(VistaLabs.PLATELETS, new RetrievedValue(1.0, new Date(), ""));
        labs.put(VistaLabs.POTASSIUM, new RetrievedValue(1.0, new Date(), ""));
        labs.put(VistaLabs.PTT, new RetrievedValue(1.0, new Date(), ""));
        labs.put(VistaLabs.SGOT, new RetrievedValue(1.0, new Date(), ""));
        labs.put(VistaLabs.SODIUM, new RetrievedValue(1.0, new Date(), ""));
        labs.put(VistaLabs.WBC, new RetrievedValue(10.0, new Date(), "x1000/mm^3"));
        
        // Replace any existing labs with the new set.
        patient.getLabs().clear();
        patient.getLabs().putAll(labs);

        return patient;
    }

    /**
     * Returns a collection of variable values for a thoracic calculation.
     * @return a Map from each Variable to its Value
     */
    public static ImmutableMap<AbstractVariable, Value> thoracicValues()
    {
        final BooleanVariable dnrVar = SampleModels.dnrVariable();
        final NumericalVariable ageVar = SampleModels.ageVariable();
        final MultiSelectVariable fsVar = SampleModels.functionalStatusVariable();
        final ProcedureVariable procVar = SampleModels.procedureVariable();
        try
        {
            return ImmutableMap.of(
                    dnrVar,
                    new BooleanValue(dnrVar, false),
                    ageVar,
                    new NumericalValue(ageVar, 45.0f),
                    fsVar,
                    new MultiSelectValue(fsVar, new MultiSelectOption("Independent")),
                    procVar,
                    new ProcedureValue(procVar, SampleModels.repairLeftProcedure()));
        }
        catch (final InvalidValueException ex)
        {
            throw new RuntimeException("test data had an invalid value", ex);
        }
    }
    
    /**
     * Create a new calculation for a dummy patient, set the specialty,
     * and then perform the calculation using the a custom set of values.
     * @return a realistic CalculationResult
     */
    public static CalculationResult thoracicResult()
    {

        final Calculation calc = Calculation.forPatient(dummyPatient(1));
        calc.setSpecialty(SampleModels.thoracicSpecialty());
        try
        {
            return calc.calculate(thoracicValues().values(), radiologistPerson());
        }
        catch (final MissingValuesException ex)
        {
            throw new RuntimeException("test data did not provide all values", ex);
        }
    }
    
    /**
     * Returns a sample {@link HistoricalCalculation} object for the Thoracic specialty.
     * Other property values are unspecified.
     */
    public static HistoricalCalculation historicalThoracic()
    {
        return new HistoricalCalculation(
                SampleModels.thoracicSpecialty().getName(),
                "500",
                new DateTime(2015, 3, 4, 10, 5).withSecondOfMinute(51),
                50,
                radiologistPerson().getProviderType());
    }

    private static final String CPT_CODE_1 = "47010";
    private static final ImmutableMap<String, String> VALUES_PROCEDURE_1 =
            ImmutableMap.of("procedure", "47010 - Open drainage liver lesion (19.4)");
    
    public static final String THORACIC_MODEL_30_DAY = "Thoracic 30-Day";
    public static final String THORACIC_MODEL_90_DAY = "Thoracic 90-Day";
    
    /**
     * Returns a sample {@link SignedResult} object for the Thoracic specialty. There are
     * two outcomes: {@link #THORACIC_MODEL_90_DAY} and {@link #THORACIC_MODEL_30_DAY}.
     * The other property values are unspecified.
     */
    public static SignedResult signedThoracic()
    {
        return new SignedResult(
            historicalThoracic(),
            1001,
            Optional.of(CPT_CODE_1),
            new DateTime(2015, 3, 4, 10, 10).withSecondOfMinute(52),
            VALUES_PROCEDURE_1,
            // Intentionally flip to emulate arbitrary DB order.
            ImmutableMap.of(THORACIC_MODEL_90_DAY, 25.1f, THORACIC_MODEL_30_DAY, 20.1f));
        
    }
}
