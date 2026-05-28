<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="ISO-8859-1">

<jsp:useBean class="com.etrm.fms.mgmt_reports.DB_MGMT_Sales_Invoice_Rpt" id="mgmt_reports" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>

<%
String sysdate=utildate.getSysdate();

String counterparty_cd=request.getParameter("counterparty_cd")==""?"0":request.getParameter("counterparty_cd");
String deal_no=request.getParameter("deal_no")==null?"0":request.getParameter("deal_no");
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String bu_seq=request.getParameter("bu_seq")==null?"0":request.getParameter("bu_seq");
String bu_plant=request.getParameter("bu_plant")==""?"0":request.getParameter("bu_plant");
String agmt_no = "";
String agmt_rev="";
String cont_no="";
String cont_rev="";
String cont_type="";
if (deal_no != null && !deal_no.trim().equals("0") && !deal_no.trim().isEmpty()) 
{
     String[] parts = deal_no.split(":");
     agmt_no   = parts[0];
     agmt_rev  = parts[1];
     cont_no   = parts[2];
     cont_type = parts[3];
}
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
mgmt_reports.setCallFlag("ALLOCATION_TO_CUSTOMER_PLANT");
mgmt_reports.setComp_cd(owner_cd);
mgmt_reports.setFrom_dt(from_dt);
mgmt_reports.setTo_dt(to_dt);
mgmt_reports.setCounterparty_cd(counterparty_cd);
mgmt_reports.setBu(bu_seq);
mgmt_reports.setBu_plant(bu_plant);
mgmt_reports.setAgmt_no(agmt_no);
mgmt_reports.setAgmt_rev(agmt_rev);
mgmt_reports.setCont_no(cont_no);
mgmt_reports.setCont_rev(cont_rev);
mgmt_reports.setCont_type(cont_type);
mgmt_reports.setDeal_no(deal_no);
mgmt_reports.init();

Vector VMST_COUNTERPARTY_CD = mgmt_reports.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = mgmt_reports.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = mgmt_reports.getVMST_COUNTERPARTY_ABBR();
Vector VBU_PLANT_ABBR=mgmt_reports.getVBU_PLANT_ABBR();
Vector VBU_PLANT_SEQ=mgmt_reports.getVBU_PLANT_SEQ();
Vector VDIS_CONT_MAPPING=mgmt_reports.getVDIS_CONT_MAPPING();
Vector VCONT_REF=mgmt_reports.getVCONT_REF();
Vector VCOUNTERPARTY_CD = mgmt_reports.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = mgmt_reports.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = mgmt_reports.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_PLANT_SEQ = mgmt_reports.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_NM = mgmt_reports.getVCOUNTERPARTY_PLANT_NM();

Vector VSEGMENT = mgmt_reports.getVSEGMENT();
Vector VSEGMENT_TYPE = mgmt_reports.getVSEGMENT_TYPE();
Vector VINDEX = mgmt_reports.getVINDEX();
Vector VINDEX1 = mgmt_reports.getVINDEX1();
Vector VCONT_NO = mgmt_reports.getVCONT_NO();

Vector VGAS_DT = mgmt_reports.getVGAS_DT();
Vector VQTY_MMBTU = mgmt_reports.getVQTY_MMBTU();
Vector VQTY_SCM = mgmt_reports.getVQTY_SCM();
Vector VTOTAL_QTY_MMBTU = mgmt_reports.getVTOTAL_QTY_MMBTU();
Vector VTOTAL_QTY_SCM = mgmt_reports.getVTOTAL_QTY_SCM();

Vector VPLANT_NM = mgmt_reports.getVPLANT_NM();
Vector VPLANT_SEQ_NO = mgmt_reports.getVPLANT_SEQ_NO();
Vector VCONTRACT_TYPE = mgmt_reports.getVCONTRACT_TYPE();
Vector VCONT_REV = mgmt_reports.getVCONT_REV();
Vector VAGMT_NO = mgmt_reports.getVAGMT_NO();
Vector VAGMT_REV = mgmt_reports.getVAGMT_REV();

