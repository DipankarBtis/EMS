package com.etrm.fms.dlng;

import java.io.File;
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
import com.etrm.fms.util.TaxCalculator;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.XmlUtilBean;
import com.etrm.fms.util.escapeSingleQuotes;

//Coded By          : Harsh Maheta
//Code Reviewed by	:  
//CR Date			: 18/04/2025 
//Status	  		: Developing

@WebServlet("/servlet/Frm_Dlng_ContractMgmt")
public class Frm_Dlng_ContractMgmt extends HttpServlet
{
	static String db_src_file_name="Frm_Dlng_ContractMgmt.java";
	public static Connection dbcon;
	
	public static String servletName = "Frm_Dlng_ContractMgmt";
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
	private static PreparedStatement stmt_temp = null;
	private static PreparedStatement stmt_temp1 = null;
	
	private static ResultSet rset = null;
	private static ResultSet rset1 = null;
	private static ResultSet rset2 = null;
	private static ResultSet rset3 = null;
	private static ResultSet rset4 = null;
	private static ResultSet rset_temp = null;
	private static ResultSet rset_temp1 = null;
	
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
	static TaxCalculator TaxCalc = new TaxCalculator(); 
	static UtilBean_DLNG dlng_util = new UtilBean_DLNG();
	
	static NumberFormat nf = new DecimalFormat("###########0.00");
	static NumberFormat nf2 = new DecimalFormat("###########0.0000");
	
