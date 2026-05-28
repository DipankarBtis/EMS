<%@page import="java.util.Vector"%>
<%@page import="java.io.File"%>
<%@page import="java.io.FileWriter"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.BufferedWriter"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<jsp:useBean class="com.etrm.fms.market_risk.DB_MR_ExposureReport" id="market_risk" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String yesdate = utildate.getPreviousDate();
String report_dt=request.getParameter("report_dt")==null?yesdate:request.getParameter("report_dt");

market_risk.setCallFlag("EXPOSURE_EOD_CSV");
market_risk.setReport_dt(report_dt);
market_risk.init();

Vector VCOUNTERPARTY_NM = market_risk.getVCOUNTERPARTY_NM();
Vector VDISPLAY_DEAL_MAP = market_risk.getVDISPLAY_DEAL_MAP();
Vector VSIGNING_DT = market_risk.getVSIGNING_DT();
Vector VACCOUNT = market_risk.getVACCOUNT();
Vector VPRICE_TYPE = market_risk.getVPRICE_TYPE();
Vector VFIN_CURVE_NM = market_risk.getVFIN_CURVE_NM();
Vector VPHYS_CURVE_NM = market_risk.getVPHYS_CURVE_NM();

Vector VGAS_DT = market_risk.getVGAS_DT();
Vector VDCQ = market_risk.getVDCQ();
Vector VACTUAL_QTY = market_risk.getVACTUAL_QTY();
Vector VEFF_RATE = market_risk.getVEFF_RATE();
Vector VPHY_EXPO_ORIGINAL = market_risk.getVPHY_EXPO_ORIGINAL();
Vector VFIN_EXPO_ORIGINAL = market_risk.getVFIN_EXPO_ORIGINAL();
Vector VPHY_REALIZED_UNREALIZED = market_risk.getVPHY_REALIZED_UNREALIZED();
Vector VFIN_REALIZED_UNREALIZED = market_risk.getVFIN_REALIZED_UNREALIZED();
Vector VPHY_EXPO_REALIZED = market_risk.getVPHY_EXPO_REALIZED();
Vector VFIN_EXPO_REALIZED = market_risk.getVFIN_EXPO_REALIZED();
Vector VPHY_EXPO_UNREALIZED = market_risk.getVPHY_EXPO_UNREALIZED();
Vector VFIN_EXPO_UNREALIZED = market_risk.getVFIN_EXPO_UNREALIZED();
Vector VSETTLE_PRICE = market_risk.getVSETTLE_PRICE();
Vector VSLOPE = market_risk.getVSLOPE();
Vector VCONST = market_risk.getVCONST();
Vector VPHY_FORWARD_PRICE = market_risk.getVPHY_FORWARD_PRICE();
Vector VFIN_FORWARD_PRICE = market_risk.getVFIN_FORWARD_PRICE();
Vector VPHY_UNREALIZED_LEG = market_risk.getVPHY_UNREALIZED_LEG();
Vector VFIN_UNREALIZED_LEG = market_risk.getVFIN_UNREALIZED_LEG();
Vector VFIN_REALIZED_LEG = market_risk.getVFIN_REALIZED_LEG();
Vector VTOTAL = market_risk.getVTOTAL();
Vector VCONT_MMYYYY = market_risk.getVCONT_MMYYYY();
Vector VFIN_MMYYYY = market_risk.getVFIN_MMYYYY();
Vector VSETTLE_START_DT = market_risk.getVSETTLE_START_DT();
Vector VSETTLE_END_DT = market_risk.getVSETTLE_END_DT();
Vector VCONTRACT_PRICE = market_risk.getVCONTRACT_PRICE();
Vector VSR = market_risk.getVSR();
Vector VDIS_CONTRACT_TYPE = market_risk.getVDIS_CONTRACT_TYPE();
Vector VLEGAL_ENTITY = market_risk.getVLEGAL_ENTITY();
Vector VCONT_REF_NO = market_risk.getVCONT_REF_NO();
Vector VTOTAL_CHARGE = market_risk.getVTOTAL_CHARGE();
Vector VREMARKS = market_risk.getVREMARKS();
Vector VQTY_UNIT_NM = market_risk.getVQTY_UNIT_NM();

String split_date[] = report_dt.split("/");
String date = split_date[2]+split_date[1]+split_date[0];

