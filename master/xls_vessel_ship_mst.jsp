<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Master" id="dbcargo" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String ship_cd = request.getParameter("ship_cd")==null?"0":request.getParameter("ship_cd");
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

dbcargo.setCallFlag("VESSEL_MST");
dbcargo.setComp_cd(owner_cd);
dbcargo.init();

String ship_name = dbcargo.getShip_name();
String ship_call_sign = dbcargo.getShip_call_sign();
String ship_flag = dbcargo.getShip_flag();
String ship_imo_no = dbcargo.getShip_imo_no();
String ship_class_soc = dbcargo.getShip_class_soc();
String inmarsat_no = dbcargo.getInmarsat_no();
String ship_owner_name = dbcargo.getShip_owner_name();
String ship_operator_name = dbcargo.getShip_operator_name();
String ship_fax_no = dbcargo.getShip_fax_no();
String ship_telex_no = dbcargo.getShip_telex_no();
String ship_email = dbcargo.getShip_email();
String gross_tonnage = dbcargo.getGross_tonnage();
String cargo_capacity = dbcargo.getCargo_capacity();
String volume_unit = dbcargo.getVolume_unit();
String percentage_capacity = dbcargo.getPercentage_capacity();
String ship_item = dbcargo.getShip_item();

Vector VSHIP_CD = dbcargo.getVSHIP_CD();
Vector VSHIP_NAME = dbcargo.getVSHIP_NAME();
Vector VSHIP_CALL_SIGN = dbcargo.getVSHIP_CALL_SIGN();
Vector VSHIP_FLAG = dbcargo.getVSHIP_FLAG();
Vector VSHIP_IMO_NO = dbcargo.getVSHIP_IMO_NO();
Vector VSHIP_CLASS_SOC = dbcargo.getVSHIP_CLASS_SOC();
Vector VINMARSAT_NO = dbcargo.getVINMARSAT_NO();
Vector VSHIP_OWNER_NAME = dbcargo.getVSHIP_OWNER_NAME();
Vector VSHIP_OPERATOR_NAME = dbcargo.getVSHIP_OPERATOR_NAME();
Vector VSHIP_FAX_NO = dbcargo.getVSHIP_FAX_NO();
Vector VSHIP_TELEX_NO = dbcargo.getVSHIP_TELEX_NO();
Vector VSHIP_EMAIL = dbcargo.getVSHIP_EMAIL();
Vector VGROSS_TONNAGE = dbcargo.getVGROSS_TONNAGE();
Vector VCARGO_CAPACITY = dbcargo.getVCARGO_CAPACITY();
Vector VVOLUME_UNIT = dbcargo.getVVOLUME_UNIT();
Vector VPERCENTAGE_CAPACITY = dbcargo.getVPERCENTAGE_CAPACITY();
Vector VSHIP_ITEM = dbcargo.getVSHIP_ITEM();
Vector VSHIP_EFF_DT = dbcargo.getVSHIP_EFF_DT();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="16" rowspan="2" align="left">Vessel Master Report</th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead >
			<tr valign="top">
				<th>Sr#</th>
				<th >Vessel Name</th>
				<th >Eff Date</th>
				<th >Call Sign</th>
				<th >Flag</th>
				<th >IMO No#</th>
				<th >Class SOC</th>
				<th >Inmarsat No#</th>
				<th >Vessel Owner</th>
				<th >Vessel Operator</th>
				<th >FAX No#</th>
				<th >Telex No#</th>
				<th >Vessel's Email</th>
				<th >Gross Tonnage</th>
				<th >Approx Cargo Capecity</th>
				<th >Items</th>
			</tr>
		</thead>
		<tbody>
		<%if(VSHIP_CD.size() > 0){ %>
			<%for(int i=0; i<VSHIP_CD.size(); i++){ %>
			<tr>
				<td><%=i+1%></td>
				<td><%=VSHIP_NAME.elementAt(i)%></td>
				<td align="center"><%=VSHIP_EFF_DT.elementAt(i)%></td>
				<td><%=VSHIP_CALL_SIGN.elementAt(i)%></td>
				<td><%=VSHIP_FLAG.elementAt(i)%></td>
				<td><%=VSHIP_IMO_NO.elementAt(i)%></td>
				<td><%=VSHIP_CLASS_SOC.elementAt(i)%></td>
				<td align="left"><%=VINMARSAT_NO.elementAt(i)%></td>
				<td><%=VSHIP_OWNER_NAME.elementAt(i)%></td>
				<td><%=VSHIP_OPERATOR_NAME.elementAt(i)%></td>
				<td><%=VSHIP_FAX_NO.elementAt(i)%></td>
				<td><%=VSHIP_TELEX_NO.elementAt(i)%></td>
				<td><%=VSHIP_EMAIL.elementAt(i)%></td>
				<td align="right"><%=VGROSS_TONNAGE.elementAt(i)%></td>
				<td align="right"><%=VCARGO_CAPACITY.elementAt(i)%> 
					<%if(VVOLUME_UNIT.elementAt(i).equals("1")){ %>
						SCM
					<%}else if(VVOLUME_UNIT.elementAt(i).equals("2")){ %>
						MMSCM
					<%}else if(VVOLUME_UNIT.elementAt(i).equals("3")){ %>
						MT
					<%} %>@ <%=VPERCENTAGE_CAPACITY.elementAt(i) %>%</td>
				<td align="center"><%=VSHIP_ITEM.elementAt(i)%></td>
			</tr>
			<%} %>
		<%}else{ %>
			<tr>
				<td colspan="16" align="center">
					<b>Ship List is not Available!</b>
				</td>
			</tr>
		<%} %>
		</tbody>
	</table>
	
</body>
</html>