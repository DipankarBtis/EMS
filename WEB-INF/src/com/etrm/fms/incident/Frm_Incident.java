package com.etrm.fms.incident;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
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

import com.etrm.fms.mail.MailDelivery;
import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.escapeSingleQuotes;

//Coded By          : Harsh Patel
//Code Reviewed by	:  
//CR Date			: 18/10/2022 
//Status	  		: Developing

@WebServlet("/servlet/Frm_Incident")
@MultipartConfig(fileSizeThreshold=1024*1024*10, 	// 10 MB 
maxFileSize=1024*1024*50,      	// 50 MB
maxRequestSize=1024*1024*100)   	// 100 MB
public class Frm_Incident extends HttpServlet
{
	public static  Connection dbcon;
	
	public static String servletName = "Frm_Incident";
	public static String option = "";
	public static String url = "";
	public static String msg = "";
	public static String msg_type = "";
	public static String err_msg = "";
	public static String frm_src_file_name ="Frm_Incident.java";
	
	private static String queryString = null;
	private static String query = null;
	private static String query1 = null;
	private static String query2 = null;
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
	public static String comp_abbr="";
	public static String emp_nm="";
	public static String ip="";
	
	public static String commonUrl_pra="";
	
	public static escapeSingleQuotes escObj = new escapeSingleQuotes();
	
	static UtilBean utilBean = new UtilBean();
	static DateUtil utilDate = new DateUtil();
	static MailDelivery mailDelv = new MailDelivery();
	
