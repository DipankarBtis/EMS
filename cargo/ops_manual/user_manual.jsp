<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function openPDF(file_nm)
{
	window.open("../ops_manual/"+file_nm)
}
</script>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_admin">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader topheader">
					<div class="d-flex justify-content-between">
						<div class="topheader">
						    User Manual
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
				<div class="row m-b-5">
					<div class="col-md-12 col-sm-12 col-xs-12">
					<div class="form-group row">
						<div class="col-md-6 col-sm-6 col-xs-6">
							<div>
								<font><b>
									<br><br>
									FMS<sup>NG</sup> Application is an efficient ETRM System for Domestic Gas Trading, customized to meet business requirement of 
									Shell Energy Marketing and Trading India Private Limited (SEMTIPL).
									<br>
									<br>
									This document describes the Operational Functions available in FMS<sup>NG</sup> Software being used at SEMTIPL. 
									This manual provides a comprehensive operational guidance for all Menus and Modules (Forms and Reports) under each Module. 
									It conveys end users how the FMSng Operations facilitate their day-to-day work and make it more efficient & effective.
									<br><br>
									To Run FMS<sup>NG</sup> Software, load EDGE web browser and specify following url:
									<br><br>
									<p style="text-indent: 5em;">
									<u>https://fmsnglive.shell.com:8443/FMSNG/home/login.jsp</u>
									</p>
									<br> 
									Once you are connected to FMS<sup>NG</sup>, 'Log in' Screen will appear for you to enter your Log in ID and Password. 
									<br>In case you forgot your password, you can retrieve same using <u>'Forgot Password'</u> option.
									<br><br>
									NOTE: The UAT/TEST environment for FMS<sup>NG</sup> Application is available via following link:
									<br><br>
									<p style="text-indent: 5em;">
									<u>https://fmsngtest.shell.com:8443/SEMTIPL/home/login.jsp</u>
									</p>
									<br> 
								</b></font>
							</div>
						</div>
						<div class="col-md-6 col-sm-6 col-xs-6">
							<div class="row m-b-5">
								<label class="form-label subheader"><i class="fa fa-snowflake-o"></i>&nbsp;FMS<sup>NG</sup> Module/s & associated Manual/s</label>
							</div>
							<div class="table-responsive">
								<table class="table table-bordered" id="filterbysearch">
									<thead>
										<tr>
											<th>Sr#</th>
											<th>Module Name</th>
											<th>PDF</th>
											<th>Last Updated</th>
										</tr>
									</thead>
									<tbody id="mainTbody">
										<tr>
											<td><div align="center">1</div></td>
											<td><div align="Center">Admin</div></td>
											<td><div align="Center"><a onclick="openPDF('FMSng_Admin_Module_User_Manual.pdf')"><i class="fa fa-file-pdf-o fa-2x" title="Admin Module User Manual" style="color: red"></i></a></div></td>
											<td><div align="center">31/03/2024</div></td>
										</tr>
										<tr>
											<td><div align="center">2</div></td>
											<td><div align="Center">Master</div></td>
											<td><div align="Center"><a onclick="openPDF('FMSng_Master_Module_User_Manual.pdf')"><i class="fa fa-file-pdf-o fa-2x" title="Master Module User Manual" style="color: red"></i></a></div></td>
											<td><div align="center">31/03/2024</div></td>
										</tr>
										<tr>
											<td><div align="center">3</div></td>
											<td><div align="Center">Purchase</div></td>
											<td><div align="Center"><a onclick="openPDF('FMSng_Purchase_Module_User_Manual.pdf')"><i class="fa fa-file-pdf-o fa-2x" title="Purchase Module User Manual" style="color: red"></i></a></div></td>
											<td><div align="center">31/03/2024</div></td>
										</tr>
										<tr>
											<td><div align="center">4</div></td>
											<td><div align="Center">Inventory</div></td>
											<td><div align="Center"><a onclick="openPDF('FMSng_Inventory_Module_User_Manual.pdf')"><i class="fa fa-file-pdf-o fa-2x" title="Inventory Module User Manual" style="color: red"></i></a></div></td>
											<td><div align="center">31/03/2024</div></td>
										</tr>
										<tr>
											<td><div align="center">5</div></td>
											<td><div align="Center">Sales</div></td>
											<td><div align="Center"><a onclick="openPDF('FMSng_Sales_Module_User_Manual.pdf')"><i class="fa fa-file-pdf-o fa-2x" title="Sales Module User Manual" style="color: red"></i></a></div></td>
											<td><div align="center">31/03/2024</div></td>
										</tr>
										<tr>
											<td><div align="center">6</div></td>
											<td><div align="Center">Transport</div></td>
											<td><div align="Center"><a onclick="openPDF('FMSng - Transport Module User Manual V1.1.pdf')"><i class="fa fa-file-pdf-o fa-2x" title="Transport Module User Manual" style="color: red"></i></a></div></td>
											<td><div align="center">24/09/2024</div></td>
										</tr>
										<tr>
											<td><div align="center">7</div></td>
											<td><div align="Center">Exchange</div></td>
											<td><div align="Center"><a onclick="openPDF('FMSng_Exchange_Module_User_Manual.pdf')"><i class="fa fa-file-pdf-o fa-2x" title="Exchange Module User Manual" style="color: red"></i></a></div></td>
											<td><div align="center">31/03/2024</div></td>
										</tr>
										<tr>
											<td><div align="center">8</div></td>
											<td><div align="Center">Risk MGMT</div></td>
											<td><div align="Center"><a onclick="openPDF('FMSng_Risk_MGMT_Module_User_Manual.pdf')"><i class="fa fa-file-pdf-o fa-2x" title="Risk MGMT Module User Manual" style="color: red"></i></a></div></td>
											<td><div align="center">31/03/2024</div></td>
										</tr>
										<tr>
											<td><div align="center">9</div></td>
											<td><div align="Center">MGMT Reports</div></td>
											<td><div align="Center"><a onclick="openPDF('FMSng_MGMT_Report_Module_User_Manual.pdf')"><i class="fa fa-file-pdf-o fa-2x" title="MGMT Reports Module User Manual" style="color: red"></i></a></div></td>
											<td><div align="center">31/03/2024</div></td>
										</tr>
										<tr>
											<td><div align="center">10</div></td>
											<td><div align="Center">Helpdesk</div></td>
											<td><div align="Center"><a onclick="openPDF('FMSng_Helpdesk_Module_User_Manual.pdf')"><i class="fa fa-file-pdf-o fa-2x" title="Incident Portal User Manual" style="color: red"></i></a></div></td>
											<td><div align="center">31/03/2024</div></td>
										</tr>
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
	</div>
</div>
</form>
</body>
</head>


</html>