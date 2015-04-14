package gov.va.med.srcalc.web.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import gov.va.med.srcalc.domain.workflow.CalculationWorkflow;
import gov.va.med.srcalc.domain.workflow.NewCalculation;
import gov.va.med.srcalc.domain.workflow.SelectedCalculation;
import gov.va.med.srcalc.service.CalculationService;
import gov.va.med.srcalc.service.InvalidIdentifierException;
import gov.va.med.srcalc.web.view.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for creating a new Calculation.
 */
@Controller
public class CalculationController
{
    private static String SESSION_CALCULATION = "srcalc_calculation";
    
    private final CalculationService fCalculationService;
    
    @Inject
    public CalculationController(final CalculationService calculationService)
    {
        fCalculationService = calculationService;
    }

    @RequestMapping(value = "/newCalc", method = RequestMethod.GET)
    public ModelAndView startNewCalculation(
            @RequestParam(value = "patientDfn") final int patientDfn,
            final HttpSession session)
    {
        // Start the calculation. A Calculation object must be created here to
        // store the start time for reporting.
        final NewCalculation newCalc = fCalculationService.startNewCalculation(patientDfn);

        // Store the calculation in the HTTP Session.
        session.setAttribute(SESSION_CALCULATION, newCalc);
        
        // Present the view.
        final ModelAndView mav = new ModelAndView(Views.SELECT_SPECIALTY);
        mav.addObject("calculation", newCalc.getCalculation());
        mav.addObject("specialties", newCalc.getPossibleSpecialties());
        return mav;
    }
    
    @RequestMapping(value = "/selectSpecialty", method = RequestMethod.POST)
    public String setSpecialty(
            final HttpSession session,
            @RequestParam("specialty") final String specialtyName)
                    throws InvalidIdentifierException
    {
        final CalculationWorkflow workflow = CalculationWorkflowSupplier.getWorkflowFromSession(session);
        final SelectedCalculation selCalc =
                fCalculationService.setSpecialty(workflow.getCalculation(), specialtyName);
        session.setAttribute(SESSION_CALCULATION, selCalc);

        // Using the POST-redirect-GET pattern.
        return "redirect:/enterVars";
    }
    
    // Variable entry is in EnterVariablesController.
    
}
