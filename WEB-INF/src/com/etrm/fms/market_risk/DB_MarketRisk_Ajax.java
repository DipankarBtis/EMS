package com.etrm.fms.market_risk;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.google.gson.Gson;

//import oracle.jdbc.driver.json.parser.JsonParserImpl;

//Coded By          : Harsh Maheta
//Code Reviewed by	:  
//CR Date			: 11/07/2023 
//Status	  		: Developing

@WebServlet("/servlet/DB_MarketRisk_Ajax")
public class DB_MarketRisk_Ajax extends HttpServlet
{
	static String db_src_file_name="DB_MarketRisk_Ajax.java";
	static Connection dbcon;
	static PreparedStatement stmt,stmt1,stmt2,stmt3,stmt4,stmt5,stmt6,stmt7,stmt8,stmt9,stmt10,stmt11;
	static ResultSet rset,rset1,rset2,rset3,rset4,rset5;
	public static String queryString, queryString1, queryString2;
	public static String setCallType="";
	public static String json = "";
	
	private static final String SAVE_DIR = CommonVariable.market_risk_dir;
	
	static JSONObject multipleData = new JSONObject();
	static JSONObject allDetail = new JSONObject();
	static JSONArray AllJsonArray = new JSONArray();
    
	static DateUtil dateUtil = new DateUtil();
	static UtilBean utilBean = new UtilBean();
    
    @SuppressWarnings("unused")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
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
					
					if(setCallType.equalsIgnoreCase("PRICE_DTL"))
					{
						AllJsonArray = new JSONArray();
						allDetail = new JSONObject();
						fetchPriceDetail(request,response);
						json = new Gson().toJson(AllJsonArray);
					}
					else if(setCallType.equalsIgnoreCase("FILE_DTL"))
					{
						AllJsonArray = new JSONArray();
						allDetail = new JSONObject();
						fetchFileDetail(request,response);
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
    
    private void fetchPriceDetail(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="fetchPriceDetail()";
   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			String delete_report_Dt=request.getParameter("delete_report_Dt")==null?"":request.getParameter("delete_report_Dt");
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray dateDtl = new JSONArray();
			JSONArray DateArray = new JSONArray();
			
			int index1 = 0;
			queryString = "SELECT TO_CHAR(REPORT_DT,'DD/MM/YYYY') "
					+ "FROM FMS_SPOT_PRICE_DTL "
					+ "WHERE REPORT_DT=TO_DATE(?,'DD/MM/YYYY')";
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, delete_report_Dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				index1+=1;
				jsonobj = new JSONObject();
				
				String date=rset.getString(1)==null?"":rset.getString(1);
			}
			jsonobj.put("SPOT_AVAILABLE_DT", ""+index1);
			rset.close();
			stmt.close();
			
			int index2 = 0;
			queryString1 = "SELECT TO_CHAR(REPORT_DT,'DD/MM/YYYY') "
					+ "FROM FMS_FORWARD_PRICE_DTL "
					+ "WHERE REPORT_DT=TO_DATE(?,'DD/MM/YYYY')";
			stmt1=dbcon.prepareStatement(queryString1);
			stmt1.setString(1, delete_report_Dt);
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				index2+=1;
				jsonobj = new JSONObject();
				
				String date=rset1.getString(1)==null?"":rset1.getString(1);
			}
			jsonobj.put("FORWARD_AVAILABLE_DT", ""+index2);
			rset1.close();
			stmt1.close();
			
			dateDtl.add(jsonobj);
			allDetail.put("SPOT_AVAILABLE_DT", index1);
			allDetail.put("FORWARD_AVAILABLE_DT", index2);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    private void fetchFileDetail(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="fetchFileDetail()";
   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			String upload_report_dt=request.getParameter("upload_report_dt")==null?"":request.getParameter("upload_report_dt");
   			String file_nm = "";
   			String file_name = "";
   			String file_size = "";
   			String file_upload_date = "";
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray files_nm = new JSONArray();
			JSONArray files_size = new JSONArray();
			JSONArray files_upload_dt = new JSONArray();
			
			JSONArray files_nameAt_dt = new JSONArray();
			JSONArray files_sizeAt_dt = new JSONArray();
			JSONArray files_updtAt_dt = new JSONArray();
			
			JSONArray price_dt_array = new JSONArray();
			
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

	        //String folder_path = appPath + "\\" + main_folder + "\\" + sub_folder + "\\" +innerSub_folder+"\\";//HP20230927 '\\' NOT WORKING IN LINUX
	        String folder_path = subSavePath+File.separator; //HP20230927
	        
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

	                         files_nm.add(allFileNames);
	                         files_size.add(allFileSizes);
	                         
	                         SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
	                         String formattedDate = dateFormat.format(new Date(lastModifiedTime));

	                         files_upload_dt.add(formattedDate);
	                     }
	                 }
	            }
	        }
	        String[] split_upload_dt = upload_report_dt.split("/");
	        String splited_upload_dt = split_upload_dt[2]+split_upload_dt[1]+split_upload_dt[0];
	        
			for(int i=0;i<files_nm.size();i++)
			{
				String inputString = ""+files_nm.get(i);
				int startIndex = inputString.indexOf("D_") + 2;
				int endIndex = startIndex+8;
				
				String extractedDate="";
			    if (startIndex != -1 && endIndex != -1) 
			    {
			        extractedDate = inputString.substring(startIndex, endIndex);
			    }
			    if(extractedDate.equals(splited_upload_dt))
			    {
			    	file_name=""+files_nm.get(i);
			    	files_nameAt_dt.add(file_name);
			    	
			    	file_size=""+files_size.get(i);
			    	files_sizeAt_dt.add(file_size);
			    
			    	file_upload_date=""+files_upload_dt.get(i);
			    	files_updtAt_dt.add(file_upload_date);
			    }
			}
			
			String curveDate="";
			queryString = "SELECT DISTINCT TO_CHAR(REPORT_DT,'DD/MM/YYYY') "
					+ "FROM FMS_FORWARD_PRICE_DTL ";
			stmt=dbcon.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				jsonobj = new JSONObject();
				
				curveDate=rset.getString(1)==null?"":rset.getString(1);
				String[] split_curveDate = curveDate.split("/");
				String splited_curveDate = split_curveDate[2]+split_curveDate[1]+split_curveDate[0];
				price_dt_array.add(splited_curveDate);
			}
			rset.close();
			stmt.close();
			
			String disableBtnFlag = "f";
			for(int j=0;j<price_dt_array.size();j++)
			{
				String curveDt_Array = price_dt_array.toString().replace("[", "").replace("]", "").replace("\"", "");
				String[] split_curveDt_Array = curveDt_Array.split(",");
				String splited_curveDt_Array = split_curveDt_Array[j];
				
				if (splited_curveDt_Array.equals(splited_upload_dt)) 
				{
					disableBtnFlag="t";
					break;
				}
			}
			allDetail.put("FILE_NM_DTL", files_nameAt_dt);
			allDetail.put("FILE_SIZE_DTL", files_sizeAt_dt);
			allDetail.put("FILE_UPDT_DTL", files_updtAt_dt);
			allDetail.put("FILE_NUM_DTL", files_updtAt_dt.size());
			allDetail.put("HIGHEST_CURVE_DT", price_dt_array);
			allDetail.put("BTNFLAG", disableBtnFlag);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
}