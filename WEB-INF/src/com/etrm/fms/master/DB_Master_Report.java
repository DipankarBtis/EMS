package com.etrm.fms.master;

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

//Coded By          : Harsh Maheta
//Code Reviewed by	:  
//CR Date			: 21/03/2023 
//Status	  		: Developing

public class DB_Master_Report 
{	
	String db_src_file_name="DB_Master_Report.java";
	
	Connection conn; 
	PreparedStatement stmt;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt5;
	PreparedStatement stmt6;
	PreparedStatement stmt7;
	PreparedStatement stmt8;
	ResultSet rset;
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
	ResultSet rset6;
	ResultSet rset7;
	ResultSet rset8;
	String queryString="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	String queryString6="";
	String queryString7="";
	String queryString8="";

	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	
	public void init() 
	{
		String function_nm="init()";
		try
		{
			Context initContext = new InitialContext();
	    	
	    	Context envContext  = (Context)initContext.lookup("java:/comp/env");
	    	DataSource ds = (DataSource)envContext.lookup(RuntimeConf.security_database);
	    	if(ds != null) 
	    	{ 	    	
	    		conn = ds.getConnection();       
	    		if(conn != null)  
	    		{    		
	    			if(callFlag.equalsIgnoreCase("EXCH_DETAIL"))
	    			{
	    				getExchComponent();
	    				getExchRatedtl();
	    			}
	    			conn.close();
    			}
	    		conn = null;
	    	}
		}
		catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally
	    {
	    	if(rset != null){try{rset.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset1 != null){try{rset1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset2 != null){try{rset2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset3 != null){try{rset3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset4 != null){try{rset4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset5 != null){try{rset5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset5 != null){try{rset5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset6 != null){try{rset6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset7 != null){try{rset7.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset8 != null){try{rset8.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt6 != null){try{stmt6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt7 != null){try{stmt7.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt8 != null){try{stmt8.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
	public void getExchComponent() 
	{
		String function_nm = "getExchComponent()";
		try 
		{
			//SBI FIRST RATE TT BUY, SBI FIRST RATE TT SELL, SBI FIRST RATE TT BUY SELL
			queryString = "SELECT EXC_RATE_NM,EXC_RATE_CD "
					+ "FROM FMS_EXCHG_RATE_MST "
					+ "WHERE EXC_RATE_CD IN ('1','2','3') ";
			stmt=conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VEXCH_NM.add(rset.getString(1)==null?"":rset.getString(1));
				VEXCH_CD.add(rset.getString(2)==null?"":rset.getString(2));
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getExchRatedtl() 
	{
		String function_nm = "getExchRatedtl()";
		try 
		{
			Vector V1 = new Vector();
			Vector V2 = new Vector();
			Vector V3 = new Vector();
			
			queryString = "SELECT TO_CHAR(A.EFF_DT,'DD/MM/YYYY'),"
					+ "(SELECT B.EXCHG_VAL FROM FMS_EXCHG_RATE_ENTRY B WHERE B.EXCHG_RATE_CD = '1' AND A.EFF_DT=B.EFF_DT) AS EXCH_1, "//SBI FIRST RATE TT BUY
					+ "(SELECT C.EXCHG_VAL FROM FMS_EXCHG_RATE_ENTRY C WHERE C.EXCHG_RATE_CD = '2' AND A.EFF_DT=C.EFF_DT) AS EXCH_2, "//SBI FIRST RATE TT SELL
					+ "(SELECT D.EXCHG_VAL FROM FMS_EXCHG_RATE_ENTRY D WHERE D.EXCHG_RATE_CD = '3' AND A.EFF_DT=D.EFF_DT) AS EXCH_3 "//SBI FIRST RATE TT BUY SELL
					+ "FROM FMS_EXCHG_RATE_ENTRY A "
					+ "WHERE A.EFF_DT >= TO_DATE(?,'DD/MM/YYYY') AND A.EFF_DT <= TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.EXCHG_RATE_CD IN ('1','2','3') GROUP BY EFF_DT ORDER BY A.EFF_DT";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, from_dt);  
			stmt.setString(2, to_dt);    
			rset = stmt.executeQuery();
			while(rset.next())
			{
				VEFF_DT.add(rset.getString(1)==null?"":rset.getString(1));
				V1.add(rset.getString(2)==null?"":nf2.format(rset.getDouble(2)));
				V2.add(rset.getString(3)==null?"":nf2.format(rset.getDouble(3)));
				V3.add(rset.getString(4)==null?"":nf2.format(rset.getDouble(4)));
			}
			rset.close();
			stmt.close();
			
			VMULTI_EXCH.add(V1);
			VMULTI_EXCH.add(V2);
			VMULTI_EXCH.add(V3);
		}
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	String from_dt = "";
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	String to_dt = "";
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	
	
	String opration="";
	
	public void setOpration(String opration) {this.opration = opration;}
	
	Vector VEXCH_NM = new Vector();
	Vector VEXCH_CD = new Vector();
	Vector VEFF_DT = new Vector();
	Vector VMULTI_EXCH = new Vector();
	
	public Vector getVEXCH_NM() {return VEXCH_NM;}
	public Vector getVEXCH_CD() {return VEXCH_CD;}
	public Vector getVEFF_DT() {return VEFF_DT;}
	public Vector getVMULTI_EXCH() {return VMULTI_EXCH;}
}
