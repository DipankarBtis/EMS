<%
String expire_msg = request.getParameter("expire_msg")==null?"":request.getParameter("expire_msg");
if(session.getAttribute("emp_uid")==null || session.getAttribute("emp_uid") == ""|| session.getAttribute("emp_uid").toString().equals("null"))
{%>
		<script>
			alert("Please Enter Login Detail!")
			var url = "../home/login.jsp";
			location.replace(url);
		</script>
<%}
else
{
	if(expire_msg.equals("Timeout"))
	{
		session.invalidate();
	%>
		<script>
		var url = "../home/login.jsp?sess=Session Expired";
		location.replace(url);
		</script>
	<%
	}
	else if(expire_msg.equals("PwdChng"))
	{
		session.invalidate();
	%>
		<script>
		var url = "../home/login.jsp";
		location.replace(url);
		</script>
	<%}
}
%>

