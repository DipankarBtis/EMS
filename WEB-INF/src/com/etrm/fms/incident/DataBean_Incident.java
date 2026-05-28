package com.etrm.fms.incident;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

//Coded By          : Harsh Patel
//Code Reviewed by	:  
//CR Date			: 14/02/2023 
//Status	  		: Developing
public class DataBean_Incident 
{
	String db_src_file_name="DataBean_Incident.java";
	
	Connection conn; 
	PreparedStatement stmt;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt5;
	PreparedStatement stmt6;
	PreparedStatement stmt7;
	ResultSet rset; 
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
	ResultSet rset6;
	ResultSet rset7;
//	String queryString=""; //Commented by HM20241015 : Due to Security Hotspot Vulnerability
//	String queryString1="";
//	String queryString2="";
//	String queryString3="";
//	String queryString4="";
//	String queryString5="";
	
	UtilBean utilBean = new UtilBean();
	DateUtil dateUtil = new DateUtil();
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	
	public void init()
	{
		String function_nm="init()";
		
		try
		{
			Context initContext = new InitialContext();
	    	if(initContext == null)
	    	{
	    		throw new Exception("Boom - No Context");
	    	}
	    	
	    	Context envContext  = (Context)initContext.lookup("java:/comp/env");
	    	DataSource ds = (DataSource)envContext.lookup(RuntimeConf.security_database);
	    	if(ds != null) 
	    	{
	    		conn = ds.getConnection();       
	    		if(conn != null)  
	    		{	    			
	    			if(callFlag.equalsIgnoreCase("INCIDENT_MST"))
	    			{
	    				getAssignToList();
	    				getFilterList();
	    				calculateRecords();	//Added By Pratham Bhatt for pagination
	    				getIncidentReport();
	    				//checkForSuperAdmin();
	    			}
	    			else if(callFlag.equalsIgnoreCase("INCIDENT_MST_XLS")) 
	    			{
	    				getIncidentReport();
	    			}
	    		}
	    		
	    		conn.close();
    			conn = null;
	    	}
		}
		catch(Exception e)
		{
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
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	//Added by Pratham Bhatt for pagination 
	public void calculateRecords()
	{	
		String function_nm = "calculateRecords()";
		try
		{
			totalEntries = 0;
			int count = 0;
			String queryString="SELECT COUNT(*) "
					+ "FROM FMS_INCIDENT_MST "
					+ "WHERE INCIDENT_ID IS NOT NULL ";
			if(!filter_status.equals("0"))
			{
				queryString+="AND STATUS=? ";
			}
			if(!filter_priority.equals("0"))
			{
				queryString+="AND PRIORITY=? ";
			}
			if(!filter_incident_type.equals("0"))
			{
				queryString+="AND INCIDENT_TYPE=? ";
			}
			if(!filter_root_cause.equals("0"))
			{
				queryString+="AND ROOT_CAUSE=? ";
			}
			if(!filter_initiated_by.equals("0"))
			{
				queryString+="AND ENT_BY=? ";
			}
			
			if(!filter_po_app.equals("0") && filter_po_app.equals("Y"))
			{
				queryString+="AND PO_APP=? ";
			}
			else if(!filter_po_app.equals("0") && filter_po_app.equals("N"))
			{
				queryString+="AND PO_APP IS NULL ";
			}
			
			if(!filter_itm_app.equals("0") && filter_itm_app.equals("Y"))
			{
				queryString+="AND ITM_APP=? ";
			}
			else if(!filter_itm_app.equals("0") && filter_itm_app.equals("N"))
			{
				queryString+="AND ITM_APP IS NULL ";
			}
			queryString+= "ORDER BY INCIDENT_ID DESC ";
			stmt = conn.prepareStatement(queryString);
			//stmt.setString(++count, comp_cd);
			if(!filter_status.equals("0"))
			{
				stmt.setString(++count, filter_status);
			}
			if(!filter_priority.equals("0"))
			{
				stmt.setString(++count, filter_priority);
			}
			if(!filter_incident_type.equals("0"))
			{
				stmt.setString(++count, filter_incident_type);
			}
			if(!filter_root_cause.equals("0"))
			{
				stmt.setString(++count, filter_root_cause);
			}
			if(!filter_initiated_by.equals("0"))
			{
				stmt.setString(++count, filter_initiated_by);
			}
			if(!filter_po_app.equals("0") && filter_po_app.equals("Y"))
			{
				stmt.setString(++count, "Y");
			}
			if(!filter_itm_app.equals("0") && filter_itm_app.equals("Y"))
			{
				stmt.setString(++count, "Y");
			}
			rset=stmt.executeQuery();
			if(rset.next())
			{
				totalEntries = rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			int maxItem = 30;
			//pages = (int) ((float)totalEntries/(float)maxItem >totalEntries/maxItem?(float)(totalEntries/maxItem) + 1 : totalEntries/maxItem);
			pages=(int) Math.ceil((double) totalEntries / maxItem);
			int pg = Integer.parseInt(page_no);
			end =totalEntries<(pg*maxItem)?totalEntries:(pg*maxItem);
			start = (pg*maxItem) - (maxItem - 1);
		}
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getIncidentReport()
	{
		String function_nm="getIncidentReport()";
		try
		{
			int count = 0;
			String queryString="SELECT * FROM ";
			queryString+="(SELECT INCIDENT_ID,INCIDENT_TYPE,INCIDENT_TITLE,PRIORITY,INCIDENT_DTL,TO_CHAR(TARGET_DT,'DD/MM/YYYY'),"
					+ "ASSIGN_TO,TO_CHAR(LIVE_DT,'DD/MM/YYYY'),STATUS,ENT_BY,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS'),"
					+ "MODIFY_BY,TO_CHAR(MODIFY_DT,'DD/MM/YYYY HH24:MI:SS'),ROOT_CAUSE,ITM_APP,ITM_APP_BY,TO_CHAR(ITM_APP_DT,'DD/MM/YYYY HH24:MI:SS'),"
					+ "PO_APP,PO_APP_BY,TO_CHAR(PO_APP_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(PROD_APP_DT,'DD/MM/YYYY'),ROLLOUT_SCHEDULE,ENT_PROFILE,MOD_PROFILE,SCOPE, "
					+ "ROW_NUMBER() OVER (ORDER BY INCIDENT_ID ASC) AS RN "
//					+ "ROW_NUMBER() OVER (ORDER BY INCIDENT_ID DESC) AS RN "
					+ "FROM FMS_INCIDENT_MST "
					+ "WHERE INCIDENT_ID IS NOT NULL ";
					//+ "COMPANY_CD=? "; // AS Incident portal is maintained at SEI Centralized level
			if(!filter_status.equals("0"))
			{
				queryString+="AND STATUS=? ";
			}
			if(!filter_priority.equals("0"))
			{
				queryString+="AND PRIORITY=? ";
			}
			if(!filter_incident_type.equals("0"))
			{
				queryString+="AND INCIDENT_TYPE=? ";
			}
			if(!filter_root_cause.equals("0"))
			{
				queryString+="AND ROOT_CAUSE=? ";
			}
			if(!filter_initiated_by.equals("0"))
			{
				queryString+="AND ENT_BY=? ";
			}
			
			if(!filter_po_app.equals("0") && filter_po_app.equals("Y"))
			{
				queryString+="AND PO_APP=? ";
			}
			else if(!filter_po_app.equals("0") && filter_po_app.equals("N"))
			{
				queryString+="AND PO_APP IS NULL ";
			}
			
			if(!filter_itm_app.equals("0") && filter_itm_app.equals("Y"))
			{
				queryString+="AND ITM_APP=? ";
			}
			else if(!filter_itm_app.equals("0") && filter_itm_app.equals("N"))
			{
				queryString+="AND ITM_APP IS NULL ";
			}
			if(!filter_scope.equals("0"))//20251014
			{
					queryString+="AND SCOPE=? ";
			}
			queryString+= " ORDER BY INCIDENT_ID DESC ) RANKED_DATA ";
			if (callFlag.equalsIgnoreCase("INCIDENT_MST"))
			{	
				queryString+= "WHERE RN BETWEEN ? AND ? ";
			}
			
			stmt = conn.prepareStatement(queryString);
			//stmt.setString(++count, comp_cd);
			if(!filter_status.equals("0"))
			{
				stmt.setString(++count, filter_status);
			}
			if(!filter_priority.equals("0"))
			{
				stmt.setString(++count, filter_priority);
			}
			if(!filter_incident_type.equals("0"))
			{
				stmt.setString(++count, filter_incident_type);
			}
			if(!filter_root_cause.equals("0"))
			{
				stmt.setString(++count, filter_root_cause);
			}
			if(!filter_initiated_by.equals("0"))
			{
				stmt.setString(++count, filter_initiated_by);
			}
			if(!filter_po_app.equals("0") && filter_po_app.equals("Y"))
			{
				stmt.setString(++count, "Y");
			}
			if(!filter_itm_app.equals("0") && filter_itm_app.equals("Y"))
			{
				stmt.setString(++count, "Y");
			}
			if(!filter_scope.equals("0"))//20251014
			{
				if(!filter_scope.equals(""))
				{
					stmt.setString(++count, filter_scope);
				}
			}
			if (callFlag.equalsIgnoreCase("INCIDENT_MST"))
			{	
				stmt.setString(++count, ""+(totalEntries-(end-1)));
				stmt.setString(++count, ""+(totalEntries-(start-1)));
			}
			
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String incident_id=rset.getString(1)==null?"":rset.getString(1);
				String assign_to=rset.getString(7)==null?"":rset.getString(7);
				String ent_by=rset.getString(10)==null?"":rset.getString(10);
				String modify_by=rset.getString(12)==null?"":rset.getString(12);
				String rollout_schedule=rset.getString(22)==null?"":rset.getString(22);
				String ent_profile=rset.getString(23)==null?"":rset.getString(23);
				String mod_profile=rset.getString(24)==null?"":rset.getString(24);
				
				VINCIDENT_ID.add(incident_id);
				VINCIDENT_TYPE.add(rset.getString(2)==null?"":rset.getString(2));
				VINCIDENT_TITLE.add(rset.getString(3)==null?"":rset.getString(3));
				VPRIORITY.add(rset.getString(4)==null?"":rset.getString(4));
				VINCIDENT_DTL.add(rset.getString(5)==null?"":rset.getString(5));
				VTARGET_DT.add(rset.getString(6)==null?"":rset.getString(6));
				VASSIGN_TO.add(assign_to);
				VASSIGN_TO_NM.add(""+utilBean.getEmpName(conn,assign_to));
				VLIVE_DT.add(rset.getString(8)==null?"":rset.getString(8));
				VSTATUS.add(rset.getString(9)==null?"":rset.getString(9));
				if(ent_by.equals("0"))
				{
					VENT_BY.add("System");
				}
				else
				{
					VENT_BY.add(""+utilBean.getEmpName(conn,ent_by));
				}
				VENT_CD.add(rset.getString(10)==null?"":rset.getString(10));
				VENT_DT.add(rset.getString(11)==null?"":rset.getString(11));
				if(modify_by.equals("0"))
				{
					VMODIFY_BY.add("System");
				}
				else
				{
					VMODIFY_BY.add(""+utilBean.getEmpName(conn,modify_by));
				}
				VMODIFY_DT.add(rset.getString(13)==null?"":rset.getString(13));
				VROOT_CAUSE.add(rset.getString(14)==null?"":rset.getString(14));
				
				VITM_APP_FLAG.add(rset.getString(15)==null?"":rset.getString(15));
				VITM_APP_BY.add(utilBean.getEmpName(conn,rset.getString(16)==null?"":rset.getString(16)));
				VITM_APP_DT.add(rset.getString(17)==null?"":rset.getString(17));
				
				VPO_APP_FLAG.add(rset.getString(18)==null?"":rset.getString(18));
				VPO_APP_BY.add(utilBean.getEmpName(conn,rset.getString(19)==null?"":rset.getString(19)));
				VPO_APP_DT.add(rset.getString(20)==null?"":rset.getString(20));
				
				VPROD_APP_DT.add(rset.getString(21)==null?"":rset.getString(21));
				VPROD_ROLLOUT_DT.add(rset.getString(8)==null?"":rset.getString(8));
				VROLLOUT_SCHEDULE.add(rollout_schedule);
				VENT_PROFILE.add(utilBean.getCompanyAbbr(conn,ent_profile));
				VMOD_PROFILE.add(utilBean.getCompanyAbbr(conn,mod_profile));
				VSCOPE.add(rset.getString(25)==null?"":rset.getString(25));
				
				String eventDetails = getIncidentEventDetails(incident_id);
				VINCIDENT_EVENT_DTL.add(eventDetails);
				
				int index=0;
				String queryString1="SELECT SEQ_NO,INCIDENT_DTL,ATTACHMENT,ASSIGN_TO,STATUS,"
						+ "ENT_BY,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS'),ROOT_CAUSE,ENT_PROFILE,SCOPE "
						+ "FROM FMS_INCIDENT_DTL "
						+ "WHERE INCIDENT_ID=? " //COMPANY_CD=? AND
						+ "ORDER BY SEQ_NO DESC";
				stmt1 = conn.prepareStatement(queryString1);
				//stmt1.setString(1, comp_cd);
				stmt1.setString(1, incident_id);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					index+=1;
					String dtl_assign_to=rset1.getString(4)==null?"":rset1.getString(4);
					String dtl_ent_by=rset1.getString(6)==null?"":rset1.getString(6);
					String attachment = rset1.getString(3)==null?"":rset1.getString(3); 
					
					VDTL_SEQ_NO.add(rset1.getString(1)==null?"":rset1.getString(1));
					VDTL_INCIDENT_DTL.add(rset1.getString(2)==null?"":rset1.getString(2));
					VDTL_ATTACHMENT.add(attachment);
					if(!attachment.equals(""))
					{
						VATTACHMENT_PATH.add("../"+CommonVariable.work_dir+"/"+CommonVariable.incident_dir+"/"+incident_id+"/"+attachment);
					}
					else
					{
						VATTACHMENT_PATH.add("");
					}
					VDTL_ASSIGN_TO.add(""+utilBean.getEmpName(conn,dtl_assign_to));
					VDTL_STATUS.add(rset1.getString(5)==null?"":rset1.getString(5));
					if(dtl_ent_by.equals("0"))
					{
						VDTL_ENT_BY.add("System");
					}
					else
					{
						VDTL_ENT_BY.add(""+utilBean.getEmpName(conn,dtl_ent_by));
					}
					VDTL_ENT_DT.add(rset1.getString(7)==null?"":rset1.getString(7));
					VDTL_ROOT_CAUSE.add(rset1.getString(8)==null?"":rset1.getString(8));
					VDTL_ENT_PROFILE.add(utilBean.getCompanyAbbr(conn,rset1.getString(9)==null?"":rset1.getString(9)));
					VDTL_SCOPE.add(rset1.getString(10)==null?"":rset1.getString(10));
				}
				VINDEX.add(index);
				rset1.close();
				stmt1.close();
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getFilterList()
	{
		String function_nm="getFilterList()";
		try
		{
			String queryString="SELECT DISTINCT ENT_BY "
					+ "FROM FMS_INCIDENT_MST ";
					//+ "WHERE COMPANY_CD=?"; 
			stmt = conn.prepareStatement(queryString);
			//stmt.setString(1, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String initiated_by=rset.getString(1)==null?"":rset.getString(1);
				VFILTER_INITIATED_BY.add(utilBean.getEmpName(conn,initiated_by));
				VFILTER_INITIATED_BY_CD.add(initiated_by);
				
				String queryString1="SELECT COUNT(*) "
						+ "FROM FMS_INCIDENT_MST "
						+ "WHERE ENT_BY=?";//COMPANY_CD=? AND 
				stmt1 = conn.prepareStatement(queryString1);
				//stmt1.setString(1, comp_cd);
				stmt1.setString(1, initiated_by);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					VFILTER_COUNT_INITIATED_BY.add(rset1.getInt(1));
				}
				else
				{
					VFILTER_COUNT_INITIATED_BY.add("0");
				}
				rset1.close();
				stmt1.close();
			}
			rset.close();
			stmt.close();
			
			String queryString1="SELECT DISTINCT ROOT_CAUSE "
					+ "FROM FMS_INCIDENT_MST "
					+ "WHERE ROOT_CAUSE IS NOT NULL ";//COMPANY_CD=? AND 
			stmt2 = conn.prepareStatement(queryString1);
			//stmt2.setString(1, comp_cd);
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				String root_cause=rset2.getString(1)==null?"":rset2.getString(1);
				VFILTER_ROOT_CAUSE.add(root_cause);
				
				String queryString2="SELECT COUNT(*) "
						+ "FROM FMS_INCIDENT_MST "
						+ "WHERE ROOT_CAUSE=?";//COMPANY_CD=? AND 
				stmt1 = conn.prepareStatement(queryString2);
				//stmt1.setString(1, comp_cd);
				stmt1.setString(1, root_cause);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					VFILTER_COUNT_ROOT_CAUSE.add(rset1.getInt(1));
				}
				else
				{
					VFILTER_COUNT_ROOT_CAUSE.add("0");
				}
				rset1.close();
				stmt1.close();
			}
			rset2.close();
			stmt2.close();
			
			String queryString3="SELECT DISTINCT STATUS "
					+ "FROM FMS_INCIDENT_MST ";
					//+ "WHERE COMPANY_CD=? ";
			stmt3 = conn.prepareStatement(queryString3);
			//stmt3.setString(1, comp_cd);
			rset3=stmt3.executeQuery();
			while(rset3.next())
			{
				String status=rset3.getString(1)==null?"":rset3.getString(1);
				VFILTER_STATUS.add(status);
				
				String queryString4="SELECT COUNT(*) "
						+ "FROM FMS_INCIDENT_MST "
						+ "WHERE STATUS=?";// COMPANY_CD=? AND 
				stmt1 = conn.prepareStatement(queryString4);
				//stmt1.setString(1, comp_cd);
				stmt1.setString(1, status);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					VFILTER_COUNT_STATUS.add(rset1.getInt(1));
				}
				else
				{
					VFILTER_COUNT_STATUS.add("0");
				}
				rset1.close();
				stmt1.close();
			}
			rset3.close();
			stmt3.close();
			
			String queryString5="SELECT DISTINCT PRIORITY "
					+ "FROM FMS_INCIDENT_MST ";
					//+ "WHERE COMPANY_CD=? ";
			stmt4 = conn.prepareStatement(queryString5);
			//stmt4.setString(1, comp_cd);
			rset4=stmt4.executeQuery();
			while(rset4.next())
			{
				String priority=rset4.getString(1)==null?"":rset4.getString(1);
				VFILTER_PRIORITY.add(priority);
				
				String queryString6="SELECT COUNT(*) "
						+ "FROM FMS_INCIDENT_MST "
						+ "WHERE PRIORITY=?";//COMPANY_CD=? AND 
				stmt1 = conn.prepareStatement(queryString6);
				//stmt1.setString(1, comp_cd);
				stmt1.setString(1, priority);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					VFILTER_COUNT_PRIORITY.add(rset1.getInt(1));
				}
				else
				{
					VFILTER_COUNT_PRIORITY.add("0");
				}
				rset1.close();
				stmt1.close();
			}
			rset4.close();
			stmt4.close();
			
			String queryString7="SELECT DISTINCT INCIDENT_TYPE "
					+ "FROM FMS_INCIDENT_MST ";
					//+ "WHERE COMPANY_CD=? ";
			stmt5 = conn.prepareStatement(queryString7);
			//stmt5.setString(1, comp_cd);
			rset5=stmt5.executeQuery();
			while(rset5.next())
			{
				String incident_type=rset5.getString(1)==null?"":rset5.getString(1);
				VFILTER_INCIDENT_TYPE.add(incident_type);
				
				String queryString8="SELECT COUNT(*) "
						+ "FROM FMS_INCIDENT_MST "
						+ "WHERE INCIDENT_TYPE=?";//COMPANY_CD=? AND 
				stmt1 = conn.prepareStatement(queryString8);
				//stmt1.setString(1, comp_cd);
				stmt1.setString(1, incident_type);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					VFILTER_COUNT_INCIDENT_TYPE.add(rset1.getInt(1));
				}
				else
				{
					VFILTER_COUNT_INCIDENT_TYPE.add("0");
				}
				rset1.close();
				stmt1.close();
			}
			rset5.close();
			stmt5.close();
			
