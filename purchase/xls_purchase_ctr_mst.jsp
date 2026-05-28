<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>
<jsp:useBean class="com.etrm.fms.purchase.DataBean_Trader_Contract_Mst" id="purchase" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
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
String sysdate=utildate.getSysdate();
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String trader_cd=request.getParameter("trader_cd")==null?"0":request.getParameter("trader_cd");

purchase.setCallFlag("PUR_CTR_MST");
purchase.setComp_cd(owner_cd);
purchase.setFrom_dt(from_dt);
purchase.setTo_dt(to_dt);
purchase.setCounterparty_cd(trader_cd);
purchase.init();

String cpStatus = purchase.getcpStatus();

Vector VMST_COUNTERPARTY_CD = purchase.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_ABBR = purchase.getVMST_COUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_NM = purchase.getVMST_COUNTERPARTY_NM();

Vector VCOUNTERPARTY_CD = purchase.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = purchase.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = purchase.getVCOUNTERPARTY_NM();
Vector VAGMT_NO = purchase.getVAGMT_NO();
Vector VCONT_NO = purchase.getVCONT_NO();
Vector VCARGO_NO = purchase.getVCARGO_NO();
Vector VCONTRACT_TYPE =purchase.getVCONTRACT_TYPE();
Vector VDEAL_MAP = purchase.getVDEAL_MAP();
Vector VEFF_DT = purchase.getVEFF_DT();
Vector VMOLE_CD = purchase.getVMOLE_CD();
Vector VPROD_CD = purchase.getVPROD_CD();
Vector VPLANT_SEQ_NO = purchase.getVPLANT_SEQ_NO();
Vector VPLANT_ABBR = purchase.getVPLANT_ABBR();
Vector VBU_PLANT_SEQ_NO = purchase.getVBU_PLANT_SEQ_NO();
Vector VBU_PLANT_ABBR = purchase.getVBU_PLANT_ABBR();
Vector VMOLECULE_NM = purchase.getVMOLECULE_NM();
Vector VCTR_REF = purchase.getVCTR_REF();
Vector VPLANT_NAME = purchase.getVPLANT_NAME();
Vector VBU_PLANT_NM = purchase.getVBU_PLANT_NM();
Vector VINDEX = purchase.getVINDEX();

Vector VCTR_COUNTERPARTY_CD = purchase.getVCTR_COUNTERPARTY_CD();
Vector VCTR_COUNTERPARTY_NM = purchase.getVCOUNTERPARTY_NM();
Vector VCTR_COUNTERPARTY_ABBR = purchase.getVCOUNTERPARTY_ABBR();
Vector VCTR_PLANT_NM = purchase.getVCTR_PLANT_NM();
Vector VCTR_PLANT_ABBR = purchase.getVCTR_PLANT_ABBR();
Vector VCTR_PLANT_SEQ_NO = purchase.getVCTR_PLANT_SEQ_NO();
Vector VCTR_BU_PLANT_NM = purchase.getVCTR_BU_PLANT_NM();
Vector VCTR_BU_PLANT_ABBR = purchase.getVCTR_BU_PLANT_ABBR();
Vector VCTR_BU_PLANT_SEQ_NO = purchase.getVCTR_BU_PLANT_SEQ_NO();
Vector VCTR_CONT_NO = purchase.getVCTR_CONT_NO();
Vector VCTR_AGMT_NO = purchase.getVCTR_AGMT_NO();
Vector VCTR_DISP_CONT_NO = purchase.getVCTR_DISP_CONT_NO();
Vector VCTR_CARGO_NO = purchase.getVCTR_CARGO_NO();
Vector VCTR_CONT_TYPE = purchase.getVCTR_CONT_TYPE();
Vector VCTR_PROD_CD = purchase.getVCTR_PROD_CD();
Vector VCTR_MOLE_CD = purchase.getVCTR_MOLE_CD();
Vector VCTR_MOLE_NM = purchase.getVCTR_MOLE_NM();
Vector VCONT_REF_NO = purchase.getVCONT_REF_NO();
Vector VCTR_CONT_REF_NO = purchase.getVCTR_CONT_REF_NO();
Vector VCTR_PROD_ABBR = purchase.getVCTR_PROD_ABBR();
Vector VPRODUCT_NM = purchase.getVPRODUCT_NM();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>

	<table  width="100%" border="1">
		<tr>
			<th colspan="10" rowspan="2" align="left">Purchase CTR</th>
		</tr>
	</table>
	<table  width="100%" >
		<tr>
			<th></th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th align="left" colspan="10"><%=VCOUNTERPARTY_NM.elementAt(0) %></th>
			</tr>
			<tr>
				<th>SR#</th>
			   	<th>CTR</th>		    		
				<th>Business Unit</th>
				<th>Trader</th>
				<th>Plant</th>				    		
			   	<th>Link Contract</th>
			   	<th>Contract Ref</th>
			   	<th>Product</th>
			   	<th>Molecule</th>
				<th>Eff. Date</th>		
			   </tr>
		</thead>
		<tbody>
		<%
		if(VCTR_REF.size() > 0)
		{
		    for(int i = 0; i < VCTR_REF.size(); i++)
		    {
		%>
		    <tr>
		        <td><%= (i+1) %></td>
		        <td><%= VCTR_REF.elementAt(i) %></td>
		        <td><%= VBU_PLANT_ABBR.elementAt(i) %></td>
		        <td><%= VCOUNTERPARTY_ABBR.elementAt(i) %></td>
		        <td><%= VPLANT_ABBR.elementAt(i) %></td>
		        <td><%= VDEAL_MAP.elementAt(i) %></td>
		        <td><%= VCONT_REF_NO.elementAt(i) %></td>
		        <td><%= VPRODUCT_NM.elementAt(i) %></td>
		        <td><%= VMOLECULE_NM.elementAt(i) %></td>
		        <td><%= VEFF_DT.elementAt(i) %></td>
		    </tr>
		<%
		    }
		}
		else
		{
		%>
		    <tr>
		        <td colspan="10" align="center">No Data Found</td>
		    </tr>
		<%
		}
		%>
		</tbody>
	</table>
</body>