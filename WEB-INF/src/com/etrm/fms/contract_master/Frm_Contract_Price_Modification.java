package com.etrm.fms.contract_master;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

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
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.escapeSingleQuotes;


@WebServlet("/servlet/Frm_Contract_Price_Modification")
public class Frm_Contract_Price_Modification extends HttpServlet {
	
	static String db_src_file_name="Frm_Contract_Price_Modification.java";

	public static  Connection dbcon;
	static NumberFormat nf = new DecimalFormat("###########0.00");
	static NumberFormat nf2 = new DecimalFormat("###########0.000");
	static NumberFormat nf3 = new DecimalFormat("###########0.0000");
	
	public static escapeSingleQuotes snglQuot = new escapeSingleQuotes();
	public static String servletName = "Frm_Contract_Price_Modification";
	public static String methodName = "";
	public static String option = "";
	public static String url = "";
	public static String form_name = "";
	public static String msg = "";
	public static String msg_type = "";
	public static String err_msg = "";
	static int count = 0;
	
	private static String queryString = null;
	private static String query = null;
	private static String query1 = null;
	private static String query2 = null;
	private static PreparedStatement stmt = null;
	private static PreparedStatement stmt1 = null;
	private static PreparedStatement stmt2 = null;
	private static PreparedStatement stmt3 = null;
	private static PreparedStatement stmt4 = null;
	
	private static ResultSet rset = null;
	private static ResultSet rset1 = null;
	private static ResultSet rset2 = null;
	private static ResultSet rset3 = null;
	private static ResultSet rset4 = null;
	
	
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
	
	static UtilBean utilBean = new UtilBean();
	
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
				if(Context == null) 
				{
					throw new Exception("Boom - No Context");
				}
				
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
					
