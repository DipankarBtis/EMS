<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page import="java.text.*"%>
<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!--Harsh Maheta 20230701 : Developed Form for Settlement Pricing-->
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
	var price_curve_type = document.forms[0].price_curve_type.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_settlement_pricing_mst.jsp?spot_curve_type="+spot_curve_type+
			"&price_curve_type="+price_curve_type+
			"&from_dt="+from_dt+"&to_dt="+to_dt+
			"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function importData()
{
  let input = document.createElement('input');
  input.type = 'file';
  input.onchange = _ => 
  {
    // you can use this method to get file and perform respective operations
            let files =   Array.from(input.files);
  };
}

function upload()
{
	var opration = document.forms[0].opration.value;
	var radios = document.getElementsByName("radio_btn");
	document.forms[0].opration.value="UPLOAD";
	
	if(opration)
	{
		var file_dtl_array = document.forms[0].radio_btn.value.split(",");
		var file_nm = file_dtl_array[0];
		var file_size = file_dtl_array[1];
		var upload_report_dt = document.forms[0].upload_report_dt.value;
		
		if(file_nm.includes(".xml"))
		{
			var upload = confirm("You are about to import ZEMA pricing in System! \n\nFile : "+file_nm+"\nFile Size : "+file_size+" Bytes"+" \nReport date "+upload_report_dt+" \n\nAre you sure want to proceed?")
			if(upload==true)
			{
				document.forms[0].submit();
			}
			else
			{
				alert("File Uploading Cancled!!!");
			}
		}
		else
		{
			var rejact = alert("Uploading file should be in .xml format only!!!!")
		}
	}		
}

function showFileName(input) 
{
    var fileNameElement = document.getElementById("fileName");
    if (input.files && input.files.length > 0) 
    {
    	var file = input.files[0];
    	var opration = document.forms[0].opration.value;
    	var file_nm = document.getElementById('fileInput').value;
    	
    	document.forms[0].opration.value="IMPORT";
        
    	fileNameElement.textContent = input.files[0].name;
        document.getElementById("fileName").style.backgroundColor = "#99ffcc";
        document.getElementById("table").style.visibility = "visible";
        
        var importt = confirm("You are about to Upload "+file.name+" \n\nAre you sure want to proceed?")
		if(importt==true)
		{
			if(file_nm.includes(".xml"))
			{
				document.forms[0].submit();
				document.getElementById("table").style.visibility = "visible";
			}
			else
			{
				alert("Uploading file must be in .xml format only!!!!");
			}
		}
    } 
    else 
    {
        fileNameElement.textContent = "No File Selected";
    }
}
function deleteFile()
{
	var opration = document.forms[0].opration.value;
	document.forms[0].opration.value="DELETE";
	
	if(opration)
	{
		var delete_report_Dt = document.forms[0].delete_report_Dt.value;
		
		var deletefile = confirm("Your action to remove ZEMA Pricing for the"+" Report date : "+delete_report_Dt+" will be reported. \n\nAre you sure want to proceed?")
		if(deletefile==true)
		{
			document.forms[0].submit();
		}
		else
		{
			alert("Price Deletion Cancled!!!");
		}
	}		
}
function uploadDt_Change(obj)
{
	var upload_repdt = obj.value;
	document.forms[0].upload_report_dt.value=upload_repdt;
	
	//alert(upload_repdt);
}

