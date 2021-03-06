<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="srcalc" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<srcalc:adminPage title="Update Procedure Set">
    <h2>Update Procedure Set</h2>
    <p>There are currently ${fn:length(procedures)} procedures in the database.</p>
    
    <h3>Upload a New Procedure Set</h3>
    <c:if test="${uploadSuccess}">
    <p class="successMessage">The new procedure set has been saved to the database.</p>
    </c:if>
    <c:if test="${!empty validationErrors}">
    <div class="error">
    <p>The uploaded procedure set was not valid. The database has not been modified.</p>
    <p>Errors were:</p>
    <ul>
        <c:forEach var="error" items="${validationErrors}">
        <li>${error.locationPrefix}<spring:message message="${error}" /></li>
        </c:forEach>
    </ul>
    </div>
    </c:if>
    <p>You may upload a new procedure set from a CSV file. As long as the CSV file is valid,
    it will immediately replace the above procedures in the database.</p>
    <c:url var="sampleUploadUrl" value="/admin/resources/sample_procedures_upload.csv" />
    <p><a href="${sampleUploadUrl}">Here</a> is a sample CSV file.</p>
    <form id="procedureUploadForm" method="post" enctype="multipart/form-data">
    <input type="file" name="newProceduresFile" accept=".csv">
    <button id="replaceProceduresButton" type="submit">Replace all Procedures</button>
    </form>
    
    <c:url var="adminHomeUrl" value="${srcalcUrls.modelAdminHome}" />
    <p><a href="${adminHomeUrl}" class="btn-link">Return Home</a></p>
    <script>
    $(document).ready(function() {
        $('#procedureUploadForm').on('submit', function (event) {
            var button = $('#replaceProceduresButton');
            button.html('This may take a moment...');
            button.attr('disabled', 'disabled');
        });
    })
    </script>
</srcalc:adminPage>
