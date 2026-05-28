<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>Remittance</title>
<link rel="stylesheet" type="text/css" href="../font-awesome-4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="../css/common.css">
<%@page import="java.util.Vector"%>
<script>

function refreshParent(report_dt)
{
	window.close();
	window.opener.refresh(report_dt);
}
var newWindow;
function printPDF(counterparty_cd,cont_no,agmt_no,contract_type,plant_seq,bu_plant_seq,from_contact,to_contact,report_dt,from_dt,to_dt,month_nm,frq_type,url_main)
{
	var url = "pdf_buyer_nom_to_trader.jsp?counterparty_cd="+counterparty_cd+"&cont_no="+cont_no+
		"&agmt_no="+agmt_no+"&contract_type="+contract_type+"&plant_seq="+plant_seq+"&bu_plant_seq="+bu_plant_seq+
		"&from_contact="+from_contact+"&to_contact="+to_contact+"&report_dt="+report_dt+"&from_dt="+from_dt+"&to_dt="+to_dt+"&month_nm="+month_nm+"&frq_type="+frq_type+"&url_main="+url_main;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Buyer Nomination To Trader PDF","top=10,left=10,width=1200,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Buyer Nomination To Trader PDF","top=10,left=10,width=1200,height=600,scrollbars=1");
	}
	refresh(url_main);
	window.close();
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.purchase.DB_Buy_Nom_Alloc_Mgmt" id="alloc" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="dateUtil" scope="request"></jsp:useBean>
<%
String sys_dttime = dateUtil.getSysdateWithTime24hr();
String sys_dt="";
String sys_time="";

if(!sys_dttime.equals(""))
{
	String temp[] = sys_dttime.split(" ");
	sys_dt=temp[0];
	sys_time=temp[1];
}

String owner_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");
String owner_abbr=""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
String owner_nm=""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");

String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String report_dt=request.getParameter("report_dt")==null?"":request.getParameter("report_dt");
String plant_seq=request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
String to_contact=request.getParameter("to_contact")==null?"":request.getParameter("to_contact");
String from_contact=request.getParameter("from_contact")==null?"":request.getParameter("from_contact");
String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String bu_plant_seq=request.getParameter("bu_plant_seq")==null?"":request.getParameter("bu_plant_seq");
String rmk=request.getParameter("rmk")==null?"":request.getParameter("rmk");
String frq_type=request.getParameter("frq_type")==null?"":request.getParameter("frq_type");
String from_dt = request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
String month_nm = request.getParameter("month_nm")==null?"":request.getParameter("month_nm");
String url_main = request.getParameter("url_main")==null?"":request.getParameter("url_main");

alloc.setCallFlag("VIEW_BUYER_NOM_TO_TRADER");
alloc.setCounterparty_cd(counterparty_cd);
alloc.setComp_cd(owner_cd);
alloc.setReport_dt(report_dt);
alloc.setTo_contact(to_contact);
alloc.setFrom_contact(from_contact);
alloc.setPlant_seq(plant_seq);
alloc.setCont_no(cont_no);
alloc.setAgmt_no(agmt_no);
alloc.setContract_type(contract_type);
alloc.setBu_plant_seq(bu_plant_seq);
alloc.setFrq_type(frq_type);
alloc.init();

String from_contact_nm = alloc.getFrom_contact_nm();
String from_fax = alloc.getFrom_fax();
String from_fax2 = alloc.getFrom_fax2();
String from_phone = alloc.getFrom_phone();
String from_mobile = alloc.getFrom_mobile();
String bu_plantAddress=alloc.getBu_plantAddress();
String bu_plantCity=alloc.getBu_plantCity();
String bu_plantState=alloc.getBu_plantState();
String bu_plantPin=alloc.getBu_plantPin();
String to_contact_nm = alloc.getTo_contact_nm();
String to_fax = alloc.getTo_fax();
String to_fax2 = alloc.getTo_fax2();
String to_phone = alloc.getTo_phone();
String to_mobile = alloc.getTo_mobile();
String plantAddress=alloc.getplantAddress();
String plantCity=alloc.getplantCity();
String plantState=alloc.getplantState();
String plantPin=alloc.getplantPin();
String plantNm=alloc.getPlantNm();
String signingDt=alloc.getSigningDt();
String contRef=alloc.getContRef();

Vector VGEN_TIME = alloc.getVGEN_TIME();
Vector VGEN_DT = alloc.getVGEN_DT();
Vector VGAS_DT = alloc.getVGAS_DT();
Vector VQTY_MMBTU = alloc.getVQTY_MMBTU();

String contract="our Gas Purchase Contract "+contRef+" dated "+signingDt+"";

