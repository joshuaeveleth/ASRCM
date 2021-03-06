package gov.va.med.srcalc.domain.model;

import java.util.Map;

import javax.persistence.*;

import com.google.common.collect.ImmutableSet;

// TODO: can we eliminate this dependency on the 'calculation' package?
import gov.va.med.srcalc.domain.calculation.Value;

/**
 * <p>A single summation term in a risk model.</p>
 * 
 * <p>Presents an immutable public interface.</p>
 */
@MappedSuperclass
public abstract class ModelTerm
{
    private float fCoefficient = 0.0f;
    
    /**
     * Mainly intended for reflection-based construction. Business code should
     * use {@link #ModelTerm(float)}.
     */
    protected ModelTerm()
    {
    }
    
    /**
     * Constructs an instance.
     */
    public ModelTerm(final float coefficient)
    {
        fCoefficient = coefficient;
    }

    /**
     * Returns the coefficient assigned to this term.
     */
    @Basic
    public float getCoefficient()
    {
        return fCoefficient;
    }

    /**
     * For reflection-based construction only.
     * @deprecated because code should not explicitly call this method
     */
    @Deprecated
    void setCoefficient(float coefficient)
    {
        fCoefficient = coefficient;
    }
    
    /**
     * Returns the required Variable(s) for the term. Unmodifiable and will not
     * contain nulls.
     */
    @Transient
    public abstract ImmutableSet<Variable> getRequiredVariables();
    
    /**
     * Base equals() functionality that simply verifies equality of the coefficient.
     * @param other must not be null
     * @return true if the coefficients are equal, false otherwise
     */
    protected boolean baseEquals(final ModelTerm other)
    {
        return new Float(this.getCoefficient()).equals(other.getCoefficient());
    }
    
    /**
     * Returns the value to add to the risk model sum, given a complete set of
     * Values for the calculation.
     * @param inputValues a map from variable to value for each value. Must
     * contain a value for each required variable
     * @throws IllegalArgumentException if the given collection of values does
     * not provide a value for each required variable
     * @throws MissingValuesException if there are any variables without assigned values
     */
    public abstract float getSummand(Map<Variable, Value> inputValues) throws MissingValuesException;
    
    // A reminder to subclasses to implement equals().
    @Override
    public abstract boolean equals(Object o);
    
    // A reminder to subclasses to implement hashCode().
    @Override
    public abstract int hashCode();
    
    /**
     * Accepts the given Visitor.
     */
    public abstract void accept(final ModelTermVisitor visitor);
}
