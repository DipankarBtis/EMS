package com.etrm.fms.contract_mgmt;

import java.io.File;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.etrm.fms.mail.MailDelivery;
import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.XmlUtilBean;
import com.etrm.fms.util.escapeSingleQuotes;

//Coded By          : Harsh Patel
//Code Reviewed by	:  
//CR Date			: 04/01/2023 
//Status	  		: Developing

@WebServlet("/servlet/Frm_ContractMgmt")
public class Frm_ContractMgmt extends HttpServlet
{
	static String db_src_file_name="Frm_ContractMgmt.java";
	public static Connection dbcon;
	
	public static String servletName = "Frm_ContractMgmt";
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
	static MailDelivery mailDelv = new MailDelivery();
	static XmlUtilBean xmlUtil = new XmlUtilBean();
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
					
					if(option.equalsIgnoreCase("BUYER_NOM"))
					{
						InsertUpdateBuyerNominationDetail(request);
					}
					else if(option.equalsIgnoreCase("PERIODIC_BUYER_NOM"))
					{
						InsertUpdatePeriodicBuyerNominationDetail(request);
					}
					else if(option.equalsIgnoreCase("SELLER_NOM"))
					{
						InsertUpdateSellerNominationDetail(request);
					}
					else if(option.equalsIgnoreCase("PERIODIC_SELLER_NOM"))
					{
						InsertUpdatePeriodicSellerNominationDetail(request);
					}
					else if(option.equalsIgnoreCase("METER_TICKET_READING"))
					{
						InsertUpdateMeterTicketReadingDtl(request);
					}
					else if(option.equalsIgnoreCase("DAILY_ALLOCATION"))
					{
						InsertUpdateDailyAllocationDtl(request);
					}
					else if(option.equalsIgnoreCase("TRANSPORTER_NOM"))
					{
						InsertUpdateTransporterNominationDetail(request);
					}
					else if(option.equalsIgnoreCase("SEND_MAIL"))
					{
						SendMail(request);
					}
					else if(option.equalsIgnoreCase("SELLER_NOM_TO_CUST_REMARK"))
					{
						InsertUpdateSellerNomToCustomerRemark(request);
					}
					else if(option.equalsIgnoreCase("DAILY_SELLER_CONTROL_ROOM"))
					{
						InsertUpdateSellerNomToControlRoom(request);
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
				if(rset != null){try {rset.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset1 != null){try {rset1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset2 != null){try {rset2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset3 != null){try {rset3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset4 != null){try {rset4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt != null){try {stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt0 != null){try {stmt0.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt1 != null){try {stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt2 != null){try {stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt3 != null){try {stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt4 != null){try {stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(dbcon != null){try {dbcon.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			}
		}
		
		try {
		response.sendRedirect(url);
		} catch(IOException e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	private void InsertUpdateSellerNomToControlRoom(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateSellerNomToControlRoom()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String gas_dt = request.getParameter("gas_dt")==null?"":request.getParameter("gas_dt");
			String gen_dt = request.getParameter("gen_dt")==null?"":request.getParameter("gen_dt");
						
			String [] exp_mmscm = request.getParameterValues("exp_mmscm");
			String [] counterparty_cd = request.getParameterValues("counterparty_cd");
			String [] transporter_cd = request.getParameterValues("transporter_cd");
			String [] trans_seq = request.getParameterValues("trans_seq");
			String [] plant_seq_no = request.getParameterValues("plant_seq_no");
			String [] category = request.getParameterValues("cont_type");
			
			query = "SELECT COUNT(*) "
					+ "FROM FMS_DAILY_SELLER_CNTRL_ROOM "
					+ "WHERE COMPANY_CD=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') ";
			stmt = dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, gas_dt);
			rset=stmt.executeQuery();	
			if(rset.next())
			{
				query1 = "DELETE FROM FMS_DAILY_SELLER_CNTRL_ROOM "
						+ "WHERE COMPANY_CD=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY')  ";
				stmt1 = dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, gas_dt);
				stmt1.executeUpdate();
				stmt1.close();
			}
			rset.close();
			stmt.close();
			
			for(int i=0;i<counterparty_cd.length;i++)
			{
											
				query2 = "INSERT INTO FMS_DAILY_SELLER_CNTRL_ROOM(COMPANY_CD,COUNTERPARTY_CD,TRANSPORTER_CD,PLANT_SEQ,"
						+ "CATEGORY,GAS_DT,EXP_MMSCM,ENT_BY,ENT_DT,TRANS_SEQ) "
					    + "VALUES(?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,SYSDATE,?)";					
				stmt2 = dbcon.prepareStatement(query2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, counterparty_cd[i]);
				stmt2.setString(3, transporter_cd[i]);
				stmt2.setString(4, plant_seq_no[i]);
				stmt2.setString(5, category[i]);
				stmt2.setString(6, gas_dt);
				stmt2.setString(7, exp_mmscm[i]);
				stmt2.setString(8, emp_cd);
				stmt2.setString(9, trans_seq[i]);
				stmt2.executeQuery();
				stmt2.close();
			}						
			msg = "Successful! - Seller Nomination to Control Room Submitted for "+gas_dt+" Successfully!";
			msg_type="S";
			
			url = "../contract_mgmt/rpt_nom_to_control_room.jsp?msg="+msg+"&msg_type="+msg_type+"&gas_dt="+gas_dt+commonUrl_pra;
			
			dbcon.commit();
			
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			msg="Error In Exception!";
			url=CommonVariable.errorpage_url+"?e="+e;
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
	
	private void InsertUpdateBuyerNominationDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateBuyerNominationDetail()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String gas_dt = request.getParameter("gas_dt")==null?"":request.getParameter("gas_dt");
			String gen_dt = request.getParameter("gen_dt")==null?"":request.getParameter("gen_dt");
			
			String[] counterparty_cd=request.getParameterValues("counterparty_cd");
			String[] agmt_no=request.getParameterValues("agmt_no");
			String[] agmt_rev_no=request.getParameterValues("agmt_rev_no");
			String[] cont_no=request.getParameterValues("cont_no");
			String[] cont_rev_no=request.getParameterValues("cont_rev_no");
			String[] contract_type=request.getParameterValues("contract_type");
			String[] cargo_no=request.getParameterValues("cargo_no");
			String[] trans_cd=request.getParameterValues("trans_cd");
			String[] trans_plantseq=request.getParameterValues("trans_plant_seq");
			String[] plant_seq=request.getParameterValues("plant_seq");
			String[] bu_plant_seq=request.getParameterValues("bu_plant_seq");
			
			String[] gen_time = request.getParameterValues("gen_time");
			String[] base = request.getParameterValues("base");
			String[] gcv = request.getParameterValues("gcv");
			String[] ncv = request.getParameterValues("ncv");
			String[] qty_mmbtu = request.getParameterValues("qty_mmbtu");
			String[] qty_scm = request.getParameterValues("qty_scm");
			String[] sf_id = request.getParameterValues("sf_id");
			
			String[] index = request.getParameterValues("index"); //INDEX of l
			String[] index1 = request.getParameterValues("index1"); //TOTAL SIZE OF CT ENTRY
			
			
			if(counterparty_cd != null)
			{
				for(int i=0; i<counterparty_cd.length; i++)
				{
					String countpty_cd=counterparty_cd[i];
					String agmt=agmt_no[i];
					String agmt_rev=agmt_rev_no[i];
					String cont=cont_no[i];
					String cont_rev=cont_rev_no[i];
					String transCd=trans_cd[i];
					String transPlantSeq=trans_plantseq[i];
					String plantSeq=plant_seq[i];
					String cont_type=contract_type[i];
					String cargoNo=cargo_no[i];
					String buPlantSeq=bu_plant_seq[i];
					String genTime=gen_time[i];
					String Base=base[i];
					String GCV=gcv[i];
					String NCV=ncv[i];
					String qtyMMBTU=qty_mmbtu[i];
					String qtySCM=qty_scm[i];
					String l=index[i];
					String sub_size=index1[i];
					String SfId = sf_id[i];
					
					BuyerNomination(request,gas_dt,gen_dt,countpty_cd,agmt,agmt_rev,cont,cont_rev,transCd,
							transPlantSeq,plantSeq,cont_type,cargoNo,buPlantSeq,genTime,Base,GCV,NCV,qtyMMBTU,qtySCM,l,sub_size,SfId);
				}
			}
			
			msg = "Successful! - Buyer Nomination Submitted for "+gas_dt+" Successfully!";
			msg_type="S";
			
			url = "../contract_mgmt/frm_daily_buyer_nom.jsp?msg="+msg+"&msg_type="+msg_type+"&gas_dt="+gas_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			msg="Error In Exception!";
			url=CommonVariable.errorpage_url+"?e="+e;
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
	
	private void BuyerNomination(HttpServletRequest request,String gas_dt,String gen_dt,String countpty_cd,String agmt,String agmt_rev,String cont,String cont_rev,String transCd,
			String transPlantSeq,String plantSeq,String cont_type,String cargoNo,String buPlantSeq,String genTime,String Base,
			String GCV,String NCV,String qtyMMBTU,String qtySCM,String l,String sub_size,String sf_id)throws Exception
	{
		try
		{
			int rev_no=-1;
			query = "SELECT NVL(MAX(NOM_REV_NO),'-1') "
					+ "FROM FMS_DAILY_BUYER_NOM "
					+ "WHERE CONT_NO=? AND AGMT_NO=? "
					+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
					+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
					+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? AND CARGO_NO=? ";
			stmt = dbcon.prepareStatement(query);
			stmt.setString(1, cont);
			stmt.setString(2, agmt);
			stmt.setString(3, comp_cd);
			stmt.setString(4, countpty_cd);
			stmt.setString(5, transCd);
			stmt.setString(6, transPlantSeq);
			stmt.setString(7, plantSeq);
			stmt.setString(8, cont_type);
			stmt.setString(9, gas_dt);
			stmt.setString(10, buPlantSeq);
			stmt.setString(11, cargoNo);
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
			
			query="INSERT INTO FMS_DAILY_BUYER_NOM(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
					+ "TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
					+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,BU_SEQ,CARGO_NO) "
					+ "VALUES(?,?,?,?,?,?,?,?,"
					+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
					+ "?,?,?,?,?,SYSDATE,?,?)";
			stmt0 = dbcon.prepareStatement(query);
			stmt0.setString(1, comp_cd);
			stmt0.setString(2, countpty_cd);
			stmt0.setString(3, agmt);
			stmt0.setString(4, agmt_rev);
			stmt0.setString(5, cont);
			stmt0.setString(6, cont_rev);
			stmt0.setInt(7, rev_no);
			stmt0.setString(8, plantSeq);
			stmt0.setString(9, transCd);
			stmt0.setString(10, transPlantSeq);
			stmt0.setString(11, gas_dt);
			stmt0.setString(12, cont_type);
			stmt0.setString(13, gen_dt);
			stmt0.setString(14, genTime);
			stmt0.setString(15, Base);
			stmt0.setString(16, GCV);
			stmt0.setString(17, NCV);
			stmt0.setString(18, qtyMMBTU);
			stmt0.setString(19, qtySCM);
			stmt0.setString(20, emp_cd);
			stmt0.setString(21, buPlantSeq);
			stmt0.setString(22, cargoNo);
			stmt0.executeQuery();
			
			stmt0.close();
			
			String[] sub_seq_no = request.getParameterValues("sub_seq_no"+l);
			String[] sub_ct_ref = request.getParameterValues("sub_ct_ref"+l);
			String[] temp_sub_ct_ref = request.getParameterValues("temp_sub_ct_ref"+l);
			String[] sub_utr_ref = request.getParameterValues("sub_utr_ref"+l);
			String[] sub_qty_mmbtu = request.getParameterValues("sub_qty_mmbtu"+l);
			String[] sub_qty_scm = request.getParameterValues("sub_qty_scm"+l);
			String[] sub_sf_id = request.getParameterValues("sub_sf_id"+l);
			
			if(sub_ct_ref!=null)
			{
				for(int j=0;j<sub_ct_ref.length;j++)
				{
					String seqNo="";
					if(sub_seq_no[j].equals(""))
					{
						query = "SELECT NVL(MAX(SEQ_NO),?) "
								+ "FROM FMS_DAILY_BUYER_NOM_DTL "
								+ "WHERE CONT_NO=? AND AGMT_NO=? "
								+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
								+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? AND CARGO_NO=? ";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, "0");
						stmt1.setString(2, cont);
						stmt1.setString(3, agmt);
						stmt1.setString(4, comp_cd);
						stmt1.setString(5, countpty_cd);
						stmt1.setString(6, transCd);
						stmt1.setString(7, transPlantSeq);
						stmt1.setString(8, plantSeq);
						stmt1.setString(9, cont_type);
						stmt1.setString(10, gas_dt);
						stmt1.setString(11, buPlantSeq);
						stmt1.setString(12, cargoNo);
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
					else
					{
						seqNo=sub_seq_no[j];
					}
					
					int sub_rev_no=-1;
					query = "SELECT NVL(MAX(NOM_REV_NO),?) "
							+ "FROM FMS_DAILY_BUYER_NOM_DTL "
							+ "WHERE CONT_NO=? AND AGMT_NO=? "
							+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
							+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
							+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? "
							+ "AND SEQ_NO=? AND CARGO_NO=? ";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(1, "-1");
					stmt1.setString(2, cont);
					stmt1.setString(3, agmt);
					stmt1.setString(4, comp_cd);
					stmt1.setString(5, countpty_cd);
					stmt1.setString(6, transCd);
					stmt1.setString(7, transPlantSeq);
					stmt1.setString(8, plantSeq);
					stmt1.setString(9, cont_type);
					stmt1.setString(10, gas_dt);
					stmt1.setString(11, buPlantSeq);
					stmt1.setString(12, seqNo);
					stmt1.setString(13, cargoNo);
					rset1 = stmt1.executeQuery();
					if(rset1.next())
					{
						sub_rev_no = rset1.getInt(1)+1;
					}
					else
					{
						sub_rev_no = rev_no + 1;
					}
					rset1.close();
					stmt1.close();
					
					query="INSERT INTO FMS_DAILY_BUYER_NOM_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
							+ "TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
							+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,BU_SEQ,SEQ_NO,CT_REF,UTR_NO,CARGO_NO,SF_ID) "
							+ "VALUES(?,?,?,?,?,?,?,?,"
							+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
							+ "?,?,?,?,?,SYSDATE,?,?,?,?,?,?)";
					stmt2 = dbcon.prepareStatement(query);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, countpty_cd);
					stmt2.setString(3, agmt);
					stmt2.setString(4, agmt_rev);
					stmt2.setString(5, cont);
					stmt2.setString(6, cont_rev);
					stmt2.setInt(7, sub_rev_no);
					stmt2.setString(8, plantSeq);
					stmt2.setString(9, transCd);
					stmt2.setString(10, transPlantSeq);
					stmt2.setString(11, gas_dt);
					stmt2.setString(12, cont_type);
					stmt2.setString(13, gen_dt);
					stmt2.setString(14, genTime);
					stmt2.setString(15, Base);
					stmt2.setString(16, GCV);
					stmt2.setString(17, NCV);
					stmt2.setString(18, sub_qty_mmbtu[j]);
					stmt2.setString(19, sub_qty_scm[j]);
					stmt2.setString(20, emp_cd);
					stmt2.setString(21, buPlantSeq);
					stmt2.setString(22, seqNo);
					stmt2.setString(23, sub_ct_ref[j]);
					stmt2.setString(24, sub_utr_ref[j]);
					stmt2.setString(25, cargoNo);
					stmt2.setString(26, sub_sf_id[j]);
					stmt2.executeQuery();
					
					stmt2.close();
					
					if(!temp_sub_ct_ref[j].equals(sub_ct_ref[j]))
					{
						String upSeqNo="";
						String upNomRevNo="";
						queryString="SELECT SEQ_NO,NOM_REV_NO "
				  				+ "FROM FMS_DAILY_SELLER_NOM_DTL A "
				  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
								+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
								+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? ";
						if(temp_sub_ct_ref[j].equals(""))
						{
							queryString+= "AND CT_REF IS NULL ";
						}
						else
						{
							queryString+= "AND CT_REF=? ";
						}
						queryString+= "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_SELLER_NOM_DTL B "
								+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
								+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
								+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND A.BU_SEQ=B.BU_SEQ "
								+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
								+ "AND B.GAS_DT=A.GAS_DT AND B.SEQ_NO=A.SEQ_NO AND B.CARGO_NO=A.CARGO_NO) ";
						stmt = dbcon.prepareStatement(queryString);
						stmt.setString(1, cont);
						stmt.setString(2, agmt);
						stmt.setString(3, comp_cd);
						stmt.setString(4, countpty_cd);
						stmt.setString(5, transCd);
						stmt.setString(6, transPlantSeq);
						stmt.setString(7, plantSeq);
						stmt.setString(8, cont_type);
						stmt.setString(9, buPlantSeq);
						stmt.setString(10, gas_dt);
						stmt.setString(11, cargoNo);
						if(!temp_sub_ct_ref[j].equals(""))
						{
							stmt.setString(12, temp_sub_ct_ref[j]);
						}
						rset = stmt.executeQuery();
						if(rset.next())
						{
							upSeqNo = rset.getString(1)==null?"":rset.getString(1);
							upNomRevNo = rset.getString(2)==null?"":rset.getString(2);
						}
						rset.close();
						stmt.close();
						
						if(!upSeqNo.equals("") && !upNomRevNo.equals(""))
						{
							String tempNomRevNo=""+(Integer.parseInt(upNomRevNo)+1);
							query="INSERT INTO FMS_DAILY_SELLER_NOM_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
									+ "TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
									+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,BU_SEQ,SEQ_NO,CT_REF,UTR_NO,CARGO_NO,SF_ID) "
									+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,?,PLANT_SEQ,"
									+ "TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
									+ "GCV,NCV,QTY_MMBTU,QTY_SCM,?,SYSDATE,BU_SEQ,SEQ_NO,?,UTR_NO,CARGO_NO,SF_ID "
									+ "FROM FMS_DAILY_SELLER_NOM_DTL "
									+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND NOM_REV_NO=? AND SEQ_NO=? AND CARGO_NO=? ";
							if(temp_sub_ct_ref[j].equals(""))
							{
								query+= "AND CT_REF IS NULL ";
							}
							else
							{
								query+= "AND CT_REF=? ";
							}
							stmt = dbcon.prepareStatement(query);
							stmt.setString(1, tempNomRevNo);
							stmt.setString(2, emp_cd);
							stmt.setString(3, sub_ct_ref[j]);
							stmt.setString(4, cont);
							stmt.setString(5, agmt);
							stmt.setString(6, comp_cd);
							stmt.setString(7, countpty_cd);
							stmt.setString(8, transCd);
							stmt.setString(9, transPlantSeq);
							stmt.setString(10, plantSeq);
							stmt.setString(11, cont_type);
							stmt.setString(12, buPlantSeq);
							stmt.setString(13, gas_dt);
							stmt.setString(14, upNomRevNo);
							stmt.setString(15, upSeqNo);
							stmt.setString(16, cargoNo);
							if(!temp_sub_ct_ref[j].equals(""))
							{
								stmt.setString(17, temp_sub_ct_ref[j]);
							}
							stmt.executeQuery();
							
							stmt.close();	
						}
						
						///FOR ALLOCATION
						upSeqNo="";
						upNomRevNo="";
						queryString="SELECT SEQ_NO,NOM_REV_NO "
				  				+ "FROM FMS_DAILY_ALLOCATION_DTL_CT A "
				  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
								+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
								+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? AND DTL_CATEGORY='CT' ";
						if(temp_sub_ct_ref[j].equals(""))
						{
							queryString+= "AND CT_REF IS NULL ";
						}
						else
						{
							queryString+= "AND CT_REF=? ";
						}
						queryString+= "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL_CT B "
								+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
								+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
								+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND A.BU_SEQ=B.BU_SEQ "
								+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
								+ "AND B.GAS_DT=A.GAS_DT AND B.SEQ_NO=A.SEQ_NO AND B.CARGO_NO=A.CARGO_NO AND B.DTL_CATEGORY=A.DTL_CATEGORY) ";
						stmt = dbcon.prepareStatement(queryString);
						stmt.setString(1, cont);
						stmt.setString(2, agmt);
						stmt.setString(3, comp_cd);
						stmt.setString(4, countpty_cd);
						stmt.setString(5, transCd);
						stmt.setString(6, transPlantSeq);
						stmt.setString(7, plantSeq);
						stmt.setString(8, cont_type);
						stmt.setString(9, buPlantSeq);
						stmt.setString(10, gas_dt);
						stmt.setString(11, cargoNo);
						if(!temp_sub_ct_ref[j].equals(""))
						{
							stmt.setString(12, temp_sub_ct_ref[j]);
						}
						rset = stmt.executeQuery();
						if(rset.next())
						{
							upSeqNo = rset.getString(1)==null?"":rset.getString(1);
							upNomRevNo = rset.getString(2)==null?"":rset.getString(2);
						}
						rset.close();
						stmt.close();
						
						if(!upSeqNo.equals("") && !upNomRevNo.equals(""))
						{
							String tempNomRevNo=""+(Integer.parseInt(upNomRevNo)+1);
							query="INSERT INTO FMS_DAILY_ALLOCATION_DTL_CT(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
									+ "TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
									+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,BU_SEQ,SEQ_NO,CT_REF,UTR_NO,CARGO_NO,DTL_CATEGORY,MOLECULE_MAP) "
									+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,?,PLANT_SEQ,"
									+ "TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
									+ "GCV,NCV,QTY_MMBTU,QTY_SCM,?,SYSDATE,BU_SEQ,SEQ_NO,?,UTR_NO,CARGO_NO,DTL_CATEGORY,MOLECULE_MAP "
									+ "FROM FMS_DAILY_ALLOCATION_DTL_CT "
									+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND NOM_REV_NO=? AND SEQ_NO=? AND CARGO_NO=? AND DTL_CATEGORY='CT' ";
							if(temp_sub_ct_ref[j].equals(""))
							{
								query+= "AND CT_REF IS NULL ";
							}
							else
							{
								query+= "AND CT_REF=? ";
							}
							stmt = dbcon.prepareStatement(query);
							stmt.setString(1, tempNomRevNo);
							stmt.setString(2, emp_cd);
							stmt.setString(3, sub_ct_ref[j]);
							stmt.setString(4, cont);
							stmt.setString(5, agmt);
							stmt.setString(6, comp_cd);
							stmt.setString(7, countpty_cd);
							stmt.setString(8, transCd);
							stmt.setString(9, transPlantSeq);
							stmt.setString(10, plantSeq);
							stmt.setString(11, cont_type);
							stmt.setString(12, buPlantSeq);
							stmt.setString(13, gas_dt);
							stmt.setString(14, upNomRevNo);
							stmt.setString(15, upSeqNo);
							stmt.setString(16, cargoNo);
							if(!temp_sub_ct_ref[j].equals(""))
							{
								stmt.setString(17, temp_sub_ct_ref[j]);
							}
							stmt.executeQuery();
							
							stmt.close();
						}
					}
				}
			}
			else //IF NO CT CONFIGURED THEN DEFUALT ENTRY WILL BE SUBMITTED IN DTL TABLE
			{
				String seqNo="1";
				
				int sub_rev_no=-1;
				query = "SELECT NVL(MAX(NOM_REV_NO),?) "
						+ "FROM FMS_DAILY_BUYER_NOM_DTL "
						+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
						+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? "
						+ "AND SEQ_NO=? AND CARGO_NO=? ";
				stmt1 = dbcon.prepareStatement(query);
				stmt1.setString(1, "-1");
				stmt1.setString(2, cont);
				stmt1.setString(3, agmt);
				stmt1.setString(4, comp_cd);
				stmt1.setString(5, countpty_cd);
				stmt1.setString(6, transCd);
				stmt1.setString(7, transPlantSeq);
				stmt1.setString(8, plantSeq);
				stmt1.setString(9, cont_type);
				stmt1.setString(10, gas_dt);
				stmt1.setString(11, buPlantSeq);
				stmt1.setString(12, seqNo);
				stmt1.setString(13, cargoNo);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					sub_rev_no = rset1.getInt(1)+1;
				}
				else
				{
					sub_rev_no = rev_no + 1;
				}
				rset1.close();
				stmt1.close();
				
				query="INSERT INTO FMS_DAILY_BUYER_NOM_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
						+ "TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
						+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,BU_SEQ,SEQ_NO,CT_REF,UTR_NO,CARGO_NO,SF_ID) "
						+ "VALUES(?,?,?,?,?,?,?,?,"
						+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
						+ "?,?,?,?,?,SYSDATE,?,?,?,?,?,?)";
				stmt2 = dbcon.prepareStatement(query);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, countpty_cd);
				stmt2.setString(3, agmt);
				stmt2.setString(4, agmt_rev);
				stmt2.setString(5, cont);
				stmt2.setString(6, cont_rev);
				stmt2.setInt(7, sub_rev_no);
				stmt2.setString(8, plantSeq);
				stmt2.setString(9, transCd);
				stmt2.setString(10, transPlantSeq);
				stmt2.setString(11, gas_dt);
				stmt2.setString(12, cont_type);
				stmt2.setString(13, gen_dt);
				stmt2.setString(14, genTime);
				stmt2.setString(15, Base);
				stmt2.setString(16, GCV);
				stmt2.setString(17, NCV);
				stmt2.setString(18, qtyMMBTU);
				stmt2.setString(19, qtySCM);
				stmt2.setString(20, emp_cd);
				stmt2.setString(21, buPlantSeq);
				stmt2.setString(22, seqNo);
				stmt2.setString(23, "");
				stmt2.setString(24, "");
				stmt2.setString(25, cargoNo);
				stmt2.setString(26, sf_id);
				stmt2.executeQuery();
				
				stmt2.close();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	static int block_count=0;
	private void InsertUpdateSellerNominationDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateSellerNominationDetail()";
		msg="";
		msg_type="";
		url="";
		
		block_count=0;
		
		try
		{
			String gas_dt = request.getParameter("gas_dt")==null?"":request.getParameter("gas_dt");
			String gen_dt = request.getParameter("gen_dt")==null?"":request.getParameter("gen_dt");
			
			String[] counterparty_cd=request.getParameterValues("counterparty_cd");
			String[] agmt_no=request.getParameterValues("agmt_no");
			String[] agmt_rev_no=request.getParameterValues("agmt_rev_no");
			String[] cont_no=request.getParameterValues("cont_no");
			String[] cont_rev_no=request.getParameterValues("cont_rev_no");
			String[] contract_type=request.getParameterValues("contract_type");
			String[] cargo_no=request.getParameterValues("cargo_no");
			String[] trans_cd=request.getParameterValues("trans_cd");
			String[] trans_plantseq=request.getParameterValues("trans_plant_seq");
			String[] plant_seq=request.getParameterValues("plant_seq");
			String[] bu_plant_seq=request.getParameterValues("bu_plant_seq");
			
			String[] gen_time = request.getParameterValues("gen_time");
			String[] base = request.getParameterValues("base");
			String[] gcv = request.getParameterValues("gcv");
			String[] ncv = request.getParameterValues("ncv");
			String[] qty_mmbtu = request.getParameterValues("qty_mmbtu");
			String[] qty_scm = request.getParameterValues("qty_scm");
			String[] sf_id = request.getParameterValues("sf_id");
			
			String[] index = request.getParameterValues("index"); //INDEX of l
			String[] index1 = request.getParameterValues("index1"); //TOTAL SIZE OF CT ENTRY
			
			String xmlFileNm="";
			
			if(counterparty_cd != null)
			{
				DocumentBuilderFactory docFactory = xmlUtil.dcoumentBuilderFactory();
			    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			    Document doc = docBuilder.newDocument();
			    Element ems = doc.createElement("EMS");
			    doc.appendChild(ems);
			    Element sellerNomination = doc.createElement("SELLER_NOMINATION");
			    ems.appendChild(sellerNomination);
		
				for(int i=0; i<counterparty_cd.length; i++)
				{
					String countpty_cd=counterparty_cd[i];
					String agmt=agmt_no[i];
					String agmt_rev=agmt_rev_no[i];
					String cont=cont_no[i];
					String cont_rev=cont_rev_no[i];
					String transCd=trans_cd[i];
					String transPlantSeq=trans_plantseq[i];
					String plantSeq=plant_seq[i];
					String cont_type=contract_type[i];
					String cargoNo=cargo_no[i];
					String buPlantSeq=bu_plant_seq[i];
					String genTime=gen_time[i];
					String Base=base[i];
					String GCV=gcv[i];
					String NCV=ncv[i];
					String qtyMMBTU=qty_mmbtu[i];
					String qtySCM=qty_scm[i];
					String l=index[i];
					String sub_size=index1[i];
					String SfId=sf_id[i];
					
					SellerNomination(request,gas_dt,gen_dt,countpty_cd,agmt,agmt_rev,cont,cont_rev,transCd,
							transPlantSeq,plantSeq,cont_type,cargoNo,buPlantSeq,genTime,Base,GCV,NCV,qtyMMBTU,qtySCM,l,sub_size,SfId,sellerNomination,doc);
				}
				
				if(block_count>0)
				{
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD","");
					transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet","");
					
				    Transformer transformer = transformerFactory.newTransformer();
				    DOMSource source = new DOMSource(doc);
				    
				    String sysdateWithTime=dateUtil.getSysdateWithTime24hrWithSS();
				    String[] splitSys = sysdateWithTime.split(" ");
				    String[] dateSplit = splitSys[0].split("/");
				    String datetime=dateSplit[2]+""+dateSplit[1]+""+dateSplit[0]+"_"+splitSys[1].replaceAll(":", "");
				    xmlFileNm="SellerNom_"+datetime+".xml";
				    
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
			        String sub_folder=""+CommonVariable.sf_xml;
			        File SubDir = new File(appPath+File.separator+main_folder+File.separator+sub_folder);
			        if(!SubDir.exists()) 
			        {
			        	SubDir.mkdir();
			        }
			        
			        String sub_folder_1=""+CommonVariable.sf_xml_outbound;
			        File SubDir_1 = new File(appPath+File.separator+main_folder+File.separator+sub_folder+File.separator+sub_folder_1);
			        if(!SubDir_1.exists()) 
			        {
			        	SubDir_1.mkdir();
			        }
			        
				    StreamResult result =  new StreamResult(new File(appPath+File.separator+main_folder+File.separator+sub_folder+File.separator+sub_folder_1+File.separator+""+xmlFileNm));
				    transformer.transform(source, result);
				}
			}
			
			msg = "Successful! - Seller Nomination Submitted for "+gas_dt+" Successfully!";
			if(!xmlFileNm.equals(""))
			{
				msg+=" "+xmlFileNm+" Generated!";
			}
			msg_type="S";
			
			url = "../contract_mgmt/frm_daily_seller_nom.jsp?msg="+msg+"&msg_type="+msg_type+"&gas_dt="+gas_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			msg="Error In Exception!";
			url=CommonVariable.errorpage_url+"?e="+e;
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
	
	private void SellerNomination(HttpServletRequest request,String gas_dt,String gen_dt,String countpty_cd,String agmt,String agmt_rev,String cont,String cont_rev,String transCd,
			String transPlantSeq,String plantSeq,String cont_type,String cargoNo,String buPlantSeq,String genTime,String Base,
			String GCV,String NCV,String qtyMMBTU,String qtySCM,String l,String sub_size,String sf_id,Element sellerNomination,Document doc)throws Exception
	{
		try
		{
			int rev_no=-1;
			query = "SELECT NVL(MAX(NOM_REV_NO),?) "
					+ "FROM FMS_DAILY_SELLER_NOM "
					+ "WHERE CONT_NO=? AND AGMT_NO=? "
					+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
					+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
					+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? AND CARGO_NO=? ";
			stmt = dbcon.prepareStatement(query);
			stmt.setString(1, "-1");
			stmt.setString(2, cont);
			stmt.setString(3, agmt);
			stmt.setString(4, comp_cd);
			stmt.setString(5, countpty_cd);
			stmt.setString(6, transCd);
			stmt.setString(7, transPlantSeq);
			stmt.setString(8, plantSeq);
			stmt.setString(9, cont_type);
			stmt.setString(10, gas_dt);
			stmt.setString(11, buPlantSeq);
			stmt.setString(12, cargoNo);
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
			
			query="INSERT INTO FMS_DAILY_SELLER_NOM(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
					+ "TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
					+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,BU_SEQ,CARGO_NO) "
					+ "VALUES(?,?,?,?,?,?,?,?,"
					+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
					+ "?,?,?,?,?,SYSDATE,?,?)";
			stmt0 = dbcon.prepareStatement(query);
			stmt0.setString(1, comp_cd);
			stmt0.setString(2, countpty_cd);
			stmt0.setString(3, agmt);
			stmt0.setString(4, agmt_rev);
			stmt0.setString(5, cont);
			stmt0.setString(6, cont_rev);
			stmt0.setInt(7, rev_no);
			stmt0.setString(8, plantSeq);
			stmt0.setString(9, transCd);
			stmt0.setString(10, transPlantSeq);
			stmt0.setString(11, gas_dt);
			stmt0.setString(12, cont_type);
			stmt0.setString(13, gen_dt);
			stmt0.setString(14, genTime);
			stmt0.setString(15, Base);
			stmt0.setString(16, GCV);
			stmt0.setString(17, NCV);
			stmt0.setString(18, qtyMMBTU);
			stmt0.setString(19, qtySCM);
			stmt0.setString(20, emp_cd);
			stmt0.setString(21, buPlantSeq);
			stmt0.setString(22, cargoNo);
			stmt0.executeQuery();
			
			stmt0.close();
			
			String[] sub_seq_no = request.getParameterValues("sub_seq_no"+l);
			String[] sub_ct_ref = request.getParameterValues("sub_ct_ref"+l);
			String[] sub_utr_ref = request.getParameterValues("sub_utr_ref"+l);
			String[] sub_qty_mmbtu = request.getParameterValues("sub_qty_mmbtu"+l);
			String[] sub_qty_scm = request.getParameterValues("sub_qty_scm"+l);
			String[] sub_sf_id = request.getParameterValues("sub_sf_id"+l);
			
		    if(sub_ct_ref!=null)
			{
				for(int j=0;j<sub_ct_ref.length;j++)
				{
					String seqNo="";
					if(sub_seq_no[j].equals(""))
					{
						query = "SELECT NVL(MAX(SEQ_NO),?) "
								+ "FROM FMS_DAILY_SELLER_NOM_DTL "
								+ "WHERE CONT_NO=? AND AGMT_NO=? "
								+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
								+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? AND CARGO_NO=? ";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, "0");
						stmt1.setString(2, cont);
						stmt1.setString(3, agmt);
						stmt1.setString(4, comp_cd);
						stmt1.setString(5, countpty_cd);
						stmt1.setString(6, transCd);
						stmt1.setString(7, transPlantSeq);
						stmt1.setString(8, plantSeq);
						stmt1.setString(9, cont_type);
						stmt1.setString(10, gas_dt);
						stmt1.setString(11, buPlantSeq);
						stmt1.setString(12, cargoNo);
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
					else
					{
						seqNo=sub_seq_no[j];
					}
					
					int sub_rev_no=-1;
					query = "SELECT NVL(MAX(NOM_REV_NO),?) "
							+ "FROM FMS_DAILY_SELLER_NOM_DTL "
							+ "WHERE CONT_NO=? AND AGMT_NO=? "
							+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
							+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
							+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? AND CARGO_NO=? "
							+ "AND SEQ_NO=?";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(1, "-1");
					stmt1.setString(2, cont);
					stmt1.setString(3, agmt);
					stmt1.setString(4, comp_cd);
					stmt1.setString(5, countpty_cd);
					stmt1.setString(6, transCd);
					stmt1.setString(7, transPlantSeq);
					stmt1.setString(8, plantSeq);
					stmt1.setString(9, cont_type);
					stmt1.setString(10, gas_dt);
					stmt1.setString(11, buPlantSeq);
					stmt1.setString(12, cargoNo);
					stmt1.setString(13, seqNo);
					rset1 = stmt1.executeQuery();
					if(rset1.next())
					{
						sub_rev_no = rset1.getInt(1)+1;
					}
					else
					{
						sub_rev_no = rev_no + 1;
					}
					rset1.close();
					stmt1.close();
					
					query="INSERT INTO FMS_DAILY_SELLER_NOM_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
							+ "TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
							+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,BU_SEQ,SEQ_NO,CT_REF,UTR_NO,CARGO_NO,SF_ID) "
							+ "VALUES(?,?,?,?,?,?,?,?,"
							+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
							+ "?,?,?,?,?,SYSDATE,?,?,?,?,?,?)";
					stmt2 = dbcon.prepareStatement(query);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, countpty_cd);
					stmt2.setString(3, agmt);
					stmt2.setString(4, agmt_rev);
					stmt2.setString(5, cont);
					stmt2.setString(6, cont_rev);
					stmt2.setInt(7, sub_rev_no);
					stmt2.setString(8, plantSeq);
					stmt2.setString(9, transCd);
					stmt2.setString(10, transPlantSeq);
					stmt2.setString(11, gas_dt);
					stmt2.setString(12, cont_type);
					stmt2.setString(13, gen_dt);
					stmt2.setString(14, genTime);
					stmt2.setString(15, Base);
					stmt2.setString(16, GCV);
					stmt2.setString(17, NCV);
					stmt2.setString(18, sub_qty_mmbtu[j]);
					stmt2.setString(19, sub_qty_scm[j]);
					stmt2.setString(20, emp_cd);
					stmt2.setString(21, buPlantSeq);
					stmt2.setString(22, seqNo);
					stmt2.setString(23, sub_ct_ref[j]);
					stmt2.setString(24, sub_utr_ref[j]);
					stmt2.setString(25, cargoNo);
					stmt2.setString(26, sub_sf_id[j]);
					stmt2.executeQuery();
					
					stmt2.close();
					
					if(!sub_sf_id[j].equals(""))
					{
						Element SCHEDULE = doc.createElement("SCHEDULE");
					    sellerNomination.appendChild(SCHEDULE);
						
						Element NOMINATION_SF_ID = doc.createElement("NOMINATION_SF_ID");
					    Element SCH_GAS_DAY = doc.createElement("SCH_GAS_DAY");
					    Element SCH_REV_NO = doc.createElement("SCH_REV_NO");
					    Element SCH_GEN_DAY = doc.createElement("SCH_GEN_DAY");
					    Element SCH_GEN_TIME = doc.createElement("SCH_GEN_TIME");
					    Element SCH_QTY = doc.createElement("SCH_QTY");
					    Element SCH_UNIT = doc.createElement("SCH_UNIT");
					    Element CT_CONTRACT_ID = doc.createElement("CT_CONTRACT_ID");
					    
						SCHEDULE.appendChild(NOMINATION_SF_ID);
					    SCHEDULE.appendChild(SCH_GAS_DAY);
					    SCHEDULE.appendChild(SCH_REV_NO);
					    SCHEDULE.appendChild(SCH_GEN_DAY);
					    SCHEDULE.appendChild(SCH_GEN_TIME);
					    SCHEDULE.appendChild(SCH_QTY);
					    SCHEDULE.appendChild(SCH_UNIT);
					    SCHEDULE.appendChild(CT_CONTRACT_ID);
					    
					    NOMINATION_SF_ID.appendChild(doc.createTextNode(sub_sf_id[j]));
					    SCH_GAS_DAY.appendChild(doc.createTextNode(gas_dt));
					    SCH_REV_NO.appendChild(doc.createTextNode(""+sub_rev_no));
					    SCH_GEN_DAY.appendChild(doc.createTextNode(""+gen_dt));
					    SCH_GEN_TIME.appendChild(doc.createTextNode(""+genTime));
					    SCH_QTY.appendChild(doc.createTextNode(""+sub_qty_mmbtu[j]));
					    SCH_UNIT.appendChild(doc.createTextNode("MMBTU"));
					    CT_CONTRACT_ID.appendChild(doc.createTextNode(sub_ct_ref[j]));
					    
					    block_count++;
					}
				}
			}
			else //IF NO CT CONFIGURED THEN DEFUALT ENTRY WILL BE SUBMITTED IN DTL TABLE
			{
				String seqNo="1";
				
				int sub_rev_no=-1;
				query = "SELECT NVL(MAX(NOM_REV_NO),?) "
						+ "FROM FMS_DAILY_SELLER_NOM_DTL "
						+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
						+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? "
						+ "AND SEQ_NO=? AND CARGO_NO=? ";
				stmt1 = dbcon.prepareStatement(query);
				stmt1.setString(1, "-1");
				stmt1.setString(2, cont);
				stmt1.setString(3, agmt);
				stmt1.setString(4, comp_cd);
				stmt1.setString(5, countpty_cd);
				stmt1.setString(6, transCd);
				stmt1.setString(7, transPlantSeq);
				stmt1.setString(8, plantSeq);
				stmt1.setString(9, cont_type);
				stmt1.setString(10, gas_dt);
				stmt1.setString(11, buPlantSeq);
				stmt1.setString(12, seqNo);
				stmt1.setString(13, cargoNo);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					sub_rev_no = rset1.getInt(1)+1;
				}
				else
				{
					sub_rev_no = rev_no + 1;
				}
				rset1.close();
				stmt1.close();
				
				query="INSERT INTO FMS_DAILY_SELLER_NOM_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
						+ "TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
						+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,BU_SEQ,SEQ_NO,CT_REF,UTR_NO,CARGO_NO,SF_ID) "
						+ "VALUES(?,?,?,?,?,?,?,?,"
						+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
						+ "?,?,?,?,?,SYSDATE,?,?,?,?,?,?)";
				stmt2 = dbcon.prepareStatement(query);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, countpty_cd);
				stmt2.setString(3, agmt);
				stmt2.setString(4, agmt_rev);
				stmt2.setString(5, cont);
				stmt2.setString(6, cont_rev);
				stmt2.setInt(7, sub_rev_no);
				stmt2.setString(8, plantSeq);
				stmt2.setString(9, transCd);
				stmt2.setString(10, transPlantSeq);
				stmt2.setString(11, gas_dt);
				stmt2.setString(12, cont_type);
				stmt2.setString(13, gen_dt);
				stmt2.setString(14, genTime);
				stmt2.setString(15, Base);
				stmt2.setString(16, GCV);
				stmt2.setString(17, NCV);
				stmt2.setString(18, qtyMMBTU);
				stmt2.setString(19, qtySCM);
				stmt2.setString(20, emp_cd);
				stmt2.setString(21, buPlantSeq);
				stmt2.setString(22, seqNo);
				stmt2.setString(23, "");
				stmt2.setString(24, "");
				stmt2.setString(25, cargoNo);
				stmt2.setString(26, sf_id);
				stmt2.executeQuery();
				
				stmt2.close();
				
				if(!sf_id.equals(""))
				{
					Element SCHEDULE = doc.createElement("SCHEDULE");
				    sellerNomination.appendChild(SCHEDULE);
				    
					Element NOMINATION_SF_ID = doc.createElement("NOMINATION_SF_ID");
				    Element SCH_GAS_DAY = doc.createElement("SCH_GAS_DAY");
				    Element SCH_REV_NO = doc.createElement("SCH_REV_NO");
				    Element SCH_GEN_DAY = doc.createElement("SCH_GEN_DAY");
				    Element SCH_GEN_TIME = doc.createElement("SCH_GEN_TIME");
				    Element SCH_QTY = doc.createElement("SCH_QTY");
				    Element SCH_UNIT = doc.createElement("SCH_UNIT");
				    Element CT_CONTRACT_ID = doc.createElement("CT_CONTRACT_ID");
				    
					SCHEDULE.appendChild(NOMINATION_SF_ID);
				    SCHEDULE.appendChild(SCH_GAS_DAY);
				    SCHEDULE.appendChild(SCH_REV_NO);
				    SCHEDULE.appendChild(SCH_GEN_DAY);
				    SCHEDULE.appendChild(SCH_GEN_TIME);
				    SCHEDULE.appendChild(SCH_QTY);
				    SCHEDULE.appendChild(SCH_UNIT);
				    SCHEDULE.appendChild(CT_CONTRACT_ID);
				    
				    NOMINATION_SF_ID.appendChild(doc.createTextNode(sf_id));
				    SCH_GAS_DAY.appendChild(doc.createTextNode(gas_dt));
				    SCH_REV_NO.appendChild(doc.createTextNode(""+sub_rev_no));
				    SCH_GEN_DAY.appendChild(doc.createTextNode(""+gen_dt));
				    SCH_GEN_TIME.appendChild(doc.createTextNode(""+genTime));
				    SCH_QTY.appendChild(doc.createTextNode(qtyMMBTU));
				    SCH_UNIT.appendChild(doc.createTextNode("MMBTU"));
				    CT_CONTRACT_ID.appendChild(doc.createTextNode(""));
				    
				    block_count++;
				}
			}
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	private void InsertUpdatePeriodicBuyerNominationDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdatePeriodicBuyerNominationDetail()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			String from_dt = request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
			String to_dt = request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String cargo_no = request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
			String nomination_freq = request.getParameter("nomination_freq")==null?"":request.getParameter("nomination_freq");
			
			String gas_dt = request.getParameter("gas_dt")==null?"":request.getParameter("gas_dt");
			
			String[] week_gas_dt=request.getParameterValues("week_gas_dt");
			String[] gen_dt=request.getParameterValues("gen_dt");
			String[] trans_cd=request.getParameterValues("trans_cd");
			String[] trans_plantseq=request.getParameterValues("trans_plant_seq");
			String[] plant_seq=request.getParameterValues("plant_seq");
			String[] bu_plant_seq=request.getParameterValues("bu_plant_seq");
			
			String[] gen_time = request.getParameterValues("gen_time");
			String[] base = request.getParameterValues("base");
			String[] gcv = request.getParameterValues("gcv");
			String[] ncv = request.getParameterValues("ncv");
			String[] qty_mmbtu = request.getParameterValues("qty_mmbtu");
			String[] qty_scm = request.getParameterValues("qty_scm");
			String[] sf_id = request.getParameterValues("sf_id");
			
			String[] index = request.getParameterValues("index"); //INDEX of l
			String[] index1 = request.getParameterValues("index1"); //TOTAL SIZE OF CT ENTRY
			
			if(week_gas_dt!=null)
			{
				for(int i=0; i<week_gas_dt.length; i++)
				{
					String genDt=gen_dt[i];
					String transCd=trans_cd[i];
					String transPlantSeq=trans_plantseq[i];
					String plantSeq=plant_seq[i];
					String buPlantSeq=bu_plant_seq[i];
					String genTime=gen_time[i];
					String Base=base[i];
					String GCV=gcv[i];
					String NCV=ncv[i];
					String qtyMMBTU=qty_mmbtu[i];
					String qtySCM=qty_scm[i];
					String l=index[i];
					String sub_size=index1[i];
					String weekof_gas_dt=week_gas_dt[i];
					String SfId=sf_id[i];
					
					BuyerNomination(request,weekof_gas_dt,genDt,counterparty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,transCd,
							transPlantSeq,plantSeq,contract_type,cargo_no,buPlantSeq,genTime,Base,GCV,NCV,qtyMMBTU,qtySCM,l,sub_size,SfId);
				}
			}
			
			if(nomination_freq.equals("W")) {
				msg = "Successful! - Weekly Buyer Nomination Submitted From "+from_dt+" to "+to_dt+" Successfully!";
			}else if(nomination_freq.equals("M")) {
				msg = "Successful! - Monthly Buyer Nomination Submitted From "+from_dt+" to "+to_dt+" Successfully!";
			}else if(nomination_freq.equals("F")) {
				msg = "Successful! - Fortnightly Buyer Nomination Submitted From "+from_dt+" to "+to_dt+" Successfully!";
			}
			msg_type="S";
			
			url = "../contract_mgmt/frm_buyer_periodic_nom.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&gas_dt="+gas_dt+"&nomination_freq="+nomination_freq+
					"&cargo_no="+cargo_no+
					"&clearance="+clearance+"&from_dt="+from_dt+"&to_dt="+to_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			msg="Error In Exception!";
			url=CommonVariable.errorpage_url+"?e="+e;
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
		
	private void InsertUpdatePeriodicSellerNominationDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdatePeriodicSellerNominationDetail()";
		msg="";
		msg_type="";
		url="";
		
		block_count=0;
		
		try
		{
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			String from_dt = request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
			String to_dt = request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String cargo_no = request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
			String nomination_freq = request.getParameter("nomination_freq")==null?"":request.getParameter("nomination_freq");
			
			String gas_dt = request.getParameter("gas_dt")==null?"":request.getParameter("gas_dt");
			
			String[] week_gas_dt=request.getParameterValues("week_gas_dt");
			String[] gen_dt=request.getParameterValues("gen_dt");
			String[] trans_cd=request.getParameterValues("trans_cd");
			String[] trans_plantseq=request.getParameterValues("trans_plant_seq");
			String[] plant_seq=request.getParameterValues("plant_seq");
			String[] bu_plant_seq=request.getParameterValues("bu_plant_seq");
			
			String[] gen_time = request.getParameterValues("gen_time");
			String[] base = request.getParameterValues("base");
			String[] gcv = request.getParameterValues("gcv");
			String[] ncv = request.getParameterValues("ncv");
			String[] qty_mmbtu = request.getParameterValues("qty_mmbtu");
			String[] qty_scm = request.getParameterValues("qty_scm");
			String[] sf_id = request.getParameterValues("sf_id");
			
			String[] index = request.getParameterValues("index"); //INDEX of l
			String[] index1 = request.getParameterValues("index1"); //TOTAL SIZE OF CT ENTRY
			
			String xmlFileNm="";
			
			if(week_gas_dt!=null)
			{
				DocumentBuilderFactory docFactory = xmlUtil.dcoumentBuilderFactory();
			    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			    Document doc = docBuilder.newDocument();
			    Element ems = doc.createElement("EMS");
			    doc.appendChild(ems);
			    Element sellerNomination = doc.createElement("SELLER_NOMINATION");
			    ems.appendChild(sellerNomination);
			    
				for(int i=0; i<week_gas_dt.length; i++)
				{
					String genDt=gen_dt[i];
					String transCd=trans_cd[i];
					String transPlantSeq=trans_plantseq[i];
					String plantSeq=plant_seq[i];
					String buPlantSeq=bu_plant_seq[i];
					String genTime=gen_time[i];
					String Base=base[i];
					String GCV=gcv[i];
					String NCV=ncv[i];
					String qtyMMBTU=qty_mmbtu[i];
					String qtySCM=qty_scm[i];
					String l=index[i];
					String sub_size=index1[i];
					String weekof_gas_dt=week_gas_dt[i];
					String SfId=sf_id[i];
						
					SellerNomination(request,weekof_gas_dt,genDt,counterparty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,transCd,
							transPlantSeq,plantSeq,contract_type,cargo_no,buPlantSeq,genTime,Base,GCV,NCV,qtyMMBTU,qtySCM,l,sub_size,SfId,sellerNomination,doc);
				}
				
				if(block_count>0)
				{
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD","");
					transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet","");
					
				    Transformer transformer = transformerFactory.newTransformer();
				    DOMSource source = new DOMSource(doc);
				    
				    String sysdateWithTime=dateUtil.getSysdateWithTime24hrWithSS();
				    String[] splitSys = sysdateWithTime.split(" ");
				    String[] dateSplit = splitSys[0].split("/");
				    String datetime=dateSplit[2]+""+dateSplit[1]+""+dateSplit[0]+"_"+splitSys[1].replaceAll(":", "");
				    xmlFileNm="SellerNom_"+datetime+".xml";
				    
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
			        String sub_folder=""+CommonVariable.sf_xml;
			        File SubDir = new File(appPath+File.separator+main_folder+File.separator+sub_folder);
			        if(!SubDir.exists()) 
			        {
			        	SubDir.mkdir();
			        }
			        
			        String sub_folder_1=""+CommonVariable.sf_xml_outbound;
			        File SubDir_1 = new File(appPath+File.separator+main_folder+File.separator+sub_folder+File.separator+sub_folder_1);
			        if(!SubDir_1.exists()) 
			        {
			        	SubDir_1.mkdir();
			        }
			        
				    StreamResult result =  new StreamResult(new File(appPath+File.separator+main_folder+File.separator+sub_folder+File.separator+sub_folder_1+File.separator+""+xmlFileNm));
				    transformer.transform(source, result);
				}
			}
			
			if(nomination_freq.equals("W")) {
				msg = "Successful! - Weekly Seller Nomination Submitted From "+from_dt+" to "+to_dt+" Successfully!";
			}else if(nomination_freq.equals("M")) {
				msg = "Successful! - Monthly Seller Nomination Submitted From "+from_dt+" to "+to_dt+" Successfully!";
			}else if(nomination_freq.equals("F")) {
				msg = "Successful! - Fortnightly Seller Nomination Submitted From "+from_dt+" to "+to_dt+" Successfully!";
			}
			if(!xmlFileNm.equals(""))
			{
				msg+=" "+xmlFileNm+" Generated!";
			}
			msg_type="S";
			
			url = "../contract_mgmt/frm_seller_periodic_nom.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&gas_dt="+gas_dt+"&nomination_freq="+nomination_freq+
					"&cargo_no="+cargo_no+
					"&clearance="+clearance+"&from_dt="+from_dt+"&to_dt="+to_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			msg="Error In Exception!";
			url=CommonVariable.errorpage_url+"?e="+e;
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
	
	private void InsertUpdateMeterTicketReadingDtl(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateMeterTicketReadingDtl()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String gas_dt = request.getParameter("gas_dt")==null?"":request.getParameter("gas_dt");
			String gen_dt = request.getParameter("gen_dt")==null?"":request.getParameter("gen_dt");
			String gen_time = request.getParameter("gen_time")==null?"":request.getParameter("gen_time");
			
			String[] counterparty_cd = request.getParameterValues("counterparty_cd");
			String[] plant_seq = request.getParameterValues("plant_seq");
			String[] meter_seq = request.getParameterValues("meter_seq");
			String[] meter_type = request.getParameterValues("meter_type");
			String[] qty_mmbtu = request.getParameterValues("qty_mmbtu");
			String[] qty_scm = request.getParameterValues("qty_scm");
			String[] reconcil_qty_mmbtu = request.getParameterValues("reconcil_qty_mmbtu");
			String[] reconcil_qty_scm = request.getParameterValues("reconcil_qty_scm");
			String[] total_qty_mmbtu = request.getParameterValues("total_qty_mmbtu");
			String[] total_qty_scm = request.getParameterValues("total_qty_scm");
			String[] calc_gcv = request.getParameterValues("calc_gcv");
			String[] calc_ncv = request.getParameterValues("calc_ncv");
			String[] define_gcv = request.getParameterValues("define_gcv");
			String[] define_ncv = request.getParameterValues("define_ncv");
			
			if(counterparty_cd != null)
			{
				for(int i=0; i<counterparty_cd.length; i++)
				{
					int count=0;
					query="SELECT COUNT(*) "
							+ "FROM FMS_METER_TICKET_READING "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND PLANT_SEQ=? AND METER_SEQ=? AND METER_TYPE=? "
							+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY')";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd[i]);
					stmt.setString(3, plant_seq[i]);
					stmt.setString(4, meter_seq[i]);
					stmt.setString(5, meter_type[i]);
					stmt.setString(6, gas_dt);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						count=rset.getInt(1);
					}
					rset.close();
					stmt.close();
					
					if(count > 0)
					{
						query="UPDATE FMS_METER_TICKET_READING SET GEN_DT=TO_DATE(?,'DD/MM/YYYY'),GEN_TIME=?,"
								+ "QTY_MMBTU=?,QTY_SCM=?,RECONCIL_QTY_MMBTU=?,"
								+ "RECONCIL_QTY_SCM=?,TOTAL_QTY_MMBTU=?,TOTAL_QTY_SCM=?,"
								+ "CALC_GCV=?,CALC_NCV=?,DEFINE_GCV=?,DEFINE_NCV=?,"
								+ "MODIFY_BY=?,MODIFY_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND PLANT_SEQ=? AND METER_SEQ=? AND METER_TYPE=? "
								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY')";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, gen_dt);
						stmt1.setString(2, gen_time);
						stmt1.setString(3, qty_mmbtu[i]);
						stmt1.setString(4, qty_scm[i]);
						stmt1.setString(5, reconcil_qty_mmbtu[i]);
						stmt1.setString(6, reconcil_qty_scm[i]);
						stmt1.setString(7, total_qty_mmbtu[i]);
						stmt1.setString(8, total_qty_scm[i]);
						stmt1.setString(9, calc_gcv[i]);
						stmt1.setString(10, calc_ncv[i]);
						stmt1.setString(11, define_gcv[i]);
						stmt1.setString(12, define_ncv[i]);
						stmt1.setString(13, emp_cd);
						stmt1.setString(14, comp_cd);
						stmt1.setString(15, counterparty_cd[i]);
						stmt1.setString(16, plant_seq[i]);
						stmt1.setString(17, meter_seq[i]);
						stmt1.setString(18, meter_type[i]);
						stmt1.setString(19, gas_dt);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					else
					{
						query="INSERT INTO FMS_METER_TICKET_READING(COMPANY_CD,COUNTERPARTY_CD,PLANT_SEQ,METER_TYPE,METER_SEQ,"
								+ "GAS_DT,GEN_DT,GEN_TIME,"
								+ "QTY_MMBTU,QTY_SCM,RECONCIL_QTY_MMBTU,RECONCIL_QTY_SCM,TOTAL_QTY_MMBTU,TOTAL_QTY_SCM,"
								+ "CALC_GCV,CALC_NCV,DEFINE_GCV,DEFINE_NCV,ENT_BY,ENT_DT) "
								+ "VALUES(?,?,?,?,?,"
								+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,"
								+ "?,?,?,?,?,?,"
								+ "?,?,?,?,?,SYSDATE)";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd[i]);
						stmt1.setString(3, plant_seq[i]);
						stmt1.setString(4, meter_type[i]);
						stmt1.setString(5, meter_seq[i]);
						stmt1.setString(6, gas_dt);
						stmt1.setString(7, gen_dt);
						stmt1.setString(8, gen_time);
						stmt1.setString(9, qty_mmbtu[i]);
						stmt1.setString(10, qty_scm[i]);
						stmt1.setString(11, reconcil_qty_mmbtu[i]);
						stmt1.setString(12, reconcil_qty_scm[i]);
						stmt1.setString(13, total_qty_mmbtu[i]);
						stmt1.setString(14, total_qty_scm[i]);
						stmt1.setString(15, calc_gcv[i]);
						stmt1.setString(16, calc_ncv[i]);
						stmt1.setString(17, define_gcv[i]);
						stmt1.setString(18, define_ncv[i]);
						stmt1.setString(19, emp_cd);
						
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					msg = "Successful! - Data Submitted Successfully!";
					msg_type="S";
				}
			}
			else
			{
				msg = "Failed! - Data Submission Failed!";
				msg_type="E";
			}
			
			url = "../contract_mgmt/frm_meter_ticket_reading.jsp?msg="+msg+"&msg_type="+msg_type+"&gas_dt="+gas_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			msg="Error In Exception!";
			url=CommonVariable.errorpage_url+"?e="+e;
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
	
	private void InsertUpdateDailyAllocationDtl(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateDailyAllocationDtl()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String gas_dt = request.getParameter("gas_dt")==null?"":request.getParameter("gas_dt");
			String gen_dt = request.getParameter("gen_dt")==null?"":request.getParameter("gen_dt");
			
			String[] counterparty_cd=request.getParameterValues("counterparty_cd");
			String[] agmt_no=request.getParameterValues("agmt_no");
			String[] agmt_rev_no=request.getParameterValues("agmt_rev_no");
			String[] cont_no=request.getParameterValues("cont_no");
			String[] cont_rev_no=request.getParameterValues("cont_rev_no");
			String[] contract_type=request.getParameterValues("contract_type");
			String[] cargo_no=request.getParameterValues("cargo_no");
			String[] trans_cd=request.getParameterValues("trans_cd");
			String[] trans_plantseq=request.getParameterValues("trans_plant_seq");
			String[] plant_seq=request.getParameterValues("plant_seq");
			String[] bu_plant_seq=request.getParameterValues("bu_plant_seq");
			
			String[] gen_time = request.getParameterValues("gen_time");
			String[] base = request.getParameterValues("base");
			String[] gcv = request.getParameterValues("gcv");
			String[] ncv = request.getParameterValues("ncv");
			String[] qty_mmbtu = request.getParameterValues("qty_mmbtu");
			String[] qty_scm = request.getParameterValues("qty_scm");
			
			String[] index = request.getParameterValues("index"); //INDEX of l
			String[] index1 = request.getParameterValues("index1"); //TOTAL SIZE OF CT ENTRY
			
			
			if(counterparty_cd != null)
			{
				for(int i=0; i<counterparty_cd.length; i++)
				{
					String countpty_cd=counterparty_cd[i];
					String agmt=agmt_no[i];
					String agmt_rev=agmt_rev_no[i];
					String cont=cont_no[i];
					String cont_rev=cont_rev_no[i];
					String transCd=trans_cd[i];
					String transPlantSeq=trans_plantseq[i];
					String plantSeq=plant_seq[i];
					String cont_type=contract_type[i];
					String cargoNo=cargo_no[i];
					String buPlantSeq=bu_plant_seq[i];
					String genTime=gen_time[i];
					String Base=base[i];
					String GCV=gcv[i];
					String NCV=ncv[i];
					String qtyMMBTU=qty_mmbtu[i];
					String qtySCM=qty_scm[i];
					String l=index[i];
					String sub_size=index1[i];
					
					String mole_mapping[] = request.getParameterValues("mole_mapping_"+l);
					String mole_seq_no[] = request.getParameterValues("mole_seq_no_"+l);
					String mole_qty_mmbtu[] = request.getParameterValues("mole_qty_mmbtu_"+l);
					String mole_qty_scm[] = request.getParameterValues("mole_qty_scm_"+l);
					
					int rev_no=-1;
					query = "SELECT NVL(MAX(NOM_REV_NO),?) "
							+ "FROM FMS_DAILY_ALLOCATION_DTL "
							+ "WHERE CONT_NO=? AND AGMT_NO=? "
							+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
							+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
							+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? AND CARGO_NO=? ";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, "-1");
					stmt.setString(2, cont);
					stmt.setString(3, agmt);
					stmt.setString(4, comp_cd);
					stmt.setString(5, countpty_cd);
					stmt.setString(6, transCd);
					stmt.setString(7, transPlantSeq);
					stmt.setString(8, plantSeq);
					stmt.setString(9, cont_type);
					stmt.setString(10, gas_dt);
					stmt.setString(11, buPlantSeq);
					stmt.setString(12, cargoNo);
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
					
					query="INSERT INTO FMS_DAILY_ALLOCATION_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
							+ "TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
							+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,BU_SEQ,CARGO_NO) "
							+ "VALUES(?,?,?,?,?,?,?,?,"
							+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
							+ "?,?,?,?,?,SYSDATE,?,?)";
					stmt0 = dbcon.prepareStatement(query);
					stmt0.setString(1, comp_cd);
					stmt0.setString(2, countpty_cd);
					stmt0.setString(3, agmt);
					stmt0.setString(4, agmt_rev);
					stmt0.setString(5, cont);
					stmt0.setString(6, cont_rev);
					stmt0.setInt(7, rev_no);
					stmt0.setString(8, plantSeq);
					stmt0.setString(9, transCd);
					stmt0.setString(10, transPlantSeq);
					stmt0.setString(11, gas_dt);
					stmt0.setString(12, cont_type);
					stmt0.setString(13, gen_dt);
					stmt0.setString(14, genTime);
					stmt0.setString(15, Base);
					stmt0.setString(16, GCV);
					stmt0.setString(17, NCV);
					stmt0.setString(18, qtyMMBTU);
					stmt0.setString(19, qtySCM);
					stmt0.setString(20, emp_cd);
					stmt0.setString(21, buPlantSeq);
					stmt0.setString(22, cargoNo);
					stmt0.executeQuery();
					
					stmt0.close();
					
					if(mole_mapping!=null)
					{
						String dtl_category="MOL";
						for(int j=0;j<mole_mapping.length;j++)
						{
							String seqNo="";
							if(mole_seq_no[j].equals(""))
							{
								query = "SELECT NVL(MAX(SEQ_NO),?) "
										+ "FROM FMS_DAILY_ALLOCATION_DTL_CT "
										+ "WHERE CONT_NO=? AND AGMT_NO=? "
										+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
										+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
										+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? AND CARGO_NO=? AND DTL_CATEGORY=? ";
								stmt1 = dbcon.prepareStatement(query);
								stmt1.setString(1, "0");
								stmt1.setString(2, cont);
								stmt1.setString(3, agmt);
								stmt1.setString(4, comp_cd);
								stmt1.setString(5, countpty_cd);
								stmt1.setString(6, transCd);
								stmt1.setString(7, transPlantSeq);
								stmt1.setString(8, plantSeq);
								stmt1.setString(9, cont_type);
								stmt1.setString(10, gas_dt);
								stmt1.setString(11, buPlantSeq);
								stmt1.setString(12, cargoNo);
								stmt1.setString(13, dtl_category);
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
							else
							{
								seqNo=mole_seq_no[j];
							}
							
							int mol_rev_no=-1;
							query = "SELECT NVL(MAX(NOM_REV_NO),?) "
									+ "FROM FMS_DAILY_ALLOCATION_DTL_CT "
									+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? "
									+ "AND SEQ_NO=? AND CARGO_NO=? AND DTL_CATEGORY=? ";
							stmt1 = dbcon.prepareStatement(query);
							stmt1.setString(1, "-1");
							stmt1.setString(2, cont);
							stmt1.setString(3, agmt);
							stmt1.setString(4, comp_cd);
							stmt1.setString(5, countpty_cd);
							stmt1.setString(6, transCd);
							stmt1.setString(7, transPlantSeq);
							stmt1.setString(8, plantSeq);
							stmt1.setString(9, cont_type);
							stmt1.setString(10, gas_dt);
							stmt1.setString(11, buPlantSeq);
							stmt1.setString(12, seqNo);
							stmt1.setString(13, cargoNo);
							stmt1.setString(14, dtl_category);
							rset1 = stmt1.executeQuery();
							if(rset1.next())
							{
								mol_rev_no = rset1.getInt(1)+1;
							}
							else
							{
								mol_rev_no = rev_no + 1;
							}
							rset1.close();
							stmt1.close();
							
							query="INSERT INTO FMS_DAILY_ALLOCATION_DTL_CT(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
									+ "TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
									+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,BU_SEQ,SEQ_NO,CARGO_NO,DTL_CATEGORY,MOLECULE_MAP) "
									+ "VALUES(?,?,?,?,?,?,?,?,"
									+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
									+ "?,?,?,?,?,SYSDATE,?,?,?,?,?)";
							stmt2 = dbcon.prepareStatement(query);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, countpty_cd);
							stmt2.setString(3, agmt);
							stmt2.setString(4, agmt_rev);
							stmt2.setString(5, cont);
							stmt2.setString(6, cont_rev);
							stmt2.setInt(7, mol_rev_no);
							stmt2.setString(8, plantSeq);
							stmt2.setString(9, transCd);
							stmt2.setString(10, transPlantSeq);
							stmt2.setString(11, gas_dt);
							stmt2.setString(12, cont_type);
							stmt2.setString(13, gen_dt);
							stmt2.setString(14, genTime);
							stmt2.setString(15, Base);
							stmt2.setString(16, GCV);
							stmt2.setString(17, NCV);
							stmt2.setString(18, mole_qty_mmbtu[j]);
							stmt2.setString(19, mole_qty_scm[j]);
							stmt2.setString(20, emp_cd);
							stmt2.setString(21, buPlantSeq);
							stmt2.setString(22, seqNo);
							stmt2.setString(23, cargoNo);
							stmt2.setString(24, dtl_category);
							stmt2.setString(25, mole_mapping[j]);
							stmt2.executeQuery();
							
							stmt2.close();
						}
					}
					
					String[] sub_seq_no = request.getParameterValues("sub_seq_no"+l);
					String[] sub_ct_ref = request.getParameterValues("sub_ct_ref"+l);
					String[] sub_utr_ref = request.getParameterValues("sub_utr_ref"+l);
					String[] sub_qty_mmbtu = request.getParameterValues("sub_qty_mmbtu"+l);
					String[] sub_qty_scm = request.getParameterValues("sub_qty_scm"+l);
					
					if(sub_ct_ref!=null)
					{
						String dtl_category="CT";
						for(int j=0;j<sub_ct_ref.length;j++)
						{
							String seqNo="";
							if(sub_seq_no[j].equals(""))
							{
								query = "SELECT NVL(MAX(SEQ_NO),?) "
										+ "FROM FMS_DAILY_ALLOCATION_DTL_CT "
										+ "WHERE CONT_NO=? AND AGMT_NO=? "
										+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
										+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
										+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? AND CARGO_NO=? AND DTL_CATEGORY=? ";
								stmt1 = dbcon.prepareStatement(query);
								stmt1.setString(1, "0");
								stmt1.setString(2, cont);
								stmt1.setString(3, agmt);
								stmt1.setString(4, comp_cd);
								stmt1.setString(5, countpty_cd);
								stmt1.setString(6, transCd);
								stmt1.setString(7, transPlantSeq);
								stmt1.setString(8, plantSeq);
								stmt1.setString(9, cont_type);
								stmt1.setString(10, gas_dt);
								stmt1.setString(11, buPlantSeq);
								stmt1.setString(12, cargoNo);
								stmt1.setString(13, dtl_category);
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
							else
							{
								seqNo=sub_seq_no[j];
							}
							
							int sub_rev_no=-1;
							query = "SELECT NVL(MAX(NOM_REV_NO),?) "
									+ "FROM FMS_DAILY_ALLOCATION_DTL_CT "
									+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? "
									+ "AND SEQ_NO=? AND CARGO_NO=? AND DTL_CATEGORY=? ";
							stmt1 = dbcon.prepareStatement(query);
							stmt1.setString(1, "-1");
							stmt1.setString(2, cont);
							stmt1.setString(3, agmt);
							stmt1.setString(4, comp_cd);
							stmt1.setString(5, countpty_cd);
							stmt1.setString(6, transCd);
							stmt1.setString(7, transPlantSeq);
							stmt1.setString(8, plantSeq);
							stmt1.setString(9, cont_type);
							stmt1.setString(10, gas_dt);
							stmt1.setString(11, buPlantSeq);
							stmt1.setString(12, seqNo);
							stmt1.setString(13, cargoNo);
							stmt1.setString(14, dtl_category);
							rset1 = stmt1.executeQuery();
							if(rset1.next())
							{
								sub_rev_no = rset1.getInt(1)+1;
							}
							else
							{
								sub_rev_no = rev_no + 1;
							}
							rset1.close();
							stmt1.close();
							
							query="INSERT INTO FMS_DAILY_ALLOCATION_DTL_CT(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
									+ "TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
									+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,BU_SEQ,SEQ_NO,CT_REF,UTR_NO,CARGO_NO,DTL_CATEGORY) "
									+ "VALUES(?,?,?,?,?,?,?,?,"
									+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
									+ "?,?,?,?,?,SYSDATE,?,?,?,?,?,?)";
							stmt2 = dbcon.prepareStatement(query);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, countpty_cd);
							stmt2.setString(3, agmt);
							stmt2.setString(4, agmt_rev);
							stmt2.setString(5, cont);
							stmt2.setString(6, cont_rev);
							stmt2.setInt(7, sub_rev_no);
							stmt2.setString(8, plantSeq);
							stmt2.setString(9, transCd);
							stmt2.setString(10, transPlantSeq);
							stmt2.setString(11, gas_dt);
							stmt2.setString(12, cont_type);
							stmt2.setString(13, gen_dt);
							stmt2.setString(14, genTime);
							stmt2.setString(15, Base);
							stmt2.setString(16, GCV);
							stmt2.setString(17, NCV);
							stmt2.setString(18, sub_qty_mmbtu[j]);
							stmt2.setString(19, sub_qty_scm[j]);
							stmt2.setString(20, emp_cd);
							stmt2.setString(21, buPlantSeq);
							stmt2.setString(22, seqNo);
							stmt2.setString(23, sub_ct_ref[j]);
							stmt2.setString(24, sub_utr_ref[j]);
							stmt2.setString(25, cargoNo);
							stmt2.setString(26, dtl_category);
							stmt2.executeQuery();
							
							stmt2.close();
						}
					}
					else //IF NO CT CONFIGURED THEN DEFUALT ENTRY WILL BE SUBMITTED IN DTL TABLE
					{
						String seqNo="1";
						String dtl_category="CT";
						int sub_rev_no=-1;
						query = "SELECT NVL(MAX(NOM_REV_NO),?) "
								+ "FROM FMS_DAILY_ALLOCATION_DTL_CT "
								+ "WHERE CONT_NO=? AND AGMT_NO=? "
								+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
								+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? "
								+ "AND SEQ_NO=? AND CARGO_NO=? AND DTL_CATEGORY=? ";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, "-1");
						stmt1.setString(2, cont);
						stmt1.setString(3, agmt);
						stmt1.setString(4, comp_cd);
						stmt1.setString(5, countpty_cd);
						stmt1.setString(6, transCd);
						stmt1.setString(7, transPlantSeq);
						stmt1.setString(8, plantSeq);
						stmt1.setString(9, cont_type);
						stmt1.setString(10, gas_dt);
						stmt1.setString(11, buPlantSeq);
						stmt1.setString(12, seqNo);
						stmt1.setString(13, cargoNo);
						stmt1.setString(14, dtl_category);
						rset1 = stmt1.executeQuery();
						if(rset1.next())
						{
							sub_rev_no = rset1.getInt(1)+1;
						}
						else
						{
							sub_rev_no = rev_no + 1;
						}
						rset1.close();
						stmt1.close();
						
						query="INSERT INTO FMS_DAILY_ALLOCATION_DTL_CT(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
								+ "TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
								+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,BU_SEQ,SEQ_NO,CT_REF,UTR_NO,CARGO_NO,DTL_CATEGORY) "
								+ "VALUES(?,?,?,?,?,?,?,?,"
								+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
								+ "?,?,?,?,?,SYSDATE,?,?,?,?,?,?)";
						stmt2 = dbcon.prepareStatement(query);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, countpty_cd);
						stmt2.setString(3, agmt);
						stmt2.setString(4, agmt_rev);
						stmt2.setString(5, cont);
						stmt2.setString(6, cont_rev);
						stmt2.setInt(7, sub_rev_no);
						stmt2.setString(8, plantSeq);
						stmt2.setString(9, transCd);
						stmt2.setString(10, transPlantSeq);
						stmt2.setString(11, gas_dt);
						stmt2.setString(12, cont_type);
						stmt2.setString(13, gen_dt);
						stmt2.setString(14, genTime);
						stmt2.setString(15, Base);
						stmt2.setString(16, GCV);
						stmt2.setString(17, NCV);
						stmt2.setString(18, qtyMMBTU);
						stmt2.setString(19, qtySCM);
						stmt2.setString(20, emp_cd);
						stmt2.setString(21, buPlantSeq);
						stmt2.setString(22, seqNo);
						stmt2.setString(23, "");
						stmt2.setString(24, "");
						stmt2.setString(25, cargoNo);
						stmt2.setString(26, dtl_category);
						stmt2.executeQuery();
						
						stmt2.close();
					}
				}
			}
			
			msg = "Successful! - Daily Allocation Submitted Successfully!";
			msg_type="S";
			
			url = "../contract_mgmt/frm_daily_allocation.jsp?msg="+msg+"&msg_type="+msg_type+"&gas_dt="+gas_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			msg="Error In Exception!";
			url=CommonVariable.errorpage_url+"?e="+e;
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
	
	private void InsertUpdateTransporterNominationDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateTransporterNominationDetail()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String gas_dt = request.getParameter("gas_dt")==null?"":request.getParameter("gas_dt");
			String gen_dt = request.getParameter("gen_dt")==null?"":request.getParameter("gen_dt");
			
			String[] counterparty_cd=request.getParameterValues("counterparty_cd");
			String[] agmt_no=request.getParameterValues("agmt_no");
			String[] agmt_rev_no=request.getParameterValues("agmt_rev_no");
			String[] cont_no=request.getParameterValues("cont_no");
			String[] cont_rev_no=request.getParameterValues("cont_rev_no");
			String[] contract_type=request.getParameterValues("contract_type");
			String[] trans_cd=request.getParameterValues("trans_cd");
			String[] trans_plantseq=request.getParameterValues("trans_plant_seq");
			String[] plant_seq=request.getParameterValues("plant_seq");
			String[] bu_plant_seq=request.getParameterValues("bu_plant_seq");
			
			String[] gen_time = request.getParameterValues("gen_time");
			String[] base = request.getParameterValues("base");
			String[] gcv = request.getParameterValues("gcv");
			String[] ncv = request.getParameterValues("ncv");
			String[] qty_mmbtu = request.getParameterValues("qty_mmbtu");
			String[] qty_scm = request.getParameterValues("qty_scm");
			
			String[] gta_no = request.getParameterValues("gta_no");
			String[] gtc_no = request.getParameterValues("gtc_no");
			String[] entry_point_map = request.getParameterValues("entry_point_map");
			String[] exit_point_map = request.getParameterValues("exit_point_map");
			
			String[] exit_base = request.getParameterValues("exit_base");
			String[] exit_gcv = request.getParameterValues("exit_gcv");
			String[] exit_ncv = request.getParameterValues("exit_ncv");
			String[] exit_qty_mmbtu = request.getParameterValues("exit_qty_mmbtu");
			String[] exit_qty_scm = request.getParameterValues("exit_qty_scm");
			
			
			if(counterparty_cd != null)
			{
				for(int i=0; i<counterparty_cd.length; i++)
				{
					String cont_map=counterparty_cd[i]+"-"+contract_type[i]+"-"+agmt_no[i]+"-"+agmt_rev_no[i]+"-"+cont_no[i]+"-"+cont_rev_no[i];
					
					int rev_no=-1;
					query = "SELECT NVL(MAX(NOM_REV_NO),?) "
							+ "FROM FMS_DAILY_TRANSPORTER_NOM "
							+ "WHERE CONT_NO=? AND AGMT_NO=? "
							+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONTRACT_TYPE=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND BU_SEQ=? AND SELL_CONT_MAP LIKE ? "
							+ "AND ENTRY_PT_MAPPING_ID=? AND EXIT_PT_MAPPING_ID=?";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, "-1");
					stmt.setString(2, gtc_no[i]);
					stmt.setString(3, gta_no[i]);
					stmt.setString(4, comp_cd);
					stmt.setString(5, trans_cd[i]);
					stmt.setString(6, "C");
					stmt.setString(7, gas_dt);
					stmt.setString(8, bu_plant_seq[i]);
					stmt.setString(9, cont_map);
					stmt.setString(10, entry_point_map[i]);
					stmt.setString(11, exit_point_map[i]);
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
					
					query="INSERT INTO FMS_DAILY_TRANSPORTER_NOM(COMPANY_CD,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
							+ "SELL_CONT_MAP,GAS_DT,GEN_DT,GEN_TIME,NOM_REV_NO,"
							+ "MDQ,MDQ_UNIT,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,"
							+ "EXIT_BASE,EXIT_GCV,EXIT_NCV,EXIT_QTY_MMBTU,EXIT_QTY_SCM,ENT_BY,ENT_DT,"
							+ "BU_SEQ,ENTRY_PT_MAPPING_ID,EXIT_PT_MAPPING_ID) "
							+ "VALUES(?,?,?,?,?,?,?,"
							+ "?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,"
							+ "?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,SYSDATE,"
							+ "?,?,?)";
					stmt0 = dbcon.prepareStatement(query);
					stmt0.setString(1, comp_cd);
					stmt0.setString(2, trans_cd[i]);
					stmt0.setString(3, "C");
					stmt0.setString(4, gta_no[i]);
					stmt0.setString(5, "0");
					stmt0.setString(6, gtc_no[i]);
					stmt0.setString(7, "0");
					stmt0.setString(8, cont_map);
					stmt0.setString(9, gas_dt);
					stmt0.setString(10, gen_dt);
					stmt0.setString(11, gen_time[i]);
					stmt0.setInt(12, rev_no);
					stmt0.setString(13, "0");
					stmt0.setString(14, "1");
					stmt0.setString(15, base[i]);
					stmt0.setString(16, gcv[i]);
					stmt0.setString(17, ncv[i]);
					stmt0.setString(18, qty_mmbtu[i]);
					stmt0.setString(19, qty_scm[i]);
					stmt0.setString(20, exit_base[i]);
					stmt0.setString(21, exit_gcv[i]);
					stmt0.setString(22, exit_ncv[i]);
					stmt0.setString(23, exit_qty_mmbtu[i]);
					stmt0.setString(24, exit_qty_scm[i]);
					stmt0.setString(25, emp_cd);
					stmt0.setString(26, bu_plant_seq[i]);
					stmt0.setString(27, entry_point_map[i]);
					stmt0.setString(28, exit_point_map[i]);
					stmt0.executeQuery();
					
					stmt0.close();
				}
			}
			
			msg = "Successful! - Transporter Nomination Submitted Successfully!";
			msg_type="S";
			
			url = "../contract_mgmt/frm_daily_transporter_nom.jsp?msg="+msg+"&msg_type="+msg_type+"&gas_dt="+gas_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			msg="Error In Exception!";
			url=CommonVariable.errorpage_url+"?e="+e;
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
	
	private void SendMail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="SendMail()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			
			String[] email_from = request.getParameterValues("email_from");
			String[] email_to = request.getParameterValues("email_to");
			String[] email_cc = request.getParameterValues("email_cc");
			String[] email_bcc = request.getParameterValues("email_bcc");
			String[] subject = request.getParameterValues("subject");
			String[] attachment = request.getParameterValues("attachment");
			String[] email_body = request.getParameterValues("email_body");
			
			String gas_dt = request.getParameter("gas_dt")==null?"":request.getParameter("gas_dt");
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String plant_seq = request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
			String bu_plant_seq = request.getParameter("bu_plant_seq")==null?"":request.getParameter("bu_plant_seq");
			String cargo_no = request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
			String mail_file_nm = request.getParameter("mail_file_nm")==null?"":request.getParameter("mail_file_nm");
			
			if(email_to != null)
			{
				for(int i=0;i<email_to.length;i++)
				{
					email_body[i]=email_body[i].replaceAll("\n", "<br>");
					email_body[i]="<html>"
							+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>"+email_body[i]+"</span>"
							+ "</html>";
					
					if(!email_to[i].equals("") && !email_body[i].equals(""))
					{
						mailDelv.sendMail(comp_cd,email_to[i], subject[i], email_body[i], attachment[i], email_cc[i], email_bcc[i]);
					}
					
					if(mail_file_nm.equals("frm_seller_nom_to_cust_send_mail.jsp"))
					{
						
						query="UPDATE FMS_DAILY_SELLER_NOM_REMARK SET MODIFIY_BY=?,MODIFIY_DT=SYSDATE,EMAIL_SENT='Y' "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND CONT_NO=? "
								+ "AND CONTRACT_TYPE=? AND PLANT_SEQ=? "
								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=?";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, emp_cd);
						stmt1.setString(2, comp_cd);
						stmt1.setString(3, counterparty_cd);
						stmt1.setString(4, agmt_no);
						stmt1.setString(5, cont_no);
						stmt1.setString(6, contract_type);
						stmt1.setString(7, plant_seq);
						stmt1.setString(8, gas_dt);
						stmt1.setString(9, cargo_no);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					else if(mail_file_nm.equals("frm_seller_nom_to_trans_send_mail.jsp"))
					{
						query="UPDATE FMS_DAILY_SELLER_NOM_TRANS SET MODIFIY_BY=?,MODIFIY_DT=SYSDATE,EMAIL_SENT='Y' "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND PLANT_SEQ=? "
								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND ENTITY_TYPE='R'";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, emp_cd);
						stmt1.setString(2, comp_cd);
						stmt1.setString(3, counterparty_cd);
						stmt1.setString(4, plant_seq);
						stmt1.setString(5, gas_dt);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					else if(mail_file_nm.equals("frm_send_mail.jsp"))
					{
						
						query="UPDATE FMS_DAILY_JOINT_TICKET SET MODIFIY_BY=?,MODIFIY_DT=SYSDATE,EMAIL_SENT='Y' "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND CONTRACT_TYPE=? AND PLANT_SEQ=? "
								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? AND ENTITY_TYPE='C'";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, emp_cd);
						stmt1.setString(2, comp_cd);
						stmt1.setString(3, counterparty_cd);
						stmt1.setString(4, contract_type);
						stmt1.setString(5, plant_seq);
						stmt1.setString(6, gas_dt);
						stmt1.setString(7, bu_plant_seq);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					else if(mail_file_nm.equals("frm_alloc_to_trans_send_mail.jsp"))
					{
						query="UPDATE FMS_DAILY_ALLOC_TRANS SET MODIFIY_BY=?,MODIFIY_DT=SYSDATE,EMAIL_SENT='Y' "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND PLANT_SEQ=? "
								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND ENTITY_TYPE='R'";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, emp_cd);
						stmt1.setString(2, comp_cd);
						stmt1.setString(3, counterparty_cd);
						stmt1.setString(4, plant_seq);
						stmt1.setString(5, gas_dt);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
						
					msg="Mail Sent for "+subject[i];
					msg_type="S";
				}
			}
			
			if(!mail_file_nm.equals(""))
			{
				url = "../contract_mgmt/"+mail_file_nm+"?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			}
			else
			{
				url = "../contract_mgmt/frm_send_mail.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			}
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			msg="Error In Exception!";
			url=CommonVariable.errorpage_url+"?e="+e;
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
	
	private void InsertUpdateSellerNomToCustomerRemark(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateSellerNomToCustomerRemark()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String mst_contract_type = request.getParameter("mst_contract_type")==null?"":request.getParameter("mst_contract_type");
			
			String gas_dt = request.getParameter("gas_dt")==null?"":request.getParameter("gas_dt");
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String plant_seq = request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
			String remark = request.getParameter("remark")==null?"":request.getParameter("remark");
			String addressed_to = request.getParameter("addressed_to")==null?"":request.getParameter("addressed_to");
			String cargo_no = request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
			
			int count=0;
			query = "SELECT COUNT(*) "
					+ "FROM FMS_DAILY_SELLER_NOM_REMARK "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND CONT_NO=? "
					+ "AND CONTRACT_TYPE=? AND PLANT_SEQ=? "
					+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=?";
			stmt = dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, cont_no);
			stmt.setString(5, contract_type);
			stmt.setString(6, plant_seq);
			stmt.setString(7, gas_dt);
			stmt.setString(8, cargo_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				count=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			if(count>0)
			{
				query="UPDATE FMS_DAILY_SELLER_NOM_REMARK SET ADDRESSED_TO=?,REMARK=?,MODIFIY_BY=?,MODIFIY_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND CONT_NO=? "
						+ "AND CONTRACT_TYPE=? AND PLANT_SEQ=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=?";
				stmt1 = dbcon.prepareStatement(query);
				stmt1.setString(1, addressed_to);
				stmt1.setString(2, remark);
				stmt1.setString(3, emp_cd);
				stmt1.setString(4, comp_cd);
				stmt1.setString(5, counterparty_cd);
				stmt1.setString(6, agmt_no);
				stmt1.setString(7, cont_no);
				stmt1.setString(8, contract_type);
				stmt1.setString(9, plant_seq);
				stmt1.setString(10, gas_dt);
				stmt1.setString(11, cargo_no);
				stmt1.executeUpdate();
				
				stmt1.close();
			}
			else
			{
				query = "INSERT INTO FMS_DAILY_SELLER_NOM_REMARK(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,CONT_NO,CONTRACT_TYPE,PLANT_SEQ,"
						+ "GAS_DT,REMARK,ENT_DT,ENT_BY,CARGO_NO,ADDRESSED_TO) "
						+ "VALUES(?,?,?,?,?,?,"
						+ "TO_DATE(?,'DD/MM/YYYY'),?,SYSDATE,?,?,?)";
				stmt1 = dbcon.prepareStatement(query);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, agmt_no);
				stmt1.setString(4, cont_no);
				stmt1.setString(5, contract_type);
				stmt1.setString(6, plant_seq);
				stmt1.setString(7, gas_dt);
				stmt1.setString(8, remark);
				stmt1.setString(9, emp_cd);
				stmt1.setString(10, cargo_no);
				stmt1.setString(11, addressed_to);
				stmt1.executeUpdate();
				
				stmt1.close();
			}
			
			msg = "Successful! - Data Submitted Successfully!";
			msg_type="S";
			
			url = "../contract_mgmt/rpt_seller_nomination_to_customer.jsp?msg="+msg+"&msg_type="+msg_type+"&gas_dt="+gas_dt+"&contract_type="+mst_contract_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			msg="Error In Exception!";
			url=CommonVariable.errorpage_url+"?e="+e;
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