	private static final String SAVE_DIR = CommonVariable.incident_dir;
	
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
				if(dbcon != null)
				{
					dbcon.setAutoCommit(false);

					form_id=request.getParameter("form_cd")==null?"0":request.getParameter("form_cd");
					form_nm=request.getParameter("form_nm")==null?"":request.getParameter("form_nm");
					mod_cd=request.getParameter("mod_cd")==null?"0":request.getParameter("mod_cd");
					mod_nm=request.getParameter("mod_nm")==null?"":request.getParameter("mod_nm");
					u=request.getParameter("u")==null?"":request.getParameter("u");
					
					emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
					comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
					comp_abbr = (String)session.getAttribute("comp_abbr")==null?"":(String)session.getAttribute("comp_abbr");
					emp_nm = (String)session.getAttribute("emp_nm")==null?"":(String)session.getAttribute("emp_nm");
					ip = (String)session.getAttribute("ip")==null?"":(String)session.getAttribute("ip");
					
					new_value="";
					old_value="";
					
					option=request.getParameter("option")==null?"":request.getParameter("option");
					
					commonUrl_pra = "&u="+u;
					
					if(option.equalsIgnoreCase("INCIDENT_MST"))
					{
						InsertUpdateIncidentDetail(request);
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
		
		try {
		response.sendRedirect(url);
		}catch(IOException e) {
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
		}
	}
	
	private void InsertUpdateIncidentDetail(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		
		String function_nm="InsertUpdateIncidentDetail()";
		
		try
		{	
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String incident_type = request.getParameter("incident_type")==null?"":request.getParameter("incident_type");
			String incident_id = request.getParameter("incident_id")==null?"":request.getParameter("incident_id");
			String assign_to = request.getParameter("assign_to")==null?"":request.getParameter("assign_to");
			String priority = request.getParameter("priority")==null?"":request.getParameter("priority");
			String target_dt = request.getParameter("target_dt")==null?"":request.getParameter("target_dt");
			String incident_title = request.getParameter("incident_title")==null?"":request.getParameter("incident_title");
			String incident_detail = request.getParameter("incident_detail")==null?"":request.getParameter("incident_detail");
			String status = request.getParameter("status")==null?"":request.getParameter("status");
			String root_cause = request.getParameter("root_cause")==null?"":request.getParameter("root_cause");
			String itm_app_flag = request.getParameter("itm_app_flag")==null?"":request.getParameter("itm_app_flag");
			String po_app_flag = request.getParameter("po_app_flag")==null?"":request.getParameter("po_app_flag");
			String prod_app_dt = request.getParameter("prod_app_dt")==null?"":request.getParameter("prod_app_dt");
			String prod_rollout_dt = request.getParameter("prod_rollout_dt")==null?"":request.getParameter("prod_rollout_dt");
			String go_live_dt = request.getParameter("go_live_dt")==null?"":request.getParameter("go_live_dt");
			String rollout_schedule = request.getParameter("rollout_schedule")==null?"":request.getParameter("rollout_schedule");
			
			String filter_status = request.getParameter("filter_status")==null?"0":request.getParameter("filter_status");
			String filter_priority = request.getParameter("filter_priority")==null?"0":request.getParameter("filter_priority");
			String filter_incident_type = request.getParameter("filter_incident_type")==null?"0":request.getParameter("filter_incident_type");
			String scopes = request.getParameter("scopes")==null?"":request.getParameter("scopes");//20251014 added by ajay
			String initiatd_by = request.getParameter("initiatd_by")==null?"":request.getParameter("initiatd_by");//20251024 added by ajay
			
			incident_title=escObj.replaceSingleQuotes(incident_title);
			incident_detail=escObj.replaceSingleQuotes(incident_detail);
			
			if (opration.equals("INSERT"))
			{
				String sysdt=""+utilDate.getSysdate();
				String year = sysdt.substring(8,sysdt.length());
				
				String temp_incident_cd="";
				
				if(!year.equals("") && !comp_cd.equals(""))
				{
					temp_incident_cd=year;//+""+comp_cd;
				}
				
				int inc_id=Integer.parseInt(temp_incident_cd)*100000;
				queryString="SELECT MAX(INCIDENT_ID) "
						+ "FROM FMS_INCIDENT_MST "
						+ "WHERE INCIDENT_ID LIKE ?";
				stmt = dbcon.prepareStatement(queryString);
				//stmt.setString(1, comp_cd);
				stmt.setString(1, temp_incident_cd+"%");
				rset=stmt.executeQuery();
				if(rset.next())
				{
					if(rset.getInt(1) > 0)
					{
						incident_id=""+(rset.getInt(1)+1);
					}
					else
					{
						incident_id=""+(inc_id+1);
					}
				}
				else
				{
					incident_id=""+(inc_id+1);
				}
				rset.close();
				stmt.close();
			}
			
	        if(!incident_id.equals(""))
			{
	        	String appPath = request.getServletContext().getRealPath("");
	        	
	        	String main_folder="";
				if(!comp_cd.equals(""))
				{
					main_folder=CommonVariable.work_dir;
				}
				File MainDir = new File(appPath+File.separator+main_folder);
		        if(!MainDir.exists()) 
		        {
		        	MainDir.mkdir();
		        }
		        
				String savePath = appPath+File.separator+main_folder+File.separator+SAVE_DIR;
		        String subSavePath = savePath+File.separator+incident_id;
		        
		        File fileSaveDir = new File(savePath);
		        if(!fileSaveDir.exists()) 
		        {
		            fileSaveDir.mkdir();
		        }
		        
	        	File subfile = new File(subSavePath);
	            if(!subfile.exists())
	            {
	            	subfile.mkdir();
	            }
		        
		        String file_name="";
		        String fileName="";
		        for (Part part : request.getParts()) 
		        {
		        	fileName = extractFileName(part);
		        	// refines the fileName in case it is an absolute path
				    fileName = new File(fileName).getName();
				    if(!fileName.equals("") ){
				    	file_name=fileName;
				    	part.write(subSavePath +File.separator+ fileName);
				    } 
		        }
		        
		        String live_dt="";
		        if(status.equals("LIVE"))
		        {
		        	//String sysdt = utilDate.getSysdateWithTime24hr();
		        	live_dt = utilDate.getSysdateWithTime24hr();
		        }	
		        
				if(opration.equals("MODIFY"))
				{
					query="UPDATE FMS_INCIDENT_MST SET INCIDENT_TYPE=?,INCIDENT_TITLE=?,PRIORITY=?, "
							+ "INCIDENT_DTL=?,TARGET_DT=TO_DATE(?,'DD/MM/YYYY'),REGD_IP=?, "
							+ "ASSIGN_TO=?,STATUS=?,ROOT_CAUSE=?,LIVE_DT=TO_DATE(?,'DD/MM/YYYY  HH24:MI:SS'), "
							+ "MODIFY_BY=?,MODIFY_DT=SYSDATE,PROD_APP_DT=TO_DATE(?,'DD/MM/YYYY  HH24:MI:SS'), "
							+ "ROLLOUT_SCHEDULE=?,MOD_PROFILE=?,SCOPE=?,ENT_BY = ? "
							+ "WHERE INCIDENT_ID=?";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, incident_type);
					stmt.setString(2, incident_title);
					stmt.setString(3, priority);
					stmt.setString(4, incident_detail);
					stmt.setString(5, target_dt);
					stmt.setString(6, ip);
					stmt.setString(7, assign_to);
					stmt.setString(8, status);
					stmt.setString(9, root_cause);
					stmt.setString(10, prod_rollout_dt);
					stmt.setString(11, emp_cd);   
					stmt.setString(12, prod_app_dt);
					stmt.setString(13, rollout_schedule);
					stmt.setString(14, comp_cd);
					stmt.setString(15, scopes);
					stmt.setString(16, initiatd_by);
					stmt.setString(17, incident_id);
					
					stmt.executeUpdate();
					
					msg = "Incident "+incident_id+" - Modified Successfully!";
					msg_type="S";
					
					stmt.close();
				}
				else if(opration.equals("ITM_APP"))
				{
					query="UPDATE FMS_INCIDENT_MST SET ITM_APP=?, ITM_APP_BY=?,ITM_APP_DT=SYSDATE,MOD_PROFILE=? "
							+ "WHERE INCIDENT_ID=?";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, itm_app_flag);
					stmt.setString(2, emp_cd);
					stmt.setString(3, comp_cd);
					stmt.setString(4, incident_id);
					stmt.executeUpdate();
					if(itm_app_flag.equals("Y")) 
					{
						msg = "Incident "+incident_id+" - ITDM Approved Successfully!";
						msg_type="S";
					}
					else if(itm_app_flag.equals("N")) 
					{
						msg = "Incident "+incident_id+" - ITDM Dispproved!";
						msg_type="F";
					}
					stmt.close();
				}
				else if(opration.equals("PO_APP"))
				{
					query="UPDATE FMS_INCIDENT_MST SET PO_APP=?, PO_APP_BY=?,PO_APP_DT=SYSDATE,MOD_PROFILE=? "
							+ "WHERE INCIDENT_ID=?";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, po_app_flag);
					stmt.setString(2, emp_cd);
					stmt.setString(3, comp_cd);
					stmt.setString(4, incident_id);
					stmt.executeUpdate();
					if(po_app_flag.equals("Y")) 
					{
						msg = "Incident "+incident_id+" - BAO Approved Successfully!";
						msg_type="S";
					}
					else if(po_app_flag.equals("N")) 
					{
						msg = "Incident "+incident_id+" - BAO Dispproved!";
						msg_type="F";
					}
					stmt.close();
				}
				else
				{
					query="INSERT INTO FMS_INCIDENT_MST(ENT_PROFILE,INCIDENT_ID,INCIDENT_TYPE,INCIDENT_TITLE,PRIORITY,INCIDENT_DTL,"
							+ "TARGET_DT,REGD_IP,ASSIGN_TO,STATUS,ROOT_CAUSE,ENT_BY,ENT_DT,PROD_APP_DT,LIVE_DT,ROLLOUT_SCHEDULE,SCOPE) "
							+ "VALUES(?,?,?,?,?,?,"
							+ "TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,SYSDATE,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?)";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, incident_id);
					stmt.setString(3, incident_type);
					stmt.setString(4, incident_title);
					stmt.setString(5, priority);
					stmt.setString(6, incident_detail);
					stmt.setString(7, target_dt);
					stmt.setString(8, ip);
					stmt.setString(9, assign_to);
					stmt.setString(10, status);
					stmt.setString(11, root_cause);
					stmt.setString(12, initiatd_by);   //20251024
					stmt.setString(13, prod_app_dt);
					stmt.setString(14, prod_rollout_dt);
					stmt.setString(15, rollout_schedule);
					stmt.setString(16, scopes);
					stmt.executeUpdate();
					
					msg = "New Incident "+incident_id+" - Added Successfully!";
					msg_type="S";
					
					stmt.close();
				}
				
