package gov.va.med.srcalc.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import gov.va.med.srcalc.domain.variable.Variable;
import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.web.view.Tile;
import gov.va.med.srcalc.web.view.VariableSummary;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Web MVC controller for risk model administration.
 */
@Controller
public class ModelAdminController
{
    private final AdminService fAdminService;
    
    @Inject
    public ModelAdminController(final AdminService adminService)
    {
        fAdminService = adminService;
    }

    @RequestMapping(value = "/admin/models", method = RequestMethod.GET)
    public String defaultPage(final Model model)
    {
        // Transform the List of all Variables into a List of VariableSummaries.
        final List<Variable> variables = fAdminService.getAllVariables();
        final ArrayList<VariableSummary> summaries = new ArrayList<>(variables.size());
        for (final Variable var : variables)
        {
            summaries.add(VariableSummary.fromVariable(var));
        }
        model.addAttribute("variables", summaries);

        return Tile.MODEL_ADMIN_HOME;
    }
}
