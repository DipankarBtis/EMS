package com.etrm.fms.gta;

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

//Coded By          : Jayasri Dhar
//Code Reviewed by	: Harsh Patel 
//CR Date			: 20/05/2023 
//Status	  		: Developing
public class DataBean_MapMaster 
{
	String db_src_file_name="DataBean_MapMaster.java";
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
	DateUtil dateUtil = new DateUtil();
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	
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
	    			if(callFlag.equalsIgnoreCase("MAP_CUSTOMER_CODE"))
	    			{
	    				getTransporterMst();
	    				getCustomerMst();
	    				getMapCustomerCodeList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("MAP_TRANSPORTER_CT"))
	    			{
	    				getFilterTransporterList();
	    				getBusinessPlantList();
	    				getTransporterMst();
	    				//getCustomerMst();
	    				getMapCTAndUTRList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("SALES_CONT_LIST"))
	    			{
	    				getSalesContList();
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
	
	public void getTransporterMst()
	{
		String function_nm="getTransporterMst()";
		try
		{
			//utilBean.getEffectiveTransporterCounterpartyList("KYC",comp_cd);	
			utilBean.getEffectiveEntityCounterpartyList(conn,"KYC",comp_cd,"R");
			//VTRANSPORTER_CD= utilBean.getCOUNTERPARTY_CD();
			//VTRANSPORTER_NM = utilBean.getCOUNTERPARTY_NM();
			//VTRANSPORTER_ABBR = utilBean.getCOUNTERPARTY_ABBR();
			
			for(int i=0;i<utilBean.getCOUNTERPARTY_CD().size(); i++)
			{
				VTRANSPORTER_CD.add(utilBean.getCOUNTERPARTY_CD().elementAt(i));
				VTRANSPORTER_NM.add(utilBean.getCOUNTERPARTY_NM().elementAt(i));
				VTRANSPORTER_ABBR.add(utilBean.getCOUNTERPARTY_ABBR().elementAt(i));
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCustomerMst()
	{
		String function_nm="getCustomerMst()";
		try
		{
			utilBean.getEffectiveEntityCounterpartyList(conn,comp_cd, "C");
			VCOUNTERPARTY_CD=utilBean.getCOUNTERPARTY_CD();
			VCOUNTERPARTY_NM=utilBean.getCOUNTERPARTY_NM();
			VCOUNTERPARTY_ABBR=utilBean.getCOUNTERPARTY_ABBR();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getMapCustomerCodeList()
	{
		String function_nm="getMapCustomerCodeList()";
		try
		{
			queryString="SELECT DISTINCT TRANSPORTER_CD "
					+ "FROM FMS_TRANSPORTER_CUST_CD "
					+ "WHERE COMPANY_CD=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String transptr_cd=rset.getString(1)==null?"":rset.getString(1);
				VTRANSPTR_CD.add(transptr_cd);
				VTRANSPTR_NM.add(""+utilBean.getCounterpartyName(conn,transptr_cd));
			}
			rset.close();
			stmt.close();
			
			for(int i=0; i<VTRANSPTR_CD.size();i++)
			{
				String transCd=""+VTRANSPTR_CD.elementAt(i);
				int index=0;
				
				queryString1="SELECT COUNTERPARTY_CD,PLANT_SEQ,CUSTOMER_CODE,TO_CHAR(EFF_DT,'DD/MM/YYYY'),STATUS "
						+ "FROM FMS_TRANSPORTER_CUST_CD A "
						+ "WHERE COMPANY_CD=? AND TRANSPORTER_CD=? "
						+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_TRANSPORTER_CUST_CD B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.PLANT_SEQ=B.PLANT_SEQ AND A.TRANSPORTER_CD=B.TRANSPORTER_CD)";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, transCd);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					index+=1;
					
					String countpty_cd=rset1.getString(1)==null?"":rset1.getString(1);
					String countpty_nm=""+utilBean.getCounterpartyName(conn,countpty_cd);
					String plant_seq=rset1.getString(2)==null?"":rset1.getString(2);
					String plant_seq_nm=""+utilBean.getCounterpartyPlantName(conn,countpty_cd, comp_cd, plant_seq, "C");
					String eff_dt=rset1.getString(4)==null?"":rset1.getString(4);
					
					VCOUNTERPTY_CD.add(countpty_cd);
					VCOUNTERPTY_NM.add(countpty_nm);
					
					VPLANT_SEQ.add(plant_seq);
					VPLANT_SEQ_NM.add(plant_seq_nm);
					
					VCUSTOMER_CODE.add(rset1.getString(3)==null?"":rset1.getString(3));
					VEFF_DT.add(eff_dt);
					VSTATUS.add(rset1.getString(5)==null?"Y":rset1.getString(5));
					
					queryString3="SELECT COUNT(*) "
							+ "FROM FMS_TRANSPORTER_CUST_CD A "
							+ "WHERE COMPANY_CD=? AND TRANSPORTER_CD=? "
							+ "AND COUNTERPARTY_CD=? AND PLANT_SEQ=?";
					stmt3=conn.prepareStatement(queryString3);
					stmt3.setString(1, comp_cd);
					stmt3.setString(2, transCd);
					stmt3.setString(3, countpty_cd);
					stmt3.setString(4, plant_seq);
					rset3=stmt3.executeQuery();
					if(rset3.next())
					{
						if(rset3.getInt(1)>1)
						{
							VSUB_INDEX.add(rset3.getInt(1));
							queryString2="SELECT CUSTOMER_CODE,TO_CHAR(EFF_DT,'DD/MM/YYYY'),STATUS "
									+ "FROM FMS_TRANSPORTER_CUST_CD A "
									+ "WHERE COMPANY_CD=? AND TRANSPORTER_CD=? "
									+ "AND COUNTERPARTY_CD=? AND PLANT_SEQ=? "
									+ "ORDER BY EFF_DT DESC";
							stmt2=conn.prepareStatement(queryString2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, transCd);
							stmt2.setString(3, countpty_cd);
							stmt2.setString(4, plant_seq);
							rset2=stmt2.executeQuery();
							while(rset2.next())
							{
								VSUB_COUNTERPTY_NM.add(countpty_nm);
								VSUB_PLANT_SEQ_NM.add(plant_seq_nm);
								VSUB_CUSTOMER_CODE.add(rset2.getString(1)==null?"":rset2.getString(1));
								VSUB_EFF_DT.add(rset2.getString(2)==null?"":rset2.getString(2));
								VSUB_STATUS.add(rset2.getString(3)==null?"Y":rset2.getString(3));
							}
							rset2.close();
							stmt2.close();
						}
						else
						{
							VSUB_INDEX.add("0");
						}
					}
					rset3.close();
					stmt3.close();
				}
				rset1.close();
				stmt1.close();
				
				VINDEX.add(index);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	
	public void getMapCTAndUTRList()
	{
		String function_nm="getMapCTAndUTRList()";
		try
		{
			/*queryString="SELECT DISTINCT ENTRY_TRANS_CD "
					+ "FROM FMS_TRANSPORTER_CT_MST "
					+ "WHERE COMPANY_CD=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String transptr_cd=rset.getString(1)==null?"":rset.getString(1);
				VTRANSPTR_CD.add(transptr_cd);
				VTRANSPTR_NM.add(""+utilBean.getCounterpartyName(conn,transptr_cd));
				VTRANSPTR_ABBR.add(""+utilBean.getCounterpartyABBR(conn,transptr_cd));
			}
			rset.close();
			stmt.close();
			
			for(int i=0; i<VTRANSPTR_CD.size();i++)*/
			{
				//String transCd=""+VTRANSPTR_CD.elementAt(i);
				int index=0;
				
				queryString2="SELECT ENTRY_TRANS_PLANT, CONTRACT_TYPE, EXIT_COUNTERPARTY, EXIT_PLANT_SEQ, "
						+ "CT_REF_NO, UTR_NO, TO_CHAR(START_DT,'DD/MM/YYYY'), TO_CHAR(END_DT,'DD/MM/YYYY'),STATUS,SEQ_NO,BU_SEQ,"
						+ "ENTRY_TRANS_CD "
						+ "FROM FMS_TRANSPORTER_CT_MST A "
						//+ "WHERE COMPANY_CD=? AND ENTRY_TRANS_CD=? ";
						+ "WHERE COMPANY_CD=? "
						+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				if(!transptr_cd.equals("0"))
				{
					queryString2+= "AND ENTRY_TRANS_CD=? ";
				}
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, to_dt);
				stmt2.setString(3, from_dt);
				if(!transptr_cd.equals("0"))
				{
					stmt2.setString(4,  transptr_cd);
				}
				
				//stmt2.setString(2, transCd);
				rset2=stmt2.executeQuery();
				while(rset2.next())
				{
					index+=1;
					
					String trans_plant_seq=rset2.getString(1)==null?"":rset2.getString(1);
					String transCd=rset2.getString(12)==null?"":rset2.getString(12);
					String trans_plant_seq_nm=""+utilBean.getCounterpartyPlantName(conn,transCd, comp_cd, trans_plant_seq, "R");
					String exit_type=rset2.getString(2)==null?"":rset2.getString(2);		
					String countpty_cd=rset2.getString(3)==null?"":rset2.getString(3);
					String countpty_nm=""+utilBean.getCounterpartyName(conn,countpty_cd);
					String plant_seq=rset2.getString(4)==null?"":rset2.getString(4);
					String plant_seq_nm=""+utilBean.getCounterpartyPlantName(conn,countpty_cd, comp_cd, plant_seq, exit_type);
					String ct_ref=rset2.getString(5)==null?"":rset2.getString(5);
					String utr=rset2.getString(6)==null?"":rset2.getString(6);
					String start_dt=rset2.getString(7)==null?"":rset2.getString(7);
					String end_dt=rset2.getString(8)==null?"":rset2.getString(8);
					
					String seq_no=rset2.getString(10)==null?"":rset2.getString(10);
					String bu_seq=rset2.getString(11)==null?"":rset2.getString(11);
					
					String gtc_entryPointMap=transCd+"-"+trans_plant_seq;
					String gtc_exitPointMap=exit_type+"-"+countpty_cd+"-"+plant_seq;
					//String gtc_cont_mapping_like=cont_type+"-"+agmt+"-%-"+cont+"-%";
					
					VTRANS_PLANT_SEQ.add(trans_plant_seq);
					VTRANS_PLANT_SEQ_NM.add(trans_plant_seq_nm);
					
					VCOUNTERPTY_CD.add(countpty_cd);
					VCOUNTERPTY_NM.add(countpty_nm);
					VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
					
					VPLANT_SEQ.add(plant_seq);
					VPLANT_SEQ_NM.add(plant_seq_nm);
					
					VEXITPOINT_TYPE.add(exit_type);
					VCT_REFERENCE.add(ct_ref);
					VUTR.add(utr);
					VSTART_DT.add(start_dt);
					VEND_DT.add(end_dt);
					VSTATUS.add(rset2.getString(9)==null?"Y":rset2.getString(9));
					VSEQ_NO.add(seq_no);
					VBU_UNIT.add(bu_seq);
					VBU_UNIT_ABBR.add(utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_seq, "B"));
					
					VTRANSPTR_CD.add(transCd);
					VTRANSPTR_ABBR.add(utilBean.getCounterpartyABBR(conn, transCd));
					
					String attachment="";
					queryString1="SELECT CONT_MAPPING "
							+ "FROM FMS_TRANS_CT_CONT_MAP "
							+ "WHERE COMPANY_CD=? AND ENTRY_TRANS_CD=? "
							+ "AND ENTRY_TRANS_PLANT=? AND CONTRACT_TYPE=? "
							+ "AND EXIT_COUNTERPARTY=? AND EXIT_PLANT_SEQ=? "
							+ "AND SEQ_NO=? AND BU_SEQ=?";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, transCd);
					stmt1.setString(3, trans_plant_seq);
					stmt1.setString(4, exit_type);
					stmt1.setString(5, countpty_cd);
					stmt1.setString(6, plant_seq);
					stmt1.setString(7, seq_no);
					stmt1.setString(8, bu_seq);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						String cont_mapping=rset1.getString(1)==null?"":rset1.getString(1);
						
						String agmt="";
						String agmt_rev="";
						String cont="";
						String cont_rev="";
						String contType="";
						String cargo_no="";
						if(!cont_mapping.equals(""))
						{
							String[] temp = cont_mapping.split("-");
							contType=temp[0];
							agmt=temp[2];
							agmt_rev=temp[3];
							cont=temp[4];
							cont_rev=temp[5];
							cargo_no=temp.length>6?temp[6]:"0";
						}
						
						String deal_no=""+utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, contType, cargo_no);
						if(attachment.equals(""))
						{
							attachment+=""+deal_no;
						}
						else
						{
							attachment+=", "+deal_no;
						}
					}
					rset1.close();
					stmt1.close();
					
					VDEAL_ATTACHMENT.add(attachment);
					
					String gtc_attachment="";
					queryString1="SELECT A.COUNTERPARTY_CD,A.AGMT_NO,A.CONT_NO,A.CONTRACT_TYPE,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY') "
							+ "FROM FMS_GTA_CONT_MST A, FMS_GTA_CONT_BU C "
							+ "WHERE A.COMPANY_CD=? AND A.CONTRACT_TYPE=? AND A.ENTRY_PT_MAPPING_ID=? AND A.EXIT_PT_MAPPING_ID=? "
							//+ "AND A.START_DT=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND A.CT_SEQ_NO=? AND C.PLANT_SEQ_NO=? "
							+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
							+ ""
							+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV "
							+ "AND A.CONT_NO=C.CONT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.CONT_REV=C.CONT_REV ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, exit_type);
					stmt1.setString(3, gtc_entryPointMap);
					stmt1.setString(4, gtc_exitPointMap);
					stmt1.setString(5, seq_no);
					stmt1.setString(6, bu_seq);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						String agmt=rset1.getString(2)==null?"":rset1.getString(2);
						String cont=rset1.getString(3)==null?"":rset1.getString(3);
						String contType=rset1.getString(4)==null?"":rset1.getString(4);
						String startDt=rset1.getString(5)==null?"":rset1.getString(5);
						String endDt=rset1.getString(6)==null?"":rset1.getString(6);
						
						String deal_no=""+utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, "", cont, "", contType, "0")+" ("+startDt+"-"+endDt+")";
						if(gtc_attachment.equals(""))
						{
							gtc_attachment+=""+deal_no;
						}
						else
						{
							gtc_attachment+=", "+deal_no;
						}
					}
					rset1.close();
					stmt1.close();
					
					VGTC_DEAL_ATTACHMENT.add(gtc_attachment);
					
					String min_dt="";
					String max_dt="";
					if(exit_type.equals("C"))
					{
						queryString1="SELECT TO_CHAR(MIN(GAS_DT),'DD/MM/YYYY'),TO_CHAR(MAX(GAS_DT),'DD/MM/YYYY') "
				  				+ "FROM FMS_DAILY_BUYER_NOM_DTL A "
				  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
								+ "AND PLANT_SEQ=? AND BU_SEQ=? AND CT_REF=? "
								+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_BUYER_NOM_DTL B "
								+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
								+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
								+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND A.BU_SEQ=B.BU_SEQ "
								+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
								+ "AND B.GAS_DT=A.GAS_DT AND B.SEQ_NO=A.SEQ_NO AND B.CARGO_NO=A.CARGO_NO) ";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, countpty_cd);
						stmt1.setString(3, transCd);
						stmt1.setString(4, trans_plant_seq);
						stmt1.setString(5, plant_seq);
						stmt1.setString(6, bu_seq);
						stmt1.setString(7, ct_ref);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							min_dt=rset1.getString(1)==null?"":rset1.getString(1);
							max_dt=rset1.getString(2)==null?"":rset1.getString(2);
						}
						rset1.close();
						stmt1.close();	
					}
					VMIN_NOM_DT.add(min_dt);
					VMAX_NOM_DT.add(max_dt);
					
					min_dt="";
					max_dt="";
					queryString1="SELECT TO_CHAR(MIN(START_DT),'DD/MM/YYYY'),TO_CHAR(MAX(END_DT),'DD/MM/YYYY') "
							+ "FROM FMS_GTA_CONT_MST A, FMS_GTA_CONT_BU C "
							+ "WHERE A.COMPANY_CD=? AND A.CONTRACT_TYPE=? AND A.ENTRY_PT_MAPPING_ID=? AND A.EXIT_PT_MAPPING_ID=? "
							+ "AND A.CT_SEQ_NO=? AND C.PLANT_SEQ_NO=? "
							+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
							+ ""
							+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV "
							+ "AND A.CONT_NO=C.CONT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.CONT_REV=C.CONT_REV ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, exit_type);
					stmt1.setString(3, gtc_entryPointMap);
					stmt1.setString(4, gtc_exitPointMap);
					stmt1.setString(5, seq_no);
					stmt1.setString(6, bu_seq);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						min_dt=rset1.getString(1)==null?"":rset1.getString(1);
						max_dt=rset1.getString(2)==null?"":rset1.getString(2);
					}
					rset1.close();
					stmt1.close();
					
