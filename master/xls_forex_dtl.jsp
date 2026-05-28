<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>

<jsp:useBean class="com.etrm.fms.master.DB_Master_Report" id="mst_rpt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();


String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");

mst_rpt.setCallFlag("EXCH_DETAIL");
mst_rpt.setFrom_dt(from_dt);
mst_rpt.setTo_dt(to_dt);
mst_rpt.init();

Vector VEXCH_NM = mst_rpt.getVEXCH_NM();
Vector VEXCH_CD = mst_rpt.getVEXCH_CD();
Vector VEFF_DT = mst_rpt.getVEFF_DT();
Vector VMULTI_EXCH = mst_rpt.getVMULTI_EXCH();

Vector V1 =(Vector)((Vector)((Vector)VMULTI_EXCH.elementAt(0)));
Vector V2 =(Vector)((Vector)((Vector)VMULTI_EXCH.elementAt(1)));
Vector V3 =(Vector)((Vector)((Vector)VMULTI_EXCH.elementAt(2)));
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="4" rowspan="" align="center" style="font-size:30px;"><b>Foreign Exchange Report [<%=from_dt %> - <%=to_dt %>]</b></th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th rowspan="1" valign="middle">Date</th>
				<%for(int i=0;i<VEXCH_NM.size();i++){%>
				<th rowspan="1" valign="middle"><%=VEXCH_NM.elementAt(i) %></th>
				<%} %>
			</tr>
		</thead>
		<tbody>
        <%int K=0;
        for(int i=0;i<VEFF_DT.size();i++){ 
 	    K+=1; %> 
        	<tr>
				<td align="center"><%=VEFF_DT.elementAt(i) %></td>
				<td align="right"><%=V1.elementAt(i) %></td>
				<td align="right"><%=V2.elementAt(i) %></td>
				<td align="right"><%=V3.elementAt(i) %></td>
           </tr>
           <%}%>
    	</tbody> 
	</table>
</body>
</html>