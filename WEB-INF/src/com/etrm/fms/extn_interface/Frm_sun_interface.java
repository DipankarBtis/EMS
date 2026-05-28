package com.etrm.fms.extn_interface;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.escapeSingleQuotes;

@WebServlet("/servlet/Frm_sun_interface")
public class Frm_sun_interface extends HttpServlet
{
	static String frm_src_file_name="Frm_sun_interface";
	public static  Connection dbcon;
	
	public static String option = "";
	public static String url = "";
	public static String msg = "";
	public static String msg_type = "";
	public static String err_msg = "";
	
	private static String queryString = null;
	private static String query = null;
	private static String query1 = null;
	private static String query2 = null;
	
	private static PreparedStatement stmt = null;
	private static PreparedStatement stmt0 = null;
	private static PreparedStatement stmt1 = null;
	private static PreparedStatement stmt2 = null;
	private static PreparedStatement stmt3 = null;
	private static PreparedStatement stmt4 = null;
	private static PreparedStatement stmt5 = null;
	private static PreparedStatement stmt6 = null;
	
	private static ResultSet rset = null;
	private static ResultSet rset0 = null;
	private static ResultSet rset1 = null;
	private static ResultSet rset2 = null;
	private static ResultSet rset3 = null;
	private static ResultSet rset4 = null;
	private static ResultSet rset5 = null;
	private static ResultSet rset6 = null;
	
	public static String form_id = "0";
	public static String form_nm = "";
	public static String mod_cd = "0";
	public static String mod_nm = "";
	public static String u = "";
	
	public static String old_value="";
	public static String new_value="";
	
	public static String emp_cd="";
	public static String comp_cd="";
	public static String comp_abbr="";
	public static String emp_nm="";
	public static String ip="";
	
	public static String commonUrl_pra="";
	
	public static escapeSingleQuotes escObj = new escapeSingleQuotes();
	
	static UtilBean utilBean = new UtilBean();
	static DateUtil utilDate = new DateUtil();
	static DataBean_sun_interface DBsun = new DataBean_sun_interface();
	
	public static NumberFormat nf = new DecimalFormat("###########0.00");
	public static NumberFormat nf2 = new DecimalFormat("###########0.0000");
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException
	{
		String function_nm="doPost()";
		HttpSession session = request.getSession();
		if(session.getAttribute("emp_uid")==null || session.getAttribute("emp_uid")=="")
		{
			url="../sess/Expire.jsp";
		}
		else
		{
			try
			{
				Context Context = new InitialContext();
				
				Context envContext  = (Context)Context.lookup("java:/comp/env");
				DataSource ds = (DataSource)envContext.lookup(RuntimeConf.security_database);
				
				if(ds != null)
				{
					dbcon = ds.getConnection();				
				}
				else
				{
					System.out.println("Data Source Not Found");
				}
				if(dbcon != null)
				{
					dbcon.setAutoCommit(false);
					
					form_id=request.getParameter("form_cd")==null?"0":request.getParameter("form_cd");
					form_nm=request.getParameter("form_nm")==null?"":request.getParameter("form_nm");
					mod_cd=request.getParameter("mod_cd")==null?"0":request.getParameter("mod_cd");
					mod_nm=request.getParameter("mod_nm")==null?"":request.getParameter("mod_nm");
					
					emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
					comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
					comp_abbr = (String)session.getAttribute("comp_abbr")==null?"":(String)session.getAttribute("comp_abbr");
					emp_nm = (String)session.getAttribute("emp_nm")==null?"":(String)session.getAttribute("emp_nm");
					ip = (String)session.getAttribute("ip")==null?"":(String)session.getAttribute("ip");
					u=request.getParameter("u")==null?"":request.getParameter("u");
					
					new_value="";
					old_value="";
					
					option=request.getParameter("option")==null?"":request.getParameter("option");
					
					commonUrl_pra = "&u="+u;
					
					if(option.equalsIgnoreCase("TAX_STRUCTURE_SUN"))
					{
						InsertUpdateTaxSturctureSunDtls(request);
					}
					else if(option.equalsIgnoreCase("SUN_ENTITY_ACC_CD"))
					{
						InsertUpdateSunEntityAccountCode(request);
					}
					else if(option.equalsIgnoreCase("GENERATE_SUN_XML"))
					{
						GenerateSunXML(request);
					}
					else if(option.equalsIgnoreCase("APPROVE_SALE_INVOICE_SUN_XML"))
					{
						ApproveSunXML(request);
					}
					else if(option.equalsIgnoreCase("APPROVE_PURCHASE_INVOICE_SUN_XML"))
					{
						ApprovePurSunXML(request);
					}
					else if(option.equalsIgnoreCase("GENERATE_PUR_SUN_XML"))
					{
						GeneratePurSunXML(request);
					}
					else if(option.equalsIgnoreCase("RE-APPROVE_SALE_INVOICE_SUN_XML"))
					{
						ReapproveSalesInvoice(request);
					}
					else if(option.equalsIgnoreCase("REAPPROVE_PURCHASE_INVOICE_SUN_XML"))	
					{
						ReapprovePurSunXML(request);
					}
				}
				dbcon.close();
				dbcon=null;
			}
			catch(Exception e)
			{
				new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
				url=CommonVariable.errorpage_url+"?e="+e;
			}
			finally
			{
				if(rset != null){try {rset.close();}catch(SQLException e){System.out.println("rset is not close " + e);}}
				if(rset0 != null){try {rset0.close();}catch(SQLException e){System.out.println("rset0 is not close " + e);}}
				if(rset1 != null){try {rset1.close();}catch(SQLException e){System.out.println("rset1 is not close " + e);}}
				if(rset2 != null){try {rset2.close();}catch(SQLException e){System.out.println("rset2 is not close " + e);}}
				if(rset3 != null){try {rset3.close();}catch(SQLException e){System.out.println("rset3 is not close " + e);}}
				if(rset4 != null){try {rset4.close();}catch(SQLException e){System.out.println("rset4 is not close " + e);}}
				if(rset5 != null){try {rset5.close();}catch(SQLException e){System.out.println("rset5 is not close " + e);}}
				if(rset6 != null){try {rset6.close();}catch(SQLException e){System.out.println("rset6 is not close " + e);}}
				if(stmt != null){try {stmt.close();}catch(SQLException e){System.out.println("stmt is not close " + e);}}
				if(stmt0 != null){try {stmt0.close();}catch(SQLException e){System.out.println("stmt0 is not close " + e);}}
				if(stmt1 != null){try {stmt1.close();}catch(SQLException e){System.out.println("stmt1 is not close " + e);}}
				if(stmt2 != null){try {stmt2.close();}catch(SQLException e){System.out.println("stmt2 is not close " + e);}}
				if(stmt3 != null){try {stmt3.close();}catch(SQLException e){System.out.println("stmt3 is not close " + e);}}
				if(stmt4 != null){try {stmt4.close();}catch(SQLException e){System.out.println("stmt4 is not close " + e);}}
				if(stmt5 != null){try {stmt5.close();}catch(SQLException e){System.out.println("stmt5 is not close " + e);}}
				if(stmt6 != null){try {stmt6.close();}catch(SQLException e){System.out.println("stmt6 is not close " + e);}}
				if(dbcon != null){try {dbcon.close();}catch(SQLException e){System.out.println("conn is not close " + e);}}
			}
		}
		try 
		{
			response.sendRedirect(url);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
		}
	}
	
