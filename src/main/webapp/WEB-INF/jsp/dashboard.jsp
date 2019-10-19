<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
           <%@ page import ="com.cloud.filoft.model.File" %>
       <%@ page import ="java.util.ArrayList"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Dashboard</title>
</head>
<body>
<h2>Welcome ${name}</h2>
<div>
<% 
 if(session.getAttribute("userFiles") != null)
 {
	 System.out.println("not null");
	 ArrayList<File> filesArray = (ArrayList<File>)session.getAttribute("userFiles"); 
 %>
 <table class="dashboardtable">
					<thead class="dthead">
					<tr>
						<th>File name</th>
						<th>Description</th>
						<th>File size</th>
						<th>Created Time</th>
						<th>Updated Time</th>
						<th>Download</th>
						<th>Update</th>
						<th>Delete</th>
									</tr>
					</thead>

<tbody class="dtr">
<% 
for(File file : filesArray) 
{ %>
<tr>
	<td><%out.println(file.getFileName()); %></td>
	<td><%out.println(file.getDescription()); %></td>
	<td><%out.println(file.getFileSize()); %></td>
	<td><%out.println(file.getCreatedTime()); %></td>
	<td><%out.println(file.getUpdatedTime()); %></td>

<td><button><a href="<%=file.getFileUrl()%>"><span class="glyphicon glyphicon-cloud-download"></span></a></button></td></tr>
	<td>Update</td>
	<form action="/delete" method="POST">
	<input type="hidden" name="emailid" value="<%=file.getEmailId() %>">
		<input type="hidden" name="filename" value="<%=file.getFileName() %>">
<td>	<button ><span class="glyphicon glyphicon-trash" type="submit"></span></button></td>
	</form>


<% } %>										
</tbody>

 
</table>
	 
 
 <% }
 else
 { %>
	 <div class="nofiles">
	<%  out.println("No Files"); %>
	 </div>
 <%   } %>
</div>

</body>
</html>