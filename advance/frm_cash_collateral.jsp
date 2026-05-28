<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
</head>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String clearance = request.getParameter("clearance")==null?"K":request.getParameter("clearance");

%>
<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet">
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
				    		Cash Collateral Management  
	   	 				</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row" >
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div align="center">
								<div class="btn-group" >
									<label class="btn btn-outline-secondary subbtngrp1 <%if(clearance.equals("K")){%>btnactive<%}%>" onclick="refresh('K')">KYC</label>
									<label class="btn btn-outline-secondary subbtngrp1 <%if(clearance.equals("I")){%>btnactive<%}%>" onclick="refresh('I')">IGX</label>
								</div>
							</div>
						</div>
					</div>
					<div class="row">						
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="d-flex justify-content-between">
								<div class="form-group row">
									<div class="col-auto">
										<label class="form-label"><b>Counterparty</b></label>
									</div>
									<div class="col-auto">
									</div>
								</div>
								<div class="btn-group" align="right">
									<label class="btn btn-outline-secondary subbtngrp1" data-bs-toggle="modal" data-bs-target="#AddNewSecurity">Add New Advance</label>
								</div> 
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						<%if(counterparty_cd.equals("")){ %>
							<div align="center"><%=utilmsg.infoMessage("<b>Please Select Counterparty!</b>") %></div>
						<%}else{ %>
							<div class="table-responsive">
								<table class="table table-bordered" id="filterbysearch">
									<thead>
										<tr>
											<th>SR#</th>
											<th>Counterparty</th>
											<th>Credit | Debit</th>
											<th>Advance Type</th>
											<th>Advance Ref</th>
											<th>Transaction Ref</th>
											<th>Value(Amount + TDS)</th>
											<th>Currency</th>
											<th>Pay | Received Date</th>
											<th>Status</th>
											<th>Deal Type</th>
											<th>Linked Deal#</th>
											<th>View / Print PDF</th>
											<th>SAP Approval</th>
											<th>Generate Reversal</th>
										</tr>
									</thead>
								</table>
							</div>
						<%} %>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="AddNewSecurity" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
        			Add New Advance
        		</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal" onclick="refresh('<%=clearance%>')">
        	</div>
        	<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Counterparty<span class="s-red">*</span> </b></label>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<select class="form-select form-select-sm" name="counterparty" >
										<option value="">--Select--</option>
										<%-- <%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
											<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %>  --%>
									</select>
									<script>document.forms[0].counterparty.value="<%=counterparty_cd%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Credit | Debit<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<select class="form-select form-select-sm" name="crdr">
			      							<option value="">--Select--</option>
					      					<option value="CR">Credit</option>
					      					<option value="DR">Debit</option>
				      					</select>
		      						</div>
				    			</div>
				  			</div>
						</div>
      				</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> Security Type<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="sec_type">
				      					<option value="ADV">ADV - Advance Payment</option>
				      					<option value="DPT">DPT - Cash Deposit</option>
				      				</select>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="button" class="btn btn-sm config_btn" value="Attach Contract#" 
					    			data-bs-toggle="modal" data-bs-target="#NewDealNoModel" onclick="showNewDealNo()">
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<label class="form-label" id="NewDealNoDisplay" style="display:none;"></label>
				      			</div>
				  			</div>
						</div>
      				</div>
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Pay/Received Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="received_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
				    			<label class="form-label"><b>Amount<span class="s-red">*</span></b></label>
				  			</div>
						</div>
				  		<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="value">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-1 col-xs-1 col-md-1">  
							<div class="form-group row">
				    			<div class="col-sm-10 col-xs-10 col-md-10">
				      				<select class="form-select form-select-sm" name="currency">
				      					<option value="1">INR</option>
				      					<option value="2">USD</option>
				      				</select>
				    			</div>
				  			</div>
				  		</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Remark</b></label>
				  			</div>
						</div>
						<div class="col-sm-12 col-xs-12 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="remark">
				    			</div>
				  			</div>
						</div>
					</div>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter">
        		<div align="right">
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="NewSubmit();">
				</div>
      		</div>
        </div>
	</div>
</div>

<input type="hidden" name="form_cd" value="<%=formCd%>">
<input type="hidden" name="form_nm" value="<%=formNm%>">
<input type="hidden" name="mod_cd" value="<%=mod_cd%>">
<input type="hidden" name="mod_nm" value="<%=mod_nm%>">	
<input type="hidden" name="u" value="<%=u%>">		

</form>

</body>
</html>