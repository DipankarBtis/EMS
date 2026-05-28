
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ page import="java.util.*" %>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh(flag)
{
	var year = document.forms[0].year.value;
	var month = document.forms[0].month.value;
	
	var u = document.forms[0].u.value;
	
	
		var url = "rpt_monthly_hedge_compliance.jsp?year="+year+"&month="+month+"&u="+u;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
}

var newWindow;
function viewHedgeDerivatives(file)
{	
	var u = document.forms[0].u.value;
	var year = document.forms[0].year.value;
	var month = document.forms[0].month.value;
	var url="";
	
	url = "rpt_view_monthly_hedge_compliance.jsp?year="+year+"&file="+file+"&month="+month+"&u="+u;
   	  		
	if(!newWindow || newWindow.closed)
	{
		newWindow= window.open(url,"Hedge Compliance Report","top=10,left=70,width=900,height=700,scrollbars=1,menubar=1");
	}
	else 
	{
		newWindow.close();
	    newWindow= window.open(url,"Hedge Compliance Report","top=10,left=70,width=900,height=700,scrollbars=1,menubar=1");
	}			
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.derivatives.DB_Derivatives_Report" id="derivatives" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.UtilBean" id="utilbean" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String date_num = "0"; 
if(!sysdate.equals(""))
{
	String[] temp = sysdate.split("/");
	date_num=temp[0];
}
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
if(month.length() == 1)
{
	month="0"+month; 
}
//String emp_cd=(String)session.getAttribute("emp_cd");

derivatives.setCallFlag("MONTHLY_HEDGE_REPORT");
derivatives.setComp_cd(owner_cd);
derivatives.setEmp_cd(emp_cd);
derivatives.setYear(year);
derivatives.setMonth(month);
derivatives.init();

String comp_name=derivatives.getComp_name();
String comp_cin_no=derivatives.getComp_cin_no();
String comp_registered_addr=derivatives.getComp_registered_addr();
String avail_deal_years=derivatives.getAvail_deal_years();
String eligibleLimit=derivatives.getEligibleLimit();
String totalPrevMonthBal=derivatives.getTotalPrevMonthBal();
String totalQty=derivatives.getTotalQty();
String totalQtySell=derivatives.getTotalQtySell();
String totalQtySettle=derivatives.getTotalQtySettle();
String totalMonthOutstanding=derivatives.getTotalMonthOutstanding();

Vector VCOUNTERPARTY_CD = derivatives.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = derivatives.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = derivatives.getVCOUNTERPARTY_ABBR();
Vector VDISP_DEAL_ID = derivatives.getVDISP_DEAL_ID();
Vector VQTY_SELL = derivatives.getVQTY_SELL();
Vector VQTY = derivatives.getVQTY();
Vector VQTY_BBL = derivatives.getVQTY_BBL();
Vector VQTYSELL_BBL = derivatives.getVQTYSELL_BBL();
Vector VQTY_SETTLE = derivatives.getVQTY_SETTLE();
Vector VDEAL_RMK = derivatives.getVDEAL_RMK();
Vector VTRADE_DT = derivatives.getVTRADE_DT();
Vector VBUY_SELL = derivatives.getVBUY_SELL();
Vector VPROD_NM = derivatives.getVPROD_NM();
Vector VCURV_NM = derivatives.getVCURV_NM();
Vector VQTY_UNIT = derivatives.getVQTY_UNIT();
Vector VRATE = derivatives.getVRATE();
Vector VRATE_UNIT = derivatives.getVRATE_UNIT();
Vector VCONT_MONTH_YEAR = derivatives.getVCONT_MONTH_YEAR();
Vector VPRICE_START_DT = derivatives.getVPRICE_START_DT();
Vector VPRICE_END_DT = derivatives.getVPRICE_END_DT();
Vector VPROJ_METHOD = derivatives.getVPROJ_METHOD();
Vector VCONT_REF = derivatives.getVCONT_REF();
Vector VQTYCURV_NM = derivatives.getVQTYCURV_NM();

String month_name ="";
if(month.equals("01"))
{
	month_name = "JANUARY";
}
else if(month.equals("02"))
{
	month_name = "FEBRAUARY";
}
else if(month.equals("03"))
{
	month_name = "MARCH";
}
else if(month.equals("04"))
{
	month_name = "APRIL";
}
else if(month.equals("05"))
{
	month_name = "MAY";
}
else if(month.equals("06"))
{
	month_name = "JUNE";
}
else if(month.equals("07"))
{
	month_name = "JULY";
}
else if(month.equals("08"))
{
	month_name = "AUGEST";
}
else if(month.equals("09"))
{
	month_name = "SEPTEMBER";
}
else if(month.equals("10"))
{
	month_name = "OCTOBER";
}
else if(month.equals("11"))
{
	month_name = "NOVEMBER";
}else if(month.equals("12"))
{
	month_name = "DECEMBER";
}

if(avail_deal_years.equals(""))
{
	avail_deal_years=""+currentYear;
}
%>
<body>
<%@ include file="../home/header.jsp"%>
<form action="">
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
						    	Hedge Compliance Report : <%=comp_name %>
						    </div>
						    <%-- <a href="../contract_mgmt/xls_sectorwise_sales.jsp?fileName=SectorWise Sales Report.xls&company_cd=<%=comp_cd %>&year=<%=year %>&rd_val=<%=rd_val %>" download="SectorWise Sales Report.xls" >
						 		<span class="input-group-text"><i style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						 	</a> --%>
						</div>
					</div>
					<div class="card-body cdbody">
						<div class="row m-b-5">
							<div class="col-sm-4 col-xs-4 col-md-4"></div>
							<div class="col-sm-4 col-xs-4 col-md-4">
								<div class="form-group row">
									<div class="col-auto d-flex align-items-center" >
										<label class="form-label">Reporting for the Month of</label>
						  			</div>
						  			<div class="col">
									  	<select class="form-select form-select-sm" name="month" onchange="refresh();">
										    <option value="01" label="January">January</option>
										    <option value="02" label="February">February</option>
										    <option value="03" label="March">March</option>
										    <option value="04" label="April">April</option>
										    <option value="05" label="May">May</option>
										    <option value="06" label="June">June</option>
										    <option value="07" label="July">July</option>
										    <option value="08" label="August">August</option>
										    <option value="09" label="September">September</option>
										    <option value="10" label="October">October</option>
										    <option value="11" label="November">November</option>
										    <option value="12" label="December">December</option>
									  	</select>
									  	<script>document.forms[0].month.value = "<%=month%>"</script>
									</div>
									<div class="col">
						  				<select class="form-select form-select-sm" name="year" onchange="refresh();">
						  					<%for(int k=Integer.parseInt(avail_deal_years); k<=currentYear; k++) { %>
												<option value="<%=k %>"><%=k %></option>
											<%} %>
										</select>
										<script>document.forms[0].year.value="<%=year%>"</script>
									</div>
						  		</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">
								<div class="d-flex justify-content-end">
									<div class="email-icon-wrapper">
								        <span class="fa-stack fa-lg" style="position: relative;" title="Export To Excel" onclick="viewHedgeDerivatives('XLS')">
								            <i class="fa fa-file-excel-o fa-stack-2x" style="position: absolute; left: -1.5em; top: 0em; color:green;"></i>
								        </span>
								    </div>
								    <div class="email-icon-wrapper">
								        <span class="fa-stack fa-lg" style="position: relative;" title="Print PDF" onclick="viewHedgeDerivatives('PDF')">
								            <i class="fa fa-print fa-stack-2x" style="position: absolute; left: -0.7em; top: 0em; color:#800000;"></i>
								        </span>
								    </div>
								    
								</div>
							</div>
						</div>
					</div>
					<div class="card-body cdbody">
						<div class="accordion-body accor-body">
			        		<div class="row">
								<div class="table-responsive">
									<table class="table table-bordered table-hover">
										<thead>
											<tr style="background-color:#7B3F00 ;">
												<th colspan="17"><div align="center"><font  size="2" face="Verdana, Arial, Helvetica, sans-serif" color="white">MONTHLY STATEMENT ON HEDGING TRANSACTIONS-ROUTED THROUGH ICICI BANK</font><br></div></th>
											</tr>
											<tr>
												<th><div align="center"></div></th>
												<th><div  align="center"></div></th>
												<th colspan="8" style="background-color:#8cbed6;"><div  align="center"> Direct Exposure</div></th>
												<th colspan="7" style="background-color:#C5E8B7;"> <div align="center">Indirect Exposure</div></th>
											</tr>
											<tr>   
												<th style="background-color:white;"><div align="center">Sr.<br>no.</div></th>		
												<th><div align="center">Commodity</div></th>
												<th style="background-color:#8cbed6;"><div align="center">Exposure / Eligible Limit (as per the approval letter from ICICI Bank)<br><br>Direct Exposure</div></th>
												<th><div align="center">Outstanding Balance as on Previous Month End<br>[Long(+)/<br>Short(-)*]<br>[A]</div></th>
												<th><div align="center">Buy for the month<br>[B]</div></th>
												<th><div align="center">Sell for the month<br>[C]</div></th>
												<th><div align="center">Net Settlement Done this Month<br>[D]</div></th>
												<th><div align="center">Total Hedges Outstanding as on Month End<br>[Sum(A:D)]</div></th>
												<th><div align="center">Hedges Outstanding on Exchange as on Month End<br>[Long(+)/<br>Short(-)*]<br><br> [A]</div></th>
												<th><div align="center">Hedges Outstanding on OTC as on Month End<br>[Long(+)/<br>Short(-)*]<br><br>[B]</div></th>
												<th style="background-color:#C5E8B7;">Exposure / Eligible Limit (as per the approval letter  from ICICI Bank)<br><br><br>Indirect Exposure</div></th>
												<th><div align="center">Outstanding Balance as on Previous Month End<br><br><br>[Long(+)/<br>Short(-)*]<br><br> [A]</div></th>
												<th><div align="center">Buy for the month&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br><br>[B]</div></th>
												<th><div align="center">Sell for the &nbsp; month&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br><br>[C]</div></th>
												<th><div align="center">Total Hedges Outstanding as on Month End<br><br><br><br>[Sum(A:C)]</div></th>
												<th><div align="center">Hedges Outstanding on Exchange as on Month End<br><br><br>[Long(+)/<br>Short(-)*]</div></th>
												<th><div align="center">OTC as on Month End<br><br><br><br>[Long(+)/<br>Short(-)*]</div></th>	
											</tr>
										</thead>
										<tbody>
											<tr>
												<td align="right" ><font color="green">&nbsp;1&nbsp;</font></td>
												<td align="right" ><font color="green">&nbsp;Natural Gas:&nbsp;</font></td>
												<td align="right" style="background-color:#8cbed6;">&nbsp;<font color="green"><B><%=eligibleLimit %></B></font>&nbsp;</td>
												<td align="right" style="background-color:#8cbed6;"><font color="green">&nbsp;<%=totalPrevMonthBal %>&nbsp;</font></td>
												<td align="right" style="background-color:#8cbed6;"><font color="green">&nbsp;<%=totalQty %>&nbsp;</font></td>
												<td align="right" style="background-color:#8cbed6;"><font color="green">&nbsp;<%=totalQtySell %>&nbsp;</font></td>
												<td align="right" style="background-color:#8cbed6;"><font color="green">&nbsp;(<%=totalQtySettle %>)&nbsp;</font></td>
												<td align="right" style="background-color:#87CEEB;"><font color="green">&nbsp;<%=totalMonthOutstanding %>&nbsp;</font></td>
												<td align="right" style="background-color:#87CEEB;"><font color="green">-&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></td>
												<td align="right" style="background-color:#87CEEB;"><font color="green">&nbsp;<%=totalMonthOutstanding %>&nbsp;</font></td>
												
												<td align="right" style="background-color:#C5E8B7;">&nbsp;<%//=TotalQty %>&nbsp;</td>
												<td align="right" style="background-color:#C5E8B7;">&nbsp;&nbsp;</td>
												<td align="right" style="background-color:#C5E8B7;">&nbsp;&nbsp;</td>
												<td align="right" style="background-color:#C5E8B7;">&nbsp;&nbsp;</td>
												<td align="right" style="background-color:#008000;">&nbsp;&nbsp;</td>
												<td align="right" style="background-color:#008000;">&nbsp;&nbsp;</td>
												<td align="right" style="background-color:#008000;">&nbsp;&nbsp;</td>
											</tr>	
											<tr>
												<td align="right" ><font color="green">&nbsp;&nbsp;</font></td>
												<td align="right" ><font color="green">&nbsp;&nbsp;</font></td>
												<td align="right" style="background-color:#8cbed6;"><font color="green">&nbsp;&nbsp;</font></td>
												<td align="right" style="background-color:#8cbed6;"><font color="green">&nbsp;&nbsp;</font></td>
												<td align="right" style="background-color:#8cbed6;"><font color="green">&nbsp;&nbsp;</font></td>
												<td align="right" style="background-color:#8cbed6;"><font color="green">&nbsp;&nbsp;</font></td>
												<td align="right" style="background-color:#8cbed6;"><font color="green">&nbsp;&nbsp;</font></td>
												<td align="right" style="background-color:#87CEEB;"><font color="green">&nbsp;&nbsp;</font></td>
												<td align="right" style="background-color:#87CEEB;"><font color="green">&nbsp;&nbsp;</font></td>
												<td align="right" style="background-color:#87CEEB;"><font color="green">&nbsp;&nbsp;</font></td>
												
												<td align="right" style="background-color:#C5E8B7;">&nbsp;<%//=TotalQty %>&nbsp;</td>
												<td align="right" style="background-color:#C5E8B7;">&nbsp;&nbsp;</td>
												<td align="right" style="background-color:#C5E8B7;">&nbsp;&nbsp;</td>
												<td align="right" style="background-color:#C5E8B7;">&nbsp;&nbsp;</td>
												<td align="right" style="background-color:#008000;">&nbsp;&nbsp;</td>
												<td align="right" style="background-color:#008000;">&nbsp;&nbsp;</td>
												<td align="right" style="background-color:#008000;">&nbsp;&nbsp;</td>
											</tr>
											<tr>
												<td align="right" ><font color="green">&nbsp;&nbsp;</font></td>
												<td align="right" ><font color="green">&nbsp;Total&nbsp;</font></td>
												<td align="right" ><font color="green">&nbsp;&nbsp;</font></td>
												<td align="right" ><font color="green">&nbsp;&nbsp;</font></td>
												<td align="right" ><font color="green">&nbsp;&nbsp;</font></td>
												<td align="right" ><font color="green">&nbsp;&nbsp;</font></td>
												<td align="right" ><font color="green">&nbsp;<%//=TotalMthSettle %>&nbsp;</font></td>
												<td align="right" style="background-color:#87CEEB;"><font color="green">&nbsp;<%=totalMonthOutstanding %>&nbsp;</font></td>
												<td align="right" style="background-color:#87CEEB;"><font color="green">-&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></td>
												<td align="right" style="background-color:#87CEEB;"><font color="green">&nbsp;<%=totalMonthOutstanding %>&nbsp;</font></td>
												
												<td align="right" >&nbsp;<%//=TotalQty %>&nbsp;</td>
												<td align="right" >&nbsp;&nbsp;</td>
												<td align="right" >&nbsp;&nbsp;</td>
												<td align="right" >&nbsp;&nbsp;</td>
												<td align="right" style="background-color:#008000;">&nbsp;&nbsp;</td>
												<td align="right" style="background-color:#008000;">&nbsp;&nbsp;</td>
												<td align="right" style="background-color:#008000;">&nbsp;&nbsp;</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
					<div class="card-body cdbody">
						<div class="accordion-body accor-body">
			        		<div class="row">
								<div class="table-responsive">
									<table class="table table-bordered table-hover">
										<thead>
											<tr style="background-color:#c46210;">
												<th colspan="14"><div align="center"><font size="2" face="Verdana, Arial, Helvetica, sans-serif" color="white">TRANSACTION-WISE DETAILS OF CUSTOMER FOR THE MONTH OF  <%=month_name%>  <%=year %> ROUTED THROUGH ICICI BANK LTD<br></font></div></th>
											</tr>
										</thead>
									</table>
									<table>
										<thead>
											<tr>
								                <th style="background-color:#c46210;"><div align="center" ><font color="white" size="3">&nbsp;&nbsp;&nbsp;&nbsp;Commodity&nbsp;&nbsp;&nbsp;&nbsp;<br>&nbsp;&nbsp;&nbsp;&nbsp;Name:&nbsp;&nbsp;&nbsp;&nbsp;</font></div></th>                
												<th style="background-color:#FFC0CB;"><div align="center" ><font color="white" size="2">&nbsp;&nbsp;&nbsp;&nbsp;<%=VQTYCURV_NM %>&nbsp;&nbsp;&nbsp;&nbsp;<br>&nbsp;&nbsp;&nbsp;&nbsp;(Underlying Commodity is Natural Gas)&nbsp;&nbsp;&nbsp;&nbsp;</font></div></th> 
												<th colspan="16"></th>               
										 	</tr>
										 	<tr>
								                <th><div  align="center" style="background-color:white;"><font color="black" size="2"></font></div></th>
								                <th><div  align="center" style="background-color:#87CEEB;"><font color="black" size="2">Direct</font></div></th>
								                <th colspan="16"></th>
										  	</tr>
										</thead>
									</table>
									<table class="table table-bordered table-hover">
										<thead>
											<tr>
												<th><div align="center">Sr.<br>no.</div></th>		
												<th><div align="center">Contract Date</div></th>
												<th><div align="center">Contract ID/Trade<br> reference</div></th>
												<th><div align="center">Maturity Date/<br> Prompt Date</div></th>
												<th><div align="center">Trade Type<br> (Forward /Futures /Options/Swaps)</div></th>
												<th><div align="center">Buy for the month<br>(Barrels)</div></th>
												<th><div align="center">Buy for the month<br>(mmBtu)<br>equivalent</div></th>
												<th><div align="center">Sell for the month<br> (Barrels)</div></th>
												<th><div align="center">Sell for the month<br> (mmBtu)<br>equivalent</div></th>	
												<th><div align="center">Deals Settled <br>in the month <br>(MMBTU)</div></th>	
												<th><div align="center">Exchanges</div></th>
												<th><div align="center">OTC Counterparty Name</div></th>
												<th><div align="center">Broker/Counterparty Name </div></th>
												<th><div align="center">Remarks </div></th>
											</tr>
										</thead>
										<tbody>
											<%for(int i=0;i<VDISP_DEAL_ID.size();i++) 
									    	{%>
									    		<tr>
									    			<td><div align="center">&nbsp;<%=i+1 %>&nbsp;</div></td> 
												   	<td><div align="center"><b><%=VTRADE_DT.elementAt(i)%></b></div></td>
												  	<td><div align="center"><b><%=VCONT_REF.elementAt(i) %></b></div></td>
												  	<td><div align="center"><b><%=VPRICE_END_DT.elementAt(i)%></b></div></td>
													<td><div align="center" TITLE="<%=VCURV_NM.elementAt(i)%>:<%=VBUY_SELL.elementAt(i)%>">&nbsp;Swap&nbsp;</div></td>	
													<td><div align="right" TITLE="<%=VQTY_UNIT.elementAt(i)%>"><b><%=VQTY_BBL.elementAt(i)%></b></div></td>
													<td><div align="right"><b><%=VQTY.elementAt(i)%></b></div></td>
													<td><div align="right" TITLE="<%=VQTY_UNIT.elementAt(i)%>"><b><%=VQTYSELL_BBL.elementAt(i)%></b></div></td>
													<td><div align="right"><b><%=VQTY_SELL.elementAt(i)%></b></div></td>	
													<td><div align="right"><b><%=VQTY_SETTLE.elementAt(i)%></b></div></td>	  				  		  		
										 			<td><div align="center">&nbsp;<b><%//=VProdNm.elementAt(i)%></b>&nbsp;</div></td>
										 			<td><div align="right">&nbsp;<b><%=VCOUNTERPARTY_NM.elementAt(i)%></b>&nbsp;</div></td>
										 			<td><div align="center">&nbsp;<b><%=VCOUNTERPARTY_NM.elementAt(i)%></b>&nbsp;</div></td>
										 			<td><div align="center">&nbsp;<b><%=VDEAL_RMK.elementAt(i)%></b>&nbsp;</div></td>
											  	</tr>
											<%}%>
											<%if(VDISP_DEAL_ID.size()==0){%>
												<tr> <td colspan="14" align="center" class="content1"><b>List Not Available !!!</b></td> </tr>
											<%}%>
											
											<tr>
												<td align="right"></td>
												<td align="right"></td>
												<td align="right"></td>
												<td align="right"></td>
												<td align="right"><font color="green">&nbsp;Total:&nbsp;</font></td>
												<td align="right" style="background-color: #ffec8b"><font color="green">&nbsp;&nbsp;</font></td>
												<td align="right" style="background-color: #ffec8b"><font color="green">&nbsp;<%=totalQty %>&nbsp;</font></td>
												<td align="right" style="background-color: #ffec8b"><font color="green">&nbsp;&nbsp;</font></td>
												<td align="right" style="background-color: #ffec8b"><font color="green">&nbsp;<%=totalQtySell %>&nbsp;</font></td>
												<td align="right" style="background-color: #ffec8b"><font color="green">&nbsp;<%=totalQtySettle %>&nbsp;</font></td>
												<td align="right" style="background-color: #ffec8b"><font color="green">&nbsp;&nbsp;</font></td>
												<td align="right" style="background-color: #ffec8b"><font color="green">&nbsp;<%//=TotalQtyOTC %>&nbsp;</font></td>
												<td align="right" style="background-color: #ffec8b"><font color="green">&nbsp;&nbsp;</font></td>
												<td align="right" style="background-color: #ffec8b"><font color="green">&nbsp;&nbsp;</font></td>
											</tr>
										</tbody>
									</table>
									&nbsp;&nbsp;
									<table>
										<thead>
											<tr>
								                <th style="background-color:#c46210;"><div align="center" ><font color="white" size="3">&nbsp;&nbsp;&nbsp;&nbsp;Commodity&nbsp;&nbsp;&nbsp;&nbsp;<br>&nbsp;&nbsp;&nbsp;&nbsp;Name:&nbsp;&nbsp;&nbsp;&nbsp;</font></div></th>                
												<th style="background-color:#FFC0CB;"><div align="center" ><font color="white" size="2"><%//=VQtyCurveNm %>&nbsp;</font></div></th> 
												<th colspan="16"></th>               
										 	</tr>
										 	<tr>
								                <th><div  align="center" style="background-color:white;"><font color="black" size="2">&nbsp;&nbsp;&nbsp;&nbsp;</font></div></th>
								                <th><div  align="center" style="background-color:#008000;"><font color="black" size="2">&nbsp;&nbsp;&nbsp;&nbsp;In Direct&nbsp;&nbsp;&nbsp;&nbsp;</font></div></th>
								                <th colspan="16"></th>
										  	</tr>
										</thead>
									</table>
									<table class="table table-bordered table-hover">
										<thead>
											<tr>
												<th><div align="center">Sr.<br>no.</div></th>		
												<th><div align="center">Contract Date</div></th>
												<th><div align="center">Contract ID/Trade<br> reference</div></th>
												<th><div align="center">Maturity Date/<br> Prompt Date</div></th>
												<th><div align="center">Trade Type<br> (Forward / Futures /Options/Swaps)</div></th>
												<th><div align="center">Buy for the month<br>(MT) / Units</div></th>
												<th><div align="center">Sell for the month<br>(MT) / Units</div></th>		
												<th><div align="center">Exchanges</div></th>
												<th><div align="center">OTC</div></th>
												<th><div align="center">Broker name</div></th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td><div align="center">&nbsp;&nbsp;</div></td> 
											    <td><div align="center">&nbsp;&nbsp;</div></td>
											    <td><div align="center"></div></td>
											    <td><div align="center"></div></td>
												<td><div align="center"></div></td>
												<td><div align="right">&nbsp;</div></td>	  				  		  		
									 			<td><div align="center">&nbsp;&nbsp;</div></td>
									 			<td><div align="center">&nbsp;&nbsp;</div></td>
									 			<td><div align="center">&nbsp;&nbsp;</div></td>
									 			<td><div align="center">&nbsp;&nbsp;</div></td>
										 	</tr>
											<tr>
												<td align="right">&nbsp;&nbsp;</td>
												<td align="right">&nbsp;&nbsp;</td>
												<td align="right">&nbsp;&nbsp;</td>
												<td align="right">&nbsp;&nbsp;</td>
												<td align="right">&nbsp;&nbsp;</td>
												<td align="right"><font color="green">&nbsp;Total:&nbsp;</font></td>
												<td align="right" style="background-color: #ffec8b">&nbsp;&nbsp;</td>
												<td align="right" style="background-color: #ffec8b">&nbsp;&nbsp;</td>
												<td align="right" style="background-color: #ffec8b">&nbsp;&nbsp;</td>
												<td align="right" style="background-color: #ffec8b">&nbsp;&nbsp;</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
					<div class="card-body cdbody">
						<div class="row m-b-5">
							<div class="col-md-12 col-sm-12 col-xs-12">
								<div class="form-group row">
									<div class="col-md-12 col-sm-12 col-xs-12">
										<div>
											<font>
												we hereby confirm that all the above transaction has been carried out in pursuant to relevant RBI guidelines on Hedging of Commodity Price Risk & Freight Risk in Overseas Markets
												<br><br><br>
												*  All quantity proposed to be hedged and the tenure of the hedge are in line with the limit granted, at any given point of time <br>
												* Alternatively, hedges can be depicted in the form of import, export <br>
												* The margin remittance has been done in line with the exposure of the entity
												<br>
												<br>
												<br>
												Yours Faithfully,<br>
												<%=comp_name %>
												<br>
												<br>
												<br>
												<br>
												CIN: <%=comp_cin_no %> <br> 
												Registered Office: <%=comp_registered_addr %>
											</font>
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
</html>