package com.etrm.fms.cargo;

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

import com.etrm.fms.util.DB_AllocationUtil;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

public class DB_cargo_service_cont_master 
{

	String db_src_file_name="DB_cargo_service_cont_master.java";

	Connection conn; 
	PreparedStatement stmt;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt5;
	ResultSet rset; 
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
	String queryString="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	
	UtilBean utilBean = new UtilBean();
	DateUtil dateUtil = new DateUtil();
	DB_AllocationUtil allocUtil = new DB_AllocationUtil();
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	
	String gx="K";//AS OF NOW HARDCODED 20230913
	
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
	    			if(callFlag.equalsIgnoreCase("CARGO_SVC_CONT_MST"))
	    			{
	    				getCargoServiceEntityList();
	    				getCargoServiceContractDtls();
	    				getCounterpartyBuDetail();
	    				getBusinessPlantList();
	    				getSelectedAgentPlantList();
	    				getSelectedAgentBusinessPlantList();
	    				getSvcCountBillingDetail();
	    			}
	    			if(callFlag.equalsIgnoreCase("CARGO_SVC_CONT_LIST"))
	    			{
	    				getCargoServiceContractList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("SVC_BILLING_DTL"))
	    			{
	    				display_map_id=utilBean.NewDealMappingId(comp_cd, counterparty_cd, "", "", cont_no, "", contract_type, "");
	    				contpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				getSvcCountBillingDetail();
	    				getStateMst();
	    				getSelectedSvcContPlantlist();
	    				getExchangeRateMaster();
	    				getInterestRateMaster();
	    				getSvcBillingDetail();
	    				getSvcApplicableTaxes();
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
	
	public void getSelectedSvcContPlantlist() 
	{
		String function_nm="getSelectedSvcContPlantlist()";
		try
		{
			String queryString="SELECT A.PLANT_SEQ_NO "
					+ "FROM FMS_CARGO_SVC_CONT_SVC_BU A "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? AND A.ENTITY_TYPE=? "
					+ "AND A.CONTRACT_TYPE=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, entity_role);
			stmt.setString(5, contract_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq = rset.getString(1)==null?"":rset.getString(1);
				String plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, entity_role);
				VSELECTED_PLANT_ABBR.add(plant_abbr);
				VSELECTED_PLANT_SEQ.add(plant_seq);
			}
			rset.close();
			stmt.close();
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
			utilBean.getStateMaster(conn);
			VSTATE_CODE = utilBean.getTIN();
			VSTATE_NM = utilBean.getSTATE_NM();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSvcApplicableTaxes()
	{
		String function_nm="getSvcApplicableTaxes()";

		try
		{
			//Added by Pratham Bhatt for optimization 
			queryString2="SELECT A.PLANT_SEQ_NO, B.PLANT_SEQ_NO  "
					+ "FROM FMS_CARGO_SVC_CONT_BU  A, FMS_CARGO_SVC_CONT_SVC_BU B "
					+ "WHERE A.COMPANY_CD=? AND B.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND B.COUNTERPARTY_CD=?  "
					+ "AND A.CONT_NO=? AND B.CONT_NO=?  "
					+ "AND A.ENTITY_TYPE=? AND B.ENTITY_TYPE=? "
					+ "AND A.CONTRACT_TYPE=? AND A.CONTRACT_TYPE=? ";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, comp_cd);
			stmt2.setString(3, counterparty_cd);
			stmt2.setString(4, counterparty_cd);
			stmt2.setString(5, cont_no);
			stmt2.setString(6, cont_no);
			stmt2.setString(7, entity_role);
			stmt2.setString(8, entity_role);
			stmt2.setString(9, contract_type);
			stmt2.setString(10, contract_type);
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				String bu_plant_seq = rset2.getString(1)==null?"":rset2.getString(1);
				String plant_seq = rset2.getString(2)==null?"":rset2.getString(2);
			
				queryString1="SELECT A.TAX_STRUCT_CD,A.TAX_STRUCT_DTL,B.SAP_TAX_CODE, A.INVOICE_TYPE "
						+ "FROM FMS_ENTITY_BU_SVC_TAX_DTL A, FMS_TAX_STRUCTURE B "
						+ "WHERE A.ENTITY=? AND A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
						+ "AND A.PLANT_SEQ_NO=? AND A.BU_UNIT=? "
						+ "AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_BU_SVC_TAX_DTL B "
						+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.BU_UNIT=B.BU_UNIT AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) "
						+ "AND A.TAX_STRUCT_CD=B.TAX_STR_CD "; 
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, entity_role);
				stmt1.setString(2, comp_cd);
				stmt1.setString(3, counterparty_cd);
				stmt1.setString(4, plant_seq);
				stmt1.setString(5, bu_plant_seq);
				stmt1.setString(6, cont_start_dt);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					VTAX_STRUCT_CD.add(rset1.getString(1)==null?"":rset1.getString(1));
					VTAX_STRUCT_NM.add(rset1.getString(2)==null?"":rset1.getString(2));
					VTAX_SAP_CODE.add(rset1.getString(3)==null?"":rset1.getString(3));
					VPLANT_NAME.add(""+utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, entity_role));
					VBU_PLANT_NM.add(""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B"));
					//Pratham Bhatt 20240821: for getting invoice type 
					String invoice_cd = rset1.getString(4)==null?"":rset1.getString(4);
					VINVOICE_TYPE.add(""+utilBean.getInvoiceNameByType(entity_role,"S",invoice_cd));
					VINVOICE_CATEGORY.add("Service Invoice");
				}
				rset1.close();
				stmt1.close();
			}
			rset2.close();
			stmt2.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSvcCountBillingDetail()
	{
		String function_nm="getSvcCountBillingDetail()";
		try
		{
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_CARGO_SVC_BILLING_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY_TYPE=? "
					+ "AND CONT_NO=? AND CONTRACT_TYPE=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, entity_role);
			stmt.setString(4, cont_no);
			stmt.setString(5, contract_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				no_of_billing_dtl+=""+rset.getInt(1);
			}
			
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSvcBillingDetail()
	{
		String function_nm="getSvcBillingDetail()";
		try
		{
			int count=0;
			String queryString3="SELECT COUNT(*) "
					+ "FROM FMS_CARGO_SVC_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY_TYPE=? "
					+ "AND CONT_NO=? AND CONTRACT_TYPE=?";
			stmt3 = conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, counterparty_cd);
			stmt3.setString(3, entity_role);
			stmt3.setString(4, cont_no);
			stmt3.setString(5, contract_type);
			rset3 = stmt3.executeQuery();
			if(rset3.next())
			{
				 count = rset3.getInt(1);
			}
			rset3.close();
			stmt3.close();
			
			if(count > 0)
			{
				for(int k=0; k<VSELECTED_PLANT_SEQ.size(); k++)
				{
					queryString="SELECT BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,"
							+ "INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,DUE_DT_IN,EXCLUDE_SAT,"
							+ "BILLING_DAYS,EXCL_SAT_MAP,PLANT_SEQ_NO "
							+ "FROM FMS_CARGO_SVC_BILLING_DTL "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY_TYPE=? "
							+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=?";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, entity_role);
					stmt.setString(4, cont_no);
					stmt.setString(5, contract_type);
					stmt.setString(6, ""+VSELECTED_PLANT_SEQ.elementAt(k));
					rset=stmt.executeQuery();
					if(rset.next())
					{
						billing_freq=rset.getString(1)==null?"":rset.getString(1);
						billing_flag=rset.getString(2)==null?"":rset.getString(2);
						due_date=rset.getString(3)==null?"2":rset.getString(3);
						sec_due_date=rset.getString(4)==null?"1":rset.getString(4);
						inv_currency=rset.getString(5)==null?"":rset.getString(5);
						payment_currency=rset.getString(6)==null?"":rset.getString(6);
						interest_rate_cd=rset.getString(7)==null?"":rset.getString(7);
						interest_cal_sign=rset.getString(8)==null?"":rset.getString(8);
						interest_cal_per=rset.getString(9)==null?"":rset.getString(9);
						exchng_rate_cd=rset.getString(10)==null?"":rset.getString(10);
						exchng_cal=rset.getString(11)==null?"D":rset.getString(11);
						exchng_criteria=rset.getString(12)==null?"":rset.getString(12);
						exchng_note=rset.getString(13)==null?"":rset.getString(13);
						
						due_dt_in=rset.getString(15)==null?"":rset.getString(15);
						exclude_sat=rset.getString(16)==null?"N":rset.getString(16);
						billing_days=rset.getString(17)==null?"":rset.getString(17);
						
						sat_days=rset.getString(18)==null?"":rset.getString(18);
						plant_seq=rset.getString(19)==null?"":rset.getString(19);
						String plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, entity_role);
						
						String state_map="";
						String state_nm = "";
						String queryString2="SELECT HOLIDAY_STATE "
								+ "FROM FMS_CARGO_SVC_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY_TYPE=? "
								+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=?";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, entity_role);
						stmt2.setString(4, cont_no);
						stmt2.setString(5, contract_type);
						stmt2.setString(6, plant_seq);
						rset2=stmt2.executeQuery();
						while(rset2.next())
						{
							state_map=rset2.getString(1)==null?"":rset2.getString(1);
							if(!state_map.equals(""))
							{
								String[] stateMap = state_map.split("@");
								
								for(int j=0; j<stateMap.length; j++)
								{
									if(!state_nm.equals(""))
									{
										state_nm+=", <font style='background:var(--sys_data_highlight)'>"+utilBean.getStateName(conn,stateMap[j])+"</font>";
									}
									else
									{
										state_nm+="<font style='background:var(--sys_data_highlight)'>"+utilBean.getStateName(conn,stateMap[j])+"</font>";
									}
								}
							}
							else
							{
								String queryString4="SELECT A.TIN "
										+ "FROM FMS_STATE_MST A, FMS_COUNTERPARTY_BU_DTL B "
										+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY=? "
										+ "AND B.SEQ_NO=? AND A.STATE_NM=B.PLANT_STATE "
										+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_BU_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD "
										+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.ENTITY=B.ENTITY "
										+ "AND C.SEQ_NO=B.SEQ_NO)";
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1, comp_cd);
								stmt4.setString(2, counterparty_cd);
								stmt4.setString(3, entity_role);
								stmt4.setString(4, plant_seq);
								rset4=stmt4.executeQuery();
								if(rset4.next())
								{
									String state_cd=rset4.getString(1)==null?"":rset4.getString(1);
									plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, entity_role);
									if(!state_map.equals(""))
									{
										state_map=state_cd;
										state_nm+=", <font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
									}
									else
									{
										state_map=state_cd;
										state_nm+="<font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
									}
								}
								else
								{
									String state_cd="24"; //In Case of Selected Plant Have State Outside The INDIA, Consider Gujarat as Default.
									plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, entity_role);
									if(!state_map.equals(""))
									{
										state_map=state_cd;
										state_nm+=", <font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
									}
									else
									{
										state_map=state_cd;
										state_nm+="<font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
									}
								}
								rset4.close();
								stmt4.close();
							}
						}
						rset2.close();
						stmt2.close();
						
						if(!state_map.equals(""))
						{
							if(!holiday_state.equals(""))
							{
								holiday_state+="@@"+plant_seq+"//"+state_map;
							}
							else
							{
								holiday_state+=plant_seq+"//"+state_map;
							}
							
							if(!disp_holiday_state.equals(""))
							{
								disp_holiday_state+="<br>"+plant_abbr+" - "+state_nm;
							}
							else
							{
								disp_holiday_state+=plant_abbr+" - "+state_nm;
							}
						}
						VPLANT_SEQ.add(plant_seq);
					}
					else
					{
						String state_map="";
						String plant_abbr="";
						String state_nm="";
						String queryString4="SELECT A.TIN "
								+ "FROM FMS_STATE_MST A, FMS_COUNTERPARTY_BU_DTL B "
								+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY=? "
								+ "AND B.SEQ_NO=? AND A.STATE_NM=B.PLANT_STATE "
								+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_BU_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD "
								+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.ENTITY=B.ENTITY "
								+ "AND C.SEQ_NO=B.SEQ_NO)";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, comp_cd);
						stmt4.setString(2, counterparty_cd);
						stmt4.setString(3, entity_role);
						stmt4.setString(4, ""+VSELECTED_PLANT_SEQ.elementAt(k));
						rset4=stmt4.executeQuery();
						if(rset4.next())
						{
							String state_cd=rset4.getString(1)==null?"":rset4.getString(1);
							plant_seq=""+VSELECTED_PLANT_SEQ.elementAt(k);
							
							plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, entity_role);
							if(!state_map.equals(""))
							{
								state_map=state_cd;
								state_nm+=", <font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
							}
							else
							{
								state_map=state_cd;
								state_nm+="<font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
							}
						}
						else
						{
							plant_seq=""+VSELECTED_PLANT_SEQ.elementAt(k);
							String state_cd="24"; //In Case of Selected Plant Have State Outside The INDIA, Consider Gujarat as Default.
							plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, entity_role);
							if(!state_map.equals(""))
							{
								state_map=state_cd;
								state_nm+=", <font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
							}
							else
							{
								state_map=state_cd;
								state_nm+="<font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
							}
						}
						rset4.close();
						stmt4.close();
						
						if(!state_map.equals(""))
						{
							if(!holiday_state.equals(""))
							{
								holiday_state+="@@"+plant_seq+"//"+state_map;
							}
							else
							{
								holiday_state+=plant_seq+"//"+state_map;
							}
							
							if(!disp_holiday_state.equals(""))
							{
								disp_holiday_state+="<br>"+plant_abbr+" - "+state_nm;
							}
							else
							{
								disp_holiday_state+=plant_abbr+" - "+state_nm;
							}
						}
						VPLANT_SEQ.add(plant_seq);
					}
					rset.close();
					stmt.close();
				}
			}
			else
			{
				for(int k=0; k<VSELECTED_PLANT_SEQ.size(); k++)
				{
					String state_map="";
					String plant_abbr="";
					String state_nm="";
					String queryString4="SELECT A.TIN "
							+ "FROM FMS_STATE_MST A, FMS_COUNTERPARTY_BU_DTL B "
							+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY=? "
							+ "AND B.SEQ_NO=? AND A.STATE_NM=B.PLANT_STATE "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_BU_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD "
							+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.ENTITY=B.ENTITY "
							+ "AND C.SEQ_NO=B.SEQ_NO)";
					stmt4 = conn.prepareStatement(queryString4);
					stmt4.setString(1, comp_cd);
					stmt4.setString(2, counterparty_cd);
					stmt4.setString(3, entity_role);
					stmt4.setString(4, ""+VSELECTED_PLANT_SEQ.elementAt(k));
					rset4=stmt4.executeQuery();
					if(rset4.next())
					{
						String state_cd=rset4.getString(1)==null?"":rset4.getString(1);
						plant_seq=""+VSELECTED_PLANT_SEQ.elementAt(k);
						
						plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, entity_role);
						if(!state_map.equals(""))
						{
							state_map=state_cd;
							state_nm+=", <font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
						}
						else
						{
							state_map=state_cd;
							state_nm+="<font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
						}
					}
					else
					{
						plant_seq=""+VSELECTED_PLANT_SEQ.elementAt(k);
						String state_cd="24"; //In Case of Selected Plant Have State Outside The INDIA, Consider Gujarat as Default.
						plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, entity_role);
						if(!state_map.equals(""))
						{
							state_map=state_cd;
							state_nm+=", <font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
						}
						else
						{
							state_map=state_cd;
							state_nm+="<font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
						}
					}
					rset4.close();
					stmt4.close();
					
					if(!state_map.equals(""))
					{
						if(!holiday_state.equals(""))
						{
							holiday_state+="@@"+plant_seq+"//"+state_map;
						}
						else
						{
							holiday_state+=plant_seq+"//"+state_map;
						}
						
						if(!disp_holiday_state.equals(""))
						{
							disp_holiday_state+="<br>"+plant_abbr+" - "+state_nm;
						}
						else
						{
							disp_holiday_state+=plant_abbr+" - "+state_nm;
						}
					}
					VPLANT_SEQ.add(plant_seq);
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getExchangeRateMaster()
	{
		String function_nm="getExchangeRateMaster()";

		try
		{
			
			queryString = "SELECT EXC_RATE_CD,EXC_RATE_NM,BANK_ABBR,FLAG "
					+ "FROM FMS_EXCHG_RATE_MST "
					+ "WHERE FLAG=? "
					+ "ORDER BY EXC_RATE_NM";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VEXCHNG_RATE_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VEXCHNG_RATE_NM.add(rset.getString(2)==null?"":rset.getString(2));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getInterestRateMaster()
	{
		String function_nm="getInterestRateMaster()";

		try
		{
			
			queryString = "SELECT INT_RATE_CD,INT_RATE_NM,BANK_ABBR,FLAG "
					+ "FROM FMS_INT_RATE_MST "
					+ "WHERE FLAG=? "
					+ "ORDER BY INT_RATE_NM";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VINT_RATE_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VINT_RATE_NM.add(rset.getString(2)==null?"":rset.getString(2));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCargoServiceContractDtls()
	{
		String function_nm="getCargoServiceContractDtls()";
		
		try
		{
			int count=0;

			queryString="SELECT CONT_REF_NO,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),"
					+ "PROV_SVC_RATE,PROV_SVC_RATE_UNIT1,PROV_SVC_RATE_UNIT2,"
					+ "FINAL_SVC_RATE,FINAL_SVC_RATE_UNIT1,FINAL_SVC_RATE_UNIT2,TO_CHAR(ENT_DT,'DD/MM/YYYY HH:MM'), CONT_STATUS,"
					+ "TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),SIGNING_TIME "
					+ "FROM FMS_CARGO_SVC_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY_TYPE=? "
					+ "AND CONT_NO=? AND CONTRACT_TYPE=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++count, comp_cd);
			stmt.setString(++count, counterparty_cd);
			stmt.setString(++count, entity_role);
			stmt.setString(++count, cont_no);
			stmt.setString(++count, contract_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				cont_ref_no = rset.getString(1)==null?"":rset.getString(1);
				start_dt = rset.getString(2)==null?"":rset.getString(2);
				end_dt = rset.getString(3)==null?"":rset.getString(3);
				
				cont_disp_no= utilBean.NewDealMappingId(comp_cd, counterparty_cd, "", "", cont_no, "", contract_type, "");
				
				prov_svc_rate = rset.getString(4)==null?"":rset.getString(4);
				prov_svc_rate_unit1 = rset.getString(5)==null?"1":rset.getString(5);
				prov_svc_rate_unit2 = rset.getString(6)==null?"":rset.getString(6);
				final_svc_rate = rset.getString(7)==null?"":rset.getString(7);
				final_svc_rate_unit1 = rset.getString(8)==null?"1":rset.getString(8);
				final_svc_rate_unit2 = rset.getString(9)==null?"":rset.getString(9);
			
				String deal_ent_dt=rset.getString(10)==null?"":rset.getString(10);
				
				String split[] = deal_ent_dt.split(" ");
				ent_dt = split[0];
				ent_time = split[1];
				cont_status= rset.getString(11)==null?"Y":rset.getString(11);
				signing_dt= rset.getString(12)==null?"":rset.getString(12);
				signing_time= rset.getString(13)==null?"":rset.getString(13);
				
				String cont_disp_number=utilBean.NewDealMappingId(comp_cd, counterparty_cd, "", "", cont_no, "", contract_type, "");
				int cargoNum = 0;
				int count1= 0;
				
				queryString1 = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,CONTRACT_TYPE,CONT_NO,CARGO_NO "
						+ "FROM FMS_BUY_CARGO_NOM A "
						+ "WHERE COMPANY_CD=? "
						+ "AND NOM_REV=(SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO "
						+ "AND A.CARGO_NO=B.CARGO_NO) ";
				if(entity_role.equals("S")) 
				{
					queryString1 += "AND LINKED_SURVEYOR_CONT=? ";
				}
				else if(entity_role.equals("H"))
				{
					queryString1 += "AND LINKED_CHAGENT_CONT=? ";
				}
				else if(entity_role.equals("V"))
				{
					queryString1 += "AND LINKED_VAGENT_CONT=? ";
				}
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(++count1, comp_cd);
				stmt1.setString(++count1, cont_disp_number);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					cargoNum++;
					
					String comp_cd = rset1.getString(1)==null?"":rset1.getString(1);
					String countrpty_cd = rset1.getString(2)==null?"":rset1.getString(2);
					String countpty_name = utilBean.getCounterpartyName(conn,countrpty_cd);
					String agmt_no = rset1.getString(3)==null?"":rset1.getString(3);
					String contract_type = rset1.getString(4)==null?"":rset1.getString(4);
					String cont_no = rset1.getString(5)==null?"":rset1.getString(5);
					String cargo_no = rset1.getString(6)==null?"":rset1.getString(6);
					String cargo_ref="";
					int cnt1=0;
					queryString2 = "SELECT CARGO_REF "
							+ "FROM FMS_TRADER_CARGO_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
							+ "AND AGMT_NO=? AND CONT_NO=? AND CARGO_NO=? "
							+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CARGO_MST B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO "
							+ "AND A.CARGO_NO=B.CARGO_NO)";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(++cnt1, comp_cd);
					stmt2.setString(++cnt1, countrpty_cd);
					stmt2.setString(++cnt1, contract_type);
					stmt2.setString(++cnt1, agmt_no);
					stmt2.setString(++cnt1, cont_no);
					stmt2.setString(++cnt1, cargo_no);
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						cargo_ref=rset2.getString(1)==null?"":rset2.getString(1);
					}
					rset2.close();
					stmt2.close();
					
					String cargo_disp_no=utilBean.NewDealMappingId(comp_cd, countrpty_cd, agmt_no, "", cont_no, "", contract_type, cargo_no);
					String cargo_disp_name = countpty_name+" "+cargo_disp_no+" ("+cargo_ref+")";
					
					if(cargoNum>1) 
					{
						cargo_disp__full_no += ", "+cargo_disp_no;
						cargo_disp_full_name += ", "+cargo_disp_name;
					}
					else 
					{
						cargo_disp__full_no += cargo_disp_no;
						cargo_disp_full_name += cargo_disp_name;
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
	
	public void getCargoServiceEntityList()
	{
		String function_nm="getCargoServiceEntityList()";
		
		try
		{
			if(!entity_role.equals("0"))
			{
				//utilBean.getAllEntityCounterpartyList(conn,comp_cd,entity_role);
				utilBean.getEffectiveEntityCounterpartyList(conn,comp_cd, entity_role);
				VCOUNTERPARTY_CD=utilBean.getCOUNTERPARTY_CD();
				VCOUNTERPARTY_NM=utilBean.getCOUNTERPARTY_NM();
				VCOUNTERPARTY_ABBR=utilBean.getCOUNTERPARTY_ABBR();
			}
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
			queryString="SELECT SEQ_NO,PLANT_NAME,PLANT_ABBR "
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
				String plant_seq = rset.getString(1)==null?"0":rset.getString(1);
				VPLANT_SEQ_NO.add(rset.getString(1)==null?"":rset.getString(1));
				VPLANT_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VPLANT_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
			}
			rset.close();
			stmt.close();
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
			utilBean.getEffectiveBusinessPlantList(conn,comp_cd);
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
	
	public void getSelectedAgentPlantList()
	{
		String function_nm="getSelectedAgentPlantList()";
		try
		{
			queryString="SELECT PLANT_SEQ_NO "
					+ "FROM FMS_CARGO_SVC_CONT_SVC_BU "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONTRACT_TYPE=? AND ENTITY_TYPE=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, contract_type);
			stmt.setString(5, entity_role);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq = rset.getString(1)==null?"":rset.getString(1);
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, entity_role);
				VSEL_AGENT_CD.add(counterparty_cd);
				VSEL_PLANT_SEQ_NO.add(plant_seq);
				VSEL_PLANT_ABBR.add(plant_abbr);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSelectedAgentBusinessPlantList()
	{
		String function_nm="getSelectedAgentBusinessPlantList()";
		try
		{
			queryString="SELECT COMPANY_CD,PLANT_SEQ_NO "
					+ "FROM FMS_CARGO_SVC_CONT_BU "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONTRACT_TYPE=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, contract_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String buCd = rset.getString(1)==null?"":rset.getString(1);
				String bu_plant_seq = rset.getString(2)==null?"":rset.getString(2);
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,buCd, comp_cd, bu_plant_seq, "B");
				
				VSEL_BU_CD.add(buCd);
				VSEL_BU_PLANT_SEQ_NO.add(bu_plant_seq);
				VSEL_BU_PLANT_ABBR.add(bu_plant_abbr);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCargoServiceContractList()
	{
		String function_nm="getCargoServiceContractList()";
		
		try
		{
			int count=0;

			queryString="SELECT CONT_REF_NO,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),"
					+ "COUNTERPARTY_CD,ENTITY_TYPE,CONT_NO,CONT_NAME,CONTRACT_TYPE, CONT_STATUS "
					+ "FROM FMS_CARGO_SVC_CONT_MST A "
					+ "WHERE COMPANY_CD=? ";
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0")) 
			{
				queryString+= "AND COUNTERPARTY_CD=? ";
			}
			if(!cargo_startdt.equals("")) 
			{
				queryString+= "AND START_DT <= TO_DATE(?,'DD/MM/YYYY') ";
			}
			if(!cargo_enddt.equals("")) 
			{
				queryString+= "AND END_DT >= TO_DATE(?,'DD/MM/YYYY') ";
			}
			queryString+= "AND ENTITY_TYPE=? AND CONTRACT_TYPE=? ";
			if(!active_status.equals("") && active_status.equals("N"))
			{
				queryString += "AND END_DT < SYSDATE AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
			}
			else 
			{
				queryString += "AND END_DT >= SYSDATE ";
			}
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++count, comp_cd);
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0")) 
			{
				stmt.setString(++count, counterparty_cd);
			}
			if(!cargo_startdt.equals("")) 
			{
				stmt.setString(++count, cargo_startdt);
			}
			if(!cargo_enddt.equals("")) 
			{
				stmt.setString(++count, cargo_enddt);
			}
			stmt.setString(++count, entity_role);
			stmt.setString(++count, contract_type);
			if(!active_status.equals("") && active_status.equals("N"))
			{
				stmt.setString(++count, to_dt);
				stmt.setString(++count, from_dt);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String cont_ref_no = rset.getString(1)==null?"":rset.getString(1);
				String start_dt = rset.getString(2)==null?"":rset.getString(2);
				String end_dt = rset.getString(3)==null?"":rset.getString(3);
				
				String counterparty_cd = rset.getString(4)==null?"":rset.getString(4);
				String counterparty_nm = utilBean.getCounterpartyName(conn,counterparty_cd);
				String entity_role = rset.getString(5)==null?"":rset.getString(5);
				String cont_number = rset.getString(6)==null?"":rset.getString(6);
				String cont_name = rset.getString(7)==null?"":rset.getString(7);
				String cont_type = rset.getString(8)==null?"":rset.getString(8);
				String cont_status = rset.getString(9)==null?"Y":rset.getString(9);
				
				String cont_disp_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, "", "", cont_number, "", cont_type, "");
				String cont_map_no = counterparty_cd+"-"+cont_type+"-"+cont_number; //comp_cd+entity_role+counterparty_cd+"-"+cont_type+""+cont_number;
				
				VCOUNTERPARTY_CD.add(counterparty_cd);
				VCOUNTERPARTY_NM.add(counterparty_nm);
				VCONT_NO.add(cont_number);
				VCONT_FULL_NO.add(cont_disp_no);
				VCONT_MAP_NO.add(cont_map_no);
				VCONT_NAME.add(cont_name);
				VSTART_DT.add(start_dt);
				VEND_DT.add(end_dt);
				VCONT_REF_NO.add(cont_ref_no);
				//VCONT_LINKED_CARGO.add("");
				VCONT_STATUS.add(cont_status);
				VCONT_TYPE.add(cont_type);
				
				int count1=0;
				int cargoNum=0;
				String cargo_disp__full_no ="";
				String cargo_disp_full_name = "";
				
				queryString1 = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,CONTRACT_TYPE,CONT_NO,CARGO_NO "
						+ "FROM FMS_BUY_CARGO_NOM A "
						+ "WHERE COMPANY_CD=? "
						+ "AND NOM_REV=(SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO "
						+ "AND A.CARGO_NO=B.CARGO_NO) ";
				if(entity_role.equals("S")) 
				{
					queryString1 += "AND LINKED_SURVEYOR_CONT=? ";
				}
				else if(entity_role.equals("H"))
				{
					queryString1 += "AND LINKED_CHAGENT_CONT=? ";
				}
				else if(entity_role.equals("V"))
				{
					queryString1 += "AND LINKED_VAGENT_CONT=? ";
				}
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(++count1, comp_cd);
				stmt1.setString(++count1, cont_disp_no);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					cargoNum++;
					
					String comp_cd = rset1.getString(1)==null?"":rset1.getString(1);
					String countrpty_cd = rset1.getString(2)==null?"":rset1.getString(2);
					String countpty_name = utilBean.getCounterpartyName(conn,countrpty_cd);
					String agmt_no = rset1.getString(3)==null?"":rset1.getString(3);
					String contract_type = rset1.getString(4)==null?"":rset1.getString(4);
					String cont_no = rset1.getString(5)==null?"":rset1.getString(5);
					String cargo_no = rset1.getString(6)==null?"":rset1.getString(6);
					String cargo_ref="";
					int cnt1=0;
					queryString2 = "SELECT CARGO_REF "
							+ "FROM FMS_TRADER_CARGO_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
							+ "AND AGMT_NO=? AND CONT_NO=? AND CARGO_NO=? "
							+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CARGO_MST B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO "
							+ "AND A.CARGO_NO=B.CARGO_NO)";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(++cnt1, comp_cd);
					stmt2.setString(++cnt1, countrpty_cd);
					stmt2.setString(++cnt1, contract_type);
					stmt2.setString(++cnt1, agmt_no);
					stmt2.setString(++cnt1, cont_no);
					stmt2.setString(++cnt1, cargo_no);
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						cargo_ref=rset2.getString(1)==null?"":rset2.getString(1);
					}
					rset2.close();
					stmt2.close();
					
					String cargo_disp_no=utilBean.NewDealMappingId(comp_cd, countrpty_cd, agmt_no, "", cont_no, "", contract_type, cargo_no);
					String cargo_disp_name = countpty_name+" "+cargo_disp_no+" ("+cargo_ref+")";
					
					if(cargoNum>1) 
					{
						cargo_disp__full_no += ", "+cargo_disp_no;
						cargo_disp_full_name += ", "+cargo_disp_name;
					}
					else 
					{
						cargo_disp__full_no += cargo_disp_no;
						cargo_disp_full_name += cargo_disp_name;
					}
				}
				
				VCONT_LINKED_CARGO.add(cargo_disp__full_no);
				VCONT_LINKED_CARGO_NAME.add(cargo_disp_full_name);
				
				/*else 
				{
					VCONT_LINKED_CARGO.add("");
				}*/
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
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	String opration = "";
	public void setOpration(String opration) {this.opration = opration;}

	
	String clearance = "";
	String counterparty_cd = "";
	String cont_no = "";
	String cont_disp_no = "";
	String cont_rev_no = "";
	String agmt_no = "";
	String agmt_rev_no = "";
	String agmt_ref_no = "";
	String agmt_name = "";
	String agmt_typ = "";
	String contract_type = "";
	String cont_start_dt = "";
	String from_dt = "";
	String to_dt = "";
	String entity_role="";
	String cargo_enddt="";
	String cargo_startdt="";

	String active_status="";
	
	public void setCargo_enddt(String cargo_enddt) {this.cargo_enddt = cargo_enddt;}
	public void setCargo_startdt(String cargo_startdt) {this.cargo_startdt = cargo_startdt;}
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	public void setClearance(String clearance) {this.clearance = clearance;}
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setCont_no(String cont_no) {this.cont_no = cont_no;}
	public void setCont_rev_no(String cont_rev_no) {this.cont_rev_no = cont_rev_no;}
	public void setAgmt_no(String agmt_no) {this.agmt_no = agmt_no;}
	public void setAgmt_rev_no(String agmt_rev_no) {this.agmt_rev_no = agmt_rev_no;}
	public void setContract_type(String contract_type) {this.contract_type = contract_type;}
	public void setCont_start_dt(String cont_start_dt) {this.cont_start_dt = cont_start_dt;}
	
	public void setEntity_role(String entity_role) {this.entity_role = entity_role;}
	
	public void setActive_status(String active_status) {this.active_status = active_status;}

	Vector VMST_COUNTERPARTY_CD = new Vector();
	Vector VMST_COUNTERPARTY_NM = new Vector();
	Vector VMST_COUNTERPARTY_ABBR = new Vector();
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VPLANT_NM = new Vector();
	Vector VPLANT_ABBR = new Vector();
	Vector VPLANT_SEQ_NO = new Vector();
	Vector VOTH_PLANT_NM = new Vector();
	Vector VOTH_PLANT_ABBR = new Vector();
	Vector VOTH_PLANT_SEQ_NO = new Vector();
	Vector VTRANS_CD = new Vector();
	Vector VTRANS_PLANT_NM = new Vector();
	Vector VTRANS_PLANT_ABBR = new Vector();
	Vector VTRANS_PLANT_SEQ_NO = new Vector();
	Vector VBU_CD = new Vector();
	Vector VBU_PLANT_NM = new Vector();
	Vector VBU_PLANT_ABBR = new Vector();
	Vector VBU_PLANT_SEQ_NO = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VCONT_REV_NO = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VAGMT_FULL_NO = new Vector();
	Vector VAGMT_REV_NO = new Vector();
	Vector VSTART_DT = new Vector();
	Vector VEND_DT = new Vector();
	Vector VCONT_NAME = new Vector();
	Vector VCONT_DISP_NAME = new Vector();
	Vector VCONT_STATUS = new Vector();
	Vector VCONT_TYPE = new Vector();
	Vector VCONT_REF_NO = new Vector();
	Vector VCONT_FULL_NO = new Vector();
	Vector VCONT_MAP_NO = new Vector();
	Vector VCONT_LINKED_CARGO = new Vector();
	Vector VCARGO_DISP_NO = new Vector();
	Vector VCONT_LINKED_CARGO_NAME = new Vector();
	
	Vector VSEL_BU_CD = new Vector();
	Vector VSEL_BU_PLANT_SEQ_NO = new Vector();
	Vector VSEL_BU_PLANT_ABBR = new Vector();
	Vector VSEL_SPLIT_VALUE = new Vector();
	Vector VSEL_PLANT_ABBR = new Vector();
	Vector VSEL_PLANT_SEQ_NO = new Vector();
	Vector VSEL_AGENT_CD = new Vector();
	
	Vector VAGMT_REF_NO = new Vector();
	Vector VAGMT_NAME = new Vector();
	Vector VAGMT_TYP = new Vector();
	Vector VAGMT_STATUS = new Vector();
	
	Vector VCARGO_NAME = new Vector();
	Vector VCARGO_NO = new Vector();
	Vector VDISP_CARGO_NO = new Vector();
	Vector VCARGO_REF = new Vector();
	Vector VCARGO_STATUS = new Vector();
	Vector VCARGO_STATUS_FLG = new Vector();
	Vector VCARGO_QTY = new Vector();
	Vector VCARGO_PRICE = new Vector();
	Vector VCARGO_PRICE_UNIT = new Vector();
	Vector VCARGO_PRICE_UNIT_FLG = new Vector();
	Vector VCARGO_START_DT = new Vector();
	Vector VCARGO_END_DT = new Vector();
	Vector VCARGO_AGMT_BASE = new Vector();

	Vector VIS_CARGO_NOMINATED = new Vector();
	Vector VIS_CARGO_ALLOCATED = new Vector();
	
	Vector VCOUNTRY_NM = new Vector();
	Vector VCOUNTRY_ISO = new Vector();
	Vector VCOUNTRY_CODE = new Vector();
	
	Vector VALLOC_REV_NO = new Vector();
	
	Vector VINT_RATE_CD = new Vector();
	Vector VINT_RATE_NM = new Vector();
	Vector VPLANT_NAME = new Vector();
	Vector VTAX_STRUCT_CD = new Vector();
	Vector VTAX_STRUCT_NM = new Vector();
	Vector VTAX_SAP_CODE = new Vector();
	Vector VEXCHNG_RATE_CD = new Vector();
	Vector VEXCHNG_RATE_NM = new Vector();
	
	Vector VINVOICE_TYPE = new Vector();		//Pratham Bhatt 20240821: for storing Invoice Type
	Vector VINVOICE_CATEGORY = new Vector(); 	//Pratham Bhatt 20240822: for storing Invoice Category
	
	Vector VSTATE_NM=new Vector();
	Vector VSTATE_CODE=new Vector();
	Vector VSELECTED_PLANT_SEQ = new Vector();
	Vector VSELECTED_PLANT_ABBR = new Vector();
	Vector VPLANT_SEQ = new Vector();
	
	public Vector getVINVOICE_TYPE() {return VINVOICE_TYPE;}			//Pratham Bhatt 20240821: getter function of Invoice Type 
	public Vector getVINVOICE_CATEGORY() {return VINVOICE_CATEGORY;}	//Pratham Bhatt 20240822: getter function of Invoice Category
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVMST_COUNTERPARTY_CD() {return VMST_COUNTERPARTY_CD;}
	public Vector getVMST_COUNTERPARTY_NM() {return VMST_COUNTERPARTY_NM;}
	public Vector getVMST_COUNTERPARTY_ABBR() {return VMST_COUNTERPARTY_ABBR;}
	public Vector getVPLANT_NM() {return VPLANT_NM;}
	public Vector getVPLANT_ABBR() {return VPLANT_ABBR;}
	public Vector getVPLANT_SEQ_NO() {return VPLANT_SEQ_NO;}
	public Vector getVOTH_PLANT_NM() {return VOTH_PLANT_NM;}
	public Vector getVOTH_PLANT_ABBR() {return VOTH_PLANT_ABBR;}
	public Vector getVOTH_PLANT_SEQ_NO() {return VOTH_PLANT_SEQ_NO;}
	public Vector getVTRANS_CD() {return VTRANS_CD;}
	public Vector getVTRANS_PLANT_NM() {return VTRANS_PLANT_NM;}
	public Vector getVTRANS_PLANT_ABBR() {return VTRANS_PLANT_ABBR;}
	public Vector getVTRANS_PLANT_SEQ_NO() {return VTRANS_PLANT_SEQ_NO;}
	public Vector getVBU_CD() {return VBU_CD;}
	public Vector getVBU_PLANT_NM() {return VBU_PLANT_NM;}
	public Vector getVBU_PLANT_ABBR() {return VBU_PLANT_ABBR;}
	public Vector getVBU_PLANT_SEQ_NO() {return VBU_PLANT_SEQ_NO;}
	public Vector getVCONT_NO() {return VCONT_NO;}
	public Vector getVCONT_REV_NO() {return VCONT_REV_NO;}
	public Vector getVAGMT_NO() {return VAGMT_NO;}
	public Vector getVAGMT_FULL_NO() {return VAGMT_FULL_NO;}
	public Vector getVAGMT_REV_NO() {return VAGMT_REV_NO;}
	public Vector getVAGMT_TYP() {return VAGMT_TYP;}
	public Vector getVAGMT_STATUS() {return VAGMT_STATUS;}
	public Vector getVAGMT_NAME() {return VAGMT_NAME;}
	public Vector getVAGMT_REF_NO() {return VAGMT_REF_NO;}
	public Vector getVSTART_DT() {return VSTART_DT;}
	public Vector getVEND_DT() {return VEND_DT;}
	public Vector getVCONT_NAME() {return VCONT_NAME;}
	public Vector getVCONT_DISP_NAME() {return VCONT_DISP_NAME;}
	public Vector getVCONT_STATUS() {return VCONT_STATUS;}
	public Vector getVCONT_TYPE() {return VCONT_TYPE;}
	public Vector getVCONT_REF_NO() {return VCONT_REF_NO;}
	public Vector getVCONT_FULL_NO() {return VCONT_FULL_NO;}
	public Vector getVCONT_MAP_NO() {return VCONT_MAP_NO;}
	public Vector getVCONT_LINKED_CARGO() {return VCONT_LINKED_CARGO;}
	public Vector getVCARGO_DISP_NO() {return VCARGO_DISP_NO;}
	public Vector getVCONT_LINKED_CARGO_NAME() {return VCONT_LINKED_CARGO_NAME;}
	
	public Vector getVSEL_BU_CD() {return VSEL_BU_CD;}
	public Vector getVSEL_BU_PLANT_SEQ_NO() {return VSEL_BU_PLANT_SEQ_NO;}
	public Vector getVSEL_BU_PLANT_ABBR() {return VSEL_BU_PLANT_ABBR;}

	public Vector getVSEL_PLANT_ABBR() {return VSEL_PLANT_ABBR;}
	public Vector getVSEL_SPLIT_VALUE() {return VSEL_SPLIT_VALUE;}
	public Vector getVSEL_PLANT_SEQ_NO() {return VSEL_PLANT_SEQ_NO;}
	public Vector getVSEL_AGENT_CD() {return VSEL_AGENT_CD;}
	
	public Vector getVCARGO_NAME() {return VCARGO_NAME;}
	public Vector getVCARGO_NO() {return VCARGO_NO;}
	public Vector getVDISP_CARGO_NO() {return VDISP_CARGO_NO;}
	public Vector getVCARGO_REF() {return VCARGO_REF;}
	public Vector getVCARGO_STATUS() {return VCARGO_STATUS;}
	public Vector getVCARGO_STATUS_FLG() {return VCARGO_STATUS_FLG;}
	public Vector getVCARGO_QTY() {return VCARGO_QTY;}
	public Vector getVCARGO_PRICE() {return VCARGO_PRICE;}
	public Vector getVCARGO_PRICE_UNIT() {return VCARGO_PRICE_UNIT;}
	public Vector getVCARGO_PRICE_UNIT_FLG() {return VCARGO_PRICE_UNIT_FLG;}
	public Vector getVCARGO_START_DT() {return VCARGO_START_DT;}
	public Vector getVCARGO_END_DT() {return VCARGO_END_DT;}
	public Vector getVCARGO_AGMT_BASE() {return VCARGO_AGMT_BASE;}
	
	public Vector getVIS_CARGO_NOMINATED() {return VIS_CARGO_NOMINATED;}
	public Vector getVIS_CARGO_ALLOCATED() {return VIS_CARGO_ALLOCATED;}
	
	public Vector getVCOUNTRY_NM() {return VCOUNTRY_NM;}
	public Vector getVCOUNTRY_ISO() {return VCOUNTRY_ISO;}
	public Vector getVCOUNTRY_CODE() {return VCOUNTRY_CODE;}
	
	public Vector getVALLOC_REV_NO() {return VALLOC_REV_NO;}
	
	public Vector getVINT_RATE_CD() {return VINT_RATE_CD;}
	public Vector getVINT_RATE_NM() {return VINT_RATE_NM;}
	public Vector getVPLANT_NAME() {return VPLANT_NAME;}
	public Vector getVTAX_STRUCT_CD() {return VTAX_STRUCT_CD;}
	public Vector getVTAX_STRUCT_NM() {return VTAX_STRUCT_NM;}
	public Vector getVTAX_SAP_CODE() {return VTAX_SAP_CODE;}
	public Vector getVEXCHNG_RATE_CD() {return VEXCHNG_RATE_CD;}
	public Vector getVEXCHNG_RATE_NM() {return VEXCHNG_RATE_NM;}
	
	public Vector getVSTATE_CODE() {return VSTATE_CODE;}
	public Vector getVSTATE_NM() {return VSTATE_NM;}
	public Vector getVSELECTED_PLANT_SEQ() {return VSELECTED_PLANT_SEQ;}
	public Vector getVSELECTED_PLANT_ABBR() {return VSELECTED_PLANT_ABBR;}
	public Vector getVPLANT_SEQ() {return VPLANT_SEQ;}
	
	
	String min_counterparty_eff_dt = "";
	//String cont_no = "";
	//String cont_rev_no = "";
	String cont_ref_no = "";
	String trade_ref_no = "";
	String signing_dt = "";
	String agmt_signing_dt = "";
	String signing_time = "";
	String dda_dt = "";
	String dda_time = "";
	String ent_dt = "";
	String ent_time = "";
	String start_dt = "";
	String end_dt = "";
	String agmt_start_dt = "";
	String agmt_end_dt = "";
	String agmt_base = "";
	String agmt_type = "";
	String status="";
	String status_nm="";
	String remark = "";
	String rev_dt = "";
	String billing_freq="";
	String billing_flag="";
	
	String prov_svc_rate ="";
	String prov_svc_rate_unit1 ="";
	String prov_svc_rate_unit2 ="";
	String final_svc_rate ="";
	String final_svc_rate_unit1 ="";
	String final_svc_rate_unit2 = "";
	String cont_status = "";
	
	String due_date="";
	String sec_due_date="";
	String inv_currency="";
	String payment_currency="";
	String interest_rate_cd="";
	String interest_cal_sign="";
	String interest_cal_per="";
	String exchng_note="";
	String due_dt_in="";
	String exclude_sat="";
	String exchng_rate_cd="";
	String exchng_cal="";
	String exchng_criteria="";
	String billing_days="";
	String no_of_billing_dtl="";
	
	String cargo_disp__full_no ="";
	String cargo_disp_full_name = "";
	String contpty_abbr = "";
	String display_map_id = "";
	
	String sat_days="";
	String plant_seq="";
	String holiday_state="";
	String disp_holiday_state="";
	
	public String getMin_counterparty_eff_dt() {return min_counterparty_eff_dt;}
	//public String getCont_no() {return cont_no;}
	//public String getCont_rev_no() {return cont_rev_no;}
	public String getCont_ref_no() {return cont_ref_no;}
	public String getTrade_ref_no() {return trade_ref_no;}
	public String getSigning_dt() {return signing_dt;}
	public String getSigning_time() {return signing_time;}
	public String getAgmt_signing_dt() {return agmt_signing_dt;}
	public String getDda_dt() {return dda_dt;}
	public String getDda_time() {return dda_time;}
	public String getEnt_dt() {return ent_dt;}
	public String getEnt_time() {return ent_time;}
	public String getStart_dt() {return start_dt;}
	public String getEnd_dt() {return end_dt;}
	public String getAgmt_start_dt() {return agmt_start_dt;}
	public String getAgmt_end_dt() {return agmt_end_dt;}
	public String getAgmt_base() {return agmt_base;}
	public String getAgmt_type() {return agmt_type;}
	public String getBilling_freq() {return billing_freq;}
	public String getBilling_flag() {return billing_flag;}
	
	public String getProv_svc_rate() {return prov_svc_rate;}
	public String getProv_svc_rate_unit1() {return prov_svc_rate_unit1;}
	public String getProv_svc_rate_unit2() {return prov_svc_rate_unit2;}
	public String getFinal_svc_rate() {return final_svc_rate;}
	public String getFinal_svc_rate_unit1() {return final_svc_rate_unit1;}
	public String getFinal_svc_rate_unit2() {return final_svc_rate_unit2;}
	public String getCont_Status() {return cont_status;}
	
	public String getDue_date() {return due_date;}
	public String getSec_due_date() {return sec_due_date;}
	public String getInv_currency() {return inv_currency;}
	public String getPayment_currency() {return payment_currency;}
	public String getInterest_rate_cd() {return interest_rate_cd;}
	public String getInterest_cal_sign() {return interest_cal_sign;}
	public String getInterest_cal_per() {return interest_cal_per;}
	public String getExchng_note() {return exchng_note;}
	public String getDue_dt_in() {return due_dt_in;}
	public String getExclude_sat() {return exclude_sat;}
	public String getExchng_rate_cd() {return exchng_rate_cd;}
	public String getExchng_cal() {return exchng_cal;}
	public String getExchng_criteria() {return exchng_criteria;}
	public String getBilling_days() {return billing_days;}
	public String getNo_of_billing_dtl() {return no_of_billing_dtl;}
	
	public String getCargo_disp__full_no() {return cargo_disp__full_no;}
	public String getCargo_disp_full_name() {return cargo_disp_full_name;}
	public String getContpty_abbr() {return contpty_abbr;}
	public String getDisplay_map_id() {return display_map_id;}
	
	public String getSat_days() {return sat_days;}
	public String getPlant_seq() {return plant_seq;}
	public String getHoliday_state() {return holiday_state;}
	public String getDisp_holiday_state() {return disp_holiday_state;}
	
	double totalQty=0;
	double UnloadedtotalQty=0;
	double AvailabletotalQty=0;
	
	public Double getTotalQty() {return totalQty;}
	public Double getUnloadedtotalQty() {return UnloadedtotalQty;}
	public Double getAvailabletotalQty() {return AvailabletotalQty;}

	public String getClearance() {return clearance;}
	public String getCounterparty_cd() {return counterparty_cd;}
	public String getCont_rev_no() {return cont_rev_no;}
	public String getAgmt_no() {return agmt_no;}
	public String getAgmt_rev_no() {return agmt_rev_no;}
	public String getContract_type() {return contract_type;}
	public String getCont_start_dt() {return cont_start_dt;}
	public String getStatus() { return status; }
	public String getStatus_nm() { return status_nm; }
	public String getAgmt_ref_no() { return agmt_ref_no; }
	public String getAgmt_name() { return agmt_name; }
	public String getAgmt_typ() { return agmt_typ; }
	public String getRev_dt() { return rev_dt; }
	public void setAgmt_type(String agmt_type) {this.agmt_type = agmt_type;}

	public String getCont_no() {return cont_no;}
	public String getCont_disp_no() {return cont_disp_no;}
}
