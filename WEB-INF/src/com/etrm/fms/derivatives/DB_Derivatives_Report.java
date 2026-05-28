package com.etrm.fms.derivatives;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.etrm.fms.util.DB_AllocationUtil;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.TaxCalculator;
import com.etrm.fms.util.UtilBean;

public class DB_Derivatives_Report 
{
	String db_src_file_name = "DB_Derivatives_Report.java";

	Connection conn;
	PreparedStatement stmt;
	PreparedStatement stmt0;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt5;
	PreparedStatement stmt6;
	PreparedStatement stmt7;
	PreparedStatement stmt_temp;
	PreparedStatement stmt_temp1;
	ResultSet rset;
	ResultSet rset0;
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
	ResultSet rset6;
	ResultSet rset7;
	ResultSet rset_temp;
	ResultSet rset_temp1;

	UtilBean utilBean = new UtilBean();
	DateUtil dateUtil = new DateUtil();
	DB_AllocationUtil utilAlloc = new DB_AllocationUtil();
	TaxCalculator TaxCalc = new TaxCalculator(); 

	NumberFormat nf0 = new DecimalFormat("###########");
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");

	public void init() 
	{
		String function_nm = "init()";
		try 
		{
			Context initContext = new InitialContext();
			if (initContext == null) 
			{
				throw new Exception("Boom - No Context");
			}

			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup(RuntimeConf.security_database);
			if (ds != null) 
			{
				conn = ds.getConnection();
				if (conn != null) 
				{

					if (callFlag.equalsIgnoreCase("MONTHLY_HEDGE_REPORT")) 
					{
						comp_name=""+utilBean.getCompanyName(conn, comp_cd);
						if(comp_cd.equals("2"))
						{
							comp_cin_no="U40100GJ2000PTC038780"; //SEIPL
						}
						else
						{
							comp_cin_no="U11200TN2012PTC124501"; //SEMTIPL
						}
						getCompanyOwnerAdddrDtlInfo();
						getCompanyOwnerAdddrDtl();
						getmonthlyHedgeRptDtl();
					}
					else if(callFlag.equalsIgnoreCase("DERV_CONTRACT_SUMMARY"))
	    			{
	    				getDervCounterpartyList();
	    				getDervContractSummary();
	    			}
				}

				conn.close();
				conn = null;
			}
		} 
		catch (Exception e) 
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
	    	if(rset7 != null){try{rset7.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_temp != null){try{rset_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_temp1 != null){try{rset_temp1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt0 != null){try{stmt0.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt6 != null){try{stmt6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt7 != null){try{stmt7.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp != null){try{stmt_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp1 != null){try{stmt_temp1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		}
	}
	
	public void getDervContractSummary()
	{
		String function_nm="getDervContractSummary()";

		try
		{
			int index=0;
			String due_date = "";
			String due_date_in = "";
  			String exchng_rate_cd ="";
  			String exchng_rate_nm ="";
  			double total_qty = 0;
  			
			String queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_TYPE,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,A.CONT_NO,A.CONT_REV,A.CONT_NAME,A.CONT_REF_NO,"
					+ "B.INSTRUMENT_NO,B.INSTRUMENT_TYPE,B.BUY_SELL,B.STATUS,B.QTY,B.QTY_UNIT,B.RATE,B.RATE_UNIT,"
					+ "B.PRODUCT_NM,B.CURVE_NM,B.PROJ_METHOD,TO_CHAR(B.CONT_DD_MM_YR,'DD/MM/YYYY'),"
					+ "TO_CHAR(B.PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_END_DT,'DD/MM/YYYY'),B.CONV_FACTOR,TO_CHAR(A.TRADE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(A.SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(B.ENT_DT,'DD/MM/YYYY'),B.ENT_BY "
					+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B "
					+ "WHERE A.COMPANY_CD=? AND B.PRICE_START_DT<=TO_DATE(?,'DD/MM/YYYY') AND B.PRICE_END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				if(!counterparty_cd.equals("0"))
				{
					queryString+= "AND A.COUNTERPARTY_CD=? ";
				}
				queryString+= "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, to_dt);
			stmt.setString(3, from_dt);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(4, counterparty_cd);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				index+=1;
				String companyCd = rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd = rset.getString(2)==null?"":rset.getString(2);
				String agmt_type = rset.getString(3)==null?"":rset.getString(3);
				String agmt = rset.getString(4)==null?"":rset.getString(4);
				String agmt_rev = rset.getString(5)==null?"":rset.getString(5);
				String cont_type = rset.getString(6)==null?"":rset.getString(6);
				String cont = rset.getString(7)==null?"":rset.getString(7);
				String cont_rev = rset.getString(8)==null?"":rset.getString(8);
				String cont_nm = rset.getString(9)==null?"":rset.getString(9);
				String cont_ref = rset.getString(10)==null?"":rset.getString(10);
				String instrument_no = rset.getString(11)==null?"":rset.getString(11);
				String instrument_type = rset.getString(12)==null?"":rset.getString(12);
				String buy_sell = rset.getString(13)==null?"":rset.getString(13);
				String cont_status_flg = rset.getString(14)==null?"":rset.getString(14);
				double qty = rset.getDouble(15);
				String qty_unit_cd = rset.getString(16)==null?"":rset.getString(16);
				String qty_unit = utilBean.getEnergyUnitNm(conn, qty_unit_cd);
				String rate = rset.getString(17)==null?"":rset.getString(17);
				String rate_unit = rset.getString(18)==null?"":rset.getString(18);
				String prod_nm = rset.getString(19)==null?"":rset.getString(19);
				String curve_nm = rset.getString(20)==null?"":rset.getString(20);
				String start_dt = rset.getString(23)==null?"":rset.getString(23);
				String end_dt = rset.getString(24)==null?"":rset.getString(24);
				String conversion_factor = rset.getString(25)==null?"":rset.getString(25);
				String trade_dt = rset.getString(26)==null?"":rset.getString(26);
				String signing_dt = rset.getString(27)==null?"":rset.getString(27);
				
				String temp_dt[]=signing_dt.split("/");
				
				String month_year=temp_dt[1]+"/"+temp_dt[2];
				
				if(cont_status_flg.equals("Y"))
				{
					VINSTRUMENT_STATUS.add("Confirmed");
				}
				else if(cont_status_flg.equals("N"))
				{
					VINSTRUMENT_STATUS.add("Not-Confirmed");
				}
				else if(cont_status_flg.equals("X"))
				{
					VINSTRUMENT_STATUS.add("Cancelled");
				}
				else
				{
					VINSTRUMENT_STATUS.add("");
				}
				
				VCOUNTERPARTY_CD.add(countpty_cd);
				VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				
				String dealNo=utilBean.NewDealMappingId(companyCd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, instrument_no);
				
				VDISP_DEAL_ID.add(dealNo);
				VTRADE_DT.add(trade_dt);
				VCONT_REF.add(cont_ref);
				VSIGNING_DT.add(signing_dt);
				VRATE.add(""+utilBean.RateNumberFormat(Double.parseDouble(rate), rate_unit));
				VRATE_UNIT.add(""+utilBean.getRateUnitNm(conn,rate_unit));
				VSTART_DT.add(start_dt);
				VEND_DT.add(end_dt);
				
				VQTY.add(nf.format(qty));
				VQTY_UNIT.add(qty_unit);
				VBUY_SELL.add(buy_sell);
				VCURVE_NM.add(curve_nm);
				VPROD_NM.add(prod_nm);
				VCONV_FACTOR.add(conversion_factor);
				VMONTH_YEAR.add(month_year);
				
				total_qty += qty;
				
				String ent_dt=rset.getString(28)==null?"":rset.getString(28);
				String ent_by=rset.getString(29)==null?"":rset.getString(29);
				VENT_DT.add(ent_dt);
				VENT_BY.add(""+utilBean.getEmpName(conn,ent_by));
				
				VPRICE_TYPE.add("Fixed");
				
	  			String BuUnit="";
			  	String queryString1 = "SELECT COMPANY_CD,PLANT_SEQ_NO "
			  			+ "FROM FMS_DERV_CONT_BU "
			  			+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
			  			+ "AND CONT_NO=? AND CONT_REV=? "
			  			+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=?";
			  	stmt0 = conn.prepareStatement(queryString1);
				stmt0.setString(1, companyCd);
				stmt0.setString(2, agmt);
				stmt0.setString(3, agmt_rev);
				stmt0.setString(4, cont);
				stmt0.setString(5, cont_rev);
				stmt0.setString(6, countpty_cd);
				stmt0.setString(7, cont_type);
				rset0=stmt0.executeQuery();
	  			while(rset0.next())
	  			{
	  				String ownercd = rset0.getString(1)==null?"0":rset0.getString(1);
	  				String plant_seq = rset0.getString(2)==null?"0":rset0.getString(2);
	  				if(BuUnit.equals(""))
	  				{
	  					BuUnit+=""+utilBean.getCounterpartyPlantABBR(conn,ownercd, companyCd, plant_seq,"B");
	  				}
	  				else
	  				{
	  					BuUnit+=","+utilBean.getCounterpartyPlantABBR(conn,ownercd, companyCd, plant_seq, "B");
	  				}
	  			}
	  			VBU_PLANT.add(BuUnit);
	  			rset0.close();
	  			stmt0.close();
	  			
	  			String plantUnits="";
	  			String holiday_state="";
			  	queryString1 = "SELECT COUNTERPARTY_CD,PLANT_SEQ_NO "
			  			+ "FROM FMS_DERV_CONT_PLANT "
			  			+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
			  			+ "AND CONT_NO=? AND CONT_REV=? "
			  			+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=?";
			  	stmt2 = conn.prepareStatement(queryString1);
				stmt2.setString(1, companyCd);
				stmt2.setString(2, agmt);
				stmt2.setString(3, agmt_rev);
				stmt2.setString(4, cont);
				stmt2.setString(5, cont_rev);
				stmt2.setString(6, countpty_cd);
				stmt2.setString(7, cont_type);
				rset2=stmt2.executeQuery();
	  			while(rset2.next())
	  			{
	  				String counterpty_cd = rset2.getString(1)==null?"0":rset2.getString(1);
	  				String plant_seq = rset2.getString(2)==null?"0":rset2.getString(2);
	  				
	  				if(plantUnits.equals(""))
	  				{
	  					plantUnits+=""+utilBean.getCounterpartyPlantABBR(conn,counterpty_cd, companyCd, plant_seq, "T");
	  				}
	  				else
	  				{
	  					plantUnits+=","+utilBean.getCounterpartyPlantABBR(conn,counterpty_cd, companyCd, plant_seq, "T");
	  				}
	  				
	  				
	  				String queryString4="SELECT HOLIDAY_STATE "
							+ "FROM FMS_DERV_CONT_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? "
							+ "AND CONT_NO=? "
							+ "AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=?";
		  			stmt4 = conn.prepareStatement(queryString4);
					stmt4.setString(1, companyCd);
					stmt4.setString(2, countpty_cd);
					stmt4.setString(3, agmt);
					stmt4.setString(4, cont);
					stmt4.setString(5, cont_type);
					stmt4.setString(6, plant_seq);
					rset4=stmt4.executeQuery();
					if(rset4.next())
					{
						String tmp_holiday_state=rset4.getString(1)==null?"":rset4.getString(1);
						
						String temp_holiday_st[]=tmp_holiday_state.split("@");
						
						for(int i=0; i<temp_holiday_st.length; i++)
						{
							String holiday_st_nm=utilBean.getStateName(conn, temp_holiday_st[i]);
							if(holiday_state.equals(""))
							{
								holiday_state=holiday_st_nm;
							}
							else
							{
								holiday_state=holiday_state+","+holiday_st_nm;
							}
						}
					}
					rset4.close();
					stmt4.close();
	  			}
	  			VPLANT_UNIT.add(plantUnits);
	  			VHOLIDAY_STATE.add(holiday_state);
	  			rset2.close();
	  			stmt2.close();
	  			
	  			String queryString4="SELECT DUE_DATE,EXCHNG_RATE_CD,DUE_DT_IN "
						+ "FROM FMS_DERV_CONT_BILLING_DTL A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? "
						+ "AND CONT_NO=? "
						+ "AND CONTRACT_TYPE=? ";
	  			stmt4 = conn.prepareStatement(queryString4);
				stmt4.setString(1, companyCd);
				stmt4.setString(2, countpty_cd);
				stmt4.setString(3, agmt);
				stmt4.setString(4, cont);
				stmt4.setString(5, cont_type);
				rset4=stmt4.executeQuery();
				if(rset4.next())
				{
					due_date=rset4.getString(1)==null?"":rset4.getString(1);
					exchng_rate_cd=rset4.getString(2)==null?"":rset4.getString(2);
					due_date_in=rset4.getString(3)==null?"":rset4.getString(3);
					
					if(!exchng_rate_cd.equals("0"))
					{
						queryString = "SELECT EXC_RATE_NM "
								+ "FROM FMS_EXCHG_RATE_MST "
								+ "WHERE EXC_RATE_CD=? ";
						stmt_temp1 = conn.prepareStatement(queryString);
						stmt_temp1.setString(1, exchng_rate_cd);
						rset_temp1=stmt_temp1.executeQuery();
						if(rset_temp1.next())
						{
							exchng_rate_nm =rset_temp1.getString(1)==null?"":rset_temp1.getString(1);
						}
						rset_temp1.close();
						stmt_temp1.close();
					}
				}
				VDUE_DATE.add(due_date);
				VDUE_DATE_IN.add(due_date_in);
				VEXCHANGE_RATE.add(exchng_rate_cd);
				VEXCHNG_RATE_NM.add(exchng_rate_nm);
				rset4.close();
				stmt4.close();
			}
			totalQty=nf.format(total_qty);
			VINDEX.add(index);
			
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getDervCounterpartyList()
	{
		String function_nm="getDervCounterpartyList()";

		try
		{
			String queryString="SELECT DISTINCT(A.COUNTERPARTY_CD) "
					+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B "
					+ "WHERE A.COMPANY_CD=? "
					+ "AND B.PRICE_START_DT<=TO_DATE(?,'DD/MM/YYYY') AND B.PRICE_END_DT>=TO_DATE(?,'DD/MM/YYYY') AND B.STATUS='Y' "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, to_dt);
			stmt.setString(3, from_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String counterpty_cd=rset.getString(1)==null?"":rset.getString(1);
				VMST_COUNTERPARTY_CD.add(counterpty_cd);
				VMST_COUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn, counterpty_cd));
				VMST_COUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn, counterpty_cd));
			}
			rset.close();
			stmt.close();		
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	private void getmonthlyHedgeRptDtl() 
	{
		String function_nm = "getmonthlyHedgeRptDtl()";
		try 
		{
			String min_year="";
			String max_year="";
			String queryString = "SELECT TO_CHAR(MIN(TRADE_DT),'YYYY'), TO_CHAR(MAX(TRADE_DT),'YYYY') FROM FMS_DERV_CONT_MST ";
			stmt=conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			if(rset.next()) 
			{
				min_year=rset.getString(1)==null?"":rset.getString(1);
				max_year=rset.getString(2)==null?"":rset.getString(2);
			}
			rset.close();
			stmt.close();
			avail_deal_years=""+min_year;
			
			String month_start_dt="01/"+month+"/"+year;
			String month_end_dt=dateUtil.getLastDateOfMonth(month, year);
			
			String prev_month_start_dt=dateUtil.getFirstDateOfPreviousMonth(month_start_dt);
			String prev_month_end_dt=dateUtil.getLastDateOfMonth(prev_month_start_dt);
			
			double totqtyMonth=0; 
			double totqtyMonthSell=0;
			double totqtyMonthSettle=0;
			
			String queryString1="SELECT * FROM (SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_TYPE,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,A.CONT_NO,A.CONT_REV,A.CONT_NAME,A.CONT_REF_NO,"
					+ "B.INSTRUMENT_NO,B.INSTRUMENT_TYPE,B.BUY_SELL,B.STATUS,B.QTY,B.QTY_UNIT,B.RATE,B.RATE_UNIT,"
					+ "B.PRODUCT_NM,B.CURVE_NM,B.PROJ_METHOD,TO_CHAR(B.CONT_DD_MM_YR,'DD/MM/YYYY'),"
					+ "TO_CHAR(B.PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_END_DT,'DD/MM/YYYY'),B.CONV_FACTOR,TO_CHAR(A.TRADE_DT,'DD/MM/YYYY') AS TRADE_DT,'SETTLED' "
					+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B "
					+ "WHERE A.COMPANY_CD=? AND B.PRICE_END_DT BETWEEN TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(?,'DD/MM/YYYY') AND B.STATUS='Y' AND A.CONT_STATUS!='X' "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO ";
			 queryString1+="UNION ALL ";
			 queryString1+="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_TYPE,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,A.CONT_NO,A.CONT_REV,A.CONT_NAME,A.CONT_REF_NO,"
						+ "B.INSTRUMENT_NO,B.INSTRUMENT_TYPE,B.BUY_SELL,B.STATUS,B.QTY,B.QTY_UNIT,B.RATE,B.RATE_UNIT,"
						+ "B.PRODUCT_NM,B.CURVE_NM,B.PROJ_METHOD,TO_CHAR(B.CONT_DD_MM_YR,'DD/MM/YYYY'),"
						+ "TO_CHAR(B.PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_END_DT,'DD/MM/YYYY'),B.CONV_FACTOR,TO_CHAR(A.TRADE_DT,'DD/MM/YYYY') AS TRADE_DT,'NOT_SETTLED' "
						+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B "
						+ "WHERE A.COMPANY_CD=? AND A.TRADE_DT BETWEEN TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(?,'DD/MM/YYYY') "
						+ "AND B.PRICE_END_DT >= TO_DATE(?,'DD/MM/YYYY') AND B.STATUS='Y' AND A.CONT_STATUS!='X' "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO) "
						+ "ORDER BY TO_DATE(TRADE_DT, 'DD/MM/YYYY') ASC";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, month_start_dt);
			stmt1.setString(3, month_end_dt);
			stmt1.setString(4, comp_cd);
			stmt1.setString(5, month_start_dt);
			stmt1.setString(6, month_end_dt);
			stmt1.setString(7, month_start_dt);
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				String counterparty_cd=rset1.getString(2)==null?"":rset1.getString(2);
				String agmt_type=rset1.getString(3)==null?"":rset1.getString(3);
				String agmt_no=rset1.getString(4)==null?"":rset1.getString(4);
				String agmt_rev_no=rset1.getString(5)==null?"":rset1.getString(5);
				String contract_type=rset1.getString(6)==null?"":rset1.getString(6);
				String cont_no=rset1.getString(7)==null?"":rset1.getString(7);
				String cont_rev_no=rset1.getString(8)==null?"":rset1.getString(8);
				String cont_name=rset1.getString(9)==null?"":rset1.getString(9);
				String cont_ref_no=rset1.getString(10)==null?"":rset1.getString(10);
				String trade_dt=rset1.getString(26)==null?"":rset1.getString(26);	
				
				String instrument_no = rset1.getString(11)==null?"":rset1.getString(11);
				String instrument_type = rset1.getString(12)==null?"":rset1.getString(12);
				String buy_sell = rset1.getString(13)==null?"":rset1.getString(13);
				String instrument_status = rset1.getString(14)==null?"":rset1.getString(14);
				double instrument_volume = rset1.getDouble(15);
				String temp_instrument_volume_unit = rset1.getString(16)==null?"":rset1.getString(16);
				String instrument_volume_unit = utilBean.getEnergyUnitNm(conn, temp_instrument_volume_unit);
				
				//String instrument_rate = rset1.getString(7)==null?"":rset1.getString(7);
				String instrument_rate_unit = rset1.getString(18)==null?"":rset1.getString(18);
				
				double temp_instrument_rate = rset1.getDouble(17);
				String instrument_rate=""+utilBean.RateNumberFormat(temp_instrument_rate, instrument_rate_unit);
				
				String product_nm = rset1.getString(19)==null?"":rset1.getString(19);
				String curve_nm = rset1.getString(20)==null?"":rset1.getString(20);
				String proj_method = rset1.getString(21)==null?"":rset1.getString(21);
				String cont_dd_mm_yr = rset1.getString(22)==null?"":rset1.getString(22);
				
				String price_start_dt = rset1.getString(23)==null?"":rset1.getString(23);
				String price_end_dt = rset1.getString(24)==null?"":rset1.getString(24);

				double conversion_factor = rset1.getDouble(25);
				
				VCOUNTERPARTY_CD.add(counterparty_cd);
				VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,counterparty_cd));
				VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,counterparty_cd));
				VDISP_DEAL_ID.add(utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, ""));
				
				double qty_mmbtu=0;
				String qty_bbl="";
				if(instrument_volume_unit.equalsIgnoreCase("BBL") || instrument_volume_unit.equalsIgnoreCase("MT"))
				{
					qty_mmbtu=instrument_volume*conversion_factor;
					qty_bbl=nf0.format(instrument_volume);
				}
				else
				{
					qty_mmbtu=instrument_volume;
					qty_bbl="";
				}
				String settel_flg=rset1.getString(27)==null?"":rset1.getString(27);	
				
				if(settel_flg.equals("SETTLED"))
				{
					VQTY_SELL.add("");
					VQTY.add("");
					
					VQTY_BBL.add("");
					VQTYSELL_BBL.add("");
					
					VQTY_SETTLE.add(nf0.format(qty_mmbtu));
					VDEAL_RMK.add("Deal has been net settled");
				}
				else
				{
					if(buy_sell.equalsIgnoreCase("BUY"))
					{
						VQTY_SELL.add("");
						VQTY.add(nf0.format(qty_mmbtu));
						
						VQTY_BBL.add(qty_bbl);
						VQTYSELL_BBL.add("");
					}
					else
					{
						VQTY_SELL.add(nf0.format(qty_mmbtu));
						VQTY.add("");
					
						VQTY_BBL.add("");
						VQTYSELL_BBL.add(qty_bbl);
					}
					VQTY_SETTLE.add("");
					VDEAL_RMK.add("");
				}
				VTRADE_DT.add(trade_dt);
				VBUY_SELL.add(buy_sell);
				VPROD_NM.add(product_nm);
				VCURV_NM.add(curve_nm);
				VQTY_UNIT.add(instrument_volume_unit);
				VRATE.add(instrument_rate);
				VRATE_UNIT.add(instrument_rate_unit);
				VCONT_MONTH_YEAR.add(cont_dd_mm_yr);
				VPRICE_START_DT.add(price_start_dt);
				VPRICE_END_DT.add(price_end_dt);
				VPROJ_METHOD.add(proj_method);
				VCONT_REF.add(cont_ref_no);
			}
			rset1.close();
			stmt1.close();
			
