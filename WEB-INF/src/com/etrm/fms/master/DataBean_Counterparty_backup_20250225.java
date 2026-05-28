package com.etrm.fms.master;

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
//CR Date			: 22/09/2022 
//Status	  		: Developing
public class DataBean_Counterparty 
{
	String db_src_file_name="DataBean_Counterparty.java";

	Connection conn; 
	PreparedStatement stmt;
	PreparedStatement stmt0;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt5;
	PreparedStatement stmt6;
	PreparedStatement stmt7;
	PreparedStatement stmt8;
	ResultSet rset;
	ResultSet rset0; 
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
	ResultSet rset6;
	ResultSet rset7;
	ResultSet rset8;
	String queryString="";
	String queryString0="";
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
	    			if(callFlag.equalsIgnoreCase("COUNTERPARTY_MST"))
	    			{
	    				getCountryMst();
	    				getStateMst();
	    				
	    				if(opration.equalsIgnoreCase("MODIFY"))
	    				{
	    					//getCounterpartyMst();
	    					
	    					if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
	    					{
	    						getCounterpartyDetail();
	    						getCounterpartyAddressDetail();
	    					}
	    					else
	    					{
	    						status="Y";
	    						state="";
	    						zone="0";
	    						country="0";
	    					}
	    				}
	    				else
	    				{
	    					status="Y";
	    					state="";
	    					zone="0";
	    					country="0";
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("COUNTERPARTY_ADMIN"))
	    			{
	    				//getCounterpartyMst();
	    				getEntityRoleRequest();
	    			}
	    			else if(callFlag.equalsIgnoreCase("ENTITY_MASTER"))
	    			{
	    				getCountryMst();
	    				getStateMst();
	    				getEntityMaster();
	    				getSectorMaster();
	    				getNcfCategoryMst();
	    				if(entity_role.equals("B"))
	    				{
	    					getCompanyOwnerDetail();
	    				}
	    				else if(entity_role.equals("G"))
	    				{
	    					getCompanyExchangeDetail();
	    				}
	    				else
	    				{
	    					getCounterpartyDetail();
	    				}
	    				getEntityAddressDetail();
	    				getCounterpartyRequesterApproverNote();
	    				getCounterpartyPlantDetail();
	    				fetchGovtStatNoList();
	    				getCounterpartyBankDetail();
	    				if(entity_role.equals("R") || entity_role.equals("G") || entity_role.equals("S") || entity_role.equals("H") || entity_role.equals("V"))
	    				{
	    					getCounterpartyBuDetail();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("COUNTERPARTY_MATRIX"))
	    			{
	    				getCounterpartyMatrix();
	    			}
	    			else if(callFlag.equalsIgnoreCase("ENTITY_CONTACT_DETAILS"))
	    			{
	    				getEntityMaster();
	    				if(entity_role.equals("G") || entity_role.equals("H") || entity_role.equals("V") || entity_role.equals("S"))
	    				{
	    					getBuPlantDetail();
	    				}
	    				else
	    				{
	    					getPlantDetail();
	    				}
	    				getAddressType();
	    				//getEntityContactDetail();
	    				getEntityContactDetailV2();
	    			}
	    			else if(callFlag.equalsIgnoreCase("ENTITY_TAX_MASTER"))
	    			{
	    				getBusinessPlantList();
	    				getEntityMaster();
	    				getEntityPlantDetail();
	    			}
	    			else if(callFlag.equalsIgnoreCase("ENTITY_TURNOVER_ENTRY"))
	    			{
	    				getFinancialYearList();
	    				getEntityMaster();
	    				getTurnoverDetail();
	    			}
	    			else if(callFlag.equalsIgnoreCase("ENTITY_EMAIL_SETUP"))
	    			{
	    				getEntityMaster();
	    				if(entity_role.equals("G") || entity_role.equals("H") || entity_role.equals("S") || entity_role.equals("V"))
	    				{
	    					getBuPlantDetail();
	    				}
	    				else
	    				{
	    					getPlantDetail();
	    				}
	    				getAddressType();
	    				if(!notification_type.equals("0") && !notification_type.equals(""))
	    				{
	    					getEntityContactPersonListRlng();
	    					getEntityContactPersonListDlng();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("COUNTERPARTY_AUDIT_REPORT"))
	    			{
	    				getCounterpartyAuditReport();
	    			}
	    			else if(callFlag.equals("COUNTERPARTY_LIST")) 
	    			{
	    				getCounterpartyList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("ENTITY_TCS_TDS_MASTER"))
	    			{
	    				getEntityMaster();
	    				getEntityTcsTdsTaxDetail();
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
	    	if(rset0 != null){try{rset0.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset1 != null){try{rset1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset2 != null){try{rset2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset3 != null){try{rset3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset4 != null){try{rset4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset5 != null){try{rset5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset6 != null){try{rset6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset7 != null){try{rset7.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset8 != null){try{rset8.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt0 != null){try{stmt0.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
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
	
	public void getCountryMst()
	{
		String function_nm="getCountryMst()";

		try
		{
			utilBean.getCountryMaster();
			VCOUNTRY_CODE= utilBean.getCOUNTRY_CODE();
			VCOUNTRY_NM = utilBean.getCOUNTRY_NM();
			VISO_CODE = utilBean.getISO_CODE();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	public void getStateMst()
	{
		String function_nm="getStateMst()";

		try
		{
			utilBean.getStateMaster();
			VTIN= utilBean.getTIN();
			VSTATE_CODE = utilBean.getSTATE_CODE();
			VSTATE_NM = utilBean.getSTATE_NM();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCounterpartyMst()
	{
		String function_nm="getCounterpartyMst()";

		try
		{
			utilBean.getCounterpartyMaster();
			VCOUNTERPARTY_CD= utilBean.getCOUNTERPARTY_CD();
			VCOUNTERPARTY_NM = utilBean.getCOUNTERPARTY_NM();
			VCOUNTERPARTY_ABBR = utilBean.getCOUNTERPARTY_ABBR();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getNcfCategoryMst()
	{
		String function_nm="getNcfCategoryMst()";

		try
		{
			String query_string = "SELECT NCF_CATEGORY FROM FMS_NCF_CATEGORY_MST";
			PreparedStatement stmt=conn.prepareStatement(query_string);
			ResultSet rset=stmt.executeQuery();
			while(rset.next())
			{
				VNCF_CATEGORY.add(rset.getString(1)==null?"":rset.getString(1));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCounterpartyDetail()
	{
		String function_nm="getCounterpartyDetail()";

		try
		{
			queryString="SELECT TO_CHAR(EFF_DT,'DD/MM/YYYY'),COUNTERPARTY_NM,COUNTERPARTY_ABBR,PAN_NO,TO_CHAR(PAN_ISSUE_DT,'DD/MM/YYYY'),NOTES,STATUS,KYC,IGX,"
					+ "CATEGORY,WEB_ADDR,SAP_CODE,NCF_CATEGORY "
					+ "FROM FMS_COUNTERPARTY_MST A "
					+ "WHERE COUNTERPARTY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, counterparty_cd);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				eff_dt = rset.getString(1)==null?"":rset.getString(1);
				name = rset.getString(2)==null?"":rset.getString(2);
				abbr = rset.getString(3)==null?"":rset.getString(3);
				pan_no = rset.getString(4)==null?"":rset.getString(4);
				pan_dt = rset.getString(5)==null?"":rset.getString(5);
				notes = rset.getString(6)==null?"":rset.getString(6);
				status = rset.getString(7)==null?"Y":rset.getString(7);
				kyc_flg = rset.getString(8)==null?"N":rset.getString(8);
				igx_flg = rset.getString(9)==null?"N":rset.getString(9);
				category = rset.getString(10)==null?"0":rset.getString(10);
				web_addr = rset.getString(11)==null?"":rset.getString(11);
				sap_code = rset.getString(12)==null?"":rset.getString(12);
				ncf_category = rset.getString(13)==null?"0":rset.getString(13);
			}
			else
			{
				status="Y";
				category="0";
				ncf_category="0";
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCompanyOwnerDetail()
	{
		String function_nm="getCompanyOwnerDetail()";

		try
		{
			queryString="SELECT TO_CHAR(EFF_DT,'DD/MM/YYYY'),COMPANY_NM,COMPANY_ABBR,PAN_NO,TO_CHAR(PAN_ISSUE_DT,'DD/MM/YYYY'),NOTES,STATUS,"
					+ "CATEGORY,WEB_ADDR,SAP_CODE,NCF_CATEGORY "
					+ "FROM FMS_COMPANY_OWNER_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_OWNER_MST B WHERE A.COMPANY_CD=B.COMPANY_CD)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, counterparty_cd);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				eff_dt = rset.getString(1)==null?"":rset.getString(1);
				name = rset.getString(2)==null?"":rset.getString(2);
				abbr = rset.getString(3)==null?"":rset.getString(3);
				pan_no = rset.getString(4)==null?"":rset.getString(4);
				pan_dt = rset.getString(5)==null?"":rset.getString(5);
				notes = rset.getString(6)==null?"":rset.getString(6);
				status = rset.getString(7)==null?"Y":rset.getString(7);
				category = rset.getString(8)==null?"0":rset.getString(8);
				web_addr = rset.getString(9)==null?"":rset.getString(9);
				sap_code = rset.getString(10)==null?"":rset.getString(10);
				ncf_category = rset.getString(11)==null?"":rset.getString(11);
			}
			else
			{
				status="Y";
				category="0";
				ncf_category="0";
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCompanyExchangeDetail()
	{
		String function_nm="getCompanyExchangeDetail()";

		try
		{
			queryString="SELECT TO_CHAR(EFF_DT,'DD/MM/YYYY'),COUNTERPARTY_NM,COUNTERPARTY_ABBR,PAN_NO,TO_CHAR(PAN_ISSUE_DT,'DD/MM/YYYY'),NOTES,STATUS,"
					+ "CATEGORY,WEB_ADDR,SAP_CODE,NCF_CATEGORY "
					+ "FROM FMS_COMPANY_EXCHG_MST A "
					+ "WHERE COUNTERPARTY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_EXCHG_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, counterparty_cd);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				eff_dt = rset.getString(1)==null?"":rset.getString(1);
				name = rset.getString(2)==null?"":rset.getString(2);
				abbr = rset.getString(3)==null?"":rset.getString(3);
				pan_no = rset.getString(4)==null?"":rset.getString(4);
				pan_dt = rset.getString(5)==null?"":rset.getString(5);
				notes = rset.getString(6)==null?"":rset.getString(6);
				status = rset.getString(7)==null?"Y":rset.getString(7);
				category = rset.getString(8)==null?"0":rset.getString(8);
				web_addr = rset.getString(9)==null?"":rset.getString(9);
				sap_code = rset.getString(10)==null?"":rset.getString(10);
				ncf_category = rset.getString(11)==null?"":rset.getString(11);
			}
			else
			{
				status="Y";
				category="0";
				ncf_category="0";
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCounterpartyAddressDetail()
	{
		String function_nm="getCounterpartyAddressDetail()";

		try
		{
			queryString="SELECT ADDRESS_TYPE,TO_CHAR(EFF_DT,'DD/MM/YYYY'),ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL "
					+ "FROM FMS_COUNTERPARTY_ADDR_MST A "
					+ "WHERE COUNTERPARTY_CD=? AND ADDRESS_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_ADDR_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.ADDRESS_TYPE=B.ADDRESS_TYPE)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, counterparty_cd);
			stmt.setString(2, "R");
			rset=stmt.executeQuery();
			if(rset.next())
			{
				reg_eff_dt = rset.getString(2)==null?"":rset.getString(2);
				address = rset.getString(3)==null?"":rset.getString(3);
				city = rset.getString(4)==null?"":rset.getString(4);
				pin = rset.getString(5)==null?"":rset.getString(5);
				state = rset.getString(6)==null?"0":rset.getString(6);
				zone = rset.getString(7)==null?"0":rset.getString(7);
				country = rset.getString(8)==null?"0":rset.getString(8);
				phone = rset.getString(9)==null?"":rset.getString(9);
				mobile = rset.getString(10)==null?"":rset.getString(10);
				alt_phone = rset.getString(11)==null?"":rset.getString(11);
				fax1 = rset.getString(12)==null?"":rset.getString(12);
				fax2 = rset.getString(13)==null?"":rset.getString(13);
				email = rset.getString(14)==null?"":rset.getString(14);
			}
			else
			{
				state="0";
				zone="0";
				country="0";
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	public void getCompnayOwnerAddressDetail()
	{
		String function_nm="getCompnayOwnerAddressDetail()";

		try
		{
			queryString="SELECT ADDRESS_TYPE,TO_CHAR(EFF_DT,'DD/MM/YYYY'),ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL "
					+ "FROM FMS_COMPANY_OWNER_ADDR_MST A "
					+ "WHERE COMPANY_CD=? AND ADDRESS_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_OWNER_ADDR_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.ADDRESS_TYPE=B.ADDRESS_TYPE)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, counterparty_cd);
			stmt.setString(2, "R");
			rset=stmt.executeQuery();
			if(rset.next())
			{
				reg_eff_dt = rset.getString(2)==null?"":rset.getString(2);
				address = rset.getString(3)==null?"":rset.getString(3);
				city = rset.getString(4)==null?"":rset.getString(4);
				pin = rset.getString(5)==null?"":rset.getString(5);
				state = rset.getString(6)==null?"0":rset.getString(6);
				zone = rset.getString(7)==null?"0":rset.getString(7);
				country = rset.getString(8)==null?"0":rset.getString(8);
				phone = rset.getString(9)==null?"":rset.getString(9);
				mobile = rset.getString(10)==null?"":rset.getString(10);
				alt_phone = rset.getString(11)==null?"":rset.getString(11);
				fax1 = rset.getString(12)==null?"":rset.getString(12);
				fax2 = rset.getString(13)==null?"":rset.getString(13);
				email = rset.getString(14)==null?"":rset.getString(14);
			}
			else
			{
				state="0";
				zone="0";
				country="0";
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getEntityAddressDetail()
	{
		String function_nm="getEntityAddressDetail()";

		try
		{
			for(int i=0;i<3;i++)
			{
				int index=i;
				String add_flg="";
				if(i==0)
				{
					add_flg="R";
				}
				else if(i==1)
				{
					add_flg="C";
				}
				else if(i==2)
				{
					add_flg="B";
				}
				
				int cont=0;
				if(entity_role.equals("B"))
				{
					queryString="SELECT ADDRESS_TYPE,TO_CHAR(EFF_DT,'DD/MM/YYYY'),ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL "
							+ "FROM FMS_COMPANY_OWNER_ADDR_MST A "
							+ "WHERE COMPANY_CD=? AND ADDRESS_TYPE=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_OWNER_ADDR_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.ADDRESS_TYPE=B.ADDRESS_TYPE)";
				}
				else if(entity_role.equals("G"))
				{
					queryString="SELECT ADDRESS_TYPE,TO_CHAR(EFF_DT,'DD/MM/YYYY'),ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL "
							+ "FROM FMS_COMPANY_EXCHG_ADDR_MST A "
							+ "WHERE COUNTERPARTY_CD=? AND ADDRESS_TYPE=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_EXCHG_ADDR_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.ADDRESS_TYPE=B.ADDRESS_TYPE)";
				}
				else if(i==0)
				{
					queryString="SELECT ADDRESS_TYPE,TO_CHAR(EFF_DT,'DD/MM/YYYY'),ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL "
							+ "FROM FMS_COUNTERPARTY_ADDR_MST A "
							+ "WHERE COUNTERPARTY_CD=? AND ADDRESS_TYPE=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_ADDR_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.ADDRESS_TYPE=B.ADDRESS_TYPE)";
				}
				else
				{
					queryString="SELECT ADDRESS_TYPE,TO_CHAR(EFF_DT,'DD/MM/YYYY'),ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL "
							+ "FROM FMS_ENTITY_ADDR_MST A "
							+ "WHERE COUNTERPARTY_CD=? AND COMPANY_CD=? AND ADDRESS_TYPE=? AND ENTITY=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_ADDR_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.ADDRESS_TYPE=B.ADDRESS_TYPE AND A.ENTITY=B.ENTITY)";
				}
				stmt=conn.prepareStatement(queryString);
				if(entity_role.equals("B"))
				{
					stmt.setString(++cont, counterparty_cd);
					stmt.setString(++cont, add_flg);
				}
				else if(entity_role.equals("G"))
				{
					stmt.setString(++cont, counterparty_cd);
					stmt.setString(++cont, add_flg);
				}
				else if(i==0)
				{
					stmt.setString(++cont, counterparty_cd);
					stmt.setString(++cont, add_flg);
				}
				else
				{
					stmt.setString(++cont, counterparty_cd);
					stmt.setString(++cont, comp_cd);
					stmt.setString(++cont, add_flg);
					stmt.setString(++cont, entity_role);
				}
				rset=stmt.executeQuery();
				if(rset.next())
				{
					REG_EFF_DT[index] = rset.getString(2)==null?"":rset.getString(2);
					ADDRESS_FLAG[index] = add_flg;
					ADDRESS[index] = rset.getString(3)==null?"":rset.getString(3);
					CITY[index] = rset.getString(4)==null?"":rset.getString(4);
					PIN[index] = rset.getString(5)==null?"":rset.getString(5);
					STATE[index] = rset.getString(6)==null?"":rset.getString(6);
					ZONE[index] = rset.getString(7)==null?"0":rset.getString(7);
					COUNTRY[index] = rset.getString(8)==null?"0":rset.getString(8);
					PHONE[index] = rset.getString(9)==null?"":rset.getString(9);
					MOBILE[index] = rset.getString(10)==null?"":rset.getString(10);
					ALT_PHONE[index] = rset.getString(11)==null?"":rset.getString(11);
					FAX1[index] = rset.getString(12)==null?"":rset.getString(12);
					FAX2[index] = rset.getString(13)==null?"":rset.getString(13);
					EMAIL[index] = rset.getString(14)==null?"":rset.getString(14);
				}
				else
				{
					REG_EFF_DT[index] = "";
					ADDRESS_FLAG[index] = add_flg;
					ADDRESS[index] =  "";
					CITY[index] =  "";
					PIN[index]  =  "";
					STATE[index]=  "";
					ZONE[index] = "0";
					COUNTRY[index]= "0";
					PHONE[index] =  "";
					MOBILE[index]=  "";
					ALT_PHONE[index]= "";
					FAX1[index]=  "";
					FAX2[index]=  "";
					EMAIL[index]=  "";
				}
				rset.close();
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getEntityRoleRequest()
	{
		String function_nm="getEntityRoleRequest()";

		try 
		{
			name=utilBean.getCounterpartyName(counterparty_cd);
			
			queryString="SELECT ENTITY,COUNT(*) "
					+ "FROM FMS_ENTITY_REQ_DTL "
					+ "WHERE (STATUS=? OR STATUS=?)  "
					+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY IN ('C','T','R','S','V','H') GROUP BY ENTITY ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "R");
			stmt.setString(2, "A");
			stmt.setString(3, comp_cd);
			stmt.setString(4, counterparty_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String entity = rset.getString(1)==null?"":rset.getString(1);
				if(entity.equals("C"))
				{
					customer=rset.getInt(2) > 0?"Y":"N";
				}
				else if(entity.equals("T"))	
				{
					trader=rset.getInt(2) > 0?"Y":"N";
				}
				else if(entity.equals("R"))
				{
					transporter=rset.getInt(2) > 0?"Y":"N";
				}
				else if(entity.equals("S"))
				{
					surveyor=rset.getInt(2) > 0?"Y":"N";
				}
				else if(entity.equals("V"))
				{
					vessel_agent=rset.getInt(2) > 0?"Y":"N";
				}
				else if(entity.equals("H"))
				{
					custom_hagent=rset.getInt(2) > 0?"Y":"N";
				}
			}
			rset.close();
			stmt.close();
		
			queryString3="SELECT COUNTERPARTY_CD,ENTITY,REMARK,REQ_BY,TO_CHAR(REQ_DT,'DD/MM/YYYY'),SEQ_NO "
					+ "FROM FMS_ENTITY_REQ_DTL "
					+ "WHERE STATUS=? AND COMPANY_CD=? ";
			stmt3=conn.prepareStatement(queryString3);
			stmt3.setString(1, "R");
			stmt3.setString(2, comp_cd);
			rset3=stmt3.executeQuery();
			while(rset3.next())
			{
				String counterpty_cd = rset3.getString(1)==null?"":rset3.getString(1);
				VCOUNTERPTY_CD.add(counterpty_cd);
				VCOUNTERPTY_NM.add(""+utilBean.getCounterpartyName(counterpty_cd));
				VENTITY_ROLE.add(rset3.getString(2)==null?"":rset3.getString(2));
				VREMARK.add(rset3.getString(3)==null?"":rset3.getString(3));
				
				String req_cd = rset3.getString(4)==null?"":rset3.getString(4);
				VREQ_BY.add(""+utilBean.getEmpName(req_cd));
				VREQ_DT.add(rset3.getString(5)==null?"":rset3.getString(5));
				VSEQ_NO.add(rset3.getString(6)==null?"":rset3.getString(6));
			}
			rset3.close();
			stmt3.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getEntityMaster()
	{
		String function_nm="getEntityMaster()";

		try
		{
			if(!entity_role.equals("0"))
			{
				if(entity_role.equals("B"))
				{
					queryString = "SELECT COMPANY_CD,COMPANY_NM,COMPANY_ABBR "
							+ "FROM FMS_COMPANY_OWNER_MST A "
							+ "WHERE COMPANY_CD=? "
							+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COMPANY_OWNER_MST B WHERE A.COMPANY_CD=B.COMPANY_CD) ";
					queryString+= "ORDER BY COMPANY_NM";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					rset=stmt.executeQuery();
					while (rset.next()) 
					{
						VCOUNTERPARTY_CD.add(rset.getString(1)==null?"":rset.getString(1));
						VCOUNTERPARTY_NM.add(rset.getString(2)==null?"":rset.getString(2));
						VCOUNTERPARTY_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
					}
					rset.close();
					stmt.close();
				}
				else if(entity_role.equals("G"))
				{
					queryString1 = "SELECT COUNTERPARTY_CD,COUNTERPARTY_NM,COUNTERPARTY_ABBR "
							+ "FROM FMS_COMPANY_EXCHG_MST A "
							+ "WHERE EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COMPANY_EXCHG_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD) ";
					queryString1+= "ORDER BY COUNTERPARTY_NM";
					stmt1=conn.prepareStatement(queryString1);
					rset1=stmt1.executeQuery();
					while (rset1.next()) 
					{
						VCOUNTERPARTY_CD.add(rset1.getString(1)==null?"":rset1.getString(1));
						VCOUNTERPARTY_NM.add(rset1.getString(2)==null?"":rset1.getString(2));
						VCOUNTERPARTY_ABBR.add(rset1.getString(3)==null?"":rset1.getString(3));
					}
					rset1.close();
					stmt1.close();
				}
				else
				{
					int cont=0;
					queryString2 = "SELECT A.COUNTERPARTY_CD,A.COUNTERPARTY_NM,A.COUNTERPARTY_ABBR "
							+ "FROM FMS_COUNTERPARTY_MST A, FMS_ENTITY_REQ_DTL B "
							+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND B.COMPANY_CD=? AND B.ENTITY=? AND B.STATUS='A' "
							+ "AND  A.EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD) ";
					queryString2+= "ORDER BY COUNTERPARTY_NM";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(++cont, comp_cd);
					stmt2.setString(++cont, entity_role);
					rset2=stmt2.executeQuery();
					while (rset2.next()) 
					{
						VCOUNTERPARTY_CD.add(rset2.getString(1)==null?"":rset2.getString(1));
						VCOUNTERPARTY_NM.add(rset2.getString(2)==null?"":rset2.getString(2));
						VCOUNTERPARTY_ABBR.add(rset2.getString(3)==null?"":rset2.getString(3));
					}
					rset2.close();
					stmt2.close();
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSectorMaster()
	{
		String function_nm="getSectorMaster()";

		try
		{
			queryString="SELECT SECTOR_CD,SECTOR_NAME "
					+ "FROM FMS_SECTOR_MST "
					+ "WHERE STATUS_FLAG=? "
					+ "ORDER BY SECTOR_NAME";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VSECTOR_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VSECTOR_NAME.add(rset.getString(2)==null?"":rset.getString(2));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCounterpartyRequesterApproverNote()
	{
		String function_nm="getCounterpartyRequesterApproverNote()";

		try
		{
			queryString="SELECT ENTITY,REMARK,APRV_NOTE,REQ_BY,TO_CHAR(REQ_DT,'DD/MM/YYYY HH:MM:SS'),APRV_BY,TO_CHAR(APRV_DT,'DD/MM/YYYY HH:MM:SS') "
					+ "FROM FMS_ENTITY_REQ_DTL "
					+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND STATUS!=? AND COMPANY_CD=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, counterparty_cd);
			stmt.setString(2, entity_role);
			stmt.setString(3, "X");
			stmt.setString(4, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String rmk=rset.getString(2)==null?"":rset.getString(2);
				String aprv_note=rset.getString(3)==null?"":rset.getString(3);
				String req_by= ""+utilBean.getEmpName(rset.getString(4)==null?"0":rset.getString(4));
				String req_dt = rset.getString(5)==null?"":rset.getString(5);
				String aprv_by= ""+utilBean.getEmpName(rset.getString(6)==null?"0":rset.getString(6));
				String aprv_dt = rset.getString(7)==null?"":rset.getString(7);
				
				if(requester_approver_note.equals(""))
				{
					if(!aprv_note.equals(""))
					{
						requester_approver_note+=""+aprv_by+" ["+aprv_dt+"] [Approver] :: "+aprv_note;
					}
					requester_approver_note+="\n"+req_by+" ["+req_dt+"] [Requester] :: "+rmk;
				}
				else
				{
					if(!aprv_note.equals(""))
					{
						requester_approver_note+="\n"+aprv_by+" ["+aprv_dt+"] [Approver] :: "+aprv_note;
					}
					requester_approver_note+="\n"+req_by+" ["+req_dt+"] [Requester] :: "+rmk;
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
	
	public void getCounterpartyPlantDetail()
	{
		String function_nm="getCounterpartyPlantDetail()";

		try
		{
			queryString="SELECT COUNTERPARTY_CD,ENTITY,SEQ_NO,TO_CHAR(EFF_DT,'DD/MM/YYYY'),PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,"
					+ "PLANT_ZONE,PLANT_CITY,PLANT_PIN,PLANT_SECTOR "
					+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
					+ "WHERE ENTITY=? AND COUNTERPARTY_CD=? AND COMPANY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, entity_role);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq = rset.getString(3)==null?"0":rset.getString(3);
				VPLANT_SEQ_NO.add(rset.getString(3)==null?"":rset.getString(3));
				VPLANT_EFF_DT.add(rset.getString(4)==null?"":rset.getString(4));
				VPLANT_NAME.add(rset.getString(5)==null?"":rset.getString(5));
				VPLANT_ABBR.add(rset.getString(6)==null?"":rset.getString(6));
				VPLANT_ADDR.add(rset.getString(7)==null?"":rset.getString(7));
				String plant_state = rset.getString(8)==null?"":rset.getString(8);
				VPLANT_STATE.add(plant_state);
				VPLANT_ZONE.add(rset.getString(9)==null?"":rset.getString(9));
				VPLANT_ZONE_NM.add(""+getZoneName(rset.getString(9)==null?"":rset.getString(9)));
				VPLANT_CITY.add(rset.getString(10)==null?"":rset.getString(10));
				VPLANT_PIN.add(rset.getString(11)==null?"":rset.getString(11));
				String sectorCd=rset.getString(12)==null?"":rset.getString(12);
				VPLANT_SECTOR.add(sectorCd);
				VPLANT_SECTOR_NM.add(""+utilBean.getSectorName(sectorCd, comp_cd));
				
				if(!VSTATE_NM.contains(plant_state))
				{
					VOTH_STATE_FLG.add("Y");
				}
				else
				{
					VOTH_STATE_FLG.add("N");
				}
				
				String tax_id="";
				queryString1 = "SELECT A.STAT_NO, B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A,FMS_GOVT_STAT_TAX B "
						+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND A.COMPANY_CD=? "
						+ "AND PLANT_SEQ_NO=? AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, counterparty_cd);
				stmt1.setString(2, entity_role);
				stmt1.setString(3, comp_cd);
				stmt1.setString(4, plant_seq);
				rset1 = stmt1.executeQuery();
				while(rset1.next())
				{
					String stat_no = rset1.getString(1)==null?"":rset1.getString(1);
					String stat_nm = rset1.getString(2)==null?"":rset1.getString(2);
					
					if(tax_id.equals(""))
					{
						tax_id +=""+stat_nm+" "+stat_no;
					}
					else
					{
						tax_id +="<br>"+stat_nm+" "+stat_no;
					}
				}
				rset1.close();
				stmt1.close();
				
				VTAX_ID.add(tax_id);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCounterpartyBankDetail()
	{
		String function_nm="getCounterpartyBankDetail()";

		try
		{
			queryString="SELECT COUNTERPARTY_CD,ENTITY,TO_CHAR(EFF_DT,'DD/MM/YYYY'),BANK_NAME,ACCOUNT_ID,IFSC_CODE,BRANCH_NAME,"
					+ "STATE_NM  "
					+ "FROM FMS_ENTITY_BANK_MST A "
					+ "WHERE ENTITY=? AND COUNTERPARTY_CD=? AND COMPANY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_BANK_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, entity_role);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, comp_cd);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				bank_eff_dt = rset.getString(3)==null?"":rset.getString(3);
				bank_name=rset.getString(4)==null?"":rset.getString(4);
				bank_account_no=rset.getString(5)==null?"":rset.getString(5);
				ifsc_code=rset.getString(6)==null?"":rset.getString(6);
				bank_branch=rset.getString(7)==null?"":rset.getString(7);
				bank_state=rset.getString(8)==null?"":rset.getString(8);
				
				bank_formula=bank_name+", "+bank_branch+", "+bank_state+", A/C No : "+bank_account_no+", IFSC Code : "+ifsc_code;
			}
			rset.close();
			stmt.close();
			
			queryString1="SELECT COUNTERPARTY_CD,ENTITY,TO_CHAR(EFF_DT,'DD/MM/YYYY'),BANK_NAME,ACCOUNT_ID,IFSC_CODE,BRANCH_NAME,"
					+ "STATE_NM  "
					+ "FROM FMS_ENTITY_BANK_MST A "
					+ "WHERE ENTITY=? AND COUNTERPARTY_CD=? AND COMPANY_CD=? "
					+ "ORDER BY EFF_DT DESC";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, entity_role);
			stmt1 .setString(2, counterparty_cd);
			stmt1.setString(3, comp_cd);
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				String bank_eff_dt = rset1.getString(3)==null?"":rset1.getString(3);
				String bank_name=rset1.getString(4)==null?"":rset1.getString(4);
				String bank_account_no=rset1.getString(5)==null?"":rset1.getString(5);
				String ifsc_code=rset1.getString(6)==null?"":rset1.getString(6);
				String bank_branch=rset1.getString(7)==null?"":rset1.getString(7);
				String bank_state=rset1.getString(8)==null?"":rset1.getString(8);
				
				String  bank_formula=bank_name+", "+bank_branch+", "+bank_state+", A/C No : "+bank_account_no+", IFSC Code : "+ifsc_code;
				
				VBANK_FORMULA.add(bank_formula);
				VBANK_EFF_DT.add(bank_eff_dt);
			}
			rset1.close();
			stmt1.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String getZoneName(String zone)
	{
		String function_nm="getZoneName()";

		String name="";
		try
		{
			if(zone.equals("N"))
			{
				name="North";
			}
			else if(zone.equals("S"))
			{
				name="South";
			}
			else if(zone.equals("E"))
			{
				name="East";
			}
			else if(zone.equals("W"))
			{
				name="West";
			}
			else if(zone.equals("C"))
			{
				name="Central";
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return name;
	}
	
	public void fetchGovtStatNoList()
	{
		String function_nm="fetchGovtStatNoList()";

		try 
		{
			queryString = "SELECT NVL(STAT_CD,?),STAT_NM,STAT_TYPE,STATUS,REMARK "
					+ "FROM FMS_GOVT_STAT_TAX "
					+ "ORDER BY STAT_CD";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "0");
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String stat_cd=rset.getString(1)==null?"0":rset.getString(1);
				VSTAT_CD.add(stat_cd);
				VSTAT_NM.add(rset.getString(2)==null?"":rset.getString(2));
				
				String stat_type = rset.getString(3)==null?"0":rset.getString(3);
				
				if(stat_type.trim().equals("S"))
				{
					stat_type = "Sales Invoice";
				}
				else if(stat_type.trim().equals("R"))
				{
					stat_type = "Re-Gas Invoice";
				}
				else if(stat_type.trim().equals("G"))
				{
					stat_type = "General Identification Number";
				}
				else if(stat_type.trim().equals("P"))
				{
					stat_type = "Purchase Invoice";
				}
				VSTAT_TYPE.add(stat_type);
				VSTAT_STATUS.add(rset.getString(4)==null?"0":rset.getString(4));
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCounterpartyMatrix()
	{
		String function_nm="getCounterpartyMatrix()";

		try
		{
			queryString="SELECT A.COUNTERPARTY_CD,TO_CHAR(A.EFF_DT,'DD/MM/YYYY'),A.COUNTERPARTY_NM,A.COUNTERPARTY_ABBR,"
					+ "A.PAN_NO,TO_CHAR(A.PAN_ISSUE_DT,'DD/MM/YYYY'),A.NOTES,A.STATUS,A.KYC,A.IGX,"
					+ "B.ENTITY,B.REQ_BY,TO_CHAR(B.REQ_DT,'DD/MM/YYYY HH:MM:SS'),B.APRV_BY,TO_CHAR(B.APRV_DT,'DD/MM/YYYY HH:MM:SS') "
					+ "FROM FMS_COUNTERPARTY_MST A LEFT JOIN FMS_ENTITY_REQ_DTL B "
					+ "ON A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND B.STATUS != 'X' AND B.COMPANY_CD=? "
					+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD) "
					+ "ORDER BY COUNTERPARTY_NM";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String countpty_cd =rset.getString(1)==null?"":rset.getString(1);
				String status =rset.getString(8)==null?"":rset.getString(8);
				String kyc =rset.getString(9)==null?"":rset.getString(9);
				String igx =rset.getString(10)==null?"":rset.getString(10);
				
				VCOUNTERPARTY_CD.add(countpty_cd);
				VCATEGORY.add(category);
				VCOUNTERPARTY_NM.add(rset.getString(3)==null?"":rset.getString(3));
				VCOUNTERPARTY_ABBR.add(rset.getString(4)==null?"":rset.getString(4));
				
				if(kyc.equals("Y") && igx.equals("Y")){
					VCLEARANCE.add("KYC, IGX");
				}else if(kyc.equals("Y")) {
					VCLEARANCE.add("KYC");
				}else if(igx.equals("Y")) {
					VCLEARANCE.add("IGX");
				}else {
					VCLEARANCE.add("");
				}
				VSTATUS.add(status);
				
				String entity= rset.getString(11)==null?"":rset.getString(11);
				String req_by= ""+utilBean.getEmpName(rset.getString(12)==null?"0":rset.getString(12));
				String req_dt = rset.getString(13)==null?"":rset.getString(13);
				String aprv_by= ""+utilBean.getEmpName(rset.getString(14)==null?"0":rset.getString(14));
				String aprv_dt = rset.getString(15)==null?"":rset.getString(15);
				
				if(entity.equals("C"))
				{
					VENTITY_ROLE.add("Customer");
					VCOLOR.add("lightgreen");
				}
				else if(entity.equals("T"))	
				{
					VENTITY_ROLE.add("Trader");
					VCOLOR.add("skyblue");
				}
				else if(entity.equals("R"))
				{
					VENTITY_ROLE.add("Transporter");
					VCOLOR.add("#ff99ff");
				}
				else if(entity.equals("S"))
				{
					VENTITY_ROLE.add("Surveyor");
					VCOLOR.add("#70FBF1");//Surveyor Color
				}
				else if(entity.equals("V"))
				{
					VENTITY_ROLE.add("Vessel Agent");
					VCOLOR.add("#e6c8be");//Vessel Agent Color
				}
				else if(entity.equals("H"))
				{
					VENTITY_ROLE.add("Custom House Agent");
					VCOLOR.add("#FFCC99");//Custom House Agent Color
				}
				else
				{
					VENTITY_ROLE.add("");
					VCOLOR.add("");
				}
				
				
				VREQUESTER.add(req_by.equals("")?"":req_by+" ["+req_dt+"]");
				VAPPROVER.add(aprv_by.equals("")?"":aprv_by+" ["+aprv_dt+"]");
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getAddressType()
	{
		String function_nm="getAddressType()";

		try
		{
			int cont=0;
			if(entity_role.equals("B"))
			{
				queryString="SELECT DISTINCT ADDRESS_TYPE "
						+ "FROM FMS_COMPANY_OWNER_ADDR_MST A "
						+ "WHERE COMPANY_CD=? ";	
			}
			else if(entity_role.equals("G"))
			{
				queryString="SELECT DISTINCT ADDRESS_TYPE "
						+ "FROM FMS_COMPANY_EXCHG_ADDR_MST A "
						+ "WHERE COUNTERPARTY_CD=? ";	
			}
			else
			{
				queryString="SELECT DISTINCT ADDRESS_TYPE "
						+ "FROM FMS_COUNTERPARTY_ADDR_MST A "
						+ "WHERE COUNTERPARTY_CD=? ";
				queryString+="UNION ";
				queryString+="SELECT DISTINCT ADDRESS_TYPE "
						+ "FROM FMS_ENTITY_ADDR_MST A "
						+ "WHERE COUNTERPARTY_CD=? AND COMPANY_CD=? ";
			}
			stmt=conn.prepareStatement(queryString);
			if(entity_role.equals("B"))
			{
				stmt.setString(++cont, counterparty_cd);
			}
			else if(entity_role.equals("G"))
			{
				stmt.setString(++cont, counterparty_cd);
			}
			else
			{
				stmt.setString(++cont, counterparty_cd);
				stmt.setString(++cont, counterparty_cd);
				stmt.setString(++cont, comp_cd);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String add_type = rset.getString(1)==null?"":rset.getString(1);
				if(add_type.equals("R"))
				{
					VADDRESS_TYPE.add(add_type);
					VADDRESS_NAME.add("Registered");
				}
				else if(add_type.equals("C"))
				{
					VADDRESS_TYPE.add(add_type);
					VADDRESS_NAME.add("Correspondence");
				}
				else if(add_type.equals("B"))
				{
					VADDRESS_TYPE.add(add_type);
					VADDRESS_NAME.add("Billing");
				}
			}
			rset.close();
			stmt.close();
			
			for(int i=0;i<VPLANT_SEQ_NO.size();i++)
			{
				VADDRESS_TYPE.add("P"+VPLANT_SEQ_NO.elementAt(i));
				VADDRESS_NAME.add(""+VPLANT_ABBR.elementAt(i));
			}
			
			for(int i=0;i<VBU_PLANT_SEQ_NO.size();i++)
			{
				VADDRESS_TYPE.add("B"+VBU_PLANT_SEQ_NO.elementAt(i));
				VADDRESS_NAME.add(""+VBU_PLANT_ABBR.elementAt(i));
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getPlantDetail()
	{
		String function_nm="getPlantDetail()";

		try
		{
			utilBean.getEffectiveCounterpartyPlantList(counterparty_cd, entity_role, comp_cd);
			VPLANT_SEQ_NO = utilBean.getPLANT_SEQ_NO();
			VPLANT_ABBR = utilBean.getPLANT_ABBR();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getBuPlantDetail()
	{
		String function_nm="getBuPlantDetail()";

		try
		{
			queryString="SELECT SEQ_NO,PLANT_ABBR "
					+ "FROM FMS_COUNTERPARTY_BU_DTL A "
					+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_BU_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) "
					+ "AND STATUS=? ";
			queryString+= "ORDER BY PLANT_NAME";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, counterparty_cd);
			stmt.setString(2, entity_role);
			stmt.setString(3, comp_cd);
			stmt.setString(4, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VBU_PLANT_SEQ_NO.add(rset.getString(1)==null?"":rset.getString(1));
				VBU_PLANT_ABBR.add(rset.getString(2)==null?"":rset.getString(2));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getEntityContactDetail()
	{
		String function_nm="getEntityContactDetail()";

		try
		{
			queryString="SELECT SEQ_NO,TO_CHAR(EFF_DT,'DD/MM/YYYY'),CONTACT_PERSON,DESIGNATION,PHONE,MOBILE,FAX_1,FAX_2,EMAIL,ADDR_FLAG,ADDL_ADDR_LINE,"
					+ "NOM_FLAG,INV_FLAG,FM_FLAG,PM_FLAG,JT_FLAG,OTHER_FLAG,ACTIVE_FLAG "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, entity_role);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String seq_no=rset.getString(1)==null?"":rset.getString(1);
				String eff_dt=rset.getString(2)==null?"":rset.getString(2);
				
				VSEQ_NO.add(rset.getString(1)==null?"":rset.getString(1));
				VEFF_DT.add(rset.getString(2)==null?"":rset.getString(2));
				VPERSON_NM.add(rset.getString(3)==null?"":rset.getString(3));
				VDESIGNATION.add(rset.getString(4)==null?"":rset.getString(4));
				//VPHONE.add(rset.getString(5)==null?"":rset.getString(5));
				VMOBILE.add(rset.getString(6)==null?"":rset.getString(6));
				//VFAX1.add(rset.getString(7)==null?"":rset.getString(7));
				//VFAX2.add(rset.getString(8)==null?"":rset.getString(8));
				VEMAIL.add(rset.getString(9)==null?"":rset.getString(9));
				
				//VADD_ADDRESS.add(rset.getString(11)==null?"":rset.getString(11));
				VNOM.add(rset.getString(12)==null?"":rset.getString(12));
				VINV.add(rset.getString(13)==null?"N":rset.getString(13));
				VFM.add(rset.getString(14)==null?"N":rset.getString(14));
				VPM.add(rset.getString(15)==null?"N":rset.getString(15));
				VJT.add(rset.getString(16)==null?"N":rset.getString(16));
				VOTHER.add(rset.getString(17)==null?"N":rset.getString(17));
				VSTATUS.add(rset.getString(18)==null?"N":rset.getString(18));
				
				String temp_add_flag="";
				String temp_add_type_nm="";
				int index=0;
				queryString1="SELECT ADDR_FLAG "
						+ "FROM FMS_ENTITY_CONTACT_ADDR_DTL "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=? "
						+ "AND SEQ_NO=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY')";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, entity_role);
				stmt1.setString(4, seq_no);
				stmt1.setString(5, eff_dt);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					index+=1;
					
					String add_flag = rset1.getString(1)==null?"":rset1.getString(1);
					String add_type_nm="";
					if(add_flag.startsWith("P"))
					{
						String no = add_flag.substring(1,add_flag.length());
						add_type_nm = utilBean.getCounterpartyPlantABBR(counterparty_cd, comp_cd, no, entity_role);
					}
					else if(add_flag.equals("R"))
					{
						add_type_nm="Registered";
					}
					else if(add_flag.equals("C"))
					{
						add_type_nm="Correspondence";
					}
					else if(add_flag.equals("B"))
					{
						add_type_nm="Billing";
					}
					
					/*if(temp_add_flag.equals(""))
					{
						temp_add_flag+=""+add_flag;
					}
					else
					{
						temp_add_flag+="@@"+add_flag;
					}
					if(temp_add_type_nm.equals(""))
					{
						temp_add_type_nm+=""+add_type_nm;
					}
					else
					{
						temp_add_type_nm+="<br>"+add_type_nm;
					}*/
					
					VADDR_FLAG.add(add_flag);
					VADDR_TYPE.add(add_type_nm);
					VADD_ADDRESS.add("");
					VPHONE.add("");
					VFAX1.add("");
					VFAX2.add("");
				}
				rset1.close();
				stmt1.close();
				
				VINDEX.add(index);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getEntityContactDetailV2()
	{
		String function_nm="getEntityContactDetailV2()";

		try
		{
			queryString="SELECT SEQ_NO,TO_CHAR(EFF_DT,'DD/MM/YYYY'),CONTACT_PERSON,DESIGNATION,MOBILE,EMAIL,"
					+ "NOM_FLAG,INV_FLAG,FM_FLAG,PM_FLAG,JT_FLAG,OTHER_FLAG,ACTIVE_FLAG,RM_FLAG,TYPE "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO) "
					+ "GROUP BY SEQ_NO,TO_CHAR(EFF_DT,'DD/MM/YYYY'),CONTACT_PERSON,DESIGNATION,MOBILE,EMAIL,"
					+ "NOM_FLAG,INV_FLAG,FM_FLAG,PM_FLAG,JT_FLAG,OTHER_FLAG,ACTIVE_FLAG,RM_FLAG,TYPE "
					+ "ORDER BY TYPE DESC";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, entity_role);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String seq_no=rset.getString(1)==null?"":rset.getString(1);
				String eff_dt=rset.getString(2)==null?"":rset.getString(2);
				
				String type = rset.getString(15)==null?"":rset.getString(15);
				
				if("RLNG".equals(type)) 
				{
					VSEQ_NO.add(rset.getString(1)==null?"":rset.getString(1));
					VEFF_DT.add(rset.getString(2)==null?"":rset.getString(2));
					VPERSON_NM.add(rset.getString(3)==null?"":rset.getString(3));
					VDESIGNATION.add(rset.getString(4)==null?"":rset.getString(4));
					VMOBILE.add(rset.getString(5)==null?"":rset.getString(5));
					VEMAIL.add(rset.getString(6)==null?"":rset.getString(6));
					
					VSTATUS.add(rset.getString(13)==null?"N":rset.getString(13));
					
					VNOM.add(rset.getString(7)==null?"N":rset.getString(7));
					VINV.add(rset.getString(8)==null?"N":rset.getString(8));
					VFM.add(rset.getString(9)==null?"N":rset.getString(9));
					VPM.add(rset.getString(10)==null?"N":rset.getString(10));
					VJT.add(rset.getString(11)==null?"N":rset.getString(11));
					VOTHER.add(rset.getString(12)==null?"N":rset.getString(12));
					VRM.add(rset.getString(14)==null?"N":rset.getString(14));
				}
				else if("DLNG".equals(type))
				{
					VDLNG_NOM.add(rset.getString(7)==null?"N":rset.getString(7));
					VDLNG_INV.add(rset.getString(8)==null?"N":rset.getString(8));
					VDLNG_FM.add(rset.getString(9)==null?"N":rset.getString(9));
					VDLNG_PM.add(rset.getString(10)==null?"N":rset.getString(10));
					VDLNG_JT.add(rset.getString(11)==null?"N":rset.getString(11));
					VDLNG_OTHER.add(rset.getString(12)==null?"N":rset.getString(12));
					VDLNG_RM.add(rset.getString(14)==null?"N":rset.getString(14));
				}
				
				if(entity_role.equals("C") || entity_role.equals("B") || entity_role.equals("T") || entity_role.equals("G")) 
				{
					int avail_type_cnt= 0;
					
					String queryString_temp="SELECT COUNT(*) "
							+ "FROM FMS_ENTITY_CONTACT_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO) AND TYPE=? "
							+ "GROUP BY SEQ_NO,TO_CHAR(EFF_DT,'DD/MM/YYYY'),CONTACT_PERSON,DESIGNATION,MOBILE,EMAIL,"
							+ "NOM_FLAG,INV_FLAG,FM_FLAG,PM_FLAG,JT_FLAG,OTHER_FLAG,ACTIVE_FLAG,RM_FLAG,TYPE "
							+ "ORDER BY TYPE DESC";
					PreparedStatement stmt_temp=conn.prepareStatement(queryString_temp);
					stmt_temp.setString(1, comp_cd);
					stmt_temp.setString(2, counterparty_cd);
					stmt_temp.setString(3, entity_role);
					stmt_temp.setString(4, "DLNG");
					ResultSet rset_temp =stmt_temp.executeQuery();
					while(rset_temp.next())
					{
						avail_type_cnt=rset.getInt(1);
					}
					rset_temp.close();
					stmt_temp.close();
					
					if(avail_type_cnt<2) 
					{
						VDLNG_NOM.add("N");
						VDLNG_INV.add("N");
						VDLNG_FM.add("N");
						VDLNG_PM.add("N");
						VDLNG_JT.add("N");
						VDLNG_OTHER.add("N");
						VDLNG_RM.add("N");
					}
				}
				
				String temp_add_flag="";
				String temp_add_addr_line="";
				String temp_phone="";
				String temp_fax1="";
				String temp_fax2="";
				String temp_flag="";
				
				String temp_to_nom="";
				String temp_to_inv="";
				String temp_to_fm="";
				String temp_to_pm="";
				String temp_to_jt="";
				String temp_to_other="";
				String temp_to_rm="";

				String temp_dlng_to_nom="";
				String temp_dlng_to_inv="";
				String temp_dlng_to_fm="";
				String temp_dlng_to_pm="";
				String temp_dlng_to_jt="";
				String temp_dlng_to_other="";
				String temp_dlng_to_rm="";
				
				int index=0;
				queryString1="SELECT ADDR_FLAG,ADDL_ADDR_LINE,PHONE,FAX_1,FAX_2,ADDR_IS_ACTIVE, "
						+ "TO_NOM,TO_INV,TO_FM,TO_PM,TO_JT,TO_OTHER,TO_RM,TYPE "
						+ "FROM FMS_ENTITY_CONTACT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=? "
						+ "AND SEQ_NO=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY')"
						+ "ORDER BY TYPE DESC";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, entity_role);
				stmt1.setString(4, seq_no);
				stmt1.setString(5, eff_dt);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String add_flag = rset1.getString(1)==null?"":rset1.getString(1);
					String add_addr_line = rset1.getString(2)==null?"":rset1.getString(2);
					String phone = rset1.getString(3)==null?"":rset1.getString(3);
					String fax1 = rset1.getString(4)==null?"":rset1.getString(4);
					String fax2 = rset1.getString(5)==null?"":rset1.getString(5);
					String flag = rset1.getString(6)==null?"N":rset1.getString(6);
					
					String typeStr = rset1.getString(14)==null?"N":rset1.getString(14);
					
					String to_nom="";
					String to_inv="";
					String to_fm="";
					String to_pm="";
					String to_jt="";
					String to_other="";
					String to_rm="";
					String dlng_to_nom="";
					String dlng_to_inv="";
					String dlng_to_fm="";
					String dlng_to_pm="";
					String dlng_to_jt="";
					String dlng_to_other="";
					String dlng_to_rm="";
					
					if("RLNG".equals(typeStr)) 
					{
						to_nom=rset1.getString(7)==null?"N":rset1.getString(7);
						to_inv=rset1.getString(8)==null?"N":rset1.getString(8);
						to_fm=rset1.getString(9)==null?"N":rset1.getString(9);
						to_pm=rset1.getString(10)==null?"N":rset1.getString(10);
						to_jt=rset1.getString(11)==null?"N":rset1.getString(11);
						to_other=rset1.getString(12)==null?"N":rset1.getString(12);
						to_rm=rset1.getString(13)==null?"N":rset1.getString(13);
					}
					else if("DLNG".equals(typeStr))
					{
						dlng_to_nom=rset1.getString(7)==null?"N":rset1.getString(7);
						dlng_to_inv=rset1.getString(8)==null?"N":rset1.getString(8);
						dlng_to_fm=rset1.getString(9)==null?"N":rset1.getString(9);
						dlng_to_pm=rset1.getString(10)==null?"N":rset1.getString(10);
						dlng_to_jt=rset1.getString(11)==null?"N":rset1.getString(11);
						dlng_to_other=rset1.getString(12)==null?"N":rset1.getString(12);
						dlng_to_rm=rset1.getString(13)==null?"N":rset1.getString(13);
					}
					
					String add_type_nm="";
					if(add_flag.startsWith("P"))
					{
						String no = add_flag.substring(1,add_flag.length());
						add_type_nm = utilBean.getCounterpartyPlantABBR(counterparty_cd, comp_cd, no, entity_role);
					}
					if(add_flag.startsWith("B") && add_flag.length()>1)
					{
						String no = add_flag.substring(1,add_flag.length());
						add_type_nm = utilBean.getCounterpartyBuPlantABBR(counterparty_cd, comp_cd, no, entity_role);
					}
					else if(add_flag.equals("R"))
					{
						add_type_nm="Registered";
					}
					else if(add_flag.equals("C"))
					{
						add_type_nm="Correspondence";
					}
					else if(add_flag.equals("B"))
					{
						add_type_nm="Billing";
					}
					
					temp_add_flag=temp_add_flag+"@@"+add_flag;
					temp_add_addr_line=temp_add_addr_line+"@@"+add_addr_line;
					temp_phone=temp_phone+"@@"+phone;
					temp_fax1=temp_fax1+"@@"+fax1;
					temp_fax2=temp_fax2+"@@"+fax2;
					temp_flag=temp_flag+"@@"+flag;
					
					temp_to_nom=temp_to_nom+"@@"+to_nom;
					temp_to_inv=temp_to_inv+"@@"+to_inv;
					temp_to_fm=temp_to_fm+"@@"+to_fm;
					temp_to_pm=temp_to_pm+"@@"+to_pm;
					temp_to_jt=temp_to_jt+"@@"+to_jt;
					temp_to_other=temp_to_other+"@@"+to_other;
					temp_to_rm=temp_to_rm+"@@"+to_rm;

					temp_dlng_to_nom=temp_dlng_to_nom+"@@"+dlng_to_nom;
					temp_dlng_to_inv=temp_dlng_to_inv+"@@"+dlng_to_inv;
					temp_dlng_to_fm=temp_dlng_to_fm+"@@"+dlng_to_fm;
					temp_dlng_to_pm=temp_dlng_to_pm+"@@"+dlng_to_pm;
					temp_dlng_to_jt=temp_dlng_to_jt+"@@"+dlng_to_jt;
					temp_dlng_to_other=temp_dlng_to_other+"@@"+dlng_to_other;
					temp_dlng_to_rm=temp_dlng_to_rm+"@@"+dlng_to_rm;
					
					
					if("RLNG".equals(typeStr)) 
					{
						index+=1;
						
						VADDR_FLAG.add(add_flag);
						VADDR_TYPE.add(add_type_nm);
						VADD_ADDRESS.add(add_addr_line);
						VPHONE.add(phone);
						VFAX1.add(fax1);
						VFAX2.add(fax2);
						VFLAG.add(flag);
					}
				}
				rset1.close();
				stmt1.close();
				
				VINDEX.add(index);
				
				VTEMP_ADDR_FLAG.add(temp_add_flag);
				VTEMP_ADD_ADDRESS.add(temp_add_addr_line);
				VTEMP_PHONE.add(temp_phone);
				VTEMP_FAX1.add(temp_fax1);
				VTEMP_FAX2.add(temp_fax2);
				VTEMP_FLAG.add(temp_flag);
				
				VTEMP_TO_NOM.add(temp_to_nom);
				VTEMP_TO_INV.add(temp_to_inv);
				VTEMP_TO_FM.add(temp_to_fm);
				VTEMP_TO_PM.add(temp_to_pm);
				VTEMP_TO_JT.add(temp_to_jt);
				VTEMP_TO_OTHER.add(temp_to_other);
				VTEMP_TO_RM.add(temp_to_rm);

				VTEMP_DLNG_TO_NOM.add(temp_dlng_to_nom);
				VTEMP_DLNG_TO_INV.add(temp_dlng_to_inv);
				VTEMP_DLNG_TO_FM.add(temp_dlng_to_fm);
				VTEMP_DLNG_TO_PM.add(temp_dlng_to_pm);
				VTEMP_DLNG_TO_JT.add(temp_dlng_to_jt);
				VTEMP_DLNG_TO_OTHER.add(temp_dlng_to_other);
				VTEMP_DLNG_TO_RM.add(temp_dlng_to_rm);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getEntityPlantDetail()
	{
		String function_nm="getEntityPlantDetail()";

		try
		{
			//IF BU NOT SELECTED, DEFAULT FIRST WILL BE SELECTED
			if(bu_unti.equals("0") || bu_unti.equals(""))
			{
				if(VBU_PLANT_SEQ_NO.size()>=1)
				{
					bu_unti=""+VBU_PLANT_SEQ_NO.elementAt(0);
				}
			}
			
			//IF COUNTERPARTY_CD NOT SELECTED, DEFAULT FIRST WILL BE SELECTED
			if(counterparty_cd.equals("0") || counterparty_cd.equals(""))
			{
				if(VCOUNTERPARTY_CD.size()>=1)
				{
					counterparty_cd=""+VCOUNTERPARTY_CD.elementAt(0);
				}
			}
			
			queryString = "SELECT PLANT_STATE "
					+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
					+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=? AND SEQ_NO=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "B");
			stmt.setString(3, comp_cd);
			stmt.setString(4, bu_unti);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				bu_plant_state=rset.getString(1)==null?"":rset.getString(1);
			}
			rset.close();
			stmt.close();
			
			if(entity_role.equals("R") || entity_role.equals("G") || entity_role.equals("H") || entity_role.equals("S") || entity_role.equals("V"))
			{
				queryString0="SELECT COUNTERPARTY_CD,SEQ_NO,PLANT_NAME,PLANT_ABBR,PLANT_STATE "
						+ "FROM FMS_COUNTERPARTY_BU_DTL A "
						+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND EFF_DT=(SELECT MAX(b.EFF_DT) FROM FMS_COUNTERPARTY_BU_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) "
						+ "ORDER BY COUNTERPARTY_CD,SEQ_NO";
				stmt0=conn.prepareStatement(queryString0);
				stmt0.setString(1, entity_role);
				stmt0.setString(2, comp_cd);
				stmt0.setString(3, counterparty_cd);
				rset0=stmt0.executeQuery();
				while(rset0.next())
				{
					String plant_seq = rset0.getString(2)==null?"":rset0.getString(2);
					VBU_SEQ_NO.add(plant_seq);
					VBU_NAME.add(rset0.getString(3)==null?"":rset0.getString(3));
					VBU_ABBR.add(rset0.getString(4)==null?"":rset0.getString(4));
					VBU_STATE.add(rset0.getString(5)==null?"":rset0.getString(5));
					
					//FOR SERVICE TAX
					tax_category ="S";
					int index=0;
					queryString1="SELECT TAX_STRUCT_CD,TO_CHAR(TAX_STRUCT_DT,'DD/MM/YYYY'),TAX_STRUCT_DTL,TAX_STRUCT_REMARK,INVOICE_TYPE,"
							+ "TO_CHAR(ENT_DT,'DD/MM/YYYY'),ENT_BY,TO_CHAR(EFF_DT,'DD/MM/YYYY'),SAP_TAX_CODE "
							+ "FROM FMS_ENTITY_BU_SVC_TAX_DTL A "
							+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND PLANT_SEQ_NO=? AND BU_UNIT=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_BU_SVC_TAX_DTL B "
							+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.INVOICE_TYPE=B.INVOICE_TYPE AND A.BU_UNIT=B.BU_UNIT)";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, entity_role);
					stmt1.setString(2, comp_cd);
					stmt1.setString(3, counterparty_cd);
					stmt1.setString(4, plant_seq);
					stmt1.setString(5, bu_unti);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						index+=1;
						
						String app_dt = rset1.getString(2)==null?"":rset1.getString(2);
						String struct_nm = rset1.getString(3)==null?"":rset1.getString(3);
						struct_nm+=" since "+app_dt;
						
						VBU_SER_TAX_STRUCT_CD.add(rset1.getString(1)==null?"":rset1.getString(1));
						VBU_SER_TAX_STRUCT_APP_DT.add(app_dt);
						VBU_SER_TAX_STRUCT_NM.add(struct_nm);
						VBU_SER_TAX_STRUCT_RMK.add(rset1.getString(4)==null?"":rset1.getString(4));
						String inv_type=rset1.getString(5)==null?"0":rset1.getString(5);
						VBU_SER_INVOICE_TYPE.add(inv_type);
						//VBU_SER_INVOICE_TYPE_NM.add(""+getBUInvoiceName(inv_type));
						
						VBU_SER_INVOICE_TYPE_NM.add(""+utilBean.getInvoiceNameByType(entity_role, tax_category, inv_type));
						VBU_SER_COLOR.add("#99ffcc");
						
						String ent_dt = rset1.getString(6)==null?"":rset1.getString(6);
						String ent_by = rset1.getString(7)==null?"":rset1.getString(7);
						
						VBU_SER_ENTERED_BY.add(""+utilBean.getEmpName(ent_by)+"<br>"+ent_dt);
						
						VBU_SER_EFF_DT.add(rset1.getString(8)==null?"":rset1.getString(8));
						VBU_SER_SAP_TAX_CODE.add(rset1.getString(9)==null?"":rset1.getString(9));
						
						queryString3="SELECT COUNT(*) "
								+ "FROM FMS_ENTITY_BU_SVC_TAX_DTL A "
								+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND PLANT_SEQ_NO=? AND INVOICE_TYPE=? AND BU_UNIT=? ";
						stmt3=conn.prepareStatement(queryString3);
						stmt3.setString(1, entity_role);
						stmt3.setString(2, comp_cd);
						stmt3.setString(3, counterparty_cd);
						stmt3.setString(4, plant_seq);
						stmt3.setString(5, inv_type);
						stmt3.setString(6, bu_unti);
						rset3=stmt3.executeQuery();
						if(rset3.next())
						{
							if(rset3.getInt(1) > 1)
							{
								VTEMP_BU_SER_INDEX.add(rset3.getInt(1));
								queryString2="SELECT TAX_STRUCT_CD,TO_CHAR(TAX_STRUCT_DT,'DD/MM/YYYY'),TAX_STRUCT_DTL,TAX_STRUCT_REMARK,TO_CHAR(ENT_DT,'DD/MM/YYYY'),ENT_BY,"
										+ "TO_CHAR(EFF_DT,'DD/MM/YYYY'),SAP_TAX_CODE "
										+ "FROM FMS_ENTITY_BU_SVC_TAX_DTL A "
										+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND PLANT_SEQ_NO=? AND INVOICE_TYPE=? AND BU_UNIT=? "
										+ "ORDER BY EFF_DT DESC";
								stmt2=conn.prepareStatement(queryString2);
								stmt2.setString(1, entity_role);
								stmt2.setString(2, comp_cd);
								stmt2.setString(3, counterparty_cd);
								stmt2.setString(4, plant_seq);
								stmt2.setString(5, inv_type);
								stmt2.setString(6, bu_unti);
								rset2=stmt2.executeQuery();
								while(rset2.next())
								{
									app_dt = rset2.getString(2)==null?"":rset2.getString(2);
									struct_nm = rset2.getString(3)==null?"":rset2.getString(3);
									struct_nm+=" since "+app_dt;
									
									VTEMP_BU_SER_TAX_STRUCT_CD.add(rset2.getString(1)==null?"":rset2.getString(1));
									VTEMP_BU_SER_TAX_STRUCT_APP_DT.add(app_dt);
									VTEMP_BU_SER_TAX_STRUCT_NM.add(struct_nm);
									VTEMP_BU_SER_TAX_STRUCT_RMK.add(rset2.getString(4)==null?"":rset2.getString(4));
									
									ent_dt = rset2.getString(5)==null?"":rset2.getString(5);
									ent_by = rset2.getString(6)==null?"":rset2.getString(6);
									
									VTEMP_BU_SER_ENTERED_BY.add(""+utilBean.getEmpName(ent_by)+"<br>"+ent_dt);
									VTEMP_BU_SER_EFF_DT.add(rset2.getString(7)==null?"":rset2.getString(7));
									VTEMP_BU_SER_SAP_TAX_CODE.add(rset2.getString(8)==null?"":rset2.getString(8));
								}
								rset2.close();
								stmt2.close();
							}
							else
							{
								VTEMP_BU_SER_INDEX.add("0");
							}
						}
						rset3.close();
						stmt3.close();
					}
					VBU_SER_INDEX.add(index);
					rset1.close();
					stmt1.close();
				}
				rset0.close();
				stmt0.close();
			}
			else
			{
				tax_category ="P";
				queryString0="SELECT COUNTERPARTY_CD,ENTITY,SEQ_NO,TO_CHAR(EFF_DT,'DD/MM/YYYY'),PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,"
						+ "PLANT_ZONE,PLANT_CITY,PLANT_PIN,PLANT_SECTOR "
						+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
						+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND EFF_DT=(SELECT MAX(b.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) "
						+ "ORDER BY COUNTERPARTY_CD,SEQ_NO";
				stmt0=conn.prepareStatement(queryString0);
				stmt0.setString(1, entity_role);
				stmt0.setString(2, comp_cd);
				stmt0.setString(3, counterparty_cd);
				rset0=stmt0.executeQuery();
				while(rset0.next())
				{
					String plant_seq = rset0.getString(3)==null?"":rset0.getString(3);
					VPLANT_SEQ_NO.add(rset0.getString(3)==null?"":rset0.getString(3));
					VPLANT_NAME.add(rset0.getString(5)==null?"":rset0.getString(5));
					VPLANT_ABBR.add(rset0.getString(6)==null?"":rset0.getString(6));
					VPLANT_STATE.add(rset0.getString(8)==null?"":rset0.getString(8));
					
					//FOR PLANT WISE TAX 
					queryString1="SELECT TAX_STRUCT_CD,TO_CHAR(TAX_STRUCT_DT,'DD/MM/YYYY'),TAX_STRUCT_DTL,TAX_STRUCT_REMARK,"
							+ "TO_CHAR(EFF_DT,'DD/MM/YYYY'),SAP_TAX_CODE "
							+ "FROM FMS_ENTITY_TAX_STRUCT_DTL A "
							+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND PLANT_SEQ_NO=? AND BU_UNIT=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_TAX_STRUCT_DTL B "
							+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.BU_UNIT=B.BU_UNIT)";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, entity_role);
					stmt1.setString(2, comp_cd);
					stmt1.setString(3, counterparty_cd);
					stmt1.setString(4, plant_seq);
					stmt1.setString(5, bu_unti);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						String app_dt = rset1.getString(2)==null?"":rset1.getString(2);
						String struct_nm = rset1.getString(3)==null?"":rset1.getString(3);
						struct_nm+=" since "+app_dt;
						
						VTAX_STRUCT_CD.add(rset1.getString(1)==null?"":rset1.getString(1));
						VTAX_STRUCT_APP_DT.add(app_dt);
						VTAX_STRUCT_NM.add(rset1.getString(3)==null?"":rset1.getString(3));
						VDIS_TAX_STRUCT_NM.add(struct_nm);
						VTAX_STRUCT_RMK.add(rset1.getString(4)==null?"":rset1.getString(4));
						VEFF_DT.add(rset1.getString(5)==null?"":rset1.getString(5));
						VSAP_TAX_CODE.add(rset1.getString(6)==null?"":rset1.getString(6));
						VCOLOR.add("#99ffcc");
					}
					else
					{
						VTAX_STRUCT_CD.add("");
						VTAX_STRUCT_APP_DT.add("");
						VTAX_STRUCT_NM.add("");
						VTAX_STRUCT_RMK.add("");
						VDIS_TAX_STRUCT_NM.add("");
						VEFF_DT.add("");
						VSAP_TAX_CODE.add("");
						VCOLOR.add("");
					}
					rset1.close();
					stmt1.close();
					
					
					queryString4="SELECT COUNT(*) "
							+ "FROM FMS_ENTITY_TAX_STRUCT_DTL A "
							+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND PLANT_SEQ_NO=? AND BU_UNIT=? ";
					stmt4=conn.prepareStatement(queryString4);
					stmt4.setString(1, entity_role);
					stmt4.setString(2, comp_cd);
					stmt4.setString(3, counterparty_cd);
					stmt4.setString(4, plant_seq);
					stmt4.setString(5, bu_unti);
					rset4=stmt4.executeQuery();
					if(rset4.next())
					{
						if(rset4.getInt(1) > 0)
						{
							VINDEX.add(rset4.getInt(1));
							queryString2="SELECT TAX_STRUCT_CD,TO_CHAR(TAX_STRUCT_DT,'DD/MM/YYYY'),TAX_STRUCT_DTL,TAX_STRUCT_REMARK,TO_CHAR(ENT_DT,'DD/MM/YYYY'),ENT_BY,"
									+ "TO_CHAR(EFF_DT,'DD/MM/YYYY'),SAP_TAX_CODE "
									+ "FROM FMS_ENTITY_TAX_STRUCT_DTL A "
									+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND PLANT_SEQ_NO=? AND BU_UNIT=? "
									+ "ORDER BY EFF_DT DESC";
							stmt2=conn.prepareStatement(queryString2);
							stmt2.setString(1, entity_role);
							stmt2.setString(2, comp_cd);
							stmt2.setString(3, counterparty_cd);
							stmt2.setString(4, plant_seq);
							stmt2.setString(5, bu_unti);
							rset2=stmt2.executeQuery();
							while(rset2.next())
							{
								String app_dt = rset2.getString(2)==null?"":rset2.getString(2);
								String struct_nm = rset2.getString(3)==null?"":rset2.getString(3);
								struct_nm+=" since "+app_dt;
								
								VTEMP_TAX_STRUCT_CD.add(rset2.getString(1)==null?"":rset2.getString(1));
								VTEMP_TAX_STRUCT_APP_DT.add(app_dt);
								VTEMP_TAX_STRUCT_NM.add(struct_nm);
								VTEMP_TAX_STRUCT_RMK.add(rset2.getString(4)==null?"":rset2.getString(4));
								
								String ent_dt = rset2.getString(5)==null?"":rset2.getString(5);
								String ent_by = rset2.getString(6)==null?"":rset2.getString(6);
								
								VTEMP_ENTERED_BY.add(""+utilBean.getEmpName(ent_by)+"<br>"+ent_dt);
								
								VTEMP_EFF_DT.add(rset2.getString(7)==null?"":rset2.getString(7));
								VTEMP_SAP_TAX_CODE.add(rset2.getString(8)==null?"":rset2.getString(8));
							}
							rset2.close();
							stmt2.close();
						}
						else
						{
							VINDEX.add("0");
						}
					}
					rset4.close();
					stmt4.close();
					
					//FOR SERVICE TAX
					tax_category ="S";
					int index=0;
					queryString4="SELECT TAX_STRUCT_CD,TO_CHAR(TAX_STRUCT_DT,'DD/MM/YYYY'),TAX_STRUCT_DTL,TAX_STRUCT_REMARK,INVOICE_TYPE,"
							+ "TO_CHAR(ENT_DT,'DD/MM/YYYY'),ENT_BY,TO_CHAR(EFF_DT,'DD/MM/YYYY'),SAP_TAX_CODE "
							+ "FROM FMS_ENTITY_SERVICE_TAX_DTL A "
							+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND PLANT_SEQ_NO=? AND BU_UNIT=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_SERVICE_TAX_DTL B "
							+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.INVOICE_TYPE=B.INVOICE_TYPE AND A.BU_UNIT=B.BU_UNIT)";
					stmt4=conn.prepareStatement(queryString4);
					stmt4.setString(1, entity_role);
					stmt4.setString(2, comp_cd);
					stmt4.setString(3, counterparty_cd);
					stmt4.setString(4, plant_seq);
					stmt4.setString(5, bu_unti);
					rset4=stmt4.executeQuery();
					while(rset4.next())
					{
						index+=1;
						
						String app_dt = rset4.getString(2)==null?"":rset4.getString(2);
						String struct_nm = rset4.getString(3)==null?"":rset4.getString(3);
						struct_nm+=" since "+app_dt;
						
						VSER_TAX_STRUCT_CD.add(rset4.getString(1)==null?"":rset4.getString(1));
						VSER_TAX_STRUCT_APP_DT.add(app_dt);
						VSER_TAX_STRUCT_NM.add(struct_nm);
						VSER_TAX_STRUCT_RMK.add(rset4.getString(4)==null?"":rset4.getString(4));
						String inv_type=rset4.getString(5)==null?"0":rset4.getString(5);
						VSER_INVOICE_TYPE.add(inv_type);
						//VSER_INVOICE_TYPE_NM.add(""+getInvoiceName(inv_type));
						VSER_INVOICE_TYPE_NM.add(""+utilBean.getInvoiceNameByType(entity_role, tax_category, inv_type));
						VSER_COLOR.add("#99ffcc");
						
						String ent_dt = rset4.getString(6)==null?"":rset4.getString(6);
						String ent_by = rset4.getString(7)==null?"":rset4.getString(7);
						
						VSER_ENTERED_BY.add(""+utilBean.getEmpName(ent_by)+"<br>"+ent_dt);
						
						VSER_EFF_DT.add(rset4.getString(8)==null?"":rset4.getString(8));
						VSER_SAP_TAX_CODE.add(rset4.getString(9)==null?"":rset4.getString(9));
						
						queryString3="SELECT COUNT(*) "
								+ "FROM FMS_ENTITY_SERVICE_TAX_DTL A "
								+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND PLANT_SEQ_NO=? AND INVOICE_TYPE=? AND BU_UNIT=? ";
						stmt3=conn.prepareStatement(queryString3);
						stmt3.setString(1, entity_role);
						stmt3.setString(2, comp_cd);
						stmt3.setString(3, counterparty_cd);
						stmt3.setString(4, plant_seq);
						stmt3.setString(5, inv_type);
						stmt3.setString(6, bu_unti);
						rset3=stmt3.executeQuery();
						if(rset3.next())
						{
							if(rset3.getInt(1) > 1)
							{
								VTEMP_SER_INDEX.add(rset3.getInt(1));
								queryString2="SELECT TAX_STRUCT_CD,TO_CHAR(TAX_STRUCT_DT,'DD/MM/YYYY'),TAX_STRUCT_DTL,TAX_STRUCT_REMARK,TO_CHAR(ENT_DT,'DD/MM/YYYY'),ENT_BY,"
										+ "TO_CHAR(EFF_DT,'DD/MM/YYYY'),SAP_TAX_CODE "
										+ "FROM FMS_ENTITY_SERVICE_TAX_DTL A "
										+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND PLANT_SEQ_NO=? AND INVOICE_TYPE=? AND BU_UNIT=? "
										+ "ORDER BY EFF_DT DESC";
								stmt2=conn.prepareStatement(queryString2);
								stmt2.setString(1, entity_role);
								stmt2.setString(2, comp_cd);
								stmt2.setString(3, counterparty_cd);
								stmt2.setString(4, plant_seq);
								stmt2.setString(5, inv_type);
								stmt2.setString(6, bu_unti);
								rset2=stmt2.executeQuery();
								while(rset2.next())
								{
									app_dt = rset2.getString(2)==null?"":rset2.getString(2);
									struct_nm = rset2.getString(3)==null?"":rset2.getString(3);
									struct_nm+=" since "+app_dt;
									
									VTEMP_SER_TAX_STRUCT_CD.add(rset2.getString(1)==null?"":rset2.getString(1));
									VTEMP_SER_TAX_STRUCT_APP_DT.add(app_dt);
									VTEMP_SER_TAX_STRUCT_NM.add(struct_nm);
									VTEMP_SER_TAX_STRUCT_RMK.add(rset2.getString(4)==null?"":rset2.getString(4));
									
									ent_dt = rset2.getString(5)==null?"":rset2.getString(5);
									ent_by = rset2.getString(6)==null?"":rset2.getString(6);
									
									VTEMP_SER_ENTERED_BY.add(""+utilBean.getEmpName(ent_by)+"<br>"+ent_dt);
									VTEMP_SER_EFF_DT.add(rset2.getString(7)==null?"":rset2.getString(7));
									VTEMP_SER_SAP_TAX_CODE.add(rset2.getString(8)==null?"":rset2.getString(8));
								}
								rset2.close();
								stmt2.close();
							}
							else
							{
								VTEMP_SER_INDEX.add("0");
							}
						}
						rset3.close();
						stmt3.close();
					}
					VSER_INDEX.add(index);
					rset4.close();
					stmt4.close();
				}
				rset0.close();
				stmt0.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getBusinessPlantList()
	{
		String function_nm="getBusinessPlantList()";

		try
		{
			utilBean.getEffectiveBusinessPlantList(comp_cd);
			VBU_CD=utilBean.getBU_CD();
			VBU_PLANT_NM=utilBean.getBU_PLANT_NM();
			VBU_PLANT_ABBR=utilBean.getBU_PLANT_ABBR();
			VBU_PLANT_SEQ_NO=utilBean.getBU_PLANT_SEQ_NO();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String getInvoiceName(String inv_type)
	{
		String function_nm="getInvoiceName()";

		String nm="";
		try
		{
			if(inv_type.equals("LP"))
			{
				nm="Late Payment";
			}
			else if(inv_type.equals("RV"))
			{
				nm="Receipt Voucher";
			}
			else if(inv_type.equals("SI"))
			{
				nm="ReGas Invoice";
			}
			else if(inv_type.equals("UG"))
			{
				nm="SUG Invoice";
			}
			else if(inv_type.equals("ST"))
			{
				nm="Storage Invoice";
			}
			else if(inv_type.equals("DI"))
			{
				nm="Deficiency Invoice";
			}
			else if(inv_type.equals("XT"))
			{
				nm="IGX Transaction Charges";
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return nm;
	}
	
	public String getBUInvoiceName(String inv_type)
	{
		String function_nm="getBUInvoiceName()";

		String nm="";
		try
		{
			if(inv_type.equals("TC"))
			{
				nm="Transmission Charges";
			}
			else if(inv_type.equals("IC"))
			{
				nm="Imbalance Charges";
			}
			else if(inv_type.equals("PC"))
			{
				nm="Parking Charges";
			}
			else if(inv_type.equals("TX"))
			{
				nm="Transaction Charges";
			}
			else if(inv_type.equals("SF"))
			{
				nm="Surveyor Fee";
			}
			else if(inv_type.equals("CH"))
			{
				nm="CH Agent Fee";
			}
			else if(inv_type.equals("VA"))
			{
				nm="Vessel Agent Fee";
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return nm;
	}
	
	public void getFinancialYearList()
	{
		String function_nm="getFinancialYearList()";

		try
		{	//HARSH20230406 AS DISCUSSED WITH VIJAY, 
			//1) FOR TRADER ENTITY ROLE, CURRENT FINANCIAL YERA SHOULD BE VISIBALE FOR SELECTION
			//2) FOR OTHER ENTITY ROLE, PREVIOUS FINANCIAL YEAR SHOULD BE VISIBALE BEFORE 15 DAYS OF NEW FINANCIAL YEAR
			int currentYear=utilDate.getCurrentYear();
			
			String sysdate=utilDate.getSysdate();
			String firstDtofFinYr="16/03/"+currentYear;
			
			int count = utilDate.getDays(sysdate, firstDtofFinYr);
			int noOfYr=2;
			if(entity_role.equals("T")) 
			{
				firstDtofFinYr="01/04/"+currentYear;
				count = utilDate.getDays(sysdate, firstDtofFinYr);
				
				if(count>0)
				{
					noOfYr=0;
				}
				else
				{
					noOfYr=1;
				}
			}
			else
			{
				if(count>0)
				{
					noOfYr=1;
				}
			}
			
			for(int i=currentYear-noOfYr;i>=currentYear-5;i--)
			{
				String tmp_dt="01/04/"+i;
				
				VFINANCIAL_YEAR.add(""+utilDate.getFinancialYear(tmp_dt));
			}
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getTurnoverDetail()
	{
		String function_nm="getTurnoverDetail()";

		try
		{
			for(int i=0; i<VCOUNTERPARTY_CD.size(); i++)
			{
				String cd=""+VCOUNTERPARTY_CD.elementAt(i);
				
				queryString="SELECT TURNOVER_FLAG "
						+ "FROM FMS_ENTITY_TURNOVER_DTL "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=? "
						+ "AND FINANCIAL_YEAR=? AND TURNOVER_CD=?";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, cd);
				stmt.setString(3, entity_role);
				stmt.setString(4, financial_year);
				stmt.setString(5, turnover_cd);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					VTURNOVER_FLAG.add(rset.getString(1)==null?"":rset.getString(1));
				}
				else
				{
					VTURNOVER_FLAG.add("");
				}
				rset.close();
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getEntityContactPersonListRlng()
	{
		String function_nm="getEntityContactPersonListRlng()";

		try
		{
			queryString="SELECT SEQ_NO,CONTACT_PERSON,TO_CHAR(EFF_DT,'DD/MM/YYYY'),TO_"+notification_type+",EMAIL "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=?"
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND ADDR_FLAG=? AND "+notification_type+"_FLAG=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO) AND TYPE=? ";
			stmt=conn.prepareStatement(queryString);
			//stmt.setString(1, "TO_"+notification_type);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, entity_role);
			stmt.setString(4, "Y");
			stmt.setString(5, "Y");
			stmt.setString(6, address_type);
			//stmt.setString(8, notification_type+"_FLAG");
			stmt.setString(7, "Y");
			stmt.setString(8, "RLNG");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String seq_no = rset.getString(1)==null?"":rset.getString(1);
				VSEQ_NO.add(seq_no);
				VPERSON_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VEFF_DT.add(rset.getString(3)==null?"":rset.getString(3));
				VTO_LIST_FLAG.add(rset.getString(4)==null?"N":rset.getString(4));
				VEMAIL.add(rset.getString(5)==null?"":rset.getString(5));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getEntityContactPersonListDlng()
	{
		String function_nm="getEntityContactPersonListDlng()";
		
		try
		{
			queryString="SELECT SEQ_NO,CONTACT_PERSON,TO_CHAR(EFF_DT,'DD/MM/YYYY'),TO_"+notification_type+",EMAIL "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=?"
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND ADDR_FLAG=? AND "+notification_type+"_FLAG=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO) AND TYPE=? ";
			stmt=conn.prepareStatement(queryString);
			//stmt.setString(1, "TO_"+notification_type);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, entity_role);
			stmt.setString(4, "Y");
			stmt.setString(5, "Y");
			stmt.setString(6, address_type);
			//stmt.setString(8, notification_type+"_FLAG");
			stmt.setString(7, "Y");
			stmt.setString(8, "DLNG");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String seq_no = rset.getString(1)==null?"":rset.getString(1);
				VDLNG_SEQ_NO.add(seq_no);
				VDLNG_PERSON_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VDLNG_EFF_DT.add(rset.getString(3)==null?"":rset.getString(3));
				VDLNG_TO_LIST_FLAG.add(rset.getString(4)==null?"N":rset.getString(4));
				VDLNG_EMAIL.add(rset.getString(5)==null?"":rset.getString(5));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCounterpartyBuDetail()
	{
		String function_nm="getCounterpartyBuDetail()";

		try
		{
			queryString="SELECT COUNTERPARTY_CD,ENTITY,SEQ_NO,TO_CHAR(EFF_DT,'DD/MM/YYYY'),PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,"
					+ "PLANT_ZONE,PLANT_CITY,PLANT_PIN "
					+ "FROM FMS_COUNTERPARTY_BU_DTL A "
					+ "WHERE ENTITY=? AND COUNTERPARTY_CD=? AND COMPANY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_BU_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, entity_role);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq = rset.getString(3)==null?"0":rset.getString(3);
				VBU_PLANT_SEQ_NO.add(rset.getString(3)==null?"":rset.getString(3));
				VBU_PLANT_EFF_DT.add(rset.getString(4)==null?"":rset.getString(4));
				VBU_PLANT_NAME.add(rset.getString(5)==null?"":rset.getString(5));
				VBU_PLANT_ABBR.add(rset.getString(6)==null?"":rset.getString(6));
				VBU_PLANT_ADDR.add(rset.getString(7)==null?"":rset.getString(7));
				VBU_PLANT_STATE.add(rset.getString(8)==null?"":rset.getString(8));
				VBU_PLANT_ZONE.add(rset.getString(9)==null?"":rset.getString(9));
				VBU_PLANT_ZONE_NM.add(""+getZoneName(rset.getString(9)==null?"":rset.getString(9)));
				VBU_PLANT_CITY.add(rset.getString(10)==null?"":rset.getString(10));
				VBU_PLANT_PIN.add(rset.getString(11)==null?"":rset.getString(11));

				String tax_id="";
				queryString1 = "SELECT A.STAT_NO, B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_BU_TAX A,FMS_GOVT_STAT_TAX B "
						+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND A.COMPANY_CD=? "
						+ "AND PLANT_SEQ_NO=? AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, counterparty_cd);
				stmt1.setString(2, entity_role);
				stmt1.setString(3, comp_cd);
				stmt1.setString(4, plant_seq);
				rset1 = stmt1.executeQuery();
				while(rset1.next())
				{
					String stat_no = rset1.getString(1)==null?"":rset1.getString(1);
					String stat_nm = rset1.getString(2)==null?"":rset1.getString(2);
					
					if(tax_id.equals(""))
					{
						tax_id +=""+stat_nm+" "+stat_no;
					}
					else
					{
						tax_id +="<br>"+stat_nm+" "+stat_no;
					}
				}
				rset1.close();
				stmt1.close();
				
				VBU_TAX_ID.add(tax_id);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCounterpartyAuditReport()
	{
		String function_nm="getCounterpartyAuditReport()";

		try
		{
			int cont=0;
			queryString="SELECT NEW_VALUE,OLD_VALUE,LOG_TIME,TO_CHAR(LOG_DT,'DD/MM/YYYY'),LOG_UID "
					+ "FROM FMS_ALL_LOG "
					+ "WHERE FORM_NAME IN (?,?) "
					+ "AND REMARKS LIKE ? AND NEW_VALUE IS NOT NULL ";
			if(!from_dt.equals("") && !to_dt.equals("")) {
				queryString+="AND LOG_DT >= TO_DATE(?,'DD/MM/YYYY') AND LOG_DT <= TO_DATE(?,'DD/MM/YYYY') ";
			}
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0")){
				queryString += "AND NEW_VALUE LIKE ? ";
			}
			queryString+= " ORDER BY LOG_DT DESC, LOG_TIME DESC";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++cont, "Counterparty Master");
			stmt.setString(++cont, "Counterparty Master (IGX)");
			stmt.setString(++cont, "Successful! - Counterparty%");
			if(!from_dt.equals("") && !to_dt.equals("")) 
			{
				stmt.setString(++cont, from_dt);
				stmt.setString(++cont, to_dt);
			}
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				stmt.setString(++cont, "CP="+counterparty_cd+"%");
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String new_values = rset.getString(1)==null?"":rset.getString(1);
				String old_values = rset.getString(2)==null?"":rset.getString(2);
				String time = rset.getString(3)==null?"":rset.getString(3);
				String update_dt=rset.getString(4)==null?"":rset.getString(4);
				String update_by=rset.getString(5)==null?"":rset.getString(5);
				
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
						String counterpartyDetail="";
						String addressDetail="";
						String statusDetail="";
						
						if(old_values.equals("")) 
						{
							counterpartyDetail="Name : "+name+"<br>"
									+ "Abbreviation : "+abbr+"<br>"
									+ "KYC Clearance : "+kyc+"<br>"
									+ "IGX Clearance : "+igx+"<br>"
									+ "PAN NO : "+pan_no+"<br>"
									+ "PAN ISSUE DATE : "+pan_iss_dt+"<br>"
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
								counterpartyDetail += "PAN NO : "+pan_no+"<br>";
							}
							if(!pan_iss_dt.equals(old_pan_iss_dt)) {
								counterpartyDetail += "PAN ISSUE DATE : "+pan_iss_dt+"<br>";
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
						
						
						VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(cp));
						VCOUNTERPARTY_DETAIL.add(counterpartyDetail);
						VCOUNTERPARTY_REGISTER_ADDRESS_DETAIL.add(addressDetail);
						VSTATUS.add(statusDetail);
						VLAST_UPDATE.add(update_dt+"&nbsp;&nbsp;"+time);
						VLAST_UPDATE_BY.add(update_by);
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
	
	public void getCounterpartyList()
	{
		String function_nm="getCounterpartyList()";

		try
		{
			queryString = "SELECT COUNTERPARTY_CD,COUNTERPARTY_NM,COUNTERPARTY_ABBR,NVL(STATUS,'N'),KYC,IGX "
					+ "FROM FMS_COUNTERPARTY_MST A "
					+ "WHERE COUNTERPARTY_ABBR!=? AND EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD) "
					+ "ORDER BY COUNTERPARTY_NM";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1,comp_abbr);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VCOUNTERPARTY_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VCOUNTERPARTY_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VCOUNTERPARTY_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
				VSTATUS.add(rset.getString(4)==null?"N":rset.getString(4));
				
				String kyc=rset.getString(5)==null?"N":rset.getString(5);
				String igx=rset.getString(6)==null?"N":rset.getString(6);
				
				String clearance="";
				if(kyc.equals("Y") && igx.equals("Y"))
				{
					clearance="KYC, IGX";
				}
				else if(kyc.equals("Y"))
				{
					clearance="KYC";
				}
				else if(igx.equals("Y"))
				{
					clearance="IGX";
				}
					
				VCLEARANCE.add(clearance);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getEntityTcsTdsTaxDetail()
	{
		String function_nm="getEntityTcsTdsTaxDetail()";

		try
		{
			//For Tax/Retail Invoice
			int index=0;
			tax_category="P";
			queryString1="SELECT TAX_STRUCT_CD,TO_CHAR(TAX_STRUCT_DT,'DD/MM/YYYY'),TAX_STRUCT_DTL,TAX_STRUCT_REMARK,INVOICE_TYPE,"
					+ "TO_CHAR(ENT_DT,'DD/MM/YYYY'),ENT_BY,TO_CHAR(EFF_DT,'DD/MM/YYYY'),SAP_TAX_CODE "
					+ "FROM FMS_ENTITY_TCS_TDS_MST A "
					+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND TAX_CATEGORY=? AND TAX_APP=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_TCS_TDS_MST B "
					+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.INVOICE_TYPE=B.INVOICE_TYPE AND A.TAX_CATEGORY=B.TAX_CATEGORY)";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, entity_role);
			stmt1.setString(2, comp_cd);
			stmt1.setString(3, counterparty_cd);
			stmt1.setString(4, tax_category);
			stmt1.setString(5, tax_app);
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				index+=1;
				
				String app_dt = rset1.getString(2)==null?"":rset1.getString(2);
				String struct_nm = rset1.getString(3)==null?"":rset1.getString(3);
				struct_nm+=" since "+app_dt;
				
				VTAX_STRUCT_CD.add(rset1.getString(1)==null?"":rset1.getString(1));
				VTAX_STRUCT_APP_DT.add(app_dt);
				VTAX_STRUCT_NM.add(struct_nm);
				VTAX_STRUCT_RMK.add(rset1.getString(4)==null?"":rset1.getString(4));
				String inv_type=rset1.getString(5)==null?"0":rset1.getString(5);
				
				VINVOICE_TYPE.add(inv_type);
				//VSER_INVOICE_TYPE_NM.add(""+getInvoiceName(inv_type));
				VINVOICE_TYPE_NM.add(""+utilBean.getTcsTdsExposedInvoiceNameByType(entity_role, tax_category, inv_type));
				VCOLOR.add("#99ffcc");
				
				String ent_dt = rset1.getString(6)==null?"":rset1.getString(6);
				String ent_by = rset1.getString(7)==null?"":rset1.getString(7);
				
				VENTERED_BY.add(""+utilBean.getEmpName(ent_by)+"<br>"+ent_dt);
				
				VEFF_DT.add(rset1.getString(8)==null?"":rset1.getString(8));
				VSAP_TAX_CODE.add(rset1.getString(9)==null?"":rset1.getString(9));
				
				queryString3="SELECT COUNT(*) "
						+ "FROM FMS_ENTITY_TCS_TDS_MST A "
						+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND INVOICE_TYPE=? AND TAX_CATEGORY=? AND TAX_APP=? ";
				stmt3=conn.prepareStatement(queryString3);
				stmt3.setString(1, entity_role);
				stmt3.setString(2, comp_cd);
				stmt3.setString(3, counterparty_cd);
				stmt3.setString(4, inv_type);
				stmt3.setString(5, tax_category);
				stmt3.setString(6, tax_app);
				rset3=stmt3.executeQuery();
				if(rset3.next())
				{
					if(rset3.getInt(1) > 1)
					{
						VTEMP_INDEX.add(rset3.getInt(1));
						queryString2="SELECT TAX_STRUCT_CD,TO_CHAR(TAX_STRUCT_DT,'DD/MM/YYYY'),TAX_STRUCT_DTL,TAX_STRUCT_REMARK,TO_CHAR(ENT_DT,'DD/MM/YYYY'),ENT_BY,"
								+ "TO_CHAR(EFF_DT,'DD/MM/YYYY'),SAP_TAX_CODE "
								+ "FROM FMS_ENTITY_TCS_TDS_MST A "
								+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND INVOICE_TYPE=? AND TAX_CATEGORY=? AND TAX_APP=? "
								+ "ORDER BY EFF_DT DESC";
						stmt2=conn.prepareStatement(queryString2);
						stmt2.setString(1, entity_role);
						stmt2.setString(2, comp_cd);
						stmt2.setString(3, counterparty_cd);
						stmt2.setString(4, inv_type);
						stmt2.setString(5, tax_category);
						stmt2.setString(6, tax_app);
						rset2=stmt2.executeQuery();
						while(rset2.next())
						{
							app_dt = rset2.getString(2)==null?"":rset2.getString(2);
							struct_nm = rset2.getString(3)==null?"":rset2.getString(3);
							struct_nm+=" since "+app_dt;
							
							VTEMP_TAX_STRUCT_CD.add(rset2.getString(1)==null?"":rset2.getString(1));
							VTEMP_TAX_STRUCT_APP_DT.add(app_dt);
							VTEMP_TAX_STRUCT_NM.add(struct_nm);
							VTEMP_TAX_STRUCT_RMK.add(rset2.getString(4)==null?"":rset2.getString(4));
							
							ent_dt = rset2.getString(5)==null?"":rset2.getString(5);
							ent_by = rset2.getString(6)==null?"":rset2.getString(6);
							
							VTEMP_ENTERED_BY.add(""+utilBean.getEmpName(ent_by)+"<br>"+ent_dt);
							VTEMP_EFF_DT.add(rset2.getString(7)==null?"":rset2.getString(7));
							VTEMP_SAP_TAX_CODE.add(rset2.getString(8)==null?"":rset2.getString(8));
						}
						rset2.close();
						stmt2.close();
					}
					else
					{
						VTEMP_INDEX.add("0");
					}
				}
				rset3.close();
				stmt3.close();
			}
			rset1.close();
			stmt1.close();
			
			//For Service Invoice
			index=0;
			tax_category="S";
			queryString4="SELECT TAX_STRUCT_CD,TO_CHAR(TAX_STRUCT_DT,'DD/MM/YYYY'),TAX_STRUCT_DTL,TAX_STRUCT_REMARK,INVOICE_TYPE,"
					+ "TO_CHAR(ENT_DT,'DD/MM/YYYY'),ENT_BY,TO_CHAR(EFF_DT,'DD/MM/YYYY'),SAP_TAX_CODE "
					+ "FROM FMS_ENTITY_TCS_TDS_MST A "
					+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND TAX_CATEGORY=? AND TAX_APP=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_TCS_TDS_MST B "
					+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.INVOICE_TYPE=B.INVOICE_TYPE AND A.TAX_CATEGORY=B.TAX_CATEGORY)";
			stmt4=conn.prepareStatement(queryString4);
			stmt4.setString(1, entity_role);
			stmt4.setString(2, comp_cd);
			stmt4.setString(3, counterparty_cd);
			stmt4.setString(4, tax_category);
			stmt4.setString(5, tax_app);
			rset4=stmt4.executeQuery();
			while(rset4.next())
			{
				index+=1;
				
				String app_dt = rset4.getString(2)==null?"":rset4.getString(2);
				String struct_nm = rset4.getString(3)==null?"":rset4.getString(3);
				struct_nm+=" since "+app_dt;
				
				VSER_TAX_STRUCT_CD.add(rset4.getString(1)==null?"":rset4.getString(1));
				VSER_TAX_STRUCT_APP_DT.add(app_dt);
				VSER_TAX_STRUCT_NM.add(struct_nm);
				VSER_TAX_STRUCT_RMK.add(rset4.getString(4)==null?"":rset4.getString(4));
				String inv_type=rset4.getString(5)==null?"0":rset4.getString(5);
				VSER_INVOICE_TYPE.add(inv_type);
				//VSER_INVOICE_TYPE_NM.add(""+getInvoiceName(inv_type));
				VSER_INVOICE_TYPE_NM.add(""+utilBean.getTcsTdsExposedInvoiceNameByType(entity_role, tax_category, inv_type));
				VSER_COLOR.add("#99ffcc");
				
				String ent_dt = rset4.getString(6)==null?"":rset4.getString(6);
				String ent_by = rset4.getString(7)==null?"":rset4.getString(7);
				
				VSER_ENTERED_BY.add(""+utilBean.getEmpName(ent_by)+"<br>"+ent_dt);
				
				VSER_EFF_DT.add(rset4.getString(8)==null?"":rset4.getString(8));
				VSER_SAP_TAX_CODE.add(rset4.getString(9)==null?"":rset4.getString(9));
				
				queryString3="SELECT COUNT(*) "
						+ "FROM FMS_ENTITY_TCS_TDS_MST A "
						+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND INVOICE_TYPE=? AND TAX_CATEGORY=? AND TAX_APP=? ";
				stmt3=conn.prepareStatement(queryString3);
				stmt3.setString(1, entity_role);
				stmt3.setString(2, comp_cd);
				stmt3.setString(3, counterparty_cd);
				stmt3.setString(4, inv_type);
				stmt3.setString(5, tax_category);
				stmt3.setString(6, tax_app);
				rset3=stmt3.executeQuery();
				if(rset3.next())
				{
					if(rset3.getInt(1) > 1)
					{
						VTEMP_SER_INDEX.add(rset3.getInt(1));
						queryString2="SELECT TAX_STRUCT_CD,TO_CHAR(TAX_STRUCT_DT,'DD/MM/YYYY'),TAX_STRUCT_DTL,TAX_STRUCT_REMARK,TO_CHAR(ENT_DT,'DD/MM/YYYY'),ENT_BY,"
								+ "TO_CHAR(EFF_DT,'DD/MM/YYYY'),SAP_TAX_CODE "
								+ "FROM FMS_ENTITY_TCS_TDS_MST A "
								+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND INVOICE_TYPE=? AND TAX_CATEGORY=? AND TAX_APP=? "
								+ "ORDER BY EFF_DT DESC";
						stmt2=conn.prepareStatement(queryString2);
						stmt2.setString(1, entity_role);
						stmt2.setString(2, comp_cd);
						stmt2.setString(3, counterparty_cd);
						stmt2.setString(4, inv_type);
						stmt2.setString(5, tax_category);
						stmt2.setString(6, tax_app);
						rset2=stmt2.executeQuery();
						while(rset2.next())
						{
							app_dt = rset2.getString(2)==null?"":rset2.getString(2);
							struct_nm = rset2.getString(3)==null?"":rset2.getString(3);
							struct_nm+=" since "+app_dt;
							
							VTEMP_SER_TAX_STRUCT_CD.add(rset2.getString(1)==null?"":rset2.getString(1));
							VTEMP_SER_TAX_STRUCT_APP_DT.add(app_dt);
							VTEMP_SER_TAX_STRUCT_NM.add(struct_nm);
							VTEMP_SER_TAX_STRUCT_RMK.add(rset2.getString(4)==null?"":rset2.getString(4));
							
							ent_dt = rset2.getString(5)==null?"":rset2.getString(5);
							ent_by = rset2.getString(6)==null?"":rset2.getString(6);
							
							VTEMP_SER_ENTERED_BY.add(""+utilBean.getEmpName(ent_by)+"<br>"+ent_dt);
							VTEMP_SER_EFF_DT.add(rset2.getString(7)==null?"":rset2.getString(7));
							VTEMP_SER_SAP_TAX_CODE.add(rset2.getString(8)==null?"":rset2.getString(8));
						}
						rset2.close();
						stmt2.close();
					}
					else
					{
						VTEMP_SER_INDEX.add("0");
					}
				}
				rset3.close();
				stmt3.close();
			}
			VSER_INDEX.add(index);
			rset4.close();
			stmt4.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	String comp_abbr = "";
	public void setComp_abbr(String comp_abbr) {this.comp_abbr = comp_abbr;}
	
	String opration="";
	String counterparty_cd="";
	String name="";
	String abbr="";
	String eff_dt="";
	String pan_no="";
	String pan_dt="";
	String notes="";
	String kyc_flg="N";
	String igx_flg="N";
	String business_unit="N";
	String customer="N";
	String trader="N";
	String transporter="N";
	String vessel_agent="N";
	String custom_hagent="N";
	String surveyor="N";
	
	String status="";
	String category="";
	String ncf_category="";
	String web_addr="";
	
	String reg_eff_dt="";
	String address="";
	String city="";
	String pin="";
	String state="";
	String zone="";
	String country="";
	String phone="";
	String mobile="";
	String alt_phone="";
	String fax1="";
	String fax2="";
	String email="";
	String sap_code="";
	
	String entity_role="";
	String requester_approver_note="";
	String bu_unti="";
	String bu_plant_state="";
	String financial_year="";
	String turnover_cd="";
	
	String bank_eff_dt="";
	String bank_name="";
	String bank_account_no="";
	String bank_branch="";
	String ifsc_code="";
	String bank_state="";
	String bank_formula="";
	
	String address_type="";
	String notification_type="";
	String from_dt="";
	String to_dt="";
	String tax_category="";
	String tax_app="";
	
	public void setOpration(String opration) {this.opration = opration;}
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setEntity_role(String entity_role) {this.entity_role = entity_role;}
	public void setBu_unti(String bu_unti) {this.bu_unti = bu_unti;}
	public void setFinancial_year(String financial_year) {this.financial_year = financial_year;}
	public void setTurnover_cd(String turnover_cd) {this.turnover_cd = turnover_cd;}
	public void setAddress_type(String address_type) {this.address_type = address_type;}
	public void setNotification_type(String notification_type) {this.notification_type = notification_type;}
	
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	
	public void setTax_category(String tax_category) {this.tax_category = tax_category;}
	public void setTax_app(String tax_app) {this.tax_app = tax_app;}
	
	
	public String getName() {return name;}
	public String getAbbr() {return abbr;}
	public String getEff_dt() {return eff_dt;}
	public String getPan_no() {return pan_no;}
	public String getPan_dt() {return pan_dt;}
	public String getNotes() {return notes;}
	public String getKyc_flg() {return kyc_flg;}
	public String getIgx_flg() {return igx_flg;}
	public String getBusiness_unit() {return business_unit;}
	public String getCustomer() {return customer;}
	public String getTrader() {return trader;}
	public String getVessel_agent() {return vessel_agent;}
	public String getCustom_hagent() {return custom_hagent;}
	public String getSurveyor() {return surveyor;}
	public String getTransporter() {return transporter;}
	
	public String getStatus() {return status;}
	public String getCategory() {return category;}
	public String getNcf_category() {return ncf_category;}
	public String getWeb_addr() {return web_addr;}
	public String getReg_eff_dt() {return reg_eff_dt;}
	public String getAddress() {return address;}
	public String getCity() {return city;}
	public String getPin() {return pin;}
	public String getState() {return state;}
	public String getZone() {return zone;}
	public String getCountry() {return country;}
	public String getPhone() {return phone;}
	public String getMobile() {return mobile;}
	public String getAlt_phone() {return alt_phone;}
	public String getFax1() {return fax1;}
	public String getFax2() {return fax2;}
	public String getEmail() {return email;}
	public String getSap_code() {return sap_code;}
	
	public String getRequester_approver_note() {return requester_approver_note;}
	public String getBu_plant_state() {return bu_plant_state;}
	
	public String getBank_eff_dt() {return bank_eff_dt;}
	public String getBank_name() {return bank_name;}
	public String getBank_account_no() {return bank_account_no;}
	public String getBank_branch() {return bank_branch;}
	public String getIfsc_code() {return ifsc_code;}
	public String getBank_state() {return bank_state;}
	public String getBank_formula() {return bank_formula;}
	
	public String getBu_unit() {return bu_unti;}
	public String getCounterparty_cd() {return counterparty_cd;}
	
	
	Vector VCOUNTRY_CODE = new Vector();
	Vector VCOUNTRY_NM = new Vector();
	Vector VISO_CODE = new Vector();
	Vector VTIN = new Vector();
	Vector VSTATE_CODE = new Vector();
	Vector VSTATE_NM = new Vector();
	Vector VCATEGORY = new Vector();
	Vector VNCF_CATEGORY = new Vector();
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VCOUNTERPTY_CD = new Vector();
	Vector VCOUNTERPTY_NM = new Vector();
	Vector VENTITY_ROLE = new Vector();
	Vector VREMARK = new Vector();
	Vector VREQ_BY = new Vector();
	Vector VREQ_DT = new Vector();
	Vector VSEQ_NO = new Vector();
	Vector VPLANT_SEQ_NO = new Vector();
	Vector VPLANT_EFF_DT = new Vector();
	Vector VPLANT_NAME = new Vector();
	Vector VPLANT_ABBR = new Vector();
	Vector VPLANT_ADDR = new Vector();
	Vector VPLANT_STATE = new Vector();
	Vector VPLANT_ZONE = new Vector();
	Vector VPLANT_ZONE_NM = new Vector();
	Vector VPLANT_CITY = new Vector();
	Vector VPLANT_PIN = new Vector();
	Vector VPLANT_SECTOR = new Vector();
	Vector VPLANT_SECTOR_NM = new Vector();
	Vector VPLANT_STATUS = new Vector();
	Vector VTAX_ID = new Vector();
	Vector VBU_PLANT_EFF_DT = new Vector();
	Vector VBU_PLANT_NAME = new Vector();
	Vector VBU_PLANT_ADDR = new Vector();
	Vector VBU_PLANT_STATE = new Vector();
	Vector VBU_PLANT_ZONE = new Vector();
	Vector VBU_PLANT_ZONE_NM = new Vector();
	Vector VBU_PLANT_CITY = new Vector();
	Vector VBU_PLANT_PIN = new Vector();
	Vector VBU_PLANT_STATUS = new Vector();
	Vector VBU_TAX_ID = new Vector();
	Vector VOTH_STATE_FLG = new Vector();
	
	Vector VSTAT_CD = new Vector();
	Vector VSTAT_NM = new Vector();
	Vector VSTAT_TYPE = new Vector();
	Vector VSTAT_STATUS = new Vector();
	Vector VSTAT_REMARK = new Vector();
	Vector VSTAT_NO = new Vector();
	Vector VSTAT_EFF_DT = new Vector();
	
	Vector VREQUESTER = new Vector();
	Vector VAPPROVER = new Vector();
	Vector VCOLOR = new Vector();
	Vector VCLEARANCE = new Vector();
	
	Vector VEFF_DT = new Vector();
	Vector VPERSON_NM = new Vector();
	Vector VDESIGNATION = new Vector();
	Vector VPHONE = new Vector();
	Vector VMOBILE = new Vector();
	Vector VFAX1 = new Vector();
	Vector VFAX2 = new Vector();
	Vector VEMAIL = new Vector();
	Vector VADDR_FLAG = new Vector();
	Vector VADDR_TYPE = new Vector();
	Vector VADD_ADDRESS = new Vector();
	Vector VNOM = new Vector();
	Vector VINV = new Vector();
	Vector VJT = new Vector();
	Vector VFM = new Vector();
	Vector VPM = new Vector();
	Vector VOTHER = new Vector();
	Vector VRM = new Vector();

	Vector VDLNG_NOM = new Vector();
	Vector VDLNG_INV = new Vector();
	Vector VDLNG_JT = new Vector();
	Vector VDLNG_FM = new Vector();
	Vector VDLNG_PM = new Vector();
	Vector VDLNG_OTHER = new Vector();
	Vector VDLNG_RM = new Vector();
	
	Vector VDLNG_SEQ_NO = new Vector();
	Vector VDLNG_PERSON_NM = new Vector();
	Vector VDLNG_EFF_DT = new Vector();
	Vector VDLNG_TO_LIST_FLAG = new Vector();
	Vector VDLNG_EMAIL = new Vector();
	
	Vector VSTATUS = new Vector();
	Vector VADDRESS_TYPE = new Vector();
	Vector VADDRESS_NAME = new Vector();
	Vector VTAX_STRUCT_CD = new Vector();
	Vector VTAX_STRUCT_NM = new Vector();
	Vector VDIS_TAX_STRUCT_NM = new Vector();
	Vector VTAX_STRUCT_APP_DT = new Vector();
	Vector VTAX_STRUCT_RMK = new Vector();
	Vector VSER_TAX_STRUCT_CD = new Vector();
	Vector VSER_TAX_STRUCT_NM = new Vector();
	Vector VSER_TAX_STRUCT_APP_DT = new Vector();
	Vector VSER_TAX_STRUCT_RMK = new Vector();
	Vector VSER_EFF_DT = new Vector();
	Vector VTEMP_TAX_STRUCT_CD = new Vector();
	Vector VTEMP_TAX_STRUCT_NM = new Vector();
	Vector VTEMP_TAX_STRUCT_APP_DT = new Vector();
	Vector VTEMP_TAX_STRUCT_RMK = new Vector();
	Vector VTEMP_EFF_DT = new Vector();
	Vector VTEMP_SER_TAX_STRUCT_CD = new Vector();
	Vector VTEMP_SER_TAX_STRUCT_NM = new Vector();
	Vector VTEMP_SER_TAX_STRUCT_APP_DT = new Vector();
	Vector VTEMP_SER_TAX_STRUCT_RMK = new Vector();
	Vector VTEMP_SER_EFF_DT = new Vector();
	Vector VINDEX = new Vector();
	Vector VTEMP_SER_INDEX = new Vector();
	Vector VTEMP_ENTERED_BY = new Vector();
	Vector VTEMP_SER_ENTERED_BY = new Vector();
	Vector VSER_COLOR = new Vector();
	Vector VSER_INDEX = new Vector();
	Vector VSER_INVOICE_TYPE = new Vector();
	Vector VSER_ENTERED_BY = new Vector();
	Vector VSER_INVOICE_TYPE_NM = new Vector();
	Vector VFLAG = new Vector();
	Vector VTO_LIST_FLAG = new Vector();
	
	Vector VBU_SER_TAX_STRUCT_CD = new Vector();
	Vector VBU_SER_TAX_STRUCT_NM = new Vector();
	Vector VBU_SER_TAX_STRUCT_APP_DT = new Vector();
	Vector VBU_SER_TAX_STRUCT_RMK = new Vector();
	Vector VBU_SER_EFF_DT = new Vector();
	Vector VTEMP_BU_SER_TAX_STRUCT_CD = new Vector();
	Vector VTEMP_BU_SER_TAX_STRUCT_NM = new Vector();
	Vector VTEMP_BU_SER_TAX_STRUCT_APP_DT = new Vector();
	Vector VTEMP_BU_SER_TAX_STRUCT_RMK = new Vector();
	Vector VTEMP_BU_SER_EFF_DT = new Vector();
	Vector VTEMP_BU_SER_INDEX = new Vector();
	Vector VTEMP_BU_SER_ENTERED_BY = new Vector();
	Vector VBU_SER_COLOR = new Vector();
	Vector VBU_SER_INDEX = new Vector();
	Vector VBU_SER_INVOICE_TYPE = new Vector();
	Vector VBU_SER_ENTERED_BY = new Vector();
	Vector VBU_SER_INVOICE_TYPE_NM = new Vector();
	
	Vector VBU_SEQ_NO = new Vector();
	Vector VBU_NAME = new Vector();
	Vector VBU_ABBR = new Vector();
	Vector VBU_STATE = new Vector();
	
	Vector VBU_CD = new Vector();
	Vector VBU_PLANT_NM = new Vector();
	Vector VBU_PLANT_ABBR = new Vector();
	Vector VBU_PLANT_SEQ_NO = new Vector();
	
	Vector VFINANCIAL_YEAR = new Vector();
	Vector VTURNOVER_FLAG = new Vector();
	
	Vector VBANK_FORMULA = new Vector();
	Vector VBANK_EFF_DT = new Vector();
	
	Vector VSECTOR_CD = new Vector();
	Vector VSECTOR_NAME = new Vector();
	
	Vector VTEMP_PHONE = new Vector();
	Vector VTEMP_FAX1 = new Vector();
	Vector VTEMP_FAX2 = new Vector();
	Vector VTEMP_ADDR_FLAG = new Vector();
	Vector VTEMP_ADD_ADDRESS = new Vector();
	Vector VTEMP_FLAG = new Vector();
	Vector VTEMP_TO_NOM = new Vector();
	Vector VTEMP_TO_INV = new Vector();
	Vector VTEMP_TO_JT = new Vector();
	Vector VTEMP_TO_FM = new Vector();
	Vector VTEMP_TO_PM = new Vector();
	Vector VTEMP_TO_OTHER = new Vector();
	Vector VTEMP_TO_RM = new Vector();
	Vector VTEMP_DLNG_TO_NOM = new Vector();
	Vector VTEMP_DLNG_TO_INV = new Vector();
	Vector VTEMP_DLNG_TO_JT = new Vector();
	Vector VTEMP_DLNG_TO_FM = new Vector();
	Vector VTEMP_DLNG_TO_PM = new Vector();
	Vector VTEMP_DLNG_TO_OTHER = new Vector();
	Vector VTEMP_DLNG_TO_RM = new Vector();
	
	Vector VCOUNTERPARTY_DETAIL = new Vector();
	Vector VCOUNTERPARTY_REGISTER_ADDRESS_DETAIL = new Vector();
	Vector VLAST_UPDATE = new Vector();
	Vector VLAST_UPDATE_BY = new Vector();
	
	Vector VSAP_TAX_CODE = new Vector();
	Vector VSER_SAP_TAX_CODE = new Vector();
	Vector VTEMP_SAP_TAX_CODE = new Vector();
	Vector VTEMP_SER_SAP_TAX_CODE = new Vector();
	Vector VBU_SER_SAP_TAX_CODE = new Vector();
	Vector VTEMP_BU_SER_SAP_TAX_CODE = new Vector();
	
	Vector VINVOICE_TYPE = new Vector();
	Vector VINVOICE_TYPE_NM = new Vector();
	Vector VENTERED_BY = new Vector();
	Vector VTEMP_INDEX= new Vector();
	
	public Vector getVCOUNTRY_CODE() {return VCOUNTRY_CODE;}
	public Vector getVCOUNTRY_NM() {return VCOUNTRY_NM;}
	public Vector getVISO_CODE() {return VISO_CODE;}
	public Vector getVTIN() {return VTIN;}
	public Vector getVSTATE_CODE() {return VSTATE_CODE;}
	public Vector getVSTATE_NM() {return VSTATE_NM;}
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCATEGORY() {return VCATEGORY;}
	public Vector getVNCF_CATEGORY() {return VNCF_CATEGORY;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVCOUNTERPTY_CD() {return VCOUNTERPTY_CD;}
	public Vector getVCOUNTERPTY_NM() {return VCOUNTERPTY_NM;}
	public Vector getVENTITY_ROLE() {return VENTITY_ROLE;}
	public Vector getVREMARK() {return VREMARK;}
	public Vector getVREQ_BY() {return VREQ_BY;}
	public Vector getVREQ_DT() {return VREQ_DT;}
	public Vector getVSEQ_NO() {return VSEQ_NO;}

	public Vector getVPLANT_SEQ_NO() {return VPLANT_SEQ_NO;}
	public Vector getVPLANT_EFF_DT() {return VPLANT_EFF_DT;}
	public Vector getVPLANT_NAME() {return VPLANT_NAME;}
	public Vector getVPLANT_ABBR() {return VPLANT_ABBR;}
	public Vector getVPLANT_ADDR() {return VPLANT_ADDR;}
	public Vector getVPLANT_STATE() {return VPLANT_STATE;}
	public Vector getVPLANT_ZONE() {return VPLANT_ZONE;}
	public Vector getVPLANT_ZONE_NM() {return VPLANT_ZONE_NM;}
	public Vector getVPLANT_CITY() {return VPLANT_CITY;}
	public Vector getVPLANT_PIN() {return VPLANT_PIN;}
	public Vector getVPLANT_SECTOR() {return VPLANT_SECTOR;}
	public Vector getVPLANT_SECTOR_NM() {return VPLANT_SECTOR_NM;}
	public Vector getVPLANT_STATUS() {return VPLANT_STATUS;}
	public Vector getVTAX_ID() {return VTAX_ID;}
	public Vector getVBU_PLANT_EFF_DT() {return VBU_PLANT_EFF_DT;}
	public Vector getVBU_PLANT_NAME() {return VBU_PLANT_NAME;}
	public Vector getVBU_PLANT_ADDR() {return VBU_PLANT_ADDR;}
	public Vector getVBU_PLANT_STATE() {return VBU_PLANT_STATE;}
	public Vector getVBU_PLANT_ZONE() {return VBU_PLANT_ZONE;}
	public Vector getVBU_PLANT_ZONE_NM() {return VBU_PLANT_ZONE_NM;}
	public Vector getVBU_PLANT_CITY() {return VBU_PLANT_CITY;}
	public Vector getVBU_PLANT_PIN() {return VBU_PLANT_PIN;}
	public Vector getVBU_PLANT_STATUS() {return VBU_PLANT_STATUS;}
	public Vector getVBU_TAX_ID() {return VBU_TAX_ID;}
	public Vector getVOTH_STATE_FLG() {return VOTH_STATE_FLG;}
	
	public Vector getVSTAT_CD() {return VSTAT_CD;}
	public Vector getVSTAT_NM() {return VSTAT_NM;}
	public Vector getVSTAT_TYPE() {return VSTAT_TYPE;}
	public Vector getVSTAT_STATUS() {return VSTAT_STATUS;}
	public Vector getVSTAT_REMARK() {return VSTAT_REMARK;}
	public Vector getVSTAT_NO() {return VSTAT_NO;}
	public Vector getVSTAT_EFF_DT() {return VSTAT_EFF_DT;}
	
	public Vector getVREQUESTER() {return VREQUESTER;}
	public Vector getVAPPROVER() {return VAPPROVER;}
	public Vector getVCOLOR() {return VCOLOR;}
	public Vector getVCLEARANCE() {return VCLEARANCE;}
	
	public Vector getVEFF_DT() {return VEFF_DT;}
	public Vector getVPERSON_NM() {return VPERSON_NM;}
	public Vector getVDESIGNATION() {return VDESIGNATION;}
	public Vector getVPHONE() {return VPHONE;}
	public Vector getVMOBILE() {return VMOBILE;}
	public Vector getVFAX1() {return VFAX1;}
	public Vector getVFAX2() {return VFAX2;}
	public Vector getVEMAIL() {return VEMAIL;}
	public Vector getVADDR_FLAG() {return VADDR_FLAG;}
	public Vector getVADDR_TYPE() {return VADDR_TYPE;}
	public Vector getVADD_ADDRESS() {return VADD_ADDRESS;}
	public Vector getVNOM() {return VNOM;}
	public Vector getVINV() {return VINV;}
	public Vector getVJT() {return VJT;}
	public Vector getVFM() {return VFM;}
	public Vector getVPM() {return VPM;}
	public Vector getVOTHER() {return VOTHER;}
	public Vector getVRM() {return VRM;}
	public Vector getVDLNG_NOM() {return VDLNG_NOM;}
	public Vector getVDLNG_INV() {return VDLNG_INV;}
	public Vector getVDLNG_JT() {return VDLNG_JT;}
	public Vector getVDLNG_FM() {return VDLNG_FM;}
	public Vector getVDLNG_PM() {return VDLNG_PM;}
	public Vector getVDLNG_OTHER() {return VDLNG_OTHER;}
	public Vector getVDLNG_RM() {return VDLNG_RM;}
	
	public Vector getVDLNG_SEQ_NO() {return VDLNG_SEQ_NO;}
	public Vector getVDLNG_PERSON_NM() {return VDLNG_PERSON_NM;}
	public Vector getVDLNG_EFF_DT() {return VDLNG_EFF_DT;}
	public Vector getVDLNG_TO_LIST_FLAG() {return VDLNG_TO_LIST_FLAG;}
	public Vector getVDLNG_EMAIL() {return VDLNG_EMAIL;}
	
	public Vector getVSTATUS() {return VSTATUS;}
	public Vector getVADDRESS_TYPE() {return VADDRESS_TYPE;}
	public Vector getVADDRESS_NAME() {return VADDRESS_NAME;}
	public Vector getVTAX_STRUCT_CD() {return VTAX_STRUCT_CD;}
	public Vector getVTAX_STRUCT_NM() {return VTAX_STRUCT_NM;}
	public Vector getVDIS_TAX_STRUCT_NM() {return VDIS_TAX_STRUCT_NM;}
	public Vector getVTAX_STRUCT_APP_DT() {return VTAX_STRUCT_APP_DT;}
	public Vector getVTAX_STRUCT_RMK() {return VTAX_STRUCT_RMK;}
	public Vector getVSER_TAX_STRUCT_CD() {return VSER_TAX_STRUCT_CD;}
	public Vector getVSER_TAX_STRUCT_NM() {return VSER_TAX_STRUCT_NM;}
	public Vector getVSER_TAX_STRUCT_APP_DT() {return VSER_TAX_STRUCT_APP_DT;}
	public Vector getVSER_TAX_STRUCT_RMK() {return VSER_TAX_STRUCT_RMK;}
	public Vector getVSER_EFF_DT() {return VSER_EFF_DT;}
	public Vector getVTEMP_TAX_STRUCT_CD() {return VTEMP_TAX_STRUCT_CD;}
	public Vector getVTEMP_TAX_STRUCT_NM() {return VTEMP_TAX_STRUCT_NM;}
	public Vector getVTEMP_TAX_STRUCT_APP_DT() {return VTEMP_TAX_STRUCT_APP_DT;}
	public Vector getVTEMP_TAX_STRUCT_RMK() {return VTEMP_TAX_STRUCT_RMK;}
	public Vector getVTEMP_EFF_DT() {return VTEMP_EFF_DT;}
	public Vector getVTEMP_SER_TAX_STRUCT_CD() {return VTEMP_SER_TAX_STRUCT_CD;}
	public Vector getVTEMP_SER_TAX_STRUCT_NM() {return VTEMP_SER_TAX_STRUCT_NM;}
	public Vector getVTEMP_SER_TAX_STRUCT_APP_DT() {return VTEMP_SER_TAX_STRUCT_APP_DT;}
	public Vector getVTEMP_SER_TAX_STRUCT_RMK() {return VTEMP_SER_TAX_STRUCT_RMK;}
	public Vector getVTEMP_SER_EFF_DT() {return VTEMP_SER_EFF_DT;}
	public Vector getVINDEX() {return VINDEX;}
	public Vector getVTEMP_SER_INDEX() {return VTEMP_SER_INDEX;}
	public Vector getVTEMP_ENTERED_BY() {return VTEMP_ENTERED_BY;}
	public Vector getVTEMP_SER_ENTERED_BY() {return VTEMP_SER_ENTERED_BY;}
	public Vector getVSER_COLOR() {return VSER_COLOR;}
	public Vector getVSER_INDEX() {return VSER_INDEX;}
	public Vector getVSER_INVOICE_TYPE() {return VSER_INVOICE_TYPE;}
	public Vector getVSER_ENTERED_BY() {return VSER_ENTERED_BY;}
	public Vector getVSER_INVOICE_TYPE_NM() {return VSER_INVOICE_TYPE_NM;}
	public Vector getVFLAG() {return VFLAG;}
	public Vector getVTO_LIST_FLAG() {return VTO_LIST_FLAG;}
	
	public Vector getVBU_SER_TAX_STRUCT_CD() {return VBU_SER_TAX_STRUCT_CD;}
	public Vector getVBU_SER_TAX_STRUCT_NM() {return VBU_SER_TAX_STRUCT_NM;}
	public Vector getVBU_SER_TAX_STRUCT_APP_DT() {return VBU_SER_TAX_STRUCT_APP_DT;}
	public Vector getVBU_SER_TAX_STRUCT_RMK() {return VBU_SER_TAX_STRUCT_RMK;}
	public Vector getVBU_SER_EFF_DT() {return VBU_SER_EFF_DT;}
	public Vector getVTEMP_BU_SER_TAX_STRUCT_CD() {return VTEMP_BU_SER_TAX_STRUCT_CD;}
	public Vector getVTEMP_BU_SER_TAX_STRUCT_NM() {return VTEMP_BU_SER_TAX_STRUCT_NM;}
	public Vector getVTEMP_BU_SER_TAX_STRUCT_APP_DT() {return VTEMP_BU_SER_TAX_STRUCT_APP_DT;}
	public Vector getVTEMP_BU_SER_TAX_STRUCT_RMK() {return VTEMP_BU_SER_TAX_STRUCT_RMK;}
	public Vector getVTEMP_BU_SER_EFF_DT() {return VTEMP_BU_SER_EFF_DT;}
	public Vector getVTEMP_BU_SER_INDEX() {return VTEMP_BU_SER_INDEX;}
	public Vector getVTEMP_BU_SER_ENTERED_BY() {return VTEMP_BU_SER_ENTERED_BY;}
	public Vector getVBU_SER_COLOR() {return VBU_SER_COLOR;}
	public Vector getVBU_SER_INDEX() {return VBU_SER_INDEX;}
	public Vector getVBU_SER_INVOICE_TYPE() {return VBU_SER_INVOICE_TYPE;}
	public Vector getVBU_SER_ENTERED_BY() {return VBU_SER_ENTERED_BY;}
	public Vector getVBU_SER_INVOICE_TYPE_NM() {return VBU_SER_INVOICE_TYPE_NM;}
	
	public Vector getVBU_SEQ_NO() {return VBU_SEQ_NO;}
	public Vector getVBU_NAME() {return VBU_NAME;}
	public Vector getVBU_ABBR() {return VBU_ABBR;}
	public Vector getVBU_STATE() {return VBU_STATE;}
	
	public Vector getVBU_CD() {return VBU_CD;}
	public Vector getVBU_PLANT_NM() {return VBU_PLANT_NM;}
	public Vector getVBU_PLANT_ABBR() {return VBU_PLANT_ABBR;}
	public Vector getVBU_PLANT_SEQ_NO() {return VBU_PLANT_SEQ_NO;}
	
	public Vector getVFINANCIAL_YEAR() {return VFINANCIAL_YEAR;}
	public Vector getVTURNOVER_FLAG() {return VTURNOVER_FLAG;}
	
	public Vector getVBANK_FORMULA() {return VBANK_FORMULA;}
	public Vector getVBANK_EFF_DT() {return VBANK_EFF_DT;}
	
	public Vector getVSECTOR_CD() {return VSECTOR_CD;}
	public Vector getVSECTOR_NAME() {return VSECTOR_NAME;}
	
	public Vector getVTEMP_PHONE() {return VTEMP_PHONE;}
	public Vector getVTEMP_FAX1() {return VTEMP_FAX1;}
	public Vector getVTEMP_FAX2() {return VTEMP_FAX2;}
	public Vector getVTEMP_ADDR_FLAG() {return VTEMP_ADDR_FLAG;}
	public Vector getVTEMP_ADD_ADDRESS() {return VTEMP_ADD_ADDRESS;}
	public Vector getVTEMP_FLAG() {return VTEMP_FLAG;}
	public Vector getVTEMP_TO_NOM() {return VTEMP_TO_NOM;}
	public Vector getVTEMP_TO_INV() {return VTEMP_TO_INV;}
	public Vector getVTEMP_TO_JT() {return VTEMP_TO_JT;}
	public Vector getVTEMP_TO_FM() {return VTEMP_TO_FM;}
	public Vector getVTEMP_TO_PM() {return VTEMP_TO_PM;}
	public Vector getVTEMP_TO_OTHER() {return VTEMP_TO_OTHER;}
	public Vector getVTEMP_TO_RM() {return VTEMP_TO_RM;}
	public Vector getVTEMP_DLNG_TO_NOM() {return VTEMP_DLNG_TO_NOM;}
	public Vector getVTEMP_DLNG_TO_INV() {return VTEMP_DLNG_TO_INV;}
	public Vector getVTEMP_DLNG_TO_JT() {return VTEMP_DLNG_TO_JT;}
	public Vector getVTEMP_DLNG_TO_FM() {return VTEMP_DLNG_TO_FM;}
	public Vector getVTEMP_DLNG_TO_PM() {return VTEMP_DLNG_TO_PM;}
	public Vector getVTEMP_DLNG_TO_OTHER() {return VTEMP_DLNG_TO_OTHER;}
	public Vector getVTEMP_DLNG_TO_RM() {return VTEMP_DLNG_TO_RM;}
	
	public Vector getVCOUNTERPARTY_DETAIL() {return VCOUNTERPARTY_DETAIL;}
	public Vector getVCOUNTERPARTY_REGISTER_ADDRESS_DETAIL() {return VCOUNTERPARTY_REGISTER_ADDRESS_DETAIL;}
	public Vector getVLAST_UPDATE() {return VLAST_UPDATE;}
	public Vector getVLAST_UPDATE_BY() {return VLAST_UPDATE_BY;}
	
	public Vector getVSAP_TAX_CODE() {return VSAP_TAX_CODE;}
	public Vector getVSER_SAP_TAX_CODE() {return VSER_SAP_TAX_CODE;}
	public Vector getVTEMP_SAP_TAX_CODE() {return VTEMP_SAP_TAX_CODE;}
	public Vector getVTEMP_SER_SAP_TAX_CODE() {return VTEMP_SER_SAP_TAX_CODE;}
	public Vector getVBU_SER_SAP_TAX_CODE() {return VBU_SER_SAP_TAX_CODE;}
	public Vector getVTEMP_BU_SER_SAP_TAX_CODE() {return VTEMP_BU_SER_SAP_TAX_CODE;}
	
	public Vector getVINVOICE_TYPE() {return VINVOICE_TYPE;}
	public Vector getVINVOICE_TYPE_NM() {return VINVOICE_TYPE_NM;}
	public Vector getVENTERED_BY() {return VENTERED_BY;}
	public Vector getVTEMP_INDEX() {return VTEMP_INDEX;}
	
	String[] REG_EFF_DT= new String[3];
	String[] ADDRESS_FLAG= new String[3];
	String[] ADDRESS= new String[3];
	String[] CITY= new String[3];
	String[] PIN= new String[3];
	String[] STATE= new String[3];
	String[] ZONE= new String[3];
	String[] COUNTRY= new String[3];
	String[] PHONE= new String[3];
	String[] MOBILE= new String[3];
	String[] ALT_PHONE= new String[3];
	String[] FAX1= new String[3];
	String[] FAX2= new String[3];
	String[] EMAIL= new String[3];

	public String[] getREG_EFF_DT() {return REG_EFF_DT;}
	public String[] getADDRESS_FLAG() {return ADDRESS_FLAG;}
	public String[] getADDRESS() {return ADDRESS;}
	public String[] getCITY() {return CITY;}
	public String[] getPIN() {return PIN;}
	public String[] getSTATE() {return STATE;}
	public String[] getZONE() {return ZONE;}
	public String[] getCOUNTRY() {return COUNTRY;}
	public String[] getPHONE() {return PHONE;}
	public String[] getMOBILE() {return MOBILE;}
	public String[] getALT_PHONE() {return ALT_PHONE;}
	public String[] getFAX1() {return FAX1;}
	public String[] getFAX2() {return FAX2;}
	public String[] getEMAIL() {return EMAIL;}	
}
