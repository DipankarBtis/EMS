package com.etrm.fms.master;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.etrm.fms.mail.MailDelivery;
import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.escapeSingleQuotes;

//Coded By          : Harsh Patel
//Code Reviewed by	:  
//CR Date			: 22/09/2022 
//Status	  		: Developing

@WebServlet("/servlet/Frm_counterparty")
public class Frm_counterparty extends HttpServlet
{
	public static String frm_src_file_name ="Frm_counterparty.java";

	public static  Connection dbcon;
	
	public static String servletName = "Frm_counterparty";
	public static String option = "";
	public static String url = "";
	public static String msg = "";
	public static String msg_type = "";
	public static String err_msg = "";
	
	private static String queryString = null;
	private static String query = null;
	private static String query1 = null;
	private static String query2 = null;
	private static String query3 = null;
	private static String query4 = null;
	private static PreparedStatement stmt = null;
	private static PreparedStatement stmt1 = null;
	private static PreparedStatement stmt2 = null;
	private static PreparedStatement stmt3 = null;
	private static PreparedStatement stmt4 = null;
	
	private static ResultSet rset = null;
	private static ResultSet rset1 = null;
	private static ResultSet rset2 = null;
	private static ResultSet rset3 = null;
	private static ResultSet rset4 = null;
	
	public static String form_id = "0";
	public static String form_nm = "";
	public static String mod_cd = "0";
	public static String mod_nm = "";
	public static String u = "";
	
	public static String old_value="";
	public static String new_value="";
	
	public static String commonUrl_pra="";
	
	public static escapeSingleQuotes escObj = new escapeSingleQuotes();
	
