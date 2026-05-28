<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.other_invoice.DB_Other_Invoice_Report" id="oth_rpt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

String sysdate = utildate.getSysdate();
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");
String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");

if(month.length() == 1)
{
	month="0"+month; 
}

oth_rpt.setCallFlag("OTH_INV_SUMMARY");
oth_rpt.setComp_cd(owner_cd);
//oth_rpt.setInvoice_type(invoice_type);
oth_rpt.setMonth(month);
oth_rpt.setYear(year);
oth_rpt.init();

Vector VSEGMENT_NM = oth_rpt.getVSEGMENT_NM();
Vector VSEGMENT_TYPE = oth_rpt.getVSEGMENT_TYPE();
Vector VDISP_SEGMENT_NM = oth_rpt.getVDISP_SEGMENT_NM();
Vector VDISP_SEGMENT_TYPE = oth_rpt.getVDISP_SEGMENT_TYPE();

Vector VINVOICE_TYPE = oth_rpt.getVINVOICE_TYPE();
Vector VVENDOR_CD = oth_rpt.getVVENDOR_CD();
Vector VVENDOR_NM = oth_rpt.getVVENDOR_NM();
Vector VINVOICE_NO = oth_rpt.getVINVOICE_NO();
Vector VINVOICE_DT = oth_rpt.getVINVOICE_DT();
Vector VGROSS_AMT_USD = oth_rpt.getVGROSS_AMT_USD();
Vector VGROSS_AMT_INR = oth_rpt.getVGROSS_AMT_INR();
Vector VTAX_AMT_USD = oth_rpt.getVTAX_AMT_USD();
Vector VTAX_AMT_INR = oth_rpt.getVTAX_AMT_INR();
Vector VNET_PAY_USD = oth_rpt.getVNET_PAY_USD();
Vector VNET_PAY_INR = oth_rpt.getVNET_PAY_INR();
Vector VCHECKED_FLAG = oth_rpt.getVCHECKED_FLAG();
Vector VCHECKED_BY = oth_rpt.getVCHECKED_BY();
Vector VCHECKED_DT = oth_rpt.getVCHECKED_DT();
Vector VAPPROVED_FLAG = oth_rpt.getVAPPROVED_FLAG();
Vector VAPPROVED_BY = oth_rpt.getVAPPROVED_BY();
Vector VAPPROVED_DT = oth_rpt.getVAPPROVED_DT();
Vector VINV_FLAG = oth_rpt.getVINV_FLAG();
Vector VCRITERIA = oth_rpt.getVCRITERIA();

Vector VINDEX = oth_rpt.getVINDEX();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table  width="100%" border="1">
		<tr>
			<th colspan="13" rowspan="2" align="left">Other Invoice Summary</th>
		</tr>
	</table>
	<div class="row">
			<div class="col-sm-12 col-xs-12 col-md-12">
				&nbsp;
			</div>
	</div> 
	<%
		int i=0,j=0,ctn=0,l=0,k=0;
		for(l=0;l<VDISP_SEGMENT_TYPE.size();l++){
			int index = Integer.parseInt(""+VINDEX.elementAt(l));
	%>
		<table  width="100%" border="1">
			<tr>
				<td colspan="13" align="left"><b><%=VDISP_SEGMENT_NM.elementAt(l)%></b></td>
			</tr>
		</table>
		<table width="100%" border="1">
			<thead>
				<tr>
					<th rowspan="2">Sr#</th>
					<th rowspan="2">Invoice Type</th>
					<th rowspan="2">Customer</th>
					<th rowspan="2">Invoice No</th>
					<th rowspan="2">Invoice Date</th>
					<th colspan="3">INR Details</th>
					<th colspan="3">USD Details</th>
					<th rowspan="2">Checked</th>
					<th rowspan="2">Approved</th>
				</tr>
				<tr>
					<th>Gross Amount</th>
					<th>Tax Amount</th>
					<th>Net Amount</th>
					<th>Gross Amount</th>
					<th>Tax Amount</th>
					<th>Net Amount</th>
				</tr>
			</thead>
			<tbody>
				<%k=0;
					if(index > 0){ %>
						<%for(i=i;i<VVENDOR_CD.size();i++){
							k+=1;
						%>
							<tr>
								<td align="center"><%=k%></td>
								<td align="center"><%=VINVOICE_TYPE.elementAt(i)%> <%if(VINV_FLAG.elementAt(i).equals("CR")||VINV_FLAG.elementAt(i).equals("DR")){%>[<%=VINV_FLAG.elementAt(i)%>]<%} %></td>
								<td align="center"><%=VVENDOR_NM.elementAt(i)%></td>
								<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
								<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
								<td align="right"><%=VGROSS_AMT_INR.elementAt(i)%></td>
								<td align="right"><%=VTAX_AMT_INR.elementAt(i)%></td>
								<td align="right"><%=VNET_PAY_INR.elementAt(i)%></td>
								<td align="right"><%=VGROSS_AMT_USD.elementAt(i)%></td>
								<td align="right"><%=VTAX_AMT_USD.elementAt(i)%></td>
								<td align="right"><%=VNET_PAY_USD.elementAt(i)%></td>
								<td align="center"><%=VCHECKED_BY.elementAt(i)%> <%=VCHECKED_DT.elementAt(i) %></td>
								<td align="center"><%=VAPPROVED_BY.elementAt(i)%> <%=VAPPROVED_DT.elementAt(i) %></td>
							</tr>
							<%if(k==index)
							{%>
								<tr>
									<td colspan="13" align="center"></td>
								</tr>
								<%i+=1;
								break;
							}
							%>
						<%} %>
				<%}else{%>
					<tr>
						<td colspan="13" align="center"><%=utilmsg.infoMessage("<b>No "+ VDISP_SEGMENT_NM.elementAt(l)+" Invoice is Generated for Selected Period!</b>") %></td>
					</tr>
				<%} %>
			</tbody>
		</table>
	<%}%>
</body>
</html>