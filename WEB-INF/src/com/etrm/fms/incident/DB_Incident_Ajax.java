package com.etrm.fms.incident;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

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
import com.etrm.fms.util.UtilBean;
import com.google.gson.Gson;

@WebServlet("/servlet/DB_Incident_Ajax")
public class DB_Incident_Ajax extends HttpServlet
{
	public static String db_src_file_name ="DB_Incident_Ajax.java";
	
	public static Connection dbcon;
	public static PreparedStatement stmt,stmt1,stmt2,stmt3,stmt4,stmt5,stmt6,stmt7,stmt8,stmt9,stmt10,stmt11;
	public static ResultSet rset,rset1,rset2,rset3,rset4,rset5;
	public static String queryString, queryString1, queryString2;
	public static String setCallType="";
	public static String json = "";
	
	static JSONObject multipleData = new JSONObject();
	static JSONObject allDetail = new JSONObject();
	static JSONArray AllJsonArray = new JSONArray();
    
	static DateUtil dateUtil = new DateUtil();
	static UtilBean utilBean = new UtilBean();
    
    @SuppressWarnings("unused")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
    	synchronized(this)
    	{
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			String function_nm="doPost()";
			
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
						
						if(setCallType.equalsIgnoreCase("INCIDENT_DTL"))
						{
							AllJsonArray = new JSONArray();
							allDetail = new JSONObject();
							fetchIncidentDetail(request,response);
							json = new Gson().toJson(AllJsonArray);
						}
						else if(setCallType.equalsIgnoreCase("TARGET_DT"))
						{
							AllJsonArray = new JSONArray();
							allDetail = new JSONObject();
							fetchTargetDate(request,response);
							json = new Gson().toJson(AllJsonArray);
						}
						else if(setCallType.equalsIgnoreCase("EVENT_DTL"))
						{
							AllJsonArray = new JSONArray();
							allDetail = new JSONObject();
							fetchEventDetail(request,response);
							json = new Gson().toJson(AllJsonArray);
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
				if(dbcon != null){try {dbcon.close();}catch(SQLException e){System.out.println("conn is not close " + e);}}
				if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt6 != null){try{stmt6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt7 != null){try{stmt7.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt8 != null){try{stmt8.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt9 != null){try{stmt9.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt10 != null){try{stmt10.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt11 != null){try{stmt11.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			}
			try {
			response.getWriter().write(json);
			}catch(IOException e) {
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			}
    	}
	}
    
    private void fetchIncidentDetail(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="fetchIncidentDetail()";
   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray incidentDtl = new JSONArray();
			
			String sysdt=""+dateUtil.getSysdate();
			String year = sysdt.substring(8,sysdt.length());
			String temp_incident_cd="";
			
			if(!year.equals("") && !comp_cd.equals(""))
			{
				temp_incident_cd=year+""+comp_cd;
			}
			
			if(!temp_incident_cd.equals(""))
			{
				int inc_id=Integer.parseInt(temp_incident_cd)*10000;
				queryString="SELECT MAX(INCIDENT_ID) "
						+ "FROM FMS_INCIDENT_MST "
						+ "WHERE INCIDENT_ID LIKE ?";//COMPANY_CD=? AND 
				stmt = dbcon.prepareStatement(queryString);
				//stmt.setString(1, comp_cd);
				stmt.setString(1, temp_incident_cd+"%");
				rset=stmt.executeQuery();
				if(rset.next())
				{
					if(rset.getInt(1) > 0)
					{
						jsonobj.put("INCIDENT_ID",(rset.getInt(1)+1));
					}
					else
					{
						jsonobj.put("INCIDENT_ID",(inc_id+1));
					}
				}
				else
				{
					jsonobj.put("INCIDENT_ID",(inc_id+1));
				}
				rset.close();
				stmt.close();
			}
			else
			{
				jsonobj.put("INCIDENT_ID","");
			}
			
			incidentDtl.add(jsonobj);
			allDetail.put("INCIDENT_DTL", incidentDtl);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    private void fetchTargetDate(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="fetchTargetDate()";
   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			String priority=request.getParameter("priority")==null?"Normal":request.getParameter("priority");
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray incidentDtl = new JSONArray();
			
			jsonobj.put("TARGET_DT",""+countTargetDt(priority));
			
			incidentDtl.add(jsonobj);
			allDetail.put("INCIDENT_DTL", incidentDtl);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    private void fetchEventDetail(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="fetchEventDetail()";
   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			String incident_id=request.getParameter("incident_id")==null?"":request.getParameter("incident_id");
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray eventDtl = new JSONArray();
			JSONArray EventArray = new JSONArray();
			
			queryString="SELECT INCIDENT_DTL,ENT_BY,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS') "
					+ "FROM FMS_INCIDENT_DTL "
					+ "WHERE INCIDENT_ID=? "//COMPANY_CD=? AND 
					+ "ORDER BY SEQ_NO DESC";
			stmt = dbcon.prepareStatement(queryString);
			//stmt.setString(1, comp_cd);
			stmt.setString(1, incident_id);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				jsonobj = new JSONObject();
				
				String event=rset.getString(1)==null?"":rset.getString(1);
				String emp_nm=""+utilBean.getEmpName(dbcon,rset.getString(2)==null?"":rset.getString(2));
				String date=rset.getString(3)==null?"":rset.getString(3);
				
				jsonobj.put("EventBy", emp_nm);
				jsonobj.put("EventDate", date);
				jsonobj.put("EventDtl", event);
				
				EventArray.add(jsonobj);
			}
			rset.close();
			stmt.close();
			
			eventDtl.add(jsonobj);
			allDetail.put("EVENT_DTL", EventArray);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    public String countTargetDt(String priority) 
	{
    	String function_nm="countTargetDt()";
		String date="";
		try
		{
			int day=0;
			if(priority.equals("Low"))
			{
				day=15;
			}
			else if(priority.equals("Normal"))
			{
				day=9;
			}
			else if(priority.equals("Critical"))
			{
				day=1;
			}
			else if(priority.equals("High"))
			{
				day=7;
			}
			
			for(int i=1; i <=day; i++)
			{
				queryString2="SELECT TO_CHAR(SYSDATE+?,'DD/MM/YYYY') "
						+ "FROM DUAL";
				stmt5= dbcon.prepareStatement(queryString2);
				stmt5.setInt(1, i);
				rset5=stmt5.executeQuery();
				if(rset5.next())
				{
					date = rset5.getString(1)==null?"":rset5.getString(1);
					
					int holi_count=0;
					queryString1 = "SELECT COUNT(*) "
							+ "FROM DUAL "
							+ "WHERE TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'DY') IN (?,?) ";
					stmt4 = dbcon.prepareStatement(queryString1);
					stmt4.setString(1, date);
					stmt4.setString(2, "SAT");
					stmt4.setString(3, "SUN");
					rset4=stmt4.executeQuery();
					if(rset4.next())
					{
						holi_count = rset4.getInt(1);
						if(holi_count == 1)
						{
							day = day + 1; 
						}
					}
					rset4.close();
					stmt4.close();
				}
				rset5.close();
				stmt5.close();
			}
			
			if(date.equals(""))
			{
				date=""+dateUtil.getSysdate();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return date;
	}
}