	private void InsertUpdateSunEntityAccountCode(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateSunEntityAccountCode()";
		msg="";
		msg_type="";
		url="";
		try
		{
			String entity_role= request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");
			String index_arr = request.getParameter("index")==null?"":request.getParameter("index");
			String acc_type_count = request.getParameter("acc_type_count")==null?"":request.getParameter("acc_type_count");
			
			String [] counterparty_cd = request.getParameterValues("counterparty_cd");
			String [] sun_account = request.getParameterValues("sun_account_"+entity_role);
			String [] plant_count = request.getParameterValues("plant_count");
			String [] sun_plant_account = request.getParameterValues("sun_plant_account_"+entity_role);
			String [] plant_seq_no = request.getParameterValues("plant_seq_no");
			
			String [] index = index_arr.split(",");
			
			int n=0;
			if(index!=null && counterparty_cd!=null && !entity_role.equals("0"))
			{
				for(int i=0;i<index.length;i++)
				{
					int indx = Integer.parseInt(index[i]);
					
					if(entity_role.equals("T"))	
					{
						int pt_ctn=Integer.parseInt(plant_count[indx]);
						for(int a=0;a<pt_ctn;a++)
						{
							String [] plant_map = request.getParameterValues("plant_map_"+counterparty_cd[indx]);
							
							if(!sun_plant_account[n].equals(""))
							{
								int count=0;
								String query1="SELECT COUNT(*) FROM FMS_ENTITY_ACCOUNT_CODE_SUN "
										+ "	WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=? AND ACCOUNT_TYPE=? AND PLANT_SEQ_NO=? ";
								stmt3=dbcon.prepareStatement(query1);
								stmt3.setString(1, comp_cd);
								stmt3.setString(2, counterparty_cd[indx]);
								stmt3.setString(3, entity_role);
								stmt3.setString(4, "DFT");
								stmt3.setString(5, plant_map[a]);
								rset3=stmt3.executeQuery();
								if(rset3.next())
								{
									count=rset3.getInt(1);
								}
								rset3.close();
								stmt3.close();
								
								if(count>0)
								{
									query1="UPDATE FMS_ENTITY_ACCOUNT_CODE_SUN "
											+ "SET ACCOUNT_CODE=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
											+ "WHERE COMPANY_CD=? AND ENTITY=? AND ACCOUNT_TYPE=? AND COUNTERPARTY_CD=?  "
											+ "AND PLANT_SEQ_NO=?";
									stmt3=dbcon.prepareStatement(query1);
									stmt3.setString(1, sun_plant_account[n]);
									stmt3.setString(2, emp_cd);
									stmt3.setString(3, comp_cd);
									stmt3.setString(4, comp_cd);
									stmt3.setString(5, entity_role);
									stmt3.setString(6, "DFT");
									stmt3.setString(7, counterparty_cd[indx]);
									stmt3.setString(8, plant_map[a]);
									stmt3.executeUpdate();
									stmt3.close();
									
								}
								else
								{
									query1="INSERT INTO FMS_ENTITY_ACCOUNT_CODE_SUN(COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,ACCOUNT_TYPE,ACCOUNT_CODE,ENT_PROFILE, "
											+ "ENT_BY,ENT_DT) "
											+ "VALUES(?,?,?,?,?,?,?,?,SYSDATE)";
									stmt3=dbcon.prepareStatement(query1);
									stmt3.setString(1, comp_cd);
									stmt3.setString(2, counterparty_cd[indx]);
									stmt3.setString(3, entity_role);
									stmt3.setString(4, plant_map[a]);
									stmt3.setString(5, "DFT");
									stmt3.setString(6, sun_plant_account[n]);
									stmt3.setString(7, comp_cd);
									stmt3.setString(8, emp_cd);
									stmt3.executeUpdate();
									stmt3.close();
								}
							}
							
							
							for(int j=0;j<Integer.parseInt(acc_type_count);j++)
							{
								String [] sun_entity_plant_account = request.getParameterValues("sun_entity_plant_account_"+entity_role+"_"+j);
								String account_type = getAccountType(entity_role,j);
								
								if(!sun_entity_plant_account[n].equals(""))
								{
									int count=0;
									query1="SELECT COUNT(*) FROM FMS_ENTITY_ACCOUNT_CODE_SUN "
											+ "	WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=? AND ACCOUNT_TYPE=? AND PLANT_SEQ_NO=? ";
									stmt3=dbcon.prepareStatement(query1);
									stmt3.setString(1, comp_cd);
									stmt3.setString(2, counterparty_cd[indx]);
									stmt3.setString(3, entity_role);
									stmt3.setString(4, account_type);
									stmt3.setString(5, plant_map[a]);
									rset3=stmt3.executeQuery();
									if(rset3.next())
									{
										count=rset3.getInt(1);
									}
									rset3.close();
									stmt3.close();
									
									if(count>0)
									{
										query1="UPDATE FMS_ENTITY_ACCOUNT_CODE_SUN "
												+ "SET ACCOUNT_CODE=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
												+ "WHERE COMPANY_CD=? AND ENTITY=? AND ACCOUNT_TYPE=? AND COUNTERPARTY_CD=?  "
												+ "AND PLANT_SEQ_NO=?";
										stmt3=dbcon.prepareStatement(query1);
										stmt3.setString(1, sun_entity_plant_account[n]);
										stmt3.setString(2, emp_cd);
										stmt3.setString(3, comp_cd);
										stmt3.setString(4, comp_cd);
										stmt3.setString(5, entity_role);
										stmt3.setString(6, account_type);
										stmt3.setString(7, counterparty_cd[indx]);
										stmt3.setString(8, plant_map[a]);
										stmt3.executeUpdate();
										stmt3.close();
									}
									else
									{
										query1="INSERT INTO FMS_ENTITY_ACCOUNT_CODE_SUN(COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,ACCOUNT_TYPE,ACCOUNT_CODE,ENT_PROFILE, "
												+ "ENT_BY,ENT_DT) "
												+ "VALUES(?,?,?,?,?,?,?,?,SYSDATE)";
										stmt3=dbcon.prepareStatement(query1);
										stmt3.setString(1, comp_cd);
										stmt3.setString(2, counterparty_cd[indx]);
										stmt3.setString(3, entity_role);
										stmt3.setString(4, plant_map[a]);
										stmt3.setString(5, account_type);
										stmt3.setString(6, sun_entity_plant_account[n]);
										stmt3.setString(7, comp_cd);
										stmt3.setString(8, emp_cd);
										stmt3.executeUpdate();
										stmt3.close();
									}
								}
							}
							n++;
						}
					}
					
					if(!sun_account[i].equals(""))
					{
						int count=0;
						String query="SELECT COUNT(*) FROM FMS_ENTITY_ACCOUNT_CODE_SUN "
								+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND ACCOUNT_TYPE=? "
								+ "AND COMPANY_CD=? AND PLANT_SEQ_NO=?";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, counterparty_cd[indx]);
						stmt.setString(2, entity_role);
						stmt.setString(3, "DFT");
						stmt.setString(4, comp_cd);
						stmt.setString(5, "0");
						rset=stmt.executeQuery();
						if(rset.next())
						{
							count=rset.getInt(1);
						}
						rset.close();
						stmt.close();
						
						if(count>0)
						{
							String queryString="UPDATE FMS_ENTITY_ACCOUNT_CODE_SUN "
									+ "SET ACCOUNT_CODE=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
									+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND ACCOUNT_TYPE=? "
									+ "AND COMPANY_CD=? AND PLANT_SEQ_NO=?";
							stmt1=dbcon.prepareStatement(queryString);
							stmt1.setString(1, sun_account[i]);
							stmt1.setString(2, emp_cd);
							stmt1.setString(3, comp_cd);
							stmt1.setString(4, counterparty_cd[indx]);
							stmt1.setString(5, entity_role);
							stmt1.setString(6, "DFT");
							stmt1.setString(7, comp_cd);
							stmt1.setString(8, "0");
							stmt1.executeUpdate();
							stmt1.close();
						}
						else
						{
							String queryString="INSERT INTO FMS_ENTITY_ACCOUNT_CODE_SUN(COUNTERPARTY_CD,ENTITY,ACCOUNT_TYPE,"
									+ "ACCOUNT_CODE,ENT_PROFILE,ENT_BY,ENT_DT,COMPANY_CD,PLANT_SEQ_NO) "
									+ "VALUES(?,?,?,?,?,?,SYSDATE,?,?)";
							stmt1=dbcon.prepareStatement(queryString);
							stmt1.setString(1, counterparty_cd[indx]);
							stmt1.setString(2, entity_role);
							stmt1.setString(3, "DFT");
							stmt1.setString(4, sun_account[i]);
							stmt1.setString(5, comp_cd);
							stmt1.setString(6, emp_cd);
							stmt1.setString(7, comp_cd);
							stmt1.setString(8, "0");
							stmt1.executeUpdate();
							stmt1.close();
						}
					}
					
					for(int j=0;j<Integer.parseInt(acc_type_count);j++)
					{
						String [] sun_entity_account = request.getParameterValues("sun_entity_account_"+entity_role+"_"+indx+"_"+j);
						
						String account_type = getAccountType(entity_role,j);
						
						for(int k=0;k<sun_entity_account.length;k++)
						{
							if(!sun_entity_account[k].equals(""))
							{
								int count=0;
								String query="SELECT COUNT(*) FROM FMS_ENTITY_ACCOUNT_CODE_SUN "
										+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND ACCOUNT_TYPE=? "
										+ "AND COMPANY_CD=? AND PLANT_SEQ_NO=?";
								stmt=dbcon.prepareStatement(query);
								stmt.setString(1, counterparty_cd[indx]);
								stmt.setString(2, entity_role);
								stmt.setString(3, account_type);
								stmt.setString(4, comp_cd);
								stmt.setString(5, "0");
								rset=stmt.executeQuery();
								if(rset.next())
								{
									count=rset.getInt(1);
								}
								rset.close();
								stmt.close();
								
								if(count>0)
								{
									String queryString="UPDATE FMS_ENTITY_ACCOUNT_CODE_SUN "
											+ "SET ACCOUNT_CODE=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
											+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND ACCOUNT_TYPE=? "
											+ "AND COMPANY_CD=? AND PLANT_SEQ_NO=?";
									stmt1=dbcon.prepareStatement(queryString);
									stmt1.setString(1, sun_entity_account[k]);
									stmt1.setString(2, emp_cd);
									stmt1.setString(3, comp_cd);
									stmt1.setString(4, counterparty_cd[indx]);
									stmt1.setString(5, entity_role);
									stmt1.setString(6, account_type);
									stmt1.setString(7, comp_cd);
									stmt1.setString(8, "0");
									stmt1.executeUpdate();
									stmt1.close();
								}
								else
								{
									String queryString="INSERT INTO FMS_ENTITY_ACCOUNT_CODE_SUN(COUNTERPARTY_CD,ENTITY,ACCOUNT_TYPE,"
											+ "ACCOUNT_CODE,ENT_PROFILE,ENT_BY,ENT_DT,COMPANY_CD,PLANT_SEQ_NO) "
											+ "VALUES(?,?,?,?,?,?,SYSDATE,?,?)";
									stmt1=dbcon.prepareStatement(queryString);
									stmt1.setString(1, counterparty_cd[indx]);
									stmt1.setString(2, entity_role);
									stmt1.setString(3, account_type);
									stmt1.setString(4, sun_entity_account[k]);
									stmt1.setString(5, comp_cd);
									stmt1.setString(6, emp_cd);
									stmt1.setString(7, comp_cd);
									stmt1.setString(8, "0");
									stmt1.executeUpdate();
									stmt1.close();
								}
							}
						}
					}
				}
				msg = "Successful! Insert/Update operation is done successfully for Tax Structure Sun!";
				msg_type = "S";
			}
			else
			{
				msg = "Failed! - Insert/Update Sun Entity Account Code failed!";
				msg_type="E";
			}
			
			url="../extn_interface/frm_entity_sun_account_code.jsp?entity_role="+entity_role+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Insert/Update failed for Sun Entity Accout Code!";
		}
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateTaxSturctureSunDtls(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateTaxSturctureSunDtls()";
		msg="";
		msg_type="";
		url="";
		try
		{
			Vector VBU_SEQ = new Vector();
			Vector VBU_TIN = new Vector();
			
			String index_arr = request.getParameter("index")==null?"":request.getParameter("index");
			
			String [] index=index_arr.split(",");
			String [] size = request.getParameterValues("size");
			String [] vtax_struct_cd = request.getParameterValues("vtax_struct_cd");
			
			String queryString="SELECT A.SEQ_NO,B.TIN "
					+ "FROM FMS_COUNTERPARTY_PLANT_DTL A, FMS_STATE_MST B "
					+ "WHERE COMPANY_CD=? AND ENTITY=? AND A.PLANT_STATE=B.STATE_NM "
					+ "AND A.EFF_DT = (SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.ENTITY=B.ENTITY AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.SEQ_NO=B.SEQ_NO)";
			stmt0 = dbcon.prepareStatement(queryString);
			stmt0.setString(1, comp_cd);
			stmt0.setString(2, "B");
			rset0 = stmt0.executeQuery();
			while(rset0.next())
			{
				String seq_no= rset0.getString(1)==null?"":rset0.getString(1);
				String bu_tin=rset0.getString(2)==null?"":rset0.getString(2);
				
				VBU_SEQ.add(seq_no);
				VBU_TIN.add(bu_tin);
			}
			rset0.close();
			stmt0.close();
			
			if(index!=null && VBU_SEQ.size()>0)
			{
				int [] k=new int[VBU_SEQ.size()];
				int [] l=new int[VBU_SEQ.size()];
				for(int i=0;i<index.length;i++)
				{
					int sun_indx = Integer.parseInt(index[i]);
					int sz = Integer.parseInt(size[sun_indx]);
					for(int j=0;j<VBU_SEQ.size();j++)
					{
						String [] sun_cd = request.getParameterValues("sun_cd_"+VBU_SEQ.elementAt(j));
						String [] sug_cd = request.getParameterValues("sug_cd_"+VBU_SEQ.elementAt(j));
						
						if (sz > 1) 
						{
							String query = "SELECT TAX_CODE FROM FMS_TAX_STRUCTURE_DTL " 
									+ "WHERE TAX_STR_CD=? ";
							stmt1 = dbcon.prepareStatement(query);
							stmt1.setString(1, vtax_struct_cd[sun_indx]);
							rset1 = stmt1.executeQuery();
							while (rset1.next()) 
							{
								String tax_cd = rset1.getString(1) == null ? "" : rset1.getString(1);
								if(!sun_cd[k[j]].equals(""))
								{
									int count=0;
									String query1="SELECT COUNT(*) FROM FMS_TAX_STRUCTURE_SUN "
											+ "WHERE COMPANY_CD=? AND TAX_STR_CD=? AND TAX_CODE=? AND BU_UNIT=? AND ACCOUNT_TYPE=? ";
									stmt4=dbcon.prepareStatement(query1);
									stmt4.setString(1, comp_cd);
									stmt4.setString(2, vtax_struct_cd[sun_indx]);
									stmt4.setString(3, tax_cd);
									stmt4.setString(4, VBU_SEQ.elementAt(j).toString());
									stmt4.setString(5, "S");
									rset4=stmt4.executeQuery();
									if(rset4.next())
									{
										count=rset4.getInt(1);
									}
									rset4.close();
									stmt4.close();
									
									if(count>0)
									{
										String query2="UPDATE FMS_TAX_STRUCTURE_SUN "
												+ "SET SUN_CODE=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
												+ "WHERE TAX_STR_CD=? AND TAX_CODE=? AND BU_UNIT=? AND COMPANY_CD=? AND ACCOUNT_TYPE=? ";
										stmt5=dbcon.prepareStatement(query2);
										stmt5.setString(1,  sun_cd[k[j]]);
										stmt5.setString(2, emp_cd);
										stmt5.setString(3, comp_cd);
										stmt5.setString(4, vtax_struct_cd[sun_indx]);
										stmt5.setString(5, tax_cd);
										stmt5.setString(6, VBU_SEQ.elementAt(j).toString());
										stmt5.setString(7, comp_cd);
										stmt5.setString(8, "S");
										stmt5.executeUpdate();
										stmt5.close();
									}
									else
									{
										String queryString2="INSERT INTO FMS_TAX_STRUCTURE_SUN(TAX_STR_CD,TAX_CODE,BU_UNIT,BU_STATE_TIN,SUN_CODE,ENT_DT,ENT_BY,ENT_PROFILE,COMPANY_CD,ACCOUNT_TYPE) "
												+ "VALUES(?,?,?,?,?,SYSDATE,?,?,?,?)";
										stmt=dbcon.prepareStatement(queryString2);
										stmt.setString(1,  vtax_struct_cd[sun_indx]);
										stmt.setString(2,  tax_cd);
										stmt.setString(3, VBU_SEQ.elementAt(j).toString());
										stmt.setString(4, VBU_TIN.elementAt(j).toString());
										stmt.setString(5, sun_cd[k[j]]);
										stmt.setString(6, emp_cd);
										stmt.setString(7, comp_cd);
										stmt.setString(8, comp_cd);
										stmt.setString(9, "S");
										stmt.executeUpdate();
										
										stmt.close();
									}
								}
								k[j]++;
								continue;
							}
							rset1.close();
							stmt1.close();
						}
						else
						{
							if(!sun_cd[l[j]].equals(""))
							{
								String tax_cd="";
								String query = "SELECT TAX_CODE FROM FMS_TAX_STRUCTURE_DTL " 
										+ "WHERE TAX_STR_CD=? ";
								stmt1 = dbcon.prepareStatement(query);
								stmt1.setString(1, vtax_struct_cd[sun_indx]);
								rset1 = stmt1.executeQuery();
								if(rset1.next())
								{
									tax_cd = rset1.getString(1) == null ? "" : rset1.getString(1);
								}
								rset1.close();
								stmt1.close();
								
								int count=0;
								String query1="SELECT COUNT(*) FROM FMS_TAX_STRUCTURE_SUN "
										+ "WHERE TAX_STR_CD=? AND TAX_CODE=? AND BU_UNIT=? AND COMPANY_CD=? AND ACCOUNT_TYPE=? ";
								stmt4=dbcon.prepareStatement(query1);
								stmt4.setString(1, vtax_struct_cd[sun_indx]);
								stmt4.setString(2, tax_cd);
								stmt4.setString(3, VBU_SEQ.elementAt(j).toString());
								stmt4.setString(4, comp_cd);
								stmt4.setString(5, "S");
								rset4=stmt4.executeQuery();
								if(rset4.next())
								{
									count=rset4.getInt(1);
								}
								rset4.close();
								stmt4.close();
								
								if(count>0)
								{
									String query2="UPDATE FMS_TAX_STRUCTURE_SUN "
											+ "SET SUN_CODE=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
											+ "WHERE TAX_STR_CD=? AND TAX_CODE=? AND BU_UNIT=? AND COMPANY_CD=? AND ACCOUNT_TYPE=?";
									stmt5=dbcon.prepareStatement(query2);
									stmt5.setString(1,  sun_cd[k[j]]);
									stmt5.setString(2, emp_cd);
									stmt5.setString(3, comp_cd);
									stmt5.setString(4, vtax_struct_cd[sun_indx]);
									stmt5.setString(5, tax_cd);
									stmt5.setString(6, VBU_SEQ.elementAt(j).toString());
									stmt5.setString(7, comp_cd);
									stmt5.setString(8, "S");
									stmt5.executeUpdate();
									stmt5.close();
								}
								else
								{
									String queryString2="INSERT INTO FMS_TAX_STRUCTURE_SUN(TAX_STR_CD,TAX_CODE,BU_UNIT,BU_STATE_TIN,SUN_CODE,ENT_DT,ENT_BY,ENT_PROFILE,COMPANY_CD,ACCOUNT_TYPE) "
											+ "VALUES(?,?,?,?,?,SYSDATE,?,?,?,?)";
									stmt=dbcon.prepareStatement(queryString2);
									stmt.setString(1,  vtax_struct_cd[sun_indx]);
									stmt.setString(2,  tax_cd);
									stmt.setString(3, VBU_SEQ.elementAt(j).toString());
									stmt.setString(4, VBU_TIN.elementAt(j).toString());
									stmt.setString(5, sun_cd[k[j]]);
									stmt.setString(6, emp_cd);
									stmt.setString(7, comp_cd);
									stmt.setString(8, comp_cd);
									stmt.setString(9, "S");
									stmt.executeUpdate();
									stmt.close();
								}
							}
							k[j]++;
						}
						//for sug account code
						if (sz > 1) 
						{
							String query = "SELECT TAX_CODE FROM FMS_TAX_STRUCTURE_DTL " 
									+ "WHERE TAX_STR_CD=? ";
							stmt1 = dbcon.prepareStatement(query);
							stmt1.setString(1, vtax_struct_cd[sun_indx]);
							rset1 = stmt1.executeQuery();
							while (rset1.next()) 
							{
								String tax_cd = rset1.getString(1) == null ? "" : rset1.getString(1);
								if(!sug_cd[l[j]].equals(""))
								{
									int count=0;
									String query1="SELECT COUNT(*) FROM FMS_TAX_STRUCTURE_SUN "
											+ "WHERE COMPANY_CD=? AND TAX_STR_CD=? AND TAX_CODE=? AND BU_UNIT=? AND ACCOUNT_TYPE=?";
									stmt4=dbcon.prepareStatement(query1);
									stmt4.setString(1, comp_cd);
									stmt4.setString(2, vtax_struct_cd[sun_indx]);
									stmt4.setString(3, tax_cd);
									stmt4.setString(4, VBU_SEQ.elementAt(j).toString());
									stmt4.setString(5, "UG");
									rset4=stmt4.executeQuery();
									if(rset4.next())
									{
										count=rset4.getInt(1);
									}
									rset4.close();
									stmt4.close();
									
									if(count>0)
									{
										String query2="UPDATE FMS_TAX_STRUCTURE_SUN "
												+ "SET SUN_CODE=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
												+ "WHERE TAX_STR_CD=? AND TAX_CODE=? AND BU_UNIT=? AND COMPANY_CD=? AND ACCOUNT_TYPE=? ";
										stmt5=dbcon.prepareStatement(query2);
										stmt5.setString(1,  sug_cd[l[j]]);
										stmt5.setString(2, emp_cd);
										stmt5.setString(3, comp_cd);
										stmt5.setString(4, vtax_struct_cd[sun_indx]);
										stmt5.setString(5, tax_cd);
										stmt5.setString(6, VBU_SEQ.elementAt(j).toString());
										stmt5.setString(7, comp_cd);
										stmt5.setString(8, "UG");
										stmt5.executeUpdate();
										stmt5.close();
									}
									else
									{
										String queryString2="INSERT INTO FMS_TAX_STRUCTURE_SUN(TAX_STR_CD,TAX_CODE,BU_UNIT,BU_STATE_TIN,SUN_CODE,ENT_DT,ENT_BY,ENT_PROFILE,COMPANY_CD,ACCOUNT_TYPE) "
												+ "VALUES(?,?,?,?,?,SYSDATE,?,?,?,?)";
										stmt=dbcon.prepareStatement(queryString2);
										stmt.setString(1,  vtax_struct_cd[sun_indx]);
										stmt.setString(2,  tax_cd);
										stmt.setString(3, VBU_SEQ.elementAt(j).toString());
										stmt.setString(4, VBU_TIN.elementAt(j).toString());
										stmt.setString(5, sug_cd[l[j]]);
										stmt.setString(6, emp_cd);
										stmt.setString(7, comp_cd);
										stmt.setString(8, comp_cd);
										stmt.setString(9, "UG");
										
										stmt.executeUpdate();
										
										stmt.close();
									}
								}
								l[j]++;
								continue;
							}
							rset1.close();
							stmt1.close();
						}
						else
						{
							if(!sug_cd[l[j]].equals(""))
							{
								String tax_cd="";
								String query = "SELECT TAX_CODE FROM FMS_TAX_STRUCTURE_DTL " 
										+ "WHERE TAX_STR_CD=? ";
								stmt1 = dbcon.prepareStatement(query);
								stmt1.setString(1, vtax_struct_cd[sun_indx]);
								rset1 = stmt1.executeQuery();
								if(rset1.next())
								{
									tax_cd = rset1.getString(1) == null ? "" : rset1.getString(1);
								}
								rset1.close();
								stmt1.close();
								
								int count=0;
								String query1="SELECT COUNT(*) FROM FMS_TAX_STRUCTURE_SUN "
										+ "WHERE TAX_STR_CD=? AND TAX_CODE=? AND BU_UNIT=? AND COMPANY_CD=? AND ACCOUNT_TYPE=? ";
								stmt4=dbcon.prepareStatement(query1);
								stmt4.setString(1, vtax_struct_cd[sun_indx]);
								stmt4.setString(2, tax_cd);
								stmt4.setString(3, VBU_SEQ.elementAt(j).toString());
								stmt4.setString(4, comp_cd);
								stmt4.setString(5, "UG");
								rset4=stmt4.executeQuery();
								if(rset4.next())
								{
									count=rset4.getInt(1);
								}
								rset4.close();
								stmt4.close();
								
								if(count>0)
								{
									String query2="UPDATE FMS_TAX_STRUCTURE_SUN "
											+ "SET SUN_CODE=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
											+ "WHERE TAX_STR_CD=? AND TAX_CODE=? AND BU_UNIT=? AND COMPANY_CD=? AND ACCOUNT_TYPE=? ";
									stmt5=dbcon.prepareStatement(query2);
									stmt5.setString(1,  sug_cd[l[j]]);
									stmt5.setString(2, emp_cd);
									stmt5.setString(3, comp_cd);
									stmt5.setString(4, vtax_struct_cd[sun_indx]);
									stmt5.setString(5, tax_cd);
									stmt5.setString(6, VBU_SEQ.elementAt(j).toString());
									stmt5.setString(7, comp_cd);
									stmt5.setString(8, "UG");
									stmt5.executeUpdate();
									stmt5.close();
								}
								else
								{
									String queryString2="INSERT INTO FMS_TAX_STRUCTURE_SUN(TAX_STR_CD,TAX_CODE,BU_UNIT,BU_STATE_TIN,SUN_CODE,ENT_DT,ENT_BY,ENT_PROFILE,COMPANY_CD,ACCOUNT_TYPE) "
											+ "VALUES(?,?,?,?,?,SYSDATE,?,?,?,?)";
									stmt=dbcon.prepareStatement(queryString2);
									stmt.setString(1,  vtax_struct_cd[sun_indx]);
									stmt.setString(2,  tax_cd);
									stmt.setString(3, VBU_SEQ.elementAt(j).toString());
									stmt.setString(4, VBU_TIN.elementAt(j).toString());
									stmt.setString(5, sug_cd[l[j]]);
									stmt.setString(6, emp_cd);
									stmt.setString(7, comp_cd);
									stmt.setString(8, comp_cd);
									stmt.setString(9, "UG");
									stmt.executeUpdate();
									stmt.close();
								}
							}
							l[j]++;
						}
						
					}
				}
				
				msg = "Successful! Insert/Update operation is done successfully for Tax Sun Account Code!";
				msg_type = "S";
			}
			else
			{
				msg = "Failed! - Insert/Update for Tax Sun Account Code failed!";
				msg_type="E";
			}
			
			url="../extn_interface/frm_tax_structure_dtl_sun.jsp?"+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Insert/Update failed for Tax Sun Account Code!";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void GenerateSunXML(HttpServletRequest request) throws SQLException
	{
		String function_nm="GenerateSunXML()";
		msg="";
		msg_type="";
		url="";
		try
		{
			String from_dt = request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
			String to_dt = request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
			
			if(!from_dt.equals("")&&!to_dt.equals(""))
			{
				DBsun.setCallFlag("GENERATE_SALES_SUN_XML");
				DBsun.setFrom_dt(from_dt);
				DBsun.setTo_dt(to_dt);
				DBsun.setComp_cd(comp_cd);
				DBsun.setRequest(request);
				DBsun.setEmp_cd(emp_cd);
				DBsun.init();
				
				msg=DBsun.getMsg();
				msg_type=DBsun.getMsg_type();
			}
			else
			{
				msg = "Failed! - SUN Sales XML generation failed!";
				msg_type="E";
			}
			url="../extn_interface/frm_sales_sun_xml_generation.jsp?"+"&msg="+msg+"&msg_type="+msg_type+"&from_dt="+from_dt
					+"&to_dt="+to_dt+commonUrl_pra;
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Generation of SUN XML failed! ";
		}
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}

	private void ApproveSunXML(HttpServletRequest request) throws SQLException
	{
		String function_nm="ApproveSunXML()";
		try
		{
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String bu_state_tin = request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String type_flag = request.getParameter("type_flag")==null?"":request.getParameter("type_flag");
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String from_dt = request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
			String to_dt = request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
			String segment = request.getParameter("segment")==null?"":request.getParameter("segment");

			if(!financial_year.equals("") && !invoice_seq.equals(""))
			{
				if(type_flag.equals("SG"))
				{
					query ="UPDATE FMS_INVOICE_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE,FIN_SYS=? "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? ";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, "Y");
					stmt.setString(2, emp_cd);
					stmt.setString(3, "S");
					stmt.setString(4, comp_cd);
					stmt.setString(5, financial_year);
					stmt.setString(6, invoice_seq);
					stmt.setString(7, bu_state_tin);
					stmt.executeUpdate();
					
					stmt.close();
				}
				else if(type_flag.equals("FFLOW"))
				{
					query ="UPDATE FMS_FFLOW_INV_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE,FIN_SYS=? "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? AND INVOICE_TYPE=?";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, "Y");
					stmt.setString(2, emp_cd);
					stmt.setString(3, "S");
					stmt.setString(4, comp_cd);
					stmt.setString(5, financial_year);
					stmt.setString(6, invoice_seq);
					stmt.setString(7, bu_state_tin);
					stmt.setString(8, invoice_type);
					stmt.executeUpdate();
					
					stmt.close();
				}
				else if(type_flag.equals("DLNG_SG"))
				{
					query="UPDATE FMS_DLNG_INVOICE_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE,FIN_SYS=? "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? ";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, "Y");
					stmt.setString(2, emp_cd);
					stmt.setString(3, "S");
					stmt.setString(4, comp_cd);
					stmt.setString(5, financial_year);
					stmt.setString(6, invoice_seq);
					stmt.setString(7, bu_state_tin);
					stmt.executeUpdate();
					
					stmt.close();
				}
				else if(type_flag.equals("DERV"))
				{
					query ="UPDATE FMS_DERV_INVOICE_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE,FIN_SYS=? "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? AND INV_TYPE=?";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, "Y");
					stmt.setString(2, emp_cd);
					stmt.setString(3, "S");
					stmt.setString(4, comp_cd);
					stmt.setString(5, financial_year);
					stmt.setString(6, invoice_seq);
					stmt.setString(7, bu_state_tin);
					stmt.setString(8, invoice_type);
					stmt.executeUpdate();
					
					stmt.close();
				}
				else if(type_flag.equals("DLNG_FFLOW"))
				{
					query ="UPDATE FMS_DLNG_FFLOW_INV_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE,FIN_SYS=? "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? AND INVOICE_TYPE=?";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, "Y");
					stmt.setString(2, emp_cd);
					stmt.setString(3, "S");
					stmt.setString(4, comp_cd);
					stmt.setString(5, financial_year);
					stmt.setString(6, invoice_seq);
					stmt.setString(7, bu_state_tin);
					stmt.setString(8, invoice_type);
					stmt.executeUpdate();
					
					stmt.close();	
				}
				else if(type_flag.equals("DLNG_SERV"))
				{
					query="UPDATE FMS_DLNG_SVC_INVOICE_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE,FIN_SYS=? "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? ";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, "Y");
					stmt.setString(2, emp_cd);
					stmt.setString(3, "S");
					stmt.setString(4, comp_cd);
					stmt.setString(5, financial_year);
					stmt.setString(6, invoice_seq);
					stmt.setString(7, bu_state_tin);
					stmt.executeUpdate();
					
					stmt.close();	
				}
				msg = "Successful! - "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+") Approved Successfully!";
				msg_type="S";
			}
			else
			{
				msg = "Failed! - "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+") Approval Failed!";
				msg_type="E";
			}
			url = "../extn_interface/frm_invoice_sun_accounting_approval.jsp?msg="+msg+"&msg_type="+msg_type+"&segment="+segment+
					"&from_dt="+from_dt+"&to_dt="+to_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Sales Sun Approval failed! ";
		}
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void ApprovePurSunXML(HttpServletRequest request) throws SQLException
	{
		String function_nm="ApprovePurSunXML()";
		try
		{
			String from_dt = request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
			String to_dt = request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
			String segment = request.getParameter("segment")==null?"":request.getParameter("segment");
			
			String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String invoice_no=request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String financial_year=request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String invoice_seq=request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String purchase_type_flag=request.getParameter("purchase_type_flag")==null?"":request.getParameter("purchase_type_flag");
			String invoice_type=request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String inv_flag=request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");
			String bu_state_tin=request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
			String exchng_rate=request.getParameter("exchng_rate")==null?"":request.getParameter("exchng_rate");
			String exchng_dt=request.getParameter("exchng_dt")==null?"":request.getParameter("exchng_dt");
			
			if(!financial_year.equals("") && !invoice_seq.equals("") && !contract_type.equals(""))
			{
				if(purchase_type_flag.equals("S"))
				{
					if(!exchng_rate.equals("") && !exchng_dt.equals(""))
					{
						query ="UPDATE FMS_PUR_SG_INV_MST SET EXCHG_RATE_VALUE=?,EXCHG_RATE_DT=TO_DATE(?,'DD/MM/YYYY') "
								//+ "SAP_EXCHNG_RATE=? "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
								+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INV_FLAG=?";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, exchng_rate);
						stmt.setString(2, exchng_dt);
						//stmt.setString(3, sap_exchng_flag);
						stmt.setString(3, comp_cd);
						stmt.setString(4, financial_year);
						stmt.setString(5, invoice_seq);
						stmt.setString(6, contract_type);
						stmt.setString(7, inv_flag);
						stmt.executeUpdate();
						
						stmt.close();
					}
					
					query ="UPDATE FMS_PUR_SG_INV_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE,FIN_SYS=? "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INV_FLAG=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, "Y");
					stmt.setString(2, emp_cd);
					stmt.setString(3, "S");
					stmt.setString(4, comp_cd);
					stmt.setString(5, financial_year);
					stmt.setString(6, invoice_seq);
					stmt.setString(7, contract_type);
					stmt.setString(8, inv_flag);
					stmt.executeUpdate();
					
					stmt.close();
					
					query1 ="UPDATE FMS_PUR_PG_INV_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=?,FIN_SYS=? "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INV_FLAG=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, "");
					stmt1.setString(2, "");
					stmt1.setString(3, "");
					stmt1.setString(4, "");
					stmt1.setString(5, comp_cd);
					stmt1.setString(6, financial_year);
					stmt1.setString(7, invoice_seq);
					stmt1.setString(8, contract_type);
					stmt1.setString(9, inv_flag);
					stmt1.executeUpdate();
					
					stmt1.close();
				}
				else if(purchase_type_flag.equals("P"))
				{
					if(!exchng_rate.equals("") && !exchng_dt.equals(""))
					{
						query ="UPDATE FMS_PUR_PG_INV_MST SET EXCHG_RATE_VALUE=?,EXCHG_RATE_DT=TO_DATE(?,'DD/MM/YYYY') "
								//+ "SAP_EXCHNG_RATE=? "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
								+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INV_FLAG=?";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, exchng_rate);
						stmt.setString(2, exchng_dt);
						//stmt.setString(3, sap_exchng_flag);
						stmt.setString(3, comp_cd);
						stmt.setString(4, financial_year);
						stmt.setString(5, invoice_seq);
						stmt.setString(6, contract_type);
						stmt.setString(7, inv_flag);
						stmt.executeUpdate();
						
						stmt.close();
					}
					
					query1 ="UPDATE FMS_PUR_SG_INV_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=?,FIN_SYS=? "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INV_FLAG=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, "");
					stmt1.setString(2, "");
					stmt1.setString(3, "");
					stmt1.setString(4, "");
					stmt1.setString(5, comp_cd);
					stmt1.setString(6, financial_year);
					stmt1.setString(7, invoice_seq);
					stmt1.setString(8, contract_type);
					stmt1.setString(9, inv_flag);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					query ="UPDATE FMS_PUR_PG_INV_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE,FIN_SYS=? "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INV_FLAG=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, "Y");
					stmt.setString(2, emp_cd);
					stmt.setString(3, "S");
					stmt.setString(4, comp_cd);
					stmt.setString(5, financial_year);
					stmt.setString(6, invoice_seq);
					stmt.setString(7, contract_type);
					stmt.setString(8, inv_flag);
					stmt.executeUpdate();
					
					stmt.close();
				}
				else if(purchase_type_flag.equals("FF"))
				{
					if(!exchng_rate.equals("") && !exchng_dt.equals(""))
					{
						query ="UPDATE FMS_PUR_FFLOW_INV_MST SET EXCHG_RATE_VALUE=?,EXCHG_RATE_DT=TO_DATE(?,'DD/MM/YYYY') "
								//+ "SAP_EXCHNG_RATE=? "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
								+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, exchng_rate);
						stmt.setString(2, exchng_dt);
						//stmt.setString(3, sap_exchng_flag);
						stmt.setString(3, comp_cd);
						stmt.setString(4, financial_year);
						stmt.setString(5, invoice_seq);
						stmt.setString(6, contract_type);
						stmt.setString(7, invoice_type);
						stmt.executeUpdate();
						
						stmt.close();
					}
					
					query ="UPDATE FMS_PUR_FFLOW_INV_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE,FIN_SYS=? "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, "Y");
					stmt.setString(2, emp_cd);
					stmt.setString(3, "S");
					stmt.setString(4, comp_cd);
					stmt.setString(5, financial_year);
					stmt.setString(6, invoice_seq);
					stmt.setString(7, contract_type);
					stmt.setString(8, invoice_type);
					stmt.executeUpdate();
					
					stmt.close();
				}
				else if(purchase_type_flag.equals("GTA_S"))
				{
					query ="UPDATE FMS_GTA_SG_INV_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE,FIN_SYS=? "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, "Y");
					stmt.setString(2, emp_cd);
					stmt.setString(3, "S");
					stmt.setString(4, comp_cd);
					stmt.setString(5, financial_year);
					stmt.setString(6, invoice_seq);
					stmt.setString(7, contract_type);
					stmt.setString(8, invoice_type);
					stmt.executeUpdate();
					
