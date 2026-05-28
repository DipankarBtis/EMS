package automation;

import java.io.File;
import java.io.FileInputStream;
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
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.XmlUtilBean;

public class Auto_SF_INBOUND_XML 
{
	public static void main(String[] args) 
	{
		Parse_SF_INBOUND_XML sf = new Parse_SF_INBOUND_XML();
		sf.init();
	}
}

class Parse_SF_INBOUND_XML
{
	String db_src_file_name="Auto_SF_INBOUND_XML.java";
	Connection conn;
	PreparedStatement stmt,stmt1,stmt2,stmt3,stmt4,stmt_temp,stmt_temp1;
	ResultSet rset,rset1,rset2,rset3,rset4,rset_temp,rset_temp1;
	String queryString="",queryString1="",queryString2="",queryString3="",queryString4="",queryString5="",queryString_temp="",queryString_temp1="";
	String file_path="";
	String ip="";
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	
	UtilBean utilBean = new UtilBean();
	XmlUtilBean xmlUtil = new XmlUtilBean();
	
	public void init()
	{ 
		String function_nm="init()";
        try
        {
        	conn=new Auto_DB_Connection().db_conn();
        	if(conn != null)
            {    			
        		conn.setAutoCommit(false);
        		file_path=utilBean.getAutomationKeyDetail(conn, "SF_XML_INBOUND");
        		ip = ""+InetAddress.getLocalHost().getHostAddress();
        		
        		parsingSF_NominationXML();
        		
        		conn.close();
    			conn = null;
            }
        	
        }
        catch(Exception e)
        {
        	new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
        }
        finally
	    {
	    	if(rset != null){try{rset.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset1 != null){try{rset1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset2 != null){try{rset2.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset3 != null){try{rset3.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset4 != null){try{rset4.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_temp != null){try{rset_temp.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_temp1 != null){try{rset_temp1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp != null){try{stmt_temp.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp1 != null){try{stmt_temp1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
	public void parsingSF_NominationXML()
	{
		String function_nm="parsingSF_NominationXML()";
		try
		{
			File sf_inbound_folder = new File(file_path);
			if(sf_inbound_folder.isDirectory()) 
	        {
	        	File[] files = sf_inbound_folder.listFiles();
	        	for(int p=0; p<files.length; p++)
				{
	        		if(files[p].isFile())
	        		{
		        		String xml_name=files[p].getName();
		        		String xml_file_path=file_path+""+xml_name;
		        		
		        		try( FileInputStream parse_file = new FileInputStream(new File(xml_file_path)))
		        		{

			        		DocumentBuilderFactory dbFactory = xmlUtil.dcoumentBuilderFactory();
						    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
						    Document doc = dBuilder.parse(parse_file);
						    
						    doc.getDocumentElement().normalize();
						    
						    HashMap<String, String> temp_map = new HashMap<String, String>();
						    HashMap<String, String> temp_map_rev = new HashMap<String, String>(); //THIS FOR AGMT_REV AND CONT_REV 
						    
						    NodeList nList = doc.getElementsByTagName("SF");
							for (int temp = 0; temp < nList.getLength(); temp++) 
							{
								Node nNode = nList.item(temp);
								Element eElement = (Element) nNode;
								
								NodeList nodes = eElement.getChildNodes();
								for(int i=0; i<nodes.getLength(); i++)
								{
									Node node = nodes.item(i);
									String childTag = node.getNodeName();
									if(childTag.equalsIgnoreCase("GASNOMINATIONS"))
									{
										Element ele = (Element) node;
										NodeList nodes1 = ele.getChildNodes();
										for(int j=0; j<nodes1.getLength(); j++)
										{
											Node node1 = nodes1.item(j);
											String childTag1 = node1.getNodeName();
											if(childTag1.equalsIgnoreCase("NOMINATION"))
											{
												Element ele1 = (Element) node1;
												NodeList nodes2 = ele1.getChildNodes();
												
												String contract_mapping="";
												String ct_cont_id="";
												String delivery_code="";
												String gas_day="";
												String gen_day="";
												String gen_time="";
												String nomination_qty="";
												String nomination_sf_id="";
												String nomination_type="";
												String plant_code="";
												
												for(int k=0; k<nodes2.getLength(); k++)
												{
													Node node2 = nodes2.item(k);
													String childTag2 = node2.getNodeName();
													
													if(childTag2.equals("AGMT_CONT_NO"))
													{
														contract_mapping=nodes2.item(k).getTextContent();
													}
													else if(childTag2.equals("CT_CONTRACT_ID"))
													{
														ct_cont_id=nodes2.item(k).getTextContent();
													}
													else if(childTag2.equals("DELIVERY_CODE"))
													{
														delivery_code=nodes2.item(k).getTextContent();
													}
													else if(childTag2.equals("GAS_DAY"))
													{
														gas_day=nodes2.item(k).getTextContent();
													}
													else if(childTag2.equals("GEN_DAY"))
													{
														gen_day=nodes2.item(k).getTextContent();
													}
													else if(childTag2.equals("GEN_TIME"))
													{
														gen_time=nodes2.item(k).getTextContent();
													}
													else if(childTag2.equals("NOMINATION_QTY"))
													{
														nomination_qty=nodes2.item(k).getTextContent();
													}
													else if(childTag2.equals("NOMINATION_SF_ID"))
													{
														nomination_sf_id=nodes2.item(k).getTextContent();
													}
													else if(childTag2.equals("NOMINATION_TYPE"))
													{
														nomination_type=nodes2.item(k).getTextContent();
													}
													else if(childTag2.equals("PLANT_CODE"))
													{
														plant_code=nodes2.item(k).getTextContent();
													}
												}
												
												//map=comp_cd+"-"+entity_type+"-"+counterparty_cd+"-"+cont_type+"-"+agmt+"-"+cont;
												//map+="-"+cargo_no //this for LTCORA only
												
												String comp_cd="",counterparty_cd="",agmt_no="",cont_no="",contract_type="",cargo_no="",
														transporter_cd="",trans_plant_seq="",bu_seq="",plant_seq="";
												if(contract_mapping.split("-").length>=6)
												{
													if(!contract_mapping.equals(""))
													{
														String split[] = contract_mapping.split("-");
														comp_cd=split[0];
														counterparty_cd=split[2];
														contract_type=split[3];
														agmt_no=split[4];
														cont_no=split[5];
														if(split.length>6)
														{
															cargo_no=split[6];
														}
														else
														{
															cargo_no="0";
														}
													}
													
													if(!delivery_code.equals(""))
													{
														//String split[] = delivery_code.replaceAll("{-}", "#").split("#");
														//transporter_cd=split[0];
														//trans_plant_seq=split[1];
														
														transporter_cd=delivery_code.substring(0,delivery_code.lastIndexOf("{"));
														trans_plant_seq=delivery_code.substring(delivery_code.lastIndexOf("}")+1,delivery_code.length());
														
														System.out.println(transporter_cd+"======="+trans_plant_seq);
													}
													
													if(!plant_code.equals(""))
													{
														/*String split[] = plant_code.replaceAll("{-}", "#").split("#");
														bu_seq=split[0];
														plant_seq=split[1];*/
														bu_seq=plant_code.substring(0,plant_code.lastIndexOf("{"));
														plant_seq=plant_code.substring(plant_code.lastIndexOf("}")+1,plant_code.length());
														
														System.out.println(bu_seq+"======="+plant_seq);	
													}
													
													
													queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,"
															+ "A.CONT_REF_NO,C.TRANSPORTER_CD,C.PLANT_SEQ_NO,A.DCQ,A.CONT_NAME,A.MDCQ_PERCENTAGE,A.TRADE_REF_NO,"
															+ "B.PLANT_SEQ_NO,D.COMPANY_CD,D.PLANT_SEQ_NO,0,'CONT',NULL,NULL "
															+ "FROM FMS_SUPPLY_CONT_MST A, FMS_SUPPLY_CONT_PLANT B, FMS_SUPPLY_CONT_TRANSPTR C, FMS_SUPPLY_CONT_BU D  "
															+ "WHERE A.COMPANY_CD=? AND A.BUYER_NOM_FLAG=? AND A.FCC_FLAG=? AND A.IS_ALLOCATED=? "
															+ "AND A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY') "
															+ "AND C.TRANSPORTER_CD=? AND C.PLANT_SEQ_NO=? AND B.PLANT_SEQ_NO=? AND D.PLANT_SEQ_NO=? "
															+ "AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
															+ "AND A.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
															+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
															+ ""
															+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
															+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
															+ ""
															+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV "
															+ "AND A.CONT_NO=C.CONT_NO AND A.CONT_REV=C.CONT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE "
															+ ""
															+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV "
															+ "AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE "
															+ " UNION ALL "
															+ "SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,"
															+ "B.CARGO_REF,C.TRANSPORTER_CD,C.PLANT_SEQ_NO,B.CSOC_QTY,A.CONT_NAME,A.MDCQ_PERCENTAGE,NULL,"
															+ "D.PLANT_SEQ_NO,E.COMPANY_CD,E.PLANT_SEQ_NO,B.CARGO_NO,'LTCORA',A.BUY_SALE,A.AGMT_TYPE "
															+ "FROM FMS_LTCORA_CONT_MST A,FMS_LTCORA_CONT_CARGO_DTL B,FMS_LTCORA_CONT_TRANSPTR C,FMS_LTCORA_CONT_PLANT D,FMS_LTCORA_CONT_BU E "
															+ "WHERE A.COMPANY_CD=? AND A.BUY_SALE=? AND A.FCC_FLAG=? AND B.CARGO_STATUS=? AND A.AGMT_TYPE=? AND B.BOE_NO IS NOT NULL "
															+ "AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0) >=TO_DATE(?,'DD/MM/YYYY') "
															+ "AND C.TRANSPORTER_CD=? AND C.PLANT_SEQ_NO=? AND D.PLANT_SEQ_NO=? AND E.PLANT_SEQ_NO=? "
															+ "AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND B.CARGO_NO=? "
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
													int stmt_cnt=0;
													stmt = conn.prepareStatement(queryString);
													stmt.setString(++stmt_cnt, comp_cd);
													stmt.setString(++stmt_cnt, "Y");
													stmt.setString(++stmt_cnt, "Y");
													stmt.setString(++stmt_cnt, "Y");
													stmt.setString(++stmt_cnt, gas_day);
													stmt.setString(++stmt_cnt, gas_day);
													stmt.setString(++stmt_cnt, transporter_cd);
													stmt.setString(++stmt_cnt, trans_plant_seq);
													stmt.setString(++stmt_cnt, plant_seq);
													stmt.setString(++stmt_cnt, bu_seq);
													stmt.setString(++stmt_cnt, counterparty_cd);
													stmt.setString(++stmt_cnt, agmt_no);
													stmt.setString(++stmt_cnt, cont_no);
													stmt.setString(++stmt_cnt, contract_type);
													stmt.setString(++stmt_cnt, comp_cd);
													stmt.setString(++stmt_cnt, "C");
													stmt.setString(++stmt_cnt, "Y");
													stmt.setString(++stmt_cnt, "Y");
													stmt.setString(++stmt_cnt, "A");
													stmt.setString(++stmt_cnt, gas_day);
													stmt.setString(++stmt_cnt, gas_day);
													stmt.setString(++stmt_cnt, transporter_cd);
													stmt.setString(++stmt_cnt, trans_plant_seq);
													stmt.setString(++stmt_cnt, plant_seq);
													stmt.setString(++stmt_cnt, bu_seq);
													stmt.setString(++stmt_cnt, counterparty_cd);
													stmt.setString(++stmt_cnt, agmt_no);
													stmt.setString(++stmt_cnt, cont_no);
													stmt.setString(++stmt_cnt, contract_type);
													stmt.setString(++stmt_cnt, cargo_no); 
													rset=stmt.executeQuery();
													if(rset.next())
													{
														String agmt_rev = rset.getString(4)==null?"":rset.getString(4);
														String cont_rev = rset.getString(6)==null?"":rset.getString(6);
														
														String map=comp_cd+"-"+counterparty_cd+"-"+contract_type+"-"+agmt_no+"-"+cont_no+"-"+cargo_no+"-"+transporter_cd+"-"+trans_plant_seq+"-"+plant_seq+"-"+bu_seq+"-"+gas_day;
														String map_rev=agmt_rev+"-"+cont_rev;
														temp_map.put(map, map);
														temp_map_rev.put(map, map_rev);
														
														String seqNo="";
														queryString1 = "SELECT SEQ_NO "
																+ "FROM FMS_DAILY_BUYER_NOM_DTL "
																+ "WHERE CONT_NO=? AND AGMT_NO=? "
																+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
																+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
																+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
																+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? AND CARGO_NO=? ";
														if(ct_cont_id.equals("")) {
															queryString1+=" AND CT_REF IS NULL ";
														}else {
															queryString1+=" AND CT_REF=? ";
														}
														
														stmt1 = conn.prepareStatement(queryString1);
														stmt1.setString(1, cont_no);
														stmt1.setString(2, agmt_no);
														stmt1.setString(3, comp_cd);
														stmt1.setString(4, counterparty_cd);
														stmt1.setString(5, transporter_cd);
														stmt1.setString(6, trans_plant_seq);
														stmt1.setString(7, plant_seq);
														stmt1.setString(8, contract_type);
														stmt1.setString(9, gas_day);
														stmt1.setString(10, bu_seq);
														stmt1.setString(11, cargo_no);
														if(ct_cont_id.equals("")) {
														}else {
															stmt1.setString(12, ct_cont_id);
														}
														
														rset1 = stmt1.executeQuery();
														if(rset1.next())
														{
															seqNo=rset1.getString(1)==null?"":rset1.getString(1);
														}
														else
														{
															/*queryString2 = "SELECT NVL(MAX(SEQ_NO),0) "
																	+ "FROM FMS_DAILY_BUYER_NOM_DTL "
																	+ "WHERE CONT_NO=? AND AGMT_NO=? "
																	+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
																	+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
																	+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
																	+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? AND CARGO_NO=? ";
															stmt2 = conn.prepareStatement(queryString2);
															stmt2.setString(1, cont_no);
															stmt2.setString(2, agmt_no);
															stmt2.setString(3, comp_cd);
															stmt2.setString(4, counterparty_cd);
															stmt2.setString(5, transporter_cd);
															stmt2.setString(6, trans_plant_seq);
															stmt2.setString(7, plant_seq);
															stmt2.setString(8, contract_type);
															stmt2.setString(9, gas_day);
															stmt2.setString(10, bu_seq);
															stmt2.setString(11, cargo_no);
															rset2 = stmt2.executeQuery();
															if(rset2.next())
															{
																seqNo=rset2.getString(1)==null?"":rset2.getString(1);
															}
															rset2.close();
															stmt2.close();*/
														}
														rset1.close();
														stmt1.close();
														
														if(seqNo.equals(""))
														{
															queryString1 = "SELECT NVL(MAX(SEQ_NO),?) "
																	+ "FROM FMS_DAILY_BUYER_NOM_DTL "
																	+ "WHERE CONT_NO=? AND AGMT_NO=? "
																	+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
																	+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
																	+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
																	+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? AND CARGO_NO=? ";
															stmt1 = conn.prepareStatement(queryString1);
															stmt1.setString(1, "0");
															stmt1.setString(2, cont_no);
															stmt1.setString(3, agmt_no);
															stmt1.setString(4, comp_cd);
															stmt1.setString(5, counterparty_cd);
															stmt1.setString(6, transporter_cd);
															stmt1.setString(7, trans_plant_seq);
															stmt1.setString(8, plant_seq);
															stmt1.setString(9, contract_type);
															stmt1.setString(10, gas_day);
															stmt1.setString(11, bu_seq);
															stmt1.setString(12, cargo_no);
															rset1 = stmt1.executeQuery();
															if(rset1.next())
															{
																seqNo = ""+(rset1.getInt(1)+1);
															}
															else
															{
																seqNo="1";
															}
															rset1.close();
															stmt1.close();
														}
														
														int sub_rev_no=-1;
														queryString1 = "SELECT NVL(MAX(NOM_REV_NO),?) "
																+ "FROM FMS_DAILY_BUYER_NOM_DTL "
																+ "WHERE CONT_NO=? AND AGMT_NO=? "
																+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
																+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
																+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
																+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? "
																+ "AND SEQ_NO=? AND CARGO_NO=? ";
														stmt1 = conn.prepareStatement(queryString1);
														stmt1.setString(1, "-1");
														stmt1.setString(2, cont_no);
														stmt1.setString(3, agmt_no);
														stmt1.setString(4, comp_cd);
														stmt1.setString(5, counterparty_cd);
														stmt1.setString(6, transporter_cd);
														stmt1.setString(7, trans_plant_seq);
														stmt1.setString(8, plant_seq);
														stmt1.setString(9, contract_type);
														stmt1.setString(10, gas_day);
														stmt1.setString(11, bu_seq);
														stmt1.setString(12, seqNo);
														stmt1.setString(13, cargo_no);
														rset1 = stmt1.executeQuery();
														if(rset1.next())
														{
															sub_rev_no = rset1.getInt(1)+1;
														}
														else
														{
															sub_rev_no = sub_rev_no + 1;
														}
														rset1.close();
														stmt1.close();
														
														String Base="GCV";
														String GCV="9802.80";
														String NCV="8831.35";
														String emp_cd="0";
														
														double deviding_factor=1; //FOR GCV AND FOR NCV 1.11
														double multiplying_factor_2 = 0.252; //For Converting MMBTU TO MMSCM ...
														double multiplying_factor = 0.252*1000000; //For Converting MMBTU TO SCM ..
														
														String scm =nf.format((Double.parseDouble(nomination_qty) * multiplying_factor)/(Double.parseDouble(GCV)*deviding_factor));
														gen_time=gen_time.substring(0,5);
														
														queryString2="INSERT INTO FMS_DAILY_BUYER_NOM_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
																+ "TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
																+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,BU_SEQ,SEQ_NO,CT_REF,UTR_NO,CARGO_NO,SF_ID) "
																+ "VALUES(?,?,?,?,?,?,?,?,"
																+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
																+ "?,?,?,?,?,SYSDATE,?,?,?,?,?,?)";
														stmt2 = conn.prepareStatement(queryString2);
														stmt2.setString(1, comp_cd);
														stmt2.setString(2, counterparty_cd);
														stmt2.setString(3, agmt_no);
														stmt2.setString(4, agmt_rev);
														stmt2.setString(5, cont_no);
														stmt2.setString(6, cont_rev);
														stmt2.setInt(7, sub_rev_no);
														stmt2.setString(8, plant_seq);
														stmt2.setString(9, transporter_cd);
														stmt2.setString(10, trans_plant_seq);
														stmt2.setString(11, gas_day);
														stmt2.setString(12, contract_type);
														stmt2.setString(13, gen_day);
														stmt2.setString(14, gen_time);
														stmt2.setString(15, Base);
														stmt2.setString(16, GCV);
														stmt2.setString(17, NCV);
														stmt2.setString(18, nomination_qty);
														stmt2.setString(19, scm);
														stmt2.setString(20, emp_cd);
														stmt2.setString(21, bu_seq);
														stmt2.setString(22, seqNo);
														stmt2.setString(23, ct_cont_id);
														stmt2.setString(24, "");
														stmt2.setString(25, cargo_no);
														stmt2.setString(26, nomination_sf_id);
														stmt2.executeQuery();
														
														stmt2.close();
														
														conn.commit();
													}
													rset.close();
													stmt.close();
												}
												else
												{
													System.out.println("Error Found in XML Data....");
												}
											}
										}
									}
								}
							}
							
							Iterator o = temp_map.keySet().iterator();
						    while (o.hasNext()) 
						    {
						    	String key = (String) o.next();
						    	String value=temp_map.get(key);
						    	String value_rev=temp_map_rev.get(key);
						    	
						    	String comp_cd="",counterparty_cd="",agmt_no="",cont_no="",contract_type="",cargo_no="",
										transporter_cd="",trans_plant_seq="",bu_seq="",plant_seq="",gas_day="";
						    	
						    	if(!value.equals(""))
						    	{
						    		String split[] = value.split("-");
									comp_cd=split[0];
									counterparty_cd=split[1];
									contract_type=split[2];
									agmt_no=split[3];
									cont_no=split[4];
									cargo_no=split[5];
									transporter_cd=split[6];
									trans_plant_seq=split[7];
									plant_seq=split[8];
									bu_seq=split[9];
									gas_day=split[10];
									
									String agmt_rev="0";
									String cont_rev="0";
									if(!value_rev.equals(""))
									{
										String split1[] = value_rev.split("-");
										agmt_rev=split1[0];
										cont_rev=split1[1];
									}
									
									String mmbtu="";
									String scm="";
									String gen_dt="";
									String gen_time="";
									
									String Base="GCV";
									String GCV="9802.80";
									String NCV="8831.35";
									String emp_cd="0";
									
									queryString3="SELECT SUM(QTY_MMBTU),SUM(QTY_SCM),TO_CHAR(MAX(GEN_DT),'DD/MM/YYYY'),MAX(GEN_TIME) "
							  				+ "FROM FMS_DAILY_BUYER_NOM_DTL A "
							  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
											+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
											+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
											+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
											+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_BUYER_NOM_DTL B "
											+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
											+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
											+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND A.BU_SEQ=B.BU_SEQ "
											+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
											+ "AND B.GAS_DT=A.GAS_DT AND B.SEQ_NO=A.SEQ_NO AND B.CARGO_NO=A.CARGO_NO) ";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, cont_no);
									stmt3.setString(2, agmt_no);
									stmt3.setString(3, comp_cd);
									stmt3.setString(4, counterparty_cd);
									stmt3.setString(5, transporter_cd);
									stmt3.setString(6, trans_plant_seq);
									stmt3.setString(7, plant_seq);
									stmt3.setString(8, contract_type);
									stmt3.setString(9, bu_seq);
									stmt3.setString(10, gas_day);
									stmt3.setString(11, cargo_no);
									rset3=stmt3.executeQuery();
									if(rset3.next())
									{
										mmbtu=nf.format(rset3.getDouble(1));
										scm=nf.format(rset3.getDouble(2));
										gen_dt=rset3.getString(3)==null?"":rset3.getString(3);
										gen_time=rset3.getString(4)==null?"":rset3.getString(4);
									}
									rset3.close();
									stmt3.close();
									
									int rev_no=-1;
									queryString = "SELECT NVL(MAX(NOM_REV_NO),'-1') "
											+ "FROM FMS_DAILY_BUYER_NOM "
											+ "WHERE CONT_NO=? AND AGMT_NO=? "
											+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
											+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
											+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? AND CARGO_NO=? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, cont_no);
									stmt.setString(2, agmt_no);
									stmt.setString(3, comp_cd);
									stmt.setString(4, counterparty_cd);
									stmt.setString(5, transporter_cd);
									stmt.setString(6, trans_plant_seq);
									stmt.setString(7, plant_seq);
									stmt.setString(8, contract_type);
									stmt.setString(9, gas_day);
									stmt.setString(10, bu_seq);
									stmt.setString(11, cargo_no);
									rset = stmt.executeQuery();
									if(rset.next())
									{
										rev_no = rset.getInt(1)+1;
									}
									else
									{
										rev_no = rev_no + 1;
									}
									rset.close();
									stmt.close();
									
									queryString="INSERT INTO FMS_DAILY_BUYER_NOM(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
											+ "TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
											+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,BU_SEQ,CARGO_NO) "
											+ "VALUES(?,?,?,?,?,?,?,?,"
											+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
											+ "?,?,?,?,?,SYSDATE,?,?)";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, comp_cd);
									stmt.setString(2, counterparty_cd);
									stmt.setString(3, agmt_no);
									stmt.setString(4, agmt_rev);
									stmt.setString(5, cont_no);
									stmt.setString(6, cont_rev);
									stmt.setInt(7, rev_no);
									stmt.setString(8, plant_seq);
									stmt.setString(9, transporter_cd);
									stmt.setString(10, trans_plant_seq);
									stmt.setString(11, gas_day);
									stmt.setString(12, contract_type);
									stmt.setString(13, gen_dt);
									stmt.setString(14, gen_time);
									stmt.setString(15, Base);
									stmt.setString(16, GCV);
									stmt.setString(17, NCV);
									stmt.setString(18, mmbtu);
									stmt.setString(19, scm);
									stmt.setString(20, emp_cd);
									stmt.setString(21, bu_seq);
									stmt.setString(22, cargo_no);
									stmt.executeQuery();
									
									stmt.close();
									
									conn.commit();
								}
						    }
						    
						    Path sourceFilePath = new File(file_path+xml_name).toPath();
						    Path destinationFilePath = new File(file_path+File.separator+"success"+File.separator+xml_name).toPath();
						    
						    File success_dir = new File(file_path+File.separator+"success");
						    if(success_dir.exists())
						    {
						    	Files.move(sourceFilePath, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
						    }
						    else
						    {
						    	success_dir.mkdir();
						    	Files.move(sourceFilePath, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
						    }
						    
						    String msg=xml_name+" Uploaded Successfully!";
						    try
							{
								new automation.Auto_InfoLogger().InsertInfoLogger("0", "","System", ip, "0", "Auto SF INBOUND XML","0","Auto SF INBOUND XML", "", "", msg);  	
							}
							catch(Exception infoLogger)
							{
								new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
							}
		        		}
					}
				}	
	        }
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
}