	static UtilBean utilBean = new UtilBean();
	static DateUtil utilDate = new DateUtil();
	static MailDelivery mailDelv = new MailDelivery();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException
	{
		synchronized(this)
		{
			String function_nm="doPost()";
			
			HttpSession session = request.getSession();
			if(session.getAttribute("emp_uid")==null || session.getAttribute("emp_uid")=="")
			{
				url="../sess/Expire.jsp";
			}
			else
			{
				try
				{
					Context Context = new InitialContext();
					if(Context == null) 
					{
						throw new Exception("Boom - No Context");
					}
					
					Context envContext  = (Context)Context.lookup("java:/comp/env");
					DataSource ds = (DataSource)envContext.lookup(RuntimeConf.security_database);
					
					if(ds != null)
					{
						dbcon = ds.getConnection();				
					}
					else
					{
						System.out.println("Data Source Not Found");
					}
					if(dbcon != null)
					{
						dbcon.setAutoCommit(false);
						
						form_id=request.getParameter("form_cd")==null?"0":request.getParameter("form_cd");
						form_nm=request.getParameter("form_nm")==null?"":request.getParameter("form_nm");
						mod_cd=request.getParameter("mod_cd")==null?"0":request.getParameter("mod_cd");
						mod_nm=request.getParameter("mod_nm")==null?"":request.getParameter("mod_nm");
						u=request.getParameter("u")==null?"":request.getParameter("u");
						
						new_value="";
						old_value="";
						
						option=request.getParameter("option")==null?"":request.getParameter("option");
						
						commonUrl_pra = "&u="+u;
						
						if(option.equalsIgnoreCase("COUNTERPARTY_MST"))
						{
							InsertUpdateCounterpartyDetail(request);
						}
						else if(option.equalsIgnoreCase("COUNTERPARTY_ADMIN"))
						{
							EntityRoleRequestApproval(request);
						}
						else if(option.equalsIgnoreCase("ENTITY_MST"))
						{
							InsertUpdateEntityDetail(request);
						}
						else if(option.equalsIgnoreCase("COUNTERPARTY_PLANT_MST"))
						{
							InsertUpdateCounterpartyPlantDetail(request);
						}
						else if(option.equalsIgnoreCase("COUNTERPARTY_BU_MST"))
						{
							InsertUpdateCounterpartyBuDetail(request);
						}
						else if(option.equalsIgnoreCase("COUNTERPARTY_BANK_MST"))
						{
							InsertUpdateCounterpartyBankDetail(request);
						}
						else if(option.equalsIgnoreCase("ENTITY_CONTACT_DETAILS"))
						{
							InsertUpdateEntityContactDetail(request);
						}
						else if(option.equalsIgnoreCase("ENTITY_TAX_MST"))
						{
							InsertUpdateEntityTaxDetail(request);
						}
						else if(option.equalsIgnoreCase("ENTITY_TURNOVER_ENTRY"))
						{
							InsertUpdateTurnoverEntry(request);
						}
						else if(option.equalsIgnoreCase("ENTITY_EMAIL_SETUP"))
						{
							InsertUpdateEntityEmailSetupDetail(request);
						}
						else if(option.equalsIgnoreCase("ENTITY_TCS_TDS_MST"))
						{
							InsertUpdateEntityTcsTdsTaxDetail(request);
						}
					}
					
					dbcon.close();
					dbcon=null;
				}
				catch(Exception e)
				{
					new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
					url=CommonVariable.errorpage_url+"?e="+e;
					msg="Error In Exception!";
				}
				finally
				{
					if(rset != null){try {rset.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);}}
					if(rset1 != null){try {rset1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);}}
					if(rset2 != null){try {rset2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);}}
					if(rset3 != null){try {rset3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);}}
					if(rset4 != null){try {rset4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);}}
					if(stmt != null){try {stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);}}
					if(stmt1 != null){try {stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);}}
					if(stmt2 != null){try {stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);}}
					if(stmt3 != null){try {stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);}}
					if(stmt4 != null){try {stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);}}
					if(dbcon != null){try {dbcon.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);}}
				}
			}
		
			try 
			{
				response.sendRedirect(url);
			}
			catch(IOException e) 
			{
				new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			}
		}
	}
	
	
	private void InsertUpdateCounterpartyDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateCounterpartyDetail()";

		msg="";
		msg_type="";
		url="";
		String opration="";
		String counterparty_cd="0";
		String form_clearance="";
		
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		
		try
		{
			opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			form_clearance = request.getParameter("form_clearance")==null?"KYC":request.getParameter("form_clearance");
			
			String eff_dt = request.getParameter("eff_dt")==null?"":request.getParameter("eff_dt");
			counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
			String name = request.getParameter("name")==null?"":request.getParameter("name");
			String abbr = request.getParameter("abbr")==null?"":request.getParameter("abbr");
			String pan_no = request.getParameter("pan_no")==null?"":request.getParameter("pan_no");
			String pan_dt = request.getParameter("pan_dt")==null?"":request.getParameter("pan_dt");
			String notes = request.getParameter("notes")==null?"":request.getParameter("notes");
			String kyc_flg = request.getParameter("kyc_flg")==null?"N":request.getParameter("kyc_flg");
			String igx_flg = request.getParameter("igx_flg")==null?"N":request.getParameter("igx_flg");
			String status=request.getParameter("status")==null?"Y":request.getParameter("status");
			String sap_code=request.getParameter("sap_code")==null?"":request.getParameter("sap_code");
			
			String reg_eff_dt = request.getParameter("reg_eff_dt")==null?"":request.getParameter("reg_eff_dt");
			String address_type = request.getParameter("address_type")==null?"":request.getParameter("address_type");
			String address = request.getParameter("address")==null?"":request.getParameter("address");
			String city = request.getParameter("city")==null?"":request.getParameter("city");
			String state = request.getParameter("state")==null?"":request.getParameter("state");
			String zone = request.getParameter("zone")==null?"":request.getParameter("zone");
			String pin = request.getParameter("pin")==null?"":request.getParameter("pin");
			String country = request.getParameter("country")==null?"":request.getParameter("country");
			String phone = request.getParameter("phone")==null?"":request.getParameter("phone");
			String alt_phone = request.getParameter("alt_phone")==null?"":request.getParameter("alt_phone");
			String cell = request.getParameter("cell")==null?"":request.getParameter("cell");
			String fax1 = request.getParameter("fax1")==null?"":request.getParameter("fax1");
			String fax2 = request.getParameter("fax2")==null?"":request.getParameter("fax2");
			String email = request.getParameter("email")==null?"":request.getParameter("email");
			String category=request.getParameter("category")==null?"":request.getParameter("category");
			String ncf_category=request.getParameter("ncf_category")==null?"":request.getParameter("ncf_category");
			
			name = escObj.replaceSingleQuotes(name);
			abbr = escObj.replaceSingleQuotes(abbr);
			notes = escObj.replaceSingleQuotes(notes);
			
			old_value=request.getParameter("old_value")==null?"":request.getParameter("old_value");
			
			if(opration.equalsIgnoreCase("INSERT"))
			{
				String query = "SELECT MAX(COUNTERPARTY_CD) "
						+ "FROM FMS_COUNTERPARTY_MST ";
				stmt=dbcon.prepareStatement(query);
				rset = stmt.executeQuery();
				int ID = 1;
				if(rset.next())
				{
					ID = Integer.parseInt(rset.getString(1)==null?"0":rset.getString(1));
					ID = ID + 1;
				}
				else
				{
					ID = 1;
				}
				rset.close();
				stmt.close();
				counterparty_cd = ID+"";
				
				old_value="";
				   
				query1 = "INSERT INTO FMS_COUNTERPARTY_MST (COUNTERPARTY_CD, EFF_DT , COUNTERPARTY_NM, COUNTERPARTY_ABBR, "
						+ "PAN_NO, PAN_ISSUE_DT, NOTES, ENT_BY, ENT_DT,STATUS,KYC,IGX,SAP_CODE,ENT_PROFILE) "
						+ "VALUES(?,TO_DATE(?,'DD/MM/YYYY'),?,?, "
						+ "?,to_date(?,'DD/MM/YYYY'),?,?,sysdate,?,?,?,?,?)";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, counterparty_cd);
				stmt1.setString(2, eff_dt);
				stmt1.setString(3, name);
				stmt1.setString(4, abbr);
				stmt1.setString(5, pan_no);
				stmt1.setString(6, pan_dt);
				stmt1.setString(7, notes);
				stmt1.setString(8, emp_cd);
				stmt1.setString(9, status);
				stmt1.setString(10, kyc_flg);
				stmt1.setString(11, igx_flg);
				stmt1.setString(12, sap_code);
				stmt1.setString(13, comp_cd);
		   		stmt1.executeUpdate();
		   		
		   		stmt1.close();
			}
			else
			{
				String queryString = "SELECT COUNT(*) "
						+ "FROM FMS_COUNTERPARTY_MST "
						+ "WHERE COUNTERPARTY_CD=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') ";
				stmt=dbcon.prepareStatement(queryString);
				stmt.setString(1, counterparty_cd);
				stmt.setString(2, eff_dt);
				rset = stmt.executeQuery();
		   		int count = 0;
		   		if(rset.next())
		   		{
		   			count = rset.getInt(1);
		   		}
		   		rset.close();
		   		stmt.close();
		   		
		   		if(count==0)
		   		{
		   			query1 = "INSERT INTO FMS_COUNTERPARTY_MST (COUNTERPARTY_CD, EFF_DT , COUNTERPARTY_NM, COUNTERPARTY_ABBR, "
							+ "PAN_NO, PAN_ISSUE_DT, NOTES, ENT_BY, ENT_DT,STATUS,KYC,IGX,SAP_CODE,ENT_PROFILE,CATEGORY,NCF_CATEGORY) "
							+ "VALUES(?,TO_DATE(?,'DD/MM/YYYY'),?,?, "
							+ "?,to_date(?,'DD/MM/YYYY'),?,?,sysdate,?,?,?,?,?,?,?)";
		   			stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, counterparty_cd);
					stmt1.setString(2, eff_dt);
					stmt1.setString(3, name);
					stmt1.setString(4, abbr);
					stmt1.setString(5, pan_no);
					stmt1.setString(6, pan_dt);
					stmt1.setString(7, notes);
					stmt1.setString(8, emp_cd);
					stmt1.setString(9, status);
					stmt1.setString(10, kyc_flg);
					stmt1.setString(11, igx_flg);
					stmt1.setString(12, sap_code);
					stmt1.setString(13, comp_cd);
					stmt1.setString(14, category);
					stmt1.setString(15, ncf_category);
			   		stmt1.executeUpdate();
			   		
			   		stmt1.close();
		   		}
		   		else
		   		{
		   			query1 = "UPDATE FMS_COUNTERPARTY_MST SET COUNTERPARTY_NM=?, COUNTERPARTY_ABBR=?,PAN_NO  =?, "
		   					+ "PAN_ISSUE_DT=to_date(?,'DD/MM/YYYY'),NOTES=?, ENT_BY=?, ENT_DT=sysdate,"
		   					+ "EFF_DT=TO_DATE(?,'dd/mm/yyyy'),MODIFY_BY=?, MODIFY_DT=sysdate,"
		   					+ "KYC=?, IGX=?,SAP_CODE=?,STATUS=?,MOD_PROFILE=? "
		   					+ "WHERE COUNTERPARTY_CD=? "
		   					+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') "; 
		   			stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, name);
					stmt1.setString(2, abbr);
					stmt1.setString(3, pan_no);
					stmt1.setString(4, pan_dt);
					stmt1.setString(5, notes);
					stmt1.setString(6, emp_cd);
					stmt1.setString(7, eff_dt);
					stmt1.setString(8, emp_cd);
					stmt1.setString(9, kyc_flg);
					stmt1.setString(10, igx_flg);
					stmt1.setString(11, sap_code);
					stmt1.setString(12, status);
					stmt1.setString(13, comp_cd);
					stmt1.setString(14, counterparty_cd);
					stmt1.setString(15, eff_dt);
			   		stmt1.executeUpdate();
			   		
			   		stmt1.close();
		   		}
		   		
		   		///ALSO UPDATE PAN NO AT PLANT LEVEL FOR ALL PROFILES, 
		   		///20230720 AS DISCUSSED WITH MADAM, ONLY PAN NO WILL BE UPDATED.
		   		query2 = "UPDATE FMS_COUNTERPARTY_PLANT_TAX SET STAT_NO=? "
		   				+ "WHERE COUNTERPARTY_CD=? AND STAT_CD=? "
		   				+ "AND ENTITY IN (?,?,?)";
		   		stmt2=dbcon.prepareStatement(query2);
		   		stmt2.setString(1, pan_no);
		   		stmt2.setString(2, counterparty_cd);
		   		stmt2.setString(3, "1001");
		   		stmt2.setString(4, "C");
		   		stmt2.setString(5, "R");
		   		stmt2.setString(6, "T");
		   		stmt2.executeUpdate();
		   		
		   		stmt2.close();
			}
			
			new_value="CP="+counterparty_cd+"#NAME="+name+"#ABBR="+abbr+"#PANNO="+pan_no+"#PANDT="+pan_dt+"#KYC="+kyc_flg+"#IGX="+igx_flg+"#EFFDT="+eff_dt+
					"#ADD="+address+"#CITY="+city+"#STATE="+state+"#ZONE="+zone+"#PIN="+pin+"#COUNTRY="+country+"#PH="+phone+"#FAX1="+fax1+
					"#FAX2="+fax2+"#CELL="+cell+"#EMAIL="+email+"#ALTPH="+alt_phone+"#REFFDT="+reg_eff_dt+"#STATUS="+status+"#SAPCD="+sap_code+"#NOTES="+notes;
			
			String query = "SELECT COUNT(*) "
					+ "FROM FMS_COUNTERPARTY_ADDR_MST "
					+ "WHERE COUNTERPARTY_CD = ? AND ADDRESS_TYPE=? "
					+ "AND EFF_DT = TO_DATE(?,'DD/MM/YYYY') ";
			stmt=dbcon.prepareStatement(query);
			stmt.setString(1, counterparty_cd);
			stmt.setString(2, address_type);
			stmt.setString(3, reg_eff_dt);
			rset = stmt.executeQuery();
			int count = 0;
			if(rset.next())
		    {
				count = rset.getInt(1);
		    }
			rset.close();
			stmt.close();
			
			if(count>0)
	    	{  
			    query1 = "UPDATE FMS_COUNTERPARTY_ADDR_MST SET ADDR=?,CITY=?,PIN=?,STATE=?,ZONE=?,"
			    		+ "COUNTRY=?,PHONE=?,MOBILE=?,ALT_PHONE=?,FAX_1=?,FAX_2=?,"
			    		+ "EMAIL=?,MODIFY_DT=SYSDATE,MODIFY_BY=? "
			    		+ "WHERE COUNTERPARTY_CD = ? AND ADDRESS_TYPE=? "
			    		+ "AND EFF_DT = TO_DATE(?,'DD/MM/YYYY') ";
			    stmt1=dbcon.prepareStatement(query1);
			    stmt1.setString(1, address);
			    stmt1.setString(2, city);
			    stmt1.setString(3, pin);
			    stmt1.setString(4, state);
			    stmt1.setString(5, zone);
			    stmt1.setString(6, country);
			    stmt1.setString(7, phone);
			    stmt1.setString(8, cell);
			    stmt1.setString(9, alt_phone);
			    stmt1.setString(10, fax1);
			    stmt1.setString(11, fax2);
			    stmt1.setString(12, email);
			    stmt1.setString(13, emp_cd);
			    stmt1.setString(14, counterparty_cd);
			    stmt1.setString(15, address_type);
			    stmt1.setString(16, reg_eff_dt);
			    stmt1.executeUpdate();
			    
			    stmt1.close();
		    }
			else
			{
				query1="INSERT INTO FMS_COUNTERPARTY_ADDR_MST(COUNTERPARTY_CD, ADDRESS_TYPE,EFF_DT, ADDR, CITY, PIN, "
						+ "STATE, ZONE,COUNTRY, PHONE, MOBILE, ALT_PHONE, FAX_1,FAX_2, EMAIL, ENT_BY, ENT_DT) "
						+ "VALUES(?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,"
						+ "?,?,?,?,?,?,?,?,?,"
						+ "?,sysdate) "; 
				 stmt1=dbcon.prepareStatement(query1);
				 stmt1.setString(1, counterparty_cd);
				 stmt1.setString(2, address_type);
			     stmt1.setString(3, reg_eff_dt);
			     stmt1.setString(4, address);
			     stmt1.setString(5, city);
			     stmt1.setString(6, pin);
			     stmt1.setString(7, state);
			     stmt1.setString(8, zone);
			     stmt1.setString(9, country);
			     stmt1.setString(10, phone);
			     stmt1.setString(11, cell);
			     stmt1.setString(12, alt_phone);
			     stmt1.setString(13, fax1);
			     stmt1.setString(14, fax2);
			     stmt1.setString(15, email);
			     stmt1.setString(16, emp_cd);
			     stmt1.executeUpdate();
				    
				 stmt1.close();
			}
			
			CounterpartyMailBody(comp_cd,name, abbr, opration,msg,emp_cd,new_value,old_value);
			
			if(opration.equalsIgnoreCase("INSERT"))
			{
				opration="MODIFY";
				msg = "Successful! - Counterparty "+name+" Added!";
				msg_type="S";
			}
			else
			{
				msg = "Successful! - Counterparty "+name+" updated!";
				msg_type="S";
			}
			
			dbcon.commit();
			
			/*if(opration.equals("INSERT"))
			{
				counterparty_cd="0";
			}*/
			
			if(form_clearance.equals("IGX"))
			{
				url = "../master/frm_igx_counterparty_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+commonUrl_pra;
			}
			else
			{
				url = "../master/frm_counterparty_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+commonUrl_pra;
			}
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - Counterparty Addition/Modification Failed!";			
			url=CommonVariable.errorpage_url+"?e="+e;
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void EntityRoleRequestApproval(HttpServletRequest request) throws SQLException
	{
		msg="";
		msg_type="";
		url="";
		
		String function_nm="EntityRoleRequestApproval()";

		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		
		String counterparty_nm = request.getParameter("counterparty_nm")==null?"":request.getParameter("counterparty_nm");
		String entity_role = request.getParameter("entity_role")==null?"":request.getParameter("entity_role");
		
		String status = request.getParameter("status")==null?"":request.getParameter("status");
		
		String counterpty_cd[] = request.getParameterValues("counterpty_cd");
		String SEQ_NO[] = request.getParameterValues("seq_no");
		String aprv_note[] = request.getParameterValues("aprv_note");
		String entityRole[] = request.getParameterValues("entityRole");
		
		String entity_nm="";
		if(entity_role.equals("C")) {
			entity_nm="Customer";
		}else if(entity_role.equals("T")) {
			entity_nm="Trader";
		}else if(entity_role.equals("R")) {
			entity_nm="Transporter";
		}else if(entity_role.equals("V")) {
			entity_nm="Vessel Agent";
		}else if(entity_role.equals("H")) {
			entity_nm="Custom House Agent";
		}else if(entity_role.equals("S")) {
			entity_nm="Surveyor";
		}
		
		
		String req_nm="";
		if(status.equals("A")) {
			req_nm="Approve";
		}else if(status.equals("R")) {
			req_nm="Request";
		}else if(status.equals("X")) {
			req_nm="Reject";
		}
		
		try
		{
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String remark = request.getParameter("remark")==null?"":request.getParameter("remark");
			
			remark = escObj.replaceSingleQuotes(remark);
			
			if(status.equals("A") || status.equals("X"))
			{
				if(counterpty_cd != null)
				{
					for(int i=0; i<counterpty_cd.length; i++)
					{
						entity_nm="";
						if(entityRole[i].equals("C")) {
							entity_nm="Customer";
						}else if(entityRole[i].equals("T")) {
							entity_nm="Trader";
						}else if(entityRole[i].equals("R")) {
							entity_nm="Transporter";
						}else if(entityRole[i].equals("V")) {
							entity_nm="Vessel Agent";
						}else if(entityRole[i].equals("H")) {
							entity_nm="Custom House Agent";
						}else if(entityRole[i].equals("S")) {
							entity_nm="Surveyor";
						}
						
						query="UPDATE FMS_ENTITY_REQ_DTL SET APRV_NOTE=?,STATUS=?, APRV_BY=?, "
								+ "APRV_DT=SYSDATE "
								+ "WHERE COUNTERPARTY_CD=? AND SEQ_NO=? AND COMPANY_CD=? ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, escObj.replaceSingleQuotes(aprv_note[i]));
						stmt.setString(2, status);
						stmt.setString(3, emp_cd);
						stmt.setString(4, counterpty_cd[i]);
						stmt.setString(5, SEQ_NO[i]);
						stmt.setString(6, comp_cd);
						stmt.executeUpdate();
						
						stmt.close();
						
						/*if(status.equals("A"))
						{
							int cont=0;
							
							query1="UPDATE FMS_COUNTERPARTY_MST A SET ";
							if(entityRole[i].equals("C")) {
								query1+="CUSTOMER=? ";
							}else if(entityRole[i].equals("T")) {
								query1+="TRADER=? ";
							}else if(entityRole[i].equals("R")) {
								query1+="TRANSPORTER=? ";
							}else if(entityRole[i].equals("V")) {
								query1+="VESSEL_AGENT=? ";
							}else if(entityRole[i].equals("H")) {
								query1+="CUSTOM_HOUSE_AGENT=? ";
							}else if(entityRole[i].equals("S")) {
								query1+="SURVEYOR=? ";
							}
							query1+="WHERE COUNTERPARTY_CD=? AND COMPANY_CD=? "
									+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.COMPANY_CD=B.COMPANY_CD)";
							stmt1=dbcon.prepareStatement(query1);
							if(entityRole[i].equals("C")) 
							{
								stmt1.setString(++cont, "Y");
							}
							else if(entityRole[i].equals("T")) 
							{
								stmt1.setString(++cont, "Y");
							}
							else if(entityRole[i].equals("R")) 
							{
								stmt1.setString(++cont, "Y");
							}
							else if(entityRole[i].equals("V")) 
							{
								stmt1.setString(++cont, "Y");
							}
							else if(entityRole[i].equals("H")) 
							{
								stmt1.setString(++cont, "Y");
							}
							else if(entityRole[i].equals("S")) 
							{
								stmt1.setString(++cont, "Y");
							}
							stmt1.setString(++cont, counterpty_cd[i]);
							stmt1.setString(++cont, comp_cd);
							stmt1.executeUpdate();
							
							stmt1.close();
						}*/
						
						msg = "Successful! - "+entity_nm+" Role for "+counterparty_nm+" "+req_nm+"ed!";
						msg_type="S";
						
						dbcon.commit();
					}
				}
			}
			else
			{
				if((!counterparty_cd.equals("") && !counterparty_cd.equals("0")) && (!entity_role.equals("") && !entity_role.equals("0")))
				{
					String seq_no="1";
					String query="SELECT MAX(SEQ_NO) "
							+ "FROM FMS_ENTITY_REQ_DTL "
							+ "WHERE COUNTERPARTY_CD=? AND COMPANY_CD=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, counterparty_cd);
					stmt.setString(2, comp_cd);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						seq_no = ""+(rset.getInt(1)+1);
					}
					rset.close();
					stmt.close();
					
					query1="INSERT INTO FMS_ENTITY_REQ_DTL(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,ENTITY,REMARK,STATUS,REQ_BY,REQ_DT) "
							+ "VALUES(?,?,?,?,"
							+ "?,?,?,SYSDATE)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, seq_no);
					stmt1.setString(4, entity_role);
					stmt1.setString(5, remark);
					stmt1.setString(6, status);
					stmt1.setString(7, emp_cd);
					stmt1.executeUpdate();
					dbcon.commit();
					
					stmt1.close();
					
					msg = "Successful! - "+entity_nm+" Role for "+counterparty_nm+" "+req_nm+"ed!";
					msg_type="S";		
				}
			}
			
			url = "../master/frm_counterparty_admin.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			msg = "Error in Exception! - "+entity_nm+" Role for "+counterparty_nm+" "+req_nm+" Failed!";
			url=CommonVariable.errorpage_url+"?e="+e;
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateEntityDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateEntityDetail()";

		msg="";
		msg_type="";
		url="";
		String opration="";
		String counterparty_cd="0";
		String entity_role="0";
		
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		
		try
		{
			opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			entity_role = request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");
			
			String eff_dt = request.getParameter("eff_dt")==null?"":request.getParameter("eff_dt");
			counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
			String name = request.getParameter("name")==null?"":request.getParameter("name");
			String abbr = request.getParameter("abbr")==null?"":request.getParameter("abbr");
			String pan_no = request.getParameter("pan_no")==null?"":request.getParameter("pan_no");
			String pan_dt = request.getParameter("pan_dt")==null?"":request.getParameter("pan_dt");
			String notes = request.getParameter("notes")==null?"":request.getParameter("notes");
			String kyc_flg = request.getParameter("kyc_flg")==null?"N":request.getParameter("kyc_flg");
			String igx_flg = request.getParameter("igx_flg")==null?"N":request.getParameter("igx_flg");
			String status=request.getParameter("status")==null?"Y":request.getParameter("status");
			String category=request.getParameter("category")==null?"":request.getParameter("category");
			String ncf_category=request.getParameter("ncf_category")==null?"":request.getParameter("ncf_category");
			String web_addr=request.getParameter("web_addr")==null?"":request.getParameter("web_addr");
			
			String[] reg_eff_dt = request.getParameterValues("reg_eff_dt");
			String[] address_type = request.getParameterValues("address_type");
			String[] address = request.getParameterValues("address");
			String[] city = request.getParameterValues("city");
			String[] state = request.getParameterValues("state");
			String[] zone = request.getParameterValues("zone");
			String[] pin = request.getParameterValues("pin");
			String[] country = request.getParameterValues("country");
			String[] phone = request.getParameterValues("phone");
			String[] alt_phone = request.getParameterValues("alt_phone");
			String[] cell = request.getParameterValues("cell");
			String[] fax1 = request.getParameterValues("fax1");
			String[] fax2 = request.getParameterValues("fax2");
			String[] email = request.getParameterValues("email");
			
			name = escObj.replaceSingleQuotes(name);
			abbr = escObj.replaceSingleQuotes(abbr);
			notes = escObj.replaceSingleQuotes(notes);
			
			String entity_roleNm="";
			if(entity_role.equals("G"))
			{
				entity_roleNm = "Gas Exchange";
			}
			else if(entity_role.equals("B"))
			{
				entity_roleNm = "Business Owner";
			}
			else if(entity_role.equals("C")) 
			{
				entity_roleNm = "Customer";
			}
			else if (entity_role.equals("T"))
			{
				entity_roleNm = "Trader";
			}
			else if (entity_role.equals("R"))
			{
				entity_roleNm = "Transporter";
			}
			else if (entity_role.equals("V"))
			{
				entity_roleNm = "Vessel Agent";
			}
			else if (entity_role.equals("H"))
			{
				entity_roleNm = "Custom House Agent";
			}
			else if (entity_role.equals("S"))
			{
				entity_roleNm = "Surveyor";
			}
			
			if(entity_role.equals("B"))
			{
				String queryString = "SELECT COUNT(*) "
						+ "FROM FMS_COMPANY_OWNER_MST "
						+ "WHERE COMPANY_CD=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY')";
		   		stmt=dbcon.prepareStatement(queryString);
		   		stmt.setString(1, counterparty_cd);
		   		stmt.setString(2, eff_dt);
				rset = stmt.executeQuery();
		   		int count = 0;
		   		if(rset.next())
		   		{
		   			count = rset.getInt(1);
		   		}
		   		rset.close();
		   		stmt.close();
		   		
		   		if(count>0)
		   		{
		   			query1 = "UPDATE FMS_COMPANY_OWNER_MST SET CATEGORY=?, WEB_ADDR=?,"
		   					+ "MODIFY_BY=?, MODIFY_DT=SYSDATE, NCF_CATEGORY=? "
		   					+ "WHERE COMPANY_CD=? "
		   					+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') "; 
		   			stmt1=dbcon.prepareStatement(query1);
		   			stmt1.setString(1, category);
		   			stmt1.setString(2, web_addr);
		   			stmt1.setString(3, emp_cd);
		   			stmt1.setString(4, ncf_category);
		   			stmt1.setString(5, counterparty_cd);
		   			stmt1.setString(6, eff_dt);
		   			stmt1.executeUpdate();
		   			
		   			stmt1.close();
		   			
		   			msg = "Successful! - "+entity_roleNm+" "+name+" updated!";
					msg_type="S";
		   		}
			}
			else if(entity_role.equals("G"))
			{
				String queryString = "SELECT COUNT(*) "
						+ "FROM FMS_COMPANY_EXCHG_MST "
						+ "WHERE COUNTERPARTY_CD=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY')";
				stmt=dbcon.prepareStatement(queryString);
		   		stmt.setString(1, counterparty_cd);
		   		stmt.setString(2, eff_dt);
				rset = stmt.executeQuery();
		   		int count = 0;
		   		if(rset.next())
		   		{
		   			count = rset.getInt(1);
		   		}
		   		rset.close();
		   		stmt.close();
		   		
		   		if(count>0)
		   		{
		   			query1 = "UPDATE FMS_COMPANY_EXCHG_MST SET CATEGORY=?, WEB_ADDR=?,"
		   					+ "MODIFY_BY=?, MODIFY_DT=SYSDATE, NCF_CATEGORY=?  "
		   					+ "WHERE COUNTERPARTY_CD=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY')"; 
		   			stmt1=dbcon.prepareStatement(query1);
		   			stmt1.setString(1, category);
		   			stmt1.setString(2, web_addr);
		   			stmt1.setString(3, emp_cd);
		   			stmt1.setString(4, ncf_category);
		   			stmt1.setString(5, counterparty_cd);
		   			stmt1.setString(6, eff_dt);
		   			stmt1.executeUpdate();
		   			
		   			stmt1.close();
		   			
		   			msg = "Successful! - "+entity_roleNm+" "+name+" updated!";
					msg_type="S";
		   		}
			}
			else
			{
				String queryString = "SELECT COUNT(*) "
						+ "FROM FMS_COUNTERPARTY_MST "
						+ "WHERE COUNTERPARTY_CD=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') ";
				stmt=dbcon.prepareStatement(queryString);
		   		stmt.setString(1, counterparty_cd);
		   		stmt.setString(2, eff_dt);
				rset = stmt.executeQuery();
		   		int count = 0;
		   		if(rset.next())
		   		{
		   			count = rset.getInt(1);
		   		}
		   		rset.close();
		   		stmt.close();
		   		
		   		if(count>0)
		   		{
		   			query1 = "UPDATE FMS_COUNTERPARTY_MST SET CATEGORY=?, WEB_ADDR=?,"
		   					+ "MODIFY_BY=?, MODIFY_DT=SYSDATE, NCF_CATEGORY=? "
		   					+ "WHERE COUNTERPARTY_CD=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') "; 
		   			stmt1=dbcon.prepareStatement(query1);
		   			stmt1.setString(1, category);
		   			stmt1.setString(2, web_addr);
		   			stmt1.setString(3, emp_cd);
		   			stmt1.setString(4, ncf_category);
		   			stmt1.setString(5, counterparty_cd);
		   			stmt1.setString(6, eff_dt);
		   			stmt1.executeUpdate();
		   			
		   			stmt1.close();
		   			
		   			msg = "Successful! - "+entity_roleNm+" "+name+" updated!";
					msg_type="S";
		   		}
			}
			
			if(address_type != null)
			{
				for(int i=0; i<address_type.length;i++)
				{
					if(entity_role.equals("B"))
					{
						String query = "SELECT COUNT(*) "
								+ "FROM FMS_COMPANY_OWNER_ADDR_MST "
								+ "WHERE COMPANY_CD = ? AND ADDRESS_TYPE=? "
								+ "AND EFF_DT = TO_DATE(?,'DD/MM/YYYY')";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, counterparty_cd);
						stmt.setString(2, address_type[i]);
						stmt.setString(3, reg_eff_dt[i]);
						rset = stmt.executeQuery();
						int count = 0;
						if(rset.next())
					    {
							count = rset.getInt(1);
					    }
						rset.close();
						stmt.close();
						
						if(count>0)
				    	{  
						    query1 = "UPDATE FMS_COMPANY_OWNER_ADDR_MST SET ADDR=?,CITY=?,PIN=?,STATE=?,ZONE=?,"
						    		+ "COUNTRY=?,PHONE=?,MOBILE=?,ALT_PHONE=?,FAX_1=?,FAX_2=?,"
						    		+ "EMAIL=?,MODIFY_DT=SYSDATE,MODIFY_BY=? "
						    		+ "WHERE COMPANY_CD = ? AND ADDRESS_TYPE=? "
						    		+ "AND EFF_DT = TO_DATE(?,'DD/MM/YYYY')";
						    stmt1=dbcon.prepareStatement(query1);
						    stmt1.setString(1, address[i]);
						    stmt1.setString(2, city[i]);
						    stmt1.setString(3, pin[i]);
						    stmt1.setString(4, state[i]);
						    stmt1.setString(5, zone[i]);
						    stmt1.setString(6, country[i]);
						    stmt1.setString(7, phone[i]);
						    stmt1.setString(8, cell[i]);
						    stmt1.setString(9, alt_phone[i]);
						    stmt1.setString(10, fax1[i]);
						    stmt1.setString(11, fax2[i]);
						    stmt1.setString(12, email[i]);
						    stmt1.setString(13, emp_cd);
						    stmt1.setString(14, counterparty_cd);
						    stmt1.setString(15, address_type[i]);
						    stmt1.setString(16, reg_eff_dt[i]);
						    stmt1.executeUpdate();
						    
						    stmt1.close();
					    }
						else
						{
							query1="INSERT INTO FMS_COMPANY_OWNER_ADDR_MST(COMPANY_CD, ADDRESS_TYPE,EFF_DT, ADDR, CITY, PIN, "
									+ "STATE, ZONE,COUNTRY, PHONE, MOBILE, ALT_PHONE, FAX_1,FAX_2, EMAIL, ENT_BY, ENT_DT) "
									+ "VALUES(?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,"
									+ "?,?,?,?,?,?,?,?,?,"
									+ "?,sysdate) "; 
						    stmt1=dbcon.prepareStatement(query1);
						    stmt1.setString(1, counterparty_cd);
						    stmt1.setString(2, address_type[i]);
						    stmt1.setString(3, reg_eff_dt[i]);
						    stmt1.setString(4, address[i]);
						    stmt1.setString(5, city[i]);
						    stmt1.setString(6, pin[i]);
						    stmt1.setString(7, state[i]);
						    stmt1.setString(8, zone[i]);
						    stmt1.setString(9, country[i]);
						    stmt1.setString(10, phone[i]);
						    stmt1.setString(11, cell[i]);
						    stmt1.setString(12, alt_phone[i]);
						    stmt1.setString(13, fax1[i]);
						    stmt1.setString(14, fax2[i]);
						    stmt1.setString(15, email[i]);
						    stmt1.setString(16, emp_cd);
						    
						    stmt1.executeUpdate();
						    
						    stmt1.close();
						}
					}
					else if(entity_role.equals("G"))
					{
						String query = "SELECT COUNT(*) "
								+ "FROM FMS_COMPANY_EXCHG_ADDR_MST "
								+ "WHERE ADDRESS_TYPE=? "
								+ "AND EFF_DT = TO_DATE(?,'DD/MM/YYYY') AND COUNTERPARTY_CD=?";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, address_type[i]);
						stmt.setString(2, reg_eff_dt[i]);
						stmt.setString(3, counterparty_cd);
						rset = stmt.executeQuery();
						int count = 0;
						if(rset.next())
					    {
							count = rset.getInt(1);
					    }
						rset.close();
						stmt.close();
						
						if(count>0)
				    	{  
						    query1 = "UPDATE FMS_COMPANY_EXCHG_ADDR_MST SET ADDR=?,CITY=?,PIN=?,STATE=?,ZONE=?,"
						    		+ "COUNTRY=?,PHONE=?,MOBILE=?,ALT_PHONE=?,FAX_1=?,FAX_2=?,"
						    		+ "EMAIL=?,MODIFY_DT=SYSDATE,MODIFY_BY=? "
						    		+ "WHERE ADDRESS_TYPE=? "
									+ "AND EFF_DT = TO_DATE(?,'DD/MM/YYYY') AND COUNTERPARTY_CD=?";
						    stmt1=dbcon.prepareStatement(query1);
						    stmt1.setString(1, address[i]);
						    stmt1.setString(2, city[i]);
						    stmt1.setString(3, pin[i]);
						    stmt1.setString(4, state[i]);
						    stmt1.setString(5, zone[i]);
						    stmt1.setString(6, country[i]);
						    stmt1.setString(7, phone[i]);
						    stmt1.setString(8, cell[i]);
						    stmt1.setString(9, alt_phone[i]);
						    stmt1.setString(10, fax1[i]);
						    stmt1.setString(11, fax2[i]);
						    stmt1.setString(12, email[i]);
						    stmt1.setString(13, emp_cd);
						    stmt1.setString(14, address_type[i]);
						    stmt1.setString(15, reg_eff_dt[i]);
						    stmt1.setString(16, counterparty_cd);
						    stmt1.executeUpdate();
						    
						    stmt1.close();
					    }
						else
						{
							query1="INSERT INTO FMS_COMPANY_EXCHG_ADDR_MST(COUNTERPARTY_CD, ADDRESS_TYPE,EFF_DT, ADDR, CITY, PIN, "
									+ "STATE, ZONE,COUNTRY, PHONE, MOBILE, ALT_PHONE, FAX_1,FAX_2, EMAIL, ENT_BY, ENT_DT) "
									+ "VALUES(?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,"
									+ "?,?,?,?,?,?,?,?,?,"
									+ "?,sysdate) "; 
							stmt1=dbcon.prepareStatement(query1);
							stmt1.setString(1, counterparty_cd);
						    stmt1.setString(2, address_type[i]);
						    stmt1.setString(3, reg_eff_dt[i]);
						    stmt1.setString(4, address[i]);
						    stmt1.setString(5, city[i]);
						    stmt1.setString(6, pin[i]);
						    stmt1.setString(7, state[i]);
						    stmt1.setString(8, zone[i]);
						    stmt1.setString(9, country[i]);
						    stmt1.setString(10, phone[i]);
						    stmt1.setString(11, cell[i]);
						    stmt1.setString(12, alt_phone[i]);
						    stmt1.setString(13, fax1[i]);
						    stmt1.setString(14, fax2[i]);
						    stmt1.setString(15, email[i]);
						    stmt1.setString(16, emp_cd);
						    
						    stmt1.executeUpdate();
						    
						    stmt1.close();
						}
					}
					else if(!address_type[i].equals("R"))
					{
						String query = "SELECT COUNT(*) "
								+ "FROM FMS_ENTITY_ADDR_MST "
								+ "WHERE COUNTERPARTY_CD = ? AND ADDRESS_TYPE=? "
								+ "AND EFF_DT = TO_DATE(?,'DD/MM/YYYY') AND COMPANY_CD=? "
								+ "AND ENTITY=?";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, counterparty_cd);
						stmt.setString(2, address_type[i]);
						stmt.setString(3, reg_eff_dt[i]);
						stmt.setString(4, comp_cd);
						stmt.setString(5, entity_role);
						rset = stmt.executeQuery();
						int count = 0;
						if(rset.next())
					    {
							count = rset.getInt(1);
					    }
						rset.close();
						stmt.close();
						
						if(count>0)
				    	{  
						    query1 = "UPDATE FMS_ENTITY_ADDR_MST SET ADDR=?,CITY=?,PIN=?,STATE=?,ZONE=?,"
						    		+ "COUNTRY=?,PHONE=?,MOBILE=?,ALT_PHONE=?,FAX_1=?,FAX_2=?,"
						    		+ "EMAIL=?,MODIFY_DT=SYSDATE,MODIFY_BY=? "
						    		+ "WHERE COUNTERPARTY_CD = ? AND ADDRESS_TYPE=? "
						    		+ "AND EFF_DT = TO_DATE(?,'DD/MM/YYYY') AND COMPANY_CD=? AND ENTITY=?";
						    stmt1=dbcon.prepareStatement(query1);
						    stmt1.setString(1, address[i]);
						    stmt1.setString(2, city[i]);
						    stmt1.setString(3, pin[i]);
						    stmt1.setString(4, state[i]);
						    stmt1.setString(5, zone[i]);
						    stmt1.setString(6, country[i]);
						    stmt1.setString(7, phone[i]);
						    stmt1.setString(8, cell[i]);
						    stmt1.setString(9, alt_phone[i]);
						    stmt1.setString(10, fax1[i]);
						    stmt1.setString(11, fax2[i]);
						    stmt1.setString(12, email[i]);
						    stmt1.setString(13, emp_cd);
						    stmt1.setString(14, counterparty_cd);
						    stmt1.setString(15, address_type[i]);
						    stmt1.setString(16, reg_eff_dt[i]);
						    stmt1.setString(17, comp_cd);
						    stmt1.setString(18, entity_role);
						    stmt1.executeUpdate();
						    
						    stmt1.close();
					    }
						else
						{
							query1="INSERT INTO FMS_ENTITY_ADDR_MST(COMPANY_CD,COUNTERPARTY_CD, ADDRESS_TYPE,EFF_DT, ADDR, CITY, PIN, "
									+ "STATE, ZONE,COUNTRY, PHONE, MOBILE, ALT_PHONE, FAX_1,FAX_2, EMAIL, ENT_BY, ENT_DT, ENTITY) "
									+ "VALUES(?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,"
									+ "?,?,?,?,?,?,?,?,?,"
									+ "?,sysdate,?) "; 
							stmt1=dbcon.prepareStatement(query1);
							stmt1.setString(1, comp_cd);
						    stmt1.setString(2, counterparty_cd);
						    stmt1.setString(3, address_type[i]);
						    stmt1.setString(4, reg_eff_dt[i]);
						    stmt1.setString(5, address[i]);
						    stmt1.setString(6, city[i]);
						    stmt1.setString(7, pin[i]);
						    stmt1.setString(8, state[i]);
						    stmt1.setString(9, zone[i]);
						    stmt1.setString(10, country[i]);
						    stmt1.setString(11, phone[i]);
						    stmt1.setString(12, cell[i]);
						    stmt1.setString(13, alt_phone[i]);
						    stmt1.setString(14, fax1[i]);
						    stmt1.setString(15, fax2[i]);
						    stmt1.setString(16, email[i]);
						    stmt1.setString(17, emp_cd);
						    stmt1.setString(18, entity_role);
						    stmt1.executeUpdate();
						    
						    stmt1.close();
						}
					}
				}
			}
			
			dbcon.commit();
			url = "../master/frm_entity_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&entity_role="+entity_role+"&counterparty_cd="+counterparty_cd+"&opration="+opration+commonUrl_pra;
		
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			msg = "Error in Exception! - Counterparty Modification Failed!";
			url=CommonVariable.errorpage_url+"?e="+e;
		}
	
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateCounterpartyPlantDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateCounterpartyPlantDetail()";

		msg="";
		msg_type="";
		url="";
		String opration="";
		String counterparty_cd="0";
		String entity_role="0";
		String counterparty_nm="";
		
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		
		try
		{
			opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			entity_role = request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");
			
			String plant_eff_dt = request.getParameter("plant_eff_dt")==null?"":request.getParameter("plant_eff_dt");
			counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
			if(entity_role.equals("G"))
			{
				counterparty_nm=utilBean.getGasExchangeName(dbcon,counterparty_cd);
			}
			else if(entity_role.equals("B"))
			{
				counterparty_nm=utilBean.getCompanyName(dbcon,comp_cd);
			}
			else
			{
				counterparty_nm=utilBean.getCounterpartyName(dbcon,counterparty_cd);
			}
			String plant_name = request.getParameter("plant_name")==null?"":request.getParameter("plant_name");
			String plant_abbr = request.getParameter("plant_abbr")==null?"":request.getParameter("plant_abbr");
			String status="Y";
			
			String plant_addr = request.getParameter("plant_addr")==null?"":request.getParameter("plant_addr");
			String plant_city = request.getParameter("plant_city")==null?"":request.getParameter("plant_city");
			String plant_state = request.getParameter("plant_state")==null?"":request.getParameter("plant_state");
			String new_state_nm = request.getParameter("new_state_nm")==null?"":request.getParameter("new_state_nm");
			String plant_zone = request.getParameter("plant_zone")==null?"":request.getParameter("plant_zone");
			String plant_pin = request.getParameter("plant_pin")==null?"":request.getParameter("plant_pin");
			String plant_sector = request.getParameter("plant_sector")==null?"":request.getParameter("plant_sector");
			
			String seq_no = request.getParameter("plant_seq_no")==null?"1":request.getParameter("plant_seq_no");
			
			String[] stat_cd = request.getParameterValues("stat_cd");
			String[] stat_no = request.getParameterValues("stat_no");
			String[] stat_eff_dt = request.getParameterValues("stat_eff_dt");
			String[] stat_remark = request.getParameterValues("stat_remark");
			
			if((!counterparty_cd.equals("") && !counterparty_cd.equals("0")) && !plant_eff_dt.equals("") && (!entity_role.equals("") && !entity_role.equals("0")))
			{
				if(plant_state.equals("other"))
				{
					plant_state=new_state_nm;
				}
				if(opration.equalsIgnoreCase("INSERT"))
				{
					seq_no = "1";
					String query="SELECT MAX(SEQ_NO) FROM FMS_COUNTERPARTY_PLANT_DTL "
							+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, counterparty_cd);
					stmt.setString(2, entity_role);
					stmt.setString(3, comp_cd);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						seq_no=""+(rset.getInt(1)+1);
					}
					rset.close();
					stmt.close();
					
					query1="INSERT INTO FMS_COUNTERPARTY_PLANT_DTL(COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,"
							+ "PLANT_STATE,PLANT_ZONE,PLANT_CITY,PLANT_PIN,PLANT_SECTOR,STATUS,ENT_DT,ENT_BY) "
							+ "VALUES(?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
							+ "?,?,?,?,?,?,?,?,SYSDATE,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, entity_role);
					stmt1.setString(4, seq_no);
					stmt1.setString(5, plant_eff_dt);
					stmt1.setString(6, plant_name);
					stmt1.setString(7, plant_abbr);
					stmt1.setString(8, plant_addr);
					stmt1.setString(9, plant_state);
					stmt1.setString(10, plant_zone);
					stmt1.setString(11, plant_city);
					stmt1.setString(12, plant_pin);
					stmt1.setString(13, plant_sector);
					stmt1.setString(14, status);
					stmt1.setString(15, emp_cd);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					msg = "Successful! - "+counterparty_nm+" Plant Added!";
					msg_type="S";
					
					if(entity_role.equals("R"))
					{
						int meter_count=0;
						String query2="SELECT COUNT(*) "
								+ "FROM FMS_METER_MST A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND PLANT_SEQ=? AND METER_TYPE=? "
								+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_METER_MST B "
								+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.PLANT_SEQ=B.PLANT_SEQ "
								+ "AND A.METER_TYPE=B.METER_TYPE AND A.METER_SEQ=B.METER_SEQ)";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, seq_no);
						stmt2.setString(4, entity_role);
						rset2=stmt2.executeQuery();
						if(rset2.next())
						{
							meter_count=rset2.getInt(1);
						}
						rset2.close();
						stmt2.close();
						
						if(meter_count==0)
						{
							String meter_seq="1";
							String query3="SELECT MAX(METER_SEQ) "
									+ "FROM FMS_METER_MST A "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND PLANT_SEQ=? AND METER_TYPE=? "
									+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_METER_MST B "
									+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.PLANT_SEQ=B.PLANT_SEQ "
									+ "AND A.METER_TYPE=B.METER_TYPE AND A.METER_SEQ=B.METER_SEQ)";
							stmt3=dbcon.prepareStatement(query3);
							stmt3.setString(1, comp_cd);
							stmt3.setString(2, counterparty_cd);
							stmt3.setString(3, seq_no);
							stmt3.setString(4, entity_role);
							rset3=stmt3.executeQuery();
							if(rset3.next())
							{
								meter_seq=""+(rset3.getInt(1)+1);
							}
							else
							{
								meter_seq="1";
							}
							rset3.close();
							stmt3.close();
							
							String trans_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
							String meter_id=trans_abbr+"-P"+seq_no+"-M"+meter_seq;
							query4="INSERT INTO FMS_METER_MST(COMPANY_CD,COUNTERPARTY_CD,PLANT_SEQ,METER_TYPE,METER_SEQ,"
									+ "METER_ID,STATUS,ENT_BY,ENT_DT,EFF_DT) "
									+ "VALUES(?,?,?,?,?,"
									+ "?,?,?,SYSDATE,TO_DATE(?,'DD/MM/YYYY'))";
							stmt4=dbcon.prepareStatement(query4);
							stmt4.setString(1, comp_cd);
							stmt4.setString(2, counterparty_cd);
							stmt4.setString(3, seq_no);
							stmt4.setString(4, entity_role);
							stmt4.setString(5, meter_seq);
							stmt4.setString(6, meter_id);
							stmt4.setString(7, "Y");
							stmt4.setString(8, emp_cd);
							stmt4.setString(9, plant_eff_dt);
							stmt4.executeUpdate();
							
							stmt4.close();
						}
					}
				}
				else
				{
					int count=0;
					String query="SELECT COUNT(*) FROM FMS_COUNTERPARTY_PLANT_DTL "
							+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND SEQ_NO=? "
							+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND COMPANY_CD=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, counterparty_cd);
					stmt.setString(2, entity_role);
					stmt.setString(3, seq_no);
					stmt.setString(4, plant_eff_dt);
					stmt.setString(5, comp_cd);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						count=rset.getInt(1);
					}
					rset.close();
					stmt.close();
					
					if(count == 0)
					{
						query1="INSERT INTO FMS_COUNTERPARTY_PLANT_DTL(COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,"
								+ "PLANT_STATE,PLANT_ZONE,PLANT_CITY,PLANT_PIN,PLANT_SECTOR,STATUS,ENT_DT,ENT_BY) "
								+ "VALUES(?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
								+ "?,?,?,?,?,?,?,?,SYSDATE,?)";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, entity_role);
						stmt1.setString(4, seq_no);
						stmt1.setString(5, plant_eff_dt);
						stmt1.setString(6, plant_name);
						stmt1.setString(7, plant_abbr);
						stmt1.setString(8, plant_addr);
						stmt1.setString(9, plant_state);
						stmt1.setString(10, plant_zone);
						stmt1.setString(11, plant_city);
						stmt1.setString(12, plant_pin);
						stmt1.setString(13, plant_sector);
						stmt1.setString(14, status);
						stmt1.setString(15, emp_cd);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					else
					{
						query="UPDATE FMS_COUNTERPARTY_PLANT_DTL SET PLANT_NAME=?,PLANT_ABBR=?,PLANT_ADDR=?, "
								+ "PLANT_STATE=?,PLANT_ZONE=?,PLANT_CITY=?,PLANT_PIN=?, "
								+ "PLANT_SECTOR=?,STATUS=?,MODIFY_DT=SYSDATE,MODIFY_BY=? "
								+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND SEQ_NO=? "
								+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND COMPANY_CD=?";
						stmt1=dbcon.prepareStatement(query);
						stmt1.setString(1, plant_name);
						stmt1.setString(2, plant_abbr);
						stmt1.setString(3, plant_addr);
						stmt1.setString(4, plant_state);
						stmt1.setString(5, plant_zone);
						stmt1.setString(6, plant_city);
						stmt1.setString(7, plant_pin);
						stmt1.setString(8, plant_sector);
						stmt1.setString(9, status);
						stmt1.setString(10, emp_cd);
						stmt1.setString(11, counterparty_cd);
						stmt1.setString(12, entity_role);
						stmt1.setString(13, seq_no);
						stmt1.setString(14, plant_eff_dt);
						stmt1.setString(15, comp_cd);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					
					msg = "Successful! - "+counterparty_nm+" Plant updated!";
					msg_type="S";
				}
				
				if(stat_cd!=null)
				{
					for(int i=0;i<stat_cd.length;i++)
					{
						query="DELETE FROM FMS_COUNTERPARTY_PLANT_TAX "
								+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND PLANT_SEQ_NO=? "
								+ "AND STAT_CD=? AND COMPANY_CD=?";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, counterparty_cd);
						stmt.setString(2, entity_role);
						stmt.setString(3, seq_no);
						stmt.setString(4, stat_cd[i]);
						stmt.setString(5, comp_cd);
						stmt.executeUpdate();
						
						stmt.close();
						
						query1="INSERT INTO FMS_COUNTERPARTY_PLANT_TAX(COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY) "
								+ "VALUES(?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,SYSDATE,?)";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, entity_role);
						stmt1.setString(4, seq_no);
						stmt1.setString(5, stat_cd[i]);
						stmt1.setString(6, stat_no[i]);
						stmt1.setString(7, stat_eff_dt[i]);
						stmt1.setString(8, "Y");
						stmt1.setString(9, stat_remark[i]);
						stmt1.setString(10, emp_cd);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
				}
				dbcon.commit();
			}
			
			opration="MODIFY";
			url = "../master/frm_entity_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&entity_role="+entity_role+"&counterparty_cd="+counterparty_cd+"&opration="+opration+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			msg = "Error in Exception! - "+counterparty_nm+" Plant Addition/Modification Failed!";
			url=CommonVariable.errorpage_url+"?e="+e;
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateCounterpartyBuDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateCounterpartyBuDetail()";

		msg="";
		msg_type="";
		url="";
		String opration="";
		String counterparty_cd="0";
		String counterparty_nm="";
		String entity_role="0";
		String entity_roleNm="";
		
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		
		try
		{
			opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			entity_role = request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");
			
			String bu_eff_dt = request.getParameter("bu_eff_dt")==null?"":request.getParameter("bu_eff_dt");
			counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
			if(entity_role.equals("G"))
			{
				counterparty_nm=utilBean.getGasExchangeName(dbcon,counterparty_cd);
				entity_roleNm = "Gas Exchange";
			}
			else if(entity_role.equals("B"))
			{
				counterparty_nm=utilBean.getCompanyName(dbcon,comp_cd);
				entity_roleNm = "Business Owner";
			}
			else
			{
				counterparty_nm=utilBean.getCounterpartyName(dbcon,counterparty_cd);
				if(entity_role.equals("C")) 
				{
					entity_roleNm = "Customer";
				}
				else if (entity_role.equals("T"))
				{
					entity_roleNm = "Trader";
				}
				else if (entity_role.equals("R"))
				{
					entity_roleNm = "Transporter";
				}
				else if (entity_role.equals("V"))
				{
					entity_roleNm = "Vessel Agent";
				}
				else if (entity_role.equals("H"))
				{
					entity_roleNm = "Custom House Agent";
				}
				else if (entity_role.equals("S"))
				{
					entity_roleNm = "Surveyor";
				}
			}
			String bu_name = request.getParameter("bu_name")==null?"":request.getParameter("bu_name");
			String bu_abbr = request.getParameter("bu_abbr")==null?"":request.getParameter("bu_abbr");
			String status="Y";
			
			String bu_addr = request.getParameter("bu_addr")==null?"":request.getParameter("bu_addr");
			String bu_city = request.getParameter("bu_city")==null?"":request.getParameter("bu_city");
			String bu_state = request.getParameter("bu_state")==null?"":request.getParameter("bu_state");
			String bu_zone = request.getParameter("bu_zone")==null?"":request.getParameter("bu_zone");
			String bu_pin = request.getParameter("bu_pin")==null?"":request.getParameter("bu_pin");
			
			String seq_no = request.getParameter("bu_seq_no")==null?"1":request.getParameter("bu_seq_no");
			
			String[] bu_stat_cd = request.getParameterValues("bu_stat_cd");
			String[] bu_stat_no = request.getParameterValues("bu_stat_no");
			String[] bu_stat_eff_dt = request.getParameterValues("bu_stat_eff_dt");
			String[] bu_stat_remark = request.getParameterValues("bu_stat_remark");
			
			if((!counterparty_cd.equals("") && !counterparty_cd.equals("0")) && !bu_eff_dt.equals("") && (!entity_role.equals("") && !entity_role.equals("0")))
			{
				if(opration.equalsIgnoreCase("INSERT"))
				{
					seq_no = "1";
					String query="SELECT MAX(SEQ_NO) FROM FMS_COUNTERPARTY_BU_DTL "
							+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, counterparty_cd);
					stmt.setString(2, entity_role);
					stmt.setString(3, comp_cd);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						seq_no=""+(rset.getInt(1)+1);
					}
					rset.close();
					stmt.close();
					
					query1="INSERT INTO FMS_COUNTERPARTY_BU_DTL(COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,"
							+ "PLANT_STATE,PLANT_ZONE,PLANT_CITY,PLANT_PIN,STATUS,ENT_DT,ENT_BY) "
							+ "VALUES(?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
							+ "?,?,?,?,?,?,?,SYSDATE,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, entity_role);
					stmt1.setString(4, seq_no);
					stmt1.setString(5, bu_eff_dt);
					stmt1.setString(6, bu_name);
					stmt1.setString(7, bu_abbr);
					stmt1.setString(8, bu_addr);
					stmt1.setString(9, bu_state);
					stmt1.setString(10, bu_zone);
					stmt1.setString(11, bu_city);
					stmt1.setString(12, bu_pin);
					stmt1.setString(13, status);
					stmt1.setString(14, emp_cd);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					msg = "Successful! -"+entity_roleNm+" "+counterparty_nm+" Business Unit Added!";
					msg_type="S";
				}
				else
				{
					int count=0;
					String query="SELECT COUNT(*) FROM FMS_COUNTERPARTY_BU_DTL "
							+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND SEQ_NO=? "
							+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND COMPANY_CD=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, counterparty_cd);
					stmt.setString(2, entity_role);
					stmt.setString(3, seq_no);
					stmt.setString(4, bu_eff_dt);
					stmt.setString(5, comp_cd);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						count=rset.getInt(1);
					}
					rset.close();
					stmt.close();
					
					if(count == 0)
					{
						query="INSERT INTO FMS_COUNTERPARTY_BU_DTL(COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,"
								+ "PLANT_STATE,PLANT_ZONE,PLANT_CITY,PLANT_PIN,STATUS,ENT_DT,ENT_BY) "
								+ "VALUES(?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
								+ "?,?,?,?,?,?,?,SYSDATE,?)";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, entity_role);
						stmt1.setString(4, seq_no);
						stmt1.setString(5, bu_eff_dt);
						stmt1.setString(6, bu_name);
						stmt1.setString(7, bu_abbr);
						stmt1.setString(8, bu_addr);
						stmt1.setString(9, bu_state);
						stmt1.setString(10, bu_zone);
						stmt1.setString(11, bu_city);
						stmt1.setString(12, bu_pin);
						stmt1.setString(13, status);
						stmt1.setString(14, emp_cd);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					else
					{
						query="UPDATE FMS_COUNTERPARTY_BU_DTL SET PLANT_NAME=?,PLANT_ABBR=?,PLANT_ADDR=?, "
								+ "PLANT_STATE=?,PLANT_ZONE=?,PLANT_CITY=?,PLANT_PIN=?, "
								+ "STATUS=?,MODIFY_DT=SYSDATE,MODIFY_BY=? "
								+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND SEQ_NO=? "
								+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND COMPANY_CD=?";
						stmt1=dbcon.prepareStatement(query);
						stmt1.setString(1, bu_name);
						stmt1.setString(2, bu_abbr);
						stmt1.setString(3, bu_addr);
						stmt1.setString(4, bu_state);
						stmt1.setString(5, bu_zone);
						stmt1.setString(6, bu_city);
						stmt1.setString(7, bu_pin);
						stmt1.setString(8, status);
						stmt1.setString(9, emp_cd);
						stmt1.setString(10, counterparty_cd);
						stmt1.setString(11, entity_role);
						stmt1.setString(12, seq_no);
						stmt1.setString(13, bu_eff_dt);
						stmt1.setString(14, comp_cd);
						
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					
					msg = "Successful! - "+entity_roleNm+" "+counterparty_nm+" Business Unit Updated!";
					msg_type="S";
				}
				
				if(bu_stat_cd!=null)
				{
					for(int i=0;i<bu_stat_cd.length;i++)
					{
						query="DELETE FROM FMS_COUNTERPARTY_BU_TAX "
								+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND PLANT_SEQ_NO=? "
								+ "AND STAT_CD=? AND COMPANY_CD=?";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, counterparty_cd);
						stmt.setString(2, entity_role);
						stmt.setString(3, seq_no);
						stmt.setString(4, bu_stat_cd[i]);
						stmt.setString(5, comp_cd);
						stmt.executeUpdate();
						
						stmt.close();
						
						query1="INSERT INTO FMS_COUNTERPARTY_BU_TAX(COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY) "
								+ "VALUES(?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,SYSDATE,?)";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, entity_role);
						stmt1.setString(4, seq_no);
						stmt1.setString(5, bu_stat_cd[i]);
						stmt1.setString(6, bu_stat_no[i]);
						stmt1.setString(7, bu_stat_eff_dt[i]);
						stmt1.setString(8, "Y");
						stmt1.setString(9, bu_stat_remark[i]);
						stmt1.setString(10, emp_cd);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
				}
				dbcon.commit();
			}
			
			opration="MODIFY";
			url = "../master/frm_entity_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&entity_role="+entity_role+"&counterparty_cd="+counterparty_cd+"&opration="+opration+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			msg = "Error in Exception! - "+entity_roleNm+" "+counterparty_nm+" Business Unit Addition/Modification Failed!";
			url=CommonVariable.errorpage_url+"?e="+e;
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateCounterpartyBankDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateCounterpartyBankDetail()";

		msg="";
		msg_type="";
		url="";
		String opration="";
		String counterparty_cd="0";
		String counterparty_nm="";
		String entity_role="0";
		String entity_roleNm="";
		
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		
		try
		{
			opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			entity_role = request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");
			
			String bank_category = request.getParameter("bank_category")==null?"":request.getParameter("bank_category");
			String bank_eff_dt = request.getParameter("bank_eff_dt")==null?"":request.getParameter("bank_eff_dt");
			counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
			if(entity_role.equals("G"))
			{
				counterparty_nm=utilBean.getGasExchangeName(dbcon,counterparty_cd);
				entity_roleNm = "Gas Exchange";
			}
			else if(entity_role.equals("B"))
			{
				counterparty_nm=utilBean.getCompanyName(dbcon,comp_cd);
				entity_roleNm = "Business Owner";
			}
			else
			{
				counterparty_nm=utilBean.getCounterpartyName(dbcon,counterparty_cd);
				if(entity_role.equals("C")) 
				{
					entity_roleNm = "Customer";
				}
				else if (entity_role.equals("T"))
				{
					entity_roleNm = "Trader";
				}
				else if (entity_role.equals("R"))
				{
					entity_roleNm = "Transporter";
				}
				else if (entity_role.equals("V"))
				{
					entity_roleNm = "Vessel Agent";
				}
				else if (entity_role.equals("H"))
				{
					entity_roleNm = "Custom House Agent";
				}
				else if (entity_role.equals("S"))
				{
					entity_roleNm = "Surveyor";
				}
			}
						
			String bank_name = request.getParameter("bank_name")==null?"":request.getParameter("bank_name");
			String account_no = request.getParameter("account_no")==null?"":request.getParameter("account_no");
			
			String bank_branch = request.getParameter("bank_branch")==null?"":request.getParameter("bank_branch");
			String ifsc_code = request.getParameter("ifsc_code")==null?"":request.getParameter("ifsc_code");
			String bank_state = request.getParameter("bank_state")==null?"":request.getParameter("bank_state");
			
			if((!counterparty_cd.equals("") && !counterparty_cd.equals("0")) && !bank_eff_dt.equals("") && (!entity_role.equals("") && !entity_role.equals("0")))
			{
				int count=0;
				String query="SELECT COUNT(*) "
						+ "FROM FMS_ENTITY_BANK_MST "
						+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? "
						+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND COMPANY_CD=? AND CATEGORY=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, counterparty_cd);
				stmt.setString(2, entity_role);
				stmt.setString(3, bank_eff_dt);
				stmt.setString(4, comp_cd);
				stmt.setString(5, bank_category);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					count=rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count==0)
				{
					query1="INSERT INTO FMS_ENTITY_BANK_MST(COMPANY_CD,COUNTERPARTY_CD,ENTITY,EFF_DT,BANK_NAME,"
							+ "ACCOUNT_ID,IFSC_CODE,BRANCH_NAME,STATE_NM,ENT_DT,ENT_BY,CATEGORY) "
							+ "VALUES(?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
							+ "?,?,?,?,SYSDATE,?,?)";
					stmt=dbcon.prepareStatement(query1);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, entity_role);
					stmt.setString(4, bank_eff_dt);
					stmt.setString(5, bank_name);
					stmt.setString(6, account_no);
					stmt.setString(7, ifsc_code);
					stmt.setString(8, bank_branch);
					stmt.setString(9, bank_state);
					stmt.setString(10, emp_cd);
					stmt.setString(11, bank_category);
					stmt.executeUpdate();
					
					stmt.close();
				}
				else
				{
					query="UPDATE FMS_ENTITY_BANK_MST SET BANK_NAME=?,ACCOUNT_ID=?,IFSC_CODE=?, "
							+ "BRANCH_NAME=?,STATE_NM=?,MODIFY_DT=SYSDATE,MODIFY_BY=? "
							+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? "
							+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND COMPANY_CD=? AND CATEGORY=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, bank_name);
					stmt.setString(2, account_no);
					stmt.setString(3, ifsc_code);
					stmt.setString(4, bank_branch);
					stmt.setString(5, bank_state);
					stmt.setString(6, emp_cd);
					stmt.setString(7, counterparty_cd);
					stmt.setString(8, entity_role);
					stmt.setString(9, bank_eff_dt);
					stmt.setString(10, comp_cd);
					stmt.setString(11, bank_category);
					stmt.executeUpdate();
					
					stmt.close();
				}
				
				if(opration.equals("INSERT"))
				{
					msg = "Successful! - "+entity_roleNm+" "+counterparty_nm+" Bank Detail Added!";
					msg_type="S";
				}
				else
				{
					msg = "Successful! - "+entity_roleNm+" "+counterparty_nm+" Bank Detail Modified!";
					msg_type="S";
				}
					
				dbcon.commit();
			}
			
			opration="MODIFY";
			if(entity_role.equals("B")) 
			{
				url = "../master/frm_entity_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&entity_role="+entity_role+"&counterparty_cd="+counterparty_cd+"&opration="+opration+commonUrl_pra;
			}
			else 
			{
				url = "../master/frm_entity_bank_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&entity_role="+entity_role+"&counterparty_cd="+counterparty_cd+"&opration="+opration+commonUrl_pra;
			}
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			msg = "Error in Exception! - "+entity_roleNm+" "+counterparty_nm+" Bank Detail Addition/Modification Failed!";
			url=CommonVariable.errorpage_url+"?e="+e;
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateEntityContactDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateEntityContactDetail()";

		msg="";
		msg_type="";
		url="";
		String opration="";
		String counterparty_cd="0";
		String counterparty_nm = "";
		String entity_role="0";
		String entity_roleNm ="";
		
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		
		try
		{
			opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			entity_role = request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");
			counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
			
			if(entity_role.equals("G"))
			{
				counterparty_nm=utilBean.getGasExchangeName(dbcon,counterparty_cd);
				entity_roleNm = "Gas Exchange";
			}
			else if(entity_role.equals("B"))
			{
				counterparty_nm=utilBean.getCompanyName(dbcon,comp_cd);
				entity_roleNm = "Business Owner";
			}
			else
			{
				counterparty_nm=utilBean.getCounterpartyName(dbcon,counterparty_cd);
				if(entity_role.equals("C")) 
				{
					entity_roleNm = "Customer";
				}
				else if (entity_role.equals("T"))
				{
					entity_roleNm = "Trader";
				}
				else if (entity_role.equals("R"))
				{
					entity_roleNm = "Transporter";
				}
				else if (entity_role.equals("V"))
				{
					entity_roleNm = "Vessel Agent";
				}
				else if (entity_role.equals("H"))
				{
					entity_roleNm = "Custom House Agent";
				}
				else if (entity_role.equals("S"))
				{
					entity_roleNm = "Surveyor";
				}
			}
			
			String person_name = request.getParameter("person_name")==null?"":request.getParameter("person_name");
			String seq_no = request.getParameter("seq_no")==null?"":request.getParameter("seq_no");
			String eff_dt = request.getParameter("eff_dt")==null?"":request.getParameter("eff_dt");
			String designation = request.getParameter("designation")==null?"":request.getParameter("designation");
			String status = request.getParameter("status")==null?"":request.getParameter("status");
			String mobile = request.getParameter("mobile")==null?"":request.getParameter("mobile");
			String email = request.getParameter("email")==null?"":request.getParameter("email");
			
			String nom = request.getParameter("nom")==null?"N":request.getParameter("nom");
			String inv = request.getParameter("inv")==null?"N":request.getParameter("inv");
			String jt = request.getParameter("jt")==null?"N":request.getParameter("jt");
			String fm = request.getParameter("fm")==null?"N":request.getParameter("fm");
			String pm = request.getParameter("pm")==null?"N":request.getParameter("pm");
			String other = request.getParameter("other")==null?"N":request.getParameter("other");
			String rm = request.getParameter("rm")==null?"N":request.getParameter("rm");
			
			String dlng_nom = request.getParameter("dlng_nom")==null?"N":request.getParameter("dlng_nom");
			String dlng_inv = request.getParameter("dlng_inv")==null?"N":request.getParameter("dlng_inv");
			String dlng_jt = request.getParameter("dlng_jt")==null?"N":request.getParameter("dlng_jt");
			String dlng_fm = request.getParameter("dlng_fm")==null?"N":request.getParameter("dlng_fm");
			String dlng_pm = request.getParameter("dlng_pm")==null?"N":request.getParameter("dlng_pm");
			String dlng_other = request.getParameter("dlng_other")==null?"N":request.getParameter("dlng_other");
			String dlng_rm = request.getParameter("dlng_rm")==null?"N":request.getParameter("dlng_rm");
			String dlng_f402 = request.getParameter("dlng_f402")==null?"N":request.getParameter("dlng_f402");
			
			String[] address_type = request.getParameterValues("address_type");
			String[] additional_address = request.getParameterValues("additional_address");
			String[] phone = request.getParameterValues("phone");
			String[] fax1 = request.getParameterValues("fax1");
			String[] fax2 = request.getParameterValues("fax2");
			String[] flag = request.getParameterValues("flag");
			
			String[] to_nom = request.getParameterValues("to_nom");
			String[] to_inv = request.getParameterValues("to_inv");
			String[] to_jt = request.getParameterValues("to_jt");
			String[] to_fm = request.getParameterValues("to_fm");
			String[] to_pm = request.getParameterValues("to_pm");
			String[] to_other = request.getParameterValues("to_other");
			String[] to_rm = request.getParameterValues("to_rm");
			
			String[] dlng_to_nom = request.getParameterValues("dlng_to_nom");
			String[] dlng_to_inv = request.getParameterValues("dlng_to_inv");
			String[] dlng_to_jt = request.getParameterValues("dlng_to_jt");
			String[] dlng_to_fm = request.getParameterValues("dlng_to_fm");
			String[] dlng_to_pm = request.getParameterValues("dlng_to_pm");
			String[] dlng_to_other = request.getParameterValues("dlng_to_other");
			String[] dlng_to_rm = request.getParameterValues("dlng_to_rm");	
			String[] dlng_to_f402 = request.getParameterValues("dlng_to_f402");	
						
			if(opration.equals("INSERT"))
			{
				String query = "SELECT NVL(MAX(SEQ_NO),0) FROM FMS_ENTITY_CONTACT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND ENTITY=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, entity_role);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					seq_no = ""+(rset.getInt(1)+1);
				}
				else
				{
					seq_no = "1";
				}
				rset.close();
				stmt.close();
			}
			
			if(!seq_no.equals("") && !seq_no.equals("0"))
			{
				if(address_type!=null)
				{
					for(int i=0;i<address_type.length;i++)
					{
						int rlng_count =0;
						String query = "SELECT COUNT(*) FROM FMS_ENTITY_CONTACT_MST "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND ENTITY=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND SEQ_NO=? AND ADDR_FLAG=? AND TYPE=?";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, counterparty_cd);
						stmt.setString(3, entity_role);
						stmt.setString(4, eff_dt);
						stmt.setString(5, seq_no);
						stmt.setString(6, address_type[i]);
						stmt.setString(7, "RLNG");
						rset=stmt.executeQuery();
						if(rset.next())
						{
							rlng_count=rset.getInt(1);
						}
						rset.close();
						stmt.close();
						
						if(rlng_count > 0)
						{
							query1 = "UPDATE FMS_ENTITY_CONTACT_MST SET CONTACT_PERSON=?, DESIGNATION=?, PHONE=?, MOBILE=?,"
									+ "FAX_1=?, FAX_2=?, EMAIL=?, ADDL_ADDR_LINE=?, "
									+ "NOM_FLAG=?, INV_FLAG=?, FM_FLAG=?, PM_FLAG=?, JT_FLAG=?, OTHER_FLAG=?,RM_FLAG=?, "
									+ "ACTIVE_FLAG=?, MODIFY_DT=SYSDATE, MODIFY_BY=?,ADDR_IS_ACTIVE=?,"
									+ "TO_NOM=?,TO_FM=?,TO_INV=?,TO_JT=?,TO_PM=?,"
									+ "TO_OTHER=?,TO_RM=? "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
									+ "AND ENTITY=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND ADDR_FLAG=? AND TYPE=?";
							stmt1=dbcon.prepareStatement(query1);
							stmt1.setString(1, person_name);
							stmt1.setString(2, designation);
							stmt1.setString(3, phone[i]);
							stmt1.setString(4, mobile);
							stmt1.setString(5, fax1[i]);
							stmt1.setString(6, fax2[i]);
							stmt1.setString(7, email);
							stmt1.setString(8, additional_address[i]);
							stmt1.setString(9, nom);
							stmt1.setString(10, inv);
							stmt1.setString(11, fm);
							stmt1.setString(12, pm);
							stmt1.setString(13, jt);
							stmt1.setString(14, other);
							stmt1.setString(15, rm);
							stmt1.setString(16, status);
							stmt1.setString(17, emp_cd);
							stmt1.setString(18, flag[i]);
							stmt1.setString(19, to_nom[i]);
							stmt1.setString(20, to_fm[i]);
							stmt1.setString(21, to_inv[i]);
							stmt1.setString(22, to_jt[i]);
							stmt1.setString(23, to_pm[i]);
							stmt1.setString(24, to_other[i]);
							stmt1.setString(25, to_rm[i]);
							stmt1.setString(26, comp_cd);
							stmt1.setString(27, counterparty_cd);
							stmt1.setString(28, seq_no);
							stmt1.setString(29, entity_role);
							stmt1.setString(30, eff_dt);
							stmt1.setString(31, address_type[i]);
							stmt1.setString(32, "RLNG");
							stmt1.executeUpdate();
							
							stmt1.close();
						}
						else
						{
							query1 = "INSERT INTO FMS_ENTITY_CONTACT_MST(COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,CONTACT_PERSON,"
									+ "DESIGNATION,PHONE,MOBILE,FAX_1,FAX_2,EMAIL,ADDR_FLAG,ADDL_ADDR_LINE,"
									+ "NOM_FLAG,INV_FLAG,FM_FLAG,PM_FLAG,JT_FLAG,OTHER_FLAG,RM_FLAG,ACTIVE_FLAG,ENT_DT,ENT_BY,ADDR_IS_ACTIVE,"
									+ "TO_NOM,TO_FM,TO_INV,TO_JT,TO_PM,TO_OTHER,TO_RM,TYPE) "
									+ "VALUES(?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
									+ "?,?,?,?,?,?,?,?,"
									+ "?,?,?,?,?,?,?,?,SYSDATE,?,?,"
									+ "?,?,?,?,?,?,?,?)";
							stmt1=dbcon.prepareStatement(query1);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, counterparty_cd);
							stmt1.setString(3, entity_role);
							stmt1.setString(4, seq_no);
							stmt1.setString(5, eff_dt);
							stmt1.setString(6, person_name);
							stmt1.setString(7, designation);
							stmt1.setString(8, phone[i]);
							stmt1.setString(9, mobile);
							stmt1.setString(10, fax1[i]);
							stmt1.setString(11, fax2[i]);
							stmt1.setString(12, email);
							stmt1.setString(13, address_type[i]);
							stmt1.setString(14, additional_address[i]);
							stmt1.setString(15, nom);
							stmt1.setString(16, inv);
							stmt1.setString(17, fm);
							stmt1.setString(18, pm);
							stmt1.setString(19, jt);
							stmt1.setString(20, other);
							stmt1.setString(21, rm);
							stmt1.setString(22, status);
							stmt1.setString(23, emp_cd);
							stmt1.setString(24, flag[i]);
							stmt1.setString(25, to_nom[i]);
							stmt1.setString(26, to_fm[i]);
							stmt1.setString(27, to_inv[i]);
							stmt1.setString(28, to_jt[i]);
							stmt1.setString(29, to_pm[i]);
							stmt1.setString(30, to_other[i]);
							stmt1.setString(31, to_rm[i]);
							stmt1.setString(32, "RLNG");
							stmt1.executeUpdate();
							
							stmt1.close();
						}
						
						if(entity_role.equals("C") || entity_role.equals("B") || entity_role.equals("T") || entity_role.equals("G"))
						{
							int dlng_count =0;
							String query1 = "SELECT COUNT(*) FROM FMS_ENTITY_CONTACT_MST "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND ENTITY=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND SEQ_NO=? AND ADDR_FLAG=? AND TYPE=?";
							stmt=dbcon.prepareStatement(query1);
							stmt.setString(1, comp_cd);
							stmt.setString(2, counterparty_cd);
							stmt.setString(3, entity_role);
							stmt.setString(4, eff_dt);
							stmt.setString(5, seq_no);
							stmt.setString(6, address_type[i]);
							stmt.setString(7, "DLNG");
							rset=stmt.executeQuery();
							if(rset.next())
							{
								dlng_count=rset.getInt(1);
							}
							rset.close();
							stmt.close();
							
							if(dlng_count > 0)
							{
								int upCnt=0;
								
								query1 = "UPDATE FMS_ENTITY_CONTACT_MST SET CONTACT_PERSON=?, DESIGNATION=?, PHONE=?, MOBILE=?,"
										+ "FAX_1=?, FAX_2=?, EMAIL=?, ADDL_ADDR_LINE=?, "
										+ "NOM_FLAG=?, INV_FLAG=?, FM_FLAG=?, PM_FLAG=?, JT_FLAG=?, OTHER_FLAG=?,RM_FLAG=?, "
										+ "ACTIVE_FLAG=?, MODIFY_DT=SYSDATE, MODIFY_BY=?,ADDR_IS_ACTIVE=?,"
										+ "TO_NOM=?,TO_FM=?,TO_INV=?,TO_JT=?,TO_PM=?,"
										+ "TO_OTHER=?,TO_RM=?,F402_FLAG=?,TO_F402=? "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
										+ "AND ENTITY=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND ADDR_FLAG=? AND TYPE=?";
								stmt1=dbcon.prepareStatement(query1);
								stmt1.setString(++upCnt, person_name);
								stmt1.setString(++upCnt, designation);
								stmt1.setString(++upCnt, phone[i]);
								stmt1.setString(++upCnt, mobile);
								stmt1.setString(++upCnt, fax1[i]);
								stmt1.setString(++upCnt, fax2[i]);
								stmt1.setString(++upCnt, email);
								stmt1.setString(++upCnt, additional_address[i]);
								stmt1.setString(++upCnt, dlng_nom);
								stmt1.setString(++upCnt, dlng_inv);
								stmt1.setString(++upCnt, dlng_fm);
								stmt1.setString(++upCnt, dlng_pm);
								stmt1.setString(++upCnt, dlng_jt);
								stmt1.setString(++upCnt, dlng_other);
								stmt1.setString(++upCnt, dlng_rm);
								stmt1.setString(++upCnt, status);
								stmt1.setString(++upCnt, emp_cd);
								stmt1.setString(++upCnt, flag[i]);
								stmt1.setString(++upCnt, dlng_to_nom[i]);
								stmt1.setString(++upCnt, dlng_to_fm[i]);
								stmt1.setString(++upCnt, dlng_to_inv[i]);
								stmt1.setString(++upCnt, dlng_to_jt[i]);
								stmt1.setString(++upCnt, dlng_to_pm[i]);
								stmt1.setString(++upCnt, dlng_to_other[i]);
								stmt1.setString(++upCnt, dlng_to_rm[i]);
								stmt1.setString(++upCnt, dlng_f402);
								stmt1.setString(++upCnt, dlng_to_f402[i]);
								stmt1.setString(++upCnt, comp_cd);
								stmt1.setString(++upCnt, counterparty_cd);
								stmt1.setString(++upCnt, seq_no);
								stmt1.setString(++upCnt, entity_role);
								stmt1.setString(++upCnt, eff_dt);
								stmt1.setString(++upCnt, address_type[i]);
								stmt1.setString(++upCnt, "DLNG");
								stmt1.executeUpdate();
								
								stmt1.close();
							}
							else
							{
								Integer insCnt=0;
								
								query1 = "INSERT INTO FMS_ENTITY_CONTACT_MST(COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,CONTACT_PERSON,"
										+ "DESIGNATION,PHONE,MOBILE,FAX_1,FAX_2,EMAIL,ADDR_FLAG,ADDL_ADDR_LINE,"
										+ "NOM_FLAG,INV_FLAG,FM_FLAG,PM_FLAG,JT_FLAG,OTHER_FLAG,RM_FLAG,ACTIVE_FLAG,ENT_DT,ENT_BY,ADDR_IS_ACTIVE,"
										+ "TO_NOM,TO_FM,TO_INV,TO_JT,TO_PM,TO_OTHER,TO_RM,TYPE,F402_FLAG,TO_F402) "
										+ "VALUES(?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
										+ "?,?,?,?,?,?,?,?,"
										+ "?,?,?,?,?,?,?,?,SYSDATE,?,?,"
										+ "?,?,?,?,?,?,?,?,?,?)";
								stmt1=dbcon.prepareStatement(query1);
								stmt1.setString(++insCnt, comp_cd);
								stmt1.setString(++insCnt, counterparty_cd);
								stmt1.setString(++insCnt, entity_role);
								stmt1.setString(++insCnt, seq_no);
								stmt1.setString(++insCnt, eff_dt);
								stmt1.setString(++insCnt, person_name);
								stmt1.setString(++insCnt, designation);
								stmt1.setString(++insCnt, phone[i]);
								stmt1.setString(++insCnt, mobile);
								stmt1.setString(++insCnt, fax1[i]);
								stmt1.setString(++insCnt, fax2[i]);
								stmt1.setString(++insCnt, email);
								stmt1.setString(++insCnt, address_type[i]);
								stmt1.setString(++insCnt, additional_address[i]);
								stmt1.setString(++insCnt, dlng_nom);
								stmt1.setString(++insCnt, dlng_inv);
								stmt1.setString(++insCnt, dlng_fm);
								stmt1.setString(++insCnt, dlng_pm);
								stmt1.setString(++insCnt, dlng_jt);
								stmt1.setString(++insCnt, dlng_other);
								stmt1.setString(++insCnt, dlng_rm);
								stmt1.setString(++insCnt, status);
								stmt1.setString(++insCnt, emp_cd);
								stmt1.setString(++insCnt, flag[i]);
								stmt1.setString(++insCnt, dlng_to_nom[i]);
								stmt1.setString(++insCnt, dlng_to_fm[i]);
								stmt1.setString(++insCnt, dlng_to_inv[i]);
								stmt1.setString(++insCnt, dlng_to_jt[i]);
								stmt1.setString(++insCnt, dlng_to_pm[i]);
								stmt1.setString(++insCnt, dlng_to_other[i]);
								stmt1.setString(++insCnt, dlng_to_rm[i]);
								stmt1.setString(++insCnt, "DLNG");
								stmt1.setString(++insCnt, dlng_f402);
								stmt1.setString(++insCnt, dlng_to_f402[i]);
								stmt1.executeUpdate();
								
								stmt1.close();
							}
						}
						
						if(opration.equals("MODIFY"))
						{
							msg = "Successful! - Contact person for "+entity_roleNm+" "+counterparty_nm+" Updated!";
							msg_type="S";
						}
						else
						{
							msg = "Successful! - Contact person for "+entity_roleNm+" "+counterparty_nm+" Added!";
							msg_type="S";
						}
					}
				}
				else
				{
					if(opration.equalsIgnoreCase("INSERT"))
					{
						msg = "Failed! - Contact person for "+entity_roleNm+" "+counterparty_nm+" Addition Failed!";
					}
					else
					{
						msg = "Failed! - Contact person for "+entity_roleNm+" "+counterparty_nm+" Modification Failed!";
					}
					msg_type="E";
				}
			}
			else
			{
				if(opration.equalsIgnoreCase("INSERT"))
				{
					msg = "Failed! - Entity Contact Addition Failed!";
				}
				else
				{
					msg = "Failed! - Entity Contact Modification Failed!";
				}
				msg_type="E";
			}
			
			opration="INSERT";
			dbcon.commit();
			url = "../master/frm_entity_contact_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&entity_role="+entity_role+"&counterparty_cd="+counterparty_cd+"&opration="+opration+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			msg = "Error in Exception! - Entity Contact Addition/Modification Failed!";
			url=CommonVariable.errorpage_url+"?e="+e;
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateEntityTaxDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateEntityTaxDetail()";

		msg="";
		msg_type="";
		url="";
		String counterparty_cd="0";
		String counterparty_nm = "";
		String entity_role="0";
		String entity_roleNm ="";
		
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		
		try
		{
			entity_role = request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");
			counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
			
			if(entity_role.equals("G"))
			{
				counterparty_nm=utilBean.getGasExchangeName(dbcon,counterparty_cd);
				entity_roleNm = "Gas Exchange";
			}
			else if(entity_role.equals("B"))
			{
				counterparty_nm=utilBean.getCompanyName(dbcon,comp_cd);
				entity_roleNm = "Business Owner";
			}
			else
			{
				counterparty_nm=utilBean.getCounterpartyName(dbcon,counterparty_cd);
				if(entity_role.equals("C")) 
				{
					entity_roleNm = "Customer";
				}
				else if (entity_role.equals("T"))
				{
					entity_roleNm = "Trader";
				}
				else if (entity_role.equals("R"))
				{
					entity_roleNm = "Transporter";
				}
				else if (entity_role.equals("V"))
				{
					entity_roleNm = "Vessel Agent";
				}
				else if (entity_role.equals("H"))
				{
					entity_roleNm = "Custom House Agent";
				}
				else if (entity_role.equals("S"))
				{
					entity_roleNm = "Surveyor";
				}
			}
			
			String type = request.getParameter("type")==null?"":request.getParameter("type");
			String bu_unit = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
			
			String[] seq_no = request.getParameterValues("seq_no");
			String[] plant_nm = request.getParameterValues("plant_nm");
			String[] plant_state = request.getParameterValues("plant_state");
			String[] tax_struct_cd = request.getParameterValues("tax_struct_cd");
			String[] sap_tax_code = request.getParameterValues("sap_tax_code");
			String[] tax_struct_eff_dt = request.getParameterValues("tax_struct_eff_dt");
			String[] tax_struct_nm = request.getParameterValues("tax_struct_nm");
			String[] tax_struct_rmk = request.getParameterValues("tax_struct_rmk");
			String[] eff_dt = request.getParameterValues("eff_dt");
			String[] invoice_type = request.getParameterValues("invoice_type");
			
			String[] ser_seq_no = request.getParameterValues("ser_seq_no");
			String[] ser_plant_nm = request.getParameterValues("ser_plant_nm");
			String[] ser_plant_state = request.getParameterValues("ser_plant_state");
			String[] ser_invoice_type = request.getParameterValues("ser_invoice_type");
			String[] ser_tax_struct_cd = request.getParameterValues("ser_tax_struct_cd");
			String[] ser_sap_tax_code = request.getParameterValues("ser_sap_tax_code");
			String[] ser_tax_struct_eff_dt = request.getParameterValues("ser_tax_struct_eff_dt");
			String[] ser_tax_struct_nm = request.getParameterValues("ser_tax_struct_nm");
			String[] ser_tax_struct_rmk = request.getParameterValues("ser_tax_struct_rmk");
			String[] ser_eff_dt = request.getParameterValues("ser_eff_dt");
			
			String[] bu_ser_seq_no = request.getParameterValues("bu_ser_seq_no");
			String[] bu_ser_plant_nm = request.getParameterValues("bu_ser_plant_nm");
			String[] bu_ser_plant_state = request.getParameterValues("bu_ser_plant_state");
			String[] bu_ser_invoice_type = request.getParameterValues("bu_ser_invoice_type");
			String[] bu_ser_tax_struct_cd = request.getParameterValues("bu_ser_tax_struct_cd");
			String[] bu_ser_sap_tax_code = request.getParameterValues("bu_ser_sap_tax_code");
			String[] bu_ser_tax_struct_eff_dt = request.getParameterValues("bu_ser_tax_struct_eff_dt");
			String[] bu_ser_tax_struct_nm = request.getParameterValues("bu_ser_tax_struct_nm");
			String[] bu_ser_tax_struct_rmk = request.getParameterValues("bu_ser_tax_struct_rmk");
			String[] bu_ser_eff_dt = request.getParameterValues("bu_ser_eff_dt");
			
			if(type.equals("BU_SERVICE"))
			{
				if(bu_ser_seq_no != null)
				{
					for(int i=0; i<bu_ser_seq_no.length; i++)
					{
						if((!bu_ser_invoice_type[i].equals("0") && !bu_ser_invoice_type[i].equals("")) 
								&& (!bu_ser_tax_struct_cd[i].equals("0") && !bu_ser_tax_struct_cd[i].equals("")) 
								&& !bu_ser_tax_struct_eff_dt[i].equals("") && !bu_ser_eff_dt[i].equals("") 
								&& (!counterparty_cd.equals("0") && !counterparty_cd.equals("")) 
								&& (!entity_role.equals("0") && !entity_role.equals("")))
						{
							int count=0;
							String query="SELECT COUNT(*) "
									+ "FROM FMS_ENTITY_BU_SVC_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=? "
									+ "AND PLANT_SEQ_NO=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND INVOICE_TYPE=? AND BU_UNIT=?";
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, comp_cd);
							stmt.setString(2, counterparty_cd);
							stmt.setString(3, entity_role);
							stmt.setString(4, bu_ser_seq_no[i]);
							stmt.setString(5, bu_ser_eff_dt[i]);
							stmt.setString(6, bu_ser_invoice_type[i]);
							stmt.setString(7, bu_unit);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								count=rset.getInt(1);
							}
							rset.close();
							stmt.close();
							
							if(count > 0)
							{
								query1="UPDATE FMS_ENTITY_BU_SVC_TAX_DTL SET TAX_STRUCT_DTL=?,"
										+ "TAX_STRUCT_REMARK=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,FLAG=?,"
										+ "TAX_STRUCT_CD=? "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=? "
										+ "AND PLANT_SEQ_NO=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') "
										+ "AND INVOICE_TYPE=? AND BU_UNIT=?";
								stmt1=dbcon.prepareStatement(query1);
								stmt1.setString(1, bu_ser_tax_struct_nm[i]);
								stmt1.setString(2, bu_ser_tax_struct_rmk[i]);
								stmt1.setString(3, emp_cd);
								stmt1.setString(4, "Y");
								stmt1.setString(5, bu_ser_tax_struct_cd[i]);
								stmt1.setString(6, comp_cd);
								stmt1.setString(7, counterparty_cd);
								stmt1.setString(8, entity_role);
								stmt1.setString(9, bu_ser_seq_no[i]);
								stmt1.setString(10, bu_ser_eff_dt[i]);
								stmt1.setString(11, bu_ser_invoice_type[i]);
								stmt1.setString(12, bu_unit);
								stmt1.executeUpdate();
								
								stmt1.close();
							}
							else
							{
								query1="INSERT INTO FMS_ENTITY_BU_SVC_TAX_DTL(COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,"
										+ "TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,FLAG,INVOICE_TYPE,BU_UNIT,EFF_DT) "
										+ "VALUES(?,?,?,?,?,"
										+ "?,?,SYSDATE,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'))";
								stmt1=dbcon.prepareStatement(query1);
								stmt1.setString(1, comp_cd);
								stmt1.setString(2, counterparty_cd);
								stmt1.setString(3, entity_role);
								stmt1.setString(4, bu_ser_seq_no[i]);
								stmt1.setString(5, bu_ser_tax_struct_cd[i]);
								stmt1.setString(6, bu_ser_tax_struct_nm[i]);
								stmt1.setString(7, bu_ser_tax_struct_rmk[i]);
								stmt1.setString(8, emp_cd);
								stmt1.setString(9, "Y");
								stmt1.setString(10, bu_ser_invoice_type[i]);
								stmt1.setString(11, bu_unit);
								stmt1.setString(12, bu_ser_eff_dt[i]);
								stmt1.executeUpdate();
								
								stmt1.close();
							}
						}
												
						msg = " Successful! - Business Unit Service Tax updated for "+entity_roleNm+" "+counterparty_nm+"!";
						msg_type="S";
					}
				}
			}
			else if(type.equals("SERVICE"))
			{
				if(ser_seq_no != null)
				{
					for(int i=0; i<ser_seq_no.length; i++)
					{
						if((!ser_invoice_type[i].equals("0") && !ser_invoice_type[i].equals("")) 
								&& (!ser_tax_struct_cd[i].equals("0") && !ser_tax_struct_cd[i].equals("")) 
								&& !ser_tax_struct_eff_dt[i].equals("") && !ser_eff_dt[i].equals("") 
								&& (!counterparty_cd.equals("0") && !counterparty_cd.equals("")) 
								&& (!entity_role.equals("0") && !entity_role.equals("")))
						{
							int count=0;
							String query="SELECT COUNT(*) "
									+ "FROM FMS_ENTITY_SERVICE_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=? "
									+ "AND PLANT_SEQ_NO=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND INVOICE_TYPE=? AND BU_UNIT=?";
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, comp_cd);
							stmt.setString(2, counterparty_cd);
							stmt.setString(3, entity_role);
							stmt.setString(4, ser_seq_no[i]);
							stmt.setString(5, ser_eff_dt[i]);
							stmt.setString(6, ser_invoice_type[i]);
							stmt.setString(7, bu_unit);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								count=rset.getInt(1);
							}
							rset.close();
							stmt.close();
							
							if(count > 0)
							{
								query1="UPDATE FMS_ENTITY_SERVICE_TAX_DTL SET TAX_STRUCT_DTL=?,"
										+ "TAX_STRUCT_REMARK=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,FLAG=?,"
										+ "TAX_STRUCT_CD=? "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=? "
										+ "AND PLANT_SEQ_NO=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') "
										+ "AND INVOICE_TYPE=? AND BU_UNIT=?";
								stmt1=dbcon.prepareStatement(query1);
								stmt1.setString(1, ser_tax_struct_nm[i]);
								stmt1.setString(2, ser_tax_struct_rmk[i]);
								stmt1.setString(3, emp_cd);
								stmt1.setString(4, "Y");
								stmt1.setString(5, ser_tax_struct_cd[i]);
								stmt1.setString(6, comp_cd);
								stmt1.setString(7, counterparty_cd);
								stmt1.setString(8, entity_role);
								stmt1.setString(9, ser_seq_no[i]);
								stmt1.setString(10, ser_eff_dt[i]);
								stmt1.setString(11, ser_invoice_type[i]);
								stmt1.setString(12, bu_unit);
								stmt1.executeUpdate();
								
								stmt1.close();
							}
							else
							{
								query1="INSERT INTO FMS_ENTITY_SERVICE_TAX_DTL(COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,"
										+ "TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,FLAG,INVOICE_TYPE,BU_UNIT,EFF_DT) "
										+ "VALUES(?,?,?,?,?,"
										+ "?,?,SYSDATE,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'))";
								stmt1=dbcon.prepareStatement(query1);
								stmt1.setString(1, comp_cd);
								stmt1.setString(2, counterparty_cd);
								stmt1.setString(3, entity_role);
								stmt1.setString(4, ser_seq_no[i]);
								stmt1.setString(5, ser_tax_struct_cd[i]);
								stmt1.setString(6, ser_tax_struct_nm[i]);
								stmt1.setString(7, ser_tax_struct_rmk[i]);
								stmt1.setString(8, emp_cd);
								stmt1.setString(9, "Y");
								stmt1.setString(10, ser_invoice_type[i]);
								stmt1.setString(11, bu_unit);
								stmt1.setString(12, ser_eff_dt[i]);
								stmt1.executeUpdate();
								
								stmt1.close();
							}
						
						}
						
						msg = " Successful! - Service Tax updated for "+entity_roleNm+" "+counterparty_nm+"!";
						msg_type="S";
					}
				}
			}
			else
			{
				if(seq_no != null)
				{
					for(int i=0; i<seq_no.length; i++)
					{
						if((!tax_struct_cd[i].equals("0") && !tax_struct_cd[i].equals("")) 
								&& !tax_struct_eff_dt[i].equals("") && !eff_dt[i].equals("") 
								&& (!counterparty_cd.equals("0") && !counterparty_cd.equals("")) 
								&& (!entity_role.equals("0") && !entity_role.equals("") && !invoice_type[i].equals("")))
						{
							int count=0;
							String query="SELECT COUNT(*) "
									+ "FROM FMS_ENTITY_TAX_STRUCT_DTL "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=? "
									+ "AND PLANT_SEQ_NO=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_UNIT=?";
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, comp_cd);
							stmt.setString(2, counterparty_cd);
							stmt.setString(3, entity_role);
							stmt.setString(4, seq_no[i]);
							stmt.setString(5, eff_dt[i]);
							stmt.setString(6, bu_unit);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								count=rset.getInt(1);
							}
							rset.close();
							stmt.close();
							
							if(count > 0)
							{
								query1="UPDATE FMS_ENTITY_TAX_STRUCT_DTL SET TAX_STRUCT_DTL=?,"
										+ "TAX_STRUCT_REMARK=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,FLAG=?,"
										+ "TAX_STRUCT_CD=? "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=? "
										+ "AND PLANT_SEQ_NO=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_UNIT=? AND INVOICE_TYPE=?";
								stmt1=dbcon.prepareStatement(query1);
								stmt1.setString(1, tax_struct_nm[i]);
								stmt1.setString(2, tax_struct_rmk[i]);
								stmt1.setString(3, emp_cd);
								stmt1.setString(4, "Y");
								stmt1.setString(5, tax_struct_cd[i]);
								stmt1.setString(6, comp_cd);
								stmt1.setString(7, counterparty_cd);
								stmt1.setString(8, entity_role);
								stmt1.setString(9, seq_no[i]);
								stmt1.setString(10, eff_dt[i]);
								stmt1.setString(11, bu_unit);
								stmt1.setString(12, invoice_type[i]);
								stmt1.executeUpdate();
								
								stmt1.close();
							}
							else
							{
								query1="INSERT INTO FMS_ENTITY_TAX_STRUCT_DTL(COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,"
										+ "TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,FLAG,BU_UNIT,EFF_DT,INVOICE_TYPE) "
										+ "VALUES(?,?,?,?,?,"
										+ "?,?,SYSDATE,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?)";
								stmt1=dbcon.prepareStatement(query1);
								stmt1.setString(1, comp_cd);
								stmt1.setString(2, counterparty_cd);
								stmt1.setString(3, entity_role);
								stmt1.setString(4, seq_no[i]);
								stmt1.setString(5, tax_struct_cd[i]);
								stmt1.setString(6, tax_struct_nm[i]);
								stmt1.setString(7, tax_struct_rmk[i]);
								stmt1.setString(8, emp_cd);
								stmt1.setString(9, "Y");
								stmt1.setString(10, bu_unit);
								stmt1.setString(11, eff_dt[i]);
								stmt1.setString(12, invoice_type[i]);
								stmt1.executeUpdate();
								
								stmt1.close();
							}
						}
						msg = " Successful! - Commodity Tax updated for "+entity_roleNm+" "+counterparty_nm+"!";
						msg_type="S";
					}
				}
			}
			
			dbcon.commit();
			url = "../master/frm_entity_tax_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&entity_role="+entity_role+"&counterparty_cd="+counterparty_cd+"&bu_unit="+bu_unit+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Tax Addition/Updatation failed for "+entity_roleNm+" "+counterparty_nm+"!";
			url=CommonVariable.errorpage_url+"?e="+e;
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateEntityTcsTdsTaxDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateEntityTcsTdsTaxDetail()";
		msg="";
		msg_type="";
		url="";
		String counterparty_cd="0";
		String counterparty_nm = "";
		String entity_role="0";
		String entity_roleNm ="";
		
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		
		try
		{
			entity_role = request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");
			counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
			
			if(entity_role.equals("G"))
			{
				counterparty_nm=utilBean.getGasExchangeName(dbcon,counterparty_cd);
				entity_roleNm = "Gas Exchange";
			}
			else if(entity_role.equals("B"))
			{
				counterparty_nm=utilBean.getCompanyName(dbcon,comp_cd);
				entity_roleNm = "Business Owner";
			}
			else
			{
				counterparty_nm=utilBean.getCounterpartyName(dbcon,counterparty_cd);
				if(entity_role.equals("C")) 
				{
					entity_roleNm = "Customer";
				}
				else if (entity_role.equals("T"))
				{
					entity_roleNm = "Trader";
				}
				else if (entity_role.equals("R"))
				{
					entity_roleNm = "Transporter";
				}
			}

			String type = request.getParameter("type")==null?"":request.getParameter("type");
			String tax_app = request.getParameter("tax_app")==null?"":request.getParameter("tax_app");
			
			String[] invoice_type = request.getParameterValues("invoice_type");
			String[] tax_struct_cd = request.getParameterValues("tax_struct_cd");
			String[] sap_tax_code = request.getParameterValues("sap_tax_code");
			String[] tax_struct_eff_dt = request.getParameterValues("tax_struct_eff_dt");
			String[] tax_struct_nm = request.getParameterValues("tax_struct_nm");
			String[] tax_struct_rmk = request.getParameterValues("tax_struct_rmk");
			String[] eff_dt = request.getParameterValues("eff_dt");
			
			String[] ser_invoice_type = request.getParameterValues("ser_invoice_type");
			String[] ser_tax_struct_cd = request.getParameterValues("ser_tax_struct_cd");
			String[] ser_sap_tax_code = request.getParameterValues("ser_sap_tax_code");
			String[] ser_tax_struct_eff_dt = request.getParameterValues("ser_tax_struct_eff_dt");
			String[] ser_tax_struct_nm = request.getParameterValues("ser_tax_struct_nm");
			String[] ser_tax_struct_rmk = request.getParameterValues("ser_tax_struct_rmk");
			String[] ser_eff_dt = request.getParameterValues("ser_eff_dt");
			
			if(type.equals("P"))
			{
				if(invoice_type!=null)
				{
					for(int i=0; i<invoice_type.length; i++)
					{
						if((!invoice_type[i].equals("0") && !invoice_type[i].equals("")) && (!tax_struct_cd[i].equals("0") 
								&& !tax_struct_cd[i].equals("")) && !tax_struct_eff_dt[i].equals("") && !eff_dt[i].equals("") 
								&& (!counterparty_cd.equals("0") && !counterparty_cd.equals("")) && (!entity_role.equals("0") 
								&& !entity_role.equals("")))
						{
							int count=0;
							String query="SELECT COUNT(*) "
									+ "FROM FMS_ENTITY_TCS_TDS_MST "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=? "
									+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND TAX_APP=? "
									+ "AND INVOICE_TYPE=? AND TAX_CATEGORY=?";
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, comp_cd);
							stmt.setString(2, counterparty_cd);
							stmt.setString(3, entity_role);
							stmt.setString(4, eff_dt[i]);
							stmt.setString(5, tax_app);
							stmt.setString(6, invoice_type[i]);
							stmt.setString(7, type);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								count=rset.getInt(1);
							}
							rset.close();
							stmt.close();
							
							if(count > 0)
							{
								query1="UPDATE FMS_ENTITY_TCS_TDS_MST SET TAX_STRUCT_DTL=?,"
										+ "TAX_STRUCT_REMARK=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,FLAG=?,"
										+ "TAX_STRUCT_CD=? "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=? "
										+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND TAX_APP=? "
										+ "AND INVOICE_TYPE=? AND TAX_CATEGORY=?";
								stmt1=dbcon.prepareStatement(query1);
								stmt1.setString(1, tax_struct_nm[i]);
								stmt1.setString(2, tax_struct_rmk[i]);
								stmt1.setString(3, emp_cd);
								stmt1.setString(4, "Y");
								stmt1.setString(5, tax_struct_cd[i]);
								stmt1.setString(6, comp_cd);
								stmt1.setString(7, counterparty_cd);
								stmt1.setString(8, entity_role);
								stmt1.setString(9, eff_dt[i]);
								stmt1.setString(10, tax_app);
								stmt1.setString(11, invoice_type[i]);
								stmt1.setString(12, type);
								stmt1.executeUpdate();
								
								stmt1.close();
							}
							else
							{
								query1="INSERT INTO FMS_ENTITY_TCS_TDS_MST(COMPANY_CD,COUNTERPARTY_CD,ENTITY,TAX_STRUCT_CD,"
										+ "TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,FLAG,INVOICE_TYPE,EFF_DT,TAX_CATEGORY,TAX_APP) "
										+ "VALUES(?,?,?,?,"
										+ "?,?,SYSDATE,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?)";
								stmt1=dbcon.prepareStatement(query1);
								stmt1.setString(1, comp_cd);
								stmt1.setString(2, counterparty_cd);
								stmt1.setString(3, entity_role);
								stmt1.setString(4, tax_struct_cd[i]);
								stmt1.setString(5, tax_struct_nm[i]);
								stmt1.setString(6, tax_struct_rmk[i]);
								stmt1.setString(7, emp_cd);
								stmt1.setString(8, "Y");
								stmt1.setString(9, invoice_type[i]);
								stmt1.setString(10, eff_dt[i]);
								stmt1.setString(11, type);
								stmt1.setString(12, tax_app);
								stmt1.executeUpdate();
								
								stmt1.close();
							}
							
							msg = "Successful! - TCS/TDS entry updated for "+entity_roleNm+" "+counterparty_nm+"!";
							msg_type="S";
						}
						else
						{
							msg = "Failed! - TCS/TDS entry updatation Failed for "+entity_roleNm+" "+counterparty_nm+"!";
							msg_type="E";
						}
					}
				}
				else
				{
					msg = "Failed! - TCS/TDS entry updatation Failed for "+entity_roleNm+" "+counterparty_nm+"!";
					msg_type="E";
				}
			}
			else if(type.equals("S"))
			{
				if(ser_invoice_type!=null)
				{
					for(int i=0; i<ser_invoice_type.length; i++)
					{
						if((!ser_invoice_type[i].equals("0") && !ser_invoice_type[i].equals("")) 
								&& (!ser_tax_struct_cd[i].equals("0") && !ser_tax_struct_cd[i].equals("")) 
								&& !ser_tax_struct_eff_dt[i].equals("") && !ser_eff_dt[i].equals("") 
								&& (!counterparty_cd.equals("0") && !counterparty_cd.equals("")) 
								&& (!entity_role.equals("0") && !entity_role.equals("")))
						{
							int count=0;
							String query="SELECT COUNT(*) "
									+ "FROM FMS_ENTITY_TCS_TDS_MST "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=? "
									+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND TAX_APP=? "
									+ "AND INVOICE_TYPE=? AND TAX_CATEGORY=?";
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, comp_cd);
							stmt.setString(2, counterparty_cd);
							stmt.setString(3, entity_role);
							stmt.setString(4, ser_eff_dt[i]);
							stmt.setString(5, tax_app);
							stmt.setString(6, ser_invoice_type[i]);
							stmt.setString(7, type);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								count=rset.getInt(1);
							}
							rset.close();
							stmt.close();
							
							if(count > 0)
							{
								query1="UPDATE FMS_ENTITY_TCS_TDS_MST SET TAX_STRUCT_DTL=?,"
										+ "TAX_STRUCT_REMARK=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,FLAG=?,"
										+ "TAX_STRUCT_CD=? "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=? "
										+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND TAX_APP=? "
										+ "AND INVOICE_TYPE=? AND TAX_CATEGORY=?";
								stmt1=dbcon.prepareStatement(query1);
								stmt1.setString(1, ser_tax_struct_nm[i]);
								stmt1.setString(2, ser_tax_struct_rmk[i]);
								stmt1.setString(3, emp_cd);
								stmt1.setString(4, "Y");
								stmt1.setString(5, ser_tax_struct_cd[i]);
								stmt1.setString(6, comp_cd);
								stmt1.setString(7, counterparty_cd);
								stmt1.setString(8, entity_role);
								stmt1.setString(9, ser_eff_dt[i]);
								stmt1.setString(10, tax_app);
								stmt1.setString(11, ser_invoice_type[i]);
								stmt1.setString(12, type);
								stmt1.executeUpdate();
								
								stmt1.close();
							}
							else
							{
								query1="INSERT INTO FMS_ENTITY_TCS_TDS_MST(COMPANY_CD,COUNTERPARTY_CD,ENTITY,TAX_STRUCT_CD,"
										+ "TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,FLAG,INVOICE_TYPE,EFF_DT,TAX_CATEGORY,TAX_APP) "
										+ "VALUES(?,?,?,?,"
										+ "?,?,SYSDATE,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?)";
								stmt1=dbcon.prepareStatement(query1);
								stmt1.setString(1, comp_cd);
								stmt1.setString(2, counterparty_cd);
								stmt1.setString(3, entity_role);
								stmt1.setString(4, ser_tax_struct_cd[i]);
								stmt1.setString(5, ser_tax_struct_nm[i]);
								stmt1.setString(6, ser_tax_struct_rmk[i]);
								stmt1.setString(7, emp_cd);
								stmt1.setString(8, "Y");
								stmt1.setString(9, ser_invoice_type[i]);
								stmt1.setString(10, ser_eff_dt[i]);
								stmt1.setString(11, type);
								stmt1.setString(12, tax_app);
								stmt1.executeUpdate();
								
								stmt1.close();
							}
							
							msg = "Successful! - TCS/TDS entry updated for "+entity_roleNm+" "+counterparty_nm+"!";
							msg_type="S";
						}
						else
						{
							msg = "Failed! - TCS/TDS entry updatation Failed for "+entity_roleNm+" "+counterparty_nm+"!";
							msg_type="E";
						}
					}
				}
				else
				{
					msg = "Failed! - TCS/TDS entry updatation Failed for "+entity_roleNm+" "+counterparty_nm+"!";
					msg_type="E";
				}
			}
			else
			{
				msg = "Failed! - TCS/TDS entry updatation Failed for "+entity_roleNm+" "+counterparty_nm+"!";
				msg_type="E";
			}
			dbcon.commit();
			url = "../master/frm_entity_tcs_tds_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&entity_role="+entity_role+"&counterparty_cd="+counterparty_cd+"&tax_app="+tax_app+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - TCS/TDS entry addition/updatation Failed for "+entity_roleNm+" "+counterparty_nm+"!";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateTurnoverEntry(HttpServletRequest request)throws IOException,SQLException
	{
		String function_nm="InsertUpdateTurnoverEntry()";
		msg="";
		msg_type="";
		url="";
		
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		String counterparty_nm = "";
		String entity_role="0";
		String entity_roleNm ="";
		try
		{
			entity_role=request.getParameter("entity_role")==null?"":request.getParameter("entity_role");
			String financial_year=request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String turnover_cd=request.getParameter("turnover_cd")==null?"":request.getParameter("turnover_cd");
			
			String[] counterparty_cd=request.getParameterValues("counterparty_cd");
			//String[] turnover_flag=request.getParameterValues("turnover_flag");
			String[] index=request.getParameterValues("index");
			
			if(counterparty_cd!=null)
			{
				for(int i=0; i<counterparty_cd.length;i++)
				{
					if(entity_role.equals("G"))
					{
						counterparty_nm=utilBean.getGasExchangeName(dbcon,counterparty_cd[i]);
						entity_roleNm = "Gas Exchange";
					}
					else if(entity_role.equals("B"))
					{
						counterparty_nm=utilBean.getCompanyName(dbcon,comp_cd);
						entity_roleNm = "Business Owner";
					}
					else
					{
						counterparty_nm=utilBean.getCounterpartyName(dbcon,counterparty_cd[i]);
						if(entity_role.equals("C")) 
						{
							entity_roleNm = "Customer";
						}
						else if (entity_role.equals("T"))
						{
							entity_roleNm = "Trader";
						}
						else if (entity_role.equals("R"))
						{
							entity_roleNm = "Transporter";
						}
					}
					String turnover_flag=request.getParameter("turnover_flag"+index[i])==null?"":request.getParameter("turnover_flag"+index[i]);
					
					int count=0;
					String query="SELECT COUNT(*) "
							+ "FROM FMS_ENTITY_TURNOVER_DTL "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=? "
							+ "AND FINANCIAL_YEAR=? AND TURNOVER_CD=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd[i]);
					stmt.setString(3, entity_role);
					stmt.setString(4, financial_year);
					stmt.setString(5, turnover_cd);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						count=rset.getInt(1);
					}
					rset.close();
					stmt.close();
					
					if(count>0)
					{
						query1="UPDATE FMS_ENTITY_TURNOVER_DTL SET TURNOVER_FLAG=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=? "
								+ "AND FINANCIAL_YEAR=? AND TURNOVER_CD=?";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(1, turnover_flag);
						stmt1.setString(2, emp_cd);
						stmt1.setString(3, comp_cd);
						stmt1.setString(4, counterparty_cd[i]);
						stmt1.setString(5, entity_role);
						stmt1.setString(6, financial_year);
						stmt1.setString(7, turnover_cd);
						stmt1.executeUpdate();
						
						stmt1.close();
						
						msg+= "Successful! - "+financial_year+" Turnover updated for "+entity_roleNm+" "+counterparty_nm;
						msg_type = "S";
					}
					else
					{
						query1="INSERT INTO FMS_ENTITY_TURNOVER_DTL(COMPANY_CD,COUNTERPARTY_CD,ENTITY,FINANCIAL_YEAR,TURNOVER_CD,"
								+ "TURNOVER_FLAG,ENT_BY,ENT_DT) "
								+ "VALUES(?,?,?,?,?,"
								+ "?,?,SYSDATE)";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd[i]);
						stmt1.setString(3, entity_role);
						stmt1.setString(4, financial_year);
						stmt1.setString(5, turnover_cd);
						stmt1.setString(6, turnover_flag);
						stmt1.setString(7, emp_cd);
						
						stmt1.executeUpdate();
						
						stmt1.close();
						
						msg+= "Successful! - "+financial_year+" Turnover added for "+entity_roleNm+" "+counterparty_nm;
						msg_type = "S";
					}
				}
			}
			else
			{
				msg = "Failed! - Turnover Entry submission Failed!";
				msg_type = "E";
			}
			
			url = "../master/frm_entity_turnover_dtl.jsp?msg="+msg+"&msg_type="+msg_type+
					"&turnover_cd="+turnover_cd+"&financial_year="+financial_year+"&entity_role="+entity_role+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Turnover Entry submission Failed!";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateEntityEmailSetupDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateEntityEmailSetupDetail()";
		msg="";
		msg_type="";
		url="";
		String counterparty_cd="0";
		String counterparty_nm = "";
		String entity_role="0";
		String entity_roleNm ="";
		
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		
		try
		{
			entity_role = request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");
			counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
			String address_type = request.getParameter("address_type")==null?"":request.getParameter("address_type");
			String notification_type = request.getParameter("notification_type")==null?"":request.getParameter("notification_type");
			
			if(entity_role.equals("G"))
			{
				counterparty_nm=utilBean.getGasExchangeName(dbcon,counterparty_cd);
				entity_roleNm = "Gas Exchange";
			}
			else if(entity_role.equals("B"))
			{
				counterparty_nm=utilBean.getCompanyName(dbcon,comp_cd);
				entity_roleNm = "Business Owner";
			}
			else
			{
				counterparty_nm=utilBean.getCounterpartyName(dbcon,counterparty_cd);
				if(entity_role.equals("C")) 
				{
					entity_roleNm = "Customer";
				}
				else if (entity_role.equals("T"))
				{
					entity_roleNm = "Trader";
				}
				else if (entity_role.equals("R"))
				{
					entity_roleNm = "Transporter";
				}
				else if (entity_role.equals("V"))
				{
					entity_roleNm = "Vessel Agent";
				}
				else if (entity_role.equals("H"))
				{
					entity_roleNm = "Custom House Agent";
				}
				else if (entity_role.equals("S"))
				{
					entity_roleNm = "Surveyor";
				}
			}
			
			String[] seq_no = request.getParameterValues("seq_no");
			String[] eff_dt = request.getParameterValues("eff_dt");
			String[] to_flag = request.getParameterValues("to_flag");
			String[] dlng_to_flag = request.getParameterValues("dlng_to_flag");
			String[] dlng_eff_dt = request.getParameterValues("dlng_eff_dt");
			String[] dlng_seq_no = request.getParameterValues("dlng_seq_no");
			
			if((!notification_type.equals("") && !notification_type.equals("0"))
				&& (!address_type.equals("") && !address_type.equals("0")) 
				&& (!entity_role.equals("") && !entity_role.equals("0"))
				&& (!counterparty_cd.equals("") && !counterparty_cd.equals("0")))
			{
				if(seq_no!=null)
				{
					for(int i=0;i<seq_no.length;i++)
					{
						int count =0;
						String query = "SELECT COUNT(*) "
								+ "FROM FMS_ENTITY_CONTACT_MST "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND ENTITY=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND SEQ_NO=? AND ADDR_FLAG=? AND TYPE=? ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, counterparty_cd);
						stmt.setString(3, entity_role);
						stmt.setString(4, eff_dt[i]);
						stmt.setString(5, seq_no[i]);
						stmt.setString(6, address_type);
						stmt.setString(7, "RLNG");
						rset=stmt.executeQuery();
						if(rset.next())
						{
							count=rset.getInt(1);
						}
						rset.close();
						stmt.close();
						
						if(to_flag[i].equals(""))
						{
							to_flag[i] = "N";
						}
						
						if(count > 0)
						{
							query1 = "UPDATE FMS_ENTITY_CONTACT_MST SET TO_"+notification_type+"=? "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND ENTITY=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND SEQ_NO=? AND ADDR_FLAG=? AND TYPE=? ";
							stmt1=dbcon.prepareStatement(query1);
							stmt1.setString(1, to_flag[i]);
							stmt1.setString(2, comp_cd);
							stmt1.setString(3, counterparty_cd);
							stmt1.setString(4, entity_role);
							stmt1.setString(5, eff_dt[i]);
							stmt1.setString(6, seq_no[i]);
							stmt1.setString(7, address_type);
							stmt1.setString(8, "RLNG");
							stmt1.executeQuery();
							
							stmt1.close();
						}

						msg = "Successful! - "+entity_roleNm+" "+counterparty_nm+" Email Setup added/updated Successfully!";
						msg_type="S";
					}
				}
				
				if(entity_role.equals("C") || entity_role.equals("B") || entity_role.equals("T") || entity_role.equals("G"))
				{
					if(dlng_seq_no!=null)
					{
						for(int i=0;i<dlng_seq_no.length;i++)
						{ 
							int dlng_count =0;
							String query = "SELECT COUNT(*) "
									+ "FROM FMS_ENTITY_CONTACT_MST "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND ENTITY=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND SEQ_NO=? AND ADDR_FLAG=? AND TYPE=? ";
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, comp_cd);
							stmt.setString(2, counterparty_cd);
							stmt.setString(3, entity_role);
							stmt.setString(4, dlng_eff_dt[i]);
							stmt.setString(5, dlng_seq_no[i]);
							stmt.setString(6, address_type);
							stmt.setString(7, "DLNG");
							rset=stmt.executeQuery();
							if(rset.next())
							{
								dlng_count=rset.getInt(1);
							}
							rset.close();
							stmt.close();
							
							if(dlng_to_flag[i].equals(""))
							{
								dlng_to_flag[i] = "N";
							}
							
							if(dlng_count > 0)
							{
								query1 = "UPDATE FMS_ENTITY_CONTACT_MST SET TO_"+notification_type+"=? "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND ENTITY=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') "
										+ "AND SEQ_NO=? AND ADDR_FLAG=? AND TYPE=? ";
								stmt1=dbcon.prepareStatement(query1);
								stmt1.setString(1, dlng_to_flag[i]);
								stmt1.setString(2, comp_cd);
								stmt1.setString(3, counterparty_cd);
								stmt1.setString(4, entity_role);
								stmt1.setString(5, dlng_eff_dt[i]);
								stmt1.setString(6, dlng_seq_no[i]);
								stmt1.setString(7, address_type);
								stmt1.setString(8, "DLNG");
								stmt1.executeQuery();
								
								stmt1.close();
							}
						}
						msg = "Successful! - "+entity_roleNm+" "+counterparty_nm+" Email Setup added/updated Successfully!";
						msg_type="S";
					}
				}
			}
			else
			{
				msg = "Failed! - Email Setup for "+entity_roleNm+" "+counterparty_nm+" Failed!";
				msg_type="E";
				
				System.out.println("Last Else Failed Run .....");
			}
			
			dbcon.commit();
			url = "../master/frm_entity_email_setup.jsp?msg="+msg+"&msg_type="+msg_type+"&entity_role="+entity_role+
					"&counterparty_cd="+counterparty_cd+"&address_type="+address_type+"&notification_type="+notification_type+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			msg = "Error in Exception! - Entity Email Setup Done Failed!";
			url=CommonVariable.errorpage_url+"?e="+e;
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void CounterpartyMailBody(String comp_cd,String cp_name,String cp_abbr,String operation,String message,String emp_cd,String new_values, String old_values) throws Exception
	{
		String mailBody="";
		String function_nm="CounterpartyMailBody()";

		try
		{
			String counterpartyDetail="";
			String addressDetail="";
			String statusDetail="";
			
			if(!new_values.equals(""))
			{
				String cp="",old_cp="";
				String name="",old_name="";
				String abbr="",old_abbr="";
				String eff_dt = "",old_eff_dt = "";
				String pan_no="",old_pan_no="";
				String pan_iss_dt="",old_pan_iss_dt="";
				String kyc="",old_kyc="";
				String igx="",old_igx="";
				String notes="",old_notes="";
				String sap_code="",old_sap_code="";
				
				String add="",old_add="";
				String city="",old_city="";
				String state="",old_state="";
				String zone="",old_zone="";
				String pin="",old_pin="";
				String country="",old_country="";
				String phone="",old_phone="";
				String fax1="",old_fax1="";
				String fax2="",old_fax2="";
				String cell="",old_cell="";
				String email="",old_email="";
				String al_phone="",old_al_phone="";
				String add_eff_dt="",old_add_eff_dt="";
				
				String status="",old_status="";
				String status_eff_dt="",old_status_eff_dt="";
				String status_chk="",old_status_chk="";
				
				String split_New_Value[] = new_values.split("#");
				for(int i=0; i<split_New_Value.length; i++)
				{
					if(split_New_Value[i].startsWith("CP=")){
						String temp[] = split_New_Value[i].split("CP=");
						if(temp.length>0){
							cp=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("NAME=")){
						String temp[] = split_New_Value[i].split("NAME=");
						if(temp.length>0){
							name=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("ABBR=")){
						String temp[] = split_New_Value[i].split("ABBR=");
						if(temp.length>0){
							abbr=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("PANNO=")){
						String temp[] = split_New_Value[i].split("PANNO=");
						if(temp.length>0){
							pan_no=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("PANDT=")){
						String temp[] = split_New_Value[i].split("PANDT=");
						if(temp.length>0){
							pan_iss_dt=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("KYC=")){
						String temp[] = split_New_Value[i].split("KYC=");
						if(temp.length>0){
							kyc=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("IGX=")){
						String temp[] = split_New_Value[i].split("IGX=");
						if(temp.length>0){
							igx=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("EFFDT=")){
						String temp[] = split_New_Value[i].split("EFFDT=");
						if(temp.length>0){
							eff_dt=temp[1];
						}
					}
					
					if(split_New_Value[i].startsWith("ADD=")){
						String temp[] = split_New_Value[i].split("ADD=");
						if(temp.length>0){
							add=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("CITY=")){
						String temp[] = split_New_Value[i].split("CITY=");
						if(temp.length>0){
							city=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("STATE=")){
						String temp[] = split_New_Value[i].split("STATE=");
						if(temp.length>0){
							state=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("ZONE=")){
						String temp[] = split_New_Value[i].split("ZONE=");
						if(temp.length>0){
							zone=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("PIN=")){
						String temp[] = split_New_Value[i].split("PIN=");
						if(temp.length>0){
							pin=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("COUNTRY=")){
						String temp[] = split_New_Value[i].split("COUNTRY=");
						if(temp.length>0){
							country=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("PH=")){
						String temp[] = split_New_Value[i].split("PH=");
						if(temp.length>0){
							phone=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("FAX1=")){
						String temp[] = split_New_Value[i].split("FAX1=");
						if(temp.length>0){
							fax1=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("FAX2=")){
						String temp[] = split_New_Value[i].split("FAX2=");
						if(temp.length>0){
							fax2=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("CELL=")){
						String temp[] = split_New_Value[i].split("CELL=");
						if(temp.length>0){
							cell=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("EMAIL=")){
						String temp[] = split_New_Value[i].split("EMAIL=");
						if(temp.length>0){
							email=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("ALTPH=")){
						String temp[] = split_New_Value[i].split("ALTPH=");
						if(temp.length>0){
							al_phone=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("REFFDT=")){
						String temp[] = split_New_Value[i].split("REFFDT=");
						if(temp.length>0){
							add_eff_dt=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("STATUS=")){
						String temp[] = split_New_Value[i].split("STATUS=");
						if(temp.length>0){
							status=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("AEFFDT=")){
						String temp[] = split_New_Value[i].split("AEFFDT=");
						if(temp.length>0){
							status_eff_dt=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("DORMANT_CHK=")){
						String temp[] = split_New_Value[i].split("DORMANT_CHK=");
						if(temp.length>0){
							status_chk=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("SAPCD=")){
						String temp[] = split_New_Value[i].split("SAPCD=");
						if(temp.length>0){
							sap_code=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("NOTES=")){
						String temp[] = split_New_Value[i].split("NOTES=");
						if(temp.length>0){
							notes=temp[1];
						}
					}
				}
				
				if(!old_values.equals(""))
				{
					String split_Old_Value[] = old_values.split("#");
					for(int i=0; i<split_Old_Value.length; i++)
					{
						if(split_Old_Value[i].startsWith("CP=")){
							String temp[] = split_Old_Value[i].split("CP=");
							if(temp.length>0){
								old_cp=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("NAME=")){
							String temp[] = split_Old_Value[i].split("NAME=");
							if(temp.length>0){
								old_name=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("ABBR=")){
							String temp[] = split_Old_Value[i].split("ABBR=");
							if(temp.length>0){
								old_abbr=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("PANNO=")){
							String temp[] = split_Old_Value[i].split("PANNO=");
							if(temp.length>0){
								old_pan_no=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("PANDT=")){
							String temp[] = split_Old_Value[i].split("PANDT=");
							if(temp.length>0){
								old_pan_iss_dt=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("KYC=")){
							String temp[] = split_Old_Value[i].split("KYC=");
							if(temp.length>0){
								old_kyc=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("IGX=")){
							String temp[] = split_Old_Value[i].split("IGX=");
							if(temp.length>0){
								old_igx=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("EFFDT=")){
							String temp[] = split_Old_Value[i].split("EFFDT=");
							if(temp.length>0){
								old_eff_dt=temp[1];
							}
						}
						
						if(split_Old_Value[i].startsWith("ADD=")){
							String temp[] = split_Old_Value[i].split("ADD=");
							if(temp.length>0){
								old_add=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("CITY=")){
							String temp[] = split_Old_Value[i].split("CITY=");
							if(temp.length>0){
								old_city=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("STATE=")){
							String temp[] = split_Old_Value[i].split("STATE=");
							if(temp.length>0){
								old_state=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("ZONE=")){
							String temp[] = split_Old_Value[i].split("ZONE=");
							if(temp.length>0){
								old_zone=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("PIN=")){
							String temp[] = split_Old_Value[i].split("PIN=");
							if(temp.length>0){
								old_pin=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("COUNTRY=")){
							String temp[] = split_Old_Value[i].split("COUNTRY=");
							if(temp.length>0){
								old_country=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("PH=")){
							String temp[] = split_Old_Value[i].split("PH=");
							if(temp.length>0){
								old_phone=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("FAX1=")){
							String temp[] = split_Old_Value[i].split("FAX1=");
							if(temp.length>0){
								old_fax1=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("FAX2=")){
							String temp[] = split_Old_Value[i].split("FAX2=");
							if(temp.length>0){
								old_fax2=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("CELL=")){
							String temp[] = split_Old_Value[i].split("CELL=");
							if(temp.length>0){
								old_cell=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("EMAIL=")){
							String temp[] = split_Old_Value[i].split("EMAIL=");
							if(temp.length>0){
								old_email=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("ALTPH=")){
							String temp[] = split_Old_Value[i].split("ALTPH=");
							if(temp.length>0){
								old_al_phone=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("REFFDT=")){
							String temp[] = split_Old_Value[i].split("REFFDT=");
							if(temp.length>0){
								old_add_eff_dt=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("STATUS=")){
							String temp[] = split_Old_Value[i].split("STATUS=");
							if(temp.length>0){
								old_status=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("AEFFDT=")){
							String temp[] = split_Old_Value[i].split("AEFFDT=");
							if(temp.length>0){
								old_status_eff_dt=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("DORMANT_CHK=")){
							String temp[] = split_Old_Value[i].split("DORMANT_CHK=");
							if(temp.length>0){
								old_status_chk=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("SAPCD=")){
							String temp[] = split_Old_Value[i].split("SAPCD=");
							if(temp.length>0){
								old_sap_code=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("NOTES=")){
							String temp[] = split_Old_Value[i].split("NOTES=");
							if(temp.length>0){
								old_notes=temp[1];
							}
						}
					}
				}
				
				if(zone.equals("N"))
				{
					zone="North";
				}
				else if(zone.equals("S"))
				{
					zone="South";
				}
				else if(zone.equals("E"))
				{
					zone="East";
				}
				else if(zone.equals("W"))
				{
					zone="West";
				}
				else if(zone.equals("C"))
				{
					zone="Central";
				}
				
				if(old_zone.equals("N"))
				{
					old_zone="North";
				}
				else if(old_zone.equals("S"))
				{
					old_zone="South";
				}
				else if(old_zone.equals("E"))
				{
					old_zone="East";
				}
				else if(old_zone.equals("W"))
				{
					old_zone="West";
				}
				else if(old_zone.equals("C"))
				{
					old_zone="Central";
				}
				
				if(!cp.equals(""))
				{
					if(old_values.equals("")) 
					{
						counterpartyDetail="Name : "+name+"<br>"
								+ "Abbreviation : "+abbr+"<br>"
								+ "KYC Clearance : "+kyc+"<br>"
								+ "IGX Clearance : "+igx+"<br>"
								+ "PAN No. : "+pan_no+"<br>"
								+ "PAN Issue Date : "+pan_iss_dt+"<br>"
								+ "SAP Code : "+sap_code+"<br>"
								+ "Effective Date : "+eff_dt+"<br>"
								+ "Notes : "+notes;
						
						addressDetail="Address : "+add+"<br>"
								+ "City : "+city+"<br>"
								+ "State/ Province : "+state+"<br>"
								+ "Zone : "+zone+"<br>"
								+ "PIN/ZIP Code : "+pin+"<br>"
								+ "Country : "+country+"<br>"
								+ "Phone : "+phone+"<br>"
								+ "Fax-1 : "+fax1+"<br>"
								+ "Fax-2 : "+fax2+"<br>"
								+ "Cell : "+cell+"<br>"
								+ "E-mail : "+email+"<br>"
								+ "Alternate Phone : "+al_phone+"<br>"
								+ "Effective Date : "+add_eff_dt;
						if(status.equals("Y"))
						{
							status="Activated";
						}
						else if(status.equals("N"))
						{
							status="Deactivated";
						}
						
						if(status_chk.equals("Y")) {
							status_chk="Yes";
						}else {
							status_chk="No";
						}
						
						statusDetail = "Status : "+status+"<br>";
								//+ "Effective Date : "+status_eff_dt+"<br>";
								//+ "Dormant Check : "+status_chk;
					}
					else
					{
						if(!name.equals(old_name)){
							counterpartyDetail+="Name : "+name+"<br>";
						}
						if(!abbr.equals(old_abbr)) {
							counterpartyDetail+= "Abbreviation : "+abbr+"<br>";
						}
						if(!pan_no.equals(old_pan_no)) {
							counterpartyDetail += "PAN No. : "+pan_no+"<br>";
						}
						if(!pan_iss_dt.equals(old_pan_iss_dt)) {
							counterpartyDetail += "PAN Issue Date : "+pan_iss_dt+"<br>";
						}
						if(!sap_code.equals(old_sap_code)) {
							counterpartyDetail += "SAP Code : "+sap_code+"<br>";
						}
						if(!kyc.equals(old_kyc)) {
							counterpartyDetail += "KYC Clearance : "+kyc+"<br>";
						}
						if(!igx.equals(old_igx)) {
							counterpartyDetail += "IGX Clearance : "+igx+"<br>";
						}
						if(!eff_dt.equals(old_eff_dt)) {
							counterpartyDetail += "Effective Date : "+eff_dt+"<br>";
						}
						if(!notes.equals(old_notes)) {
							counterpartyDetail += "Notes : "+notes+"<br>";
						}
						
						if(!add.equals(old_add)) {
							addressDetail+="Address : "+add+"<br>";
						}
						if(!city.equals(old_city)) {
							addressDetail+= "City : "+city+"<br>";
						}
						if(!state.equals(old_state)) {
							addressDetail+= "State/ Province : "+state+"<br>";
						}
						if(!zone.equals(old_zone)) {
							addressDetail+= "Zone : "+zone+"<br>";
						}
						if(!pin.equals(old_pin)) {
							addressDetail+= "PIN/ZIP Code : "+pin+"<br>";
						}
						if(!country.equals(old_country)) {
							addressDetail+= "Country : "+country+"<br>";
						}
						if(!phone.equals(old_phone)) {
							addressDetail+= "Phone : "+phone+"<br>";
						}
						if(!fax1.equals(old_fax1)) {
							addressDetail+= "Fax-1 : "+fax1+"<br>";
						}
						if(!fax2.equals(old_fax2)) {
							addressDetail+= "Fax-2 : "+fax2+"<br>";
						}
						if(!cell.equals(old_cell)) {
							addressDetail += "Cell : "+cell+"<br>";
						}
						if(!email.equals(old_email)) {
							addressDetail+= "E-mail : "+email+"<br>";
						}
						if(!al_phone.equals(old_al_phone)) {
							addressDetail+= "Alternate Phone : "+al_phone+"<br>";
						}
						if(!add_eff_dt.equals(old_add_eff_dt)) {
							addressDetail+= "Effective Date : "+add_eff_dt+"<br>";
						}
						
						if(!status.equals(old_status))
						{
							if(status.equals("Y"))
							{
								status="Activated";
							}
							else if(status.equals("N"))
							{
								status="Deactivated";
							}
							
							statusDetail += "Status : "+status+"<br>";
									
						}
						if(!status_eff_dt.equals(old_status_eff_dt))
						{
							statusDetail+= "Effective Date : "+status_eff_dt+"<br>";
						}
						
						/*
						if(!status_chk.equals(old_status_chk)) 
						{
							if(status_chk.equals("Y")) {
								status_chk="Yes";
							}else {
								status_chk="No";
							}
							
							statusDetail+="Dormant Check : "+status_chk;
						}
						*/
					}
				}
			}
			
			String subject="Counterparty "+cp_abbr+" - "+cp_name+" : ";
			if(operation.equals("MODIFY"))
			{
				subject+= "Modified Successfully!";
			}
			else
			{
				subject+= "Inserted Successfully!";
			}
			
			mailBody="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Counterparty <b>"+cp_abbr+" - "+cp_name+"</b> ";
					if(operation.equals("MODIFY"))
					{
						mailBody+= "<font style='background:#00cc00' color='white'>Modified</font> Successfully ";
					}
					else
					{
						mailBody+= "<font style='background:#00cc00' color='white'>Inserted</font> Successfully ";
					}
			mailBody+="by "+utilBean.getEmpName(dbcon,emp_cd)+" ";
			mailBody+="on "+utilDate.getSysdateWithTime24hr()+"";
			mailBody+= "</span><br><br>";
			
			if(!counterpartyDetail.equals(""))
			{
				mailBody+="<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'><b>Counterparty Detail :<br></b>"+counterpartyDetail+"</span><br><br>";
			}
			if(!addressDetail.equals(""))
			{
				mailBody+="<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'><b>Counterparty Register Address & Contact Detail :<br></b>"+addressDetail+"</span><br><br>";
			}
			if(!statusDetail.equals(""))
			{
				mailBody+="<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'><b>Status :<br></b>"+statusDetail+"</span><br><br>";
			}
			
			
			
			mailBody+=CommonVariable.mail_disclaimer
					+ "</html>";
			
			String to_mail_list = utilBean.getToMailReceipentList(dbcon,"0","Counterparty Audit Report", "Master", "NA", "On-Event");
			String cc_mail_list=utilBean.getCcMailReceipentList(dbcon,"0","Counterparty Audit Report", "Master", "NA", "On-Event");
			
			if(!to_mail_list.equals("") && !mailBody.equals(""))
			{
				mailDelv.sendMail(comp_cd,to_mail_list, subject, mailBody, "", cc_mail_list, "");
			}
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error In Exception!";
		}
	}
}
