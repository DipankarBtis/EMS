<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	//var from_dt=document.forms[0].from_dt.value;
	//var to_dt=document.forms[0].to_dt.value;
	var trader_cd=document.forms[0].trader_cd.value;
	
	var u = document.forms[0].u.value;
    var flag = true;
    var msg = "";

    //if (from_dt != null && to_dt != null) 
    {
       // if (trim(from_dt) != "" && trim(to_dt) != "") 
        {
        	if(flag)
        	{
        		//var url="fms_purchase_ctr_mst.jsp?from_dt="+from_dt+"&to_dt="+to_dt+"&trader_cd="+trader_cd+"&u="+u;
        		var url="fms_purchase_ctr_mst.jsp?trader_cd="+trader_cd+"&u="+u;

                document.getElementById("loading").style.visibility = "visible";
                location.replace(url);
        	}
        }
    }
}

function doClear()
{
	document.forms[0].ctr_contract_dtl.disabled=false;
	document.forms[0].bu_unit.disabled = false;
	document.forms[0].plant_cd.disabled = false;
	document.forms[0].ctr_ref.disabled=false;

	// ensure option 0 exists for ctr_contract_dtl
	if(document.forms[0].ctr_contract_dtl.options.length == 0 || document.forms[0].ctr_contract_dtl.options[0].value != "0")
	{
		document.forms[0].ctr_contract_dtl.innerHTML = "<option value='0'>---Select---</option>";
	}

	// ensure option 0 exists for plant_cd
		document.forms[0].plant_cd.innerHTML = "<option value='0'>---Select---</option>";
	if(document.forms[0].plant_cd.options.length == 0 || document.forms[0].plant_cd.options[0].value != "0")
	{
	}

		document.forms[0].mole_cd.innerHTML = "<option value='0'>---Select---</option>";
	// ensure option 0 exists for mole_cd
	if(document.forms[0].mole_cd.options.length == 0 || document.forms[0].mole_cd.options[0].value != "0")
	{
	}
		document.forms[0].bu_unit.innerHTML = "<option value='0'>---Select---</option>";
	// ensure select option with value 0 exists
	if(document.forms[0].bu_unit.options.length == 0 || document.forms[0].bu_unit.options[0].value != "0")
	{
	}
	
	// reset values
	document.forms[0].ctr_contract_dtl.value="0";
	document.forms[0].bu_unit.value="0";
	document.forms[0].plant_cd.value="0";
	document.forms[0].ctr_ref.value="";
	document.forms[0].mole_cd.value="0";
	document.forms[0].eff_dt.value="";
	document.forms[0].opration.value="INSERT";
}

function checkSpace(input)
{
	if(input.value.includes(" "))
	{
		alert("Spaces are not allowed!");
		input.value="";
		return false;
	}
	
}

