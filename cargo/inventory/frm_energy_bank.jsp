<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function doAllocation()
{
	var chk = document.forms[0].chk;
	var counterparty_cd = document.forms[0].counterparty_cd;
	var agmt_no = document.forms[0].agmt_no;
	var agmt_rev_no = document.forms[0].agmt_rev_no;
	var cont_no = document.forms[0].cont_no;
	var cont_rev_no = document.forms[0].cont_rev_no;
	var contract_type = document.forms[0].contract_type;
	var balance_qty = document.forms[0].balance_qty;
	var cargo_no = document.forms[0].cargo_no;
	
	var cargo_status_flg = document.forms[0].cargo_status_flg;
	
	var chk_count = parseInt("0");
	
	var msg="";
	var flag=true;
	
	var multiCountpty="";
	var multiAgmtNo="";
	var multiAgmtRev="";
	var multiContNo="";
	var multiContRev="";
	var multiContTyp="";
	var multiCargoNo="";
	
	if(chk!=null && chk!=undefined)
	{
		if(chk.length!=undefined)
		{
			for(var i=0;i<chk.length;i++)
			{
				if(chk[i].checked)
				{
					chk_count++;
					
					if(parseInt(balance_qty[i].value) <= 0)
					{
						msg="Select Cargo having Balance Qty > 0! ";
						flag=false;
						break;
					}
					else if(cargo_status_flg[i].value == "X" && contract_type[i].value == "N")
					{
						msg="Unselect Cargo With Canceled Status and Proceed!";
						flag=false;
						break;
					}
					else
					{
						if(multiContNo == "")
						{
							multiCountpty+=counterparty_cd[i].value;
							multiAgmtNo+=agmt_no[i].value;
							multiAgmtRev+=agmt_rev_no[i].value;
							multiContNo+=cont_no[i].value;
							multiContRev+=cont_rev_no[i].value;
							multiContTyp+=contract_type[i].value;
							multiCargoNo+=cargo_no[i].value;
						}
						else
						{
							multiCountpty+="@@"+counterparty_cd[i].value;
							multiAgmtNo+="@@"+agmt_no[i].value;
							multiAgmtRev+="@@"+agmt_rev_no[i].value;
							multiContNo+="@@"+cont_no[i].value;
							multiContRev+="@@"+cont_rev_no[i].value;
							multiContTyp+="@@"+contract_type[i].value;
							multiCargoNo+="@@"+cargo_no[i].value;
						}
					}
				}
			}
		}
		else
		{
			if(chk.checked)
			{
				chk_count++;
				
				if(parseInt(balance_qty.value) <= 0)
				{
					msg="Select Cargo having Balance Qty > 0! ";
					flag=false;
				}
				else if(cargo_status_flg.value == "X" && contract_type.value == "N")
				{
					msg="Select Cargo having Confirmed Status! ";
					flag=false;
				}
				else
				{
					if(multiContNo == "")
					{
						multiCountpty+=counterparty_cd.value;
						multiAgmtNo+=agmt_no.value;
						multiAgmtRev+=agmt_rev_no.value;
						multiContNo+=cont_no.value;
						multiContRev+=cont_rev_no.value;
						multiContTyp+=contract_type.value;
						multiCargoNo+=cargo_no.value;
					}
					else
					{
						multiCountpty+="@@"+counterparty_cd.value;
						multiAgmtNo+="@@"+agmt_no.value;
						multiAgmtRev+="@@"+agmt_rev_no.value;
						multiContNo+="@@"+cont_no.value;
						multiContRev+="@@"+cont_rev_no.value;
						multiContTyp+="@@"+contract_type.value;
						multiCargoNo+="@@"+cargo_no.value;
					}
				}
			}		
		}
	}
	
	if(parseInt(chk_count) == 0)
	{
		alert("Select atleast ONE(1) Purchase Contract# for Allocation!");
	}
	else if(!flag)
	{
		alert(msg);
	}
	else
	{
		var u = document.forms[0].u.value;
		
		var url = "frm_allocation.jsp?multiCountpty="+multiCountpty+"&multiAgmtNo="+multiAgmtNo+"&multiAgmtRev="+multiAgmtRev+
				"&multiContNo="+multiContNo+"&multiContRev="+multiContRev+"&multiContTyp="+multiContTyp+"&multiCargoNo="+multiCargoNo+
				"&u="+u;	
		location.replace(url);
	}
}

