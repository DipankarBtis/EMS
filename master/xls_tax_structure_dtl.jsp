<%@page import= "java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<!--Harsh Maheta 20230504 : Added Export File Functionality-->
</head>

<jsp:useBean class="com.etrm.fms.master.DataBean_Master" id="dbmaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>

<%
dbmaster.setCallFlag("TAX_STRUCTURE");
dbmaster.init();

Vector VTAX_STRUCT_CD = dbmaster.getVTAX_STRUCT_CD();
Vector VTAX_STRUCT_NM = dbmaster.getVTAX_STRUCT_NM();
Vector VTAX_STRUCT_APP_DT = dbmaster.getVTAX_STRUCT_APP_DT();
Vector VTAX_STRUCT_STATUS = dbmaster.getVTAX_STRUCT_STATUS();
Vector VTAX_STRUCT_RMK = dbmaster.getVTAX_STRUCT_RMK();
Vector VPAY_RECV= dbmaster.getVPAY_RECV();
Vector VPAY_RECV_NM= dbmaster.getVPAY_RECV_NM();

Vector VMASTER_TAX_CATEGORY = dbmaster.getVMASTER_TAX_CATEGORY();
Vector VMASTER_TAX_CATEGORY_NM = dbmaster.getVMASTER_TAX_CATEGORY_NM();

Vector VTAX_CD = dbmaster.getVTAX_CD();
Vector VTAX_NM = dbmaster.getVTAX_NM();
Vector VTAX_ALIAS_NM = dbmaster.getVTAX_ALIAS_NM();
Vector VTAX_SHT_NM = dbmaster.getVTAX_SHT_NM();
Vector VTAX_APP_DT = dbmaster.getVTAX_APP_DT();
Vector VTAX_STATUS = dbmaster.getVTAX_STATUS();
Vector VTAX_CATEGORY = dbmaster.getVTAX_CATEGORY();
Vector VTAX_CATEGORY_NM = dbmaster.getVTAX_CATEGORY_NM();
Vector VSAP_TAX_CODE = dbmaster.getVSAP_TAX_CODE();
Vector VSAP_GL = dbmaster.getVSAP_GL();

Vector VINDEX = dbmaster.getVINDEX();
Vector VCOUNT = dbmaster.getVCOUNT();

String fileName ="SEI_TaxStructureDetail.xls";
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<span style="font-weight: bold;font-size: 40px;">SEI Tax Structure Detail</span>
	<br><br>
	<%int i=0,k=0;
	for(int j=0; j<VMASTER_TAX_CATEGORY.size(); j++){ 
		int index = Integer.parseInt(""+VINDEX.elementAt(j));
	%>	
		<span style="font-weight: bold;font-size: 24px;"><%=VMASTER_TAX_CATEGORY_NM.elementAt(j) %></span>
		<table border="1">
			<thead>
				<tr>
					<th>Tax Structure<br>(Internal Code)</th>
					<th>Tax Structure</th>
					<th>Tax Category</th>
					<th>Commencement on</th>
					<th>SAP Tax Code</th>
					<th>SAP GL</th>
					<th>Status</th>
					<th>Payable / Receivable</th>
					<th>Remark</th>
				</tr>
			</thead>
			<tbody>
			<%k=0;
			if(index > 0){ %>
				<%for(i=i; i<VTAX_STRUCT_CD.size(); i++){ 
					k+=1;
				%>
				<tr>					
					<td align="center"><%=VTAX_STRUCT_CD.elementAt(i)%></td>
					<td><%=VTAX_STRUCT_NM.elementAt(i)%></td>
					<td align="center"><%=VTAX_CATEGORY_NM.elementAt(i)%></td>
					<td align="center"><%=VTAX_STRUCT_APP_DT.elementAt(i)%></td>
					<td align="center"><%=VSAP_TAX_CODE.elementAt(i)%></td>
					<td align="center"><%=VSAP_GL.elementAt(i)%></td>
					<td align="center">
						<%if(VTAX_STRUCT_STATUS.elementAt(i).equals("1")){%>
						Active
						<%}else{ %>
						In-Active
						<%} %>
					</td>
					<td align="center"><%=VPAY_RECV_NM.elementAt(i) %></td>
					<td><%=VTAX_STRUCT_RMK.elementAt(i)%></td>
				</tr>
				<%
					if(k==index)
					{
						i=i+1;
						break;
					}
				} %>
			<%}else{ %>
				<tr>
					<td align="center" colspan="9"><%=utilmsg.infoMessage("<b>No Tax Structure is Configured!</b>") %></td>
				</tr>
			<%} %>
			</tbody>
		</table>				
	<%} %>
</body>
</html>