function doSubmit()
{
	var trader_cd=document.forms[0].trader_cd.value;
	var ctr_contract_dtl = document.forms[0].ctr_contract_dtl.value;
	var bu_unit=document.forms[0].bu_unit.value;
	var plant_cd=document.forms[0].plant_cd.value;
	var ctr_ref=document.forms[0].ctr_ref.value
	var mole_cd=document.forms[0].mole_cd.value;
	var prod_cd=document.forms[0].prod_cd.value;
	var eff_dt=document.forms[0].eff_dt.value;
	
	var trader_cd_ctr = document.forms[0].trader_cd_ctr;
	var bu_unit_ctr = document.forms[0].bu_unit_ctr;
	var plant_seq_ctr = document.forms[0].plant_seq_ctr;
	var ctr_no = document.forms[0].ctr_no;
	var temp_ctr_contract_dtl = document.forms[0].temp_ctr_contract_dtl;
	var ctr_trader_cd = document.forms[0].ctr_trader_cd.value;
	
	var opration=document.forms[0].opration.value;
	
	var msg="";
	var flag=true;
	
	if(opration=='MODIFY')
	{
		if(ctr_trader_cd=='' || ctr_trader_cd==0)
		{
			msg+="Select Trader!\n";
			flag=false;
		}
	}
	else
	{
		if(trader_cd=='' || trader_cd==0)
		{
			msg+="Select Trader!\n";
			flag=false;
		}
	}
	if(ctr_contract_dtl==''||ctr_contract_dtl==0)
	{
		msg+="Select Contract!\n";
		flag=false;
	}
	if(bu_unit==''||bu_unit==0)
	{
		msg+="Select Business Unit!\n";
		flag=false;
	}
	if(plant_cd==''||plant_cd==0)
	{
		msg+="Select Plant!\n";
		flag=false;
	}
	if(ctr_ref=='')
	{
		msg+="Enter CTR!\n";
		flag=false;
	}
	if(prod_cd==''||prod_cd==0)
	{
		msg+="Select Product!\n";
		flag=false;
	}
	if(mole_cd==''||mole_cd==0)
	{
		msg+="Select Molecule!\n";
		flag=false;
	}
	if(eff_dt=='')
	{
		msg+="Enter Eff. date!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a;
		a= confirm("Do you Want to Submit?");
		if(a)
		{
			ctr_no.value = ctr_ref;
			if(opration=='INSERT')
			{
				trader_cd_ctr.value = trader_cd;
				bu_unit_ctr.value = bu_unit;
				plant_seq_ctr.value = plant_cd;
				temp_ctr_contract_dtl.value = ctr_contract_dtl;
				ctr_no.value = ctr_ref;
			}
			
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
	
}

function doModify(coutpty_cd,agmt_no,cont_no,cont_type,cargo_no,bu_cd,plant_seq,temp_ctr_ref,deal_map,temp_eff_dt,prod_cd1,molecule_cd)
{
	var map=coutpty_cd+"@@"+agmt_no+"@@"+cont_no+"@@"+cont_type+"@@"+cargo_no;
	var molecule_map=molecule_cd+"-"+prod_cd1;
	
	var ctr_trader_cd = document.forms[0].ctr_trader_cd;
	var ctr_contract_dtl = document.forms[0].ctr_contract_dtl;
	var ctr_ref=document.forms[0].ctr_ref;
	var eff_dt=document.forms[0].eff_dt;
	var mole_cd=document.forms[0].mole_cd;
	var prod_cd=document.forms[0].prod_cd;
	
	ctr_trader_cd.value=coutpty_cd;
	ctr_contract_dtl.value=map;
	ctr_ref.value=temp_ctr_ref;
	eff_dt.value=temp_eff_dt;
	//mole_cd.value=molecule_map;
	prod_cd.value=prod_cd1
	
	var select = document.getElementById("ctr_contract_dtl");
	
	// Check if the option already exists
    var optionExists = false;
    for (var i = 0; i < select.options.length; i++) 
    {
        if (select.options[i].value === map) 
        {
            select.selectedIndex = i;
            optionExists = true;
            break;
        }
    }

    // If it does not exist, create a new option
    if (!optionExists) 
    {
        var newOption = document.createElement("option");
        newOption.value = map;
        newOption.text = deal_map;  // or any text you want to display
        select.add(newOption);
        select.value = map;  // select it
    }
	
    //alert(ctr_ref.value);
    
	document.forms[0].trader_cd_ctr.value=coutpty_cd;
	document.forms[0].temp_ctr_contract_dtl.value=map;
	document.forms[0].bu_unit_ctr.value=bu_cd;
	document.forms[0].plant_seq_ctr.value=plant_seq;
	document.forms[0].ctr_no.value=ctr_ref.value;
	
	fetchBuDtl(document.forms[0].temp_ctr_contract_dtl);
	// wait for ajax to populate dropdown
	window.setTimeout(function() 
	{
		setTimeout(function(){
			document.forms[0].bu_unit.value = bu_cd;
		},500);
	},500);
	
	fetchPlantDtl(document.forms[0].temp_ctr_contract_dtl);
	window.setTimeout(function() 
	{
		setTimeout(function(){
			document.forms[0].plant_cd.value = plant_seq;
		},500);
	},500)
	
	fetchMolecules(prod_cd)
	window.setTimeout(function() 
	{
		setTimeout(function(){
			document.forms[0].mole_cd.value = molecule_cd;
		},500);
	},500)
	
	ctr_trader_cd.disabled=true;
	ctr_contract_dtl.disabled=true;
	select.disabled = true;
	document.forms[0].bu_unit.disabled = true;
	document.forms[0].plant_cd.disabled = true;
	//document.forms[0].ctr_ref.disabled = true;		//AS PER VIJAY'S FEEDBACK 20260323
	checkIsCtrNominated(document.forms[0].ctr_ref);
	
	document.forms[0].opration.value = 'MODIFY';
	checkCTR();
}

function hide_show(id1,id2)
{	
	if(document.getElementById(id1).style.display=='none'){
		document.getElementById(id1).style.display='table-row-group';
		document.getElementById(id2).className='fa fa-compress';
	}else{
		document.getElementById(id1).style.display='none';
		document.getElementById(id2).className='fa fa-expand';
	}
} 

function exportToXls()
{
	var trader_cd=document.forms[0].trader_cd.value;
	var owner_abbr=document.forms[0].owner_abbr.value;
	var fileName=owner_abbr+"_Purchase_CTR.xls";
	
	var url = "xls_purchase_ctr_mst.jsp?fileName="+fileName+"&trader_cd="+trader_cd;

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.purchase.DataBean_Trader_Contract_Mst" id="purchase" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String trader_cd=request.getParameter("trader_cd")==null?"0":request.getParameter("trader_cd");

purchase.setCallFlag("PUR_CTR_MST");
purchase.setComp_cd(owner_cd);
purchase.setFrom_dt(from_dt);
purchase.setTo_dt(to_dt);
purchase.setCounterparty_cd(trader_cd);
purchase.init();

String cpStatus = purchase.getcpStatus();

Vector VMST_COUNTERPARTY_CD = purchase.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_ABBR = purchase.getVMST_COUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_NM = purchase.getVMST_COUNTERPARTY_NM();

Vector VCOUNTERPARTY_CD = purchase.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = purchase.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = purchase.getVCOUNTERPARTY_NM();
Vector VAGMT_NO = purchase.getVAGMT_NO();
Vector VCONT_NO = purchase.getVCONT_NO();
Vector VCARGO_NO = purchase.getVCARGO_NO();
Vector VCONTRACT_TYPE =purchase.getVCONTRACT_TYPE();
Vector VDEAL_MAP = purchase.getVDEAL_MAP();
Vector VEFF_DT = purchase.getVEFF_DT();
Vector VMOLE_CD = purchase.getVMOLE_CD();
Vector VPROD_CD = purchase.getVPROD_CD();
Vector VPLANT_SEQ_NO = purchase.getVPLANT_SEQ_NO();
Vector VPLANT_ABBR = purchase.getVPLANT_ABBR();
Vector VBU_PLANT_SEQ_NO = purchase.getVBU_PLANT_SEQ_NO();
Vector VBU_PLANT_ABBR = purchase.getVBU_PLANT_ABBR();
Vector VMOLECULE_NM = purchase.getVMOLECULE_NM();
Vector VCTR_REF = purchase.getVCTR_REF();
Vector VPLANT_NAME = purchase.getVPLANT_NAME();
Vector VBU_PLANT_NM = purchase.getVBU_PLANT_NM();
Vector VINDEX = purchase.getVINDEX();

Vector VCTR_COUNTERPARTY_CD = purchase.getVCTR_COUNTERPARTY_CD();
Vector VCTR_COUNTERPARTY_NM = purchase.getVCOUNTERPARTY_NM();
Vector VCTR_COUNTERPARTY_ABBR = purchase.getVCOUNTERPARTY_ABBR();
Vector VCTR_PLANT_NM = purchase.getVCTR_PLANT_NM();
Vector VCTR_PLANT_ABBR = purchase.getVCTR_PLANT_ABBR();
Vector VCTR_PLANT_SEQ_NO = purchase.getVCTR_PLANT_SEQ_NO();
Vector VCTR_BU_PLANT_NM = purchase.getVCTR_BU_PLANT_NM();
Vector VCTR_BU_PLANT_ABBR = purchase.getVCTR_BU_PLANT_ABBR();
Vector VCTR_BU_PLANT_SEQ_NO = purchase.getVCTR_BU_PLANT_SEQ_NO();
Vector VCTR_CONT_NO = purchase.getVCTR_CONT_NO();
Vector VCTR_AGMT_NO = purchase.getVCTR_AGMT_NO();
Vector VCTR_DISP_CONT_NO = purchase.getVCTR_DISP_CONT_NO();
Vector VCTR_CARGO_NO = purchase.getVCTR_CARGO_NO();
Vector VCTR_CONT_TYPE = purchase.getVCTR_CONT_TYPE();
Vector VCTR_PROD_CD = purchase.getVCTR_PROD_CD();
Vector VCTR_MOLE_CD = purchase.getVCTR_MOLE_CD();
Vector VCTR_MOLE_NM = purchase.getVCTR_MOLE_NM();
Vector VCONT_REF_NO = purchase.getVCONT_REF_NO();
Vector VCTR_CONT_REF_NO = purchase.getVCTR_CONT_REF_NO();
Vector VCTR_PROD_ABBR = purchase.getVCTR_PROD_ABBR();
Vector VPRODUCT_NM = purchase.getVPRODUCT_NM();

%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_Trader_Contract_Mst">
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
					    	Purchase CTR Master
					    </div>
					    <%if(!trader_cd.equals("")&&!trader_cd.equals("0")){%>
						    <div align="right" onclick="exportToXls();" style="color:green;">
								<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
							</div>
						<%} %>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-3">  
							
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Trader</b></label>
								</div>
								<div class="col">
									<select class="form-select form-select-sm" name="trader_cd">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].trader_cd.value="<%=trader_cd%>"</script>
								</div>
							</div>
						</div>
						<%-- <div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>From</b></label>
								</div>
								<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt %>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
								<div class="col-auto">
									<label class="form-label"><b>To</b></label>
								</div>
								<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt %>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
							</div>
						</div> --%>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<input type="button" class="btn btn-warning com-btn" value="Apply Filter" onclick="refresh();">
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
				<%if(!trader_cd.equals("")&&!trader_cd.equals("0")&&!cpStatus.equals("N")){%>
					<div class="row m-b-5" id="new_ctr">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#MapCTRModal" onclick="doClear();">Add CTR</label>
							</div>
						</div>
					</div>
				<%} %>
					<div class="row">
					<div class="col-md-12 col-sm-12 col-xs-12">
		    					<div align="right">
		    						<input class="form-control form-control-sm" type="text" id="globalSearch" onkeyup="globalSearchTableWithInnerTbody(this,0)" placeholder="Search.." style="width:200px"/>
		    					</div>
	    					</div>
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered ems_sorttbl" id="ems_table0">
									<thead id="ems_tbsort0">
										<tr>
											<th></th>
									    	<th class="ems_innerthsort0">CTR</th>		    		
											<th class="ems_innerthsort0">Business Unit</th>
											<th class="ems_innerthsort0">Trader</th>
											<th class="ems_innerthsort0">Plant</th>				    		
									    	<th class="ems_innerthsort0">Link Contract</th>
									    	<th class="ems_innerthsort0">Contract Ref</th>
									    	<th class="ems_innerthsort0">Product</th>
									    	<th class="ems_innerthsort0">Molecule</th>
											<th class="ems_innerthsort0">Eff. Date</th>		
									    </tr>
									</thead>
									<%
									if(VCOUNTERPARTY_CD.size()>0){
									    int j=0;
									    int groupIndex=0;
									    while(j < VCTR_REF.size()){
									        String currentCtr = ""+VCTR_REF.elementAt(j);
									        int size = 0;
									        for(int x=j; x<VCTR_REF.size(); x++){
									            if(currentCtr.equals(""+VCTR_REF.elementAt(x))){
									                size++;
									            }else{
									                break;
									            }
									        }
									%>
									<tbody id="parent-body<%=groupIndex%>">
										<tr class="parent-row<%=groupIndex%>">
										    <td align="center">
										        <font title="Click to Edit" style="color:var(--header_color)">
										            <i class="fa fa-edit fa-lg"
										            data-bs-toggle="modal" data-bs-target="#MapCTRModal"
										            onclick="doClear();doModify('<%=VCOUNTERPARTY_CD.elementAt(j)%>',
										            '<%=VAGMT_NO.elementAt(j)%>',
										            '<%=VCONT_NO.elementAt(j)%>',
										            '<%=VCONTRACT_TYPE.elementAt(j)%>',
										            '<%=VCARGO_NO.elementAt(j)%>',
										            '<%=VBU_PLANT_SEQ_NO.elementAt(j)%>',
										            '<%=VPLANT_SEQ_NO.elementAt(j)%>',
										            '<%=VCTR_REF.elementAt(j)%>',
										            '<%=VDEAL_MAP.elementAt(j)%>',
										            '<%=VEFF_DT.elementAt(j)%>',
										            '<%=VPROD_CD.elementAt(j)%>',
										            '<%=VMOLE_CD.elementAt(j)%>')">
										            </i>
										        </font>
										    </td>
											<%-- <% if(size>1){ %>
												<td align="center" title="<%=VCOUNTERPARTY_NM.elementAt(j)%>"
												onclick="hide_show('tbody<%=groupIndex%>','hidCont<%=groupIndex%>');">
												    <%=VCOUNTERPARTY_ABBR.elementAt(j)%>
												    &nbsp;&nbsp;
												    <span id="hidCont<%=groupIndex%>" class="fa fa-expand"></span>
												</td>
											<% } else { %> --%>
											<% if(size>1){ %>
												<td align="center" onclick="hide_show('tbody<%=groupIndex%>','hidCont<%=groupIndex%>');">
													<%=VCTR_REF.elementAt(j)%>
													&nbsp;&nbsp;
												    <span id="hidCont<%=groupIndex%>" class="fa fa-expand"></span>
												</td>
											<% } else { %>
												<td align="center"><%=VCTR_REF.elementAt(j)%></td>
											<% } %>
											<td align="center"><%=VBU_PLANT_ABBR.elementAt(j)%></td>
											
												<td align="center" title="<%=VCOUNTERPARTY_NM.elementAt(j)%>">
												    <%=VCOUNTERPARTY_ABBR.elementAt(j)%>
												</td>
											<%-- <% } %> --%>
											<td align="center"><%=VPLANT_ABBR.elementAt(j)%></td>
											<td align="center"><%=VDEAL_MAP.elementAt(j)%></td>
											<td align="center"><%=VCONT_REF_NO.elementAt(j)%></td>
											<td align="center"><%=VPRODUCT_NM.elementAt(j)%></td>
											<td align="center"><%=VMOLECULE_NM.elementAt(j)%></td>
											<td align="center"><%=VEFF_DT.elementAt(j)%></td>
										</tr>
										</tbody>
										<% if(size>1){ %>
											<tbody id="tbody<%=groupIndex%>" style="display:none;">
												<tr class="child-heading child-heading<%=groupIndex%>" style="text-align:center;font-weight:bold;background:#bce6ff;color:#0c63e4;">
													<td colspan="2" rowspan="<%=size+1%>" style="background:white;"></td>
													<!-- <td>CTR</td> -->
													<td>Trader</td>
													<td>Business Unit</td>
													<td>Plant</td>
													<td>Linked Contract</td>
													<td>Contract Ref</td>
													<td>Product</td>
													<td>Molecule</td>
													<td>EFF Date</td>
												</tr>
												<%
												for(int y=1; y<size; y++){
												j++;
												%>
												<tr class="child-row<%=groupIndex%>">
													<%-- <td align="center"><%=VCTR_REF.elementAt(j)%></td> --%> 
													<td align="center" title="<%=VCOUNTERPARTY_NM.elementAt(j)%>">
													<%=VCOUNTERPARTY_ABBR.elementAt(j)%>
													</td>
													<td align="center"><%=VBU_PLANT_ABBR.elementAt(j)%></td>
													<td align="center"><%=VPLANT_ABBR.elementAt(j)%></td>
													<td align="center"><%=VDEAL_MAP.elementAt(j)%></td>
													<td align="center"><%=VCONT_REF_NO.elementAt(j)%></td>
													<td align="center"><%=VPRODUCT_NM.elementAt(j)%></td>
													<td align="center"><%=VMOLECULE_NM.elementAt(j)%></td>
													<td align="center"><%=VEFF_DT.elementAt(j)%></td>
												</tr>
												<% } %>
											</tbody>
										<% } %>
										<%
											j++;
											groupIndex++;
										}
									}else{
									%>
										<tr>
											<td colspan="10" align="center">
												<%=utilmsg.infoMessage("<b>No CTR Added for given date!</b>") %>
											</td>
										</tr>
									</tbody>
									<% } %>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="MapCTRModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-lg">
		<div class="modal-content">
			<div class="modal-header cdheader">
	    		<div class="topheader">
					Add/Modify CTR 
				</div>
	    		<input type="button" class="btn-close" data-bs-dismiss="modal">
	  		</div>
	  		<div class="modal-body mdbody">
	  			<div class="cdbody">
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Trader<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="ctr_trader_cd" onchange="" disabled>
										<option value="0">--Select--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].ctr_trader_cd.value="<%=trader_cd%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Contract<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="ctr_contract_dtl" id="ctr_contract_dtl" onchange="fetchBuDtl(this);fetchPlantDtl(this);checkCTR()">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCTR_DISP_CONT_NO.size();i++){ 
											String map=VCTR_COUNTERPARTY_CD.elementAt(i)+"@@"+VCTR_AGMT_NO.elementAt(i)+"@@"+VCTR_CONT_NO.elementAt(i)
											+"@@"+VCTR_CONT_TYPE.elementAt(i)+"@@"+VCTR_CARGO_NO.elementAt(i);
										%>
										<option value="<%=map%>"><%=VCTR_DISP_CONT_NO.elementAt(i)%> [<%=VCTR_CONT_REF_NO.elementAt(i)%>]</option>
										<%} %>
									</select>
								</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Business Unit<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="bu_unit" onchange="checkCTR();">
										<option value="0">--Select--</option>
										<%-- <%for(int i=0;i<VCTR_BU_PLANT_SEQ_NO.size();i++){ %>
										<option value="<%=VCTR_BU_PLANT_SEQ_NO.elementAt(i)%>"><%=VCTR_BU_PLANT_ABBR.elementAt(i)%></option>
										<%} %> --%>
									</select>
								</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Plant<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="plant_cd" onchange="checkCTR();">
										<option value="0">--Select--</option>
										<%-- <%for(int i=0;i<VCTR_PLANT_SEQ_NO.size();i++){ %>
										<option value="<%=VCTR_PLANT_SEQ_NO.elementAt(i)%>"><%=VCTR_PLANT_ABBR.elementAt(i)%></option>
										<%} %> --%>
									</select>
								</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>CTR<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
				      					<input type="text" class="form-control form-control-sm" name="ctr_ref" value="" onchange="checkSpace(this);if (document.forms[0].opration.value === 'INSERT') { validateCTR(this); }">
				      					<input type="hidden" class="form-control form-control-sm" name="seq_no" value="">
				    				</div>
				    			</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Product<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="prod_cd" onchange="fetchMolecules(this)">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCTR_PROD_CD.size();i++){ %>
										<option value="<%=VCTR_PROD_CD.elementAt(i)%>"><%=VCTR_PROD_ABBR.elementAt(i)%></option>
										<%} %>
									</select>
								</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Molecule<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="mole_cd" onchange="">
										<option value="0">--Select--</option>
										<%-- <%for(int i=0;i<VCTR_PROD_CD.size();i++){ %>
										<option value="<%=VCTR_PROD_CD.elementAt(i)%>"><%=VCTR_PROD_ABBR.elementAt(i)%></option>
										<%} %> --%>
									</select>
								</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Eff. Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="eff_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);" 
			      						onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    		</div>
						</div>
					</div>
					<span id="alert"></span>
	  			</div>
	  		</div>
	  		<div class="modal-footer cdfooter">
        		<div class="d-flex justify-content-between">
					<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
					<%if(write_access.equals("Y") && !cpStatus.equals("N")){ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();" id="submit_btn">
					<%}else{ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
					<%} %>
				</div>
      		</div>
		</div>
	</div>
