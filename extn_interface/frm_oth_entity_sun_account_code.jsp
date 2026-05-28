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
	
	var url="frm_oth_entity_sun_account_code.jsp?u="+u;
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function enable_disable(index)
{
	var chk = document.forms[0].chk;
	
	var sun_account = document.getElementById("sun_account_"+index);
	
	if(chk!=null && chk!=undefined)
	{
		if(chk.length!=undefined)
		{
			if(chk[index].checked)
			{
				sun_account.disabled=false;
			}
			else
			{
				sun_account.disabled=true;
			}
		}
		else
		{
			if(chk.checked)
			{
				sun_account.disabled=false;
			}
			else
			{
				sun_account.disabled=true;
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
		alert("Select atleast 1(ONE) Vendor!");
	}
}

function exportToXls()
{
	var entity_nm="_Vendor";
	var url = "xls_oth_entity_sun_account_code.jsp?fileName=Other_Entity_Sun_Account_Code"+entity_nm+".xls";

	location.replace(url);
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.extn_interface.DataBean_oth_inv_sun_interface" id="sun_master" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%

sun_master.setCallFlag("SUN_ENTITY_ACC_CD");
sun_master.setComp_cd(owner_cd);
sun_master.init();

Vector VVENDOR_CD = sun_master.getVVENDOR_CD();
Vector VVENDOR_NM = sun_master.getVVENDOR_NM();
Vector VVENDOR_ABBR = sun_master.getVVENDOR_ABBR();
Vector VSUN_ACCOUNT = sun_master.getVSUN_ACCOUNT();

%>
<body>
<%@ include file="../home/header.jsp"%>
<%if(!owner_cd.equals("2")) {%>
<div class="box-body">
	<div class="row">
		<div class="col-md-2 col-sm-2 col-xs-2"></div>
		<div class="col-md-8 col-sm-8 col-xs-8">
			<div class="card cardmain">
				<div class="card-header cdheader ">
				</div>
				<div class="card-body cdbody">
					<div class="alert alert-info">
						<div class="row">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<div class="form-group row" align="center">
									<label class="form-label"  style="font-size:40px;font-weight: 700;"><i class='fa fa-exclamation-circle fa-lg'></i> Feature Not Supported</label>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
				</div>
			</div>   
		</div>
		<div class="col-md-2 col-sm-2 col-xs-2"></div>
	</div>
</div>
<%}else{ %>
<form method="post" action="../servlet/Frm_oth_inv_sun_interface">
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
							Other Entity Sun Account Code
						</div>
						 <div class="row justify-content-end">
							<div class="col-auto">
			   	 				<span class="input-group-text">
								 	<i class="fa fa-file-excel-o fa-2x" style="color: green;" onclick="exportToXls();"></i>
								</span>	
							</div>
						 </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="table-responsive">
						<table class="table table-bordered" id="example">
							<thead id="tbsearch">
								<tr>
									<th></th>
									<th>Vendor ABBR</th>
									<th>Vendor Name</th>
									<th>SUN Account Code</th>
								</tr>
							</thead>
							<tbody>
								<%if(VVENDOR_CD.size()!=0){%>
									<% for(int i=0; i<VVENDOR_CD.size();i++){%>
										<tr>
											<td align="center">
												<input type="checkbox" class="form-check-input" name="chk" id="chk<%=i%>" onchange="enable_disable('<%=i%>');">
											</td>
											<td align="center">
												<%=VVENDOR_ABBR.elementAt(i)%>
												<input type="hidden" name="vendor_cd" value="<%=VVENDOR_CD.elementAt(i)%>"> 
												<input type="hidden" name="vendor_abbr" value="<%=VVENDOR_ABBR.elementAt(i)%>"> 
											</td>
											<td><%=VVENDOR_NM.elementAt(i)%></td>
											<td>
												<input type="text" class="form-control form-control-sm" name="sun_account" id="sun_account_<%=i%>" 
													value="<%=VSUN_ACCOUNT.elementAt(i)%>" style="text-align:right;"  onchange="chkNumber(this);" disabled >
											</td>
										</tr>
									<%} %>
								<%}else{%>
									<tr>
										<td colspan="4" align="center"><%=utilmsg.infoMessage("<b>No Vendor found!</b>") %></td>
									</tr>
								<%} %>
							</tbody>
						</table>
					</div>
				</div>
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
			</div>
		</div>
	</div>
</div>

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
<%} %>
</form>
</body>
</html>