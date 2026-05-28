package com.etrm.fms.inventory;

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
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.escapeSingleQuotes;

//Coded By          : Harsh Patel
//Code Reviewed by	:  
//CR Date			: 02/01/2023 
//Status	  		: Developing

@WebServlet("/servlet/Frm_EnergyBank")
public class Frm_EnergyBank extends HttpServlet
{
	static String db_src_file_name="Frm_EnergyBank.java";
	public static Connection dbcon;
	
	public static String servletName = "Frm_EnergyBank";
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
	
	static NumberFormat nf = new DecimalFormat("###########0.00");
	static NumberFormat nf2 = new DecimalFormat("###########0.0000");
	
	public static escapeSingleQuotes escObj = new escapeSingleQuotes();
	
	static UtilBean utilBean = new UtilBean();
	static DateUtil dateUtil = new DateUtil();
	
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
					u=request.getParameter("u")==null?"":request.getParameter("u");
					
					emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
					comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
					comp_abbr = (String)session.getAttribute("comp_abbr")==null?"":(String)session.getAttribute("comp_abbr");
					emp_nm = (String)session.getAttribute("emp_nm")==null?"":(String)session.getAttribute("emp_nm");
					ip = (String)session.getAttribute("ip")==null?"":(String)session.getAttribute("ip");
					
					new_value="";
					old_value="";
					
					option=request.getParameter("option")==null?"":request.getParameter("option");
					
					commonUrl_pra = "&u="+u;
					
