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

import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

public class DB_GtaMaster_Report 
{
	String db_src_file_name="DataBean_MapMaster.java";
	Connection conn; 
	PreparedStatement stmt;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt_temp;
	ResultSet rset; 
	ResultSet rset1;
	ResultSet rset2; 
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset_temp;
	String queryString="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	
	UtilBean utilBean = new UtilBean();
	
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
//	    			stmt = conn.createStatement();
//	    			stmt1= conn.createStatement();
//	    			stmt2= conn.createStatement();	
//	    			stmt3= conn.createStatement();
//	    			stmt_temp= conn.createStatement();
	    			
	    			if(callFlag.equalsIgnoreCase("GTA_CONTRACT_SUMMARY"))
	    			{
	    				getCustomerCounterpartyList();
	    				getSegment();
	    				getGtaContractSummary();
	    				if(segmentType.equals("C"))
	    				{
	    					getCustomerList();
	    				}
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
	    	if(rset_temp != null){try{rset_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp != null){try{stmt_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
	public void getCustomerCounterpartyList()
	{
		String function_nm="getCustomerCounterpartyList()";
		try
		{
			//utilBean.getEffectiveTransporterCounterpartyList(comp_cd);
			utilBean.getEffectiveEntityCounterpartyList(conn,comp_cd,"R");
			VMST_COUNTERPARTY_CD= utilBean.getCOUNTERPARTY_CD();
			VMST_COUNTERPARTY_NM = utilBean.getCOUNTERPARTY_NM();
			VMST_COUNTERPARTY_ABBR = utilBean.getCOUNTERPARTY_ABBR();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSegment()
	{
		String function_nm="getSegment()";
		try
		{
			VSEGMENT.add("CT - Customer");
			VSEGMENT.add("CT - Transporter");
			VSEGMENT.add("Parking");
			
			VSEGMENT_TYPE.add("C");
			VSEGMENT_TYPE.add("R");
			VSEGMENT_TYPE.add("K");
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getGtaContractSummary()
	{
		String function_nm="getGtaContractSummary()";
		try
		{
			if(!segmentType.equals("0"))
			{
				VTEMP_SEGMENT_TYPE.add(segmentType);
				if(segmentType.equals("C"))
				{
					VTEMP_SEGMENT.add("CT - Customer");
				}
				else if(segmentType.equals("R"))
				{
					VTEMP_SEGMENT.add("CT - Transporter");
				}
				else if(segmentType.equals("K"))
				{
					VTEMP_SEGMENT.add("Parking");
				}
				else
				{
					VTEMP_SEGMENT.add("");
				}
			}
			else
			{
				VTEMP_SEGMENT=VSEGMENT;
				VTEMP_SEGMENT_TYPE=VSEGMENT_TYPE;
			}
			
			for(int i=0; i<VTEMP_SEGMENT_TYPE.size(); i++)
			{
				int index=0;
				int cnt=0;
				if(segmentType.equals("C"))
				{
					queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONT_REF_NO,"
							+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),"
							+ "TO_CHAR(A.ENT_DT,'DD/MM/YYYY'),A.ENT_BY,A.ENTRY_PT_MAPPING_ID,A.EXIT_PT_MAPPING_ID,A.MDQ "
							+ "FROM FMS_GTA_CONT_MST A,FMS_GTA_CONT_MAP B "
							+ "WHERE A.COMPANY_CD=? AND A.CONTRACT_TYPE=? "
							+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
							+ "AND A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE ";
						if(!customer_cd.equals("0"))
						{	
							queryString+= "AND B.CUSTOMER_CD=? ";
						}
				}
				else
				{
					queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONT_REF_NO,"
							+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),"
							+ "TO_CHAR(ENT_DT,'DD/MM/YYYY'),ENT_BY,ENTRY_PT_MAPPING_ID,EXIT_PT_MAPPING_ID,MDQ "
							+ "FROM FMS_GTA_CONT_MST A "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
							+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				}
				if(!counterparty_cd.equals("0"))
				{
					queryString+=" AND A.COUNTERPARTY_CD=? ";
				}
				stmt=conn.prepareStatement(queryString);
				if(segmentType.equals("C"))
				{
					stmt.setString(++cnt, comp_cd);
					stmt.setString(++cnt, segmentType);
					stmt.setString(++cnt, to_dt);
					stmt.setString(++cnt, from_dt);
					if(!customer_cd.equals("0"))
					{
						stmt.setString(++cnt, customer_cd);
					}
				}
				else
				{
					stmt.setString(++cnt, comp_cd);
					stmt.setString(++cnt, ""+VTEMP_SEGMENT_TYPE.elementAt(i));
					stmt.setString(++cnt, to_dt);
					stmt.setString(++cnt, from_dt);
				}
				if(!counterparty_cd.equals("0"))
				{
					stmt.setString(++cnt, counterparty_cd);
				}
				rset=stmt.executeQuery();
				while(rset.next())
				{
					index+=1;
					String companyCd = rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd = rset.getString(2)==null?"":rset.getString(2);
					String agmt = rset.getString(3)==null?"":rset.getString(3);
					String agmt_rev = rset.getString(4)==null?"":rset.getString(4);
					String cont = rset.getString(5)==null?"":rset.getString(5);
					String cont_rev = rset.getString(6)==null?"":rset.getString(6);
					String cont_ref = rset.getString(7)==null?"":rset.getString(7);
					String cont_type=""+VTEMP_SEGMENT_TYPE.elementAt(i);
					VCOUNTERPARTY_CD.add(countpty_cd);
					VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
					VDIS_CONT_MAPPING.add(utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type,""));
					VCONT_REF.add(cont_ref);
					VSTART_DT.add(rset.getString(8)==null?"":rset.getString(8));
					VEND_DT.add(rset.getString(9)==null?"":rset.getString(9));
					
					String entry=rset.getString(12)==null?"":rset.getString(12);
					String exit=rset.getString(13)==null?"":rset.getString(13);
					
					double entryQty=0;
					double exitQty=0;
					String min_dt="";
					String max_dt="";
					queryString="SELECT SUM(QTY_MMBTU),TO_CHAR(MIN(GAS_DT),'DD/MM/YYYY'),TO_CHAR(MAX(GAS_DT),'DD/MM/YYYY'),SUM(EXIT_QTY_MMBTU) "
							+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
			  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
							+ "AND AGMT_NO=? AND CONT_NO=? "
							+ "AND ENTRY_PT_MAPPING_ID=? AND EXIT_PT_MAPPING_ID=? "
							+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
							+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
					stmt_temp=conn.prepareStatement(queryString);
					stmt_temp.setString(1, companyCd);
					stmt_temp.setString(2, countpty_cd);
					stmt_temp.setString(3, cont_type);
					stmt_temp.setString(4, agmt);
					stmt_temp.setString(5, cont);
					stmt_temp.setString(6, entry);
					stmt_temp.setString(7, exit);
					rset_temp=stmt_temp.executeQuery();
					if(rset_temp.next())
					{
						entryQty=rset_temp.getDouble(1);
						min_dt=rset_temp.getString(2)==null?"":rset_temp.getString(2);
						max_dt=rset_temp.getString(3)==null?"":rset_temp.getString(3);
						exitQty=rset_temp.getDouble(4);
					}
					rset_temp.close();
					stmt_temp.close();
					
					double mmscm = 38900;
					VENTRY_QTY_MMBTU.add(nf3.format(entryQty));
					VENTRY_QTY_MMSCM.add(nf.format(entryQty/mmscm));
					VEXIT_QTY_MMBTU.add(nf3.format(exitQty));
					VEXIT_QTY_MMSCM.add(nf.format(exitQty/mmscm));
					VIMBALANCE_QTY.add(nf3.format(entryQty -exitQty));
					VALLOC_MIN_DT.add(min_dt);
					VALLOC_MAX_DT.add(max_dt);
					
					String ent_dt=rset.getString(10)==null?"":rset.getString(10);
					String ent_by=rset.getString(11)==null?"":rset.getString(11);
				
					VENT_DT.add(ent_dt);
					VENT_BY.add(""+utilBean.getEmpName(conn,ent_by));
					
					String entryTransCd="";
					String entryTransPlant="";
					
					String exitEntity="";
					String exitTransCd="";
					String exitTransPlant="";
					
					if(!entry.equals(""))
					{
						String[] split = entry.split("-");
						entryTransCd=split[0];
						entryTransPlant=split[1];
					}
					
					if(!exit.equals(""))
					{
						String[] split = exit.split("-");
						exitEntity=split[0];
						exitTransCd=split[1];
						exitTransPlant=split[2];
					}
					
					String entryAbbr=utilBean.getCounterpartyPlantName(conn,entryTransCd, comp_cd, entryTransPlant, "R");
					String exitAbbr=utilBean.getCounterpartyPlantName(conn,exitTransCd, comp_cd, exitTransPlant,exitEntity);
					
					VENTRY_POINT_NAME.add(entryAbbr);
					VEXIT_POINT_NAME.add(exitAbbr);
					VMDQ.add(nf.format(rset.getDouble(14)));
					
					String DelvNm="";
				  	queryString1 = "SELECT TRANSPORTER_CD,PLANT_SEQ_NO "
				  			+ "FROM FMS_SUPPLY_CONT_TRANSPTR "
				  			+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
				  			+ "AND CONT_NO=? AND CONT_REV=? "
				  			+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=?";
				  	stmt1=conn.prepareStatement(queryString1);
				  	stmt1.setString(1, companyCd);
				  	stmt1.setString(2, agmt);
				  	stmt1.setString(3, agmt_rev);
				  	stmt1.setString(4, cont);
				  	stmt1.setString(5, cont_rev);
				  	stmt1.setString(6, countpty_cd);
				  	stmt1.setString(7, cont_type);
				  	rset1=stmt1.executeQuery();
		  			while(rset1.next())
		  			{
		  				String trans_cd = rset1.getString(1)==null?"0":rset1.getString(1);
		  				String plant_seq = rset1.getString(2)==null?"0":rset1.getString(2);
		  				if(DelvNm.equals(""))
		  				{
		  					DelvNm+=""+utilBean.getCounterpartyPlantABBR(conn,trans_cd, companyCd, plant_seq, "R");
		  				}
		  				else
		  				{
		  					DelvNm+=","+utilBean.getCounterpartyPlantABBR(conn,trans_cd, companyCd, plant_seq, "R");
		  				}
		  			}
		  			rset1.close();
		  			stmt1.close();
		  			VDELV_POINT.add(DelvNm);
		  			
		  			String BuUnit="";
				  	queryString2 = "SELECT COMPANY_CD,PLANT_SEQ_NO "
				  			+ "FROM FMS_GTA_CONT_BU "
				  			+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
				  			+ "AND CONT_NO=? AND CONT_REV=? "
				  			+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=?";
				  	stmt2=conn.prepareStatement(queryString2);
				  	stmt2.setString(1, companyCd);
				  	stmt2.setString(2, agmt);
				  	stmt2.setString(3, agmt_rev);
				  	stmt2.setString(4, cont);
				  	stmt2.setString(5, cont_rev);
				  	stmt2.setString(6, countpty_cd);
				  	stmt2.setString(7, cont_type);
				  	rset2=stmt2.executeQuery();
		  			while(rset2.next())
		  			{
		  				String ownercd = rset2.getString(1)==null?"0":rset2.getString(1);
		  				String plant_seq = rset2.getString(2)==null?"0":rset2.getString(2);
		  				if(BuUnit.equals(""))
		  				{
		  					BuUnit+=""+utilBean.getCounterpartyPlantABBR(conn,ownercd, companyCd, plant_seq, "B");
		  				}
		  				else
		  				{
		  					BuUnit+=","+utilBean.getCounterpartyPlantABBR(conn,ownercd, companyCd, plant_seq, "B");
		  				}
		  			}
		  			rset2.close();
		  			stmt2.close();
		  			VBU_POINT.add(BuUnit);
		  			
		  			String sales_cont_nm="";
					queryString3="SELECT CUSTOMER_CD,SELL_CONT_MAP "
							+ "FROM FMS_GTA_CONT_MAP "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=?";
					stmt3=conn.prepareStatement(queryString3);
				  	stmt3.setString(1, companyCd);
				  	stmt3.setString(2, countpty_cd);
				  	stmt3.setString(3, cont);
				  	stmt3.setString(4, cont_rev);
				  	stmt3.setString(5, agmt);
				  	stmt3.setString(6, agmt_rev);
				  	stmt3.setString(7, cont_type);
				  	rset3=stmt3.executeQuery();
					if(rset3.next())
					{
						String customer_cd=rset3.getString(1)==null?"":rset3.getString(1);
						String sales_cont_map=rset3.getString(2)==null?"":rset3.getString(2);
						
						String contType="";
						String agmtno="";
						String agmt_revno="";
						String contno="";
						String cont_revno="";
						
						String[] split=sales_cont_map.split("-");
						contType=split[0];
						agmtno=split[1];
						agmt_revno=split[2];
						contno=split[3];
						cont_revno=split[4];
						
						cont_ref="";
						if(cont_type.equals("K"))
						{
							queryString2="SELECT CONT_REF_NO "
									+ "FROM FMS_GTA_CONT_MST A "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
									+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
									+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
									+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
									+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
							stmt4=conn.prepareStatement(queryString2);
							stmt4.setString(1, comp_cd);
							stmt4.setString(2, customer_cd);
							stmt4.setString(3, contType);
							stmt4.setString(4, agmtno);
							stmt4.setString(5, agmt_revno);
							stmt4.setString(6, contno);
							rset4=stmt4.executeQuery();
							if(rset4.next())
							{
								cont_ref=rset4.getString(1)==null?"":rset4.getString(1);
							}
							rset4.close();
							stmt4.close();
						}
						else
						{
							queryString2="SELECT CONT_REF_NO,TRADE_REF_NO "
									+ "FROM FMS_SUPPLY_CONT_MST A "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
									+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
									+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
									+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
									+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
							stmt4=conn.prepareStatement(queryString2);
							stmt4.setString(1, comp_cd);
							stmt4.setString(2, customer_cd);
							stmt4.setString(3, contType);
							stmt4.setString(4, agmtno);
							stmt4.setString(5, agmt_revno);
							stmt4.setString(6, contno);
							rset4=stmt4.executeQuery();
							if(rset4.next())
							{
								cont_ref=rset4.getString(1)==null?"":rset4.getString(1);
								String trade_ref=rset4.getString(2)==null?"":rset4.getString(2);
								if(contType.equals("X"))
								{
									cont_ref=trade_ref;
								}
							}
							rset4.close();
							stmt4.close();
						}
						String deal_no=utilBean.NewDealMappingId(comp_cd, customer_cd, agmtno, agmt_revno, contno, cont_revno, contType,"");
						String countpty_abbr=utilBean.getCounterpartyABBR(conn,customer_cd);
						String countpty_nm=utilBean.getCounterpartyName(conn,customer_cd);
						sales_cont_nm=countpty_abbr+"-"+countpty_nm+"<br><font color='blue'>"+deal_no+"</font> ("+cont_ref+")";
					}
					rset3.close();
					stmt3.close();
					VLINKED_SALES_CONT.add(sales_cont_nm);
					
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
	
	public void getCustomerList()
	{
		String function_nm="getCustomerList()";
		try
		{
			queryString="SELECT DISTINCT B.CUSTOMER_CD "
					+ "FROM FMS_GTA_CONT_MST A,FMS_GTA_CONT_MAP B "
					+ "WHERE A.COMPANY_CD=? AND A.CONTRACT_TYPE=? "
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
					+ "AND A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, segmentType);
			stmt.setString(3, to_dt);
			stmt.setString(4, from_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String cust_cd = rset.getString(1)==null?"":rset.getString(1);
				VCUSTOMER_CD.add(cust_cd);
				VCUSTOMER_NM.add(utilBean.getCounterpartyName(conn,cust_cd));
				VCUSTOMER_ABBR.add(utilBean.getCounterpartyABBR(conn,cust_cd));
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
	
	String segmentType = "";
	String counterparty_cd = "";
	String from_dt = "";
	String to_dt = "";
	String customer_cd = "";
	
	public void setSegmentType(String segmentType) {this.segmentType = segmentType;}
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	public void setCustomer_cd(String customer_cd) {this.customer_cd = customer_cd;}
	
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VMST_COUNTERPARTY_CD = new Vector();
	Vector VMST_COUNTERPARTY_NM = new Vector();
	Vector VMST_COUNTERPARTY_ABBR = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VCONT_REV_NO = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VAGMT_REV_NO = new Vector();
	Vector VTCQ = new Vector();
	Vector VTCQ_MMSCM = new Vector();
	Vector VSIGNING_DT = new Vector();
	Vector VSTART_DT = new Vector();
	Vector VEND_DT = new Vector();
	Vector VRATE = new Vector();
	Vector VRATE_UNIT = new Vector();
	Vector VDIS_CONT_MAPPING = new Vector();
	Vector VCONT_STATUS = new Vector();
	Vector VCONT_STATUS_FLG = new Vector();
	Vector VCONT_REF = new Vector();
	Vector VENT_DT = new Vector();
	Vector VENT_BY = new Vector();
	Vector VAPRV_DT = new Vector();
	Vector VAPRV_BY = new Vector();
	Vector VSEGMENT = new Vector();
	Vector VSEGMENT_TYPE = new Vector();
	Vector VTEMP_SEGMENT = new Vector();
	Vector VTEMP_SEGMENT_TYPE = new Vector();
	Vector VINDEX = new Vector();
	Vector VPRICE_TYPE = new Vector();
	Vector VDELV_POINT = new Vector();
	Vector VBU_POINT = new Vector();
	Vector VIS_ALLOCATED = new Vector();
	Vector VSUPPLIED_QTY_MMBTU = new Vector();
	Vector VSUPPLIED_QTY_MMSCM = new Vector();
	Vector VBALANCE_QTY_MMBTU = new Vector();
	Vector VBALANCE_QTY_MMSCM = new Vector();
	Vector VALLOC_MIN_DT = new Vector();
	Vector VALLOC_MAX_DT = new Vector();
	
	Vector VENTRY_POINT_NAME = new Vector();
	Vector VEXIT_POINT_NAME = new Vector();
	
	Vector VENTRY_QTY_MMBTU = new Vector();
	Vector VENTRY_QTY_MMSCM = new Vector();
	Vector VEXIT_QTY_MMBTU = new Vector();
	Vector VEXIT_QTY_MMSCM = new Vector();
	
	Vector VMDQ = new Vector();
	Vector VIMBALANCE_QTY = new Vector();
	Vector VLINKED_SALES_CONT = new Vector();
	
	Vector VCUSTOMER_CD = new Vector();
	Vector VCUSTOMER_NM = new Vector();
	Vector VCUSTOMER_ABBR = new Vector();
	
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVMST_COUNTERPARTY_CD() {return VMST_COUNTERPARTY_CD;}
	public Vector getVMST_COUNTERPARTY_NM() {return VMST_COUNTERPARTY_NM;}
	public Vector getVMST_COUNTERPARTY_ABBR() {return VMST_COUNTERPARTY_ABBR;}
	public Vector getVCONT_NO() {return VCONT_NO;}
	public Vector getVCONT_REV_NO() {return VCONT_REV_NO;}
	public Vector getVAGMT_NO() {return VAGMT_NO;}
	public Vector getVAGMT_REV_NO() {return VAGMT_REV_NO;}
	public Vector getVTCQ() {return VTCQ;}
	public Vector getVTCQ_MMSCM() {return VTCQ_MMSCM;}
	public Vector getVSIGNING_DT() {return VSIGNING_DT;}
	public Vector getVSTART_DT() {return VSTART_DT;}
	public Vector getVEND_DT() {return VEND_DT;}
	public Vector getVRATE() {return VRATE;}
	public Vector getVRATE_UNIT() {return VRATE_UNIT;}
	public Vector getVDIS_CONT_MAPPING() {return VDIS_CONT_MAPPING;}
	public Vector getVCONT_STATUS() {return VCONT_STATUS;}
	public Vector getVCONT_STATUS_FLG() {return VCONT_STATUS_FLG;}
	public Vector getVCONT_REF() {return VCONT_REF;}
	public Vector getVENT_DT() {return VENT_DT;}
	public Vector getVENT_BY() {return VENT_BY;}
	public Vector getVAPRV_DT() {return VAPRV_DT;}
	public Vector getVAPRV_BY() {return VAPRV_BY;}
	public Vector getVSEGMENT() {return VSEGMENT;}
	public Vector getVSEGMENT_TYPE() {return VSEGMENT_TYPE;}
	public Vector getVTEMP_SEGMENT() {return VTEMP_SEGMENT;}
	public Vector getVTEMP_SEGMENT_TYPE() {return VTEMP_SEGMENT_TYPE;}
	public Vector getVINDEX() {return VINDEX;}
	public Vector getVPRICE_TYPE() {return VPRICE_TYPE;}
	public Vector getVDELV_POINT() {return VDELV_POINT;}
	public Vector getVBU_POINT() {return VBU_POINT;}
	public Vector getVIS_ALLOCATED() {return VIS_ALLOCATED;}
	
	public Vector getVSUPPLIED_QTY_MMBTU() {return VSUPPLIED_QTY_MMBTU;}
	public Vector getVSUPPLIED_QTY_MMSCM() {return VSUPPLIED_QTY_MMSCM;}
	public Vector getVBALANCE_QTY_MMBTU() {return VBALANCE_QTY_MMBTU;}
	public Vector getVBALANCE_QTY_MMSCM() {return VBALANCE_QTY_MMSCM;}
	public Vector getVALLOC_MIN_DT() {return VALLOC_MIN_DT;}
	public Vector getVALLOC_MAX_DT() {return VALLOC_MAX_DT;}
	
	public Vector getVENTRY_POINT_NAME() {return VENTRY_POINT_NAME;}
	public Vector getVEXIT_POINT_NAME() {return VEXIT_POINT_NAME;}
	
	public Vector getVENTRY_QTY_MMBTU() {return VENTRY_QTY_MMBTU;}
	public Vector getVENTRY_QTY_MMSCM() {return VENTRY_QTY_MMSCM;}
	public Vector getVEXIT_QTY_MMBTU() {return VEXIT_QTY_MMBTU;}
	public Vector getVEXIT_QTY_MMSCM() {return VEXIT_QTY_MMSCM;}
	
	public Vector getVMDQ() {return VMDQ;}
	public Vector getVIMBALANCE_QTY() {return VIMBALANCE_QTY;}
	public Vector getVLINKED_SALES_CONT() {return VLINKED_SALES_CONT;}
	
	public Vector getVCUSTOMER_CD() {return VCUSTOMER_CD;}
	public Vector getVCUSTOMER_NM() {return VCUSTOMER_NM;}
	public Vector getVCUSTOMER_ABBR() {return VCUSTOMER_ABBR;}
	
}
