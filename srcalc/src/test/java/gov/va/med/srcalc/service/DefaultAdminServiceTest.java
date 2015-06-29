package gov.va.med.srcalc.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import gov.va.med.srcalc.db.RiskModelDao;
import gov.va.med.srcalc.db.RuleDao;
import gov.va.med.srcalc.db.VariableDao;
import gov.va.med.srcalc.domain.model.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;

public class DefaultAdminServiceTest
{
    private List<AbstractVariable> fSampleVariables;
    private ImmutableSortedSet<VariableGroup> fSampleGroups;
    private ImmutableList<Rule> fSampleRules;
    
    @Before
    public final void setup()
    {
        fSampleVariables = SampleModels.sampleVariableList();
        fSampleGroups = SampleModels.variableGroups();
        fSampleRules = ImmutableList.of(SampleModels.ageAndFsRule());
    }
    
    private VariableDao mockVariableDao()
    {
        final VariableDao dao = mock(VariableDao.class);
        when(dao.getAllVariables()).thenReturn(fSampleVariables);
        // Use a reference to the existing DNR variable so we can do updates.
        final AbstractVariable variable = fSampleVariables.get(3);
        when(dao.getByKey(variable.getKey())).thenReturn(variable);
        when(dao.getAllVariableGroups()).thenReturn(fSampleGroups);
        return dao;
    }
    
    private RiskModelDao mockRiskModelDao()
    {
        final RiskModelDao dao = mock(RiskModelDao.class);
        when(dao.getAllRiskModels()).thenReturn(
                ImmutableList.of(SampleModels.thoracicRiskModel()));
        return dao;
    }
    
    private RuleDao mockRuleDao()
    {
        final RuleDao dao = mock(RuleDao.class);
        when(dao.getAllRules()).thenReturn(
                ImmutableList.of(SampleModels.ageAndFsRule()));
        final Rule rule = fSampleRules.get(0);
        when(dao.getByDisplayName(rule.getDisplayName())).thenReturn(rule);
        return dao;
    }
    
    @Test
    public final void testGetAllVariables()
    {
        // Create the class under test.
        final DefaultAdminService s = new DefaultAdminService(
                mockVariableDao(), mockRiskModelDao(), mockRuleDao());
        
        // Behavior verification.
        // Variables do not override equals() but this works because we use
        // the same Variable objects.
        assertEquals(fSampleVariables, s.getAllVariables());
    }
    
    @Test
    public final void testGetAllVariableGroups()
    {
        // Create the class under test.
        final DefaultAdminService s = new DefaultAdminService(
                mockVariableDao(), mockRiskModelDao(), mockRuleDao());
        
        // Behavior verification.
        assertEquals(fSampleGroups, s.getAllVariableGroups());
    }
    
    @Test
    public final void testGetVariable() throws Exception
    {
        final String key = "dnr";
        
        // Create the class under test.
        final DefaultAdminService s = new DefaultAdminService(
                mockVariableDao(), mockRiskModelDao(), mockRuleDao());
        
        // Behavior verification.
        final BooleanVariable actualVar = (BooleanVariable)s.getVariable(key);
        assertEquals(key, actualVar.getKey());
    }
    
    @Test(expected = InvalidIdentifierException.class)
    public final void testGetInvalidVariable() throws Exception
    {
        // Create the class under test.
        final DefaultAdminService s = new DefaultAdminService(
                mockVariableDao(), mockRiskModelDao(), mockRuleDao());
        
        // Behavior verification.
        s.getVariable("Does not exist");
    }
    
    @Test
    public final void testUpdateVariable() throws Exception
    {
    	final String key = "dnr";
        final String origName = "DNR";
        final String newName = "Do Not R";
        final String newHelpText = "Help me!";
        
        // Create the class under test.
        final VariableDao mockDao = mockVariableDao();
        final DefaultAdminService s = new DefaultAdminService(
                mockDao, mockRiskModelDao(), mockRuleDao());
        
        // Setup
        final AbstractVariable var = s.getVariable(key);
        assertEquals(origName, var.getDisplayName());  // sanity check
        
        // Behavior verification.
        var.setDisplayName(newName);
        var.setHelpText(Optional.of(newHelpText));
        s.saveVariable(var);
        
        // Normally we try to verify the contract of a method instead of its
        // implementation, but without Hibernate it is impossible to verify
        // the contract here. Just verify that the service called update().
        verify(mockDao).mergeVariable(var);
    }
    
    @Test
    public final void getAllRules()
    {
        // Create the class under test.
        final DefaultAdminService s = new DefaultAdminService(
                mockVariableDao(), mockRiskModelDao(), mockRuleDao());
        // Behavior verification.
        assertEquals(fSampleRules, s.getAllRules());
    }
    
    @Test
    public final void getRule() throws Exception
    {
        final String ruleDisplayName = "Age multiplier for functional status";
        
        // Create the class under test.
        final DefaultAdminService s = new DefaultAdminService(mockVariableDao(),
                mockRiskModelDao(), mockRuleDao());
        
        // Behavior verification.
        final Rule actualRule = (Rule) s.getRule(ruleDisplayName);
        assertEquals(ruleDisplayName, actualRule.getDisplayName());
    }
    
    @Test(expected = InvalidIdentifierException.class)
    public final void getInvalidRule() throws Exception
    {
        // Create the class under test.
        final DefaultAdminService s = new DefaultAdminService(
                mockVariableDao(), mockRiskModelDao(), mockRuleDao());
        
        // Behavior verification.
        s.getRule("Does not exist");
    }
}
