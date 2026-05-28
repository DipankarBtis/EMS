<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>
function refresh()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var segmentType = document.forms[0].segmentType.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;

	var flag=true;
	var msg="";
	
	var u = document.forms[0].u.value;
	
	if(trim(from_dt)!="" && trim(to_dt)!="")
	{
		if(flag)
		{
			var url = "rpt_purchase_summary.jsp?counterparty_cd="+counterparty_cd+"&u="+u+
					"&segmentType="+segmentType+"&from_dt="+from_dt+"&to_dt="+to_dt;
		
			document.getElementById("loading").style.visibility = "visible";
			location.replace(url);
		}
	}
}

function exportToXls()
{
	var sysdate = document.forms[0].sysdate.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var segmentType = document.forms[0].segmentType.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	sysdate = sysdate.toString();
	sysdate = sysdate.split('/').join('');
	
	var url = "xls_purchase_summary.jsp?fileName=Purchse Summary "+sysdate+".xls&counterparty_cd="+counterparty_cd+"&segmentType="+segmentType+"&from_dt="+from_dt+"&to_dt="+to_dt;

	location.replace(url);
}
</script>
<%@ include file="../util/common_js.jsp"%>
</head>
<jsp:useBean class="com.etrm.fms.purchase.DataBean_Trader_Contract_Mst" id="purchaseSum" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String segmentType=request.getParameter("segmentType")==null?"0":request.getParameter("segmentType");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

purchaseSum.setCallFlag("PURCHASE_SUMMARY");
purchaseSum.setComp_cd(owner_cd);
purchaseSum.setSegmentType(segmentType);
purchaseSum.setCounterparty_cd(counterparty_cd);
purchaseSum.setFrom_dt(from_dt);
purchaseSum.setTo_dt(to_dt);
purchaseSum.init();

Vector VCOUNTERPARTY_CD = purchaseSum.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = purchaseSum.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = purchaseSum.getVCOUNTERPARTY_ABBR();	
Vector VMST_COUNTERPARTY_CD = purchaseSum.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = purchaseSum.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = purchaseSum.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_NO = purchaseSum.getVCONT_NO();	
Vector VCONT_REV_NO = purchaseSum.getVCONT_REV_NO();
Vector VCONT_NAME = purchaseSum.getVCONT_NAME();	
Vector VSTART_DT = purchaseSum.getVSTART_DT();
Vector VEND_DT = purchaseSum.getVEND_DT();
Vector VRATE = purchaseSum.getVRATE();
Vector VRATE_UNIT = purchaseSum.getVRATE_UNIT();
Vector VCONT_STATUS = purchaseSum.getVCONT_STATUS();
Vector VCONT_STATUS_FLG = purchaseSum.getVCONT_STATUS_FLG();	
Vector VPRICE_TYPE = purchaseSum.getVPRICE_TYPE();
Vector VBOOKED_QTY = purchaseSum.getVBOOKED_QTY();
Vector VAGMT_NO = purchaseSum.getVAGMT_NO();		
Vector VAGMT_REV_NO = purchaseSum.getVAGMT_REV_NO();	
Vector VCONTRACT_TYPE = purchaseSum.getVCONTRACT_TYPE();
Vector VMIN_ALLOC_DT = purchaseSum.getVMIN_ALLOC_DT();
Vector VMAX_ALLOC_DT = purchaseSum.getVMAX_ALLOC_DT();
Vector VUNLOADED_QTY = purchaseSum.getVUNLOADED_QTY();
Vector VAVAILABLE_FOR_SALE = purchaseSum.getVAVAILABLE_FOR_SALE();	
Vector VBU_POINT=purchaseSum.getVBU_POINT();
Vector VTRADER_PLANT=purchaseSum.getVTRADER_PLANT();
Vector VCONT_REF_NO=purchaseSum.getVCONT_REF_NO();

Vector VDISPLAY_SEGMENT = purchaseSum.getVDISPLAY_SEGMENT();
Vector VTEMP_SEGMENT_TYPE = purchaseSum.getVTEMP_SEGMENT_TYPE();
Vector VSEGMENT_TYPE = purchaseSum.getVSEGMENT_TYPE();		
Vector VDISPLAY_SEGMENT_TYP = purchaseSum.getVDISPLAY_SEGMENT_TYP();
Vector VINDEX = purchaseSum.getVINDEX();

