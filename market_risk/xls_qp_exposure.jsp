<%@page import="java.io.PrintWriter"%>

<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>

<jsp:useBean class="com.etrm.fms.market_risk.DB_MR_ExposureReport" id="db_MarketRisk" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String previousDate=utildate.getPreviousDate();
String rpt_dt = request.getParameter("rpt_dt")==null?previousDate:request.getParameter("rpt_dt");
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
db_MarketRisk.setCallFlag("EXPOSURE_QP_RPT");
db_MarketRisk.setReport_dt(rpt_dt);
db_MarketRisk.setComp_cd(owner_cd);
db_MarketRisk.init();

Vector VCOB_DT = db_MarketRisk.getVCOB_DT();
Vector VLEGAL_ENTITY = db_MarketRisk.getVLEGAL_ENTITY();
Vector VDEAL_NUM = db_MarketRisk.getVDEAL_NUM();
Vector VCOUNTERPARTY = db_MarketRisk.getVCOUNTERPARTY();
Vector VBUY_SELL = db_MarketRisk.getVBUY_SELL();
Vector VUNIT = db_MarketRisk.getVUNIT();
Vector VCONTRACT_MONTH = db_MarketRisk.getVCONTRACT_MONTH();
Vector VCURVE_NAME = db_MarketRisk.getVCURVE_NAME();
Vector VEXPOSURE = db_MarketRisk.getVEXPOSURE();
Vector VFINANCIAL_PHYSICAL = db_MarketRisk.getVFINANCIAL_PHYSICAL();
Vector VREALISED_UNREALISED = db_MarketRisk.getVREALISED_UNREALISED();
Vector VFORWARD_PRICE = db_MarketRisk.getVFORWARD_PRICE();
Vector VPRICE_TYPE = db_MarketRisk.getVPRICE_TYPE();
Vector VCONT_TYPE_NM = db_MarketRisk.getVCONT_TYPE_NM();
Vector VREMARKS = db_MarketRisk.getVREMARKS();
%>
<body>
<%
	response.setContentType("text/csv");
	response.setCharacterEncoding("UTF-8");
	response.setHeader("content-Disposition","attachment; filename="+fileName);
	
	PrintWriter writer = response.getWriter();
	
	writer.println("COB DT,LEGAL ENTITY,CONTRACT TYPE,DEAL NUM,COUNTERPARTY,BUY/SELL,PRICE TYPE,UNIT,CONTRACT MONTH,CURVE NAME,EXPOSURE,FINANCIAL/PHYSICAL,REALISED/UNREALISED,FORWARD PRICE,REMARKS");
	
	if(VCOB_DT.size()>0)
	{
		for(int i=0;i<VCOB_DT.size();i++)
		{
			writer.println(""+VCOB_DT.elementAt(i)+","+VLEGAL_ENTITY.elementAt(i)+","+VCONT_TYPE_NM.elementAt(i)+","+VDEAL_NUM.elementAt(i)+","+
				VCOUNTERPARTY.elementAt(i)+","+VBUY_SELL.elementAt(i)+","+VPRICE_TYPE.elementAt(i)+","+VUNIT.elementAt(i)+", "+
				VCONTRACT_MONTH.elementAt(i)+","+VCURVE_NAME.elementAt(i)+","+VEXPOSURE.elementAt(i)+","+VFINANCIAL_PHYSICAL.elementAt(i)+","+
				VREALISED_UNREALISED.elementAt(i)+","+VFORWARD_PRICE.elementAt(i)+","+VREMARKS.elementAt(i));
		}
	}
	else
	{
		writer.println("No QP Exposure Data is Available!");
	}
	writer.flush();
	writer.close();
%>
</body>
</html>