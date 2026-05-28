<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head></head>

<jsp:useBean class="com.etrm.fms.extn_interface.DataBean_sun_interface" id="sun_master" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>

<%
String sysdt = utildate.getSysdate();
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

String entity_role = request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");

String entity_nm="";
if(entity_role.equals("C"))
{
	entity_nm="Customer";
}
else if(entity_role.equals("T"))
{
	entity_nm="Trader";
}
else if(entity_role.equals("R"))
{
	entity_nm="Transporter";
}

sun_master.setCallFlag("SUN_ENTITY_ACC_CD");
sun_master.setComp_cd(owner_cd);
sun_master.setEntity_role(entity_role);
sun_master.init();

Vector VCOUNTERPARTY_CD = sun_master.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = sun_master.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = sun_master.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_CATEGORY = sun_master.getVCOUNTERPARTY_CATEGORY();

Vector VSUN_ENTITY_ACCOUNT = sun_master.getVSUN_ENTITY_ACCOUNT();
Vector VSUN_ACCOUNT = sun_master.getVSUN_ACCOUNT();
Vector VACCOUNT_TYPE = sun_master.getVACCOUNT_TYPE();
Vector VACCOUNT_TYPE_NM = sun_master.getVACCOUNT_TYPE_NM();
Vector VPLANT_SEQ_NO = sun_master.getVPLANT_SEQ_NO();
Vector VPLANT_ABBR = sun_master.getVPLANT_ABBR();
Vector VPLANT_INDEX = sun_master.getVPLANT_INDEX();
Vector VACC_PLANT = sun_master.getVACC_PLANT();
Vector VACC_OTH_PLANT = sun_master.getVACC_OTH_PLANT();
%>

<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>

	<table width="100%" border="1">
		<tr>
			<th colspan="<%=5+VACCOUNT_TYPE.size()%>" rowspan="2" align="left">Entity Sun Account Code <%if(!entity_nm.equals("")){%>[<%=entity_nm %>]<%} %></th>
		</tr>
	</table>
	<br>
	<%if(!entity_role.equals("0")){ %>
		<table width="100%" border="1">
			<thead id="tbsearch">
				<tr>
					<th>Sr#</th>
					<th>Counterparty ABBR</th>
					<th>Counterparty Name</th>
					<th>Category</th>
					<th>SUN Account Code</th>
					<%for(int acc=0;acc<VACCOUNT_TYPE.size();acc++){%>
						<th><%=VACCOUNT_TYPE_NM.elementAt(acc) %></th>
					<%} %>
				</tr>
			</thead>
			<tbody>
				<%if(VCOUNTERPARTY_CD.size()!=0){%>
					<% int j=0,n=0,plant_count=0,t=0;
						for(int i=0; i<VCOUNTERPARTY_CD.size();i++){
						if(entity_role.equals("T"))
						{
							plant_count=Integer.parseInt(""+VPLANT_INDEX.elementAt(i));
						}
						%>
						<tr>
							<td align="center">
								<%=i+1 %>
							</td>
							<td align="center">
								<%=VCOUNTERPARTY_ABBR.elementAt(i)%>
							</td> 
							<td><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
							<td><%=VCOUNTERPARTY_CATEGORY.elementAt(i) %></td>
							<td>
								<%=VSUN_ACCOUNT.elementAt(i)%>
							</td>
							<%for(int acc=0;acc<VACCOUNT_TYPE.size();acc++){%>
								<td>
									<%=VSUN_ENTITY_ACCOUNT.elementAt(j) %>
								</td>
							<%j++;} %>
						</tr>
						<%if(entity_role.equals("T")) { %>
							<tbody id="tbody<%=i%>" >
								<tr style="text-align:center;font-weight:bold;background:#bce6ff;color:#0c63e4;">
									<td colspan="3" rowspan="<%=plant_count+1%>" align="right"  style="background:white;"></td>
									<td align="right">Trader Plant</td>
									<td align="right">Sun Account Code</td>
									<%for(int acc=0;acc<VACCOUNT_TYPE.size();acc++){%>
										<td><%=VACCOUNT_TYPE_NM.elementAt(acc) %></td>
									<%} %>
								</tr>
								<%for(int p=0;p<plant_count;p++){%>
									<tr>
										<td>
											<%=VPLANT_ABBR.elementAt(n)%>
										</td>
										<td>
											<%=VACC_PLANT.elementAt(n) %>
										</td>
										<%for(int acc=0;acc<VACCOUNT_TYPE.size();acc++){%>
											<td>
												<%=VACC_OTH_PLANT.elementAt(t++) %>
											</td>
										<%} %>
									</tr>
								<%n++;}%>
							</tbody>
						<%} %>
					<%} %>
				<%}else{%>
					<tr>
						<td colspan="<%=5+VACCOUNT_TYPE.size()%>"><%=utilmsg.infoMessage("<b>No Counterparty found!</b>") %></td>
					</tr>
				<%} %>
			</tbody>
		</table>
	<%}else{%>
		<table width="100%" border="1">
			<tr>
				<th colspan="<%=5+VACCOUNT_TYPE.size()%>" rowspan="2" align="center"><%=utilmsg.infoMessage("<b>Please Select any Entity!</b>") %></th>
			</tr>
		</table>
	<%} %>
</body>
</html>