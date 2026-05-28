<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var gas_dt = document.forms[0].gas_dt.value;
	
	var u = document.forms[0].u.value;
	
	var url = "rpt_nom_to_control_room.jsp?gas_dt="+gas_dt+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function refresh_rpt(msg,msg_type)
{
	var gas_dt = document.forms[0].gas_dt.value;
	
	var u = document.forms[0].u.value;
	
	var url = "rpt_nom_to_control_room.jsp?gas_dt="+gas_dt+"&msg="+msg+"&msg_type="+msg_type+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function nextDate(day_no)
{
	//var clearance = document.forms[0].clearance.value;
	
	var dt = document.forms[0].gas_dt.value;
	if(dt!="")
	{
	   	var split = dt.split("/");
		var d_dt = split[0];
		var m_dt = split[1];
		var y_dt = split[2];
		
		var dt1 = new Date(y_dt+"-"+m_dt+"-"+d_dt);
		if(day_no == "-1")
		{
			dt1.setDate(dt1.getDate()-1);
		}
		else
		{
			dt1.setDate(dt1.getDate()+1);
		}
		var day = dt1.getDate();
		if(parseInt(day) < 10)
		{
			day="0"+day;
		}
		var month = dt1.getMonth()+1;
		var year = dt1.getFullYear();
		if(parseInt(month) < 10)
		{
			month="0"+month;
		}
		var to_dt= day+"/"+month+"/"+year;
		
		document.forms[0].gas_dt.value=to_dt;
		
		//refresh(clearance);
		refresh();
	}
}

function doSubmit()
{
	var tot_exp_mmscm = document.forms[0].tot_exp_mmscm;
	if(trim(tot_exp_mmscm.value)!=null && trim(tot_exp_mmscm.value)!=0)
	{
		var a = confirm("Do you want to Submit?")
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
		 	document.forms[0].submit();
		}
	}
	else
	{
		alert("Enter Expected MMSCM Value to Submit!")
	}
}

function sumExpMmscm(value)
{
	var exp_mmscm ="";
	var ask_exp_mmscm = "";
	var size  = parseInt(value);
	var sum_exp_mmscm = 0.0 ; 
	if(size == 1)
	{
		exp_mmscm = document.forms[0].exp_mmscm.value;
		document.forms[0].tot_exp_mmscm.value = round(exp_mmscm,2);
		ask_exp_mmscm ='' + parseFloat(exp_mmscm)/24;
		if(ask_exp_mmscm=="NaN")
		{
			document.forms[0].ask_exp_mmscm.value = "0";
		}
		else
		{
			document.forms[0].ask_exp_mmscm.value = round(ask_exp_mmscm,2);
		}
	}
	else if(size > 1)
	{
		for(var i=0; i<size; i++)
		{			
			if(document.forms[0].exp_mmscm[i].value == '' ||  document.forms[0].exp_mmscm[i].value == null || document.forms[0].exp_mmscm[i].value == ' ')
			{	
				document.forms[0].exp_mmscm[i].value = '0' ;
			}
			sum_exp_mmscm = sum_exp_mmscm + parseFloat(document.forms[0].exp_mmscm[i].value);			
			if(document.forms[0].exp_mmscm[i].value == '0')
			{
				document.forms[0].exp_mmscm[i].value = '' ;
			}
		} 
		document.forms[0].tot_exp_mmscm.value = round(sum_exp_mmscm,2); //Math.round(sum_exp_mmscm*100)/100;
		ask_exp_mmscm = sum_exp_mmscm / 24;
		if(ask_exp_mmscm=="NaN")
		{
			document.forms[0].ask_exp_mmscm.value = "0";
		}
		else
		{
			document.forms[0].ask_exp_mmscm.value = round(ask_exp_mmscm,2);
		}
	}
}

