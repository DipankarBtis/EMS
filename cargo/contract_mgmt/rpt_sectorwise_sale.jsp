<%@page import="org.apache.poi.util.SystemOutLogger"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ page import="java.util.*" %>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh(flag)
{
	var year = document.forms[0].year.value;
	var rd_val=flag;
	
	var u = document.forms[0].u.value;
	
	
		var url = "rpt_sectorwise_sale.jsp?year="+year+"&rd_val="+rd_val+"&u="+u;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_mgmt.DB_ContractMgmt_Report" id="sector" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String date_num = "0"; 
if(!sysdate.equals(""))
{
	String[] temp = sysdate.split("/");
	date_num=temp[0];
}
int currentYear = utildate.getCurrentYear();
String curr_year = sysdate.substring(6);
String next_year = ""+(Integer.parseInt(curr_year)+1);
String pre_year  = ""+(Integer.parseInt(curr_year)-1);
String year_2    = ""+(Integer.parseInt(curr_year)-2);
String year_3    = ""+(Integer.parseInt(curr_year)-3);
String year_4    = ""+(Integer.parseInt(curr_year)-4);
String year_5    = ""+(Integer.parseInt(curr_year)-5);
String year_6    = ""+(Integer.parseInt(curr_year)-6);

String year=request.getParameter("year")==null?"0":request.getParameter("year");
String user_cd=(String)session.getAttribute("user_cd");
String rd_val = request.getParameter("rd_val") == null?"Q":request.getParameter("rd_val");

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

%>
<body>
<%@ include file="../home/header.jsp"%>
<form action="">
	<div class="box-body">
		<div class="row">
			<div class="col-md-12 col-sm-12 col-xs-12">
				<%if(!msg.equals("")){
					if(msg_type.equals("S")){%>
						<div class="fadealert"><%=utilmsg.successMessage(msg)%></div>
					<%}else if(msg_type.equals("E")){%>
						<div class="fadealert"><%= utilmsg.errorMessage(msg)%></div>
					<%}
				} %>
				<div class="card cardmain">
					<div class="card-header cdheader">
						<div class="d-flex justify-content-between">
						    <div class="topheader">
						    	Sectorwise Sales Report
						    </div>
						    <a href="../contract_mgmt/xls_sectorwise_sales.jsp?fileName=SectorWise Sales Report.xls&company_cd=<%=comp_cd %>&year=<%=year %>&rd_val=<%=rd_val %>" download="SectorWise Sales Report.xls" >
						 		<span class="input-group-text"><i style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						 	</a>
						</div>
					</div>
					<div class="card-body cdbody">
						<div class="row">
							<div class="col-sm-3 col-xs-3 col-md-7">  
								<div class="d-flex justify-content-between">
									<div class="form-group row">
										<div class="col-auto">
											<label class="form-label"><b>Year :</b></label>
										</div>
										<div class="col-auto">
											<select class="form-select form-select-sm" name="year" id="year" onchange="refresh(document.forms[0].rd.value);">
												<option value="0">--Select--</option>
												<option value="<%=curr_year%>"><%=curr_year%>-<%=next_year.substring(2)%></option>
												<option value="<%=pre_year%>"><%=pre_year%>-<%=curr_year.substring(2)%></option>
												<option value="<%=year_2%>"><%=year_2%>-<%=pre_year.substring(2)%></option>
												<option value="<%=year_3%>"><%=year_3%>-<%=year_2.substring(2)%></option>
												<option value="<%=year_4%>"><%=year_4%>-<%=year_3.substring(2)%></option>
												<option value="<%=year_5%>"><%=year_5%>-<%=year_4.substring(2)%></option>
												<option value="<%=year_6%>"><%=year_6%>-<%=year_5.substring(2)%></option>									
											</select>
											<script>document.forms[0].year.value="<%=year%>"</script>
										</div>
									</div>
									<div align="center">
										<input type="radio" name="rd" value="Q" <%if(rd_val.equals("Q")){ %> checked="checked" <%} %> onclick="refresh('Q')" >&nbsp;<b>Quaterly</b>&nbsp; &nbsp; &nbsp;
						    			<input type="radio" name="rd" value="M" <%if(rd_val.equals("M")){ %> checked="checked" <%} %> onclick="refresh('M')">&nbsp;<b>Monthly</b> &nbsp; &nbsp;
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="card-body cdbody">
					<%int i=0;int k=0; int l=0; int j=0; int m=0; int c=0; int n=0; int b=0; int d=0;
					for(j=0; j<VSECTOR_TYPE.size(); j++){
					int index = Integer.parseInt(""+VINDEX.elementAt(j));%>
					<% if(j!=0)
						{%>
							<div class="row">
								<div class="col-sm-12 col-xs-12 col-md-12">&nbsp;
								</div>
							</div> 
						<%} %>
							<div class="row m-b-5">
								<label class="form-label subheader"><i class="fa fa-snowflake-o"></i>&nbsp;<%=VSECTOR_TYPE.elementAt(j)%></label>
							</div>
						<%if(index>0 && !year.equals("0") ){ %>
							<div class="row">
								<div class="table-responsive">
									<table class="table table-bordered table-hover">
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
								</div>
							</div>
						<%}else{ %>
							<div colspan="22" align="center"><%=utilmsg.infoMessage("<b>No Sector Data is Available!</b>") %></div>
						<%} %>
					<%} %>
					</div>
				</div>
			</div>
		</div>
	</div>	
<input type="hidden" name="sysdate" value="<%=sysdate%>">
<input type="hidden" name="read_access" value="<%=read_access%>">
<input type="hidden" name="write_access" value="<%=write_access%>">
<input type="hidden" name="check_access" value="<%=check_access%>">
<input type="hidden" name="print_access" value="<%=print_access%>">
<input type="hidden" name="delete_access" value="<%=delete_access%>">  	
<input type="hidden" name="audit_access" value="<%=audit_access%>">
<input type="hidden" name="authorize_access" value="<%=authorize_access%>">
<input type="hidden" name="approve_access" value="<%=approve_access%>">
<input type="hidden" name="execute_access" value="<%=execute_access%>">
<input type="hidden" name="form_cd" value="<%=formCd%>">
<input type="hidden" name="form_nm" value="<%=formNm%>">
<input type="hidden" name="mod_cd" value="<%=mod_cd%>">
<input type="hidden" name="mod_nm" value="<%=mod_nm%>">
<input type="hidden" name="u" value="<%=u%>">

</form>
</body>
</html>