			for(int i=0;i<VCOUNTERPARTY_CD.size();i++)
			{
				if(!VQTY_SELL.elementAt(i).equals(""))
				{
					totqtyMonthSell+=Double.parseDouble(""+VQTY_SELL.elementAt(i));
				}
				if(!VQTY.elementAt(i).equals(""))
				{
					totqtyMonth+=Double.parseDouble(""+VQTY.elementAt(i));
				}
				if(!VQTY_SETTLE.elementAt(i).equals(""))
				{
					totqtyMonthSettle+=Double.parseDouble(""+VQTY_SETTLE.elementAt(i));
				}
			}
			totalQty=nf0.format(totqtyMonth);
			totalQtySell=nf0.format(totqtyMonthSell);
			totalQtySettle=nf0.format(totqtyMonthSettle);
			
			Vector VQTYCURVENM_TEMP = new Vector();
			queryString = "SELECT DISTINCT(CURVE_NM) "
					+ "FROM FMS_DERV_INSTRUMENT_MST "
					+ "WHERE COMPANY_CD=? AND PRICE_END_DT BETWEEN TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(?,'DD/MM/YYYY') "
					+ "ORDER BY CURVE_NM ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, month_start_dt);
			stmt.setString(3, month_end_dt);
			rset=stmt.executeQuery();				
			while(rset.next())
			{
				VQTYCURVENM_TEMP.add(rset.getString(1)==null?"":rset.getString(1));
			}
			rset.close();
			stmt.close();
			
