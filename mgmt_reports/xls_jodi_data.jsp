<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>

<jsp:useBean class="com.etrm.fms.mgmt_reports.DataBean_Govt_Reports" id="govt_rpt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");

if(month.length() == 1)
{
    month="0"+month; 
}

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

String month_nm=utildate.getMonthNameMON("01/"+month+"/"+year);
month_nm= (""+month_nm.charAt(0))+month_nm.substring(1).toLowerCase();


govt_rpt.setCallFlag("JODI_DATA_RPT");
govt_rpt.setComp_cd(owner_cd);
govt_rpt.setMonth(month);
govt_rpt.setYear(year);
govt_rpt.init();

Vector VNATURAL_GAS_DESC = govt_rpt.getVNATURAL_GAS_DESC();
Vector VLNG_DESC = govt_rpt.getVLNG_DESC();
Vector VUNLOADED_DATA = govt_rpt.getVUNLOADED_DATA();

String sales_mmbtu = govt_rpt.getSales_mmbtu();
String sales_mmscm = govt_rpt.getSales_mmscm();
String sales_tera_joules = govt_rpt.getSales_tera_joules();
String unloaded_mmbtu = govt_rpt.getUnloaded_mmbtu();

String comp_nm=govt_rpt.getComp_nm();
%>
<body>
<%
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("content-Disposition","inline; filename="+fileName);
%>
    <table width="100%" >
        <tr>
            <td colspan="5" rowspan="" align="center">
                <span style="font-size:16pt; font-weight:bold; text-decoration:underline;">MONTHLY JODI GAS DATA</span>
            </td>
        </tr>
    </table>
    <table width="100%">
        <tr>
            <th align="right" colspan="4">
                <span style="font-size:14pt; font-weight:bold;">Month: </span>
            </th>
            <th align="left">
                <span style="font-size:14pt;"><%=month_nm%>, <%=year %></span>
            </th>
        </tr>
        <tr>
            <th align="right" colspan="4">
                <span style="font-size:14pt; font-weight:bold;">Company: </span>
            </th>
            <th align="left"  style="white-space: nowrap; width: auto;">
                <span style="font-size:14pt; white-space:nowrap;"><%=comp_nm%></span>
            </th>
        </tr>
    </table>
    <table width="100%" border="1">
        <thead>
            <tr>
                <th colspan="4" align="left">
                    <span style="font-size:14pt; font-weight:bold;">1. Natural Gas Details:</span>
                </th>
            </tr>
            <tr>
                <th align="center" colspan="2" rowspan="4"><b>DESCRIPTION</b></th>
                <th align="center" colspan="2"></th>
            </tr>
            <tr>
                <th align="center" colspan="2"><b>TOTAL</b></th>
            </tr>
            <tr>
                <th align="center">Million M<sup>3</sup> (at 15&deg; C, 760 mm hg)</th>
                <th align="center">Terajoules</th>
            </tr>
            <tr>
                <th align="center">A</th>
                <th align="center">B</th>
            </tr>
        </thead>
        <tbody>
            <%//if(!sales_mmbtu.equals("")){
                int ctn1=0;
            %>
                <%for(int i=0;i<VNATURAL_GAS_DESC.size();i++){%>
                    <tr>
                        <%if(VNATURAL_GAS_DESC.elementAt(i).equals("Pipeline")||VNATURAL_GAS_DESC.elementAt(i).equals("of which: Power Generation")){%>
                            <td align="center"></td>
                        <%}else{%>
                            <td align="center"><%=++ctn1%></td>
                        <%}%>
                        <td><%if(VNATURAL_GAS_DESC.elementAt(i).equals("Pipeline")||VNATURAL_GAS_DESC.elementAt(i).equals("of which: Power Generation")){%>&nbsp;&nbsp;<%}%><%=VNATURAL_GAS_DESC.elementAt(i) %></td>                              
                        <%if(VNATURAL_GAS_DESC.elementAt(i).equals("Gross Inland Deliveries")){%>
                            <td align="right"><b><%=sales_mmscm %></b></td>
                            <td align="right"><b><%=sales_tera_joules %></b></td>
                        <%}else{%>
                            <td align="right"></td>
                            <td align="right"></td>
                        <%}%>
                    </tr>
                <%} %>
            <%-- <%} else{%>
                <tr>
                    <td colspan="4" align="center"><%=utilmsg.infoMessage("<b>No Natural Gas Details Found!</b>") %></td>
                </tr>
            <%} %> --%>
        </tbody>
    </table>
    <table width="100%" border="1">
        <thead>
            <tr>
                <th colspan="4" align="left">
                    <span style="font-size:14pt; font-weight:bold;">2. LNG Details:</span>
                </th>
            </tr>
            <tr>
                <th align="center" rowspan="2" colspan="2"><b>DESCRIPTION</b></th>
                <th align="center" colspan="2"></th>
            </tr>
            <tr>
                <th align="center" colspan="2"><b>TOTAL</b></th>
            </tr>
        </thead>
        <tbody>
            <%//if(!unloaded_mmbtu.equals("")){
                int ctn=0;
            %>
                <%for(int i=0;i<VLNG_DESC.size();i++){%>
                    <%if(!VLNG_DESC.elementAt(i).equals("Gross Inland Deliveries <b>(in MMBTU)</b>")){%>
                    	<tr>
                            <%if(VLNG_DESC.elementAt(i).equals("&nbsp;&nbsp;of which: Power Generation <b>(in TeraJoules)</b>")){%>
                                <td align="center"></td>
                            <%}else{%>
                                <td align="center"><%=++ctn%></td>
                            <%} %>
                            <td><%=VLNG_DESC.elementAt(i) %></td>
                            <td align="right" colspan="2"><b><%=VUNLOADED_DATA.elementAt(i) %></b></td>
                    	</tr>
                 	<%}%>
                <%}%>
            <%-- <%}else{%>
                <tr>
                    <td colspan="4" align="center"><%=utilmsg.infoMessage("<b>No LNG Details Found!</b>") %></td>
                </tr>
            <%}%> --%>
        </tbody>
    </table>
</body>
</html>
