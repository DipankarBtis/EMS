
<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<script>

function openPdf(file_nm,url)
{
	var msg="";
	var msg_type="";
	if(file_nm!="")
	{
		msg="PDF has been Generated!";
		msg_type="S";
	}
	else
	{
		msg="PDF Generation Failed!";
		msg_type="E";
	}
	//alert(msg)
	window.opener.refresh_rpt(msg,msg_type);
	var window1="";
	if(!window1 || window1.closed)
	{
		window1= window.open(url,"Seller Nomination To Control Room","top=10,left=10,width=1000,height=600,scrollbars=1");
	}
	else
	{
		window1.close();
		window1= window.open(url,"Seller Nomination To Control Room","top=10,left=10,width=1000,height=600,scrollbars=1");
	}
	
	//window.close();
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_mgmt.DataBean_ContractMgmt" id="cont_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DataBean_Pdf_Generation" id="pdfFile" scope="request"/>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>

<%
String sys_dt_time = utildate.getSysdateWithTime24hr();
String prevdate = utildate.getPreviousDate();

String gas_dt = request.getParameter("gas_dt")==null?prevdate:request.getParameter("gas_dt");
String owner_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");
String owner_abbr=""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
String owner_nm=""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");
String owner_logo=""+session.getAttribute("comp_logo")==null?"":""+session.getAttribute("comp_logo");
String emp_cd=""+session.getAttribute("emp_cd")==null?"":""+session.getAttribute("emp_cd");

String file=request.getParameter("file")==null?"":request.getParameter("file");
String chk_oblig=request.getParameter("chk_oblig")==null?"":request.getParameter("chk_oblig");
String chk_exp=request.getParameter("chk_exp")==null?"":request.getParameter("chk_exp");
String tot_exp_mmscm=request.getParameter("tot_exp_mmscm")==null?"":request.getParameter("tot_exp_mmscm");
String ask_exp_mmscm=request.getParameter("ask_exp_mmscm")==null?"":request.getParameter("ask_exp_mmscm");

cont_mgmt.setCallFlag("DAILY_SELLER_CONTROL_ROOM");
cont_mgmt.setComp_cd(owner_cd);
cont_mgmt.setEmp_cd(emp_cd);
cont_mgmt.setGas_dt(gas_dt);
cont_mgmt.init();

Vector VCATEGORY = cont_mgmt.getVCATEGORY();
Vector VTRANSPORTER_CD = cont_mgmt.getVTRANSPORTER_CD();
Vector VTRANSPORTER_NM = cont_mgmt.getVTRANSPORTER_NM();
Vector VTRANSPORTER_ABBR = cont_mgmt.getVTRANSPORTER_ABBR();
Vector VCOUNTERPARTY_NM = cont_mgmt.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = cont_mgmt.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_CD = cont_mgmt.getVCOUNTERPARTY_CD();
Vector VCUST_WISE_MMSCM = cont_mgmt.getVCUST_WISE_MMSCM();
Vector VCUST_WISE_MMBTU = cont_mgmt.getVCUST_WISE_MMBTU();

Vector VQTY_MMSCM = cont_mgmt.getVQTY_MMSCM();
Vector VQTY_MMBTU = cont_mgmt.getVQTY_MMBTU();

Vector VTOT_MMSCM = cont_mgmt.getVTOT_MMSCM();
Vector VTOT_MMBTU = cont_mgmt.getVTOT_MMBTU();
Vector VOBLG_MMSCM= cont_mgmt.getVOBLG_MMSCM();
Vector VCUST_WISE_OBLG = cont_mgmt.getVCUST_WISE_OBLG();
Vector VTOT_OBLG_MMSCM= cont_mgmt.getVTOT_OBLG_MMSCM();
Vector VTEMP_TRANS_CD = cont_mgmt.getVTEMP_TRANS_CD();
Vector VTEMP_TRANS_PLANT_SEQ = cont_mgmt.getVTEMP_TRANS_PLANT_SEQ();
Vector VTEMP_COUNTERPARTY_CD = cont_mgmt.getVTEMP_COUNTERPARTY_CD();
Vector VEXP_MMSCM = cont_mgmt.getVEXP_MMSCM();
Vector VPLANT_NM = cont_mgmt.getVPLANT_NM();
Vector VPLANT_SEQ = cont_mgmt.getVPLANT_SEQ();
Vector VTRANSPORTER_PLANT_SEQ = cont_mgmt.getVTRANSPORTER_PLANT_SEQ();
Vector VTRANSPORTER_PLANT_ABBR = cont_mgmt.getVTRANSPORTER_PLANT_ABBR();

String tot_mmscm = cont_mgmt.getTot_mmscm();
String tot_mmbtu = cont_mgmt.getTot_mmbtu();
String tot_obl_mcm = cont_mgmt.getTot_obl_mcm();
String ask_nom_mcm = cont_mgmt.getAsk_nom_mcm();
String ask_obl_mcm = cont_mgmt.getAsk_obl_mcm();
String ask_nom_mmbtu= cont_mgmt.getAsk_nom_mmbtu();

%>
<%if(file.equals("XLS")){ %>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename=Seller_NominationToControlRoom_"+gas_dt.replaceAll("/", "")+".xls");
%>
<table>
	<tr>
		<td colspan="5">
			<font size="4"><b>Seller's Daily Nomination To Control Room for Date <%=gas_dt%></b></font>
		</td>
	</tr>
</table>
<br>
<br>
<table width="80%"  border="1" align="center" cellpadding="2" cellspacing="0">	
	<thead>
		<tr>
			<th rowspan="2"><div align="center">Transporter</div></th>
			<th><div align="center">Nomination</div></th>							
			<th><div align="center">Nomination</div></th>	
			<%if(chk_oblig.trim().equalsIgnoreCase("Y")){ %>
				<th><div align="center">Obligation</div></th>	
			<%} %>	
			<%if(chk_exp.trim().equalsIgnoreCase("Y")){%>		
				<th><div align="center">Expected</div></th>	
			<%}%>
		</tr>
		<tr>
			<th><div align="center">MCM</div></th>							
			<th><div align="center">MMBTU</div></th>
			<%if(chk_oblig.trim().equalsIgnoreCase("Y")){ %>
				<th><div align="center">MCM</div></th>	
			<%} %>	
			<%if(chk_exp.trim().equalsIgnoreCase("Y")){%>	
				<th><div align="center">MMSCM</div></th>
			<%}%>										
		</tr>
	</thead>
	<tbody>
		<%if(VTRANSPORTER_PLANT_SEQ.size()>0){ %>
			<%for(int i=0; i<VTRANSPORTER_PLANT_SEQ.size(); i++){ %>
			<tr>
				<td><div align="left"><b><%=VTRANSPORTER_PLANT_ABBR.elementAt(i) %></b></div></td>
				<td><div align="right"><%=VTOT_MMSCM.elementAt(i) %></div></td>
				<td><div align="right"><%=VTOT_MMBTU.elementAt(i) %></div></td>
				<%if(chk_oblig.trim().equalsIgnoreCase("Y")){ %>
					<td><div align="right"><%=VTOT_OBLG_MMSCM.elementAt(i) %></div></td>
				<%} %>	
				<%if(chk_exp.trim().equalsIgnoreCase("Y")){%>
					<td></td>
				<%}%>
			</tr>
			<%if(VTEMP_COUNTERPARTY_CD.size()>0)
		  	{
	  		  	for(int j=0;j<VTEMP_COUNTERPARTY_CD.size();j++)
				{
	  		  		if(VTRANSPORTER_CD.elementAt(i).toString().equalsIgnoreCase(VTEMP_TRANS_CD.elementAt(j).toString()) && VTRANSPORTER_PLANT_SEQ.elementAt(i).toString().equalsIgnoreCase(VTEMP_TRANS_PLANT_SEQ.elementAt(j).toString()))
	  		  		{%>
					<tr>
						<td>
							<div align="left">&nbsp;&nbsp;<%=VCOUNTERPARTY_ABBR.elementAt(j)%> - <%=VPLANT_NM.elementAt(j)%> (<%=VCATEGORY.elementAt(j)%>) </div>
							<input type="hidden" name="transporter_cd" value="<%=(String)VTEMP_TRANS_CD.elementAt(j)%>">
							<input type="hidden" name="counterparty_cd" value="<%=(String)VTEMP_COUNTERPARTY_CD.elementAt(j)%>">
						</td>							
						<td><div align="right"><%=VCUST_WISE_MMSCM.elementAt(j)%></div></td>											
						<td><div align="right"><%=VCUST_WISE_MMBTU.elementAt(j)%><input type="hidden" name="plant_seq_no" value="<%=VPLANT_SEQ.elementAt(j)%>"></div></td>
						<%if(chk_oblig.trim().equalsIgnoreCase("Y")){ %>	
							<td><div align="right"><%=VCUST_WISE_OBLG.elementAt(j)%></div>
								<input type="hidden" name="cont_type" value="<%=VCATEGORY.elementAt(j)%>">
							</td>
						<%} %>	
						<%if(chk_exp.trim().equalsIgnoreCase("Y")){%>
							<td><div align="center"><%=VEXP_MMSCM.elementAt(j)%></div></td>		
						<%} %>					
					</tr>		
		  			<%}
		  		}
			}
		} %>
		<tr>
			<td><div align="right"><b>Total</b></div></td>		
			<td><div align="right"><%=tot_mmscm%></div></td>
			<td><div align="right"><%=tot_mmbtu%></div></td>	
			<%if(chk_oblig.trim().equalsIgnoreCase("Y")){ %>
				<td><div align="right"><%=tot_obl_mcm%></div></td>	
			<%} %>	
			<%if(chk_exp.trim().equalsIgnoreCase("Y")){%>					
				<td><div align="center"><%=tot_exp_mmscm %></div></td>		
			<%}%>				
		</tr>	
		<tr>
			<td><div align="right"><b>Asking Rate</b></div></td>		
			<td><div align="right"><%=ask_nom_mcm%></div></td>
			<td><div align="right"><%=ask_nom_mmbtu%></div></td>
			<%if(chk_oblig.trim().equalsIgnoreCase("Y")){ %>
				<td><div align="right"><%=ask_obl_mcm%></div></td>
			<%} %>	
			<%if(chk_exp.trim().equalsIgnoreCase("Y")){%>
				<td><div align="center"><%=ask_exp_mmscm %></div></td>	
			<%}%>				
		</tr>
		<%}%>
	</tbody>
</table>
<br><br>

<%}else if(file.equals("PDF")){ %>
<% 
HttpServletRequest req = request;

pdfFile.setCallFlag("PDF_DAILY_SELLER_CONTROL_ROOM");
pdfFile.setRequest(req);
pdfFile.setComp_logo(owner_logo);
pdfFile.setComp_cd(owner_cd);
pdfFile.setComp_nm(owner_nm);
pdfFile.setEmp_cd(emp_cd);
pdfFile.setGas_dt(gas_dt);
pdfFile.setTot_mmscm(tot_mmscm);
pdfFile.setTot_mmbtu(tot_mmbtu);
pdfFile.setTot_obl_mcm(tot_obl_mcm);
pdfFile.setAsk_nom_mcm(ask_nom_mcm);
pdfFile.setAsk_obl_mcm(ask_obl_mcm);
pdfFile.setAsk_nom_mmbtu(ask_nom_mmbtu);
pdfFile.setChk_oblig(chk_oblig);
pdfFile.setChk_exp(chk_exp);
pdfFile.setTot_exp_mmscm(tot_exp_mmscm);
pdfFile.setAsk_exp_mmscm(ask_exp_mmscm);

pdfFile.setVCATEGORY(VCATEGORY);
pdfFile.setVTRANSPORTER_CD(VTRANSPORTER_CD);
pdfFile.setVTRANSPORTER_NM(VTRANSPORTER_NM);
pdfFile.setVTRANSPORTER_ABBR(VTRANSPORTER_ABBR);
pdfFile.setVCOUNTERPARTY_NM(VCOUNTERPARTY_NM);
pdfFile.setVCOUNTERPARTY_ABBR(VCOUNTERPARTY_ABBR);
pdfFile.setVCOUNTERPARTY_CD(VCOUNTERPARTY_CD);
pdfFile.setVCUST_WISE_MMSCM(VCUST_WISE_MMSCM);
pdfFile.setVCUST_WISE_MMBTU(VCUST_WISE_MMBTU);
pdfFile.setVQTY_MMSCM(VQTY_MMSCM);
pdfFile.setVQTY_MMBTU(VQTY_MMBTU);
pdfFile.setVTOT_MMSCM(VTOT_MMSCM);
pdfFile.setVTOT_MMBTU(VTOT_MMBTU);
pdfFile.setVOBLG_MMSCM(VOBLG_MMSCM);
pdfFile.setVCUST_WISE_OBLG(VCUST_WISE_OBLG);
pdfFile.setVTOT_OBLG_MMSCM(VTOT_OBLG_MMSCM);
pdfFile.setVTEMP_TRANS_CD(VTEMP_TRANS_CD);
pdfFile.setVTEMP_COUNTERPARTY_CD(VTEMP_COUNTERPARTY_CD);
pdfFile.setVEXP_MMSCM(VEXP_MMSCM);
pdfFile.setVPLANT_NM(VPLANT_NM);
pdfFile.setVPLANT_SEQ(VPLANT_SEQ);
pdfFile.setVTRANSPORTER_PLANT_SEQ(VTRANSPORTER_PLANT_SEQ);
pdfFile.setVTRANSPORTER_PLANT_ABBR(VTRANSPORTER_PLANT_ABBR);
pdfFile.setVTEMP_TRANS_PLANT_SEQ(VTEMP_TRANS_PLANT_SEQ);
pdfFile.init();

String file_url = pdfFile.getFile_url();
String file_nm = pdfFile.getFile_nm();
String pdfpath = file_url+"/"+CommonVariable.work_dir+owner_cd+""+CommonVariable.nom_to_control_room_pdf_path+""+file_nm;
%>
<script>
openPdf('<%=file_nm%>','<%=pdfpath%>');
</script>
<%}%>


</html>