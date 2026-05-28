<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page import="java.text.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!--Harsh Maheta 20230615 : Developed Form for Holiday Calendar-->
<head>
<style>
.inputWrapper {
    height: 32px;
    width: 32px;
    overflow: hidden;
    position: relative;
    cursor: pointer;
}
.fileInput {
    cursor: pointer;
    height: 100%;
    width: 75px;
    position:absolute;
    top: 0;
    right: 0;
    z-index: 99;
    /*This makes the button huge. If you want a bigger button, increase the font size*/
    font-size:50px;
    /*Opacity settings for all browsers*/
    opacity: 0;
    -moz-opacity: 0;
    filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0)
}
</style>

<%@ include file="../util/common_js.jsp"%>

<script type="text/javascript">
function refresh()
{
	var spot_curve_type = document.forms[0].spot_curve_type.value;
	var settlement_dt = document.forms[0].settlement_dt.value;
	var phy_fin = document.forms[0].phy_fin.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_forward_pricing_mst.jsp?spot_curve_type="+spot_curve_type+"&settlement_dt="+settlement_dt+
			"&phy_fin="+phy_fin+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function exportToXls()
{
	var spot_curve_type = document.forms[0].spot_curve_type.value;
	var settlement_dt = document.forms[0].settlement_dt.value;
	var phy_fin = document.forms[0].phy_fin.value;
	
	var url = "xls_forward_pricing_mst.jsp?fileName=Forward Pricing.xls&spot_curve_type="+spot_curve_type+
				"&settlement_dt="+settlement_dt+"&phy_fin="+phy_fin;

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.market_risk.DataBean_MarketRisk" id="dbmarket" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String spot_curve_type = request.getParameter("spot_curve_type")==null?"0":request.getParameter("spot_curve_type");
String settlement_dt = request.getParameter("settlement_dt")==null?"0":request.getParameter("settlement_dt");
String phy_fin = request.getParameter("phy_fin")==null?"":request.getParameter("phy_fin");

dbmarket.setCallFlag("FORWARD_PRICING_MST");
dbmarket.setSpot_curve_type(spot_curve_type);
dbmarket.setSettlement_dt(settlement_dt);
dbmarket.setPhy_fin(phy_fin);
dbmarket.init();

Vector VSPOT_CURVE_TYPE = dbmarket.getVSPOT_CURVE_TYPE();
Vector<String> VSPOT_REPORT_DATE = dbmarket.getVSPOT_REPORT_DATE();
Vector<String> VFORWARD_REPORT_DATE = dbmarket.getVFORWARD_REPORT_DATE();
Vector VCURVE_TYPE = dbmarket.getVCURVE_TYPE();
Vector VTEMP_CURVE_TYPE = dbmarket.getVTEMP_CURVE_TYPE();
Vector VCURVE_NM = dbmarket.getVCURVE_NM();
Vector VINDEX = dbmarket.getVINDEX();
Vector VFORWARD_REPORT_DT = dbmarket.getVFORWARD_REPORT_DT();
Vector VCURVE_DT = dbmarket.getVCURVE_DT();
Vector VCOMMODITY_TYPE = dbmarket.getVCOMMODITY_TYPE();
Vector VCURVE_UNIT = dbmarket.getVCURVE_UNIT();
Vector VPHYS_FIN = dbmarket.getVPHYS_FIN();
Vector VSETTLE_PRICE = dbmarket.getVSETTLE_PRICE();
Vector VENT_BY = dbmarket.getVENT_BY();
Vector VENT_DT = dbmarket.getVENT_DT();
Vector VMAX_CURVE_DT = dbmarket.getVMAX_CURVE_DT();
Vector VPHYS_FIN_MST = dbmarket.getVPHYS_FIN_MST();

%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_MarketRisk" enctype="multipart/form-data">
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
							Forward Pricing
						</div>
						<div onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-4 col-xs-4 col-md-4"></div>
						<div class="col-sm-4 col-xs-4 col-md-4">
						<%if(VMAX_CURVE_DT.size()!=0){ %>
							<div align="center"><%=utilmsg.infoMessage("<b>Last ZEMA Price available on "+VMAX_CURVE_DT.elementAt(0)+"!</b>")%></div>
						<%}else{ %>
							<div align="center"><%=utilmsg.infoMessage("<b>No ZEMA Price available!</b>")%></div>
						<%} %>
						</div>
					</div>&nbsp;
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Report Date</b></label>
								</div>
								<div class="col">
				      				<div class="btn-group" >
				      					<select class="form-select form-select-sm" name="settlement_dt" onchange="refresh()">
											<option value="0">--Select--</option>
											<%for(int i=0; i<VFORWARD_REPORT_DATE.size(); i++) {%>
												<option value='<%=VFORWARD_REPORT_DATE.elementAt(i)%>'><%=VFORWARD_REPORT_DATE.elementAt(i)%></option>
											<%}%>
										</select>
			    					</div>
				    				<script>
										document.forms[0].settlement_dt.value="<%=settlement_dt%>"
									</script>
				    			</div>
				    		</div>
				    	</div>
				    	<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Price Curve</b></label>&nbsp;
								</div>
								<div class="col">
									<div class="btn-group" >
										<select class="form-select form-select-sm" name="spot_curve_type" onchange="refresh()">
											<option value="0">--Select--</option>
											<%for(int i=0; i<VSPOT_CURVE_TYPE.size(); i++) {%>
												<option value='<%=VSPOT_CURVE_TYPE.elementAt(i)%>'><%=VSPOT_CURVE_TYPE.elementAt(i)%></option>
											<%}%>
										</select>
									</div>
								</div>
								<script>
									document.forms[0].spot_curve_type.value="<%=spot_curve_type%>"
								</script>
							</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Phy/Fin</b></label>
								</div>
								<div class="col">
									<div class="btn-group" >
										<select class="form-select form-select-sm" name="phy_fin" onchange="refresh()">
											<option value="">--All--</option>
											<%for(int i=0; i<VPHYS_FIN_MST.size(); i++) {%>
												<option value='<%=VPHYS_FIN_MST.elementAt(i)%>'><%=VPHYS_FIN_MST.elementAt(i)%></option>
											<%}%>
										</select>	
									</div>								
								</div>
								<script>
									document.forms[0].phy_fin.value="<%=phy_fin%>"
								</script>
							</div>
						</div>
						<%-- <div class="col-sm-6 col-xs-6 col-md-6" align="right">
							<div class="d-flex justify-content-end">
								<%if(write_access.equals("Y")){%>
								<div class="col-auto">
									<div class="btn-group">
										<div class="btn btn-outline-secondary subbtngrp" onclick="selectFile();" position:relative;" class="inputWrapper" title="Import Settlement Pricing" >
											<input class="fileInput" type="file" name="File" id="file" ><i class="fa fa-upload" aria-hidden="true"></i>&nbsp;Upload
										</div>
										<div class="btn btn-outline-secondary subbtngrp" onclick="deleteFile();" position:relative;" class="inputWrapper" title="Delete Settlement Pricing" >
											<input class="fileInput" type="file" name="File" id="file" ><i class="fa fa-trash" aria-hidden="true"></i>&nbsp; Delete
										</div>
									</div>
								</div>
								<%}%>
							</div>
						</div> --%>
					</div>
				</div>
				<div class="card-body cdbody">
					<%if(VTEMP_CURVE_TYPE.size()>0){ %>
					<%int j=0,k=0,l=0,m=0;
					for(int i=0; i<VTEMP_CURVE_TYPE.size(); i++)
					{ 
						int index=Integer.parseInt(""+VINDEX.elementAt(i));
						%>						
						<div class="row">
							<div class="col-md-12 col-sm-12 col-xs-12">
								<div class="accordion">
									<div class="accordion-item accor_item">
										<h2 class="accordion-header" id="heading<%=l%>">
	   										<button name="sub_module_cd" class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=l%>" aria-expanded="false" aria-controls="collapse<%=l%>">
								    			<%=VTEMP_CURVE_TYPE.elementAt(i)%>&nbsp;<font color="blue">(<%=index%>)</font>
								      		</button>	
								    	</h2>
										<div id="collapse<%=l%>" class="accordion-collapse collapse" aria-labelledby="heading<%=l%>">
								      		<div class="accordion-body accor-body">
								        		<div class="row">
													<div class="table-responsive">
														<table class="table table-bordered table-hover">
															<thead>
																<tr>
																	<th>Sr#</th>
																	<th>Report Date</th>
																	<th>Price curve</th>
																	<th>Commodity Type</th>
																	<th>Curve Type</th>
																	<th>Curve Unit</th>
																	<th>Physical/Financial</th>
																	<th>Curve MM/YYYY</th>
																	<th>Curve Value</th>
																	<th>Uploaded Date</th>
																	<th>Uploaded By</th>
																</tr>
															</thead>
															<tbody>
																<%k=0; for(l=l; l<VFORWARD_REPORT_DT.size(); l++)
																{
																	k+=1;
																%>
																	<tr id="r<%=i%>">
																		<td align="center"><%=k%></td>
																		<td align="center"><%=VFORWARD_REPORT_DT.elementAt(l)%></td>
																		<td align="center"><%=VCURVE_NM.elementAt(l)%></td>
																		<td align="center"><%=VCOMMODITY_TYPE.elementAt(l) %></td>
																		<td align="center"><%=VCURVE_TYPE.elementAt(l)%></td>
																		<td align="center"><%=VCURVE_UNIT.elementAt(l)%></td>
																		<td align="center"><%=VPHYS_FIN.elementAt(l) %></td>
																		<td align="center"><%=VCURVE_DT.elementAt(l) %></td>
																		<td align="center"><%=VSETTLE_PRICE.elementAt(l) %></td>
																		<td align="center"><%=VENT_DT.elementAt(l) %></td>
																		<td align="center"><%=VENT_BY.elementAt(l) %></td>
																	</tr>
																	<%if(k==index){
																		l=l+1;
																		break;} %>
																<%} %>
															</tbody>
														</table>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					<%}%>
				<%}else{ %>
					&nbsp;
					<div align="center"><%=utilmsg.infoMessage("<b>Select Forward pricing Date!</b>")%></div>
				<%} %>
				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" name="option" id="option" value="SETTLEMENT_PRICING_DTL">
<input type="hidden" name="opration" value="INSERT" id="opration">
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