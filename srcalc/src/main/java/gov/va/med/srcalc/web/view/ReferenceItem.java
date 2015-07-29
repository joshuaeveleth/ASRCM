package gov.va.med.srcalc.web.view;

import java.util.List;

import com.github.rjeschke.txtmark.Processor;
import com.google.common.base.Optional;

import gov.va.med.srcalc.domain.model.VariableGroup;

/**
 * This class represents reference information that will be automatically retrieved from sources
 * such as VistA instead of from the database.
 */
public class ReferenceItem implements DisplayItem
{
    private static final String DISPLAY_FRAGMENT = "referenceItem.jsp";
    
    private final String fDisplayName;
    private final VariableGroup fGroup;
    private final Optional<String> fHelpText;
    private final List<String> fReferenceInfo;
    
    /**
     * Constructs an instance using the designated information.
     * 
     * @param displayName
     *            the display name of the reference information
     * @param group
     *            the VariableGroup that this reference information belongs to
     * @param referenceInfo
     *            the reference information to show to the user
     */
    public ReferenceItem(final String displayName, final VariableGroup group,
            final List<String> referenceInfo)
    {
        fDisplayName = displayName;
        fGroup = group;
        fHelpText = Optional.of("");
        fReferenceInfo = referenceInfo;
    }
    
    @Override
    public String getDisplayName()
    {
        return fDisplayName;
    }
    
    @Override
    public VariableGroup getDisplayGroup()
    {
        return fGroup;
    }
    
    @Override
    public String getFragmentName()
    {
        return DISPLAY_FRAGMENT;
    }
    
    @Override
    public Optional<String> getHelpText()
    {
        return fHelpText;
    }
    
    @Override
    public String getHelpTextAsHtml()
    {
        return Processor.process(fHelpText.or(""));
    }
    
    /**
     * Returns the reference information in a List so that formatting later is easier. For instance,
     * each List item could be a paragraph element or an unordered item on a page.
     */
    public List<String> getReferenceInfo()
    {
        return fReferenceInfo;
    }
    
    @Override
    public String toString()
    {
        return fDisplayName;
    }
}