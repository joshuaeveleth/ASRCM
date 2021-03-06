package gov.va.med.srcalc.domain.model;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;

/**
 * Tests the {@link BooleanTerm} class.
 */
public class BooleanTermTest
{
    @Test
    public final void testBasic()
    {
        final float coeff = -1.7f;
        final BooleanVariable var = SampleModels.dnrVariable();
        final BooleanTerm term = new BooleanTerm(var, coeff);
        
        assertEquals(coeff, term.getCoefficient(), 0.0f);
        assertSame(var, term.getVariable());
        assertEquals(ImmutableSet.of(var), term.getRequiredVariables());
        assertThat(term.toString(), allOf(
                containsString(Float.toString(coeff)), containsString("DNR")));
    }
    
    @Test
    public final void testEquals()
    {
        EqualsVerifier.forClass(BooleanTerm.class)
            // Public interface disallows null.
            .suppress(Warning.NONFINAL_FIELDS, Warning.NULL_FIELDS)
            .verify();
    }
    
}
