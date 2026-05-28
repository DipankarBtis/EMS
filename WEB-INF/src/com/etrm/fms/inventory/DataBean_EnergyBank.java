package com.etrm.fms.inventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.etrm.fms.util.DB_AllocationUtil;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

//Coded By          : Harsh Patel
//Code Reviewed by	:
//CR Date			: 21/10/2022
//Status	  		: Developing

public class DataBean_EnergyBank
{
	String db_src_file_name="DataBean_EnergyBank.java";

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
	DB_AllocationUtil allocUtil = new DB_AllocationUtil();	//PB20250101

	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");

	String color_unloaded="#ccffcc";
	String color_expected="#ff66d9";
	String color_pseudo="#ffb380";
	String color_own="#09F4FE";

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
	    			if(callFlag.equalsIgnoreCase("ENERGY_BANK"))
		    		{
		    			getEnergyBank();
		    			getCargoPool();
	    				getCargoDetails();
	    				//getOwnAccountDtl(); 	//Temporary...
		    		}
		    		else if(callFlag.equalsIgnoreCase("MMBTU_ALLOCATION"))
		    		{
		    			getExchangeRate();
		    			getCustomerListForAllocation();
		    			getSelectedPurchaseConttactList();
		    			getContractListForAllocation();
		    		}
		    		else if(callFlag.equalsIgnoreCase("TCQ_MODIFICATION"))
		    		{
		    			is_tcq_modification="Y";
		    			getTcqModifyCargoPool();
		    			getCargoDetails();
		    		}
		    		else if(callFlag.equalsIgnoreCase("TRADER_PRICE_CHANGE"))
		    		{
		    			getExchangeRate();
		    			getTraderContractListForPriceChange();
		    		}
		    		else if(callFlag.equalsIgnoreCase("MMBTU_ALLOCATION_TRANSFER"))		//PB20241231: for allocation transfer
		    		{
		    			getSelectedPurchaseConttactList();
		    			getCargoAllocatedDeatils();
		    			getCargoPool();
		    			getCargoDetails();
		    		}
		    		else if(callFlag.equalsIgnoreCase("TRANSFER_CARGO"))	//PB20250104
		    		{
		    			getCargoAllocatedDeatils();
		    			multiCountpty=temp_multiCountpty;
		    			multiAgmtNo=temp_multiAgmtNo;
		    			multiAgmtRev=temp_multiAgmtRev;
		    			multiContNo = temp_multiContNo;
		    			multiContTyp = temp_multiContTyp;
		    			multiCargoNo = temp_multiCargoNo;
		    			getSelectedPurchaseConttactList();
		    		}
		    		else if(callFlag.equalsIgnoreCase("ALLOCATION_TRANSFER_REPORT"))	//PB20250124: for allocation transfer report
		    		{
		    			getAllocationTransferDetail();
		    		}
		    		else if(callFlag.equalsIgnoreCase("PSEUDO_TRANSFER_REPORT"))		//PB20250219: for pseudo transfer report
		    		{
		    			getPseudoTransferDetails();
		    		}
		    		else if(callFlag.equalsIgnoreCase("OWN_PROJECTED_DTL"))
		    		{
		    			getProjectedLTCORACargoDtl();
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

	public void getEnergyBank()
	{
		String function_nm="getEnergyBank()";
		try
		{
			String queryString="SELECT SUM(TCQ) "
					+ "FROM FMS_TRADER_CONT_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV) ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				purchaseBookedQty = rset.getDouble(1);
			}
			rset.close();
			stmt.close();

			queryString="SELECT SUM(QTY_MMBTU) "
	  				+ "FROM FMS_BUY_DAILY_ALLOCATION A "
	  				+ "WHERE COMPANY_CD=? "
					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
					+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
					+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				unloadedQty = rset.getDouble(1);
			}
			rset.close();
			stmt.close();

			balanceQty = unloadedQty;
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getCargoPool()
	{
		String function_nm="getCargoPool()";
		try
		{
			VCARGO_POOL_FLAG.add("U");
			VCARGO_POOL_NM.add("Unloaded MMBTU");

			VCARGO_POOL_FLAG.add("O");	//added by Pratham Bhatt 20250303 for own volume
			VCARGO_POOL_NM.add("Own Volume Account");	//added by Pratham Bhatt 20250303 for own volume

			VCARGO_POOL_FLAG.add("E");
			VCARGO_POOL_NM.add("Expected MMBTU");

			VCARGO_POOL_FLAG.add("S");	//added by Pratham Bhatt 20250213 for pseudo cargo
			VCARGO_POOL_NM.add("Pseudo MMBTU");	//added by Pratham Bhatt 20250213 for pseudo cargo

		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getTcqModifyCargoPool()
	{
		String function_nm="getTcqModifyCargoPool()";
		try
		{
			if(tcq_sign.equals("-"))
			{
				VCARGO_POOL_FLAG.add("P");
				VCARGO_POOL_NM.add("Previous Purchase MMBTU Mapping");
			}
			else
			{
				VCARGO_POOL_FLAG.add("P");
				VCARGO_POOL_NM.add("Previous Purchase MMBTU Mapping");

				VCARGO_POOL_FLAG.add("U");
				VCARGO_POOL_NM.add("Unloaded MMBTU");

				VCARGO_POOL_FLAG.add("O");	//added by Pratham Bhatt 20250303 for own volume
				VCARGO_POOL_NM.add("Own Volume Account");	//added by Pratham Bhatt 20250303 for own volume

				VCARGO_POOL_FLAG.add("E");
				VCARGO_POOL_NM.add("Expected MMBTU");

				VCARGO_POOL_FLAG.add("S");	//added by Pratham Bhatt 20250217 for pseudo cargo
				VCARGO_POOL_NM.add("Pseudo MMBTU");	//added by Pratham Bhatt 20250217 for pseudo cargo
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	//AS DISCUSSED WITH VIJAY, INTERNAL CONSYMPTION CALCULATION IS NOT REQUITED FOR SEMTIPL 20240926
	//FOR LNG CARGO, MINUS 0.66% OF SUG FROM AVILE FOR SALE.
	//double internal_consumption = 2.5;
	//double sug_percent=0.66;
	double sug_percent=0;

	double initial_vol2=816900;		//Initial volume of own volume account for SEIPL profile ONLY!

	public void getCargoDetails()
	{
		String function_nm="getCargoDetails()";

		try
		{
			comp_abbr = utilBean.getCompanyAbbr(conn,comp_cd);
			for(int i=0; i<VCARGO_POOL_FLAG.size(); i++)
			{
				if(VCARGO_POOL_FLAG.elementAt(i).equals("S"))
				{
					getPseudoCargoDetails();
				}
				else if(VCARGO_POOL_FLAG.elementAt(i).equals("O"))
				{
					getOwnAccountDtl();
				}
				else
				{
					String chkingSign="=";
					if(VCARGO_POOL_FLAG.elementAt(i).equals("U"))
					{
						chkingSign = ">";
					}
					else if(VCARGO_POOL_FLAG.elementAt(i).equals("E"))
					{
						chkingSign = "<=";
					}

					if(VCARGO_POOL_FLAG.elementAt(i).equals("P"))
					{
						is_parent_cargo_list="Y";
					}
					else
					{
						is_parent_cargo_list="N";
					}

					int count=0;

					String commonQry = "FROM FMS_BUY_DAILY_ALLOCATION C "
							+ "WHERE A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
							+ "AND A.CONT_NO=C.CONT_NO AND A.AGMT_NO=C.AGMT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE "
							+ "AND C.GAS_DT >= A.START_DT AND C.GAS_DT <= A.END_DT "
							+ "AND NOM_REV_NO=(SELECT MAX(B.NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
							+ "WHERE B.CONT_NO=C.CONT_NO AND B.AGMT_NO=C.AGMT_NO "
							+ "AND B.COMPANY_CD=C.COMPANY_CD AND B.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
							+ "AND B.TRANSPORTER_CD=C.TRANSPORTER_CD AND B.TRANS_SEQ=C.TRANS_SEQ AND B.BU_SEQ=C.BU_SEQ "
							+ "AND B.PLANT_SEQ=C.PLANT_SEQ AND B.CONTRACT_TYPE=C.CONTRACT_TYPE "
							+ "AND B.GAS_DT=C.GAS_DT AND C.CARGO_NO=B.CARGO_NO) ";

					String commonQry1 = "FROM FMS_BUY_CARGO_ALLOC C "
							+ "WHERE A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.AGMT_TYPE=C.AGMT_TYPE "
							+ "AND A.CONT_NO=C.CONT_NO AND A.AGMT_NO=C.AGMT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND B.CARGO_NO=C.CARGO_NO "
							+ "AND C.ALLOC_REV=(SELECT MAX(B.ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC B "
							+ "WHERE B.CONT_NO=C.CONT_NO AND B.AGMT_NO=C.AGMT_NO AND B.AGMT_TYPE=C.AGMT_TYPE AND B.CARGO_NO=C.CARGO_NO "
							+ "AND B.COMPANY_CD=C.COMPANY_CD AND B.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND B.CONTRACT_TYPE=C.CONTRACT_TYPE) ";

					String qty="SELECT NVL(SUM(C.QTY_MMBTU),0) "+commonQry;
					String qty1="SELECT NVL(C.QQ_QTY_MMBTU,0) "+commonQry1;

					String min_dt="SELECT TO_CHAR(MIN(C.GAS_DT),'DD/MM/YYYY') "+commonQry;
					String min_dt1="SELECT TO_CHAR(MIN(C.QQ_DT),'DD/MM/YYYY') "+commonQry1;

					String max_dt="SELECT TO_CHAR(MAX(C.GAS_DT),'DD/MM/YYYY') "+commonQry;
					String max_dt1="SELECT TO_CHAR(MAX(C.QQ_DT),'DD/MM/YYYY') "+commonQry1;

					String alloc_count="SELECT COUNT(*) "+commonQry;
					String alloc_count1="SELECT COUNT(*) "+commonQry1;

					String allocated = "SELECT NVL(SUM(ALLOC_QTY),0) "
							+ "FROM FMS_SUPPLY_PURCHASE_MAP_DTL C "
							+ "WHERE C.COMPANY_CD=A.COMPANY_CD AND C.PUR_CONT_NO=A.CONT_NO ";

					String allocated1 = "SELECT NVL(SUM(ALLOC_QTY),0) "
							+ "FROM FMS_SUPPLY_PURCHASE_MAP_DTL C "
							+ "WHERE C.COMPANY_CD=A.COMPANY_CD AND C.PUR_CONT_NO=A.CONT_NO AND C.CARGO_NO=B.CARGO_NO ";

					String no_of_contDay="SELECT TO_DATE(A.END_DT,'DD/MM/YYYY') - TO_DATE(SYSDATE,'DD/MM/YYYY')+1 FROM DUAL";

					String projQty="(CASE WHEN ("+alloc_count+") > 0 THEN CASE WHEN ("+no_of_contDay+") > 0 THEN (A.DCQ * ("+no_of_contDay+")) ELSE 0 END ELSE 0 END)";

					//PB20250307: FOR FETCHING SUG% FROM LTCORA IF LNG CARGO IS LINKED

					sug_percent=comp_cd.equals("1")?0.66:0;				// default 0.66 for SEMTIPL and 0 for SEIPL

					String common_mod_sug="FROM FMS_LTCORA_CONT_CARGO_MOD D WHERE D.COMPANY_CD = E.COMPANY_CD "
							+ "AND D.COUNTERPARTY_CD = E.COUNTERPARTY_CD AND D.BUY_SALE=E.BUY_SALE AND D.AGMT_TYPE = E.AGMT_TYPE "
							+ "AND D.AGMT_NO=E.AGMT_NO AND D.CONT_NO=E.CONT_NO AND D.CONTRACT_TYPE=E.CONTRACT_TYPE "
							+ "AND D.CARGO_NO=E.CARGO_NO AND D.APPROVAL_FLAG='Y' "
							+ "AND D.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_MOD C "
							+ "WHERE D.COMPANY_CD = C.COMPANY_CD AND D.COUNTERPARTY_CD = D.COUNTERPARTY_CD AND D.BUY_SALE=C.BUY_SALE "
							+ "AND D.AGMT_TYPE = C.AGMT_TYPE  AND D.CONT_NO=C.CONT_NO AND D.CONTRACT_TYPE=C.CONTRACT_TYPE AND D.CARGO_NO=C.CARGO_NO "
							+ "AND C.APPROVAL_FLAG='Y') ";

					String common_sug="FROM FMS_LTCORA_CONT_MST B  WHERE E.COMPANY_CD = B.COMPANY_CD AND B.BUY_SALE=E.BUY_SALE "
							+ "AND B.AGMT_TYPE = E.AGMT_TYPE AND B.AGMT_NO=E.AGMT_NO AND B.CONT_NO=E.CONT_NO "
							+ "AND B.CONTRACT_TYPE=E.CONTRACT_TYPE AND B.CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST C "
							+ "WHERE B.COMPANY_CD = C.COMPANY_CD AND  B. COUNTERPARTY_CD = C.COUNTERPARTY_CD AND B.BUY_SALE=C.BUY_SALE "
							+ "AND B.AGMT_TYPE = C.AGMT_TYPE AND B.CONT_NO=C.CONT_NO AND B.CONTRACT_TYPE=C.CONTRACT_TYPE )";

					String sug="SELECT SUG "+common_sug;

					String count_mod_sug="SELECT COUNT(*) "+common_mod_sug;

					String mod_sug="SELECT SUG "+common_mod_sug;

					String temp_sug="CASE WHEN ("+count_mod_sug+")>0 THEN ("+mod_sug+") ELSE ("+sug+") END ";

					String final_sug = "CASE WHEN E.ATTACH_LNG_CARGO=(TO_CHAR(B.COMPANY_CD)||'-'||TO_CHAR(B.COUNTERPARTY_CD)||'-'||TO_CHAR(B.AGMT_TYPE)||'-'||TO_CHAR(B.AGMT_NO)||'-'||TO_CHAR(B.AGMT_REV)||'-'||TO_CHAR(B.CONTRACT_TYPE)||'-'||TO_CHAR(B.CONT_NO)||'-'||TO_CHAR(B.CONT_REV)||'-'||TO_CHAR(B.CARGO_NO)) THEN ("+temp_sug+") ELSE "+sug_percent+" END";


					//Pratham Bhatt 20250226 for adding balance mmbtu filter in energy bank
					String bal_qty_range = "(CASE WHEN ("+alloc_count+") > 0 THEN ("+qty+")+("+projQty+") ELSE TCQ END) - ("+allocated+")";

					String act_qty = "CASE WHEN ("+alloc_count1+") > 0 THEN ("+qty1+") ELSE B.CARGO_QTY END";
					String bal_qty_range1="ROUND("+act_qty+"-(("+act_qty+")*("+final_sug+"))/100,2)-("+allocated1+")";

					int index=0;
					double total_balance_mmbtu = 0;
					String queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, CONT_NO, CONT_REV, TCQ, QUANTITY_UNIT, "
							+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),"
							+ "CONT_NAME,RATE,RATE_UNIT,CONT_STATUS,AGMT_NO,AGMT_REV,CONTRACT_TYPE,"
							+ "("+qty+") UNLOADED_QTY, "
							+ "("+min_dt+") MIN_ALLOC_DT, "
							+ "("+max_dt+") MIN_ALLOC_DT, "
							+ "("+alloc_count+") ALLOC_COUNT, "
							+ "("+allocated+") ALLOCATED_QTY, "
							+ "CONT_REF_NO,TRADE_REF_NO, "
							+ "("+projQty+") PROJECTED_QTY,"
							+ "NULL,NULL,"
							+ "NULL "		//FOR FINAL SUG PERCENT
							+ "FROM FMS_TRADER_CONT_MST A "
							+ "WHERE A.COMPANY_CD=? "
							+ "AND A.CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
					if(!mmbtu_range_sign.equals("0"))
					{
						queryString+="AND ("+bal_qty_range+")" +mmbtu_range_sign+" 0 ";
					}
					if(!VCARGO_POOL_FLAG.elementAt(i).equals("P"))
					{
						queryString += "AND ("+qty+") "+chkingSign+" 0 ";
					}
					if(is_tcq_modification.equals("Y"))
					{
						if(is_parent_cargo_list.equals("Y"))
						{
							queryString +="AND A.CONT_NO IN (SELECT DISTINCT(PUR_CONT_NO) "
									+ "FROM FMS_SUPPLY_PURCHASE_MAP_DTL WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=?) ";
						}
						else
						{
							queryString +="AND A.CONT_NO NOT IN (SELECT DISTINCT(PUR_CONT_NO) "
									+ "FROM FMS_SUPPLY_PURCHASE_MAP_DTL WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=?) ";
						}
					}
					queryString+=" UNION ALL ";
					queryString+="SELECT A.COMPANY_CD, A.COUNTERPARTY_CD, A.CONT_NO, A.CONT_REV, B.CARGO_QTY, 1, "
							+ "TO_CHAR(B.START_DT,'DD/MM/YYYY'),TO_CHAR(B.END_DT,'DD/MM/YYYY'),"
							+ "A.CONT_NAME,B.RATE,B.RATE_UNIT,A.CONT_STATUS,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,"
							+ "("+qty1+") UNLOADED_QTY, "
							+ "("+min_dt1+") MIN_ALLOC_DT, "
							+ "("+max_dt1+") MIN_ALLOC_DT, "
							+ "("+alloc_count1+") ALLOC_COUNT, "
							+ "("+allocated1+") ALLOCATED_QTY, "
							+ "B.CARGO_REF,NULL, "
							+ "(NULL) PROJECTED_QTY, "//+ "("+projQty+") PROJECTED_QTY "
							+ "B.CARGO_NO,B.CARGO_STATUS,"
							+ "("+final_sug+") "	//FOR FINAL SUG PERCENT
							+ "FROM FMS_TRADER_CN_MST A, FMS_TRADER_CARGO_MST B LEFT JOIN FMS_LTCORA_CONT_CARGO_DTL E "
							+ "ON  E.COMPANY_CD=B.COMPANY_CD "
							+ "AND E.ATTACH_LNG_CARGO=B.COMPANY_CD||'-'||B.COUNTERPARTY_CD||'-'||B.AGMT_TYPE||'-'||B.AGMT_NO||'-'||B.AGMT_REV||'-'||B.CONTRACT_TYPE||'-'||B.CONT_NO||'-'||B.CONT_REV||'-'||B.CARGO_NO "
							+ "WHERE A.COMPANY_CD=? "
							+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
							+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV ";

					if(!mmbtu_range_sign.equals("0"))
					{
						queryString += "AND ("+bal_qty_range1+") "+mmbtu_range_sign+" 0 ";
					}
					if(!VCARGO_POOL_FLAG.elementAt(i).equals("P"))
					{
						queryString += "AND NVL(("+qty1+"),0) "+chkingSign+" 0 ";
					}
					if(is_tcq_modification.equals("Y"))
					{
						if(is_parent_cargo_list.equals("Y"))
						{
							queryString +="AND A.CONT_NO IN (SELECT DISTINCT(PUR_CONT_NO) "
									+ "FROM FMS_SUPPLY_PURCHASE_MAP_DTL WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=?) ";

							queryString +="AND B.CARGO_NO IN (SELECT DISTINCT(CARGO_NO) "
									+ "FROM FMS_SUPPLY_PURCHASE_MAP_DTL WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=?) ";
						}
						else
						{
							queryString +="AND A.CONT_NO NOT IN (SELECT DISTINCT(PUR_CONT_NO) "
									+ "FROM FMS_SUPPLY_PURCHASE_MAP_DTL WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=?) ";

							queryString +="AND B.CARGO_NO NOT IN (SELECT DISTINCT(CARGO_NO) "
									+ "FROM FMS_SUPPLY_PURCHASE_MAP_DTL WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND PUR_CONT_NO != 0) "; 	//PUR_CONT_NO !=0 added for the pseudo cargo
						}
					}

					//PB20250218: NOTE
					//The CONT_NO for the pseudo cargo is kept "0", so handle it whenever it requires.

					String temp_queryString = queryString;
					stmt=conn.prepareStatement(temp_queryString);
					stmt.setString(++count, comp_cd);
					if(is_tcq_modification.equals("Y"))
					{
						stmt.setString(++count, comp_cd);
						stmt.setString(++count, counterparty_cd);
						stmt.setString(++count, agmt_no);
						stmt.setString(++count, cont_no);
						stmt.setString(++count, contract_type);
					}
					stmt.setString(++count, comp_cd);
					if(is_tcq_modification.equals("Y"))
					{
						stmt.setString(++count, comp_cd);
						stmt.setString(++count, counterparty_cd);
						stmt.setString(++count, agmt_no);
						stmt.setString(++count, cont_no);
						stmt.setString(++count, contract_type);
						stmt.setString(++count, comp_cd);
						stmt.setString(++count, counterparty_cd);
						stmt.setString(++count, agmt_no);
						stmt.setString(++count, cont_no);
						stmt.setString(++count, contract_type);
					}
					rset=stmt.executeQuery();
					while(rset.next())
					{
//					index+=1;

						String own_cd=rset.getString(1)==null?"":rset.getString(1);
						String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
						String contNo=rset.getString(3)==null?"":rset.getString(3);
						String contRev=rset.getString(4)==null?"0":rset.getString(4);
						String agmtNo=rset.getString(13)==null?"0":rset.getString(13);
						String agmtRev=rset.getString(14)==null?"0":rset.getString(14);
						String cont_type=rset.getString(15)==null?"":rset.getString(15);
						String cargoNo=rset.getString(24)==null?"0":rset.getString(24);

						String cont_ref=rset.getString(21)==null?"":rset.getString(21);
						String trade_ref=rset.getString(22)==null?"":rset.getString(22);

						double expected = rset.getDouble(5);
						int allocation_count = rset.getInt(19);

						String qty_unit=rset.getString(6)==null?"":rset.getString(6);
						if(qty_unit.equals("2"))
						{
							expected = expected * 1000000; //Convert to MMBTU
						}
						double unloaded = rset.getDouble(16);
						double projected_qty = rset.getDouble(23);
						String unloadedQtyInfo="Actual Unloaded Qty : "+nf.format(unloaded)+"\nProjected Qty : "+nf.format(projected_qty);
						unloaded = unloaded + projected_qty;

						double actual_unloaded=0;
						if(allocation_count <= 0)
						{
							actual_unloaded=expected;
						}
						else
						{
							actual_unloaded=unloaded;
						}
						//PB
						//20240926 double avail_for_sale = actual_unloaded - (actual_unloaded*internal_consumption)/100;

						double sug_per= rset.getDouble(26);

						double avail_for_sale = actual_unloaded;
						String avail_for_sale_info=""+nf.format(actual_unloaded);
						if(cont_type.equals("N"))
						{
							//avail_for_sale = actual_unloaded - (actual_unloaded*sug_percent)/100;
							//avail_for_sale_info=""+nf.format(actual_unloaded)+" - "+sug_percent+"% of "+nf.format(actual_unloaded)+" = "+nf.format(avail_for_sale);
							avail_for_sale = actual_unloaded - (actual_unloaded*sug_per)/100;
							avail_for_sale_info=""+nf.format(actual_unloaded)+" - "+sug_per+"% of "+nf.format(actual_unloaded)+" = "+nf.format(avail_for_sale);
						}
						double allocated_qty = rset.getDouble(20);
						double balance_qty = avail_for_sale - allocated_qty;

						total_balance_mmbtu +=balance_qty;

						if(callFlag.equalsIgnoreCase("MMBTU_ALLOCATION_TRANSFER"))
						{
							//PB20250102
							if(!multiContNo.equals(""))
							{
								if(multiContNo.equals(rset.getString(3)==null?"0":rset.getString(3)))
								{
									continue;
								}
							}
							if(balance_qty<=0)
							{
								continue;
							}
						}

						index+=1;

						String purchase_map_id=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtNo, agmtRev, contNo, contRev, cont_type, cargoNo);
						VPURCHASE_MAP_ID.add(purchase_map_id);

						VCOUNTERPARTY_CD.add(countpty_cd);
						VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
						VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
						VCONT_NO.add(contNo);
						VCONT_REV_NO.add(contRev);
						VAGMT_NO.add(agmtNo);
						VAGMT_REV_NO.add(agmtRev);
						VCARGO_NO.add(cargoNo);
						VBOOKED_QTY.add(nf.format(expected));

						VSTART_DT.add(rset.getString(7)==null?"":rset.getString(7));
						VEND_DT.add(rset.getString(8)==null?"":rset.getString(8));
						VCONT_NAME.add(rset.getString(9)==null?"":rset.getString(9));

						double rate =rset.getDouble(10);
						String rate_unit = rset.getString(11)==null?"1":rset.getString(11);
						VRATE.add(""+utilBean.RateNumberFormat(rate, rate_unit));
						VRATE_UNIT.add(rate_unit);
						VRATE_UNIT_NM.add(""+utilBean.getRateUnitNm(conn,rate_unit));

						VPRICE_TYPE.add("Fixed");
						String status_flg = rset.getString(12)==null?"":rset.getString(12);
						VCONT_STATUS_FLG.add(status_flg);
						String cargo_status_flag=rset.getString(25)==null?"":rset.getString(25);
						VCARGO_STATUS_FLG.add(cargo_status_flag);
						String cont_status=""+ContStatusName(status_flg);
						if(cargo_status_flag.equals("Y"))
						{
							cont_status+=" <font color='green'>[Confirmed]</font>";
						}
						else if(cargo_status_flag.equals("N"))
						{
							cont_status+=" <font color='blue'>[Not Confirmed]</font>";
						}
						else if(cargo_status_flag.equals("X"))
						{
							cont_status+=" <font color='red'>[Canceled]</font>";
						}
						VCONT_STATUS.add(cont_status);

						VCONTRACT_TYPE.add(cont_type);
						VCONTRACT_TYPE_NM.add(""+utilBean.getContractTypeName(cont_type));
						/*if(cont_type.equals("D"))
						{
							VCONTRACT_TYPE_NM.add("Domestic NG");
						}
						else if(cont_type.equals("I"))
						{
							VCONTRACT_TYPE_NM.add("IGX");
						}
						else if(cont_type.equals("N"))
						{
							VCONTRACT_TYPE_NM.add("LNG");
						}
						else if(cont_type.equals("T"))
						{
							VCONTRACT_TYPE_NM.add("In Tank LNG|RLNG");
						}
						else
						{
							VCONTRACT_TYPE_NM.add("");
						}*/
						VUNLOADED_QTY.add(nf.format(unloaded));
						VUNLOADED_QTY_INFO.add(unloadedQtyInfo);

						VMIN_ALLOC_DT.add(rset.getString(17)==null?"":rset.getString(17));
						VMAX_ALLOC_DT.add(rset.getString(18)==null?"":rset.getString(18));

//					//20240926 double avail_for_sale = actual_unloaded - (actual_unloaded*internal_consumption)/100;
//
//					double avail_for_sale = actual_unloaded;
//					String avail_for_sale_info=""+nf.format(actual_unloaded);
//					if(cont_type.equals("N"))
//					{
//						avail_for_sale = actual_unloaded - (actual_unloaded*sug_percent)/100;
//						avail_for_sale_info=""+nf.format(actual_unloaded)+" - "+sug_percent+"% of "+nf.format(actual_unloaded)+" = "+nf.format(avail_for_sale);
//					}
//					double allocated_qty = rset.getDouble(20);
//					double balance_qty = avail_for_sale - allocated_qty;

						VAVAIL_FOR_SALE_QTY.add(nf.format(avail_for_sale));
						VAVAIL_FOR_SALE_QTY_INFO.add(avail_for_sale_info);
						VALLOCATED_QTY.add(nf.format(allocated_qty));
						VBALANCE_QTY.add(nf.format(balance_qty));
						VBALANCE_QTY_INFO.add("");

						if(cont_type.equals("I"))
						{
							cont_ref=trade_ref;
						}
						VCONT_REF.add(cont_ref);

						String remark="";

						if(is_tcq_modification.equals("Y"))
						{
							if(is_parent_cargo_list.equals("Y"))
							{
								double contQty=getContractWiseAllocatedMMBTU(counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, contNo,cargoNo);
								VALLOC_QTY_CONTRACT_WISE.add(nf.format(contQty));
							}
							else
							{
								VALLOC_QTY_CONTRACT_WISE.add("0.00");
							}
						}
						else
						{
							int price_req_count=0;
							String queryString1="SELECT COUNT(*) "
									+ "FROM FMS_TRADER_CONT_PRICE_DTL A "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? AND CONT_NO=? "
									+ "AND CONTRACT_TYPE=? AND FLAG=? "
									+ "AND SEQ_NO=(SELECT MAX(B.SEQ_NO) FROM FMS_TRADER_CONT_PRICE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
									+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
							stmt1=conn.prepareStatement(queryString1);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, countpty_cd);
							stmt1.setString(3, agmtNo);
							stmt1.setString(4, contNo);
							stmt1.setString(5, cont_type);
							stmt1.setString(6, "R");
							rset1=stmt1.executeQuery();
							if(rset1.next())
							{
								price_req_count=rset1.getInt(1);
							}
							rset1.close();
							stmt1.close();

							if(price_req_count>0)
							{
								remark="<font color='blue'>Price Modification Approval Pending!!</font>";
							}
							if(balance_qty<0)		//PB20250124: For inserting the negative balance quantity remark
							{
								remark="<font color='red'><b>MMBTU Allocated exceeds MMBTU Avail for Sale!!</b></font>";
							}
						}

						VREMARK.add(remark);
					}
					rset.close();
					stmt.close();

					//PB20250218: Added for including pseudo cargo as Previous purchase mapping.
					if(is_tcq_modification.equals("Y"))
					{
						if(is_parent_cargo_list.equals("Y"))
						{
							int temp_index=getOwnAccountDtl();
							index+=temp_index;

							int temp_index_pseudo = getPseudoCargoDetails();
							index+=temp_index_pseudo;
						}
					}
					VINDEX.add(index);
					VTOTAL_BALANCE_MMBTU.add(nf.format(total_balance_mmbtu));
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	//PRATHAM BHATT 20250213: FOR PSEUDO CARGO DETAILS
	public int getPseudoCargoDetails()
	{
		String function_nm = "getPseudoCargoDetails()";
		int  index = 0;
		double total_balance_mmbtu = 0;
		try
		{

			String pseudoQuantity = "SELECT SUM(QTY)FROM FMS_PSEUDO_CARGO_DTL B WHERE A.COMPANY_CD = B.COMPANY_CD "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO";

			String allocated = "SELECT NVL(SUM(ALLOC_QTY),0) "
					+ "FROM FMS_SUPPLY_PURCHASE_MAP_DTL C "
					+ "WHERE C.COMPANY_CD=A.COMPANY_CD AND C.PUR_CONT_NO=0 AND C.CARGO_NO=A.CARGO_NO AND C.CARGO_NO!=0 ";

			String queryString = "SELECT DISTINCT A.COMPANY_CD,A.CONTRACT_TYPE,A.CARGO_NO,"
					+ "("+pseudoQuantity+") PSEUDO_QTY,"
					+ "("+allocated+")  ALLOCATED_QTY "
					+ "FROM FMS_PSEUDO_CARGO_DTL A  "
					+ "WHERE A.COMPANY_CD=? ";
			if(is_tcq_modification.equals("Y"))
			{
				if(is_parent_cargo_list.equals("Y"))
				{
					queryString+="AND A.CARGO_NO IN (SELECT DISTINCT(CARGO_NO) FROM FMS_SUPPLY_PURCHASE_MAP_DTL D "
							+ "WHERE D.COMPANY_CD = A.COMPANY_CD AND D.COUNTERPARTY_CD=? "
							+ "AND D.AGMT_NO=? AND D.CONT_NO=? AND D.CONTRACT_TYPE=? AND D.PUR_CONT_NO=?)";
				}
				else
				{
					queryString+="AND A.CARGO_NO NOT IN (SELECT DISTINCT(CARGO_NO) FROM FMS_SUPPLY_PURCHASE_MAP_DTL D "
							+ "WHERE D.COMPANY_CD = A.COMPANY_CD AND D.COUNTERPARTY_CD=? "
							+ "AND D.AGMT_NO=? AND D.CONT_NO=? AND D.CONTRACT_TYPE=? AND D.PUR_CONT_NO=?)";
				}
			}
			queryString+="ORDER BY CARGO_NO";
			String temp_query = queryString;
			int ctn=0;
			stmt = conn.prepareStatement(temp_query);
			stmt.setString(++ctn, comp_cd);
			if(is_tcq_modification.equals("Y"))
			{
				stmt.setString(++ctn,counterparty_cd);
				stmt.setString(++ctn,agmt_no);
				stmt.setString(++ctn,cont_no);
				stmt.setString(++ctn,contract_type);
				stmt.setString(++ctn,"0");		//Since the contract number of the pseudo cargo is 0
			}
			rset = stmt.executeQuery();
			while(rset.next())
			{
				index++;
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String cont_type=rset.getString(2)==null?"":rset.getString(2);
				String cargo_no = rset.getString(3)==null?"":rset.getString(3);
				double pseudo_qty = rset.getDouble(4);
				double allocated_qty = rset.getDouble(5);

				double balance_qty = pseudo_qty-allocated_qty;
				total_balance_mmbtu+=balance_qty;

				if(callFlag.equalsIgnoreCase("MMBTU_ALLOCATION_TRANSFER"))
				{
					//PB20250102
					if(!multiContNo.equals(""))
					{
						if(multiCargoNo.equals(cargo_no) && multiCountpty.equals("0"))
						{
							continue;
						}
					}
					if(balance_qty<0)
					{
						continue;
					}
				}

				//String purchase_map_id=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtNo, agmtRev, contNo, contRev, cont_type, cargoNo);
				String purchase_map_id=""+own_cd+""+cont_type+"-"+cargo_no;
				VPURCHASE_MAP_ID.add(purchase_map_id);		//display mapping needs to created for pseudo cargo

				VCARGO_NO.add(cargo_no);
				VBOOKED_QTY.add(nf.format(pseudo_qty));
				VCONTRACT_TYPE.add(cont_type);
				VCONTRACT_TYPE_NM.add(""+utilBean.getContractTypeName(cont_type));
				/*if(cont_type.equals("D"))
				{
					VCONTRACT_TYPE_NM.add("Domestic NG");
				}
				else if(cont_type.equals("I"))
				{
					VCONTRACT_TYPE_NM.add("IGX");
				}
				else if(cont_type.equals("N"))
				{
					VCONTRACT_TYPE_NM.add("LNG");
				}
				else if(cont_type.equals("T"))
				{
					VCONTRACT_TYPE_NM.add("In Tank LNG|RLNG");
				}
				else
				{
					VCONTRACT_TYPE_NM.add("");
				}*/
				VALLOCATED_QTY.add(nf.format(allocated_qty));
				VBALANCE_QTY.add(nf.format(balance_qty));

				if(is_tcq_modification.equals("Y"))
				{
					if(is_parent_cargo_list.equals("Y"))
					{
						double contQty=getContractWiseAllocatedMMBTU(counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "0",cargo_no);
						VALLOC_QTY_CONTRACT_WISE.add(nf.format(contQty));
					}
					else
					{
						VALLOC_QTY_CONTRACT_WISE.add("0.00");
					}
				}

				//The null or 0 value vectors are stored here
				VCOUNTERPARTY_CD.add("0");
				VCOUNTERPARTY_ABBR.add("");
				VCOUNTERPARTY_NM.add("");
				VCONT_NO.add("0");
				VCONT_REV_NO.add("0");
				VAGMT_NO.add("0");
				VAGMT_REV_NO.add("0");
				VCONT_REF.add("");
				VAVAIL_FOR_SALE_QTY.add("");
				VAVAIL_FOR_SALE_QTY_INFO.add("");
				VSTART_DT.add("");
				VEND_DT.add("");
				VCONT_NAME.add("");
				VRATE.add("0");
				VRATE_UNIT.add("1");
				VRATE_UNIT_NM.add("");
				VPRICE_TYPE.add("");
				VCONT_STATUS_FLG.add("");
				VCARGO_STATUS_FLG.add("");
				VCONT_STATUS.add("");
				VUNLOADED_QTY.add("");
				VUNLOADED_QTY_INFO.add("");
				VMIN_ALLOC_DT.add("");
				VMAX_ALLOC_DT.add("");
				VREMARK.add("");
				VBALANCE_QTY_INFO.add("");
			}
			rset.close();
			stmt.close();

			VTOTAL_BALANCE_MMBTU.add(nf.format(total_balance_mmbtu));
			if(!is_parent_cargo_list.equals("Y"))
			{
				VINDEX.add(index);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return index;
	}

	//PB20250303: FOR OWN VOLUME ACCOUNT
	public int getOwnAccountDtl()
	{
		String function_nm = "getOwnAccountDtl()";
		int index=0;
		int parent_ctn=0;
		try
		{
			double proj_mmbtu=0;
			double available_mmbtu=0;
			double allocated_mmbtu=0;
			double balance_mmbtu=0;
			boolean flag=true;	//USED FOR HANDLING MMBTU ALLOCATION TRANSFER AND TCQ MODIFICATION

			if(callFlag.equalsIgnoreCase("MMBTU_ALLOCATION_TRANSFER"))
			{
				if(!multiContNo.equals(""))
				{
					if(multiCargoNo.equals("0")&&multiCountpty.equals("0"))
					{
						flag=false;
					}
				}
			}

			String sysdate = utilDate.getSysdate();

			String common_mod_sug="FROM FMS_LTCORA_CONT_CARGO_MOD B WHERE B.COMPANY_CD = A.COMPANY_CD AND  B.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
					+ "AND B.BUY_SALE=A.BUY_SALE AND B.AGMT_TYPE = A.AGMT_TYPE AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.CONT_NO=A.CONT_NO AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.CARGO_NO=A.CARGO_NO AND B.APPROVAL_FLAG='Y' "
					+ "AND B.CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_MOD C WHERE B.COMPANY_CD = C.COMPANY_CD "
					+ "AND B.COUNTERPARTY_CD = C.COUNTERPARTY_CD AND B.BUY_SALE=C.BUY_SALE AND B.AGMT_TYPE = C.AGMT_TYPE "
					+ "AND B.CONT_NO=C.CONT_NO AND B.CONTRACT_TYPE=C.CONTRACT_TYPE AND B.CARGO_NO=C.CARGO_NO AND B.APPROVAL_FLAG='Y')";

			String common_sug = "FROM FMS_LTCORA_CONT_MST B  WHERE A.COMPANY_CD = B.COMPANY_CD AND B.BUY_SALE=A.BUY_SALE AND B.AGMT_TYPE = A.AGMT_TYPE "
					+ "AND B.AGMT_NO=A.AGMT_NO AND B.CONT_NO=A.CONT_NO AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST C "
					+ "WHERE B.COMPANY_CD = C.COMPANY_CD AND  B. COUNTERPARTY_CD = C.COUNTERPARTY_CD AND B.BUY_SALE=C.BUY_SALE AND B.AGMT_TYPE = C.AGMT_TYPE "
					+ "AND B.CONT_NO=C.CONT_NO AND B.CONTRACT_TYPE=C.CONTRACT_TYPE )";

			String count_mod_sug = "SELECT COUNT(*) "+common_mod_sug;

			String mod_sug = "SELECT SUG "+common_mod_sug;

			String sug_per ="SELECT SUG "+common_sug;

			String final_sug = "CASE WHEN ("+count_mod_sug+") > 0 THEN ("+mod_sug+") ELSE ("+sug_per+") END ";

			String avail="SELECT  SUM(ADQ_QTY) "
					+ "FROM FMS_LTCORA_CONT_CARGO_ADQ B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE "
					+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONT_REV = B.CONT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO =  B.CARGO_NO ";

			String allocated = "SELECT NVL(SUM(ALLOC_QTY),0) ALLOCATED "
					+ "FROM FMS_SUPPLY_PURCHASE_MAP_DTL C "
					+ "WHERE C.COMPANY_CD=? AND C.PUR_CONT_NO=0 AND C.CARGO_NO=0 ";

			//for generating projected quantity
			String projected = "SELECT SUM(EDQ_QTY * ("+final_sug+")/100) PROJECTED "
					+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
					+ "WHERE COMPANY_CD=? AND BUY_SALE=? AND AGMT_TYPE=? "
					+ "AND CONTRACT_TYPE IN (?,?) AND "
					+ "CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL C "
					+ "WHERE A.COMPANY_CD = C.COMPANY_CD AND A.COUNTERPARTY_CD = C.COUNTERPARTY_CD AND A.BUY_SALE=C.BUY_SALE AND A.AGMT_TYPE = C.AGMT_TYPE "
					+ "AND A.CONT_NO=C.CONT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.CARGO_NO=C.CARGO_NO) "
					+ "AND ACTUAL_RECPT_DT>TO_DATE(?,'DD/MM/YYYY')";

			//for generating available quantity
			String avial_for_sale = "SELECT SUM(("+avail+")*("+final_sug+")/100) ";
			if(comp_cd.equals("2"))
			{
				avial_for_sale+="+ "+initial_vol2+" ";
			}
			avial_for_sale+= " AVAILABLE "
					+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
					+ "WHERE COMPANY_CD=? AND BUY_SALE=? AND AGMT_TYPE=? "
					+ "AND CONTRACT_TYPE IN (?,?) AND "
					+ "CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL C "
					+ "WHERE A.COMPANY_CD = C.COMPANY_CD AND A.COUNTERPARTY_CD = C.COUNTERPARTY_CD AND A.BUY_SALE=C.BUY_SALE AND A.AGMT_TYPE = C.AGMT_TYPE "
					+ "AND A.CONT_NO=C.CONT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.CARGO_NO=C.CARGO_NO) "
					+ "AND ACTUAL_RECPT_DT<=TO_DATE(?,'DD/MM/YYYY') ";

			//QUERY FOR CHECKING THE OWN VOLUME ACCOUNT IS PRESENT IN PARENT CARGO LIST
			String queryString = "SELECT COUNT(*) PARENT_COUNT  "
					+ "FROM FMS_SUPPLY_PURCHASE_MAP_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND CONT_NO=?  AND CONTRACT_TYPE=? "
					+ "AND PUR_CONT_NO=? AND CARGO_NO=?";

			String query = "SELECT A.PROJECTED,B.AVAILABLE,C.ALLOCATED,E.PARENT_COUNT  "
					+ "FROM ("+projected+") A,("+avial_for_sale+") B,("+allocated+") C,("+queryString+") E";

			String temp_query = query;
			int ctn=0;
			stmt2 = conn.prepareStatement(temp_query);
			stmt2.setString(++ctn,comp_cd);
			stmt2.setString(++ctn,"C");
			stmt2.setString(++ctn,"A");
			stmt2.setString(++ctn,"O");
			stmt2.setString(++ctn,"Q");
			stmt2.setString(++ctn,sysdate);
			stmt2.setString(++ctn,comp_cd);
			stmt2.setString(++ctn,"C");
			stmt2.setString(++ctn,"A");
			stmt2.setString(++ctn,"O");
			stmt2.setString(++ctn,"Q");
			stmt2.setString(++ctn,sysdate);
			stmt2.setString(++ctn,comp_cd);
			stmt2.setString(++ctn,comp_cd);
			stmt2.setString(++ctn,counterparty_cd);
			stmt2.setString(++ctn,agmt_no);
			stmt2.setString(++ctn,cont_no);
			stmt2.setString(++ctn,contract_type);
			stmt2.setString(++ctn,"0");
			stmt2.setString(++ctn,"0");
			rset2 = stmt2.executeQuery();
			if(rset2.next())
			{
				parent_ctn=rset2.getInt(4);
				if(is_tcq_modification.equals("Y"))
				{
					if(parent_ctn>0)
					{
						if(is_parent_cargo_list.equals("Y"))
						{
							double contQty=getContractWiseAllocatedMMBTU(counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "0","0");
							VALLOC_QTY_CONTRACT_WISE.add(nf.format(contQty));
						}
						else
						{
							flag=false;
							//VALLOC_QTY_CONTRACT_WISE.add(nf.format(0));
						}
					}
					else
					{
						if(is_parent_cargo_list.equals("Y"))
						{
							flag=false;
						}
						else
						{
							flag=true;
							VALLOC_QTY_CONTRACT_WISE.add(nf.format(0));
						}
					}
				}

				if(flag)
				{
					index++;
					proj_mmbtu = rset2.getDouble(1);
					available_mmbtu = rset2.getDouble(2);
					allocated_mmbtu=rset2.getDouble(3);
					//available_mmbtu=rset2.getDouble(5);

					//FOR GETTING THE INTERNAL CONSUMPTION
					double internal_consume = 0;
					String internal_consumption = "SELECT SUM(NVL(TOTAL_CONSUMPTION,0) - NVL(SUG,0)) CONSUME "
							+ "FROM FMS_TANK_INTRNL_CONSUMPTION "
							+ "WHERE COMPANY_CD=? "
							+ "AND TO_DATE('01'||'-'||TO_CHAR(MONTH)||'-'||TO_CHAR(YEAR),'DD/MM/YYYY') <= TO_DATE(?,'DD/MM/YYYY') ";
					stmt3 = conn.prepareStatement(internal_consumption);
					stmt3.setString(1,comp_cd);
					stmt3.setString(2,sysdate);
					rset3 = stmt3.executeQuery();
					if(rset3.next())
					{
						internal_consume = rset3.getDouble(1);
					}
					rset3.close();
					stmt3.close();
					//double available_for_sale = (available_mmbtu) - (((available_mmbtu)*losses_percentage)/100);
					//double available_for_sale = (available_mmbtu)- (((available_mmbtu)*sug_percent)/100);
					//available_for_sale=available_for_sale+816900;

					double available=available_mmbtu-internal_consume;
					String available_for_sale_info = "availabe - total consumption\n"+""+nf.format(available)+"-"+nf.format(internal_consume);

					balance_mmbtu = (available + proj_mmbtu) - allocated_mmbtu;
					String balance_mmbtu_info = "(Projected MMBTU + MMBTU Avail for Sale)-MMBTU Allocated \n("+nf.format(proj_mmbtu)+"+"+nf.format(available_mmbtu)+")-"+nf.format(allocated_mmbtu)+"="+nf.format(balance_mmbtu);

					String purchase_map_id="Own Voulme";

					VPURCHASE_MAP_ID.add(purchase_map_id);
					VCARGO_NO.add("0");
					VBOOKED_QTY.add(nf.format(proj_mmbtu));
					VAVAIL_FOR_SALE_QTY.add(nf.format(available));
					VAVAIL_FOR_SALE_QTY_INFO.add(available_for_sale_info);

					VALLOCATED_QTY.add(nf.format(allocated_mmbtu));
					VBALANCE_QTY.add(nf.format(balance_mmbtu));
					VBALANCE_QTY_INFO.add(balance_mmbtu_info);

					//The null or 0 value vectors are stored here
					VCONTRACT_TYPE.add("0");
					VCONTRACT_TYPE_NM.add("");
					VCOUNTERPARTY_CD.add("0");
					VCOUNTERPARTY_ABBR.add("");
					VCOUNTERPARTY_NM.add("");
					VCONT_NO.add("0");
					VCONT_REV_NO.add("0");
					VAGMT_NO.add("0");
					VAGMT_REV_NO.add("0");
					VCONT_REF.add("");
					VSTART_DT.add("");
					VEND_DT.add("");
					VCONT_NAME.add("");
					VRATE.add("0");
					VRATE_UNIT.add("1");
					VRATE_UNIT_NM.add("");
					VPRICE_TYPE.add("");
					VCONT_STATUS_FLG.add("");
					VCARGO_STATUS_FLG.add("");
					VCONT_STATUS.add("");
					VUNLOADED_QTY.add("");
					VUNLOADED_QTY_INFO.add("");
					VMIN_ALLOC_DT.add("");
					VMAX_ALLOC_DT.add("");
					VREMARK.add("");
				}
			}
			rset2.close();
			stmt2.close();
			VTOTAL_BALANCE_MMBTU.add(nf.format(balance_mmbtu));
			if(!is_parent_cargo_list.equals("Y"))
			{
				VINDEX.add(index);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return index;
	}

	public String ContStatusName(String status_flg)
	{
		String function_nm="ContStatusName()";
		String nm="";
		try
		{
			if(status_flg.equals("F"))
			{
				nm="New";
			}
			else if(status_flg.equals("P"))
			{
				nm="Pending Approval";
			}
			else if(status_flg.equals("Y"))
			{
				nm="Approved";
			}
			else if(status_flg.equals("N"))
			{
				nm="Not Approved";
			}
			else if(status_flg.equals("X"))
			{
				nm="Canceled";
			}
			else if(status_flg.equals("C"))
			{
				nm="Closed";
			}
			else if(status_flg.equals("R"))
			{
				nm="Re-Opened";
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return nm;
	}

	public void getCustomerListForAllocation()
	{
		String function_nm="getCustomerListForAllocation()";
		try
		{
			String queryString="SELECT DISTINCT A.COUNTERPARTY_CD, C.COUNTERPARTY_NM,C.COUNTERPARTY_ABBR "
					+ "FROM FMS_SUPPLY_CONT_MST A, FMS_COUNTERPARTY_MST C "
					+ "WHERE A.COMPANY_CD=? "
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) AND IS_ALLOCATED='N' "
					+ "AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
					+ "AND C.EFF_DT=(SELECT MAX(D.EFF_DT) FROM FMS_COUNTERPARTY_MST D WHERE D.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
					+ "AND D.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) "
					+ "AND A.CONT_STATUS NOT IN ('X','T','C') ";	//PB20251106: NOT ALLOWING CANCEL, TERMINATED AND CLOSED CONTRACT FOR ALLOCATION  
			if(clearance.equals("IGX"))
			{
				queryString+="AND CONTRACT_TYPE IN ('X','W') ";
			}
			else
			{
				queryString+="AND CONTRACT_TYPE IN ('S','L','E','F') ";
			}
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VCUSTOMER_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VCUSTOMER_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VCUSTOMER_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getTraderContractListForPriceChange()
	{
		String function_nm="getTraderContractListForPriceChange()";
		try
		{
			String[] tempCountpty=multiCountpty.split("@");
			String[] tempAgmtNo=multiAgmtNo.split("@");
			String[] tempAgmtRev=multiAgmtRev.split("@");
			String[] tempContNo=multiContNo.split("@");
			String[] tempContRev=multiContRev.split("@");
			String[] tempContTyp=multiContTyp.split("@");

			for(int i=0; i<tempCountpty.length; i++)
			{
				String queryString="SELECT COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),"
						+ "RATE,RATE_UNIT,CONT_REF_NO,TRADE_REF_NO "
						+ "FROM FMS_TRADER_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
						+ "AND AGMT_REV=? AND CONT_NO=? AND CONTRACT_TYPE=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, tempCountpty[i]);
				stmt.setString(3, tempAgmtNo[i]);
				stmt.setString(4, tempAgmtRev[i]);
				stmt.setString(5, tempContNo[i]);
				stmt.setString(6, tempContTyp[i]);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String counterpty_cd = rset.getString(1)==null?"":rset.getString(1);
					String agmt = rset.getString(2)==null?"":rset.getString(2);
					String agmt_rev = rset.getString(3)==null?"":rset.getString(3);
					String cont = rset.getString(4)==null?"":rset.getString(4);
					String cont_rev = rset.getString(5)==null?"":rset.getString(5);
					String cont_type = rset.getString(6)==null?"":rset.getString(6);
					String start_dt = rset.getString(7)==null?"":rset.getString(7);
					String end_dt = rset.getString(8)==null?"":rset.getString(8);
					String rate = ""+rset.getDouble(9);
					String rate_unit = rset.getString(10)==null?"":rset.getString(10);
					String cont_ref = rset.getString(11)==null?"":rset.getString(11);
					String trade_ref = rset.getString(12)==null?"":rset.getString(12);

					VCOUNTERPARTY_CD.add(counterpty_cd);
					VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterpty_cd));
					VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,counterpty_cd));
					VAGMT_NO.add(agmt);
					VAGMT_REV_NO.add(agmt_rev);
					VCONT_NO.add(cont);
					VCONT_REV_NO.add(cont_rev);
					VCONTRACT_TYPE.add(cont_type);

					VCONTRACT_TYPE_NM.add(""+utilBean.getContractTypeName(cont_type));
					/*if(cont_type.equals("D"))
					{
						VCONTRACT_TYPE_NM.add("Domestic NG");
					}
					else if(cont_type.equals("I"))
					{
						VCONTRACT_TYPE_NM.add("IGX");
					}
					else if(cont_type.equals("N"))
					{
						VCONTRACT_TYPE_NM.add("LNG");
					}
					else if(cont_type.equals("T"))
					{
						VCONTRACT_TYPE_NM.add("In Tank LNG|RLNG");
					}
					else
					{
						VCONTRACT_TYPE_NM.add("");
					}*/

					VSTART_DT.add(start_dt);
					VEND_DT.add(end_dt);
					VRATE.add(""+utilBean.RateNumberFormat(Double.parseDouble(rate), rate_unit));
					VRATE_UNIT.add(rate_unit);
					VRATE_UNIT_NM.add(""+utilBean.getRateUnitNm(conn,rate_unit));

					String purchase_map_id=utilBean.NewDealMappingId(comp_cd, counterpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
					VDISPLAY_CONT_DTL.add(purchase_map_id);

					if(cont_type.equals("I"))
					{
						cont_ref=trade_ref;
					}
					VCONT_REF.add(cont_ref);

					String queryString1="SELECT NEW_PRICE,SEQ_NO,FLAG,TO_CHAR(EFF_DT,'DD/MM/YYYY') "
							+ "FROM FMS_TRADER_CONT_PRICE_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND CONT_NO=? "
							+ "AND CONTRACT_TYPE=? AND FLAG=? "
							+ "AND SEQ_NO=(SELECT MAX(B.SEQ_NO) FROM FMS_TRADER_CONT_PRICE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterpty_cd);
					stmt1.setString(3, agmt);
					stmt1.setString(4, cont);
					stmt1.setString(5, cont_type);
					stmt1.setString(6, "R");
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						double new_rate=rset1.getDouble(1);
						VNEW_RATE.add(""+utilBean.RateNumberFormat(new_rate, rate_unit));
						VCHANGE_SEQ_NO.add(rset1.getString(2)==null?"":rset1.getString(2));
						VFLAG.add(rset1.getString(3)==null?"":rset1.getString(3));
						VNEW_EFF_DATE.add(rset1.getString(4)==null?"":rset1.getString(4));
					}
					else
					{
						VNEW_RATE.add("");
						VCHANGE_SEQ_NO.add("");
						VFLAG.add("");
						VNEW_EFF_DATE.add("");
					}
					rset1.close();
					stmt1.close();
				}
				rset.close();
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getSelectedPurchaseConttactList()
	{
		String function_nm="getSelectedPurchaseConttactList()";
		try
		{
			String sysdate = utilDate.getSysdate();
			String[] tempCountpty=multiCountpty.split("@@");
			String[] tempAgmtNo=multiAgmtNo.split("@@");
			String[] tempAgmtRev=multiAgmtRev.split("@@");
			String[] tempContNo=multiContNo.split("@@");
			String[] tempContRev=multiContRev.split("@@");
			String[] tempContTyp=multiContTyp.split("@@");
			String[] tempCargoNo=multiCargoNo.split("@@");
			String purchase_map_id="";

			//owner_cd+"-"+counterparty_cd+"-"+VAGMT_TYPE.elementAt(i)+"-"+VAGMT_NO.elementAt(i)+"-"+VAGMT_REV_NO.elementAt(i)
			//+"-"+VCONTRACT_TYPE.elementAt(i)+"-"+VCONT_NO.elementAt(i)+"-"+VCONT_REV_NO.elementAt(i)+"-"+VCARGO_NO.elementAt(i);

			for(int i=0; i<tempCountpty.length; i++)
			{
				//for own volume
				String common_mod_sug="FROM FMS_LTCORA_CONT_CARGO_MOD D WHERE D.COMPANY_CD = E.COMPANY_CD AND  D.COUNTERPARTY_CD = E.COUNTERPARTY_CD "
						+ "AND D.BUY_SALE=E.BUY_SALE AND D.AGMT_TYPE = E.AGMT_TYPE AND D.AGMT_NO=E.AGMT_NO "
						+ "AND D.CONT_NO=E.CONT_NO AND D.CONTRACT_TYPE=E.CONTRACT_TYPE AND D.CARGO_NO=E.CARGO_NO AND D.APPROVAL_FLAG='Y' "
						+ "AND D.CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_MOD C WHERE D.COMPANY_CD = C.COMPANY_CD "
						+ "AND D.COUNTERPARTY_CD = C.COUNTERPARTY_CD AND D.BUY_SALE=C.BUY_SALE AND D.AGMT_TYPE = C.AGMT_TYPE "
						+ "AND D.CONT_NO=C.CONT_NO AND D.CONTRACT_TYPE=C.CONTRACT_TYPE AND D.CARGO_NO=C.CARGO_NO AND D.APPROVAL_FLAG='Y')";

				String common_sug = "FROM FMS_LTCORA_CONT_MST B  WHERE E.COMPANY_CD = B.COMPANY_CD AND B.BUY_SALE=E.BUY_SALE AND B.AGMT_TYPE = E.AGMT_TYPE "
						+ "AND B.AGMT_NO=E.AGMT_NO AND B.CONT_NO=E.CONT_NO AND B.CONTRACT_TYPE=E.CONTRACT_TYPE AND B.CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST C "
						+ "WHERE B.COMPANY_CD = C.COMPANY_CD AND  B.COUNTERPARTY_CD = C.COUNTERPARTY_CD AND B.BUY_SALE=C.BUY_SALE AND B.AGMT_TYPE = C.AGMT_TYPE "
						+ "AND B.CONT_NO=C.CONT_NO AND B.CONTRACT_TYPE=C.CONTRACT_TYPE )";

				String count_mod_sug = "SELECT COUNT(*) "+common_mod_sug;

				String mod_sug = "SELECT SUG "+common_mod_sug;

				String sug_per ="SELECT SUG "+common_sug;

				String final_sug = "CASE WHEN ("+count_mod_sug+") > 0 THEN ("+mod_sug+") ELSE ("+sug_per+") END ";

				sug_percent=comp_cd.equals("1")?0.66:0;
				String final_sug1 = "CASE WHEN E.ATTACH_LNG_CARGO=(TO_CHAR(B.COMPANY_CD)||'-'||TO_CHAR(B.COUNTERPARTY_CD)||'-'||TO_CHAR(B.AGMT_TYPE)||'-'||TO_CHAR(B.AGMT_NO)||'-'||TO_CHAR(B.AGMT_REV)||'-'||TO_CHAR(B.CONTRACT_TYPE)||'-'||TO_CHAR(B.CONT_NO)||'-'||TO_CHAR(B.CONT_REV)||'-'||TO_CHAR(B.CARGO_NO)) THEN ("+final_sug+") ELSE "+sug_percent+" END";

				String avail="SELECT  SUM(ADQ_QTY) "
						+ "FROM FMS_LTCORA_CONT_CARGO_ADQ B "
						+ "WHERE E.COMPANY_CD=B.COMPANY_CD AND E.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND E.BUY_SALE=B.BUY_SALE AND E.AGMT_NO=B.AGMT_NO AND E.AGMT_TYPE=B.AGMT_TYPE "
						+ "AND E.AGMT_REV=B.AGMT_REV AND E.CONT_NO=B.CONT_NO AND E.CONT_REV = B.CONT_REV "
						+ "AND E.CONTRACT_TYPE=B.CONTRACT_TYPE AND E.CARGO_NO =  B.CARGO_NO ";

				String own_allocated = "SELECT NVL(SUM(ALLOC_QTY),0) ALLOCATED "
						+ "FROM FMS_SUPPLY_PURCHASE_MAP_DTL C "
						+ "WHERE C.COMPANY_CD=? AND C.PUR_CONT_NO=0 AND C.CARGO_NO=0 ";

				//for generating projected quantity
				String projected = "SELECT SUM(EDQ_QTY * ("+final_sug+")/100) PROJECTED "
						+ "FROM FMS_LTCORA_CONT_CARGO_DTL E "
						+ "WHERE COMPANY_CD=? AND BUY_SALE=? AND AGMT_TYPE=? "
						+ "AND CONTRACT_TYPE IN (?,?) AND "
						+ "CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL C "
						+ "WHERE E.COMPANY_CD = C.COMPANY_CD AND E.COUNTERPARTY_CD = C.COUNTERPARTY_CD AND E.BUY_SALE=C.BUY_SALE AND E.AGMT_TYPE = C.AGMT_TYPE "
						+ "AND E.CONT_NO=C.CONT_NO AND E.CONTRACT_TYPE=C.CONTRACT_TYPE AND E.CARGO_NO=C.CARGO_NO) "
						+ "AND ACTUAL_RECPT_DT>TO_DATE(?,'DD/MM/YYYY')";

				//for generating available quantity
				String avial_for_sale = "SELECT SUM(("+avail+")*("+final_sug+")/100) ";
				if(comp_cd.equals("2"))
				{
					avial_for_sale+="+ "+initial_vol2+" ";
				}
				avial_for_sale+= " AVAILABLE "
						+ "FROM FMS_LTCORA_CONT_CARGO_DTL E "
						+ "WHERE COMPANY_CD=? AND BUY_SALE=? AND AGMT_TYPE=? "
						+ "AND CONTRACT_TYPE IN (?,?) AND "
						+ "CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL C "
						+ "WHERE E.COMPANY_CD = C.COMPANY_CD AND E.COUNTERPARTY_CD = C.COUNTERPARTY_CD AND E.BUY_SALE=C.BUY_SALE AND E.AGMT_TYPE = C.AGMT_TYPE "
						+ "AND E.CONT_NO=C.CONT_NO AND E.CONTRACT_TYPE=C.CONTRACT_TYPE AND E.CARGO_NO=C.CARGO_NO) "
						+ "AND ACTUAL_RECPT_DT<=TO_DATE(?,'DD/MM/YYYY')";

				String query = "SELECT A.PROJECTED,B.AVAILABLE,C.ALLOCATED FROM "
						+ "("+projected+") A,("+avial_for_sale+") B,("+own_allocated+") C";

				//for pseudo cargo
				String pseudoQuantity = "SELECT SUM(QTY)FROM FMS_PSEUDO_CARGO_DTL B WHERE A.COMPANY_CD = B.COMPANY_CD "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO";

				String pseudo_allocated = "SELECT NVL(SUM(ALLOC_QTY),0) "
						+ "FROM FMS_SUPPLY_PURCHASE_MAP_DTL C "
						+ "WHERE C.COMPANY_CD=A.COMPANY_CD AND C.PUR_CONT_NO=0 AND C.CARGO_NO=A.CARGO_NO ";

				String queryString1 = "SELECT DISTINCT A.COMPANY_CD,A.CONTRACT_TYPE,A.CARGO_NO,"
						+ "("+pseudoQuantity+") PSEUDO_QTY,"
						+ "("+pseudo_allocated+")  ALLOCATED_QTY "
						+ "FROM FMS_PSEUDO_CARGO_DTL A  "
						+ "WHERE A.COMPANY_CD=? "
						+ "AND A.CARGO_NO=? AND A.CONTRACT_TYPE=?";

				String commonQry = "FROM FMS_BUY_DAILY_ALLOCATION C "
		  				+ "WHERE A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
		  				+ "AND A.CONT_NO=C.CONT_NO AND A.AGMT_NO=C.AGMT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE "
		  				+ "AND C.GAS_DT >= A.START_DT AND C.GAS_DT <= A.END_DT "
		  				+ "AND NOM_REV_NO=(SELECT MAX(B.NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
						+ "WHERE B.CONT_NO=C.CONT_NO AND B.AGMT_NO=C.AGMT_NO "
						+ "AND B.COMPANY_CD=C.COMPANY_CD AND B.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=C.TRANSPORTER_CD AND B.TRANS_SEQ=C.TRANS_SEQ AND B.BU_SEQ=C.BU_SEQ "
						+ "AND B.PLANT_SEQ=C.PLANT_SEQ AND B.CONTRACT_TYPE=C.CONTRACT_TYPE "
						+ "AND B.GAS_DT=C.GAS_DT AND B.CARGO_NO=C.CARGO_NO) ";

				String commonQry1 = "FROM FMS_BUY_CARGO_ALLOC C "
		  				+ "WHERE A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.AGMT_TYPE=C.AGMT_TYPE "
		  				+ "AND A.CONT_NO=C.CONT_NO AND A.AGMT_NO=C.AGMT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND B.CARGO_NO=C.CARGO_NO "
		  				+ "AND C.ALLOC_REV=(SELECT MAX(B.ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC B "
						+ "WHERE B.CONT_NO=C.CONT_NO AND B.AGMT_NO=C.AGMT_NO AND B.AGMT_TYPE=C.AGMT_TYPE AND B.CARGO_NO=C.CARGO_NO "
						+ "AND B.COMPANY_CD=C.COMPANY_CD AND B.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND B.CONTRACT_TYPE=C.CONTRACT_TYPE) ";

				String qty="SELECT NVL(SUM(C.QTY_MMBTU),0) "+commonQry;
				String qty1="SELECT NVL(C.QQ_QTY_MMBTU,0) "+commonQry1;

				String min_dt="SELECT TO_CHAR(MIN(C.GAS_DT),'DD/MM/YYYY') "+commonQry;
				String min_dt1="SELECT TO_CHAR(MIN(C.QQ_DT),'DD/MM/YYYY') "+commonQry1;

				String max_dt="SELECT TO_CHAR(MAX(C.GAS_DT),'DD/MM/YYYY') "+commonQry;
				String max_dt1="SELECT TO_CHAR(MAX(C.QQ_DT),'DD/MM/YYYY') "+commonQry1;

				String alloc_count="SELECT COUNT(*) "+commonQry;
				String alloc_count1="SELECT COUNT(*) "+commonQry1;

				String allocated = "SELECT NVL(SUM(ALLOC_QTY),0) "
						+ "FROM FMS_SUPPLY_PURCHASE_MAP_DTL C "
						+ "WHERE C.COMPANY_CD=A.COMPANY_CD AND C.PUR_CONT_NO=A.CONT_NO ";

				String allocated1 = "SELECT NVL(SUM(ALLOC_QTY),0) "
						+ "FROM FMS_SUPPLY_PURCHASE_MAP_DTL C "
						+ "WHERE C.COMPANY_CD=A.COMPANY_CD AND C.PUR_CONT_NO=A.CONT_NO AND C.CARGO_NO=B.CARGO_NO ";

				String no_of_contDay="SELECT TO_DATE(A.END_DT,'DD/MM/YYYY') - TO_DATE(SYSDATE,'DD/MM/YYYY')+1 FROM DUAL";

				String projQty="(CASE WHEN ("+alloc_count+") > 0 THEN CASE WHEN ("+no_of_contDay+") > 0 THEN (A.DCQ * ("+no_of_contDay+")) ELSE 0 END ELSE 0 END)";

				String queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, CONT_NO, CONT_REV, TCQ, QUANTITY_UNIT, "
							+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),"
							+ "CONT_NAME,RATE,RATE_UNIT,CONT_STATUS,AGMT_NO,AGMT_REV,CONTRACT_TYPE,"
							+ "("+qty+") UNLOADED_QTY, "
							+ "("+alloc_count+") ALLOC_COUNT, "
							+ "("+allocated+") ALLOCATED_QTY, "
							+ "("+projQty+") PROJECTED_QTY, "
							+ "NULL, "
							+"CONT_REF_NO,TRADE_REF_NO,NULL, "	//PB20250101
							+ "("+min_dt+")," //PB20250101
							+ "("+max_dt+"),"
							+ "NULL " //FOR SUG
						+ "FROM FMS_TRADER_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
							+ "AND AGMT_REV=? AND CONT_NO=? AND CONTRACT_TYPE=? "
							+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				queryString+=" UNION ALL ";
				queryString+="SELECT A.COMPANY_CD, A.COUNTERPARTY_CD, A.CONT_NO, A.CONT_REV, B.CARGO_QTY, 1, "
						+ "TO_CHAR(B.START_DT,'DD/MM/YYYY'),TO_CHAR(B.END_DT,'DD/MM/YYYY'),"
						+ "A.CONT_NAME,B.RATE,B.RATE_UNIT,A.CONT_STATUS,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,"
						+ "("+qty1+") UNLOADED_QTY, "
						+ "("+alloc_count1+") ALLOC_COUNT, "
						+ "("+allocated1+") ALLOCATED_QTY, "
						+ "(NULL) PROJECTED_QTY, "
						+ "B.CARGO_NO, "
						+ "B.CARGO_REF,NULL,B.CARGO_STATUS, "		//PB20250101
						+ "("+min_dt1+")," //PB20250101
						+ "("+max_dt1+"),"
						+ "("+final_sug1+") " 	//FOR SUG
						+ "FROM FMS_TRADER_CN_MST A, FMS_TRADER_CARGO_MST B "
						+ "LEFT JOIN FMS_LTCORA_CONT_CARGO_DTL E "
						+ "ON  E.COMPANY_CD=B.COMPANY_CD "
						+ "AND E.ATTACH_LNG_CARGO=B.COMPANY_CD||'-'||B.COUNTERPARTY_CD||'-'||B.AGMT_TYPE||'-'||B.AGMT_NO||'-'||B.AGMT_REV||'-'||B.CONTRACT_TYPE||'-'||B.CONT_NO||'-'||B.CONT_REV||'-'||B.CARGO_NO "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? "
						+ "AND A.AGMT_REV=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND B.CARGO_NO=? "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV ";

				if(tempCountpty[i].equals("0") && !tempCargoNo[i].equals("0"))		//Counterparty_cd = 0 then Pseudo Cargo
				{
					String temp_query = queryString1;
					stmt=conn.prepareStatement(temp_query);
					stmt.setString(1, comp_cd);
					stmt.setString(2,tempCargoNo[i]);
					stmt.setString(3, tempContTyp[i]);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						String own_cd=rset.getString(1)==null?"":rset.getString(1);
						String cont_type=rset.getString(2)==null?"":rset.getString(2);
						String cargoNo=rset.getString(3)==null?"0":rset.getString(3);
						double pseudo_qty = rset.getDouble(4);
						double pseudo_alloc = rset.getDouble(5);
						double bal = pseudo_qty-pseudo_alloc;

						VSEL_CARGO_NO.add(cargoNo);
						VSEL_CONT_NO.add("0");
						VSEL_CONT_REV_NO.add("0");
						VSEL_AGMT_NO.add("0");
						VSEL_AGMT_REV_NO.add("0");
						VSEL_CONT_TYPE.add(cont_type);
						VSEL_CONT_TYPE_NM.add(""+utilBean.getContractTypeName(cont_type));
						/*String cont_nm="";
						if(cont_type.equals("D"))
						{
							cont_nm="Domestic NG";
							VSEL_CONT_TYPE_NM.add("Domestic NG");
						}
						else if(cont_type.equals("I"))
						{
							cont_nm="IGX";
							VSEL_CONT_TYPE_NM.add("IGX");
						}
						else if(cont_type.equals("N"))
						{
							cont_nm="LNG";
							VSEL_CONT_TYPE_NM.add("LNG");
						}
						else if(cont_type.equals("T"))
						{
							cont_nm="In Tank LNG|RLNG";
							VSEL_CONT_TYPE_NM.add("In Tank LNG|RLNG");
						}
						else
						{
							VSEL_CONT_TYPE_NM.add("");
						}*/
						purchase_map_id=""+comp_cd+""+cont_type+"-"+cargoNo;

						VSEL_DISPLAY_CONT_DTL.add(purchase_map_id);

						VSEL_BOOKED_QTY.add(nf.format(pseudo_qty));
						VSEL_ALLOCATED_QTY.add(nf.format(pseudo_alloc));
						VSEL_BALANCE_QTY.add(nf.format(bal));
						VSEL_AVAIL_QTY.add(nf.format(0));
						VSEL_RATE.add(""+utilBean.RateNumberFormat(0, "1"));
						VSEL_RATE_UNIT.add("1");
						VSEL_RATE_UNIT_NM.add(""+utilBean.getRateUnitNm(conn,"1"));
						VSEL_PRICE_TYPE.add("Fixed");
						VSEL_COLOR.add(color_pseudo);

						VSEL_MIN_ALLOC_DT.add("");
						VSEL_MAX_ALLOC_DT.add("");
						VSEL_START_DT.add("");
						VSEL_END_DT.add("");
						VSEL_CONT_STATUS_FLG.add("");
						VSEL_CARGO_STATUS_FLG.add("");
						VSEL_CONT_STATUS.add("");
						VSEL_UNLOADED_QTY.add("");
						VSEL_TRADER_ABBR.add("");
						VSEL_TRADER_NM.add("");
						VSEL_CONT_NAME.add("");
						VSEL_CONT_REF.add("");
					}
					rset.close();
					stmt.close();
				}
				else if(tempCountpty[i].equals("0") && tempCargoNo[i].equals("0"))		//Counterparty_cd = 0  and cargo_no = 0 then OWN Volume
				{
					String temp_query = query;
					stmt=conn.prepareStatement(temp_query);
					stmt.setString(1,comp_cd);
					stmt.setString(2,"C");
					stmt.setString(3,"A");
					stmt.setString(4,"O");
					stmt.setString(5,"Q");
					stmt.setString(6,sysdate);
					stmt.setString(7,comp_cd);
					stmt.setString(8,"C");
					stmt.setString(9,"A");
					stmt.setString(10,"O");
					stmt.setString(11,"Q");
					stmt.setString(12,sysdate);
					stmt.setString(13,comp_cd);
					rset = stmt.executeQuery();
					if(rset.next())
					{
						double own_projected = rset.getDouble(1);
						double own_available = rset.getDouble(2);
						double own_allocated_mmbtu = rset.getDouble(3);
						//double available_for_sale = (available_mmbtu) - (((available_mmbtu)*losses_percentage)/100);
						//((unloaded_vol+reconciled_qty) - (((unloaded_vol+reconciled_qty)*losses_percentage)/100))
						//double available_for_sale = (own_available)- (((own_available)*sug_percent)/100);
						//available_for_sale=available_for_sale+816900;

						double internal_consume=0;
						//FOR GETTING THE INTERNAL CONSUMPTION
						String internal_consumption = "SELECT NVL(TOTAL_CONSUMPTION,0) - NVL(SUG,0) CONSUME "
								+ "FROM FMS_TANK_INTRNL_CONSUMPTION "
								+ "WHERE COMPANY_CD=? AND YEAR=TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'YYYY') "
								+ "AND MONTH=TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'MM')";
						stmt3 = conn.prepareStatement(internal_consumption);
						stmt3.setString(1,comp_cd);
						stmt3.setString(2,sysdate);
						stmt3.setString(3,sysdate);
						rset3 = stmt3.executeQuery();
						if(rset3.next())
						{
							internal_consume=rset3.getDouble(1);
						}
						rset3.close();
						stmt3.close();

						double available = own_available-internal_consume;

						double balance_mmbtu = (available + own_projected) - own_allocated_mmbtu;


						VSEL_CARGO_NO.add("0");
						VSEL_CONT_NO.add("0");
						VSEL_CONT_REV_NO.add("0");
						VSEL_AGMT_NO.add("0");
						VSEL_AGMT_REV_NO.add("0");
						VSEL_CONT_TYPE.add("");
						VSEL_CONT_TYPE_NM.add("");
						purchase_map_id="Own Volume";

						VSEL_DISPLAY_CONT_DTL.add(purchase_map_id);

						VSEL_BOOKED_QTY.add(nf.format(own_projected));
						VSEL_ALLOCATED_QTY.add(nf.format(own_allocated_mmbtu));
						VSEL_BALANCE_QTY.add(nf.format(balance_mmbtu));
						VSEL_AVAIL_QTY.add(nf.format(available));
						VSEL_RATE.add(""+utilBean.RateNumberFormat(0, "1"));
						VSEL_RATE_UNIT.add("1");
						VSEL_RATE_UNIT_NM.add(""+utilBean.getRateUnitNm(conn,"1"));
						VSEL_PRICE_TYPE.add("Fixed");
						VSEL_COLOR.add(color_own);

						VSEL_MIN_ALLOC_DT.add("");
						VSEL_MAX_ALLOC_DT.add("");
						VSEL_START_DT.add("");
						VSEL_END_DT.add("");
						VSEL_CONT_STATUS_FLG.add("");
						VSEL_CARGO_STATUS_FLG.add("");
						VSEL_CONT_STATUS.add("");
						VSEL_UNLOADED_QTY.add("");
						VSEL_TRADER_ABBR.add("");
						VSEL_TRADER_NM.add("");
						VSEL_CONT_NAME.add("");
						VSEL_CONT_REF.add("");
					}
					rset.close();
					stmt.close();
				}
				else
				{
					String temp_queryString = queryString;
					stmt=conn.prepareStatement(temp_queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, tempCountpty[i]);
					stmt.setString(3, tempAgmtNo[i]);
					stmt.setString(4, tempAgmtRev[i]);
					stmt.setString(5, tempContNo[i]);
					stmt.setString(6, tempContTyp[i]);
					stmt.setString(7, comp_cd);
					stmt.setString(8, tempCountpty[i]);
					stmt.setString(9, tempAgmtNo[i]);
					stmt.setString(10, tempAgmtRev[i]);
					stmt.setString(11, tempContNo[i]);
					stmt.setString(12, tempContTyp[i]);
					stmt.setString(13, tempCargoNo[i]);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						String own_cd=rset.getString(1)==null?"":rset.getString(1);
						String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
						String contNo=rset.getString(3)==null?"0":rset.getString(3);
						String contRev=rset.getString(4)==null?"0":rset.getString(4);
						String agmtNo=rset.getString(13)==null?"0":rset.getString(13);
						String agmtRev=rset.getString(14)==null?"0":rset.getString(14);
						String cont_type=rset.getString(15)==null?"":rset.getString(15);
						String cargoNo=rset.getString(20)==null?"0":rset.getString(20);
						String contNM = rset.getString(9)==null?"":rset.getString(9);	//PB20250101
						String cont_ref = rset.getString(21)==null?"":rset.getString(21);	//PB20250101
						String trade_ref = rset.getString(22)==null?"":rset.getString(22);	//PB20250101

						//pb20250101
						VSEL_PRICE_TYPE.add("Fixed");
						VSEL_MIN_ALLOC_DT.add(rset.getString(24)==null?"":rset.getString(24));
						VSEL_MAX_ALLOC_DT.add(rset.getString(25)==null?"":rset.getString(25));
						VSEL_START_DT.add(rset.getString(7)==null?"":rset.getString(7));
						VSEL_END_DT.add(rset.getString(8)==null?"":rset.getString(8));
						String status_flg = rset.getString(12)==null?"":rset.getString(12);
						VSEL_CONT_STATUS_FLG.add(status_flg);
						String cargo_status_flag=rset.getString(23)==null?"":rset.getString(23);
						VSEL_CARGO_STATUS_FLG.add(cargo_status_flag);
						String cont_status=""+ContStatusName(status_flg);
						if(cargo_status_flag.equals("Y"))
						{
							cont_status+=" <font color='green'>[Confirmed]</font>";
						}
						else if(cargo_status_flag.equals("N"))
						{
							cont_status+=" <font color='blue'>[Not Confirmed]</font>";
						}
						else if(cargo_status_flag.equals("X"))
						{
							cont_status+=" <font color='red'>[Canceled]</font>";
						}
						VSEL_CONT_STATUS.add(cont_status);
						//till here

						int allocation_count = rset.getInt(17);
						String color="";
						double expected = rset.getDouble(5);
						String qty_unit=rset.getString(6)==null?"":rset.getString(6);
						if(qty_unit.equals("2"))
						{
							expected = expected * 1000000; //Convert to MMBTU
						}
						double unloaded = rset.getDouble(16);
						double projectedQty = rset.getDouble(19);
						unloaded=unloaded+projectedQty;

						double actual_unloaded=0;
						if(allocation_count <= 0)
						{
							actual_unloaded=expected;
							color=color_expected;
						}
						else
						{
							actual_unloaded=unloaded;
							color=color_unloaded;
						}
						VSEL_COLOR.add(color);
						VSEL_BOOKED_QTY.add(nf.format(expected));	//PB20250101
						VSEL_UNLOADED_QTY.add(nf.format(unloaded));	//PB20250101

						VSEL_TRADER_ABBR.add(utilBean.getCounterpartyABBR(conn,countpty_cd));
						VSEL_TRADER_NM.add(utilBean.getCounterpartyName(conn,countpty_cd));

						VSEL_CONT_NAME.add(contNM);		//PB20250101
						VSEL_CONT_NO.add(contNo);
						VSEL_CONT_REV_NO.add(contRev);
						VSEL_AGMT_NO.add(agmtNo);
						VSEL_AGMT_REV_NO.add(agmtRev);
						VSEL_CONT_TYPE.add(cont_type);	//PB20250101: for adding contract type
						VSEL_CONT_TYPE_NM.add(""+utilBean.getContractTypeName(cont_type));
						/*if(cont_type.equals("D"))
						{
							VSEL_CONT_TYPE_NM.add("Domestic NG");
						}
						else if(cont_type.equals("I"))
						{
							VSEL_CONT_TYPE_NM.add("IGX");
						}
						else if(cont_type.equals("N"))
						{
							VSEL_CONT_TYPE_NM.add("LNG");
						}
						else if(cont_type.equals("T"))
						{
							VSEL_CONT_TYPE_NM.add("In Tank LNG|RLNG");
						}
						else
						{
							VSEL_CONT_TYPE_NM.add("");
						}*/

						double rate =rset.getDouble(10);
						String rate_unit = rset.getString(11)==null?"1":rset.getString(11);
						VSEL_RATE.add(""+utilBean.RateNumberFormat(rate, rate_unit));
						VSEL_RATE_UNIT.add(rate_unit);
						VSEL_RATE_UNIT_NM.add(""+utilBean.getRateUnitNm(conn,rate_unit));

						//20240926 double avail_for_sale = actual_unloaded - (actual_unloaded*internal_consumption)/100;

						double sug = rset.getDouble(26);
						double avail_for_sale = actual_unloaded;
						if(cont_type.equals("N"))
						{
							//avail_for_sale = actual_unloaded - (actual_unloaded*sug_percent)/100;
							avail_for_sale = actual_unloaded - (actual_unloaded*sug)/100;
						}
						double allocated_qty = rset.getDouble(18);
						double balance_qty = avail_for_sale - allocated_qty;

						VSEL_ALLOCATED_QTY.add(nf.format(allocated_qty));	//PB20250101
						VSEL_BALANCE_QTY.add(nf.format(balance_qty));
						VSEL_AVAIL_QTY.add(nf.format(avail_for_sale));

						purchase_map_id=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtNo, agmtRev, contNo, contRev, cont_type, cargoNo);
						VSEL_DISPLAY_CONT_DTL.add(purchase_map_id);
						VSEL_CARGO_NO.add(cargoNo);

						if(cont_type.equals("I"))
						{
							cont_ref=trade_ref;
						}
						VSEL_CONT_REF.add(cont_ref);
					}
					rset.close();
					stmt.close();
				}

			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getContractListForAllocation()
	{
		String function_nm="getContractListForAllocation()";
		try
		{
			String queryString="SELECT COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TCQ,QUANTITY_UNIT,"
					+ "CONT_NAME,CONT_REF_NO,RATE,RATE_UNIT,TRADE_REF_NO "
					+ "FROM FMS_SUPPLY_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) AND IS_ALLOCATED='N' "
					+ "AND A.CONT_STATUS NOT IN ('X','T','C') ";	//PB20251106: NOT ALLOWING CANCEL, TERMINATED AND CLOSED CONTRACT FOR ALLOCATION
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, customer_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String counterpty_cd=rset.getString(1)==null?"":rset.getString(1);
				String agmt=rset.getString(2)==null?"":rset.getString(2);
				String agmt_rev=rset.getString(3)==null?"":rset.getString(3);
				String cont=rset.getString(4)==null?"":rset.getString(4);
				String cont_rev=rset.getString(5)==null?"":rset.getString(5);
				String cont_type=rset.getString(6)==null?"":rset.getString(6);

				double tcq = rset.getDouble(7);

				VCOUNTERPARTY_CD.add(counterpty_cd);
				VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterpty_cd));
				VAGMT_NO.add(agmt);
				VAGMT_REV_NO.add(agmt_rev);
				VCONT_NO.add(cont);
				VCONT_REV_NO.add(cont_rev);
				VCONTRACT_TYPE.add(cont_type);
				VTCQ.add(nf.format(tcq));
				VCONTRACT_TYPE_NM.add(""+utilBean.getContractTypeName(cont_type));

				double rate=rset.getDouble(11);
				String rate_unit = rset.getString(12)==null?"1":rset.getString(12);
				VRATE.add(""+utilBean.RateNumberFormat(rate, rate_unit));
				VRATE_UNIT.add(rate_unit);
				VRATE_UNIT_NM.add(""+utilBean.getRateUnitNm(conn,rate_unit));

				String display_cont="";
				/*if(cont_type.equals("S")||cont_type.equals("L") || cont_type.equals("X"))
				{
					// display_cont ="FGSA"+agmt+"-"+cont_type+""+cont;
				}
				else if(cont_type.equals("F")||cont_type.equals("E") || cont_type.equals("W"))
				{
					//display_cont ="FLSA"+agmt+"-"+cont_type+""+cont;
				}
				if(cont_type.equals("L") || cont_type.equals("X"))
				{
					//display_cont=""+cont_type+""+cont;
				}*/

				display_cont=utilBean.NewDealMappingId(comp_cd, counterpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
				/*if(cont_type.equals("F")||cont_type.equals("E") || cont_type.equals("W"))
				{
					display_cont += " <span style='background:yellow;'>(DLNG)</span>";		//other color: #FDCDE8;
				}*/

				VDISPLAY_CONT_DTL.add(display_cont);

				String cont_ref_no=rset.getString(10)==null?"":rset.getString(10);
				String trade_ref_no=rset.getString(13)==null?"":rset.getString(13);
				if(cont_type.equals("X"))
				{
					cont_ref_no=trade_ref_no;
				}

				VCONT_REF.add(cont_ref_no);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getExchangeRate()
	{
		String function_nm="getExchangeRate()";
		try
		{
			String exchg_rate_cd = "",exchg_rate_nm = "";
			String rate_nm="Shell Treasury Rate";
			String queryString = "SELECT EXC_RATE_NM,EXC_RATE_CD "
					+ "FROM FMS_EXCHG_RATE_MST "
					+ "WHERE UPPER(EXC_RATE_NM) =?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, rate_nm.toUpperCase());
			rset=stmt.executeQuery();
			if(rset.next())
			{
				exchg_rate_nm = rset.getString(1)==null?"N.A":rset.getString(1);
				exchg_rate_cd = rset.getString(2)==null?"":rset.getString(2);
			}
			rset.close();
			stmt.close();

			queryString = "SELECT EXCHG_VAL "
					+ "FROM FMS_EXCHG_RATE_ENTRY A "
					+ "WHERE EXCHG_RATE_CD=? "
					+ "AND EFF_DT =(SELECT MAX(B.EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
					+ "WHERE A.EXCHG_RATE_CD=B.EXCHG_RATE_CD  "
					+ "AND B.EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND FLAG='Y'";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, exchg_rate_cd);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				exchgRate = rset.getDouble(1);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public double getContractWiseAllocatedMMBTU(String counterparty_cd,String agmt,String agmt_rev,String cont,String cont_rev,String cont_type,String pur_cont_no,String cargo_no)
	{
		String function_nm="getContractWiseAllocatedMMBTU()";
		double alloc_qty=0;
		try
		{
			String queryString_tmp="SELECT SUM(ALLOC_QTY) "
					+ "FROM FMS_SUPPLY_PURCHASE_MAP_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
					+ "AND AGMT_NO=? AND CONT_NO=? AND PUR_CONT_NO=? AND CARGO_NO=? ";
			stmt_tmp=conn.prepareStatement(queryString_tmp);
			stmt_tmp.setString(1, comp_cd);
			stmt_tmp.setString(2, counterparty_cd);
			stmt_tmp.setString(3, cont_type);
			stmt_tmp.setString(4, agmt);
			stmt_tmp.setString(5, cont);
			stmt_tmp.setString(6, pur_cont_no);
			stmt_tmp.setString(7, cargo_no);
			rset_tmp=stmt_tmp.executeQuery();
			if(rset_tmp.next())
			{
				alloc_qty=rset_tmp.getDouble(1);
			}
			rset_tmp.close();
			stmt_tmp.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return alloc_qty;
	}

	//PB20241231: for allocation transfer
	public void getCargoAllocatedDeatils()
	{
		String function_nm = "getCargoAllocatedDeatils()";
		double total_alloc=0;
		try
		{
			String alloc_quant = "SELECT SUM(ALLOC_QTY) FROM FMS_SUPPLY_PURCHASE_MAP_DTL C WHERE A.COMPANY_CD = C.COMPANY_CD AND  "
					+ "C.AGMT_NO = A.AGMT_NO AND C.AGMT_REV=A.AGMT_REV "
					+ "AND A.CONT_NO = C.CONT_NO AND C.PUR_CONT_NO = A.PUR_CONT_NO "
					+ "AND C.COUNTERPARTY_CD = A.COUNTERPARTY_CD ";
			if(!multiCargoNo.equals(""))
			{
				alloc_quant+="AND C.CARGO_NO=A.CARGO_NO ";
			};
			String query = "SELECT  COUNTERPARTY_CD, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE, "
					+ "("+alloc_quant+") ALLOC_QUANT "
					+ "FROM FMS_SUPPLY_PURCHASE_MAP_DTL A "
					+ "WHERE COMPANY_CD=? AND PUR_CONT_NO=? "
					+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_SUPPLY_PURCHASE_MAP_DTL C WHERE A.COMPANY_CD = C.COMPANY_CD AND  "
					+ "C.AGMT_NO = A.AGMT_NO AND C.AGMT_REV=A.AGMT_REV "
					+ "AND A.CONT_NO = C.CONT_NO AND C.PUR_CONT_NO = A.PUR_CONT_NO "
					+ "AND C.COUNTERPARTY_CD = A.COUNTERPARTY_CD ";
			if(!multiCargoNo.equals(""))
			{
				query+= "AND C.CARGO_NO=A.CARGO_NO ";
			}
			query+= ") ";
			if(!multiCargoNo.equals(""))
			{
				query+= "AND CARGO_NO=? ";
			}
			query+= "AND ("+alloc_quant+")>0";
			String temp_query = query;
			stmt = conn.prepareStatement(temp_query);	//PB20250212: probable solution security HotSpot issue
			//stmt = conn.prepareStatement(query);	//Giving security HotSpot issue
			stmt.setString(1, comp_cd);
			stmt.setString(2, multiContNo);
			if(!multiCargoNo.equals(""))
			{
				stmt.setString(3, multiCargoNo);
			}
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String cust_counter_pty = rset.getString(1)==null?"":rset.getString(1);
				String sales_agmt_no = rset.getString(2)==null?"0":rset.getString(2);
				String sales_agmt_rev = rset.getString(3)==null?"0":rset.getString(3);
				String sales_cont_no = rset.getString(4)==null?"":rset.getString(4);
				String sales_cont_rev = rset.getString(5)==null?"":rset.getString(5);
				String sales_cont_type = rset.getString(6)==null?"":rset.getString(6);
				//String alloc_qtn_to_sales = rset.getString(7)==null?"":rset.getString(7);
				double alloc_qtn_to_sales = rset.getDouble(7);
				total_alloc+=alloc_qtn_to_sales;
				String salesdeal = utilBean.NewDealMappingId(comp_cd, cust_counter_pty, sales_agmt_no, sales_agmt_rev, sales_cont_no, sales_cont_rev, sales_cont_type,multiCargoNo);

				//FETCHING THE CUSTOMER CONTRACT DETAILS
				String query1 = "SELECT TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),TCQ,QUANTITY_UNIT,"
						+ "CASE WHEN TO_DATE(END_DT) >= TO_DATE(SYSDATE) THEN CONT_STATUS "
						+ "ELSE 'EXPIRE' "
						+ "END, "
						+ "RATE,RATE_UNIT,AGMT_BASE,CONTRACT_TYPE,CONT_REF_NO,TRADE_REF_NO "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
						+ "AND CONTRACT_TYPE=? AND  IS_ALLOCATED='Y'  "
						+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD  "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO  "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
				stmt1 = conn.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, cust_counter_pty);
				stmt1.setString(3, sales_agmt_no);
				stmt1.setString(4, sales_agmt_rev);
				stmt1.setString(5, sales_cont_no);
				stmt1.setString(6, sales_cont_type);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					String start_dt = rset1.getString(1)==null?"":rset1.getString(1);
					String end_dt = rset1.getString(2)==null?"":rset1.getString(2);
					double booked = rset1.getDouble(3);
					String qty_unit=rset1.getString(4)==null?"":rset1.getString(4);
					String status_flag = rset1.getString(5)==null?"":rset1.getString(5);
					double rate =rset1.getDouble(6);
					String rate_unit = rset1.getString(7)==null?"1":rset1.getString(7);
					String agmt_base = rset1.getString(8)==null?"":rset1.getString(8);
					String cont_type = rset1.getString(9)==null?"":rset1.getString(9);
					String cont_ref = rset1.getString(10)==null?"":rset1.getString(10);
					String trade_ref = rset1.getString(11)==null?"":rset1.getString(11);

					if(cont_type.equals("X"))
					{
						cont_ref = trade_ref;
					}

					if(qty_unit.equals("2"))
					{
						booked = booked * 1000000; //Convert to MMBTU
					}

					String status ="";
					if(status_flag.equals("EXPIRE"))
					{
						status = status_flag;
					}
					else
					{
						status = ""+ContStatusName(status_flag);
					}

					//PB20250109: for taking active deals for transferring energy.
					if(callFlag.equalsIgnoreCase("TRANSFER_CARGO"))
					{
						if(transfer_type.equals(""))
						{
							if(status_flag.equals("EXPIRE"))
							{
								continue;
							}
						}
					}

					VCUST_START_DT.add(start_dt);
					VCUST_END_DT.add(end_dt);
					VCUST_BOOKED_QTY.add(nf.format(booked));
					VCUST_CONT_STATUS_FLAG.add(status_flag);
					VCUST_CONT_STATUS.add(status);
					VCUST_RATE.add(""+utilBean.RateNumberFormat(rate, rate_unit));
					VCUST_RATE_UNIT.add(rate_unit);
					VCUST_RATE_UNIT_NM.add(""+utilBean.getRateUnitNm(conn,rate_unit));
					String supplied_qty=allocUtil.getBestSupplyAllocationQty(conn,comp_cd, cust_counter_pty, sales_agmt_no, sales_cont_no, sales_cont_type,start_dt,end_dt,agmt_base,"0");
					VCUST_SUPPLIED_QTY.add(supplied_qty);
					VCUST_CONT_TYPE.add(cont_type);
					VCUST_CONTRACT_TYPE_NM.add(""+utilBean.getContractTypeName(cont_type));
					double transferable=booked-Double.parseDouble(supplied_qty);
					VCUST_TRANSERABLE_QTY.add(nf.format(transferable));
					VCUST_CONT_REF.add(cont_ref);
				}
				rset1.close();
				stmt1.close();

				VCUSTOMER_CD.add(cust_counter_pty);
				VCUSTOMER_ABBR.add(""+utilBean.getCounterpartyABBR(conn,cust_counter_pty));
				VCUSTOMER_NM.add(""+utilBean.getCounterpartyName(conn,cust_counter_pty));
				VCUST_AGMT_NO.add(sales_agmt_no);
				VCUST_AGMT_REV_NO.add(sales_agmt_rev);
				VCUST_CONT_NO.add(sales_cont_no);
				VCUST_CONT_REV_NO.add(sales_cont_rev);
				VALLOC_QTY_TO_CUST.add(nf.format(alloc_qtn_to_sales));
				VCUST_DISPLAY_CONT_DTL.add(salesdeal);
			}
			rset.close();
			stmt.close();
			totalAlloc_Qty=""+nf.format(total_alloc);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	//PB20250124: For Allocation Transfer Report
	public void getAllocationTransferDetail()
	{
		String function_nm = "getAllocationTransferDetail()";
		try
		{
			comp_abbr = utilBean.getCompanyAbbr(conn,comp_cd);
			String query = "SELECT COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE, "
					+ "NEW_PUR_CONT_NO,OLD_PUR_CONT_NO,NEW_CARGO_NO,OLD_CARGO_NO,ALLOC_QTY, "
					+ "TRANSFER_TYPE,ENT_BY,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI') "
					+ "FROM FMS_TRADE_SUPPLY_TRANSFER "
					+ "WHERE COMPANY_CD=? AND TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					//+ "AND TRANSFER_TYPE NOT IN ('TCQ Modification','MMBTU Allocation') "
					+ "ORDER BY ENT_DT DESC";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, to_dt);	//to date
			stmt.setString(3,from_dt); //from date
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String countpty_cd = rset.getString(1)==null?"":rset.getString(1);
				String agmt_no = rset.getString(2)==null?"0":rset.getString(2);
				String agmt_rev = rset.getString(3)==null?"0":rset.getString(3);
				String cont_no = rset.getString(4)==null?"":rset.getString(4);
				String cont_rev = rset.getString(5)==null?"0":rset.getString(5);
				String cont_type = rset.getString(6)==null?"":rset.getString(6);
				String new_pur_cont_no = rset.getString(7)==null?"":rset.getString(7);
				String old_pur_cont_no = rset.getString(8)==null?"":rset.getString(8);
				String new_cargo_no = rset.getString(9)==null?"":rset.getString(9);
				String old_cargo_no = rset.getString(10)==null?"":rset.getString(10);
				double alloc_qty = rset.getDouble(11);
				String transfer_type = rset.getString(12)==null?"":rset.getString(12);
				String ent_by = rset.getString(13)==null?"":rset.getString(13);
				String ent_dt = rset.getString(14)==null?"":rset.getString(14);

				String salesdeal = utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt_no, agmt_rev, cont_no, cont_rev, cont_type,multiCargoNo);


				String sourceCargoDtl = getPurchaseDTL(comp_cd,old_pur_cont_no,old_cargo_no);
				String destinationCargoDtl = getPurchaseDTL(comp_cd,new_pur_cont_no,new_cargo_no);

				VCOUNTERPARTY_CD.add(countpty_cd);
				VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VSEL_AGMT_NO.add(agmt_no);
				VSEL_AGMT_REV_NO.add(agmt_rev);
				VSEL_CONT_NO.add(cont_no);
				VSEL_CONT_REV_NO.add(cont_rev);
				VSEL_CONT_TYPE.add(cont_type);
				VSEL_CONT_TYPE_NM.add(""+utilBean.getContractTypeName(cont_type));
				VCUST_DISPLAY_CONT_DTL.add(salesdeal);
				VSOURCE_CARGO_DTL.add(sourceCargoDtl); //temporary
				VDESTINATION_CARGO_DTL.add(destinationCargoDtl); //temporary
				VALLOC_QTY_TO_CUST.add(nf.format(alloc_qty));
				VTRANSFER_TYPE.add(transfer_type);
				VENT_DT.add(ent_dt);
				VEMP_NM.add(""+utilBean.getEmpName(conn,ent_by));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	//PB20250127: FOR EXTRACTING THE PURCHASE CONTRACT DETAILS
	public String getPurchaseDTL(String comp_cd, String cont_no, String cargo_no)
	{
		String function_nm="getPurchaseDTL()";
		String purchase_map_id = "";
		String cont_type_nm="";
		try
		{
			//added for handling the pseudo cargo
			if(cont_no.equals("0") && !cargo_no.equals("0"))		//cont_no =0, counterparty_cd=0 and cargo_no!=0 then pseudo cargo
			{
				String query="SELECT COMPANY_CD, CONTRACT_TYPE, CARGO_NO "
						+ "FROM FMS_PSEUDO_CARGO_DTL "
						+ "WHERE COMPANY_CD=? AND CARGO_NO=?";
				stmt2 = conn.prepareStatement(query);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, cargo_no);
				rset2 = stmt2.executeQuery();
				if(rset2.next())
				{
					String cont_type = rset2.getString(2)==null?"":rset2.getString(2);
					String cont_nm="";
					/*if(cont_type.equals("D"))
					{
						cont_nm="Domestic NG";
					}
					else if(cont_type.equals("I"))
					{
						cont_nm="IGX";
					}
					else if(cont_type.equals("N"))
					{
						cont_nm="LNG";
					}
					else if(cont_type.equals("T"))
					{
						cont_nm="In Tank LNG|RLNG";
					}*/
					cont_nm=""+utilBean.getContractTypeName(cont_type);
					purchase_map_id=""+comp_cd+""+cont_type+"-"+cargo_no;
				}
				rset2.close();
				stmt2.close();
			}
			//for own volume
			else if(cont_no.equals("0") && cargo_no.equals("0"))		//values similar to pseudo cargo but here cargo_no is also 0
			{
				purchase_map_id=cargo_no;
			}
			else
			{
				String query1 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, "
						+ "CONT_NO,CONT_REV,CONTRACT_TYPE,0 AS CARGO_NO "
						+ "FROM FMS_TRADER_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM  FMS_TRADER_CONT_MST B WHERE  "
						+ "A.COMPANY_CD = B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV = B.AGMT_REV AND  "
						+ "A.CONT_NO = B.CONT_NO AND A.CONTRACT_TYPE = B.CONTRACT_TYPE) "
						+ "UNION  "
						+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, "
						+ "CONT_NO,CONT_REV,CONTRACT_TYPE,CARGO_NO "
						+ "FROM FMS_TRADER_CARGO_MST A "
						+ "WHERE COMPANY_CD=? AND CONT_NO=? AND CARGO_NO=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM  FMS_TRADER_CARGO_MST B WHERE  "
						+ "A.COMPANY_CD = B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV = B.AGMT_REV AND  "
						+ "A.CONT_NO = B.CONT_NO AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO)";
				stmt1 = conn.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, cont_no);
				stmt1.setString(3, comp_cd);
				stmt1.setString(4, cont_no);
				stmt1.setString(5, cargo_no);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					// String cmp_cd = rset1.getString(1)==null?"":rset1.getString(1);
					String counterparty_cd = rset1.getString(2)==null?"":rset1.getString(2);
					String agmt_no = rset1.getString(3)==null?"":rset1.getString(3);
					String agmt_rev = rset1.getString(4)==null?"":rset1.getString(4);
					// String cont_no = rset1.getString(5)==null?"":rset1.getString(5);
					String cont_rev = rset1.getString(6)==null?"":rset1.getString(6);
					String cont_type = rset1.getString(7)==null?"":rset1.getString(7);
					// String cargo_no = rset1.getString(8)==null?"":rset1.getString(8);
					/*if(cont_type.equals("D"))
					{
						cont_nm="Domestic NG";
					}
					else if(cont_type.equals("I"))
					{
						cont_nm="IGX";
					}
					else if(cont_type.equals("N"))
					{
						cont_nm="LNG";
					}
					else if(cont_type.equals("T"))
					{
						cont_nm="In Tank LNG|RLNG";
					}*/
					String cont_nm=""+utilBean.getContractTypeName(cont_type);
					cont_type_nm=cont_nm;
					purchase_map_id = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, cont_type,cargo_no);
				}
				rset1.close();
				stmt1.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return purchase_map_id;
	}

	//Pratham Bhatt 20250219: for pseudo cargo report
	public void getPseudoTransferDetails()
	{
		String function_nm = "getPseudoTransferDetails()";
		try
		{
			comp_abbr = utilBean.getCompanyAbbr(conn,comp_cd);
			String query="SELECT COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE, "
					+ "NEW_PUR_CONT_NO,OLD_PUR_CONT_NO,ALLOC_QTY,TRANSFER_TYPE,NEW_CARGO_NO,OLD_CARGO_NO, "
					+ "TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI'),ENT_BY "
					+ "FROM FMS_PSEUDO_SUPPLY_TRANSFER "
					+ "WHERE COMPANY_CD=? AND TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY') <= TO_DATE(?,'DD/MM/YYYY')  "
					+ "AND TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE(?,'DD/MM/YYYY') "
					+ "ORDER BY ENT_DT DESC";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, to_dt);	//to date
			stmt.setString(3,from_dt); //from date
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String counterparty_cd = rset.getString(1)==null?"0":rset.getString(1);
				String agmt_no = rset.getString(2)==null?"0":rset.getString(2);
				String agmt_rev = rset.getString(3)==null?"0":rset.getString(3);
				String cont_no = rset.getString(4)==null?"0":rset.getString(4);
				String cont_rev = rset.getString(5)==null?"0":rset.getString(5);
				String contract_type = rset.getString(6)==null?"0":rset.getString(6);
				String new_pur_cont_no = rset.getString(7)==null?"0":rset.getString(7);
				String old_pur_cont_no = rset.getString(8)==null?"0":rset.getString(8);
				double alloc_qty= rset.getDouble(9);
				String transfer_type = rset.getString(10)==null?"":rset.getString(10);
				String new_cargo_no = rset.getString(11)==null?"":rset.getString(11);
				String old_cargo_no = rset.getString(12)==null?"0":rset.getString(12);
				String ent_dt = rset.getString(13)==null?"":rset.getString(13);
				String ent_by = rset.getString(14)==null?"":rset.getString(14);

				VCOUNTERPARTY_CD.add(counterparty_cd.equals("0")?"":counterparty_cd);
				VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,counterparty_cd.equals("0")?"":counterparty_cd));
				VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterparty_cd.equals("0")?"":counterparty_cd));
				VSEL_AGMT_NO.add(agmt_no);
				VSEL_AGMT_REV_NO.add(agmt_rev);
				VSEL_CONT_NO.add(cont_no);
				VSEL_CONT_REV_NO.add(cont_rev);
				VSEL_CONT_TYPE.add(contract_type);

				String sourceCargoDtl="";
				String destinationCargoDtl="";
				String pseudoCargo="";
				if(transfer_type.equals("MMBTU Allocation")||transfer_type.equals("TCQ Modification"))
				{
					pseudoCargo = old_cargo_no;
					sourceCargoDtl = getPurchaseDTL(comp_cd,old_pur_cont_no,old_cargo_no);
					destinationCargoDtl = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, contract_type,new_cargo_no); //Sales contract is destination...
				}
				else
				{
					pseudoCargo = old_pur_cont_no.equals("0")?old_cargo_no:new_cargo_no;
					sourceCargoDtl = getPurchaseDTL(comp_cd,old_pur_cont_no,old_cargo_no);
					destinationCargoDtl = getPurchaseDTL(comp_cd,new_pur_cont_no,new_cargo_no);
				}
				VCUST_DISPLAY_CONT_DTL.add(pseudoCargo);
				VSOURCE_CARGO_DTL.add(sourceCargoDtl);
				VDESTINATION_CARGO_DTL.add(destinationCargoDtl);
				VALLOC_QTY_TO_CUST.add(nf.format(alloc_qty));
				VTRANSFER_TYPE.add(transfer_type);
				VENT_DT.add(ent_dt);
				VEMP_NM.add(""+utilBean.getEmpName(conn,ent_by));

			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	//PB20250303: for projected LTCORA CARGO details
	public void getProjectedLTCORACargoDtl()
	{
		String function_nm = "getProjectedLTCORACargoDtl()";
		try
		{
			String sysdate = utilDate.getSysdate();

			String common_mod_sug="FROM FMS_LTCORA_CONT_CARGO_MOD B WHERE B.COMPANY_CD = A.COMPANY_CD AND  B.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
					+ "AND B.BUY_SALE=A.BUY_SALE AND B.AGMT_TYPE = A.AGMT_TYPE "
					+ "AND B.CONT_NO=A.CONT_NO AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.CARGO_NO=A.CARGO_NO AND B.APPROVAL_FLAG='Y' "
					+ "AND B.CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_MOD C WHERE B.COMPANY_CD = C.COMPANY_CD "
					+ "AND B.COUNTERPARTY_CD = C.COUNTERPARTY_CD AND B.BUY_SALE=C.BUY_SALE AND B.AGMT_TYPE = C.AGMT_TYPE "
					+ "AND B.CONT_NO=C.CONT_NO AND B.CONTRACT_TYPE=C.CONTRACT_TYPE AND B.CARGO_NO=C.CARGO_NO AND B.APPROVAL_FLAG='Y')";

			String common_sug = "FROM FMS_LTCORA_CONT_MST B  WHERE A.COMPANY_CD = B.COMPANY_CD AND B.BUY_SALE=A.BUY_SALE AND B.AGMT_TYPE = A.AGMT_TYPE "
					+ "AND B.CONT_NO=A.CONT_NO AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST C "
					+ "WHERE B.COMPANY_CD = C.COMPANY_CD AND  B. COUNTERPARTY_CD = C.COUNTERPARTY_CD AND B.BUY_SALE=C.BUY_SALE AND B.AGMT_TYPE = C.AGMT_TYPE "
					+ "AND B.CONT_NO=C.CONT_NO AND B.CONTRACT_TYPE=C.CONTRACT_TYPE )";

			String count_mod_sug = "SELECT COUNT(*) "+common_mod_sug;

			String mod_sug = "SELECT SUG "+common_mod_sug;

			String sug_per ="SELECT SUG "+common_sug;

			String final_sug = "CASE WHEN ("+count_mod_sug+") > 0 THEN ("+mod_sug+") ELSE ("+sug_per+") END ";

			//for generating projected quantity
			String projected_mmbtu_dtl = "SELECT COUNTERPARTY_CD,AGMT_NO,AGMT_REV,AGMT_TYPE,CONT_NO,CONT_REV,CONTRACT_TYPE,EDQ_QTY,"
					+ "("+final_sug+"), "
					+ "(EDQ_QTY * ("+final_sug+")/100) SUG_PER,TO_CHAR(ACTUAL_RECPT_DT,'DD/MM/YYYY'),CARGO_NO "
					+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
					+ "WHERE COMPANY_CD=? AND BUY_SALE=? AND AGMT_TYPE=? "
					+ "AND CONTRACT_TYPE IN (?,?) AND ACTUAL_RECPT_DT>TO_DATE(?,'DD/MM/YYYY')";
			String temp_projected_mmbtu = projected_mmbtu_dtl;
			int ctn=0;
			double proj_mmbtu=0;
			double sum_edq_qty=0;
			double sum_projected_qty=0;
			stmt3 = conn.prepareStatement(temp_projected_mmbtu);
			stmt3.setString(++ctn,comp_cd);
			stmt3.setString(++ctn,"C");
			stmt3.setString(++ctn,"A");
			stmt3.setString(++ctn,"O");
			stmt3.setString(++ctn,"Q");
			stmt3.setString(++ctn,sysdate);
			rset3 = stmt3.executeQuery();
			while(rset3.next())
			{
				String counterparty_cd=rset3.getString(1)==null?"":rset3.getString(1);
				String agmt_no=rset3.getString(2)==null?"":rset3.getString(2);
				String agmt_rev=rset3.getString(3)==null?"":rset3.getString(3);
				String agmt_type=rset3.getString(4)==null?"":rset3.getString(4);
				String cont_no=rset3.getString(5)==null?"":rset3.getString(5);
				String cont_rev=rset3.getString(6)==null?"":rset3.getString(6);
				String cont_type=rset3.getString(7)==null?"":rset3.getString(7);
				double edq_qty=rset3.getDouble(8);
				double sug = rset3.getDouble(9);
				proj_mmbtu = rset3.getDouble(10);
				String actual_recpt_dt= rset3.getString(11)==null?"":rset3.getString(11);
				String cargo_no = rset3.getString(12)==null?"":rset3.getString(12);

				sum_edq_qty+=edq_qty;
				sum_projected_qty+=proj_mmbtu;

				String sales_map_id = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, cont_type,cargo_no);

				VSEL_TRADER_CD.add(counterparty_cd);
				VSEL_TRADER_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterparty_cd));
				VSEL_DISPLAY_CONT_DTL.add(sales_map_id);
				VSEL_START_DT.add(actual_recpt_dt);
				VSEL_BOOKED_QTY.add(nf.format(edq_qty));
				VSEL_SUG.add(sug);
				VSEL_UNLOADED_QTY.add(nf.format(proj_mmbtu));
			}
			VSEL_TOTAL_EDQ.add(nf.format(sum_edq_qty));
			VSEL_TOTAL_PROJECTED.add(nf.format(sum_projected_qty));
			rset3.close();
			stmt3.close();
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

	String multiCountpty="";
	String multiAgmtNo="";
	String multiAgmtRev="";
	String multiContNo="";
	String multiContRev="";
	String multiContTyp="";
	String multiCargoNo="";
	String temp_multiCountpty="";
	String temp_multiAgmtNo="";
	String temp_multiAgmtRev="";
	String temp_multiContNo="";
	String temp_multiContRev="";
	String temp_multiContTyp="";
	String temp_multiCargoNo="";
	String customer_cd="";
	String clearance="";
	String counterparty_cd = "";
	String cont_no = "";
	String cont_rev_no = "";
	String agmt_no = "";
	String agmt_rev_no = "";
	String contract_type = "";
	String tcq_sign = "";
	String transfer_type="";
	String totalAlloc_Qty="";
	String from_dt="";
	String to_dt="";
	String mmbtu_range_sign="0";
	String comp_abbr = "";

	public String getTotalAlloc_Qty() {return totalAlloc_Qty;}
	public String getComp_Abbr() {return comp_abbr;}
	public void setFrom_Dt(String from_dt) {this.from_dt = from_dt;}
	public void setTo_Dt(String to_dt) {this.to_dt = to_dt;}
	public void setTransferType(String transfer_type) {this.transfer_type = transfer_type;}
	public void setMultiCountpty(String multiCountpty) {this.multiCountpty = multiCountpty;}
	public void setMultiAgmtNo(String multiAgmtNo) {this.multiAgmtNo = multiAgmtNo;}
	public void setMultiAgmtRev(String multiAgmtRev) {this.multiAgmtRev = multiAgmtRev;}
	public void setMultiContNo(String multiContNo) {this.multiContNo = multiContNo;}
	public void setMultiContRev(String multiContRev) {this.multiContRev = multiContRev;}
	public void setMultiContTyp(String multiContTyp) {this.multiContTyp = multiContTyp;}
	public void setMultiCargoNo(String multiCargoNo) {this.multiCargoNo = multiCargoNo;}
	public void setTempMultiCountpty(String temp_multiCountpty) {this.temp_multiCountpty = temp_multiCountpty;}
	public void setTempMultiAgmtNo(String temp_multiAgmtNo) {this.temp_multiAgmtNo = temp_multiAgmtNo;}
	public void setTempMultiAgmtRev(String temp_multiAgmtRev) {this.temp_multiAgmtRev = temp_multiAgmtRev;}
	public void setTempMultiContNo(String temp_multiContNo) {this.temp_multiContNo = temp_multiContNo;}
	public void setTempMultiContRev(String temp_multiContRev) {this.temp_multiContRev = temp_multiContRev;}
	public void setTempMultiContTyp(String temp_multiContTyp) {this.temp_multiContTyp = temp_multiContTyp;}
	public void setTempMultiCargoNo(String temp_multiCargoNo) {this.temp_multiCargoNo = temp_multiCargoNo;}
	public void setCustomer_cd(String customer_cd) {this.customer_cd = customer_cd;}
	public void setClearance(String clearance) {this.clearance = clearance;}
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setCont_no(String cont_no) {this.cont_no = cont_no;}
	public void setCont_rev_no(String cont_rev_no) {this.cont_rev_no = cont_rev_no;}
	public void setAgmt_no(String agmt_no) {this.agmt_no = agmt_no;}
	public void setAgmt_rev_no(String agmt_rev_no) {this.agmt_rev_no = agmt_rev_no;}
	public void setContract_type(String contract_type) {this.contract_type = contract_type;}
	public void setTcq_sign(String tcq_sign) {this.tcq_sign = tcq_sign;}
	public void setBalanceMMBTURange(String mmbtu_range_sign)  {this.mmbtu_range_sign = mmbtu_range_sign;}

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
	Vector VBALANCE_QTY_INFO = new Vector();
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

	Vector VSEL_TRADER_CD = new Vector();
	Vector VSEL_TRADER_ABBR = new Vector();
	Vector VSEL_TRADER_NM = new Vector();
	Vector VSEL_CONT_NO = new Vector();
	Vector VSEL_CONT_NAME = new Vector();
	Vector VSEL_CONT_REF = new Vector();
	Vector VSEL_CONT_TYPE = new Vector();
	Vector VSEL_CONT_TYPE_NM = new Vector();
	Vector VSEL_CONT_REV_NO = new Vector();
	Vector VSEL_AGMT_NO = new Vector();
	Vector VSEL_AGMT_REV_NO = new Vector();
	Vector VSEL_RATE = new Vector();
	Vector VSEL_RATE_UNIT = new Vector();
	Vector VSEL_RATE_UNIT_NM = new Vector();
	Vector VSEL_BALANCE_QTY = new Vector();
	Vector VSEL_BOOKED_QTY = new Vector();
	Vector VSEL_UNLOADED_QTY = new Vector();
	Vector VSEL_ALLOCATED_QTY = new Vector();
	Vector VSEL_DISPLAY_CONT_DTL = new Vector();
	Vector VSEL_COLOR = new Vector();
	Vector VSEL_PRICE_TYPE = new Vector();
	Vector VSEL_CONT_STATUS_FLG = new Vector();
	Vector VSEL_CONT_STATUS = new Vector();
	Vector VSEL_CARGO_STATUS_FLG = new Vector();
	Vector VSEL_MIN_ALLOC_DT = new Vector();
	Vector VSEL_MAX_ALLOC_DT = new Vector();
	Vector VSEL_START_DT = new Vector();
	Vector VSEL_END_DT = new Vector();
	Vector VSEL_CARGO_NO = new Vector();
	Vector VSEL_SUG = new Vector();
	Vector VSEL_TOTAL_EDQ = new Vector();
	Vector VSEL_TOTAL_PROJECTED = new Vector();
	Vector VSEL_AVAIL_QTY = new Vector();

	Vector VCUST_AGMT_NO = new Vector();
	Vector VCUST_AGMT_REV_NO = new Vector();
	Vector VCUST_CONT_NO = new Vector();
	Vector VCUST_CONT_REV_NO = new Vector();
	Vector VALLOC_QTY_TO_CUST= new Vector();
	Vector VCUST_DISPLAY_CONT_DTL= new Vector();
	Vector VCUST_START_DT= new Vector();
	Vector VCUST_END_DT= new Vector();
	Vector VCUST_BOOKED_QTY= new Vector();
	Vector VCUST_CONT_STATUS= new Vector();
	Vector VCUST_CONT_STATUS_FLAG= new Vector();
	Vector VCUST_RATE= new Vector();
	Vector VCUST_RATE_UNIT_NM= new Vector();
	Vector VCUST_RATE_UNIT= new Vector();
	Vector VCUST_SUPPLIED_QTY= new Vector();
	Vector VCUST_CONT_TYPE= new Vector();
	Vector VCUST_CONTRACT_TYPE_NM= new Vector();
	Vector VCUST_TRANSERABLE_QTY= new Vector();
	Vector VCUST_CONT_REF = new Vector();

	Vector VSOURCE_CARGO_DTL = new Vector();
	Vector VDESTINATION_CARGO_DTL = new Vector();
	Vector VTRANSFER_TYPE = new Vector();
	Vector VENT_DT = new Vector();
	Vector VEMP_NM = new Vector();

	Vector VTOTAL_BALANCE_MMBTU = new Vector();

	public Vector getVSOURCE_CARGO_DTL() {return VSOURCE_CARGO_DTL;}
	public Vector getVDESTINATION_CARGO_DTL() {return VDESTINATION_CARGO_DTL;}
	public Vector getVTRANSFER_TYPE() {return VTRANSFER_TYPE;}
	public Vector getVENT_DT() {return VENT_DT;}
	public Vector getVEMP_NM() {return VEMP_NM;}

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
	public Vector getVBALANCE_QTY_INFO() {return VBALANCE_QTY_INFO;}
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

	public Vector getVSEL_TRADER_CD() {return VSEL_TRADER_CD;}
	public Vector getVSEL_TRADER_ABBR() {return VSEL_TRADER_ABBR;}
	public Vector getVSEL_TRADER_NM() {return VSEL_TRADER_NM;}
	public Vector getVSEL_CONT_NO() {return VSEL_CONT_NO;}
	public Vector getVSEL_CONT_NAME() {return VSEL_CONT_NAME;}
	public Vector getVSEL_CONT_REF() {return VSEL_CONT_REF;}
	public Vector getVSEL_CONT_TYPE() {return VSEL_CONT_TYPE;}
	public Vector getVSEL_CONT_TYPE_NM() {return VSEL_CONT_TYPE_NM;}
	public Vector getVSEL_CONT_REV_NO() {return VSEL_CONT_REV_NO;}
	public Vector getVSEL_AGMT_NO() {return VSEL_AGMT_NO;}
	public Vector getVSEL_AGMT_REV_NO() {return VSEL_AGMT_REV_NO;}
	public Vector getVSEL_RATE() {return VSEL_RATE;}
	public Vector getVSEL_RATE_UNIT() {return VSEL_RATE_UNIT;}
	public Vector getVSEL_RATE_UNIT_NM() {return VSEL_RATE_UNIT_NM;}
	public Vector getVSEL_BALANCE_QTY() {return VSEL_BALANCE_QTY;}
	public Vector getVSEL_BOOKED_QTY() {return VSEL_BOOKED_QTY;}
	public Vector getVSEL_UNLOADED_QTY() {return VSEL_UNLOADED_QTY;}
	public Vector getVSEL_ALLOCATED_QTY () {return VSEL_ALLOCATED_QTY ;}
	public Vector getVSEL_DISPLAY_CONT_DTL() {return VSEL_DISPLAY_CONT_DTL;}
	public Vector getVSEL_COLOR() {return VSEL_COLOR;}
	public Vector getVSEL_PRICE_TYPE() {return VSEL_PRICE_TYPE;}
	public Vector getVSEL_CONT_STATUS_FLG() {return VSEL_CONT_STATUS_FLG;}
	public Vector getVSEL_CONT_STATUS() {return VSEL_CONT_STATUS;}
	public Vector getVSEL_MIN_ALLOC_DT() {return VSEL_MIN_ALLOC_DT;}
	public Vector getVSEL_MAX_ALLOC_DT() {return VSEL_MAX_ALLOC_DT;}
	public Vector getVSEL_START_DT() {return VSEL_START_DT;}
	public Vector getVSEL_END_DT() {return VSEL_END_DT;}
	public Vector getVSEL_CARGO_NO() {return VSEL_CARGO_NO;}
	public Vector getVSEL_SUG() {return VSEL_SUG;}
	public Vector getVSEL_TOTAL_EDQ() {return VSEL_TOTAL_EDQ;}
	public Vector getVSEL_TOTAL_PROJECTED() {return VSEL_TOTAL_PROJECTED;}
	public Vector getVSEL_AVAIL_QTY() {return VSEL_AVAIL_QTY;}

	public Vector getVCUST_AGMT_NO() {return VCUST_AGMT_NO;}
	public Vector getVCUST_AGMT_REV_NO() {return VCUST_AGMT_REV_NO;}
	public Vector getVCUST_CONT_NO() {return VCUST_CONT_NO;}
	public Vector getVCUST_CONT_REV_NO() {return VCUST_CONT_REV_NO;}
	public Vector getVALLOC_QTY_TO_CUST() {return VALLOC_QTY_TO_CUST;}
	public Vector getVCUST_DISPLAY_CONT_DTL() {return VCUST_DISPLAY_CONT_DTL;}
	public Vector getVCUST_START_DT() {return VCUST_START_DT;}
	public Vector getVCUST_END_DT() {return VCUST_END_DT;}
	public Vector getVCUST_BOOKED_QTY() {return VCUST_BOOKED_QTY;}
	public Vector getVCUST_CONT_STATUS() {return VCUST_CONT_STATUS;}
	public Vector getVCUST_CONT_STATUS_FLAG() {return VCUST_CONT_STATUS_FLAG;}
	public Vector getVCUST_RATE() {return VCUST_RATE;}
	public Vector getVCUST_RATE_UNIT_NM() {return VCUST_RATE_UNIT_NM;}
	public Vector getVCUST_RATE_UNIT() {return VCUST_RATE_UNIT;}
	public Vector getVCUST_SUPPLIED_QTY() {return VCUST_SUPPLIED_QTY;}
	public Vector getVCUST_CONT_TYPE() {return VCUST_CONT_TYPE;}
	public Vector getVCUST_CONTRACT_TYPE_NM() {return VCUST_CONTRACT_TYPE_NM;}
	public Vector getVCUST_TRANSERABLE_QTY() {return VCUST_TRANSERABLE_QTY;}
	public Vector getVCUST_CONT_REF() {return VCUST_CONT_REF;}

	public Vector getVTOTAL_BALANCE_MMBTU() {return VTOTAL_BALANCE_MMBTU;}

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