function doPriceModification()
{
	var chk = document.forms[0].chk;
	var counterparty_cd = document.forms[0].counterparty_cd;
	var agmt_no = document.forms[0].agmt_no;
	var agmt_rev_no = document.forms[0].agmt_rev_no;
	var cont_no = document.forms[0].cont_no;
	var cont_rev_no = document.forms[0].cont_rev_no;
	var contract_type = document.forms[0].contract_type;
	var balance_qty = document.forms[0].balance_qty;
	
	var chk_count = parseInt("0");
	
	var msg="";
	var flag=true;
	
	var multiCountpty="";
	var multiAgmtNo="";
	var multiAgmtRev="";
	var multiContNo="";
	var multiContRev="";
	var multiContTyp="";
	
	if(chk!=null && chk!=undefined)
	{
		if(chk.length!=undefined)
		{
			for(var i=0;i<chk.length;i++)
			{
				if(chk[i].checked)
				{
					chk_count++;
					
					if(multiContNo == "")
					{
						multiCountpty+=counterparty_cd[i].value;
						multiAgmtNo+=agmt_no[i].value;
						multiAgmtRev+=agmt_rev_no[i].value;
						multiContNo+=cont_no[i].value;
						multiContRev+=cont_rev_no[i].value;
						multiContTyp+=contract_type[i].value;
					}
					else
					{
						multiCountpty+="@"+counterparty_cd[i].value;
						multiAgmtNo+="@"+agmt_no[i].value;
						multiAgmtRev+="@"+agmt_rev_no[i].value;
						multiContNo+="@"+cont_no[i].value;
						multiContRev+="@"+cont_rev_no[i].value;
						multiContTyp+="@"+contract_type[i].value;
					}
				}
			}
		}
		else
		{
			if(chk.checked)
			{
				chk_count++;
				
				if(multiContNo == "")
				{
					multiCountpty+=counterparty_cd.value;
					multiAgmtNo+=agmt_no.value;
					multiAgmtRev+=agmt_rev_no.value;
					multiContNo+=cont_no.value;
					multiContRev+=cont_rev_no.value;
					multiContTyp+=contract_type.value;
				}
				else
				{
					multiCountpty+="@"+counterparty_cd.value;
					multiAgmtNo+="@"+agmt_no.value;
					multiAgmtRev+="@"+agmt_rev_no.value;
					multiContNo+="@"+cont_no.value;
					multiContRev+="@"+cont_rev_no.value;
					multiContTyp+="@"+contract_type.value;
				}
			}		
		}
	}
	
	if(parseInt(chk_count) == 0)
	{
		alert("Select atleast ONE(1) Purchase Contract# for Price Modification!");
	}
	else if(!flag)
	{
		alert(msg);
	}
	else
	{
		var u = document.forms[0].u.value;
		
		var url = "frm_trade_cont_price_modification.jsp?multiCountpty="+multiCountpty+"&multiAgmtNo="+multiAgmtNo+"&multiAgmtRev="+multiAgmtRev+
				"&multiContNo="+multiContNo+"&multiContRev="+multiContRev+"&multiContTyp="+multiContTyp+
				"&u="+u;	
		location.replace(url);
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.inventory.DataBean_EnergyBank" id="energyBank" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sys_dt = utildate.getSysdate();

energyBank.setCallFlag("ENERGY_BANK");
energyBank.setComp_cd(owner_cd);
energyBank.init();

Vector VINDEX = energyBank.getVINDEX();
Vector VCARGO_POOL_FLAG = energyBank.getVCARGO_POOL_FLAG();
Vector VCARGO_POOL_NM = energyBank.getVCARGO_POOL_NM();

Vector VCOUNTERPARTY_CD = energyBank.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = energyBank.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = energyBank.getVCOUNTERPARTY_ABBR();
Vector VCONT_NO = energyBank.getVCONT_NO();
Vector VCONT_REV_NO = energyBank.getVCONT_REV_NO();
Vector VCONT_NAME = energyBank.getVCONT_NAME();
Vector VSTART_DT = energyBank.getVSTART_DT();
Vector VEND_DT = energyBank.getVEND_DT();
Vector VRATE = energyBank.getVRATE();
Vector VRATE_UNIT = energyBank.getVRATE_UNIT();
Vector VRATE_UNIT_NM = energyBank.getVRATE_UNIT_NM();
Vector VCONT_STATUS = energyBank.getVCONT_STATUS();
Vector VCONT_STATUS_FLG = energyBank.getVCONT_STATUS_FLG();
Vector VPRICE_TYPE = energyBank.getVPRICE_TYPE();
Vector VBOOKED_QTY = energyBank.getVBOOKED_QTY();
Vector VAGMT_NO = energyBank.getVAGMT_NO();
Vector VAGMT_REV_NO = energyBank.getVAGMT_REV_NO();
Vector VCONTRACT_TYPE = energyBank.getVCONTRACT_TYPE();
Vector VCONTRACT_TYPE_NM = energyBank.getVCONTRACT_TYPE_NM();
Vector VMIN_ALLOC_DT = energyBank.getVMIN_ALLOC_DT();
Vector VMAX_ALLOC_DT = energyBank.getVMAX_ALLOC_DT();
Vector VUNLOADED_QTY = energyBank.getVUNLOADED_QTY();
Vector VUNLOADED_QTY_INFO = energyBank.getVUNLOADED_QTY_INFO();
Vector VAVAIL_FOR_SALE_QTY = energyBank.getVAVAIL_FOR_SALE_QTY();
Vector VAVAIL_FOR_SALE_QTY_INFO = energyBank.getVAVAIL_FOR_SALE_QTY_INFO();
Vector VALLOCATED_QTY = energyBank.getVALLOCATED_QTY();
Vector VBALANCE_QTY = energyBank.getVBALANCE_QTY();
Vector VCONT_REF = energyBank.getVCONT_REF();
Vector VREMARK = energyBank.getVREMARK();

Vector VCARGO_NO = energyBank.getVCARGO_NO();
Vector VPURCHASE_MAP_ID = energyBank.getVPURCHASE_MAP_ID();
Vector VCARGO_STATUS_FLG = energyBank.getVCARGO_STATUS_FLG();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form>
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
					<div class="topheader">
				    	Energy Bank
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">  
							<div class="form-group row">
								<div class="col-auto">
				    				<input type="button" class="btn btn-sm request_btn" value="MMBTU Allocation" onclick="doAllocation();">
				    			</div>
				    			<div class="col-auto" style="display:none;">
				    				<input type="button" class="btn btn-sm request_btn" value="Reconcile Allocation">
				    			</div>
				    			<div class="col-auto">
				    				<input type="button" class="btn btn-sm request_btn" value="Allocation Transfer">
				    			</div>
				    			<div class="col-auto">
				    				<input type="button" class="btn btn-sm request_btn" value="Price Modification" onclick="doPriceModification();">
				    			</div>
				    			<div class="col-auto" style="display:none;">
				    				<input type="button" class="btn btn-sm request_btn" value="Modify Pseudo MMBTU">
				    			</div>
				    			<div class="col-auto">
				    				<input type="button" class="btn btn-sm request_btn" value="Expected Contract Cancellation">
				    			</div>
				  			</div>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
				</div>
			</div>
		</div>
	</div>
	<%int i=0;int k=0;
	for(int j=0; j<VCARGO_POOL_FLAG.size(); j++){ 
	int index = Integer.parseInt(""+VINDEX.elementAt(j));
	String tbl_id = "tbl_"+VCARGO_POOL_FLAG.elementAt(j);
	%>
	&nbsp;
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="topheader">
				    	<%=VCARGO_POOL_NM.elementAt(j) %>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="<%=tbl_id%>">
								<thead>
									<tr>
										<th></th>
										<th>
											Purchase Contract#
											<br>
											<div align="center">
												<input class="form-control form-control-sm" type="text" id="<%=VCARGO_POOL_FLAG.elementAt(j)%>_pur_cont" 
												onkeyup="Search(this,'1','<%=tbl_id%>');" placeholder="Search.." style="width:100px"/>
											</div>
										</th>
										<th>Contract Type</th>
										<th>Counterparty</th>
										<th>Contract Name</th>
										<th>Contract/Cargo/Trade Ref#</th>
										<th>Status</th>
										<th>Contract Period</th>
										<th>Allocation Start Date</th>
										<th>Last Allocation Date</th>
										<th>Price Type</th>
										<th>Currency/MMBTU</th>
										<th>Price</th>
										<th>MMBTU Booked</th>
										<th>MMBTU Unloaded <%if(!VCARGO_POOL_FLAG.elementAt(j).equals("E")){ %><font color="#a0333a">(& Projected)</font><br><%} %></th>
										<th><%if(VCARGO_POOL_FLAG.elementAt(j).equals("E")){ %><font color="#a0333a">Expected</font><br><%} %> MMBTU Avail for Sale </th>
										<th><%if(VCARGO_POOL_FLAG.elementAt(j).equals("E")){ %><font color="#a0333a">Expected</font><br><%} %> MMBTU Allocated</th>
										<th><%if(VCARGO_POOL_FLAG.elementAt(j).equals("E")){ %><font color="#a0333a">Expected</font><br><%} %> Balance MMBTU</th>
										<th>Remark</th>
									</tr>
								</thead>
								<tbody>
								<%k=0;
								if(index > 0)
								{ %>
									<%for(i=i; i<VCOUNTERPARTY_CD.size(); i++)
									{
										k+=1;
									%>
										<tr>
											<td align="center"><input type="checkbox" class="form-check-input" name="chk" id="chk<%=i%>"></td>
											<%-- <td align="center" title="REV : <%=VCONT_REV_NO.elementAt(i)%>"><%=VCONT_NO.elementAt(i)%></td> --%>
											<td align="center" title="REV : <%=VCONT_REV_NO.elementAt(i)%>"><%=VPURCHASE_MAP_ID.elementAt(i)%></td>
											<td align="center">
												<span
													<%if(VCONTRACT_TYPE.elementAt(i).equals("D")){ %>
								    					style="background: #66ffd9;"
								    				<%}else if(VCONTRACT_TYPE.elementAt(i).equals("N")){ %>
								    					style="background: skyblue;"
								    				<%}else if(VCONTRACT_TYPE.elementAt(i).equals("I")){ %>
								    					style="background: pink;"
								    				<%} %>	
												><b><%=VCONTRACT_TYPE_NM.elementAt(i)%></b></span>
											</td>
											<td title="<%=VCOUNTERPARTY_CD.elementAt(i)%> : <%=VCOUNTERPARTY_ABBR.elementAt(i)%>">
												<%=VCOUNTERPARTY_NM.elementAt(i)%>
												<input type="hidden" name="counterparty_cd" id="counterparty_cd<%=i%>" value="<%=VCOUNTERPARTY_CD.elementAt(i)%>">
												<input type="hidden" name="agmt_no" id="agmt_no<%=i%>" value="<%=VAGMT_NO.elementAt(i)%>">
												<input type="hidden" name="agmt_rev_no" id="agmt_rev_no<%=i%>" value="<%=VAGMT_REV_NO.elementAt(i)%>">
												<input type="hidden" name="cont_no" id="cont_no<%=i%>" value="<%=VCONT_NO.elementAt(i)%>">
												<input type="hidden" name="cont_rev_no" id="cont_rev_no<%=i%>" value="<%=VCONT_REV_NO.elementAt(i)%>">
												<input type="hidden" name="contract_type" id="contract_type<%=i%>" value="<%=VCONTRACT_TYPE.elementAt(i)%>">
												<input type="hidden" name="balance_qty" id="balance_qty<%=i%>" value="<%=VBALANCE_QTY.elementAt(i)%>">
												<input type="hidden" name="cargo_no" id="cargo_no<%=i%>" value="<%=VCARGO_NO.elementAt(i)%>">
												
												<input type="hidden" name="cargo_status_flg" id="cargo_status_flg<%=i%>" value="<%=VCARGO_STATUS_FLG.elementAt(i)%>">
											</td>
											<td><%=VCONT_NAME.elementAt(i)%></td>
											<td><%=VCONT_REF.elementAt(i)%></td>
											<td align="center"><%=VCONT_STATUS.elementAt(i)%></td>
											<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
											<td align="center"><%=VMIN_ALLOC_DT.elementAt(i) %></td>
											<td align="center"><%=VMAX_ALLOC_DT.elementAt(i) %></td>
											<td align="center"><%=VPRICE_TYPE.elementAt(i)%></td>
											<td align="center"><%=VRATE_UNIT_NM.elementAt(i)%></td>
											<td align="right"><%=VRATE.elementAt(i)%></td>
											<td align="right"><%=VBOOKED_QTY.elementAt(i)%></td>
											<td align="right" <%if(!VCARGO_POOL_FLAG.elementAt(j).equals("E")){ %>title="<%=VUNLOADED_QTY_INFO.elementAt(i)%>"<%}%>><%=VUNLOADED_QTY.elementAt(i)%></td>
											<td align="right" title="<%=VAVAIL_FOR_SALE_QTY_INFO.elementAt(i)%>"><%=VAVAIL_FOR_SALE_QTY.elementAt(i)%></td>
											<td align="right"><%=VALLOCATED_QTY.elementAt(i)%></td>
											<td align="right"><%=VBALANCE_QTY.elementAt(i)%></td>
											<td><%=VREMARK.elementAt(i)%></td>
										</tr>
										<%
										if(k==index)
										{
											i=i+1;
											break;
										}%>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="20" align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
				</div>
			</div>
		</div>
	</div>
	<%} %>
</div>
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
<script>
function Search(obj, indx, tbl_id) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById(tbl_id);
  	
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