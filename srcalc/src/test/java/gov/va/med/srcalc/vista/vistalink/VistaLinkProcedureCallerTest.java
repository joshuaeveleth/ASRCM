package gov.va.med.srcalc.vista.vistalink;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import javax.naming.NamingException;
import javax.resource.ResourceException;

import gov.va.med.srcalc.domain.VistaLabs;
import gov.va.med.srcalc.vista.RemoteProcedure;
import gov.va.med.srcalc.vista.vistalink.VistaLinkProcedureCaller;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnectionFactory;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnectionSpec;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

/**
 * Tests the {@link VistaLinkProcedureCaller} class.
 */
public class VistaLinkProcedureCallerTest
{
    /**
     * The division which {@link #populateJndiWithMockVlcf()} populates.
     */
    private static final String SUPPORTED_DIVISON = "500";

    private final static String VLCF_JNDI_NAME = "java:comp/env/vlj/Asrc500";
    
    /**
     * Populate JNDI with a mock VistaLinkConnectionFactory that will return a
     * MockVistaLinkConnection.
     */
    private static void populateJndiWithMockVlcf()
            throws NamingException, ResourceException
    {
        SimpleNamingContextBuilder builder =
                SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        final VistaLinkConnectionFactory vlcf = mock(VistaLinkConnectionFactory.class);
        when(vlcf.getConnection(isNotNull(VistaLinkConnectionSpec.class)))
            .thenReturn(new MockVistaLinkConnection());
        builder.bind(VLCF_JNDI_NAME, vlcf);
    }

    @Before
    public void setUp() throws Exception
    {
        populateJndiWithMockVlcf();
    }
    
    /**
     * Verifies execution of {@link RemoteProcedure#GET_USER_INFO}, which requires a
     * different RPC context.
     */
    @Test
    public final void testDoUserRpc() throws Exception
    {
        final VistaLinkProcedureCaller caller =
                new VistaLinkProcedureCaller(SUPPORTED_DIVISON);
        
        final List<String> results = caller.doRpc("11111", RemoteProcedure.GET_USER_INFO);
        // Just verify the name.
        assertEquals(MockVistaLinkConnection.RADIOLOGIST_NAME, results.get(1));
    }
    
    @Test
    public final void testDoPatientRpc() throws Exception
    {
        final VistaLinkProcedureCaller caller =
                new VistaLinkProcedureCaller(SUPPORTED_DIVISON);
        final List<String> results = caller.doRpc(
                "11111", RemoteProcedure.GET_PATIENT, MockVistaLinkConnection.PATIENT_DFN);
        assertEquals(
                Arrays.asList(MockVistaLinkConnection.PATIENT_DATA),
                results);
    }
    
    @Test
    public final void testDoSaveProgressNoteCall() throws Exception
    {
        final VistaLinkProcedureCaller caller =
                new VistaLinkProcedureCaller(SUPPORTED_DIVISON);
        final String result = caller.doSaveProgressNoteCall(
                "11111",
                "fakeEncryptedSig",
                MockVistaLinkConnection.PATIENT_DFN,
                Arrays.asList("line1", "line2"));
        
        assertEquals(RemoteProcedure.VALID_SIGNATURE_RETURN, result);
    }
    
    @Test
    public final void testDoSaveRiskCalculationCall() throws Exception
    {
        final VistaLinkProcedureCaller caller =
                new VistaLinkProcedureCaller(SUPPORTED_DIVISON);
        final String result = caller.doSaveRiskCalculationCall(
                "11111",
                MockVistaLinkConnection.PATIENT_DFN,
                "12345",
                "01/01/2015@1001",
                Arrays.asList("Model^02.1"));
        
        assertEquals(RemoteProcedure.RISK_SAVED_RETURN, result);
    }
    
    @Test
    public final void testDoRetrieveLabsCallSgot() throws Exception
    {
        final VistaLinkProcedureCaller caller =
                new VistaLinkProcedureCaller(SUPPORTED_DIVISON);
        final String result = caller.doRetrieveLabsCall(
                "11111",
                MockVistaLinkConnection.PATIENT_DFN,
                VistaLabs.SGOT.getPossibleLabNames());
        
        assertEquals(MockVistaLinkConnection.SGOT_LAB_DATA, result);
    }
    
    @Test
    public final void testDoRetrieveLabsCallAlbumin() throws Exception
    {
        final VistaLinkProcedureCaller caller =
                new VistaLinkProcedureCaller(SUPPORTED_DIVISON);
        final String result = caller.doRetrieveLabsCall(
                "11111",
                MockVistaLinkConnection.PATIENT_DFN,
                VistaLabs.ALBUMIN.getPossibleLabNames());
        
        assertEquals(MockVistaLinkConnection.ALBUMIN_LAB_DATA, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testInvalidDivision()
    {
        final String division = "600";
        
        new VistaLinkProcedureCaller(division);
    }
    
}
