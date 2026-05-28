package automation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.servlet.http.HttpServletRequest;

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.SystemErrorLogger;
import java.util.Vector;

//Code By:	Pratham Bhatt
//Date: 	28/01/2025

public class Auto_EnergyBank_Emails
{
	public static void main(String [] args)
	{
		Auto_Energy_Bank_Reports autoEBReport = new Auto_Energy_Bank_Reports();
		autoEBReport.init();
	}
}

class Auto_Energy_Bank_Reports
{
	String db_src_file_name="Auto_Energy_Bank_Reports.java";
	Connection conn;
	PreparedStatement stmt,stmt1,stmt2,stmt3,stmt4,stmt5,stmt6,stmt13,stmt14,stmt15,stmt16,stmt_tmp,stmt_tmp1,stmt_tmp2,stmt_tmp3,stmt_temp,stmt_temp1;
	ResultSet rset,rset1,rset2,rset3,rset4,rset5,rset6,rset13,rset14,rset15,rset16,rset_tmp,rset_tmp1,rset_tmp2,rset_tmp3,rset_temp,rset_temp1;
	String queryString="",queryString1="",queryString2="",queryString3="";
	String queryString4="",queryString5="",queryString6="",queryString7="";
	String queryString8="",queryString9="",queryString10="",queryString11="",queryString12="",queryString13="",queryString14="",queryString15="",queryString16="";
	String queryString_temp="";
	String queryString_temp1="";
	String queryString_temp2="";
	String context="";
	
	HttpServletRequest request;
	HttpServletRequest session;

	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");

	Auto_UtilBean utilBean = new Auto_UtilBean();
	DateUtil utilDate = new DateUtil();
	Auto_MailDelivery mailDelv = new Auto_MailDelivery();
	
