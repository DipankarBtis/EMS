<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>
<jsp:useBean class="com.etrm.fms.contract_mgmt.DB_ContractMgmt_Report" id="sector" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String year=request.getParameter("year")==null?"0":request.getParameter("year");
String user_cd=(String)session.getAttribute("user_cd");
String rd_val = request.getParameter("rd_val") == null?"Q":request.getParameter("rd_val");
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

sector.setCallFlag("SECTORWISE_SALES_REPORT");
sector.setComp_cd(owner_cd);
sector.setYear(year);
sector.setRd_flag(rd_val);
sector.init();

Vector VSECTOR_TYPE = sector.getVSECTOR_TYPE();
Vector VINDEX = sector.getVINDEX();
Vector VSUB_INDEX = sector.getVSUB_INDEX();
Vector VSECTOR_CD = sector.getVSECTOR_CD();
Vector VSECTOR_NAME = sector.getVSECTOR_NAME();
Vector VQTY_MMBTU_GUJ = sector.getVQTY_MMBTU_GUJ();
Vector VQTY_MT_GUJ = sector.getVQTY_MT_GUJ();
Vector VQTY_SCM_GUJ = sector.getVQTY_SCM_GUJ();
Vector V_COLOR = sector.getV_COLOR();
Vector VCOLOR = sector.getVCOLOR();
Vector VCOLOUR = sector.getVCOLOUR();
Vector VQTY_MMBTU_NONGUJ = sector.getVQTY_MMBTU_NONGUJ();
Vector VQTY_MT_NONGUJ = sector.getVQTY_MT_NONGUJ();
Vector VQTY_SCM_NONGUJ = sector.getVQTY_SCM_NONGUJ();
Vector VTOTAL_QTY = sector.getVTOTAL_QTY();
Vector VTOTAL_SCM = sector.getVTOTAL_SCM();
Vector VTOTAL_MT = sector.getVTOTAL_MT();

int duration=4;
if(rd_val.equals("M"))
{
	duration=12;
}
else
{
	duration=4;	
}
int year1 = Integer.parseInt(""+year)+1;
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>

	<table width="100%" border="1">
		<tr>
		<%if(rd_val.equals("M")){ %>
			<th nowrap="nowrap" style="font-size: 21" colspan="28" rowspan="" align="center">Sector Sales Report (Generated For year: <%=year%>-<%=year1 %> )</th>
		<%}else{ %>
			<th nowrap="nowrap" style="font-size: 21" colspan="12" rowspan="" align="center">Sector Sales Report (Generated For year: <%=year%>-<%=year1 %> )</th>
		<%} %>
		</tr>
		<tr></tr>
	</table>
	<%int i=0;int k=0; int l=0; int j=0; int m=0; int c=0; int n=0; int b=0; int d=0;
	for(j=0; j<VSECTOR_TYPE.size(); j++){
	int index = Integer.parseInt(""+VINDEX.elementAt(j));
	if(j!=0)
		{
		%>
		<div class="row">
			<div class="col-sm-12 col-xs-12 col-md-12">
				&nbsp;
			</div>
		</div> 
		<%} %>
	<div class="row m-b-5" align="center">
		<label><b><%=VSECTOR_TYPE.elementAt(j)%> : </b></label>
	</div>
	<div>
	</div>
		
		<table width="100%" border="1">
			<thead>
				<tr>
					<th rowspan="2">Sr#</th>
					<th rowspan="2">Sector</th>
					<th rowspan="2">Unit</th>
					<%if(rd_val.equals("Q"))
				 	{ %>
					<th colspan="4">Gujarat</th>
					<th colspan="4">Out Side Gujarat</th>
					<%}else if(rd_val.equals("M"))
					{%>
					<th colspan="12">Gujarat</th>
					<th colspan="12">Out Side Gujarat</th>
					<%} %>
					<th rowspan="2">Total</th>
				</tr>
				<%if(rd_val.equals("Q"))
				 { %>
				<tr>
					<th>APR - JUN</th>
					<th>JUL - SEP</th>
					<th>OCT - DEC</th>
					<th>JAN - MAR</th>
					<th>APR - JUN</th>
					<th>JUL - SEP</th>
					<th>OCT - DEC</th>
					<th>JAN - MAR</th>
				</tr>
				<%}else if(rd_val.equals("M"))
				{%>
				<tr>
					<th>APR</th>
					<th>MAY</th>
					<th>JUN</th>
					<th>JUL</th>
					<th>AUG</th>
					<th>SEP</th>
					<th>OCT</th>
					<th>NOV</th>
					<th>DEC</th>
					<th>JAN</th>
					<th>FAB</th>
					<th>MAR</th>
					<th>APR</th>
					<th>MAY</th>
					<th>JUN</th>
					<th>JUL</th>
					<th>AUG</th>
					<th>SEP</th>
					<th>OCT</th>
					<th>NOV</th>
					<th>DEC</th>
					<th>JAN</th>
					<th>FAB</th>
					<th>MAR</th>
				</tr>
				<%} %>
			</thead>
			<tbody>
			<%k=0;%>
				<%for(i=i;i<VSECTOR_CD.size(); i++){ 
				int sub_index = Integer.parseInt(""+VSUB_INDEX.elementAt(i));
				k+=1;
				%>
					<tr>
						<td rowspan="3" align="center"><%=k%></td>
						<td rowspan="3"><%=VSECTOR_NAME.elementAt(i) %></td>					    
						<td>MT</td>
						<%for(int a=0;a<duration;a++)
						{ %>
					  		<td align="right" style="background:<%=V_COLOR.elementAt(b)%>;"><%=VQTY_MT_GUJ.elementAt(b) %> </td>
						<%b++;
						} %>
						<%for(int a=0;a<duration;a++){ %>
							<td align="right" style="background:<%=VCOLOR.elementAt(l)%>;"><%=VQTY_MT_NONGUJ.elementAt(l) %></td>
						<%
						l++;} %>
						<td align="right" style="background:<%=VCOLOUR.elementAt(i)%>;"><%=VTOTAL_MT.elementAt(i) %></td>
					</tr>
					<tr>
						<td>MMSCM</td>
						<%for(int a=0;a<duration;a++){ %>
						  	<td align="right" style="background:<%=V_COLOR.elementAt(c)%>;"><%=VQTY_SCM_GUJ.elementAt(c) %> </td>
						<%c++;
						} %>
						<%for(int a=0;a<duration;a++){ %>
							<td align="right" style="background:<%=VCOLOR.elementAt(d)%>;"><%=VQTY_SCM_NONGUJ.elementAt(d) %> </td>
						<%d++;
						} %>
						<td align="right" style="background:<%=VCOLOUR.elementAt(i)%>;"><%=VTOTAL_SCM.elementAt(i) %></td>
					</tr>
					<tr>
						<td>MMBTU</td>
						<%for(int a=0;a<duration;a++)
						{%>
						  	<td align="right" style="background:<%=V_COLOR.elementAt(m)%>;"><%=VQTY_MMBTU_GUJ.elementAt(m) %> </td>
						<%m++;
						} %>
						<%for(int a=0;a<duration;a++){ %>
							<td align="right" style="background:<%=VCOLOR.elementAt(n)%>;"><%=VQTY_MMBTU_NONGUJ.elementAt(n) %></td>
						<%n++;
						} %> 
						<td align="right" style="background:<%=VCOLOUR.elementAt(i)%>;"><%=VTOTAL_QTY.elementAt(i) %></td>
					</tr>
					<%if(k==index)
					{
						i=i+1;
						break;
					}%>
				<%} %>
			
			</tbody>
		</table>
	<%} %>
</body>
</html>