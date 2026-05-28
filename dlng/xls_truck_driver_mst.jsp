<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_DLNG_Master" id="dlngmaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.dlng.UtilBean_DLNG" id="utilBean_dlng" scope="request"></jsp:useBean>
<%
String prevdate = utildate.getPreviousDate();
String sysdate=utildate.getSysdate();

String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");
String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

dlngmaster.setCallFlag("TRUCK_DRIVER_MST");
dlngmaster.init();

Vector VDRIVER_CD = dlngmaster.getVDRIVER_CD();
Vector VDRIVER_NAME = dlngmaster.getVDRIVER_NAME();
Vector VSTATUS = dlngmaster.getVDRIVER_STATUS();
Vector VDRIVER_ADDR = dlngmaster.getVDRIVER_ADDR();
Vector VDRIVER_DOB = dlngmaster.getVDRIVER_DOB();
Vector VDRIVER_MOBILE = dlngmaster.getVDRIVER_MOBILE();
Vector VLICENSE_NO = dlngmaster.getVLICENSE_NO();
Vector VLICENSE_TYPE = dlngmaster.getVLICENSE_TYPE();
Vector VLICENSE_FROM_DT = dlngmaster.getVLICENSE_FROM_DT();
Vector VLICENSE_TO_DT = dlngmaster.getVLICENSE_TO_DT();
Vector VLICENSE_ISSUE_STATE_NAME = dlngmaster.getVLICENSE_ISSUE_STATE_NAME();
Vector VLICENSE_ISSUE_STATE_CD = dlngmaster.getVLICENSE_ISSUE_STATE_CD();
Vector VLICENSE_FILE_NAME = dlngmaster.getVLICENSE_FILE_NAME();
Vector VSTATE_CD = dlngmaster.getVSTATE_CD();
Vector VSTATE_NM = dlngmaster.getVSTATE_NM();
Vector VTRUCK_TRANS_CD = dlngmaster.getVTRUCK_TRANS_CD();
Vector VTRUCK_TRANS_NAME = dlngmaster.getVTRUCK_TRANS_NAME();
Vector VTRUCK_TRANS_ABBR = dlngmaster.getVTRUCK_TRANS_ABBR();
Vector VLINKED_TRUCK_TRANS_CD = dlngmaster.getVLINKEDTRUCKTRANS();
Vector VLINKED_TRUCK_TRANS_NAME = dlngmaster.getVLINKEDTRUCKTRANSNAME();
Vector VLINKED_EFF_DT = dlngmaster.getVLINKEDEFFDT();
Vector VLINKED_FLAG = dlngmaster.getVLINKEDFLAG();
Vector VALLTRUCK_TRANS_CD = dlngmaster.getVALLTRUCK_TRANS_CD();
Vector VALLTRUCK_TRANS_NAME = dlngmaster.getVALLTRUCK_TRANS_NAME();
Vector VEFF_DT = dlngmaster.getVDRIVER_EFF_DT();

%>
<body onload="">
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>

	<table width="100%" border="1">
		<tr>
			<th colspan="13" rowspan="2" align="left">Truck Driver Report</th>
		</tr>
	</table>
	<table  width="100%" border="1">
		<thead>
			<tr>
				<th></th>
				<th>Driver Name</th>
				<th>Driver DOB</th>
				<th>Address</th>
		    	<th>Driver Mobile No</th>			    		
		    	<th>Effective Date</th>			    		
		    	<th>License No</th>
		    	<th>License Type</th>
		    	<th>License Period</th>
		    	<th>License Issue State</th>
		    	<th>Status</th>
		    	<th>Linked Transporter</th>
		    	<th>License File</th>
			</tr>
		</thead>
		<tbody id="mainTbody">
		<%int j=0;int k=0;
		if(VDRIVER_CD.size()>0){%>
			<%for(int i=0; i<VDRIVER_CD.size(); i++){ 
			k+=1;%>
				<tr>
					<td align="center"><%=k %></td>
					<td align="center"><%=VDRIVER_NAME.elementAt(i)%></td>
					<td align="center"><%=VDRIVER_DOB.elementAt(i)%></td>
					<td align="center"><%=VDRIVER_ADDR.elementAt(i)%></td>
					<td align="center"><%=VDRIVER_MOBILE.elementAt(i)%></td>
					<td align="center"><%=VEFF_DT.elementAt(i)%></td>
					<td align="center"><%=VLICENSE_NO.elementAt(i)%></td>
					<td align="center"><%=VLICENSE_TYPE.elementAt(i) %></td>
					<td align="center"><%=VLICENSE_FROM_DT.elementAt(i) %>-<%=VLICENSE_TO_DT.elementAt(i) %></td>
					<td align="center"><%=VLICENSE_ISSUE_STATE_NAME.elementAt(i) %></td>
					<td align="center">
					<%if(VSTATUS.elementAt(i).equals("Y")){%>
						Active
					<%}else{ %>
					 	Inactive
					<%} %>
					<td align="center"><%=VLINKED_TRUCK_TRANS_NAME.elementAt(i) %></td>
					<td align="center"><%=VLICENSE_FILE_NAME.elementAt(i)%></td>
				</tr>
			<%} %>
		<%}else{ %>
			<tr>
				<td colspan="9" align="center"><%=utilmsg.infoMessage("<b>No Truck Driver Data Available!</b>") %></td>
			</tr>
		<%} %>
		</tbody>
	</table>
</body>
</html>