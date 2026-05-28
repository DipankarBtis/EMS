package automation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

public class Auto_SF_CONT_XML_GEN
{
	public static void main(String[] args) throws ServletException, IOException 
	{
		Auto_SF_CONT_XML asxg = new Auto_SF_CONT_XML();
		asxg.init(); 
	}
}

class Auto_SF_CONT_XML
{
	String db_src_file_name="Auto_SF_CONT_XML_GEN.java";
	public static String mail_font_size="x-small";
	public static String mail_font_family="Calibri";
	
	Connection conn;
	
	private static PreparedStatement st,stmt,stmt1,stmt2,stmt3,stmt4,stmt5;
    private static PreparedStatement stmt_temp;
    private static PreparedStatement stmt6 = null;
    private static PreparedStatement stmt7 = null;
	private static  ResultSet rt,rset,rset1,rset2,rset3,rset4,rset5,rset_temp;

    private static ResultSet rset6 = null;
	private static ResultSet rset7 = null;
	
	public static String sf_xml_inbound_dir="";
	public static String sf_xml_outbound_dir="";
	public static String ip="";
   
	public static NumberFormat nf = new DecimalFormat("###########0.00");
	public static NumberFormat nf2 = new DecimalFormat("###########0.0000");
	public static NumberFormat nf3 = new DecimalFormat("###########0.000");
	
	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();
   
    public void init()
	{
    	String function_nm="init()";
    	try
        {
    		conn=new Auto_DB_Connection().db_conn();
    		
    		if(conn != null)  
    		{         
    			String queryString = "SELECT COMPANY_CD,COMPANY_ABBR "
						+ "FROM FMS_COMPANY_OWNER_MST A "
						+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_OWNER_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) "
						+ "AND STATUS='Y' "
						+ "ORDER BY COMPANY_CD";
				st = conn.prepareStatement(queryString);
				rt = st.executeQuery();
				while(rt.next())
				{
					comp_cd=rt.getString(1)==null?"":rt.getString(1);
					comp_abbr=rt.getString(2)==null?"":rt.getString(2);
					
		            emp_cd=""; 
		            
		            sf_xml_inbound_dir=utilBean.getAutomationKeyDetail(conn, "SF_XML_INBOUND");
		            sf_xml_outbound_dir=utilBean.getAutomationKeyDetail(conn, "SF_XML_OUTBOUND_"+comp_cd);
		            
		            GenerateContactDetailsXml();
		            
		            VSEGMENT.clear();
					VSEGMENT.clear();

					VSEGMENT_NM.clear();
					VSEGMENT_NM.clear();
		            
		            System.out.println("Successfully - Salesforce XML Generated for "+comp_abbr);
		            
		            ip = ""+InetAddress.getLocalHost().getHostAddress();
				}
				rt.close();
				st.close();
             }
	         conn = null;
        }
    	catch(Exception e)
        {
        	new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
        }
    	
