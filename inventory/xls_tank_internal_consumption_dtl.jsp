<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import ="java.time.Month" %>
<%@page import ="java.text.DecimalFormat" %>
<%@page import ="java.text.NumberFormat" %>
<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--Harsh Maheta 20250303 : Form for Internal Consumption Details-->
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.inventory.DataBean_TankTerminal" id="dbterminal" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");
String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}
String sysDt=utildate.getSysdate();
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String year = request.getParameter("year")==null?""+currentYear:request.getParameter("year");

dbterminal.setCallFlag("INTERNAL_CONSUMPTION_DTL");
dbterminal.setComp_cd(owner_cd);
dbterminal.setYear(year);
dbterminal.init();

Vector VLNG_WRITE_OFF = dbterminal.getVLNG_WRITE_OFF();
Vector VFLARING = dbterminal.getVFLARING();
Vector VAUXILARY_CONSUMPTION = dbterminal.getVAUXILARY_CONSUMPTION();
Vector VSCV_FUEL_CONSUMPTION = dbterminal.getVSCV_FUEL_CONSUMPTION();
Vector VSUG = dbterminal.getVSUG();
Vector VOTHER_CONSUMPTION = dbterminal.getVOTHER_CONSUMPTION();
Vector VMASS_BALANCING = dbterminal.getVMASS_BALANCING();
Vector VTOTAL_CONSUMPTION = dbterminal.getVTOTAL_CONSUMPTION();

Vector VMONTH_LIST = new Vector();
Vector VMONTH_NO = new Vector();

int monthNo=0;
for (Month month : Month.values())
{
	++monthNo;
	VMONTH_NO.add(monthNo);
	VMONTH_LIST.add(month);
}

NumberFormat nf = new DecimalFormat("###########0.00");

double sum_lng_write_off=0.00;
double sum_flaring=0.00;
double sum_auxilary_consumption=0.00;
double sum_scv_fuel_consumption=0.00;
double sum_sug=0.00;
double sum_other_consumption=0.00;
double sum_mass_balancing=0.00;
double sum_total_consumption=0.00;

for (int i = 0; i < VLNG_WRITE_OFF.size(); i++)
{
	String numStr = ""+VLNG_WRITE_OFF.elementAt(i);
	if(numStr.isEmpty() || numStr.equals(""))
	{
		numStr="0.00";
	}
	double num = Double.parseDouble(numStr);
	sum_lng_write_off += num;
}
for (int i = 0; i < VFLARING.size(); i++)
{
	String numStr = ""+VFLARING.elementAt(i);
	if(numStr.isEmpty() || numStr.equals(""))
	{
		numStr="0.00";
	}
	double num = Double.parseDouble(numStr);
	sum_flaring += num;
}
for (int i = 0; i < VAUXILARY_CONSUMPTION.size(); i++)
{
	String numStr = ""+VAUXILARY_CONSUMPTION.elementAt(i);
	if(numStr.isEmpty() || numStr.equals(""))
	{
		numStr="0.00";
	}
	double num = Double.parseDouble(numStr);
	sum_auxilary_consumption += num;
}
for (int i = 0; i < VSCV_FUEL_CONSUMPTION.size(); i++)
{
	String numStr = ""+VSCV_FUEL_CONSUMPTION.elementAt(i);
	if(numStr.isEmpty() || numStr.equals(""))
	{
		numStr="0.00";
	}
	double num = Double.parseDouble(numStr);
	sum_scv_fuel_consumption += num;
}
for (int i = 0; i < VSUG.size(); i++)
{
	String numStr = ""+VSUG.elementAt(i);
	if(numStr.isEmpty() || numStr.equals(""))
	{
		numStr="0.00";
	}
	double num = Double.parseDouble(numStr);
	sum_sug += num;
}
for (int i = 0; i < VOTHER_CONSUMPTION.size(); i++)
{
	String numStr = ""+VOTHER_CONSUMPTION.elementAt(i);
	if(numStr.isEmpty() || numStr.equals(""))
	{
		numStr="0.00";
	}
	double num = Double.parseDouble(numStr);
	sum_other_consumption += num;
}
for (int i = 0; i < VMASS_BALANCING.size(); i++)
{
	String numStr = ""+VMASS_BALANCING.elementAt(i);
	if(numStr.isEmpty() || numStr.equals(""))
	{
		numStr="0.00";
	}
	double num = Double.parseDouble(numStr);
	sum_mass_balancing += num;
}
for (int i = 0; i < VTOTAL_CONSUMPTION.size(); i++)
{
	String numStr = ""+VTOTAL_CONSUMPTION.elementAt(i);
	if(numStr.isEmpty() || numStr.equals(""))
	{
		numStr="0.00";
	}
	double num = Double.parseDouble(numStr);
	sum_total_consumption += num;
}
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table  width="100%" border="1">
		<tr>
			<th colspan="9" rowspan="2" align="left">Internal Consumption Details (<%=year%>)</th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th>Month</th>
				<th>LNG Write Off<br>(MMBTU)</th>
				<th>Flaring</th>
				<th>Power Fuel Consumption<br>(MMBTU)</th>
				<th>SCV Fuel Consumption<br>(MMBTU)</th>
				<th>SUG<br>(MMBTU)</th>
				<th>Other Consumption<br>(MMBTU)</th>
				<th>Unaccounted Losses<br>(MMBTU)</th>
				<th>Total Consumption<br>(MMBTU)</th>
			</tr>
		</thead>
		<tbody >
			<%for(int i=0; i<VMONTH_LIST.size();i++){ %>
			<tr>
				<td align="center">
					<b><%=VMONTH_LIST.elementAt(i) %> </b>
				</td>
				<td align="center">
					<%=VLNG_WRITE_OFF.elementAt(i) %>
				</td>
				<td align="center">
					<%=VFLARING.elementAt(i) %>
				</td>
				<td align="center">
					<%=VAUXILARY_CONSUMPTION.elementAt(i) %>
				</td>
				<td align="center">
					<%=VSCV_FUEL_CONSUMPTION.elementAt(i) %>
				</td>
				<td align="center">
					<%=VSUG.elementAt(i) %>
				</td>
				<td align="center">
					<%=VOTHER_CONSUMPTION.elementAt(i) %>
				</td>
				<td align="center">
					<%=VMASS_BALANCING.elementAt(i) %>
				</td>
				<td align="center">
					<%=VTOTAL_CONSUMPTION.elementAt(i) %>
				</td>
			</tr>
			<%} %>
			<tr>
				<td>
					<b>Sum of Total :</b>
				</td>
				<td align="center">
					<%=nf.format(sum_lng_write_off) %>
				</td>
				<td align="center">
					<%=nf.format(sum_flaring) %>
				</td>
				<td align="center">
					<%=nf.format(sum_auxilary_consumption) %>
				</td>
				<td align="center">
					<%=nf.format(sum_scv_fuel_consumption) %>
				</td>
				<td align="center">
					<%=nf.format(sum_sug) %>
				</td>
				<td align="center">
					<%=nf.format(sum_other_consumption) %>
				</td>
				<td align="center">
					<%=nf.format(sum_mass_balancing) %>
				</td>
				<td align="center">
					<%=nf.format(sum_total_consumption) %>
				</td>
			</tr>
		</tbody>
	</table>
</body>
</html>