response.setContentType("text/csv");
response.setCharacterEncoding("UTF-8");
response.setHeader("Content-Disposition","attachment; filename=Gross_Margin_" + date + ".csv");

PrintWriter pw = null;
pw=response.getWriter();
pw.println("Report Date,Legal Entity,Counterparty,BUYorSELL,Signing Date,Contract Type,Contract#,Contract Ref#,Fin.Curve,Phy.Curve,Sr.No,Gas Day,DCQ,Price Type,Allocated MMBTU,Unit,Contract Price($/MMBTU),Fin. Month,Spot Start Date,Spot End Date,Slope,Const,Total Charge($/MMBTU),Effective Contract Price,Delivery Month,Original Expo Phy.,Original Expo Fin., Realized/Unrealized Phy., Realized/Unrealized Fin,Unrealized Expo. Phy. dt. "+report_dt+",Unrealized Expo. Fin. dt. "+report_dt+",Realized Expo. Phy. dt. "+report_dt+",Realized Expo. Fin. dt. "+report_dt+",Forward Price Phy.,Forward Price Fin.,Settle Price dt. "+report_dt+",Unrealized Phy. Leg($),Unrealized Fin. Leg($),Realized Fin. Leg($),Total($),Remarks");
if(VCOUNTERPARTY_NM.size()>0)
{ 
	for(int i=0; i<VCOUNTERPARTY_NM.size(); i++)
	{
		String fin_month=(VFIN_MMYYYY.elementAt(i).toString().equals("")?"":" "+VFIN_MMYYYY.elementAt(i));
		String settle_st_dt = (VSETTLE_START_DT.elementAt(i).toString().equals("")?"":" "+VSETTLE_START_DT.elementAt(i));
		String settle_end_dt = (VSETTLE_END_DT.elementAt(i).toString().equals("")?"":" "+VSETTLE_END_DT.elementAt(i));
		
		pw.println(""+report_dt+","+VLEGAL_ENTITY.elementAt(i)+","+VCOUNTERPARTY_NM.elementAt(i)+","+VACCOUNT.elementAt(i)+", "+VSIGNING_DT.elementAt(i)+","+VDIS_CONTRACT_TYPE.elementAt(i)+","+VDISPLAY_DEAL_MAP.elementAt(i)+", "+VCONT_REF_NO.elementAt(i)+","
				+ ""+VFIN_CURVE_NM.elementAt(i)+","+VPHYS_CURVE_NM.elementAt(i)+","+VSR.elementAt(i)+", "+VGAS_DT.elementAt(i)+","+VDCQ.elementAt(i)+","
				+ ""+VPRICE_TYPE.elementAt(i)+","+VACTUAL_QTY.elementAt(i)+","+VQTY_UNIT_NM.elementAt(i)+","+VCONTRACT_PRICE.elementAt(i)+","+fin_month+","+settle_st_dt+","
				+ ""+settle_end_dt+","+VSLOPE.elementAt(i)+","+VCONST.elementAt(i)+","+VTOTAL_CHARGE.elementAt(i)+","+VEFF_RATE.elementAt(i)+", "+VCONT_MMYYYY.elementAt(i)+","
				+ ""+VPHY_EXPO_ORIGINAL.elementAt(i)+","+VFIN_EXPO_ORIGINAL.elementAt(i)+","+VPHY_REALIZED_UNREALIZED.elementAt(i)+","+VFIN_REALIZED_UNREALIZED.elementAt(i)+","
				+ ""+VPHY_EXPO_UNREALIZED.elementAt(i)+","+VFIN_EXPO_UNREALIZED.elementAt(i)+","+VPHY_EXPO_REALIZED.elementAt(i)+","+VFIN_EXPO_REALIZED.elementAt(i)+","
				+ ""+VPHY_FORWARD_PRICE.elementAt(i)+","+VFIN_FORWARD_PRICE.elementAt(i)+","+VSETTLE_PRICE.elementAt(i)+","
				+ ""+VPHY_UNREALIZED_LEG.elementAt(i)+","+VFIN_UNREALIZED_LEG.elementAt(i)+","+VFIN_REALIZED_LEG.elementAt(i)+","+VTOTAL.elementAt(i)+","+VREMARKS.elementAt(i));

	}
}

pw.flush();
pw.close();
%>