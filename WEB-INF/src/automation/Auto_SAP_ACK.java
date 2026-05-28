package automation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.XmlUtilBean;

public class Auto_SAP_ACK
{
	public static void main(String[] args) throws ServletException, IOException 
	 {
		Auto_SAP_XML asx = new Auto_SAP_XML();
		asx.init(); 
    }
}

class Auto_SAP_XML
{
	String db_src_file_name="Auto_SAP_XML.java";
	public static String mail_font_size="x-small";
	public static String mail_font_family="Calibri";
	
	Connection conn;
    PreparedStatement st,stmt,stmt1,stmt2,stmt3,stmt4,stmt5,stmt_tmp,stmt_tmp1,stmt_tmp2,stmt_tmp3,stmt_tmp4,stmt_tmp5;
    ResultSet rt,rset,rset1,rset2,rset3,rset4,rset5,rset_tmp,rset_tmp1,rset_tmp2,rset_tmp3,rset_tmp4,rset_tmp5;
	String query="",query1="",queryString="",queryString1="",queryString2="",queryString3="";
	String queryString4="",queryString5="";
	String queryString_temp="",queryString_temp1="",queryString_temp2="",queryString_temp3="",queryString_temp4="";
    
	String in_sap_xml_dir="";
   
    NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	
	Auto_UtilBean utilBean = new Auto_UtilBean();
	DateUtil utilDate = new DateUtil();
	Auto_MailDelivery mailDelv = new Auto_MailDelivery();
	XmlUtilBean xmlUtil = new XmlUtilBean();
   
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
		            
		            in_sap_xml_dir=utilBean.getAutomationKeyDetail(conn, "SAP_ACK_PATH");
		            