	public void init()
	{
		String function_nm="init()";
		try
		{
			conn=new Auto_DB_Connection().db_conn();
			if(conn != null)  
			{
				context=utilBean.getAutomationKeyDetail(conn, "ENV_CONTEXT");
				
				today_date = utilDate.getSysdate();
				
				//for selecting multiple companies
				String query = "SELECT COMPANY_CD FROM FMS_COMPANY_OWNER_MST WHERE EFF_DT<SYSDATE AND STATUS=?";
				stmt=conn.prepareStatement(query);
				stmt.setString(1, "Y");
				rset=stmt.executeQuery();
				while(rset.next())
				{
					VCOMP_CD.add(rset.getString(1)==null?"":rset.getString(1));
				}
				rset.close();
				stmt.close();
				
				for(int i=0;i<VCOMP_CD.size();i++)
				{
					//Initializing company Code 
					comp_cd = VCOMP_CD.elementAt(i).toString();
					comp_abbr = utilBean.getCompanyAbbr(conn,comp_cd);
					
					//Fetching the recipient List for selected companies
					to_mail = utilBean.getToMailReceipentList(conn,comp_cd,"Energy Bank","Inventory","Daily","Auto"); 	
					System.out.println("To email is: "+to_mail);
					
					if(!to_mail.equals(""))
					{
						//Daily: Price Modification Request Email
						sendPriceModificationDtl();
						doClear();
						
						//Daily: Short Transfer Mail 
						sendShortTransfer();
						doClear();
						
						System.out.println("--------------------All functions executed for company: "+comp_abbr+"--------------------");
					}
					else
					{
						System.out.println("--------------------NO Recipient Configured for Inventory Energy Bank Module, for "+comp_abbr+" Profile!--------------------");
					}
				}
				System.out.println("Program Terminated !");
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
				if(rset != null){try{rset.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset1 != null){try{rset1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset2 != null){try{rset2.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset3 != null){try{rset3.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset4 != null){try{rset4.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset5 != null){try{rset5.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset6 != null){try{rset6.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset13 != null){try{rset13.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset14 != null){try{rset14.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset15 != null){try{rset15.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset16 != null){try{rset16.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset_tmp != null){try{rset_tmp.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset_tmp1 != null){try{rset_tmp1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset_tmp2 != null){try{rset_tmp2.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset_tmp3 != null){try{rset_tmp3.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset_temp != null){try{rset_temp.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset_temp1 != null){try{rset_temp1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt != null){try{stmt.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt4!= null){try{stmt4.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt6!= null){try{stmt6.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt13!= null){try{stmt13.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt14!= null){try{stmt14.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt15!= null){try{stmt15.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt16!= null){try{stmt16.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt_tmp!= null){try{stmt_tmp.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt_tmp1!= null){try{stmt_tmp1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt_tmp2!= null){try{stmt_tmp2.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt_tmp3!= null){try{stmt_tmp3.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt_temp!= null){try{stmt_temp.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt_temp1!= null){try{stmt_temp1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(conn != null){try{conn.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			}
			catch(Exception e)
			{
				new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			}
		}
	}
	
	//Auto mail for price modification 
	public void sendPriceModificationDtl()
	{
		String function_nm="sendPriceModificationDtl()";
		try
		{
			int count=0;
			
			//fetching the pending price modification details 
			queryString =" SELECT A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,   "
					+ "A.CONTRACT_TYPE,A.PRICE_UNIT,C.RATE,A.NEW_PRICE,A.FLAG,TO_CHAR(A.EFF_DT,'DD/MM/YYYY')   "
					+ "FROM FMS_TRADER_CONT_PRICE_DTL A, FMS_TRADER_CONT_MST C    "
					+ "WHERE A.COMPANY_CD=? AND A.FLAG=?   "
					+ "AND A.SEQ_NO = (SELECT MAX(B.SEQ_NO) FROM FMS_TRADER_CONT_PRICE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD    "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)  "
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.AGMT_NO = C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONT_NO=C.CONT_NO AND A.CONT_REV=C.CONT_REV "
					+ "AND A.CONTRACT_TYPE=C.CONTRACT_TYPE";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,comp_cd);
			stmt.setString(2,"R");
			rset = stmt.executeQuery();
			while(rset.next())
			{
				count+=1;
				String counterparty_cd = rset.getString(1)==null?"":rset.getString(1);
				String agmt_no = rset.getString(2)==null?"":rset.getString(2);
				String agmt_rev = rset.getString(3)==null?"":rset.getString(3);
				String cont_no = rset.getString(4)==null?"":rset.getString(4);
				String cont_rev = rset.getString(5)==null?"":rset.getString(5);
				String cont_type = rset.getString(6)==null?"":rset.getString(6);
				String price_unit = rset.getString(7)==null?"":rset.getString(7);
				String old_price = rset.getString(8)==null?"":rset.getString(8);
				String new_price = rset.getString(9)==null?"":rset.getString(9);
				String flag = rset.getString(10)==null?"":rset.getString(10);
				String eff_dt = rset.getString(11)==null?"":rset.getString(11);
				
				String map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, cont_type, "0"); //cargo on is kept 0: might needs to change 
				
				String status="";
				if(flag.equals("R"))
				{
					status = "Requested";
				}
				
				VCOUNTERPARTY_CD.add(counterparty_cd);
				VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn, counterparty_cd));
				VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn, counterparty_cd));
				VAGMT_NO.add(agmt_no);
				VAGMT_REV.add(agmt_rev);
				VCONT_NO.add(cont_no);
				VCONT_REV.add(cont_rev);
				VCONT_TYPE.add(cont_type);
				VDEAL_MAP_ID.add(map);
				VPRICE_UNIT.add(price_unit);
				VPRICE_UNIT_NM.add(""+utilBean.getRateUnitNm(conn,price_unit));
				VOLD_PRICE.add(old_price);
				VNEW_PRICE.add(new_price);
				VSTATUS.add(status);
				VEFF_DT.add(eff_dt);
			}
			rset.close();
			stmt.close();
			
			//for generating the mail.
			String subject=comp_abbr+" "+CommonVariable.app_name_sub+" "+context+": Cargo Price Modification: ("+count+") Approval Request";
			String mailBody="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";
			mailBody+="Please find the following cargo price modification request for "+today_date+".";
			mailBody+="<br><br>";
			mailBody+="<table style=\"border:1px solid black;border-collapse:collapse;\" border=\"1\">";
			mailBody+="<thead style=\"font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";\">";
			mailBody+="<tr>"
						+ "<th>Purchase Contract#</th>"
						+ "<th>Current Price</th>"
						+ "<th>Requested Price</th>"
						+ "<th>Currency/MMBTU</th>"
						+ "<th>Eff Date</th>"
						+ "<th>Status</th>"
					+ "</tr>";
			mailBody+="</thead>";
			mailBody+="<tbody style=\"font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";\">";
			for(int i=0; i<VCOUNTERPARTY_CD.size();i++)
			{
				mailBody+="<tr>"
							+ "<td align=\"center\">"+VDEAL_MAP_ID.elementAt(i)+"</td>"
							+ "<td align=\"center\">"+VOLD_PRICE.elementAt(i)+"</td>"
							+ "<td align=\"center\">"+VNEW_PRICE.elementAt(i)+"</td>"
							+ "<td align=\"center\">"+VPRICE_UNIT_NM.elementAt(i)+"</td>"
							+ "<td align=\"center\">"+VEFF_DT.elementAt(i)+"</td>"
							+ "<td align=\"center\">"+VSTATUS.elementAt(i)+"</td>"
						+ "</tr>";
			}
			mailBody+="</tbody>";
			mailBody+="</table>";
			mailBody+="</span>";
			mailBody+="</html>";
			mailBody+=CommonVariable.mail_signature;
			mailBody+=CommonVariable.mail_disclaimer;
			
			String to_mail_list = utilBean.getToMailReceipentList(conn,comp_cd,"Energy Bank","Inventory","Daily","Auto");
			String cc_mail_list= utilBean.getCcMailReceipentList(conn,comp_cd,"Energy Bank","Inventory","Daily","Auto");

			if(!to_mail_list.equals("") && !mailBody.equals("") && VCOUNTERPARTY_CD.size()>0)
			{
				boolean mailRes = mailDelv.sendMail(conn,to_mail_list, subject, mailBody,"",cc_mail_list,"");
				if(mailRes)
				{
					System.out.println("SUCCESS: Cargo Price Request Approval mail send successfully for "+comp_abbr);
				}
				else
				{
					System.out.println("FAILURE: Faile to mail Cargo Price Request Approval for "+comp_abbr);
				}
			}
			else
			{
				System.out.println("--------------------NO Price Modification found for company"+comp_abbr+" -------------------- ");			
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//Auto mail for short transfer 
	public void sendShortTransfer()
	{
		String function_nm="sendShortTransfer()";
		try
		{
			double sug_percent=0.66;
			int count = 0;
			//fetching the -ve balance quantity data 
			String commonQry = "FROM FMS_BUY_DAILY_ALLOCATION C "
	  				+ "WHERE A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
	  				+ "AND A.CONT_NO=C.CONT_NO AND A.AGMT_NO=C.AGMT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE "
	  				+ "AND C.GAS_DT >= A.START_DT AND C.GAS_DT <= A.END_DT "
	  				+ "AND NOM_REV_NO=(SELECT MAX(B.NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
					+ "WHERE B.CONT_NO=C.CONT_NO AND B.AGMT_NO=C.AGMT_NO "
					+ "AND B.COMPANY_CD=C.COMPANY_CD AND B.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
					+ "AND B.TRANSPORTER_CD=C.TRANSPORTER_CD AND B.TRANS_SEQ=C.TRANS_SEQ AND B.BU_SEQ=C.BU_SEQ "
					+ "AND B.PLANT_SEQ=C.PLANT_SEQ AND B.CONTRACT_TYPE=C.CONTRACT_TYPE "
					+ "AND B.GAS_DT=C.GAS_DT) ";
			
			String commonQry1 = "FROM FMS_BUY_CARGO_ALLOC C "
	  				+ "WHERE A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.AGMT_TYPE=C.AGMT_TYPE "
	  				+ "AND A.CONT_NO=C.CONT_NO AND A.AGMT_NO=C.AGMT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND B.CARGO_NO=C.CARGO_NO "
	  				+ "AND C.ALLOC_REV=(SELECT MAX(B.ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC B "
					+ "WHERE B.CONT_NO=C.CONT_NO AND B.AGMT_NO=C.AGMT_NO AND B.AGMT_TYPE=C.AGMT_TYPE AND B.CARGO_NO=C.CARGO_NO "
					+ "AND B.COMPANY_CD=C.COMPANY_CD AND B.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND B.CONTRACT_TYPE=C.CONTRACT_TYPE) ";
			
			String qty="SELECT NVL(SUM(C.QTY_MMBTU),0) "+commonQry;
			
			String qty1="SELECT NVL(C.QQ_QTY_MMBTU,0) "+commonQry1;
			
			String allocated = "SELECT NVL(SUM(ALLOC_QTY),0) "
					+ "FROM FMS_SUPPLY_PURCHASE_MAP_DTL C "
					+ "WHERE C.COMPANY_CD=A.COMPANY_CD AND C.PUR_CONT_NO=A.CONT_NO ";
			
			String allocated1 = "SELECT NVL(SUM(ALLOC_QTY),0) "
					+ "FROM FMS_SUPPLY_PURCHASE_MAP_DTL C "
					+ "WHERE C.COMPANY_CD=A.COMPANY_CD AND C.PUR_CONT_NO=A.CONT_NO AND C.CARGO_NO=B.CARGO_NO ";
			
			String alloc_count="SELECT COUNT(*) "+commonQry;
			String alloc_count1="SELECT COUNT(*) "+commonQry1;
			
			String no_of_contDay="SELECT TO_DATE(A.END_DT,'DD/MM/YYYY') - TO_DATE(SYSDATE,'DD/MM/YYYY')+1 FROM DUAL";
			
			String projQty="(CASE WHEN ("+alloc_count+") > 0 THEN CASE WHEN ("+no_of_contDay+") > 0 THEN (A.DCQ * ("+no_of_contDay+")) ELSE 0 END ELSE 0 END)";
			
			String queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, CONT_NO, CONT_REV, TCQ, QUANTITY_UNIT, "
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),"
					+ "CONT_NAME,RATE,RATE_UNIT,CONT_STATUS,AGMT_NO,AGMT_REV,CONTRACT_TYPE,"
					+ "("+qty+") UNLOADED_QTY, "
					+ "("+allocated+") ALLOCATED_QTY,"
					+ "("+alloc_count+") ALLOC_COUNT, "
					+ "NULL, "
					+ "("+projQty+") PROJECTED_QTY "
					+ "FROM FMS_TRADER_CONT_MST A "
					+ "WHERE A.COMPANY_CD=? "
					+ "AND A.CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			queryString+=" UNION ALL ";
			queryString+="SELECT A.COMPANY_CD, A.COUNTERPARTY_CD, A.CONT_NO, A.CONT_REV, B.CARGO_QTY, 1, "
					+ "TO_CHAR(B.START_DT,'DD/MM/YYYY'),TO_CHAR(B.END_DT,'DD/MM/YYYY'),"
					+ "A.CONT_NAME,B.RATE,B.RATE_UNIT,A.CONT_STATUS,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,"
					+ "("+qty1+") UNLOADED_QTY, "
					+ "("+allocated1+") ALLOCATED_QTY,"
					+ "("+alloc_count1+") ALLOC_COUNT, "
					+ "CARGO_NO, "
					+ "NULL PROJECTED_QTY "
					+ "FROM FMS_TRADER_CN_MST A, FMS_TRADER_CARGO_MST B "
					+ "WHERE A.COMPANY_CD=? "
					+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV ";
			
			stmt1 = conn.prepareStatement(queryString);
			stmt1.setString(1,comp_cd);
			stmt1.setString(2,comp_cd);
			rset1 = stmt1.executeQuery();
			while(rset1.next())
			{
				String own_cd=rset1.getString(1)==null?"":rset1.getString(1);
				String countpty_cd=rset1.getString(2)==null?"":rset1.getString(2);
				String contNo=rset1.getString(3)==null?"0":rset1.getString(3);
				String contRev=rset1.getString(4)==null?"0":rset1.getString(4);
				double expected = rset1.getDouble(5);
				String qty_unit=rset1.getString(6)==null?"":rset1.getString(6);
				String agmtNo=rset1.getString(13)==null?"0":rset1.getString(13);
				String agmtRev=rset1.getString(14)==null?"0":rset1.getString(14);
				String cont_type=rset1.getString(15)==null?"":rset1.getString(15);
				double unloaded = rset1.getDouble(16);
				double allocated_qty = rset1.getDouble(17);
				int allocation_count = rset1.getInt(18);
				String cargoNo=rset1.getString(19)==null?"0":rset1.getString(19);
				
				if(qty_unit.equals("2"))
				{
					expected = expected * 1000000; //Convert to MMBTU
				}
				double projected_qty = rset1.getDouble(20);
				String unloadedQtyInfo="Actual Unloaded Qty : "+nf.format(unloaded)+"\nProjected Qty : "+nf.format(projected_qty);
				unloaded = unloaded + projected_qty;
				
				double actual_unloaded=0;
				if(allocation_count <= 0)
				{
					actual_unloaded=expected;
				}
				else
				{
					actual_unloaded=unloaded;
				}
				
				double avail_for_sale = actual_unloaded;
				String avail_for_sale_info=""+nf.format(actual_unloaded);
				if(cont_type.equals("N"))
				{
					avail_for_sale = actual_unloaded - (actual_unloaded*sug_percent)/100;
				}
				double balance_qty = avail_for_sale - allocated_qty;
				if(balance_qty<0)
				{
					count+=1;
					VCOUNTERPARTY_CD.add(countpty_cd);
					VCONT_NO.add(contNo);
					VCONT_REV.add(contRev);
					VAGMT_NO.add(agmtNo);
					VAGMT_REV.add(agmtRev);
					VCONT_TYPE.add(cont_type);
					VCARGO_NO.add(cargoNo);
					VBALANCE_QTY.add(balance_qty);
					
					String map = utilBean.NewDealMappingId(comp_cd, countpty_cd, agmtNo, agmtRev, contNo, contRev, cont_type, cargoNo);
					
					VDEAL_MAP_ID.add(map);
					String remark = "MMBTU Allocated exceeds MMBTU Avail for Sale!!";
					VSTATUS.add(remark);
				}
			}
			rset1.close();
			stmt1.close();
			
			//for generating the mail.
			String subject=comp_abbr+" "+CommonVariable.app_name_sub+" "+context+": Cargo Allocation Transfer: ("+count+") Short Cargo Closure Request";
			String mailBody="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";
			mailBody+="Please find the following short cargo closure request for "+today_date+".";
			mailBody+="<br><br>";
			mailBody+="<table style=\"border:1px solid black;border-collapse:collapse;\" border=\"1\">";
			mailBody+="<thead style=\"font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";\">";
			mailBody+="<tr>"
							+ "<th>Purchase Contract#</th>"
							+ "<th>Balance MMBTU</th>"
							+ "<th>Status</th>"
					+ "</tr>";
			mailBody+="</thead>";
			mailBody+="<tbody style=\"font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";\">";
			for(int i=0; i<VCOUNTERPARTY_CD.size();i++)
			{
				mailBody+="<tr>"
							+ "<td align=\"center\">"+VDEAL_MAP_ID.elementAt(i)+"</td>"
							+ "<td align=\"center\">"+VBALANCE_QTY.elementAt(i)+"</td>"
							+ "<td align=\"center\">"+VSTATUS.elementAt(i)+"</td>"
						+ "</tr>";
			}
			mailBody+="</tbody>";
			mailBody+="</table>";
			mailBody+="</span>";
			mailBody+="</html>";
			mailBody+=CommonVariable.mail_signature;
			mailBody+=CommonVariable.mail_disclaimer;
			
			String to_mail_list = utilBean.getToMailReceipentList(conn,comp_cd,"Energy Bank","Inventory","Daily","Auto");
			String cc_mail_list= utilBean.getCcMailReceipentList(conn,comp_cd,"Energy Bank","Inventory","Daily","Auto");

			if(!to_mail_list.equals("") && !mailBody.equals("")&& VCOUNTERPARTY_CD.size()>0)
			{
				boolean mailRes = mailDelv.sendMail(conn,to_mail_list, subject, mailBody,"",cc_mail_list,"");
				if(mailRes)
				{
					System.out.println("SUCCESS: Short Transfer Closure request mailed successfully for "+comp_abbr);
				}
				else
				{
					System.out.println("FAILURE: Failed to mail Short Transfer Closure request for "+comp_abbr);
				}
			}
			else
			{
				System.out.println("--------------------NO Short Transfer Closure request found for company"+comp_abbr+" -------------------- ");
			}
			
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void doClear()
	{
		 VCOUNTERPARTY_CD.clear();
		 VCOUNTERPARTY_NM.clear();
		 VCOUNTERPARTY_ABBR.clear();
		 VAGMT_NO.clear();
		 VAGMT_REV.clear();
		 VCONT_NO.clear();
		 VCONT_REV.clear();
		 VCONT_TYPE.clear();
		 VDEAL_MAP_ID.clear();
		 VPRICE_UNIT.clear();
		 VPRICE_UNIT_NM.clear();
		 VOLD_PRICE.clear();
		 VNEW_PRICE.clear();
		 VSTATUS.clear();
		 VEFF_DT.clear();
		 VCARGO_NO.clear();
		 VBALANCE_QTY.clear();
	}
	
	String to_mail="";
	String comp_cd="";
	String comp_abbr="";
	String today_date = "";
	
	Vector VCOMP_CD = new Vector();
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VAGMT_REV = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VCONT_REV = new Vector();
	Vector VCONT_TYPE = new Vector();
	Vector VDEAL_MAP_ID = new Vector();
	Vector VPRICE_UNIT = new Vector();
	Vector VPRICE_UNIT_NM = new Vector();
	Vector VOLD_PRICE = new Vector();
	Vector VNEW_PRICE = new Vector();
	Vector VSTATUS = new Vector();
	Vector VEFF_DT = new Vector();
	Vector VCARGO_NO = new Vector();
	Vector VBALANCE_QTY = new Vector();
}