//Added by Pratham Bhatt for Cargo name
Vector VCARGO_NAME = purchaseSum.getVCARGO_NAME();
Vector VBALANCE_QTY = purchaseSum.getVBALANCE_QTY();		
Vector VBAL_INFO = purchaseSum.getVBAL_INFO();
Vector VBOOKED_INFO = purchaseSum.getVBOOKED_INFO();
Vector VTOTAL_QUANTITY = purchaseSum.getVTOTAL_QUANTITY();
Vector VTOTAL_UNLOADED_QUANTITY = purchaseSum.getVTOTAL_UNLOADED_QUANTITY();
Vector VTOTAL_BALANCE = purchaseSum.getVTOTAL_BALANCE();

Vector VCOUNTERPARTY_STATUS = purchaseSum.getVCOUNTERPARTY_STATUS();
Vector VSTATUS_EFF_DT = purchaseSum.getVSTATUS_EFF_DT();

double totalQty=purchaseSum.getTotalQty();		
double UnloadedtotalQty=purchaseSum.getUnloadedtotalQty();	
double AvailabletotalQty=purchaseSum.getAvailabletotalQty();	

String balTotalQty_str = purchaseSum.getBalTotalQty_str();
String bookedToolTip = purchaseSum.getBookedToolTip();
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
					<div class="d-flex justify-content-between">
						<div class="topheader">
					    	Purchase Summary Report
					    </div>
					    <div align="right" onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Contract</b></label>
								</div>
								<div class="col">
									<select class="form-select form-select-sm" name="segmentType" onchange="">
										<option value="0">--All--</option>
										<%for(int i=0;i<VDISPLAY_SEGMENT_TYP.size();i++){ %>
										<option value="<%=VDISPLAY_SEGMENT_TYP.elementAt(i)%>"><%=VDISPLAY_SEGMENT.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].segmentType.value="<%=segmentType%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Counterparty</b></label>
								</div>
								<div class="col">
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="">
										<option value="0">--All--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_ABBR.size();i++){ %>
										<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>From</b></label>
								</div>
								<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);checkDateRange(this,document.forms[0].to_dt);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
								<div class="col-auto">
									<label class="form-label"><b>To</b></label>
								</div>
								<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);checkDateRange(document.forms[0].from_dt,this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    			<div class="col-auto">
									<input type="button" class="btn btn-warning com-btn" value="Apply Filter" onclick="refresh();">
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<%int i=0;int k=0; int m = 0;
					for(int j=0; j<VTEMP_SEGMENT_TYPE.size(); j++){ 
						int index = Integer.parseInt(""+VINDEX.elementAt(j));
						String seg_type="";
						if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("D")){
							seg_type="Domestic NG Purchase";
						}
						else if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("T"))
						{
							seg_type="In Tank LNG/RLNG Purchase";
						} 
						else if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("I"))
						{
							seg_type="IGX Purchase";
						}
						else if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("N"))
						{
							seg_type="LNG Cargo";
						}
						else if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("L"))
						{
							seg_type="LTCORA";
						}
						if(VTEMP_SEGMENT_TYPE.size()==1)
						{
							if(segmentType.equals("D"))
							{
								m=0;
							}
							else if(segmentType.equals("T"))
							{
								m=1;
							}
							else if(segmentType.equals("I"))
							{
								m=2;
							}
							else if(segmentType.equals("N"))
							{
								m=3;
							}
							else if(segmentType.equals("L"))
							{
								m=4;
							}
						}
						else
						{
							m=j;
						}
					if(j!=0)
					{
					%>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div> 
					<%} %>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=seg_type%></label>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="payable<%=j%>">
								<thead>
										<tr>
											<th>Sr#</th>
											<th>
												Counterparty
												<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Customer<%=j %>" onkeyup="Search(this,'1','<%=j%>');" placeholder="Search.." style="width:100px"/></div>	
											</th>
											<%if(VDISPLAY_SEGMENT_TYP.elementAt(m).equals("N") || VDISPLAY_SEGMENT_TYP.elementAt(m).equals("L")){%>
											<th>
												Cargo#
												<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Cargo<%=j %>" onkeyup="Search(this,'2','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
											</th>
												<%if( VDISPLAY_SEGMENT_TYP.elementAt(m).equals("L")){%>
												<th>
													Cont Ref#
													<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_cont_ref<%=j %>" onkeyup="Search(this,'3','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
												</th>
												<th>
													Cargo Arrival - Storage Window
													<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_duration_window<%=j %>" onkeyup="Search(this,'4','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
												</th>
												<%}else{ %>
												<th>
													Cargo Ref#
													<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_cargo_ref<%=j %>" onkeyup="Search(this,'3','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
												</th>
												<th>
													Cargo Arrival Window
													<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_arrival_window<%=j %>" onkeyup="Search(this,'4','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
												</th>
												<%} %>
											<%}else{ %>
												<th>
													Contract#
													<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Cont<%=j %>" onkeyup="Search(this,'2','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
												</th>
													<%if(VDISPLAY_SEGMENT_TYP.elementAt(m).equals("I")){ %>
														<th>
															Trade Ref#
															<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_trade_ref<%=j %>" onkeyup="Search(this,'3','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
														</th>
													<%}else{ %>
														<th>
															Cont Ref#
															<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_cont_ref<%=j %>" onkeyup="Search(this,'3','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
														</th>
													<%} %>
												<th>
													Contract Period
													<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_cont_period<%=j %>" onkeyup="Search(this,'4','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
												</th>
											<%} %>
											
											<th>
												Status
												<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_status<%=j %>" onkeyup="Search(this,'5','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
											</th>
											<th >Price Type</th>
											<th >Price</th>
											<th >Currency/MMBTU</th>
											<th >Business Unit</th>
											<th >Trader Plants</th>
											<%if(VDISPLAY_SEGMENT_TYP.elementAt(m).equals("N")){ %>
											<th>Actual Arrival Date</th>
											<th>Q&Q Certificate Date</th>
											<%}else{%>
												<th >Allocation Start Date</th>
												<th >Last Allocation Date</th>
											<%} %>
											<th <%if(VDISPLAY_SEGMENT_TYP.elementAt(m).equals("L")){%>title="<%=bookedToolTip%>"<%} %>>MMBTU Booked</th>
											<%if(VDISPLAY_SEGMENT_TYP.elementAt(m).equals("L")){ %>
											<th title="Allocated Quantity">Regassified MMBTU </th>
											<th title="Booked Qty = Booked MMBTU - Regassified Qty"> Balance MMBTU </th>
											<%}else{ %>
											<th >MMBTU Unloaded</th>
											<th title="Booked Qty = Booked MMBTU - MMBTU Unloaded"> Balance MMBTU </th>
											<%} %>
										</tr>
								</thead>
								<tbody>
								<%
								k=0;
								if(index > 0){ %>
									<%for(i=i;i<VCOUNTERPARTY_CD.size(); i++){ 
										k+=1;
									%>
										<tr>
											<td align="center"><%=k%></td>
											<%
												String title = ""+VCOUNTERPARTY_CD.elementAt(i);

												if (VCOUNTERPARTY_STATUS.elementAt(i).equals("E")||VCOUNTERPARTY_STATUS.elementAt(i).equals("N")) {
												    title += "\neff. date:" + VSTATUS_EFF_DT.elementAt(i);
												}
											%>
											<td title="<%=title%>">
												<%=VCOUNTERPARTY_NM.elementAt(i)%>
												<span
												<%if(VCOUNTERPARTY_STATUS.elementAt(i).equals("N")){ %>class='alert alert-danger' 
												<%}else if(VCOUNTERPARTY_STATUS.elementAt(i).equals("E")){ %>class='alert alert-warning' 
												<%} %>
												><b><%if(VCOUNTERPARTY_STATUS.elementAt(i).equals("N")){ %>De-active
												<%}else if(VCOUNTERPARTY_STATUS.elementAt(i).equals("E")){ %>E-Rate
												<%} %></b></span>
											</td>
											<%if(VDISPLAY_SEGMENT_TYP.elementAt(m).equals("N")){%>
											<td align="center" title="REV : <%=VCONT_REV_NO.elementAt(i)%>"><%=VCARGO_NAME.elementAt(k-1)%></td>
											<%}else if (VDISPLAY_SEGMENT_TYP.elementAt(m).equals("L")){%>
											<td align="center" title="REV : <%=VCONT_REV_NO.elementAt(i)%>"><%=VCONTRACT_TYPE.elementAt(i)%></td>
											<%}else{ %>
											<td align="center" title="REV : <%=VCONT_REV_NO.elementAt(i)%>"><%=VCONTRACT_TYPE.elementAt(i)%></td>
											<%-- <td align="center" title="REV : <%=VCONT_REV_NO.elementAt(i)%>"><%=VCONTRACT_TYPE.elementAt(i)%><%=VCONT_NO.elementAt(i)%></td> --%>
											<%} %>
											<td><%=VCONT_REF_NO.elementAt(i)%></td>
											<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
											<td align="center">
												<%if(VCONT_STATUS.elementAt(i).equals("Cancelled")){%>
												<font color="red"><%=VCONT_STATUS.elementAt(i)%></font>
												<%}else if(VCONT_STATUS.elementAt(i).equals("Confirmed")){%>
												<font color="green"><%=VCONT_STATUS.elementAt(i)%></font>
												<%}else{ %>
												<%=VCONT_STATUS.elementAt(i)%>
												<%} %>
											</td>
											<td align="center"><%=VPRICE_TYPE.elementAt(i)%></td>
											<td align="right"><%=VRATE.elementAt(i)%></td>
											<td align="center"><%=VRATE_UNIT.elementAt(i)%></td>
											<td align="center"><%=VBU_POINT.elementAt(i)%></td>
											<td align="center"><%=VTRADER_PLANT.elementAt(i)%></td>
											<td align="center"><%=VMIN_ALLOC_DT.elementAt(i) %></td>
											<td align="center"><%=VMAX_ALLOC_DT.elementAt(i) %></td>
											<td align="right" <%if(VDISPLAY_SEGMENT_TYP.elementAt(m).equals("L")){%>title="Booked MMBTU = <%=VBOOKED_INFO.elementAt(k-1)%>"<%} %>><%=VBOOKED_QTY.elementAt(i)%></td>
											<td align="right"><%=VUNLOADED_QTY.elementAt(i)%></td>
<%-- 											<%if(VDISPLAY_SEGMENT_TYP.elementAt(m).equals("L")){%>  --%>
											<td align="right" title="Balance Qty = <%=VBAL_INFO.elementAt(i)%>"><%=VBALANCE_QTY.elementAt(i)%></td>
<%-- 											<%}%>  --%>
										</tr>
											
										<%
											if(k==index)
											{
												i=i+1;
												break;
											}
											%>
									<%}%>
									<tbody>
									<tr>
										<td colspan="13" align="right">Total :</td>
										<td align="right"><%=VTOTAL_QUANTITY.elementAt(j) %></td>
										<td align="right"><%=VTOTAL_UNLOADED_QUANTITY.elementAt(j) %></td>
										<%-- <%if(VDISPLAY_SEGMENT_TYP.elementAt(m).equals("L")){ %> --%>
										<td><%=VTOTAL_BALANCE.elementAt(j)%></td>
										<%-- <%}%> --%>
										<%--<td align="right"><%=UnloadedtotalQty%></td> --%>
										<%-- <td align="right"><%=AvailabletotalQty%></td>--%>
									</tr>
								</tbody>
								<%}else{ %>
									<tr>
										<%if(VDISPLAY_SEGMENT_TYP.elementAt(m).equals("L")){ %>
										<td colspan="16" align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></td>
										<%}else{ %>
										<td colspan="16" align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></td>
										<%}%>
									</tr>
								<%} %>
								 
							</table>
						</div>
					</div>
					<%} %>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div align="center"><%=utilmsg.infoMessage("<b>Note: The date filter will show result with respect to Contract Period or Cargo Arrival Window!</b>") %></div>
   					</div>
   				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" name="sysdate" value="<%=sysdate%>">
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
function Search(obj, indx, j) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("payable"+j);
  	
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