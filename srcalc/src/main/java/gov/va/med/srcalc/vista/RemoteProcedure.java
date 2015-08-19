package gov.va.med.srcalc.vista;

/**
 * Enumerates all VistA Remote Procedures which this application uses.
 */
public enum RemoteProcedure
{
    /**
     * Returns information about the current user.
     */
    GET_USER("SR ASRC USER"),
    
    /**
     * Returns Person Classes for the current user.
     */
    GET_USER_PERSON_CLASSES("SR ASRC PERSON CLASSES"),
    
    /**
     * Returns information about the given patient.
     */
    GET_PATIENT("SR ASRC PATIENT"),
    
    /**
     * Returns the most recent vitals on the patient.
     */
    GET_RECENT_VITALS("GMV LATEST VM"),
    
    /**
     * Returns a vital from the given patient with the specified date range.
     */
    GET_VITAL("GMV EXTRACT REC"),
    
    /**
     * Submits a completed calculation as a note on the patient's records.
     */
    SAVE_PROGRESS_NOTE("SR ASRC PROGRESS NOTE"),
    
    /**
     * Saves a Risk Calculation as discrete data to VistA Surgery.
     */
    SAVE_RISK("SR ASRC RISK SAVE"),
    
    /**
     * Returns the most recent result for the given lab name possibilities.
     */
    GET_LABS("SR ASRC LAB RESULTS"),
    
    /**
     * Returns any of the patient's health factors in the last year.
     */
    GET_HEALTH_FACTORS("SR ASRC HEALTH FACTORS"),
    
    /**
     * Returns all VA and non-VA active medications for the patient.
     */
    GET_ACTIVE_MEDICATIONS("SR ASRC ACTIVE MEDS"),

    /**
     * Returns all nursing notes with the "NURSING ADMISSION EVALUATION NOTE" title.
     */
    GET_ADL_STATUS("SR ASRC ADL NOTES"),
    
    /**
     * Returns any nursing notes for the specified patient, that contain the specified substring.
     */
    GET_NOTES_WITH_SUBSTRING("SR ASRC DNR NOTES");

    /**
     * VistA returns this string if {@link #SAVE_PROGRESS_NOTE} succeeds.
     */
    public final static String VALID_SIGNATURE_RETURN =
            "1^Progress note was created and signed successfully.";
    
    /**
     * VistA returns this string if {@link #SAVE_RISK} succeeds.
     */
    public final static String RISK_SAVED_RETURN =
            "1^Record created successfully.";
    
    private final String fProcedureName;
    
    RemoteProcedure(final String procedureName)
    {
        fProcedureName = procedureName;
    }
    
    /**
     * Returns the actual name of the Remote Procedure in VistA.
     */
    public String getProcedureName()
    {
        return fProcedureName;
    }
}