			String SrNo="";
			for(int i=0;i<VQTYCURVENM_TEMP.size();i++)
			{
				SrNo="";
				for(int j=0;j<VCURV_NM.size();j++)
				{
					if(VQTYCURVENM_TEMP.elementAt(i).equals(VCURV_NM.elementAt(j)))
					{
						if(!SrNo.equals("") && j>0)
							SrNo=SrNo+",";
						SrNo=SrNo+(j+1)+"";
					}
				}
				SrNo=SrNo+"; ";
				VQTYCURV_NM.add(VQTYCURVENM_TEMP.elementAt(i)+":"+SrNo);
			}
			 			
			double exposureEligibileLimit=0;
			double prevOutStanding=0;
			double out_standing=0;
			queryString = "SELECT BALANCE_QTY "
					+ "FROM FMS_DERV_HEDGE_EXPOSURE_DTL A "
					+ "WHERE COMPANY_CD=? AND HEDGE_DT=(SELECT MAX(HEDGE_DT) FROM FMS_DERV_HEDGE_EXPOSURE_DTL B "
					+ "WHERE HEDGE_DT<=TO_DATE(?,'DD/MM/YYYY') AND FLAG='L') ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, month_end_dt);
			rset=stmt.executeQuery();		
			if(rset.next())
			{
				exposureEligibileLimit=rset.getDouble(1);
			}
			rset.close();
			stmt.close();
			