				String incident_dtl_seq_no="1";
				
				query="SELECT MAX(SEQ_NO)"
						+ "FROM FMS_INCIDENT_DTL "
						+ "WHERE INCIDENT_ID=?";
				stmt = dbcon.prepareStatement(query);
				//stmt.setString(1, comp_cd);
				stmt.setString(1, incident_id);
				rset = stmt.executeQuery();
				if(rset.next())
				{
					incident_dtl_seq_no=""+(rset.getInt(1)+1);
				}
				rset.close();
				stmt.close();
				
				if(!incident_dtl_seq_no.equals(""))
				{
					if(opration.equals("MODIFY")) 
					{
						query="INSERT INTO FMS_INCIDENT_DTL(ENT_PROFILE,INCIDENT_ID,SEQ_NO,INCIDENT_DTL,ATTACHMENT,"
								+ "ASSIGN_TO,STATUS,ENT_BY,ENT_DT,ROOT_CAUSE,SCOPE) "
								+ "VALUES(?,?,?,?,?,"
								+ "?,?,?,SYSDATE,?,?)";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, incident_id);
						stmt1.setString(3, incident_dtl_seq_no);
						stmt1.setString(4, incident_detail);
						stmt1.setString(5, file_name);
						stmt1.setString(6, assign_to);
						stmt1.setString(7, status);
						stmt1.setString(8, emp_cd);  //20251024
						stmt1.setString(9, root_cause);
						stmt1.setString(10, scopes);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					else if (opration.equals("INSERT"))
					{
						query="INSERT INTO FMS_INCIDENT_DTL(ENT_PROFILE,INCIDENT_ID,SEQ_NO,INCIDENT_DTL,ATTACHMENT,"
								+ "ASSIGN_TO,STATUS,ENT_BY,ENT_DT,ROOT_CAUSE,SCOPE) "
								+ "VALUES(?,?,?,?,?,"
								+ "?,?,?,SYSDATE,?,?)";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, incident_id);
						stmt1.setString(3, incident_dtl_seq_no);
						stmt1.setString(4, incident_detail);
						stmt1.setString(5, file_name);
						stmt1.setString(6, assign_to);
						stmt1.setString(7, status);
						stmt1.setString(8, emp_cd);  //20251024
						stmt1.setString(9, root_cause);
						stmt1.setString(10, scopes);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
				}
			}
			else
			{
				if(opration.equals("MODIFY"))
				{
					msg = "Failed! - Incident Detail Modification Failed!";
					msg_type="E";
				}
				else
				{
					msg = "Failed! - Incident Detail Submission Failed!";
					msg_type="E";
				}
			}
			
			dbcon.commit();
			
			url = "../incident/frm_incident_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&filter_status="+filter_status+
					"&filter_incident_type="+filter_incident_type+"&filter_priority="+filter_priority+commonUrl_pra;
			
			IncidentMailBody(comp_cd,opration,incident_id, incident_type, priority,assign_to,incident_detail,status, root_cause, incident_title,msg,emp_cd,new_value,old_value,itm_app_flag,po_app_flag);
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error In Exception!";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void IncidentMailBody(String comp_cd,String operation,String incident_id,String incident_type,String priority,
			String assign_to,String incident_detail,String status,String root_cause, String incident_title,String msg,
			String emp_cd,String new_values,String old_values,String itm_app_flag, String po_app_flag) throws Exception
	{
		String mailBody="";
		String assign_to_emailId = "";
		String mod_to_emailId = "";
		String ini_to_emailId = "";
		String mod_to = "";
		String ini_to = "";
		String ass_to = "";
		
		String function_nm="IncidentMailBody()";
		try
		{
			Vector VMOD_BY_LIST = new Vector();
			Vector VMOD_BY_EMAIL = new Vector();
			
			Vector VASS_BY_LIST = new Vector();
			Vector VASS_BY_EMAIL = new Vector();
			
			//Modified By
			query1 = "SELECT DISTINCT ENT_BY "
					+ "FROM FMS_INCIDENT_DTL "
					+ " WHERE INCIDENT_ID=? ";
			stmt1 = dbcon.prepareStatement(query1);
			//stmt1.setString(1, comp_cd);
			stmt1.setString(1, incident_id);
			rset1 = stmt1.executeQuery();
			while(rset1.next())
			{
				mod_to = rset1.getString(1)==null?"":rset1.getString(1);
				VMOD_BY_LIST.add(mod_to);
			}
			rset1.close();
			stmt1.close();
			
			for(int a=0;a<VMOD_BY_LIST.size();a++) 
			{
				query2 = "SELECT EMAIL_ID "
						+ "FROM FMS_EMP_MST "
						+ " WHERE EMP_CD=? ";//COMPANY_CD=? AND 
				stmt2 = dbcon.prepareStatement(query2);
				//stmt2.setString(1, comp_cd);
				stmt2.setString(1, ""+VMOD_BY_LIST.elementAt(a));
				rset2 = stmt2.executeQuery();
				if(rset2.next())
				{
					mod_to_emailId = rset2.getString(1)==null?"":rset2.getString(1);
					VMOD_BY_EMAIL.add(mod_to_emailId);
				}
				rset2.close();
				stmt2.close();
			}
			
			//Assign To
			query1 = "SELECT DISTINCT ASSIGN_TO "
					+ "FROM FMS_INCIDENT_DTL "
					+ " WHERE INCIDENT_ID=? ";
			stmt3 = dbcon.prepareStatement(query1);
			//stmt3.setString(1, comp_cd);
			stmt3.setString(1, incident_id);
			rset3 = stmt3.executeQuery();
			while(rset3.next())
			{
				ass_to = rset3.getString(1)==null?"":rset3.getString(1);
				VASS_BY_LIST.add(ass_to);
			}
			rset3.close();
			stmt3.close();
			
			for(int b=0;b<VASS_BY_LIST.size();b++) 
			{
				query2 = "SELECT EMAIL_ID "
						+ "FROM FMS_EMP_MST "
						+ " WHERE EMP_CD=? ";
				stmt4 = dbcon.prepareStatement(query2);
				stmt4.setString(1, ""+VASS_BY_LIST.elementAt(b));
				rset4 = stmt4.executeQuery();
				if(rset4.next())
				{
					String ass_to_emailId = rset4.getString(1)==null?"":rset4.getString(1);
					VASS_BY_EMAIL.add(ass_to_emailId);
				}
				rset4.close();
				stmt4.close();
			}
			
			String incidentDetail="";
			String sysdt = utilDate.getSysdateWithTime24hr();
			String root_cause_nm="";
			
			if(root_cause.equals("IMP")){
				root_cause_nm="Implemented";
			}else if(root_cause.equals("ADNAB")){
				root_cause_nm="As designed/Not A BUG";
			}else if(root_cause.equals("WAP")){
				root_cause_nm="Workaround Provided";
			}else if(root_cause.equals("SNI")){
				root_cause_nm="System/Network Issue";
			}
				
			if(!incident_id.equals(""));
			{
				if(!incident_detail.isEmpty())
				{
					incidentDetail="Incident ID : "+incident_id+"<br>"
							+ "Incident Type : "+incident_type+"<br>"
							+ "Priority : "+priority+"<br>"
							+ "Assigned To : "+utilBean.getEmpName(dbcon, assign_to)+"<br>";
					
					incidentDetail+= "Detail : "+incident_detail.replaceAll("\n", "<br>")+"<br>";
					incidentDetail+= "Status : "+status+"<br>";
					if(status.equals("Solution Provided"))
					{
						incidentDetail+= "Root Cause : "+root_cause_nm+"<br>";
					}	
					
					if(operation.equals("MODIFY")) 
					{
						incidentDetail+= "Modified By : "+utilBean.getEmpName(dbcon,emp_cd)+"<br>"
									+ "Modified On : "+sysdt+"<br>";
					}
					else if(operation.equals("INSERT")) 
					{
						incidentDetail+= "Initiated By : "+utilBean.getEmpName(dbcon,emp_cd)+"<br>"
									+ "Initiated On : "+sysdt+"<br>";
					}
				}
			}
			
			if(!incidentDetail.isEmpty() || !itm_app_flag.equals("") || !po_app_flag.equals("")) 
			{
				String subject="";
				if(!itm_app_flag.equals("")) 
				{
					subject="ITDM Approval :: "+CommonVariable.app_name+" Incident# "+incident_type+" "+incident_id+"/"+priority+"/"+status+"/"+incident_title;
				}
				else if (!po_app_flag.equals(""))
				{
					subject="BAO Approval :: "+CommonVariable.app_name+" Incident# "+incident_type+" "+incident_id+"/"+priority+"/"+status+"/"+incident_title;
				}
				else
				{
					subject=""+CommonVariable.app_name+" Incident# "+incident_type+" "+incident_id+"/"+priority+"/"+status+"/"+incident_title;
				}
			
				mailBody="<html>";
				if(!itm_app_flag.equals("")) 
				{
					mailBody += "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>IDTM Approval Received for Incident#"+incident_type+" "+incident_id+", from";
				}
				else if (!po_app_flag.equals(""))
				{
					mailBody += " <span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>BAO Approval Received for Incident#"+incident_type+" "+incident_id+", from";
				}
				else
				{
					mailBody += "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Incident#"+incident_type+" "+incident_id+"/"+priority+"/"+status+"/"+incident_title;
				}
				
				if(operation.equals("MODIFY"))
				{
					mailBody+= "<font style='background:#00cc00' color='white'> Modified</font> Successfully, by ";
				}
				else if(operation.equals("INSERT"))
				{
					mailBody+= "<font style='background:#00cc00' color='white'> Inserted</font> Successfully, by ";
				}
				mailBody+=" "+utilBean.getEmpName(dbcon,emp_cd)+" ";
				mailBody+="on "+utilDate.getSysdateWithTime24hr()+"";
				mailBody+= "</span><br><br>";
				
				if(!incidentDetail.equals(""))
				{
					mailBody+="<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'><b>Incident Detail :<br></b>"+incidentDetail+"</span><br><br>";
				}
				
				mailBody+=CommonVariable.mail_signature;
				mailBody+=CommonVariable.mail_disclaimer;
				mailBody+= "</html>";
				
				String to_mail_list_incident ="";
				
				String asign_to_Email_list = "";
				String modified_by_Email_list = "";
				
				Set elementsInVector2 = new HashSet<>(VMOD_BY_EMAIL);

				for (int i = VASS_BY_EMAIL.size() - 1; i >= 0; i--)
				{
					if (elementsInVector2.contains(VASS_BY_EMAIL.get(i)))
					{
						VASS_BY_EMAIL.remove(i);
					}
				}
				
				asign_to_Email_list+=""+VASS_BY_EMAIL.toString().replace("[", "").replace("]","");
				
				modified_by_Email_list+=""+VMOD_BY_EMAIL.toString().replace("[", "").replace("]","");
				
				String to_list_Mail_Recp= utilBean.getToMailReceipentList(dbcon,"0","Incident Notification", "HELPDESK", "NA", "On-Event");
				
				if(!to_list_Mail_Recp.equals("") && to_list_Mail_Recp != null && !to_list_Mail_Recp.trim().equals("")) //HM20250301 : used '!= null' as per vulnerability report.   
				{
					to_mail_list_incident = asign_to_Email_list+","+modified_by_Email_list+","+to_list_Mail_Recp;
				}
				else
				{
					to_mail_list_incident = asign_to_Email_list+","+modified_by_Email_list;
				}
				
				String cc_mail_list_incident=utilBean.getCcMailReceipentList(dbcon,"0","Incident Notification", "HELPDESK", "NA", "On-Event");
				
				//System.out.println(to_mail_list_incident+"----"+cc_mail_list_incident+">>>>>"+mailBody);

				if(!to_mail_list_incident.equals("") && !mailBody.equals(""))
				{
					mailDelv.sendMail(comp_cd,to_mail_list_incident, subject, mailBody, "", cc_mail_list_incident, "");
				}
			}
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			throw e;
		}
	}
	
	private String extractFileName(Part part) 
    {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        //System.out.println("items*******"+items.length);
        String filenm = "";
        for (String s : items) 
        {
            if (s.trim().startsWith("filename") || s.trim().startsWith("meet_file")) 
            {
           	//System.out.println("s*****"+s);
                filenm = s.substring(s.indexOf("=") + 2, s.length()-1);
            }       
        }
        return filenm;
    }
}
