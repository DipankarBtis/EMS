package com.etrm.fms.remittance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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

import com.etrm.fms.util.DB_AllocationUtil;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.TaxCalculator;
import com.etrm.fms.util.UtilBean;
import com.google.gson.Gson;

@WebServlet("/servlet/DB_Remittance_Ajax")
public class DB_Remittance_Ajax extends HttpServlet
{
	static String db_src_file_name="DB_Remittance_Ajax.java";
	static Connection dbcon;
	static PreparedStatement stmt;
	static PreparedStatement stmt1;
	static PreparedStatement stmt2;
	static PreparedStatement stmt3;
	static PreparedStatement stmt4;
	static PreparedStatement stmt5;
	static PreparedStatement stmt6;
	static PreparedStatement stmt7;
	static PreparedStatement stmt8; 
	static PreparedStatement stmt9; 
	static PreparedStatement stmt10;
	static PreparedStatement stmt11;
	static ResultSet rset;
	static ResultSet rset1;
	static ResultSet rset2;
	static ResultSet rset3;
	static ResultSet rset4;
	static ResultSet rset5;
	static public String queryString;
	static public String queryString1;
	static public String queryString2;
	static public String setCallType="";
	static public String json = "";
	
	static JSONObject multipleData = new JSONObject(); 
	static JSONObject allDetail = new JSONObject();
	static JSONArray AllJsonArray = new JSONArray();
    
	static DateUtil dateUtil = new DateUtil();
	static UtilBean utilBean = new UtilBean();
	static TaxCalculator TaxCalc = new TaxCalculator();
	
	static NumberFormat nf = new DecimalFormat("###########0.00");
	
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
				Context envContext  = (Context)initContext.lookup("java:/comp/env");
				DataSource ds = (DataSource)envContext.lookup(RuntimeConf.security_database);
				Properties prop = System.getProperties();
				
				if (ds != null) 
				{
					dbcon = ds.getConnection();       
					if(dbcon != null)  
					{			
						setCallType = request.getParameter("setCallType");
						if(setCallType.equalsIgnoreCase("CALC_EXISTING_TAX_DTL_FOR_CR_DR"))
						{
							AllJsonArray = new JSONArray();
							allDetail = new JSONObject();
							calcExistingTaxDtl(request,response);
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
				if(rset != null){try {rset.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset1 != null){try {rset1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset2 != null){try {rset2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset3 != null){try {rset3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset4 != null){try {rset4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset5 != null){try {rset5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
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
		    	
		    	if(dbcon != null){try {dbcon.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			}
			try 
			{
				response.getWriter().write(json);
			} 
			catch(IOException e) 
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			}
    	}
	}
    
    private void calcExistingTaxDtl(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="calcExistingTaxDtl()";
   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			String taxStruct_cd=request.getParameter("tax_struct_cd")==null?"":request.getParameter("tax_struct_cd");
   			String grossAmt=request.getParameter("gross_amt")==null?"":request.getParameter("gross_amt");
   			
   			JSONObject jsonobj = new JSONObject();
			JSONArray taxDtl = new JSONArray();
			JSONArray subTaxDtl = new JSONArray();
			
			Vector temp = new Vector();
			temp=TaxCalc.TaxAmountCalculation(dbcon, taxStruct_cd, grossAmt);
			
			String tax_amt = nf.format(Double.parseDouble(""+temp.elementAt(0)));
			String tax_struct_cd = ""+temp.elementAt(1);
			String tax_struct_dt = ""+temp.elementAt(2);
			String tax_info = ""+temp.elementAt(3);
			String tax_struct_dtl = ""+temp.elementAt(4);
			String tax_factor = ""+temp.elementAt(5);
			
			jsonobj = new JSONObject();
			jsonobj.put("TAX_AMT",tax_amt);
			jsonobj.put("TAX_STRUCT_CD",tax_struct_cd);
			jsonobj.put("TAX_STRUCT_DTL",tax_struct_dtl);
			jsonobj.put("TAX_STRUCT_DT",tax_struct_dt);
			jsonobj.put("TAX_FACTOR",tax_factor);
		
			taxDtl.add(jsonobj);
			allDetail.put("TOTAL_TAX_DTL", taxDtl);
			
			Vector VMULTI_TAX_STRUCT=new Vector();
			VMULTI_TAX_STRUCT.add(temp.elementAt(6));
			
			if(VMULTI_TAX_STRUCT.size()>0)
			{
				for(int i=0;i<VMULTI_TAX_STRUCT.size();i++)
				{
					Vector temp1 =(Vector)((Vector)((Vector)VMULTI_TAX_STRUCT.elementAt(i)));
					
					for(int j=0;j<((Vector) temp1.elementAt(0)).size(); j++)
					{
						jsonobj = new JSONObject();
						taxDtl = new JSONArray();
						jsonobj.put("SUB_TAX_AMT",((Vector) temp1.elementAt(2)).elementAt(j));
						jsonobj.put("SUB_TAX_STRUCT_CD",tax_struct_cd);
						jsonobj.put("SUB_TAX_STRUCT_DTL",((Vector) temp1.elementAt(1)).elementAt(j));
						jsonobj.put("SUB_TAX_CODE",((Vector) temp1.elementAt(0)).elementAt(j));
						jsonobj.put("SUB_TAX_BASE_AMT",((Vector) temp1.elementAt(3)).elementAt(j));
						taxDtl.add(jsonobj);
						subTaxDtl.add(taxDtl);
					}
				}
			}
			allDetail.put("SUB_TAX_DTL", subTaxDtl);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
}
