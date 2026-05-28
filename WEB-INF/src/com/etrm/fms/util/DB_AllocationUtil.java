package com.etrm.fms.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;

//Coded By          : Harsh Patel
//Code Reviewed by	:  
//CR Date			: 22/09/2022 
//Status	  		: Developing
public class DB_AllocationUtil 
{
	String db_src_file_name="DB_AllocationUtil.java";
	
	PreparedStatement stmtement,stmtement1,stmtement2,stmtement3;
	ResultSet resultset,resultset1,resultset2,resultset3;
	String query = "";
	String query1="";
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	
	//SUPPLY ALLOCATION QTY PLANT, BU AND DATE RANGE WISE
	public Double getSupplyAllocationQty(Connection conn, String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String contract_type, String plant_seq, String bu_plant_seq, String period_start_dt, String period_end_dt, String cargo_no) throws Exception
	{
		String function_nm="getSupplyAllocationQty()";
		double qty=0;
		try
		{
			if(conn != null)
			{
				query="SELECT SUM(QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
						+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cont_no);
				stmtement.setString(2, agmt_no);
				stmtement.setString(3, comp_cd);
				stmtement.setString(4, counterparty_cd);
				stmtement.setString(5, plant_seq);
				stmtement.setString(6, contract_type);
				stmtement.setString(7, bu_plant_seq);
				stmtement.setString(8, period_start_dt);
				stmtement.setString(9, period_end_dt);
				stmtement.setString(10, cargo_no);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					qty=resultset.getDouble(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return qty;
	}
	
	//DELIVERED SUPPLY ALLOCATION QTY PLANT, BU AND DATE RANGE WISE
	public Double getDeliverdSupplyAllocationQty(Connection conn ,String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String contract_type, String plant_seq, String bu_plant_seq, String period_start_dt, String period_end_dt) throws Exception
	{
		String function_nm="getDeliverdSupplyAllocationQty()";
		double qty=0;
		try
		{
			if(conn!=null)
			{
				String cont_map=counterparty_cd+"-"+contract_type+"-"+agmt_no+"-%-"+cont_no+"-%";
				String exit_point="C-"+counterparty_cd+"-"+plant_seq;
				
				query="SELECT SUM(EXIT_QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
		  				+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
		  				+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND EXIT_PT_MAPPING_ID=? "
						+ "AND SELL_CONT_MAP LIKE ? AND BU_SEQ=? "
						+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, comp_cd);
				stmtement.setString(2, "C");
				stmtement.setString(3, period_start_dt);
				stmtement.setString(4, period_end_dt);
				stmtement.setString(5, exit_point);
				stmtement.setString(6, cont_map);
				stmtement.setString(7, bu_plant_seq);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					qty=resultset.getDouble(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{ 	
			throw e;
		} 	
		
		return qty;
	}
	
	//DELIVERED SUPPLY ALLOCATION QTY PLANT, BU WISE
	public Double getDeliverdSupplyAllocationQty(Connection conn ,String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String contract_type, String plant_seq, String bu_plant_seq) throws Exception
	{
		String function_nm="getDeliverdSupplyAllocationQty()";
		double qty=0;
		try
		{
			if(conn!=null)
			{
				String cont_map=counterparty_cd+"-"+contract_type+"-"+agmt_no+"-%-"+cont_no+"-%";
				String exit_point="C-"+counterparty_cd+"-"+plant_seq;
				
				query="SELECT SUM(EXIT_QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
		  				+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
		  				+ "AND EXIT_PT_MAPPING_ID=? "
						+ "AND SELL_CONT_MAP LIKE ? AND BU_SEQ=? "
						+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, comp_cd);
				stmtement.setString(2, "C");
				stmtement.setString(3, exit_point);
				stmtement.setString(4, cont_map);
				stmtement.setString(5, bu_plant_seq);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					qty=resultset.getDouble(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{ 	
			throw e;
		} 	
		
		return qty;
	}
	
	//DELIVERED SUPPLY ALLOCATION SCM PLANT, BU AND DATE RANGE WISE...........Added By Arth Patel on 20240503 for Getting SCM for Buyer vs Seller Nomination vs Allocation
	public Double getDeliverdSupplyAllocationScm(Connection conn,String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String contract_type, String plant_seq, String bu_plant_seq, String period_start_dt, String period_end_dt) throws Exception
	{
		String function_nm="getDeliverdSupplyAllocationScm()";
		double scm=0;
		try
		{
			if(conn!=null)
			{
				String cont_map=counterparty_cd+"-"+contract_type+"-"+agmt_no+"-%-"+cont_no+"-%";
				String exit_point="C-"+counterparty_cd+"-"+plant_seq;
				
				query="SELECT SUM(EXIT_QTY_SCM) "
		  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
		  				+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
		  				+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND EXIT_PT_MAPPING_ID=? "
						+ "AND SELL_CONT_MAP LIKE ? AND BU_SEQ=? "
						+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, comp_cd);
				stmtement.setString(2, "C");
				stmtement.setString(3, period_start_dt);
				stmtement.setString(4, period_end_dt);
				stmtement.setString(5, exit_point);
				stmtement.setString(6, cont_map);
				stmtement.setString(7, bu_plant_seq);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					scm=resultset.getDouble(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{ 	
			throw e;
		} 	
		
		return scm;
	}
	
	//SUPPLY ALLOCATION QTY CONTRACT AND DATE RANGE WISE
	public String getSupplyAllocationQty(Connection conn,String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String contract_type, String start_dt, String end_dt, String cargo_no) throws Exception
	{
		String function_nm="getSupplyAllocationQty()";
		String qty="";
		try
		{
			if(conn!=null)
			{
				query="SELECT SUM(QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONTRACT_TYPE=? "
						+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cont_no);
				stmtement.setString(2, agmt_no);
				stmtement.setString(3, comp_cd);
				stmtement.setString(4, counterparty_cd);
				stmtement.setString(5, contract_type);
				stmtement.setString(6, start_dt);
				stmtement.setString(7, end_dt);
				stmtement.setString(8, cargo_no);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					qty=nf.format(resultset.getDouble(1));
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return qty;
	}
	
	//SUPPLY ALLOCATION QTY CONTRACT AND DATE WISE
	public String getSupplyAllocationQty(Connection conn,String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String contract_type, String date, String cargo_no) throws Exception
	{
		String function_nm="getSupplyAllocationQty()";
		String qty="";
		try
		{
			if(conn!=null)
			{
				query="SELECT SUM(QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONTRACT_TYPE=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cont_no);
				stmtement.setString(2, agmt_no);
				stmtement.setString(3, comp_cd);
				stmtement.setString(4, counterparty_cd);
				stmtement.setString(5, contract_type);
				stmtement.setString(6, date);
				stmtement.setString(7, cargo_no);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					qty=nf.format(resultset.getDouble(1));
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return qty;
	}
	
	//SUPPLY ALLOCATION SCM CONTRACT AND DATE WISE...........Added By Arth Patel on 20240503 for Getting SCM for Buyer vs Seller Nomination vs Allocation
	public String getSupplyAllocationScm(Connection conn,String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String contract_type, String date, String cargo_no) throws Exception
	{
		String function_nm="getSupplyAllocationScm()";
		String scm="";
		try
		{
			if(conn!=null)
			{
				query="SELECT SUM(QTY_SCM) "
		  				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONTRACT_TYPE=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cont_no);
				stmtement.setString(2, agmt_no);
				stmtement.setString(3, comp_cd);
				stmtement.setString(4, counterparty_cd);
				stmtement.setString(5, contract_type);
				stmtement.setString(6, date);
				stmtement.setString(7, cargo_no);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					scm=nf.format(resultset.getDouble(1));
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return scm;
	}

	//DELIVERED SUPPLY ALLOCATION QTY CONTRACT AND DATE WISE
	public String getDeliverdSupplyAllocationQty(Connection conn,String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String contract_type, String date) throws Exception
	{
		String function_nm="getDeliverdSupplyAllocationQty()";
		String qty="";
		try
		{
			if(conn!=null)
			{
				String cont_map=counterparty_cd+"-"+contract_type+"-"+agmt_no+"-%-"+cont_no+"-%";
				
				query="SELECT SUM(EXIT_QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
		  				+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
		  				+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND SELL_CONT_MAP LIKE ? "
						+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, comp_cd);
				stmtement.setString(2, "C");
				stmtement.setString(3, date);
				stmtement.setString(4, cont_map);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					qty=nf.format(resultset.getDouble(1));
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return qty;
	}
	
	//SUPPLY ALLOCATION QTY CONTRACT WISE
	public String getSupplyAllocationQty(Connection conn,String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String contract_type, String cargo_no) throws Exception
	{
		String function_nm="getSupplyAllocationQty()";
		String qty="";
		try
		{
			if(conn!=null)
			{
				query="SELECT SUM(QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONTRACT_TYPE=? AND CARGO_NO=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cont_no);
				stmtement.setString(2, agmt_no);
				stmtement.setString(3, comp_cd);
				stmtement.setString(4, counterparty_cd);
				stmtement.setString(5, contract_type);
				stmtement.setString(6, cargo_no);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					qty=nf.format(resultset.getDouble(1));
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return qty;
	}
	
	//DELIVERED SUPPLY ALLOCATION QTY CONTRACT WISE
	public String getDeliverdSupplyAllocationQty(Connection conn,String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String contract_type) throws Exception
	{
		String function_nm="getDeliverdSupplyAllocationQty()";
		String qty="";
		try
		{
			if(conn!=null)
			{
				String cont_map=counterparty_cd+"-"+contract_type+"-"+agmt_no+"-%-"+cont_no+"-%";
				
				query="SELECT SUM(EXIT_QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
		  				+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND SELL_CONT_MAP LIKE ? "
						+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, comp_cd);
				stmtement.setString(2, "C");
				stmtement.setString(3, cont_map);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					qty=nf.format(resultset.getDouble(1));
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return qty;
	}
	
	//BEST SUPPLY ALLOCATION QTY CONTRACT WISE
	public String getBestSupplyAllocationQty(Connection conn,String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String contract_type,String start_dt,String end_dt,String agmt_base, String cargo_no) throws Exception
	{
		String function_nm="getBestSupplyAllocationQty()";
		String qty="";
		try
		{
			if(conn!=null)
			{
				String cont_map=counterparty_cd+"-"+contract_type+"-"+agmt_no+"-%-"+cont_no+"-%";
				int st_count=0;
				
				//PB20250529: Added for DLNG
				if(contract_type.equals("F")||contract_type.equals("E")||contract_type.equals("W"))
				{
					query="SELECT SUM(QTY_MMBTU) FROM FMS_DLNG_ALLOC_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
							+ "AND CONT_NO=?  AND CONTRACT_TYPE=? "
							+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD "
							+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ AND B.GAS_DT=A.GAS_DT "
							+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND A.TRUCK_CD=B.TRUCK_CD "
							+ "AND B.FILL_STATION_CD=A.FILL_STATION_CD AND B.BAY_CD=A.BAY_CD "
							+ "AND B.SLOT_START_TIME=A.SLOT_START_TIME AND B.SLOT_END_TIME=A.SLOT_END_TIME "
							+ "AND B.CARGO_NO=A.CARGO_NO "
							+ ") ";
					stmtement=conn.prepareStatement(query);
					stmtement.setString(1, comp_cd);
					stmtement.setString(2, counterparty_cd);
					stmtement.setString(3, agmt_no);
					stmtement.setString(4, cont_no);
					stmtement.setString(5, contract_type);
					stmtement.setString(6, start_dt);
					stmtement.setString(7, end_dt);
					resultset=stmtement.executeQuery();
					if(resultset.next())
					{
						qty = nf.format(resultset.getDouble(1));
					}
					resultset.close();
					stmtement.close();
				}
				else
				{
					if(agmt_base.equals("D"))
					{
						query = "SELECT SUM(ALLOC_QTY) " 
								+ "FROM "
								+ "(SELECT SUM(EXIT_QTY_MMBTU) ALLOC_QTY "
								+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A " 
								+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
								+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND SELL_CONT_MAP LIKE ? "
								+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
								+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
								+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
								+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
								+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) "
								+ "UNION " 
								+ "SELECT SUM(QTY_MMBTU) ALLOC_QTY "
								+ "FROM FMS_DAILY_ALLOCATION_DTL A " 
								+ "WHERE CONT_NO=? AND AGMT_NO=? "
								+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND CONTRACT_TYPE=? AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
								+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
								+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
								+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
								+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
								+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
								+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) " 
								+ "AND GAS_DT NOT IN (SELECT DISTINCT GAS_DT FROM FMS_DAILY_TRANSPORTER_ALLOC A "
								+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND SELL_CONT_MAP LIKE ? ))";
					}
					else
					{
						query="SELECT SUM(QTY_MMBTU) "
								+ "FROM FMS_DAILY_ALLOCATION_DTL A "
								+ "WHERE CONT_NO=? AND AGMT_NO=? "
								+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND CONTRACT_TYPE=? AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
								+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
								+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
								+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
								+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
								+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
								+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
					}
					stmtement = conn.prepareStatement(query);
					if(agmt_base.equals("D"))
					{
						stmtement.setString(++st_count, comp_cd);
						stmtement.setString(++st_count, "C");
						stmtement.setString(++st_count, start_dt);
						stmtement.setString(++st_count, end_dt);
						stmtement.setString(++st_count, cont_map);
						stmtement.setString(++st_count, cont_no);
						stmtement.setString(++st_count, agmt_no);
						stmtement.setString(++st_count, comp_cd);
						stmtement.setString(++st_count, counterparty_cd);
						stmtement.setString(++st_count, contract_type);
						stmtement.setString(++st_count, start_dt);
						stmtement.setString(++st_count, end_dt);
						stmtement.setString(++st_count, cargo_no);
						stmtement.setString(++st_count, comp_cd);
						stmtement.setString(++st_count, "C");
						stmtement.setString(++st_count, start_dt);
						stmtement.setString(++st_count, end_dt);
						stmtement.setString(++st_count, cont_map);
					}
					else
					{
						stmtement.setString(++st_count, cont_no);
						stmtement.setString(++st_count, agmt_no);
						stmtement.setString(++st_count, comp_cd);
						stmtement.setString(++st_count, counterparty_cd);
						stmtement.setString(++st_count, contract_type);
						stmtement.setString(++st_count, start_dt);
						stmtement.setString(++st_count, end_dt);
						stmtement.setString(++st_count, cargo_no);
					}
					resultset = stmtement.executeQuery();
					if(resultset.next())
					{
						qty=nf.format(resultset.getDouble(1));
					}
					stmtement.close();
					resultset.close();
				}
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return qty;
	}
	
	//BEST SUPPLY ALLOCATION SCM CONTRACT WISE
	public String getBestSupplyAllocationScm(Connection conn,String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String contract_type,String start_dt,String end_dt,String agmt_base, String cargo_no) throws Exception
	{
		String function_nm="getBestSupplyAllocationScm()";
		String scm="";
		try
		{
			if(conn!=null)
			{
				String cont_map=counterparty_cd+"-"+contract_type+"-"+agmt_no+"-%-"+cont_no+"-%";
				int st_count=0;
				if(agmt_base.equals("D"))
				{
					query = "SELECT SUM(ALLOC_SCM) " 
							+ "FROM "
							+ "(SELECT SUM(EXIT_QTY_SCM) ALLOC_SCM "
							+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A " 
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND SELL_CONT_MAP LIKE ? "
							+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
							+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) "
							+ "UNION " 
							+ "SELECT SUM(QTY_SCM) ALLOC_SCM "
							+ "FROM FMS_DAILY_ALLOCATION_DTL A " 
							+ "WHERE CONT_NO=? AND AGMT_NO=? "
							+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONTRACT_TYPE=? AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
							+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
							+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
							+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) " 
							+ "AND GAS_DT NOT IN (SELECT DISTINCT GAS_DT FROM FMS_DAILY_TRANSPORTER_ALLOC A "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND SELL_CONT_MAP LIKE ? ))";
				}
				else
				{
					query="SELECT SUM(QTY_SCM) "
			  				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
			  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
							+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONTRACT_TYPE=? AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
							+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
							+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
							+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				}
				stmtement = conn.prepareStatement(query);
				if(agmt_base.equals("D"))
				{
					stmtement.setString(++st_count, comp_cd);
					stmtement.setString(++st_count, "C");
					stmtement.setString(++st_count, start_dt);
					stmtement.setString(++st_count, end_dt);
					stmtement.setString(++st_count, cont_map);
					stmtement.setString(++st_count, cont_no);
					stmtement.setString(++st_count, agmt_no);
					stmtement.setString(++st_count, comp_cd);
					stmtement.setString(++st_count, counterparty_cd);
					stmtement.setString(++st_count, contract_type);
					stmtement.setString(++st_count, start_dt);
					stmtement.setString(++st_count, end_dt);
					stmtement.setString(++st_count, cargo_no);
					stmtement.setString(++st_count, comp_cd);
					stmtement.setString(++st_count, "C");
					stmtement.setString(++st_count, start_dt);
					stmtement.setString(++st_count, end_dt);
					stmtement.setString(++st_count, cont_map);
				}
				else
				{
					stmtement.setString(++st_count, cont_no);
					stmtement.setString(++st_count, agmt_no);
					stmtement.setString(++st_count, comp_cd);
					stmtement.setString(++st_count, counterparty_cd);
					stmtement.setString(++st_count, contract_type);
					stmtement.setString(++st_count, start_dt);
					stmtement.setString(++st_count, end_dt);
					stmtement.setString(++st_count, cargo_no);
				}
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					scm=nf.format(resultset.getDouble(1));
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return scm;
	}
	
	//PURCHASE ALLOCATION QTY CONTRACT WISE
	public String getPurchaseAllocationQty(Connection conn,String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String contract_type, String cargo_no) throws Exception
	{
		String function_nm="getPurchaseAllocationQty()";
		String qty="";
		try
		{
			if(conn!=null)
			{
				query="SELECT SUM(QTY_MMBTU) "
		  				+ "FROM FMS_BUY_DAILY_ALLOCATION A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONTRACT_TYPE=? AND CARGO_NO=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cont_no);
				stmtement.setString(2, agmt_no);
				stmtement.setString(3, comp_cd);
				stmtement.setString(4, counterparty_cd);
				stmtement.setString(5, contract_type);
				stmtement.setString(6, cargo_no);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					qty=nf.format(resultset.getDouble(1));
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return qty;
	}
	
	//PURCHASE ALLOCATION QTY CONTRACT AND DATE WISE
	public String getPurchaseAllocationQty(Connection conn,String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String contract_type,String cargo_no, String date) throws Exception
	{
		String function_nm="getPurchaseAllocationQty()";
		String qty="";
		try
		{
			if(conn!=null)
			{
				query="SELECT SUM(QTY_MMBTU) "
		  				+ "FROM FMS_BUY_DAILY_ALLOCATION A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONTRACT_TYPE=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cont_no);
				stmtement.setString(2, agmt_no);
				stmtement.setString(3, comp_cd);
				stmtement.setString(4, counterparty_cd);
				stmtement.setString(5, contract_type);
				stmtement.setString(6, date);
				stmtement.setString(7, cargo_no);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					qty=nf.format(resultset.getDouble(1));
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return qty;
	}

	//PURCHASE ALLOCATION QTY PLANT, BU AND DATE RANGE WISE
	public Double getPurchaseAllocationQty(Connection conn,String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String contract_type, String plant_seq, String bu_plant_seq, String cargo_no,String period_start_dt, String period_end_dt) throws Exception
	{
		String function_nm="getPurchaseAllocationQty()";
		double qty=0;
		try
		{
			if(conn!=null)
			{
				query="SELECT SUM(QTY_MMBTU) "
		  				+ "FROM FMS_BUY_DAILY_ALLOCATION A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
						+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND CARGO_NO=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.CARGO_NO=B.CARGO_NO) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cont_no);
				stmtement.setString(2, agmt_no);
				stmtement.setString(3, comp_cd);
				stmtement.setString(4, counterparty_cd);
				stmtement.setString(5, plant_seq);
				stmtement.setString(6, contract_type);
				stmtement.setString(7, bu_plant_seq);
				stmtement.setString(8, period_start_dt);
				stmtement.setString(9, period_end_dt);
				stmtement.setString(10, cargo_no);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					qty=resultset.getDouble(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return qty;
	}
	
	//PURCHASE ALLOCATION QTY BU AND DATE RANGE WISE
	public Double getPurchaseAllocationQty(Connection conn,String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String contract_type, String bu_plant_seq, String period_start_dt, String period_end_dt, String cargo_no) throws Exception
	{
		String function_nm="getPurchaseAllocationQty()";
		double qty=0;
		try
		{
			if(conn!=null)
			{
				query="SELECT SUM(QTY_MMBTU) "
		  				+ "FROM FMS_BUY_DAILY_ALLOCATION A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONTRACT_TYPE=? AND BU_SEQ=? "
						+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.CARGO_NO=B.CARGO_NO) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cont_no);
				stmtement.setString(2, agmt_no);
				stmtement.setString(3, comp_cd);
				stmtement.setString(4, counterparty_cd);
				stmtement.setString(5, contract_type);
				stmtement.setString(6, bu_plant_seq);
				stmtement.setString(7, period_start_dt);
				stmtement.setString(8, period_end_dt);
				stmtement.setString(9, cargo_no);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					qty=resultset.getDouble(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return qty;
	}
	
	//SUPPLY ALLOCATION QTY PLANT, BU AND DATE RANGE WISE
	public Double getDlngAllocationQty(Connection conn, String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String contract_type) throws Exception
	{
		String function_nm="getDlngAllocationQty()";
		double qty=0;
		try
		{
			if(conn != null)
			{
				query="SELECT SUM(QTY_MMBTU) "
		  				+ "FROM FMS_DLNG_ALLOC_MST A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONTRACT_TYPE=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND A.TRUCK_CD=B.TRUCK_CD "
						+ "AND B.FILL_STATION_CD=A.FILL_STATION_CD AND B.BAY_CD=A.BAY_CD "
						+ "AND B.SLOT_START_TIME=A.SLOT_START_TIME AND B.SLOT_END_TIME=A.SLOT_END_TIME AND B.CARGO_NO=A.CARGO_NO "
						+ ") ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cont_no);
				stmtement.setString(2, agmt_no);
				stmtement.setString(3, comp_cd);
				stmtement.setString(4, counterparty_cd);
				stmtement.setString(5, contract_type);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					qty=resultset.getDouble(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return qty;
	}
	
	//SUPPLY ALLOCATION QTY PLANT, BU AND DATE WISE
	public String getDlngAllocationQty(Connection conn,String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String contract_type, String date) throws Exception
	{
		String function_nm="getDlngAllocationQty()";
		String qty="";
		try
		{
			if(conn!=null)
			{
				query="SELECT SUM(QTY_MMBTU) "
		  				+ "FROM FMS_DLNG_ALLOC_MST A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONTRACT_TYPE=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND A.TRUCK_CD=B.TRUCK_CD "
						+ "AND B.FILL_STATION_CD=A.FILL_STATION_CD AND B.BAY_CD=A.BAY_CD "
						+ "AND B.SLOT_START_TIME=A.SLOT_START_TIME AND B.SLOT_END_TIME=A.SLOT_END_TIME AND B.CARGO_NO=A.CARGO_NO ) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cont_no);
				stmtement.setString(2, agmt_no);
				stmtement.setString(3, comp_cd);
				stmtement.setString(4, counterparty_cd);
				stmtement.setString(5, contract_type);
				stmtement.setString(6, date);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					qty=nf.format(resultset.getDouble(1));
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return qty;
	}
	
	//Purchase ALLOCATION QTY PLANT, BU AND DATE RANGE WISE
	public Double getTraderAllocationQty(Connection conn, String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String contract_type, String plant_seq, String bu_plant_seq, String period_start_dt, String period_end_dt, String cargo_no) throws Exception
	{
		String function_nm="getTraderAllocationQty()";
		double qty=0;
		try
		{
			if(conn != null)
			{
				query="SELECT SUM(QTY_MMBTU) "
						+ "FROM FMS_BUY_DAILY_ALLOCATION A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
						+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cont_no);
				stmtement.setString(2, agmt_no);
				stmtement.setString(3, comp_cd);
				stmtement.setString(4, counterparty_cd);
				stmtement.setString(5, plant_seq);
				stmtement.setString(6, contract_type);
				stmtement.setString(7, bu_plant_seq);
				stmtement.setString(8, period_start_dt);
				stmtement.setString(9, period_end_dt);
				stmtement.setString(10, cargo_no);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					qty=resultset.getDouble(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return qty;
	}
}