	static Vector MSG_RPT_DT = new Vector();
	static Vector MSG_DEAL_NO = new Vector();
	static Vector MSG_TRUCK_REG_NO = new Vector();
	static Vector MSG_QTY = new Vector();
	static Vector MSG_ALLOW_CREDIT = new Vector();
	static Vector MSG_CONSUMED_AMT = new Vector();
	static Vector MSG_BALANCE = new Vector();
	static Vector MSG_SUBMITTED = new Vector();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException
	{
		synchronized(this)
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
						else if(option.equalsIgnoreCase("DAILY_ALLOCATION"))
						{
							InsertUpdateDailyAllocationDtl(request);
						}
						else if(option.equalsIgnoreCase("SEND_MAIL"))
						{
							SendMail(request);
						}
						else if(option.equalsIgnoreCase("DLNG_SELLER_NOM_TO_CUST_REMARK"))
						{
							InsertUpdateSellerNomToCustomerRemark(request);
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
			
			try 
			{
				response.sendRedirect(url);
			}
			catch(IOException e)
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			}
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
			String submit_flag = request.getParameter("submit_flag")==null?"":request.getParameter("submit_flag");
			
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
			String[] qty_mt = request.getParameterValues("qty_mt");
			String[] qty_scm = request.getParameterValues("qty_scm");
			String[] sf_id = request.getParameterValues("sf_id");
			
			String[] index = request.getParameterValues("index"); //INDEX of l
			//String[] index1 = request.getParameterValues("index1"); //TOTAL SIZE OF CT ENTRY
			
			if(counterparty_cd != null)
			{
				for(int i=0; i<counterparty_cd.length; i++)
				{
					String countpty_cd=counterparty_cd[i];
					String agmt=agmt_no[i];
					String agmt_rev=agmt_rev_no[i];
					String cont=cont_no[i];
					String cont_rev=cont_rev_no[i];
					String plantSeq=plant_seq[i];
					String cont_type=contract_type[i];
					String cargoNo=cargo_no[i];
					String buPlantSeq=bu_plant_seq[i];
					String genTime=gen_time[i];
					String Base=base[i];
					String GCV=gcv[i];
					String NCV=ncv[i];
					String qtyMMBTU=qty_mmbtu[i];
					String qtyMT=qty_mt[i];
					String qtySCM=qty_scm[i];
					String l=index[i];
					//String sub_size=index1[i];
					String SfId = sf_id[i];
					
					BuyerNomination(request,gas_dt,gen_dt,countpty_cd,agmt,agmt_rev,cont,cont_rev,
							plantSeq,cont_type,buPlantSeq,genTime,Base,GCV,NCV,qtyMMBTU,qtyMT,qtySCM,l,SfId,submit_flag,cargoNo);
				}
				
				msg = "Successful! - Buyer Nomination Submitted for "+gas_dt+" Successfully!";
				msg_type="S";
			}
			else 
			{
				msg = "Failed! - Buyer Nomination does not Submitted for "+gas_dt+" !";
				msg_type="E";
			}
			
			url = "../dlng/frm_dlng_daily_buyer_nom.jsp?msg="+msg+"&msg_type="+msg_type+"&gas_dt="+gas_dt+commonUrl_pra;
			
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
	
	private void BuyerNomination(HttpServletRequest request,String gas_dt,String gen_dt,String countpty_cd,String agmt,String agmt_rev,String cont,String cont_rev,
			String plantSeq,String cont_type,String buPlantSeq,String genTime,String Base,
			String GCV,String NCV,String qtyMMBTU,String qtyMT,String qtySCM,String l,String sf_id, String submit_flag, String cargo_no)throws Exception
	{
		try
		{
			int rev_no=-1;
			query = "SELECT NVL(MAX(NOM_REV_NO),'-1') "
					+ "FROM FMS_DLNG_BUYER_NOM "
					+ "WHERE CONT_NO=? AND AGMT_NO=? "
					+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
					+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? "
					+ "AND CARGO_NO=? ";
			stmt = dbcon.prepareStatement(query);
			stmt.setString(1, cont);
			stmt.setString(2, agmt);
			stmt.setString(3, comp_cd);
			stmt.setString(4, countpty_cd);
			stmt.setString(5, plantSeq);
			stmt.setString(6, cont_type);
			stmt.setString(7, gas_dt);
			stmt.setString(8, buPlantSeq);
			stmt.setString(9, cargo_no);
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
			
			int insCnt=0;
			
			query="INSERT INTO FMS_DLNG_BUYER_NOM(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
					+ "GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
					+ "GCV,NCV,QTY_MMBTU,QTY_MT,QTY_SCM,ENT_BY,ENT_DT,BU_SEQ,CARGO_NO) "
					+ "VALUES(?,?,?,?,?,?,?,?,"
					+ "TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
					+ "?,?,?,?,?,?,SYSDATE,?,?)";
			stmt0 = dbcon.prepareStatement(query);
			stmt0.setString(++insCnt, comp_cd);
			stmt0.setString(++insCnt, countpty_cd);
			stmt0.setString(++insCnt, agmt);
			stmt0.setString(++insCnt, agmt_rev);
			stmt0.setString(++insCnt, cont);
			stmt0.setString(++insCnt, cont_rev);
			stmt0.setInt(++insCnt, rev_no);
			stmt0.setString(++insCnt, plantSeq);
			stmt0.setString(++insCnt, gas_dt);
			stmt0.setString(++insCnt, cont_type);
			stmt0.setString(++insCnt, gen_dt);
			stmt0.setString(++insCnt, genTime);
			stmt0.setString(++insCnt, Base);
			stmt0.setString(++insCnt, GCV);
			stmt0.setString(++insCnt, NCV);
			stmt0.setString(++insCnt, qtyMMBTU);
			stmt0.setString(++insCnt, qtyMT);
			stmt0.setString(++insCnt, qtySCM);
			stmt0.setString(++insCnt, emp_cd);
			stmt0.setString(++insCnt, buPlantSeq);
			stmt0.setString(++insCnt, cargo_no);
			stmt0.executeQuery();
			
			stmt0.close();
			
			String truck_trans_cd[] = request.getParameterValues("truck_trans_cd_"+l);
			String truck_cd[] = request.getParameterValues("truck_cd_"+l);
			
			String nom_qunt_mmbtu[] = request.getParameterValues("nom_qunt_mmbtu_"+l);
			String nom_qunt_mt[] = request.getParameterValues("nom_qunt_mt_"+l);
			String filling_station[] = request.getParameterValues("filling_station_"+l);
			String sel_bay[] = request.getParameterValues("sel_bay_"+l);
			String sel_slot[] = request.getParameterValues("sel_slot_"+l);
			String arrival_dt[] = request.getParameterValues("arrival_dt_"+l);
			String arrival_time[] = request.getParameterValues("arrival_time_"+l);
			String next_avl_hrs[] = request.getParameterValues("next_avl_hrs_"+l);
			String truck_remark[] = request.getParameterValues("truck_remark_"+l);
			
			int truck_rev_no=-1;
			query2 = "SELECT NVL(MAX(NOM_REV_NO),'-1') "
					+ "FROM FMS_DLNG_BUYER_NOM "
					+ "WHERE CONT_NO=? AND AGMT_NO=? "
					+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
					+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? AND CARGO_NO=? ";
					//+ "AND TRUCK_TRANS_CD=? AND TRUCK_CD=? ";
			stmt2 = dbcon.prepareStatement(query2);
			stmt2.setString(1, cont);
			stmt2.setString(2, agmt);
			stmt2.setString(3, comp_cd);
			stmt2.setString(4, countpty_cd);
			stmt2.setString(5, plantSeq);
			stmt2.setString(6, cont_type);
			stmt2.setString(7, gas_dt);
			stmt2.setString(8, buPlantSeq);
			stmt2.setString(9, cargo_no);
			//stmt2.setString(9, truck_trans_cd[j]);
			//stmt2.setString(10, truck_cd[j]);
			rset2 = stmt2.executeQuery();
			if(rset2.next())
			{
				truck_rev_no = rset2.getInt(1);
			}
			else
			{
				truck_rev_no = truck_rev_no + 1;
			}
			rset2.close();
			stmt2.close();
			
			if(truck_cd!=null)
			{
				for(int j=0;j<truck_cd.length;j++)
				{
					if(!truck_cd[j].equals(""))
					{
						String slotStrTime=sel_slot[j].split("-")[0];
						String slotEndTime=sel_slot[j].split("-")[1];
						
						insCnt=0;
						
						query1="INSERT INTO FMS_DLNG_BUYER_NOM_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,"
								+ "AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,"
								+ "TRUCK_TRANS_CD,TRUCK_CD,NOM_REV_NO,QTY_MMBTU,QTY_MT,"
								+ "FILL_STATION_CD,BAY_CD,SLOT_START_TIME,SLOT_END_TIME,ARRIVAL_DT,ARRIVAL_TIME,NEXT_AVAIL_HRS,"
								+ "GEN_DT,GEN_TIME,BASE,GCV,NCV,REMARK,ENT_BY,ENT_DT,CARGO_NO) "
								+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,?,"
								+ "TO_DATE(?,'DD/MM/YYYY'),?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,SYSDATE,?)";
						stmt1 = dbcon.prepareStatement(query1);
						stmt1.setString(++insCnt, comp_cd);
						stmt1.setString(++insCnt, countpty_cd);
						stmt1.setString(++insCnt, agmt);
						stmt1.setString(++insCnt, agmt_rev);
						stmt1.setString(++insCnt, cont);
						stmt1.setString(++insCnt, cont_rev);
						stmt1.setString(++insCnt, plantSeq);
						stmt1.setString(++insCnt, buPlantSeq);
						stmt1.setString(++insCnt, gas_dt);
						stmt1.setString(++insCnt, cont_type);
						stmt1.setString(++insCnt, truck_trans_cd[j]);
						stmt1.setString(++insCnt, truck_cd[j]);
						stmt1.setInt(++insCnt, truck_rev_no);
						stmt1.setString(++insCnt, nom_qunt_mmbtu[j]);
						stmt1.setString(++insCnt, nom_qunt_mt[j]);
						stmt1.setString(++insCnt, filling_station[j]);
						stmt1.setString(++insCnt, sel_bay[j]);
						stmt1.setString(++insCnt, slotStrTime);
						stmt1.setString(++insCnt, slotEndTime);
						stmt1.setString(++insCnt, arrival_dt[j]);
						stmt1.setString(++insCnt, arrival_time[j]);
						stmt1.setString(++insCnt, next_avl_hrs[j]);
						stmt1.setString(++insCnt, gen_dt);
						stmt1.setString(++insCnt, genTime);
						stmt1.setString(++insCnt, Base);
						stmt1.setString(++insCnt, GCV);
						stmt1.setString(++insCnt, NCV);
						stmt1.setString(++insCnt, truck_remark[j]);
						stmt1.setString(++insCnt, emp_cd);
						stmt1.setString(++insCnt, cargo_no);
						stmt1.executeQuery();
						
						stmt1.close();
					}
				}
			}
			else if(submit_flag.equals("NOM_DTL"))
			{
				insCnt=0;
				
				query1="INSERT INTO FMS_DLNG_BUYER_NOM_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,"
						+ "AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,"
						+ "TRUCK_TRANS_CD,TRUCK_CD,NOM_REV_NO,QTY_MMBTU,QTY_MT,"
						+ "FILL_STATION_CD,BAY_CD,SLOT_START_TIME,SLOT_END_TIME,ARRIVAL_DT,ARRIVAL_TIME,NEXT_AVAIL_HRS,"
						+ "GEN_DT,GEN_TIME,BASE,GCV,NCV,REMARK,ENT_BY,ENT_DT,CARGO_NO) "
						+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,?,"
						//+ "TO_DATE(?,'DD/MM/YYYY'),"
						+ "SYSDATE,"
						+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,SYSDATE,?)";
				stmt1 = dbcon.prepareStatement(query1);
				stmt1.setString(++insCnt, comp_cd);
				stmt1.setString(++insCnt, countpty_cd);
				stmt1.setString(++insCnt, agmt);
				stmt1.setString(++insCnt, agmt_rev);
				stmt1.setString(++insCnt, cont);
				stmt1.setString(++insCnt, cont_rev);
				stmt1.setString(++insCnt, plantSeq);
				stmt1.setString(++insCnt, buPlantSeq);
				stmt1.setString(++insCnt, gas_dt);
				stmt1.setString(++insCnt, cont_type);
				stmt1.setString(++insCnt, "0");
				stmt1.setString(++insCnt, "0");
				stmt1.setInt(++insCnt, truck_rev_no);
				stmt1.setString(++insCnt, "0");
				stmt1.setString(++insCnt, "0");
				stmt1.setString(++insCnt, "0");
				stmt1.setString(++insCnt, "0");
				stmt1.setString(++insCnt, "0");
				stmt1.setString(++insCnt, "0");
				//stmt1.setString(++insCnt, "");
				stmt1.setString(++insCnt, "0");
				stmt1.setString(++insCnt, "0");
				stmt1.setString(++insCnt, gen_dt);
				stmt1.setString(++insCnt, genTime);
				stmt1.setString(++insCnt, Base);
				stmt1.setString(++insCnt, GCV);
				stmt1.setString(++insCnt, NCV);
				stmt1.setString(++insCnt, "RELESE");
				stmt1.setString(++insCnt, emp_cd);
				stmt1.setString(++insCnt, cargo_no);
				stmt1.executeQuery();
				
				stmt1.close();
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
		
		MSG_RPT_DT.clear();
		MSG_DEAL_NO.clear();
		MSG_TRUCK_REG_NO.clear();
		MSG_QTY.clear();
		MSG_ALLOW_CREDIT.clear();
		MSG_CONSUMED_AMT.clear();
		MSG_BALANCE.clear();
		MSG_SUBMITTED.clear();
		
		try
		{
			HttpSession session = request.getSession();
			
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
			//String[] qty_scm = request.getParameterValues("buyer_nom_scm");
			String[] qty_mt = request.getParameterValues("qty_mt");
			String[] sf_id = request.getParameterValues("sf_id");
			
			String[] index = request.getParameterValues("index"); //INDEX of j
			//String[] index1 = request.getParameterValues("index1"); //TOTAL SIZE OF CT ENTRY
			
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
					//String transCd=trans_cd[i];
					//String transPlantSeq=trans_plantseq[i];
					String plantSeq=plant_seq[i];
					String cont_type=contract_type[i];
					//String cargoNo=cargo_no[i];
					String buPlantSeq=bu_plant_seq[i];
					String genTime=gen_time[i];
					String Base=base[i];
					String GCV=gcv[i];
					String NCV=ncv[i];
					String qtyMMBTU=qty_mmbtu[i];
					String qtySCM="";//qty_scm[i];
					String qtyMT=qty_mt[i];
					String l=index[i];
					//String sub_size=index1[i];
					String SfId=sf_id[i];
					String cargoNo=cargo_no[i];
					
					
					String price="";
					String price_unit="";
					double credit=0;
					double exchng_rate=0;
					String cont_price="";
					
					int OA_COUNT=0;
					query2 ="SELECT COUNT(*) "
							+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.GX=? "
							+ "AND B.AGMT_NO=? AND B.CONT_NO=? AND B.CONTRACT_TYPE=? "
							+ "AND SEC_TYPE IN ('OA') "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.SEQ_NO=B.SEQ_NO "
							+ "AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, countpty_cd);
					stmt2.setString(3, "K");
					stmt2.setString(4, agmt);
					stmt2.setString(5, cont);
					stmt2.setString(6, cont_type);
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						OA_COUNT=rset2.getInt(1);
					}
					rset2.close();
					stmt2.close();
					
					if(!cont_type.equals("W") && OA_COUNT == 0)
					{
						String queryString="SELECT TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),DCQ,RATE,RATE_UNIT,"
								+ "CONT_REF_NO,TRADE_REF_NO "
								+ "FROM FMS_SUPPLY_CONT_MST A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
								+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
								+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "; 
						stmt=dbcon.prepareStatement(queryString);
						stmt.setString(1, comp_cd);
						stmt.setString(2, countpty_cd);
						stmt.setString(3, agmt);
						stmt.setString(4, cont);
						stmt.setString(5, cont_type);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							//contract_start_dt=rset.getString(1)==null?"":rset.getString(1);
							//contract_end_dt=rset.getString(2)==null?"":rset.getString(2);
							//dcq=rset.getString(3)==null?"":nf.format(rset.getDouble(3));
							price_unit=rset.getString(5)==null?"":rset.getString(5);
							price=rset.getString(4)==null?"":utilBean.RateNumberFormat(rset.getDouble(4), price_unit);
						}
						rset.close();
						stmt.close();
						
						credit=utilBean.getAllowableCreditAmount(dbcon, comp_cd, countpty_cd, agmt, cont, cont_type, "K", gas_dt,plantSeq,buPlantSeq);
						exchng_rate=utilBean.getContExchangeRate(dbcon,comp_cd, countpty_cd, agmt, cont, cont_type, gas_dt);
						String queryString1 = "SELECT DISTINCT NEW_SALE_PRICE "
								+ "FROM FMS_SUPPLY_ALLOC_REVISED A "
								+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND CONT_NO=? "
								+ "AND COUNTERPARTY_CD=? AND FLAG=? AND CONTRACT_TYPE=? "
								+ "AND MODIFICATION_SEQ_NO = (SELECT MAX(MODIFICATION_SEQ_NO) FROM FMS_SUPPLY_ALLOC_REVISED B "
								+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.FLAG=B.FLAG AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
								+ "AND B.NEW_PRICE_EFF_DT <=TO_DATE(?,'DD/MM/YYYY'))";
						stmt1=dbcon.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, agmt);
						stmt1.setString(3, cont);
						stmt1.setString(4, countpty_cd);
						stmt1.setString(5, "A");
						stmt1.setString(6, cont_type);
						stmt1.setString(7, gas_dt);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							cont_price=rset1.getString(1)==null?"":utilBean.RateNumberFormat(rset1.getDouble(1), price_unit);
						}
						rset1.close();
						stmt1.close();
						
						if(cont_price.equals(""))
						{
							cont_price=price;
						}
					}
					
