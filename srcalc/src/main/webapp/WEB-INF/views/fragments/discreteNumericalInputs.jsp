<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>

<%-- This file cannot be a jspf extension because it is not included statically. --%>
<%-- This jsp expects for displayItemParam and variableEntryParam to be defined. --%>
<!-- Wrap both the radio button and numerical entry in a span.radioLabel
     for proper spacing. -->
<span class="radioLabel"><form:radiobutton path="${displayItemParam.varPath}" cssClass="numericalRadio" value="numerical"/>
<c:set var="numericalVarPath" value="${displayItemParam.numericalVarPath}" />
<form:input cssClass="numerical" path="${numericalVarPath}" size="6"/>
<c:out value="${displayItemParam.units} ${variableEntryParam.getNumericalMeasureDate(displayItemParam.key)}"/></span>
<form:errors path="${numericalVarPath}" cssClass="error" /><br>
<c:forEach var="cat" items="${displayItemParam.categories}">
<label class="radioLabel"><form:radiobutton path="${displayItemParam.varPath}" value="${cat.option.value}"/>
    Presumed <c:out value="${cat.option.value}"/></label>
</c:forEach>
<!-- Display any errors immediately following the input control. -->
<form:errors path="${displayItemParam.varPath}" cssClass="error" />