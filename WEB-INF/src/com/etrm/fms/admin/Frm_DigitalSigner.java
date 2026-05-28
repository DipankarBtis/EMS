package com.etrm.fms.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.EncryptTest;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.XmlUtilBean;

@WebServlet("/servlet/Frm_DigitalSigner")
public class Frm_DigitalSigner extends HttpServlet
{
	static String db_src_file_name="Frm_DigitalSigner.java";
	static Connection conn; 
	static PreparedStatement stmt;
	static PreparedStatement stmt1;
	static PreparedStatement stmt2;
	static PreparedStatement stmt3;
	static ResultSet rset; 
	static ResultSet rset1;
	static ResultSet rset2;
	static ResultSet rset3;
	//String queryString,queryString2;
	
	static String temp_option="";
	
	static UtilBean utilBean = new UtilBean();
	static DateUtil utilDate = new DateUtil();
	static XmlUtilBean xmlUtil = new XmlUtilBean();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException 
	{
		String function_nm="doPost()";
		try
		{
			String option = request.getParameter("option")==null?"":request.getParameter("option");
			
			/*if(option.equalsIgnoreCase("company_mst"))
			{
				getCompanyMaster(request,response);
			}
			else*/
			{
				temp_option=option;
				init(request,response);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	void getCompanyMaster(HttpServletRequest request, HttpServletResponse response) throws IOException	
	{
		String function_nm="getCompanyMaster()";
		try
		{
			utilBean.getEffectiveCompanyOwnerList(conn);
			Vector VCOMPANY_CD=utilBean.getCOUNTERPARTY_CD();
			//VCOMPANY_NM=utilBean.getCOUNTERPARTY_NM();
			Vector VCOMPANY_ABBR=utilBean.getCOUNTERPARTY_ABBR();
			
			PrintWriter out = response.getWriter();
	        out.println(VCOMPANY_ABBR);
	        out.println(VCOMPANY_CD);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	void LoginCheck(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String function_nm="LoginCheck()";
		
		boolean login=false;
		boolean locked=false;
		boolean disabled=false;
		boolean dormant=false;
		try
		{
			String username = request.getParameter("username")==null?"":request.getParameter("username");
			String company_cd = request.getParameter("company_cd")==null?"":request.getParameter("company_cd");
			String password = request.getParameter("password")==null?"":request.getParameter("password");
			String emp_cd ="";
			String emp_nm = "";
			
			String queryString="SELECT A.EMP_CD,A.EMP_UID,A.EMP_NM,A.EMAIL_ID,B.PASSWORD,A.LOCK_STATUS,A.EMP_STATUS "
					+ "FROM FMS_EMP_MST A, FMS_EMP_PASSWD_MST B "
					+ "WHERE A.EMP_CD=B.EMP_CD AND A.EMP_UID=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, username);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				emp_cd = rset.getString(1)==null?"":rset.getString(1);
				emp_nm = rset.getString(3)==null?"":rset.getString(3);
				//email = rset.getString(4)==null?"":rset.getString(4);
				String pwd = rset.getString(5)==null?"":rset.getString(5);
				String lock_status = rset.getString(6)==null?"":rset.getString(6);
				String emp_status = rset.getString(7)==null?"":rset.getString(7);
				
				if(emp_status.equals("Y")) 
				{
					if(!lock_status.equals("Y")) 
					{
						StringBuffer stringbuffer1 = (new EncryptTest()).encrypt(password);
						pwd = (new EncryptTest()).decrypt(pwd).toString();
						password = (new EncryptTest()).decrypt(stringbuffer1.toString()).toString();
						
						if(password.equals(pwd))
						{
							login = true;
						}
					}
					else 
					{
						locked = true;
					}
				}
				else 
				{
					if(emp_status.equals("N")) 
					{
						disabled = true;
					}
					else if(emp_status.equals("D")) 
					{
						dormant = true;
					}
				}
			}
			rset.close();
			stmt.close();
			
			boolean profile_access=false;
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_EMP_GROUP_DTL "
					+ "WHERE COMPANY_CD=? AND EMP_CD=? "
					+ "AND (FROM_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') "
					+ "AND TO_DT >= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, emp_cd);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				if(rset.getInt(1)>0)
				{
					profile_access=true;
				}
			}
			rset.close();
			stmt.close();
			
			PrintWriter out = response.getWriter();
            out.println("login="+login);
            out.println("locked="+locked);
            out.println("disabled="+disabled);
            out.println("dormant="+dormant);
            out.println("empcd="+emp_cd);
            out.println("empnm="+emp_nm);
            out.println("profile_access="+profile_access);
            
            if(login && profile_access)
            {
            	try
        		{
        			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, company_cd, utilBean.getEmpName(conn,emp_cd), request.getRemoteAddr(), "0", "PDF Signer","0","PDF Signer", "", "", "Login");  	
        		}
        		catch(Exception infoLogger)
        		{
        			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
        		}
            }
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	void getUnsignedPDFlist(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String function_nm="getUnsignedPDFlist()";
		try
		{
			Vector VFILE_NAME=new Vector();
			
			String company_cd = request.getParameter("company_cd")==null?"":request.getParameter("company_cd");
			String directory = request.getParameter("directory")==null?"":request.getParameter("directory");
			String sign_directory = request.getParameter("sign_directory")==null?"":request.getParameter("sign_directory");
			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			
			//for signed pdf path
			String sign_directory_path=CommonVariable.work_dir+""+company_cd+File.separator+sign_directory;
			String sign_appPath = request.getServletContext().getRealPath(sign_directory_path);
			if(!new File(sign_appPath).exists())
			{
				new File(sign_appPath).mkdirs();
			}
			
			//for unsigned pdf path
			String directory_path=CommonVariable.work_dir+""+company_cd+File.separator+directory;
			String appPath = request.getServletContext().getRealPath(directory_path);
			
			if(!new File(appPath).exists())
			{
				new File(appPath).mkdirs();
			}
			
			if(new File(appPath).exists())
			{
				if(invoice_type.equals("Sales") 
					|| invoice_type.equals("Sales CR DR Note") 
					|| invoice_type.equals("Sales Late Payment") 
					|| invoice_type.equals("LTCORA(Sell)") 
					|| invoice_type.equals("LTCORA(Sell) SUG") 
					|| invoice_type.equals("LTCORA(Sell) Storage") 
					|| invoice_type.equals("LTCORA(Sell) Receipt Voucher") 
					|| invoice_type.equals("LTCORA(Sell) Refund Voucher") 
					|| invoice_type.equals("LTCORA(Sell) CR DR Note") 
					|| invoice_type.equals("LTCORA(Sell) Late Payment") 
					|| invoice_type.equals("DLNG Sales") 
					|| invoice_type.equals("DLNG Sales CR DR Note")
					|| invoice_type.equals("DLNG Sales Late Payment")
					|| invoice_type.equals("DLNG Service") 
					|| invoice_type.equals("Derivative"))
				{
					String contType="";
					String invFlag="";
					if(invoice_type.equals("LTCORA(Sell)"))
					{
						contType="'Q','O'";
						invFlag="'F'";
					}
					else if(invoice_type.equals("LTCORA(Sell) SUG"))
					{
						contType="'Q','O'";
						invFlag="'UG'";
					}
					else if(invoice_type.equals("LTCORA(Sell) Storage"))
					{
						contType="'Q','O'";
						invFlag="'ST'";
					}
					else if(invoice_type.equals("LTCORA(Sell) Receipt Voucher"))
					{
						contType="'Q','O'";
					}
					else if(invoice_type.equals("LTCORA(Sell) Refund Voucher"))
					{
						contType="'Q','O'";
					}
					else if(invoice_type.equals("LTCORA(Sell) CR DR Note"))
					{
						contType="'Q','O'";
						invFlag="'CR','DR'";
					}
					else if(invoice_type.equals("LTCORA(Sell) Late Payment"))
					{
						contType="'Q','O'";
						invFlag="'LP'";
					}
					else if(invoice_type.equals("Sales"))
					{
						contType="'S','L','X'";
						invFlag="'F'";
					}
					else if(invoice_type.equals("Sales CR DR Note"))
					{
						contType="'S','L','X'";
						invFlag="'CR','DR'";
					}
					else if(invoice_type.equals("Sales Late Payment"))
					{
						contType="'S','L','X'";
						invFlag="'LP'";
					}
					else if(invoice_type.equals("DLNG Sales"))
					{
						contType="'F','E','W'";
						invFlag="'F'";
					}
					else if(invoice_type.equals("DLNG Sales CR DR Note"))
					{
						contType="'F','E','W'";
						invFlag="'CR','DR'";
					}					
					else if(invoice_type.equals("DLNG Sales Late Payment"))
					{
						contType="'F','E','W'";
						invFlag="'LP'";
					}
					else if(invoice_type.equals("DLNG Service"))
					{
						contType="'B','M','O','Q'";
						invFlag="'F','TLU'";
					}
					
					String queryString="";
					if(invoice_type.equals("DLNG Sales") || invoice_type.equals("DLNG Sales CR DR Note") || invoice_type.equals("DLNG Sales Late Payment"))
					{
						queryString="SELECT A.FILE_NAME "
		                    	+ "FROM FMS_DLNG_INV_FILE_DTL A, FMS_DLNG_INVOICE_MST B "
		                    	+ "WHERE A.COMPANY_CD=? AND A.PDF_SIGNED IS NULL "
		                    	+ "AND A.PDF_TYPE IN ('O','D','T') AND B.CONTRACT_TYPE IN ("+contType+") AND B.INV_FLAG IN ("+invFlag+") "
		                    	+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN "
		                    	+ "AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR ";
					}
					else if(invoice_type.equals("DLNG Service"))
					{
						queryString="SELECT A.FILE_NAME "
		                    	+ "FROM FMS_DLNG_SVC_INV_FILE_DTL A, FMS_DLNG_SVC_INVOICE_MST B "
		                    	+ "WHERE A.COMPANY_CD=? AND A.PDF_SIGNED IS NULL "
		                    	+ "AND A.PDF_TYPE IN ('O','D','T') AND B.CONTRACT_TYPE IN ("+contType+") AND B.INV_FLAG IN ("+invFlag+") "
		                    	+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN "
		                    	+ "AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR ";
					}
					else if(invoice_type.equals("Derivative"))
					{
						queryString="SELECT DISTINCT A.FILE_NAME "
		                    	+ "FROM FMS_DERV_INV_FILE_DTL A, FMS_DERV_INVOICE_MST B "
		                    	+ "WHERE A.COMPANY_CD=? AND A.PDF_SIGNED IS NULL "
		                    	+ "AND A.PDF_TYPE IN ('O') AND B.INV_TYPE='I'  "
		                    	+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN "
		                    	+ "AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR ";
					}
					else if(invoice_type.equals("LTCORA(Sell) Receipt Voucher"))
					{
						queryString="SELECT C.FILE_NAME "
								+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B, FMS_SECURITY_FILE_DTL C "
								+ "WHERE A.COMPANY_CD=? AND A.GX=? AND A.CR_DR='CR' AND C.FILE_TYPE='PDF' AND C.PDF_SIGNED IS NULL "
								+ "AND B.CONTRACT_TYPE IN ("+contType+") "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.SEQ_NO=B.SEQ_NO AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX "
								+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
								+ "AND A.SEQ_NO=C.SEQ_NO AND A.SEQ_REV_NO=C.SEQ_REV_NO AND A.GX=C.GX ";
					}
					else if(invoice_type.equals("LTCORA(Sell) Refund Voucher"))
					{
						queryString="SELECT C.FILE_NAME "
								+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B, FMS_SECURITY_FILE_DTL C "
								+ "WHERE A.COMPANY_CD=? AND A.GX=? AND A.CR_DR='DR' AND C.FILE_TYPE='PDF' AND C.PDF_SIGNED IS NULL "
								+ "AND B.CONTRACT_TYPE IN ("+contType+") "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.SEQ_NO=B.SEQ_NO AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX "
								+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
								+ "AND A.SEQ_NO=C.SEQ_NO AND A.SEQ_REV_NO=C.SEQ_REV_NO AND A.GX=C.GX ";
					}
					else
					{
						queryString="SELECT A.FILE_NAME "
		                    	+ "FROM FMS_INV_FILE_DTL A, FMS_INVOICE_MST B "
		                    	+ "WHERE A.COMPANY_CD=? AND A.PDF_SIGNED IS NULL "
		                    	+ "AND A.PDF_TYPE IN ('O','D','T') AND B.CONTRACT_TYPE IN ("+contType+") AND B.INV_FLAG IN ("+invFlag+") "
		                    	+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN "
		                    	+ "AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR ";
					}
					String temp_queryString=queryString;
					int stCount=0;
					stmt=conn.prepareStatement(temp_queryString);
					stmt.setString(++stCount, company_cd);
					if(invoice_type.equals("LTCORA(Sell) Receipt Voucher") || invoice_type.equals("LTCORA(Sell) Refund Voucher"))
					{
						stmt.setString(++stCount, "K");
					}
					rset=stmt.executeQuery();
					while(rset.next())
					{
						String file_name=rset.getString(1)==null?"":rset.getString(1);
						if(new File(appPath+File.separator+file_name).exists())
						{
							VFILE_NAME.add(file_name);
						}
					}
					rset.close();
					stmt.close();
					PrintWriter out = response.getWriter();
		            out.println(VFILE_NAME);
				}
				else if(invoice_type.equals("Free flow"))
				{
					String queryString="SELECT FILE_NAME "
	                    	+ "FROM FMS_FFLOW_INV_FILE_DTL "
	                    	+ "WHERE COMPANY_CD=? AND PDF_SIGNED IS NULL "
	                    	+ "AND PDF_TYPE IN ('O','D','T') ";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					rset=stmt.executeQuery();
					while(rset.next())
					{
						String file_name=rset.getString(1)==null?"":rset.getString(1);
						if(new File(appPath+File.separator+file_name).exists())
						{
							VFILE_NAME.add(file_name);
						}
					}
					rset.close();
					stmt.close();
					PrintWriter out = response.getWriter();
		            out.println(VFILE_NAME);
				}
				else if(invoice_type.equals("DLNG Free flow"))
				{
					String queryString="SELECT FILE_NAME "
	                    	+ "FROM FMS_DLNG_FFLOW_INV_FILE_DTL "
	                    	+ "WHERE COMPANY_CD=? AND PDF_SIGNED IS NULL "
	                    	+ "AND PDF_TYPE IN ('O','D','T') ";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					rset=stmt.executeQuery();
					while(rset.next())
					{
						String file_name=rset.getString(1)==null?"":rset.getString(1);
						if(new File(appPath+File.separator+file_name).exists())
						{
							VFILE_NAME.add(file_name);
						}
					}
					rset.close();
					stmt.close();
					PrintWriter out = response.getWriter();
		            out.println(VFILE_NAME);
				}
				else if(invoice_type.equals("IG Finance"))
				{
					String queryString="SELECT A.FILE_NAME "
	                    	+ "FROM FMS_OTH_INV_FILE_DTL A, FMS_OTH_INVOICE_MST B "
	                    	+ "WHERE A.COMPANY_CD=? AND A.PDF_SIGNED IS NULL "
	                    	+ "AND A.PDF_TYPE IN ('O','D','T') "
	                    	+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.INVOICE_SEQ=B.INVOICE_SEQ "
	                    	+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.SUPPLIER_CD=B.SUPPLIER_CD "
	                    	+ "AND A.INVOICE_TYPE=B.INVOICE_TYPE ";
					String temp_queryString=queryString;
					int stCount=0;
					stmt=conn.prepareStatement(temp_queryString);
					stmt.setString(++stCount, company_cd);
					rset=stmt.executeQuery();
					while(rset.next())
					{
						String file_name=rset.getString(1)==null?"":rset.getString(1);
						if(new File(appPath+File.separator+file_name).exists())
						{
							VFILE_NAME.add(file_name);
						}
					}
					rset.close();
					stmt.close();
					PrintWriter out = response.getWriter();
		            out.println(VFILE_NAME);	
				}
			}
			else
			{
				
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	void updateSignedPDFDetail(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String function_nm="updateSignedPDFDetail()";
		try
		{	
			String company_cd = request.getParameter("company_cd")==null?"":request.getParameter("company_cd");
			String directory = request.getParameter("directory")==null?"":request.getParameter("directory");
			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String signer_nm = request.getParameter("signer_nm")==null?"":request.getParameter("signer_nm");
			String file_nm = request.getParameter("file_nm")==null?"":request.getParameter("file_nm");
			String ent_by = request.getParameter("ent_by")==null?"":request.getParameter("ent_by");
			
			String directory_path=CommonVariable.work_dir+""+company_cd+File.separator+directory;
			String appPath = request.getServletContext().getRealPath(directory_path);
			
			String realPath = request.getRealPath("");
			File file_path = new File(appPath+File.separator+file_nm);
			String canonicalPath_file_path = file_path.getCanonicalPath();
			
			/*if(canonicalPath_file_path.startsWith("/var/lib/"))
			{
				canonicalPath_file_path=canonicalPath_file_path.replaceAll("/var/lib/", "/usr/share/");
			}*/
			
			if(!canonicalPath_file_path.startsWith(realPath))
	        {
	        	throw new IOException("Entry is outside of the target directory!");
	        }
	        else if(file_path.exists())
			{
	        	FileInputStream fis = new FileInputStream(file_path);
	            byte[] pdfBytes = new byte[(int) file_path.length()];
	            fis.read(pdfBytes);
	            fis.close();

	            // Encode bytes to Base64
	            String encodedPdf = Base64.getEncoder().encodeToString(pdfBytes);
	            byte[] base64bytes = encodedPdf.getBytes();
	            
	            String invice_no="";
				String invice_dt="";
				String counterParty_cd="";
				String agmtno="";
				String contno="";
				String cont_type="";
				String cargono="";
				int st_respond =0;
	            
				if(invoice_type.equals("Sales") 
					|| invoice_type.equals("LTCORA(Sell)") 
					|| invoice_type.equals("LTCORA(Sell) SUG") 
					|| invoice_type.equals("LTCORA(Sell) Storage") 
					|| invoice_type.equals("Sales CR DR Note") 
					|| invoice_type.equals("LTCORA(Sell) CR DR Note") 
					|| invoice_type.equals("Sales Late Payment") 
					|| invoice_type.equals("LTCORA(Sell) Late Payment"))
				{
					String queryString="SELECT A.FILE_NAME,A.PDF_SIGNED,B.INVOICE_NO,TO_CHAR(B.INVOICE_DT,'DD/MM/YYYY'),"
							+ "B.COUNTERPARTY_CD,B.AGMT_NO,B.CONT_NO,B.CONTRACT_TYPE,B.CARGO_NO "
            		  		+ "FROM FMS_INV_FILE_DTL A, FMS_INVOICE_MST B "
            		  		+ "WHERE A.COMPANY_CD=? AND A.FILE_NAME=? "
            		  		+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
            		  		+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, file_nm);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						String temp_str=rset.getString(2)==null?"":rset.getString(2);
						invice_no=rset.getString(3)==null?"":rset.getString(3);
						invice_dt=rset.getString(4)==null?"":rset.getString(4);
						counterParty_cd=rset.getString(5)==null?"":rset.getString(5);
						agmtno=rset.getString(6)==null?"":rset.getString(6);
						contno=rset.getString(7)==null?"":rset.getString(7);
						cont_type=rset.getString(8)==null?"":rset.getString(8);
						cargono=rset.getString(9)==null?"":rset.getString(9);
						
						if(temp_str.equals("") || temp_str.equals("N"))
						{
							String queryString2="UPDATE FMS_INV_FILE_DTL SET PDF_SIGNED='Y',SIGNED_DT=sysdate,SIGNED_BY=?,"
									+ "SIGNED_ENT_BY=?,PDF_CONTENT=? "
		                  			+ "WHERE COMPANY_CD=? AND FILE_NAME =? ";
		                  	stmt1=conn.prepareStatement(queryString2);
		                  	stmt1.setString(1, signer_nm);
		                  	stmt1.setString(2, ent_by);
		                  	stmt1.setBytes(3, base64bytes);
		                  	stmt1.setString(4, company_cd);
		                  	stmt1.setString(5, file_nm);
		                  	if(invoice_type.equals("Sales CR DR Note") 
		                  			|| invoice_type.equals("LTCORA(Sell) CR DR Note") 
		    						|| invoice_type.equals("Sales Late Payment") 
		    						|| invoice_type.equals("LTCORA(Sell) Late Payment"))
		                  	{
		                  		stmt1.executeUpdate();
		                  	}
		                  	else
		                  	{
		                  		st_respond = stmt1.executeUpdate();
		                  	}
							stmt1.close();
							
							String msg = file_nm+" Signed Successfully!";
							
							try
			        		{
			        			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(ent_by, company_cd, utilBean.getEmpName(conn,ent_by), request.getRemoteAddr(), "0", "PDF Signer","0","PDF Signer", "", "", msg);  	
			        		}
			        		catch(Exception infoLogger)
			        		{
			        			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
			        		}
						}
					}
				}
				else if(invoice_type.equals("LTCORA(Sell) Receipt Voucher") || invoice_type.equals("LTCORA(Sell) Refund Voucher"))
				{
					String queryString="SELECT C.FILE_NAME,C.PDF_SIGNED "
							+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B, FMS_SECURITY_FILE_DTL C "
							+ "WHERE A.COMPANY_CD=? AND A.GX=? AND C.FILE_TYPE='PDF' AND C.FILE_NAME=? "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.SEQ_NO=B.SEQ_NO AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX "
							+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
							+ "AND A.SEQ_NO=C.SEQ_NO AND A.SEQ_REV_NO=C.SEQ_REV_NO AND A.GX=C.GX ";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, "K");
					stmt.setString(3, file_nm);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						String temp_str=rset.getString(2)==null?"":rset.getString(2);
						//invice_no=rset.getString(3)==null?"":rset.getString(3);
						//invice_dt=rset.getString(4)==null?"":rset.getString(4);
						//counterParty_cd=rset.getString(5)==null?"":rset.getString(5);
						//agmtno=rset.getString(6)==null?"":rset.getString(6);
						//contno=rset.getString(7)==null?"":rset.getString(7);
						//cont_type=rset.getString(8)==null?"":rset.getString(8);
						//cargono=rset.getString(9)==null?"":rset.getString(9);
						
						if(temp_str.equals("") || temp_str.equals("N"))
						{
							String queryString2="UPDATE FMS_SECURITY_FILE_DTL SET PDF_SIGNED='Y',SIGNED_DT=sysdate,SIGNED_BY=?,"
									+ "SIGNED_ENT_BY=?,PDF_CONTENT=? "
		                  			+ "WHERE COMPANY_CD=? AND FILE_NAME =? ";
		                  	stmt1=conn.prepareStatement(queryString2);
		                  	stmt1.setString(1, signer_nm);
		                  	stmt1.setString(2, ent_by);
		                  	stmt1.setBytes(3, base64bytes);
		                  	stmt1.setString(4, company_cd);
		                  	stmt1.setString(5, file_nm);
							stmt1.executeUpdate();
							stmt1.close();
							
							String msg = file_nm+" Signed Successfully!";
							
							try
			        		{
			        			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(ent_by, company_cd, utilBean.getEmpName(conn,ent_by), request.getRemoteAddr(), "0", "PDF Signer","0","PDF Signer", "", "", msg);  	
			        		}
			        		catch(Exception infoLogger)
			        		{
			        			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
			        		}
						}
					}
				}
				else if(invoice_type.equals("Derivative"))
				{
					String queryString="SELECT A.FILE_NAME,A.PDF_SIGNED "
							//+ ",B.INVOICE_NO,TO_CHAR(B.INVOICE_DT,'DD/MM/YYYY'),"
							//+ "B.COUNTERPARTY_CD,B.AGMT_NO,B.CONT_NO,B.CONTRACT_TYPE,B.INSTRUMENT_NO "
            		  		+ "FROM FMS_DERV_INV_FILE_DTL A "
            		  		+ "WHERE A.COMPANY_CD=? AND A.FILE_NAME=? "
            		  		+ "AND (SELECT COUNT(*) FROM FMS_DERV_INVOICE_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
            		  		+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR) > 0 ";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, file_nm);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						String temp_str=rset.getString(2)==null?"":rset.getString(2);
						/*invice_no=rset.getString(3)==null?"":rset.getString(3);
						invice_dt=rset.getString(4)==null?"":rset.getString(4);
						counterParty_cd=rset.getString(5)==null?"":rset.getString(5);
						agmtno=rset.getString(6)==null?"":rset.getString(6);
						contno=rset.getString(7)==null?"":rset.getString(7);
						cont_type=rset.getString(8)==null?"":rset.getString(8);
						cargono=rset.getString(9)==null?"":rset.getString(9);
						*/
						if(temp_str.equals("") || temp_str.equals("N"))
						{
							String queryString2="UPDATE FMS_DERV_INV_FILE_DTL SET PDF_SIGNED='Y',SIGNED_DT=sysdate,SIGNED_BY=?,"
									+ "SIGNED_ENT_BY=?,PDF_CONTENT=? "
		                  			+ "WHERE COMPANY_CD=? AND FILE_NAME =? ";
		                  	stmt1=conn.prepareStatement(queryString2);
		                  	stmt1.setString(1, signer_nm);
		                  	stmt1.setString(2, ent_by);
		                  	stmt1.setBytes(3, base64bytes);
		                  	stmt1.setString(4, company_cd);
		                  	stmt1.setString(5, file_nm);
							stmt1.executeUpdate();
							stmt1.close();
							
							String msg = file_nm+" Signed Successfully!";
							
							try
			        		{
			        			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(ent_by, company_cd, utilBean.getEmpName(conn,ent_by), request.getRemoteAddr(), "0", "PDF Signer","0","PDF Signer", "", "", msg);  	
			        		}
			        		catch(Exception infoLogger)
			        		{
			        			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
			        		}
						}
					}
				}
				else if(invoice_type.equals("DLNG Sales") || invoice_type.equals("DLNG Sales CR DR Note") || invoice_type.equals("DLNG Sales Late Payment"))
				{
					String queryString="SELECT A.FILE_NAME,A.PDF_SIGNED,B.INVOICE_NO,TO_CHAR(B.INVOICE_DT,'DD/MM/YYYY'),"
							+ "B.COUNTERPARTY_CD,B.AGMT_NO,B.CONT_NO,B.CONTRACT_TYPE "
            		  		+ "FROM FMS_DLNG_INV_FILE_DTL A, FMS_DLNG_INVOICE_MST B "
            		  		+ "WHERE A.COMPANY_CD=? AND A.FILE_NAME=? "
            		  		+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
            		  		+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, file_nm);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						String temp_str=rset.getString(2)==null?"":rset.getString(2);
						invice_no=rset.getString(3)==null?"":rset.getString(3);
						invice_dt=rset.getString(4)==null?"":rset.getString(4);
						counterParty_cd=rset.getString(5)==null?"":rset.getString(5);
						agmtno=rset.getString(6)==null?"":rset.getString(6);
						contno=rset.getString(7)==null?"":rset.getString(7);
						cont_type=rset.getString(8)==null?"":rset.getString(8);
						//cargono=rset.getString(9)==null?"":rset.getString(9);
						
						if(temp_str.equals("") || temp_str.equals("N"))
						{
							String queryString2="UPDATE FMS_DLNG_INV_FILE_DTL SET PDF_SIGNED='Y',SIGNED_DT=sysdate,SIGNED_BY=?,"
									+ "SIGNED_ENT_BY=?,PDF_CONTENT=? "
		                  			+ "WHERE COMPANY_CD=? AND FILE_NAME =? ";
		                  	stmt1=conn.prepareStatement(queryString2);
		                  	stmt1.setString(1, signer_nm);
		                  	stmt1.setString(2, ent_by);
		                  	stmt1.setBytes(3, base64bytes);
		                  	stmt1.setString(4, company_cd);
		                  	stmt1.setString(5, file_nm);
		                  	if(invoice_type.equals("DLNG Sales CR DR Note") || invoice_type.equals("DLNG Sales Late Payment"))
		                  	{
		                  		stmt1.executeUpdate();
		                  	}
		                  	else
		                  	{
		                  		st_respond = stmt1.executeUpdate();
		                  	}
							stmt1.close();
							
							String msg = file_nm+" Signed Successfully!";
							
							try
			        		{
			        			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(ent_by, company_cd, utilBean.getEmpName(conn,ent_by), request.getRemoteAddr(), "0", "PDF Signer","0","PDF Signer", "", "", msg);  	
			        		}
			        		catch(Exception infoLogger)
			        		{
			        			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
			        		}
						}
					}
				}
				else if(invoice_type.equals("DLNG Service"))
				{
					String queryString="SELECT A.FILE_NAME,A.PDF_SIGNED,B.INVOICE_NO,TO_CHAR(B.INVOICE_DT,'DD/MM/YYYY'),"
							+ "B.COUNTERPARTY_CD,B.AGMT_NO,B.CONT_NO,B.CONTRACT_TYPE "
            		  		+ "FROM FMS_DLNG_SVC_INV_FILE_DTL A, FMS_DLNG_SVC_INVOICE_MST B "
            		  		+ "WHERE A.COMPANY_CD=? AND A.FILE_NAME=? "
            		  		+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
            		  		+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, file_nm);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						String temp_str=rset.getString(2)==null?"":rset.getString(2);
						invice_no=rset.getString(3)==null?"":rset.getString(3);
						invice_dt=rset.getString(4)==null?"":rset.getString(4);
						counterParty_cd=rset.getString(5)==null?"":rset.getString(5);
						agmtno=rset.getString(6)==null?"":rset.getString(6);
						contno=rset.getString(7)==null?"":rset.getString(7);
						cont_type=rset.getString(8)==null?"":rset.getString(8);
						//cargono=rset.getString(9)==null?"":rset.getString(9);
						
						if(temp_str.equals("") || temp_str.equals("N"))
						{
							String queryString2="UPDATE FMS_DLNG_SVC_INV_FILE_DTL SET PDF_SIGNED='Y',SIGNED_DT=sysdate,SIGNED_BY=?,"
									+ "SIGNED_ENT_BY=?,PDF_CONTENT=? "
		                  			+ "WHERE COMPANY_CD=? AND FILE_NAME =? ";
		                  	stmt1=conn.prepareStatement(queryString2);
		                  	stmt1.setString(1, signer_nm);
		                  	stmt1.setString(2, ent_by);
		                  	stmt1.setBytes(3, base64bytes);
		                  	stmt1.setString(4, company_cd);
		                  	stmt1.setString(5, file_nm);
							stmt1.executeUpdate();
							stmt1.close();
							
							String msg = file_nm+" Signed Successfully!";
							
							try
			        		{
			        			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(ent_by, company_cd, utilBean.getEmpName(conn,ent_by), request.getRemoteAddr(), "0", "PDF Signer","0","PDF Signer", "", "", msg);  	
			        		}
			        		catch(Exception infoLogger)
			        		{
			        			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
			        		}
						}
					}
				}
				else if(invoice_type.equals("Free flow"))
				{
					String queryString="SELECT A.FILE_NAME,A.PDF_SIGNED,B.INVOICE_NO,TO_CHAR(B.INVOICE_DT,'DD/MM/YYYY'),"
							+ "B.COUNTERPARTY_CD,B.AGMT_NO,B.CONT_NO,B.CONTRACT_TYPE,B.CARGO_NO "
							+ "FROM FMS_FFLOW_INV_FILE_DTL A , FMS_FFLOW_INV_MST B "
		    		  		+ "WHERE A.COMPANY_CD=? AND A.FILE_NAME=? "
		    		  		+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
		    		  		+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.INVOICE_TYPE=B.INVOICE_TYPE";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, file_nm);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						String temp_str=rset.getString(2)==null?"":rset.getString(2);
						invice_no=rset.getString(3)==null?"":rset.getString(3);
						invice_dt=rset.getString(4)==null?"":rset.getString(4);
						counterParty_cd=rset.getString(5)==null?"":rset.getString(5);
						agmtno=rset.getString(6)==null?"":rset.getString(6);
						contno=rset.getString(7)==null?"":rset.getString(7);
						cont_type=rset.getString(8)==null?"":rset.getString(8);
						cargono=rset.getString(9)==null?"":rset.getString(9);
						
						if(temp_str.equals("") || temp_str.equals("N"))
						{
							String queryString2="UPDATE FMS_FFLOW_INV_FILE_DTL SET PDF_SIGNED='Y',SIGNED_DT=sysdate,SIGNED_BY=?,"
									+ "SIGNED_ENT_BY=?,PDF_CONTENT=? "
		                  			+ "WHERE COMPANY_CD=? AND FILE_NAME = ? ";
							stmt1=conn.prepareStatement(queryString2);
		                  	stmt1.setString(1, signer_nm);
		                  	stmt1.setString(2, ent_by);
		                  	stmt1.setBytes(3, base64bytes);
		                  	stmt1.setString(4, company_cd);
		                  	stmt1.setString(5, file_nm);
							stmt1.executeUpdate();
							
							String msg = file_nm+" Signed Successfully!";
							
							try
			        		{
			        			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(ent_by, company_cd, utilBean.getEmpName(conn,ent_by), request.getRemoteAddr(), "0", "PDF Signer","0","PDF Signer", "", "", msg);  	
			        		}
			        		catch(Exception infoLogger)
			        		{
			        			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
			        		}
						}
					}
					rset.close();
					stmt.close();
				}
				else if(invoice_type.equals("DLNG Free flow"))
				{
					String queryString="SELECT A.FILE_NAME,A.PDF_SIGNED,B.INVOICE_NO,TO_CHAR(B.INVOICE_DT,'DD/MM/YYYY'),"
							+ "B.COUNTERPARTY_CD,B.AGMT_NO,B.CONT_NO,B.CONTRACT_TYPE,B.CARGO_NO "
							+ "FROM FMS_DLNG_FFLOW_INV_FILE_DTL A , FMS_DLNG_FFLOW_INV_MST B "
		    		  		+ "WHERE A.COMPANY_CD=? AND A.FILE_NAME=? "
		    		  		+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
		    		  		+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.INVOICE_TYPE=B.INVOICE_TYPE";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, file_nm);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						String temp_str=rset.getString(2)==null?"":rset.getString(2);
						invice_no=rset.getString(3)==null?"":rset.getString(3);
						invice_dt=rset.getString(4)==null?"":rset.getString(4);
						counterParty_cd=rset.getString(5)==null?"":rset.getString(5);
						agmtno=rset.getString(6)==null?"":rset.getString(6);
						contno=rset.getString(7)==null?"":rset.getString(7);
						cont_type=rset.getString(8)==null?"":rset.getString(8);
						cargono=rset.getString(9)==null?"":rset.getString(9);
						
						if(temp_str.equals("") || temp_str.equals("N"))
						{
							String queryString2="UPDATE FMS_DLNG_FFLOW_INV_FILE_DTL SET PDF_SIGNED='Y',SIGNED_DT=sysdate,SIGNED_BY=?,"
									+ "SIGNED_ENT_BY=?,PDF_CONTENT=? "
		                  			+ "WHERE COMPANY_CD=? AND FILE_NAME = ? ";
							stmt1=conn.prepareStatement(queryString2);
		                  	stmt1.setString(1, signer_nm);
		                  	stmt1.setString(2, ent_by);
		                  	stmt1.setBytes(3, base64bytes);
		                  	stmt1.setString(4, company_cd);
		                  	stmt1.setString(5, file_nm);
							stmt1.executeUpdate();
							
							String msg = file_nm+" Signed Successfully!";
							
							try
			        		{
			        			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(ent_by, company_cd, utilBean.getEmpName(conn,ent_by), request.getRemoteAddr(), "0", "PDF Signer","0","PDF Signer", "", "", msg);  	
			        		}
			        		catch(Exception infoLogger)
			        		{
			        			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
			        		}
						}
					}
					rset.close();
					stmt.close();
				}
				else if(invoice_type.equals("IG Finance"))
				{
					String queryString="SELECT A.FILE_NAME,A.PDF_SIGNED,B.INVOICE_NO,TO_CHAR(B.INVOICE_DT,'DD/MM/YYYY') "
            		  		+ "FROM FMS_OTH_INV_FILE_DTL A, FMS_OTH_INVOICE_MST B "
            		  		+ "WHERE A.COMPANY_CD=? AND A.FILE_NAME=? "
            		  		+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.INVOICE_SEQ=B.INVOICE_SEQ "
	                    	+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.SUPPLIER_CD=B.SUPPLIER_CD "
	                    	+ "AND A.INVOICE_TYPE=B.INVOICE_TYPE ";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, file_nm);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						String temp_str=rset.getString(2)==null?"":rset.getString(2);
						invice_no=rset.getString(3)==null?"":rset.getString(3);
						invice_dt=rset.getString(4)==null?"":rset.getString(4);
						//counterParty_cd=rset.getString(5)==null?"":rset.getString(5);
						//agmtno=rset.getString(6)==null?"":rset.getString(6);
						//contno=rset.getString(7)==null?"":rset.getString(7);
						//cont_type=rset.getString(8)==null?"":rset.getString(8);
						//cargono=rset.getString(9)==null?"":rset.getString(9);
						
						if(temp_str.equals("") || temp_str.equals("N"))
						{
							String queryString2="UPDATE FMS_OTH_INV_FILE_DTL SET PDF_SIGNED='Y',SIGNED_DT=sysdate,SIGNED_BY=?,"
									+ "SIGNED_ENT_BY=?,PDF_CONTENT=? "
		                  			+ "WHERE COMPANY_CD=? AND FILE_NAME =? ";
		                  	stmt1=conn.prepareStatement(queryString2);
		                  	stmt1.setString(1, signer_nm);
		                  	stmt1.setString(2, ent_by);
		                  	stmt1.setBytes(3, base64bytes);
		                  	stmt1.setString(4, company_cd);
		                  	stmt1.setString(5, file_nm);
		                  	stmt1.executeUpdate();
							stmt1.close();
							
							String msg = file_nm+" Signed Successfully!";
							
							try
			        		{
			        			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(ent_by, company_cd, utilBean.getEmpName(conn,ent_by), request.getRemoteAddr(), "0", "PDF Signer","0","PDF Signer", "", "", msg);  	
			        		}
			        		catch(Exception infoLogger)
			        		{
			        			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
			        		}
						}
					}
				}
				
				if(st_respond>0)
				{
					if(file_nm.startsWith("O-"))
					{
						DocumentBuilderFactory docFactory = xmlUtil.dcoumentBuilderFactory();
					    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

					    Document doc = docBuilder.newDocument();
					    Element ems = doc.createElement("EMS");
					    doc.appendChild(ems);
					    Element invoice_dtl = doc.createElement("INVOICE_DTL");
					    ems.appendChild(invoice_dtl);
					    
					    Element INVOICE = doc.createElement("INVOICE");
					    invoice_dtl.appendChild(INVOICE);
						
						Element AGMT_CONT_NO = doc.createElement("AGMT_CONT_NO");
					    Element INVOICE_DATE = doc.createElement("INVOICE_DATE");
					    Element INVOICE_ID = doc.createElement("INVOICE_ID");
					    Element INVOICE_FILE_TYPE = doc.createElement("INVOICE_FILE_TYPE");
					    Element INVOICE_FILE_NAME = doc.createElement("INVOICE_FILE_NAME");
					    Element INVOICE_CONTENT_DATA = doc.createElement("INVOICE_CONTENT_DATA");
					    Element INVOICE_TYPE = doc.createElement("INVOICE_TYPE");
					    Element INVOICE_SIGN = doc.createElement("INVOICE_SIGN");
					    
					    INVOICE.appendChild(AGMT_CONT_NO);
					    INVOICE.appendChild(INVOICE_DATE);
					    INVOICE.appendChild(INVOICE_ID);
					    INVOICE.appendChild(INVOICE_FILE_TYPE);
					    INVOICE.appendChild(INVOICE_FILE_NAME);
					    INVOICE.appendChild(INVOICE_CONTENT_DATA);
					    INVOICE.appendChild(INVOICE_TYPE);
					    INVOICE.appendChild(INVOICE_SIGN);
					    
					    String dealMapping =utilBean.SalesforceDealMapping(company_cd, counterParty_cd, agmtno,"0", contno, "0", cont_type, cargono);
					    AGMT_CONT_NO.appendChild(doc.createTextNode(""+dealMapping));
					    INVOICE_DATE.appendChild(doc.createTextNode(""+invice_dt));
					    INVOICE_ID.appendChild(doc.createTextNode(""+invice_no));
					    INVOICE_FILE_TYPE.appendChild(doc.createTextNode("PDF"));
					    INVOICE_FILE_NAME.appendChild(doc.createTextNode(""+file_nm));
					    INVOICE_CONTENT_DATA.appendChild(doc.createTextNode(""+encodedPdf));
					    INVOICE_TYPE.appendChild(doc.createTextNode("SALES INVOICE"));
					    INVOICE_SIGN.appendChild(doc.createTextNode(""+utilDate.getSysdate()));
					    
					    TransformerFactory transformerFactory = TransformerFactory.newInstance();
						transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD","");
						transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet","");
						
					    Transformer transformer = transformerFactory.newTransformer();
					    DOMSource source = new DOMSource(doc);
					    
					    String sysdateWithTime=utilDate.getSysdateWithTime24hrWithSS();
					    String[] splitSys = sysdateWithTime.split(" ");
					    String[] dateSplit = splitSys[0].split("/");
					    String datetime=dateSplit[2]+""+dateSplit[1]+""+dateSplit[0]+"_"+splitSys[1].replaceAll(":", "");
					    String xmlFileNm="Invoice_"+datetime+".xml";
					    
					    String xmlfilepath=utilBean.getAutomationKeyDetail(conn, "SF_XML_OUTBOUND_"+company_cd);
						File MainDir = new File(xmlfilepath);
				        if(!MainDir.exists()) 
				        {
				        	MainDir.mkdirs();
				        }
				        xmlfilepath=xmlfilepath+File.separator+""+xmlFileNm;
				        StreamResult result =  new StreamResult(new File(xmlfilepath));
					    transformer.transform(source, result);
					    
					    if(new File(xmlfilepath).exists())
					    {
					    	if(invoice_type.equals("Sales") 
									|| invoice_type.equals("LTCORA(Sell)") 
									|| invoice_type.equals("LTCORA(Sell) SUG") 
									|| invoice_type.equals("LTCORA(Sell) Storage"))
							{
							    String queryString2="UPDATE FMS_INV_FILE_DTL SET SF_GEN_DT=sysdate "
			                  			+ "WHERE COMPANY_CD=? AND FILE_NAME =? ";
			                  	stmt1=conn.prepareStatement(queryString2);
			                  	stmt1.setString(1, company_cd);
			                  	stmt1.setString(2, file_nm);
			                  	stmt1.executeUpdate();
								stmt1.close();
							}
					    	else if(invoice_type.equals("DLNG Sales"))
							{
					    		 String queryString2="UPDATE FMS_DLNG_INV_FILE_DTL SET SF_GEN_DT=sysdate "
				                  			+ "WHERE COMPANY_CD=? AND FILE_NAME =? ";
				                  	stmt1=conn.prepareStatement(queryString2);
				                  	stmt1.setString(1, company_cd);
				                  	stmt1.setString(2, file_nm);
				                  	stmt1.executeUpdate();
									stmt1.close();
							}
					    	else if(invoice_type.equals("Free flow"))
							{
					    		String queryString2="UPDATE FMS_FFLOW_INV_FILE_DTL SET SF_GEN_DT=sysdate "
			                  			+ "WHERE COMPANY_CD=? AND FILE_NAME =? ";
			                  	stmt1=conn.prepareStatement(queryString2);
			                  	stmt1.setString(1, company_cd);
			                  	stmt1.setString(2, file_nm);
			                  	stmt1.executeUpdate();
								stmt1.close();
							}	
					    }
					}
				}
			}
			else
			{
				
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void init(HttpServletRequest request, HttpServletResponse response) 
	{
		String function_nm="init()";
		synchronized(this)
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
		    			if(temp_option.equalsIgnoreCase("Login"))
		    			{
		    				LoginCheck(request, response);
		    			}
		    			else if(temp_option.equalsIgnoreCase("UnsignedPDF"))
		    			{
		    				getUnsignedPDFlist(request, response);
		    			}
		    			else if(temp_option.equalsIgnoreCase("SignedPDF"))
		    			{
		    				updateSignedPDFDetail(request, response);
		    			}
		    			else if(temp_option.equalsIgnoreCase("company_mst"))
		    			{
		    				getCompanyMaster(request,response);
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
	}
}
