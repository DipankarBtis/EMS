package com.etrm.fms.dlng;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.TaxCalculator;
import com.etrm.fms.util.UtilBean;
import com.google.gson.Gson;

@WebServlet("/servlet/DB_Dlng_ContractMgmt_Ajax")
public class DB_Dlng_ContractMgmt_Ajax extends HttpServlet
{
	static String db_src_file_name="DB_Dlng_ContractMgmt_Ajax.java";
	static Connection dbcon;
	static PreparedStatement stmt,stmt1,stmt2,stmt3,stmt4,stmt5;
	static ResultSet rset,rset1,rset2,rset3,rset4,rset5;
	//public static String queryString, queryString1, queryString2;
	public static String setCallType="";
	public static String json = "";
	
	static JSONObject multipleData = new JSONObject();
	static JSONObject allDetail = new JSONObject();
	static JSONArray AllJsonArray = new JSONArray();
    
	static UtilBean_DLNG utilBean_DLNG = new UtilBean_DLNG();
	static DateUtil dateUtil = new DateUtil();
	static UtilBean utilBean = new UtilBean();
	static TaxCalculator TaxCalc = new TaxCalculator(); 
	
	static NumberFormat nf = new DecimalFormat("###########0.00");
	static NumberFormat nf2 = new DecimalFormat("###########0.0000");
	
