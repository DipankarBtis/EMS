package com.etrm.fms.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

//Coded By          : Harsh Patel
//Code Reviewed by	:  
//CR Date			: 14/09/2022 
//Status	  		: Developing

public class DataBean_Admin 
{
	String db_src_file_name="DataBean_Admin.java";
	Connection conn; 
	PreparedStatement stmt;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	ResultSet rset; 
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	String queryString="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	
	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();
	
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
	    			if(callFlag.equalsIgnoreCase("MODULE_MST"))
	    			{
	    				ModuleDetail();
	    			}
	    			else if(callFlag.equalsIgnoreCase("GROUP_MST"))
	    			{
	    				GroupDetail();
	    			}
	    			else if(callFlag.equalsIgnoreCase("MENU_MST"))
	    			{
	    				ModuleDetail();
	    				GroupDetail();
	    				
	    				if(!module_cd.equals("") && !module_cd.equals("0"))
	    				{
	    					MenuMst();
	    					getMenuDetail();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("ACCESS_RIGHT_MST"))
	    			{
	    				ModuleDetail();
	    				getAccessGroupDetail();
	    				SubModuleDetail();
	    				
	    				if(!module_cd.equals("0") && !group_cd.equals("0"))
	    				{
	    					AccessRightDetail();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("USER_MST"))
	    			{
	    				if(opration.equals("MODIFY"))
	    				{
	    					EmpMasterNoStatus();
	    					getUserDetail();
	    				}
	    				getPasswordPolicyDtl();
	    			}
	    			else if(callFlag.equalsIgnoreCase("ACCESS_GROUP_ALLOCATION"))
	    			{
	    				EmpMaster();
	    				GroupDetailWithActiveStatus();
	    				if(!emp_cd.equals("0") && !emp_cd.equals(""))
	    				{
	    					getAccessGroupAllocationDetail();
	    				}
	    				checkForSuperAdmin();
	    			}
	    			else if(callFlag.equalsIgnoreCase("AUDIT_TRAIL"))
	    			{
	    				getAuditTrail();
	    				checkForSuperAdmin();
	    			}
	    			else if(callFlag.equalsIgnoreCase("USER_LIST"))
	    			{
	    				getEmpList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("ACTIVE_USER_LIST"))
	    			{
	    				getActiveEmpList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("PASSWORD_POLICY"))
	    			{
	    				getPasswordPolicyList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CHANGE_PASSWORD"))
	    			{
	    				getPasswordPolicyDtl();
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
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
	public void getPasswordPolicyDtl()
	{
		String function_nm="getPasswordPolicyDtl()";
		try
		{
			String sysdate = utilDate.getSysdate();
			
			queryString="SELECT USR_MIN,USR_MAX,PWD_LEN_MIN,PWD_LEN_MAX,PREV_PWD_NOREP,PWD_EXP_DAYS,PWD_EXP_REMDR,DORMNT_DAYS "
					+ "FROM FMS_ADMIN_POLICY "
					+ "WHERE EFF_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "ORDER BY EFF_DT DESC";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, sysdate);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				uid_min_length = rset.getString(1)==null?"":rset.getString(1);
				uid_max_length = rset.getString(2)==null?"":rset.getString(2);
				pid_min_length = rset.getString(3)==null?"":rset.getString(3);
				pid_max_length = rset.getString(4)==null?"":rset.getString(4);
				no_password_notrep = rset.getString(5)==null?"":rset.getString(5);
				password_exp = rset.getString(6)==null?"":rset.getString(6);
				rem_days = rset.getString(7)==null?"":rset.getString(7);
				dormant_days = rset.getString(8)==null?"":rset.getString(8);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getPasswordPolicyList()
	{
		String function_nm="getPasswordPolicyList()";
		try
		{
			queryString="SELECT USR_MIN,USR_MAX,PWD_LEN_MIN,PWD_LEN_MAX,PREV_PWD_NOREP,PWD_EXP_DAYS,PWD_EXP_REMDR,DORMNT_DAYS,MAX_ADMINISTRATOR,MAX_ADMIN "
					+ "FROM FMS_ADMIN_POLICY "
					+ "WHERE EFF_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "ORDER BY EFF_DT DESC";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, eff_dt);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				uid_min_length = rset.getString(1)==null?"":rset.getString(1);
				uid_max_length = rset.getString(2)==null?"":rset.getString(2);
				pid_min_length = rset.getString(3)==null?"":rset.getString(3);
				pid_max_length = rset.getString(4)==null?"":rset.getString(4);
				no_password_notrep = rset.getString(5)==null?"":rset.getString(5);
				password_exp = rset.getString(6)==null?"":rset.getString(6);
				rem_days = rset.getString(7)==null?"":rset.getString(7);
				dormant_days = rset.getString(8)==null?"":rset.getString(8);
				max_admn = rset.getString(9)==null?"":rset.getString(9);
				max_sup_admn = rset.getString(10)==null?"":rset.getString(10);
			}
			rset.close();
			stmt.close();
			
			checkForSuperAdmin();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void checkForSuperAdmin() 
	{
		String function_nm="checkForSuperAdmin()";
		try 
		{
			isSupAdmn = "";
			
			queryString1="SELECT GROUP_CD "
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
			
			rset1.close();
			stmt1.close();
		} 
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void ModuleDetail()
	{
		String function_nm="ModuleDetail()";
		try
		{
			queryString="SELECT MODULE_CD,MODULE_NAME,MODULE_PRIORITY,ACTIVE "
					+ "FROM FMS_MODULE_MST "
					+ "ORDER BY MODULE_CD";
			stmt=conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VMODULE_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VMODULE_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VMODULE_PRIORITY.add(rset.getString(3)==null?"":rset.getString(3));
				VACTIVE.add(rset.getString(4)==null?"":rset.getString(4));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void GroupDetail()
	{
		String function_nm="GroupDetail()";
		try
		{
			String isSupAdmin ="";
			
			queryString1="SELECT GROUP_CD "
					+ "FROM FMS_EMP_GROUP_DTL "
					+ "WHERE COMPANY_CD=? AND GROUP_CD=? AND EMP_CD=? ";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, "1");
			stmt1.setString(3, emp_cd);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				isSupAdmin = "Y";
			}
			rset1.close();
			stmt1.close();
			if(isSupAdmin.equals("Y")) 
			{
				queryString="SELECT GROUP_CD,GROUP_NAME,ACTIVE "
						+ "FROM FMS_GROUP_MST "
						+ "ORDER BY GROUP_CD";
				stmt=conn.prepareStatement(queryString);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					VGROUP_CD.add(rset.getString(1)==null?"":rset.getString(1));
					VGROUP_NM.add(rset.getString(2)==null?"":rset.getString(2));
					VACTIVE.add(rset.getString(3)==null?"":rset.getString(3));
				}
			}
			else 
			{
				queryString="SELECT GROUP_CD,GROUP_NAME,ACTIVE "
						+ "FROM FMS_GROUP_MST "
						+ "WHERE GROUP_NAME NOT IN('Admin') "
						+ "ORDER BY GROUP_CD";
				stmt=conn.prepareStatement(queryString);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					VGROUP_CD.add(rset.getString(1)==null?"":rset.getString(1));
					VGROUP_NM.add(rset.getString(2)==null?"":rset.getString(2));
					VACTIVE.add(rset.getString(3)==null?"":rset.getString(3));
				}
			}
			
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getAccessGroupDetail()
	{
		String function_nm="getAccessGroupDetail()";
		try
		{
			String isSupAdmin ="";
			
			queryString1="SELECT GROUP_CD "
					+ "FROM FMS_EMP_GROUP_DTL "
					+ "WHERE COMPANY_CD=? AND GROUP_CD=? AND EMP_CD=? ";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, "1");
			stmt1.setString(3, emp_cd);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				isSupAdmin = "Y";
			}
			rset1.close();
			stmt1.close();
			if(isSupAdmin.equals("Y")) 
			{
				queryString="SELECT GROUP_CD,GROUP_NAME,ACTIVE "
						+ "FROM FMS_GROUP_MST "
						+ "ORDER BY GROUP_CD";
				stmt=conn.prepareStatement(queryString);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					VGROUP_CD.add(rset.getString(1)==null?"":rset.getString(1));
					VGROUP_NM.add(rset.getString(2)==null?"":rset.getString(2));
					VACTIVE.add(rset.getString(3)==null?"":rset.getString(3));
				}
			}
			else 
			{
				queryString="SELECT GROUP_CD,GROUP_NAME,ACTIVE "
						+ "FROM FMS_GROUP_MST "
						+ "WHERE GROUP_NAME NOT IN('Admin','Administrator') "
						+ "ORDER BY GROUP_CD";
				stmt=conn.prepareStatement(queryString);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					VGROUP_CD.add(rset.getString(1)==null?"":rset.getString(1));
					VGROUP_NM.add(rset.getString(2)==null?"":rset.getString(2));
					VACTIVE.add(rset.getString(3)==null?"":rset.getString(3));
				}
			}
			
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void GroupDetailWithActiveStatus()
	{
		String function_nm="GroupDetailWithActiveStatus()";
		try
		{
			String isSupAdmin ="";
			
			queryString1="SELECT GROUP_CD "
					+ "FROM FMS_EMP_GROUP_DTL "
					+ "WHERE COMPANY_CD=? AND GROUP_CD=? AND EMP_CD=? ";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, "1");
			stmt1.setString(3, emp_cd);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				isSupAdmin = "Y";
			}
			rset1.close();
			stmt1.close();
			
			if(isSupAdmin.equals("Y")) 
			{
				queryString="SELECT GROUP_CD,GROUP_NAME,ACTIVE "
						+ "FROM FMS_GROUP_MST "
						+ "WHERE ACTIVE='Y'  "
						+ "ORDER BY GROUP_CD";
				stmt=conn.prepareStatement(queryString);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					VGROUP_CD.add(rset.getString(1)==null?"":rset.getString(1));
					VGROUP_NM.add(rset.getString(2)==null?"":rset.getString(2));
					VACTIVE.add(rset.getString(3)==null?"":rset.getString(3));
				}
			}
			else 
			{
				queryString="SELECT GROUP_CD,GROUP_NAME,ACTIVE "
						+ "FROM FMS_GROUP_MST "
						+ "WHERE ACTIVE='Y' AND GROUP_NAME NOT IN('Admin','Administrator') "
						+ "ORDER BY GROUP_CD";
				stmt=conn.prepareStatement(queryString);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					VGROUP_CD.add(rset.getString(1)==null?"":rset.getString(1));
					VGROUP_NM.add(rset.getString(2)==null?"":rset.getString(2));
					VACTIVE.add(rset.getString(3)==null?"":rset.getString(3));
				}
			}
			
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void SubModuleDetail()
	{
		String function_nm="SubModuleDetail()";
		try
		{
			queryString="SELECT DISTINCT SUB_MENU_CD,SUB_MENU_NM "
					+ "FROM FMS_FORM_MST "
					+ "WHERE MODULE_CD=? "
					+ "ORDER BY SUB_MENU_NM ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, module_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VSUB_MODULE_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VSUB_MODULE_NM.add(rset.getString(2)==null?"":rset.getString(2));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void MenuMst()
	{
		String function_nm="MenuMst()";
		try
		{
			queryString="SELECT FORM_CD,FORM_NAME "
					+ "FROM FMS_FORM_MST "
					+ "WHERE MODULE_CD=? "
					+ "ORDER BY FORM_NAME";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, module_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VMENU_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VMENU_NM.add(rset.getString(2)==null?"":rset.getString(2));
			}
			rset.close();
			stmt.close();
			
			queryString="SELECT DISTINCT SUB_MENU_CD,SUB_MENU_NM "
					+ "FROM FMS_FORM_MST "
					+ "WHERE MODULE_CD=? "
					+ "ORDER BY SUB_MENU_NM";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, module_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VMENU_GROUP_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VMENU_GROUP_NM.add(rset.getString(2)==null?"":rset.getString(2));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getMenuDetail()
	{
		String function_nm="getMenuDetail()";
		try
		{
			queryString="SELECT FORM_NAME,CLASSPATH,SUB_MENU_CD,FORM_TYPE,SUB_MENU_SEQ,FLAG "
					+ "FROM FMS_FORM_MST "
					+ "WHERE FORM_CD=? AND MODULE_CD=? "
					+ "ORDER BY SUB_MENU_CD,SUB_MENU_SEQ,FORM_NAME ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, menu_cd);
			stmt.setString(2, module_cd);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				menu_name = rset.getString(1)==null?"":rset.getString(1);
				menu_path = rset.getString(2)==null?"":rset.getString(2);
				group_cd = rset.getString(3)==null?"0":rset.getString(3);
				menu_type = rset.getString(4)==null?"F":rset.getString(4);
				group_priority = rset.getString(5)==null?"":rset.getString(5);
				status_flag = rset.getString(6)==null?"":rset.getString(6);
				
				if(group_priority.equals(""))
				{
					queryString1="SELECT MAX(SUB_MENU_SEQ) FROM FMS_FORM_MST "
							+ "WHERE MODULE_CD=? AND SUB_MENU_CD=?";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, module_cd);
					stmt1.setString(2, group_cd);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						group_priority = ""+(rset1.getInt(1)+1);
					}
				}
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void AccessRightDetail()
	{
		String function_nm="AccessRightDetail()";
		try
		{
			int count=0;
			queryString="SELECT A.FORM_CD,A.FORM_NAME,B.MODULE_CD, B.MODULE_NAME, "
					+ "(CASE WHEN (SELECT COUNT(*) FROM FMS_GROUP_ACCESS C WHERE C.GROUP_CD=? AND C.READ_ACCESS='Y' AND C.FORM_CD=A.FORM_CD AND C.COMPANY_CD=?) > 0 THEN 'Y' ELSE 'N' END) READ_ACCESS, "
					+ "(CASE WHEN (SELECT COUNT(*) FROM FMS_GROUP_ACCESS C WHERE C.GROUP_CD=? AND C.WRITE_ACCESS='Y' AND C.FORM_CD=A.FORM_CD AND C.COMPANY_CD=?) > 0 THEN 'Y' ELSE 'N' END) WRITE_ACCESS, "
					+ "(CASE WHEN (SELECT COUNT(*) FROM FMS_GROUP_ACCESS C WHERE C.GROUP_CD=? AND C.CHECK_ACCESS='Y' AND C.FORM_CD=A.FORM_CD AND C.COMPANY_CD=?) > 0 THEN 'Y' ELSE 'N' END) CHECK_ACCESS, "
					+ "(CASE WHEN (SELECT COUNT(*) FROM FMS_GROUP_ACCESS C WHERE C.GROUP_CD=? AND C.PRINT_ACCESS='Y' AND C.FORM_CD=A.FORM_CD AND C.COMPANY_CD=?) > 0 THEN 'Y' ELSE 'N' END) PRINT_ACCESS, "
					+ "(CASE WHEN (SELECT COUNT(*) FROM FMS_GROUP_ACCESS C WHERE C.GROUP_CD=? AND C.DELETE_ACCESS='Y' AND C.FORM_CD=A.FORM_CD AND C.COMPANY_CD=?) > 0 THEN 'Y' ELSE 'N' END) DELETE_ACCESS, "
					+ "(CASE WHEN (SELECT COUNT(*) FROM FMS_GROUP_ACCESS C WHERE C.GROUP_CD=? AND C.AUDIT_ACCESS='Y' AND C.FORM_CD=A.FORM_CD AND C.COMPANY_CD=?) > 0 THEN 'Y' ELSE 'N' END) AUDIT_ACCESS, "
					+ "(CASE WHEN (SELECT COUNT(*) FROM FMS_GROUP_ACCESS C WHERE C.GROUP_CD=? AND C.AUTHORIZE_ACCESS='Y' AND C.FORM_CD=A.FORM_CD AND C.COMPANY_CD=?) > 0 THEN 'Y' ELSE 'N' END) AUTHORIZE_ACCESS, "
					+ "(CASE WHEN (SELECT COUNT(*) FROM FMS_GROUP_ACCESS C WHERE C.GROUP_CD=? AND C.APPROVE_ACCESS='Y' AND C.FORM_CD=A.FORM_CD AND C.COMPANY_CD=?) > 0 THEN 'Y' ELSE 'N' END) APPROVE_ACCESS, "
					+ "(CASE WHEN (SELECT COUNT(*) FROM FMS_GROUP_ACCESS C WHERE C.GROUP_CD=? AND C.EXECUTE_ACCESS='Y' AND C.FORM_CD=A.FORM_CD AND C.COMPANY_CD=?) > 0 THEN 'Y' ELSE 'N' END) EXECUTE_ACCESS "
					+ "FROM FMS_FORM_MST A, FMS_MODULE_MST B "
					+ "WHERE A.FLAG='Y' AND A.MODULE_CD=B.MODULE_CD ";
				//if(module_cd.equals("All"))
				{
					queryString+= "AND B.MODULE_CD=? ";
				}
				
				if(!sub_module_cd.equals("All"))
				{
					queryString+="AND A.SUB_MENU_CD=? ";
				}
				queryString+= "ORDER BY B.MODULE_CD,A.FORM_CD,A.FORM_TYPE ";
				
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++count, group_cd);
			stmt.setString(++count, comp_cd);
			stmt.setString(++count, group_cd);
			stmt.setString(++count, comp_cd);
			stmt.setString(++count, group_cd);
			stmt.setString(++count, comp_cd);
			stmt.setString(++count, group_cd);
			stmt.setString(++count, comp_cd);
			stmt.setString(++count, group_cd);
			stmt.setString(++count, comp_cd);
			stmt.setString(++count, group_cd);
			stmt.setString(++count, comp_cd);
			stmt.setString(++count, group_cd);
			stmt.setString(++count, comp_cd);
			stmt.setString(++count, group_cd);
			stmt.setString(++count, comp_cd);
			stmt.setString(++count, group_cd);
			stmt.setString(++count, comp_cd);
			
			stmt.setString(++count, module_cd);
			if(!sub_module_cd.equals("All"))
			{
				stmt.setString(++count, sub_module_cd);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VMENU_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VMENU_NM.add(rset.getString(2)==null?"":rset.getString(2));
				
				VREAD_ACS.add(rset.getString(5)==null?"N":rset.getString(5));
				VWRITE_ACS.add(rset.getString(6)==null?"N":rset.getString(6));
				VCHECK_ACS.add(rset.getString(7)==null?"N":rset.getString(7));
				VPRINT_ACS.add(rset.getString(8)==null?"N":rset.getString(8));
				VDELETE_ACS.add(rset.getString(9)==null?"N":rset.getString(9));
				VAUDIT_ACS.add(rset.getString(10)==null?"N":rset.getString(10));
				VAUTHORIZE_ACS.add(rset.getString(11)==null?"N":rset.getString(11));
				VAPPROVE_ACS.add(rset.getString(12)==null?"N":rset.getString(12));
				VEXECUTE_ACS.add(rset.getString(13)==null?"N":rset.getString(13));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void EmpMasterNoStatus()
	{
		String function_nm="EmpMasterNoStatus()";
		try
		{
			queryString="SELECT EMP_CD,EMP_NM "
					+ "FROM FMS_EMP_MST "
					+ "ORDER BY EMP_NM";
			stmt=conn.prepareStatement(queryString);
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
		
	public void EmpMaster()
	{
		String function_nm="EmpMaster()";
		try
		{
			queryString="SELECT EMP_CD,EMP_NM "
					+ "FROM FMS_EMP_MST "
					+ "WHERE EMP_STATUS=?"
					+ "ORDER BY EMP_NM";
			stmt=conn.prepareStatement(queryString);
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

	public void getUserDetail()
	{
		String function_nm="getUserDetail()";
		try
		{
			int count=0;
			queryString="SELECT EMP_NM,EMP_UID,EMAIL_ID,EMP_STATUS,REMARK,"
					+ "PH_NO,MOB_NO "
					+ "FROM FMS_EMP_MST "
					+ "WHERE EMP_CD=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++count, user_cd);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				emp_nm = rset.getString(1)==null?"":rset.getString(1);
				emp_uid = rset.getString(2)==null?"":rset.getString(2);
				email_id = rset.getString(3)==null?"":rset.getString(3);
				emp_status = rset.getString(4)==null?"Y":rset.getString(4);
				remark = rset.getString(5)==null?"":rset.getString(5);
				phone_no = rset.getString(6)==null?"":rset.getString(6);
				mobile_no = rset.getString(7)==null?"":rset.getString(7);
			}
			else
			{
				emp_nm="";
				emp_uid="";
				email_id="";
				emp_status="Y";
				remark="";
				phone_no="";
				mobile_no="";
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void CompanyMaster()
	{
		utilBean.getEffectiveCompanyOwnerList();
		VCOMPANY_CD=utilBean.getCOUNTERPARTY_CD();
		VCOMPANY_NM=utilBean.getCOUNTERPARTY_NM();
		VCOMPANY_ABBR=utilBean.getCOUNTERPARTY_ABBR();
	}
	
	public void getAccessGroupAllocationDetail()
	{
		String function_nm="getAccessGroupAllocationDetail()";
		try
		{
			queryString="SELECT A.GROUP_CD,B.GROUP_NAME,TO_CHAR(A.FROM_DT,'DD/MM/YYYY'),TO_CHAR(A.TO_DT,'DD/MM/YYYY') "
					+ "FROM FMS_EMP_GROUP_DTL A, FMS_GROUP_MST B "
					+ "WHERE EMP_CD=? AND A.GROUP_CD = B.GROUP_CD AND A.COMPANY_CD=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, user_cd);
			stmt.setString(2, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VALLOCATED_GRP_CD.add(rset.getString(1)==null?"0":rset.getString(1));
				VALLOCATED_GRP_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VALLOCATED_FRM_DT.add(rset.getString(3)==null?"":rset.getString(3));
				VALLOCATED_TO_DT.add(rset.getString(4)==null?"":rset.getString(4));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getAuditTrail()
	{
		String function_nm="getAuditTrail()";
		try 
		{
			queryString="SELECT TO_CHAR(LOG_DT,'DD/MM/YYYY'),LOG_TIME,LOG_MACH_ID,LOG_UID,FORM_NAME,REMARKS,NEW_VALUE,OLD_VALUE,MODULE_NAME,COMPANY_CD "
					+ "FROM FMS_ALL_LOG "
					+ "WHERE LOG_DT >= TO_DATE(?,'DD/MM/YYYY') AND LOG_DT <= TO_DATE(?,'DD/MM/YYYY') AND (COMPANY_CD=? OR COMPANY_CD IS NULL)  "
					+ "ORDER BY LOG_DT DESC, TO_DATE(LOG_TIME,'HH24:MI:SS') DESC";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, from_dt);
			stmt.setString(2, to_dt);
			stmt.setString(3, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VAUDIT_DT.add(rset.getString(1)==null?"":rset.getString(1));
				VAUDIT_TIME.add(rset.getString(2)==null?"":rset.getString(2));
				VMACHINE_ID.add(rset.getString(3)==null?"":rset.getString(3));
				VEMP_NM.add(rset.getString(4)==null?"":rset.getString(4));
				VMENU_NM.add(rset.getString(5)==null?"":rset.getString(5));
				VREMARK.add(rset.getString(6)==null?"":rset.getString(6));
				VNEW_VALUE.add(rset.getString(7)==null?"":rset.getString(7));
				VOLD_VALUE.add(rset.getString(8)==null?"":rset.getString(8));
				VMODULE_NM.add(rset.getString(9)==null?"":rset.getString(9));
				VCOMPANY_ABBR.add(utilBean.getCompanyAbbr(rset.getString(10)==null?"":rset.getString(10)));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getEmpList()
	{
		String function_nm="getEmpList()";
		try
		{
			
			queryString="SELECT EMP_CD,EMP_NM,EMP_UID,EMP_STATUS "
					+ "FROM FMS_EMP_MST "
					+ "ORDER BY EMP_NM";
			stmt=conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VEMP_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VEMP_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VEMP_UID.add(rset.getString(3)==null?"":rset.getString(3));
				V_EMP_STATUS.add(rset.getString(4)==null?"":rset.getString(4));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getActiveEmpList()
	{
		String function_nm="getActiveEmpList()";
		try
		{	
			queryString="SELECT EMP_CD,EMP_NM,EMP_UID,EMP_STATUS "
					+ "FROM FMS_EMP_MST "
					+ "WHERE EMP_STATUS='Y' "
					+ "ORDER BY EMP_NM";
			stmt=conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VEMP_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VEMP_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VEMP_UID.add(rset.getString(3)==null?"":rset.getString(3));
				V_EMP_STATUS.add(rset.getString(4)==null?"":rset.getString(4));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public int getMaxAdminNo() 
	{
		String function_nm="getMaxAdminNo()";
		int maxAdminNo =0;
		
		try 
		{
			String sysdate = utilDate.getSysdate();
			
			queryString="SELECT MAX_ADMIN "
					+ "FROM FMS_ADMIN_POLICY "
					+ "WHERE EFF_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "ORDER BY EFF_DT DESC";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, sysdate);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				maxAdminNo = Integer.parseInt(rset.getString(1)==null?"":rset.getString(1));
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return maxAdminNo;
	}
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	String opration = "";
	public void setOpration(String opration) {this.opration = opration;}
	String eff_dt = "";
	public void setEff_dt(String eff_dt) {this.eff_dt = eff_dt;}
	
	String module_cd = "";
	String menu_cd = "";
	String menu_name = "";
	String menu_path = "";
	String menu_type = "";
	String group_priority = "";
	String group_cd = "";
	String emp_cd = "";
	String user_cd = "";
	String emp_nm = "";
	String emp_uid = "";
	String email_id = "";
	String emp_status = "";
	String remark = "";
	String sub_module_cd = "";
	String from_dt = "";
	String to_dt = "";
	String company_cd = "";
	String phone_no = "";
	String mobile_no = "";

	String uid_min_length = "";
	String uid_max_length = "";
	String pid_min_length = "";
	String pid_max_length = "";
	String no_password_notrep = "";
	String password_exp = "";
	String rem_days = "";
	String dormant_days = "";
	String max_admn = "";
	String max_sup_admn = "";
	String isSupAdmn = "";
	
	String status_flag="";
	
	public String getUid_min_length() {return uid_min_length;}
	public String getUid_max_length() {return uid_max_length;}
	public String getPid_min_length() {return pid_min_length;}
	public String getPid_max_length() {return pid_max_length;}
	public String getNo_password_notrep() {return no_password_notrep;}
	public String getPassword_exp() {return password_exp;}
	public String getRem_days() {return rem_days;}
	public String getDormant_days() {return dormant_days;}
	public String getMax_admn() {return max_admn;}
	public String getMax_sup_admn() {return max_sup_admn;}
	public String getIsSupAdmn() {return isSupAdmn;}
	
	public String getMenu_name() {return menu_name;}
	public String getMenu_path() {return menu_path;}
	public String getMenu_type() {return menu_type;}
	public String getGroup_cd() {return group_cd;}
	public String getEmp_nm() {return emp_nm;}
	public String getEmp_uid() {return emp_uid;}
	public String getEmail_id() {return email_id;}
	public String getEmp_status() {return emp_status;}
	public String getRemark() {return remark;}
	public String getGroup_priority() {return group_priority;}
	public String getCompany_cd() {return company_cd;}
	public String getPhone_no() {return phone_no;}
	public String getMobile_no() {return mobile_no;}
	
	public String getStatus_flag() {return status_flag;}
	
	public void setModule_cd(String module_cd) {this.module_cd = module_cd;}
	public void setMenu_cd(String menu_cd) {this.menu_cd = menu_cd;}
	public void setGroup_cd(String group_cd) {this.group_cd = group_cd;}
	public void setEmp_cd(String emp_cd) {this.emp_cd = emp_cd;}
	public void setUser_cd(String user_cd) {this.user_cd = user_cd;}
	public void setSub_module_cd(String sub_module_cd) {this.sub_module_cd = sub_module_cd;}
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	
	Vector VMODULE_CD = new Vector();
	Vector VMODULE_NM = new Vector();
	Vector VMODULE_PRIORITY = new Vector();
	Vector VACTIVE = new Vector();
	Vector VGROUP_CD = new Vector();
	Vector VGROUP_NM = new Vector();
	Vector VMENU_CD = new Vector();
	Vector VMENU_NM = new Vector();
	Vector VMENU_GROUP_CD = new Vector();
	Vector VMENU_GROUP_NM = new Vector();
	Vector VREAD_ACS = new Vector();
	Vector VWRITE_ACS = new Vector();
	Vector VCHECK_ACS = new Vector();
	Vector VPRINT_ACS = new Vector();
	Vector VDELETE_ACS = new Vector();
	Vector VAUDIT_ACS = new Vector();
	Vector VAUTHORIZE_ACS = new Vector();
	Vector VAPPROVE_ACS = new Vector();
	Vector VEXECUTE_ACS = new Vector();
	Vector VEMP_CD = new Vector();
	Vector VEMP_NM = new Vector();
	Vector VEMP_UID = new Vector();
	Vector VALLOCATED_GRP_CD = new Vector();
	Vector VALLOCATED_GRP_NM = new Vector();
	Vector VALLOCATED_FRM_DT = new Vector();
	Vector VALLOCATED_TO_DT = new Vector();
	Vector VSUB_MODULE_CD = new Vector();
	Vector VSUB_MODULE_NM = new Vector();
	Vector VREMARK = new Vector();
	Vector VAUDIT_DT = new Vector();
	Vector VAUDIT_TIME = new Vector();
	Vector VNEW_VALUE = new Vector();
	Vector VOLD_VALUE = new Vector();
	Vector VMACHINE_ID = new Vector();
	Vector VCOMPANY_CD = new Vector();
	Vector VCOMPANY_NM = new Vector();
	Vector VCOMPANY_ABBR = new Vector();
	Vector V_EMP_STATUS = new Vector();
	
	public Vector getVMODULE_CD() {return VMODULE_CD;}
	public Vector getVMODULE_NM() {return VMODULE_NM;}
	public Vector getVMODULE_PRIORITY() {return VMODULE_PRIORITY;}
	public Vector getVACTIVE() {return VACTIVE;}
	public Vector getVGROUP_CD() {return VGROUP_CD;}
	public Vector getVGROUP_NM() {return VGROUP_NM;}
	public Vector getVMENU_CD() {return VMENU_CD;}
	public Vector getVMENU_NM() {return VMENU_NM;}
	public Vector getVMENU_GROUP_CD() {return VMENU_GROUP_CD;}
	public Vector getVMENU_GROUP_NM() {return VMENU_GROUP_NM;}
	public Vector getVREAD_ACS() {return VREAD_ACS;}
	public Vector getVWRITE_ACS() {return VWRITE_ACS;}
	public Vector getVCHECK_ACS() {return VCHECK_ACS;}
	public Vector getVPRINT_ACS() {return VPRINT_ACS;}
	public Vector getVDELETE_ACS() {return VDELETE_ACS;}
	public Vector getVAUDIT_ACS() {return VAUDIT_ACS;}
	public Vector getVAUTHORIZE_ACS() {return VAUTHORIZE_ACS;}
	public Vector getVAPPROVE_ACS() {return VAPPROVE_ACS;}
	public Vector getVEXECUTE_ACS() {return VEXECUTE_ACS;}
	public Vector getVEMP_CD() {return VEMP_CD;}
	public Vector getVEMP_NM() {return VEMP_NM;}
	public Vector getVEMP_UID() {return VEMP_UID;}
	public Vector getVALLOCATED_GRP_CD() {return VALLOCATED_GRP_CD;}
	public Vector getVALLOCATED_GRP_NM() {return VALLOCATED_GRP_NM;}
	public Vector getVALLOCATED_FRM_DT() {return VALLOCATED_FRM_DT;}
	public Vector getVALLOCATED_TO_DT() {return VALLOCATED_TO_DT;}
	public Vector getVSUB_MODULE_CD() {return VSUB_MODULE_CD;}
	public Vector getVSUB_MODULE_NM() {return VSUB_MODULE_NM;}
	public Vector getVREMARK() {return VREMARK;}
	public Vector getVAUDIT_DT() {return VAUDIT_DT;}
	public Vector getVAUDIT_TIME() {return VAUDIT_TIME;}
	public Vector getVNEW_VALUE() {return VNEW_VALUE;}
	public Vector getVOLD_VALUE() {return VOLD_VALUE;}
	public Vector getVMACHINE_ID() {return VMACHINE_ID;}
	public Vector getVCOMPANY_CD() {return VCOMPANY_CD;}
	public Vector getVCOMPANY_NM() {return VCOMPANY_NM;}
	public Vector getVCOMPANY_ABBR() {return VCOMPANY_ABBR;}
	public Vector getV_EMP_STATUS() {return V_EMP_STATUS;}
}
