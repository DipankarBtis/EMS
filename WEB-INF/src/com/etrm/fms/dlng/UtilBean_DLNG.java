package com.etrm.fms.dlng;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.SystemErrorLogger;

//Coded By          : Arth Patel
//Code Reviewed by	:  
//CR Date			: 03/01/2025 
//Status	  		: Developing

public class UtilBean_DLNG 
{
	String db_src_file_name="UtilBean_DLNG.java";
	
	PreparedStatement stmtement,stmtement1,stmtement2,stmtement3;
	ResultSet resultset,resultset1,resultset2,resultset3;
	String query = "";
	String query1="";
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	
	DateUtil dateUtil = new DateUtil();
	
	
	public String getTruckRegNo(Connection conn,String cd) throws Exception
	{
		String function_nm="getTruckRegNo()";
		String nm="";
		try
		{
			if(conn!=null)
			{
				query = "SELECT TRUCK_REG_NUM "
						+ "FROM FMS_TRUCK_MST "
						+ "WHERE TRUCK_CD=? "
						+ "ORDER BY TRUCK_CD";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cd);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					nm= resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		return nm;
	}
	
	public String getTruckTransName(Connection conn,String cd) throws Exception
	{
		String function_nm="getTruckTransName()";
		String nm="";
		try
		{
			if(conn!=null)
			{
				query = "SELECT TRUCK_TRANS_NAME "
						+ "FROM FMS_TRUCK_TRANSPORTER_MST "
						+ "WHERE TRUCK_TRANS_CD=? "
						+ "ORDER BY TRUCK_TRANS_CD";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cd);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					nm= resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		return nm;
	}
	
	public String getTruckTransABBR(Connection conn,String cd) throws Exception
	{
		String function_nm="getTruckTransABBR()";
		String abbr="";
		try
		{
			if(conn!=null)
			{
				query = "SELECT TRUCK_TRANS_ABBR "
						+ "FROM FMS_TRUCK_TRANSPORTER_MST "
						+ "WHERE TRUCK_TRANS_CD=? "
						+ "ORDER BY TRUCK_TRANS_CD";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cd);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					abbr= resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		return abbr;
	}
	
	public String getDriverName(Connection conn,String cd) throws Exception
	{
		String function_nm="getDriverName()";
		String nm="";
		try
		{
			if(conn!=null)
			{
				query = "SELECT DRIVER_NAME "
						+ "FROM FMS_TRUCK_DRIVER_MST "
						+ "WHERE DRIVER_CD=? "
						+ "ORDER BY DRIVER_CD";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cd);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					nm= resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		return nm;
	}
	
	public String getCheckPostName(Connection conn,String cd) throws Exception
	{
		String function_nm="getCheckPostName()";
		String nm="";
		try
		{
			if(conn!=null)
			{
				query = "SELECT CHKPOST_NAME "
						+ "FROM FMS_CHECKPOST_MST "
						+ "WHERE CHKPOST_CD=? "
						+ "ORDER BY CHKPOST_CD";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cd);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					nm= resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		return nm;
	}
	
	public String getFillStationName(Connection conn, String cd) throws Exception
	{
		String function_nm="getFillStationName()";
		String nm="";
		try
		{
			if(conn!=null)
			{
				query = "SELECT FILL_STATION_NAME "
						+ "FROM FMS_FILLING_STATION_MST "
						+ "WHERE FILL_STATION_CD=? "
						+ "ORDER BY FILL_STATION_CD";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cd);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					nm= resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		return nm;
	}

	public String getFillStationABBR(Connection conn, String cd) throws Exception
	{
		String function_nm="getFillStationABBR()";
		String abbr="";
		try
		{
			if(conn!=null)
			{
				query = "SELECT FILL_STATION_ABBR "
						+ "FROM FMS_FILLING_STATION_MST "
						+ "WHERE FILL_STATION_CD=? "
						+ "ORDER BY FILL_STATION_CD";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cd);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					abbr= resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		return abbr;
	}
	
	public String getTruckOccupancyData(Connection conn,String comp_cd, String from_dt, String to_dt, String truck_cd)
	{
		String function_nm = "getTruckOccupancyData()";
	    String occupiedUntilDt = "";

	    try 
	    {
	        // Priority 1: FMS_DLNG_ALLOC_MST
	        String query1 = "SELECT SLOT_START_TIME,SLOT_END_TIME,NEXT_AVAIL_HRS, "
	                + " LOAD_START_TIME, LOAD_END_TIME, TO_CHAR(LOAD_END_DT,'DD/MM/YYYY') "
	                + " FROM FMS_DLNG_ALLOC_MST E "
	                + " WHERE "
	                //+ "COMPANY_CD=? AND "
	                + "TRUCK_CD=? "
	                + " AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') "
	                + " AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST F "
	                + " WHERE F.CONT_NO=E.CONT_NO AND F.AGMT_NO=E.AGMT_NO "
	                + " AND F.COMPANY_CD=E.COMPANY_CD AND F.COUNTERPARTY_CD=E.COUNTERPARTY_CD "
	                + " AND F.PLANT_SEQ=E.PLANT_SEQ AND F.CONTRACT_TYPE=E.CONTRACT_TYPE AND F.GAS_DT=E.GAS_DT AND F.CARGO_NO=E.CARGO_NO "
	                //+ " AND F.TRUCK_TRANS_CD=E.TRUCK_TRANS_CD AND F.TRUCK_CD=E.TRUCK_CD"
	                + ")";

	        occupiedUntilDt = executeOccupancyQuery(conn,query1, comp_cd, from_dt, to_dt, truck_cd);
	        if (!occupiedUntilDt.isEmpty()) return occupiedUntilDt;

	        // Priority 2: FMS_DLNG_SELLER_NOM_DTL
	        String query2 = "SELECT SLOT_START_TIME,SLOT_END_TIME,NEXT_AVAIL_HRS, "
	                + " '', '', TO_CHAR(ARRIVAL_DT,'DD/MM/YYYY') "
	                + " FROM FMS_DLNG_SELLER_NOM_DTL C "
	                + " WHERE "
	                //+ "COMPANY_CD=? AND "
	                + "TRUCK_CD=? "
	                + " AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') "
	                + " AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_SELLER_NOM_DTL D "
	                + " WHERE C.CONT_NO=D.CONT_NO AND C.AGMT_NO=D.AGMT_NO "
	                + " AND C.COMPANY_CD=D.COMPANY_CD AND C.COUNTERPARTY_CD=D.COUNTERPARTY_CD "
	                + " AND C.PLANT_SEQ=D.PLANT_SEQ AND C.CONTRACT_TYPE=D.CONTRACT_TYPE AND C.GAS_DT=D.GAS_DT AND C.CARGO_NO=D.CARGO_NO "
	                //+ " AND C.TRUCK_TRANS_CD=D.TRUCK_TRANS_CD AND C.TRUCK_CD=D.TRUCK_CD"
	                + ")";

	        occupiedUntilDt = executeOccupancyQuery(conn, query2, comp_cd, from_dt, to_dt, truck_cd);
	        if (!occupiedUntilDt.isEmpty()) return occupiedUntilDt;

	        // Priority 3: FMS_DLNG_BUYER_NOM_DTL
	        String query3 = "SELECT SLOT_START_TIME,SLOT_END_TIME,NEXT_AVAIL_HRS, "
	                + " '', '', TO_CHAR(ARRIVAL_DT,'DD/MM/YYYY') "
	                + " FROM FMS_DLNG_BUYER_NOM_DTL A "
	                + " WHERE "
	                //+ "COMPANY_CD=? AND "
	                + "TRUCK_CD=? "
	                + " AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') "
	                + " AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_BUYER_NOM_DTL B "
	                + " WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
	                + " AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
	                + " AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO "
	                //+ " AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD"
	                + ")";

	        occupiedUntilDt = executeOccupancyQuery(conn, query3, comp_cd, from_dt, to_dt, truck_cd);
	    } 
	    catch (Exception e)
	    {
	        new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
	    }

	    return occupiedUntilDt;
	}

	private String executeOccupancyQuery(Connection conn, String query, String comp_cd, String from_dt, String to_dt, String truck_cd) throws SQLException
	{
	    String result = "";
	    LocalDateTime maxDateTime = null;

	    stmtement = conn.prepareStatement(query);
	    //stmtement.setString(1, comp_cd);
	    stmtement.setString(1, truck_cd);
	    stmtement.setString(2, to_dt);
	    stmtement.setString(3, from_dt);

	    resultset = stmtement.executeQuery();

	    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	    while (resultset.next())
	    {
	        String slot_start_time = resultset.getString(1) == null ? "" : resultset.getString(1);
	        String slot_end_time = resultset.getString(2) == null ? "" : resultset.getString(2);
	        String next_avail_hrs = resultset.getString(3) == null ? "0" : resultset.getString(3);

	        String temp_occupied_start_time = resultset.getString(4) == null ? slot_start_time : resultset.getString(4);
	        String temp_occupied_end_time = resultset.getString(5) == null ? slot_end_time : resultset.getString(5);
	        String temp_occupied_end_dt = resultset.getString(6) == null ? "" : resultset.getString(6);

	        if (temp_occupied_end_time.isEmpty() || temp_occupied_end_dt.isEmpty()) continue;//This condition is added So, if either value is missing, the code doesn't try to parse or use them�it simply moves on to the next row

	        try 
	        {
	            LocalTime endTime = LocalTime.parse(temp_occupied_end_time, timeFormatter);
	            LocalDate endDate = LocalDate.parse(temp_occupied_end_dt, dateFormatter);
	            LocalDateTime dateTime = LocalDateTime.of(endDate, endTime);

	            LocalDateTime resultDateTime = dateTime.plusHours(Integer.parseInt(next_avail_hrs));

	            if (maxDateTime == null || resultDateTime.isAfter(maxDateTime)) {
	                maxDateTime = resultDateTime;
	            }

	        } 
	        catch (Exception e)
	        {
	        	new SystemErrorLogger().InsertErrorLogger(db_src_file_name, "executeOccupancyQuery()", e);
	        }
	    }
	    resultset.close();
	    stmtement.close();

	    if (maxDateTime != null)
	    {
	        result = maxDateTime.toLocalDate().format(dateFormatter) + " " + maxDateTime.toLocalTime().format(timeFormatter);
	    }
	    return result;
	}
}
