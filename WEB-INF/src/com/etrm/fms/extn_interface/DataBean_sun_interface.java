package com.etrm.fms.extn_interface;

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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.XmlUtilBean;

public class DataBean_sun_interface
{
	String db_src_file_name="DataBean_sun_interface.java";
	Connection conn;
	
	PreparedStatement stmt;
	PreparedStatement stmt0;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt5;
	PreparedStatement stmt6;
	
	ResultSet rset;
	ResultSet rset0;
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
	ResultSet rset6;
	
	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();
	XmlUtilBean xmlUtil = new XmlUtilBean();
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	
	public HttpServletRequest request = null;
	public void setRequest(HttpServletRequest request) {this.request = request;}
	
	public void init()
	{
		String function_nm="init()";
		synchronized(this)		//for handling parallel generation of SUN XML
		{
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
						if(callFlag.equalsIgnoreCase("TAX_STRUCTURE_SUN"))
						{
							getTaxCategoryMaster();
							getBusinessUnit();
							getTaxStructure();
							getSunCode();
							getSubSunCode();
						}
						else if(callFlag.equalsIgnoreCase("SUN_ENTITY_ACC_CD"))
						{
							getSunAccountEntityWise();
							getCounterpartyEntityWise();
							getAccountDetails();
							getSunAccountDtlsPlantWise();
						}
						else if(callFlag.equalsIgnoreCase("INVOICE_SUN_XML_GENERATION"))
						{
							getSunApprovedList();
						}
						else if(callFlag.equalsIgnoreCase("INVOICE_SUN_XML_DOWNLOAD"))
						{
							getXmlFilesList();
							parseSunXML();
						}
						else if(callFlag.equalsIgnoreCase("INVOICE_SUN_APPROVAL"))
						{
							getSegment();
							getTEMP_ReceivableTracking();
						}
						else if(callFlag.equalsIgnoreCase("REMITTANCE_SUN_APPROVAL"))
						{
							getRemittanceSegment();
							getPurchaseRemittanceApproval();
						}
						else if(callFlag.equalsIgnoreCase("REMITTANCE_SUN_XML_GENERATION"))
						{
							getRemittanceSegment();
							getPurSunApprovedList();
						}
						
						else if(callFlag.equalsIgnoreCase("GENERATE_SALES_SUN_XML"))
						{
							generateSalesSunXML();
						}
						else if(callFlag.equalsIgnoreCase("GENERATE_PUR_SUN_XML"))
						{
							generatePurSunXML();
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
				
				if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt0 != null){try{stmt0.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}	
				if(stmt6 != null){try{stmt6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}	
				
				if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			}
		}
		
	}
	
	public void getBusinessUnit()
	{
		String function_nm="getBusinessUnit()";
		try
		{
			String queryString="SELECT SEQ_NO,PLANT_ABBR,PLANT_NAME,PLANT_STATE "
					+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
					+ "WHERE COMPANY_CD=? AND ENTITY=? "
					+ "AND EFF_DT = (SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.ENTITY=B.ENTITY AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.SEQ_NO=B.SEQ_NO) ";
			stmt0 = conn.prepareStatement(queryString);
			stmt0.setString(1, comp_cd);
			stmt0.setString(2, "B");
			rset0 = stmt0.executeQuery();
			while(rset0.next())
			{
				String seq_no= rset0.getString(1)==null?"":rset0.getString(1);
				String bu_abbr = rset0.getString(2)==null?"":rset0.getString(2);
				String plant_state = rset0.getString(4)==null?"":rset0.getString(4);
				
				String co_cd = utilBean.getCompanySAPcode(conn, comp_cd);
				VBU_SEQ.add(seq_no);
				VBU_ABBR.add(bu_abbr);
				VCO_CD.add(co_cd);
				VBU_STATE.add(plant_state);
			}
			rset0.close();
			stmt0.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String Tax_CategoryName(String flag)
	{
		String function_nm="Tax_CategoryName()";
		String tax_category_nm="";
		try
		{
			if(flag.equals("P"))
			{
				tax_category_nm="Product";
			}
			else if(flag.equals("S"))
			{
				tax_category_nm="Service";
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return tax_category_nm;
	}
	
	public void getTaxCategoryMaster()
	{
		String function_nm="getTaxCategoryMaster()";
		try
		{
			String queryString="SELECT DISTINCT TAX_CATEGORY "
					+ "FROM FMS_TAX_STRUCTURE "
					+ "WHERE TAX_CATEGORY IS NOT NULL ";
			stmt=conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String tax_category=rset.getString(1)==null?"":rset.getString(1);
				String tax_category_nm = Tax_CategoryName(tax_category);
				VMASTER_TAX_CATEGORY.add(tax_category);
				VMASTER_TAX_CATEGORY_NM.add(tax_category_nm+" Tax/s");
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getTaxStructure()
	{
		String function_nm="getTaxStructure()";
		try
		{
			for(int i=0; i<VMASTER_TAX_CATEGORY.size(); i++)
			{
				int index=0;
				String queryString="SELECT TAX_STR_CD,DESCR,STATUS,REMARK,TAX_CATEGORY,TO_CHAR(APP_DATE,'DD/MM/YYYY'),"
						+ "SAP_TAX_CODE,SAP_GL,PAY_RECV "
						+ "FROM FMS_TAX_STRUCTURE A "
						+ "WHERE TAX_CATEGORY=? "
						+ "AND APP_DATE=(SELECT MAX(B.APP_DATE) FROM FMS_TAX_STRUCTURE B "
						+ "WHERE A.TAX_STR_CD=B.TAX_STR_CD AND B.APP_DATE<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) "
						+ "ORDER BY TAX_STR_CD";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, ""+VMASTER_TAX_CATEGORY.elementAt(i));
				rset=stmt.executeQuery();
				while(rset.next())
				{
					int tax_count=0;
					String tax_cd="";
					index+=1;
					String tax_struct_cd=rset.getString(1)==null?"":rset.getString(1);
					
					//for fetching the tax code 
					String queryString2="SELECT TAX_CODE FROM FMS_TAX_STRUCTURE_DTL "
							+ "WHERE TAX_STR_CD=?";
					stmt1=conn.prepareStatement(queryString2);
					stmt1.setString(1,tax_struct_cd);
					rset1 = stmt1.executeQuery();
					while(rset1.next())
					{
						tax_count++;
						String temp=rset1.getString(1)==null?"":rset1.getString(1);
						if(tax_cd.equals(""))
						{
							tax_cd+=temp;
						}
						else
						{
							tax_cd+=","+temp;
						}
					}
					rset1.close();
					stmt1.close();
					
					String descr=rset.getString(2)==null?"":rset.getString(2);
					VTAX_CD.add(tax_cd);
					VTAX_COUNT.add(""+tax_count);
					VTAX_STRUCT_CD.add(tax_struct_cd);
					VTAX_STRUCT_NM.add(descr);
					VTAX_STRUCT_STATUS.add(rset.getString(3)==null?"":rset.getString(3));
					VTAX_STRUCT_RMK.add(rset.getString(4)==null?"":rset.getString(4));
					String tax_category=rset.getString(5)==null?"P":rset.getString(5);
					VTAX_CATEGORY.add(tax_category);
					VTAX_CATEGORY_NM.add(""+Tax_CategoryName(tax_category));
					VTAX_STRUCT_APP_DT.add(rset.getString(6)==null?"":rset.getString(6));
					VSAP_TAX_CODE.add(rset.getString(7)==null?"":rset.getString(7));
					String pay_recv=rset.getString(9)==null?"":rset.getString(9);
					String pay_recv_nm="";
					if(pay_recv.equals("P"))
					{
						pay_recv_nm="Payable";
					}
					else if(pay_recv.equals("R"))
					{
						pay_recv_nm="Receivable";
					}
					VPAY_RECV_NM.add(pay_recv_nm);	
					
					if(tax_count>1)
					{
						String [] temp = descr.split(",");
						for(int m=0;m<temp.length;m++)
						{
							temp[m]=temp[m].trim();
							VSUB_TAX_STRUCT_NM.add(temp[m]);
						}
					}
				}
				VINDEX.add(index);
				rset.close();
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSunCode()
	{
		String function_nm="getSunCode()";
		try
		{
			for(int i=0;i<VTAX_STRUCT_CD.size();i++)
			{
				for(int j=0;j<getVBU_SEQ().size();j++)
				{
					for(int k=0;k<2;k++)
					{
						int count=0;
						String query="SELECT COUNT(*) FROM FMS_TAX_STRUCTURE_SUN "
								+ "WHERE TAX_STR_CD=? AND BU_UNIT=? AND COMPANY_CD=? AND ACCOUNT_TYPE=? "; //AND TAX_CODE=?";
						stmt3=conn.prepareStatement(query);
						stmt3.setString(1, ""+VTAX_STRUCT_CD.elementAt(i));
						stmt3.setString(2, ""+VBU_SEQ.elementAt(j));
						stmt3.setString(3, comp_cd);
						if(k==0)
						{
							stmt3.setString(4, "S");
						}
						else
						{
							stmt3.setString(4, "UG");
						}
						rset3=stmt3.executeQuery();
						if(rset3.next())
						{
							count=rset3.getInt(1);
						}
						rset3=stmt3.executeQuery();
						rset3.close();
						stmt3.close();
						if(count>0)
						{
							String sunCd="";
							String query1="SELECT SUN_CODE FROM FMS_TAX_STRUCTURE_SUN "
									+ "WHERE TAX_STR_CD=? AND BU_UNIT=? AND COMPANY_CD=? AND ACCOUNT_TYPE=? ";//AND TAX_CODE=?";
							stmt3=conn.prepareStatement(query1);
							stmt3.setString(1, ""+VTAX_STRUCT_CD.elementAt(i));
							stmt3.setString(2, ""+VBU_SEQ.elementAt(j));
							stmt3.setString(3, comp_cd);
							if(k==0)
							{
								stmt3.setString(4, "S");
							}
							else
							{
								stmt3.setString(4, "UG");
							}
							rset3=stmt3.executeQuery();
							while(rset3.next())
							{
								String sun_cd = rset3.getString(1)==null?"":rset3.getString(1);
								if(sunCd.equals(""))
								{
									sunCd+=""+sun_cd;
								}
								else
								{
									sunCd+=","+sun_cd;
								}
							}
							if(k==0)
							{
								VSUN_CD.add(sunCd);
							}
							else
							{
								VSUG_CD.add(sunCd);
							}
							rset3.close();
							stmt3.close();
						}
						else
						{
							if(k==0)
							{
								VSUN_CD.add("");
							}
							else
							{
								VSUG_CD.add("");
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSubSunCode()
	{
		String function_nm="getSubSunCode()";
		try
		{
			for(int i=0;i<VTAX_STRUCT_CD.size();i++)
			{
				if(Integer.parseInt(""+VTAX_COUNT.elementAt(i))>1)
				{
					String tax_cd[]=VTAX_CD.elementAt(i).toString().split(",");
					for(int k=0;k<tax_cd.length;k++)
					{
						for(int j=0;j<VBU_SEQ.size();j++)
						{
							for(int l=0;l<2;l++)
							{
								int count=0;
								String query="SELECT COUNT(*) FROM FMS_TAX_STRUCTURE_SUN "
										+ "WHERE TAX_STR_CD=? AND BU_UNIT=? AND TAX_CODE=? AND COMPANY_CD=? AND ACCOUNT_TYPE=? ";
								stmt3=conn.prepareStatement(query);
								stmt3.setString(1, ""+VTAX_STRUCT_CD.elementAt(i));
								stmt3.setString(2, ""+VBU_SEQ.elementAt(j));
								stmt3.setString(3, ""+tax_cd[k]);
								stmt3.setString(4, comp_cd);
								if(l==0)
								{
									stmt3.setString(5, "S");
								}
								else
								{
									stmt3.setString(5, "UG");
								}
								rset3=stmt3.executeQuery();
								if(rset3.next())
								{
									count=rset3.getInt(1);
								}
								rset3=stmt3.executeQuery();
								rset3.close();
								stmt3.close();
								if(count>0)
								{
									String sunCd="";
									String query1="SELECT SUN_CODE FROM FMS_TAX_STRUCTURE_SUN "
											+ "WHERE TAX_STR_CD=? AND BU_UNIT=? AND TAX_CODE=? AND COMPANY_CD=? AND ACCOUNT_TYPE=? ";
									stmt3=conn.prepareStatement(query1);
									stmt3.setString(1, ""+VTAX_STRUCT_CD.elementAt(i));
									stmt3.setString(2, ""+VBU_SEQ.elementAt(j));
									stmt3.setString(3, ""+tax_cd[k]);
									stmt3.setString(4, comp_cd);
									if(l==0)
									{
										stmt3.setString(5, "S");
									}
									else
									{
										stmt3.setString(5, "UG");
									}
									rset3=stmt3.executeQuery();
									while(rset3.next())
									{
										String sun_cd = rset3.getString(1)==null?"":rset3.getString(1);
										if(sunCd.equals(""))
										{
											sunCd+=""+sun_cd;
										}
										else
										{
											sunCd+=","+sun_cd;
										}
									}
									if(l==0)
									{
										VSUB_SUN_CD.add(sunCd);
									}
									else
									{
										VSUB_SUG_CD.add(sunCd);
									}
									rset3.close();
									stmt3.close();
								}
								else
								{
									if(l==0)
									{
										VSUB_SUN_CD.add("");
									}
									else
									{
										VSUB_SUG_CD.add("");
									}
								}
								
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSunAccountEntityWise()
	{
			/*
		 	For Customer Entity Account Type :
		 		1. Sales Account Code:	'S'
		 		2. REGAS Account Code:	'SI'
		 		3. SUG Account Code:	'UG'
		 	
		 	For Trader Entity Account Type :
		 		1. Purchase Account Code:		'S'
		 		2. IGX Purchase Account Code:	'I'
		 		3. Derivative Account Code:		'V'
		 		4. Custom Duty Account Code:	'CD'
		 	
		 	For Transporter Entity Account Type :
		 		1.Transporter Account Code:	'R'
		 		2.Parking Account Code:		'K'
		 */
		String function_nm="getSunAccountEntityWise()";
		try
		{
			if(entity_role.equals("C"))
			{
				VACCOUNT_TYPE.add("S");
				VACCOUNT_TYPE.add("SI");
				VACCOUNT_TYPE.add("UG");
				
				VACCOUNT_TYPE_NM.add("Sales Account Code");
				VACCOUNT_TYPE_NM.add("REGAS Account Code");
				VACCOUNT_TYPE_NM.add("SUG Account Code");
			}
			else if(entity_role.equals("T"))
			{
				VACCOUNT_TYPE.add("S");
				VACCOUNT_TYPE.add("I");
				VACCOUNT_TYPE.add("V");
				VACCOUNT_TYPE.add("CD");
				
				VACCOUNT_TYPE_NM.add("Purchase Account Code");
				VACCOUNT_TYPE_NM.add("IGX Purchase Account Code");
				VACCOUNT_TYPE_NM.add("Derivative Account Code");
				VACCOUNT_TYPE_NM.add("Custom Duty Account Code");
			}
			else if(entity_role.equals("R"))
			{
				VACCOUNT_TYPE.add("R");
				VACCOUNT_TYPE.add("K");
				
				VACCOUNT_TYPE_NM.add("Transporter Account Code");
				VACCOUNT_TYPE_NM.add("Parking Account Code");
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	
	public void getCounterpartyEntityWise()
	{
		String function_nm="getCounterpartyEntityWise()";
		try
		{
			String queryString="SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_ENTITY_REQ_DTL "
					+ "WHERE COMPANY_CD=? AND ENTITY=? "
					+ "AND STATUS=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, entity_role);
			stmt.setString(3, "A");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String counterpty_cd = rset.getString(1)==null?"":rset.getString(1);
				getPlantDetails(counterpty_cd);		//20250526 only for Trader
				VCOUNTERPARTY_CD.add(counterpty_cd);
				VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
				VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn, counterpty_cd));
				VCOUNTERPARTY_CATEGORY.add(""+utilBean.getCounterpartyCategory(conn, counterpty_cd));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getAccountDetails()
	{
		String function_nm="getAccountDetails()";
		try
		{
			//FOR FETCHING SUN ACCOUNT CODE
			for(int i=0;i<VCOUNTERPARTY_CD.size();i++)
			{
				int count=0;
				String queryString1="SELECT COUNT(*) FROM FMS_ENTITY_ACCOUNT_CODE_SUN "
						+ "WHERE ACCOUNT_TYPE=? AND ENTITY=? AND COUNTERPARTY_CD=? "
						+ "AND COMPANY_CD=? AND PLANT_SEQ_NO=? ";
				stmt4=conn.prepareStatement(queryString1);
				stmt4.setString(1, "DFT");
				stmt4.setString(2, entity_role);
				stmt4.setString(3, ""+VCOUNTERPARTY_CD.elementAt(i));
				stmt4.setString(4, comp_cd);
				stmt4.setString(5, "0");
				rset4 = stmt4.executeQuery();
				if(rset4.next())
				{
					count=rset4.getInt(1);
				}
				rset4.close();
				stmt4.close();
				
				if(count>0)
				{
					String queryString="SELECT ACCOUNT_CODE FROM FMS_ENTITY_ACCOUNT_CODE_SUN "
							+ "WHERE ACCOUNT_TYPE=? AND ENTITY=? AND COUNTERPARTY_CD=? "
							+ "AND COMPANY_CD=? AND PLANT_SEQ_NO=? ";
					stmt4=conn.prepareStatement(queryString);
					stmt4.setString(1, "DFT");
					stmt4.setString(2, entity_role);
					stmt4.setString(3, ""+VCOUNTERPARTY_CD.elementAt(i));
					stmt4.setString(4, comp_cd);
					stmt4.setString(5, "0");
					rset4 = stmt4.executeQuery();
					while(rset4.next())
					{
						VSUN_ACCOUNT.add(rset4.getString(1)==null?"":rset4.getString(1));
					}
					rset4.close();
					stmt4.close();
				}
				else
				{
					VSUN_ACCOUNT.add("");
				}
				
				//for fetching the other types sun account 
				count=0;
				for(int j=0;j<VACCOUNT_TYPE.size();j++)
				{
					String queryString2="SELECT COUNT(*) FROM FMS_ENTITY_ACCOUNT_CODE_SUN "
							+ "WHERE ACCOUNT_TYPE=? AND ENTITY=? AND COUNTERPARTY_CD=? "
							+ "AND COMPANY_CD=? AND PLANT_SEQ_NO=? ";
					stmt4=conn.prepareStatement(queryString2);
					stmt4.setString(1, ""+VACCOUNT_TYPE.elementAt(j));
					stmt4.setString(2, entity_role);
					stmt4.setString(3, ""+VCOUNTERPARTY_CD.elementAt(i));
					stmt4.setString(4, comp_cd);
					stmt4.setString(5, "0");
					rset4 = stmt4.executeQuery();
					if(rset4.next())
					{
						count=rset4.getInt(1);
					}
					rset4.close();
					stmt4.close();
					
					if(count>0)
					{
						String queryString3="SELECT ACCOUNT_CODE FROM FMS_ENTITY_ACCOUNT_CODE_SUN "
								+ "WHERE ACCOUNT_TYPE=? AND ENTITY=? AND COUNTERPARTY_CD=? "
								+ "AND COMPANY_CD=? AND PLANT_SEQ_NO=? ";
						stmt4=conn.prepareStatement(queryString3);
						stmt4.setString(1, ""+VACCOUNT_TYPE.elementAt(j));
						stmt4.setString(2, entity_role);
						stmt4.setString(3, ""+VCOUNTERPARTY_CD.elementAt(i));
						stmt4.setString(4, comp_cd);
						stmt4.setString(5, "0");
						rset4 = stmt4.executeQuery();
						while(rset4.next())
						{
							VSUN_ENTITY_ACCOUNT.add(rset4.getString(1)==null?"":rset4.getString(1));
						}
						rset4.close();
						stmt4.close();
					}
					else
					{
						VSUN_ENTITY_ACCOUNT.add("");
					}
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getPlantDetails(String counterparty_cd)
	{
		String function_nm = "getPlantDetails()";
		try
		{
			if(entity_role.equals("T"))		//AS ON 20250526 only for Trader entity  
			{
				int indx=0;
				String queryString="SELECT SEQ_NO,PLANT_ABBR,PLANT_NAME "
						+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND ENTITY=? AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY "
						+ "AND A.SEQ_NO=B.SEQ_NO) ";
				stmt5=conn.prepareStatement(queryString);
				stmt5.setString(1, comp_cd);
				stmt5.setString(2, counterparty_cd);
				stmt5.setString(3, entity_role);
				rset5=stmt5.executeQuery();
				while(rset5.next())
				{
					indx++;
					String plant_seq_no = rset5.getString(1)==null?"":rset5.getString(1);
					String plant_abbr = rset5.getString(2)==null?"":rset5.getString(2);
					VPLANT_SEQ_NO.add(plant_seq_no);
					VPLANT_ABBR.add(plant_abbr);
				}
				VPLANT_INDEX.add(indx);
				rset5.close();
				stmt5.close();
				
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSunAccountDtlsPlantWise()
	{
		String function_nm="getSunAccountDtlsPlantWise()";
		try
		{
			if(entity_role.equals("T"))
			{
				for(int i=0;i<VCOUNTERPARTY_CD.size();i++)
				{
					String queryString="SELECT SEQ_NO "
							+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND ENTITY=? "
							+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY "
							+ "AND A.SEQ_NO=B.SEQ_NO) ";
					stmt5=conn.prepareStatement(queryString);
					stmt5.setString(1, comp_cd);
					stmt5.setString(2, ""+VCOUNTERPARTY_CD.elementAt(i));
					stmt5.setString(3, entity_role);
					rset5=stmt5.executeQuery();
					while(rset5.next())
					{
						String plant_seq_no = rset5.getString(1)==null?"":rset5.getString(1);
						int count =0;
						
						String queryString1="SELECT COUNT(*) FROM FMS_ENTITY_ACCOUNT_CODE_SUN "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ACCOUNT_TYPE=? "
								+ "AND ENTITY=? AND PLANT_SEQ_NO=? ";
						stmt6=conn.prepareStatement(queryString1);
						stmt6.setString(1, comp_cd);
						stmt6.setString(2, ""+VCOUNTERPARTY_CD.elementAt(i));
						stmt6.setString(3, "DFT");
						stmt6.setString(4, entity_role);
						stmt6.setString(5, plant_seq_no);
						rset6=stmt6.executeQuery();
						if(rset6.next())
						{
							count=rset6.getInt(1);
						}
						rset6.close();
						stmt6.close();
						
						if(count>0)
						{
							String queryString2="SELECT ACCOUNT_CODE FROM FMS_ENTITY_ACCOUNT_CODE_SUN "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ACCOUNT_TYPE=? "
									+ "AND ENTITY=? AND PLANT_SEQ_NO=? ";
							stmt6=conn.prepareStatement(queryString2);
							stmt6.setString(1, comp_cd);
							stmt6.setString(2, ""+VCOUNTERPARTY_CD.elementAt(i));
							stmt6.setString(3, "DFT");
							stmt6.setString(4, entity_role);
							stmt6.setString(5, plant_seq_no);
							rset6=stmt6.executeQuery();
							if(rset6.next())
							{
								VACC_PLANT.add(rset6.getString(1)==null?"":rset6.getString(1));
							}
							rset6.close();
							stmt6.close();
						}
						else
						{
							VACC_PLANT.add("");
						}
						
						//for other account type 
						for(int j=0;j<VACCOUNT_TYPE.size();j++)
						{
							count=0;
							queryString1="SELECT COUNT(*) FROM FMS_ENTITY_ACCOUNT_CODE_SUN "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ACCOUNT_TYPE=? "
									+ "AND ENTITY=? AND PLANT_SEQ_NO=? ";
							stmt6=conn.prepareStatement(queryString1);
							stmt6.setString(1, comp_cd);
							stmt6.setString(2, ""+VCOUNTERPARTY_CD.elementAt(i));
							stmt6.setString(3, ""+VACCOUNT_TYPE.elementAt(j));
							stmt6.setString(4, entity_role);
							stmt6.setString(5, plant_seq_no);
							rset6=stmt6.executeQuery();
							if(rset6.next())
							{
								count=rset6.getInt(1);
							}
							rset6.close();
							stmt6.close();
							
							if(count>0)
							{
								String queryString2="SELECT ACCOUNT_CODE FROM FMS_ENTITY_ACCOUNT_CODE_SUN "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ACCOUNT_TYPE=? "
										+ "AND ENTITY=? AND PLANT_SEQ_NO=? ";
								stmt6=conn.prepareStatement(queryString2);
								stmt6.setString(1, comp_cd);
								stmt6.setString(2, ""+VCOUNTERPARTY_CD.elementAt(i));
								stmt6.setString(3, ""+VACCOUNT_TYPE.elementAt(j));
								stmt6.setString(4, entity_role);
								stmt6.setString(5, plant_seq_no);
								rset6=stmt6.executeQuery();
								if(rset6.next())
								{
									VACC_OTH_PLANT.add(rset6.getString(1)==null?"":rset6.getString(1));
								}
								rset6.close();
								stmt6.close();
							}
							else
							{
								VACC_OTH_PLANT.add("");
							}
						}
					}
					rset5.close();
					stmt5.close();
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//for fetching the list for approval
	public void getTEMP_ReceivableTracking()
	{
		String function_nm="getTEMP_ReceivableTracking()";
		
		try
		{
			//VSEGMENT_TYPE.add("O");
			//VSEGMENT.add("LTCORA");
			if(segment.equals("S"))
			{
				VTEMP_SEGMENT_TYPE.add("S");
				VTEMP_SEGMENT.add("RLNG(SN,LOA)");
			}
			else if(segment.equals("X"))
			{
				VTEMP_SEGMENT_TYPE.add("X");
				VTEMP_SEGMENT.add("IGX");
			}
			else if(segment.equals("O"))
			{
				VTEMP_SEGMENT_TYPE.add("O");
				VTEMP_SEGMENT.add("LTCORA");
			}
			else if(segment.equals("E"))
			{
				VTEMP_SEGMENT_TYPE.add("E");
				VTEMP_SEGMENT.add("DLNG(SN,LOA,IGX,TMS,TLU)");
			}
			else if(segment.equals("V"))
			{
				VTEMP_SEGMENT_TYPE.add("V");
				VTEMP_SEGMENT.add("Derivatives");
			}
			else
			{
				//VTEMP_SEGMENT_TYPE.add("S");
				//VTEMP_SEGMENT.add("RLNG(SN,LOA)");
				VTEMP_SEGMENT_TYPE=VSEGMENT_TYPE;
				VTEMP_SEGMENT=VSEGMENT;
			}
			for(int i=0;i<VTEMP_SEGMENT_TYPE.size();i++)
			{
				int index=0;
				String queryString="";
				int ctn=0;
				if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("E"))
				{
					queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,FINANCIAL_YEAR,  "
							+ "BU_STATE_TIN,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY') INV_DT,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'), "
							+ "TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),SALE_PRICE,SALE_PRICE_UNIT, "
							+ "SALE_AMT,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),INVOICE_AMT,NET_PAYABLE_AMT, "
							+ "TCS_TDS,TCS_AMT,TCS_FACTOR,TDS_GROSS_AMT,TDS_GROSS_PERCENT,TDS_TAX_AMT,TDS_TAX_PERCENT, "
							+ "PAY_RECV_AMT,TO_CHAR(PAY_RECV_DT,'DD/MM/YYYY'),PLANT_SEQ,BU_UNIT,ALLOC_QTY,INVOICE_RAISED_IN,TCS_TDS, "
							+ "TCS_AMT,TCS_FACTOR,NULL,SAP_APPROVAL, "
							+ "NULL,NULL,NULL,SAP_APPROVED_BY,TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),INV_FLAG  "
							+ "FROM FMS_DLNG_INVOICE_MST A  "
							+ "WHERE COMPANY_CD=? "
							+ "AND INVOICE_DT>=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND APPROVED_FLAG=? AND INVOICE_ID_SEQ IS NOT NULL AND INVOICE_AMT IS NOT NULL  "
							+ "AND PDF_INV_DTL IS NOT NULL "
							+ "AND ((SELECT COUNT(*) FROM FMS_DLNG_INV_FILE_DTL B  "
							+ "WHERE B.COMPANY_CD=A.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ  "
							+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND B.PDF_TYPE=? )=0  "
							+ "OR (SELECT COUNT(*) FROM FMS_DLNG_INV_FILE_DTL B  "
							+ "WHERE B.COMPANY_CD=A.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ  "
							+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND B.PDF_TYPE='S' )>0) ";
					if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("E"))
					{
						queryString+="AND CONTRACT_TYPE IN (?,?,?) ";
					}
					//queryString+= "ORDER BY INVOICE_DT ";
					queryString+="UNION ALL ";
					queryString+="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,FINANCIAL_YEAR,  "
							+ "BU_STATE_TIN,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY') INV_DT,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'), "
							+ "TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),SALE_PRICE,SALE_PRICE_UNIT, "
							+ "SALE_AMT,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,NULL,INVOICE_AMT,NET_PAYABLE_AMT, "
							+ "TCS_TDS,TCS_AMT,TCS_FACTOR,TDS_GROSS_AMT,TDS_GROSS_PERCENT,TDS_TAX_AMT,TDS_TAX_PERCENT, "
							+ "PAY_RECV_AMT,TO_CHAR(PAY_RECV_DT,'DD/MM/YYYY'),PLANT_SEQ,BU_UNIT,QTY,INVOICE_RAISED_IN,TCS_TDS, "
							+ "TCS_AMT,TCS_FACTOR,NULL,SAP_APPROVAL, "
							+ "NULL,NULL,CARGO_NO,SAP_APPROVED_BY,TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),INV_FLAG "
							+ "FROM FMS_DLNG_SVC_INVOICE_MST A "
							+ "WHERE COMPANY_CD=? "
							+ "AND INVOICE_DT>=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND APPROVED_FLAG=? AND INVOICE_ID_SEQ IS NOT NULL AND INVOICE_AMT IS NOT NULL "
							+ "AND PDF_INV_DTL IS NOT NULL "
							+ "AND ((SELECT COUNT(*) FROM FMS_DLNG_SVC_INV_FILE_DTL B "
							+ "WHERE B.COMPANY_CD=A.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ  "
							+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND B.PDF_TYPE=? )=0 "
							+ "OR (SELECT COUNT(*) FROM FMS_DLNG_SVC_INV_FILE_DTL B "
							+ "WHERE B.COMPANY_CD=A.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
							+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND B.PDF_TYPE='S' )>0) ";
					if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("E"))
					{
						queryString+="AND CONTRACT_TYPE IN (?,?,?,?) ";		//for service-order "B", term-sheet "M" is hold right now O,Q for LTCORA 
					}
					queryString+= "ORDER BY INV_DT ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(++ctn, comp_cd);
					stmt.setString(++ctn, from_dt);
					stmt.setString(++ctn, to_dt);
					stmt.setString(++ctn, "Y");
					stmt.setString(++ctn, "X");
					if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("E"))
					{
						stmt.setString(++ctn, "F");
						stmt.setString(++ctn, "E");
						stmt.setString(++ctn, "W");
					}
					stmt.setString(++ctn, comp_cd);
					stmt.setString(++ctn, from_dt);
					stmt.setString(++ctn, to_dt);
					stmt.setString(++ctn, "Y");
					stmt.setString(++ctn, "X");
					if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("E"))
					{
						stmt.setString(++ctn, "B");
						stmt.setString(++ctn, "M");
						stmt.setString(++ctn, "O");		//PB20260117: added for LTCORA 
						stmt.setString(++ctn, "Q");
					}
					rset=stmt.executeQuery();
				}
				else if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("V"))
				{
					getDerivativeInvoiceList("I");
				}
				else
				{
					queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,FINANCIAL_YEAR, "
							+ "BU_STATE_TIN,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),"
							+ "TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),SALE_PRICE,SALE_PRICE_UNIT,"
							+ "SALE_AMT,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),INVOICE_AMT,NET_PAYABLE_AMT,"
							+ "TCS_TDS,TCS_AMT,TCS_FACTOR,TDS_GROSS_AMT,TDS_GROSS_PERCENT,TDS_TAX_AMT,TDS_TAX_PERCENT,"
							+ "PAY_RECV_AMT,TO_CHAR(PAY_RECV_DT,'DD/MM/YYYY'),PLANT_SEQ,BU_UNIT,ALLOC_QTY,INVOICE_RAISED_IN,TCS_TDS,"
							+ "TCS_AMT,TCS_FACTOR,TRANSPORTATION_AMOUNT,SAP_APPROVAL,"
							+ "MARKET_MARGIN_AMT,OTHER_CHARGES_AMT,CARGO_NO,SAP_APPROVED_BY,TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),INV_FLAG "
							+ "FROM FMS_INVOICE_MST A "
							+ "WHERE COMPANY_CD=? "
							+ "AND INVOICE_DT>=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND APPROVED_FLAG=? AND INVOICE_ID_SEQ IS NOT NULL AND INVOICE_AMT IS NOT NULL "
							+ "AND PDF_INV_DTL IS NOT NULL "
							+ "AND ((SELECT COUNT(*) FROM FMS_INV_FILE_DTL B "
							+ "WHERE B.COMPANY_CD=A.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
							+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND B.PDF_TYPE=? )=0 "
							+ "OR (SELECT COUNT(*) FROM FMS_INV_FILE_DTL B "
							+ "WHERE B.COMPANY_CD=A.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
							+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND B.PDF_TYPE='S' )>0) ";
					
					if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("S") || VTEMP_SEGMENT_TYPE.elementAt(i).equals("O"))
					{
						queryString+="AND CONTRACT_TYPE IN (?,?) ";
					}
					else
					{
						queryString+="AND CONTRACT_TYPE=? ";
					}
					queryString+= "ORDER BY INVOICE_DT";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(++ctn, comp_cd);
					stmt.setString(++ctn, from_dt);
					stmt.setString(++ctn, to_dt);
					stmt.setString(++ctn, "Y");
					stmt.setString(++ctn, "X");
					if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("S"))
					{
						stmt.setString(++ctn, "S");
						stmt.setString(++ctn, "L");
					}
					else if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("O"))
					{
						stmt.setString(++ctn, "O");
						stmt.setString(++ctn, "Q");
					}
					else
					{
						stmt.setString(++ctn, ""+VTEMP_SEGMENT_TYPE.elementAt(i));
					}
					rset=stmt.executeQuery();
				}
				if(!VTEMP_SEGMENT_TYPE.elementAt(i).equals("V"))
				{
					while(rset.next())
					{
						index++;
						String companyCd = rset.getString(1)==null?"":rset.getString(1);
						String countpty_cd = rset.getString(2)==null?"":rset.getString(2);
						String agmt = rset.getString(3)==null?"":rset.getString(3);
						String agmt_rev = rset.getString(4)==null?"":rset.getString(4);
						String cont = rset.getString(5)==null?"":rset.getString(5);
						String cont_rev = rset.getString(6)==null?"":rset.getString(6);
						String cont_type = rset.getString(7)==null?"":rset.getString(7);
						String finan_yr = rset.getString(8)==null?"":rset.getString(8);
						String bu_state_tin = rset.getString(9)==null?"":rset.getString(9);
						String inv_seq = rset.getString(10)==null?"":rset.getString(10);
						String inv_no = rset.getString(11)==null?"":rset.getString(11);
						String inv_dt = rset.getString(12)==null?"":rset.getString(12);
						String period_st_dt = rset.getString(13)==null?"":rset.getString(13);
						String period_end_dt = rset.getString(14)==null?"":rset.getString(14);
						String inv_due_dt = rset.getString(15)==null?"":rset.getString(15);
						double sales_price=rset.getDouble(16);
						String sales_price_cd = rset.getString(17)==null?"":rset.getString(17);
						String cargo_no = rset.getString(45)==null?"":rset.getString(45);
						String sun_approved_by = rset.getString(46)==null?"":rset.getString(46);
						String sun_approved_dt = rset.getString(47)==null?"":rset.getString(47);
						String inv_flag = rset.getString(48)==null?"":rset.getString(48);
						
						double sales_amt = rset.getDouble(18);
						double gross_amt = rset.getDouble(19);
						double tax_amt = rset.getDouble(20);
						String tax_struct_cd = rset.getString(21)==null?"":rset.getString(21);
						String tax_struct_dt = rset.getString(22)==null?"":rset.getString(22);
						
						double invoice_amt = rset.getDouble(23);
						double net_payable = rset.getDouble(24);
						String tds_tcs_flag = rset.getString(25)==null?"":rset.getString(25);
						double tcs_amt = rset.getDouble(26);
						//27
						String temp_tds_gross_amt = rset.getString(28)==null?"":rset.getString(28);
						double tds_gross_amt = rset.getDouble(28);
						String temp_tds_gross_per = rset.getString(29)==null?"":rset.getString(29);
						double tds_gross_per = rset.getDouble(29);
						String temp_tds_tax_amt = rset.getString(30)==null?"":rset.getString(30);
						double tds_tax_amt = rset.getDouble(30);
						String temp_tds_tax_per = rset.getString(31)==null?"":rset.getString(31);
						double tds_tax_per = rset.getDouble(31);
						
						String temp_pay_recv_amt = rset.getString(32)==null?"":rset.getString(32);
						double pay_recv_amt = rset.getDouble(32);
						String temp_pay_recv_dt = rset.getString(33)==null?"":rset.getString(33);
						
						String plant_seq = rset.getString(34)==null?"":rset.getString(34);
						
						String transportation_amount=rset.getString(41)==null?"":nf.format(rset.getDouble(41));
						String marketing_margin_amount=rset.getString(43)==null?"":nf.format(rset.getDouble(43));
						String other_charges_amount=rset.getString(44)==null?"":nf.format(rset.getDouble(44));
						
						if(!transportation_amount.equals(""))
						{
							gross_amt+=Double.parseDouble(transportation_amount);
						}
						
						if(!marketing_margin_amount.equals(""))
						{
							gross_amt+=Double.parseDouble(marketing_margin_amount);
						}
						
						if(!other_charges_amount.equals(""))
						{
							gross_amt+=Double.parseDouble(other_charges_amount);
						}
						
						if(!temp_tds_tax_amt.equals(""))
						{
							VTDS_TAX_AMT.add(nf.format(tds_tax_amt));
						}
						else{
							VTDS_TAX_AMT.add("");
						}
						
						if(!temp_tds_tax_per.equals("")){
							VTDS_TAX_PERCENT.add(nf.format(tds_tax_per));
						}else{
							VTDS_TAX_PERCENT.add("");
						}
						
						if(!temp_pay_recv_amt.equals("")){
							VPAY_RECV_AMT.add(nf.format(pay_recv_amt));
						}else{
							VPAY_RECV_AMT.add("");
						}
						VPAY_RECV_DT.add(temp_pay_recv_dt);
						
						VCOUNTERPARTY_CD.add(countpty_cd);
						VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
						VAGMT_NO.add(agmt);
						VAGMT_REV_NO.add(agmt_rev);
						VCONT_NO.add(cont);
						VCONT_REV_NO.add(cont_rev);
						VCONTRACT_TYPE.add(cont_type);
						VDIS_CONT_MAPPING.add(""+utilBean.NewDealMappingId(companyCd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo_no));
						VFINANCIAL_YEAR.add(finan_yr);
						VBU_STATE_TIN.add(bu_state_tin);
						VINVOICE_SEQ.add(inv_seq);
						VINVOICE_NO.add(inv_no);
						VINVOICE_DT.add(inv_dt);
						VPERIOD_START_DT.add(period_st_dt);
						VPERIOD_END_DT.add(period_end_dt);
						VINVOICE_DUE_DT.add(inv_due_dt);
						VSALES_PRICE.add(""+utilBean.RateNumberFormat(sales_price, sales_price_cd));
						VSALES_PRICE_CD.add(sales_price_cd);
						VSALES_PRICE_NM.add(""+utilBean.getRateUnitNm(conn,sales_price_cd));
						VGROSS_AMT.add(nf.format(gross_amt));
						VTAX_AMT.add(nf.format(Math.abs(tax_amt)));
						VINVOICE_AMT.add(nf.format(Math.abs(invoice_amt)));
						
						net_payable=net_payable-tds_gross_amt-tds_tax_amt;
						VNET_PAYABLE_AMT.add(nf.format(Math.abs(net_payable)));
						
						double short_received = net_payable - pay_recv_amt;
						VSHORT_RECEIVED.add(nf.format(short_received));
						
						VTDS_TCS_FLAG.add(tds_tcs_flag);
						VTCS_AMT.add(nf.format(Math.abs(tcs_amt)));
						
						/*queryString1="SELECT TAX_STRUCT_DTL "
					+ "FROM FMS_ENTITY_TAX_STRUCT_DTL A "
					+ "WHERE A.COMPANY_CD=? AND A.TAX_STRUCT_CD=? "
					+ "AND A.ENTITY=? AND A.COUNTERPARTY_CD=? AND A.PLANT_SEQ_NO=? "
					+ "AND A.TAX_STRUCT_DT=(SELECT MAX(D.TAX_STRUCT_DT) FROM FMS_ENTITY_TAX_STRUCT_DTL D WHERE A.COMPANY_CD=D.COMPANY_CD "
					+ "AND A.ENTITY=D.ENTITY AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.PLANT_SEQ_NO=D.PLANT_SEQ_NO "
					+ "AND D.TAX_STRUCT_DT <= TO_DATE(?,'DD/MM/YYYY')) ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, tax_struct_cd);
					stmt1.setString(3, "C");
					stmt1.setString(4, countpty_cd);
					stmt1.setString(5, plant_seq);
					stmt1.setString(6, tax_struct_dt);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						VTAX_STRUCT_DTL.add(rset1.getString(1)==null?"":rset1.getString(1));
					}
					else
					{
						VTAX_STRUCT_DTL.add("");
					}
					rset1.close();
					stmt1.close();*/
						
						VTAX_STRUCT_DTL.add(utilBean.getTaxDescr(conn, tax_struct_cd));
						
						String bu_plant_seq=rset.getString(35)==null?"":rset.getString(35);
						VBU_NM.add(""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B"));
						VSAP_APPROVAL_FLAG.add(rset.getString(42)==null?"":rset.getString(42));
						if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("E"))
						{
							if(cont_type.equals("B") || cont_type.equals("M")||cont_type.equals("O") || cont_type.equals("Q"))
							{
								VTYPE_FLAG.add("DLNG_SERV");
							}
							else
							{
								VTYPE_FLAG.add("DLNG_SG");
							}
						}
						else
						{
							VTYPE_FLAG.add("SG");
						}
						VALLOC_QTY.add(rset.getString(36)==null?"":nf.format(Math.abs(rset.getDouble(36))));
						VINVOICE_RAISED_IN.add(""+utilBean.getRateUnitNm(conn,rset.getString(37)==null?"":rset.getString(37)));
						VPAYMENT_DONE_IN.add("INR");
						String tcs_tds=rset.getString(38)==null?"":rset.getString(38);
						VTCS_TDS.add(tcs_tds);
						
						if(tcs_tds.equals("TCS"))
						{
							VTDS_GROSS_AMT.add(rset.getString(39)==null?"":nf.format(rset.getDouble(39)));
							VTDS_GROSS_PERCENT.add(rset.getString(40)==null?"":nf.format(rset.getDouble(40)));
						}
						else
						{
							if(!temp_tds_gross_amt.equals("")){
								VTDS_GROSS_AMT.add(nf.format(Math.abs(tds_gross_amt)));
							}else{
								VTDS_GROSS_AMT.add("");
							}
							
							if(!temp_tds_gross_per.equals("")){
								VTDS_GROSS_PERCENT.add(nf.format(Math.abs(tds_gross_per)));
							}else{
								VTDS_GROSS_PERCENT.add("");
							}
						}
						
						VSALES_AMT.add(nf.format(Math.abs(sales_amt)));
						
						VINVOICE_TYPE.add("");
						
						String contRef="";
						if(cont_type.equals("O") || cont_type.equals("Q"))
						{
							String queryString1="SELECT A.CONT_REF_NO "
									+ "FROM FMS_LTCORA_CONT_MST A,"
									+ "FMS_LTCORA_CONT_CARGO_DTL B "
									+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? "
									+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.AGMT_TYPE=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND B.CARGO_NO=? "
									+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
									+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
									+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
									+ ""
									+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
									+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
							stmt2=conn.prepareStatement(queryString1);
							stmt2.setString(1, companyCd);
							stmt2.setString(2, countpty_cd);
							stmt2.setString(3, "C");
							stmt2.setString(4, agmt);
							stmt2.setString(5, agmt_rev);
							stmt2.setString(6, "A");
							stmt2.setString(7, cont);
							stmt2.setString(8, cont_type);
							stmt2.setString(9, cargo_no);
							rset2=stmt2.executeQuery();
							if(rset2.next())
							{
								contRef=rset2.getString(1)==null?"":rset2.getString(1);
							}
							rset2.close();
							stmt2.close();
						}
						else if(cont_type.equals("B") || cont_type.equals("M"))
						{
							String queryString1="SELECT CONT_REF_NO FROM FMS_SVC_CONT_MST A "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? AND CONTRACT_TYPE=? "
									+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_SVC_CONT_MST B "
									+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
									+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
							stmt2=conn.prepareStatement(queryString1);
							stmt2.setString(1, companyCd);
							stmt2.setString(2, countpty_cd);
							stmt2.setString(3, agmt);
							stmt2.setString(4, agmt_rev);
							stmt2.setString(5, cont);
							stmt2.setString(6,cont_type);
							rset2=stmt2.executeQuery();
							if(rset2.next())
							{
								contRef=rset2.getString(1)==null?"":rset2.getString(1);
							}
							rset2.close();
							stmt2.close();
						}
						else
						{
							String queryString1="SELECT CONT_REF_NO,TRADE_REF_NO,AGMT_BASE "
									+ "FROM FMS_SUPPLY_CONT_MST A "
									+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
									+ "AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
									+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
									+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
							stmt2 = conn.prepareStatement(queryString1);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, cont_type);
							stmt2.setString(3, countpty_cd);
							stmt2.setString(4, agmt);
							stmt2.setString(5, cont);
							rset2=stmt2.executeQuery();
							if(rset2.next())
							{
								contRef=rset2.getString(1)==null?"":rset2.getString(1);
								if(cont_type.equals("X"))
								{
									contRef=rset2.getString(2)==null?"":rset2.getString(2);
								}
							}
							rset2.close();
							stmt2.close();
						}
						
						VCONT_REF_NO.add(contRef);
						if(inv_flag.equals("LP"))
						{
							VCASH_FLOW.add("Interest");
						}
						else
						{
							if(cont_type.equals("O") || cont_type.equals("Q"))
							{
								VCASH_FLOW.add("Capacity");
							}
							else if(cont_type.equals("B") || cont_type.equals("M"))
							{
								VCASH_FLOW.add("Service");
							}
							else
							{
								VCASH_FLOW.add("Commodity");
							}
						}
						
						VAPPROVE_HIST.add(utilBean.getEmpName(conn, sun_approved_by)+"<br>"+sun_approved_dt);
					}
					rset.close();
					stmt.close();
					
				}
				
				if(!VTEMP_SEGMENT_TYPE.elementAt(i).equals("V"))
				{
					ctn=0;
					if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("E"))
					{
						queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,FINANCIAL_YEAR,  "
								+ "BU_STATE_TIN,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'), "
								+ "TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),GROSS_AMT_INR,GROSS_AMT_INR, "
								+ "GROSS_AMT_INR,GROSS_AMT_INR,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),INVOICE_AMT,NET_PAYABLE_AMT, "
								+ "TCS_TDS,TCS_AMT,TCS_FACTOR,TDS_GROSS_AMT,TDS_GROSS_PERCENT,TDS_TAX_AMT,TDS_TAX_PERCENT, "
								+ "PAY_RECV_AMT,TO_CHAR(PAY_RECV_DT,'DD/MM/YYYY'),ADDR_FLAG,BU_UNIT,INVOICE_RAISED_IN,INVOICE_TYPE,SAP_APPROVAL, "
								+ "INVOICE_CATEGORY,ALLOC_QTY,CARGO_NO,SAP_APPROVED_BY,TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY')   "
								+ "FROM FMS_DLNG_FFLOW_INV_MST A   "
								+ "WHERE COMPANY_CD=?  "
								+ "AND INVOICE_DT>=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY')  "
								+ "AND APPROVED_FLAG=? AND INVOICE_ID_SEQ IS NOT NULL AND INVOICE_AMT IS NOT NULL   "
								+ "AND PDF_INV_DTL IS NOT NULL  "
								+ "AND ((SELECT COUNT(*) FROM FMS_DLNG_FFLOW_INV_FILE_DTL B   "
								+ "WHERE B.COMPANY_CD=A.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ   "
								+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND B.PDF_TYPE=? )=0   "
								+ "OR (SELECT COUNT(*) FROM FMS_DLNG_FFLOW_INV_FILE_DTL B   "
								+ "WHERE B.COMPANY_CD=A.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ   "
								+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND B.PDF_TYPE='S' )>0)";	
						if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("E"))
						{
							queryString+="AND CONTRACT_TYPE IN (?,?,?,?,?) ";
						}
						queryString+= "ORDER BY INVOICE_DT ";
						stmt1 = conn.prepareStatement(queryString);
						stmt1.setString(++ctn, comp_cd);
						stmt1.setString(++ctn, from_dt);
						stmt1.setString(++ctn, to_dt);
						stmt1.setString(++ctn, "Y");
						stmt1.setString(++ctn, "X");
						if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("E"))
						{
							stmt1.setString(++ctn, "F");
							stmt1.setString(++ctn, "E");
							stmt1.setString(++ctn, "W");
							stmt1.setString(++ctn, "O");
							stmt1.setString(++ctn, "Q");
						}
					}
					else
					{
						queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,FINANCIAL_YEAR, "
								+ "BU_STATE_TIN,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),"
								+ "TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),GROSS_AMT_INR,GROSS_AMT_INR,"
								+ "GROSS_AMT_INR,GROSS_AMT_INR,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),INVOICE_AMT,NET_PAYABLE_AMT,"
								+ "TCS_TDS,TCS_AMT,TCS_FACTOR,TDS_GROSS_AMT,TDS_GROSS_PERCENT,TDS_TAX_AMT,TDS_TAX_PERCENT,"
								+ "PAY_RECV_AMT,TO_CHAR(PAY_RECV_DT,'DD/MM/YYYY'),ADDR_FLAG,BU_UNIT,INVOICE_RAISED_IN,INVOICE_TYPE,SAP_APPROVAL,"
								+ "INVOICE_CATEGORY,ALLOC_QTY,CARGO_NO,SAP_APPROVED_BY,TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY') "
								+ "FROM FMS_FFLOW_INV_MST A "
								+ "WHERE COMPANY_CD=? "
								+ "AND INVOICE_DT>=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND APPROVED_FLAG=? AND INVOICE_ID_SEQ IS NOT NULL AND INVOICE_AMT IS NOT NULL "
								+ "AND PDF_INV_DTL IS NOT NULL "
								+ "AND  ((SELECT COUNT(*) FROM FMS_FFLOW_INV_FILE_DTL B "
								+ "WHERE B.COMPANY_CD=A.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
								+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR  AND A.INVOICE_TYPE=B.INVOICE_TYPE AND B.PDF_TYPE=? )=0 "
								+ "OR (SELECT COUNT(*) FROM FMS_FFLOW_INV_FILE_DTL B "
								+ "WHERE B.COMPANY_CD=A.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
								+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.INVOICE_TYPE=B.INVOICE_TYPE AND B.PDF_TYPE='S' )>0) ";
						if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("S") || VTEMP_SEGMENT_TYPE.elementAt(i).equals("O"))
						{
							queryString+="AND CONTRACT_TYPE IN (?,?) ";
						}
						else
						{
							queryString+="AND CONTRACT_TYPE=? ";
						}
						queryString+= "ORDER BY INVOICE_DT ";
						stmt1 = conn.prepareStatement(queryString);
						stmt1.setString(++ctn, comp_cd);
						stmt1.setString(++ctn, from_dt);
						stmt1.setString(++ctn, to_dt);
						stmt1.setString(++ctn, "Y");
						stmt1.setString(++ctn, "X");
						if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("S"))
						{
							stmt1.setString(++ctn, "S");
							stmt1.setString(++ctn, "L");
						}
						else if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("O"))
						{
							stmt1.setString(++ctn, "O");
							stmt1.setString(++ctn, "Q");
						}
						else
						{
							stmt1.setString(++ctn, ""+VTEMP_SEGMENT_TYPE.elementAt(i));
						}
					}
					
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						index++;
						String companyCd = rset1.getString(1)==null?"":rset1.getString(1);
						String countpty_cd = rset1.getString(2)==null?"":rset1.getString(2);
						String agmt = rset1.getString(3)==null?"":rset1.getString(3);
						String agmt_rev = rset1.getString(4)==null?"":rset1.getString(4);
						String cont = rset1.getString(5)==null?"":rset1.getString(5);
						String cont_rev = rset1.getString(6)==null?"":rset1.getString(6);
						String cont_type = rset1.getString(7)==null?"":rset1.getString(7);
						String finan_yr = rset1.getString(8)==null?"":rset1.getString(8);
						String bu_state_tin = rset1.getString(9)==null?"":rset1.getString(9);
						String inv_seq = rset1.getString(10)==null?"":rset1.getString(10);
						String inv_no = rset1.getString(11)==null?"":rset1.getString(11);
						String inv_dt = rset1.getString(12)==null?"":rset1.getString(12);
						String period_st_dt = rset1.getString(13)==null?"":rset1.getString(13);
						String period_end_dt = rset1.getString(14)==null?"":rset1.getString(14);
						String inv_due_dt = rset1.getString(15)==null?"":rset1.getString(15);
						double sales_price=0;//rset1.getDouble(16);
						String sales_price_cd ="";// rset1.getString(17)==null?"":rset1.getString(17);
						String cargo_no = rset1.getString(41)==null?"":rset1.getString(41);
						String sun_approved_by = rset1.getString(42)==null?"":rset1.getString(42);
						String sun_approved_dt = rset1.getString(43)==null?"":rset1.getString(43);
						
						//18
						double gross_amt = rset1.getDouble(19);
						double tax_amt = rset1.getDouble(20);
						String tax_struct_cd = rset1.getString(21)==null?"":rset1.getString(21);
						String tax_struct_dt = rset1.getString(22)==null?"":rset1.getString(22);
						
						double invoice_amt = rset1.getDouble(23);
						double net_payable = rset1.getDouble(24);
						String tds_tcs_flag = rset1.getString(25)==null?"":rset1.getString(25);
						double tcs_amt = rset1.getDouble(26);
						//27
						String temp_tds_gross_amt = rset1.getString(28)==null?"":rset1.getString(28);
						double tds_gross_amt = rset1.getDouble(28);
						String temp_tds_gross_per = rset1.getString(29)==null?"":rset1.getString(29);
						double tds_gross_per = rset1.getDouble(29);
						String temp_tds_tax_amt = rset1.getString(30)==null?"":rset1.getString(30);
						double tds_tax_amt = rset1.getDouble(30);
						String temp_tds_tax_per = rset1.getString(31)==null?"":rset1.getString(31);
						double tds_tax_per = rset1.getDouble(31);
						
						String temp_pay_recv_amt = rset1.getString(32)==null?"":rset1.getString(32);
						double pay_recv_amt = rset1.getDouble(32);
						String temp_pay_recv_dt = rset1.getString(33)==null?"":rset1.getString(33);
						
						String plant_seq = rset1.getString(34)==null?"":rset1.getString(34);
						
						if(!temp_tds_tax_amt.equals("")){
							VTDS_TAX_AMT.add(nf.format(tds_tax_amt));
						}else{
							VTDS_TAX_AMT.add("");
						}
						
						if(!temp_tds_tax_per.equals("")){
							VTDS_TAX_PERCENT.add(nf.format(tds_tax_per));
						}else{
							VTDS_TAX_PERCENT.add("");
						}
						
						if(!temp_pay_recv_amt.equals("")){
							VPAY_RECV_AMT.add(nf.format(pay_recv_amt));
						}else{
							VPAY_RECV_AMT.add("");
						}
						VPAY_RECV_DT.add(temp_pay_recv_dt);
						
						VCOUNTERPARTY_CD.add(countpty_cd);
						VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
						VAGMT_NO.add(agmt);
						VAGMT_REV_NO.add(agmt_rev);
						VCONT_NO.add(cont);
						VCONT_REV_NO.add(cont_rev);
						VCONTRACT_TYPE.add(cont_type);
						VDIS_CONT_MAPPING.add(""+utilBean.NewDealMappingId(companyCd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo_no));
						VFINANCIAL_YEAR.add(finan_yr);
						VBU_STATE_TIN.add(bu_state_tin);
						VINVOICE_SEQ.add(inv_seq);
						VINVOICE_NO.add(inv_no);
						VINVOICE_DT.add(inv_dt);
						VPERIOD_START_DT.add(period_st_dt);
						VPERIOD_END_DT.add(period_end_dt);
						VINVOICE_DUE_DT.add(inv_due_dt);
						if(Double.doubleToRawLongBits(sales_price)>Double.doubleToRawLongBits(0))
						{
							VSALES_PRICE.add(""+utilBean.RateNumberFormat(sales_price, sales_price_cd));
						}
						else
						{
							VSALES_PRICE.add("");
						}
						VSALES_PRICE_CD.add(sales_price_cd);
						VSALES_PRICE_NM.add(""+utilBean.getRateUnitNm(conn,sales_price_cd));
						VGROSS_AMT.add(nf.format(Math.abs(gross_amt)));
						if(Double.doubleToRawLongBits(tax_amt)>Double.doubleToRawLongBits(0))
						{
							VTAX_AMT.add(nf.format(Math.abs(tax_amt)));
						}
						else
						{
							VTAX_AMT.add("");
						}
						VINVOICE_AMT.add(nf.format(Math.abs(invoice_amt)));
						
						net_payable=net_payable-tds_gross_amt-tds_tax_amt;
						VNET_PAYABLE_AMT.add(nf.format(Math.abs(net_payable)));
						
						double short_received = net_payable - pay_recv_amt;
						VSHORT_RECEIVED.add(nf.format(Math.abs(short_received)));
						
						VTDS_TCS_FLAG.add(tds_tcs_flag);
						if(Double.doubleToRawLongBits(tcs_amt)>Double.doubleToRawLongBits(0))
						{
							VTCS_AMT.add(nf.format(Math.abs(tcs_amt)));
						}
						else
						{
							VTCS_AMT.add("");
						}
						
						/*queryString1="SELECT TAX_STRUCT_DTL "
						+ "FROM FMS_ENTITY_TAX_STRUCT_DTL A "
						+ "WHERE A.COMPANY_CD='"+comp_cd+"' AND A.TAX_STRUCT_CD='"+tax_struct_cd+"' "
						+ "AND A.ENTITY='C' AND A.COUNTERPARTY_CD='"+countpty_cd+"' AND A.PLANT_SEQ_NO='"+plant_seq+"' "
						+ "AND A.TAX_STRUCT_DT=(SELECT MAX(D.TAX_STRUCT_DT) FROM FMS_ENTITY_TAX_STRUCT_DTL D WHERE A.COMPANY_CD=D.COMPANY_CD "
						+ "AND A.ENTITY=D.ENTITY AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.PLANT_SEQ_NO=D.PLANT_SEQ_NO "
						+ "AND D.TAX_STRUCT_DT <= TO_DATE('"+tax_struct_dt+"','DD/MM/YYYY')) ";
					rset1=stmt1.executeQuery(queryString1);
					if(rset1.next())
					{
						VTAX_STRUCT_DTL.add(rset1.getString(1)==null?"":rset1.getString(1));
					}
					else
					{*/
						//VTAX_STRUCT_DTL.add("");
						//}
						
						VTAX_STRUCT_DTL.add(utilBean.getTaxDescr(conn, tax_struct_cd));
						
						String bu_plant_seq=rset1.getString(35)==null?"":rset1.getString(35);
						VBU_NM.add(""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B"));
						VSAP_APPROVAL_FLAG.add(rset1.getString(38)==null?"":rset1.getString(38));
						if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("E"))
						{
							VTYPE_FLAG.add("DLNG_FFLOW");
						}
						else
						{
							VTYPE_FLAG.add("FFLOW");
						}
						VALLOC_QTY.add(rset1.getString(40)==null?"":nf.format(rset1.getDouble(40)));
						VINVOICE_RAISED_IN.add(""+utilBean.getRateUnitNm(conn,rset1.getString(36)==null?"":rset1.getString(36)));
						VPAYMENT_DONE_IN.add("INR");
						VTCS_TDS.add(tds_tcs_flag);
						
						if(tds_tcs_flag.equals("TCS"))
						{
							VTDS_GROSS_AMT.add(rset1.getString(26)==null?"":nf.format(rset1.getDouble(26)));
							VTDS_GROSS_PERCENT.add(rset1.getString(27)==null?"":nf.format(rset1.getDouble(27)));
						}
						else
						{
							if(!temp_tds_gross_amt.equals("")){
								VTDS_GROSS_AMT.add(nf.format(Math.abs(tds_gross_amt)));
							}else{
								VTDS_GROSS_AMT.add("");
							}
							
							if(!temp_tds_gross_per.equals("")){
								VTDS_GROSS_PERCENT.add(nf.format(Math.abs(tds_gross_per)));
							}else{
								VTDS_GROSS_PERCENT.add("");
							}
						}
						
						VSALES_AMT.add("");
						String inv_type = rset1.getString(37)==null?"":rset1.getString(37);
						VINVOICE_TYPE.add(inv_type);
						
						String contRef="";
						if(cont_type.equals("O") || cont_type.equals("Q"))
						{
							String queryString1="SELECT A.CONT_REF_NO "
									+ "FROM FMS_LTCORA_CONT_MST A,"
									+ "FMS_LTCORA_CONT_CARGO_DTL B "
									+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? "
									+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.AGMT_TYPE=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND B.CARGO_NO=? "
									+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
									+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
									+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
									+ ""
									+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
									+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
							stmt2=conn.prepareStatement(queryString1);
							stmt2.setString(1, companyCd);
							stmt2.setString(2, countpty_cd);
							stmt2.setString(3, "C");
							stmt2.setString(4, agmt);
							stmt2.setString(5, agmt_rev);
							stmt2.setString(6, "A");
							stmt2.setString(7, cont);
							stmt2.setString(8, cont_type);
							stmt2.setString(9, cargo_no);
							rset2=stmt2.executeQuery();
							if(rset2.next())
							{
								contRef=rset2.getString(1)==null?"":rset2.getString(1);
								
							}
							rset2.close();
							stmt2.close();
						}
						else
						{
							String queryString1="SELECT CONT_REF_NO,TRADE_REF_NO,AGMT_BASE "
									+ "FROM FMS_SUPPLY_CONT_MST A "
									+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
									+ "AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
									+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
									+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
							stmt2 = conn.prepareStatement(queryString1);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, cont_type);
							stmt2.setString(3, countpty_cd);
							stmt2.setString(4, agmt);
							stmt2.setString(5, cont);
							rset2=stmt2.executeQuery();
							if(rset2.next())
							{
								contRef=rset2.getString(1)==null?"":rset2.getString(1);
								if(cont_type.equals("X"))
								{
									contRef=rset2.getString(2)==null?"":rset2.getString(2);
								}
							}
							rset2.close();
							stmt2.close();
						}
						
						VCONT_REF_NO.add(contRef);
						
						String inv_cetegory=rset1.getString(39)==null?"":rset1.getString(39);
						String cash_flow="";
						if(inv_type.equals("LP"))
						{
							cash_flow="Interest";
						}
						else if(inv_type.equals("CR") || inv_type.equals("CCR"))
						{
							//cash_flow="Brokerage/Commission";
							cash_flow="Commodity";
						}
						else if(inv_cetegory.equals("P"))
						{
							cash_flow="Commodity";
						}
						else if(inv_cetegory.equals("S"))
						{
							cash_flow="Service";
						}
						VCASH_FLOW.add(cash_flow);
						VAPPROVE_HIST.add(utilBean.getEmpName(conn, sun_approved_by)+"<br>"+sun_approved_dt);
					}
					rset1.close();
					stmt1.close();
					VINDEX.add(index);
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//for fetching the list for derivative invoice
	public void getDerivativeInvoiceList(String inv_type)
	{
		String function_nm="getDerivativeInvoiceList()";
		try
		{
			int index=0;
			String query="SELECT DISTINCT INVOICE_SEQ,BU_STATE_TIN,COUNTERPARTY_CD,SAP_APPROVED_BY,TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY') "
					+ "FROM FMS_DERV_INVOICE_MST "
					+ "WHERE COMPANY_CD=? "
					+ "AND INVOICE_DT>=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND INV_TYPE=? "
					+ "AND APPROVED_FLAG='Y' AND INVOICE_ID_SEQ IS NOT NULL AND INVOICE_AMT IS NOT NULL "
					+ "AND PDF_INV_DTL IS NOT NULL ";
					//+ "AND SAP_APPROVAL='Y' ";
			stmt1=conn.prepareStatement(query);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, from_dt);
			stmt1.setString(3, to_dt);
			stmt1.setString(4, inv_type);
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				index+=1;
				String inv_seq=rset1.getString(1)==null?"":rset1.getString(1);
				String buStTin=rset1.getString(2)==null?"":rset1.getString(2);
				String contPtyCd=rset1.getString(3)==null?"":rset1.getString(3);
				String sun_approved_by = rset1.getString(4)==null?"":rset1.getString(4);
				String sun_approved_dt = rset1.getString(5)==null?"":rset1.getString(5);
				
				int count=0;
				String companyCd="";
				String countpty_cd="";
				String agmtno="";
				String contno="";
				String cont_type="";
				String instrument_no="";
				String fin_year="";
				String bu_state_tin="";
				String invoice_seq="";
				String invoice_no="";
				String inv_ref_no="";
				String invoice_dt="";
				String period_start_dt="";
				String period_end_dt="";
				String invoice_due_dt="";
				double sales_price=0;
				double alloc_qty=0;
				double sales_amt=0;
				double gross_amt=0;
				double invoice_amt=0;
				double net_payable=0;
				String temp_pay_recv_amt="";
				double pay_recv_amt=0;
				String temp_pay_recv_dt="";
				String plant_seq="";
				String bu_plant_seq="";
				String sap_approval_flg="";
				String inv_raised_in="";
				String invoice_type="";
				String deal_map="";
				String contRef="";
				String agmt_rev="";
				String cont_rev="";
				
				String queryString = "SELECT COMPANY_CD,INVOICE_SEQ, INVOICE_REF_NO, TO_CHAR(INVOICE_DT,'DD/MM/YYYY'), "
					    + "TO_CHAR(DUE_DT,'DD/MM/YYYY'), TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'), TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'), "
					    + "FREQ, ALLOC_QTY, SALE_PRICE, SALE_PRICE_UNIT, INVOICE_AMT, INVOICE_AMT,NET_PAYABLE_AMT, "
					    + "INVOICE_RAISED_IN, TO_CHAR(INVOICE_DT, 'Month'), COUNTERPARTY_CD, CONT_NO, CONTRACT_TYPE, "
					    + "BU_UNIT, CHECKED_FLAG, AUTHORIZED_FLAG, APPROVED_FLAG,INV_TYPE, AGMT_NO, INVOICE_NO,INSTRUMENT_NO,FINANCIAL_YEAR,"
					    + "SAP_APPROVAL,INVOICE_RAISED_IN,PAY_RECV_AMT,PAY_RECV_DT,PLANT_SEQ,AGMT_REV,CONT_REV,CONTRACT_TYPE "
					    + "FROM FMS_DERV_INVOICE_MST A "
					    + "WHERE COMPANY_CD = ? "
					    + "AND INVOICE_DT >= TO_DATE(?,'DD/MM/YYYY') "
					    + "AND INVOICE_DT <= TO_DATE(?,'DD/MM/YYYY') "
					    + "AND CONTRACT_TYPE IN ('V') AND INV_TYPE=? AND INVOICE_SEQ=? AND COUNTERPARTY_CD=? AND BU_STATE_TIN=? "
					    + "AND APPROVED_FLAG=? AND INVOICE_ID_SEQ IS NOT NULL AND INVOICE_AMT IS NOT NULL "
					    + "AND PDF_INV_DTL IS NOT NULL "
						//+ "AND SAP_APPROVAL=? "
					    + "ORDER BY INVOICE_DT";
				stmt3 = conn.prepareStatement(queryString);
				stmt3.setString(++count, comp_cd);
				stmt3.setString(++count, from_dt);
				stmt3.setString(++count, to_dt);
				stmt3.setString(++count, inv_type);
				stmt3.setString(++count, inv_seq);
				stmt3.setString(++count, contPtyCd);
				stmt3.setString(++count, buStTin);
				stmt3.setString(++count, "Y");
				//stmt3.setString(++count, "Y");
				rset3=stmt3.executeQuery();
				while(rset3.next())
				{
					companyCd = rset3.getString(1)==null?"":rset3.getString(1);
					countpty_cd = rset3.getString(17)==null?"":rset3.getString(17);
					agmtno = rset3.getString(25)==null?"":rset3.getString(25);
					contno = rset3.getString(18)==null?"":rset3.getString(18);
					cont_type = rset3.getString(19)==null?"":rset3.getString(19);
					instrument_no = rset3.getString(27)==null?"":rset3.getString(27);
					fin_year = rset3.getString(28)==null?"":rset3.getString(28);
					
					bu_state_tin = rset1.getString(2)==null?"":rset1.getString(2);
					invoice_seq = rset3.getString(2)==null?"":rset3.getString(2);
					invoice_no = rset3.getString(26)==null?"":rset3.getString(26);
					inv_ref_no = rset3.getString(3)==null?"":rset3.getString(3);
					invoice_dt = rset3.getString(4)==null?"":rset3.getString(4);
					period_start_dt = rset3.getString(6)==null?"":rset3.getString(6);
					period_end_dt = rset3.getString(7)==null?"":rset3.getString(7);
					invoice_due_dt = rset3.getString(5)==null?"":rset3.getString(5);
					sales_price=rset3.getDouble(10);
					alloc_qty+=rset3.getDouble(9);
					
					sales_amt += rset3.getDouble(12);
					gross_amt += rset3.getDouble(13);
					
					invoice_amt += rset3.getDouble(13);
					net_payable += rset3.getDouble(14);
					
					temp_pay_recv_amt = rset3.getString(31)==null?"":rset3.getString(31);
					pay_recv_amt = rset3.getDouble(31);
					temp_pay_recv_dt = rset3.getString(32)==null?"":rset3.getString(32);
					
					plant_seq = rset3.getString(33)==null?"":rset3.getString(33);
					bu_plant_seq=rset3.getString(20)==null?"":rset3.getString(20);
					String dealMap=utilBean.NewDealMappingId(companyCd, countpty_cd, agmtno, agmt_rev, contno,cont_rev, cont_type, instrument_no);
					sap_approval_flg=rset3.getString(29)==null?"":rset3.getString(29);
					inv_raised_in=rset3.getString(30)==null?"":rset3.getString(30);
					invoice_type=rset3.getString(24)==null?"":rset3.getString(24);
					agmt_rev=rset3.getString(34)==null?"":rset3.getString(34);
					cont_rev=rset3.getString(35)==null?"":rset3.getString(35);
					
					if(deal_map.equals(""))
					{
						deal_map=dealMap;
					}
					else
					{
						deal_map=deal_map+", "+dealMap;
					}
					
					String contRefNo="";
					String queryString2="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_TYPE,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,A.CONT_NO,A.CONT_REV,A.CONT_NAME,A.CONT_REF_NO,"
							+ "B.INSTRUMENT_NO,B.INSTRUMENT_TYPE,B.BUY_SELL,B.STATUS,B.QTY,B.QTY_UNIT,B.RATE,B.RATE_UNIT,"
							+ "B.PRODUCT_NM,B.CURVE_NM,B.PROJ_METHOD,TO_CHAR(B.CONT_DD_MM_YR,'DD/MM/YYYY'),"
							+ "TO_CHAR(B.PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_END_DT,'DD/MM/YYYY'),B.CONV_FACTOR,TO_CHAR(A.TRADE_DT,'DD/MM/YYYY') "
							+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
							+ "AND B.STATUS='Y' AND B.INSTRUMENT_NO=? "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO";
				  	stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, companyCd);
					stmt2.setString(2, countpty_cd);
					stmt2.setString(3, agmtno);
					stmt2.setString(4, contno);
					stmt2.setString(5, cont_type);
					stmt2.setString(6, instrument_no);
					rset2=stmt2.executeQuery();
					while(rset2.next())
					{
						contRefNo=rset2.getString(10)==null?"":rset2.getString(10);
					}
					rset2.close();
					stmt2.close();
					
					if(contRef.equals(""))
					{
						contRef=contRefNo;
					}
					else
					{
						contRef=contRef+", "+contRefNo;
					}
				}
				rset3.close();
				stmt3.close();
				
				if(sales_amt<0)
				{
					sales_amt=sales_amt*(-1);
				}
				if(gross_amt<0)
				{
					gross_amt=gross_amt*(-1);
				}
				if(invoice_amt<0)
				{
					invoice_amt=invoice_amt*(-1);
				}
				if(net_payable<0)
				{
					net_payable=net_payable*(-1);
				}
				
				if(!temp_pay_recv_amt.equals(""))
				{
					VPAY_RECV_AMT.add(nf.format(pay_recv_amt));
				}
				else
				{
					VPAY_RECV_AMT.add("");
				}
				VPAY_RECV_DT.add(temp_pay_recv_dt);
				VBU_NM.add(""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B"));
				//VINV_REF.add(inv_ref_no);
				VDIS_REMITTANCE_NO.add(inv_ref_no);
				VCOUNTERPARTY_CD.add(countpty_cd);
				VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VAGMT_NO.add(agmtno);
				VAGMT_REV_NO.add(agmt_rev);
				VCONT_NO.add(contno);
				VCONT_REV_NO.add(cont_rev);
				VCONTRACT_TYPE.add(cont_type);
				VDIS_CONT_MAPPING.add(deal_map);
				VFINANCIAL_YEAR.add(fin_year);
				VBU_STATE_TIN.add(bu_state_tin);
				VINVOICE_SEQ.add(inv_seq);
				VINVOICE_NO.add(invoice_no);
				VINVOICE_DT.add(invoice_dt);
				VPERIOD_START_DT.add(period_start_dt);
				VPERIOD_END_DT.add(period_end_dt);
				VINVOICE_DUE_DT.add(invoice_due_dt);
				//VSELL_RATE.add(""+utilBean.RateNumberFormat(sales_price, "2"));
				//VSELL_PRICE_CD.add("2");
				//VSELL_PRICE_NM.add(""+utilBean.getRateUnitNm(conn,"2"));
				if(inv_type.equals("I"))
				{
					VGROSS_AMT.add(nf.format(gross_amt));
					VINVOICE_AMT.add(nf.format(invoice_amt));
				}
				else
				{
					VGROSS_AMT.add("");
					VINVOICE_AMT.add("");
				}
				VNET_PAYABLE_AMT.add(nf.format(net_payable));
				double short_received = net_payable - pay_recv_amt;
				VSHORT_RECEIVED.add(nf.format(short_received));
				
				//VBU_PLANT_ABBR.add(""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B"));
				VSAP_APPROVAL_FLAG.add(sap_approval_flg);
				
				VALLOC_QTY.add(nf.format(alloc_qty));
				VINVOICE_RAISED_IN.add(""+utilBean.getRateUnitNm(conn,inv_raised_in));
				VPAYMENT_DONE_IN.add("USD");
				VSALES_AMT.add(nf.format(sales_amt));
				VINVOICE_TYPE.add(invoice_type);
				VCONT_REF_NO.add(contRef);
				VCASH_FLOW.add("Commodity");
				VAPPROVE_HIST.add(utilBean.getEmpName(conn, sun_approved_by)+"<br>"+sun_approved_dt);
				
				VPAYMENT_DONE_IN.add("USD");
				VTYPE_FLAG.add("DERV");		//for derivatives
				VPAYMENT_TYPE_FLAG.add("DERV");	//for purchase side 
				VSALE_AMT.add(nf.format(sales_amt));
				VSALES_PRICE.add("");
				VBU_SEQ.add("");		//for purchase side
				VSALES_PRICE_NM.add("");
				VTAX_AMT.add("");
				VTCS_TDS.add("");
				VTDS_GROSS_PERCENT.add("");
				VTDS_GROSS_AMT.add("");
				VEXCHNAGE_RATE.add("");
				VEXCHNAGE_RATE_DATE.add("");
				VSALES_PRICE_UNIT.add("");
				VQTY_UNIT.add("");
				VADJ_SIGN.add("");
				VINV_FLAG.add("");
				VTCS_TDS.add("");
				VTCS_TDS_AMT_USD.add("");
				VTCS_TDS_AMT.add("");
				VTCS_TDS_FACTOR.add("");
				VTCS_TDS_STRUCT_CD.add("");
				VTCS_TDS_EFF_DT.add("");
				VTCS_TDS_DONE.add("");
				VGROSS_AMT_USD.add(gross_amt);
				VTAX_AMT_USD.add("");
				VINVOICE_AMT_USD.add(invoice_amt);
				VADJ_AMT_USD.add("");
				VADJ_AMT.add("");
				VNET_PAYABLE_USD.add(invoice_amt);
				VNET_PAYABLE.add("");
				VSPLIT_VALUE.add("");
				VSAP_EXCHANG_FLAG.add("");
				VEXCHNG_RATE_CD.add("");
				VEXCHNG_RATE_CONFIG.add("");
				VACT_ARRIVAL_DT.add("");
				VTRANS_DT.add("");
			}
			rset1.close();
			stmt1.close();
			VINDEX.add(index);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//for generating the sales sun xml
	public void generateSalesSunXML()
	{
		String function_nm="generateSalesSunXML()";
		try
		{
			DocumentBuilderFactory docFactory = xmlUtil.dcoumentBuilderFactory();
		    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		    Document doc = docBuilder.newDocument();
			//root fmsng
		    Element fmsng = doc.createElement("SSC");
		    doc.appendChild(fmsng);
		    
		    //root elements
		    Element Payload = doc.createElement("Payload");
		    fmsng.appendChild(Payload);
		    Element Ledger = doc.createElement("Ledger");
		    //fmsng.appendChild(Ledger);
		    Payload.appendChild(Ledger);
		    
		    String counterpty_cd="";
		    String fin_year="";
		    String agmt_no="";
		    String agmt_rev="";
		    String cont_no="";
		    String cont_rev="";
		    String cont_type="";
		    String bu_unit="";
		    String bu_state_tin="";
		    String plant_seq_no="";
		    String invoice_seq="";
		    String cargo_no="";
		    String invoice_no="";
		    String invoice_dt="";
		    String gross_amt="";
		    String tax_amt="";
		    String exchg_rate="";
		    String inv_due_dt="";
		    String accountPeriod="";
		    String inv_raised_in="";
		    String gross_amt_usd="0.00";
		    String journal_type="FMSSL";
		    String alloc_qty="";
		    String sales_price="";
		    String sales_price_unit="";
		    String periodStartDt="";
		    String periodEndDt="";
		    String bucac="SEI";	
		    String taxStructCd="";
		    String tcsStructCd="";
		    String tdsStructCd="";
		    String tcs_amt="";
		    String tds_amt="";
		    String tcs_tds="";
		    String transportationAmt="";
		    String trans_account_cd="802012";
		    String igx_account_cd="153995";	//Account code for the IGX account 
		    String invoice_type="";
		    String sub_inv_type="";
		    String gen_type="";
		    String market_margin_amt="";
		    String oth_amt="";
		    String approve_dt="";
		   // String sysdate=utilDate.getSysdate();
		    String xmlFileNm="";
		    String sysdate=utilDate.getSysdateWithTime24hrWithSS();		//PB 20251003: FOR HANDLING THE PARALLEL SUBMIT ISSUE
		    sysdate=sysdate.replace("/", "_");
		    sysdate=sysdate.replace(" ", "_");
		    sysdate=sysdate.replace(":", "_");
		    String inv_flag="";
		    String sug_qty="";
		    String latePaymentAcc_cd="792003";
		    String criteria="";
		    
		    boolean xml_generate_flag=true;
		    
			String queryString="SELECT A.COUNTERPARTY_CD,A.FINANCIAL_YEAR,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE, "
					+ "A.BU_UNIT,A.BU_STATE_TIN,A.PLANT_SEQ,A.INVOICE_SEQ,A.CARGO_NO,A.INVOICE_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'), "
					+ "A.GROSS_AMT,A.TAX_AMT,A.EXCHG_RATE_VALUE,TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),A.INVOICE_RAISED_IN,A.ALLOC_QTY,A.SALE_PRICE,"
					+ "A.SALE_PRICE_UNIT,TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),A.TAX_STRUCT_CD, "
					+ "A.TCS_TDS,A.TCS_STRUCT_CD,A.TCS_AMT,A.TDS_STRUCT_CD,A.TDS_GROSS_AMT,A.TRANSPORTATION_AMOUNT,NULL,NULL,NULL,'SG', "	//A.GROSS_AMT_USD,A.INVOICE_TYPE,A.SUB_INV_TYPE,gen_type 
					+ "A.MARKET_MARGIN_AMT,A.OTHER_CHARGES_AMT,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),INV_FLAG,SUG_QTY,NET_PAYABLE_AMT,REF_NO,"
					+ "A.CRITERIA " 
					+ "FROM FMS_INVOICE_MST A "
					+ "WHERE A.COMPANY_CD=? AND A.SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "	
					+ "AND NOT EXISTS(SELECT B.PDF_TYPE FROM FMS_INV_FILE_DTL B WHERE  B.COMPANY_CD=A.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
	    			+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND B.PDF_TYPE IN (?,?))";
			queryString+="UNION ALL ";
			queryString+="SELECT A.COUNTERPARTY_CD,A.FINANCIAL_YEAR,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE, "
					+ "A.BU_UNIT,A.BU_STATE_TIN,NULL,A.INVOICE_SEQ,A.CARGO_NO,A.INVOICE_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'), "
					+ "A.GROSS_AMT_INR,A.TAX_AMT,A.EXCHG_RATE_VALUE,TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),A.INVOICE_RAISED_IN,A.ALLOC_QTY,NULL, "
					+ "NULL,TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),A.TAX_STRUCT_CD, "
					+ "A.TCS_TDS,A.TCS_STRUCT_CD,A.TCS_AMT,A.TDS_STRUCT_CD,A.TDS_GROSS_AMT,A.TRANSPORTATION_AMOUNT,A.GROSS_AMT_USD,A.INVOICE_TYPE,A.SUB_INV_TYPE,'FFLOW', "
					+ "NULL,NULL,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),NULL,NULL,NET_PAYABLE_AMT,NULL,"
					+ "NULL "
					+ "FROM FMS_FFLOW_INV_MST A "
					+ "WHERE A.COMPANY_CD=? AND A.SAP_APPROVAL=?  "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND NOT EXISTS(SELECT B.PDF_TYPE FROM FMS_FFLOW_INV_FILE_DTL B WHERE  B.COMPANY_CD=A.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
			    	+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND B.PDF_TYPE IN (?,?) AND B.INVOICE_TYPE=A.INVOICE_TYPE)";
			queryString+="UNION ALL ";
			queryString+="SELECT A.COUNTERPARTY_CD,A.FINANCIAL_YEAR,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,  "
					+ "A.BU_UNIT,A.BU_STATE_TIN,A.PLANT_SEQ,A.INVOICE_SEQ,NULL,A.INVOICE_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),  "
					+ "A.GROSS_AMT,A.TAX_AMT,A.EXCHG_RATE_VALUE,TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),A.INVOICE_RAISED_IN,A.ALLOC_QTY,A.SALE_PRICE, "
					+ "A.SALE_PRICE_UNIT,TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),A.TAX_STRUCT_CD,  "
					+ "A.TCS_TDS,A.TCS_STRUCT_CD,A.TCS_AMT,A.TDS_STRUCT_CD,A.TDS_GROSS_AMT,NULL,NULL,NULL,NULL,'DLNG_SG', 	 "
					+ "NULL,NULL,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),INV_FLAG,NULL,NET_PAYABLE_AMT,REF_NO,"
					+ "A.CRITERIA "
					+ "FROM FMS_DLNG_INVOICE_MST A  "
					+ "WHERE A.COMPANY_CD=? AND A.SAP_APPROVAL=?  "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY')  "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY')  "
					+ "AND NOT EXISTS(SELECT B.PDF_TYPE FROM FMS_DLNG_INV_FILE_DTL B WHERE  B.COMPANY_CD=A.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ  "
					+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND B.PDF_TYPE IN (?,?) ) ";
			queryString+="UNION ALL ";
			queryString+="SELECT A.COUNTERPARTY_CD,A.FINANCIAL_YEAR,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE, "
					+ "A.BU_UNIT,A.BU_STATE_TIN,A.PLANT_SEQ,A.INVOICE_SEQ,A.INSTRUMENT_NO,A.INVOICE_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'), "
					+ "A.INVOICE_AMT,NULL,NULL,TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),A.INVOICE_RAISED_IN,A.ALLOC_QTY,A.SALE_PRICE, "
					+ "A.SALE_PRICE_UNIT,TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),NULL, "
					+ "NULL,NULL,NULL,NULL,NULL,NULL,NULL,A.INV_TYPE,NULL,'DERV', "
					+ "NULL,NULL,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),NULL,NULL,NET_PAYABLE_AMT,NULL,"
					+ "NULL "
					+ "FROM FMS_DERV_INVOICE_MST A  "
					+ "WHERE A.COMPANY_CD=? AND A.SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND NOT EXISTS(SELECT B.PDF_TYPE FROM FMS_DERV_INV_FILE_DTL B WHERE  B.COMPANY_CD=A.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
					+ "AND A.INV_TYPE=B.INV_TYPE AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND B.PDF_TYPE IN (?,?) ) "
					+ "AND A.INV_TYPE=? ";
			queryString+="UNION ALL ";		//PB 20250913 FOR DLNG  FFLOW INVOICE
			queryString+="SELECT A.COUNTERPARTY_CD,A.FINANCIAL_YEAR,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE, "
					+ "A.BU_UNIT,A.BU_STATE_TIN,NULL,A.INVOICE_SEQ,A.CARGO_NO,A.INVOICE_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'), "
					+ "A.GROSS_AMT_INR,A.TAX_AMT,A.EXCHG_RATE_VALUE,TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),A.INVOICE_RAISED_IN,A.ALLOC_QTY,NULL, "
					+ "NULL,TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),A.TAX_STRUCT_CD, "
					+ "A.TCS_TDS,A.TCS_STRUCT_CD,A.TCS_AMT,A.TDS_STRUCT_CD,A.TDS_GROSS_AMT,NULL,A.GROSS_AMT_USD,A.INVOICE_TYPE,A.SUB_INV_TYPE,'DLNG_FFLOW', "
					+ "NULL,NULL,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),NULL,NULL,NET_PAYABLE_AMT,NULL,"
					+ "NULL "
					+ "FROM FMS_DLNG_FFLOW_INV_MST A "
					+ "WHERE A.COMPANY_CD=? AND A.SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND NOT EXISTS(SELECT B.PDF_TYPE FROM FMS_DLNG_FFLOW_INV_FILE_DTL B WHERE  B.COMPANY_CD=A.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
					+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND B.PDF_TYPE IN (?,?) AND B.INVOICE_TYPE=A.INVOICE_TYPE)";
			queryString+="UNION ALL ";		//PB 20251028 FOR DLNG SERVICE INVOICE
			queryString+="SELECT A.COUNTERPARTY_CD,A.FINANCIAL_YEAR,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE, "
					+ "A.BU_UNIT,A.BU_STATE_TIN,A.PLANT_SEQ,A.INVOICE_SEQ,A.CARGO_NO,A.INVOICE_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'), "
					+ "A.GROSS_AMT,A.TAX_AMT,A.EXCHG_RATE_VALUE,TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),A.INVOICE_RAISED_IN,A.QTY,A.SALE_PRICE, "
					+ "A.SALE_PRICE_UNIT,TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),A.TAX_STRUCT_CD, "
					+ "A.TCS_TDS,A.TCS_STRUCT_CD,A.TCS_AMT,A.TDS_STRUCT_CD,A.TDS_GROSS_AMT,NULL,NULL,NULL,NULL,'DLNG_SERV', "
					+ "NULL,NULL,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),INV_FLAG,NULL,NET_PAYABLE_AMT,NULL,"
					+ "NULL "
					+ "FROM FMS_DLNG_SVC_INVOICE_MST A "
					+ "WHERE A.COMPANY_CD=? AND A.SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND NOT EXISTS(SELECT B.PDF_TYPE FROM FMS_DLNG_SVC_INV_FILE_DTL B WHERE  B.COMPANY_CD=A.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
					+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND B.PDF_TYPE IN (?,?) ) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "Y");
			stmt.setString(3, from_dt);
			stmt.setString(4, to_dt);
			stmt.setString(5, "S");
			stmt.setString(6, "X");
			stmt.setString(7, comp_cd);
			stmt.setString(8, "Y");
			stmt.setString(9, from_dt);
			stmt.setString(10, to_dt);
			stmt.setString(11, "S");
			stmt.setString(12, "X");
			stmt.setString(13, comp_cd);
			stmt.setString(14, "Y");
			stmt.setString(15, from_dt);
			stmt.setString(16, to_dt);
			stmt.setString(17, "S");
			stmt.setString(18, "X");
			stmt.setString(19, comp_cd);
			stmt.setString(20, "Y");
			stmt.setString(21, from_dt);
			stmt.setString(22, to_dt);
			stmt.setString(23, "S");
			stmt.setString(24, "X");
			stmt.setString(25, "I");
			stmt.setString(26, comp_cd);
			stmt.setString(27, "Y");
			stmt.setString(28, from_dt);
			stmt.setString(29, to_dt);
			stmt.setString(30, "S");
			stmt.setString(31, "X");
			stmt.setString(32, comp_cd);
			stmt.setString(33, "Y");
			stmt.setString(34, from_dt);
			stmt.setString(35, to_dt);
			stmt.setString(36, "S");
			stmt.setString(37, "X");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				counterpty_cd=rset.getString(1)==null?"":rset.getString(1);
				fin_year=rset.getString(2)==null?"":rset.getString(2);
				agmt_no=rset.getString(3)==null?"":rset.getString(3);
				agmt_rev=rset.getString(4)==null?"":rset.getString(4);
				cont_no=rset.getString(5)==null?"":rset.getString(5);
				cont_rev=rset.getString(6)==null?"":rset.getString(6);
				cont_type=rset.getString(7)==null?"":rset.getString(7);
				bu_unit=rset.getString(8)==null?"":rset.getString(8);
				bu_state_tin=rset.getString(9)==null?"":rset.getString(9);
				plant_seq_no=rset.getString(10)==null?"":rset.getString(10);
				invoice_seq=rset.getString(11)==null?"":rset.getString(11);
				cargo_no=rset.getString(12)==null?"":rset.getString(12);
				invoice_no=rset.getString(13)==null?"":rset.getString(13);
				invoice_dt=rset.getString(14)==null?"":rset.getString(14);
				gross_amt=rset.getString(15)==null?"":nf.format(rset.getDouble(15));
				tax_amt=rset.getString(16)==null?"":nf.format(rset.getDouble(16));
				exchg_rate=rset.getString(17)==null?"":nf.format(rset.getDouble(17));
				inv_due_dt=rset.getString(18)==null?"-":rset.getString(18);
				inv_raised_in=rset.getString(19)==null?"":rset.getString(19);
				alloc_qty=rset.getString(20)==null?"":nf.format(rset.getDouble(20));
				sales_price=rset.getString(21)==null?"":nf.format(rset.getDouble(21));
				sales_price_unit=rset.getString(22)==null?"":rset.getString(22);
				periodStartDt=rset.getString(23)==null?"":rset.getString(23);
				periodEndDt=rset.getString(24)==null?"":rset.getString(24);
				taxStructCd=rset.getString(25)==null?"":rset.getString(25);
				tcs_tds=rset.getString(26)==null?"":rset.getString(26);
				tcsStructCd=rset.getString(27)==null?"":rset.getString(27);
				tcs_amt=rset.getString(28)==null?"":nf.format(rset.getDouble(28));
				tdsStructCd=rset.getString(29)==null?"":rset.getString(29);
				tds_amt=rset.getString(30)==null?"":nf.format(rset.getDouble(30));
				transportationAmt=rset.getString(31)==null?"":nf.format(rset.getDouble(31));
				gross_amt_usd=rset.getString(32)==null?nf.format(0):nf.format(rset.getDouble(32));
				invoice_type=rset.getString(33)==null?"":rset.getString(33);
				sub_inv_type=rset.getString(34)==null?"":rset.getString(34);
				gen_type=rset.getString(35)==null?"":rset.getString(35);
				market_margin_amt=rset.getString(36)==null?"":nf.format(rset.getDouble(36));
				oth_amt=rset.getString(37)==null?"":nf.format(rset.getDouble(37));
				approve_dt=rset.getString(38)==null?"":rset.getString(38);
				inv_flag=rset.getString(39)==null?"":rset.getString(39);
				sug_qty=rset.getString(40)==null?"":nf.format(rset.getDouble(40));
				double net_payable=rset.getDouble(41);
				String ref_no=rset.getString(42)==null?"":rset.getString(42);
				double tds_gross_amt=rset.getDouble(30);
				criteria=rset.getString(43)==null?"":rset.getString(43);
				
				String sup_type=utilBean.getTaxDescr(conn, taxStructCd);
				accountPeriod=getAccountingPeriod(invoice_dt);
				
				String []invDt = invoice_dt.split("/");
				String trans_dt = invDt[0]+invDt[1]+invDt[2];
				
				//String [] invDueDt=inv_due_dt.split("/");
				//String dueDt=invDueDt[0]+invDueDt[1]+invDueDt[2];
				String dueDt=inv_due_dt.replace("/", "");
				
				if(gen_type.equals("DERV"))				//for derivative
				{
					//for finding the diff in price
					String diff_amt="";
					if(!gross_amt.equals("")&&!alloc_qty.equals(""))
					{
						diff_amt=nf.format(Double.parseDouble(gross_amt)/Double.parseDouble(alloc_qty));
						if(Double.parseDouble(diff_amt)<0)
						{
							diff_amt="("+diff_amt+")";
						}
					}
					String qty_unit=getQuantityUnit(counterpty_cd,agmt_no,"U",agmt_rev,cont_no,cont_type,cont_rev,cargo_no);		//here cargo_no represents instrument
					String description=(utilBean.getCounterpartyABBR(conn, counterpty_cd)+" "+alloc_qty+" "+qty_unit+" @ "+diff_amt+" MTM").toUpperCase();
					String account_cd=getCounterpartySunAccountCode(counterpty_cd, "T", "0", "V");
					String account_cd_lng_sale="711008"; 	//for derivative 711008 code is used
					String dr_cr_marker=Double.parseDouble(gross_amt)<0?"C":"D";
					
					for(int i=0;i<2;i++)
					{
						Element Line  = doc.createElement("Line");
						Element AccountCode = doc.createElement("AccountCode");
						Element AccountingPeriod = doc.createElement("AccountingPeriod");
						Element BaseAmount = doc.createElement("BaseAmount");
						Element DebitCredit = doc.createElement("DebitCredit");
						Element TransactionAmount = doc.createElement("TransactionAmount");
						Element Base2ReportingAmount = doc.createElement("Base2ReportingAmount");
						Element CurrencyCode = doc.createElement("CurrencyCode");
						Element CurrencyRate = doc.createElement("CurrencyRate");
						Element TransactionDate = doc.createElement("TransactionDate");
						Element JournalType = doc.createElement("JournalType");
						Element JournalSource = doc.createElement("JournalSource");			//Null in FMS8
						Element TransactionReference = doc.createElement("TransactionReference");
						Element Description = doc.createElement("Description");
						Element DueDate = doc.createElement("DueDate");
						Element AnalysisCode1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
						Element AnalysisCode2 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
						Element AnalysisCode3 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
						Element AnalysisCode4 = doc.createElement("AnalysisCode4");			//COA_CD: 
						Element AnalysisCode5 = doc.createElement("AnalysisCode5");			//Null: in FMS8
						Element AnalysisCode6 = doc.createElement("AnalysisCode6");			
						Element AnalysisCode7 = doc.createElement("AnalysisCode7");			
						Element AnalysisCode8 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
						Element AnalysisCode10 = doc.createElement("AnalysisCode10");			//BU SUN account code 
						Element GeneralDescription9 = doc.createElement("GeneralDescription9");			//Service for LTCORA else NULL 
						Element GeneralDescription10 = doc.createElement("GeneralDescription10");			//REV_CHARGE: in FMS8
						Element GeneralDescription11 = doc.createElement("GeneralDescription11");			//HSN_SAC: in FMS8
						Element GeneralDescription12 = doc.createElement("GeneralDescription12");			//POS: in FMS8
						Element GeneralDescription13 = doc.createElement("GeneralDescription13");			//TAX_AMT: in FMS8 configured for LTCORA
						Element GeneralDescription14 = doc.createElement("GeneralDescription14");			
						Element GeneralDescription15 = doc.createElement("GeneralDescription15");			//TOTAL INV AMT 
						
						Ledger.appendChild(Line);
						Line.appendChild(AccountCode);
						Line.appendChild(AccountingPeriod);
						Line.appendChild(BaseAmount);
						Line.appendChild(DebitCredit);
						Line.appendChild(TransactionAmount);
						Line.appendChild(Base2ReportingAmount);
						Line.appendChild(CurrencyCode);
						Line.appendChild(CurrencyRate);
						Line.appendChild(TransactionDate);
						Line.appendChild(JournalType);
						Line.appendChild(JournalSource);		//Null in FMS8
						Line.appendChild(TransactionReference);
						Line.appendChild(Description);
						Line.appendChild(DueDate);
						Line.appendChild(AnalysisCode1);
						Line.appendChild(AnalysisCode2);		
						Line.appendChild(AnalysisCode3);		
						Line.appendChild(AnalysisCode4);		
						Line.appendChild(AnalysisCode5);		
						Line.appendChild(AnalysisCode6);		
						Line.appendChild(AnalysisCode7);		
						Line.appendChild(AnalysisCode8);		
						Line.appendChild(AnalysisCode10);		
						Line.appendChild(GeneralDescription9);		
						Line.appendChild(GeneralDescription10);		
						Line.appendChild(GeneralDescription11);		
						Line.appendChild(GeneralDescription12);		
						Line.appendChild(GeneralDescription13);		
						Line.appendChild(GeneralDescription14);		
						Line.appendChild(GeneralDescription15);	
						
						if(i==0)
						{
							AccountCode.appendChild(doc.createTextNode(account_cd));
							DebitCredit.appendChild(doc.createTextNode(dr_cr_marker));
							AnalysisCode4.appendChild(doc.createTextNode(account_cd_lng_sale));
						}
						else
						{
							dr_cr_marker=Double.parseDouble(gross_amt)<0?"D":"C";
							AccountCode.appendChild(doc.createTextNode(account_cd_lng_sale));
							DebitCredit.appendChild(doc.createTextNode(dr_cr_marker));
						}
						AccountingPeriod.appendChild(doc.createTextNode(accountPeriod));
						//BaseAmount.appendChild(doc.createTextNode(invoice_amt));
						TransactionAmount.appendChild(doc.createTextNode(gross_amt));
						Base2ReportingAmount.appendChild(doc.createTextNode("0"));		
						CurrencyCode.appendChild(doc.createTextNode("USD"));
						//CurrencyRate.appendChild(doc.createTextNode("0"));					
						TransactionDate.appendChild(doc.createTextNode(trans_dt));
						JournalType.appendChild(doc.createTextNode(journal_type));
						TransactionReference.appendChild(doc.createTextNode(invoice_no));
						Description.appendChild(doc.createTextNode(description));
						DueDate.appendChild(doc.createTextNode(dueDt));
						AnalysisCode2.appendChild(doc.createTextNode("2MK01"));
						AnalysisCode7.appendChild(doc.createTextNode("NA"));
						AnalysisCode10.appendChild(doc.createTextNode(bucac));
					}
				}
				else
				{
					String counterparty_abbr = utilBean.getCounterpartyABBR(conn, counterpty_cd);
					String account_code = getCounterpartySunAccountCode(counterpty_cd,"C","0","DFT");
					
					//for fetching the BU state 
					String buStateNm="";
					String buAbbr="";
					String query1 = "SELECT PLANT_STATE,PLANT_ABBR "
							+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
							+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=? AND SEQ_NO=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
					stmt1 = conn.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, "B");
					stmt1.setString(3, comp_cd);
					stmt1.setString(4, bu_unit);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						buStateNm=rset1.getString(1)==null?"":rset1.getString(1);
						buAbbr=rset1.getString(2)==null?"":rset1.getString(2);
					}
					rset1.close();
					stmt1.close();
					
					//PB20251128: FOR LATE PAYMENT AND INTEREST INVOICE 
					if(inv_flag.equals("LP"))
					{
						String description = (counterparty_abbr+" INT CH DELAYED PAYT "+" "+ref_no+" "+trans_dt).toUpperCase();
						String cccac= "1TM01"; //for IGX: cccac="2MK01"
						if(cont_type.equals("X") || cont_type.equals("W"))
						{
							cccac="2MK01";
						}
						if(!bu_state_tin.equals("24"))
						{
							cccac="2"+getStateCd(bu_state_tin)+"01";
						}
						
						//Debit line
						Element Line  = doc.createElement("Line");
						Element AccountCode = doc.createElement("AccountCode");
						Element AccountingPeriod = doc.createElement("AccountingPeriod");
						Element BaseAmount = doc.createElement("BaseAmount");
						Element DebitCredit = doc.createElement("DebitCredit");
						Element TransactionAmount = doc.createElement("TransactionAmount");
						Element Base2ReportingAmount = doc.createElement("Base2ReportingAmount");
						Element CurrencyCode = doc.createElement("CurrencyCode");
						Element CurrencyRate = doc.createElement("CurrencyRate");
						Element TransactionDate = doc.createElement("TransactionDate");
						Element JournalType = doc.createElement("JournalType");
						Element JournalSource = doc.createElement("JournalSource");			//Null in FMS8
						Element TransactionReference = doc.createElement("TransactionReference");
						Element Description = doc.createElement("Description");
						Element DueDate = doc.createElement("DueDate");
						Element AnalysisCode1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
						Element AnalysisCode2 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
						Element AnalysisCode3 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
						Element AnalysisCode4 = doc.createElement("AnalysisCode4");			//COA_CD: 
						Element AnalysisCode5 = doc.createElement("AnalysisCode5");			//Null: in FMS8
						Element AnalysisCode6 = doc.createElement("AnalysisCode6");			
						Element AnalysisCode7 = doc.createElement("AnalysisCode7");			
						Element AnalysisCode8 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
						Element AnalysisCode10 = doc.createElement("AnalysisCode10");			//BU SUN account code 
						Element GeneralDescription9 = doc.createElement("GeneralDescription9");			//Service for LTCORA else NULL 
						Element GeneralDescription10 = doc.createElement("GeneralDescription10");			//REV_CHARGE: in FMS8
						Element GeneralDescription11 = doc.createElement("GeneralDescription11");			//HSN_SAC: in FMS8
						Element GeneralDescription12 = doc.createElement("GeneralDescription12");			//POS: in FMS8
						Element GeneralDescription13 = doc.createElement("GeneralDescription13");			//TAX_AMT: in FMS8 configured for LTCORA
						Element GeneralDescription14 = doc.createElement("GeneralDescription14");			
						Element GeneralDescription15 = doc.createElement("GeneralDescription15");			//TOTAL INV AMT 
						
						Ledger.appendChild(Line);
						Line.appendChild(AccountCode);
						Line.appendChild(AccountingPeriod);
						Line.appendChild(BaseAmount);
						Line.appendChild(DebitCredit);
						Line.appendChild(TransactionAmount);
						Line.appendChild(Base2ReportingAmount);
						Line.appendChild(CurrencyCode);
						Line.appendChild(CurrencyRate);
						Line.appendChild(TransactionDate);
						Line.appendChild(JournalType);
						Line.appendChild(JournalSource);		//Null in FMS8
						Line.appendChild(TransactionReference);
						Line.appendChild(Description);
						Line.appendChild(DueDate);
						Line.appendChild(AnalysisCode1);
						Line.appendChild(AnalysisCode2);		
						Line.appendChild(AnalysisCode3);		
						Line.appendChild(AnalysisCode4);		
						Line.appendChild(AnalysisCode5);		
						Line.appendChild(AnalysisCode6);		
						Line.appendChild(AnalysisCode7);		
						Line.appendChild(AnalysisCode8);		
						Line.appendChild(AnalysisCode10);		
						Line.appendChild(GeneralDescription9);		
						Line.appendChild(GeneralDescription10);		
						Line.appendChild(GeneralDescription11);		
						Line.appendChild(GeneralDescription12);		
						Line.appendChild(GeneralDescription13);		
						Line.appendChild(GeneralDescription14);		
						Line.appendChild(GeneralDescription15);
						
						AccountCode.appendChild(doc.createTextNode(account_code));
						DebitCredit.appendChild(doc.createTextNode("D"));
						AccountingPeriod.appendChild(doc.createTextNode(accountPeriod));
						BaseAmount.appendChild(doc.createTextNode(gross_amt));
						TransactionAmount.appendChild(doc.createTextNode(gross_amt));
						Base2ReportingAmount.appendChild(doc.createTextNode("0.00"));	
						CurrencyCode.appendChild(doc.createTextNode("INR"));
						CurrencyRate.appendChild(doc.createTextNode(exchg_rate));	
						TransactionDate.appendChild(doc.createTextNode(trans_dt));
						JournalType.appendChild(doc.createTextNode(journal_type));
						TransactionReference.appendChild(doc.createTextNode(invoice_no));
						Description.appendChild(doc.createTextNode(description));
						if(!cont_type.equals("O") && !cont_type.equals("Q"))
						{
							AnalysisCode4.appendChild(doc.createTextNode(latePaymentAcc_cd));
						}
						AnalysisCode10.appendChild(doc.createTextNode(bucac));
						if(cont_type.equals("O") || cont_type.equals("Q"))
						{
							GeneralDescription9.appendChild(doc.createTextNode("Service"));		//GOODS_SERVICE_FLAG
							GeneralDescription10.appendChild(doc.createTextNode("0"));			//REV_CHARGE
							GeneralDescription11.appendChild(doc.createTextNode("999799"));		//HSN_SAC
							GeneralDescription12.appendChild(doc.createTextNode(buStateNm));	//POS
							GeneralDescription13.appendChild(doc.createTextNode(tax_amt));		//TAX_AMT
							GeneralDescription14.appendChild(doc.createTextNode(sup_type));		//SUPPLY_TYPE
							GeneralDescription15.appendChild(doc.createTextNode(tax_amt));	//TOTAL_INV_AMT
						}
						//TAX LINE for LTCORA LATEPAYMENT INVOICE 
						if(!tax_amt.equals(""))
						{
							Element Line_A  = doc.createElement("Line");
							Element AccountCode_A = doc.createElement("AccountCode");
							Element AccountingPeriod_A = doc.createElement("AccountingPeriod");
							Element BaseAmount_A = doc.createElement("BaseAmount");
							Element DebitCredit_A = doc.createElement("DebitCredit");
							Element TransactionAmount_A = doc.createElement("TransactionAmount");
							Element Base2ReportingAmount_A = doc.createElement("Base2ReportingAmount");
							Element CurrencyCode_A = doc.createElement("CurrencyCode");
							Element CurrencyRate_A = doc.createElement("CurrencyRate");
							Element TransactionDate_A = doc.createElement("TransactionDate");
							Element JournalType_A = doc.createElement("JournalType");
							Element JournalSource_A = doc.createElement("JournalSource");			//Null in FMS8
							Element TransactionReference_A = doc.createElement("TransactionReference");
							Element Description_A = doc.createElement("Description");
							Element DueDate_A = doc.createElement("DueDate");
							Element AnalysisCode1_A = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
							Element AnalysisCode2_A = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
							Element AnalysisCode3_A = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
							Element AnalysisCode4_A = doc.createElement("AnalysisCode4");			//COA_CD: 
							Element AnalysisCode5_A = doc.createElement("AnalysisCode5");			//Null: in FMS8
							Element AnalysisCode6_A = doc.createElement("AnalysisCode6");			
							Element AnalysisCode7_A = doc.createElement("AnalysisCode7");			
							Element AnalysisCode8_A = doc.createElement("AnalysisCode8");			//NULL: in FMS8
							Element AnalysisCode10_A = doc.createElement("AnalysisCode10");			//BU SUN account code 
							Element GeneralDescription9_A = doc.createElement("GeneralDescription9");			//Service for LTCORA else NULL 
							Element GeneralDescription10_A = doc.createElement("GeneralDescription10");			//REV_CHARGE: in FMS8
							Element GeneralDescription11_A = doc.createElement("GeneralDescription11");			//HSN_SAC: in FMS8
							Element GeneralDescription12_A = doc.createElement("GeneralDescription12");			//POS: in FMS8
							Element GeneralDescription13_A = doc.createElement("GeneralDescription13");			//TAX_AMT: in FMS8 configured for LTCORA
							Element GeneralDescription14_A = doc.createElement("GeneralDescription14");			
							Element GeneralDescription15_A = doc.createElement("GeneralDescription15");			//TOTAL INV AMT 
							
							Ledger.appendChild(Line_A);
							Line_A.appendChild(AccountCode_A);
							Line_A.appendChild(AccountingPeriod_A);
							Line_A.appendChild(BaseAmount_A);
							Line_A.appendChild(DebitCredit_A);
							Line_A.appendChild(TransactionAmount_A);
							Line_A.appendChild(Base2ReportingAmount_A);
							Line_A.appendChild(CurrencyCode_A);
							Line_A.appendChild(CurrencyRate_A);
							Line_A.appendChild(TransactionDate_A);
							Line_A.appendChild(JournalType_A);
							Line_A.appendChild(JournalSource_A);		//Null in FMS8
							Line_A.appendChild(TransactionReference_A);
							Line_A.appendChild(Description_A);
							Line_A.appendChild(DueDate_A);
							Line_A.appendChild(AnalysisCode1_A);
							Line_A.appendChild(AnalysisCode2_A);		
							Line_A.appendChild(AnalysisCode3_A);		
							Line_A.appendChild(AnalysisCode4_A);		
							Line_A.appendChild(AnalysisCode5_A);		
							Line_A.appendChild(AnalysisCode6_A);		
							Line_A.appendChild(AnalysisCode7_A);		
							Line_A.appendChild(AnalysisCode8_A);		
							Line_A.appendChild(AnalysisCode10_A);		
							Line_A.appendChild(GeneralDescription9_A);		
							Line_A.appendChild(GeneralDescription10_A);		
							Line_A.appendChild(GeneralDescription11_A);		
							Line_A.appendChild(GeneralDescription12_A);		
							Line_A.appendChild(GeneralDescription13_A);		
							Line_A.appendChild(GeneralDescription14_A);		
							Line_A.appendChild(GeneralDescription15_A);
							
							AccountCode_A.appendChild(doc.createTextNode(account_code));
							DebitCredit_A.appendChild(doc.createTextNode("D"));
							AccountingPeriod_A.appendChild(doc.createTextNode(accountPeriod));
							BaseAmount_A.appendChild(doc.createTextNode(tax_amt));
							TransactionAmount_A.appendChild(doc.createTextNode(tax_amt));
							Base2ReportingAmount_A.appendChild(doc.createTextNode("0.00"));	
							CurrencyCode_A.appendChild(doc.createTextNode("INR"));
							CurrencyRate_A.appendChild(doc.createTextNode(exchg_rate));	
							TransactionDate_A.appendChild(doc.createTextNode(trans_dt));
							JournalType_A.appendChild(doc.createTextNode(journal_type));
							TransactionReference_A.appendChild(doc.createTextNode(invoice_no));
							Description_A.appendChild(doc.createTextNode(description));
							//AnalysisCode4.appendChild(doc.createTextNode(latePaymentAcc_cd));
							AnalysisCode7_A.appendChild(doc.createTextNode("NA"));
							AnalysisCode10_A.appendChild(doc.createTextNode(bucac));
							if(cont_type.equals("O") || cont_type.equals("Q"))
							{
								GeneralDescription9_A.appendChild(doc.createTextNode("Service"));		//GOODS_SERVICE_FLAG
								GeneralDescription10_A.appendChild(doc.createTextNode("0"));			//REV_CHARGE
								GeneralDescription11_A.appendChild(doc.createTextNode("999799"));		//HSN_SAC
								GeneralDescription12_A.appendChild(doc.createTextNode(buStateNm));	//POS
								GeneralDescription13_A.appendChild(doc.createTextNode(tax_amt));		//TAX_AMT
								GeneralDescription14_A.appendChild(doc.createTextNode(sup_type));		//SUPPLY_TYPE
								GeneralDescription15_A.appendChild(doc.createTextNode(tax_amt));	//TOTAL_INV_AMT
							}
						}
						
						//CREDIT LINE
						Element Line_A  = doc.createElement("Line");
						Element AccountCode_A = doc.createElement("AccountCode");
						Element AccountingPeriod_A = doc.createElement("AccountingPeriod");
						Element BaseAmount_A = doc.createElement("BaseAmount");
						Element DebitCredit_A = doc.createElement("DebitCredit");
						Element TransactionAmount_A = doc.createElement("TransactionAmount");
						Element Base2ReportingAmount_A = doc.createElement("Base2ReportingAmount");
						Element CurrencyCode_A = doc.createElement("CurrencyCode");
						Element CurrencyRate_A = doc.createElement("CurrencyRate");
						Element TransactionDate_A = doc.createElement("TransactionDate");
						Element JournalType_A = doc.createElement("JournalType");
						Element JournalSource_A = doc.createElement("JournalSource");			//Null in FMS8
						Element TransactionReference_A = doc.createElement("TransactionReference");
						Element Description_A = doc.createElement("Description");
						Element DueDate_A = doc.createElement("DueDate");
						Element AnalysisCode1_A = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
						Element AnalysisCode2_A = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
						Element AnalysisCode3_A = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
						Element AnalysisCode4_A = doc.createElement("AnalysisCode4");			//COA_CD: 
						Element AnalysisCode5_A = doc.createElement("AnalysisCode5");			//Null: in FMS8
						Element AnalysisCode6_A = doc.createElement("AnalysisCode6");			
						Element AnalysisCode7_A = doc.createElement("AnalysisCode7");			
						Element AnalysisCode8_A = doc.createElement("AnalysisCode8");			//NULL: in FMS8
						Element AnalysisCode10_A = doc.createElement("AnalysisCode10");			//BU SUN account code 
						Element GeneralDescription9_A = doc.createElement("GeneralDescription9");			//Service for LTCORA else NULL 
						Element GeneralDescription10_A = doc.createElement("GeneralDescription10");			//REV_CHARGE: in FMS8
						Element GeneralDescription11_A = doc.createElement("GeneralDescription11");			//HSN_SAC: in FMS8
						Element GeneralDescription12_A = doc.createElement("GeneralDescription12");			//POS: in FMS8
						Element GeneralDescription13_A = doc.createElement("GeneralDescription13");			//TAX_AMT: in FMS8 configured for LTCORA
						Element GeneralDescription14_A = doc.createElement("GeneralDescription14");			
						Element GeneralDescription15_A = doc.createElement("GeneralDescription15");			//TOTAL INV AMT 
						
						Ledger.appendChild(Line_A);
						Line_A.appendChild(AccountCode_A);
						Line_A.appendChild(AccountingPeriod_A);
						Line_A.appendChild(BaseAmount_A);
						Line_A.appendChild(DebitCredit_A);
						Line_A.appendChild(TransactionAmount_A);
						Line_A.appendChild(Base2ReportingAmount_A);
						Line_A.appendChild(CurrencyCode_A);
						Line_A.appendChild(CurrencyRate_A);
						Line_A.appendChild(TransactionDate_A);
						Line_A.appendChild(JournalType_A);
						Line_A.appendChild(JournalSource_A);		//Null in FMS8
						Line_A.appendChild(TransactionReference_A);
						Line_A.appendChild(Description_A);
						Line_A.appendChild(DueDate_A);
						Line_A.appendChild(AnalysisCode1_A);
						Line_A.appendChild(AnalysisCode2_A);		
						Line_A.appendChild(AnalysisCode3_A);		
						Line_A.appendChild(AnalysisCode4_A);		
						Line_A.appendChild(AnalysisCode5_A);		
						Line_A.appendChild(AnalysisCode6_A);		
						Line_A.appendChild(AnalysisCode7_A);		
						Line_A.appendChild(AnalysisCode8_A);		
						Line_A.appendChild(AnalysisCode10_A);		
						Line_A.appendChild(GeneralDescription9_A);		
						Line_A.appendChild(GeneralDescription10_A);		
						Line_A.appendChild(GeneralDescription11_A);		
						Line_A.appendChild(GeneralDescription12_A);		
						Line_A.appendChild(GeneralDescription13_A);		
						Line_A.appendChild(GeneralDescription14_A);		
						Line_A.appendChild(GeneralDescription15_A);
						
						AccountCode_A.appendChild(doc.createTextNode(latePaymentAcc_cd));
						DebitCredit_A.appendChild(doc.createTextNode("C"));
						AnalysisCode2_A.appendChild(doc.createTextNode(cccac));
						AccountingPeriod_A.appendChild(doc.createTextNode(accountPeriod));
						BaseAmount_A.appendChild(doc.createTextNode(gross_amt));
						TransactionAmount_A.appendChild(doc.createTextNode(gross_amt));
						Base2ReportingAmount_A.appendChild(doc.createTextNode("0.00"));	
						CurrencyCode_A.appendChild(doc.createTextNode("INR"));
						CurrencyRate_A.appendChild(doc.createTextNode(exchg_rate));	
						TransactionDate_A.appendChild(doc.createTextNode(trans_dt));
						JournalType_A.appendChild(doc.createTextNode(journal_type));
						TransactionReference_A.appendChild(doc.createTextNode(invoice_no));
						Description_A.appendChild(doc.createTextNode(description));
						AnalysisCode4_A.appendChild(doc.createTextNode(latePaymentAcc_cd));
						AnalysisCode10_A.appendChild(doc.createTextNode(bucac));
						if(cont_type.equals("O") || cont_type.equals("Q"))
						{
							GeneralDescription9_A.appendChild(doc.createTextNode("Service"));		//GOODS_SERVICE_FLAG
							GeneralDescription10_A.appendChild(doc.createTextNode("0"));			//REV_CHARGE
							GeneralDescription11_A.appendChild(doc.createTextNode("999799"));		//HSN_SAC
							GeneralDescription12_A.appendChild(doc.createTextNode(buStateNm));	//POS
							GeneralDescription13_A.appendChild(doc.createTextNode(tax_amt));		//TAX_AMT
							GeneralDescription14_A.appendChild(doc.createTextNode(sup_type));		//SUPPLY_TYPE
							GeneralDescription15_A.appendChild(doc.createTextNode(tax_amt));	//TOTAL_INV_AMT
						}
						
						//CREDITING THE TAX INTO ITS ACCOUNT

						String queryString3="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
								+ "FROM FMS_INV_TAX_DTL "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
								+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=?";
						stmt1=conn.prepareStatement(queryString3);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, fin_year);
						stmt1.setString(3, invoice_seq);
						stmt1.setString(4, bu_state_tin);
						rset1=stmt1.executeQuery();
						while(rset1.next())
						{
							Element Line_B  = doc.createElement("Line");
							Element AccountCode_B = doc.createElement("AccountCode");
							Element AccountingPeriod_B = doc.createElement("AccountingPeriod");
							Element BaseAmount_B = doc.createElement("BaseAmount");
							Element DebitCredit_B = doc.createElement("DebitCredit");
							Element TransactionAmount_B = doc.createElement("TransactionAmount");
							Element Base2ReportingAmount_B = doc.createElement("Base2ReportingAmount");
							Element CurrencyCode_B = doc.createElement("CurrencyCode");
							Element CurrencyRate_B = doc.createElement("CurrencyRate");
							Element TransactionDate_B = doc.createElement("TransactionDate");
							Element JournalType_B = doc.createElement("JournalType");
							Element JournalSource_B = doc.createElement("JournalSource");			//Null in FMS8
							Element TransactionReference_B = doc.createElement("TransactionReference");
							Element Description_B = doc.createElement("Description");
							Element DueDate_B = doc.createElement("DueDate");
							Element AnalysisCode1_B = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
							Element AnalysisCode2_B = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
							Element AnalysisCode3_B = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
							Element AnalysisCode4_B = doc.createElement("AnalysisCode4");			//COA_CD: 
							Element AnalysisCode5_B = doc.createElement("AnalysisCode5");			//Null: in FMS8
							Element AnalysisCode6_B = doc.createElement("AnalysisCode6");			
							Element AnalysisCode7_B = doc.createElement("AnalysisCode7");			
							Element AnalysisCode8_B = doc.createElement("AnalysisCode8");			//NULL: in FMS8
							Element AnalysisCode10_B = doc.createElement("AnalysisCode10");			//BU SUN account code 
							Element GeneralDescription9_B = doc.createElement("GeneralDescription9");			//Service for LTCORA else NULL 
							Element GeneralDescription10_B = doc.createElement("GeneralDescription10");			//REV_CHARGE: in FMS8
							Element GeneralDescription11_B = doc.createElement("GeneralDescription11");			//HSN_SAC: in FMS8
							Element GeneralDescription12_B = doc.createElement("GeneralDescription12");			//POS: in FMS8
							Element GeneralDescription13_B = doc.createElement("GeneralDescription13");			//TAX_BMT: in FMS8 configured for LTCORA
							Element GeneralDescription14_B = doc.createElement("GeneralDescription14");			
							Element GeneralDescription15_B = doc.createElement("GeneralDescription15");			//TOTAL INV AMT 
							
							Ledger.appendChild(Line_B);
							Line_B.appendChild(AccountCode_B);
							Line_B.appendChild(AccountingPeriod_B);
							Line_B.appendChild(BaseAmount_B);
							Line_B.appendChild(DebitCredit_B);
							Line_B.appendChild(TransactionAmount_B);
							Line_B.appendChild(Base2ReportingAmount_B);
							Line_B.appendChild(CurrencyCode_B);
							Line_B.appendChild(CurrencyRate_B);
							Line_B.appendChild(TransactionDate_B);
							Line_B.appendChild(JournalType_B);
							Line_B.appendChild(JournalSource_B);		//Null in FMS8
							Line_B.appendChild(TransactionReference_B);
							Line_B.appendChild(Description_B);
							Line_B.appendChild(DueDate_B);
							Line_B.appendChild(AnalysisCode1_B);
							Line_B.appendChild(AnalysisCode2_B);		
							Line_B.appendChild(AnalysisCode3_B);		
							Line_B.appendChild(AnalysisCode4_B);		
							Line_B.appendChild(AnalysisCode5_B);		
							Line_B.appendChild(AnalysisCode6_B);		
							Line_B.appendChild(AnalysisCode7_B);		
							Line_B.appendChild(AnalysisCode8_B);		
							Line_B.appendChild(AnalysisCode10_B);		
							Line_B.appendChild(GeneralDescription9_B);		
							Line_B.appendChild(GeneralDescription10_B);		
							Line_B.appendChild(GeneralDescription11_B);		
							Line_B.appendChild(GeneralDescription12_B);		
							Line_B.appendChild(GeneralDescription13_B);		
							Line_B.appendChild(GeneralDescription14_B);		
							Line_B.appendChild(GeneralDescription15_B);
							
							String tax_code=rset1.getString(1)==null?"":rset1.getString(1);
							String taxStrctCd=rset1.getString(2)==null?"":rset1.getString(2);
							String taxAmt=rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
							String taxBaseAmt=rset1.getString(4)==null?"":nf.format(rset1.getDouble(4));
							
							String acc_cd=getTaxStructureSunAccountCd(taxStrctCd,tax_code, bu_state_tin, bu_unit,"S");
							
							AccountCode_B.appendChild(doc.createTextNode(acc_cd));
							AccountingPeriod_B.appendChild(doc.createTextNode(accountPeriod));
							BaseAmount_B.appendChild(doc.createTextNode(taxAmt));
							DebitCredit_B.appendChild(doc.createTextNode("C"));
							TransactionAmount_B.appendChild(doc.createTextNode(taxAmt));
							Base2ReportingAmount_B.appendChild(doc.createTextNode("0.00"));		
							CurrencyCode_B.appendChild(doc.createTextNode("INR"));
							CurrencyRate_B.appendChild(doc.createTextNode(exchg_rate));					
							TransactionDate_B.appendChild(doc.createTextNode(trans_dt));
							JournalType_B.appendChild(doc.createTextNode(journal_type));
							TransactionReference_B.appendChild(doc.createTextNode(invoice_no));
							Description_B.appendChild(doc.createTextNode(description));
							//DueDate_B.appendChild(doc.createTextNode(dueDt));
							AnalysisCode4_B.appendChild(doc.createTextNode(latePaymentAcc_cd));
							AnalysisCode10_B.appendChild(doc.createTextNode(bucac));
							if(cont_type.equals("O") || cont_type.equals("Q"))
							{
								GeneralDescription9_B.appendChild(doc.createTextNode("Service"));		//GOODS_SERVICE_FLAG
								GeneralDescription10_B.appendChild(doc.createTextNode("0"));			//REV_CHARGE
								GeneralDescription11_B.appendChild(doc.createTextNode("999799"));		//HSN_SAC
								GeneralDescription12_B.appendChild(doc.createTextNode(buStateNm));		//POS
								GeneralDescription13_B.appendChild(doc.createTextNode(tax_amt));		//TAX_AMT
								GeneralDescription14_B.appendChild(doc.createTextNode(sup_type));		//SUPPLY_TYPE
								GeneralDescription15_B.appendChild(doc.createTextNode(tax_amt));	//TOTAL_INV_AMT
							}
						}
						rset1.close();
						stmt1.close();
					}
					else
					{
						//For fetching the agreement base 
						String agmt_base="";
						String query="SELECT AGMT_BASE FROM FMS_SUPPLY_CONT_MST "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? AND CONTRACT_TYPE=? "
								+ "AND CONT_REV=? ";
						stmt1=conn.prepareStatement(query);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterpty_cd);
						stmt1.setString(3, agmt_no);
						stmt1.setString(4, agmt_rev);
						stmt1.setString(5, cont_no);
						stmt1.setString(6, cont_type);
						stmt1.setString(7, cont_rev);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							agmt_base=rset1.getString(1)==null?"":rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						//for fetching the QQ date of LTCORA Cargo
						String qq_dt="";
						String qurey2="SELECT TO_CHAR(QQ_DT,'DDMON') FROM FMS_LTCORA_CONT_CARGO_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND AGMT_TYPE=? AND CARGO_NO=? "
								+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_NO=B.AGMT_NO "
								+ "AND A.AGMT_REV=B.AGMT_REV AND A.AGMT_TYPE=B.AGMT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
								+ "AND A.CARGO_NO=B.CARGO_NO)";
						stmt1 = conn.prepareStatement(qurey2);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterpty_cd);
						stmt1.setString(3, "C");
						stmt1.setString(4, agmt_no);
						stmt1.setString(5, agmt_rev);
						stmt1.setString(6, cont_no);
						stmt1.setString(7, cont_type);
						stmt1.setString(8, "A");
						stmt1.setString(9, cargo_no);
						rset1 = stmt1.executeQuery();
						if(rset1.next())
						{
							qq_dt=rset1.getString(1)==null?"":rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						if(gen_type.equals("SG") || gen_type.equals("DLNG_SG")|| gen_type.equals("DLNG_SERV"))
						{
							if(inv_flag.equals("CR") || inv_flag.equals("DR"))
							{
								String temp_exchng_rate="0";
								String temp_gross_amt="";
								String temp_sale_price="0";
								String temp_tax_amt="";
								String temp_alloc_qty="";
								
								String queryString1="SELECT EXCHG_RATE_VALUE,GROSS_AMT,SALE_PRICE,TAX_AMT,ALLOC_QTY  "
										+ "FROM FMS_INV_CRDR_REF "
										+ "WHERE COMPANY_CD=? "
										+ "AND FINANCIAL_YEAR=? "
										+ "AND BU_STATE_TIN=? AND INVOICE_SEQ=? ";
								queryString1+="UNION ALL ";
								queryString1+="SELECT EXCHG_RATE_VALUE,GROSS_AMT,SALE_PRICE,TAX_AMT,ALLOC_QTY  "
										+ "FROM FMS_DLNG_INV_CRDR_REF "
										+ "WHERE COMPANY_CD=? "
										+ "AND FINANCIAL_YEAR=? "
										+ "AND BU_STATE_TIN=? AND INVOICE_SEQ=? ";
								stmt4=conn.prepareStatement(queryString1);
								stmt4.setString(1, comp_cd);
								stmt4.setString(2, fin_year);
								stmt4.setString(3, bu_state_tin);
								stmt4.setString(4, invoice_seq);
								stmt4.setString(5, comp_cd);
								stmt4.setString(6, fin_year);
								stmt4.setString(7, bu_state_tin);
								stmt4.setString(8, invoice_seq);
								rset4=stmt4.executeQuery();
								if(rset4.next())
								{
									temp_exchng_rate=rset4.getString(1)==null?"":nf.format(rset4.getDouble(1));
									temp_gross_amt=rset4.getString(2)==null?"":nf.format(rset4.getDouble(2));
									temp_sale_price=rset4.getString(3)==null?"":nf.format(rset4.getDouble(3));
									temp_tax_amt=rset4.getString(4)==null?"":nf.format(rset4.getDouble(4));
									temp_alloc_qty=rset4.getString(5)==null?"":nf.format(rset4.getDouble(5));
								}
								rset4.close();
								stmt4.close();
								
								
								if(!gross_amt.equals(""))
								{
									if(Double.doubleToRawLongBits(Double.parseDouble(gross_amt))==Double.doubleToRawLongBits(0))
									{
										gross_amt=temp_gross_amt;
									}
								}
								if(!alloc_qty.equals(""))
								{
									if(Double.doubleToRawLongBits(Double.parseDouble(alloc_qty))==Double.doubleToRawLongBits(0))
									{
										alloc_qty=temp_alloc_qty;
									}
								}
								if(!sales_price.equals(""))
								{
									if(Double.doubleToRawLongBits(Double.parseDouble(sales_price))==Double.doubleToRawLongBits(0))
									{
										sales_price=temp_sale_price;
									}
								}
								if(!exchg_rate.equals(""))
								{
									if(Double.doubleToRawLongBits(Double.parseDouble(exchg_rate))==Double.doubleToRawLongBits(0))
									{
										exchg_rate=temp_exchng_rate;
									}
								}
							}
							
							if(inv_raised_in.equals("2") && !exchg_rate.equals(""))
							{
								gross_amt = rset.getString(15) == null ?"":nf.format(rset.getDouble(15) * Double.parseDouble(exchg_rate));
								tax_amt = rset.getString(16) == null ? "": nf.format(rset.getDouble(16) * Double.parseDouble(exchg_rate));
								tcs_amt=rset.getString(28)==null?"":nf.format(rset.getDouble(28) * Double.parseDouble(exchg_rate));
								tds_amt=rset.getString(30)==null?"":nf.format(rset.getDouble(30) * Double.parseDouble(exchg_rate));
							}
							
							if(!exchg_rate.equals(""))
							{
								if(Double.parseDouble(exchg_rate)>0)
								{
									gross_amt_usd=nf.format(Double.parseDouble(gross_amt)/Double.parseDouble(exchg_rate));
								}
							}
							else
							{
								gross_amt_usd="0.00";
								exchg_rate="0.00";
							}
						}
						else
						{
							if(inv_raised_in.equals("2") && !exchg_rate.equals(""))
							{
								tax_amt=rset.getString(16)==null?"":nf.format(rset.getDouble(16) * Double.parseDouble(exchg_rate));
								tcs_amt=rset.getString(28)==null?"":nf.format(rset.getDouble(28) * Double.parseDouble(exchg_rate));
								tds_amt=rset.getString(30)==null?"":nf.format(rset.getDouble(30) * Double.parseDouble(exchg_rate));
								gross_amt=rset.getString(32)==null?"":nf.format(rset.getDouble(32) * Double.parseDouble(exchg_rate));
							}
							sales_price=nf.format(Double.parseDouble(gross_amt)/Double.parseDouble(alloc_qty));
						}
						
						String invoice_amt=gross_amt;
						String invoice_amt_usd=gross_amt_usd;
						if(criteria.contains("OC") || criteria.contains("MM") || criteria.contains("TC"))
						{
							double temp_inv_amt=0;
							if(!oth_amt.equals(""))
							{
								temp_inv_amt+=Double.parseDouble(oth_amt);
								invoice_amt=nf.format(temp_inv_amt);
								if(!invoice_amt_usd.equals("0.00"))
								{
									invoice_amt_usd=nf.format(Double.parseDouble(invoice_amt)/Double.parseDouble(exchg_rate));
								}
							}
							if(!market_margin_amt.equals(""))
							{
								temp_inv_amt+=Double.parseDouble(market_margin_amt);
								invoice_amt=nf.format(temp_inv_amt);
								if(!invoice_amt_usd.equals("0.00"))
								{
									invoice_amt_usd=nf.format(Double.parseDouble(invoice_amt)/Double.parseDouble(exchg_rate));
								}
							}
							if(!transportationAmt.equals(""))
							{
								temp_inv_amt+=Double.parseDouble(transportationAmt);
								invoice_amt=nf.format(temp_inv_amt);
								if(!invoice_amt_usd.equals("0.00"))
								{
									invoice_amt_usd=nf.format(Double.parseDouble(invoice_amt)/Double.parseDouble(exchg_rate));
								}
							}
						}
						if(!inv_flag.equals("CR")&&!inv_flag.equals("DR"))
						{
							double temp_inv_amt=0;
							if(!oth_amt.equals(""))
							{
								temp_inv_amt+=Double.parseDouble(oth_amt);
							}
							if(!market_margin_amt.equals(""))
							{
								temp_inv_amt+=Double.parseDouble(market_margin_amt);
							}
							invoice_amt=nf.format(Double.parseDouble(invoice_amt) +temp_inv_amt);
							if(!invoice_amt_usd.equals("0.00"))
							{
								invoice_amt_usd=nf.format(Double.parseDouble(invoice_amt)/Double.parseDouble(exchg_rate));
							}
						}
						if(inv_flag.equals("ST"))
						{
							gross_amt_usd=nf.format(0);
							exchg_rate="-";
						}
						exchg_rate=exchg_rate.equals("")?"0":exchg_rate;
						//gross_amt=gross_amt.equals("")?"":nf.format(Double.parseDouble(gross_amt)<0?(-1)*Double.parseDouble(gross_amt):Double.parseDouble(gross_amt));
						gross_amt=gross_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(gross_amt)));
						//gross_amt_usd=gross_amt_usd.equals("")?"":nf.format(Double.parseDouble(gross_amt_usd)<0?(-1)*Double.parseDouble(gross_amt_usd):Double.parseDouble(gross_amt_usd));
						gross_amt_usd=gross_amt_usd.equals("")?"":nf.format(Math.abs(Double.parseDouble(gross_amt_usd)));
						//invoice_amt=invoice_amt.equals("")?"":nf.format(Double.parseDouble(invoice_amt)<0?(-1)*Double.parseDouble(invoice_amt):Double.parseDouble(invoice_amt));
						invoice_amt=invoice_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(invoice_amt)));
						//invoice_amt_usd=invoice_amt_usd.equals("")?"":nf.format(Double.parseDouble(invoice_amt_usd)<0?(-1)*Double.parseDouble(invoice_amt_usd):Double.parseDouble(invoice_amt_usd));
						invoice_amt_usd=invoice_amt_usd.equals("")?"":nf.format(Math.abs(Double.parseDouble(invoice_amt_usd)));
						//tax_amt=tax_amt.equals("")?"":nf.format(Double.parseDouble(tax_amt)<0?(-1)*Double.parseDouble(tax_amt):Double.parseDouble(tax_amt));
						tax_amt=tax_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(tax_amt)));
						
						String gen_for="SUPPLY";
						if(cont_type.equals("O")||cont_type.equals("Q")) 
						{
							//gen_for="Re-Gas Capacity";
							gen_for="CN";
						}
						else if(cont_type.equals("Q"))
						{
							//gen_for="Period";
						}
						
						//String counterparty_abbr = utilBean.getCounterpartyABBR(conn, counterpty_cd);
						
						//String account_code = getCounterpartySunAccountCode(counterpty_cd,"C","0","DFT").trim();
						String description = "";
						if(inv_flag.equals("UG"))
						{
							description= (counterparty_abbr+" AQ "+alloc_qty+" -SUG "+sug_qty+" @ "+sales_price).toUpperCase();
						}
						else if(inv_flag.equals("ST"))
						{
							description=(counterparty_abbr+" STORAGECHEXTENDED "+qq_dt).toUpperCase();
						}
						else 
						{
							description=(counterparty_abbr+" "+gen_for+" "+alloc_qty+" MMBTU @ "+sales_price+" "+periodStartDt.split("/")[0]+"-"+periodEndDt).toUpperCase();
						}
						
						//for credit
						String counterpty_category=utilBean.getCounterpartyCategory(conn, counterpty_cd);
						String account_cd="";
						{
							if(cont_type.equals("E")||cont_type.equals("F")||cont_type.equals("S")||cont_type.equals("L"))
							{
								account_cd=getCounterpartySunAccountCode(counterpty_cd, "C", "0","S");
								if(account_cd.equals(""))
								{
									//FOR NON GROUP
									account_cd ="701001";
									if(counterpty_category.equals("Group"))
									{
										//FOR GROUP it should be 701009
										account_cd ="701009";
									}
								}
								
							}
							else if(cont_type.equals("Q")||cont_type.equals("O"))
							{
								//type_of_supply="LTCORA";
								account_cd=getCounterpartySunAccountCode(counterpty_cd, "C", "0","SI");
								if(account_cd.equals(""))
								{
									account_cd ="701003";
									if(counterpty_category.equals("Group"))
									{
										//FOR GROUP it should be 701010
										account_cd ="701010";
									}
								}
							}
							else if(cont_type.equals("X")||cont_type.equals("W"))
							{
								//type_of_supply="IGX";
								account_cd ="701006";
							}
							else if(cont_type.equals("B") || cont_type.equals("M"))
							{
								account_cd=getCounterpartySunAccountCode(counterpty_cd, "C", "0","S");
								if(account_cd.equals(""))
								{
									account_cd="802027";
								}
							}
						}
						
						if(inv_flag.equals("ST"))
						{
							account_cd="701005";
						}
						if(invoice_type.equals("TLU"))
						{
							//FOR NON GROUP
							account_cd ="701003";			//AS PER EMAIL BY VAISHNAVI DATE 20251024
							if(counterpty_category.equals("Group"))
							{
								//FOR GROUP 
								account_cd ="701010";
							}
						}
						
						//PB20260117: added for LTCORA DLNG INVOICE SAME AS TLU INVOICE
						if(gen_type.equals("DLNG_SERV"))
						{
							if(cont_type.equals("O")||cont_type.equals("Q"))
							{
								//FOR NON GROUP
								account_cd ="701003";			
								if(counterpty_category.equals("Group"))
								{
									//FOR GROUP 
									account_cd ="701010";
								}
							}
						}
						
						String cccac="";
						if(gen_type.equals("DLNG_SG") || gen_type.equals("DLNG_FFLOW") || gen_type.equals("DLNG_SERV"))
						{
							if(invoice_type.equals("TLU")||cont_type.equals("O")||cont_type.equals("Q"))		//AS PER EMAIL BY VAISHNAVI DATE 20251024
							{
								cccac= "1TM01"; 
							}
							else
							{
								cccac="2BD01";
								if(!bu_state_tin.equals("24"))
								{
									cccac="2"+getStateCd(bu_state_tin)+"01";
								}
							}
						}
						else
						{
							cccac= "1TM01"; 
							if(cont_type.equals("X") || cont_type.equals("W"))
							{
								cccac="2MK01";
							}
							if(!bu_state_tin.equals("24"))		//AS THE CONST CENTER CODE FOR GUJARAT BU UNIT IS 1TM01 FOR SN,LOA AND 2MK01 FOR IGX
							{
								cccac="2"+getStateCd(bu_state_tin)+"01";
							}
						}
						
						//Debit first line 
						String dr_cr_mark="D";
						if(!inv_flag.equals("ST"))
						{
							if(invoice_type.equals("CR") || invoice_type.equals("CCR") ||inv_flag.equals("CR"))
							{
								dr_cr_mark="C";
							}
							else
							{
								dr_cr_mark="D";
							}
						}
						for(int i=0;i<2;i++)
						{
							if(inv_flag.equals("UG"))
							{
								if(i%2!=0)
								{
									break;
								}
							}
							if(criteria.contains("TAXP"))
							{
								if(i==0)
								{
									continue;
								}
							}
							Element Line  = doc.createElement("Line");
							Element AccountCode = doc.createElement("AccountCode");
							Element AccountingPeriod = doc.createElement("AccountingPeriod");
							Element BaseAmount = doc.createElement("BaseAmount");
							Element DebitCredit = doc.createElement("DebitCredit");
							Element TransactionAmount = doc.createElement("TransactionAmount");
							Element Base2ReportingAmount = doc.createElement("Base2ReportingAmount");
							Element CurrencyCode = doc.createElement("CurrencyCode");
							Element CurrencyRate = doc.createElement("CurrencyRate");
							Element TransactionDate = doc.createElement("TransactionDate");
							Element JournalType = doc.createElement("JournalType");
							Element JournalSource = doc.createElement("JournalSource");			//Null in FMS8
							Element TransactionReference = doc.createElement("TransactionReference");
							Element Description = doc.createElement("Description");
							Element DueDate = doc.createElement("DueDate");
							Element AnalysisCode1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
							Element AnalysisCode2 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
							Element AnalysisCode3 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
							Element AnalysisCode4 = doc.createElement("AnalysisCode4");			//COA_CD: 
							Element AnalysisCode5 = doc.createElement("AnalysisCode5");			//Null: in FMS8
							Element AnalysisCode6 = doc.createElement("AnalysisCode6");			
							Element AnalysisCode7 = doc.createElement("AnalysisCode7");			
							Element AnalysisCode8 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
							Element AnalysisCode10 = doc.createElement("AnalysisCode10");			//BU SUN account code 
							Element GeneralDescription9 = doc.createElement("GeneralDescription9");			//Service for LTCORA else NULL 
							Element GeneralDescription10 = doc.createElement("GeneralDescription10");			//REV_CHARGE: in FMS8
							Element GeneralDescription11 = doc.createElement("GeneralDescription11");			//HSN_SAC: in FMS8
							Element GeneralDescription12 = doc.createElement("GeneralDescription12");			//POS: in FMS8
							Element GeneralDescription13 = doc.createElement("GeneralDescription13");			//TAX_AMT: in FMS8 configured for LTCORA
							Element GeneralDescription14 = doc.createElement("GeneralDescription14");			
							Element GeneralDescription15 = doc.createElement("GeneralDescription15");			//TOTAL INV AMT 
							
							Ledger.appendChild(Line);
							Line.appendChild(AccountCode);
							Line.appendChild(AccountingPeriod);
							Line.appendChild(BaseAmount);
							Line.appendChild(DebitCredit);
							Line.appendChild(TransactionAmount);
							Line.appendChild(Base2ReportingAmount);
							Line.appendChild(CurrencyCode);
							Line.appendChild(CurrencyRate);
							Line.appendChild(TransactionDate);
							Line.appendChild(JournalType);
							Line.appendChild(JournalSource);		//Null in FMS8
							Line.appendChild(TransactionReference);
							Line.appendChild(Description);
							Line.appendChild(DueDate);
							Line.appendChild(AnalysisCode1);
							Line.appendChild(AnalysisCode2);		
							Line.appendChild(AnalysisCode3);		
							Line.appendChild(AnalysisCode4);		
							Line.appendChild(AnalysisCode5);		
							Line.appendChild(AnalysisCode6);		
							Line.appendChild(AnalysisCode7);		
							Line.appendChild(AnalysisCode8);		
							Line.appendChild(AnalysisCode10);		
							Line.appendChild(GeneralDescription9);		
							Line.appendChild(GeneralDescription10);		
							Line.appendChild(GeneralDescription11);		
							Line.appendChild(GeneralDescription12);		
							Line.appendChild(GeneralDescription13);		
							Line.appendChild(GeneralDescription14);		
							Line.appendChild(GeneralDescription15);	
							
							AccountCode.appendChild(doc.createTextNode(account_code));
							AccountingPeriod.appendChild(doc.createTextNode(accountPeriod));
							if(inv_flag.equals("UG"))
							{
								BaseAmount.appendChild(doc.createTextNode(tax_amt));
								DebitCredit.appendChild(doc.createTextNode("D"));
								TransactionAmount.appendChild(doc.createTextNode(tax_amt));
								Base2ReportingAmount.appendChild(doc.createTextNode("0.00"));		
							}
							else
							{
								if(i%2==0)
								{
									BaseAmount.appendChild(doc.createTextNode(invoice_amt));
								}
								else
								{
									BaseAmount.appendChild(doc.createTextNode(tax_amt));
								}
								DebitCredit.appendChild(doc.createTextNode(dr_cr_mark));
								if(i%2==0)
								{
									TransactionAmount.appendChild(doc.createTextNode(invoice_amt));
								}
								else
								{
									TransactionAmount.appendChild(doc.createTextNode(tax_amt));
								}
								if(i%2==0)
								{
									Base2ReportingAmount.appendChild(doc.createTextNode(invoice_amt_usd));		
								}
								else
								{
									Base2ReportingAmount.appendChild(doc.createTextNode("0.00"));		
								}
							}
							
							//DebitCredit.appendChild(doc.createTextNode("D"));
							CurrencyCode.appendChild(doc.createTextNode("INR"));
							if(inv_flag.equals("UG"))
							{
								CurrencyRate.appendChild(doc.createTextNode("0"));					
							}
							else
							{
								CurrencyRate.appendChild(doc.createTextNode(exchg_rate));					
							}
							TransactionDate.appendChild(doc.createTextNode(trans_dt));
							JournalType.appendChild(doc.createTextNode(journal_type));
							TransactionReference.appendChild(doc.createTextNode(invoice_no));
							Description.appendChild(doc.createTextNode(description));
							DueDate.appendChild(doc.createTextNode(dueDt));
							AnalysisCode10.appendChild(doc.createTextNode(bucac));
							if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
							{
								GeneralDescription9.appendChild(doc.createTextNode("Service"));		//GOODS_SERVICE_FLAG
								GeneralDescription10.appendChild(doc.createTextNode("0"));			//REV_CHARGE
								GeneralDescription11.appendChild(doc.createTextNode("999799"));		//HSN_SAC
								GeneralDescription12.appendChild(doc.createTextNode(buStateNm));	//POS
								GeneralDescription13.appendChild(doc.createTextNode(tax_amt));		//TAX_AMT
								GeneralDescription14.appendChild(doc.createTextNode(sup_type));		//SUPPLY_TYPE
								if(inv_flag.equals("UG"))
								{
									GeneralDescription15.appendChild(doc.createTextNode(tax_amt));	//TOTAL_INV_AMT
								}
								else
								{
									GeneralDescription15.appendChild(doc.createTextNode(invoice_amt));	//TOTAL_INV_AMT
								}
							}
						}
						
						if(!inv_flag.equals("UG") && !criteria.contains("TAXP"))
						{
							//Credit 
							dr_cr_mark="C";
							if(invoice_type.equals("CR") || invoice_type.equals("CCR") ||inv_flag.equals("CR"))
							{
								dr_cr_mark="D";
							}
							else
							{
								dr_cr_mark="C";
							}
							if(criteria.contains("TC")||criteria.contains("OC") || criteria.contains("MM"))
							{
								if(criteria.contains("MM"))
								{
									if(!market_margin_amt.equals(""))
									{
										String temp_gross_amt=nf.format(Math.abs(Double.parseDouble(market_margin_amt)));
										Element Line  = doc.createElement("Line");
										Element AccountCode = doc.createElement("AccountCode");
										Element AccountingPeriod = doc.createElement("AccountingPeriod");
										Element BaseAmount = doc.createElement("BaseAmount");
										Element DebitCredit = doc.createElement("DebitCredit");
										Element TransactionAmount = doc.createElement("TransactionAmount");
										Element Base2ReportingAmount = doc.createElement("Base2ReportingAmount");
										Element CurrencyCode = doc.createElement("CurrencyCode");
										Element CurrencyRate = doc.createElement("CurrencyRate");
										Element TransactionDate = doc.createElement("TransactionDate");
										Element JournalType = doc.createElement("JournalType");
										Element JournalSource = doc.createElement("JournalSource");			//Null in FMS8
										Element TransactionReference = doc.createElement("TransactionReference");
										Element Description = doc.createElement("Description");
										Element DueDate = doc.createElement("DueDate");
										Element AnalysisCode1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
										Element AnalysisCode2 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
										Element AnalysisCode3 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
										Element AnalysisCode4 = doc.createElement("AnalysisCode4");			//COA_CD: 
										Element AnalysisCode5 = doc.createElement("AnalysisCode5");			//Null: in FMS8
										Element AnalysisCode6 = doc.createElement("AnalysisCode6");			
										Element AnalysisCode7 = doc.createElement("AnalysisCode7");			
										Element AnalysisCode8 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
										Element AnalysisCode10 = doc.createElement("AnalysisCode10");			//BU SUN account code 
										Element GeneralDescription9 = doc.createElement("GeneralDescription9");			//Service for LTCORA else NULL 
										Element GeneralDescription10 = doc.createElement("GeneralDescription10");			//REV_CHARGE: in FMS8
										Element GeneralDescription11 = doc.createElement("GeneralDescription11");			//HSN_SAC: in FMS8
										Element GeneralDescription12 = doc.createElement("GeneralDescription12");			//POS: in FMS8
										Element GeneralDescription13 = doc.createElement("GeneralDescription13");			//TAX_AMT: in FMS8 configured for LTCORA
										Element GeneralDescription14 = doc.createElement("GeneralDescription14");			
										Element GeneralDescription15 = doc.createElement("GeneralDescription15");			//TOTAL INV AMT 
										
										Ledger.appendChild(Line);
										Line.appendChild(AccountCode);
										Line.appendChild(AccountingPeriod);
										Line.appendChild(BaseAmount);
										Line.appendChild(DebitCredit);
										Line.appendChild(TransactionAmount);
										Line.appendChild(Base2ReportingAmount);
										Line.appendChild(CurrencyCode);
										Line.appendChild(CurrencyRate);
										Line.appendChild(TransactionDate);
										Line.appendChild(JournalType);
										Line.appendChild(JournalSource);		//Null in FMS8
										Line.appendChild(TransactionReference);
										Line.appendChild(Description);
										Line.appendChild(DueDate);
										Line.appendChild(AnalysisCode1);
										Line.appendChild(AnalysisCode2);		
										Line.appendChild(AnalysisCode3);		
										Line.appendChild(AnalysisCode4);		
										Line.appendChild(AnalysisCode5);		
										Line.appendChild(AnalysisCode6);		
										Line.appendChild(AnalysisCode7);		
										Line.appendChild(AnalysisCode8);		
										Line.appendChild(AnalysisCode10);		
										Line.appendChild(GeneralDescription9);		
										Line.appendChild(GeneralDescription10);		
										Line.appendChild(GeneralDescription11);		
										Line.appendChild(GeneralDescription12);		
										Line.appendChild(GeneralDescription13);		
										Line.appendChild(GeneralDescription14);		
										Line.appendChild(GeneralDescription15);	
										
										AccountCode.appendChild(doc.createTextNode(account_cd));
										AccountingPeriod.appendChild(doc.createTextNode(accountPeriod));
										BaseAmount.appendChild(doc.createTextNode(temp_gross_amt));
										//DebitCredit.appendChild(doc.createTextNode("C"));
										DebitCredit.appendChild(doc.createTextNode(dr_cr_mark));
										TransactionAmount.appendChild(doc.createTextNode(temp_gross_amt));
										Base2ReportingAmount.appendChild(doc.createTextNode(invoice_amt_usd));		
										CurrencyCode.appendChild(doc.createTextNode("INR"));
										CurrencyRate.appendChild(doc.createTextNode(exchg_rate));					
										TransactionDate.appendChild(doc.createTextNode(trans_dt));
										JournalType.appendChild(doc.createTextNode(journal_type));
										TransactionReference.appendChild(doc.createTextNode(invoice_no));
										Description.appendChild(doc.createTextNode(description));
										DueDate.appendChild(doc.createTextNode(dueDt));
										AnalysisCode2.appendChild(doc.createTextNode(cccac));
										AnalysisCode7.appendChild(doc.createTextNode("NA"));
										AnalysisCode10.appendChild(doc.createTextNode(bucac));
										if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
										{
											GeneralDescription9.appendChild(doc.createTextNode("Service"));		//GOODS_SERVICE_FLAG
											GeneralDescription10.appendChild(doc.createTextNode("0"));			//REV_CHARGE
											GeneralDescription11.appendChild(doc.createTextNode("999799"));		//HSN_SAC
											GeneralDescription12.appendChild(doc.createTextNode(buStateNm));	//POS
											GeneralDescription13.appendChild(doc.createTextNode(tax_amt));		//TAX_AMT
											GeneralDescription14.appendChild(doc.createTextNode(sup_type));		//SUPPLY_TYPE
											GeneralDescription15.appendChild(doc.createTextNode(invoice_amt));	//TOTAL_INV_AMT
										}
									}
								}
								if(criteria.contains("OC"))
								{
									if(!oth_amt.equals(""))
									{
										String temp_gross_amt=nf.format(Math.abs(Double.parseDouble(oth_amt)));
										Element Line  = doc.createElement("Line");
										Element AccountCode = doc.createElement("AccountCode");
										Element AccountingPeriod = doc.createElement("AccountingPeriod");
										Element BaseAmount = doc.createElement("BaseAmount");
										Element DebitCredit = doc.createElement("DebitCredit");
										Element TransactionAmount = doc.createElement("TransactionAmount");
										Element Base2ReportingAmount = doc.createElement("Base2ReportingAmount");
										Element CurrencyCode = doc.createElement("CurrencyCode");
										Element CurrencyRate = doc.createElement("CurrencyRate");
										Element TransactionDate = doc.createElement("TransactionDate");
										Element JournalType = doc.createElement("JournalType");
										Element JournalSource = doc.createElement("JournalSource");			//Null in FMS8
										Element TransactionReference = doc.createElement("TransactionReference");
										Element Description = doc.createElement("Description");
										Element DueDate = doc.createElement("DueDate");
										Element AnalysisCode1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
										Element AnalysisCode2 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
										Element AnalysisCode3 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
										Element AnalysisCode4 = doc.createElement("AnalysisCode4");			//COA_CD: 
										Element AnalysisCode5 = doc.createElement("AnalysisCode5");			//Null: in FMS8
										Element AnalysisCode6 = doc.createElement("AnalysisCode6");			
										Element AnalysisCode7 = doc.createElement("AnalysisCode7");			
										Element AnalysisCode8 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
										Element AnalysisCode10 = doc.createElement("AnalysisCode10");			//BU SUN account code 
										Element GeneralDescription9 = doc.createElement("GeneralDescription9");			//Service for LTCORA else NULL 
										Element GeneralDescription10 = doc.createElement("GeneralDescription10");			//REV_CHARGE: in FMS8
										Element GeneralDescription11 = doc.createElement("GeneralDescription11");			//HSN_SAC: in FMS8
										Element GeneralDescription12 = doc.createElement("GeneralDescription12");			//POS: in FMS8
										Element GeneralDescription13 = doc.createElement("GeneralDescription13");			//TAX_AMT: in FMS8 configured for LTCORA
										Element GeneralDescription14 = doc.createElement("GeneralDescription14");			
										Element GeneralDescription15 = doc.createElement("GeneralDescription15");			//TOTAL INV AMT 
										
										Ledger.appendChild(Line);
										Line.appendChild(AccountCode);
										Line.appendChild(AccountingPeriod);
										Line.appendChild(BaseAmount);
										Line.appendChild(DebitCredit);
										Line.appendChild(TransactionAmount);
										Line.appendChild(Base2ReportingAmount);
										Line.appendChild(CurrencyCode);
										Line.appendChild(CurrencyRate);
										Line.appendChild(TransactionDate);
										Line.appendChild(JournalType);
										Line.appendChild(JournalSource);		//Null in FMS8
										Line.appendChild(TransactionReference);
										Line.appendChild(Description);
										Line.appendChild(DueDate);
										Line.appendChild(AnalysisCode1);
										Line.appendChild(AnalysisCode2);		
										Line.appendChild(AnalysisCode3);		
										Line.appendChild(AnalysisCode4);		
										Line.appendChild(AnalysisCode5);		
										Line.appendChild(AnalysisCode6);		
										Line.appendChild(AnalysisCode7);		
										Line.appendChild(AnalysisCode8);		
										Line.appendChild(AnalysisCode10);		
										Line.appendChild(GeneralDescription9);		
										Line.appendChild(GeneralDescription10);		
										Line.appendChild(GeneralDescription11);		
										Line.appendChild(GeneralDescription12);		
										Line.appendChild(GeneralDescription13);		
										Line.appendChild(GeneralDescription14);		
										Line.appendChild(GeneralDescription15);	
										
										AccountCode.appendChild(doc.createTextNode(account_cd));
										AccountingPeriod.appendChild(doc.createTextNode(accountPeriod));
										BaseAmount.appendChild(doc.createTextNode(temp_gross_amt));
										//DebitCredit.appendChild(doc.createTextNode("C"));
										DebitCredit.appendChild(doc.createTextNode(dr_cr_mark));
										TransactionAmount.appendChild(doc.createTextNode(temp_gross_amt));
										Base2ReportingAmount.appendChild(doc.createTextNode(invoice_amt_usd));		
										CurrencyCode.appendChild(doc.createTextNode("INR"));
										CurrencyRate.appendChild(doc.createTextNode(exchg_rate));					
										TransactionDate.appendChild(doc.createTextNode(trans_dt));
										JournalType.appendChild(doc.createTextNode(journal_type));
										TransactionReference.appendChild(doc.createTextNode(invoice_no));
										Description.appendChild(doc.createTextNode(description));
										DueDate.appendChild(doc.createTextNode(dueDt));
										AnalysisCode2.appendChild(doc.createTextNode(cccac));
										AnalysisCode7.appendChild(doc.createTextNode("NA"));
										AnalysisCode10.appendChild(doc.createTextNode(bucac));
										if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
										{
											GeneralDescription9.appendChild(doc.createTextNode("Service"));		//GOODS_SERVICE_FLAG
											GeneralDescription10.appendChild(doc.createTextNode("0"));			//REV_CHARGE
											GeneralDescription11.appendChild(doc.createTextNode("999799"));		//HSN_SAC
											GeneralDescription12.appendChild(doc.createTextNode(buStateNm));	//POS
											GeneralDescription13.appendChild(doc.createTextNode(tax_amt));		//TAX_AMT
											GeneralDescription14.appendChild(doc.createTextNode(sup_type));		//SUPPLY_TYPE
											GeneralDescription15.appendChild(doc.createTextNode(invoice_amt));	//TOTAL_INV_AMT
										}
									}
								}
								if(criteria.contains("TC"))
								{
									if(!transportationAmt.equals(""))
									{
										String temp_gross_amt=nf.format(Math.abs(Double.parseDouble(transportationAmt)));
										Element Line  = doc.createElement("Line");
										Element AccountCode = doc.createElement("AccountCode");
										Element AccountingPeriod = doc.createElement("AccountingPeriod");
										Element BaseAmount = doc.createElement("BaseAmount");
										Element DebitCredit = doc.createElement("DebitCredit");
										Element TransactionAmount = doc.createElement("TransactionAmount");
										Element Base2ReportingAmount = doc.createElement("Base2ReportingAmount");
										Element CurrencyCode = doc.createElement("CurrencyCode");
										Element CurrencyRate = doc.createElement("CurrencyRate");
										Element TransactionDate = doc.createElement("TransactionDate");
										Element JournalType = doc.createElement("JournalType");
										Element JournalSource = doc.createElement("JournalSource");			//Null in FMS8
										Element TransactionReference = doc.createElement("TransactionReference");
										Element Description = doc.createElement("Description");
										Element DueDate = doc.createElement("DueDate");
										Element AnalysisCode1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
										Element AnalysisCode2 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
										Element AnalysisCode3 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
										Element AnalysisCode4 = doc.createElement("AnalysisCode4");			//COA_CD: 
										Element AnalysisCode5 = doc.createElement("AnalysisCode5");			//Null: in FMS8
										Element AnalysisCode6 = doc.createElement("AnalysisCode6");			
										Element AnalysisCode7 = doc.createElement("AnalysisCode7");			
										Element AnalysisCode8 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
										Element AnalysisCode10 = doc.createElement("AnalysisCode10");			//BU SUN account code 
										Element GeneralDescription9 = doc.createElement("GeneralDescription9");			//Service for LTCORA else NULL 
										Element GeneralDescription10 = doc.createElement("GeneralDescription10");			//REV_CHARGE: in FMS8
										Element GeneralDescription11 = doc.createElement("GeneralDescription11");			//HSN_SAC: in FMS8
										Element GeneralDescription12 = doc.createElement("GeneralDescription12");			//POS: in FMS8
										Element GeneralDescription13 = doc.createElement("GeneralDescription13");			//TAX_AMT: in FMS8 configured for LTCORA
										Element GeneralDescription14 = doc.createElement("GeneralDescription14");			
										Element GeneralDescription15 = doc.createElement("GeneralDescription15");			//TOTAL INV AMT 
										
										Ledger.appendChild(Line);
										Line.appendChild(AccountCode);
										Line.appendChild(AccountingPeriod);
										Line.appendChild(BaseAmount);
										Line.appendChild(DebitCredit);
										Line.appendChild(TransactionAmount);
										Line.appendChild(Base2ReportingAmount);
										Line.appendChild(CurrencyCode);
										Line.appendChild(CurrencyRate);
										Line.appendChild(TransactionDate);
										Line.appendChild(JournalType);
										Line.appendChild(JournalSource);		//Null in FMS8
										Line.appendChild(TransactionReference);
										Line.appendChild(Description);
										Line.appendChild(DueDate);
										Line.appendChild(AnalysisCode1);
										Line.appendChild(AnalysisCode2);		
										Line.appendChild(AnalysisCode3);		
										Line.appendChild(AnalysisCode4);		
										Line.appendChild(AnalysisCode5);		
										Line.appendChild(AnalysisCode6);		
										Line.appendChild(AnalysisCode7);		
										Line.appendChild(AnalysisCode8);		
										Line.appendChild(AnalysisCode10);		
										Line.appendChild(GeneralDescription9);		
										Line.appendChild(GeneralDescription10);		
										Line.appendChild(GeneralDescription11);		
										Line.appendChild(GeneralDescription12);		
										Line.appendChild(GeneralDescription13);		
										Line.appendChild(GeneralDescription14);		
										Line.appendChild(GeneralDescription15);	
										
										AccountCode.appendChild(doc.createTextNode(trans_account_cd));
										AccountingPeriod.appendChild(doc.createTextNode(accountPeriod));
										BaseAmount.appendChild(doc.createTextNode(temp_gross_amt));
										//DebitCredit.appendChild(doc.createTextNode("C"));
										DebitCredit.appendChild(doc.createTextNode(dr_cr_mark));
										TransactionAmount.appendChild(doc.createTextNode(temp_gross_amt));
										Base2ReportingAmount.appendChild(doc.createTextNode(invoice_amt_usd));		
										CurrencyCode.appendChild(doc.createTextNode("INR"));
										CurrencyRate.appendChild(doc.createTextNode(exchg_rate));					
										TransactionDate.appendChild(doc.createTextNode(trans_dt));
										JournalType.appendChild(doc.createTextNode(journal_type));
										TransactionReference.appendChild(doc.createTextNode(invoice_no));
										Description.appendChild(doc.createTextNode(description));
										DueDate.appendChild(doc.createTextNode(dueDt));
										AnalysisCode2.appendChild(doc.createTextNode(cccac));
										AnalysisCode7.appendChild(doc.createTextNode("NA"));
										AnalysisCode10.appendChild(doc.createTextNode(bucac));
										if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
										{
											GeneralDescription9.appendChild(doc.createTextNode("Service"));		//GOODS_SERVICE_FLAG
											GeneralDescription10.appendChild(doc.createTextNode("0"));			//REV_CHARGE
											GeneralDescription11.appendChild(doc.createTextNode("999799"));		//HSN_SAC
											GeneralDescription12.appendChild(doc.createTextNode(buStateNm));	//POS
											GeneralDescription13.appendChild(doc.createTextNode(tax_amt));		//TAX_AMT
											GeneralDescription14.appendChild(doc.createTextNode(sup_type));		//SUPPLY_TYPE
											GeneralDescription15.appendChild(doc.createTextNode(invoice_amt));	//TOTAL_INV_AMT
										}
									}
								}
							}
							else
							{
								Element Line  = doc.createElement("Line");
								Element AccountCode = doc.createElement("AccountCode");
								Element AccountingPeriod = doc.createElement("AccountingPeriod");
								Element BaseAmount = doc.createElement("BaseAmount");
								Element DebitCredit = doc.createElement("DebitCredit");
								Element TransactionAmount = doc.createElement("TransactionAmount");
								Element Base2ReportingAmount = doc.createElement("Base2ReportingAmount");
								Element CurrencyCode = doc.createElement("CurrencyCode");
								Element CurrencyRate = doc.createElement("CurrencyRate");
								Element TransactionDate = doc.createElement("TransactionDate");
								Element JournalType = doc.createElement("JournalType");
								Element JournalSource = doc.createElement("JournalSource");			//Null in FMS8
								Element TransactionReference = doc.createElement("TransactionReference");
								Element Description = doc.createElement("Description");
								Element DueDate = doc.createElement("DueDate");
								Element AnalysisCode1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
								Element AnalysisCode2 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
								Element AnalysisCode3 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
								Element AnalysisCode4 = doc.createElement("AnalysisCode4");			//COA_CD: 
								Element AnalysisCode5 = doc.createElement("AnalysisCode5");			//Null: in FMS8
								Element AnalysisCode6 = doc.createElement("AnalysisCode6");			
								Element AnalysisCode7 = doc.createElement("AnalysisCode7");			
								Element AnalysisCode8 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
								Element AnalysisCode10 = doc.createElement("AnalysisCode10");			//BU SUN account code 
								Element GeneralDescription9 = doc.createElement("GeneralDescription9");			//Service for LTCORA else NULL 
								Element GeneralDescription10 = doc.createElement("GeneralDescription10");			//REV_CHARGE: in FMS8
								Element GeneralDescription11 = doc.createElement("GeneralDescription11");			//HSN_SAC: in FMS8
								Element GeneralDescription12 = doc.createElement("GeneralDescription12");			//POS: in FMS8
								Element GeneralDescription13 = doc.createElement("GeneralDescription13");			//TAX_AMT: in FMS8 configured for LTCORA
								Element GeneralDescription14 = doc.createElement("GeneralDescription14");			
								Element GeneralDescription15 = doc.createElement("GeneralDescription15");			//TOTAL INV AMT 
								
								Ledger.appendChild(Line);
								Line.appendChild(AccountCode);
								Line.appendChild(AccountingPeriod);
								Line.appendChild(BaseAmount);
								Line.appendChild(DebitCredit);
								Line.appendChild(TransactionAmount);
								Line.appendChild(Base2ReportingAmount);
								Line.appendChild(CurrencyCode);
								Line.appendChild(CurrencyRate);
								Line.appendChild(TransactionDate);
								Line.appendChild(JournalType);
								Line.appendChild(JournalSource);		//Null in FMS8
								Line.appendChild(TransactionReference);
								Line.appendChild(Description);
								Line.appendChild(DueDate);
								Line.appendChild(AnalysisCode1);
								Line.appendChild(AnalysisCode2);		
								Line.appendChild(AnalysisCode3);		
								Line.appendChild(AnalysisCode4);		
								Line.appendChild(AnalysisCode5);		
								Line.appendChild(AnalysisCode6);		
								Line.appendChild(AnalysisCode7);		
								Line.appendChild(AnalysisCode8);		
								Line.appendChild(AnalysisCode10);		
								Line.appendChild(GeneralDescription9);		
								Line.appendChild(GeneralDescription10);		
								Line.appendChild(GeneralDescription11);		
								Line.appendChild(GeneralDescription12);		
								Line.appendChild(GeneralDescription13);		
								Line.appendChild(GeneralDescription14);		
								Line.appendChild(GeneralDescription15);	
								
								AccountCode.appendChild(doc.createTextNode(account_cd));
								AccountingPeriod.appendChild(doc.createTextNode(accountPeriod));
								BaseAmount.appendChild(doc.createTextNode(gross_amt));
								//DebitCredit.appendChild(doc.createTextNode("C"));
								DebitCredit.appendChild(doc.createTextNode(dr_cr_mark));
								TransactionAmount.appendChild(doc.createTextNode(gross_amt));
								Base2ReportingAmount.appendChild(doc.createTextNode(invoice_amt_usd));		
								CurrencyCode.appendChild(doc.createTextNode("INR"));
								CurrencyRate.appendChild(doc.createTextNode(exchg_rate));					
								TransactionDate.appendChild(doc.createTextNode(trans_dt));
								JournalType.appendChild(doc.createTextNode(journal_type));
								TransactionReference.appendChild(doc.createTextNode(invoice_no));
								Description.appendChild(doc.createTextNode(description));
								DueDate.appendChild(doc.createTextNode(dueDt));
								AnalysisCode2.appendChild(doc.createTextNode(cccac));
								AnalysisCode7.appendChild(doc.createTextNode("NA"));
								AnalysisCode10.appendChild(doc.createTextNode(bucac));
								if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
								{
									GeneralDescription9.appendChild(doc.createTextNode("Service"));		//GOODS_SERVICE_FLAG
									GeneralDescription10.appendChild(doc.createTextNode("0"));			//REV_CHARGE
									GeneralDescription11.appendChild(doc.createTextNode("999799"));		//HSN_SAC
									GeneralDescription12.appendChild(doc.createTextNode(buStateNm));	//POS
									GeneralDescription13.appendChild(doc.createTextNode(tax_amt));		//TAX_AMT
									GeneralDescription14.appendChild(doc.createTextNode(sup_type));		//SUPPLY_TYPE
									GeneralDescription15.appendChild(doc.createTextNode(invoice_amt));	//TOTAL_INV_AMT
								}
							}
						}
						
						dr_cr_mark="C";
						if(!inv_flag.equals("ST"))
						{
							if(invoice_type.equals("CR") || invoice_type.equals("CCR") ||inv_flag.equals("CR"))
							{
								dr_cr_mark="D";
							}
							else
							{
								dr_cr_mark="C";
							}
						}
						String queryString1="";
						if(gen_type.equals("SG"))
						{
							queryString1="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=?";
							stmt1=conn.prepareStatement(queryString1);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, fin_year);
							stmt1.setString(3, invoice_seq);
							stmt1.setString(4, bu_state_tin);
							rset1=stmt1.executeQuery();
						}
						else if(gen_type.equals("FFLOW"))
						{
							queryString1="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_FFLOW_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? "
									+ "AND INVOICE_TYPE=?";
							stmt1=conn.prepareStatement(queryString1);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, fin_year);
							stmt1.setString(3, invoice_seq);
							stmt1.setString(4, bu_state_tin);
							stmt1.setString(5, invoice_type);
							rset1=stmt1.executeQuery();
						}
						else if(gen_type.equals("DLNG_SG"))
						{
							queryString1="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT  "
									+ "FROM FMS_DLNG_INV_TAX_DTL  "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? ";
							stmt1=conn.prepareStatement(queryString1);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, fin_year);
							stmt1.setString(3, invoice_seq);
							stmt1.setString(4, bu_state_tin);
							rset1=stmt1.executeQuery();
						}
						else if(gen_type.equals("DLNG_FFLOW"))
						{
							queryString1="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_DLNG_FFLOW_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? "
									+ "AND INVOICE_TYPE=?";
							stmt1=conn.prepareStatement(queryString1);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, fin_year);
							stmt1.setString(3, invoice_seq);
							stmt1.setString(4, bu_state_tin);
							stmt1.setString(5, invoice_type);
							rset1=stmt1.executeQuery();
						}
						else if(gen_type.equals("DLNG_SERV"))
						{
							queryString1="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_DLNG_SVC_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? ";
							stmt1=conn.prepareStatement(queryString1);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, fin_year);
							stmt1.setString(3, invoice_seq);
							stmt1.setString(4, bu_state_tin);
							rset1=stmt1.executeQuery();
						}
						while(rset1.next())
						{
							String tax_code=rset1.getString(1)==null?"":rset1.getString(1);
							String taxStrctCd=rset1.getString(2)==null?"":rset1.getString(2);
							String taxAmt=rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
							String taxBaseAmt=rset1.getString(4)==null?"":nf.format(rset1.getDouble(4));
							
							taxAmt=taxAmt.equals("")?"":nf.format(Double.parseDouble(taxAmt)<0?-1*Double.parseDouble(taxAmt):Double.parseDouble(taxAmt));
							
							String acc_cd = "";
							if(inv_flag.equals("UG"))
							{
								acc_cd=getTaxStructureSunAccountCd(taxStrctCd,tax_code, bu_state_tin, bu_unit,"UG").trim();
							}
							else
							{
								acc_cd=getTaxStructureSunAccountCd(taxStrctCd,tax_code, bu_state_tin, bu_unit,"S").trim();
							}
							
							Element Line1  = doc.createElement("Line");
							Element AccountCode1 = doc.createElement("AccountCode");
							Element AccountingPeriod1 = doc.createElement("AccountingPeriod");
							Element BaseAmount1 = doc.createElement("BaseAmount");
							Element DebitCredit1 = doc.createElement("DebitCredit");
							Element TransactionAmount1 = doc.createElement("TransactionAmount");
							Element Base2ReportingAmount1 = doc.createElement("Base2ReportingAmount");
							Element CurrencyCode1 = doc.createElement("CurrencyCode");
							Element CurrencyRate1 = doc.createElement("CurrencyRate");
							Element TransactionDate1 = doc.createElement("TransactionDate");
							Element JournalType1 = doc.createElement("JournalType");
							Element JournalSource1 = doc.createElement("JournalSource");			//Null in FMS8
							Element TransactionReference1 = doc.createElement("TransactionReference");
							Element Description1 = doc.createElement("Description");
							Element DueDate1 = doc.createElement("DueDate");
							Element AnalysisCode1_1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
							Element AnalysisCode2_1 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
							Element AnalysisCode3_1 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
							Element AnalysisCode4_1 = doc.createElement("AnalysisCode4");			//COA_CD: 
							Element AnalysisCode5_1 = doc.createElement("AnalysisCode5");			//Null: in FMS8
							Element AnalysisCode6_1 = doc.createElement("AnalysisCode6");			
							Element AnalysisCode7_1 = doc.createElement("AnalysisCode7");			 
							Element AnalysisCode8_1 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
							Element AnalysisCode10_1 = doc.createElement("AnalysisCode10");			//BU SUN account code 
							Element GeneralDescription9_1 = doc.createElement("GeneralDescription9");			//Service for LTCORA else NULL 
							Element GeneralDescription10_1 = doc.createElement("GeneralDescription10");			//REV_CHARGE: in FMS8
							Element GeneralDescription11_1 = doc.createElement("GeneralDescription11");			//HSN_SAC: in FMS8
							Element GeneralDescription12_1 = doc.createElement("GeneralDescription12");			//POS: in FMS8
							Element GeneralDescription13_1 = doc.createElement("GeneralDescription13");			//TAX_AMT: in FMS8 configured for LTCORA
							Element GeneralDescription14_1 = doc.createElement("GeneralDescription14");			
							Element GeneralDescription15_1 = doc.createElement("GeneralDescription15");
							
							Ledger.appendChild(Line1);
							Line1.appendChild(AccountCode1);
							Line1.appendChild(AccountingPeriod1);
							Line1.appendChild(BaseAmount1);
							Line1.appendChild(DebitCredit1);
							Line1.appendChild(TransactionAmount1);
							Line1.appendChild(Base2ReportingAmount1);
							Line1.appendChild(CurrencyCode1);
							Line1.appendChild(CurrencyRate1);
							Line1.appendChild(TransactionDate1);
							Line1.appendChild(JournalType1);
							Line1.appendChild(JournalSource1);		//Null in FMS8
							Line1.appendChild(TransactionReference1);
							Line1.appendChild(Description1);
							Line1.appendChild(DueDate1);
							Line1.appendChild(AnalysisCode1_1);
							Line1.appendChild(AnalysisCode2_1);		
							Line1.appendChild(AnalysisCode3_1);		
							Line1.appendChild(AnalysisCode4_1);		
							Line1.appendChild(AnalysisCode5_1);		
							Line1.appendChild(AnalysisCode6_1);		
							Line1.appendChild(AnalysisCode7_1);		
							Line1.appendChild(AnalysisCode8_1);		
							Line1.appendChild(AnalysisCode10_1);		
							Line1.appendChild(GeneralDescription9_1);		
							Line1.appendChild(GeneralDescription10_1);		
							Line1.appendChild(GeneralDescription11_1);		
							Line1.appendChild(GeneralDescription12_1);		
							Line1.appendChild(GeneralDescription13_1);		
							Line1.appendChild(GeneralDescription14_1);		
							Line1.appendChild(GeneralDescription15_1);	
							
							AccountCode1.appendChild(doc.createTextNode(acc_cd));
							AccountingPeriod1.appendChild(doc.createTextNode(accountPeriod));
							BaseAmount1.appendChild(doc.createTextNode(taxAmt));
							if(inv_flag.equals("UG"))
							{
								DebitCredit1.appendChild(doc.createTextNode("C"));
							}
							else
							{
								DebitCredit1.appendChild(doc.createTextNode(dr_cr_mark));
							}
							TransactionAmount1.appendChild(doc.createTextNode(taxAmt));
							Base2ReportingAmount1.appendChild(doc.createTextNode("0.00"));		
							CurrencyCode1.appendChild(doc.createTextNode("INR"));
							if(inv_flag.equals("UG"))
							{
								CurrencyRate1.appendChild(doc.createTextNode("0"));					
							}
							else
							{
								CurrencyRate1.appendChild(doc.createTextNode(exchg_rate));					
							}
							TransactionDate1.appendChild(doc.createTextNode(trans_dt));
							JournalType1.appendChild(doc.createTextNode(journal_type));
							TransactionReference1.appendChild(doc.createTextNode(invoice_no));
							Description1.appendChild(doc.createTextNode(description));
							DueDate1.appendChild(doc.createTextNode(dueDt));
							if(invoice_type.equals("LP"))
							{
								AnalysisCode4_1.appendChild(doc.createTextNode(latePaymentAcc_cd));
							}
							else
							{
								AnalysisCode4_1.appendChild(doc.createTextNode(account_cd));
							}
							AnalysisCode10_1.appendChild(doc.createTextNode(bucac));
							if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
							{
								GeneralDescription9_1.appendChild(doc.createTextNode("Service"));		//GOODS_SERVICE_FLAG
								GeneralDescription10_1.appendChild(doc.createTextNode("0"));			//REV_CHARGE
								GeneralDescription11_1.appendChild(doc.createTextNode("999799"));		//HSN_SAC
								GeneralDescription12_1.appendChild(doc.createTextNode(buStateNm));		//POS
								GeneralDescription13_1.appendChild(doc.createTextNode(tax_amt));		//TAX_AMT
								GeneralDescription14_1.appendChild(doc.createTextNode(sup_type));		//SUPPLY_TYPE
								if(inv_flag.equals("UG"))
								{
									GeneralDescription15_1.appendChild(doc.createTextNode(tax_amt));	//TOTAL_INV_AMT
								}
								else
								{
									GeneralDescription15_1.appendChild(doc.createTextNode(invoice_amt));	//TOTAL_INV_AMT
								}
							}
						}
						rset1.close();
						stmt1.close();
						
						//PB 20250630: 
						//IN SUN System the Invoice amount and the TAX amount are debited into IGX account and credited into customer account 
						if((cont_type.equals("X") || cont_type.equals("W"))&&!criteria.contains("TAXP"))
						{ 
							//Debit first line 
							dr_cr_mark="C";
							for(int i=0;i<2;i++)
							{
								//The amount is credited into customer account 
								Element Line1  = doc.createElement("Line");
								Element AccountCode1 = doc.createElement("AccountCode");
								Element AccountingPeriod1 = doc.createElement("AccountingPeriod");
								Element BaseAmount1 = doc.createElement("BaseAmount");
								Element DebitCredit1 = doc.createElement("DebitCredit");
								Element TransactionAmount1 = doc.createElement("TransactionAmount");
								Element Base2ReportingAmount1 = doc.createElement("Base2ReportingAmount");
								Element CurrencyCode1 = doc.createElement("CurrencyCode");
								Element CurrencyRate1 = doc.createElement("CurrencyRate");
								Element TransactionDate1 = doc.createElement("TransactionDate");
								Element JournalType1 = doc.createElement("JournalType");
								Element JournalSource1 = doc.createElement("JournalSource");			//Null in FMS8
								Element TransactionReference1 = doc.createElement("TransactionReference");
								Element Description1 = doc.createElement("Description");
								Element DueDate1 = doc.createElement("DueDate");
								Element AnalysisCode1_1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
								Element AnalysisCode2_1 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
								Element AnalysisCode3_1 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
								Element AnalysisCode4_1 = doc.createElement("AnalysisCode4");			//COA_CD: 
								Element AnalysisCode5_1 = doc.createElement("AnalysisCode5");			//Null: in FMS8
								Element AnalysisCode6_1 = doc.createElement("AnalysisCode6");			
								Element AnalysisCode7_1 = doc.createElement("AnalysisCode7");			
								Element AnalysisCode8_1 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
								Element AnalysisCode10_1 = doc.createElement("AnalysisCode10");			//BU SUN account code 
								Element GeneralDescription9_1 = doc.createElement("GeneralDescription9");			//Service for LTCORA else NULL 
								Element GeneralDescription10_1 = doc.createElement("GeneralDescription10");			//REV_CHARGE: in FMS8
								Element GeneralDescription11_1 = doc.createElement("GeneralDescription11");			//HSN_SAC: in FMS8
								Element GeneralDescription12_1 = doc.createElement("GeneralDescription12");			//POS: in FMS8
								Element GeneralDescription13_1 = doc.createElement("GeneralDescription13");			//TAX_AMT: in FMS8 configured for LTCORA
								Element GeneralDescription14_1 = doc.createElement("GeneralDescription14");			
								Element GeneralDescription15_1 = doc.createElement("GeneralDescription15");			//TOTAL INV AMT 
								
								Ledger.appendChild(Line1);
								Line1.appendChild(AccountCode1);
								Line1.appendChild(AccountingPeriod1);
								Line1.appendChild(BaseAmount1);
								Line1.appendChild(DebitCredit1);
								Line1.appendChild(TransactionAmount1);
								Line1.appendChild(Base2ReportingAmount1);
								Line1.appendChild(CurrencyCode1);
								Line1.appendChild(CurrencyRate1);
								Line1.appendChild(TransactionDate1);
								Line1.appendChild(JournalType1);
								Line1.appendChild(JournalSource1);		//Null in FMS8
								Line1.appendChild(TransactionReference1);
								Line1.appendChild(Description1);
								Line1.appendChild(DueDate1);
								Line1.appendChild(AnalysisCode1_1);
								Line1.appendChild(AnalysisCode2_1);		
								Line1.appendChild(AnalysisCode3_1);		
								Line1.appendChild(AnalysisCode4_1);		
								Line1.appendChild(AnalysisCode5_1);		
								Line1.appendChild(AnalysisCode6_1);		
								Line1.appendChild(AnalysisCode7_1);		
								Line1.appendChild(AnalysisCode8_1);		
								Line1.appendChild(AnalysisCode10_1);		
								Line1.appendChild(GeneralDescription9_1);		
								Line1.appendChild(GeneralDescription10_1);		
								Line1.appendChild(GeneralDescription11_1);		
								Line1.appendChild(GeneralDescription12_1);		
								Line1.appendChild(GeneralDescription13_1);		
								Line1.appendChild(GeneralDescription14_1);		
								Line1.appendChild(GeneralDescription15_1);	
								
								AccountCode1.appendChild(doc.createTextNode(account_code));
								AccountingPeriod1.appendChild(doc.createTextNode(accountPeriod));
								if(i%2==0)
								{
									BaseAmount1.appendChild(doc.createTextNode(invoice_amt));
								}
								else
								{
									BaseAmount1.appendChild(doc.createTextNode(tax_amt));
								}
								DebitCredit1.appendChild(doc.createTextNode(dr_cr_mark));
								//DebitCredit.appendChild(doc.createTextNode("D"));
								if(i%2==0)
								{
									TransactionAmount1.appendChild(doc.createTextNode(invoice_amt));
								}
								else
								{
									TransactionAmount1.appendChild(doc.createTextNode(tax_amt));
								}
								if(i%2==0)
								{
									Base2ReportingAmount1.appendChild(doc.createTextNode(invoice_amt_usd));		
								}
								else
								{
									Base2ReportingAmount1.appendChild(doc.createTextNode("0.00"));		
								}
								CurrencyCode1.appendChild(doc.createTextNode("INR"));
								CurrencyRate1.appendChild(doc.createTextNode(exchg_rate));					
								TransactionDate1.appendChild(doc.createTextNode(trans_dt));
								JournalType1.appendChild(doc.createTextNode(journal_type));
								TransactionReference1.appendChild(doc.createTextNode(invoice_no));
								Description1.appendChild(doc.createTextNode(description));
								DueDate1.appendChild(doc.createTextNode(dueDt));
								AnalysisCode10_1.appendChild(doc.createTextNode(bucac));
								if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
								{
									GeneralDescription9_1.appendChild(doc.createTextNode("Service"));		//GOODS_SERVICE_FLAG
									GeneralDescription10_1.appendChild(doc.createTextNode("0"));			//REV_CHARGE
									GeneralDescription11_1.appendChild(doc.createTextNode("999799"));		//HSN_SAC
									GeneralDescription12_1.appendChild(doc.createTextNode(buStateNm));	//POS
									GeneralDescription13_1.appendChild(doc.createTextNode(tax_amt));		//TAX_AMT
									GeneralDescription14_1.appendChild(doc.createTextNode(sup_type));		//SUPPLY_TYPE
									GeneralDescription15_1.appendChild(doc.createTextNode(invoice_amt));	//TOTAL_INV_AMT
								}
							}
							
							//Debit the total amount into IGX account 
							dr_cr_mark="D";
							for(int i=0;i<2;i++)
							{
								//The amount is credited into customer account 
								Element Line1  = doc.createElement("Line");
								Element AccountCode1 = doc.createElement("AccountCode");
								Element AccountingPeriod1 = doc.createElement("AccountingPeriod");
								Element BaseAmount1 = doc.createElement("BaseAmount");
								Element DebitCredit1 = doc.createElement("DebitCredit");
								Element TransactionAmount1 = doc.createElement("TransactionAmount");
								Element Base2ReportingAmount1 = doc.createElement("Base2ReportingAmount");
								Element CurrencyCode1 = doc.createElement("CurrencyCode");
								Element CurrencyRate1 = doc.createElement("CurrencyRate");
								Element TransactionDate1 = doc.createElement("TransactionDate");
								Element JournalType1 = doc.createElement("JournalType");
								Element JournalSource1 = doc.createElement("JournalSource");			//Null in FMS8
								Element TransactionReference1 = doc.createElement("TransactionReference");
								Element Description1 = doc.createElement("Description");
								Element DueDate1 = doc.createElement("DueDate");
								Element AnalysisCode1_1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
								Element AnalysisCode2_1 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
								Element AnalysisCode3_1 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
								Element AnalysisCode4_1 = doc.createElement("AnalysisCode4");			//COA_CD: 
								Element AnalysisCode5_1 = doc.createElement("AnalysisCode5");			//Null: in FMS8
								Element AnalysisCode6_1 = doc.createElement("AnalysisCode6");			
								Element AnalysisCode7_1 = doc.createElement("AnalysisCode7");			
								Element AnalysisCode8_1 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
								Element AnalysisCode10_1 = doc.createElement("AnalysisCode10");			//BU SUN account code 
								Element GeneralDescription9_1 = doc.createElement("GeneralDescription9");			//Service for LTCORA else NULL 
								Element GeneralDescription10_1 = doc.createElement("GeneralDescription10");			//REV_CHARGE: in FMS8
								Element GeneralDescription11_1 = doc.createElement("GeneralDescription11");			//HSN_SAC: in FMS8
								Element GeneralDescription12_1 = doc.createElement("GeneralDescription12");			//POS: in FMS8
								Element GeneralDescription13_1 = doc.createElement("GeneralDescription13");			//TAX_AMT: in FMS8 configured for LTCORA
								Element GeneralDescription14_1 = doc.createElement("GeneralDescription14");			
								Element GeneralDescription15_1 = doc.createElement("GeneralDescription15");			//TOTAL INV AMT 
								
								Ledger.appendChild(Line1);
								Line1.appendChild(AccountCode1);
								Line1.appendChild(AccountingPeriod1);
								Line1.appendChild(BaseAmount1);
								Line1.appendChild(DebitCredit1);
								Line1.appendChild(TransactionAmount1);
								Line1.appendChild(Base2ReportingAmount1);
								Line1.appendChild(CurrencyCode1);
								Line1.appendChild(CurrencyRate1);
								Line1.appendChild(TransactionDate1);
								Line1.appendChild(JournalType1);
								Line1.appendChild(JournalSource1);		//Null in FMS8
								Line1.appendChild(TransactionReference1);
								Line1.appendChild(Description1);
								Line1.appendChild(DueDate1);
								Line1.appendChild(AnalysisCode1_1);
								Line1.appendChild(AnalysisCode2_1);		
								Line1.appendChild(AnalysisCode3_1);		
								Line1.appendChild(AnalysisCode4_1);		
								Line1.appendChild(AnalysisCode5_1);		
								Line1.appendChild(AnalysisCode6_1);		
								Line1.appendChild(AnalysisCode7_1);		
								Line1.appendChild(AnalysisCode8_1);		
								Line1.appendChild(AnalysisCode10_1);		
								Line1.appendChild(GeneralDescription9_1);		
								Line1.appendChild(GeneralDescription10_1);		
								Line1.appendChild(GeneralDescription11_1);		
								Line1.appendChild(GeneralDescription12_1);		
								Line1.appendChild(GeneralDescription13_1);		
								Line1.appendChild(GeneralDescription14_1);		
								Line1.appendChild(GeneralDescription15_1);	
								
								AccountCode1.appendChild(doc.createTextNode(igx_account_cd));
								AccountingPeriod1.appendChild(doc.createTextNode(accountPeriod));
								if(i%2==0)
								{
									BaseAmount1.appendChild(doc.createTextNode(invoice_amt));
								}
								else
								{
									BaseAmount1.appendChild(doc.createTextNode(tax_amt));
								}
								DebitCredit1.appendChild(doc.createTextNode(dr_cr_mark));
								//DebitCredit.appendChild(doc.createTextNode("D"));
								if(i%2==0)
								{
									TransactionAmount1.appendChild(doc.createTextNode(invoice_amt));
								}
								else
								{
									TransactionAmount1.appendChild(doc.createTextNode(tax_amt));
								}
								if(i%2==0)
								{
									Base2ReportingAmount1.appendChild(doc.createTextNode(invoice_amt_usd));		
								}
								else
								{
									Base2ReportingAmount1.appendChild(doc.createTextNode("0.00"));		
								}
								CurrencyCode1.appendChild(doc.createTextNode("INR"));
								CurrencyRate1.appendChild(doc.createTextNode(exchg_rate));					
								TransactionDate1.appendChild(doc.createTextNode(trans_dt));
								JournalType1.appendChild(doc.createTextNode(journal_type));
								TransactionReference1.appendChild(doc.createTextNode(invoice_no));
								Description1.appendChild(doc.createTextNode(description));
								DueDate1.appendChild(doc.createTextNode(dueDt));
								if(i%2==0)
								{
									AnalysisCode2_1.appendChild(doc.createTextNode(cccac));
								}
								if(invoice_type.equals("LP"))
								{
									AnalysisCode4_1.appendChild(doc.createTextNode(latePaymentAcc_cd));
								}
								else
								{
									AnalysisCode4_1.appendChild(doc.createTextNode(account_cd));
								}
								if(i%2==0)
								{
									AnalysisCode7_1.appendChild(doc.createTextNode("NA"));
								}
								AnalysisCode10_1.appendChild(doc.createTextNode(bucac));
								if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
								{
									GeneralDescription9_1.appendChild(doc.createTextNode("Service"));		//GOODS_SERVICE_FLAG
									GeneralDescription10_1.appendChild(doc.createTextNode("0"));			//REV_CHARGE
									GeneralDescription11_1.appendChild(doc.createTextNode("999799"));		//HSN_SAC
									GeneralDescription12_1.appendChild(doc.createTextNode(buStateNm));	//POS
									GeneralDescription13_1.appendChild(doc.createTextNode(tax_amt));		//TAX_AMT
									GeneralDescription14_1.appendChild(doc.createTextNode(sup_type));		//SUPPLY_TYPE
									GeneralDescription15_1.appendChild(doc.createTextNode(invoice_amt));	//TOTAL_INV_AMT
								}
							}
						}
						
						if(!invoice_type.equals("LP")&&!invoice_type.equals("CR")&&!invoice_type.equals("DR")&&!invoice_type.equals("CCR")&&!invoice_type.equals("CDR")&&!inv_flag.equals("UG")&&!inv_flag.equals("ST")&&!criteria.contains("TAXP"))
						{
							if(criteria.equals("")||criteria.contains("QTY"))
							{
								String dc_marker="C";
								if(inv_flag.equals("CR"))
								{
									dc_marker="D";
								}
								else
								{
									dc_marker="C";
								}
								//AGMT Base 'Delivery' 
								//if(agmt_base.equals("D"))
								{
									if(!transportationAmt.equals(""))
									{
										for(int i=0;i<2;i++)
										{
											Element Line1  = doc.createElement("Line");
											Element AccountCode1 = doc.createElement("AccountCode");
											Element AccountingPeriod1 = doc.createElement("AccountingPeriod");
											Element BaseAmount1 = doc.createElement("BaseAmount");
											Element DebitCredit1 = doc.createElement("DebitCredit");
											Element TransactionAmount1 = doc.createElement("TransactionAmount");
											Element Base2ReportingAmount1 = doc.createElement("Base2ReportingAmount");
											Element CurrencyCode1 = doc.createElement("CurrencyCode");
											Element CurrencyRate1 = doc.createElement("CurrencyRate");
											Element TransactionDate1 = doc.createElement("TransactionDate");
											Element JournalType1 = doc.createElement("JournalType");
											Element JournalSource1 = doc.createElement("JournalSource");			//Null in FMS8
											Element TransactionReference1 = doc.createElement("TransactionReference");
											Element Description1 = doc.createElement("Description");
											Element DueDate1 = doc.createElement("DueDate");
											Element AnalysisCode1_1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
											Element AnalysisCode2_1 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
											Element AnalysisCode3_1 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
											Element AnalysisCode4_1 = doc.createElement("AnalysisCode4");			//COA_CD: 
											Element AnalysisCode5_1 = doc.createElement("AnalysisCode5");			//Null: in FMS8
											Element AnalysisCode6_1 = doc.createElement("AnalysisCode6");			
											Element AnalysisCode7_1 = doc.createElement("AnalysisCode7");			
											Element AnalysisCode8_1 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
											Element AnalysisCode10_1 = doc.createElement("AnalysisCode10");			//BU SUN account code 
											Element GeneralDescription9_1 = doc.createElement("GeneralDescription9");			//Service for LTCORA else NULL 
											Element GeneralDescription10_1 = doc.createElement("GeneralDescription10");			//REV_CHARGE: in FMS8
											Element GeneralDescription11_1 = doc.createElement("GeneralDescription11");			//HSN_SAC: in FMS8
											Element GeneralDescription12_1 = doc.createElement("GeneralDescription12");			//POS: in FMS8
											Element GeneralDescription13_1 = doc.createElement("GeneralDescription13");			//TAX_AMT: in FMS8 configured for LTCORA
											Element GeneralDescription14_1 = doc.createElement("GeneralDescription14");			//
											Element GeneralDescription15_1 = doc.createElement("GeneralDescription15");
											
											Ledger.appendChild(Line1);
											Line1.appendChild(AccountCode1);
											Line1.appendChild(AccountingPeriod1);
											Line1.appendChild(BaseAmount1);
											Line1.appendChild(DebitCredit1);
											Line1.appendChild(TransactionAmount1);
											Line1.appendChild(Base2ReportingAmount1);
											Line1.appendChild(CurrencyCode1);
											Line1.appendChild(CurrencyRate1);
											Line1.appendChild(TransactionDate1);
											Line1.appendChild(JournalType1);
											Line1.appendChild(JournalSource1);		//Null in FMS8
											Line1.appendChild(TransactionReference1);
											Line1.appendChild(Description1);
											Line1.appendChild(DueDate1);
											Line1.appendChild(AnalysisCode1_1);
											Line1.appendChild(AnalysisCode2_1);		
											Line1.appendChild(AnalysisCode3_1);		
											Line1.appendChild(AnalysisCode4_1);		
											Line1.appendChild(AnalysisCode5_1);		
											Line1.appendChild(AnalysisCode6_1);		
											Line1.appendChild(AnalysisCode7_1);		
											Line1.appendChild(AnalysisCode8_1);		
											Line1.appendChild(AnalysisCode10_1);		
											Line1.appendChild(GeneralDescription9_1);		
											Line1.appendChild(GeneralDescription10_1);		
											Line1.appendChild(GeneralDescription11_1);		
											Line1.appendChild(GeneralDescription12_1);		
											Line1.appendChild(GeneralDescription13_1);		
											Line1.appendChild(GeneralDescription14_1);		
											Line1.appendChild(GeneralDescription15_1);	
											
											if(i%2==0)
											{
												AccountCode1.appendChild(doc.createTextNode(trans_account_cd));
											}
											else
											{
												AccountCode1.appendChild(doc.createTextNode(account_code));
											}
											AccountingPeriod1.appendChild(doc.createTextNode(accountPeriod));
											BaseAmount1.appendChild(doc.createTextNode(nf.format(Math.abs(Double.parseDouble(transportationAmt)))));
											if(i%2==0)
											{
												DebitCredit1.appendChild(doc.createTextNode("C"));
											}
											else
											{
												DebitCredit1.appendChild(doc.createTextNode("D"));
											}
											TransactionAmount1.appendChild(doc.createTextNode(nf.format(Math.abs(Double.parseDouble(transportationAmt)))));
											Base2ReportingAmount1.appendChild(doc.createTextNode("0.00"));		
											CurrencyCode1.appendChild(doc.createTextNode("INR"));
											CurrencyRate1.appendChild(doc.createTextNode(exchg_rate));					
											TransactionDate1.appendChild(doc.createTextNode(trans_dt));
											JournalType1.appendChild(doc.createTextNode(journal_type));
											TransactionReference1.appendChild(doc.createTextNode(invoice_no));
											Description1.appendChild(doc.createTextNode(description));
											DueDate1.appendChild(doc.createTextNode(dueDt));
											if(i%2==0)
											{
												AnalysisCode2_1.appendChild(doc.createTextNode(cccac));
												
											}
											AnalysisCode4_1.appendChild(doc.createTextNode(account_cd));
											AnalysisCode10_1.appendChild(doc.createTextNode(bucac));
											if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
											{
												GeneralDescription9_1.appendChild(doc.createTextNode("Service"));		//GOODS_SERVICE_FLAG
												GeneralDescription10_1.appendChild(doc.createTextNode("0"));			//REV_CHARGE
												GeneralDescription11_1.appendChild(doc.createTextNode("999799"));		//HSN_SAC
												GeneralDescription12_1.appendChild(doc.createTextNode(buStateNm));		//POS
												GeneralDescription13_1.appendChild(doc.createTextNode(tax_amt));		//TAX_AMT
												GeneralDescription14_1.appendChild(doc.createTextNode(sup_type));		//SUPPLY_TYPE
												GeneralDescription15_1.appendChild(doc.createTextNode(invoice_amt));	//TOTAL_INV_AMT
											}
										}
									}
								}
							}
							
						}
						
						//For TCS
						if(!tcs_tds.equals("") && !criteria.contains("TAXP"))
						{
							if(tcs_tds.equals("TCS") && !inv_flag.equals("CR") && !inv_flag.equals("DR"))
							{
								String ac_cd = getTcsTdsStructureSunAccountCd(tcsStructCd, bu_state_tin, bu_unit).trim();
								for(int i=0;i<2;i++)
								{
									Element Line1  = doc.createElement("Line");
									Element AccountCode1 = doc.createElement("AccountCode");
									Element AccountingPeriod1 = doc.createElement("AccountingPeriod");
									Element BaseAmount1 = doc.createElement("BaseAmount");
									Element DebitCredit1 = doc.createElement("DebitCredit");
									Element TransactionAmount1 = doc.createElement("TransactionAmount");
									Element Base2ReportingAmount1 = doc.createElement("Base2ReportingAmount");
									Element CurrencyCode1 = doc.createElement("CurrencyCode");
									Element CurrencyRate1 = doc.createElement("CurrencyRate");
									Element TransactionDate1 = doc.createElement("TransactionDate");
									Element JournalType1 = doc.createElement("JournalType");
									Element JournalSource1 = doc.createElement("JournalSource");			//Null in FMS8
									Element TransactionReference1 = doc.createElement("TransactionReference");
									Element Description1 = doc.createElement("Description");
									Element DueDate1 = doc.createElement("DueDate");
									Element AnalysisCode1_1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
									Element AnalysisCode2_1 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
									Element AnalysisCode3_1 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
									Element AnalysisCode4_1 = doc.createElement("AnalysisCode4");			//COA_CD: 
									Element AnalysisCode5_1 = doc.createElement("AnalysisCode5");			//Null: in FMS8
									Element AnalysisCode6_1 = doc.createElement("AnalysisCode6");			
									Element AnalysisCode7_1 = doc.createElement("AnalysisCode7");			
									Element AnalysisCode8_1 = doc.createElement("AnalysisCode8");						//NULL: in FMS8
									Element AnalysisCode10_1 = doc.createElement("AnalysisCode10");						//BU SUN account code 
									Element GeneralDescription9_1 = doc.createElement("GeneralDescription9");			//Service for LTCORA else NULL 
									Element GeneralDescription10_1 = doc.createElement("GeneralDescription10");			//REV_CHARGE: in FMS8
									Element GeneralDescription11_1 = doc.createElement("GeneralDescription11");			//HSN_SAC: in FMS8
									Element GeneralDescription12_1 = doc.createElement("GeneralDescription12");			//POS: in FMS8
									Element GeneralDescription13_1 = doc.createElement("GeneralDescription13");			//TAX_AMT: in FMS8 configured for LTCORA
									Element GeneralDescription14_1 = doc.createElement("GeneralDescription14");			
									Element GeneralDescription15_1 = doc.createElement("GeneralDescription15");
									
									Ledger.appendChild(Line1);
									Line1.appendChild(AccountCode1);
									Line1.appendChild(AccountingPeriod1);
									Line1.appendChild(BaseAmount1);
									Line1.appendChild(DebitCredit1);
									Line1.appendChild(TransactionAmount1);
									Line1.appendChild(Base2ReportingAmount1);
									Line1.appendChild(CurrencyCode1);
									Line1.appendChild(CurrencyRate1);
									Line1.appendChild(TransactionDate1);
									Line1.appendChild(JournalType1);
									Line1.appendChild(JournalSource1);		//Null in FMS8
									Line1.appendChild(TransactionReference1);
									Line1.appendChild(Description1);
									Line1.appendChild(DueDate1);
									Line1.appendChild(AnalysisCode1_1);
									Line1.appendChild(AnalysisCode2_1);		
									Line1.appendChild(AnalysisCode3_1);		
									Line1.appendChild(AnalysisCode4_1);		
									Line1.appendChild(AnalysisCode5_1);		
									Line1.appendChild(AnalysisCode6_1);		
									Line1.appendChild(AnalysisCode7_1);		
									Line1.appendChild(AnalysisCode8_1);		
									Line1.appendChild(AnalysisCode10_1);		
									Line1.appendChild(GeneralDescription9_1);		
									Line1.appendChild(GeneralDescription10_1);		
									Line1.appendChild(GeneralDescription11_1);		
									Line1.appendChild(GeneralDescription12_1);		
									Line1.appendChild(GeneralDescription13_1);		
									Line1.appendChild(GeneralDescription14_1);		
									Line1.appendChild(GeneralDescription15_1);	
									
									if(i%2==0)
									{
										AccountCode1.appendChild(doc.createTextNode(ac_cd));
									}
									else
									{
										AccountCode1.appendChild(doc.createTextNode(account_code));
									}
									AccountingPeriod1.appendChild(doc.createTextNode(accountPeriod));
									BaseAmount1.appendChild(doc.createTextNode(tcs_amt));
									if(i%2==0)
									{
										DebitCredit1.appendChild(doc.createTextNode("C"));
									}
									else
									{
										DebitCredit1.appendChild(doc.createTextNode("D"));
									}
									TransactionAmount1.appendChild(doc.createTextNode(tcs_amt));
									Base2ReportingAmount1.appendChild(doc.createTextNode("0.00"));		
									CurrencyCode1.appendChild(doc.createTextNode("INR"));
									CurrencyRate1.appendChild(doc.createTextNode(exchg_rate));					
									TransactionDate1.appendChild(doc.createTextNode(trans_dt));
									JournalType1.appendChild(doc.createTextNode(journal_type));
									TransactionReference1.appendChild(doc.createTextNode(invoice_no));
									Description1.appendChild(doc.createTextNode(description));
									DueDate1.appendChild(doc.createTextNode(dueDt));
									if(invoice_type.equals("LP"))
									{
										AnalysisCode4_1.appendChild(doc.createTextNode(latePaymentAcc_cd));
									}
									else
									{
										AnalysisCode4_1.appendChild(doc.createTextNode(account_cd));
									}
									AnalysisCode10_1.appendChild(doc.createTextNode(bucac));
									if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
									{
										GeneralDescription9_1.appendChild(doc.createTextNode("Service"));		//GOODS_SERVICE_FLAG
										GeneralDescription10_1.appendChild(doc.createTextNode("0"));			//REV_CHARGE
										GeneralDescription11_1.appendChild(doc.createTextNode("999799"));		//HSN_SAC
										GeneralDescription12_1.appendChild(doc.createTextNode(buStateNm));		//POS
										GeneralDescription13_1.appendChild(doc.createTextNode(tax_amt));		//TAX_AMT
										GeneralDescription14_1.appendChild(doc.createTextNode(sup_type));		//SUPPLY_TYPE
										GeneralDescription15_1.appendChild(doc.createTextNode(invoice_amt));	//TOTAL_INV_AMT
									}
									
								}
							}
							//PB20250619: As TDS line is not shown in SUN XML 
							/*
					else if(tcs_tds.equals("TDS"))
			    	{
						String ac_cd = getTcsTdsStructureSunAccountCd(tdsStructCd, bu_state_tin, bu_unit);
						for(int i=0;i<2;i++)
						{
							Element Line1  = doc.createElement("Line");
							Element AccountCode1 = doc.createElement("AccountCode");
							Element AccountingPeriod1 = doc.createElement("AccountingPeriod");
							Element BaseAmount1 = doc.createElement("BaseAmount");
							Element DebitCredit1 = doc.createElement("DebitCredit");
							Element TransactionAmount1 = doc.createElement("TransactionAmount");
							Element Base2ReportingAmount1 = doc.createElement("Base2ReportingAmount");
							Element CurrencyCode1 = doc.createElement("CurrencyCode");
							Element CurrencyRate1 = doc.createElement("CurrencyRate");
							Element TransactionDate1 = doc.createElement("TransactionDate");
							Element JournalType1 = doc.createElement("JournalType");
							Element JournalSource1 = doc.createElement("JournalSource");			//Null in FMS8
							Element TransactionReference1 = doc.createElement("TransactionReference");
							Element Description1 = doc.createElement("Description");
							Element DueDate1 = doc.createElement("DueDate");
							Element AnalysisCode1_1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
							Element AnalysisCode2_1 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD
							Element AnalysisCode3_1 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
							Element AnalysisCode4_1 = doc.createElement("AnalysisCode4");			//COA_CD 
							Element AnalysisCode5_1 = doc.createElement("AnalysisCode5");			//Null: in FMS8
							Element AnalysisCode6_1 = doc.createElement("AnalysisCode6");			
							Element AnalysisCode7_1 = doc.createElement("AnalysisCode7");			
							Element AnalysisCode8_1 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
							Element AnalysisCode10_1 = doc.createElement("AnalysisCode10");			//BU SUN account code 
							Element GeneralDescription9_1 = doc.createElement("GeneralDescription9");			//Service for LTCORA else NULL 
							Element GeneralDescription10_1 = doc.createElement("GeneralDescription10");			//REV_CHARGE: in FMS8
							Element GeneralDescription11_1 = doc.createElement("GeneralDescription11");			//HSN_SAC: in FMS8
							Element GeneralDescription12_1 = doc.createElement("GeneralDescription12");			//POS: in FMS8
							Element GeneralDescription13_1 = doc.createElement("GeneralDescription13");			//TAX_AMT: in FMS8 configured for LTCORA
							Element GeneralDescription14_1 = doc.createElement("GeneralDescription14");			
							Element GeneralDescription15_1 = doc.createElement("GeneralDescription15");
							
							Ledger.appendChild(Line1);
							Line1.appendChild(AccountCode1);
							Line1.appendChild(AccountingPeriod1);
							Line1.appendChild(BaseAmount1);
							Line1.appendChild(DebitCredit1);
							Line1.appendChild(TransactionAmount1);
							Line1.appendChild(Base2ReportingAmount1);
							Line1.appendChild(CurrencyCode1);
							Line1.appendChild(CurrencyRate1);
							Line1.appendChild(TransactionDate1);
							Line1.appendChild(JournalType1);
							Line1.appendChild(JournalSource1);		//Null in FMS8
							Line1.appendChild(TransactionReference1);
							Line1.appendChild(Description1);
							Line1.appendChild(DueDate1);
							Line1.appendChild(AnalysisCode1_1);
							Line1.appendChild(AnalysisCode2_1);		
							Line1.appendChild(AnalysisCode3_1);		
							Line1.appendChild(AnalysisCode4_1);		
							Line1.appendChild(AnalysisCode5_1);		
							Line1.appendChild(AnalysisCode6_1);		
							Line1.appendChild(AnalysisCode7_1);		
							Line1.appendChild(AnalysisCode8_1);		
							Line1.appendChild(AnalysisCode10_1);		
							Line1.appendChild(GeneralDescription9_1);		
							Line1.appendChild(GeneralDescription10_1);		
							Line1.appendChild(GeneralDescription11_1);		
							Line1.appendChild(GeneralDescription12_1);		
							Line1.appendChild(GeneralDescription13_1);		
							Line1.appendChild(GeneralDescription14_1);		
							Line1.appendChild(GeneralDescription15_1);	
							
							if(i%2==0)
							{
								AccountCode1.appendChild(doc.createTextNode(ac_cd));
							}
							else
							{
								AccountCode1.appendChild(doc.createTextNode(account_code));
							}
							AccountingPeriod1.appendChild(doc.createTextNode(accountPeriod));
							BaseAmount1.appendChild(doc.createTextNode(tds_amt));
							if(i%2==0)
							{
								DebitCredit1.appendChild(doc.createTextNode("C"));
							}
							else
							{
								DebitCredit1.appendChild(doc.createTextNode("D"));
							}
							TransactionAmount1.appendChild(doc.createTextNode(tds_amt));
							Base2ReportingAmount1.appendChild(doc.createTextNode("0.00"));		
							CurrencyCode1.appendChild(doc.createTextNode("INR"));
							CurrencyRate1.appendChild(doc.createTextNode(exchg_rate));					
							TransactionDate1.appendChild(doc.createTextNode(trans_dt));
							JournalType1.appendChild(doc.createTextNode(journal_type));
							TransactionReference1.appendChild(doc.createTextNode(invoice_no));
							Description1.appendChild(doc.createTextNode(description));
							DueDate1.appendChild(doc.createTextNode(dueDt));
							AnalysisCode4_1.appendChild(doc.createTextNode(account_cd));
							AnalysisCode10_1.appendChild(doc.createTextNode(bucac));
							if(cont_type.equals("O") || cont_type.equals("Q"))
							{
								GeneralDescription9_1.appendChild(doc.createTextNode("Service"));		//GOODS_SERVICE_FLAG
								GeneralDescription10_1.appendChild(doc.createTextNode("0"));			//REV_CHARGE
								GeneralDescription11_1.appendChild(doc.createTextNode("999799"));		//HSN_SAC
								GeneralDescription12_1.appendChild(doc.createTextNode(buStateNm));		//POS
								GeneralDescription13_1.appendChild(doc.createTextNode(tax_amt));		//TAX_AMT
								GeneralDescription14_1.appendChild(doc.createTextNode(sup_type));		//SUPPLY_TYPE
								GeneralDescription15_1.appendChild(doc.createTextNode(gross_amt));		//TOTAL_INV_AMT
							}
						}
			    	}*/
						}
						
						if(!invoice_type.equals("LP")&&!invoice_type.equals("CR")&&!invoice_type.equals("DR")&&!invoice_type.equals("CCR")&&!invoice_type.equals("CDR")&&!inv_flag.equals("UG")&&!inv_flag.equals("ST") && !inv_flag.equals("CR") && !inv_flag.equals("DR") &&!criteria.contains("TAXP"))
						{
							//for marketing margin amt
							if(!market_margin_amt.equals(""))
							{
								//String acc_cd=getCounterpartySunAccountCode(counterpty_cd, "C", "0","S").trim();
								
								Element Line1  = doc.createElement("Line");
								Element AccountCode1 = doc.createElement("AccountCode");
								Element AccountingPeriod1 = doc.createElement("AccountingPeriod");
								Element BaseAmount1 = doc.createElement("BaseAmount");
								Element DebitCredit1 = doc.createElement("DebitCredit");
								Element TransactionAmount1 = doc.createElement("TransactionAmount");
								Element Base2ReportingAmount1 = doc.createElement("Base2ReportingAmount");
								Element CurrencyCode1 = doc.createElement("CurrencyCode");
								Element CurrencyRate1 = doc.createElement("CurrencyRate");
								Element TransactionDate1 = doc.createElement("TransactionDate");
								Element JournalType1 = doc.createElement("JournalType");
								Element JournalSource1 = doc.createElement("JournalSource");			//Null in FMS8
								Element TransactionReference1 = doc.createElement("TransactionReference");
								Element Description1 = doc.createElement("Description");
								Element DueDate1 = doc.createElement("DueDate");
								Element AnalysisCode1_1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
								Element AnalysisCode2_1 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
								Element AnalysisCode3_1 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
								Element AnalysisCode4_1 = doc.createElement("AnalysisCode4");			//COA_CD: 
								Element AnalysisCode5_1 = doc.createElement("AnalysisCode5");			//Null: in FMS8
								Element AnalysisCode6_1 = doc.createElement("AnalysisCode6");			
								Element AnalysisCode7_1 = doc.createElement("AnalysisCode7");			
								Element AnalysisCode8_1 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
								Element AnalysisCode10_1 = doc.createElement("AnalysisCode10");			//BU SUN account code 
								Element GeneralDescription9_1 = doc.createElement("GeneralDescription9");			//Service for LTCORA else NULL 
								Element GeneralDescription10_1 = doc.createElement("GeneralDescription10");			//REV_CHARGE: in FMS8
								Element GeneralDescription11_1 = doc.createElement("GeneralDescription11");			//HSN_SAC: in FMS8
								Element GeneralDescription12_1 = doc.createElement("GeneralDescription12");			//POS: in FMS8
								Element GeneralDescription13_1 = doc.createElement("GeneralDescription13");			//TAX_AMT: in FMS8 configured for LTCORA
								Element GeneralDescription14_1 = doc.createElement("GeneralDescription14");			//
								Element GeneralDescription15_1 = doc.createElement("GeneralDescription15");
								
								Ledger.appendChild(Line1);
								Line1.appendChild(AccountCode1);
								Line1.appendChild(AccountingPeriod1);
								Line1.appendChild(BaseAmount1);
								Line1.appendChild(DebitCredit1);
								Line1.appendChild(TransactionAmount1);
								Line1.appendChild(Base2ReportingAmount1);
								Line1.appendChild(CurrencyCode1);
								Line1.appendChild(CurrencyRate1);
								Line1.appendChild(TransactionDate1);
								Line1.appendChild(JournalType1);
								Line1.appendChild(JournalSource1);		//Null in FMS8
								Line1.appendChild(TransactionReference1);
								Line1.appendChild(Description1);
								Line1.appendChild(DueDate1);
								Line1.appendChild(AnalysisCode1_1);
								Line1.appendChild(AnalysisCode2_1);		
								Line1.appendChild(AnalysisCode3_1);		
								Line1.appendChild(AnalysisCode4_1);		
								Line1.appendChild(AnalysisCode5_1);		
								Line1.appendChild(AnalysisCode6_1);		
								Line1.appendChild(AnalysisCode7_1);		
								Line1.appendChild(AnalysisCode8_1);		
								Line1.appendChild(AnalysisCode10_1);		
								Line1.appendChild(GeneralDescription9_1);		
								Line1.appendChild(GeneralDescription10_1);		
								Line1.appendChild(GeneralDescription11_1);		
								Line1.appendChild(GeneralDescription12_1);		
								Line1.appendChild(GeneralDescription13_1);		
								Line1.appendChild(GeneralDescription14_1);		
								Line1.appendChild(GeneralDescription15_1);	
								
								AccountCode1.appendChild(doc.createTextNode(account_cd));		
								AccountingPeriod1.appendChild(doc.createTextNode(accountPeriod));
								BaseAmount1.appendChild(doc.createTextNode(nf.format(Math.abs(Double.parseDouble(market_margin_amt)))));
								DebitCredit1.appendChild(doc.createTextNode("C"));
								TransactionAmount1.appendChild(doc.createTextNode(nf.format(Math.abs(Double.parseDouble(market_margin_amt)))));
								Base2ReportingAmount1.appendChild(doc.createTextNode("0.00"));		
								CurrencyCode1.appendChild(doc.createTextNode("INR"));
								CurrencyRate1.appendChild(doc.createTextNode(exchg_rate));					
								TransactionDate1.appendChild(doc.createTextNode(trans_dt));
								JournalType1.appendChild(doc.createTextNode(journal_type));
								TransactionReference1.appendChild(doc.createTextNode(invoice_no));
								Description1.appendChild(doc.createTextNode(description));
								DueDate1.appendChild(doc.createTextNode(dueDt));
								AnalysisCode2_1.appendChild(doc.createTextNode(cccac));
								AnalysisCode4_1.appendChild(doc.createTextNode(account_cd));
								AnalysisCode10_1.appendChild(doc.createTextNode(bucac));
								if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
								{
									GeneralDescription9_1.appendChild(doc.createTextNode("Service"));		//GOODS_SERVICE_FLAG
									GeneralDescription10_1.appendChild(doc.createTextNode("0"));			//REV_CHARGE
									GeneralDescription11_1.appendChild(doc.createTextNode("999799"));		//HSN_SAC
									GeneralDescription12_1.appendChild(doc.createTextNode(buStateNm));		//POS
									GeneralDescription13_1.appendChild(doc.createTextNode(tax_amt));		//TAX_AMT
									GeneralDescription14_1.appendChild(doc.createTextNode(sup_type));		//SUPPLY_TYPE
									GeneralDescription15_1.appendChild(doc.createTextNode(invoice_amt));	//TOTAL_INV_AMT
								}
							}
							//for other charges 
							if(!oth_amt.equals(""))
							{
								//String acc_cd=getCounterpartySunAccountCode(counterpty_cd, "C", "0","S").trim();
								Element Line1  = doc.createElement("Line");
								Element AccountCode1 = doc.createElement("AccountCode");
								Element AccountingPeriod1 = doc.createElement("AccountingPeriod");
								Element BaseAmount1 = doc.createElement("BaseAmount");
								Element DebitCredit1 = doc.createElement("DebitCredit");
								Element TransactionAmount1 = doc.createElement("TransactionAmount");
								Element Base2ReportingAmount1 = doc.createElement("Base2ReportingAmount");
								Element CurrencyCode1 = doc.createElement("CurrencyCode");
								Element CurrencyRate1 = doc.createElement("CurrencyRate");
								Element TransactionDate1 = doc.createElement("TransactionDate");
								Element JournalType1 = doc.createElement("JournalType");
								Element JournalSource1 = doc.createElement("JournalSource");			//Null in FMS8
								Element TransactionReference1 = doc.createElement("TransactionReference");
								Element Description1 = doc.createElement("Description");
								Element DueDate1 = doc.createElement("DueDate");
								Element AnalysisCode1_1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
								Element AnalysisCode2_1 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
								Element AnalysisCode3_1 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
								Element AnalysisCode4_1 = doc.createElement("AnalysisCode4");			//COA_CD: 
								Element AnalysisCode5_1 = doc.createElement("AnalysisCode5");			//Null: in FMS8
								Element AnalysisCode6_1 = doc.createElement("AnalysisCode6");			
								Element AnalysisCode7_1 = doc.createElement("AnalysisCode7");			
								Element AnalysisCode8_1 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
								Element AnalysisCode10_1 = doc.createElement("AnalysisCode10");			//BU SUN account code 
								Element GeneralDescription9_1 = doc.createElement("GeneralDescription9");			//Service for LTCORA else NULL 
								Element GeneralDescription10_1 = doc.createElement("GeneralDescription10");			//REV_CHARGE: in FMS8
								Element GeneralDescription11_1 = doc.createElement("GeneralDescription11");			//HSN_SAC: in FMS8
								Element GeneralDescription12_1 = doc.createElement("GeneralDescription12");			//POS: in FMS8
								Element GeneralDescription13_1 = doc.createElement("GeneralDescription13");			//TAX_AMT: in FMS8 configured for LTCORA
								Element GeneralDescription14_1 = doc.createElement("GeneralDescription14");			//
								Element GeneralDescription15_1 = doc.createElement("GeneralDescription15");
								
								Ledger.appendChild(Line1);
								Line1.appendChild(AccountCode1);
								Line1.appendChild(AccountingPeriod1);
								Line1.appendChild(BaseAmount1);
								Line1.appendChild(DebitCredit1);
								Line1.appendChild(TransactionAmount1);
								Line1.appendChild(Base2ReportingAmount1);
								Line1.appendChild(CurrencyCode1);
								Line1.appendChild(CurrencyRate1);
								Line1.appendChild(TransactionDate1);
								Line1.appendChild(JournalType1);
								Line1.appendChild(JournalSource1);		//Null in FMS8
								Line1.appendChild(TransactionReference1);
								Line1.appendChild(Description1);
								Line1.appendChild(DueDate1);
								Line1.appendChild(AnalysisCode1_1);
								Line1.appendChild(AnalysisCode2_1);		
								Line1.appendChild(AnalysisCode3_1);		
								Line1.appendChild(AnalysisCode4_1);		
								Line1.appendChild(AnalysisCode5_1);		
								Line1.appendChild(AnalysisCode6_1);		
								Line1.appendChild(AnalysisCode7_1);		
								Line1.appendChild(AnalysisCode8_1);		
								Line1.appendChild(AnalysisCode10_1);		
								Line1.appendChild(GeneralDescription9_1);		
								Line1.appendChild(GeneralDescription10_1);		
								Line1.appendChild(GeneralDescription11_1);		
								Line1.appendChild(GeneralDescription12_1);		
								Line1.appendChild(GeneralDescription13_1);		
								Line1.appendChild(GeneralDescription14_1);		
								Line1.appendChild(GeneralDescription15_1);	
								
								AccountCode1.appendChild(doc.createTextNode(account_cd));		 
								AccountingPeriod1.appendChild(doc.createTextNode(accountPeriod));
								BaseAmount1.appendChild(doc.createTextNode(nf.format(Math.abs(Double.parseDouble(oth_amt)))));
								DebitCredit1.appendChild(doc.createTextNode("C"));
								TransactionAmount1.appendChild(doc.createTextNode(nf.format(Math.abs(Double.parseDouble(oth_amt)))));
								Base2ReportingAmount1.appendChild(doc.createTextNode("0.00"));		
								CurrencyCode1.appendChild(doc.createTextNode("INR"));
								CurrencyRate1.appendChild(doc.createTextNode(exchg_rate));					
								TransactionDate1.appendChild(doc.createTextNode(trans_dt));
								JournalType1.appendChild(doc.createTextNode(journal_type));
								TransactionReference1.appendChild(doc.createTextNode(invoice_no));
								Description1.appendChild(doc.createTextNode(description));
								DueDate1.appendChild(doc.createTextNode(dueDt));
								AnalysisCode2_1.appendChild(doc.createTextNode(cccac));
								AnalysisCode4_1.appendChild(doc.createTextNode(account_cd));
								AnalysisCode10_1.appendChild(doc.createTextNode(bucac));
								if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
								{
									GeneralDescription9_1.appendChild(doc.createTextNode("Service"));		//GOODS_SERVICE_FLAG
									GeneralDescription10_1.appendChild(doc.createTextNode("0"));			//REV_CHARGE
									GeneralDescription11_1.appendChild(doc.createTextNode("999799"));		//HSN_SAC
									GeneralDescription12_1.appendChild(doc.createTextNode(buStateNm));		//POS
									GeneralDescription13_1.appendChild(doc.createTextNode(tax_amt));		//TAX_AMT
									GeneralDescription14_1.appendChild(doc.createTextNode(sup_type));		//SUPPLY_TYPE
									GeneralDescription15_1.appendChild(doc.createTextNode(invoice_amt));		//TOTAL_INV_AMT
								}
							}
						}
						
						//Additional Line for SUG 
						if(inv_flag.equals("UG"))
						{
							String acc_cd=getCounterpartySunAccountCode(counterpty_cd, "C", "0","UG").trim();
							if(!acc_cd.equals(""))
							{
								for(int i=0;i<2;i++)
								{
									Element Line  = doc.createElement("Line");
									Element AccountCode = doc.createElement("AccountCode");
									Element AccountingPeriod = doc.createElement("AccountingPeriod");
									Element BaseAmount = doc.createElement("BaseAmount");
									Element DebitCredit = doc.createElement("DebitCredit");
									Element TransactionAmount = doc.createElement("TransactionAmount");
									Element Base2ReportingAmount = doc.createElement("Base2ReportingAmount");
									Element CurrencyCode = doc.createElement("CurrencyCode");
									Element CurrencyRate = doc.createElement("CurrencyRate");
									Element TransactionDate = doc.createElement("TransactionDate");
									Element JournalType = doc.createElement("JournalType");
									Element JournalSource = doc.createElement("JournalSource");			//Null in FMS8
									Element TransactionReference = doc.createElement("TransactionReference");
									Element Description = doc.createElement("Description");
									Element DueDate = doc.createElement("DueDate");
									Element AnalysisCode1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
									Element AnalysisCode2 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
									Element AnalysisCode3 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
									Element AnalysisCode4 = doc.createElement("AnalysisCode4");			//COA_CD: 
									Element AnalysisCode5 = doc.createElement("AnalysisCode5");			//Null: in FMS8
									Element AnalysisCode6 = doc.createElement("AnalysisCode6");			
									Element AnalysisCode7 = doc.createElement("AnalysisCode7");			
									Element AnalysisCode8 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
									Element AnalysisCode10 = doc.createElement("AnalysisCode10");			//BU SUN account code 
									Element GeneralDescription9 = doc.createElement("GeneralDescription9");			//Service for LTCORA else NULL 
									Element GeneralDescription10 = doc.createElement("GeneralDescription10");			//REV_CHARGE: in FMS8
									Element GeneralDescription11 = doc.createElement("GeneralDescription11");			//HSN_SAC: in FMS8
									Element GeneralDescription12 = doc.createElement("GeneralDescription12");			//POS: in FMS8
									Element GeneralDescription13 = doc.createElement("GeneralDescription13");			//TAX_AMT: in FMS8 configured for LTCORA
									Element GeneralDescription14 = doc.createElement("GeneralDescription14");			
									Element GeneralDescription15 = doc.createElement("GeneralDescription15");			//TOTAL INV AMT 
									
									Ledger.appendChild(Line);
									Line.appendChild(AccountCode);
									Line.appendChild(AccountingPeriod);
									Line.appendChild(BaseAmount);
									Line.appendChild(DebitCredit);
									Line.appendChild(TransactionAmount);
									Line.appendChild(Base2ReportingAmount);
									Line.appendChild(CurrencyCode);
									Line.appendChild(CurrencyRate);
									Line.appendChild(TransactionDate);
									Line.appendChild(JournalType);
									Line.appendChild(JournalSource);		//Null in FMS8
									Line.appendChild(TransactionReference);
									Line.appendChild(Description);
									Line.appendChild(DueDate);
									Line.appendChild(AnalysisCode1);
									Line.appendChild(AnalysisCode2);		
									Line.appendChild(AnalysisCode3);		
									Line.appendChild(AnalysisCode4);		
									Line.appendChild(AnalysisCode5);		
									Line.appendChild(AnalysisCode6);		
									Line.appendChild(AnalysisCode7);		
									Line.appendChild(AnalysisCode8);		
									Line.appendChild(AnalysisCode10);		
									Line.appendChild(GeneralDescription9);		
									Line.appendChild(GeneralDescription10);		
									Line.appendChild(GeneralDescription11);		
									Line.appendChild(GeneralDescription12);		
									Line.appendChild(GeneralDescription13);		
									Line.appendChild(GeneralDescription14);		
									Line.appendChild(GeneralDescription15);	
									
									if(i%2==0)
									{
										AccountCode.appendChild(doc.createTextNode(account_code));
									}
									else
									{
										AccountCode.appendChild(doc.createTextNode(acc_cd));
									}
									AccountingPeriod.appendChild(doc.createTextNode(accountPeriod));
									BaseAmount.appendChild(doc.createTextNode(tax_amt));
									if(i%2==0)
									{
										DebitCredit.appendChild(doc.createTextNode("C"));
									}
									else
									{
										DebitCredit.appendChild(doc.createTextNode("D"));
									}
									TransactionAmount.appendChild(doc.createTextNode(tax_amt));
									Base2ReportingAmount.appendChild(doc.createTextNode("0.00"));		
									CurrencyCode.appendChild(doc.createTextNode("INR"));
									CurrencyRate.appendChild(doc.createTextNode("0"));					
									TransactionDate.appendChild(doc.createTextNode(trans_dt));
									JournalType.appendChild(doc.createTextNode(journal_type));
									TransactionReference.appendChild(doc.createTextNode(invoice_no));
									Description.appendChild(doc.createTextNode(description));
									DueDate.appendChild(doc.createTextNode(dueDt));
									if(i%2!=0)
									{
										AnalysisCode2.appendChild(doc.createTextNode(cccac));
										AnalysisCode3.appendChild(doc.createTextNode("LNA"));
										AnalysisCode4.appendChild(doc.createTextNode(acc_cd));
									}
									AnalysisCode10.appendChild(doc.createTextNode(bucac));
									if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
									{
										GeneralDescription9.appendChild(doc.createTextNode("Service"));		//GOODS_SERVICE_FLAG
										GeneralDescription10.appendChild(doc.createTextNode("0"));			//REV_CHARGE
										GeneralDescription11.appendChild(doc.createTextNode("999799"));		//HSN_SAC
										GeneralDescription12.appendChild(doc.createTextNode(buStateNm));	//POS
										GeneralDescription13.appendChild(doc.createTextNode(tax_amt));		//TAX_AMT
										GeneralDescription14.appendChild(doc.createTextNode(sup_type));		//SUPPLY_TYPE
										if(inv_flag.equals("UG"))
										{
											GeneralDescription15.appendChild(doc.createTextNode(tax_amt));	//TOTAL_INV_AMT
										}
										else
										{
											GeneralDescription15.appendChild(doc.createTextNode(invoice_amt));	//TOTAL_INV_AMT
										}
									}
								}
							}
						}
						
					}
				}
				
				//for saving the file info in the table 
				if(!sysdate.equals(""))
				{
			    	String appPath = request.getServletContext().getRealPath("");
			    	
		        	String main_folder="";
					if(!comp_cd.equals(""))
					{
						main_folder=CommonVariable.work_dir+comp_cd;
					}
					File MainDir = new File(appPath+File.separator+main_folder);
			        if(!MainDir.exists()) 
			        {
			        	MainDir.mkdir();
			        }
			        String sub_folder=""+CommonVariable.sun_xml;
					
					File SubDir = new File(appPath+File.separator+main_folder+File.separator+sub_folder);
			        if(!SubDir.exists()) 
			        {
			        	SubDir.mkdir();
			        }
			        
			        File temp [] = new File(appPath+File.separator+main_folder+File.separator+sub_folder).listFiles();
			        
			        //int count1=1;
			        xmlFileNm=utilBean.getCompanyAbbr(conn, comp_cd)+"_"+sysdate+".xml";
			        for(File filename : temp)
			        {
			        	if(filename.getName().equals(sysdate))
			        	{
			        		xml_generate_flag=xml_generate_flag && false;
			        	}
			        }
			        //xmlFileNm=utilBean.getCompanyAbbr(conn, comp_cd)+"_"+sysdate+"_"+count1+".xml";
			        
			        if(xml_generate_flag)
			        {
			        	if(gen_type.equals("SG"))
			        	{
			        		int count=0;
			        		String queryString2="SELECT COUNT(*) "
			        				+ "FROM FMS_INV_FILE_DTL "
			        				+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
			        				+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=?";
			        		stmt2 = conn.prepareStatement(queryString2);
			        		stmt2.setString(1, comp_cd);
			        		stmt2.setString(2, bu_state_tin);
			        		stmt2.setString(3, invoice_seq);
			        		stmt2.setString(4, fin_year);
			        		stmt2.setString(5, "S");
			        		rset2=stmt2.executeQuery();
			        		if(rset2.next())
			        		{
			        			count=rset2.getInt(1);
			        		}
			        		rset2.close();
			        		stmt2.close();
			        		
			        		if(count > 0)
			        		{
			        			String queryString3="UPDATE FMS_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
			        					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
			        					+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=?";
			        			stmt3 = conn.prepareStatement(queryString3);
			        			stmt3.setString(1, xmlFileNm);
			        			stmt3.setString(2, emp_cd);
			        			stmt3.setString(3, comp_cd);
			        			stmt3.setString(4, bu_state_tin);
			        			stmt3.setString(5, invoice_seq);
			        			stmt3.setString(6, fin_year);
			        			stmt3.setString(7, "S");
			        			stmt3.executeUpdate();
			        			
			        			stmt3.close();
			        		}
			        		else
			        		{
			        			String queryString3="INSERT INTO FMS_INV_FILE_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,"
			        					+ "FILE_NAME,ENT_BY,ENT_DT) "
			        					+ "VALUES(?,?,?,?,?,"
			        					+ "?,?,SYSDATE)";
			        			stmt3 = conn.prepareStatement(queryString3);
			        			stmt3.setString(1, comp_cd);
			        			stmt3.setString(2, bu_state_tin);
			        			stmt3.setString(3, invoice_seq);
			        			stmt3.setString(4, fin_year);
			        			stmt3.setString(5, "S");
			        			stmt3.setString(6, xmlFileNm);
			        			stmt3.setString(7, emp_cd);
			        			stmt3.executeUpdate();
			        			stmt3.close();
			        		}
			        		conn.commit();
			        	}
			        	else if(gen_type.equals("FFLOW"))
			        	{
			        		int count=0;
			        		String queryString2="SELECT COUNT(*) "
			        				+ "FROM FMS_FFLOW_INV_FILE_DTL "
			        				+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND BU_STATE_TIN=? "
			        				+ "AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
			        				+ "AND PDF_TYPE=?";
			        		stmt2 = conn.prepareStatement(queryString2);
			        		stmt2.setString(1, comp_cd);
			        		stmt2.setString(2, invoice_seq);
			        		stmt2.setString(3, bu_state_tin);
			        		stmt2.setString(4, invoice_type);
			        		stmt2.setString(5, fin_year);
			        		stmt2.setString(6, "S");
			        		rset2=stmt2.executeQuery();
			        		if(rset2.next())
			        		{
			        			count=rset2.getInt(1);
			        		}
			        		rset2.close();
			        		stmt2.close();
			        		
			        		if(count > 0)
			        		{
			        			String queryString3="UPDATE FMS_FFLOW_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
			        					+"WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND BU_STATE_TIN=? "
			        					+ "AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
			        					+ "AND PDF_TYPE=?";
			        			stmt3 = conn.prepareStatement(queryString3);
			        			stmt3.setString(1, xmlFileNm);
			        			stmt3.setString(2, emp_cd);
			        			stmt3.setString(3, comp_cd);
			        			stmt3.setString(4, invoice_seq);
			        			stmt3.setString(5, bu_state_tin);
			        			stmt3.setString(6, invoice_type);
			        			stmt3.setString(7, fin_year);
			        			stmt3.setString(8, "S");
			        			stmt3.executeUpdate();
			        			
			        			stmt3.close();
			        		}
			        		else
			        		{
			        			String queryString3="INSERT INTO FMS_FFLOW_INV_FILE_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,"
			        					+ "FILE_NAME,ENT_BY,ENT_DT,INVOICE_TYPE) "
			        					+ "VALUES(?,?,?,?,?,"
			        					+ "?,?,SYSDATE,?)";
			        			stmt3 = conn.prepareStatement(queryString3);
			        			stmt3.setString(1, comp_cd);
			        			stmt3.setString(2, bu_state_tin);
			        			stmt3.setString(3, invoice_seq);
			        			stmt3.setString(4, fin_year);
			        			stmt3.setString(5, "S");
			        			stmt3.setString(6, xmlFileNm);
			        			stmt3.setString(7, emp_cd);
			        			stmt3.setString(8, invoice_type);
			        			stmt3.executeUpdate();
			        			
			        			stmt3.close();
			        		}
			        		conn.commit();
			        	}
			        	else if(gen_type.equals("DLNG_SG"))
			        	{
			        		int count=0;
			        		String queryString2="SELECT COUNT(*) "
			        				+ "FROM FMS_DLNG_INV_FILE_DTL "
			        				+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
			        				+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=?";
			        		stmt2 = conn.prepareStatement(queryString2);
			        		stmt2.setString(1, comp_cd);
			        		stmt2.setString(2, bu_state_tin);
			        		stmt2.setString(3, invoice_seq);
			        		stmt2.setString(4, fin_year);
			        		stmt2.setString(5, "S");
			        		rset2=stmt2.executeQuery();
			        		if(rset2.next())
			        		{
			        			count=rset2.getInt(1);
			        		}
			        		rset2.close();
			        		stmt2.close();
			        		
			        		if(count > 0)
			        		{
			        			String queryString3="UPDATE FMS_DLNG_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
			        					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
			        					+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=?";
			        			stmt3 = conn.prepareStatement(queryString3);
			        			stmt3.setString(1, xmlFileNm);
			        			stmt3.setString(2, emp_cd);
			        			stmt3.setString(3, comp_cd);
			        			stmt3.setString(4, bu_state_tin);
			        			stmt3.setString(5, invoice_seq);
			        			stmt3.setString(6, fin_year);
			        			stmt3.setString(7, "S");
			        			stmt3.executeUpdate();
			        			
			        			stmt3.close();
			        		}
			        		else
			        		{
			        			String queryString3="INSERT INTO FMS_DLNG_INV_FILE_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,"
			        					+ "FILE_NAME,ENT_BY,ENT_DT) "
			        					+ "VALUES(?,?,?,?,?,"
			        					+ "?,?,SYSDATE)";
			        			stmt3 = conn.prepareStatement(queryString3);
			        			stmt3.setString(1, comp_cd);
			        			stmt3.setString(2, bu_state_tin);
			        			stmt3.setString(3, invoice_seq);
			        			stmt3.setString(4, fin_year);
			        			stmt3.setString(5, "S");
			        			stmt3.setString(6, xmlFileNm);
			        			stmt3.setString(7, emp_cd);
			        			stmt3.executeUpdate();
			        			stmt3.close();
			        		}
			        		conn.commit();
			        	}
			        	else if(gen_type.equals("DERV"))
			        	{
			        		int count=0;
			        		String queryString2="SELECT COUNT(*) "
			        				+ "FROM FMS_DERV_INV_FILE_DTL "
			        				+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
			        				+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND INV_TYPE=? ";
			        		stmt2 = conn.prepareStatement(queryString2);
			        		stmt2.setString(1, comp_cd);
			        		stmt2.setString(2, bu_state_tin);
			        		stmt2.setString(3, invoice_seq);
			        		stmt2.setString(4, fin_year);
			        		stmt2.setString(5, "S");
			        		stmt2.setString(6, invoice_type);
			        		rset2=stmt2.executeQuery();
			        		if(rset2.next())
			        		{
			        			count=rset2.getInt(1);
			        		}
			        		rset2.close();
			        		stmt2.close();
			        		
			        		if(count > 0)
			        		{
			        			String queryString3="UPDATE FMS_DERV_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
			        					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
			        					+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND INV_TYPE=? ";
			        			stmt3 = conn.prepareStatement(queryString3);
			        			stmt3.setString(1, xmlFileNm);
			        			stmt3.setString(2, emp_cd);
			        			stmt3.setString(3, comp_cd);
			        			stmt3.setString(4, bu_state_tin);
			        			stmt3.setString(5, invoice_seq);
			        			stmt3.setString(6, fin_year);
			        			stmt3.setString(7, "S");
			        			stmt3.setString(8, invoice_type);
			        			stmt3.executeUpdate();
			        			
			        			stmt3.close();
			        		}
			        		else
			        		{
			        			String queryString3="INSERT INTO FMS_DERV_INV_FILE_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,"
			        					+ "FILE_NAME,ENT_BY,ENT_DT,INV_TYPE) "
			        					+ "VALUES(?,?,?,?,?,"
			        					+ "?,?,SYSDATE,?)";
			        			stmt3 = conn.prepareStatement(queryString3);
			        			stmt3.setString(1, comp_cd);
			        			stmt3.setString(2, bu_state_tin);
			        			stmt3.setString(3, invoice_seq);
			        			stmt3.setString(4, fin_year);
			        			stmt3.setString(5, "S");
			        			stmt3.setString(6, xmlFileNm);
			        			stmt3.setString(7, emp_cd);
			        			stmt3.setString(8, invoice_type);
			        			stmt3.executeUpdate();
			        			stmt3.close();
			        		}
			        		conn.commit();
			        	}
			        	else if(gen_type.equals("DLNG_FFLOW"))
			        	{
			        		int count=0;
			        		String queryString2="SELECT COUNT(*) "
			        				+ "FROM FMS_DLNG_FFLOW_INV_FILE_DTL "
			        				+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
			        				+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND INVOICE_TYPE=?";
			        		stmt2 = conn.prepareStatement(queryString2);
			        		stmt2.setString(1, comp_cd);
			        		stmt2.setString(2, bu_state_tin);
			        		stmt2.setString(3, invoice_seq);
			        		stmt2.setString(4, fin_year);
			        		stmt2.setString(5, "S");
			        		stmt2.setString(6, invoice_type);
			        		rset2=stmt2.executeQuery();
			        		if(rset2.next())
			        		{
			        			count=rset2.getInt(1);
			        		}
			        		rset2.close();
			        		stmt2.close();
			        		
			        		if(count > 0)
			        		{
			        			String queryString3="UPDATE FMS_DLNG_FFLOW_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
			        					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
			        					+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND INVOICE_TYPE=? " ;
			        			stmt3 = conn.prepareStatement(queryString3);
			        			stmt3.setString(1, xmlFileNm);
			        			stmt3.setString(2, emp_cd);
			        			stmt3.setString(3, comp_cd);
			        			stmt3.setString(4, bu_state_tin);
			        			stmt3.setString(5, invoice_seq);
			        			stmt3.setString(6, fin_year);
			        			stmt3.setString(7, "S");
			        			stmt3.setString(8, invoice_type);
			        			stmt3.executeUpdate();
			        			
			        			stmt3.close();
			        		}
			        		else
			        		{
			        			String queryString3="INSERT INTO FMS_DLNG_FFLOW_INV_FILE_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,"
			        					+ "FILE_NAME,ENT_BY,ENT_DT,INVOICE_TYPE) "
			        					+ "VALUES(?,?,?,?,?,"
			        					+ "?,?,SYSDATE,?)";
			        			stmt3 = conn.prepareStatement(queryString3);
			        			stmt3.setString(1, comp_cd);
			        			stmt3.setString(2, bu_state_tin);
			        			stmt3.setString(3, invoice_seq);
			        			stmt3.setString(4, fin_year);
			        			stmt3.setString(5, "S");
			        			stmt3.setString(6, xmlFileNm);
			        			stmt3.setString(7, emp_cd);
			        			stmt3.setString(8, invoice_type);
			        			stmt3.executeUpdate();
			        			stmt3.close();
			        		}
			        		conn.commit();
			        	}
			        	else if(gen_type.equals("DLNG_SERV"))
			        	{
			        		int count=0;
			        		String queryString2="SELECT COUNT(*) "
			        				+ "FROM FMS_DLNG_SVC_INV_FILE_DTL "
			        				+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
			        				+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? ";
			        		stmt2 = conn.prepareStatement(queryString2);
			        		stmt2.setString(1, comp_cd);
			        		stmt2.setString(2, bu_state_tin);
			        		stmt2.setString(3, invoice_seq);
			        		stmt2.setString(4, fin_year);
			        		stmt2.setString(5, "S");
			        		rset2=stmt2.executeQuery();
			        		if(rset2.next())
			        		{
			        			count=rset2.getInt(1);
			        		}
			        		rset2.close();
			        		stmt2.close();
			        		
			        		if(count>0)
			        		{
			        			String queryString3="UPDATE FMS_DLNG_SVC_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
			        					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
			        					+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? " ;
			        			stmt3 = conn.prepareStatement(queryString3);
			        			stmt3.setString(1, xmlFileNm);
			        			stmt3.setString(2, emp_cd);
			        			stmt3.setString(3, comp_cd);
			        			stmt3.setString(4, bu_state_tin);
			        			stmt3.setString(5, invoice_seq);
			        			stmt3.setString(6, fin_year);
			        			stmt3.setString(7, "S");
			        			stmt3.executeUpdate();
			        			
			        			stmt3.close();
			        		}
			        		else
			        		{
			        			String queryString3="INSERT INTO FMS_DLNG_SVC_INV_FILE_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,"
			        					+ "FILE_NAME,ENT_BY,ENT_DT) "
			        					+ "VALUES(?,?,?,?,?,"
			        					+ "?,?,SYSDATE)";
			        			stmt3 = conn.prepareStatement(queryString3);
			        			stmt3.setString(1, comp_cd);
			        			stmt3.setString(2, bu_state_tin);
			        			stmt3.setString(3, invoice_seq);
			        			stmt3.setString(4, fin_year);
			        			stmt3.setString(5, "S");
			        			stmt3.setString(6, xmlFileNm);
			        			stmt3.setString(7, emp_cd);
			        			stmt3.executeUpdate();
			        			stmt3.close();
			        		}
			        		conn.commit();
			        	}
			        }
				}
			}
			rset.close();
			stmt.close();
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD","");
			transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet","");
			
		    Transformer transformer = transformerFactory.newTransformer();
		    DOMSource source = new DOMSource(doc);
		    
		    if(xml_generate_flag)
		    {
		    	if(!xmlFileNm.equals(""))
		    	{
		    		String appPath = request.getServletContext().getRealPath("");
		    		
		    		String main_folder="";
		    		if(!comp_cd.equals(""))
		    		{
		    			main_folder=CommonVariable.work_dir+comp_cd;
		    		}
		    		File MainDir = new File(appPath+File.separator+main_folder);
		    		if(!MainDir.exists()) 
		    		{
		    			MainDir.mkdir();
		    		}
		    		String sub_folder=""+CommonVariable.sun_xml;
		    		
		    		File SubDir = new File(appPath+File.separator+main_folder+File.separator+sub_folder);
		    		if(!SubDir.exists()) 
		    		{
		    			SubDir.mkdir();
		    		}
		    		
		    		StreamResult result =  new StreamResult(new File(appPath+File.separator+main_folder+File.separator+sub_folder+""+File.separator+""+xmlFileNm));
		    		transformer.transform(source, result);
		    		
		    		msg = "Success! - SUN Sales XML generated!";
					msg_type="S";
		    	}
		    	else
		    	{
		    		msg = "Failed! - SUN Sales XML generation failed!";
					msg_type="E";
		    	}
		    }
		    else
		    {
		    	msg = "Failed! - SUN Sales XML generation failed, Please retry again!";
				msg_type="E";
		    }
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//Parsing the Sun XML File
	public void parseSunXML()
	{
		String function_nm="parseSunXML()";
		try
		{
			if(!file_nm.equals(""))
			{
				String final_path=file_path+File.separator+file_nm;
				
				File fXmlFile = new File(file_path+File.separator+file_nm);
				
				if(fXmlFile.exists())
				{
					DocumentBuilderFactory dbFactory = xmlUtil.dcoumentBuilderFactory();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(fXmlFile);
					    
					doc.getDocumentElement().normalize();
					NodeList nList = doc.getElementsByTagName("SSC");
					for (int temp = 0; temp < nList.getLength(); temp++) 
					{
						String journal_type="";
						String inv_no="";
						
						Node nNode = nList.item(temp);
						Element eElement = (Element) nNode;
						
						NodeList nodes = eElement.getChildNodes();
						for(int i=0; i<nodes.getLength(); i++)
						{
							Node node = nodes.item(i);
							String childTag = node.getNodeName();
							if(childTag.equalsIgnoreCase("Payload"))
							{
								Element ele = (Element) node;
								NodeList nodes1 = ele.getChildNodes();
								for(int j=0; j<nodes1.getLength(); j++)
								{
									Node node1 = nodes1.item(j);
									String childTag1 = node1.getNodeName();
									if(childTag1.equalsIgnoreCase("Ledger"))
									{
										Element ele1 = (Element) node1;
										NodeList nodes2 = ele1.getChildNodes();
										
										for(int n=0;n<nodes2.getLength();n++)
										{
											Node node2 = nodes2.item(n);
											String childTag2 = node2.getNodeName();
											
											if(childTag2.equalsIgnoreCase("Line"))
											{
												Element ele2 = (Element) node2;
												NodeList nodes3 = ele2.getChildNodes();
												
												for(int k=0;k<nodes3.getLength();k++)
												{
													Node node3 = nodes3.item(k);
													String childTag3 = node3.getNodeName();
													
													//String approval_dt []= file_nm.split("_");
													//String appr_dt = approval_dt[1]+"/"+approval_dt[2]+"/"+approval_dt[3];
													//VAPPROVAL_DT.add(appr_dt);
													VLEDGER.add("A");
													
													if(childTag3.equalsIgnoreCase("AccountCode"))
													{
														VACCOUNT_CD.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("AccountingPeriod"))
													{
														VPERIOD_START_DT.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("BaseAmount"))
													{
														VBASE_AMT.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("DebitCredit"))
													{
														VDEBIT_CREDIT.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("TransactionAmount"))
													{
														VTRANS_AMT.add(nodes3.item(k).getTextContent()); 
													}
													else if(childTag3.equalsIgnoreCase("Base2ReportingAmount"))
													{
														VREPORT_AMT.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("CurrencyCode"))
													{
														VCURRENCY_CD.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("CurrencyRate"))
													{
														VEXCHNG_RATE.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("TransactionDate"))
													{
														VINVOICE_DT.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("JournalType"))
													{
														journal_type=nodes3.item(k).getTextContent();
														/*
														 * if(data.trim().equalsIgnoreCase("FMSPI") ||
														 * data.trim().equalsIgnoreCase("FMSPC") ||
														 * data.trim().equalsIgnoreCase("FMSFI") ||
														 * data.trim().equalsIgnoreCase("FMSFC")) { data = "FMSPR"; }
														 */
														//VJOURNAL_TYPE.add(journal_type);
													}
													else if(childTag3.equalsIgnoreCase("JournalSource"))
													{
														//null
													}
													else if(childTag3.equalsIgnoreCase("TransactionReference"))
													{
														inv_no=nodes3.item(k).getTextContent();
														VINVOICE_NO.add(inv_no);
														if(inv_no.contains("PNCD"))	//for custom_duty provisional
														{
															journal_type="FMSPC";
														}
														else if(inv_no.contains("PNS"))	//for cargo provisional
														{
															journal_type="FMSPI";
														}
														else if(inv_no.contains("NS"))	//for cargo final
														{
															journal_type="FMSFI";
														}
														else if(inv_no.contains("NCD"))	//for custom duty final
														{
															journal_type="FMSFC";
														}
														VJOURNAL_TYPE.add(journal_type);
														
														String sun_approval_dt=getSunApprovalDt(inv_no);
														if(!sun_approval_dt.equals(""))
														{
															sun_approval_dt=sun_approval_dt.split("/")[0]+sun_approval_dt.split("/")[1]+sun_approval_dt.split("/")[2];
														}
														VAPPROVAL_DT.add(sun_approval_dt);
													}
													else if(childTag3.equalsIgnoreCase("Description"))
													{
														VDESC.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("DueDate"))
													{
														VINVOICE_DUE_DT.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("AnalysisCode1"))
													{
														//Null
													}
													else if(childTag3.equalsIgnoreCase("AnalysisCode2"))
													{
														VCOST_CTR_CD.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("AnalysisCode3"))
													{
														VEMPLOYEE_CD.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("AnalysisCode4"))
													{
														VCOA_CD.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("AnalysisCode5"))
													{
														//NULL
													}
													else if(childTag3.equalsIgnoreCase("AnalysisCode6"))
													{
														//NULL
													}
													else if(childTag3.equalsIgnoreCase("AnalysisCode7"))
													{
														VCODE.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("AnalysisCode8"))
													{
														//Null
													}
													else if(childTag3.equalsIgnoreCase("AnalysisCode10"))
													{
														VBU_UNIT_CD.add(nodes3.item(k).getTextContent());
													}
													if(journal_type.equals("FMSSL"))
													{
														if(childTag3.equalsIgnoreCase("GeneralDescription9"))
														{
															VGOOD_SERVICE.add(nodes3.item(k).getTextContent());
														}
														else if(childTag3.equalsIgnoreCase("GeneralDescription10"))
														{
															VREV_CHARGE.add(nodes3.item(k).getTextContent());
														}
														else if(childTag3.equalsIgnoreCase("GeneralDescription11"))
														{
															VHSN_CD.add(nodes3.item(k).getTextContent());
														}
														else if(childTag3.equalsIgnoreCase("GeneralDescription12"))
														{
															VPOS_CD.add(nodes3.item(k).getTextContent());
														}
														else if(childTag3.equalsIgnoreCase("GeneralDescription13"))
														{
															VTAX_LINE_AMT.add(nodes3.item(k).getTextContent());
														}
														else if(childTag3.equalsIgnoreCase("GeneralDescription14"))
														{
															VSUPPLY_TYPE.add(nodes3.item(k).getTextContent());
														}
														else if(childTag3.equalsIgnoreCase("GeneralDescription15"))
														{
															VTOTAL_INV_AMT.add(nodes3.item(k).getTextContent());
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//for generating the report 
	public void getSunApprovedList()
	{
		String function_nm="getSunApprovedList()";
		try
		{
			String counterpty_cd="";
		    String fin_year="";
		    String agmt_no="";
		    String agmt_rev="";
		    String cont_no="";
		    String cont_rev="";
		    String cont_type="";
		    String bu_unit="";
		    String bu_state_tin="";
		    String plant_seq_no="";
		    String invoice_seq="";
		    String cargo_no="";
		    String invoice_no="";
		    String invoice_dt="";
		    String gross_amt="";
		    String tax_amt="";
		    String exchg_rate="";
		    String inv_due_dt="";
		    String accountPeriod="";
		    String inv_raised_in="";
		    String gross_amt_usd="0.00";
		    String journal_type="FMSSL";
		    String alloc_qty="";
		    String sales_price="";
		    String sales_price_unit="";
		    String periodStartDt="";
		    String periodEndDt="";
		    String bucac="SEI";	
		    String taxStructCd="";
		    String tcsStructCd="";
		    String tdsStructCd="";
		    String tcs_amt="";
		    String tds_amt="";
		    String tcs_tds="";
		    String transportationAmt="";
		    String trans_account_cd="802012";
		    String igx_account_cd="153995";	//Account code for the IGX account 
		    String invoice_type="";
		    String sub_inv_type="";
		    String gen_type="";
		    String market_margin_amt="";
		    String oth_amt="";
		    String approve_dt="";
		    String inv_flag="";
		    String sug_qty="";
		    String net_payable_amt="";
		    String latePaymentAcc_cd="792003";
		    String criteria="";
		    
		    int ctn=0;
		    String queryString="";
		    if(inv_type.equals(""))		
	    	{
			    queryString="SELECT A.COUNTERPARTY_CD,A.FINANCIAL_YEAR,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE, "
						+ "A.BU_UNIT,A.BU_STATE_TIN,A.PLANT_SEQ,A.INVOICE_SEQ,A.CARGO_NO,A.INVOICE_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'), "
						+ "A.GROSS_AMT,A.TAX_AMT,A.EXCHG_RATE_VALUE,TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),A.INVOICE_RAISED_IN,A.ALLOC_QTY,A.SALE_PRICE,"
						+ "A.SALE_PRICE_UNIT,TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),A.TAX_STRUCT_CD, "
						+ "A.TCS_TDS,A.TCS_STRUCT_CD,A.TCS_AMT,A.TDS_STRUCT_CD,A.TDS_GROSS_AMT,A.TRANSPORTATION_AMOUNT,NULL,NULL,NULL,'SG', "	//A.GROSS_AMT_USD,A.INVOICE_TYPE,A.SUB_INV_TYPE,gen_type 
						+ "A.MARKET_MARGIN_AMT,A.OTHER_CHARGES_AMT,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),INV_FLAG,SUG_QTY,NET_PAYABLE_AMT,REF_NO, "
						+ "A.CRITERIA " 
						+ "FROM FMS_INVOICE_MST A "
						+ "WHERE A.COMPANY_CD=? AND A.SAP_APPROVAL=? ";
			    if(callFlag.equalsIgnoreCase("INVOICE_SUN_XML_GENERATION"))
			    {
			    	queryString+="AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
			    			+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
			    			+ "AND NOT EXISTS(SELECT B.PDF_TYPE FROM FMS_INV_FILE_DTL B WHERE  B.COMPANY_CD=A.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
			    			+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND B.PDF_TYPE IN (?,?) ) ";
			    }
			    else
			    {
		    		queryString+="AND EXISTS(SELECT B.PDF_TYPE FROM FMS_INV_FILE_DTL B WHERE  B.COMPANY_CD=A.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
		    				+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND B.PDF_TYPE IN (?) AND B.ENT_DT=(SELECT MAX(ENT_DT) FROM FMS_INV_FILE_DTL C "
		    				+ "WHERE B.COMPANY_CD=C.COMPANY_CD AND C.BU_STATE_TIN=B.BU_STATE_TIN AND C.INVOICE_SEQ=B.INVOICE_SEQ "
		    				+ "AND C.FINANCIAL_YEAR=B.FINANCIAL_YEAR)) AND A.BU_STATE_TIN=? AND FINANCIAL_YEAR=? AND A.INVOICE_SEQ=? ";
			    }
				queryString+="UNION ALL ";
	    	}
			queryString+="SELECT A.COUNTERPARTY_CD,A.FINANCIAL_YEAR,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE, "
					+ "A.BU_UNIT,A.BU_STATE_TIN,NULL,A.INVOICE_SEQ,A.CARGO_NO,A.INVOICE_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'), "
					+ "A.GROSS_AMT_INR,A.TAX_AMT,A.EXCHG_RATE_VALUE,TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),A.INVOICE_RAISED_IN,A.ALLOC_QTY,NULL, "
					+ "NULL,TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),A.TAX_STRUCT_CD, "
					+ "A.TCS_TDS,A.TCS_STRUCT_CD,A.TCS_AMT,A.TDS_STRUCT_CD,A.TDS_GROSS_AMT,A.TRANSPORTATION_AMOUNT,A.GROSS_AMT_USD,A.INVOICE_TYPE,A.SUB_INV_TYPE,'FFLOW', "
					+ "NULL,NULL,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),NULL,NULL,NET_PAYABLE_AMT,NULL,"
					+ "NULL "
					+ "FROM FMS_FFLOW_INV_MST A "
					+ "WHERE A.COMPANY_CD=? AND A.SAP_APPROVAL=?  ";
			if(callFlag.equalsIgnoreCase("INVOICE_SUN_XML_GENERATION"))
		    {
				queryString+="AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
						+"AND NOT EXISTS(SELECT B.PDF_TYPE FROM FMS_FFLOW_INV_FILE_DTL B WHERE  B.COMPANY_CD=A.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
		    			+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND B.PDF_TYPE IN (?,?) AND B.INVOICE_TYPE=A.INVOICE_TYPE) ";
		    }
			else
		    {
				queryString+="AND EXISTS(SELECT B.PDF_TYPE FROM FMS_FFLOW_INV_FILE_DTL B WHERE  B.COMPANY_CD=A.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
		    			+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND B.PDF_TYPE IN (?) AND B.INVOICE_TYPE=A.INVOICE_TYPE AND B.ENT_DT=(SELECT MAX(ENT_DT) FROM FMS_FFLOW_INV_FILE_DTL C "
		    			+ "WHERE B.COMPANY_CD=C.COMPANY_CD AND C.BU_STATE_TIN=B.BU_STATE_TIN AND C.INVOICE_SEQ=B.INVOICE_SEQ "
		    			+ "AND C.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND C.INVOICE_TYPE=B.INVOICE_TYPE)) AND A.BU_STATE_TIN=? AND A.FINANCIAL_YEAR=? AND A.INVOICE_SEQ=? AND A.INVOICE_TYPE=? ";
		    }
			queryString+="UNION ALL ";
			queryString+="SELECT A.COUNTERPARTY_CD,A.FINANCIAL_YEAR,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,  "
					+ "A.BU_UNIT,A.BU_STATE_TIN,A.PLANT_SEQ,A.INVOICE_SEQ,NULL,A.INVOICE_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),  "
					+ "A.GROSS_AMT,A.TAX_AMT,A.EXCHG_RATE_VALUE,TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),A.INVOICE_RAISED_IN,A.ALLOC_QTY,A.SALE_PRICE, "
					+ "A.SALE_PRICE_UNIT,TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),A.TAX_STRUCT_CD,  "
					+ "A.TCS_TDS,A.TCS_STRUCT_CD,A.TCS_AMT,A.TDS_STRUCT_CD,A.TDS_GROSS_AMT,NULL,NULL,NULL,NULL,'DLNG_SG', 	 "
					+ "NULL,NULL,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),INV_FLAG,NULL,NET_PAYABLE_AMT,NULL, "
					+ "A.CRITERIA  "
					+ "FROM FMS_DLNG_INVOICE_MST A  "
					+ "WHERE A.COMPANY_CD=? AND A.SAP_APPROVAL=?  "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY')  "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY')  "
					+ "AND NOT EXISTS(SELECT B.PDF_TYPE FROM FMS_DLNG_INV_FILE_DTL B WHERE  B.COMPANY_CD=A.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ  "
					+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND B.PDF_TYPE IN (?,?) ) ";
			queryString+="UNION ALL ";
			queryString+="SELECT A.COUNTERPARTY_CD,A.FINANCIAL_YEAR,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE, "
					+ "A.BU_UNIT,A.BU_STATE_TIN,A.PLANT_SEQ,A.INVOICE_SEQ,A.INSTRUMENT_NO,A.INVOICE_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'), "
					+ "A.INVOICE_AMT,NULL,NULL,TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),A.INVOICE_RAISED_IN,A.ALLOC_QTY,A.SALE_PRICE, "
					+ "A.SALE_PRICE_UNIT,TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),NULL, "
					+ "NULL,NULL,NULL,NULL,NULL,NULL,NULL,A.INV_TYPE,NULL,'DERV', "
					+ "NULL,NULL,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),NULL,NULL,NET_PAYABLE_AMT,NULL, "
					+ "NULL "
					+ "FROM FMS_DERV_INVOICE_MST A  "
					+ "WHERE A.COMPANY_CD=? AND A.SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND NOT EXISTS(SELECT B.PDF_TYPE FROM FMS_DERV_INV_FILE_DTL B WHERE  B.COMPANY_CD=A.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
					+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND B.PDF_TYPE IN (?,?) ) "
					+ "AND A.INV_TYPE=? ";
			queryString+="UNION ALL ";		//PB 20250913 for adding the dlng fflow invoice 
			queryString+="SELECT A.COUNTERPARTY_CD,A.FINANCIAL_YEAR,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,  "
					+ "A.BU_UNIT,A.BU_STATE_TIN,NULL,A.INVOICE_SEQ,A.CARGO_NO,A.INVOICE_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),  "
					+ "A.GROSS_AMT_INR,A.TAX_AMT,A.EXCHG_RATE_VALUE,TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),A.INVOICE_RAISED_IN,A.ALLOC_QTY,NULL,  "
					+ "NULL,TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),A.TAX_STRUCT_CD,  "
					+ "A.TCS_TDS,A.TCS_STRUCT_CD,A.TCS_AMT,A.TDS_STRUCT_CD,A.TDS_GROSS_AMT,NULL,A.GROSS_AMT_USD,A.INVOICE_TYPE,A.SUB_INV_TYPE,'DLNG_FFLOW',  "
					+ "NULL,NULL,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),NULL,NULL,NET_PAYABLE_AMT,NULL,"
					+ "NULL  "
					+ "FROM FMS_DLNG_FFLOW_INV_MST A  "
					+ "WHERE A.COMPANY_CD=? AND A.SAP_APPROVAL=?   "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY')   "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY')   "
					+ "AND NOT EXISTS(SELECT B.PDF_TYPE FROM FMS_DLNG_FFLOW_INV_FILE_DTL B WHERE  B.COMPANY_CD=A.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ   "
					+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.INVOICE_TYPE=B.INVOICE_TYPE AND B.PDF_TYPE IN (?,?)) ";
			queryString+="UNION ALL ";		//PB 20251027 for adding the dlng service invoice 
			queryString+="SELECT A.COUNTERPARTY_CD,A.FINANCIAL_YEAR,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE, "
					+ "A.BU_UNIT,A.BU_STATE_TIN,A.PLANT_SEQ,A.INVOICE_SEQ,A.CARGO_NO,A.INVOICE_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'), "
					+ "A.GROSS_AMT,A.TAX_AMT,A.EXCHG_RATE_VALUE,TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),A.INVOICE_RAISED_IN,A.QTY,A.SALE_PRICE, "
					+ "A.SALE_PRICE_UNIT,TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),A.TAX_STRUCT_CD, "
					+ "A.TCS_TDS,A.TCS_STRUCT_CD,A.TCS_AMT,A.TDS_STRUCT_CD,A.TDS_GROSS_AMT,NULL,NULL,NULL,NULL,'DLNG_SERV', "
					+ "NULL,NULL,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),INV_FLAG,NULL,NET_PAYABLE_AMT,NULL,"
					+ "NULL "
					+ "FROM FMS_DLNG_SVC_INVOICE_MST A "
					+ "WHERE A.COMPANY_CD=? AND A.SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND NOT EXISTS(SELECT B.PDF_TYPE FROM FMS_DLNG_SVC_INV_FILE_DTL B WHERE  B.COMPANY_CD=A.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
					+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND B.PDF_TYPE IN (?,?)) ";
			
			
			stmt = conn.prepareStatement(queryString);
			if(inv_type.equals(""))
			{
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, "Y");
				if(callFlag.equalsIgnoreCase("INVOICE_SUN_XML_GENERATION"))
			    {
					stmt.setString(++ctn, from_dt);
					stmt.setString(++ctn, to_dt);
					stmt.setString(++ctn, "X");
					stmt.setString(++ctn, "S");
			    }
				else
				{
					stmt.setString(++ctn, "S");
					stmt.setString(++ctn, buStateTin);
					stmt.setString(++ctn, financial_year);
					stmt.setString(++ctn, inv_seq);
				}
			}
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, "Y");
			if(callFlag.equalsIgnoreCase("INVOICE_SUN_XML_GENERATION"))
		    {
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, "X");
				stmt.setString(++ctn, "S");
		    }
			else
			{
				stmt.setString(++ctn, "S");
				stmt.setString(++ctn, buStateTin);
				stmt.setString(++ctn, financial_year);
				stmt.setString(++ctn, inv_seq);
				stmt.setString(++ctn, inv_type);
			}
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, from_dt);
			stmt.setString(++ctn, to_dt);
			stmt.setString(++ctn, "X");
			stmt.setString(++ctn, "S");
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, from_dt);
			stmt.setString(++ctn, to_dt);
			stmt.setString(++ctn, "X");
			stmt.setString(++ctn, "S");
			stmt.setString(++ctn, "I");
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, from_dt);
			stmt.setString(++ctn, to_dt);
			stmt.setString(++ctn, "X");
			stmt.setString(++ctn, "S");
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, from_dt);
			stmt.setString(++ctn, to_dt);
			stmt.setString(++ctn, "X");
			stmt.setString(++ctn, "S");
			
			rset=stmt.executeQuery();
			while(rset.next())
			{
				counterpty_cd=rset.getString(1)==null?"":rset.getString(1);
				fin_year=rset.getString(2)==null?"":rset.getString(2);
				agmt_no=rset.getString(3)==null?"":rset.getString(3);
				agmt_rev=rset.getString(4)==null?"":rset.getString(4);
				cont_no=rset.getString(5)==null?"":rset.getString(5);
				cont_rev=rset.getString(6)==null?"":rset.getString(6);
				cont_type=rset.getString(7)==null?"":rset.getString(7);
				bu_unit=rset.getString(8)==null?"":rset.getString(8);
				bu_state_tin=rset.getString(9)==null?"":rset.getString(9);
				plant_seq_no=rset.getString(10)==null?"":rset.getString(10);
				invoice_seq=rset.getString(11)==null?"":rset.getString(11);
				cargo_no=rset.getString(12)==null?"":rset.getString(12);
				invoice_no=rset.getString(13)==null?"":rset.getString(13);
				invoice_dt=rset.getString(14)==null?"":rset.getString(14);
				gross_amt=rset.getString(15)==null?"":nf.format(rset.getDouble(15));
				tax_amt=rset.getString(16)==null?"":nf.format(rset.getDouble(16));
				exchg_rate=rset.getString(17)==null?"":nf.format(rset.getDouble(17));
				inv_due_dt=rset.getString(18)==null?"-":rset.getString(18);
				inv_raised_in=rset.getString(19)==null?"":rset.getString(19);
				alloc_qty=rset.getString(20)==null?"":nf.format(rset.getDouble(20));
				sales_price=rset.getString(21)==null?"":nf.format(rset.getDouble(21));
				sales_price_unit=rset.getString(22)==null?"":rset.getString(22);
				periodStartDt=rset.getString(23)==null?"":rset.getString(23);
				periodEndDt=rset.getString(24)==null?"":rset.getString(24);
				taxStructCd=rset.getString(25)==null?"":rset.getString(25);
				tcs_tds=rset.getString(26)==null?"":rset.getString(26);
				tcsStructCd=rset.getString(27)==null?"":rset.getString(27);
				tcs_amt=rset.getString(28)==null?"":nf.format(rset.getDouble(28));
				tdsStructCd=rset.getString(29)==null?"":rset.getString(29);
				tds_amt=rset.getString(30)==null?"":nf.format(rset.getDouble(30));
				transportationAmt=rset.getString(31)==null?"":nf.format(rset.getDouble(31));
				gross_amt_usd=rset.getString(32)==null?nf.format(0):nf.format(rset.getDouble(32));
				invoice_type=rset.getString(33)==null?"":rset.getString(33);
				sub_inv_type=rset.getString(34)==null?"":rset.getString(34);
				gen_type=rset.getString(35)==null?"":rset.getString(35);
				market_margin_amt=rset.getString(36)==null?"":nf.format(rset.getDouble(36));
				oth_amt=rset.getString(37)==null?"":nf.format(rset.getDouble(37));
				approve_dt=rset.getString(38)==null?"":rset.getString(38);
				inv_flag=rset.getString(39)==null?"":rset.getString(39);
				sug_qty=rset.getString(40)==null?"":nf.format(rset.getDouble(40));
				double net_payable=rset.getDouble(41);
				String ref_no=rset.getString(42)==null?"":rset.getString(42);
				double tds_gross_amt=rset.getDouble(30);
				criteria=rset.getString(43)==null?"":rset.getString(43);
								
				accountPeriod=getAccountingPeriod(invoice_dt);
				
				String []invDt = invoice_dt.split("/");
				String trans_dt = invDt[0]+invDt[1]+invDt[2];
				
				String sup_type=utilBean.getTaxDescr(conn, taxStructCd);
				//String [] invDueDt=inv_due_dt.split("/");
				//String dueDt=invDueDt[0]+invDueDt[1]+invDueDt[2];
				String dueDt=inv_due_dt.replace("/", "");
				
				if(gen_type.equals("DERV"))
				{
					//for finding the diff in price
					String diff_amt="";
					if(!gross_amt.equals("")&&!alloc_qty.equals(""))
					{
						diff_amt=nf.format(Double.parseDouble(gross_amt)/Double.parseDouble(alloc_qty));
						if(Double.parseDouble(diff_amt)<0)
						{
							diff_amt="("+diff_amt+")";
						}
					}
					String qty_unit=getQuantityUnit(counterpty_cd,agmt_no,"U",agmt_rev,cont_no,cont_type,cont_rev,cargo_no);		//here cargo_no represents instrument
					String description=(utilBean.getCounterpartyABBR(conn, counterpty_cd)+" "+alloc_qty+" "+qty_unit+" @ "+diff_amt+" MTM").toUpperCase();
					String account_cd=getCounterpartySunAccountCode(counterpty_cd, "T", "0", "V");
					String account_cd_lng_sale="711008"; 	//for derivative 711008 code is used
					String dr_cr_marker=Double.parseDouble(gross_amt)<0?"C":"D";
					for(int i=0;i<2;i++)
					{
						VINVOICE_NO.add(invoice_no);
						VJOURNAL_TYPE.add(journal_type);
						VAPPROVAL_DT.add(approve_dt);
						VLEDGER.add("A");
						if(i==0)
						{
							VACCOUNT_CD.add(account_cd);
							VDEBIT_CREDIT.add(dr_cr_marker);
							VCOA_CD.add(account_cd_lng_sale);	 
						}
						else
						{
							dr_cr_marker=Double.parseDouble(gross_amt)<0?"D":"C";
							VACCOUNT_CD.add(account_cd_lng_sale);
							VDEBIT_CREDIT.add(dr_cr_marker);
							VCOA_CD.add("");	 
						}
						VPERIOD_START_DT.add(accountPeriod);
						VBASE_AMT.add("");
						VTRANS_AMT.add(gross_amt);
						VREPORT_AMT.add("");
						VCURRENCY_CD.add("USD");
						VEXCHNG_RATE.add("");
						VINVOICE_DT.add(trans_dt);
						VDESC.add(description);
						VINVOICE_DUE_DT.add(dueDt);
						VCOST_CTR_CD.add("2MK01");
						VEMPLOYEE_CD.add("");
						VCODE.add("NA");
						VBU_UNIT_CD.add(bucac);
						
						VGOOD_SERVICE.add("");		//GOODS_SERVICE_FLAG
						VREV_CHARGE.add("");			//REV_CHARGE
						VHSN_CD.add("");		//HSN_SAC
						VPOS_CD.add("");	//POS
						VTAX_LINE_AMT.add("");		//TAX_AMT
						VSUPPLY_TYPE.add("");		//SUPPLY_TYPE
						VTOTAL_INV_AMT.add("");	//TOTAL_INV_AMT
						VTOTAL_INV_AMT.add("");	//TOTAL_INV_AMT
					}
				}
				else
				{
					//for fetching the BU state 
					String buStateNm="";
					String buAbbr="";
					String query1 = "SELECT PLANT_STATE,PLANT_ABBR "
							+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
							+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=? AND SEQ_NO=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
					stmt1 = conn.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, "B");
					stmt1.setString(3, comp_cd);
					stmt1.setString(4, bu_unit);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						buStateNm=rset1.getString(1)==null?"":rset1.getString(1);
						buAbbr=rset1.getString(2)==null?"":rset1.getString(2);
					}
					rset1.close();
					stmt1.close();
					
					String counterparty_abbr = utilBean.getCounterpartyABBR(conn, counterpty_cd);
					String account_code = getCounterpartySunAccountCode(counterpty_cd,"C","0","DFT");
					
					//PB20251128: FOR LATE PAYMENT AND INTEREST INVOICE 
					if(inv_flag.equals("LP"))
					{
						String description = (counterparty_abbr+" INT CH DELAYED PAYT "+" "+ref_no+" "+trans_dt).toUpperCase();
						String cccac= "1TM01"; //for IGX: cccac="2MK01"
						if(cont_type.equals("X") || cont_type.equals("W"))
						{
							cccac="2MK01";
						}
						if(!bu_state_tin.equals("24"))
						{
							cccac="2"+getStateCd(bu_state_tin)+"01";
						}
						
						//for debit
						VINVOICE_NO.add(invoice_no);
						VJOURNAL_TYPE.add(journal_type);
						VAPPROVAL_DT.add(approve_dt);
						VLEDGER.add("A");
						VACCOUNT_CD.add(account_code);
						VPERIOD_START_DT.add(accountPeriod);
						VBASE_AMT.add(gross_amt);
						VTRANS_AMT.add(gross_amt);
						VDEBIT_CREDIT.add("D");
						VREPORT_AMT.add(gross_amt_usd);
						VCURRENCY_CD.add("INR");
						VEXCHNG_RATE.add(exchg_rate);
						VINVOICE_DT.add(trans_dt);
						VDESC.add(description);
						VINVOICE_DUE_DT.add(dueDt);
						VCOST_CTR_CD.add("");
						if(cont_type.equals("O")||cont_type.equals("Q"))
						{
							VCOA_CD.add("");
						}
						else
						{
							VCOA_CD.add(latePaymentAcc_cd);
						}
						VEMPLOYEE_CD.add("");
						VCODE.add("");
						VBU_UNIT_CD.add(bucac);
						if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
						{
							VGOOD_SERVICE.add("Service");		//GOODS_SERVICE_FLAG
							VREV_CHARGE.add("0");			//REV_CHARGE
							VHSN_CD.add("999799");		//HSN_SAC
							VPOS_CD.add(buStateNm);	//POS
							VTAX_LINE_AMT.add(tax_amt);		//TAX_AMT
							VSUPPLY_TYPE.add(sup_type);		//SUPPLY_TYPE
							VTOTAL_INV_AMT.add(tax_amt);	//TOTAL_INV_AM
						}
						else
						{
							VGOOD_SERVICE.add("");		//GOODS_SERVICE_FLAG
							VREV_CHARGE.add("");			//REV_CHARGE
							VHSN_CD.add("");		//HSN_SAC
							VPOS_CD.add("");	//POS
							VTAX_LINE_AMT.add("");		//TAX_AMT
							VSUPPLY_TYPE.add("");		//SUPPLY_TYPE
							VTOTAL_INV_AMT.add("");	//TOTAL_INV_AMT
						}
						
						//for tax amount 
						if(!tax_amt.equals(""))
						{
							VINVOICE_NO.add(invoice_no);
							VJOURNAL_TYPE.add(journal_type);
							VAPPROVAL_DT.add(approve_dt);
							VLEDGER.add("A");
							VACCOUNT_CD.add(account_code);
							VPERIOD_START_DT.add(accountPeriod);
							VBASE_AMT.add(tax_amt);
							VTRANS_AMT.add(tax_amt);
							VDEBIT_CREDIT.add("D");
							VREPORT_AMT.add("0.00");
							VCURRENCY_CD.add("INR");
							VEXCHNG_RATE.add(exchg_rate);
							VINVOICE_DT.add(trans_dt);
							VDESC.add(description);
							VINVOICE_DUE_DT.add(dueDt);
							VCOST_CTR_CD.add("");
							VCOA_CD.add("");
							VEMPLOYEE_CD.add("");
							VCODE.add("NA");
							VBU_UNIT_CD.add(bucac);
							if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
							{
								VGOOD_SERVICE.add("Service");		//GOODS_SERVICE_FLAG
								VREV_CHARGE.add("0");			//REV_CHARGE
								VHSN_CD.add("999799");		//HSN_SAC
								VPOS_CD.add(buStateNm);	//POS
								VTAX_LINE_AMT.add(tax_amt);		//TAX_AMT
								VSUPPLY_TYPE.add(sup_type);		//SUPPLY_TYPE
								VTOTAL_INV_AMT.add(tax_amt);	//TOTAL_INV_AM
							}
							else
							{
								VGOOD_SERVICE.add("");		//GOODS_SERVICE_FLAG
								VREV_CHARGE.add("");			//REV_CHARGE
								VHSN_CD.add("");		//HSN_SAC
								VPOS_CD.add("");	//POS
								VTAX_LINE_AMT.add("");		//TAX_AMT
								VSUPPLY_TYPE.add("");		//SUPPLY_TYPE
								VTOTAL_INV_AMT.add("");	//TOTAL_INV_AMT
							}
						}
						//for credit 
						VINVOICE_NO.add(invoice_no);
						VJOURNAL_TYPE.add(journal_type);
						VAPPROVAL_DT.add(approve_dt);
						VLEDGER.add("A");
						VACCOUNT_CD.add(latePaymentAcc_cd);
						VPERIOD_START_DT.add(accountPeriod);
						VBASE_AMT.add(gross_amt);
						VTRANS_AMT.add(gross_amt);
						VDEBIT_CREDIT.add("C");
						VREPORT_AMT.add(gross_amt_usd);
						VCURRENCY_CD.add("INR");
						VEXCHNG_RATE.add(exchg_rate);
						VINVOICE_DT.add(trans_dt);
						VDESC.add(description);
						VINVOICE_DUE_DT.add(dueDt);
						VCOST_CTR_CD.add(cccac);
						VCOA_CD.add("");
						VEMPLOYEE_CD.add("");
						VCODE.add("");
						VBU_UNIT_CD.add(bucac);
						if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
						{
							VGOOD_SERVICE.add("Service");		//GOODS_SERVICE_FLAG
							VREV_CHARGE.add("0");			//REV_CHARGE
							VHSN_CD.add("999799");		//HSN_SAC
							VPOS_CD.add(buStateNm);	//POS
							VTAX_LINE_AMT.add(tax_amt);		//TAX_AMT
							VSUPPLY_TYPE.add(sup_type);		//SUPPLY_TYPE
							VTOTAL_INV_AMT.add(tax_amt);	//TOTAL_INV_AM
						}
						else
						{
							VGOOD_SERVICE.add("");		//GOODS_SERVICE_FLAG
							VREV_CHARGE.add("");			//REV_CHARGE
							VHSN_CD.add("");		//HSN_SAC
							VPOS_CD.add("");	//POS
							VTAX_LINE_AMT.add("");		//TAX_AMT
							VSUPPLY_TYPE.add("");		//SUPPLY_TYPE
							VTOTAL_INV_AMT.add("");	//TOTAL_INV_AMT
						}
						
						String queryString3="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
								+ "FROM FMS_INV_TAX_DTL "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
								+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=?";
						stmt1=conn.prepareStatement(queryString3);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, fin_year);
						stmt1.setString(3, invoice_seq);
						stmt1.setString(4, bu_state_tin);
						rset1=stmt1.executeQuery();
						
						while(rset1.next())
						{
							String tax_code=rset1.getString(1)==null?"":rset1.getString(1);
							String taxStrctCd=rset1.getString(2)==null?"":rset1.getString(2);
							String taxAmt=rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
							String taxBaseAmt=rset1.getString(4)==null?"":nf.format(rset1.getDouble(4));
							
							String acc_cd=getTaxStructureSunAccountCd(taxStrctCd,tax_code, bu_state_tin, bu_unit,"S");
							
							VINVOICE_NO.add(invoice_no);
							VJOURNAL_TYPE.add(journal_type);
							VAPPROVAL_DT.add(approve_dt);
							VLEDGER.add("A");
							VACCOUNT_CD.add(acc_cd);
							VPERIOD_START_DT.add(accountPeriod);
							VBASE_AMT.add(taxAmt);
							VTRANS_AMT.add(taxAmt);
							VDEBIT_CREDIT.add("C");
							VREPORT_AMT.add("0.00");
							VCURRENCY_CD.add("INR");
							VEXCHNG_RATE.add(exchg_rate);
							VINVOICE_DT.add(trans_dt);
							VDESC.add(description);
							VINVOICE_DUE_DT.add(dueDt);
							VCOST_CTR_CD.add("");
							VCOA_CD.add(latePaymentAcc_cd);
							VCODE.add("");
							VEMPLOYEE_CD.add("");
							VBU_UNIT_CD.add(bucac);
							
							if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
							{
								VGOOD_SERVICE.add("Service");		//GOODS_SERVICE_FLAG
								VREV_CHARGE.add("0");			//REV_CHARGE
								VHSN_CD.add("999799");		//HSN_SAC
								VPOS_CD.add(buStateNm);	//POS
								VTAX_LINE_AMT.add(tax_amt);		//TAX_AMT
								VSUPPLY_TYPE.add(sup_type);		//SUPPLY_TYPE
								VTOTAL_INV_AMT.add(tax_amt);	//TOTAL_INV_AM
							}
							else
							{
								VGOOD_SERVICE.add("");		//GOODS_SERVICE_FLAG
								VREV_CHARGE.add("");			//REV_CHARGE
								VHSN_CD.add("");		//HSN_SAC
								VPOS_CD.add("");	//POS
								VTAX_LINE_AMT.add("");		//TAX_AMT
								VSUPPLY_TYPE.add("");		//SUPPLY_TYPE
								VTOTAL_INV_AMT.add("");	//TOTAL_INV_AMT
							}
							
						}
						rset1.close();
						stmt1.close();
						
					}
					else
					{
						//for fetching the QQ date of LTCORA Cargo
						String qq_dt="";
						String qurey2="SELECT TO_CHAR(QQ_DT,'DDMON') FROM FMS_LTCORA_CONT_CARGO_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND AGMT_TYPE=? AND CARGO_NO=? "
								+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_NO=B.AGMT_NO "
								+ "AND A.AGMT_REV=B.AGMT_REV AND A.AGMT_TYPE=B.AGMT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
								+ "AND A.CARGO_NO=B.CARGO_NO)";
						stmt1 = conn.prepareStatement(qurey2);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterpty_cd);
						stmt1.setString(3, "C");
						stmt1.setString(4, agmt_no);
						stmt1.setString(5, agmt_rev);
						stmt1.setString(6, cont_no);
						stmt1.setString(7, cont_type);
						stmt1.setString(8, "A");
						stmt1.setString(9, cargo_no);
						rset1 = stmt1.executeQuery();
						if(rset1.next())
						{
							qq_dt=rset1.getString(1)==null?"":rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						if(gen_type.equals("SG") || gen_type.equals("DLNG_SG") || gen_type.equals("DLNG_SERV"))
						{
							if(inv_flag.equals("CR") || inv_flag.equals("DR"))
							{
								String temp_exchng_rate="0";
								String temp_gross_amt="";
								String temp_sale_price="0";
								String temp_tax_amt="";
								String temp_alloc_qty="";
								
								String queryString1="SELECT EXCHG_RATE_VALUE,GROSS_AMT,SALE_PRICE,TAX_AMT,ALLOC_QTY  "
										+ "FROM FMS_INV_CRDR_REF "
										+ "WHERE COMPANY_CD=? "
										+ "AND FINANCIAL_YEAR=? "
										+ "AND BU_STATE_TIN=? AND INVOICE_SEQ=? ";
								queryString1+="UNION ALL ";
								queryString1+="SELECT EXCHG_RATE_VALUE,GROSS_AMT,SALE_PRICE,TAX_AMT,ALLOC_QTY  "
										+ "FROM FMS_DLNG_INV_CRDR_REF "
										+ "WHERE COMPANY_CD=? "
										+ "AND FINANCIAL_YEAR=? "
										+ "AND BU_STATE_TIN=? AND INVOICE_SEQ=? ";
								stmt4=conn.prepareStatement(queryString1);
								stmt4.setString(1, comp_cd);
								stmt4.setString(2, fin_year);
								stmt4.setString(3, bu_state_tin);
								stmt4.setString(4, invoice_seq);
								stmt4.setString(5, comp_cd);
								stmt4.setString(6, fin_year);
								stmt4.setString(7, bu_state_tin);
								stmt4.setString(8, invoice_seq);
								rset4=stmt4.executeQuery();
								if(rset4.next())
								{
									temp_exchng_rate=rset4.getString(1)==null?"":nf.format(rset4.getDouble(1));
									temp_gross_amt=rset4.getString(2)==null?"":nf.format(rset4.getDouble(2));
									temp_sale_price=rset4.getString(3)==null?"":nf.format(rset4.getDouble(3));
									temp_tax_amt=rset4.getString(4)==null?"":nf.format(rset4.getDouble(4));
									temp_alloc_qty=rset4.getString(5)==null?"":nf.format(rset4.getDouble(5));
								}
								rset4.close();
								stmt4.close();
								
								if(!gross_amt.equals(""))
								{
									if(Double.doubleToRawLongBits(Double.parseDouble(gross_amt))==Double.doubleToRawLongBits(0))
									{
										gross_amt=temp_gross_amt;
									}
								}
								if(!alloc_qty.equals(""))
								{
									if(Double.doubleToRawLongBits(Double.parseDouble(alloc_qty))==Double.doubleToRawLongBits(0))
									{
										alloc_qty=temp_alloc_qty;
									}
								}
								if(!sales_price.equals(""))
								{
									if(Double.doubleToRawLongBits(Double.parseDouble(sales_price))==Double.doubleToRawLongBits(0))
									{
										sales_price=temp_sale_price;
									}
								}
								if(!exchg_rate.equals(""))
								{
									if(Double.doubleToRawLongBits(Double.parseDouble(exchg_rate))==Double.doubleToRawLongBits(0))
									{
										exchg_rate=temp_exchng_rate;
									}
								}
							}
							
							if(inv_raised_in.equals("2") && !exchg_rate.equals(""))
							{
								gross_amt = rset.getString(15) == null ?"":nf.format(rset.getDouble(15) * Double.parseDouble(exchg_rate));
								tax_amt = rset.getString(16) == null ? "": nf.format(rset.getDouble(16) * Double.parseDouble(exchg_rate));
								tcs_amt=rset.getString(28)==null?"":nf.format(rset.getDouble(28) * Double.parseDouble(exchg_rate));
								tds_amt=rset.getString(30)==null?"":nf.format(rset.getDouble(30) * Double.parseDouble(exchg_rate));
							}
							
							if(!exchg_rate.equals(""))
							{
								if(Double.parseDouble(exchg_rate)>0)
								{
									gross_amt_usd=nf.format(Double.parseDouble(gross_amt)/Double.parseDouble(exchg_rate));
								}
							}
							else
							{
								gross_amt_usd="0.00";
								exchg_rate="0";
							}
						}
						else
						{
							if(inv_raised_in.equals("2") && !exchg_rate.equals(""))
							{
								tax_amt=rset.getString(16)==null?"":nf.format(rset.getDouble(16) * Double.parseDouble(exchg_rate));
								tcs_amt=rset.getString(28)==null?"":nf.format(rset.getDouble(28) * Double.parseDouble(exchg_rate));
								tds_amt=rset.getString(30)==null?"":nf.format(rset.getDouble(30) * Double.parseDouble(exchg_rate));
								gross_amt=rset.getString(32)==null?"":nf.format(rset.getDouble(32) * Double.parseDouble(exchg_rate));
							}
							sales_price=nf.format(Double.parseDouble(gross_amt)/Double.parseDouble(alloc_qty));
						}
						
						String invoice_amt=gross_amt;
						String invoice_amt_usd=gross_amt_usd;
						if(criteria.contains("OC") || criteria.contains("MM") || criteria.contains("TC"))
						{
							double temp_inv_amt=0;
							if(!oth_amt.equals(""))
							{
								temp_inv_amt+=Double.parseDouble(oth_amt);
								invoice_amt=nf.format(temp_inv_amt);
								if(!invoice_amt_usd.equals("0.00"))
								{
									invoice_amt_usd=nf.format(Double.parseDouble(invoice_amt)/Double.parseDouble(exchg_rate));
								}
							}
							if(!market_margin_amt.equals(""))
							{
								temp_inv_amt+=Double.parseDouble(market_margin_amt);
								invoice_amt=nf.format(temp_inv_amt);
								if(!invoice_amt_usd.equals("0.00"))
								{
									invoice_amt_usd=nf.format(Double.parseDouble(invoice_amt)/Double.parseDouble(exchg_rate));
								}
							}
							if(!transportationAmt.equals(""))
							{
								temp_inv_amt+=Double.parseDouble(transportationAmt);
								invoice_amt=nf.format(temp_inv_amt);
								if(!invoice_amt_usd.equals("0.00"))
								{
									invoice_amt_usd=nf.format(Double.parseDouble(invoice_amt)/Double.parseDouble(exchg_rate));
								}
							}
						}
						
						if(!inv_flag.equals("CR")&&!inv_flag.equals("DR"))
						{
							double temp_inv_amt=0;
							if(!oth_amt.equals(""))
							{
								temp_inv_amt+=Double.parseDouble(oth_amt);
							}
							if(!market_margin_amt.equals(""))
							{
								temp_inv_amt+=Double.parseDouble(market_margin_amt);
							}
							invoice_amt=nf.format(Double.parseDouble(invoice_amt) +temp_inv_amt);
							if(!invoice_amt_usd.equals("0.00"))
							{
								invoice_amt_usd=nf.format(Double.parseDouble(invoice_amt)/Double.parseDouble(exchg_rate));
							}
						}
						
						if(inv_flag.equals("ST"))
						{
							gross_amt_usd=nf.format(0);
							exchg_rate="-";
						}
						exchg_rate=exchg_rate.equals("")?"0":exchg_rate;
						//gross_amt=gross_amt.equals("")?"":nf.format(Double.parseDouble(gross_amt)<0?(-1)*Double.parseDouble(gross_amt):Double.parseDouble(gross_amt));
						gross_amt=gross_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(gross_amt)));
						//gross_amt_usd=gross_amt_usd.equals("")?"":nf.format(Double.parseDouble(gross_amt_usd)<0?(-1)*Double.parseDouble(gross_amt_usd):Double.parseDouble(gross_amt_usd));
						gross_amt_usd=gross_amt_usd.equals("")?"":nf.format(Math.abs(Double.parseDouble(gross_amt_usd)));
						//invoice_amt=invoice_amt.equals("")?"":nf.format(Double.parseDouble(invoice_amt)<0?(-1)*Double.parseDouble(invoice_amt):Double.parseDouble(invoice_amt));
						invoice_amt=invoice_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(invoice_amt)));
						//invoice_amt_usd=invoice_amt_usd.equals("")?"":nf.format(Double.parseDouble(invoice_amt_usd)<0?(-1)*Double.parseDouble(invoice_amt_usd):Double.parseDouble(invoice_amt_usd));
						invoice_amt_usd=invoice_amt_usd.equals("")?"":nf.format(Math.abs(Double.parseDouble(invoice_amt_usd)));
						//tax_amt=tax_amt.equals("")?"":nf.format(Double.parseDouble(tax_amt)<0?(-1)*Double.parseDouble(tax_amt):Double.parseDouble(tax_amt));
						tax_amt=tax_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(tax_amt)));
						
						String gen_for="SUPPLY";
						String description ="";
						if(inv_flag.equals("UG"))
						{
							description= (counterparty_abbr+" AQ "+alloc_qty+" -SUG "+sug_qty+" @ "+sales_price).toUpperCase();
						}
						else if(inv_flag.equals("ST"))
						{
							description= (counterparty_abbr+" STORAGECHEXTENDED "+qq_dt).toUpperCase();
						}
						else
						{
							if(cont_type.equals("O")||cont_type.equals("Q")) 
							{
								//gen_for="Re-Gas Capacity";
								gen_for="CN";
							}
							else if(cont_type.equals("Q"))
							{
								//gen_for="Period";
							}
							description = (counterparty_abbr+" "+gen_for+" "+alloc_qty+" MMBTU @ "+sales_price+" "+periodStartDt.split("/")[0]+"-"+periodEndDt).toUpperCase();
						}
						
						String counterpty_category=utilBean.getCounterpartyCategory(conn, counterpty_cd);
						String account_cd="";
						{
							if(cont_type.equals("E")||cont_type.equals("F")||cont_type.equals("S")||cont_type.equals("L"))
							{
								account_cd=getCounterpartySunAccountCode(counterpty_cd, "C", "0","S");
								if(account_cd.equals(""))
								{
									//FOR NON GROUP
									account_cd ="701001";
									if(counterpty_category.equals("Group"))
									{
										//FOR GROUP it should be 701009
										account_cd ="701009";
									}
								}
								
							}
							else if(cont_type.equals("Q")||cont_type.equals("O"))
							{
								//type_of_supply="LTCORA";
								account_cd=getCounterpartySunAccountCode(counterpty_cd, "C", "0","SI");
								if(account_cd.equals(""))
								{
									account_cd ="701003";
									if(counterpty_category.equals("Group"))
									{
										//FOR GROUP it should be 701010
										account_cd ="701010";
									}
								}
							}
							else if(cont_type.equals("X")||cont_type.equals("W"))
							{
								//type_of_supply="IGX";
								account_cd ="701006";
							}
							else if(cont_type.equals("B") || cont_type.equals("M"))
							{
								account_cd=getCounterpartySunAccountCode(counterpty_cd, "C", "0","S");
								if(account_cd.equals(""))
								{
									account_cd="802027";
								}
							}
						}
						
						if(inv_flag.equals("ST"))		//for storage invoice 
						{
							account_cd="701005";
						}
						
						if(invoice_type.equals("TLU"))
						{
							//FOR NON GROUP
							account_cd ="701003";			//AS PER EMAIL BY VAISHNAVI DATE 20251024
							if(counterpty_category.equals("Group"))
							{
								//FOR GROUP 
								account_cd ="701010";
							}
						}
						
						//PB20260117: added for LTCORA DLNG INVOICE SAME AS TLU INVOICE
						if(gen_type.equals("DLNG_SERV"))
						{
							if(cont_type.equals("O")||cont_type.equals("Q"))
							{
								//FOR NON GROUP
								account_cd ="701003";			
								if(counterpty_category.equals("Group"))
								{
									//FOR GROUP 
									account_cd ="701010";
								}
							}
						}
						
						String cccac="";
						if(gen_type.equals("DLNG_SG") || gen_type.equals("DLNG_FFLOW") || gen_type.equals("DLNG_SERV"))
						{
							if(invoice_type.equals("TLU")||cont_type.equals("O")||cont_type.equals("Q"))		//AS PER EMAIL BY VAISHNAVI DATE 20251024
							{
								cccac="1TM01";
							}
							else
							{
								cccac="2BD01";
								if(!bu_state_tin.equals("24"))
								{
									cccac="2"+getStateCd(bu_state_tin)+"01";
								}
							}
						}
						else
						{
							cccac= "1TM01"; //for IGX: cccac="2MK01"
							if(cont_type.equals("X") || cont_type.equals("W"))
							{
								cccac="2MK01";
							}
							if(!bu_state_tin.equals("24"))
							{
								cccac="2"+getStateCd(bu_state_tin)+"01";
							}
						}
						
						//for debit
						String dr_cr_mark="D";
						if(!inv_flag.equals("ST"))
						{
							if(invoice_type.equals("CR")||invoice_type.equals("CCR")||inv_flag.equals("CR"))
							{
								dr_cr_mark="C";
							}
							else
							{
								dr_cr_mark="D";
							}
						}
						for(int i=0;i<2;i++)
						{
							if(inv_flag.equals("UG"))
							{
								if(i%2!=0) 
								{
									break;
								}
							}
							if(criteria.contains("TAXP"))
							{
								if(i==0) 
								{
									continue;
								}
							}
							VINVOICE_NO.add(invoice_no);
							VJOURNAL_TYPE.add(journal_type);
							VAPPROVAL_DT.add(approve_dt);
							VLEDGER.add("A");
							VACCOUNT_CD.add(account_code);
							VPERIOD_START_DT.add(accountPeriod);
							//VPERIOD_END_DT.add(periodEndDt);
							if(inv_flag.equals("UG"))
							{
								VBASE_AMT.add(tax_amt);
								VTRANS_AMT.add(tax_amt);
								VDEBIT_CREDIT.add("D");
								VREPORT_AMT.add("0.00");
								VEXCHNG_RATE.add("0");
							}
							else
							{
								if(i%2==0)
								{
									VBASE_AMT.add(invoice_amt);
									VTRANS_AMT.add(invoice_amt);
								}
								else
								{
									VBASE_AMT.add(tax_amt);
									VTRANS_AMT.add(tax_amt);
								}
								VDEBIT_CREDIT.add(dr_cr_mark);
								if(i%2==0)
								{
									VREPORT_AMT.add(gross_amt_usd);
								}
								else
								{
									VREPORT_AMT.add("0.00");
								}
								VEXCHNG_RATE.add(exchg_rate);
							}
							VCURRENCY_CD.add("INR");
							VINVOICE_DT.add(trans_dt);
							VDESC.add(description);
							VINVOICE_DUE_DT.add(dueDt);
							VCOST_CTR_CD.add("");
							VCOA_CD.add("");
							VEMPLOYEE_CD.add("");
							VCODE.add("");
							VBU_UNIT_CD.add(bucac);
							if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
							{
								VGOOD_SERVICE.add("Service");		//GOODS_SERVICE_FLAG
								VREV_CHARGE.add("0");			//REV_CHARGE
								VHSN_CD.add("999799");		//HSN_SAC
								VPOS_CD.add(buStateNm);	//POS
								VTAX_LINE_AMT.add(tax_amt);		//TAX_AMT
								VSUPPLY_TYPE.add(sup_type);		//SUPPLY_TYPE
								if(inv_flag.equals("UG"))
								{
									VTOTAL_INV_AMT.add(tax_amt);	//TOTAL_INV_AMT
								}
								else
								{
									VTOTAL_INV_AMT.add(invoice_amt);	//TOTAL_INV_AMT
								}
							}
							else
							{
								VGOOD_SERVICE.add("");		//GOODS_SERVICE_FLAG
								VREV_CHARGE.add("");			//REV_CHARGE
								VHSN_CD.add("");		//HSN_SAC
								VPOS_CD.add("");	//POS
								VTAX_LINE_AMT.add("");		//TAX_AMT
								VSUPPLY_TYPE.add("");		//SUPPLY_TYPE
								VTOTAL_INV_AMT.add("");	//TOTAL_INV_AMT
							}
						}
						
						if(!inv_flag.equals("UG") && !criteria.contains("TAXP"))
						{
							//for credit
							dr_cr_mark="C";
							if(invoice_type.equals("CR")||invoice_type.equals("CCR")||inv_flag.equals("CR"))
							{
								dr_cr_mark="D";
							}
							else
							{
								dr_cr_mark="C";
							}
							
							if(criteria.contains("OC") || criteria.contains("MM") || criteria.contains("TC"))
							{
								if(criteria.contains("MM"))
								{
									if(!market_margin_amt.equals(""))
									{
										String temp_gross_amt=nf.format(Math.abs(Double.parseDouble(market_margin_amt)));
										VINVOICE_NO.add(invoice_no);
										VJOURNAL_TYPE.add(journal_type);
										VAPPROVAL_DT.add(approve_dt);
										VLEDGER.add("A");
										VACCOUNT_CD.add(account_cd);
										VPERIOD_START_DT.add(accountPeriod);
										//VPERIOD_END_DT.add(periodEndDt);
										VBASE_AMT.add(temp_gross_amt);
										VTRANS_AMT.add(temp_gross_amt);
										VDEBIT_CREDIT.add(dr_cr_mark);
										VREPORT_AMT.add(gross_amt_usd);
										VCURRENCY_CD.add("INR");
										VEXCHNG_RATE.add(exchg_rate);
										VINVOICE_DT.add(trans_dt);
										VDESC.add(description);
										VINVOICE_DUE_DT.add(dueDt);
										VCOST_CTR_CD.add(cccac);
										VCOA_CD.add("");
										VEMPLOYEE_CD.add("");
										VCODE.add("NA");
										VBU_UNIT_CD.add(bucac);
										if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
										{
											VGOOD_SERVICE.add("Service");		//GOODS_SERVICE_FLAG
											VREV_CHARGE.add("0");			//REV_CHARGE
											VHSN_CD.add("999799");		//HSN_SAC
											VPOS_CD.add(buStateNm);	//POS
											VTAX_LINE_AMT.add(tax_amt);		//TAX_AMT
											VSUPPLY_TYPE.add(sup_type);		//SUPPLY_TYPE
											if(inv_flag.equals("UG"))
											{
												VTOTAL_INV_AMT.add(tax_amt);	//TOTAL_INV_AMT
											}
											else
											{
												VTOTAL_INV_AMT.add(invoice_amt);	//TOTAL_INV_AMT
											}
										}
										else
										{
											VGOOD_SERVICE.add("");		//GOODS_SERVICE_FLAG
											VREV_CHARGE.add("");			//REV_CHARGE
											VHSN_CD.add("");		//HSN_SAC
											VPOS_CD.add("");	//POS
											VTAX_LINE_AMT.add("");		//TAX_AMT
											VSUPPLY_TYPE.add("");		//SUPPLY_TYPE
											VTOTAL_INV_AMT.add("");	//TOTAL_INV_AMT
										}
									}
								}
								if(criteria.contains("OC"))
								{
									if(!oth_amt.equals(""))
									{
										String temp_gross_amt=nf.format(Math.abs(Double.parseDouble(oth_amt)));
										VINVOICE_NO.add(invoice_no);
										VJOURNAL_TYPE.add(journal_type);
										VAPPROVAL_DT.add(approve_dt);
										VLEDGER.add("A");
										VACCOUNT_CD.add(account_cd);
										VPERIOD_START_DT.add(accountPeriod);
										//VPERIOD_END_DT.add(periodEndDt);
										VBASE_AMT.add(temp_gross_amt);
										VTRANS_AMT.add(temp_gross_amt);
										VDEBIT_CREDIT.add(dr_cr_mark);
										VREPORT_AMT.add(gross_amt_usd);
										VCURRENCY_CD.add("INR");
										VEXCHNG_RATE.add(exchg_rate);
										VINVOICE_DT.add(trans_dt);
										VDESC.add(description);
										VINVOICE_DUE_DT.add(dueDt);
										VCOST_CTR_CD.add(cccac);
										VCOA_CD.add("");
										VEMPLOYEE_CD.add("");
										VCODE.add("NA");
										VBU_UNIT_CD.add(bucac);
										if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
										{
											VGOOD_SERVICE.add("Service");		//GOODS_SERVICE_FLAG
											VREV_CHARGE.add("0");			//REV_CHARGE
											VHSN_CD.add("999799");		//HSN_SAC
											VPOS_CD.add(buStateNm);	//POS
											VTAX_LINE_AMT.add(tax_amt);		//TAX_AMT
											VSUPPLY_TYPE.add(sup_type);		//SUPPLY_TYPE
											if(inv_flag.equals("UG"))
											{
												VTOTAL_INV_AMT.add(tax_amt);	//TOTAL_INV_AMT
											}
											else
											{
												VTOTAL_INV_AMT.add(invoice_amt);	//TOTAL_INV_AMT
											}
										}
										else
										{
											VGOOD_SERVICE.add("");		//GOODS_SERVICE_FLAG
											VREV_CHARGE.add("");			//REV_CHARGE
											VHSN_CD.add("");		//HSN_SAC
											VPOS_CD.add("");	//POS
											VTAX_LINE_AMT.add("");		//TAX_AMT
											VSUPPLY_TYPE.add("");		//SUPPLY_TYPE
											VTOTAL_INV_AMT.add("");	//TOTAL_INV_AMT
										}
									}
								}
								if(criteria.contains("TC"))
								{
									if(!transportationAmt.equals(""))
									{
										String temp_gross_amt=nf.format(Math.abs(Double.parseDouble(transportationAmt)));
										VINVOICE_NO.add(invoice_no);
										VJOURNAL_TYPE.add(journal_type);
										VAPPROVAL_DT.add(approve_dt);
										VLEDGER.add("A");
										VACCOUNT_CD.add(trans_account_cd);
										VPERIOD_START_DT.add(accountPeriod);
										//VPERIOD_END_DT.add(periodEndDt);
										VBASE_AMT.add(temp_gross_amt);
										VTRANS_AMT.add(temp_gross_amt);
										VDEBIT_CREDIT.add(dr_cr_mark);
										VREPORT_AMT.add(gross_amt_usd);
										VCURRENCY_CD.add("INR");
										VEXCHNG_RATE.add(exchg_rate);
										VINVOICE_DT.add(trans_dt);
										VDESC.add(description);
										VINVOICE_DUE_DT.add(dueDt);
										VCOST_CTR_CD.add(cccac);
										VCOA_CD.add("");
										VEMPLOYEE_CD.add("");
										VCODE.add("NA");
										VBU_UNIT_CD.add(bucac);
										if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
										{
											VGOOD_SERVICE.add("Service");		//GOODS_SERVICE_FLAG
											VREV_CHARGE.add("0");			//REV_CHARGE
											VHSN_CD.add("999799");		//HSN_SAC
											VPOS_CD.add(buStateNm);	//POS
											VTAX_LINE_AMT.add(tax_amt);		//TAX_AMT
											VSUPPLY_TYPE.add(sup_type);		//SUPPLY_TYPE
											if(inv_flag.equals("UG"))
											{
												VTOTAL_INV_AMT.add(tax_amt);	//TOTAL_INV_AMT
											}
											else
											{
												VTOTAL_INV_AMT.add(invoice_amt);	//TOTAL_INV_AMT
											}
										}
										else
										{
											VGOOD_SERVICE.add("");		//GOODS_SERVICE_FLAG
											VREV_CHARGE.add("");			//REV_CHARGE
											VHSN_CD.add("");		//HSN_SAC
											VPOS_CD.add("");	//POS
											VTAX_LINE_AMT.add("");		//TAX_AMT
											VSUPPLY_TYPE.add("");		//SUPPLY_TYPE
											VTOTAL_INV_AMT.add("");	//TOTAL_INV_AMT
										}
									}
								}
							}
							else
							{
								VINVOICE_NO.add(invoice_no);
								VJOURNAL_TYPE.add(journal_type);
								VAPPROVAL_DT.add(approve_dt);
								VLEDGER.add("A");
								VACCOUNT_CD.add(account_cd);
								VPERIOD_START_DT.add(accountPeriod);
								//VPERIOD_END_DT.add(periodEndDt);
								VBASE_AMT.add(gross_amt);
								VTRANS_AMT.add(gross_amt);
								VDEBIT_CREDIT.add(dr_cr_mark);
								VREPORT_AMT.add(gross_amt_usd);
								VCURRENCY_CD.add("INR");
								VEXCHNG_RATE.add(exchg_rate);
								VINVOICE_DT.add(trans_dt);
								VDESC.add(description);
								VINVOICE_DUE_DT.add(dueDt);
								VCOST_CTR_CD.add(cccac);
								VCOA_CD.add("");
								VEMPLOYEE_CD.add("");
								VCODE.add("NA");
								VBU_UNIT_CD.add(bucac);
								if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
								{
									VGOOD_SERVICE.add("Service");		//GOODS_SERVICE_FLAG
									VREV_CHARGE.add("0");			//REV_CHARGE
									VHSN_CD.add("999799");		//HSN_SAC
									VPOS_CD.add(buStateNm);	//POS
									VTAX_LINE_AMT.add(tax_amt);		//TAX_AMT
									VSUPPLY_TYPE.add(sup_type);		//SUPPLY_TYPE
									if(inv_flag.equals("UG"))
									{
										VTOTAL_INV_AMT.add(tax_amt);	//TOTAL_INV_AMT
									}
									else
									{
										VTOTAL_INV_AMT.add(invoice_amt);	//TOTAL_INV_AMT
									}
								}
								else
								{
									VGOOD_SERVICE.add("");		//GOODS_SERVICE_FLAG
									VREV_CHARGE.add("");			//REV_CHARGE
									VHSN_CD.add("");		//HSN_SAC
									VPOS_CD.add("");	//POS
									VTAX_LINE_AMT.add("");		//TAX_AMT
									VSUPPLY_TYPE.add("");		//SUPPLY_TYPE
									VTOTAL_INV_AMT.add("");	//TOTAL_INV_AMT
								}
							}
						}
						
						dr_cr_mark="C";
						if(!inv_flag.equals("ST"))
						{
							if(invoice_type.equals("CR") || invoice_type.equals("CCR") ||inv_flag.equals("CR"))
							{
								dr_cr_mark="D";
							}
							else
							{
								dr_cr_mark="C";
							}
						}
						String queryString3="";
						if(gen_type.equals("SG"))
						{
							queryString3="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=?";
							stmt1=conn.prepareStatement(queryString3);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, fin_year);
							stmt1.setString(3, invoice_seq);
							stmt1.setString(4, bu_state_tin);
							rset1=stmt1.executeQuery();
						}
						else if(gen_type.equals("FFLOW"))
						{
							queryString3="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_FFLOW_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? "
									+ "AND INVOICE_TYPE=?";
							stmt1=conn.prepareStatement(queryString3);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, fin_year);
							stmt1.setString(3, invoice_seq);
							stmt1.setString(4, bu_state_tin);
							stmt1.setString(5, invoice_type);
							rset1=stmt1.executeQuery();
						}
						else if(gen_type.equals("DLNG_SG"))
						{
							queryString3="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT  "
									+ "FROM FMS_DLNG_INV_TAX_DTL  "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? ";
							stmt1=conn.prepareStatement(queryString3);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, fin_year);
							stmt1.setString(3, invoice_seq);
							stmt1.setString(4, bu_state_tin);
							rset1=stmt1.executeQuery();
						}
						else if(gen_type.equals("DLNG_FFLOW"))
						{
							queryString3="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_DLNG_FFLOW_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? "
									+ "AND INVOICE_TYPE=?";
							stmt1=conn.prepareStatement(queryString3);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, fin_year);
							stmt1.setString(3, invoice_seq);
							stmt1.setString(4, bu_state_tin);
							stmt1.setString(5, invoice_type);
							rset1=stmt1.executeQuery();
						}
						else if(gen_type.equals("DLNG_SERV"))
						{
							queryString3="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_DLNG_SVC_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? ";
							stmt1=conn.prepareStatement(queryString3);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, fin_year);
							stmt1.setString(3, invoice_seq);
							stmt1.setString(4, bu_state_tin);
							rset1=stmt1.executeQuery();
						}
						while(rset1.next())
						{
							String tax_code=rset1.getString(1)==null?"":rset1.getString(1);
							String taxStrctCd=rset1.getString(2)==null?"":rset1.getString(2);
							String taxAmt=rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
							String taxBaseAmt=rset1.getString(4)==null?"":nf.format(rset1.getDouble(4));
							
							taxAmt=taxAmt.equals("")?"":nf.format(Double.parseDouble(taxAmt)<0?-1*Double.parseDouble(taxAmt):Double.parseDouble(taxAmt));
							
							String acc_cd = "";
							if(inv_flag.equals("UG"))
							{
								acc_cd=getTaxStructureSunAccountCd(taxStrctCd,tax_code, bu_state_tin, bu_unit,"UG");
							}
							else
							{
								acc_cd=getTaxStructureSunAccountCd(taxStrctCd,tax_code, bu_state_tin, bu_unit,"S");
							}
							
							VINVOICE_NO.add(invoice_no);
							VJOURNAL_TYPE.add(journal_type);
							VAPPROVAL_DT.add(approve_dt);
							VLEDGER.add("A");
							VACCOUNT_CD.add(acc_cd);
							VPERIOD_START_DT.add(accountPeriod);
							//VPERIOD_END_DT.add(periodEndDt);
							VBASE_AMT.add(taxAmt);
							VTRANS_AMT.add(taxAmt);
							if(inv_flag.equals("UG"))
							{
								VDEBIT_CREDIT.add("C");
							}
							else
							{
								VDEBIT_CREDIT.add(dr_cr_mark);
							}
							VREPORT_AMT.add("0.00");
							VCURRENCY_CD.add("INR");
							VEXCHNG_RATE.add(exchg_rate);
							VINVOICE_DT.add(trans_dt);
							VDESC.add(description);
							VINVOICE_DUE_DT.add(dueDt);
							VCOST_CTR_CD.add("");
							if(invoice_type.equals("LP"))
							{
								VCOA_CD.add(latePaymentAcc_cd);
							}
							else
							{
								VCOA_CD.add(account_cd);
							}
							VCODE.add("");
							VEMPLOYEE_CD.add("");
							VBU_UNIT_CD.add(bucac);
							if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
							{
								VGOOD_SERVICE.add("Service");		//GOODS_SERVICE_FLAG
								VREV_CHARGE.add("0");			//REV_CHARGE
								VHSN_CD.add("999799");		//HSN_SAC
								VPOS_CD.add(buStateNm);	//POS
								VTAX_LINE_AMT.add(tax_amt);		//TAX_AMT
								VSUPPLY_TYPE.add(sup_type);		//SUPPLY_TYPE
								if(inv_flag.equals("UG"))
								{
									VTOTAL_INV_AMT.add(tax_amt);	//TOTAL_INV_AMT
								}
								else
								{
									VTOTAL_INV_AMT.add(invoice_amt);	//TOTAL_INV_AMT
								}
							}
							else
							{
								VGOOD_SERVICE.add("");		//GOODS_SERVICE_FLAG
								VREV_CHARGE.add("");			//REV_CHARGE
								VHSN_CD.add("");		//HSN_SAC
								VPOS_CD.add("");	//POS
								VTAX_LINE_AMT.add("");		//TAX_AMT
								VSUPPLY_TYPE.add("");		//SUPPLY_TYPE
								VTOTAL_INV_AMT.add("");	//TOTAL_INV_AMT
							}
							
						}
						rset1.close();
						stmt1.close();
						
						//PB 20250630: for IGX
						//The Invoice amount and the tax amount is credited to the customer account code
						//The above amount are debited into IGX account code 
						if((cont_type.equals("X") || cont_type.equals("W")) && !criteria.contains("TAXP"))
						{
							//Credit into the customer's account 
							dr_cr_mark="C";
							
							/*
							 * if(invoice_type.equals("CR")||invoice_type.equals("CCR")) { dr_cr_mark="C"; }
							 * else { dr_cr_mark="D"; }
							 */
							 
							for(int i=0;i<2;i++)
							{
								VINVOICE_NO.add(invoice_no);
								VJOURNAL_TYPE.add(journal_type);
								VAPPROVAL_DT.add(approve_dt);
								VLEDGER.add("A");
								VACCOUNT_CD.add(account_code);
								VPERIOD_START_DT.add(accountPeriod);
								//VPERIOD_END_DT.add(periodEndDt);
								if(i%2==0)
								{
									VBASE_AMT.add(invoice_amt);
									VTRANS_AMT.add(invoice_amt);
								}
								else
								{
									VBASE_AMT.add(tax_amt);
									VTRANS_AMT.add(tax_amt);
								}
								VDEBIT_CREDIT.add(dr_cr_mark);
								if(i%2==0)
								{
									VREPORT_AMT.add(gross_amt_usd);
								}
								else
								{
									VREPORT_AMT.add("0.00");
								}
								VCURRENCY_CD.add("INR");
								VEXCHNG_RATE.add(exchg_rate);
								VINVOICE_DT.add(trans_dt);
								VDESC.add(description);
								VINVOICE_DUE_DT.add(dueDt);
								VCOST_CTR_CD.add("");
								VCOA_CD.add("");
								VCODE.add("");
								VEMPLOYEE_CD.add("");
								VBU_UNIT_CD.add(bucac);
								if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
								{
									VGOOD_SERVICE.add("Service");		//GOODS_SERVICE_FLAG
									VREV_CHARGE.add("0");			//REV_CHARGE
									VHSN_CD.add("999799");		//HSN_SAC
									VPOS_CD.add(buStateNm);	//POS
									VTAX_LINE_AMT.add(tax_amt);		//TAX_AMT
									VSUPPLY_TYPE.add(sup_type);		//SUPPLY_TYPE
									if(inv_flag.equals("UG"))
									{
										VTOTAL_INV_AMT.add(tax_amt);	//TOTAL_INV_AMT
									}
									else
									{
										VTOTAL_INV_AMT.add(invoice_amt);	//TOTAL_INV_AMT
									}
								}
								else
								{
									VGOOD_SERVICE.add("");		//GOODS_SERVICE_FLAG
									VREV_CHARGE.add("");			//REV_CHARGE
									VHSN_CD.add("");		//HSN_SAC
									VPOS_CD.add("");	//POS
									VTAX_LINE_AMT.add("");		//TAX_AMT
									VSUPPLY_TYPE.add("");		//SUPPLY_TYPE
									VTOTAL_INV_AMT.add("");	//TOTAL_INV_AMT
								}
							}
							
							//Debit into the IGX account 
							dr_cr_mark="D";
							/*
							 * if(invoice_type.equals("CR")||invoice_type.equals("CCR")) { dr_cr_mark="C"; }
							 * else { dr_cr_mark="D"; }
							 */
							for(int i=0;i<2;i++)
							{
								VINVOICE_NO.add(invoice_no);
								VJOURNAL_TYPE.add(journal_type);
								VAPPROVAL_DT.add(approve_dt);
								VLEDGER.add("A");
								VACCOUNT_CD.add(igx_account_cd);
								VPERIOD_START_DT.add(accountPeriod);
								//VPERIOD_END_DT.add(periodEndDt);
								if(i%2==0)
								{
									VBASE_AMT.add(invoice_amt);
									VTRANS_AMT.add(invoice_amt);
								}
								else
								{
									VBASE_AMT.add(tax_amt);
									VTRANS_AMT.add(tax_amt);
								}
								VDEBIT_CREDIT.add(dr_cr_mark);
								if(i%2==0)
								{
									VREPORT_AMT.add(gross_amt_usd);
								}
								else
								{
									VREPORT_AMT.add("0.00");
								}
								VCURRENCY_CD.add("INR");
								VEXCHNG_RATE.add(exchg_rate);
								VINVOICE_DT.add(trans_dt);
								VDESC.add(description);
								VINVOICE_DUE_DT.add(dueDt);
								if(i%2==0)
								{
									VCOST_CTR_CD.add(cccac);
									VCODE.add("NA");
								}
								else
								{
									VCOST_CTR_CD.add("");
									VCODE.add("");
								}
								if(invoice_type.equals("LP"))
								{
									VCOA_CD.add(latePaymentAcc_cd);
								}
								else
								{
									VCOA_CD.add(account_cd);
								}
								VEMPLOYEE_CD.add("");
								VBU_UNIT_CD.add(bucac);
								if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
								{
									VGOOD_SERVICE.add("Service");		//GOODS_SERVICE_FLAG
									VREV_CHARGE.add("0");			//REV_CHARGE
									VHSN_CD.add("999799");		//HSN_SAC
									VPOS_CD.add(buStateNm);	//POS
									VTAX_LINE_AMT.add(tax_amt);		//TAX_AMT
									VSUPPLY_TYPE.add(sup_type);		//SUPPLY_TYPE
									if(inv_flag.equals("UG"))
									{
										VTOTAL_INV_AMT.add(tax_amt);	//TOTAL_INV_AMT
									}
									else
									{
										VTOTAL_INV_AMT.add(invoice_amt);	//TOTAL_INV_AMT
									}
								}
								else
								{
									VGOOD_SERVICE.add("");		//GOODS_SERVICE_FLAG
									VREV_CHARGE.add("");			//REV_CHARGE
									VHSN_CD.add("");		//HSN_SAC
									VPOS_CD.add("");	//POS
									VTAX_LINE_AMT.add("");		//TAX_AMT
									VSUPPLY_TYPE.add("");		//SUPPLY_TYPE
									VTOTAL_INV_AMT.add("");	//TOTAL_INV_AMT
								}
							}
						}
						
						if(!invoice_type.equals("CR")&&!invoice_type.equals("DR")&&!invoice_type.equals("CCR")&&!invoice_type.equals("CDR") &&!inv_flag.equals("UG")&&!inv_flag.equals("ST") && !criteria.contains("TAXP"))
						{
							if(criteria.equals("")||criteria.contains("QTY"))
							{
								String dc_marker="C";
								if(inv_flag.equals("CR"))
								{
									dc_marker="D";
								}
								else
								{
									dc_marker="C";
								}
								//if(agmt_base.equals("D"))
								{
									if(!transportationAmt.equals(""))
									{
										for(int i=0;i<2;i++)
										{
											VINVOICE_NO.add(invoice_no);
											VJOURNAL_TYPE.add(journal_type);
											VAPPROVAL_DT.add(approve_dt);
											VLEDGER.add("A");
											if(i%2==0)
											{
												VACCOUNT_CD.add(trans_account_cd);
											}
											else
											{
												VACCOUNT_CD.add(account_code);
											}
											VPERIOD_START_DT.add(accountPeriod);
											//VPERIOD_END_DT.add(periodEndDt);
											VBASE_AMT.add(nf.format(Math.abs(Double.parseDouble(transportationAmt))));
											VTRANS_AMT.add(nf.format(Math.abs(Double.parseDouble(transportationAmt))));
											if(i%2==0)
											{
												VDEBIT_CREDIT.add(dc_marker);
											}
											else
											{
												VDEBIT_CREDIT.add(dc_marker.equals("C")?"D":"C");
											}
											VREPORT_AMT.add("0.00");
											VCURRENCY_CD.add("INR");
											VEXCHNG_RATE.add(exchg_rate);
											VINVOICE_DT.add(trans_dt);
											VDESC.add(description);
											VINVOICE_DUE_DT.add(dueDt);
											if(i%2==0)
											{
												VCOST_CTR_CD.add(cccac);
											}
											else
											{
												VCOST_CTR_CD.add("");
											}
											VCOA_CD.add(account_cd);
											VCODE.add("");
											VEMPLOYEE_CD.add("");
											VBU_UNIT_CD.add(bucac);
											if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
											{
												VGOOD_SERVICE.add("Service");		//GOODS_SERVICE_FLAG
												VREV_CHARGE.add("0");			//REV_CHARGE
												VHSN_CD.add("999799");		//HSN_SAC
												VPOS_CD.add(buStateNm);	//POS
												VTAX_LINE_AMT.add(tax_amt);		//TAX_AMT
												VSUPPLY_TYPE.add(sup_type);		//SUPPLY_TYPE
												if(inv_flag.equals("UG"))
												{
													VTOTAL_INV_AMT.add(tax_amt);	//TOTAL_INV_AMT
												}
												else
												{
													VTOTAL_INV_AMT.add(invoice_amt);	//TOTAL_INV_AMT
												}
											}
											else
											{
												VGOOD_SERVICE.add("");		//GOODS_SERVICE_FLAG
												VREV_CHARGE.add("");			//REV_CHARGE
												VHSN_CD.add("");		//HSN_SAC
												VPOS_CD.add("");	//POS
												VTAX_LINE_AMT.add("");		//TAX_AMT
												VSUPPLY_TYPE.add("");		//SUPPLY_TYPE
												VTOTAL_INV_AMT.add("");	//TOTAL_INV_AMT
											}
										}
									}
								}
							}
						}
						
						//For TCS
						if(!tcs_tds.equals("") &&!inv_flag.equals("UG") &&!inv_flag.equals("CR") &&!inv_flag.equals("DR") && !criteria.contains("TAXP"))
						{
							if(tcs_tds.equals("TCS"))
							{
								String ac_cd = getTcsTdsStructureSunAccountCd(tcsStructCd, bu_state_tin, bu_unit);
								for(int i=0;i<2;i++)
								{
									VINVOICE_NO.add(invoice_no);
									VJOURNAL_TYPE.add(journal_type);
									VAPPROVAL_DT.add(approve_dt);
									VLEDGER.add("A");
									if(i%2==0)
									{
										VACCOUNT_CD.add(ac_cd);
									}
									else
									{
										VACCOUNT_CD.add(account_code);
									}
									VPERIOD_START_DT.add(accountPeriod);
									//VPERIOD_END_DT.add(periodEndDt);
									VBASE_AMT.add(tcs_amt);
									VTRANS_AMT.add(tcs_amt);
									if(i%2==0)
									{
										VDEBIT_CREDIT.add("C");
									}
									else
									{
										VDEBIT_CREDIT.add("D");
									}
									VREPORT_AMT.add("0.00");
									VCURRENCY_CD.add("INR");
									VEXCHNG_RATE.add(exchg_rate);
									VINVOICE_DT.add(trans_dt);
									VDESC.add(description);
									VINVOICE_DUE_DT.add(dueDt);
									VCOST_CTR_CD.add("");
									if(invoice_type.equals("LP"))
									{
										VCOA_CD.add(latePaymentAcc_cd);
									}
									else
									{
										VCOA_CD.add(account_cd);
									}
									VCODE.add("");
									VEMPLOYEE_CD.add("");
									VBU_UNIT_CD.add(bucac);
									if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
									{
										VGOOD_SERVICE.add("Service");		//GOODS_SERVICE_FLAG
										VREV_CHARGE.add("0");			//REV_CHARGE
										VHSN_CD.add("999799");		//HSN_SAC
										VPOS_CD.add(buStateNm);	//POS
										VTAX_LINE_AMT.add(tax_amt);		//TAX_AMT
										VSUPPLY_TYPE.add(sup_type);		//SUPPLY_TYPE
										if(inv_flag.equals("UG"))
										{
											VTOTAL_INV_AMT.add(tax_amt);	//TOTAL_INV_AMT
										}
										else
										{
											VTOTAL_INV_AMT.add(invoice_amt);	//TOTAL_INV_AMT
										}
									}
									else
									{
										VGOOD_SERVICE.add("");		//GOODS_SERVICE_FLAG
										VREV_CHARGE.add("");			//REV_CHARGE
										VHSN_CD.add("");		//HSN_SAC
										VPOS_CD.add("");	//POS
										VTAX_LINE_AMT.add("");		//TAX_AMT
										VSUPPLY_TYPE.add("");		//SUPPLY_TYPE
										VTOTAL_INV_AMT.add("");	//TOTAL_INV_AMT
									}
								}
							}
						}
						
						if(!invoice_type.equals("CR")&&!invoice_type.equals("DR")&&!invoice_type.equals("CCR")&&!invoice_type.equals("CDR")  &&!inv_flag.equals("UG")&&!inv_flag.equals("ST")&&!inv_flag.equals("CR")&&!inv_flag.equals("DR") && !criteria.contains("TAXP"))
						{
							if(!market_margin_amt.equals(""))
							{
								//String acc_cd=getCounterpartySunAccountCode(counterpty_cd, "C", "0","S");
								VINVOICE_NO.add(invoice_no);
								VJOURNAL_TYPE.add(journal_type);
								VAPPROVAL_DT.add(approve_dt);
								VLEDGER.add("A");
								VACCOUNT_CD.add(account_cd);
								VPERIOD_START_DT.add(accountPeriod);
								//VPERIOD_END_DT.add(periodEndDt);
								VBASE_AMT.add(nf.format(Math.abs(Double.parseDouble(market_margin_amt))));
								VTRANS_AMT.add(nf.format(Math.abs(Double.parseDouble(market_margin_amt))));
								VDEBIT_CREDIT.add("C");
								VREPORT_AMT.add("0.00");
								VCURRENCY_CD.add("INR");
								VEXCHNG_RATE.add(exchg_rate);
								VINVOICE_DT.add(trans_dt);
								VDESC.add(description);
								VINVOICE_DUE_DT.add(dueDt);
								VCOST_CTR_CD.add(cccac);
								VCOA_CD.add(account_cd);
								VCODE.add("");
								VEMPLOYEE_CD.add("");
								VBU_UNIT_CD.add(bucac);
								if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
								{
									VGOOD_SERVICE.add("Service");		//GOODS_SERVICE_FLAG
									VREV_CHARGE.add("0");			//REV_CHARGE
									VHSN_CD.add("999799");		//HSN_SAC
									VPOS_CD.add(buStateNm);	//POS
									VTAX_LINE_AMT.add(tax_amt);		//TAX_AMT
									VSUPPLY_TYPE.add(sup_type);		//SUPPLY_TYPE
									VTOTAL_INV_AMT.add(invoice_amt);	//TOTAL_INV_AMT
								}
								else
								{
									VGOOD_SERVICE.add("");		//GOODS_SERVICE_FLAG
									VREV_CHARGE.add("");			//REV_CHARGE
									VHSN_CD.add("");		//HSN_SAC
									VPOS_CD.add("");	//POS
									VTAX_LINE_AMT.add("");		//TAX_AMT
									VSUPPLY_TYPE.add("");		//SUPPLY_TYPE
									VTOTAL_INV_AMT.add("");	//TOTAL_INV_AMT
								}
							}
							if(!oth_amt.equals(""))
							{
								//String acc_cd=getCounterpartySunAccountCode(counterpty_cd, "C", "0","S");
								VINVOICE_NO.add(invoice_no);
								VJOURNAL_TYPE.add(journal_type);
								VAPPROVAL_DT.add(approve_dt);
								VLEDGER.add("A");
								VACCOUNT_CD.add(account_cd);
								VPERIOD_START_DT.add(accountPeriod);
								//VPERIOD_END_DT.add(periodEndDt);
								VBASE_AMT.add(nf.format(Math.abs(Double.parseDouble(oth_amt))));
								VTRANS_AMT.add(nf.format(Math.abs(Double.parseDouble(oth_amt))));
								VDEBIT_CREDIT.add("C");
								VREPORT_AMT.add("0.00");
								VCURRENCY_CD.add("INR");
								VEXCHNG_RATE.add(exchg_rate);
								VINVOICE_DT.add(trans_dt);
								VDESC.add(description);
								VINVOICE_DUE_DT.add(dueDt);
								VCOST_CTR_CD.add(cccac);
								VCOA_CD.add(account_cd);
								VCODE.add("");
								VEMPLOYEE_CD.add("");
								VBU_UNIT_CD.add(bucac);
								if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
								{
									VGOOD_SERVICE.add("Service");		//GOODS_SERVICE_FLAG
									VREV_CHARGE.add("0");			//REV_CHARGE
									VHSN_CD.add("999799");		//HSN_SAC
									VPOS_CD.add(buStateNm);	//POS
									VTAX_LINE_AMT.add(tax_amt);		//TAX_AMT
									VSUPPLY_TYPE.add(sup_type);		//SUPPLY_TYPE
									if(inv_flag.equals("UG"))
									{
										VTOTAL_INV_AMT.add(tax_amt);	//TOTAL_INV_AMT
									}
									else
									{
										VTOTAL_INV_AMT.add(invoice_amt);	//TOTAL_INV_AMT
									}
								}
								else
								{
									VGOOD_SERVICE.add("");		//GOODS_SERVICE_FLAG
									VREV_CHARGE.add("");			//REV_CHARGE
									VHSN_CD.add("");		//HSN_SAC
									VPOS_CD.add("");	//POS
									VTAX_LINE_AMT.add("");		//TAX_AMT
									VSUPPLY_TYPE.add("");		//SUPPLY_TYPE
									VTOTAL_INV_AMT.add("");	//TOTAL_INV_AMT
								}
							}
						}
						if(inv_flag.equals("UG"))
						{
							String acc_cd=getCounterpartySunAccountCode(counterpty_cd, "C", "0","UG");
							if(!acc_cd.equals(""))
							{
								//Credit line 
								VINVOICE_NO.add(invoice_no);
								VJOURNAL_TYPE.add(journal_type);
								VAPPROVAL_DT.add(approve_dt);
								VLEDGER.add("A");
								VACCOUNT_CD.add(account_code);
								VPERIOD_START_DT.add(accountPeriod);
								//VPERIOD_END_DT.add(periodEndDt);
								VBASE_AMT.add(tax_amt);
								VTRANS_AMT.add(tax_amt);
								VDEBIT_CREDIT.add("C");
								VREPORT_AMT.add("0.00");
								VEXCHNG_RATE.add("0");
								VCURRENCY_CD.add("INR");
								VINVOICE_DT.add(trans_dt);
								VDESC.add(description);
								VINVOICE_DUE_DT.add(dueDt);
								VCOST_CTR_CD.add("");
								VCOA_CD.add("");
								VCODE.add("");
								VEMPLOYEE_CD.add("");
								VBU_UNIT_CD.add(bucac);
								if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
								{
									VGOOD_SERVICE.add("Service");		//GOODS_SERVICE_FLAG
									VREV_CHARGE.add("0");			//REV_CHARGE
									VHSN_CD.add("999799");		//HSN_SAC
									VPOS_CD.add(buStateNm);	//POS
									VTAX_LINE_AMT.add(tax_amt);		//TAX_AMT
									VSUPPLY_TYPE.add(sup_type);		//SUPPLY_TYPE
									VTOTAL_INV_AMT.add(tax_amt);	//TOTAL_INV_AMT
								}
								else
								{
									VGOOD_SERVICE.add("");		//GOODS_SERVICE_FLAG
									VREV_CHARGE.add("");			//REV_CHARGE
									VHSN_CD.add("");		//HSN_SAC
									VPOS_CD.add("");	//POS
									VTAX_LINE_AMT.add("");		//TAX_AMT
									VSUPPLY_TYPE.add("");		//SUPPLY_TYPE
									VTOTAL_INV_AMT.add("");	//TOTAL_INV_AMT
								}
								
								//Debit Line 
								VINVOICE_NO.add(invoice_no);
								VJOURNAL_TYPE.add(journal_type);
								VAPPROVAL_DT.add(approve_dt);
								VLEDGER.add("A");
								VACCOUNT_CD.add(acc_cd);
								VPERIOD_START_DT.add(accountPeriod);
								//VPERIOD_END_DT.add(periodEndDt);
								VBASE_AMT.add(tax_amt);
								VTRANS_AMT.add(tax_amt);
								VDEBIT_CREDIT.add("D");
								VREPORT_AMT.add("0.00");
								VEXCHNG_RATE.add("0");
								VCURRENCY_CD.add("INR");
								VINVOICE_DT.add(trans_dt);
								VDESC.add(description);
								VINVOICE_DUE_DT.add(dueDt);
								VCOST_CTR_CD.add(cccac);
								VCOA_CD.add(acc_cd);
								VCODE.add("");
								VBU_UNIT_CD.add(bucac);
								VEMPLOYEE_CD.add("LNA");
								if(!invoice_type.equals("TLU")&&!gen_type.equals("DLNG_SERV")&&(cont_type.equals("O") || cont_type.equals("Q")))
								{
									VGOOD_SERVICE.add("Service");		//GOODS_SERVICE_FLAG
									VREV_CHARGE.add("0");			//REV_CHARGE
									VHSN_CD.add("999799");		//HSN_SAC
									VPOS_CD.add(buStateNm);	//POS
									VTAX_LINE_AMT.add(tax_amt);		//TAX_AMT
									VSUPPLY_TYPE.add(sup_type);		//SUPPLY_TYPE
									VTOTAL_INV_AMT.add(tax_amt);	//TOTAL_INV_AMT
								}
								else
								{
									VGOOD_SERVICE.add("");		//GOODS_SERVICE_FLAG
									VREV_CHARGE.add("");			//REV_CHARGE
									VHSN_CD.add("");		//HSN_SAC
									VPOS_CD.add("");	//POS
									VTAX_LINE_AMT.add("");		//TAX_AMT
									VSUPPLY_TYPE.add("");		//SUPPLY_TYPE
									VTOTAL_INV_AMT.add("");	//TOTAL_INV_AMT
								}
							}
						}
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
	
	public void getXmlFilesList()
	{
		String function_nm="getXmlFilesList()";
		try
		{
			String queryString="SELECT DISTINCT FILE_NAME,NULL,NULL FROM FMS_INV_FILE_DTL "
					+ "WHERE COMPANY_CD=? AND PDF_TYPE=? "
					+ "AND TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') ";
			queryString+="UNION ";
			queryString+="SELECT DISTINCT FILE_NAME,NULL,NULL FROM FMS_FFLOW_INV_FILE_DTL "
					+ "WHERE COMPANY_CD=? AND PDF_TYPE=? "
					+ "AND TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') ";
			queryString+="UNION ";
			queryString+="SELECT DISTINCT FILE_NAME,INV_FLAG,NULL FROM FMS_PUR_INV_FILE_DTL "
					+ "WHERE COMPANY_CD=? AND INV_TITLE=? "
					+ "AND TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') ";
			queryString+="UNION ";
			queryString+="SELECT DISTINCT A.FILE_NAME,A.INVOICE_TYPE,B.INV_HEAD FROM FMS_PUR_FFLOW_INV_FILE_DTL A, FMS_PUR_FFLOW_INV_MST B "
					+ "WHERE A.COMPANY_CD=? AND A.INV_TITLE=? "
					+ "AND TO_DATE(TO_CHAR(A.ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY')  "
					+ "AND TO_DATE(TO_CHAR(A.ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.INVOICE_SEQ=B.INVOICE_SEQ "
					+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.INVOICE_TYPE=B.INVOICE_TYPE ";
			queryString+="UNION ";
			queryString+="SELECT DISTINCT FILE_NAME,NULL,NULL FROM FMS_DLNG_INV_FILE_DTL "
					+ "WHERE COMPANY_CD=? AND PDF_TYPE=? "
					+ "AND TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') ";
			queryString+="UNION ";
			queryString+="SELECT DISTINCT FILE_NAME,INVOICE_TYPE,NULL FROM FMS_GTA_INV_FILE_DTL "
					+ "WHERE COMPANY_CD=? AND INV_TITLE=? "
					+ "AND TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') ";
			queryString+="UNION ";
			queryString+="SELECT DISTINCT FILE_NAME,INVOICE_TYPE,NULL FROM FMS_GTA_FFLOW_INV_FILE_DTL "
					+ "WHERE COMPANY_CD=? AND INV_TITLE=? "
					+ "AND TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') ";
			queryString+="UNION ";
			queryString+="SELECT DISTINCT FILE_NAME,INV_TYPE,NULL FROM FMS_DERV_INV_FILE_DTL "
					+ "WHERE COMPANY_CD=? AND PDF_TYPE=? "
					+ "AND TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') ";
			queryString+="UNION ";
			queryString+="SELECT DISTINCT FILE_NAME,NULL,NULL FROM FMS_DLNG_FFLOW_INV_FILE_DTL "
					+ "WHERE COMPANY_CD=? AND PDF_TYPE=? "
					+ "AND TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') ";
			queryString+="UNION ";
			queryString+="SELECT DISTINCT FILE_NAME,NULL,NULL FROM FMS_DLNG_SVC_INV_FILE_DTL "
					+ "WHERE COMPANY_CD=? AND PDF_TYPE=? "
					+ "AND TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') ";
			stmt3=conn.prepareStatement(queryString);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, "S");
			stmt3.setString(3, xml_gen_from_dt);
			stmt3.setString(4, xml_gen_to_dt);
			stmt3.setString(5, comp_cd);
			stmt3.setString(6, "S");
			stmt3.setString(7, xml_gen_from_dt);
			stmt3.setString(8, xml_gen_to_dt);
			stmt3.setString(9, comp_cd);
			stmt3.setString(10, "S");
			stmt3.setString(11, xml_gen_from_dt);
			stmt3.setString(12, xml_gen_to_dt);
			stmt3.setString(13, comp_cd);
			stmt3.setString(14, "S");
			stmt3.setString(15, xml_gen_from_dt);
			stmt3.setString(16, xml_gen_to_dt);
			stmt3.setString(17, comp_cd);
			stmt3.setString(18, "S");
			stmt3.setString(19, xml_gen_from_dt);
			stmt3.setString(20, xml_gen_to_dt);
			stmt3.setString(21, comp_cd);
			stmt3.setString(22, "S");
			stmt3.setString(23, xml_gen_from_dt);
			stmt3.setString(24, xml_gen_to_dt);
			stmt3.setString(25, comp_cd);
			stmt3.setString(26, "S");
			stmt3.setString(27, xml_gen_from_dt);
			stmt3.setString(28, xml_gen_to_dt);
			stmt3.setString(29, comp_cd);
			stmt3.setString(30, "S");
			stmt3.setString(31, xml_gen_from_dt);
			stmt3.setString(32, xml_gen_to_dt);
			stmt3.setString(33, comp_cd);
			stmt3.setString(34, "S");
			stmt3.setString(35, xml_gen_from_dt);
			stmt3.setString(36, xml_gen_to_dt);
			stmt3.setString(37, comp_cd);
			stmt3.setString(38, "S");
			stmt3.setString(39, xml_gen_from_dt);
			stmt3.setString(40, xml_gen_to_dt);
			rset3=stmt3.executeQuery();
			while(rset3.next())
			{
				String file_nm=rset3.getString(1)==null?"":rset3.getString(1);
				String inv_flag=rset3.getString(2)==null?"":rset3.getString(2);
				String inv_head=rset3.getString(3)==null?"":rset3.getString(3);
				if(inv_flag.equals("P"))
				{
					VJOURNAL_TYPE_NM.add("Provisional Purchase Invoice");
				}
				else if(inv_flag.equals("CP"))
				{
					VJOURNAL_TYPE_NM.add("Provisional Custom Duty");
				}
				else if(inv_flag.equals("F")|| inv_flag.equals("TC")|| inv_flag.equals("IC")|| inv_flag.equals("PC")||inv_flag.equals("R")||((inv_flag.equals("OR")||inv_flag.equals("CR")||inv_flag.equals("DR"))&&!inv_head.equals("CD")))
				{
					VJOURNAL_TYPE_NM.add("Final Purchase Invoice");
				}
				else if(inv_flag.equals("CF")|| (inv_flag.equals("OR")&&inv_head.equals("CD")))
				{
					VJOURNAL_TYPE_NM.add("Final Custom Duty");
				}
				else
				{
					VJOURNAL_TYPE_NM.add("Sales/Re-Gas Invoice");
				}
				VSUN_FILE_NM.add(file_nm);
			}
			rset3.close();
			stmt3.close();
		
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
			VSEGMENT.add("RLNG(SN,LOA)");
			VSEGMENT.add("IGX");
			VSEGMENT.add("LTCORA");
			VSEGMENT.add("DLNG(SN,LOA,IGX,TMS,TLU)");
			VSEGMENT.add("Derivatives");
			
			VSEGMENT_TYPE.add("S");
			VSEGMENT_TYPE.add("X");
			VSEGMENT_TYPE.add("O");
			VSEGMENT_TYPE.add("E");
			VSEGMENT_TYPE.add("V");
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getRemittanceSegment()
	{
		String function_nm="getRemittanceSegment()";
		try
		{
			/*
			 	INV_FLAG='P'		for provisional invoice 
			 	INV_FLAG='CP'		for custom duty provisional invoice 
			 	INV_FLAG='F'		for Final purchase remittance 
			 	INV_FLAG='CF'		for Final Custom Duty
			*/
			VSEGMENT.add("Purchase Provisional Remittance");		//Provisional Purchase Invoice 
			VSEGMENT.add("Custom Duty Provisional Remittance");		//Custom duty provisional 
			VSEGMENT.add("Final Purchase Invoice");					//Final Purchase Remittance 
			VSEGMENT.add("Final Custom Duty");						//Final Custom Duty
			if(!callFlag.equalsIgnoreCase("REMITTANCE_SUN_XML_GENERATION"))
			{
				VSEGMENT.add("Transportation/Parking");					//Final Transportation and Parking 
				VSEGMENT.add("Derivatives");							//Derivatives
			}
			
			VSEGMENT_TYPE.add("P");									//Provisional Purchase Invoice
			VSEGMENT_TYPE.add("CP"); 								//Custom duty provisional 
			VSEGMENT_TYPE.add("F");									//Final Purchase Remittance
			VSEGMENT_TYPE.add("CF");								//Final Custom Duty 
			if(!callFlag.equalsIgnoreCase("REMITTANCE_SUN_XML_GENERATION"))
			{
				VSEGMENT_TYPE.add("T");									//Final Transportation and Parking 
				VSEGMENT_TYPE.add("V");									//Derivatives
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getPurchaseRemittanceApproval()
	{
		String function_nm="getPurchaseRemittanceApproval()";
		try
		{
			String invFlag="F";
			String segmentType="";
			if(segment.equals("P"))
			{
				VTEMP_SEGMENT.add("Purchase Provisional Remittance");
				VTEMP_SEGMENT_TYPE.add("P");
			}
			else if(segment.equals("CP"))
			{
				VTEMP_SEGMENT.add("Custom Duty Provisional Remittance");
				VTEMP_SEGMENT_TYPE.add("CP");
			}
			else if(segment.equals("F"))
			{
				VTEMP_SEGMENT.add("Final Purchase Invoice");
				VTEMP_SEGMENT_TYPE.add("F");
			}
			else if(segment.equals("CF"))
			{
				VTEMP_SEGMENT.add("Final Custom Duty");
				VTEMP_SEGMENT_TYPE.add("CF");
			}
			else if(segment.equals("T"))
			{
				VTEMP_SEGMENT.add("Transportation/Parking");
				VTEMP_SEGMENT_TYPE.add("T");
			}
			else if(segment.equals("V"))
			{
				VTEMP_SEGMENT.add("Derivatives");
				VTEMP_SEGMENT_TYPE.add("V");
			}
			else
			{
				VTEMP_SEGMENT=VSEGMENT;
				VTEMP_SEGMENT_TYPE=VSEGMENT_TYPE;
			}
			for(int i=0;i<VTEMP_SEGMENT_TYPE.size();i++)
			{
				int index=0;
				if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("P"))
				{
					invFlag="INV_FLAG IN ('P') ";
					segmentType="CONTRACT_TYPE IN ('N') ";
				}
				else if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("CP"))
				{
					invFlag="INV_FLAG IN ('CP') ";
					segmentType="CONTRACT_TYPE IN ('N') ";
				}
				else if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("F"))
				{
					invFlag="INV_FLAG IN ('F') ";
					segmentType="CONTRACT_TYPE IN ('N','D','T','I') ";
				}
				else if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("CF"))
				{
					invFlag="INV_FLAG IN ('CF') ";
					segmentType="A.CONTRACT_TYPE IN ('N') ";
				}
				else if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("T"))
				{
					//invFlag="INV_FLAG IN ('A') ";
					segmentType="A.CONTRACT_TYPE IN ('R','C','K') ";
				}
				
				int cnt=0;
				String queryString="";
				if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("V"))
				{
					getDerivativeInvoiceList("R");
				}
				else
				{
					if(!VTEMP_SEGMENT_TYPE.elementAt(i).equals("T"))
					{
						queryString="SELECT COUNTERPARTY_CD,CONT_NO,CONTRACT_TYPE,BU_UNIT,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
								+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'), "
								+ "FREQ,ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,"
								+ "(NVL(GROSS_AMT,0) + NVL(TRANSPORTATION_AMOUNT,0) + NVL(MARKET_MARGIN_AMT,0) + NVL(OTHER_CHARGES_AMT,0)),"
								+ "TAX_AMT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,"
								+ "EXCHG_RATE_VALUE,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),INVOICE_RAISED_IN,TO_CHAR(INVOICE_DT, 'Month'),"
								+ "CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,TCS_CERT_FLAG,TCS_AMT,TCS_FACTOR,AGMT_NO,FINANCIAL_YEAR,SAP_EXCHNG_RATE,SAP_APPROVAL,"
								+ "TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),TCS_TDS,INV_FLAG,'S','SG',CARGO_NO,BOE_NO,SYS_INV_NO,QTY_UNIT,"
								+ "TRANSPORTATION_AMOUNT,MARKET_MARGIN_AMT,OTHER_CHARGES_AMT,PLANT_SEQ,SAP_APPROVED_BY,TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),NULL,NULL,NULL,EXCHG_RATE_CD,TO_CHAR(APPROVED_DT,'DD/MM/YYYY') "
								+ "FROM FMS_PUR_SG_INV_MST A "
								+ "WHERE COMPANY_CD=? "
								+ "AND INVOICE_DT>=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND "+segmentType+" AND PDF_INV_DTL IS NOT NULL AND "+invFlag+" ";		
						queryString+=" UNION ALL ";
						queryString+="SELECT COUNTERPARTY_CD,CONT_NO,CONTRACT_TYPE,BU_UNIT,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
								+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'), "
								+ "FREQ,ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,"
								+ "(NVL(GROSS_AMT,0) + NVL(TRANSPORTATION_AMOUNT,0) + NVL(MARKET_MARGIN_AMT,0) + NVL(OTHER_CHARGES_AMT,0)),"
								+ "TAX_AMT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,"
								+ "EXCHG_RATE_VALUE,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),INVOICE_RAISED_IN,TO_CHAR(INVOICE_DT, 'Month'),"
								+ "CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,TCS_CERT_FLAG,TCS_AMT,TCS_FACTOR,AGMT_NO,FINANCIAL_YEAR,SAP_EXCHNG_RATE,SAP_APPROVAL,"
								+ "TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),TCS_TDS,INV_FLAG,'P','PG',CARGO_NO,BOE_NO,SYS_INV_NO,QTY_UNIT,"
								+ "TRANSPORTATION_AMOUNT,MARKET_MARGIN_AMT,OTHER_CHARGES_AMT,PLANT_SEQ,SAP_APPROVED_BY,TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),NULL,NULL,NULL,EXCHG_RATE_CD,TO_CHAR(APPROVED_DT,'DD/MM/YYYY') "
								+ "FROM FMS_PUR_PG_INV_MST A "
								+ "WHERE COMPANY_CD=? "
								+ "AND INVOICE_DT>=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND "+segmentType+" AND PDF_INV_DTL IS NOT NULL AND "+invFlag+" ";
						if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("F")||VTEMP_SEGMENT_TYPE.elementAt(i).equals("CF"))
						{
							if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("CF"))
							{
								segmentType="A.CONTRACT_TYPE IN ('N') ";
								invFlag="AND INV_HEAD IN ('CD') ";
							}
							else
							{
								invFlag="AND (INV_HEAD IN ('NG','IMB') OR INVOICE_TYPE IN ('CR','DR') )";
							}
							queryString += " UNION ALL ";
							queryString +="SELECT COUNTERPARTY_CD,CONT_NO,CONTRACT_TYPE,BU_UNIT,INVOICE_SEQ,INVOICE_REF,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'), "
									+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'), "
									+ "FREQ,ALLOC_QTY,NULL,NULL,NULL, "
									+ "GROSS_AMT_INR, "
									+ "TAX_AMT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,  "
									+ "EXCHG_RATE_VALUE,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),INVOICE_RAISED_IN,TO_CHAR(INVOICE_DT, 'Month'), "
									+ "CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,TCS_CERT_FLAG,TCS_AMT,NULL,AGMT_NO,FINANCIAL_YEAR,SAP_EXCHNG_RATE,SAP_APPROVAL,  "
									+ "TDS_AMT,NULL,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),TCS_TDS,NULL,'FF','FFLOW',CARGO_NO,NULL,INVOICE_NO,QTY_UNIT, "
									+ "NULL,NULL,NULL,NULL,SAP_APPROVED_BY,TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),GROSS_AMT_USD,INVOICE_CATEGORY,INVOICE_TYPE,EXCHG_RATE_CD,TO_CHAR(APPROVED_DT,'DD/MM/YYYY') "
									+ "FROM FMS_PUR_FFLOW_INV_MST A " 
									+ "WHERE COMPANY_CD=? " 
									+ "AND INVOICE_DT>=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND " + segmentType + " AND PDF_INV_DTL IS NOT NULL " + invFlag + " ";
						}
						queryString+="ORDER BY CONT_NO ";
					}
					else
					{
						queryString+="SELECT COUNTERPARTY_CD,CONT_NO,CONTRACT_TYPE,BU_UNIT,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'), "
								+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),  "
								+ "FREQ,ALLOC_QTY,NULL,NULL,SALE_AMT, "
								+ "(NVL(GROSS_AMT,0)), "
								+ "TAX_AMT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT, "
								+ "EXCHG_RATE_VALUE,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),INVOICE_RAISED_IN,TO_CHAR(INVOICE_DT, 'Month'), "
								+ "CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,NULL,NULL,NULL,AGMT_NO,FINANCIAL_YEAR,NULL,SAP_APPROVAL, "
								+ "TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),TCS_TDS,NULL,'GTA_S','GTA_SG',NULL,NULL,SYS_INV_NO,NULL, "
								+ "NULL,NULL,NULL,NULL,SAP_APPROVED_BY,TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),NULL,NULL,INVOICE_TYPE,EXCHG_RATE_CD,TO_CHAR(APPROVED_DT,'DD/MM/YYYY')  "
								+ "FROM FMS_GTA_SG_INV_MST A  "
								+ "WHERE COMPANY_CD=? "
								+ "AND INVOICE_DT>=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND " + segmentType + " AND PDF_INV_DTL IS NOT NULL ";
						queryString += " UNION ALL ";
						queryString+="SELECT COUNTERPARTY_CD,CONT_NO,CONTRACT_TYPE,BU_UNIT,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'), "
								+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'), "
								+ "FREQ,ALLOC_QTY,NULL,NULL,SALE_AMT, "
								+ "(NVL(GROSS_AMT,0)), "
								+ "TAX_AMT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT, "
								+ "EXCHG_RATE_VALUE,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),INVOICE_RAISED_IN,TO_CHAR(INVOICE_DT, 'Month'), "
								+ "CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,NULL,NULL,NULL,AGMT_NO,FINANCIAL_YEAR,NULL,SAP_APPROVAL, "
								+ "TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),TCS_TDS,NULL,'GTA_P','GTA_PG',NULL,NULL,SYS_INV_NO,NULL, "
								+ "NULL,NULL,NULL,NULL,SAP_APPROVED_BY,TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),NULL,NULL,INVOICE_TYPE,EXCHG_RATE_CD,TO_CHAR(APPROVED_DT,'DD/MM/YYYY')  "
								+ "FROM FMS_GTA_PG_INV_MST A  "
								+ "WHERE COMPANY_CD=? "
								+ "AND INVOICE_DT>=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY')"
								+ "AND " + segmentType + " AND PDF_INV_DTL IS NOT NULL ";
						queryString += " UNION ALL ";
						queryString +="SELECT COUNTERPARTY_CD,CONT_NO,CONTRACT_TYPE,BU_UNIT,INVOICE_SEQ,INVOICE_REF,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),  "
								+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),  "
								+ "FREQ,ALLOC_QTY,NULL,NULL,NULL,  "
								+ "GROSS_AMT_INR,  "
								+ "TAX_AMT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,   "
								+ "EXCHG_RATE_VALUE,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),INVOICE_RAISED_IN,TO_CHAR(INVOICE_DT, 'Month'),  "
								+ "CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,TCS_CERT_FLAG,TCS_AMT,NULL,AGMT_NO,FINANCIAL_YEAR,SAP_EXCHNG_RATE,SAP_APPROVAL,   "
								+ "TDS_AMT,NULL,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),TCS_TDS,NULL,'GTA_FF','GTA_FFLOW',NULL,NULL,INVOICE_NO,NULL,  "
								+ "NULL,NULL,NULL,NULL,SAP_APPROVED_BY,TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),GROSS_AMT_USD,INVOICE_CATEGORY,INVOICE_TYPE,EXCHG_RATE_CD,TO_CHAR(APPROVED_DT,'DD/MM/YYYY')  "
								+ "FROM FMS_GTA_FFLOW_INV_MST A "
								+ "WHERE COMPANY_CD=? "
								+ "AND INVOICE_DT>=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY')"
								+ "AND " + segmentType + " AND PDF_INV_DTL IS NOT NULL ";
						queryString+="ORDER BY CONT_NO ";
					}
					
				}
				if(!VTEMP_SEGMENT_TYPE.elementAt(i).equals("V"))
				{
					String temp_queryString=queryString;
					stmt=conn.prepareStatement(temp_queryString);
					if(!VTEMP_SEGMENT_TYPE.elementAt(i).equals("T"))
					{
						stmt.setString(++cnt, comp_cd);
						stmt.setString(++cnt, from_dt);
						stmt.setString(++cnt, to_dt);
						stmt.setString(++cnt, comp_cd);
						stmt.setString(++cnt, from_dt);
						stmt.setString(++cnt, to_dt);
						if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("F")||VTEMP_SEGMENT_TYPE.elementAt(i).equals("CF"))
						{
							stmt.setString(++cnt, comp_cd); 
							stmt.setString(++cnt, from_dt);
							stmt.setString(++cnt, to_dt);
						}
					}
					else
					{
						stmt.setString(++cnt, comp_cd);
						stmt.setString(++cnt, from_dt);
						stmt.setString(++cnt, to_dt);
						stmt.setString(++cnt, comp_cd);
						stmt.setString(++cnt, from_dt);
						stmt.setString(++cnt, to_dt);
						stmt.setString(++cnt, comp_cd);
						stmt.setString(++cnt, from_dt);
						stmt.setString(++cnt, to_dt);
					}
					rset=stmt.executeQuery();
					while(rset.next())
					{
						index+=1;
						String counterpty_cd = rset.getString(1)==null?"":rset.getString(1);
						String contno=rset.getString(2)==null?"0":rset.getString(2);
						String cont_type=rset.getString(3)==null?"":rset.getString(3);
						String bu_seq=rset.getString(4)==null?"":rset.getString(4);
						String inv_seq_no=rset.getString(5)==null?"":rset.getString(5);
						String inv_no = rset.getString(6)==null?"":rset.getString(6);
						String invoice_dt = rset.getString(7)==null?"":rset.getString(7);
						String invoice_due_dt=rset.getString(8)==null?"":rset.getString(8);
						String period_st_dt = rset.getString(9)==null?"":rset.getString(9);
						String period_end_dt = rset.getString(10)==null?"":rset.getString(10);
						//11: freq
						String alloc_qty=nf.format(rset.getDouble(12));
						String rate = ""+rset.getDouble(13);
						String rate_unit = rset.getString(14)==null?"":rset.getString(14);
						String sale_amt=nf.format(rset.getDouble(15));
						double gross_amt=rset.getDouble(16);
						double tax_amt=rset.getDouble(17);
						double inv_amt=rset.getDouble(18);
						String adj_sign=rset.getString(19)==null?"":rset.getString(19);
						double adj_amt=rset.getDouble(20);
						double temp_net_payable = rset.getDouble(21);
						double temp_exchang_rate = rset.getDouble(22);
						String exc_rate_dt=rset.getString(23)==null?"":rset.getString(23);
						String invoice_raised_in=rset.getString(24)==null?"":rset.getString(24);
						String inv_chk_flg=rset.getString(25)==null?"":rset.getString(25);
						String inv_auth_flg=rset.getString(26)==null?"":rset.getString(26);
						String inv_app_flg=rset.getString(27)==null?"":rset.getString(27);
						//approved_flag: 28
						String tcs_tds_cert=rset.getString(29)==null?"":rset.getString(29);
						double tcs_amt_temp=rset.getDouble(30);
						String tcs_factor=rset.getString(31)==null?"":nf3.format(rset.getDouble(31));
						String agmtno=rset.getString(32)==null?"0":rset.getString(32);
						String financial_year=rset.getString(33)==null?"":rset.getString(33);
						String sap_exchng_rate=rset.getString(34)==null?"":rset.getString(34);
						String sap_approval_flag=rset.getString(35)==null?"":rset.getString(35);
						double tds_amt_temp=rset.getDouble(36);
						String tds_factor=rset.getString(37)==null?"":nf3.format(rset.getDouble(37));
						String tds_struct_cd=rset.getString(38)==null?"":rset.getString(38);
						String tds_eff_dt=rset.getString(39)==null?"":rset.getString(39);
						String tcs_tds=rset.getString(40)==null?"":rset.getString(40);
						String invoice_flag= rset.getString(41)==null?"":rset.getString(41);
						String payment_type=rset.getString(42)==null?"":rset.getString(42);
						String payment_type_nm=rset.getString(43)==null?"":rset.getString(43);
						String cargono=rset.getString(44)==null?"":rset.getString(44);
						String boeno=rset.getString(45)==null?"":rset.getString(45);
						String remittance_no = rset.getString(46)==null?"":rset.getString(46);
						String qty_unit=rset.getString(47)==null?"1":rset.getString(47);
						//transportation amt: 48
						//marketing margine: 49
						//other amt: 50
						String plant_seq = rset.getString(51)==null?"":rset.getString(51);
						String sun_approved_by=rset.getString(52)==null?"":rset.getString(52);
						String sun_approved_dt=rset.getString(53)==null?"":rset.getString(53);
						String gross_amt_usd = rset.getString(54)==null?"":nf.format(rset.getDouble(54));
						String inv_cetegory=rset.getString(55)==null?"":rset.getString(55);
						String invoice_type=rset.getString(56)==null?"":rset.getString(56);
						String exchng_rate_cd = rset.getString(57)==null?"":rset.getString(57);
						String inv_approve_dt = rset.getString(58)==null?"":rset.getString(58);
						
						String deal_no=utilBean.NewDealMappingId(comp_cd, counterpty_cd, agmtno, "", contno, "", cont_type, cargono);
						String disp_remittance_no = remittance_no; 
						if(cont_type.equals("N"))
						{
							disp_remittance_no = "<font style='background: #ff99ff;'>BOE"+utilBean.PrePaddingZero(boeno, 2)+":</font> "+remittance_no;
						}
						String trans_dt=utilDate.getMonthNameMON(period_end_dt).equals(utilDate.getMonthNameMON(inv_approve_dt))?inv_approve_dt:period_end_dt;		//for showing the transaction date

						//for fetching the actual arrival date of the cargo
						String act_arr_dt="";
						String query="SELECT TO_CHAR(BOE_DT,'DD/MM/YYYY') FROM FMS_BUY_CARGO_ALLOC_BOE A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? "
								+ "AND CARGO_NO=? AND CONT_REV=? AND ALLOC_REV=(SELECT MAX(ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC_BOE B "
								+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE  "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO  "
								+ "AND A.CONT_REV=B.CONT_REV  AND A.CARGO_NO=B.CARGO_NO)";
						/*
						 * query+="UNION "; query+
						 * ="SELECT TO_CHAR(EXP_TO_DT,'DD/MM/YYYY'),SHIP_CD FROM FMS_BUY_CARGO_NOM A " +
						 * "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? " +
						 * "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? " +
						 * "AND CARGO_NO=? AND CONT_REV=? AND NOM_REV=(SELECT MAX(NOM_REV) FROM FMS_BUY_CARGO_NOM B "
						 * +
						 * "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE "
						 * +
						 * "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO "
						 * + "AND A.CONT_REV=B.CONT_REV  AND A.CARGO_NO=B.CARGO_NO)";
						 */
						stmt1=conn.prepareStatement(query);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterpty_cd);
						stmt1.setString(3, "M");
						stmt1.setString(4, agmtno);
						stmt1.setString(5, "0");
						stmt1.setString(6, cont_type);
						stmt1.setString(7, contno);
						stmt1.setString(8, cargono);
						stmt1.setString(9, "0");
						/*
						 * stmt1.setString(10, comp_cd); stmt1.setString(11, counterpty_cd);
						 * stmt1.setString(12, "M"); stmt1.setString(13, agmtno); stmt1.setString(14,
						 * "0"); stmt1.setString(15, cont_type); stmt1.setString(16, contno);
						 * stmt1.setString(17, cargono); stmt1.setString(18, "0");
						 */
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							act_arr_dt=rset1.getString(1)==null?"":rset1.getString(1);
							//ship_cd=rset1.getString(2)==null?"":rset1.getString(2);
							VACT_ARRIVAL_DT.add(act_arr_dt);
						}
						else
						{
							VACT_ARRIVAL_DT.add("");
						}
						rset1.close();
						stmt1.close();
						
						act_arr_dt=act_arr_dt.equals("")?invoice_dt:act_arr_dt;
						
						if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("P") || (VTEMP_SEGMENT_TYPE.elementAt(i).equals("F")&& cont_type.equals("N")))
						{
							double exc_rate = getExchg_rate("SBI RATE SELL",act_arr_dt);
							if(Double.doubleToRawLongBits(exc_rate)==Double.doubleToRawLongBits(0))
							{
								VEXCHNG_RATE_CONFIG.add("N");
							}
							else
							{
								VEXCHNG_RATE_CONFIG.add("Y");
							}
						}
						else
						{
							VEXCHNG_RATE_CONFIG.add("");
						}
						
						VEXCHNG_RATE_CD.add(exchng_rate_cd);
						VCONTRACT_TYPE.add(cont_type);
						VFINANCIAL_YEAR.add(financial_year);
						VSAP_APPROVAL_FLAG.add(sap_approval_flag);
						
						VCOUNTERPARTY_CD.add(counterpty_cd);
						VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterpty_cd));
						VCONT_NO.add(contno);
						VDIS_CONT_MAPPING.add(deal_no);
						VBU_SEQ.add(bu_seq);
						VBU_NM.add(""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_seq, "B"));
						
						VINVOICE_SEQ.add(inv_seq_no);
						VINVOICE_NO.add(inv_no);
						VINVOICE_DT.add(invoice_dt);
						VINVOICE_DUE_DT.add(invoice_due_dt);
						VBU_STATE_TIN.add("");
						if(cont_type.equals("N") && (invoice_flag.equals("F") || invoice_flag.equals("P")))
						{
							VPAYMENT_DONE_IN.add("USD");
						}
						else
						{
							VPAYMENT_DONE_IN.add("INR");
						}
						VPERIOD_START_DT.add(period_st_dt);
						VPERIOD_END_DT.add(period_end_dt);
						VALLOC_QTY.add(alloc_qty);
						VSALES_PRICE.add(""+utilBean.RateNumberFormat(Double.parseDouble(rate), rate_unit));
						VSALES_PRICE_UNIT.add(""+utilBean.getRateUnitNm(conn,rate_unit));
						if(qty_unit.equals("0"))
						{
							VQTY_UNIT.add("Lumpsum");
						}
						else
						{
							VQTY_UNIT.add(""+utilBean.getEnergyUnitNm(conn,qty_unit));
						}
						VSALE_AMT.add(sale_amt);
						VADJ_SIGN.add(adj_sign);
						if(!exchng_rate_dt.equals("") && remittance_no.equals(rem_no))
						{
							VEXCHNAGE_RATE_DATE.add(exchng_rate_dt);
						}
						else
						{
							VEXCHNAGE_RATE_DATE.add(exc_rate_dt);
						}
						VINVOICE_RAISED_IN.add(utilBean.getRateUnitNm(conn,invoice_raised_in));
						VINV_FLAG.add(invoice_flag);
						VPAYMENT_TYPE_FLAG.add(payment_type);
						VPAYMENT_TYPE_NM.add(payment_type_nm);
						VAPPROVE_HIST.add(utilBean.getEmpName(conn, sun_approved_by)+"<br>"+sun_approved_dt);
						VDIS_REMITTANCE_NO.add(disp_remittance_no);
						VTRANS_DT.add(trans_dt);
						
						double net_payable_usd = 0;
						double tcs_amt_usd = 0;
						double tds_amt_usd = 0;
						double net_payable_inr = 0;
						double tcs_amt_inr = 0;
						double tds_amt_inr = 0;	
						
						if(invoice_raised_in.equals("2"))
						{
							tcs_amt_usd=tcs_amt_temp;
							tds_amt_usd=tds_amt_temp;
							net_payable_usd = temp_net_payable;
							
							if(payment_type.equals("FF")||payment_type.equals("GTA_FF"))
							{
								VGROSS_AMT_USD.add(gross_amt_usd);
							}
							else
							{
								VGROSS_AMT_USD.add(rset.getString(16)==null?"":nf.format(gross_amt));
							}
							VTAX_AMT_USD.add(rset.getString(17)==null?"":nf.format(tax_amt));
							VINVOICE_AMT_USD.add(rset.getString(18)==null?"":nf.format(inv_amt));
							VADJ_AMT_USD.add(rset.getString(20)==null?"":nf.format(adj_amt));
							
							if(Double.doubleToRawLongBits(temp_exchang_rate)>Double.doubleToRawLongBits(0))
							{
								tcs_amt_inr=tcs_amt_temp* temp_exchang_rate;
								tds_amt_inr=tds_amt_temp* temp_exchang_rate;
								net_payable_inr = temp_net_payable*temp_exchang_rate;	

								if(payment_type.equals("FF")||payment_type.equals("GTA_FF"))
								{
									VGROSS_AMT.add((Double.parseDouble(gross_amt_usd)* temp_exchang_rate));
								}
								else
								{
									VGROSS_AMT.add(rset.getString(16)==null?"":nf.format((gross_amt) * temp_exchang_rate));
								}
								VTAX_AMT.add(rset.getString(17)==null?"":nf.format(tax_amt * temp_exchang_rate));
								VINVOICE_AMT.add(rset.getString(18)==null?"":nf.format(inv_amt * temp_exchang_rate));
								VADJ_AMT.add(rset.getString(20)==null?"":nf.format(adj_amt * temp_exchang_rate));
							}
							else
							{
								tcs_amt_inr=0;
								tds_amt_inr=0;
								net_payable_inr=0;
								
								VGROSS_AMT.add("");
								VTAX_AMT.add("");
								VINVOICE_AMT.add("");
								VADJ_AMT.add("");
							}	
							
							if(!exchng_rate_dt.equals("") && remittance_no.equals(rem_no))
							{
								String new_exchng_rate=getExchng_Rate(exchng_rate_cd,exchng_rate_dt);
								VEXCHNAGE_RATE.add(new_exchng_rate);
							}
							else
							{
								VEXCHNAGE_RATE.add(rset.getString(22)==null?"":nf2.format(temp_exchang_rate));
							}
							VSAP_EXCHANG_FLAG.add("Y");
						}
						else
						{
							tcs_amt_inr=tcs_amt_temp;
							tds_amt_inr=tds_amt_temp;
							net_payable_inr=temp_net_payable;
							
							tcs_amt_usd=0;
							tds_amt_usd=0;
							net_payable_usd = 0;
							
							VGROSS_AMT.add(rset.getString(16)==null?"":nf.format(gross_amt));
							VTAX_AMT.add(rset.getString(17)==null?"":nf.format(tax_amt));
							VINVOICE_AMT.add(rset.getString(18)==null?"":nf.format(inv_amt));
							VADJ_AMT.add(rset.getString(20)==null?"":nf.format(adj_amt));
							
							if(!exchng_rate_dt.equals("") && remittance_no.equals(rem_no))
							{
								String new_exchng_rate=getExchng_Rate(exchng_rate_cd,exchng_rate_dt);
								VEXCHNAGE_RATE.add(new_exchng_rate);
							}
							else
							{
								VEXCHNAGE_RATE.add(rset.getString(22)==null?"":nf2.format(temp_exchang_rate));
							}
							VSAP_EXCHANG_FLAG.add(sap_exchng_rate);
							
							VGROSS_AMT_USD.add("");
							VTAX_AMT_USD.add("");
							VINVOICE_AMT_USD.add("");
							VADJ_AMT_USD.add("");
						}
						
						if(tcs_tds.equals("TDS"))
						{
							net_payable_inr=net_payable_inr-tds_amt_inr;
							net_payable_usd=net_payable_usd-tds_amt_usd;
							
							VTCS_TDS.add(tcs_tds);
							
							if(invoice_raised_in.equals("2"))
							{
								VTCS_TDS_AMT_USD.add(rset.getString(36)==null?"":nf3.format(tds_amt_usd));
								VTCS_TDS_AMT.add(rset.getString(36)==null?"":nf3.format(tds_amt_inr));
							}
							else
							{
								VTCS_TDS_AMT.add(rset.getString(36)==null?"":nf3.format(tds_amt_inr));
								VTCS_TDS_AMT_USD.add("");
							}
							VTCS_TDS_FACTOR.add(tds_factor);
							VTCS_TDS_STRUCT_CD.add(tds_struct_cd);
							VTCS_TDS_EFF_DT.add(tds_eff_dt);
							VTCS_TDS_DONE.add("Y");
						}
						else  if(tcs_tds.equals("TCS"))
						{
							net_payable_inr=net_payable_inr+tcs_amt_inr;
							net_payable_usd=net_payable_usd+tcs_amt_usd;
							
							VTCS_TDS.add(tcs_tds);
							if(invoice_raised_in.equals("2"))
							{
								VTCS_TDS_AMT_USD.add(rset.getString(30)==null?"":nf3.format(tcs_amt_usd));
								VTCS_TDS_AMT.add(rset.getString(30)==null?"":nf3.format(tcs_amt_inr));
							}
							else
							{
								VTCS_TDS_AMT.add(rset.getString(30)==null?"":nf3.format(tcs_amt_inr));
								VTCS_TDS_AMT_USD.add("");
							}
							VTCS_TDS_FACTOR.add(tcs_factor);
							VTCS_TDS_STRUCT_CD.add("");
							VTCS_TDS_EFF_DT.add("");
							VTCS_TDS_DONE.add("Y");
						}
						else
						{
							VTCS_TDS.add("");
							
							VTCS_TDS_AMT.add("");
							VTCS_TDS_AMT_USD.add("");
							VTCS_TDS_FACTOR.add(tcs_factor);
							VTCS_TDS_STRUCT_CD.add("");
							VTCS_TDS_EFF_DT.add("");
							VTCS_TDS_DONE.add("Y");
						}
						
						String net_payable="";
						
						if(invoice_raised_in.equals("2") && cont_type.equals("N"))
						{
							net_payable=""+net_payable_usd;
						}
						else
						{
							net_payable=""+net_payable_inr;
						}
						
						if(Double.doubleToRawLongBits(net_payable_inr)!=Double.doubleToRawLongBits(0))
						{
							VNET_PAYABLE.add(nf.format(net_payable_inr));
						}
						else
						{
							VNET_PAYABLE.add("");
						}
						if(Double.doubleToRawLongBits(net_payable_usd)!=Double.doubleToRawLongBits(0))
						{
							VNET_PAYABLE_USD.add(nf2.format(net_payable_usd));
						}
						else
						{
							VNET_PAYABLE_USD.add("");
						}
						
						VINVOICE_TYPE.add(invoice_type);
						
						String cashflow="Commodity";
						if(payment_type.equals("FF"))
						{
							String cash_flow="";
							if(inv_cetegory.equals("S"))
							{
								cash_flow="Service";
							}
							else if(inv_type.equals("LP"))
							{
								cash_flow="Interest";
							}
							else if(inv_type.equals("CR") || inv_type.equals("CCR"))
							{
								//cash_flow="Brokerage/Commission";
								cash_flow="Commodity";
							}
							else if(inv_cetegory.equals("P"))
							{
								cash_flow="Commodity";
							}
							VCASH_FLOW.add(cash_flow);
						}
						else if(payment_type.equals("GTA_S"))
						{
							if(invoice_type.equals("TC"))
							{
								cashflow="Transmission Charge";
							}
							else if(invoice_type.equals("IC"))
							{
								cashflow="Imbalance Charge";
							}
							else if(invoice_type.equals("PC"))
							{
								cashflow="Parking Charge";
							}
							VCASH_FLOW.add(cashflow);
						}
						else if(payment_type.equals("GTA_FF"))
						{
							cashflow="Free Flow";
							VCASH_FLOW.add(cashflow);
						}
						else
						{
							if(cont_type.equals("G") || cont_type.equals("P")) 
							{
								if(invoice_flag.equals("UG")) {
									cashflow="SUG";
								} else {
									cashflow="Capacity";
								}
							}
							else if(cont_type.equals("Y") || cont_type.equals("A") || cont_type.equals("H"))
							{
								cashflow="Service";
							}
							VCASH_FLOW.add(cashflow);
						}
						
						String split_value="";
						int cnt1=0;
						String queryString1="SELECT SPLIT_VALUE "
								+ "FROM FMS_TRADER_CONT_PLANT "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
								+ "AND CONT_NO=? AND PLANT_SEQ_NO=? AND CONTRACT_TYPE=? ";
						queryString1+=" UNION ALL ";
						queryString1+= "SELECT SPLIT_VALUE "
								+ "FROM FMS_TRADER_CONT_SPLIT_PLANT "
								+ "WHERE COMPANY_CD=? AND SPLIT_TRADER_CD=? AND AGMT_NO=? "
								+ "AND CONT_NO=? AND PLANT_SEQ_NO=? AND CONTRACT_TYPE=? ";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(++cnt1, comp_cd);
						stmt1.setString(++cnt1, counterpty_cd);
						stmt1.setString(++cnt1, agmtno);
						stmt1.setString(++cnt1, contno);
						stmt1.setString(++cnt1, plant_seq);
						stmt1.setString(++cnt1, cont_type);
						stmt1.setString(++cnt1, comp_cd);
						stmt1.setString(++cnt1, counterpty_cd);
						stmt1.setString(++cnt1, agmtno);
						stmt1.setString(++cnt1, contno);
						stmt1.setString(++cnt1, plant_seq);
						stmt1.setString(++cnt1, cont_type);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							split_value=rset1.getString(1)==null?"":rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						VSPLIT_VALUE.add(split_value);
					}
					rset.close();
					stmt.close();
					VINDEX.add(index);
					
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getPurSunApprovedList()
	{
		String function_nm="getPurSunApprovedList()";
		try
		{
			String journal_type="FMSPI";
			
			
			String invFlag="F";
			String segmentType="";
			if(segment.equals("P"))
			{
				VTEMP_SEGMENT.add("Purchase Provisional Remittance");
				VTEMP_SEGMENT_TYPE.add("P");
			}
			else if(segment.equals("CP"))
			{
				VTEMP_SEGMENT.add("Custom Duty Provisional Remittance");
				VTEMP_SEGMENT_TYPE.add("CP");
			}
			else if(segment.equals("F"))
			{
				VTEMP_SEGMENT.add("Final Purchase Invoice");
				VTEMP_SEGMENT_TYPE.add("F");
			}
			else if(segment.equals("CF"))
			{
				VTEMP_SEGMENT.add("Final Custom Duty");
				VTEMP_SEGMENT_TYPE.add("CF");
			}
			
			/*else
			{
				VTEMP_SEGMENT=VSEGMENT;
				VTEMP_SEGMENT_TYPE=VSEGMENT_TYPE;
			}*/
			for(int i=0;i<VTEMP_SEGMENT_TYPE.size();i++)
			{
				int index=0;
				if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("P"))
				{
					invFlag="A.INV_FLAG IN ('P') ";
					segmentType="A.CONTRACT_TYPE IN ('N') ";
				}
				else if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("CP"))
				{
					invFlag="A.INV_FLAG IN ('CP') ";
					segmentType="A.CONTRACT_TYPE IN ('N') ";
					journal_type="FMSPC";
				}
				else if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("F"))
				{
					invFlag="INV_FLAG IN ('F') ";
					segmentType="A.CONTRACT_TYPE IN ('N','D','T','I') ";
					journal_type="FMSFI";
				}
				else if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("CF"))
				{
					invFlag="INV_FLAG IN ('CF') ";
					segmentType="A.CONTRACT_TYPE IN ('N') ";
					journal_type="FMSFC";
				}
				
				int cnt=0;
				String queryString="SELECT A.COUNTERPARTY_CD,A.CONT_NO,A.CONTRACT_TYPE,A.BU_UNIT,A.INVOICE_SEQ,A.INVOICE_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),"
							+ "TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'), "
							+ "A.FREQ,A.ALLOC_QTY,A.SALE_PRICE,A.SALE_PRICE_UNIT,A.SALE_AMT,"
							+ "(NVL(A.GROSS_AMT,0) + NVL(A.TRANSPORTATION_AMOUNT,0) + NVL(A.MARKET_MARGIN_AMT,0) + NVL(A.OTHER_CHARGES_AMT,0)),"
							+ "A.TAX_AMT,A.INVOICE_AMT,A.ADJUST_SIGN,A.ADJUST_AMT,A.NET_PAYABLE_AMT,"
							+ "A.EXCHG_RATE_VALUE,TO_CHAR(A.EXCHG_RATE_DT,'DD/MM/YYYY'),A.INVOICE_RAISED_IN,TO_CHAR(A.INVOICE_DT, 'Month'),"
							+ "A.CHECKED_FLAG,A.AUTHORIZED_FLAG,A.APPROVED_FLAG,A.TCS_CERT_FLAG,A.TCS_AMT,A.TCS_FACTOR,A.AGMT_NO,A.FINANCIAL_YEAR,A.SAP_EXCHNG_RATE,A.SAP_APPROVAL,"
							+ "A.TDS_AMT,A.TDS_FACTOR,A.TDS_STRUCT_CD,TO_CHAR(A.TDS_EFF_DT,'DD/MM/YYYY'),A.TCS_TDS,A.INV_FLAG,'S','SG',A.CARGO_NO,A.BOE_NO,A.SYS_INV_NO,A.QTY_UNIT,"
							+ "A.TRANSPORTATION_AMOUNT,A.MARKET_MARGIN_AMT,A.OTHER_CHARGES_AMT,A.PLANT_SEQ,A.SAP_APPROVED_BY,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),A.AGMT_REV,A.CONT_REV,A.TAX_STRUCT_CD, "
							+ "NVL(A.NET_PAYABLE_AMT,0)-NVL(A.INVOICE_AMT,0),A.TCS_STRUCT_CD,NULL,NULL,NULL,CD_PAID_AMT,TO_CHAR(APPROVED_DT,'DD/MM/YYYY') "
							+ "FROM FMS_PUR_SG_INV_MST A "
							+ "WHERE A.COMPANY_CD=? "
							+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND "+segmentType+" AND PDF_INV_DTL IS NOT NULL AND "+invFlag+" "
							+ "AND A.SAP_APPROVAL=? AND A.FIN_SYS=? "
							+ "AND NOT EXISTS (SELECT INV_TITLE FROM FMS_PUR_INV_FILE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.INVOICE_SEQ=B.INVOICE_SEQ "
							+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.INV_FLAG=B.INV_FLAG AND B.INV_TITLE IN (?,?) )";
					queryString+=" UNION ALL ";
					queryString+="SELECT A.COUNTERPARTY_CD,A.CONT_NO,A.CONTRACT_TYPE,A.BU_UNIT,A.INVOICE_SEQ,A.INVOICE_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),"
							+ "TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'), "
							+ "A.FREQ,A.ALLOC_QTY,A.SALE_PRICE,A.SALE_PRICE_UNIT,A.SALE_AMT,"
							+ "(NVL(A.GROSS_AMT,0) + NVL(A.TRANSPORTATION_AMOUNT,0) + NVL(A.MARKET_MARGIN_AMT,0) + NVL(A.OTHER_CHARGES_AMT,0)),"
							+ "A.TAX_AMT,A.INVOICE_AMT,A.ADJUST_SIGN,A.ADJUST_AMT,A.NET_PAYABLE_AMT,"
							+ "A.EXCHG_RATE_VALUE,TO_CHAR(A.EXCHG_RATE_DT,'DD/MM/YYYY'),A.INVOICE_RAISED_IN,TO_CHAR(A.INVOICE_DT, 'Month'),"
							+ "A.CHECKED_FLAG,A.AUTHORIZED_FLAG,A.APPROVED_FLAG,A.TCS_CERT_FLAG,A.TCS_AMT,A.TCS_FACTOR,A.AGMT_NO,A.FINANCIAL_YEAR,A.SAP_EXCHNG_RATE,A.SAP_APPROVAL,"
							+ "A.TDS_AMT,A.TDS_FACTOR,A.TDS_STRUCT_CD,TO_CHAR(A.TDS_EFF_DT,'DD/MM/YYYY'),A.TCS_TDS,A.INV_FLAG,'P','PG',A.CARGO_NO,A.BOE_NO,A.SYS_INV_NO,A.QTY_UNIT,"
							+ "A.TRANSPORTATION_AMOUNT,A.MARKET_MARGIN_AMT,A.OTHER_CHARGES_AMT,A.PLANT_SEQ,A.SAP_APPROVED_BY,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),A.AGMT_REV,A.CONT_REV,A.TAX_STRUCT_CD,  "
							+ "NVL(A.NET_PAYABLE_AMT,0)-NVL(A.INVOICE_AMT,0),A.TCS_STRUCT_CD,NULL,NULL,NULL,CD_PAID_AMT,TO_CHAR(APPROVED_DT,'DD/MM/YYYY') "
							+ "FROM FMS_PUR_PG_INV_MST A  "
							+ "WHERE A.COMPANY_CD=? "
							+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND "+segmentType+" AND PDF_INV_DTL IS NOT NULL AND "+invFlag+" "
							+ "AND SAP_APPROVAL=? AND FIN_SYS=? "
							+ "AND NOT EXISTS (SELECT INV_TITLE FROM FMS_PUR_INV_FILE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.INVOICE_SEQ=B.INVOICE_SEQ "
							+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.INV_FLAG=B.INV_FLAG AND B.INV_TITLE IN (?,?) )";
					if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("F")||VTEMP_SEGMENT_TYPE.elementAt(i).equals("CF"))
					{
						if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("CF"))
						{
							segmentType="A.CONTRACT_TYPE IN ('N') ";
							invFlag="INV_HEAD IN ('CD') ";
						}
						else
						{
							segmentType="A.CONTRACT_TYPE IN ('N','D','T','I') ";
							invFlag="(INV_HEAD IN ('NG','IMB') OR INVOICE_TYPE IN ('CR','DR') )";
						}
						
						queryString+=" UNION ALL ";
						queryString+="SELECT COUNTERPARTY_CD,CONT_NO,CONTRACT_TYPE,BU_UNIT,INVOICE_SEQ,INVOICE_REF,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),  "
								+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),  "
								+ "FREQ,ALLOC_QTY,NULL,NULL,NULL,  "
								+ "GROSS_AMT_INR,  "
								+ "TAX_AMT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,   "
								+ "EXCHG_RATE_VALUE,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),INVOICE_RAISED_IN,TO_CHAR(INVOICE_DT, 'Month'),  "
								+ "CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,TCS_CERT_FLAG,TCS_AMT,NULL,AGMT_NO,FINANCIAL_YEAR,SAP_EXCHNG_RATE,SAP_APPROVAL,   "
								+ "TDS_AMT,NULL,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),TCS_TDS,NULL,'FF','FFLOW',CARGO_NO,NULL,INVOICE_NO,QTY_UNIT,  "
								+ "NULL,NULL,NULL,NULL,SAP_APPROVED_BY,TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),A.AGMT_REV,A.CONT_REV,A.TAX_STRUCT_CD,  "
								+ "NVL(A.NET_PAYABLE_AMT,0)-NVL(A.INVOICE_AMT,0),A.TCS_STRUCT_CD,A.GROSS_AMT_USD,A.INVOICE_CATEGORY,A.INVOICE_TYPE,NULL,TO_CHAR(APPROVED_DT,'DD/MM/YYYY') "
								+ "FROM FMS_PUR_FFLOW_INV_MST A "
								+ "WHERE COMPANY_CD=? "
								+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND "+segmentType+" AND PDF_INV_DTL IS NOT NULL AND "+invFlag+" "
								+ "AND SAP_APPROVAL=? AND FIN_SYS=? "
								+ "AND NOT EXISTS (SELECT INV_TITLE FROM FMS_PUR_FFLOW_INV_FILE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.INVOICE_SEQ=B.INVOICE_SEQ  "
								+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.INVOICE_TYPE=B.INVOICE_TYPE AND B.INV_TITLE IN (?,?) )";
						if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("F"))
						{
							segmentType="A.CONTRACT_TYPE IN ('R','C','K') ";
							queryString+="UNION ALL ";
							queryString+="SELECT COUNTERPARTY_CD,CONT_NO,CONTRACT_TYPE,BU_UNIT,INVOICE_SEQ,A.INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),   "
									+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),   "
									+ "FREQ,ALLOC_QTY,NULL,NULL,NULL,   "
									+ "GROSS_AMT,   "
									+ "TAX_AMT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,    "
									+ "EXCHG_RATE_VALUE,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),INVOICE_RAISED_IN,TO_CHAR(INVOICE_DT, 'Month'),   "
									+ "CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,NULL,NULL,NULL,AGMT_NO,FINANCIAL_YEAR,NULL,SAP_APPROVAL,    "
									+ "TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),TCS_TDS,NULL,'GTA_S','GTA_SG',NULL,NULL,A.SYS_INV_NO,NULL,   "
									+ "NULL,NULL,NULL,NULL,SAP_APPROVED_BY,TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),A.AGMT_REV,A.CONT_REV,A.TAX_STRUCT_CD,   "
									+ "NVL(A.NET_PAYABLE_AMT,0)-NVL(A.INVOICE_AMT,0),NULL,NULL,NULL,A.INVOICE_TYPE,NULL,TO_CHAR(APPROVED_DT,'DD/MM/YYYY')  "
									+ "FROM FMS_GTA_SG_INV_MST A  "
									+ "WHERE COMPANY_CD=? "
									+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND " + segmentType + " AND PDF_INV_DTL IS NOT NULL "
									+ "AND SAP_APPROVAL=? AND FIN_SYS=? "
									+ "AND NOT EXISTS (SELECT INV_TITLE FROM FMS_GTA_INV_FILE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.INVOICE_SEQ=B.INVOICE_SEQ "
									+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.INVOICE_TYPE=B.INVOICE_TYPE AND B.INV_TITLE IN (?,?) )";
							queryString+="UNION ALL ";
							queryString+="SELECT COUNTERPARTY_CD,CONT_NO,CONTRACT_TYPE,BU_UNIT,INVOICE_SEQ,A.INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'), "
									+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'), "
									+ "FREQ,ALLOC_QTY,NULL,NULL,NULL, "
									+ "GROSS_AMT, "
									+ "TAX_AMT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT, "
									+ "EXCHG_RATE_VALUE,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),INVOICE_RAISED_IN,TO_CHAR(INVOICE_DT, 'Month'),"
									+ "CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,NULL,NULL,NULL,AGMT_NO,FINANCIAL_YEAR,NULL,SAP_APPROVAL, "
									+ "TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),TCS_TDS,NULL,'GTA_P','GTA_PG',NULL,NULL,A.SYS_INV_NO,NULL, "
									+ "NULL,NULL,NULL,NULL,SAP_APPROVED_BY,TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),A.AGMT_REV,A.CONT_REV,A.TAX_STRUCT_CD, "
									+ "NVL(A.NET_PAYABLE_AMT,0)-NVL(A.INVOICE_AMT,0),NULL,NULL,NULL,A.INVOICE_TYPE,NULL,TO_CHAR(APPROVED_DT,'DD/MM/YYYY') "
									+ "FROM FMS_GTA_PG_INV_MST A "
									+ "WHERE COMPANY_CD=? "
									+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND " + segmentType + " AND PDF_INV_DTL IS NOT NULL "
									+ "AND SAP_APPROVAL=? AND FIN_SYS=? "
									+ "AND NOT EXISTS (SELECT INV_TITLE FROM FMS_GTA_INV_FILE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.INVOICE_SEQ=B.INVOICE_SEQ "
									+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.INVOICE_TYPE=B.INVOICE_TYPE AND B.INV_TITLE IN (?,?) )";
							queryString+="UNION ALL ";
							queryString+="SELECT COUNTERPARTY_CD,CONT_NO,CONTRACT_TYPE,BU_UNIT,INVOICE_SEQ,INVOICE_REF,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),   "
									+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),   "
									+ "FREQ,ALLOC_QTY,NULL,NULL,NULL,   "
									+ "GROSS_AMT_INR,   "
									+ "TAX_AMT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,    "
									+ "EXCHG_RATE_VALUE,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),INVOICE_RAISED_IN,TO_CHAR(INVOICE_DT, 'Month'),   "
									+ "CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,TCS_CERT_FLAG,TCS_AMT,NULL,AGMT_NO,FINANCIAL_YEAR,SAP_EXCHNG_RATE,SAP_APPROVAL,    "
									+ "TDS_AMT,NULL,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),TCS_TDS,NULL,'GTA_FF','GTA_FFLOW',NULL,NULL,INVOICE_NO,NULL,   "
									+ "NULL,NULL,NULL,NULL,SAP_APPROVED_BY,TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),A.AGMT_REV,A.CONT_REV,A.TAX_STRUCT_CD,   "
									+ "NVL(A.NET_PAYABLE_AMT,0)-NVL(A.INVOICE_AMT,0),A.TCS_STRUCT_CD,A.GROSS_AMT_USD,A.INVOICE_CATEGORY,A.INVOICE_TYPE,NULL,TO_CHAR(APPROVED_DT,'DD/MM/YYYY')  "
									+ "FROM FMS_GTA_FFLOW_INV_MST A "
									+ "WHERE COMPANY_CD=? "
									+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND " + segmentType + " AND PDF_INV_DTL IS NOT NULL "
									+ "AND SAP_APPROVAL=? AND FIN_SYS=? "
									+ "AND NOT EXISTS (SELECT INV_TITLE FROM FMS_GTA_FFLOW_INV_FILE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.INVOICE_SEQ=B.INVOICE_SEQ "
									+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.INVOICE_TYPE=B.INVOICE_TYPE AND B.INV_TITLE IN (?,?) )";
							//for derivative inward invoice 
							segmentType="AND A.INV_TYPE='R' ";
							queryString+="UNION ALL ";
							queryString+="SELECT A.COUNTERPARTY_CD,A.CONT_NO,A.CONTRACT_TYPE,A.BU_UNIT,A.INVOICE_SEQ,A.INVOICE_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'), "
									+ "TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),  "
									+ "A.FREQ,A.ALLOC_QTY,A.SALE_PRICE,A.SALE_PRICE_UNIT,A.SALE_AMT, "
									+ "A.INVOICE_AMT, "
									+ "NULL,A.INVOICE_AMT,NULL,NULL,A.NET_PAYABLE_AMT, "
									+ "NULL,NULL,A.INVOICE_RAISED_IN,TO_CHAR(A.INVOICE_DT, 'Month'), "
									+ "A.CHECKED_FLAG,A.AUTHORIZED_FLAG,A.APPROVED_FLAG,NULL,NULL,NULL,A.AGMT_NO,A.FINANCIAL_YEAR,NULL,A.SAP_APPROVAL, "
									+ "NULL,NULL,NULL,NULL,NULL,NULL,'DERV','DERV',A.INSTRUMENT_NO,NULL,INVOICE_REF_NO,NULL, "
									+ "NULL,NULL,NULL,A.PLANT_SEQ,A.SAP_APPROVED_BY,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),A.AGMT_REV,A.CONT_REV,NULL,  "
									+ "NVL(A.NET_PAYABLE_AMT,0)-NVL(A.INVOICE_AMT,0),NULL,NULL,NULL,A.INV_TYPE,NULL,TO_CHAR(APPROVED_DT,'DD/MM/YYYY') "
									+ "FROM FMS_DERV_INVOICE_MST A  "
									+ "WHERE COMPANY_CD=? "
									+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY')  "
									+ " "+ segmentType+" AND A.PDF_INV_DTL IS NOT NULL "
									+ "AND SAP_APPROVAL=? AND FIN_SYS=? "
									+ "AND NOT EXISTS (SELECT PDF_TYPE FROM FMS_DERV_INV_FILE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN  "
									+ "AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.INV_TYPE=B.INV_TYPE AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND B.PDF_TYPE IN (?,?))";
						}
					}
				//queryString+="ORDER BY CONT_NO";
				String temp_queryString=queryString;
				stmt=conn.prepareStatement(temp_queryString);
				stmt.setString(++cnt, comp_cd);
				stmt.setString(++cnt, from_dt);
				stmt.setString(++cnt, to_dt);
				stmt.setString(++cnt, "Y");
				stmt.setString(++cnt, "S");
				stmt.setString(++cnt, "S");
				stmt.setString(++cnt, "X");
				stmt.setString(++cnt, comp_cd);
				stmt.setString(++cnt, from_dt);
				stmt.setString(++cnt, to_dt);
				stmt.setString(++cnt, "Y");
				stmt.setString(++cnt, "S");
				stmt.setString(++cnt, "S");
				stmt.setString(++cnt, "X");
				if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("F")||VTEMP_SEGMENT_TYPE.elementAt(i).equals("CF"))
				{
					stmt.setString(++cnt, comp_cd);
					stmt.setString(++cnt, from_dt);
					stmt.setString(++cnt, to_dt);
					stmt.setString(++cnt, "Y");
					stmt.setString(++cnt, "S");
					stmt.setString(++cnt, "S");
					stmt.setString(++cnt, "X");
					if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("F"))
					{
						stmt.setString(++cnt, comp_cd);
						stmt.setString(++cnt, from_dt);
						stmt.setString(++cnt, to_dt);
						stmt.setString(++cnt, "Y");
						stmt.setString(++cnt, "S");
						stmt.setString(++cnt, "S");
						stmt.setString(++cnt, "X");
						stmt.setString(++cnt, comp_cd);
						stmt.setString(++cnt, from_dt);
						stmt.setString(++cnt, to_dt);
						stmt.setString(++cnt, "Y");
						stmt.setString(++cnt, "S");
						stmt.setString(++cnt, "S");
						stmt.setString(++cnt, "X");
						stmt.setString(++cnt, comp_cd);
						stmt.setString(++cnt, from_dt);
						stmt.setString(++cnt, to_dt);
						stmt.setString(++cnt, "Y");
						stmt.setString(++cnt, "S");
						stmt.setString(++cnt, "S");
						stmt.setString(++cnt, "X");
						stmt.setString(++cnt, comp_cd);
						stmt.setString(++cnt, from_dt);
						stmt.setString(++cnt, to_dt);
						stmt.setString(++cnt, "Y");
						stmt.setString(++cnt, "S");
						stmt.setString(++cnt, "S");
						stmt.setString(++cnt, "X");
					}
				}
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String counterparty_cd=rset.getString(1)==null?"":rset.getString(1);
					String cont_no=rset.getString(2)==null?"":rset.getString(2);
					String cont_type=rset.getString(3)==null?"":rset.getString(3);
					String bu_unit=rset.getString(4)==null?"":rset.getString(4);
					String inv_seq=rset.getString(5)==null?"":rset.getString(5);
					String invoice_no=rset.getString(6)==null?"":rset.getString(6);
					String invoice_dt=rset.getString(7)==null?"":rset.getString(7);
					String inv_due_dt=rset.getString(8)==null?"":rset.getString(8);
					String period_st_dt=rset.getString(9)==null?"":rset.getString(9);
					String period_end_dt=rset.getString(10)==null?"":rset.getString(10);
					String billing_freq=rset.getString(11)==null?"":rset.getString(11);
					String alloc_qty=rset.getString(12)==null?"":nf.format(rset.getDouble(12));
					String sale_price=rset.getString(13)==null?"":nf.format(rset.getDouble(13));
					String gross_amt=rset.getString(16)==null?"":nf.format(rset.getDouble(16));
					String tax_amt=rset.getString(17)==null?"":nf.format(rset.getDouble(17));
					String inv_amt=rset.getString(18)==null?"":nf.format(rset.getDouble(18));
					String net_payable=rset.getString(21)==null?"":nf.format(rset.getDouble(21));
					String exchng_rate=rset.getString(22)==null?"":nf.format(rset.getDouble(22));
					String invoice_raised_in=rset.getString(24)==null?"":rset.getString(24);
					String tcs_amt=rset.getString(30)==null?"":nf.format(rset.getDouble(30));
					String agmt_no=rset.getString(32)==null?"0":rset.getString(32);
					String fin_year=rset.getString(33)==null?"":rset.getString(33);
					String tds_amt=rset.getString(36)==null?"":nf.format(rset.getDouble(36));
					String tds_factor=rset.getString(37)==null?"":rset.getString(37);
					String tdsStructCd=rset.getString(38)==null?"":rset.getString(38);
					String tcs_tds=rset.getString(40)==null?"":rset.getString(40);
					String inv_flag=rset.getString(41)==null?"":rset.getString(41);
					String purchase_type_flag=rset.getString(42)==null?"":rset.getString(42);
					String cargo_no=rset.getString(44)==null?"":rset.getString(44);
					String remittance_no = rset.getString(46)==null?"":rset.getString(46);
					String plant_seq_no=rset.getString(51)==null?"":rset.getString(51);
					String sun_approved_dt=rset.getString(53)==null?"0":rset.getString(53);
					String agmt_rev=rset.getString(54)==null?"":rset.getString(54);
					String cont_rev=rset.getString(55)==null?"":rset.getString(55);
					String taxStructCd=rset.getString(56)==null?"":rset.getString(56);
					double intrest_amt=rset.getDouble(57);
					String tcsStructCd=rset.getString(58)==null?"":rset.getString(58);
					String gross_amt_usd=rset.getString(59)==null?"":rset.getString(59);
					String invoice_type=rset.getString(61)==null?"":rset.getString(61);
					String cd_paid_amt=rset.getString(62)==null?"":nf.format(rset.getDouble(62));
					String inv_approve_dt=rset.getString(63)==null?"":rset.getString(63);
					
					String ledger="A";
					String cccac ="1TM01";
					String ecac ="LNA";
					String cccaac ="142100";
					String statutory_payment = "243991"; 
					String bucac="SEI";	
					String miscellaneous_ac_code = "839999";
					String trans_acc_cd="802031";
					String igx_account_cd="153995";
					
					String bu_state_tin=utilBean.getState_TIN(conn, comp_cd, comp_cd, "B", bu_unit);
					String state_cd = getStateCd(bu_state_tin);
					String pur_account_cd="";
					String currency_unit="INR";
					String display_deal_map=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, cont_type, cargo_no);
					
					if(purchase_type_flag.equals("DERV"))
					{
						//for finding the diff in price
						String diff_amt="";
						String currency_cd="USD";
						if(!gross_amt.equals("")&&!alloc_qty.equals(""))
						{
							diff_amt=nf.format(Double.parseDouble(gross_amt)/Double.parseDouble(alloc_qty));
							if(Double.parseDouble(diff_amt)<0)
							{
								diff_amt="("+diff_amt+")";
							}
						}
						String trans_dt=invoice_dt.split("/")[0]+invoice_dt.split("/")[1]+invoice_dt.split("/")[2];
						//inv_due_dt = inv_due_dt.split("/")[0]+inv_due_dt.split("/")[1]+inv_due_dt.split("/")[2];
						inv_due_dt = inv_due_dt.replace("/","");
						journal_type="FMSPR";
						String qty_unit=getQuantityUnit(counterparty_cd,agmt_no,"U",agmt_rev,cont_no,cont_type,cont_rev,cargo_no);		//here cargo_no represents instrument
						String description=(utilBean.getCounterpartyABBR(conn, counterparty_cd)+" "+alloc_qty+" "+qty_unit+" @ "+diff_amt+" MTM").toUpperCase();
						String account_cd=getCounterpartySunAccountCode(counterparty_cd, "T", "0", "V");
						String account_cd_lng_sale="711008"; 	//for derivative 711008 code is used
						String dr_cr_marker=Double.parseDouble(gross_amt)<0?"C":"D";
						String accPeriod=getAccountingPeriod(invoice_dt);
						cccac="2MK01";
						
						for(int j=0;j<2;j++)
						{
							index+=1;
							//VINVOICE_NO.add(invoice_no);
							VINVOICE_NO.add(remittance_no);		//AS PE RARUN JAIN FEEDBACK TO SHOW PARTY GENERATED INVOICE NO 
							VDIS_CONT_MAPPING.add(display_deal_map);
							VJOURNAL_TYPE.add(journal_type);
							VAPPROVAL_DT.add(sun_approved_dt);
							VLEDGER.add(ledger);
							if(j==0)
							{
								VACCOUNT_CD.add(account_cd);
								VDEBIT_CREDIT.add(dr_cr_marker);
								VCOA_CD.add(account_cd_lng_sale);
							}
							else
							{
								dr_cr_marker=Double.parseDouble(gross_amt)<0?"D":"C";
								VACCOUNT_CD.add(account_cd_lng_sale);
								VDEBIT_CREDIT.add(dr_cr_marker);
								VCOA_CD.add("");
							}
							VPERIOD_START_DT.add(accPeriod);
							VBASE_AMT.add("");
							VTRANS_AMT.add(inv_amt);
							VREPORT_AMT.add("");
							VCURRENCY_CD.add(currency_cd);
							VEXCHNG_RATE.add("");
							VINVOICE_DT.add(trans_dt);
							VDESC.add(description);
							VINVOICE_DUE_DT.add(inv_due_dt);
							VCOST_CTR_CD.add(cccac);
							VEMPLOYEE_CD.add("");
							VCODE.add("NA");
							VBU_UNIT_CD.add(bucac);
						}
					}
					else
					{
						String trader_sun_acc_cd="";
						
						if(purchase_type_flag.equals("GTA_S")||purchase_type_flag.equals("GTA_P")||purchase_type_flag.equals("GTA_FF"))
						{
							trader_sun_acc_cd=getCounterpartySunAccountCode(counterparty_cd,"R","0","DFT");
							bucac="LNG";
						}
						else
						{
							if(!cont_type.equals("N"))
							{
								trader_sun_acc_cd=getCounterpartySunAccountCode(counterparty_cd,"T",plant_seq_no,"DFT");
								if(trader_sun_acc_cd.equals(""))
								{
									trader_sun_acc_cd=getCounterpartySunAccountCode(counterparty_cd,"T","0","DFT");
								}
							}
							else
							{
								trader_sun_acc_cd=getCounterpartySunAccountCode(counterparty_cd,"T","0","DFT");
							}
						}
						
						
						if(journal_type.equals("FMSFI") && !cont_type.equals("N"))
						{
							journal_type="FMSPR";
						}
						
						//NOTE: In FMS8: Accounting period for the provisional invoice is month_code+ACT_ARRV_DT(year) and transaction date is also ACT_ARRV_DT
						//NOTE for Provisional Custom DUTY 
						/*
					 	1. the Group Foreign Exchange Rate is used 
					  	2. For Custom Duty The tax BCD Tax.
					  	3. transaction reference -> PROVN-Vessel Name
					  	4. due date is empty
					  	5. for custom duty the transaction date is custom duty date 
						 */
						
						//for fetching the actual arrival date of the cargo
						String act_arr_dt="";
						String query="SELECT TO_CHAR(BOE_DT,'DD/MM/YYYY') FROM FMS_BUY_CARGO_ALLOC_BOE A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? "
								+ "AND CARGO_NO=? AND CONT_REV=? AND ALLOC_REV=(SELECT MAX(ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC_BOE B "
								+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE  "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO  "
								+ "AND A.CONT_REV=B.CONT_REV  AND A.CARGO_NO=B.CARGO_NO)";
						stmt1=conn.prepareStatement(query);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, "M");
						stmt1.setString(4, agmt_no);
						stmt1.setString(5, agmt_rev);
						stmt1.setString(6, cont_type);
						stmt1.setString(7, cont_no);
						stmt1.setString(8, cargo_no);
						stmt1.setString(9, cont_rev);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							act_arr_dt=rset1.getString(1)==null?"":rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						String ship_cd="";
						String query_str1="SELECT TO_CHAR(ACT_ARRV_DT,'DD/MM/YYYY'),SHIP_CD FROM FMS_BUY_CARGO_ALLOC A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? "
								+ "AND CARGO_NO=? AND CONT_REV=? AND ALLOC_REV=(SELECT MAX(ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC B "
								+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE  "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO  "
								+ "AND A.CONT_REV=B.CONT_REV  AND A.CARGO_NO=B.CARGO_NO)";
						query_str1+="UNION ";
						query_str1+="SELECT TO_CHAR(EXP_TO_DT,'DD/MM/YYYY'),SHIP_CD FROM FMS_BUY_CARGO_NOM A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? "
								+ "AND CARGO_NO=? AND CONT_REV=? AND NOM_REV=(SELECT MAX(NOM_REV) FROM FMS_BUY_CARGO_NOM B "
								+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO "
								+ "AND A.CONT_REV=B.CONT_REV  AND A.CARGO_NO=B.CARGO_NO)";
						stmt1=conn.prepareStatement(query_str1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, "M");
						stmt1.setString(4, agmt_no);
						stmt1.setString(5, agmt_rev);
						stmt1.setString(6, cont_type);
						stmt1.setString(7, cont_no);
						stmt1.setString(8, cargo_no);
						stmt1.setString(9, cont_rev);
						stmt1.setString(10, comp_cd);
						stmt1.setString(11, counterparty_cd);
						stmt1.setString(12, "M");
						stmt1.setString(13, agmt_no);
						stmt1.setString(14, agmt_rev);
						stmt1.setString(15, cont_type);
						stmt1.setString(16, cont_no);
						stmt1.setString(17, cargo_no);
						stmt1.setString(18, cont_rev);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							//act_arr_dt=rset1.getString(1)==null?"":rset1.getString(1);
							ship_cd=rset1.getString(2)==null?"":rset1.getString(2);
						}
						rset1.close();
						stmt1.close();
						
						act_arr_dt=act_arr_dt.equals("")?invoice_dt:act_arr_dt;
						
						String custom_account_cd="";
						String custom_duty_tax_cd="";
						String tax_acc_cd="";
						String base_amt="";
						String final_custom_amt="";
						String report_amt=nf.format(0);
						String code="";
						String analysis_cd5="";
						
						String description=utilBean.getCounterpartyABBR(conn, counterparty_cd)+" "+alloc_qty+" MMBTU @ "+sale_price+" "+utilBean.getShipName(conn, ship_cd);
						String description1="";
						String tax_description="";
						String tcs_tds_description="";
						String billing_note="";
						if(segment.equals("P"))
						{
							pur_account_cd=getCounterpartySunAccountCode(counterparty_cd,"T","0","S");
							currency_unit="USD";
							
							double exc_rate = getExchg_rate("SBI RATE SELL",act_arr_dt);		//For Provisional Cargo SBI TT Selling rate is used
							if(Double.doubleToRawLongBits(exc_rate)==Double.doubleToRawLongBits(0))
							{
								base_amt="-";
								exchng_rate="-";
							}
							else
							{
								exchng_rate=""+exc_rate;
								base_amt = nf.format(Double.parseDouble(exchng_rate) * Double.parseDouble(inv_amt));
							}
						}
						else if(segment.equals("CP"))
						{
							//For Custom Duty The tax BCD Tax.
							pur_account_cd=getTcsTdsStructureSunAccountCd(taxStructCd, bu_state_tin, bu_unit);		
							currency_unit="INR";
							act_arr_dt=invoice_dt;
							
							//exc_rate: needs to changed from SBI TT Selling rate to Foreign Exchange rate 
							/*double exc_rate = getExchg_rate("4", act_arr_dt);		//For Provisional Custom Duty Cargo Group Foreign Exchange Rate is used
						if (exc_rate==0) 
						{
							base_amt = "0";
							exchng_rate = "-";
						} */
							if(!exchng_rate.equals(""))
							{
								//base_amt = nf.format(Double.parseDouble(exchng_rate) * Double.parseDouble(net_payable));
								base_amt = nf.format(Double.parseDouble(net_payable));
								//exchng_rate=""+exc_rate;
							}
							//invoice_no="PROVN-"+utilBean.getShipName(conn, ship_cd);
						}
						else if(segment.equals("F")) 
						{
							if(cont_type.equals("N"))
							{
								report_amt=nf.format(0);
								pur_account_cd = getCounterpartySunAccountCode(counterparty_cd, "T", "0", "S");
								currency_unit="USD";
								double exc_rate = getExchg_rate("SBI RATE SELL", act_arr_dt);			//SBI TT Selling Rate
								if (Double.doubleToRawLongBits(exc_rate)==Double.doubleToRawLongBits(0)) 
								{
									base_amt = "-";
									exchng_rate = "-";
								} 
								else 
								{
									exchng_rate=""+exc_rate;	
									//base_amt = nf.format(Double.parseDouble(exchng_rate) * Double.parseDouble(inv_amt));
									base_amt=net_payable;
									net_payable = nf.format(Double.parseDouble(base_amt)*Double.parseDouble(exchng_rate));
								}
								description=utilBean.getCounterpartyABBR(conn, counterparty_cd)+" "+0+" MMBTU @ "+sale_price+" "+utilBean.getShipName(conn, ship_cd);
							}
							else
							{
								//for transaction date 
								
								invoice_dt=utilDate.getMonthNameMON(period_end_dt).equals(utilDate.getMonthNameMON(inv_approve_dt))?inv_approve_dt:period_end_dt;
								
								act_arr_dt=invoice_dt;
								
								if(cont_type.equals("I"))
								{
									pur_account_cd=getCounterpartySunAccountCode(counterparty_cd, "T", plant_seq_no, "I");		//fetching the purchase account code plant wise
									if(pur_account_cd.equals(""))
									{
										pur_account_cd=getCounterpartySunAccountCode(counterparty_cd, "T", "0", "I");	//if the account code is not configured for the plant
									}
								}
								else
								{
									pur_account_cd=getCounterpartySunAccountCode(counterparty_cd, "T", plant_seq_no, "S");		//fetching the purchase account code plant wise
									if(pur_account_cd.equals(""))
									{
										pur_account_cd=getCounterpartySunAccountCode(counterparty_cd, "T", "0", "S");	//if the account code is not configured for the plant
									}
								}
								
								currency_unit="INR";
								
								base_amt=gross_amt;
								
								if(invoice_raised_in.equals("2"))
								{
									if(purchase_type_flag.equals("FF")||purchase_type_flag.equals("GTA_FF"))
									{
										base_amt=gross_amt_usd;
									}
									report_amt=nf.format(0);
									currency_unit="USD";
									if(!exchng_rate.contentEquals(""))
									{
										if(purchase_type_flag.equals("FF")||purchase_type_flag.equals("GTA_FF"))
										{
											sale_price=nf.format(Double.parseDouble(base_amt)/Double.parseDouble(alloc_qty));
											net_payable=nf.format(Double.parseDouble(gross_amt_usd)*Double.parseDouble(exchng_rate));
										}
										else
										{
											net_payable=nf.format(Double.parseDouble(gross_amt)*Double.parseDouble(exchng_rate));
										}
									}
								}
								else
								{
									net_payable=base_amt;
									if(purchase_type_flag.equals("FF")||purchase_type_flag.equals("GTA_FF"))
									{
										sale_price=nf.format(Double.parseDouble(base_amt)/Double.parseDouble(alloc_qty));
									}
									if(!exchng_rate.contentEquals(""))
									{
										if(Double.doubleToRawLongBits(Double.parseDouble(exchng_rate))!=Double.doubleToRawLongBits(0))
										{
											report_amt=nf.format(Double.parseDouble(net_payable)/Double.parseDouble(exchng_rate));
										}
										else
										{
											report_amt=nf.format(0);
										}
									}
									else
									{
										report_amt=nf.format(0);
									}
									analysis_cd5="NA";
								}
								if(billing_freq.equals("1")||billing_freq.equals("2"))
								{
									billing_note=billing_freq+"FN";
								}
								else if(billing_freq.equals("3") || billing_freq.equals("4") || billing_freq.equals("5") || billing_freq.equals("6") || billing_freq.equals("9"))
								{
									billing_note=billing_freq+"W";
								}
								else if(billing_freq.equals("7"))
								{
									billing_note=billing_freq+"M";
								}
								else if(billing_freq.equals("8"))
								{
									billing_note=billing_freq+"O";
								}
								else if(billing_freq.equals("10"))
								{
									billing_note=billing_freq+"D";
								}
								
								if(!bu_state_tin.equals("24"))
								{
									cccac=2+state_cd+"01";
								}
								else
								{
									cccac="1TM01";
								}
								cccaac=pur_account_cd;
								
								if(cont_type.equals("I"))
								{
									code="VCQ3";
								}
								else 
								{
									if(tcs_tds.equals("TCS"))
									{
										code="NA";
									}
									else
									{
										//code="WCQ3";		//for financial year 2023-24
										//code="XCQ3";
										
										if(!tds_amt.equals("") && Double.doubleToRawLongBits(Double.parseDouble(tds_amt))>Double.doubleToRawLongBits(0))
										{
											code="YCQ3";		//20250728: AS PER ARUN INSTRUCTION: CHANGED FOR FINANCIAL YEAR 2025-2026
										}
										else
										{
											code="NA";			//20250811: AS PER ARUN FEEDBACK: CHANGED TO 'NA' IF 0% TDS IS APPLICABLE
										}
									}
								}
								
								String mtn=utilDate.getMonthNameMON(period_st_dt);
								description=utilBean.getCounterpartyABBR(conn, counterparty_cd)+" "+state_cd+" "+alloc_qty+" MMBTU @ "+sale_price+" "+billing_note+" "+mtn;
								if(!exchng_rate.equals(""))
								{
									if(Double.doubleToRawLongBits(Double.parseDouble(exchng_rate))!=Double.doubleToRawLongBits(0))
									{
										description+=" @"+exchng_rate;
									}
								}
								if(exchng_rate.equals(""))
								{
									exchng_rate="0";
								}
							}
							if(purchase_type_flag.equals("GTA_S") || purchase_type_flag.equals("GTA_P") || purchase_type_flag.equals("GTA_FF"))
							{
								
								act_arr_dt=invoice_dt;
								//act_arr_dt=period_st_dt;
								pur_account_cd=getCounterpartySunAccountCode(counterparty_cd, "R", "0", "R");
								if(cont_type.equals("K"))	//for parking 
								{
									pur_account_cd=getCounterpartySunAccountCode(counterparty_cd, "R", "0", "K");
								}
								cccaac=pur_account_cd;
								//description1=utilBean.getCounterpartyABBR(conn, counterparty_cd)+"  GAS TRANSMISSION CH  "+period_st_dt.split("/")[0]+" - "+period_end_dt; 
								description=utilBean.getCounterpartyABBR(conn, counterparty_cd)+"  GAS TRANSMISSION CH  "+invoice_dt.split("/")[0]+" - "+period_end_dt; 
								
								//code
								if(tds_factor.equals(""))
								{
									tds_factor=getTdsFactor(tdsStructCd);
								}
								code=getTdsAnalysisCode(tds_factor);
								
							}
							
						}
						else if(segment.equals("CF") )
						{
							//For Custom Duty The tax BCD Tax.
							custom_duty_tax_cd=getTcsTdsStructureSunAccountCd(taxStructCd, bu_state_tin, bu_unit);
							custom_account_cd=getCounterpartySunAccountCode(counterparty_cd, "T", "0", "CD");
							act_arr_dt=invoice_dt;
							//double exc_rate = getExchg_rate("4", act_arr_dt);			//SBI TT Selling Rate
							
							double diff_qty=getDeltaQunatity(counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, cont_type, cargo_no);
							String increment_decrement_qty="";
							String diff_qty_remark="";
							if(Double.doubleToRawLongBits(diff_qty)>Double.doubleToRawLongBits(0))
							{
								increment_decrement_qty=nf.format(diff_qty);
								diff_qty_remark="excess";
							}
							else if(Double.doubleToRawLongBits(diff_qty)<Double.doubleToRawLongBits(0))
							{
								increment_decrement_qty=nf.format(diff_qty*-1);
								diff_qty_remark="short";
							}
							else
							{
								increment_decrement_qty=nf.format(diff_qty);
							}
							description1 = (utilBean.getCounterpartyABBR(conn, counterparty_cd)+" "+diff_qty_remark+" "+increment_decrement_qty+" MMBTU @ "+sale_price+" "+utilBean.getShipName(conn, ship_cd)).toUpperCase();
							if(!net_payable.equals(""))
							{
								base_amt=net_payable;
								description =""+utilBean.getCounterpartyABBR(conn, counterparty_cd)+" Custom Duty Trf - "+utilBean.getShipName(conn, ship_cd);
								
								final_custom_amt=nf.format(Double.parseDouble(base_amt)-intrest_amt);
							}
						}
						String trans_dt=act_arr_dt.split("/")[0]+act_arr_dt.split("/")[1]+act_arr_dt.split("/")[2];
						String account_period=getAccountingPeriod(act_arr_dt);
						description = description.toUpperCase();
						inv_due_dt = inv_due_dt.split("/")[0]+inv_due_dt.split("/")[1]+inv_due_dt.split("/")[2];
						for(int j=0;j<2;j++)
						{
							index+=1;
							String report_amt_temp=report_amt;
							VDIS_CONT_MAPPING.add(display_deal_map);
							//VINVOICE_NO.add(remittance_no);		//system generated  
							VINVOICE_NO.add(invoice_no);
							VJOURNAL_TYPE.add(journal_type);
							VAPPROVAL_DT.add(sun_approved_dt);
							VLEDGER.add(ledger);
							VPERIOD_START_DT.add(account_period);
							if(segment.equals("P"))
							{
								if(j%2==0)
								{
									VACCOUNT_CD.add(pur_account_cd);
									VINVOICE_DUE_DT.add("");
									VEMPLOYEE_CD.add(ecac);
									VDEBIT_CREDIT.add("D");
									VCODE.add("");
								}
								else
								{
									VACCOUNT_CD.add(trader_sun_acc_cd);
									VINVOICE_DUE_DT.add(inv_due_dt);
									VEMPLOYEE_CD.add("");
									VDEBIT_CREDIT.add("C");
									VCODE.add("NA");
								}
								VBASE_AMT.add(base_amt);
								VCOST_CTR_CD.add(cccac);
								VTRANS_AMT.add(inv_amt);
								VCOA_CD.add(cccaac);
								VPROJECT_CD.add("");
								VDESC.add(description);
							}
							else if(segment.equals("CP"))
							{
								if(j%2==0)
								{
									VACCOUNT_CD.add(pur_account_cd);
									VCOST_CTR_CD.add(cccac);
									VCOA_CD.add(statutory_payment);
									VDEBIT_CREDIT.add("D");
									VCODE.add("");
								}
								else
								{
									VACCOUNT_CD.add(statutory_payment);
									VCOST_CTR_CD.add("");
									VCOA_CD.add(pur_account_cd);
									VDEBIT_CREDIT.add("C");
									VCODE.add("NA");
								}
								VBASE_AMT.add(base_amt);
								VINVOICE_DUE_DT.add("");
								VTRANS_AMT.add(base_amt);
								VEMPLOYEE_CD.add("");
								VPROJECT_CD.add("");
								VDESC.add(description);
							}
							else if(segment.equals("F")) 
							{
								if(!net_payable.equals(""))
								{
									String net_payable_temp="";
									String base_amt_temp="";
									if(Double.doubleToRawLongBits(Double.parseDouble(net_payable))>=Double.doubleToRawLongBits(0))
									{
										net_payable_temp=nf.format(Double.parseDouble(net_payable));
										base_amt_temp=nf.format(Double.parseDouble(base_amt));
										if(j%2==0)
										{
											String acc_cd=invoice_type.equals("CR")?trader_sun_acc_cd:pur_account_cd;
											String dr_cr_marker=invoice_type.equals("DR")?"C":"D";		//20250731: AS PER ARUN INSTRUCTION, SINCE IN DEBIT NOTE THE AMOUNT IS CREDITED INTO SEI ACCOUNT
											VACCOUNT_CD.add(acc_cd);
											//VDEBIT_CREDIT.add("D");
											VDEBIT_CREDIT.add(dr_cr_marker);
											if(purchase_type_flag.equals("GTA_S")||purchase_type_flag.equals("GTA_P")||purchase_type_flag.equals("GTA_FF"))
											{
												VINVOICE_DUE_DT.add(inv_due_dt);
												VCODE.add(code);
											}
											else
											{
												VINVOICE_DUE_DT.add("");
												VCODE.add("");
											}
											VEMPLOYEE_CD.add(ecac);
											VPROJECT_CD.add("");
										}
										else
										{
											if(!cont_type.equals("N") && !purchase_type_flag.equals("GTA_S") && !purchase_type_flag.equals("GTA_P") && !purchase_type_flag.equals("GTA_FF"))
											{
												net_payable_temp=nf.format(Double.parseDouble(net_payable)*-1);
												base_amt_temp=nf.format(Double.parseDouble(base_amt)*-1);
												report_amt_temp=Double.doubleToRawLongBits(Double.parseDouble(report_amt_temp))==Double.doubleToRawLongBits(0)?nf.format(0):nf.format(Double.parseDouble(report_amt_temp)*(-1));
											}
											
											String acc_cd=invoice_type.equals("CR")?pur_account_cd:trader_sun_acc_cd;
											String dr_cr_marker=invoice_type.equals("DR")?"D":"C";
											VACCOUNT_CD.add(acc_cd);
											//VDEBIT_CREDIT.add("C");
											VDEBIT_CREDIT.add(dr_cr_marker);
											VINVOICE_DUE_DT.add(inv_due_dt);
											VPROJECT_CD.add(analysis_cd5);
											if(purchase_type_flag.equals("GTA_S")||purchase_type_flag.equals("GTA_P")||purchase_type_flag.equals("GTA_FF"))
											{
												VEMPLOYEE_CD.add(ecac);
											}
											else
											{
												VEMPLOYEE_CD.add("");
											}
											if(cont_type.equals("N"))
											{
												VCODE.add("NA");
											}
											else
											{
												VCODE.add(code);
											}
										}
									}
									else
									{
										net_payable_temp=nf.format(Double.parseDouble(net_payable)*-1);
										base_amt_temp=nf.format(Double.parseDouble(base_amt)*-1);
										if(j%2==0)
										{
											VACCOUNT_CD.add(trader_sun_acc_cd);
											VDEBIT_CREDIT.add("D");
											VINVOICE_DUE_DT.add(inv_due_dt);
											VEMPLOYEE_CD.add("");
											VCODE.add("NA");
										}
										else
										{
											VACCOUNT_CD.add(pur_account_cd);
											VDEBIT_CREDIT.add("C");
											VINVOICE_DUE_DT.add("");
											VEMPLOYEE_CD.add(ecac);
											VCODE.add("");
										}
										VPROJECT_CD.add("");
									}
									VBASE_AMT.add(net_payable_temp);
									VTRANS_AMT.add(base_amt_temp);
									VCOST_CTR_CD.add(cccac);
									VCOA_CD.add(cccaac);
								}
								else
								{
									VACCOUNT_CD.add("");
									VDEBIT_CREDIT.add("");
									VINVOICE_DUE_DT.add("");
									VEMPLOYEE_CD.add("");
									VCODE.add("");
									VTRANS_AMT.add("");
									VCOST_CTR_CD.add("");
									VCOA_CD.add("");
									VPROJECT_CD.add("");
								}
								VDESC.add(description);
							}
							else if(segment.equals("CF"))
							{
								if(!net_payable.equals(""))
								{
									if(Double.doubleToRawLongBits(Double.parseDouble(net_payable))>=Double.doubleToRawLongBits(0))
									{
										if(j%2==0)
										{
											VACCOUNT_CD.add(custom_duty_tax_cd);
											VDEBIT_CREDIT.add("D");
											VCOA_CD.add(statutory_payment);
											VCOST_CTR_CD.add(cccac);
											VCODE.add("");
										}
										else
										{
											VACCOUNT_CD.add(statutory_payment);
											VDEBIT_CREDIT.add("C");
											VCOST_CTR_CD.add("");
											VCOA_CD.add(custom_duty_tax_cd);
											VCODE.add("NA");
											
										}
										VBASE_AMT.add(net_payable);
										VTRANS_AMT.add(net_payable);
										VINVOICE_DUE_DT.add("");
									}
									else
									{
										if(j%2==0)
										{
											VACCOUNT_CD.add(custom_account_cd);
											VDEBIT_CREDIT.add("D");
											VCOA_CD.add(custom_duty_tax_cd);
										}
										else
										{
											VACCOUNT_CD.add(custom_duty_tax_cd);
											VDEBIT_CREDIT.add("C");
											VCOA_CD.add(custom_account_cd);
										}
										VBASE_AMT.add(base_amt);
										VTRANS_AMT.add(base_amt);
										VINVOICE_DUE_DT.add("");
										VCODE.add("");
										VCOST_CTR_CD.add(cccac);
									}
									VEMPLOYEE_CD.add("");
									VPROJECT_CD.add("");
								}
								VDESC.add(description1);
							}
							
							VREPORT_AMT.add(report_amt_temp);
							VCURRENCY_CD.add(currency_unit);
							VEXCHNG_RATE.add(exchng_rate);
							VINVOICE_DT.add(trans_dt);
							VBU_UNIT_CD.add(bucac);
							
							//for IGX settlement account
							if(cont_type.equals("I") && !igx_account_cd.equals(""))
							{
								index+=1;
								VDIS_CONT_MAPPING.add(display_deal_map);
								//VINVOICE_NO.add(remittance_no);		//system generated  
								VINVOICE_NO.add(invoice_no);
								VJOURNAL_TYPE.add(journal_type);
								VAPPROVAL_DT.add(sun_approved_dt);
								VLEDGER.add(ledger);
								VPERIOD_START_DT.add(account_period);
								
								String net_payable_temp="";
								String base_amt_temp="";
								if(j==0)
								{
									if(!net_payable.equals(""))
									{
										if(Double.doubleToRawLongBits(Double.parseDouble(net_payable))>=Double.doubleToRawLongBits(0))
										{
											net_payable_temp=nf.format(Double.parseDouble(net_payable));
											base_amt_temp=nf.format(Double.parseDouble(base_amt));
											VACCOUNT_CD.add(igx_account_cd);
											VDEBIT_CREDIT.add("C");
											VINVOICE_DUE_DT.add("");
											VEMPLOYEE_CD.add(ecac);
											VCODE.add("");
											VPROJECT_CD.add("");
										}
									}
								}
								else
								{
									net_payable_temp=nf.format(Double.parseDouble(net_payable)*-1);
									base_amt_temp=nf.format(Double.parseDouble(base_amt)*-1);
									report_amt_temp=Double.doubleToRawLongBits(Double.parseDouble(report_amt_temp))==Double.doubleToRawLongBits(0)?nf.format(0):nf.format(Double.parseDouble(report_amt_temp)*(-1));
									
									String acc_cd=invoice_type.equals("CR")?pur_account_cd:trader_sun_acc_cd;
									VACCOUNT_CD.add(acc_cd);
									VDEBIT_CREDIT.add("D");
									VINVOICE_DUE_DT.add(inv_due_dt);
									VPROJECT_CD.add(analysis_cd5);
									VEMPLOYEE_CD.add("");
									VCODE.add(code);
								}
								VBASE_AMT.add(net_payable_temp);
								VTRANS_AMT.add(base_amt_temp);
								VCOST_CTR_CD.add(cccac);
								VCOA_CD.add(cccaac);
								VREPORT_AMT.add(report_amt_temp);
								VCURRENCY_CD.add(currency_unit);
								VEXCHNG_RATE.add(exchng_rate);
								VINVOICE_DT.add(trans_dt);
								VDESC.add(description);
								VBU_UNIT_CD.add(bucac);
							}
						}
						
						//for LNG, In-tank IGX
						if(segment.equals("F") && !cont_type.equals("N"))
						{
							String acc_cd="";
							String query1="";
							if(purchase_type_flag.equals("S"))
							{
								query1="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
										+ "FROM FMS_PUR_SG_INV_TAX_DTL "
										+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
										+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INV_FLAG=?";
							}
							else if(purchase_type_flag.equals("P"))
							{
								query1="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
										+ "FROM FMS_PUR_PG_INV_TAX_DTL "
										+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
										+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INV_FLAG=?";
							}
							else if(purchase_type_flag.equals("FF")) 
							{
								query1 = "SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
										+ "FROM FMS_PUR_FFLOW_INV_TAX_DTL " 
										+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
										+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=?";
							}
							else if(purchase_type_flag.equals("GTA_S"))
							{
								query1="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
										+ "FROM FMS_GTA_SG_INV_TAX_DTL "
										+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
										+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? ";
							}
							else if(purchase_type_flag.equals("GTA_P"))
							{
								query1="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
										+ "FROM FMS_GTA_PG_INV_TAX_DTL "
										+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
										+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? ";
							}
							else if(purchase_type_flag.equals("GTA_FF"))
							{
								query1="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
										+ "FROM FMS_GTA_FFLOW_INV_TAX_DTL "
										+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
										+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? ";
							}
							stmt5=conn.prepareStatement(query1);
							stmt5.setString(1, comp_cd);
							stmt5.setString(2, fin_year);
							stmt5.setString(3, inv_seq);
							stmt5.setString(4, cont_type);
							if(purchase_type_flag.equals("FF") || purchase_type_flag.equals("GTA_S")||purchase_type_flag.equals("GTA_P")||purchase_type_flag.equals("GTA_FF"))
							{
								stmt5.setString(5, invoice_type);
							}
							else
							{
								stmt5.setString(5, inv_flag);
							} 
							rset5=stmt5.executeQuery();
							while(rset5.next())
							{
								String tax_code=rset5.getString(1)==null?"":rset5.getString(1);
								String taxStrctCd=rset5.getString(2)==null?"":rset5.getString(2);
								String taxAmt=rset5.getString(3)==null?"":nf.format(rset5.getDouble(3));
								String taxBaseAmt=rset5.getString(4)==null?"":nf.format(rset5.getDouble(4));
								
								String mtn=utilDate.getMonthNameMON(period_st_dt);
								String tax_sht_nm=getTax_sht_nm(tax_code);
								tax_description=utilBean.getCounterpartyABBR(conn, counterparty_cd)+" "+state_cd+" "+alloc_qty+" MMBTU @ "+sale_price+" "+billing_note+" "+mtn+" "+tax_sht_nm;
								if(!exchng_rate.equals(""))
								{
									if(Double.doubleToRawLongBits(Double.parseDouble(exchng_rate))!=Double.doubleToRawLongBits(0))
									{
										tax_description+=" @"+exchng_rate;
									}
								}
								tax_description= tax_description.toUpperCase();
								index+=1;
								String trans_amt=taxAmt;
								if(invoice_raised_in.equals("2") && !exchng_rate.equals(""))
								{
									taxAmt=nf.format(Double.parseDouble(taxAmt) * Double.parseDouble(exchng_rate));
								}
								tax_acc_cd=getTaxStructureSunAccountCd(taxStructCd, tax_code, bu_state_tin, bu_unit,"S");
								
								String taxacc=tax_acc_cd;
								
								//20251014: IT WAS CONSIDERING PUR_ACCOUNT_CODE IN FMS8 
								//AS PER COMMUNICATING WITH MILIND AND VAISHNAVI referring remittance. 3251202052 
								//FROM NOW ONWARDS IT WILL FOLLOW FRONT-END INPUT.
								/*if(tax_sht_nm.contains("CST"))
								{
									taxacc=pur_account_cd;
								}*/
								
								if(invoice_type.equals("CR"))
								{
									acc_cd=trader_sun_acc_cd;
								}
								else
								{
									acc_cd=taxacc;
								}
								String dr_cr_marker=invoice_type.equals("DR")?"C":"D";
								VDIS_CONT_MAPPING.add(display_deal_map);
								//VINVOICE_NO.add(remittance_no);		//system generated  
								VINVOICE_NO.add(invoice_no);	  
								VJOURNAL_TYPE.add(journal_type);
								VAPPROVAL_DT.add(sun_approved_dt);
								VLEDGER.add(ledger);
								VPERIOD_START_DT.add(account_period);
								if(purchase_type_flag.equals("GTA_S")||purchase_type_flag.equals("GTA_P")||purchase_type_flag.equals("GTA_FF"))
								{
									VACCOUNT_CD.add(trans_acc_cd);
									VDESC.add(description);
									VINVOICE_DUE_DT.add(inv_due_dt);
									VCODE.add("NA");
								}
								else
								{
									VACCOUNT_CD.add(acc_cd);
									VDESC.add(tax_description);
									VINVOICE_DUE_DT.add("");
									VCODE.add("");
								}
								VBASE_AMT.add(taxAmt);
								//VDEBIT_CREDIT.add("D");
								VDEBIT_CREDIT.add(dr_cr_marker);
								VTRANS_AMT.add(trans_amt);
								VREPORT_AMT.add(nf.format(0));
								VCURRENCY_CD.add(currency_unit);
								VEXCHNG_RATE.add(exchng_rate);
								VINVOICE_DT.add(trans_dt);
								VCOST_CTR_CD.add(cccac);
								VEMPLOYEE_CD.add(ecac);
								VCOA_CD.add(pur_account_cd);
								VBU_UNIT_CD.add(bucac);
								VPROJECT_CD.add("");
								
								if(cont_type.equals("I")&&!igx_account_cd.equals(""))
								{
									index+=1;
									VDIS_CONT_MAPPING.add(display_deal_map);
									//VINVOICE_NO.add(remittance_no);		//system generated  
									VINVOICE_NO.add(invoice_no);		//system generated  
									VJOURNAL_TYPE.add(journal_type);
									VAPPROVAL_DT.add(sun_approved_dt);
									VLEDGER.add(ledger);
									VPERIOD_START_DT.add(account_period);
									VACCOUNT_CD.add(igx_account_cd);
									VDESC.add(tax_description);
									VINVOICE_DUE_DT.add("");
									VCODE.add("");
									VBASE_AMT.add(taxAmt);
									VDEBIT_CREDIT.add("C");
									VTRANS_AMT.add(trans_amt);
									VREPORT_AMT.add(nf.format(0));
									VCURRENCY_CD.add(currency_unit);
									VEXCHNG_RATE.add(exchng_rate);
									VINVOICE_DT.add(trans_dt);
									VCOST_CTR_CD.add(cccac);
									VEMPLOYEE_CD.add(ecac);
									VCOA_CD.add(pur_account_cd);
									VBU_UNIT_CD.add(bucac);
									VPROJECT_CD.add("");
								}
							}
							rset5.close();
							stmt5.close();
							
							//credit line
							if(invoice_type.equals("CR"))
							{
								acc_cd=pur_account_cd;
							}
							else
							{
								acc_cd=trader_sun_acc_cd;
							}
							String dr_cr_marker=invoice_type.equals("DR")?"D":"C";
							String trans_amt=nf.format(Double.parseDouble(tax_amt)*-1);
							if(invoice_raised_in.equals("2") && !exchng_rate.equals(""))
							{
								tax_amt=nf.format(Double.parseDouble(tax_amt)*Double.parseDouble(exchng_rate));
							}
							tax_amt=nf.format(Double.parseDouble(tax_amt)*(-1));
							if(purchase_type_flag.equals("GTA_S")||purchase_type_flag.equals("GTA_P")||purchase_type_flag.equals("GTA_FF"))
							{
								trans_amt=nf.format(Double.parseDouble(trans_amt)*-1);
								tax_amt=nf.format(Double.parseDouble(tax_amt)*(-1));
							}
							
							index+=1;
							VDIS_CONT_MAPPING.add(display_deal_map);
							//VINVOICE_NO.add(remittance_no);		//system generated  
							VINVOICE_NO.add(invoice_no);		//system generated  
							VJOURNAL_TYPE.add(journal_type);
							VAPPROVAL_DT.add(sun_approved_dt);
							VLEDGER.add(ledger);
							VPERIOD_START_DT.add(account_period);
							VBASE_AMT.add(tax_amt);
							//VDEBIT_CREDIT.add("C");
							VDEBIT_CREDIT.add(dr_cr_marker);
							VTRANS_AMT.add(trans_amt);
							VREPORT_AMT.add(nf.format(0));
							VCURRENCY_CD.add(currency_unit);
							VEXCHNG_RATE.add(exchng_rate);
							VINVOICE_DT.add(trans_dt);
							if(purchase_type_flag.equals("GTA_S")||purchase_type_flag.equals("GTA_P")||purchase_type_flag.equals("GTA_FF"))
							{
								VACCOUNT_CD.add(trader_sun_acc_cd);
								VDESC.add(description);
								VEMPLOYEE_CD.add(ecac);
							}
							else
							{
								VACCOUNT_CD.add(acc_cd);
								VDESC.add(tax_description);
								VEMPLOYEE_CD.add("");
							}
							VCOST_CTR_CD.add(cccac);
							VCOA_CD.add(pur_account_cd);
							VBU_UNIT_CD.add(bucac);
							VINVOICE_DUE_DT.add(inv_due_dt);
							VCODE.add("NA");
							VPROJECT_CD.add(analysis_cd5);
							
							if(cont_type.equals("I")&&!igx_account_cd.equals(""))
							{
								index+=1;
								VDIS_CONT_MAPPING.add(display_deal_map);
								//VINVOICE_NO.add(remittance_no);		//system generated  
								VINVOICE_NO.add(invoice_no);		  
								VJOURNAL_TYPE.add(journal_type);
								VAPPROVAL_DT.add(sun_approved_dt);
								VLEDGER.add(ledger);
								VPERIOD_START_DT.add(account_period);
								VACCOUNT_CD.add(acc_cd);
								VDESC.add(tax_description);
								VINVOICE_DUE_DT.add(inv_due_dt);
								VCODE.add("NA");
								VBASE_AMT.add(tax_amt);
								VDEBIT_CREDIT.add("D");
								VTRANS_AMT.add(trans_amt);
								VREPORT_AMT.add(nf.format(0));
								VCURRENCY_CD.add(currency_unit);
								VEXCHNG_RATE.add(exchng_rate);
								VINVOICE_DT.add(trans_dt);
								VCOST_CTR_CD.add(cccac);
								VEMPLOYEE_CD.add(ecac);
								VCOA_CD.add(pur_account_cd);
								VBU_UNIT_CD.add(bucac);
								VPROJECT_CD.add(analysis_cd5);
							}
							
							//for tcs and tds
							if(!tcs_tds.equals(""))
							{
								if(tcs_tds.equals("TDS"))
								{
									if(!tds_amt.equals("") && Double.doubleToRawLongBits(Double.parseDouble(tds_amt))>Double.doubleToRawLongBits(0))
									{
										String mtn=utilDate.getMonthNameMON(period_st_dt);
										tcs_tds_description=utilBean.getCounterpartyABBR(conn, counterparty_cd)+" "+state_cd+" "+alloc_qty+" MMBTU @ "+sale_price+" "+billing_note+" "+mtn+" TDS";
										if(!exchng_rate.equals(""))
										{
											if(Double.doubleToRawLongBits(Double.parseDouble(exchng_rate))!=Double.doubleToRawLongBits(0))
											{
												tcs_tds_description+=" @"+exchng_rate;
											}
										}
										tcs_tds_description=tcs_tds_description.toUpperCase();
										
										for(int j=0;j<2;j++)
										{
											index+=1;
											String tds_acc_cd=getTcsTdsStructureSunAccountCd(tdsStructCd, bu_state_tin, bu_unit);
											String tdsAmt=tds_amt;
											String report_amt1=nf.format(0);
											String tds_amt_inr=tds_amt;
											if(invoice_raised_in.equals("2") && !exchng_rate.equals(""))
											{
												tds_amt_inr=nf.format(Double.parseDouble(tds_amt) * Double.parseDouble(exchng_rate));
											}
											else
											{
												if(!exchng_rate.equals(""))
												{
													if(Double.doubleToRawLongBits(Double.parseDouble(exchng_rate))!=Double.doubleToRawLongBits(0))
													{
														report_amt1=nf.format(Double.parseDouble(tdsAmt)/Double.parseDouble(exchng_rate));
													}
												}
											}
											VDIS_CONT_MAPPING.add(display_deal_map);
											//VINVOICE_NO.add(remittance_no);		//system generated  
											VINVOICE_NO.add(invoice_no);		  
											VJOURNAL_TYPE.add(journal_type);
											VAPPROVAL_DT.add(sun_approved_dt);
											VLEDGER.add(ledger);
											VPERIOD_START_DT.add(account_period);
											if(j==0)
											{
												if(invoice_type.equals("CR"))
												{
													acc_cd=tds_acc_cd;
												}
												else
												{
													acc_cd=trader_sun_acc_cd;
												}
												dr_cr_marker=invoice_type.equals("DR")?"C":"D";
												VACCOUNT_CD.add(acc_cd);
												//VDEBIT_CREDIT.add("D");
												VDEBIT_CREDIT.add(dr_cr_marker);
												VEMPLOYEE_CD.add(ecac);
												if(purchase_type_flag.equals("GTA_S")||purchase_type_flag.equals("GTA_P")||purchase_type_flag.equals("GTA_FF"))
												{
													VINVOICE_DUE_DT.add(inv_due_dt);
												}
												else
												{
													VINVOICE_DUE_DT.add("");
												}
												VPROJECT_CD.add(analysis_cd5);
											}
											else
											{
												if(!purchase_type_flag.equals("GTA_S")&&!purchase_type_flag.equals("GTA_P")&&!purchase_type_flag.equals("GTA_FF"))
												{
													tdsAmt=nf.format(Double.parseDouble(tdsAmt)*-1);
													tds_amt=nf.format(Double.parseDouble(tds_amt)*-1);
													tds_amt_inr=nf.format(Double.parseDouble(tds_amt_inr)*-1);										
													report_amt1=Double.doubleToRawLongBits(Double.parseDouble(report_amt1))==Double.doubleToRawLongBits(0)?nf.format(0):nf.format(Double.parseDouble(report_amt1)*(-1));							
													VEMPLOYEE_CD.add("");
												}
												else
												{
													VEMPLOYEE_CD.add(ecac);
												}
												if(invoice_type.equals("CR"))
												{
													acc_cd=trader_sun_acc_cd;
												}
												else
												{
													acc_cd=tds_acc_cd;
												}
												dr_cr_marker=invoice_type.equals("DR")?"D":"C";
												VACCOUNT_CD.add(acc_cd);
												//VDEBIT_CREDIT.add("C");
												VDEBIT_CREDIT.add(dr_cr_marker);
												VINVOICE_DUE_DT.add(inv_due_dt);
												VPROJECT_CD.add("");
											}
											VBASE_AMT.add(tds_amt_inr);
											VTRANS_AMT.add(tdsAmt);
											//VREPORT_AMT.add(nf.format(0));
											VREPORT_AMT.add(report_amt1);
											VCURRENCY_CD.add(currency_unit);
											VEXCHNG_RATE.add(exchng_rate);
											VINVOICE_DT.add(trans_dt);
											if(purchase_type_flag.equals("GTA_S")||purchase_type_flag.equals("GTA_P")||purchase_type_flag.equals("GTA_FF"))
											{
												VDESC.add(description);
											}
											else
											{
												VDESC.add(tcs_tds_description);
											}
											VCOST_CTR_CD.add(cccac);
											VCOA_CD.add(pur_account_cd);
											VBU_UNIT_CD.add(bucac);
											VCODE.add(code);
										}
									}
								}
								else if(tcs_tds.equals("TCS"))
								{
									String mtn=utilDate.getMonthNameMON(period_st_dt);
									tcs_tds_description=utilBean.getCounterpartyABBR(conn, counterparty_cd)+" "+state_cd+" "+alloc_qty+" MMBTU @ "+sale_price+" "+billing_note+" "+mtn+" TCS";
									if(!exchng_rate.equals(""))
									{
										if(Double.doubleToRawLongBits(Double.parseDouble(exchng_rate))!=Double.doubleToRawLongBits(0))
										{
											tcs_tds_description+=" @"+exchng_rate;
										}
									}
									tcs_tds_description=tcs_tds_description.toUpperCase();
									for(int j=0;j<2;j++)
									{
										index+=1;
										String tcs_acc_cd=getTcsTdsStructureSunAccountCd(tcsStructCd, bu_state_tin, bu_unit);
										String tcsAmt=tcs_amt;
										String report_amt1=nf.format(0);
										String tcs_amt_inr=tcs_amt;
										if(invoice_raised_in.equals("2") && !exchng_rate.equals(""))
										{
											tcs_amt_inr=nf.format(Double.parseDouble(tcs_amt) * Double.parseDouble(exchng_rate));
										}
										else
										{
											if(!exchng_rate.equals(""))
											{
												if(Double.doubleToRawLongBits(Double.parseDouble(exchng_rate))!=Double.doubleToRawLongBits(0))
												{
													report_amt1=nf.format(Double.parseDouble(tcsAmt)/Double.parseDouble(exchng_rate));
												}
											}
										}
										VDIS_CONT_MAPPING.add(display_deal_map);
										//VINVOICE_NO.add(remittance_no);		//system generated  
										VINVOICE_NO.add(invoice_no);		
										VJOURNAL_TYPE.add(journal_type);
										VAPPROVAL_DT.add(sun_approved_dt);
										VLEDGER.add(ledger);
										VPERIOD_START_DT.add(account_period);
										if(j==0)
										{
											/*tcsAmt=nf.format(Double.parseDouble(tcsAmt)*-1);
										tcs_amt=nf.format(Double.parseDouble(tcs_amt)*-1);
										tcs_amt_inr=nf.format(Double.parseDouble(tcs_amt_inr)*-1);
										report_amt1=Double.parseDouble(report_amt1)==0?nf.format(0):nf.format(Double.parseDouble(report_amt1)*(-1));*/
											
											VACCOUNT_CD.add(trader_sun_acc_cd);
											VDEBIT_CREDIT.add("C");
											VINVOICE_DUE_DT.add("");
											VEMPLOYEE_CD.add(ecac);
										}
										else
										{
											tcsAmt=nf.format(Double.parseDouble(tcsAmt)*-1);
											tcs_amt=nf.format(Double.parseDouble(tcs_amt)*-1);
											tcs_amt_inr=nf.format(Double.parseDouble(tcs_amt_inr)*-1);
											report_amt1=Double.doubleToRawLongBits(Double.parseDouble(report_amt1))==Double.doubleToRawLongBits(0)?nf.format(0):nf.format(Double.parseDouble(report_amt1)*(-1));
											
											VACCOUNT_CD.add(tcs_acc_cd);
											VDEBIT_CREDIT.add("D");
											VINVOICE_DUE_DT.add(inv_due_dt);
											VEMPLOYEE_CD.add("");
										}
										VBASE_AMT.add(tcs_amt_inr);
										VTRANS_AMT.add(tcsAmt);
										VREPORT_AMT.add(report_amt1);
										VCURRENCY_CD.add(currency_unit);
										VEXCHNG_RATE.add(exchng_rate);
										VINVOICE_DT.add(trans_dt);
										VDESC.add(tcs_tds_description);
										VCOST_CTR_CD.add(cccac);
										VCOA_CD.add(pur_account_cd);
										VBU_UNIT_CD.add(bucac);
										VPROJECT_CD.add("");
										VCODE.add(code);
										
										if(cont_type.equals("I")&&!igx_account_cd.equals(""))
										{
											index+=1;
											VDIS_CONT_MAPPING.add(display_deal_map);
											//VINVOICE_NO.add(remittance_no);		//system generated  
											VINVOICE_NO.add(invoice_no);		//system generated  
											VJOURNAL_TYPE.add(journal_type);
											VAPPROVAL_DT.add(sun_approved_dt);
											VLEDGER.add(ledger);
											VPERIOD_START_DT.add(account_period);
											if(j==0)
											{
												VACCOUNT_CD.add(igx_account_cd);
												VDEBIT_CREDIT.add("C");
												VINVOICE_DUE_DT.add("");
											}
											else
											{
												VACCOUNT_CD.add(trader_sun_acc_cd);
												VDEBIT_CREDIT.add("D");
												VINVOICE_DUE_DT.add(inv_due_dt);
											}
											VDESC.add(tcs_tds_description);
											VCODE.add(code);
											VBASE_AMT.add(tcs_amt_inr);
											VTRANS_AMT.add(tcsAmt);
											VREPORT_AMT.add(report_amt1);
											VCURRENCY_CD.add(currency_unit);
											VEXCHNG_RATE.add(exchng_rate);
											VINVOICE_DT.add(trans_dt);
											VCOST_CTR_CD.add(cccac);
											VEMPLOYEE_CD.add(ecac);
											VCOA_CD.add(pur_account_cd);
											VBU_UNIT_CD.add(bucac);
											VPROJECT_CD.add("");
										}
									}
								}
							}
						}
						
						//for custom duty 
						if(segment.equals("CF"))
						{
							if(Double.doubleToRawLongBits(Double.parseDouble(net_payable))>=Double.doubleToRawLongBits(0))
							{
								//j=0: final_custom_amt
								//j=1: interest amount
								//j=2: base amount credit line 
								//j=3: Provisional amount Debit			//20250728: AS PER ARUN INSTRUCTION OF ADDING ADDITIONAL LINE FOR PROVISIONAL AMOUNT
								//j=4: Provisional amount Credit		//20250728: AS PER ARUN INSTRUCTION OF ADDING ADDITIONAL LINE FOR PROVISIONAL AMOUNT
								for(int j=0;j<5;j++)
								{
									index+=1;
									VDIS_CONT_MAPPING.add(display_deal_map);
									//VINVOICE_NO.add(remittance_no);		//system generated  
									VINVOICE_NO.add(invoice_no);		//system generated  
									VJOURNAL_TYPE.add(journal_type);
									VAPPROVAL_DT.add(sun_approved_dt);
									VLEDGER.add(ledger);
									VPERIOD_START_DT.add(account_period);
									if(j==0)
									{
										VACCOUNT_CD.add(custom_account_cd);
										VBASE_AMT.add(final_custom_amt);
										VDEBIT_CREDIT.add("D");
										VTRANS_AMT.add(final_custom_amt);
										VCOA_CD.add(custom_duty_tax_cd);
										VEMPLOYEE_CD.add(ecac);
									}
									else if(j==1)
									{
										VACCOUNT_CD.add(miscellaneous_ac_code);
										VBASE_AMT.add(nf.format(intrest_amt));
										VDEBIT_CREDIT.add("D");
										VTRANS_AMT.add(nf.format(intrest_amt));
										VCOA_CD.add(custom_duty_tax_cd);
										VEMPLOYEE_CD.add(ecac);
									}
									else if(j==2)
									{
										VACCOUNT_CD.add(custom_duty_tax_cd);
										VBASE_AMT.add(base_amt);
										VDEBIT_CREDIT.add("C");
										VTRANS_AMT.add(base_amt);
										VCOA_CD.add(custom_account_cd);
										VEMPLOYEE_CD.add("");
									}
									else if(j==3)
									{
										VACCOUNT_CD.add(custom_account_cd);
										VBASE_AMT.add(cd_paid_amt);
										VDEBIT_CREDIT.add("D");
										VTRANS_AMT.add(cd_paid_amt);
										VCOA_CD.add(custom_duty_tax_cd);
										VEMPLOYEE_CD.add(ecac);
									}
									else if(j==4)
									{
										VACCOUNT_CD.add(custom_duty_tax_cd);
										VBASE_AMT.add(cd_paid_amt);
										VDEBIT_CREDIT.add("C");
										VTRANS_AMT.add(cd_paid_amt);
										VCOA_CD.add(custom_account_cd);
										VEMPLOYEE_CD.add("");
									}
									VREPORT_AMT.add(nf.format(0));
									VCURRENCY_CD.add(currency_unit);
									VEXCHNG_RATE.add(exchng_rate);
									VINVOICE_DT.add(trans_dt);
									VDESC.add(description);
									VINVOICE_DUE_DT.add("");
									VCOST_CTR_CD.add(cccac);
									VBU_UNIT_CD.add(bucac);
									VCODE.add("");
									VPROJECT_CD.add("");
								}
							}
						}
					}
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
	
	//for generating the purchase sun xml
	public void generatePurSunXML()
	{
		String function_nm="generatePurSunXML()";
		try
		{
			DocumentBuilderFactory docFactory = xmlUtil.dcoumentBuilderFactory();
		    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		    Document doc = docBuilder.newDocument();
			//root fmsng
		    Element fmsng = doc.createElement("SSC");
		    doc.appendChild(fmsng);
		    
		    //root elements
		    Element Payload = doc.createElement("Payload");
		    fmsng.appendChild(Payload);
		    Element Ledger = doc.createElement("Ledger");
		    //fmsng.appendChild(Ledger);
		    Payload.appendChild(Ledger);
		    
		    String xmlFileNm="";
		    String journal_type="FMSPI";
		    boolean xml_generate_flag=true;
		    
		    //String sysdate=utilDate.getSysdate();
		    String sysdate=utilDate.getSysdateWithTime24hrWithSS();		//PB 20251003: FOR HANDLING THE PARALLEL SUBMIT ISSUE
		    sysdate=sysdate.replace("/", "_");
		    sysdate=sysdate.replace(" ", "_");
		    sysdate=sysdate.replace(":", "_");
			String invFlag="F";
			String segmentType="";
			if(segment.equals("P"))
			{
				invFlag="A.INV_FLAG IN ('P') ";
				segmentType="A.CONTRACT_TYPE IN ('N') ";
				journal_type="FMSPR";
			}
			else if(segment.equals("CP"))
			{
				invFlag="A.INV_FLAG IN ('CP') ";
				segmentType="A.CONTRACT_TYPE IN ('N') ";
				journal_type="FMSPR";
			}
			else if(segment.equals("F"))
			{
				invFlag="INV_FLAG IN ('F') ";
				segmentType="A.CONTRACT_TYPE IN ('N','D','T','I') ";
				journal_type="FMSPR";
			}
			else if(segment.equals("CF"))
			{
				invFlag="INV_FLAG IN ('CF') ";
				segmentType="A.CONTRACT_TYPE IN ('N') ";
				journal_type="FMSPR";
			}
			int cnt=0;
			String queryString="SELECT A.COUNTERPARTY_CD,A.CONT_NO,A.CONTRACT_TYPE,A.BU_UNIT,A.INVOICE_SEQ,A.INVOICE_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'), "
					+ "A.FREQ,A.ALLOC_QTY,A.SALE_PRICE,A.SALE_PRICE_UNIT,A.SALE_AMT,"
					+ "(NVL(A.GROSS_AMT,0) + NVL(A.TRANSPORTATION_AMOUNT,0) + NVL(A.MARKET_MARGIN_AMT,0) + NVL(A.OTHER_CHARGES_AMT,0)),"
					+ "A.TAX_AMT,A.INVOICE_AMT,A.ADJUST_SIGN,A.ADJUST_AMT,A.NET_PAYABLE_AMT,"
					+ "A.EXCHG_RATE_VALUE,TO_CHAR(A.EXCHG_RATE_DT,'DD/MM/YYYY'),A.INVOICE_RAISED_IN,TO_CHAR(A.INVOICE_DT, 'Month'),"
					+ "A.CHECKED_FLAG,A.AUTHORIZED_FLAG,A.APPROVED_FLAG,A.TCS_CERT_FLAG,A.TCS_AMT,A.TCS_FACTOR,A.AGMT_NO,A.FINANCIAL_YEAR,A.SAP_EXCHNG_RATE,A.SAP_APPROVAL,"
					+ "A.TDS_AMT,A.TDS_FACTOR,A.TDS_STRUCT_CD,TO_CHAR(A.TDS_EFF_DT,'DD/MM/YYYY'),A.TCS_TDS,A.INV_FLAG,'S','SG',A.CARGO_NO,A.BOE_NO,A.SYS_INV_NO,A.QTY_UNIT,"
					+ "A.TRANSPORTATION_AMOUNT,A.MARKET_MARGIN_AMT,A.OTHER_CHARGES_AMT,A.PLANT_SEQ,A.SAP_APPROVED_BY,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),A.AGMT_REV,A.CONT_REV,A.FINANCIAL_YEAR,A.TAX_STRUCT_CD, "
					+ "NVL(A.NET_PAYABLE_AMT,0)-NVL(A.INVOICE_AMT,0),A.TCS_STRUCT_CD,NULL,NULL,NULL,CD_PAID_AMT,NULL,TO_CHAR(APPROVED_DT,'DD/MM/YYYY')   "
					+ "FROM FMS_PUR_SG_INV_MST A "
					+ "WHERE A.COMPANY_CD=? "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND "+segmentType+" AND A.PDF_INV_DTL IS NOT NULL AND "+invFlag+" "
					+ "AND A.SAP_APPROVAL=? AND A.FIN_SYS=? "
					+ "AND NOT EXISTS (SELECT INV_TITLE FROM FMS_PUR_INV_FILE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.INVOICE_SEQ=B.INVOICE_SEQ "
					+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.INV_FLAG=B.INV_FLAG AND B.INV_TITLE IN (?,?) )";
			queryString+=" UNION ALL ";
			queryString+="SELECT A.COUNTERPARTY_CD,A.CONT_NO,A.CONTRACT_TYPE,A.BU_UNIT,A.INVOICE_SEQ,A.INVOICE_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'), "
					+ "A.FREQ,A.ALLOC_QTY,A.SALE_PRICE,A.SALE_PRICE_UNIT,A.SALE_AMT,"
					+ "(NVL(A.GROSS_AMT,0) + NVL(A.TRANSPORTATION_AMOUNT,0) + NVL(A.MARKET_MARGIN_AMT,0) + NVL(A.OTHER_CHARGES_AMT,0)),"
					+ "A.TAX_AMT,A.INVOICE_AMT,A.ADJUST_SIGN,A.ADJUST_AMT,A.NET_PAYABLE_AMT,"
					+ "A.EXCHG_RATE_VALUE,TO_CHAR(A.EXCHG_RATE_DT,'DD/MM/YYYY'),A.INVOICE_RAISED_IN,TO_CHAR(A.INVOICE_DT, 'Month'),"
					+ "A.CHECKED_FLAG,A.AUTHORIZED_FLAG,A.APPROVED_FLAG,A.TCS_CERT_FLAG,A.TCS_AMT,A.TCS_FACTOR,A.AGMT_NO,A.FINANCIAL_YEAR,A.SAP_EXCHNG_RATE,A.SAP_APPROVAL,"
					+ "A.TDS_AMT,A.TDS_FACTOR,A.TDS_STRUCT_CD,TO_CHAR(A.TDS_EFF_DT,'DD/MM/YYYY'),A.TCS_TDS,A.INV_FLAG,'P','PG',A.CARGO_NO,A.BOE_NO,A.SYS_INV_NO,A.QTY_UNIT,"
					+ "A.TRANSPORTATION_AMOUNT,A.MARKET_MARGIN_AMT,A.OTHER_CHARGES_AMT,A.PLANT_SEQ,A.SAP_APPROVED_BY,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),A.AGMT_REV,A.CONT_REV,A.FINANCIAL_YEAR,A.TAX_STRUCT_CD, "
					+ "NVL(A.NET_PAYABLE_AMT,0)-NVL(A.INVOICE_AMT,0),A.TCS_STRUCT_CD,NULL,NULL,NULL,CD_PAID_AMT,NULL,TO_CHAR(APPROVED_DT,'DD/MM/YYYY')   "
					+ "FROM FMS_PUR_PG_INV_MST A "
					+ "WHERE A.COMPANY_CD=? "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND "+segmentType+" AND A.PDF_INV_DTL IS NOT NULL AND "+invFlag+" "
					+ "AND SAP_APPROVAL=? AND FIN_SYS=? "
					+ "AND NOT EXISTS (SELECT INV_TITLE FROM FMS_PUR_INV_FILE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.INVOICE_SEQ=B.INVOICE_SEQ "
					+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.INV_FLAG=B.INV_FLAG AND B.INV_TITLE IN (?,?) )";
			if(segment.equals("F")||segment.equals("CF"))
			{
				if(segment.equals("CF"))
				{
					segmentType="A.CONTRACT_TYPE IN ('N') ";
					invFlag="INV_HEAD IN ('CD') ";
				}
				else
				{
					segmentType="A.CONTRACT_TYPE IN ('N','D','T','I') ";
					invFlag="(INV_HEAD IN ('NG','IMB') OR INVOICE_TYPE IN ('CR','DR') )";
				}
				queryString+=" UNION ALL ";
				queryString+="SELECT COUNTERPARTY_CD,CONT_NO,CONTRACT_TYPE,BU_UNIT,INVOICE_SEQ,INVOICE_REF,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),  "
						+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),  "
						+ "FREQ,ALLOC_QTY,NULL,NULL,NULL,  "
						+ "GROSS_AMT_INR,  "
						+ "TAX_AMT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,   "
						+ "EXCHG_RATE_VALUE,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),INVOICE_RAISED_IN,TO_CHAR(INVOICE_DT, 'Month'),  "
						+ "CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,TCS_CERT_FLAG,TCS_AMT,NULL,AGMT_NO,FINANCIAL_YEAR,SAP_EXCHNG_RATE,SAP_APPROVAL,   "
						+ "TDS_AMT,NULL,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),TCS_TDS,NULL,'FF','FFLOW',CARGO_NO,NULL,INVOICE_NO,QTY_UNIT,  "
						+ "NULL,NULL,NULL,NULL,SAP_APPROVED_BY,TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),A.AGMT_REV,A.CONT_REV,A.FINANCIAL_YEAR,A.TAX_STRUCT_CD,  "
						+ "NVL(A.NET_PAYABLE_AMT,0)-NVL(A.INVOICE_AMT,0),A.TCS_STRUCT_CD,A.GROSS_AMT_USD,A.INVOICE_CATEGORY,A.INVOICE_TYPE,NULL,NULL,TO_CHAR(APPROVED_DT,'DD/MM/YYYY') "
						+ "FROM FMS_PUR_FFLOW_INV_MST A "
						+ "WHERE COMPANY_CD=? "
						+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND "+segmentType+" AND PDF_INV_DTL IS NOT NULL AND "+invFlag+" "
						+ "AND SAP_APPROVAL=? AND FIN_SYS=? "
						+ "AND NOT EXISTS (SELECT INV_TITLE FROM FMS_PUR_FFLOW_INV_FILE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.INVOICE_SEQ=B.INVOICE_SEQ  "
						+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.INVOICE_TYPE=B.INVOICE_TYPE AND B.INV_TITLE IN (?,?) )";
				if(segment.equals("F"))
				{
					segmentType="A.CONTRACT_TYPE IN ('R','C','K') ";
					queryString+="UNION ALL ";
					queryString+="SELECT A.COUNTERPARTY_CD,A.CONT_NO,A.CONTRACT_TYPE,A.BU_UNIT,A.INVOICE_SEQ,A.INVOICE_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'), "
							+ "TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),  "
							+ "A.FREQ,A.ALLOC_QTY,NULL,NULL,A.SALE_AMT, "
							+ "A.GROSS_AMT, "
							+ "A.TAX_AMT,A.INVOICE_AMT,A.ADJUST_SIGN,A.ADJUST_AMT,A.NET_PAYABLE_AMT, "
							+ "A.EXCHG_RATE_VALUE,TO_CHAR(A.EXCHG_RATE_DT,'DD/MM/YYYY'),A.INVOICE_RAISED_IN,TO_CHAR(A.INVOICE_DT, 'Month'), "
							+ "A.CHECKED_FLAG,A.AUTHORIZED_FLAG,A.APPROVED_FLAG,NULL,NULL,NULL,A.AGMT_NO,A.FINANCIAL_YEAR,NULL,A.SAP_APPROVAL, "
							+ "A.TDS_AMT,A.TDS_FACTOR,A.TDS_STRUCT_CD,TO_CHAR(A.TDS_EFF_DT,'DD/MM/YYYY'),A.TCS_TDS,NULL,'GTA_S','GTA_SG',NULL,NULL,A.SYS_INV_NO,NULL, "
							+ "NULL,NULL,NULL,NULL,A.SAP_APPROVED_BY,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),A.AGMT_REV,A.CONT_REV,A.FINANCIAL_YEAR,A.TAX_STRUCT_CD,  "
							+ "NVL(A.NET_PAYABLE_AMT,0)-NVL(A.INVOICE_AMT,0),NULL,NULL,NULL,INVOICE_TYPE,NULL,NULL,TO_CHAR(APPROVED_DT,'DD/MM/YYYY') "
							+ "FROM FMS_GTA_SG_INV_MST A  "
							+ "WHERE COMPANY_CD=? "
							+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND " + segmentType + " AND PDF_INV_DTL IS NOT NULL "
							+ "AND SAP_APPROVAL=? AND FIN_SYS=? "
							+ "AND NOT EXISTS (SELECT INV_TITLE FROM FMS_GTA_INV_FILE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.INVOICE_SEQ=B.INVOICE_SEQ "
							+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.INVOICE_TYPE=B.INVOICE_TYPE AND B.INV_TITLE IN (?,?) )";
					queryString+="UNION ALL ";
					queryString+="SELECT A.COUNTERPARTY_CD,A.CONT_NO,A.CONTRACT_TYPE,A.BU_UNIT,A.INVOICE_SEQ,A.INVOICE_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'), "
							+ "TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),  "
							+ "A.FREQ,A.ALLOC_QTY,NULL,NULL,A.SALE_AMT, "
							+ "A.GROSS_AMT, "
							+ "A.TAX_AMT,A.INVOICE_AMT,A.ADJUST_SIGN,A.ADJUST_AMT,A.NET_PAYABLE_AMT, "
							+ "A.EXCHG_RATE_VALUE,TO_CHAR(A.EXCHG_RATE_DT,'DD/MM/YYYY'),A.INVOICE_RAISED_IN,TO_CHAR(A.INVOICE_DT, 'Month'), "
							+ "A.CHECKED_FLAG,A.AUTHORIZED_FLAG,A.APPROVED_FLAG,NULL,NULL,NULL,A.AGMT_NO,A.FINANCIAL_YEAR,NULL,A.SAP_APPROVAL, "
							+ "A.TDS_AMT,A.TDS_FACTOR,A.TDS_STRUCT_CD,TO_CHAR(A.TDS_EFF_DT,'DD/MM/YYYY'),A.TCS_TDS,NULL,'GTA_P','GTA_PG',NULL,NULL,A.SYS_INV_NO,NULL, "
							+ "NULL,NULL,NULL,NULL,A.SAP_APPROVED_BY,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),A.AGMT_REV,A.CONT_REV,A.FINANCIAL_YEAR,A.TAX_STRUCT_CD,  "
							+ "NVL(A.NET_PAYABLE_AMT,0)-NVL(A.INVOICE_AMT,0),NULL,NULL,NULL,INVOICE_TYPE,NULL,NULL,TO_CHAR(APPROVED_DT,'DD/MM/YYYY') "
							+ "FROM FMS_GTA_PG_INV_MST A "
							+ "WHERE COMPANY_CD=? "
							+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND " + segmentType + " AND PDF_INV_DTL IS NOT NULL "
							+ "AND SAP_APPROVAL=? AND FIN_SYS=? "
							+ "AND NOT EXISTS (SELECT INV_TITLE FROM FMS_GTA_INV_FILE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.INVOICE_SEQ=B.INVOICE_SEQ "
							+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.INVOICE_TYPE=B.INVOICE_TYPE AND B.INV_TITLE IN (?,?) )";
					queryString+="UNION ALL ";
					queryString+="SELECT COUNTERPARTY_CD,CONT_NO,CONTRACT_TYPE,BU_UNIT,INVOICE_SEQ,INVOICE_REF,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),   "
							+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),   "
							+ "FREQ,ALLOC_QTY,NULL,NULL,NULL,   "
							+ "GROSS_AMT_INR,   "
							+ "TAX_AMT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,    "
							+ "EXCHG_RATE_VALUE,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),INVOICE_RAISED_IN,TO_CHAR(INVOICE_DT, 'Month'),   "
							+ "CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,TCS_CERT_FLAG,TCS_AMT,NULL,AGMT_NO,FINANCIAL_YEAR,SAP_EXCHNG_RATE,SAP_APPROVAL,    "
							+ "TDS_AMT,NULL,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),TCS_TDS,NULL,'GTA_FF','GTA_FFLOW',NULL,NULL,INVOICE_NO,NULL,   "
							+ "NULL,NULL,NULL,NULL,SAP_APPROVED_BY,TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),A.AGMT_REV,A.CONT_REV,A.FINANCIAL_YEAR,A.TAX_STRUCT_CD,   "
							+ "NVL(A.NET_PAYABLE_AMT,0)-NVL(A.INVOICE_AMT,0),A.TCS_STRUCT_CD,A.GROSS_AMT_USD,A.INVOICE_CATEGORY,A.INVOICE_TYPE,NULL,NULL,TO_CHAR(APPROVED_DT,'DD/MM/YYYY') "
							+ "FROM FMS_GTA_FFLOW_INV_MST A "
							+ "WHERE COMPANY_CD=? "
							+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND " + segmentType + " AND PDF_INV_DTL IS NOT NULL "
							+ "AND SAP_APPROVAL=? AND FIN_SYS=? "
							+ "AND NOT EXISTS (SELECT INV_TITLE FROM FMS_GTA_FFLOW_INV_FILE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.INVOICE_SEQ=B.INVOICE_SEQ "
							+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.INVOICE_TYPE=B.INVOICE_TYPE AND B.INV_TITLE IN (?,?) )";
					
					//for derivative remittance
					segmentType="AND A.INV_TYPE='R' ";
					queryString+="UNION ALL ";
					queryString+="SELECT A.COUNTERPARTY_CD,A.CONT_NO,A.CONTRACT_TYPE,A.BU_UNIT,A.INVOICE_SEQ,A.INVOICE_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'), "
							+ "TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),  "
							+ "A.FREQ,A.ALLOC_QTY,A.SALE_PRICE,A.SALE_PRICE_UNIT,A.SALE_AMT, "
							+ "A.INVOICE_AMT, "
							+ "NULL,A.INVOICE_AMT,NULL,NULL,A.NET_PAYABLE_AMT, "
							+ "NULL,NULL,A.INVOICE_RAISED_IN,TO_CHAR(A.INVOICE_DT, 'Month'), "
							+ "A.CHECKED_FLAG,A.AUTHORIZED_FLAG,A.APPROVED_FLAG,NULL,NULL,NULL,A.AGMT_NO,A.FINANCIAL_YEAR,NULL,A.SAP_APPROVAL, "
							+ "NULL,NULL,NULL,NULL,NULL,NULL,'DERV','DERV',A.INSTRUMENT_NO,NULL,A.INVOICE_REF_NO,NULL, "
							+ "NULL,NULL,NULL,A.PLANT_SEQ,A.SAP_APPROVED_BY,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),A.AGMT_REV,A.CONT_REV,A.FINANCIAL_YEAR,NULL,  "
							+ "NVL(A.NET_PAYABLE_AMT,0)-NVL(A.INVOICE_AMT,0),NULL,NULL,NULL,A.INV_TYPE,NULL,A.BU_STATE_TIN ,TO_CHAR(APPROVED_DT,'DD/MM/YYYY')   "
							+ "FROM FMS_DERV_INVOICE_MST A  "
							+ "WHERE COMPANY_CD=? "
							+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
							+ " "+segmentType+" AND A.PDF_INV_DTL IS NOT NULL "
							+ "AND SAP_APPROVAL=? AND FIN_SYS=? "
							+ "AND NOT EXISTS (SELECT PDF_TYPE FROM FMS_DERV_INV_FILE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN   "
							+ "AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.INV_TYPE=B.INV_TYPE AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND B.PDF_TYPE IN (?,?)) ";
				}
			}
			//queryString+="ORDER BY CONT_NO";
			String temp_queryString=queryString;
			stmt=conn.prepareStatement(temp_queryString);
			stmt.setString(++cnt, comp_cd);
			stmt.setString(++cnt, from_dt);
			stmt.setString(++cnt, to_dt);
			stmt.setString(++cnt, "Y");
			stmt.setString(++cnt, "S");
			stmt.setString(++cnt, "S");
			stmt.setString(++cnt, "X");
			stmt.setString(++cnt, comp_cd);
			stmt.setString(++cnt, from_dt);
			stmt.setString(++cnt, to_dt);
			stmt.setString(++cnt, "Y");
			stmt.setString(++cnt, "S");
			stmt.setString(++cnt, "S");
			stmt.setString(++cnt, "X");
			if(segment.equals("F")||segment.equals("CF"))
			{
				stmt.setString(++cnt, comp_cd);
				stmt.setString(++cnt, from_dt);
				stmt.setString(++cnt, to_dt);
				stmt.setString(++cnt, "Y");
				stmt.setString(++cnt, "S");
				stmt.setString(++cnt, "S");
				stmt.setString(++cnt, "X");
				if(segment.equals("F"))
				{
					stmt.setString(++cnt, comp_cd);
					stmt.setString(++cnt, from_dt);
					stmt.setString(++cnt, to_dt);
					stmt.setString(++cnt, "Y");
					stmt.setString(++cnt, "S");
					stmt.setString(++cnt, "S");
					stmt.setString(++cnt, "X");
					stmt.setString(++cnt, comp_cd);
					stmt.setString(++cnt, from_dt);
					stmt.setString(++cnt, to_dt);
					stmt.setString(++cnt, "Y");
					stmt.setString(++cnt, "S");
					stmt.setString(++cnt, "S");
					stmt.setString(++cnt, "X");
					stmt.setString(++cnt, comp_cd);
					stmt.setString(++cnt, from_dt);
					stmt.setString(++cnt, to_dt);
					stmt.setString(++cnt, "Y");
					stmt.setString(++cnt, "S");
					stmt.setString(++cnt, "S");
					stmt.setString(++cnt, "X");
					stmt.setString(++cnt, comp_cd);
					stmt.setString(++cnt, from_dt);
					stmt.setString(++cnt, to_dt);
					stmt.setString(++cnt, "Y");
					stmt.setString(++cnt, "S");
					stmt.setString(++cnt, "S");
					stmt.setString(++cnt, "X");
				}
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String counterparty_cd=rset.getString(1)==null?"":rset.getString(1);
				String cont_no=rset.getString(2)==null?"":rset.getString(2);
				String cont_type=rset.getString(3)==null?"":rset.getString(3);
				String bu_unit=rset.getString(4)==null?"":rset.getString(4);
				String invoice_seq=rset.getString(5)==null?"":rset.getString(5);
				String invoice_no=rset.getString(6)==null?"":rset.getString(6);
				String invoice_dt=rset.getString(7)==null?"":rset.getString(7);
				String inv_due_dt=rset.getString(8)==null?"":rset.getString(8);
				String period_st_dt=rset.getString(9)==null?"":rset.getString(9);
				String period_end_dt=rset.getString(10)==null?"":rset.getString(10);
				String billing_freq=rset.getString(11)==null?"":rset.getString(11);
				String alloc_qty=rset.getString(12)==null?"":nf.format(rset.getDouble(12));
				String sale_price=rset.getString(13)==null?"":nf.format(rset.getDouble(13));
				String gross_amt=rset.getString(16)==null?"":nf.format(rset.getDouble(16));
				String tax_amt=rset.getString(17)==null?"":nf.format(rset.getDouble(17));
				String inv_amt=rset.getString(18)==null?"":nf.format(rset.getDouble(18));
				String net_payable=rset.getString(21)==null?"":nf.format(rset.getDouble(21));
				String exchng_rate=rset.getString(22)==null?"":nf.format(rset.getDouble(22));
				String invoice_raised_in=rset.getString(24)==null?"":rset.getString(24);
				String tcs_amt=rset.getString(30)==null?"":nf.format(rset.getDouble(30));
				String agmt_no=rset.getString(32)==null?"0":rset.getString(32);
				String fin_year=rset.getString(33)==null?"":rset.getString(33);
				String tds_amt=rset.getString(36)==null?"":nf.format(rset.getDouble(36));
				String tds_factor=rset.getString(37)==null?"":rset.getString(37);
				String tdsStructCd=rset.getString(38)==null?"":rset.getString(38);
				String tcs_tds=rset.getString(40)==null?"":rset.getString(40);
				String inv_flag=rset.getString(41)==null?"":rset.getString(41);
				String purchase_type_flag=rset.getString(42)==null?"":rset.getString(42);
				String payment_type_nm=rset.getString(43)==null?"":rset.getString(43);
				String cargo_no=rset.getString(44)==null?"":rset.getString(44);
				String remittance_no = rset.getString(46)==null?"":rset.getString(46);
				String plant_seq_no=rset.getString(51)==null?"":rset.getString(51);
				String sun_approved_dt=rset.getString(53)==null?"":rset.getString(53);
				String agmt_rev=rset.getString(54)==null?"":rset.getString(54);
				String cont_rev=rset.getString(55)==null?"":rset.getString(55);
				String taxStructCd=rset.getString(57)==null?"":rset.getString(57);
				double intrest_amt=rset.getDouble(58);
				String tcsStructCd=rset.getString(59)==null?"":rset.getString(59);
				String gross_amt_usd=rset.getString(60)==null?"":rset.getString(60);
				String invoice_type=rset.getString(62)==null?"":rset.getString(62);
				String cd_paid_amt=rset.getString(63)==null?"":nf.format(rset.getDouble(63));
				String bu_state_tin=rset.getString(64)==null?"":rset.getString(64);
				String inv_approve_dt=rset.getString(65)==null?"":rset.getString(65);
				
				String ledger="A";
			    String cccac ="1TM01";;
			    String ecac ="LNA";
			    String cccaac ="142100";
			    String statutory_payment = "243991"; 
			    String bucac="SEI";	
			    String miscellaneous_ac_code = "839999";
			    String igx_account_cd="153995";
			    String trans_acc_cd="802031";
				
				if(purchase_type_flag.equals("DERV"))
				{
					//for finding the diff in price
					String diff_amt="";
					String currency_cd="USD";
					if(!gross_amt.equals("")&&!alloc_qty.equals(""))
					{
						diff_amt=nf.format(Double.parseDouble(gross_amt)/Double.parseDouble(alloc_qty));
						if(Double.parseDouble(diff_amt)<0)
						{
							diff_amt="("+diff_amt+")";
						}
					}
					String trans_dt=invoice_dt.split("/")[0]+invoice_dt.split("/")[1]+invoice_dt.split("/")[2];
					//inv_due_dt = inv_due_dt.split("/")[0]+inv_due_dt.split("/")[1]+inv_due_dt.split("/")[2];
					inv_due_dt = inv_due_dt.replace("/","");
					journal_type="FMSPR";
					String qty_unit=getQuantityUnit(counterparty_cd,agmt_no,"U",agmt_rev,cont_no,cont_type,cont_rev,cargo_no);		//here cargo_no represents instrument
					String description=(utilBean.getCounterpartyABBR(conn, counterparty_cd)+" "+alloc_qty+" "+qty_unit+" @ "+diff_amt+" MTM").toUpperCase();
					String account_cd=getCounterpartySunAccountCode(counterparty_cd, "T", "0", "V");
					String account_cd_lng_sale="711008"; 	//for derivative 711008 code is used
					String dr_cr_marker=Double.parseDouble(gross_amt)<0?"C":"D";
					String accPeriod=getAccountingPeriod(invoice_dt);
					cccac="2MK01";
					
					for(int i=0;i<2;i++)
					{
						Element Line  = doc.createElement("Line");
						Element AccountCode = doc.createElement("AccountCode");
						Element AccountingPeriod = doc.createElement("AccountingPeriod");
						Element BaseAmount = doc.createElement("BaseAmount");
						Element DebitCredit = doc.createElement("DebitCredit");
						Element TransactionAmount = doc.createElement("TransactionAmount");
						Element Base2ReportingAmount = doc.createElement("Base2ReportingAmount");
						Element CurrencyCode = doc.createElement("CurrencyCode");
						Element CurrencyRate = doc.createElement("CurrencyRate");
						Element TransactionDate = doc.createElement("TransactionDate");
						Element JournalType = doc.createElement("JournalType");
						Element JournalSource = doc.createElement("JournalSource");			//Null in FMS8
						Element TransactionReference = doc.createElement("TransactionReference");
						Element Description = doc.createElement("Description");
						Element DueDate = doc.createElement("DueDate");
						Element DetailLad = doc.createElement("DetailLad");
						Element AnalysisCode1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
						Element AnalysisCode2 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
						Element AnalysisCode3 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
						Element AnalysisCode4 = doc.createElement("AnalysisCode4");			//COA_CD: 
						Element AnalysisCode5 = doc.createElement("AnalysisCode5");			//Null: in FMS8
						Element AnalysisCode6 = doc.createElement("AnalysisCode6");			
						Element AnalysisCode7 = doc.createElement("AnalysisCode7");			
						Element AnalysisCode8 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
						Element AnalysisCode10 = doc.createElement("AnalysisCode10");			//BU SUN account code 
						
						Ledger.appendChild(Line);
						Line.appendChild(AccountCode);
						Line.appendChild(AccountingPeriod);
						Line.appendChild(BaseAmount);
						Line.appendChild(DebitCredit);
						Line.appendChild(TransactionAmount);
						Line.appendChild(Base2ReportingAmount);
						Line.appendChild(CurrencyCode);
						Line.appendChild(CurrencyRate);
						Line.appendChild(TransactionDate);
						Line.appendChild(JournalType);
						Line.appendChild(JournalSource);		//Null in FMS8
						Line.appendChild(TransactionReference);
						Line.appendChild(Description);
						Line.appendChild(DueDate);
						
						Element AccountCode_d = doc.createElement("AccountCode");
						Element AccountingPeriod_d = doc.createElement("AccountingPeriod");
						Element GeneralDate4 = doc.createElement("GeneralDate4");
						Element JournalLineNumber = doc.createElement("JournalLineNumber");
						Element JournalNumber = doc.createElement("JournalNumber");
						Element TransactionDate_d=doc.createElement("TransactionDate");
						Element UserName=doc.createElement("UserName");
						
						DetailLad.appendChild(AccountCode_d);
						DetailLad.appendChild(AccountingPeriod_d);
						DetailLad.appendChild(GeneralDate4);
						DetailLad.appendChild(JournalLineNumber);
						DetailLad.appendChild(JournalNumber);
						DetailLad.appendChild(TransactionDate_d);
						DetailLad.appendChild(UserName);
						
						if(segment.equals("F")) 
						{
							Line.appendChild(DetailLad);
						}
						Line.appendChild(AnalysisCode1);
						Line.appendChild(AnalysisCode2);		
						Line.appendChild(AnalysisCode3);		
						Line.appendChild(AnalysisCode4);		
						Line.appendChild(AnalysisCode5);		
						Line.appendChild(AnalysisCode6);		
						Line.appendChild(AnalysisCode7);		
						Line.appendChild(AnalysisCode8);		
						Line.appendChild(AnalysisCode10);
						
						if(i==0)
						{
							AccountCode.appendChild(doc.createTextNode(account_cd));
							AccountCode_d.appendChild(doc.createTextNode(account_cd));
							DebitCredit.appendChild(doc.createTextNode(dr_cr_marker));
							AnalysisCode4.appendChild(doc.createTextNode(account_cd_lng_sale));
						}
						else
						{
							dr_cr_marker=Double.parseDouble(gross_amt)<0?"D":"C";
							AccountCode.appendChild(doc.createTextNode(account_cd_lng_sale));
							AccountCode_d.appendChild(doc.createTextNode(account_cd_lng_sale));
							DebitCredit.appendChild(doc.createTextNode(dr_cr_marker));
						}
						AccountingPeriod.appendChild(doc.createTextNode(accPeriod));
						//BaseAmount.appendChild(doc.createTextNode(""));
						TransactionAmount.appendChild(doc.createTextNode(inv_amt));
						Base2ReportingAmount.appendChild(doc.createTextNode("0"));
						CurrencyCode.appendChild(doc.createTextNode("USD"));
						//CurrencyRate.appendChild(doc.createTextNode(""));
						TransactionDate.appendChild(doc.createTextNode(trans_dt));
						JournalType.appendChild(doc.createTextNode(journal_type));
						//JournalSource.appendChild(doc.createTextNode(""));
						//TransactionReference.appendChild(doc.createTextNode(invoice_no));
						TransactionReference.appendChild(doc.createTextNode(remittance_no));	//AS PER ARUN JAIN FEEDBACK TO SHOW PARTY GENERATED INVOICE NO.
						Description.appendChild(doc.createTextNode(description));
						DueDate.appendChild(doc.createTextNode(inv_due_dt));
						//AnalysisCode1.appendChild(doc.createTextNode(""));
						AnalysisCode2.appendChild(doc.createTextNode(cccac));
						//AnalysisCode3.appendChild(doc.createTextNode(""));
						//AnalysisCode5.appendChild(doc.createTextNode(""));
						//AnalysisCode6.appendChild(doc.createTextNode(""));
						AnalysisCode7.appendChild(doc.createTextNode("NA"));
						//AnalysisCode8.appendChild(doc.createTextNode(""));
						AnalysisCode10.appendChild(doc.createTextNode(bucac));
						
						AccountingPeriod_d.appendChild(doc.createTextNode(accPeriod));
						GeneralDate4.appendChild(doc.createTextNode(trans_dt));
						TransactionDate_d.appendChild(doc.createTextNode(trans_dt));
					}
				}
				else
				{
					String report_amt=nf.format(0);
					
					bu_state_tin=utilBean.getState_TIN(conn, comp_cd, comp_cd, "B", bu_unit);
					String state_cd = getStateCd(bu_state_tin);
					String pur_account_cd="";
					String currency_unit="INR";
					
					String trader_sun_acc_cd="";
					if(purchase_type_flag.equals("GTA_S")||purchase_type_flag.equals("GTA_P")||purchase_type_flag.equals("GTA_FF"))
					{
						trader_sun_acc_cd=getCounterpartySunAccountCode(counterparty_cd,"R","0","DFT").trim();
						bucac="LNG";
					}
					else
					{
						if(!cont_type.equals("N"))
						{
							trader_sun_acc_cd=getCounterpartySunAccountCode(counterparty_cd,"T",plant_seq_no,"DFT").trim();
							if(trader_sun_acc_cd.equals(""))
							{
								trader_sun_acc_cd=getCounterpartySunAccountCode(counterparty_cd,"T","0","DFT").trim();
							}
						}
						else
						{
							trader_sun_acc_cd=getCounterpartySunAccountCode(counterparty_cd,"T","0","DFT").trim();
						}
					}
					
					String display_deal_map=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, cont_type, cargo_no);
					
					//NOTE: In FMS8: Accounting period for the provisional invoice is month_code+ACT_ARRV_DT(year) and transaction date is also ACT_ARRV_DT
					//NOTE for Provisional Custom DUTY 
					/*
				 	1. the Group Foreign Exchange Rate is used 
				  	2. For Custom Duty The tax BCD Tax.	(hard coded)
				  	3. transaction reference -> PROVN-Vessel Name
				  	4. due date is empty
				  	5. for custom duty the transaction date is custom duty date 
					 */
					
					//for fetching the actual arrival date of the cargo
					String act_arr_dt="";
					String query="SELECT TO_CHAR(BOE_DT,'DD/MM/YYYY') FROM FMS_BUY_CARGO_ALLOC_BOE A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? "
							+ "AND CARGO_NO=? AND CONT_REV=? AND ALLOC_REV=(SELECT MAX(ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC_BOE B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE  "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO  "
							+ "AND A.CONT_REV=B.CONT_REV  AND A.CARGO_NO=B.CARGO_NO)";
					stmt1=conn.prepareStatement(query);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, "M");
					stmt1.setString(4, agmt_no);
					stmt1.setString(5, agmt_rev);
					stmt1.setString(6, cont_type);
					stmt1.setString(7, cont_no);
					stmt1.setString(8, cargo_no);
					stmt1.setString(9, cont_rev);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						act_arr_dt=rset1.getString(1)==null?"":rset1.getString(1);
					}
					rset1.close();
					stmt1.close();
					String ship_cd="";
					String query_str1="SELECT TO_CHAR(ACT_ARRV_DT,'DD/MM/YYYY'),SHIP_CD FROM FMS_BUY_CARGO_ALLOC A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? "
							+ "AND CARGO_NO=? AND CONT_REV=? AND ALLOC_REV=(SELECT MAX(ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE  "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO  "
							+ "AND A.CONT_REV=B.CONT_REV  AND A.CARGO_NO=B.CARGO_NO)";
					query_str1+="UNION ";
					query_str1+="SELECT TO_CHAR(EXP_TO_DT,'DD/MM/YYYY'),SHIP_CD FROM FMS_BUY_CARGO_NOM A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? "
							+ "AND CARGO_NO=? AND CONT_REV=? AND NOM_REV=(SELECT MAX(NOM_REV) FROM FMS_BUY_CARGO_NOM B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO "
							+ "AND A.CONT_REV=B.CONT_REV  AND A.CARGO_NO=B.CARGO_NO)";
					stmt1=conn.prepareStatement(query_str1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, "M");
					stmt1.setString(4, agmt_no);
					stmt1.setString(5, agmt_rev);
					stmt1.setString(6, cont_type);
					stmt1.setString(7, cont_no);
					stmt1.setString(8, cargo_no);
					stmt1.setString(9, cont_rev);
					stmt1.setString(10, comp_cd);
					stmt1.setString(11, counterparty_cd);
					stmt1.setString(12, "M");
					stmt1.setString(13, agmt_no);
					stmt1.setString(14, agmt_rev);
					stmt1.setString(15, cont_type);
					stmt1.setString(16, cont_no);
					stmt1.setString(17, cargo_no);
					stmt1.setString(18, cont_rev);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						//act_arr_dt=rset1.getString(1)==null?"":rset1.getString(1);
						ship_cd=rset1.getString(2)==null?"":rset1.getString(2);
					}
					rset1.close();
					stmt1.close();
					
					act_arr_dt=act_arr_dt.equals("")?invoice_dt:act_arr_dt;
					
					String base_amt="";
					String final_custom_amt="";
					String custom_account_cd="";
					String custom_duty_tax_cd="";
					String tax_acc_cd="";
					String description=utilBean.getCounterpartyABBR(conn, counterparty_cd)+" "+alloc_qty+" MMBTU @ "+sale_price+" "+utilBean.getShipName(conn, ship_cd);
					String tax_description="";
					String tcs_tds_description="";
					String billing_note="";
					String code="";
					String analysis_cd5="";
					String description1="";
					if(segment.equals("P"))
					{
						pur_account_cd=getCounterpartySunAccountCode(counterparty_cd,"T","0","S").trim();
						currency_unit="USD";
						double exc_rate = getExchg_rate("SBI RATE SELL",act_arr_dt);
						if(Double.doubleToRawLongBits(exc_rate)==Double.doubleToRawLongBits(0))
						{
							base_amt="-";
							exchng_rate="-";
						}
						else
						{
							exchng_rate=""+exc_rate;
							base_amt = nf.format(Double.parseDouble(exchng_rate) * Double.parseDouble(inv_amt));
						}
					}
					else if(segment.equals("CP"))
					{
						//For Custom Duty The tax BCD Tax.
						pur_account_cd=getTcsTdsStructureSunAccountCd(taxStructCd, bu_state_tin, bu_unit).trim();		
						currency_unit="INR";
						act_arr_dt=invoice_dt;
						
						//exc_rate: needs to changed from SBI II Selling rate to Foreign Exchange rate 
						/*double exc_rate = getExchg_rate("4", act_arr_dt);		//For Provisional Custom Duty Cargo Group Foreign Exchange Rate is used
					if (exchng_rate.equals("")) 
					{
						base_amt = "0";
						exchng_rate = "-";
					} */
						if(!exchng_rate.equals("")) 
						{
							//base_amt = nf.format(Double.parseDouble(exchng_rate) * Double.parseDouble(net_payable));
							base_amt = nf.format(Double.parseDouble(net_payable));
							//exchng_rate=""+exc_rate;
						}
						//invoice_no="PROVN-"+utilBean.getShipName(conn, ship_cd);
					}
					else if(segment.equals("F")) 
					{
						if(cont_type.equals("N"))
						{
							report_amt=nf.format(0);
							pur_account_cd = getCounterpartySunAccountCode(counterparty_cd, "T", "0", "S").trim();
							currency_unit="USD";
							
							double exc_rate = getExchg_rate("SBI RATE SELL", act_arr_dt);			//SBI TT Selling Rate
							if (Double.doubleToRawLongBits(exc_rate)==Double.doubleToRawLongBits(0)) 
							{
								base_amt = "-";
								exchng_rate = "-";
							} 
							else 
							{
								exchng_rate=""+exc_rate;
								//base_amt = nf.format(Double.parseDouble(exchng_rate) * Double.parseDouble(inv_amt));
								//base_amt = nf.format(Double.parseDouble(net_payable)*Double.parseDouble(exchng_rate));
								base_amt=net_payable;
								net_payable = nf.format(Double.parseDouble(base_amt)*Double.parseDouble(exchng_rate));
							}
							description=utilBean.getCounterpartyABBR(conn, counterparty_cd)+" "+0+" MMBTU @ "+sale_price+" "+utilBean.getShipName(conn, ship_cd);
						}
						else
						{
							invoice_dt=utilDate.getMonthNameMON(period_end_dt).equals(utilDate.getMonthNameMON(inv_approve_dt))?inv_approve_dt:period_end_dt;
							
							act_arr_dt=invoice_dt;
							act_arr_dt=invoice_dt;
							if(cont_type.equals("I"))
							{
								pur_account_cd=getCounterpartySunAccountCode(counterparty_cd, "T", plant_seq_no, "I").trim();		//fetching the purchase account code plant wise
								if(pur_account_cd.equals(""))
								{
									pur_account_cd=getCounterpartySunAccountCode(counterparty_cd, "T", "0", "I").trim();	//if the account code is not configured for the plant
								}
							}
							else
							{
								pur_account_cd=getCounterpartySunAccountCode(counterparty_cd, "T", plant_seq_no, "S").trim();		//fetching the purchase account code plant wise
								if(pur_account_cd.equals(""))
								{
									pur_account_cd=getCounterpartySunAccountCode(counterparty_cd, "T", "0", "S").trim();	//if the account code is not configured for the plant
								}
							}
							
							currency_unit="INR";
							base_amt=gross_amt;
							
							if(invoice_raised_in.equals("2"))
							{
								if(purchase_type_flag.equals("FF")||purchase_type_flag.equals("GTA_FF"))
								{
									base_amt=gross_amt_usd;
								}
								report_amt=nf.format(0);
								currency_unit="USD";
								if(!exchng_rate.contentEquals(""))
								{
									if(purchase_type_flag.equals("FF")||purchase_type_flag.equals("GTA_FF"))
									{
										sale_price=nf.format(Double.parseDouble(base_amt)/Double.parseDouble(alloc_qty));
										net_payable=nf.format(Double.parseDouble(gross_amt_usd)*Double.parseDouble(exchng_rate));
									}
									else
									{
										net_payable=nf.format(Double.parseDouble(gross_amt)*Double.parseDouble(exchng_rate));
									}
								}
							}
							else
							{
								net_payable=base_amt;
								if(purchase_type_flag.equals("FF")||purchase_type_flag.equals("GTA_FF"))
								{
									sale_price=nf.format(Double.parseDouble(base_amt)/Double.parseDouble(alloc_qty));
								}
								if(!exchng_rate.contentEquals(""))
								{
									if(Double.doubleToRawLongBits(Double.parseDouble(exchng_rate))!=Double.doubleToRawLongBits(0))
									{
										report_amt=nf.format(Double.parseDouble(net_payable)/Double.parseDouble(exchng_rate));
									}
									else
									{
										report_amt=nf.format(0);
									}
								}
								else
								{
									report_amt=nf.format(0);
								}
								analysis_cd5="NA";
							}
							
							if(billing_freq.equals("1")||billing_freq.equals("2"))
							{
								billing_note=billing_freq+"FN";
							}
							else if(billing_freq.equals("3") || billing_freq.equals("4") || billing_freq.equals("5") || billing_freq.equals("6") || billing_freq.equals("9"))
							{
								billing_note=billing_freq+"W";
							}
							else if(billing_freq.equals("7"))
							{
								billing_note=billing_freq+"M";
							}
							else if(billing_freq.equals("8"))
							{
								billing_note=billing_freq+"O";
							}
							else if(billing_freq.equals("10"))
							{
								billing_note=billing_freq+"D";
							}
							if(!bu_state_tin.equals("24"))
							{
								cccac=2+state_cd+"01";
							}
							else
							{
								cccac="1TM01";
							}
							cccaac=pur_account_cd;
							
							if(cont_type.equals("I"))
							{
								code="VCQ3";
							}
							else 
							{
								if(tcs_tds.equals("TCS"))
								{
									code="NA";
								}
								else
								{
									//code="WCQ3";
									//code="XCQ3";
									if(!tds_amt.equals("") && Double.doubleToRawLongBits(Double.parseDouble(tds_amt))>Double.doubleToRawLongBits(0))
									{
										code="YCQ3";		//20250728: AS PER ARUN INSTRUCTION: CHANGED FOR FINANCIAL YEAR 2025-2026
									}
									else
									{
										code="NA";		//20250811: AS PER ARUN FEEDBACK: CHAGED TO 'NA' IF THE 0% TDS IS APPLICABLE.
									}
								}
							}
							
							String mtn=utilDate.getMonthNameMON(period_st_dt);
							description=utilBean.getCounterpartyABBR(conn, counterparty_cd)+" "+state_cd+" "+alloc_qty+" MMBTU @ "+sale_price+" "+billing_note+" "+mtn;
							if(!exchng_rate.equals(""))
							{
								if(Double.doubleToRawLongBits(Double.parseDouble(exchng_rate))!=Double.doubleToRawLongBits(0))
								{
									description+=" @"+exchng_rate;
								}
							}
						}
						if(purchase_type_flag.equals("GTA_S") || purchase_type_flag.equals("GTA_P") || purchase_type_flag.equals("GTA_FF"))
						{
							
							act_arr_dt=invoice_dt;
							//act_arr_dt=period_st_dt;
							pur_account_cd=getCounterpartySunAccountCode(counterparty_cd, "R", "0", "R");
							if(cont_type.equals("K"))	//for parking 
							{
								pur_account_cd=getCounterpartySunAccountCode(counterparty_cd, "R", "0", "K");
							}
							cccaac=pur_account_cd;
							//description1=utilBean.getCounterpartyABBR(conn, counterparty_cd)+"  GAS TRANSMISSION CH  "+period_st_dt.split("/")[0]+" - "+period_end_dt; 
							description=utilBean.getCounterpartyABBR(conn, counterparty_cd)+"  GAS TRANSMISSION CH  "+invoice_dt.split("/")[0]+" - "+period_end_dt; 
							
							//code
							if(tds_factor.equals(""))
							{
								tds_factor=getTdsFactor(tdsStructCd);
							}
							code=getTdsAnalysisCode(tds_factor);
						}
					}
					else if(segment.equals("CF"))
					{
						//For Custom Duty The tax BCD Tax.
						custom_duty_tax_cd=getTcsTdsStructureSunAccountCd(taxStructCd, bu_state_tin, bu_unit).trim();
						custom_account_cd=getCounterpartySunAccountCode(counterparty_cd, "T", "0", "CD").trim();
						act_arr_dt=invoice_dt;
						//double exc_rate = getExchg_rate("4", act_arr_dt);			//SBI TT Selling Rate
						
						if(!net_payable.equals(""))
						{
							base_amt=net_payable;
							description =""+utilBean.getCounterpartyABBR(conn, counterparty_cd)+" Custom Duty Trf - "+utilBean.getShipName(conn, ship_cd);
							
							final_custom_amt=nf.format(Double.parseDouble(base_amt)-intrest_amt);
						}
						double diff_qty=getDeltaQunatity(counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, cont_type, cargo_no);
						String increment_decrement_qty="";
						String diff_qty_remark="";
						if(Double.doubleToRawLongBits(diff_qty)>Double.doubleToRawLongBits(0))
						{
							increment_decrement_qty=nf.format(diff_qty);
							diff_qty_remark="excess";
						}
						else if(Double.doubleToRawLongBits(diff_qty)<Double.doubleToRawLongBits(0))
						{
							increment_decrement_qty=nf.format(diff_qty*-1);
							diff_qty_remark="short";
						}
						else
						{
							increment_decrement_qty=nf.format(diff_qty);
						}
						description1 = (utilBean.getCounterpartyABBR(conn, counterparty_cd)+" "+diff_qty_remark+" "+increment_decrement_qty+" MMBTU @ "+sale_price+" "+utilBean.getShipName(conn, ship_cd)).toUpperCase();
						if(!net_payable.equals(""))
						{
							base_amt=net_payable;
							description =""+utilBean.getCounterpartyABBR(conn, counterparty_cd)+" Custom Duty Trf - "+utilBean.getShipName(conn, ship_cd);
							
							final_custom_amt=nf.format(Double.parseDouble(base_amt)-intrest_amt);
						}
					}
					description=description.toUpperCase();
					String trans_dt=act_arr_dt.split("/")[0]+act_arr_dt.split("/")[1]+act_arr_dt.split("/")[2];
					String account_period=getAccountingPeriod(act_arr_dt);
					
					if(inv_flag.equals("CP"))
					{
						//invoice_no="PROVN-"+utilBean.getShipName(conn, ship_cd);
					}
					inv_due_dt=inv_due_dt.split("/")[0]+inv_due_dt.split("/")[1]+inv_due_dt.split("/")[2];
					for(int i=0;i<2;i++)
					{
						String report_amt_temp=report_amt;
						
						Element Line  = doc.createElement("Line");
						Element AccountCode = doc.createElement("AccountCode");
						Element AccountingPeriod = doc.createElement("AccountingPeriod");
						Element BaseAmount = doc.createElement("BaseAmount");
						Element DebitCredit = doc.createElement("DebitCredit");
						Element TransactionAmount = doc.createElement("TransactionAmount");
						Element Base2ReportingAmount = doc.createElement("Base2ReportingAmount");
						Element CurrencyCode = doc.createElement("CurrencyCode");
						Element CurrencyRate = doc.createElement("CurrencyRate");
						Element TransactionDate = doc.createElement("TransactionDate");
						Element JournalType = doc.createElement("JournalType");
						Element JournalSource = doc.createElement("JournalSource");			//Null in FMS8
						Element TransactionReference = doc.createElement("TransactionReference");
						Element Description = doc.createElement("Description");
						Element DueDate = doc.createElement("DueDate");
						Element DetailLad = doc.createElement("DetailLad");
						Element AnalysisCode1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
						Element AnalysisCode2 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
						Element AnalysisCode3 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
						Element AnalysisCode4 = doc.createElement("AnalysisCode4");			//COA_CD: 
						Element AnalysisCode5 = doc.createElement("AnalysisCode5");			//Null: in FMS8
						Element AnalysisCode6 = doc.createElement("AnalysisCode6");			
						Element AnalysisCode7 = doc.createElement("AnalysisCode7");			
						Element AnalysisCode8 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
						Element AnalysisCode10 = doc.createElement("AnalysisCode10");			//BU SUN account code 
						
						Ledger.appendChild(Line);
						Line.appendChild(AccountCode);
						Line.appendChild(AccountingPeriod);
						Line.appendChild(BaseAmount);
						Line.appendChild(DebitCredit);
						Line.appendChild(TransactionAmount);
						Line.appendChild(Base2ReportingAmount);
						Line.appendChild(CurrencyCode);
						Line.appendChild(CurrencyRate);
						Line.appendChild(TransactionDate);
						Line.appendChild(JournalType);
						Line.appendChild(JournalSource);		//Null in FMS8
						Line.appendChild(TransactionReference);
						Line.appendChild(Description);
						Line.appendChild(DueDate);
						if(segment.equals("F")) 
						{
							Line.appendChild(DetailLad);
						}
						Line.appendChild(AnalysisCode1);
						Line.appendChild(AnalysisCode2);		
						Line.appendChild(AnalysisCode3);		
						Line.appendChild(AnalysisCode4);		
						Line.appendChild(AnalysisCode5);		
						Line.appendChild(AnalysisCode6);		
						Line.appendChild(AnalysisCode7);		
						Line.appendChild(AnalysisCode8);		
						Line.appendChild(AnalysisCode10);		
						
						if(segment.equals("P"))
						{
							if(i%2==0)
							{
								AccountCode.appendChild(doc.createTextNode(pur_account_cd));
								DebitCredit.appendChild(doc.createTextNode("D"));
								AnalysisCode3.appendChild(doc.createTextNode(ecac));
							}
							else
							{
								AccountCode.appendChild(doc.createTextNode(trader_sun_acc_cd));
								DebitCredit.appendChild(doc.createTextNode("C"));
								DueDate.appendChild(doc.createTextNode(inv_due_dt));
								AnalysisCode7.appendChild(doc.createTextNode("NA"));
							}
							AnalysisCode2.appendChild(doc.createTextNode(cccac));
							TransactionAmount.appendChild(doc.createTextNode(inv_amt));
							AnalysisCode4.appendChild(doc.createTextNode(cccaac));
							BaseAmount.appendChild(doc.createTextNode(base_amt));
							Description.appendChild(doc.createTextNode(description));
						}
						else if(segment.equals("CP"))
						{
							if(i%2==0)
							{
								AccountCode.appendChild(doc.createTextNode(pur_account_cd));
								DebitCredit.appendChild(doc.createTextNode("D"));
								AnalysisCode2.appendChild(doc.createTextNode(cccac));
								AnalysisCode4.appendChild(doc.createTextNode(statutory_payment));
							}
							else
							{
								AccountCode.appendChild(doc.createTextNode(statutory_payment));
								DebitCredit.appendChild(doc.createTextNode("C"));
								AnalysisCode4.appendChild(doc.createTextNode(pur_account_cd));
								AnalysisCode7.appendChild(doc.createTextNode("NA"));
							}
							TransactionAmount.appendChild(doc.createTextNode(base_amt));
							BaseAmount.appendChild(doc.createTextNode(base_amt));
							Description.appendChild(doc.createTextNode(description));
						}
						else if(segment.equals("F")) 
						{
							//DetailLad element present only in the final invoice 
							Element AccountCode_d = doc.createElement("AccountCode");
							Element AccountingPeriod_d = doc.createElement("AccountingPeriod");
							Element GeneralDate4 = doc.createElement("GeneralDate4");
							Element JournalLineNumber = doc.createElement("JournalLineNumber");
							Element JournalNumber = doc.createElement("JournalNumber");
							Element TransactionDate_d=doc.createElement("TransactionDate");
							Element UserName=doc.createElement("UserName");
							
							DetailLad.appendChild(AccountCode_d);
							DetailLad.appendChild(AccountingPeriod_d);
							DetailLad.appendChild(GeneralDate4);
							DetailLad.appendChild(JournalLineNumber);
							DetailLad.appendChild(JournalNumber);
							DetailLad.appendChild(TransactionDate_d);
							DetailLad.appendChild(UserName);
							
							if(!net_payable.equals(""))
							{
								String temp_net_payable="";
								String temp_base_amt="";
								if(Double.doubleToRawLongBits(Double.parseDouble(net_payable))>=Double.doubleToRawLongBits(0))
								{
									temp_net_payable=nf.format(Double.parseDouble(net_payable));
									temp_base_amt=nf.format(Double.parseDouble(base_amt));
									if(i%2==0)
									{
										String acc_cd=invoice_type.equals("CR")?trader_sun_acc_cd:pur_account_cd;
										String dr_cr_marker=invoice_type.equals("DR")?"C":"D";	//AS PER ARUN INSTRUCTION, SINCE IN DEBIT NOTE THE AMOUNT IS RECEIVED BY THE SEI
										AccountCode.appendChild(doc.createTextNode(acc_cd));
										//DebitCredit.appendChild(doc.createTextNode("D"));
										DebitCredit.appendChild(doc.createTextNode(dr_cr_marker));
										AnalysisCode3.appendChild(doc.createTextNode(ecac));
										
										AccountCode_d.appendChild(doc.createTextNode(acc_cd));
										
										if(purchase_type_flag.equals("GTA_S")||purchase_type_flag.equals("GTA_P")||purchase_type_flag.equals("GTA_FF"))
										{
											AnalysisCode7.appendChild(doc.createTextNode(code));
											DueDate.appendChild(doc.createTextNode(inv_due_dt));
										}
									}
									else
									{
										if(!cont_type.equals("N") && !purchase_type_flag.equals("GTA_S") && !purchase_type_flag.equals("GTA_P") && !purchase_type_flag.equals("GTA_FF"))
										{
											temp_net_payable=nf.format(Double.parseDouble(net_payable)*-1);
											temp_base_amt=nf.format(Double.parseDouble(base_amt)*-1);
											report_amt_temp=Double.doubleToRawLongBits(Double.parseDouble(report_amt_temp))==Double.doubleToRawLongBits(0)?nf.format(0):nf.format(Double.parseDouble(report_amt_temp)*(-1));
										}
										
										String acc_cd=invoice_type.equals("CR")?pur_account_cd:trader_sun_acc_cd;
										String dr_cr_marker=invoice_type.equals("DR")?"D":"C";
										AccountCode.appendChild(doc.createTextNode(acc_cd));
										//DebitCredit.appendChild(doc.createTextNode("C"));
										DebitCredit.appendChild(doc.createTextNode(dr_cr_marker));
										DueDate.appendChild(doc.createTextNode(inv_due_dt));
										
										if(purchase_type_flag.equals("GTA_S")||purchase_type_flag.equals("GTA_P")||purchase_type_flag.equals("GTA_FF"))
										{
											AnalysisCode3.appendChild(doc.createTextNode(ecac));
										}
										if(cont_type.equals("N"))
										{
											AnalysisCode7.appendChild(doc.createTextNode("NA"));
										}
										else
										{
											AnalysisCode7.appendChild(doc.createTextNode(code));
										}
										AnalysisCode5.appendChild(doc.createTextNode(analysis_cd5));
										
										AccountCode_d.appendChild(doc.createTextNode(acc_cd));
									}
								}
								else
								{
									temp_net_payable=nf.format(Double.parseDouble(net_payable)*-1);
									temp_base_amt=nf.format(Double.parseDouble(base_amt)*-1);
									if(i%2==0)
									{
										AccountCode.appendChild(doc.createTextNode(trader_sun_acc_cd));
										DebitCredit.appendChild(doc.createTextNode("D"));
										DueDate.appendChild(doc.createTextNode(inv_due_dt));
										AnalysisCode7.appendChild(doc.createTextNode("NA"));
										
										AccountCode_d.appendChild(doc.createTextNode(trader_sun_acc_cd));
									}
									else
									{
										AccountCode.appendChild(doc.createTextNode(pur_account_cd));
										DebitCredit.appendChild(doc.createTextNode("C"));
										AnalysisCode3.appendChild(doc.createTextNode(ecac));
										
										AccountCode_d.appendChild(doc.createTextNode(pur_account_cd));
									}
								}
								BaseAmount.appendChild(doc.createTextNode(temp_net_payable));
								TransactionAmount.appendChild(doc.createTextNode(temp_base_amt));
								AnalysisCode2.appendChild(doc.createTextNode(cccac));
								AnalysisCode4.appendChild(doc.createTextNode(cccaac));
								
								AccountingPeriod_d.appendChild(doc.createTextNode(account_period));
								GeneralDate4.appendChild(doc.createTextNode(trans_dt));
								TransactionDate_d.appendChild(doc.createTextNode(trans_dt));
							}
							Description.appendChild(doc.createTextNode(description));
						}
						else if(segment.equals("CF"))
						{
							if(!net_payable.equals(""))
							{
								if(Double.doubleToRawLongBits(Double.parseDouble(net_payable))>=Double.doubleToRawLongBits(0))
								{
									if(i%2==0)
									{
										AccountCode.appendChild(doc.createTextNode(custom_duty_tax_cd));
										DebitCredit.appendChild(doc.createTextNode("D"));
										AnalysisCode4.appendChild(doc.createTextNode(statutory_payment));
										AnalysisCode2.appendChild(doc.createTextNode(cccac));
									}
									else
									{
										AccountCode.appendChild(doc.createTextNode(statutory_payment));
										DebitCredit.appendChild(doc.createTextNode("C"));
										AnalysisCode4.appendChild(doc.createTextNode(custom_duty_tax_cd));
										AnalysisCode7.appendChild(doc.createTextNode("NA"));
										
									}
									BaseAmount.appendChild(doc.createTextNode(net_payable));
									TransactionAmount.appendChild(doc.createTextNode(net_payable));
								}
								else
								{
									if(i%2==0)
									{
										AccountCode.appendChild(doc.createTextNode(custom_account_cd));
										DebitCredit.appendChild(doc.createTextNode("D"));
										AnalysisCode4.appendChild(doc.createTextNode(custom_duty_tax_cd));
									}
									else
									{
										AccountCode.appendChild(doc.createTextNode(custom_duty_tax_cd));
										DebitCredit.appendChild(doc.createTextNode("C"));
										AnalysisCode4.appendChild(doc.createTextNode(custom_account_cd));
									}
									BaseAmount.appendChild(doc.createTextNode(base_amt));
									TransactionAmount.appendChild(doc.createTextNode(base_amt));
									//VINVOICE_DUE_DT.add("");
									//VCODE.add("");
									AnalysisCode2.appendChild(doc.createTextNode(cccac));
								}
								//VEMPLOYEE_CD.add("");
							}
							Description.appendChild(doc.createTextNode(description1));
						}
						AccountingPeriod.appendChild(doc.createTextNode(account_period));
						Base2ReportingAmount.appendChild(doc.createTextNode(report_amt_temp));		
						CurrencyCode.appendChild(doc.createTextNode(currency_unit));
						CurrencyRate.appendChild(doc.createTextNode(exchng_rate));					
						TransactionDate.appendChild(doc.createTextNode(trans_dt));
						JournalType.appendChild(doc.createTextNode(journal_type));
						//TransactionReference.appendChild(doc.createTextNode(remittance_no));
						TransactionReference.appendChild(doc.createTextNode(invoice_no));
						AnalysisCode10.appendChild(doc.createTextNode(bucac));
						
						//for IGX settlement Account
						if(cont_type.equals("I")&&!igx_account_cd.equals(""))
						{
							Element Line1  = doc.createElement("Line");
							Element AccountCode1 = doc.createElement("AccountCode");
							Element AccountingPeriod1 = doc.createElement("AccountingPeriod");
							Element BaseAmount1 = doc.createElement("BaseAmount");
							Element DebitCredit1 = doc.createElement("DebitCredit");
							Element TransactionAmount1 = doc.createElement("TransactionAmount");
							Element Base2ReportingAmount1 = doc.createElement("Base2ReportingAmount");
							Element CurrencyCode1 = doc.createElement("CurrencyCode");
							Element CurrencyRate1 = doc.createElement("CurrencyRate");
							Element TransactionDate1 = doc.createElement("TransactionDate");
							Element JournalType1 = doc.createElement("JournalType");
							Element JournalSource1 = doc.createElement("JournalSource");			//Null in FMS8
							Element TransactionReference1 = doc.createElement("TransactionReference");
							Element Description1 = doc.createElement("Description");
							Element DueDate1 = doc.createElement("DueDate");
							Element DetailLad1 = doc.createElement("DetailLad");
							Element AnalysisCode1_1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
							Element AnalysisCode2_1 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
							Element AnalysisCode3_1 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
							Element AnalysisCode4_1 = doc.createElement("AnalysisCode4");			//COA_CD: 
							Element AnalysisCode5_1 = doc.createElement("AnalysisCode5");			//Null: in FMS8
							Element AnalysisCode6_1 = doc.createElement("AnalysisCode6");			
							Element AnalysisCode7_1 = doc.createElement("AnalysisCode7");			
							Element AnalysisCode8_1 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
							Element AnalysisCode10_1 = doc.createElement("AnalysisCode10");			//BU SUN account code 
							Element AccountCode_d = doc.createElement("AccountCode");
							Element AccountingPeriod_d = doc.createElement("AccountingPeriod");
							Element GeneralDate4 = doc.createElement("GeneralDate4");
							Element JournalLineNumber = doc.createElement("JournalLineNumber");
							Element JournalNumber = doc.createElement("JournalNumber");
							Element TransactionDate_d=doc.createElement("TransactionDate");
							Element UserName=doc.createElement("UserName");
							
							Ledger.appendChild(Line1);
							Line1.appendChild(AccountCode1);
							Line1.appendChild(AccountingPeriod1);
							Line1.appendChild(BaseAmount1);
							Line1.appendChild(DebitCredit1);
							Line1.appendChild(TransactionAmount1);
							Line1.appendChild(Base2ReportingAmount1);
							Line1.appendChild(CurrencyCode1);
							Line1.appendChild(CurrencyRate1);
							Line1.appendChild(TransactionDate1);
							Line1.appendChild(JournalType1);
							Line1.appendChild(JournalSource1);		//Null in FMS8
							Line1.appendChild(TransactionReference1);
							Line1.appendChild(Description1);
							Line1.appendChild(DueDate1);
							if(segment.equals("F")) 
							{
								Line1.appendChild(DetailLad1);
								DetailLad1.appendChild(AccountCode_d);
								DetailLad1.appendChild(AccountingPeriod_d);
								DetailLad1.appendChild(GeneralDate4);
								DetailLad1.appendChild(JournalLineNumber);
								DetailLad1.appendChild(JournalNumber);
								DetailLad1.appendChild(TransactionDate_d);
								DetailLad1.appendChild(UserName);
							}
							Line1.appendChild(AnalysisCode1_1);
							Line1.appendChild(AnalysisCode2_1);		
							Line1.appendChild(AnalysisCode3_1);		
							Line1.appendChild(AnalysisCode4_1);		
							Line1.appendChild(AnalysisCode5_1);		
							Line1.appendChild(AnalysisCode6_1);		
							Line1.appendChild(AnalysisCode7_1);		
							Line1.appendChild(AnalysisCode8_1);		
							Line1.appendChild(AnalysisCode10_1);
							
							String temp_net_payable="";
							String temp_base_amt="";
							if(i==0)
							{
								temp_net_payable=nf.format(Double.parseDouble(net_payable));
								temp_base_amt=nf.format(Double.parseDouble(base_amt));
								if(!net_payable.equals(""))
								{
									AccountCode1.appendChild(doc.createTextNode(igx_account_cd));
									DebitCredit1.appendChild(doc.createTextNode("C"));
									AnalysisCode3_1.appendChild(doc.createTextNode(ecac));
									
									AccountCode_d.appendChild(doc.createTextNode(igx_account_cd));
								}
							}
							else
							{
								temp_net_payable=nf.format(Double.parseDouble(net_payable)*-1);
								temp_base_amt=nf.format(Double.parseDouble(base_amt)*-1);
								report_amt_temp=Double.doubleToRawLongBits(Double.parseDouble(report_amt_temp))==Double.doubleToRawLongBits(0)?nf.format(0):nf.format(Double.parseDouble(report_amt_temp)*(-1));
								
								String acc_cd=invoice_type.equals("CR")?pur_account_cd:trader_sun_acc_cd;
								AccountCode1.appendChild(doc.createTextNode(acc_cd));
								DebitCredit1.appendChild(doc.createTextNode("D"));
								DueDate1.appendChild(doc.createTextNode(inv_due_dt));
								AnalysisCode7_1.appendChild(doc.createTextNode(code));
								AnalysisCode5_1.appendChild(doc.createTextNode(analysis_cd5));
								AccountCode_d.appendChild(doc.createTextNode(acc_cd));
							}
							BaseAmount1.appendChild(doc.createTextNode(temp_net_payable));
							TransactionAmount1.appendChild(doc.createTextNode(temp_base_amt));
							AnalysisCode2_1.appendChild(doc.createTextNode(cccac));
							AnalysisCode4_1.appendChild(doc.createTextNode(cccaac));
							
							AccountingPeriod_d.appendChild(doc.createTextNode(account_period));
							GeneralDate4.appendChild(doc.createTextNode(trans_dt));
							TransactionDate_d.appendChild(doc.createTextNode(trans_dt));
							AccountingPeriod1.appendChild(doc.createTextNode(account_period));
							Base2ReportingAmount1.appendChild(doc.createTextNode(report_amt_temp));		
							CurrencyCode1.appendChild(doc.createTextNode(currency_unit));
							CurrencyRate1.appendChild(doc.createTextNode(exchng_rate));					
							TransactionDate1.appendChild(doc.createTextNode(trans_dt));
							JournalType1.appendChild(doc.createTextNode(journal_type));
							//TransactionReference1.appendChild(doc.createTextNode(remittance_no));
							TransactionReference1.appendChild(doc.createTextNode(invoice_no));
							Description1.appendChild(doc.createTextNode(description));
							AnalysisCode10_1.appendChild(doc.createTextNode(bucac));
						}
					}
					
					//for LNG, IN-Tank
					if(segment.equals("F")&& !cont_type.equals("N"))
					{
						String query1="";
						String acc_cd="";
						if(purchase_type_flag.equals("S"))
						{
							query1="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_PUR_SG_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INV_FLAG=?";
						}
						else if(purchase_type_flag.equals("P"))
						{
							query1="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_PUR_PG_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INV_FLAG=?";
						}
						else if (purchase_type_flag.equals("FF")) 
						{
							query1 = "SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_PUR_FFLOW_INV_TAX_DTL " + "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=?";
						}
						else if(purchase_type_flag.equals("GTA_S"))
						{
							query1="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_GTA_SG_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? ";
						}
						else if(purchase_type_flag.equals("GTA_P"))
						{
							query1="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_GTA_PG_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? ";
						}
						else if(purchase_type_flag.equals("GTA_FF"))
						{
							query1="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_GTA_FFLOW_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? ";
						}
						stmt5=conn.prepareStatement(query1);
						stmt5.setString(1, comp_cd);
						stmt5.setString(2, fin_year);
						stmt5.setString(3, invoice_seq);
						stmt5.setString(4, cont_type);
						if(purchase_type_flag.equals("FF")|| purchase_type_flag.equals("GTA_S")||purchase_type_flag.equals("GTA_P")||purchase_type_flag.equals("GTA_FF"))
						{
							stmt5.setString(5, invoice_type);
						}
						else
						{
							stmt5.setString(5, inv_flag);
						}
						rset5=stmt5.executeQuery();
						while(rset5.next())
						{
							String tax_code=rset5.getString(1)==null?"":rset5.getString(1);
							String taxStrctCd=rset5.getString(2)==null?"":rset5.getString(2);
							String taxAmt=rset5.getString(3)==null?"":nf.format(rset5.getDouble(3));
							String taxBaseAmt=rset5.getString(4)==null?"":nf.format(rset5.getDouble(4));
							
							String mtn=utilDate.getMonthNameMON(period_st_dt);
							String tax_sht_nm=getTax_sht_nm(tax_code);
							tax_description=utilBean.getCounterpartyABBR(conn, counterparty_cd)+" "+state_cd+" "+alloc_qty+" MMBTU @ "+sale_price+" "+billing_note+" "+mtn+" "+tax_sht_nm;
							if(!exchng_rate.equals(""))
							{
								if(Double.doubleToRawLongBits(Double.parseDouble(exchng_rate))!=Double.doubleToRawLongBits(0))
								{
									tax_description+=" @"+exchng_rate;
								}
							}
							tax_description=tax_description.toUpperCase();
							String trans_amt=taxAmt;
							if(invoice_raised_in.equals("2") && !exchng_rate.equals(""))
							{
								taxAmt=nf.format(Double.parseDouble(taxAmt) * Double.parseDouble(exchng_rate));
							}
							tax_acc_cd=getTaxStructureSunAccountCd(taxStructCd, tax_code, bu_state_tin, bu_unit,"S").trim();
							
							String taxacc=tax_acc_cd;
							
							//20251014: IT WAS CONSIDERING PUR_ACCOUNT_CODE IN FMS8 
							//AS PER COMMUNICATING WITH MILIND AND VAISHNAVI referring remittance. 3251202052
							//FROM NOW ONWARDS IT WILL FOLLOW FRONT-END INPUT.
							/*if(tax_sht_nm.contains("CST"))
							{
								taxacc=pur_account_cd;
							}*/
							
							if(invoice_type.equals("CR"))
							{
								acc_cd=trader_sun_acc_cd;
							}
							else
							{
								acc_cd=taxacc;
							}
							
							Element Line  = doc.createElement("Line");
							Element AccountCode = doc.createElement("AccountCode");
							Element AccountingPeriod = doc.createElement("AccountingPeriod");
							Element BaseAmount = doc.createElement("BaseAmount");
							Element DebitCredit = doc.createElement("DebitCredit");
							Element TransactionAmount = doc.createElement("TransactionAmount");
							Element Base2ReportingAmount = doc.createElement("Base2ReportingAmount");
							Element CurrencyCode = doc.createElement("CurrencyCode");
							Element CurrencyRate = doc.createElement("CurrencyRate");
							Element TransactionDate = doc.createElement("TransactionDate");
							Element JournalType = doc.createElement("JournalType");
							Element JournalSource = doc.createElement("JournalSource");			//Null in FMS8
							Element TransactionReference = doc.createElement("TransactionReference");
							Element Description = doc.createElement("Description");
							Element DueDate = doc.createElement("DueDate");
							Element DetailLad = doc.createElement("DetailLad");
							Element AnalysisCode1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
							Element AnalysisCode2 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
							Element AnalysisCode3 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
							Element AnalysisCode4 = doc.createElement("AnalysisCode4");			//COA_CD: 
							Element AnalysisCode5 = doc.createElement("AnalysisCode5");			//Null: in FMS8
							Element AnalysisCode6 = doc.createElement("AnalysisCode6");			
							Element AnalysisCode7 = doc.createElement("AnalysisCode7");			
							Element AnalysisCode8 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
							Element AnalysisCode10 = doc.createElement("AnalysisCode10");			//BU SUN account code 
							
							Ledger.appendChild(Line);
							Line.appendChild(AccountCode);
							Line.appendChild(AccountingPeriod);
							Line.appendChild(BaseAmount);
							Line.appendChild(DebitCredit);
							Line.appendChild(TransactionAmount);
							Line.appendChild(Base2ReportingAmount);
							Line.appendChild(CurrencyCode);
							Line.appendChild(CurrencyRate);
							Line.appendChild(TransactionDate);
							Line.appendChild(JournalType);
							Line.appendChild(JournalSource);		//Null in FMS8
							Line.appendChild(TransactionReference);
							Line.appendChild(Description);
							Line.appendChild(DueDate);
							if(segment.equals("F")) 
							{
								Line.appendChild(DetailLad);
							}
							Line.appendChild(AnalysisCode1);
							Line.appendChild(AnalysisCode2);		
							Line.appendChild(AnalysisCode3);		
							Line.appendChild(AnalysisCode4);		
							Line.appendChild(AnalysisCode5);		
							Line.appendChild(AnalysisCode6);		
							Line.appendChild(AnalysisCode7);		
							Line.appendChild(AnalysisCode8);		
							Line.appendChild(AnalysisCode10);
							
							Element AccountCode_d = doc.createElement("AccountCode");
							Element AccountingPeriod_d = doc.createElement("AccountingPeriod");
							Element GeneralDate4 = doc.createElement("GeneralDate4");
							Element JournalLineNumber = doc.createElement("JournalLineNumber");
							Element JournalNumber = doc.createElement("JournalNumber");
							Element TransactionDate_d=doc.createElement("TransactionDate");
							Element UserName=doc.createElement("UserName");
							
							DetailLad.appendChild(AccountCode_d);
							DetailLad.appendChild(AccountingPeriod_d);
							DetailLad.appendChild(GeneralDate4);
							DetailLad.appendChild(JournalLineNumber);
							DetailLad.appendChild(JournalNumber);
							DetailLad.appendChild(TransactionDate_d);
							DetailLad.appendChild(UserName);
							
							if(purchase_type_flag.equals("GTA_S")||purchase_type_flag.equals("GTA_P")||purchase_type_flag.equals("GTA_FF"))
							{
								AccountCode.appendChild(doc.createTextNode(trans_acc_cd));
								Description.appendChild(doc.createTextNode(description));
								DueDate.appendChild(doc.createTextNode(inv_due_dt));
								AnalysisCode7.appendChild(doc.createTextNode("NA"));
								AccountCode_d.appendChild(doc.createTextNode(acc_cd));
							}
							else
							{
								AccountCode.appendChild(doc.createTextNode(acc_cd));
								Description.appendChild(doc.createTextNode(tax_description));
								AccountCode_d.appendChild(doc.createTextNode(acc_cd));
							}
							
							String dr_cr_marker=invoice_type.equals("DR")?"C":"D";
							AccountingPeriod.appendChild(doc.createTextNode(account_period));
							BaseAmount.appendChild(doc.createTextNode(taxAmt));
							//DebitCredit.appendChild(doc.createTextNode("D"));
							DebitCredit.appendChild(doc.createTextNode(dr_cr_marker));
							TransactionAmount.appendChild(doc.createTextNode(trans_amt));
							Base2ReportingAmount.appendChild(doc.createTextNode(nf.format(0)));
							CurrencyCode.appendChild(doc.createTextNode(currency_unit));
							CurrencyRate.appendChild(doc.createTextNode(exchng_rate));
							TransactionDate.appendChild(doc.createTextNode(trans_dt));
							JournalType.appendChild(doc.createTextNode(journal_type));
							TransactionReference.appendChild(doc.createTextNode(invoice_no));
							//DueDate.appendChild(doc.createTextNode(inv_due_dt));
							
							AccountingPeriod_d.appendChild(doc.createTextNode(account_period));
							GeneralDate4.appendChild(doc.createTextNode(trans_dt));
							TransactionDate_d.appendChild(doc.createTextNode(trans_dt));
							
							//AnalysisCode1
							AnalysisCode2.appendChild(doc.createTextNode(cccac));
							AnalysisCode3.appendChild(doc.createTextNode(ecac));
							AnalysisCode4.appendChild(doc.createTextNode(pur_account_cd));
							//AnalysisCode5
							//AnalysisCode6
							//AnalysisCode7.appendChild(doc.createTextNode("NA"));
							//AnalysisCode8
							AnalysisCode10.appendChild(doc.createTextNode(bucac));
							
							if(cont_type.equals("I")&&!igx_account_cd.equals(""))
							{
								Element Line1  = doc.createElement("Line");
								Element AccountCode1 = doc.createElement("AccountCode");
								Element AccountingPeriod1 = doc.createElement("AccountingPeriod");
								Element BaseAmount1 = doc.createElement("BaseAmount");
								Element DebitCredit1 = doc.createElement("DebitCredit");
								Element TransactionAmount1 = doc.createElement("TransactionAmount");
								Element Base2ReportingAmount1 = doc.createElement("Base2ReportingAmount");
								Element CurrencyCode1 = doc.createElement("CurrencyCode");
								Element CurrencyRate1 = doc.createElement("CurrencyRate");
								Element TransactionDate1 = doc.createElement("TransactionDate");
								Element JournalType1 = doc.createElement("JournalType");
								Element JournalSource1 = doc.createElement("JournalSource");			//Null in FMS8
								Element TransactionReference1 = doc.createElement("TransactionReference");
								Element Description1 = doc.createElement("Description");
								Element DueDate1 = doc.createElement("DueDate");
								Element DetailLad1 = doc.createElement("DetailLad");
								Element AnalysisCode1_1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
								Element AnalysisCode2_1 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
								Element AnalysisCode3_1 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
								Element AnalysisCode4_1 = doc.createElement("AnalysisCode4");			//COA_CD: 
								Element AnalysisCode5_1 = doc.createElement("AnalysisCode5");			//Null: in FMS8
								Element AnalysisCode6_1 = doc.createElement("AnalysisCode6");			
								Element AnalysisCode7_1 = doc.createElement("AnalysisCode7");			
								Element AnalysisCode8_1 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
								Element AnalysisCode10_1 = doc.createElement("AnalysisCode10");			//BU SUN account code 
								Element AccountCode_d1 = doc.createElement("AccountCode");
								Element AccountingPeriod_d1 = doc.createElement("AccountingPeriod");
								Element GeneralDate4_1 = doc.createElement("GeneralDate4");
								Element JournalLineNumber1 = doc.createElement("JournalLineNumber");
								Element JournalNumber1 = doc.createElement("JournalNumber");
								Element TransactionDate_d1=doc.createElement("TransactionDate");
								Element UserName1=doc.createElement("UserName");
								
								Ledger.appendChild(Line1);
								Line1.appendChild(AccountCode1);
								Line1.appendChild(AccountingPeriod1);
								Line1.appendChild(BaseAmount1);
								Line1.appendChild(DebitCredit1);
								Line1.appendChild(TransactionAmount1);
								Line1.appendChild(Base2ReportingAmount1);
								Line1.appendChild(CurrencyCode1);
								Line1.appendChild(CurrencyRate1);
								Line1.appendChild(TransactionDate1);
								Line1.appendChild(JournalType1);
								Line1.appendChild(JournalSource1);		//Null in FMS8
								Line1.appendChild(TransactionReference1);
								Line1.appendChild(Description1);
								Line1.appendChild(DueDate1);
								if(segment.equals("F")) 
								{
									Line1.appendChild(DetailLad1);
									DetailLad1.appendChild(AccountCode_d1);
									DetailLad1.appendChild(AccountingPeriod_d1);
									DetailLad1.appendChild(GeneralDate4_1);
									DetailLad1.appendChild(JournalLineNumber1);
									DetailLad1.appendChild(JournalNumber1);
									DetailLad1.appendChild(TransactionDate_d1);
									DetailLad1.appendChild(UserName1);
								}
								Line1.appendChild(AnalysisCode1_1);
								Line1.appendChild(AnalysisCode2_1);		
								Line1.appendChild(AnalysisCode3_1);		
								Line1.appendChild(AnalysisCode4_1);		
								Line1.appendChild(AnalysisCode5_1);		
								Line1.appendChild(AnalysisCode6_1);		
								Line1.appendChild(AnalysisCode7_1);		
								Line1.appendChild(AnalysisCode8_1);		
								Line1.appendChild(AnalysisCode10_1);
								
								AccountCode1.appendChild(doc.createTextNode(igx_account_cd));
								AccountingPeriod1.appendChild(doc.createTextNode(account_period));
								BaseAmount1.appendChild(doc.createTextNode(taxAmt));
								DebitCredit1.appendChild(doc.createTextNode("C"));
								TransactionAmount1.appendChild(doc.createTextNode(trans_amt));
								Base2ReportingAmount1.appendChild(doc.createTextNode(nf.format(0)));
								CurrencyCode1.appendChild(doc.createTextNode(currency_unit));
								CurrencyRate1.appendChild(doc.createTextNode(exchng_rate));
								TransactionDate1.appendChild(doc.createTextNode(trans_dt));
								JournalType1.appendChild(doc.createTextNode(journal_type));
								TransactionReference1.appendChild(doc.createTextNode(invoice_no));
								Description1.appendChild(doc.createTextNode(tax_description));
								//DueDate.appendChild(doc.createTextNode(inv_due_dt));
								
								AccountCode_d1.appendChild(doc.createTextNode(igx_account_cd));
								AccountingPeriod_d1.appendChild(doc.createTextNode(account_period));
								GeneralDate4_1.appendChild(doc.createTextNode(trans_dt));
								TransactionDate_d1.appendChild(doc.createTextNode(trans_dt));
								
								//AnalysisCode1
								AnalysisCode2_1.appendChild(doc.createTextNode(cccac));
								AnalysisCode3_1.appendChild(doc.createTextNode(ecac));
								AnalysisCode4_1.appendChild(doc.createTextNode(pur_account_cd));
								//AnalysisCode5
								//AnalysisCode6
								//AnalysisCode7.appendChild(doc.createTextNode("NA"));
								//AnalysisCode8
								AnalysisCode10_1.appendChild(doc.createTextNode(bucac));
							}
						}
						rset5.close();
						stmt5.close();
						
						//for credit line 
						String trans_amt=nf.format(Double.parseDouble(tax_amt)*-1);
						if(invoice_raised_in.equals("2") && !exchng_rate.equals(""))
						{
							tax_amt=nf.format(Double.parseDouble(tax_amt)*Double.parseDouble(exchng_rate));
						}
						tax_amt=nf.format(Double.parseDouble(tax_amt)*(-1));
						
						if(purchase_type_flag.equals("GTA_S")||purchase_type_flag.equals("GTA_P")||purchase_type_flag.equals("GTA_FF"))
						{
							trans_amt=nf.format(Double.parseDouble(trans_amt)*-1);
							tax_amt=nf.format(Double.parseDouble(tax_amt)*(-1));
						}
						
						if(invoice_type.equals("CR"))
						{
							acc_cd=pur_account_cd;
						}
						else
						{
							acc_cd=trader_sun_acc_cd;
						}
						
						Element Line  = doc.createElement("Line");
						Element AccountCode = doc.createElement("AccountCode");
						Element AccountingPeriod = doc.createElement("AccountingPeriod");
						Element BaseAmount = doc.createElement("BaseAmount");
						Element DebitCredit = doc.createElement("DebitCredit");
						Element TransactionAmount = doc.createElement("TransactionAmount");
						Element Base2ReportingAmount = doc.createElement("Base2ReportingAmount");
						Element CurrencyCode = doc.createElement("CurrencyCode");
						Element CurrencyRate = doc.createElement("CurrencyRate");
						Element TransactionDate = doc.createElement("TransactionDate");
						Element JournalType = doc.createElement("JournalType");
						Element JournalSource = doc.createElement("JournalSource");			//Null in FMS8
						Element TransactionReference = doc.createElement("TransactionReference");
						Element Description = doc.createElement("Description");
						Element DueDate = doc.createElement("DueDate");
						Element DetailLad = doc.createElement("DetailLad");
						Element AnalysisCode1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
						Element AnalysisCode2 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
						Element AnalysisCode3 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
						Element AnalysisCode4 = doc.createElement("AnalysisCode4");			//COA_CD: 
						Element AnalysisCode5 = doc.createElement("AnalysisCode5");			//Null: in FMS8
						Element AnalysisCode6 = doc.createElement("AnalysisCode6");			
						Element AnalysisCode7 = doc.createElement("AnalysisCode7");			
						Element AnalysisCode8 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
						Element AnalysisCode10 = doc.createElement("AnalysisCode10");			//BU SUN account code 
						
						Ledger.appendChild(Line);
						Line.appendChild(AccountCode);
						Line.appendChild(AccountingPeriod);
						Line.appendChild(BaseAmount);
						Line.appendChild(DebitCredit);
						Line.appendChild(TransactionAmount);
						Line.appendChild(Base2ReportingAmount);
						Line.appendChild(CurrencyCode);
						Line.appendChild(CurrencyRate);
						Line.appendChild(TransactionDate);
						Line.appendChild(JournalType);
						Line.appendChild(JournalSource);		//Null in FMS8
						Line.appendChild(TransactionReference);
						Line.appendChild(Description);
						Line.appendChild(DueDate);
						if(segment.equals("F")) 
						{
							Line.appendChild(DetailLad);
						}
						Line.appendChild(AnalysisCode1);
						Line.appendChild(AnalysisCode2);		
						Line.appendChild(AnalysisCode3);		
						Line.appendChild(AnalysisCode4);		
						Line.appendChild(AnalysisCode5);		
						Line.appendChild(AnalysisCode6);		
						Line.appendChild(AnalysisCode7);		
						Line.appendChild(AnalysisCode8);		
						Line.appendChild(AnalysisCode10);
						
						Element AccountCode_d = doc.createElement("AccountCode");
						Element AccountingPeriod_d = doc.createElement("AccountingPeriod");
						Element GeneralDate4 = doc.createElement("GeneralDate4");
						Element JournalLineNumber = doc.createElement("JournalLineNumber");
						Element JournalNumber = doc.createElement("JournalNumber");
						Element TransactionDate_d=doc.createElement("TransactionDate");
						Element UserName=doc.createElement("UserName");
						
						DetailLad.appendChild(AccountCode_d);
						DetailLad.appendChild(AccountingPeriod_d);
						DetailLad.appendChild(GeneralDate4);
						DetailLad.appendChild(JournalLineNumber);
						DetailLad.appendChild(JournalNumber);
						DetailLad.appendChild(TransactionDate_d);
						DetailLad.appendChild(UserName);
						
						if(purchase_type_flag.equals("GTA_S")||purchase_type_flag.equals("GTA_P")||purchase_type_flag.equals("GTA_FF"))
						{
							AccountCode.appendChild(doc.createTextNode(trader_sun_acc_cd));
							Description.appendChild(doc.createTextNode(description));
							AnalysisCode3.appendChild(doc.createTextNode(ecac));
						}
						else
						{
							AccountCode.appendChild(doc.createTextNode(acc_cd));
							Description.appendChild(doc.createTextNode(tax_description));
						}
						
						String dr_cr_marker=invoice_type.equals("DR")?"D":"C";
						
						AccountingPeriod.appendChild(doc.createTextNode(account_period));
						BaseAmount.appendChild(doc.createTextNode(tax_amt));
						//DebitCredit.appendChild(doc.createTextNode("C"));
						DebitCredit.appendChild(doc.createTextNode(dr_cr_marker));
						TransactionAmount.appendChild(doc.createTextNode(trans_amt));
						Base2ReportingAmount.appendChild(doc.createTextNode(nf.format(0)));
						CurrencyCode.appendChild(doc.createTextNode(currency_unit));
						CurrencyRate.appendChild(doc.createTextNode(exchng_rate));
						TransactionDate.appendChild(doc.createTextNode(trans_dt));
						JournalType.appendChild(doc.createTextNode(journal_type));
						TransactionReference.appendChild(doc.createTextNode(invoice_no));
						DueDate.appendChild(doc.createTextNode(inv_due_dt));
						
						AccountCode_d.appendChild(doc.createTextNode(acc_cd));
						AccountingPeriod_d.appendChild(doc.createTextNode(account_period));
						GeneralDate4.appendChild(doc.createTextNode(trans_dt));
						TransactionDate_d.appendChild(doc.createTextNode(trans_dt));
						
						//AnalysisCode1
						AnalysisCode2.appendChild(doc.createTextNode(cccac));
						//AnalysisCode3.appendChild(doc.createTextNode(ecac));
						AnalysisCode4.appendChild(doc.createTextNode(pur_account_cd));
						AnalysisCode5.appendChild(doc.createTextNode(analysis_cd5));
						//AnalysisCode6
						AnalysisCode7.appendChild(doc.createTextNode("NA"));
						//AnalysisCode8
						AnalysisCode10.appendChild(doc.createTextNode(bucac));
						
						//for igx settlement account 
						if(cont_type.equals("I")&&!igx_account_cd.equals(""))
						{
							Element Line1  = doc.createElement("Line");
							Element AccountCode1 = doc.createElement("AccountCode");
							Element AccountingPeriod1 = doc.createElement("AccountingPeriod");
							Element BaseAmount1 = doc.createElement("BaseAmount");
							Element DebitCredit1 = doc.createElement("DebitCredit");
							Element TransactionAmount1 = doc.createElement("TransactionAmount");
							Element Base2ReportingAmount1 = doc.createElement("Base2ReportingAmount");
							Element CurrencyCode1 = doc.createElement("CurrencyCode");
							Element CurrencyRate1 = doc.createElement("CurrencyRate");
							Element TransactionDate1 = doc.createElement("TransactionDate");
							Element JournalType1 = doc.createElement("JournalType");
							Element JournalSource1 = doc.createElement("JournalSource");			//Null in FMS8
							Element TransactionReference1 = doc.createElement("TransactionReference");
							Element Description1 = doc.createElement("Description");
							Element DueDate1 = doc.createElement("DueDate");
							Element DetailLad1 = doc.createElement("DetailLad");
							Element AnalysisCode1_1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
							Element AnalysisCode2_1 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
							Element AnalysisCode3_1 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
							Element AnalysisCode4_1 = doc.createElement("AnalysisCode4");			//COA_CD: 
							Element AnalysisCode5_1 = doc.createElement("AnalysisCode5");			//Null: in FMS8
							Element AnalysisCode6_1 = doc.createElement("AnalysisCode6");			
							Element AnalysisCode7_1 = doc.createElement("AnalysisCode7");			
							Element AnalysisCode8_1 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
							Element AnalysisCode10_1 = doc.createElement("AnalysisCode10");			//BU SUN account code 
							Element AccountCode_d1 = doc.createElement("AccountCode");
							Element AccountingPeriod_d1 = doc.createElement("AccountingPeriod");
							Element GeneralDate4_1 = doc.createElement("GeneralDate4");
							Element JournalLineNumber1 = doc.createElement("JournalLineNumber");
							Element JournalNumber1 = doc.createElement("JournalNumber");
							Element TransactionDate_d1=doc.createElement("TransactionDate");
							Element UserName1=doc.createElement("UserName");
							
							Ledger.appendChild(Line1);
							Line1.appendChild(AccountCode1);
							Line1.appendChild(AccountingPeriod1);
							Line1.appendChild(BaseAmount1);
							Line1.appendChild(DebitCredit1);
							Line1.appendChild(TransactionAmount1);
							Line1.appendChild(Base2ReportingAmount1);
							Line1.appendChild(CurrencyCode1);
							Line1.appendChild(CurrencyRate1);
							Line1.appendChild(TransactionDate1);
							Line1.appendChild(JournalType1);
							Line1.appendChild(JournalSource1);		//Null in FMS8
							Line1.appendChild(TransactionReference1);
							Line1.appendChild(Description1);
							Line1.appendChild(DueDate1);
							if(segment.equals("F")) 
							{
								Line1.appendChild(DetailLad1);
								DetailLad1.appendChild(AccountCode_d1);
								DetailLad1.appendChild(AccountingPeriod_d1);
								DetailLad1.appendChild(GeneralDate4_1);
								DetailLad1.appendChild(JournalLineNumber1);
								DetailLad1.appendChild(JournalNumber1);
								DetailLad1.appendChild(TransactionDate_d1);
								DetailLad1.appendChild(UserName1);
							}
							Line1.appendChild(AnalysisCode1_1);
							Line1.appendChild(AnalysisCode2_1);		
							Line1.appendChild(AnalysisCode3_1);		
							Line1.appendChild(AnalysisCode4_1);		
							Line1.appendChild(AnalysisCode5_1);		
							Line1.appendChild(AnalysisCode6_1);		
							Line1.appendChild(AnalysisCode7_1);		
							Line1.appendChild(AnalysisCode8_1);		
							Line1.appendChild(AnalysisCode10_1);
							
							AccountCode1.appendChild(doc.createTextNode(acc_cd));
							AccountingPeriod1.appendChild(doc.createTextNode(account_period));
							BaseAmount1.appendChild(doc.createTextNode(tax_amt));
							DebitCredit1.appendChild(doc.createTextNode("D"));
							TransactionAmount1.appendChild(doc.createTextNode(trans_amt));
							Base2ReportingAmount1.appendChild(doc.createTextNode(nf.format(0)));
							CurrencyCode1.appendChild(doc.createTextNode(currency_unit));
							CurrencyRate1.appendChild(doc.createTextNode(exchng_rate));
							TransactionDate1.appendChild(doc.createTextNode(trans_dt));
							JournalType1.appendChild(doc.createTextNode(journal_type));
							TransactionReference1.appendChild(doc.createTextNode(invoice_no));
							Description1.appendChild(doc.createTextNode(tax_description));
							DueDate1.appendChild(doc.createTextNode(inv_due_dt));
							
							AccountCode_d1.appendChild(doc.createTextNode(acc_cd));
							AccountingPeriod_d1.appendChild(doc.createTextNode(account_period));
							GeneralDate4_1.appendChild(doc.createTextNode(trans_dt));
							TransactionDate_d1.appendChild(doc.createTextNode(trans_dt));
							
							//AnalysisCode1
							AnalysisCode2_1.appendChild(doc.createTextNode(cccac));
							//AnalysisCode3.appendChild(doc.createTextNode(ecac));
							AnalysisCode4_1.appendChild(doc.createTextNode(pur_account_cd));
							AnalysisCode5_1.appendChild(doc.createTextNode(analysis_cd5));
							//AnalysisCode6
							AnalysisCode7_1.appendChild(doc.createTextNode("NA"));
							//AnalysisCode8
							AnalysisCode10_1.appendChild(doc.createTextNode(bucac));
						}
						
						
						if(!tcs_tds.equals(""))
						{
							if(tcs_tds.equals("TDS"))
							{
								if(!tds_amt.equals("") && Double.doubleToRawLongBits(Double.parseDouble(tds_amt))>Double.doubleToRawLongBits(0))
								{
									String mtn=utilDate.getMonthNameMON(period_st_dt);
									tcs_tds_description=utilBean.getCounterpartyABBR(conn, counterparty_cd)+" "+state_cd+" "+alloc_qty+" MMBTU @ "+sale_price+" "+billing_note+" "+mtn+" TDS";
									if(!exchng_rate.equals(""))
									{
										if(Double.doubleToRawLongBits(Double.parseDouble(exchng_rate))!=Double.doubleToRawLongBits(0))
										{
											tcs_tds_description+=" @"+exchng_rate;
										}
									}
									tcs_tds_description=tcs_tds_description.toUpperCase();
									for(int i=0;i<2;i++)
									{
										String tds_acc_cd=getTcsTdsStructureSunAccountCd(tdsStructCd, bu_state_tin, bu_unit).trim();
										String tdsAmt=tds_amt;
										String report_amt1=nf.format(0);
										String tds_amt_inr=tds_amt;
										if(invoice_raised_in.equals("2") && !exchng_rate.equals(""))
										{
											tds_amt_inr=nf.format(Double.parseDouble(tds_amt) * Double.parseDouble(exchng_rate));
										}
										else
										{
											if(!exchng_rate.equals(""))
											{
												if(Double.doubleToRawLongBits(Double.parseDouble(exchng_rate))!=Double.doubleToRawLongBits(0))
												{
													report_amt1=nf.format(Double.parseDouble(tdsAmt)/Double.parseDouble(exchng_rate));
												}
											}
										}
										
										Element Line1  = doc.createElement("Line");
										Element AccountCode1 = doc.createElement("AccountCode");
										Element AccountingPeriod1 = doc.createElement("AccountingPeriod");
										Element BaseAmount1 = doc.createElement("BaseAmount");
										Element DebitCredit1 = doc.createElement("DebitCredit");
										Element TransactionAmount1 = doc.createElement("TransactionAmount");
										Element Base2ReportingAmount1 = doc.createElement("Base2ReportingAmount");
										Element CurrencyCode1 = doc.createElement("CurrencyCode");
										Element CurrencyRate1 = doc.createElement("CurrencyRate");
										Element TransactionDate1 = doc.createElement("TransactionDate");
										Element JournalType1 = doc.createElement("JournalType");
										Element JournalSource1 = doc.createElement("JournalSource");			//Null in FMS8
										Element TransactionReference1 = doc.createElement("TransactionReference");
										Element Description1 = doc.createElement("Description");
										Element DueDate1 = doc.createElement("DueDate");
										Element DetailLad1 = doc.createElement("DetailLad");
										Element AnalysisCode1_1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
										Element AnalysisCode2_1 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
										Element AnalysisCode3_1 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
										Element AnalysisCode4_1 = doc.createElement("AnalysisCode4");			//COA_CD: 
										Element AnalysisCode5_1 = doc.createElement("AnalysisCode5");			//Null: in FMS8
										Element AnalysisCode6_1 = doc.createElement("AnalysisCode6");			
										Element AnalysisCode7_1 = doc.createElement("AnalysisCode7");			
										Element AnalysisCode8_1 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
										Element AnalysisCode10_1 = doc.createElement("AnalysisCode10");			//BU SUN account code 
										
										Ledger.appendChild(Line1);
										Line1.appendChild(AccountCode1);
										Line1.appendChild(AccountingPeriod1);
										Line1.appendChild(BaseAmount1);
										Line1.appendChild(DebitCredit1);
										Line1.appendChild(TransactionAmount1);
										Line1.appendChild(Base2ReportingAmount1);
										Line1.appendChild(CurrencyCode1);
										Line1.appendChild(CurrencyRate1);
										Line1.appendChild(TransactionDate1);
										Line1.appendChild(JournalType1);
										Line1.appendChild(JournalSource1);		//Null in FMS8
										Line1.appendChild(TransactionReference1);
										Line1.appendChild(Description1);
										Line1.appendChild(DueDate1);
										if(segment.equals("F")) 
										{
											Line1.appendChild(DetailLad1);
										}
										Line1.appendChild(AnalysisCode1_1);
										Line1.appendChild(AnalysisCode2_1);		
										Line1.appendChild(AnalysisCode3_1);		
										Line1.appendChild(AnalysisCode4_1);		
										Line1.appendChild(AnalysisCode5_1);		
										Line1.appendChild(AnalysisCode6_1);		
										Line1.appendChild(AnalysisCode7_1);		
										Line1.appendChild(AnalysisCode8_1);		
										Line1.appendChild(AnalysisCode10_1);
										
										Element AccountCode_d_1 = doc.createElement("AccountCode");
										Element AccountingPeriod_d_1 = doc.createElement("AccountingPeriod");
										Element GeneralDate4_1 = doc.createElement("GeneralDate4");
										Element JournalLineNumber_1 = doc.createElement("JournalLineNumber");
										Element JournalNumber_1 = doc.createElement("JournalNumber");
										Element TransactionDate_d_1=doc.createElement("TransactionDate");
										Element UserName_1=doc.createElement("UserName");
										
										DetailLad1.appendChild(AccountCode_d_1);
										DetailLad1.appendChild(AccountingPeriod_d_1);
										DetailLad1.appendChild(GeneralDate4_1);
										DetailLad1.appendChild(JournalLineNumber_1);
										DetailLad1.appendChild(JournalNumber_1);
										DetailLad1.appendChild(TransactionDate_d_1);
										DetailLad1.appendChild(UserName_1);
										
										if(i==0)
										{
											if(invoice_type.equals("CR"))
											{
												acc_cd=tds_acc_cd;
											}
											else
											{
												acc_cd=trader_sun_acc_cd;
											}
											dr_cr_marker=invoice_type.equals("DR")?"C":"D";
											AccountCode1.appendChild(doc.createTextNode(acc_cd));
											//DebitCredit1.appendChild(doc.createTextNode("D"));
											DebitCredit1.appendChild(doc.createTextNode(dr_cr_marker));
											AnalysisCode3_1.appendChild(doc.createTextNode(ecac));
											AnalysisCode5_1.appendChild(doc.createTextNode(analysis_cd5));
											
											AccountCode_d_1.appendChild(doc.createTextNode(acc_cd));
											if(purchase_type_flag.equals("GTA_S")||purchase_type_flag.equals("GTA_P")||purchase_type_flag.equals("GTA_FF"))
											{
												DueDate1.appendChild(doc.createTextNode(inv_due_dt));
											}
										}
										else
										{
											if(!purchase_type_flag.equals("GTA_S")&&!purchase_type_flag.equals("GTA_P")&&!purchase_type_flag.equals("GTA_FF"))
											{
												tdsAmt=nf.format(Double.parseDouble(tdsAmt)*-1);
												tds_amt=nf.format(Double.parseDouble(tds_amt)*-1);
												tds_amt_inr=nf.format(Double.parseDouble(tds_amt_inr)*-1);
												//report_amt1=nf.format(Double.parseDouble(report_amt1)*-1);
												report_amt1=Double.doubleToRawLongBits(Double.parseDouble(report_amt1))==Double.doubleToRawLongBits(0)?nf.format(0):nf.format(Double.parseDouble(report_amt1)*(-1));
											}
											else
											{
												AnalysisCode3_1.appendChild(doc.createTextNode(ecac));
											}
											if(invoice_type.equals("CR"))
											{
												acc_cd=trader_sun_acc_cd;
											}
											else
											{
												acc_cd=tds_acc_cd;
											}
											dr_cr_marker=invoice_type.equals("DR")?"D":"C";
											AccountCode1.appendChild(doc.createTextNode(acc_cd));
											//DebitCredit1.appendChild(doc.createTextNode("C"));
											DebitCredit1.appendChild(doc.createTextNode(dr_cr_marker));
											DueDate1.appendChild(doc.createTextNode(inv_due_dt));
											
											AccountCode_d_1.appendChild(doc.createTextNode(acc_cd));
										}
										
										AccountingPeriod1.appendChild(doc.createTextNode(account_period));
										BaseAmount1.appendChild(doc.createTextNode(tds_amt_inr));
										TransactionAmount1.appendChild(doc.createTextNode(tdsAmt));
										Base2ReportingAmount1.appendChild(doc.createTextNode(report_amt1));
										CurrencyCode1.appendChild(doc.createTextNode(currency_unit));
										CurrencyRate1.appendChild(doc.createTextNode(exchng_rate));
										TransactionDate1.appendChild(doc.createTextNode(trans_dt));
										JournalType1.appendChild(doc.createTextNode(journal_type));
										TransactionReference1.appendChild(doc.createTextNode(invoice_no));
										if(purchase_type_flag.equals("GTA_S")||purchase_type_flag.equals("GTA_P")||purchase_type_flag.equals("GTA_FF"))
										{
											Description1.appendChild(doc.createTextNode(description));
										}
										else
										{
											Description1.appendChild(doc.createTextNode(tcs_tds_description));
										}
										
										AccountingPeriod_d_1.appendChild(doc.createTextNode(account_period));
										GeneralDate4_1.appendChild(doc.createTextNode(trans_dt));
										TransactionDate_d_1.appendChild(doc.createTextNode(trans_dt));
										
										//AnalysisCode1_1
										AnalysisCode2_1.appendChild(doc.createTextNode(cccac));
										AnalysisCode4_1.appendChild(doc.createTextNode(pur_account_cd));
										//AnalysisCode6_1
										AnalysisCode7_1.appendChild(doc.createTextNode(code));
										//AnalysisCode8_1
										AnalysisCode10_1.appendChild(doc.createTextNode(bucac));
									}
								}
							}
							else if(tcs_tds.equals("TCS"))
							{
								String mtn=utilDate.getMonthNameMON(period_st_dt);
								tcs_tds_description=utilBean.getCounterpartyABBR(conn, counterparty_cd)+" "+state_cd+" "+alloc_qty+" MMBTU @ "+sale_price+" "+billing_note+" "+mtn+" TCS";
								if(!exchng_rate.equals(""))
								{
									if(Double.doubleToRawLongBits(Double.parseDouble(exchng_rate))!=Double.doubleToRawLongBits(0))
									{
										tcs_tds_description+=" @"+exchng_rate;
									}
								}
								tcs_tds_description=tcs_tds_description.toUpperCase();
								for(int i=0;i<2;i++)
								{
									String tcs_acc_cd=getTcsTdsStructureSunAccountCd(tcsStructCd, bu_state_tin, bu_unit).trim();
									String tcsAmt=tcs_amt;
									String report_amt1=nf.format(0);
									String tcs_amt_inr=tcs_amt;
									if(invoice_raised_in.equals("2") && !exchng_rate.equals(""))
									{
										tcs_amt_inr=nf.format(Double.parseDouble(tcs_amt) * Double.parseDouble(exchng_rate));
									}
									else
									{
										if(!exchng_rate.equals(""))
										{
											if(Double.doubleToRawLongBits(Double.parseDouble(exchng_rate))!=Double.doubleToRawLongBits(0))
											{
												report_amt1=nf.format(Double.parseDouble(tcsAmt)/Double.parseDouble(exchng_rate));
											}
										}
									}
									
									Element Line1  = doc.createElement("Line");
									Element AccountCode1 = doc.createElement("AccountCode");
									Element AccountingPeriod1 = doc.createElement("AccountingPeriod");
									Element BaseAmount1 = doc.createElement("BaseAmount");
									Element DebitCredit1 = doc.createElement("DebitCredit");
									Element TransactionAmount1 = doc.createElement("TransactionAmount");
									Element Base2ReportingAmount1 = doc.createElement("Base2ReportingAmount");
									Element CurrencyCode1 = doc.createElement("CurrencyCode");
									Element CurrencyRate1 = doc.createElement("CurrencyRate");
									Element TransactionDate1 = doc.createElement("TransactionDate");
									Element JournalType1 = doc.createElement("JournalType");
									Element JournalSource1 = doc.createElement("JournalSource");			//Null in FMS8
									Element TransactionReference1 = doc.createElement("TransactionReference");
									Element Description1 = doc.createElement("Description");
									Element DueDate1 = doc.createElement("DueDate");
									Element DetailLad1 = doc.createElement("DetailLad");
									Element AnalysisCode1_1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
									Element AnalysisCode2_1 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
									Element AnalysisCode3_1 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
									Element AnalysisCode4_1 = doc.createElement("AnalysisCode4");			//COA_CD: 
									Element AnalysisCode5_1 = doc.createElement("AnalysisCode5");			//Null: in FMS8
									Element AnalysisCode6_1 = doc.createElement("AnalysisCode6");			
									Element AnalysisCode7_1 = doc.createElement("AnalysisCode7");			
									Element AnalysisCode8_1 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
									Element AnalysisCode10_1 = doc.createElement("AnalysisCode10");			//BU SUN account code 
									
									Ledger.appendChild(Line1);
									Line1.appendChild(AccountCode1);
									Line1.appendChild(AccountingPeriod1);
									Line1.appendChild(BaseAmount1);
									Line1.appendChild(DebitCredit1);
									Line1.appendChild(TransactionAmount1);
									Line1.appendChild(Base2ReportingAmount1);
									Line1.appendChild(CurrencyCode1);
									Line1.appendChild(CurrencyRate1);
									Line1.appendChild(TransactionDate1);
									Line1.appendChild(JournalType1);
									Line1.appendChild(JournalSource1);		//Null in FMS8
									Line1.appendChild(TransactionReference1);
									Line1.appendChild(Description1);
									Line1.appendChild(DueDate1);
									if(segment.equals("F")) 
									{
										Line1.appendChild(DetailLad1);
									}
									Line1.appendChild(AnalysisCode1_1);
									Line1.appendChild(AnalysisCode2_1);		
									Line1.appendChild(AnalysisCode3_1);		
									Line1.appendChild(AnalysisCode4_1);		
									Line1.appendChild(AnalysisCode5_1);		
									Line1.appendChild(AnalysisCode6_1);		
									Line1.appendChild(AnalysisCode7_1);		
									Line1.appendChild(AnalysisCode8_1);		
									Line1.appendChild(AnalysisCode10_1);
									
									Element AccountCode_d_1 = doc.createElement("AccountCode");
									Element AccountingPeriod_d_1 = doc.createElement("AccountingPeriod");
									Element GeneralDate4_1 = doc.createElement("GeneralDate4");
									Element JournalLineNumber_1 = doc.createElement("JournalLineNumber");
									Element JournalNumber_1 = doc.createElement("JournalNumber");
									Element TransactionDate_d_1=doc.createElement("TransactionDate");
									Element UserName_1=doc.createElement("UserName");
									
									DetailLad1.appendChild(AccountCode_d_1);
									DetailLad1.appendChild(AccountingPeriod_d_1);
									DetailLad1.appendChild(GeneralDate4_1);
									DetailLad1.appendChild(JournalLineNumber_1);
									DetailLad1.appendChild(JournalNumber_1);
									DetailLad1.appendChild(TransactionDate_d_1);
									DetailLad1.appendChild(UserName_1);
									
									if(i==0)
									{
										/*tcsAmt=nf.format(Double.parseDouble(tcsAmt)*-1);
									tcs_amt=nf.format(Double.parseDouble(tcs_amt)*-1);
									tcs_amt_inr=nf.format(Double.parseDouble(tcs_amt_inr)*-1);
									//report_amt1=nf.format(Double.parseDouble(report_amt1)*-1);
									report_amt1=Double.parseDouble(report_amt1)==0?nf.format(0):nf.format(Double.parseDouble(report_amt1)*(-1));*/
										
										AccountCode1.appendChild(doc.createTextNode(trader_sun_acc_cd));
										DebitCredit1.appendChild(doc.createTextNode("C"));
										AnalysisCode3_1.appendChild(doc.createTextNode(ecac));
										
										AccountCode_d_1.appendChild(doc.createTextNode(trader_sun_acc_cd));
									}
									else
									{
										tcsAmt=nf.format(Double.parseDouble(tcsAmt)*-1);
										tcs_amt=nf.format(Double.parseDouble(tcs_amt)*-1);
										tcs_amt_inr=nf.format(Double.parseDouble(tcs_amt_inr)*-1);
										//report_amt1=nf.format(Double.parseDouble(report_amt1)*-1);
										report_amt1=Double.doubleToRawLongBits(Double.parseDouble(report_amt1))==Double.doubleToRawLongBits(0)?nf.format(0):nf.format(Double.parseDouble(report_amt1)*(-1));
										
										AccountCode1.appendChild(doc.createTextNode(tcs_acc_cd));
										DebitCredit1.appendChild(doc.createTextNode("D"));
										DueDate1.appendChild(doc.createTextNode(inv_due_dt));
										
										AccountCode_d_1.appendChild(doc.createTextNode(tcs_acc_cd));
									}
									
									AccountingPeriod1.appendChild(doc.createTextNode(account_period));
									BaseAmount1.appendChild(doc.createTextNode(tcs_amt_inr));
									TransactionAmount1.appendChild(doc.createTextNode(tcsAmt));
									Base2ReportingAmount1.appendChild(doc.createTextNode(report_amt1));
									CurrencyCode1.appendChild(doc.createTextNode(currency_unit));
									CurrencyRate1.appendChild(doc.createTextNode(exchng_rate));
									TransactionDate1.appendChild(doc.createTextNode(trans_dt));
									JournalType1.appendChild(doc.createTextNode(journal_type));
									TransactionReference1.appendChild(doc.createTextNode(invoice_no));
									Description1.appendChild(doc.createTextNode(tcs_tds_description));
									
									AccountingPeriod_d_1.appendChild(doc.createTextNode(account_period));
									GeneralDate4_1.appendChild(doc.createTextNode(trans_dt));
									TransactionDate_d_1.appendChild(doc.createTextNode(trans_dt));
									
									//AnalysisCode1_1
									AnalysisCode2_1.appendChild(doc.createTextNode(cccac));
									AnalysisCode4_1.appendChild(doc.createTextNode(pur_account_cd));
									//AnalysisCode5_1
									//AnalysisCode6_1
									AnalysisCode7_1.appendChild(doc.createTextNode(code));
									//AnalysisCode8_1
									AnalysisCode10_1.appendChild(doc.createTextNode(bucac));
									
									//for igx settlement account 
									if(cont_type.equals("I")&&!igx_account_cd.equals(""))
									{
										Element Line2  = doc.createElement("Line");
										Element AccountCode2 = doc.createElement("AccountCode");
										Element AccountingPeriod2 = doc.createElement("AccountingPeriod");
										Element BaseAmount2 = doc.createElement("BaseAmount");
										Element DebitCredit2 = doc.createElement("DebitCredit");
										Element TransactionAmount2 = doc.createElement("TransactionAmount");
										Element Base2ReportingAmount2 = doc.createElement("Base2ReportingAmount");
										Element CurrencyCode2 = doc.createElement("CurrencyCode");
										Element CurrencyRate2 = doc.createElement("CurrencyRate");
										Element TransactionDate2 = doc.createElement("TransactionDate");
										Element JournalType2 = doc.createElement("JournalType");
										Element JournalSource2 = doc.createElement("JournalSource");			//Null in FMS8
										Element TransactionReference2 = doc.createElement("TransactionReference");
										Element Description2 = doc.createElement("Description");
										Element DueDate2 = doc.createElement("DueDate");
										Element DetailLad2 = doc.createElement("DetailLad");
										Element AnalysisCode1_2 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
										Element AnalysisCode2_2 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
										Element AnalysisCode3_2 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
										Element AnalysisCode4_2 = doc.createElement("AnalysisCode4");			//COA_CD: 
										Element AnalysisCode5_2 = doc.createElement("AnalysisCode5");			//Null: in FMS8
										Element AnalysisCode6_2 = doc.createElement("AnalysisCode6");			
										Element AnalysisCode7_2 = doc.createElement("AnalysisCode7");			
										Element AnalysisCode8_2 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
										Element AnalysisCode10_2 = doc.createElement("AnalysisCode10");			//BU SUN account code 
										Element AccountCode_d1 = doc.createElement("AccountCode");
										Element AccountingPeriod_d1 = doc.createElement("AccountingPeriod");
										Element GeneralDate4_2 = doc.createElement("GeneralDate4");
										Element JournalLineNumber1 = doc.createElement("JournalLineNumber");
										Element JournalNumber1 = doc.createElement("JournalNumber");
										Element TransactionDate_d1=doc.createElement("TransactionDate");
										Element UserName1=doc.createElement("UserName");
										
										Ledger.appendChild(Line2);
										Line2.appendChild(AccountCode2);
										Line2.appendChild(AccountingPeriod2);
										Line2.appendChild(BaseAmount2);
										Line2.appendChild(DebitCredit2);
										Line2.appendChild(TransactionAmount2);
										Line2.appendChild(Base2ReportingAmount2);
										Line2.appendChild(CurrencyCode2);
										Line2.appendChild(CurrencyRate2);
										Line2.appendChild(TransactionDate2);
										Line2.appendChild(JournalType2);
										Line2.appendChild(JournalSource2);		//Null in FMS8
										Line2.appendChild(TransactionReference2);
										Line2.appendChild(Description2);
										Line2.appendChild(DueDate2);
										if(segment.equals("F")) 
										{
											Line1.appendChild(DetailLad2);
											DetailLad2.appendChild(AccountCode_d1);
											DetailLad2.appendChild(AccountingPeriod_d1);
											DetailLad2.appendChild(GeneralDate4_2);
											DetailLad2.appendChild(JournalLineNumber1);
											DetailLad2.appendChild(JournalNumber1);
											DetailLad2.appendChild(TransactionDate_d1);
											DetailLad2.appendChild(UserName1);
										}
										Line2.appendChild(AnalysisCode1_2);
										Line2.appendChild(AnalysisCode2_2);		
										Line2.appendChild(AnalysisCode3_2);		
										Line2.appendChild(AnalysisCode4_2);		
										Line2.appendChild(AnalysisCode5_2);		
										Line2.appendChild(AnalysisCode6_2);		
										Line2.appendChild(AnalysisCode7_2);		
										Line2.appendChild(AnalysisCode8_2);		
										Line2.appendChild(AnalysisCode10_2);
										
										if(i==0)
										{
											AccountCode2.appendChild(doc.createTextNode(igx_account_cd));
											DebitCredit2.appendChild(doc.createTextNode("C"));
											AnalysisCode3_2.appendChild(doc.createTextNode(ecac));
											
											AccountCode_d1.appendChild(doc.createTextNode(igx_account_cd));
										}
										else
										{
											AccountCode2.appendChild(doc.createTextNode(trader_sun_acc_cd));
											DebitCredit2.appendChild(doc.createTextNode("D"));
											DueDate2.appendChild(doc.createTextNode(inv_due_dt));
											
											AccountCode_d1.appendChild(doc.createTextNode(trader_sun_acc_cd));
										}
										
										AccountingPeriod2.appendChild(doc.createTextNode(account_period));
										BaseAmount2.appendChild(doc.createTextNode(tcs_amt_inr));
										TransactionAmount2.appendChild(doc.createTextNode(tcsAmt));
										Base2ReportingAmount2.appendChild(doc.createTextNode(report_amt1));
										CurrencyCode2.appendChild(doc.createTextNode(currency_unit));
										CurrencyRate2.appendChild(doc.createTextNode(exchng_rate));
										TransactionDate2.appendChild(doc.createTextNode(trans_dt));
										JournalType2.appendChild(doc.createTextNode(journal_type));
										TransactionReference2.appendChild(doc.createTextNode(invoice_no));
										Description2.appendChild(doc.createTextNode(tcs_tds_description));
										
										AccountingPeriod_d1.appendChild(doc.createTextNode(account_period));
										GeneralDate4_2.appendChild(doc.createTextNode(trans_dt));
										TransactionDate_d1.appendChild(doc.createTextNode(trans_dt));
										
										//AnalysisCode1_1
										AnalysisCode2_2.appendChild(doc.createTextNode(cccac));
										AnalysisCode4_2.appendChild(doc.createTextNode(pur_account_cd));
										//AnalysisCode5_1
										//AnalysisCode6_1
										AnalysisCode7_2.appendChild(doc.createTextNode("VCQ3"));
										//AnalysisCode8_1
										AnalysisCode10_2.appendChild(doc.createTextNode(bucac));
									}
								}
								
							}
						}
					}
					
					//for custom duty 
					if(segment.equals("CF"))
					{
						if(Double.doubleToRawLongBits(Double.parseDouble(net_payable))>=Double.doubleToRawLongBits(0))
						{
							//i=0: 	final_custom_amt
							//i=1: 	interest amount
							//i=2:  base amount credit line 
							//i=3: 	Provisional amount debit line 		//20250728: AS PER ARUN INSTRUCTION OF ADDING ADDITIONAL LINE FOR PROVISIONAL AMOUNT
							//i=4: 	Provisional amount credit line 		//20250728: AS PER ARUN INSTRUCTION OF ADDING ADDITIONAL LINE FOR PROVISIONAL AMOUNT
							for(int i=0;i<5;i++)
							{
								Element Line  = doc.createElement("Line");
								Element AccountCode = doc.createElement("AccountCode");
								Element AccountingPeriod = doc.createElement("AccountingPeriod");
								Element BaseAmount = doc.createElement("BaseAmount");
								Element DebitCredit = doc.createElement("DebitCredit");
								Element TransactionAmount = doc.createElement("TransactionAmount");
								Element Base2ReportingAmount = doc.createElement("Base2ReportingAmount");
								Element CurrencyCode = doc.createElement("CurrencyCode");
								Element CurrencyRate = doc.createElement("CurrencyRate");
								Element TransactionDate = doc.createElement("TransactionDate");
								Element JournalType = doc.createElement("JournalType");
								Element JournalSource = doc.createElement("JournalSource");			//Null in FMS8
								Element TransactionReference = doc.createElement("TransactionReference");
								Element Description = doc.createElement("Description");
								Element DueDate = doc.createElement("DueDate");
								Element AnalysisCode1 = doc.createElement("AnalysisCode1");			//PROJECT_CD: NULL for Sales XML
								Element AnalysisCode2 = doc.createElement("AnalysisCode2");			//COST_CENTER_CD:
								Element AnalysisCode3 = doc.createElement("AnalysisCode3");			//EMPLOYEE_CD: NULL for Sales XML
								Element AnalysisCode4 = doc.createElement("AnalysisCode4");			//COA_CD: 
								Element AnalysisCode5 = doc.createElement("AnalysisCode5");			//Null: in FMS8
								Element AnalysisCode6 = doc.createElement("AnalysisCode6");			
								Element AnalysisCode7 = doc.createElement("AnalysisCode7");			
								Element AnalysisCode8 = doc.createElement("AnalysisCode8");			//NULL: in FMS8
								Element AnalysisCode10 = doc.createElement("AnalysisCode10");			//BU SUN account code 
								
								Ledger.appendChild(Line);
								Line.appendChild(AccountCode);
								Line.appendChild(AccountingPeriod);
								Line.appendChild(BaseAmount);
								Line.appendChild(DebitCredit);
								Line.appendChild(TransactionAmount);
								Line.appendChild(Base2ReportingAmount);
								Line.appendChild(CurrencyCode);
								Line.appendChild(CurrencyRate);
								Line.appendChild(TransactionDate);
								Line.appendChild(JournalType);
								Line.appendChild(JournalSource);		//Null in FMS8
								Line.appendChild(TransactionReference);
								Line.appendChild(Description);
								Line.appendChild(DueDate);
								Line.appendChild(AnalysisCode1);
								Line.appendChild(AnalysisCode2);		
								Line.appendChild(AnalysisCode3);		
								Line.appendChild(AnalysisCode4);		
								Line.appendChild(AnalysisCode5);		
								Line.appendChild(AnalysisCode6);		
								Line.appendChild(AnalysisCode7);		
								Line.appendChild(AnalysisCode8);		
								Line.appendChild(AnalysisCode10);
								
								if(i==0)
								{
									AccountCode.appendChild(doc.createTextNode(custom_account_cd));
									BaseAmount.appendChild(doc.createTextNode(final_custom_amt));
									DebitCredit.appendChild(doc.createTextNode("D"));
									TransactionAmount.appendChild(doc.createTextNode(final_custom_amt));
									AnalysisCode3.appendChild(doc.createTextNode(ecac));
									AnalysisCode4.appendChild(doc.createTextNode(custom_duty_tax_cd));
								}
								else if(i==1)
								{
									AccountCode.appendChild(doc.createTextNode(miscellaneous_ac_code));
									BaseAmount.appendChild(doc.createTextNode(nf.format(intrest_amt)));
									DebitCredit.appendChild(doc.createTextNode("D"));
									TransactionAmount.appendChild(doc.createTextNode(nf.format(intrest_amt)));
									AnalysisCode3.appendChild(doc.createTextNode(ecac));
									AnalysisCode4.appendChild(doc.createTextNode(custom_duty_tax_cd));
								}
								else if(i==2)
								{
									AccountCode.appendChild(doc.createTextNode(custom_duty_tax_cd));
									BaseAmount.appendChild(doc.createTextNode(base_amt));
									DebitCredit.appendChild(doc.createTextNode("C"));
									TransactionAmount.appendChild(doc.createTextNode(base_amt));
									AnalysisCode4.appendChild(doc.createTextNode(custom_account_cd));
								}
								else if(i==3)
								{
									AccountCode.appendChild(doc.createTextNode(custom_account_cd));
									BaseAmount.appendChild(doc.createTextNode(cd_paid_amt));
									DebitCredit.appendChild(doc.createTextNode("D"));
									TransactionAmount.appendChild(doc.createTextNode(cd_paid_amt));
									AnalysisCode3.appendChild(doc.createTextNode(ecac));
									AnalysisCode4.appendChild(doc.createTextNode(custom_duty_tax_cd));
								}
								else if(i==4)
								{
									AccountCode.appendChild(doc.createTextNode(custom_duty_tax_cd));
									BaseAmount.appendChild(doc.createTextNode(cd_paid_amt));
									DebitCredit.appendChild(doc.createTextNode("C"));
									TransactionAmount.appendChild(doc.createTextNode(cd_paid_amt));
									AnalysisCode4.appendChild(doc.createTextNode(custom_account_cd));
								}
								
								Base2ReportingAmount.appendChild(doc.createTextNode(nf.format(0)));
								AccountingPeriod.appendChild(doc.createTextNode(account_period));
								CurrencyCode.appendChild(doc.createTextNode(currency_unit));
								CurrencyRate.appendChild(doc.createTextNode(exchng_rate));
								TransactionDate.appendChild(doc.createTextNode(trans_dt));
								JournalType.appendChild(doc.createTextNode(journal_type));
								TransactionReference.appendChild(doc.createTextNode(invoice_no));
								Description.appendChild(doc.createTextNode(description));
								AnalysisCode2.appendChild(doc.createTextNode(cccac));
								AnalysisCode10.appendChild(doc.createTextNode(bucac));
							}
						}
					}
				}
				
				if(!sysdate.equals(""))
				{
					String appPath = request.getServletContext().getRealPath("");
					
					String main_folder="";
					if(!comp_cd.equals(""))
					{
						main_folder=CommonVariable.work_dir+comp_cd;
					}
					File MainDir = new File(appPath+File.separator+main_folder);
					if(!MainDir.exists()) 
					{
						MainDir.mkdir();
					}
					String sub_folder=""+CommonVariable.sun_xml;
					
					File SubDir = new File(appPath+File.separator+main_folder+File.separator+sub_folder);
					if(!SubDir.exists()) 
					{
						SubDir.mkdir();
					}
					File temp [] = new File(appPath+File.separator+main_folder+File.separator+sub_folder).listFiles();
					
					//int count1=1;
					xmlFileNm=utilBean.getCompanyAbbr(conn, comp_cd)+"_"+sysdate+".xml";
					for(File filename : temp)
					{
						if(filename.getName().equals(xmlFileNm))
						{
							xml_generate_flag=xml_generate_flag && false;
						}
					}
					//xmlFileNm=utilBean.getCompanyAbbr(conn, comp_cd)+"_"+sysdate+"_"+count1+".xml";
					
					if(xml_generate_flag)
					{
						if(purchase_type_flag.equals("FF"))
						{
							int count=0;
							String query3="SELECT COUNT(*) "
									+ "FROM FMS_PUR_FFLOW_INV_FILE_DTL "
									+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
									+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_TYPE=? AND INV_TITLE=?";
							stmt1=conn.prepareStatement(query3);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, cont_type);
							stmt1.setString(3, invoice_seq);
							stmt1.setString(4, fin_year);
							stmt1.setString(5, invoice_type);
							stmt1.setString(6, "S");
							rset1=stmt1.executeQuery();
							if(rset1.next())
							{
								count=rset1.getInt(1);
							}
							rset1.close();
							stmt1.close();
							
							if(count > 0)
							{
								String queryString1="UPDATE FMS_PUR_FFLOW_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
										+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? "
										+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=?";
								stmt1=conn.prepareStatement(queryString1);
								stmt1.setString(1, xmlFileNm);
								stmt1.setString(2, emp_cd);
								stmt1.setString(3, comp_cd);
								stmt1.setString(4, cont_type);
								stmt1.setString(5, invoice_type);
								stmt1.setString(6, invoice_seq);
								stmt1.setString(7, fin_year);
								stmt1.setString(8, "S");
								stmt1.executeUpdate();
								
								stmt1.close();
							}
							else
							{
								String queryString1="INSERT INTO FMS_PUR_FFLOW_INV_FILE_DTL(COMPANY_CD,CONTRACT_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,INV_TITLE,"
										+ "FILE_NAME,ENT_BY,ENT_DT,INVOICE_TYPE) "
										+ "VALUES(?,?,?,?,?,"
										+ "?,?,SYSDATE,?)";
								stmt1=conn.prepareStatement(queryString1);
								stmt1.setString(1, comp_cd);
								stmt1.setString(2, cont_type);
								stmt1.setString(3, invoice_seq);
								stmt1.setString(4, fin_year);
								stmt1.setString(5, "S");
								stmt1.setString(6, xmlFileNm);
								stmt1.setString(7, emp_cd);
								stmt1.setString(8, invoice_type);
								stmt1.executeUpdate();
								
								stmt1.close();
							}
							conn.commit();
						}
						else if(purchase_type_flag.equals("GTA_FF"))
						{
							int count=0;
							String query3="SELECT COUNT(*) "
									+ "FROM FMS_GTA_FFLOW_INV_FILE_DTL "
									+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
									+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=? AND INVOICE_TYPE=?";
							stmt3=conn.prepareStatement(query3);
							stmt3.setString(1, comp_cd);
							stmt3.setString(2, cont_type);
							stmt3.setString(3, invoice_seq);
							stmt3.setString(4, fin_year);
							stmt3.setString(5, "S");
							stmt3.setString(6, invoice_type);
							rset3=stmt3.executeQuery();
							if(rset3.next())
							{
								count=rset3.getInt(1);
							}
							rset3.close();
							stmt3.close();
							
							if(count > 0)
							{
								String queryString1="UPDATE FMS_GTA_FFLOW_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
										+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? "
										+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=?";
								stmt1=conn.prepareStatement(queryString1);
								stmt1.setString(1, xmlFileNm);
								stmt1.setString(2, emp_cd);
								stmt1.setString(3, comp_cd);
								stmt1.setString(4, cont_type);
								stmt1.setString(5, invoice_type);
								stmt1.setString(6, invoice_seq);
								stmt1.setString(7, fin_year);
								stmt1.setString(8, "S");
								stmt1.executeUpdate();
								
								stmt1.close();
							}
							else
							{
								String queryString1="INSERT INTO FMS_GTA_FFLOW_INV_FILE_DTL(COMPANY_CD,CONTRACT_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,INV_TITLE,"
										+ "FILE_NAME,ENT_BY,ENT_DT,INVOICE_TYPE) "
										+ "VALUES(?,?,?,?,?,"
										+ "?,?,SYSDATE,?)";
								stmt1=conn.prepareStatement(queryString1);
								stmt1.setString(1, comp_cd);
								stmt1.setString(2, cont_type);
								stmt1.setString(3, invoice_seq);
								stmt1.setString(4, fin_year);
								stmt1.setString(5, "S");
								stmt1.setString(6, xmlFileNm);
								stmt1.setString(7, emp_cd);
								stmt1.setString(8, invoice_type);
								stmt1.executeUpdate();
								
								stmt1.close();
							}
							conn.commit();
						}
						else if(purchase_type_flag.equals("GTA_S")||purchase_type_flag.equals("GTA_P"))
						{
							int count=0;
							String query3="SELECT COUNT(*) "
									+ "FROM FMS_GTA_INV_FILE_DTL "
									+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
									+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=? AND INVOICE_TYPE=?";
							stmt3=conn.prepareStatement(query3);
							stmt3.setString(1, comp_cd);
							stmt3.setString(2, cont_type);
							stmt3.setString(3, invoice_seq);
							stmt3.setString(4, fin_year);
							stmt3.setString(5, "S");
							stmt3.setString(6, invoice_type);
							rset3=stmt3.executeQuery();
							if(rset3.next())
							{
								count=rset3.getInt(1);
							}
							rset3.close();
							stmt3.close();
							
							if(count > 0)
							{
								String queryString1="UPDATE FMS_GTA_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
										+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? "
										+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=?";
								stmt1=conn.prepareStatement(queryString1);
								stmt1.setString(1, xmlFileNm);
								stmt1.setString(2, emp_cd);
								stmt1.setString(3, comp_cd);
								stmt1.setString(4, cont_type);
								stmt1.setString(5, invoice_type);
								stmt1.setString(6, invoice_seq);
								stmt1.setString(7, fin_year);
								stmt1.setString(8, "S");
								stmt1.executeUpdate();
								
								stmt1.close();
							}
							else
							{
								String queryString1="INSERT INTO FMS_GTA_INV_FILE_DTL(COMPANY_CD,CONTRACT_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,INV_TITLE,"
										+ "FILE_NAME,ENT_BY,ENT_DT,INVOICE_TYPE) "
										+ "VALUES(?,?,?,?,?,"
										+ "?,?,SYSDATE,?)";
								stmt1=conn.prepareStatement(queryString1);
								stmt1.setString(1, comp_cd);
								stmt1.setString(2, cont_type);
								stmt1.setString(3, invoice_seq);
								stmt1.setString(4, fin_year);
								stmt1.setString(5, "S");
								stmt1.setString(6, xmlFileNm);
								stmt1.setString(7, emp_cd);
								stmt1.setString(8, invoice_type);
								stmt1.executeUpdate();
								
								stmt1.close();
							}
							conn.commit();
						}
						else if(purchase_type_flag.equals("DERV"))
						{
							int count=0;
							String queryString2="SELECT COUNT(*) "
									+ "FROM FMS_DERV_INV_FILE_DTL "
									+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
									+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND INV_TYPE=? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, bu_state_tin);
							stmt2.setString(3, invoice_seq);
							stmt2.setString(4, fin_year);
							stmt2.setString(5, "S");
							stmt2.setString(6, invoice_type);
							rset2=stmt2.executeQuery();
							if(rset2.next())
							{
								count=rset2.getInt(1);
							}
							rset2.close();
							stmt2.close();
							
							if(count > 0)
							{
								String queryString3="UPDATE FMS_DERV_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
										+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
										+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND INV_TYPE=? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, xmlFileNm);
								stmt3.setString(2, emp_cd);
								stmt3.setString(3, comp_cd);
								stmt3.setString(4, bu_state_tin);
								stmt3.setString(5, invoice_seq);
								stmt3.setString(6, fin_year);
								stmt3.setString(7, "S");
								stmt3.setString(8, invoice_type);
								stmt3.executeUpdate();
								
								stmt3.close();
							}
							else
							{
								String queryString3="INSERT INTO FMS_DERV_INV_FILE_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,"
										+ "FILE_NAME,ENT_BY,ENT_DT,INV_TYPE) "
										+ "VALUES(?,?,?,?,?,"
										+ "?,?,SYSDATE,?)";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, comp_cd);
								stmt3.setString(2, bu_state_tin);
								stmt3.setString(3, invoice_seq);
								stmt3.setString(4, fin_year);
								stmt3.setString(5, "S");
								stmt3.setString(6, xmlFileNm);
								stmt3.setString(7, emp_cd);
								stmt3.setString(8, invoice_type);
								stmt3.executeUpdate();
								stmt3.close();
							}
							conn.commit();
						}
						else
						{
							int count=0;
							String query3="SELECT COUNT(*) "
									+ "FROM FMS_PUR_INV_FILE_DTL "
									+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
									+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=? AND INV_FLAG=?";
							stmt3=conn.prepareStatement(query3);
							stmt3.setString(1, comp_cd);
							stmt3.setString(2, cont_type);
							stmt3.setString(3, invoice_seq);
							stmt3.setString(4, fin_year);
							stmt3.setString(5, "S");
							stmt3.setString(6, inv_flag);
							rset3=stmt3.executeQuery();
							if(rset3.next())
							{
								count=rset3.getInt(1);
							}
							rset3.close();
							stmt3.close();
							
							if(count > 0)
							{
								String queryString3="UPDATE FMS_PUR_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
										+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
										+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=? AND INV_FLAG=?";
								stmt1=conn.prepareStatement(queryString3);
								stmt1.setString(1, xmlFileNm);
								stmt1.setString(2, emp_cd);
								stmt1.setString(3, comp_cd);
								stmt1.setString(4, cont_type);
								stmt1.setString(5, invoice_seq);
								stmt1.setString(6, fin_year);
								stmt1.setString(7, "S");
								stmt1.setString(8, inv_flag);
								stmt1.executeUpdate();
								
								stmt1.close();
							}
							else
							{
								String queryString3="INSERT INTO FMS_PUR_INV_FILE_DTL(COMPANY_CD,CONTRACT_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,INV_TITLE,"
										+ "FILE_NAME,ENT_BY,ENT_DT,INV_FLAG) "
										+ "VALUES(?,?,?,?,?,"
										+ "?,?,SYSDATE,?)";
								stmt1=conn.prepareStatement(queryString3);
								stmt1.setString(1, comp_cd);
								stmt1.setString(2, cont_type);
								stmt1.setString(3, invoice_seq);
								stmt1.setString(4, fin_year);
								stmt1.setString(5, "S");
								stmt1.setString(6, xmlFileNm);
								stmt1.setString(7, emp_cd);
								stmt1.setString(8, inv_flag);
								stmt1.executeUpdate();
								
								stmt1.close();
							}
							conn.commit();
						}
					}
				}
			}
			rset.close();
			stmt.close();
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD","");
			transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet","");

			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			if(xml_generate_flag)
			{
				if(!xmlFileNm.equals(""))
				{
					String appPath = request.getServletContext().getRealPath("");
					
					String main_folder="";
					if(!comp_cd.equals(""))
					{
						main_folder=CommonVariable.work_dir+comp_cd;
					}
					File MainDir = new File(appPath+File.separator+main_folder);
					if(!MainDir.exists()) 
					{
						MainDir.mkdir();
					}
					String sub_folder=""+CommonVariable.sun_xml;
					
					File SubDir = new File(appPath+File.separator+main_folder+File.separator+sub_folder);
					if(!SubDir.exists()) 
					{
						SubDir.mkdir();
					}
					
					StreamResult result =  new StreamResult(new File(appPath+File.separator+main_folder+File.separator+sub_folder+""+File.separator+""+xmlFileNm));
					transformer.transform(source, result);
					msg = "Success! - SUN Purchase XML generated!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - SUN Purchase XML generation failed!";
					msg_type="E";
				}
			}
			else
			{
				msg = "Failed! - SUN Purchase XML generation failed, Please try again!";
				msg_type="E";
			}
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//other helper function
	public String getTdsAnalysisCode(String tds_per)
	{
		/*
		 	NOTE: 
		 		In FMS8: it is fetched from FMS7_TDS_CERTIFICATE_DTL
		 		this code is configured directly from the back-end and this code changes in every financial year
		 		the current representation is taken from the below details.
		 		
		 		EFF_DT:				01-04-25
		 		FINANCIAL_YEAR:		2025-2026
		*/
		String function_nm="getTdsAnalysisCode()";
		String tds_acc_cd="";
		try
		{
			if(!tds_per.equals(""))
			{
				if(Double.doubleToRawLongBits(Double.parseDouble(tds_per))==Double.doubleToRawLongBits(2))
				{
					tds_acc_cd="YCC3";
				}
				else if(Double.doubleToRawLongBits(Double.parseDouble(tds_per))==Double.doubleToRawLongBits(0.05))
				{
					tds_acc_cd="YCC5";
				}
				else if(Double.doubleToRawLongBits(Double.parseDouble(tds_per))==Double.doubleToRawLongBits(0.04))
				{
					tds_acc_cd="YCQ4";
				}
				else if(Double.doubleToRawLongBits(Double.parseDouble(tds_per))==Double.doubleToRawLongBits(0.1))
				{
					tds_acc_cd="YCQ3";
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return tds_acc_cd;
	}
	
	public String getQuantityUnit(String counterparty_cd,String agmt_no,String agmt_type,String agmt_rev, String cont_no, String cont_type, String cont_rev, String instrument_no)
	{
		String function_nm="getQuantityUnit()";
		String unit="";
		try
		{
			String query="SELECT QTY_UNIT FROM FMS_DERV_INSTRUMENT_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND AGMT_TYPE=? "
					+ "AND AGMT_REV=? AND CONT_NO=? AND CONTRACT_TYPE=? AND CONT_REV=? AND INSTRUMENT_NO=? ";
			stmt6=conn.prepareStatement(query);
			stmt6.setString(1, comp_cd);
			stmt6.setString(2, counterparty_cd);
			stmt6.setString(3, agmt_no);
			stmt6.setString(4, agmt_type);
			stmt6.setString(5, agmt_rev);
			stmt6.setString(6, cont_no);
			stmt6.setString(7, cont_type);
			stmt6.setString(8, cont_rev);
			stmt6.setString(9, instrument_no);
			rset6=stmt6.executeQuery();
			if(rset6.next())
			{
				unit=rset6.getString(1)==null?"":rset6.getString(1);
			}
			rset6.close();
			stmt6.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return unit;
	}
	
	public String getTdsFactor(String tdsStructCd)
	{
		String function_nm="getTdsFactor()";
		String tds_factor="";
		try
		{
			String query="SELECT FACTOR FROM FMS_TAX_STRUCTURE_DTL "
					+ "WHERE TAX_STR_CD=? ";
			stmt6=conn.prepareStatement(query);
			stmt6.setString(1,tdsStructCd);
			rset6=stmt6.executeQuery();
			if(rset6.next())
			{
				tds_factor=rset6.getString(1)==null?"":rset6.getString(1);
			}
			rset6.close();
			stmt6.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return tds_factor;
	}
	
	public double getDeltaQunatity(String counterparty_cd, String agmt_no, String agmt_rev, String cont_no, String cont_rev, String cont_type, String cargo_no)
	{
		String function_nm="getDeltaQuantity()";
		double delta_qty=0;
		try
		{
			String query="SELECT A.ALLOC_QTY-B.ALLOC_QTY FROM FMS_PUR_SG_INV_MST A,FMS_PUR_SG_INV_MST B   "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=?  "
					+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONT_NO=?  "
					+ "AND A.CONT_REV=? AND A.CONTRACT_TYPE=? AND A.CARGO_NO=? "
					+ "AND A.INV_FLAG=? AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
					+ "AND A.CONT_REV=B.CONT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO AND B.INV_FLAG=?";
			stmt6=conn.prepareStatement(query);
			stmt6.setString(1, comp_cd);
			stmt6.setString(2, counterparty_cd);
			stmt6.setString(3, agmt_no);
			stmt6.setString(4, agmt_rev);
			stmt6.setString(5, cont_no);
			stmt6.setString(6, cont_rev);
			stmt6.setString(7, cont_type);
			stmt6.setString(8, cargo_no);
			stmt6.setString(9, "CF");
			stmt6.setString(10, "CP");
			rset6=stmt6.executeQuery();
			if(rset6.next())
			{
				delta_qty=rset6.getDouble(1);
			}
			rset6.close();
			stmt6.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return delta_qty;
	}
	
	public String getTax_sht_nm(String tax_cd)
	{
		String function_nm="getTax_sht_nm()";
		String sht_nm="";
		try
		{
			String query="SELECT SHT_NM FROM FMS_TAX_MST "
					+ "WHERE TAX_CODE=? ";
			stmt6=conn.prepareStatement(query);
			stmt6.setString(1, tax_cd);
			rset6=stmt6.executeQuery();
			if(rset6.next())
			{
				sht_nm=rset6.getString(1)==null?"":rset6.getString(1);
			}
			rset6.close();
			stmt6.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return sht_nm;
	}
	
	public String getSunApprovalDt(String inv_no)
	{
		String function_nm="getSunApprovalDt()";
		String sun_approval_dt="";
		try
		{
			String query="SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY')  "
					+ "FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND INVOICE_NO=? AND FIN_SYS=? ";
			query+= "UNION ";
			query+= "SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY')  "
					+ "FROM FMS_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND INVOICE_NO=? AND FIN_SYS=? ";
			query+= "UNION ";
			query+="SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY')  "
					+ "FROM FMS_PUR_SG_INV_MST "
					+ "WHERE COMPANY_CD=? AND INVOICE_NO=? AND FIN_SYS=? ";
			query+= "UNION ";
			query+= "SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY')  "
					+ "FROM FMS_PUR_PG_INV_MST "
					+ "WHERE COMPANY_CD=? AND INVOICE_NO=? AND FIN_SYS=?";
			query+= "UNION ";
			query+= "SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY')  "
					+ "FROM FMS_PUR_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND INVOICE_NO=? AND FIN_SYS=?";
			query+= "UNION ";
			query+="SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY')  "
					+ "FROM FMS_DLNG_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND INVOICE_NO=? AND FIN_SYS=? ";
			query+= "UNION ";
			query+="SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY')  "
					+ "FROM FMS_GTA_SG_INV_MST "
					+ "WHERE COMPANY_CD=? AND INVOICE_NO=? AND FIN_SYS=? ";
			query+= "UNION ";
			query+="SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY')  "
					+ "FROM FMS_GTA_PG_INV_MST "
					+ "WHERE COMPANY_CD=? AND INVOICE_NO=? AND FIN_SYS=? ";
			query+= "UNION ";
			query+="SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY')  "
					+ "FROM FMS_GTA_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND INVOICE_REF=? AND FIN_SYS=? ";
			query+= "UNION ";
			query+="SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY') "
					+ "FROM FMS_DERV_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND INVOICE_NO=? AND FIN_SYS=? ";
			query+= "UNION ";
			query+="SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY') "
					+ "FROM FMS_DERV_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND INVOICE_REF_NO=? AND FIN_SYS=? ";
			query+= "UNION ";
			query+="SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY') "
					+ "FROM FMS_DLNG_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND INVOICE_NO=? AND FIN_SYS=? ";
			query+= "UNION ";
			query+="SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY') "
					+ "FROM FMS_DLNG_SVC_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND INVOICE_NO=? AND FIN_SYS=? ";
			stmt6=conn.prepareStatement(query);
			stmt6.setString(1, comp_cd);
			stmt6.setString(2, inv_no);
			stmt6.setString(3, "S");
			stmt6.setString(4, comp_cd);
			stmt6.setString(5, inv_no);
			stmt6.setString(6, "S");
			stmt6.setString(7, comp_cd);
			stmt6.setString(8, inv_no);
			stmt6.setString(9, "S");
			stmt6.setString(10, comp_cd);
			stmt6.setString(11, inv_no);
			stmt6.setString(12, "S");
			stmt6.setString(13, comp_cd);
			stmt6.setString(14, inv_no);
			stmt6.setString(15, "S");
			stmt6.setString(16, comp_cd);
			stmt6.setString(17, inv_no);
			stmt6.setString(18, "S");
			stmt6.setString(19, comp_cd);
			stmt6.setString(20, inv_no);
			stmt6.setString(21, "S");
			stmt6.setString(22, comp_cd);
			stmt6.setString(23, inv_no);
			stmt6.setString(24, "S");
			stmt6.setString(25, comp_cd);
			stmt6.setString(26, inv_no);
			stmt6.setString(27, "S");
			stmt6.setString(28, comp_cd);
			stmt6.setString(29, inv_no);
			stmt6.setString(30, "S");
			stmt6.setString(31, comp_cd);
			stmt6.setString(32, inv_no);
			stmt6.setString(33, "S");
			stmt6.setString(34, comp_cd);
			stmt6.setString(35, inv_no);
			stmt6.setString(36, "S");
			stmt6.setString(37, comp_cd);
			stmt6.setString(38, inv_no);
			stmt6.setString(39, "S");
			rset6=stmt6.executeQuery();
			if(rset6.next())
			{
				sun_approval_dt=rset6.getString(1)==null?"":rset6.getString(1);
			}
			rset6.close();
			stmt6.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return sun_approval_dt;
	}
	
	public String getExchng_Rate(String rate_cd, String eff_dt)
	{
		String function_nm="getExchng_Rate()";
		String exchng_val="";
		try
		{
			String queryString1="SELECT EXCHG_VAL "
					+ "FROM FMS_EXCHG_RATE_ENTRY A "
					+ "WHERE "//COMPANY_CD=? AND "
					+ "EXCHG_RATE_CD=? "
					+ "AND EFF_DT = TO_DATE(?,'DD/MM/YYYY') AND FLAG=?";
			stmt6 = conn.prepareStatement(queryString1);
			//stmt1.setString(1, comp_cd);
			stmt6.setString(1, rate_cd);
			stmt6.setString(2, eff_dt);
			stmt6.setString(3, "Y");
			rset6 = stmt6.executeQuery();
			if(rset6.next())
			{
				exchng_val = rset6.getString(1)==null?"":rset6.getString(1);
			}
			rset6.close();
			stmt6.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return exchng_val;
	}
	
	public double getExchg_rate(String rate_nm, String eff_dt)
	{
		String function_nm="getExchg_rate()";
		double exchg_val=0;
		try
		{
			String exchng_rate_cd="";
			String queryString1="SELECT EXC_RATE_CD "
					+ "FROM FMS_EXCHG_RATE_MST "
					+ "WHERE "//COMPANY_CD=? AND "
					+ "UPPER(EXC_RATE_NM) = ?"; 
			stmt6 = conn.prepareStatement(queryString1);
			//stmt1.setString(1, comp_cd);
			stmt6.setString(1, rate_nm.toUpperCase());
			rset6 = stmt6.executeQuery();
			if(rset6.next())
			{
				exchng_rate_cd = rset6.getString(1)==null?"":rset6.getString(1);
			}
			rset6.close();
			stmt6.close();

			queryString1="SELECT EXCHG_VAL "
					+ "FROM FMS_EXCHG_RATE_ENTRY A "
					+ "WHERE "//COMPANY_CD=? AND "
					+ "EXCHG_RATE_CD=? "
					+ "AND EFF_DT = TO_DATE(?,'DD/MM/YYYY') "
					//+ "AND EFF_DT =(SELECT MAX(B.EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
					//+ "WHERE "//A.COMPANY_CD=B.COMPANY_CD AND "
					//+ "A.EXCHG_RATE_CD=B.EXCHG_RATE_CD  "
					//+ "AND B.EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')") "
					+ "AND FLAG=?";
			stmt6 = conn.prepareStatement(queryString1);
			//stmt1.setString(1, comp_cd);
			stmt6.setString(1, exchng_rate_cd);
			stmt6.setString(2, eff_dt);
			stmt6.setString(3, "Y");
			rset6 = stmt6.executeQuery();
			if(rset6.next())
			{
				exchg_val = rset6.getDouble(1);
			}
			rset6.close();
			stmt6.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return exchg_val;
	}
	
	public String getAccountingPeriod(String invoice_dt)
	{
		String function_nm="getAccountingPeriod()";
		String acc_period="";
		try
		{
			String []invDt = invoice_dt.split("/");
			String trans_dt = invDt[0]+invDt[1]+invDt[2];
			if(invDt[1].equals("01"))
			{
				acc_period="001"+invDt[2];
			}
			else if(invDt[1].equals("02"))
			{
				acc_period="002"+invDt[2];
			}
			else if(invDt[1].equals("03"))
			{
				acc_period="003"+invDt[2];
			}
			else if(invDt[1].equals("04"))
			{
				acc_period="005"+invDt[2];
			}
			else if(invDt[1].equals("05"))
			{
				acc_period="006"+invDt[2];
			}
			else if(invDt[1].equals("06"))
			{
				acc_period="007"+invDt[2];
			}
			else if(invDt[1].equals("07"))
			{
				acc_period="009"+invDt[2];
			}
			else if (invDt[1].equals("08"))
			{
				acc_period="010"+invDt[2];
			}
			else if(invDt[1].equals("09"))
			{
				acc_period="011"+invDt[2];
			}
			else if(invDt[1].equals("10"))
			{
				acc_period="013"+invDt[2];
			}
			else if(invDt[1].equals("11"))
			{
				acc_period="014"+invDt[2];
			}
			else if(invDt[1].equals("12"))
			{
				acc_period="015"+invDt[2];
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return acc_period;
	}
	
	public String getCounterpartySunAccountCode(String counterparty_cd, String entity_role, String plant_seq_no, String account_type)
	{
		String function_nm="getCounterpartySunAccountCode()";
		String account_cd="";
		try
		{
			String queryString="SELECT ACCOUNT_CODE "
					+ "FROM FMS_ENTITY_ACCOUNT_CODE_SUN "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND PLANT_SEQ_NO=? AND ACCOUNT_TYPE=? ";
			stmt6= conn.prepareStatement(queryString);
			stmt6.setString(1, comp_cd);
			stmt6.setString(2, counterparty_cd);
			stmt6.setString(3, entity_role);
			stmt6.setString(4, plant_seq_no);
			stmt6.setString(5, account_type);
			rset6=stmt6.executeQuery();
			if(rset6.next())
			{
				account_cd=rset6.getString(1)==null?"":rset6.getString(1);
			}
			rset6.close();
			stmt6.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return account_cd;
	}
	
	public String getTaxStructureSunAccountCd(String tax_struct_cd,String tax_cd, String bu_state_tin, String bu_unit, String account_type)
	{
		String function_nm="getTaxStructureSunAccountCd()";
		String account_cd="";
		try
		{
			String queryString="SELECT SUN_CODE FROM FMS_TAX_STRUCTURE_SUN "
					+ "WHERE COMPANY_CD=? AND TAX_STR_CD=? AND TAX_CODE=? "
					+ "AND BU_STATE_TIN=? AND BU_UNIT=? AND ACCOUNT_TYPE=? ";
			stmt6=conn.prepareStatement(queryString);
			stmt6.setString(1, comp_cd);
			stmt6.setString(2, tax_struct_cd);
			stmt6.setString(3, tax_cd);
			stmt6.setString(4, bu_state_tin);
			stmt6.setString(5, bu_unit);
			stmt6.setString(6, account_type);
			rset6 = stmt6.executeQuery();
			if(rset6.next())
			{
				account_cd=rset6.getString(1)==null?"":rset6.getString(1);
			}
			rset6.close();
			stmt6.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return account_cd;
	}
	
	public String getTcsTdsStructureSunAccountCd(String tax_struct_cd, String bu_state_tin, String bu_unit)
	{
		String function_nm="getTcsTdsStructureSunAccountCd()";
		String account_cd="";
		try
		{
			String queryString="SELECT SUN_CODE FROM FMS_TAX_STRUCTURE_SUN "
					+ "WHERE COMPANY_CD=? AND TAX_STR_CD=? "
					+ "AND BU_STATE_TIN=? AND BU_UNIT=? ";
			stmt6=conn.prepareStatement(queryString);
			stmt6.setString(1, comp_cd);
			stmt6.setString(2, tax_struct_cd);
			stmt6.setString(3, bu_state_tin);
			stmt6.setString(4, bu_unit);
			rset6 = stmt6.executeQuery();
			if(rset6.next())
			{
				account_cd=rset6.getString(1)==null?"":rset6.getString(1);
			}
			rset6.close();
			stmt6.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return account_cd;
	}
	
	public String getStateCd(String cd) throws Exception
	{
		String function_nm="getStateCd()";
		String nm="";
		try
		{
			if(conn!=null)
			{
				String query = "SELECT STATE_CODE "
						+ "FROM FMS_STATE_MST "
						+ "WHERE TIN=? "
						+ "ORDER BY STATE_NM";
				stmt6 = conn.prepareStatement(query);
				stmt6.setString(1, cd);
				rset6 = stmt6.executeQuery();
				if(rset6.next())
				{
					nm= rset6.getString(1)==null?"":rset6.getString(1);
				}
				stmt6.close();
				rset6.close();
				
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return nm;
	}
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	String entity_role="";
	public void setEntity_role(String entity_role) {this.entity_role = entity_role;}
	String from_dt="";
	public void setFrom_dt(String from_dt) {this.from_dt=from_dt;}
	String to_dt="";
	public void setTo_dt(String to_dt) {this.to_dt=to_dt;}
	String emp_cd="";
	public void setEmp_cd(String emp_cd) {this.emp_cd=emp_cd;}
	String file_nm="";
	public void setFileNm(String file_nm) {this.file_nm = file_nm;}
	String xml_gen_from_dt="";
	public void setXml_gen_from_dt(String xml_gen_from_dt) {this.xml_gen_from_dt=xml_gen_from_dt;}
	String xml_gen_to_dt="";
	public void setXml_gen_to_dt(String xml_gen_to_dt) {this.xml_gen_to_dt=xml_gen_to_dt;}
	String segment="";
	public void setSegment(String segment) {this.segment=segment;}
	String file_path="";
	public void setFile_path(String filePath) {this.file_path=filePath;}
	String rem_no="";
	public void setRemittance_no(String remittance_no) {this.rem_no=remittance_no;}
	String exchng_rate_dt="";
	public void setExchng_rate_dt(String exchng_rate_dt) {this.exchng_rate_dt=exchng_rate_dt;}
	
	String buStateTin="";
	String financial_year="";
	String inv_seq="";
	String inv_type="";
	
	String msg="";
	String msg_type="";
	
	public String getMsg() {return msg;}
	public String getMsg_type() {return msg_type;}
	
	Vector VMASTER_TAX_CATEGORY = new Vector();
	Vector VMASTER_TAX_CATEGORY_NM = new Vector();
	
	Vector VBU_SEQ = new Vector();
	Vector VBU_ABBR = new Vector();
	Vector VCO_CD = new Vector();
	Vector VBU_STATE = new Vector(); 
	
	Vector VTAX_STRUCT_CD = new Vector();
	Vector VTAX_STRUCT_NM = new Vector();
	Vector VTAX_STRUCT_STATUS = new Vector();
	Vector VTAX_STRUCT_RMK = new Vector();
	Vector VTAX_CATEGORY = new Vector();
	Vector VTAX_CATEGORY_NM = new Vector();
	Vector VTAX_STRUCT_APP_DT = new Vector();
	Vector VSAP_TAX_CODE = new Vector();
	Vector VPAY_RECV_NM = new Vector();
	Vector VINDEX = new Vector();
	Vector VTAX_COUNT = new Vector();
	Vector VSUB_TAX_STRUCT_NM = new Vector();
	Vector VSUN_CD = new Vector();
	Vector VSUG_CD = new Vector();
	Vector VSUB_SUN_CD = new Vector();
	Vector VSUB_SUG_CD = new Vector();
	Vector VTAX_CD = new Vector();
	
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VCOUNTERPARTY_CATEGORY = new Vector();
	
	Vector VACCOUNT_TYPE = new Vector();
	Vector VACCOUNT_TYPE_NM = new Vector();
	Vector VSUN_ACCOUNT = new Vector();
	Vector VSUN_ENTITY_ACCOUNT = new Vector();
	Vector VPLANT_SEQ_NO = new Vector();
	
	Vector VPLANT_ABBR = new Vector();
	Vector VPLANT_INDEX = new Vector();
	Vector VACC_PLANT = new Vector();
	Vector VACC_OTH_PLANT = new Vector();
	
	Vector VINVOICE_NO= new Vector();
	Vector VJOURNAL_TYPE= new Vector();
	Vector VAPPROVAL_DT= new Vector();
	Vector VLEDGER= new Vector();
	Vector VACCOUNT_CD= new Vector();
	Vector VPERIOD_START_DT= new Vector();
	Vector VPERIOD_END_DT= new Vector();
	Vector VBASE_AMT= new Vector();
	Vector VDEBIT_CREDIT= new Vector();
	Vector VREPORT_AMT= new Vector();
	Vector VCURRENCY_CD= new Vector();
	Vector VEXCHNG_RATE= new Vector();
	Vector VINVOICE_DT= new Vector();
	Vector VDESC= new Vector();
	Vector VINVOICE_DUE_DT= new Vector();
	Vector VCOST_CTR_CD= new Vector();
	Vector VCOA_CD= new Vector();
	Vector VCODE= new Vector();
	Vector VBU_UNIT_CD= new Vector();
	Vector VGOOD_SERVICE= new Vector();		
	Vector VREV_CHARGE= new Vector();
	Vector VHSN_CD= new Vector();
	Vector VPOS_CD= new Vector();
	Vector VTAX_LINE_AMT= new Vector();
	Vector VSUPPLY_TYPE= new Vector();
	Vector VTOTAL_INV_AMT= new Vector();
	Vector VEMPLOYEE_CD = new Vector();
	Vector VTRANS_AMT=new Vector();
	Vector VPROJECT_CD=new Vector();
	
	Vector VJOURNAL_TYPE_NM=new Vector();
	Vector VSUN_FILE_NM=new Vector();
	
	Vector VSEGMENT=new Vector();
	Vector VSEGMENT_TYPE=new Vector();
	
	Vector VTEMP_SEGMENT=new Vector();
	Vector VTEMP_SEGMENT_TYPE=new Vector();
	
	Vector VTDS_TAX_AMT = new Vector();
	Vector VTDS_TAX_PERCENT = new Vector();
	Vector VPAY_RECV_AMT= new Vector();
	Vector VPAY_RECV_DT= new Vector();
	Vector VAGMT_NO= new Vector();
	Vector VAGMT_REV_NO= new Vector();
	Vector VCONT_NO= new Vector();
	Vector VCONT_REV_NO= new Vector();
	Vector VCONTRACT_TYPE= new Vector();
	Vector VDIS_CONT_MAPPING= new Vector();
	Vector VFINANCIAL_YEAR= new Vector();
	Vector VBU_STATE_TIN= new Vector();
	Vector VINVOICE_SEQ= new Vector();
	Vector VSALES_PRICE= new Vector();
	Vector VSALES_PRICE_CD= new Vector();
	Vector VSALES_PRICE_NM= new Vector();
	Vector VGROSS_AMT= new Vector();
	Vector VTAX_AMT= new Vector();
	Vector VINVOICE_AMT= new Vector();
	Vector VNET_PAYABLE_AMT= new Vector();
	Vector VSHORT_RECEIVED= new Vector();
	Vector VTDS_TCS_FLAG= new Vector();
	Vector VTCS_AMT= new Vector();
	Vector VTAX_STRUCT_DTL= new Vector();
	Vector VBU_NM= new Vector();
	Vector VSAP_APPROVAL_FLAG= new Vector();
	Vector VTYPE_FLAG= new Vector();
	Vector VALLOC_QTY= new Vector();
	Vector VINVOICE_RAISED_IN= new Vector();
	Vector VPAYMENT_DONE_IN= new Vector();
	Vector VTCS_TDS= new Vector();
	Vector VTDS_GROSS_AMT= new Vector();
	Vector VTDS_GROSS_PERCENT= new Vector();
	Vector VSALES_AMT= new Vector();
	Vector VINVOICE_TYPE= new Vector();
	Vector VCONT_REF_NO= new Vector();
	Vector VCASH_FLOW= new Vector();
	Vector VAPPROVE_HIST=new Vector();
	
	Vector VPAYMENT_TYPE_FLAG=new Vector();
	Vector VPAYMENT_TYPE_NM=new Vector();
	Vector VDIS_REMITTANCE_NO=new Vector();
	Vector VSALES_PRICE_UNIT=new Vector();
	Vector VQTY_UNIT=new Vector();
	Vector VSALE_AMT=new Vector();
	Vector VADJ_SIGN=new Vector();
	Vector VEXCHNAGE_RATE_DATE=new Vector();
	Vector VINV_FLAG=new Vector();
	Vector VTCS_TDS_AMT_USD=new Vector();
	Vector VTCS_TDS_AMT=new Vector();
	Vector VTCS_TDS_FACTOR=new Vector();
	Vector VTCS_TDS_STRUCT_CD=new Vector();
	Vector VTCS_TDS_EFF_DT=new Vector();
	Vector VTCS_TDS_DONE=new Vector();
	Vector VGROSS_AMT_USD=new Vector();
	Vector VTAX_AMT_USD=new Vector();
	Vector VINVOICE_AMT_USD=new Vector();
	Vector VADJ_AMT_USD=new Vector();
	Vector VNET_PAYABLE_USD=new Vector();
	Vector VADJ_AMT=new Vector();
	Vector VNET_PAYABLE=new Vector();
	Vector VEXCHNAGE_RATE=new Vector();
	Vector VSAP_EXCHANG_FLAG=new Vector();
	Vector VSPLIT_VALUE=new Vector();
	Vector VEXCHNG_RATE_CD = new Vector();
	Vector VEXCHNG_RATE_CONFIG = new Vector();
	Vector VACT_ARRIVAL_DT = new Vector();
	Vector VTRANS_DT = new Vector();
	
	public Vector getVSEGMENT() {return VSEGMENT;}
	public Vector getVSEGMENT_TYPE() {return VSEGMENT_TYPE;}
	
	public Vector getVTEMP_SEGMENT() {return VTEMP_SEGMENT;}
	public Vector getVTEMP_SEGMENT_TYPE() {return VTEMP_SEGMENT_TYPE;}
	
	public Vector getVTDS_TAX_AMT() {return VTDS_TAX_AMT;}
	public Vector getVTDS_TAX_PERCENT() {return VTDS_TAX_PERCENT;}
	public Vector getVPAY_RECV_AMT() {return VPAY_RECV_AMT;}
	public Vector getVPAY_RECV_DT() {return VPAY_RECV_DT;}
	public Vector getVAGMT_NO() {return VAGMT_NO;}
	public Vector getVAGMT_REV_NO() {return VAGMT_REV_NO;}
	public Vector getVCONT_NO() {return VCONT_NO;}
	public Vector getVCONT_REV_NO() {return VCONT_REV_NO;}
	public Vector getVCONTRACT_TYPE() {return VCONTRACT_TYPE;}
	public Vector getVDIS_CONT_MAPPING() {return VDIS_CONT_MAPPING;}
	public Vector getVFINANCIAL_YEAR() {return VFINANCIAL_YEAR;}
	public Vector getVBU_STATE_TIN() {return VBU_STATE_TIN;}
	public Vector getVINVOICE_SEQ() {return VINVOICE_SEQ;}
	public Vector getVSALES_PRICE() {return VSALES_PRICE;}
	public Vector getVSALES_PRICE_CD() {return VSALES_PRICE_CD;}
	public Vector getVSALES_PRICE_NM() {return VSALES_PRICE_NM;}
	public Vector getVGROSS_AMT() {return VGROSS_AMT;}
	public Vector getVTAX_AMT() {return VTAX_AMT;}
	public Vector getVINVOICE_AMT() {return VINVOICE_AMT;}
	public Vector getVNET_PAYABLE_AMT() {return VNET_PAYABLE_AMT;}
	public Vector getVSHORT_RECEIVED() {return VSHORT_RECEIVED;}
	public Vector getVTDS_TCS_FLAG() {return VTDS_TCS_FLAG;}
	public Vector getVTCS_AMT() {return VTCS_AMT;}
	public Vector getVTAX_STRUCT_DTL() {return VTAX_STRUCT_DTL;}
	public Vector getVBU_NM() {return VBU_NM;}
	public Vector getVSAP_APPROVAL_FLAG() {return VSAP_APPROVAL_FLAG;}
	public Vector getVTYPE_FLAG() {return VTYPE_FLAG;}
	public Vector getVALLOC_QTY() {return VALLOC_QTY;}
	public Vector getVINVOICE_RAISED_IN() {return VINVOICE_RAISED_IN;}
	public Vector getVPAYMENT_DONE_IN() {return VPAYMENT_DONE_IN;}
	public Vector getVTCS_TDS() {return VTCS_TDS;}
	public Vector getVTDS_GROSS_AMT() {return VTDS_GROSS_AMT;}
	public Vector getVSALES_AMT() {return VSALES_AMT;}
	public Vector getVINVOICE_TYPE() {return VINVOICE_TYPE;}
	public Vector getVCONT_REF_NO() {return VCONT_REF_NO;}
	public Vector getVCASH_FLOW() {return VCASH_FLOW;}
	public Vector getVAPPROVE_HIST() {return VAPPROVE_HIST;}
	public Vector getVTDS_GROSS_PERCENT() {return VTDS_GROSS_PERCENT;}
	
	public Vector getVPAYMENT_TYPE_FLAG() {return VPAYMENT_TYPE_FLAG;}
	public Vector getVPAYMENT_TYPE_NM() {return VPAYMENT_TYPE_NM;}
	public Vector getVDIS_REMITTANCE_NO() {return VDIS_REMITTANCE_NO;}
	public Vector getVSALES_PRICE_UNIT() {return VSALES_PRICE_UNIT;}
	public Vector getVQTY_UNIT() {return VQTY_UNIT;}
	public Vector getVSALE_AMT() {return VSALE_AMT;}
	public Vector getVADJ_SIGN() {return VADJ_SIGN;}
	public Vector getVEXCHNAGE_RATE_DATE() {return VEXCHNAGE_RATE_DATE;}
	public Vector getVINV_FLAG() {return VINV_FLAG;}
	public Vector getVTCS_TDS_AMT_USD() {return VTCS_TDS_AMT_USD;}
	public Vector getVTCS_TDS_AMT() {return VTCS_TDS_AMT;}
	public Vector getVTCS_TDS_FACTOR() {return VTCS_TDS_FACTOR;}
	public Vector getVTCS_TDS_STRUCT_CD() {return VTCS_TDS_STRUCT_CD;}
	public Vector getVTCS_TDS_EFF_DT() {return VTCS_TDS_EFF_DT;}
	public Vector getVTCS_TDS_DONE() {return VTCS_TDS_DONE;}
	public Vector getVGROSS_AMT_USD() {return VGROSS_AMT_USD;}
	public Vector getVTAX_AMT_USD() {return VTAX_AMT_USD;}
	public Vector getVINVOICE_AMT_USD() {return VINVOICE_AMT_USD;}
	public Vector getVADJ_AMT_USD() {return VADJ_AMT_USD;}
	public Vector getVNET_PAYABLE_USD() {return VNET_PAYABLE_USD;}
	public Vector getVADJ_AMT() {return VADJ_AMT;}
	public Vector getVNET_PAYABLE() {return VNET_PAYABLE;}
	public Vector getVEXCHNAGE_RATE() {return VEXCHNAGE_RATE;}
	public Vector getVSAP_EXCHANG_FLAG() {return VSAP_EXCHANG_FLAG;}
	public Vector getVSPLIT_VALUE() {return VSPLIT_VALUE;}
	public Vector getVEXCHNG_RATE_CD() {return VEXCHNG_RATE_CD;}
	public Vector getVEXCHNG_RATE_CONFIG() {return VEXCHNG_RATE_CONFIG;}
	public Vector getVACT_ARRIVAL_DT() {return VACT_ARRIVAL_DT;}
	public Vector getVTRANS_DT() {return VTRANS_DT;}

	public Vector getVINVOICE_NO() {return VINVOICE_NO;}
	public Vector getVJOURNAL_TYPE() {return VJOURNAL_TYPE;}
	public Vector getVAPPROVAL_DT() {return VAPPROVAL_DT;}
	public Vector getVLEDGER() {return VLEDGER;}
	public Vector getVACCOUNT_CD() {return VACCOUNT_CD;}
	public Vector getVPERIOD_START_DT() {return VPERIOD_START_DT;}
	public Vector getVPERIOD_END_DT() {return VPERIOD_END_DT;}
	public Vector getVBASE_AMT() {return VBASE_AMT;}
	public Vector getVDEBIT_CREDIT() {return VDEBIT_CREDIT;}
	public Vector getVREPORT_AMT() {return VREPORT_AMT;}
	public Vector getVCURRENCY_CD() {return VCURRENCY_CD;}
	public Vector getVEXCHNG_RATE() {return VEXCHNG_RATE;}
	public Vector getVINVOICE_DT() {return VINVOICE_DT;}
	public Vector getVDESC() {return VDESC;}
	public Vector getVINVOICE_DUE_DT() {return VINVOICE_DUE_DT;}
	public Vector getVCOST_CTR_CD() {return VCOST_CTR_CD;}
	public Vector getVCOA_CD() {return VCOA_CD;}
	public Vector getVCODE() {return VCODE;}
	public Vector getVBU_UNIT_CD() {return VBU_UNIT_CD;}
	public Vector getVGOOD_SERVICE() {return VGOOD_SERVICE;}
	public Vector getVREV_CHARGE() {return VREV_CHARGE;}
	public Vector getVHSN_CD() {return VHSN_CD;}
	public Vector getVPOS_CD() {return VPOS_CD;}
	public Vector getVTAX_LINE_AMT() {return VTAX_LINE_AMT;}
	public Vector getVSUPPLY_TYPE() {return VSUPPLY_TYPE;}
	public Vector getVTOTAL_INV_AMT() {return VTOTAL_INV_AMT;}
	public Vector getVEMPLOYEE_CD() {return VEMPLOYEE_CD;}
	public Vector getVTRANS_AMT() {return VTRANS_AMT;}
	public Vector getVPROJECT_CD() {return VPROJECT_CD;}
	
	public Vector getVSUN_FILE_NM() {return VSUN_FILE_NM;}
	public Vector getVJOURNAL_TYPE_NM() {return VJOURNAL_TYPE_NM;}
	
	public Vector getVMASTER_TAX_CATEGORY() {return VMASTER_TAX_CATEGORY;}
	public Vector getVMASTER_TAX_CATEGORY_NM() {return VMASTER_TAX_CATEGORY_NM;}

	public Vector getVBU_SEQ() {return VBU_SEQ;}
	public Vector getVBU_ABBR() {return VBU_ABBR;}
	public Vector getVCO_CD() {return VCO_CD;}
	public Vector getVBU_STATE() {return VBU_STATE;}

	public Vector getVTAX_STRUCT_CD() {return VTAX_STRUCT_CD ;}
	public Vector getVTAX_STRUCT_NM() {return VTAX_STRUCT_NM ;}
	public Vector getVTAX_STRUCT_STATUS() {return  VTAX_STRUCT_STATUS;}
	public Vector getVTAX_STRUCT_RMK() {return VTAX_STRUCT_RMK ;}
	public Vector getVTAX_CATEGORY() {return VTAX_CATEGORY ;}
	public Vector getVTAX_CATEGORY_NM() {return VTAX_CATEGORY_NM ;}
	public Vector getVTAX_STRUCT_APP_DT() {return VTAX_STRUCT_APP_DT;}
	public Vector getVSAP_TAX_CODE() {return VSAP_TAX_CODE;}
	public Vector getVPAY_RECV_NM() {return  VPAY_RECV_NM;}
	public Vector getVINDEX() {return VINDEX;}
	public Vector getVTAX_COUNT() {return VTAX_COUNT;}
	public Vector getVSUB_TAX_STRUCT_NM() {return VSUB_TAX_STRUCT_NM;}
	public Vector getVSUN_CD() {return VSUN_CD;}
	public Vector getVSUG_CD() {return VSUG_CD;}
	public Vector getVSUB_SUN_CD() {return VSUB_SUN_CD;}
	public Vector getVSUB_SUG_CD() {return VSUB_SUG_CD;}
	
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVCOUNTERPARTY_CATEGORY() {return VCOUNTERPARTY_CATEGORY;}
	
	public Vector getVSUN_ACCOUNT() {return VSUN_ACCOUNT;}
	public Vector getVSUN_ENTITY_ACCOUNT() {return VSUN_ENTITY_ACCOUNT;}
	public Vector getVACCOUNT_TYPE() {return VACCOUNT_TYPE;}
	public Vector getVACCOUNT_TYPE_NM() {return VACCOUNT_TYPE_NM;}
	public Vector getVPLANT_SEQ_NO() {return VPLANT_SEQ_NO;}
	public Vector getVPLANT_ABBR() {return VPLANT_ABBR;}
	public Vector getVPLANT_INDEX() {return VPLANT_INDEX;}
	public Vector getVACC_PLANT() {return VACC_PLANT;}
	public Vector getVACC_OTH_PLANT() {return VACC_OTH_PLANT;}

}