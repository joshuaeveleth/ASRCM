package gov.va.med.srcalc.web.view.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.common.collect.ImmutableSet;

import gov.va.med.srcalc.domain.model.Rule;
import gov.va.med.srcalc.domain.model.ValueMatcher;
import gov.va.med.srcalc.service.InvalidIdentifierException;
import gov.va.med.srcalc.service.ModelInspectionService;

/**
 * <p>A form backing object for creating a new or editing an existing
 * Rule.</p>
 */
public class EditRule
{
    /**
     * Used in {@link EditRuleValidator}.
     */
    public static final int MAX_MATCHERS = 10;
    
    private final List<ValueMatcherBuilder> fMatchers;
    private String fSummandExpression;
    private boolean fBypassEnabled;
    private String fDisplayName;
    private String fNewVariableKey;
    
    /**
     * Constructs an instance with default values and an empty list of 
     * ValueMatcherBuilder objects.
     */
    public EditRule() 
    {
        fMatchers = new ArrayList<ValueMatcherBuilder>();
        fSummandExpression = "";
        fBypassEnabled = false;
        fDisplayName = "";
        fNewVariableKey = "";
    }
    
    /**
     * Constructs an instance that is filled with the same information that the rule
     * contains.
     * 
     * @param rule the rule to copy into this EditRule
     */
    public EditRule(final Rule rule)
    {
        // Copy the value matchers
        fMatchers = new ArrayList<ValueMatcherBuilder>(rule.getMatchers().size());
        for(final ValueMatcher matcher: rule.getMatchers())
        {
            fMatchers.add(ValueMatcherBuilder.fromPrototype(matcher));
        }
        fSummandExpression = rule.getSummandExpression();
        fBypassEnabled = rule.isBypassEnabled();
        fDisplayName = rule.getDisplayName();
        fNewVariableKey = null;
    }
    
    /**
     * Returns the list of ValueMatcherBuilders for this EditRule.
     */
    public List<ValueMatcherBuilder> getMatchers()
    {
        return fMatchers;
    }

    /**
     * Returns the summand expression for this EditRule in String form.
     */
    public String getSummandExpression()
    {
        return fSummandExpression;
    }

    /**
     * Sets the summand expression for this EditRule.
     * @param summandExpression
     */
    public void setSummandExpression(final String summandExpression)
    {
        this.fSummandExpression = summandExpression;
    }

    /**
     * Returns true if this rule is required and false if it is not required.
     */
    public boolean isBypassEnabled()
    {
        return fBypassEnabled;
    }

    /**
     * Sets if this rule is required or not.
     * @param bypassEnabled True for required, false for not required.
     */
    public void setBypassEnabled(final boolean bypassEnabled)
    {
        this.fBypassEnabled = bypassEnabled;
    }

    /**
     * Returns this EditRule's display name.
     */
    public String getDisplayName()
    {
        return fDisplayName;
    }

    /**
     * Sets this EditRule's display name.
     * @param displayName
     */
    public void setDisplayName(final String displayName)
    {
        this.fDisplayName = displayName;
    }
    
    /**
     * Returns the variable key used to identify a new variable being added to
     * this EditRule.
     */
    public String getNewVariableKey()
    {
        return fNewVariableKey;
    }
    
    /**
     * Sets the variable key used to identify a new variable being added to this
     * EditRule.
     * @param newVariableKey
     */
    public void setNewVariableKey(final String newVariableKey)
    {
        fNewVariableKey = newVariableKey;
    }
    
    /**
     * Returns true if this object is editing an existing rule.
     */
    public boolean isEditingRule()
    {
        return false;
    }

    /**
     * Returns a validator for this class.
     */
    public EditRuleValidator getValidator()
    {
        return new EditRuleValidator();
    }
    
    /**
     * Returns all variables that this EditRule requires.
     */
    public Set<String> getRequiredVariableKeys()
    {
        final SortedSet<String> variableKeys = new TreeSet<String>();
        for (final ValueMatcherBuilder vm : fMatchers)
        {
            variableKeys.add(vm.getVariableKey());
        }
        return ImmutableSet.copyOf(variableKeys);
    }
    
    /**
     * Build a new {@link Rule} object from this EditRule.
     * @return a Rule with the same information as this EditRule
     * @throws InvalidIdentifierException if one of the variable keys present in the builder
     * does not exist in the database.
     */
    public Rule buildNew(final ModelInspectionService adminService)
            throws InvalidIdentifierException
    {
        final List<ValueMatcher> matchers = new ArrayList<ValueMatcher>(fMatchers.size());
        for(final ValueMatcherBuilder builder: fMatchers)
        {
            matchers.add(builder.buildNew(adminService));
        }
        final Rule rule = new Rule(matchers, fSummandExpression, fBypassEnabled, fDisplayName);
        return rule;
    }
}
