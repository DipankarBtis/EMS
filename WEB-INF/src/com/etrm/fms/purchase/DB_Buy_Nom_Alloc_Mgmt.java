package com.etrm.fms.purchase;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;

public class DB_Buy_Nom_Alloc_Mgmt 
{
	String db_src_file_name="DB_Buy_Nom_Alloc_Mgmt.java";
	
	Connection conn; 
	PreparedStatement stmt;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt5;
	PreparedStatement stmt6;
	PreparedStatement stmt7;
	PreparedStatement stmt8;
	PreparedStatement stmt9;
	PreparedStatement stmt_tmp;
	PreparedStatement stmt_temp;
	ResultSet rset; 
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
	ResultSet rset6;
	ResultSet rset7;
	ResultSet rset8;
	ResultSet rset9;
	ResultSet rset_tmp;
	ResultSet rset_temp;
	/*String queryString="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	String queryString_temp="";*/
	
	UtilBean utilBean = new UtilBean();
	DateUtil dateUtil = new DateUtil();
	
	public HttpServletRequest request = null;
	public void setRequest(HttpServletRequest request) {this.request = request;}
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	
	public void init()
	{
		String function_nm="init()";
		try
		{
			Context initContext = new InitialContext();
	    	Context envContext  = (Context)initContext.lookup("java:/comp/env");
	    	DataSource ds = (DataSource)envContext.lookup(RuntimeConf.security_database);
	    	if(ds != null) 
	    	{
	    		conn = ds.getConnection();       
	    		if(conn != null)  
	    		{
	    			
	    			if(callFlag.equalsIgnoreCase("BUY_DAILY_BUYER_NOM"))
	    			{
	    				getBuyerNomination();
	    			}
	    			else if(callFlag.equalsIgnoreCase("BUY_DAILY_SELLER_NOM"))
	    			{
	    				getSellerNomination();
	    			}
	    			else if(callFlag.equalsIgnoreCase("BUY_DAILY_ALLOC"))
	    			{
	    				getDailyAllocation();
	    				getMoleculeMaster();
	    			}
	    			else if(callFlag.equalsIgnoreCase("BUY_DAILY_ALLOC-BKP")) //NOT IN USED
	    			{
	    				getTraderCounterpartyList();
	    				getTraderContractDetail();
	    				getDailyAllocation();
	    			}
	    			else if(callFlag.equalsIgnoreCase("REPORT_NOMINATION_ALLOCATION"))
	    			{
	    				getTraderCounterpartyList();
	    				if(cont_cargo.equals("isCargo")) 
	    				{
	    					getTraderCargoDetail();
	    				}
	    				else if(cont_cargo.equals("isCont"))
	    				{
	    					getTraderContractDetail();
	    				}
	    				getSelectedTraderPlantList();
	    				getSelectedBusinessPlantList();
	    				ReportNominationAllocation();
	    			}
	    			else if(callFlag.equalsIgnoreCase("WEEKLY_BUYER_NOM"))
	    			{
	    				getTraderContractDetail();
	    				getTraderCargoDetail();
	    				getWeeklyBuyerNomination();	
	    			}
	    			else if(callFlag.equalsIgnoreCase("WEEKLY_SELLER_NOM"))
	    			{
	    				getTraderContractDetail();
	    				getTraderCargoDetail();
	    				getWeeklySellerNomination();	
	    			}
	    			else if(callFlag.equalsIgnoreCase("RPT_BUYER_NOM_TO_TRADER"))
	    			{
	    				getBuyerNomToTrd();
	    			}
	    			else if(callFlag.equalsIgnoreCase("PERIODIC_ALLOCATION"))
	    			{
	    				contract_type_nm = utilBean.getContractTypeName(contract_type);
	    				getTraderCounterpartyList();
	    				getTraderContractDetail();
	    				getTraderCargoDetail();
	    				getForthnightlyDateRang();
	    				getPeriodicAllocation();
	    				getMoleculeMaster();
	    			}
	    			else if(callFlag.equalsIgnoreCase("VIEW_BUYER_NOM_TO_TRADER"))//Added By arth patel on 20240416 
	    			{
	    				getViewBuyerNominationToTrader();
	    			}
	    			else if(callFlag.equalsIgnoreCase("SEND_MAIL_BUYER_NOM_TO_TRADER"))//Added By arth patel on 20240423 
	    			{
	    				getSendMailBuyerToTraderDetail();
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
	    	if(rset6 != null){try{rset6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset7 != null){try{rset7.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset8 != null){try{rset8.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset9 != null){try{rset9.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_tmp != null){try{rset_tmp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_temp != null){try{rset_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt6 != null){try{stmt6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt7 != null){try{stmt7.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt8 != null){try{stmt8.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt9 != null){try{stmt9.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_tmp != null){try{stmt_tmp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp != null){try{stmt_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
	//Below method created by arth patel  on 20240423 
	private void getSendMailBuyerToTraderDetail()
	{
		String function_nm="printPdfFileForTraderBuyerNomination()";
		try
		{
			String ownAddress="";
			String ownCity="";
			String ownState="";
			String ownPin="";
			String ownCountry="";
			String ownPhone="";
			String ownEmail="";
			
			String queryString="SELECT ADDR,CITY,STATE,PIN,COUNTRY,PHONE,EMAIL "
					+ "FROM FMS_COMPANY_OWNER_ADDR_MST A "
					+ "WHERE COMPANY_CD=? AND ADDRESS_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_OWNER_ADDR_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.ADDRESS_TYPE=B.ADDRESS_TYPE "
					+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "C");
			rset=stmt.executeQuery();
			if(rset.next())
			{
				ownAddress  = rset.getString(1)==null?"":rset.getString(1);
				ownCity  = rset.getString(2)==null?"":rset.getString(2);
				ownState  = rset.getString(3)==null?"":rset.getString(3);
				ownPin  = rset.getString(4)==null?"":rset.getString(4);
				ownCountry  = rset.getString(5)==null?"":rset.getString(5);
				ownPhone  = rset.getString(6)==null?"":rset.getString(6);
				ownEmail  = rset.getString(7)==null?"":rset.getString(7);
			}
			rset.close();
			stmt.close();
			
			//get TO list
			String to_list="";
			String queryString1="SELECT EMAIL "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
					+ "AND ENTITY=? AND ADDR_FLAG=? AND NOM_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? "
					+ "AND TYPE=? "//AND TO_INV='Y' "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.TYPE=B.TYPE) "
					+ "AND EMAIL IS NOT NULL";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, to_contact);
			stmt1.setString(4, "T");
			stmt1.setString(5, "P"+plant_seq);
			stmt1.setString(6, "Y");
			stmt1.setString(7, "Y");
			stmt1.setString(8, "Y");
			stmt1.setString(9, "RLNG");
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				String email=rset1.getString(1)==null?"":rset1.getString(1);
				if(to_list.equals(""))
				{
					to_list=email;
				}
				else
				{
					to_list+=", "+email;
				}
			}
			rset1.close();
			stmt1.close();
			
			String queryString2="SELECT EMAIL "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
					+ "AND ENTITY=? AND ADDR_FLAG=? AND NOM_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? "
					+ "AND TYPE=? "//AND TO_INV='Y' "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.TYPE=B.TYPE) "
					+ "AND EMAIL IS NOT NULL";
			stmt1=conn.prepareStatement(queryString2);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, comp_cd);
			stmt1.setString(3, from_contact);
			stmt1.setString(4, "B");
			stmt1.setString(5, "P"+plant_seq);
			stmt1.setString(6, "Y");
			stmt1.setString(7, "Y");
			stmt1.setString(8, "Y");
			stmt1.setString(9, "RLNG");
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				String email=rset1.getString(1)==null?"":rset1.getString(1);
				if(to_list.equals(""))
				{
					to_list+=email;
				}
				else
				{
					to_list+=", "+email;
				}
			}
			rset1.close();
			stmt1.close();
			
			//get CC list
			String cc_list="";
			String queryString3="SELECT EMAIL "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO!=? "
					+ "AND ENTITY=? AND ADDR_FLAG=? AND NOM_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? "
					+ "AND TYPE=? " //AND (TO_INV='N' OR TO_INV IS NULL) "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.TYPE=B.TYPE) "
					+ "AND EMAIL IS NOT NULL";
			stmt2=conn.prepareStatement(queryString3);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, counterparty_cd);
			stmt2.setString(3, to_contact);
			stmt2.setString(4, "T");
			stmt2.setString(5, "P"+plant_seq);
			stmt2.setString(6, "Y");
			stmt2.setString(7, "Y");
			stmt2.setString(8, "Y");
			stmt2.setString(9, "RLNG");
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				String email=rset2.getString(1)==null?"":rset2.getString(1);
				if(cc_list.equals(""))
				{
					cc_list=email;
				}
				else
				{
					cc_list+=", "+email;
				}
			}
			rset2.close();
			stmt2.close();
			
			String queryString4="SELECT EMAIL "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO!=? "
					+ "AND ENTITY=? AND ADDR_FLAG=? AND NOM_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? "
					+ "AND TYPE=? " //AND (TO_INV='N' OR TO_INV IS NULL) "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.TYPE=B.TYPE) "
					+ "AND EMAIL IS NOT NULL";
			stmt2=conn.prepareStatement(queryString4);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, comp_cd);
			stmt2.setString(3, from_contact);
			stmt2.setString(4, "B");
			stmt2.setString(5, "P"+plant_seq);
			stmt2.setString(6, "Y");
			stmt2.setString(7, "Y");
			stmt2.setString(8, "Y");
			stmt2.setString(9, "RLNG");
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				String email=rset2.getString(1)==null?"":rset2.getString(1);
				if(cc_list.equals(""))
				{
					cc_list+=email;
				}
				else
				{
					cc_list+=", "+email;
				}
			}
			rset2.close();
			stmt2.close();
			
			// CC List selected from Company Correspondence Address
			String queryString5="SELECT EMAIL "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND ADDR_FLAG=? AND NOM_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND (TO_NOM=? OR TO_NOM IS NULL) "
					+ "AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.TYPE=B.TYPE) "
					+ "AND EMAIL IS NOT NULL";
			stmt3=conn.prepareStatement(queryString5);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, comp_cd);
			stmt3.setString(3, "B");
			stmt3.setString(4, "C");
			stmt3.setString(5, "Y");
			stmt3.setString(6, "Y");
			stmt3.setString(7, "Y");
			stmt3.setString(8, "N");
			stmt3.setString(9, "RLNG");
			rset3=stmt3.executeQuery();
			while(rset3.next())
			{
				String email=rset3.getString(1)==null?"":rset3.getString(1);
				if(cc_list.equals(""))
				{
					cc_list+=email;
				}
				else
				{
					cc_list+=", "+email;
				}
			}
			rset3.close();
			stmt3.close();
			
			String company_abbr = ""+utilBean.getCompanyAbbr(conn,comp_cd);
			String counterparty_nm=""+utilBean.getCounterpartyName(conn,counterparty_cd);
			String counterparty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
			String bu_plantNm = ""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B");
			String plantNm = ""+utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
			
			String subject="Buyer Nomination To Trader("+counterparty_abbr+"-"+plantNm+")-"+report_dt;
			
			String file_nm=""+company_abbr+"_"+bu_plantNm+"_BUYER_NOM_to_"+counterparty_abbr+"_"+plantNm+"_"+contract_type+agmt_no+"-"+cont_no+"-"+report_dt.replaceAll("/", "")+".pdf";
			
			String directory_path=file_path+File.separator+file_nm;
			File pdfFile = new File(directory_path);
			if(!pdfFile.exists())
			{
				file_nm="";
			}
			
			String companyNm=utilBean.getCompanyName(conn,comp_cd);
			String mail_body="Dear Sir/Madam,"
					+ "\n\nPlease find enclosed "+counterparty_abbr+"-"+plantNm+" Buyer Nomination details for the Report date "+report_dt+"."
					+ "\nIn case of any discrepancy, please revert to us by end of today or else it will be construed as deemed acceptance."
					+ "\n\nIn case of any query, please contact us at "+ownEmail+""				
					+ "\n\n\nThank You,"
					+ "\n\n"+companyNm+""
					+ "\n"+ownAddress+", "
					+ "\n"+ownCity+", "+ownState+" - "+ownPin+""
					+ "\nEmail: "+ownEmail+""
					+ "\nPh: "+ownPhone+""
					+ "\n\n***This is an auto-generated email from the system, please do not reply to this email.";
			
			VMAIL_FROM_LIST.add(utilBean.getFromMailId(conn));
			VMAIL_TO_LIST.add(to_list);
			VMAIL_CC_LIST.add(cc_list);
			VMAIL_SUBJECT.add(subject);
			VMAIL_ATTACHMENT.add(file_nm);
			VMAIL_ATTACHMENT_PATH.add("/purchase/buyer_nom_to_trader/");
			VMAIL_BODY.add(mail_body);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	//Below method created by arth patel  on 20240416 
	private void getViewBuyerNominationToTrader() 
	{
		String function_nm="printPdfFileForTraderBuyerNomination()";
		Rectangle pageSize = new Rectangle(595, 842);
		pageSize.setBackgroundColor(new BaseColor(0xffffff));
		Document document = new Document(pageSize);
		try
		{
	
			String counterparty_nm=""+utilBean.getCounterpartyName(conn,counterparty_cd);
			String counterparty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
			String bu_plantNm = ""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B");
			plantNm = ""+utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
	
	
			String queryString = "SELECT TO_CHAR(NEXT_DAY(TO_DATE(?,'DD/MM/YYYY')-7,'MONDAY'),'DD/MM/YYYY') , TO_CHAR(NEXT_DAY(TO_DATE(?,'DD/MM/YYYY')-7,'MONDAY')+6,'DD/MM/YYYY') FROM DUAL";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, report_dt);
			stmt.setString(2, report_dt);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				from_date = rset.getString(1)==null?"":rset.getString(1);
				to_date = rset.getString(2)==null?"":rset.getString(2);
			}
			stmt.close();
			rset.close();
			
			String queryString1="SELECT CONT_REF_NO,TO_CHAR(SIGNING_DT,'DD-MON-YY'),TRADE_REF_NO  "
					+ "FROM FMS_TRADER_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
					+ "AND CONT_NO=? AND AGMT_NO=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt=conn.prepareStatement(queryString1);
			stmt.setString(1, comp_cd);
			stmt.setString(2, contract_type);
			stmt.setString(3, cont_no);
			stmt.setString(4, agmt_no);
			stmt.setString(5, counterparty_cd);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				contRef=rset.getString(1)==null?"":rset.getString(1);
				signingDt=rset.getString(2)==null?"":rset.getString(2);
				
				String trade_ref=rset.getString(3)==null?"":rset.getString(3);
				if(contract_type.equals("I"))
				{
					contRef=trade_ref;
				}
			}
			stmt.close();
			rset.close();
	
	
			int count=0;
			String queryString2="SELECT GEN_TIME,TO_CHAR(GEN_DT,'DD-MON-YY'),TO_CHAR(GAS_DT,'DD-MON-YY'),QTY_MMBTU "
	  				+ "FROM FMS_BUY_DAILY_BUYER_NOM A "
	  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND PLANT_SEQ=? "
	  				+ "AND BU_SEQ=? AND CONTRACT_TYPE=? AND CONT_NO=? AND AGMT_NO=? ";
			if(frq_type.equals("W"))
			{
				queryString2+= "AND GAS_DT >= TO_DATE(?,'DD/MM/YYYY') AND GAS_DT <= TO_DATE(?,'DD/MM/YYYY') ";
			}
			else
			{
				queryString2+= "AND GAS_DT = TO_DATE(?,'DD/MM/YYYY') ";
			}
			queryString2+="AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_BUYER_NOM B "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
					+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
					+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) "
					+ "ORDER BY GAS_DT";
			stmt=conn.prepareStatement(queryString2);
			stmt.setString(++count, comp_cd);
			stmt.setString(++count, counterparty_cd);
			stmt.setString(++count, plant_seq);
			stmt.setString(++count, bu_plant_seq);
			stmt.setString(++count, contract_type);
			stmt.setString(++count, cont_no);
			stmt.setString(++count, agmt_no);
			if(frq_type.equals("W"))
			{
				stmt.setString(++count, from_date);
				stmt.setString(++count, to_date);
			}
			else
			{
				stmt.setString(++count, report_dt);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VGEN_TIME.add(rset.getString(1)==null?"":rset.getString(1));
				VGEN_DT.add(rset.getString(2)==null?"":rset.getString(2));
				VGAS_DT.add(rset.getString(3)==null?"":rset.getString(3));
				VQTY_MMBTU.add(nf.format(rset.getDouble(4)));
			}
			stmt.close();
			rset.close();
			
			String queryString3="SELECT CONTACT_PERSON, FAX_1, FAX_2,PHONE,MOBILE "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY='T' AND SEQ_NO=? "
					+ "AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
					+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY') AND A.TYPE=B.TYPE)";
			stmt=conn.prepareStatement(queryString3);
			stmt.setString(1, comp_cd);	
			stmt.setString(2, counterparty_cd);	
			stmt.setString(3, to_contact);	
			stmt.setString(4, report_dt);	
			stmt.setString(5, "RLNG");	
			rset=stmt.executeQuery();
			if(rset.next())
			{
				to_contact_nm  = rset.getString(1)==null?"":rset.getString(1);
				to_fax  = rset.getString(2)==null?"":rset.getString(2);
				to_fax2  = rset.getString(3)==null?"":rset.getString(3);
				to_phone = rset.getString(4)==null?"":rset.getString(4);
				to_mobile  = rset.getString(5)==null?"":rset.getString(5);
			}
			rset.close();
			stmt.close();
	
			
			String queryString4="SELECT CONTACT_PERSON, FAX_1, FAX_2,PHONE,MOBILE "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY='B' AND SEQ_NO=? "
					+ "AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
					+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY') AND A.TYPE=B.TYPE)";
			stmt=conn.prepareStatement(queryString4);
			stmt.setString(1, comp_cd);	
			stmt.setString(2, comp_cd);	
			stmt.setString(3, from_contact);	
			stmt.setString(4, report_dt);	
			stmt.setString(5, "RLNG");	
			rset=stmt.executeQuery();
			if(rset.next())
			{
				from_contact_nm  = rset.getString(1)==null?"":rset.getString(1);
				from_fax  = rset.getString(2)==null?"":rset.getString(2);
				from_fax2  = rset.getString(3)==null?"":rset.getString(3);
				from_phone = rset.getString(4)==null?"":rset.getString(4);
				from_mobile  = rset.getString(5)==null?"":rset.getString(5);
			}
			rset.close();
			stmt.close();
	
			
			String queryString5 = "SELECT PLANT_ADDR,PLANT_CITY,PLANT_STATE,PLANT_PIN "
					+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
					+ "WHERE COUNTERPARTY_CD=? AND ENTITY='T' AND COMPANY_CD=? AND SEQ_NO=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
			stmt=conn.prepareStatement(queryString5);
			stmt.setString(1, counterparty_cd);	
			stmt.setString(2, comp_cd);	
			stmt.setString(3, plant_seq);	
			stmt.setString(4, report_dt);	
			rset=stmt.executeQuery();
			if(rset.next())
			{
				plantAddress  = rset.getString(1)==null?"":rset.getString(1);
				plantCity  = rset.getString(2)==null?"":rset.getString(2);
				plantState  = rset.getString(3)==null?"":rset.getString(3);
				plantPin  = rset.getString(4)==null?"":rset.getString(4);
			}
			rset.close();
			stmt.close();
	
			
			String queryString6 = "SELECT PLANT_ADDR,PLANT_CITY,PLANT_STATE,PLANT_PIN "
					+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
					+ "WHERE COUNTERPARTY_CD=? AND ENTITY='B' AND COMPANY_CD=? AND SEQ_NO=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
			stmt=conn.prepareStatement(queryString6);
			stmt.setString(1, comp_cd);	
			stmt.setString(2, comp_cd);	
			stmt.setString(3, bu_plant_seq);	
			stmt.setString(4, report_dt);	
			rset=stmt.executeQuery();
			if(rset.next())
			{
				bu_plantAddress  = rset.getString(1)==null?"":rset.getString(1);
				bu_plantCity  = rset.getString(2)==null?"":rset.getString(2);
				bu_plantState  = rset.getString(3)==null?"":rset.getString(3);
				bu_plantPin  = rset.getString(4)==null?"":rset.getString(4);
			}
			rset.close();
			stmt.close();
	
			String sys_dttime = dateUtil.getSysdateWithTime24hr();
			String sys_dt="";
			String sys_time="";
	
			if(!sys_dttime.equals(""))
			{
				String temp[] = sys_dttime.split(" ");
				sys_dt=temp[0];
				sys_time=temp[1];
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getTraderCounterpartyList()
	{
		String function_nm="getTraderCounterpartyList()";
		try
		{
			//utilBean.getEffectiveTraderCounterpartyList(clearance,comp_cd);
			//utilBean.getEffectiveEntityCounterpartyList(conn,clearance,comp_cd,"T");
			utilBean.getAllEntityCounterpartyList(conn,clearance,comp_cd,"T");
			VCOUNTERPARTY_CD= utilBean.getCOUNTERPARTY_CD();
			VCOUNTERPARTY_NM = utilBean.getCOUNTERPARTY_NM();
			VCOUNTERPARTY_ABBR = utilBean.getCOUNTERPARTY_ABBR();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getTraderContractDetail()
	{
		String function_nm="getTraderContractDetail()";
		try
		{
			String queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,CONT_NO,CONT_REV,CONT_REF_NO,TRADE_REF_NO,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),"
					+ "SIGNING_TIME,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_BASE,AGMT_TYPE,TCQ,DCQ,QUANTITY_UNIT,RATE,RATE_UNIT,"
					+ "POST_MARGIN,TRANSPORTATION_CHARGE,SPLIT_FLAG,SPLIT_TYPE,BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,"
					+ "SELLER_NOM_FLAG,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,MDCQ_FLAG,MDCQ_PERCENTAGE,"
					+ "REMARK,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI'),CONT_NAME "
					+ "FROM FMS_TRADER_CONT_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
					+ "AND AGMT_NO=? AND AGMT_REV=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, cont_rev_no);
			stmt.setString(5, contract_type);
			stmt.setString(6, agmt_no);
			stmt.setString(7, agmt_rev_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				//cont_no=rset.getString(3)==null?"":rset.getString(3);
				//cont_rev_no=rset.getString(4)==null?"":rset.getString(4);
				contRef=rset.getString(5)==null?"":rset.getString(5); //JD
				//cont_ref_no=rset.getString(5)==null?"":rset.getString(5);
				//trade_ref_no=rset.getString(6)==null?"":rset.getString(6);
				//signing_dt=rset.getString(7)==null?"":rset.getString(7);
				//signing_time=rset.getString(8)==null?"":rset.getString(8);
				start_dt=rset.getString(9)==null?"":rset.getString(9);
				end_dt=rset.getString(10)==null?"":rset.getString(10);
				//agmt_base=rset.getString(11)==null?"":rset.getString(11);
				//agmt_type=rset.getString(12)==null?"":rset.getString(12);
				//tcq=nf.format(rset.getDouble(13));
				dcq=nf.format(rset.getDouble(14));
				//quantity_unit=rset.getString(15)==null?"1":rset.getString(15);
				//rate_unit=rset.getString(17)==null?"2":rset.getString(17);
				/*if(rate_unit.equals("1"))
				{
					rate=nf.format(rset.getDouble(16));
				}
				else
				{
					rate=nf2.format(rset.getDouble(16));
				}*/
				
				//post_margin=rset.getString(18)==null?"":rset.getString(18);
				//transportation_charges=rset.getString(19)==null?"":rset.getString(19);
				//split_flag=rset.getString(20)==null?"":rset.getString(20);
				//split_type=rset.getString(21)==null?"":rset.getString(21);
				//buy_nom_flag=rset.getString(22)==null?"":rset.getString(22);
				//buy_month_nom=rset.getString(23)==null?"":rset.getString(23);
				//buy_week_nom=rset.getString(24)==null?"":rset.getString(24);
				//buy_daily_nom=rset.getString(25)==null?"":rset.getString(25);
				//sell_nom_flag=rset.getString(26)==null?"":rset.getString(26);
				//sell_month_nom=rset.getString(27)==null?"":rset.getString(27);
				//sell_week_nom=rset.getString(28)==null?"":rset.getString(28);
				//sell_daily_nom=rset.getString(29)==null?"":rset.getString(29);
				//day_def_flag=rset.getString(30)==null?"":rset.getString(30);
				//day_start_time=rset.getString(31)==null?"":rset.getString(31);
				//day_end_time=rset.getString(32)==null?"":rset.getString(32);
				//mdcq_flag=rset.getString(33)==null?"":rset.getString(33);
				mdcq_percentage=rset.getString(34)==null?"":rset.getString(34);
				//remark=rset.getString(35)==null?"":rset.getString(35);
				//String deal_ent_dt=rset.getString(36)==null?"":rset.getString(36);
				cont_name=rset.getString(37)==null?"":rset.getString(37);
				
				//String split[] = deal_ent_dt.split(" ");
				//ent_dt = split[0];
				//ent_time = split[1];
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getTraderCargoDetail()
	{
		String function_nm="getTraderCargoDetail()";
		try
		{
			String queryString="SELECT TO_CHAR(ACTUAL_RECPT_DT,'DD/MM/YYYY'),COALESCE(STORAGE_EXT_DAYS, 0) + COALESCE(STORAGE_DAYS-1, 0),"
					+ "CSOC_QTY,CARGO_REF "
					+ "FROM FMS_LTCORA_CONT_CARGO_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
					+ "AND AGMT_NO=? AND AGMT_REV=? AND CARGO_NO=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, cont_rev_no);
			stmt.setString(5, contract_type);
			stmt.setString(6, agmt_no);
			stmt.setString(7, agmt_rev_no);
			stmt.setString(8, cargo_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				start_dt=rset.getString(1)==null?"":rset.getString(1);
				String eff_days = rset.getString(2)==null?"":rset.getString(2);
				end_dt=""+dateUtil.getDate(start_dt, eff_days);
				dcq=nf.format(rset.getDouble(3));
				cont_name=rset.getString(4)==null?"":rset.getString(4);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSelectedTraderPlantList()
	{
		String function_nm="getSelectedTraderPlantList()";
		try
		{
			if(cont_cargo.equals("isCont"))
			{
				String queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_TRADER_CONT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
						+ "AND AGMT_NO=? AND AGMT_REV=?";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, cont_rev_no);
				stmt.setString(5, contract_type);
				stmt.setString(6, agmt_no);
				stmt.setString(7, agmt_rev_no);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String plant_seq = rset.getString(1)==null?"":rset.getString(1);
					VSEL_PLANT_SEQ_NO.add(plant_seq);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
					VSEL_PLANT_ABBR.add(plant_abbr);
				}
				rset.close();
				stmt.close();
			}
			else if(cont_cargo.equals("isCargo"))
			{
				String queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_LTCORA_CONT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? AND BUY_SALE=? ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, cont_rev_no);
				stmt.setString(5, contract_type);
				stmt.setString(6, agmt_no);
				stmt.setString(7, agmt_rev_no);
				stmt.setString(8, "T");
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String plant_seq = rset.getString(1)==null?"":rset.getString(1);
					VSEL_PLANT_SEQ_NO.add(plant_seq);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
					VSEL_PLANT_ABBR.add(plant_abbr);
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
	
	public void getSelectedBusinessPlantList()
	{
		String function_nm="getSelectedBusinessPlantList()";
		try
		{
			if(cont_cargo.equals("isCont"))
			{
				String queryString="SELECT COMPANY_CD,PLANT_SEQ_NO "
						+ "FROM FMS_TRADER_CONT_BU "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
						+ "AND AGMT_NO=? AND AGMT_REV=?";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, cont_rev_no);
				stmt.setString(5, contract_type);
				stmt.setString(6, agmt_no);
				stmt.setString(7, agmt_rev_no);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String buCd = rset.getString(1)==null?"":rset.getString(1);
					String plant_seq = rset.getString(2)==null?"":rset.getString(2);
					VSEL_BU_CD.add(buCd);
					VSEL_BU_PLANT_SEQ_NO.add(plant_seq);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,buCd, comp_cd, plant_seq, "B");
					VSEL_BU_PLANT_ABBR.add(plant_abbr);
				}
				rset.close();
				stmt.close();
			}
			else if(cont_cargo.equals("isCargo"))
			{
				String queryString="SELECT COMPANY_CD,PLANT_SEQ_NO "
						+ "FROM FMS_LTCORA_CONT_BU "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? AND BUY_SALE=? ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, cont_rev_no);
				stmt.setString(5, contract_type);
				stmt.setString(6, agmt_no);
				stmt.setString(7, agmt_rev_no);
				stmt.setString(8, "T");
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String buCd = rset.getString(1)==null?"":rset.getString(1);
					String plant_seq = rset.getString(2)==null?"":rset.getString(2);
					VSEL_BU_CD.add(buCd);
					VSEL_BU_PLANT_SEQ_NO.add(plant_seq);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,buCd, comp_cd, plant_seq, "B");
					VSEL_BU_PLANT_ABBR.add(plant_abbr);
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
	
	public void getBuyerNomination()
	{
		String function_nm="getBuyerNomination()";
		try
		{
			dateTime = dateUtil.getSysdateWithTime24hr();
			
			String queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
					+ "CONT_REF_NO,DCQ,CONT_NAME,MDCQ_PERCENTAGE,TRADE_REF_NO,TO_CHAR(START_DT,'DD/MM/YYYY') "
					+ "FROM FMS_TRADER_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND BUYER_NOM_FLAG=? AND FCC_FLAG=? "
					+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "Y");
			stmt.setString(3, "Y");
			stmt.setString(4, gas_dt);
			stmt.setString(5, gas_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String companyCd = rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd = rset.getString(2)==null?"":rset.getString(2);
				String agmt = rset.getString(3)==null?"":rset.getString(3);
				String agmt_rev = rset.getString(4)==null?"":rset.getString(4);
				String cont = rset.getString(5)==null?"":rset.getString(5);
				String cont_rev = rset.getString(6)==null?"":rset.getString(6);
				String cont_type = rset.getString(7)==null?"":rset.getString(7);
				String cont_ref = rset.getString(8)==null?"":rset.getString(8);
				String dcq = nf.format(rset.getDouble(9));
				
				String variable_dcq_qty=utilBean.getPurchaseContVariableDCQ(conn,companyCd,countpty_cd, agmt, cont, cont_type, gas_dt);
				
				String cpStatus = (String) utilBean.getCounterpartyDetails(conn, countpty_cd, gas_dt).get("status");
				
				if(!variable_dcq_qty.equals(""))
				{
					dcq=variable_dcq_qty;
				}
				
				String cont_name = rset.getString(10)==null?"":rset.getString(10);
				String mdcq_percentage = nf.format(rset.getDouble(11));
				double mdcq_qty = (Double.parseDouble(dcq)*Double.parseDouble(mdcq_percentage))/100;
				
				String trade_ref = rset.getString(12)==null?"":rset.getString(12);
				if(cont_type.equals("I")) {
					cont_ref=trade_ref;
				}
				
				String internal_map_id = cont_type+"-"+countpty_cd+"-"+agmt+"-"+agmt_rev+"-"+cont+"-"+cont_rev;
				
				String cont_start_dt = rset.getString(13)==null?"":rset.getString(13);
				
				//GET SELECTED COUNTERPARTY PLANT
				String queryString1="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_TRADER_CONT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
						+ "AND AGMT_NO=? AND AGMT_REV=?";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, companyCd);
				stmt1.setString(2, countpty_cd);
				stmt1.setString(3, cont);
				stmt1.setString(4, cont_rev);
				stmt1.setString(5, cont_type);
				stmt1.setString(6, agmt);
				stmt1.setString(7, agmt_rev);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String plant_seq = rset1.getString(1)==null?"":rset1.getString(1);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, comp_cd, plant_seq, "T");
					
					//GET SELECTED BU PLANT
					String queryString2="SELECT COMPANY_CD,PLANT_SEQ_NO "
							+ "FROM FMS_TRADER_CONT_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=?";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, companyCd);
					stmt2.setString(2, countpty_cd);
					stmt2.setString(3, cont);
					stmt2.setString(4, cont_rev);
					stmt2.setString(5, cont_type);
					stmt2.setString(6, agmt);
					stmt2.setString(7, agmt_rev);
					rset2=stmt2.executeQuery();
					while(rset2.next())
					{
						String transporter_cd = "0"; //NOT IN USED 10/11/2022
						String transporter_plant_seq = "0"; //NOT IN USED 10/11/2022
						String buCd = rset2.getString(1)==null?"":rset2.getString(1);
						String bu_plant_seq = rset2.getString(2)==null?"":rset2.getString(2);
						String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,buCd, comp_cd, bu_plant_seq, "B");
						
						VCOUNTERPARTY_CD.add(countpty_cd);
						VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
						VAGMT_NO.add(agmt);
						VAGMT_REV_NO.add(agmt_rev);
						VCONT_NO.add(cont);
						VCONT_REV_NO.add(cont_rev);
						VCONTRACT_TYPE.add(cont_type);
						VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
						VCARGO_NO.add("");
						VCOUNTERPATY_STATUS.add(cpStatus);
						
						VDIS_CONT_MAPPING.add(utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, ""));
						/*
						if(cont_type.equals("S"))
						{
							VDIS_CONT_MAPPING.add(comp_cd+cont_type+countpty_cd+"-"+agmt+"-"+cont);
						}
						else
						{
							VDIS_CONT_MAPPING.add(comp_cd+cont_type+countpty_cd+"-0-"+cont);
						}*/
						VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
						VCOUNTERPARTY_PLANT_ABBR.add(plant_abbr);
						VBU_CD.add(buCd);
						VBU_PLANT_SEQ.add(bu_plant_seq);
						VBU_PLANT_ABBR.add(bu_plant_abbr);
						VDCQ.add(dcq);
						VCONT_REF.add(cont_ref);
						VCONT_NAME.add(cont_name);
						VMDCQ_QTY.add(nf.format(mdcq_qty));
						VINTERNAL_MAP_ID.add(internal_map_id);
						
						String billingFrq="";
						String queryString4="SELECT BILLING_FREQ "
								+ "FROM FMS_TRADER_BILLING_DTL "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONT_NO=? AND CONT_REV=? "
								+ "AND CONTRACT_TYPE=?";
						stmt4=conn.prepareStatement(queryString4);
						stmt4.setString(1, companyCd);
						stmt4.setString(2, countpty_cd);
						stmt4.setString(3, agmt);
						stmt4.setString(4, agmt_rev);
						stmt4.setString(5, cont);
						stmt4.setString(6, cont_rev);
						stmt4.setString(7, cont_type);
						rset4=stmt4.executeQuery();
				  		if(rset4.next())
				  		{
				  			billingFrq=rset4.getString(1)==null?"":rset4.getString(1);
				  		}
				  		rset4.close();
				  		stmt4.close();
				  		
				  		String periodStDt=utilBean.getFirstDtOfBillingCycle(billingFrq, "", gas_dt);
						
						VTAX_DTL.add(utilBean.getEntityTaxStructureDtl(conn,companyCd, countpty_cd, "T", plant_seq, bu_plant_seq, periodStDt, "S"));
						
						int isInvoiceSubmitted=isInvoiceSubmitted(countpty_cd, cont, agmt, plant_seq, cont_type, bu_plant_seq, gas_dt,false);
						if(isInvoiceSubmitted>0)
						{
							VNOM_BLOCK.add("Y");
						}
						else
						{
							VNOM_BLOCK.add("N");
						}
						
						//GET BUYER NOMINATION DATA FOR SELECTED CONTRACT
						String queryString3="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY') "
				  				+ "FROM FMS_BUY_DAILY_BUYER_NOM A "
				  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
								+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
								+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_BUYER_NOM B "
								+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
								+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
								+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
								+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
								+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
						stmt3=conn.prepareStatement(queryString3);
						stmt3.setString(1, cont);
						stmt3.setString(2, agmt);
						stmt3.setString(3, companyCd);
						stmt3.setString(4, countpty_cd);
						stmt3.setString(5, transporter_cd);
						stmt3.setString(6, transporter_plant_seq);
						stmt3.setString(7, bu_plant_seq);
						stmt3.setString(8, plant_seq);
						stmt3.setString(9, cont_type);
						stmt3.setString(10, gas_dt);
						rset3=stmt3.executeQuery();
				  		if(rset3.next())
				  		{
				  			VNOM_REV_NO.add(rset3.getString(1)==null?"":rset3.getString(1));
				  			VGEN_TIME.add(rset3.getString(2)==null?"":rset3.getString(2));
				  			VBASE.add(rset3.getString(3)==null?"":rset3.getString(3));
				  			VGCV.add(rset3.getString(4)==null?"9802.80":rset3.getString(4));
				  			VNCV.add(rset3.getString(5)==null?"8831.35":rset3.getString(5));
				  			VQTY_MMBTU.add(rset3.getString(6)==null?"":rset3.getString(6));
				  			VQTY_SCM.add(rset3.getString(7)==null?"":rset3.getString(7));
				  			VGEN_DT.add(rset3.getString(8)==null?"":rset3.getString(8));
				  			
				  			VNOM_COLOR.add("#99ffcc");	
				  		}
				  		else
				  		{
				  			VNOM_REV_NO.add("");
				  			
				  			String split[] = dateTime.split(" ");
				  			VGEN_DT.add(split[0]);
				  			VGEN_TIME.add(split[1]);
				  			VBASE.add("GCV");
				  			VGCV.add("9802.80");
				  			VNCV.add("8831.35");
				  			VQTY_MMBTU.add("");
				  			VQTY_SCM.add("");
				  			VNOM_COLOR.add("");
				  		}
				  		rset3.close();
				  		stmt3.close();
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
			}
			rset.close();
			stmt.close();
			
			//Cargo Leveled Buyer Nomination Details
			
			String queryString1="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CARGO_NO,CARGO_REF,CSOC_QTY "
					+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
					+ "WHERE COMPANY_CD=? AND BOE_NO IS NOT NULL "
					+ "AND ACTUAL_RECPT_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND (COALESCE(STORAGE_EXT_DAYS, 0) + COALESCE(STORAGE_DAYS-1, 0)) >= (SELECT ROUND( (TO_DATE(?,'DD/MM/YYYY') - ACTUAL_RECPT_DT), 0) FROM DUAL) "
					+ "AND BUY_SALE=? AND CARGO_STATUS=? "
					+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt=conn.prepareStatement(queryString1);
			stmt.setString(1, comp_cd);
			stmt.setString(2, gas_dt);
			stmt.setString(3, gas_dt);
			stmt.setString(4, "T");//ONLY FOR BUY SIDE(T)
			stmt.setString(5, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String companyCd = rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd = rset.getString(2)==null?"":rset.getString(2);
				String agmt = rset.getString(3)==null?"":rset.getString(3);
				String agmt_rev = rset.getString(4)==null?"":rset.getString(4);
				String cont = rset.getString(5)==null?"":rset.getString(5);
				String cont_rev = rset.getString(6)==null?"":rset.getString(6);
				String cont_type = rset.getString(7)==null?"":rset.getString(7);
				String cargo_no = rset.getString(8)==null?"":rset.getString(8);
				String cargo_ref_no = rset.getString(9)==null?"":rset.getString(9);
				
				String csoc_mmbtu = rset.getString(10)==null?"":rset.getString(10);
				String variable_csoc_mmbtu=utilBean.getCargoVariableCSOC(conn,companyCd, countpty_cd, "T",agmt, cont, cont_type, cargo_no,gas_dt);
				
				if(!variable_csoc_mmbtu.equals(""))
				{
					csoc_mmbtu=variable_csoc_mmbtu;
				}
				String cpStatus = (String) utilBean.getCounterpartyDetails(conn, countpty_cd, gas_dt).get("status");
				
				String internal_map_id = cont_type+"-"+countpty_cd+"-"+agmt+"-"+agmt_rev+"-"+cont+"-"+cont_rev+"-"+cargo_no;;
				//GET SELECTED COUNTERPARTY PLANT
				String queryString2="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_LTCORA_CONT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
						+ "AND AGMT_NO=? AND AGMT_REV=?";
				stmt1=conn.prepareStatement(queryString2);
				stmt1.setString(1, companyCd);
				stmt1.setString(2, countpty_cd);
				stmt1.setString(3, cont);
				stmt1.setString(4, cont_rev);
				stmt1.setString(5, cont_type);
				stmt1.setString(6, agmt);
				stmt1.setString(7, agmt_rev);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String plant_seq = rset1.getString(1)==null?"":rset1.getString(1);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, comp_cd, plant_seq, "T");
					
					//GET SELECTED BU PLANT
					String queryString3="SELECT COMPANY_CD,PLANT_SEQ_NO "
							+ "FROM FMS_LTCORA_CONT_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=?";
					stmt2=conn.prepareStatement(queryString3);
					stmt2.setString(1, companyCd);
					stmt2.setString(2, countpty_cd);
					stmt2.setString(3, cont);
					stmt2.setString(4, cont_rev);
					stmt2.setString(5, cont_type);
					stmt2.setString(6, agmt);
					stmt2.setString(7, agmt_rev);
					rset2=stmt2.executeQuery();
					while(rset2.next())
					{
						String transporter_cd = "0"; //NOT IN USED 10/11/2022
						String transporter_plant_seq = "0"; //NOT IN USED 10/11/2022
						String buCd = rset2.getString(1)==null?"":rset2.getString(1);
						String bu_plant_seq = rset2.getString(2)==null?"":rset2.getString(2);
						String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,buCd, comp_cd, bu_plant_seq, "B");

						VCOUNTERPARTY_CD.add(countpty_cd);
						VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
						VAGMT_NO.add(agmt);
						VAGMT_REV_NO.add(agmt_rev);
						VCONT_NO.add(cont);
						VCONT_REV_NO.add(cont_rev);
						VCONTRACT_TYPE.add(cont_type);
						VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
						VCARGO_NO.add(cargo_no);
						VDIS_CONT_MAPPING.add(cargo_ref_no);

						VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
						VCOUNTERPARTY_PLANT_ABBR.add(plant_abbr);
						VBU_CD.add(buCd);
						VBU_PLANT_SEQ.add(bu_plant_seq);
						VBU_PLANT_ABBR.add(bu_plant_abbr);
						VDCQ.add(csoc_mmbtu);
						VCONT_NAME.add(cont_name);
						VINTERNAL_MAP_ID.add(internal_map_id);
						VCOUNTERPATY_STATUS.add(cpStatus);
						
						String queryString5 = "SELECT CONT_REF_NO,MDCQ_PERCENTAGE "
								+ "FROM FMS_LTCORA_CONT_MST A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
								+ "AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? "
								+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
						stmt5=conn.prepareStatement(queryString5);
						stmt5.setString(1, companyCd);
						stmt5.setString(2, countpty_cd);
						stmt5.setString(3, agmt);
						stmt5.setString(4, agmt_rev);
						stmt5.setString(5, cont_type);
						stmt5.setString(6, cont);
						rset5=stmt5.executeQuery();
				  		if(rset5.next())
				  		{
				  			VCONT_REF.add(rset5.getString(1)==null?"":rset5.getString(1));
				  			
				  			String mcsoc_per = rset5.getString(2)==null?"":rset5.getString(2);
				  			
				  			double mcsoc_qty = (Double.parseDouble(csoc_mmbtu)*Integer.parseInt(mcsoc_per))/100;
							VMDCQ_QTY.add(nf.format(mcsoc_qty));
				  		}
				  		else 
				  		{
				  			VCONT_REF.add("");
				  			VMDCQ_QTY.add("");
				  		}
				  		rset5.close();
				  		stmt5.close();
				  		
						String billingFrq="";
						
						String queryString4="SELECT BILLING_FREQ "
								+ "FROM FMS_LTCORA_CONT_BILLING_DTL "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONT_NO=? AND CONT_REV=? "
								+ "AND CONTRACT_TYPE=?";
						stmt4=conn.prepareStatement(queryString4);
						stmt4.setString(1, companyCd);
						stmt4.setString(2, countpty_cd);
						stmt4.setString(3, agmt);
						stmt4.setString(4, agmt_rev);
						stmt4.setString(5, cont);
						stmt4.setString(6, cont_rev);
						stmt4.setString(7, cont_type);
						rset4=stmt4.executeQuery();
				  		if(rset4.next())
				  		{
				  			billingFrq=rset4.getString(1)==null?"":rset4.getString(1);
				  		}
				  		rset4.close();
				  		stmt4.close();
				  		
				  		String periodStDt=utilBean.getFirstDtOfBillingCycle(billingFrq, "", gas_dt);
						
				  		String queryString_temp="SELECT TAX_STRUCT_DTL "
								+ "FROM FMS_ENTITY_SERVICE_TAX_DTL A "
								+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND PLANT_SEQ_NO=? AND BU_UNIT=? "
								+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_SERVICE_TAX_DTL B "
								+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.BU_UNIT=B.BU_UNIT AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
						stmt_temp=conn.prepareStatement(queryString_temp);
						stmt_temp.setString(1, "T");
						stmt_temp.setString(2, companyCd);
						stmt_temp.setString(3, countpty_cd);
						stmt_temp.setString(4, plant_seq);
						stmt_temp.setString(5, bu_plant_seq);
						stmt_temp.setString(6, periodStDt);
						rset_temp=stmt_temp.executeQuery();
						if(rset_temp.next())
						{
							VTAX_DTL.add(rset_temp.getString(1)==null?"":rset_temp.getString(1));
						}
						else 
						{
							VTAX_DTL.add("");
						}
						rset_temp.close();
						stmt_temp.close();
						
						int isInvoiceSubmitted=isInvoiceSubmitted_Cargo(countpty_cd, cont, agmt, plant_seq, cont_type, bu_plant_seq, gas_dt, cargo_no,false);
						if(isInvoiceSubmitted>0)
						{
							VNOM_BLOCK.add("Y");
						}
						else
						{
							VNOM_BLOCK.add("N");
						}
						
						//GET BUYER NOMINATION DATA FOR SELECTED CONTRACT
						String queryString6="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY') "
				  				+ "FROM FMS_BUY_DAILY_BUYER_NOM A "
				  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
								+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
								+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
								+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_BUYER_NOM B "
								+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
								+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
								+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
								+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
								+ "AND B.GAS_DT=A.GAS_DT AND A.CARGO_NO=B.CARGO_NO) ";
						stmt3=conn.prepareStatement(queryString6);
						stmt3.setString(1, cont);
						stmt3.setString(2, agmt);
						stmt3.setString(3, companyCd);
						stmt3.setString(4, countpty_cd);
						stmt3.setString(5, transporter_cd);
						stmt3.setString(6, transporter_plant_seq);
						stmt3.setString(7, bu_plant_seq);
						stmt3.setString(8, plant_seq);
						stmt3.setString(9, cont_type);
						stmt3.setString(10, gas_dt);
						stmt3.setString(11, cargo_no);
						rset3=stmt3.executeQuery();
				  		if(rset3.next())
				  		{
				  			VNOM_REV_NO.add(rset3.getString(1)==null?"":rset3.getString(1));
				  			VGEN_TIME.add(rset3.getString(2)==null?"":rset3.getString(2));
				  			VBASE.add(rset3.getString(3)==null?"":rset3.getString(3));
				  			VGCV.add(rset3.getString(4)==null?"9802.80":rset3.getString(4));
				  			VNCV.add(rset3.getString(5)==null?"8831.35":rset3.getString(5));
				  			VQTY_MMBTU.add(rset3.getString(6)==null?"":rset3.getString(6));
				  			VQTY_SCM.add(rset3.getString(7)==null?"":rset3.getString(7));
				  			VGEN_DT.add(rset3.getString(8)==null?"":rset3.getString(8));
				  			
				  			VNOM_COLOR.add("#99ffcc");	
				  		}
				  		else
				  		{
				  			VNOM_REV_NO.add("");
				  			
				  			String split[] = dateTime.split(" ");
				  			VGEN_DT.add(split[0]);
				  			VGEN_TIME.add(split[1]);
				  			VBASE.add("GCV");
				  			VGCV.add("9802.80");
				  			VNCV.add("8831.35");
				  			VQTY_MMBTU.add("");
				  			VQTY_SCM.add("");
				  			VNOM_COLOR.add("");
				  		}
				  		rset3.close();
				  		stmt3.close();
					}
					rset2.close();
					stmt2.close();
				}
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
	
	public int isInvoiceSubmitted(String countpty_cd,String cont, String agmt,String plant_seq, String cont_type, String bu_plant_seq, String gas_dt, boolean isAllocation)
	{
		String function_nm="isInvoiceSubmitted()";
		int count=0;
		try
		{
			//02/06/2023 - THIS PART IS NOT DISCUSSED WITH MADAM/CUSTOMER(CLIENT) BEFOR DEVELOPING
			//THE QUESTION IS - WHICH INVOICE SHOULD BE CONSIDERED FOR NOMINATION/ALLOCATION BLOCKING?
			String queryString="SELECT COUNT(*) "
					+ "FROM FMS_PUR_SG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND AGMT_NO=? "
					+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_UNIT=? "
					+ "AND PERIOD_START_DT<=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND INV_FLAG = 'F' ";//CHECK ONLY FOR 'F' INVOICE FLAG
			stmt_temp=conn.prepareStatement(queryString);
			stmt_temp.setString(1, comp_cd);
			stmt_temp.setString(2, countpty_cd);
			stmt_temp.setString(3, cont);
			stmt_temp.setString(4, agmt);
			stmt_temp.setString(5, plant_seq);
			stmt_temp.setString(6, cont_type);
			stmt_temp.setString(7, bu_plant_seq);
			stmt_temp.setString(8, gas_dt);
			stmt_temp.setString(9, gas_dt);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				count=rset_temp.getInt(1);
			}
			rset_temp.close();
			stmt_temp.close();
			
			if(isAllocation)
			{
				int cnt=0;
				queryString="SELECT SUM(CNT) "
						+ "FROM (SELECT COUNT(*) CNT "
						+ "FROM FMS_PUR_SG_INV_MST "
						+ "WHERE COMPANY_CD=? "//AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND AGMT_NO=? "
						+ "AND CONTRACT_TYPE=? AND BU_UNIT=? " //AND PLANT_SEQ=? 
						+ "AND PERIOD_START_DT<=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT>=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND INV_FLAG IN ('CR','DR') AND CRITERIA LIKE ? "
						+ "AND (CHECKED_FLAG != ? OR CHECKED_FLAG IS NULL) "
						+ "UNION ALL "
						+ "SELECT COUNT(*) CNT "
						+ "FROM FMS_PUR_PG_INV_MST "
						+ "WHERE COMPANY_CD=? "//AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND AGMT_NO=? "
						+ "AND CONTRACT_TYPE=? AND BU_UNIT=? " //AND PLANT_SEQ=? 
						+ "AND PERIOD_START_DT<=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT>=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND INV_FLAG IN ('CR','DR') AND CRITERIA LIKE ? "
						+ "AND (CHECKED_FLAG != ? OR CHECKED_FLAG IS NULL)) ";
				stmt_temp = conn.prepareStatement(queryString);
				stmt_temp.setString(++cnt, comp_cd);
				//stmt_temp.setString(2, countpty_cd);
				stmt_temp.setString(++cnt, cont);
				stmt_temp.setString(++cnt, agmt);
				//stmt_temp.setString(5, plant_seq);
				stmt_temp.setString(++cnt, cont_type);
				stmt_temp.setString(++cnt, bu_plant_seq);
				stmt_temp.setString(++cnt, gas_dt);
				stmt_temp.setString(++cnt, gas_dt);
				stmt_temp.setString(++cnt, "%QTY%");
				stmt_temp.setString(++cnt, "Y");
				stmt_temp.setString(++cnt, comp_cd);
				//stmt_temp.setString(13, countpty_cd);
				stmt_temp.setString(++cnt, cont);
				stmt_temp.setString(++cnt, agmt);
				//stmt_temp.setString(16, plant_seq);
				stmt_temp.setString(++cnt, cont_type);
				stmt_temp.setString(++cnt, bu_plant_seq);
				stmt_temp.setString(++cnt, gas_dt);
				stmt_temp.setString(++cnt, gas_dt);
				stmt_temp.setString(++cnt, "%QTY%");
				stmt_temp.setString(++cnt, "Y");
				rset_temp=stmt_temp.executeQuery();
				if(rset_temp.next())
				{
					int temp_count=rset_temp.getInt(1);
					
					if(temp_count > 0)
					{
						count=0;
					}
				}
				rset_temp.close();
				stmt_temp.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return count;
	}
	
	public int isInvoiceSubmitted_Cargo(String countpty_cd,String cont, String agmt,String plant_seq, String cont_type, String bu_plant_seq, String gas_dt, String cargo_no, boolean isAllocation)
	{
		String function_nm="isInvoiceSubmitted_Cargo()";
		int count=0;
		try
		{
			//02/06/2023 - THIS PART IS NOT DISCUSSED WITH MADAM/CUSTOMER(CLIENT) BEFOR DEVELOPING
			//THE QUESTION IS - WHICH INVOICE SHOULD BE CONSIDERED FOR NOMINATION/ALLOCATION BLOCKING?
			String queryString="SELECT COUNT(*) "
					+ "FROM FMS_PUR_SG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND AGMT_NO=? "
					+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_UNIT=? "
					+ "AND PERIOD_START_DT<=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND CARGO_NO=? "
					+ "AND INV_FLAG = 'F' ";//CHECK ONLY FOR 'F' INVOICE FLAG
			stmt_temp=conn.prepareStatement(queryString);
			stmt_temp.setString(1, comp_cd);
			stmt_temp.setString(2, countpty_cd);
			stmt_temp.setString(3, cont);
			stmt_temp.setString(4, agmt);
			stmt_temp.setString(5, plant_seq);
			stmt_temp.setString(6, cont_type);
			stmt_temp.setString(7, bu_plant_seq);
			stmt_temp.setString(8, gas_dt);
			stmt_temp.setString(9, gas_dt);
			stmt_temp.setString(10, cargo_no);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				count=rset_temp.getInt(1);
			}
			rset_temp.close();
			stmt_temp.close();
			
			if(isAllocation)
			{
				int cnt=0;
				queryString="SELECT SUM(CNT) "
						+ "FROM (SELECT COUNT(*) CNT "
						+ "FROM FMS_PUR_SG_INV_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND AGMT_NO=? "
						+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_UNIT=? "
						+ "AND PERIOD_START_DT<=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT>=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND INV_FLAG IN ('CR','DR') AND CRITERIA LIKE ? "
						+ "AND (CHECKED_FLAG != ? OR CHECKED_FLAG IS NULL) AND CARGO_NO=? "
						+ "UNION ALL "
						+ "SELECT COUNT(*) CNT "
						+ "FROM FMS_PUR_PG_INV_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND AGMT_NO=? "
						+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_UNIT=? "
						+ "AND PERIOD_START_DT<=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT>=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND INV_FLAG IN ('CR','DR') AND CRITERIA LIKE ? "
						+ "AND (CHECKED_FLAG != ? OR CHECKED_FLAG IS NULL) AND CARGO_NO=?) ";
				stmt_temp = conn.prepareStatement(queryString);
				stmt_temp.setString(++cnt, comp_cd);
				stmt_temp.setString(++cnt, countpty_cd);
				stmt_temp.setString(++cnt, cont);
				stmt_temp.setString(++cnt, agmt);
				stmt_temp.setString(++cnt, plant_seq);
				stmt_temp.setString(++cnt, cont_type);
				stmt_temp.setString(++cnt, bu_plant_seq);
				stmt_temp.setString(++cnt, gas_dt);
				stmt_temp.setString(++cnt, gas_dt);
				stmt_temp.setString(++cnt, "%QTY%");
				stmt_temp.setString(++cnt, "Y");
				stmt_temp.setString(++cnt, cargo_no);
				stmt_temp.setString(++cnt, comp_cd);
				stmt_temp.setString(++cnt, countpty_cd);
				stmt_temp.setString(++cnt, cont);
				stmt_temp.setString(++cnt, agmt);
				stmt_temp.setString(++cnt, plant_seq);
				stmt_temp.setString(++cnt, cont_type);
				stmt_temp.setString(++cnt, bu_plant_seq);
				stmt_temp.setString(++cnt, gas_dt);
				stmt_temp.setString(++cnt, gas_dt);
				stmt_temp.setString(++cnt, "%QTY%");
				stmt_temp.setString(++cnt, "Y");
				stmt_temp.setString(++cnt, cargo_no);
				rset_temp=stmt_temp.executeQuery();
				if(rset_temp.next())
				{
					int temp_count=rset_temp.getInt(1);
					
					if(temp_count > 0)
					{
						count=0;
					}
				}
				rset_temp.close();
				stmt_temp.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return count;
	}
	
	public void getSellerNomination()
	{
		String function_nm="getSellerNomination()";
		try
		{
			dateTime = dateUtil.getSysdateWithTime24hr();
			
			String queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
					+ "CONT_REF_NO,DCQ,CONT_NAME,MDCQ_PERCENTAGE,TRADE_REF_NO,TO_CHAR(START_DT,'DD/MM/YYYY') "
					+ "FROM FMS_TRADER_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND SELLER_NOM_FLAG=? AND FCC_FLAG=? "
					+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "Y");
			stmt.setString(3, "Y");
			stmt.setString(4, gas_dt);
			stmt.setString(5, gas_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String companyCd = rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd = rset.getString(2)==null?"":rset.getString(2);
				String agmt = rset.getString(3)==null?"":rset.getString(3);
				String agmt_rev = rset.getString(4)==null?"":rset.getString(4);
				String cont = rset.getString(5)==null?"":rset.getString(5);
				String cont_rev = rset.getString(6)==null?"":rset.getString(6);
				String cont_type = rset.getString(7)==null?"":rset.getString(7);
				String cont_ref = rset.getString(8)==null?"":rset.getString(8);
				String dcq = nf.format(rset.getDouble(9));
				
				String variable_dcq_qty=utilBean.getPurchaseContVariableDCQ(conn,companyCd,countpty_cd, agmt, cont, cont_type, gas_dt);
				
				if(!variable_dcq_qty.equals(""))
				{
					dcq=variable_dcq_qty;
				}
				
				String cont_name = rset.getString(10)==null?"":rset.getString(10);
				String mdcq_percentage = nf.format(rset.getDouble(11));
				double mdcq_qty = (Double.parseDouble(dcq)*Double.parseDouble(mdcq_percentage))/100;
				
				String trade_ref = rset.getString(12)==null?"":rset.getString(12);
				if(cont_type.equals("I")) {
					cont_ref=trade_ref;
				}
				
				String internal_map_id = cont_type+"-"+countpty_cd+"-"+agmt+"-"+agmt_rev+"-"+cont+"-"+cont_rev;
				
				String cont_start_dt=rset.getString(13)==null?"":rset.getString(13);
				String cpStatus = (String) utilBean.getCounterpartyDetails(conn, countpty_cd, gas_dt).get("status");
				
				//GET SELECTED COUNTERPARTY PLANT
				String queryString1="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_TRADER_CONT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
						+ "AND AGMT_NO=? AND AGMT_REV=?";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, companyCd);
				stmt1.setString(2, countpty_cd);
				stmt1.setString(3, cont);
				stmt1.setString(4, cont_rev);
				stmt1.setString(5, cont_type);
				stmt1.setString(6, agmt);
				stmt1.setString(7, agmt_rev);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String plant_seq = rset1.getString(1)==null?"":rset1.getString(1);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, comp_cd, plant_seq, "T");
					
					//GET SELECTED BU PLANT
					String queryString2="SELECT COMPANY_CD,PLANT_SEQ_NO "
							+ "FROM FMS_TRADER_CONT_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=?";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, companyCd);
					stmt2.setString(2, countpty_cd);
					stmt2.setString(3, cont);
					stmt2.setString(4, cont_rev);
					stmt2.setString(5, cont_type);
					stmt2.setString(6, agmt);
					stmt2.setString(7, agmt_rev);
					rset2=stmt2.executeQuery();
					while(rset2.next())
					{
						String transporter_cd = "0"; //NOT IN USED 10/11/2022
						String transporter_plant_seq = "0"; //NOT IN USED 10/11/2022
						String buCd = rset2.getString(1)==null?"":rset2.getString(1);
						String bu_plant_seq = rset2.getString(2)==null?"":rset2.getString(2);
						String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,buCd, comp_cd, bu_plant_seq, "B");
						
						String billingFrq="";
						String queryString3="SELECT BILLING_FREQ "
								+ "FROM FMS_TRADER_BILLING_DTL "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONT_NO=? AND CONT_REV=? "
								+ "AND CONTRACT_TYPE=?";
						stmt3=conn.prepareStatement(queryString3);
						stmt3.setString(1, companyCd);
						stmt3.setString(2, countpty_cd);
						stmt3.setString(3, agmt);
						stmt3.setString(4, agmt_rev);
						stmt3.setString(5, cont);
						stmt3.setString(6, cont_rev);
						stmt3.setString(7, cont_type);
						rset3=stmt3.executeQuery();
				  		if(rset3.next())
				  		{
				  			billingFrq=rset3.getString(1)==null?"":rset3.getString(1);
				  		}
				  		rset3.close();
				  		stmt3.close();
				  		
				  		String periodStDt=utilBean.getFirstDtOfBillingCycle(billingFrq, "", gas_dt);
						
						//GET SELLER NOMINATION DATA FOR SELECTED CONTRACT
				  		String queryString5="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY') "
				  				+ "FROM FMS_BUY_DAILY_SELLER_NOM A "
				  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
								+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
								+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_SELLER_NOM B "
								+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
								+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
								+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
								+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
								+ "AND B.GAS_DT=A.GAS_DT  AND B.CARGO_NO=A.CARGO_NO) ";
						stmt5=conn.prepareStatement(queryString5);
						stmt5.setString(1, cont);
						stmt5.setString(2, agmt);
						stmt5.setString(3, companyCd);
						stmt5.setString(4, countpty_cd);
						stmt5.setString(5, transporter_cd);
						stmt5.setString(6, transporter_plant_seq);
						stmt5.setString(7, bu_plant_seq);
						stmt5.setString(8, plant_seq);
						stmt5.setString(9, cont_type);
						stmt5.setString(10, gas_dt);
						rset5=stmt5.executeQuery();
				  		if(rset5.next())
				  		{
				  			VCOUNTERPARTY_CD.add(countpty_cd);
							VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
							VAGMT_NO.add(agmt);
							VAGMT_REV_NO.add(agmt_rev);
							VCONT_NO.add(cont);
							VCONT_REV_NO.add(cont_rev);
							VCONTRACT_TYPE.add(cont_type);
							VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
							VCARGO_NO.add("");
							VDIS_CONT_MAPPING.add(utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, ""));
							/*
							if(cont_type.equals("S"))
							{
								VDIS_CONT_MAPPING.add(comp_cd+cont_type+countpty_cd+"-"+agmt+"-"+cont);
							}
							else
							{
								VDIS_CONT_MAPPING.add(comp_cd+cont_type+countpty_cd+"-0-"+cont);
							}*/
							VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
							VCOUNTERPARTY_PLANT_ABBR.add(plant_abbr);
							VBU_CD.add(buCd);
							VBU_PLANT_SEQ.add(bu_plant_seq);
							VBU_PLANT_ABBR.add(bu_plant_abbr);
							VDCQ.add(dcq);
							VCONT_REF.add(cont_ref);
							VCONT_NAME.add(cont_name);
							VMDCQ_QTY.add(nf.format(mdcq_qty));
							VINTERNAL_MAP_ID.add(internal_map_id);
							VTAX_DTL.add(utilBean.getEntityTaxStructureDtl(conn,companyCd, countpty_cd, "T", plant_seq, bu_plant_seq, periodStDt, "S"));
							
				  			VNOM_REV_NO.add(rset5.getString(1)==null?"":rset5.getString(1));
				  			VGEN_TIME.add(rset5.getString(2)==null?"":rset5.getString(2));
				  			VBASE.add(rset5.getString(3)==null?"":rset5.getString(3));
				  			VGCV.add(rset5.getString(4)==null?"9802.80":rset5.getString(4));
				  			VNCV.add(rset5.getString(5)==null?"8831.35":rset5.getString(5));
				  			VQTY_MMBTU.add(rset5.getString(6)==null?"":rset5.getString(6));
				  			VQTY_SCM.add(rset5.getString(7)==null?"":rset5.getString(7));
				  			VGEN_DT.add(rset5.getString(8)==null?"":rset5.getString(8));
				  			
				  			VNOM_COLOR.add("#99ffcc");
				  			VCOUNTERPATY_STATUS.add(cpStatus);
				  			
				  			int isInvoiceSubmitted=isInvoiceSubmitted(countpty_cd, cont, agmt, plant_seq, cont_type, bu_plant_seq, gas_dt,false);
							if(isInvoiceSubmitted>0)
							{
								VNOM_BLOCK.add("Y");
							}
							else
							{
								VNOM_BLOCK.add("N");
							}
				  			
							String queryString4="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM "
					  				+ "FROM FMS_BUY_DAILY_BUYER_NOM A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_BUYER_NOM B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				  			stmt4=conn.prepareStatement(queryString4);
							stmt4.setString(1, cont);
							stmt4.setString(2, agmt);
							stmt4.setString(3, companyCd);
							stmt4.setString(4, countpty_cd);
							stmt4.setString(5, transporter_cd);
							stmt4.setString(6, transporter_plant_seq);
							stmt4.setString(7, bu_plant_seq);
							stmt4.setString(8, plant_seq);
							stmt4.setString(9, cont_type);
							stmt4.setString(10, gas_dt);
							rset4=stmt4.executeQuery();
					  		if(rset4.next())
					  		{
					  			VBUYER_NOM_REV_NO.add(rset4.getString(1)==null?"":rset4.getString(1));
					  			VBUYER_NOM.add(rset4.getString(6)==null?"":rset4.getString(6));
					  		}
					  		rset4.close();
					  		stmt4.close();
				  		}
				  		else
				  		{
				  			//GET BUYER NOMINATION DATA FOR SELECTED CONTRACT IF BUYER NOMINATION DONE
				  			String queryString4="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY') "
					  				+ "FROM FMS_BUY_DAILY_BUYER_NOM A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_BUYER_NOM B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				  			stmt4=conn.prepareStatement(queryString4);
							stmt4.setString(1, cont);
							stmt4.setString(2, agmt);
							stmt4.setString(3, companyCd);
							stmt4.setString(4, countpty_cd);
							stmt4.setString(5, transporter_cd);
							stmt4.setString(6, transporter_plant_seq);
							stmt4.setString(7, bu_plant_seq);
							stmt4.setString(8, plant_seq);
							stmt4.setString(9, cont_type);
							stmt4.setString(10, gas_dt);
							rset4=stmt4.executeQuery();
					  		if(rset4.next())
					  		{
					  			VCOUNTERPARTY_CD.add(countpty_cd);
								VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
								VAGMT_NO.add(agmt);
								VAGMT_REV_NO.add(agmt_rev);
								VCONT_NO.add(cont);
								VCONT_REV_NO.add(cont_rev);
								VCONTRACT_TYPE.add(cont_type);
								VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
								VCARGO_NO.add("");
								VDIS_CONT_MAPPING.add(utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, ""));
								VCOUNTERPATY_STATUS.add(cpStatus);
								/*
								if(cont_type.equals("S"))
								{
									VDIS_CONT_MAPPING.add(comp_cd+cont_type+countpty_cd+"-"+agmt+"-"+cont);
								}
								else
								{
									VDIS_CONT_MAPPING.add(comp_cd+cont_type+countpty_cd+"-0-"+cont);
								}*/
								VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
								VCOUNTERPARTY_PLANT_ABBR.add(plant_abbr);
								VBU_CD.add(buCd);
								VBU_PLANT_SEQ.add(bu_plant_seq);
								VBU_PLANT_ABBR.add(bu_plant_abbr);
								VDCQ.add(dcq);
								VCONT_REF.add(cont_ref);
								VCONT_NAME.add(cont_name);
								VMDCQ_QTY.add(nf.format(mdcq_qty));
								VINTERNAL_MAP_ID.add(internal_map_id);
								VTAX_DTL.add(utilBean.getEntityTaxStructureDtl(conn,companyCd, countpty_cd, "T", plant_seq, bu_plant_seq, periodStDt, "S"));
								
								VNOM_REV_NO.add("");
					  			VBUYER_NOM_REV_NO.add(rset4.getString(1)==null?"":rset4.getString(1));
					  			VBASE.add(rset4.getString(3)==null?"":rset4.getString(3));
					  			VGCV.add(rset4.getString(4)==null?"9802.80":rset4.getString(4));
					  			VNCV.add(rset4.getString(5)==null?"8831.35":rset4.getString(5));
					  			VBUYER_NOM.add(rset4.getString(6)==null?"":rset4.getString(6));
					  			VQTY_MMBTU.add(rset4.getString(6)==null?"":rset4.getString(6));
					  			VQTY_SCM.add(rset4.getString(7)==null?"":rset4.getString(7));
					  			
					  			int isInvoiceSubmitted=isInvoiceSubmitted(countpty_cd, cont, agmt, plant_seq, cont_type, bu_plant_seq, gas_dt,false);
								if(isInvoiceSubmitted>0)
								{
									VNOM_BLOCK.add("Y");
								}
								else
								{
									VNOM_BLOCK.add("N");
								}
					  			
					  			String split[] = dateTime.split(" ");
					  			VGEN_DT.add(split[0]);
					  			VGEN_TIME.add(split[1]);
					  			VNOM_COLOR.add("");
					  		}
					  		rset4.close();
					  		stmt4.close();
				  		}
				  		rset5.close();
				  		stmt5.close();
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
			}
			rset.close();
			stmt.close();
			
			//Cargo Leveled Seller Nomination Details

			String queryString1="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CARGO_NO,CARGO_REF,CSOC_QTY "
					+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
					+ "WHERE COMPANY_CD=? AND BOE_NO IS NOT NULL "
					+ "AND ACTUAL_RECPT_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND (COALESCE(STORAGE_EXT_DAYS, 0) + COALESCE(STORAGE_DAYS-1, 0)) >= (SELECT ROUND( (TO_DATE(?,'DD/MM/YYYY') - ACTUAL_RECPT_DT), 0) FROM DUAL) "
					+ "AND BUY_SALE=? AND CARGO_STATUS=? "
					+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt=conn.prepareStatement(queryString1);
			stmt.setString(1, comp_cd);
			stmt.setString(2, gas_dt);
			stmt.setString(3, gas_dt);
			stmt.setString(4, "T");//ONLY FOR BUY SIDE(T)
			stmt.setString(5, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String companyCd = rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd = rset.getString(2)==null?"":rset.getString(2);
				String agmt = rset.getString(3)==null?"":rset.getString(3);
				String agmt_rev = rset.getString(4)==null?"":rset.getString(4);
				String cont = rset.getString(5)==null?"":rset.getString(5);
				String cont_rev = rset.getString(6)==null?"":rset.getString(6);
				String cont_type = rset.getString(7)==null?"":rset.getString(7);
				String cargo_no = rset.getString(8)==null?"":rset.getString(8);
				String cargo_ref_no = rset.getString(9)==null?"":rset.getString(9);
				String csoc_mmbtu = rset.getString(10)==null?"":rset.getString(10);
				String variable_csoc_mmbtu=utilBean.getCargoVariableCSOC(conn,companyCd, countpty_cd, "T",agmt, cont, cont_type, cargo_no,gas_dt);
				if(!variable_csoc_mmbtu.equals(""))
				{
					csoc_mmbtu=variable_csoc_mmbtu;
				}
				
				double mcsoc_qty =0;
				String cont_ref="";
				String cpStatus = (String) utilBean.getCounterpartyDetails(conn, countpty_cd, gas_dt).get("status");
				
				String queryString5 = "SELECT CONT_REF_NO,MDCQ_PERCENTAGE "
						+ "FROM FMS_LTCORA_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
						+ "AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				stmt5=conn.prepareStatement(queryString5);
				stmt5.setString(1, companyCd);
				stmt5.setString(2, countpty_cd);
				stmt5.setString(3, agmt);
				stmt5.setString(4, agmt_rev);
				stmt5.setString(5, cont_type);
				stmt5.setString(6, cont);
				rset5=stmt5.executeQuery();
		  		if(rset5.next())
		  		{
		  			cont_ref = rset5.getString(1)==null?"":rset5.getString(1);
		  			
		  			String mcsoc_percentage = nf.format(rset5.getDouble(2));
					mcsoc_qty = (Double.parseDouble(csoc_mmbtu)*Double.parseDouble(mcsoc_percentage))/100;
		  		}
		  		rset5.close();
		  		stmt5.close();
				
				String internal_map_id = cont_type+"-"+countpty_cd+"-"+agmt+"-"+agmt_rev+"-"+cont+"-"+cont_rev+"-"+cargo_no;
				
				//GET SELECTED COUNTERPARTY PLANT
				String queryString2="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_LTCORA_CONT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
						+ "AND AGMT_NO=? AND AGMT_REV=?";
				stmt1=conn.prepareStatement(queryString2);
				stmt1.setString(1, companyCd);
				stmt1.setString(2, countpty_cd);
				stmt1.setString(3, cont);
				stmt1.setString(4, cont_rev);
				stmt1.setString(5, cont_type);
				stmt1.setString(6, agmt);
				stmt1.setString(7, agmt_rev);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String plant_seq = rset1.getString(1)==null?"":rset1.getString(1);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, comp_cd, plant_seq, "T");
					
					//GET SELECTED BU PLANT
					String queryString3="SELECT COMPANY_CD,PLANT_SEQ_NO "
							+ "FROM FMS_LTCORA_CONT_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=?";
					stmt2=conn.prepareStatement(queryString3);
					stmt2.setString(1, companyCd);
					stmt2.setString(2, countpty_cd);
					stmt2.setString(3, cont);
					stmt2.setString(4, cont_rev);
					stmt2.setString(5, cont_type);
					stmt2.setString(6, agmt);
					stmt2.setString(7, agmt_rev);
					rset2=stmt2.executeQuery();
					while(rset2.next())
					{
						String transporter_cd = "0"; //NOT IN USED 10/11/2022
						String transporter_plant_seq = "0"; //NOT IN USED 10/11/2022
						String buCd = rset2.getString(1)==null?"":rset2.getString(1);
						String bu_plant_seq = rset2.getString(2)==null?"":rset2.getString(2);
						String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,buCd, comp_cd, bu_plant_seq, "B");
						
						String billingFrq="";
						String queryString4="SELECT BILLING_FREQ "
								+ "FROM FMS_LTCORA_CONT_BILLING_DTL "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONT_NO=? AND CONT_REV=? "
								+ "AND CONTRACT_TYPE=?";
						stmt3=conn.prepareStatement(queryString4);
						stmt3.setString(1, companyCd);
						stmt3.setString(2, countpty_cd);
						stmt3.setString(3, agmt);
						stmt3.setString(4, agmt_rev);
						stmt3.setString(5, cont);
						stmt3.setString(6, cont_rev);
						stmt3.setString(7, cont_type);
						rset3=stmt3.executeQuery();
				  		if(rset3.next())
				  		{
				  			billingFrq=rset3.getString(1)==null?"":rset3.getString(1);
				  		}
				  		rset3.close();
				  		stmt3.close();
				  		
				  		String periodStDt=utilBean.getFirstDtOfBillingCycle(billingFrq, "", gas_dt);
						
						//GET SELLER NOMINATION DATA FOR SELECTED CONTRACT
				  		String queryString6="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY') "
				  				+ "FROM FMS_BUY_DAILY_SELLER_NOM A "
				  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
								+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
								+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
								+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_SELLER_NOM B "
								+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
								+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
								+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
								+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
								+ "AND B.GAS_DT=A.GAS_DT AND A.CARGO_NO=B.CARGO_NO) ";
						stmt5=conn.prepareStatement(queryString6);
						stmt5.setString(1, cont);
						stmt5.setString(2, agmt);
						stmt5.setString(3, companyCd);
						stmt5.setString(4, countpty_cd);
						stmt5.setString(5, transporter_cd);
						stmt5.setString(6, transporter_plant_seq);
						stmt5.setString(7, bu_plant_seq);
						stmt5.setString(8, plant_seq);
						stmt5.setString(9, cont_type);
						stmt5.setString(10, gas_dt);
						stmt5.setString(11, cargo_no);
						rset5=stmt5.executeQuery();
				  		if(rset5.next())
				  		{
				  			VCOUNTERPARTY_CD.add(countpty_cd);
							VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
							VAGMT_NO.add(agmt);
							VAGMT_REV_NO.add(agmt_rev);
							VCONT_NO.add(cont);
							VCONT_REV_NO.add(cont_rev);
							VCONTRACT_TYPE.add(cont_type);
							VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
							VDIS_CONT_MAPPING.add(cargo_ref_no);
							VDCQ.add(csoc_mmbtu);
							VCARGO_NO.add(cargo_no);
							
							VCONT_REF.add(cont_ref);
							VMDCQ_QTY.add(nf.format(mcsoc_qty));
							
							VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
							VCOUNTERPARTY_PLANT_ABBR.add(plant_abbr);
							VBU_CD.add(buCd);
							VBU_PLANT_SEQ.add(bu_plant_seq);
							VBU_PLANT_ABBR.add(bu_plant_abbr);
							VCONT_NAME.add(cont_name);
							VINTERNAL_MAP_ID.add(internal_map_id);
							VCOUNTERPATY_STATUS.add(cpStatus);
							
							String queryString_temp="SELECT TAX_STRUCT_DTL "
									+ "FROM FMS_ENTITY_SERVICE_TAX_DTL A "
									+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND PLANT_SEQ_NO=? AND BU_UNIT=? "
									+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_SERVICE_TAX_DTL B "
									+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
									+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.BU_UNIT=B.BU_UNIT AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
							stmt_temp=conn.prepareStatement(queryString_temp);
							stmt_temp.setString(1, "T");
							stmt_temp.setString(2, companyCd);
							stmt_temp.setString(3, countpty_cd);
							stmt_temp.setString(4, plant_seq);
							stmt_temp.setString(5, bu_plant_seq);
							stmt_temp.setString(6, periodStDt);
							rset_temp=stmt_temp.executeQuery();
							if(rset_temp.next())
							{
								VTAX_DTL.add(rset_temp.getString(1)==null?"":rset_temp.getString(1));
							}
							else 
							{
								VTAX_DTL.add("");
							}
							rset_temp.close();
							stmt_temp.close();
							
				  			VNOM_REV_NO.add(rset5.getString(1)==null?"":rset5.getString(1));
				  			VGEN_TIME.add(rset5.getString(2)==null?"":rset5.getString(2));
				  			VBASE.add(rset5.getString(3)==null?"":rset5.getString(3));
				  			VGCV.add(rset5.getString(4)==null?"9802.80":rset5.getString(4));
				  			VNCV.add(rset5.getString(5)==null?"8831.35":rset5.getString(5));
				  			VQTY_MMBTU.add(rset5.getString(6)==null?"":rset5.getString(6));
				  			VQTY_SCM.add(rset5.getString(7)==null?"":rset5.getString(7));
				  			VGEN_DT.add(rset5.getString(8)==null?"":rset5.getString(8));
				  			
				  			VNOM_COLOR.add("#99ffcc");
				  			
				  			int isInvoiceSubmitted=isInvoiceSubmitted_Cargo(countpty_cd, cont, agmt, plant_seq, cont_type, bu_plant_seq, gas_dt, cargo_no,false);
							if(isInvoiceSubmitted>0)
							{
								VNOM_BLOCK.add("Y");
							}
							else
							{
								VNOM_BLOCK.add("N");
							}
				  			
							String queryString7="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM "
					  				+ "FROM FMS_BUY_DAILY_BUYER_NOM A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_BUYER_NOM B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
									+ "AND B.GAS_DT=A.GAS_DT AND A.CARGO_NO=B.CARGO_NO) ";
				  			stmt4=conn.prepareStatement(queryString7);
							stmt4.setString(1, cont);
							stmt4.setString(2, agmt);
							stmt4.setString(3, companyCd);
							stmt4.setString(4, countpty_cd);
							stmt4.setString(5, transporter_cd);
							stmt4.setString(6, transporter_plant_seq);
							stmt4.setString(7, bu_plant_seq);
							stmt4.setString(8, plant_seq);
							stmt4.setString(9, cont_type);
							stmt4.setString(10, gas_dt);
							stmt4.setString(11, cargo_no);
							rset4=stmt4.executeQuery();
					  		if(rset4.next())
					  		{
					  			VBUYER_NOM_REV_NO.add(rset4.getString(1)==null?"":rset4.getString(1));
					  			VBUYER_NOM.add(rset4.getString(6)==null?"":rset4.getString(6));
					  		}
					  		rset4.close();
					  		stmt4.close();
				  		}
				  		else
				  		{
				  			//GET BUYER NOMINATION DATA FOR SELECTED CONTRACT IF BUYER NOMINATION DONE
				  			String queryString7="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY') "
					  				+ "FROM FMS_BUY_DAILY_BUYER_NOM A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_BUYER_NOM B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
									+ "AND B.GAS_DT=A.GAS_DT AND A.CARGO_NO=B.CARGO_NO) ";
				  			stmt4=conn.prepareStatement(queryString7);
							stmt4.setString(1, cont);
							stmt4.setString(2, agmt);
							stmt4.setString(3, companyCd);
							stmt4.setString(4, countpty_cd);
							stmt4.setString(5, transporter_cd);
							stmt4.setString(6, transporter_plant_seq);
							stmt4.setString(7, bu_plant_seq);
							stmt4.setString(8, plant_seq);
							stmt4.setString(9, cont_type);
							stmt4.setString(10, gas_dt);
							stmt4.setString(11, cargo_no);
							rset4=stmt4.executeQuery();
					  		if(rset4.next())
					  		{
					  			VCOUNTERPARTY_CD.add(countpty_cd);
								VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
								VAGMT_NO.add(agmt);
								VAGMT_REV_NO.add(agmt_rev);
								VCONT_NO.add(cont);
								VCONT_REV_NO.add(cont_rev);
								VCONTRACT_TYPE.add(cont_type);
								VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
								VDIS_CONT_MAPPING.add(cargo_ref_no);
								VDCQ.add(csoc_mmbtu);
								VCARGO_NO.add(cargo_no);
								
								VCONT_REF.add(cont_ref);
								VMDCQ_QTY.add(nf.format(mcsoc_qty));
								
								VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
								VCOUNTERPARTY_PLANT_ABBR.add(plant_abbr);
								VBU_CD.add(buCd);
								VBU_PLANT_SEQ.add(bu_plant_seq);
								VBU_PLANT_ABBR.add(bu_plant_abbr);
								VCONT_NAME.add(cont_name);
								VINTERNAL_MAP_ID.add(internal_map_id);
								VCOUNTERPATY_STATUS.add(cpStatus);

								String queryString_temp="SELECT TAX_STRUCT_DTL "
										+ "FROM FMS_ENTITY_SERVICE_TAX_DTL A "
										+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND PLANT_SEQ_NO=? AND BU_UNIT=? "
										+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_SERVICE_TAX_DTL B "
										+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
										+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.BU_UNIT=B.BU_UNIT AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
								stmt_temp=conn.prepareStatement(queryString_temp);
								stmt_temp.setString(1, "T");
								stmt_temp.setString(2, companyCd);
								stmt_temp.setString(3, countpty_cd);
								stmt_temp.setString(4, plant_seq);
								stmt_temp.setString(5, bu_plant_seq);
								stmt_temp.setString(6, periodStDt);
								rset_temp=stmt_temp.executeQuery();
								if(rset_temp.next())
								{
									VTAX_DTL.add(rset_temp.getString(1)==null?"":rset_temp.getString(1));
								}
								else 
								{
									VTAX_DTL.add("");
								}
								rset_temp.close();
								stmt_temp.close();
								
								VNOM_REV_NO.add("");
					  			VBUYER_NOM_REV_NO.add(rset4.getString(1)==null?"":rset4.getString(1));
					  			VBASE.add(rset4.getString(3)==null?"":rset4.getString(3));
					  			VGCV.add(rset4.getString(4)==null?"9802.80":rset4.getString(4));
					  			VNCV.add(rset4.getString(5)==null?"8831.35":rset4.getString(5));
					  			VBUYER_NOM.add(rset4.getString(6)==null?"":rset4.getString(6));
					  			VQTY_MMBTU.add(rset4.getString(6)==null?"":rset4.getString(6));
					  			VQTY_SCM.add(rset4.getString(7)==null?"":rset4.getString(7));
					  			
					  			int isInvoiceSubmitted=isInvoiceSubmitted_Cargo(countpty_cd, cont, agmt, plant_seq, cont_type, bu_plant_seq, gas_dt,cargo_no,false);
								if(isInvoiceSubmitted>0)
								{
									VNOM_BLOCK.add("Y");
								}
								else
								{
									VNOM_BLOCK.add("N");
								}
					  			
					  			String split[] = dateTime.split(" ");
					  			VGEN_DT.add(split[0]);
					  			VGEN_TIME.add(split[1]);
					  			VNOM_COLOR.add("");
					  		}
					  		rset4.close();
					  		stmt4.close();
				  		}
				  		rset5.close();
				  		stmt5.close();
					}
					rset2.close();
					stmt2.close();
				}
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
	
	public void getDailyAllocation()
	{
		String function_nm="getDailyAllocation()";
		try
		{
			dateTime = dateUtil.getSysdateWithTime24hr();
			
			String queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
					+ "CONT_REF_NO,DCQ,CONT_NAME,MDCQ_PERCENTAGE,TRADE_REF_NO,TO_CHAR(START_DT,'DD/MM/YYYY') "
					+ "FROM FMS_TRADER_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND FCC_FLAG=? "
					+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "Y");
			stmt.setString(3, gas_dt);
			stmt.setString(4, gas_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String companyCd = rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd = rset.getString(2)==null?"":rset.getString(2);
				String agmt = rset.getString(3)==null?"":rset.getString(3);
				String agmt_rev = rset.getString(4)==null?"":rset.getString(4);
				String cont = rset.getString(5)==null?"":rset.getString(5);
				String cont_rev = rset.getString(6)==null?"":rset.getString(6);
				String cont_type = rset.getString(7)==null?"":rset.getString(7);
				String cont_ref = rset.getString(8)==null?"":rset.getString(8);
				String dcq = nf.format(rset.getDouble(9));
				
				String variable_dcq_qty=utilBean.getPurchaseContVariableDCQ(conn,companyCd,countpty_cd, agmt, cont, cont_type, gas_dt);
				
				if(!variable_dcq_qty.equals(""))
				{
					dcq=variable_dcq_qty;
				}
				
				String cpStatus = (String) utilBean.getCounterpartyDetails(conn, countpty_cd, gas_dt).get("status");
				
				String cont_name = rset.getString(10)==null?"":rset.getString(10);
				String mdcq_percentage = nf.format(rset.getDouble(11));
				double mdcq_qty = (Double.parseDouble(dcq)*Double.parseDouble(mdcq_percentage))/100;
				
				String trade_ref = rset.getString(12)==null?"":rset.getString(12);
				if(cont_type.equals("I")) {
					cont_ref=trade_ref;
				}
				
				String internal_map_id = cont_type+"-"+countpty_cd+"-"+agmt+"-"+agmt_rev+"-"+cont+"-"+cont_rev;
				
				String cont_start_dt=rset.getString(13)==null?"":rset.getString(13);
				
				//GET SELECTED COUNTERPARTY PLANT
				String queryString1="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_TRADER_CONT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
						+ "AND AGMT_NO=? AND AGMT_REV=?";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, companyCd);
				stmt1.setString(2, countpty_cd);
				stmt1.setString(3, cont);
				stmt1.setString(4, cont_rev);
				stmt1.setString(5, cont_type);
				stmt1.setString(6, agmt);
				stmt1.setString(7, agmt_rev);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String plant_seq = rset1.getString(1)==null?"":rset1.getString(1);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, comp_cd, plant_seq, "T");
					
					//GET SELECTED BU PLANT
					String queryString2="SELECT COMPANY_CD,PLANT_SEQ_NO "
							+ "FROM FMS_TRADER_CONT_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=?";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, companyCd);
					stmt2.setString(2, countpty_cd);
					stmt2.setString(3, cont);
					stmt2.setString(4, cont_rev);
					stmt2.setString(5, cont_type);
					stmt2.setString(6, agmt);
					stmt2.setString(7, agmt_rev);
					rset2=stmt2.executeQuery();
					while(rset2.next())
					{
						String transporter_cd = "0"; //NOT IN USED 10/11/2022
						String transporter_plant_seq = "0"; //NOT IN USED 10/11/2022
						String buCd = rset2.getString(1)==null?"":rset2.getString(1);
						String bu_plant_seq = rset2.getString(2)==null?"":rset2.getString(2);
						String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,buCd, comp_cd, bu_plant_seq, "B");
						
						String billingFrq="";
						String queryString3="SELECT BILLING_FREQ "
								+ "FROM FMS_TRADER_BILLING_DTL "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONT_NO=? AND CONT_REV=? "
								+ "AND CONTRACT_TYPE=?";
						stmt3=conn.prepareStatement(queryString3);
						stmt3.setString(1, companyCd);
						stmt3.setString(2, countpty_cd);
						stmt3.setString(3, agmt);
						stmt3.setString(4, agmt_rev);
						stmt3.setString(5, cont);
						stmt3.setString(6, cont_rev);
						stmt3.setString(7, cont_type);
						rset3=stmt3.executeQuery();
				  		if(rset3.next())
				  		{
				  			billingFrq=rset3.getString(1)==null?"":rset3.getString(1);
				  		}
				  		rset3.close();
				  		stmt3.close();
				  		
				  		String periodStDt=utilBean.getFirstDtOfBillingCycle(billingFrq, "", gas_dt);
						
				  		int mole_count=0;
						Vector TEMP_MOLE_MAP=new Vector();
						Vector TEMP_MOLE_SEQ_NO=new Vector();
						Vector TEMP_MOLE_QTY_MMBTU=new Vector();
						Vector TEMP_MOLE_QTY_SCM=new Vector();
						Vector TEMP_MOLE_EXIST=new Vector();
						Vector TEMP_MOLE_COLOR=new Vector();
						
						//GET ALLOCATION DATA
				  		String queryString5="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY') "
				  				+ "FROM FMS_BUY_DAILY_ALLOCATION A "
				  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
								+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
								+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
								+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
								+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
								+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
								+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
								+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
						stmt5=conn.prepareStatement(queryString5);
						stmt5.setString(1, cont);
						stmt5.setString(2, agmt);
						stmt5.setString(3, companyCd);
						stmt5.setString(4, countpty_cd);
						stmt5.setString(5, transporter_cd);
						stmt5.setString(6, transporter_plant_seq);
						stmt5.setString(7, bu_plant_seq);
						stmt5.setString(8, plant_seq);
						stmt5.setString(9, cont_type);
						stmt5.setString(10, gas_dt);
						rset5=stmt5.executeQuery();
				  		if(rset5.next())
				  		{
				  			VCOUNTERPARTY_CD.add(countpty_cd);
							VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
							VAGMT_NO.add(agmt);
							VAGMT_REV_NO.add(agmt_rev);
							VCONT_NO.add(cont);
							VCONT_REV_NO.add(cont_rev);
							VCONTRACT_TYPE.add(cont_type);
							VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
							VCARGO_NO.add("");
							VDIS_CONT_MAPPING.add(utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, ""));
							VCOUNTERPATY_STATUS.add(cpStatus);
							/*
							if(cont_type.equals("S"))
							{
								VDIS_CONT_MAPPING.add(comp_cd+cont_type+countpty_cd+"-"+agmt+"-"+cont);
							}
							else
							{
								VDIS_CONT_MAPPING.add(comp_cd+cont_type+countpty_cd+"-0-"+cont);
							}*/
							VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
							VCOUNTERPARTY_PLANT_ABBR.add(plant_abbr);
							VBU_CD.add(buCd);
							VBU_PLANT_SEQ.add(bu_plant_seq);
							VBU_PLANT_ABBR.add(bu_plant_abbr);
							VDCQ.add(dcq);
							VCONT_REF.add(cont_ref);
							VCONT_NAME.add(cont_name);
							VMDCQ_QTY.add(nf.format(mdcq_qty));
							VINTERNAL_MAP_ID.add(internal_map_id);
							VTAX_DTL.add(utilBean.getEntityTaxStructureDtl(conn,companyCd, countpty_cd, "T", plant_seq, bu_plant_seq, periodStDt, "S"));
							
							VIS_EXIST.add("Y");
							
							int isInvoiceSubmitted=isInvoiceSubmitted(countpty_cd, cont, agmt, plant_seq, cont_type, bu_plant_seq, gas_dt,true);
							if(isInvoiceSubmitted>0)
							{
								VNOM_BLOCK.add("Y");
							}
							else
							{
								VNOM_BLOCK.add("N");
							}
							
				  			VNOM_REV_NO.add(rset5.getString(1)==null?"":rset5.getString(1));
				  			VGEN_TIME.add(rset5.getString(2)==null?"":rset5.getString(2));
				  			VBASE.add(rset5.getString(3)==null?"":rset5.getString(3));
				  			VGCV.add(rset5.getString(4)==null?"9802.80":rset5.getString(4));
				  			VNCV.add(rset5.getString(5)==null?"8831.35":rset5.getString(5));
				  			VQTY_MMBTU.add(rset5.getString(6)==null?"":rset5.getString(6));
				  			VQTY_SCM.add(rset5.getString(7)==null?"":rset5.getString(7));
				  			VGEN_DT.add(rset5.getString(8)==null?"":rset5.getString(8));
				  			
				  			VNOM_COLOR.add("#99ffcc");
				  			
				  			String queryString4="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM "
					  				+ "FROM FMS_BUY_DAILY_SELLER_NOM A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_SELLER_NOM B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				  			stmt4=conn.prepareStatement(queryString4);
							stmt4.setString(1, cont);
							stmt4.setString(2, agmt);
							stmt4.setString(3, companyCd);
							stmt4.setString(4, countpty_cd);
							stmt4.setString(5, transporter_cd);
							stmt4.setString(6, transporter_plant_seq);
							stmt4.setString(7, bu_plant_seq);
							stmt4.setString(8, plant_seq);
							stmt4.setString(9, cont_type);
							stmt4.setString(10, gas_dt);
							rset4=stmt4.executeQuery();
					  		if(rset4.next())
					  		{
					  			VBUYER_NOM_REV_NO.add(rset4.getString(1)==null?"":rset4.getString(1));
					  			VBUYER_NOM.add(rset4.getString(6)==null?"":rset4.getString(6));
					  		}
					  		rset4.close();
					  		stmt4.close();
					  		
					  		String queryString6="SELECT NOM_REV_NO,SEQ_NO,QTY_MMBTU,QTY_SCM,MOLECULE_MAP "
					  				+ "FROM FMS_BUY_DAILY_ALLOCATION_MM A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? AND DTL_CATEGORY='MOL' "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION_MM B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND A.BU_SEQ=B.BU_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
									+ "AND B.GAS_DT=A.GAS_DT AND B.SEQ_NO=A.SEQ_NO AND B.CARGO_NO=A.CARGO_NO AND B.DTL_CATEGORY=A.DTL_CATEGORY) ";
							stmt6 = conn.prepareStatement(queryString6);
							stmt6.setString(1, cont);
							stmt6.setString(2, agmt);
							stmt6.setString(3, companyCd);
							stmt6.setString(4, countpty_cd);
							stmt6.setString(5, transporter_cd);
							stmt6.setString(6, transporter_plant_seq);
							stmt6.setString(7, plant_seq);
							stmt6.setString(8, cont_type);
							stmt6.setString(9, bu_plant_seq);
							stmt6.setString(10, gas_dt);
							stmt6.setString(11, cargo_no);
							rset6=stmt6.executeQuery();
							while(rset6.next())
							{
								mole_count++;
								
								TEMP_MOLE_SEQ_NO.add(rset6.getString(2)==null?"":rset6.getString(2));
								TEMP_MOLE_QTY_MMBTU.add(nf.format(rset6.getDouble(3)));
								TEMP_MOLE_QTY_SCM.add(nf.format(rset6.getDouble(4)));
								TEMP_MOLE_MAP.add(rset6.getString(5)==null?"":rset6.getString(5));
								TEMP_MOLE_EXIST.add("Y");
								TEMP_MOLE_COLOR.add("#99ffcc");
							}
							rset6.close();
							stmt6.close();
							
							//FETCHING LAST DATE FOR AUTO SELECTION
							if(mole_count==0)
							{
								String queryString7="SELECT MOLECULE_MAP,MAX(GAS_DT) "
										+ "FROM FMS_BUY_DAILY_ALLOCATION_MM A "
										+ "WHERE CONT_NO=? AND AGMT_NO=? "
										+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
										+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
										+ "AND GAS_DT < TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? AND DTL_CATEGORY='MOL' "
										+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION_MM B "
										+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
										+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
										+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND A.BU_SEQ=B.BU_SEQ "
										+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
										+ "AND B.GAS_DT=A.GAS_DT AND B.SEQ_NO=A.SEQ_NO AND B.CARGO_NO=A.CARGO_NO AND B.DTL_CATEGORY=A.DTL_CATEGORY) "
										+ "GROUP BY MOLECULE_MAP ";
								stmt7 = conn.prepareStatement(queryString7);
								stmt7.setString(1, cont);
								stmt7.setString(2, agmt);
								stmt7.setString(3, companyCd);
								stmt7.setString(4, countpty_cd);
								stmt7.setString(5, transporter_cd);
								stmt7.setString(6, transporter_plant_seq);
								stmt7.setString(7, plant_seq);
								stmt7.setString(8, cont_type);
								stmt7.setString(9, bu_plant_seq);
								stmt7.setString(10, gas_dt);
								stmt7.setString(11, cargo_no);
								rset7=stmt7.executeQuery();
								while(rset7.next())
								{
									TEMP_MOLE_SEQ_NO.add("");
									TEMP_MOLE_QTY_MMBTU.add("");
									TEMP_MOLE_QTY_SCM.add("");
									TEMP_MOLE_MAP.add(rset7.getString(1)==null?"":rset7.getString(1));
									TEMP_MOLE_EXIST.add("N");
									TEMP_MOLE_COLOR.add("");
								}
								rset7.close();
								stmt7.close();
							}
							
							VALLOC_MOLE_MAPPING.add(TEMP_MOLE_MAP);
							VALLOC_MOLE_SEQ_NO.add(TEMP_MOLE_SEQ_NO);
							VALLOC_MOLE_QTY_MMBTU.add(TEMP_MOLE_QTY_MMBTU);
							VALLOC_MOLE_QTY_SCM.add(TEMP_MOLE_QTY_SCM);
							VALLOC_MOLE_EXIST.add(TEMP_MOLE_EXIST);
							VALLOC_MOLE_COLOR.add(TEMP_MOLE_COLOR);
				  		}
				  		else
				  		{
				  			//GET SELLER NOMINATION DATA FOR SELECTED CONTRACT IF SELLER NOMINATION DONE
				  			String queryString4="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY') "
					  				+ "FROM FMS_BUY_DAILY_SELLER_NOM A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_SELLER_NOM B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				  			stmt4=conn.prepareStatement(queryString4);
							stmt4.setString(1, cont);
							stmt4.setString(2, agmt);
							stmt4.setString(3, companyCd);
							stmt4.setString(4, countpty_cd);
							stmt4.setString(5, transporter_cd);
							stmt4.setString(6, transporter_plant_seq);
							stmt4.setString(7, bu_plant_seq);
							stmt4.setString(8, plant_seq);
							stmt4.setString(9, cont_type);
							stmt4.setString(10, gas_dt);
							rset4=stmt4.executeQuery();
					  		if(rset4.next())
					  		{
					  			VCOUNTERPARTY_CD.add(countpty_cd);
								VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
								VAGMT_NO.add(agmt);
								VAGMT_REV_NO.add(agmt_rev);
								VCONT_NO.add(cont);
								VCONT_REV_NO.add(cont_rev);
								VCONTRACT_TYPE.add(cont_type);
								VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
								VCARGO_NO.add("");
								VCOUNTERPATY_STATUS.add(cpStatus);
								VDIS_CONT_MAPPING.add(utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, ""));
								/*
								if(cont_type.equals("S"))
								{
									VDIS_CONT_MAPPING.add(comp_cd+cont_type+countpty_cd+"-"+agmt+"-"+cont);
								}
								else
								{
									VDIS_CONT_MAPPING.add(comp_cd+cont_type+countpty_cd+"-0-"+cont);
								}*/
								VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
								VCOUNTERPARTY_PLANT_ABBR.add(plant_abbr);
								VBU_CD.add(buCd);
								VBU_PLANT_SEQ.add(bu_plant_seq);
								VBU_PLANT_ABBR.add(bu_plant_abbr);
								VDCQ.add(dcq);
								VCONT_REF.add(cont_ref);
								VCONT_NAME.add(cont_name);
								VMDCQ_QTY.add(nf.format(mdcq_qty));
								VINTERNAL_MAP_ID.add(internal_map_id);
								VTAX_DTL.add(utilBean.getEntityTaxStructureDtl(conn,companyCd, countpty_cd, "T", plant_seq, bu_plant_seq, periodStDt, "S"));
								
								int isInvoiceSubmitted=isInvoiceSubmitted(countpty_cd, cont, agmt, plant_seq, cont_type, bu_plant_seq, gas_dt,true);
								if(isInvoiceSubmitted>0)
								{
									VNOM_BLOCK.add("Y");
								}
								else
								{
									VNOM_BLOCK.add("N");
								}
								
								VNOM_REV_NO.add("");
					  			VBUYER_NOM_REV_NO.add(rset4.getString(1)==null?"":rset4.getString(1));
					  			VBASE.add(rset4.getString(3)==null?"":rset4.getString(3));
					  			VGCV.add(rset4.getString(4)==null?"9802.80":rset4.getString(4));
					  			VNCV.add(rset4.getString(5)==null?"8831.35":rset4.getString(5));
					  			VBUYER_NOM.add(rset4.getString(6)==null?"":rset4.getString(6));
					  			VQTY_MMBTU.add(rset4.getString(6)==null?"":rset4.getString(6));
					  			VQTY_SCM.add(rset4.getString(7)==null?"":rset4.getString(7));
					  			
					  			String split[] = dateTime.split(" ");
					  			VGEN_DT.add(split[0]);
					  			VGEN_TIME.add(split[1]);
					  			VNOM_COLOR.add("");
					  			
					  			VIS_EXIST.add("N");
					  			
					  			//FETCHING LAST DATE FOR AUTO SELECTION
								if(mole_count==0)
								{
									String queryString7="SELECT MOLECULE_MAP,MAX(GAS_DT) "
											+ "FROM FMS_BUY_DAILY_ALLOCATION_MM A "
											+ "WHERE CONT_NO=? AND AGMT_NO=? "
											+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
											+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
											+ "AND GAS_DT < TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? AND DTL_CATEGORY='MOL' "
											+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION_MM B "
											+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
											+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
											+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND A.BU_SEQ=B.BU_SEQ "
											+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
											+ "AND B.GAS_DT=A.GAS_DT AND B.SEQ_NO=A.SEQ_NO AND B.CARGO_NO=A.CARGO_NO AND B.DTL_CATEGORY=A.DTL_CATEGORY) "
											+ "GROUP BY MOLECULE_MAP ";
									stmt7 = conn.prepareStatement(queryString7);
									stmt7.setString(1, cont);
									stmt7.setString(2, agmt);
									stmt7.setString(3, companyCd);
									stmt7.setString(4, countpty_cd);
									stmt7.setString(5, transporter_cd);
									stmt7.setString(6, transporter_plant_seq);
									stmt7.setString(7, plant_seq);
									stmt7.setString(8, cont_type);
									stmt7.setString(9, bu_plant_seq);
									stmt7.setString(10, gas_dt);
									stmt7.setString(11, cargo_no);
									rset7=stmt7.executeQuery();
									while(rset7.next())
									{
										TEMP_MOLE_SEQ_NO.add("");
										TEMP_MOLE_QTY_MMBTU.add("");
										TEMP_MOLE_QTY_SCM.add("");
										TEMP_MOLE_MAP.add(rset7.getString(1)==null?"":rset7.getString(1));
										TEMP_MOLE_EXIST.add("N");
										TEMP_MOLE_COLOR.add("");
									}
									rset7.close();
									stmt7.close();
								}
					  			
					  			VALLOC_MOLE_MAPPING.add(TEMP_MOLE_MAP);
								VALLOC_MOLE_SEQ_NO.add(TEMP_MOLE_SEQ_NO);
								VALLOC_MOLE_QTY_MMBTU.add(TEMP_MOLE_QTY_MMBTU);
								VALLOC_MOLE_QTY_SCM.add(TEMP_MOLE_QTY_SCM);
								VALLOC_MOLE_EXIST.add(TEMP_MOLE_EXIST);
								VALLOC_MOLE_COLOR.add(TEMP_MOLE_COLOR);
					  		}
					  		rset4.close();
					  		stmt4.close();
				  		}
				  		rset5.close();
				  		stmt5.close();
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
			}
			rset.close();
			stmt.close();
			
			//Cargo Leveled Allocation Details
			
			String queryString1="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CARGO_NO,CARGO_REF,CSOC_QTY "
					+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
					+ "WHERE COMPANY_CD=? AND BOE_NO IS NOT NULL "
					+ "AND ACTUAL_RECPT_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND (COALESCE(STORAGE_EXT_DAYS, 0) + COALESCE(STORAGE_DAYS-1, 0)) >= (SELECT ROUND( (TO_DATE(?,'DD/MM/YYYY') - ACTUAL_RECPT_DT), 0) FROM DUAL) "
					+ "AND BUY_SALE=? AND CARGO_STATUS=? "
					+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt=conn.prepareStatement(queryString1);
			stmt.setString(1, comp_cd);
			stmt.setString(2, gas_dt);
			stmt.setString(3, gas_dt);
			stmt.setString(4, "T");//ONLY FOR BUY SIDE(T)
			stmt.setString(5, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String companyCd = rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd = rset.getString(2)==null?"":rset.getString(2);
				String agmt = rset.getString(3)==null?"":rset.getString(3);
				String agmt_rev = rset.getString(4)==null?"":rset.getString(4);
				String cont = rset.getString(5)==null?"":rset.getString(5);
				String cont_rev = rset.getString(6)==null?"":rset.getString(6);
				String cont_type = rset.getString(7)==null?"":rset.getString(7);
				String cargo_no = rset.getString(8)==null?"":rset.getString(8);
				String cargo_ref_no = rset.getString(9)==null?"":rset.getString(9);
				String csoc_mmbtu = rset.getString(10)==null?"":rset.getString(10);
				String variable_csoc_mmbtu=utilBean.getCargoVariableCSOC(conn,companyCd, countpty_cd, "T",agmt, cont, cont_type, cargo_no, gas_dt);
				if(!variable_csoc_mmbtu.equals(""))
				{
					csoc_mmbtu=variable_csoc_mmbtu;
				}
				
				String cpStatus = (String) utilBean.getCounterpartyDetails(conn, countpty_cd, gas_dt).get("status");
				
				double mcsoc_qty =0;
				String cont_ref="";
				
				String queryString5 = "SELECT CONT_REF_NO,MDCQ_PERCENTAGE "
						+ "FROM FMS_LTCORA_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
						+ "AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				stmt5=conn.prepareStatement(queryString5);
				stmt5.setString(1, companyCd);
				stmt5.setString(2, countpty_cd);
				stmt5.setString(3, agmt);
				stmt5.setString(4, agmt_rev);
				stmt5.setString(5, cont_type);
				stmt5.setString(6, cont);
				rset5=stmt5.executeQuery();
		  		if(rset5.next())
		  		{
		  			cont_ref = rset5.getString(1)==null?"":rset5.getString(1);
		  			
		  			String mcsoc_percentage = nf.format(rset5.getDouble(2));
					mcsoc_qty = (Double.parseDouble(csoc_mmbtu)*Double.parseDouble(mcsoc_percentage))/100;
		  		}
		  		rset5.close();
		  		stmt5.close();
				
				String internal_map_id = cont_type+"-"+countpty_cd+"-"+agmt+"-"+agmt_rev+"-"+cont+"-"+cont_rev;
				
				//GET SELECTED COUNTERPARTY PLANT
				String queryString2="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_LTCORA_CONT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
						+ "AND AGMT_NO=? AND AGMT_REV=?";
				stmt1=conn.prepareStatement(queryString2);
				stmt1.setString(1, companyCd);
				stmt1.setString(2, countpty_cd);
				stmt1.setString(3, cont);
				stmt1.setString(4, cont_rev);
				stmt1.setString(5, cont_type);
				stmt1.setString(6, agmt);
				stmt1.setString(7, agmt_rev);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String plant_seq = rset1.getString(1)==null?"":rset1.getString(1);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, comp_cd, plant_seq, "T");
					
					//GET SELECTED BU PLANT
					String queryString3="SELECT COMPANY_CD,PLANT_SEQ_NO "
							+ "FROM FMS_LTCORA_CONT_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=?";
					stmt2=conn.prepareStatement(queryString3);
					stmt2.setString(1, companyCd);
					stmt2.setString(2, countpty_cd);
					stmt2.setString(3, cont);
					stmt2.setString(4, cont_rev);
					stmt2.setString(5, cont_type);
					stmt2.setString(6, agmt);
					stmt2.setString(7, agmt_rev);
					rset2=stmt2.executeQuery();
					while(rset2.next())
					{
						String transporter_cd = "0"; //NOT IN USED 10/11/2022
						String transporter_plant_seq = "0"; //NOT IN USED 10/11/2022
						String buCd = rset2.getString(1)==null?"":rset2.getString(1);
						String bu_plant_seq = rset2.getString(2)==null?"":rset2.getString(2);
						String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,buCd, comp_cd, bu_plant_seq, "B");
						
						String billingFrq="";
						String queryString4="SELECT BILLING_FREQ "
								+ "FROM FMS_LTCORA_CONT_BILLING_DTL "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONT_NO=? AND CONT_REV=? "
								+ "AND CONTRACT_TYPE=?";
						stmt3=conn.prepareStatement(queryString4);
						stmt3.setString(1, companyCd);
						stmt3.setString(2, countpty_cd);
						stmt3.setString(3, agmt);
						stmt3.setString(4, agmt_rev);
						stmt3.setString(5, cont);
						stmt3.setString(6, cont_rev);
						stmt3.setString(7, cont_type);
						rset3=stmt3.executeQuery();
				  		if(rset3.next())
				  		{
				  			billingFrq=rset3.getString(1)==null?"":rset3.getString(1);
				  		}
				  		rset3.close();
				  		stmt3.close();
				  		
				  		String periodStDt=utilBean.getFirstDtOfBillingCycle(billingFrq, "", gas_dt);
						
				  		int mole_count=0;
						Vector TEMP_MOLE_MAP=new Vector();
						Vector TEMP_MOLE_SEQ_NO=new Vector();
						Vector TEMP_MOLE_QTY_MMBTU=new Vector();
						Vector TEMP_MOLE_QTY_SCM=new Vector();
						Vector TEMP_MOLE_EXIST=new Vector();
						Vector TEMP_MOLE_COLOR=new Vector();
						
						//GET ALLOCATION DATA
				  		String queryString6="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY') "
				  				+ "FROM FMS_BUY_DAILY_ALLOCATION A "
				  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
								+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
								+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
								+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
								+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
								+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
								+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
								+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
								+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)";
						stmt5=conn.prepareStatement(queryString6);
						stmt5.setString(1, cont);
						stmt5.setString(2, agmt);
						stmt5.setString(3, companyCd);
						stmt5.setString(4, countpty_cd);
						stmt5.setString(5, transporter_cd);
						stmt5.setString(6, transporter_plant_seq);
						stmt5.setString(7, bu_plant_seq);
						stmt5.setString(8, plant_seq);
						stmt5.setString(9, cont_type);
						stmt5.setString(10, gas_dt);
						stmt5.setString(11, cargo_no);
						rset5=stmt5.executeQuery();
				  		if(rset5.next())
				  		{
				  			VCOUNTERPARTY_CD.add(countpty_cd);
							VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
							VAGMT_NO.add(agmt);
							VAGMT_REV_NO.add(agmt_rev);
							VCONT_NO.add(cont);
							VCONT_REV_NO.add(cont_rev);
							VCONTRACT_TYPE.add(cont_type);
							VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
							VCARGO_NO.add(cargo_no);
							VDIS_CONT_MAPPING.add(cargo_ref_no);
							
							VCOUNTERPATY_STATUS.add(cpStatus);
							
							VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
							VCOUNTERPARTY_PLANT_ABBR.add(plant_abbr);
							VBU_CD.add(buCd);
							VBU_PLANT_SEQ.add(bu_plant_seq);
							VBU_PLANT_ABBR.add(bu_plant_abbr);
							
							VDCQ.add(csoc_mmbtu);
							VCONT_REF.add(cont_ref);
							VMDCQ_QTY.add(nf.format(mcsoc_qty));
							
							VCONT_NAME.add(cont_name);
							VINTERNAL_MAP_ID.add(internal_map_id);
							
							String queryString_temp="SELECT TAX_STRUCT_DTL "
									+ "FROM FMS_ENTITY_SERVICE_TAX_DTL A "
									+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND PLANT_SEQ_NO=? AND BU_UNIT=? "
									+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_SERVICE_TAX_DTL B "
									+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
									+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.BU_UNIT=B.BU_UNIT AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
							stmt_temp=conn.prepareStatement(queryString_temp);
							stmt_temp.setString(1, "T");
							stmt_temp.setString(2, companyCd);
							stmt_temp.setString(3, countpty_cd);
							stmt_temp.setString(4, plant_seq);
							stmt_temp.setString(5, bu_plant_seq);
							stmt_temp.setString(6, periodStDt);
							rset_temp=stmt_temp.executeQuery();
							if(rset_temp.next())
							{
								VTAX_DTL.add(rset_temp.getString(1)==null?"":rset_temp.getString(1));
							}
							else 
							{
								VTAX_DTL.add("");
							}
							rset_temp.close();
							stmt_temp.close();
							
							int isInvoiceSubmitted=isInvoiceSubmitted_Cargo(countpty_cd, cont, agmt, plant_seq, cont_type, bu_plant_seq, gas_dt,cargo_no,true);
							if(isInvoiceSubmitted>0)
							{
								VNOM_BLOCK.add("Y");
							}
							else
							{
								VNOM_BLOCK.add("N");
							}
							
				  			VNOM_REV_NO.add(rset5.getString(1)==null?"":rset5.getString(1));
				  			VGEN_TIME.add(rset5.getString(2)==null?"":rset5.getString(2));
				  			VBASE.add(rset5.getString(3)==null?"":rset5.getString(3));
				  			VGCV.add(rset5.getString(4)==null?"9802.80":rset5.getString(4));
				  			VNCV.add(rset5.getString(5)==null?"8831.35":rset5.getString(5));
				  			VQTY_MMBTU.add(rset5.getString(6)==null?"":rset5.getString(6));
				  			VQTY_SCM.add(rset5.getString(7)==null?"":rset5.getString(7));
				  			VGEN_DT.add(rset5.getString(8)==null?"":rset5.getString(8));
				  			
				  			VNOM_COLOR.add("#99ffcc");
				  			
				  			VIS_EXIST.add("Y");
				  			
				  			String queryString7="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM "
					  				+ "FROM FMS_BUY_DAILY_SELLER_NOM A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_SELLER_NOM B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				  			stmt4=conn.prepareStatement(queryString7);
							stmt4.setString(1, cont);
							stmt4.setString(2, agmt);
							stmt4.setString(3, companyCd);
							stmt4.setString(4, countpty_cd);
							stmt4.setString(5, transporter_cd);
							stmt4.setString(6, transporter_plant_seq);
							stmt4.setString(7, bu_plant_seq);
							stmt4.setString(8, plant_seq);
							stmt4.setString(9, cont_type);
							stmt4.setString(10, gas_dt);
							stmt4.setString(11, cargo_no);
							rset4=stmt4.executeQuery();
					  		if(rset4.next())
					  		{
					  			VBUYER_NOM_REV_NO.add(rset4.getString(1)==null?"":rset4.getString(1));
					  			VBUYER_NOM.add(rset4.getString(6)==null?"":rset4.getString(6));
					  		}
					  		rset4.close();
					  		stmt4.close();
					  		
					  		String queryString8="SELECT NOM_REV_NO,SEQ_NO,QTY_MMBTU,QTY_SCM,MOLECULE_MAP "
					  				+ "FROM FMS_BUY_DAILY_ALLOCATION_MM A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? AND DTL_CATEGORY='MOL' "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION_MM B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND A.BU_SEQ=B.BU_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
									+ "AND B.GAS_DT=A.GAS_DT AND B.SEQ_NO=A.SEQ_NO AND B.CARGO_NO=A.CARGO_NO AND B.DTL_CATEGORY=A.DTL_CATEGORY) ";
							stmt6 = conn.prepareStatement(queryString8);
							stmt6.setString(1, cont);
							stmt6.setString(2, agmt);
							stmt6.setString(3, companyCd);
							stmt6.setString(4, countpty_cd);
							stmt6.setString(5, transporter_cd);
							stmt6.setString(6, transporter_plant_seq);
							stmt6.setString(7, plant_seq);
							stmt6.setString(8, cont_type);
							stmt6.setString(9, bu_plant_seq);
							stmt6.setString(10, gas_dt);
							stmt6.setString(11, cargo_no);
							rset6=stmt6.executeQuery();
							while(rset6.next())
							{
								mole_count++;
								
								TEMP_MOLE_SEQ_NO.add(rset6.getString(2)==null?"":rset6.getString(2));
								TEMP_MOLE_QTY_MMBTU.add(nf.format(rset6.getDouble(3)));
								TEMP_MOLE_QTY_SCM.add(nf.format(rset6.getDouble(4)));
								TEMP_MOLE_MAP.add(rset6.getString(5)==null?"":rset6.getString(5));
								TEMP_MOLE_EXIST.add("Y");
								TEMP_MOLE_COLOR.add("#99ffcc");
							}
							rset6.close();
							stmt6.close();
							
							//FETCHING LAST DATE FOR AUTO SELECTION
							if(mole_count==0)
							{
								String queryString9="SELECT MOLECULE_MAP,MAX(GAS_DT) "
										+ "FROM FMS_BUY_DAILY_ALLOCATION_MM A "
										+ "WHERE CONT_NO=? AND AGMT_NO=? "
										+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
										+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
										+ "AND GAS_DT < TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? AND DTL_CATEGORY='MOL' "
										+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION_MM B "
										+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
										+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
										+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND A.BU_SEQ=B.BU_SEQ "
										+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
										+ "AND B.GAS_DT=A.GAS_DT AND B.SEQ_NO=A.SEQ_NO AND B.CARGO_NO=A.CARGO_NO AND B.DTL_CATEGORY=A.DTL_CATEGORY) "
										+ "GROUP BY MOLECULE_MAP ";
								stmt8 = conn.prepareStatement(queryString9);
								stmt8.setString(1, cont);
								stmt8.setString(2, agmt);
								stmt8.setString(3, companyCd);
								stmt8.setString(4, countpty_cd);
								stmt8.setString(5, transporter_cd);
								stmt8.setString(6, transporter_plant_seq);
								stmt8.setString(7, plant_seq);
								stmt8.setString(8, cont_type);
								stmt8.setString(9, bu_plant_seq);
								stmt8.setString(10, gas_dt);
								stmt8.setString(11, cargo_no);
								rset8=stmt8.executeQuery();
								while(rset8.next())
								{
									TEMP_MOLE_SEQ_NO.add("");
									TEMP_MOLE_QTY_MMBTU.add("");
									TEMP_MOLE_QTY_SCM.add("");
									TEMP_MOLE_MAP.add(rset8.getString(1)==null?"":rset8.getString(1));
									TEMP_MOLE_EXIST.add("N");
									TEMP_MOLE_COLOR.add("");
								}
								rset8.close();
								stmt8.close();
							}
							
							VALLOC_MOLE_MAPPING.add(TEMP_MOLE_MAP);
							VALLOC_MOLE_SEQ_NO.add(TEMP_MOLE_SEQ_NO);
							VALLOC_MOLE_QTY_MMBTU.add(TEMP_MOLE_QTY_MMBTU);
							VALLOC_MOLE_QTY_SCM.add(TEMP_MOLE_QTY_SCM);
							VALLOC_MOLE_EXIST.add(TEMP_MOLE_EXIST);
							VALLOC_MOLE_COLOR.add(TEMP_MOLE_COLOR);
				  		}
				  		else
				  		{
				  			//GET SELLER NOMINATION DATA FOR SELECTED CONTRACT IF SELLER NOMINATION DONE
				  			String queryString7="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY') "
					  				+ "FROM FMS_BUY_DAILY_SELLER_NOM A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_SELLER_NOM B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				  			stmt4=conn.prepareStatement(queryString7);
							stmt4.setString(1, cont);
							stmt4.setString(2, agmt);
							stmt4.setString(3, companyCd);
							stmt4.setString(4, countpty_cd);
							stmt4.setString(5, transporter_cd);
							stmt4.setString(6, transporter_plant_seq);
							stmt4.setString(7, bu_plant_seq);
							stmt4.setString(8, plant_seq);
							stmt4.setString(9, cont_type);
							stmt4.setString(10, gas_dt);
							stmt4.setString(11, cargo_no);
							rset4=stmt4.executeQuery();
					  		if(rset4.next())
					  		{
					  			VCOUNTERPARTY_CD.add(countpty_cd);
								VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
								VAGMT_NO.add(agmt);
								VAGMT_REV_NO.add(agmt_rev);
								VCONT_NO.add(cont);
								VCONT_REV_NO.add(cont_rev);
								VCONTRACT_TYPE.add(cont_type);
								VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
								VCARGO_NO.add(cargo_no);
								VDIS_CONT_MAPPING.add(cargo_ref_no);
								
								VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
								VCOUNTERPARTY_PLANT_ABBR.add(plant_abbr);
								VBU_CD.add(buCd);
								VBU_PLANT_SEQ.add(bu_plant_seq);
								VBU_PLANT_ABBR.add(bu_plant_abbr);
								
								VDCQ.add(csoc_mmbtu);
								VCONT_REF.add(cont_ref);
								
								VCONT_NAME.add(cont_name);
								VMDCQ_QTY.add(nf.format(mcsoc_qty));
								VINTERNAL_MAP_ID.add(internal_map_id);
								VCOUNTERPATY_STATUS.add(cpStatus);

								String queryString_temp="SELECT TAX_STRUCT_DTL "
										+ "FROM FMS_ENTITY_SERVICE_TAX_DTL A "
										+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND PLANT_SEQ_NO=? AND BU_UNIT=? "
										+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_SERVICE_TAX_DTL B "
										+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
										+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.BU_UNIT=B.BU_UNIT AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
								stmt_temp=conn.prepareStatement(queryString_temp);
								stmt_temp.setString(1, "T");
								stmt_temp.setString(2, companyCd);
								stmt_temp.setString(3, countpty_cd);
								stmt_temp.setString(4, plant_seq);
								stmt_temp.setString(5, bu_plant_seq);
								stmt_temp.setString(6, periodStDt);
								rset_temp=stmt_temp.executeQuery();
								if(rset_temp.next())
								{
									VTAX_DTL.add(rset_temp.getString(1)==null?"":rset_temp.getString(1));
								}
								else 
								{
									VTAX_DTL.add("");
								}
								rset_temp.close();
								stmt_temp.close();
								
								int isInvoiceSubmitted=isInvoiceSubmitted_Cargo(countpty_cd, cont, agmt, plant_seq, cont_type, bu_plant_seq, gas_dt,cargo_no,true);
								if(isInvoiceSubmitted>0)
								{
									VNOM_BLOCK.add("Y");
								}
								else
								{
									VNOM_BLOCK.add("N");
								}
								
								VNOM_REV_NO.add("");
					  			VBUYER_NOM_REV_NO.add(rset4.getString(1)==null?"":rset4.getString(1));
					  			VBASE.add(rset4.getString(3)==null?"":rset4.getString(3));
					  			VGCV.add(rset4.getString(4)==null?"9802.80":rset4.getString(4));
					  			VNCV.add(rset4.getString(5)==null?"8831.35":rset4.getString(5));
					  			VBUYER_NOM.add(rset4.getString(6)==null?"":rset4.getString(6));
					  			VQTY_MMBTU.add(rset4.getString(6)==null?"":rset4.getString(6));
					  			VQTY_SCM.add(rset4.getString(7)==null?"":rset4.getString(7));
					  			
					  			String split[] = dateTime.split(" ");
					  			VGEN_DT.add(split[0]);
					  			VGEN_TIME.add(split[1]);
					  			VNOM_COLOR.add("");
					  			
					  			VIS_EXIST.add("N");
					  			
					  			//FETCHING LAST DATE FOR AUTO SELECTION
								if(mole_count==0)
								{
									String queryString8="SELECT MOLECULE_MAP,MAX(GAS_DT) "
											+ "FROM FMS_BUY_DAILY_ALLOCATION_MM A "
											+ "WHERE CONT_NO=? AND AGMT_NO=? "
											+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
											+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
											+ "AND GAS_DT < TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? AND DTL_CATEGORY='MOL' "
											+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION_MM B "
											+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
											+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
											+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND A.BU_SEQ=B.BU_SEQ "
											+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
											+ "AND B.GAS_DT=A.GAS_DT AND B.SEQ_NO=A.SEQ_NO AND B.CARGO_NO=A.CARGO_NO AND B.DTL_CATEGORY=A.DTL_CATEGORY) "
											+ "GROUP BY MOLECULE_MAP ";
									stmt8 = conn.prepareStatement(queryString8);
									stmt8.setString(1, cont);
									stmt8.setString(2, agmt);
									stmt8.setString(3, companyCd);
									stmt8.setString(4, countpty_cd);
									stmt8.setString(5, transporter_cd);
									stmt8.setString(6, transporter_plant_seq);
									stmt8.setString(7, plant_seq);
									stmt8.setString(8, cont_type);
									stmt8.setString(9, bu_plant_seq);
									stmt8.setString(10, gas_dt);
									stmt8.setString(11, cargo_no);
									rset8=stmt8.executeQuery();
									while(rset8.next())
									{
										TEMP_MOLE_SEQ_NO.add("");
										TEMP_MOLE_QTY_MMBTU.add("");
										TEMP_MOLE_QTY_SCM.add("");
										TEMP_MOLE_MAP.add(rset8.getString(1)==null?"":rset8.getString(1));
										TEMP_MOLE_EXIST.add("N");
										TEMP_MOLE_COLOR.add("");
									}
									rset8.close();
									stmt8.close();
								}
					  			
					  			VALLOC_MOLE_MAPPING.add(TEMP_MOLE_MAP);
								VALLOC_MOLE_SEQ_NO.add(TEMP_MOLE_SEQ_NO);
								VALLOC_MOLE_QTY_MMBTU.add(TEMP_MOLE_QTY_MMBTU);
								VALLOC_MOLE_QTY_SCM.add(TEMP_MOLE_QTY_SCM);
								VALLOC_MOLE_EXIST.add(TEMP_MOLE_EXIST);
								VALLOC_MOLE_COLOR.add(TEMP_MOLE_COLOR);
					  		}
					  		rset4.close();
					  		stmt4.close();
				  		}
				  		rset5.close();
				  		stmt5.close();
					}
					rset2.close();
					stmt2.close();
				}
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
	
	public void getDailyAllocation_bkp()
	{
		String function_nm="getDailyAllocation_bkp()";
		try
		{
			day_total_buyNom_qty=0;
			
			dateTime = dateUtil.getSysdateWithTime24hr();
			
			String queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,CONT_NO,CONT_REV,CONT_REF_NO,TRADE_REF_NO,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),"
					+ "SIGNING_TIME,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_BASE,AGMT_TYPE,TCQ,DCQ,QUANTITY_UNIT,RATE,RATE_UNIT,"
					+ "POST_MARGIN,TRANSPORTATION_CHARGE,SPLIT_FLAG,SPLIT_TYPE,BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,"
					+ "SELLER_NOM_FLAG,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,MDCQ_FLAG,MDCQ_PERCENTAGE,"
					+ "REMARK,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI'),CONT_NAME "
					+ "FROM FMS_TRADER_CONT_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
					+ "AND AGMT_NO=? AND AGMT_REV=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, cont_rev_no);
			stmt.setString(5, contract_type);
			stmt.setString(6, agmt_no);
			stmt.setString(7, agmt_rev_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				//GET SELECTED COUNTERPARTY PLANT
				String queryString1="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_TRADER_CONT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
						+ "AND AGMT_NO=? AND AGMT_REV=?";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, cont_rev_no);
				stmt1.setString(5, contract_type);
				stmt1.setString(6, agmt_no);
				stmt1.setString(7, agmt_rev_no);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String plant_seq = rset1.getString(1)==null?"":rset1.getString(1);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
					
					//GET SELECTED BU PLANT
					String queryString2="SELECT COMPANY_CD,PLANT_SEQ_NO "
							+ "FROM FMS_TRADER_CONT_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=?";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, cont_no);
					stmt2.setString(4, cont_rev_no);
					stmt2.setString(5, contract_type);
					stmt2.setString(6, agmt_no);
					stmt2.setString(7, agmt_rev_no);
					rset2=stmt2.executeQuery();
					while(rset2.next())
					{
						String transporter_cd = "0"; //NOT IN USED 11/10/2022
						String transporter_plant_seq = "0"; //NOT IN USED 11/10/2022
						String buCd = rset2.getString(1)==null?"":rset2.getString(1);
						String bu_plant_seq = rset2.getString(2)==null?"":rset2.getString(2);
						String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,buCd, comp_cd, bu_plant_seq, "B");
						
						//GET DAILY ALLOCATION DATA FOR SELECTED CONTRACT
						String queryString5="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY') "
				  				+ "FROM FMS_BUY_DAILY_ALLOCATION A "
				  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
								+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
								+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
								+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
								+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
								+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
								+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
								+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
						stmt5=conn.prepareStatement(queryString5);
						stmt5.setString(1, cont_no);
						stmt5.setString(2, agmt_no);
						stmt5.setString(3, comp_cd);
						stmt5.setString(4, counterparty_cd);
						stmt5.setString(5, transporter_cd);
						stmt5.setString(6, transporter_plant_seq);
						stmt5.setString(7, bu_plant_seq);
						stmt5.setString(8, plant_seq);
						stmt5.setString(9, contract_type);
						stmt5.setString(10, gas_dt);
						rset5=stmt5.executeQuery();
				  		if(rset5.next())
				  		{
				  			VTRANSPORTER_CD.add(transporter_cd);
							VTRANSPORTER_PLANT_SEQ.add(transporter_plant_seq);
							VTRANSPORTER_PLANT_ABBR.add("");
							VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
							VCOUNTERPARTY_PLANT_ABBR.add(plant_abbr);
							VBU_CD.add(buCd);
							VBU_PLANT_SEQ.add(bu_plant_seq);
							VBU_PLANT_ABBR.add(bu_plant_abbr);
							
				  			VNOM_REV_NO.add(rset5.getString(1)==null?"":rset5.getString(1));
				  			VGEN_TIME.add(rset5.getString(2)==null?"":rset5.getString(2));
				  			VBASE.add(rset5.getString(3)==null?"":rset5.getString(3));
				  			VGCV.add(rset5.getString(4)==null?"9802.80":rset5.getString(4));
				  			VNCV.add(rset5.getString(5)==null?"8831.35":rset5.getString(5));
				  			VQTY_MMBTU.add(rset5.getString(6)==null?"":rset5.getString(6));
				  			VQTY_SCM.add(rset5.getString(7)==null?"":rset5.getString(7));
				  			VGEN_DT.add(rset5.getString(8)==null?"":rset5.getString(8));
				  			
				  			VNOM_COLOR.add("#99ffcc");
				  			
				  			String queryString4="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM "
					  				+ "FROM FMS_BUY_DAILY_SELLER_NOM A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_SELLER_NOM B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				  			stmt4=conn.prepareStatement(queryString4);
							stmt4.setString(1, cont_no);
							stmt4.setString(2, agmt_no);
							stmt4.setString(3, comp_cd);
							stmt4.setString(4, counterparty_cd);
							stmt4.setString(5, transporter_cd);
							stmt4.setString(6, transporter_plant_seq);
							stmt4.setString(7, bu_plant_seq);
							stmt4.setString(8, plant_seq);
							stmt4.setString(9, contract_type);
							stmt4.setString(10, gas_dt);
							rset4=stmt4.executeQuery();
					  		if(rset4.next())
					  		{
					  			VBUYER_NOM_REV_NO.add(rset4.getString(1)==null?"":rset4.getString(1));
					  			VBUYER_NOM.add(rset4.getString(6)==null?"":rset4.getString(6));
					  			
					  			day_total_buyNom_qty+=rset4.getDouble(6);
					  		}
					  		rset4.close();
					  		stmt4.close();
				  		}
				  		else
				  		{
				  			//GET SELLER NOMINATION DATA FOR SELECTED CONTRACT IF SELLER NOMINATION DONE
				  			String queryString4="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY') "
					  				+ "FROM FMS_BUY_DAILY_SELLER_NOM A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_SELLER_NOM B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				  			stmt4=conn.prepareStatement(queryString4);
							stmt4.setString(1, cont_no);
							stmt4.setString(2, agmt_no);
							stmt4.setString(3, comp_cd);
							stmt4.setString(4, counterparty_cd);
							stmt4.setString(5, transporter_cd);
							stmt4.setString(6, transporter_plant_seq);
							stmt4.setString(7, bu_plant_seq);
							stmt4.setString(8, plant_seq);
							stmt4.setString(9, contract_type);
							stmt4.setString(10, gas_dt);
							rset4=stmt4.executeQuery();
					  		if(rset4.next())
					  		{
					  			VTRANSPORTER_CD.add(transporter_cd);
								VTRANSPORTER_PLANT_SEQ.add(transporter_plant_seq);
								VTRANSPORTER_PLANT_ABBR.add("");
								VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
								VCOUNTERPARTY_PLANT_ABBR.add(plant_abbr);
								VBU_CD.add(buCd);
								VBU_PLANT_SEQ.add(bu_plant_seq);
								VBU_PLANT_ABBR.add(bu_plant_abbr);
								
								VNOM_REV_NO.add("");
					  			VBUYER_NOM_REV_NO.add(rset4.getString(1)==null?"":rset4.getString(1));
					  			VBASE.add(rset4.getString(3)==null?"":rset4.getString(3));
					  			VGCV.add(rset4.getString(4)==null?"9802.80":rset4.getString(4));
					  			VNCV.add(rset4.getString(5)==null?"8831.35":rset4.getString(5));
					  			VBUYER_NOM.add(rset4.getString(6)==null?"":rset4.getString(6));
					  			//VQTY_MMBTU.add(rset4.getString(6)==null?"":rset4.getString(6));
					  			//VQTY_SCM.add(rset4.getString(7)==null?"":rset4.getString(7));
					  			VQTY_MMBTU.add("");
					  			VQTY_SCM.add("");
					  			
					  			String split[] = dateTime.split(" ");
					  			VGEN_DT.add(split[0]);
					  			VGEN_TIME.add(split[1]);
					  			VNOM_COLOR.add("");
					  			
					  			day_total_buyNom_qty+=rset4.getDouble(6);
					  		}
					  		rset4.close();
					  		stmt4.close();
				  		}
				  		rset5.close();
				  		stmt5.close();
					}
					rset2.close();
					stmt2.close();
				}
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
	
	public void getWeeklyBuyerNomination()
	{
		String function_nm="getWeeklyBuyerNomination()";
		try
		{
			day_total_buyNom_qty=0;
			
			dateTime = dateUtil.getSysdateWithTime24hr();
			String temp_gen_dt=dateUtil.getDate(gas_dt, "-1");
			
			String[] dtsplit=gas_dt.split("/");
			String month=dtsplit[1];
			String year=dtsplit[2];
			
			if(nomination_freq.equals("W"))
			{
				String queryString = "SELECT TO_CHAR(NEXT_DAY(TO_DATE(?,'DD/MM/YYYY')-7,'MONDAY'),'DD/MM/YYYY') , TO_CHAR(NEXT_DAY(TO_DATE(?,'DD/MM/YYYY')-7,'MONDAY')+6,'DD/MM/YYYY') FROM DUAL";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, gas_dt);
				stmt.setString(2, gas_dt);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					from_date = rset.getString(1)==null?"":rset.getString(1); 
					to_date = rset.getString(2)==null?"":rset.getString(2);
				}
				rset.close();
				stmt.close();
			}
			else if(nomination_freq.equals("M"))
			{
				from_date=""+dateUtil.getFirstDateOfMonth(month, year);
				to_date=""+dateUtil.getLastDateOfMonth(month, year);
			}
			else if(nomination_freq.equals("F"))
			{
				int count=dateUtil.getDays(gas_dt, "15/"+month+"/"+year);
				int count1=dateUtil.getDays(gas_dt, ""+dateUtil.getLastDateOfMonth(month, year));
				
				if(count <= 1)
				{
					from_date= "01/"+month+"/"+year;
					to_date= "15/"+month+"/"+year;
				}
				else if(count1 <= 1)
				{
					from_date= "16/"+month+"/"+year;
					to_date= ""+dateUtil.getLastDateOfMonth(month, year);
				}
			}
			else
			{
				from_date=gas_dt;
				to_date=gas_dt;
			}
						
			int count = dateUtil.getDays(to_date,from_date);
			for(int k=0; k<count; k++)
			{
				String weekofdt="";
				String queryString1 = "SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+?,'DD/MM/YYYY') FROM DUAL";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, from_date);
				stmt1.setInt(2, k);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					weekofdt = rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
				
				String cpStatus = (String) utilBean.getCounterpartyDetails(conn, counterparty_cd, weekofdt).get("status");
				
				String queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,CONT_NO,CONT_REV,CONT_REF_NO,TRADE_REF_NO,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),"
						+ "SIGNING_TIME,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_BASE,AGMT_TYPE,TCQ,DCQ,QUANTITY_UNIT,RATE,RATE_UNIT,"
						+ "POST_MARGIN,TRANSPORTATION_CHARGE,SPLIT_FLAG,SPLIT_TYPE,BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,"
						+ "SELLER_NOM_FLAG,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,MDCQ_FLAG,MDCQ_PERCENTAGE,"
						+ "REMARK,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI'),CONT_NAME "
						+ "FROM FMS_TRADER_CONT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND CONTRACT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY')";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, cont_rev_no);
				stmt.setString(5, contract_type);
				stmt.setString(6, agmt_no);
				stmt.setString(7, agmt_rev_no);
				stmt.setString(8, weekofdt);
				stmt.setString(9, weekofdt);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					VGAS_DATE.add(weekofdt);
					
					VCOUNTERPATY_STATUS.add(cpStatus);
					
					String variable_dcq_qty=utilBean.getPurchaseContVariableDCQ(conn,comp_cd,counterparty_cd, agmt_no, cont_no, contract_type, weekofdt);
					
					if(!variable_dcq_qty.equals(""))
					{
						dcq=variable_dcq_qty;
					}
					
					VDCQ.add(nf.format(Double.parseDouble(""+dcq)));
					VMDCQ.add(nf.format((Double.parseDouble(""+dcq)*Double.parseDouble(""+mdcq_percentage))/100));
					
					String cont_start_dt =rset.getString(9)==null?"":rset.getString(9);
					
					int index=0;
					//GET SELECTED BU PLANT
					String queryString2="SELECT COMPANY_CD,PLANT_SEQ_NO "
							+ "FROM FMS_TRADER_CONT_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=?";
					stmt1=conn.prepareStatement(queryString2);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, cont_no);
					stmt1.setString(4, cont_rev_no);
					stmt1.setString(5, contract_type);
					stmt1.setString(6, agmt_no);
					stmt1.setString(7, agmt_rev_no);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						String buCd = rset1.getString(1)==null?"":rset1.getString(1);
						String bu_plant_seq = rset1.getString(2)==null?"":rset1.getString(2);
						String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,buCd, comp_cd, bu_plant_seq, "B");
						
						VBU_CD.add(buCd);
						VBU_PLANT_SEQ.add(bu_plant_seq);
						VBU_PLANT_ABBR.add(bu_plant_abbr);
						
						index+=1;
						int index1=0;
						
						//GET SELECTED COUNTERPARTY PLANT
						String queryString3="SELECT PLANT_SEQ_NO "
								+ "FROM FMS_TRADER_CONT_PLANT "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
								+ "AND AGMT_NO=? AND AGMT_REV=?";
						stmt2=conn.prepareStatement(queryString3);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, cont_no);
						stmt2.setString(4, cont_rev_no);
						stmt2.setString(5, contract_type);
						stmt2.setString(6, agmt_no);
						stmt2.setString(7, agmt_rev_no);
						rset2=stmt2.executeQuery();
						while(rset2.next())
						{
							index1+=1;
							
							String transporter_cd = "0"; //NOT IN USED 10/11/2022
							String transporter_plant_seq = "0"; //NOT IN USED 10/11/2022
							String plant_seq = rset2.getString(1)==null?"":rset2.getString(1);
							String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
							
							VTRANSPORTER_CD.add(transporter_cd);
							VTRANSPORTER_PLANT_SEQ.add(transporter_plant_seq);
							VTRANSPORTER_PLANT_ABBR.add(""+utilBean.getCounterpartyPlantABBR(conn,transporter_cd, comp_cd, transporter_plant_seq, "R"));
							VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
							VCOUNTERPARTY_PLANT_ABBR.add(plant_abbr);
							
							String billingFrq="";
							String queryString4="SELECT BILLING_FREQ "
									+ "FROM FMS_TRADER_BILLING_DTL "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? AND AGMT_REV=? "
									+ "AND CONT_NO=? AND CONT_REV=? "
									+ "AND CONTRACT_TYPE=?";
							stmt3=conn.prepareStatement(queryString4);
							stmt3.setString(1, comp_cd);
							stmt3.setString(2, counterparty_cd);
							stmt3.setString(3, agmt_no);
							stmt3.setString(4, agmt_rev_no);
							stmt3.setString(5, cont_no);
							stmt3.setString(6, cont_rev_no);
							stmt3.setString(7, contract_type);
							rset3=stmt3.executeQuery();
					  		if(rset3.next())
					  		{
					  			billingFrq=rset3.getString(1)==null?"":rset3.getString(1);
					  		}
					  		rset3.close();
					  		stmt3.close();
					  		
					  		String periodStDt=utilBean.getFirstDtOfBillingCycle(billingFrq, "", weekofdt);
							
							VTAX_DTL.add(utilBean.getEntityTaxStructureDtl(conn,comp_cd, counterparty_cd, "T", plant_seq, bu_plant_seq, periodStDt, "S"));
							
							int isInvoiceSubmitted=isInvoiceSubmitted(counterparty_cd, cont_no, agmt_no, plant_seq, contract_type, bu_plant_seq, weekofdt,false);
							if(isInvoiceSubmitted>0)
							{
								VNOM_BLOCK.add("Y");
							}
							else
							{
								VNOM_BLOCK.add("N");
							}
							
							//GET BUYER NOMINATION DATA FOR SELECTED CONTRACT
							String queryString5="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY') "
					  				+ "FROM FMS_BUY_DAILY_BUYER_NOM A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_BUYER_NOM B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
							stmt4=conn.prepareStatement(queryString5);
							stmt4.setString(1, cont_no);
							stmt4.setString(2, agmt_no);
							stmt4.setString(3, comp_cd);
							stmt4.setString(4, counterparty_cd);
							stmt4.setString(5, transporter_cd);
							stmt4.setString(6, transporter_plant_seq);
							stmt4.setString(7, bu_plant_seq);
							stmt4.setString(8, plant_seq);
							stmt4.setString(9, contract_type);
							stmt4.setString(10, weekofdt);
							rset4=stmt4.executeQuery();
					  		if(rset4.next())
					  		{
					  			VNOM_REV_NO.add(rset4.getString(1)==null?"":rset4.getString(1));
					  			VGEN_TIME.add(rset4.getString(2)==null?"":rset4.getString(2));
					  			VBASE.add(rset4.getString(3)==null?"":rset4.getString(3));
					  			VGCV.add(rset4.getString(4)==null?"9802.80":rset4.getString(4));
					  			VNCV.add(rset4.getString(5)==null?"8831.35":rset4.getString(5));
					  			VQTY_MMBTU.add(rset4.getString(6)==null?"":rset4.getString(6));
					  			VQTY_SCM.add(rset4.getString(7)==null?"":rset4.getString(7));
					  			VGEN_DT.add(rset4.getString(8)==null?"":rset4.getString(8));
					  			
					  			VNOM_COLOR.add("#99ffcc");
					  			
					  			day_total_buyNom_qty+=rset4.getDouble(6);
					  		}
					  		else
					  		{
					  			VNOM_REV_NO.add("");
					  			
					  			String split[] = dateTime.split(" ");
					  			
					  			int tempDays=dateUtil.getDays(dateUtil.getSysdate(), weekofdt);
								if(tempDays<=0)
								{
									temp_gen_dt=dateUtil.getSysdate();
								}
								else
								{
									temp_gen_dt=dateUtil.getDate(weekofdt, "-1");
								}
								
					  			VGEN_TIME.add(split[1]);
					  			//VGEN_DT.add(split[0]);
								VGEN_DT.add(temp_gen_dt);
					  			VBASE.add("GCV");
					  			VGCV.add("9802.80");
					  			VNCV.add("8831.35");
					  			VQTY_MMBTU.add("");
					  			VQTY_SCM.add("");
					  			VNOM_COLOR.add("");
					  		}
					  		rset4.close();
					  		stmt4.close();
						}
						rset2.close();
						stmt2.close();
						
						VINDEX1.add(index1);
					}
					rset1.close();
					stmt1.close();
					
					VINDEX.add(index);
				}
				rset.close();
				stmt.close();
				
				//Cargo Leveled Weekly Buyer Nomination Details
				String queryString2="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CARGO_NO,CARGO_REF,CSOC_QTY"
						+ ",ACTUAL_RECPT_DT, ACTUAL_RECPT_DT + (COALESCE(STORAGE_EXT_DAYS, 0) + COALESCE(STORAGE_DAYS-1, 0)) "
						+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
						+ "WHERE COMPANY_CD=? AND BOE_NO IS NOT NULL AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONTRACT_TYPE=? AND AGMT_NO=? " 
						+ "AND ACTUAL_RECPT_DT<=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND ACTUAL_RECPT_DT + (COALESCE(STORAGE_EXT_DAYS, 0) + COALESCE(STORAGE_DAYS-1, 0)) >= TO_DATE(?,'DD/MM/YYYY') "
						+ "AND BUY_SALE=? AND CARGO_STATUS=? "
						+ "AND A.CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO "  
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO) AND CARGO_NO=? ";
				stmt=conn.prepareStatement(queryString2);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, contract_type);
				stmt.setString(5, agmt_no);
				stmt.setString(6, weekofdt);
				stmt.setString(7, weekofdt);
				stmt.setString(8, "T");
				stmt.setString(9, "Y");
				stmt.setString(10, cargo_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					VGAS_DATE.add(weekofdt);
					
					VCOUNTERPATY_STATUS.add(cpStatus);
					
					String companyCd = rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd = rset.getString(2)==null?"":rset.getString(2);
					String agmt = rset.getString(3)==null?"":rset.getString(3);
					String agmt_rev = rset.getString(4)==null?"":rset.getString(4);
					String cont = rset.getString(5)==null?"":rset.getString(5);
					String cont_rev = rset.getString(6)==null?"":rset.getString(6);
					String cont_type = rset.getString(7)==null?"":rset.getString(7);
					String cargo_no = rset.getString(8)==null?"":rset.getString(8);
					String cargo_ref_no = rset.getString(9)==null?"":rset.getString(9);
					String csoc_mmbtu = dcq;
					
					String variable_csoc_mmbtu=utilBean.getCargoVariableCSOC(conn,companyCd, countpty_cd, "T",agmt, cont, cont_type, cargo_no,weekofdt);
					
					if(!variable_csoc_mmbtu.equals(""))
					{
						csoc_mmbtu=variable_csoc_mmbtu;
					}
					
					String start_dt = rset.getString(11)==null?"":rset.getString(11);
					String end_dt = rset.getString(12)==null?"":rset.getString(12);
					//System.out.println("start_dt==="+start_dt+"  end_dt-----"+end_dt);
					
					VDCQ.add(nf.format(Double.parseDouble(""+csoc_mmbtu)));
					
					double mcsoc_qty=0;
					
					String queryString5 = "SELECT CONT_REF_NO,MDCQ_PERCENTAGE "
							+ "FROM FMS_LTCORA_CONT_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
							+ "AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? "
							+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
					stmt5=conn.prepareStatement(queryString5);
					stmt5.setString(1, companyCd);
					stmt5.setString(2, countpty_cd);
					stmt5.setString(3, agmt);
					stmt5.setString(4, agmt_rev);
					stmt5.setString(5, cont_type);
					stmt5.setString(6, cont);
					rset5=stmt5.executeQuery();
			  		if(rset5.next())
			  		{
			  			String mcsoc_percentage = nf.format(rset5.getDouble(2));
						mcsoc_qty = (Double.parseDouble(csoc_mmbtu)*Double.parseDouble(mcsoc_percentage))/100;
			  		}
			  		rset5.close();
			  		stmt5.close();
			  		
					VMDCQ.add(nf.format(mcsoc_qty));
					
					String cont_start_dt =rset.getString(9)==null?"":rset.getString(9);
					
					int index=0;
					//GET SELECTED BU PLANT
					String queryString3="SELECT COMPANY_CD,PLANT_SEQ_NO "
							+ "FROM FMS_LTCORA_CONT_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=?";
					stmt1=conn.prepareStatement(queryString3);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, cont_no);
					stmt1.setString(4, cont_rev_no);
					stmt1.setString(5, contract_type);
					stmt1.setString(6, agmt_no);
					stmt1.setString(7, agmt_rev_no);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						String buCd = rset1.getString(1)==null?"":rset1.getString(1);
						String bu_plant_seq = rset1.getString(2)==null?"":rset1.getString(2);
						String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,buCd, comp_cd, bu_plant_seq, "B");
						
						VBU_CD.add(buCd);
						VBU_PLANT_SEQ.add(bu_plant_seq);
						VBU_PLANT_ABBR.add(bu_plant_abbr);
						
						index+=1;
						int index1=0;
						
						//GET SELECTED COUNTERPARTY PLANT
						String queryString4="SELECT PLANT_SEQ_NO "
								+ "FROM FMS_LTCORA_CONT_PLANT "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
								+ "AND AGMT_NO=? AND AGMT_REV=?";
						stmt2=conn.prepareStatement(queryString4);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, cont_no);
						stmt2.setString(4, cont_rev_no);
						stmt2.setString(5, contract_type);
						stmt2.setString(6, agmt_no);
						stmt2.setString(7, agmt_rev_no);
						rset2=stmt2.executeQuery();
						while(rset2.next())
						{
							index1+=1;
							
							String transporter_cd = "0"; //NOT IN USED 10/11/2022
							String transporter_plant_seq = "0"; //NOT IN USED 10/11/2022
							String plant_seq = rset2.getString(1)==null?"":rset2.getString(1);
							String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
							
							VTRANSPORTER_CD.add(transporter_cd);
							VTRANSPORTER_PLANT_SEQ.add(transporter_plant_seq);
							VTRANSPORTER_PLANT_ABBR.add(""+utilBean.getCounterpartyPlantABBR(conn,transporter_cd, comp_cd, transporter_plant_seq, "R"));
							VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
							VCOUNTERPARTY_PLANT_ABBR.add(plant_abbr);
							
							String billingFrq="";
							String queryString6="SELECT BILLING_FREQ "
									+ "FROM FMS_LTCORA_CONT_BILLING_DTL "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? AND AGMT_REV=? "
									+ "AND CONT_NO=? AND CONT_REV=? "
									+ "AND CONTRACT_TYPE=?";
							stmt3=conn.prepareStatement(queryString6);
							stmt3.setString(1, comp_cd);
							stmt3.setString(2, counterparty_cd);
							stmt3.setString(3, agmt_no);
							stmt3.setString(4, agmt_rev_no);
							stmt3.setString(5, cont_no);
							stmt3.setString(6, cont_rev_no);
							stmt3.setString(7, contract_type);
							rset3=stmt3.executeQuery();
					  		if(rset3.next())
					  		{
					  			billingFrq=rset3.getString(1)==null?"":rset3.getString(1);
					  		}
					  		rset3.close();
					  		stmt3.close();
					  		
					  		String periodStDt=utilBean.getFirstDtOfBillingCycle(billingFrq, "", weekofdt);
							
					  		String queryString_temp="SELECT TAX_STRUCT_DTL "
									+ "FROM FMS_ENTITY_SERVICE_TAX_DTL A "
									+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND PLANT_SEQ_NO=? AND BU_UNIT=? "
									+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_SERVICE_TAX_DTL B "
									+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
									+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.BU_UNIT=B.BU_UNIT AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
							stmt_temp=conn.prepareStatement(queryString_temp);
							stmt_temp.setString(1, "T");
							stmt_temp.setString(2, companyCd);
							stmt_temp.setString(3, countpty_cd);
							stmt_temp.setString(4, plant_seq);
							stmt_temp.setString(5, bu_plant_seq);
							stmt_temp.setString(6, periodStDt);
							rset_temp=stmt_temp.executeQuery();
							if(rset_temp.next())
							{
								VTAX_DTL.add(rset_temp.getString(1)==null?"":rset_temp.getString(1));
							}
							else 
							{
								VTAX_DTL.add("");
							}
							rset_temp.close();
							stmt_temp.close();
							
							int isInvoiceSubmitted=isInvoiceSubmitted_Cargo(counterparty_cd, cont_no, agmt_no, plant_seq, contract_type, bu_plant_seq, weekofdt,cargo_no,false);
							if(isInvoiceSubmitted>0)
							{
								VNOM_BLOCK.add("Y");
							}
							else
							{
								VNOM_BLOCK.add("N");
							}
							
							//GET BUYER NOMINATION DATA FOR SELECTED CONTRACT
							String queryString7="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY') "
					  				+ "FROM FMS_BUY_DAILY_BUYER_NOM A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_BUYER_NOM B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
							stmt4=conn.prepareStatement(queryString7);
							stmt4.setString(1, cont);
							stmt4.setString(2, agmt);
							stmt4.setString(3, companyCd);
							stmt4.setString(4, countpty_cd);
							stmt4.setString(5, transporter_cd);
							stmt4.setString(6, transporter_plant_seq);
							stmt4.setString(7, bu_plant_seq);
							stmt4.setString(8, plant_seq);
							stmt4.setString(9, cont_type);
							stmt4.setString(10, weekofdt);
							stmt4.setString(11, cargo_no);
							rset4=stmt4.executeQuery();
					  		if(rset4.next())
					  		{
					  			VNOM_REV_NO.add(rset4.getString(1)==null?"":rset4.getString(1));
					  			VGEN_TIME.add(rset4.getString(2)==null?"":rset4.getString(2));
					  			VBASE.add(rset4.getString(3)==null?"":rset4.getString(3));
					  			VGCV.add(rset4.getString(4)==null?"9802.80":rset4.getString(4));
					  			VNCV.add(rset4.getString(5)==null?"8831.35":rset4.getString(5));
					  			VQTY_MMBTU.add(rset4.getString(6)==null?"":rset4.getString(6));
					  			VQTY_SCM.add(rset4.getString(7)==null?"":rset4.getString(7));
					  			VGEN_DT.add(rset4.getString(8)==null?"":rset4.getString(8));
					  			
					  			VNOM_COLOR.add("#99ffcc");
					  			
					  			day_total_buyNom_qty+=rset4.getDouble(6);
					  		}
					  		else
					  		{
					  			VNOM_REV_NO.add("");
					  			
					  			String split[] = dateTime.split(" ");
					  			
					  			int tempDays=dateUtil.getDays(dateUtil.getSysdate(), weekofdt);
								if(tempDays<=0)
								{
									temp_gen_dt=dateUtil.getSysdate();
								}
								else
								{
									temp_gen_dt=dateUtil.getDate(weekofdt, "-1");
								}
								VGEN_DT.add(temp_gen_dt);
					  			//VGEN_DT.add(split[0]);
					  			VGEN_TIME.add(split[1]);
					  			VBASE.add("GCV");
					  			VGCV.add("9802.80");
					  			VNCV.add("8831.35");
					  			VQTY_MMBTU.add("");
					  			VQTY_SCM.add("");
					  			VNOM_COLOR.add("");
					  		}
					  		rset4.close();
					  		stmt4.close();
						}
						rset2.close();
						stmt2.close();
						
						VINDEX1.add(index1);
					}
					rset1.close();
					stmt1.close();
					
					VINDEX.add(index);
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
	
	public void getWeeklySellerNomination()
	{
		String function_nm="getWeeklySellerNomination()";
		try
		{
			day_total_buyNom_qty=0;
			
			dateTime = dateUtil.getSysdateWithTime24hr();
			
			String[] dtsplit=gas_dt.split("/");
			String month=dtsplit[1];
			String year=dtsplit[2];
			
			if(nomination_freq.equals("W"))
			{
				String queryString = "SELECT TO_CHAR(NEXT_DAY(TO_DATE(?,'DD/MM/YYYY')-7,'MONDAY'),'DD/MM/YYYY') , TO_CHAR(NEXT_DAY(TO_DATE(?,'DD/MM/YYYY')-7,'MONDAY')+6,'DD/MM/YYYY') FROM DUAL";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, gas_dt);
				stmt.setString(2, gas_dt);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					from_date = rset.getString(1)==null?"":rset.getString(1); 
					to_date = rset.getString(2)==null?"":rset.getString(2);
				}
				rset.close();
				stmt.close();
			}
			else if(nomination_freq.equals("M"))
			{
				from_date=""+dateUtil.getFirstDateOfMonth(month, year);
				to_date=""+dateUtil.getLastDateOfMonth(month, year);
			}
			else if(nomination_freq.equals("F"))
			{
				int count=dateUtil.getDays(gas_dt, "15/"+month+"/"+year);
				int count1=dateUtil.getDays(gas_dt, ""+dateUtil.getLastDateOfMonth(month, year));
				
				if(count <= 1)
				{
					from_date= "01/"+month+"/"+year;
					to_date= "15/"+month+"/"+year;
				}
				else if(count1 <= 1)
				{
					from_date= "16/"+month+"/"+year;
					to_date= ""+dateUtil.getLastDateOfMonth(month, year);
				}
			}
			else
			{
				from_date=gas_dt;
				to_date=gas_dt;
			}
			
			
			int count = dateUtil.getDays(to_date,from_date);
			for(int k=0; k<count; k++)
			{
				boolean isBuyerNomDone=false;
				
				String weekofdt="";
				String queryString1 = "SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+?,'DD/MM/YYYY') FROM DUAL";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, from_date);
				stmt1.setInt(2, k);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					weekofdt = rset1.getString(1)==null?"":rset1.getString(1);
				}
				
				String cpStatus = (String) utilBean.getCounterpartyDetails(conn, counterparty_cd, weekofdt).get("status");
				
				String queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,CONT_NO,CONT_REV,CONT_REF_NO,TRADE_REF_NO,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),"
						+ "SIGNING_TIME,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_BASE,AGMT_TYPE,TCQ,DCQ,QUANTITY_UNIT,RATE,RATE_UNIT,"
						+ "POST_MARGIN,TRANSPORTATION_CHARGE,SPLIT_FLAG,SPLIT_TYPE,BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,"
						+ "SELLER_NOM_FLAG,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,MDCQ_FLAG,MDCQ_PERCENTAGE,"
						+ "REMARK,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI'),CONT_NAME "
						+ "FROM FMS_TRADER_CONT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND CONTRACT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY')";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, cont_rev_no);
				stmt.setString(5, contract_type);
				stmt.setString(6, agmt_no);
				stmt.setString(7, agmt_rev_no);
				stmt.setString(8, weekofdt);
				stmt.setString(9, weekofdt);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					String cont_start_dt =rset.getString(9)==null?"":rset.getString(9);
					
					int index=0;
					//GET SELECTED BU PLANT
					String queryString2="SELECT COMPANY_CD,PLANT_SEQ_NO "
							+ "FROM FMS_TRADER_CONT_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=?";
					stmt1=conn.prepareStatement(queryString2);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, cont_no);
					stmt1.setString(4, cont_rev_no);
					stmt1.setString(5, contract_type);
					stmt1.setString(6, agmt_no);
					stmt1.setString(7, agmt_rev_no);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						String buCd = rset1.getString(1)==null?"":rset1.getString(1);
						String bu_plant_seq = rset1.getString(2)==null?"":rset1.getString(2);
						String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,buCd, comp_cd, bu_plant_seq, "B");
						
						int index1=0;
						
						//GET SELECTED COUNTERPARTY PLANT
						String queryString3="SELECT PLANT_SEQ_NO "
								+ "FROM FMS_TRADER_CONT_PLANT "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
								+ "AND AGMT_NO=? AND AGMT_REV=?";
						stmt2=conn.prepareStatement(queryString3);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, cont_no);
						stmt2.setString(4, cont_rev_no);
						stmt2.setString(5, contract_type);
						stmt2.setString(6, agmt_no);
						stmt2.setString(7, agmt_rev_no);
						rset2=stmt2.executeQuery();
						while(rset2.next())
						{
							String transporter_cd = "0"; //NOT IN USED 10/11/2022
							String transporter_plant_seq = "0"; //NOT IN USED 10/11/2022
							String plant_seq = rset2.getString(1)==null?"":rset2.getString(1);
							String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
							
							String billingFrq="";
							String queryString4="SELECT BILLING_FREQ "
									+ "FROM FMS_TRADER_BILLING_DTL "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? AND AGMT_REV=? "
									+ "AND CONT_NO=? AND CONT_REV=? "
									+ "AND CONTRACT_TYPE=?";
							stmt3=conn.prepareStatement(queryString4);
							stmt3.setString(1, comp_cd);
							stmt3.setString(2, counterparty_cd);
							stmt3.setString(3, agmt_no);
							stmt3.setString(4, agmt_rev_no);
							stmt3.setString(5, cont_no);
							stmt3.setString(6, cont_rev_no);
							stmt3.setString(7, contract_type);
							rset3=stmt3.executeQuery();
					  		if(rset3.next())
					  		{
					  			billingFrq=rset3.getString(1)==null?"":rset3.getString(1);
					  		}
					  		rset3.close();
					  		stmt3.close();
					  		
					  		String periodStDt=utilBean.getFirstDtOfBillingCycle(billingFrq, "", weekofdt);
							
							//GET SELLER NOMINATION DATA FOR SELECTED CONTRACT
					  		String queryString5="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY') "
					  				+ "FROM FMS_BUY_DAILY_SELLER_NOM A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_SELLER_NOM B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
							stmt5=conn.prepareStatement(queryString5);
							stmt5.setString(1, cont_no);
							stmt5.setString(2, agmt_no);
							stmt5.setString(3, comp_cd);
							stmt5.setString(4, counterparty_cd);
							stmt5.setString(5, transporter_cd);
							stmt5.setString(6, transporter_plant_seq);
							stmt5.setString(7, bu_plant_seq);
							stmt5.setString(8, plant_seq);
							stmt5.setString(9, contract_type);
							stmt5.setString(10, weekofdt);
							rset5=stmt5.executeQuery();
					  		if(rset5.next())
					  		{
					  			isBuyerNomDone=true;
					  			
					  			VNOM_REV_NO.add(rset5.getString(1)==null?"":rset5.getString(1));
					  			VGEN_TIME.add(rset5.getString(2)==null?"":rset5.getString(2));
					  			VBASE.add(rset5.getString(3)==null?"":rset5.getString(3));
					  			VGCV.add(rset5.getString(4)==null?"9802.80":rset5.getString(4));
					  			VNCV.add(rset5.getString(5)==null?"8831.35":rset5.getString(5));
					  			VQTY_MMBTU.add(rset5.getString(6)==null?"":rset5.getString(6));
					  			VQTY_SCM.add(rset5.getString(7)==null?"":rset5.getString(7));
					  			VGEN_DT.add(rset5.getString(8)==null?"":rset5.getString(8));
					  			
					  			VNOM_COLOR.add("#99ffcc");
					  			
					  			String queryString6="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM "
						  				+ "FROM FMS_BUY_DAILY_BUYER_NOM A "
						  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
										+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
										+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
										+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
										+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_BUYER_NOM B "
										+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
										+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
										+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
										+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
										+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
					  			stmt4=conn.prepareStatement(queryString6);
								stmt4.setString(1, cont_no);
								stmt4.setString(2, agmt_no);
								stmt4.setString(3, comp_cd);
								stmt4.setString(4, counterparty_cd);
								stmt4.setString(5, transporter_cd);
								stmt4.setString(6, transporter_plant_seq);
								stmt4.setString(7, bu_plant_seq);
								stmt4.setString(8, plant_seq);
								stmt4.setString(9, contract_type);
								stmt4.setString(10, weekofdt);
								rset4=stmt4.executeQuery();
						  		if(rset4.next())
						  		{
						  			VBUYER_NOM_REV_NO.add(rset4.getString(1)==null?"":rset4.getString(1));
						  			VBUYER_NOM.add(rset4.getString(6)==null?"":rset4.getString(6));
						  		}
						  		rset4.close();
						  		stmt4.close();
					  		}
					  		else
					  		{
					  			String queryString6="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY') "
						  				+ "FROM FMS_BUY_DAILY_BUYER_NOM A "
						  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
										+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
										+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
										+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
										+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_BUYER_NOM B "
										+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
										+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
										+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
										+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
										+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
					  			stmt4=conn.prepareStatement(queryString6);
								stmt4.setString(1, cont_no);
								stmt4.setString(2, agmt_no);
								stmt4.setString(3, comp_cd);
								stmt4.setString(4, counterparty_cd);
								stmt4.setString(5, transporter_cd);
								stmt4.setString(6, transporter_plant_seq);
								stmt4.setString(7, bu_plant_seq);
								stmt4.setString(8, plant_seq);
								stmt4.setString(9, contract_type);
								stmt4.setString(10, weekofdt);
								rset4=stmt4.executeQuery();
						  		if(rset4.next())
						  		{
						  			isBuyerNomDone=true;
						  			
						  			VNOM_REV_NO.add("");
						  			VBUYER_NOM_REV_NO.add(rset4.getString(1)==null?"":rset4.getString(1));
						  			VBASE.add(rset4.getString(3)==null?"":rset4.getString(3));
						  			VGCV.add(rset4.getString(4)==null?"9802.80":rset4.getString(4));
						  			VNCV.add(rset4.getString(5)==null?"8831.35":rset4.getString(5));
						  			VBUYER_NOM.add(rset4.getString(6)==null?"":rset4.getString(6));
						  			VQTY_MMBTU.add(rset4.getString(6)==null?"":rset4.getString(6));
						  			VQTY_SCM.add(rset4.getString(7)==null?"":rset4.getString(7));
						  			
						  			String split[] = dateTime.split(" ");
						  			VGEN_DT.add(split[0]);
						  			VGEN_TIME.add(split[1]);
						  			VNOM_COLOR.add("");
						  		}
						  		else
						  		{
						  			isBuyerNomDone=false;
						  		}
						  		rset4.close();
						  		stmt4.close();
					  		}
					  		rset5.close();
					  		stmt5.close();
					  		
					  		if(isBuyerNomDone)
					  		{
					  			index1+=1;
					  			
					  			VTRANSPORTER_CD.add(transporter_cd);
								VTRANSPORTER_PLANT_SEQ.add(transporter_plant_seq);
								VTRANSPORTER_PLANT_ABBR.add(""+utilBean.getCounterpartyPlantABBR(conn,transporter_cd, comp_cd, transporter_plant_seq, "R"));
								VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
								VCOUNTERPARTY_PLANT_ABBR.add(plant_abbr);
								
								VTAX_DTL.add(utilBean.getEntityTaxStructureDtl(conn,comp_cd, counterparty_cd, "T", plant_seq, bu_plant_seq, periodStDt, "S"));
								
								int isInvoiceSubmitted=isInvoiceSubmitted(counterparty_cd, cont_no, agmt_no, plant_seq, contract_type, bu_plant_seq, weekofdt,false);
								if(isInvoiceSubmitted>0)
								{
									VNOM_BLOCK.add("Y");
								}
								else
								{
									VNOM_BLOCK.add("N");
								}
					  		}
						}
						rset2.close();
						stmt2.close();
						//if(isBuyerNomDone)
						if(index1 > 0)
				  		{
							VBU_CD.add(buCd);
							VBU_PLANT_SEQ.add(bu_plant_seq);
							VBU_PLANT_ABBR.add(bu_plant_abbr);
							
							index+=1;
							VINDEX1.add(index1);
				  		}
					}
					rset1.close();
					stmt1.close();
					//if(isBuyerNomDone)
					if(index>0)
			  		{
						VGAS_DATE.add(weekofdt);
						VCOUNTERPATY_STATUS.add(cpStatus);
						String variable_dcq_qty=utilBean.getPurchaseContVariableDCQ(conn,comp_cd,counterparty_cd, agmt_no, cont_no, contract_type, weekofdt);
						
						if(!variable_dcq_qty.equals(""))
						{
							dcq=variable_dcq_qty;
						}
						
						VDCQ.add(nf.format(Double.parseDouble(""+dcq)));
						VMDCQ.add(nf.format((Double.parseDouble(""+dcq)*Double.parseDouble(""+mdcq_percentage))/100));
					
						VINDEX.add(index);
			  		}
				}
				rset.close();
				stmt.close();
				
				//Cargo Leveled Weekly Seller Nomination

				String queryString2="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CARGO_NO,CARGO_REF,CSOC_QTY "
						+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
						+ "WHERE COMPANY_CD=? AND BOE_NO IS NOT NULL AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONTRACT_TYPE=? AND AGMT_NO=? "
						+ "AND ACTUAL_RECPT_DT<=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND ACTUAL_RECPT_DT + (COALESCE(STORAGE_EXT_DAYS, 0) + COALESCE(STORAGE_DAYS-1, 0)) >= TO_DATE(?,'DD/MM/YYYY') "
						+ "AND BUY_SALE=? AND CARGO_STATUS=? "
						+ "AND A.CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO "  
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO) AND CARGO_NO=? ";
				stmt=conn.prepareStatement(queryString2);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, contract_type);
				stmt.setString(5, agmt_no);
				stmt.setString(6, weekofdt);
				stmt.setString(7, weekofdt);
				stmt.setString(8, "T");
				stmt.setString(9, "Y");
				stmt.setString(10, cargo_no);
				rset=stmt.executeQuery();
				rset=stmt.executeQuery();
				if(rset.next())
				{
					String companyCd = rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd = rset.getString(2)==null?"":rset.getString(2);
					String agmt = rset.getString(3)==null?"":rset.getString(3);
					String agmt_rev = rset.getString(4)==null?"":rset.getString(4);
					String cont = rset.getString(5)==null?"":rset.getString(5);
					String cont_rev = rset.getString(6)==null?"":rset.getString(6);
					String cont_type = rset.getString(7)==null?"":rset.getString(7);
					String cargo_no = rset.getString(8)==null?"":rset.getString(8);
					String cargo_ref_no = rset.getString(9)==null?"":rset.getString(9);
					String csoc_mmbtu = dcq;
					
					String variable_csoc_mmbtu=utilBean.getCargoVariableCSOC(conn,companyCd, countpty_cd, "T",agmt, cont, cont_type, cargo_no,weekofdt);
					
					if(!variable_csoc_mmbtu.equals(""))
					{
						csoc_mmbtu=variable_csoc_mmbtu;
					}
					
					int index=0;
					//GET SELECTED BU PLANT
					String queryString3="SELECT COMPANY_CD,PLANT_SEQ_NO "
							+ "FROM FMS_LTCORA_CONT_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=?";
					stmt1=conn.prepareStatement(queryString3);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, cont_no);
					stmt1.setString(4, cont_rev_no);
					stmt1.setString(5, contract_type);
					stmt1.setString(6, agmt_no);
					stmt1.setString(7, agmt_rev_no);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						String buCd = rset1.getString(1)==null?"":rset1.getString(1);
						String bu_plant_seq = rset1.getString(2)==null?"":rset1.getString(2);
						String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,buCd, comp_cd, bu_plant_seq, "B");
						
						int index1=0;
						
						//GET SELECTED COUNTERPARTY PLANT
						String queryString4="SELECT PLANT_SEQ_NO "
								+ "FROM FMS_LTCORA_CONT_PLANT "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
								+ "AND AGMT_NO=? AND AGMT_REV=?";
						stmt2=conn.prepareStatement(queryString4);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, cont_no);
						stmt2.setString(4, cont_rev_no);
						stmt2.setString(5, contract_type);
						stmt2.setString(6, agmt_no);
						stmt2.setString(7, agmt_rev_no);
						rset2=stmt2.executeQuery();
						while(rset2.next())
						{
							String transporter_cd = "0"; //NOT IN USED 10/11/2022
							String transporter_plant_seq = "0"; //NOT IN USED 10/11/2022
							String plant_seq = rset2.getString(1)==null?"":rset2.getString(1);
							String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
							
							String billingFrq="";
							String queryString5="SELECT BILLING_FREQ "
									+ "FROM FMS_LTCORA_CONT_BILLING_DTL "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? AND AGMT_REV=? "
									+ "AND CONT_NO=? AND CONT_REV=? "
									+ "AND CONTRACT_TYPE=?";
							stmt3=conn.prepareStatement(queryString5);
							stmt3.setString(1, comp_cd);
							stmt3.setString(2, counterparty_cd);
							stmt3.setString(3, agmt_no);
							stmt3.setString(4, agmt_rev_no);
							stmt3.setString(5, cont_no);
							stmt3.setString(6, cont_rev_no);
							stmt3.setString(7, contract_type);
							rset3=stmt3.executeQuery();
					  		if(rset3.next())
					  		{
					  			billingFrq=rset3.getString(1)==null?"":rset3.getString(1);
					  		}
					  		rset3.close();
					  		stmt3.close();
					  		
					  		String periodStDt=utilBean.getFirstDtOfBillingCycle(billingFrq, "", weekofdt);
							
							//GET SELLER NOMINATION DATA FOR SELECTED CONTRACT
					  		String queryString6="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY') "
					  				+ "FROM FMS_BUY_DAILY_SELLER_NOM A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_SELLER_NOM B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
							stmt5=conn.prepareStatement(queryString6);
							stmt5.setString(1, cont_no);
							stmt5.setString(2, agmt_no);
							stmt5.setString(3, comp_cd);
							stmt5.setString(4, counterparty_cd);
							stmt5.setString(5, transporter_cd);
							stmt5.setString(6, transporter_plant_seq);
							stmt5.setString(7, bu_plant_seq);
							stmt5.setString(8, plant_seq);
							stmt5.setString(9, contract_type);
							stmt5.setString(10, weekofdt);
							stmt5.setString(11, cargo_no);
							rset5=stmt5.executeQuery();
					  		if(rset5.next())
					  		{
					  			isBuyerNomDone=true;
					  			
					  			VNOM_REV_NO.add(rset5.getString(1)==null?"":rset5.getString(1));
					  			VGEN_TIME.add(rset5.getString(2)==null?"":rset5.getString(2));
					  			VBASE.add(rset5.getString(3)==null?"":rset5.getString(3));
					  			VGCV.add(rset5.getString(4)==null?"9802.80":rset5.getString(4));
					  			VNCV.add(rset5.getString(5)==null?"8831.35":rset5.getString(5));
					  			VQTY_MMBTU.add(rset5.getString(6)==null?"":rset5.getString(6));
					  			VQTY_SCM.add(rset5.getString(7)==null?"":rset5.getString(7));
					  			VGEN_DT.add(rset5.getString(8)==null?"":rset5.getString(8));
					  			
					  			VNOM_COLOR.add("#99ffcc");
					  			
					  			String queryString7="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM "
						  				+ "FROM FMS_BUY_DAILY_BUYER_NOM A "
						  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
										+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
										+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
										+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
										+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_BUYER_NOM B "
										+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
										+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
										+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
										+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
										+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
					  			stmt4=conn.prepareStatement(queryString7);
								stmt4.setString(1, cont_no);
								stmt4.setString(2, agmt_no);
								stmt4.setString(3, comp_cd);
								stmt4.setString(4, counterparty_cd);
								stmt4.setString(5, transporter_cd);
								stmt4.setString(6, transporter_plant_seq);
								stmt4.setString(7, bu_plant_seq);
								stmt4.setString(8, plant_seq);
								stmt4.setString(9, contract_type);
								stmt4.setString(10, weekofdt);
								stmt4.setString(11, cargo_no);
								rset4=stmt4.executeQuery();
						  		if(rset4.next())
						  		{
						  			VBUYER_NOM_REV_NO.add(rset4.getString(1)==null?"":rset4.getString(1));
						  			VBUYER_NOM.add(rset4.getString(6)==null?"":rset4.getString(6));
						  		}
						  		rset4.close();
						  		stmt4.close();
					  		}
					  		else
					  		{
					  			String queryString7="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY') "
						  				+ "FROM FMS_BUY_DAILY_BUYER_NOM A "
						  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
										+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
										+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
										+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
										+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_BUYER_NOM B "
										+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
										+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
										+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
										+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
										+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
					  			stmt4=conn.prepareStatement(queryString7);
								stmt4.setString(1, cont_no);
								stmt4.setString(2, agmt_no);
								stmt4.setString(3, comp_cd);
								stmt4.setString(4, counterparty_cd);
								stmt4.setString(5, transporter_cd);
								stmt4.setString(6, transporter_plant_seq);
								stmt4.setString(7, bu_plant_seq);
								stmt4.setString(8, plant_seq);
								stmt4.setString(9, contract_type);
								stmt4.setString(10, weekofdt);
								stmt4.setString(11, cargo_no);
								rset4=stmt4.executeQuery();
						  		if(rset4.next())
						  		{
						  			isBuyerNomDone=true;
						  			
						  			VNOM_REV_NO.add("");
						  			VBUYER_NOM_REV_NO.add(rset4.getString(1)==null?"":rset4.getString(1));
						  			VBASE.add(rset4.getString(3)==null?"":rset4.getString(3));
						  			VGCV.add(rset4.getString(4)==null?"9802.80":rset4.getString(4));
						  			VNCV.add(rset4.getString(5)==null?"8831.35":rset4.getString(5));
						  			VBUYER_NOM.add(rset4.getString(6)==null?"":rset4.getString(6));
						  			VQTY_MMBTU.add(rset4.getString(6)==null?"":rset4.getString(6));
						  			VQTY_SCM.add(rset4.getString(7)==null?"":rset4.getString(7));
						  			
						  			String split[] = dateTime.split(" ");
						  			VGEN_DT.add(split[0]);
						  			VGEN_TIME.add(split[1]);
						  			VNOM_COLOR.add("");
						  		}
						  		else
						  		{
						  			isBuyerNomDone=false;
						  		}
						  		rset4.close();
						  		stmt4.close();
					  		}
					  		rset5.close();
					  		stmt5.close();
					  		
					  		if(isBuyerNomDone)
					  		{
					  			index1+=1;
					  			
					  			VTRANSPORTER_CD.add(transporter_cd);
								VTRANSPORTER_PLANT_SEQ.add(transporter_plant_seq);
								VTRANSPORTER_PLANT_ABBR.add(""+utilBean.getCounterpartyPlantABBR(conn,transporter_cd, comp_cd, transporter_plant_seq, "R"));
								VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
								VCOUNTERPARTY_PLANT_ABBR.add(plant_abbr);
								
								String queryString_temp="SELECT TAX_STRUCT_DTL "
										+ "FROM FMS_ENTITY_SERVICE_TAX_DTL A "
										+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND PLANT_SEQ_NO=? AND BU_UNIT=? "
										+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_SERVICE_TAX_DTL B "
										+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
										+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.BU_UNIT=B.BU_UNIT AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
								stmt_temp=conn.prepareStatement(queryString_temp);
								stmt_temp.setString(1, "T");
								stmt_temp.setString(2, companyCd);
								stmt_temp.setString(3, countpty_cd);
								stmt_temp.setString(4, plant_seq);
								stmt_temp.setString(5, bu_plant_seq);
								stmt_temp.setString(6, periodStDt);
								rset_temp=stmt_temp.executeQuery();
								if(rset_temp.next())
								{
									VTAX_DTL.add(rset_temp.getString(1)==null?"":rset_temp.getString(1));
								}
								else 
								{
									VTAX_DTL.add("");
								}
								rset_temp.close();
								stmt_temp.close();
								
								int isInvoiceSubmitted=isInvoiceSubmitted_Cargo(counterparty_cd, cont_no, agmt_no, plant_seq, contract_type, bu_plant_seq, weekofdt,cargo_no,false);
								if(isInvoiceSubmitted>0)
								{
									VNOM_BLOCK.add("Y");
								}
								else
								{
									VNOM_BLOCK.add("N");
								}
					  		}
						}
						rset2.close();
						stmt2.close();
						//if(isBuyerNomDone)
						if(index1 > 0)
				  		{
							VBU_CD.add(buCd);
							VBU_PLANT_SEQ.add(bu_plant_seq);
							VBU_PLANT_ABBR.add(bu_plant_abbr);
							
							index+=1;
							VINDEX1.add(index1);
				  		}
					}
					rset1.close();
					stmt1.close();
					//if(isBuyerNomDone)
					if(index>0)
			  		{
						double mcsoc_qty=0;
						
						String queryString5 = "SELECT CONT_REF_NO,MDCQ_PERCENTAGE "
								+ "FROM FMS_LTCORA_CONT_MST A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
								+ "AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? "
								+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
						stmt5=conn.prepareStatement(queryString5);
						stmt5.setString(1, companyCd);
						stmt5.setString(2, countpty_cd);
						stmt5.setString(3, agmt);
						stmt5.setString(4, agmt_rev);
						stmt5.setString(5, cont_type);
						stmt5.setString(6, cont);
						rset5=stmt5.executeQuery();
				  		if(rset5.next())
				  		{
				  			String mcsoc_percentage = nf.format(rset5.getDouble(2));
							mcsoc_qty = (Double.parseDouble(csoc_mmbtu)*Double.parseDouble(mcsoc_percentage))/100;
				  		}
				  		rset5.close();
				  		stmt5.close();
				  		
						VGAS_DATE.add(weekofdt);
						VCOUNTERPATY_STATUS.add(cpStatus);
						
						if(!variable_csoc_mmbtu.equals(""))
						{
							csoc_mmbtu=variable_csoc_mmbtu;
						}
						
						VDCQ.add(nf.format(Double.parseDouble(""+csoc_mmbtu)));
						VMDCQ.add(nf.format(mcsoc_qty));
					
						VINDEX.add(index);
			  		}
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
	
	public void ReportNominationAllocation()
	{
		String function_nm="ReportNominationAllocation()";
		try
		{
			double totalBuyer_mmbtu = 0;
			double totalBuyer_scm=0;
		    double totalSeller_mmbtu=0;
			double totalSeller_scm=0;
			double totalAlloc_mmbtu=0;
			double totalAlloc_scm=0;
			
			String queryString = "SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY') + ROWNUM - 1,'DD/MM/YYYY') FROM ALL_OBJECTS "
		            +" WHERE ROWNUM <= TO_DATE(?,'DD/MM/YYYY')-TO_DATE(?,'DD/MM/YYYY')+1"; 
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, from_date);
			stmt.setString(2, to_date);
			stmt.setString(3, from_date);
			rset = stmt.executeQuery();
		  	while(rset.next())
		  	{
		  		String gen_dt = rset.getString(1)==null?"":rset.getString(1); 
		  		VGAS_DT.add(gen_dt);
		  		
		  		if(mdcq_percentage.equals(""))
		  		{
		  			mdcq_percentage="100";
		  		}
		  		if(dcq.equals(""))
		  		{
		  			dcq="0";
		  		}
		  		
		  		String variable_dcq = (utilBean.getPurchaseContVariableDCQ(conn,comp_cd,counterparty_cd, agmt_no, cont_no, contract_type, gen_dt));
		  		double mdcq_qty = 0.00;
		  		
		  		if(variable_dcq.equals(""))
		  		{
		  			VDCQ.add(nf.format(Double.parseDouble(dcq)));
		  			mdcq_qty = (Double.parseDouble(dcq)*Double.parseDouble(mdcq_percentage))/100;
		  		}
		  		else
		  		{
		  			VDCQ.add(nf.format(Double.parseDouble(variable_dcq)));
		  			mdcq_qty = (Double.parseDouble(variable_dcq)*Double.parseDouble(mdcq_percentage))/100;
		  		}
		  		VMDCQ.add(nf.format(mdcq_qty));
		  		
		  		int cont=0;
		  		double buyer_mmbtu=0;
		  		double buyer_scm=0;
		  		String queryString1="SELECT SUM(QTY_MMBTU),SUM(QTY_SCM) "
		  				+ "FROM FMS_BUY_DAILY_BUYER_NOM A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONTRACT_TYPE=? "
						+ "AND GAS_DT = TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_BUYER_NOM B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
						+ "AND B.GAS_DT=A.GAS_DT AND A.CARGO_NO=B.CARGO_NO) ";
		  		if(!bu_plant_seq.equals("0"))
		  		{
		  			queryString1+=" AND A.BU_SEQ =? ";
		  		}
		  		if(!plant_seq.equals("0"))
		  		{
		  			queryString1+=" AND A.PLANT_SEQ =? ";
		  		}
		  		stmt1=conn.prepareStatement(queryString1);
		  		stmt1.setString(++cont, cont_no);
		  		stmt1.setString(++cont, agmt_no);
		  		stmt1.setString(++cont, comp_cd);
		  		stmt1.setString(++cont, counterparty_cd);
		  		stmt1.setString(++cont, contract_type);
		  		stmt1.setString(++cont, gen_dt);
		  		stmt1.setString(++cont, cargo_no);
		  		if(!bu_plant_seq.equals("0"))
		  		{
		  			stmt1.setString(++cont, bu_plant_seq);
		  		}
		  		if(!plant_seq.equals("0"))
		  		{
		  			stmt1.setString(++cont, plant_seq);
		  		}
				rset1=stmt1.executeQuery();
		  		if(rset1.next())
		  		{
		  			if(rset1.getDouble(1) > 0)
		  			{
		  				buyer_mmbtu=rset1.getDouble(1);
		  				buyer_scm=rset1.getDouble(2);
			  			VBUYER_QTY_MMBTU.add(nf.format(rset1.getDouble(1)));
			  			VBUYER_QTY_SCM.add(nf.format(rset1.getDouble(2)));
			  			VBUYER_COLOR.add("#99ffcc");
		  			}
		  			else
		  			{
		  				VBUYER_QTY_MMBTU.add("-");
			  			VBUYER_QTY_SCM.add("-");
			  			VBUYER_COLOR.add("");
		  			}
		  		}
		  		else
		  		{
		  			VBUYER_QTY_MMBTU.add("-");
		  			VBUYER_QTY_SCM.add("-");
		  			VBUYER_COLOR.add("");
		  		}
		  		rset1.close();
		  		stmt1.close();
		  		
		  		int cont1=0;
		  		double seller_mmbtu=0;
		  		double seller_scm=0;
		  		String queryString2="SELECT SUM(QTY_MMBTU),SUM(QTY_SCM) "
		  				+ "FROM FMS_BUY_DAILY_SELLER_NOM A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONTRACT_TYPE=? "
						+ "AND GAS_DT = TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_SELLER_NOM B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
						+ "AND B.GAS_DT=A.GAS_DT AND A.CARGO_NO=B.CARGO_NO) ";
		  		if(!bu_plant_seq.equals("0"))
		  		{
		  			queryString2+=" AND A.BU_SEQ =? ";
		  		}
		  		if(!plant_seq.equals("0"))
		  		{
		  			queryString2+=" AND A.PLANT_SEQ =? ";
		  		}
		  		stmt2=conn.prepareStatement(queryString2);
		  		stmt2.setString(++cont1, cont_no);
		  		stmt2.setString(++cont1, agmt_no);
		  		stmt2.setString(++cont1, comp_cd);
		  		stmt2.setString(++cont1, counterparty_cd);
		  		stmt2.setString(++cont1, contract_type);
		  		stmt2.setString(++cont1, gen_dt);
		  		stmt2.setString(++cont1, cargo_no);
		  		if(!bu_plant_seq.equals("0"))
		  		{
		  			stmt2.setString(++cont1, bu_plant_seq);
		  		}
		  		if(!plant_seq.equals("0"))
		  		{
		  			stmt2.setString(++cont1, plant_seq);
		  		}
				rset2=stmt2.executeQuery();
		  		if(rset2.next())
		  		{
		  			if(rset2.getDouble(1) > 0)
		  			{
		  				seller_mmbtu=rset2.getDouble(1);
		  				seller_scm=rset2.getDouble(2);
		  				VSELLER_QTY_MMBTU.add(nf.format(rset2.getDouble(1)));
			  			VSELLER_QTY_SCM.add(nf.format(rset2.getDouble(2)));
			  			VSELLER_COLOR.add("#99ffcc");
		  			}
		  			else
		  			{
		  				VSELLER_QTY_MMBTU.add("-");
			  			VSELLER_QTY_SCM.add("-");
			  			VSELLER_COLOR.add("");
		  			}
		  		}
		  		else
		  		{
		  			VSELLER_QTY_MMBTU.add("-");
		  			VSELLER_QTY_SCM.add("-");
		  			VSELLER_COLOR.add("");
		  		}
		  		rset2.close();
		  		stmt2.close();
		  		
		  		int cont2=0;
		  		double alloc_mmbtu=0;
		  		double alloc_scm=0;
		  		String queryString3="SELECT SUM(QTY_MMBTU),SUM(QTY_SCM) "
		  				+ "FROM FMS_BUY_DAILY_ALLOCATION A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONTRACT_TYPE=? "
						+ "AND GAS_DT = TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
						+ "AND B.GAS_DT=A.GAS_DT AND A.CARGO_NO=B.CARGO_NO) ";
		  		if(!bu_plant_seq.equals("0"))
		  		{
		  			queryString3+=" AND A.BU_SEQ =? ";
		  		}
		  		if(!plant_seq.equals("0"))
		  		{
		  			queryString3+=" AND A.PLANT_SEQ =? ";
		  		}
		  		stmt3=conn.prepareStatement(queryString3);
		  		stmt3.setString(++cont2, cont_no);
		  		stmt3.setString(++cont2, agmt_no);
		  		stmt3.setString(++cont2, comp_cd);
		  		stmt3.setString(++cont2, counterparty_cd);
		  		stmt3.setString(++cont2, contract_type);
		  		stmt3.setString(++cont2, gen_dt);
		  		stmt3.setString(++cont2, cargo_no);
		  		if(!bu_plant_seq.equals("0"))
		  		{
		  			stmt3.setString(++cont2, bu_plant_seq);
		  		}
		  		if(!plant_seq.equals("0"))
		  		{
		  			stmt3.setString(++cont2, plant_seq);
		  		}
				rset3=stmt3.executeQuery();
		  		if(rset3.next())
		  		{
		  			if(rset3.getDouble(1) > 0)
		  			{
		  				alloc_mmbtu=rset3.getDouble(1);
		  				alloc_scm=rset3.getDouble(2);
		  				VQTY_MMBTU.add(nf.format(rset3.getDouble(1)));
			  			VQTY_SCM.add(nf.format(rset3.getDouble(2)));
			  			VCOLOR.add("#99ffcc");
		  			}
		  			else
		  			{
		  				VQTY_MMBTU.add("-");
			  			VQTY_SCM.add("-");
			  			VCOLOR.add("");
		  			}
		  		}
		  		else
		  		{
		  			VQTY_MMBTU.add("-");
		  			VQTY_SCM.add("-");
		  			VCOLOR.add("");
		  		}
		  		rset3.close();
		  		stmt3.close();
		  		
		  		totalBuyer_mmbtu+=buyer_mmbtu;
				totalBuyer_scm+=buyer_scm;
			    totalSeller_mmbtu+=seller_mmbtu;
				totalSeller_scm+=seller_scm;
				totalAlloc_mmbtu+=alloc_mmbtu;
				totalAlloc_scm+=alloc_scm;
		  	}
		  	rset.close();
		  	stmt.close();
		  	
		  	total_buyer_mmbtu=nf.format(totalBuyer_mmbtu);
		  	total_buyer_scm=nf.format(totalBuyer_scm);
		  	total_seller_mmbtu=nf.format(totalSeller_mmbtu);
		  	total_seller_scm=nf.format(totalSeller_scm);
		  	total_alloc_mmbtu=nf.format(totalAlloc_mmbtu);
		  	total_alloc_scm=nf.format(totalAlloc_scm);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getBuyerNomToTrd()
	{
		String function_nm="getBuyerNomToTrd()";
		try
		{
			String queryString = "SELECT TO_CHAR(NEXT_DAY(TO_DATE(?,'DD/MM/YYYY')-7,'MONDAY'),'DD/MM/YYYY') , TO_CHAR(NEXT_DAY(TO_DATE(?,'DD/MM/YYYY')-7,'MONDAY')+6,'DD/MM/YYYY') FROM DUAL";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, report_dt);
			stmt.setString(2, report_dt);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				from_date = rset.getString(1)==null?"":rset.getString(1); 
				to_date = rset.getString(2)==null?"":rset.getString(2);
			}
			rset.close();
			stmt.close();
			
			String queryString1="SELECT COMPANY_CD,COUNTERPARTY_CD,PLANT_SEQ,CONT_NO,AGMT_NO,CONTRACT_TYPE,BU_SEQ,SUM(QTY_MMBTU),SUM(QTY_SCM) "
	  				+ "FROM FMS_BUY_DAILY_BUYER_NOM A "
	  				+ "WHERE COMPANY_CD=? ";
			if(frq_type.equals("W"))
			{
				queryString1+= "AND GAS_DT >= TO_DATE(?,'DD/MM/YYYY') AND GAS_DT <= TO_DATE(?,'DD/MM/YYYY') ";
			}
			else
			{
				queryString1+= "AND GAS_DT = TO_DATE(?,'DD/MM/YYYY') ";
			}
			queryString1+="AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_BUYER_NOM B "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
					+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
					+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) "
					+ "GROUP BY COMPANY_CD,COUNTERPARTY_CD,PLANT_SEQ,CONT_NO,AGMT_NO,CONTRACT_TYPE,BU_SEQ";
			stmt=conn.prepareStatement(queryString1);
			stmt.setString(1, comp_cd);
			if(frq_type.equals("W"))
			{
				stmt.setString(2, from_date);
				stmt.setString(3, to_date);
			}
			else
			{
				stmt.setString(2, report_dt);
			}
	  		rset=stmt.executeQuery();
			while(rset.next())
			{
				String owner_cd = rset.getString(1)==null?"":rset.getString(1);
				String counterpty_cd = rset.getString(2)==null?"":rset.getString(2);
				String counterpty_nm = ""+utilBean.getCounterpartyName(conn,counterpty_cd);
				String counterpty_abbr = ""+utilBean.getCounterpartyABBR(conn,counterpty_cd);
				String plant_seq = rset.getString(3)==null?"":rset.getString(3);
				String contno = rset.getString(4)==null?"":rset.getString(4);
				String agmtno = rset.getString(5)==null?"":rset.getString(5);
				String cont_type = rset.getString(6)==null?"":rset.getString(6);
				String bu_plant_seq = rset.getString(7)==null?"":rset.getString(7);
				
				String contRef="";
				String nom_flg="";
				String plant_abbr="";
				String queryString2="SELECT CONT_REF_NO,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM "
						+ "FROM FMS_TRADER_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_NO=? AND AGMT_NO=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				stmt1=conn.prepareStatement(queryString2);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, cont_type);
				stmt1.setString(3, contno);
				stmt1.setString(4, agmtno);
				stmt1.setString(5, counterpty_cd);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					contRef=rset1.getString(1)==null?"":rset1.getString(1);
					String mth = rset1.getString(2)==null?"N":rset1.getString(2);
					String week = rset1.getString(3)==null?"N":rset1.getString(3);
					String daily = rset1.getString(4)==null?"N":rset1.getString(4);
					
					if(frq_type.equals("M")) {
						nom_flg=mth;
					}else if(frq_type.equals("W")) {
						nom_flg=week;
					}else if(frq_type.equals("D")) {
						nom_flg=daily;
					}
				}
				rset1.close();
				stmt1.close();
				
				if(nom_flg.equals("Y"))
				{
					VCOUNTERPARTY_CD.add(counterpty_cd);
					VCOUNTERPARTY_NM.add(counterpty_nm);
					VCOUNTERPARTY_ABBR.add(counterpty_abbr);
					VCONT_NO.add(contno);
					VAGMT_NO.add(agmtno);
					VCONTRACT_TYPE.add(cont_type);
					
					VPLANT_SEQ.add(plant_seq);
					plant_abbr=""+utilBean.getCounterpartyPlantABBR(conn,counterpty_cd, comp_cd, plant_seq, "T");
					VPLANT_ABBR.add(""+utilBean.getCounterpartyPlantABBR(conn,counterpty_cd, comp_cd, plant_seq, "T"));
					
					VBU_PLANT_SEQ.add(bu_plant_seq);
					VBU_PLANT_ABBR.add(""+utilBean.getCounterpartyPlantABBR(conn,owner_cd, comp_cd, bu_plant_seq, "B"));
					
					VCONT_REF.add(contRef);
					
					Vector temp_seq_no = new Vector();
					Vector temp_contact_person = new Vector();
					String temp_to_selected="0";
					String queryString3="SELECT CONTACT_PERSON, SEQ_NO, TO_NOM "
							+ "FROM FMS_ENTITY_CONTACT_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND ENTITY=? AND NOM_FLAG=? AND ADDR_FLAG=? AND ACTIVE_FLAG=? "
							+ "AND ADDR_IS_ACTIVE=? "
							+ "AND TYPE=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
							+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY') AND A.TYPE=B.TYPE)";
					stmt2=conn.prepareStatement(queryString3);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterpty_cd);
					stmt2.setString(3, "T");
					stmt2.setString(4, "Y");
					stmt2.setString(5, "P"+plant_seq);
					stmt2.setString(6, "Y");
					stmt2.setString(7, "Y");
					stmt2.setString(8, report_dt);
					stmt2.setString(9, "RLNG");
					rset2=stmt2.executeQuery();
					while(rset2.next())
					{
						temp_contact_person.add(rset2.getString(1)==null?"":rset2.getString(1));
						temp_seq_no.add(rset2.getString(2)==null?"":rset2.getString(2));
						String to_nom=rset2.getString(3)==null?"":rset2.getString(3);
						if(to_nom.equals("Y"))
						{
							temp_to_selected=rset2.getString(2)==null?"0":rset2.getString(2);
						}
					}
					rset2.close();
					stmt2.close();
					if(temp_seq_no.size() <=0)
					{
						temp_contact_person.add("-Select-");
						temp_seq_no.add("0");
						temp_to_selected="0";
					}
					
					VTO_CONTACT_PERSON_SEQ_NO.add(temp_seq_no);
					VTO_CONTACT_PERSON.add(temp_contact_person);
					VSEL_TO_CONTACT_PERSON_SEQ_NO.add(temp_to_selected);
					
					Vector temp_seq_no_from = new Vector();
					Vector temp_contact_person_from = new Vector();
					String temp_from_selected="0";
					String queryString4="SELECT CONTACT_PERSON, SEQ_NO, TO_NOM "
							+ "FROM FMS_ENTITY_CONTACT_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND ENTITY=? AND ADDR_FLAG=? AND NOM_FLAG=?  AND ACTIVE_FLAG=? "
							+ "AND ADDR_IS_ACTIVE=? "
							+ "AND TYPE=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
							+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY') AND A.TYPE=B.TYPE)";
					stmt3=conn.prepareStatement(queryString4);
					stmt3.setString(1, comp_cd);
					stmt3.setString(2, owner_cd);
					stmt3.setString(3, "B");
					stmt3.setString(4, "P"+bu_plant_seq);
					stmt3.setString(5, "Y");
					stmt3.setString(6, "Y");
					stmt3.setString(7, "Y");
					stmt3.setString(8, report_dt);
					stmt3.setString(9, "RLNG");
					rset3=stmt3.executeQuery();
					while(rset3.next())
					{
						temp_contact_person_from.add(rset3.getString(1)==null?"":rset3.getString(1));
						temp_seq_no_from.add(rset3.getString(2)==null?"":rset3.getString(2));
						String to_nom=rset3.getString(3)==null?"":rset3.getString(3);
						if(to_nom.equals("Y"))
						{
							temp_from_selected=rset3.getString(2)==null?"0":rset3.getString(2);
						}
					}
					rset3.close();
					stmt3.close();
					if(temp_seq_no_from.size() <=0)
					{
						temp_contact_person_from.add("-Select-");
						temp_seq_no_from.add("0");
						temp_from_selected="0";
					}
					
					VFROM_CONTACT_PERSON_SEQ_NO.add(temp_seq_no_from);
					VFROM_CONTACT_PERSON.add(temp_contact_person_from);
					VSEL_FROM_CONTACT_PERSON_SEQ_NO.add(temp_from_selected);
					
					String rmk="";
					String queryString5 = "SELECT REMARKS "
							+ "FROM FMS_BUY_BUYER_NOM_RMK A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
							+ "AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? AND BU_SEQ=? "
							+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND RMK_REV_NO=(SELECT MAX(B.RMK_REV_NO) FROM FMS_BUY_BUYER_NOM_RMK B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO "
							+ "AND A.BU_SEQ=B.BU_SEQ AND A.GAS_DT=B.GAS_DT)";
					stmt4=conn.prepareStatement(queryString5);
					stmt4.setString(1, owner_cd);
					stmt4.setString(2, counterpty_cd);
					stmt4.setString(3, agmtno);
					stmt4.setString(4, contno);
					stmt4.setString(5, cont_type);
					stmt4.setString(6, plant_seq);
					stmt4.setString(7, bu_plant_seq);
					stmt4.setString(8, report_dt);
					rset4=stmt4.executeQuery();
					if(rset4.next())
					{
						rmk = rset4.getString(1)==null?"":rset4.getString(1);
					}
					rset4.close();
					stmt4.close();
					
					VREMARK.add(rmk);
				}
				String counterparty_nm=""+utilBean.getCounterpartyName(conn,counterpty_cd);
				String counterparty_abbr=""+utilBean.getCounterpartyABBR(conn,counterpty_cd);
				String bu_plantNm = ""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B");
				
				String company_abbr = ""+utilBean.getCompanyAbbr(conn,comp_cd);
				
				String pdf_exists="Y";
				String file_nm=""+company_abbr+"_"+bu_plantNm+"_BUYER_NOM_to_"+counterparty_abbr+"_"+plant_abbr+"_"+cont_type+agmtno+"-"+contno+"-"+report_dt.replaceAll("/", "")+".pdf";
				String directory_path=file_path+File.separator+file_nm;
				
				File pdfFile = new File(directory_path);
				if(!pdfFile.exists())
				{
					pdf_exists="N";
				}
				
				
				VPDF_EXISTS.add(pdf_exists);
				VPDF_FILE_NAME.add(file_nm);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getForthnightlyDateRang()
	{
		String function_nm="getForthnightlyDateRang()";
		try
		{
			int days=dateUtil.getDays(end_dt, start_dt);
			if(days >= 15)
			{
				String sysdt=dateUtil.getSysdate();
				
				int isActive=0;
				String queryString="SELECT COUNT(*) FROM DUAL "
						+ "WHERE TO_DATE(?,'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND TO_DATE(?,'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY')";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, start_dt);
				stmt.setString(2, sysdt);
				stmt.setString(3, end_dt);
				stmt.setString(4, sysdt);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					isActive=rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				int isExpire=0;
				String queryString1="SELECT COUNT(*) FROM DUAL "
						+ "WHERE TO_DATE(?,'DD/MM/YYYY')<TO_DATE(?,'DD/MM/YYYY')";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, end_dt);
				stmt1.setString(2, sysdt);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					isExpire=rset1.getInt(1);
				}
				rset1.close();
				stmt1.close();
				
				int isUpcoming=0;
				String queryString2="SELECT COUNT(*) FROM DUAL "
						+ "WHERE TO_DATE(?,'DD/MM/YYYY')>TO_DATE(?,'DD/MM/YYYY') ";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, start_dt);
				stmt2.setString(2, sysdt);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					isUpcoming=rset2.getInt(1);
				}
				rset2.close();
				stmt2.close();
				
				/*System.out.println("days=="+days);
				System.out.println("isExpire=="+isExpire);
				System.out.println("isUpcoming=="+isUpcoming);
				System.out.println("isActive=="+isActive);
				System.out.println("sysdt=="+sysdt);*/
				
				if(isUpcoming==1)
				{
					sysdt=start_dt;
				}
				else if(isExpire==1)
				{
					sysdt=end_dt;
				}
				
				String month="";
				String year="";
				String day="";
				if(!sysdt.equals(""))
				{
					String[] split=sysdt.split("/");
					day=split[0];
					month=split[1];
					year=split[2];
				}
				
				String period_start_dt="";
				String period_end_dt="";
				
				if(Integer.parseInt(day) > 15)
				{
					period_start_dt="16/"+month+"/"+year;
					period_end_dt=dateUtil.getLastDateOfMonth(month, year);
				}
				else
				{
					period_start_dt="01/"+month+"/"+year;
					period_end_dt="15/"+month+"/"+year;
				}
				
				int after=0;
				String queryString3="SELECT COUNT(*) FROM DUAL "
						+ "WHERE TO_DATE(?,'DD/MM/YYYY')>TO_DATE(?,'DD/MM/YYYY')";
				stmt3=conn.prepareStatement(queryString3);
				stmt3.setString(1, start_dt);
				stmt3.setString(2, period_start_dt);
				rset3=stmt3.executeQuery();
				if(rset3.next())
				{
					after=rset3.getInt(1);
				}
				rset3.close();
				stmt3.close();
				
				int before=0;
				String queryString4="SELECT COUNT(*) FROM DUAL "
						+ "WHERE TO_DATE(?,'DD/MM/YYYY')<TO_DATE(?,'DD/MM/YYYY') ";
				stmt4=conn.prepareStatement(queryString4);
				stmt4.setString(1, end_dt);
				stmt4.setString(2, period_end_dt);
				rset4=stmt4.executeQuery();
				if(rset4.next())
				{
					before=rset4.getInt(1);
				}
				rset4.close();
				stmt4.close();
				
				if(after==1)
				{
					period_start_dt=start_dt;
				}
				if(before==1)
				{
					period_end_dt=end_dt;
				}
				
				forthnighly_start_dt=period_start_dt;
				forthnighly_end_dt=period_end_dt;
			}
			else
			{
				forthnighly_start_dt=start_dt;
				forthnighly_end_dt=end_dt;
			}
			
			if(from_date.equals("") && to_date.equals(""))
			{
				from_date=forthnighly_start_dt;
				to_date=forthnighly_end_dt;
			}
		}
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getPeriodicAllocation()
	{
		String function_nm="getPeriodicAllocation()";
		try
		{
			dateTime = dateUtil.getSysdateWithTime24hr();
			
			String queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
					+ "CONT_REF_NO,DCQ,CONT_NAME,MDCQ_PERCENTAGE,TRADE_REF_NO,TO_CHAR(START_DT,'DD/MM/YYYY') "
					+ "FROM FMS_TRADER_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONT_NO=? AND CONT_REV=?  AND FCC_FLAG=? AND CONTRACT_TYPE=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agmt_rev_no);
			stmt.setString(5, cont_no);
			stmt.setString(6, cont_rev_no);
			stmt.setString(7, "Y");
			stmt.setString(8, contract_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String companyCd = rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd = rset.getString(2)==null?"":rset.getString(2);
				String agmt = rset.getString(3)==null?"":rset.getString(3);
				String agmt_rev = rset.getString(4)==null?"":rset.getString(4);
				String cont = rset.getString(5)==null?"":rset.getString(5);
				String cont_rev = rset.getString(6)==null?"":rset.getString(6);
				String cont_type = rset.getString(7)==null?"":rset.getString(7);
				String cont_ref = rset.getString(8)==null?"":rset.getString(8);
				String dcq = nf.format(rset.getDouble(9));
				
				String variable_dcq_qty=utilBean.getPurchaseContVariableDCQ(conn,companyCd,countpty_cd, agmt, cont, cont_type, gas_dt);
				
				if(!variable_dcq_qty.equals(""))
				{
					dcq=variable_dcq_qty;
				}
				
				String cont_name = rset.getString(10)==null?"":rset.getString(10);
				String mdcq_percentage = nf.format(rset.getDouble(11));
				double mdcq_qty = (Double.parseDouble(dcq)*Double.parseDouble(mdcq_percentage))/100;
				
				String trade_ref = rset.getString(12)==null?"":rset.getString(12);
				if(cont_type.equals("I")) {
					cont_ref=trade_ref;
				}
				
				String internal_map_id = cont_type+"-"+countpty_cd+"-"+agmt+"-"+agmt_rev+"-"+cont+"-"+cont_rev;
				
				String cont_start_dt=rset.getString(13)==null?"":rset.getString(13);
				
				int count = dateUtil.getDays(to_date,from_date);
				for(int k=0; k<count; k++)
				{
					String date="";
					String queryString1 = "SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+?,'DD/MM/YYYY') FROM DUAL";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, from_date);
					stmt1.setInt(2, k);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						date = rset1.getString(1)==null?"":rset1.getString(1);
					}
					rset1.close();
					stmt1.close();
					
					VGAS_DT.add(date);
					
					String cpStatus = (String) utilBean.getCounterpartyDetails(conn, counterparty_cd, date).get("status");
					
					int index=0;
					
					//GET SELECTED BU PLANT
					String queryString2="SELECT COMPANY_CD,PLANT_SEQ_NO "
							+ "FROM FMS_TRADER_CONT_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=?";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, companyCd);
					stmt2.setString(2, countpty_cd);
					stmt2.setString(3, cont);
					stmt2.setString(4, cont_rev);
					stmt2.setString(5, cont_type);
					stmt2.setString(6, agmt);
					stmt2.setString(7, agmt_rev);
					rset2=stmt2.executeQuery();
					while(rset2.next())
					{
						String buCd = rset2.getString(1)==null?"":rset2.getString(1);
						String bu_plant_seq = rset2.getString(2)==null?"":rset2.getString(2);
						String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,buCd, comp_cd, bu_plant_seq, "B");
						
						//GET SELECTED COUNTERPARTY PLANT
						String queryString6="SELECT PLANT_SEQ_NO "
								+ "FROM FMS_TRADER_CONT_PLANT "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
								+ "AND AGMT_NO=? AND AGMT_REV=?";
						stmt1=conn.prepareStatement(queryString6);
						stmt1.setString(1, companyCd);
						stmt1.setString(2, countpty_cd);
						stmt1.setString(3, cont);
						stmt1.setString(4, cont_rev);
						stmt1.setString(5, cont_type);
						stmt1.setString(6, agmt);
						stmt1.setString(7, agmt_rev);
						rset1=stmt1.executeQuery();
						while(rset1.next())
						{
							String plant_seq = rset1.getString(1)==null?"":rset1.getString(1);
							String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, comp_cd, plant_seq, "T");
							
							String transporter_cd = "0"; //NOT IN USED 10/11/2022
							String transporter_plant_seq = "0"; //NOT IN USED 10/11/2022
							
							String billingFrq="";
							String queryString3="SELECT BILLING_FREQ "
									+ "FROM FMS_TRADER_BILLING_DTL "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? AND AGMT_REV=? "
									+ "AND CONT_NO=? AND CONT_REV=? "
									+ "AND CONTRACT_TYPE=?";
							stmt3=conn.prepareStatement(queryString3);
							stmt3.setString(1, companyCd);
							stmt3.setString(2, countpty_cd);
							stmt3.setString(3, agmt);
							stmt3.setString(4, agmt_rev);
							stmt3.setString(5, cont);
							stmt3.setString(6, cont_rev);
							stmt3.setString(7, cont_type);
							rset3=stmt3.executeQuery();
					  		if(rset3.next())
					  		{
					  			billingFrq=rset3.getString(1)==null?"":rset3.getString(1);
					  		}
					  		rset3.close();
					  		stmt3.close();
					  		
					  		String periodStDt=utilBean.getFirstDtOfBillingCycle(billingFrq, "", date);
							
					  		int mole_count=0;
							Vector TEMP_MOLE_MAP=new Vector();
							Vector TEMP_MOLE_SEQ_NO=new Vector();
							Vector TEMP_MOLE_QTY_MMBTU=new Vector();
							Vector TEMP_MOLE_QTY_SCM=new Vector();
							Vector TEMP_MOLE_EXIST=new Vector();
							Vector TEMP_MOLE_COLOR=new Vector();
							
							//GET ALLOCATION DATA
					  		String queryString5="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY') "
					  				+ "FROM FMS_BUY_DAILY_ALLOCATION A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
							stmt5=conn.prepareStatement(queryString5);
							stmt5.setString(1, cont);
							stmt5.setString(2, agmt);
							stmt5.setString(3, companyCd);
							stmt5.setString(4, countpty_cd);
							stmt5.setString(5, transporter_cd);
							stmt5.setString(6, transporter_plant_seq);
							stmt5.setString(7, bu_plant_seq);
							stmt5.setString(8, plant_seq);
							stmt5.setString(9, cont_type);
							stmt5.setString(10, date);
							rset5=stmt5.executeQuery();
					  		if(rset5.next())
					  		{
					  			index+=1;
					  			
					  			VAGMT_NO.add(agmt);
								VAGMT_REV_NO.add(agmt_rev);
								VCONT_NO.add(cont);
								VCONT_REV_NO.add(cont_rev);
								VCONTRACT_TYPE.add(cont_type);
								
								VDIS_CONT_MAPPING.add(utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, ""));
								/*
								if(cont_type.equals("S"))
								{
									VDIS_CONT_MAPPING.add(cont_type+agmt+"-"+cont);
								}
								else
								{
									VDIS_CONT_MAPPING.add(cont_type+""+cont);
								}*/
								VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
								VCOUNTERPATY_STATUS.add(cpStatus);
								VCOUNTERPARTY_PLANT_ABBR.add(plant_abbr);
								VBU_CD.add(buCd);
								VBU_PLANT_SEQ.add(bu_plant_seq);
								VBU_PLANT_ABBR.add(bu_plant_abbr);
								VDCQ.add(dcq);
								VCONT_REF.add(cont_ref);
								VCONT_NAME.add(cont_name);
								VMDCQ_QTY.add(nf.format(mdcq_qty));
								VINTERNAL_MAP_ID.add(internal_map_id);
								VTAX_DTL.add(utilBean.getEntityTaxStructureDtl(conn,companyCd, countpty_cd, "T", plant_seq, bu_plant_seq, periodStDt, "S"));
								
								int isInvoiceSubmitted=isInvoiceSubmitted(countpty_cd, cont, agmt, plant_seq, cont_type, bu_plant_seq, date, true);
								if(isInvoiceSubmitted>0)
								{
									VNOM_BLOCK.add("Y");
								}
								else
								{
									VNOM_BLOCK.add("N");
								}
								
					  			VNOM_REV_NO.add(rset5.getString(1)==null?"":rset5.getString(1));
					  			VGEN_TIME.add(rset5.getString(2)==null?"":rset5.getString(2));
					  			VBASE.add(rset5.getString(3)==null?"":rset5.getString(3));
					  			VGCV.add(rset5.getString(4)==null?"9802.80":rset5.getString(4));
					  			VNCV.add(rset5.getString(5)==null?"8831.35":rset5.getString(5));
					  			VQTY_MMBTU.add(rset5.getString(6)==null?"":rset5.getString(6));
					  			VQTY_SCM.add(rset5.getString(7)==null?"":rset5.getString(7));
					  			VGEN_DT.add(rset5.getString(8)==null?"":rset5.getString(8));
					  			
					  			VNOM_COLOR.add("#99ffcc");
					  			
					  			String queryString4="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM "
						  				+ "FROM FMS_BUY_DAILY_SELLER_NOM A "
						  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
										+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
										+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
										+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
										+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_SELLER_NOM B "
										+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
										+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
										+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
										+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
										+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
					  			stmt4=conn.prepareStatement(queryString4);
								stmt4.setString(1, cont);
								stmt4.setString(2, agmt);
								stmt4.setString(3, companyCd);
								stmt4.setString(4, countpty_cd);
								stmt4.setString(5, transporter_cd);
								stmt4.setString(6, transporter_plant_seq);
								stmt4.setString(7, bu_plant_seq);
								stmt4.setString(8, plant_seq);
								stmt4.setString(9, cont_type);
								stmt4.setString(10, date);
								rset4=stmt4.executeQuery();
						  		if(rset4.next())
						  		{
						  			VBUYER_NOM_REV_NO.add(rset4.getString(1)==null?"":rset4.getString(1));
						  			VBUYER_NOM.add(rset4.getString(6)==null?"":rset4.getString(6));
						  		}
						  		rset4.close();
						  		stmt4.close();
						  		
						  		String queryString7="SELECT NOM_REV_NO,SEQ_NO,QTY_MMBTU,QTY_SCM,MOLECULE_MAP "
						  				+ "FROM FMS_BUY_DAILY_ALLOCATION_MM A "
						  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
										+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
										+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
										+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? AND DTL_CATEGORY='MOL' "
										+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION_MM B "
										+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
										+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
										+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND A.BU_SEQ=B.BU_SEQ "
										+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
										+ "AND B.GAS_DT=A.GAS_DT AND B.SEQ_NO=A.SEQ_NO AND B.CARGO_NO=A.CARGO_NO AND B.DTL_CATEGORY=A.DTL_CATEGORY) ";
								stmt7 = conn.prepareStatement(queryString7);
								stmt7.setString(1, cont);
								stmt7.setString(2, agmt);
								stmt7.setString(3, companyCd);
								stmt7.setString(4, countpty_cd);
								stmt7.setString(5, transporter_cd);
								stmt7.setString(6, transporter_plant_seq);
								stmt7.setString(7, plant_seq);
								stmt7.setString(8, cont_type);
								stmt7.setString(9, bu_plant_seq);
								stmt7.setString(10, date);
								stmt7.setString(11, cargo_no);
								rset7=stmt7.executeQuery();
								while(rset7.next())
								{
									mole_count++;
									
									TEMP_MOLE_SEQ_NO.add(rset7.getString(2)==null?"":rset7.getString(2));
									TEMP_MOLE_QTY_MMBTU.add(nf.format(rset7.getDouble(3)));
									TEMP_MOLE_QTY_SCM.add(nf.format(rset7.getDouble(4)));
									TEMP_MOLE_MAP.add(rset7.getString(5)==null?"":rset7.getString(5));
									TEMP_MOLE_EXIST.add("Y");
									TEMP_MOLE_COLOR.add("#99ffcc");
								}
								rset7.close();
								stmt7.close();
								
								//FETCHING LAST DATE FOR AUTO SELECTION
								if(mole_count==0)
								{
									String queryString8="SELECT MOLECULE_MAP,MAX(GAS_DT) "
											+ "FROM FMS_BUY_DAILY_ALLOCATION_MM A "
											+ "WHERE CONT_NO=? AND AGMT_NO=? "
											+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
											+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
											+ "AND GAS_DT < TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? AND DTL_CATEGORY='MOL' "
											+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION_MM B "
											+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
											+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
											+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND A.BU_SEQ=B.BU_SEQ "
											+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
											+ "AND B.GAS_DT=A.GAS_DT AND B.SEQ_NO=A.SEQ_NO AND B.CARGO_NO=A.CARGO_NO AND B.DTL_CATEGORY=A.DTL_CATEGORY) "
											+ "GROUP BY MOLECULE_MAP ";
									stmt8 = conn.prepareStatement(queryString8);
									stmt8.setString(1, cont);
									stmt8.setString(2, agmt);
									stmt8.setString(3, companyCd);
									stmt8.setString(4, countpty_cd);
									stmt8.setString(5, transporter_cd);
									stmt8.setString(6, transporter_plant_seq);
									stmt8.setString(7, plant_seq);
									stmt8.setString(8, cont_type);
									stmt8.setString(9, bu_plant_seq);
									stmt8.setString(10, date);
									stmt8.setString(11, cargo_no);
									rset8=stmt8.executeQuery();
									while(rset8.next())
									{
										TEMP_MOLE_SEQ_NO.add("");
										TEMP_MOLE_QTY_MMBTU.add("");
										TEMP_MOLE_QTY_SCM.add("");
										TEMP_MOLE_MAP.add(rset8.getString(1)==null?"":rset8.getString(1));
										TEMP_MOLE_EXIST.add("N");
										TEMP_MOLE_COLOR.add("");
									}
									rset8.close();
									stmt8.close();
								}
								
								VIS_EXIST.add("Y");
								
								VALLOC_MOLE_MAPPING.add(TEMP_MOLE_MAP);
								VALLOC_MOLE_SEQ_NO.add(TEMP_MOLE_SEQ_NO);
								VALLOC_MOLE_QTY_MMBTU.add(TEMP_MOLE_QTY_MMBTU);
								VALLOC_MOLE_QTY_SCM.add(TEMP_MOLE_QTY_SCM);
								VALLOC_MOLE_EXIST.add(TEMP_MOLE_EXIST);
								VALLOC_MOLE_COLOR.add(TEMP_MOLE_COLOR);
					  		}
					  		else
					  		{
					  			//GET SELLER NOMINATION DATA FOR SELECTED CONTRACT IF SELLER NOMINATION DONE
					  			String queryString4="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY') "
						  				+ "FROM FMS_BUY_DAILY_SELLER_NOM A "
						  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
										+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
										+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
										+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
										+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_SELLER_NOM B "
										+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
										+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
										+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
										+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
										+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
					  			stmt4=conn.prepareStatement(queryString4);
								stmt4.setString(1, cont);
								stmt4.setString(2, agmt);
								stmt4.setString(3, companyCd);
								stmt4.setString(4, countpty_cd);
								stmt4.setString(5, transporter_cd);
								stmt4.setString(6, transporter_plant_seq);
								stmt4.setString(7, bu_plant_seq);
								stmt4.setString(8, plant_seq);
								stmt4.setString(9, cont_type);
								stmt4.setString(10, date);
								rset4=stmt4.executeQuery();
						  		if(rset4.next())
						  		{
						  			index+=1;
						  			
						  			VAGMT_NO.add(agmt);
									VAGMT_REV_NO.add(agmt_rev);
									VCONT_NO.add(cont);
									VCONT_REV_NO.add(cont_rev);
									VCONTRACT_TYPE.add(cont_type);
									VDIS_CONT_MAPPING.add(utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, ""));
									/*
									if(cont_type.equals("S"))
									{
										VDIS_CONT_MAPPING.add(cont_type+agmt+"-"+cont);
									}
									else
									{
										VDIS_CONT_MAPPING.add(cont_type+""+cont);
									}*/
									VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
									VCOUNTERPARTY_PLANT_ABBR.add(plant_abbr);
									VBU_CD.add(buCd);
									VBU_PLANT_SEQ.add(bu_plant_seq);
									VBU_PLANT_ABBR.add(bu_plant_abbr);
									VDCQ.add(dcq);
									VCONT_REF.add(cont_ref);
									VCONT_NAME.add(cont_name);
									VMDCQ_QTY.add(nf.format(mdcq_qty));
									VINTERNAL_MAP_ID.add(internal_map_id);
									VTAX_DTL.add(utilBean.getEntityTaxStructureDtl(conn,companyCd, countpty_cd, "T", plant_seq, bu_plant_seq, periodStDt, "S"));
									VCOUNTERPATY_STATUS.add(cpStatus);
									
									int isInvoiceSubmitted=isInvoiceSubmitted(countpty_cd, cont, agmt, plant_seq, cont_type, bu_plant_seq, date, true);
									if(isInvoiceSubmitted>0)
									{
										VNOM_BLOCK.add("Y");
									}
									else
									{
										VNOM_BLOCK.add("N");
									}
									
									VNOM_REV_NO.add("");
						  			VBUYER_NOM_REV_NO.add(rset4.getString(1)==null?"":rset4.getString(1));
						  			VBASE.add(rset4.getString(3)==null?"":rset4.getString(3));
						  			VGCV.add(rset4.getString(4)==null?"9802.80":rset4.getString(4));
						  			VNCV.add(rset4.getString(5)==null?"8831.35":rset4.getString(5));
						  			VBUYER_NOM.add(rset4.getString(6)==null?"":rset4.getString(6));
						  			VQTY_MMBTU.add(rset4.getString(6)==null?"":rset4.getString(6));
						  			VQTY_SCM.add(rset4.getString(7)==null?"":rset4.getString(7));
						  			
						  			String split[] = dateTime.split(" ");
						  			VGEN_DT.add(split[0]);
						  			VGEN_TIME.add(split[1]);
						  			VNOM_COLOR.add("");
						  			
						  			VIS_EXIST.add("N");
						  			
						  			//FETCHING LAST DATE FOR AUTO SELECTION
									if(mole_count==0)
									{
										String queryString7="SELECT MOLECULE_MAP,MAX(GAS_DT) "
												+ "FROM FMS_BUY_DAILY_ALLOCATION_MM A "
												+ "WHERE CONT_NO=? AND AGMT_NO=? "
												+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
												+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
												+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
												+ "AND GAS_DT < TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? AND DTL_CATEGORY='MOL' "
												+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION_MM B "
												+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
												+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
												+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND A.BU_SEQ=B.BU_SEQ "
												+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
												+ "AND B.GAS_DT=A.GAS_DT AND B.SEQ_NO=A.SEQ_NO AND B.CARGO_NO=A.CARGO_NO AND B.DTL_CATEGORY=A.DTL_CATEGORY) "
												+ "GROUP BY MOLECULE_MAP ";
										stmt7 = conn.prepareStatement(queryString7);
										stmt7.setString(1, cont);
										stmt7.setString(2, agmt);
										stmt7.setString(3, companyCd);
										stmt7.setString(4, countpty_cd);
										stmt7.setString(5, transporter_cd);
										stmt7.setString(6, transporter_plant_seq);
										stmt7.setString(7, plant_seq);
										stmt7.setString(8, cont_type);
										stmt7.setString(9, bu_plant_seq);
										stmt7.setString(10, date);
										stmt7.setString(11, cargo_no);
										rset7=stmt7.executeQuery();
										while(rset7.next())
										{
											TEMP_MOLE_SEQ_NO.add("");
											TEMP_MOLE_QTY_MMBTU.add("");
											TEMP_MOLE_QTY_SCM.add("");
											TEMP_MOLE_MAP.add(rset7.getString(1)==null?"":rset7.getString(1));
											TEMP_MOLE_EXIST.add("N");
											TEMP_MOLE_COLOR.add("");
										}
										rset7.close();
										stmt7.close();
									}
						  			
						  			VALLOC_MOLE_MAPPING.add(TEMP_MOLE_MAP);
									VALLOC_MOLE_SEQ_NO.add(TEMP_MOLE_SEQ_NO);
									VALLOC_MOLE_QTY_MMBTU.add(TEMP_MOLE_QTY_MMBTU);
									VALLOC_MOLE_QTY_SCM.add(TEMP_MOLE_QTY_SCM);
									VALLOC_MOLE_EXIST.add(TEMP_MOLE_EXIST);
									VALLOC_MOLE_COLOR.add(TEMP_MOLE_COLOR);
						  		}
						  		rset4.close();
						  		stmt4.close();
					  		}
					  		rset5.close();
					  		stmt5.close();
						}
						rset1.close();
						stmt1.close();
					}
					rset2.close();
					stmt2.close();
					
					VINDEX.add(index);
				}
			}
			rset.close();
			stmt.close();
			
			//Cargo Leveled Weekly Allocation
			
			String queryString1="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CARGO_NO,CARGO_REF,CSOC_QTY "
					+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
					+ "WHERE COMPANY_CD=? AND BOE_NO IS NOT NULL "
					+ "AND BUY_SALE=? AND CARGO_STATUS=? AND CONTRACT_TYPE=? "
					+ "AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CARGO_NO=? "
					+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO) ";
			stmt=conn.prepareStatement(queryString1);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "T");//ONLY FOR BUY SIDE(T)
			stmt.setString(3, "Y");
			stmt.setString(4, contract_type);
			stmt.setString(5, counterparty_cd);
			stmt.setString(6, agmt_no);
			stmt.setString(7, cont_no);
			stmt.setString(8, cargo_no);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String companyCd = rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd = rset.getString(2)==null?"":rset.getString(2);
				String agmt = rset.getString(3)==null?"":rset.getString(3);
				String agmt_rev = rset.getString(4)==null?"":rset.getString(4);
				String cont = rset.getString(5)==null?"":rset.getString(5);
				String cont_rev = rset.getString(6)==null?"":rset.getString(6);
				String cont_type = rset.getString(7)==null?"":rset.getString(7);
				String cargo_no = rset.getString(8)==null?"":rset.getString(8);
				String cargo_ref_no = rset.getString(9)==null?"":rset.getString(9);
				String csoc_mmbtu = rset.getString(10)==null?"":rset.getString(10);
				String variable_csoc_mmbtu=utilBean.getCargoVariableCSOC(conn,companyCd, countpty_cd, "T",agmt, cont, cont_type, cargo_no, gas_dt);
				if(!variable_csoc_mmbtu.equals(""))
				{
					csoc_mmbtu=variable_csoc_mmbtu;
				}
				
				double mcsoc_qty =0;
				String cont_ref="";
				
				String internal_map_id = cont_type+"-"+countpty_cd+"-"+agmt+"-"+agmt_rev+"-"+cont+"-"+cont_rev;
				
				int count = dateUtil.getDays(to_date,from_date);
				for(int k=0; k<count; k++)
				{
					String date="";
					String queryString2 = "SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+?,'DD/MM/YYYY') FROM DUAL";
					stmt1=conn.prepareStatement(queryString2);
					stmt1.setString(1, from_date);
					stmt1.setInt(2, k);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						date = rset1.getString(1)==null?"":rset1.getString(1);
					}
					rset1.close();
					stmt1.close();
					
					VGAS_DT.add(date);
					
					String cpStatus = (String) utilBean.getCounterpartyDetails(conn, counterparty_cd, date).get("status");
					
					int index=0;
					
					//GET SELECTED BU PLANT
					String queryString3="SELECT COMPANY_CD,PLANT_SEQ_NO "
							+ "FROM FMS_LTCORA_CONT_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=?";
					stmt2=conn.prepareStatement(queryString3);
					stmt2.setString(1, companyCd);
					stmt2.setString(2, countpty_cd);
					stmt2.setString(3, cont);
					stmt2.setString(4, cont_rev);
					stmt2.setString(5, cont_type);
					stmt2.setString(6, agmt);
					stmt2.setString(7, agmt_rev);
					rset2=stmt2.executeQuery();
					while(rset2.next())
					{
						String buCd = rset2.getString(1)==null?"":rset2.getString(1);
						String bu_plant_seq = rset2.getString(2)==null?"":rset2.getString(2);
						String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,buCd, comp_cd, bu_plant_seq, "B");
						
						//GET SELECTED COUNTERPARTY PLANT
						String queryString4="SELECT PLANT_SEQ_NO "
								+ "FROM FMS_LTCORA_CONT_PLANT "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
								+ "AND AGMT_NO=? AND AGMT_REV=?";
						stmt1=conn.prepareStatement(queryString4);
						stmt1.setString(1, companyCd);
						stmt1.setString(2, countpty_cd);
						stmt1.setString(3, cont);
						stmt1.setString(4, cont_rev);
						stmt1.setString(5, cont_type);
						stmt1.setString(6, agmt);
						stmt1.setString(7, agmt_rev);
						rset1=stmt1.executeQuery();
						while(rset1.next())
						{
							String plant_seq = rset1.getString(1)==null?"":rset1.getString(1);
							String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, comp_cd, plant_seq, "T");
							
							String transporter_cd = "0"; //NOT IN USED 10/11/2022
							String transporter_plant_seq = "0"; //NOT IN USED 10/11/2022
							
							String billingFrq="";
							String queryString5="SELECT BILLING_FREQ "
									+ "FROM FMS_LTCORA_CONT_BILLING_DTL "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? AND AGMT_REV=? "
									+ "AND CONT_NO=? AND CONT_REV=? "
									+ "AND CONTRACT_TYPE=?";
							stmt3=conn.prepareStatement(queryString5);
							stmt3.setString(1, companyCd);
							stmt3.setString(2, countpty_cd);
							stmt3.setString(3, agmt);
							stmt3.setString(4, agmt_rev);
							stmt3.setString(5, cont);
							stmt3.setString(6, cont_rev);
							stmt3.setString(7, cont_type);
							rset3=stmt3.executeQuery();
					  		if(rset3.next())
					  		{
					  			billingFrq=rset3.getString(1)==null?"":rset3.getString(1);
					  		}
					  		rset3.close();
					  		stmt3.close();
					  		
					  		String periodStDt=utilBean.getFirstDtOfBillingCycle(billingFrq, "", date);
							
					  		int mole_count=0;
							Vector TEMP_MOLE_MAP=new Vector();
							Vector TEMP_MOLE_SEQ_NO=new Vector();
							Vector TEMP_MOLE_QTY_MMBTU=new Vector();
							Vector TEMP_MOLE_QTY_SCM=new Vector();
							Vector TEMP_MOLE_EXIST=new Vector();
							Vector TEMP_MOLE_COLOR=new Vector();
							
							//GET ALLOCATION DATA
					  		String queryString6="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY') "
					  				+ "FROM FMS_BUY_DAILY_ALLOCATION A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
							stmt5=conn.prepareStatement(queryString6);
							stmt5.setString(1, cont);
							stmt5.setString(2, agmt);
							stmt5.setString(3, companyCd);
							stmt5.setString(4, countpty_cd);
							stmt5.setString(5, transporter_cd);
							stmt5.setString(6, transporter_plant_seq);
							stmt5.setString(7, bu_plant_seq);
							stmt5.setString(8, plant_seq);
							stmt5.setString(9, cont_type);
							stmt5.setString(10, date);
							stmt5.setString(11, cargo_no);
							rset5=stmt5.executeQuery();
					  		if(rset5.next())
					  		{
					  			index+=1;
					  			
					  			VAGMT_NO.add(agmt);
								VAGMT_REV_NO.add(agmt_rev);
								VCONT_NO.add(cont);
								VCONT_REV_NO.add(cont_rev);
								VCONTRACT_TYPE.add(cont_type);
								VDIS_CONT_MAPPING.add(utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, ""));
								/*
								if(cont_type.equals("S"))
								{
									VDIS_CONT_MAPPING.add(cont_type+agmt+"-"+cont);
								}
								else
								{
									VDIS_CONT_MAPPING.add(cont_type+""+cont);
								}*/
								VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
								VCOUNTERPARTY_PLANT_ABBR.add(plant_abbr);
								VBU_CD.add(buCd);
								VBU_PLANT_SEQ.add(bu_plant_seq);
								VBU_PLANT_ABBR.add(bu_plant_abbr);
								VDCQ.add(dcq);
								VCONT_REF.add(cont_ref);
								VCONT_NAME.add(cont_name);
								VCOUNTERPATY_STATUS.add(cpStatus);
								
								VIS_EXIST.add("Y");
								
								String queryString_temp = "SELECT CONT_REF_NO,MDCQ_PERCENTAGE "
										+ "FROM FMS_LTCORA_CONT_MST A "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
										+ "AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? "
										+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
										+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
								stmt_temp=conn.prepareStatement(queryString_temp);
								stmt_temp.setString(1, companyCd);
								stmt_temp.setString(2, countpty_cd);
								stmt_temp.setString(3, agmt);
								stmt_temp.setString(4, agmt_rev);
								stmt_temp.setString(5, cont_type);
								stmt_temp.setString(6, cont);
								rset_temp=stmt_temp.executeQuery();
						  		if(rset_temp.next())
						  		{
						  			VCONT_REF.add(rset_temp.getString(1)==null?"":rset_temp.getString(1));
						  			
						  			String mcsoc_per = rset_temp.getString(2)==null?"":rset_temp.getString(2);
						  			
						  			mcsoc_qty = (Double.parseDouble(csoc_mmbtu)*Integer.parseInt(mcsoc_per))/100;
									VMDCQ_QTY.add(nf.format(mcsoc_qty));
						  		}
						  		else 
						  		{
						  			VCONT_REF.add("");
						  			VMDCQ_QTY.add("");
						  		}
						  		rset_temp.close();
						  		stmt_temp.close();
								
						  		VINTERNAL_MAP_ID.add(internal_map_id);
						  		
						  		String queryString_temp1="SELECT TAX_STRUCT_DTL "
										+ "FROM FMS_ENTITY_SERVICE_TAX_DTL A "
										+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND PLANT_SEQ_NO=? AND BU_UNIT=? "
										+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_SERVICE_TAX_DTL B "
										+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
										+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.BU_UNIT=B.BU_UNIT AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
								stmt_temp=conn.prepareStatement(queryString_temp1);
								stmt_temp.setString(1, "T");
								stmt_temp.setString(2, companyCd);
								stmt_temp.setString(3, countpty_cd);
								stmt_temp.setString(4, plant_seq);
								stmt_temp.setString(5, bu_plant_seq);
								stmt_temp.setString(6, periodStDt);
								rset_temp=stmt_temp.executeQuery();
								if(rset_temp.next())
								{
									VTAX_DTL.add(rset_temp.getString(1)==null?"":rset_temp.getString(1));
								}
								else 
								{
									VTAX_DTL.add("");
								}
								rset_temp.close();
								stmt_temp.close();
								
								int isInvoiceSubmitted=isInvoiceSubmitted_Cargo(countpty_cd, cont, agmt, plant_seq, cont_type, bu_plant_seq, date,cargo_no,true);
								if(isInvoiceSubmitted>0)
								{
									VNOM_BLOCK.add("Y");
								}
								else
								{
									VNOM_BLOCK.add("N");
								}
								
					  			VNOM_REV_NO.add(rset5.getString(1)==null?"":rset5.getString(1));
					  			VGEN_TIME.add(rset5.getString(2)==null?"":rset5.getString(2));
					  			VBASE.add(rset5.getString(3)==null?"":rset5.getString(3));
					  			VGCV.add(rset5.getString(4)==null?"9802.80":rset5.getString(4));
					  			VNCV.add(rset5.getString(5)==null?"8831.35":rset5.getString(5));
					  			VQTY_MMBTU.add(rset5.getString(6)==null?"":rset5.getString(6));
					  			VQTY_SCM.add(rset5.getString(7)==null?"":rset5.getString(7));
					  			VGEN_DT.add(rset5.getString(8)==null?"":rset5.getString(8));
					  			
					  			VNOM_COLOR.add("#99ffcc");
					  			
					  			String queryString7="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM "
						  				+ "FROM FMS_BUY_DAILY_SELLER_NOM A "
						  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
										+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
										+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
										+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
										+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_SELLER_NOM B "
										+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
										+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
										+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
										+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
										+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
					  			stmt4=conn.prepareStatement(queryString7);
								stmt4.setString(1, cont);
								stmt4.setString(2, agmt);
								stmt4.setString(3, companyCd);
								stmt4.setString(4, countpty_cd);
								stmt4.setString(5, transporter_cd);
								stmt4.setString(6, transporter_plant_seq);
								stmt4.setString(7, bu_plant_seq);
								stmt4.setString(8, plant_seq);
								stmt4.setString(9, cont_type);
								stmt4.setString(10, date);
								stmt4.setString(11, cargo_no);
								rset4=stmt4.executeQuery();
						  		if(rset4.next())
						  		{
						  			VBUYER_NOM_REV_NO.add(rset4.getString(1)==null?"":rset4.getString(1));
						  			VBUYER_NOM.add(rset4.getString(6)==null?"":rset4.getString(6));
						  		}
						  		rset4.close();
						  		stmt4.close();
						  		
						  		String queryString9="SELECT NOM_REV_NO,SEQ_NO,QTY_MMBTU,QTY_SCM,MOLECULE_MAP "
						  				+ "FROM FMS_BUY_DAILY_ALLOCATION_MM A "
						  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
										+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
										+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
										+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? AND DTL_CATEGORY='MOL' "
										+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION_MM B "
										+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
										+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
										+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND A.BU_SEQ=B.BU_SEQ "
										+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
										+ "AND B.GAS_DT=A.GAS_DT AND B.SEQ_NO=A.SEQ_NO AND B.CARGO_NO=A.CARGO_NO AND B.DTL_CATEGORY=A.DTL_CATEGORY) ";
								stmt9 = conn.prepareStatement(queryString9);
								stmt9.setString(1, cont);
								stmt9.setString(2, agmt);
								stmt9.setString(3, companyCd);
								stmt9.setString(4, countpty_cd);
								stmt9.setString(5, transporter_cd);
								stmt9.setString(6, transporter_plant_seq);
								stmt9.setString(7, plant_seq);
								stmt9.setString(8, cont_type);
								stmt9.setString(9, bu_plant_seq);
								stmt9.setString(10, date);
								stmt9.setString(11, cargo_no);
								rset9=stmt9.executeQuery();
								while(rset9.next())
								{
									mole_count++;
									
									TEMP_MOLE_SEQ_NO.add(rset9.getString(2)==null?"":rset9.getString(2));
									TEMP_MOLE_QTY_MMBTU.add(nf.format(rset9.getDouble(3)));
									TEMP_MOLE_QTY_SCM.add(nf.format(rset9.getDouble(4)));
									TEMP_MOLE_MAP.add(rset9.getString(5)==null?"":rset9.getString(5));
									TEMP_MOLE_EXIST.add("Y");
									TEMP_MOLE_COLOR.add("#99ffcc");
								}
								rset9.close();
								stmt9.close();
						  		
						  		//FETCHING LAST DATE FOR AUTO SELECTION
								if(mole_count==0)
								{
									String queryString8="SELECT MOLECULE_MAP,MAX(GAS_DT) "
											+ "FROM FMS_BUY_DAILY_ALLOCATION_MM A "
											+ "WHERE CONT_NO=? AND AGMT_NO=? "
											+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
											+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
											+ "AND GAS_DT < TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? AND DTL_CATEGORY='MOL' "
											+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION_MM B "
											+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
											+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
											+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND A.BU_SEQ=B.BU_SEQ "
											+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
											+ "AND B.GAS_DT=A.GAS_DT AND B.SEQ_NO=A.SEQ_NO AND B.CARGO_NO=A.CARGO_NO AND B.DTL_CATEGORY=A.DTL_CATEGORY) "
											+ "GROUP BY MOLECULE_MAP ";
									stmt8 = conn.prepareStatement(queryString8);
									stmt8.setString(1, cont);
									stmt8.setString(2, agmt);
									stmt8.setString(3, companyCd);
									stmt8.setString(4, countpty_cd);
									stmt8.setString(5, transporter_cd);
									stmt8.setString(6, transporter_plant_seq);
									stmt8.setString(7, plant_seq);
									stmt8.setString(8, cont_type);
									stmt8.setString(9, bu_plant_seq);
									stmt8.setString(10, date);
									stmt8.setString(11, cargo_no);
									rset8=stmt8.executeQuery();
									while(rset8.next())
									{
										TEMP_MOLE_SEQ_NO.add("");
										TEMP_MOLE_QTY_MMBTU.add("");
										TEMP_MOLE_QTY_SCM.add("");
										TEMP_MOLE_MAP.add(rset8.getString(1)==null?"":rset8.getString(1));
										TEMP_MOLE_EXIST.add("N");
										TEMP_MOLE_COLOR.add("");
									}
									rset8.close();
									stmt8.close();
								}

								VALLOC_MOLE_MAPPING.add(TEMP_MOLE_MAP);
								VALLOC_MOLE_SEQ_NO.add(TEMP_MOLE_SEQ_NO);
								VALLOC_MOLE_QTY_MMBTU.add(TEMP_MOLE_QTY_MMBTU);
								VALLOC_MOLE_QTY_SCM.add(TEMP_MOLE_QTY_SCM);
								VALLOC_MOLE_EXIST.add(TEMP_MOLE_EXIST);
								VALLOC_MOLE_COLOR.add(TEMP_MOLE_COLOR);
					  		}
					  		else
					  		{
					  			//GET SELLER NOMINATION DATA FOR SELECTED CONTRACT IF SELLER NOMINATION DONE
					  			String queryString7="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY') "
						  				+ "FROM FMS_BUY_DAILY_SELLER_NOM A "
						  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
										+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? AND BU_SEQ=? "
										+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
										+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
										+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_SELLER_NOM B "
										+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
										+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
										+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
										+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
										+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
					  			stmt4=conn.prepareStatement(queryString7);
								stmt4.setString(1, cont);
								stmt4.setString(2, agmt);
								stmt4.setString(3, companyCd);
								stmt4.setString(4, countpty_cd);
								stmt4.setString(5, transporter_cd);
								stmt4.setString(6, transporter_plant_seq);
								stmt4.setString(7, bu_plant_seq);
								stmt4.setString(8, plant_seq);
								stmt4.setString(9, cont_type);
								stmt4.setString(10, date);
								stmt4.setString(11, cargo_no);
								rset4=stmt4.executeQuery();
						  		if(rset4.next())
						  		{
						  			index+=1;
						  			
						  			VAGMT_NO.add(agmt);
									VAGMT_REV_NO.add(agmt_rev);
									VCONT_NO.add(cont);
									VCONT_REV_NO.add(cont_rev);
									VCONTRACT_TYPE.add(cont_type);
									VDIS_CONT_MAPPING.add(utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, ""));
									/*
									if(cont_type.equals("S"))
									{
										VDIS_CONT_MAPPING.add(cont_type+agmt+"-"+cont);
									}
									else
									{
										VDIS_CONT_MAPPING.add(cont_type+""+cont);
									}*/
									VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
									VCOUNTERPARTY_PLANT_ABBR.add(plant_abbr);
									VBU_CD.add(buCd);
									VBU_PLANT_SEQ.add(bu_plant_seq);
									VBU_PLANT_ABBR.add(bu_plant_abbr);
									VDCQ.add(dcq);
									VCONT_REF.add(cont_ref);
									VCONT_NAME.add(cont_name);
									VCOUNTERPATY_STATUS.add(cpStatus);
									
									String queryString_temp = "SELECT CONT_REF_NO,MDCQ_PERCENTAGE "
											+ "FROM FMS_LTCORA_CONT_MST A "
											+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
											+ "AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? "
											+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
											+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
									stmt_temp=conn.prepareStatement(queryString_temp);
									stmt_temp.setString(1, companyCd);
									stmt_temp.setString(2, countpty_cd);
									stmt_temp.setString(3, agmt);
									stmt_temp.setString(4, agmt_rev);
									stmt_temp.setString(5, cont_type);
									stmt_temp.setString(6, cont);
									rset_temp=stmt_temp.executeQuery();
							  		if(rset_temp.next())
							  		{
							  			VCONT_REF.add(rset_temp.getString(1)==null?"":rset_temp.getString(1));
							  			
							  			String mcsoc_per = rset_temp.getString(2)==null?"":rset_temp.getString(2);
							  			
							  			mcsoc_qty = (Double.parseDouble(csoc_mmbtu)*Integer.parseInt(mcsoc_per))/100;
										VMDCQ_QTY.add(nf.format(mcsoc_qty));
							  		}
							  		else 
							  		{
							  			VCONT_REF.add("");
							  			VMDCQ_QTY.add("");
							  		}
							  		rset_temp.close();
							  		stmt_temp.close();
									
							  		VINTERNAL_MAP_ID.add(internal_map_id);
							  		
							  		String queryString_temp2="SELECT TAX_STRUCT_DTL "
											+ "FROM FMS_ENTITY_SERVICE_TAX_DTL A "
											+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND PLANT_SEQ_NO=? AND BU_UNIT=? "
											+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_SERVICE_TAX_DTL B "
											+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
											+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.BU_UNIT=B.BU_UNIT AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
									stmt_temp=conn.prepareStatement(queryString_temp2);
									stmt_temp.setString(1, "T");
									stmt_temp.setString(2, companyCd);
									stmt_temp.setString(3, countpty_cd);
									stmt_temp.setString(4, plant_seq);
									stmt_temp.setString(5, bu_plant_seq);
									stmt_temp.setString(6, periodStDt);
									rset_temp=stmt_temp.executeQuery();
									if(rset_temp.next())
									{
										VTAX_DTL.add(rset_temp.getString(1)==null?"":rset_temp.getString(1));
									}
									else 
									{
										VTAX_DTL.add("");
									}
									rset_temp.close();
									stmt_temp.close();
									
									int isInvoiceSubmitted=isInvoiceSubmitted_Cargo(countpty_cd, cont, agmt, plant_seq, cont_type, bu_plant_seq, date,cargo_no,true);
									if(isInvoiceSubmitted>0)
									{
										VNOM_BLOCK.add("Y");
									}
									else
									{
										VNOM_BLOCK.add("N");
									}
									
									VNOM_REV_NO.add("");
						  			VBUYER_NOM_REV_NO.add(rset4.getString(1)==null?"":rset4.getString(1));
						  			VBASE.add(rset4.getString(3)==null?"":rset4.getString(3));
						  			VGCV.add(rset4.getString(4)==null?"9802.80":rset4.getString(4));
						  			VNCV.add(rset4.getString(5)==null?"8831.35":rset4.getString(5));
						  			VBUYER_NOM.add(rset4.getString(6)==null?"":rset4.getString(6));
						  			VQTY_MMBTU.add(rset4.getString(6)==null?"":rset4.getString(6));
						  			VQTY_SCM.add(rset4.getString(7)==null?"":rset4.getString(7));
						  			
						  			String split[] = dateTime.split(" ");
						  			VGEN_DT.add(split[0]);
						  			VGEN_TIME.add(split[1]);
						  			VNOM_COLOR.add("");
						  			
						  			VIS_EXIST.add("N");
						  			
						  			//FETCHING LAST DATE FOR AUTO SELECTION
									if(mole_count==0)
									{
										String queryString8="SELECT MOLECULE_MAP,MAX(GAS_DT) "
												+ "FROM FMS_BUY_DAILY_ALLOCATION_MM A "
												+ "WHERE CONT_NO=? AND AGMT_NO=? "
												+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
												+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
												+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
												+ "AND GAS_DT < TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? AND DTL_CATEGORY='MOL' "
												+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION_MM B "
												+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
												+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
												+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND A.BU_SEQ=B.BU_SEQ "
												+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
												+ "AND B.GAS_DT=A.GAS_DT AND B.SEQ_NO=A.SEQ_NO AND B.CARGO_NO=A.CARGO_NO AND B.DTL_CATEGORY=A.DTL_CATEGORY) "
												+ "GROUP BY MOLECULE_MAP ";
										stmt8 = conn.prepareStatement(queryString8);
										stmt8.setString(1, cont);
										stmt8.setString(2, agmt);
										stmt8.setString(3, companyCd);
										stmt8.setString(4, countpty_cd);
										stmt8.setString(5, transporter_cd);
										stmt8.setString(6, transporter_plant_seq);
										stmt8.setString(7, plant_seq);
										stmt8.setString(8, cont_type);
										stmt8.setString(9, bu_plant_seq);
										stmt8.setString(10, date);
										stmt8.setString(11, cargo_no);
										rset8=stmt8.executeQuery();
										while(rset8.next())
										{
											TEMP_MOLE_SEQ_NO.add("");
											TEMP_MOLE_QTY_MMBTU.add("");
											TEMP_MOLE_QTY_SCM.add("");
											TEMP_MOLE_MAP.add(rset8.getString(1)==null?"":rset8.getString(1));
											TEMP_MOLE_EXIST.add("N");
											TEMP_MOLE_COLOR.add("");
										}
										rset8.close();
										stmt8.close();
									}
									
									VALLOC_MOLE_MAPPING.add(TEMP_MOLE_MAP);
									VALLOC_MOLE_SEQ_NO.add(TEMP_MOLE_SEQ_NO);
									VALLOC_MOLE_QTY_MMBTU.add(TEMP_MOLE_QTY_MMBTU);
									VALLOC_MOLE_QTY_SCM.add(TEMP_MOLE_QTY_SCM);
									VALLOC_MOLE_EXIST.add(TEMP_MOLE_EXIST);
									VALLOC_MOLE_COLOR.add(TEMP_MOLE_COLOR);
						  		}
						  		rset4.close();
						  		stmt4.close();
					  		}
					  		rset5.close();
					  		stmt5.close();
						}
						rset1.close();
						stmt1.close();
					}
					rset2.close();
					stmt2.close();
					
					VINDEX.add(index);
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
	
	public void getMoleculeMaster()
	{
		String function_nm="getMoleculeMaster()";
		try
		{
			String queryString="SELECT A.PROD_CD,A.PROD_ABBR,B.MOLE_CD,B.MOLE_ABBR "
					+ "FROM FMS_PRODUCT_MST A, FMS_PRODUCT_MOLECULE_MST B "
					+ "WHERE A.PROD_FLAG='Y' AND B.MOLE_FLAG='Y' AND A.PROD_CD=B.PROD_CD "
					+ "ORDER BY A.PROD_CD, B.MOLE_ABBR";
			stmt=conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String prod_cd=rset.getString(1)==null?"":rset.getString(1);
				VPRODUCT_ABBR.add(rset.getString(2)==null?"":rset.getString(2));
				String mole_cd=rset.getString(3)==null?"":rset.getString(3);
				VMOLECULE_ABBR.add(rset.getString(4)==null?"":rset.getString(4));
				VMOLECULE_MAPPING.add(prod_cd+"-"+mole_cd);
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
	
	String clearance = "";
	String counterparty_cd = "";
	String cont_no = "";
	String cont_rev_no = "";
	String agmt_no = "";
	String agmt_rev_no = "";
	String cargo_no = "0";
	String contract_type = "";
	String gas_dt="";
	String from_date = "";
	String to_date = "";
	String report_dt = "";
	String frq_type = "";
	String plant_seq = "";
	String bu_plant_seq = "";
	String cont_cargo = "";
	String nomination_freq="";
	
	String to_contact = "";
	String from_contact = "";
	String file_path = "";
	
	public void setClearance(String clearance) {this.clearance = clearance;}
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setCont_no(String cont_no) {this.cont_no = cont_no;}
	public void setCont_rev_no(String cont_rev_no) {this.cont_rev_no = cont_rev_no;}
	public void setAgmt_no(String agmt_no) {this.agmt_no = agmt_no;}
	public void setAgmt_rev_no(String agmt_rev_no) {this.agmt_rev_no = agmt_rev_no;}
	public void setCargo_no(String cargo_no) {this.cargo_no = cargo_no;}
	public void setContract_type(String contract_type) {this.contract_type = contract_type;}
	public void setContRef(String contRef) {this.contRef = contRef;} //JD
	public void setGas_dt(String gas_dt) {this.gas_dt = gas_dt;}
	public void setFrom_date(String from_date) {this.from_date = from_date;}
	public void setTo_date(String to_date) {this.to_date = to_date;}
	public void setReport_dt(String report_dt) {this.report_dt = report_dt;}
	public void setFrq_type(String frq_type) {this.frq_type = frq_type;}
	public void setPlant_seq(String plant_seq) {this.plant_seq = plant_seq;}
	public void setBu_plant_seq(String bu_plant_seq) {this.bu_plant_seq = bu_plant_seq;}
	public void setCont_cargo(String cont_cargo) {this.cont_cargo = cont_cargo;}
	public void setNomination_freq(String nomination_freq) {this.nomination_freq = nomination_freq;}
	
	public String getFrom_date() {return from_date;}
	public String getTo_date() {return to_date;}
	
	public void setTo_contact(String to_contact) {this.to_contact = to_contact;}
	public void setFrom_contact(String from_contact) {this.from_contact = from_contact;}
	public void setFile_path(String file_path) {this.file_path = file_path;}
	
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VTRANS_CD = new Vector();
	Vector VTRANS_ABBR = new Vector();
	Vector VSEL_TRANS_CD = new Vector();
	Vector VSEL_TRANS_PLANT_SEQ_NO = new Vector();
	Vector VSEL_TRANS_PLANT_ABBR = new Vector();
	Vector VCOUNTERPATY_STATUS = new Vector();
	
	Vector VTRANSPORTER_CD = new Vector();
	Vector VTRANSPORTER_PLANT_SEQ = new Vector();
	Vector VTRANSPORTER_PLANT_ABBR = new Vector();
	Vector VCOUNTERPARTY_PLANT_SEQ = new Vector();
	Vector VCOUNTERPARTY_PLANT_ABBR = new Vector();
	Vector VBU_CD = new Vector();
	Vector VBU_PLANT_SEQ = new Vector();
	Vector VBU_PLANT_ABBR = new Vector();
	Vector VPLANT_SEQ = new Vector();
	Vector VPLANT_ABBR = new Vector();
	
	Vector VNOM_REV_NO = new Vector();
	Vector VGEN_TIME = new Vector();
	Vector VBASE = new Vector();
	Vector VGCV = new Vector();
	Vector VNCV = new Vector();
	Vector VQTY_MMBTU = new Vector();
	Vector VQTY_SCM = new Vector();
	Vector VNOM_COLOR = new Vector();
	Vector VBUYER_NOM_REV_NO = new Vector();
	Vector VBUYER_NOM = new Vector();
	Vector VGEN_DT = new Vector();
	Vector VGAS_DT = new Vector();
	Vector VBUYER_QTY_MMBTU = new Vector();
	Vector VBUYER_QTY_SCM = new Vector();
	Vector VSELLER_QTY_MMBTU = new Vector();
	Vector VSELLER_QTY_SCM = new Vector();
	Vector VDCQ = new Vector();
	Vector VMDCQ = new Vector();
	Vector VCOLOR = new Vector();
	Vector VBUYER_COLOR = new Vector();
	Vector VSELLER_COLOR = new Vector();
	Vector VCONT_REF = new Vector();
	Vector VTO_CONTACT_PERSON_SEQ_NO = new Vector();
	Vector VSEL_TO_CONTACT_PERSON_SEQ_NO = new Vector();
	Vector VTO_CONTACT_PERSON = new Vector();
	Vector VFROM_CONTACT_PERSON_SEQ_NO = new Vector();
	Vector VSEL_FROM_CONTACT_PERSON_SEQ_NO = new Vector();
	Vector VFROM_CONTACT_PERSON = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VCONT_REV_NO = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VAGMT_REV_NO = new Vector();
	Vector VCONTRACT_TYPE = new Vector();
	Vector VCONTRACT_TYPE_NM = new Vector();
	Vector VREMARK = new Vector();
	Vector VGAS_DATE = new Vector();
	Vector VINDEX = new Vector();
	Vector VINDEX1 = new Vector();
	Vector VSEL_PLANT_SEQ_NO = new Vector();
	Vector VSEL_PLANT_ABBR = new Vector();
	Vector VSEL_BU_CD = new Vector();
	Vector VSEL_BU_PLANT_SEQ_NO = new Vector();
	Vector VSEL_BU_PLANT_ABBR = new Vector();
	Vector VDIS_CONT_MAPPING = new Vector();
	Vector VCONT_NAME = new Vector();
	Vector VMDCQ_QTY = new Vector();
	Vector VINTERNAL_MAP_ID = new Vector();
	Vector VTAX_DTL = new Vector();
	Vector VNOM_BLOCK = new Vector();
	Vector VCARGO_NO = new Vector();
	Vector VIS_EXIST = new Vector();
	
	Vector VPDF_EXISTS = new Vector();
	Vector VMAIL_FROM_LIST = new Vector();
	Vector VMAIL_TO_LIST = new Vector();
	Vector VMAIL_CC_LIST = new Vector();
	Vector VMAIL_SUBJECT = new Vector();
	Vector VMAIL_ATTACHMENT = new Vector();
	Vector VMAIL_ATTACHMENT_PATH = new Vector();
	Vector VMAIL_BODY = new Vector();
	Vector VPDF_FILE_NAME = new Vector();
	
	Vector VPRODUCT_ABBR = new Vector();
	Vector VMOLECULE_ABBR = new Vector();
	Vector VMOLECULE_MAPPING = new Vector();
	
	Vector VALLOC_MOLE_MAPPING = new Vector();
	Vector VALLOC_MOLE_SEQ_NO = new Vector();
	Vector VALLOC_MOLE_QTY_MMBTU = new Vector();
	Vector VALLOC_MOLE_QTY_SCM = new Vector();
	Vector VALLOC_MOLE_EXIST = new Vector();
	Vector VALLOC_MOLE_COLOR = new Vector();
	
	public Vector getVPRODUCT_ABBR() {return VPRODUCT_ABBR;}
	public Vector getVMOLECULE_ABBR() {return VMOLECULE_ABBR;}
	public Vector getVMOLECULE_MAPPING() {return VMOLECULE_MAPPING;}
	
	public Vector getVALLOC_MOLE_MAPPING() {return VALLOC_MOLE_MAPPING;}
	public Vector getVALLOC_MOLE_SEQ_NO() {return VALLOC_MOLE_SEQ_NO;}
	public Vector getVALLOC_MOLE_QTY_MMBTU() {return VALLOC_MOLE_QTY_MMBTU;}
	public Vector getVALLOC_MOLE_QTY_SCM() {return VALLOC_MOLE_QTY_SCM;}
	public Vector getVALLOC_MOLE_EXIST() {return VALLOC_MOLE_EXIST;}
	public Vector getVALLOC_MOLE_COLOR() {return VALLOC_MOLE_COLOR;}

	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVTRANS_CD() {return VTRANS_CD;}
	public Vector getVTRANS_ABBR() {return VTRANS_ABBR;}
	public Vector getVSEL_TRANS_CD() {return VSEL_TRANS_CD;}
	public Vector getVSEL_TRANS_PLANT_SEQ_NO() {return VSEL_TRANS_PLANT_SEQ_NO;}
	public Vector getVSEL_TRANS_PLANT_ABBR() {return VSEL_TRANS_PLANT_ABBR;}
	public Vector getVCOUNTERPATY_STATUS() {return VCOUNTERPATY_STATUS;}
	
	public Vector getVTRANSPORTER_CD() {return VTRANSPORTER_CD;}
	public Vector getVTRANSPORTER_PLANT_SEQ() {return VTRANSPORTER_PLANT_SEQ;}
	public Vector getVTRANSPORTER_PLANT_ABBR() {return VTRANSPORTER_PLANT_ABBR;}
	public Vector getVCOUNTERPARTY_PLANT_SEQ() {return VCOUNTERPARTY_PLANT_SEQ;}
	public Vector getVCOUNTERPARTY_PLANT_ABBR() {return VCOUNTERPARTY_PLANT_ABBR;}
	public Vector getVBU_CD() {return VBU_CD;}
	public Vector getVBU_PLANT_SEQ() {return VBU_PLANT_SEQ;}
	public Vector getVBU_PLANT_ABBR() {return VBU_PLANT_ABBR;}
	public Vector getVPLANT_SEQ() {return VPLANT_SEQ;}
	public Vector getVPLANT_ABBR() {return VPLANT_ABBR;}
	
	public Vector getVNOM_REV_NO() {return VNOM_REV_NO;}
	public Vector getVGEN_TIME() {return VGEN_TIME;}
	public Vector getVBASE() {return VBASE;}
	public Vector getVGCV() {return VGCV;}
	public Vector getVNCV() {return VNCV;}
	public Vector getVQTY_MMBTU() {return VQTY_MMBTU;}
	public Vector getVQTY_SCM() {return VQTY_SCM;}
	public Vector getVNOM_COLOR() {return VNOM_COLOR;}
	public Vector getVBUYER_NOM_REV_NO() {return VBUYER_NOM_REV_NO;}
	public Vector getVBUYER_NOM() {return VBUYER_NOM;}
	public Vector getVGEN_DT() {return VGEN_DT;}
	public Vector getVGAS_DT() {return VGAS_DT;}
	public Vector getVBUYER_QTY_MMBTU() {return VBUYER_QTY_MMBTU;}
	public Vector getVBUYER_QTY_SCM() {return VBUYER_QTY_SCM;}
	public Vector getVSELLER_QTY_MMBTU() {return VSELLER_QTY_MMBTU;}
	public Vector getVSELLER_QTY_SCM() {return VSELLER_QTY_SCM;}
	public Vector getVDCQ() {return VDCQ;}
	public Vector getVMDCQ() {return VMDCQ;}
	public Vector getVCOLOR() {return VCOLOR;}
	public Vector getVBUYER_COLOR() {return VBUYER_COLOR;}
	public Vector getVSELLER_COLOR() {return VSELLER_COLOR;}
	public Vector getVCONT_REF() {return VCONT_REF;}
	public Vector getVTO_CONTACT_PERSON_SEQ_NO() {return VTO_CONTACT_PERSON_SEQ_NO;}
	public Vector getVSEL_TO_CONTACT_PERSON_SEQ_NO() {return VSEL_TO_CONTACT_PERSON_SEQ_NO;}
	public Vector getVTO_CONTACT_PERSON() {return VTO_CONTACT_PERSON;}
	public Vector getVFROM_CONTACT_PERSON_SEQ_NO() {return VFROM_CONTACT_PERSON_SEQ_NO;}
	public Vector getVSEL_FROM_CONTACT_PERSON_SEQ_NO() {return VSEL_FROM_CONTACT_PERSON_SEQ_NO;}
	public Vector getVFROM_CONTACT_PERSON() {return VFROM_CONTACT_PERSON;}
	public Vector getVCONT_NO() {return VCONT_NO;}
	public Vector getVCONT_REV_NO() {return VCONT_REV_NO;}
	public Vector getVAGMT_NO() {return VAGMT_NO;}
	public Vector getVAGMT_REV_NO() {return VAGMT_REV_NO;}
	public Vector getVCONTRACT_TYPE() {return VCONTRACT_TYPE;}
	public Vector getVCONTRACT_TYPE_NM() {return VCONTRACT_TYPE_NM;}
	public Vector getVREMARK() {return VREMARK;}
	public Vector getVGAS_DATE() {return VGAS_DATE;}
	public Vector getVINDEX() {return VINDEX;}
	public Vector getVINDEX1() {return VINDEX1;}
	public Vector getVSEL_PLANT_SEQ_NO() {return VSEL_PLANT_SEQ_NO;}
	public Vector getVSEL_PLANT_ABBR() {return VSEL_PLANT_ABBR;}
	public Vector getVSEL_BU_CD() {return VSEL_BU_CD;}
	public Vector getVSEL_BU_PLANT_SEQ_NO() {return VSEL_BU_PLANT_SEQ_NO;}
	public Vector getVSEL_BU_PLANT_ABBR() {return VSEL_BU_PLANT_ABBR;}
	public Vector getVDIS_CONT_MAPPING() {return VDIS_CONT_MAPPING;}
	public Vector getVCARGO_NO() {return VCARGO_NO;}
	public Vector getVCONT_NAME() {return VCONT_NAME;}
	public Vector getVMDCQ_QTY() {return VMDCQ_QTY;}
	public Vector getVINTERNAL_MAP_ID() {return VINTERNAL_MAP_ID;}
	public Vector getVTAX_DTL() {return VTAX_DTL;}
	public Vector getVNOM_BLOCK() {return VNOM_BLOCK;}
	public Vector getVIS_EXIST() {return VIS_EXIST;}
	
	public Vector getVPDF_EXISTS() {return VPDF_EXISTS;}
	public Vector getVMAIL_FROM_LIST() {return VMAIL_FROM_LIST;}
	public Vector getVMAIL_TO_LIST() {return VMAIL_TO_LIST;}
	public Vector getVMAIL_CC_LIST() {return VMAIL_CC_LIST;}
	public Vector getVMAIL_SUBJECT() {return VMAIL_SUBJECT;}
	public Vector getVMAIL_ATTACHMENT() {return VMAIL_ATTACHMENT;}
	public Vector getVMAIL_ATTACHMENT_PATH() {return VMAIL_ATTACHMENT_PATH;}
	public Vector getVMAIL_BODY() {return VMAIL_BODY;}
	public Vector getVPDF_FILE_NAME() {return VPDF_FILE_NAME;}
	
	String start_dt = "";
	String end_dt = "";
	String cont_name = "";
	String dcq = "";
	String mdcq_percentage = "";
	String dateTime = "";
	String forthnighly_start_dt="";
	String forthnighly_end_dt="";
	
	String from_contact_nm = "";
	String from_fax="";
	String from_fax2="";
	String from_phone="";
	String from_mobile="";
	String bu_plantAddress="";
	String bu_plantCity="";
	String bu_plantState="";
	String bu_plantPin="";
	String to_contact_nm = "";
	String to_fax="";
	String to_fax2="";
	String to_phone="";
	String to_mobile="";
	String plantAddress="";
	String plantCity="";
	String plantState="";
	String plantPin="";
	String plantNm="";
	String contRef="";
	String signingDt="";
	String contract_type_nm="";
	
	String total_buyer_mmbtu="";
	String total_buyer_scm="";
	String total_seller_mmbtu="";
	String total_seller_scm="";
	String total_alloc_mmbtu="";
	String total_alloc_scm="";
	
	public String getStart_dt() {return start_dt;}
	public String getEnd_dt() {return end_dt;}
	public String getCont_name() {return cont_name;}
	public String getDcq() {return dcq;}
	public String getMdcq_percentage() {return mdcq_percentage;}
	public String getDateTime() {return dateTime;}
	public String getForthnighly_start_dt() {return forthnighly_start_dt;}
	public String getForthnighly_end_dt() {return forthnighly_end_dt;}
	
	public String getFrom_contact_nm() {return from_contact_nm;}
	public String getFrom_fax() {return from_fax;}
	public String getFrom_fax2() {return from_fax2;}
	public String getFrom_phone() {return from_phone;}
	public String getFrom_mobile() {return from_mobile;}
	public String getBu_plantAddress() {return bu_plantAddress;}
	public String getBu_plantCity() {return bu_plantCity;}
	public String getBu_plantState() {return bu_plantState;}
	public String getBu_plantPin() {return bu_plantPin;}
	public String getTo_contact_nm() {return to_contact_nm;}
	public String getTo_fax() {return to_fax;}
	public String getTo_fax2() {return to_fax2;}
	public String getTo_phone() {return to_phone;}
	public String getTo_mobile() {return to_mobile;}
	public String getplantAddress() {return plantAddress;}
	public String getplantCity() {return plantCity;}
	public String getplantState() {return plantState;}
	public String getplantPin() {return plantPin;}
	public String getPlantNm() {return plantNm;}
	public String getContRef() {return contRef;}
	public String getSigningDt() {return signingDt;}
	public String getContract_type_nm() {return contract_type_nm;}
	
	public String getTotal_buyer_mmbtu() {return total_buyer_mmbtu;}
	public String getTotal_buyer_scm() {return total_buyer_scm;}
	public String getTotal_seller_mmbtu() {return total_seller_mmbtu;}
	public String getTotal_seller_scm() {return total_seller_scm;}
	public String getTotal_alloc_mmbtu() {return total_alloc_mmbtu;}
	public String getTotal_alloc_scm() {return total_alloc_scm;}
	
	double day_total_buyNom_qty=0;
	public Double getDay_total_buyNom_qty() {return day_total_buyNom_qty;}
	
}