					SellerNomination(request,gas_dt,gen_dt,countpty_cd,agmt,agmt_rev,cont,cont_rev,
							plantSeq,cont_type,cargoNo,buPlantSeq,genTime,Base,GCV,NCV,qtyMMBTU,qtySCM,l,"",SfId,sellerNomination,doc,qtyMT,
							credit,exchng_rate,cont_price,price_unit,OA_COUNT);
				
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
				    xmlFileNm="DLNG_SellerNom_"+datetime+".xml";
				    
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
			
			if(MSG_RPT_DT.size() > 0)
			{
				session.setAttribute("MSG_TRUCK_REG_NO", MSG_TRUCK_REG_NO);
				session.setAttribute("MSG_DEAL_NO", MSG_DEAL_NO);
				session.setAttribute("MSG_QTY", MSG_QTY);
				session.setAttribute("MSG_ALLOW_CREDIT", MSG_ALLOW_CREDIT);
				session.setAttribute("MSG_CONSUMED_AMT", MSG_CONSUMED_AMT);
				session.setAttribute("MSG_BALANCE", MSG_BALANCE);
				session.setAttribute("MSG_SUBMITTED", MSG_SUBMITTED);
				session.setAttribute("MSG_RPT_DT", MSG_RPT_DT);
			}
			
			msg = "Successful! - Seller Nomination Submitted for "+gas_dt+" Successfully!";
			if(!xmlFileNm.equals(""))
			{
				msg+=" "+xmlFileNm+" Generated!";
			}
			msg_type="S";
			
			url = "../dlng/frm_dlng_daily_seller_nom.jsp?msg="+msg+"&msg_type="+msg_type+"&gas_dt="+gas_dt+commonUrl_pra;
			
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
	