</div>

<script>
function fetchBuDtl(obj)
{
	//map=counterparty_cd+"@@"+agmt_no+"@@"+cont_no+"@@"+cont_type+"@@"+cargo_no;
	//alert(obj.value);
	var trader_cd=document.forms[0].trader_cd.value;
	var cont_dtl=obj.value;
	var temp = cont_dtl.split("@@");
	var counterparty_cd=temp[0];
	var agmt_no=temp[1];
	var cont_no=temp[2];
	var cont_type=temp[3];
	var cargo_no=temp[4];
	
	document.getElementById("loading").style.visibility = "visible";
	$.post("../servlet/DB_Purchase_CTR_Ajax?setCallType=BUY_CONT_BU_DTL&trader_cd="+counterparty_cd+"&agmt_no="+agmt_no+"&cont_no="+cont_no+"&cont_type="+cont_type+"&cargo_no="+cargo_no, function(responseJson){
		var option = "<option value='0'>---Select---</option>"
		$.each(responseJson,function(index, json) {
			$.each(json.BU_DTL,function(index_1, json_1) {
				option+="<option value="+json_1.SEQ_NO+" >"+json_1.PLANT_NM+"</option>"
			});
		});
		document.forms[0].bu_unit.innerHTML=option;
		checkCTR();
	});
	document.getElementById("loading").style.visibility = "hidden";
}

