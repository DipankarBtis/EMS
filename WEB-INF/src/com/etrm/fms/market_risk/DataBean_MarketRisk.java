package com.etrm.fms.market_risk;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

//Coded By          : Harsh Maheta
//Code Reviewed by	:  
//CR Date			: 13/06/2023 
//Status	  		: Developing

public class DataBean_MarketRisk
{

	String db_src_file_name="DataBean_MarketRisk.java";
	
	Connection conn;
	PreparedStatement stmt;
	PreparedStatement stmt0;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt5;
	PreparedStatement stmt6;
	ResultSet rset;
	ResultSet rset0;
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
	ResultSet rset6;
	String queryString="";
	String queryString0="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	String queryString6="";

	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	
	public HttpServletRequest request = null;
	public void setRequest(HttpServletRequest request) {this.request = request;}
	
	private static final String SAVE_DIR = CommonVariable.market_risk_dir;

	public void init()
	{
		String function_nm="init()";
		try
		{
			Context initContext = new InitialContext();
	    	if(initContext == null)
	    	{
	    		throw new Exception("Boom - No Context");
	    	}

	    	Context envContext  = (Context)initContext.lookup("java:/comp/env");
	    	DataSource ds = (DataSource)envContext.lookup(RuntimeConf.security_database);
	    	if(ds != null)
	    	{
	    		conn = ds.getConnection();
	    		if(conn != null)
	    		{
	    			if(callFlag.equalsIgnoreCase("SETTLEMENT_MST"))
	    			{
	    				getSettlementCalendarList();
	    				getSettleCurve();
	    				getYearList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("HOLIDAY_CLAND_MST"))
	    			{
	    				getHolidayCalendarList();
	    				getHolidayCurve();
	    				getYearListForHolidayClendar();
	    			}
	    			else if(callFlag.equalsIgnoreCase("SETTLEMENT_PRICING_MST")) 
	    			{
	    				getSettlementPricing();
	    				getSettlementPricingCurveName();
	    				
	    				getZEMAXMLFileDetails();
	    				getReportDtforPricing();
	    			}
	    			else if(callFlag.equalsIgnoreCase("FORWARD_PRICING_MST")) 
	    			{
	    				getReportDtforPricing();
	    				getForwardPricing();
	    				getForwardPricingCurveName();
	    			}
	    			else if(callFlag.equalsIgnoreCase("LATE_DEAL_RPT")) 
	    			{
	    				getTraderCustomerList();
	    				getSellLateDealsDtl();
	    				getPurchaseLateDealsDtl();
	    			}
	    			else if(callFlag.equalsIgnoreCase("DEAL_AUDIT_HISTORY")) 
	    			{
	    				getDealAuditReport();
	    			}
	    		}
	    		conn.close();
    			conn = null;
	    	}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally
	    {
	    	if(rset != null){try{rset.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset0 != null){try{rset0.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset1 != null){try{rset1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset2 != null){try{rset2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset3 != null){try{rset3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset4 != null){try{rset4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset5 != null){try{rset5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset6 != null){try{rset6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt0 != null){try{stmt0.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}	
	    	if(stmt6 != null){try{stmt6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}	
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
	public void getDealAuditReport()
	{
		String function_nm="getDealAuditReport()";
		try
		{
			queryString="SELECT NEW_VALUE,OLD_VALUE,LOG_TIME,TO_CHAR(LOG_DT,'DD/MM/YYYY'),LOG_UID,FORM_NAME "
					+ "FROM FMS_ALL_LOG "
					+ "WHERE COMPANY_CD=? AND FORM_CD IN (?,?,?,?,?,?,?) "
					+ "AND NEW_VALUE IS NOT NULL ";
			if(!from_dt.equals("") && !to_dt.equals("")) 
			{
				queryString+="AND LOG_DT >= TO_DATE(?,'DD/MM/YYYY') AND LOG_DT <= TO_DATE(?,'DD/MM/YYYY') ";
			}
			queryString+= " ORDER BY LOG_DT DESC, LOG_TIME DESC";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "16"); //Trader Contract Master
			stmt.setString(3, "26"); // Gas Supply Contract(SN/LOA/IGX)
			stmt.setString(4, "141"); // CN Cargo
			stmt.setString(5, "177"); // DLNG Supply Contract(SN/LOA/IGX)			
			stmt.setString(6, "190"); // Derivatives		190	- Live form Cd
			stmt.setString(7, "148"); // LTCORA Buy			148 - Live form Cd
			stmt.setString(8, "160"); // LTCORA Sell		160 - Live form Cd
			stmt.setString(9, from_dt);
			stmt.setString(10, to_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String new_values = rset.getString(1)==null?"":rset.getString(1);
				String old_values = rset.getString(2)==null?"":rset.getString(2);
				String time = rset.getString(3)==null?"":rset.getString(3);
				String update_dt=rset.getString(4)==null?"":rset.getString(4);
				String update_by=rset.getString(5)==null?"":rset.getString(5);
				String form_nm=rset.getString(6)==null?"":rset.getString(6);
				
				if(!new_values.equals(""))
				{
					String cp="",old_cp="";
					String name="",old_name="";
					String abbr="",old_abbr="";
					String cont_name = "",old_cont_name = "";
					String cont_no = "",old_cont_no = "";
					String cont_ref_no = "",old_cont_ref_no = "";
					String contract_type="",old_contract_type="";
					String trade_ref_no="",old_trade_ref_no="";
					String dda_dt="",old_dda_dt="";
					String dda_time="",old_dda_time="";
					String signing_dt="",old_signing_dt="";
					String signing_time="",old_signing_time="";
					String txn_charges="",old_txn_charges="";
					String txn_unit="",old_txn_unit="";
					String post_margin="",old_post_margin="";
					
					String ent_dt="",old_ent_dt="";
					String ent_time="",old_ent_time="";
					String agmt_type="",old_agmt_type="";
					String agmt_base="",old_agmt_base="";
					String start_dt="",old_start_dt="";
					String end_dt="",old_end_dt="";
					String rate="",old_rate="";
					String rate_unit="",old_rate_unit="";
					String tcq="",old_tcq="";
					String dcq="",old_dcq="";
					String var_qty="",old_var_qty="";
					String quantity_unit="",old_quantity_unit="";
					String mdcq_percentage="",old_mdcq_percentage="";
					String cont_status="";String old_cont_status="";
					String cont_type_nm = "";
					
					String slope ="", old_slope="";
					String constant ="", old_constant="";
					String curve_nm ="", old_curve_nm="";
					String vp_flag ="", old_vp_flag="";
					
					String split_New_Value[] = new_values.split("#");
					for(int i=0; i<split_New_Value.length; i++)
					{
						if(split_New_Value[i].startsWith("CP=")){
							String temp[] = split_New_Value[i].split("CP=");
							if(temp.length>0){
								cp=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("NAME=")){
							String temp[] = split_New_Value[i].split("NAME=");
							if(temp.length>0){
								name=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("ABBR=")){
							String temp[] = split_New_Value[i].split("ABBR=");
							if(temp.length>0){
								abbr=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("CONTNAME=")){
							String temp[] = split_New_Value[i].split("CONTNAME=");
							if(temp.length>0){
								cont_name=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("CONTNO=")){
							String temp[] = split_New_Value[i].split("CONTNO=");
							if(temp.length>0){
								cont_no=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("CONTREFNO=")){
							String temp[] = split_New_Value[i].split("CONTREFNO=");
							if(temp.length>0){
								cont_ref_no=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("CONTTYPE=")){
							String temp[] = split_New_Value[i].split("CONTTYPE=");
							if(temp.length>0){
								contract_type=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("TRADE_REFNO=")){
							String temp[] = split_New_Value[i].split("TRADE_REFNO=");
							if(temp.length>0){
								trade_ref_no=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("DDADT=")){
							String temp[] = split_New_Value[i].split("DDADT=");
							if(temp.length>0){
								dda_dt=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("DDATIME=")){
							String temp[] = split_New_Value[i].split("DDATIME=");
							if(temp.length>0){
								dda_time=temp[1];
							}
						}
						
						if(split_New_Value[i].startsWith("SIGNDT=")){
							String temp[] = split_New_Value[i].split("SIGNDT=");
							if(temp.length>0){
								signing_dt=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("SIGNTIME=")){
							String temp[] = split_New_Value[i].split("SIGNTIME=");
							if(temp.length>0){
								signing_time=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("ENTDT=")){
							String temp[] = split_New_Value[i].split("ENTDT=");
							if(temp.length>0){
								ent_dt=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("ENTTIME=")){
							String temp[] = split_New_Value[i].split("ENTTIME=");
							if(temp.length>0){
								ent_time=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("AGMTTYPE=")){
							String temp[] = split_New_Value[i].split("AGMTTYPE=");
							if(temp.length>0){
								agmt_type=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("AGMTBASE=")){
							String temp[] = split_New_Value[i].split("AGMTBASE=");
							if(temp.length>0){
								agmt_base=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("STARTDT=")){
							String temp[] = split_New_Value[i].split("STARTDT=");
							if(temp.length>0){
								start_dt=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("ENDDT=")){
							String temp[] = split_New_Value[i].split("ENDDT=");
							if(temp.length>0){
								end_dt=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("RATE=")){
							String temp[] = split_New_Value[i].split("RATE=");
							if(temp.length>0){
								rate=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("RATEUNIT=")){
							String temp[] = split_New_Value[i].split("RATEUNIT=");
							if(temp.length>0){
								rate_unit=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("TCQ=")){
							String temp[] = split_New_Value[i].split("TCQ=");
							if(temp.length>0){
								tcq=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("DCQ=")){
							String temp[] = split_New_Value[i].split("DCQ=");
							if(temp.length>0){
								dcq=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("QTYMOD=")){
							String temp[] = split_New_Value[i].split("QTYMOD=");
							if(temp.length>0){
								var_qty=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("QUNIT=")){
							String temp[] = split_New_Value[i].split("QUNIT=");
							if(temp.length>0){
								quantity_unit=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("MDCQ=")){
							String temp[] = split_New_Value[i].split("MDCQ=");
							if(temp.length>0){
								mdcq_percentage=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("GXFEE=")){
							String temp[] = split_New_Value[i].split("GXFEE=");
							if(temp.length>0){
								txn_charges=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("GXFEEUNIT=")){
							String temp[] = split_New_Value[i].split("GXFEEUNIT=");
							if(temp.length>0){
								txn_unit=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("POSTMARG=")){
							String temp[] = split_New_Value[i].split("POSTMARG=");
							if(temp.length>0){
								post_margin=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("CONT_STATUS=")){
							String temp[] = split_New_Value[i].split("CONT_STATUS=");
							if(temp.length>0){
								cont_status=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("SLOPE=")){
							String temp[] = split_New_Value[i].split("SLOPE=");
							if(temp.length>0){
								slope=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("CONSTANT=")){
							String temp[] = split_New_Value[i].split("CONSTANT=");
							if(temp.length>0){
								constant=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("CURVE_NM=")){
							String temp[] = split_New_Value[i].split("CURVE_NM=");
							if(temp.length>0){
								curve_nm=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("VP=")){
							String temp[] = split_New_Value[i].split("VP=");
							if(temp.length>0){
								vp_flag=temp[1];
							}
						}
					}
					
					if(!old_values.equals(""))
					{
						String split_Old_Value[] = old_values.split("#");
						for(int i=0; i<split_Old_Value.length; i++)
						{
							if(split_Old_Value[i].startsWith("CP=")){
								String temp[] = split_Old_Value[i].split("CP=");
								if(temp.length>0){
									old_cp=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("NAME=")){
								String temp[] = split_Old_Value[i].split("NAME=");
								if(temp.length>0){
									old_name=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("ABBR=")){
								String temp[] = split_Old_Value[i].split("ABBR=");
								if(temp.length>0){
									old_abbr=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("CONTNAME=")){
								String temp[] = split_Old_Value[i].split("CONTNAME=");
								if(temp.length>0){
									old_cont_name=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("CONTNO=")){
								String temp[] = split_Old_Value[i].split("CONTNO=");
								if(temp.length>0){
									old_cont_no=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("CONTREFNO=")){
								String temp[] = split_Old_Value[i].split("CONTREFNO=");
								if(temp.length>0){
									old_cont_ref_no=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("CONTTYPE=")){
								String temp[] = split_Old_Value[i].split("CONTTYPE=");
								if(temp.length>0){
									old_contract_type=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("TRADE_REFNO=")){
								String temp[] = split_Old_Value[i].split("TRADE_REFNO=");
								if(temp.length>0){
									old_trade_ref_no=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("DDADT=")){
								String temp[] = split_Old_Value[i].split("DDADT=");
								if(temp.length>0){
									old_dda_dt=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("DDATIME=")){
								String temp[] = split_Old_Value[i].split("DDATIME=");
								if(temp.length>0){
									old_dda_time=temp[1];
								}
							}
							
							if(split_Old_Value[i].startsWith("SIGNDT=")){
								String temp[] = split_Old_Value[i].split("SIGNDT=");
								if(temp.length>0){
									old_signing_dt=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("SIGNTIME=")){
								String temp[] = split_Old_Value[i].split("SIGNTIME=");
								if(temp.length>0){
									old_signing_time=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("ENTDT=")){
								String temp[] = split_Old_Value[i].split("ENTDT=");
								if(temp.length>0){
									old_ent_dt=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("ENTTIME=")){
								String temp[] = split_Old_Value[i].split("ENTTIME=");
								if(temp.length>0){
									old_ent_time=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("AGMTTYPE=")){
								String temp[] = split_Old_Value[i].split("AGMTTYPE=");
								if(temp.length>0){
									old_agmt_type=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("AGMTBASE=")){
								String temp[] = split_Old_Value[i].split("AGMTBASE=");
								if(temp.length>0){
									old_agmt_base=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("STARTDT=")){
								String temp[] = split_Old_Value[i].split("STARTDT=");
								if(temp.length>0){
									old_start_dt=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("ENDDT=")){
								String temp[] = split_Old_Value[i].split("ENDDT=");
								if(temp.length>0){
									old_end_dt=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("RATE=")){
								String temp[] = split_Old_Value[i].split("RATE=");
								if(temp.length>0){
									old_rate=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("RATEUNIT=")){
								String temp[] = split_Old_Value[i].split("RATEUNIT=");
								if(temp.length>0){
									old_rate_unit=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("TCQ=")){
								String temp[] = split_Old_Value[i].split("TCQ=");
								if(temp.length>0){
									old_tcq=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("DCQ=")){
								String temp[] = split_Old_Value[i].split("DCQ=");
								if(temp.length>0){
									old_dcq=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("QTYMOD=")){
								String temp[] = split_Old_Value[i].split("QTYMOD=");
								if(temp.length>0){
									old_var_qty=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("QUNIT=")){
								String temp[] = split_Old_Value[i].split("QUNIT=");
								if(temp.length>0){
									old_quantity_unit=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("MDCQ=")){
								String temp[] = split_Old_Value[i].split("MDCQ=");
								if(temp.length>0){
									old_mdcq_percentage=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("GXFEE=")){
								String temp[] = split_Old_Value[i].split("GXFEE=");
								if(temp.length>0){
									old_txn_charges=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("GXFEEUNIT=")){
								String temp[] = split_Old_Value[i].split("GXFEEUNIT=");
								if(temp.length>0){
									old_txn_unit=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("POSTMARG=")){
								String temp[] = split_Old_Value[i].split("POSTMARG=");
								if(temp.length>0){
									old_post_margin=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("CONT_STATUS=")){
								String temp[] = split_Old_Value[i].split("CONT_STATUS=");
								if(temp.length>0){
									old_cont_status=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("SLOPE=")){
								String temp[] = split_Old_Value[i].split("SLOPE=");
								if(temp.length>0){
									old_slope=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("CONSTANT=")){
								String temp[] = split_Old_Value[i].split("CONSTANT=");
								if(temp.length>0){
									old_constant=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("CURVE_NM=")){
								String temp[] = split_Old_Value[i].split("CURVE_NM=");
								if(temp.length>0){
									old_curve_nm=temp[1];
								}
							}
						}
					}
					if(agmt_base.equals("X")) 
					{
						agmt_base="Ex-Terminal";
					}
					else if (agmt_base.equals("D"))
					{
						agmt_base="Delivery";
					}
					if(agmt_type.equals("0")) 
					{
						agmt_type="Term";
					}
					else if (agmt_type.equals("1"))
					{
						agmt_type="Spot";
					}

					if(old_agmt_base.equals("X")) 
					{
						old_agmt_base="Ex-Terminal";
					}
					else if (old_agmt_base.equals("D"))
					{
						old_agmt_base="Delivery";
					}
					if(old_agmt_type.equals("0")) 
					{
						old_agmt_type="Term";
					}
					else if (old_agmt_type.equals("1"))
					{
						old_agmt_type="Spot";
					}
					
					if(rate_unit.equals("1")) 
					{
						rate_unit="INR/MMBTU";
					}
					else if (rate_unit.equals("2"))
					{
						rate_unit="USD/MMBTU";
					}
					
					if(quantity_unit.equals("1")) 
					{
						quantity_unit="MMBTU";
					}
					else if (quantity_unit.equals("2"))
					{
						quantity_unit="TBTU";
					}
					if(txn_unit.equals("1")) 
					{
						txn_unit="INR/MMBTU";
					}
					else if (txn_unit.equals("2"))
					{
						txn_unit="USD/MMBTU";
					}
					
					cont_type_nm = utilBean.getContractTypeName(contract_type);
					
					if(!cp.equals(""))
					{
						String contractDetail="";
						
						if(old_values.equals("")) 
						{
							if(!name.equals("")) 
							{
								cont_status = "New";
								
								contractDetail="Name : "+name+"<font style=\"color:blue\">( )</font><br>"
										+ "Abbreviation : "+abbr+"<font style=\"color:blue\">( )</font><br>"
										+ "Contract Ref#: "+cont_ref_no+"<font style=\"color:blue\">( )</font><br>"
										+ "Trader Ref#: "+trade_ref_no+"<font style=\"color:blue\">( )</font><br>"
										+ "Contract Type : "+cont_type_nm+"<font style=\"color:blue\">( )</font><br>"
										+ "DDA Date: "+dda_dt+" "+dda_time+"<font style=\"color:blue\">( )</font><br>"
										+ "Signing Date : "+signing_dt+" "+signing_time+"<font style=\"color:blue\">( )</font><br>"
										+ "Deal Enter Date : "+ent_dt+" "+ent_time+"<font style=\"color:blue\">( )</font><br>";
										if(contract_type.equals("S")||contract_type.equals("L")||contract_type.equals("X")) 
										{
											contractDetail+= "Agreement Type : "+agmt_type+"<font style=\"color:blue\">( )</font><br>"
															+ "Agreement Base 	: "+agmt_base+"<font style=\"color:blue\">( )</font><br>";
										}
										contractDetail+= "Start Date : "+start_dt+"<font style=\"color:blue\">( )</font><br>"
										+ "End Date : "+end_dt+"<font style=\"color:blue\">( )</font><br>"
										+ "Gas Price: "+rate+" "+rate_unit+"<font style=\"color:blue\">( )</font><br>"
										+ "TCQ : "+tcq+" "+quantity_unit+"<font style=\"color:blue\">( )</font><br>"
										+ "DCQ : "+dcq+"<font style=\"color:blue\">( )</font><br>"
										//+ "Quantity Modification : "+var_qty+"<br>"
										+ "MDCQ(%) : "+mdcq_percentage+"<font style=\"color:blue\">( )</font><br>";
										if(contract_type.equals("X")||contract_type.equals("I")) 
										{
											contractDetail+= "GX Transaction Fee : "+txn_charges+" "+txn_unit+"<font style=\"color:blue\">( )</font><br>"
															+ "Post Trade Margin(%) : "+post_margin+"<font style=\"color:blue\">( )</font><br>";
										}
							}
									
							if(vp_flag.equals("Y"))
							{
								contractDetail+= "Index : "+curve_nm.replace("<br>", ", ")+"<font style=\"color:blue\">( )</font><br>"
										+ "Slope : "+slope.replace("<br>", ", ")+"<font style=\"color:blue\">( )</font><br>"
										+ "Constant : "+constant.replace("<br>", ", ")+"<font style=\"color:blue\">( )</font><br>";
							}
						}
						else
						{
							//deal_status="";
							
							if(!name.equals(old_name)){
								contractDetail+="Name : "+name+"<font style=\"color:blue\"> ( "+old_name+" )</font>"+"</font><br>";
							}
							if(!abbr.equals(old_abbr)) {
								contractDetail+= "Abbreviation : "+abbr+"<font style=\"color:blue\"> ( "+old_abbr+" )"+"</font><br>";
							}
							if(!cont_ref_no.equals(old_cont_ref_no)) {
								contractDetail += "Contract Ref#: "+cont_ref_no+"<font style=\"color:blue\"> ( "+old_cont_ref_no+" )"+"</font><br>";
							}
							if(!contract_type.equals(old_contract_type)) {
								contractDetail += "Contract Type : "+cont_type_nm+"<font style=\"color:blue\"> ( "+old_contract_type+ ")"+"</font><br>";
							}
							if(!trade_ref_no.equals(old_trade_ref_no)) {
								contractDetail += "Trader Ref#: "+trade_ref_no+"<font style=\"color:blue\"> ( "+old_trade_ref_no+" )"+"</font><br>";
							}
							if(!dda_dt.equals(old_dda_dt) || !dda_time.equals(old_dda_time)) {
								contractDetail +=  "DDA Date: "+dda_dt+" "+dda_time+"<font style=\"color:blue\"> ( "+old_dda_dt+" "+old_dda_time+" )"+"</font><br>";
							}
							if(!signing_dt.equals(old_signing_dt) || !signing_time.equals(old_signing_time)) {
								contractDetail += "Signing Date : "+signing_dt+" "+signing_time+"<font style=\"color:blue\">( "+old_signing_dt+" "+old_signing_time+" )"+"</font><br>";
							}
							if(!ent_dt.equals(old_ent_dt)||!ent_time.equals(old_ent_time)) {
								contractDetail += "Deal Enter Date : "+ent_dt+" "+ent_time+"<font style=\"color:blue\"> ( "+old_ent_dt+" "+old_ent_time+" )"+"</font><br>";
							}
							if(contract_type.equals("S")||contract_type.equals("L")||contract_type.equals("X")) 
							{
								if(!agmt_type.equals(old_agmt_type)) {
									contractDetail += "Agreement Type : "+agmt_type+"<font style=\"color:blue\"> ( "+old_agmt_type+" )"+"</font><br>";
								}
								if(!agmt_base.equals(old_agmt_base)) {
									contractDetail += "Agreement Base : "+agmt_base+"<font style=\"color:blue\"> ( "+old_agmt_base+" )"+"</font><br>";
								}
							}
							if(!start_dt.equals(old_start_dt)) {
								contractDetail += "Start Date : "+start_dt+"<font style=\"color:blue\"> ( "+old_start_dt+" )"+"</font><br>";
							}
							if(!end_dt.equals(old_end_dt)) {
								contractDetail+="End Date : "+end_dt+"<font style=\"color:blue\"> ( "+old_end_dt+" )"+"</font><br>";
							}
							if(!rate.equals(old_rate)) {
								contractDetail+= "Gas Price: "+rate+" "+rate_unit+"<font style=\"color:blue\"> ( "+old_rate+" "+rate_unit+" )"+"</font><br>";
							}
							if(!tcq.equals(old_tcq)) {
								contractDetail+= "TCQ : "+tcq+" "+quantity_unit+"<font style=\"color:blue\"> ( "+old_tcq+" "+quantity_unit+" )"+"</font><br>";
							}
							if(!dcq.equals(old_dcq)) {
								contractDetail+=  "DCQ : "+dcq+"<font style=\"color:blue\"> ( "+old_dcq+" )"+"</font><br>";
							}
//							if(!var_qty.equals(var_qty)) {
//								contractDetail+=  "Quantity Modification : "+var_qty+"<br>";
//							}
							if(!mdcq_percentage.equals(old_mdcq_percentage)) {
								contractDetail+=  "MDCQ(%) : "+mdcq_percentage+"<font style=\"color:blue\"> ( "+old_mdcq_percentage+" )"+"</font><br>";
							}
							if(contract_type.equals("X")||contract_type.equals("I"))
							{
								if(!txn_charges.equals(old_txn_charges)) {
									contractDetail+=  "GX Transaction Fee : "+txn_charges+" "+txn_unit+"<font style=\"color:blue\"> ( "+old_txn_charges+" "+txn_unit+" )"+"</font><br>";
								}
								if(!post_margin.equals(old_post_margin)) {
									contractDetail+=  "Post Trade Margin(%) : "+post_margin+"<font style=\"color:blue\"> ( "+old_post_margin+" )"+"</font><br>";
								}
							}
							if(!curve_nm.equals(old_curve_nm)) {
								contractDetail+=  "Index : "+curve_nm.replace("<br>", ", ")+"<font style=\"color:blue\"> ( "+old_curve_nm.replace("<br>", ", ")+" )"+"</font><br>";
							}
							if(!slope.equals(old_slope)) {
								contractDetail+=  "Slope : "+slope.replace("<br>", ", ")+"<font style=\"color:blue\"> ( "+old_slope.replace("<br>", ", ")+" )"+"</font><br>";
							}
							if(!constant.equals(old_constant)) {
								contractDetail+=  "Constant : "+constant.replace("<br>", ", ")+"<font style=\"color:blue\"> ( "+old_constant.replace("<br>", ", ")+" )"+"</font><br>";
							}
						}
						
						if(form_nm.equals("Confirmation Notice"))
						{
							cont_no=comp_cd+contract_type+cp+"-"+cont_no;
						}
						
						if(cont_status.equals("Y") || cont_status.equals("A"))
						{
							cont_status="Approved";
						}
						if(cont_status.equals("F"))
						{
							cont_status="New";
						}
						if(cont_status.equals("P"))
						{
							cont_status="Pending Approval";
						}
						if(cont_status.equals("N"))
						{
							cont_status="Not Approved";
						}

						if(!VCOUNTERPARTY_CD.contains(cp)) 
						{
							VCOUNTERPARTY_CD.add(cp);
							VMST_COUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,cp));
							VMST_COUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,cp));
						}
						if(!contractDetail.isEmpty() && !cont_no.isEmpty())
						{
							if(status_flag.equals("New Deals")) 
							{
								if(cont_status.equals("New")) 
								{
									if(!counterparty_cd.equals("0")) 
									{
										if(counterparty_cd.equals(cp)) 
										{
											VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,cp));
											VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,cp));
											VDEAL_NAME.add(cont_ref_no);
											VCONT_NO.add(cont_no);
											VDEAL_DETAILS.add(contractDetail);
											VLAST_UPDATE.add(update_dt+"&nbsp;&nbsp;"+time);
											VLAST_UPDATE_BY.add(update_by);
											VDEAL_STATUS.add(cont_status);
										}
									}
									else 
									{
										VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,cp));
										VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,cp));
										VCONT_NO.add(cont_no);
										VDEAL_NAME.add(cont_ref_no);
										VDEAL_DETAILS.add(contractDetail);
										VLAST_UPDATE.add(update_dt+"&nbsp;&nbsp;"+time);
										VLAST_UPDATE_BY.add(update_by);
										VDEAL_STATUS.add(cont_status);
									}
								}
							}
							else
							{
								if(!counterparty_cd.equals("0")) 
								{
									if(counterparty_cd.equals(cp)) 
									{
										VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,cp));
										VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,cp));
										VDEAL_NAME.add(cont_ref_no);
										VCONT_NO.add(cont_no);
										VDEAL_DETAILS.add(contractDetail);
										VLAST_UPDATE.add(update_dt+"&nbsp;&nbsp;"+time);
										VLAST_UPDATE_BY.add(update_by);
										VDEAL_STATUS.add(cont_status);
									}
								}
								else 
								{
									VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,cp));
									VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,cp));
									VCONT_NO.add(cont_no);
									VDEAL_NAME.add(cont_ref_no);
									VDEAL_DETAILS.add(contractDetail);
									VLAST_UPDATE.add(update_dt+"&nbsp;&nbsp;"+time);
									VLAST_UPDATE_BY.add(update_by);
									VDEAL_STATUS.add(cont_status);
								}
							}
						}
					}
				}
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String getSegmentNm(String seg_ty)
	{
		String function_nm="getSegmentNm()";
		String nm="";
		try
		{
			nm = utilBean.getContractTypeName(seg_ty);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return nm;
	}
	
	public void getTraderCustomerList() 
	{
		String function_nm="getTraderCustomerList()";
		try 
		{
			utilBean.getEffectiveMultiEntityCounterpartyList(conn,comp_cd,"T,C");
			VMST_COUNTERPARTY_CD= utilBean.getCOUNTERPARTY_CD();
			VMST_COUNTERPARTY_NM = utilBean.getCOUNTERPARTY_NM();
			VMST_COUNTERPARTY_ABBR = utilBean.getCOUNTERPARTY_ABBR();
		} 
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSellLateDealsDtl() 
	{
		String function_nm="getSellLateDealsDtl()";
		try 
		{
			queryString6 = "SELECT COMPANY_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY'),CUT_OFF_TIME "
					+ "FROM FMS_LATEDEAL_LOGIC_MST A "
					+ "WHERE COMPANY_CD=? AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_LATEDEAL_LOGIC_MST B WHERE A.COMPANY_CD=B.COMPANY_CD )";
			stmt6 = conn.prepareStatement(queryString6);
			stmt6.setString(1, comp_cd);
			rset6 = stmt6.executeQuery();
			if(rset6.next()) 
			{
				String eff_dt = rset6.getString(2)==null?"":rset6.getString(2);
				cut_off_time = rset6.getString(3)==null?"18:00":rset6.getString(3);
			}
			else 
			{
				cut_off_time = "18:00";//By Default Value..
			}
			rset6.close();
			stmt6.close();
			
			VSELL_SEGMENT.add("Supply Notice");
			VSELL_SEGMENT.add("Letter of Agreement");
			VSELL_SEGMENT.add("IGX");
			VSELL_SEGMENT.add("Hedge Deal(Sell)");
			
			VSELL_SEGMENT_TYPE.add("S");
			VSELL_SEGMENT_TYPE.add("L");
			VSELL_SEGMENT_TYPE.add("X");
			VSELL_SEGMENT_TYPE.add("VS");
			
			int index=0;
			
			if(!segmentType.equals("0"))
			{
				VSELL_TEMP_SEGMENT_TYPE.add(segmentType);
				
				if(segmentType.equals("S"))
				{
					VSELL_TEMP_SEGMENT.add("Supply Notice");
				}
				else if(segmentType.equals("L"))
				{
					VSELL_TEMP_SEGMENT.add("Letter of Agreement");
				}
				else if(segmentType.equals("X"))
				{
					VSELL_TEMP_SEGMENT.add("IGX");
				}
				else if(segmentType.equals("VS"))
				{
					VSELL_TEMP_SEGMENT.add("Hedge Deal(Sell)");
				}
				else
				{
					VSELL_TEMP_SEGMENT.add("");
				}
			}
			else
			{
				VSELL_TEMP_SEGMENT=VSELL_SEGMENT;
				VSELL_TEMP_SEGMENT_TYPE=VSELL_SEGMENT_TYPE;
			}
			for(int i=0; i<VSELL_TEMP_SEGMENT_TYPE.size(); i++)
			{
				if(VSELL_TEMP_SEGMENT_TYPE.elementAt(i).equals("VS"))
				{
					getDerivativesDealDtls(VSELL_TEMP_SEGMENT_TYPE.elementAt(i).toString()); //Added By AP20251215
				}
				else
				{
					index=0;
					queryString5="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONT_REF_NO,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),"
							+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),"
							+ "TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI'),ENT_BY,AGMT_BASE, TCQ,DCQ,TO_CHAR(DDA_DT,'DD/MM/YYYY'),DDA_TIME "
							+ "FROM FMS_SUPPLY_CONT_MST A "
							+ "WHERE COMPANY_CD=?"
							+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
							+ "AND ENT_DT<=TO_DATE(?,'DD/MM/YYYY') AND ENT_DT>=TO_DATE(?,'DD/MM/YYYY') ";
					queryString5+=" AND A.CONTRACT_TYPE=? ";
					if(!counterparty_cd.equals("0"))
					{
						queryString5+="AND A.COUNTERPARTY_CD=? ";
					}
					queryString5+=" ORDER BY ENT_DT ASC ";
					stmt5 = conn.prepareStatement(queryString5);
					stmt5.setString(1, comp_cd);
					stmt5.setString(2, to_dt);
					stmt5.setString(3, from_dt);
					stmt5.setString(4, ""+VSELL_TEMP_SEGMENT_TYPE.elementAt(i));
					if(!counterparty_cd.equals("0"))
					{
						stmt5.setString(5, counterparty_cd);
					}
					rset5 = stmt5.executeQuery();
					while(rset5.next())
					{
						index+=1;
						String countpty_cd = rset5.getString(2)==null?"":rset5.getString(2);
						String agmt = rset5.getString(3)==null?"":rset5.getString(3);
						String agmt_rev = rset5.getString(4)==null?"":rset5.getString(4);
						String cont = rset5.getString(5)==null?"":rset5.getString(5);
						String cont_rev = rset5.getString(6)==null?"":rset5.getString(6);
						String cont_ref = rset5.getString(7)==null?"":rset5.getString(7);
						VSELL_SIGNING_DT.add(rset5.getString(8)==null?"":rset5.getString(8));
						VSELL_START_DT.add(rset5.getString(9)==null?"":rset5.getString(9));
						VSELL_END_DT.add(rset5.getString(10)==null?"":rset5.getString(10));
						String ent_dt=(rset5.getString(11)==null?"":rset5.getString(11));
						VSELL_ENT_DT.add(ent_dt);
						String ent_by=(rset5.getString(12)==null?"":rset5.getString(12));
						VSELL_ENT_BY.add(""+utilBean.getEmpName(conn,ent_by));
						String agmt_base = rset5.getString(13)==null?"":rset5.getString(13);
						VSELL_TCQ.add(rset5.getString(14)==null?"":rset5.getString(14));
						VSELL_DCQ.add(rset5.getString(15)==null?"":rset5.getString(15));
						VSELL_QTY_UNIT.add("");
						String dda_dt=(rset5.getString(16)==null?"":rset5.getString(16));
						VSELL_DDA_DT.add(dda_dt);
						String dda_time=(rset5.getString(17)==null?"":rset5.getString(17));
						VSELL_DDA_TIME.add(dda_time);
						String cont_type=""+VSELL_TEMP_SEGMENT_TYPE.elementAt(i);
						
						//String dealNo=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
						String dealNo = utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
						if(agmt_base.equals("D"))
						{
							dealNo=dealNo+" <font style='background: #a6ff4d;'>[DLV]</font>";
						}
						VSELL_DIS_CONT_MAPPING.add(dealNo);
						
						VSELL_COUNTERPARTY_CD.add(countpty_cd);
						VSELL_COUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
						VSELL_COUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
						
						String[] spilt_ent_dt = ent_dt.split(" ");
						String spli_ent_dt = spilt_ent_dt[0];
						String ent_time = spilt_ent_dt[1];
						String type="SELL";
						checkLateDeal(dda_dt,dda_time,spli_ent_dt,ent_time,type);
					}
					VSELL_INDEX.add(index);
					rset5.close();
					stmt5.close();
				}
			}
		} 
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	public void getPurchaseLateDealsDtl()
	{
		String function_nm="getPurchaseLateDealsDtl()";
		try
		{
			queryString0 = "SELECT COMPANY_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY'),CUT_OFF_TIME "
					+ "FROM FMS_LATEDEAL_LOGIC_MST A "
					+ "WHERE COMPANY_CD=? AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_LATEDEAL_LOGIC_MST B WHERE A.COMPANY_CD=B.COMPANY_CD )";
			stmt0 = conn.prepareStatement(queryString0);
			stmt0.setString(1, comp_cd);
			rset0 = stmt0.executeQuery();
			if(rset0.next()) 
			{
				String eff_dt = rset0.getString(2)==null?"":rset0.getString(2);
				cut_off_time = rset0.getString(3)==null?"18:00":rset0.getString(3);
			}
			else 
			{
				cut_off_time = "18:00";//By Default Value..
			}
			rset0.close();
			stmt0.close();
			
			//FOR PURCHASE...
			if(segmentType.equals("D") || segmentType.equals("I") || segmentType.equals("N") || segmentType.equals("T")|| segmentType.equals("VB"))
			{
				VPURCHASE_DISPLAY_SEGMENT_TYP.add(segmentType);
				if(segmentType.equals("VB"))
				{
					VPURCHASE_DISPLAY_SEGMENT.add(""+getSegmentNm("V")+" Purchase");
				}
				else
				{
					VPURCHASE_DISPLAY_SEGMENT.add(""+getSegmentNm(segmentType)+" Purchase");
				}
			}
			else
			{
				VPURCHASE_DISPLAY_SEGMENT_TYP.add("D");
				VPURCHASE_DISPLAY_SEGMENT.add(""+getSegmentNm("D")+" Purchase");
				
				VPURCHASE_DISPLAY_SEGMENT_TYP.add("I");
				VPURCHASE_DISPLAY_SEGMENT.add(""+getSegmentNm("I")+" Purchase");
				
				VPURCHASE_DISPLAY_SEGMENT_TYP.add("N");
				VPURCHASE_DISPLAY_SEGMENT.add(""+getSegmentNm("N")+" Purchase");
				
				VPURCHASE_DISPLAY_SEGMENT_TYP.add("T");
				VPURCHASE_DISPLAY_SEGMENT.add(""+getSegmentNm("T")+" Purchase");
				
				VPURCHASE_DISPLAY_SEGMENT_TYP.add("VB");
				VPURCHASE_DISPLAY_SEGMENT.add(""+getSegmentNm("V")+" Purchase");
			}
			
			int index1=0;
			if(!segmentType.equals("0"))
			{
				VPURCHASE_TEMP_SEGMENT_TYPE.add(segmentType);
			}
			else
			{
				VPURCHASE_TEMP_SEGMENT_TYPE=VPURCHASE_DISPLAY_SEGMENT_TYP;
			}
			for(int i=0; i<VPURCHASE_TEMP_SEGMENT_TYPE.size(); i++)
			{
				
				if(!VPURCHASE_TEMP_SEGMENT_TYPE.elementAt(i).toString().equals("N") && !VPURCHASE_TEMP_SEGMENT_TYPE.elementAt(i).toString().equals("VB"))
				{
					index1=0;
					queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, CONT_NO, TCQ, DCQ, "
							+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),"
							+ "CONT_NAME,RATE_UNIT,CONTRACT_TYPE,TO_CHAR(DDA_DT,'DD/MM/YYYY'),DDA_TIME,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI'),ENT_BY,"
							+ "AGMT_NO,AGMT_REV,CONT_REV "
							+ "FROM FMS_TRADER_CONT_MST A "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE ) "
							+ "AND ENT_DT<=TO_DATE(?,'DD/MM/YYYY') AND ENT_DT>=TO_DATE(?,'DD/MM/YYYY') ";
					if(!counterparty_cd.equals("0"))
					{
						queryString+=" AND COUNTERPARTY_CD=? ";
					}
					queryString+=" ORDER BY ENT_DT ASC ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, ""+VPURCHASE_TEMP_SEGMENT_TYPE.elementAt(i));
					stmt.setString(3, to_dt);
					stmt.setString(4, from_dt);
					if(!counterparty_cd.equals("0"))
					{
						stmt.setString(5, counterparty_cd);
					}
					rset=stmt.executeQuery();
					while(rset.next())
					{
						index1+=1;
						
						String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
						String contNo=rset.getString(3)==null?"0":rset.getString(3);
						VPURCHASE_TCQ.add(rset.getString(4)==null?"0":rset.getString(4));
						VPURCHASE_DCQ.add(rset.getString(5)==null?"0":rset.getString(5));
						VPURCHASE_START_DT.add(rset.getString(6)==null?"":rset.getString(6));
						VPURCHASE_END_DT.add(rset.getString(7)==null?"":rset.getString(7));
						VPURCHASE_CONT_NAME.add(rset.getString(8)==null?"":rset.getString(8));
						String rate_unit = rset.getString(9)==null?"1":rset.getString(9);
						String contract_type=rset.getString(10)==null?"0":rset.getString(10);
						String dda_dt=(rset.getString(11)==null?"":rset.getString(11));
						VPURCHASE_DDA_DT.add(dda_dt);
						String dda_time=(rset.getString(12)==null?"":rset.getString(12));
						VPURCHASE_DDA_TIME.add(dda_time);
						String ent_dt=(rset.getString(13)==null?"":rset.getString(13));
						VPURCHASE_ENT_DT.add(ent_dt);
						String ent_by=(rset.getString(14)==null?"":rset.getString(14));
						
						String agmtNo=(rset.getString(15)==null?"":rset.getString(15));
						String agmt_rev=(rset.getString(16)==null?"":rset.getString(16));
						String cont_rev=(rset.getString(17)==null?"":rset.getString(17));
						
						VPURCHASE_ENT_BY.add(""+utilBean.getEmpName(conn,ent_by));
						VPURCHASE_CONTRACT_TYPE.add(contract_type);
						VPURCHASE_QTY_UNIT.add("");
						
						VPURCHASE_COUNTERPARTY_CD.add(countpty_cd);
						VPURCHASE_COUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
						VPURCHASE_COUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
						
						String disp_cont_no = utilBean.NewDealMappingId(comp_cd, countpty_cd, agmtNo, agmt_rev, contNo, cont_rev, contract_type, "");
						VPURCHASE_CONT_NO.add(disp_cont_no);
						
						String[] spilt_ent_dt = ent_dt.split(" ");
						String spli_ent_dt = spilt_ent_dt[0];
						String ent_time = spilt_ent_dt[1];
						String type="PURCHASE";
						checkLateDeal(dda_dt,dda_time,spli_ent_dt,ent_time,type);
					}
					VPURCHASE_INDEX.add(index1);
					rset.close();
					stmt.close();
				}
				else if(VPURCHASE_TEMP_SEGMENT_TYPE.elementAt(i).toString().equals("N"))
				{
					index1=0;
					
					queryString1 = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CARGO_NO,CARGO_QTY,RATE_UNIT,"
							+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI'),ENT_BY,CONT_REV "
							+ "FROM FMS_TRADER_CARGO_MST A "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CARGO_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE ) "
							+ "AND ENT_DT<=TO_DATE(?,'DD/MM/YYYY') AND ENT_DT>=TO_DATE(?,'DD/MM/YYYY') ";
					if(!counterparty_cd.equals("0"))
					{
						queryString1+=" AND COUNTERPARTY_CD=? ";
					}
					queryString1+=" ORDER BY ENT_DT ASC ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, ""+VPURCHASE_TEMP_SEGMENT_TYPE.elementAt(i));
					stmt1.setString(3, to_dt);
					stmt1.setString(4, from_dt);
					if(!counterparty_cd.equals("0"))
					{
						stmt1.setString(5, counterparty_cd);
					}
					rset6=stmt1.executeQuery();
					while(rset6.next())
					{
						index1+=1;
						String company_cd = rset6.getString(1)==null?"":rset6.getString(1);
						String countpty_cd=rset6.getString(2)==null?"":rset6.getString(2);
						String agmt_no = rset6.getString(3)==null?"":rset6.getString(3);
						String agmt_rev = rset6.getString(4)==null?"":rset6.getString(4);
						String contract_type=rset6.getString(5)==null?"0":rset6.getString(5);
						String cont_no = rset6.getString(6)==null?"":rset6.getString(6);
						String cargo_no = rset6.getString(7)==null?"":rset6.getString(7);
						String cont_rev = rset6.getString(14)==null?"":rset6.getString(14);
						
						String disp_cargo_no = utilBean.NewDealMappingId(company_cd, countpty_cd, agmt_no, agmt_rev, cont_no, cont_rev, contract_type, cargo_no);
						
						VPURCHASE_TCQ.add(rset6.getString(8)==null?"0":rset6.getString(8));
						VPURCHASE_DCQ.add(rset6.getString(8)==null?"0":rset6.getString(8));
						VPURCHASE_START_DT.add(rset6.getString(10)==null?"":rset6.getString(10));
						VPURCHASE_END_DT.add(rset6.getString(11)==null?"":rset6.getString(11));
						VPURCHASE_QTY_UNIT.add("");
						
						String rate_unit = rset6.getString(9)==null?"1":rset6.getString(9);
						
						String ent_dt=(rset6.getString(12)==null?"":rset6.getString(12));
						VPURCHASE_ENT_DT.add(ent_dt);
						String ent_by=(rset6.getString(13)==null?"":rset6.getString(13));
						VPURCHASE_ENT_BY.add(""+utilBean.getEmpName(conn,ent_by));
						VPURCHASE_CONTRACT_TYPE.add(contract_type);
						
						VPURCHASE_COUNTERPARTY_CD.add(countpty_cd);
						VPURCHASE_COUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
						VPURCHASE_COUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
						VPURCHASE_CONT_NO.add(disp_cargo_no);
						
						String[] spilt_ent_dt = ent_dt.split(" ");
						String spli_ent_dt = spilt_ent_dt[0];
						String ent_time = spilt_ent_dt[1];
						String type="PURCHASE";
						
						queryString="SELECT CONT_NAME,TO_CHAR(DDA_DT,'DD/MM/YYYY'),DDA_TIME "
								+ "FROM FMS_TRADER_CN_MST A "
								+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONT_NO=? AND COUNTERPARTY_CD=? "
								+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE ) ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, contract_type);
						stmt.setString(3, agmt_no);
						stmt.setString(4, agmt_rev);
						stmt.setString(5, cont_no);
						stmt.setString(6, countpty_cd);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							VPURCHASE_CONT_NAME.add(rset.getString(1)==null?"":rset.getString(1));
							String dda_dt=(rset.getString(2)==null?"":rset.getString(2));
							VPURCHASE_DDA_DT.add(dda_dt);
							String dda_time=(rset.getString(3)==null?"":rset.getString(3));
							VPURCHASE_DDA_TIME.add(dda_time);
							checkLateDeal(dda_dt,dda_time,spli_ent_dt,ent_time,type);
							
						}
						else
						{
							VPURCHASE_CONT_NAME.add("");
							VPURCHASE_DDA_DT.add("");
							VPURCHASE_DDA_TIME.add("");
						}
						
						rset.close();
						stmt.close();
					}
					VPURCHASE_INDEX.add(index1);
					rset6.close();
					stmt1.close();
				}
				else if(VPURCHASE_TEMP_SEGMENT_TYPE.elementAt(i).toString().equals("VB"))
				{
					index1=0;
					
					String queryString5="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONT_NAME,A.CONT_REF_NO,"
							+ "B.INSTRUMENT_NO,B.INSTRUMENT_TYPE,B.BUY_SELL,B.STATUS,B.QTY,B.QTY_UNIT,B.RATE,B.RATE_UNIT,"
							+ "B.PRODUCT_NM,B.CURVE_NM,B.PROJ_METHOD,TO_CHAR(B.CONT_DD_MM_YR,'DD/MM/YYYY'),"
							+ "TO_CHAR(B.PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_END_DT,'DD/MM/YYYY'),B.CONV_FACTOR,TO_CHAR(A.TRADE_DT,'DD/MM/YYYY'),"
							+ "TO_CHAR(A.SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(B.ENT_DT,'DD/MM/YYYY HH24:MI'),B.ENT_BY,A.CONTRACT_TYPE,TO_CHAR(A.DDA_DT,'DD/MM/YYYY'),A.DDA_TIME "
							+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B "
							+ "WHERE A.COMPANY_CD=? AND B.PRICE_START_DT<=TO_DATE(?,'DD/MM/YYYY') AND B.PRICE_END_DT>=TO_DATE(?,'DD/MM/YYYY') AND B.BUY_SELL='BUY' ";
					if(!counterparty_cd.equals("0"))
					{
						queryString5+= "AND A.COUNTERPARTY_CD=? ";
					}
					queryString5+= "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO";
					stmt5 = conn.prepareStatement(queryString5);
					stmt5.setString(1, comp_cd);
					stmt5.setString(2, to_dt);
					stmt5.setString(3, from_dt);
					if(!counterparty_cd.equals("0"))
					{
						stmt5.setString(4, counterparty_cd);
					}
					rset5=stmt5.executeQuery();
					while(rset5.next())
					{
						index1+=1;
						String countpty_cd = rset5.getString(2)==null?"":rset5.getString(2);
						String agmt = rset5.getString(3)==null?"":rset5.getString(3);
						String agmt_rev = rset5.getString(4)==null?"":rset5.getString(4);
						String cont = rset5.getString(5)==null?"":rset5.getString(5);
						String cont_rev = rset5.getString(6)==null?"":rset5.getString(6);
						String cont_ref = rset5.getString(8)==null?"":rset5.getString(8);
						String instrument_no = rset5.getString(9)==null?"":rset5.getString(9);
						//VPURCHASE_SIGNING_DT.add(rset5.getString(25)==null?"":rset5.getString(25));
						VPURCHASE_START_DT.add(rset5.getString(21)==null?"":rset5.getString(21));
						VPURCHASE_END_DT.add(rset5.getString(22)==null?"":rset5.getString(22));
						String ent_dt=(rset5.getString(26)==null?"":rset5.getString(26));
						VPURCHASE_ENT_DT.add(ent_dt);
						String ent_by=(rset5.getString(27)==null?"":rset5.getString(27));
						VPURCHASE_ENT_BY.add(""+utilBean.getEmpName(conn,ent_by));
						//String agmt_base = rset5.getString(13)==null?"":rset5.getString(13);
						VPURCHASE_TCQ.add(rset5.getString(13)==null?"":rset5.getString(13));
						VPURCHASE_DCQ.add("");
						VPURCHASE_QTY_UNIT.add(""+utilBean.getEnergyUnitNm(conn, rset5.getString(14)==null?"":rset5.getString(14)));
						String dda_dt=(rset5.getString(29)==null?"":rset5.getString(29));
						VPURCHASE_DDA_DT.add(dda_dt);
						String dda_time=(rset5.getString(30)==null?"00:00":rset5.getString(30));
						VPURCHASE_DDA_TIME.add(dda_time);
						String cont_type=rset5.getString(28)==null?"":rset5.getString(28);
						
						//String dealNo=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
						String dealNo = utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, instrument_no);
						VPURCHASE_CONT_NO.add(dealNo);
						
						VPURCHASE_COUNTERPARTY_CD.add(countpty_cd);
						VPURCHASE_COUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
						VPURCHASE_COUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
						
						String[] spilt_ent_dt = ent_dt.split(" ");
						String spli_ent_dt = spilt_ent_dt[0];
						String ent_time = spilt_ent_dt[1];
						String type="DERV_PURCHASE";
						checkLateDeal(dda_dt,dda_time,spli_ent_dt,ent_time,type);
					}
					VPURCHASE_INDEX.add(index1);
					rset5.close();
					stmt5.close();
				}
			}	
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void checkLateDeal(String dda_dt,String dda_time,String ent_dt, String ent_time, String type)
	{
		String function_nm="checkLateDeal()";
		try
		{
			//Cut-Off time logic..
			queryString1 = "SELECT COMPANY_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY'),CUT_OFF_TIME "
					+ "FROM FMS_LATEDEAL_LOGIC_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_LATEDEAL_LOGIC_MST B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT <= TO_DATE(?,'DD/MM/YYYY'))";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, from_dt);
			rset1 = stmt1.executeQuery();
			if(rset1.next()) 
			{
				String eff_dt = rset1.getString(2)==null?"":rset1.getString(2);
				cut_off_time = rset1.getString(3)==null?"18:00":rset1.getString(3);
			}
			else 
			{
				cut_off_time = "18:00";//By Default Value..
			}
			rset1.close();
			stmt1.close();
			//Logic For Late Deals...
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
			LocalTime sdf_dda_time = null;
			if (dda_time != null && !dda_time.trim().isEmpty()) 
			{
			    sdf_dda_time = LocalTime.parse(dda_time, formatter);
			}
			else
			{
				sdf_dda_time = LocalTime.parse("00:00", formatter);
			}
			
			LocalTime sdf_ent_time = null;
			if (ent_time != null && !ent_time.trim().isEmpty()) 
			{
				sdf_ent_time = LocalTime.parse(ent_time, formatter);
			}
			else
			{
				sdf_ent_time = LocalTime.parse("00:00", formatter);
			}
	        LocalTime sdf_cutt_off_time = LocalTime.parse(cut_off_time, formatter);
	        LocalTime sdf_noon = LocalTime.parse("12:00", formatter);
	        if(type.equals("DERV_SELL") || type.equals("DERV_PURCHASE"))
	        {
	        	sdf_noon = LocalTime.parse("17:00", formatter);
	        }
			
			int day = 0;
			
			queryString2 = "SELECT TO_DATE(?,'DD/MM/YYYY')-TO_DATE(?,'DD/MM/YYYY') FROM DUAL";
			stmt2 = conn.prepareStatement(queryString2);
			stmt2.setString(1, dda_dt);
			stmt2.setString(2, ent_dt);
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				day = rset2.getInt(1);
				if(day > 0)
				{
					if(type.equals("PURCHASE") || type.equals("DERV_PURCHASE")) 
					{
						VPURCHASE_LATE_DEAL_FLAG.add("");
					}
					else if(type.equals("SELL") || type.equals("DERV_SELL"))
					{
						VSELL_LATE_DEAL_FLAG.add("");
					}
				}
				else
				{
					if(day < -1)
					{
						day = ((-1)*day);
					}
					else if(day == -1)
					{
						day = (-1) * day;
					}
					
					int comparison1 = sdf_dda_time.compareTo(sdf_cutt_off_time);

					if(comparison1 > 0)
					{
						int Counter = checkHoliday(day, dda_dt); 
						
						if(Counter == day)
						{
							int comparison = sdf_ent_time.compareTo(sdf_noon);

							if(Counter>0) 
							{
								if (comparison < 0 || comparison == 0) 
						        {
						        	if(type.equals("PURCHASE") || type.equals("DERV_PURCHASE")) 
									{
										VPURCHASE_LATE_DEAL_FLAG.add("");
									}
									else if(type.equals("SELL") || type.equals("DERV_SELL"))
									{
										VSELL_LATE_DEAL_FLAG.add("");
									}
						        } 
						        else if (comparison > 0) 
						        {
						        	if(type.equals("PURCHASE") || type.equals("DERV_PURCHASE")) 
									{
										VPURCHASE_LATE_DEAL_FLAG.add("Late Deal Entry");
									}
									else if(type.equals("SELL") || type.equals("DERV_SELL"))
									{
										VSELL_LATE_DEAL_FLAG.add("Late Deal Entry");
									}
						        }
							}
							else 
							{
								if(type.equals("PURCHASE") || type.equals("DERV_PURCHASE")) 
								{
									VPURCHASE_LATE_DEAL_FLAG.add("");
								}
								else if(type.equals("SELL") || type.equals("DERV_SELL"))
								{
									VSELL_LATE_DEAL_FLAG.add("");
								}
							}
						}
						else
						{
							int comparison2 = sdf_ent_time.compareTo(sdf_noon);

							if((day-Counter)>1) 
							{
								if(type.equals("PURCHASE") || type.equals("DERV_PURCHASE")) 
								{
									VPURCHASE_LATE_DEAL_FLAG.add("Late Deal Entry");
								}
								else if(type.equals("SELL") || type.equals("DERV_SELL"))
								{
									VSELL_LATE_DEAL_FLAG.add("Late Deal Entry");
								}
							}
							else 
							{
								if ((comparison2 < 0 || comparison2 == 0)) 
						        {
						        	if(type.equals("PURCHASE") || type.equals("DERV_PURCHASE")) 
									{
										VPURCHASE_LATE_DEAL_FLAG.add("");
									}
									else if(type.equals("SELL") || type.equals("DERV_SELL"))
									{
										VSELL_LATE_DEAL_FLAG.add("");
									}
						        } 
						        else if (comparison2 > 0) 
						        {
						        	if(type.equals("PURCHASE") || type.equals("DERV_PURCHASE")) 
									{
										VPURCHASE_LATE_DEAL_FLAG.add("Late Deal Entry");
									}
									else if(type.equals("SELL") || type.equals("DERV_SELL"))
									{
										VSELL_LATE_DEAL_FLAG.add("Late Deal Entry");
									}
						        }
							}
						}							
					}
					else
					{
						if(day == 0)
						{
							if(type.equals("PURCHASE") || type.equals("DERV_PURCHASE")) 
							{
								VPURCHASE_LATE_DEAL_FLAG.add("");
							}
							else if(type.equals("SELL") || type.equals("DERV_SELL"))
							{
								VSELL_LATE_DEAL_FLAG.add("");
							}
						}
						else
						{
							if(type.equals("PURCHASE") || type.equals("DERV_PURCHASE")) 
							{
								VPURCHASE_LATE_DEAL_FLAG.add("Late Deal Entry");
							}
							else if(type.equals("SELL") || type.equals("DERV_SELL"))
							{
								VSELL_LATE_DEAL_FLAG.add("Late Deal Entry");
							}
						}
					}
				}
			}
			rset2.close();
			stmt2.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public int checkHoliday(int day, String dda_dt)
	{
		String function_nm="checkHoliday()";
		int Counter = 0;
		try
		{
			for(int j=1; j <= day; j++)
			{
				String nextDay = "",dayName="";
				int holidayCount = 0; 
				queryString3 = "SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+?,'DD/MM/YYYY'),TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+?,'DAY') FROM DUAL";
				stmt3 = conn.prepareStatement(queryString3);
				stmt3.setString(1, dda_dt);
				stmt3.setInt(2, j);
				stmt3.setString(3, dda_dt);
				stmt3.setInt(4, j);
				rset3=stmt3.executeQuery();
				if(rset3.next())
				{
					nextDay = rset3.getString(1);
					dayName = rset3.getString(2);
				}
				rset3.close();
				stmt3.close();
				
				if(dayName.trim().equals("SATURDAY") || dayName.trim().equals("SUNDAY"))
				{
					Counter += 1;
				}
				else
				{
					queryString4 = "SELECT COUNT(*) FROM FMS_HOLIDAY_DTL "
							+ "WHERE "
							//+ "COMPANY_CD=? AND "
							+ "HOLIDAY_DT = TO_DATE(?,'DD/MM/YYYY') AND STATE_TIN=? AND FLAG=? ";//Considering Maharastra holidays only
					stmt4 = conn.prepareStatement(queryString4);
					//stmt4.setString(1, comp_cd);
					stmt4.setString(1, nextDay);
					stmt4.setString(2, "27");
					stmt4.setString(3, "Y");
					rset4=stmt4.executeQuery();
					if(rset4.next())
					{
						holidayCount = rset4.getInt(1);
					}
					if(holidayCount > 0)
					{
						Counter += 1;
					}
					rset4.close();
					stmt4.close();
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return Counter;
	}
	
	public void getReportDtforPricing() 
	{
		String function_nm="getReportDtforPricing()";
		try 
		{
			String highestDate = "";
			queryString = "SELECT DISTINCT TO_CHAR(REPORT_DT,'DD/MM/YYYY') "
					+ "FROM FMS_FORWARD_PRICE_DTL A "
					+ "WHERE A.REPORT_DT=(SELECT MAX(B.REPORT_DT) FROM FMS_FORWARD_PRICE_DTL B) ";
			stmt = conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while (rset.next())
			{
				highestDate = (rset.getString(1)==null?"":rset.getString(1));
				VMAX_CURVE_DT.add(highestDate);
			}
			rset.close();
			stmt.close();
		} 
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getZEMAXMLFileDetails() 
	{
		String function_nm="getZEMAXMLFileDetails()";
		try 
		{
			String appPath = request.getServletContext().getRealPath("");
			
			String main_folder=CommonVariable.work_dir;
			File main_folderDir = new File(appPath+File.separator+main_folder);
	        if(!main_folderDir.exists())
	        {
	        	main_folderDir.mkdir();
	        }
	        
	        String savePath = appPath+File.separator+main_folder+File.separator+SAVE_DIR;
			File fileSaveDir = new File(savePath);
	        if(!fileSaveDir.exists()) 
	        {
	            fileSaveDir.mkdir();
	        }
			
	        String subSavePath = savePath+File.separator+"Pricing_XML";
	        File subfile = new File(subSavePath);
	        if(!subfile.exists())
	        {
	        	subfile.mkdir();
	        }

	        file_nm="Shell_India_XML_Batch_PROD_"+report_dt;
	        
	        String folder_path = subSavePath+File.separator;
	        String fileNM_path = subSavePath+File.separator+file_nm;

	        File folder = new File(folder_path);
	        if (folder.exists() && folder.isDirectory()) 
	        {
	            File[] files = folder.listFiles();
	            if (files != null) 
	            {
	            	 for (File file : files)
	            	 {
	                     if (file.isFile()) 
	                     {
	                         String allFileNames = file.getName();
	                         long allFileSizes = file.length();
	                         long lastModifiedTime = file.lastModified();

	                         VFILE_NM.add(allFileNames);
	                         VFILE_SIZE.add(allFileSizes);

	                         SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy, hh:mm:ss a");
	                         String formattedDate = dateFormat.format(new Date(lastModifiedTime));

	                         VFILE_UP_ON.add(formattedDate);
	                     }
	                 }
	            }
	        }
	    } 
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getForwardPricingCurveName() 
	{
		String function_nm="getForwardPricingCurveName()";
		try 
		{
			queryString = "SELECT DISTINCT CURVE_NM "
					+ "FROM FMS_FORWARD_PRICE_DTL ";
			stmt = conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while (rset.next())
			{
				VSPOT_CURVE_TYPE.add(rset.getString(1)==null?"":rset.getString(1));
			}
			rset.close();
			stmt.close();
			
			queryString1 = "SELECT DISTINCT TO_CHAR(REPORT_DT,'DD/MM/YYYY'),REPORT_DT "
					+ "FROM FMS_FORWARD_PRICE_DTL "
					+ "ORDER BY REPORT_DT DESC";
			stmt1 = conn.prepareStatement(queryString1);
			rset1=stmt1.executeQuery();
			while (rset1.next())
			{
				VFORWARD_REPORT_DATE.add(rset1.getString(1)==null?"":rset1.getString(1));
			}
			rset1.close();
			stmt1.close();
			
			queryString2 = "SELECT DISTINCT PHYS_FIN "
					+ "FROM FMS_FORWARD_PRICE_DTL ";
			stmt2 = conn.prepareStatement(queryString2);
			rset2=stmt2.executeQuery();
			while (rset2.next())
			{
				VPHYS_FIN_MST.add(rset2.getString(1)==null?"":rset2.getString(1));
			}
			rset2.close();
			stmt2.close();
		}
		catch (SQLException e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getForwardPricing() 
	{
		String function_nm="getForwardPricing()";
		try 
		{
			if(settlement_dt.equals("0") && VMAX_CURVE_DT.size()>0)
			{
				settlement_dt=""+VMAX_CURVE_DT.elementAt(0);
			}
			int count=0;
			queryString = "SELECT DISTINCT CURVE_NM "
					+ "FROM FMS_FORWARD_PRICE_DTL "
					+ "WHERE 0=0 ";
			
			if(!spot_curve_type.equals("0"))
			 { 
				 queryString+="AND CURVE_NM=? "; 
			 }
			//if(!settlement_dt.equals("0"))
			{
				queryString += "AND REPORT_DT = TO_DATE(?,'DD/MM/YYYY') ";
			}
			if(!phy_fin.equals("")) 
			{ 
				 queryString+="AND PHYS_FIN=? "; 
			}
			stmt = conn.prepareStatement(queryString);
			if(!spot_curve_type.equals("0"))
			{
				stmt.setString(++count, spot_curve_type);
			}
			//if(!settlement_dt.equals("0"))
			{
				stmt.setString(++count, settlement_dt);
			}
			if(!phy_fin.equals("")) 
			{ 
				stmt.setString(++count, phy_fin);
			}
			rset=stmt.executeQuery();
			while (rset.next())
			{
				String spot_type = rset.getString(1)==null?"":rset.getString(1);
				VTEMP_CURVE_TYPE.add(spot_type);
				
				int index=0;
				String emp_cd = "";
				
				int count1 = 0;
				queryString1 = "SELECT TO_CHAR(REPORT_DT,'DD/MM/YYYY'), TO_CHAR(CURVE_DT,'MM/YYYY'), CURVE_NM, "
						+ "COMMODITY_TYPE, CURVE_TYPE, CURVE_UNIT, PHYS_FIN, SETTLE_PRICE, "
						+ "ENT_BY, TO_CHAR(ENT_DT,'DD/MM/YYYY'), MODIFY_BY, TO_CHAR(MODIFY_DT,'DD/MM/YYYY') "
						+ "FROM FMS_FORWARD_PRICE_DTL "
						+ "WHERE CURVE_NM=? ";
				//if(!settlement_dt.equals("0"))
				{
					queryString1 += "AND REPORT_DT = TO_DATE(?,'DD/MM/YYYY') ";
				}
				if(!phy_fin.equals("")) 
				{ 
					 queryString1+="AND PHYS_FIN=? "; 
				}
				queryString1+="ORDER BY CURVE_DT ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(++count1, spot_type);
				//if(!settlement_dt.equals("0"))
				{
					stmt1.setString(++count1, settlement_dt);
				}
				if(!phy_fin.equals("")) 
				{
					stmt1.setString(++count1, phy_fin);
				}
				rset1=stmt1.executeQuery();
				while(rset1.next()) 
				{
					index+=1;
					VFORWARD_REPORT_DT.add(rset1.getString(1)==null?"":rset1.getString(1));
					VCURVE_DT.add(rset1.getString(2)==null?"":rset1.getString(2));
					VCURVE_NM.add(rset1.getString(3)==null?"":rset1.getString(3));
					VCOMMODITY_TYPE.add(rset1.getString(4)==null?"":rset1.getString(4));
					VCURVE_TYPE.add(rset1.getString(5)==null?"":rset1.getString(5));
					VCURVE_UNIT.add(rset1.getString(6)==null?"":rset1.getString(6));
					VPHYS_FIN.add(rset1.getString(7)==null?"":rset1.getString(7));
					VSETTLE_PRICE.add(rset1.getString(8)==null?"":rset1.getString(8));
					emp_cd = rset1.getString(9)==null?"":rset1.getString(9);
					String emp_nm = utilBean.getEmpName(conn,emp_cd);
					VENT_BY.add(emp_nm);
					VENT_DT.add(rset1.getString(10)==null?"":rset1.getString(10));
				}
				VINDEX.add(index);
				rset1.close();
				stmt1.close();
			}
			rset.close();
			stmt.close();
		} 
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSettlementPricingCurveName() 
	{
		String function_nm="getSettlementPricingCurveName()";
		try 
		{
			queryString = "SELECT DISTINCT CURVE_NM "
					+ "FROM FMS_SPOT_PRICE_DTL ";
			stmt = conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VSPOT_CURVE_TYPE.add(rset.getString(1)==null?"":rset.getString(1));
			}
			rset.close();
			stmt.close();
			
			//JD20230925
			queryString1 = "SELECT DISTINCT ACTUAL_CURVE "
					+ "FROM FMS_SPOT_PRICE_DTL "
					+ "WHERE ACTUAL_CURVE IS NOT NULL";
			stmt1 = conn.prepareStatement(queryString1);
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				VPRICE_CURVE_TYPE.add(rset1.getString(1)==null?"":rset1.getString(1));
			}
			rset1.close();
			stmt1.close();
			///////////////////////////////
			
			queryString2 = "SELECT DISTINCT TO_CHAR(REPORT_DT,'DD/MM/YYYY') "
					+ "FROM FMS_SPOT_PRICE_DTL ";
			stmt2 = conn.prepareStatement(queryString2);
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				VSPOT_REPORT_DATE.add(rset2.getString(1)==null?"":rset2.getString(1));
			}
			rset2.close();
			stmt2.close();
		}
		catch (SQLException e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSettlementPricing() 
	{
		String function_nm="getSettlementPricing()";
		try 
		{
			int count = 0;
			queryString = "SELECT DISTINCT CURVE_NM "
					+ "FROM FMS_SPOT_PRICE_DTL "
					+ "WHERE 0=0 ";
			if(!spot_curve_type.equals("0")) 
			{ 
				queryString+="AND CURVE_NM=? "; 
			}
			//JD20230926
			if(!price_curve_type.equals("0")) 
			{ 
				queryString+="AND ACTUAL_CURVE=? "; 
			}
			/*JD20230922
			if(!settlement_dt.equals("0")) 
			{
				queryString += "AND REPORT_DT = TO_DATE('"+settlement_dt+"','DD/MM/YYYY') ";
			}*/
			if(!delete_report_Dt.equals("")) 
			{
				queryString += "AND REPORT_DT = TO_DATE(?,'DD/MM/YYYY') ";
			}
			else //JD20230922
			{
				queryString+="AND REPORT_DT <= TO_DATE(?,'DD/MM/YYYY') AND REPORT_DT >= TO_DATE(?,'DD/MM/YYYY') ";
			}
			stmt = conn.prepareStatement(queryString);
			if(!spot_curve_type.equals("0")) 
			{ 
				stmt.setString(++count, spot_curve_type);
			}
			if(!price_curve_type.equals("0")) 
			{ 
				stmt.setString(++count, price_curve_type);
			}
			if(!delete_report_Dt.equals("")) 
			{
				stmt.setString(++count, delete_report_Dt);
			}
			else
			{
				stmt.setString(++count, to_dt);
				stmt.setString(++count, from_dt);
			}
			rset=stmt.executeQuery();
			while (rset.next())
			{
				String spot_type = rset.getString(1)==null?"":rset.getString(1);
				VTEMP_CURVE_TYPE.add(spot_type);
				
				int index=0;
				String emp_cd = "";
				int count1=0;
				queryString1 = "SELECT TO_CHAR(REPORT_DT,'DD/MM/YYYY'), TO_CHAR(CURVE_DT,'DD/MM/YYYY'), CURVE_NM, "
						+ "COMMODITY_TYPE, CURVE_TYPE, CURVE_UNIT, PHYS_FIN, SETTLE_PRICE, "
						+ "ENT_BY, TO_CHAR(ENT_DT,'DD/MM/YYYY'), MODIFY_BY, TO_CHAR(MODIFY_DT,'DD/MM/YYYY'),ACTUAL_CURVE "
						+ "FROM FMS_SPOT_PRICE_DTL "
						+ "WHERE CURVE_NM=?";
				/*JD20230922
				if(!settlement_dt.equals("0")) 
				{
					queryString1 += "AND REPORT_DT = TO_DATE('"+settlement_dt+"','DD/MM/YYYY') ";
				}
				*/
				//JD20230926
				if(!price_curve_type.equals("0")) 
				{ 
					queryString1+="AND ACTUAL_CURVE=? "; 
				}
				if(!delete_report_Dt.equals("")) 
				{
					queryString1 += "AND REPORT_DT = TO_DATE(?,'DD/MM/YYYY') ";
				}
				else //JD20230922
				{
					queryString1+="AND REPORT_DT <= TO_DATE(?,'DD/MM/YYYY') AND REPORT_DT >= TO_DATE(?,'DD/MM/YYYY') ";
				}
				stmt1 =conn.prepareStatement(queryString1);
				stmt1.setString(++count1, spot_type);
				if(!price_curve_type.equals("0")) 
				{ 
					stmt1.setString(++count1, price_curve_type);
				}
				if(!delete_report_Dt.equals("")) 
				{
					stmt1.setString(++count1, delete_report_Dt);
				}
				else //JD20230922
				{
					stmt1.setString(++count1, to_dt);
					stmt1.setString(++count1, from_dt);
				}
				rset1=stmt1.executeQuery();
				while(rset1.next()) 
				{
					index+=1;
					VSPOT_REPORT_DT.add(rset1.getString(1)==null?"":rset1.getString(1));
					VCURVE_DT.add(rset1.getString(2)==null?"":rset1.getString(2));
					VCURVE_NM.add(rset1.getString(3)==null?"":rset1.getString(3));
					VCOMMODITY_TYPE.add(rset1.getString(4)==null?"":rset1.getString(4));
					VCURVE_TYPE.add(rset1.getString(5)==null?"":rset1.getString(5));
					VCURVE_UNIT.add(rset1.getString(6)==null?"":rset1.getString(6));
					VPHYS_FIN.add(rset1.getString(7)==null?"":rset1.getString(7));
					VSETTLE_PRICE.add(rset1.getString(8)==null?"":rset1.getString(8));
					emp_cd = rset1.getString(9)==null?"":rset1.getString(9);
					String emp_nm = utilBean.getEmpName(conn,emp_cd);
					VENT_BY.add(emp_nm);
					VENT_DT.add(rset1.getString(10)==null?"":rset1.getString(10));	
					VACTUAL_CURVE_TYPE.add(rset1.getString(13)==null?"":rset1.getString(13)); //JD20230925
				}
				rset1.close();
				stmt1.close();
				
				// JD20230922
				double avg=0;
				queryString2 = "SELECT AVG(SETTLE_PRICE) "
						+ "FROM FMS_SPOT_PRICE_DTL "
						+ "WHERE CURVE_NM=?";
				if(!delete_report_Dt.equals("")) 
				{
					queryString2 += "AND REPORT_DT = TO_DATE(?,'DD/MM/YYYY') ";
				}
				else
				{
					queryString2+="AND REPORT_DT <= TO_DATE(?,'DD/MM/YYYY') AND REPORT_DT >= TO_DATE(?,'DD/MM/YYYY') ";
				}
				stmt2 =conn.prepareStatement(queryString2);
				stmt2.setString(1, spot_type);
				if(!delete_report_Dt.equals("")) 
				{
					stmt2.setString(2, delete_report_Dt);
				}
				else
				{
					stmt2.setString(2, to_dt);
					stmt2.setString(3, from_dt);
				}
				rset2=stmt2.executeQuery();
				if(rset2.next()) 
				{
					avg = rset2.getDouble(1);				
				}
				VSETTLE_PRICE_AVG.add(nf2.format(avg));	// JD20230922
				
				VINDEX.add(index);
				rset2.close();
				stmt2.close();
			}
			rset.close();
			stmt.close();
		} 
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getHolidayCurve()
	{
		String function_nm="getHolidayCurve()";
		try 
		{
			queryString = "SELECT DISTINCT CURVE_NM "
					+ "FROM FMS_CURVE_HOLIDAY_CALND ";
			stmt = conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while (rset.next())
			{
				VSETTLE_TYPE.add(rset.getString(1)==null?"":rset.getString(1));
			}
			rset.close();
			stmt.close();
		} 
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getYearListForHolidayClendar()
	{
		String function_nm="getYearListForHolidayClendar()";
		try
		{
			int curr_yr=utilDate.getCurrentYear();

			queryString = "SELECT DISTINCT TO_NUMBER(TO_CHAR(TO_DATE(TO_CHAR(HOLIDAY_DT,'DD/MM/YYYY'),'DD/MM/YYYY'),'YYYY')) "
					+ "FROM FMS_CURVE_HOLIDAY_CALND "
					+ "WHERE HOLIDAY_DT IS NOT NULL "
					+ "ORDER BY TO_NUMBER(TO_CHAR(TO_DATE(TO_CHAR(HOLIDAY_DT,'DD/MM/YYYY'),'DD/MM/YYYY'),'YYYY')) DESC";
			stmt = conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VYEAR.add(rset.getString(1)==null?"":rset.getString(1));
			}
			rset.close();
			stmt.close();

			/*
			 * if(!VYEAR.contains(""+curr_yr)) { VYEAR.add(curr_yr); }
			 */
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getHolidayCalendarList()
	{
		String function_nm="getHolidayCalendarList()";
		try 
		{
			int count=0;
			queryString = "SELECT DISTINCT CURVE_NM "
					+ "FROM FMS_CURVE_HOLIDAY_CALND "
					+ "WHERE 0=0 ";
			if(!holiday_type.equals("0")) 
			{ 
				 queryString+="AND CURVE_NM=? "; 
			}
			if(!year.equals("0")) 
			{
				queryString += "AND HOLIDAY_DT >= TO_DATE(?,'DD/MM/YYYY') AND HOLIDAY_DT <= TO_DATE(?,'DD/MM/YYYY')";
			}
			stmt = conn.prepareStatement(queryString);
			if(!holiday_type.equals("0")) 
			{ 
				 stmt.setString(++count, holiday_type);
			}
			if(!year.equals("0")) 
			{
				stmt.setString(++count, "01/01/"+year);
				stmt.setString(++count, "31/12/"+year);
			}
			rset=stmt.executeQuery();
			while (rset.next())
			{
				String holi_type = rset.getString(1)==null?"":rset.getString(1);
				VCURVE_NM.add(holi_type);
			
				int index=0;
				
				queryString1 = "SELECT CURVE_NM, TO_CHAR(HOLIDAY_DT,'DD/MM/YYYY'),HOLIDAY_NM,STATUS, "
						+ "ENT_BY,TO_CHAR(ENT_DT,'DD/MM/YYYY'),TO_CHAR(HOLIDAY_LAST_UP,'DD/MM/YYYY'), "
						+ "MODIFY_BY,TO_CHAR(MODIFY_DT,'DD/MM/YYYY') "
						+ "FROM FMS_CURVE_HOLIDAY_CALND "
						+ "WHERE CURVE_NM=? ";
				if(!year.equals("0")) 
				{
					queryString1 += "AND HOLIDAY_DT >= TO_DATE(?,'DD/MM/YYYY') AND HOLIDAY_DT <= TO_DATE(?,'DD/MM/YYYY')";
				}
				queryString1 += "ORDER BY HOLIDAY_DT";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, holi_type);
				if(!year.equals("0")) 
				{
					stmt1.setString(2, "01/01/"+year);
					stmt1.setString(3, "31/12/"+year);
				}
				rset1=stmt1.executeQuery();
				while(rset1.next()) 
				{
					index+=1;
					VSETTLE_CURVE.add(rset1.getString(1)==null?"":rset1.getString(1));
					VHOLIDAY_DT.add(rset1.getString(2)==null?"":rset1.getString(2));
					VHOLIDAY_NM.add(rset1.getString(3)==null?"":rset1.getString(3));
					VHOLIDAY_STATUS.add(rset1.getString(4)==null?"":rset1.getString(4));
				}
				VINDEX.add(index);
				rset1.close();
				stmt1.close();
			}
			rset.close();
			stmt.close();
		} 
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSettleCurve()
	{
		String function_nm="getSettleCurve()";
		try 
		{
			queryString = "SELECT DISTINCT CURVE_NM "
					+ "FROM FMS_CURVE_SETTLE_CALND ";
			stmt = conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while (rset.next())
			{
				VSETTLE_TYPE.add(rset.getString(1)==null?"":rset.getString(1));
			}
			rset.close();
			stmt.close();
		} 
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSettlementCalendarList()
	{
		String function_nm="getSettlementCalendarList()";
		try 
		{
			int count=0;
			queryString = "SELECT DISTINCT CURVE_NM "
					+ "FROM FMS_CURVE_SETTLE_CALND "
					+ "WHERE 0=0 ";
			if(!settle_curve.equals("0")) 
			{
				queryString += "AND CURVE_NM=?";
			}
			if(!year.equals("0")) 
			{
				queryString += "AND CONT_MONTH >= TO_DATE(?,'DD/MM/YYYY') AND CONT_MONTH <= TO_DATE(?,'DD/MM/YYYY')";
			}
			stmt = conn.prepareStatement(queryString);
			if(!settle_curve.equals("0")) 
			{
				stmt.setString(++count, settle_curve);
			}
			if(!year.equals("0")) 
			{
				stmt.setString(++count, "01/01/"+year);
				stmt.setString(++count, "31/12/"+year);
			}
			rset=stmt.executeQuery();
			while (rset.next())
			{
				String settle_type = rset.getString(1)==null?"":rset.getString(1);
				VCURVE_NM.add(settle_type);
		
				int index=0;
				
				queryString1 = "SELECT CURVE_NM, TO_CHAR(CONT_MONTH,'MM/YYYY'),TO_CHAR(SETTLE_START_DT,'DD/MM/YYYY'), "
						+ "TO_CHAR(SETTLE_END_DT,'DD/MM/YYYY'),TO_CHAR(SETTLE_DT,'DD/MM/YYYY'),ENT_BY,TO_CHAR(ENT_DT,'DD/MM/YYYY'), "
						+ "MODIFY_BY,TO_CHAR(MODIFY_DT,'DD/MM/YYYY') "
						+ "FROM FMS_CURVE_SETTLE_CALND "
						+ "WHERE CURVE_NM=? ";
				if(!year.equals("0")) 
				{
					queryString1 += "AND CONT_MONTH >= TO_DATE(?,'DD/MM/YYYY') AND CONT_MONTH <= TO_DATE(?,'DD/MM/YYYY')";
				}
				queryString1 += "ORDER BY CONT_MONTH";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, settle_type);
				if(!year.equals("0")) 
				{
					stmt1.setString(2, "01/01/"+year);
					stmt1.setString(3, "31/12/"+year);
				}
				rset1=stmt1.executeQuery();
				while(rset1.next()) 
				{
					index+=1;
					VSETTLE_CURVE.add(rset1.getString(1)==null?"":rset1.getString(1));
					VCONT_MONTH.add(rset1.getString(2)==null?"":rset1.getString(2));
					VSETTLE_START_DT.add(rset1.getString(3)==null?"":rset1.getString(3));
					VSETTLE_END_DT.add(rset1.getString(4)==null?"":rset1.getString(4));
					VSETTLE_DT.add(rset1.getString(5)==null?"":rset1.getString(5));
				}
				VINDEX.add(index);
				rset1.close();
				stmt1.close();
			}
			rset.close();
			stmt.close();
		} 
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getYearList()
	{
		String function_nm="getYearList()";
		try
		{
			int curr_yr=utilDate.getCurrentYear();

			queryString = "SELECT DISTINCT TO_NUMBER(TO_CHAR(TO_DATE(TO_CHAR(CONT_MONTH,'DD/MM/YYYY'),'DD/MM/YYYY'),'YYYY')) "
					+ "FROM FMS_CURVE_SETTLE_CALND "
					+ "WHERE CONT_MONTH IS NOT NULL "
					+ "ORDER BY TO_NUMBER(TO_CHAR(TO_DATE(TO_CHAR(CONT_MONTH,'DD/MM/YYYY'),'DD/MM/YYYY'),'YYYY')) DESC";
			stmt = conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VYEAR.add(rset.getString(1)==null?"":rset.getString(1));
			}
			rset.close();
			stmt.close();

			/*
			 * if(!VYEAR.contains(""+curr_yr)) { VYEAR.add(curr_yr); }
			 */
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//AP20251215
	public void getDerivativesDealDtls(String segment)
	{
		String function_nm="getDerivativesDealDtls()";
		try
		{
			int index=0;
					
			String queryString5="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONT_NAME,A.CONT_REF_NO,"
					+ "B.INSTRUMENT_NO,B.INSTRUMENT_TYPE,B.BUY_SELL,B.STATUS,B.QTY,B.QTY_UNIT,B.RATE,B.RATE_UNIT,"
					+ "B.PRODUCT_NM,B.CURVE_NM,B.PROJ_METHOD,TO_CHAR(B.CONT_DD_MM_YR,'DD/MM/YYYY'),"
					+ "TO_CHAR(B.PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_END_DT,'DD/MM/YYYY'),B.CONV_FACTOR,TO_CHAR(A.TRADE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(A.SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(B.ENT_DT,'DD/MM/YYYY HH24:MI'),B.ENT_BY,A.CONTRACT_TYPE,TO_CHAR(A.DDA_DT,'DD/MM/YYYY'),A.DDA_TIME "
					+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B "
					+ "WHERE A.COMPANY_CD=? AND B.PRICE_START_DT<=TO_DATE(?,'DD/MM/YYYY') AND B.PRICE_END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
			if(segment.equals("VS"))
			{
				queryString5+= "AND B.BUY_SELL='SELL' ";
			}
			else if(segment.equals("VB"))
			{
				queryString5+= "AND B.BUY_SELL='BUY' ";
			}
			if(!counterparty_cd.equals("0"))
			{
				queryString5+= "AND A.COUNTERPARTY_CD=? ";
			}
			queryString5+= "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, comp_cd);
			stmt5.setString(2, to_dt);
			stmt5.setString(3, from_dt);
			if(!counterparty_cd.equals("0"))
			{
				stmt5.setString(4, counterparty_cd);
			}
			rset5=stmt5.executeQuery();
			while(rset5.next())
			{
				index+=1;
				String countpty_cd = rset5.getString(2)==null?"":rset5.getString(2);
				String agmt = rset5.getString(3)==null?"":rset5.getString(3);
				String agmt_rev = rset5.getString(4)==null?"":rset5.getString(4);
				String cont = rset5.getString(5)==null?"":rset5.getString(5);
				String cont_rev = rset5.getString(6)==null?"":rset5.getString(6);
				String cont_ref = rset5.getString(8)==null?"":rset5.getString(8);
				String instrument_no = rset5.getString(9)==null?"":rset5.getString(9);
				VSELL_SIGNING_DT.add(rset5.getString(25)==null?"":rset5.getString(25));
				VSELL_START_DT.add(rset5.getString(21)==null?"":rset5.getString(21));
				VSELL_END_DT.add(rset5.getString(22)==null?"":rset5.getString(22));
				String ent_dt=(rset5.getString(26)==null?"":rset5.getString(26));
				VSELL_ENT_DT.add(ent_dt);
				String ent_by=(rset5.getString(27)==null?"":rset5.getString(27));
				VSELL_ENT_BY.add(""+utilBean.getEmpName(conn,ent_by));
				//String agmt_base = rset5.getString(13)==null?"":rset5.getString(13);
				VSELL_TCQ.add(rset5.getString(13)==null?"":rset5.getString(13));
				VSELL_DCQ.add("");
				VSELL_QTY_UNIT.add(""+utilBean.getEnergyUnitNm(conn, rset5.getString(14)==null?"":rset5.getString(14)));
				String dda_dt=(rset5.getString(29)==null?"":rset5.getString(29));
				VSELL_DDA_DT.add(dda_dt);
				String dda_time=(rset5.getString(30)==null?"":rset5.getString(30));
				VSELL_DDA_TIME.add(dda_time);
				String cont_type=rset5.getString(28)==null?"":rset5.getString(28);
				
				//String dealNo=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
				String dealNo = utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, instrument_no);
				VSELL_DIS_CONT_MAPPING.add(dealNo);
				
				VSELL_COUNTERPARTY_CD.add(countpty_cd);
				VSELL_COUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VSELL_COUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				
				String[] spilt_ent_dt = ent_dt.split(" ");
				String spli_ent_dt = spilt_ent_dt[0];
				String ent_time = spilt_ent_dt[1];
				String type="DERV_SELL";
				checkLateDeal(dda_dt,dda_time,spli_ent_dt,ent_time,type);
			}
			VSELL_INDEX.add(index);
			rset5.close();
			stmt5.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}

	String segmentType = "";
	String counterparty_cd = "";
	String from_dt = "";
	String to_dt = "";
	String status_flag = "";
	
	String settle_curve = "";
	String holiday_type = "";
	String year = "";
	String spot_curve_type = "";
	String price_curve_type = ""; //JD20230926
	String settlement_dt = "";
	String delete_report_Dt = "";
	String upload_report_dt = "";
	
	String report_dt = "";
	String fileNM ="";
	String fileSIZE = "";
	
	String emp_cd="";
	String file_url = "";
	String file_nm = "";
	
	String cut_off_time ="18:00"; //By Default Time For Late Deal Entry 
	String phy_fin ="";
	
	public String getCut_off_time() {return cut_off_time;}
	
	public String getFile_url() {return file_url;}
	public String getFile_nm() {return file_nm;}
	
	public void setSegmentType(String segmentType) {this.segmentType = segmentType;}
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	public void setStatus_flag(String status_flag) {this.status_flag = status_flag;}
	
	public String getSpot_curve_type(){return spot_curve_type;}
	public void setSpot_curve_type(String spot_curve_type){this.spot_curve_type = spot_curve_type;}
	public void setPrice_curve_type(String price_curve_type){this.price_curve_type = price_curve_type;} //JD20230926
	public String getHoliday_type(){return holiday_type;}
	public void setHoliday_type(String holiday_type){this.holiday_type = holiday_type;}
	public void setSettle_curve(String settle_curve){this.settle_curve = settle_curve;}
	public String getSettle_curve(){return settle_curve;}
	public void setYear(String year){this.year = year;}
	public String getYear(){return year;}
	public String getSettlement_dt() {return settlement_dt;}
	public void setSettlement_dt(String settlement_dt) {this.settlement_dt = settlement_dt;}
	
	public void setPhy_fin(String phy_fin) {this.phy_fin = phy_fin;}
	
	Vector VCONT_MONTH = new Vector();
	Vector VCURVE_NM = new Vector();
	Vector VSETTLE_START_DT = new Vector();
	Vector VSETTLE_END_DT = new Vector();
	Vector VSETTLE_DT = new Vector();
	Vector VSETTLE_CURVE = new Vector();
	Vector VSETTLE_TYPE = new Vector();
	Vector VHOLIDAY_NM = new Vector();
	Vector VHOLIDAY_DT = new Vector();
	Vector VHOLIDAY_STATUS = new Vector();
	Vector VHOLIDAY_UP = new Vector();
	
	Vector VSPOT_CURVE_TYPE = new Vector();
	Vector<String> VSPOT_REPORT_DATE = new Vector();
	Vector<String> VFORWARD_REPORT_DATE = new Vector();
	Vector VREPORT_DT = new Vector();
	Vector VFORWARD_REPORT_DT = new Vector();
	Vector VSPOT_REPORT_DT = new Vector();
	Vector VCURVE_DT = new Vector();
	Vector VCOMMODITY_TYPE = new Vector();
	Vector VCURVE_UNIT = new Vector();
	Vector VPHYS_FIN = new Vector();
	Vector VSETTLE_PRICE = new Vector();
	Vector VCURVE_TYPE = new Vector();
	Vector VTEMP_CURVE_TYPE = new Vector();
	Vector VENT_BY = new Vector();
	Vector VENT_DT = new Vector();
	Vector VPHYS_FIN_MST = new Vector();
	
	Vector VMAX_CURVE_DT = new Vector();
	
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	
	Vector VSELL_COUNTERPARTY_CD =new Vector();
	Vector VSELL_COUNTERPARTY_NM = new Vector();
	Vector VSELL_COUNTERPARTY_ABBR = new Vector();
	Vector VSELL_CONT_NO = new Vector();
	Vector VSELL_CONT_REV_NO = new Vector();
	Vector VSELL_START_DT = new Vector();
	Vector VSELL_END_DT = new Vector();
	Vector VSELL_TCQ = new Vector();
	Vector VSELL_DCQ = new Vector();
	Vector VSELL_DDA_TIME = new Vector();
	Vector VSELL_DDA_DT = new Vector();
	Vector VSELL_SIGNING_DT = new Vector();
	Vector VSELL_ENT_DT = new Vector();
	Vector VSELL_ENT_BY = new Vector();
	Vector VSELL_DIS_CONT_MAPPING = new Vector();
	
	Vector VSELL_INDEX = new Vector();
	Vector VSELL_TEMP_SEGMENT_TYPE = new Vector();
	Vector VSELL_SEGMENT = new Vector();
	Vector VSELL_SEGMENT_TYPE = new Vector();
	Vector VSELL_TEMP_SEGMENT = new Vector();
	Vector VSELL_QTY_UNIT = new Vector();

	Vector VPURCHASE_COUNTERPARTY_CD = new Vector();
	Vector VPURCHASE_COUNTERPARTY_NM = new Vector();
	Vector VPURCHASE_COUNTERPARTY_ABBR = new Vector();
	Vector VPURCHASE_CONT_NO = new Vector();
	Vector VPURCHASE_CONTRACT_TYPE = new Vector();
	Vector VPURCHASE_CONT_REV_NO = new Vector();
	Vector VPURCHASE_CONT_NAME = new Vector();
	Vector VPURCHASE_START_DT = new Vector();
	Vector VPURCHASE_TCQ = new Vector();
	Vector VPURCHASE_DCQ = new Vector();
	Vector VPURCHASE_END_DT = new Vector();
	Vector VPURCHASE_DDA_DT = new Vector();
	Vector VPURCHASE_DDA_TIME = new Vector();
	Vector VPURCHASE_ENT_BY= new Vector();
	Vector VPURCHASE_ENT_DT = new Vector();
	Vector VPURCHASE_DISPLAY_SEGMENT = new Vector();
	Vector VPURCHASE_TEMP_SEGMENT_TYPE = new Vector();
	Vector VPURCHASE_SEGMENT_TYPE = new Vector();
	Vector VPURCHASE_DISPLAY_SEGMENT_TYP = new Vector();
	Vector VPURCHASE_INDEX = new Vector();
	Vector VPURCHASE_QTY_UNIT = new Vector();
	
	Vector VPURCHASE_LATE_DEAL_FLAG = new Vector();
	Vector VSELL_LATE_DEAL_FLAG = new Vector();
	
	Vector VYEAR = new Vector();
	Vector VTEMP_YEAR = new Vector();
	Vector VINDEX = new Vector();
	Vector VSUB_INDEX = new Vector();
	
	Vector VFILE_NM = new Vector();
	Vector VFILE_SIZE = new Vector();
	Vector VFILE_UP_BY = new Vector();
	Vector VFILE_UP_ON = new Vector();

	Vector VDEAL_DETAILS = new Vector();
	Vector VDEAL_NAME = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VLAST_UPDATE = new Vector();
	Vector VLAST_UPDATE_BY = new Vector();
	Vector VDEAL_STATUS = new Vector();
	
	Vector VSETTLE_PRICE_AVG = new Vector(); //JAYASRI20230922
	Vector VACTUAL_CURVE_TYPE = new Vector(); //JAYASRI20230925
	Vector VPRICE_CURVE_TYPE = new Vector(); //JAYASRI20230925
	
	Vector VMST_COUNTERPARTY_CD = new Vector();
	Vector VMST_COUNTERPARTY_NM = new Vector();
	Vector VMST_COUNTERPARTY_ABBR = new Vector();
	
	public Vector getVCONT_MONTH(){return VCONT_MONTH;}
	public Vector getVCURVE_NM(){return VCURVE_NM;}
	public Vector getVSETTLE_START_DT(){return VSETTLE_START_DT;}
	public Vector getVSETTLE_END_DT(){return VSETTLE_END_DT;}
	public Vector getVSETTLE_DT() {return VSETTLE_DT;}
	public Vector getVSETTLE_CURVE(){return VSETTLE_CURVE;}
	public void setVSETTLE_CURVE(Vector vSETTLE_CURVE){VSETTLE_CURVE = vSETTLE_CURVE;}
	public Vector getVINDEX(){return VINDEX;}
	public Vector getVSUB_INDEX(){return VSUB_INDEX;}
	public Vector getVSETTLE_TYPE(){return VSETTLE_TYPE;}
	public void setVSETTLE_TYPE(Vector vSETTLE_TYPE){VSETTLE_CURVE = vSETTLE_TYPE;}
	public Vector getVHOLIDAY_NM(){return VHOLIDAY_NM;}
	public Vector getVHOLIDAY_DT(){return VHOLIDAY_DT;}
	public Vector getVHOLIDAY_STATUS(){return VHOLIDAY_STATUS;}
	public Vector getVHOLIDAY_UP(){return VHOLIDAY_UP;}
	public Vector getVYEAR(){return VYEAR;}
	public void setVYEAR(Vector vYear){VYEAR = vYear;}
	public Vector getVTEMP_YEAR(){return VTEMP_YEAR;}
	public void setVTEMP_YEAR(Vector vTEMP_YEAR){VTEMP_YEAR = vTEMP_YEAR;}
	public Vector getVSPOT_CURVE_TYPE(){return VSPOT_CURVE_TYPE;}
	public void setVSPOT_CURVE_TYPE(Vector vSPOT_CURVE_TYPE) {VSPOT_CURVE_TYPE = vSPOT_CURVE_TYPE;}
	public Vector getVCURVE_TYPE() {return VCURVE_TYPE;}
	public void setVCURVE_TYPE(Vector vCURVE_TYPE) {VCURVE_TYPE = vCURVE_TYPE;}
	public Vector getVREPORT_DT() {return VREPORT_DT;}
	public Vector getVCURVE_DT() {return VCURVE_DT;}
	public Vector getVCOMMODITY_TYPE() {return VCOMMODITY_TYPE;}
	public Vector getVCURVE_UNIT() {return VCURVE_UNIT;}
	public Vector getVPHYS_FIN() {return VPHYS_FIN;}
	public Vector getVPHYS_FIN_MST() {return VPHYS_FIN_MST;}
	public Vector getVSETTLE_PRICE() {return VSETTLE_PRICE;}
	public Vector getVTEMP_CURVE_TYPE() {return VTEMP_CURVE_TYPE;}
	public void setVTEMP_CURVE_TYPE(Vector vTEMP_CURVE_TYPE) {VTEMP_CURVE_TYPE = vTEMP_CURVE_TYPE;}
	public Vector getVENT_BY() {return VENT_BY;}
	public Vector getVENT_DT() {return VENT_DT;}
	public Vector getVSPOT_REPORT_DATE() {return VSPOT_REPORT_DATE;}
	public void setVSPOT_REPORT_DATE(Vector vSPOT_REPORT_DATE) {VSPOT_REPORT_DATE = vSPOT_REPORT_DATE;}
	public String getFileNM(){return fileNM;}
	public void setFileNM(String fileNM) {this.fileNM = fileNM;}
	public String getFileSIZE() {return fileSIZE;}
	public void setFileSIZE(String fileSIZE) {this.fileSIZE = fileSIZE;	}
	public Vector getVFILE_NM() {return VFILE_NM;}
	public Vector getVFILE_SIZE() {return VFILE_SIZE;}
	public String getReport_dt(){return report_dt;}
	public void setReport_dt(String report_dt) {this.report_dt = report_dt;}
	public String getEmp_cd(){return emp_cd;}
	public void setEmp_cd(String emp_cd) {this.emp_cd = emp_cd;}
	public Vector getVFILE_UP_BY() {return VFILE_UP_BY;}
	public Vector getVFILE_UP_ON() {return VFILE_UP_ON;}
	public Vector getVFORWARD_REPORT_DT() {return VFORWARD_REPORT_DT;}
	public Vector getVSPOT_REPORT_DT() {return VSPOT_REPORT_DT;}
	public Vector getVMAX_CURVE_DT() {return VMAX_CURVE_DT;}
	public String getDelete_report_Dt() {return delete_report_Dt;}
	public void setDelete_report_Dt(String delete_report_Dt) {this.delete_report_Dt = delete_report_Dt;}
	public String getUpload_report_dt() {return upload_report_dt;}
	public void setUpload_report_dt(String upload_report_dt) {this.upload_report_dt = upload_report_dt;}
	public Vector<String> getVFORWARD_REPORT_DATE() {return VFORWARD_REPORT_DATE;}
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVSELL_TCQ(){return VSELL_TCQ;}
	public Vector getVSELL_DCQ(){return VSELL_DCQ;}
	public Vector getVSELL_DDA_DT(){return VSELL_DDA_DT;}
	public Vector getVSELL_DDA_TIME(){return VSELL_DDA_TIME;}
	public Vector getVSELL_COUNTERPARTY_CD() {return VSELL_COUNTERPARTY_CD;}
	public Vector getVSELL_COUNTERPARTY_NM() {return VSELL_COUNTERPARTY_NM;}
	public Vector getVSELL_COUNTERPARTY_ABBR() {return VSELL_COUNTERPARTY_ABBR;}
	public Vector getVSELL_CONT_NO() {return VSELL_CONT_NO;}
	public Vector getVSELL_CONT_REV_NO() {return VSELL_CONT_REV_NO;}
	public Vector getVSELL_START_DT() {return VSELL_START_DT;}
	public Vector getVSELL_END_DT() {return VSELL_END_DT;}
	public Vector getVSELL_SIGNING_DT() {return VSELL_SIGNING_DT;}
	public Vector getVSELL_ENT_DT() {return VSELL_ENT_DT;}
	public Vector getVSELL_ENT_BY() {return VSELL_ENT_BY;}
	public Vector getVSELL_DIS_CONT_MAPPING() {return VSELL_DIS_CONT_MAPPING;}

	public Vector getVSELL_INDEX() {return VSELL_INDEX;}
	public Vector getVSELL_TEMP_SEGMENT_TYPE() {return VSELL_TEMP_SEGMENT_TYPE;}
	public Vector getVSELL_SEGMENT() {return VSELL_SEGMENT;}
	public Vector getVSELL_SEGMENT_TYPE() {	return VSELL_SEGMENT_TYPE;}
	public Vector getVSELL_TEMP_SEGMENT() {	return VSELL_TEMP_SEGMENT;}
	public Vector getVSELL_QTY_UNIT() {	return VSELL_QTY_UNIT;}

	public Vector getVPURCHASE_COUNTERPARTY_CD() {return VPURCHASE_COUNTERPARTY_CD;}
	public Vector getVPURCHASE_COUNTERPARTY_NM() {return VPURCHASE_COUNTERPARTY_NM;}
	public Vector getVPURCHASE_COUNTERPARTY_ABBR() {return VPURCHASE_COUNTERPARTY_ABBR;}
	public Vector getVPURCHASE_CONT_NO() {return VPURCHASE_CONT_NO;}
	public Vector getVPURCHASE_CONT_REV_NO() {return VPURCHASE_CONT_REV_NO;}
	public Vector getVPURCHASE_CONT_NAME() {return VPURCHASE_CONT_NAME;}
	public Vector getVPURCHASE_START_DT() {return VPURCHASE_START_DT;}
	public Vector getVPURCHASE_END_DT() {return VPURCHASE_END_DT;}
	public Vector getVPURCHASE_DISPLAY_SEGMENT() {return VPURCHASE_DISPLAY_SEGMENT;}
	public Vector getVPURCHASE_TEMP_SEGMENT_TYPE() {return VPURCHASE_TEMP_SEGMENT_TYPE;	}
	public Vector getVPURCHASE_SEGMENT_TYPE() {	return VPURCHASE_SEGMENT_TYPE;}
	public Vector getVPURCHASE_DISPLAY_SEGMENT_TYP() {return VPURCHASE_DISPLAY_SEGMENT_TYP;}
	public Vector getVPURCHASE_INDEX() {return VPURCHASE_INDEX;	}
	public Vector getVPURCHASE_QTY_UNIT() {return VPURCHASE_QTY_UNIT;	}
	public Vector getVPURCHASE_CONTRACT_TYPE() {return VPURCHASE_CONTRACT_TYPE;	}
	public Vector getVPURCHASE_TCQ() {return VPURCHASE_TCQ;}
	public Vector getVPURCHASE_DCQ() {return VPURCHASE_DCQ;}
	public Vector getVPURCHASE_DDA_DT() {return VPURCHASE_DDA_DT;}
	public Vector getVPURCHASE_DDA_TIME() {return VPURCHASE_DDA_TIME;}
	public Vector getVPURCHASE_ENT_BY() {return VPURCHASE_ENT_BY;}
	public Vector getVPURCHASE_ENT_DT() {return VPURCHASE_ENT_DT;}
	public Vector getVPURCHASE_LATE_DEAL_FLAG() {return VPURCHASE_LATE_DEAL_FLAG;}
	public Vector getVSELL_LATE_DEAL_FLAG() {return VSELL_LATE_DEAL_FLAG;}
	public Vector getVDEAL_DETAILS() {return VDEAL_DETAILS;}
	public Vector getVLAST_UPDATE() {return VLAST_UPDATE;}
	public Vector getVLAST_UPDATE_BY() {return VLAST_UPDATE_BY;}
	public Vector getVDEAL_NAME() {return VDEAL_NAME;}
	public Vector getVCONT_NO() {return VCONT_NO;}
	public Vector getVDEAL_STATUS() {return VDEAL_STATUS;}
	
	public Vector getVSETTLE_PRICE_AVG() {return VSETTLE_PRICE_AVG;} //JAYASRI20230922
	public Vector getVACTUAL_CURVE_TYPE() {return VACTUAL_CURVE_TYPE;} //JAYASRI20230925
	public Vector getVPRICE_CURVE_TYPE() {return VPRICE_CURVE_TYPE;} //JAYASRI20230925
	
	public Vector getVMST_COUNTERPARTY_CD() {return VMST_COUNTERPARTY_CD;}
	public Vector getVMST_COUNTERPARTY_NM() {return VMST_COUNTERPARTY_NM;}
	public Vector getVMST_COUNTERPARTY_ABBR() {return VMST_COUNTERPARTY_ABBR;}
}
