<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>

function refresh()
{
	var u = document.forms[0].u.value;
	var entity_role=document.forms[0].entity_role.value;
	
	var url="frm_entity_sun_account_code.jsp?u="+u+"&entity_role="+entity_role;
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function enable_disable(index)
{
	var acc_type_count = document.forms[0].acc_type_count.value;
	var entity_role=document.forms[0].entity_role.value;
	var chk = document.forms[0].chk;
	
	var sun_account = document.getElementById("sun_account_"+entity_role+"_"+index);
	
	if(chk!=null && chk!=undefined)
	{
		if(chk.length!=undefined)
		{
			if(chk[index].checked)
			{
				sun_account.disabled=false;
				for(i=0;i<parseInt(acc_type_count);i++)
				{
					var sun_entity_account = document.getElementById("sun_entity_account_"+entity_role+"_"+index+"_"+i);
					sun_entity_account.disabled=false;
				}
			}
			else
			{
				sun_account.disabled=true;
				for(i=0;i<parseInt(acc_type_count);i++)
				{
					var sun_entity_account = document.getElementById("sun_entity_account_"+entity_role+"_"+index+"_"+i);
					sun_entity_account.disabled=true;
				}
			}
		}
		else
		{
			if(chk.checked)
			{
				sun_account.disabled=false;
				for(i=0;i<parseInt(acc_type_count);i++)
				{
					var sun_entity_account = document.getElementById("sun_entity_account_"+entity_role+"_"+index+"_"+i);
					sun_entity_account.disabled=false;
				}
			}
			else
			{
				sun_account.disabled=true;
				for(i=0;i<parseInt(acc_type_count);i++)
				{
					var sun_entity_account = document.getElementById("sun_entity_account_"+entity_role+"_"+index+"_"+i);
					sun_entity_account.disabled=true;
				}
			}
		}
	}
	
}

function enable_disable_plant(index,plant_indx)
{
	var entity_role=document.forms[0].entity_role.value;
	var chk = document.forms[0].chk;
	var acc_type_count = document.forms[0].acc_type_count.value;
	
	if(entity_role=='T')
	{
		if(chk!=null && chk!=undefined)
		{
			if(chk.length!=undefined)
			{
				if(chk[index].checked)
				{
					for(i=0;i<plant_indx;i++)
					{
						var sun_plant_account = document.getElementById("sun_plant_account_"+entity_role+"_"+index+"_"+i);
						sun_plant_account.disabled=false;
						
						for(j=0;j<acc_type_count;j++)
						{
							var sun_entity_plant_account = document.getElementById("sun_entity_plant_account_"+entity_role+"_"+index+"_"+j+"_"+i);
							sun_entity_plant_account.disabled=false;
						}
					}
				}
				else
				{
					for(i=0;i<plant_indx;i++)
					{
						var sun_plant_account = document.getElementById("sun_plant_account_"+entity_role+"_"+index+"_"+i);
						sun_plant_account.disabled=true;
						
						for(j=0;j<acc_type_count;j++)
						{
							var sun_entity_plant_account = document.getElementById("sun_entity_plant_account_"+entity_role+"_"+index+"_"+j+"_"+i);
							sun_entity_plant_account.disabled=true;
						}
					}
				}
			}
			else
			{
				if(chk.checked)
				{
					for(i=0;i<plant_indx;i++)
					{
						var sun_plant_account = document.getElementById("sun_plant_account_"+entity_role+"_"+index+"_"+i);
						sun_plant_account.disabled=false;
						
						for(j=0;j<acc_type_count;j++)
						{
							var sun_entity_plant_account = document.getElementById("sun_entity_plant_account_"+entity_role+"_"+index+"_"+j+"_"+i);
							sun_entity_plant_account.disabled=false;
						}
					}
				}
				else
				{
					for(i=0;i<plant_indx;i++)
					{
						var sun_plant_account = document.getElementById("sun_plant_account_"+entity_role+"_"+index+"_"+i);
						sun_plant_account.disabled=true;
						
						for(j=0;j<acc_type_count;j++)
						{
							var sun_entity_plant_account = document.getElementById("sun_entity_plant_account_"+entity_role+"_"+index+"_"+j+"_"+i);
							sun_entity_plant_account.disabled=true;
						}
					}
				}
			}
		}
	}
}

function chkNumber(obj)
{
	if(isNaN(obj.value))
	{
		alert("SUN Code must be Numeric!");
		obj.value='';
		return false;
	}
}
function doSubmit()
{
	var chk=document.forms[0].chk;
	var counterparty_cd = document.forms[0].counterparty_cd;
	
	var indx=0;
	var chk_count=0;
	var index_arr=new Array();
	
	if(chk!=null && chk!=undefined)
	{
		if(chk.length!=undefined)
		{
			for(var i=0;i<chk.length;i++)
			{
				if(chk[i].checked)
				{
					chk_count++;
					index_arr[indx]=i;
					indx++;
				}
			}
		}
		else
		{
			if(chk.checked)
			{
				chk_count++;
				index_arr[indx]=0;
				indx++;
			}
		}
	}
	
	if(chk_count>0)
	{
		var a=confirm("Do you want to submit?");
		if(a)
		{
			document.forms[0].index.value=index_arr;
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert("Select atleast 1(ONE) Counterparty!");
	}
}

function hide_show(id1,id2)
{	
	if(document.getElementById(id1).style.display=='none')
	{
		document.getElementById(id1).style.display='table-row-group';
		document.getElementById(id2).className='fa fa-compress';
	}
	else
	{
		document.getElementById(id1).style.display='none';
		document.getElementById(id2).className='fa fa-expand';
	}
}

function exportToXls()
{
	var entity_role=document.forms[0].entity_role.value;
	var entity_nm="";
	if(entity_role=="C")
	{
		entity_nm="_Customer";
	}
	else if(entity_role=="T")
	{
		entity_nm="_Trader";
	}
	else if(entity_role=="R")
	{
		entity_nm="_Transporter";
	}
	var url = "xls_entity_sun_account_code.jsp?fileName=Entity_Sun_Account_Code"+entity_nm+".xls&entity_role="+entity_role;

	location.replace(url);
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.extn_interface.DataBean_sun_interface" id="sun_master" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String entity_role = request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");

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
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_sun_interface">
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
							Entity Sun Account Code
						</div>
						 <div class="row justify-content-end">
							<div class="col-auto">
			   	 				<span class="input-group-text">
								 	<i class="fa fa-file-excel-o fa-2x" style="color: green;" onclick="exportToXls();"></i>
								</span>	
							</div>
							<div class="col-auto">
								<div class="btn-group">
									<select class="btn btn-outline-secondary btngrp <%if(!entity_role.equals("0")){%>btnactive<%}%>" name="entity_role" onchange="refresh();">
										<option value="0">Select Entity Roles</option>
										<option value="C">Customer</option>
						    			<option value="T">Trader</option>
						    			<option value="R">Transporter</option>
						    			<!-- <option value="V">Vessel Agent</option>
						    			<option value="H">Custom House Agent</option>
						    			<option value="S">Surveyor</option> -->
						    			<!-- <option value="G">Gas Exchange</option>  -->
						    			<!-- <option value="B">Business Owner</option>-->
									</select>
								</div>
							</div>
						 </div>
						<script>
							document.forms[0].entity_role.value="<%=entity_role%>"
						</script>	
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="table-responsive">
						<%if(!entity_role.equals("0")){ %>
							<table class="table table-bordered" id="example">
								<thead id="tbsearch">
									<tr>
										<th></th>
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
													<input type="checkbox" class="form-check-input" name="chk" id="chk<%=i%>" onchange="enable_disable('<%=i%>');enable_disable_plant('<%=i%>','<%=plant_count%>');">
												</td>
												<%if(entity_role.equals("T")){%>
													<td align="center" onclick="hide_show('tbody<%=i %>','hidCont<%=i%>');">
														<%=VCOUNTERPARTY_ABBR.elementAt(i)%>
														&nbsp;&nbsp;&nbsp;<span id="hidCont<%=i%>" class="fa fa-compress" title="Click here to edit for different plant!"></span>
													
														<input type="hidden" name="plant_count" value="<%=plant_count%>">
														<input type="hidden" name="counterparty_cd" value="<%=VCOUNTERPARTY_CD.elementAt(i)%>">
													</td> 
												<%}else{%>
													<td align="center">
														<%=VCOUNTERPARTY_ABBR.elementAt(i)%>
														<input type="hidden" name="counterparty_cd" value="<%=VCOUNTERPARTY_CD.elementAt(i)%>">
													</td>
												<%} %>
												<td><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
												<td><%=VCOUNTERPARTY_CATEGORY.elementAt(i) %></td>
												<td>
													<input type="text" class="form-control form-control-sm" name="sun_account_<%=entity_role%>" id="sun_account_<%=entity_role%>_<%=i%>" 
														value="<%=VSUN_ACCOUNT.elementAt(i)%>" style="text-align:right;"  onchange="chkNumber(this);" disabled >
												</td>
												<%for(int acc=0;acc<VACCOUNT_TYPE.size();acc++){%>
													<td>
														<input type="text" class="form-control form-control-sm" name="sun_entity_account_<%=entity_role%>_<%=i%>_<%=acc%>" id="sun_entity_account_<%=entity_role%>_<%=i%>_<%=acc%>" 
														value="<%=VSUN_ENTITY_ACCOUNT.elementAt(j) %>" style="text-align:right;"  onchange="chkNumber(this);" disabled >
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
																<input type="hidden" name="plant_map_<%=VCOUNTERPARTY_CD.elementAt(i)%>" value="<%=VPLANT_SEQ_NO.elementAt(n)%>">
																<input type="hidden" name="plant_seq_no" value="<%=VPLANT_SEQ_NO.elementAt(n)%>">
															</td>
															<td>
																<input type="text" class="form-control form-control-sm" name="sun_plant_account_<%=entity_role%>" id="sun_plant_account_<%=entity_role%>_<%=i%>_<%=p%>" 
																	value="<%=VACC_PLANT.elementAt(n) %>" style="text-align:right;"  onchange="chkNumber(this);" disabled >
															</td>
															<%for(int acc=0;acc<VACCOUNT_TYPE.size();acc++){%>
																<td>
																	<input type="text" class="form-control form-control-sm" name="sun_entity_plant_account_<%=entity_role%>_<%=acc%>" id="sun_entity_plant_account_<%=entity_role%>_<%=i%>_<%=acc%>_<%=p%>" 
																	value="<%=VACC_OTH_PLANT.elementAt(t++) %>" style="text-align:right;"  onchange="chkNumber(this);" disabled >
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
							<div colspan="<%=5+VACCOUNT_TYPE.size()%>" align="center">
							<%=utilmsg.infoMessage("<b>Please Select any Entity!</b>") %>
							</div>
						<%} %>
					</div>
				</div>
				<%if(!entity_role.equals("0")){%>
					<div class="card-footer cdfooter text-center">
						<div class="d-flex justify-content-between">
							<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
							<%if(write_access.equals("Y")){ %>
							<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();">
							<%}else{ %>
							<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
							<%} %>
						</div>
					</div>
				<%} %>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="acc_type_count" value="<%=VACCOUNT_TYPE.size()%>">
<input type="hidden" name="index" value="">
<input type="hidden" name="option" value="SUN_ENTITY_ACC_CD">

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