					stmt.close();
					
					query1 ="UPDATE FMS_GTA_PG_INV_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=?,FIN_SYS=? "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, "");
					stmt1.setString(2, "");
					stmt1.setString(3, "");
					stmt1.setString(4, "");
					stmt1.setString(5, comp_cd);
					stmt1.setString(6, financial_year);
					stmt1.setString(7, invoice_seq);
					stmt1.setString(8, contract_type);
					stmt1.setString(9, invoice_type);
					stmt1.executeUpdate();
					
					stmt1.close();
				}
				else if(purchase_type_flag.equals("GTA_P"))
				{
					query ="UPDATE FMS_GTA_SG_INV_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=?,FIN_SYS=? "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, "");
					stmt.setString(2, "");
					stmt.setString(3, "");
					stmt.setString(4, "");
					stmt.setString(5, comp_cd);
					stmt.setString(6, financial_year);
					stmt.setString(7, invoice_seq);
					stmt.setString(8, contract_type);
					stmt.setString(9, invoice_type);
					stmt.executeUpdate();
					
					stmt.close();
					
					query1 ="UPDATE FMS_GTA_PG_INV_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE,FIN_SYS=? "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, "Y");
					stmt1.setString(2, emp_cd);
					stmt1.setString(3, "S");
					stmt1.setString(4, comp_cd);
					stmt1.setString(5, financial_year);
					stmt1.setString(6, invoice_seq);
					stmt1.setString(7, contract_type);
					stmt1.setString(8, invoice_type);
					stmt1.executeUpdate();
					
					stmt1.close();
				}
				else if(purchase_type_flag.equals("GTA_FF"))
				{
					query ="UPDATE FMS_GTA_FFLOW_INV_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE,FIN_SYS=? "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, "Y");
					stmt.setString(2, emp_cd);
					stmt.setString(3, "S");
					stmt.setString(4, comp_cd);
					stmt.setString(5, financial_year);
					stmt.setString(6, invoice_seq);
					stmt.setString(7, contract_type);
					stmt.setString(8, invoice_type);
					stmt.executeUpdate();
					
					stmt.close();
				}
				else if(purchase_type_flag.equals("DERV"))
				{
					query ="UPDATE FMS_DERV_INVOICE_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE,FIN_SYS=? "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? AND INV_TYPE=?";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, "Y");
					stmt.setString(2, emp_cd);
					stmt.setString(3, "S");
					stmt.setString(4, comp_cd);
					stmt.setString(5, financial_year);
					stmt.setString(6, invoice_seq);
					stmt.setString(7, bu_state_tin);
					stmt.setString(8, invoice_type);
					stmt.executeUpdate();
					
					stmt.close();
				}
				msg = "Successful! - "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+") Approved Successfully!";
				msg_type="S";
			}
			else
			{	
				msg = "Failed! - "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+") Approval Failed!";
				msg_type="E";
			}
			url = "../extn_interface/frm_remittance_sun_accounting approval.jsp?msg="+msg+"&msg_type="+msg_type+"&segment="+segment+
					"&from_dt="+from_dt+"&to_dt="+to_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Purchase Sun Approval failed! ";
		}
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void GeneratePurSunXML(HttpServletRequest request) throws SQLException
	{
		String function_nm="GeneratePurSunXML()";
		msg="";
		msg_type="";
		url="";
		try
		{
			String from_dt = request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
			String to_dt = request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
			String segment = request.getParameter("segment")==null?"":request.getParameter("segment");
			
			if(!from_dt.equals("")&&!to_dt.equals("")&&!segment.equals(""))
			{
				DBsun.setCallFlag("GENERATE_PUR_SUN_XML");
				DBsun.setFrom_dt(from_dt);
				DBsun.setTo_dt(to_dt);
				DBsun.setSegment(segment);
				DBsun.setComp_cd(comp_cd);
				DBsun.setRequest(request);
				DBsun.setEmp_cd(emp_cd);
				DBsun.init();
				
				msg=DBsun.getMsg();
				msg_type=DBsun.getMsg_type();
			}
			else
			{
				msg = "Failed! - SUN Purchase XML generation failed!";
				msg_type="E";
			}
			url="../extn_interface/frm_pur_sun_xml_generation.jsp?"+"&msg="+msg+"&msg_type="+msg_type+"&from_dt="+from_dt
					+"&to_dt="+to_dt+"&segment="+segment+commonUrl_pra;
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Generation of SUN XML failed! ";
		}
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	public void ReapproveSalesInvoice(HttpServletRequest request) throws SQLException
	{
		String function_nm="ReapproveSalesInvoice()";
		try
		{
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String bu_state_tin = request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String type_flag = request.getParameter("type_flag")==null?"":request.getParameter("type_flag");
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String from_dt = request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
			String to_dt = request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
			String segment = request.getParameter("segment")==null?"":request.getParameter("segment");

			if(!financial_year.equals("") && !invoice_seq.equals(""))
			{
				if(type_flag.equals("SG"))
				{
					int del_count=0;
					query1="SELECT COUNT(PDF_TYPE) FROM FMS_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? AND INVOICE_SEQ=? "
							+ "AND FINANCIAL_YEAR=? AND PDF_TYPE=? ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, bu_state_tin);
					stmt1.setString(3, invoice_seq);
					stmt1.setString(4, financial_year);
					stmt1.setString(5, "S");
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						del_count=rset1.getInt(1);
					}
					stmt1.close();
					
					if(del_count==1)
					{
						query2="DELETE FROM FMS_INV_FILE_DTL "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? AND INVOICE_SEQ=? "
								+ "AND FINANCIAL_YEAR=? AND PDF_TYPE=? ";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, bu_state_tin);
						stmt2.setString(3, invoice_seq);
						stmt2.setString(4, financial_year);
						stmt2.setString(5, "S");
						stmt2.executeUpdate();
						stmt2.close();
						
						query ="UPDATE FMS_INVOICE_MST SET SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
								+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? AND SAP_APPROVAL=? AND FIN_SYS=? ";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, emp_cd);
						stmt.setString(2, comp_cd);
						stmt.setString(3, financial_year);
						stmt.setString(4, invoice_seq);
						stmt.setString(5, bu_state_tin);
						stmt.setString(6, "Y");
						stmt.setString(7, "S");
						stmt.executeUpdate();
						stmt.close();
						
						msg="Re-approved "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+")!";
						msg_type="S";
					}
					else
					{
						msg="No XML generated post Approval, Re-approval not applicable "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+")!";
						msg_type="E";
					}
					
				}
				else if(type_flag.equals("FFLOW"))
				{
					int del_count=0;
					query1="SELECT COUNT(*) "
							+ "FROM FMS_FFLOW_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND BU_STATE_TIN=? "
							+ "AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
							+ "AND PDF_TYPE=? ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, invoice_seq);
					stmt1.setString(3, bu_state_tin);
					stmt1.setString(4, invoice_type);
					stmt1.setString(5, financial_year);
					stmt1.setString(6, "S");
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						del_count=rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					if(del_count==1)
					{
						query2="DELETE FROM FMS_FFLOW_INV_FILE_DTL "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? AND INVOICE_SEQ=? "
								+ "AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND INVOICE_TYPE=? ";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, bu_state_tin);
						stmt2.setString(3, invoice_seq);
						stmt2.setString(4, financial_year);
						stmt2.setString(5, "S");
						stmt2.setString(6, invoice_type);
						stmt2.executeUpdate();
						stmt2.close();
						
						query ="UPDATE FMS_FFLOW_INV_MST SET SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
								+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? AND INVOICE_TYPE=? AND SAP_APPROVAL=? AND FIN_SYS=? ";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, emp_cd);
						stmt.setString(2, comp_cd);
						stmt.setString(3, financial_year);
						stmt.setString(4, invoice_seq);
						stmt.setString(5, bu_state_tin);
						stmt.setString(6, invoice_type);
						stmt.setString(7, "Y");
						stmt.setString(8, "S");
						stmt.executeUpdate();
						stmt.close();
					
						msg="Re-approved "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+")!";
						msg_type="S";
					}
					else
					{
						msg="No XML generated post Approval, Re-approval not applicable "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+")!";
						msg_type="E";
					}
				}
				else if(type_flag.equals("DLNG_SG"))
				{
					int del_count=0;
					query="SELECT COUNT(*) "
						+ "FROM FMS_DLNG_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
						+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, bu_state_tin);
					stmt.setString(3, invoice_seq);
					stmt.setString(4, financial_year);
					stmt.setString(5, "S");
					rset=stmt.executeQuery();
					if(rset.next())
					{
						del_count=rset.getInt(1);
					}
					rset.close();
					stmt.close();
					if(del_count==1)
					{
						query2="DELETE FROM FMS_DLNG_INV_FILE_DTL "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? AND INVOICE_SEQ=? "
								+ "AND FINANCIAL_YEAR=? AND PDF_TYPE=? ";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, bu_state_tin);
						stmt2.setString(3, invoice_seq);
						stmt2.setString(4, financial_year);
						stmt2.setString(5, "S");
						stmt2.executeUpdate();
						stmt2.close();
						
						query1="UPDATE FMS_DLNG_INVOICE_MST SET SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
								+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? AND SAP_APPROVAL=? AND FIN_SYS=? ";
						stmt1 = dbcon.prepareStatement(query1);
						stmt1.setString(1, emp_cd);
						stmt1.setString(2, comp_cd);
						stmt1.setString(3, financial_year);
						stmt1.setString(4, invoice_seq);
						stmt1.setString(5, bu_state_tin);
						stmt1.setString(6, "Y");
						stmt1.setString(7, "S");
						stmt1.executeUpdate();
						stmt1.close();
						
						msg="Re-approved "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+")!";
						msg_type="S";
					}
					else
					{
						msg="No XML generated post Approval, Re-approval not applicable "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+")!";
						msg_type="E";
					}
				}
				else if(type_flag.equals("DERV"))
				{
					int del_count=0;
					query1="SELECT COUNT(*) "
						+ "FROM FMS_DERV_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=?  "
						+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND INV_TYPE=? ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, bu_state_tin);
					stmt1.setString(3, invoice_seq);
					stmt1.setString(4, financial_year);
					stmt1.setString(5, "S");
					stmt1.setString(6, invoice_type);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						del_count=rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					
					if(del_count==1)
					{
						query2="DELETE FROM FMS_DERV_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND INV_TYPE=? ";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, bu_state_tin);
						stmt2.setString(3, invoice_seq);
						stmt2.setString(4, financial_year);
						stmt2.setString(5, "S");
						stmt2.setString(6, invoice_type);
						stmt2.executeUpdate();
						stmt2.close();
						
						query ="UPDATE FMS_DERV_INVOICE_MST SET SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
								+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? AND INV_TYPE=? AND  SAP_APPROVAL=? AND FIN_SYS=? ";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, emp_cd);
						stmt.setString(2, comp_cd);
						stmt.setString(3, financial_year);
						stmt.setString(4, invoice_seq);
						stmt.setString(5, bu_state_tin);
						stmt.setString(6, invoice_type);
						stmt.setString(7, "Y");
						stmt.setString(8, "S");
						stmt.executeUpdate();
						stmt.close();

						msg="Re-approved "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+")!";
						msg_type="S";
					}
					else
					{
						msg="No XML generated post Approval, Re-approval not applicable "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+")!";
						msg_type="E";
					}
				}
				else if(type_flag.equals("DLNG_FFLOW"))
				{
					int del_count=0;
					query1="SELECT COUNT(*) "
						+ "FROM FMS_DLNG_FFLOW_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
						+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND INVOICE_TYPE=? ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, bu_state_tin);
					stmt1.setString(3, invoice_seq);
					stmt1.setString(4, financial_year);
					stmt1.setString(5, "S");
					stmt1.setString(6, invoice_type);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						del_count=rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					if(del_count==1)
					{
						query2="DELETE FROM FMS_DLNG_FFLOW_INV_FILE_DTL "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND INVOICE_TYPE=? ";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, bu_state_tin);
						stmt2.setString(3, invoice_seq);
						stmt2.setString(4, financial_year);
						stmt2.setString(5, "S");
						stmt2.setString(6, invoice_type);
						stmt2.executeUpdate();
						stmt2.close();
						
						query ="UPDATE FMS_DLNG_FFLOW_INV_MST SET SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
								+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? AND INVOICE_TYPE=? AND SAP_APPROVAL=? AND FIN_SYS=? ";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, emp_cd);
						stmt.setString(2, comp_cd);
						stmt.setString(3, financial_year);
						stmt.setString(4, invoice_seq);
						stmt.setString(5, bu_state_tin);
						stmt.setString(6, invoice_type);
						stmt.setString(7, "Y");
						stmt.setString(8, "S");
						stmt.executeUpdate();
						stmt.close();	
						
						msg="Re-approved "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+")!";
						msg_type="S";
					}
					else
					{
						msg="No XML generated post Approval, Re-approval not applicable "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+")!";
						msg_type="E";
					}
				}
				else if(type_flag.equals("DLNG_SERV"))
				{
					int del_count=0;
					query1="SELECT COUNT(*) FROM FMS_DLNG_SVC_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, bu_state_tin);
					stmt1.setString(3, invoice_seq);
					stmt1.setString(4, financial_year);
					stmt1.setString(5, "S");
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						del_count=rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					if(del_count==1)
					{
						query2="DELETE FROM FMS_DLNG_SVC_INV_FILE_DTL "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? ";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, bu_state_tin);
						stmt2.setString(3, invoice_seq);
						stmt2.setString(4, financial_year);
						stmt2.setString(5, "S");
						stmt2.executeUpdate();
						stmt2.close();

						query="UPDATE FMS_DLNG_SVC_INVOICE_MST SET SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
								+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? AND SAP_APPROVAL=? AND FIN_SYS=? ";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, emp_cd);
						stmt.setString(2, comp_cd);
						stmt.setString(3, financial_year);
						stmt.setString(4, invoice_seq);
						stmt.setString(5, bu_state_tin);
						stmt.setString(6, "Y");
						stmt.setString(7, "S");
						stmt.executeUpdate();
						
						stmt.close();
						
						msg="Re-approved "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+")!";
						msg_type="S";
					}
					else
					{
						msg="No XML generated post Approval, Re-approval not applicable "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+")!";
						msg_type="E";
					}
				}
			}
			else
			{
				msg = "Failed! - "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+") Re-approval Failed!";
				msg_type="E";
			}
			url = "../extn_interface/frm_invoice_sun_accounting_approval.jsp?msg="+msg+"&msg_type="+msg_type+"&segment="+segment+
					"&from_dt="+from_dt+"&to_dt="+to_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Sales Invoice Re-Approval failed! ";
		}
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}

	public void ReapprovePurSunXML(HttpServletRequest request) throws SQLException
	{
		String function_nm="ReapprovePurSunXML()";
		try
		{
			String from_dt = request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
			String to_dt = request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
			String segment = request.getParameter("segment")==null?"":request.getParameter("segment");
			
			String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String invoice_no=request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String financial_year=request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String invoice_seq=request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String purchase_type_flag=request.getParameter("purchase_type_flag")==null?"":request.getParameter("purchase_type_flag");
			String invoice_type=request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String inv_flag=request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");
			String bu_state_tin=request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
			String exchng_rate=request.getParameter("exchng_rate")==null?"":request.getParameter("exchng_rate");
			String exchng_dt=request.getParameter("exchng_dt")==null?"":request.getParameter("exchng_dt");
			
			
			if(!financial_year.equals("") && !invoice_seq.equals("") && !contract_type.equals(""))
			{
				if(purchase_type_flag.equals("S")||purchase_type_flag.equals("P"))
				{
					int del_count=0;
					query1="SELECT COUNT(*) "
							+ "FROM FMS_PUR_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=? AND INV_FLAG=? ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, contract_type);
					stmt1.setString(3, invoice_seq);
					stmt1.setString(4, financial_year);
					stmt1.setString(5, "S");
					stmt1.setString(6, inv_flag);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						del_count=rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					if(del_count==1)
					{
						query2="DELETE FROM FMS_PUR_INV_FILE_DTL "
								+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=? AND INV_FLAG=? ";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, contract_type);
						stmt2.setString(3, invoice_seq);
						stmt2.setString(4, financial_year);
						stmt2.setString(5, "S");
						stmt2.setString(6, inv_flag);
						stmt2.executeUpdate();
						stmt2.close();
						
						if(purchase_type_flag.equals("S"))
						{
							query ="UPDATE FMS_PUR_SG_INV_MST SET SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INV_FLAG=? AND SAP_APPROVAL=? AND FIN_SYS=? ";
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, emp_cd);
							stmt.setString(2, comp_cd);
							stmt.setString(3, financial_year);
							stmt.setString(4, invoice_seq);
							stmt.setString(5, contract_type);
							stmt.setString(6, inv_flag);
							stmt.setString(7, "Y");
							stmt.setString(8, "S");
							stmt.executeUpdate();
							stmt.close();
							
							if(!exchng_rate.equals("") && !exchng_dt.equals(""))
							{
								query ="UPDATE FMS_PUR_SG_INV_MST SET EXCHG_RATE_VALUE=?,EXCHG_RATE_DT=TO_DATE(?,'DD/MM/YYYY') "
										//+ "SAP_EXCHNG_RATE=? "
										+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
										+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INV_FLAG=?";
								stmt=dbcon.prepareStatement(query);
								stmt.setString(1, exchng_rate);
								stmt.setString(2, exchng_dt);
								//stmt.setString(3, sap_exchng_flag);
								stmt.setString(3, comp_cd);
								stmt.setString(4, financial_year);
								stmt.setString(5, invoice_seq);
								stmt.setString(6, contract_type);
								stmt.setString(7, inv_flag);
								stmt.executeUpdate();
								stmt.close();
							}
						}
						else
						{
							query ="UPDATE FMS_PUR_PG_INV_MST SET SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INV_FLAG=? AND SAP_APPROVAL=? AND FIN_SYS=? ";
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, emp_cd);
							stmt.setString(2, comp_cd);
							stmt.setString(3, financial_year);
							stmt.setString(4, invoice_seq);
							stmt.setString(5, contract_type);
							stmt.setString(6, inv_flag);
							stmt.setString(7, "Y");
							stmt.setString(8, "S");
							stmt.executeUpdate();
							stmt.close();
							
							if(!exchng_rate.equals("") && !exchng_dt.equals(""))
							{
								query ="UPDATE FMS_PUR_PG_INV_MST SET EXCHG_RATE_VALUE=?,EXCHG_RATE_DT=TO_DATE(?,'DD/MM/YYYY') "
										//+ "SAP_EXCHNG_RATE=? "
										+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
										+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INV_FLAG=?";
								stmt=dbcon.prepareStatement(query);
								stmt.setString(1, exchng_rate);
								stmt.setString(2, exchng_dt);
								//stmt.setString(3, sap_exchng_flag);
								stmt.setString(3, comp_cd);
								stmt.setString(4, financial_year);
								stmt.setString(5, invoice_seq);
								stmt.setString(6, contract_type);
								stmt.setString(7, inv_flag);
								stmt.executeUpdate();
								stmt.close();
							}
						}

						msg="Re-approved "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+")!";
						msg_type="S";
					}
					else
					{
						msg="No XML generated post Approval, Re-approval not applicable "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+")!";
						msg_type="E";
					}
				}
				else if(purchase_type_flag.equals("FF"))
				{
					int del_count=0;
					query1="SELECT COUNT(*) "
							+ "FROM FMS_PUR_FFLOW_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_TYPE=? AND INV_TITLE=? ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, contract_type);
					stmt1.setString(3, invoice_seq);
					stmt1.setString(4, financial_year);
					stmt1.setString(5, invoice_type);
					stmt1.setString(6, "S");
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						del_count=rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					if(del_count==1)
					{
						query2="DELETE FROM FMS_PUR_FFLOW_INV_FILE_DTL "
								+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
								+ "AND INVOICE_TYPE=? AND INV_TITLE=? " ;
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, contract_type);
						stmt2.setString(3, invoice_seq);
						stmt2.setString(4, financial_year);
						stmt2.setString(5, invoice_type);
						stmt2.setString(6, "S");
						stmt2.executeUpdate();
						stmt2.close();
						
						query ="UPDATE FMS_PUR_FFLOW_INV_MST SET SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
								+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? AND SAP_APPROVAL=? AND FIN_SYS=? ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, emp_cd);
						stmt.setString(2, comp_cd);
						stmt.setString(3, financial_year);
						stmt.setString(4, invoice_seq);
						stmt.setString(5, contract_type);
						stmt.setString(6, invoice_type);
						stmt.setString(7, "Y");
						stmt.setString(8, "S");
						stmt.executeUpdate();
						stmt.close();
						
						if(!exchng_rate.equals("") && !exchng_dt.equals(""))
						{
							query ="UPDATE FMS_PUR_FFLOW_INV_MST SET EXCHG_RATE_VALUE=?,EXCHG_RATE_DT=TO_DATE(?,'DD/MM/YYYY') "
									//+ "SAP_EXCHNG_RATE=? "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? ";
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, exchng_rate);
							stmt.setString(2, exchng_dt);
							//stmt.setString(3, sap_exchng_flag);
							stmt.setString(3, comp_cd);
							stmt.setString(4, financial_year);
							stmt.setString(5, invoice_seq);
							stmt.setString(6, contract_type);
							stmt.setString(7, invoice_type);
							stmt.executeUpdate();
							stmt.close();
						}

						msg="Re-approved "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+")!";
						msg_type="S";
					}
					else
					{
						msg="No XML generated post Approval, Re-approval not applicable "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+")!";
						msg_type="E";
					}
				}
				else if(purchase_type_flag.equals("GTA_S") || purchase_type_flag.equals("GTA_P"))
				{
					int del_count=0;
					query1="SELECT COUNT(*) "
							+ "FROM FMS_GTA_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=? AND INVOICE_TYPE=? ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, contract_type);
					stmt1.setString(3, invoice_seq);
					stmt1.setString(4, financial_year);
					stmt1.setString(5, "S");
					stmt1.setString(6, invoice_type);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						del_count=rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					if(del_count==1)
					{
						query2="DELETE FROM FMS_GTA_INV_FILE_DTL "
								+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=?  "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=? AND INVOICE_TYPE=? ";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, contract_type);
						stmt2.setString(3, invoice_seq);
						stmt2.setString(4, financial_year);
						stmt2.setString(5, "S");
						stmt2.setString(6, invoice_type);
						stmt2.executeUpdate();
						stmt2.close();
						
						if(purchase_type_flag.equals("GTA_S"))
						{
							query ="UPDATE FMS_GTA_SG_INV_MST SET SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? AND SAP_APPROVAL=? AND FIN_SYS=? ";
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, emp_cd);
							stmt.setString(2, comp_cd);
							stmt.setString(3, financial_year);
							stmt.setString(4, invoice_seq);
							stmt.setString(5, contract_type);
							stmt.setString(6, invoice_type);
							stmt.setString(7, "Y");
							stmt.setString(8, "S");
							stmt.executeUpdate();
							stmt.close();
						}
						else
						{
							query1 ="UPDATE FMS_GTA_PG_INV_MST SET SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? AND SAP_APPROVAL=? AND FIN_SYS=? ";
							stmt1=dbcon.prepareStatement(query1);
							stmt1.setString(1, emp_cd);
							stmt1.setString(2, comp_cd);
							stmt1.setString(3, financial_year);
							stmt1.setString(4, invoice_seq);
							stmt1.setString(5, contract_type);
							stmt1.setString(6, invoice_type);
							stmt1.setString(7, "Y");
							stmt1.setString(8, "S");
							stmt1.executeUpdate();
							stmt1.close();
						}

						msg="Re-approved "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+")!";
						msg_type="S";
					}
					else
					{
						msg="No XML generated post Approval, Re-approval not applicable "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+")!";
						msg_type="E";
					}
				}
				else if(purchase_type_flag.equals("GTA_FF"))
				{
					int del_count=0;
					query1="SELECT COUNT(*) "
							+ "FROM FMS_GTA_FFLOW_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=? AND INVOICE_TYPE=? ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, contract_type);
					stmt1.setString(3, invoice_seq);
					stmt1.setString(4, financial_year);
					stmt1.setString(5, "S");
					stmt1.setString(6, invoice_type);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						del_count=rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					if(del_count==1)
					{
						query2="DELETE FROM FMS_GTA_FFLOW_INV_FILE_DTL "
								+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=? AND INVOICE_TYPE=? ";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, contract_type);
						stmt2.setString(3, invoice_seq);
						stmt2.setString(4, financial_year);
						stmt2.setString(5, "S");
						stmt2.setString(6, invoice_type);
						stmt2.executeUpdate();
						stmt2.close();
						
						query ="UPDATE FMS_GTA_FFLOW_INV_MST SET SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
								+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? AND SAP_APPROVAL=? AND FIN_SYS=? ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, emp_cd);
						stmt.setString(2, comp_cd);
						stmt.setString(3, financial_year);
						stmt.setString(4, invoice_seq);
						stmt.setString(5, contract_type);
						stmt.setString(6, invoice_type);
						stmt.setString(7, "Y");
						stmt.setString(8, "S");
						stmt.executeUpdate();
						stmt.close();

						msg="Re-approved "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+")!";
						msg_type="S";
					}
					else
					{
						msg="No XML generated post Approval, Re-approval not applicable "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+")!";
						msg_type="E";
					}
				}
				else if(purchase_type_flag.equals("DERV"))
				{
					int del_count=0;
					query1="SELECT COUNT(*) "
							+ "FROM FMS_DERV_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND INV_TYPE=? ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, bu_state_tin);
					stmt1.setString(3, invoice_seq);
					stmt1.setString(4, financial_year);
					stmt1.setString(5, "S");
					stmt1.setString(6, invoice_type);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						del_count=rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					if(del_count==1)
					{
						query2="DELETE FROM FMS_DERV_INV_FILE_DTL "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND INV_TYPE=? ";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, bu_state_tin);
						stmt2.setString(3, invoice_seq);
						stmt2.setString(4, financial_year);
						stmt2.setString(5, "S");
						stmt2.setString(6, invoice_type);
						stmt2.executeUpdate();
						stmt2.close();
						
						query ="UPDATE FMS_DERV_INVOICE_MST SET SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
								+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? AND INV_TYPE=? AND SAP_APPROVAL=? AND FIN_SYS=? ";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, emp_cd);
						stmt.setString(2, comp_cd);
						stmt.setString(3, financial_year);
						stmt.setString(4, invoice_seq);
						stmt.setString(5, bu_state_tin);
						stmt.setString(6, invoice_type);
						stmt.setString(7, "Y");
						stmt.setString(8, "S");
						stmt.executeUpdate();
						stmt.close();

						msg="Re-approved "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+")!";
						msg_type="S";
					}
					else
					{
						msg="No XML generated post Approval, Re-approval not applicable "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+")!";
						msg_type="E";
					}
				}
			}
			else
			{	
				msg = "Failed! - "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+") Re-Approval Failed!";
				msg_type="E";
			}

			url = "../extn_interface/frm_remittance_sun_accounting approval.jsp?msg="+msg+"&msg_type="+msg_type+"&segment="+segment+
					"&from_dt="+from_dt+"&to_dt="+to_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Purchase Invoice Re-Approval failed! ";
		}
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	public String getAccountType(String entity_role,int index)
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
		String function_nm="getAccountType()";
		String account_type="";
		try
		{
			if(entity_role.equals("C"))
			{
				if(index==0)
				{
					account_type="S";
				}
				else if(index==1)
				{
					account_type="SI";
				}
				else if(index==2)
				{
					account_type="UG";
				}
			}
			else if(entity_role.equals("T"))
			{
				if(index==0)
				{
					account_type="S";
				}
				else if(index==1)
				{
					account_type="I";
				}
				else if(index==2)
				{
					account_type="V";
				}
				else if(index==3)
				{
					account_type="CD";
				}
			}
			else if(entity_role.equals("R"))
			{
				if(index==0)
				{
					account_type="R";
				}
				else if(index==1)
				{
					account_type="K";
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
		}
		
		return account_type;
	}
}