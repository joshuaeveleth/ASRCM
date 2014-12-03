package gov.va.med.srcalc.web.view;

import java.io.IOException;

import gov.va.med.srcalc.domain.variable.Variable;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Tag handler for srcalc:variableInput custom tag.
 */
public class VariableInputTag extends SimpleTagSupport
{
    private Variable fVariable;
    
    @Override
    public void doTag() throws JspException, IOException
    {
        final JspWriter out = getJspContext().getOut();
        final InputGeneratorVisitor visitor = new InputGeneratorVisitor(out);
        try
        {
            fVariable.accept(visitor);
        }
        catch (final IOException e)
        {
            throw e;
        }
        catch (final Exception e)
        {
            throw new JspException("Unable to generate variableInput tag.", e);
        }
    }
    
    public void setVariable(final Variable variable)
    {
        fVariable = variable;
    }
}