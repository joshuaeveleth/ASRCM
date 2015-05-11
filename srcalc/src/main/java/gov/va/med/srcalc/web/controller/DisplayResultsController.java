package gov.va.med.srcalc.web.controller;

import java.util.HashMap;

import gov.va.med.srcalc.domain.calculation.Calculation;
import gov.va.med.srcalc.domain.calculation.CalculationResult;
import gov.va.med.srcalc.service.CalculationService;
import gov.va.med.srcalc.web.view.Views;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DisplayResultsController
{
	private static final Logger fLogger = LoggerFactory.getLogger(DisplayResultsController.class);
	private final CalculationService fCalculationService;
    
    @Inject
    public DisplayResultsController(final CalculationService calculationService)
    {
        fCalculationService = calculationService;
    }
    
    @RequestMapping(value = "/displayResults", method = RequestMethod.GET)
    public String displayResults(
            final HttpSession session,
            final Model model)
    {
        // Get the current Calculation from the session.
        final Calculation calculation = SrcalcSession.getCalculation(session);
        model.addAttribute("calculation", calculation);

        // And get the current CalculationResult from the session.
        model.addAttribute("result", SrcalcSession.getRequiredLastResult(session));
        
        return Views.DISPLAY_RESULTS;
    }
    
    @RequestMapping(
    		value="/signCalculation",
    		method = RequestMethod.POST,
            produces = "application/json")
    @ResponseBody
    public HashMap<String, String> signCalculation(final HttpSession session, 
    		@RequestParam(value = "eSig") final String electronicSignature,
    		final Model model)
    {
    	// Build the note body and submit the RPC
    	String resultString;
    	try
    	{
    	    final CalculationResult lastResult = SrcalcSession.getRequiredLastResult(session);
            resultString = fCalculationService.saveRiskCalculationNote(
                    lastResult, electronicSignature).getDescription();
    	}
    	catch(final RecoverableDataAccessException e)
    	{
    		fLogger.warn("There was a problem connecting to VistA", e);
    		resultString = "There was a problem connecting to VistA.";
    	}
    	// The json could be expanded to return more information/fields
        final HashMap<String, String> jsonStatus = new HashMap<>();
        jsonStatus.put("status", resultString);
    	// Return the result that will be translated to a json response
    	return jsonStatus;
    }
    
    @RequestMapping(value="/successfulSign", method = RequestMethod.GET)
    public String displaySuccess(final HttpSession session, final Model model)
    {
    	return Views.SUCCESSFUL_SIGN;
    }
}
