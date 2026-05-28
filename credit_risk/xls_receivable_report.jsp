<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.credit_risk.DB_CR_ReceivableReport" id="dbcredit" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String previousDate=utildate.getPreviousDate();
String rpt_dt = request.getParameter("rpt_dt")==null?previousDate:request.getParameter("rpt_dt");
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

dbcredit.setCallFlag("RECEIVABLE_REPORT");
dbcredit.setComp_cd(owner_cd);
//dbcredit.setRpt_dt(rpt_dt);
dbcredit.init();

Vector VCOUNTERPARTY_NM = dbcredit.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dbcredit.getVCOUNTERPARTY_ABBR();
Vector VCOLL_CATEGORY = dbcredit.getVCOLL_CATEGORY();
Vector VCATEGORY = dbcredit.getVCATEGORY();
Vector VBUSINESS = dbcredit.getVBUSINESS();
Vector VLEGAL_ENTITY = dbcredit.getVLEGAL_ENTITY();
Vector VDOC_NO = dbcredit.getVDOC_NO();
Vector VINVOICE_NO = dbcredit.getVINVOICE_NO();
Vector VREF_K1 = dbcredit.getVREF_K1();
Vector VREF_K2 = dbcredit.getVREF_K2();
Vector VREF_K3 = dbcredit.getVREF_K3();
Vector VDEAL_ASSIGNMENT = dbcredit.getVDEAL_ASSIGNMENT();
Vector VCONT_TYPE = dbcredit.getVCONT_TYPE();
Vector VTEXT = dbcredit.getVTEXT();
Vector VBA = dbcredit.getVBA();
Vector VNET_DUE_DT = dbcredit.getVNET_DUE_DT();
Vector VAMT_DC = dbcredit.getVAMT_DC();
Vector VAMT_INR = dbcredit.getVAMT_INR();
Vector VINV_TYPE = dbcredit.getVINV_TYPE();
Vector VDESK_NAME = dbcredit.getVDESK_NAME();
Vector VRES_COLLECTION_PRTY = dbcredit.getVRES_COLLECTION_PRTY();
Vector VRTL_GPL_TRADER = dbcredit.getVRTL_GPL_TRADER();
Vector VCLRNG_DOC = dbcredit.getVCLRNG_DOC();
Vector VCLRNG_DT = dbcredit.getVCLRNG_DT();
Vector VWBS_PNL = dbcredit.getVWBS_PNL();
Vector VBL_DT = dbcredit.getVBL_DT();
Vector VCOUNTERPARTY_CATEGORY = dbcredit.getVCOUNTERPARTY_CATEGORY();
Vector VINVOICE_TYPE = dbcredit.getVINVOICE_TYPE();
Vector VCURRANCY = dbcredit.getVCURRANCY();
Vector VAMT_USD = dbcredit.getVAMT_USD();
Vector VSTATUS = dbcredit.getVSTATUS();
Vector VOVERDUE_COZ = dbcredit.getVOVERDUE_COZ();
Vector VAGING = dbcredit.getVAGING();
Vector VARREARS_DAYS = dbcredit.getVARREARS_DAYS();
Vector VDUE_AMT_USD = dbcredit.getVDUE_AMT_USD();
Vector VDUE_AMT = dbcredit.getVDUE_AMT();
Vector VCO_CODE = dbcredit.getVCO_CODE();
Vector VCO_ABBR = dbcredit.getVCO_ABBR();

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table  width="100%" border="1">
		<tr>
			<th colspan="38" rowspan="2" align="left">Receivable Report <%=rpt_dt %></th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th align="center">Sr#</th>
				<th align="center">CO CODE</th>
				<th align="center">CUSTOMER ABBR</th>
				<th align="center">CUSTOMER NAME</th>
				<th align="center">COLLECTIONS CATEGORY</th>
				<th align="center">CUSTOMER CATEGORY</th>
				<th align="center">TYPE OF INVOICE</th>
				<th align="center">CATEGORY</th>
				<th align="center">BUSINESS</th>
				<th align="center">LEGAL ENTITY</th>
				<th align="center">DOC.NO.</th>
				<th align="center">REF.</th>
				<th align="center">REF.KEY 1</th>
				<th align="center">REF. KEY 2</th>
				<th align="center">REF. KEY 3</th>
				<th align="center">ASSIGNMENT</th>
				<th align="center">TYPE</th>
				<th align="center">TEXT</th>
				<th align="center">BA</th>
				<th align="center">NET DUE DT</th>
				<th align="center">AMOUNT IN DC</th>
				<th align="center">CURR.</th>
				<th align="center">AMT IN LOC.CUR.</th>
				<th align="center">AMOUNT IN USD</th>
				<th align="center" style="background-color: #000066; color: white">DUE AMOUNT INR</th>
				<th align="center" style="background-color: #000066; color: white">DUE AMOUNT USD</th>
				<th align="center">ARRERS (DAYS)</th>
				<th align="center">INV TYPE</th>
				<th align="center">AGEING</th>
				<th align="center">DESK NAME</th>
				<th align="center">RESPONSIBLE COLLECTION PARTY</th>
				<th align="center">RTL/GPL/TRADER NAME</th>
				<th align="center">STATUS</th>
				<th align="center">CLRNG DOC.</th>
				<th align="center">CLEARING DATE</th>
				<th align="center">WBS ELEMENT P&L ITEM</th>
				<th align="center">B/L DATE</th>
				<th align="center">REASON FOR OVERDUE</th>
			</tr>
		</thead>
		<tbody>
			<%if(VCOUNTERPARTY_NM.size()>0){
				for(int i=0;i<VCOUNTERPARTY_NM.size();i++){%>
				<tr>
					<td align="center"><%=i+1 %></td>
					<td align="center"><%=VCO_ABBR.elementAt(i)%></td>
					<td align="center"><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>
					<td align="center"><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
					<td align="center"><%=VCOLL_CATEGORY.elementAt(i)%></td>
					<td align="center"><%=VCOUNTERPARTY_CATEGORY.elementAt(i)%></td>
					<td align="center"><%=VINVOICE_TYPE.elementAt(i)%></td>
					<td align="center"><%=VCATEGORY.elementAt(i)%></td>
					<td align="center"><%=VBUSINESS.elementAt(i)%></td>
					<td align="center"><%=VLEGAL_ENTITY.elementAt(i)%></td>
					<td align="center"><%=VDOC_NO.elementAt(i)%></td>
					<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
					<td align="center"><%=VREF_K1.elementAt(i)%></td>
					<td align="center"><%=VREF_K2.elementAt(i)%></td>
					<td align="center"><%=VREF_K3.elementAt(i)%></td>
					<td align="center"><%=VDEAL_ASSIGNMENT.elementAt(i)%></td>
					<td align="center"><%=VCONT_TYPE.elementAt(i)%></td>
					<td align="center"><%=VTEXT.elementAt(i)%></td>
					<td align="center"><%=VBA.elementAt(i)%></td>
					<td align="center"><%=VNET_DUE_DT.elementAt(i)%></td>
					<td align="right"><%=VAMT_DC.elementAt(i)%></td>
					<td align="center"><%=VCURRANCY.elementAt(i)%></td>
					<td align="right"><%=VAMT_INR.elementAt(i)%></td>
					<td align="right"><%=VAMT_USD.elementAt(i)%></td>
					<td align="right" style="background-color: #b3f0ff"><%=VDUE_AMT.elementAt(i)%></td>
					<td align="right" style="background-color: #b3f0ff"><%=VDUE_AMT_USD.elementAt(i)%></td>
					<td align="center"><%=VARREARS_DAYS.elementAt(i)%></td>
					<td align="center"><%=VINV_TYPE.elementAt(i)%></td>
					<td align="center"><%=VAGING.elementAt(i)%></td>
					<td align="center"><%=VDESK_NAME.elementAt(i)%></td>
					<td align="center"><%=VRES_COLLECTION_PRTY.elementAt(i)%></td>
					<td align="center"><%=VRTL_GPL_TRADER.elementAt(i)%></td>
					<td align="center"><%=VSTATUS.elementAt(i)%></td>
					<td align="center"><%=VCLRNG_DOC.elementAt(i)%></td>
					<td align="center"><%=VCLRNG_DT.elementAt(i)%></td>
					<td align="center"><%=VWBS_PNL.elementAt(i)%></td>
					<td align="center"><%=VBL_DT.elementAt(i)%></td>
					<td align="center"><%=VOVERDUE_COZ.elementAt(i)%></td>
				</tr>
				<%} %>
			<%}else{ %>
				<tr>
					<td colspan="38" align="center"><%=utilmsg.infoMessage("<b>No Receivable Data is Available!</b>") %></td>
				</tr>
			<%} %>
		</tbody>
	</table>
</body>
</html>