var newWindow;
function viewSellerNomControlRoom(file)
{	
	var u = document.forms[0].u.value;
	var gas_dt = document.forms[0].gas_dt.value;
	var tot_exp_mmscm = document.forms[0].tot_exp_mmscm.value;
	var ask_exp_mmscm = document.forms[0].ask_exp_mmscm.value;
   	var chk_oblig='N';
	var chk_exp='N';
	var url="";
	
	if(document.forms[0].chk_oblig.checked)
	{
		chk_oblig='Y';
	}
	
	if(document.forms[0].chk_exp.checked)
	{
		chk_exp='Y';
	}
	
	url = "rpt_view_nom_to_control_room.jsp?gas_dt="+gas_dt+"&file="+file+"&chk_oblig="+chk_oblig+"&chk_exp="+chk_exp+"&tot_exp_mmscm="+tot_exp_mmscm+"&ask_exp_mmscm="+ask_exp_mmscm+"&u="+u;
   	  		
	if(!newWindow || newWindow.closed)
	{
		newWindow= window.open(url,"Seller Nomination To Control Room","top=10,left=70,width=900,height=700,scrollbars=1,menubar=1");
	}
	else 
	{
		newWindow.close();
	    newWindow= window.open(url,"Seller Nomination To Control Room","top=10,left=70,width=900,height=700,scrollbars=1,menubar=1");
	}			
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_mgmt.DataBean_ContractMgmt" id="cont_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String nextdate = utildate.getNextDate();

String gas_dt = request.getParameter("gas_dt")==null?nextdate:request.getParameter("gas_dt");
String gen_dt = utildate.getDate(gas_dt, "1");

String path ="/"+CommonVariable.work_dir+owner_cd+"/"+CommonVariable.nom_to_customer_pdf_path;
String file_path = request.getRealPath(path);

cont_mgmt.setCallFlag("DAILY_SELLER_CONTROL_ROOM");
cont_mgmt.setComp_cd(owner_cd);
cont_mgmt.setGas_dt(gas_dt);
cont_mgmt.init();

Vector VCATEGORY = cont_mgmt.getVCATEGORY();
Vector VTRANSPORTER_CD = cont_mgmt.getVTRANSPORTER_CD();
Vector VTRANSPORTER_NM = cont_mgmt.getVTRANSPORTER_NM();
Vector VTRANSPORTER_ABBR = cont_mgmt.getVTRANSPORTER_ABBR();
Vector VCOUNTERPARTY_NM = cont_mgmt.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = cont_mgmt.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_CD = cont_mgmt.getVCOUNTERPARTY_CD();
Vector VCUST_WISE_MMSCM = cont_mgmt.getVCUST_WISE_MMSCM();
Vector VCUST_WISE_MMBTU = cont_mgmt.getVCUST_WISE_MMBTU();

Vector VQTY_MMSCM = cont_mgmt.getVQTY_MMSCM();
Vector VQTY_MMBTU = cont_mgmt.getVQTY_MMBTU();

Vector VTOT_MMSCM = cont_mgmt.getVTOT_MMSCM();
Vector VTOT_MMBTU = cont_mgmt.getVTOT_MMBTU();
Vector VOBLG_MMSCM= cont_mgmt.getVOBLG_MMSCM();
Vector VCUST_WISE_OBLG = cont_mgmt.getVCUST_WISE_OBLG();
Vector VTOT_OBLG_MMSCM= cont_mgmt.getVTOT_OBLG_MMSCM();
Vector VTEMP_TRANS_CD = cont_mgmt.getVTEMP_TRANS_CD();
Vector VTEMP_TRANS_PLANT_SEQ = cont_mgmt.getVTEMP_TRANS_PLANT_SEQ();
Vector VTEMP_COUNTERPARTY_CD = cont_mgmt.getVTEMP_COUNTERPARTY_CD();
Vector VEXP_MMSCM = cont_mgmt.getVEXP_MMSCM();
Vector VPLANT_NM = cont_mgmt.getVPLANT_NM();
Vector VPLANT_SEQ = cont_mgmt.getVPLANT_SEQ();
Vector VTRANSPORTER_PLANT_SEQ = cont_mgmt.getVTRANSPORTER_PLANT_SEQ();
Vector VTRANSPORTER_PLANT_ABBR = cont_mgmt.getVTRANSPORTER_PLANT_ABBR();

String tot_mmscm = cont_mgmt.getTot_mmscm();
String tot_mmbtu = cont_mgmt.getTot_mmbtu();
String tot_obl_mcm = cont_mgmt.getTot_obl_mcm();
String ask_nom_mcm = cont_mgmt.getAsk_nom_mcm();
String ask_obl_mcm = cont_mgmt.getAsk_obl_mcm();
String ask_nom_mmbtu= cont_mgmt.getAsk_nom_mmbtu();

String context_nm = request.getContextPath();
String server_nm = request.getServerName();
String server_port = ""+request.getServerPort();
String url=CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//";
String file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//"+CommonVariable.work_dir+owner_cd;
int cnt=0;
%>
<body onload="sumExpMmscm('<%=VEXP_MMSCM.size()%>');">
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_ContractMgmt">

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
					    	Seller Nomination To Control Room
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Gas Day<span class="s-red">*</span></b></label>
								</div>
			    				<div class="col">
				      				<div class="input-group input-group-sm" >
				      					<span class="input-group-text" onclick="nextDate('-1');" title="click for Back Date"><i class="fa fa-backward fa-lg"></i></span>
					      				<input type="text" class="form-control form-control-sm date fmsdtpick" name="gas_dt" id="gas_dt" value="<%=gas_dt%>" maxLength="10" 
					      				onchange="validateDate(this);refresh();">
					      				<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
				      					<span class="input-group-text" onclick="nextDate('1');" title="click for Next Date"><i class="fa fa-forward fa-lg"></i></span>
				      				</div>
				    			</div>
				    		</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<div class="row m-b-5">
								<div class="d-flex justify-content-end">
								    <div class="email-icon-wrapper">
								        <span class="fa-stack fa-lg" style="position: relative;" title="Print PDF" onclick="viewSellerNomControlRoom('PDF')">
								            <i class="fa fa-print fa-stack-2x" style="position: absolute; left: -1.5em; top: 0em; color:#800000;"></i>
								        </span>
								    </div>
								    <div class="email-icon-wrapper">
								        <span class="fa-stack fa-lg" style="position: relative;" title="Export To Excel" onclick="viewSellerNomControlRoom('XLS')">
								            <i class="fa fa-file-excel-o fa-stack-2x" style="position: absolute; left: -0.7em; top: 0em; color:green;"></i>
								        </span>
								    </div>
								</div>
							</div>
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th rowspan="2"><div align="center"></div></th>
										<th><div align="center">Nomination</div></th>							
										<th><div align="center">Nomination</div></th>	
										<th><div align="center"><input type="checkbox" class="form-check-input" name="chk_oblig">&nbsp;Obligation</div></th>				
										<th><div align="center"><input type="checkbox" class="form-check-input" name="chk_exp">&nbsp;Expected</div></th>	
									</tr>
									<tr>
										<th><div align="center">MCM</div></th>							
										<th><div align="center">MMBTU</div></th>
										<th><div align="center">MCM</div></th>		
										<th><div align="center">MMSCM</div></th>											
									</tr>
								</thead>
								<tbody>
									<%if(VTRANSPORTER_PLANT_SEQ.size()>0){ %>
										<%for(int i=0; i<VTRANSPORTER_PLANT_SEQ.size(); i++){ %>
										<tr>
											<td><div align="left"><b><%=VTRANSPORTER_PLANT_ABBR.elementAt(i) %></b></div></td>
											<td><div align="right"><%=VTOT_MMSCM.elementAt(i) %></div></td>
											<td><div align="right"><%=VTOT_MMBTU.elementAt(i) %></div></td>
											<td><div align="right"><%=VTOT_OBLG_MMSCM.elementAt(i) %></div></td>
											<td></td>
										</tr>
										<%if(VTEMP_COUNTERPARTY_CD.size()>0)
									  	{
								  		  	for(int j=0;j<VTEMP_COUNTERPARTY_CD.size();j++)
											{
								  		  	//System.out.println("(String)VTRANSPORTER_PLANT_SEQ.elementAt(j)....."+VTRANSPORTER_PLANT_SEQ.elementAt(j)+"...."+VTEMP_TRANS_PLANT_SEQ.elementAt(j));
								  		  		if(VTRANSPORTER_CD.elementAt(i).toString().equalsIgnoreCase(VTEMP_TRANS_CD.elementAt(j).toString()) && VTRANSPORTER_PLANT_SEQ.elementAt(i).toString().equalsIgnoreCase(VTEMP_TRANS_PLANT_SEQ.elementAt(j).toString()))
								  		  		{%>
												<tr>
													<td>
														<div align="left">&nbsp;&nbsp;<%=VCOUNTERPARTY_ABBR.elementAt(j)%> - <%=VPLANT_NM.elementAt(j)%> (<%=VCATEGORY.elementAt(j)%>) </div>
														<input type="hidden" name="transporter_cd" value="<%=(String)VTEMP_TRANS_CD.elementAt(j)%>">
														<input type="hidden" name="trans_seq" value="<%=(String)VTEMP_TRANS_PLANT_SEQ.elementAt(j)%>">
														<input type="hidden" name="counterparty_cd" value="<%=(String)VTEMP_COUNTERPARTY_CD.elementAt(j)%>">
													</td>							
													<td><div align="right"><%=VCUST_WISE_MMSCM.elementAt(j)%></div></td>											
													<td><div align="right"><%=VCUST_WISE_MMBTU.elementAt(j)%><input type="hidden" name="plant_seq_no" value="<%=VPLANT_SEQ.elementAt(j)%>"></div></td>
													<td><div align="right"><%=VCUST_WISE_OBLG.elementAt(j)%></div>
														<input type="hidden" name="cont_type" value="<%=VCATEGORY.elementAt(j)%>">
													</td>
													<td><div align="center"><input type="text" class="form-control form-control-sm" name="exp_mmscm" value="<%=VEXP_MMSCM.elementAt(j)%>" size="6" maxlength="6" style="text-align:right;" onBlur="checkNumber1(this,4,2);sumExpMmscm('<%=VEXP_MMSCM.size()%>');"></div></td>							
												</tr>		
									  			<%}
									  		}
										}
									} %>
									<tr>
										<td><div align="right"><b>Total</b></div></td>		
										<td><div align="right"><%=tot_mmscm%></div></td>
										<td><div align="right"><%=tot_mmbtu%></div></td>	
										<td><div align="right"><%=tot_obl_mcm%></div></td>						
										<td><div align="center"><input type="text" class="form-control form-control-sm" name="tot_exp_mmscm" value="" size="6" maxlength="6" style="text-align:right;" readonly></div></td>						
									</tr>	
									<tr>
										<td><div align="right"><b>Asking Rate</b></div></td>		
										<td><div align="right"><%=ask_nom_mcm%></div></td>
										<td><div align="right"><%=ask_nom_mmbtu%></div></td>
										<td><div align="right"><%=ask_obl_mcm%></div></td>
										<td><div align="center"><input type="text" class="form-control form-control-sm" name="ask_exp_mmscm" value="" size="6" maxlength="6" style="text-align:right;" readonly></div></td>					
									</tr>
									<%}else{ %>
										<tr>
											<td colspan="5"><div align="center"><%=utilmsg.infoMessage("<b>Nomination is not Done for Selected Gas Day!</b>") %></div></td>
										</tr>
									<%} %>
								</tbody>
							</table>
						</div>
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

<input type="hidden" name="option" value="DAILY_SELLER_CONTROL_ROOM">

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
<script type="text/javascript">
function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("filterbysearch");
  	
  	tr = table.getElementsByTagName("tr");
  	for (i = 1; i < tr.length; i++) 
  	{
    	td = tr[i].getElementsByTagName("td")[indx];
    	if (td) 
    	{
      		txtValue = td.textContent || td.innerText;
      		if (txtValue.toLocaleLowerCase().indexOf(filter) > -1) {
        		tr[i].style.display = "";
        		count++;
      		} else {
      			tr[i].style.display = "none";
      		}
    	}
  	}
}

</script>
</html>