	private void SellerNomination(HttpServletRequest request,String gas_dt,String gen_dt,String countpty_cd,String agmt,String agmt_rev,String cont,String cont_rev,
			String plantSeq,String cont_type,String cargoNo,String buPlantSeq,String genTime,String Base,
			String GCV,String NCV,String qtyMMBTU,String qtySCM,String l,String sub_size,String sf_id,Element sellerNomination,Document doc,String qtyMT, 
			double credit_amt,double exchng_rate,String price,String price_unit, int OA_COUNT)throws Exception
	{
		try
		{
			int rev_no=-1;
			
			String truck_trans_cd[] = request.getParameterValues("truck_trans_cd_"+l);
			String truck_cd[] = request.getParameterValues("truck_cd_"+l);
			
			String nom_qunt_mmbtu[] = request.getParameterValues("nom_qunt_mmbtu_"+l);
			String nom_qunt_mt[] = request.getParameterValues("nom_qunt_mt_"+l);
			String filling_station[] = request.getParameterValues("filling_station_"+l);
			String sel_bay[] = request.getParameterValues("sel_bay_"+l);
			String sel_slot[] = request.getParameterValues("sel_slot_"+l);
			String arrival_dt[] = request.getParameterValues("arrival_dt_"+l);
			String arrival_time[] = request.getParameterValues("arrival_time_"+l);
			String next_avl_hrs[] = request.getParameterValues("next_avl_hrs_"+l);
			String truck_remark[] = request.getParameterValues("truck_remark_"+l);
			
			if(truck_cd!=null)
			{
				int truck_rev_no=-1;
				query2 = "SELECT NVL(MAX(NOM_REV_NO),'-1') "
						+ "FROM FMS_DLNG_SELLER_NOM_DTL "
						+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? "
						+ "AND CARGO_NO=? ";
				stmt2 = dbcon.prepareStatement(query2);
				stmt2.setString(1, cont);
				stmt2.setString(2, agmt);
				stmt2.setString(3, comp_cd);
				stmt2.setString(4, countpty_cd);
				stmt2.setString(5, plantSeq);
				stmt2.setString(6, cont_type);
				stmt2.setString(7, gas_dt);
				stmt2.setString(8, buPlantSeq);
				stmt2.setString(9, cargoNo);
				rset2 = stmt2.executeQuery();
				if(rset2.next())
				{
					truck_rev_no = rset2.getInt(1)+1;
				}
				else
				{
					truck_rev_no = truck_rev_no + 1;
				}
				rset2.close();
				stmt2.close();
				
				String dealNo=utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
				
				for(int j=0;j<truck_cd.length;j++)
				{
					if(!truck_cd[j].equals(""))
					{
						String slotStrTime=sel_slot[j].split("-")[0];
						String slotEndTime=sel_slot[j].split("-")[1];
						
						String truckNo=dlng_util.getTruckRegNo(dbcon, truck_cd[j]);
						
						boolean isNomAllow=true;
						double temp_qty=Double.parseDouble(nom_qunt_mmbtu[j]);
						if(!cont_type.equals("W") && !cont_type.equals("O") && !cont_type.equals("Q") && temp_qty > 0 && OA_COUNT == 0)
						{
							/*double prev_seller_qty=0;
							int seller_enrty_count=0;	
							query= "SELECT QTY_MMBTU, COUNT(*) "
									+ "FROM FMS_DLNG_SELLER_NOM_DTL A "
									+ "WHERE CONT_NO=? AND AGMT_NO=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND TRUCK_TRANS_CD=? AND TRUCK_CD=? AND FILL_STATION_CD=? AND BAY_CD=? AND SLOT_START_TIME=? AND SLOT_END_TIME=? "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) "
										+ "FROM FMS_DLNG_SELLER_NOM_DTL B "
										+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO AND B.COMPANY_CD=A.COMPANY_CD  "
										+ "AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE  "
										+ "AND B.BU_SEQ=A.BU_SEQ AND B.GAS_DT=A.GAS_DT) "
										+ "GROUP BY QTY_MMBTU";
							stmt = dbcon.prepareStatement(query);
							stmt.setString(1, cont);
							stmt.setString(2, agmt);
							stmt.setString(3, comp_cd);
							stmt.setString(4, countpty_cd);
							stmt.setString(5, plantSeq);
							stmt.setString(6, cont_type);
							stmt.setString(7, buPlantSeq);
							stmt.setString(8, gas_dt);
							stmt.setString(9, truck_trans_cd[j]);
							stmt.setString(10, truck_cd[j]);
							stmt.setString(11, filling_station[j]);
							stmt.setString(12, sel_bay[j]);
							stmt.setString(13, slotStrTime);
							stmt.setString(14, slotEndTime);
							rset = stmt.executeQuery();
							if(rset.next())
							{
								prev_seller_qty=rset.getDouble(1);
								seller_enrty_count=rset.getInt(2);
							}
							rset.close();
							stmt.close();
							
							double alloc_qty=0;
							int alloc_enrty_count=0;
							query= "SELECT QTY_MMBTU, COUNT(*) "
									+ "FROM FMS_DLNG_ALLOC_MST A "
									+ "WHERE CONT_NO=? AND AGMT_NO=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND TRUCK_TRANS_CD=? AND TRUCK_CD=? AND FILL_STATION_CD=? AND BAY_CD=? AND SLOT_START_TIME=? AND SLOT_END_TIME=? "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) "
										+ "FROM FMS_DLNG_ALLOC_MST B "
										+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO AND B.COMPANY_CD=A.COMPANY_CD  "
										+ "AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE  "
										+ "AND B.BU_SEQ=A.BU_SEQ AND B.GAS_DT=A.GAS_DT) "
										+ "GROUP BY QTY_MMBTU";
							stmt = dbcon.prepareStatement(query);
							stmt.setString(1, cont);
							stmt.setString(2, agmt);
							stmt.setString(3, comp_cd);
							stmt.setString(4, countpty_cd);
							stmt.setString(5, plantSeq);
							stmt.setString(6, cont_type);
							stmt.setString(7, buPlantSeq);
							stmt.setString(8, gas_dt);
							stmt.setString(9, truck_trans_cd[j]);
							stmt.setString(10, truck_cd[j]);
							stmt.setString(11, filling_station[j]);
							stmt.setString(12, sel_bay[j]);
							stmt.setString(13, slotStrTime);
							stmt.setString(14, slotEndTime);
							rset = stmt.executeQuery();
							if(rset.next())
							{
								alloc_qty=rset.getDouble(1);
								alloc_enrty_count=rset.getInt(2);
							}
							rset.close();
							stmt.close();
							*/
							
							double temp_gross_amt=temp_qty * Double.parseDouble(price);
							if(price_unit.equals("2"))
							{
								temp_gross_amt=temp_gross_amt * exchng_rate;
							}
							double temp_total_gross_amt=temp_gross_amt;
							Vector temp = new Vector();
							temp=TaxCalc.TaxAmountCalculationWithInfo(dbcon, comp_cd, countpty_cd, "C", plantSeq, buPlantSeq, gas_dt, nf.format(temp_total_gross_amt));
							double temp_tax_amt = Double.parseDouble(nf.format(Double.parseDouble(""+temp.elementAt(0))));
							double net_amt = temp_total_gross_amt+temp_tax_amt;
							
							double rem_amt=credit_amt-net_amt;
							//System.out.println("Allowable :: "+nf.format(credit_amt) +" :: Consumned Amt : "+nf.format(net_amt)+" :: rem : "+nf.format(credit_amt-net_amt));
							
							MSG_RPT_DT.add(gas_dt);
							MSG_DEAL_NO.add(dealNo);
							MSG_TRUCK_REG_NO.add(truckNo);
							MSG_QTY.add(nom_qunt_mmbtu[j]);
							MSG_ALLOW_CREDIT.add(nf.format(credit_amt));
							MSG_CONSUMED_AMT.add(nf.format(net_amt));
							MSG_BALANCE.add(nf.format(rem_amt));
							
							
							if(rem_amt < 0)
							{
								isNomAllow=false;
								MSG_SUBMITTED.add("E");
							}
							else
							{
								credit_amt = credit_amt-net_amt;
								MSG_SUBMITTED.add("S");
							}
						}
						else
						{
							/*MSG_RPT_DT.add(gas_dt);
							MSG_TRUCK_REG_NO.add(truckNo);
							MSG_QTY.add(nom_qunt_mmbtu[j]);
							MSG_ALLOW_CREDIT.add("");
							MSG_CONSUMED_AMT.add("");
							MSG_BALANCE.add("");
							MSG_SUBMITTED.add("S");*/
						}
						
						if(isNomAllow)
						{
							int insCnt=0;
							
							query1="INSERT INTO FMS_DLNG_SELLER_NOM_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,"
									+ "AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,"
									+ "TRUCK_TRANS_CD,TRUCK_CD,NOM_REV_NO,QTY_MMBTU,QTY_MT,"
									+ "FILL_STATION_CD,BAY_CD,SLOT_START_TIME,SLOT_END_TIME,ARRIVAL_DT,ARRIVAL_TIME,NEXT_AVAIL_HRS,"
									+ "GEN_DT,GEN_TIME,BASE,GCV,NCV,REMARK,ENT_BY,ENT_DT,CARGO_NO) "
									+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,?,"
									+ "TO_DATE(?,'DD/MM/YYYY'),?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,SYSDATE,?)";
							stmt1 = dbcon.prepareStatement(query1);
							stmt1.setString(++insCnt, comp_cd);
							stmt1.setString(++insCnt, countpty_cd);
							stmt1.setString(++insCnt, agmt);
							stmt1.setString(++insCnt, agmt_rev);
							stmt1.setString(++insCnt, cont);
							stmt1.setString(++insCnt, cont_rev);
							stmt1.setString(++insCnt, plantSeq);
							stmt1.setString(++insCnt, buPlantSeq);
							stmt1.setString(++insCnt, gas_dt);
							stmt1.setString(++insCnt, cont_type);
							stmt1.setString(++insCnt, truck_trans_cd[j]);
							stmt1.setString(++insCnt, truck_cd[j]);
							stmt1.setInt(++insCnt, truck_rev_no);
							stmt1.setString(++insCnt, nom_qunt_mmbtu[j]);
							stmt1.setString(++insCnt, nom_qunt_mt[j]);
							stmt1.setString(++insCnt, filling_station[j]);
							stmt1.setString(++insCnt, sel_bay[j]);
							stmt1.setString(++insCnt, slotStrTime);
							stmt1.setString(++insCnt, slotEndTime);
							stmt1.setString(++insCnt, arrival_dt[j]);
							stmt1.setString(++insCnt, arrival_time[j]);
							stmt1.setString(++insCnt, next_avl_hrs[j]);
							stmt1.setString(++insCnt, gen_dt);
							stmt1.setString(++insCnt, genTime);
							stmt1.setString(++insCnt, Base);
							stmt1.setString(++insCnt, GCV);
							stmt1.setString(++insCnt, NCV);
							stmt1.setString(++insCnt, truck_remark[j]);
							stmt1.setString(++insCnt, emp_cd);
							stmt1.setString(++insCnt, cargoNo);
							stmt1.executeQuery();
							
							stmt1.close();
						}
					}
				}
			}
			else
			{
				int truck_rev_no=-1;
				query2 = "SELECT NVL(MAX(NOM_REV_NO),'-1') "
						+ "FROM FMS_DLNG_SELLER_NOM_DTL "
						+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? AND CARGO_NO=? ";
				stmt2 = dbcon.prepareStatement(query2);
				stmt2.setString(1, cont);
				stmt2.setString(2, agmt);
				stmt2.setString(3, comp_cd);
				stmt2.setString(4, countpty_cd);
				stmt2.setString(5, plantSeq);
				stmt2.setString(6, cont_type);
				stmt2.setString(7, gas_dt);
				stmt2.setString(8, buPlantSeq);
				stmt2.setString(9, cargoNo);
				rset2 = stmt2.executeQuery();
				if(rset2.next())
				{
					truck_rev_no = rset2.getInt(1)+1;
				}
				else
				{
					truck_rev_no = truck_rev_no + 1;
				}
				rset2.close();
				stmt2.close();
				
				int insCnt=0;
				
				query1="INSERT INTO FMS_DLNG_SELLER_NOM_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,"
						+ "AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,"
						+ "TRUCK_TRANS_CD,TRUCK_CD,NOM_REV_NO,QTY_MMBTU,QTY_MT,"
						+ "FILL_STATION_CD,BAY_CD,SLOT_START_TIME,SLOT_END_TIME,ARRIVAL_DT,ARRIVAL_TIME,NEXT_AVAIL_HRS,"
						+ "GEN_DT,GEN_TIME,BASE,GCV,NCV,REMARK,ENT_BY,ENT_DT,CARGO_NO) "
						+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,?,"
						//+ "TO_DATE(?,'DD/MM/YYYY'),"
						+ "SYSDATE,"
						+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,SYSDATE,?)";
				stmt1 = dbcon.prepareStatement(query1);
				stmt1.setString(++insCnt, comp_cd);
				stmt1.setString(++insCnt, countpty_cd);
				stmt1.setString(++insCnt, agmt);
				stmt1.setString(++insCnt, agmt_rev);
				stmt1.setString(++insCnt, cont);
				stmt1.setString(++insCnt, cont_rev);
				stmt1.setString(++insCnt, plantSeq);
				stmt1.setString(++insCnt, buPlantSeq);
				stmt1.setString(++insCnt, gas_dt);
				stmt1.setString(++insCnt, cont_type);
				stmt1.setString(++insCnt, "0");
				stmt1.setString(++insCnt, "0");
				stmt1.setInt(++insCnt, truck_rev_no);
				stmt1.setString(++insCnt, "0");
				stmt1.setString(++insCnt, "0");
				stmt1.setString(++insCnt, "0");
				stmt1.setString(++insCnt, "0");
				stmt1.setString(++insCnt, "0");
				stmt1.setString(++insCnt, "0");
				//stmt1.setString(++insCnt, arrival_dt[j]);
				stmt1.setString(++insCnt, "0");
				stmt1.setString(++insCnt, "0");
				stmt1.setString(++insCnt, gen_dt);
				stmt1.setString(++insCnt, genTime);
				stmt1.setString(++insCnt, Base);
				stmt1.setString(++insCnt, GCV);
				stmt1.setString(++insCnt, NCV);
				stmt1.setString(++insCnt, "RELESE");
				stmt1.setString(++insCnt, emp_cd);
				stmt1.setString(++insCnt, cargoNo);
				stmt1.executeQuery();
				
				stmt1.close();
			}
			
//			String[] sub_seq_no = request.getParameterValues("sub_seq_no"+l);
//			String[] sub_ct_ref = request.getParameterValues("sub_ct_ref"+l);
//			String[] sub_utr_ref = request.getParameterValues("sub_utr_ref"+l);
//			String[] sub_qty_mmbtu = request.getParameterValues("sub_qty_mmbtu"+l);
//			String[] sub_qty_scm = request.getParameterValues("sub_qty_scm"+l);
//			String[] sub_sf_id = request.getParameterValues("sub_sf_id"+l);
//			
//		    if(sub_ct_ref!=null)
//			{
//				for(int j=0;j<sub_ct_ref.length;j++)
//				{
//					String seqNo="";
//					if(sub_seq_no[j].equals(""))
//					{
//						query = "SELECT NVL(MAX(SEQ_NO),?) "
//								+ "FROM FMS_DAILY_SELLER_NOM_DTL "
//								+ "WHERE CONT_NO=? AND AGMT_NO=? "
//								+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
//								+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
//								+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
//								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? AND CARGO_NO=? ";
//						stmt1 = dbcon.prepareStatement(query);
//						stmt1.setString(1, "0");
//						stmt1.setString(2, cont);
//						stmt1.setString(3, agmt);
//						stmt1.setString(4, comp_cd);
//						stmt1.setString(5, countpty_cd);
//						stmt1.setString(6, transCd);
//						stmt1.setString(7, truckCd);
//						stmt1.setString(8, plantSeq);
//						stmt1.setString(9, cont_type);
//						stmt1.setString(10, gas_dt);
//						stmt1.setString(11, buPlantSeq);
//						stmt1.setString(12, cargoNo);
//						rset1 = stmt1.executeQuery();
//						if(rset1.next())
//						{
//							seqNo = ""+(rset1.getInt(1)+1);
//						}
//						else
//						{
//							seqNo="1";
//						}
//						rset1.close();
//						stmt1.close();
//					}
//					else
//					{
//						seqNo=sub_seq_no[j];
//					}
//					
//					int sub_rev_no=-1;
//					query = "SELECT NVL(MAX(NOM_REV_NO),?) "
//							+ "FROM FMS_DAILY_SELLER_NOM_DTL "
//							+ "WHERE CONT_NO=? AND AGMT_NO=? "
//							+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
//							+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
//							+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
//							+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? AND CARGO_NO=? "
//							+ "AND SEQ_NO=?";
//					stmt1 = dbcon.prepareStatement(query);
//					stmt1.setString(1, "-1");
//					stmt1.setString(2, cont);
//					stmt1.setString(3, agmt);
//					stmt1.setString(4, comp_cd);
//					stmt1.setString(5, countpty_cd);
//					stmt1.setString(6, transCd);
//					stmt1.setString(7, truckCd);
//					stmt1.setString(8, plantSeq);
//					stmt1.setString(9, cont_type);
//					stmt1.setString(10, gas_dt);
//					stmt1.setString(11, buPlantSeq);
//					stmt1.setString(12, cargoNo);
//					stmt1.setString(13, seqNo);
//					rset1 = stmt1.executeQuery();
//					if(rset1.next())
//					{
//						sub_rev_no = rset1.getInt(1)+1;
//					}
//					else
//					{
//						sub_rev_no = rev_no + 1;
//					}
//					rset1.close();
//					stmt1.close();
//					
//					query="INSERT INTO FMS_DAILY_SELLER_NOM_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
//							+ "TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
//							+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,BU_SEQ,SEQ_NO,CT_REF,UTR_NO,CARGO_NO,SF_ID) "
//							+ "VALUES(?,?,?,?,?,?,?,?,"
//							+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
//							+ "?,?,?,?,?,SYSDATE,?,?,?,?,?,?)";
//					stmt2 = dbcon.prepareStatement(query);
//					stmt2.setString(1, comp_cd);
//					stmt2.setString(2, countpty_cd);
//					stmt2.setString(3, agmt);
//					stmt2.setString(4, agmt_rev);
//					stmt2.setString(5, cont);
//					stmt2.setString(6, cont_rev);
//					stmt2.setInt(7, sub_rev_no);
//					stmt2.setString(8, plantSeq);
//					stmt2.setString(9, transCd);
//					stmt2.setString(10, truckCd);
//					stmt2.setString(11, gas_dt);
//					stmt2.setString(12, cont_type);
//					stmt2.setString(13, gen_dt);
//					stmt2.setString(14, genTime);
//					stmt2.setString(15, Base);
//					stmt2.setString(16, GCV);
//					stmt2.setString(17, NCV);
//					stmt2.setString(18, sub_qty_mmbtu[j]);
//					stmt2.setString(19, sub_qty_scm[j]);
//					stmt2.setString(20, emp_cd);
//					stmt2.setString(21, buPlantSeq);
//					stmt2.setString(22, seqNo);
//					stmt2.setString(23, sub_ct_ref[j]);
//					stmt2.setString(24, sub_utr_ref[j]);
//					stmt2.setString(25, cargoNo);
//					stmt2.setString(26, sub_sf_id[j]);
//					stmt2.executeQuery();
//					
//					stmt2.close();
//					
//					if(!sub_sf_id[j].equals(""))
//					{
//						Element SCHEDULE = doc.createElement("SCHEDULE");
//					    sellerNomination.appendChild(SCHEDULE);
//						
//						Element NOMINATION_SF_ID = doc.createElement("NOMINATION_SF_ID");
//					    Element SCH_GAS_DAY = doc.createElement("SCH_GAS_DAY");
//					    Element SCH_REV_NO = doc.createElement("SCH_REV_NO");
//					    Element SCH_GEN_DAY = doc.createElement("SCH_GEN_DAY");
//					    Element SCH_GEN_TIME = doc.createElement("SCH_GEN_TIME");
//					    Element SCH_QTY = doc.createElement("SCH_QTY");
//					    Element SCH_UNIT = doc.createElement("SCH_UNIT");
//					    Element CT_CONTRACT_ID = doc.createElement("CT_CONTRACT_ID");
//					    
//						SCHEDULE.appendChild(NOMINATION_SF_ID);
//					    SCHEDULE.appendChild(SCH_GAS_DAY);
//					    SCHEDULE.appendChild(SCH_REV_NO);
//					    SCHEDULE.appendChild(SCH_GEN_DAY);
//					    SCHEDULE.appendChild(SCH_GEN_TIME);
//					    SCHEDULE.appendChild(SCH_QTY);
//					    SCHEDULE.appendChild(SCH_UNIT);
//					    SCHEDULE.appendChild(CT_CONTRACT_ID);
//					    
//					    NOMINATION_SF_ID.appendChild(doc.createTextNode(sub_sf_id[j]));
//					    SCH_GAS_DAY.appendChild(doc.createTextNode(gas_dt));
//					    SCH_REV_NO.appendChild(doc.createTextNode(""+sub_rev_no));
//					    SCH_GEN_DAY.appendChild(doc.createTextNode(""+gen_dt));
//					    SCH_GEN_TIME.appendChild(doc.createTextNode(""+genTime));
//					    SCH_QTY.appendChild(doc.createTextNode(""+sub_qty_mmbtu[j]));
//					    SCH_UNIT.appendChild(doc.createTextNode("MMBTU"));
//					    CT_CONTRACT_ID.appendChild(doc.createTextNode(sub_ct_ref[j]));
//					    
//					    block_count++;
//					}
//				}
//			}
//			else //IF NO CT CONFIGURED THEN DEFUALT ENTRY WILL BE SUBMITTED IN DTL TABLE
//			{
//				String seqNo="1";
//				
//				int sub_rev_no=-1;
//				query = "SELECT NVL(MAX(NOM_REV_NO),?) "
//						+ "FROM FMS_DAILY_SELLER_NOM_DTL "
//						+ "WHERE CONT_NO=? AND AGMT_NO=? "
//						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
//						+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
//						+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
//						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? "
//						+ "AND SEQ_NO=? AND CARGO_NO=? ";
//				stmt1 = dbcon.prepareStatement(query);
//				stmt1.setString(1, "-1");
//				stmt1.setString(2, cont);
//				stmt1.setString(3, agmt);
//				stmt1.setString(4, comp_cd);
//				stmt1.setString(5, countpty_cd);
//				stmt1.setString(6, transCd);
//				stmt1.setString(7, truckCd);
//				stmt1.setString(8, plantSeq);
//				stmt1.setString(9, cont_type);
//				stmt1.setString(10, gas_dt);
//				stmt1.setString(11, buPlantSeq);
//				stmt1.setString(12, seqNo);
//				stmt1.setString(13, cargoNo);
//				rset1 = stmt1.executeQuery();
//				if(rset1.next())
//				{
//					sub_rev_no = rset1.getInt(1)+1;
//				}
//				else
//				{
//					sub_rev_no = rev_no + 1;
//				}
//				rset1.close();
//				stmt1.close();
//				
//				query="INSERT INTO FMS_DAILY_SELLER_NOM_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
//						+ "TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
//						+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,BU_SEQ,SEQ_NO,CT_REF,UTR_NO,CARGO_NO,SF_ID) "
//						+ "VALUES(?,?,?,?,?,?,?,?,"
//						+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
//						+ "?,?,?,?,?,SYSDATE,?,?,?,?,?,?)";
//				stmt2 = dbcon.prepareStatement(query);
//				stmt2.setString(1, comp_cd);
//				stmt2.setString(2, countpty_cd);
//				stmt2.setString(3, agmt);
//				stmt2.setString(4, agmt_rev);
//				stmt2.setString(5, cont);
//				stmt2.setString(6, cont_rev);
//				stmt2.setInt(7, sub_rev_no);
//				stmt2.setString(8, plantSeq);
//				stmt2.setString(9, transCd);
//				stmt2.setString(10, truckCd);
//				stmt2.setString(11, gas_dt);
//				stmt2.setString(12, cont_type);
//				stmt2.setString(13, gen_dt);
//				stmt2.setString(14, genTime);
//				stmt2.setString(15, Base);
//				stmt2.setString(16, GCV);
//				stmt2.setString(17, NCV);
//				stmt2.setString(18, qtyMMBTU);
//				stmt2.setString(19, qtySCM);
//				stmt2.setString(20, emp_cd);
//				stmt2.setString(21, buPlantSeq);
//				stmt2.setString(22, seqNo);
//				stmt2.setString(23, "");
//				stmt2.setString(24, "");
//				stmt2.setString(25, cargoNo);
//				stmt2.setString(26, sf_id);
//				stmt2.executeQuery();
//				
//				stmt2.close();
//				
//				if(!sf_id.equals(""))
//				{
//					Element SCHEDULE = doc.createElement("SCHEDULE");
//				    sellerNomination.appendChild(SCHEDULE);
//				    
//					Element NOMINATION_SF_ID = doc.createElement("NOMINATION_SF_ID");
//				    Element SCH_GAS_DAY = doc.createElement("SCH_GAS_DAY");
//				    Element SCH_REV_NO = doc.createElement("SCH_REV_NO");
//				    Element SCH_GEN_DAY = doc.createElement("SCH_GEN_DAY");
//				    Element SCH_GEN_TIME = doc.createElement("SCH_GEN_TIME");
//				    Element SCH_QTY = doc.createElement("SCH_QTY");
//				    Element SCH_UNIT = doc.createElement("SCH_UNIT");
//				    Element CT_CONTRACT_ID = doc.createElement("CT_CONTRACT_ID");
//				    
//					SCHEDULE.appendChild(NOMINATION_SF_ID);
//				    SCHEDULE.appendChild(SCH_GAS_DAY);
//				    SCHEDULE.appendChild(SCH_REV_NO);
//				    SCHEDULE.appendChild(SCH_GEN_DAY);
//				    SCHEDULE.appendChild(SCH_GEN_TIME);
//				    SCHEDULE.appendChild(SCH_QTY);
//				    SCHEDULE.appendChild(SCH_UNIT);
//				    SCHEDULE.appendChild(CT_CONTRACT_ID);
//				    
//				    NOMINATION_SF_ID.appendChild(doc.createTextNode(sf_id));
//				    SCH_GAS_DAY.appendChild(doc.createTextNode(gas_dt));
//				    SCH_REV_NO.appendChild(doc.createTextNode(""+sub_rev_no));
//				    SCH_GEN_DAY.appendChild(doc.createTextNode(""+gen_dt));
//				    SCH_GEN_TIME.appendChild(doc.createTextNode(""+genTime));
//				    SCH_QTY.appendChild(doc.createTextNode(qtyMMBTU));
//				    SCH_UNIT.appendChild(doc.createTextNode("MMBTU"));
//				    CT_CONTRACT_ID.appendChild(doc.createTextNode(""));
//				    
//				    block_count++;
//				}
//			}
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
			String submit_flag = request.getParameter("submit_flag")==null?"":request.getParameter("submit_flag");
			
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
			String[] qty_mt = request.getParameterValues("qty_mt");
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
					String qtyMT=qty_mt[i];
					String qtySCM=qty_scm[i];
					String l=index[i];
					String sub_size=index1[i];
					String weekof_gas_dt=week_gas_dt[i];
					String SfId=sf_id[i];
					
					BuyerNomination(request,weekof_gas_dt,genDt,counterparty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,
							plantSeq,contract_type,buPlantSeq,genTime,Base,GCV,NCV,qtyMMBTU,qtyMT,qtySCM,l,SfId,submit_flag,cargo_no);
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
			
			url = "../dlng/frm_dlng_buyer_periodic_nom.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+
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
		
		MSG_RPT_DT.clear();
		MSG_DEAL_NO.clear();
		MSG_TRUCK_REG_NO.clear();
		MSG_QTY.clear();
		MSG_ALLOW_CREDIT.clear();
		MSG_CONSUMED_AMT.clear();
		MSG_BALANCE.clear();
		MSG_SUBMITTED.clear();
		
		try
		{
			HttpSession session = request.getSession();
			
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
			String[] qty_mt = request.getParameterValues("qty_mt");
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
					String qtyMT=qty_mt[i];
					String l=index[i];
					String sub_size=index1[i];
					String weekof_gas_dt=week_gas_dt[i];
					String SfId=sf_id[i];
					
					String price="";
					String price_unit="";
					double credit=0;
					double exchng_rate=0;
					String cont_price="";
					
					int OA_COUNT=0;
					query2 ="SELECT COUNT(*) "
							+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.GX=? "
							+ "AND B.AGMT_NO=? AND B.CONT_NO=? AND B.CONTRACT_TYPE=? "
							+ "AND SEC_TYPE IN ('OA') "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.SEQ_NO=B.SEQ_NO "
							+ "AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, "K");
					stmt2.setString(4, agmt_no);
					stmt2.setString(5, cont_no);
					stmt2.setString(6, contract_type);
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						OA_COUNT=rset2.getInt(1);
					}
					rset2.close();
					stmt2.close();
					
					if(!contract_type.equals("W") && OA_COUNT==0)
					{
						String queryString="SELECT TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),DCQ,RATE,RATE_UNIT,"
								+ "CONT_REF_NO,TRADE_REF_NO "
								+ "FROM FMS_SUPPLY_CONT_MST A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
								+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
								+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "; 
						stmt=dbcon.prepareStatement(queryString);
						stmt.setString(1, comp_cd);
						stmt.setString(2, counterparty_cd);
						stmt.setString(3, agmt_no);
						stmt.setString(4, cont_no);
						stmt.setString(5, contract_type);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							//contract_start_dt=rset.getString(1)==null?"":rset.getString(1);
							//contract_end_dt=rset.getString(2)==null?"":rset.getString(2);
							//dcq=rset.getString(3)==null?"":nf.format(rset.getDouble(3));
							price_unit=rset.getString(5)==null?"":rset.getString(5);
							price=rset.getString(4)==null?"":utilBean.RateNumberFormat(rset.getDouble(4), price_unit);
						}
						rset.close();
						stmt.close();
						
						credit=utilBean.getAllowableCreditAmount(dbcon, comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, "K", weekof_gas_dt,plantSeq,buPlantSeq);
						exchng_rate=utilBean.getContExchangeRate(dbcon,comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, weekof_gas_dt);
						String queryString1 = "SELECT DISTINCT NEW_SALE_PRICE "
								+ "FROM FMS_SUPPLY_ALLOC_REVISED A "
								+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND CONT_NO=? "
								+ "AND COUNTERPARTY_CD=? AND FLAG=? AND CONTRACT_TYPE=? "
								+ "AND MODIFICATION_SEQ_NO = (SELECT MAX(MODIFICATION_SEQ_NO) FROM FMS_SUPPLY_ALLOC_REVISED B "
								+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.FLAG=B.FLAG AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
								+ "AND B.NEW_PRICE_EFF_DT <=TO_DATE(?,'DD/MM/YYYY'))";
						stmt1=dbcon.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, agmt_no);
						stmt1.setString(3, cont_no);
						stmt1.setString(4, counterparty_cd);
						stmt1.setString(5, "A");
						stmt1.setString(6, contract_type);
						stmt1.setString(7, gas_dt);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							cont_price=rset1.getString(1)==null?"":utilBean.RateNumberFormat(rset1.getDouble(1), price_unit);
						}
						rset1.close();
						stmt1.close();
						
						if(cont_price.equals(""))
						{
							cont_price=price;
						}
					}
						
					SellerNomination(request,weekof_gas_dt,genDt,counterparty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,
							plantSeq,contract_type,cargo_no,buPlantSeq,genTime,Base,GCV,NCV,qtyMMBTU,qtySCM,l,sub_size,SfId,sellerNomination,doc,qtyMT,
							credit,exchng_rate,cont_price,price_unit,OA_COUNT);
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
			
			if(MSG_RPT_DT.size() > 0)
			{
				session.setAttribute("MSG_TRUCK_REG_NO", MSG_TRUCK_REG_NO);
				session.setAttribute("MSG_DEAL_NO", MSG_DEAL_NO);
				session.setAttribute("MSG_QTY", MSG_QTY);
				session.setAttribute("MSG_ALLOW_CREDIT", MSG_ALLOW_CREDIT);
				session.setAttribute("MSG_CONSUMED_AMT", MSG_CONSUMED_AMT);
				session.setAttribute("MSG_BALANCE", MSG_BALANCE);
				session.setAttribute("MSG_SUBMITTED", MSG_SUBMITTED);
				session.setAttribute("MSG_RPT_DT", MSG_RPT_DT);
			}
			
			url = "../dlng/frm_dlng_seller_periodic_nom.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+
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
			String[] plant_seq=request.getParameterValues("plant_seq");
			String[] bu_plant_seq=request.getParameterValues("bu_plant_seq");
			
			String[] gen_time = request.getParameterValues("gen_time");
			String[] base = request.getParameterValues("base");
			String[] gcv = request.getParameterValues("gcv");
			String[] ncv = request.getParameterValues("ncv");
			
			String load_start_dt[] = request.getParameterValues("load_start_dt");
			String load_start_time[] = request.getParameterValues("load_start_time");
			String load_end_dt[] = request.getParameterValues("load_end_dt");
			String load_end_time[] = request.getParameterValues("load_end_time");
			
			String truck_trans_cd[] = request.getParameterValues("truck_trans_cd");
			String truck_cd[] = request.getParameterValues("truck_cd");
			String[] qty_mmbtu = request.getParameterValues("qty_mmbtu");
			String[] qty_mt = request.getParameterValues("qty_mt");
			String[] gcv_mmbtu = request.getParameterValues("gcv_mmbtu");

			String[] filling_station = request.getParameterValues("filling_station");
			String[] bay_cd = request.getParameterValues("bay_cd");
			String[] slot_start_time = request.getParameterValues("slot_start_time");
			String[] slot_end_time = request.getParameterValues("slot_end_time");
			String[] next_avail_hrs = request.getParameterValues("next_avail_hrs");

			String[] cargo_no = request.getParameterValues("cargo_no");
			
			String[] index = request.getParameterValues("index"); //INDEX of l
			
			if(counterparty_cd != null)
			{
				for(int i=0; i<counterparty_cd.length; i++)
				{
					String countpty_cd=counterparty_cd[i];
					String agmt=agmt_no[i];
					String agmt_rev=agmt_rev_no[i];
					String cont=cont_no[i];
					String cont_rev=cont_rev_no[i];
					String plantSeq=plant_seq[i];
					String cont_type=contract_type[i];
					String buPlantSeq=bu_plant_seq[i];
					String genTime=gen_time[i];
					String Base=base[i];
					String GCV=gcv[i];
					String NCV=ncv[i];
					String cargoNo=cargo_no[i];
					
					String fillSt=filling_station[i];
					String bayCd=bay_cd[i];
					String slotStTime=slot_start_time[i];
					String slotEndTime=slot_end_time[i];
					String nextAvailHrs=next_avail_hrs[i];
					
					int truck_rev_no=-1;
					query2 = "SELECT NVL(MAX(NOM_REV_NO),'-1') "
							+ "FROM FMS_DLNG_ALLOC_MST "
							+ "WHERE CONT_NO=? AND AGMT_NO=? "
							+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
							+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? "
							+ "AND TRUCK_TRANS_CD=? AND TRUCK_CD=? "
							+ "AND FILL_STATION_CD=? AND BAY_CD=? "
							+ "AND SLOT_START_TIME=? AND SLOT_END_TIME=? "
							+ "AND CARGO_NO=? ";
					stmt2 = dbcon.prepareStatement(query2);
					stmt2.setString(1, cont);
					stmt2.setString(2, agmt);
					stmt2.setString(3, comp_cd);
					stmt2.setString(4, countpty_cd);
					stmt2.setString(5, plantSeq);
					stmt2.setString(6, cont_type);
					stmt2.setString(7, gas_dt);
					stmt2.setString(8, buPlantSeq);
					stmt2.setString(9, truck_trans_cd[i]);
					stmt2.setString(10, truck_cd[i]);
					stmt2.setString(11, filling_station[i]);
					stmt2.setString(12, bay_cd[i]);
					stmt2.setString(13, slot_start_time[i]);
					stmt2.setString(14, slot_end_time[i]);
					stmt2.setString(15, cargoNo);
					rset2 = stmt2.executeQuery();
					if(rset2.next())
					{
						truck_rev_no = rset2.getInt(1)+1;
					}
					else
					{
						truck_rev_no = truck_rev_no + 1;
					}
					rset2.close();
					stmt2.close();

					int insCnt=0;
					query1="INSERT INTO FMS_DLNG_ALLOC_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,"
							+ "AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,"
							+ "TRUCK_TRANS_CD,TRUCK_CD,NOM_REV_NO,QTY_MMBTU,QTY_MT,"
							+ "LOAD_START_DT,LOAD_START_TIME,LOAD_END_DT,LOAD_END_TIME,"
							+ "GEN_DT,GEN_TIME,BASE,GCV,NCV,ENT_BY,ENT_DT,GCV_MMBTU,"
							+ "FILL_STATION_CD,BAY_CD,SLOT_START_TIME,SLOT_END_TIME,NEXT_AVAIL_HRS,CARGO_NO) "
							+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,"
							+ "TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,"
							+ "TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,SYSDATE,?,?,?,?,?,?,?)";
					stmt1 = dbcon.prepareStatement(query1);
					stmt1.setString(++insCnt, comp_cd);
					stmt1.setString(++insCnt, countpty_cd);
					stmt1.setString(++insCnt, agmt);
					stmt1.setString(++insCnt, agmt_rev);
					stmt1.setString(++insCnt, cont);
					stmt1.setString(++insCnt, cont_rev);
					stmt1.setString(++insCnt, plantSeq);
					stmt1.setString(++insCnt, buPlantSeq);
					stmt1.setString(++insCnt, gas_dt);
					stmt1.setString(++insCnt, cont_type);
					stmt1.setString(++insCnt, truck_trans_cd[i]);
					stmt1.setString(++insCnt, truck_cd[i]);
					stmt1.setInt(++insCnt, truck_rev_no);
					stmt1.setString(++insCnt, qty_mmbtu[i]);
					stmt1.setString(++insCnt, qty_mt[i]);
					stmt1.setString(++insCnt, load_start_dt[i]);
					stmt1.setString(++insCnt, load_start_time[i]);
					stmt1.setString(++insCnt, load_end_dt[i]);
					stmt1.setString(++insCnt, load_end_time[i]);
					stmt1.setString(++insCnt, gen_dt);
					stmt1.setString(++insCnt, genTime);
					stmt1.setString(++insCnt, Base);
					stmt1.setString(++insCnt, GCV);
					stmt1.setString(++insCnt, NCV);
					stmt1.setString(++insCnt, emp_cd);
					stmt1.setString(++insCnt, gcv_mmbtu[i]);
					stmt1.setString(++insCnt, fillSt);
					stmt1.setString(++insCnt, bayCd);
					stmt1.setString(++insCnt, slotStTime);
					stmt1.setString(++insCnt, slotEndTime);
					stmt1.setString(++insCnt, nextAvailHrs);
					stmt1.setString(++insCnt, cargoNo);
					stmt1.executeQuery();
					
					stmt1.close();
				}
			}
			
			msg = "Successful! - Daily Allocation Submitted Successfully!";
			msg_type="S";
			
			url = "../dlng/frm_dlng_daily_allocation.jsp?msg="+msg+"&msg_type="+msg_type+"&gas_dt="+gas_dt+commonUrl_pra;
			
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
			String truck_cd = request.getParameter("truck_cd")==null?"":request.getParameter("truck_cd");
			String plant_seq = request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
			String bu_plant_seq = request.getParameter("bu_plant_seq")==null?"":request.getParameter("bu_plant_seq");
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
					
					if(mail_file_nm.equals("frm_dlng_seller_nom_to_cust_send_mail.jsp"))
					{
						query="UPDATE FMS_DAILY_SELLER_NOM_REMARK SET MODIFIY_BY=?,MODIFIY_DT=SYSDATE,EMAIL_SENT='Y' "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND CONT_NO=? "
								+ "AND CONTRACT_TYPE=? AND PLANT_SEQ=? "
								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY')";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, emp_cd);
						stmt1.setString(2, comp_cd);
						stmt1.setString(3, counterparty_cd);
						stmt1.setString(4, agmt_no);
						stmt1.setString(5, cont_no);
						stmt1.setString(6, contract_type);
						stmt1.setString(7, plant_seq);
						stmt1.setString(8, gas_dt);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					else if(mail_file_nm.equals("frm_dlng_seller_nom_to_trans_send_mail.jsp"))
					{
						query="UPDATE FMS_DLNG_SELLER_NOM_TRANS SET MODIFIY_BY=?,MODIFIY_DT=SYSDATE,EMAIL_SENT='Y' "
								+ "WHERE COMPANY_CD=? AND TRUCK_TRANS_CD=? "
								//+ "AND TRUCK_CD=? "
								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY')";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, emp_cd);
						stmt1.setString(2, comp_cd);
						stmt1.setString(3, counterparty_cd);
						//stmt1.setString(4, truck_cd);
						stmt1.setString(4, gas_dt);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					else if(mail_file_nm.equals("frm_dlng_send_mail.jsp"))
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
					/*else if(mail_file_nm.equals("frm_alloc_to_trans_send_mail.jsp"))
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
					}*/
						
					msg="Mail Sent for "+subject[i];
					msg_type="S";
				}
			}
			
			if(!mail_file_nm.equals(""))
			{
				url = "../dlng/"+mail_file_nm+"?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			}
			else
			{
				url = "../dlng/frm_send_mail.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
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
			String cargo_no = request.getParameter("cargo_no")==null?"0":request.getParameter("cargo_no");
			String addressed_to = request.getParameter("addressed_to")==null?"":request.getParameter("addressed_to");
			
			//AP20250602 Used Same table as Daily seller nomination to customer (Sales Side).
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
			
			url = "../dlng/rpt_dlng_seller_nomination_to_customer.jsp?msg="+msg+"&msg_type="+msg_type+"&gas_dt="+gas_dt+"&contract_type="+mst_contract_type+commonUrl_pra;
			
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