		            InsertUpdate_SAP_ACK_XML();
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
    	    	if(rset_tmp != null){try{rset_tmp.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(rset_tmp1 != null){try{rset_tmp1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(rset_tmp2 != null){try{rset_tmp2.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(rset_tmp3 != null){try{rset_tmp3.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(rset_tmp4 != null){try{rset_tmp4.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(rset_tmp5 != null){try{rset_tmp5.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(st != null){try{st.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt4!= null){try{stmt4.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt_tmp!= null){try{stmt_tmp.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt_tmp1!= null){try{stmt_tmp1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt_tmp2!= null){try{stmt_tmp2.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt_tmp3!= null){try{stmt_tmp3.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt_tmp4!= null){try{stmt_tmp4.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt_tmp5!= null){try{stmt_tmp5.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(conn != null){try{conn.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
        	}
        	catch(Exception e)
        	{
        		new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
        	}
        }
	}
    
    public void InsertUpdate_SAP_ACK_XML()
    {
    	String function_nm="InsertUpdate_SAP_ACK_XML()";
    	try
		{
    		String file_name="";
	        String fileName="";
	        
	        File folder = new File(in_sap_xml_dir);
	        
	        if (folder.isDirectory()) 
	        {
	        	File[] files = folder.listFiles();

	            // Iterate over the files
	            if (files != null) 
	            {
	                for (File file : files) 
	                {
	                    if (file.isFile()) 
	                    {
	                        String ack_file_name=file.getName();
	                        String file_path =in_sap_xml_dir+""+file.getName();
	                        
	        	    		try (FileInputStream parse_file = new FileInputStream(new File(file_path)))
	        	    		{
	        	    			//an instance of factory that gives a document builder
		        				DocumentBuilderFactory dbf = xmlUtil.dcoumentBuilderFactory(); 
		        				
		        				//an instance of builder to parse the specified XML file  
		        				javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();  
		        				
		        				// Parse the XML data
		        				org.w3c.dom.Document doc = db.parse(file);
		        				
		        				// Get the root element
		        	            Element rootElement = doc.getDocumentElement();
		        				doc.getDocumentElement().normalize();
		        				
		        				NodeList nList = doc.getElementsByTagName("SapPostingResponse");
		        				for (int temp = 0; temp < nList.getLength(); temp++) 
		        				{
		        					Node nNode = nList.item(temp);
		        					if (nNode.getNodeType() == Node.ELEMENT_NODE) 
		        					{
		        						if(nNode.hasAttributes())
		        						{
		        							NamedNodeMap nodeMap = (NamedNodeMap) nNode.getAttributes();
		        							for (int i = 0; i < ((NamedNodeMap) nodeMap).getLength(); i++) {
		        								Node node = nodeMap.item(i);
		        							}
		        						}
		        						
		        						String sysdt = utilDate.getSysdate();
		        						
		        						String sap_posting_status = "";
		        						String sap_post_date = "";
		        						String sap_post_time = "";
		        						String idocNo = "";
		        						String idocStatus = "";
		        						String sap_status_msg = "";
		        						String sap_doc_number = "";
		        						String sap_company_code = "";
		        						String sap_fiscal_year = "";
		        						String fmsng_ref_num = "";
		        						String msg_status = "";
		        						
		        						Element eElement = (Element) nNode;
		        					   
		        			            NodeList SapPostingStatus = doc.getElementsByTagName("SapPostingStatus");
		        			            if (SapPostingStatus.getLength() > 0) 
		        			            {
		        			                Node statusNode = SapPostingStatus.item(0);
		        			                if (statusNode.getNodeType() == Node.ELEMENT_NODE) 
		        			                {
		        			                    Element statusElement = (Element) statusNode;
		        			                    sap_posting_status = statusElement.getTextContent();
		        			                }
		        			            }
		        			                
		        						NodeList SAPPostDate = doc.getElementsByTagName("SAPPostDate");
		        			            if (SAPPostDate.getLength() > 0) 
		        			            {
		        			                Node sapPostDate = SAPPostDate.item(0);
		        			                if (sapPostDate.getNodeType() == Node.ELEMENT_NODE) 
		        			                {
		        			                    Element statusElement = (Element) sapPostDate;
		        			                    sap_post_date = statusElement.getTextContent();
		        			                }
		        			            }
		        			            
		        			            NodeList SAPPostTime = doc.getElementsByTagName("SAPPostTime");
		        			            if (SAPPostTime.getLength() > 0) 
		        			            {
		        			                Node sapPostTime = SAPPostTime.item(0);
		        			                if (sapPostTime.getNodeType() == Node.ELEMENT_NODE) 
		        			                {
		        			                    Element statusElement = (Element) sapPostTime;
		        			                    sap_post_time = statusElement.getTextContent();
		        			                }
		        			            }
		        			            NodeList iDocNo = doc.getElementsByTagName("iDocNo");
		        			            if (iDocNo.getLength() > 0) 
		        			            {
		        			                Node iDocno = iDocNo.item(0);
		        			                if (iDocno.getNodeType() == Node.ELEMENT_NODE) 
		        			                {
		        			                    Element statusElement = (Element) iDocno;
		        			                    idocNo = statusElement.getTextContent();
		        			                }
		        			            }
		        			            NodeList iDocStatus = doc.getElementsByTagName("iDocStatus");
		        			            if (iDocStatus.getLength() > 0) 
		        			            {
		        			                Node iDocstatus = iDocStatus.item(0);
		        			                if (iDocstatus.getNodeType() == Node.ELEMENT_NODE) 
		        			                {
		        			                    Element statusElement = (Element) iDocstatus;
		        			                    idocStatus = statusElement.getTextContent();
		        			                }
		        			            }
		        			            NodeList SAPStatusMsg = doc.getElementsByTagName("SAPStatusMsg");
		        			            if (SAPStatusMsg.getLength() > 0) 
		        			            {
		        			                Node sapStatusMsg = SAPStatusMsg.item(0);
		        			                if (sapStatusMsg.getNodeType() == Node.ELEMENT_NODE) 
		        			                {
		        			                    Element statusElement = (Element) sapStatusMsg;
		        			                    sap_status_msg = statusElement.getTextContent();
		        			                }
		        			            }
		        			            NodeList SAPDocNumber = doc.getElementsByTagName("SAPDocNumber");
		        			            if (SAPDocNumber.getLength() > 0) 
		        			            {
		        			                Node sapDocNumber = SAPDocNumber.item(0);
		        			                if (sapDocNumber.getNodeType() == Node.ELEMENT_NODE) 
		        			                {
		        			                    Element statusElement = (Element) sapDocNumber;
		        			                    sap_doc_number = statusElement.getTextContent();
		        			                }
		        			            }
		        			            NodeList SAPCompanyCode = doc.getElementsByTagName("SAPCompanyCode");
		        			            if (SAPCompanyCode.getLength() > 0) 
		        			            {
		        			                Node sapCompanyCode = SAPCompanyCode.item(0);
		        			                if (sapCompanyCode.getNodeType() == Node.ELEMENT_NODE) 
		        			                {
		        			                    Element statusElement = (Element) sapCompanyCode;
		        			                    sap_company_code = statusElement.getTextContent();
		        			                }
		        			            }
		        			            NodeList SAPFiscalYear = doc.getElementsByTagName("SAPFiscalYear");
		        			            if (SAPFiscalYear.getLength() > 0) 
		        			            {
		        			                Node sapFiscalYear = SAPFiscalYear.item(0);
		        			                if (sapFiscalYear.getNodeType() == Node.ELEMENT_NODE) 
		        			                {
		        			                    Element statusElement = (Element) sapFiscalYear;
		        			                    sap_fiscal_year = statusElement.getTextContent();
		        			                }
		        			            }
		        			            NodeList EmsRefNum = doc.getElementsByTagName("EmsRefNum");
		        			            if (EmsRefNum.getLength() > 0) 
		        			            {
		        			                Node emsRefNum = EmsRefNum.item(0);
		        			                if (emsRefNum.getNodeType() == Node.ELEMENT_NODE) 
		        			                {
		        			                    Element statusElement = (Element) emsRefNum;
		        			                    fmsng_ref_num = statusElement.getTextContent();
		        			                }
		        			            }
		        			            NodeList MSGStatus = doc.getElementsByTagName("msgStatus");
		        			            if (MSGStatus.getLength() > 0) 
		        			            {
		        			                Node msgStatus = MSGStatus.item(0);
		        			                if (msgStatus.getNodeType() == Node.ELEMENT_NODE) 
		        			                {
		        			                    Element statusElement = (Element) msgStatus;
		        			                    msg_status = statusElement.getTextContent();
		        			                }
		        			            }
		        						
		        			            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
		        			          
		        			            Date date = inputFormat.parse(sap_post_date);
		        			            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
		        			            String sdf_sap_post_date = outputFormat.format(date);
		        			            
			        			        String sdf_sap_post_time = sap_post_time.substring(0, 2)+":"+sap_post_time.substring(2, 4)+":"+sap_post_time.substring(4, 6);
			        			        
										String query = "SELECT COMPANY_CD,FMS_REF FROM FMS_SAP_ACK_DTL "
												+ "WHERE COMPANY_CD=? AND FMS_REF=? "
//												+ "AND ACK_FILE_NM=? "
												+ "AND (TO_DATE(POST_DT,'DD/MM/YY')>TO_DATE(?,'DD/MM/YY') OR (TO_DATE(POST_DT,'DD/MM/YY')<=TO_DATE(?,'DD/MM/YY') AND TO_TIMESTAMP(POST_TIME,'HH24:MI:SS')>=TO_TIMESTAMP(?,'HH24:MI:SS')))";
										stmt = conn.prepareStatement(query);
										stmt.setString(1, comp_cd);
										stmt.setString(2, fmsng_ref_num);
//										stmt.setString(3, ack_file_name);
										stmt.setString(3, sdf_sap_post_date);
										stmt.setString(4, sdf_sap_post_date);
										stmt.setString(5, sdf_sap_post_time);
										rset = stmt.executeQuery();

										if(rset.next())
										{ 
											System.out.println("Data Already Exists For "+ack_file_name);
										}
										else
										{
											queryString = "INSERT INTO FMS_SAP_ACK_DTL (COMPANY_CD,FMS_REF,ACK_FILE_NM,POST_STATUS,POST_DT,POST_TIME,IDOC_NO,"
				        							+ "IDOC_STATUS,STATUS_MSG,DOC_NO,COMPANY_CODE,FISCAL_YR,MSG_STATUS) "
				        							+ "VALUES (?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
				        							+ "?,?,?,?,?,?,?,?)";
											
											stmt1=conn.prepareStatement(queryString);
											stmt1.setString(1, comp_cd);
											stmt1.setString(2, fmsng_ref_num);
											stmt1.setString(3, ack_file_name);
											stmt1.setString(4, sap_posting_status);
											stmt1.setString(5, sdf_sap_post_date);
											stmt1.setString(6, sdf_sap_post_time);
											stmt1.setString(7, idocNo);
											stmt1.setString(8, idocStatus);
											stmt1.setString(9, sap_status_msg);
											stmt1.setString(10, sap_doc_number);
											stmt1.setString(11, sap_company_code);
											stmt1.setString(12, sap_fiscal_year);
											stmt1.setString(13, msg_status);
											stmt1.executeUpdate();

											stmt1.close();
											//conn.commit();
											
											//System.out.println("Data Inserted For "+ack_file_name);
											
											String sourceFile =ack_file_name;
									      
								            File sourceFileFolder = new File(in_sap_xml_dir);
									        Path sourceFilePath = new File(sourceFileFolder, file.getName()).toPath();
									        
											String destinationPath = in_sap_xml_dir+""+"SUCCESS_FILES";
									        File destinationFolder = new File(destinationPath);
									        
									        try 
									        {
									            if (!destinationFolder.exists())
									            {
									                destinationFolder.mkdir(); 
									            }

									            File destinationFile = new File(destinationFolder, file.getName());
									            Path destinationFilePath = new File(destinationFolder, file.getName()).toPath();
									            
									            parse_file.close();
									            
									            FileReader fileReader = new FileReader(in_sap_xml_dir+""+ack_file_name);  
									            fileReader.close();
									            
									           Move_File_To_Success(sourceFile,sourceFilePath,destinationFilePath);
									        } 
									        catch (Exception e) 
									        {
									            new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
									        }
											
										}
										rset.close();
										stmt.close();
		        					}
		        				}	
	        	    		}
	                    }
	                }
	            } 
	        } 
		}
		catch (Exception e) 
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    public void Move_File_To_Success(String sourceFile,Path sourceFilePath, Path destinationFilePath) 
    {
    	String function_nm="Move_File_To_Success()";
		try 
		{
			Files.move(sourceFilePath, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
			
			System.out.println(sourceFile+" parsed and moved To SUCCESS_FILES!");
		}
		catch (Exception e) 
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

    String comp_cd= "";
    String comp_abbr= "";
    String emp_cd="";
}