%>
<body>
<%@ include file="../home/loading.jsp"%>
<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
	<tr valign="middle">
		<td colspan="7">
			<div align="center">
				<font size="4" face="Arial">
					<b><u>Buyer Nomination To Trader</u></b>
					<br><br>
				</font>				
				<font size="4" face="Arial">
					<b><%=owner_nm%></b>
					<br>
				</font>	
			</div>
		</td>
	</tr>
	<tr valign="middle">
		<td colspan="7">
			<div align="center">
				<font size="1px" face="Arial">
					<b></b>
				</font>
			</div>
		</td>
	</tr>
	<tr valign="middle">
		<td colspan="7">
			<div align="center">&nbsp;</div>
		</td>
	</tr>
	<tr valign="top">
		<td colspan="3" width="70%"><div align="left"><font size="1.5px" face="Arial"></font></div></td>
		<td colspan="1" width="50%">
			<div align="left">
				<font size="1.5px" face="Arial">
					<br><%=from_contact_nm%>
					<br><%=owner_nm %>
					<br><%=bu_plantAddress%>
					<br><%=bu_plantCity%>,<%=bu_plantState%>
					<br>Pin : <%=bu_plantPin%>
					<br>Fax : <%=from_fax %>,<%=from_fax2 %>
					<br>Phone : <%=from_phone %>
					<br>Mobile : <%=from_mobile %>
					<br><br>Date : <%=sys_dt %>
					<br>Time : <%=sys_time %>
				</font>
			</div>
		</td>
		
	</tr>
	<tr>
		<td colspan="3" width="40%">
			<div align="left">
				<font size="1.5px" face="Arial">
					<b>To:</b>
					<br><%=to_contact_nm %>
					<br><%=counterparty_cd%>
					<br><%=plantAddress%>
					<br><%=plantCity%>,<%=plantState%>
					<br>Pin : <%=plantPin%>
					<br>Fax : <%=to_fax %>,<%=to_fax2 %>
					<br>Phone : <%=to_phone %>
					<br>Mobile : <%=to_mobile %>				
				</font>
			</div>
		</td>
	</tr>
	<tr valign="top">
		<td colspan="7">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="4" width="40%">
			<div align="left">
				<font size="1.5px" face="Arial">
					<b>Subject : </b>&nbsp;&nbsp;
					<%if(frq_type.equals("W")){ %>
						"Buyer's Weekly Nomination for <%=from_dt %> - <%=to_dt %>
					<%}else{ %>
						Buyer's Daily Nomination for Date <%=report_dt %>
					<%} %>
				</font>
				<hr color="black" size="2px">
			</div>
		</td>
	</tr>
	<tr valign="top">
		<td colspan="7">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="3" width="40%">
			<div align="left">
				<font size="1.5px" face="Arial">
					Dear Sir,&nbsp;&nbsp;
					<br><br>As per <%=contract %> , we notify nomination as follows:
				</font>
			</div>
		</td>
	</tr>
</table>
<br>
<br>
<table width="100%"  border="1" align="center" cellpadding="2" cellspacing="0">
	<tr valign="bottom">
		<td width="6%"><div align="center"><font size="1.5px" face="Arial"><b>Date</b></font></div></td>
		<td width="34%"><div align="center"><font size="1.5px" face="Arial"><b>MMBTU</b></font></div></td>
		<td width="10%"><div align="center"><font size="1.5px" face="Arial"><b>Delivery Point</b></font></div></td>
	</tr>
	<%for(int i=0; i<VGAS_DT.size(); i++)
    { %>
		<tr valign="top">
			<td>
				<div align="center">
					<font size="1.5px" face="Arial"><%=VGAS_DT.elementAt(i) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=VQTY_MMBTU.elementAt(i)%></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="1.5px" face="Arial"><%=plantNm%></font>
				</div>
			</td>
		</tr>
	<%} %>
</table>
<br>
<br>
<table width="100%"  border="0" align="center" cellpadding="2" cellspacing="0">
	<tr>
		<td colspan="3" width="40%">
			<div align="left">
				<font size="1.5px" face="Arial">
					Thanking You,
					<br><br><br><%=owner_nm %>
					<br><br><%=from_contact_nm %>
					<br><br><b>Authorised Signatory</b>
				</font>
			</div>
		</td>
	</tr>
</table>
<br>
<br>
<table align="center">
	<tr valign="middle"><td colspan="7"><div align="center">&nbsp;</div></td></tr>
	<tr valign="middle"><td colspan="7"><div align="center">&nbsp;</div></td></tr>
	<tr valign="middle">
		<td>
			<div align="center">
					<font style="color: blue;"><I><b>Do you want to Generate Buyer Nomination PDF?</b></I></font>&nbsp;
					<input type="radio" name="rd" value="Y" 
					onclick="printPDF('<%=counterparty_cd%>','<%=cont_no%>','<%=agmt_no%>','<%=contract_type%>',
												'<%=plant_seq%>','<%=bu_plant_seq%>','<%=from_contact%>','<%=to_contact%>','<%=report_dt%>','<%=from_dt%>','<%=to_dt%>','<%=month_nm%>','<%=frq_type%>','<%=url_main%>')">&nbsp;<b>Yes</b>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
		</td>
	</tr>
</table>
<br>
<br>


	
</body>
</html>