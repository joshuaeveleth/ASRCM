package gov.va.med.srcalc.web.controller.admin;

import gov.va.med.srcalc.domain.model.AbstractVariable;
import gov.va.med.srcalc.domain.model.RiskModel;
import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.web.SrcalcUrls;
import gov.va.med.srcalc.web.view.VariableSummary;
import gov.va.med.srcalc.web.view.Views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Web MVC controller for the Administration home page.
 */
@Controller
@RequestMapping(SrcalcUrls.ADMIN_HOME)
public class AdminHomeController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminHomeController.class);

    private final AdminService fAdminService;
    
    /**
     * Constructs an instance that will use the provided service(s) for operations.
     */
    @Inject
    public AdminHomeController(final AdminService adminService)
    {
        fAdminService = adminService;
    }
    
    /**
     * Presents the Administrator Login Form.
     */
    @RequestMapping(value = SrcalcUrls.ADMIN_LOGIN_FORM_SUFFIX, method = RequestMethod.GET)
    public String loginForm()
    {
        return Views.ADMIN_LOGIN_FORM;
    }

    /**
     * Presents the Administration Home Page.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String defaultPage()
    {
        return Views.ADMIN_HOME;
    }
    
    /**
     * Presents the Model Administration Home Page.
     */
    @RequestMapping(value = SrcalcUrls.MODEL_ADMIN_HOME_SUFFIX, method = RequestMethod.GET)
    public ModelAndView modelHome()
    {
        List<RiskModel> riskModels = new ArrayList<RiskModel>( fAdminService.getAllRiskModels() );
        Collections.sort( riskModels );
        LOGGER.debug( "There are {} Risk Models in the DB.", riskModels.size());

        // Transform the List of all Variables into a List of VariableSummaries.
        final List<AbstractVariable> variables = fAdminService.getAllVariables();
        final ArrayList<VariableSummary> summaries = new ArrayList<>(variables.size());
        for (final AbstractVariable var : variables)
        {
            summaries.add(VariableSummary.fromVariable(var));
        }

        return new ModelAndView(Views.MODEL_ADMIN_HOME)
                .addObject("variables", summaries)
                .addObject("rules", fAdminService.getAllRules())
                .addObject("riskModels", riskModels );
    }

}