function fetchPlantDtl(obj)
{
	//map=counterparty_cd+"@@"+agmt_no+"@@"+cont_no+"@@"+cont_type+"@@"+cargo_no;
	//alert(obj.value);
	var trader_cd=document.forms[0].trader_cd.value;
	var cont_dtl=obj.value;
	var temp = cont_dtl.split("@@");
	var counterparty_cd=temp[0];
	var agmt_no=temp[1];
	var cont_no=temp[2];
	var cont_type=temp[3];
	var cargo_no=temp[4];
	
	document.getElementById("loading").style.visibility = "visible";
	$.post("../servlet/DB_Purchase_CTR_Ajax?setCallType=BUY_CONT_PLANT_DTL&trader_cd="+counterparty_cd+"&agmt_no="+agmt_no+"&cont_no="+cont_no+"&cont_type="+cont_type+"&cargo_no="+cargo_no, function(responseJson){
		var option = "<option value='0'>---Select---</option>"
		$.each(responseJson,function(index, json) {
			$.each(json.PLANT_DTL,function(index_1, json_1) {
				option+="<option value="+json_1.SEQ_NO+">"+json_1.PLANT_NM+"</option>"
			});
		});
		document.forms[0].plant_cd.innerHTML=option;
		checkCTR();
	});
	document.getElementById("loading").style.visibility = "hidden";
}