					if(option.equalsIgnoreCase("MMBTU_ALLOCATION"))
					{
						InsertMmbtuAllocationDetail(request);
					}
					else if(option.equalsIgnoreCase("TCQ_MODIFICATION"))
					{
						UpdateTcqQuntityDetail(request);
					}
					else if(option.equalsIgnoreCase("TRADER_PRICE_CHANGE"))
					{
						InsertUpdatePriceModification(request);
					}
					else if(option.equalsIgnoreCase("TRADER_PRICE_REQUEST_APPROVE"))
					{
						ApprovedRejectPriceModificationRequest(request);
					}
					else if(option.equalsIgnoreCase("MMBTU_TRANSFER"))
					{
						InsertUpdateMmbtuTransferDetails(request);
					}
					else if(option.equalsIgnoreCase("PSEUDO_MMBTU_MODIFICATION"))
					{
						InsertUpdatePseudoMmbtuModification(request);
					}
					else if(option.equalsIgnoreCase("APPROVE_CLOSURE"))
					{
						ApproveClosureRequest(request);
					}
				}
				
				dbcon.close();
				dbcon=null;
			}
			catch(Exception e)
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
				url=CommonVariable.errorpage_url+"?e="+e;
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
		
		try {
		response.sendRedirect(url);
		} catch(IOException e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	private void InsertMmbtuAllocationDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertMmbtuAllocationDetail()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			String multiCountpty = request.getParameter("multiCountpty")==null?"":request.getParameter("multiCountpty");
			String multiAgmtNo = request.getParameter("multiAgmtNo")==null?"":request.getParameter("multiAgmtNo");
			String multiAgmtRev = request.getParameter("multiAgmtRev")==null?"":request.getParameter("multiAgmtRev");
			String multiContNo = request.getParameter("multiContNo")==null?"":request.getParameter("multiContNo");
			String multiContRev = request.getParameter("multiContRev")==null?"":request.getParameter("multiContRev");
			String multiContTyp = request.getParameter("multiContTyp")==null?"":request.getParameter("multiContTyp");
			String multiCargoNo = request.getParameter("multiCargoNo")==null?"":request.getParameter("multiCargoNo");
			
			String selected_index = request.getParameter("selected_index")==null?"":request.getParameter("selected_index");
			
			String[] counterparty_cd = request.getParameterValues("counterparty_cd");
			String[] agmt_no = request.getParameterValues("agmt_no");
			String[] agmt_rev_no = request.getParameterValues("agmt_rev_no");
			String[] cont_no = request.getParameterValues("cont_no");
			String[] cont_rev_no = request.getParameterValues("cont_rev_no");
			String[] contract_type = request.getParameterValues("contract_type");
			String[] rate = request.getParameterValues("rate");
			String[] rate_unit = request.getParameterValues("rate_unit");
			String[] avg_margin = request.getParameterValues("avg_margin");
			
			if(counterparty_cd != null)
			{
				for(int i=0; i<counterparty_cd.length; i++)
				{
					String dealMap=utilBean.NewDealMappingId(comp_cd, counterparty_cd[i], agmt_no[i], agmt_rev_no[i], cont_no[i], cont_rev_no[i], contract_type[i], "");
					String counterparty_abbr=utilBean.getCounterpartyABBR(dbcon,counterparty_cd[i]);
					String strMsg=counterparty_abbr+" "+dealMap;
					
					if(!selected_index.equals(""))
					{
						String[] sel_cont_no = request.getParameterValues("sel_cont_no"+selected_index);
						String[] sel_cargo_no = request.getParameterValues("sel_cargo_no"+selected_index);
						String[] sel_rate = request.getParameterValues("sel_rate"+selected_index);
						String[] sel_rate_unit = request.getParameterValues("sel_rate_unit"+selected_index);
						String[] alloc_qty = request.getParameterValues("alloc_qty"+selected_index);
						String[] margin = request.getParameterValues("margin"+selected_index);
						String[] total_margin = request.getParameterValues("total_margin"+selected_index);
						
						String qty_unit="1"; //MMBTU
						String status="Y";
						
						if(sel_rate != null)
						{
							int allocation_count=0;
							for(int j=0; j<sel_rate.length; j++)
							{
								if(!alloc_qty[j].trim().equals(""))
								{
									String contRevNo="0";
									query="SELECT COUNT(*) "
											+ "FROM FMS_SUPPLY_PURCHASE_MAP_DTL "
											+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
											+ "AND CONTRACT_TYPE=? AND PUR_CONT_NO=? AND CARGO_NO=?";
									stmt=dbcon.prepareStatement(query);
									stmt.setString(1, comp_cd);
									stmt.setString(2, counterparty_cd[i]);
									stmt.setString(3, agmt_no[i]);
									stmt.setString(4, agmt_rev_no[i]);
									stmt.setString(5, cont_no[i]);
									stmt.setString(6, contract_type[i]);
									stmt.setString(7, sel_cont_no[j]);
									stmt.setString(8, sel_cargo_no[j]);
									rset=stmt.executeQuery();
									if(rset.next())
									{
										contRevNo=""+rset.getInt(1);
									}
									rset.close();
									stmt.close();
									
									query="INSERT INTO FMS_SUPPLY_PURCHASE_MAP_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
											+ "CONTRACT_TYPE,PUR_CONT_NO,ALLOC_QTY,QTY_UNIT,SALE_PRICE,RATE_UNIT,"
											+ "COST_PRICE,CP_UNIT,MARGIN,TOTAL_MARGIN,STATUS,ENT_BY,ENT_DT,"
											+ "AVG_MARGIN,CARGO_NO) "
											+ "VALUES(?,?,?,?,?,?,"
											+ "?,?,?,?,?,?,"
											+ "?,?,?,?,?,?,SYSDATE,"
											+ "?,?)";
									stmt=dbcon.prepareStatement(query);
									stmt.setString(1, comp_cd);
									stmt.setString(2, counterparty_cd[i]);
									stmt.setString(3, agmt_no[i]);
									stmt.setString(4, agmt_rev_no[i]);
									stmt.setString(5, cont_no[i]);
									stmt.setString(6, contRevNo);
									stmt.setString(7, contract_type[i]);
									stmt.setString(8, sel_cont_no[j]);
									stmt.setString(9, alloc_qty[j]);
									stmt.setString(10, qty_unit);
									stmt.setString(11, rate[i]);
									stmt.setString(12, rate_unit[i]);
									stmt.setString(13, sel_rate[j]);
									stmt.setString(14, sel_rate_unit[j]);
									stmt.setString(15, margin[j]);
									stmt.setString(16, total_margin[j]);
									stmt.setString(17, status);
									stmt.setString(18, emp_cd);
									stmt.setString(19, avg_margin[i]);
									stmt.setString(20, sel_cargo_no[j]);
									stmt.executeUpdate();
									
									String cont_st_dt="";
									query="SELECT TO_CHAR(START_DT,'DD/MM/YYYY') "
											+ "FROM FMS_SUPPLY_CONT_MST A "
											+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND AGMT_NO=? AND AGMT_REV=? "
											+ "AND CONT_NO=? AND CONTRACT_TYPE=? "
											+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
											+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
											+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
									stmt=dbcon.prepareStatement(query);
									stmt.setString(1, comp_cd);
									stmt.setString(2, counterparty_cd[i]);
									stmt.setString(3, agmt_no[i]);
									stmt.setString(4, agmt_rev_no[i]);
									stmt.setString(5, cont_no[i]);
									stmt.setString(6, contract_type[i]);
									rset=stmt.executeQuery();
									if(rset.next())
									{
										cont_st_dt=rset.getString(1)==null?"":rset.getString(1);
									}
									rset.close();
									stmt.close();
									
									int ModSeQNo=0;
									query="SELECT MAX(MODIFICATION_SEQ_NO) "+
											" FROM FMS_SUPPLY_ALLOC_REVISED "
											+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND AGMT_NO=? AND AGMT_REV=? "
											+ "AND CONT_NO=? AND CONT_REV=? "
											+ "AND CONTRACT_TYPE=? AND PUR_CONT=? AND CARGO_NO=?";
									stmt=dbcon.prepareStatement(query);
									stmt.setString(1, comp_cd);
									stmt.setString(2, counterparty_cd[i]);
									stmt.setString(3, agmt_no[i]);
									stmt.setString(4, agmt_rev_no[i]);
									stmt.setString(5, cont_no[i]);
									stmt.setString(6, contRevNo);
									stmt.setString(7, contract_type[i]);
									stmt.setString(8, sel_cont_no[j]);
									stmt.setString(9, sel_cargo_no[j]);
									rset=stmt.executeQuery();
									if(rset.next()) 
									{
										ModSeQNo=rset.getInt(1);
									}
									rset.close();
									stmt.close();
									ModSeQNo++;
									
									query="INSERT INTO FMS_SUPPLY_ALLOC_REVISED(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
											+ "CONTRACT_TYPE,PUR_CONT,MODIFICATION_SEQ_NO,ALLOC_QTY,NEW_SALE_PRICE,"
											+ "NEW_MARGIN,NEW_TOT_MARGIN,ENT_BY,ENT_DT,"
											+ "NEW_AVG_MARGIN,FLAG,NEW_PRICE_EFF_DT,CARGO_NO) "
											+ "VALUES(?,?,?,?,?,?,"
											+ "?,?,?,?,?,"
											+ "?,?,?,SYSDATE,"
											+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?)";
									stmt=dbcon.prepareStatement(query);
									stmt.setString(1, comp_cd);
									stmt.setString(2, counterparty_cd[i]);
									stmt.setString(3, agmt_no[i]);
									stmt.setString(4, agmt_rev_no[i]);
									stmt.setString(5, cont_no[i]);
									stmt.setString(6, contRevNo);
									stmt.setString(7, contract_type[i]);
									stmt.setString(8, sel_cont_no[j]);
									stmt.setString(9, ""+ModSeQNo);
									stmt.setString(10, alloc_qty[j]);
									stmt.setString(11, rate[i]);
									stmt.setString(12, margin[j]);
									stmt.setString(13, total_margin[j]);
									stmt.setString(14, emp_cd);
									stmt.setString(15, avg_margin[i]);
									stmt.setString(16, "A");
									stmt.setString(17, cont_st_dt);
									stmt.setString(18, sel_cargo_no[j]);
									stmt.executeUpdate();
									
									insertTransferDetail(counterparty_cd[i], agmt_no[i], agmt_rev_no[i], cont_no[i], cont_rev_no[i], contract_type[i], sel_cont_no[j], sel_cont_no[j], alloc_qty[j], "Volume Allocation", sel_cargo_no[j],sel_cargo_no[j]);
									
									if(sel_cont_no[j].equals("0") && !sel_cargo_no[j].equals("0"))	//PB20250214: For storing pseudo mmbtu logs
									{
										insertPseudoTransferDetails(counterparty_cd[i],agmt_no[i],agmt_rev_no[i],cont_no[i],cont_rev_no[i],contract_type[i],sel_cont_no[j],sel_cont_no[j],alloc_qty[j],"Volume Allocation",sel_cargo_no[j],sel_cargo_no[j]);
									}
									
									allocation_count++;
								}
							}
							
							if(allocation_count > 0)
							{
								int count=0;
								
								query="SELECT COUNT(*) "
										+ "FROM FMS_SUPPLY_CONT_MST "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
										+ "AND CONTRACT_TYPE=? AND CONT_REV=?";
								stmt=dbcon.prepareStatement(query);
								stmt.setString(1, comp_cd);
								stmt.setString(2, counterparty_cd[i]);
								stmt.setString(3, agmt_no[i]);
								stmt.setString(4, agmt_rev_no[i]);
								stmt.setString(5, cont_no[i]);
								stmt.setString(6, contract_type[i]);
								stmt.setString(7, cont_rev_no[i]);
								rset=stmt.executeQuery();
								if(rset.next())
								{
									count=rset.getInt(1);
								}
								rset.close();
								stmt.close();
										
								if(count > 0)
								{
									query="UPDATE FMS_SUPPLY_CONT_MST SET IS_ALLOCATED='Y' "
											+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
											+ "AND CONTRACT_TYPE=? AND CONT_REV=?";
									stmt=dbcon.prepareStatement(query);
									stmt.setString(1, comp_cd);
									stmt.setString(2, counterparty_cd[i]);
									stmt.setString(3, agmt_no[i]);
									stmt.setString(4, agmt_rev_no[i]);
									stmt.setString(5, cont_no[i]);
									stmt.setString(6, contract_type[i]);
									stmt.setString(7, cont_rev_no[i]);
									stmt.executeUpdate();
									
									msg = "Successful! - MMBTU Allocation for "+strMsg+" submitted Successfully!";
									msg_type = "S";
								}
							}
							else
							{
								msg = "Failed! - MMBTU Allocation for "+strMsg+" submission Failed!";
								msg_type = "E";
							}
						}
						else
						{
							msg = "Failed! - MMBTU Allocation for "+strMsg+" submission Failed!";
							msg_type = "E";
						}
					}
					else
					{
						msg = "Failed! - MMBTU Allocation for "+strMsg+" submission Failed!";
						msg_type = "E";
					}
				}
			}
			else
			{
				msg = "Failed! - MMBTU Allocation submission Failed!";
				msg_type = "E";
			}
			
			url = "../inventory/frm_allocation.jsp?msg="+msg+"&msg_type="+msg_type+"&clearance="+clearance+
					"&multiCountpty="+multiCountpty+"&multiAgmtNo="+multiAgmtNo+"&multiAgmtRev="+multiAgmtRev+
					"&multiContNo="+multiContNo+"&multiContRev="+multiContRev+"&multiContTyp="+multiContTyp+"&multiCargoNo="+multiCargoNo+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - MMBTU Allocation submission Failed!";
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
	
	private void UpdateTcqQuntityDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="UpdateTcqQuntityDetail()";
		msg="";
		msg_type="";
		url="";
		
		String use_flag = request.getParameter("use_flag")==null?"":request.getParameter("use_flag");
		try
		{
			String sales_counterparty_cd = request.getParameter("sales_counterparty_cd")==null?"0":request.getParameter("sales_counterparty_cd");
			String sales_agmt_no = request.getParameter("sales_agmt_no")==null?"":request.getParameter("sales_agmt_no");
			String sales_agmt_rev_no = request.getParameter("sales_agmt_rev_no")==null?"":request.getParameter("sales_agmt_rev_no");
			String sales_cont_no = request.getParameter("sales_cont_no")==null?"":request.getParameter("sales_cont_no");
			String sales_cont_rev_no = request.getParameter("sales_cont_rev_no")==null?"":request.getParameter("sales_cont_rev_no");
			String sales_contract_type=request.getParameter("sales_contract_type")==null?"":request.getParameter("sales_contract_type");
			String sales_rate = request.getParameter("sales_rate")==null?"":request.getParameter("sales_rate");
			String sales_rate_unit=request.getParameter("sales_rate_unit")==null?"":request.getParameter("sales_rate_unit");
			
			String dealMap=utilBean.NewDealMappingId(comp_cd, sales_counterparty_cd, sales_agmt_no, sales_agmt_rev_no, sales_cont_no, sales_cont_rev_no, sales_contract_type, "");
			String counterparty_abbr=utilBean.getCounterpartyABBR(dbcon,sales_counterparty_cd);
			String strMsg=counterparty_abbr+" "+dealMap;
			
			String tcq_sign = request.getParameter("tcq_sign")==null?"":request.getParameter("tcq_sign");
			String var_tcq=request.getParameter("var_tcq")==null?"":request.getParameter("var_tcq");
			String tcq=request.getParameter("tcq")==null?"":request.getParameter("tcq");
			
			String[] counterparty_cd = request.getParameterValues("counterparty_cd");
			String[] agmt_no = request.getParameterValues("agmt_no");
			String[] agmt_rev_no = request.getParameterValues("agmt_rev_no");
			String[] cont_no = request.getParameterValues("cont_no");
			String[] cont_rev_no = request.getParameterValues("cont_rev_no");
			String[] contract_type = request.getParameterValues("contract_type");
			String[] allocate_qty = request.getParameterValues("allocate_qty");
			String[] rate = request.getParameterValues("rate");
			String[] rate_unit = request.getParameterValues("rate_unit");
			String[] cargo_no = request.getParameterValues("cargo_no");
			
			String closure_eff_dt = request.getParameter("closure_eff_dt")==null?"":request.getParameter("closure_eff_dt");
			
			String qty_unit="1"; //MMBTU
			String status="Y";
			
			String transfer_type = use_flag.equals("C")?"Deal Closure":"TCQ Modification";
			
			boolean isDone=false;
			
			if(counterparty_cd != null)
			{
				for(int i=0; i<counterparty_cd.length; i++)
				{
					if(!allocate_qty[i].trim().equals(""))
					{
						String alloc_qty=allocate_qty[i];
						if(tcq_sign.equals("-"))
						{
							alloc_qty=tcq_sign+""+allocate_qty[i];
						}
						
						String contRevNo="0";
						query="SELECT COUNT(*) "
								+ "FROM FMS_SUPPLY_PURCHASE_MAP_DTL "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
								+ "AND CONTRACT_TYPE=? AND PUR_CONT_NO=? AND CARGO_NO=? ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, sales_counterparty_cd);
						stmt.setString(3, sales_agmt_no);
						stmt.setString(4, sales_agmt_rev_no);
						stmt.setString(5, sales_cont_no);
						stmt.setString(6, sales_contract_type);
						stmt.setString(7, cont_no[i]);
						stmt.setString(8, cargo_no[i]);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							contRevNo=""+rset.getInt(1);
						}
						rset.close();
						stmt.close();
						
						query="INSERT INTO FMS_SUPPLY_PURCHASE_MAP_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
								+ "CONTRACT_TYPE,PUR_CONT_NO,ALLOC_QTY,QTY_UNIT,SALE_PRICE,RATE_UNIT,"
								+ "COST_PRICE,CP_UNIT,MARGIN,TOTAL_MARGIN,STATUS,ENT_BY,ENT_DT,"
								+ "AVG_MARGIN,CARGO_NO) "
								+ "VALUES(?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,SYSDATE,"
								+ "?,?)";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, sales_counterparty_cd);
						stmt.setString(3, sales_agmt_no);
						stmt.setString(4, sales_agmt_rev_no);
						stmt.setString(5, sales_cont_no);
						stmt.setString(6, contRevNo);
						stmt.setString(7, sales_contract_type);
						stmt.setString(8, cont_no[i]);
						stmt.setString(9, alloc_qty);
						stmt.setString(10, qty_unit);
						stmt.setString(11, sales_rate);
						stmt.setString(12, sales_rate_unit);
						stmt.setString(13, rate[i]);
						stmt.setString(14, rate_unit[i]);
						stmt.setString(15, "");
						stmt.setString(16, "");
						stmt.setString(17, status);
						stmt.setString(18, emp_cd);
						stmt.setString(19, "");
						stmt.setString(20, cargo_no[i]);
						stmt.executeUpdate();
						stmt.close();
						
						insertTransferDetail(sales_counterparty_cd, sales_agmt_no, sales_agmt_rev_no, sales_cont_no, sales_cont_rev_no, sales_contract_type, cont_no[i], cont_no[i], alloc_qty, transfer_type,cargo_no[i],cargo_no[i]);
						
						//PB20250218: For storing the pseudo cargo logs...
						if(cont_no[i].equals("0") && !cargo_no[i].equals("0"))
						{
							insertPseudoTransferDetails(sales_counterparty_cd, sales_agmt_no, sales_agmt_rev_no, sales_cont_no, sales_cont_rev_no, sales_contract_type, cont_no[i], cont_no[i], alloc_qty, transfer_type,cargo_no[i],cargo_no[i]);
						}
						isDone=true;
					}
				}
				
				if(isDone)
				{
					String clsr_qty="";
					if(tcq_sign.equals("+"))
					{
						clsr_qty="-"+nf.format(Double.parseDouble(var_tcq));
						tcq=nf.format(Double.parseDouble(tcq)+Double.parseDouble(var_tcq));
					}
					else if(tcq_sign.equals("-"))
					{
						clsr_qty=nf.format(Double.parseDouble(var_tcq));
						tcq=nf.format(Double.parseDouble(tcq)-Double.parseDouble(var_tcq));
					}
					
					if(use_flag.equals("C"))
					{
						insertClosureDetail(sales_counterparty_cd,sales_agmt_no,  sales_agmt_rev_no,  sales_cont_no,  sales_cont_rev_no,  sales_contract_type,  closure_eff_dt,  clsr_qty);
						
						msg = "Successful! - Contract Closure for "+strMsg+" submitted Successfully!";
						msg_type = "S";
					}
					else
					{
						query="UPDATE FMS_SUPPLY_CONT_MST SET TCQ=?,TCQ_REQUEST_FLAG=?,TCQ_REQUEST_CLOSE=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONT_NO=? AND CONT_REV=? "
								+ "AND CONTRACT_TYPE=?";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, tcq);
						stmt.setString(2, "N");
						stmt.setString(3, "Y");
						stmt.setString(4, comp_cd);
						stmt.setString(5, sales_counterparty_cd);
						stmt.setString(6, sales_agmt_no);
						stmt.setString(7, sales_agmt_rev_no);
						stmt.setString(8, sales_cont_no);
						stmt.setString(9, sales_cont_rev_no);
						stmt.setString(10, sales_contract_type);
						stmt.executeUpdate();
						stmt.close();
						
						msg = "Successful! - TCQ MMBTU Update for "+strMsg+" submitted Successfully!";
						msg_type = "S";
					}
				}
				else
				{
					if(use_flag.equals("C"))
					{
						msg = "Failed! - Contract Closure for "+strMsg+" submission Failed!";
						msg_type = "E";
					}
					else
					{
						msg = "Failed! - TCQ MMBTU Update for "+strMsg+" submission Failed!";
						msg_type = "E";
					}
				}
			}
			
			url = "../contract_master/frm_tcq_modification.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			if(use_flag.equals("C"))
			{
				msg = "Error in Exception! - Contract Closure Approval Failed";
			}
			else
			{
				msg = "Error in Exception! - TCQ MMBTU Update Failed!";
			}
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
	
	public void insertClosureDetail(String counterparty_cd, String agmt, String agmt_rev, String cont, String cont_rev, String cont_type, String closure_eff_dt, String closure_qty) throws SQLException
	{
		String function_nm="insertClosureDetail()";
		try
		{
			query="UPDATE FMS_SUPPLY_CONT_MST SET CONT_STATUS=?,CLOSE_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),CLOSURE_REQUEST_FLAG=?,"
					+ "FCC_FLAG=?,FCC_BY=?,CLOSURE_ALLOC_QTY=?,FCC_DATE=SYSDATE "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONT_NO=? AND CONT_REV=? "
					+ "AND CONTRACT_TYPE=?";
			stmt=dbcon.prepareStatement(query);
			stmt.setString(1,"C");
			stmt.setString(2, closure_eff_dt);
			stmt.setString(3,"A");
			stmt.setString(4,"Y");
			stmt.setString(5,emp_cd);
			stmt.setString(6,closure_qty);
			stmt.setString(7, comp_cd);
			stmt.setString(8, counterparty_cd);
			stmt.setString(9, agmt);
			stmt.setString(10,agmt_rev);
			stmt.setString(11,cont);
			stmt.setString(12,cont_rev);
			stmt.setString(13,cont_type);
			stmt.executeUpdate();
			stmt.close();
		}
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			throw e;
		}
	}
	
	public void insertTransferDetail(String counterparty_cd,String agmt,String agmt_rev,String cont,String cont_rev,String cont_type,String new_pur_cont_no,String old_pur_cont_no,String alloc_qty,String transfer_type,String new_cargo_no,String old_cargo_no) throws SQLException
	{
		String function_nm="insertTransferDetail()";
		try 
		{
			int max_seq_no=1;
			query="SELECT MAX(SEQ_NO) "
					+ "FROM FMS_TRADE_SUPPLY_TRANSFER "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=?";
			stmt=dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt);
			stmt.setString(4, cont);
			stmt.setString(5, cont_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				if(rset.getString(1) != null)
				{
					max_seq_no = rset.getInt(1)+1;
				}
				else
				{
					max_seq_no=1;
				}
			}
			else
			{
				max_seq_no=1;
			}
			rset.close();
			stmt.close();
			
			query="INSERT INTO FMS_TRADE_SUPPLY_TRANSFER(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
					+ "SEQ_NO,NEW_PUR_CONT_NO,OLD_PUR_CONT_NO,ALLOC_QTY,TRANSFER_TYPE,ENT_BY,ENT_DT,NEW_CARGO_NO,OLD_CARGO_NO) "
					+ "VALUES(?,?,?,?,?,?,?,"
					+ "?,?,?,"
					+ "?,?,?,sysdate,?,?)";
			stmt=dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt);
			stmt.setString(4, agmt_rev);
			stmt.setString(5, cont);
			stmt.setString(6, cont_rev);
			stmt.setString(7, cont_type);
			stmt.setString(8, ""+max_seq_no);
			stmt.setString(9, new_pur_cont_no);
			stmt.setString(10, old_pur_cont_no);
			stmt.setString(11, alloc_qty);
			stmt.setString(12, transfer_type);
			stmt.setString(13, emp_cd);
			stmt.setString(14, new_cargo_no);
			stmt.setString(15, old_cargo_no);
			stmt.executeQuery();
		}
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			throw e;
		}
	}
	
	private void InsertUpdatePriceModification(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdatePriceModification()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			String multiCountpty = request.getParameter("multiCountpty")==null?"":request.getParameter("multiCountpty");
			String multiAgmtNo = request.getParameter("multiAgmtNo")==null?"":request.getParameter("multiAgmtNo");
			String multiAgmtRev = request.getParameter("multiAgmtRev")==null?"":request.getParameter("multiAgmtRev");
			String multiContNo = request.getParameter("multiContNo")==null?"":request.getParameter("multiContNo");
			String multiContRev = request.getParameter("multiContRev")==null?"":request.getParameter("multiContRev");
			String multiContTyp = request.getParameter("multiContTyp")==null?"":request.getParameter("multiContTyp");
			
			String exchgRate = request.getParameter("exchgRate")==null?"":request.getParameter("exchgRate");
			
			String[] counterparty_cd = request.getParameterValues("counterparty_cd");
			String[] agmt_no = request.getParameterValues("agmt_no");
			String[] agmt_rev_no = request.getParameterValues("agmt_rev_no");
			String[] cont_no = request.getParameterValues("cont_no");
			String[] cont_rev_no = request.getParameterValues("cont_rev_no");
			String[] contract_type = request.getParameterValues("contract_type");
			String[] rate = request.getParameterValues("rate");
			String[] rate_unit = request.getParameterValues("rate_unit");
			String[] new_rate = request.getParameterValues("new_rate");
			String[] new_eff_dt = request.getParameterValues("new_eff_dt");
			String[] change_seq_no = request.getParameterValues("change_seq_no");
			
			if(counterparty_cd != null)
			{
				for(int i=0; i<counterparty_cd.length; i++)
				{
					String dealMap=utilBean.NewDealMappingId(comp_cd, counterparty_cd[i], agmt_no[i], agmt_rev_no[i], cont_no[i], cont_rev_no[i], contract_type[i], "");
					String counterparty_abbr=utilBean.getCounterpartyABBR(dbcon,counterparty_cd[i]);
					String strMsg=counterparty_abbr+" "+dealMap;
					
					if(change_seq_no[i].equals(""))
					{
						String seq_no="1";
						query="SELECT MAX(NVL(SEQ_NO,0)) "
								+ "FROM FMS_TRADER_CONT_PRICE_DTL "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND CONT_NO=? "
								+ "AND CONTRACT_TYPE=? ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, counterparty_cd[i]);
						stmt.setString(3, agmt_no[i]);
						stmt.setString(4, cont_no[i]);
						stmt.setString(5, contract_type[i]);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							seq_no = ""+(rset.getInt(1)+1);
						}
						rset.close();
						stmt.close();
						
						query="INSERT INTO FMS_TRADER_CONT_PRICE_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
								+ "EFF_DT,SEQ_NO,OLD_PRICE,NEW_PRICE,ENT_BY,ENT_DT,FLAG,PRICE_UNIT) "
								+ "VALUES(?,?,?,?,?,?,?,"
								+ "TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,SYSDATE,?,?)";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, counterparty_cd[i]);
						stmt.setString(3, agmt_no[i]);
						stmt.setString(4, agmt_rev_no[i]);
						stmt.setString(5, cont_no[i]);
						stmt.setString(6, cont_rev_no[i]);
						stmt.setString(7, contract_type[i]);
						stmt.setString(8, new_eff_dt[i]);
						stmt.setString(9, seq_no);
						stmt.setString(10, rate[i]);
						stmt.setString(11, new_rate[i]);
						stmt.setString(12, emp_cd);
						stmt.setString(13, "R");
						stmt.setString(14, rate_unit[i]);
						stmt.executeQuery();
						
						msg="Successful! - Contract Price Change Request for "+strMsg+" initiated Successfully!";
						msg_type="S";
					}
					else
					{
						query="UPDATE FMS_TRADER_CONT_PRICE_DTL SET EFF_DT=TO_DATE(?,'DD/MM/YYYY'),OLD_PRICE=?,NEW_PRICE=?, "
								+ "MODIFIY_BY=?,MODIFIY_DT=SYSDATE  "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND CONT_NO=? "
								+ "AND CONTRACT_TYPE=? AND SEQ_NO=? ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, new_eff_dt[i]);
						stmt.setString(2, rate[i]);
						stmt.setString(3, new_rate[i]);
						stmt.setString(4, emp_cd);
						stmt.setString(5, comp_cd);
						stmt.setString(6, counterparty_cd[i]);
						stmt.setString(7, agmt_no[i]);
						stmt.setString(8, cont_no[i]);
						stmt.setString(9, contract_type[i]);
						stmt.setString(10, change_seq_no[i]);
						stmt.executeQuery();
						
						msg="Successful! - Contract Price Change Request for "+strMsg+" Modified Successfully!";
						msg_type="S";
					}
						
					query="UPDATE FMS_SUPPLY_PURCHASE_MAP_DTL SET STATUS=?, AUTH_BY=?, AUTH_DT=sysdate "
						+ "WHERE COMPANY_CD=? AND PUR_CONT_NO=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, "N");
					stmt.setString(2, emp_cd);
					stmt.setString(3, comp_cd);
					stmt.setString(4, cont_no[i]);
					stmt.executeUpdate();
				}
			}
			else
			{
				msg = "Failed! - Contract Price Change Request initiation Failed!";
				msg_type = "E";
			}
			
			url = "../inventory/frm_trade_cont_price_modification.jsp?msg="+msg+"&msg_type="+msg_type+"&clearance="+clearance+
					"&multiCountpty="+multiCountpty+"&multiAgmtNo="+multiAgmtNo+"&multiAgmtRev="+multiAgmtRev+
					"&multiContNo="+multiContNo+"&multiContRev="+multiContRev+"&multiContTyp="+multiContTyp+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Contract Price Change Request initiation Failed!";
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
	
	public void insertIntoFMS_SUPPLY_ALLOC_REVISED(String cargo_ref_cd, String fgsa_no, String fgsa_rev_no, String sn_no,String sn_rev_no, 
			String customer_cd,String contract_type, String old_cost_price,String old_margin, String old_total_margin, 
			String new_cost_price,String new_margin, String new_total_margin, String eff_dt, String user_cd, String alloc_qty) throws SQLException
	{
		String function_nm="insertIntoFMS_SUPPLY_ALLOC_REVISED()";
		try
		{
			int ModSeQNo=0;
			int count = 0;
			
			String queryString11="";
			queryString11 = "SELECT MAX(MODIFICATION_SEQ_NO) "+
					" FROM FMS_SUPPLY_ALLOC_REVISED "
					+ "WHERE COMPANY_CD=? AND PUR_CONT=? "
					+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? AND CONT_REV=? "
					+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? AND NEW_PRICE_EFF_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND FLAG=? ";
			stmt3=dbcon.prepareStatement(queryString11);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, cargo_ref_cd);
			stmt3.setString(3, fgsa_no);
			stmt3.setString(4, fgsa_rev_no);
			stmt3.setString(5, sn_no);
			stmt3.setString(6, sn_rev_no);
			stmt3.setString(7, customer_cd);
			stmt3.setString(8, contract_type);
			stmt3.setString(9, eff_dt);
			stmt3.setString(10, "R");
			rset1 = stmt3.executeQuery();
			if(rset1.next()) 
			{
				count=rset1.getInt(1);
			}
			else
			{
				count=0;
			}
			rset1.close();
			stmt3.close();
			
			if(count==0) 
			{
				String queryString1 ="";
				queryString1 = "SELECT MAX(MODIFICATION_SEQ_NO) "+
						" FROM FMS_SUPPLY_ALLOC_REVISED "
						+ "WHERE COMPANY_CD=? AND PUR_CONT=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? AND CONT_REV=? "
						+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=?";
				stmt3=dbcon.prepareStatement(queryString1);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, cargo_ref_cd);
				stmt3.setString(3, fgsa_no);
				stmt3.setString(4, fgsa_rev_no);
				stmt3.setString(5, sn_no);
				stmt3.setString(6, sn_rev_no);
				stmt3.setString(7, customer_cd);
				stmt3.setString(8, contract_type);
				rset1 = stmt3.executeQuery();
				if(rset1.next()) 
				{
					ModSeQNo=rset1.getInt(1);
				}
				else
				{
					ModSeQNo=0;
				}
				ModSeQNo++;
				rset1.close();
				stmt3.close();
				
				query = "INSERT INTO FMS_SUPPLY_ALLOC_REVISED(COMPANY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,COUNTERPARTY_CD,"
						+ "CONTRACT_TYPE,PUR_CONT,MODIFICATION_SEQ_NO,FLAG,ENT_BY,ENT_DT,"
						+ "ORI_SALE_PRICE,NEW_SALE_PRICE,ORI_MARGIN,NEW_MARGIN,ORI_AVG_MARGIN,"
						+ "NEW_AVG_MARGIN,ORI_TOT_MARGIN,NEW_TOT_MARGIN,ALLOC_QTY, NEW_PRICE_EFF_DT) VALUES(?,?"
						+ ",?,?,?,?,?,"
						+ "?,?,?,?,SYSDATE,?,?,"
						+ "?,?,?,?,?,"
						+ "?,?,TO_DATE(?,'DD/MM/YYYY')) ";
				stmt3=dbcon.prepareStatement(query);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, fgsa_no);
				stmt3.setString(3, fgsa_rev_no);
				stmt3.setString(4, sn_no);
				stmt3.setString(5, sn_rev_no);
				stmt3.setString(6, customer_cd);
				stmt3.setString(7, contract_type);
				stmt3.setString(8, cargo_ref_cd);
				stmt3.setString(9, ""+ModSeQNo);
				stmt3.setString(10, "R");
				stmt3.setString(11, user_cd);
				stmt3.setString(12, old_cost_price);
				stmt3.setString(13, new_cost_price);
				stmt3.setString(14, old_margin);
				stmt3.setString(15, new_margin);
				stmt3.setString(16, old_margin);
				stmt3.setString(17, new_margin);
				stmt3.setString(18, old_total_margin);
				stmt3.setString(19, new_total_margin);
				stmt3.setString(20, alloc_qty);
				stmt3.setString(21, eff_dt);
				stmt3.executeUpdate();
			
			}
			else if(count>0) 
			{ //ENTRY EXISTS..BUT NOT APPROVED..
				ModSeQNo=count;
				
				query = "UPDATE FMS_SUPPLY_ALLOC_REVISED SET  NEW_SALE_PRICE=?,"
						+ "NEW_MARGIN=?,NEW_AVG_MARGIN=?,"
						+ "NEW_TOT_MARGIN=?,ENT_BY=?,ENT_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND PUR_CONT=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? AND CONT_REV=? "
						+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? AND NEW_PRICE_EFF_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND FLAG=? ";
				stmt3=dbcon.prepareStatement(query);
				stmt3.setString(1, new_cost_price);
				stmt3.setString(2, new_margin);
				stmt3.setString(3, new_margin);
				stmt3.setString(4, new_total_margin);
				stmt3.setString(5, user_cd);
				stmt3.setString(6, comp_cd);
				stmt3.setString(7, cargo_ref_cd);
				stmt3.setString(8, fgsa_no);
				stmt3.setString(9, fgsa_rev_no);
				stmt3.setString(10, sn_no);
				stmt3.setString(11, sn_rev_no);
				stmt3.setString(12, customer_cd);
				stmt3.setString(13, contract_type);
				stmt3.setString(14, eff_dt);
				stmt3.setString(15, "R");
				stmt3.executeUpdate();
			}

		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			throw e;  
		}
	}
	
	private void ApprovedRejectPriceModificationRequest(HttpServletRequest request) throws SQLException 
	{
		String function_nm="ApprovedRejectPriceModificationRequest()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			String multiCountpty = request.getParameter("multiCountpty")==null?"":request.getParameter("multiCountpty");
			String multiAgmtNo = request.getParameter("multiAgmtNo")==null?"":request.getParameter("multiAgmtNo");
			String multiAgmtRev = request.getParameter("multiAgmtRev")==null?"":request.getParameter("multiAgmtRev");
			String multiContNo = request.getParameter("multiContNo")==null?"":request.getParameter("multiContNo");
			String multiContRev = request.getParameter("multiContRev")==null?"":request.getParameter("multiContRev");
			String multiContTyp = request.getParameter("multiContTyp")==null?"":request.getParameter("multiContTyp");
			
			String exchgRate = request.getParameter("exchgRate")==null?"":request.getParameter("exchgRate");
			String action = request.getParameter("action")==null?"":request.getParameter("action");
			
			String[] counterparty_cd = request.getParameterValues("counterparty_cd");
			String[] agmt_no = request.getParameterValues("agmt_no");
			String[] agmt_rev_no = request.getParameterValues("agmt_rev_no");
			String[] cont_no = request.getParameterValues("cont_no");
			String[] cont_rev_no = request.getParameterValues("cont_rev_no");
			String[] contract_type = request.getParameterValues("contract_type");
			String[] rate = request.getParameterValues("rate");
			String[] rate_unit = request.getParameterValues("rate_unit");
			String[] new_rate = request.getParameterValues("new_rate");
			String[] new_eff_dt = request.getParameterValues("new_eff_dt");
			String[] change_seq_no = request.getParameterValues("change_seq_no");
			
			if(counterparty_cd != null)
			{
				for(int i=0; i<counterparty_cd.length; i++)
				{
					String dealMap=utilBean.NewDealMappingId(comp_cd, counterparty_cd[i], agmt_no[i], agmt_rev_no[i], cont_no[i], cont_rev_no[i], contract_type[i], "");
					String counterparty_abbr=utilBean.getCounterpartyABBR(dbcon,counterparty_cd[i]);
					String strMsg=counterparty_abbr+" "+dealMap;
					
					if(action.equals("approve"))
					{
						query="UPDATE FMS_TRADER_CONT_MST SET RATE=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONT_NO=? AND CONT_REV=? "
								+ "AND CONTRACT_TYPE=?";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, new_rate[i]);
						stmt.setString(2, emp_cd);
						stmt.setString(3, comp_cd);
						stmt.setString(4, counterparty_cd[i]);
						stmt.setString(5, agmt_no[i]);
						stmt.setString(6, agmt_rev_no[i]);
						stmt.setString(7, cont_no[i]);
						stmt.setString(8, cont_rev_no[i]);
						stmt.setString(9, contract_type[i]);
						stmt.executeQuery();
								
						query="UPDATE FMS_TRADER_CONT_PRICE_DTL SET FLAG=?,APPROVE_BY=?,APPROVE_DT=SYSDATE,"
								+ "MODIFIY_BY=?,MODIFIY_DT=SYSDATE  "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND CONT_NO=? "
								+ "AND CONTRACT_TYPE=? AND SEQ_NO=? "
								+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND FLAG=?";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, "Y");
						stmt.setString(2, emp_cd);
						stmt.setString(3, emp_cd);
						stmt.setString(4, comp_cd);
						stmt.setString(5, counterparty_cd[i]);
						stmt.setString(6, agmt_no[i]);
						stmt.setString(7, cont_no[i]);
						stmt.setString(8, contract_type[i]);
						stmt.setString(9, change_seq_no[i]);
						stmt.setString(10, new_eff_dt[i]);
						stmt.setString(11, "R");
						stmt.executeQuery();
						
						msg="Approved Contract/Cargo Price for "+strMsg;
						msg_type="S";
					}
					else
					{
						query="UPDATE FMS_TRADER_CONT_PRICE_DTL SET FLAG=?,MODIFIY_BY=?,MODIFIY_DT=SYSDATE  "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND CONT_NO=? "
								+ "AND CONTRACT_TYPE=? AND SEQ_NO=? "
								+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND FLAG=?";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, "X");
						stmt.setString(2, emp_cd);
						stmt.setString(3, comp_cd);
						stmt.setString(4, counterparty_cd[i]);
						stmt.setString(5, agmt_no[i]);
						stmt.setString(6, cont_no[i]);
						stmt.setString(7, contract_type[i]);
						stmt.setString(8, change_seq_no[i]);
						stmt.setString(9, new_eff_dt[i]);
						stmt.setString(10, "R");
						stmt.executeQuery();
						
						msg="Rejected Contract/Cargo Price for "+strMsg;
						msg_type="S";
					}
					
					query="SELECT COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,ALLOC_QTY,SALE_PRICE,COST_PRICE,MARGIN,TOTAL_MARGIN,RATE_UNIT,"
							+ "CONTRACT_TYPE "
							+ "FROM FMS_SUPPLY_PURCHASE_MAP_DTL "
							+ "WHERE COMPANY_CD=? AND PUR_CONT_NO=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, cont_no[i]);
					rset=stmt.executeQuery();
					while(rset.next())
					{
						String countpty_cd=rset.getString(1)==null?"0":rset.getString(1);
						String agmtNo=rset.getString(2)==null?"0":rset.getString(2);
						String agmtRev=rset.getString(3)==null?"0":rset.getString(3);
						String contNo=rset.getString(4)==null?"0":rset.getString(4);
						String contRev=rset.getString(5)==null?"0":rset.getString(5);
						
						double alloc_qty=rset.getDouble(6);
						double sale_price=rset.getDouble(7);
						double cost_price=rset.getDouble(8);
						double margin=rset.getDouble(9);
						double total_margin=rset.getDouble(10);
						
						double new_cost_price = Double.parseDouble(""+new_rate[i]);
						double new_margin = 0; 
						
						String sales_rate_unit = rset.getString(11)==null?"2":rset.getString(11);
						String sales_cont_type = rset.getString(12)==null?"":rset.getString(12);
						
						if(sale_price > 0)
						{	
							double SalesPrice=0;
							double CostPrice=0;
							if(sales_rate_unit.trim().equals("1"))
							{
								if(Double.parseDouble(exchgRate) > 0)
								{
									SalesPrice = (sale_price/Double.parseDouble(exchgRate));
								}
								else
								{
									SalesPrice = 0;
								}
							}
							else
							{
								SalesPrice = sale_price;
							}
							
							if(rate_unit[i].trim().equals("1"))
							{
								if(Double.parseDouble(exchgRate) > 0)
								{
									CostPrice = (new_cost_price/Double.parseDouble(exchgRate));
								}
								else
								{
									CostPrice = 0;
								}
							}
							else
							{
								CostPrice = new_cost_price;
							}
							
							new_margin = SalesPrice - CostPrice;
						}
						double new_total_margin=alloc_qty * new_margin;
						
						new_margin = Double.parseDouble(nf2.format(new_margin));
						new_total_margin = Double.parseDouble(nf2.format(new_total_margin));
						
						query1="UPDATE FMS_SUPPLY_PURCHASE_MAP_DTL SET COST_PRICE=?,MARGIN=?,AVG_MARGIN=?,"
								+ "TOTAL_MARGIN=?,STATUS=?,APRV_BY=?,APRV_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "WHERE COMPANY_CD=? AND AGMT_NO=? "
								+ "AND CONT_NO=? AND COUNTERPARTY_CD=? "
								+ "AND CONTRACT_TYPE=? AND PUR_CONT_NO=? AND STATUS=?";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(1, ""+new_cost_price);
						stmt1.setString(2, ""+new_margin);
						stmt1.setString(3, ""+new_margin);
						stmt1.setString(4, ""+new_total_margin);
						stmt1.setString(5, "Y");
						stmt1.setString(6, emp_cd);
						stmt1.setString(7, new_eff_dt[i]);
						stmt1.setString(8, comp_cd);
						stmt1.setString(9, rset.getString(2));
						stmt1.setString(10, rset.getString(4));
						stmt1.setString(11, rset.getString(1));
						stmt1.setString(12, sales_cont_type);
						stmt1.setString(13, cont_no[i]);
						stmt1.setString(14, "N");
						stmt1.executeUpdate();
					}
					rset.close();
					stmt.close();
				}
			}
			else
			{
				msg = "Failed! - Contract/Cargo Price Approval Failed!";
				msg_type = "E";
			}
			
			url = "../inventory/frm_trade_cont_price_modification.jsp?msg="+msg+"&msg_type="+msg_type+"&clearance="+clearance+
					"&multiCountpty="+multiCountpty+"&multiAgmtNo="+multiAgmtNo+"&multiAgmtRev="+multiAgmtRev+
					"&multiContNo="+multiContNo+"&multiContRev="+multiContRev+"&multiContTyp="+multiContTyp+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Contract/Cargo Price Approval Failed!";
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
	
	private void InsertUpdateMmbtuTransferDetails(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateMmbtuTransferDetails()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String [] chk=request.getParameterValues("chk");
			//For old cargo information 
			String Countpty = request.getParameter("Countpty")==null?"":request.getParameter("Countpty");
			String AgmtNo = request.getParameter("AgmtNo")==null?"":request.getParameter("AgmtNo");
			String AgmtRev = request.getParameter("AgmtRev")==null?"":request.getParameter("AgmtRev");
			String ContNo = request.getParameter("ContNo")==null?"":request.getParameter("ContNo");
			String ContRev = request.getParameter("ContRev")==null?"":request.getParameter("ContRev");
			String ContTyp = request.getParameter("ContTyp")==null?"":request.getParameter("ContTyp");
			String CargoNo = request.getParameter("CargoNo")==null?"":request.getParameter("CargoNo");
			String pool_flag = request.getParameter("pool_flag")==null?"":request.getParameter("pool_flag");
			String transfer_type = request.getParameter("transfer_type")==null?"":request.getParameter("transfer_type");
			String tf="";
			
			if(transfer_type.equals("short_transfer"))
			{
				tf= "Short Transfer";
			}
			else if(transfer_type.equals("full_replacement"))
			{
				tf = "Full Replacement";
			}
			else if(transfer_type.equals("pseudo_replacement"))
			{
				tf = "Pseudo Replacement";
			}
			else
			{
				tf = "Volume Allocation Transfer";
			}
			
			String dealMap=utilBean.NewDealMappingId(comp_cd, Countpty, AgmtNo, AgmtRev, ContNo, ContRev, ContTyp, CargoNo);
			
			for(int i=0;i<chk.length;i++)
			{
				String qty_unit="1";
				String status="Y";
				
				//for buyer contract information 
				String buyer_cd=request.getParameter("buy_cd"+chk[i])==null?"":request.getParameter("buy_cd"+chk[i]);
				String buyer_agmt_no=request.getParameter("buy_agmt_no"+chk[i])==null?"":request.getParameter("buy_agmt_no"+chk[i]);
				String buyer_agmt_rev_no=request.getParameter("buy_agmt_rev_no"+chk[i])==null?"":request.getParameter("buy_agmt_rev_no"+chk[i]);
				String buyer_cont_no=request.getParameter("buy_cont_no"+chk[i])==null?"":request.getParameter("buy_cont_no"+chk[i]);
				String buyer_cont_rev_no=request.getParameter("buy_cont_rev_no"+chk[i])==null?"":request.getParameter("buy_cont_rev_no"+chk[i]);
				String buyer_cont_type=request.getParameter("buy_cont_type"+chk[i])==null?"":request.getParameter("buy_cont_type"+chk[i]);
				String sales_price=request.getParameter("sales_price"+chk[i])==null?"":request.getParameter("sales_price"+chk[i]);
				String sales_price_rate_unit=request.getParameter("sales_price_rate_unit"+chk[i])==null?"":request.getParameter("sales_price_rate_unit"+chk[i]);
				
				//for selected cargo information
				String [] transfer_alloc_qty = request.getParameterValues("transfer_alloc_qty"+chk[i]);
				String [] new_cargo_cont_no = request.getParameterValues("pur_cont_no"+chk[i]);
				String [] new_cargo_no = request.getParameterValues("pur_cargo_no"+chk[i]);
				String [] cost_price = request.getParameterValues("cost_price"+chk[i]);
				String [] cost_price_rate_unit = request.getParameterValues("cost_price_rate_unit"+chk[i]);
				
				if(new_cargo_cont_no.length>0)
				{
					for(int j=0;j<new_cargo_cont_no.length;j++)
					{
						if(!transfer_alloc_qty[j].equals(""))
						{
							//taking the MMBTU from selected cargoes 
							String contRevNo="0";
							query="SELECT COUNT(*) "
									+ "FROM FMS_SUPPLY_PURCHASE_MAP_DTL "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
									+ "AND CONTRACT_TYPE=? AND PUR_CONT_NO=? AND CARGO_NO=? ";
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, comp_cd);
							stmt.setString(2, buyer_cd);
							stmt.setString(3, buyer_agmt_no);
							stmt.setString(4, buyer_agmt_rev_no);
							stmt.setString(5, buyer_cont_no);
							stmt.setString(6, buyer_cont_type);
							stmt.setString(7, new_cargo_cont_no[j]);
							stmt.setString(8, new_cargo_no[j]);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								contRevNo=""+rset.getInt(1);
							}
							rset.close();
							stmt.close();
							
							query1="INSERT INTO FMS_SUPPLY_PURCHASE_MAP_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
									+ "CONTRACT_TYPE,PUR_CONT_NO,ALLOC_QTY,QTY_UNIT,SALE_PRICE,RATE_UNIT,"
									+ "COST_PRICE,CP_UNIT,MARGIN,TOTAL_MARGIN,STATUS,ENT_BY,ENT_DT,"
									+ "AVG_MARGIN,CARGO_NO) "
									+ "VALUES(?,?,?,?,?,?,"
									+ "?,?,?,?,?,?,"
									+ "?,?,?,?,?,?,SYSDATE,"
									+ "?,?)";
							stmt1=dbcon.prepareStatement(query1);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, buyer_cd);
							stmt1.setString(3, buyer_agmt_no);
							stmt1.setString(4, buyer_agmt_rev_no);
							stmt1.setString(5, buyer_cont_no);
							stmt1.setString(6, contRevNo);
							stmt1.setString(7, buyer_cont_type);
							stmt1.setString(8, new_cargo_cont_no[j]);
							stmt1.setString(9, transfer_alloc_qty[j]);
							stmt1.setString(10, qty_unit);
							stmt1.setString(11, sales_price);
							stmt1.setString(12, sales_price_rate_unit);
							stmt1.setString(13, cost_price[j]);
							stmt1.setString(14, cost_price_rate_unit[j]);
							stmt1.setString(15, "");
							stmt1.setString(16, "");
							stmt1.setString(17, status);
							stmt1.setString(18, emp_cd);
							stmt1.setString(19, "");
							stmt1.setString(20, new_cargo_no[j]);
							stmt1.executeUpdate();
							stmt1.close();
							//inserting transfer details for fetching to allocating 
							insertTransferDetail(buyer_cd, buyer_agmt_no, buyer_agmt_rev_no, buyer_cont_no, buyer_cont_rev_no, buyer_cont_type, new_cargo_cont_no[j], ContNo, transfer_alloc_qty[j], tf, new_cargo_no[j],CargoNo);
							//if pseudo cargo is used 
							if(new_cargo_cont_no[j].equals("0") && !new_cargo_no[j].equals("0"))
							{
								String tf1 = tf.equals("")?"Volume Allocation Transfer":tf;
								insertPseudoTransferDetails(buyer_cd, buyer_agmt_no, buyer_agmt_rev_no, buyer_cont_no, buyer_cont_rev_no, buyer_cont_type, new_cargo_cont_no[j], ContNo, transfer_alloc_qty[j], tf1, new_cargo_no[j],CargoNo);
							}
							
							//adding the MMBTU in parent Cargo
							String transfer_alloc = "-"+transfer_alloc_qty[j];
							String contRevNo1="0";
							query="SELECT COUNT(*) "
									+ "FROM FMS_SUPPLY_PURCHASE_MAP_DTL "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
									+ "AND CONTRACT_TYPE=? AND PUR_CONT_NO=? AND CARGO_NO=? ";
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, comp_cd);
							stmt.setString(2, buyer_cd);
							stmt.setString(3, buyer_agmt_no);
							stmt.setString(4, buyer_agmt_rev_no);
							stmt.setString(5, buyer_cont_no);
							stmt.setString(6, buyer_cont_type);
							stmt.setString(7, ContNo);
							stmt.setString(8, CargoNo);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								contRevNo1=""+rset.getInt(1);
							}
							rset.close();
							stmt.close();
							query="INSERT INTO FMS_SUPPLY_PURCHASE_MAP_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
									+ "CONTRACT_TYPE,PUR_CONT_NO,ALLOC_QTY,QTY_UNIT,SALE_PRICE,RATE_UNIT,"
									+ "COST_PRICE,CP_UNIT,MARGIN,TOTAL_MARGIN,STATUS,ENT_BY,ENT_DT,"
									+ "AVG_MARGIN,CARGO_NO) "
									+ "VALUES(?,?,?,?,?,?,"
									+ "?,?,?,?,?,?,"
									+ "?,?,?,?,?,?,SYSDATE,"
									+ "?,?)";
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, comp_cd);
							stmt.setString(2, buyer_cd);
							stmt.setString(3, buyer_agmt_no);
							stmt.setString(4, buyer_agmt_rev_no);
							stmt.setString(5, buyer_cont_no);
							stmt.setString(6, contRevNo1);
							stmt.setString(7, buyer_cont_type);
							stmt.setString(8, ContNo);
							stmt.setString(9, transfer_alloc);
							stmt.setString(10, qty_unit);
							stmt.setString(11, sales_price);
							stmt.setString(12, sales_price_rate_unit);
							stmt.setString(13, cost_price[j]);
							stmt.setString(14, cost_price_rate_unit[j]);
							stmt.setString(15, "");
							stmt.setString(16, "");
							stmt.setString(17, status);
							stmt.setString(18, emp_cd);
							stmt.setString(19, "");
							stmt.setString(20, CargoNo);
							stmt.executeUpdate();
							stmt.close();
							insertTransferDetail(buyer_cd, buyer_agmt_no, buyer_agmt_rev_no, buyer_cont_no, buyer_cont_rev_no, buyer_cont_type, ContNo, ContNo, transfer_alloc, tf, CargoNo,CargoNo);
							if(ContNo.equals("0") && !CargoNo.equals("0"))
							{
								String tf1 = tf.equals("")?"Volume Allocation Transfer":tf;
								insertPseudoTransferDetails(buyer_cd, buyer_agmt_no, buyer_agmt_rev_no, buyer_cont_no, buyer_cont_rev_no, buyer_cont_type, new_cargo_cont_no[j], ContNo, transfer_alloc, tf1, CargoNo,CargoNo);
							}
							
							msg = "Successful! - MMBTU Allocation Transfer for "+dealMap+" submitted Successfully!";
							msg_type = "S";
						}
					}
				}
				else
				{
					msg = "Failed! - MMBTU Allocation Transfer failed for " +dealMap+"!";
					msg_type = "E";
				}
			}
			url = "../inventory/frm_allocation_transfer.jsp?Countpty="+Countpty+"&AgmtNo="+AgmtNo+"&AgmtRev="+AgmtRev+
					"&ContNo="+ContNo+"&ContRev="+ContRev+"&ContTyp="+ContTyp+"&CargoNo="+CargoNo+
					"&u="+u+"&pool_flag="+pool_flag+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - MMBTU Allocation Transfer Failed!";
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
	
	private void InsertUpdatePseudoMmbtuModification(HttpServletRequest request) throws SQLException 
	{
		String function_nm = "InsertUpdatePseudoMmbtuModification()";
		msg="";
		msg_type="";
		url="";
		try
		{
			String pseudo_contract_type = request.getParameter("pseudo_contract_type")==null?"":request.getParameter("pseudo_contract_type");
			String pseudo_contract_type_nm=request.getParameter("pseudo_contract_type_name")==null?"":request.getParameter("pseudo_contract_type_name");
			String pseudo_cargo_no = request.getParameter("pseudo_cargo_no")==null?"":request.getParameter("pseudo_cargo_no");
			String pseudo_mmbtu = request.getParameter("pseudo_mmbtu")==null?"":request.getParameter("pseudo_mmbtu");
			String sysdate = dateUtil.getSysdate();	
			String transfer_type = "Pseudo Volume Modification";
			String pseudo_mmbtu_sign = request.getParameter("pseudo_mmbtu_sign")==null?"+":request.getParameter("pseudo_mmbtu_sign");
			String seq_no="0";
			
			if(pseudo_mmbtu_sign.equals("-"))
			{
				pseudo_mmbtu=pseudo_mmbtu_sign+pseudo_mmbtu;
			}
			
			String queryString ="SELECT COUNT(SEQ_NO) FROM FMS_PSEUDO_CARGO_DTL "
					+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND CARGO_NO=? ";
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1,comp_cd);
			stmt.setString(2, pseudo_contract_type);
			stmt.setString(3, pseudo_cargo_no);
			rset = stmt.executeQuery();
			if(rset.next())
			{
				seq_no = ""+rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			if(!pseudo_mmbtu.equals(""))
			{
				int ctn=0;
				String queryString1 ="INSERT INTO FMS_PSEUDO_CARGO_DTL(COMPANY_CD,CONTRACT_TYPE,CARGO_NO,SEQ_NO,QTY,ENT_DT,ENT_BY) "
						+ "VALUES(?,?,?,?,?,SYSDATE,?)";
				stmt=dbcon.prepareStatement(queryString1);
				stmt.setString(++ctn,comp_cd);
				stmt.setString(++ctn,pseudo_contract_type);
				stmt.setString(++ctn,pseudo_cargo_no);
				stmt.setString(++ctn,seq_no);
				stmt.setString(++ctn,pseudo_mmbtu);
				//stmt.setString(++ctn,sysdate);
				stmt.setString(++ctn,emp_cd);
				stmt.executeUpdate();
				stmt.close();
				
				//NOTE: For Pseudo MMBTU Modification the  counterparty_cd, agmt, agmt_rev, cont,
				//       cont_rev, cont_type, new_pur_cont_no, old_pur_cont_no,
				//		 transfer_type are set as '0' 
				// AND new_cargo_no == old_cargo_no
				
				insertPseudoTransferDetails("0","0","0","0","0","0","0","0",pseudo_mmbtu,transfer_type,pseudo_cargo_no,pseudo_cargo_no);

				
				msg = "Successful! - Pseudo MMBTU Modification for "+pseudo_contract_type_nm+"("+comp_cd+pseudo_contract_type+"-"+pseudo_cargo_no+") submitted Successfully!";
				msg_type = "S";
			}
			else
			{
				msg = "Failed! - Pseudo MMBTU Modification for "+pseudo_contract_type_nm+"("+comp_cd+pseudo_contract_type+"-"+pseudo_cargo_no+") Failed!";
				msg_type = "E";
			}
			
			url = "../inventory/frm_energy_bank.jsp?&u="+u+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Pseudo MMBTU Modification Failed!";
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
	
	public void insertPseudoTransferDetails(String counterparty_cd,String agmt,String agmt_rev,String cont,String cont_rev,String cont_type,String new_pur_cont_no,String old_pur_cont_no,String alloc_qty,String transfer_type,String new_cargo_no,String old_cargo_no) throws SQLException
	{
		String function_nm="insertPseudoTransferDetails()";
		try
		{
			String sysdate = dateUtil.getSysdate();
			int ctn=0;
			String seq_no = "0";
			String query="SELECT COUNT(SEQ_NO) FROM FMS_PSEUDO_SUPPLY_TRANSFER  "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=?  "
					+ "AND AGMT_REV=? AND CONT_NO=? AND CONT_REV=? AND CONTRACT_TYPE=?";
			stmt = dbcon.prepareStatement(query);
			stmt.setString(++ctn,comp_cd);
			stmt.setString(++ctn,counterparty_cd);
			stmt.setString(++ctn,agmt);
			stmt.setString(++ctn,agmt_rev);
			stmt.setString(++ctn,cont);
			stmt.setString(++ctn,cont_rev);
			stmt.setString(++ctn,cont_type);
			rset = stmt.executeQuery();
			if(rset.next())
			{
				seq_no = ""+rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			ctn=0;
			String queryString="INSERT INTO FMS_PSEUDO_SUPPLY_TRANSFER(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV, "
					+ "CONT_NO,CONT_REV,CONTRACT_TYPE,SEQ_NO,NEW_PUR_CONT_NO,OLD_PUR_CONT_NO,ALLOC_QTY, "
					+ "TRANSFER_TYPE,ENT_DT,ENT_BY,NEW_CARGO_NO,OLD_CARGO_NO) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?,?)";
			stmt = dbcon.prepareStatement(queryString);
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, counterparty_cd);
			stmt.setString(++ctn, agmt);
			stmt.setString(++ctn, agmt_rev);
			stmt.setString(++ctn, cont);
			stmt.setString(++ctn, cont_rev);
			stmt.setString(++ctn, cont_type);
			stmt.setString(++ctn, seq_no);
			stmt.setString(++ctn, new_pur_cont_no);
			stmt.setString(++ctn, old_pur_cont_no);
			stmt.setString(++ctn, alloc_qty);
			stmt.setString(++ctn, transfer_type);
			//stmt.setString(++ctn, sysdate);
			stmt.setString(++ctn, emp_cd);
			stmt.setString(++ctn, new_cargo_no);
			stmt.setString(++ctn, old_cargo_no);
			stmt.executeUpdate();
			stmt.close();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - MMBTU Allocation Transfer Failed!";
		}
	}
	
	private void ApproveClosureRequest(HttpServletRequest request) throws SQLException
	{
		String function_nm="ApproveClosureRequest()";
		msg="";
		msg_type="";
		url="";
		try
		{
			String customer_cd = request.getParameter("customer_cd")==null?"":request.getParameter("customer_cd");
			String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String cont_type = request.getParameter("cont_type")==null?"":request.getParameter("cont_type");
			String closure_eff_dt = request.getParameter("closure_eff_dt")==null?"":request.getParameter("closure_eff_dt");
			
			String cont_name_map = utilBean.NewDealMappingId(comp_cd, customer_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, cont_type, "");
			String counterparty_abbr = utilBean.getCounterpartyABBR(dbcon,customer_cd);
			
			String strMsg=counterparty_abbr+" "+cont_name_map;
			
			insertClosureDetail(customer_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, cont_type, closure_eff_dt, "0");
			
			msg = "Successful! - Contract Closure for "+strMsg+" submitted Successfully!";
			msg_type = "S";
			
			url = "../contract_master/frm_cont_closure_request.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Contract Closure Approval Failed";
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
}
