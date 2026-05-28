<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>
<jsp:useBean class="com.etrm.fms.purchase.DB_Buy_Nom_Alloc_Mgmt" id="purchase" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
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
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String clearance = request.getParameter("clearance")==null?"KYC":request.getParameter("clearance");

String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");

String stdt = request.getParameter("st_dt")==null?"":request.getParameter("st_dt");
String enddt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
String from_dt = request.getParameter("from_dt")==null?stdt:request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?enddt:request.getParameter("to_dt");

String sel_trad_plant =request.getParameter("sel_trad_plant")==null?"0":request.getParameter("sel_trad_plant");
String sel_bu_plant =request.getParameter("sel_bu_plant")==null?"0":request.getParameter("sel_bu_plant");
String cont_cargo =request.getParameter("cont_cargo")==null?"":request.getParameter("cont_cargo");
String cargo_no =request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");

String contract_type=request.getParameter("cont_type")==null?"":request.getParameter("cont_type");
if(contract_type.equals(""))
{
	if(clearance.equals("IGX"))
	{
		contract_type="I";
	}
	else if(clearance.equals("KYC"))
	{
		contract_type="D";
	}
}

purchase.setCallFlag("REPORT_NOMINATION_ALLOCATION");
purchase.setClearance(clearance);
purchase.setCounterparty_cd(counterparty_cd);
purchase.setAgmt_no(agmt_no);
purchase.setAgmt_rev_no(agmt_rev_no);
purchase.setCont_no(cont_no);
purchase.setCont_rev_no(cont_rev_no);
purchase.setContract_type(contract_type);
purchase.setComp_cd(owner_cd);
purchase.setFrom_date(from_dt);
purchase.setTo_date(to_dt);
purchase.setPlant_seq(sel_trad_plant);
purchase.setBu_plant_seq(sel_bu_plant);
purchase.setCont_cargo(cont_cargo);
purchase.setCargo_no(cargo_no);
purchase.init();

Vector VCOUNTERPARTY_CD = purchase.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = purchase.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = purchase.getVCOUNTERPARTY_ABBR();

Vector VGAS_DT = purchase.getVGAS_DT();
Vector VBUYER_QTY_MMBTU = purchase.getVBUYER_QTY_MMBTU();
Vector VBUYER_QTY_SCM = purchase.getVBUYER_QTY_SCM();
Vector VSELLER_QTY_MMBTU = purchase.getVSELLER_QTY_MMBTU();
Vector VSELLER_QTY_SCM = purchase.getVSELLER_QTY_SCM();
Vector VQTY_MMBTU = purchase.getVQTY_MMBTU();
Vector VQTY_SCM = purchase.getVQTY_SCM();
Vector VDCQ = purchase.getVDCQ();
Vector VMDCQ = purchase.getVMDCQ();
Vector VCOLOR = purchase.getVCOLOR();
Vector VBUYER_COLOR = purchase.getVBUYER_COLOR();
Vector VSELLER_COLOR = purchase.getVSELLER_COLOR();

Vector VSEL_PLANT_SEQ_NO = purchase.getVSEL_PLANT_SEQ_NO();
Vector VSEL_PLANT_ABBR = purchase.getVSEL_PLANT_ABBR();
Vector VSEL_BU_CD = purchase.getVSEL_BU_CD();
Vector VSEL_BU_PLANT_SEQ_NO = purchase.getVSEL_BU_PLANT_SEQ_NO();
Vector VSEL_BU_PLANT_ABBR = purchase.getVSEL_BU_PLANT_ABBR();

String start_dt = purchase.getStart_dt();
String end_dt = purchase.getEnd_dt();
String cont_name = purchase.getCont_name();
String dcq = purchase.getDcq();
String mdcq_percentage = purchase.getMdcq_percentage();
if(mdcq_percentage.equals("")){
	mdcq_percentage="100";
}
if(dcq.equals("")){
	dcq="0";
}

String displayContNm="";
if(!start_dt.equals("") && !end_dt.equals(""))
{
	displayContNm=cont_name+"  ("+start_dt+" - "+end_dt+")";
}

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>

<table  width="100%" border="1">
	<tr>
		<th colspan="9" rowspan="2" align="left">Nomination & Allocation From <%=from_dt%> To <%=to_dt %></th>
	</tr>	
</table >

<table width="100%" border="1">
	<thead>
		<tr>
			<th colspan="9" align="center"><%=displayContNm%></th>
		</tr>
		<tr>
			<th rowspan="2">Gas Day</th>
			<th rowspan="2">DCQ<br>(MMBTU)</th>
			<th rowspan="2">MDCQ<br>(MMBTU)</th>
			<th colspan="2">Buyer Nomination</th>
			<th colspan="2" title="Entry Point Energy">Seller Nomination</th>
			<th colspan="2" title="Exit Point Energy">Allocation</th>
		</tr>
		<tr>
			<th>Energy (MMBTU)</th>
			<th>Energy (SCM)</th>
			<th>Energy (MMBTU)</th>
			<th>Energy (SCM)</th>
			<th>Energy (MMBTU)</th>
			<th>Energy (SCM)</th>
		</tr>
	</thead>
	<tbody>
	<%for(int i=0; i<VGAS_DT.size(); i++){ %>
		<tr>
			<td align="center">&nbsp;<%=VGAS_DT.elementAt(i) %>&nbsp;</td>
			<td align="right"><%=VDCQ.elementAt(i) %></td>
			<td align="right"><%=VMDCQ.elementAt(i) %></td>
			<td align="right" style="background:<%=VBUYER_COLOR.elementAt(i)%>"><%=VBUYER_QTY_MMBTU.elementAt(i) %></td>
			<td align="right" style="background:<%=VBUYER_COLOR.elementAt(i)%>"><%=VBUYER_QTY_SCM.elementAt(i) %></td>
			<td align="right" style="background:<%=VSELLER_COLOR.elementAt(i)%>"><%=VSELLER_QTY_MMBTU.elementAt(i) %></td>
			<td align="right" style="background:<%=VSELLER_COLOR.elementAt(i)%>"><%=VSELLER_QTY_SCM.elementAt(i) %></td>
			<td align="right" style="background:<%=VCOLOR.elementAt(i)%>"><%=VQTY_MMBTU.elementAt(i) %></td>
			<td align="right" style="background:<%=VCOLOR.elementAt(i)%>"><%=VQTY_SCM.elementAt(i) %></td>
		</tr>
	<%} %>	
	</tbody>
</table>
</body>
</html>