					if(option.equalsIgnoreCase("Contract_Price_Modifiction"))
					{
						modifyContractPrice(request);
					}
					else if(option.equalsIgnoreCase("Contract_Price_Modification_Approve"))
					{
						modifyContractPriceApproval(request);
					}
				}
				
				dbcon.close();
				dbcon=null;
			}
			catch(Exception e)
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
				url=CommonVariable.errorpage_url;
			}
			finally
			{
				if(rset != null){try {rset.close();}catch(SQLException e){System.out.println("rset is not close " + e);}}
				if(rset1 != null){try {rset1.close();}catch(SQLException e){System.out.println("rset1 is not close " + e);}}
				if(rset2 != null){try {rset2.close();}catch(SQLException e){System.out.println("rset2 is not close " + e);}}
				if(rset3 != null){try {rset3.close();}catch(SQLException e){System.out.println("rset3 is not close " + e);}}
				if(rset4 != null){try {rset4.close();}catch(SQLException e){System.out.println("rset4 is not close " + e);}}
				if(stmt != null){try {stmt.close();}catch(SQLException e){System.out.println("stmt is not close " + e);}}
				if(stmt1 != null){try {stmt1.close();}catch(SQLException e){System.out.println("stmt1 is not close " + e);}}
				if(stmt2 != null){try {stmt2.close();}catch(SQLException e){System.out.println("stmt2 is not close " + e);}}
				if(stmt3 != null){try {stmt3.close();}catch(SQLException e){System.out.println("stmt3 is not close " + e);}}
				if(stmt4 != null){try {stmt4.close();}catch(SQLException e){System.out.println("stmt4 is not close " + e);}}
				if(dbcon != null){try {dbcon.close();}catch(SQLException e){System.out.println("conn is not close " + e);}}
			}
		}
		try 
		{
			response.sendRedirect(url);
		} 
		catch(IOException e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	private void modifyContractPrice(HttpServletRequest request) throws SQLException 
	{
		String function_nm="modifyContractPrice()";

		String customer_cd = request.getParameter("customer_cd")==null?"":request.getParameter("customer_cd");
		try
		{
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,customer_cd);
			HttpSession session = request.getSession();
			String user_cd = (String)session.getAttribute("user_cd")==null?"":(String)session.getAttribute("user_cd");
			
			String fgsa[] = request.getParameterValues("fgsa");
			String fgsa_rev[] = request.getParameterValues("fgsa_rev");
			String sn[] = request.getParameterValues("sn");
			String sn_rev[] = request.getParameterValues("sn_rev");
			String cont_type[] = request.getParameterValues("cont_type");
			String segment[] = request.getParameterValues("segment");
			String price_flag[] = request.getParameterValues("price_flag");
			String modification_seq_no[] = request.getParameterValues("modification_seq_no");
			
			String sales_rate[] = request.getParameterValues("sales_rate");
			String new_sales_rate[] = request.getParameterValues("new_sales_rate");
			String new_eff_dt[] = request.getParameterValues("new_eff_dt");
			
			String cargo_no[] = request.getParameterValues("cargo_no");
			String cargo_seq[] = request.getParameterValues("cargo_seq");
			String cost_price[] = request.getParameterValues("cost_price");
			String alloc_qty[] = request.getParameterValues("alloc_qty");
			String margin[] = request.getParameterValues("margin");
			String new_margin[] = request.getParameterValues("new_margin");
			String total_margin[] = request.getParameterValues("total_margin");
			String new_total_margin[] = request.getParameterValues("new_total_margin");
			String cargo_sn_rev[] = request.getParameterValues("cargo_sn_rev");
			
			boolean dlng_flag=false;
			
			if(fgsa != null)
			{
				for(int i=0;i<fgsa.length; i++)
				{
					//System.out.println(fgsa[i]+"\n"+ fgsa_rev[i]+"\n"+ sn[i]+"\n"+ sn_rev[i]+"\n"+ cont_type[i]+"\n"+ segment[i]+"\n"+ price_flag[i]+"\n"+ modification_seq_no[i]+"\n"+ sales_rate[i]+"\n"+ new_sales_rate[i]+"\n");
					
					int new_seq_no = Integer.parseInt(modification_seq_no[i].toString()) + 1;
					
					if(cargo_no != null)
					{
						for(int j=0; j<cargo_no.length; j++)
						{
							String cont_name_map = counterparty_abbr+"-"+cont_type[i]+fgsa[i]+"-"+sn[i];
							if(price_flag[i].toString().equals("") || price_flag[i].toString().equals("A") || price_flag[i].toString().equals("X"))
							{
								query = "INSERT INTO FMS_SUPPLY_ALLOC_REVISED(COMPANY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,COUNTERPARTY_CD,"
										+ "CONTRACT_TYPE,PUR_CONT,MODIFICATION_SEQ_NO,FLAG,ENT_BY,ENT_DT,"
										+ "ORI_SALE_PRICE,NEW_SALE_PRICE,ORI_MARGIN,NEW_MARGIN,ORI_AVG_MARGIN,"
										+ "NEW_AVG_MARGIN,ORI_TOT_MARGIN,NEW_TOT_MARGIN,ALLOC_QTY, NEW_PRICE_EFF_DT,CARGO_NO)"
										+ " VALUES(?,?,?,?,?,?,?,"
										+ "?,?,?,?,SYSDATE,?,?,"
										+ "?,?,?,?,?,"
										+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?) ";
								stmt = dbcon.prepareStatement(query);
								stmt.setString(1, comp_cd);
								stmt.setString(2, fgsa[i]);
								stmt.setString(3, fgsa_rev[i]);
								stmt.setString(4, sn[i]);
								stmt.setString(5, cargo_sn_rev[j]);
								stmt.setString(6, customer_cd);
								stmt.setString(7, cont_type[i]);
								stmt.setString(8, cargo_no[j]);
								stmt.setInt(9, new_seq_no);
								stmt.setString(10, "R");
								stmt.setString(11, user_cd);
								stmt.setString(12, sales_rate[i]);
								stmt.setString(13, new_sales_rate[i]);
								stmt.setString(14, margin[j]);
								stmt.setString(15, new_margin[j]);
								stmt.setString(16, "0");
								stmt.setString(17, "0");
								stmt.setString(18, total_margin[j]);
								stmt.setString(19, new_total_margin[j]);
								stmt.setString(20, alloc_qty[j]);
								stmt.setString(21, new_eff_dt[i]);
								stmt.setString(22, cargo_seq[j]);
								stmt.executeUpdate();
								
								stmt.close();
							}
							else if(price_flag[i].toString().equals("R"))
							{
								query1="SELECT COUNT(*) FROM FMS_SUPPLY_ALLOC_REVISED "
										+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
										+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
										+ "AND CONT_REV=? AND MODIFICATION_SEQ_NO=? "
										+ "AND FLAG=? AND PUR_CONT=? AND CARGO_NO=? ";
								stmt1 = dbcon.prepareStatement(query1);
								stmt1.setString(1, comp_cd);
								stmt1.setString(2, fgsa[i]);
								stmt1.setString(3, fgsa_rev[i]);
								stmt1.setString(4, sn[i]);
								stmt1.setString(5, customer_cd);
								stmt1.setString(6, cont_type[i]);
								stmt1.setString(7, cargo_sn_rev[j]);
								stmt1.setString(8, modification_seq_no[i]);
								stmt1.setString(9, "R");
								stmt1.setString(10, cargo_no[j]);
								stmt1.setString(11, cargo_seq[j]);
								rset1=stmt1.executeQuery();
								if(rset1.next())
								{
									if(rset1.getInt(1) > 0)
									{
										query = "UPDATE FMS_SUPPLY_ALLOC_REVISED SET ORI_SALE_PRICE=?, NEW_SALE_PRICE=?,"
												+ "ORI_MARGIN=?,NEW_MARGIN=?,ORI_AVG_MARGIN=?,NEW_AVG_MARGIN=?,"
												+ "ORI_TOT_MARGIN=?,NEW_TOT_MARGIN=?,ENT_BY=?,ENT_DT=SYSDATE, NEW_PRICE_EFF_DT=TO_DATE(?,'DD/MM/YYYY') "
												+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
												+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
												+ "AND CONT_REV=? AND MODIFICATION_SEQ_NO=? "
												+ "AND FLAG=? AND PUR_CONT=? AND CARGO_NO=? ";
										stmt = dbcon.prepareStatement(query);
										stmt.setString(1, sales_rate[i]);
										stmt.setString(2, new_sales_rate[i]);
										stmt.setString(3, margin[j]);
										stmt.setString(4, new_margin[j]);
										stmt.setString(5, "0");
										stmt.setString(6, "0");
										stmt.setString(7, total_margin[j]);
										stmt.setString(8, new_total_margin[j]);
										stmt.setString(9, user_cd);
										stmt.setString(10, new_eff_dt[i]);
										stmt.setString(11, comp_cd);
										stmt.setString(12, fgsa[i]);
										stmt.setString(13, fgsa_rev[i]);
										stmt.setString(14, sn[i]);
										stmt.setString(15, customer_cd);
										stmt.setString(16, cont_type[i]);
										stmt.setString(17, cargo_sn_rev[j]);
										stmt.setString(18, modification_seq_no[i]);
										stmt.setString(19, "R");
										stmt.setString(20, cargo_no[j]);
										stmt.setString(21, cargo_seq[j]);
										stmt.executeUpdate();
										
										stmt.close();
									}
									else
									{
										query = "INSERT INTO FMS_SUPPLY_ALLOC_REVISED(COMPANY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,COUNTERPARTY_CD,"
												+ "CONTRACT_TYPE,PUR_CONT,MODIFICATION_SEQ_NO,FLAG,ENT_BY,ENT_DT,"
												+ "ORI_SALE_PRICE,NEW_SALE_PRICE,ORI_MARGIN,NEW_MARGIN,ORI_AVG_MARGIN,"
												+ "NEW_AVG_MARGIN,ORI_TOT_MARGIN,NEW_TOT_MARGIN,ALLOC_QTY, NEW_PRICE_EFF_DT,CARGO_NO)"
												+ " VALUES(?,?,?,?,?,?,?,"
												+ "?,?,?,?,SYSDATE,?,?,"
												+ "?,?,?,?,?,"
												+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?) ";
										stmt = dbcon.prepareStatement(query);
										stmt.setString(1, comp_cd);
										stmt.setString(2, fgsa[i]);
										stmt.setString(3, fgsa_rev[i]);
										stmt.setString(4, sn[i]);
										stmt.setString(5, cargo_sn_rev[j]);
										stmt.setString(6, customer_cd);
										stmt.setString(7, cont_type[i]);
										stmt.setString(8, cargo_no[j]);
										stmt.setString(9, modification_seq_no[i]);
										stmt.setString(10, "R");
										stmt.setString(11, user_cd);
										stmt.setString(12, sales_rate[i]);
										stmt.setString(13, new_sales_rate[i]);
										stmt.setString(14, margin[j]);
										stmt.setString(15, new_margin[j]);
										stmt.setString(16, "0");
										stmt.setString(17, "0");
										stmt.setString(18, total_margin[j]);
										stmt.setString(19, new_total_margin[j]);
										stmt.setString(20, alloc_qty[j]);
										stmt.setString(21, new_eff_dt[i]);
										stmt.setString(22, cargo_seq[j]);
										stmt.executeUpdate();
										
										stmt.close();
									}
								}
								rset1.close();
								stmt1.close();
							}
							msg="Contract "+cont_name_map+" Price Change Request initiated";
							msg_type="S";
							dbcon.commit();
							
							if(cont_type[i].equals("F")||cont_type[i].equals("E")||cont_type[i].equals("W"))
							{
								dlng_flag=true;
							}
						}
					}
				}
			}
			if(dlng_flag)
			{
				url="../contract_master/frm_dlng_contract_price_modification.jsp?customer_cd="+customer_cd+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			}
			else
			{
				url="../contract_master/frm_contract_price_modification.jsp?customer_cd="+customer_cd+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			}
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Price Change Request initiation Failed";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
		}	
	}
	
	private void modifyContractPriceApproval(HttpServletRequest request) throws SQLException 
	{
		String function_nm="modifyContractPriceApproval()";

		String customer_cd = request.getParameter("customer_cd")==null?"":request.getParameter("customer_cd");

		try
		{
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,customer_cd);
			HttpSession session = request.getSession();
			String user_cd = (String)session.getAttribute("user_cd")==null?"":(String)session.getAttribute("user_cd");
			
			String fgsa[] = request.getParameterValues("fgsa");
			String fgsa_rev[] = request.getParameterValues("fgsa_rev");
			String sn[] = request.getParameterValues("sn");
			String sn_rev[] = request.getParameterValues("sn_rev");
			String cont_type[] = request.getParameterValues("cont_type");
			String segment[] = request.getParameterValues("segment");
			String price_flag[] = request.getParameterValues("price_flag");
			String modification_seq_no[] = request.getParameterValues("modification_seq_no");
			
			String sales_rate[] = request.getParameterValues("sales_rate");
			String new_sales_rate[] = request.getParameterValues("new_sales_rate");
			String new_eff_dt[] = request.getParameterValues("new_eff_dt");
			
			String cargo_no[] = request.getParameterValues("cargo_no");
			String cargo_seq[] = request.getParameterValues("cargo_seq");
			String cost_price[] = request.getParameterValues("cost_price");
			String alloc_qty[] = request.getParameterValues("alloc_qty");
			String margin[] = request.getParameterValues("margin");
			String new_margin[] = request.getParameterValues("new_margin");
			String total_margin[] = request.getParameterValues("total_margin");
			String new_total_margin[] = request.getParameterValues("new_total_margin");
			String cargo_sn_rev[] = request.getParameterValues("cargo_sn_rev");
			
			String action = request.getParameter("action")==null?"":request.getParameter("action");
			
			boolean dlng_flag=false;
			
			if(fgsa != null)
			{
				for(int i=0;i<fgsa.length; i++)
				{
					String cont_name_map = counterparty_abbr+"-"+cont_type[i]+fgsa[i]+"-"+sn[i];
					if(action.equals("reject"))
					{
						query = "UPDATE FMS_SUPPLY_ALLOC_REVISED SET FLAG=? "
								+ "WHERE AGMT_NO=? "
								+ "AND AGMT_REV=? AND CONT_NO=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
								+ "AND MODIFICATION_SEQ_NO=? AND FLAG=? AND COMPANY_CD=? ";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, "X");
						stmt.setString(2, fgsa[i]);
						stmt.setString(3, fgsa_rev[i]);
						stmt.setString(4, sn[i]);
						stmt.setString(5, customer_cd);
						stmt.setString(6, cont_type[i]);
						stmt.setString(7, modification_seq_no[i]);
						stmt.setString(8, "R");
						stmt.setString(9, comp_cd);
						stmt.executeUpdate();
						
						stmt.close();
						
						query = "UPDATE FMS_SUPPLY_CONT_MST SET PRICE_REQUEST_FLAG=? "
								+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONT_NO=? AND CONT_REV=? "
								+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? ";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, "N");
						stmt1.setString(2, comp_cd);
						stmt1.setString(3, fgsa[i]);
						stmt1.setString(4, fgsa_rev[i]);
						stmt1.setString(5, sn[i]);
						stmt1.setString(6, sn_rev[i]);
						stmt1.setString(7, customer_cd);
						stmt1.setString(8, cont_type[i]);
						stmt1.executeUpdate();
						
						stmt1.close();
						
						generateSupplyContractLog(customer_cd,sn[i],sn_rev[i],fgsa[i],fgsa_rev[i],cont_type[i],emp_cd);
						
						dbcon.commit();
						msg = "Sale Price Change Request for "+cont_name_map+" Rejected !!!";
						msg_type="S";
					}
					else
					{
						query = "UPDATE FMS_SUPPLY_ALLOC_REVISED SET APPROVE_BY=?, APPROVE_DT=SYSDATE, FLAG=? "
								+ "WHERE AGMT_NO=? "
								+ "AND AGMT_REV=? AND CONT_NO=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
								+ "AND MODIFICATION_SEQ_NO=? AND FLAG=? AND COMPANY_CD=? ";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, user_cd);
						stmt.setString(2, "A");
						stmt.setString(3, fgsa[i]);
						stmt.setString(4, fgsa_rev[i]);
						stmt.setString(5, sn[i]);
						stmt.setString(6, customer_cd);
						stmt.setString(7, cont_type[i]);
						stmt.setString(8, modification_seq_no[i]);
						stmt.setString(9, "R");
						stmt.setString(10, comp_cd);
						stmt.executeUpdate();
						
						stmt.close();
						
						query = "UPDATE FMS_SUPPLY_CONT_MST SET RATE=?,PRICE_APPROVE_FLAG=?,PRICE_REQUEST_FLAG=? "
								+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONT_NO=? AND CONT_REV=? "
								+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? ";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, new_sales_rate[i]);
						stmt1.setString(2, "Y");
						stmt1.setString(3, "N");
						stmt1.setString(4, comp_cd);
						stmt1.setString(5, fgsa[i]);
						stmt1.setString(6, fgsa_rev[i]);
						stmt1.setString(7, sn[i]);
						stmt1.setString(8, sn_rev[i]);
						stmt1.setString(9, customer_cd);
						stmt1.setString(10, cont_type[i]);
						stmt1.executeUpdate();
						
						stmt1.close();
						
						generateSupplyContractLog(customer_cd,sn[i],sn_rev[i],fgsa[i],fgsa_rev[i],cont_type[i],emp_cd);
						
						for(int j=0;j<cargo_no.length;j++) 
						{
							query1 = "UPDATE FMS_SUPPLY_PURCHASE_MAP_DTL SET SALE_PRICE=?, MARGIN=?,"
									+ "TOTAL_MARGIN=? "
									+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
									+ "AND CONT_NO=? AND CONT_REV=? AND PUR_CONT_NO=? "
									+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? AND CARGO_NO=? ";
							stmt2 = dbcon.prepareStatement(query1);
							stmt2.setString(1, new_sales_rate[i]);
							stmt2.setString(2, new_margin[j]);
							stmt2.setString(3, new_total_margin[j]);
							stmt2.setString(4, comp_cd);
							stmt2.setString(5, fgsa[i]);
							stmt2.setString(6, fgsa_rev[i]);
							stmt2.setString(7, sn[i]);
							stmt2.setString(8, cargo_sn_rev[j]);
							stmt2.setString(9, cargo_no[j]);
							stmt2.setString(10, customer_cd);
							stmt2.setString(11, cont_type[i]);
							stmt2.setString(12, cargo_seq[j]);
							stmt2.executeUpdate();
							stmt2.close();
						}
						dbcon.commit();
						msg = "Sale Price Change Request for "+cont_name_map+" Approved !";
						msg_type="S";
					}
					if(cont_type[i].equals("F")||cont_type[i].equals("E")||cont_type[i].equals("W"))
					{
						dlng_flag=true;
					}
				}
			}
			if(dlng_flag)
			{
				url="../contract_master/frm_dlng_contract_price_modification.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			}
			else
			{
				url="../contract_master/frm_contract_price_modification.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			}
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Sale Price Change Modification Failed";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
		}
	}
	
	//Added to store the contract price modification logs...
	private void generateSupplyContractLog(String counterparty_cd,String cont_no,String cont_rev_no, String agmt_no, String agmt_rev_no, String contract_type, String emp_cd) throws Exception
	{
		String function_nm="generateSupplyContractLog()";
		try
		{
			//ADD LOG IN LOG TABLE
			int log_seq_no=1;
			query = "SELECT MAX(LOG_SEQ_NO) "
					+ "FROM LOG_SUPPLY_CONT_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=?";
			stmt1 =dbcon.prepareStatement(query);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, cont_no);
			stmt1.setString(4, agmt_no);
			stmt1.setString(5, contract_type);
			rset1 = stmt1.executeQuery();
			if(rset1.next())
			{
				log_seq_no = (rset1.getInt(1)+1);
			}
			rset1.close();
			stmt1.close();
			
			query="INSERT INTO LOG_SUPPLY_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,LOG_SEQ_NO,LOG_BY,LOG_DT, "
					+ "CONT_NAME,CONT_REF_NO,TRADE_REF_NO,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,AGMT_BASE,AGMT_TYPE,TCQ,DCQ,VARIABLE_DCQ,QUANTITY_UNIT,"
					+ "RATE,RATE_UNIT,VARIABLE_RATE,POST_MARGIN,TRANSPORTATION_CHARGE,BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,"
					+ "SELLER_NOM_FLAG,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,"
					+ "MDCQ_FLAG,MDCQ_PERCENTAGE,FCC_FLAG,FCC_BY,FCC_DATE,REMARK,RENEWAL_DT,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,CONT_STATUS,IS_ALLOCATED,"
					+ "DDA_DT,DDA_TIME,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,TXN_CHARGE,BUYER_NOM_CUTOFF,TXN_UNIT,TCQ_SIGN,TCQ_REQUEST_FLAG,TCQ_REQUEST_CLOSE,"
					+ "TCQ_REQUEST_QTY,PRICE_REQUEST_FLAG,PRICE_APPROVE_FLAG,CHANGE_DATE_REQ, "
					+ "MEASUREMENT,MEASUREMENT_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR, "
					+ "OFF_SPEC_GAS,OFF_SPEC_GAS_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY, LIABILITY, "
					+ "LIABILITY_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG ,TERMINATE_CLAUSE,TERMINATE_PLANED , "
					+ "TERMINATE_FORCE,SF_GEN_DT ,CLOSE_EFF_DT ,CLOSURE_REQUEST_FLAG ,CLOSURE_ALLOC_QTY,CLOSURE_REMARK) "
					+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,?,?,SYSDATE,"
					+ "CONT_NAME,CONT_REF_NO,TRADE_REF_NO,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,AGMT_BASE,AGMT_TYPE,TCQ,DCQ,VARIABLE_DCQ,QUANTITY_UNIT,"
					+ "RATE,RATE_UNIT,VARIABLE_RATE,POST_MARGIN,TRANSPORTATION_CHARGE,BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,SELLER_NOM_FLAG,"
					+ "SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,"
					+ "MDCQ_FLAG,MDCQ_PERCENTAGE,FCC_FLAG,FCC_BY,FCC_DATE,REMARK,RENEWAL_DT,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,CONT_STATUS,IS_ALLOCATED,DDA_DT,DDA_TIME,"
					+ "BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,TXN_CHARGE,BUYER_NOM_CUTOFF,TXN_UNIT,TCQ_SIGN,TCQ_REQUEST_FLAG,TCQ_REQUEST_CLOSE,TCQ_REQUEST_QTY,"
					+ "PRICE_REQUEST_FLAG,PRICE_APPROVE_FLAG,CHANGE_DATE_REQ,MEASUREMENT,MEASUREMENT_CLAUSE,"
					+ "MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR, "
					+ "OFF_SPEC_GAS,OFF_SPEC_GAS_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY, LIABILITY, "
					+ "LIABILITY_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG ,TERMINATE_CLAUSE,TERMINATE_PLANED , "
					+ "TERMINATE_FORCE,SF_GEN_DT ,CLOSE_EFF_DT ,CLOSURE_REQUEST_FLAG ,CLOSURE_ALLOC_QTY,CLOSURE_REMARK "
					+ "FROM FMS_SUPPLY_CONT_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND CONT_REV=? "
					+ "AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONTRACT_TYPE=? ";
			stmt4= dbcon.prepareStatement(query);
			stmt4.setInt(1, log_seq_no);
			stmt4.setString(2, emp_cd);
			stmt4.setString(3, comp_cd);
			stmt4.setString(4, counterparty_cd);
			stmt4.setString(5, cont_no);
			stmt4.setString(6, cont_rev_no);
			stmt4.setString(7, agmt_no);
			stmt4.setString(8, agmt_rev_no);
			stmt4.setString(9, contract_type);
			stmt4.executeUpdate();
			stmt4.close();
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			throw e;
		}
	}
}