function exportToXls()
{
	var spot_curve_type = document.forms[0].spot_curve_type.value;
	var price_curve_type = document.forms[0].price_curve_type.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var url = "xls_settlement_pricing_mst.jsp?fileName=SPOT (Settlement) Pricing.xls&spot_curve_type="+spot_curve_type+
			"&price_curve_type="+price_curve_type+
			"&from_dt="+from_dt+"&to_dt="+to_dt;

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.market_risk.DataBean_MarketRisk" id="dbmarket" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>

<%
String sysdate=utildate.getSysdate();
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

String spot_curve_type = request.getParameter("spot_curve_type")==null?"0":request.getParameter("spot_curve_type");
String price_curve_type = request.getParameter("price_curve_type")==null?"0":request.getParameter("price_curve_type");
String settlement_dt = request.getParameter("settlement_dt")==null?"0":request.getParameter("settlement_dt");
String upload_report_dt = request.getParameter("upload_report_dt")==null?sysdate:request.getParameter("upload_report_dt");
String delete_report_Dt = request.getParameter("delete_report_Dt")==null?"":request.getParameter("delete_report_Dt");

HttpServletRequest req = request;
dbmarket.setRequest(req);

dbmarket.setCallFlag("SETTLEMENT_PRICING_MST");
dbmarket.setSpot_curve_type(spot_curve_type);
dbmarket.setPrice_curve_type(price_curve_type);
dbmarket.setSettlement_dt(settlement_dt);
dbmarket.setUpload_report_dt(upload_report_dt);
dbmarket.setDelete_report_Dt(delete_report_Dt);
dbmarket.setFrom_dt(from_dt);
dbmarket.setTo_dt(to_dt);
dbmarket.init();

Vector VSPOT_CURVE_TYPE = dbmarket.getVSPOT_CURVE_TYPE();
Vector<String> VSPOT_REPORT_DATE = dbmarket.getVSPOT_REPORT_DATE();
Vector VPRICE_CURVE_TYPE = dbmarket.getVPRICE_CURVE_TYPE();
Vector VCURVE_TYPE = dbmarket.getVCURVE_TYPE();
Vector VACTUAL_CURVE_TYPE = dbmarket.getVACTUAL_CURVE_TYPE();
Vector VTEMP_CURVE_TYPE = dbmarket.getVTEMP_CURVE_TYPE();
Vector VCURVE_NM = dbmarket.getVCURVE_NM();
Vector VINDEX = dbmarket.getVINDEX();
Vector VSPOT_REPORT_DT = dbmarket.getVSPOT_REPORT_DT();
Vector VCURVE_DT = dbmarket.getVCURVE_DT();
Vector VCOMMODITY_TYPE = dbmarket.getVCOMMODITY_TYPE();
Vector VCURVE_UNIT = dbmarket.getVCURVE_UNIT();
Vector VPHYS_FIN = dbmarket.getVPHYS_FIN();
Vector VSETTLE_PRICE = dbmarket.getVSETTLE_PRICE();
Vector VENT_BY = dbmarket.getVENT_BY();
Vector VENT_DT = dbmarket.getVENT_DT();
Vector VMAX_CURVE_DT = dbmarket.getVMAX_CURVE_DT();
Vector VFILE_NM = dbmarket.getVFILE_NM();
Vector VFILE_SIZE = dbmarket.getVFILE_SIZE();
Vector VFILE_UP_BY = dbmarket.getVFILE_UP_BY();
Vector VFILE_UP_ON = dbmarket.getVFILE_UP_ON();
Vector VSETTLE_PRICE_AVG = dbmarket.getVSETTLE_PRICE_AVG();


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
				<%}else if(msg_type.equals("W")){%>
					<div class="fadealert"><%= utilmsg.warningMessage(msg)%></div>
				<%}
			} %>
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
						<div class="topheader">
							SPOT (Settlement) Pricing
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
						<div class="col-sm-4 col-xs-4 col-md-4" align="right">
							<div class="d-flex justify-content-end">
								<%if(write_access.equals("Y")){%>
								<div class="col-auto">
									<div class="btn-group">
										<div data-bs-toggle="modal" data-bs-target="#uploadFile"  class="btn btn-outline-secondary subbtngrp" position:relative;" class="inputWrapper" title="Upload Settlement Pricing" onclick="fetchFileDtl();">
											<label><i class="fa fa-upload" aria-hidden="true"></i>&nbsp;Upload</label>
										</div>
										<div data-bs-toggle="modal" data-bs-target="#deleteFile" class="btn btn-outline-secondary subbtngrp" position:relative;" class="inputWrapper" title="Delete Settlement Pricing" onclick="fetchPriceDtl();" >
											<label><i class="fa fa-trash" aria-hidden="true"></i>&nbsp;Delete</label>
										</div>
									</div>
								</div>
								<%}%>
							</div>
						</div>
					</div>&nbsp;
					<div class="row m-b-5">
						<%-- <div class="col-sm-3 col-xs-3 col-md-3" align="left">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Report Date</b></label>&nbsp;
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
				      					<select class="form-select form-select-sm" name="settlement_dt" onchange="refresh()">
											<option value="0">--Select--</option>
											<%for(int i=0; i<VSPOT_REPORT_DATE.size(); i++) {%>
												<option value='<%=VSPOT_REPORT_DATE.elementAt(i)%>'><%=VSPOT_REPORT_DATE.elementAt(i)%></option>
											<%}%>
										</select>
			    					</div>
				    				<script>
										document.forms[0].settlement_dt.value="<%=settlement_dt%>"
									</script>
				    			</div>
				    		</div>
				    	</div> --%>
				    	<div class="col-sm-4 col-xs-4 col-md-4" align="left">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Product Type</b></label>
								</div>
								<div class="col">
									<div class="btn-group">
										<select class="form-select form-select-sm" name="spot_curve_type" onchange="refresh()">
											<option value="0">--All--</option>
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
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Price Curve</b></label>
								</div>
								<div class="col">
									<div class="btn-group">
										<select class="form-select form-select-sm" name="price_curve_type" onchange="refresh()">
											<option value="0">--All--</option>
											<%for(int i=0; i<VPRICE_CURVE_TYPE.size(); i++) {%>
												<option value='<%=VPRICE_CURVE_TYPE.elementAt(i)%>'><%=VPRICE_CURVE_TYPE.elementAt(i)%></option>
											<%}%>
										</select>
									</div>
								</div>
								<script>
									document.forms[0].price_curve_type.value="<%=price_curve_type%>"
								</script>
							</div>
						</div>				    	
				    	<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="d-flex justify-content-start">
								<div class="form-group row">
									<div class="col-auto">
										<label class="form-label"><b>From</b></label>
									</div>
									<div class="col-auto">
										<div class="input-group input-group-sm" >
											<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
											onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
											<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
										</div>
									</div>
									<div class="col-auto">
										<label class="form-label"><b>To</b></label>
									</div>
									<div class="col-auto">
										<div class="input-group input-group-sm" >
											<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
											onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
											<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
										</div>
									</div>
								</div>
							</div>
						</div>
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
								    			&nbsp;<font style="color: #055160; background-color: #cff4fc; border-color: #b6effb; padding: 5px; border-radius: 5px;">(Avg:<%=VSETTLE_PRICE_AVG.elementAt(i)%>)</font>
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
																	<th>Product Type</th>
																	<th>Price curve</th>
																	<th>Commodity Type</th>
																	<th>Curve Type</th>
																	<th>Curve Unit</th>
																	<th>Financial Curve</th>
																	<th>Settle Date</th>
																	<th>Curve Value</th>
																	<th>Uploaded Date</th>
																	<th>Uploaded By</th>
																</tr>
															</thead>
															<tbody>
																<%k=0; for(l=l; l<VSPOT_REPORT_DT.size(); l++)
																{
																	k+=1;
																%>
																	<tr id="r<%=i%>">
																		<td align="center"><%=k%></td>
																		<td align="center"><%=VCURVE_NM.elementAt(l)%></td>
																		<td align="center"><%=VACTUAL_CURVE_TYPE.elementAt(l)%></td>
																		<td align="center"><%=VCOMMODITY_TYPE.elementAt(l) %></td>
																		<td align="center"><%=VCURVE_TYPE.elementAt(l)%></td>
																		<td align="center"><%=VCURVE_UNIT.elementAt(l)%></td>
																		<td align="center"><%=VPHYS_FIN.elementAt(l) %></td>
																		<td align="center"><%=VSPOT_REPORT_DT.elementAt(l) %></td>
																		<td align="right"><%=VSETTLE_PRICE.elementAt(l) %></td>
																		<td align="center"><%=VENT_DT.elementAt(l) %></td>
																		<td align="center"><%=VENT_BY.elementAt(l) %></td>
																	</tr>
																	<%if(k==index){%>
																		<tr>
																			<td colspan="8" align="right"><b>Average :</b></td>
																			<td align="right" style="background-color: #cff4fc; border-color: #b6effb; color: #055160;"><b><%=VSETTLE_PRICE_AVG.elementAt(i) %></b></td>
																			<td colspan="2"></td>
																		</tr>
																		<%l=l+1;
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
					<div align="center"><%=utilmsg.infoMessage("<b>Settlement is not Available!</b>")%></div>
				<%} %>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="uploadFile" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Select XML File for Upload
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="card-body cdbody">
					<div class="row m-b-5">
						<div id="avl_price_dt" style="display: none;" align="center"></div>&nbsp;
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Step 1 : Upload ZEMA XML File</label>&nbsp;
					</div>
					<div class="row">
		    			<div class="col-sm-2 col-xs-2 col-md-2" align="right">
							<div class="d-flex ju	stify-content-front">
								<div class="col-auto">
									<div class="btn-group">
										<div class="btn btn-outline-secondary subbtngrp" onclick="importData();" class="inputWrapper" title="Import ZEMA XML File" >
											<input onchange="showFileName(this)" class="fileInput" type="file" name="fileInput" id="fileInput" ><i class="fa fa-upload" aria-hidden="true"></i>&nbsp;Choose File										
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3" align="right">
							<div class="d-flex justify-content-front">
								<div>
									<span class="fileName" id="fileName">No File Selected</span>
								</div>
							</div>
						</div>
	    			</div>
		    	</div>
		    	<div class="card-body cdbody">
	    			<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Step 2 : Manual ZEMA Price Upload Process</label>&nbsp;
					</div>
					<div class="row m-b-5">
						<div class="col-md-4 col-sm-4 col-xs-4"></div>
						<div class="col-auto">
							<label class="form-label"><b>Report Date</b></label>
						</div>
						<div class="col-md-3 col-sm-3 col-xs-3">
							<div class="col-auto">
			      				<div class="input-group input-group-sm" >
			   						<input type="text" class="form-control form-control-sm date fmsdtpick" name="upload_report_dt" size="10" maxLength="10" onchange="uploadDt_Change(this);fetchFileDtl();" onblur="if(validateDate(this)){};" >
			   						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			    				</div>
			    				<script>
									document.forms[0].upload_report_dt.value="<%=upload_report_dt%>"
								</script>
			    			</div>
		    			</div>
					</div>&nbsp;
		    		<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table id="table" class="table table-bordered">
									<thead id="table_head">
										<tr>
											<th></th>
											<th>Name</th>
											<th>Size</th>
											<th>Updated On</th>
										</tr>
									</thead>
									<%for(int i=0;i<VFILE_NM.size();i++)
									  {
										%>
									<tbody id="table_body">
										<tr>
											<td><input id="radio_btn_id<%=i%>" type="radio" name="radio_btn" onclick="upload();" title="Upload ZEMA XML File" ></td>
											<td id="td_file_nm<%=i%>" name="td_file_nm_Nm"></td>
											<td id="td_file_size<%=i%>" name="td_file_size_Nm"></td>
											<td id="td_file_up_on<%=i%>" name="td_file_upon_Nm"></td>
										</tr>
									<%}%>
									</tbody>
								</table>
								<table class="table table-bordered" id="no_file_table" style="visibility: hidden;">
									<tr>
										<td colspan="4"><div align="center"><%=utilmsg.infoMessage("<b>No Files Are Available!</b>")%></div></td>
									</tr>
								</table>
							</div>
						</div>
					</div>
    			</div>
      		</div>
      	</div>
	</div>
</div>
<div class="modal fade" id="deleteFile" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Select XML File for Delete
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="card-body cdbody">
					<div class="row m-b-5">
						<%if(VMAX_CURVE_DT.size()!=0) { %>
							<div align="center"><%=utilmsg.infoMessage("<b>Deletion allowed only for last available Report date : "+VMAX_CURVE_DT.elementAt(0)+"</b>")%></div>
						<%}else{%>
							<div align="center"><%=utilmsg.infoMessage("<b>No Zema pricing available in system!</b>")%></div>
						<%} %>
					</div>
					<div class="row m-b-5">
						<div class="col-md-4 col-sm-4 col-xs-4"></div>
						<div class="col-auto">
							<label class="form-label"><b>Report Date</b></label>
						</div>
						<div class="col-md-3 col-sm-3 col-xs-3">
							<div class="col-auto">
			      				<div class="input-group input-group-sm" >
			   						<input <%if(VMAX_CURVE_DT.size()!=0) { %>value="<%=VMAX_CURVE_DT.elementAt(0)%>"<%} %> disabled="disabled" type="text" class="form-control form-control-sm date fmsdtpick" name="delete_reportDt" size="10" maxLength="10" onchange="refresh();" onblur="if(validateDate(this)){};" >
			   						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			    					<input type="hidden" name="delete_report_Dt" >
			    				</div>
			    				<%if(VMAX_CURVE_DT.size()!=0) { %>
			    				<script>
									document.forms[0].delete_report_Dt.value="<%=VMAX_CURVE_DT.elementAt(0)%>"
								</script><%} %>
			    			</div>
		    			</div>
		    		</div>
		    	</div>
	    		<div class="card-body cdbody">
	    			<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Manual ZEMA Price Removal Process</label>&nbsp;
					</div>
		    		<div class="row m-b-5">
			    		<div class="form-group row">
			    			<div class="col-md-3 col-sm-3 col-xs-3">  
				    			<label><b>Total #Forward Price :</b></label>&nbsp;<label id="Total_Forward_Price"></label>
							</div>
							<div class="col-md-3 col-sm-3 col-xs-3">  
					    		<label><b>Total #Settle Price :</b></label>&nbsp;<label id="Total_Settle_Price"></label>
							</div>
						</div>
					</div>
    			</div>
				<div class="card-footer cdfooter text-center">
					<div class="" align="right">
						<input type="button" class="btn btn-warning com-btn" value="Delete" onclick="deleteFile();">
					</div>
				</div>
      		</div>
      	</div>
	</div>
</div>
<input type="hidden" name="option" id="option" value="SETTLEMENT_FORWARD_PRICING_DTL">
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

<script>
function fetchPriceDtl()
{
	var delete_report_Dt = document.forms[0].delete_report_Dt.value;
	$.post("../servlet/DB_MarketRisk_Ajax?delete_report_Dt="+delete_report_Dt+"&setCallType=PRICE_DTL", function(responseJson) {
		$.each(responseJson, function(index, json) {
			var display1="";
			var display2="";
			display1+="<label><b>"+json.FORWARD_AVAILABLE_DT+"</b></label>";
			display2+="<label><b>"+json.SPOT_AVAILABLE_DT+"</b></label>";
			document.getElementById('Total_Forward_Price').innerHTML=display1;
			document.getElementById('Total_Settle_Price').innerHTML=display2;
		});
	});
}
function fetchFileDtl()
{
	var upload_report_dt = document.forms[0].upload_report_dt.value;
	$.post("../servlet/DB_MarketRisk_Ajax?upload_report_dt="+upload_report_dt+"&setCallType=FILE_DTL", function(responseJson) {
		$.each(responseJson, function(index, json) {
			var display1="";
			var display2="";
			document.forms[0].upload_report_dt.value=upload_report_dt;
			
			var upload_dt = upload_report_dt;
			var updt_Array = upload_report_dt.split("/");
			var split_updt_Array = updt_Array[2]+updt_Array[1]+updt_Array[0];
			
			var btnFlag = json.BTNFLAG;
			
			display1+="<div class='container'><div class='alert alert-info'><i class='fa fa-info-circle fa-lg'></i>&nbsp;"+"<b>ZEMA Pricing available in system for "+upload_dt+"! Try again after removing existing data!!</b>"+"</div></div>"
        	document.getElementById('avl_price_dt').innerHTML = display1;
			if (btnFlag == "f")
            {
				document.getElementById('avl_price_dt').style.display = 'none';
            }
			if(json.FILE_NM_DTL.length!=0)
			{
				const initialLength = json.FILE_NM_DTL.length;
				const totalRows = Math.max(initialLength,json.FILE_NM_DTL.length+1);
				const table = document.getElementById('table');
				
				for(let i = 0; i < json.FILE_NM_DTL.length; i++)
				{
					table.rows[i+1].style.display = '';

					document.getElementById('table').style.visibility = 'visible';
					document.getElementById('no_file_table').style.visibility = 'hidden';
					
					var myArray1 = String(json.FILE_NM_DTL).split(",");
					let file_name = myArray1[i];
					document.getElementById('td_file_nm'+i).innerHTML=file_name;
					
					var myArray2 = String(json.FILE_SIZE_DTL).split(",");
					let file_size = myArray2[i];
					document.getElementById('td_file_size'+i).innerHTML=file_size+" Bytes";
					
					var myArray3 = String(json.FILE_UPDT_DTL).split(",");
					let file_updt = myArray3[i];
					document.getElementById('td_file_up_on'+i).innerHTML=file_updt;
					
					const file_dtl_array = [file_name,file_size];
					document.getElementById('radio_btn_id'+i).value=file_dtl_array;
					
                    if (btnFlag == "t") 
					{
		                document.getElementById('avl_price_dt').style.display = 'block';
		                document.getElementById('radio_btn_id' + i).disabled = true;
		            } 
					else if (btnFlag == "f")
		            {
						document.getElementById('avl_price_dt').style.display = 'none';
		                document.getElementById('radio_btn_id' + i).disabled = false;
		            }
				}
				for (let i = totalRows; i < table.rows.length; i++) 
				{
				   table.rows[i].style.display = 'none'; // Hide the remaining rows
				}
			}
			else
			{
				document.getElementById('table').style.visibility = 'collapse';
				document.getElementById('no_file_table').style.visibility = 'visible';
			}
		});
	});
}
</script>
</form>
</body>
</html>