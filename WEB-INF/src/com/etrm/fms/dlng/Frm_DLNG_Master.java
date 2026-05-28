package com.etrm.fms.dlng;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.ExcelVersionDtl;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.escapeSingleQuotes;

@WebServlet("/servlet/Frm_DLNG_Master")
@MultipartConfig(fileSizeThreshold=1024*1024*10, 	// 10 MB 
maxFileSize=1024*1024*50,      	// 50 MB
maxRequestSize=1024*1024*100)   	// 100 MB
public class Frm_DLNG_Master extends HttpServlet
{
	static String db_src_file_name="Frm_DLNG_Master.java";
	public static  Connection dbcon;
	
	public static String servletName = "Frm_DLNG_Master.java";
	public static String option = "";
	public static String url = "";
	public static String msg = "";
	public static String msg_type = "";
	public static String err_msg = "";
	
	private static String queryString = null;
	private static String query0 = null;
	private static String query = null;
	private static String query1 = null;
	private static String query2 = null;
	private static String query3 = null;
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
	
	public static String emp_cd="";
	public static String comp_cd="";
	public static String emp_nm="";
	public static String ip="";
	
	public static String commonUrl_pra="";
	
	public static escapeSingleQuotes escObj = new escapeSingleQuotes();
	
	static UtilBean utilBean = new UtilBean();
	static UtilBean_DLNG utilBean_dlng = new UtilBean_DLNG();
	static DateUtil utilDate = new DateUtil();
	static ExcelVersionDtl excelversiondtl = new ExcelVersionDtl();
	
	private static final String SAVE_DIR = CommonVariable.dlng_dir;
	
	public static boolean isOLE2Stream(String filePath) throws IOException {return ExcelVersionDtl.isMasterNumberMatch(filePath, ExcelVersionDtl.OLE2_MASTER_NUMBER);}
	public static boolean isOOXMLStream(String filePath) throws IOException {return ExcelVersionDtl.isMasterNumberMatch(filePath,ExcelVersionDtl. OOXML_MASTER_NUMBER);}
	
	static NumberFormat nf = new DecimalFormat("###########0.00");
	static NumberFormat nf2 = new DecimalFormat("###########0.0000");
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException
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
					comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
					emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
					emp_nm = (String)session.getAttribute("emp_nm")==null?"":(String)session.getAttribute("emp_nm");
					ip = (String)session.getAttribute("ip")==null?"":(String)session.getAttribute("ip");
					u=request.getParameter("u")==null?"":request.getParameter("u");
					
					new_value="";
					old_value="";
					
					option=request.getParameter("option")==null?"":request.getParameter("option");
					
					commonUrl_pra = "&u="+u;
					