String deal_map = mgmt_reports.getDeal_map();
String cont_ref_no = mgmt_reports.getCont_ref_no();

%>

<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
<font size="4"><b>Customer Allocation (By Plant And Contract)  From <%=from_dt%> To <%=to_dt%></b></font>
<br>

<%if(!deal_no.equals("")&& !deal_no.equals("0")) { %>
    <%if(VCOUNTERPARTY_CD.size()>0){ %>
	<%int k=0,l=0,p=0;
	  for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ 
		 int plant_size=((Vector) VCOUNTERPARTY_PLANT_SEQ.elementAt(i)).size();
		 int index=Integer.parseInt(""+VINDEX.elementAt(i));
	 %>
		<div class="row">
		  <div class="table-responsive">
			<table class="table table-bordered table-hover" width="100%" border="1">
			  <thead>
				<tr>
					<th colspan="<%=(plant_size * 2) + 3%>" style="text-align:left; font-size:14px; background-color:<%= owner_cd.equals("2") ? "#336699" : "#FFD700" %>;
						    color:<%= owner_cd.equals("2") ? "white" : "black" %>;">
						    <%=VCOUNTERPARTY_ABBR.elementAt(i)%>&nbsp;-&nbsp;<%=VCOUNTERPARTY_NM.elementAt(i)%>&nbsp;-&nbsp;
				    		<%if ( !deal_map.toString().trim().matches("^$|\\[\\]") &&
				    		    !cont_ref_no.toString().trim().matches("^$|\\[\\]"))  {
								%>
								    <%=deal_map%>-[<%=cont_ref_no%>]
								<%
								}
		                     %>
					 </th>
				 </tr>
				 <tr>
					<th rowspan="3">Gas Day</th>
					<th rowspan="2" colspan="2">Total Quantity Supplied</th>
					<th colspan="<%=plant_size*2%>">Total Quantity Supplied To Plant</th>
				 </tr>
				 <tr>
					<%for(int j=0;j<plant_size;j++){ %>
					<th colspan="2"><%=((Vector) VCOUNTERPARTY_PLANT_NM.elementAt(i)).elementAt(j)%></th>
					<%} %>
				 </tr>
				 <tr>
					<th>MMBTU</th>
					<th>SCM</th>
					<%for(int j=0;j<plant_size;j++){ %>
					<th>MMBTU</th>
					<th>SCM</th>
					<%} %>
				 </tr>
			  </thead>
			  <tbody>
				<%int n=0;
				for(k=k;k<VGAS_DT.size();k++){
					n+=1;
				%>
				<tr>
					<td align="center"><%=VGAS_DT.elementAt(k)%></td>
						<%int m=0;
						for(l=l;l<VQTY_MMBTU.size();l++){ 
							m+=1;
						%>
					<td align="right"><%=VQTY_MMBTU.elementAt(l)%></td>
					<td align="right"><%=VQTY_SCM.elementAt(l)%></td>
						<%if((plant_size+1) == m){
							l++;
							break;
						} %>
					<%} %>
				</tr>
				<%if(index == n){ %>
				<tr style="font-weight:bold;">
					<td align="right">Total&nbsp;:&nbsp;</td>
					<%int o=0;
					for(p=p;p<VTOTAL_QTY_MMBTU.size();p++){ 
						o+=1;
					%>
						<td align="right"><%=VTOTAL_QTY_MMBTU.elementAt(p)%></td>
						<td align="right"><%=VTOTAL_QTY_SCM.elementAt(p)%></td>
						<%if((plant_size+1) == o){
							p++;
							break;
						} %>
					<%} %>
				</tr>
					<%k++;
					break;
					} %>
				<%} %>
			</tbody>
		 </table>
		</div>
	</div>
    <% } %>
    <% } %>
    <% } %>
</body>
</html>