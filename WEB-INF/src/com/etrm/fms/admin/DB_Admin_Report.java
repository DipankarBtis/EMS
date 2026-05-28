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

// Coded By Arth Patel On 20230505 : for Report of User Details

public class DB_Admin_Report
{
	String db_src_file_name="DB_Admin_Report.java";
	
	Connection conn; 
	PreparedStatement stmt;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	ResultSet rset; 
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	String query="";
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
	               	if(callFlag.equalsIgnoreCase("ACCESS_RIGHT_MATRIX"))
		    		{
	    				AccessRightMatrixReport();   				
		    		}
	                else if(callFlag.equalsIgnoreCase("USER DETAILS")) 
					{
	                	fetchUserDetails();
	                }
	                else if(callFlag.equalsIgnoreCase("SYSTEM_ERROR")) 
					{
	                	getSystemErrorList();
	                }
	               	
	               	conn.close();
	    			conn = null;
	    		}
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

	// Function Developer: Arth Patel
	// Function Modified By: Harsh Maheta 20231126
	
	public void fetchUserDetails() 
	{
		String function_nm="fetchUserDetails()";
		try 
		{
			utilBean.getEffectiveCompanyOwnerList(conn);
			VCOMPANY_CD=utilBean.getCOUNTERPARTY_CD();
			//VCOMPANY_NM=utilBean.getCOUNTERPARTY_NM();
			VCOMPANY_ABBR=utilBean.getCOUNTERPARTY_ABBR();
			
			int count=0;
			queryString = "SELECT EMP_CD,EMP_NM,EMP_STATUS, EMP_UID, EMAIL_ID,TO_CHAR(ENT_DT,'DD/MM/YYYY'),LOCK_STATUS "
					+ "FROM FMS_EMP_MST "
					+ "WHERE 0=0 ";
			//if(rd_flag.equals("A"))
			
			if (rd_flag.equals("E")) 
			{
				queryString += "AND TRUNC(ENT_DT) BETWEEN TRUNC(TO_DATE(?,'DD/MM/YYYY')) AND TRUNC(TO_DATE(?,'DD/MM/YYYY')) ";
			}
			
			if(filter_status.equals("e")) 
			{
				queryString +="AND (emp_status ='Y' OR emp_status IS NULL) ";
			}
			else if (filter_status.equals("d")) 
			{
				queryString +="AND emp_status ='N' ";
			}
			else if (filter_status.equals("x")) 
			{
				queryString +="AND emp_status ='D' ";
			}
			else if (filter_status.equals("r")) 
			{
				queryString +="AND emp_status ='R' ";
			}
			if(lock_status_dd.equals("L")) 
			{
				queryString +="AND LOCK_STATUS = 'Y' ";
			}
			else if (lock_status_dd.equals("U")) 
			{
				queryString +="AND LOCK_STATUS IS NULL ";
			}
			queryString	+= "ORDER BY EMP_NM ASC ";
			
			stmt=conn.prepareStatement(queryString);
			if (rd_flag.equals("E")) 
			{
				stmt.setString(++count, from_dt);
				stmt.setString(++count, set_to_dt);
			}
			rset = stmt.executeQuery();
			while(rset.next()) 
			{
				emp_cd = rset.getString(1)==null?"":rset.getString(1);
				String emp_nm = rset.getString(2)==null?"":rset.getString(2);
				 
				VEMP_CD.add(emp_cd);
				VEMP_NM.add(emp_nm);
				 
				String status = rset.getString(3)==null?"":rset.getString(3);
				if(status.equals("N"))
				{
					VSTATUS.add("Disabled");
				}
				else if(status.equals("Y"))
				{
					VSTATUS.add("Enabled");
				}
				else if(status.equals("D"))
				{
					VSTATUS.add("Dormant");
				}
				else if(status.equals("R"))
				{
					VSTATUS.add("Removed");
				}
				VEMP_UID.add(rset.getString(4)==null?"":rset.getString(4));
				VEMAIL_ID.add(rset.getString(5)==null?"":rset.getString(5));
				
				String lock_status = rset.getString(7)==null?"":rset.getString(7);
				String ent_dt =rset.getString(6)==null?"":rset.getString(6);
				
				VLOCK_STATUS.add(lock_status);
				VENT_DT.add(ent_dt);
				
				int index=0;
				
				queryString1="SELECT EMP_UID , EMP_NM , EMP_STATUS , TO_CHAR(MODIFY_DT,'DD/MM/YYYY') , SEQ_NO , LOCK_STATUS, MODIFY_BY "
						+ "FROM FMS_EMP_MST_LOG "
						+ "WHERE EMP_CD=? "
						+ "ORDER BY SEQ_NO DESC ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, emp_cd);
				rset1 = stmt1.executeQuery();
				while(rset1.next()) 
				{
					index+=1;
					
					V_EMP_UID.add(rset1.getString(1)==null?"":rset1.getString(1));
					
					String emp_name= rset1.getString(2)==null?"Y":rset1.getString(2);
					String emp_status= rset1.getString(3)==null?"Y":rset1.getString(3);
					
					if(emp_status.equalsIgnoreCase("Y"))
					{
						V_EMP_STATUS.add("Enabled");
					}
					else if  (emp_status.equalsIgnoreCase("D")) 
					{
						V_EMP_STATUS.add("Dormant");
					}
					else
					{
						V_EMP_STATUS.add("Disabled");
					}
					
					String log_mod_by = rset1.getString(7)==null?"0":rset1.getString(7);
					
					V_MODIFY_DT.add(rset1.getString(4)==null?ent_dt:rset1.getString(4));
					V_SEQ_NO.add(rset1.getString(5)==null?"":rset1.getString(5));
					
					if(log_mod_by.equals("0")) 
					{
						V_MODIFIED_BY.add("SYSTEM");
					}
					else 
					{
						V_MODIFIED_BY.add(utilBean.getEmpName(conn,rset1.getString(7)==null?"":rset1.getString(7)));
					}
					
					V_LOCKED_STATUS.add(rset1.getString(6)==null?"":rset1.getString(6));
				}
				rset1.close();
				stmt1.close();
				VINDEX.add(index);
				
				Vector TEMP_GRP_NM = new Vector(); 
				for(int i=0; i<VCOMPANY_CD.size();i++)
				{
					String companyCd=""+VCOMPANY_CD.elementAt(i);
					String group_nm = "";
					String active = "";
					String valid_dt ="";
					
					queryString3 = "SELECT B.GROUP_NAME , TO_CHAR(A.TO_DT,'DD/MM/YYYY') "
							+ "FROM FMS_EMP_GROUP_DTL A, FMS_GROUP_MST B "
							+ "WHERE A.GROUP_CD=B.GROUP_CD AND trunc(A.TO_DT)>=trunc(SYSDATE) " 
						    + "AND A.EMP_CD =? AND A.COMPANY_CD=? " ;
					stmt3=conn.prepareStatement(queryString3);
					stmt3.setString(1, emp_cd);
					stmt3.setString(2, companyCd);
					rset3 = stmt3.executeQuery();				
					while(rset3.next())
					{
						valid_dt = rset3.getString(2);
						group_nm += rset3.getString(1)+" ("+valid_dt+") "+", ";
					}
					rset3.close();
					stmt3.close();
					
					if(group_nm.length()>3)
					{
						group_nm = group_nm.substring(0,group_nm.length()-2);
					}
					
					TEMP_GRP_NM.add(group_nm);
				}
				VGROUP_NM.add(TEMP_GRP_NM);
			}
			rset.close();
			stmt.close();
			
			int dormant_days= 0;
			String sysdate = utilDate.getSysdate();
			
			String queryString="SELECT DORMNT_DAYS "
					+ "FROM FMS_ADMIN_POLICY "
					+ "WHERE EFF_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "ORDER BY EFF_DT DESC";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, sysdate);
			ResultSet rset4=stmt.executeQuery();
			if(rset4.next())
			{
				dormant_days = Integer.parseInt(rset4.getString(1)==null?"":rset4.getString(1));
			}
			rset4.close();
			stmt.close();
            VDORMANT_DAYS_COUNT.add(dormant_days);
		}
		catch(Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}	
	}
	
	
	// Function Developer: Harsh Maheta
	public void GroupDetail()
	{
		String function_nm="GroupDetail()";
		try
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
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	// Function Developer: Harsh Maheta
	public void ModuleDetail()
	{
		String function_nm="ModuleDetail()";
		try
		{
			queryString="SELECT DISTINCT MODULE_CD,MODULE_NAME,MODULE_PRIORITY,ACTIVE"
					+ " FROM FMS_MODULE_MST"
					+ " ORDER BY MODULE_CD";
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
	
	// Function Developer: Harsh Maheta
	// Modifier: Harsh Patel
	public void AccessRightMatrixReport()
	{
		String function_nm="AccessRightMatrixReport()";
		try
		{		
			ModuleDetail();
			GroupDetail();
			getUserAccessList();
			
			if(module_cd.equals("0") || module_cd.equals(""))
			{
				VTEMP_MODULE_CD=VMODULE_CD;
				VTEMP_MODULE_NM=VMODULE_NM;
			}
			else
			{
				if(VMODULE_CD.contains(module_cd))
				{
					VTEMP_MODULE_CD.add(VMODULE_CD.get(VMODULE_CD.indexOf(module_cd)));
					VTEMP_MODULE_NM.add(VMODULE_NM.get(VMODULE_CD.indexOf(module_cd)));
				}
			}
			
			for(int i=0; i<VTEMP_MODULE_CD.size(); i++)
			{
				String ModuleCd=""+VTEMP_MODULE_CD.elementAt(i);
				
				int index=0;
				
				queryString="SELECT DISTINCT SUB_MENU_CD,SUB_MENU_NM "
						+ "FROM FMS_FORM_MST "
						+ "WHERE MODULE_CD=? AND FLAG='Y' "
						+ "ORDER BY SUB_MENU_CD ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, ModuleCd);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String sub_mod_cd = rset.getString(1)==null?"":rset.getString(1);
					VSUB_MODULE_CD.add(sub_mod_cd);
					VSUB_MODULE_NM.add(rset.getString(2)==null?"":rset.getString(2));
					
					index+=1;
					int sub_index=0;
					
					queryString1="SELECT FORM_NAME,FORM_CD,SUB_MENU_SEQ,CLASSPATH,SUB_MENU_CD,FORM_TYPE "
							+ "FROM FMS_FORM_MST "
							+ "WHERE MODULE_CD=? AND SUB_MENU_CD=? AND FLAG='Y' "
							+ "ORDER BY SUB_MENU_CD,SUB_MENU_SEQ,FORM_NAME ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, ModuleCd);
					stmt1.setString(2, sub_mod_cd);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						sub_index+=1;
						
						String menu_cd = rset1.getString(2)==null?"":rset1.getString(2); 
						
						VMENU_NM.add(rset1.getString(1)==null?"":rset1.getString(1));
						VMENU_CD.add(menu_cd);
						VSUB_MENU_SEQ.add(rset1.getString(3)==null?"":rset1.getString(3));
						
						int inner_sub_index=0;
						for (int j=0; j<VGROUP_CD.size();j++) 
						{
							inner_sub_index+=1;
							
							queryString2="SELECT READ_ACCESS,WRITE_ACCESS,CHECK_ACCESS,PRINT_ACCESS,DELETE_ACCESS,"
									+ "AUDIT_ACCESS, AUTHORIZE_ACCESS, APPROVE_ACCESS, EXECUTE_ACCESS "
									+ "FROM FMS_GROUP_ACCESS "
									+ "WHERE COMPANY_CD=? AND FORM_CD=? AND GROUP_CD=? ";
							queryString2 +=" ORDER BY GROUP_CD";
							stmt2=conn.prepareStatement(queryString2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, menu_cd);
							stmt2.setString(3, ""+VGROUP_CD.elementAt(j));
							rset2=stmt2.executeQuery();
							if(rset2.next()) 
							{	
								String read_acs = rset2.getString(1)==null?"":rset2.getString(1);
								if(read_acs.equals("Y")) 
								{
									VREAD_ACS.add(read_acs);
								}
								else 
								{
									VREAD_ACS.add("");
								}
								
								String write_acs = rset2.getString(2)==null?"":rset2.getString(2);
								if(write_acs.equals("Y")) 
								{
									VWRITE_ACS.add(write_acs);
								}
								else
								{
									VWRITE_ACS.add("");
								}
								
								String check_acs = rset2.getString(3)==null?"":rset2.getString(3);
								if(check_acs.equals("Y")) 
								{
									VCHECK_ACS.add(check_acs);
								}
								else
								{
									VCHECK_ACS.add("");
								}
								
								String print_acs =rset2.getString(4)==null?"":rset2.getString(4);
								if(print_acs.equals("Y")) 
								{
									VPRINT_ACS.add(print_acs);
								}
								else 
								{
									VPRINT_ACS.add("");
								}
								
								String delete_acs =rset2.getString(5)==null?"":rset2.getString(5);
								if(delete_acs.equals("Y")) 
								{
									VDELETE_ACS.add(delete_acs);
								} 
								else 
								{
									VDELETE_ACS.add("");
								}
								
								String audit_acs =rset2.getString(6)==null?"":rset2.getString(6);
								if(audit_acs.equals("Y")) 
								{
									VAUDIT_ACS.add(audit_acs);
								}
								else 
								{
									VAUDIT_ACS.add("");
								}
								
								String authorize_acs =rset2.getString(7)==null?"":rset2.getString(7);
								if(authorize_acs.equals("Y")) 
								{
									VAUTHORIZE_ACS.add(authorize_acs);
								}
								else 
								{
									VAUTHORIZE_ACS.add("");
								}
								
								String approve_acs =rset2.getString(8)==null?"":rset2.getString(8);
								if(approve_acs.equals("Y")) 
								{
									VAPPROVE_ACS.add(approve_acs);
								}
								else 
								{
									VAPPROVE_ACS.add("");
								}
								
								String execute_acs =rset2.getString(9)==null?"":rset2.getString(9);
								if(execute_acs.equals("Y")) 
								{
									VEXECUTE_ACS.add(execute_acs);
								}
								else
								{
									VEXECUTE_ACS.add("");
								}			
							}
							else
							{
								VREAD_ACS.add("");
								VWRITE_ACS.add("");
								VCHECK_ACS.add("");
								VPRINT_ACS.add("");
								VDELETE_ACS.add("");
								VAUDIT_ACS.add("");
								VAUTHORIZE_ACS.add("");
								VAPPROVE_ACS.add("");
								VEXECUTE_ACS.add("");
							}
							rset2.close();
							stmt2.close();
						}
						VINNER_SUB_INDEX.add(inner_sub_index);
					}
					rset1.close();
					stmt1.close();
					VSUB_INDEX.add(sub_index);
				}
				rset.close();
				stmt.close();
				
				VINDEX.add(index);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getUserAccessList()
	{
		String function_nm="getUserAccessList()";
		try
		{
			String sysdate = utilDate.getSysdate();
			for (int i=0; i<VGROUP_CD.size();i++) 
			{
				String grp_cd=""+VGROUP_CD.elementAt(i);
				String user_nm="";
				queryString="SELECT EMP_CD "
						+ "FROM FMS_EMP_GROUP_DTL "
						+ "WHERE COMPANY_CD=? AND GROUP_CD=? "
						+ "AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT>=TO_DATE(?,'DD/MM/YYYY')";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, grp_cd);
				stmt.setString(3, sysdate);
				stmt.setString(4, sysdate);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String e_cd = rset.getString(1)==null?"":rset.getString(1);
					String e_nm = utilBean.getEmpName(conn,e_cd);
					if(user_nm.equals(""))
					{
						user_nm=e_nm;
					}
					else
					{
						user_nm+=", "+e_nm;
					}
				}
				rset.close();
				stmt.close();
				VUSER_ACCESS_GROUP.add(user_nm);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSystemErrorList()
	{
		String function_nm="getSystemErrorList()";
		try
		{
			queryString="SELECT ERROR_CD,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS'),SRC_FILE_NM,FUNCTION_NM,ERROR_MSG,STACK_TRACE "
					+ "FROM FMS_ERROR_LOG "
					+ "WHERE TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE(?,'DD/MM/YYYY') "
					+ "AND TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY') <= TO_DATE(?,'DD/MM/YYYY') "
					+ "ORDER BY ENT_DT DESC";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, from_dt);
			stmt.setString(2, to_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VERROR_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VERROR_ENT_DT.add(rset.getString(2)==null?"":rset.getString(2));
				VERROR_SRC_FILE.add(rset.getString(3)==null?"":rset.getString(3));
				VERROR_FUNC_NM.add(rset.getString(4)==null?"":rset.getString(4));
				VERROR_MSG.add(rset.getString(5)==null?"":rset.getString(5));
				VERROR_STACK_TRACE.add(rset.getString(6)==null?"":rset.getString(6).replaceAll("\n", "<br>"));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	String callFlag = "";
	public void setCallFlag(String callFlag){this.callFlag = callFlag;}
	String comp_cd="";
	public void setComp_cd(String comp_cd){this.comp_cd = comp_cd;}
	
	String emp_id = "";
	String rd_flag="";
	String filter_status="";
	String lock_status_dd="";
	String method_name="";
	String from_dt="";
	String to_dt="";
	String set_to_dt="";
	String st_dt="";
	String end_dt ="";
	String emp_cd = "";
	String module_cd = "";
	String sub_module_cd = "";
	String group_cd = "";
	
	public String getFrom_dt() {return from_dt;}
	public String getTo_dt() {return to_dt;}
	public String getSet_to_dt() {return set_to_dt;}
	
	public void setFrom_dt(String from_dt){this.from_dt = from_dt;}
	public void setTo_dt(String to_dt){this.to_dt = to_dt;}
	public void setSet_to_dt(String set_to_dt){this.set_to_dt = set_to_dt;}
	public void setRd_flag(String rd_flag){this.rd_flag = rd_flag;}
	public void setFilter_status(String filter_status){this.filter_status = filter_status;}
	public void setLock_status_dd(String lock_status_dd){this.lock_status_dd = lock_status_dd;}
	public void setModule_cd(String module_cd) {this.module_cd = module_cd;}
	public void setSub_module_cd(String sub_module_cd) {this.sub_module_cd = sub_module_cd;}
	public void setGroup_cd(String group_cd) {this.group_cd = group_cd;}
	
	Vector VEMP_CD = new Vector();
	Vector VEMP_NM = new Vector();
	Vector VSTATUS = new Vector();
	Vector VLOCK_STATUS = new Vector();
	Vector VEMP_UID = new Vector();
	Vector VEMAIL_ID = new Vector();
	Vector VENT_DT = new Vector();
	Vector VGROUP_NM = new Vector();
	Vector VINDEX = new Vector();
	Vector V_EMP_UID = new Vector();
	Vector V_EMP_NM = new Vector();
	Vector V_EMP_STATUS = new Vector();
	Vector V_MODIFY_DT = new Vector();
	Vector V_SEQ_NO = new Vector();
	Vector V_MODIFIED_BY = new Vector();
	Vector V_LOCKED_STATUS = new Vector();
	Vector VDORMANT_DAYS_COUNT = new Vector();
	
	Vector VGROUP_CD = new Vector();
	Vector VMENU_CD = new Vector();
	Vector VMENU_NM = new Vector();
	Vector VMODULE_CD = new Vector();
	Vector VMODULE_NM = new Vector();
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
	Vector VACTIVE = new Vector();
	
	Vector VINNER_SUB_INDEX = new Vector();
	Vector VSUB_INDEX = new Vector();
	Vector VSUB_MODULE_CD = new Vector();
	Vector VSUB_MODULE_NM = new Vector();
	Vector VMODULE_PRIORITY = new Vector();
	Vector VSUB_MENU_SEQ =new Vector();
	
	Vector VTEMP_MODULE_CD = new Vector();
	Vector VTEMP_MODULE_NM = new Vector();
	
	Vector VUSER_ACCESS_GROUP = new Vector();
	
	Vector VERROR_CD = new Vector();
	Vector VERROR_ENT_DT = new Vector();
	Vector VERROR_SRC_FILE = new Vector();
	Vector VERROR_FUNC_NM = new Vector();
	Vector VERROR_MSG = new Vector();
	Vector VERROR_STACK_TRACE = new Vector();
	
	Vector VCOMPANY_CD = new Vector();
	Vector VCOMPANY_NM = new Vector();
	Vector VCOMPANY_ABBR = new Vector();
	
	public Vector getVEMP_CD() {return VEMP_CD;}
	public Vector getVEMP_NM() {return VEMP_NM;}
	public Vector getVSTATUS() {return VSTATUS;}
	public Vector getVLOCK_STATUS() {return VLOCK_STATUS;}
	public Vector getVEMP_UID() {return VEMP_UID;}
	public Vector getVEMAIL_ID() {return VEMAIL_ID;}
	public Vector getVENT_DT() {return VENT_DT;}
	public Vector getVGROUP_NM() {return VGROUP_NM;}
	public Vector getVINDEX() {return VINDEX;}
	public Vector getV_EMP_UID() {return V_EMP_UID;}
	public Vector getV_EMP_NM() {return V_EMP_NM;}
	public Vector getV_EMP_STATUS() {return V_EMP_STATUS;}
	public Vector getV_MODIFY_DT() {return V_MODIFY_DT;}
	public Vector getV_SEQ_NO() {return V_SEQ_NO;}
	public Vector getV_MODIFIED_BY() {return V_MODIFIED_BY;}
	public Vector getV_LOCKED_STATUS() {return V_LOCKED_STATUS;}
	public Vector getVDORMANT_DAYS_COUNT() {return VDORMANT_DAYS_COUNT;}
	
	public Vector getVGROUP_CD() {return VGROUP_CD;}
	public Vector getVMENU_CD() {return VMENU_CD;}
	public Vector getVMENU_NM() {return VMENU_NM;}
	public Vector getVMODULE_CD() {return VMODULE_CD;}
	public Vector getVMODULE_NM() {return VMODULE_NM;}
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
	public Vector getVACTIVE() {return VACTIVE;}
	public Vector getVSUB_INDEX() {return VSUB_INDEX;}
	public Vector getVSUB_MENU_SEQ() {return VSUB_MENU_SEQ;}
	public Vector getVINNER_SUB_INDEX() {return VINNER_SUB_INDEX;}
	public Vector getVSUB_MODULE_CD() {return VSUB_MODULE_CD;}
	public Vector getVSUB_MODULE_NM() {return VSUB_MODULE_NM;}
	public Vector getVMODULE_PRIORITY() {return VMODULE_PRIORITY;}
	
	public Vector getVTEMP_MODULE_CD() {return VTEMP_MODULE_CD;}
	public Vector getVTEMP_MODULE_NM() {return VTEMP_MODULE_NM;}
	
	public Vector getVUSER_ACCESS_GROUP() {return VUSER_ACCESS_GROUP;}
	
	public Vector getVERROR_CD() {return VERROR_CD;}
	public Vector getVERROR_ENT_DT() {return VERROR_ENT_DT;}
	public Vector getVERROR_SRC_FILE() {return VERROR_SRC_FILE;}
	public Vector getVERROR_FUNC_NM() {return VERROR_FUNC_NM;}
	public Vector getVERROR_MSG() {return VERROR_MSG;}
	public Vector getVERROR_STACK_TRACE() {return VERROR_STACK_TRACE;}
	
	public Vector getVCOMPANY_CD() {return VCOMPANY_CD;}
	public Vector getVCOMPANY_NM() {return VCOMPANY_NM;}
	public Vector getVCOMPANY_ABBR() {return VCOMPANY_ABBR;}
}
