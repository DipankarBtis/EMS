package com.etrm.fms.mgmt_reports;

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

import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

public class DataBean_Govt_Reports
{
	String db_src_file_name="DataBean_Govt_Reports.java";
	
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
	
	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf6 = new DecimalFormat("###########0.000000");
	
	public void init()
	{
		String function_nm="init()";
		try
		{
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource)envContext.lookup(RuntimeConf.security_database);
			if (ds != null) 
			{
				conn = ds.getConnection();
				if (conn != null) 
				{
					if(callFlag.equalsIgnoreCase("SECTORAL_CONSUMPTION_RPT")) 
					{
						getSectoralConsumptionRpt();
					}
					if(callFlag.equalsIgnoreCase("SUPPLY_DEMAND_SECTOR_MASTER_RPT"))
					{
						getDemandNSupplySectorMstRpt();
					}
					if(callFlag.equalsIgnoreCase("PPAC_DEMAND_RPT"))
					{
						getStateCd();		//State code of BU
						getPPACDemandRpt();
					}
					if(callFlag.equalsIgnoreCase("TERMINAL_CAPACITY_UTILIZATION_MST"))
					{
						getFinancialYear();
						getTerminalUtilizationMstRpt();
					}
					if(callFlag.equalsIgnoreCase("TERMINAL_CAPACITY_UTILIZATION"))
					{
						getFinancialYear();
						getTerminalUtilizationRpt();
					}
					if(callFlag.equalsIgnoreCase("LNG_TERMINAL_RPT"))
					{
						getLNGTerminalRpt();
					}
					if(callFlag.equalsIgnoreCase("JODI_DATA_RPT"))
					{
						comp_nm=utilBean.getCompanyName(conn, comp_cd);
						getDescriptionNm();
						getJodiData();
					}
					if(callFlag.equalsIgnoreCase("CAP_MASTER"))
					{
						getCapacityType();
						getCapacityDtl();
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
	
	public void getSectoralConsumptionRpt()
	{
		String function_nm="getSectoralConsumptionRpt()";
		try
		{
			//String month_year=month+""+year;
			String from_dt="01/"+month+"/"+year;
			//String to_dt="01/"+month_to+"/"+year_to;
			String to_dt=utilDate.getLastDateOfMonth(month_to,year_to);
			String queryString="SELECT COMPANY_CD,MONTH_YEAR,SUP_COMP_CD,PRODUCT_CD, "
					+ "SECTOR_CD,REC_COMP_CD,STATE_CD,QTY_MMBTU,QTY_MMSCM,CAL_VAL "
					+ "FROM VIEW_GOVT_SECTOR_CONSUME_RPT "
					+ "WHERE COMPANY_CD=? "
					//+ "AND MONTH_YEAR=? "
					+ "AND TO_DATE(TO_CHAR(TO_DATE('01' || TRIM(MONTH_YEAR), 'DDMMYYYY'),'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE(?, 'DD/MM/YYYY') "	//AS PER VIJAY'S FEEDBACK TO SUPPORT THE FROM MONTH TO MONTH FILTER 
					+ "AND TO_DATE(TO_CHAR(TO_DATE('01' || TRIM(MONTH_YEAR), 'DDMMYYYY'),'DD/MM/YYYY'),'DD/MM/YYYY') <= TO_DATE(?, 'DD/MM/YYYY') ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, from_dt);
			stmt.setString(3, to_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String company_cd=rset.getString(1)==null?"":rset.getString(1);
				String mnt_yr=rset.getString(2)==null?"":rset.getString(2);
				String sup_comp_cd=rset.getString(3)==null?"":rset.getString(3);
				String product_cd=rset.getString(4)==null?"":rset.getString(4);
				String sector_cd=rset.getString(5)==null?"":rset.getString(5);
				String rec_comp_cd=rset.getString(6)==null?"":rset.getString(6);
				String state_cd=rset.getString(7)==null?"":rset.getString(7);
				String qty_mmbtu=rset.getString(8)==null?"":nf.format(rset.getDouble(8));
				String qty_mmscm=rset.getString(9)==null?"":nf.format(rset.getDouble(9));
				String cal_val=rset.getString(10)==null?"":nf.format(rset.getDouble(10));
				
				VMONTH_YEAR.add(mnt_yr);
				VSUPP_COMP_CD.add(sup_comp_cd);
				VPRODUCT_CD.add(product_cd);
				VSECTOR_CD.add(sector_cd);
				VRECEV_COMP_CD.add(rec_comp_cd);
				VSTATE_CD.add(state_cd);
				VQTY_MMBTU.add(qty_mmbtu);
				VQTY_MMSCM.add(qty_mmscm);
				VCALORIFIC_VAL.add(cal_val);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getDemandNSupplySectorMstRpt()
	{
		String function_nm="getDemandNSupplySectorMstRpt()";
		try
		{
			String start_dt="01/"+month+"/"+year;
			String end_dt=utilDate.getLastDateOfMonth(month, year);
			
			String queryString="SELECT ALLOC.COUNTERPARTY_CD, ALLOC.PLANT_SEQ, SUM(ALLOC.MMBTU) MMBTU, SUM(ALLOC.MMBTU)/38900 MMSCM, SUM(ALLOC.MMBTU)/52.08, "
					+ "LISTAGG(ALLOC.AGMT_NO||'@'||ALLOC.AGMT_REV||'@'||ALLOC.CONT_NO||'@'||ALLOC.CONT_REV||'@'||ALLOC.CONTRACT_TYPE, ',') WITHIN GROUP (ORDER BY ALLOC.CONT_NO) CONT_INFO "
					+ "FROM "
					+ "(SELECT COMPANY_CD,COUNTERPARTY_CD, PLANT_SEQ, SUM(QTY_MMBTU) MMBTU, SUM(QTY_MMBTU)/38900 MMSCM, SUM(QTY_MMBTU)/52.08 MT, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE "
					+ "FROM FMS_DLNG_ALLOC_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND A.NOM_REV_NO = (SELECT MAX(C.NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST C WHERE A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONT_NO=C.CONT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.PLANT_SEQ=C.PLANT_SEQ "
					+ "AND A.BU_SEQ=C.BU_SEQ AND A.GAS_DT=C.GAS_DT AND A.FILL_STATION_CD=C.FILL_STATION_CD AND A.BAY_CD=C.BAY_CD AND A.SLOT_START_TIME=C.SLOT_START_TIME "
					+ "	AND A.SLOT_END_TIME=C.SLOT_END_TIME AND A.TRUCK_TRANS_CD=C.TRUCK_TRANS_CD AND A.TRUCK_CD=C.TRUCK_CD AND A.CARGO_NO=C.CARGO_NO)"
					+ "AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY')  "
					+ "GROUP BY COMPANY_CD,COUNTERPARTY_CD,PLANT_SEQ,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE "
					+ "UNION ALL "
					+ "SELECT RLNG.COMPANY_CD,RLNG.COUNTERPARTY_CD,RLNG.PLANT_SEQ,SUM(RLNG.QTY_MMBTU) MMBTU,SUM(RLNG.QTY_MMBTU)/38900 MMSCM, SUM(RLNG.QTY_MMBTU)/52.08 MT, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE  "
					+ "FROM "
					+ "	(SELECT COMPANY_CD,COUNTERPARTY_CD,PLANT_SEQ,QTY_MMBTU, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,GAS_DT  "
					+ "		FROM FMS_DAILY_ALLOCATION_DTL A  "
					+ "		WHERE COMPANY_CD=?  "
					+ "		AND A.CONTRACT_TYPE IN ('S','L','X')  "
					+ "		AND A.NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
					+ "		WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "		AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "		AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
					+ "		AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
					+ "		AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) "
					+ "		AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ "		AND A.GAS_DT NOT IN (SELECT DISTINCT GAS_DT FROM FMS_DAILY_TRANSPORTER_ALLOC B  "
					+ "		WHERE B.COMPANY_CD=A.COMPANY_CD  "
					+ "		AND B.CONTRACT_TYPE='C'  "
					+ "		AND A.GAS_DT=B.GAS_DT  "
					+ "		AND B.SELL_CONT_MAP LIKE A.COUNTERPARTY_CD||'-'||A.CONTRACT_TYPE||'-'||A.AGMT_NO||'-%-'||A.CONT_NO||'-%') "
					+ "	UNION ALL "
					+ "	SELECT T.COMPANY_CD,TO_NUMBER(REGEXP_SUBSTR(T.SELL_CONT_MAP, '[^-]+', 1, 1)) AS COUNTERPARTY_CD, "
					+ "		TO_NUMBER(SUBSTR(T.EXIT_PT_MAPPING_ID,INSTR(T.EXIT_PT_MAPPING_ID,'-',-1)+1)) AS PLANT_SEQ, "
					+ "		T.EXIT_QTY_MMBTU QTY_MMBTU, TO_NUMBER(REGEXP_SUBSTR(T.SELL_CONT_MAP, '[^-]+', 1, 3)) AS AGMT_NO, "
					+ "		TO_NUMBER(REGEXP_SUBSTR(T.SELL_CONT_MAP, '[^-]+', 1, 4)) AS AGMT_REV, "
					+ "		TO_NUMBER(REGEXP_SUBSTR(T.SELL_CONT_MAP, '[^-]+', 1, 5)) AS CONT_NO, "
					+ "		TO_NUMBER(REGEXP_SUBSTR(T.SELL_CONT_MAP, '[^-]+', 1, 6)) AS CONT_REV, "
					+ "		REGEXP_SUBSTR(T.SELL_CONT_MAP, '[^-]+', 1, 2) AS CONTRACT_TYPE,T.GAS_DT "
					+ "		FROM FMS_DAILY_TRANSPORTER_ALLOC T "
					+ "		WHERE T.COMPANY_CD=? "
					+ "		AND T.CONTRACT_TYPE='C' "
					+ "		AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ "		AND T.ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B  "
					+ "		WHERE B.CONT_NO=T.CONT_NO AND B.AGMT_NO=T.AGMT_NO AND B.COMPANY_CD=T.COMPANY_CD  "
					+ "		AND B.COUNTERPARTY_CD=T.COUNTERPARTY_CD AND B.CONTRACT_TYPE=T.CONTRACT_TYPE  "
					+ "		AND B.SELL_CONT_MAP=T.SELL_CONT_MAP AND T.BU_SEQ=B.BU_SEQ  "
					+ "		AND B.GAS_DT=T.GAS_DT AND T.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID  "
					+ "		AND T.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) "
					+ "		) RLNG "
					+ "		GROUP BY RLNG.COMPANY_CD, RLNG.COUNTERPARTY_CD, RLNG.PLANT_SEQ, AGMT_NO, AGMT_REV,  "
					+ "		CONT_NO, CONT_REV,CONTRACT_TYPE"
					+ ") ALLOC  "
					+ "		GROUP BY ALLOC.COMPANY_CD,ALLOC.COUNTERPARTY_CD,ALLOC.PLANT_SEQ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, end_dt);
			stmt.setString(3, start_dt);
			stmt.setString(4, comp_cd);
			stmt.setString(5, end_dt);
			stmt.setString(6, start_dt);
			stmt.setString(7, comp_cd);
			stmt.setString(8, end_dt);
			stmt.setString(9, start_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String customer_cd=rset.getString(1)==null?"":rset.getString(1);
				String plant_seq=rset.getString(2)==null?"":rset.getString(2);
				String qty_mmbtu=rset.getString(3)==null?"":nf.format(rset.getDouble(3));
				String qty_mmscm=rset.getString(4)==null?"":nf.format(rset.getDouble(4));
				String qty_mt=rset.getString(5)==null?"":nf.format(rset.getDouble(5));
				String cont_dtl=rset.getString(6)==null?"":rset.getString(6);
				
				String customer_abbr=utilBean.getCounterpartyABBR(conn, customer_cd);
				String customer_nm=utilBean.getCounterpartyName(conn, customer_cd);
				String plant_nm=utilBean.getCounterpartyPlantName(conn, customer_cd, comp_cd, plant_seq, "C");
				String plant_sector_cd=getPlantSector(customer_cd, "C", plant_seq);
				String plant_sector_nm=utilBean.getSectorName(conn, plant_sector_cd, customer_cd);
				String plant_state_cd=getPlantStateCd(customer_cd, "C", plant_seq);
				String demand_sect_cd=getDemandSupplySectorCode(plant_sector_cd, "D");
				String supply_sect_cd=getDemandSupplySectorCode(plant_sector_cd, "S");
				
				String [] cont_dtl_arr = cont_dtl.split(",");
				String disp_deal_map="";
				for(int i=0;i<cont_dtl_arr.length;i++)
				{
					//agmt@agmt_rev@Cont@cont_rev@cont_type
					String dtl [] = cont_dtl_arr[i].split("@");
					
					String agmt = dtl[0];
					String agmt_rev=dtl[1];
					String cont = dtl[2];
					String cont_rev=dtl[3];
					String cont_type=dtl[4];
					
					String agmt_base="";
					String query="SELECT AGMT_BASE FROM FMS_SUPPLY_CONT_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? AND CONTRACT_TYPE=? "
							+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
					stmt1=conn.prepareStatement(query);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, customer_cd);
					stmt1.setString(3, agmt);
					stmt1.setString(4, agmt_rev);
					stmt1.setString(5, cont);
					stmt1.setString(6, cont_type);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						agmt_base=rset1.getString(1)==null?"":rset1.getString(1);
					}
					rset1.close();
					stmt1.close();
							
					String temp_disp_deal_map=utilBean.NewDealMappingId(comp_cd, customer_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
					
					if(agmt_base.equals("D") && (cont_type.equals("S") || cont_type.equals("L")))
					{
						temp_disp_deal_map=temp_disp_deal_map+" <font style='background: #a6ff4d;'>[DLV]</font>";
					}
					
					if(disp_deal_map.equals(""))
					{
						disp_deal_map+=temp_disp_deal_map;
					}
					else
					{
						disp_deal_map+=", "+temp_disp_deal_map;
					}
					
				}
				
				VDISPLAY_DEAL_MAP.add(disp_deal_map);
				
				VCOUNTERPARTY_CD.add(customer_cd);
				VCOUNTERPARTY_ABBR.add(customer_abbr);
				VCOUNTERPARTY_NM.add(customer_nm);
				VPLANT_SEQ_NO.add(plant_seq);
				VPLANT_NM.add(plant_nm);
				VPLANT_SECTOR_CD.add(plant_sector_cd);
				VPLANT_SECTOR_NM.add(plant_sector_nm);
				VSTATE_CD.add(plant_state_cd);
				VDEMAND_SECTOR_CD.add(demand_sect_cd);
				VSUPPLY_SECTOR_CD.add(supply_sect_cd);
				VQTY_MMBTU.add(qty_mmbtu);
				VQTY_MMSCM.add(qty_mmscm);
				VQTY_MT.add(qty_mt);
			}
			rset.close();
			stmt.close();
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String getPlantSector(String counterpty_cd, String entity, String plant_seq_no)
	{
		String function_nm="getPlantSector()";
		String sector="";
		try
		{
			String queryString="SELECT PLANT_SECTOR FROM FMS_COUNTERPARTY_PLANT_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=? "
					+ "AND SEQ_NO=? "
					+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
					+ "AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO) ";
			stmt0=conn.prepareStatement(queryString);
			stmt0.setString(1,  comp_cd);
			stmt0.setString(2, counterpty_cd);
			stmt0.setString(3, entity);
			stmt0.setString(4, plant_seq_no);
			rset0=stmt0.executeQuery();
			if(rset0.next())
			{
				sector=rset0.getString(1)==null?"":rset0.getString(1);
			}
			rset0.close();
			stmt0.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return sector;
	}
	
	public String getPlantStateCd(String counterpty_cd, String entity, String plant_seq_no)
	{
		String function_nm="getPlantStateCd()";
		String st_cd="";
		try
		{
			String state="";
			String queryString="SELECT PLANT_STATE FROM FMS_COUNTERPARTY_PLANT_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=? "
					+ "AND SEQ_NO=? "
					+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
					+ "AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO) ";
			stmt0=conn.prepareStatement(queryString);
			stmt0.setString(1,  comp_cd);
			stmt0.setString(2, counterpty_cd);
			stmt0.setString(3, entity);
			stmt0.setString(4, plant_seq_no);
			rset0=stmt0.executeQuery();
			if(rset0.next())
			{
				state=rset0.getString(1)==null?"":rset0.getString(1);
			}
			rset0.close();
			stmt0.close();
			
			String query="SELECT STATE_CODE FROM FMS_STATE_MST "
					+ "WHERE TRIM(STATE_NM)=TRIM(?) ";
			stmt0=conn.prepareStatement(query);
			stmt0.setString(1, state);
			rset0=stmt0.executeQuery();
			if(rset0.next())
			{
				st_cd=rset0.getString(1)==null?"":rset0.getString(1);
			}
			rset0.close();
			stmt0.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return st_cd;
	}
	
	public String getDemandSupplySectorCode(String sector_cd, String type)
	{
		/*type: 
		 	D: for demand
		   	S: for supply
		 */
		String function_nm="getDemandSupplySectorCode()";
		String code="";
		String demand_sect_cd="";
		String supply_sect_cd="";
		try
		{
			String queryString="SELECT DEMAND_SECT_CD,SUPPLY_SECT_CD "
					+ "FROM FMS_SECTOR_DTL A "
					+ "WHERE SECTOR_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SECTOR_DTL B "
					+ "WHERE A.SECTOR_CD=B.SECTOR_CD) ";
			stmt0=conn.prepareStatement(queryString);
			stmt0.setString(1, sector_cd);
			rset0=stmt0.executeQuery();
			if(rset0.next())
			{
				demand_sect_cd=rset0.getString(1)==null?"":rset0.getString(1);
				supply_sect_cd=rset0.getString(2)==null?"":rset0.getString(2);
			}
			rset0.close();
			stmt0.close();
			
			if(type.equals("D"))
			{
				code=demand_sect_cd;
			}
			else if(type.equals("S"))
			{
				code=supply_sect_cd;
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return code;
	}
	
	public void getStateCd()
	{
		String function_nm="getStateCd()";
		try
		{
			String query="SELECT A.SEQ_NO,A.PLANT_STATE,B.STATE_CODE "
					+ "FROM FMS_COUNTERPARTY_PLANT_DTL A,FMS_STATE_MST B "
					+ "WHERE A.COMPANY_CD=? AND A.ENTITY=? "
					+ "AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
					+ "AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO) "
					+ "AND TRIM(A.PLANT_STATE)=TRIM(B.STATE_NM) ";
			stmt1=conn.prepareStatement(query);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, "B");
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				String state_cd=rset1.getString(3)==null?"":rset1.getString(3);
				
				VMST_STATE_CD.add(state_cd);
			}
			rset1.close();
			stmt1.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getPPACDemandRpt()
	{
		String function_nm="getPPACDemandRpt()";
		try
		{
			String date = "01/"+month+"/"+year;
			String month_sht_nm=utilDate.getShortMonthName(date).toUpperCase();
			String mm_yy=month_sht_nm+"-"+year.substring(2);
			
			String queryString="";
			if(force_mark_gj.equals("Y"))
			{
				queryString="SELECT COMP_CD, PRODUCT_CD,TRANSPORT_MODE,'GJ' ST_CD,'GJ36' REV_ST_CD,CUSTOMER_CATEGORY, "
						+ "END_USE,SUM(QTY_MMBTU),SUM(QTY_MMSCM),SUM(QTY_MT),MMM_YY "
						+ "FROM VIEW_GOVT_PPAC_DEMAND_RPT "
						+ "WHERE COMPANY_CD=? "
						+ "AND MMM_YY=? ";
				if(!state_cd.equals("0"))
				{
					queryString+="AND STATE_CD=? ";
				}
				queryString+= "GROUP BY COMP_CD, PRODUCT_CD,TRANSPORT_MODE,CUSTOMER_CATEGORY,"
						+ "END_USE,MMM_YY ";
			}
			else
			{
				queryString="SELECT COMP_CD,PRODUCT_CD,TRANSPORT_MODE,STATE_CD,REVENUE_DISTRICT_CD, "
						+ "CUSTOMER_CATEGORY,END_USE,QTY_MMBTU,QTY_MMSCM,QTY_MT,MMM_YY "
						+ "FROM VIEW_GOVT_PPAC_DEMAND_RPT "
						+ "WHERE COMPANY_CD=? "
						+ "AND MMM_YY=? ";
				if(!state_cd.equals("0"))
				{
					queryString+="AND STATE_CD=? ";
				}
			}
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1,comp_cd);
			stmt.setString(2, mm_yy);
			if(!state_cd.equals("0"))
			{
				stmt.setString(3, state_cd);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String company_code=rset.getString(1)==null?"":rset.getString(1);
				String product_cd=rset.getString(2)==null?"":rset.getString(2);
				String transport_mode=rset.getString(3)==null?"":rset.getString(3);
				String state_cd=rset.getString(4)==null?"":rset.getString(4);
				String revenue_dist_cd=rset.getString(5)==null?"":rset.getString(5);
				String cust_category=rset.getString(6)==null?"":rset.getString(6);
				String end_use=rset.getString(7)==null?"":rset.getString(7);
				String qty_mmbtu=rset.getString(8)==null?"":nf.format(rset.getDouble(8));
				String qty_mmscm=rset.getString(9)==null?"":nf.format(rset.getDouble(9));
				String qty_mt=rset.getString(10)==null?"":nf.format(rset.getDouble(10));
				String month_year=rset.getString(11)==null?"":rset.getString(11);
				
				VCOMPANY_CODE.add(company_code);
				VPRODUCT_CD.addElement(product_cd);
				VTRANSPORT_MODE.add(transport_mode);
				VSTATE_CD.add(state_cd);
				VREVENUE_DIST_CD.add(revenue_dist_cd);
				VCUST_CATEGORY.add(cust_category);
				VEND_USE.add(end_use);
				VQTY_MMBTU.add(qty_mmbtu);
				VQTY_MMSCM.add(qty_mmscm);
				VQTY_MT.add(qty_mt);
				VMONTH_YEAR.add(month_year);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getTerminalUtilizationMstRpt()
	{
		String function_nm="getTerminalUtilizationMstRpt()";
		try
		{
			String query="SELECT FINANCIAL_YEAR,MONTH_YEAR,RLNG_SEND_OUT,DLNG_SEND_OUT, "
					+ "TOTAL_SEND_OUT,MONTH_DAYS,TOTAL_SEND_OUT_MMSCM,TOTAL_SEND_OUT_MMSCMD, "
					+ "NP_CAP,MMSCMD_BY_NP_CAP,MMTPA_CAP,NP_CAP_PER_MONTH,MMTPA_CAP_PER_MONTH, "
					+ "CAP_UTILIZE_PER,CUMULATIVE_CAP_UTILIZE_PER "
					+ "FROM VIEW_GOVT_TRML_CAP_UTL_MST "
					+ "WHERE COMPANY_CD=? "
					+ "AND FINANCIAL_YEAR=? ";
			stmt=conn.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, financial_year);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String financial_year=rset.getString(1)==null?"":rset.getString(1);
				String month_year=rset.getString(2)==null?"":rset.getString(2);
				String rlng_send_out=rset.getString(3)==null?"":nf6.format(rset.getDouble(3));
				String dlng_send_out=rset.getString(4)==null?"":nf6.format(rset.getDouble(4));
				String total_send_out=rset.getString(5)==null?"":nf6.format(rset.getDouble(5));
				String month_days=rset.getString(6)==null?"":rset.getString(6);
				String total_send_out_mmscm=rset.getString(7)==null?"":nf6.format(rset.getDouble(7));
				String total_send_out_mmscmd=rset.getString(8)==null?"":nf6.format(rset.getDouble(8));
				String np_cap=rset.getString(9)==null?"":nf6.format(rset.getDouble(9));
				String mmscmd_by_np=rset.getString(10)==null?"":nf6.format(rset.getDouble(10));
				String mmtpa_cap=rset.getString(11)==null?"":nf6.format(rset.getDouble(11));
				String np_cap_per_mth=rset.getString(12)==null?"":nf6.format(rset.getDouble(12));
				String mmtpa_cap_per_mth=rset.getString(13)==null?"":nf6.format(rset.getDouble(13));
				String cap_utilize_per=rset.getString(14)==null?"":nf6.format(rset.getDouble(14));
				String cum_cap_utilize_per=rset.getString(15)==null?"":nf6.format(rset.getDouble(15));
				
				VMONTH_YEAR.add(month_year);
				VRLNG_SEND_OUT_MMBTU.add(rlng_send_out);
				VDLNG_SEND_OUT_MMBTU.add(dlng_send_out);
				VTOTAL_SEND_OUT_MMBTU.add(total_send_out);
				VMONTH_DAYS.add(month_days);
				VTOTAL_SEND_OUT_MMSCM.add(total_send_out_mmscm);
				VTOTAL_SEND_OUT_MMSCMD.add(total_send_out_mmscmd);
				VNAMEPLATE_CAP.add(np_cap);
				VMMSCMD_BY_NP.add(mmscmd_by_np);
				VMMTPA_CAP.add(mmtpa_cap);
				VNAMEPLATE_CAP_PER_MTH.add(np_cap_per_mth);
				VMMTPA_CAP_PER_MTH.add(mmtpa_cap_per_mth);
				VCAP_UTILIZE_PER.add(cap_utilize_per);
				VCUM_CAP_UTILIZE_PER.add(cum_cap_utilize_per);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getFinancialYear()
	{
		String function_nm="getFinancialYear()";
		try
		{
			int year=utilDate.getCurrentYear();
			int month=utilDate.getCurrentMonth();
			
			int fin_start=0;
			if(month>=4)
			{
				fin_start=year;
			}
			else
			{
				fin_start=year-1;
			}
			
			for(int i=0;i<5;i++)
			{
				int start = fin_start-i;
				int end=start+1;
				
				String fin_year=start+"-"+end;
				VMST_FINANCIAL_YEAR.add(fin_year);
			}
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getTerminalUtilizationRpt()
	{
		String function_nm="getTerminalUtilizationRpt()";
		try
		{

			String query="SELECT FINANCIAL_YEAR,MONTH_YEAR,RLNG_SEND_OUT,DLNG_SEND_OUT, "
					+ "TOTAL_SEND_OUT,MONTH_DAYS,TOTAL_SEND_OUT_MMSCM,TOTAL_SEND_OUT_MMSCMD, "
					+ "NP_CAP,MMSCMD_BY_NP_CAP,MMTPA_CAP,NP_CAP_PER_MONTH,MMTPA_CAP_PER_MONTH, "
					+ "CAP_UTILIZE_PER,CUMULATIVE_CAP_UTILIZE_PER,TO_CHAR(LAST_DAY(TO_DATE(MONTH_YEAR,'MON-YY')),'DD/MM/YYYY') "
					+ "FROM VIEW_GOVT_TRML_CAP_UTL_MST "
					+ "WHERE COMPANY_CD=? "
					+ "AND FINANCIAL_YEAR=? ";
			stmt=conn.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, financial_year);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String financial_year=rset.getString(1)==null?"":rset.getString(1);
				String month_year=rset.getString(2)==null?"":rset.getString(2);
				String rlng_send_out=rset.getString(3)==null?"":nf.format(rset.getDouble(3));
				String dlng_send_out=rset.getString(4)==null?"":nf.format(rset.getDouble(4));
				String total_send_out=rset.getString(5)==null?"":nf.format(rset.getDouble(5));
				String month_days=rset.getString(6)==null?"":rset.getString(6);
				String total_send_out_mmscm=rset.getString(7)==null?"":nf.format(rset.getDouble(7));
				String total_send_out_mmscmd=rset.getString(8)==null?"":nf.format(rset.getDouble(8));
				String np_cap=rset.getString(9)==null?"":nf.format(rset.getDouble(9));
				String mmscmd_by_np=rset.getString(10)==null?"":nf.format(rset.getDouble(10));
				String mmtpa_cap=rset.getString(11)==null?"":nf.format(rset.getDouble(11));
				String np_cap_per_mth=rset.getString(12)==null?"":nf.format(rset.getDouble(12));
				String mmtpa_cap_per_mth=rset.getString(13)==null?"":nf.format(rset.getDouble(13));
				String cap_utilize_per=rset.getString(14)==null?"":nf.format(rset.getDouble(14));
				String cum_cap_utilize_per=rset.getString(15)==null?"":nf.format(rset.getDouble(15));
				String lastDtOfMonth=rset.getString(16)==null?"":rset.getString(16);
				
				String loaction = "Hazira"; 	//fixed
				String promoters=utilBean.getCompanyName(conn, comp_cd);	//as per profile
				
				String avg_val = np_cap.equals("")?"":nf.format(Double.parseDouble(np_cap)/12);
				
				VLOCATION.add(loaction);
				VPROMOTERS.add(promoters);
				VMONTH_END_DT.add(lastDtOfMonth);
				VNAMEPLATE_CAP.add(np_cap);
				VAVG_CAP.add(avg_val);
				VAVAILABLE_CAP.add(avg_val);
				VCAP_UTILIZE_PER.add(cap_utilize_per);
				VCUM_CAP_UTILIZE_PER.add(cum_cap_utilize_per);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getLNGTerminalRpt()
	{
		String function_nm="getLNGTerminalRpt()";
		try
		{
			String from_dt = "01/"+month+"/"+year;
			String to_dt=utilDate.getLastDateOfMonth(month_to,year_to);
			
			String query="SELECT FINANCIAL_YEAR,MONTH_YEAR,RLNG_SEND_OUT,DLNG_SEND_OUT, "
					+ "TOTAL_SEND_OUT,MONTH_DAYS,TOTAL_SEND_OUT_MMSCM,TOTAL_SEND_OUT_MMSCMD, "
					+ "NP_CAP,MMSCMD_BY_NP_CAP,MMTPA_CAP,NP_CAP_PER_MONTH,MMTPA_CAP_PER_MONTH, "
					+ "CAP_UTILIZE_PER,CUMULATIVE_CAP_UTILIZE_PER,TO_CHAR(TO_DATE(MONTH_YEAR,'MON-YY'),'MMYYYY'),TO_CHAR(LAST_DAY(TO_DATE(MONTH_YEAR,'MON-YY')),'DD/MM/YYYY')  "
					+ "FROM VIEW_GOVT_TRML_CAP_UTL_MST "
					+ "WHERE COMPANY_CD=? "
					+ "AND TO_DATE(TO_CHAR(TO_DATE('01' || TRIM(MONTH_YEAR), 'DD-MON-YY'),'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE(?, 'DD/MM/YYYY') "	 
					+ "AND TO_DATE(TO_CHAR(TO_DATE('01' || TRIM(MONTH_YEAR), 'DD-MON-YY'),'DD/MM/YYYY'),'DD/MM/YYYY') <= TO_DATE(?, 'DD/MM/YYYY') ";
			stmt=conn.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, from_dt);
			stmt.setString(3, to_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String financial_year=rset.getString(1)==null?"":rset.getString(1);
				String month_year=rset.getString(2)==null?"":rset.getString(2);
				String rlng_send_out=rset.getString(3)==null?"":nf.format(rset.getDouble(3));
				String dlng_send_out=rset.getString(4)==null?"":nf.format(rset.getDouble(4));
				String total_send_out=rset.getString(5)==null?"":nf.format(rset.getDouble(5));
				String month_days=rset.getString(6)==null?"":rset.getString(6);
				String total_send_out_mmscm=rset.getString(7)==null?"":nf.format(rset.getDouble(7));
				String total_send_out_mmscmd=rset.getString(8)==null?"":nf.format(rset.getDouble(8));
				String np_cap=rset.getString(9)==null?"":nf.format(rset.getDouble(9));
				String mmscmd_by_np=rset.getString(10)==null?"":nf.format(rset.getDouble(10));
				String mmtpa_cap=rset.getString(11)==null?"":nf.format(rset.getDouble(11));
				String np_cap_per_mth=rset.getString(12)==null?"":nf.format(rset.getDouble(12));
				String mmtpa_cap_per_mth=rset.getString(13)==null?"":nf.format(rset.getDouble(13));
				String cap_utilize_per=rset.getString(14)==null?"":nf.format(rset.getDouble(14));
				String cum_cap_utilize_per=rset.getString(15)==null?"":nf.format(rset.getDouble(15));
				String monthYear=rset.getString(16)==null?"":rset.getString(16);
				String lastDtOfMonth=rset.getString(17)==null?"":rset.getString(17);
				
				String terminal_cd=comp_cd.equals("2")?"LNG1180GJ36":"";
				String terminal_cap=np_cap;	
				String no_tanks=comp_cd.equals("2")?"2":"";
				
				VMONTH_YEAR.add(monthYear);
				VTERMINAL_CD.add(terminal_cd);
				VTERMINAL_CAP.add(terminal_cap);
				VCUM_CAP_UTILIZE_PER.add(cum_cap_utilize_per);
				VNO_TANKS.add(no_tanks);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getDescriptionNm()
	{
		String function_nm="getDescriptionNm()";
		try
		{
			//for storing natural gas description 
			VNATURAL_GAS_DESC.add("Opening Stock");
			VNATURAL_GAS_DESC.add("Indigenous Production");
			VNATURAL_GAS_DESC.add("Imports");
			VNATURAL_GAS_DESC.add("Pipeline");
			VNATURAL_GAS_DESC.add("Exports");
			VNATURAL_GAS_DESC.add("Pipeline");
			VNATURAL_GAS_DESC.add("Stock Change");
			VNATURAL_GAS_DESC.add("Gross Inland Deliveries");
			VNATURAL_GAS_DESC.add("Statistical Difference");
			VNATURAL_GAS_DESC.add("Gross Inland Deliveries Observed");
			VNATURAL_GAS_DESC.add("of which: Power Generation");
			VNATURAL_GAS_DESC.add("Closing level of stock held");
			
			//for storing the LNG details
			VLNG_DESC.add("Opening Stock");
			VLNG_DESC.add("Quantity of imports <b>(at m minus 160&deg; C) (in TeraJoules)</b>");
			VLNG_DESC.add("Gross Inland Deliveries <b>(in MMBTU)</b>");
			VLNG_DESC.add("Gross Inland Deliveries <b>(in TeraJoules)</b>");
			VLNG_DESC.add("&nbsp;&nbsp;of which: Power Generation <b>(in TeraJoules)</b>");
			VLNG_DESC.add("Closing level of stock held");
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getJodiData()
	{
		String function_nm="getJodiData()";
		try
		{
			String month_yr=month+""+year;
			String query="SELECT MONTH_YEAR,SALES_MMBTU,SALES_MMSCM,SALES_TERA_JOULES, "
					+ "UNLOADED_MMBTU,UNLOADED_MMSCM,UNLOADED_TERA_JOULES "
					+ "FROM VIEW_GOVT_JODI_REPORT "
					+ "WHERE COMPANY_CD=? AND MONTH_YEAR=? ";
			stmt=conn.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, month_yr);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				String month_year=rset.getString(1)==null?"":rset.getString(1);
				sales_mmbtu=rset.getString(2)==null?"":nf.format(rset.getDouble(2));
				sales_mmscm=rset.getString(3)==null?"":nf.format(rset.getDouble(3));
				sales_tera_joules=rset.getString(4)==null?"":nf.format(rset.getDouble(4));
				unloaded_mmbtu=rset.getString(5)==null?"":nf.format(rset.getDouble(5));
				String unloaded_mmscm=rset.getString(6)==null?"":nf.format(rset.getDouble(6));
				String unloaded_tera_joules=rset.getString(7)==null?"":nf.format(rset.getDouble(7));
				
				VUNLOADED_DATA.add("NA");
				VUNLOADED_DATA.add(nf.format(0));
				VUNLOADED_DATA.add(unloaded_mmbtu);
				VUNLOADED_DATA.add(unloaded_tera_joules);
				VUNLOADED_DATA.add(nf.format(0));
				VUNLOADED_DATA.add("NA");
			}
			else
			{
				sales_mmbtu=nf.format(0);
				sales_mmscm=nf.format(0);
				sales_tera_joules=nf.format(0);
				unloaded_mmbtu=nf.format(0);
				String unloaded_mmscm=nf.format(0);
				String unloaded_tera_joules=nf.format(0);
				
				VUNLOADED_DATA.add("NA");
				VUNLOADED_DATA.add(nf.format(0));
				VUNLOADED_DATA.add(unloaded_mmbtu);
				VUNLOADED_DATA.add(unloaded_tera_joules);
				VUNLOADED_DATA.add(nf.format(0));
				VUNLOADED_DATA.add("NA");
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCapacityType()
	{
		String function_nm="getCapacityType()";
		try
		{
			VMST_CAP_TYPE.add("N");	//Nameplate Capacity
			VMST_CAP_TYPE.add("M"); //MMSCMD/Nameplate capacity
			
			VMST_CAP_TYPE_NM.add(getCapacityNm("N"));
			VMST_CAP_TYPE_NM.add(getCapacityNm("M"));
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String getCapacityNm(String type)
	{
		String function_nm="getCapacityNm()";
		String cap_nm="";
		try
		{
			if(type.equals("N"))
			{
				cap_nm="Nameplate Capacity";
			}
			else if(type.equals("M"))
			{
				cap_nm="MMSCMD/Nameplate capacity";
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return cap_nm;
	}
	
	public void getCapacityDtl()
	{
		String function_nm="getCapacityDtl()";
		try
		{
			String from_dt="01/"+month+"/"+year;
			String to_dt="01/"+month_to+"/"+year_to;
			String query="SELECT CAP_TYPE,CAP_VALUE, TO_CHAR(EFF_DT,'MON-YYYY'), "
					+ "TO_CHAR(EFF_DT,'MM'),TO_CHAR(EFF_DT,'YYYY') "
					+ "FROM FMS_GOVT_RPT_CONST "
					+ "WHERE COMPANY_CD=? "
					+ "AND EFF_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND EFF_DT<=TO_DATE(?,'DD/MM/YYYY')";
			stmt=conn.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, from_dt);
			stmt.setString(3, to_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String cap_type=rset.getString(1)==null?"":rset.getString(1);
				String cap_val=rset.getString(2)==null?"":rset.getString(2);
				String month_yr=rset.getString(3)==null?"":rset.getString(3);
				String cap_month=rset.getString(4)==null?"":rset.getString(4);
				String cap_year=rset.getString(5)==null?"":rset.getString(5);
				
				VCAP_TYPE.add(cap_type);
				VCAP_VAL.add(cap_val);
				VMONTH_YEAR.add(month_yr);
				VCAP_NM.add(getCapacityNm(cap_type));
				VMONTH.add(cap_month);
				VYEAR.add(cap_year);
			}
			rset.close();
			stmt.close();
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
	
	String month = "";
	public void setMonth(String month) {this.month = month;}
	
	String year = "";
	public void setYear(String year) {this.year=year;}
	
	String month_to = "";
	public void setMonth_to(String month_to) {this.month_to = month_to;}
	
	String year_to = "";
	public void setYear_to(String year_to) {this.year_to=year_to;}
	
	String state_cd = "";
	public void setState_cd(String state_cd) {this.state_cd=state_cd;}
	
	String financial_year = "";
	public void setFinancial_year(String financial_year) {this.financial_year=financial_year;}
	
	String force_mark_gj="";
	public void setForce_mark_gj(String force_mark_gj) {this.force_mark_gj=force_mark_gj;}
	
	String comp_nm="";
	String sales_mmbtu="";
	String sales_mmscm="";
	String sales_tera_joules="";
	String unloaded_mmbtu="";
	
	public String getComp_nm() {return comp_nm;}
	public String getSales_mmbtu() {return sales_mmbtu;}
	public String getSales_mmscm() {return sales_mmscm;}
	public String getSales_tera_joules() {return sales_tera_joules;}
	public String getUnloaded_mmbtu() {return unloaded_mmbtu;}
	
	Vector VMONTH_YEAR = new Vector();
	Vector VSUPP_COMP_CD = new Vector();
	Vector VPRODUCT_CD = new Vector();
	Vector VSECTOR_CD = new Vector();
	Vector VRECEV_COMP_CD = new Vector();
	Vector VSTATE_CD = new Vector();
	Vector VQTY_MMBTU = new Vector();
	Vector VQTY_MMSCM = new Vector();
	Vector VCALORIFIC_VAL = new Vector();
	Vector VQTY_MT = new Vector();
	
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VPLANT_SEQ_NO = new Vector();
	Vector VPLANT_NM = new Vector();
	Vector VPLANT_SECTOR_CD = new Vector();
	Vector VPLANT_SECTOR_NM = new Vector();
	Vector VDISPLAY_DEAL_MAP = new Vector();
	Vector VDEMAND_SECTOR_CD = new Vector();
	Vector VSUPPLY_SECTOR_CD = new Vector();
	
	Vector VMST_STATE_CD = new Vector();
	
	Vector VCOMPANY_CODE = new Vector();
	Vector VTRANSPORT_MODE = new Vector();
	Vector VREVENUE_DIST_CD = new Vector();
	Vector VCUST_CATEGORY = new Vector();
	Vector VEND_USE = new Vector();
	
	Vector VRLNG_SEND_OUT_MMBTU = new Vector();
	Vector VDLNG_SEND_OUT_MMBTU = new Vector();
	Vector VTOTAL_SEND_OUT_MMBTU = new Vector();
	Vector VMONTH_DAYS = new Vector();
	Vector VTOTAL_SEND_OUT_MMSCM = new Vector();
	Vector VTOTAL_SEND_OUT_MMSCMD = new Vector();
	Vector VNAMEPLATE_CAP = new Vector();
	Vector VMMSCMD_BY_NP = new Vector();
	Vector VMMTPA_CAP = new Vector();
	Vector VNAMEPLATE_CAP_PER_MTH = new Vector();
	Vector VMMTPA_CAP_PER_MTH = new Vector();
	Vector VCAP_UTILIZE_PER = new Vector();
	Vector VCUM_CAP_UTILIZE_PER = new Vector();
	Vector VMST_FINANCIAL_YEAR = new Vector(); 
	
	Vector VLOCATION = new Vector();
	Vector VPROMOTERS = new Vector();
	Vector VMONTH_END_DT = new Vector();
	Vector VAVG_CAP = new Vector();
	Vector VAVAILABLE_CAP = new Vector();

	Vector VTERMINAL_CD = new Vector();
	Vector VTERMINAL_CAP = new Vector();
	Vector VNO_TANKS = new Vector();
	
	Vector VNATURAL_GAS_DESC = new Vector();
	Vector VLNG_DESC = new Vector();
	Vector VUNLOADED_DATA = new Vector();
	
	Vector VMST_CAP_TYPE = new Vector();
	Vector VMST_CAP_TYPE_NM = new Vector();
	Vector VCAP_TYPE = new Vector();
	Vector VCAP_VAL = new Vector();
	Vector VCAP_NM = new Vector();
	Vector VMONTH = new Vector();
	Vector VYEAR = new Vector();
	
	public Vector getVMONTH_YEAR() {return VMONTH_YEAR;}
	public Vector getVSUPP_COMP_CD() {return VSUPP_COMP_CD;}
	public Vector getVPRODUCT_CD() {return VPRODUCT_CD;}
	public Vector getVSECTOR_CD() {return VSECTOR_CD;}
	public Vector getVRECEV_COMP_CD() {return VRECEV_COMP_CD;}
	public Vector getVSTATE_CD() {return VSTATE_CD;}
	public Vector getVQTY_MMBTU() {return VQTY_MMBTU;}
	public Vector getVQTY_MMSCM() {return VQTY_MMSCM;}
	public Vector getVCALORIFIC_VAL() {return VCALORIFIC_VAL;}
	public Vector getVQTY_MT() {return VQTY_MT;}

	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVPLANT_SEQ_NO() {return VPLANT_SEQ_NO;}
	public Vector getVPLANT_NM() {return VPLANT_NM;}
	public Vector getVPLANT_SECTOR_CD() {return VPLANT_SECTOR_CD;}
	public Vector getVPLANT_SECTOR_NM() {return VPLANT_SECTOR_NM;}
	public Vector getVDISPLAY_DEAL_MAP() {return VDISPLAY_DEAL_MAP;}
	public Vector getVDEMAND_SECTOR_CD() {return VDEMAND_SECTOR_CD;}
	public Vector getVSUPPLY_SECTOR_CD() {return VSUPPLY_SECTOR_CD;}

	public Vector getVMST_STATE_CD() {return VMST_STATE_CD;}
	
	public Vector getVCOMPANY_CODE() {return VCOMPANY_CODE;}
	public Vector getVTRANSPORT_MODE() {return VTRANSPORT_MODE;}
	public Vector getVREVENUE_DIST_CD() {return VREVENUE_DIST_CD;}
	public Vector getVCUST_CATEGORY() {return VCUST_CATEGORY;}
	public Vector getVEND_USE() {return VEND_USE;}
	
	public Vector getVRLNG_SEND_OUT_MMBTU() {return VRLNG_SEND_OUT_MMBTU;}
	public Vector getVDLNG_SEND_OUT_MMBTU() {return VDLNG_SEND_OUT_MMBTU;}
	public Vector getVTOTAL_SEND_OUT_MMBTU() {return VTOTAL_SEND_OUT_MMBTU;}
	public Vector getVMONTH_DAYS() {return VMONTH_DAYS;}
	public Vector getVTOTAL_SEND_OUT_MMSCM() {return VTOTAL_SEND_OUT_MMSCM;}
	public Vector getVTOTAL_SEND_OUT_MMSCMD() {return VTOTAL_SEND_OUT_MMSCMD;}
	public Vector getVNAMEPLATE_CAP() {return VNAMEPLATE_CAP;}
	public Vector getVMMSCMD_BY_NP() {return VMMSCMD_BY_NP;}
	public Vector getVMMTPA_CAP() {return VMMTPA_CAP;}
	public Vector getVNAMEPLATE_CAP_PER_MTH() {return VNAMEPLATE_CAP_PER_MTH;}
	public Vector getVMMTPA_CAP_PER_MTH() {return VMMTPA_CAP_PER_MTH;}
	public Vector getVCAP_UTILIZE_PER() {return VCAP_UTILIZE_PER;}
	public Vector getVCUM_CAP_UTILIZE_PER() {return VCUM_CAP_UTILIZE_PER;}
	public Vector getVMST_FINANCIAL_YEAR() {return VMST_FINANCIAL_YEAR;}
	
	public Vector getVLOCATION() {return VLOCATION;}
	public Vector getVPROMOTERS() {return VPROMOTERS;}
	public Vector getVMONTH_END_DT() {return VMONTH_END_DT;}
	public Vector getVAVG_CAP() {return VAVG_CAP;}
	public Vector getVAVAILABLE_CAP() {return VAVAILABLE_CAP;}
	
	public Vector getVTERMINAL_CD() {return VTERMINAL_CD;}
	public Vector getVTERMINAL_CAP() {return VTERMINAL_CAP;}
	public Vector getVNO_TANKS() {return VNO_TANKS;}
	
	public Vector getVNATURAL_GAS_DESC() {return VNATURAL_GAS_DESC;}
	public Vector getVLNG_DESC() {return VLNG_DESC;}
	public Vector getVUNLOADED_DATA() {return VUNLOADED_DATA;}
	public Vector getVMST_CAP_TYPE() {return VMST_CAP_TYPE;}
	public Vector getVMST_CAP_TYPE_NM() {return VMST_CAP_TYPE_NM;}
	public Vector getVCAP_TYPE() {return VCAP_TYPE;}
	public Vector getVCAP_VAL() {return VCAP_VAL;}
	public Vector getVCAP_NM() {return VCAP_NM;}
	public Vector getVMONTH() {return VMONTH;}
	public Vector getVYEAR() {return VYEAR;}
}