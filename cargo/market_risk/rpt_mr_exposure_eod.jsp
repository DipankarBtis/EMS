<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
</head>
<jsp:useBean class="com.etrm.fms.market_risk.DB_MR_ExposureReport" id="market_risk" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String yesdate = utildate.getPreviousDate();
String report_dt=request.getParameter("report_dt")==null?yesdate:request.getParameter("report_dt");
String legalEntity=request.getParameter("legalEntity")==null?"":request.getParameter("legalEntity");

String emp_nm="";
if(session.getAttribute("emp_nm")==null||session.getAttribute("emp_nm")==""||session.getAttribute("emp_nm").toString().equals("null"))
{
	emp_nm="";
}  
else
{
	emp_nm=""+session.getAttribute("emp_nm");
}

String ip="";
if(session.getAttribute("ip")==null||session.getAttribute("ip")==""||session.getAttribute("ip").toString().equals("null"))
{
	ip="";
}  
else
{
	ip=""+session.getAttribute("ip");
}



market_risk.setCallFlag("EXPOSURE_CONTRACT_LIST");
market_risk.setReport_dt(report_dt);
market_risk.init();

Vector VCOUNTERPARTY_CD = market_risk.getVCOUNTERPARTY_CD();
Vector VCONTRACT_TYPE = market_risk.getVCONTRACT_TYPE();
Vector VCONT_NO = market_risk.getVCONT_NO();
Vector VCONT_REV_NO = market_risk.getVCONT_REV_NO();
Vector VAGMT_NO = market_risk.getVAGMT_NO();
Vector VAGMT_REV_NO = market_risk.getVAGMT_REV_NO();
Vector VCARGO_NO = market_risk.getVCARGO_NO();
Vector VACCOUNT = market_risk.getVACCOUNT();
Vector VLEGAL_ENTITY_CD = market_risk.getVLEGAL_ENTITY_CD();
Vector VLEGAL_ENTITY = market_risk.getVLEGAL_ENTITY();

market_risk.setCallFlag("DELETE_EOD_DATA");
market_risk.setReport_dt(report_dt);
market_risk.setEmp_cd(emp_cd);
market_risk.init();

for(int i=0; i<VCOUNTERPARTY_CD.size();i++)
{
	market_risk.setCallFlag("AUTO_EXPOSURE_EOD_PROCESS");
	market_risk.setReport_dt(report_dt);
	market_risk.setComp_cd(""+VLEGAL_ENTITY_CD.elementAt(i));
	market_risk.setCounterparty_cd(""+VCOUNTERPARTY_CD.elementAt(i));
	market_risk.setAgmt_no(""+VAGMT_NO.elementAt(i));
	market_risk.setAgmt_rev_no(""+VAGMT_REV_NO.elementAt(i));
	market_risk.setCont_no(""+VCONT_NO.elementAt(i));
	market_risk.setCont_rev_no(""+VCONT_REV_NO.elementAt(i));
	market_risk.setContract_type(""+VCONTRACT_TYPE.elementAt(i));
	market_risk.setAccount(""+VACCOUNT.elementAt(i));
	market_risk.setCargo_no(""+VCARGO_NO.elementAt(i));
	market_risk.setIndex(i);
	market_risk.setEmp_cd(emp_cd);
	market_risk.init();
}

market_risk.setCallFlag("GENERATE_QP_EXPO_CSV");
market_risk.setReport_dt(report_dt);
market_risk.init();
try
{
	new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, owner_cd,emp_nm, ip, formCd, formNm,mod_cd,mod_nm, "", "", "Manual Exposure EOD Process dated "+report_dt+" Completed");  	
}
catch(Exception infoLogger)
{
	infoLogger.printStackTrace();
}
%>

<body>
<%@ include file="../home/header.jsp"%>
<form action="">

<input type="hidden" name="report_dt" value="<%=report_dt%>">

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
<script type="text/javascript">
alert("EOD Process has been Done for the report date : <%=report_dt%>");

var report_dt = document.forms[0].report_dt.value;

var u = document.forms[0].u.value;

var url = "rpt_mr_exposure.jsp?report_dt="+report_dt+"&u="+u;
document.getElementById("loading").style.visibility = "visible";
location.replace(url);	
</script>
</body>
</html>