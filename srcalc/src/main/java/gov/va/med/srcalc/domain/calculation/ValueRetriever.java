package gov.va.med.srcalc.domain.calculation;

import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.model.Variable;
import gov.va.med.srcalc.web.view.VariableEntry;

/**
 * <p>The set of retrievers that extract a particular variable's value from a
 * Patient object.</p>
 * 
 * <p>Most of these retrieve a numerical (floating-point) value, but GENDER
 * retrieves discrete String values. TODO: this enumeration should probably be
 * split based on this discrepancy.</p>
 */
public enum ValueRetriever
{
    GENDER
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            variableEntry.getDynamicValues().put(key, patient.getGender());
        }
    },
    AGE
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            variableEntry.getDynamicValues().put(key, String.valueOf(patient.getAge()));
        }
    },
    BMI
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            if (patient.getBmi() != null)
            {
                variableEntry.getDynamicValues().put(key, String.valueOf(patient.getBmi().getValue()));
                final String retrievalString = VariableEntry.makeRetrievalString(
                        patient.getBmi().getValue(),
                        patient.getBmi().getMeasureDate(),
                        patient.getBmi().getUnits());
                variableEntry.setMeasureDate(key, retrievalString);
            }
        }
    },
    WEIGHT
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            if (patient.getWeight() != null)
            {
                variableEntry.getDynamicValues().put(key, String.valueOf(patient.getWeight().getValue()));
                final String retrievalString = VariableEntry.makeRetrievalString(
                        patient.getWeight().getValue(),
                        patient.getWeight().getMeasureDate(),
                        patient.getWeight().getUnits());
                variableEntry.setMeasureDate(key, retrievalString);
            }
        }
    },
    WEIGHT_6_MONTHS_AGO
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            if (patient.getWeight6MonthsAgo() != null)
            {
                variableEntry.getDynamicValues().put(key, String.valueOf(patient.getWeight6MonthsAgo().getValue()));
                final String retrievalString = VariableEntry.makeRetrievalString(
                        patient.getWeight6MonthsAgo().getValue(),
                        patient.getWeight6MonthsAgo().getMeasureDate(),
                        patient.getWeight6MonthsAgo().getUnits());
                variableEntry.setMeasureDate(key, retrievalString);
            }
        }
    },
    HEIGHT
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            if (patient.getHeight() != null)
            {
                variableEntry.getDynamicValues().put(key, String.valueOf(patient.getHeight().getValue()));
                final String retrievalString = VariableEntry.makeRetrievalString(
                        patient.getHeight().getValue(),
                        patient.getHeight().getMeasureDate(),
                        patient.getHeight().getUnits());
                variableEntry.setMeasureDate(key, retrievalString);
            }
        }
    },
    ALBUMIN
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            addLabValue("ALBUMIN", patient, variableEntry, key);
        }
    },
    CREATININE
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            addLabValue("CREATININE", patient, variableEntry, key);
        }
    },
    ALKALINE_PHOSPHATASE
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            addLabValue("ALKALINE_PHOSPHATASE", patient, variableEntry, key);
        }
    },
    BUN
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            addLabValue("BUN", patient, variableEntry, key);
        }
    },
    SGOT
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            addLabValue("SGOT", patient, variableEntry, key);
        }
    },
    WBC
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            addLabValue("WBC", patient, variableEntry, key);
        }
    },
    PLATELETS
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            addLabValue("PLATELETS", patient, variableEntry, key);
        }
    },
    HEMATOCRIT
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            addLabValue("HEMATOCRIT", patient, variableEntry, key);
        }
    },
    SODIUM
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            addLabValue("SODIUM", patient, variableEntry, key);
        }
    },
    INR
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            addLabValue("INR", patient, variableEntry, key);
        }
    },
    BILIRUBIN
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            addLabValue("BILIRUBIN", patient, variableEntry, key);
        }
    },
    PTT
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            addLabValue("PTT", patient, variableEntry, key);
        }
    };
    
    /**
     * Attempt to add the retrieved value to the {@link VariableEntry} object. Do nothing if
     * there is no retrieved value for the specified variable.
     * @param patient the current patient
     * @param variableEntry the {@link VariableEntry} object to put the retrieved value into
     * @param variable the variable to get the retrieved value for
     * @param key the variable's fully qualified key (i.e. "bmi$numerical" for bmi)
     */
    public abstract void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
            final String key);
    
    /**
     * If there was a retrieved value for the specified lab, it will be added to the variable entry.
     * If there was no value then nothing needs to be done.
     * @param labName the well-known name of the lab
     * @param patient the current patient
     * @param variableEntry the {@link VariableEntry} object to put the retrieved value into
     * @param key the variable's fully qualified key (i.e. "bmi$numerical" for bmi)
     */
    protected static void addLabValue(final String labName, final Patient patient, final VariableEntry variableEntry,
            final String key)
    {
        final RetrievedValue labValue = patient.getLabs().get(labName);
        if(labValue != null)
        {
            variableEntry.getDynamicValues().put(key, String.valueOf(labValue.getValue()));
            final String retrievalString = VariableEntry.makeRetrievalString(
                    labValue.getValue(),
                    labValue.getMeasureDate(),
                    labValue.getUnits());
            variableEntry.setMeasureDate(key, retrievalString);
        }
    }
}