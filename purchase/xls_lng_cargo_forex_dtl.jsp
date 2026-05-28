<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>

<jsp:useBean class="com.etrm.fms.purchase.DB_PurchaseReports" id="purchase" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();

String owner_cd = request.getParameter("owner_cd")==null?"":request.getParameter("owner_cd");
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");

purchase.setCallFlag("LNG_CARGO_DTL");
purchase.setComp_cd(owner_cd);
purchase.setFrom_dt(from_dt);
purchase.setTo_dt(to_dt);
purchase.init();

Vector VCOUNTERPTY_CD = purchase.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_NM = purchase.getVCOUNTERPTY_NM();
Vector VCOUNTERPTY_ABBR = purchase.getVCOUNTERPTY_ABBR();

Vector VAGMT_NO = purchase.getVAGMT_NO();
Vector VAGMT_REV_NO = purchase.getVAGMT_REV_NO();
Vector VCONT_NO = purchase.getVCONT_NO();
Vector VCONT_REV_NO = purchase.getVCONT_REV_NO();
Vector VCONTRACT_TYPE = purchase.getVCONTRACT_TYPE();
Vector VDEAL_NO = purchase.getVDEAL_NO();
Vector VINVOICE_NO = purchase.getVINVOICE_NO();
Vector VALLOC_QTY = purchase.getVALLOC_QTY();
Vector VSALES_PRICE = purchase.getVSALES_PRICE();
Vector VEXCHNAGE_RATE = purchase.getVEXCHNAGE_RATE();
Vector VEXCHNAGE_RATE_DATE = purchase.getVEXCHNAGE_RATE_DATE();

Vector VARRIVAL_DT = purchase.getVARRIVAL_DT();
Vector VSHIP_NM = purchase.getVSHIP_NM();
Vector VEXCH_1 = purchase.getVEXCH_1();
Vector VEXCH_2 = purchase.getVEXCH_2();
Vector VEXCH_3 = purchase.getVEXCH_3();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="11" align="center" style="font-size:26px;"><b>LNG Cargo Forex Details [<%=from_dt %> - <%=to_dt %>]</b></th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th>Cargo Arrival Date</th>
				<th>Cargo Name</th>
				<th>Name of LNG Supplier</th>
				<th>Invoice No#</th>
				<th>LNG Quantity(MMBTU)</th>
				<th>LNG Price(USD/MMBTU)</th>
				<th>Exchange Rate</th>
				<th>Exchange Rate Date</th>
				<th>SBI FIRST RATE TT BUY</th>
				<th>SBI FIRST RATE TT SELL</th>
				<th>SBI FIRST RATE TT BUY SELL</th>
			</tr>
		</thead>
		<tbody>
		<%if(VCOUNTERPTY_CD.size()>0){ %>
		<%int K=0;%>
		<% for(int i=0;i<VCOUNTERPTY_CD.size();i++){ 
		K+=1; %>
			<tr>
				<td align="center"><%=VARRIVAL_DT.elementAt(i) %></td>
				<td align="center"><%=VSHIP_NM.elementAt(i) %></td>
				<td align="center"><%=VCOUNTERPTY_NM.elementAt(i) %></td>
				<td align="center" title="<%=VDEAL_NO.elementAt(i)%>"><%=VINVOICE_NO.elementAt(i) %></td>
				<td align="right"><%=VALLOC_QTY.elementAt(i) %></td>
				<td align="right"><%=VSALES_PRICE.elementAt(i) %></td>
				<td align="right"><%=VEXCHNAGE_RATE.elementAt(i) %></td>
				<td align="center"><%=VEXCHNAGE_RATE_DATE.elementAt(i) %></td>
				<td align="right"><%=VEXCH_1.elementAt(i) %></td>
				<td align="right"><%=VEXCH_2.elementAt(i) %></td>
				<td align="right"><%=VEXCH_3.elementAt(i) %></td>
			</tr>
		<%}%>
		<%} else{ %>
			<tr>
				<td colspan = "11" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated!</b>") %></td>
			</tr>
		<%} %>
		</tbody>
	</table>
</body>
</html>