    @SuppressWarnings("unused")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
    	synchronized (this) 
    	{
    		String function_nm="doPost()";
        	
    		response.setContentType("application/json");
    		response.setCharacterEncoding("UTF-8");
    		
    		try 
    		{
    			Context initContext = new InitialContext();
    			if(initContext == null ) 
    			{
    				throw new Exception("Boom - No Context");
    			}
    			Context envContext  = (Context)initContext.lookup("java:/comp/env");
    			DataSource ds = (DataSource)envContext.lookup(RuntimeConf.security_database);
    			Properties prop = System.getProperties();
    			
    			if (ds != null) 
    			{
    				dbcon = ds.getConnection();       
    				if(dbcon != null)  
    				{
    					setCallType = request.getParameter("setCallType");
    					
    					if(setCallType.equalsIgnoreCase("fetchTruckDeatils"))
    					{
    						AllJsonArray = new JSONArray();
    						allDetail = new JSONObject();
    						fetchTruckDeatils(request,response);
    						json = new Gson().toJson(AllJsonArray);
    					}
    					else if(setCallType.equalsIgnoreCase("fetchFillStDeatils"))
    					{
    						AllJsonArray = new JSONArray();
    						allDetail = new JSONObject();
    						fetchFillStDeatils(request,response);
    						json = new Gson().toJson(AllJsonArray);
    					}
    					else if(setCallType.equalsIgnoreCase("fetchBayDeatils"))
    					{
    						AllJsonArray = new JSONArray();
    						allDetail = new JSONObject();
    						fetchBayDeatils(request,response);
    						json = new Gson().toJson(AllJsonArray);
    					}
    					else if(setCallType.equalsIgnoreCase("fetchSlotDeatils"))
    					{
    						AllJsonArray = new JSONArray();
    						allDetail = new JSONObject();
    						fetchSlotDeatils(request,response);
    						json = new Gson().toJson(AllJsonArray);
    					}
    					else if(setCallType.equalsIgnoreCase("checkAllowableCredit"))
    					{
    						AllJsonArray = new JSONArray();
    						allDetail = new JSONObject();
    						checkAllowableCredit(request,response);
    						json = new Gson().toJson(AllJsonArray);
    					}
    					else if(setCallType.equalsIgnoreCase("resetSessionData"))
    					{
    						AllJsonArray = new JSONArray();
    						allDetail = new JSONObject();
    						json = new Gson().toJson(AllJsonArray);
    						
    						HttpSession session = request.getSession();
    						session.removeAttribute("MSG_TRUCK_REG_NO");
    						session.removeAttribute("MSG_QTY");
    						session.removeAttribute("MSG_ALLOW_CREDIT");
    						session.removeAttribute("MSG_CONSUMED_AMT");
    						session.removeAttribute("MSG_BALANCE");
    						session.removeAttribute("MSG_SUBMITTED");
    						session.removeAttribute("MSG_RPT_DT");
    					}
    				}
    			}
    		}
    		catch(Exception e) 
    		{
    			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
    	    }
    		finally 
    		{
    			if(rset != null){try {rset.close();}catch(SQLException e){System.out.println("rset is not close " + e);}}
    			if(rset1 != null){try {rset1.close();}catch(SQLException e){System.out.println("rset1 is not close " + e);}}
    			if(rset2 != null){try {rset2.close();}catch(SQLException e){System.out.println("rset2 is not close " + e);}}
    			if(rset3 != null){try {rset3.close();}catch(SQLException e){System.out.println("rset3 is not close " + e);}}
    			if(rset4 != null){try {rset4.close();}catch(SQLException e){System.out.println("rset4 is not close " + e);}}
    			if(rset5 != null){try {rset5.close();}catch(SQLException e){System.out.println("rset5 is not close " + e);}}
    			if(stmt != null){try {stmt.close();}catch(SQLException e){System.out.println("stmt is not close " + e);}}
    			if(stmt1 != null){try {stmt1.close();}catch(SQLException e){System.out.println("stmt1 is not close " + e);}}
    			if(stmt2 != null){try {stmt2.close();}catch(SQLException e){System.out.println("stmt2 is not close " + e);}}
    			if(stmt3 != null){try {stmt3.close();}catch(SQLException e){System.out.println("stmt3 is not close " + e);}}
    			if(stmt4 != null){try {stmt4.close();}catch(SQLException e){System.out.println("stmt4 is not close " + e);}}
    			if(stmt5 != null){try {stmt5.close();}catch(SQLException e){System.out.println("stmt5 is not close " + e);}}
    			if(dbcon != null){try {dbcon.close();}catch(SQLException e){System.out.println("dbcon is not close " + e);}}
    		}
    		
    		try {
    		response.getWriter().write(json);
    		} catch(IOException e) {
    			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
    		}
		}
	}
    
    private void fetchFillStDeatils(HttpServletRequest request,HttpServletResponse response) throws SQLException
    {
    	String function_nm="fetchFillStDeatils()";
    	try
    	{
    		allDetail.clear();
    		AllJsonArray.clear();
    		
    		HttpSession session = request.getSession();
    		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
    		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
    		
    		String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
    		String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
    		String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
    		String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
    		String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
    		String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
    		
    		JSONObject jsonobj = new JSONObject();
    		JSONArray stDtl = new JSONArray();
    		
    		String queryString="SELECT FILL_STATION_CD "
    				+ "FROM FMS_SUPPLY_CONT_FILLING_STN "
    				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
    				+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
    				+ "AND CONTRACT_TYPE=?";
    		stmt = dbcon.prepareStatement(queryString);
    		stmt.setString(1, comp_cd);
    		stmt.setString(2, counterparty_cd);
    		stmt.setString(3, cont_no);
    		stmt.setString(4, cont_rev_no);
    		stmt.setString(5, agmt_no);
    		stmt.setString(6, agmt_rev_no);
    		stmt.setString(7, contract_type);
    		rset=stmt.executeQuery();
    		while(rset.next())
    		{
    			String fillCd = rset.getString(1)==null?"":rset.getString(1);
    			
    			String queryString2="SELECT FILL_STATION_NAME,FILL_STATION_ABBR,FILL_STATION_CD "
						+ "FROM FMS_FILLING_STATION_MST A "
						+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_FILLING_STATION_MST B "
						+ "WHERE A.FILL_STATION_CD=B.FILL_STATION_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) "
						+ "AND ACTIVE_FLAG=? AND FILL_STATION_CD=? ";
				stmt2 = dbcon.prepareStatement(queryString2);
				stmt2.setString(1, "Y");
				stmt2.setString(2, fillCd);
				rset2=stmt2.executeQuery();
				while(rset2.next())
				{
					String stName = rset2.getString(1)==null?"":rset2.getString(1);
					String stAbbr = rset2.getString(2)==null?"":rset2.getString(2);
					String stCd = rset2.getString(3)==null?"":rset2.getString(3);
					
					jsonobj = new JSONObject();
					
					jsonobj.put("STNAME",stName);
					jsonobj.put("STABBR",stAbbr);
					jsonobj.put("STCD",stCd);
				}
				rset2.close();
				stmt2.close();
    			
				stDtl.add(jsonobj);
    		}
    		rset.close();
    		stmt.close();
    		
    		allDetail.put("FILLST_DTL", stDtl);
    		
    		AllJsonArray.add(allDetail);
    	}
    	catch(Exception e)
    	{
    		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
    	}
    }
    
    private void fetchBayDeatils(HttpServletRequest request,HttpServletResponse response) throws SQLException
    {
    	String function_nm="fetchBayDeatils()";
    	try
    	{
    		allDetail.clear();
    		AllJsonArray.clear();
    		
    		HttpSession session = request.getSession();
    		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
    		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
    		
    		String fillStCd=request.getParameter("fillStCd")==null?"":request.getParameter("fillStCd");
    		
    		JSONObject jsonobj = new JSONObject();
    		JSONArray bayDtl = new JSONArray();
    		
    		String queryString2="SELECT BAY_CD,BAY_NAME,SLOT_START_TIME,SLOT_INTERVAL "
					+ "FROM FMS_BAY_SLOT_MST A "
					+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_BAY_SLOT_MST B "
					+ "WHERE A.FILL_STATION_CD=B.FILL_STATION_CD "
					+ "AND A.BAY_CD=B.BAY_CD "
					+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) "
					+ "AND ACTIVE_FLAG=? AND FILL_STATION_CD=? ";
			stmt2 = dbcon.prepareStatement(queryString2);
			stmt2.setString(1, "Y");
			stmt2.setString(2, fillStCd);
			//stmt2.setString(3, comp_cd);
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				JSONObject bayObj = new JSONObject(); 
				
				String bayCd = rset2.getString(1)==null?"":rset2.getString(1);
				String bayName = rset2.getString(2)==null?"":rset2.getString(2);
				String baySlotStTime = rset2.getString(3)==null?"":rset2.getString(3);
				String baySlotIntrvl = rset2.getString(4)==null?"":rset2.getString(4);
				
				bayObj.put("BAYCD",bayCd);
				bayObj.put("BAYNAME",bayName);
				
				bayDtl.add(bayObj);
			}
			rset2.close();
			stmt2.close();
    		
    		allDetail.put("BAY_DTL", bayDtl);
    		
    		AllJsonArray.add(allDetail);
    	}
    	catch(Exception e)
    	{
    		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
    	}
    }
    
    
    private void fetchSlotDeatils(HttpServletRequest request,HttpServletResponse response) throws SQLException
    {
    	String function_nm="fetchSlotDeatils()";
    	try
    	{
    		allDetail.clear();
    		AllJsonArray.clear();
    		
    		HttpSession session = request.getSession();
    		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
    		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
    		
    		String bayCd=request.getParameter("bayCd")==null?"":request.getParameter("bayCd");
    		String gasDt=request.getParameter("gasDt")==null?"":request.getParameter("gasDt");
    		String fillSt=request.getParameter("fillSt")==null?"":request.getParameter("fillSt");
    		
    		JSONObject jsonobj = new JSONObject();
    		JSONArray slotDtl = new JSONArray();
    		
    		String queryString2="SELECT BAY_CD,BAY_NAME,SLOT_START_TIME,SLOT_INTERVAL "
    				+ "FROM FMS_BAY_SLOT_MST A "
    				+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_BAY_SLOT_MST B "
    				+ "WHERE A.FILL_STATION_CD=B.FILL_STATION_CD "
    				+ "AND A.BAY_CD=B.BAY_CD "
    				+ "AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) "
    				+ "AND ACTIVE_FLAG=? AND BAY_CD=?";
    		stmt2 = dbcon.prepareStatement(queryString2);
    		stmt2.setString(1, gasDt);
    		stmt2.setString(2, "Y");
    		stmt2.setString(3, bayCd);
    		rset2=stmt2.executeQuery();
    		while(rset2.next())
    		{
				String baySlotStTime = rset2.getString(3)==null?"":rset2.getString(3);
				String baySlotIntrvl = rset2.getString(4)==null?"":rset2.getString(4);
    			
    			List<TimeSlot> slots = generateTimeSlots(baySlotStTime, baySlotIntrvl, gasDt);
    	        for (TimeSlot slot : slots) 
    	        {
    	        	JSONObject slotObj = new JSONObject(); 
    	        	
    	            String slotValue = slot.toString().split("@")[0];
    	            String slotText = slot.toString().split("@")[1];
    	            
    	            String slotStTime = slotValue.split("-")[0];
    	            String slotEndTime = slotValue.split("-")[1];
    	            
    	            String slotAvailable = "Y";
    	            
    	            String queryString3="SELECT SLOT_START_TIME "
    	    				+ " FROM FMS_DLNG_BUYER_NOM_DTL A "
    	    				+ " WHERE COMPANY_CD=? AND BAY_CD=? AND SLOT_START_TIME=? AND SLOT_END_TIME=? AND FILL_STATION_CD=? "
    	    				+ " AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_BUYER_NOM_DTL B "
    						+ " WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
    						+ "	AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
    						+ "	AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO "
    						+ ") "
    						+ " AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
    						+ " AND NOT EXISTS ( "
    						+ "   SELECT 1 "
    						+ "   FROM FMS_DLNG_SELLER_NOM_DTL C "
    						+ "   WHERE C.COMPANY_CD=A.COMPANY_CD AND C.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
    						+ "   AND C.PLANT_SEQ=A.PLANT_SEQ AND C.CONTRACT_TYPE=A.CONTRACT_TYPE "
    						+ "   AND C.GAS_DT=A.GAS_DT AND C.CONT_NO=A.CONT_NO AND C.AGMT_NO=A.AGMT_NO AND C.CARGO_NO=A.CARGO_NO) "
    						+ " UNION ALL "
    						+ " SELECT SLOT_START_TIME "
    						+ " FROM FMS_DLNG_SELLER_NOM_DTL A "
    	    				+ " WHERE COMPANY_CD=? AND BAY_CD=? AND SLOT_START_TIME=? AND SLOT_END_TIME=? AND FILL_STATION_CD=? "
    	    				+ " AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_SELLER_NOM_DTL B "
    						+ " WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
    						+ "	AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
    						+ "	AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO "
    						+ ") "
    						+ " AND GAS_DT=TO_DATE(?,'DD/MM/YYYY')";
    	    		stmt3 = dbcon.prepareStatement(queryString3);
    	    		stmt3.setString(1, comp_cd);
    	    		stmt3.setString(2, bayCd);
    	    		stmt3.setString(3, slotStTime);
    	    		stmt3.setString(4, slotEndTime);
    	    		stmt3.setString(5, fillSt);
    	    		stmt3.setString(6, gasDt);
    	    		stmt3.setString(7, comp_cd);
    	    		stmt3.setString(8, bayCd);
    	    		stmt3.setString(9, slotStTime);
    	    		stmt3.setString(10, slotEndTime);
    	    		stmt3.setString(11, fillSt);
    	    		stmt3.setString(12, gasDt);
    	    		rset3=stmt3.executeQuery();
    	    		if(rset3.next())
    	    		{
    	    			slotAvailable = "N";
    	    		}
    	    		rset3.close();
    	    		stmt3.close();
    	    		
    	            slotObj.put("BAYSLOTVALUE",slotValue);
        			slotObj.put("BAYSLOTTEXT",slotText);
        			slotObj.put("SLOTAVAILABLE",slotAvailable);
        			
        			slotDtl.add(slotObj);
    	        }
    	        
    		}
    		rset2.close();
    		stmt2.close();
    		
    		allDetail.put("BAY_DTL", slotDtl);
    		
    		AllJsonArray.add(allDetail);
    		
    	}
    	catch(Exception e)
    	{
    		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
    	}
    }
    
    public static List<TimeSlot> generateTimeSlots(String baySlotStTime, String baySlotIntrvl, String gasDateStr) {
        List<TimeSlot> timeSlots = new ArrayList<>();

        // Parse start time and interval (format: HH:mm)
        String[] startParts = baySlotStTime.split(":");
        int startMinutes = Integer.parseInt(startParts[0]) * 60 + Integer.parseInt(startParts[1]);

        String[] intervalParts = baySlotIntrvl.split(":");
        int intervalMinutes = Integer.parseInt(intervalParts[0]) * 60 + Integer.parseInt(intervalParts[1]);

        int endLimit = startMinutes + 1440;

        // Parse gas date (format: DD/MM/YYYY)
        DateTimeFormatter inputDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate baseGasDate = LocalDate.parse(gasDateStr, inputDateFormat);

        for (int i = startMinutes; i < endLimit; i += intervalMinutes) {
            int slotStart = i;
            int slotEnd = i + intervalMinutes;

            int slotDayOffset = slotStart / 1440;
            LocalDate slotDate = baseGasDate.plusDays(slotDayOffset);
            String slotDateStr = slotDate.format(inputDateFormat);

            String slotStartStr = formatTime(slotStart);
            String slotEndStr = formatTime(Math.min(slotEnd, endLimit));

            String value = slotStartStr + "-" + slotEndStr;
            String text = slotStartStr + " - " + slotEndStr + " (" + slotDateStr + ")";

            timeSlots.add(new TimeSlot(value, text));

            if (slotEnd >= endLimit) {
                break;
            }
        }

        return timeSlots;
    }
    
    public static class TimeSlot {
        String value;
        String text;

        public TimeSlot(String value, String text) {
            this.value = value;
            this.text = text;
        }

        @Override
        public String toString() {
            return value + "@" + text;
        }
    }

    private static String formatTime(int minutes) {
        minutes = minutes % 1440;
        int hours = minutes / 60;
        int mins = minutes % 60;
        return String.format("%02d:%02d", hours, mins);
    }
    
    private void fetchTruckDeatilsByCd(HttpServletRequest request,HttpServletResponse response) throws SQLException
	{
    	String function_nm="fetchTruckDeatilsByCd()";
		try
		{
			allDetail.clear();
			AllJsonArray.clear();
			
			HttpSession session = request.getSession();
			String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
			
			String truck_cd=request.getParameter("truck_cd")==null?"":request.getParameter("truck_cd");
			String gas_dt=request.getParameter("gas_dt")==null?"":request.getParameter("gas_dt");

			JSONObject jsonobj = new JSONObject();
			JSONArray truckDtl = new JSONArray();
			
			String queryString2="SELECT TRUCK_REG_NUM,TRUCK_VOL_M3,TRUCK_VOL_MT,TRUCK_LOAD_CAP "
					+ "FROM FMS_TRUCK_MST A "
					+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_TRUCK_MST B WHERE A.TRUCK_CD=B.TRUCK_CD "
					+ "AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) AND ACTIVE_FLAG=? AND TRUCK_CD=? ";
			stmt2 = dbcon.prepareStatement(queryString2);
			stmt2.setString(1, gas_dt);
			stmt2.setString(2, "Y");
			stmt2.setString(3, truck_cd);
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				String truck_reg_num = rset2.getString(1)==null?"":rset2.getString(1);
				
				Float truck_vol_m3 = rset2.getFloat(2);
				Float truck_vol_mt = rset2.getFloat(3);
				
				Double truck_load_cap = rset2.getDouble(4);
				
				jsonobj.put("TRUCK_REG_NUM",truck_reg_num);
				
				jsonobj.put("TRUCK_VOL_M3",truck_vol_m3);
				jsonobj.put("TRUCK_VOL_MT",truck_vol_mt);
				jsonobj.put("TRUCK_LOAD_CAP",truck_load_cap);
			}
			rset2.close();
			stmt2.close();
			
			truckDtl.add(jsonobj);

			allDetail.put("TRUCK_DTL", truckDtl);
			
			AllJsonArray.add(allDetail);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    private void fetchTruckDeatils(HttpServletRequest request,HttpServletResponse response) throws SQLException
	{
    	String function_nm="fetchTruckDeatils()";
		try
		{
			allDetail.clear();
			AllJsonArray.clear();
			
			HttpSession session = request.getSession();
			String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
			
			String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String truck_cd=request.getParameter("truck_cd")==null?"":request.getParameter("truck_cd");
			String gas_date=request.getParameter("gas_date")==null?"":request.getParameter("gas_date");
			
			JSONObject jsonobj = new JSONObject();
			JSONArray truckDtl = new JSONArray();
			
			String queryString2="SELECT TRUCK_REG_NUM,TRUCK_VOL_M3,TRUCK_VOL_MT,TRUCK_LOAD_CAP "
					+ "FROM FMS_TRUCK_MST A "
					+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_TRUCK_MST B WHERE A.TRUCK_CD=B.TRUCK_CD "
					+ "AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) AND ACTIVE_FLAG=? AND TRUCK_CD=? ";
			stmt2 = dbcon.prepareStatement(queryString2);
			stmt2.setString(1, gas_date);
			stmt2.setString(2, "Y");
			stmt2.setString(3, truck_cd);
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{						
				String truck_reg_num = rset2.getString(1)==null?"":rset2.getString(1);
				
				Float truck_vol_m3 = rset2.getFloat(2);
				Float truck_vol_mt = rset2.getFloat(3);
				
				Double truck_load_cap = rset2.getDouble(4);
				
				jsonobj = new JSONObject();
				
				jsonobj.put("TRUCK_REG_NUM",truck_reg_num);
				
				jsonobj.put("TRUCK_VOL_M3",truck_vol_m3);
				jsonobj.put("TRUCK_VOL_MT",truck_vol_mt);
				jsonobj.put("TRUCK_LOAD_CAP",truck_load_cap);
				
				String transCd = "";
				String queryString1="SELECT TRUCK_TRANS_CD "
						+ "FROM FMS_TRUCK_TRANSPORTER_LINK A "
						+ "WHERE LINK_SEQ=(SELECT MAX(B.LINK_SEQ) FROM FMS_TRUCK_TRANSPORTER_LINK B WHERE A.TRUCK_CD=B.TRUCK_CD "
						+ "AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY') AND "
						+ "(B.RELEASE_DT>TO_DATE(?,'DD/MM/YYYY') OR RELEASE_DT IS NULL)) AND TRUCK_CD=? ";
				
				stmt1 = dbcon.prepareStatement(queryString1);
				stmt1.setString(1, gas_date);
				stmt1.setString(2, gas_date);
				stmt1.setString(3, truck_cd);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					transCd = rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
				
				jsonobj.put("TRUCK_TRANS_CD",transCd);
				
				String prevGasDate = dateUtil.getDate(gas_date, "-1");
				
				String availAfter =utilBean_DLNG.getTruckOccupancyData(dbcon, comp_cd, prevGasDate, gas_date, truck_cd);

				jsonobj.put("TRUCK_AVAILAFTER",availAfter);
			}
			rset2.close();
			stmt2.close();

			truckDtl.add(jsonobj);	
			
			allDetail.put("TRUCK_DTL", truckDtl);
			
			AllJsonArray.add(allDetail);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    private void checkAllowableCredit(HttpServletRequest request,HttpServletResponse response) throws SQLException
    {
    	String function_nm="checkAllowableCredit()";
    	try
    	{
    		allDetail.clear();
    		AllJsonArray.clear();
    		
    		HttpSession session = request.getSession();
    		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
    		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
    		
    		String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String report_dt = request.getParameter("report_dt")==null?"":request.getParameter("report_dt");
			String plant_seq = request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
			String bu_unit = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
			String buyer_qty = request.getParameter("buyer_qty")==null?"0":request.getParameter("buyer_qty");
    		
    		JSONObject jsonobj = new JSONObject();
    		JSONArray creditDtl = new JSONArray();
    		
    		String price="";
			String price_unit="";
			double credit=0;
			double exchng_rate=0;
			String cont_price="";
			double buyerNom=0;
			if(!buyer_qty.equals(""))
			{
				buyerNom=Double.parseDouble(buyer_qty);
			}
			
    		int OA_COUNT=0;
			String query2 ="SELECT COUNT(*) "
					+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.GX=? "
					+ "AND B.AGMT_NO=? AND B.CONT_NO=? AND B.CONTRACT_TYPE=? "
					+ "AND SEC_TYPE IN ('OA') "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.SEQ_NO=B.SEQ_NO "
					+ "AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX";
			stmt2=dbcon.prepareStatement(query2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, counterparty_cd);
			stmt2.setString(3, "K");
			stmt2.setString(4, agmt_no);
			stmt2.setString(5, cont_no);
			stmt2.setString(6, contract_type);
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				OA_COUNT=rset2.getInt(1);
			}
			rset2.close();
			stmt2.close();
			
			String msg="";
			String msg_type="";
			if(!contract_type.equals("W") && !contract_type.equals("O") && !contract_type.equals("Q") && OA_COUNT==0)
			{
				String queryString="SELECT TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),DCQ,RATE,RATE_UNIT,"
						+ "CONT_REF_NO,TRADE_REF_NO "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "; 
				stmt=dbcon.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_no);
				stmt.setString(4, cont_no);
				stmt.setString(5, contract_type);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					//contract_start_dt=rset.getString(1)==null?"":rset.getString(1);
					//contract_end_dt=rset.getString(2)==null?"":rset.getString(2);
					//dcq=rset.getString(3)==null?"":nf.format(rset.getDouble(3));
					price_unit=rset.getString(5)==null?"":rset.getString(5);
					price=rset.getString(4)==null?"":utilBean.RateNumberFormat(rset.getDouble(4), price_unit);
				}
				rset.close();
				stmt.close();
				
				credit=utilBean.getAllowableCreditAmount(dbcon, comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, "K", report_dt,plant_seq,bu_unit);
				exchng_rate=utilBean.getContExchangeRate(dbcon,comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, report_dt);
				String queryString1 = "SELECT DISTINCT NEW_SALE_PRICE "
						+ "FROM FMS_SUPPLY_ALLOC_REVISED A "
						+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND CONT_NO=? "
						+ "AND COUNTERPARTY_CD=? AND FLAG=? AND CONTRACT_TYPE=? "
						+ "AND MODIFICATION_SEQ_NO = (SELECT MAX(MODIFICATION_SEQ_NO) FROM FMS_SUPPLY_ALLOC_REVISED B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.FLAG=B.FLAG AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ "AND B.NEW_PRICE_EFF_DT <=TO_DATE(?,'DD/MM/YYYY'))";
				stmt1=dbcon.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, agmt_no);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, counterparty_cd);
				stmt1.setString(5, "A");
				stmt1.setString(6, contract_type);
				stmt1.setString(7, report_dt);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					cont_price=rset1.getString(1)==null?"":utilBean.RateNumberFormat(rset1.getDouble(1), price_unit);
				}
				rset1.close();
				stmt1.close();
				
				if(cont_price.equals(""))
				{
					cont_price=price;
				}
				
				double temp_gross_amt=buyerNom * Double.parseDouble(price);
				double temp_price = Double.parseDouble(price);
				if(price_unit.equals("2"))
				{
					temp_gross_amt=temp_gross_amt * exchng_rate;
					temp_price=temp_price * exchng_rate;
				}
				double temp_total_gross_amt=temp_gross_amt;
				Vector temp = new Vector();
				temp=TaxCalc.TaxAmountCalculationWithInfo(dbcon, comp_cd, counterparty_cd, "C", plant_seq, bu_unit, report_dt, nf.format(temp_total_gross_amt));
				double temp_tax_amt = Double.parseDouble(nf.format(Double.parseDouble(""+temp.elementAt(0))));
				double tax_factor=Double.parseDouble(""+temp.elementAt(5));
				double net_amt = temp_total_gross_amt+temp_tax_amt;
				
				double qty=0;
				
				if(credit <= 0)
				{
					qty=0;
					msg_type="E";
				}
				else
				{
					qty = (credit * (1 - (tax_factor/100))) / (temp_price);
					if(qty > buyerNom)
					{
						qty = buyerNom;
					}
					msg_type="F";
				}
				
				msg="Allowable Credit "+nf.format(credit)+", Allowable Qty "+nf.format(qty)+" MMBTU";
			}
			else
			{
				if(OA_COUNT > 0)
				{
					if(!contract_type.equals("O") && !contract_type.equals("Q")) 
					{
						msg="Open Account credit, Allowable Qty "+nf.format(buyerNom)+" MMBTU";
					}
				}
				
				msg_type="F";
			}
    		
			jsonobj.put("MSG", msg);
			jsonobj.put("MSG_TYPE", msg_type);
			
			creditDtl.add(jsonobj);
    		
			allDetail.put("MSG_DTL", creditDtl);
    		AllJsonArray.add(allDetail);
    		
    	}
    	catch(Exception e)
    	{
    		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
    	}
    }
}