function fetchMolecules(obj)
{
	document.getElementById("loading").style.visibility = "visible";
	$.post("../servlet/DB_Purchase_CTR_Ajax?setCallType=MOLECULE&prod_cd="+obj.value, function(responseJson){
		var option = "<option value='0'>---Select---</option>"
		$.each(responseJson,function(index, json) {
			$.each(json.MOLE_DTL,function(index_1, json_1) {
				option+="<option value="+json_1.SEQ_NO+">"+json_1.MOLE_ABBR+"</option>"
			});
		});
		document.forms[0].mole_cd.innerHTML=option;
	});
	document.getElementById("loading").style.visibility = "hidden";
}

function checkCTR()
{
	//map=counterparty_cd+"@@"+agmt_no+"@@"+cont_no+"@@"+cont_type+"@@"+cargo_no;
	//alert(obj.value);
	if(ctr_contract_dtl == "0" || ctr_contract_dtl == "")
	{
	    return;
	}
	var trader_cd=document.forms[0].trader_cd.value;
	var ctr_contract_dtl=document.forms[0].ctr_contract_dtl.value;
	var temp = ctr_contract_dtl.split("@@");
	var counterparty_cd=temp[0];
	var agmt_no=temp[1];
	var cont_no=temp[2];
	var cont_type=temp[3];
	var cargo_no=temp[4];
	var plant_seq=document.forms[0].plant_cd.value;
	var bu_seq=document.forms[0].bu_unit.value;
	var opration=document.forms[0].opration.value;
	
	if(plant_seq!=0 && bu_seq!=0 && opration=='INSERT')
	{
		document.getElementById("loading").style.visibility = "visible";
		$.post("../servlet/DB_Purchase_CTR_Ajax?setCallType=CHECK_CTR&trader_cd="+counterparty_cd+"&agmt_no="+agmt_no+"&cont_no="+cont_no+"&cont_type="+cont_type+"&cargo_no="+cargo_no+"&plant_seq="+plant_seq+"&bu_seq="+bu_seq, function(responseJson){
			var info="";
			var flag=false;
			$.each(responseJson,function(index, json) {
				$.each(json.chkCtr,function(index_1, json_1) {
					if(json_1.count>0)
					{
						flag=true;
						info+="<p style='color:red;'>CTR is already generated for selected Business Unit and Plant!.</p>";
					}
					else
					{
						flag=false;
					}
				});
			});
			if(flag)
			{
				document.getElementById("alert").innerHTML="<%=utilmsg.errorMessage("<b>CTR is already generated for selected Business Unit and Plant!!</b>") %>";
				document.getElementById("submit_btn").disabled=true;
			}
			else
			{
				document.getElementById("alert").innerHTML="";
				if(document.forms[0].write_access.value=='Y')
				{
					document.getElementById("submit_btn").disabled=false;
				}
			}
		});
		document.getElementById("loading").style.visibility = "hidden";
	}
	if(opration=='MODIFY')
	{
		document.getElementById("alert").innerHTML="";
		if(document.forms[0].write_access.value=='Y')
		{
			document.getElementById("submit_btn").disabled=false;
		}
	}
}

