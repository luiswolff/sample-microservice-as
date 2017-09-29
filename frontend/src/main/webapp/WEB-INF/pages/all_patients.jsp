<%--
  Created by IntelliJ IDEA.
  User: luis-
  Date: 05.06.2017
  Time: 23:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Patients</title>
</head>
<body>
    <table>
        <caption><h1>Patients in Register</h1></caption>
        <thead>
            <tr>
                <th>Identification</th>
                <th>Gender</th>
                <th>Birthday</th>
                <th>Count Diagnoses</th>
                <th>Count Medications</th>
            </tr>
        </thead>
        <tbody>
            <%--@elvariable id="model" type="javax.json.JsonArray"--%>
            <%--@elvariable id="patient" type="javax.json.JsonObject"--%>
            <c:forEach var="patient" items="${model}">
                <tr>
                    <td><a href="${pageContext.request.contextPath}/${patient.id}/edit.html">${patient.id}</a></td>
                    <td>${patient.getString('gender') == 'M' ? 'Male' : 'Female'}</td>
                    <fmt:parseDate value="${patient.getString('birthday')}" var="birthday" pattern="yyyy-MM-dd'T'hh:mm:ss" />
                    <td><fmt:formatDate value="${birthday}" pattern="yyyy" /></td>
                    <td><a href="${pageContext.request.contextPath}/${patient.id}/diagnoses.html"><fmt:formatNumber value="${patient.getInt('countDiagnoses')}" /></a></td>
                    <td><a href="${pageContext.request.contextPath}/${patient.id}/medications.html"><fmt:formatNumber value="${patient.getInt('countMedications')}" /></a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
<a href="${pageContext.request.contextPath}/createPatient.html">New Patient</a>
</body>
</html>
