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

sun_master.setComp_cd(owner_cd);
sun_master.setCallFlag("TAX_STRUCTURE_SUN");
sun_master.init();

Vector VMASTER_TAX_CATEGORY = sun_master.getVMASTER_TAX_CATEGORY();
Vector VMASTER_TAX_CATEGORY_NM = sun_master.getVMASTER_TAX_CATEGORY_NM();

Vector VCO_CD = sun_master.getVCO_CD();
Vector VBU_SEQ = sun_master.getVBU_SEQ();
Vector VBU_ABBR = sun_master.getVBU_ABBR();
Vector VBU_STATE = sun_master.getVBU_STATE();

Vector VTAX_STRUCT_CD = sun_master.getVTAX_STRUCT_CD();
Vector VTAX_STRUCT_NM = sun_master.getVTAX_STRUCT_NM();
Vector VTAX_CATEGORY_NM = sun_master.getVTAX_CATEGORY_NM();
Vector VTAX_STRUCT_APP_DT = sun_master.getVTAX_STRUCT_APP_DT();
Vector VTAX_STRUCT_STATUS = sun_master.getVTAX_STRUCT_STATUS();
Vector VPAY_RECV_NM = sun_master.getVPAY_RECV_NM();
Vector VTAX_STRUCT_RMK = sun_master.getVTAX_STRUCT_RMK();
Vector VINDEX = sun_master.getVINDEX();
Vector VTAX_COUNT = sun_master.getVTAX_COUNT();
Vector VSUB_TAX_STRUCT_NM = sun_master.getVSUB_TAX_STRUCT_NM();
Vector VSUN_CD = sun_master.getVSUN_CD();
Vector VSUG_CD = sun_master.getVSUG_CD();
Vector VSUB_SUN_CD = sun_master.getVSUB_SUN_CD();
Vector VSUB_SUG_CD = sun_master.getVSUB_SUG_CD();
%>

<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>

	<table width="100%" border="1">
		<tr>
			<th colspan="<%=8+VBU_SEQ.size()%>" rowspan="2" align="left">Tax Sun Account Code</th>
		</tr>
	</table>
	<br>

	<%
	int i=0,k=0,sub=0,c=0,p=0;
	for(int j=0; j<VMASTER_TAX_CATEGORY.size(); j++){ 
		int index = Integer.parseInt(""+VINDEX.elementAt(j));
	%>
			<table width="100%" border="1">
				<tr>
				<%if(j==0){%>
					<th colspan="<%=8+VBU_SEQ.size()%>" rowspan="2" align="left"> <%=VMASTER_TAX_CATEGORY_NM.elementAt(j) %></th>
				<%} else{%>
					<th colspan="<%=8+(VBU_SEQ.size()*2)%>" rowspan="2" align="left"> <%=VMASTER_TAX_CATEGORY_NM.elementAt(j) %></th>
				<%} %>
				</tr>
			</table>

			<table width="100%" border="1">
				<thead id="tbsearch">
					<tr>
						<th rowspan="2">Sr#</th>
						<th rowspan="2">Tax Structure<br>(Internal Code)</th>
						<th rowspan="2">Tax Structure</th>
						<th rowspan="2">Tax Category</th>
						<th rowspan="2">Commencement on</th>
						<th rowspan="2">Status</th>
						<th rowspan="2">Payable / Receivable</th>
						<th rowspan="2">Remark</th>
						<%for(int bu_seq=0;bu_seq<VBU_SEQ.size();bu_seq++){%>
							<th <%if(j==0){%>colspan="1"<%}else{%>colspan="2"<%} %>>
								<%=VCO_CD.elementAt(bu_seq)%>-<%=VBU_ABBR.elementAt(bu_seq)%><br>(<%=VBU_STATE.elementAt(bu_seq)%>)
							</th>
						<%} %>
					</tr>

					<tr>
						<%for(int bu_seq=0;bu_seq<VBU_SEQ.size();bu_seq++){%>
							<th>SUN Account Code</th>
							<% if(j != 0) { %>
								<th>SUG Account Code</th>
							<% } %>
						<%} %>
					</tr>
				</thead>

				<tbody>
				<%k=0;
				if(index > 0){ %>
					<%for(i=i; i<VTAX_STRUCT_CD.size(); i++){ 
						k+=1;
						int size =Integer.parseInt(""+VTAX_COUNT.elementAt(i));
					%>
						<tr>
							<td align="center"><%=k %></td>
							<td align="center"><%=VTAX_STRUCT_CD.elementAt(i)%></td>
							<td><%=VTAX_STRUCT_NM.elementAt(i)%></td>
							<td align="center"><%=VTAX_CATEGORY_NM.elementAt(i)%></td>
							<td align="center"><%=VTAX_STRUCT_APP_DT.elementAt(i)%></td>
							<td align="center">
								<div align="center">
									<font style="color:<%if(VTAX_STRUCT_STATUS.elementAt(i).equals("1")){%>#a6ff4d<%}else{%>red<%}%>">
										<i class="fa fa-circle fa-lg" ></i>
										&nbsp;
									</font>
									<%if(VTAX_STRUCT_STATUS.elementAt(i).equals("1")){%>
										Active
									<%}else{ %>
										In-Active
									<%} %>
								</div>
							</td>
							<td align="center"><%=VPAY_RECV_NM.elementAt(i) %></td>
							<td><%=VTAX_STRUCT_RMK.elementAt(i)%></td>
							<%for(int bu_seq=0;bu_seq<VBU_SEQ.size();bu_seq++){%>
								<%if(size>1){ %>
									<td></td>
								<%}else{%>
									<td><%=VSUN_CD.elementAt(p)%></td>
									<% if(j != 0) { %>
										<td ><%=VSUG_CD.elementAt(p)%></td>
									<% } %>
								<%} %>
								<% p++; %>
							<%} %>
						</tr>

						<%if(size>1){ %>
						    <tbody id="tbody<%=i%>">
						        <%for(int n=0;n<size;n++){ %>
						            <tr>
						                <td colspan="8" align="right"><%=VSUB_TAX_STRUCT_NM.elementAt(sub) %></td>
						                <%for(int bu_seq=0; bu_seq<VBU_SEQ.size(); bu_seq++){%>
						                    <td><%=VSUB_SUN_CD.elementAt(c) %></td>
						                    <% if(j != 0) { %>
						                        <td><%=VSUB_SUG_CD.elementAt(c)%></td>
						                    <% } %>
						                    <% c++; %>
						                <%} %>
						            </tr>
						        <%sub+=1;} %>
						    </tbody>
						<%} %>

						<%
							if(k==index)
							{
								i=i+1;
								break;
							}
						%>
					<%} %>
				<%}else{%>
					<tr>
						<td align="center" colspan="<%=(8+VBU_SEQ.size())%>"><%=utilmsg.infoMessage("<b>No Tax Structure is Configured!</b>") %></td>
					</tr>
				<%} %>
				</tbody>
			</table>
			<table>
					<tr>
						<td align="center" colspan="<%=(8+VBU_SEQ.size())%>"></td>
					</tr>
			</table>
	<%}%>
</body>
</html>
