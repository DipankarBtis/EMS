
<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<script>

function openPdf(file_nm,url)
{
	var msg="";
	var msg_type="";
	if(file_nm!="")
	{
		msg="PDF has been Generated!";
		msg_type="S";
	}
	else
	{
		msg="PDF Generation Failed!";
		msg_type="E";
	}
	//alert(msg)
	//window.opener.refresh_rpt(msg,msg_type);
	var window1="";
	if(!window1 || window1.closed)
	{
		window1= window.open(url,"Hedge Compliance Report","top=10,left=10,width=1000,height=600,scrollbars=1");
	}
	else
	{
		window1.close();
		window1= window.open(url,"Hedge Compliance Report","top=10,left=10,width=1000,height=600,scrollbars=1");
	}
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.derivatives.DB_Derivatives_Report" id="derivatives" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.UtilBean" id="utilbean" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DB_Pdf_Generation1" id="pdfFile" scope="request"/>
<%
String sysdate=utildate.getSysdate();

String owner_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");
String owner_abbr=""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
String owner_nm=""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");
String owner_logo=""+session.getAttribute("comp_logo")==null?"":""+session.getAttribute("comp_logo");
String emp_cd=""+session.getAttribute("emp_cd")==null?"":""+session.getAttribute("emp_cd");

String file=request.getParameter("file")==null?"":request.getParameter("file");

String year=request.getParameter("year")==null?"":request.getParameter("year");
String month=request.getParameter("month")==null?"":request.getParameter("month");
if(month.length() == 1)
{
	month="0"+month; 
}

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
HashMap view_info = derivatives.getView_info();
%>
<%if(file.equals("XLS")){ %>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename=Monthly_hedge_report_for_"+month+"/"+year+".xls");
%>

<table width="100%" border="1">
    <tr>
        <td class="title1" style="background-color:#D3D3D3; text-align: center;" colspan="17">
            <font size="4" color="black"><b><%=comp_name %></b></font>
        </td>
    </tr>
</table>
<div id="col-new">
 	<table width="100%">
	   	<tr>
	   		<td class="title2" align="right" width="2%" style="background-color: white;">&nbsp;
	   	    <td class="title2" align="right" width="5%" style="background-color: yellow;">&nbsp;
	   			<div align="center"><font face="Verdana, Arial, Helvetica, sans-serif">Reporting for the Month of </font></div>
	   		</td>
	   		<td class="title2" align="right" width="2%" style="background-color:#FFA500;">	   				
				<%=month_name %>-<%=year %>
				<font size="3"><b>&nbsp;&nbsp;</b></font>	   	           				         
              <td class="title2" align="left" width="10%" style="background-color:white;">&nbsp;</td>	   		    
	   		 </tr>
	   </table>
  </div>
<table width="100%" border="1">
    <tr>
        <td class="title1" style="background-color:#7B3F00; text-align: center;" colspan="17">
            <font size="4" color="black"><b>MONTHLY STATEMENT ON HEDGING TRANSACTIONS-ROUTED THROUGH ICICI BANK</b></font>
        </td>
    </tr>
</table>		
<table width="100%" border="1">
	<thead>
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
</table><br><br><br>
<table width="100%" align="center" border="1">
	<tr>
		<td class="title2" style="background-color:#7B3F00; text-align: center;" colspan="14">
   			<font size="4"  color="white"><b>Transaction-wise details of customer for the month of  <%=month_name%>  <%=year %> routed through ICICI Bank Ltd</b></font>
   		</td>
   	</tr>	  
</table>	   
<table width="10%"  border="1">
	<tr>
		<td style="background-color:#c46210;"><div  align="center" ><font color="white" size="3">Commodity Name:</font></div></td>                
		<td style="background-color:#FFC0CB;"><div  align="center" ><font color="white" size="2"><%=VQTYCURV_NM %>&nbsp;(Underlying Commodity is Natural Gas)</div></td>                					
	</tr> 
</table>
<table width="10%" border="1">
	<tr>
		<td width="20%"><div  align="center" style="background-color:white;"><font color="black" size="2"></font></div></td>
		<td width="20%" style="background-color: #87CEEB;"><div align="center"><font color="black" size="2">Direct</font></div></td>
	</tr>
</table>
<table width="100%" border="1">
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
			  	<td><div align="center"><b><%//=VDealNo.elementAt(i)%><%=VCONT_REF.elementAt(i) %></b></div></td>
			  	<td><div align="center"><b><%=VPRICE_END_DT.elementAt(i)%></b></div></td>
				<td><div align="center" TITLE="<%=VCURV_NM.elementAt(i)%>:<%=VBUY_SELL.elementAt(i)%>">&nbsp;Swap&nbsp;<%//=VCurveNm.elementAt(i)%></div></td>	
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
<table width="10%"  border="1">
	<tr>
		<td style="background-color:#c46210;"><div align="center" ><font color="white" size="3">Commodity Name:</font></div></td>                
		<td style="background-color:#FFC0CB;"><div align="center" ><font color="white" size="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></div></td>                
	</tr> 
</table>
<table width="10%">
	<tr>
		<td width="20%"><div  align="center" style="background-color:white;"><font color="black" size="2"></font></div></td>
        <td width="20%" style="background-color:#008000;"><div  align="center"><font color="black" size="2">IN Direct</font></div></td>
	</tr> 
</table> 
	<table width="100%" border="1">
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
&nbsp;&nbsp;
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

<br><br>

<%}else if(file.equals("PDF")){ %>
<% 
HttpServletRequest req = request;

pdfFile.setCallFlag("PDF_HEDGE_COMPLIANCE");
pdfFile.setRequest(req);
pdfFile.setComp_logo(owner_logo);
pdfFile.setComp_cd(owner_cd);
pdfFile.setComp_nm(owner_nm);
pdfFile.setComp_cin_no(comp_cin_no);
pdfFile.setComp_registered_addr(comp_registered_addr);
pdfFile.setEmp_cd(emp_cd);
pdfFile.setYear(year);
pdfFile.setMonth(month);
pdfFile.setMonth_name(month_name);

pdfFile.setEligibleLimit(eligibleLimit);
pdfFile.setTotalPrevMonthBal(totalPrevMonthBal);
pdfFile.setTotalQty(totalQty);
pdfFile.setTotalQtySell(totalQtySell);
pdfFile.setTotalQtySettle(totalQtySettle);
pdfFile.setTotalMonthOutstanding(totalMonthOutstanding);
pdfFile.setView_hedge_info(view_info);

pdfFile.setVCOUNTERPARTY_CD(VCOUNTERPARTY_CD);
pdfFile.setVCOUNTERPARTY_NM(VCOUNTERPARTY_NM);
pdfFile.setVCOUNTERPARTY_ABBR(VCOUNTERPARTY_ABBR);
pdfFile.setVDISP_DEAL_ID(VDISP_DEAL_ID);
pdfFile.setVQTY_SELL(VQTY_SELL);
pdfFile.setVQTY(VQTY);
pdfFile.setVQTY_BBL(VQTY_BBL);
pdfFile.setVQTYSELL_BBL(VQTYSELL_BBL);
pdfFile.setVQTY_SETTLE(VQTY_SETTLE);
pdfFile.setVDEAL_RMK(VDEAL_RMK);
pdfFile.setVTRADE_DT(VTRADE_DT);
pdfFile.setVBUY_SELL(VBUY_SELL);
pdfFile.setVPROD_NM(VPROD_NM);
pdfFile.setVCURV_NM(VCURV_NM);
pdfFile.setVQTY_UNIT(VQTY_UNIT);
pdfFile.setVRATE(VRATE);
pdfFile.setVRATE_UNIT(VRATE_UNIT);
pdfFile.setVCONT_MONTH_YEAR(VCONT_MONTH_YEAR);
pdfFile.setVPRICE_START_DT(VPRICE_START_DT);
pdfFile.setVPRICE_END_DT(VPRICE_END_DT);
pdfFile.setVPROJ_METHOD(VPROJ_METHOD);
pdfFile.setVCONT_REF(VCONT_REF);
pdfFile.setVQTYCURV_NM(VQTYCURV_NM);

pdfFile.init();

String file_url = pdfFile.getFile_url();
String file_nm = pdfFile.getFile_nm();
String pdfpath = file_url+"/"+CommonVariable.work_dir+owner_cd+""+CommonVariable.hedge_compliance_pdf_path+""+file_nm;
%>
<script>
openPdf('<%=file_nm%>','<%=pdfpath%>');
</script>
<%}%>


</html>