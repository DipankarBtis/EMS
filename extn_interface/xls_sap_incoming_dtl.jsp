
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>

<jsp:useBean class="com.etrm.fms.extn_interface.DataBean_sap_interface" id="dbmgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String sysdt = utildate.getSysdate();
String from_dt=request.getParameter("from_dt")==null?sysdt:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdt:request.getParameter("to_dt");
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

dbmgmt.setCallFlag("SAP_INCOMING_DTL");
dbmgmt.setComp_cd(owner_cd);
dbmgmt.setFrom_dt(from_dt);
dbmgmt.setTo_dt(to_dt);
dbmgmt.init();


Vector VBUKRS = dbmgmt.getVBUKRS();
Vector VKUNNR = dbmgmt.getVKUNNR();
Vector VBELNR = dbmgmt.getVBELNR();
Vector VGJAHR = dbmgmt.getVGJAHR();
Vector VBLDAT = dbmgmt.getVBLDAT();
Vector VSGTXT = dbmgmt.getVSGTXT();
Vector VSHKZG = dbmgmt.getVSHKZG();
Vector VWRBTR = dbmgmt.getVWRBTR();
Vector VWAERS = dbmgmt.getVWAERS();
Vector VDMBTR = dbmgmt.getVDMBTR();
Vector VHWAER = dbmgmt.getVHWAER();
Vector VZUKEY = dbmgmt.getVZUKEY();
Vector VZDTYP = dbmgmt.getVZDTYP();
Vector VHKONT = dbmgmt.getVHKONT();
Vector VAUGBL = dbmgmt.getVAUGBL();
Vector VXBLNR = dbmgmt.getVXBLNR();
Vector VRSTGR = dbmgmt.getVRSTGR();
Vector VAUGDT = dbmgmt.getVAUGDT();
Vector VBLART = dbmgmt.getVBLART();
Vector VZUONR = dbmgmt.getVZUONR();
Vector VNAME = dbmgmt.getVNAME();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="22" rowspan="2" align="left">SAP Incoming Details (<%=from_dt%> - <%=to_dt%>)</th>
		</tr>
	</table>
	<br>
	<table width="100%" border="1">
		<thead id="tbsearch">
			<tr>
				<th align="center">Sr#</th>
				<th align="center">Company code <br>(BUKRS)</th><!-- BUKRS -->
				<th align="center">Customer code<br>(KUNNR)</th><!-- KUNNR -->
				<th align="center">Customer Name<br>(NAME)</th><!-- NAME -->
				<th align="center">Document Number<br>(BELNR)</th><!-- BELNR -->
				<th align="center">Fiscal Year<br>(GJAHR)</th><!-- GJAHR -->
				<th align="center">Document Date<br>(BLDAT)</th><!-- BLDAT -->
				<th align="center">Text<br>(SGTXT)</th><!-- SGTXT -->
				<th align="center">Debit/Credit<br>(SHKZG)</th><!-- SHKZG -->
				<th align="center">Amount<br>(WRBTR)</th><!-- WRBTR -->
				<th align="center">Currency<br>(WAERS)</th><!-- WAERS -->
				<th align="center">Amount in Local Currency<br>(DMBTR)</th><!-- DMBTR -->
				<th align="center">Local Currency<br>(HWAER)</th><!-- HWAER -->
				<th align="center">Unique Character combination<br>(ZUKEY)</th><!-- ZUKEY -->
				<th align="center">Type of Transaction<br>(ZDTYP)</th><!-- ZDTYP -->
				<th align="center">General Ledger<br>(HKONT)</th><!-- HKONT -->
				<th align="center">Clearing Document<br>(AUGBL)</th><!-- AUGBL -->
				<th align="center">Reference<br>(XBLNR)</th><!-- XBLNR -->
				<th align="center">Reason Code<br>(RSTGR)</th><!-- RSTGR -->
				<th align="center">Clearing Document Date<br>(AUGDT)</th><!-- AUGDT -->
				<th align="center">Document Type<br>(BLART)</th><!-- BLART  -->
				<th align="center">Assignment number<br>(ZUONR)</th><!-- ZUONR -->
			</tr>
		</thead>
		<tbody>
			<%if(VBUKRS.size()>0){ %>
			<%for(int i=0;i<VBUKRS.size();i++){ %>
			<tr>
				<td align="center"><%=i+1%></td>
				<td align="center"><%=VBUKRS.elementAt(i)%></td>
				<td align="center"><%=VKUNNR.elementAt(i)%></td>
				<td align="center"><%=VNAME.elementAt(i)%></td>
				<td align="center"><%=VBELNR.elementAt(i)%></td>
				<td align="center"><%=VGJAHR.elementAt(i)%></td>
				<td align="center"><%=VBLDAT.elementAt(i)%></td>
				<td align="center"><%=VSGTXT.elementAt(i)%></td>
				<td align="center"><%=VSHKZG.elementAt(i)%></td>
				<td align="right"><%=VWRBTR.elementAt(i)%></td>
				<td align="center"><%=VWAERS.elementAt(i)%></td>
				<td align="right"><%=VDMBTR.elementAt(i)%></td>
				<td align="center"><%=VHWAER.elementAt(i)%></td>
				<td align="center"><%=VZUKEY.elementAt(i)%></td>
				<td align="center"><%=VZDTYP.elementAt(i)%></td>
				<td align="center"><%=VHKONT.elementAt(i)%></td>
				<td align="center"><%=VAUGBL.elementAt(i)%></td>
				<td align="center"><%=VXBLNR.elementAt(i)%></td>
				<td align="center"><%=VRSTGR.elementAt(i)%></td>
				<td align="center"><%=VAUGDT.elementAt(i)%></td>
				<td align="center"><%=VBLART.elementAt(i)%></td>
				<td align="center"><%=VZUONR.elementAt(i)%></td>
			</tr>
			<%} %>
			<%}else{ %>
			<tr>
				<td colspan="22" align="center"><%=utilmsg.infoMessage("<b>No SAP incoming deatils Available!</b>") %></td>
			</tr>
			<%} %>
		</tbody>
	</table>
</body>
</html>