			//20251015
			String queryString9="SELECT DISTINCT SCOPE "
					+ "FROM FMS_INCIDENT_MST ";
					//+ "WHERE COMPANY_CD=? ";
			stmt2 = conn.prepareStatement(queryString9);
			//stmt5.setString(1, comp_cd);
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				String scopes=rset2.getString(1)==null?"":rset2.getString(1);
				if(!scopes.equals(""))
				{
				VFILTER_SCOPE.add(scopes);
				String queryString10="SELECT COUNT(*) "
						+ "FROM FMS_INCIDENT_MST "
						+ "WHERE SCOPE=?";//COMPANY_CD=? AND 
				stmt1 = conn.prepareStatement(queryString10);
				//stmt1.setString(1, comp_cd);
				stmt1.setString(1, scopes);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					VFILTER_COUNT_SCOPE.add(rset1.getInt(1));
				}
				else
				{
					VFILTER_COUNT_SCOPE.add("0");
				}
				rset1.close();
				stmt1.close();
				}
			}
			rset2.close();
			stmt2.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getAssignToList()
	{
		String function_nm="getAssignToList()";
		try
		{
			String queryString="SELECT EMP_CD,EMP_NM "
					+ "FROM FMS_EMP_MST "
					+ "WHERE EMP_STATUS=? "
					+ "ORDER BY EMP_NM";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VEMP_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VEMP_NM.add(rset.getString(2)==null?"":rset.getString(2));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void checkForSuperAdmin() // JD : Not required anymore
	{
		String function_nm="checkForSuperAdmin()";
		try 
		{
			isSupAdmn = "";
			
			String queryString1="SELECT GROUP_CD "
					+ "FROM FMS_EMP_GROUP_DTL "
					+ "WHERE COMPANY_CD=? AND GROUP_CD=? AND EMP_CD=? ";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, "1");
			stmt1.setString(3, emp_cd);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				isSupAdmn = "Y";
			}
			else 
			{
				isSupAdmn = "N";
			}
			
			rset1.close();
			stmt1.close();
		} 
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	private String getIncidentEventDetails(String incident_id)
	{
		String function_nm="getIncidentEventDetails()";
		String eventDetails="";
   		try
   		{
			String queryString="SELECT INCIDENT_DTL,ENT_BY,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS') "
					+ "FROM FMS_INCIDENT_DTL "
					+ "WHERE INCIDENT_ID=? "
					+ "ORDER BY SEQ_NO DESC";
			stmt7 = conn.prepareStatement(queryString);
			stmt7.setString(1, incident_id);
			rset7=stmt7.executeQuery();
			while(rset7.next())
			{
				String event=rset7.getString(1)==null?"":rset7.getString(1);
				String emp_nm=""+utilBean.getEmpName(conn,rset7.getString(2)==null?"":rset7.getString(2));
				String date=rset7.getString(3)==null?"":rset7.getString(3);
				
				eventDetails +=emp_nm + " [" + date + "] :: " + event + "<br><br>";
			}
			rset7.close();
			stmt7.close();
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return eventDetails;
	}
	
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	String isSupAdmn = "";
	public void setIsSupAdmn(String isSupAdmn) {this.isSupAdmn = isSupAdmn;}
	String emp_cd = "";
	public void setEmp_cd(String emp_cd) {this.emp_cd = emp_cd;}
	
	String filter_po_app="";
	String filter_itm_app="";
	String filter_status="";
	String filter_root_cause="";
	String filter_priority="";
	String filter_incident_type="";
	String filter_initiated_by="";
	String filter_scope="";
	
	//Added by Pratham Bhatt for pagination
	String page_no = "";	
	String scope = "";	
	int totalEntries;
	int start;
	int end;
	int pages;
	public void setPage_no(String pg) {this.page_no=pg;}
	public int getTotalPages() {return this.pages;}
	public int getTotalEntries() {return this.totalEntries;}
	public int getStartEntryNo() {return this.start;}
	public int getEndEntryNo() {return this.end;}
	//till here
	
	public void setFilter_status(String filter_status) {this.filter_status = filter_status;}
	public void setFilter_root_cause(String filter_root_cause) {this.filter_root_cause = filter_root_cause;}
	public void setFilter_priority(String filter_priority) {this.filter_priority = filter_priority;}
	public void setFilter_incident_type(String filter_incident_type) {this.filter_incident_type = filter_incident_type;}
	public void setFilter_initiated_by(String filter_initiated_by) {this.filter_initiated_by = filter_initiated_by;}
	public void setFilter_po_app(String filter_po_app) {this.filter_po_app = filter_po_app;}
	public void setFilter_itm_app(String filter_itm_app) {this.filter_itm_app = filter_itm_app;}
	public void setFilter_scope(String filter_scope) {this.filter_scope=filter_scope;}//20251014


	Vector VINCIDENT_ID = new Vector();
	Vector VINCIDENT_TYPE = new Vector();
	Vector VINCIDENT_TITLE = new Vector();
	Vector VPRIORITY = new Vector();
	Vector VINCIDENT_DTL = new Vector();
	Vector VTARGET_DT = new Vector();
	Vector VASSIGN_TO = new Vector();
	Vector VASSIGN_TO_NM = new Vector();
	Vector VLIVE_DT = new Vector();
	Vector VSTATUS = new Vector();
	Vector VENT_BY = new Vector();
	Vector VENT_DT = new Vector();
	Vector VMODIFY_BY = new Vector();
	Vector VMODIFY_DT = new Vector();
	Vector VROOT_CAUSE = new Vector();
	Vector VITM_APP_FLAG = new Vector();
	Vector VITM_APP_BY = new Vector();
	Vector VITM_APP_DT = new Vector();
	Vector VPO_APP_FLAG = new Vector();
	Vector VPO_APP_BY = new Vector();
	Vector VPO_APP_DT = new Vector();
	Vector VPROD_APP_DT =  new Vector();
	Vector VPROD_ROLLOUT_DT =  new Vector();
	Vector VROLLOUT_SCHEDULE = new Vector();
	Vector VENT_PROFILE = new Vector();
	Vector VMOD_PROFILE = new Vector();
	
	Vector VDTL_SEQ_NO = new Vector();
	Vector VDTL_ENT_BY = new Vector();
	Vector VDTL_ENT_DT = new Vector();
	Vector VDTL_ENT_PROFILE = new Vector();
	Vector VDTL_INCIDENT_DTL = new Vector();
	Vector VDTL_ASSIGN_TO = new Vector();
	Vector VDTL_ATTACHMENT = new Vector();
	Vector VDTL_STATUS = new Vector();
	Vector VATTACHMENT_PATH = new Vector();
	Vector VDTL_ROOT_CAUSE = new Vector();
	Vector VDTL_PO_APP_FLAG = new Vector();
	Vector VDTL_PO_APP_BY= new Vector();
	Vector VDTL_PO_APP_DT= new Vector();
	Vector VDTL_ITM_APP_FLAG = new Vector();
	Vector VDTL_ITM_APP_BY = new Vector();
	Vector VDTL_ITM_APP_DT= new Vector();
	Vector VDTL_SCOPE= new Vector(); //20251015
	
	Vector VINDEX = new Vector();
	Vector VEMP_CD = new Vector();
	Vector VEMP_NM = new Vector();
	Vector VENT_CD = new Vector();
	
	Vector VFILTER_STATUS = new Vector();
	Vector VFILTER_ROOT_CAUSE = new Vector();
	Vector VFILTER_COUNT_ROOT_CAUSE = new Vector();
	Vector VFILTER_COUNT_STATUS = new Vector();
	Vector VFILTER_PRIORITY = new Vector();
	Vector VFILTER_COUNT_PRIORITY = new Vector();
	Vector VFILTER_INCIDENT_TYPE = new Vector();
	Vector VFILTER_COUNT_INCIDENT_TYPE = new Vector();
	Vector VFILTER_INITIATED_BY = new Vector();
	Vector VFILTER_INITIATED_BY_CD = new Vector();
	Vector VFILTER_COUNT_INITIATED_BY = new Vector();
	Vector VSCOPE = new Vector();//20251014
	Vector VFILTER_COUNT_SCOPE = new Vector();//20251014
	Vector VFILTER_SCOPE = new Vector();//20251014
	Vector VINCIDENT_EVENT_DTL = new Vector();
	
	public Vector getVINCIDENT_ID() {return VINCIDENT_ID;}
	public Vector getVINCIDENT_TYPE() {return VINCIDENT_TYPE;}
	public Vector getVINCIDENT_TITLE() {return VINCIDENT_TITLE;}
	public Vector getVPRIORITY() {return VPRIORITY;}
	public Vector getVINCIDENT_DTL() {return VINCIDENT_DTL;}
	public Vector getVTARGET_DT() {return VTARGET_DT;}
	public Vector getVASSIGN_TO() {return VASSIGN_TO;}
	public Vector getVASSIGN_TO_NM() {return VASSIGN_TO_NM;}
	public Vector getVLIVE_DT() {return VLIVE_DT;}
	public Vector getVSTATUS() {return VSTATUS;}
	public Vector getVENT_BY() {return VENT_BY;}
	public Vector getVENT_DT() {return VENT_DT;}
	public Vector getVMODIFY_BY() {return VMODIFY_BY;}
	public Vector getVMODIFY_DT() {return VMODIFY_DT;}
	public Vector getVROOT_CAUSE() {return VROOT_CAUSE;}
	public Vector getVITM_APP_FLAG() {return VITM_APP_FLAG;}
	public Vector getVITM_APP_BY() {return VITM_APP_BY;}
	public Vector getVITM_APP_DT() {return VITM_APP_DT;}
	public Vector getVPO_APP_FLAG() {return VPO_APP_FLAG;}
	public Vector getVPO_APP_BY() {return VPO_APP_BY;}
	public Vector getVPO_APP_DT() {return VPO_APP_DT;}
	
	public Vector getVPROD_ROLLOUT_DT() {return VPROD_ROLLOUT_DT;}
	public Vector getVPROD_APP_DT() {return VPROD_APP_DT;}
	public Vector getVROLLOUT_SCHEDULE() {return VROLLOUT_SCHEDULE;}
	public Vector getVENT_PROFILE() {return VENT_PROFILE;}
	public Vector getVMOD_PROFILE() {return VMOD_PROFILE;}
	
	public Vector getVDTL_SEQ_NO() {return VDTL_SEQ_NO;}
	public Vector getVDTL_ENT_BY() {return VDTL_ENT_BY;}
	public Vector getVDTL_ENT_DT() {return VDTL_ENT_DT;}
	public Vector getVDTL_ENT_PROFILE() {return VDTL_ENT_PROFILE;}
	public Vector getVDTL_INCIDENT_DTL() {return VDTL_INCIDENT_DTL;}
	public Vector getVDTL_ASSIGN_TO() {return VDTL_ASSIGN_TO;}
	public Vector getVDTL_ATTACHMENT() {return VDTL_ATTACHMENT;}
	public Vector getVDTL_STATUS() {return VDTL_STATUS;}
	public Vector getVATTACHMENT_PATH() {return VATTACHMENT_PATH;}
	public Vector getVDTL_ROOT_CAUSE() {return VDTL_ROOT_CAUSE;}
	public Vector getVDTL_ITM_APP_FLAG() {return VDTL_ITM_APP_FLAG;}
	public Vector getVDTL_ITM_APP_BY() {return VDTL_ITM_APP_BY;}
	public Vector getVDTL_ITM_APP_DT() {return VDTL_ITM_APP_DT;}
	public Vector getVDTL_PO_APP_FLAG() {return VDTL_PO_APP_FLAG;}
	public Vector getVDTL_PO_APP_BY() {return VDTL_PO_APP_BY;}
	public Vector getVDTL_PO_APP_DT() {return VDTL_PO_APP_DT;}
	public Vector getVDTL_SCOPE() {return VDTL_SCOPE;}
	
	public Vector getVINDEX() {return VINDEX;}
	public Vector getVEMP_CD(){return VEMP_CD;}
	public Vector getVEMP_NM(){return VEMP_NM;}
	public Vector getVENT_CD(){return VENT_CD;}

	public Vector getVFILTER_STATUS(){return VFILTER_STATUS;}
	public Vector getVFILTER_ROOT_CAUSE(){return VFILTER_ROOT_CAUSE;}
	public Vector getVFILTER_COUNT_ROOT_CAUSE(){return VFILTER_COUNT_ROOT_CAUSE;}
	public Vector getVFILTER_COUNT_STATUS(){return VFILTER_COUNT_STATUS;}
	public Vector getVFILTER_PRIORITY(){return VFILTER_PRIORITY;}
	public Vector getVFILTER_COUNT_PRIORITY(){return VFILTER_COUNT_PRIORITY;}
	public Vector getVFILTER_INCIDENT_TYPE(){return VFILTER_INCIDENT_TYPE;}
	public Vector getVFILTER_COUNT_INCIDENT_TYPE(){return VFILTER_COUNT_INCIDENT_TYPE;}
	public Vector getVFILTER_INITIATED_BY(){return VFILTER_INITIATED_BY;}
	public Vector getVFILTER_INITIATED_BY_CD(){return VFILTER_INITIATED_BY_CD;}
	public Vector getVFILTER_COUNT_INITIATED_BY(){return VFILTER_COUNT_INITIATED_BY;}
	public Vector getVSCOPE(){return VSCOPE;}//20251014
	public Vector getVFILTER_COUNT_SCOPE(){return VFILTER_COUNT_SCOPE;}//20251014
	public Vector getVFILTER_SCOPE(){return VFILTER_SCOPE;}//20251014
	public Vector getVINCIDENT_EVENT_DTL(){return VINCIDENT_EVENT_DTL;}
	
	
	String target_dt="";
	public String getTarget_dt() {return target_dt;}

	public String getIsSupAdmn() {return isSupAdmn;}
}
