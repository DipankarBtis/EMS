package com.etrm.fms.credit_risk;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.etrm.fms.mail.MailDelivery;
import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.escapeSingleQuotes;

@WebServlet("/servlet/Frm_Advance")
public class Frm_Advance extends HttpServlet
{
	static String frm_src_file_name="Frm_Advance.java";
	public static Connection dbcon;
	
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
	
	public static escapeSingleQuotes escObj = new escapeSingleQuotes();
	
	static UtilBean utilBean = new UtilBean();
	static DataBean_Advance Db_cerdit = new DataBean_Advance();
	static MailDelivery mailDelv = new MailDelivery();
	
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
						
						if(option.equalsIgnoreCase("CASH_COLLATERAL_MST"))
						{
							InsertUpdateCashCollateral(request);
						}
						else if(option.equalsIgnoreCase("ADV_SAP_APPROVE"))//HP20230919
						{
							UpdateSapApprovalFlag(request);
						}
						
						dbcon.close();
						dbcon=null;
					}
				}
			}
			catch(Exception e)
			{
				new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
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
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
		}
	}
	
	private void InsertUpdateCashCollateral(HttpServletRequest request)throws SQLException 
	{
		String function_nm="InsertUpdateCashCollateral()";
		msg="";
		msg_type="";
		url="";
		try
		{
			String operation = request.getParameter("opration")==null?"":request.getParameter("opration");
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			
			String counterparty = request.getParameter("counterparty")==null?"":request.getParameter("counterparty");
			String sec_type = request.getParameter("sec_type")==null?"":request.getParameter("sec_type");
			String crdr = request.getParameter("crdr")==null?"":request.getParameter("crdr");
			String value = request.getParameter("value")==null?"":request.getParameter("value");
			String currency = request.getParameter("currency")==null?"":request.getParameter("currency");
			String tds_cd = request.getParameter("tds_cd")==null?"":request.getParameter("tds_cd");
			String tds_amt = request.getParameter("tds_amt")==null?"":request.getParameter("tds_amt");
			String received_dt = request.getParameter("received_dt")==null?"":request.getParameter("received_dt");
			String pg_ref = request.getParameter("pg_ref")==null?"":request.getParameter("pg_ref");
			String deal_type = request.getParameter("deal_type")==null?"":request.getParameter("deal_type");
			String status = request.getParameter("status")==null?"":request.getParameter("status");
			String remark = request.getParameter("remark")==null?"":request.getParameter("remark");
			remark=escObj.replaceSingleQuotes(remark);
			String plant_seq = request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
			String bu_unit = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
			
			String gross_amt = request.getParameter("gross_amt")==null?"":request.getParameter("gross_amt");
			String tax_amt = request.getParameter("tax_amt")==null?"":request.getParameter("tax_amt");
			String tax_struct_cd = request.getParameter("tax_struct_cd")==null?"":request.getParameter("tax_struct_cd");
			String receipt_voucher = request.getParameter("receipt_voucher")==null?"":request.getParameter("receipt_voucher");
			
			String[] sub_tax_amt = request.getParameterValues("sub_tax_amt");
			String[] sub_tax_struct_cd = request.getParameterValues("sub_tax_struct_cd");
			String[] sub_tax_struct_dtl = request.getParameterValues("sub_tax_struct_dtl");
			String[] sub_tax_code = request.getParameterValues("sub_tax_code");
			String[] sub_tax_base_amt = request.getParameterValues("sub_tax_base_amt");
			
			String[] deal_dtl = request.getParameterValues("deal_dtl");
			String[] split_value = request.getParameterValues("split_value");
			String temp_split_by = request.getParameter("temp_split_by")==null?"":request.getParameter("temp_split_by");
			
			String seq_no="1";
			String seq_rev_no="0";
			String ref_no="";
			String counterparty_abbr = "";
			String counterparty_nm = "";
			if(clearance.equals("I"))
			{
				counterparty_abbr = ""+utilBean.getGasExchangeAbbr(dbcon,counterparty);
				counterparty_nm = ""+utilBean.getGasExchangeName(dbcon,counterparty);
			}
			else
			{
				counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty);
				counterparty_nm = ""+utilBean.getCounterpartyName(dbcon,counterparty);
			}
			
			boolean isOperation=false;
			if(operation.equals("MODIFY"))
			{
				seq_no = request.getParameter("seq_no")==null?"":request.getParameter("seq_no");
				seq_rev_no = request.getParameter("seq_rev_no")==null?"":request.getParameter("seq_rev_no");
				ref_no = request.getParameter("ref_no")==null?"":request.getParameter("ref_no");
				
				int num=0;
				query="UPDATE FMS_SECURITY_MST SET CR_DR=?,VALUE=?,CURRENCY=?,SEC_TYPE=?,"
						+ "RECEIPT_DT=TO_DATE(?,'DD/MM/YYYY'),PG_REF=?, "
						+ "MODIFY_DT=SYSDATE , MODIFY_BY=? ,REMARKS=?,STATUS=?,"
						+ "TDS_AMT=?,TDS_STRUCT_CD=?,DEAL_TYPE=?,BU_UNIT=?,PLANT_SEQ=?,TAX_AMT=?,TAX_STRUCT_CD=?,GROSS_AMT=?,RECPT_SEC_REF=? ";
				if(status.equals("C"))
				{
					query+=",CANCEL_DT=SYSDATE ";
				}
				if(status.equals("O"))
				{
					query+=",INORDER_HIST=?,APRV_DT=SYSDATE,APRV_BY=? ";
				}
				query+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND GX=? "
						+ "AND SEQ_NO=? AND SEQ_REV_NO=? ";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(++num, crdr);
				stmt.setString(++num, value);
				stmt.setString(++num, currency);
				stmt.setString(++num, sec_type);
				stmt.setString(++num, received_dt);
				stmt.setString(++num, pg_ref);
				stmt.setString(++num, emp_cd);
				stmt.setString(++num, remark);
				stmt.setString(++num, status);
				stmt.setString(++num, tds_amt);
				stmt.setString(++num, tds_cd);
				stmt.setString(++num, deal_type);
				stmt.setString(++num, bu_unit);
				stmt.setString(++num, plant_seq);
				stmt.setString(++num, tax_amt);
				stmt.setString(++num, tax_struct_cd);
				stmt.setString(++num, gross_amt);
				stmt.setString(++num, receipt_voucher);
				if(status.equals("O"))
				{
					stmt.setString(++num, "Y");
					stmt.setString(++num, emp_cd);
				}
				stmt.setString(++num, comp_cd);
				stmt.setString(++num, counterparty);
				stmt.setString(++num, clearance);
				stmt.setString(++num, seq_no);
				stmt.setString(++num, seq_rev_no);
				stmt.executeUpdate();
				stmt.close();
				
				query = "DELETE FROM FMS_SECURITY_DEAL_MAP "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND GX=? "
					+ "AND SEQ_NO=? AND SEQ_REV_NO=? ";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty);
				stmt.setString(3, clearance);
				stmt.setString(4, seq_no);
				stmt.setString(5, seq_rev_no);
				stmt.executeUpdate();
				stmt.close();
				
				isOperation=true;
				
				msg = "Successful! - "+sec_type+" Cash Collateral "+comp_cd+"-"+ref_no+" Modified for "+counterparty_nm+"!";
				msg_type="S";
			}
			else if(operation.equals("INSERT"))
			{
				query="SELECT MAX(SEQ_NO)"
						+ "FROM FMS_SECURITY_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND GX=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty);
				stmt.setString(3, clearance);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					seq_no=""+(rset.getInt(1)+1);
				}
				rset.close();
				stmt.close();
				
				ref_no=counterparty_abbr+"-S-"+seq_no;
				query="INSERT INTO FMS_SECURITY_MST(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,CR_DR,SEC_TYPE,CURRENCY,VALUE,"
						+ "RECEIPT_DT,REMARKS,STATUS,ENT_BY,ENT_DT,SEQ_REV_NO,GX,TDS_AMT,TDS_STRUCT_CD,PG_REF,DEAL_TYPE,BU_UNIT,PLANT_SEQ,TAX_AMT,TAX_STRUCT_CD,GROSS_AMT,RECPT_SEC_REF) "
						+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,SYSDATE,?,?,?,?,?,?,?,?,?,?,?,?)";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty);
				stmt.setString(3, seq_no);
				stmt.setString(4, ref_no);
				stmt.setString(5, crdr);
				stmt.setString(6, sec_type);
				stmt.setString(7, currency);
				stmt.setString(8, value);
				stmt.setString(9, received_dt);
				stmt.setString(10, remark);
				stmt.setString(11, status);
				stmt.setString(12, emp_cd);
				stmt.setString(13, seq_rev_no);
				stmt.setString(14, clearance);
				stmt.setString(15, tds_amt);
				stmt.setString(16, tds_cd);
				stmt.setString(17, pg_ref);
				stmt.setString(18, deal_type);
				stmt.setString(19, bu_unit);
				stmt.setString(20, plant_seq);
				stmt.setString(21, tax_amt);
				stmt.setString(22, tax_struct_cd);
				stmt.setString(23, gross_amt);
				stmt.setString(24, receipt_voucher);
				stmt.executeUpdate();
				
				stmt.close();
				
				isOperation=true;
				
				msg = "Successful! - "+sec_type+" Cash Collateral "+comp_cd+"-"+ref_no+" Added for "+counterparty_nm+"!";
				msg_type="S";
			}
			
			if(isOperation)
			{
				int tax_count=0;
				query1="SELECT COUNT(*) "
						+ "FROM FMS_SECURITY_TAX_DTL "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty);
				stmt1.setString(3, seq_no);
				stmt1.setString(4, seq_rev_no);
				stmt1.setString(5, clearance);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					tax_count=rset1.getInt(1);
				}
				rset1.close();
				stmt1.close();
				
				if(tax_count>0)
				{
					query2="DELETE FROM FMS_SECURITY_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty);
					stmt2.setString(3, seq_no);
					stmt2.setString(4, seq_rev_no);
					stmt2.setString(5, clearance);
					stmt2.executeUpdate();
					
					stmt2.close();
				}
				
				if(sub_tax_code!=null)
				{
					for(int i=0; i<sub_tax_code.length;i++)
					{
						query2="INSERT INTO FMS_SECURITY_TAX_DTL(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEQ_REV_NO,GX,"
								+ "TAX_STRUCT_CD,TAX_CODE,TAX_DESCR,"
								+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT) "
								+ "VALUES(?,?,?,?,?,"
								+ "?,?,?,"
								+ "?,?,?,SYSDATE)";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty);
						stmt2.setString(3, seq_no);
						stmt2.setString(4, seq_rev_no);
						stmt2.setString(5, clearance);
						stmt2.setString(6, tax_struct_cd);
						stmt2.setString(7, sub_tax_code[i]);
						stmt2.setString(8, sub_tax_struct_dtl[i]);
						stmt2.setString(9, sub_tax_amt[i]);
						stmt2.setString(10, sub_tax_base_amt[i]);
						stmt2.setString(11, emp_cd);
						stmt2.executeUpdate();
						
						stmt2.close();
					}
				}
				
				if(deal_dtl != null)
				{
					for(int i=0;i<deal_dtl.length;i++)
					{
						String map_seq_no="1";
						query="SELECT MAX(MAP_SEQ_NO) "
								+ "FROM FMS_SECURITY_DEAL_MAP "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, counterparty);
						stmt.setString(3, seq_no);
						stmt.setString(4, seq_rev_no);
						stmt.setString(5, clearance);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							map_seq_no=""+(rset.getInt(1)+1);
						}
						rset.close();
						stmt.close();
						
						String[] new_deal_info = deal_dtl[i].split("-");
						String entity_cd="";
						if(clearance.equals("I"))
						{
							entity_cd=new_deal_info[0];
						}
						String cont_type =  new_deal_info[1];
						String agmt = new_deal_info[2];
						String agmt_rev = new_deal_info[3];
						String cont = new_deal_info[4];
						String cont_rev = new_deal_info[5];
						
						query="INSERT INTO FMS_SECURITY_DEAL_MAP(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,SEQ_NO,"
								+ "MAP_SEQ_NO,SEC_REF_NO,CONTRACT_TYPE,ENT_BY,ENT_DT,SEQ_REV_NO,GX,ENTITY_CD,SHARE_PERCENT,SPLIT_BY) "
								+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?,?,?,?)";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, counterparty);
						stmt.setString(3, agmt);
						stmt.setString(4, agmt_rev);
						stmt.setString(5, cont);
						stmt.setString(6, cont_rev);
						stmt.setString(7, seq_no);
						stmt.setString(8, map_seq_no);
						stmt.setString(9, ref_no);
						stmt.setString(10, cont_type);
						stmt.setString(11, emp_cd);
						stmt.setString(12, seq_rev_no);
						stmt.setString(13, clearance);
						stmt.setString(14, entity_cd);
						stmt.setString(15, split_value[i]);
						stmt.setString(16, temp_split_by);
						stmt.executeQuery();
						stmt.close();
					}
				}
				
				String dtl_seqNo = "";
				query1 = "SELECT MAX(NVL(LOG_SEQ_NO,1)+1) "
						+ "FROM LOG_FMS_SECURITY_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
						+ "AND SEQ_REV_NO=? AND GX=?";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty);
				stmt1.setString(3, seq_no);
				stmt1.setString(4, seq_rev_no);
				stmt1.setString(5, clearance);
				rset1 = stmt1.executeQuery();
				if(rset1.next()) 
				{
					dtl_seqNo = rset1.getString(1)==null?"1":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
				
				query2 = "INSERT INTO LOG_FMS_SECURITY_MST(COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, LOG_SEQ_NO, LOG_ENT_DT, SEC_REF_NO, SEC_CATEGORY, "
						+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
						+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
						+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
						+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT,PG_REF,CR_DR,TDS_AMT,TDS_STRUCT_CD,BU_UNIT,PLANT_SEQ,"
						+ "TAX_AMT,TAX_STRUCT_CD,GROSS_AMT,RECPT_SEC_REF) "
						+ "SELECT COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, ?, SYSDATE, SEC_REF_NO, SEC_CATEGORY, "
						+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
						+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
						+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
						+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT,PG_REF,CR_DR,TDS_AMT,TDS_STRUCT_CD,BU_UNIT,PLANT_SEQ,"
						+ "TAX_AMT,TAX_STRUCT_CD,GROSS_AMT,RECPT_SEC_REF "
						+ "FROM FMS_SECURITY_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";
				stmt2=dbcon.prepareStatement(query2);
				stmt2.setString(1, dtl_seqNo);
				stmt2.setString(2, comp_cd);
				stmt2.setString(3, counterparty);
				stmt2.setString(4, seq_no);
				stmt2.setString(5, seq_rev_no);
				stmt2.setString(6, clearance);
				stmt2.executeQuery();
				stmt2.close();
			}
			
			url = "../credit_risk/frm_cash_collateral.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty+"&clearance="+clearance+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url;
			msg = "Error in Exception! - Cash Collateral Insert/Update Failed!";
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
	
	private void UpdateSapApprovalFlag(HttpServletRequest request) throws SQLException
	{
		String function_nm="UpdateSapApprovalFlag()";
		msg="";
		msg_type="";
		url="";

		try
		{
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String seq_no=request.getParameter("seq_no")==null?"":request.getParameter("seq_no");
			String seq_rev_no=request.getParameter("seq_rev_no")==null?"":request.getParameter("seq_rev_no");
			String gx=request.getParameter("gx")==null?"":request.getParameter("gx");
			String sap_approval_flag = request.getParameter("sap_approval_flag")==null?"":request.getParameter("sap_approval_flag");
			String sec_ref_no = request.getParameter("sec_ref_no")==null?"":request.getParameter("sec_ref_no");
			String isReversal = request.getParameter("isReversal")==null?"":request.getParameter("isReversal");
			String sec_type = request.getParameter("sec_type")==null?"":request.getParameter("sec_type");
			String status = request.getParameter("status")==null?"":request.getParameter("status");
			String value = request.getParameter("value")==null?"":request.getParameter("value");
			String currency = request.getParameter("currency")==null?"":request.getParameter("currency");
			String deal_no = request.getParameter("deal_no")==null?"":request.getParameter("deal_no");
			String sec_category = request.getParameter("sec_category")==null?"":request.getParameter("sec_category");
			String xmlfile_nm ="";
			String counterparty_nm = "";
			String counterparty_abbr = "";
			if(gx.equals("K"))
			{
				counterparty_nm = ""+utilBean.getCounterpartyName(dbcon,counterparty_cd);
				counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			}
			else if(gx.equals("I"))
			{
				counterparty_nm = ""+utilBean.getGasExchangeName(dbcon,counterparty_cd);
				counterparty_abbr = ""+utilBean.getGasExchangeAbbr(dbcon,counterparty_cd);
			}		
			old_value=request.getParameter("old_value")==null?"":request.getParameter("old_value");;
			
			if(!counterparty_cd.equals("") && !seq_no.equals("") && !seq_rev_no.equals("") && !gx.equals(""))
			{
				if(isReversal.equals("Y"))
				{
					query="UPDATE FMS_SECURITY_MST SET SAP_REVERSAL=?,SAP_REVERSAL_BY=?,SAP_REVERSAL_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";	
					stmt=dbcon.prepareStatement(query); 
					stmt.setString(1, "Y");
					stmt.setString(2, emp_cd);
					stmt.setString(3, comp_cd);
					stmt.setString(4, counterparty_cd);
					stmt.setString(5, seq_no);
					stmt.setString(6, seq_rev_no);
					stmt.setString(7, gx);
					stmt.executeUpdate();
					stmt.close();
					
					msg = "Successful! - Advance "+sec_ref_no+" Reversal Approved for SAP Posting!";
					msg_type="S";
					
					sap_approval_flag="Y";
					
					new_value = "CP="+counterparty_cd+"#NAME="+counterparty_nm+"#ABBR="+counterparty_abbr+"#SEC_TYPE="+sec_type+"#SEC_CATEGORY="+sec_category+"#DEAL_TYPE="+""+"#DEAL_NO="+deal_no+"#VALUE="+value+"#CURRENCY="+currency+
							"#FLUCTUATION="+""+"#VARIATION="+""+"#ISS_BANK_CD="+""+"#ISS_BANK_NAME="+""+"#ISS_BANK_REF="+""+"#ADV_BANK_CD="+""+"#ADV_BANK_NAME="+""+"#ADV_BANK_REF="+""+"#CONF_BANK_CD="+""+"#CONF_BANK_NAME="+""+
						    "#CONF_BANK_REF="+""+"#RECIEVED_DT="+""+"#REVIEW_DT="+""+"#ISSUANCE_DT="+""+"#EXPIRY_DT="+""+"#STATUS="+status+"#TENOR="+""+"#REMARK="+""+"#GUARANTOR_CD="+""+"#GUARANTOR_NM="+""+"#SEC_REF_NO="+sec_ref_no+"#CANCEL_DT="+""+"#GX="+gx+"#REVERSAL="+"Y"+"#SAP_APPROVAL="+"";
					
				}
				else
				{
					query="UPDATE FMS_SECURITY_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";	
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, "Y");
					stmt.setString(2, emp_cd);
					stmt.setString(3, comp_cd);
					stmt.setString(4, counterparty_cd);
					stmt.setString(5, seq_no);
					stmt.setString(6, seq_rev_no);
					stmt.setString(7, gx);
					stmt.executeUpdate();
					stmt.close();
					
					msg = "Successful! - Advance "+sec_ref_no+" Approved for SAP Posting!";
					msg_type="S";
					
					sap_approval_flag="Y";
					
					new_value = "CP="+counterparty_cd+"#NAME="+counterparty_nm+"#ABBR="+counterparty_abbr+"#SEC_TYPE="+sec_type+"#SEC_CATEGORY="+sec_category+"#DEAL_TYPE="+""+"#DEAL_NO="+deal_no+"#VALUE="+value+"#CURRENCY="+currency+
							"#FLUCTUATION="+""+"#VARIATION="+""+"#ISS_BANK_CD="+""+"#ISS_BANK_NAME="+""+"#ISS_BANK_REF="+""+"#ADV_BANK_CD="+""+"#ADV_BANK_NAME="+""+"#ADV_BANK_REF="+""+"#CONF_BANK_CD="+""+"#CONF_BANK_NAME="+""+
						    "#CONF_BANK_REF="+""+"#RECIEVED_DT="+""+"#REVIEW_DT="+""+"#ISSUANCE_DT="+""+"#EXPIRY_DT="+""+"#STATUS="+status+"#TENOR="+""+"#REMARK="+""+"#GUARANTOR_CD="+""+"#GUARANTOR_NM="+""+"#SEC_REF_NO="+sec_ref_no+"#CANCEL_DT="+""+"#GX="+gx+"#REVERSAL="+""+"#SAP_APPROVAL="+"Y";
				}
				
				String workDir=CommonVariable.work_dir+comp_cd;
				String sapxml_dir="";
				sapxml_dir=CommonVariable.sap_xml;
				String appPath = request.getServletContext().getRealPath(workDir+"/"+sapxml_dir+"/");
				
				Db_cerdit.setCallFlag("GENERATE_ADV_SAP_XML");
				Db_cerdit.setRequest(request);
				Db_cerdit.setCounterparty_cd(counterparty_cd);
				Db_cerdit.setSeq_no(seq_no);
				Db_cerdit.setSeq_rev_no(seq_rev_no);
				Db_cerdit.setSec_ref_no(sec_ref_no);
				Db_cerdit.setComp_cd(comp_cd);
				Db_cerdit.setFile_path(appPath);
				Db_cerdit.setGx(gx);
				Db_cerdit.setSap_approval_flag(sap_approval_flag);
				Db_cerdit.setIsReversal(isReversal);
				Db_cerdit.setEmp_cd(emp_cd);
				Db_cerdit.init();
				
				xmlfile_nm = Db_cerdit.getXmlfile_name();
			}
			else
			{
				sap_approval_flag="N";
				if(isReversal.equals("Y"))
				{
					msg = "Failed! - Advance "+sec_ref_no+" Reversal SAP Posting Approval Failed!";
					msg_type="E";
				}
				else
				{
					msg = "Failed! - Advance "+sec_ref_no+" SAP Posting Approval Failed!";
					msg_type="E";
				}
			}
			
			url = "../credit_risk/rpt_view_advance_sap_approval.jsp?counterparty_cd="+counterparty_cd+
					"&seq_no="+seq_no+"&seq_rev_no="+seq_rev_no+"&clearance="+gx+"&sap_approval_flag="+sap_approval_flag+
					"&sec_ref_no="+sec_ref_no+"&msg="+msg+"&msg_type="+msg_type+"&isReversal="+isReversal+commonUrl_pra;
			
			SecurityMailBody(comp_cd,counterparty_nm, counterparty_abbr, "",msg,emp_cd,new_value,old_value);
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url;
			msg = "Error in Exception! SAP Posting submission Failed!";
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
	
	private void SecurityMailBody(String comp_cd,String cp_name,String cp_abbr,String operation,String msg,String emp_cd,String new_values, String old_values) throws Exception
	{
		String function_nm="SecurityMailBody()";
		String mailBody="";
		try
		{
			String security_details="";
			String sec_ref_no="" , old_sec_ref_no="";
			String sec_type="", old_sec_type="";
			String status="" , old_status="";
			String gx="", old_gx = "";
			String cp="",old_cp="";
			String sec_value="" , old_sec_value="";
			String reversal="", sap_approval="";
			String old_reversal="", old_sap_approval="";
			String currency="" , old_currency="";
			String deal_no="" , old_deal_no="";
			String sec_category="" , old_sec_category="";
			
			if(!new_values.equals(""))
			{
				
				String name="",old_name="";
				String abbr="",old_abbr="";
				String deal_type="" , old_deal_type="";
				String fluctuation="" , old_fluctuation="";
				String variation="" , old_variation="";
				String iss_bank_cd="" , old_iss_bank_cd="";
				String iss_bank_nm="" , old_iss_bank_nm="";
				String iss_bank_ref="" , old_iss_bank_ref="";
				String adv_bank_cd="" , old_adv_bank_cd="";
				String adv_bank_nm="" , old_adv_bank_nm="";
				String adv_bank_ref="" , old_adv_bank_ref="";
				String conf_bank_cd="" , old_conf_bank_cd="";
				String conf_bank_nm="" , old_conf_bank_nm="";
				String conf_bank_ref="" , old_conf_bank_ref="";
				String received_dt="" , old_received_dt="";
				String review_dt="" , old_review_dt="";
				String iss_date="" , old_iss_date="";
				String expire_dt="" , old_expire_dt="";
				String tenor="" , old_tenor="";
				String remark="" , old_remark="";
				String guarantor_cd="" , old_guarantor_cd="";
				String guarantor_nm="" , old_guarantor_nm="";
				String deal_dtl="";
				
				String split_New_Value[] = new_values.split("#");
				for(int i=0; i<split_New_Value.length; i++)
				{
					if(split_New_Value[i].startsWith("CP="))
					{
						String temp[] = split_New_Value[i].split("CP=");
						if(temp.length>0)
						{
							cp=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("NAME="))
					{
						String temp[] = split_New_Value[i].split("NAME=");
						if(temp.length>0)
						{
							name=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("ABBR="))
					{
						String temp[] = split_New_Value[i].split("ABBR=");
						if(temp.length>0)
						{
							abbr=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("SEC_TYPE="))
					{
						String temp[] = split_New_Value[i].split("SEC_TYPE=");
						if(temp.length>0)
						{
							sec_type=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("SEC_CATEGORY="))
					{
						String temp[] = split_New_Value[i].split("SEC_CATEGORY=");
						if(temp.length>0)
						{
							sec_category=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("DEAL_TYPE="))
					{
						String temp[] = split_New_Value[i].split("DEAL_TYPE=");
						if(temp.length>0)
						{
							deal_type=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("DEAL_NO="))
					{
						String temp[] = split_New_Value[i].split("DEAL_NO=");
						if(temp.length>0)
						{
							deal_no=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("VALUE="))
					{
						String temp[] = split_New_Value[i].split("VALUE=");
						if(temp.length>0)
						{
							sec_value=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("CURRENCY="))
					{
						String temp[] = split_New_Value[i].split("CURRENCY=");
						if(temp.length>0)
						{
							currency=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("FLUCTUATION="))
					{
						String temp[] = split_New_Value[i].split("FLUCTUATION=");
						if(temp.length>0)
						{
							fluctuation=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("VARIATION="))
					{
						String temp[] = split_New_Value[i].split("VARIATION=");
						if(temp.length>0)
						{
							variation=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("ISS_BANK_CD="))
					{
						String temp[] = split_New_Value[i].split("ISS_BANK_CD=");
						if(temp.length>0)
						{
							iss_bank_cd=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("ISS_BANK_NAME="))
					{
						String temp[] = split_New_Value[i].split("ISS_BANK_NAME=");
						if(temp.length>0)
						{
							iss_bank_nm=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("ISS_BANK_REF="))
					{
						String temp[] = split_New_Value[i].split("ISS_BANK_REF=");
						if(temp.length>0)
						{
							iss_bank_ref=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("ADV_BANK_CD="))
					{
						String temp[] = split_New_Value[i].split("ADV_BANK_CD=");
						if(temp.length>0)
						{
							adv_bank_cd=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("ADV_BANK_NAME"))
					{
						String temp[] = split_New_Value[i].split("ADV_BANK_NAME=");
						if(temp.length>0)
						{
							adv_bank_nm=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("ADV_BANK_REF="))
					{
						String temp[] = split_New_Value[i].split("ADV_BANK_REF=");
						if(temp.length>0)
						{
							adv_bank_ref=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("CONF_BANK_CD="))
					{
						String temp[] = split_New_Value[i].split("CONF_BANK_CD=");
						if(temp.length>0)
						{
							conf_bank_cd=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("CONF_BANK_NAME="))
					{
						String temp[] = split_New_Value[i].split("CONF_BANK_NAME=");
						if(temp.length>0)
						{
							conf_bank_nm=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("CONF_BANK_REF="))
					{
						String temp[] = split_New_Value[i].split("CONF_BANK_REF=");
						if(temp.length>0)
						{
							conf_bank_ref=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("RECIEVED_DT="))
					{
						String temp[] = split_New_Value[i].split("RECIEVED_DT=");
						if(temp.length>0)
						{
							received_dt=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("REVIEW_DT="))
					{
						String temp[] = split_New_Value[i].split("REVIEW_DT=");
						if(temp.length>0)
						{
							review_dt=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("ISSUANCE_DT="))
					{
						String temp[] = split_New_Value[i].split("ISSUANCE_DT=");
						if(temp.length>0)
						{
							iss_date=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("EXPIRY_DT="))
					{
						String temp[] = split_New_Value[i].split("EXPIRY_DT=");
						if(temp.length>0)
						{
							expire_dt=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("STATUS="))
					{
						String temp[] = split_New_Value[i].split("STATUS=");
						if(temp.length>0)
						{
							status=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("TENOR="))
					{
						String temp[] = split_New_Value[i].split("TENOR=");
						if(temp.length>0)
						{
							tenor=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("REMARK="))
					{
						String temp[] = split_New_Value[i].split("REMARK=");
						if(temp.length>0)
						{
							remark=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("GUARANTOR_CD="))
					{
						String temp[] = split_New_Value[i].split("GUARANTOR_CD=");
						if(temp.length>0)
						{
							guarantor_cd=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("GUARANTOR_NM="))
					{
						String temp[] = split_New_Value[i].split("GUARANTOR_NM=");
						if(temp.length>0)
						{
							guarantor_nm=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("SEC_REF_NO="))
					{
						String temp[] = split_New_Value[i].split("SEC_REF_NO=");
						if(temp.length>0)
						{
							sec_ref_no=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("DEAL_DTL="))
					{
						String temp[] = split_New_Value[i].split("DEAL_DTL=");
						if(temp.length>0)
						{
							deal_dtl=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("GX="))
					{
						String temp[] = split_New_Value[i].split("GX=");
						if(temp.length>0)
						{
							gx=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("REVERSAL="))
					{
						String temp[] = split_New_Value[i].split("REVERSAL=");
						if(temp.length>0)
						{
							reversal=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("SAP_APPROVAL="))
					{
						String temp[] = split_New_Value[i].split("SAP_APPROVAL=");
						if(temp.length>0)
						{
							sap_approval=temp[1];
						}
					}
				}
				if(!old_values.equals(""))
				{
					String split_Old_Value[] = old_values.split("#");
					for(int i=0; i<split_Old_Value.length; i++)
					{
						if(split_Old_Value[i].startsWith("CP="))
						{
							String temp[] = split_Old_Value[i].split("CP=");
							if(temp.length>0)
							{
								old_cp=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("NAME="))
						{
							String temp[] = split_Old_Value[i].split("NAME=");
							if(temp.length>0)
							{
								old_name=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("ABBR="))
						{
							String temp[] = split_Old_Value[i].split("ABBR=");
							if(temp.length>0)
							{
								old_abbr=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("SEC_TYPE="))
						{
							String temp[] = split_Old_Value[i].split("SEC_TYPE=");
							if(temp.length>0)
							{
								old_sec_type=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("SEC_CATEGORY="))
						{
							String temp[] = split_Old_Value[i].split("SEC_CATEGORY=");
							if(temp.length>0)
							{
								old_sec_category=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("DEAL_TYPE="))
						{
							String temp[] = split_Old_Value[i].split("DEAL_TYPE=");
							if(temp.length>0)
							{
								old_deal_type=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("DEAL_NO="))
						{
							String temp[] = split_Old_Value[i].split("DEAL_NO=");
							if(temp.length>0)
							{
								old_deal_no=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("VALUE="))
						{
							String temp[] = split_Old_Value[i].split("VALUE=");
							if(temp.length>0)
							{
								old_sec_value=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("CURRENCY="))
						{
							String temp[] = split_Old_Value[i].split("CURRENCY=");
							if(temp.length>0)
							{
								old_currency=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("FLUCTUATION="))
						{
							String temp[] = split_Old_Value[i].split("FLUCTUATION=");
							if(temp.length>0)
							{
								old_fluctuation=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("VARIATION="))
						{
							String temp[] = split_Old_Value[i].split("VARIATION=");
							if(temp.length>0)
							{
								old_variation=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("ISS_BANK_CD="))
						{
							String temp[] = split_Old_Value[i].split("ISS_BANK_CD=");
							if(temp.length>0)
							{
								old_iss_bank_cd=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("ISS_BANK_NAME="))
						{
							String temp[] = split_Old_Value[i].split("ISS_BANK_NAME=");
							if(temp.length>0)
							{
								old_iss_bank_nm=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("ISS_BANK_REF="))
						{
							String temp[] = split_Old_Value[i].split("ISS_BANK_REF=");
							if(temp.length>0)
							{
								old_iss_bank_ref=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("ADV_BANK_CD="))
						{
							String temp[] = split_Old_Value[i].split("ADV_BANK_CD=");
							if(temp.length>0)
							{
								old_adv_bank_cd=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("ADV_BANK_NAME"))
						{
							String temp[] = split_Old_Value[i].split("ADV_BANK_NAME=");
							if(temp.length>0)
							{
								old_adv_bank_nm=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("ADV_BANK_REF="))
						{
							String temp[] = split_Old_Value[i].split("ADV_BANK_REF=");
							if(temp.length>0)
							{
								old_adv_bank_ref=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("CONF_BANK_CD="))
						{
							String temp[] = split_Old_Value[i].split("CONF_BANK_CD=");
							if(temp.length>0)
							{
								old_conf_bank_cd=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("CONF_BANK_NAME="))
						{
							String temp[] = split_Old_Value[i].split("CONF_BANK_NAME=");
							if(temp.length>0)
							{
								old_conf_bank_nm=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("CONF_BANK_REF="))
						{
							String temp[] = split_Old_Value[i].split("CONF_BANK_REF=");
							if(temp.length>0)
							{
								old_conf_bank_ref=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("RECIEVED_DT="))
						{
							String temp[] = split_Old_Value[i].split("RECIEVED_DT=");
							if(temp.length>0)
							{
								old_received_dt=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("REVIEW_DT="))
						{
							String temp[] = split_Old_Value[i].split("REVIEW_DT=");
							if(temp.length>0)
							{
								old_review_dt=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("ISSUANCE_DT="))
						{
							String temp[] = split_Old_Value[i].split("ISSUANCE_DT=");
							if(temp.length>0)
							{
								old_iss_date=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("EXPIRY_DT="))
						{
							String temp[] = split_Old_Value[i].split("EXPIRY_DT=");
							if(temp.length>0)
							{
								old_expire_dt=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("STATUS="))
						{
							String temp[] = split_Old_Value[i].split("STATUS=");
							if(temp.length>0)
							{
								old_status=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("TENOR="))
						{
							String temp[] = split_Old_Value[i].split("TENOR=");
							if(temp.length>0)
							{
								old_tenor=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("REMARK="))
						{
							String temp[] = split_Old_Value[i].split("REMARK=");
							if(temp.length>0)
							{
								old_remark=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("GUARANTOR_CD="))
						{
							String temp[] = split_Old_Value[i].split("GUARANTOR_CD=");
							if(temp.length>0)
							{
								old_guarantor_cd=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("GUARANTOR_NM="))
						{
							String temp[] = split_Old_Value[i].split("GUARANTOR_NM=");
							if(temp.length>0)
							{
								old_guarantor_nm=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("SEC_REF_NO="))
						{
							String temp[] = split_Old_Value[i].split("SEC_REF_NO=");
							if(temp.length>0)
							{
								old_sec_ref_no=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("GX="))
						{
							String temp[] = split_Old_Value[i].split("GX=");
							if(temp.length>0)
							{
								old_gx=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("REVERSAL="))
						{
							String temp[] = split_Old_Value[i].split("REVERSAL=");
							if(temp.length>0)
							{
								old_reversal=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("SAP_APPROVAL="))
						{
							String temp[] = split_Old_Value[i].split("SAP_APPROVAL=");
							if(temp.length>0)
							{
								old_sap_approval=temp[1];
							}
						}
					}
				}
				
				
				if(status.equals("A"))
				{
					status="Pending for Amendment";
				}
				else if(status.equals("P"))
				{
					status="Pending";
				}
				else if(status.equals("O"))
				{
					status="In Order";
				}
				else if(status.equals("C"))
				{
					status="Cancelled";
				}
				else if(status.equals("R"))
				{
					status="Restated";
				}
				
				if(old_status.equals("A"))
				{
					old_status="Pending for Amendment";
				}
				else if(old_status.equals("P"))
				{
					old_status="Pending";
				}
				else if(old_status.equals("O"))
				{
					old_status="In Order";
				}
				else if(old_status.equals("C"))
				{
					old_status="Cancelled";
				}
				else if(old_status.equals("R"))
				{
					old_status="Restated";
				}
				
				if(sec_category.equals("R"))
				{
					sec_category="Incoming";
				}
				else if(sec_category.equals("I"))
				{
					sec_category="Outgoing";
				}
				
				if(old_sec_category.equals("R"))
				{
					old_sec_category="Incoming";
				}
				else if(old_sec_category.equals("I"))
				{
					old_sec_category="Outgoing";
				}
				
				if(currency.equals("1"))
				{
					currency="INR";
				}
				else if(currency.equals("2"))
				{
					currency="USD";
				}
				if(old_currency.equals("1"))
				{
					old_currency="INR";
				}
				else if(old_currency.equals("2"))
				{
					old_currency="USD";
				}
				
				if(!cp.equals(""))
				{
					
					if(old_values.equals("")) 
					{
						security_details="Counterparty Name : "+name+"<br>"
								+ "Abbreviation : "+abbr+"<br>"
								+ "Security Type : "+sec_type+"<br>"
								+ "Category : "+sec_category+"<br>"
								+ "Contract# : "+deal_no+"<br>"
								+ "Value : "+sec_value+"<br>"
								+ "Currency : "+currency+"<br>"
								+ "Recieved Date : "+received_dt+"<br>"
								+ "Status : "+status+"<br>"
								+ "Remark : "+remark;
					}
					else
					{
						if(!name.equals(old_name))
						{
							security_details+="Name : "+name+"<br>";
						}
						if(!abbr.equals(old_abbr))
						{
							security_details+="Abbreviation : "+abbr+"<br>";
						}
						if(!sec_type.equals(old_sec_type))
						{
							security_details+="Security Type : "+sec_type+"<br>";
						}
						if(!sec_category.equals(old_sec_category))
						{
							security_details+="Category : "+sec_category+"<br>";
						}
						if(!deal_type.equals(old_deal_type))
						{
							security_details+="Deal Type : "+deal_type+"<br>";
						}
						if(!deal_no.equals(old_deal_no))
						{
							security_details+="Contract# : "+deal_no+"<br>";
						}
						if(!sec_value.equals(old_sec_value))
						{
							security_details+="Value : "+sec_value+"<br>";
						}
						if(!currency.equals(old_currency))
						{
							security_details+="Currency : "+currency+"<br>";
						}
						if(!guarantor_nm.equals(old_guarantor_nm))
						{
							security_details+="Guarantor Name : "+guarantor_nm+"<br>";
						}
						if(!fluctuation.equals(old_fluctuation))
						{
							security_details+="Fluctuation : "+fluctuation+"<br>";
						}
						if(!variation.equals(old_variation))
						{
							security_details+="Variation : "+variation+"<br>";
						}
						if(!iss_bank_nm.equals(old_iss_bank_nm))
						{
							security_details+="Issuing Bank Name : "+iss_bank_nm+"<br>";
						}
						if(!iss_bank_ref.equals(old_iss_bank_ref))
						{
							security_details+="Issuing Bank Reference : "+iss_bank_ref+"<br>";
						}
						if(!adv_bank_nm.equals(old_adv_bank_nm))
						{
							security_details+="Advising Bank Name : "+adv_bank_nm+"<br>";
						}
						if(!adv_bank_ref.equals(old_adv_bank_ref))
						{
							security_details+="Advising Bank Reference  : "+adv_bank_ref+"<br>";
						}
						if(!conf_bank_nm.equals(old_conf_bank_nm))
						{
							security_details+="Confirming Bank Name : "+conf_bank_nm+"<br>";
						}
						if(!conf_bank_ref.equals(old_conf_bank_ref))
						{
							security_details+="Confirming Bank Reference : "+conf_bank_ref+"<br>";
						}
						if(!received_dt.equals(old_received_dt))
						{
							security_details+="Received Date : "+received_dt+"<br>";
						}
						if(!review_dt.equals(old_review_dt))
						{
							security_details+="Review Date : "+review_dt+"<br>";
						}
						if(!iss_date.equals(old_iss_date))
						{
							security_details+="Issuance Date : "+iss_date+"<br>";
						}
						if(!expire_dt.equals(old_expire_dt))
						{
							security_details+="Expire Date : "+expire_dt+"<br>";
						}
						if(!status.equals(old_status))
						{
							security_details+="Status : "+status+"<br>";
						}
						if(!tenor.equals(old_tenor))
						{
							security_details+="Tenor : "+tenor+"<br>";
						}
						if(!remark.equals(old_remark))
						{
							security_details+="Remark : "+remark+"<br>";
						}
					}
				}
			}
			
			if(sec_type.equals("ADV") && status.equals("In Order") && gx.equals("I"))
			{
				String subject="";
				if(sap_approval.equals("") && reversal.equals("Y"))
				{
					subject="IGX Advance "+sec_ref_no+" - Reversed by Fin. Ops.!";
				}
				else if(sap_approval.equals("Y") && reversal.equals(""))
				{
					subject="IGX Advance "+sec_ref_no+" - Approved by Fin. Ops.!";
				}
				else if(sap_approval.equals("") && reversal.equals(""))
				{
					subject="IGX Advance "+sec_ref_no+" - Approved";
				}
				
				mailBody="<html>"
						+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Dear Recipients, <br>";
				mailBody+= "</span><br>";
				
				
				if(sap_approval.equals("") && reversal.equals("Y"))
				{
					mailBody+="<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following IGX Advance Reversal is ";
					mailBody+= "<font style='background:#00cc00' color='white'>Approved</font> ";
					mailBody+="<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'> by Fin. Ops. for SAP P80 Posting! ";
				}
				else if(sap_approval.equals("Y") && reversal.equals(""))
				{
					mailBody+="<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following IGX Advance is ";
					mailBody+= "<font style='background:#00cc00' color='white'>Approved</font> ";
					mailBody+="<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'> by Fin. Ops. for SAP P80 Posting! ";
				}
				else if(sap_approval.equals("") && reversal.equals(""))
				{
					mailBody+="<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following IGX Advance is ";
					mailBody+= "<font style='background:#00cc00' color='white'>Approved</font> ";
					mailBody+="<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'> ! Please Process SAP Posting Approval!! ";
				}
				
				mailBody+="<br><br><br><table bordercolor='black' style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";border-collapse: collapse;' border='2'>"
						+ "<tr><td><b>Counterparty</b>&nbsp;</td><td>&nbsp;"+utilBean.getGasExchangeName(dbcon,cp)+"&nbsp;</td></tr>"
						+ "<tr><td><b>Advance#</b>&nbsp;</td><td>&nbsp;"+sec_ref_no+"&nbsp;</td></tr>"
						+ "<tr><td><b>Value</b>&nbsp;</td><td>&nbsp;"+sec_value+"&nbsp;"+currency+"&nbsp;</td></tr>"
						+ "<tr><td><b>Contract#</b>&nbsp;</td><td>&nbsp;"+deal_no+"&nbsp;</td></tr>"
						+ "<tr><td><b>Category</b>&nbsp;</td><td>&nbsp;"+sec_category+"&nbsp;</td></tr>"
						+ "</table>";
				
				mailBody+="<br><br><br><font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Please maintain confidentiality."
						+ "<br><b>NOTE:</b><i> System Generated Notification - Please do not Reply.</i>"
						+ "</html>";
				
				String to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Advance Approval Notification", "Risk Mgmt", "NA", "On-Event");
				String cc_mail_list=utilBean.getCcMailReceipentList(dbcon,comp_cd,"Advance Approval Notification", "Risk Mgmt", "NA", "On-Event");
				
				if(!to_mail_list.equals("") && !mailBody.equals(""))
				{
					mailDelv.sendMail(comp_cd,to_mail_list, subject, mailBody, "", cc_mail_list, "");
				}
			}
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url;
			msg = "Error in Exception! Mail Delivery Failed!";

		}
	}
}