function validateCTR(obj)
{
	document.getElementById("loading").style.visibility = "visible";
	$.post("../servlet/DB_Purchase_CTR_Ajax?setCallType=CHECK_CTR_REF&ctr_ref="+obj.value, function(responseJson){
		var info="";
		var flag=false;
		$.each(responseJson,function(index, json) {
			$.each(json.chkCtrRef,function(index_1, json_1) {
				if(json_1.count>0)
				{
					flag=true;
				}
			});
		});
		if(flag)
		{
			alert("CTR "+obj.value+" is already present!");
			obj.value="";
		}
	});
	document.getElementById("loading").style.visibility = "hidden";
}

function checkIsCtrNominated(obj)
{
	document.getElementById("loading").style.visibility = "visible";
	$.post("../servlet/DB_Purchase_CTR_Ajax?setCallType=CHECK_CTR_NOM&ctr_ref="+obj.value, function(responseJson){
		var info="";
		var flag=false;
		$.each(responseJson,function(index, json) {
			$.each(json.chkCtrNom,function(index_1, json_1) {
				if(json_1.count>0)
				{
					flag=true;
				}
			});
		});
		if(flag)
		{
			obj.setAttribute("title", "This CTR is already nominated");
			obj.disabled=true;
		}
		else
		{
			obj.setAttribute("title", "");
			obj.disabled=false;
		}
	});
	document.getElementById("loading").style.visibility = "hidden";
}
</script>

<input type="hidden" name="option" value="MAP_PUR_CTR_MST">
<input type="hidden" name="opration" value="INSERT">
<input type="hidden" name="owner_abbr" value="<%=owner_abbr%>">

<input type="hidden" name="counterparty_cd" value="<%=trader_cd%>">
<input type="hidden" name="temp_from_dt" value="<%=from_dt%>">
<input type="hidden" name="temp_to_dt" value="<%=to_dt%>">
<input type="hidden" name="trader_cd_ctr" value="">
<input type="hidden" name="bu_unit_ctr" value="">
<input type="hidden" name="plant_seq_ctr" value="">
<input type="hidden" name="ctr_no" value="">
<input type="hidden" name="temp_ctr_contract_dtl" value="">

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