    	finally
        {
        	try
        	{
        		if(rt != null){try{rt.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
        		if(rset != null){try{rset.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(rset1 != null){try{rset1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(rset2 != null){try{rset2.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(rset3 != null){try{rset3.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(rset4 != null){try{rset4.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(rset5 != null){try{rset5.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(rset6 != null){try{rset6.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(rset7 != null){try{rset7.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(rset_temp != null){try{rset_temp.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(st != null){try{st.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt4!= null){try{stmt4.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt6!= null){try{stmt6.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt7!= null){try{stmt7.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt_temp != null){try{stmt_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(conn != null){try{conn.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
        	}
        	catch(Exception e)
        	{
        		new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
        	}
        }
	}
    
    public void GenerateContactDetailsXml()
    {
    	String function_nm="GenerateContactDetailsXml()";
    	try
		{
    		VSEGMENT.add("RLNG");
			VSEGMENT.add("DLNG");

			VSEGMENT_NM.add("RLNG Sales/LTCORA Contract");
			VSEGMENT_NM.add("DLNG Sales Contract");
			
			List<String> dwn_rlng_cont_dtl = new ArrayList<>();
	        List<String> dwn_dlng_cont_dtl = new ArrayList<>();
	        
			for(int i=0;i<VSEGMENT.size();i++) 
			{
				int selCnt=0;
				int dataCnt=0;
				
				String queryString ="";
				
				if(VSEGMENT.elementAt(i).equals("RLNG"))
				{
					queryString += "SELECT COMPANY_CD, COUNTERPARTY_CD, CONTRACT_TYPE, CONT_NO, CARGO_NO, CARGO_REF, '', TO_CHAR(ACTUAL_RECPT_DT, 'DD/MM/YYYY'), "
						    + " TO_CHAR(TO_DATE(ACTUAL_RECPT_DT + COALESCE(STORAGE_EXT_DAYS, 0) + COALESCE(STORAGE_DAYS-1, 0), 'DD/MM/YYYY'),'DD/MM/YYYY'), "
						    + " CARGO_STATUS, TO_CHAR(COALESCE(MODIFY_DT,ENT_DT), 'DD/MM/YYYY HH24:MI:SS'),AGMT_NO,AGMT_REV,CONT_REV,TO_CHAR(SF_GEN_DT, 'DD/MM/YYYY HH24:MI:SS') "
						    + " FROM FMS_LTCORA_CONT_CARGO_DTL A "
						    + " WHERE COMPANY_CD=? AND BUY_SALE=? "
						    + " AND ACTUAL_RECPT_DT<=TO_DATE(?,'DD/MM/YYYY') "
						    + " AND (ACTUAL_RECPT_DT + COALESCE(STORAGE_EXT_DAYS, 0) + COALESCE(STORAGE_DAYS-1, 0))>=TO_DATE(?,'DD/MM/YYYY') "
						    + " AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B "
							+ " WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE "
							+ " AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
							+ " AND A.CONT_NO=B.CONT_NO AND A.CARGO_NO=B.CARGO_NO) "
						    + " UNION ";
				}
				queryString +="SELECT COMPANY_CD, COUNTERPARTY_CD, CONTRACT_TYPE, CONT_NO, 0, CONT_REF_NO, TRADE_REF_NO, TO_CHAR(START_DT, 'DD/MM/YYYY'), "
					    + " TO_CHAR(END_DT, 'DD/MM/YYYY'), CONT_STATUS, TO_CHAR(COALESCE(MODIFY_DT,ENT_DT), 'DD/MM/YYYY HH24:MI:SS'),AGMT_NO,AGMT_REV,CONT_REV,TO_CHAR(SF_GEN_DT, 'DD/MM/YYYY HH24:MI:SS') "
					    + " FROM FMS_SUPPLY_CONT_MST A "
					    + " WHERE COMPANY_CD=? "
					    + " AND START_DT<=TO_DATE(?,'DD/MM/YYYY') "
					    + " AND END_DT>=TO_DATE(?,'DD/MM/YYYY') "
					    + " AND A.FCC_FLAG=? AND A.IS_ALLOCATED=? "
					    + " AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ " AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				
				if(VSEGMENT.elementAt(i).equals("RLNG"))
				{
					queryString +="AND CONTRACT_TYPE IN ('S','L','X')";
				}
				else if(VSEGMENT.elementAt(i).equals("DLNG"))
				{
					queryString +="AND CONTRACT_TYPE IN ('F','E','W')";
				}
				String temp_string = queryString;
				
				stmt=conn.prepareStatement(temp_string);
				if(VSEGMENT.elementAt(i).equals("RLNG"))
				{
					stmt.setString(++selCnt, comp_cd);
					stmt.setString(++selCnt, "C");//BUY_SALE
					stmt.setString(++selCnt, report_dt);
					stmt.setString(++selCnt, report_dt);
				}
				stmt.setString(++selCnt, comp_cd);
				stmt.setString(++selCnt, report_dt);
				stmt.setString(++selCnt, report_dt);
				stmt.setString(++selCnt, "Y");
				stmt.setString(++selCnt, "Y");
				rset=stmt.executeQuery();
				
				while(rset.next())
				{
					dataCnt++;
					
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String cont_type = rset.getString(3)==null?"":rset.getString(3);
					String cont =rset.getString(4)==null?"":rset.getString(4);
					String cargo_no =rset.getString(5)==null?"":rset.getString(5);
					String agmt =rset.getString(12)==null?"":rset.getString(12);
					String agmt_rev =rset.getString(13)==null?"":rset.getString(13);
					String cont_rev =rset.getString(14)==null?"":rset.getString(14);
					String last_mod_dt_time =rset.getString(11)==null?"":rset.getString(11);
					String sf_gen_dt =rset.getString(15)==null?"":rset.getString(15);
					
					String ltcora_cont_ref = "";
		            String ltcora_cont_name ="";
		            
		            String queryString_temp = "SELECT CONT_REF_NO,CONT_NAME "
							+ "FROM FMS_LTCORA_CONT_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
							+ "AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? "
							+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
					stmt_temp=conn.prepareStatement(queryString_temp);
					stmt_temp.setString(1, own_cd);
					stmt_temp.setString(2, countpty_cd);
					stmt_temp.setString(3, agmt);
					stmt_temp.setString(4, agmt_rev);
					stmt_temp.setString(5, cont_type);
					stmt_temp.setString(6, cont);
					rset_temp=stmt_temp.executeQuery();
			  		if(rset_temp.next())
			  		{
			  			ltcora_cont_ref =  rset_temp.getString(1)==null?"":rset_temp.getString(1);
			  			ltcora_cont_name = rset_temp.getString(2)==null?"":rset_temp.getString(2);
			  		}
			  		rset_temp.close();
			  		stmt_temp.close();
			  		
					String cont_disp_name = utilBean.SalesforceDealMapping(own_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo_no);
					String cont_status = rset.getString(10)==null?"":rset.getString(10);
					
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

					boolean postingPending = false;
					
					if (!cont_status.equals("Y"))
					{	
						postingPending = false;//Posting not Allowed
					} 
					else
					{									
						if(!sf_gen_dt.equals(""))
						{
							LocalDateTime lastModDateTime = LocalDateTime.parse(last_mod_dt_time, formatter);
					        LocalDateTime sfGenDateTime = LocalDateTime.parse(sf_gen_dt, formatter);
					        
							if (lastModDateTime.isAfter(sfGenDateTime)) 
					        {
								postingPending = true;//Posting yet not done for Last modified contract
					        } 
							else
							{
								postingPending = false;//Posting done for Last modified contract
							}
						}
						else
						{						
							postingPending = true;//Posting yet not done for Last modified contract
						}
					}	
					
					String cont_xml_gen_mapping = own_cd+"@"+countpty_cd+"@"+agmt+"@"+agmt_rev+"@"+cont+"@"+cont_rev+"@"+cont_type+"@"+cargo_no;
				
					if ("RLNG".equals(VSEGMENT.elementAt(i)) && postingPending) 
					{
						dwn_rlng_cont_dtl.add(cont_xml_gen_mapping);
			        } 
					else if ("DLNG".equals(VSEGMENT.elementAt(i)) && postingPending)
					{
			            dwn_dlng_cont_dtl.add(cont_xml_gen_mapping);
			        }
				}
				rset.close();
				stmt.close();
			}
			

			String report_dt = utilDate.getSysdate();
			
			if ((dwn_rlng_cont_dtl != null && !dwn_rlng_cont_dtl.isEmpty()) ||
				    (dwn_dlng_cont_dtl != null && !dwn_dlng_cont_dtl.isEmpty())) 
			{
		        String savePath = sf_xml_outbound_dir.replace("\\", File.separator);
		       
				File fileSaveDir = new File(savePath);
		        if(!fileSaveDir.exists()) 
		        {
		        	fileSaveDir.mkdirs();
		        }
				
				//For File uploaded file naming
		        LocalDateTime dateObj = LocalDateTime.now();
		        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
		        String date = dateObj.format(formatter1);
		        
		        String file_name="Contract_";
		        
				String file_path =savePath+File.separator+file_name+date+".xml";
				
				/*FileOutputStream fileOut = new FileOutputStream(new File(file_path));*/
				try (FileOutputStream fileOut = new FileOutputStream(new File(file_path)))
				{
					DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			            
					Document doc = docBuilder.newDocument();
		            Element fmsElement = doc.createElement("EMS");
		            doc.appendChild(fmsElement);

		            // Create <CONTRACT> element
		            Element contractElement = doc.createElement("CONTRACT");
		            fmsElement.appendChild(contractElement);

		            if(dwn_rlng_cont_dtl!=null)
		            {
		            	for(int i=0;i<dwn_rlng_cont_dtl.size();i++)
			            {
			            	String rlng_deal_mapping = dwn_rlng_cont_dtl.get(i);
			            	
			            	String[] splitted_mapping = rlng_deal_mapping.split("@");
			            	
			            	String own_cd = splitted_mapping[0];
			            	String countpty_cd = splitted_mapping[1];
			            	String agmt = splitted_mapping[2];
			            	String agmt_rev = splitted_mapping[3];
			            	String cont = splitted_mapping[4];
			            	String cont_rev = splitted_mapping[5];
			            	String cont_type = splitted_mapping[6];
			            	String cargo_no = splitted_mapping[7];
			            	
			            	String cont_ref_no="";
			            	String trade_ref_no="";
			            	String start_dt="";
			            	String end_dt="";
			            	String dcq="";
			            	String quantity_unit="";
			            	String mdcq_percentage="";
			            	String cont_name="";
			            	
			            	// Create <CONTRACT> element
				            Element contractMstElement = doc.createElement("CONT_MST");
				            contractElement.appendChild(contractMstElement);
				            
				            Element contDtl = doc.createElement("CONT_DTL");
				            contractMstElement.appendChild(contDtl);
				            
				            String ltcora_cont_ref = "";
				            String mcsoc_per ="";
				            String ltcora_cont_name ="";
				            
				            String queryString_temp = "SELECT CONT_REF_NO,MDCQ_PERCENTAGE,CONT_NAME "
									+ "FROM FMS_LTCORA_CONT_MST A "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
									+ "AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? "
									+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
									+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
							stmt_temp=conn.prepareStatement(queryString_temp);
							stmt_temp.setString(1, own_cd);
							stmt_temp.setString(2, countpty_cd);
							stmt_temp.setString(3, agmt);
							stmt_temp.setString(4, agmt_rev);
							stmt_temp.setString(5, cont_type);
							stmt_temp.setString(6, cont);
							rset_temp=stmt_temp.executeQuery();
					  		if(rset_temp.next())
					  		{
					  			ltcora_cont_ref =  rset_temp.getString(1)==null?"":rset_temp.getString(1);
					  			
					  			mcsoc_per = rset_temp.getString(2)==null?"":rset_temp.getString(2);
					  			//ltcora_cont_name = rset_temp.getString(3)==null?"":rset_temp.getString(3);
					  		}
					  		rset_temp.close();
					  		stmt_temp.close();
					  		
				            String queryString="";
				            if(cont_type.equals("O") || cont_type.equals("Q"))
			            	{
				            	 queryString="SELECT CARGO_REF,'',TO_CHAR(ACTUAL_RECPT_DT,'DD/MM/YYYY'),TO_CHAR((COALESCE(STORAGE_EXT_DAYS, 0) + COALESCE(STORAGE_DAYS-1, 0))),"
				            	 		+ "CSOC_QTY,1,'','' ";
				            	 queryString+= "FROM FMS_LTCORA_CONT_CARGO_DTL ";
			            	}
				            else
				            {
				            	 queryString="SELECT CONT_REF_NO,TRADE_REF_NO,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),DCQ,QUANTITY_UNIT,MDCQ_PERCENTAGE,CONT_NAME ";
				            	 queryString+= "FROM FMS_SUPPLY_CONT_MST ";
				            }
				            queryString+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
									+ "AND AGMT_NO=? AND AGMT_REV=?";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, own_cd);
							stmt.setString(2, countpty_cd);
							stmt.setString(3, cont);
							stmt.setString(4, cont_rev);
							stmt.setString(5, cont_type);
							stmt.setString(6, agmt);
							stmt.setString(7, agmt_rev);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								if(cont_type.equals("O") || cont_type.equals("Q"))
				            	{
									cont_ref_no=rset.getString(1)==null?"":rset.getString(1);//ltcora_cont_ref;
									
									mdcq_percentage=mcsoc_per;
									//cont_name=ltcora_cont_name;
				            	}
								else
								{
									cont_ref_no=rset.getString(1)==null?"":rset.getString(1);
									
									mdcq_percentage=rset.getString(7)==null?"":rset.getString(7);
									//cont_name=rset.getString(8)==null?"":rset.getString(8);
								}
								
								trade_ref_no=rset.getString(2)==null?"":rset.getString(2);
								start_dt=rset.getString(3)==null?"":rset.getString(3);
								if(cont_type.equals("O") || cont_type.equals("Q")) 
								{
									String end_days=rset.getString(4)==null?"":rset.getString(4);
									
									end_dt = utilDate.getDate(start_dt, end_days);
								}
								else
								{
									end_dt=rset.getString(4)==null?"":rset.getString(4);
								}
								
								dcq=nf.format(rset.getDouble(5));
								quantity_unit=rset.getString(6)==null?"1":rset.getString(6);
							}
							
							if(cont_type.equals("O") || cont_type.equals("Q")) 
							{
								String contractName="";
								
								if(cont_type.equals("O"))
								{
									contractName="CN";
								}
								else if (cont_type.equals("Q") )
								{
									contractName="PERIOD";
								}
								
								cont_name = utilBean.getCounterpartyABBR(conn,countpty_cd)
										+"-"+utilBean.getCompanyAbbr(conn, comp_cd)
										+"-"+utilBean.getSalesforceContractTypeName(cont_type)+agmt
										+"-"+contractName+cont
										+"-"+"CARGO"+cargo_no;
							}
							else if(cont_type.equals("S"))
							{
								
								cont_name = utilBean.getCounterpartyABBR(conn,countpty_cd)
										+"-"+utilBean.getCompanyAbbr(conn, comp_cd)
										+"-"+"FGSA"+agmt
										+"-"+utilBean.getSalesforceContractTypeName(cont_type)+cont;
							}
							else
							{
								cont_name = utilBean.getCounterpartyABBR(conn,countpty_cd)
										+"-"+utilBean.getCompanyAbbr(conn, comp_cd)
										+"-"+utilBean.getSalesforceContractTypeName(cont_type)+cont;
							}
							
				            addElement(contDtl, "CUSTOMER_CODE", countpty_cd);
				            addElement(contDtl, "CUSTOMER_NAME", utilBean.getCounterpartyName(conn,countpty_cd));
				            addElement(contDtl, "CONT_TYPE", utilBean.getSalesforceContractTypeName(cont_type));
				            addElement(contDtl, "AGMT_CONT_NO", utilBean.SalesforceDealMapping(own_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo_no));
				            addElement(contDtl, "AGMT_CONT_NAME", cont_name);
				            
				            if(cont_type.equals("X") || cont_type.equals("W"))
				            {
				            	addElement(contDtl, "CONT_REF", trade_ref_no);
				            }
				            else
				            {
				            	addElement(contDtl, "CONT_REF", cont_ref_no);
				            }
				            addElement(contDtl, "CONT_ST_DT", start_dt);
				            addElement(contDtl, "CONT_END_DT", end_dt);
				            addElement(contDtl, "MDQ_PERC", mdcq_percentage);
				            addElement(contDtl, "DCQ", dcq);
				            
				            String quantity_unit_nm="";
				            if(quantity_unit.equals("1"))
				            {
				            	quantity_unit_nm="MMBTU";
				            }
				            else
				            {
				            	quantity_unit_nm=quantity_unit;
				            }
				            addElement(contDtl, "UNIT", quantity_unit_nm);
				            
				            Element contPlantDtl = doc.createElement("CONT_PLANT_DTL");
				            contractMstElement.appendChild(contPlantDtl);
				            
				            Element contDeliveryPoint = doc.createElement("CONT_DELIVERY_POINT");
				            contractMstElement.appendChild(contDeliveryPoint);
				            
			            	String agmtContNo=utilBean.SalesforceDealMapping(own_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo_no);
			            	
			            	String bu_plant_seq ="";
			            	String bu_plant_abbr="";
			            	
			            	String cust_plant_seq="";
			            	String plant_abbr="";
			            	
			            	String queryString3="SELECT COMPANY_CD,PLANT_SEQ_NO ";
	            			if(cont_type.equals("O") || cont_type.equals("Q"))
			            	{
	            				queryString3+= "FROM FMS_LTCORA_CONT_BU ";
			            	}
			            	else
			            	{
			            		queryString3+= "FROM FMS_SUPPLY_CONT_BU ";
			            	}
	            			queryString3+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
									+ "AND CONTRACT_TYPE=?";
	            			if(cont_type.equals("O") || cont_type.equals("Q"))
			            	{
	            				queryString3+= "AND BUY_SALE=? ";
			            	}
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, own_cd);
							stmt3.setString(2, countpty_cd);
							stmt3.setString(3, cont);
							stmt3.setString(4, cont_rev);
							stmt3.setString(5, agmt);
							stmt3.setString(6, agmt_rev);
							stmt3.setString(7, cont_type);
							if(cont_type.equals("O") || cont_type.equals("Q"))
			            	{
								stmt3.setString(8, "C");
			            	}
							rset3=stmt3.executeQuery();
							while(rset3.next())
			            	{
								String buCd = rset3.getString(1)==null?"":rset3.getString(1);
								bu_plant_seq = rset3.getString(2)==null?"":rset3.getString(2);
								bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn, buCd, comp_cd, bu_plant_seq, "B");
								
			            		String queryString1="SELECT PLANT_SEQ_NO ";
			            		
				            	if(cont_type.equals("O") || cont_type.equals("Q"))
				            	{
				            		queryString1+= "FROM FMS_LTCORA_CONT_PLANT ";
				            	}
				            	else
				            	{
				            		queryString1+= "FROM FMS_SUPPLY_CONT_PLANT ";
				            	}
				            	queryString1+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
										+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
										+ "AND CONTRACT_TYPE=? ";
				            	if(cont_type.equals("O") || cont_type.equals("Q"))
				            	{
				            		queryString1+= "AND BUY_SALE=? ";
				            	}
				            	
				            	String tempQstring = queryString1;
								stmt1 = conn.prepareStatement(tempQstring);
								stmt1.setString(1, own_cd);
								stmt1.setString(2, countpty_cd);
								stmt1.setString(3, cont);
								stmt1.setString(4, cont_rev);
								stmt1.setString(5, agmt);
								stmt1.setString(6, agmt_rev);
								stmt1.setString(7, cont_type);
								if(cont_type.equals("O") || cont_type.equals("Q"))
				            	{
									stmt1.setString(8, "C");
				            	}
								rset1=stmt1.executeQuery();
								while(rset1.next())
								{
									cust_plant_seq = rset1.getString(1)==null?"":rset1.getString(1);
									plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, cust_plant_seq, "C");
									String plant_nm=utilBean.getCounterpartyPlantName(conn,countpty_cd, own_cd, cust_plant_seq, "C");
									
									HashMap<String, String> plant_detail = utilBean.getCounterpartyPlantDetail(conn,own_cd, "C", countpty_cd, cust_plant_seq);
									String plantAddr = ""+plant_detail.get("plant_address");
									
									String billingFrq="";
									String sysdt=utilDate.getSysdate();
									
									int selCount=0;
									
									String queryString2="SELECT BILLING_FREQ ";
									if(cont_type.equals("O") || cont_type.equals("Q"))
					            	{
										queryString2+="FROM FMS_LTCORA_CONT_BILLING_DTL A ";
					            	}
									else
									{
										queryString2+="FROM FMS_SUPPLY_BILLING_DTL A ";
									}
									queryString2+="WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev+"' "
											+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev+"' "
											+ "AND CONTRACT_TYPE=? ";
									if(!cont_type.equals("O") && !cont_type.equals("Q"))
									{
										queryString2+= "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_SUPPLY_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
												+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
												+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND TO_DATE(?,'DD/MM/YYYY') > EFF_DT)";
									}
									if(cont_type.equals("O") || cont_type.equals("Q"))
					            	{
					            		queryString2+= " AND BUY_SALE=? ";
					            	}
									stmt2 = conn.prepareStatement(queryString2);
									stmt2.setString(++selCount, own_cd);
									stmt2.setString(++selCount, countpty_cd);
									stmt2.setString(++selCount, agmt);
									stmt2.setString(++selCount, cont);
									stmt2.setString(++selCount, cont_type);
									if(!cont_type.equals("O") && !cont_type.equals("Q")) 
									{
										stmt2.setString(++selCount, sysdt);
									}
									if(cont_type.equals("O") || cont_type.equals("Q"))
					            	{
										stmt2.setString(++selCount, "C");
					            	}
									rset2=stmt2.executeQuery();
									if(rset2.next())
							  		{
							  			billingFrq=rset2.getString(1)==null?"":rset2.getString(1);
							  		}
									rset2.close();
									stmt2.close();
							  		
							  		String periodStDt=utilBean.getFirstDtOfBillingCycle(billingFrq, "", sysdt);
							  		
							  		String taxStructure = "";
							  		
							  		if(cont_type.equals("O") || cont_type.equals("Q"))
							  		{
							  			taxStructure = utilBean.getEntityServiceStructureDtl(conn,own_cd, countpty_cd, "C", cust_plant_seq, bu_plant_seq, periodStDt,"SI");
							  		}
							  		else
							  		{
							  			taxStructure = utilBean.getEntityTaxStructureDtl(conn,own_cd, countpty_cd, "C", cust_plant_seq, bu_plant_seq, periodStDt, "S");
							  		}
									
									addPlantDtl(contPlantDtl, agmtContNo, bu_plant_seq+"{-}"+cust_plant_seq, bu_plant_abbr+"{-}"+plant_abbr, plantAddr, taxStructure);
							
									 String queryString4="SELECT TRANSPORTER_CD, PLANT_SEQ_NO ";
							            if(cont_type.equals("O") || cont_type.equals("Q"))
						            	{
							            	queryString4+= "FROM FMS_LTCORA_CONT_TRANSPTR ";
						            	}
						            	else
						            	{
						            		queryString4+= "FROM FMS_SUPPLY_CONT_TRANSPTR ";
						            	}
							            queryString4+="WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
												+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
												+ "AND CONTRACT_TYPE=?";
							            if(cont_type.equals("O") || cont_type.equals("Q"))
						            	{
							            	queryString4+= "AND BUY_SALE=? ";
						            	}
										stmt4 = conn.prepareStatement(queryString4);
										stmt4.setString(1, own_cd);
										stmt4.setString(2, countpty_cd);
										stmt4.setString(3, cont);
										stmt4.setString(4, cont_rev);
										stmt4.setString(5, agmt);
										stmt4.setString(6, agmt_rev);
										stmt4.setString(7, cont_type);
										if(cont_type.equals("O") || cont_type.equals("Q"))
						            	{
											stmt4.setString(8, "C");
						            	}
										rset4=stmt4.executeQuery();
										while(rset4.next())
										{
											String transCd = rset4.getString(1)==null?"":rset4.getString(1);
											String transAbbr = utilBean.getCounterpartyABBR(conn,transCd);
											String trans_plant_seq1 = rset4.getString(2)==null?"":rset4.getString(2);
											//String plant_abbr=utilBean.getCounterpartyPlantABBR(transCd, own_cd, plant_seq, "R");
											
											String plant_name=utilBean.getCounterpartyPlantName(conn,transCd, own_cd, trans_plant_seq1, "R");
											
											String transporterAssignedCustomerCode="";
											String ctContractId="";
											String deliveryCode="";
											String deliveryName="";
											
											String gtc_cont_mapping_like=cont_type+"-"+countpty_cd+"-"+agmt+"-%-"+cont+"-%";
											
											if(cont_type.equals("O") || cont_type.equals("Q"))
											{
												gtc_cont_mapping_like+="-"+cargo_no;
											}
											
											String queryString5="SELECT ENTRY_TRANS_PLANT, CONTRACT_TYPE, EXIT_COUNTERPARTY, EXIT_PLANT_SEQ, "
													+ "CT_REF_NO, UTR_NO, TO_CHAR(START_DT,'DD/MM/YYYY'), TO_CHAR(END_DT,'DD/MM/YYYY'),STATUS,SEQ_NO,BU_SEQ "
													+ "FROM FMS_TRANSPORTER_CT_MST A "
													+ "WHERE COMPANY_CD=? "
													+ "AND ENTRY_TRANS_CD=? AND ENTRY_TRANS_PLANT=? "
													+ "AND EXIT_COUNTERPARTY=? AND EXIT_PLANT_SEQ=? "
													+ "AND BU_SEQ=? "
													+ "AND CONTRACT_TYPE=? "
													+ "AND TO_DATE(TO_CHAR(A.END_DT,'DD/MM/YYYY'),'DD/MM/YYYY') >=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') "
													+ "ORDER BY START_DT DESC";
											stmt5=conn.prepareStatement(queryString5);
											stmt5.setString(1, comp_cd);
											stmt5.setString(2, transCd);
											stmt5.setString(3, trans_plant_seq1);
											stmt5.setString(4, countpty_cd);
											stmt5.setString(5, cust_plant_seq);
											stmt5.setString(6, bu_plant_seq);
											stmt5.setString(7, "C");
											rset5=stmt5.executeQuery();
											while(rset5.next())
											{
												String trans_plant_seq=rset5.getString(1)==null?"":rset5.getString(1);
												String trans_plant_seq_nm=""+utilBean.getCounterpartyPlantName(conn,transCd, comp_cd, trans_plant_seq, "R");
												String exit_type=rset5.getString(2)==null?"":rset5.getString(2);		
												String exit_countpty_cd=rset5.getString(3)==null?"":rset5.getString(3);
												String exit_countpty_nm=""+utilBean.getCounterpartyName(conn,exit_countpty_cd);
												String exit_plant_seq=rset5.getString(4)==null?"":rset5.getString(4);
												String plant_seq_nm=""+utilBean.getCounterpartyPlantName(conn,exit_countpty_cd, comp_cd, exit_plant_seq, exit_type);
												String ct_ref=rset5.getString(5)==null?"":rset5.getString(5);
												String utr=rset5.getString(6)==null?"":rset5.getString(6);
												String exit_start_dt=rset5.getString(7)==null?"":rset5.getString(7);
												String exit_end_dt=rset5.getString(8)==null?"":rset5.getString(8);
												
												String exit_seq_no=rset5.getString(10)==null?"":rset5.getString(10);
												String exit_bu_seq=rset5.getString(11)==null?"":rset5.getString(11);
												
												String gtc_entryPointMap=transCd+"-"+trans_plant_seq;
												String gtc_exitPointMap=exit_type+"-"+countpty_cd+"-"+cust_plant_seq;
																
												String attachment="";
												
												String queryString6="SELECT CONT_MAPPING "
														+ "FROM FMS_TRANS_CT_CONT_MAP "
														+ "WHERE COMPANY_CD=? AND ENTRY_TRANS_CD=? "
														+ "AND ENTRY_TRANS_PLANT=? AND CONTRACT_TYPE=? "
														+ "AND EXIT_COUNTERPARTY=? AND EXIT_PLANT_SEQ=? "
														+ "AND SEQ_NO=? AND BU_SEQ=? AND CONT_MAPPING LIKE ?";
												stmt6=conn.prepareStatement(queryString6);
												stmt6.setString(1, own_cd );
												stmt6.setString(2, transCd);
												stmt6.setString(3, trans_plant_seq);
												stmt6.setString(4, exit_type);
												stmt6.setString(5, exit_countpty_cd);
												stmt6.setString(6, exit_plant_seq);
												stmt6.setString(7, exit_seq_no);
												stmt6.setString(8, exit_bu_seq);
												stmt6.setString(9, gtc_cont_mapping_like);
												rset6=stmt6.executeQuery();
												while(rset6.next())
												{
													String cont_mapping=rset6.getString(1)==null?"":rset6.getString(1);
													
													if(ctContractId.equals(""))
													{
														ctContractId+=""+ct_ref+","+exit_start_dt+","+exit_end_dt;
													}
													else
													{
														ctContractId+="{-}"+ct_ref+","+exit_start_dt+","+exit_end_dt;
													}
												}
												rset6.close();
												stmt6.close();
											}
											rset5.close();
											stmt5.close();
											
											String queryStringTemp="SELECT COUNTERPARTY_CD,PLANT_SEQ,CUSTOMER_CODE,TO_CHAR(EFF_DT,'DD/MM/YYYY'),STATUS "
													+ "FROM FMS_TRANSPORTER_CUST_CD A "
													+ "WHERE COMPANY_CD=? AND TRANSPORTER_CD=?  AND COUNTERPARTY_CD=? "
													+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_TRANSPORTER_CUST_CD B WHERE A.COMPANY_CD=B.COMPANY_CD "
													+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.PLANT_SEQ=B.PLANT_SEQ AND A.TRANSPORTER_CD=B.TRANSPORTER_CD)";
											stmt5=conn.prepareStatement(queryStringTemp);
											stmt5.setString(1, own_cd);
											stmt5.setString(2, transCd);
											stmt5.setString(3, countpty_cd);
											rset5=stmt5.executeQuery();
											while(rset5.next())
											{
												String countpty_cd1=rset5.getString(1)==null?"":rset5.getString(1);
												String countpty_abbr=""+utilBean.getCounterpartyABBR(conn,countpty_cd1);
												String plant_seq2=rset5.getString(2)==null?"":rset5.getString(2);
												String plant_seq_nm=""+utilBean.getCounterpartyPlantName(conn,countpty_cd1, own_cd, plant_seq2, "C");
												String eff_dt=rset5.getString(4)==null?"":rset5.getString(4);
												String custCode = rset5.getString(3)==null?"":rset5.getString(3);
												
												transporterAssignedCustomerCode=custCode;
											}
											rset5.close();
											stmt5.close();
											
											deliveryCode=transCd+"{-}"+trans_plant_seq1;
											deliveryName=transAbbr+"{-}"+plant_name;
											
											addDeliveryPoint(contDeliveryPoint, agmtContNo, transporterAssignedCustomerCode, ctContractId, deliveryCode, deliveryName, bu_plant_seq+"{-}"+cust_plant_seq);
										}
										rset4.close();
										stmt4.close();
								}
								rset1.close();
								stmt1.close();
			            	}
							rset3.close();
							stmt3.close();
							
							//SF XML Gen Date Updation.
							
							if(cont_type.equals("O") || cont_type.equals("Q"))
							{
								int st_count=0;
								String query ="UPDATE FMS_LTCORA_CONT_CARGO_DTL SET SF_GEN_DT = SYSDATE ";
								query+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND CONT_NO=? AND CONT_REV=? "
										+ "AND AGMT_NO=? AND AGMT_REV=? "
										+ "AND CONTRACT_TYPE=? AND BUY_SALE=? "
										+ "AND CARGO_NO=? ";
								stmt5 = conn.prepareStatement(query);
								stmt5.setString(++st_count, own_cd);
								stmt5.setString(++st_count, countpty_cd);
								stmt5.setString(++st_count, cont);
								stmt5.setString(++st_count, cont_rev);
								stmt5.setString(++st_count, agmt);
								stmt5.setString(++st_count, agmt_rev);
								stmt5.setString(++st_count, cont_type);
								stmt5.setString(++st_count, "C");
								stmt5.setString(++st_count, cargo_no);
								stmt5.executeUpdate();
								
								stmt5.close();
							}
							else
							{
								int st_count=0;
								String query ="UPDATE FMS_SUPPLY_CONT_MST SET SF_GEN_DT = SYSDATE ";
								query+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND CONT_NO=? AND CONT_REV=? "
										+ "AND AGMT_NO=? AND AGMT_REV=? "
										+ "AND CONTRACT_TYPE=?";
								stmt5 = conn.prepareStatement(query);
								stmt5.setString(++st_count, own_cd);
								stmt5.setString(++st_count, countpty_cd);
								stmt5.setString(++st_count, cont);
								stmt5.setString(++st_count, cont_rev);
								stmt5.setString(++st_count, agmt);
								stmt5.setString(++st_count, agmt_rev);
								stmt5.setString(++st_count, cont_type);
								stmt5.executeUpdate();
								
								stmt5.close();
							}
			            }
		            }

		            if(dwn_dlng_cont_dtl!=null)
		            {
		            	for(int i=0;i<dwn_dlng_cont_dtl.size();i++)
			            {
			            	String dlng_deal_mapping = dwn_dlng_cont_dtl.get(i);
			            	
			            	String[] splitted_mapping = dlng_deal_mapping.split("@");
			            	
			            	String own_cd = splitted_mapping[0];
			            	String countpty_cd = splitted_mapping[1];
			            	String agmt = splitted_mapping[2];
			            	String agmt_rev = splitted_mapping[3];
			            	String cont = splitted_mapping[4];
			            	String cont_rev = splitted_mapping[5];
			            	String cont_type = splitted_mapping[6];
			            	String cargo_no = splitted_mapping[7];
			            	
			            	String cont_ref_no="";
			            	String trade_ref_no="";
			            	String start_dt="";
			            	String end_dt="";
			            	String dcq="";
			            	String quantity_unit="";
			            	String mdcq_percentage="";
			            	String cont_name="";
			            	
			            	// Create <CONTRACT> element
				            Element contractMstElement = doc.createElement("CONT_MST");
				            contractElement.appendChild(contractMstElement);
				            
				            Element contDtl = doc.createElement("CONT_DTL");
				            contractMstElement.appendChild(contDtl);
				            
				            String queryString="SELECT CONT_REF_NO,TRADE_REF_NO,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),DCQ,QUANTITY_UNIT,MDCQ_PERCENTAGE,CONT_NAME "
									+ "FROM FMS_SUPPLY_CONT_MST "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
									+ "AND AGMT_NO=? AND AGMT_REV=?";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, own_cd);
							stmt.setString(2, countpty_cd);
							stmt.setString(3, cont);
							stmt.setString(4, cont_rev);
							stmt.setString(5, cont_type);
							stmt.setString(6, agmt);
							stmt.setString(7, agmt_rev);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								cont_ref_no=rset.getString(1)==null?"":rset.getString(1);
								trade_ref_no=rset.getString(2)==null?"":rset.getString(2);
								start_dt=rset.getString(3)==null?"":rset.getString(3);
								end_dt=rset.getString(4)==null?"":rset.getString(4);
								dcq=nf.format(rset.getDouble(5));
								quantity_unit=rset.getString(6)==null?"1":rset.getString(6);
								
								mdcq_percentage=rset.getString(7)==null?"":rset.getString(7);
								//cont_name=rset.getString(8)==null?"":rset.getString(8);
							}
							rset.close();
							stmt.close();
							

							if(cont_type.equals("F"))
							{
								
								cont_name = utilBean.getCounterpartyABBR(conn,countpty_cd)
										+"-"+utilBean.getCompanyAbbr(conn, comp_cd)
										+"-"+"FLSA"+agmt
										+"-DLNG"
										+"-"+utilBean.getSalesforceContractTypeName(cont_type)+cont;
							}
							else
							{
								cont_name = utilBean.getCounterpartyABBR(conn,countpty_cd)
										+"-"+utilBean.getCompanyAbbr(conn, comp_cd)
										+"-DLNG"
										+"-"+utilBean.getSalesforceContractTypeName(cont_type)+cont;
							}
							
							
				            addElement(contDtl, "CUSTOMER_CODE", countpty_cd);
				            addElement(contDtl, "CUSTOMER_NAME", utilBean.getCounterpartyName(conn, countpty_cd));
				            addElement(contDtl, "CONT_TYPE", utilBean.getSalesforceContractTypeName(cont_type));
				            addElement(contDtl, "AGMT_CONT_NO", utilBean.SalesforceDealMapping(own_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo_no));
				            addElement(contDtl, "AGMT_CONT_NAME", cont_name);
				            
				            if(cont_type.equals("X") || cont_type.equals("W"))
				            {
				            	addElement(contDtl, "CONT_REF", trade_ref_no);
				            }
				            else
				            {
				            	addElement(contDtl, "CONT_REF", cont_ref_no);
				            }
				            addElement(contDtl, "CONT_ST_DT", start_dt);
				            addElement(contDtl, "CONT_END_DT", end_dt);
				            addElement(contDtl, "MDQ_PERC", mdcq_percentage);
				            addElement(contDtl, "DCQ", dcq);
				            
				            String quantity_unit_nm="";
				            if(quantity_unit.equals("1"))
				            {
				            	quantity_unit_nm="MMBTU";
				            }
				            else
				            {
				            	quantity_unit_nm=quantity_unit;
				            }
				            addElement(contDtl, "UNIT", quantity_unit_nm);
				            
				            Element contPlantDtl = doc.createElement("CONT_PLANT_DTL");
				            contractMstElement.appendChild(contPlantDtl);
				            
				            Element contDeliveryPoint = doc.createElement("CONT_DELIVERY_POINT");
				            contractMstElement.appendChild(contDeliveryPoint);
				            
			            	String agmtContNo=utilBean.SalesforceDealMapping(own_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo_no);
			            	
			            	String bu_plant_seq ="";
			            	String bu_plant_abbr="";
			            	
			            	String cust_plant_seq="";
			            	String plant_abbr="";
			            	
			            	String queryString3="SELECT COMPANY_CD,PLANT_SEQ_NO ";
	            			queryString3+= "FROM FMS_SUPPLY_CONT_BU ";
	            			queryString3+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
									+ "AND CONTRACT_TYPE=?";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, own_cd);
							stmt3.setString(2, countpty_cd);
							stmt3.setString(3, cont);
							stmt3.setString(4, cont_rev);
							stmt3.setString(5, agmt);
							stmt3.setString(6, agmt_rev);
							stmt3.setString(7, cont_type);
							rset3=stmt3.executeQuery();
							while(rset3.next())
			            	{
								String buCd = rset3.getString(1)==null?"":rset3.getString(1);
								bu_plant_seq = rset3.getString(2)==null?"":rset3.getString(2);
								bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,buCd, comp_cd, bu_plant_seq, "B");
								
			            		String queryString1="SELECT PLANT_SEQ_NO ";
				            	queryString1+= "FROM FMS_SUPPLY_CONT_PLANT ";
				            	queryString1+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
										+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
										+ "AND CONTRACT_TYPE=? ";
				            	String tempQstring = queryString1;
								stmt1 = conn.prepareStatement(tempQstring);
								stmt1.setString(1, own_cd);
								stmt1.setString(2, countpty_cd);
								stmt1.setString(3, cont);
								stmt1.setString(4, cont_rev);
								stmt1.setString(5, agmt);
								stmt1.setString(6, agmt_rev);
								stmt1.setString(7, cont_type);
								rset1=stmt1.executeQuery();
								while(rset1.next())
								{
									cust_plant_seq = rset1.getString(1)==null?"":rset1.getString(1);
									plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, cust_plant_seq, "C");
									String plant_nm=utilBean.getCounterpartyPlantName(conn,countpty_cd, own_cd, cust_plant_seq, "C");
									
									HashMap<String, String> plant_detail = utilBean.getCounterpartyPlantDetail(conn,own_cd, "C", countpty_cd, cust_plant_seq);
									String plantAddr = ""+plant_detail.get("plant_address");
									
									String billingFrq="";
									String sysdt=utilDate.getSysdate();
									
									String queryString2="SELECT BILLING_FREQ "
											+ "FROM FMS_SUPPLY_BILLING_DTL A "
											+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev+"' "
											+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev+"' "
											+ "AND CONTRACT_TYPE=? "
											+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_SUPPLY_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
											+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
											+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND TO_DATE(?,'DD/MM/YYYY') > EFF_DT)";
									stmt2 = conn.prepareStatement(queryString2);
									stmt2.setString(1, own_cd);
									stmt2.setString(2, countpty_cd);
									stmt2.setString(3, agmt);
									stmt2.setString(4, cont);
									stmt2.setString(5, cont_type);
									stmt2.setString(6, sysdt);
									rset2=stmt2.executeQuery();
									if(rset2.next())
							  		{
							  			billingFrq=rset2.getString(1)==null?"":rset2.getString(1);
							  		}
									rset2.close();
									stmt2.close();
							  		
							  		String periodStDt=utilBean.getFirstDtOfBillingCycle(billingFrq, "", sysdt);
							  		
									String taxStructure = utilBean.getEntityTaxStructureDtl(conn,own_cd, countpty_cd, "C", cust_plant_seq, bu_plant_seq, periodStDt, "DS");
									
									addPlantDtl(contPlantDtl, agmtContNo, bu_plant_seq+"{-}"+cust_plant_seq, bu_plant_abbr+"{-}"+plant_abbr, plantAddr, taxStructure);
								
									String queryString4="SELECT TRANSPORTER_CD "
											+ "FROM FMS_SUPPLY_CONT_TRUCK_TRANS "
											+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
											+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
											+ "AND CONTRACT_TYPE=?";
									stmt4 = conn.prepareStatement(queryString4);
									stmt4.setString(1, own_cd);
									stmt4.setString(2, countpty_cd);
									stmt4.setString(3, cont);
									stmt4.setString(4, cont_rev);
									stmt4.setString(5, agmt);
									stmt4.setString(6, agmt_rev);
									stmt4.setString(7, cont_type);
									rset4=stmt4.executeQuery();
									while(rset4.next())
									{
										String transCd = rset4.getString(1)==null?"":rset4.getString(1);
										
										String transAbbr ="";
										
										String queryString5="SELECT TRUCK_TRANS_CD, TRUCK_TRANS_ABBR "
												+ "FROM FMS_TRUCK_TRANSPORTER_MST A "
												+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_TRUCK_TRANSPORTER_MST B WHERE A.TRUCK_TRANS_CD=B.TRUCK_TRANS_CD "
												+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND ACTIVE_FLAG=? AND TRUCK_TRANS_CD=? ";
										stmt5 = conn.prepareStatement(queryString5);
										stmt5.setString(1, "Y");
										stmt5.setString(2, transCd);
										rset5=stmt5.executeQuery();
										while(rset5.next())
										{
											transAbbr = rset5.getString(2)==null?"":rset5.getString(2);
										}
										rset5.close();
										stmt5.close();
										
										String transporterAssignedCustomerCode="";
										String ctContractId="";
										String deliveryCode="";
										String deliveryName="";
										
										String gtc_cont_mapping_like=cont_type+"-"+countpty_cd+"-"+agmt+"-%-"+cont+"-%";
										
										if(cont_type.equals("O") || cont_type.equals("Q"))
										{
											gtc_cont_mapping_like+="-"+cargo_no;
										}
										
										String queryString7="SELECT ENTRY_TRANS_PLANT, CONTRACT_TYPE, EXIT_COUNTERPARTY, EXIT_PLANT_SEQ, "
												+ "CT_REF_NO, UTR_NO, TO_CHAR(START_DT,'DD/MM/YYYY'), TO_CHAR(END_DT,'DD/MM/YYYY'),STATUS,SEQ_NO,BU_SEQ "
												+ "FROM FMS_TRANSPORTER_CT_MST A "
												+ "WHERE COMPANY_CD=? "
												+ "AND ENTRY_TRANS_CD=? AND ENTRY_TRANS_PLANT=? "
												+ "AND EXIT_COUNTERPARTY=? "
												+ "AND BU_SEQ=? "
												+ "AND CONTRACT_TYPE=? "
												+ "AND TO_DATE(TO_CHAR(A.END_DT,'DD/MM/YYYY'),'DD/MM/YYYY') >=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') "
												+ "ORDER BY START_DT DESC";
										stmt7=conn.prepareStatement(queryString7);
										stmt7.setString(1, comp_cd);
										stmt7.setString(2, transCd);
										stmt7.setString(3, countpty_cd);
										stmt7.setString(4, cust_plant_seq);
										stmt7.setString(5, bu_plant_seq);
										stmt7.setString(6, "C");
										rset7=stmt7.executeQuery();
										while(rset7.next())
										{
											String trans_plant_seq=rset7.getString(1)==null?"":rset7.getString(1);
											String trans_plant_seq_nm=""+utilBean.getCounterpartyPlantName(conn, transCd, comp_cd, trans_plant_seq, "R");
											String exit_type=rset7.getString(2)==null?"":rset7.getString(2);		
											String exit_countpty_cd=rset7.getString(3)==null?"":rset7.getString(3);
											String exit_countpty_nm=""+utilBean.getCounterpartyName(conn, exit_countpty_cd);
											String exit_plant_seq=rset7.getString(4)==null?"":rset7.getString(4);
											String plant_seq_nm=""+utilBean.getCounterpartyPlantName(conn, exit_countpty_cd, comp_cd, exit_plant_seq, exit_type);
											String ct_ref=rset7.getString(5)==null?"":rset7.getString(5);
											String utr=rset7.getString(6)==null?"":rset7.getString(6);
											String exit_start_dt=rset7.getString(7)==null?"":rset7.getString(7);
											String exit_end_dt=rset7.getString(8)==null?"":rset7.getString(8);
											
											String exit_seq_no=rset7.getString(10)==null?"":rset7.getString(10);
											String exit_bu_seq=rset7.getString(11)==null?"":rset7.getString(11);
											
											String gtc_entryPointMap=transCd+"-"+trans_plant_seq;
											String gtc_exitPointMap=exit_type+"-"+countpty_cd+"-"+cust_plant_seq;
															
											String attachment="";
											
											String queryString6="SELECT CONT_MAPPING "
													+ "FROM FMS_TRANS_CT_CONT_MAP "
													+ "WHERE COMPANY_CD=? AND ENTRY_TRANS_CD=? "
													+ "AND ENTRY_TRANS_PLANT=? AND CONTRACT_TYPE=? "
													+ "AND EXIT_COUNTERPARTY=? AND EXIT_PLANT_SEQ=? "
													+ "AND SEQ_NO=? AND BU_SEQ=? AND CONT_MAPPING LIKE ?";
											stmt6=conn.prepareStatement(queryString6);
											stmt6.setString(1, own_cd );
											stmt6.setString(2, transCd);
											stmt6.setString(3, trans_plant_seq);
											stmt6.setString(4, exit_type);
											stmt6.setString(5, exit_countpty_cd);
											stmt6.setString(6, exit_plant_seq);
											stmt6.setString(7, exit_seq_no);
											stmt6.setString(8, exit_bu_seq);
											stmt6.setString(9, gtc_cont_mapping_like);
											rset6=stmt6.executeQuery();
											while(rset6.next())
											{
												String cont_mapping=rset6.getString(1)==null?"":rset6.getString(1);
												
												if(ctContractId.equals(""))
												{
													ctContractId+=""+ct_ref+","+exit_start_dt+","+exit_end_dt;
												}
												else
												{
													ctContractId+="{-}"+ct_ref+","+exit_start_dt+","+exit_end_dt;
												}
											}
											rset6.close();
											stmt6.close();
										}
										rset7.close();
										stmt7.close();
										
										String queryStringTemp="SELECT COUNTERPARTY_CD,PLANT_SEQ,CUSTOMER_CODE,TO_CHAR(EFF_DT,'DD/MM/YYYY'),STATUS "
												+ "FROM FMS_TRANSPORTER_CUST_CD A "
												+ "WHERE COMPANY_CD=? AND TRANSPORTER_CD=? AND COUNTERPARTY_CD=? "
												+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_TRANSPORTER_CUST_CD B WHERE A.COMPANY_CD=B.COMPANY_CD "
												+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.PLANT_SEQ=B.PLANT_SEQ AND A.TRANSPORTER_CD=B.TRANSPORTER_CD)";
										stmt5=conn.prepareStatement(queryStringTemp);
										stmt5.setString(1, own_cd);
										stmt5.setString(2, transCd);
										stmt5.setString(3, countpty_cd);
										rset5=stmt5.executeQuery();
										while(rset5.next())
										{
											String countpty_cd1=rset5.getString(1)==null?"":rset5.getString(1);
											String countpty_abbr=""+utilBean.getCounterpartyABBR(conn,countpty_cd1);
											String plant_seq1=rset5.getString(2)==null?"":rset5.getString(2);
											String plant_seq_nm=""+utilBean.getCounterpartyPlantName(conn,countpty_cd1, own_cd, plant_seq1, "C");
											String eff_dt=rset5.getString(4)==null?"":rset5.getString(4);
											String custCode = rset5.getString(3)==null?"":rset5.getString(3);
											
											transporterAssignedCustomerCode=custCode;
										}
										rset5.close();
										stmt5.close();
										
										deliveryCode=transCd;//For DLNG : No Plant is Linked with Truck Transporters
										deliveryName=transAbbr;
										
										addDeliveryPoint(contDeliveryPoint, agmtContNo, transporterAssignedCustomerCode, ctContractId, deliveryCode,deliveryName ,bu_plant_seq+"{-}"+cust_plant_seq);
									}
									rset4.close();
									stmt4.close();
								}
								rset1.close();
								stmt1.close();
			            	}
							rset3.close();
							stmt3.close();
							
							//SF XML Gen Date Updation.
							
							int st_count=0;
							String query ="UPDATE FMS_SUPPLY_CONT_MST SET SF_GEN_DT = SYSDATE ";
							query+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND CONT_NO=? AND CONT_REV=? "
									+ "AND AGMT_NO=? AND AGMT_REV=? "
									+ "AND CONTRACT_TYPE=?";
							stmt5 = conn.prepareStatement(query);
							stmt5.setString(++st_count, own_cd);
							stmt5.setString(++st_count, countpty_cd);
							stmt5.setString(++st_count, cont);
							stmt5.setString(++st_count, cont_rev);
							stmt5.setString(++st_count, agmt);
							stmt5.setString(++st_count, agmt_rev);
							stmt5.setString(++st_count, cont_type);
							stmt5.executeUpdate();
							
							stmt5.close();
			            }
		            }

		            TransformerFactory transformerFactory = TransformerFactory.newInstance();
		            Transformer transformer = transformerFactory.newTransformer();
		            transformer.setOutputProperty("indent", "yes");  // For Indentation of the output file
		            DOMSource source = new DOMSource(doc);
		            StreamResult result = new StreamResult(fileOut);

		            // Write the XML to the file
		            transformer.transform(source, result);
		            
		            fileOut.close();// Close the file output stream
				}
				
				//System.out.println("SF XML file for Contract Generated Successfully!");

				String msg=file_path+" Generated Successfully!";
			   
				try
				{
					new automation.Auto_InfoLogger().InsertInfoLogger("0", "","System", ip, "0", "Auto SF OUTBOUND XML","0","Auto SF INBOUND XML", "", "", msg);  	
				}
				catch(Exception infoLogger)
				{
					new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
				}
			}
		
			//conn.commit();
		}
		catch (Exception e) 
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

    private static void addElement(Element parent, String tag, String value)
	{
        Element element = parent.getOwnerDocument().createElement(tag);
        element.appendChild(parent.getOwnerDocument().createTextNode(value));
        parent.appendChild(element);
    }
	
	private static void addPlantDtl(Element parent, String agmtContNo, String plantCode, String plantName, String plantAddr, String taxStructure)
	{
        Element plantDtlElement = parent.getOwnerDocument().createElement("PLANT_DTL");
        parent.appendChild(plantDtlElement);
        addElement(plantDtlElement, "AGMT_CONT_NO", agmtContNo);
        addElement(plantDtlElement, "PLANT_CODE", plantCode);
        addElement(plantDtlElement, "PLANT_NAME", plantName);
        addElement(plantDtlElement, "PLANT_ADDR", plantAddr);
        addElement(plantDtlElement, "TAX_STRUCTURE", taxStructure);
    }
	
	private static void addDeliveryPoint(Element parent, String agmtContNo, String transporterAssignedCustomerCode, String ctContractId, String deliveryCode, String deliveryName, String plantCode)
	{
        Element deliveryPointElement = parent.getOwnerDocument().createElement("DELIVERY_POINT");
        parent.appendChild(deliveryPointElement);
        addElement(deliveryPointElement, "AGMT_CONT_NO", agmtContNo);
        addElement(deliveryPointElement, "TRANSPORTER_ASSIGNED_CUSTOMER_CODE", transporterAssignedCustomerCode);
        addElement(deliveryPointElement, "CT_CONTRACT_ID", ctContractId);
        addElement(deliveryPointElement, "DELIVERY_CODE", deliveryCode);
        addElement(deliveryPointElement, "DELIVERY_NAME", deliveryName);
        addElement(deliveryPointElement, "PLANT_CODE", plantCode);
    }
	
    String comp_cd= "";
    String comp_abbr= "";
    String emp_cd="";
    String report_dt=utilDate.getSysdate();
    
    Vector VSEGMENT = new Vector();
	Vector VSEGMENT_NM = new Vector();
	Vector VSEGMENT_TYPE = new Vector();
}