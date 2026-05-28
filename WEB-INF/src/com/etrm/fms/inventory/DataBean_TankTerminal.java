package com.etrm.fms.inventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Month;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

//Coded By          : Harsh Maheta
//Code Reviewed by	:
//CR Date			: 24/12/2024
//Status	  		: Developing

public class DataBean_TankTerminal
{
	String db_src_file_name="DataBean_TankTerminal.java";

	Connection conn;
	PreparedStatement stmt;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt_tmp;
	ResultSet rset;
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset_tmp;

	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();

	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");

	String color_unloaded="#ccffcc";
	String color_expected="#ff66d9";

	String is_parent_cargo_list="N";
	String is_tcq_modification="N";

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
		    		if(callFlag.equalsIgnoreCase("TANK_MST"))
		    		{
		    			getTankMaster();
		    			getTankInternalConsumptionDtl();
		    		}
		    		else if(callFlag.equalsIgnoreCase("TANK_INVENTORY_DTL"))
		    		{
		    			getTankInventoryDtl();
		    		}
		    		else if(callFlag.equalsIgnoreCase("INTERNAL_CONSUMPTION_DTL"))
		    		{
		    			getInternalConsumptionDtl();
		    		}
		    		else if(callFlag.equalsIgnoreCase("INV_STOCK_DTL"))
		    		{
		    			getInventoryStockDtl();
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
	    	if(rset1 != null){try{rset1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset2 != null){try{rset2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset3 != null){try{rset3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_tmp != null){try{rset_tmp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_tmp != null){try{stmt_tmp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}

	public void getTankMaster()
	{
		String function_nm="getTankMaster()";
		try
		{
			String queryString = "SELECT TANK_CD,TANK_NAME,TO_CHAR(EFF_DT,'DD/MM/YYYY'),STATUS,TANK_T1_VOLUME,TANK_T1_HEIGHT,"
					+ "TANK_T2_VOLUME,TANK_T2_HEIGHT,TANK_D1_VOLUME,TANK_D1_HEIGHT,TANK_D2_VOLUME,TANK_D2_HEIGHT,"
					+ "TANK_DIAMETER,TANK_PI_TAG "
					+ "FROM FMS_TANK_MST A "
					+ "WHERE COMPANY_CD=? AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_TANK_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.TANK_CD=B.TANK_CD)"
					+ "ORDER BY TANK_CD";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VTANK_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VTANK_NAME.add(rset.getString(2)==null?"":rset.getString(2));
				VEFF_DT.add(rset.getString(3)==null?"":rset.getString(3));
				VSTATUS.add(rset.getString(4)==null?"":rset.getString(4));
				VTANK_T1_VOLUME.add(rset.getString(5)==null?"":rset.getString(5));
				VTANK_T1_HEIGHT.add(rset.getString(6)==null?"":rset.getString(6));
				VTANK_T2_VOLUME.add(rset.getString(7)==null?"":rset.getString(7));
				VTANK_T2_HEIGHT.add(rset.getString(8)==null?"":rset.getString(8));
				VTANK_D1_VOLUME.add(rset.getString(9)==null?"":rset.getString(9));
				VTANK_D1_HEIGHT.add(rset.getString(10)==null?"":rset.getString(10));
				VTANK_D2_VOLUME.add(rset.getString(11)==null?"":rset.getString(11));
				VTANK_D2_HEIGHT.add(rset.getString(12)==null?"":rset.getString(12));
				VTANK_DIAMETER.add(rset.getString(13)==null?"":rset.getString(13));
				VTANK_PI_TAG.add(rset.getString(14)==null?"":rset.getString(14));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getTankInventoryDtl()
	{
		String function_nm="getTankInventoryDtl()";
		try
		{
			String queryString = "SELECT TANK_CD,TANK_NAME,STATUS "
					+ "FROM FMS_TANK_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY') "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_TANK_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.TANK_CD=B.TANK_CD) "
					+ "ORDER BY TANK_CD";
					//+ "AND STATUS=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_level_dt);
			//stmt.setString(3, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String tank_cd = rset.getString(1)==null?"":rset.getString(1);

				VTANK_CD.add(tank_cd);
				VTANK_NAME.add(rset.getString(2)==null?"":rset.getString(2));
				VSTATUS.add(rset.getString(3)==null?"":rset.getString(3));

				String queryString1 = "SELECT TANK_VOLUME,TANK_HEIGHT,TANK_MMSCM,TANK_CONV_FACTOR_1,TANK_CONV_FACTOR_2,TANK_MMBTU "
						+ "FROM FMS_TANK_INVENTORY_DTL A "
						+ "WHERE COMPANY_CD=? AND TANK_CD=? AND INV_LEVEL_DT = TO_DATE(?,'DD/MM/YYYY')";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, tank_cd);
				stmt1.setString(3, inv_level_dt);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					VTANK_VOLUME.add(rset1.getString(1)==null?"":rset1.getString(1));
					VTANK_HEIGHT.add(rset1.getString(2)==null?"":rset1.getString(2));
					VTANK_MMSCM.add(rset1.getString(3)==null?"":rset1.getString(3));
					String tank_conv_factor_1 = rset1.getString(4)==null?"":rset1.getString(4);
					String tank_conv_factor_2 = rset1.getString(5)==null?"":rset1.getString(5);

					VTANK_CONV_FACTOR_1.add(tank_conv_factor_1);
					VTANK_CONV_FACTOR_2.add(tank_conv_factor_2);
					VTANK_MMBTU.add(rset1.getString(6)==null?"":rset1.getString(6));
				}
				else
				{
					VTANK_VOLUME.add("");
					VTANK_HEIGHT.add("");
					VTANK_MMSCM.add("");

					String queryString2 = "SELECT TANK_CONV_FACTOR_1,TANK_CONV_FACTOR_2 "
							+ "FROM FMS_TANK_INVENTORY_DTL A "
							+ "WHERE COMPANY_CD=? AND TANK_CD=? "
							+ "AND A.INV_LEVEL_DT = (SELECT MAX(B.INV_LEVEL_DT) FROM FMS_TANK_INVENTORY_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.TANK_CD=B.TANK_CD) "
							+ "AND A.INV_LEVEL_DT <= TO_DATE(?,'DD/MM/YYYY')";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, tank_cd);
					stmt2.setString(3, inv_level_dt);
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						String max_tank_conv_factor_1 = rset2.getString(1)==null?"":rset2.getString(1);
						String max_tank_conv_factor_2 = rset2.getString(2)==null?"":rset2.getString(2);

						VTANK_CONV_FACTOR_1.add(max_tank_conv_factor_1);
						VTANK_CONV_FACTOR_2.add(max_tank_conv_factor_2);
					}
					else
					{
						VTANK_CONV_FACTOR_1.add("");
						VTANK_CONV_FACTOR_2.add("");
					}
					rset2.close();
					stmt2.close();

					VTANK_MMBTU.add("");
				}
				rset1.close();
				stmt1.close();
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getTankInternalConsumptionDtl()
	{
		String function_nm="getTankInternalConsumptionDtl()";
		try
		{
			String query = "SELECT PERCENTAGE,REMARK,TO_CHAR(EFF_DT,'DD/MM/YYYY') "
					+ "FROM FMS_TANK_CONSUMPTION_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "ORDER BY EFF_DT DESC";
					//+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_TANK_CONSUMPTION_MST B WHERE A.COMPANY_CD=B.COMPANY_CD) ";
			stmt=conn.prepareStatement(query);
			stmt.setString(1, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String int_cons_percentage = rset.getString(1)==null?"":rset.getString(1);
				String int_cons_remark = rset.getString(2)==null?"":rset.getString(2);
				String int_cons_eff_dt = rset.getString(3)==null?"":rset.getString(3);
				
				VICD_EFF_DT.add(int_cons_eff_dt);
				VICD_REMARK.add(int_cons_remark);
				VICD_PERCENTAGE.add(int_cons_percentage);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getTankMonthlyOpeningBalance()
	{
		String function_nm="getTankMonthlyOpeningBalance()";
		try
		{
			String prev_month = ""+(Integer.parseInt(opening_balance_month)-1);
			String prev_year = opening_balance_year;
			if(prev_month.equals("12"))
			{
				prev_year = ""+(Integer.parseInt(opening_balance_year)-1);
			}
			String last_month_date = utilDate.getLastDateOfMonth(prev_month, prev_year);

			String queryString1 = "SELECT SUM(TANK_VOLUME),SUM(TANK_MMSCM),TANK_CONV_FACTOR_1,TANK_CONV_FACTOR_2,SUM(TANK_MMBTU) "
					+ "FROM FMS_TANK_INVENTORY_DTL A "
					+ "WHERE COMPANY_CD=? AND INV_LEVEL_DT = TO_DATE(?,'DD/MM/YYYY') "
					+ "GROUP BY  TANK_CONV_FACTOR_1, TANK_CONV_FACTOR_2 ";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, last_month_date);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				omb_tank_volume = (rset1.getString(1)==null?"":rset1.getString(1));
				omb_tank_mmscm = (rset1.getString(2)==null?"":rset1.getString(2));
				omb_tank_conv_factor_1 = rset1.getString(3)==null?"":rset1.getString(3);
				omb_tank_conv_factor_2 = rset1.getString(4)==null?"":rset1.getString(4);
				omb_tank_mmbtu = (rset1.getString(5)==null?"":rset1.getString(5));
			}
			rset1.close();
			stmt1.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getInternalConsumptionDtl()
	{
		String function_nm="getInternalConsumptionDtl()";
		try
		{
			Vector VMONTH_LIST = new Vector();

			int monthNo=0;
			for (Month month : Month.values())
			{
				++monthNo;
				VMONTH_LIST.add(monthNo);
			}

			for(int i=0; i<VMONTH_LIST.size();i++)
			{
				String queryString = "SELECT LNG_WRITE_OFF,FLARING,AUXILARY_CONSUMPTION,SCV_FUEL_CONSUMPTION,SUG,OTHER_CONSUMPTION,MASS_BALANCING,TOTAL_CONSUMPTION "
						+ "FROM FMS_TANK_INTRNL_CONSUMPTION A "
						+ "WHERE COMPANY_CD=? AND YEAR = ? AND MONTH=?";
				stmt1=conn.prepareStatement(queryString);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, year);
				stmt1.setString(3, ""+VMONTH_LIST.elementAt(i));
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					VLNG_WRITE_OFF.add(rset1.getString(1)==null?"":rset1.getString(1));
					VFLARING.add(rset1.getString(2)==null?"":rset1.getString(2));
					VAUXILARY_CONSUMPTION.add(rset1.getString(3)==null?"":rset1.getString(3));
					VSCV_FUEL_CONSUMPTION.add(rset1.getString(4)==null?"":rset1.getString(4));
					VSUG.add(rset1.getString(5)==null?"":rset1.getString(5));
					VOTHER_CONSUMPTION.add(rset1.getString(6)==null?"":rset1.getString(6));
					VMASS_BALANCING.add(rset1.getString(7)==null?"":rset1.getString(7));
					VTOTAL_CONSUMPTION.add(rset1.getString(8)==null?"":rset1.getString(8));
				}
				else
				{
					VLNG_WRITE_OFF.add("");
					VFLARING.add("");
					VAUXILARY_CONSUMPTION.add("");
					VSCV_FUEL_CONSUMPTION.add("");
					VSUG.add("");
					VOTHER_CONSUMPTION.add("");
					VMASS_BALANCING.add("");
					VTOTAL_CONSUMPTION.add("");
				}
				rset1.close();
				stmt1.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	
	public void getInventoryStockDtl()
	{
		String function_nm = "getInventoryStockDtl()";
		try
		{
			
			String queryString="";
			int total_days=0;
			 queryString = "SELECT TRUNC(TO_DATE(?, 'DD/MM/YYYY')  - TO_DATE(?, 'DD/MM/YYYY'))+1 AS DAYS_DIFFERENCE FROM DUAL ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, to_dt);
			stmt.setString(2, from_dt);
			rset = stmt.executeQuery();
			if(rset.next())
			{
				 total_days = rset.getInt(1) == 0?0:rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			for(int i=0;i<total_days;i++)
			{
				String inv_dt="";
				queryString="SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+"+i+",'DD/MM/YYYY') FROM DUAL";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, from_dt);
				rset = stmt.executeQuery();
				if(rset.next())
				{
					inv_dt = rset.getString(1) == null?"":rset.getString(1);
					VEFF_DT.add(inv_dt);
				}
				rset.close();
				stmt.close();
				
				double T1Dead_Inventory_mmscm=0;   
				double T1Dead_Inventory_mmbtu=0;				
				double mmbtu=0;
				String queryString1 = "SELECT SUM(TANK_MMBTU) "
						+"FROM FMS_TANK_INVENTORY_DTL "
						+"WHERE TRUNC(INV_LEVEL_DT)=TO_DATE(?,'DD/MM/YYYY') AND COMPANY_CD = ?";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, inv_dt);
				stmt1.setString(2, comp_cd);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					mmbtu = rset1.getDouble(1);
					VTANK_VOLUME.add(nf.format(mmbtu));
				}
				rset1.close();
				stmt1.close();
				
				double conv_factor_1=0, conv_factor_2=0;
				queryString1 = "SELECT SUM(A.TANK_CONV_FACTOR_1),SUM(A.TANK_CONV_FACTOR_2) "
						+ "FROM FMS_TANK_INVENTORY_DTL A "
						+ "WHERE A.INV_LEVEL_DT = (SELECT MAX(B.INV_LEVEL_DT) "
						+ "FROM FMS_TANK_INVENTORY_DTL B "
						+ "WHERE B.INV_LEVEL_DT <= TO_DATE(?,'DD/MM/YYYY') ) AND A.COMPANY_CD = ? ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, inv_dt);
				stmt1.setString(2, comp_cd);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					conv_factor_1 = rset1.getDouble(1);
					conv_factor_2 = rset1.getDouble(2);
				}
				rset1.close();
				stmt1.close();
				
				double FactorM3ToMMSCM=595; double FactorMMSCMtoMMBTU=38900;
				double d1_vol=0,d2_vol=0,eff_stock=0;
				queryString1 = "SELECT SUM(NVL(A.TANK_D1_VOLUME,0)),SUM(NVL(A.TANK_D2_VOLUME,0))  "
						+ "FROM FMS_TANK_MST A "
						+ "WHERE A.EFF_DT=(SELECT MAX(B.EFF_DT) "
						+ "FROM FMS_TANK_MST B "
						+ "WHERE B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) AND A.COMPANY_CD = ? ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, inv_dt);
				stmt1.setString(2, comp_cd);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					d1_vol = rset1.getDouble(1);
					d2_vol = rset1.getDouble(2);
				}
				rset1.close();
				stmt1.close();
				
				if(conv_factor_1>0)
				{
					FactorM3ToMMSCM = conv_factor_1;	
				}
				T1Dead_Inventory_mmscm = ((d1_vol+d2_vol)*FactorM3ToMMSCM)/1000000;
				
				if(conv_factor_2>0)
				{
					FactorMMSCMtoMMBTU = conv_factor_2;	
				}
				T1Dead_Inventory_mmbtu = (T1Dead_Inventory_mmscm*FactorMMSCMtoMMBTU);
				VUNLOADED_QTY.add(nf.format(T1Dead_Inventory_mmbtu));
				
				eff_stock = mmbtu-T1Dead_Inventory_mmbtu;
				VTANK_MMBTU.add(nf.format(eff_stock));
				
				String countrpty_cd = "",agmt_no = "",cont_type="",cont_no="",start_dt = "",cargo_no="";
				double adq_qty = 0,total_allocated=0,obligation=0,seipl_stk=0;
				queryString1 = "SELECT A.COUNTERPARTY_CD,A.AGMT_NO,B.CONTRACT_TYPE,A.CONT_NO, "
						+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),B.CARGO_NO "
						+ "FROM FMS_LTCORA_CONT_MST A, FMS_LTCORA_CONT_CARGO_DTL B "
						+ "WHERE B.ACTUAL_RECPT_DT <=TO_DATE(?,'DD/MM/YYYY') AND  "
						+ "(B.ACTUAL_RECPT_DT + NVL(B.STORAGE_DAYS, 0) - 1 + NVL(B.STORAGE_EXT_DAYS, 0)) >= TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.COMPANY_CD = ?  "
						+ "AND A.COUNTERPARTY_CD =  B.COUNTERPARTY_CD AND A.AGMT_NO = B.AGMT_NO  "
						+ "AND A.CONT_NO = B.CONT_NO AND A.AGMT_TYPE = B.AGMT_TYPE "
						+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND A.COMPANY_CD = B.COMPANY_CD  "
						+ "AND A.CONT_REV = (SELECT MAX(C.CONT_REV) "
						+ "FROM FMS_LTCORA_CONT_MST C  "
						+ "WHERE C.COUNTERPARTY_CD =  B.COUNTERPARTY_CD AND C.AGMT_NO = B.AGMT_NO  "
						+ "AND C.CONT_NO = B.CONT_NO AND C.AGMT_TYPE = B.AGMT_TYPE "
						+ "AND C.CONTRACT_TYPE = B.CONTRACT_TYPE AND C.COMPANY_CD = B.COMPANY_CD ) AND A.BUY_SALE = 'C' ORDER BY B.ACTUAL_RECPT_DT ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, inv_dt);
				stmt1.setString(2, inv_dt);
				stmt1.setString(3, comp_cd);
				rset1 = stmt1.executeQuery();
				while(rset1.next())
				{
					countrpty_cd = rset1.getString(1)==null?"":rset1.getString(1);
					agmt_no = rset1.getString(2)==null?"":rset1.getString(2);
					cont_type= rset1.getString(3)==null?"":rset1.getString(3);
					cont_no= rset1.getString(4)==null?"":rset1.getString(4);
					start_dt = rset1.getString(5)==null?"":rset1.getString(5);
					cargo_no= rset1.getString(6)==null?"":rset1.getString(6);
					
					String queryString2 = "SELECT SUM(ADQ_QTY)  "
							+ "FROM FMS_LTCORA_CONT_CARGO_ADQ "
							+ "WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? "
							+ "AND CONT_NO = ? AND CONTRACT_TYPE = ? "
							+ "AND CARGO_NO = ? AND ADQ_DT<=TO_DATE(?,'DD/MM/YYYY') AND BUY_SALE = 'C' ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, countrpty_cd);
					stmt2.setString(3, agmt_no);
					stmt2.setString(4, cont_no);
					stmt2.setString(5, cont_type);
					stmt2.setString(6, cargo_no);
					stmt2.setString(7, inv_dt);
					rset2 = stmt2.executeQuery();
					if(rset2.next())
					{
						adq_qty +=rset2.getDouble(1);
					}
					else
					{
						adq_qty=0;
					}
					rset2.close();
					stmt2.close();
					
					queryString2 = "SELECT SUM(QTY_MMBTU) "
							+ "FROM FMS_DAILY_ALLOCATION_DTL A "
							+ "WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? "
							+ "AND CONT_NO = ? AND CONTRACT_TYPE = ? "
							+ "AND CARGO_NO = ? AND "
							+ "GAS_DT BETWEEN TO_DATE(?,'DD/MM/YYYY') "
							+ "AND TO_DATE(?,'DD/MM/YYYY') AND NOM_REV_NO =( "
							+ "SELECT MAX(B.NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
							+ "WHERE A.COMPANY_CD = B.COMPANY_CD AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO = B.AGMT_NO AND A.CONT_NO = B.CONT_NO AND A.PLANT_SEQ = B.PLANT_SEQ "
							+ "AND A.TRANSPORTER_CD = B.TRANSPORTER_CD AND A.TRANS_SEQ = B.TRANS_SEQ AND "
							+ "A.BU_SEQ = B.BU_SEQ AND A.GAS_DT = B.GAS_DT AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND "
							+ "A.CARGO_NO = B.CARGO_NO) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, countrpty_cd);
					stmt2.setString(3, agmt_no);
					stmt2.setString(4, cont_no);
					stmt2.setString(5, cont_type);
					stmt2.setString(6, cargo_no);
					stmt2.setString(7, start_dt);
					stmt2.setString(8, inv_dt);
					rset2 = stmt2.executeQuery();
					if(rset2.next())
					{
						total_allocated +=rset2.getDouble(1);
					}
					else
					{
						total_allocated=0;
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				obligation = adq_qty-total_allocated;
				
				VBALANCE_QTY.add(nf.format(obligation));
				seipl_stk = eff_stock-obligation;
				VSEL_BALANCE_QTY.add(nf.format(seipl_stk));
			}
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
	String year = "";
	public void setYear(String year) {this.year = year;}
	String ic_eff_dt = "";
	public void setIc_eff_dt(String ic_eff_dt) {this.ic_eff_dt = ic_eff_dt;}

	String multiCountpty="";
	String multiAgmtNo="";
	String multiAgmtRev="";
	String multiContNo="";
	String multiContRev="";
	String multiContTyp="";
	String multiCargoNo="";
	String customer_cd="";
	String clearance="";
	String counterparty_cd = "";
	String cont_no = "";
	String cont_rev_no = "";
	String agmt_no = "";
	String agmt_rev_no = "";
	String contract_type = "";
	String tcq_sign = "";
	String inv_level_dt="";

	String opening_balance_month="";
	String opening_balance_year="";
	String omb_tank_volume="";
	String omb_tank_mmscm="";
	String omb_tank_conv_factor_1="";
	String omb_tank_conv_factor_2="";
	String omb_tank_mmbtu="";

	String int_cons_percentage="";
	String int_cons_remark="";
	
	String from_dt="";
	String to_dt="";

	public String getOmb_tank_volume(){return omb_tank_volume;}
	public String getOmb_tank_mmscm(){return omb_tank_mmscm;}
	public String getOmb_tank_conv_factor_1(){return omb_tank_conv_factor_1;}
	public String getOmb_tank_conv_factor_2(){return omb_tank_conv_factor_2;}
	public String getOmb_tank_mmbtu(){return omb_tank_mmbtu;}

	public String getInt_cons_percentage(){return int_cons_percentage;}
	public String getInt_cons_remark(){return int_cons_remark;}

	public void setMultiCountpty(String multiCountpty) {this.multiCountpty = multiCountpty;}
	public void setMultiAgmtNo(String multiAgmtNo) {this.multiAgmtNo = multiAgmtNo;}
	public void setMultiAgmtRev(String multiAgmtRev) {this.multiAgmtRev = multiAgmtRev;}
	public void setMultiContNo(String multiContNo) {this.multiContNo = multiContNo;}
	public void setMultiContRev(String multiContRev) {this.multiContRev = multiContRev;}
	public void setMultiContTyp(String multiContTyp) {this.multiContTyp = multiContTyp;}
	public void setMultiCargoNo(String multiCargoNo) {this.multiCargoNo = multiCargoNo;}
	public void setCustomer_cd(String customer_cd) {this.customer_cd = customer_cd;}
	public void setClearance(String clearance) {this.clearance = clearance;}
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setCont_no(String cont_no) {this.cont_no = cont_no;}
	public void setCont_rev_no(String cont_rev_no) {this.cont_rev_no = cont_rev_no;}
	public void setAgmt_no(String agmt_no) {this.agmt_no = agmt_no;}
	public void setAgmt_rev_no(String agmt_rev_no) {this.agmt_rev_no = agmt_rev_no;}
	public void setContract_type(String contract_type) {this.contract_type = contract_type;}
	public void setTcq_sign(String tcq_sign) {this.tcq_sign = tcq_sign;}
	public void setInv_level_dt(String inv_level_dt) {this.inv_level_dt = inv_level_dt;}

	public void setOpening_balance_month(String opening_balance_month) {this.opening_balance_month = opening_balance_month;}
	public void setOpening_balance_year(String opening_balance_year) {this.opening_balance_year = opening_balance_year;}
	
	public void setFrom_Dt(String from_dt) {this.from_dt = from_dt;}
	public void setTo_Dt(String to_dt) {this.to_dt = to_dt;}

	Vector VINDEX = new Vector();
	Vector VCARGO_POOL_FLAG = new Vector();
	Vector VCARGO_POOL_NM = new Vector();
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VCUSTOMER_CD = new Vector();
	Vector VCUSTOMER_NM = new Vector();
	Vector VCUSTOMER_ABBR = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VCONT_REV_NO = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VAGMT_REV_NO = new Vector();
	Vector VCONT_NAME = new Vector();
	Vector VSTART_DT = new Vector();
	Vector VEND_DT = new Vector();
	Vector VRATE = new Vector();
	Vector VRATE_UNIT = new Vector();
	Vector VRATE_UNIT_NM = new Vector();
	Vector VCONT_STATUS = new Vector();
	Vector VCONT_STATUS_FLG = new Vector();
	Vector VPRICE_TYPE = new Vector();
	Vector VCONTRACT_TYPE = new Vector();
	Vector VCONTRACT_TYPE_NM = new Vector();
	Vector VBOOKED_QTY = new Vector();
	Vector VMIN_ALLOC_DT = new Vector();
	Vector VMAX_ALLOC_DT = new Vector();
	Vector VUNLOADED_QTY = new Vector();
	Vector VUNLOADED_QTY_INFO = new Vector();
	Vector VAVAIL_FOR_SALE_QTY = new Vector();
	Vector VAVAIL_FOR_SALE_QTY_INFO = new Vector();
	Vector VALLOCATED_QTY = new Vector();
	Vector VBALANCE_QTY = new Vector();
	Vector VTCQ = new Vector();
	Vector VDISPLAY_CONT_DTL = new Vector();
	Vector VCONT_REF = new Vector();
	Vector VALLOC_QTY_CONTRACT_WISE = new Vector();
	Vector VNEW_RATE = new Vector();
	Vector VNEW_EFF_DATE = new Vector();
	Vector VCHANGE_SEQ_NO = new Vector();
	Vector VFLAG = new Vector();
	Vector VREMARK = new Vector();
	Vector VCARGO_NO = new Vector();
	Vector VPURCHASE_MAP_ID = new Vector();
	Vector VCARGO_STATUS_FLG = new Vector();

	Vector VSEL_TRADER_ABBR = new Vector();
	Vector VSEL_TRADER_NM = new Vector();
	Vector VSEL_CONT_NO = new Vector();
	Vector VSEL_CONT_REV_NO = new Vector();
	Vector VSEL_AGMT_NO = new Vector();
	Vector VSEL_AGMT_REV_NO = new Vector();
	Vector VSEL_RATE = new Vector();
	Vector VSEL_RATE_UNIT = new Vector();
	Vector VSEL_RATE_UNIT_NM = new Vector();
	Vector VSEL_BALANCE_QTY = new Vector();
	Vector VSEL_DISPLAY_CONT_DTL = new Vector();
	Vector VSEL_COLOR = new Vector();
	Vector VSEL_CARGO_NO = new Vector();

	Vector VTANK_CD = new Vector();
	Vector VTANK_NAME = new Vector();
	Vector VEFF_DT = new Vector();
	Vector VSTATUS = new Vector();
	Vector VTANK_T1_VOLUME = new Vector();
	Vector VTANK_T1_HEIGHT = new Vector();
	Vector VTANK_T2_VOLUME = new Vector();
	Vector VTANK_T2_HEIGHT = new Vector();
	Vector VTANK_D1_VOLUME = new Vector();
	Vector VTANK_D1_HEIGHT = new Vector();
	Vector VTANK_D2_VOLUME = new Vector();
	Vector VTANK_D2_HEIGHT = new Vector();
	Vector VTANK_DIAMETER = new Vector();
	Vector VTANK_PI_TAG = new Vector();
	Vector VTANK_VOLUME = new Vector();
	Vector VTANK_HEIGHT = new Vector();
	Vector VTANK_MMSCM = new Vector();
	Vector VTANK_CONV_FACTOR_1 = new Vector();
	Vector VTANK_CONV_FACTOR_2 = new Vector();
	Vector VTANK_MMBTU = new Vector();
	
	Vector VICD_EFF_DT = new Vector();
	Vector VICD_PERCENTAGE = new Vector();
	Vector VICD_REMARK = new Vector();

	Vector VLNG_WRITE_OFF = new Vector();
	Vector VFLARING = new Vector();
	Vector VAUXILARY_CONSUMPTION = new Vector();
	Vector VSCV_FUEL_CONSUMPTION = new Vector();
	Vector VSUG = new Vector();
	Vector VOTHER_CONSUMPTION = new Vector();
	Vector VMASS_BALANCING = new Vector();
	Vector VTOTAL_CONSUMPTION = new Vector();

	public Vector getVLNG_WRITE_OFF() {return VLNG_WRITE_OFF;}
	public Vector getVFLARING() {return VFLARING;}
	public Vector getVAUXILARY_CONSUMPTION() {return VAUXILARY_CONSUMPTION;}
	public Vector getVSCV_FUEL_CONSUMPTION() {return VSCV_FUEL_CONSUMPTION;}
	public Vector getVSUG() {return VSUG;}
	public Vector getVOTHER_CONSUMPTION() {return VOTHER_CONSUMPTION;}
	public Vector getVMASS_BALANCING() {return VMASS_BALANCING;}
	public Vector getVTOTAL_CONSUMPTION() {return VTOTAL_CONSUMPTION;}

	public Vector getVTANK_CD() {return VTANK_CD;}
	public Vector getVTANK_NAME() {return VTANK_NAME;}
	public Vector getVEFF_DT() {return VEFF_DT;}
	public Vector getVSTATUS() {return VSTATUS;}
	public Vector getVTANK_T1_VOLUME() {return VTANK_T1_VOLUME;}
	public Vector getVTANK_T1_HEIGHT() {return VTANK_T1_HEIGHT;}
	public Vector getVTANK_T2_VOLUME() {return VTANK_T2_VOLUME;}
	public Vector getVTANK_T2_HEIGHT() {return VTANK_T2_HEIGHT;}
	public Vector getVTANK_D1_VOLUME() {return VTANK_D1_VOLUME;}
	public Vector getVTANK_D1_HEIGHT() {return VTANK_D1_HEIGHT;}
	public Vector getVTANK_D2_VOLUME() {return VTANK_D2_VOLUME;}
	public Vector getVTANK_D2_HEIGHT() {return VTANK_D2_HEIGHT;}
	public Vector getVTANK_DIAMETER() {return VTANK_DIAMETER;}
	public Vector getVTANK_PI_TAG() {return VTANK_PI_TAG;}
	public Vector getVTANK_VOLUME() {return VTANK_VOLUME;}
	public Vector getVTANK_HEIGHT() {return VTANK_HEIGHT;}
	public Vector getVTANK_MMSCM() {return VTANK_MMSCM;}
	public Vector getVTANK_CONV_FACTOR_1() {return VTANK_CONV_FACTOR_1;}
	public Vector getVTANK_CONV_FACTOR_2() {return VTANK_CONV_FACTOR_2;}
	public Vector getVTANK_MMBTU() {return VTANK_MMBTU;}
	
	public Vector getVICD_EFF_DT() {return VICD_EFF_DT;}
	public Vector getVICD_PERCENTAGE() {return VICD_PERCENTAGE;}
	public Vector getVICD_REMARK() {return VICD_REMARK;}

	public Vector getVINDEX() {return VINDEX;}
	public Vector getVCARGO_POOL_FLAG() {return VCARGO_POOL_FLAG;}
	public Vector getVCARGO_POOL_NM() {return VCARGO_POOL_NM;}
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVCUSTOMER_CD() {return VCUSTOMER_CD;}
	public Vector getVCUSTOMER_NM() {return VCUSTOMER_NM;}
	public Vector getVCUSTOMER_ABBR() {return VCUSTOMER_ABBR;}
	public Vector getVCONT_NO() {return VCONT_NO;}
	public Vector getVCONT_REV_NO() {return VCONT_REV_NO;}
	public Vector getVAGMT_NO() {return VAGMT_NO;}
	public Vector getVAGMT_REV_NO() {return VAGMT_REV_NO;}
	public Vector getVCONT_NAME() {return VCONT_NAME;}
	public Vector getVSTART_DT() {return VSTART_DT;}
	public Vector getVEND_DT() {return VEND_DT;}
	public Vector getVRATE() {return VRATE;}
	public Vector getVRATE_UNIT() {return VRATE_UNIT;}
	public Vector getVRATE_UNIT_NM() {return VRATE_UNIT_NM;}
	public Vector getVCONT_STATUS() {return VCONT_STATUS;}
	public Vector getVCONT_STATUS_FLG() {return VCONT_STATUS_FLG;}
	public Vector getVPRICE_TYPE() {return VPRICE_TYPE;}
	public Vector getVCONTRACT_TYPE() {return VCONTRACT_TYPE;}
	public Vector getVCONTRACT_TYPE_NM() {return VCONTRACT_TYPE_NM;}
	public Vector getVBOOKED_QTY() {return VBOOKED_QTY;}
	public Vector getVMIN_ALLOC_DT() {return VMIN_ALLOC_DT;}
	public Vector getVMAX_ALLOC_DT() {return VMAX_ALLOC_DT;}
	public Vector getVUNLOADED_QTY() {return VUNLOADED_QTY;}
	public Vector getVUNLOADED_QTY_INFO() {return VUNLOADED_QTY_INFO;}
	public Vector getVAVAIL_FOR_SALE_QTY() {return VAVAIL_FOR_SALE_QTY;}
	public Vector getVAVAIL_FOR_SALE_QTY_INFO() {return VAVAIL_FOR_SALE_QTY_INFO;}
	public Vector getVALLOCATED_QTY() {return VALLOCATED_QTY;}
	public Vector getVBALANCE_QTY() {return VBALANCE_QTY;}
	public Vector getVTCQ() {return VTCQ;}
	public Vector getVDISPLAY_CONT_DTL() {return VDISPLAY_CONT_DTL;}
	public Vector getVCONT_REF() {return VCONT_REF;}
	public Vector getVALLOC_QTY_CONTRACT_WISE() {return VALLOC_QTY_CONTRACT_WISE;}
	public Vector getVNEW_RATE() {return VNEW_RATE;}
	public Vector getVNEW_EFF_DATE() {return VNEW_EFF_DATE;}
	public Vector getVCHANGE_SEQ_NO() {return VCHANGE_SEQ_NO;}
	public Vector getVFLAG() {return VFLAG;}
	public Vector getVREMARK() {return VREMARK;}
	public Vector getVCARGO_NO() {return VCARGO_NO;}
	public Vector getVPURCHASE_MAP_ID() {return VPURCHASE_MAP_ID;}
	public Vector getVCARGO_STATUS_FLG() {return VCARGO_STATUS_FLG;}

	public Vector getVSEL_TRADER_ABBR() {return VSEL_TRADER_ABBR;}
	public Vector getVSEL_TRADER_NM() {return VSEL_TRADER_NM;}
	public Vector getVSEL_CONT_NO() {return VSEL_CONT_NO;}
	public Vector getVSEL_CONT_REV_NO() {return VSEL_CONT_REV_NO;}
	public Vector getVSEL_AGMT_NO() {return VSEL_AGMT_NO;}
	public Vector getVSEL_AGMT_REV_NO() {return VSEL_AGMT_REV_NO;}
	public Vector getVSEL_RATE() {return VSEL_RATE;}
	public Vector getVSEL_RATE_UNIT() {return VSEL_RATE_UNIT;}
	public Vector getVSEL_RATE_UNIT_NM() {return VSEL_RATE_UNIT_NM;}
	public Vector getVSEL_BALANCE_QTY() {return VSEL_BALANCE_QTY;}
	public Vector getVSEL_DISPLAY_CONT_DTL() {return VSEL_DISPLAY_CONT_DTL;}
	public Vector getVSEL_COLOR() {return VSEL_COLOR;}
	public Vector getVSEL_CARGO_NO() {return VSEL_CARGO_NO;}

	double purchaseBookedQty=0;
	double unloadedQty=0;
	double saleQty=0;
	double saleBookedQty=0;
	double suppliedQty=0;
	double balanceQty=0;
	double exchgRate = 0;

	public Double getPurchaseBookedQty() {return purchaseBookedQty;}
	public Double getUnloadedQty() {return unloadedQty;}
	public Double getSaleQty() {return saleQty;}
	public Double getSaleBookedQty() {return saleBookedQty;}
	public Double getSuppliedQty() {return suppliedQty;}
	public Double getBalanceQty() {return balanceQty;}
	public double getExchgRate() {return exchgRate;}

}