			out_standing=totqtyMonth+totqtyMonthSell-totqtyMonthSettle;
			
			queryString1 = "SELECT BALANCE_QTY " +
					"FROM FMS_DERV_HEDGE_EXPOSURE_DTL " +
					"WHERE COMPANY_CD=? AND HEDGE_DT=TO_DATE(?,'DD/MM/YYYY') AND FLAG='Y' ";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, prev_month_end_dt);
			rset1=stmt1.executeQuery();					
			while(rset1.next())
			{
				prevOutStanding=rset1.getDouble(1);
			}
			rset1.close();
			stmt1.close();
			
			eligibleLimit=nf0.format(exposureEligibileLimit);
			totalPrevMonthBal=nf0.format(prevOutStanding);
			totalMonthOutstanding=nf0.format(prevOutStanding+out_standing);
			
			int count=0;
			double existingBalanceQty=0;
			String queryString3 = "SELECT COUNT(BALANCE_QTY) "
					+ "FROM FMS_DERV_HEDGE_EXPOSURE_DTL "
					+ "WHERE COMPANY_CD=? AND HEDGE_DT=TO_DATE(?,'DD/MM/YYYY') AND FLAG='Y' ";
			stmt3=conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, month_end_dt);
			rset3=stmt3.executeQuery();	
			if(rset3.next()) 
			{
				count=rset3.getInt(1);
			}
			else
			{
				count=0;
			}
			rset3.close();
			stmt3.close();
			
			String queryString4 = "SELECT BALANCE_QTY "
					+ "FROM FMS_DERV_HEDGE_EXPOSURE_DTL "
					+ "WHERE COMPANY_CD=? AND HEDGE_DT=TO_DATE(?,'DD/MM/YYYY') AND FLAG='Y' ";
			stmt4=conn.prepareStatement(queryString4);
			stmt4.setString(1, comp_cd);
			stmt4.setString(2, month_end_dt);
			rset4=stmt4.executeQuery();	
			if(rset4.next())
			{
				existingBalanceQty=rset4.getDouble(1);
			}
			else
			{
				existingBalanceQty=0;
			}
			rset4.close();
			stmt4.close();
			
			if(Double.doubleToRawLongBits(prevOutStanding+out_standing)!=Double.doubleToRawLongBits(0))
			{
				if(count==0) 
				{
					String query_temp="INSERT INTO FMS_DERV_HEDGE_EXPOSURE_DTL (COMPANY_CD,HEDGE_DT,BALANCE_QTY,ENT_BY,ENT_DT,FLAG) "
							+ "VALUES (?,TO_DATE(?,'DD/MM/YYYY'),?,?,SYSDATE,'Y')  ";
					stmt_temp=conn.prepareStatement(query_temp);
					stmt_temp.setString(1, comp_cd);
					stmt_temp.setString(2, month_end_dt);
					stmt_temp.setString(3, totalMonthOutstanding);
					stmt_temp.setString(4, emp_cd);
					stmt_temp.executeUpdate();
					stmt_temp.close();
					conn.commit();
				}
				else
				{
					if(Double.doubleToRawLongBits(existingBalanceQty)!=Double.doubleToRawLongBits(prevOutStanding+out_standing))
					{
						String query_temp="UPDATE FMS_DERV_HEDGE_EXPOSURE_DTL SET BALANCE_QTY=?,ENT_BY=?,ENT_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND HEDGE_DT=TO_DATE(?,'DD/MM/YYYY') AND FLAG='Y' ";
						stmt_temp=conn.prepareStatement(query_temp);
						stmt_temp.setString(1, totalMonthOutstanding);
						stmt_temp.setString(2, emp_cd);
						stmt_temp.setString(3, comp_cd);
						stmt_temp.setString(4, month_end_dt);
						stmt_temp.executeUpdate();
						stmt_temp.close();
						conn.commit();
					}
				}
			}
			
			if(Integer.parseInt(totalPrevMonthBal)<0)
			{
				totalPrevMonthBal="("+(-1)*Integer.parseInt(totalPrevMonthBal)+")";
			}
			if(Integer.parseInt(totalMonthOutstanding)<0)
			{
				totalMonthOutstanding="("+(-1)*Integer.parseInt(totalMonthOutstanding)+")";
			}
		}
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	private void getCompanyOwnerAdddrDtl() 
	{
		String function_nm = "getCompanyOwnerAdddrDtl()";
		try 
		{
			String addr="";
			String city="";
			String state="";
			String pin="";
			String country="";
			String phone="";
			String fax="";
			String queryString3="SELECT ADDR,CITY,STATE,PIN,COUNTRY,PHONE,FAX_1 "
					+ "FROM FMS_COMPANY_OWNER_ADDR_MST A "
					+ "WHERE COMPANY_CD=? AND ADDRESS_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_OWNER_ADDR_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.ADDRESS_TYPE=B.ADDRESS_TYPE "
					+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
			stmt3=conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, "R");
			rset3=stmt3.executeQuery();
			if(rset3.next())
			{
				addr  = rset3.getString(1)==null?"":rset3.getString(1);
				city  = rset3.getString(2)==null?"":rset3.getString(2);
				state  = rset3.getString(3)==null?"":rset3.getString(3);
				pin  = rset3.getString(4)==null?"":rset3.getString(4);
				country  = rset3.getString(5)==null?"":rset3.getString(5);
				phone  = rset3.getString(6)==null?"":rset3.getString(6);
				fax  = rset3.getString(7)==null?"":rset3.getString(7);
			}
			rset3.close();
			stmt3.close();
			
			comp_registered_addr=addr+","+city+","+state+","+country+" "+pin+"  Tel: +"+phone+"  Fax: +"+fax;
		}
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCompanyOwnerAdddrDtlInfo()
	{
		String function_nm="getCompanyOwnerAdddrDtlInfo()";
		try
		{
			String company_nm = ""+utilBean.getCompanyName(conn,comp_cd);
			view_info.put("company_nm", company_nm);
			String ownAddress="";
			String ownCity="";
			String ownState="";
			String ownPin="";
			String ownCountry="";
			String ownPhone="";
			String ownEmail="";
			
			String queryString3="SELECT ADDR,CITY,STATE,PIN,COUNTRY,PHONE,EMAIL "
					+ "FROM FMS_COMPANY_OWNER_ADDR_MST A "
					+ "WHERE COMPANY_CD=? AND ADDRESS_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_OWNER_ADDR_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.ADDRESS_TYPE=B.ADDRESS_TYPE "
					+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
			stmt3=conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, "C");
			rset3=stmt3.executeQuery();
			if(rset3.next())
			{
				ownAddress  = rset3.getString(1)==null?"":rset3.getString(1);
				ownCity  = rset3.getString(2)==null?"":rset3.getString(2);
				ownState  = rset3.getString(3)==null?"":rset3.getString(3);
				ownPin  = rset3.getString(4)==null?"":rset3.getString(4);
				ownCountry  = rset3.getString(5)==null?"":rset3.getString(5);
				ownPhone  = rset3.getString(6)==null?"":rset3.getString(6);
				ownEmail  = rset3.getString(7)==null?"":rset3.getString(7);
			}
			rset3.close();
			stmt3.close();
			
			view_info.put("ownAddress",ownAddress);
			view_info.put("ownCity",ownCity);
			view_info.put("ownState",ownState);
			view_info.put("ownPin",ownPin);
			view_info.put("ownCountry",ownCountry);
			view_info.put("ownPhone",ownPhone);
			view_info.put("ownEmail",ownEmail);
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	
	HashMap<String, String> view_info = new HashMap();
	public HashMap<String, String> getView_info() {return view_info;}
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}

	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	
	String year = "";
	String month = "";
	String emp_cd = "";
	String from_dt = "";
	String to_dt = "";
	String counterparty_cd = "";
	
	public void setYear(String year) {this.year = year;}
	public void setMonth(String month) {this.month = month;}
	public void setEmp_cd(String emp_cd) {this.emp_cd = emp_cd;}
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	
	String comp_name="";
	String comp_cin_no="";
	String comp_registered_addr="";
	String avail_deal_years="";
	String eligibleLimit="";
	String totalPrevMonthBal="";
	String totalQty="";
	String totalQtySell="";
	String totalQtySettle="";
	String totalMonthOutstanding="";
	
	public String getComp_name() {return comp_name;}
	public String getComp_cin_no() {return comp_cin_no;}
	public String getComp_registered_addr() {return comp_registered_addr;}
	public String getAvail_deal_years() {return avail_deal_years;}
	public String getEligibleLimit() {return eligibleLimit;}
	public String getTotalPrevMonthBal() {return totalPrevMonthBal;}
	public String getTotalQty() {return totalQty;}
	public String getTotalQtySell() {return totalQtySell;}
	public String getTotalQtySettle() {return totalQtySettle;}
	public String getTotalMonthOutstanding() {return totalMonthOutstanding;}
	
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VDISP_DEAL_ID = new Vector();
	Vector VQTY_SELL = new Vector();
	Vector VQTY = new Vector();
	Vector VQTY_BBL = new Vector();
	Vector VQTYSELL_BBL = new Vector();
	Vector VQTY_SETTLE = new Vector();
	Vector VDEAL_RMK = new Vector();
	Vector VTRADE_DT = new Vector();
	Vector VBUY_SELL = new Vector();
	Vector VPROD_NM = new Vector();
	Vector VCURV_NM = new Vector();
	Vector VQTY_UNIT = new Vector();
	Vector VRATE = new Vector();
	Vector VRATE_UNIT = new Vector();
	Vector VCONT_MONTH_YEAR = new Vector();
	Vector VPRICE_START_DT = new Vector();
	Vector VPRICE_END_DT = new Vector();
	Vector VPROJ_METHOD = new Vector();
	Vector VCONT_REF = new Vector();
	Vector VQTYCURV_NM = new Vector();
	Vector VMST_COUNTERPARTY_CD = new Vector();
	Vector VMST_COUNTERPARTY_NM = new Vector();
	Vector VMST_COUNTERPARTY_ABBR = new Vector();
	Vector VSIGNING_DT = new Vector();
	Vector VSTART_DT = new Vector();
	Vector VEND_DT = new Vector();
	Vector VPRICE_TYPE = new Vector();
	Vector VENT_BY = new Vector();
	Vector VENT_DT = new Vector();
	Vector VPLANT_UNIT = new Vector();
	Vector VBU_PLANT = new Vector();
	Vector VTOTAL_QTY = new Vector();
	Vector VINDEX = new Vector();
	Vector VDUE_DATE = new Vector();
	Vector VEXCHANGE_RATE = new Vector();
	Vector VEXCHNG_RATE_NM = new Vector();
	Vector VCURVE_NM = new Vector();
	Vector VCONV_FACTOR = new Vector();
	Vector VDUE_DATE_IN = new Vector();
	Vector VMONTH_YEAR = new Vector();
	Vector VHOLIDAY_STATE = new Vector();
	Vector VINSTRUMENT_STATUS = new Vector();
	
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVDISP_DEAL_ID() {return VDISP_DEAL_ID;}
	public Vector getVQTY_SELL() {return VQTY_SELL;}
	public Vector getVQTY() {return VQTY;}
	public Vector getVQTY_BBL() {return VQTY_BBL;}
	public Vector getVQTYSELL_BBL() {return VQTYSELL_BBL;}
	public Vector getVQTY_SETTLE() {return VQTY_SETTLE;}
	public Vector getVDEAL_RMK() {return VDEAL_RMK;}
	public Vector getVTRADE_DT() {return VTRADE_DT;}
	public Vector getVBUY_SELL() {return VBUY_SELL;}
	public Vector getVPROD_NM() {return VPROD_NM;}
	public Vector getVCURV_NM() {return VCURV_NM;}
	public Vector getVQTY_UNIT() {return VQTY_UNIT;}
	public Vector getVRATE() {return VRATE;}
	public Vector getVRATE_UNIT() {return VRATE_UNIT;}
	public Vector getVCONT_MONTH_YEAR() {return VCONT_MONTH_YEAR;}
	public Vector getVPRICE_START_DT() {return VPRICE_START_DT;}
	public Vector getVPRICE_END_DT() {return VPRICE_END_DT;}
	public Vector getVPROJ_METHOD() {return VPROJ_METHOD;}
	public Vector getVCONT_REF() {return VCONT_REF;}
	public Vector getVQTYCURV_NM() {return VQTYCURV_NM;}
	public Vector getVMST_COUNTERPARTY_CD() {return VMST_COUNTERPARTY_CD;}
	public Vector getVMST_COUNTERPARTY_NM() {return VMST_COUNTERPARTY_NM;}
	public Vector getVMST_COUNTERPARTY_ABBR() {return VMST_COUNTERPARTY_ABBR;}
	public Vector getVSIGNING_DT() {return VSIGNING_DT;}
	public Vector getVSTART_DT() {return VSTART_DT;}
	public Vector getVEND_DT() {return VEND_DT;}
	public Vector getVENT_DT() {return VENT_DT;}
	public Vector getVENT_BY() {return VENT_BY;}
	public Vector getVPRICE_TYPE() {return VPRICE_TYPE;}
	public Vector getVPLANT_UNIT() {return VPLANT_UNIT;}
	public Vector getVBU_PLANT() {return VBU_PLANT;}
	public Vector getVTOTAL_QTY() {return VTOTAL_QTY;}
	public Vector getVINDEX() {return VINDEX;}
	public Vector getVDUE_DATE() {return VDUE_DATE;}
	public Vector getVEXCHANGE_RATE() {return VEXCHANGE_RATE;}
	public Vector getVEXCHNG_RATE_NM() {return VEXCHNG_RATE_NM;}
	public Vector getVCURVE_NM() {return VCURVE_NM;}
	public Vector getVCONV_FACTOR() {return VCONV_FACTOR;}
	public Vector getVDUE_DATE_IN() {return VDUE_DATE_IN;}
	public Vector getVMONTH_YEAR() {return VMONTH_YEAR;}
	public Vector getVHOLIDAY_STATE() {return VHOLIDAY_STATE;}
	public Vector getVINSTRUMENT_STATUS() {return VINSTRUMENT_STATUS;}
}