					VGTC_MIN_DT.add(min_dt);
					VGTC_MAX_DT.add(max_dt);
				}
				rset2.close();
				stmt2.close();
				
				VINDEX.add(index);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}	
	
	public void getSalesContList()
	{
		String function_nm="getSalesContList()";
		try
		{
			queryString="SELECT A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,"
					+ "A.CONT_REF_NO,A.TRADE_REF_NO,TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),"
					+ "A.AGMT_BASE,0,'CONT' "
					+ "FROM FMS_SUPPLY_CONT_MST A, FMS_SUPPLY_CONT_TRANSPTR C,FMS_SUPPLY_CONT_PLANT D, FMS_SUPPLY_CONT_BU E  "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=?  "
					+ "AND A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
					+ ""
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV "
					+ "AND A.CONT_NO=C.CONT_NO AND A.CONT_REV=C.CONT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE "
					+ "AND C.TRANSPORTER_CD=? AND C.PLANT_SEQ_NO=? "
					+ ""
					+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV "
					+ "AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE "
					+ "AND D.PLANT_SEQ_NO=? "
					+ ""
					+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=E.AGMT_NO AND A.AGMT_REV=E.AGMT_REV "
					+ "AND A.CONT_NO=E.CONT_NO AND A.CONT_REV=E.CONT_REV AND A.CONTRACT_TYPE=E.CONTRACT_TYPE "
					+ "AND E.PLANT_SEQ_NO=? "
					+ " UNION ALL "
					+ "SELECT A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,"
					+ "B.CARGO_REF,NULL,TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),TO_CHAR(TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0),'DD/MM/YYYY'),"
					+ "NULL,B.CARGO_NO,'LTCORA' "
					+ "FROM FMS_LTCORA_CONT_MST A,"
						+ "FMS_LTCORA_CONT_CARGO_DTL B,"
						+ "FMS_LTCORA_CONT_TRANSPTR C,"
						+ "FMS_LTCORA_CONT_PLANT D,"
						+ "FMS_LTCORA_CONT_BU E "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? AND B.CARGO_STATUS=? AND A.AGMT_TYPE=? "
					+ "AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0) >=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND C.TRANSPORTER_CD=? AND C.PLANT_SEQ_NO=? AND D.PLANT_SEQ_NO=? AND E.PLANT_SEQ_NO=? "
					+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
					+ ""
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE "
					+ ""
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO AND A.CONT_REV=C.CONT_REV "
					+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.BUY_SALE=C.BUY_SALE AND A.AGMT_TYPE=C.AGMT_TYPE "
					+ ""
					+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV "
					+ "AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE AND A.BUY_SALE=D.BUY_SALE AND A.AGMT_TYPE=D.AGMT_TYPE "
					+ ""
					+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO AND A.CONT_REV=E.CONT_REV "
					+ "AND A.AGMT_NO=E.AGMT_NO AND A.AGMT_REV=E.AGMT_REV AND A.CONTRACT_TYPE=E.CONTRACT_TYPE AND A.BUY_SALE=E.BUY_SALE AND A.AGMT_TYPE=E.AGMT_TYPE ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, end_dt);
			stmt.setString(4, start_dt);
			stmt.setString(5, transporter_cd);
			stmt.setString(6, transporter_plant_seq);
			stmt.setString(7, counterparty_plant_seq);
			stmt.setString(8, bu_unit);
			stmt.setString(9, comp_cd);
			stmt.setString(10, counterparty_cd);
			stmt.setString(11, "C");
			stmt.setString(12, "Y");
			stmt.setString(13, "A");
			stmt.setString(14, end_dt);
			stmt.setString(15, start_dt);
			stmt.setString(16, transporter_cd);
			stmt.setString(17, transporter_plant_seq);
			stmt.setString(18, counterparty_plant_seq);
			stmt.setString(19, bu_unit);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String agmt=rset.getString(1)==null?"":rset.getString(1);
				String agmt_rev=rset.getString(2)==null?"":rset.getString(2);
				String cont=rset.getString(3)==null?"":rset.getString(3);
				String cont_rev=rset.getString(4)==null?"":rset.getString(4);
				String cont_type=rset.getString(5)==null?"":rset.getString(5);
				String cont_ref=rset.getString(6)==null?"":rset.getString(6);
				String trade_ref=rset.getString(7)==null?"":rset.getString(7);
				String startDt=rset.getString(8)==null?"":rset.getString(8);
				String endDt=rset.getString(9)==null?"":rset.getString(9);
				String agmt_base=rset.getString(10)==null?"":rset.getString(10);
				String cargo_no = rset.getString(11)==null?"":rset.getString(11);
				String contractName = rset.getString(12)==null?"":rset.getString(12);
				
				if(cont_type.equals("X"))
				{
					cont_ref=trade_ref;
				}
				
				VAGMT_NO.add(agmt);
				VAGMT_REV_NO.add(agmt_rev);
				VCONT_NO.add(cont);
				VCONT_REV_NO.add(cont_rev);
				VCONTRACT_TYPE.add(cont_type);
				VCONT_REF_NO.add(cont_ref);
				VSTART_DT.add(startDt);
				VEND_DT.add(endDt);
				
				String deal_no=""+utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo_no);
				
				if(agmt_base.equals("D"))
				{
					deal_no=deal_no+" <font style='background: #a6ff4d;'>[DLV]</font>";
				}
				
				String cont_mapping=cont_type+"-"+counterparty_cd+"-"+agmt+"-"+agmt_rev+"-"+cont+"-"+cont_rev;
				String cont_mapping_like=cont_type+"-"+counterparty_cd+"-"+agmt+"-%-"+cont+"-%";
				if(contractName.equals("LTCORA"))
				{
					cont_mapping+="-"+cargo_no;
					cont_mapping_like+="-"+cargo_no;
				}
				
				VDIS_DEAL_NO.add(deal_no);
				VCONT_MAPPING.add(cont_mapping);
				
				int count=0;
				queryString1="SELECT COUNT(*) "
						+ "FROM FMS_TRANS_CT_CONT_MAP "
						+ "WHERE COMPANY_CD=? AND ENTRY_TRANS_CD=? "
						+ "AND ENTRY_TRANS_PLANT=? AND CONTRACT_TYPE=? "
						+ "AND EXIT_COUNTERPARTY=? AND EXIT_PLANT_SEQ=? "
						+ "AND SEQ_NO=? AND BU_SEQ=? AND CONT_MAPPING LIKE ?";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, transporter_cd);
				stmt1.setString(3, transporter_plant_seq);
				stmt1.setString(4, contract_type);
				stmt1.setString(5, counterparty_cd);
				stmt1.setString(6, counterparty_plant_seq);
				stmt1.setString(7, seq_no);
				stmt1.setString(8, bu_unit);
				stmt1.setString(9, cont_mapping_like);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					count=rset1.getInt(1);
				}
				rset1.close();
				stmt1.close();
				
				if(count>0)
				{
					VIS_ATTACHED.add("Y");
				}
				else
				{
					VIS_ATTACHED.add("N");
				}
				
				int buyer_row=0;
				int seller_row=0;
				int allocation_row=0;
				int gtc_row=0;
				int row=0;
				
				String gtc_entryPointMap=transporter_cd+"-"+transporter_plant_seq;
				String gtc_exitPointMap=contract_type+"-"+counterparty_cd+"-"+counterparty_plant_seq;
				String gtc_cont_mapping_like=cont_type+"-"+agmt+"-%-"+cont+"-%";
				
				String ct_ref=utilBean.getTransporterCT_Reference(conn,comp_cd, transporter_cd, transporter_plant_seq, contract_type, counterparty_cd, counterparty_plant_seq, seq_no,bu_unit);
				
				queryString1="SELECT COUNT(*) "
		  				+ "FROM FMS_DAILY_BUYER_NOM_DTL A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
						+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? AND CARGO_NO=? "
						+ "AND TO_DATE(?,'DD/MM/YYYY')<=GAS_DT AND TO_DATE(?,'DD/MM/YYYY')>=GAS_DT AND CT_REF=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_BUYER_NOM_DTL B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
						+ "AND B.GAS_DT=A.GAS_DT AND B.SEQ_NO=A.SEQ_NO AND B.CARGO_NO=A.CARGO_NO) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, cont);
				stmt1.setString(2, agmt);
				stmt1.setString(3, comp_cd);
				stmt1.setString(4, counterparty_cd);
				stmt1.setString(5, transporter_cd);
				stmt1.setString(6, transporter_plant_seq);
				stmt1.setString(7, counterparty_plant_seq);
				stmt1.setString(8, cont_type);
				stmt1.setString(9, bu_unit);
				stmt1.setString(10, cargo_no);
				stmt1.setString(11, start_dt);
				stmt1.setString(12, end_dt);
				stmt1.setString(13, ct_ref);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					buyer_row=rset1.getInt(1);
					row+=buyer_row;
				}
				rset1.close();
				stmt1.close();
				
				queryString1="SELECT COUNT(*) "
		  				+ "FROM FMS_DAILY_SELLER_NOM_DTL A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
						+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? AND CARGO_NO=? "
						+ "AND TO_DATE(?,'DD/MM/YYYY')<=GAS_DT AND TO_DATE(?,'DD/MM/YYYY')>=GAS_DT AND CT_REF=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_SELLER_NOM_DTL B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
						+ "AND B.GAS_DT=A.GAS_DT AND B.SEQ_NO=A.SEQ_NO AND B.CARGO_NO=A.CARGO_NO) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, cont);
				stmt1.setString(2, agmt);
				stmt1.setString(3, comp_cd);
				stmt1.setString(4, counterparty_cd);
				stmt1.setString(5, transporter_cd);
				stmt1.setString(6, transporter_plant_seq);
				stmt1.setString(7, counterparty_plant_seq);
				stmt1.setString(8, cont_type);
				stmt1.setString(9, bu_unit);
				stmt1.setString(10, cargo_no);
				stmt1.setString(11, start_dt);
				stmt1.setString(12, end_dt);
				stmt1.setString(13, ct_ref);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					seller_row=rset1.getInt(1);
					row+=seller_row;
				}
				rset1.close();
				stmt1.close();
				
				queryString1="SELECT COUNT(*) "
		  				+ "FROM FMS_DAILY_ALLOCATION_DTL_CT A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
						+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? AND CARGO_NO=? "
						+ "AND TO_DATE(?,'DD/MM/YYYY')<=GAS_DT AND TO_DATE(?,'DD/MM/YYYY')>=GAS_DT AND CT_REF=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL_CT B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
						+ "AND B.GAS_DT=A.GAS_DT AND B.SEQ_NO=A.SEQ_NO AND B.CARGO_NO=A.CARGO_NO AND B.DTL_CATEGORY=A.DTL_CATEGORY) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, cont);
				stmt1.setString(2, agmt);
				stmt1.setString(3, comp_cd);
				stmt1.setString(4, counterparty_cd);
				stmt1.setString(5, transporter_cd);
				stmt1.setString(6, transporter_plant_seq);
				stmt1.setString(7, counterparty_plant_seq);
				stmt1.setString(8, cont_type);
				stmt1.setString(9, bu_unit);
				stmt1.setString(10, cargo_no);
				stmt1.setString(11, start_dt);
				stmt1.setString(12, end_dt);
				stmt1.setString(13, ct_ref);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					allocation_row=rset1.getInt(1);
					row+=allocation_row;
				}
				rset1.close();
				stmt1.close();
				
				//CHECKING GTC CONTRACT
				queryString1="SELECT COUNT(*) "
						+ "FROM FMS_GTA_CONT_MST A, FMS_GTA_CONT_MAP B, FMS_GTA_CONT_BU C "
						+ "WHERE A.COMPANY_CD=? AND A.CONTRACT_TYPE='C' AND A.ENTRY_PT_MAPPING_ID=? AND A.EXIT_PT_MAPPING_ID=? "
						//+ "AND A.START_DT=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.CT_SEQ_NO=? AND B.CUSTOMER_CD=? AND B.SELL_CONT_MAP LIKE ? AND C.PLANT_SEQ_NO=? "
						+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ ""
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_REV=B.CONT_REV "
						+ ""
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV "
						+ "AND A.CONT_NO=C.CONT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.CONT_REV=C.CONT_REV ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, gtc_entryPointMap);
				stmt1.setString(3, gtc_exitPointMap);
				//stmt1.setString(4, start_dt);
				//stmt1.setString(5, end_dt);
				stmt1.setString(4, seq_no);
				stmt1.setString(5, counterparty_cd);
				stmt1.setString(6, gtc_cont_mapping_like);
				stmt1.setString(7, bu_unit);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					gtc_row=rset1.getInt(1);
					row+=gtc_row;
				}
				rset1.close();
				stmt1.close();
				
				VROW_INFO.add(buyer_row+" : "+seller_row+" : "+allocation_row+" : "+gtc_row);
				VROW_COUNT.add(row);
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
	
	public void getFilterTransporterList()
	{
		String function_nm="getFilterTransporterList()";
		try
		{
			queryString="SELECT DISTINCT ENTRY_TRANS_CD "
					+ "FROM FMS_TRANSPORTER_CT_MST "
					+ "WHERE COMPANY_CD=? "
					+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, to_dt);
			stmt.setString(3, from_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String transptr_cd=rset.getString(1)==null?"":rset.getString(1);
				VMST_TRANSPTR_CD.add(transptr_cd);
				VMST_TRANSPTR_ABBR.add(""+utilBean.getCounterpartyABBR(conn, transptr_cd));
				VMST_TRANSPTR_NM.add(""+utilBean.getCounterpartyName(conn, transptr_cd));
			}
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
	String from_dt="";
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	String to_dt="";
	public void setTo_dt(String to_dt) {this.to_dt=to_dt;}
	String transptr_cd="";
	public void setTransptr_Cd(String transptr_cd) {this.transptr_cd=transptr_cd;}
	
	String transporter_cd = "";
	String transporter_plant_seq = "";
	String counterparty_cd = "";
	String counterparty_plant_seq = "";
	String customer_plant_seq = "";
	String customer_code = "";
	String effective_dt = "";
	String start_dt ="";
	String end_dt ="";
	String seq_no ="";
	String contract_type ="";
	String bu_unit ="";
	
	public void setTransporter_cd(String transporter_cd) {this.transporter_cd = transporter_cd;}
	public void setTransporter_plant_seq(String transporter_plant_seq) {this.transporter_plant_seq = transporter_plant_seq;}
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setCounterparty_plant_seq(String counterparty_plant_seq) {this.counterparty_plant_seq = counterparty_plant_seq;}
	public void setCustomer_plant_seq(String customer_plant_seq) {this.customer_plant_seq = customer_plant_seq;}
	public void setCustomer_code(String customer_code) {this.customer_code = customer_code;}
	public void setEffective_dt(String effective_dt) {this.effective_dt = effective_dt;}
	public void getStart_dt(String start_dt) {this.start_dt = start_dt;} //WRITTEN WRONG FUNCTION NAME DON'T USE ANY MORE
	public void getEnd_dt(String end_dt) {this.end_dt = end_dt;} //WRITTEN WRONG FUNCTION NAME DON'T USE ANY MORE
	
	public void setStart_dt(String start_dt) {this.start_dt = start_dt;}
	public void setEnd_dt(String end_dt) {this.end_dt = end_dt;}
	public void setSeq_no(String seq_no) {this.seq_no = seq_no;}
	public void setContract_type(String contract_type) {this.contract_type = contract_type;}
	public void setBu_unit(String bu_unit) {this.bu_unit = bu_unit;}
	
	Vector VTRANSPORTER_CD = new Vector();
	Vector VTRANSPORTER_NM = new Vector();
	Vector VTRANSPORTER_ABBR = new Vector();
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VCUSTOMER_PLANT_SEQ = new Vector();
	Vector VCUSTOMER_PLANT_SEQ_NM = new Vector();
	
	// For Table data
	Vector VTRANSPTR_CD = new Vector();
	Vector VTRANSPTR_NM = new Vector();
	Vector VTRANSPTR_ABBR = new Vector();
	Vector VTRANS_PLANT_SEQ = new Vector();
	Vector VTRANS_PLANT_SEQ_NM = new Vector();
	Vector VCOUNTERPTY_CD = new Vector();
	Vector VCOUNTERPTY_NM = new Vector();
	Vector VCOUNTERPTY_ABBR=new Vector();
	Vector VPLANT_SEQ = new Vector();
	Vector VPLANT_SEQ_NM = new Vector();
	Vector VCUSTOMER_CODE = new Vector();
	Vector VEFF_DT = new Vector();
	Vector VSTATUS = new Vector();
	Vector VINDEX = new Vector();
	Vector VEXITPOINT_TYPE = new Vector();
	Vector VCT_REFERENCE = new Vector();
	Vector VUTR = new Vector();
	Vector VSTART_DT = new Vector();
	Vector VEND_DT = new Vector();
	Vector VSEQ_NO = new Vector();
	Vector VBU_UNIT = new Vector();
	Vector VBU_UNIT_ABBR = new Vector();
	
	Vector VSUB_COUNTERPTY_NM = new Vector();
	Vector VSUB_PLANT_SEQ_NM = new Vector();
	Vector VSUB_CUSTOMER_CODE = new Vector();
	Vector VSUB_EFF_DT = new Vector();
	Vector VSUB_STATUS = new Vector();
	Vector VSUB_INDEX = new Vector();
	
	Vector VAGMT_NO = new Vector();
	Vector VAGMT_REV_NO = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VCONT_REV_NO = new Vector();
	Vector VDIS_DEAL_NO = new Vector();
	Vector VCONTRACT_TYPE = new Vector();
	Vector VCONT_REF_NO = new Vector();
	Vector VCONT_MAPPING = new Vector();
	Vector VIS_ATTACHED = new Vector();
	Vector VDEAL_ATTACHMENT = new Vector();
	Vector VGTC_DEAL_ATTACHMENT = new Vector();
	Vector VAGMT_BASE = new Vector();
	
	Vector VROW_COUNT = new Vector();
	Vector VROW_INFO = new Vector();
	Vector VMIN_NOM_DT = new Vector();
	Vector VMAX_NOM_DT = new Vector();
	
	Vector VGTC_MIN_DT = new Vector();
	Vector VGTC_MAX_DT = new Vector();
	
	Vector VBU_CD = new Vector();
	Vector VBU_PLANT_NM = new Vector();
	Vector VBU_PLANT_ABBR = new Vector();
	Vector VBU_PLANT_SEQ_NO = new Vector();
	
	Vector VMST_TRANSPTR_CD = new Vector();
	Vector VMST_TRANSPTR_ABBR = new Vector();
	Vector VMST_TRANSPTR_NM = new Vector();
	
	public Vector getVTRANSPORTER_CD() {return VTRANSPORTER_CD;}
	public Vector getVTRANSPORTER_NM() {return VTRANSPORTER_NM;}
	public Vector getVTRANSPORTER_ABBR() {return VTRANSPORTER_ABBR;}
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVCUSTOMER_PLANT_SEQ() {return VCUSTOMER_PLANT_SEQ;}
	public Vector getVCUSTOMER_PLANT_SEQ_NM() {return VCUSTOMER_PLANT_SEQ_NM;}
	
	// For Table data
	public Vector getVTRANSPTR_CD() {return VTRANSPTR_CD;}
	public Vector getVTRANSPTR_NM() {return VTRANSPTR_NM;}
	public Vector getVTRANSPTR_ABBR() {return VTRANSPTR_ABBR;}
	public Vector getVTRANS_PLANT_SEQ() {return VTRANS_PLANT_SEQ;}
	public Vector getVTRANS_PLANT_SEQ_NM() {return VTRANS_PLANT_SEQ_NM;} 
	public Vector getVCOUNTERPTY_CD() {return VCOUNTERPTY_CD;}
	public Vector getVCOUNTERPTY_NM() {return VCOUNTERPTY_NM;}
	public Vector getVCOUNTERPTY_ABBR() {return VCOUNTERPTY_ABBR;}
	public Vector getVPLANT_SEQ() {return VPLANT_SEQ;}
	public Vector getVPLANT_SEQ_NM() {return VPLANT_SEQ_NM;}
	public Vector getVCUSTOMER_CODE() {return VCUSTOMER_CODE;}
	public Vector getVEFF_DT() {return VEFF_DT;}
	public Vector getVSTATUS() {return VSTATUS;}
	public Vector getVINDEX() {return VINDEX;}
	public Vector getVEXITPOINT_TYPE() {return VEXITPOINT_TYPE;}
	public Vector getVCT_REFERENCE() {return VCT_REFERENCE;}
	public Vector getVUTR() {return VUTR;}
	public Vector getVSTART_DT() {return VSTART_DT;}
	public Vector getVEND_DT() {return VEND_DT;}
	public Vector getVSEQ_NO() {return VSEQ_NO;}
	public Vector getVBU_UNIT() {return VBU_UNIT;}
	public Vector getVBU_UNIT_ABBR() {return VBU_UNIT_ABBR;}
	
	public Vector getVSUB_COUNTERPTY_NM() {return VSUB_COUNTERPTY_NM;}
	public Vector getVSUB_PLANT_SEQ_NM() {return VSUB_PLANT_SEQ_NM;}
	public Vector getVSUB_CUSTOMER_CODE() {return VSUB_CUSTOMER_CODE;}
	public Vector getVSUB_EFF_DT() {return VSUB_EFF_DT;}
	public Vector getVSUB_STATUS() {return VSUB_STATUS;}
	public Vector getVSUB_INDEX() {return VSUB_INDEX;}
	
	public Vector getVAGMT_NO() {return VAGMT_NO;}
	public Vector getVAGMT_REV_NO() {return VAGMT_REV_NO;}
	public Vector getVCONT_NO() {return VCONT_NO;}
	public Vector getVCONT_REV_NO() {return VCONT_REV_NO;}
	public Vector getVDIS_DEAL_NO() {return VDIS_DEAL_NO;}
	public Vector getVCONTRACT_TYPE() {return VCONTRACT_TYPE;}
	public Vector getVCONT_REF_NO() {return VCONT_REF_NO;}
	public Vector getVCONT_MAPPING() {return VCONT_MAPPING;}
	public Vector getVIS_ATTACHED() {return VIS_ATTACHED;}
	public Vector getVDEAL_ATTACHMENT() {return VDEAL_ATTACHMENT;}
	public Vector getVGTC_DEAL_ATTACHMENT() {return VGTC_DEAL_ATTACHMENT;}
	public Vector getVAGMT_BASE() {return VAGMT_BASE;}
	
	public Vector getVROW_COUNT() {return VROW_COUNT;}
	public Vector getVROW_INFO() {return VROW_INFO;}
	public Vector getVMIN_NOM_DT() {return VMIN_NOM_DT;}
	public Vector getVMAX_NOM_DT() {return VMAX_NOM_DT;}
	
	public Vector getVGTC_MIN_DT() {return VGTC_MIN_DT;}
	public Vector getVGTC_MAX_DT() {return VGTC_MAX_DT;}
	
	public Vector getVBU_CD() {return VBU_CD;}
	public Vector getVBU_PLANT_NM() {return VBU_PLANT_NM;}
	public Vector getVBU_PLANT_ABBR() {return VBU_PLANT_ABBR;}
	public Vector getVBU_PLANT_SEQ_NO() {return VBU_PLANT_SEQ_NO;}
	
	public Vector getVMST_TRANSPTR_CD() {return VMST_TRANSPTR_CD;}
	public Vector getVMST_TRANSPTR_ABBR() {return VMST_TRANSPTR_ABBR;}
	public Vector getVMST_TRANSPTR_NM() {return VMST_TRANSPTR_NM;}
}