					if(option.equalsIgnoreCase("TRUCK_TRANS_MST"))
					{
						InsertUpdateTruckTransDtls(request);
					}
					else if(option.equalsIgnoreCase("TRUCK_TYPE_MST"))
					{
						InsertUpdateTruckTypeDtls(request);
					}
					else if(option.equalsIgnoreCase("TRUCK_MST"))
					{
						InsertUpdateTruckDtls(request);
					}
					else if(option.equalsIgnoreCase("TRUCK_DRIVER_MST"))
					{
						InsertUpdateTruckDriverDtls(request);
					}
					else if(option.equalsIgnoreCase("LINK_TRUCK_TRANS"))
					{
						InsertUpdateLinkTruckTransDtls(request);
					}
					else if(option.equalsIgnoreCase("LINK_DRIVER_TRANS"))
					{
						InsertUpdateLinkDriverTransDtls(request);
					}
					else if(option.equalsIgnoreCase("LINK_TRUCK_DRIVER"))
					{
						InsertUpdateLinkDriverTruckDtls(request);
					}
					else if(option.equalsIgnoreCase("CHECKPOST_MST"))
					{
						InsertUpdateCheckpostDtls(request);
					}
					else if(option.equalsIgnoreCase("TRUCK_TRANS_CONTACT_DETAILS"))
					{
						InsertUpdateTruckTransContactDetail(request);
					}
					else if(option.equalsIgnoreCase("FILLING_MST"))
					{
						InsertUpdatefillStationDetail(request);
					}
					else if(option.equalsIgnoreCase("BAY_MST"))
					{
						InsertUpdateBaySlotDetail(request);
					}
					else if(option.equalsIgnoreCase("CUST_PLANT_CHKPOST_LINK"))
					{
						InsertUpdateLinkCheckpostPlantDetail(request);
					}
				}
				
				dbcon.close();
				dbcon=null;
			}
			catch(Exception e)
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
				url=CommonVariable.errorpage_url+"?e="+e;
			}
			finally
			{
				if(rset != null){try {rset.close();}catch(SQLException e){System.out.println("rset is not close " + e);}}
				if(rset1 != null){try {rset1.close();}catch(SQLException e){System.out.println("rset1 is not close " + e);}}
				if(rset2 != null){try {rset2.close();}catch(SQLException e){System.out.println("rset2 is not close " + e);}}
				if(rset3 != null){try {rset3.close();}catch(SQLException e){System.out.println("rset3 is not close " + e);}}
				if(rset4 != null){try {rset4.close();}catch(SQLException e){System.out.println("rset4 is not close " + e);}}
				if(stmt != null){try {stmt.close();}catch(SQLException e){System.out.println("stmt is not close " + e);}}
				if(stmt1 != null){try {stmt1.close();}catch(SQLException e){System.out.println("stmt1 is not close " + e);}}
				if(stmt2 != null){try {stmt2.close();}catch(SQLException e){System.out.println("stmt2 is not close " + e);}}
				if(stmt3 != null){try {stmt3.close();}catch(SQLException e){System.out.println("stmt3 is not close " + e);}}
				if(stmt4 != null){try {stmt4.close();}catch(SQLException e){System.out.println("stmt4 is not close " + e);}}
				if(dbcon != null){try {dbcon.close();}catch(SQLException e){System.out.println("conn is not close " + e);}}
			}
		}
		try 
		{
			response.sendRedirect(url);
		}
		catch(IOException e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	private void InsertUpdateLinkCheckpostPlantDetail(HttpServletRequest request) throws SQLException
	{
		String function_nm="InsertUpdateLinkCheckpostPlantDetail()";
		msg="";
		msg_type="";
		url="";
		String opration="";
		
		try
		{
			opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String counterparty_cd=request.getParameter("customer_cd")==null?"":request.getParameter("customer_cd");
			String eff_dt=request.getParameter("eff_dt")==null?"":request.getParameter("eff_dt");
			String customer_plant=request.getParameter("customer_plant")==null?"":request.getParameter("customer_plant");
			String checkPost=request.getParameter("checkPost")==null?"":request.getParameter("checkPost");
			String checkPost_name = utilBean_dlng.getCheckPostName(dbcon,checkPost);
			String plant_abbr = utilBean.getCounterpartyPlantABBR(dbcon,counterparty_cd, comp_cd, customer_plant, "C");
			String deLink_trans = request.getParameter("deLink_trans")==null?"":request.getParameter("deLink_trans");
			String release_dt=request.getParameter("release_dt")==null?"":request.getParameter("release_dt");
			
			if(opration.equalsIgnoreCase("MODIFY"))
			{
				if(deLink_trans.equals("Y"))
				{
					if(!customer_plant.equals(""))
					{
						query = "UPDATE FMS_LINK_CHECKPOST_PLANT A SET RELEASE_DT=TO_DATE(?,'DD/MM/YYYY'),MODIFY_BY=?,MODIFY_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY_TYPE=? AND PLANT_SEQ_NO=? AND CHKPOST_CD=?"
								+ "AND A.REV_SEQ=(SELECT MAX(B.REV_SEQ) FROM FMS_LINK_CHECKPOST_PLANT B WHERE "
								+ "	A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY_TYPE=B.ENTITY_TYPE "
								+ "	AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.EFF_DT=B.EFF_DT)";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, release_dt);
						stmt.setString(2, emp_cd);
						stmt.setString(3, comp_cd);
						stmt.setString(4, counterparty_cd);
						stmt.setString(5, "C");
						stmt.setString(6, customer_plant);
						stmt.setString(7, checkPost);
						stmt.executeUpdate();
						
						stmt.close();
						
						msg = "Successful! - CheckPost "+checkPost_name+" DeLinked With Customer Plant "+plant_abbr+" !";
						msg_type="S";
					}
				}
				else
				{
					if(!customer_plant.equals("") && !checkPost.equals(""))
					{
						query = "UPDATE FMS_LINK_CHECKPOST_PLANT A SET CHKPOST_CD=?,EFF_DT=TO_DATE(?,'DD/MM/YYYY'),MODIFY_BY=?,MODIFY_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND PLANT_SEQ_NO=?"
								+ "AND A.REV_SEQ=(SELECT MAX(B.REV_SEQ) FROM FMS_LINK_CHECKPOST_PLANT B WHERE "
								+ "	A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY_TYPE=B.ENTITY_TYPE "
								+ "	AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.EFF_DT=B.EFF_DT)";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, checkPost);
						stmt.setString(2, eff_dt);
						stmt.setString(3, emp_cd);
						stmt.setString(4, comp_cd);
						stmt.setString(5, counterparty_cd);
						stmt.setString(6, customer_plant);
						stmt.executeUpdate();
						
						stmt.close();
						
						msg = "Successful! - CheckPost "+checkPost_name+" linking to "+plant_abbr+" Details updated !";
						msg_type="S";
					}
				}
			}
			else
			{
				int newRevSeq =0;
				
				String queryString2="SELECT REV_SEQ "
						+ "FROM FMS_LINK_CHECKPOST_PLANT A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND PLANT_SEQ_NO=? AND ENTITY_TYPE='C' "
						+ "AND (RELEASE_DT>=TO_DATE(SYSDATE,'DD/MM/YYYY') OR RELEASE_DT IS NULL) "
						+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY')"
						+ "AND A.REV_SEQ=(SELECT MAX(B.REV_SEQ) FROM FMS_LINK_CHECKPOST_PLANT B WHERE "
						+ "	A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY_TYPE=B.ENTITY_TYPE "
						+ "	AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.EFF_DT=B.EFF_DT)";
				stmt2=dbcon.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, counterparty_cd);
				stmt2.setString(3, customer_plant);
				stmt2.setString(4, eff_dt);
				rset2=stmt2.executeQuery();
				if(rset2.next()) 
				{
					int revSeq = rset2.getInt(1);
					
					newRevSeq = revSeq+1;
				}
				rset2.close();
				stmt2.close();
				
				query1 = "INSERT INTO FMS_LINK_CHECKPOST_PLANT(COMPANY_CD,COUNTERPARTY_CD,ENTITY_TYPE,PLANT_SEQ_NO,CHKPOST_CD,EFF_DT,ENT_BY,ENT_DT,REV_SEQ) "
						+ "VALUES(?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,SYSDATE,?)";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, "C");
				stmt1.setString(4, customer_plant);
				stmt1.setString(5, checkPost);
				stmt1.setString(6, eff_dt);
				stmt1.setString(7, emp_cd);
				stmt1.setString(8, ""+newRevSeq);
				stmt1.executeUpdate();
				
				stmt1.close();
				
				msg = "Successful! - CheckPost "+checkPost_name+" linking to "+plant_abbr+" Details Inserted !";
				msg_type="S";
				
			}
			url = "../dlng/cust_plant_check_post_link.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			if(opration.equalsIgnoreCase("MODIFY"))
			{
				msg = "Error in Exception! - Checkpost-2-Customer Plant Link Modification Failed!";
				msg_type="E";
			}
			else
			{
				msg = "Error in Exception! - Checkpost-2-Customer Plant Link Addition Failed!";
				msg_type="E";
			}
			url=CommonVariable.errorpage_url+"?e="+e;
		}
	}
	
	private void InsertUpdateBaySlotDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateBaySlotDetail()";
		msg="";
		msg_type="";
		url="";
		String opration="";
		
		try
		{
			opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String fill_station=request.getParameter("fill_station")==null?"":request.getParameter("fill_station");
			String eff_dt=request.getParameter("eff_dt")==null?"":request.getParameter("eff_dt");
			String status_flag=request.getParameter("status_flag")==null?"":request.getParameter("status_flag");
			String bay_cd=request.getParameter("bay_cd")==null?"":request.getParameter("bay_cd");
			String bay_name=request.getParameter("bay_name")==null?"":request.getParameter("bay_name");
			String slot_cald_type=request.getParameter("slot_cald_type")==null?"":request.getParameter("slot_cald_type");
			String slot_start_time=request.getParameter("slot_start_time")==null?"":request.getParameter("slot_start_time");
			String slot_interval=request.getParameter("slot_interval")==null?"":request.getParameter("slot_interval");
			
			if(opration.equalsIgnoreCase("MODIFY"))
			{
				if(!bay_cd.equals(""))
				{
					query = "UPDATE FMS_BAY_SLOT_MST SET BAY_NAME=?,EFF_DT=TO_DATE(?,'DD/MM/YYYY'),ACTIVE_FLAG=?,"
							+ "SLOT_CALD_TYPE=?,SLOT_START_TIME=?,SLOT_INTERVAL=?,"
							+ "MODIFY_BY=?,MODIFY_DT=SYSDATE,FILL_STATION_CD=? "
							+ "WHERE BAY_CD=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, bay_name);
					stmt.setString(2, eff_dt);
					stmt.setString(3, status_flag);
					stmt.setString(4, slot_cald_type);
					stmt.setString(5, slot_start_time);
					stmt.setString(6, slot_interval);
					stmt.setString(7, emp_cd);
					stmt.setString(8, fill_station);
					stmt.setString(9, bay_cd);
					stmt.executeUpdate();
					
					stmt.close();
					
					msg = "Successful! - Bay and Slot Details for "+bay_name+" updated !";
					msg_type="S";
				}
			}
			else
			{
				query = "SELECT NVL(MAX(BAY_CD),0) "
						+ "FROM FMS_BAY_SLOT_MST ";
				stmt=dbcon.prepareStatement(query);
				rset = stmt.executeQuery();
				if(rset.next())
				{
					bay_cd = ""+(rset.getInt(1)+1);
				}
				else
				{
					bay_cd="1";
				}
				rset.close();
				stmt.close();
				
				query1 = "INSERT INTO FMS_BAY_SLOT_MST(FILL_STATION_CD,BAY_CD,BAY_NAME,EFF_DT,"
						+ "ACTIVE_FLAG,SLOT_CALD_TYPE,SLOT_START_TIME,SLOT_INTERVAL,ENT_BY,ENT_DT) "
						+ "VALUES(?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,SYSDATE)";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, fill_station);
				stmt1.setString(2, bay_cd);
				stmt1.setString(3, bay_name);
				stmt1.setString(4, eff_dt);
				stmt1.setString(5, status_flag);
				stmt1.setString(6, slot_cald_type);
				stmt1.setString(7, slot_start_time);
				stmt1.setString(8, slot_interval);
				stmt1.setString(9, emp_cd);
				stmt1.executeUpdate();
				
				stmt1.close();
				
				msg = "Successful! - Bay and Slot Details for "+bay_name+" Inserted !";
				msg_type="S";
				
			}
			url = "../dlng/frm_bay_slot_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&fill_station="+fill_station+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			if(opration.equalsIgnoreCase("MODIFY"))
			{
				msg = "Error in Exception! - Bay and Slot Details Modification Failed!";
				msg_type="E";
			}
			else
			{
				msg = "Error in Exception! - Bay and Slot Details Addition Failed!";
				msg_type="E";
			}
			url=CommonVariable.errorpage_url+"?e="+e;
		}
	}
	
	private void InsertUpdatefillStationDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdatefillStationDetail()";
		msg="";
		msg_type="";
		url="";
		String opration="";
		
		try
		{
			opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String fill_station_cd=request.getParameter("fill_station_cd")==null?"":request.getParameter("fill_station_cd");
			String fill_station_name=request.getParameter("fill_station_name")==null?"":request.getParameter("fill_station_name");
			String eff_dt=request.getParameter("eff_dt")==null?"":request.getParameter("eff_dt");
			String fill_station_abbr=request.getParameter("fill_station_abbr")==null?"":request.getParameter("fill_station_abbr");
			String status_flag=request.getParameter("status_flag")==null?"":request.getParameter("status_flag");
			String countpty=countpty=request.getParameter("countpty_trader")==null?"":request.getParameter("countpty_trader");
			
			if(opration.equalsIgnoreCase("MODIFY"))
			{
				if(!fill_station_cd.equals(""))
				{
					query = "UPDATE FMS_FILLING_STATION_MST SET FILL_STATION_NAME=?,FILL_STATION_ABBR=?,"
							+ "EFF_DT=TO_DATE(?,'DD/MM/YYYY'),ACTIVE_FLAG=?,"
							+ "MODIFY_BY=?,MODIFY_DT=SYSDATE,COUNTERPARTY_CD=? "
							+ "WHERE FILL_STATION_CD=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, fill_station_name);
					stmt.setString(2, fill_station_abbr);
					stmt.setString(3, eff_dt);
					stmt.setString(4, status_flag);
					stmt.setString(5, emp_cd);
					stmt.setString(6, countpty);
					stmt.setString(7, fill_station_cd);
					stmt.executeUpdate();
					
					stmt.close();
					
					msg = "Successful! - Filling Station "+fill_station_name+" updated !";
					msg_type="S";
				}
			}
			else
			{
				query = "SELECT NVL(MAX(FILL_STATION_CD),0) "
						+ "FROM FMS_FILLING_STATION_MST ";
				stmt=dbcon.prepareStatement(query);
				rset = stmt.executeQuery();
				if(rset.next())
				{
					fill_station_cd = ""+(rset.getInt(1)+1);
				}
				else
				{
					fill_station_cd="1";
				}
				rset.close();
				stmt.close();
				
				query1 = "INSERT INTO FMS_FILLING_STATION_MST(COUNTERPARTY_CD,FILL_STATION_CD,EFF_DT,"
						+ "FILL_STATION_NAME,FILL_STATION_ABBR,ACTIVE_FLAG,ENT_BY,ENT_DT) "
						+ "VALUES(?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,SYSDATE)";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, countpty);
				stmt1.setString(2, fill_station_cd);
				stmt1.setString(3, eff_dt);
				stmt1.setString(4, fill_station_name);
				stmt1.setString(5, fill_station_abbr);
				stmt1.setString(6, status_flag);
				stmt1.setString(7, emp_cd);
				stmt1.executeUpdate();
				
				stmt1.close();
				
				msg = "Successful! - Fill Station "+fill_station_name+" Inserted !";
				msg_type="S";
				
			}
			url = "../dlng/frm_filling_mst.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			if(opration.equalsIgnoreCase("MODIFY"))
			{
				msg = "Error in Exception! - Fill Station Modification Failed!";
				msg_type="E";
			}
			else
			{
				msg = "Error in Exception! - Fill Station Addition Failed!";
				msg_type="E";
			}
			url=CommonVariable.errorpage_url+"?e="+e;
		}
	}
	
	private void InsertUpdateTruckTransContactDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateTruckTransContactDetail()";

		msg="";
		msg_type="";
		url="";
		String opration="";
		String truck_trans_cd="0";
		String truck_trans_nm = "";
		
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		
		try
		{
			opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			truck_trans_cd = request.getParameter("truck_trans_cd")==null?"0":request.getParameter("truck_trans_cd");
			truck_trans_nm = utilBean_dlng.getTruckTransName(dbcon,truck_trans_cd);
			
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
			
			String[] address_type = request.getParameterValues("address_type");
			String[] additional_address = request.getParameterValues("additional_address");
			String[] phone = request.getParameterValues("phone");
			String[] fax1 = request.getParameterValues("fax1");
			String[] fax2 = request.getParameterValues("fax2");
			String[] flag = request.getParameterValues("flag");
			
			if(opration.equals("INSERT"))
			{
				query = "SELECT NVL(MAX(SEQ_NO),0) FROM FMS_TRUCK_TRANS_CONTACT_MST "
						+ "WHERE COMPANY_CD=? AND TRUCK_TRANS_CD=? ";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, truck_trans_cd);
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
						int count =0;
						query = "SELECT COUNT(*) FROM FMS_TRUCK_TRANS_CONTACT_MST "
								+ "WHERE COMPANY_CD=? AND TRUCK_TRANS_CD=? "
								+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND SEQ_NO=? AND ADDR_FLAG=?";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, truck_trans_cd);
						stmt.setString(3, eff_dt);
						stmt.setString(4, seq_no);
						stmt.setString(5, address_type[i]);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							count=rset.getInt(1);
						}
						rset.close();
						stmt.close();
						
						if(count > 0)
						{
							query1 = "UPDATE FMS_TRUCK_TRANS_CONTACT_MST SET CONTACT_PERSON=?, DESIGNATION=?, PHONE=?, MOBILE=?,"
									+ "FAX_1=?, FAX_2=?, EMAIL=?, ADDL_ADDR_LINE=?, "
									+ "NOM_FLAG=?, INV_FLAG=?, FM_FLAG=?, PM_FLAG=?, JT_FLAG=?, OTHER_FLAG=?,RM_FLAG=?, "
									+ "ACTIVE_FLAG=?, MODIFY_DT=SYSDATE, MODIFY_BY=?,ADDR_IS_ACTIVE=? "
									+ "WHERE COMPANY_CD=? AND TRUCK_TRANS_CD=? AND SEQ_NO=? "
									+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND ADDR_FLAG=?";
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
							stmt1.setString(19, comp_cd);
							stmt1.setString(20, truck_trans_cd);
							stmt1.setString(21, seq_no);
							stmt1.setString(22, eff_dt);
							stmt1.setString(23, address_type[i]);
							stmt1.executeUpdate();
							
							stmt1.close();
						}
						else
						{
							query1 = "INSERT INTO FMS_TRUCK_TRANS_CONTACT_MST(COMPANY_CD,TRUCK_TRANS_CD,SEQ_NO,EFF_DT,CONTACT_PERSON,"
									+ "DESIGNATION,PHONE,MOBILE,FAX_1,FAX_2,EMAIL,ADDR_FLAG,ADDL_ADDR_LINE,"
									+ "NOM_FLAG,INV_FLAG,FM_FLAG,PM_FLAG,JT_FLAG,OTHER_FLAG,RM_FLAG,ACTIVE_FLAG,ENT_DT,ENT_BY,ADDR_IS_ACTIVE) "
									+ "VALUES(?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
									+ "?,?,?,?,?,?,?,?,"
									+ "?,?,?,?,?,?,?,?,SYSDATE,?,?)";
							stmt1=dbcon.prepareStatement(query1);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, truck_trans_cd);
							stmt1.setString(3, seq_no);
							stmt1.setString(4, eff_dt);
							stmt1.setString(5, person_name);
							stmt1.setString(6, designation);
							stmt1.setString(7, phone[i]);
							stmt1.setString(8, mobile);
							stmt1.setString(9, fax1[i]);
							stmt1.setString(10, fax2[i]);
							stmt1.setString(11, email);
							stmt1.setString(12, address_type[i]);
							stmt1.setString(13, additional_address[i]);
							stmt1.setString(14, nom);
							stmt1.setString(15, inv);
							stmt1.setString(16, fm);
							stmt1.setString(17, pm);
							stmt1.setString(18, jt);
							stmt1.setString(19, other);
							stmt1.setString(20, rm);
							stmt1.setString(21, status);
							stmt1.setString(22, emp_cd);
							stmt1.setString(23, flag[i]);
							stmt1.executeUpdate();
							
							stmt1.close();
						}
						
						if(opration.equals("MODIFY"))
						{
							msg = "Successful! - Contact person for "+truck_trans_nm+" Updated!";
							msg_type="S";
						}
						else
						{
							msg = "Successful! - Contact person for "+truck_trans_nm+" Added!";
							msg_type="S";
						}
					}
				}
				else
				{
					if(opration.equalsIgnoreCase("INSERT"))
					{
						msg = "Failed! - Contact person for "+truck_trans_nm+" Addition Failed!";
					}
					else
					{
						msg = "Failed! - Contact person for "+truck_trans_nm+" Modification Failed!";
					}
					msg_type="E";
				}
			}
			else
			{
				if(opration.equalsIgnoreCase("INSERT"))
				{
					msg = "Failed! - Truck Transporter Contact Addition Failed!";
				}
				else
				{
					msg = "Failed! - Truck Transporter Contact Modification Failed!";
				}
				msg_type="E";
			}
			
			opration="INSERT";
			dbcon.commit();
			url = "../dlng/frm_truck_trans_contact_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&truck_trans_cd="+truck_trans_cd+"&opration="+opration+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			msg = "Error in Exception! - Entity Contact Addition/Modification Failed!";
			url=CommonVariable.errorpage_url+"?e="+e;
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
		}
	}
	private void InsertUpdateCheckpostDtls(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateCheckpostDtls()";
		msg="";
		msg_type="";
		url="";
		String opration="";
		
		try
		{
			opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String checkpost_cd=request.getParameter("checkpost_cd")==null?"":request.getParameter("checkpost_cd");
			String checkpost_name=request.getParameter("checkpost_name")==null?"":request.getParameter("checkpost_name");
			String eff_dt=request.getParameter("eff_dt")==null?"":request.getParameter("eff_dt");
			String state_cd=request.getParameter("state_cd")==null?"":request.getParameter("state_cd");
			String status_flag=request.getParameter("status_flag")==null?"":request.getParameter("status_flag");
			
			if(opration.equalsIgnoreCase("MODIFY"))
			{
				if(!checkpost_cd.equals(""))
				{
					query = "UPDATE FMS_CHECKPOST_MST SET CHKPOST_NAME=?,STATE_CODE=?,EFF_DT=TO_DATE(?,'DD/MM/YYYY'),ACTIVE_FLAG=?,"
							+ "MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
							+ "WHERE CHKPOST_CD=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, checkpost_name);
					stmt.setString(2, state_cd);
					stmt.setString(3, eff_dt);
					stmt.setString(4, status_flag);
					stmt.setString(5, emp_cd);
					stmt.setString(6, comp_cd);
					stmt.setString(7, checkpost_cd);
					stmt.executeUpdate();
					
					stmt.close();
					
					msg = "Successful! - CheckPost "+checkpost_name+" updated !";
					msg_type="S";
				}
			}
			else
			{
				query = "SELECT NVL(MAX(CHKPOST_CD),0) "
						+ "FROM FMS_CHECKPOST_MST ";	
				stmt=dbcon.prepareStatement(query);
				rset = stmt.executeQuery();
				if(rset.next())
				{
					checkpost_cd = ""+(rset.getInt(1)+1);
				}
				else
				{
					checkpost_cd="1";
				}
				rset.close();
				stmt.close();
				
				query1 = "INSERT INTO FMS_CHECKPOST_MST(CHKPOST_CD,CHKPOST_NAME,STATE_CODE,EFF_DT,ACTIVE_FLAG,ENT_BY,ENT_DT,ENT_PROFILE) "
						+ "VALUES(?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,SYSDATE,?)";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, checkpost_cd);
				stmt1.setString(2, checkpost_name);
				stmt1.setString(3, state_cd);
				stmt1.setString(4, eff_dt);
				stmt1.setString(5, status_flag);
				stmt1.setString(6, emp_cd);
				stmt1.setString(7, comp_cd);
				stmt1.executeUpdate();
				
				stmt1.close();
				
				msg = "Successful! - CkeckPost "+checkpost_name+" Inserted !";
				msg_type="S";
				
			}
			url = "../dlng/frm_checkpost_mst.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			if(opration.equalsIgnoreCase("MODIFY"))
			{
				msg = "Error in Exception! - CheckPost Modification Failed!";
				msg_type="E";
			}
			else
			{
				msg = "Error in Exception! - CheckPost Addition Failed!";
				msg_type="E";
			}
			url=CommonVariable.errorpage_url+"?e="+e;
		}
	}
	private void InsertUpdateLinkDriverTruckDtls(HttpServletRequest request) throws SQLException
	{
		String function_nm="InsertUpdateLinkDriverTruckDtls()";
		msg="";
		msg_type="";
		url="";
		String opration="";
		
		try
		{
			opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String driver_cd=request.getParameter("driver_cd")==null?"":request.getParameter("driver_cd");
			String driver_name = request.getParameter("driver_name")==null?"":request.getParameter("driver_name");
			String truck_cd=request.getParameter("truck_cd")==null?"":request.getParameter("truck_cd");
			String eff_dt=request.getParameter("eff_dt")==null?"":request.getParameter("eff_dt");
			String release_dt=request.getParameter("release_dt")==null?"":request.getParameter("release_dt");
			String remark=request.getParameter("remark")==null?"":request.getParameter("remark");
			String truck_reg_no=utilBean_dlng.getTruckRegNo(dbcon,truck_cd);
			String deLink_trans = request.getParameter("deLink_trans")==null?"":request.getParameter("deLink_trans");
			String overwrite_flg = request.getParameter("overwrite_flg")==null?"":request.getParameter("overwrite_flg");
			String link_seq_no = request.getParameter("link_seq_no")==null?"":request.getParameter("link_seq_no");
			String deLink_dt=utilDate.getPreviousDate();
			
			if(opration.equalsIgnoreCase("MODIFY"))
			{
				if(deLink_trans.equals("Y"))
				{
					if(!driver_cd.equals(""))
					{
						query = "UPDATE FMS_TRUCK_DRIVER_LINK SET RELEASE_DT=TO_DATE(?,'DD/MM/YYYY'),R_REMARKS=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
								+ "WHERE DRIVER_CD=? AND TRUCK_CD=? AND LINK_SEQ=?";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, release_dt);
						stmt.setString(2, remark);
						stmt.setString(3, emp_cd);
						stmt.setString(4, comp_cd);
						stmt.setString(5, driver_cd);
						stmt.setString(6, truck_cd);
						stmt.setString(7, link_seq_no);
						stmt.executeUpdate();
						
						stmt.close();
						
						msg = "Successful! - Driver "+driver_name+" Linked With Truck "+truck_reg_no+" Details Modified!";
						msg_type="S";
						
					}
				}
				else
				{
					if(!driver_cd.equals("") && !truck_cd.equals(""))
					{
						query = "UPDATE FMS_TRUCK_DRIVER_LINK SET EFF_DT=TO_DATE(?,'DD/MM/YYYY'),REMARKS=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
								+ "WHERE DRIVER_CD=? AND TRUCK_CD=? AND LINK_SEQ=?";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, eff_dt);
						//stmt.setString(2, release_dt);
						stmt.setString(2, remark);
						stmt.setString(3, emp_cd);
						stmt.setString(4, comp_cd);
						stmt.setString(5, driver_cd);
						stmt.setString(6, truck_cd);
						stmt.setString(7, link_seq_no);
						stmt.executeUpdate();
						
						stmt.close();
						
						msg = "Successful! - Driver "+driver_name+" Linked With Truck "+truck_reg_no+" Details Modified!";
						msg_type="S";
					}
				}
			}
			else
			{
				// commented Below part as seq_no logic implemented by AP20250519
				/*if(overwrite_flg.equals("Y"))
				{
					query = "UPDATE FMS_TRUCK_DRIVER_LINK SET EFF_DT=TO_DATE(?,'DD/MM/YYYY'),REMARKS=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
							+ "WHERE DRIVER_CD=? AND TRUCK_CD=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND LINK_SEQ=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, eff_dt);
					//stmt.setString(2, release_dt);
					stmt.setString(2, remark);
					stmt.setString(3, emp_cd);
					stmt.setString(4, comp_cd);
					stmt.setString(5, driver_cd);
					stmt.setString(6, truck_cd);
					stmt.setString(7, eff_dt);
					stmt.setString(8, link_seq_no);
					stmt.executeUpdate();
					
					stmt.close();
				}
				else*/
				{
					
					String queryString = "SELECT NVL(MAX(LINK_SEQ),0) FROM FMS_TRUCK_DRIVER_LINK "
							+ "WHERE DRIVER_CD=? ";
					stmt=dbcon.prepareStatement(queryString);
					stmt.setString(1, driver_cd);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						link_seq_no = ""+(rset.getInt(1)+1);
					}
					else
					{
						link_seq_no = "1";
					}
					rset.close();
					stmt.close();
					
					query1 = "INSERT INTO FMS_TRUCK_DRIVER_LINK(DRIVER_CD,TRUCK_CD,EFF_DT,REMARKS,ENT_BY,ENT_DT,ENT_PROFILE,LINK_SEQ) "
							+ "VALUES(?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
							+ "?,SYSDATE,?,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, driver_cd);
					stmt1.setString(2, truck_cd);
					stmt1.setString(3, eff_dt);
					//stmt1.setString(4, release_dt);
					stmt1.setString(4, remark);
					stmt1.setString(5, emp_cd);
					stmt1.setString(6, comp_cd);
					stmt1.setString(7, link_seq_no);
					stmt1.executeUpdate();
					
					stmt1.close();
				}
				
				msg = "Successful! - Driver "+driver_name+" Linked With Truck "+truck_reg_no+" Details Inserted!";
				msg_type="S";
				
			}
			url = "../dlng/frm_link_truck_driver.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			if(opration.equalsIgnoreCase("MODIFY"))
			{
				msg = "Error in Exception! - Modification Of Driver Linking To Truck Failed!";
				msg_type="E";
			}
			else
			{
				msg = "Error in Exception! - Addition Of Driver Linking To Truck Failed!";
				msg_type="E";
			}
			url=CommonVariable.errorpage_url+"?e="+e;
		}
	}
	private void InsertUpdateLinkDriverTransDtls(HttpServletRequest request) throws SQLException
	{
		String function_nm="InsertUpdateLinkDriverTransDtls()";
		msg="";
		msg_type="";
		url="";
		String opration="";
		
		try
		{
			opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String driver_cd=request.getParameter("driver_cd")==null?"":request.getParameter("driver_cd");
			String driver_name = request.getParameter("driver_name")==null?"":request.getParameter("driver_name");
			String truck_trans_cd=request.getParameter("truck_trans_cd")==null?"":request.getParameter("truck_trans_cd");
			String eff_dt=request.getParameter("eff_dt")==null?"":request.getParameter("eff_dt");
			String release_dt=request.getParameter("release_dt")==null?"":request.getParameter("release_dt");
			String remark=request.getParameter("remark")==null?"":request.getParameter("remark");
			String truck_trans_name=utilBean_dlng.getTruckTransName(dbcon,truck_trans_cd);
			String deLink_trans = request.getParameter("deLink_trans")==null?"":request.getParameter("deLink_trans");
			String overwrite_flg = request.getParameter("overwrite_flg")==null?"":request.getParameter("overwrite_flg");
			String link_seq_no = request.getParameter("link_seq_no")==null?"":request.getParameter("link_seq_no");
			String deLink_dt=utilDate.getPreviousDate();
			
			if(opration.equalsIgnoreCase("MODIFY"))
			{
				if(deLink_trans.equals("Y"))
				{
					if(!driver_cd.equals(""))
					{
						query = "UPDATE FMS_TRUCK_DRIVER_TRANS_LINK SET RELEASE_DT=TO_DATE(?,'DD/MM/YYYY'),R_REMARKS=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
								+ "WHERE DRIVER_CD=? AND TRUCK_TRANS_CD=? AND LINK_SEQ=?";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, release_dt);
						stmt.setString(2, remark);
						stmt.setString(3, emp_cd);
						stmt.setString(4, comp_cd);
						stmt.setString(5, driver_cd);
						stmt.setString(6, truck_trans_cd);
						stmt.setString(7, link_seq_no);
						stmt.executeUpdate();
						
						stmt.close();
						
						msg = "Successful! - Driver "+driver_name+" Linked With Transporter "+truck_trans_name+" Details Modified!";
						msg_type="S";
						
						int driver_cnt=0;
						query1="SELECT COUNT(DRIVER_CD) "
								+ "FROM FMS_TRUCK_DRIVER_LINK "
								+ "WHERE DRIVER_CD=? AND (RELEASE_DT>SYSDATE OR RELEASE_DT IS NULL)";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(1, driver_cd);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							driver_cnt=rset1.getInt(1);
						}
						rset1.close();
						stmt1.close();
						
						if(driver_cnt>0)
						{
							query2 = "UPDATE FMS_TRUCK_DRIVER_LINK SET RELEASE_DT=TO_DATE(?,'DD/MM/YYYY'),REMARKS=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
									+ "WHERE DRIVER_CD=? AND (RELEASE_DT>SYSDATE OR RELEASE_DT IS NULL)";
							stmt2=dbcon.prepareStatement(query2);
							stmt2.setString(1, release_dt);
							stmt2.setString(2, remark);
							stmt2.setString(3, emp_cd);
							stmt2.setString(4, comp_cd);
							stmt2.setString(5, driver_cd);
							stmt2.executeUpdate();
							
							stmt2.close();
						}
					}
				}
				else
				{
					if(!driver_cd.equals("") && !truck_trans_cd.equals(""))
					{
						query = "UPDATE FMS_TRUCK_DRIVER_TRANS_LINK SET EFF_DT=TO_DATE(?,'DD/MM/YYYY'),REMARKS=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
								+ "WHERE DRIVER_CD=? AND TRUCK_TRANS_CD=? AND LINK_SEQ=?";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, eff_dt);
						//stmt.setString(2, release_dt);
						stmt.setString(2, remark);
						stmt.setString(3, emp_cd);
						stmt.setString(4, comp_cd);
						stmt.setString(5, driver_cd);
						stmt.setString(6, truck_trans_cd);
						stmt.setString(7, link_seq_no);
						stmt.executeUpdate();
						
						stmt.close();
						
						msg = "Successful! - Driver "+driver_name+" Linked With Transporter "+truck_trans_name+" Details Modified!";
						msg_type="S";
					}
				}
			}
			else
			{
				// commented Below part as seq_no logic implemented by AP20250519
				/*if(overwrite_flg.equals("Y"))
				{
					query = "UPDATE FMS_TRUCK_DRIVER_TRANS_LINK SET EFF_DT=TO_DATE(?,'DD/MM/YYYY'),REMARKS=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
							+ "WHERE DRIVER_CD=? AND TRUCK_TRANS_CD=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND LINK_SEQ=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, eff_dt);
					//stmt.setString(2, release_dt);
					stmt.setString(2, remark);
					stmt.setString(3, emp_cd);
					stmt.setString(4, comp_cd);
					stmt.setString(5, driver_cd);
					stmt.setString(6, truck_trans_cd);
					stmt.setString(7, eff_dt);
					stmt.setString(8, link_seq_no);
					stmt.executeUpdate();
					
					stmt.close();
				}
				else*/
				{
					String queryString = "SELECT NVL(MAX(LINK_SEQ),0) FROM FMS_TRUCK_DRIVER_TRANS_LINK "
							+ "WHERE DRIVER_CD=? ";
					stmt=dbcon.prepareStatement(queryString);
					stmt.setString(1, driver_cd);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						link_seq_no = ""+(rset.getInt(1)+1);
					}
					else
					{
						link_seq_no = "1";
					}
					rset.close();
					stmt.close();
					
					query1 = "INSERT INTO FMS_TRUCK_DRIVER_TRANS_LINK(DRIVER_CD,TRUCK_TRANS_CD,EFF_DT,REMARKS,ENT_BY,ENT_DT,ENT_PROFILE,LINK_SEQ) "
							+ "VALUES(?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
							+ "?,SYSDATE,?,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, driver_cd);
					stmt1.setString(2, truck_trans_cd);
					stmt1.setString(3, eff_dt);
					//stmt1.setString(4, release_dt);
					stmt1.setString(4, remark);
					stmt1.setString(5, emp_cd);
					stmt1.setString(6, comp_cd);
					stmt1.setString(7, link_seq_no);
					stmt1.executeUpdate();
					
					stmt1.close();
				}
				
				msg = "Successful! - Driver "+driver_name+" Linked With Transporter "+truck_trans_name+" Details Inserted!";
				msg_type="S";
				
			}
			url = "../dlng/frm_link_driver_transporter.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			if(opration.equalsIgnoreCase("MODIFY"))
			{
				msg = "Error in Exception! - Modification Of Driver Linking Failed!";
				msg_type="E";
			}
			else
			{
				msg = "Error in Exception! - Addition Of Driver Linking Failed!";
				msg_type="E";
			}
			url=CommonVariable.errorpage_url+"?e="+e;
		}
	}
	
	private void InsertUpdateLinkTruckTransDtls(HttpServletRequest request) throws SQLException
	{
		String function_nm="InsertUpdateLinkTruckTransDtls()";
		msg="";
		msg_type="";
		url="";
		String opration="";
		
		try
		{
			opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String truck_cd=request.getParameter("truck_cd")==null?"":request.getParameter("truck_cd");
			String truck_reg_no = request.getParameter("truck_reg_no")==null?"":request.getParameter("truck_reg_no");
			String truck_trans_cd=request.getParameter("truck_trans_cd")==null?"":request.getParameter("truck_trans_cd");
			String eff_dt=request.getParameter("eff_dt")==null?"":request.getParameter("eff_dt");
			String release_dt=request.getParameter("release_dt")==null?"":request.getParameter("release_dt");
			String remark=request.getParameter("remark")==null?"":request.getParameter("remark");
			String truck_trans_name=utilBean_dlng.getTruckTransName(dbcon,truck_trans_cd);
			String deLink_trans = request.getParameter("deLink_trans")==null?"":request.getParameter("deLink_trans");
			String overwrite_flg = request.getParameter("overwrite_flg")==null?"":request.getParameter("overwrite_flg");
			String link_seq_no = request.getParameter("link_seq_no")==null?"":request.getParameter("link_seq_no");
			String deLink_dt=utilDate.getPreviousDate();
			
			if(opration.equalsIgnoreCase("MODIFY"))
			{
				if(deLink_trans.equals("Y"))
				{
					if(!truck_cd.equals(""))
					{
						query = "UPDATE FMS_TRUCK_TRANSPORTER_LINK SET RELEASE_DT=TO_DATE(?,'DD/MM/YYYY'),R_REMARKS=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
								+ "WHERE TRUCK_CD=? AND TRUCK_TRANS_CD=? AND LINK_SEQ=?";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, release_dt);
						stmt.setString(2, remark);
						stmt.setString(3, emp_cd);
						stmt.setString(4, comp_cd);
						stmt.setString(5, truck_cd);
						stmt.setString(6, truck_trans_cd);
						stmt.setString(7, link_seq_no);
						stmt.executeUpdate();
						
						stmt.close();
						
						msg = "Successful! - Truck "+truck_reg_no+" DeLinked With Transporter "+truck_trans_name+" !";
						msg_type="S";
						
						int truck_cnt=0;
						query1="SELECT COUNT(TRUCK_CD) "
								+ "FROM FMS_TRUCK_DRIVER_LINK "
								+ "WHERE TRUCK_CD=? AND (RELEASE_DT>SYSDATE OR RELEASE_DT IS NULL)";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(1, truck_cd);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							truck_cnt=rset1.getInt(1);
						}
						rset1.close();
						stmt1.close();
						
						if(truck_cnt>0)
						{
							query2 = "UPDATE FMS_TRUCK_DRIVER_LINK SET RELEASE_DT=TO_DATE(?,'DD/MM/YYYY'),REMARKS=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
									+ "WHERE TRUCK_CD=? AND (RELEASE_DT>SYSDATE OR RELEASE_DT IS NULL)";
							stmt2=dbcon.prepareStatement(query2);
							stmt2.setString(1, release_dt);
							stmt2.setString(2, remark);
							stmt2.setString(3, emp_cd);
							stmt2.setString(4, comp_cd);
							stmt2.setString(5, truck_cd);
							stmt2.executeUpdate();
							
							stmt2.close();
						}
					}
				}
				else
				{
					if(!truck_cd.equals("") && !truck_trans_cd.equals(""))
					{
						query = "UPDATE FMS_TRUCK_TRANSPORTER_LINK SET EFF_DT=TO_DATE(?,'DD/MM/YYYY'),REMARKS=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
								+ "WHERE TRUCK_CD=? AND TRUCK_TRANS_CD=? AND LINK_SEQ=?";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, eff_dt);
						//stmt.setString(2, release_dt);
						stmt.setString(2, remark);
						stmt.setString(3, emp_cd);
						stmt.setString(4, comp_cd);
						stmt.setString(5, truck_cd);
						stmt.setString(6, truck_trans_cd);
						stmt.setString(7, link_seq_no);
						stmt.executeUpdate();
						
						stmt.close();
						
						msg = "Successful! - Truck "+truck_reg_no+" Linked With Transporter "+truck_trans_name+" Details Modified!";
						msg_type="S";
					}
				}
			}
			else
			{
				// commented Below part as seq_no logic implemented by AP20250519
				/*if(overwrite_flg.equals("Y"))
				{
					query = "UPDATE FMS_TRUCK_TRANSPORTER_LINK SET EFF_DT=TO_DATE(?,'DD/MM/YYYY'),REMARKS=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
							+ "WHERE TRUCK_CD=? AND TRUCK_TRANS_CD=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND LINK_SEQ=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, eff_dt);
					//stmt.setString(2, release_dt);
					stmt.setString(2, remark);
					stmt.setString(3, emp_cd);
					stmt.setString(4, comp_cd);
					stmt.setString(5, truck_cd);
					stmt.setString(6, truck_trans_cd);
					stmt.setString(7, eff_dt);
					stmt.setString(8, link_seq_no);
					stmt.executeUpdate();
					
					stmt.close();
				}
				else*/
				{
					String queryString = "SELECT NVL(MAX(LINK_SEQ),0) FROM FMS_TRUCK_TRANSPORTER_LINK "
							+ "WHERE TRUCK_CD=? ";
					stmt=dbcon.prepareStatement(queryString);
					stmt.setString(1, truck_cd);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						link_seq_no = ""+(rset.getInt(1)+1);
					}
					else
					{
						link_seq_no = "1";
					}
					rset.close();
					stmt.close();
					
					query1 = "INSERT INTO FMS_TRUCK_TRANSPORTER_LINK(TRUCK_CD,TRUCK_TRANS_CD,EFF_DT,REMARKS,ENT_BY,ENT_DT,ENT_PROFILE,LINK_SEQ) "
							+ "VALUES(?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
							+ "?,SYSDATE,?,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, truck_cd);
					stmt1.setString(2, truck_trans_cd);
					stmt1.setString(3, eff_dt);
					//stmt1.setString(4, release_dt);
					stmt1.setString(4, remark);
					stmt1.setString(5, emp_cd);
					stmt1.setString(6, comp_cd);
					stmt1.setString(7, link_seq_no);
					stmt1.executeUpdate();
					
					stmt1.close();
				}
				
				msg = "Successful! - Truck "+truck_reg_no+" Linked With Transporter "+truck_trans_name+" Details Inserted!";
				msg_type="S";
				
			}
			url = "../dlng/frm_link_truck_transporter.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			if(opration.equalsIgnoreCase("MODIFY"))
			{
				msg = "Error in Exception! - Modification Of Truck Linking Failed!";
				msg_type="E";
			}
			else
			{
				msg = "Error in Exception! - Addition Of Truck Linking Failed!";
				msg_type="E";
			}
			url=CommonVariable.errorpage_url+"?e="+e;
		}
	}
	private void InsertUpdateTruckDriverDtls(HttpServletRequest request) throws SQLException
	{
		String function_nm="InsertUpdateTruckDriverDtls()";
		msg="";
		msg_type="";
		url="";
		String opration="";
		
		try
		{
			opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String driver_name=request.getParameter("driver_name")==null?"":request.getParameter("driver_name");
			String driver_cd=request.getParameter("driver_cd")==null?"":request.getParameter("driver_cd");
			String status_flag=request.getParameter("status_flag")==null?"":request.getParameter("status_flag");
			String driver_mobile=request.getParameter("driver_mobile")==null?"":request.getParameter("driver_mobile");
			String driver_dob=request.getParameter("driver_dob")==null?"0":request.getParameter("driver_dob");
			String driver_addr=request.getParameter("driver_addr")==null?"":request.getParameter("driver_addr");
			String license_no=request.getParameter("license_no")==null?"":request.getParameter("license_no");
			String license_type=request.getParameter("license_type")==null?"":request.getParameter("license_type");
			String eff_dt=request.getParameter("eff_dt")==null?"":request.getParameter("eff_dt");
			
			String license_from_dt=request.getParameter("license_from_dt")==null?"":request.getParameter("license_from_dt");
			String license_to_dt=request.getParameter("license_to_dt")==null?"":request.getParameter("license_to_dt");
			String license_iss_state=request.getParameter("license_iss_state")==null?"":request.getParameter("license_iss_state");
			String file_upload=request.getParameter("file_upload")==null?"":request.getParameter("file_upload");
			String link_trans = request.getParameter("link_trans")==null?"":request.getParameter("link_trans");
			String truck_trans_cd=request.getParameter("truck_trans_cd")==null?"":request.getParameter("truck_trans_cd");
			String truck_trans_eff_dt=request.getParameter("truck_trans_eff_dt")==null?"":request.getParameter("truck_trans_eff_dt");
			String linked_flg=request.getParameter("linked_flg")==null?"":request.getParameter("linked_flg");
			String upload_file_name="";
			
			String appPath = request.getServletContext().getRealPath("");
	    	
	    	String main_folder=CommonVariable.work_dir;
			File MainDir = new File(appPath+File.separator+main_folder);
	        if(!MainDir.exists()) 
	        {
	        	MainDir.mkdir();
	        }
	        
			String savePath = appPath+File.separator+main_folder+File.separator+SAVE_DIR;
			File fileSaveDir = new File(savePath);
	        if(!fileSaveDir.exists()) 
	        {
	            fileSaveDir.mkdir();
	        }
			
	        String subSavePath = savePath+File.separator+"driver_license";
	        File subfile = new File(subSavePath);
	        if(!subfile.exists())
	        {
	        	subfile.mkdir();
	        }
	        LocalDateTime dateObj = LocalDateTime.now();
	        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
	        String date = dateObj.format(formatter1);
	        
	        String file_name="";
	        String fileName="";
	        
	        for (Part part : request.getParts()) 
	        {
	        	fileName = extractFileName(part);
	        	//Refines the fileName in case it is an absolute path
			    fileName = new File(fileName).getName().replace(".pdf", "");
			    if(!fileName.equals("") )
			    {
			    	file_name=fileName;
			    	part.write(subSavePath +File.separator+ fileName+"_"+date+".pdf");
			    	upload_file_name=fileName+"_"+date+".pdf";
			    } 
	        }
	        /*if(!fileName.equals("") ) // No need to use
		    {
				SimpleDateFormat formatter = new SimpleDateFormat("DD/MM/YYYY");
					
				String file_path =subSavePath+File.separator+file_name+"_"+date+".pdf";
				//FileInputStream file = new FileInputStream(new File(file_path));
		    }*/
		        
			if(opration.equalsIgnoreCase("MODIFY"))
			{
				if(!driver_cd.equals(""))
				{
					int cnt=0;
					query = "UPDATE FMS_TRUCK_DRIVER_MST SET DRIVER_NAME=?,DRIVER_ADDR=?,DRIVER_DOB=TO_DATE(?,'DD/MM/YYYY'),DRIVER_STATUS=?,DRIVER_MOBILE=?,LICENCE_NO=?,"
							+ "LICENCE_TYPE=?,LICENCE_FROM_DT=TO_DATE(?,'DD/MM/YYYY'),LICENCE_TO_DT=TO_DATE(?,'DD/MM/YYYY'),LICENCE_ISSUE_STATE=?,EFF_DT=TO_DATE(?,'DD/MM/YYYY'), ";
					if(!upload_file_name.equals(""))
					{
						query += "LICENCE_FILE_NAME=?,";
					}
					query += "MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
						+ "WHERE DRIVER_CD=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(++cnt, driver_name);
					stmt.setString(++cnt, driver_addr);
					stmt.setString(++cnt, driver_dob);
					stmt.setString(++cnt, status_flag);
					stmt.setString(++cnt, driver_mobile);
					stmt.setString(++cnt, license_no);
					stmt.setString(++cnt, license_type);
					stmt.setString(++cnt, license_from_dt);
					stmt.setString(++cnt, license_to_dt);
					stmt.setString(++cnt, license_iss_state);
					stmt.setString(++cnt, eff_dt);
					if(!upload_file_name.equals(""))
					{
						stmt.setString(++cnt, upload_file_name);
					}
					stmt.setString(++cnt, emp_cd);
					stmt.setString(++cnt, comp_cd);
					stmt.setString(++cnt, driver_cd);
					stmt.executeUpdate();
					
					stmt.close();
					
					msg = "Successful! - Driver "+driver_name+" Details updated !";
					msg_type="S";
				}
			}
			else
			{
				
				query = "SELECT NVL(MAX(DRIVER_CD),0) "
						+ "FROM FMS_TRUCK_DRIVER_MST ";	
				stmt=dbcon.prepareStatement(query);
				rset = stmt.executeQuery();
				if(rset.next())
				{
					driver_cd = ""+(rset.getInt(1)+1);
				}
				else
				{
					driver_cd="1";
				}
				rset.close();
				stmt.close();
				
				query1 = "INSERT INTO FMS_TRUCK_DRIVER_MST(DRIVER_CD,DRIVER_NAME,DRIVER_ADDR,DRIVER_DOB,DRIVER_STATUS,DRIVER_MOBILE,LICENCE_NO,"
						+ "LICENCE_TYPE,LICENCE_FROM_DT,LICENCE_TO_DT,LICENCE_ISSUE_STATE,LICENCE_FILE_NAME,"
						+ "ENT_BY,ENT_DT,ENT_PROFILE,EFF_DT) "
						+ "VALUES(?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,"
						+ "?,SYSDATE,?,TO_DATE(?,'DD/MM/YYYY'))";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, driver_cd);
				stmt1.setString(2, driver_name);
				stmt1.setString(3, driver_addr);
				stmt1.setString(4, driver_dob);
				stmt1.setString(5, status_flag);
				stmt1.setString(6, driver_mobile);
				stmt1.setString(7, license_no);
				stmt1.setString(8, license_type);
				stmt1.setString(9, license_from_dt);
				stmt1.setString(10, license_to_dt);
				stmt1.setString(11, license_iss_state);
				stmt1.setString(12, upload_file_name);
				stmt1.setString(13, emp_cd);
				stmt1.setString(14, comp_cd);
				stmt1.setString(15, eff_dt);
				stmt1.executeUpdate();
				
				stmt1.close();
				
				msg = "Successful! - Truck "+driver_name+" Details Inserted !";
				msg_type="S";
				
			}
			if(link_trans.equals("Y") && !driver_cd.equals("") && !linked_flg.equals("Y"))
			{
				String link_seq_no="";
				String queryString = "SELECT NVL(MAX(LINK_SEQ),0) FROM FMS_TRUCK_DRIVER_TRANS_LINK "
						+ "WHERE DRIVER_CD=? ";
				stmt=dbcon.prepareStatement(queryString);
				stmt.setString(1, driver_cd);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					link_seq_no = ""+(rset.getInt(1)+1);
				}
				else
				{
					link_seq_no = "1";
				}
				rset.close();
				stmt.close();
				
				query1 = "INSERT INTO FMS_TRUCK_DRIVER_TRANS_LINK(DRIVER_CD,TRUCK_TRANS_CD,EFF_DT,REMARKS,ENT_BY,ENT_DT,ENT_PROFILE,LINK_SEQ) "
						+ "VALUES(?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
						+ "?,SYSDATE,?,?)";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, driver_cd);
				stmt1.setString(2, truck_trans_cd);
				stmt1.setString(3, truck_trans_eff_dt);
				stmt1.setString(4, "");
				stmt1.setString(5, emp_cd);
				stmt1.setString(6, comp_cd);
				stmt1.setString(7, link_seq_no);
				stmt1.executeUpdate();
				
				stmt1.close();
			}
			url = "../dlng/frm_truck_driver_mst.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			if(opration.equalsIgnoreCase("MODIFY"))
			{
				msg = "Error in Exception! - Driver Modification Failed!";
				msg_type="E";
			}
			else
			{
				msg = "Error in Exception! - Driver Addition Failed!";
				msg_type="E";
			}
			url=CommonVariable.errorpage_url+"?e="+e;
		}
	}
	private void InsertUpdateTruckDtls(HttpServletRequest request) throws SQLException
	{
		String function_nm="InsertUpdateTruckDtls()";
		msg="";
		msg_type="";
		url="";
		String opration="";
		
		try
		{
			opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String truck_reg_no=request.getParameter("truck_reg_no")==null?"":request.getParameter("truck_reg_no");
			String truck_cd=request.getParameter("truck_cd")==null?"":request.getParameter("truck_cd");
			String truck_type=request.getParameter("truck_type")==null?"":request.getParameter("truck_type");
			String eff_dt=request.getParameter("eff_dt")==null?"":request.getParameter("eff_dt");
			String status_flag=request.getParameter("status_flag")==null?"":request.getParameter("status_flag");
			String truck_vol_m3=request.getParameter("truck_vol_m3")==null?"":request.getParameter("truck_vol_m3");
			String truck_vol_mt=request.getParameter("truck_vol_mt")==null?"":request.getParameter("truck_vol_mt");
			String load_capacity=request.getParameter("load_capacity")==null?"":request.getParameter("load_capacity");
			String link_trans = request.getParameter("link_trans")==null?"":request.getParameter("link_trans");
			String truck_trans_cd=request.getParameter("truck_trans_cd")==null?"":request.getParameter("truck_trans_cd");
			String truck_trans_eff_dt=request.getParameter("truck_trans_eff_dt")==null?"":request.getParameter("truck_trans_eff_dt");
			String linked_flg=request.getParameter("linked_flg")==null?"":request.getParameter("linked_flg");
			
			
			if(opration.equalsIgnoreCase("MODIFY"))
			{
				if(!truck_cd.equals(""))
				{
					query = "UPDATE FMS_TRUCK_MST SET EFF_DT=TO_DATE(?,'DD/MM/YYYY'),TRUCK_REG_NUM=?,"
							+ "TRUCK_TYPE=?,TRUCK_VOL_M3=?,TRUCK_VOL_MT=?,TRUCK_LOAD_CAP=?,ACTIVE_FLAG=?,"
							+ "MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
							+ "WHERE TRUCK_CD=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, eff_dt);
					stmt.setString(2, truck_reg_no);
					stmt.setString(3, truck_type);
					stmt.setString(4, truck_vol_m3);
					stmt.setString(5, truck_vol_mt);
					stmt.setString(6, load_capacity);
					stmt.setString(7, status_flag);
					stmt.setString(8, emp_cd);
					stmt.setString(9, comp_cd);
					stmt.setString(10, truck_cd);
					stmt.executeUpdate();
					
					stmt.close();
					
					msg = "Successful! - Truck "+truck_reg_no+" Details updated !";
					msg_type="S";
				}
			}
			else
			{
				query = "SELECT NVL(MAX(TRUCK_CD),0) "
						+ "FROM FMS_TRUCK_MST ";	
				stmt=dbcon.prepareStatement(query);
				rset = stmt.executeQuery();
				if(rset.next())
				{
					truck_cd = ""+(rset.getInt(1)+1);
				}
				else
				{
					truck_cd="1";
				}
				rset.close();
				stmt.close();
				
				query1 = "INSERT INTO FMS_TRUCK_MST(TRUCK_CD,EFF_DT,TRUCK_REG_NUM,TRUCK_TYPE,TRUCK_VOL_M3,TRUCK_VOL_MT,TRUCK_LOAD_CAP,ACTIVE_FLAG,"
						+ "ENT_BY,ENT_DT,ENT_PROFILE) "
						+ "VALUES(?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,"
						+ "?,SYSDATE,?)";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, truck_cd);
				stmt1.setString(2, eff_dt);
				stmt1.setString(3, truck_reg_no);
				stmt1.setString(4, truck_type);
				stmt1.setString(5, truck_vol_m3);
				stmt1.setString(6, truck_vol_mt);
				stmt1.setString(7, load_capacity);
				stmt1.setString(8, status_flag);
				stmt1.setString(9, emp_cd);
				stmt1.setString(10, comp_cd);
				stmt1.executeUpdate();
				
				stmt1.close();
				
				msg = "Successful! - Truck "+truck_reg_no+" Details Inserted !";
				msg_type="S";
				
			}
			if(link_trans.equals("Y") && !truck_cd.equals("") && !linked_flg.equals("Y"))
			{
				String link_seq_no="";
				String queryString = "SELECT NVL(MAX(LINK_SEQ),0) FROM FMS_TRUCK_TRANSPORTER_LINK "
						+ "WHERE TRUCK_CD=? ";
				stmt=dbcon.prepareStatement(queryString);
				stmt.setString(1, truck_cd);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					link_seq_no = ""+(rset.getInt(1)+1);
				}
				else
				{
					link_seq_no = "1";
				}
				rset.close();
				stmt.close();
				
				query1 = "INSERT INTO FMS_TRUCK_TRANSPORTER_LINK(TRUCK_CD,TRUCK_TRANS_CD,EFF_DT,REMARKS,ENT_BY,ENT_DT,ENT_PROFILE,LINK_SEQ) "
						+ "VALUES(?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
						+ "?,SYSDATE,?,?)";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, truck_cd);
				stmt1.setString(2, truck_trans_cd);
				stmt1.setString(3, truck_trans_eff_dt);
				stmt1.setString(4, "");
				stmt1.setString(5, emp_cd);
				stmt1.setString(6, comp_cd);
				stmt1.setString(7, link_seq_no);
				stmt1.executeUpdate();
				
				stmt1.close();
			}
			url = "../dlng/frm_truck_mst.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			if(opration.equalsIgnoreCase("MODIFY"))
			{
				msg = "Error in Exception! - Truck Modification Failed!";
				msg_type="E";
			}
			else
			{
				msg = "Error in Exception! - Truck Addition Failed!";
				msg_type="E";
			}
			url=CommonVariable.errorpage_url+"?e="+e;
		}
	}
	private void InsertUpdateTruckTypeDtls(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateTruckTypeDtls()";
		msg="";
		msg_type="";
		url="";
		String opration="";
		
		try
		{
			opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String truck_type=request.getParameter("truck_type")==null?"":request.getParameter("truck_type");
			String eff_dt=request.getParameter("eff_dt")==null?"":request.getParameter("eff_dt");
			String status_flag=request.getParameter("status_flag")==null?"":request.getParameter("status_flag");
			String truck_vol_m3=request.getParameter("truck_vol_m3")==null?"":request.getParameter("truck_vol_m3");
			String truck_vol_mt=request.getParameter("truck_vol_mt")==null?"":request.getParameter("truck_vol_mt");
			String load_capacity=request.getParameter("load_capacity")==null?"":request.getParameter("load_capacity");
			
			if(opration.equalsIgnoreCase("MODIFY"))
			{
				if(!truck_type.equals(""))
				{
					query = "UPDATE FMS_TRUCK_TYPE_MST SET EFF_DT=TO_DATE(?,'DD/MM/YYYY'),ACTIVE_FLAG=?,TRUCK_VOL_M3=?,TRUCK_VOL_MT=?,TRUCK_LOAD_CAP=?,"
							+ "MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
							+ "WHERE TRUCK_TYPE=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, eff_dt);
					stmt.setString(2, status_flag);
					stmt.setString(3, truck_vol_m3);
					stmt.setString(4, truck_vol_mt);
					stmt.setString(5, load_capacity);
					stmt.setString(6, emp_cd);
					stmt.setString(7, comp_cd);
					stmt.setString(8, truck_type);
					stmt.executeUpdate();
					
					stmt.close();
					
					msg = "Successful! - Truck Type "+truck_type+" updated !";
					msg_type="S";
				}
			}
			else
			{
				query1 = "INSERT INTO FMS_TRUCK_TYPE_MST(TRUCK_TYPE,EFF_DT,ACTIVE_FLAG,ENT_BY,ENT_DT,ENT_PROFILE,TRUCK_VOL_M3,TRUCK_VOL_MT,TRUCK_LOAD_CAP) "
						+ "VALUES(?,TO_DATE(?,'DD/MM/YYYY'),?,?,SYSDATE,?,?,?,?)";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, truck_type);
				stmt1.setString(2, eff_dt);
				stmt1.setString(3, status_flag);
				stmt1.setString(4, emp_cd);
				stmt1.setString(5, comp_cd);
				stmt1.setString(6, truck_vol_m3);
				stmt1.setString(7, truck_vol_mt);
				stmt1.setString(8, load_capacity);
				stmt1.executeUpdate();
				
				stmt1.close();
				
				msg = "Successful! - Truck Type "+truck_type+" Inserted !";
				msg_type="S";
				
			}
			url = "../dlng/frm_truck_type_mst.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			if(opration.equalsIgnoreCase("MODIFY"))
			{
				msg = "Error in Exception! - Truck Type Modification Failed!";
				msg_type="E";
			}
			else
			{
				msg = "Error in Exception! - Truck Type Addition Failed!";
				msg_type="E";
			}
			url=CommonVariable.errorpage_url+"?e="+e;
		}
	}
	private void InsertUpdateTruckTransDtls(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateTruckTransDtls()";
		msg="";
		msg_type="";
		url="";
		String opration="";
		
		try
		{
			opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String truck_trans_nm=request.getParameter("truck_trans_nm")==null?"":request.getParameter("truck_trans_nm");
			String truck_trans_cd=request.getParameter("truck_trans_cd")==null?"":request.getParameter("truck_trans_cd");
			String eff_dt=request.getParameter("eff_dt")==null?"":request.getParameter("eff_dt");
			String truck_trans_abbr=request.getParameter("truck_trans_abbr")==null?"":request.getParameter("truck_trans_abbr");
			String status_flag=request.getParameter("status_flag")==null?"":request.getParameter("status_flag");
			String truck_trans_address=request.getParameter("truck_trans_address")==null?"":request.getParameter("truck_trans_address");
			String truck_trans_city=request.getParameter("truck_trans_city")==null?"":request.getParameter("truck_trans_city");
			String state_cd=request.getParameter("state_cd")==null?"":request.getParameter("state_cd");
			String truck_trans_pin=request.getParameter("truck_trans_pin")==null?"":request.getParameter("truck_trans_pin");
			
			String[] stat_cd = request.getParameterValues("stat_cd");
			String[] stat_no = request.getParameterValues("stat_no");
			String[] stat_eff_dt = request.getParameterValues("stat_eff_dt");
			String[] stat_remark = request.getParameterValues("stat_remark");
			
			if(opration.equalsIgnoreCase("MODIFY"))
			{
				if(!truck_trans_cd.equals(""))
				{
					query = "UPDATE FMS_TRUCK_TRANSPORTER_MST SET TRUCK_TRANS_NAME=?,TRUCK_TRANS_ABBR=?,EFF_DT=TO_DATE(?,'DD/MM/YYYY'),ADDR=?,"
							+ "CITY=?,PIN=?,STATE=?,ACTIVE_FLAG=?,"
							+ "MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
							+ "WHERE TRUCK_TRANS_CD=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, truck_trans_nm);
					stmt.setString(2, truck_trans_abbr);
					stmt.setString(3, eff_dt);
					stmt.setString(4, truck_trans_address);
					stmt.setString(5, truck_trans_city);
					stmt.setString(6, truck_trans_pin);
					stmt.setString(7, state_cd);
					stmt.setString(8, status_flag);
					stmt.setString(9, emp_cd);
					stmt.setString(10, comp_cd);
					stmt.setString(11, truck_trans_cd);
					stmt.executeUpdate();
					
					stmt.close();
					
					msg = "Successful! - Truck Transporter "+truck_trans_nm+" updated !";
					msg_type="S";
				}
			}
			else
			{
				query = "SELECT NVL(MAX(TRUCK_TRANS_CD),90000) "
						+ "FROM FMS_TRUCK_TRANSPORTER_MST ";	
				stmt=dbcon.prepareStatement(query);
				rset = stmt.executeQuery();
				if(rset.next())
				{
					truck_trans_cd = ""+(rset.getInt(1)+1);
				}
				else
				{
					truck_trans_cd="90001";
				}
				rset.close();
				stmt.close();
				
				query1 = "INSERT INTO FMS_TRUCK_TRANSPORTER_MST(TRUCK_TRANS_CD,TRUCK_TRANS_NAME,TRUCK_TRANS_ABBR,EFF_DT,"
						+ "ADDR,CITY,PIN,STATE,ACTIVE_FLAG,ENT_BY,ENT_DT,ENT_PROFILE) "
						+ "VALUES(?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,SYSDATE,?)";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, truck_trans_cd);
				stmt1.setString(2, truck_trans_nm);
				stmt1.setString(3, truck_trans_abbr);
				stmt1.setString(4, eff_dt);
				stmt1.setString(5, truck_trans_address);
				stmt1.setString(6, truck_trans_city);
				stmt1.setString(7, truck_trans_pin);
				stmt1.setString(8, state_cd);
				stmt1.setString(9, status_flag);
				stmt1.setString(10, emp_cd);
				stmt1.setString(11, comp_cd);
				stmt1.executeUpdate();
				
				stmt1.close();
				
				msg = "Successful! - Truck Transporter "+truck_trans_nm+" Inserted !";
				msg_type="S";
				
			}
			
			if(stat_cd!=null)
			{
				for(int i=0;i<stat_cd.length;i++)
				{
					query="DELETE FROM FMS_TRUCK_TRANSPORTER_TAX "
							+ "WHERE TRUCK_TRANS_CD=? AND STAT_CD=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, truck_trans_cd);
					stmt.setString(2, stat_cd[i]);
					stmt.executeUpdate();
					
					stmt.close();
					
					query1="INSERT INTO FMS_TRUCK_TRANSPORTER_TAX(TRUCK_TRANS_CD,STAT_CD,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY) "
							+ "VALUES(?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,SYSDATE,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, truck_trans_cd);
					stmt1.setString(2, stat_cd[i]);
					stmt1.setString(3, stat_no[i]);
					stmt1.setString(4, stat_eff_dt[i]);
					stmt1.setString(5, "Y");
					stmt1.setString(6, stat_remark[i]);
					stmt1.setString(7, emp_cd);
					stmt1.executeUpdate();
					
					stmt1.close();
				}
			}
			url = "../dlng/frm_truck_transport_mst.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			if(opration.equalsIgnoreCase("MODIFY"))
			{
				msg = "Error in Exception! - Truck Transporter Modification Failed!";
				msg_type="E";
			}
			else
			{
				msg = "Error in Exception! - Truck Transporter Addition Failed!";
				msg_type="E";
			}
			url=CommonVariable.errorpage_url+"?e="+e;
		}
	}
	
	private String extractFileName(Part part) 
    {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        String filenm = "";
        for (String s : items) 
        {
            if (s.trim().startsWith("filename") || s.trim().startsWith("meet_file")) 
            {
                filenm = s.substring(s.indexOf("=") + 2, s.length()-1);
            }       
        }
        return filenm;
    }
}
