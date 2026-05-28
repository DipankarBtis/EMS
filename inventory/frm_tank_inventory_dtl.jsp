<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--Harsh Maheta 20230807 : Form for Tank Inventory Details-->
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh()
{
	var inv_level_dt = document.forms[0].inv_level_dt.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_tank_inventory_dtl.jsp?u="+u+"&inv_level_dt="+inv_level_dt;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function doSubmit()
{
	var msg="";
	var flag=true;
	
	if(flag)
	{
		var a = confirm("Do you want to Submit the Tank Inventory Details?");
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function calcMMSCM_Energy(i)
{
	var tank_volume = document.getElementById('tank_volume'+i).value;
	var tank_conv_factor_1 = document.getElementById('tank_conv_factor_1'+i).value;
	var tank_mmscm = document.getElementById('tank_mmscm'+i).value;
	var tank_mmbtu = document.getElementById('tank_mmbtu'+i).value;
	var tank_conv_factor_2 = document.getElementById('tank_conv_factor_2'+i).value;
	
	if(tank_volume!=null && trim(tank_volume)!='')
	{
		if(parseFloat(tank_conv_factor_1)>0.0001)
		{
			document.getElementById('tank_mmscm'+i).value = round((parseFloat(tank_volume)*parseFloat(tank_conv_factor_1))/1000000,2);
			var tank_mmscm_not_rounded = (parseFloat(tank_volume)*parseFloat(tank_conv_factor_1))/1000000;
			
			tank_mmscm = document.getElementById('tank_mmscm'+i).value;
			
			if(tank_mmscm!=null && trim(tank_mmscm)!='')
			{
				if(parseFloat(tank_conv_factor_1)>0.0001 && parseFloat(tank_conv_factor_2)>0.0001)
				{
					document.getElementById('tank_mmbtu'+i).value = round((tank_mmscm_not_rounded*parseFloat(tank_conv_factor_2)),2);
				}
				else
				{
					document.getElementById('tank_mmbtu'+i).value = "";
				}
			}
			else
			{
				document.getElementById('tank_mmbtu'+i).value = "";
			}
		}
		else
		{
			document.getElementById('tank_mmscm'+i).value = "";
			document.getElementById('tank_mmbtu'+i).value = "";
		}
	}
	else
	{
		document.getElementById('tank_mmscm'+i).value = "";
		document.getElementById('tank_mmbtu'+i).value = "";
	}
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.inventory.DataBean_TankTerminal" id="dbterminal" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysDt=utildate.getSysdate();
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String inv_level_dt = request.getParameter("inv_level_dt")==null?sysDt:request.getParameter("inv_level_dt");

dbterminal.setCallFlag("TANK_INVENTORY_DTL");
dbterminal.setComp_cd(owner_cd);
dbterminal.setInv_level_dt(inv_level_dt);
dbterminal.init();

Vector VTANK_CD = dbterminal.getVTANK_CD();
Vector VTANK_NAME = dbterminal.getVTANK_NAME();
Vector VSTATUS = dbterminal.getVSTATUS();
Vector VTANK_VOLUME = dbterminal.getVTANK_VOLUME();
Vector VTANK_HEIGHT = dbterminal.getVTANK_HEIGHT();
Vector VTANK_MMSCM = dbterminal.getVTANK_MMSCM();
Vector VTANK_CONV_FACTOR_1 = dbterminal.getVTANK_CONV_FACTOR_1();
Vector VTANK_CONV_FACTOR_2 = dbterminal.getVTANK_CONV_FACTOR_2();
Vector VTANK_MMBTU = dbterminal.getVTANK_MMBTU();

String omb_tank_volume= dbterminal.getOmb_tank_volume();
String omb_tank_mmscm= dbterminal.getOmb_tank_mmscm();
String omb_tank_conv_factor_1= dbterminal.getOmb_tank_conv_factor_1();
String omb_tank_conv_factor_2= dbterminal.getOmb_tank_conv_factor_2();
String omb_tank_mmbtu= dbterminal.getOmb_tank_mmbtu();

boolean isPrevDate = false;

String[] sysDtArray = sysDt.split("/");
int splitedSysDate = Integer.parseInt(sysDtArray[2]+sysDtArray[1]+sysDtArray[0]);

String[] invLvlArray = inv_level_dt.split("/");
int splitedinvLvl = Integer.parseInt(invLvlArray[2]+invLvlArray[1]+invLvlArray[0]);

if(splitedinvLvl<splitedSysDate)
{
	isPrevDate=true;
}
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_TankTerminal">
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
				    		LNG Tank Inventory
	   	 				</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div align="center">
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Report Date</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="inv_level_dt" id="inv_level_dt" value="<%=inv_level_dt%>" maxLength="10" 
						      				onchange="validateDate(this);refresh();">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="example">
									<thead>
										<tr>
											<th >Tank Name</th>
									    	<th >Height (Millimetre)</th>
									    	<th >Volume (M3 of LNG)</th>
									    	<th >Conversion Factor</th>
									    	<th >Volume (MMSCM)</th>
									    	<th >Conversion Factor</th>
									    	<th >Energy (MMBTU)</th>
										</tr>
									</thead>
									<tbody id="mainTbody">
									<%int j=0;int k=0;
									if(VTANK_CD.size()>0){%>
										<%for(int i=0; i<VTANK_CD.size(); i++){ 
										%>
											<tr>
		    									<td align="center">
		    									<font style="color:<%if(VSTATUS.elementAt(i).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
													<i class="fa fa-circle fa-lg" ></i>
													&nbsp;
												</font>
												<%=VTANK_NAME.elementAt(i)%>
		    										<input type="hidden" name="tank_cd" value="<%=VTANK_CD.elementAt(i)%>" <%if(VSTATUS.elementAt(i).equals("N")){ %>disabled<%} %>>  	
		    									</td>
		    									<td align="center">
													<div style="width:100px;">
														<input type="text" class="form-control form-control-sm" name="tank_height" id="tank_height<%=i %>"  value="<%=VTANK_HEIGHT.elementAt(i)%>" <%if(VSTATUS.elementAt(i).equals("N")){ %>disabled<%} %>>
						    						</div>
						    					</td>
		    									<td align="center">
													<div style="width:100px;">
														<input type="text" class="form-control form-control-sm" name="tank_volume" id="tank_volume<%=i %>"  value="<%=VTANK_VOLUME.elementAt(i)%>" onchange="checkNumber1(this,10,2);calcMMSCM_Energy(<%=i%>);"  <%if(VSTATUS.elementAt(i).equals("N")){ %>disabled<%} %>>
						    						</div>
						    					</td>
		    									<td align="center">
													<div style="width:100px;">
														<input type="text" class="form-control form-control-sm" name="tank_conv_factor_1" id="tank_conv_factor_1<%=i %>"  value="<%=VTANK_CONV_FACTOR_1.elementAt(i)%>" onchange="checkNumber1(this,10,2);calcMMSCM_Energy(<%=i%>);"  <%if(VSTATUS.elementAt(i).equals("N")){ %>disabled<%} %>>
						    						</div>
						    					</td>
		    									<td align="center">
													<div style="width:100px;">
														<input type="text" class="form-control form-control-sm" name="tank_mmscm" id="tank_mmscm<%=i %>"  value="<%=VTANK_MMSCM.elementAt(i)%>" readOnly>
						    						</div>
						    					</td>
		    									<td align="center">
													<div style="width:100px;">
														<input type="text" class="form-control form-control-sm" name="tank_conv_factor_2" id="tank_conv_factor_2<%=i %>"  value="<%=VTANK_CONV_FACTOR_2.elementAt(i)%>" onchange="checkNumber1(this,10,2);calcMMSCM_Energy(<%=i%>);"  <%if(VSTATUS.elementAt(i).equals("N")){ %>disabled<%} %>>
						    						</div>
						    					</td>
		    									<td align="center">
													<div style="width:100px;">
														<input type="text" class="form-control form-control-sm" name="tank_mmbtu" id="tank_mmbtu<%=i %>"  value="<%=VTANK_MMBTU.elementAt(i)%>" readOnly  <%if(VSTATUS.elementAt(i).equals("N")){ %>disabled<%} %>>
						    						</div>
						    					</td>
											</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="13" align="center"><%=utilmsg.infoMessage("<b>No Tank Data Available!</b>") %></td>
										</tr>
									<%} %>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer cdfooter">
	        		<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
						<%} %>
					</div>
	      		</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="TANK_INVENTORY_DTL">
<input type="hidden" name="opration" value="INSERT">

<input type="hidden" name="prev_display" value="">
<input type="hidden" name="prev_display1" value="">

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