package automation;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;

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
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.XmlUtilBean;

public class Auto_Accrual_Eod 
{
	public static void main(String[] args) 
	{
		AccrualReports ar = new AccrualReports();
		ar.init();
	}
}

class AccrualReports
{
	String db_src_file_name="Auto_Accrual_Eod.java";
	Connection conn;
	PreparedStatement st,stmt,stmt1,stmt2,stmt3,stmt4,stmt5,stmt6,stmt_temp,stmt_temp1,stmt_temp2,stmt_temp3;
	ResultSet rt,rset,rset1,rset2,rset3,rset4,rset5,rset6,rset_temp,rset_temp1,rset_temp2,rset_temp3;
	String queryString="",queryString1="",queryString2="",queryString3="",queryString4="",queryString5="",queryString_temp="";
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	
	String comp_cd="1";
	String comp_abbr="";
	String context="";
	String emp_cd="0";
	String file_path="";
	
	String AccrualFlag="";
	
	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();
	Auto_MailDelivery mail = new Auto_MailDelivery();
	XmlUtilBean xmlUtil = new XmlUtilBean();

    public void init()
	{ 
    	String function_nm="init()";
        try
        {
        	conn=new Auto_DB_Connection().db_conn();
        	if(conn != null)
            {  		
    			context=utilBean.getAutomationKeyDetail(conn, "ENV_CONTEXT");
    			
    			report_dt=utilDate.getSysdate();
    			String lastDateOfMonth=utilDate.getLastDateOfMonth( report_dt);
    			//lastDateOfMonth = report_dt; // JD: For dev Testing uncomment this line
    			
    			queryString = "SELECT COMPANY_CD,COMPANY_ABBR "
						+ "FROM FMS_COMPANY_OWNER_MST A "
						+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_OWNER_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) "
						+ "AND STATUS='Y' "
						+ "ORDER BY COMPANY_CD";
				st = conn.prepareStatement(queryString);
				rt = st.executeQuery();
				while(rt.next())
				{
					comp_cd=rt.getString(1)==null?"":rt.getString(1);
					comp_abbr=rt.getString(2)==null?"":rt.getString(2);
					
					file_path=utilBean.getAutomationKeyDetail(conn, "SAP_PATH_"+comp_cd);
					
					System.out.println("\n\n----------Accrual for "+comp_abbr+" Profile----------");
					
					AccrualFlag="PURCHASE";
	    			forAllBillingOption();
	    			PurchaseFreezAccrualData();
	    			System.out.println("Purchase Accrual EoD Completed!");
	    			if(!file_path.equals("") && report_dt.equals(lastDateOfMonth))
	    			{
	    				if(new File(file_path).exists()) 
				        {
		    				generatePurchaseAccrualXML();
		    				System.out.println("Purchase Accrual XML Generated!");
				        }
						else
						{
							System.out.println("File path doesn't exist, Purchase Accrual XML Generation Failed!");
						}
	    			}
	    			sendAccrualEODConfirmationMailForPurchase();
	    			
	    			doClear();
	    			
	    			AccrualFlag="GTA";
	    			getInvoiceType();
					for(int i=0;i<VINVOICE_TYPE.size();i++)
					{
						invoice_type=""+VINVOICE_TYPE.elementAt(i);
						forAllBillingOption();
					}
					GtaFreezAccrualData();
					System.out.println("GTA Accrual EoD Completed!");
					if(!file_path.equals("") && report_dt.equals(lastDateOfMonth))
	    			{
						if(new File(file_path).exists()) 
				        {
		    				generateGTAAccrualXML();
		    				System.out.println("GTA Accrual XML Generated!");
				        }
						else
						{
							System.out.println("File path doesn't exist, GTA Accrual XML Generation Failed!");
						}
	    			}
					sendAccrualEODConfirmationMailForGta();
					
					doClear();
					
					from_dt=utilDate.getFirstDateOfMonth(report_dt);
					to_dt=lastDateOfMonth;
					getSellBuy();
					getGxTransactionAccrualList();
					GxFreezAccrualData();
					System.out.println("Gx Accrual EoD Completed!");
					if(!file_path.equals("") && report_dt.equals(lastDateOfMonth))
	    			{
						if(new File(file_path).exists()) 
				        {
		    				generateGXAccrualXML();
		    				System.out.println("GX Accrual XML Generated!");
				        }
						else
						{
							System.out.println("File path doesn't exist, GX Accrual XML Generation Failed!");
						}
	    			}
					sendAccrualEODConfirmationMailForGx();
					
					doClear();
					
					AccrualFlag="SELL";
					forAllBillingOption();
					SalesFreezAccrualData();
					System.out.println("Sales Accrual EoD Completed!");
					if(!file_path.equals("") && report_dt.equals(lastDateOfMonth))
	    			{
						if(new File(file_path).exists()) 
				        {
		    				generateSalesAccrualXML();
		    				System.out.println("Sales Accrual XML Generated!");
				        }
						else
						{
							System.out.println("File path doesn't exist, Sales Accrual XML Generation Failed!");
						}
	    			}
					sendAccrualEODConfirmationMailForSales();
					
					doClear();
				}
				rt.close();
				st.close();
            }
        	conn.close();
			conn = null;
        }
        catch(Exception e)
        {
        	new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
        }
        finally
	    {
        	if(rt != null){try{rt.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
        	if(rset != null){try{rset.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset1 != null){try{rset1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset2 != null){try{rset2.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset3 != null){try{rset3.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset4 != null){try{rset4.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset5 != null){try{rset5.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset6 != null){try{rset6.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_temp != null){try{rset_temp.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_temp1 != null){try{rset_temp1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_temp2 != null){try{rset_temp2.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_temp3 != null){try{rset_temp3.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(st != null){try{st.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt6 != null){try{stmt6.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp != null){try{stmt_temp.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp1 != null){try{stmt_temp1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp2 != null){try{stmt_temp2.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp3 != null){try{stmt_temp3.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
    
    public void getSellBuy()
	{
    	String function_nm="getSellBuy()";
		VSELL_BUY_NM.add("Sell");
		VSELL_BUY_NM.add("Buy");
		
		VSELL_BUY_FLAG.add("S");
		VSELL_BUY_FLAG.add("B");
	}
    
    public void forAllBillingOption()
	{
    	String function_nm="forAllBillingOption()";
		try
		{
			if(!report_dt.equals(""))
			{
				String split_dt[]=report_dt.split("/");
				month=split_dt[1];
				year=split_dt[2];
				
				report_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
				report_end_dt=""+utilDate.getLastDateOfMonth(month, year);
			}
			
			//if(!month.equals("00"))
			{
				/*if(!month.equals("") && !year.equals(""))
				{
					String temp_billing_cycle=billing_cycle;
					for(int i=1; i<=9; i++)
					{
						billing_cycle=""+i;
						getBillingCyclePeriod();
						getAccrualList();
					}
					billing_cycle=temp_billing_cycle;
				}*/
			}
			//else
			{
				if(!month.equals("") && !year.equals(""))
				{
				 	int temp_month=Integer.parseInt(month);
						
					for(int j=1;j<=temp_month;j++)
					{
						if(j<=9)
						{
							month="0"+j;
						}
						else
						{
							month=""+j;
						}
						if(!month.equals("") && !year.equals(""))
						{
							String temp_billing_cycle=billing_cycle;
							for(int i=1; i<=9; i++)
							{
								billing_cycle=""+i;
								getBillingCyclePeriod();
								
								if(AccrualFlag.equals("PURCHASE"))
								{	
									getPurchaseAccrualList();
									getPurchaseLTCORAAccrualList();
								}
								else if(AccrualFlag.equals("GTA"))
								{
									if(i==1 || i==2 || i==8)
									{
										getGtaAccrualList();
									}
								}
								else if(AccrualFlag.equals("SELL"))
								{
									getSalesAccrualList();
									getSalesLTCORAAccrualList();
								}
							}
							
							billing_cycle=temp_billing_cycle;
						}
					}
					
					if(AccrualFlag.equals("PURCHASE"))
					{
						billing_cycle="10";
						getBillingCyclePeriod();
						getPurchaseCargoAccrualList();
					}
				}
			}
		}
		catch (Exception e) 
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
	public void getBillingCyclePeriod()
	{
		String function_nm="getBillingCyclePeriod()";
		try
		{
			String temp_lastDtOfMth=""+utilDate.getLastDateOfMonth(month, year);
			String temp_firstDtOfMth=""+utilDate.getFirstDateOfMonth(month, year);
			
			if(billing_cycle.equals("1") || billing_cycle.equals("2"))
			{
				billing_freq="F";
				if(billing_cycle.equals("1"))
				{
					billing_freq_nm="1st-Fortnight";
					period_start_dt="01/"+month+"/"+year;
					period_end_dt="15/"+month+"/"+year;
				}
				else if(billing_cycle.equals("2"))
				{
					billing_freq_nm="2nd-Fortnight";
					period_start_dt="16/"+month+"/"+year;
					period_end_dt=temp_lastDtOfMth;
				}
			}
			else if(billing_cycle.equals("3") || billing_cycle.equals("4") || billing_cycle.equals("5") || billing_cycle.equals("6") || billing_cycle.equals("9"))
			{
				billing_freq="W";
				if(billing_cycle.equals("3"))
				{
					billing_freq_nm="1st-Weekly";
					period_start_dt="01/"+month+"/"+year;
					period_end_dt="07/"+month+"/"+year;
				}
				else if(billing_cycle.equals("4")) 
				{
					billing_freq_nm="2nd-Weekly";
					period_start_dt="08/"+month+"/"+year;
					period_end_dt="14/"+month+"/"+year;
				}
				else if(billing_cycle.equals("5")) 
				{
					billing_freq_nm="3rd-Weekly";
					period_start_dt="15/"+month+"/"+year;
					period_end_dt="21/"+month+"/"+year;
				} 
				else if(billing_cycle.equals("6")) 
				{
					billing_freq_nm="4th-Weekly";
					period_start_dt="22/"+month+"/"+year;
					period_end_dt="28/"+month+"/"+year;
				} 
				else if(billing_cycle.equals("9"))
				{
					billing_freq_nm="5th-Weekly";
					if(month.equals("02"))
					{
						int days=utilDate.getDays(temp_lastDtOfMth, temp_firstDtOfMth);
						if(days==29)
						{
							period_start_dt="29/"+month+"/"+year;
							period_end_dt=""+temp_lastDtOfMth;
						}
					}
					else
					{
						period_start_dt="29/"+month+"/"+year;
						period_end_dt=""+temp_lastDtOfMth;
					}
				}
			}
			else if(billing_cycle.equals("7"))
			{
				billing_freq_nm="Monthly";
				billing_freq="M";
				period_start_dt=temp_firstDtOfMth;
				period_end_dt=temp_lastDtOfMth;
			}
			else if(billing_cycle.equals("8"))
			{
				billing_freq="O";
				billing_freq_nm="Other";
				//period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
				//period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
			}
			else if(billing_cycle.equals("10"))
			{
				billing_freq="D";
				billing_freq_nm="Delivery Period";
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getPurchaseAccrualList()
	{
		String function_nm="getPurchaseAccrualList()";
		try
		{
			financial_year=utilDate.getFinancialYear(period_end_dt);
			
			if(billing_cycle.equals("8"))
			{
				String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
				String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);

				queryString="SELECT DISTINCT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,"
						+ "C.BILLING_DAYS,A.TRADE_REF_NO,A.SPLIT_FLAG,C.DUE_DATE,C.DUE_DT_IN,C.EXCL_SAT_MAP,"
						+ "A.DCQ,A.RATE,A.RATE_UNIT,C.INVOICE_CUR_CD,C.EXCHNG_RATE_CD,C.EXCHNG_RATE_CAL,C.EXCHG_VAL "
						+ "FROM FMS_TRADER_CONT_MST A, FMS_TRADER_BILLING_DTL C "
						+ "WHERE A.COMPANY_CD=? "
						+ "AND (A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY')) "
						+ "AND (A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY')) "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "//AND A.CONT_REV=C.CONT_REV "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND C.BILLING_FREQ=?";
				//HP20250405 REMOVING CONT_REV CHECKING FOR BILLING DETAIL FROM THE QUERY CONDITION AS CONFIRMED BY JD MAM
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, temp_period_end_dt);
				stmt.setString(3, temp_period_start_dt);
				stmt.setString(4, report_end_dt);
				stmt.setString(5, report_start_dt);
				stmt.setString(6, billing_freq);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					String cont_st_dt=rset.getString(7)==null?"":rset.getString(7);
					String cont_end_dt=rset.getString(8)==null?"":rset.getString(8);
					String cont_ref_no=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					String deal_no=cont_type+""+contno;

					String billing_days=rset.getString(12)==null?"1":rset.getString(12);
					String trade_ref_no=rset.getString(13)==null?"":rset.getString(13);
					String split_flag=rset.getString(14)==null?"":rset.getString(14);
					
					String due_days=rset.getString(15)==null?"":rset.getString(15);
					String consider_due_dt_in=rset.getString(16)==null?"":rset.getString(16);
					String exclude_sat=rset.getString(17)==null?"":rset.getString(17);
					
					String dcq=rset.getString(18)==null?"":rset.getString(18);
					String price=rset.getString(19)==null?"":rset.getString(19);
					String price_unit=rset.getString(20)==null?"":rset.getString(20);
					String price_unit_nm=utilBean.getRateUnitNm(conn,price_unit);
					String invoice_raise_in=rset.getString(21)==null?"":rset.getString(21);
					
					String exchng_rate_cd = rset.getString(22)==null?"":rset.getString(22);
					String exchng_rate_cal = rset.getString(23)==null?"":rset.getString(23);
					
					String fixed_exchng_val=nf2.format(rset.getDouble(24));
					
					if(cont_type.equals("I"))
					{
						cont_ref_no=trade_ref_no;
					}
					
					int temp_count=utilDate.getDays(cont_end_dt,temp_period_end_dt);
					if(temp_count <= 0)
					{
						temp_period_end_dt=cont_end_dt;
					}
					
					temp_count=utilDate.getDays(temp_period_start_dt,cont_st_dt);
					if(temp_count <= 1)
					{
						temp_period_start_dt=cont_st_dt;
					}
					
					int days=utilDate.getDays(temp_period_end_dt, temp_period_start_dt);
					int tot_row = days/Integer.parseInt(billing_days);
					
					if(days%2!=0)
					{
						tot_row+=1;
					}		
					
					String temp_dt = utilDate.getDate(temp_period_start_dt,"-1");
					
					for(int i=0;i<tot_row;i++)
					{
						queryString_temp="SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+?,'DD/MM/YYYY') "
								+ "FROM DUAL";
						stmt_temp3 = conn.prepareStatement(queryString_temp);
						stmt_temp3.setString(1, temp_dt);
						stmt_temp3.setString(2, billing_days);
						rset_temp3 = stmt_temp3.executeQuery();
						while(rset_temp3.next())
						{
							String st_dt=utilDate.getDate(temp_dt,"1");
							temp_dt=rset_temp3.getString(1);
							String end_dt = temp_dt;
							
							temp_count=utilDate.getDays(temp_dt,temp_period_end_dt);
							if(temp_count > 0)
							{
								end_dt=temp_period_end_dt;
							}
							
							Vector VTEMP_TRD_CD=new Vector();
							Vector VTEMP_PLANT_SEQ=new Vector();
							Vector VTEMP_PLANT_ABBR=new Vector();
							Vector VTEMP_SPLIT_VALUE=new Vector();
							
							queryString1="SELECT PLANT_SEQ_NO,SPLIT_VALUE "
									+ "FROM FMS_TRADER_CONT_PLANT "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
							stmt1 = conn.prepareStatement(queryString1);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, countpty_cd);
							stmt1.setString(3, contno);
							stmt1.setString(4, contrev);
							stmt1.setString(5, agmtno);
							stmt1.setString(6, agmtrev);
							stmt1.setString(7, cont_type);
							rset1 = stmt1.executeQuery();
							while(rset1.next())
							{
								String plant_seq = rset1.getString(1)==null?"":rset1.getString(1);
								String split_value = rset1.getString(2)==null?"":rset1.getString(2);
								String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "T");
								
								VTEMP_TRD_CD.add(countpty_cd);
								VTEMP_PLANT_SEQ.add(plant_seq);
								VTEMP_PLANT_ABBR.add(plant_abbr);
								VTEMP_SPLIT_VALUE.add(split_value);
							}
							
							rset1.close();
							stmt1.close();
							
							if(split_flag.equals("Y"))
							{
								queryString1="SELECT SPLIT_TRADER_CD,PLANT_SEQ_NO,SPLIT_VALUE "
										+ "FROM FMS_TRADER_CONT_SPLIT_PLANT "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
										+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
								stmt1 = conn.prepareStatement(queryString1);
								stmt1.setString(1, comp_cd);
								stmt1.setString(2, countpty_cd);
								stmt1.setString(3, contno);
								stmt1.setString(4, contrev);
								stmt1.setString(5, agmtno);
								stmt1.setString(6, agmtrev);
								stmt1.setString(7, cont_type);
								rset1 = stmt1.executeQuery();
								while(rset1.next())
								{
									String split_trd_cd = rset1.getString(1)==null?"":rset1.getString(1);
									String plant_seq = rset1.getString(2)==null?"":rset1.getString(2);
									String split_value = rset1.getString(3)==null?"":rset1.getString(3);
									String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,split_trd_cd, own_cd, plant_seq, "T");
									
									VTEMP_TRD_CD.add(split_trd_cd);
									VTEMP_PLANT_SEQ.add(plant_seq);
									VTEMP_PLANT_ABBR.add(plant_abbr);
									VTEMP_SPLIT_VALUE.add(split_value);
								}
								rset1.close();
								stmt1.close();
							}
							
							queryString2="SELECT PLANT_SEQ_NO "
									+ "FROM FMS_TRADER_CONT_BU "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, countpty_cd);
							stmt2.setString(3, contno);
							stmt2.setString(4, contrev);
							stmt2.setString(5, agmtno);
							stmt2.setString(6, agmtrev);
							stmt2.setString(7, cont_type);
							rset2 = stmt2.executeQuery();
							while(rset2.next())
							{
								String bu_plant_seq = rset2.getString(1)==null?"":rset2.getString(1);
								String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
								
								for(int j=0;j<VTEMP_PLANT_SEQ.size();j++)
								{
									String plant_seq=""+VTEMP_PLANT_SEQ.elementAt(j);
									String plant_abbr=""+VTEMP_PLANT_ABBR.elementAt(j);
									String split_value=""+VTEMP_SPLIT_VALUE.elementAt(j);
									
									String temp_countpty_cd=""+VTEMP_TRD_CD.elementAt(j);
									
									int isInvExist=0;
									queryString3="SELECT COUNT(*) "
											+ "FROM FMS_PUR_SG_INV_MST "
											+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
											+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
											+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
											+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
											+ "AND FINANCIAL_YEAR=? AND PDF_INV_DTL IS NOT NULL AND INV_FLAG=? ";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, own_cd);
									stmt3.setString(2, temp_countpty_cd);
									stmt3.setString(3, contno);
									stmt3.setString(4, agmtno);
									stmt3.setString(5, plant_seq);
									stmt3.setString(6, bu_plant_seq);
									stmt3.setString(7, cont_type);
									stmt3.setString(8, billing_cycle);
									stmt3.setString(9, st_dt);
									stmt3.setString(10, end_dt);
									stmt3.setString(11, financial_year);
									stmt3.setString(12, "F");
									rset3 = stmt3.executeQuery();
									if(rset3.next())
									{
										isInvExist+=rset3.getInt(1);
									}
									rset3.close();
									stmt3.close();
									
									queryString3="SELECT COUNT(*) "
											+ "FROM FMS_PUR_PG_INV_MST "
											+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
											+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
											+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
											+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
											+ "AND FINANCIAL_YEAR=? AND PDF_INV_DTL IS NOT NULL AND INV_FLAG=?";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, own_cd);
									stmt3.setString(2, temp_countpty_cd);
									stmt3.setString(3, contno);
									stmt3.setString(4, agmtno);
									stmt3.setString(5, plant_seq);
									stmt3.setString(6, bu_plant_seq);
									stmt3.setString(7, cont_type);
									stmt3.setString(8, billing_cycle);
									stmt3.setString(9, st_dt);
									stmt3.setString(10, end_dt);
									stmt3.setString(11, financial_year);
									stmt3.setString(12, "F");
									rset3 = stmt3.executeQuery();
									if(rset3.next())
									{
										isInvExist+=rset3.getInt(1);
									}
									rset3.close();
									stmt3.close();
									if(isInvExist==0)
									{
										String tempEndDate=end_dt;
										if(utilDate.getDays(cont_end_dt, end_dt) <= 0)
										{
											tempEndDate=cont_end_dt;
										}
										
										String holiday_state="";
										queryString1="SELECT HOLIDAY_STATE "
												+ "FROM FMS_TRADER_BILLING_DTL "
												+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
												+ "AND AGMT_NO=? AND AGMT_REV=? "
												+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? ";
										stmt1=conn.prepareStatement(queryString1);
										stmt1.setString(1, comp_cd);
										stmt1.setString(2, temp_countpty_cd);
										stmt1.setString(3, agmtno);
										stmt1.setString(4, agmtrev);
										stmt1.setString(5, contno);
										stmt1.setString(6, cont_type);
										stmt1.setString(7, plant_seq);
										rset1=stmt1.executeQuery();
										if(rset1.next())
										{
											holiday_state=rset1.getString(1)==null?"":rset1.getString(1);
										}
										rset1.close();
										stmt1.close();
										
										String transportation_charges="", other_charges="",marketing_margin="";
										queryString1="SELECT CHARGE_RATE,CHARGE_ABBR "
												+ "FROM FMS_TRADER_CONT_PLANT_CHRG A "
												+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
												+ "AND AGMT_NO=? AND AGMT_REV=? "
												+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? "
												+ "AND A.EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_TRADER_CONT_PLANT_CHRG B WHERE A.COMPANY_CD=B.COMPANY_CD "
												+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
												+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.CHARGE_ABBR=B.CHARGE_ABBR "
												+ "AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
										stmt1=conn.prepareStatement(queryString1);
										stmt1.setString(1, comp_cd);
										stmt1.setString(2, temp_countpty_cd);
										stmt1.setString(3, agmtno);
										stmt1.setString(4, agmtrev);
										stmt1.setString(5, contno);
										stmt1.setString(6, cont_type);
										stmt1.setString(7, plant_seq);
										stmt1.setString(8, tempEndDate);
										rset1=stmt1.executeQuery();
										while(rset1.next())
										{
											String charge_abbr=rset1.getString(2)==null?"":rset1.getString(2);
											if(charge_abbr.equals("TC"))
											{
												transportation_charges=rset1.getString(1)==null?"":nf2.format(rset1.getDouble(1));
											}
											else if(charge_abbr.equals("OC"))
											{
												other_charges=rset1.getString(1)==null?"":nf2.format(rset1.getDouble(1));
											}
											else if(charge_abbr.equals("MM"))
											{
												marketing_margin=rset1.getString(1)==null?"":nf2.format(rset1.getDouble(1));
											}
										}
										rset1.close();
										stmt1.close();
										
										double qtyMMBTU=0;
										
										if(split_flag.equals("Y"))
										{
											queryString3="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
													+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
													+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
													+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
											stmt3 = conn.prepareStatement(queryString3);
											stmt3.setString(1, st_dt);
											stmt3.setString(2, tempEndDate);
											rset3 = stmt3.executeQuery();
											while(rset3.next())
											{
												String date=rset3.getString(1)==null?"":rset3.getString(1);
												
												queryString5="SELECT COALESCE(ALLOCATION, SELLER_NOM, BUYER_NOM, DCQ, 0) "
														+ "FROM "
														+ "(SELECT (SELECT SUM(QTY_MMBTU) "
										  				+ "FROM FMS_BUY_DAILY_ALLOCATION A "
										  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
														+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
														+ "AND CONTRACT_TYPE=? AND BU_SEQ=? "
														+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
														+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
														+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
														+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
														+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
														+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
														+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) ALLOCATION, "
														+ "(SELECT SUM(QTY_MMBTU) "
										  				+ "FROM FMS_BUY_DAILY_SELLER_NOM A "
										  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
														+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
														+ "AND CONTRACT_TYPE=? AND BU_SEQ=? "
														+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
														+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_SELLER_NOM B "
														+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
														+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
														+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
														+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
														+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) SELLER_NOM, "
														+ "(SELECT SUM(QTY_MMBTU) "
										  				+ "FROM FMS_BUY_DAILY_BUYER_NOM A "
										  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
														+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
														+ "AND CONTRACT_TYPE=? AND BU_SEQ=? "
														+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
														+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_BUYER_NOM B "
														+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
														+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
														+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
														+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
														+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) BUYER_NOM, "
														+ "(NVL((SELECT DCQ "
														+ "FROM FMS_TRADER_CONT_DCQ_DTL "
														+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
														+ "AND AGMT_NO=? AND CONT_NO=? "
														+ "AND CONTRACT_TYPE=? AND STATUS=? "
														+ "AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT>=TO_DATE(?,'DD/MM/YYYY')),?)) DCQ FROM DUAL) ";
												stmt5 = conn.prepareStatement(queryString5);
												stmt5.setString(1, contno);
												stmt5.setString(2, agmtno);
												stmt5.setString(3, comp_cd);
												stmt5.setString(4, countpty_cd);
												stmt5.setString(5, cont_type);
												stmt5.setString(6, bu_plant_seq);
												stmt5.setString(7, date);
												stmt5.setString(8, contno);
												stmt5.setString(9, agmtno);
												stmt5.setString(10, comp_cd);
												stmt5.setString(11, countpty_cd);
												stmt5.setString(12, cont_type);
												stmt5.setString(13, bu_plant_seq);
												stmt5.setString(14, date);
												stmt5.setString(15, contno);
												stmt5.setString(16, agmtno);
												stmt5.setString(17, comp_cd);
												stmt5.setString(18, countpty_cd);
												stmt5.setString(19, cont_type);
												stmt5.setString(20, bu_plant_seq);
												stmt5.setString(21, date);
												stmt5.setString(22, comp_cd);
												stmt5.setString(23, countpty_cd);
												stmt5.setString(24, agmtno);
												stmt5.setString(25, contno);
												stmt5.setString(26, cont_type);
												stmt5.setString(27, "Y");
												stmt5.setString(28, date);
												stmt5.setString(29, date);
												stmt5.setString(30, dcq);
												rset5 = stmt5.executeQuery();
												if(rset5.next())
												{
													qtyMMBTU+=rset5.getDouble(1);
												}
												rset5.close();
												stmt5.close();
											}
											rset3.close();
											stmt3.close();
											
											if(qtyMMBTU>0 && !split_value.equals(""))
											{
												qtyMMBTU=(qtyMMBTU * (Double.parseDouble(""+split_value) / 100));
											}
											else
											{
												qtyMMBTU=0;
											}
										}
										else
										{
											queryString3="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
													+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
													+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
													+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
											stmt3 = conn.prepareStatement(queryString3);
											stmt3.setString(1, st_dt);
											stmt3.setString(2, tempEndDate);
											rset3 = stmt3.executeQuery();
											while(rset3.next())
											{
												String date=rset3.getString(1)==null?"":rset3.getString(1);
												
												queryString5="SELECT COALESCE(ALLOCATION, SELLER_NOM, BUYER_NOM, DCQ, 0) "
														+ "FROM "
														+ "(SELECT (SELECT SUM(QTY_MMBTU) "
										  				+ "FROM FMS_BUY_DAILY_ALLOCATION A "
										  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
														+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
														+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
														+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
														+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
														+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
														+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
														+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
														+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
														+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) ALLOCATION, "
														+ "(SELECT SUM(QTY_MMBTU) "
										  				+ "FROM FMS_BUY_DAILY_SELLER_NOM A "
										  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
														+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
														+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
														+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
														+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_SELLER_NOM B "
														+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
														+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
														+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
														+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
														+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) SELLER_NOM, "
														+ "(SELECT SUM(QTY_MMBTU) "
										  				+ "FROM FMS_BUY_DAILY_BUYER_NOM A "
										  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
														+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
														+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
														+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
														+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_BUYER_NOM B "
														+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
														+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
														+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
														+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
														+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) BUYER_NOM, "
														+ "(NVL((SELECT DCQ "
														+ "FROM FMS_TRADER_CONT_DCQ_DTL "
														+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
														+ "AND AGMT_NO=? AND CONT_NO=? "
														+ "AND CONTRACT_TYPE=? AND STATUS=? "
														+ "AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT>=TO_DATE(?,'DD/MM/YYYY')),?)) DCQ FROM DUAL) ";
												stmt5 = conn.prepareStatement(queryString5);
												stmt5.setString(1, contno);
												stmt5.setString(2, agmtno);
												stmt5.setString(3, comp_cd);
												stmt5.setString(4, countpty_cd);
												stmt5.setString(5, plant_seq);
												stmt5.setString(6, cont_type);
												stmt5.setString(7, bu_plant_seq);
												stmt5.setString(8, date);
												stmt5.setString(9, contno);
												stmt5.setString(10, agmtno);
												stmt5.setString(11, comp_cd);
												stmt5.setString(12, countpty_cd);
												stmt5.setString(13, plant_seq);
												stmt5.setString(14, cont_type);
												stmt5.setString(15, bu_plant_seq);
												stmt5.setString(16, date);
												stmt5.setString(17, contno);
												stmt5.setString(18, agmtno);
												stmt5.setString(19, comp_cd);
												stmt5.setString(20, countpty_cd);
												stmt5.setString(21, plant_seq);
												stmt5.setString(22, cont_type);
												stmt5.setString(23, bu_plant_seq);
												stmt5.setString(24, date);
												stmt5.setString(25, comp_cd);
												stmt5.setString(26, countpty_cd);
												stmt5.setString(27, agmtno);
												stmt5.setString(28, contno);
												stmt5.setString(29, cont_type);
												stmt5.setString(30, "Y");
												stmt5.setString(31, date);
												stmt5.setString(32, date);
												stmt5.setString(33, dcq);
												rset5 = stmt5.executeQuery();
												if(rset5.next())
												{
													qtyMMBTU+=rset5.getDouble(1);
												}
												rset5.close();
												stmt5.close();
											}
											rset3.close();
											stmt3.close();
										}
										
										String new_price="";
										queryString4 = "SELECT DISTINCT NEW_SALE_PRICE "
												+ "FROM FMS_SUPPLY_ALLOC_REVISED A "
												+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND CONT_NO=? "
												//+ "AND COUNTERPARTY_CD='"+temp_countpty_cd+"' "
												+ "AND FLAG='A' AND CONTRACT_TYPE=? "
												+ "AND MODIFICATION_SEQ_NO = (SELECT MAX(MODIFICATION_SEQ_NO) FROM FMS_SUPPLY_ALLOC_REVISED B "
												+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
												+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.FLAG=B.FLAG AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
												+ "AND B.NEW_PRICE_EFF_DT <=TO_DATE(?,'DD/MM/YYYY'))";
										stmt4 = conn.prepareStatement(queryString4);
										stmt4.setString(1, comp_cd);
										stmt4.setString(2, agmtno);
										stmt4.setString(3, contno);
										stmt4.setString(4, cont_type);
										stmt4.setString(5, end_dt);
										rset4 = stmt4.executeQuery();
										if(rset4.next())
										{
											new_price=rset4.getString(1)==null?"":rset4.getString(1);
											if(!new_price.equals(""))
											{
												price=new_price;
											}
										}
										rset4.close();
										stmt4.close();
										
										double exchng_rate=0;
										String exchng_rate_dt="";
										if(exchng_rate_cd.equals("0")) //FOR FIXED EXCHANGE RATE
										{
											exchng_rate=Double.parseDouble(fixed_exchng_val);
										}
										else
										{
											queryString3="SELECT NVL(EXCHG_VAL,0),TO_CHAR(EFF_DT,'DD/MM/YYYY') "
													+ "FROM FMS_EXCHG_RATE_ENTRY A "
													+ "WHERE EXCHG_RATE_CD=? "
													+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY') "
													+ "ORDER BY EFF_DT DESC";
											stmt3 = conn.prepareStatement(queryString3);
											stmt3.setString(1, exchng_rate_cd);
											stmt3.setString(2, end_dt);
											rset3 = stmt3.executeQuery();
											if(rset3.next())
											{
												exchng_rate=rset3.getDouble(1);
												exchng_rate_dt=rset3.getString(2)==null?"":rset3.getString(2);
											}
											rset3.close();
											stmt3.close();
										}
										
										double accrual_amt=0;
										double charges_amt=0;
										double gross_amt=0;
										
										if(!transportation_charges.equals(""))
										{
											charges_amt+= qtyMMBTU * Double.parseDouble(transportation_charges);
										}
										
										if(!marketing_margin.equals(""))
										{
											charges_amt+= qtyMMBTU * Double.parseDouble(marketing_margin);
										}
										
										if(!other_charges.equals(""))
										{
											charges_amt+= qtyMMBTU * Double.parseDouble(other_charges);
										}
										
										/*USD=USD need to convert in INR
										if(price_unit.equals(invoice_raise_in))
										{
											accrual_amt=qtyMMBTU * Double.parseDouble(price);
											gross_amt = accrual_amt;
										}
										else*/
										{
											accrual_amt=qtyMMBTU * Double.parseDouble(price);
											if(price_unit.equals("2") && charges_amt > 0)
											{
												accrual_amt+=charges_amt / exchng_rate;
											}
											else
											{
												accrual_amt+=charges_amt;
											}

											if(price_unit.equals("2"))
											{
												gross_amt = accrual_amt * exchng_rate;
											}
											else
											{
												gross_amt = accrual_amt;
											}
										}
										
										if(qtyMMBTU>0 && accrual_amt>0)
										{
											VFINANCIAL_YEAR.add(financial_year);
											VCOUNTERPTY_CD.add(temp_countpty_cd);
											VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,temp_countpty_cd));
											VCOUNTERPTY_NM.add(""+utilBean.getCounterpartyName(conn,temp_countpty_cd));
											VAGMT_NO.add(agmtno);
											VAGMT_REV_NO.add(agmtrev);
											VCONT_NO.add(contno);
											VCONT_REV_NO.add(contrev);
											VCARGO_NO.add("0");
											VBOE_NO.add("0");
											VBOE_NM.add("");
											VINV_FLAG.add("F");
											VCONTRACT_TYPE.add(cont_type);
											VSTART_DT.add(rset.getString(7)==null?"":rset.getString(7));
											VEND_DT.add(rset.getString(8)==null?"":rset.getString(8));
											VDIS_CONT_MAPPING.add(deal_no);
											VCONT_REF_NO.add(cont_ref_no);
											VPLANT_SEQ.add(plant_seq);
											VPLANT_ABBR.add(plant_abbr);
											VBU_PLANT_SEQ.add(bu_plant_seq);
											VBU_PLANT_ABBR.add(bu_plant_abbr);
											VPERIOD_START_DT.add(st_dt);
											VPERIOD_END_DT.add(end_dt);
											VBILLING_FREQ_FLAG.add(billing_cycle);
											VBILLING_FREQ_NM.add(billing_freq_nm);
											VPRODUCTION_MONTH.add(month+"/"+year);
											VINVOICE_DUE_DT.add(utilBean.DueDateCalculation(conn,end_dt, due_days,consider_due_dt_in,exclude_sat,comp_cd,holiday_state));
											VSPLIT_FLAG.add(split_flag);
											if(split_flag.equals("Y"))
											{
												VSPLIT_VALUE.add(split_value);
											}
											else
											{
												VSPLIT_VALUE.add("");
											}
											
											VCASH_FLOW.add("Commodity");
											
											if(price_unit.equals("2"))
											{
												VEXCHNG_RATE.add(nf2.format(exchng_rate));
												VEXCHNG_RATE_CD.add(exchng_rate_cd);
												VEXCHNG_RATE_DT.add(exchng_rate_dt);
											}
											else
											{
												VEXCHNG_RATE.add("");
												VEXCHNG_RATE_CD.add("");
												VEXCHNG_RATE_DT.add("");
											}
											
											VACCRUAL_QTY.add(nf.format(qtyMMBTU));
											VACCRUAL_AMT.add(nf.format(accrual_amt));
											
											VSALES_PRICE_CD.add(price_unit);
											VSALES_PRICE_NM.add(price_unit_nm);
											VINVOICE_RAISED_IN.add(invoice_raise_in);
											
											VGROSS_AMT.add(nf.format(gross_amt));
										}
									}
								}
							}
							rset2.close();
							stmt2.close();
						}
						rset_temp3.close();
						stmt_temp3.close();
					}
				}
				rset.close();
				stmt.close();
			}
			else
			{
				queryString="SELECT DISTINCT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,"
						+ "A.TRADE_REF_NO,A.SPLIT_FLAG,C.DUE_DATE,C.DUE_DT_IN,C.EXCL_SAT_MAP, "
						+ "A.DCQ,A.RATE,A.RATE_UNIT,C.INVOICE_CUR_CD,C.EXCHNG_RATE_CD,C.EXCHNG_RATE_CAL,C.EXCHG_VAL "
						+ "FROM FMS_TRADER_CONT_MST A, FMS_TRADER_BILLING_DTL C "
						+ "WHERE A.COMPANY_CD=? "
						+ "AND (A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY')) "
						+ "AND (A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY')) "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "//AND A.CONT_REV=C.CONT_REV "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND C.BILLING_FREQ=?";
				//HP20250405 REMOVING CONT_REV CHECKING FOR BILLING DETAIL FROM THE QUERY CONDITION AS CONFIRMED BY JD MAM
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, period_end_dt);
				stmt.setString(3, period_start_dt);
				stmt.setString(4, report_end_dt);
				stmt.setString(5, report_start_dt);
				stmt.setString(6, billing_freq);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					String cont_start_dt=rset.getString(7)==null?"":rset.getString(7);
					String cont_end_dt=rset.getString(8)==null?"":rset.getString(8);
					String cont_ref_no=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					String deal_no=cont_type+""+contno;

					String trade_ref_no=rset.getString(12)==null?"":rset.getString(12);
					String split_flag=rset.getString(13)==null?"":rset.getString(13);
					
					String due_days=rset.getString(14)==null?"":rset.getString(14);
					String consider_due_dt_in=rset.getString(15)==null?"":rset.getString(15);
					String exclude_sat=rset.getString(16)==null?"":rset.getString(16);
					
					String dcq=rset.getString(17)==null?"":rset.getString(17);
					String price=rset.getString(18)==null?"":rset.getString(18);
					String price_unit=rset.getString(19)==null?"":rset.getString(19);
					String price_unit_nm=utilBean.getRateUnitNm(conn,price_unit);
					String invoice_raise_in=rset.getString(20)==null?"":rset.getString(20);
					
					String exchng_rate_cd = rset.getString(21)==null?"":rset.getString(21);
					String exchng_rate_cal = rset.getString(22)==null?"":rset.getString(22);
					
					String fixed_exchng_val=nf2.format(rset.getDouble(23));
					
					if(cont_type.equals("I"))
					{
						cont_ref_no=trade_ref_no;
					}

					Vector VTEMP_TRD_CD=new Vector();
					Vector VTEMP_PLANT_SEQ=new Vector();
					Vector VTEMP_PLANT_ABBR=new Vector();
					Vector VTEMP_SPLIT_VALUE=new Vector();
					
					queryString1="SELECT PLANT_SEQ_NO,SPLIT_VALUE "
							+ "FROM FMS_TRADER_CONT_PLANT "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, countpty_cd);
					stmt1.setString(3, contno);
					stmt1.setString(4, contrev);
					stmt1.setString(5, agmtno);
					stmt1.setString(6, agmtrev);
					stmt1.setString(7, cont_type);
					rset1 = stmt1.executeQuery();
					while(rset1.next())
					{
						String plant_seq = rset1.getString(1)==null?"":rset1.getString(1);
						String split_value = rset1.getString(2)==null?"":rset1.getString(2);
						String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "T");
						
						VTEMP_TRD_CD.add(countpty_cd);
						VTEMP_PLANT_SEQ.add(plant_seq);
						VTEMP_PLANT_ABBR.add(plant_abbr);
						VTEMP_SPLIT_VALUE.add(split_value);
					}
					rset1.close();
					stmt1.close();
					
					if(split_flag.equals("Y"))
					{
						queryString1="SELECT SPLIT_TRADER_CD,PLANT_SEQ_NO,SPLIT_VALUE "
								+ "FROM FMS_TRADER_CONT_SPLIT_PLANT "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, countpty_cd);
						stmt1.setString(3, contno);
						stmt1.setString(4, contrev);
						stmt1.setString(5, agmtno);
						stmt1.setString(6, agmtrev);
						stmt1.setString(7, cont_type);
						rset1 = stmt1.executeQuery();
						while(rset1.next())
						{
							String split_trd_cd = rset1.getString(1)==null?"":rset1.getString(1);
							String plant_seq = rset1.getString(2)==null?"":rset1.getString(2);
							String split_value = rset1.getString(3)==null?"":rset1.getString(3);
							String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,split_trd_cd, own_cd, plant_seq, "T");
							
							VTEMP_TRD_CD.add(split_trd_cd);
							VTEMP_PLANT_SEQ.add(plant_seq);
							VTEMP_PLANT_ABBR.add(plant_abbr);
							VTEMP_SPLIT_VALUE.add(split_value);
						}
						rset1.close();
						stmt1.close();
					}
					queryString2="SELECT PLANT_SEQ_NO "
							+ "FROM FMS_TRADER_CONT_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, countpty_cd);
					stmt2.setString(3, contno);
					stmt2.setString(4, contrev);
					stmt2.setString(5, agmtno);
					stmt2.setString(6, agmtrev);
					stmt2.setString(7, cont_type);
					rset2 = stmt2.executeQuery();
					while(rset2.next())
					{
						String bu_plant_seq = rset2.getString(1)==null?"":rset2.getString(1);
						String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
					
						for(int i=0;i<VTEMP_PLANT_SEQ.size();i++)
						{
							String plant_seq=""+VTEMP_PLANT_SEQ.elementAt(i);
							String plant_abbr=""+VTEMP_PLANT_ABBR.elementAt(i);
							String split_value=""+VTEMP_SPLIT_VALUE.elementAt(i);
							
							String temp_countpty_cd=""+VTEMP_TRD_CD.elementAt(i);
							
							int isInvExist=0;
							queryString3="SELECT COUNT(*) "
									+ "FROM FMS_PUR_SG_INV_MST "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
									+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND FINANCIAL_YEAR=? AND PDF_INV_DTL IS NOT NULL AND INV_FLAG=? ";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, own_cd);
							stmt3.setString(2, temp_countpty_cd);
							stmt3.setString(3, contno);
							stmt3.setString(4, agmtno);
							stmt3.setString(5, plant_seq);
							stmt3.setString(6, bu_plant_seq);
							stmt3.setString(7, cont_type);
							stmt3.setString(8, billing_cycle);
							stmt3.setString(9, period_start_dt);
							stmt3.setString(10, period_end_dt);
							stmt3.setString(11, financial_year);
							stmt3.setString(12, "F");
							rset3 = stmt3.executeQuery();
							if(rset3.next())
							{
								isInvExist+=rset3.getInt(1);
							}
							rset3.close();
							stmt3.close();
							
							queryString3="SELECT COUNT(*) "
									+ "FROM FMS_PUR_PG_INV_MST "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
									+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND FINANCIAL_YEAR=? AND PDF_INV_DTL IS NOT NULL AND INV_FLAG=? ";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, own_cd);
							stmt3.setString(2, temp_countpty_cd);
							stmt3.setString(3, contno);
							stmt3.setString(4, agmtno);
							stmt3.setString(5, plant_seq);
							stmt3.setString(6, bu_plant_seq);
							stmt3.setString(7, cont_type);
							stmt3.setString(8, billing_cycle);
							stmt3.setString(9, period_start_dt);
							stmt3.setString(10, period_end_dt);
							stmt3.setString(11, financial_year);
							stmt3.setString(12, "F");
							rset3 = stmt3.executeQuery();
							if(rset3.next())
							{
								isInvExist+=rset3.getInt(1);
							}
							rset3.close();
							stmt3.close();
							
							if(isInvExist==0)
							{
								String tempEndDate=period_end_dt;
								String tempStratDate=period_start_dt;
								if(utilDate.getDays(cont_end_dt, period_end_dt) <= 0)
								{
									tempEndDate=cont_end_dt;
								}
								
								if(utilDate.getDays(period_start_dt,cont_start_dt) <= 1)
								{
									tempStratDate=cont_start_dt;
								}
								
								String holiday_state="";
								queryString1="SELECT HOLIDAY_STATE "
										+ "FROM FMS_TRADER_BILLING_DTL "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND AGMT_NO=? AND AGMT_REV=? "
										+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? ";
								stmt1=conn.prepareStatement(queryString1);
								stmt1.setString(1, comp_cd);
								stmt1.setString(2, temp_countpty_cd);
								stmt1.setString(3, agmtno);
								stmt1.setString(4, agmtrev);
								stmt1.setString(5, contno);
								stmt1.setString(6, cont_type);
								stmt1.setString(7, plant_seq);
								rset1=stmt1.executeQuery();
								if(rset1.next())
								{
									holiday_state=rset1.getString(1)==null?"":rset1.getString(1);
								}
								rset1.close();
								stmt1.close();
								
								String transportation_charges="", other_charges="",marketing_margin="";
								queryString1="SELECT CHARGE_RATE,CHARGE_ABBR "
										+ "FROM FMS_TRADER_CONT_PLANT_CHRG A "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND AGMT_NO=? AND AGMT_REV=? "
										+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? "
										+ "AND A.EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_TRADER_CONT_PLANT_CHRG B WHERE A.COMPANY_CD=B.COMPANY_CD "
										+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
										+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.CHARGE_ABBR=B.CHARGE_ABBR "
										+ "AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
								stmt1=conn.prepareStatement(queryString1);
								stmt1.setString(1, comp_cd);
								stmt1.setString(2, temp_countpty_cd);
								stmt1.setString(3, agmtno);
								stmt1.setString(4, agmtrev);
								stmt1.setString(5, contno);
								stmt1.setString(6, cont_type);
								stmt1.setString(7, plant_seq);
								stmt1.setString(8, period_end_dt);
								rset1=stmt1.executeQuery();
								while(rset1.next())
								{
									String charge_abbr=rset1.getString(2)==null?"":rset1.getString(2);
									if(charge_abbr.equals("TC"))
									{
										transportation_charges=rset1.getString(1)==null?"":nf2.format(rset1.getDouble(1));
									}
									else if(charge_abbr.equals("OC"))
									{
										other_charges=rset1.getString(1)==null?"":nf2.format(rset1.getDouble(1));
									}
									else if(charge_abbr.equals("MM"))
									{
										marketing_margin=rset1.getString(1)==null?"":nf2.format(rset1.getDouble(1));
									}
								}
								rset1.close();
								stmt1.close();
								
								double qtyMMBTU=0;
								
								if(split_flag.equals("Y"))
								{
									queryString3="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
											+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
											+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
											+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, tempStratDate);
									stmt3.setString(2, tempEndDate);
									rset3 = stmt3.executeQuery();
									while(rset3.next())
									{
										String date=rset3.getString(1)==null?"":rset3.getString(1);
										
										queryString5="SELECT COALESCE(ALLOCATION, SELLER_NOM, BUYER_NOM, DCQ, 0) "
												+ "FROM "
												+ "(SELECT (SELECT SUM(QTY_MMBTU) "
								  				+ "FROM FMS_BUY_DAILY_ALLOCATION A "
								  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
												+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
												+ "AND CONTRACT_TYPE=? AND BU_SEQ=? "
												+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
												+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
												+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
												+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
												+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
												+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
												+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) ALLOCATION, "
												+ "(SELECT SUM(QTY_MMBTU) "
								  				+ "FROM FMS_BUY_DAILY_SELLER_NOM A "
								  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
												+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
												+ "AND CONTRACT_TYPE=? AND BU_SEQ=? "
												+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
												+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_SELLER_NOM B "
												+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
												+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
												+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
												+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
												+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) SELLER_NOM, "
												+ "(SELECT SUM(QTY_MMBTU) "
								  				+ "FROM FMS_BUY_DAILY_BUYER_NOM A "
								  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
												+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
												+ "AND CONTRACT_TYPE=? AND BU_SEQ=? "
												+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
												+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_BUYER_NOM B "
												+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
												+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
												+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
												+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
												+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) BUYER_NOM, "
												+ "(NVL((SELECT DCQ "
												+ "FROM FMS_TRADER_CONT_DCQ_DTL "
												+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
												+ "AND AGMT_NO=? AND CONT_NO=? "
												+ "AND CONTRACT_TYPE=? AND STATUS=? "
												+ "AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT>=TO_DATE(?,'DD/MM/YYYY')),?)) DCQ FROM DUAL) ";
										stmt5 = conn.prepareStatement(queryString5);
										stmt5.setString(1, contno);
										stmt5.setString(2, agmtno);
										stmt5.setString(3, comp_cd);
										stmt5.setString(4, countpty_cd);
										stmt5.setString(5, cont_type);
										stmt5.setString(6, bu_plant_seq);
										stmt5.setString(7, date);
										stmt5.setString(8, contno);
										stmt5.setString(9, agmtno);
										stmt5.setString(10, comp_cd);
										stmt5.setString(11, countpty_cd);
										stmt5.setString(12, cont_type);
										stmt5.setString(13, bu_plant_seq);
										stmt5.setString(14, date);
										stmt5.setString(15, contno);
										stmt5.setString(16, agmtno);
										stmt5.setString(17, comp_cd);
										stmt5.setString(18, countpty_cd);
										stmt5.setString(19, cont_type);
										stmt5.setString(20, bu_plant_seq);
										stmt5.setString(21, date);
										stmt5.setString(22, comp_cd);
										stmt5.setString(23, countpty_cd);
										stmt5.setString(24, agmtno);
										stmt5.setString(25, contno);
										stmt5.setString(26, cont_type);
										stmt5.setString(27, "Y");
										stmt5.setString(28, date);
										stmt5.setString(29, date);
										stmt5.setString(30, dcq);
										rset5 = stmt5.executeQuery();
										
										if(rset5.next())
										{
											qtyMMBTU+=rset5.getDouble(1);
										}
										rset5.close();
										stmt5.close();
									}
									
									rset3.close();
									stmt3.close();
									
									if(qtyMMBTU>0 && !split_value.equals(""))
									{
										qtyMMBTU=(qtyMMBTU * (Double.parseDouble(""+split_value) / 100));
									}
									else
									{
										qtyMMBTU=0;
									}
								}
								else
								{
									queryString3="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
											+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
											+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
											+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, tempStratDate);
									stmt3.setString(2, tempEndDate);
									rset3 = stmt3.executeQuery();
									while(rset3.next())
									{
										String date=rset3.getString(1)==null?"":rset3.getString(1);
										
										queryString5="SELECT COALESCE(ALLOCATION, SELLER_NOM, BUYER_NOM, DCQ, 0) "
												+ "FROM "
												+ "(SELECT (SELECT SUM(QTY_MMBTU) "
								  				+ "FROM FMS_BUY_DAILY_ALLOCATION A "
								  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
												+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
												+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
												+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
												+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
												+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
												+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
												+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
												+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
												+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) ALLOCATION, "
												+ "(SELECT SUM(QTY_MMBTU) "
								  				+ "FROM FMS_BUY_DAILY_SELLER_NOM A "
								  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
												+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
												+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
												+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
												+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_SELLER_NOM B "
												+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
												+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
												+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
												+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
												+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) SELLER_NOM, "
												+ "(SELECT SUM(QTY_MMBTU) "
								  				+ "FROM FMS_BUY_DAILY_BUYER_NOM A "
								  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
												+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
												+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
												+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
												+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_BUYER_NOM B "
												+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
												+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
												+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
												+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
												+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) BUYER_NOM, "
												+ "(NVL((SELECT DCQ "
												+ "FROM FMS_TRADER_CONT_DCQ_DTL "
												+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
												+ "AND AGMT_NO=? AND CONT_NO=? "
												+ "AND CONTRACT_TYPE=? AND STATUS=? "
												+ "AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT>=TO_DATE(?,'DD/MM/YYYY')),?)) DCQ FROM DUAL) ";
										stmt5 = conn.prepareStatement(queryString5);
										stmt5.setString(1, contno);
										stmt5.setString(2, agmtno);
										stmt5.setString(3, comp_cd);
										stmt5.setString(4, countpty_cd);
										stmt5.setString(5, plant_seq);
										stmt5.setString(6, cont_type);
										stmt5.setString(7, bu_plant_seq);
										stmt5.setString(8, date);
										stmt5.setString(9, contno);
										stmt5.setString(10, agmtno);
										stmt5.setString(11, comp_cd);
										stmt5.setString(12, countpty_cd);
										stmt5.setString(13, plant_seq);
										stmt5.setString(14, cont_type);
										stmt5.setString(15, bu_plant_seq);
										stmt5.setString(16, date);
										stmt5.setString(17, contno);
										stmt5.setString(18, agmtno);
										stmt5.setString(19, comp_cd);
										stmt5.setString(20, countpty_cd);
										stmt5.setString(21, plant_seq);
										stmt5.setString(22, cont_type);
										stmt5.setString(23, bu_plant_seq);
										stmt5.setString(24, date);
										stmt5.setString(25, comp_cd);
										stmt5.setString(26, countpty_cd);
										stmt5.setString(27, agmtno);
										stmt5.setString(28, contno);
										stmt5.setString(29, cont_type);
										stmt5.setString(30, "Y");
										stmt5.setString(31, date);
										stmt5.setString(32, date);
										stmt5.setString(33, dcq);
										rset5 = stmt5.executeQuery();
										if(rset5.next())
										{
											qtyMMBTU+=rset5.getDouble(1);
										}
										rset5.close();
										stmt5.close();
									}
									rset3.close();
									stmt3.close();
								}
								
								String new_price="";
								queryString4 = "SELECT DISTINCT NEW_SALE_PRICE "
										+ "FROM FMS_SUPPLY_ALLOC_REVISED A "
										+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND CONT_NO=? "
										//+ "AND COUNTERPARTY_CD='"+temp_countpty_cd+"' "
										+ "AND FLAG='A' AND CONTRACT_TYPE=? "
										+ "AND MODIFICATION_SEQ_NO = (SELECT MAX(MODIFICATION_SEQ_NO) FROM FMS_SUPPLY_ALLOC_REVISED B "
										+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
										+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.FLAG=B.FLAG AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
										+ "AND B.NEW_PRICE_EFF_DT <=TO_DATE(?,'DD/MM/YYYY'))";
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1, comp_cd);
								stmt4.setString(2, agmtno);
								stmt4.setString(3, contno);
								stmt4.setString(4, cont_type);
								stmt4.setString(5, period_end_dt);
								rset4 = stmt4.executeQuery();
								if(rset4.next())
								{
									new_price=rset4.getString(1)==null?"":rset4.getString(1);
									if(!new_price.equals(""))
									{
										price=new_price;
									}
								}
								rset4.close();
								stmt4.close();
								
								double exchng_rate=0;
								String exchng_rate_dt="";
								if(exchng_rate_cd.equals("0")) //FOR FIXED EXCHANGE RATE
								{
									exchng_rate=Double.parseDouble(fixed_exchng_val);
								}
								else
								{
									queryString4="SELECT NVL(EXCHG_VAL,0),TO_CHAR(EFF_DT,'DD/MM/YYYY') "
											+ "FROM FMS_EXCHG_RATE_ENTRY A "
											+ "WHERE EXCHG_RATE_CD=? "
											+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY') "
											+ "ORDER BY EFF_DT DESC";
									stmt4 = conn.prepareStatement(queryString4);
									stmt4.setString(1, exchng_rate_cd);
									stmt4.setString(2, period_end_dt);
									rset4 = stmt4.executeQuery();
									if(rset4.next())
									{
										exchng_rate=rset4.getDouble(1);
										exchng_rate_dt=rset4.getString(2)==null?"":rset4.getString(2);
									}
									rset4.close();
									stmt5.close();
								}
								
								double accrual_amt=0;
								double charges_amt=0;
								double gross_amt=0;
								
								if(!transportation_charges.equals(""))
								{
									charges_amt+= qtyMMBTU * Double.parseDouble(transportation_charges);
								}
								
								if(!marketing_margin.equals(""))
								{
									charges_amt+= qtyMMBTU * Double.parseDouble(marketing_margin);
								}
								
								if(!other_charges.equals(""))
								{
									charges_amt+= qtyMMBTU * Double.parseDouble(other_charges);
								}
								
								/*USD=USD need to convert in INR
								if(price_unit.equals(invoice_raise_in))
								{
									accrual_amt=qtyMMBTU * Double.parseDouble(price);
									gross_amt = accrual_amt;
								}
								else*/
								{
									accrual_amt=qtyMMBTU * Double.parseDouble(price);
									if(price_unit.equals("2") && charges_amt > 0)
									{
										accrual_amt+=charges_amt / exchng_rate;
									}
									else
									{
										accrual_amt+=charges_amt;
									}

									if(price_unit.equals("2"))
									{
										gross_amt = accrual_amt * exchng_rate;
									}
									else
									{
										gross_amt = accrual_amt;
									}
								}
								
								if(qtyMMBTU>0 && accrual_amt>0)
								{
									VFINANCIAL_YEAR.add(financial_year);
									VCOUNTERPTY_CD.add(temp_countpty_cd);
									VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,temp_countpty_cd));
									VCOUNTERPTY_NM.add(""+utilBean.getCounterpartyName(conn,temp_countpty_cd));
									VAGMT_NO.add(agmtno);
									VAGMT_REV_NO.add(agmtrev);
									VCONT_NO.add(contno);
									VCONT_REV_NO.add(contrev);
									VCARGO_NO.add("0");
									VBOE_NO.add("0");
									VBOE_NM.add("");
									VINV_FLAG.add("F");
									VCONTRACT_TYPE.add(cont_type);
									VSTART_DT.add(rset.getString(7)==null?"":rset.getString(7));
									VEND_DT.add(rset.getString(8)==null?"":rset.getString(8));
									VDIS_CONT_MAPPING.add(deal_no);
									VCONT_REF_NO.add(cont_ref_no);
									VPLANT_SEQ.add(plant_seq);
									VPLANT_ABBR.add(plant_abbr);
									VBU_PLANT_SEQ.add(bu_plant_seq);
									VBU_PLANT_ABBR.add(bu_plant_abbr);
									VPERIOD_START_DT.add(period_start_dt);
									VPERIOD_END_DT.add(period_end_dt);
									VBILLING_FREQ_FLAG.add(billing_cycle);
									VBILLING_FREQ_NM.add(billing_freq_nm);
									VPRODUCTION_MONTH.add(month+"/"+year);
									VINVOICE_DUE_DT.add(utilBean.DueDateCalculation(conn,period_end_dt, due_days,consider_due_dt_in,exclude_sat,comp_cd,holiday_state));
									VSPLIT_FLAG.add(split_flag);
									if(split_flag.equals("Y"))
									{	
										VSPLIT_VALUE.add(split_value);
									}
									else
									{
										VSPLIT_VALUE.add("");
									}
									VCASH_FLOW.add("Commodity");
									
									if(price_unit.equals("2"))
									{
										VEXCHNG_RATE.add(nf2.format(exchng_rate));
										VEXCHNG_RATE_CD.add(exchng_rate_cd);
										VEXCHNG_RATE_DT.add(exchng_rate_dt);
									}
									else
									{
										VEXCHNG_RATE.add("");
										VEXCHNG_RATE_CD.add("");
										VEXCHNG_RATE_DT.add("");
									}
									
									VACCRUAL_QTY.add(nf.format(qtyMMBTU));
									VACCRUAL_AMT.add(nf.format(accrual_amt));
									
									VSALES_PRICE_CD.add(price_unit);
									VSALES_PRICE_NM.add(price_unit_nm);
									VINVOICE_RAISED_IN.add(invoice_raise_in);
									
									VGROSS_AMT.add(nf.format(gross_amt));
								}
							}
						}
					}
					rset2.close();
					stmt2.close();
				}
				rset.close();
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getPurchaseLTCORAAccrualList()
	{
		String function_nm="getPurchaseLTCORAAccrualList()";
		try
		{
			financial_year=utilDate.getFinancialYear(period_end_dt);
			
			if(billing_cycle.equals("8"))
			{
			}
			else
			{
				int cont=0;
				
				String queryString="SELECT DISTINCT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),TO_CHAR(TO_DATE(B.ACTUAL_RECPT_DT,'DD/MM/YY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0),'DD/MM/YYYY'),"
						+ "A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,C.DUE_DATE,C.DUE_DT_IN,C.EXCL_SAT_MAP,"
						+ "B.CSOC_QTY,A.LTCORA_TARIFF,A.LTCORA_TARIFF_UNIT,C.INVOICE_CUR_CD,C.EXCHNG_RATE_CD,C.EXCHNG_RATE_CAL,C.EXCHG_VAL,"
						+ "B.CARGO_NO,D.PLANT_SEQ_NO,E.PLANT_SEQ_NO "
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B,"
							+ "FMS_LTCORA_CONT_BILLING_DTL C,"
							+ "FMS_LTCORA_CONT_BU D,"
							+ "FMS_LTCORA_CONT_PLANT E "
						+ "WHERE A.COMPANY_CD=? AND A.BUY_SALE=? AND B.CARGO_STATUS=? AND A.AGMT_TYPE=? "
						+ "AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0) >=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0) >=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ ""
						+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV "
						+ "AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE AND A.BUY_SALE=D.BUY_SALE AND A.AGMT_TYPE=D.AGMT_TYPE "
						+ ""
						+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO AND A.CONT_REV=E.CONT_REV "
						+ "AND A.AGMT_NO=E.AGMT_NO AND A.AGMT_REV=E.AGMT_REV AND A.CONTRACT_TYPE=E.CONTRACT_TYPE AND A.BUY_SALE=E.BUY_SALE AND A.AGMT_TYPE=E.AGMT_TYPE "
						+ ""
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE "
						+ ""
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "//AND A.CONT_REV=C.CONT_REV "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.BUY_SALE=C.BUY_SALE AND A.AGMT_TYPE=C.AGMT_TYPE "
						+ "AND C.BILLING_FREQ=? ";
				//HP20250405 REMOVING CONT_REV CHECKING FOR BILLING DETAIL FROM THE QUERY CONDITION AS CONFIRMED BY JD MAM
				stmt=conn.prepareStatement(queryString);
				stmt.setString(++cont, comp_cd);
				stmt.setString(++cont, "T");
				stmt.setString(++cont, "Y");
				stmt.setString(++cont, "L");
				stmt.setString(++cont, period_end_dt);
				stmt.setString(++cont, period_start_dt);
				stmt.setString(++cont, report_end_dt);
				stmt.setString(++cont, report_start_dt);
				stmt.setString(++cont, billing_freq);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					String cont_start_dt=rset.getString(7)==null?"":rset.getString(7);
					String cont_end_dt=rset.getString(8)==null?"":rset.getString(8);
					String cont_ref_no=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					
					String due_days=rset.getString(12)==null?"":rset.getString(12);
					String consider_due_dt_in=rset.getString(13)==null?"":rset.getString(13);
					String exclude_sat=rset.getString(14)==null?"":rset.getString(14);
					String dcq=rset.getString(15)==null?"":rset.getString(15);
					String price=rset.getString(16)==null?"":rset.getString(16);
					String price_unit=rset.getString(17)==null?"":rset.getString(17);
					String invoice_raise_in=rset.getString(18)==null?"":rset.getString(18);
					String price_unit_nm=utilBean.getRateUnitNm(conn,price_unit);
					String exchng_rate_cd=rset.getString(19)==null?"":rset.getString(19);
					String exchng_rate_cal=rset.getString(20)==null?"":rset.getString(20);
					String fixed_exchng_val=nf2.format(rset.getDouble(21));
					String cargo_no=rset.getString(22)==null?"":rset.getString(22);
					
					String bu_plant_seq = rset.getString(23)==null?"":rset.getString(23);
					String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
					String plant_seq = rset.getString(24)==null?"":rset.getString(24);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "T");
					
					String deal_no=utilBean.NewDealMappingId(comp_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, cargo_no);
					
					int isGreter=utilDate.getDays(cont_start_dt, period_start_dt);
					
					String temp_st_dt="";
					String temp_end_dt="";
					if(isGreter>1)
					{
						temp_st_dt=cont_start_dt;
						temp_end_dt=period_end_dt;
					}
					else
					{
						temp_st_dt=period_start_dt;
						temp_end_dt=period_end_dt;
					}
					
					if(utilDate.getDays(cont_end_dt, temp_end_dt)<=0)
					{
						temp_end_dt=cont_end_dt;
					}
					
					int isInvExist=0;
					queryString3="SELECT ((SELECT COUNT(*) "
							+ "FROM FMS_PUR_SG_INV_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
							+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND FINANCIAL_YEAR=? AND PDF_INV_DTL IS NOT NULL AND INV_FLAG=? AND CARGO_NO=?) ";
					queryString3+="+ (SELECT COUNT(*) "
							+ "FROM FMS_PUR_PG_INV_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
							+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND FINANCIAL_YEAR=? AND PDF_INV_DTL IS NOT NULL AND INV_FLAG=? AND CARGO_NO=?)) FROM DUAL ";
					int st_count=0;
					stmt3=conn.prepareStatement(queryString3);
					stmt3.setString(++st_count, own_cd);
					stmt3.setString(++st_count, countpty_cd);
					stmt3.setString(++st_count, contno);
					stmt3.setString(++st_count, agmtno);
					stmt3.setString(++st_count, plant_seq);
					stmt3.setString(++st_count, bu_plant_seq);
					stmt3.setString(++st_count, cont_type);
					stmt3.setString(++st_count, billing_cycle);
					stmt3.setString(++st_count, temp_st_dt);
					stmt3.setString(++st_count, temp_end_dt);
					stmt3.setString(++st_count, financial_year);
					stmt3.setString(++st_count, "F");
					stmt3.setString(++st_count, cargo_no);
					stmt3.setString(++st_count, own_cd);
					stmt3.setString(++st_count, countpty_cd);
					stmt3.setString(++st_count, contno);
					stmt3.setString(++st_count, agmtno);
					stmt3.setString(++st_count, plant_seq);
					stmt3.setString(++st_count, bu_plant_seq);
					stmt3.setString(++st_count, cont_type);
					stmt3.setString(++st_count, billing_cycle);
					stmt3.setString(++st_count, temp_st_dt);
					stmt3.setString(++st_count, temp_end_dt);
					stmt3.setString(++st_count, financial_year);
					stmt3.setString(++st_count, "F");
					stmt3.setString(++st_count, cargo_no);
					rset3=stmt3.executeQuery();
					if(rset3.next())
					{
						isInvExist+=rset3.getInt(1);
					}
					rset3.close();
					stmt3.close();
					
					if(isInvExist==0)
					{
						String holiday_state="";
						String queryString1="SELECT HOLIDAY_STATE "
								+ "FROM FMS_LTCORA_CONT_BILLING_DTL "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? AND BUY_SALE=? AND AGMT_TYPE=? ";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, countpty_cd);
						stmt1.setString(3, agmtno);
						stmt1.setString(4, agmtrev);
						stmt1.setString(5, contno);
						stmt1.setString(6, cont_type);
						stmt1.setString(7, plant_seq);
						stmt1.setString(8, "T");
						stmt1.setString(9, "L");
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							holiday_state=rset1.getString(1)==null?"":rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						double qtyMMBTU=0;
						queryString3="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
								+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
								+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
								+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
						stmt3=conn.prepareStatement(queryString3);
						stmt3.setString(1, temp_st_dt);
						stmt3.setString(2, temp_end_dt);
						rset3=stmt3.executeQuery();
						while(rset3.next())
						{
							String date=rset3.getString(1)==null?"":rset3.getString(1);
							
							String queryString5="SELECT COALESCE(ALLOCATION, SELLER_NOM, BUYER_NOM, DCQ, 0) "
									+ "FROM "
									+ "(SELECT (SELECT SUM(QTY_MMBTU) "
					  				+ "FROM FMS_BUY_DAILY_ALLOCATION A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? AND CARGO_NO=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) ALLOCATION, "
									+ "(SELECT SUM(QTY_MMBTU) "
					  				+ "FROM FMS_BUY_DAILY_SELLER_NOM A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? AND CARGO_NO=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_SELLER_NOM B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) SELLER_NOM, "
									+ "(SELECT SUM(QTY_MMBTU) "
					  				+ "FROM FMS_BUY_DAILY_BUYER_NOM A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? AND CARGO_NO=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_BUYER_NOM B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) BUYER_NOM, "
									+ "(NVL((SELECT CSOC "
									+ "FROM FMS_LTCORA_CONT_CARGO_CSOC "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? AND CONT_NO=? AND CARGO_NO=? "
									+ "AND CONTRACT_TYPE=? AND BUY_SALE=? AND AGMT_TYPE=? "
									+ "AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT>=TO_DATE(?,'DD/MM/YYYY')),?)) DCQ FROM DUAL) ";
							String temp_queryString5=queryString5;
							stmt5=conn.prepareStatement(temp_queryString5);
							stmt5.setString(1, contno);
							stmt5.setString(2, agmtno);
							stmt5.setString(3, comp_cd);
							stmt5.setString(4, countpty_cd);
							stmt5.setString(5, plant_seq);
							stmt5.setString(6, cont_type);
							stmt5.setString(7, bu_plant_seq);
							stmt5.setString(8, cargo_no);
							stmt5.setString(9, date);
							stmt5.setString(10, contno);
							stmt5.setString(11, agmtno);
							stmt5.setString(12, comp_cd);
							stmt5.setString(13, countpty_cd);
							stmt5.setString(14, plant_seq);
							stmt5.setString(15, cont_type);
							stmt5.setString(16, bu_plant_seq);
							stmt5.setString(17, cargo_no);
							stmt5.setString(18, date);
							stmt5.setString(19, contno);
							stmt5.setString(20, agmtno);
							stmt5.setString(21, comp_cd);
							stmt5.setString(22, countpty_cd);
							stmt5.setString(23, plant_seq);
							stmt5.setString(24, cont_type);
							stmt5.setString(25, bu_plant_seq);
							stmt5.setString(26, cargo_no);
							stmt5.setString(27, date);
							stmt5.setString(28, comp_cd);
							stmt5.setString(29, countpty_cd);
							stmt5.setString(30, agmtno);
							stmt5.setString(31, contno);
							stmt5.setString(32, cargo_no);
							stmt5.setString(33, cont_type);
							stmt5.setString(34, "T");
							stmt5.setString(35, "L");
							stmt5.setString(36, date);
							stmt5.setString(37, date);
							stmt5.setString(38, dcq);
							rset5=stmt5.executeQuery();
							if(rset5.next())
							{
								qtyMMBTU+=rset5.getDouble(1);
							}
							rset5.close();
							stmt5.close();
						}
						rset3.close();
						stmt3.close();
						
						double exchng_rate=0;
						String exchng_rate_dt="";
						if(exchng_rate_cd.equals("0")) //FOR FIXED EXCHANGE RATE
						{
							exchng_rate=Double.parseDouble(fixed_exchng_val);
						}
						else
						{
							queryString3="SELECT NVL(EXCHG_VAL,0),TO_CHAR(EFF_DT,'DD/MM/YYYY') "
									+ "FROM FMS_EXCHG_RATE_ENTRY A "
									+ "WHERE EXCHG_RATE_CD=? "
									+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY') "
									+ "ORDER BY EFF_DT DESC";
							stmt3=conn.prepareStatement(queryString3);
							stmt3.setString(1, exchng_rate_cd);
							stmt3.setString(2, temp_end_dt);
							rset3=stmt3.executeQuery();
							if(rset3.next())
							{
								exchng_rate=rset3.getDouble(1);
								exchng_rate_dt=rset3.getString(2)==null?"":rset3.getString(2);
							}
							rset3.close();
							stmt3.close();
						}
						
						double accrual_amt=0;
						double gross_amt=0;
						
						accrual_amt=qtyMMBTU * Double.parseDouble(price);

						if(price_unit.equals("2"))
						{
							gross_amt = accrual_amt * exchng_rate;
						}
						else
						{
							gross_amt = accrual_amt;
						}
						
						if(qtyMMBTU>0 && accrual_amt > 0)
						{
							VFINANCIAL_YEAR.add(financial_year);
							VCOUNTERPTY_CD.add(countpty_cd);
							VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
							VCOUNTERPTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
							VAGMT_NO.add(agmtno);
							VAGMT_REV_NO.add(agmtrev);
							VCONT_NO.add(contno);
							VCONT_REV_NO.add(contrev);
							VCONTRACT_TYPE.add(cont_type);
							VCARGO_NO.add(cargo_no);
							VBOE_NO.add("0");
							VBOE_NM.add("");
							VINV_FLAG.add("F");
							VSTART_DT.add(cont_start_dt);
							VEND_DT.add(cont_end_dt);
							VDIS_CONT_MAPPING.add(deal_no);
							VCONT_REF_NO.add(cont_ref_no);
							VPLANT_SEQ.add(plant_seq);
							VPLANT_ABBR.add(plant_abbr);
							VBU_PLANT_SEQ.add(bu_plant_seq);
							VBU_PLANT_ABBR.add(bu_plant_abbr);
							VPERIOD_START_DT.add(temp_st_dt);
							VPERIOD_END_DT.add(temp_end_dt);
							VBILLING_FREQ_FLAG.add(billing_cycle);
							VBILLING_FREQ_NM.add(billing_freq_nm);
							VPRODUCTION_MONTH.add(month+"/"+year);
							VINVOICE_DUE_DT.add(utilBean.DueDateCalculation(conn,temp_end_dt, due_days,consider_due_dt_in,exclude_sat,comp_cd,holiday_state));
							
							VSPLIT_FLAG.add("");
							VSPLIT_VALUE.add("");
							
							VCASH_FLOW.add("Capacity");
							
							VACCRUAL_QTY.add(nf.format(qtyMMBTU));
							VACCRUAL_AMT.add(nf.format(accrual_amt));
							
							VSALES_PRICE_CD.add(price_unit);
							VSALES_PRICE_NM.add(price_unit_nm);
							VINVOICE_RAISED_IN.add(invoice_raise_in);
							
							if(price_unit.equals("2"))
							{
								VEXCHNG_RATE.add(nf2.format(exchng_rate));
								VEXCHNG_RATE_CD.add(exchng_rate_cd);
								VEXCHNG_RATE_DT.add(exchng_rate_dt);
							}
							else
							{
								VEXCHNG_RATE.add("");
								VEXCHNG_RATE_CD.add("");
								VEXCHNG_RATE_DT.add("");
							}
							VGROSS_AMT.add(nf.format(gross_amt));
						}
					}
				}
				rset.close();
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getPurchaseCargoAccrualList()
	{
		String function_nm="getPurchaseCargoAccrualList()";
		try
		{
			String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
			String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
			
			financial_year=utilDate.getFinancialYear(temp_period_end_dt);
			
			String queryString="SELECT DISTINCT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
					+ "TO_CHAR(B.START_DT,'DD/MM/YYYY'),TO_CHAR(B.END_DT,'DD/MM/YYYY'),A.CONT_NAME,B.CARGO_REF,A.CONTRACT_TYPE,E.DUE_DATE,E.DUE_DT_IN,E.EXCL_SAT_MAP,"
					+ "B.CARGO_QTY,B.RATE,B.RATE_UNIT,E.INVOICE_CUR_CD,E.EXCHNG_RATE_CD,E.EXCHNG_RATE_CAL,E.EXCHG_VAL,"
					+ "B.CARGO_NO,C.PLANT_SEQ_NO,D.PLANT_SEQ_NO,NULL,"
					+ "(SELECT NVL(COUNT(*),0) FROM FMS_TRADER_CONT_BU X, FMS_TRADER_CONT_PLANT Y WHERE "
					+ "A.COMPANY_CD=X.COMPANY_CD AND A.COUNTERPARTY_CD=X.COUNTERPARTY_CD AND A.CONTRACT_TYPE=X.CONTRACT_TYPE "
					+ "AND A.CONT_NO=X.CONT_NO AND A.CONT_REV=X.CONT_REV AND A.AGMT_NO=X.AGMT_NO AND A.AGMT_REV=X.AGMT_REV "
					+ "AND Y.COMPANY_CD=X.COMPANY_CD AND Y.COUNTERPARTY_CD=X.COUNTERPARTY_CD AND Y.CONTRACT_TYPE=X.CONTRACT_TYPE "
					+ "AND Y.CONT_NO=X.CONT_NO AND Y.CONT_REV=X.CONT_REV AND Y.AGMT_NO=X.AGMT_NO AND Y.AGMT_REV=X.AGMT_REV),'P' "
					+ "FROM FMS_TRADER_CN_MST A, "
						+ "FMS_TRADER_CARGO_MST B,"
						+ "FMS_TRADER_CONT_BU C,"
						+ "FMS_TRADER_CONT_PLANT D,"
						+ "FMS_TRADER_BILLING_DTL E "
						+ "WHERE A.COMPANY_CD=? AND B.CARGO_STATUS=? AND B.CARGO_QTY > 0 "
						//+ "AND B.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND B.END_DT >=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND B.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND B.END_DT >=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONTRACT_TYPE=C.CONTRACT_TYPE "
						+ "AND A.CONT_NO=C.CONT_NO AND A.CONT_REV=C.CONT_REV AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV "
						+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.CONTRACT_TYPE=D.CONTRACT_TYPE "
						+ "AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV "
						+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO "
						+ "AND A.AGMT_NO=E.AGMT_NO AND A.CONTRACT_TYPE=E.CONTRACT_TYPE AND E.BILLING_FREQ=? "
						+ "AND (SELECT NVL(COUNT(*),0) FROM FMS_BUY_CARGO_NOM X, FMS_BUY_CARGO_NOM_BOE Y "
						+ "WHERE A.COMPANY_CD=X.COMPANY_CD AND A.COUNTERPARTY_CD=X.COUNTERPARTY_CD AND A.CONTRACT_TYPE=X.CONTRACT_TYPE "
						+ "AND A.CONT_NO=X.CONT_NO AND A.AGMT_NO=X.AGMT_NO AND A.AGMT_TYPE=X.AGMT_TYPE AND B.CARGO_NO=X.CARGO_NO "
						+ "AND X.NOM_REV = (SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM B WHERE X.COMPANY_CD=B.COMPANY_CD "
						+ "AND X.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND X.CONT_NO=B.CONT_NO AND X.AGMT_NO=B.AGMT_NO "
						+ "AND X.CONTRACT_TYPE=B.CONTRACT_TYPE AND X.CARGO_NO=B.CARGO_NO AND X.AGMT_TYPE=B.AGMT_TYPE) "
						+ "AND Y.COMPANY_CD=X.COMPANY_CD AND Y.COUNTERPARTY_CD=X.COUNTERPARTY_CD AND Y.CONTRACT_TYPE=X.CONTRACT_TYPE "
						+ "AND Y.CONT_NO=X.CONT_NO AND Y.AGMT_NO=X.AGMT_NO AND Y.AGMT_TYPE=X.AGMT_TYPE AND Y.CARGO_NO=X.CARGO_NO) = 0 ";
			queryString+="UNION ALL ";
			queryString+="SELECT DISTINCT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
					+ "TO_CHAR(C.EXP_FROM_DT,'DD/MM/YYYY'),TO_CHAR(C.EXP_TO_DT,'DD/MM/YYYY'),A.CONT_NAME,B.CARGO_REF,A.CONTRACT_TYPE,E.DUE_DATE,E.DUE_DT_IN,E.EXCL_SAT_MAP,"
					+ "D.BOE_QTY,D.BOE_PRICE,D.BOE_PRICE_UNIT,E.INVOICE_CUR_CD,E.EXCHNG_RATE_CD,E.EXCHNG_RATE_CAL,E.EXCHG_VAL,"
					+ "B.CARGO_NO,D.BU_SEQ,D.PLANT_SEQ,D.BOE_NO,0,'P' "
					+ "FROM FMS_TRADER_CN_MST A, "
						+ "FMS_TRADER_CARGO_MST B, "
						+ "FMS_BUY_CARGO_NOM C,"
						+ "FMS_BUY_CARGO_NOM_BOE D, "
						+ "FMS_TRADER_BILLING_DTL E "
					+ "WHERE A.COMPANY_CD=? AND B.CARGO_STATUS=? AND D.BOE_QTY > 0 "
					//+ "AND C.EXP_FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND C.EXP_TO_DT >=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND C.EXP_FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND C.EXP_TO_DT >=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONTRACT_TYPE=C.CONTRACT_TYPE "
					+ "AND A.CONT_NO=C.CONT_NO AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_TYPE=C.AGMT_TYPE AND B.CARGO_NO=C.CARGO_NO "
					+ "AND C.NOM_REV = (SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM B WHERE C.COMPANY_CD=B.COMPANY_CD "
					+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.CONT_NO=B.CONT_NO AND C.AGMT_NO=B.AGMT_NO "
					+ "AND C.CONTRACT_TYPE=B.CONTRACT_TYPE AND C.CARGO_NO=B.CARGO_NO AND C.AGMT_TYPE=B.AGMT_TYPE) "
					+ "AND D.COMPANY_CD=C.COMPANY_CD AND D.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND D.CONTRACT_TYPE=C.CONTRACT_TYPE "
					+ "AND D.CONT_NO=C.CONT_NO AND D.AGMT_TYPE=C.AGMT_TYPE AND D.AGMT_NO=C.AGMT_NO AND D.CARGO_NO=C.CARGO_NO AND D.NOM_REV=C.NOM_REV "
					+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO "
					+ "AND A.AGMT_NO=E.AGMT_NO AND A.CONTRACT_TYPE=E.CONTRACT_TYPE AND E.BILLING_FREQ=? "
					+ "AND (SELECT NVL(COUNT(*),0) FROM FMS_BUY_CARGO_ALLOC X, FMS_BUY_CARGO_ALLOC_BOE Y "
					+ "WHERE A.COMPANY_CD=X.COMPANY_CD AND A.COUNTERPARTY_CD=X.COUNTERPARTY_CD AND A.CONTRACT_TYPE=X.CONTRACT_TYPE "
					+ "AND A.CONT_NO=X.CONT_NO AND A.AGMT_NO=X.AGMT_NO AND A.AGMT_TYPE=X.AGMT_TYPE AND B.CARGO_NO=X.CARGO_NO "
					+ "AND X.ALLOC_REV = (SELECT MAX(B.ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC B WHERE X.COMPANY_CD=B.COMPANY_CD "
					+ "AND X.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND X.CONT_NO=B.CONT_NO AND X.AGMT_NO=B.AGMT_NO "
					+ "AND X.CONTRACT_TYPE=B.CONTRACT_TYPE AND X.CARGO_NO=B.CARGO_NO AND X.AGMT_TYPE=B.AGMT_TYPE) "
					+ "AND Y.COMPANY_CD=X.COMPANY_CD AND Y.COUNTERPARTY_CD=X.COUNTERPARTY_CD AND Y.CONTRACT_TYPE=X.CONTRACT_TYPE "
					+ "AND Y.CONT_NO=X.CONT_NO AND Y.AGMT_TYPE=X.AGMT_TYPE AND Y.AGMT_NO=X.AGMT_NO AND Y.CARGO_NO=X.CARGO_NO "
					+ "AND Y.ALLOC_REV=X.ALLOC_REV AND Y.BOE_NO=D.BOE_NO AND Y.ACT_BOE_QTY IS NOT NULL) = 0 ";
			queryString+="UNION ALL ";
			queryString+="SELECT DISTINCT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
					+ "TO_CHAR(C.EXP_FROM_DT,'DD/MM/YYYY'),TO_CHAR(C.EXP_TO_DT,'DD/MM/YYYY'),A.CONT_NAME,B.CARGO_REF,A.CONTRACT_TYPE,E.DUE_DATE,E.DUE_DT_IN,E.EXCL_SAT_MAP,"
					+ "Y.ACT_BOE_QTY,Y.BOE_PROVISIONAL_PRICE,Y.BOE_PROVISIONAL_PRICE_UNIT,E.INVOICE_CUR_CD,E.EXCHNG_RATE_CD,E.EXCHNG_RATE_CAL,E.EXCHG_VAL,"
					+ "B.CARGO_NO,Y.BU_SEQ,Y.PLANT_SEQ,Y.BOE_NO,0,'P' "
					+ "FROM FMS_TRADER_CN_MST A, "
						+ "FMS_TRADER_CARGO_MST B, "
						+ "FMS_BUY_CARGO_NOM C,"
						+ "FMS_BUY_CARGO_ALLOC X,"
						+ "FMS_BUY_CARGO_ALLOC_BOE Y,"
						+ "FMS_TRADER_BILLING_DTL E "
					+ "WHERE A.COMPANY_CD=? AND B.CARGO_STATUS=? AND Y.ACT_BOE_QTY > 0 "
					//+ "AND C.EXP_FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND C.EXP_TO_DT >=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND C.EXP_FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND C.EXP_TO_DT >=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONTRACT_TYPE=C.CONTRACT_TYPE "
					+ "AND A.CONT_NO=C.CONT_NO AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_TYPE=C.AGMT_TYPE AND B.CARGO_NO=C.CARGO_NO "
					+ "AND C.NOM_REV = (SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM B WHERE C.COMPANY_CD=B.COMPANY_CD "
					+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.CONT_NO=B.CONT_NO AND C.AGMT_NO=B.AGMT_NO "
					+ "AND C.CONTRACT_TYPE=B.CONTRACT_TYPE AND C.CARGO_NO=B.CARGO_NO AND C.AGMT_TYPE=B.AGMT_TYPE) "
					+ "AND A.COMPANY_CD=X.COMPANY_CD AND A.COUNTERPARTY_CD=X.COUNTERPARTY_CD AND A.CONTRACT_TYPE=X.CONTRACT_TYPE "
					+ "AND A.CONT_NO=X.CONT_NO AND A.AGMT_NO=X.AGMT_NO AND A.AGMT_TYPE=X.AGMT_TYPE AND B.CARGO_NO=X.CARGO_NO "
					+ "AND X.ALLOC_REV = (SELECT MAX(B.ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC B WHERE X.COMPANY_CD=B.COMPANY_CD "
					+ "AND X.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND X.CONT_NO=B.CONT_NO AND X.AGMT_NO=B.AGMT_NO "
					+ "AND X.CONTRACT_TYPE=B.CONTRACT_TYPE AND X.CARGO_NO=B.CARGO_NO AND X.AGMT_TYPE=B.AGMT_TYPE) "
					+ "AND Y.COMPANY_CD=X.COMPANY_CD AND Y.COUNTERPARTY_CD=X.COUNTERPARTY_CD AND Y.CONTRACT_TYPE=X.CONTRACT_TYPE "
					+ "AND Y.CONT_NO=X.CONT_NO AND Y.AGMT_TYPE=X.AGMT_TYPE AND Y.AGMT_NO=X.AGMT_NO AND Y.CARGO_NO=X.CARGO_NO AND Y.ALLOC_REV=X.ALLOC_REV "
					+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO "
					+ "AND A.AGMT_NO=E.AGMT_NO AND A.CONTRACT_TYPE=E.CONTRACT_TYPE AND E.BILLING_FREQ=? "
					+ "AND ((SELECT NVL(COUNT(*),0) FROM FMS_PUR_SG_INV_MST Z "
					+ "WHERE Z.COMPANY_CD=A.COMPANY_CD AND Z.COUNTERPARTY_CD=A.COUNTERPARTY_CD AND Z.CONT_NO=A.CONT_NO "
					+ "AND Z.AGMT_NO=A.AGMT_NO AND Z.PLANT_SEQ=Y.PLANT_SEQ AND Z.BU_UNIT=Y.BU_SEQ AND Z.CONTRACT_TYPE=A.CONTRACT_TYPE "
					+ "AND Z.FREQ=? AND Z.PERIOD_START_DT=C.EXP_FROM_DT AND Z.PERIOD_END_DT=C.EXP_TO_DT "
					+ "AND Z.FINANCIAL_YEAR=? AND Z.PDF_INV_DTL IS NOT NULL AND Z.INV_FLAG=? AND Z.CARGO_NO=B.CARGO_NO AND Z.BOE_NO=Y.BOE_NO) = 0 "
					+ "AND (SELECT NVL(COUNT(*),0) FROM FMS_PUR_PG_INV_MST Z "
					+ "WHERE Z.COMPANY_CD=A.COMPANY_CD AND Z.COUNTERPARTY_CD=A.COUNTERPARTY_CD AND Z.CONT_NO=A.CONT_NO "
					+ "AND Z.AGMT_NO=A.AGMT_NO AND Z.PLANT_SEQ=Y.PLANT_SEQ AND Z.BU_UNIT=Y.BU_SEQ AND Z.CONTRACT_TYPE=A.CONTRACT_TYPE "
					+ "AND Z.FREQ=? AND Z.PERIOD_START_DT=C.EXP_FROM_DT AND Z.PERIOD_END_DT=C.EXP_TO_DT "
					+ "AND Z.FINANCIAL_YEAR=? AND Z.PDF_INV_DTL IS NOT NULL AND Z.INV_FLAG=? AND Z.CARGO_NO=B.CARGO_NO AND Z.BOE_NO=Y.BOE_NO) = 0) ";
			queryString+="UNION ALL ";
			queryString+="SELECT DISTINCT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
					+ "TO_CHAR(C.EXP_FROM_DT,'DD/MM/YYYY'),TO_CHAR(C.EXP_TO_DT,'DD/MM/YYYY'),A.CONT_NAME,B.CARGO_REF,A.CONTRACT_TYPE,E.DUE_DATE,E.DUE_DT_IN,E.EXCL_SAT_MAP,"
					+ "Y.ACT_BOE_QTY,(NVL(Y.BOE_FINAL_PRICE,0)-NVL(Y.BOE_PROVISIONAL_PRICE,0)),Y.BOE_FINAL_PRICE_UNIT,E.INVOICE_CUR_CD,E.EXCHNG_RATE_CD,E.EXCHNG_RATE_CAL,E.EXCHG_VAL,"
					+ "B.CARGO_NO,Y.BU_SEQ,Y.PLANT_SEQ,Y.BOE_NO,0,'F' "
					+ "FROM FMS_TRADER_CN_MST A,"
						+ "FMS_TRADER_CARGO_MST B,"
						+ "FMS_BUY_CARGO_NOM C,"
						+ "FMS_BUY_CARGO_ALLOC X,"
						+ "FMS_BUY_CARGO_ALLOC_BOE Y,"
						+ "FMS_TRADER_BILLING_DTL E "
					+ "WHERE A.COMPANY_CD=? AND B.CARGO_STATUS=? AND Y.ACT_BOE_QTY > 0 "
					//+ "AND C.EXP_FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND C.EXP_TO_DT >=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND C.EXP_FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND C.EXP_TO_DT >=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONTRACT_TYPE=C.CONTRACT_TYPE "
					+ "AND A.CONT_NO=C.CONT_NO AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_TYPE=C.AGMT_TYPE AND B.CARGO_NO=C.CARGO_NO "
					+ "AND C.NOM_REV = (SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM B WHERE C.COMPANY_CD=B.COMPANY_CD "
					+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.CONT_NO=B.CONT_NO AND C.AGMT_NO=B.AGMT_NO "
					+ "AND C.CONTRACT_TYPE=B.CONTRACT_TYPE AND C.CARGO_NO=B.CARGO_NO AND C.AGMT_TYPE=B.AGMT_TYPE) "
					+ "AND A.COMPANY_CD=X.COMPANY_CD AND A.COUNTERPARTY_CD=X.COUNTERPARTY_CD AND A.CONTRACT_TYPE=X.CONTRACT_TYPE "
					+ "AND A.CONT_NO=X.CONT_NO AND A.AGMT_NO=X.AGMT_NO AND A.AGMT_TYPE=X.AGMT_TYPE AND B.CARGO_NO=X.CARGO_NO "
					+ "AND X.ALLOC_REV = (SELECT MAX(B.ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC B WHERE X.COMPANY_CD=B.COMPANY_CD "
					+ "AND X.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND X.CONT_NO=B.CONT_NO AND X.AGMT_NO=B.AGMT_NO "
					+ "AND X.CONTRACT_TYPE=B.CONTRACT_TYPE AND X.CARGO_NO=B.CARGO_NO AND X.AGMT_TYPE=B.AGMT_TYPE) "
					+ "AND Y.COMPANY_CD=X.COMPANY_CD AND Y.COUNTERPARTY_CD=X.COUNTERPARTY_CD AND Y.CONTRACT_TYPE=X.CONTRACT_TYPE "
					+ "AND Y.CONT_NO=X.CONT_NO AND Y.AGMT_TYPE=X.AGMT_TYPE AND Y.AGMT_NO=X.AGMT_NO AND Y.CARGO_NO=X.CARGO_NO AND Y.ALLOC_REV=X.ALLOC_REV "
					+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO "
					+ "AND A.AGMT_NO=E.AGMT_NO AND A.CONTRACT_TYPE=E.CONTRACT_TYPE AND E.BILLING_FREQ=? "
					+ "AND Y.BOE_PROVISIONAL_PRICE IS NOT NULL AND Y.BOE_FINAL_PRICE IS NOT NULL AND Y.BOE_PROVISIONAL_PRICE != Y.BOE_FINAL_PRICE "
					+ "AND ((SELECT NVL(COUNT(*),0) FROM FMS_PUR_SG_INV_MST Z "
					+ "WHERE Z.COMPANY_CD=A.COMPANY_CD AND Z.COUNTERPARTY_CD=A.COUNTERPARTY_CD AND Z.CONT_NO=A.CONT_NO "
					+ "AND Z.AGMT_NO=A.AGMT_NO AND Z.PLANT_SEQ=Y.PLANT_SEQ AND Z.BU_UNIT=Y.BU_SEQ AND Z.CONTRACT_TYPE=A.CONTRACT_TYPE "
					+ "AND Z.FREQ=? AND Z.PERIOD_START_DT=C.EXP_FROM_DT AND Z.PERIOD_END_DT=C.EXP_TO_DT "
					+ "AND Z.FINANCIAL_YEAR=? AND Z.PDF_INV_DTL IS NOT NULL AND Z.INV_FLAG=? AND Z.CARGO_NO=B.CARGO_NO AND Z.BOE_NO=Y.BOE_NO) = 0 "
					+ "AND (SELECT NVL(COUNT(*),0) FROM FMS_PUR_PG_INV_MST Z "
					+ "WHERE Z.COMPANY_CD=A.COMPANY_CD AND Z.COUNTERPARTY_CD=A.COUNTERPARTY_CD AND Z.CONT_NO=A.CONT_NO "
					+ "AND Z.AGMT_NO=A.AGMT_NO AND Z.PLANT_SEQ=Y.PLANT_SEQ AND Z.BU_UNIT=Y.BU_SEQ AND Z.CONTRACT_TYPE=A.CONTRACT_TYPE "
					+ "AND Z.FREQ=? AND Z.PERIOD_START_DT=C.EXP_FROM_DT AND Z.PERIOD_END_DT=C.EXP_TO_DT "
					+ "AND Z.FINANCIAL_YEAR=? AND Z.PDF_INV_DTL IS NOT NULL AND Z.INV_FLAG=? AND Z.CARGO_NO=B.CARGO_NO AND Z.BOE_NO=Y.BOE_NO) = 0) ";
			int st_count=0;
			String temp_queryString=queryString;
			stmt=conn.prepareStatement(temp_queryString);
			stmt.setString(++st_count, comp_cd);
			stmt.setString(++st_count, "Y");
			//stmt.setString(++st_count, temp_period_end_dt);
			//stmt.setString(++st_count, temp_period_start_dt);
			stmt.setString(++st_count, report_end_dt);
			stmt.setString(++st_count, report_start_dt);
			stmt.setString(++st_count, billing_freq);
			stmt.setString(++st_count, comp_cd);
			stmt.setString(++st_count, "Y");
			//stmt.setString(++st_count, temp_period_end_dt);
			//stmt.setString(++st_count, temp_period_start_dt);
			stmt.setString(++st_count, report_end_dt);
			stmt.setString(++st_count, report_start_dt);
			stmt.setString(++st_count, billing_freq);
			stmt.setString(++st_count, comp_cd);
			stmt.setString(++st_count, "Y");
			//stmt.setString(++st_count, temp_period_end_dt);
			//stmt.setString(++st_count, temp_period_start_dt);
			stmt.setString(++st_count, report_end_dt);
			stmt.setString(++st_count, report_start_dt);
			stmt.setString(++st_count, billing_freq);
			stmt.setString(++st_count, billing_cycle);
			stmt.setString(++st_count, financial_year);
			stmt.setString(++st_count, "P");
			stmt.setString(++st_count, billing_cycle);
			stmt.setString(++st_count, financial_year);
			stmt.setString(++st_count, "P");
			stmt.setString(++st_count, comp_cd);
			stmt.setString(++st_count, "Y");
			//stmt.setString(++st_count, temp_period_end_dt);
			//stmt.setString(++st_count, temp_period_start_dt);
			stmt.setString(++st_count, report_end_dt);
			stmt.setString(++st_count, report_start_dt);
			stmt.setString(++st_count, billing_freq);
			stmt.setString(++st_count, billing_cycle);
			stmt.setString(++st_count, financial_year);
			stmt.setString(++st_count, "F");
			stmt.setString(++st_count, billing_cycle);
			stmt.setString(++st_count, financial_year);
			stmt.setString(++st_count, "F");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmtno=rset.getString(3)==null?"0":rset.getString(3);
				String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
				String contno=rset.getString(5)==null?"0":rset.getString(5);
				String contrev=rset.getString(6)==null?"0":rset.getString(6);
				String cont_start_dt=rset.getString(7)==null?"":rset.getString(7);
				String cont_end_dt=rset.getString(8)==null?"":rset.getString(8);
				//9
				String cont_ref_no=rset.getString(10)==null?"":rset.getString(10);
				String cont_type=rset.getString(11)==null?"":rset.getString(11);
				
				String due_days=rset.getString(12)==null?"":rset.getString(12);
				String consider_due_dt_in=rset.getString(13)==null?"":rset.getString(13);
				String exclude_sat=rset.getString(14)==null?"":rset.getString(14);
				double qty=rset.getDouble(15);
				String price=rset.getString(16)==null?"":rset.getString(16);
				String price_unit=rset.getString(17)==null?"":rset.getString(17);
				String invoice_raise_in=rset.getString(18)==null?"":rset.getString(18);
				String price_unit_nm=utilBean.getRateUnitNm(conn,price_unit);
				String exchng_rate_cd=rset.getString(19)==null?"":rset.getString(19);
				String exchng_rate_cal=rset.getString(20)==null?"":rset.getString(20);
				String fixed_exchng_val=nf2.format(rset.getDouble(21));
				String cargo_no=rset.getString(22)==null?"":rset.getString(22);
				
				String bu_plant_seq = rset.getString(23)==null?"":rset.getString(23);
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
				String plant_seq = rset.getString(24)==null?"":rset.getString(24);
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "T");
				
				String boe_no = rset.getString(25)==null?"0":rset.getString(25);
				
				String deal_no=utilBean.NewDealMappingId(comp_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, cargo_no);
				
				int noofcomibination=rset.getInt(26);
				String invFlag = rset.getString(27)==null?"":rset.getString(27);
				
				String holiday_state="";
				String queryString1="SELECT HOLIDAY_STATE "
						+ "FROM FMS_TRADER_BILLING_DTL "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, countpty_cd);
				stmt1.setString(3, agmtno);
				stmt1.setString(4, agmtrev);
				stmt1.setString(5, contno);
				stmt1.setString(6, cont_type);
				stmt1.setString(7, plant_seq);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					holiday_state=rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
					
				double accrual_amt=0;
				double gross_amt=0;
				
				if(noofcomibination>1)
				{
					qty = qty / noofcomibination;
				}
				
				accrual_amt=qty * Double.parseDouble(price);

				gross_amt = accrual_amt;
				
				if(qty>0 && accrual_amt > 0)
				{
					VFINANCIAL_YEAR.add(financial_year);
					VCOUNTERPTY_CD.add(countpty_cd);
					VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
					VCOUNTERPTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
					VAGMT_NO.add(agmtno);
					VAGMT_REV_NO.add(agmtrev);
					VCONT_NO.add(contno);
					VCONT_REV_NO.add(contrev);
					VCONTRACT_TYPE.add(cont_type);
					VCARGO_NO.add(cargo_no);
					VBOE_NO.add(boe_no);
					VBOE_NM.add("BOE"+utilBean.PrePaddingZero(boe_no, 2));
					VINV_FLAG.add(invFlag);
					VSTART_DT.add(cont_start_dt);
					VEND_DT.add(cont_end_dt);
					VDIS_CONT_MAPPING.add(deal_no);
					VCONT_REF_NO.add(cont_ref_no);
					VPLANT_SEQ.add(plant_seq);
					VPLANT_ABBR.add(plant_abbr);
					VBU_PLANT_SEQ.add(bu_plant_seq);
					VBU_PLANT_ABBR.add(bu_plant_abbr);
					VPERIOD_START_DT.add(cont_start_dt);
					VPERIOD_END_DT.add(cont_end_dt);
					VBILLING_FREQ_FLAG.add(billing_cycle);
					VBILLING_FREQ_NM.add(billing_freq_nm);
					VPRODUCTION_MONTH.add(month+"/"+year);
					VINVOICE_DUE_DT.add(utilBean.DueDateCalculation(conn,cont_end_dt, due_days,consider_due_dt_in,exclude_sat,comp_cd,holiday_state));
					
					VSPLIT_FLAG.add("");
					VSPLIT_VALUE.add("");
					
					VCASH_FLOW.add("Commodity");
					
					VACCRUAL_QTY.add(nf.format(qty));
					VACCRUAL_AMT.add(nf.format(accrual_amt));
					
					VSALES_PRICE_CD.add(price_unit);
					VSALES_PRICE_NM.add(price_unit_nm);
					VINVOICE_RAISED_IN.add(invoice_raise_in);
				
					VEXCHNG_RATE.add("");
					VEXCHNG_RATE_CD.add("");
					VEXCHNG_RATE_DT.add("");
					
					VGROSS_AMT.add("");
				}
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getGtaAccrualList()
	{
		String function_nm="getGtaAccrualList()";
		try
		{
			Vector TEMP_INV_COMP_ABBR = new Vector();
			Vector TEMP_INV_COMP_NM = new Vector();
			
			String filterContTyp="'R','C'"; 
			if(invoice_type.equals("TC"))
			{
				TEMP_INV_COMP_NM.add("Transportation");
				TEMP_INV_COMP_NM.add("Ship-or-Pay");
				
				TEMP_INV_COMP_ABBR.add("TP");
				TEMP_INV_COMP_ABBR.add("SP");
			}
			else if(invoice_type.equals("IC"))
			{
				TEMP_INV_COMP_NM.add("Negative Imbalance");
				TEMP_INV_COMP_NM.add("Positive Imbalance");
				TEMP_INV_COMP_NM.add("Unauthorized Overrun");
				
				TEMP_INV_COMP_ABBR.add("NI");
				TEMP_INV_COMP_ABBR.add("PI");
				TEMP_INV_COMP_ABBR.add("UR");	
			}
			else if(invoice_type.equals("PC"))
			{
				TEMP_INV_COMP_NM.add("Parking Charge");
				
				TEMP_INV_COMP_ABBR.add("PC");
				
				filterContTyp="'K'";
			}
			
			int cont=0;
			if(billing_cycle.equals("8"))
			{
				String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
				String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
				
				queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,"
						+ "A.SIP_PAY_FREQ,C.INVOICE_CUR_CD,C.DUE_DATE,C.DUE_DT_IN,C.EXCL_SAT_MAP,A.TRANSPORT_RATE,A.RATE_UNIT,"
						+ "A.POSITIVE_IMB_RATE,A.NEGETIVE_IMB_RATE,A.UNAUTH_OVERRUN_RATE,A.SIP_PAY_FREQ,A.SIP_PAY_RATE,"
						+ "D.PLANT_SEQ_NO,E.PLANT_SEQ_NO,TO_CHAR(C.EFF_DT,'DD/MM/YYYY'),C.BILLING_DAYS,C.EFF_DT,C.HOLIDAY_STATE,A.PARKING_RATE "
						+ ""
						+ "FROM FMS_GTA_CONT_MST A, FMS_GTA_BILLING_DTL C, FMS_GTA_CONT_TRANS_BU D, FMS_GTA_CONT_BU E "
						+ "WHERE A.COMPANY_CD=? "
						+ "AND A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND C.PLANT_SEQ_NO=D.PLANT_SEQ_NO AND C.BILLING_FREQ=? "
						+ "AND ((C.EFF_DT>=TO_DATE(?,'DD/MM/YYYY') AND C.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) "
						+ "OR C.EFF_DT=(SELECT MAX(E.EFF_DT) FROM FMS_GTA_BILLING_DTL E WHERE C.COMPANY_CD=E.COMPANY_CD "
						+ "AND C.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND C.AGMT_NO=E.AGMT_NO AND C.CONT_NO=E.CONT_NO "
						+ "AND C.CONTRACT_TYPE=E.CONTRACT_TYPE AND E.EFF_DT<TO_DATE(?,'DD/MM/YYYY'))) "
						+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV "
						+ "AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE "
						+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO AND A.CONT_REV=E.CONT_REV "
						+ "AND A.AGMT_NO=E.AGMT_NO AND A.AGMT_REV=E.AGMT_REV AND A.CONTRACT_TYPE=E.CONTRACT_TYPE "
						+ ""
						+ "AND A.CONTRACT_TYPE IN ("+filterContTyp+") "
						+ "ORDER BY C.EFF_DT";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(++cont, comp_cd);
				stmt.setString(++cont, temp_period_end_dt);
				stmt.setString(++cont, temp_period_start_dt);
				stmt.setString(++cont, billing_freq);
				stmt.setString(++cont, temp_period_start_dt);
				stmt.setString(++cont, temp_period_end_dt);
				stmt.setString(++cont, temp_period_start_dt);
				ResultSet rset=stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					String start_dt=rset.getString(7)==null?"":rset.getString(7);
					String end_dt=rset.getString(8)==null?"":rset.getString(8);
					String cont_name=rset.getString(9)==null?"":rset.getString(9);
					String cont_ref_no=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					String sip_pay_freq=rset.getString(12)==null?"D":rset.getString(12);
					String invoice_raise_in=rset.getString(13)==null?"":rset.getString(13);
					String due_days=rset.getString(14)==null?"":rset.getString(14);
					String consider_due_dt_in=rset.getString(15)==null?"":rset.getString(15);
					String exclude_sat=rset.getString(16)==null?"":rset.getString(16);
					
					String price = nf.format(rset.getDouble(17));
					String price_cd = rset.getString(18)==null?"1":rset.getString(18);
									
					String positive_imbalance_rate = nf.format(rset.getDouble(19));
					String negative_imbalance_rate = nf.format(rset.getDouble(20));
					String unauthorized_overrun_rate = nf.format(rset.getDouble(21));
					
					String ship_pay_freq=rset.getString(20)==null?"D":rset.getString(22);
					String ship_pay_rate=nf.format(rset.getDouble(23));
					
					String trans_bu_seq = rset.getString(24)==null?"":rset.getString(24);
					String trans_bu_abbr=utilBean.getCounterpartyBuABBR(conn,countpty_cd, own_cd, trans_bu_seq, "R");
					
					String bu_plant_seq = rset.getString(25)==null?"":rset.getString(25);
					String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
					
					String billing_eff_dt=rset.getString(26)==null?"":rset.getString(26);
					String billing_days=rset.getString(27)==null?"1":rset.getString(27);
					
					String holiday_state=rset.getString(28)==null?"":rset.getString(28);
					String parkingRate=nf.format(rset.getDouble(29));
					
					//String deal_no=cont_type+agmtno+"-"+contno;
					String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, "");
					String fin_yr = utilDate.getFinancialYear(temp_period_end_dt);
					
					//System.out.println("\n"+countpty_abbr+" -- "+deal_no+" :: "+start_dt+" - "+end_dt+" :: Bill Eff "+billing_eff_dt+" :: "+temp_period_start_dt+" - "+temp_period_end_dt);
					
					String periodStartDate="";
					String periodEndDate="";
					
					int isGreter=utilDate.getDays(billing_eff_dt, temp_period_start_dt);
					if(isGreter>1)
					{
						periodStartDate=billing_eff_dt;
					}
					else
					{
						periodStartDate=temp_period_start_dt;
					}
					int isLower=utilDate.getDays(end_dt, temp_period_end_dt);
					if(isLower < 1)
					{
						periodEndDate=end_dt;
					}
					else
					{
						periodEndDate=temp_period_end_dt;
					}
					//System.out.println("PeriodDate="+periodStartDate+"=="+periodEndDate);
					String temp_st_dt=periodStartDate;
					String temp_end_dt=periodEndDate;
					//System.out.println("Billing Days : "+billing_days);
					
					String temp_dt = utilDate.getDate(temp_st_dt,"-1");
					//System.out.println("temp_dt :: "+temp_dt);
					int tot_row=1;
					for(int j=0;j<tot_row;j++)
					{
						temp_st_dt=utilDate.getDate(temp_dt,"1");
						temp_dt=utilDate.getDate(temp_dt,billing_days);
						
						int checkMthEnd=utilDate.getDays(periodEndDate,temp_st_dt);
						
						if(Integer.parseInt(billing_days) <= checkMthEnd)
						{
							temp_end_dt = temp_dt;
						}
						else
						{
							temp_end_dt = periodEndDate;
						}
						
						String innerBillingEffDt=billing_eff_dt; //defaualt added
						String innerBillingDays="";
						String innerBillingFreq="";
						queryString1="SELECT DISTINCT TO_CHAR(EFF_DT,'DD/MM/YYYY'),BILLING_DAYS,BILLING_FREQ "
								+ "FROM FMS_GTA_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND CONT_NO=? "
								+ "AND CONTRACT_TYPE=? "
								+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_GTA_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
								+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY'))";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, own_cd);
						stmt1.setString(2, countpty_cd);
						stmt1.setString(3, agmtno);
						stmt1.setString(4, contno);
						stmt1.setString(5, cont_type);
						stmt1.setString(6, temp_end_dt);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							innerBillingEffDt=rset1.getString(1)==null?"":rset1.getString(1);
							innerBillingDays=rset1.getString(2)==null?"1":rset1.getString(2);
							innerBillingFreq=rset1.getString(3)==null?"":rset1.getString(3);
						}
						rset1.close();
						stmt1.close();
						
						if(!billing_eff_dt.equals(innerBillingEffDt))
						{
							System.out.println("New Eff Date : "+innerBillingEffDt);
							break;
						}
						
						int rem_checkMthEnd=utilDate.getDays(periodEndDate,utilDate.getDate(temp_end_dt,"1"));
						System.out.println(j+" - "+checkMthEnd+" : "+temp_st_dt+" :: "+temp_end_dt+" Rem Day : "+rem_checkMthEnd);
						tot_row+=1;
						
						int countDays=utilDate.getDays(end_dt, temp_end_dt);
						
						for(int i=0; i<TEMP_INV_COMP_ABBR.size(); i++)
						{
							int isInvExist=0;
							String invComponent=""+TEMP_INV_COMP_ABBR.elementAt(i);
							
							queryString4="SELECT COUNT(*) "
									+ "FROM FMS_GTA_SG_INV_MST "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND AGMT_NO=? AND TRANS_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
									+ "AND FREQ=? AND INVOICE_TYPE=? "
									+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND PDF_INV_DTL IS NOT NULL AND INV_COMPONENT LIKE ? ";
							stmt4=conn.prepareStatement(queryString4);
							stmt4.setString(1, own_cd);
							stmt4.setString(2, countpty_cd);
							stmt4.setString(3, contno);
							stmt4.setString(4, agmtno);
							stmt4.setString(5, trans_bu_seq);
							stmt4.setString(6, bu_plant_seq);
							stmt4.setString(7, cont_type);
							stmt4.setString(8, billing_cycle);
							stmt4.setString(9, invoice_type);
							stmt4.setString(10, temp_st_dt);
							stmt4.setString(11, temp_end_dt);
							stmt4.setString(12, "%"+invComponent+"%");
							rset4=stmt4.executeQuery();
							if(rset4.next())
							{
								isInvExist+=rset4.getInt(1);
							}
							rset4.close();
							stmt4.close();
							
							queryString5="SELECT COUNT(*) "
									+ "FROM FMS_GTA_PG_INV_MST "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND AGMT_NO=? AND TRANS_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
									+ "AND FREQ=? AND INVOICE_TYPE=? "
									+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND PDF_INV_DTL IS NOT NULL AND INV_COMPONENT LIKE ? ";
							stmt5=conn.prepareStatement(queryString5);
							stmt5.setString(1, own_cd);
							stmt5.setString(2, countpty_cd);
							stmt5.setString(3, contno);
							stmt5.setString(4, agmtno);
							stmt5.setString(5, trans_bu_seq);
							stmt5.setString(6, bu_plant_seq);
							stmt5.setString(7, cont_type);
							stmt5.setString(8, billing_cycle);
							stmt5.setString(9, invoice_type);
							stmt5.setString(10, temp_st_dt);
							stmt5.setString(11, temp_end_dt);
							stmt5.setString(12, "%"+invComponent+"%");
							rset5=stmt5.executeQuery();
							if(rset5.next())
							{
								isInvExist+=rset5.getInt(1);
							}
							rset5.close();
							stmt5.close();
							
							if(isInvExist==0)
							{
								double qty_mmbtu=0;
								double temp_qty_mmbtu=0;
								
								String cashflow="";
								int days=0;
								
								String accrual_amt="";
								if(invoice_type.equals("TC"))
								{
									cashflow="Transmission Charge";
									if (sip_pay_freq.equals("M")) 
									{	
										String tempStart_dt=""+utilDate.getFirstDateOfMonth(month, year);
										String tempEnd_dt=""+utilDate.getLastDateOfMonth(month, year);
										
										days=utilDate.getDays(end_dt, tempEnd_dt);
										
										if(billing_cycle.equals("2") || countDays<=1) //ADDED = TO SIGN
										{
											if(days<1)
											{
												tempEnd_dt=end_dt;
											}
											if(utilDate.getDays(tempStart_dt, start_dt)<1)
											{
												tempStart_dt=start_dt;
											}
											
											if(invComponent.contains("TP"))
											{
												qty_mmbtu=getAccrualTransmissionQty(countpty_cd, cont_type, agmtno, contno, temp_st_dt, temp_end_dt, bu_plant_seq);
												temp_qty_mmbtu += qty_mmbtu;
												
												String transmission_amt=nf.format(qty_mmbtu * Double.parseDouble(price));
												accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(transmission_amt)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(transmission_amt));
											}
											if(invComponent.contains("SP"))
											{
												double temp_def_qty=getAccrualDeficiencyQty(countpty_cd, cont_type, agmtno, contno, tempStart_dt, tempEnd_dt, bu_plant_seq);
												if(temp_def_qty<0)
												{
													temp_def_qty=0;
												}
												deficiency_qty=nf.format(temp_def_qty);
												
												temp_qty_mmbtu+=temp_def_qty;
												
												String deficiency_amt=nf.format(Double.parseDouble(deficiency_qty) * Double.parseDouble(ship_pay_rate));
												accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(deficiency_amt)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(deficiency_amt));
											}
										}
										else
										{
											if(invComponent.contains("TP"))
											{
												qty_mmbtu=getAccrualTransmissionQty(countpty_cd, cont_type, agmtno, contno, temp_st_dt, temp_end_dt, bu_plant_seq);
												temp_qty_mmbtu+=qty_mmbtu;
												
												accrual_amt = nf.format(qty_mmbtu * Double.parseDouble(price));
											}
										}
									}
									else
									{
										if(invComponent.contains("TP"))
										{
											qty_mmbtu=getAccrualTransmissionQty(countpty_cd, cont_type, agmtno, contno, temp_st_dt, temp_end_dt, bu_plant_seq);
											temp_qty_mmbtu+=qty_mmbtu;
											
											accrual_amt = nf.format(qty_mmbtu * Double.parseDouble(price));
										}
									}
								}
								else if(invoice_type.equals("IC"))
								{
									cashflow="Imbalance Charge";
									getImbalanceQty(countpty_cd, cont_type, agmtno, contno, temp_st_dt, temp_end_dt, bu_plant_seq);
									
									//temp_qty_mmbtu=Double.parseDouble(positive_imbalance_qty) + Double.parseDouble(negative_imbalance_qty) + Double.parseDouble(unauthorized_overrun_qty);
									//temp_qty_mmbtu=1;
									
									if(invComponent.contains("NI"))
									{
										temp_qty_mmbtu+=Double.parseDouble(negative_imbalance_qty);
										
										String negative_imbalance_amount=nf.format(Double.parseDouble(negative_imbalance_qty) * Double.parseDouble(negative_imbalance_rate));
										accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(negative_imbalance_amount)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(negative_imbalance_amount));
									}
									if(invComponent.contains("PI"))
									{
										temp_qty_mmbtu+=Double.parseDouble(positive_imbalance_qty);
										
										String positive_imbalance_amount=nf.format(Double.parseDouble(positive_imbalance_qty) * Double.parseDouble(positive_imbalance_rate));
										accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(positive_imbalance_amount)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(positive_imbalance_amount));
									}
									if(invComponent.contains("UR"))
									{
										temp_qty_mmbtu+=Double.parseDouble(unauthorized_overrun_qty);
										
										String unauthorized_imbalance_amount=nf.format(Double.parseDouble(unauthorized_overrun_qty) * Double.parseDouble(unauthorized_overrun_rate));
										accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(unauthorized_imbalance_amount)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(unauthorized_imbalance_amount));
									}
								}
								else if(invoice_type.equals("PC"))
								{
									cashflow="Parking Charge";
									if(invComponent.contains("PC"))
									{
										qty_mmbtu = getAccrualParkingQty(countpty_cd, cont_type, agmtno, contno, temp_st_dt, temp_end_dt, bu_plant_seq);
										temp_qty_mmbtu=qty_mmbtu;
										
										accrual_amt = nf.format(qty_mmbtu * Double.parseDouble(parkingRate));
									}
								}
								
								if(temp_qty_mmbtu > 0 && !accrual_amt.equals(""))
								{
									if(Double.parseDouble(accrual_amt) > 0)
									{
										VINV_COMPONENTS.add(invComponent);
										VQTY_MMBTU.add(nf3.format(qty_mmbtu));
										if(invoice_type.equals("IC"))
										{
											VTEMP_QTY_MMBTU.add(nf3.format(temp_qty_mmbtu));
										}
										else
										{
											VTEMP_QTY_MMBTU.add(nf3.format(qty_mmbtu));
										}
										VCOUNTERPTY_CD.add(countpty_cd);
										VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
										VCOUNTERPTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
										VAGMT_NO.add(agmtno);
										VAGMT_REV_NO.add(agmtrev);
										VCONT_NO.add(contno);
										VCONT_REV_NO.add(contrev);
										VSTART_DT.add(start_dt);
										VEND_DT.add(end_dt);
										VCONT_REF_NO.add(cont_ref_no);
										VCONTRACT_TYPE.add(cont_type);
										VDIS_CONT_MAPPING.add(deal_no);
					
										VTRANS_BU_SEQ.add(trans_bu_seq);
										VTRANS_BU_ABBR.add(trans_bu_abbr);
										VBU_PLANT_SEQ.add(bu_plant_seq);
										VBU_PLANT_ABBR.add(bu_plant_abbr);
										VPERIOD_START_DT.add(temp_st_dt);
										VPERIOD_END_DT.add(temp_end_dt);
										
										VBILLING_FREQ_NM.add(billing_freq_nm);
										VBILLING_FREQ_FLAG.add(billing_cycle);
										
										VFINANCIAL_YEAR.add(fin_yr);
										
										VINVOICE_DUE_DT.add(""+utilBean.DueDateCalculation(conn,temp_end_dt, due_days,consider_due_dt_in,exclude_sat,comp_cd,holiday_state));
										VPRODUCTION_MONTH.add(period_end_dt.substring(3,temp_end_dt.length()));
										
										VCASH_FLOW_NM.add(cashflow);
										VCASH_FLOW.add(invoice_type);
										
										VINVOICE_RAISED_IN.add(invoice_raise_in);
										VACCRUAL_QTY.add(nf.format(temp_qty_mmbtu));
										
										VACCRUAL_AMT.add(accrual_amt);
										VGROSS_AMT.add(accrual_amt);
									}
								}
							}
						}
						
						if(rem_checkMthEnd == 0)
						{
							break;
						}
					}
				}
				rset.close();
				stmt.close();
			}
			else
			{
				queryString="SELECT DISTINCT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,"
						+ "A.SIP_PAY_FREQ,C.INVOICE_CUR_CD,C.DUE_DATE,C.DUE_DT_IN,C.EXCL_SAT_MAP,"
						+ "A.TRANSPORT_RATE,A.RATE_UNIT,"
						+ "A.POSITIVE_IMB_RATE,A.NEGETIVE_IMB_RATE,A.UNAUTH_OVERRUN_RATE,A.SIP_PAY_FREQ,A.SIP_PAY_RATE,TO_CHAR(C.EFF_DT,'DD/MM/YYYY'),"
						+ "C.HOLIDAY_STATE,D.PLANT_SEQ_NO,E.PLANT_SEQ_NO,A.PARKING_RATE "		
						+ "FROM FMS_GTA_CONT_MST A, FMS_GTA_BILLING_DTL C, FMS_GTA_CONT_TRANS_BU D, FMS_GTA_CONT_BU E "
						+ "WHERE A.COMPANY_CD=? "
						+ "AND A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND D.PLANT_SEQ_NO=C.PLANT_SEQ_NO AND C.BILLING_FREQ=? "
						+ "AND C.EFF_DT=(SELECT MAX(E.EFF_DT) FROM FMS_GTA_BILLING_DTL E WHERE C.COMPANY_CD=E.COMPANY_CD "
						+ "AND C.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND C.AGMT_NO=E.AGMT_NO AND C.CONT_NO=E.CONT_NO "
						+ "AND C.CONTRACT_TYPE=E.CONTRACT_TYPE AND E.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) "
						+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV "
						+ "AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE "
						+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO AND A.CONT_REV=E.CONT_REV "
						+ "AND A.AGMT_NO=E.AGMT_NO AND A.AGMT_REV=E.AGMT_REV AND A.CONTRACT_TYPE=E.CONTRACT_TYPE "
						+ ""
						+ "AND A.CONTRACT_TYPE IN ("+filterContTyp+")";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(++cont, comp_cd);
				stmt.setString(++cont, period_end_dt);
				stmt.setString(++cont, period_start_dt);
				stmt.setString(++cont, billing_freq);
				stmt.setString(++cont, period_end_dt);
				ResultSet rset=stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					String start_dt=rset.getString(7)==null?"":rset.getString(7);
					String end_dt=rset.getString(8)==null?"":rset.getString(8);
					String cont_name=rset.getString(9)==null?"":rset.getString(9);
					String cont_ref_no=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					String sip_pay_freq=rset.getString(12)==null?"D":rset.getString(12);
					String invoice_raise_in=rset.getString(13)==null?"":rset.getString(13);
					String due_days=rset.getString(14)==null?"":rset.getString(14);
					String consider_due_dt_in=rset.getString(15)==null?"":rset.getString(15);
					String exclude_sat=rset.getString(16)==null?"":rset.getString(16);
					
					String price = nf.format(rset.getDouble(17));
					String price_cd = rset.getString(18)==null?"1":rset.getString(18);
									
					String positive_imbalance_rate = nf.format(rset.getDouble(19));
					String negative_imbalance_rate = nf.format(rset.getDouble(20));
					String unauthorized_overrun_rate = nf.format(rset.getDouble(21));
					
					String ship_pay_freq=rset.getString(20)==null?"D":rset.getString(22);
					String ship_pay_rate=nf.format(rset.getDouble(23));
					
					String billing_eff_dt=rset.getString(24)==null?"":rset.getString(24);
					String holiday_state=rset.getString(25)==null?"":rset.getString(25);
					
					String trans_bu_seq = rset.getString(26)==null?"":rset.getString(26);
					String trans_bu_abbr=utilBean.getCounterpartyBuABBR(conn,countpty_cd, own_cd, trans_bu_seq, "R");
					
					String bu_plant_seq = rset.getString(27)==null?"":rset.getString(27);
					String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
					
					String parkingRate=nf.format(rset.getDouble(28));
					
					//String deal_no=cont_type+agmtno+"-"+contno;
					String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, "");
					
					String fin_yr = utilDate.getFinancialYear(period_end_dt);
							
					String temp_period_start_dt=period_start_dt;
					String temp_period_end_dt=period_end_dt;
					
					/*int countDays=utilDate.getDays(end_dt, period_end_dt);
					
					if(countDays<1)
					{
						temp_period_end_dt=end_dt;
					}
					/*if(utilDate.getDays(period_start_dt, start_dt)<1)
					{
						temp_period_start_dt=start_dt;
					}*/
					
					int isGreter=utilDate.getDays(billing_eff_dt, period_start_dt);
					
					if(isGreter>1)
					{
						temp_period_start_dt=billing_eff_dt;
						temp_period_end_dt=period_end_dt;
					}
					else
					{
						temp_period_start_dt=period_start_dt;
						temp_period_end_dt=period_end_dt;
					}
					
					int countDays=utilDate.getDays(end_dt, period_end_dt);
					
					if(countDays<1)
					{
						temp_period_end_dt=end_dt;
					}
					
					/*if(utilDate.getDays(end_dt, period_end_dt)<=0)
					{
						temp_period_end_dt=end_dt;
					}*/
					
					for(int i=0; i<TEMP_INV_COMP_ABBR.size(); i++)
					{
						int isInvExist=0;
						String invComponent=""+TEMP_INV_COMP_ABBR.elementAt(i);
						
						queryString4="SELECT COUNT(*) "
								+ "FROM FMS_GTA_SG_INV_MST "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND TRANS_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND FREQ=? AND INVOICE_TYPE=? "
								+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND PDF_INV_DTL IS NOT NULL AND INV_COMPONENT LIKE ? ";
						stmt4=conn.prepareStatement(queryString4);
						stmt4.setString(1, own_cd);
						stmt4.setString(2, countpty_cd);
						stmt4.setString(3, contno);
						stmt4.setString(4, agmtno);
						stmt4.setString(5, trans_bu_seq);
						stmt4.setString(6, bu_plant_seq);
						stmt4.setString(7, cont_type);
						stmt4.setString(8, billing_cycle);
						stmt4.setString(9, invoice_type);
						//stmt4.setString(10, period_start_dt);
						//stmt4.setString(11, period_end_dt);
						stmt4.setString(10, temp_period_start_dt);
						stmt4.setString(11, temp_period_end_dt);
						stmt4.setString(12, "%"+invComponent+"%");
						rset4=stmt4.executeQuery();
						if(rset4.next())
						{
							isInvExist+=rset4.getInt(1);
						}
						rset4.close();
						stmt4.close();
						
						queryString5="SELECT COUNT(*) "
								+ "FROM FMS_GTA_PG_INV_MST "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND TRANS_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND FREQ=? AND INVOICE_TYPE=? "
								+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND PDF_INV_DTL IS NOT NULL AND INV_COMPONENT LIKE ? ";
						stmt5=conn.prepareStatement(queryString5);
						stmt5.setString(1, own_cd);
						stmt5.setString(2, countpty_cd);
						stmt5.setString(3, contno);
						stmt5.setString(4, agmtno);
						stmt5.setString(5, trans_bu_seq);
						stmt5.setString(6, bu_plant_seq);
						stmt5.setString(7, cont_type);
						stmt5.setString(8, billing_cycle);
						stmt5.setString(9, invoice_type);
						//stmt5.setString(10, period_start_dt);
						//stmt5.setString(11, period_end_dt);
						stmt5.setString(10, temp_period_start_dt);
						stmt5.setString(11, temp_period_end_dt);
						stmt5.setString(12, "%"+invComponent+"%");
						rset5=stmt5.executeQuery();
						if(rset5.next())
						{
							isInvExist+=rset5.getInt(1);
						}
						rset5.close();
						stmt5.close();
					
						if(isInvExist==0)
						{
							double qty_mmbtu=0;
							double temp_qty_mmbtu=0;
							
							String cashflow="";
							int days=0;
							
							String accrual_amt="";
							if(invoice_type.equals("TC"))
							{
								cashflow="Transmission Charge";
								if (sip_pay_freq.equals("M")) 
								{	
									String temp_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
									String temp_end_dt=""+utilDate.getLastDateOfMonth(month, year);
									
									days=utilDate.getDays(end_dt, temp_end_dt);
									
									if(billing_cycle.equals("2") || countDays<=1) //ADDED = TO SIGN
									{
										if(days<1)
										{
											temp_end_dt=end_dt;
										}
										if(utilDate.getDays(temp_start_dt, start_dt)<1)
										{
											temp_start_dt=start_dt;
										}
										
										if(invComponent.contains("TP"))
										{
											qty_mmbtu=getAccrualTransmissionQty(countpty_cd, cont_type, agmtno, contno, temp_period_start_dt, temp_period_end_dt, bu_plant_seq);
											temp_qty_mmbtu += qty_mmbtu;
											
											String transmission_amt=nf.format(qty_mmbtu * Double.parseDouble(price));
											accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(transmission_amt)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(transmission_amt));
										}
										if(invComponent.contains("SP"))
										{
											double temp_def_qty=getAccrualDeficiencyQty(countpty_cd, cont_type, agmtno, contno, temp_start_dt, temp_end_dt, bu_plant_seq);
											if(temp_def_qty<0)
											{
												temp_def_qty=0;
											}
											deficiency_qty=nf.format(temp_def_qty);
											
											temp_qty_mmbtu+=temp_def_qty;
											
											String deficiency_amt=nf.format(Double.parseDouble(deficiency_qty) * Double.parseDouble(ship_pay_rate));
											accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(deficiency_amt)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(deficiency_amt));
										}
									}
									else
									{
										if(invComponent.contains("TP"))
										{
											qty_mmbtu=getAccrualTransmissionQty(countpty_cd, cont_type, agmtno, contno, temp_period_start_dt, temp_period_end_dt, bu_plant_seq);
											temp_qty_mmbtu+=qty_mmbtu;
											
											accrual_amt = nf.format(qty_mmbtu * Double.parseDouble(price));
										}
									}
								}
								else
								{
									if(invComponent.contains("TP"))
									{
										qty_mmbtu=getAccrualTransmissionQty(countpty_cd, cont_type, agmtno, contno, temp_period_start_dt, temp_period_end_dt, bu_plant_seq);
										temp_qty_mmbtu+=qty_mmbtu;
										
										accrual_amt = nf.format(qty_mmbtu * Double.parseDouble(price));
									}
								}
							}
							else if(invoice_type.equals("IC"))
							{
								cashflow="Imbalance Charge";
								getImbalanceQty(countpty_cd, cont_type, agmtno, contno, temp_period_start_dt, temp_period_end_dt, bu_plant_seq);
								
								//temp_qty_mmbtu=Double.parseDouble(positive_imbalance_qty) + Double.parseDouble(negative_imbalance_qty) + Double.parseDouble(unauthorized_overrun_qty);
								//temp_qty_mmbtu=1;
								
								if(invComponent.contains("NI"))
								{
									temp_qty_mmbtu+=Double.parseDouble(negative_imbalance_qty);
									
									String negative_imbalance_amount=nf.format(Double.parseDouble(negative_imbalance_qty) * Double.parseDouble(negative_imbalance_rate));
									accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(negative_imbalance_amount)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(negative_imbalance_amount));
								}
								if(invComponent.contains("PI"))
								{
									temp_qty_mmbtu+=Double.parseDouble(positive_imbalance_qty);
									
									String positive_imbalance_amount=nf.format(Double.parseDouble(positive_imbalance_qty) * Double.parseDouble(positive_imbalance_rate));
									accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(positive_imbalance_amount)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(positive_imbalance_amount));
								}
								if(invComponent.contains("UR"))
								{
									temp_qty_mmbtu+=Double.parseDouble(unauthorized_overrun_qty);
									
									String unauthorized_imbalance_amount=nf.format(Double.parseDouble(unauthorized_overrun_qty) * Double.parseDouble(unauthorized_overrun_rate));
									accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(unauthorized_imbalance_amount)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(unauthorized_imbalance_amount));
								}
							}
							else if(invoice_type.equals("PC"))
							{
								cashflow="Parking Charge";
								if(invComponent.contains("PC"))
								{
									qty_mmbtu = getAccrualParkingQty(countpty_cd, cont_type, agmtno, contno, temp_period_start_dt, temp_period_end_dt, bu_plant_seq);
									temp_qty_mmbtu=qty_mmbtu;
									
									accrual_amt = nf.format(qty_mmbtu * Double.parseDouble(parkingRate));
								}
							}
							
							/*
							//String accrual_amt="";
							if(invoice_type.equals("IC"))
							{
								String positive_imbalance_amount=nf.format(Double.parseDouble(positive_imbalance_qty) * Double.parseDouble(positive_imbalance_rate));
								String negative_imbalance_amount=nf.format(Double.parseDouble(negative_imbalance_qty) * Double.parseDouble(negative_imbalance_rate));
								String unauthorized_imbalance_amount=nf.format(Double.parseDouble(unauthorized_overrun_qty) * Double.parseDouble(unauthorized_overrun_rate));
								
								//accrual_amt=nf.format(Double.parseDouble(positive_imbalance_amount) + Double.parseDouble(negative_imbalance_amount) + Double.parseDouble(unauthorized_imbalance_amount));
								
								if(invComponent.contains("NI"))
								{
									accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(negative_imbalance_amount)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(negative_imbalance_amount));
								}
								if(invComponent.contains("PI"))
								{
									accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(positive_imbalance_amount)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(positive_imbalance_amount));
								}
								if(invComponent.contains("UR"))
								{
									accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(unauthorized_imbalance_amount)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(unauthorized_imbalance_amount));
								}
							}
							else
							{
								if (sip_pay_freq.equals("M")) 
								{
									if(billing_cycle.equals("2") || countDays<=1) //ADDED = TO SIGN
									{
										String transmission_amt=nf.format(qty_mmbtu * Double.parseDouble(price));
										String deficiency_amt=nf.format(Double.parseDouble(deficiency_qty) * Double.parseDouble(ship_pay_rate));
										
										//accrual_amt=nf.format(Double.parseDouble(transmission_amt) + Double.parseDouble(deficiency_amt));
										
										if(invComponent.contains("TP"))
										{
											accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(transmission_amt)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(transmission_amt));
										}
										if(invComponent.contains("SP"))
										{
											accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(deficiency_amt)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(deficiency_amt));
										}
									}
									else
									{
										accrual_amt = nf.format(qty_mmbtu * Double.parseDouble(price));
									}
								}
								else
								{
									accrual_amt = nf.format(qty_mmbtu * Double.parseDouble(price));	
								}
							}*/
							
							if(temp_qty_mmbtu > 0 && !accrual_amt.equals(""))
							{
								if(Double.parseDouble(accrual_amt) > 0)
								{
									VINV_COMPONENTS.add(invComponent);
									VQTY_MMBTU.add(nf3.format(qty_mmbtu));
									if(invoice_type.equals("IC"))
									{
										VTEMP_QTY_MMBTU.add(nf3.format(temp_qty_mmbtu));
									}
									else
									{
										VTEMP_QTY_MMBTU.add(nf3.format(qty_mmbtu));
									}
									VCOUNTERPTY_CD.add(countpty_cd);
									VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
									VCOUNTERPTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
									VAGMT_NO.add(agmtno);
									VAGMT_REV_NO.add(agmtrev);
									VCONT_NO.add(contno);
									VCONT_REV_NO.add(contrev);
									VSTART_DT.add(start_dt);
									VEND_DT.add(end_dt);
									VCONT_REF_NO.add(cont_ref_no);
									VCONTRACT_TYPE.add(cont_type);
									VDIS_CONT_MAPPING.add(deal_no);
				
									VTRANS_BU_SEQ.add(trans_bu_seq);
									VTRANS_BU_ABBR.add(trans_bu_abbr);
									VBU_PLANT_SEQ.add(bu_plant_seq);
									VBU_PLANT_ABBR.add(bu_plant_abbr);
									VPERIOD_START_DT.add(temp_period_start_dt);
									VPERIOD_END_DT.add(temp_period_end_dt);
									
									VBILLING_FREQ_NM.add(billing_freq_nm);
									VBILLING_FREQ_FLAG.add(billing_cycle);
									
									VFINANCIAL_YEAR.add(fin_yr);
									
									VINVOICE_DUE_DT.add(""+utilBean.DueDateCalculation(conn,temp_period_end_dt, due_days,consider_due_dt_in,exclude_sat,comp_cd,holiday_state));
									VPRODUCTION_MONTH.add(period_end_dt.substring(3,temp_period_end_dt.length()));
									
									VCASH_FLOW_NM.add(cashflow);
									VCASH_FLOW.add(invoice_type);
									
									VINVOICE_RAISED_IN.add(invoice_raise_in);
									VACCRUAL_QTY.add(nf.format(temp_qty_mmbtu));
									
									VACCRUAL_AMT.add(accrual_amt);
									VGROSS_AMT.add(accrual_amt);
								}
							}
						}
					}
				}
				rset.close();
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public Double getAccrualParkingQty(String counterparty_cd, String contract_type, String agmt_no, String cont_no, String from_dt, String to_dt, String bu_plant_seq)
	{
		String function_nm="getParkingQty()";
		double qty=0;
		try
		{
			double cumulative_imbalance=0;
			queryString="SELECT SUM((NVL(QTY_MMBTU,0)-NVL(EXIT_QTY_MMBTU,0))+NVL(ADJ_IMBALANCE,0)) "
	  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
	  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
					+ "AND GAS_DT<TO_DATE(?,'DD/MM/YYYY') AND AGMT_NO=? AND CONT_NO=? "
					+ "AND BU_SEQ=? "
					+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
					+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
			stmt_temp=conn.prepareStatement(queryString);
			stmt_temp.setString(1, comp_cd);
			stmt_temp.setString(2, counterparty_cd);
			stmt_temp.setString(3, contract_type);
			stmt_temp.setString(4, from_dt);
			stmt_temp.setString(5, agmt_no);
			stmt_temp.setString(6, cont_no);
			stmt_temp.setString(7, bu_plant_seq);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				cumulative_imbalance=rset_temp.getDouble(1);
			}
			rset_temp.close();
			stmt_temp.close();
			
			queryString="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
					+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
					+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
					+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
			stmt_temp=conn.prepareStatement(queryString);
			stmt_temp.setString(1, from_dt);
			stmt_temp.setString(2, to_dt);
			rset_temp=stmt_temp.executeQuery();
			while(rset_temp.next())
			{
				String gas_dt=rset_temp.getString(1)==null?"":rset_temp.getString(1);
				
				double alloc_entry_qty=0;
				double alloc_exit_qty=0;
				double derived_deparking=0;
				
				queryString1="SELECT QTY_MMBTU,EXIT_QTY_MMBTU,((NVL(QTY_MMBTU,0)-NVL(EXIT_QTY_MMBTU,0)) + NVL(ADJ_IMBALANCE,0)) "
		  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND AGMT_NO=? AND CONT_NO=? "
						+ "AND BU_SEQ=? "
						+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt_temp1=conn.prepareStatement(queryString1);
				stmt_temp1.setString(1, comp_cd);
				stmt_temp1.setString(2, counterparty_cd);
				stmt_temp1.setString(3, contract_type);
				stmt_temp1.setString(4, gas_dt);
				stmt_temp1.setString(5, agmt_no);
				stmt_temp1.setString(6, cont_no);
				stmt_temp1.setString(7, bu_plant_seq);
				rset_temp1=stmt_temp1.executeQuery();
				if(rset_temp1.next())
				{
					alloc_entry_qty=rset_temp1.getDouble(1);
					alloc_exit_qty=rset_temp1.getDouble(2);
					derived_deparking=rset_temp1.getDouble(3);
				}
				rset_temp1.close();
				stmt_temp1.close();
				
				cumulative_imbalance+=derived_deparking;
				
				qty+=cumulative_imbalance;
			}
			rset_temp.close();
			stmt_temp.close();		
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return qty;
	}

	public void getSalesAccrualList()
	{
		String function_nm="getSalesAccrualList()";
		try
		{
			financial_year=utilDate.getFinancialYear(period_end_dt);
			
			if(billing_cycle.equals("8"))
			{
				String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
				String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
				
				queryString="SELECT DISTINCT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,"
						+ "A.TRADE_REF_NO,TO_CHAR(C.EFF_DT,'DD/MM/YYYY'),C.BILLING_DAYS,A.AGMT_BASE,A.FCC_FLAG,C.DUE_DATE,C.DUE_DT_IN,C.EXCL_SAT_MAP,"
						+ "A.DCQ,A.RATE,A.RATE_UNIT,C.INVOICE_CUR_CD,C.EXCHNG_RATE_CD,C.EXCHNG_RATE_CAL,C.EXCHG_VAL,C.EFF_DT "
						+ "FROM FMS_SUPPLY_CONT_MST A, FMS_SUPPLY_BILLING_DTL C "
						+ "WHERE A.COMPANY_CD=? AND A.IS_ALLOCATED='Y' " //AND A.FCC_FLAG='Y' " REMOVED AS DISCUSSED WITH MAM
						+ "AND (A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY')) "
						+ "AND (A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY')) "
						+ "AND A.CONTRACT_TYPE IN ('S','L','X') "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND C.BILLING_FREQ=? "
						+ "AND ((C.EFF_DT>=TO_DATE(?,'DD/MM/YYYY') AND C.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) "
						+ "OR C.EFF_DT=(SELECT MAX(E.EFF_DT) FROM FMS_SUPPLY_BILLING_DTL E WHERE C.COMPANY_CD=E.COMPANY_CD "
						+ "AND C.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND C.AGMT_NO=E.AGMT_NO AND C.CONT_NO=E.CONT_NO "
						+ "AND C.CONTRACT_TYPE=E.CONTRACT_TYPE AND E.EFF_DT<TO_DATE(?,'DD/MM/YYYY'))) "
						+ "ORDER BY C.EFF_DT";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, temp_period_end_dt);
				stmt.setString(3, temp_period_start_dt);
				stmt.setString(4, report_end_dt);
				stmt.setString(5, report_start_dt);
				stmt.setString(6, billing_freq);
				stmt.setString(7, temp_period_start_dt);
				stmt.setString(8, temp_period_end_dt);
				stmt.setString(9, temp_period_start_dt);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					String cont_start_dt=rset.getString(7)==null?"":rset.getString(7);
					String cont_end_dt=rset.getString(8)==null?"":rset.getString(8);
					String contRef=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					String tradeRef=rset.getString(12)==null?"":rset.getString(12);
					
					String agmt_base=rset.getString(15)==null?"":rset.getString(15);
					
					String deal_no="";
					if(cont_type.equals("S"))
					{
						deal_no=cont_type+agmtno+"-"+contno;
					}
					else
					{
						deal_no=cont_type+""+contno;
					}
					if(agmt_base.equals("D"))
					{
						deal_no+="<font style='background: #a6ff4d;'>[DLV]</font>";
					}
					if(cont_type.equals("X"))
					{
						contRef=tradeRef;
					}
					
					String fcc=rset.getString(16)==null?"":rset.getString(16);
					
					String billing_eff_dt=rset.getString(13)==null?"":rset.getString(13);
					String billing_days=rset.getString(14)==null?"1":rset.getString(14);
					
					String due_days=rset.getString(17)==null?"":rset.getString(17);
					String consider_due_dt_in=rset.getString(18)==null?"":rset.getString(18);
					String exclude_sat=rset.getString(19)==null?"":rset.getString(19);
					
					String dcq=rset.getString(20)==null?"":rset.getString(20);
					//String price=rset.getString(21)==null?"":rset.getString(21);
					String price_unit=rset.getString(22)==null?"":rset.getString(22);
					String price_unit_nm=utilBean.getRateUnitNm(conn,price_unit);
					String price = utilBean.RateNumberFormat(rset.getDouble(21), price_unit);
					String invoice_raise_in=rset.getString(23)==null?"":rset.getString(23);
					
					String exchng_rate_cd = rset.getString(24)==null?"":rset.getString(24);
					String exchng_rate_cal = rset.getString(25)==null?"":rset.getString(25);
					
					String fixed_exchng_val=nf2.format(rset.getDouble(26));
					
					int isGreter=utilDate.getDays(billing_eff_dt, temp_period_start_dt);
					
					String issue_st_dt=temp_period_start_dt;
					String issue_end_dt=temp_period_end_dt;
					
					String temp_st_dt="";
					String temp_end_dt="";
					String diff_color="";
					if(isGreter>1)
					{
						temp_st_dt=billing_eff_dt;
					}
					else
					{
						temp_st_dt=temp_period_start_dt;
					}
					
					int temp_count=utilDate.getDays(cont_end_dt,issue_end_dt);
					if(temp_count <= 0)
					{
						issue_end_dt=cont_end_dt;
					}
					
					/*temp_count=utilDate.getDays(temp_period_start_dt,st_dt);
					if(temp_count <= 1)
					{
						temp_period_start_dt=st_dt;
					}*/
					
					String temp_dt = utilDate.getDate(temp_st_dt,"-1");
					int tot_row=1;
					for(int i=0;i<tot_row;i++)
					{
						temp_st_dt=utilDate.getDate(temp_dt,"1");
						temp_dt=utilDate.getDate(temp_dt,billing_days);
						
						//int checkMthEnd=utilDate.getDays(temp_period_end_dt,temp_st_dt);
						int checkMthEnd=utilDate.getDays(issue_end_dt,temp_st_dt);
						boolean isBreak=false;
						if(Integer.parseInt(billing_days) <= checkMthEnd)
						{
							temp_end_dt = temp_dt;
							
							/*if(utilDate.getDays(cont_end_dt,temp_end_dt) <=0)
							{
								temp_end_dt=cont_end_dt;
							}*/
							//isBreak=false;
						}
						else
						{
							//temp_end_dt = temp_period_end_dt;
							temp_end_dt = issue_end_dt;
							//isBreak=true;
							break;
						}
						
						if(utilDate.getDays(cont_end_dt, temp_end_dt)<=0)
						{
							temp_end_dt=cont_end_dt;
							diff_color="blue";
						}
						
						String innerBillingEffDt=billing_eff_dt; //defaualt added
						String innerBillingDays="";
						String innerBillingFreq="";
						queryString1="SELECT DISTINCT TO_CHAR(EFF_DT,'DD/MM/YYYY'),BILLING_DAYS,BILLING_FREQ "
								+ "FROM FMS_SUPPLY_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND CONT_NO=? "
								+ "AND CONTRACT_TYPE=? "
								+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_SUPPLY_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
								+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY'))";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, own_cd);
						stmt1.setString(2, countpty_cd);
						stmt1.setString(3, agmtno);
						stmt1.setString(4, contno);
						stmt1.setString(5, cont_type);
						stmt1.setString(6, temp_end_dt);
						rset1 = stmt1.executeQuery();
						if(rset1.next())
						{
							innerBillingEffDt=rset1.getString(1)==null?"":rset1.getString(1);
							innerBillingDays=rset1.getString(2)==null?"1":rset1.getString(2);
							innerBillingFreq=rset1.getString(3)==null?"":rset1.getString(3);
						}
						rset1.close();
						stmt1.close();
						//System.out.println(billing_eff_dt+"==="+innerBillingEffDt);
						if(!billing_eff_dt.equals(innerBillingEffDt))
						{
							/*if(billing_freq.equals(innerBillingFreq))
							{
								billing_days=innerBillingDays;
								tot_row+=1;
							}
							else*/
							{
								//System.out.println("billing before break!");
								break;
							}
						}
						else
						{
							tot_row+=1;
						}
						
						queryString1="SELECT PLANT_SEQ_NO "
								+ "FROM FMS_SUPPLY_CONT_PLANT "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, countpty_cd);
						stmt1.setString(3, contno);
						stmt1.setString(4, contrev);
						stmt1.setString(5, agmtno);
						stmt1.setString(6, agmtrev);
						stmt1.setString(7, cont_type);
						rset1 = stmt1.executeQuery();
						while(rset1.next())
						{
							String plant_seq = rset1.getString(1)==null?"":rset1.getString(1);
							String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "C");
							String transportation_charges="";
							String marketing_margin="";
							String other_charges="";
							
							queryString2="SELECT CHARGE_RATE,CHARGE_ABBR "
									+ "FROM FMS_SUPPLY_CONT_PLANT_CHRG A "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? AND AGMT_REV=? "
									+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? "
									+ "AND A.EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_SUPPLY_CONT_PLANT_CHRG B WHERE A.COMPANY_CD=B.COMPANY_CD "
									+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
									+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.CHARGE_ABBR=B.CHARGE_ABBR "
									+ "AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
							stmt2=conn.prepareStatement(queryString2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, countpty_cd);
							stmt2.setString(3, agmtno);
							stmt2.setString(4, agmtrev);
							stmt2.setString(5, contno);
							stmt2.setString(6, cont_type);
							stmt2.setString(7, plant_seq);
							stmt2.setString(8, temp_end_dt);
							rset2=stmt2.executeQuery();
							while(rset2.next())
							{
								String charge_abbr=rset2.getString(2)==null?"":rset2.getString(2);
								if(charge_abbr.equals("TC"))
								{
									transportation_charges=rset2.getString(1)==null?"":nf2.format(rset2.getDouble(1));
								}
								else if(charge_abbr.equals("OC"))
								{
									other_charges=rset2.getString(1)==null?"":nf2.format(rset2.getDouble(1));
								}
								else if(charge_abbr.equals("MM"))
								{
									marketing_margin=rset2.getString(1)==null?"":nf2.format(rset2.getDouble(1));
								}
							}
							rset2.close();
							stmt2.close();
							
							String holiday_state="";
							queryString2="SELECT HOLIDAY_STATE "
									+ "FROM FMS_SUPPLY_BILLING_DTL A "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
									+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
									+ "AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? "
									+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SUPPLY_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
									+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO "
									+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) ";
							stmt2=conn.prepareStatement(queryString2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, countpty_cd);
							stmt2.setString(3, agmtno);
							stmt2.setString(4, contno);
							stmt2.setString(5, cont_type);
							stmt2.setString(6, plant_seq);
							stmt2.setString(7, temp_end_dt);
							rset2=stmt2.executeQuery();
							if(rset.next())
							{
								holiday_state=rset2.getString(1)==null?"":rset2.getString(1);
							}
							rset2.close();
							stmt2.close();
							
							queryString2="SELECT PLANT_SEQ_NO "
									+ "FROM FMS_SUPPLY_CONT_BU "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, countpty_cd);
							stmt2.setString(3, contno);
							stmt2.setString(4, contrev);
							stmt2.setString(5, agmtno);
							stmt2.setString(6, agmtrev);
							stmt2.setString(7, cont_type);
							rset2 = stmt2.executeQuery();
							while(rset2.next())
							{
								String bu_plant_seq = rset2.getString(1)==null?"":rset2.getString(1);
								String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
								
								String state_code="";
								queryString3="SELECT TIN "
										+ "FROM FMS_STATE_MST "
										+ "WHERE STATE_NM=(SELECT PLANT_STATE FROM FMS_COUNTERPARTY_PLANT_DTL A WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND ENTITY='B' AND SEQ_NO=? AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
										+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')))";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, own_cd);
								stmt3.setString(2, own_cd);
								stmt3.setString(3, bu_plant_seq);
								rset3 = stmt3.executeQuery();
								if(rset3.next())
								{
									state_code=rset3.getString(1)==null?"":rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
								int isInvExist=0;
								queryString3="SELECT COUNT(*) "
										+ "FROM FMS_INVOICE_MST "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
										+ "AND AGMT_NO=? AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_UNIT=? "
										+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
										+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_STATE_TIN=? "
										+ "AND FINANCIAL_YEAR=? AND PDF_INV_DTL IS NOT NULL";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, own_cd);
								stmt3.setString(2, countpty_cd);
								stmt3.setString(3, contno);
								stmt3.setString(4, agmtno);
								stmt3.setString(5, plant_seq);
								stmt3.setString(6, cont_type);
								stmt3.setString(7, bu_plant_seq);
								stmt3.setString(8, billing_cycle);
								stmt3.setString(9, temp_st_dt);
								stmt3.setString(10, temp_end_dt);
								stmt3.setString(11, state_code);
								stmt3.setString(12, financial_year);
								rset3 = stmt3.executeQuery();
								if(rset3.next())
								{
									isInvExist=rset3.getInt(1);
									if(isInvExist > 0 && !fcc.equals("Y"))
									{
										fcc="Y";
									}
								}
								rset3.close();
								stmt3.close();
								
								if(isInvExist==0)
								{
									double qtyMMBTU=0;
									
									String tempEndDate=temp_end_dt;
									if(utilDate.getDays(cont_end_dt, temp_end_dt) <= 0)
									{
										tempEndDate=cont_end_dt;
									}
									
									String cont_map=countpty_cd+"-"+cont_type+"-"+agmtno+"-%-"+contno+"-%";
									String exit_point="C-"+countpty_cd+"-"+plant_seq;
									
									queryString4="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
											+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
											+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
											+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
									stmt4 = conn.prepareStatement(queryString4);
									stmt4.setString(1, temp_st_dt);
									stmt4.setString(2, tempEndDate);
									rset4 = stmt4.executeQuery();
									while(rset4.next())
									{
										String date=rset4.getString(1)==null?"":rset4.getString(1);
										
										queryString5="SELECT COALESCE(ALLOCATION, SELLER_NOM, BUYER_NOM, DCQ, 0) "
												+ "FROM "
												+ "(SELECT ";
										if(agmt_base.equals("D")) {
											queryString5+="(SELECT COALESCE(GTA_QTY, ALLOC_QTY, NULL) "
													+ "FROM "
													+ "(SELECT (SELECT SUM(EXIT_QTY_MMBTU) "
									  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
									  				+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
									  				+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
													+ "AND SELL_CONT_MAP LIKE ? AND BU_SEQ=? "
													+ "AND EXIT_PT_MAPPING_ID=? "
													+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
													+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
													+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
													+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
													+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID)) GTA_QTY, "
													+ "(SELECT SUM(QTY_MMBTU) "
									  				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
									  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
													+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
													+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
													+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
													+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
													+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
													+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
													+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
													+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
													+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) ALLOC_QTY FROM DUAL)) ALLOCATION, ";
										}else {
											queryString5+= "(SELECT SUM(QTY_MMBTU) "
									  				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
									  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
													+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
													+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
													+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
													+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
													+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
													+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
													+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
													+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
													+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) ALLOCATION, ";
										}
										queryString5+= "(SELECT SUM(QTY_MMBTU) "
								  				+ "FROM FMS_DAILY_SELLER_NOM A "
								  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
												+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
												+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
												+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
												+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_SELLER_NOM B "
												+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
												+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
												+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
												+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
												+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) SELLER_NOM, "
												+ "(SELECT SUM(QTY_MMBTU) "
								  				+ "FROM FMS_DAILY_BUYER_NOM A "
								  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
												+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
												+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
												+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
												+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_BUYER_NOM B "
												+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
												+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
												+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
												+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
												+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) BUYER_NOM, "
												+ "(NVL((SELECT DCQ "
												+ "FROM FMS_SUPPLY_CONT_DCQ_DTL "
												+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
												+ "AND AGMT_NO=? AND CONT_NO=? "
												+ "AND CONTRACT_TYPE=? AND STATUS=? "
												+ "AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT>=TO_DATE(?,'DD/MM/YYYY')),?)) DCQ FROM DUAL) ";
										stmt5 = conn.prepareStatement(queryString5);
										int stmt_count=0;
										if(agmt_base.equals("D")) {
											stmt5.setString(++stmt_count, comp_cd);
											stmt5.setString(++stmt_count, "C");
											stmt5.setString(++stmt_count, date);
											stmt5.setString(++stmt_count, cont_map);
											stmt5.setString(++stmt_count, bu_plant_seq);
											stmt5.setString(++stmt_count, exit_point);
											stmt5.setString(++stmt_count, contno);
											stmt5.setString(++stmt_count, agmtno);
											stmt5.setString(++stmt_count, comp_cd);
											stmt5.setString(++stmt_count, countpty_cd);
											stmt5.setString(++stmt_count, plant_seq);
											stmt5.setString(++stmt_count, cont_type);
											stmt5.setString(++stmt_count, bu_plant_seq);
											stmt5.setString(++stmt_count, date);
											
										}else {
											stmt5.setString(++stmt_count, contno);
											stmt5.setString(++stmt_count, agmtno);
											stmt5.setString(++stmt_count, comp_cd);
											stmt5.setString(++stmt_count, countpty_cd);
											stmt5.setString(++stmt_count, plant_seq);
											stmt5.setString(++stmt_count, cont_type);
											stmt5.setString(++stmt_count, bu_plant_seq);
											stmt5.setString(++stmt_count, date);
										}
										stmt5.setString(++stmt_count, contno);
										stmt5.setString(++stmt_count, agmtno);
										stmt5.setString(++stmt_count, comp_cd);
										stmt5.setString(++stmt_count, countpty_cd);
										stmt5.setString(++stmt_count, plant_seq);
										stmt5.setString(++stmt_count, cont_type);
										stmt5.setString(++stmt_count, bu_plant_seq);
										stmt5.setString(++stmt_count, date);
										stmt5.setString(++stmt_count, contno);
										stmt5.setString(++stmt_count, agmtno);
										stmt5.setString(++stmt_count, comp_cd);
										stmt5.setString(++stmt_count, countpty_cd);
										stmt5.setString(++stmt_count, plant_seq);
										stmt5.setString(++stmt_count, cont_type);
										stmt5.setString(++stmt_count, bu_plant_seq);
										stmt5.setString(++stmt_count, date);
										stmt5.setString(++stmt_count, comp_cd);
										stmt5.setString(++stmt_count, countpty_cd);
										stmt5.setString(++stmt_count, agmtno);
										stmt5.setString(++stmt_count, contno);
										stmt5.setString(++stmt_count, cont_type);
										stmt5.setString(++stmt_count, "Y");
										stmt5.setString(++stmt_count, date);
										stmt5.setString(++stmt_count, date);
										stmt5.setString(++stmt_count, dcq);
										rset5 = stmt5.executeQuery();
										if(rset5.next())
										{
											qtyMMBTU+=rset5.getDouble(1);
										}
										rset5.close();
										stmt5.close();
									}
									rset4.close();
									stmt4.close();
									
									String new_price="";
									queryString4 = "SELECT DISTINCT NEW_SALE_PRICE "
											+ "FROM FMS_SUPPLY_ALLOC_REVISED A "
											+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND CONT_NO=? "
											+ "AND COUNTERPARTY_CD=? AND FLAG='A' AND CONTRACT_TYPE=? "
											+ "AND MODIFICATION_SEQ_NO = (SELECT MAX(MODIFICATION_SEQ_NO) FROM FMS_SUPPLY_ALLOC_REVISED B "
											+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
											+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.FLAG=B.FLAG AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
											+ "AND B.NEW_PRICE_EFF_DT <=TO_DATE(?,'DD/MM/YYYY'))";
									stmt4 = conn.prepareStatement(queryString4);
									stmt4.setString(1, comp_cd);
									stmt4.setString(2, agmtno);
									stmt4.setString(3, contno);
									stmt4.setString(4, countpty_cd);
									stmt4.setString(5, cont_type);
									stmt4.setString(6, temp_end_dt);
									rset4=stmt4.executeQuery();
									if(rset4.next())
									{
										new_price=rset4.getString(1)==null?"":rset4.getString(1);
										if(!new_price.equals(""))
										{
											price = utilBean.RateNumberFormat(rset4.getDouble(1), price_unit);
										}
									}
									rset4.close();
									stmt4.close();
									double exchng_rate=0;
									String exchng_rate_dt="";
									if(exchng_rate_cd.equals("0")) //FOR FIXED EXCHANGE RATE
									{
										exchng_rate=Double.parseDouble(fixed_exchng_val);
									}
									else
									{
										queryString4="SELECT NVL(EXCHG_VAL,0),TO_CHAR(EFF_DT,'DD/MM/YYYY') "
												+ "FROM FMS_EXCHG_RATE_ENTRY A "
												+ "WHERE EXCHG_RATE_CD=? "
												+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY') "
												+ "ORDER BY EFF_DT DESC";
										stmt4 = conn.prepareStatement(queryString4);
										stmt4.setString(1, exchng_rate_cd);
										stmt4.setString(2, temp_end_dt);
										rset4 = stmt4.executeQuery();
										if(rset4.next())
										{
											exchng_rate=rset4.getDouble(1);
											exchng_rate_dt=rset4.getString(2)==null?"":rset4.getString(2);
										}
										rset4.close();
										stmt4.close();
									}
									
									double accrual_amt=0;
									double charges_amt=0;
									if(!price.equals(""))
									{
										accrual_amt=qtyMMBTU * Double.parseDouble(price);
									}
									
									if(!transportation_charges.equals(""))
									{
										charges_amt+= qtyMMBTU * Double.parseDouble(transportation_charges);
									}
									
									if(!marketing_margin.equals(""))
									{
										charges_amt+= qtyMMBTU * Double.parseDouble(marketing_margin);
									}
									
									if(!other_charges.equals(""))
									{
										charges_amt+= qtyMMBTU * Double.parseDouble(other_charges);
									}
									
									if(price_unit.equals("2") && charges_amt > 0)
									{
										accrual_amt+=charges_amt / exchng_rate;
									}
									else
									{
										accrual_amt+=charges_amt;
									}
									
									double gross_amt=0;
									if(price_unit.equals("2"))
									{
										gross_amt=accrual_amt * exchng_rate;
									}
									else
									{
										gross_amt=accrual_amt;
									}
									
									if(qtyMMBTU>0 && accrual_amt>0)
									{
										VBU_STATE_TIN.add(state_code);							
										VFINANCIAL_YEAR.add(financial_year);
										VCOUNTERPTY_CD.add(countpty_cd);
										VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
										VCOUNTERPTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
										VAGMT_NO.add(agmtno);
										VAGMT_REV_NO.add(agmtrev);
										VCONT_NO.add(contno);
										VCONT_REV_NO.add(contrev);
										VCARGO_NO.add("0");
										VCONTRACT_TYPE.add(cont_type);
										VSTART_DT.add(cont_start_dt);
										VEND_DT.add(cont_end_dt);
										VDIS_CONT_MAPPING.add(deal_no);
										VCONT_REF_NO.add(contRef);
										VPLANT_SEQ.add(plant_seq);
										VPLANT_ABBR.add(plant_abbr);
										VBU_PLANT_SEQ.add(bu_plant_seq);
										VBU_PLANT_ABBR.add(bu_plant_abbr);
										VPERIOD_START_DT.add(temp_st_dt);
										VPERIOD_END_DT.add(temp_end_dt);
										VBILLING_FREQ_FLAG.add(billing_cycle);
										VBILLING_FREQ_NM.add(billing_freq_nm);
										VPRODUCTION_MONTH.add(month+"/"+year);
										VINVOICE_DUE_DT.add(utilBean.DueDateCalculation(conn,temp_end_dt, due_days,consider_due_dt_in,exclude_sat,comp_cd,holiday_state));
										
										VAGMT_BASE.add(agmt_base);
										VCASH_FLOW.add("Commodity");
										
										VACCRUAL_QTY.add(nf.format(qtyMMBTU));
										VACCRUAL_AMT.add(nf.format(accrual_amt));
										
										VSALES_PRICE_CD.add(price_unit);
										VSALES_PRICE_NM.add(price_unit_nm);
										VINVOICE_RAISED_IN.add(invoice_raise_in);
										
										if(price_unit.equals("2"))
										{
											VEXCHNG_RATE.add(nf2.format(exchng_rate));
											VEXCHNG_RATE_CD.add(exchng_rate_cd);
											VEXCHNG_RATE_DT.add(exchng_rate_dt);
										}
										else
										{
											VEXCHNG_RATE.add("");
											VEXCHNG_RATE_CD.add("");
											VEXCHNG_RATE_DT.add("");
										}
										VGROSS_AMT.add(nf.format(gross_amt));
									}
								}
							}
							rset2.close();
							stmt2.close();
						}
						rset1.close();
						stmt1.close();
						
						/*if(isBreak)
						{
							//System.out.println("End Date Break!");
							break;
						}*/
						//System.out.println(tot_row+"-->");
					}
				}
				rset.close();
				stmt.close();
			}
			else
			{
				/*queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,"
						+ "A.TRADE_REF_NO,TO_CHAR(C.EFF_DT,'DD/MM/YYYY'),A.AGMT_BASE,A.FCC_FLAG,C.DUE_DATE,C.DUE_DT_IN,C.EXCLUDE_SAT,"
						+ "A.DCQ,A.RATE,A.RATE_UNIT,C.INVOICE_CUR_CD,C.EXCHNG_RATE_CD,C.EXCHNG_RATE_CAL,C.EXCHG_VAL "
						+ "FROM FMS_SUPPLY_CONT_MST A, FMS_SUPPLY_BILLING_DTL C "
						+ "WHERE A.COMPANY_CD=? AND A.IS_ALLOCATED='Y' " //AND A.FCC_FLAG='Y' " REMOVED AS DISCUSSED WITH MAM
						+ "AND (A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY')) "
						+ "AND (A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY')) "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND C.BILLING_FREQ=? "
						+ "AND C.EFF_DT=(SELECT MAX(E.EFF_DT) FROM FMS_SUPPLY_BILLING_DTL E WHERE C.COMPANY_CD=E.COMPANY_CD "
						+ "AND C.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND C.AGMT_NO=E.AGMT_NO AND C.CONT_NO=E.CONT_NO "
						+ "AND C.CONTRACT_TYPE=E.CONTRACT_TYPE AND E.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) ";
				*/
				queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,"
						+ "A.TRADE_REF_NO,TO_CHAR(C.EFF_DT,'DD/MM/YYYY'),A.AGMT_BASE,A.FCC_FLAG,C.DUE_DATE,C.DUE_DT_IN,C.EXCLUDE_SAT,"
						+ "A.DCQ,A.RATE,A.RATE_UNIT,C.INVOICE_CUR_CD,C.EXCHNG_RATE_CD,C.EXCHNG_RATE_CAL,C.EXCHG_VAL,C.HOLIDAY_STATE,"
						+ "D.PLANT_SEQ_NO,E.PLANT_SEQ_NO "		
						+ "FROM FMS_SUPPLY_CONT_MST A, "
							+ "FMS_SUPPLY_BILLING_DTL C, "
							+ "FMS_SUPPLY_CONT_PLANT D, "
							+ "FMS_SUPPLY_CONT_BU E "
						+ "WHERE A.COMPANY_CD=? AND A.IS_ALLOCATED='Y' " //AND A.FCC_FLAG='Y' " REMOVED AS DISCUSSED WITH MAM
						+ "AND (A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY')) "
						+ "AND (A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY')) "
						+ "AND A.CONTRACT_TYPE IN ('S','L','X') "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ ""
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND C.BILLING_FREQ=? AND D.PLANT_SEQ_NO=C.PLANT_SEQ_NO "
						+ "AND C.EFF_DT=(SELECT MAX(E.EFF_DT) FROM FMS_SUPPLY_BILLING_DTL E WHERE C.COMPANY_CD=E.COMPANY_CD "
							+ "AND C.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND C.AGMT_NO=E.AGMT_NO AND C.CONT_NO=E.CONT_NO "
							+ "AND C.CONTRACT_TYPE=E.CONTRACT_TYPE AND E.PLANT_SEQ_NO=C.PLANT_SEQ_NO AND E.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) "
						+ ""
						+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV "
						+ "AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE "
						+ ""
						+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO AND A.CONT_REV=E.CONT_REV "
						+ "AND A.AGMT_NO=E.AGMT_NO AND A.AGMT_REV=E.AGMT_REV AND A.CONTRACT_TYPE=E.CONTRACT_TYPE ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, period_end_dt);
				stmt.setString(3, period_start_dt);
				stmt.setString(4, report_end_dt);
				stmt.setString(5, report_start_dt);
				stmt.setString(6, billing_freq);
				stmt.setString(7, period_end_dt);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					String cont_start_dt=rset.getString(7)==null?"":rset.getString(7);
					String cont_end_dt=rset.getString(8)==null?"":rset.getString(8);
					String contRef=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					String tradeRef=rset.getString(12)==null?"":rset.getString(12);
					
					String billing_eff_dt=rset.getString(13)==null?"":rset.getString(13);
					String agmt_base=rset.getString(14)==null?"":rset.getString(14);
					
					String deal_no="";
					if(cont_type.equals("S"))
					{
						deal_no=cont_type+agmtno+"-"+contno;
					}
					else
					{
						deal_no=cont_type+""+contno;
					}
					if(agmt_base.equals("D"))
					{
						deal_no+="<font style='background: #a6ff4d;'>[DLV]</font>";
					}
					
					if(cont_type.equals("X"))
					{
						contRef=tradeRef;
					}
					
					String fcc=rset.getString(15)==null?"":rset.getString(15);
					
					String due_days=rset.getString(16)==null?"":rset.getString(16);
					String consider_due_dt_in=rset.getString(17)==null?"":rset.getString(17);
					String exclude_sat=rset.getString(18)==null?"":rset.getString(18);
					
					String dcq=rset.getString(19)==null?"":rset.getString(19);
					//String price=rset.getString(20)==null?"":rset.getString(20);
					String price_unit=rset.getString(21)==null?"":rset.getString(21);
					String price_unit_nm=utilBean.getRateUnitNm(conn,price_unit);
					String price = utilBean.RateNumberFormat(rset.getDouble(20), price_unit);
					
					String invoice_raise_in=rset.getString(22)==null?"":rset.getString(22);
					
					String exchng_rate_cd = rset.getString(23)==null?"":rset.getString(23);
					String exchng_rate_cal = rset.getString(24)==null?"":rset.getString(24);
					String fixed_exchng_val=nf2.format(rset.getDouble(25));
					String holiday_state = rset.getString(26)==null?"":rset.getString(26);
					
					String plant_seq = rset.getString(27)==null?"":rset.getString(27);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "C");
					
					String bu_plant_seq = rset.getString(28)==null?"":rset.getString(28);
					String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
					
					String transportation_charges="";
					String marketing_margin="";
					String other_charges="";
							
					int isGreter=utilDate.getDays(billing_eff_dt, period_start_dt);
					
					String temp_st_dt="";
					String temp_end_dt="";
					String diff_color="";
					if(isGreter>1)
					{
						temp_st_dt=billing_eff_dt;
						temp_end_dt=period_end_dt;
						diff_color="blue";
					}
					else
					{
						temp_st_dt=period_start_dt;
						temp_end_dt=period_end_dt;
						diff_color="";
					}
					
					if(utilDate.getDays(cont_end_dt, temp_end_dt)<=0)
					{
						temp_end_dt=cont_end_dt;
						diff_color="blue";
					}
					
					String state_code="";
					queryString3="SELECT TIN "
							+ "FROM FMS_STATE_MST "
							+ "WHERE STATE_NM=(SELECT PLANT_STATE FROM FMS_COUNTERPARTY_PLANT_DTL A WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND ENTITY='B' AND SEQ_NO=? AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')))";
					stmt3 = conn.prepareStatement(queryString3);
					stmt3.setString(1, own_cd);
					stmt3.setString(2, own_cd);
					stmt3.setString(3, bu_plant_seq);
					rset3 = stmt3.executeQuery();
					if(rset3.next())
					{
						state_code=rset3.getString(1)==null?"":rset3.getString(1);
					}
					rset3.close();
					stmt3.close();
					
					int isInvExist=0;
					queryString3="SELECT COUNT(*) "
							+ "FROM FMS_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_UNIT=? "
							+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_STATE_TIN=? "
							+ "AND FINANCIAL_YEAR=? AND PDF_INV_DTL IS NOT NULL";
					stmt3 = conn.prepareStatement(queryString3);
					stmt3.setString(1, own_cd);
					stmt3.setString(2, countpty_cd);
					stmt3.setString(3, contno);
					stmt3.setString(4, agmtno);
					stmt3.setString(5, plant_seq);
					stmt3.setString(6, cont_type);
					stmt3.setString(7, bu_plant_seq);
					stmt3.setString(8, billing_cycle);
					stmt3.setString(9, temp_st_dt);
					stmt3.setString(10, temp_end_dt);
					stmt3.setString(11, state_code);
					stmt3.setString(12, financial_year);
					rset3 = stmt3.executeQuery();
					if(rset3.next())
					{
						isInvExist=rset3.getInt(1);
						if(isInvExist > 0 && !fcc.equals("Y"))
						{
							fcc="Y";
						}
					}
					rset3.close();
					stmt3.close();
					
					if(isInvExist==0)
					{
						queryString1="SELECT CHARGE_RATE,CHARGE_ABBR "
								+ "FROM FMS_SUPPLY_CONT_PLANT_CHRG A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? "
								+ "AND A.EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_SUPPLY_CONT_PLANT_CHRG B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
								+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.CHARGE_ABBR=B.CHARGE_ABBR "
								+ "AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, countpty_cd);
						stmt1.setString(3, agmtno);
						stmt1.setString(4, agmtrev);
						stmt1.setString(5, contno);
						stmt1.setString(6, cont_type);
						stmt1.setString(7, plant_seq);
						stmt1.setString(8, temp_end_dt);
						rset1=stmt1.executeQuery();
						while(rset1.next())
						{
							String charge_abbr=rset1.getString(2)==null?"":rset1.getString(2);
							if(charge_abbr.equals("TC"))
							{
								transportation_charges=rset1.getString(1)==null?"":nf2.format(rset1.getDouble(1));
							}
							else if(charge_abbr.equals("OC"))
							{
								other_charges=rset1.getString(1)==null?"":nf2.format(rset1.getDouble(1));
							}
							else if(charge_abbr.equals("MM"))
							{
								marketing_margin=rset1.getString(1)==null?"":nf2.format(rset1.getDouble(1));
							}
						}
						rset1.close();
						stmt1.close();
						
						double qtyMMBTU=0;
						
						String tempEndDate=temp_end_dt;
						if(utilDate.getDays(cont_end_dt, temp_end_dt) <= 0)
						{
							tempEndDate=cont_end_dt;
						}
						
						String cont_map=countpty_cd+"-"+cont_type+"-"+agmtno+"-%-"+contno+"-%";
						String exit_point="C-"+countpty_cd+"-"+plant_seq;
						
						queryString4="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
								+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
								+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
								+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, temp_st_dt);
						stmt4.setString(2, tempEndDate);
						rset4 = stmt4.executeQuery();
						while(rset4.next())
						{
							String date=rset4.getString(1)==null?"":rset4.getString(1);
							
							queryString5="SELECT COALESCE(ALLOCATION, SELLER_NOM, BUYER_NOM, DCQ, 0) "
									+ "FROM "
									+ "(SELECT ";
							if(agmt_base.equals("D")) {
								queryString5+="(SELECT COALESCE(GTA_QTY, ALLOC_QTY, NULL) "
										+ "FROM "
										+ "(SELECT (SELECT SUM(EXIT_QTY_MMBTU) "
						  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
						  				+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						  				+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
										+ "AND SELL_CONT_MAP LIKE ? AND BU_SEQ=? "
										+ "AND EXIT_PT_MAPPING_ID=? "
										+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
										+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
										+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
										+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
										+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID)) GTA_QTY, "
										+ "(SELECT SUM(QTY_MMBTU) "
						  				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
						  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
										+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
										+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
										+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
										+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
										+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
										+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
										+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
										+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) ALLOC_QTY FROM DUAL)) ALLOCATION, ";
							}else {
								queryString5+= "(SELECT SUM(QTY_MMBTU) "
						  				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
						  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
										+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
										+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
										+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
										+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
										+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
										+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
										+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
										+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) ALLOCATION, ";
							}
							queryString5+= "(SELECT SUM(QTY_MMBTU) "
					  				+ "FROM FMS_DAILY_SELLER_NOM A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_SELLER_NOM B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) SELLER_NOM, "
									+ "(SELECT SUM(QTY_MMBTU) "
					  				+ "FROM FMS_DAILY_BUYER_NOM A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_BUYER_NOM B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) BUYER_NOM, "
									+ "(NVL((SELECT DCQ "
									+ "FROM FMS_SUPPLY_CONT_DCQ_DTL "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? AND CONT_NO=? "
									+ "AND CONTRACT_TYPE=? AND STATUS=? "
									+ "AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT>=TO_DATE(?,'DD/MM/YYYY')),?)) DCQ FROM DUAL) ";
							stmt5 = conn.prepareStatement(queryString5);
							int stmt_count=0;
							if(agmt_base.equals("D")) {
								stmt5.setString(++stmt_count, comp_cd);
								stmt5.setString(++stmt_count, "C");
								stmt5.setString(++stmt_count, date);
								stmt5.setString(++stmt_count, cont_map);
								stmt5.setString(++stmt_count, bu_plant_seq);
								stmt5.setString(++stmt_count, exit_point);
								stmt5.setString(++stmt_count, contno);
								stmt5.setString(++stmt_count, agmtno);
								stmt5.setString(++stmt_count, comp_cd);
								stmt5.setString(++stmt_count, countpty_cd);
								stmt5.setString(++stmt_count, plant_seq);
								stmt5.setString(++stmt_count, cont_type);
								stmt5.setString(++stmt_count, bu_plant_seq);
								stmt5.setString(++stmt_count, date);
								
							}else {
								stmt5.setString(++stmt_count, contno);
								stmt5.setString(++stmt_count, agmtno);
								stmt5.setString(++stmt_count, comp_cd);
								stmt5.setString(++stmt_count, countpty_cd);
								stmt5.setString(++stmt_count, plant_seq);
								stmt5.setString(++stmt_count, cont_type);
								stmt5.setString(++stmt_count, bu_plant_seq);
								stmt5.setString(++stmt_count, date);
							}
							stmt5.setString(++stmt_count, contno);
							stmt5.setString(++stmt_count, agmtno);
							stmt5.setString(++stmt_count, comp_cd);
							stmt5.setString(++stmt_count, countpty_cd);
							stmt5.setString(++stmt_count, plant_seq);
							stmt5.setString(++stmt_count, cont_type);
							stmt5.setString(++stmt_count, bu_plant_seq);
							stmt5.setString(++stmt_count, date);
							stmt5.setString(++stmt_count, contno);
							stmt5.setString(++stmt_count, agmtno);
							stmt5.setString(++stmt_count, comp_cd);
							stmt5.setString(++stmt_count, countpty_cd);
							stmt5.setString(++stmt_count, plant_seq);
							stmt5.setString(++stmt_count, cont_type);
							stmt5.setString(++stmt_count, bu_plant_seq);
							stmt5.setString(++stmt_count, date);
							stmt5.setString(++stmt_count, comp_cd);
							stmt5.setString(++stmt_count, countpty_cd);
							stmt5.setString(++stmt_count, agmtno);
							stmt5.setString(++stmt_count, contno);
							stmt5.setString(++stmt_count, cont_type);
							stmt5.setString(++stmt_count, "Y");
							stmt5.setString(++stmt_count, date);
							stmt5.setString(++stmt_count, date);
							stmt5.setString(++stmt_count, dcq);
							rset5 = stmt5.executeQuery();
							while(rset5.next())
							{
								qtyMMBTU+=rset5.getDouble(1);
							}
							rset5.close();
							stmt5.close();
						}
						rset4.close();
						stmt4.close();
						
						String new_price="";
						queryString4 = "SELECT DISTINCT NEW_SALE_PRICE "
								+ "FROM FMS_SUPPLY_ALLOC_REVISED A "
								+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND CONT_NO=? "
								+ "AND COUNTERPARTY_CD=? AND FLAG='A' AND CONTRACT_TYPE=? "
								+ "AND MODIFICATION_SEQ_NO = (SELECT MAX(MODIFICATION_SEQ_NO) FROM FMS_SUPPLY_ALLOC_REVISED B "
								+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.FLAG=B.FLAG AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
								+ "AND B.NEW_PRICE_EFF_DT <=TO_DATE(?,'DD/MM/YYYY'))";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, comp_cd);
						stmt4.setString(2, agmtno);
						stmt4.setString(3, contno);
						stmt4.setString(4, countpty_cd);
						stmt4.setString(5, cont_type);
						stmt4.setString(6, temp_end_dt);
						rset4 =stmt4.executeQuery();
						if(rset4.next())
						{
							new_price=rset4.getString(1)==null?"":rset4.getString(1);
							if(!new_price.equals(""))
							{
								price = utilBean.RateNumberFormat(rset4.getDouble(1), price_unit);
							}
						}
						rset4.close();
						stmt4.close();
						
						double exchng_rate=0;
						String exchng_rate_dt="";
						if(exchng_rate_cd.equals("0")) //FOR FIXED EXCHANGE RATE
						{
							exchng_rate=Double.parseDouble(fixed_exchng_val);
						}
						else
						{
							queryString4="SELECT NVL(EXCHG_VAL,0),TO_CHAR(EFF_DT,'DD/MM/YYYY') "
									+ "FROM FMS_EXCHG_RATE_ENTRY A "
									+ "WHERE EXCHG_RATE_CD=? "
									+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY') "
									+ "ORDER BY EFF_DT DESC";
							stmt4 = conn.prepareStatement(queryString4);
							stmt4.setString(1, exchng_rate_cd);
							stmt4.setString(2, temp_end_dt);
							rset4 = stmt4.executeQuery();
							if(rset4.next())
							{
								exchng_rate=rset4.getDouble(1);
								exchng_rate_dt=rset4.getString(2)==null?"":rset4.getString(2);
							}
							rset4.close();
							stmt4.close();
						}
						
						double accrual_amt=0;
						double charges_amt=0;
						if(!price.equals(""))
						{
							accrual_amt=qtyMMBTU * Double.parseDouble(price);
						}
						
						if(!transportation_charges.equals(""))
						{
							charges_amt+= qtyMMBTU * Double.parseDouble(transportation_charges);
						}
						
						if(!marketing_margin.equals(""))
						{
							charges_amt+= qtyMMBTU * Double.parseDouble(marketing_margin);
						}
						
						if(!other_charges.equals(""))
						{
							charges_amt+= qtyMMBTU * Double.parseDouble(other_charges);
						}
						
						if(price_unit.equals("2") && charges_amt > 0)
						{
							accrual_amt+=charges_amt / exchng_rate;
						}
						else
						{
							accrual_amt+=charges_amt;
						}
						
						double gross_amt=0;
						if(price_unit.equals("2"))
						{
							gross_amt=accrual_amt * exchng_rate;
						}
						else
						{
							gross_amt=accrual_amt;
						}
						
						if(qtyMMBTU>0 && accrual_amt>0)
						{
							VBU_STATE_TIN.add(state_code);							
							VFINANCIAL_YEAR.add(financial_year);
							VCOUNTERPTY_CD.add(countpty_cd);
							VAGMT_NO.add(agmtno);
							VAGMT_REV_NO.add(agmtrev);
							VCONT_NO.add(contno);
							VCONT_REV_NO.add(contrev);
							VCARGO_NO.add("0");
							VCONTRACT_TYPE.add(cont_type);
							VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
							VCOUNTERPTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
							VSTART_DT.add(cont_start_dt);
							VEND_DT.add(cont_end_dt);
							VDIS_CONT_MAPPING.add(deal_no);
							VCONT_REF_NO.add(contRef);
							VPLANT_SEQ.add(plant_seq);
							VPLANT_ABBR.add(plant_abbr);
							VBU_PLANT_SEQ.add(bu_plant_seq);
							VBU_PLANT_ABBR.add(bu_plant_abbr);
							VPERIOD_START_DT.add(temp_st_dt);
							VPERIOD_END_DT.add(temp_end_dt);
							VBILLING_FREQ_FLAG.add(billing_cycle);
							VBILLING_FREQ_NM.add(billing_freq_nm);
							VPRODUCTION_MONTH.add(month+"/"+year);
							VINVOICE_DUE_DT.add(utilBean.DueDateCalculation(conn,temp_end_dt, due_days,consider_due_dt_in,exclude_sat,comp_cd,holiday_state));
							
							VAGMT_BASE.add(agmt_base);
							VCASH_FLOW.add("Commodity");
							
							VACCRUAL_QTY.add(nf.format(qtyMMBTU));
							VACCRUAL_AMT.add(nf.format(accrual_amt));
							
							VSALES_PRICE_CD.add(price_unit);
							VSALES_PRICE_NM.add(price_unit_nm);
							VINVOICE_RAISED_IN.add(invoice_raise_in);
							
							if(price_unit.equals("2"))
							{
								VEXCHNG_RATE.add(nf2.format(exchng_rate));
								VEXCHNG_RATE_CD.add(exchng_rate_cd);
								VEXCHNG_RATE_DT.add(exchng_rate_dt);
							}
							else
							{
								VEXCHNG_RATE.add("");
								VEXCHNG_RATE_CD.add("");
								VEXCHNG_RATE_DT.add("");
							}
							VGROSS_AMT.add(nf.format(gross_amt));
							
						}
					}
				}
				rset.close();
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getSalesLTCORAAccrualList()
	{
		String function_nm="getSalesLTCORAAccrualList()";
		try
		{
			financial_year=utilDate.getFinancialYear(period_end_dt);
			
			if(billing_cycle.equals("8"))
			{
				String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
				String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
				
				queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),TO_CHAR(TO_DATE(B.ACTUAL_RECPT_DT,'DD/MM/YY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0),'DD/MM/YYYY'),"
						+ "A.CONT_NAME,B.CARGO_REF,A.CONTRACT_TYPE,B.CARGO_NO,D.PLANT_SEQ_NO,E.PLANT_SEQ_NO,A.FCC_FLAG,"
						+ "C.DUE_DATE,C.DUE_DT_IN,C.EXCLUDE_SAT,B.CSOC_QTY,A.LTCORA_TARIFF,A.LTCORA_TARIFF_UNIT,C.INVOICE_CUR_CD,C.EXCHNG_RATE_CD,C.EXCHNG_RATE_CAL,C.EXCHG_VAL,C.HOLIDAY_STATE,"
						+ "A.AGMT_BASE,C.BILLING_DAYS "
						+ ""
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B,"
							+ "FMS_LTCORA_CONT_BILLING_DTL C,"
							+ "FMS_LTCORA_CONT_BU D,"
							+ "FMS_LTCORA_CONT_PLANT E "
						+ "WHERE A.COMPANY_CD=? AND A.BUY_SALE=? AND B.CARGO_STATUS=? AND A.AGMT_TYPE=? "
						+ "AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0) >=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0) >=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ ""
						+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV "
						+ "AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE AND A.BUY_SALE=D.BUY_SALE AND A.AGMT_TYPE=D.AGMT_TYPE "
						+ ""
						+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO AND A.CONT_REV=E.CONT_REV "
						+ "AND A.AGMT_NO=E.AGMT_NO AND A.AGMT_REV=E.AGMT_REV AND A.CONTRACT_TYPE=E.CONTRACT_TYPE AND A.BUY_SALE=E.BUY_SALE AND A.AGMT_TYPE=E.AGMT_TYPE "
						+ ""
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE "
						+ ""
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "//AND A.CONT_REV=C.CONT_REV "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.BUY_SALE=C.BUY_SALE AND A.AGMT_TYPE=C.AGMT_TYPE "
						+ "AND E.PLANT_SEQ_NO=C.PLANT_SEQ_NO AND C.BILLING_FREQ=? ";
				//HP20250402 REMOVING CONT_REV CHECKING FOR BILLING DETAIL FROM THE QUERY CONDITION AS CONFIRMED BY JD MAM
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, "C");
				stmt.setString(3, "Y");
				stmt.setString(4, "A");
				stmt.setString(5, temp_period_end_dt);
				stmt.setString(6, temp_period_start_dt);
				stmt.setString(7, report_end_dt);
				stmt.setString(8, report_start_dt);
				stmt.setString(9, billing_freq);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String countpty_abbr=""+utilBean.getCounterpartyABBR(conn,countpty_cd);
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					String start_dt = rset.getString(7)==null?"":rset.getString(7);
					String end_dt = rset.getString(8)==null?"":rset.getString(8);
					String contRef=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					String cargo_no=rset.getString(12)==null?"0":rset.getString(12);
					
					String bu_plant_seq = rset.getString(13)==null?"":rset.getString(13);
					String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
					
					String plant_seq=rset.getString(14)==null?"":rset.getString(14);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "C");
					
					String fcc=rset.getString(15)==null?"":rset.getString(15);
					
					String due_days=rset.getString(16)==null?"":rset.getString(16);
					String consider_due_dt_in=rset.getString(17)==null?"":rset.getString(17);
					String exclude_sat=rset.getString(18)==null?"":rset.getString(18);
					
					String dcq=rset.getString(19)==null?"":rset.getString(19);
					//String price=rset.getString(20)==null?"":rset.getString(20);
					String price_unit=rset.getString(21)==null?"":rset.getString(21);
					String price_unit_nm=utilBean.getRateUnitNm(conn,price_unit);
					String price = utilBean.RateNumberFormat(rset.getDouble(20), price_unit);
					
					String invoice_raise_in=rset.getString(22)==null?"":rset.getString(22);
					
					String exchng_rate_cd = rset.getString(23)==null?"":rset.getString(23);
					String exchng_rate_cal = rset.getString(24)==null?"":rset.getString(24);
					String fixed_exchng_val=nf2.format(rset.getDouble(25));
					String holiday_state = rset.getString(26)==null?"":rset.getString(26);
					String agmt_base = rset.getString(27)==null?"":rset.getString(27);
					String billing_days=rset.getString(28)==null?"1":rset.getString(28);
					
					queryString1="SELECT LTCORA_TARIFF,LTCORA_TARIFF_UNIT,SUG "
							+ "FROM FMS_LTCORA_CONT_CARGO_MOD "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=? AND CONT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, own_cd);
					stmt1.setString(2, countpty_cd);
					stmt1.setString(3, "C");
					stmt1.setString(4, agmtno);
					stmt1.setString(5, agmtrev);
					stmt1.setString(6, "A");
					stmt1.setString(7, contno);
					stmt1.setString(8, cont_type);
					stmt1.setString(9, cargo_no);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						price_unit = rset1.getString(2)==null?"2":rset1.getString(2);
						price = utilBean.RateNumberFormat(rset1.getDouble(1), price_unit);
						price_unit_nm=""+utilBean.getRateUnitNm(conn,price_unit);
						
						//sug_percentage = rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
					}
					rset1.close();
					stmt1.close();
					
					//String deal_no=cont_type+""+contno;
					String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, cargo_no);
					
					String state_code=utilBean.getState_TIN(conn, own_cd, own_cd, "B", bu_plant_seq);
					
					System.out.println("\n"+countpty_abbr+" -- "+deal_no+" :: "+start_dt+" - "+end_dt+" :: Bill Eff "+start_dt+" :: "+temp_period_start_dt+" - "+temp_period_end_dt);
					
					String periodStartDate="";
					String periodEndDate="";
					
					int isGreter=utilDate.getDays(start_dt, temp_period_start_dt);
					if(isGreter>1)
					{
						periodStartDate=start_dt;
					}
					else
					{
						periodStartDate=temp_period_start_dt;
					}
					int isLower=utilDate.getDays(end_dt, temp_period_end_dt);
					if(isLower < 1)
					{
						periodEndDate=end_dt;
					}
					else
					{
						periodEndDate=temp_period_end_dt;
					}
					System.out.println("PeriodDate="+periodStartDate+"=="+periodEndDate);
					String temp_st_dt=periodStartDate;
					String temp_end_dt=periodEndDate;
					System.out.println("Billing Days : "+billing_days);
					
					String temp_dt = utilDate.getDate(temp_st_dt,"-1");
					System.out.println("temp_dt :: "+temp_dt);
					int tot_row=1;
					for(int j=0;j<tot_row;j++)
					{
						temp_st_dt=utilDate.getDate(temp_dt,"1");
						temp_dt=utilDate.getDate(temp_dt,billing_days);
						
						int checkMthEnd=utilDate.getDays(periodEndDate,temp_st_dt);
						
						if(Integer.parseInt(billing_days) <= checkMthEnd)
						{
							temp_end_dt = temp_dt;
						}
						else
						{
							temp_end_dt = periodEndDate;
						}
						
						/*NOT REQUIRED AT THIS POINT OF TIME
						String innerBillingEffDt=billing_eff_dt; //defaualt added
						String innerBillingDays="";
						String innerBillingFreq="";
						queryString1="SELECT DISTINCT TO_CHAR(EFF_DT,'DD/MM/YYYY'),BILLING_DAYS,BILLING_FREQ "
								+ "FROM FMS_GTA_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND CONT_NO=? "
								+ "AND CONTRACT_TYPE=? "
								+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_GTA_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
								+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY'))";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, own_cd);
						stmt1.setString(2, countpty_cd);
						stmt1.setString(3, agmtno);
						stmt1.setString(4, contno);
						stmt1.setString(5, cont_type);
						stmt1.setString(6, temp_end_dt);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							innerBillingEffDt=rset1.getString(1)==null?"":rset1.getString(1);
							innerBillingDays=rset1.getString(2)==null?"1":rset1.getString(2);
							innerBillingFreq=rset1.getString(3)==null?"":rset1.getString(3);
						}
						rset1.close();
						stmt1.close();
						
						if(!billing_eff_dt.equals(innerBillingEffDt))
						{
							System.out.println("New Eff Date : "+innerBillingEffDt);
							break;
						}
						*/
						
						int rem_checkMthEnd=utilDate.getDays(periodEndDate,utilDate.getDate(temp_end_dt,"1"));
						System.out.println(j+" - "+checkMthEnd+" : "+temp_st_dt+" :: "+temp_end_dt+" Rem Day : "+rem_checkMthEnd);
						tot_row+=1;
					
						int isInvExist=isInvoiceExist(own_cd,countpty_cd, contno, agmtno,cont_type, plant_seq, 
								bu_plant_seq, billing_cycle, temp_st_dt, temp_end_dt, state_code,cargo_no,financial_year);
						if(isInvExist > 0 && !fcc.equals("Y"))
						{
							fcc="Y";
						}
						
						if(isInvExist==0)
						{
							double qtyMMBTU=0;
							
							queryString4="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
									+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
									+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
									+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
							stmt4 = conn.prepareStatement(queryString4);
							stmt4.setString(1, temp_st_dt);
							stmt4.setString(2, temp_end_dt);
							rset4=stmt4.executeQuery();
							while(rset4.next())
							{
								String date=rset4.getString(1)==null?"":rset4.getString(1);
								
								queryString5="SELECT COALESCE(ALLOCATION, SELLER_NOM, BUYER_NOM, DCQ, 0) "
										+ "FROM "
										+ "(SELECT (SELECT SUM(QTY_MMBTU) "
							  				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
							  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
											+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
											+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
											+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
											+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
											+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
											+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
											+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
											+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) ALLOCATION, "
										+ "(SELECT SUM(QTY_MMBTU) "
						  					+ "FROM FMS_DAILY_SELLER_NOM A "
						  					+ "WHERE CONT_NO=? AND AGMT_NO=? "
						  					+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						  					+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
						  					+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
						  					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_SELLER_NOM B "
						  					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						  					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						  					+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
						  					+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						  					+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) SELLER_NOM, "
										+ "(SELECT SUM(QTY_MMBTU) "
						  					+ "FROM FMS_DAILY_BUYER_NOM A "
						  					+ "WHERE CONT_NO=? AND AGMT_NO=? "
						  					+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						  					+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
						  					+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
						  					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_BUYER_NOM B "
						  					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						  					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						  					+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
						  					+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						  					+ "AND B.GAS_DT=A.GAS_DT AND A.CARGO_NO=B.CARGO_NO)) BUYER_NOM, "
						  				+ "(NVL((SELECT CSOC "
						  					+ "FROM FMS_LTCORA_CONT_CARGO_CSOC "
						  					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND AGMT_NO=? AND CONT_NO=? "
											+ "AND CONTRACT_TYPE=? AND BUY_SALE=? "
											+ "AND CARGO_NO=? AND AGMT_TYPE=? "
						  					+ "AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT>=TO_DATE(?,'DD/MM/YYYY')),?)) DCQ FROM DUAL) ";
								stmt5 = conn.prepareStatement(queryString5);
								int stmt_count=0;
								stmt5.setString(++stmt_count, contno);
								stmt5.setString(++stmt_count, agmtno);
								stmt5.setString(++stmt_count, comp_cd);
								stmt5.setString(++stmt_count, countpty_cd);
								stmt5.setString(++stmt_count, plant_seq);
								stmt5.setString(++stmt_count, cont_type);
								stmt5.setString(++stmt_count, bu_plant_seq);
								stmt5.setString(++stmt_count, date);
								stmt5.setString(++stmt_count, cargo_no);
								stmt5.setString(++stmt_count, contno);
								stmt5.setString(++stmt_count, agmtno);
								stmt5.setString(++stmt_count, comp_cd);
								stmt5.setString(++stmt_count, countpty_cd);
								stmt5.setString(++stmt_count, plant_seq);
								stmt5.setString(++stmt_count, cont_type);
								stmt5.setString(++stmt_count, bu_plant_seq);
								stmt5.setString(++stmt_count, date);
								stmt5.setString(++stmt_count, cargo_no);
								stmt5.setString(++stmt_count, contno);
								stmt5.setString(++stmt_count, agmtno);
								stmt5.setString(++stmt_count, comp_cd);
								stmt5.setString(++stmt_count, countpty_cd);
								stmt5.setString(++stmt_count, plant_seq);
								stmt5.setString(++stmt_count, cont_type);
								stmt5.setString(++stmt_count, bu_plant_seq);
								stmt5.setString(++stmt_count, date);
								stmt5.setString(++stmt_count, cargo_no);
								stmt5.setString(++stmt_count, comp_cd);
								stmt5.setString(++stmt_count, countpty_cd);
								stmt5.setString(++stmt_count, agmtno);
								stmt5.setString(++stmt_count, contno);
								stmt5.setString(++stmt_count, cont_type);
								stmt5.setString(++stmt_count, "C");
								stmt5.setString(++stmt_count, cargo_no);
								stmt5.setString(++stmt_count, "A");
								stmt5.setString(++stmt_count, date);
								stmt5.setString(++stmt_count, date);
								stmt5.setString(++stmt_count, dcq);
								rset5=stmt5.executeQuery();
								while(rset5.next())
								{
									qtyMMBTU+=rset5.getDouble(1);
								}
								rset5.close();
								stmt5.close();
							}
							rset4.close();
							stmt4.close();
							
							double exchng_rate=0;
							String exchng_rate_dt="";
							if(exchng_rate_cd.equals("0")) //FOR FIXED EXCHANGE RATE
							{
								exchng_rate=Double.parseDouble(fixed_exchng_val);
							}
							else
							{
								queryString4="SELECT NVL(EXCHG_VAL,0),TO_CHAR(EFF_DT,'DD/MM/YYYY') "
										+ "FROM FMS_EXCHG_RATE_ENTRY A "
										+ "WHERE EXCHG_RATE_CD=? "
										+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY') "
										+ "ORDER BY EFF_DT DESC";
								stmt6 = conn.prepareStatement(queryString4);
								stmt6.setString(1, exchng_rate_cd);
								stmt6.setString(2, temp_end_dt);
								rset6=stmt6.executeQuery();
								if(rset6.next())
								{
									exchng_rate=rset6.getDouble(1);
									exchng_rate_dt=rset6.getString(2)==null?"":rset6.getString(2);
								}
								rset6.close();
								stmt6.close();
							}
							
							double accrual_amt=0;
							if(!price.equals(""))
							{
								accrual_amt=qtyMMBTU * Double.parseDouble(price);
							}
							
							double gross_amt=0;
							if(price_unit.equals("2"))
							{
								gross_amt=accrual_amt * exchng_rate;
							}
							else
							{
								gross_amt=accrual_amt;
							}
							
							if(qtyMMBTU>0 && accrual_amt > 0)
							{
								VBU_STATE_TIN.add(state_code);							
								VFINANCIAL_YEAR.add(financial_year);
								VCOUNTERPTY_CD.add(countpty_cd);
								VAGMT_NO.add(agmtno);
								VAGMT_REV_NO.add(agmtrev);
								VCONT_NO.add(contno);
								VCONT_REV_NO.add(contrev);
								VCARGO_NO.add(cargo_no);
								VCONTRACT_TYPE.add(cont_type);
								VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
								VCOUNTERPTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
								VSTART_DT.add(rset.getString(7)==null?"":rset.getString(7));
								VEND_DT.add(rset.getString(8)==null?"":rset.getString(8));
								VDIS_CONT_MAPPING.add(deal_no);
								VCONT_REF_NO.add(contRef);
								VPLANT_SEQ.add(plant_seq);
								VPLANT_ABBR.add(plant_abbr);
								VBU_PLANT_SEQ.add(bu_plant_seq);
								VBU_PLANT_ABBR.add(bu_plant_abbr);
								VPERIOD_START_DT.add(temp_st_dt);
								VPERIOD_END_DT.add(temp_end_dt);
								VBILLING_FREQ_FLAG.add(billing_cycle);
								VBILLING_FREQ_NM.add(billing_freq_nm);
								VPRODUCTION_MONTH.add(month+"/"+year);
								VINVOICE_DUE_DT.add(utilBean.DueDateCalculation(conn,temp_end_dt, due_days,consider_due_dt_in,exclude_sat,comp_cd,holiday_state));
								
								VAGMT_BASE.add(agmt_base);
								VCASH_FLOW.add("Re-Gas Capacity");
									
								VACCRUAL_QTY.add(nf.format(qtyMMBTU));
								VACCRUAL_AMT.add(nf.format(accrual_amt));
								
								VSALES_PRICE_CD.add(price_unit);
								VSALES_PRICE_NM.add(price_unit_nm);
								VINVOICE_RAISED_IN.add(invoice_raise_in);
								
								if(price_unit.equals("2"))
								{
									VEXCHNG_RATE.add(nf2.format(exchng_rate));
									VEXCHNG_RATE_CD.add(exchng_rate_cd);
									VEXCHNG_RATE_DT.add(exchng_rate_dt);
								}
								else
								{
									VEXCHNG_RATE.add("");
									VEXCHNG_RATE_CD.add("");
									VEXCHNG_RATE_DT.add("");
								}
								VGROSS_AMT.add(nf.format(gross_amt));
							}
						}
						
						if(rem_checkMthEnd == 0)
						{
							break;
						}
					}
				}
			}
			else
			{
				queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),TO_CHAR(TO_DATE(B.ACTUAL_RECPT_DT,'DD/MM/YY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0),'DD/MM/YYYY'),"
						+ "A.CONT_NAME,B.CARGO_REF,A.CONTRACT_TYPE,B.CARGO_NO,D.PLANT_SEQ_NO,E.PLANT_SEQ_NO,A.FCC_FLAG,"
						+ "C.DUE_DATE,C.DUE_DT_IN,C.EXCLUDE_SAT,B.CSOC_QTY,A.LTCORA_TARIFF,A.LTCORA_TARIFF_UNIT,C.INVOICE_CUR_CD,C.EXCHNG_RATE_CD,C.EXCHNG_RATE_CAL,C.EXCHG_VAL,C.HOLIDAY_STATE,"
						+ "A.AGMT_BASE "
						+ ""
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B,"
							+ "FMS_LTCORA_CONT_BILLING_DTL C,"
							+ "FMS_LTCORA_CONT_BU D,"
							+ "FMS_LTCORA_CONT_PLANT E "
						+ "WHERE A.COMPANY_CD=? AND A.BUY_SALE=? AND B.CARGO_STATUS=? AND A.AGMT_TYPE=? "
						+ "AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0) >=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0) >=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ ""
						+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV "
						+ "AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE AND A.BUY_SALE=D.BUY_SALE AND A.AGMT_TYPE=D.AGMT_TYPE "
						+ ""
						+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO AND A.CONT_REV=E.CONT_REV "
						+ "AND A.AGMT_NO=E.AGMT_NO AND A.AGMT_REV=E.AGMT_REV AND A.CONTRACT_TYPE=E.CONTRACT_TYPE AND A.BUY_SALE=E.BUY_SALE AND A.AGMT_TYPE=E.AGMT_TYPE "
						+ ""
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE "
						+ ""
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "//AND A.CONT_REV=C.CONT_REV "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.BUY_SALE=C.BUY_SALE AND A.AGMT_TYPE=C.AGMT_TYPE "
						+ "AND E.PLANT_SEQ_NO=C.PLANT_SEQ_NO AND C.BILLING_FREQ=? ";
				//HP20250402 REMOVING CONT_REV CHECKING FOR BILLING DETAIL FROM THE QUERY CONDITION AS CONFIRMED BY JD MAM
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, "C");
				stmt.setString(3, "Y");
				stmt.setString(4, "A");
				stmt.setString(5, period_end_dt);
				stmt.setString(6, period_start_dt);
				stmt.setString(7, report_end_dt);
				stmt.setString(8, report_start_dt);
				stmt.setString(9, billing_freq);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					String start_dt = rset.getString(7)==null?"":rset.getString(7);
					String end_dt = rset.getString(8)==null?"":rset.getString(8);
					String contRef=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					String cargo_no=rset.getString(12)==null?"0":rset.getString(12);
					
					String bu_plant_seq = rset.getString(13)==null?"":rset.getString(13);
					String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
					
					String plant_seq=rset.getString(14)==null?"":rset.getString(14);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "C");
					
					String fcc=rset.getString(15)==null?"":rset.getString(15);
					
					String due_days=rset.getString(16)==null?"":rset.getString(16);
					String consider_due_dt_in=rset.getString(17)==null?"":rset.getString(17);
					String exclude_sat=rset.getString(18)==null?"":rset.getString(18);
					
					String dcq=rset.getString(19)==null?"":rset.getString(19);
					//String price=rset.getString(20)==null?"":rset.getString(20);
					String price_unit=rset.getString(21)==null?"":rset.getString(21);
					String price_unit_nm=utilBean.getRateUnitNm(conn,price_unit);
					String price = utilBean.RateNumberFormat(rset.getDouble(20), price_unit);
					
					String invoice_raise_in=rset.getString(22)==null?"":rset.getString(22);
					
					String exchng_rate_cd = rset.getString(23)==null?"":rset.getString(23);
					String exchng_rate_cal = rset.getString(24)==null?"":rset.getString(24);
					String fixed_exchng_val=nf2.format(rset.getDouble(25));
					String holiday_state = rset.getString(26)==null?"":rset.getString(26);
					String agmt_base = rset.getString(27)==null?"":rset.getString(27);
					
					queryString1="SELECT LTCORA_TARIFF,LTCORA_TARIFF_UNIT,SUG "
							+ "FROM FMS_LTCORA_CONT_CARGO_MOD "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=? AND CONT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, own_cd);
					stmt1.setString(2, countpty_cd);
					stmt1.setString(3, "C");
					stmt1.setString(4, agmtno);
					stmt1.setString(5, agmtrev);
					stmt1.setString(6, "A");
					stmt1.setString(7, contno);
					stmt1.setString(8, cont_type);
					stmt1.setString(9, cargo_no);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						price_unit = rset1.getString(2)==null?"2":rset1.getString(2);
						price = utilBean.RateNumberFormat(rset1.getDouble(1), price_unit);
						price_unit_nm=""+utilBean.getRateUnitNm(conn,price_unit);
						
						//sug_percentage = rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
					}
					rset1.close();
					stmt1.close();
					
					//String deal_no=cont_type+""+contno;
					String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, cargo_no);
					
					//int isGreter=utilDate.getDays(billing_eff_dt, period_start_dt);
					int isGreter=utilDate.getDays(start_dt, period_start_dt);
					
					String temp_st_dt="";
					String temp_end_dt="";
					String diff_color="";
					if(isGreter>1)
					{
						//temp_st_dt=billing_eff_dt;
						temp_st_dt=start_dt;
						temp_end_dt=period_end_dt;
						diff_color="blue";
					}
					else
					{
						temp_st_dt=period_start_dt;
						temp_end_dt=period_end_dt;
						diff_color="";
					}
					
					if(utilDate.getDays(end_dt, temp_end_dt)<=0)
					{
						temp_end_dt=end_dt;
						diff_color="blue";
					}
					
					String state_code=utilBean.getState_TIN(conn, own_cd, own_cd, "B", bu_plant_seq);
					
					int isInvExist=isInvoiceExist(own_cd,countpty_cd, contno, agmtno,cont_type, plant_seq, 
							bu_plant_seq, billing_cycle, temp_st_dt, temp_end_dt, state_code,cargo_no,financial_year);
					if(isInvExist > 0 && !fcc.equals("Y"))
					{
						fcc="Y";
					}
					
					if(isInvExist==0)
					{
						double qtyMMBTU=0;
						
						queryString4="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
								+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
								+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
								+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, temp_st_dt);
						stmt4.setString(2, temp_end_dt);
						rset4=stmt4.executeQuery();
						while(rset4.next())
						{
							String date=rset4.getString(1)==null?"":rset4.getString(1);
							
							queryString5="SELECT COALESCE(ALLOCATION, SELLER_NOM, BUYER_NOM, DCQ, 0) "
									+ "FROM "
									+ "(SELECT (SELECT SUM(QTY_MMBTU) "
						  				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
						  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
										+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
										+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
										+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
										+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
										+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
										+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
										+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
										+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) ALLOCATION, "
									+ "(SELECT SUM(QTY_MMBTU) "
					  					+ "FROM FMS_DAILY_SELLER_NOM A "
					  					+ "WHERE CONT_NO=? AND AGMT_NO=? "
					  					+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
					  					+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
					  					+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
					  					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_SELLER_NOM B "
					  					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					  					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					  					+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
					  					+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
					  					+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)) SELLER_NOM, "
									+ "(SELECT SUM(QTY_MMBTU) "
					  					+ "FROM FMS_DAILY_BUYER_NOM A "
					  					+ "WHERE CONT_NO=? AND AGMT_NO=? "
					  					+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
					  					+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
					  					+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
					  					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_BUYER_NOM B "
					  					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					  					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					  					+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
					  					+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
					  					+ "AND B.GAS_DT=A.GAS_DT AND A.CARGO_NO=B.CARGO_NO)) BUYER_NOM, "
					  				+ "(NVL((SELECT CSOC "
					  					+ "FROM FMS_LTCORA_CONT_CARGO_CSOC "
					  					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND AGMT_NO=? AND CONT_NO=? "
										+ "AND CONTRACT_TYPE=? AND BUY_SALE=? "
										+ "AND CARGO_NO=? AND AGMT_TYPE=? "
					  					+ "AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT>=TO_DATE(?,'DD/MM/YYYY')),?)) DCQ FROM DUAL) ";
							stmt5 = conn.prepareStatement(queryString5);
							int stmt_count=0;
							stmt5.setString(++stmt_count, contno);
							stmt5.setString(++stmt_count, agmtno);
							stmt5.setString(++stmt_count, comp_cd);
							stmt5.setString(++stmt_count, countpty_cd);
							stmt5.setString(++stmt_count, plant_seq);
							stmt5.setString(++stmt_count, cont_type);
							stmt5.setString(++stmt_count, bu_plant_seq);
							stmt5.setString(++stmt_count, date);
							stmt5.setString(++stmt_count, cargo_no);
							stmt5.setString(++stmt_count, contno);
							stmt5.setString(++stmt_count, agmtno);
							stmt5.setString(++stmt_count, comp_cd);
							stmt5.setString(++stmt_count, countpty_cd);
							stmt5.setString(++stmt_count, plant_seq);
							stmt5.setString(++stmt_count, cont_type);
							stmt5.setString(++stmt_count, bu_plant_seq);
							stmt5.setString(++stmt_count, date);
							stmt5.setString(++stmt_count, cargo_no);
							stmt5.setString(++stmt_count, contno);
							stmt5.setString(++stmt_count, agmtno);
							stmt5.setString(++stmt_count, comp_cd);
							stmt5.setString(++stmt_count, countpty_cd);
							stmt5.setString(++stmt_count, plant_seq);
							stmt5.setString(++stmt_count, cont_type);
							stmt5.setString(++stmt_count, bu_plant_seq);
							stmt5.setString(++stmt_count, date);
							stmt5.setString(++stmt_count, cargo_no);
							stmt5.setString(++stmt_count, comp_cd);
							stmt5.setString(++stmt_count, countpty_cd);
							stmt5.setString(++stmt_count, agmtno);
							stmt5.setString(++stmt_count, contno);
							stmt5.setString(++stmt_count, cont_type);
							stmt5.setString(++stmt_count, "C");
							stmt5.setString(++stmt_count, cargo_no);
							stmt5.setString(++stmt_count, "A");
							stmt5.setString(++stmt_count, date);
							stmt5.setString(++stmt_count, date);
							stmt5.setString(++stmt_count, dcq);
							rset5=stmt5.executeQuery();
							while(rset5.next())
							{
								qtyMMBTU+=rset5.getDouble(1);
							}
							rset5.close();
							stmt5.close();
						}
						rset4.close();
						stmt4.close();
						
						double exchng_rate=0;
						String exchng_rate_dt="";
						if(exchng_rate_cd.equals("0")) //FOR FIXED EXCHANGE RATE
						{
							exchng_rate=Double.parseDouble(fixed_exchng_val);
						}
						else
						{
							queryString4="SELECT NVL(EXCHG_VAL,0),TO_CHAR(EFF_DT,'DD/MM/YYYY') "
									+ "FROM FMS_EXCHG_RATE_ENTRY A "
									+ "WHERE EXCHG_RATE_CD=? "
									+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY') "
									+ "ORDER BY EFF_DT DESC";
							stmt6 = conn.prepareStatement(queryString4);
							stmt6.setString(1, exchng_rate_cd);
							stmt6.setString(2, temp_end_dt);
							rset6=stmt6.executeQuery();
							if(rset6.next())
							{
								exchng_rate=rset6.getDouble(1);
								exchng_rate_dt=rset6.getString(2)==null?"":rset6.getString(2);
							}
							rset6.close();
							stmt6.close();
						}
						
						double accrual_amt=0;
						if(!price.equals(""))
						{
							accrual_amt=qtyMMBTU * Double.parseDouble(price);
						}
						
						double gross_amt=0;
						if(price_unit.equals("2"))
						{
							gross_amt=accrual_amt * exchng_rate;
						}
						else
						{
							gross_amt=accrual_amt;
						}
						if(qtyMMBTU>0 && accrual_amt > 0)
						{
							VBU_STATE_TIN.add(state_code);							
							VFINANCIAL_YEAR.add(financial_year);
							VCOUNTERPTY_CD.add(countpty_cd);
							VAGMT_NO.add(agmtno);
							VAGMT_REV_NO.add(agmtrev);
							VCONT_NO.add(contno);
							VCONT_REV_NO.add(contrev);
							VCARGO_NO.add(cargo_no);
							VCONTRACT_TYPE.add(cont_type);
							VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
							VCOUNTERPTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
							VSTART_DT.add(rset.getString(7)==null?"":rset.getString(7));
							VEND_DT.add(rset.getString(8)==null?"":rset.getString(8));
							VDIS_CONT_MAPPING.add(deal_no);
							VCONT_REF_NO.add(contRef);
							VPLANT_SEQ.add(plant_seq);
							VPLANT_ABBR.add(plant_abbr);
							VBU_PLANT_SEQ.add(bu_plant_seq);
							VBU_PLANT_ABBR.add(bu_plant_abbr);
							VPERIOD_START_DT.add(temp_st_dt);
							VPERIOD_END_DT.add(temp_end_dt);
							VBILLING_FREQ_FLAG.add(billing_cycle);
							VBILLING_FREQ_NM.add(billing_freq_nm);
							VPRODUCTION_MONTH.add(month+"/"+year);
							VINVOICE_DUE_DT.add(utilBean.DueDateCalculation(conn,temp_end_dt, due_days,consider_due_dt_in,exclude_sat,comp_cd,holiday_state));
							
							VAGMT_BASE.add(agmt_base);
							VCASH_FLOW.add("Re-Gas Capacity");
								
							VACCRUAL_QTY.add(nf.format(qtyMMBTU));
							VACCRUAL_AMT.add(nf.format(accrual_amt));
							
							VSALES_PRICE_CD.add(price_unit);
							VSALES_PRICE_NM.add(price_unit_nm);
							VINVOICE_RAISED_IN.add(invoice_raise_in);
							
							if(price_unit.equals("2"))
							{
								VEXCHNG_RATE.add(nf2.format(exchng_rate));
								VEXCHNG_RATE_CD.add(exchng_rate_cd);
								VEXCHNG_RATE_DT.add(exchng_rate_dt);
							}
							else
							{
								VEXCHNG_RATE.add("");
								VEXCHNG_RATE_CD.add("");
								VEXCHNG_RATE_DT.add("");
							}
							VGROSS_AMT.add(nf.format(gross_amt));
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
	
	public int isInvoiceExist(String own_cd,String countpty_cd, String contno, String agmtno,String cont_type, String plant_seq, 
			String bu_plant_seq, String billing_cycle, String period_start_dt, String period_end_dt,String state_code,String cargo_no,String financial_year)
	{
		String function_nm="isInvoiceExist()";
		int isInvExist=0;
		try
		{
			queryString4="SELECT COUNT(*) "
					+ "FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND AGMT_NO=? AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_UNIT=? "
					+ "AND FREQ=? AND BU_STATE_TIN=? AND CARGO_NO=? AND FINANCIAL_YEAR=? AND PDF_INV_DTL IS NOT NULL ";
			if(!billing_cycle.equals("11"))
			{
				queryString4+="AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') ";
			} 
			queryString4+= "AND PDF_INV_DTL IS NOT NULL"; //AND FINANCIAL_YEAR=? 
			int cnt=0;
			stmt4=conn.prepareStatement(queryString4);
			stmt4.setString(++cnt, own_cd);
			stmt4.setString(++cnt, countpty_cd);
			stmt4.setString(++cnt, contno);
			stmt4.setString(++cnt, agmtno);
			stmt4.setString(++cnt, plant_seq);
			stmt4.setString(++cnt, cont_type);
			stmt4.setString(++cnt, bu_plant_seq);
			stmt4.setString(++cnt, billing_cycle);
			stmt4.setString(++cnt, state_code);
			stmt4.setString(++cnt, cargo_no);
			stmt4.setString(++cnt, financial_year);
			if(!billing_cycle.equals("11"))
			{
				stmt4.setString(++cnt, period_start_dt);
				stmt4.setString(++cnt, period_end_dt);
			}
			rset4=stmt4.executeQuery();
			if(rset4.next())
			{
				isInvExist=rset4.getInt(1);
			}
			rset4.close();
			stmt4.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return isInvExist;
	}
	
	public void PurchaseFreezAccrualData()
	{
		String function_nm="PurchaseFreezAccrualData()";
		try
		{
			int count=0;
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_TRADER_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, report_dt);
			rset = stmt.executeQuery();
			if(rset.next())
			{
				count=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			if(count>0)
			{
				queryString="DELETE FROM FMS_TRADER_ACCRUAL_DTL "
						+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, report_dt);
				stmt.executeUpdate();
				
				stmt.close();
			}
			for(int i=0;i<VCOUNTERPTY_CD.size();i++)
			{		
				String prod_month="01/"+VPRODUCTION_MONTH.elementAt(i);
				
				queryString="INSERT INTO FMS_TRADER_ACCRUAL_DTL(COMPANY_CD,REPORT_DT,COUNTERPARTY_CD,FINANCIAL_YEAR,"
						+ "AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
						+ "CONTRACT_TYPE,BU_UNIT,PLANT_SEQ,"
						+ "INVOICE_DUE_DT,PROD_MONTH,FREQ,"
						+ "PERIOD_START_DT,PERIOD_END_DT,"
						+ "ACCRUAL_QTY,ACCRUAL_AMT,RATE_IN,INVOICE_RAISED_IN,"
						+ "GROSS_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,"
						+ "CONT_REF_NO,CASH_FLOW,CONT_START_DT,CONT_END_DT,"
						+ "SPLIT_FLAG,SPLIT_VALUE,ENT_BY,ENT_DT,CARGO_NO,BOE_NO,INV_FLAG) "
						+ "VALUES(?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
						+ "?,?,?,?,"
						+ "?,?,?,"
						+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,"
						+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
						+ "?,?,?,?,"
						+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
						+ "?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
						+ "?,?,?,SYSDATE,?,?,?)";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, report_dt);
				stmt.setString(3, ""+VCOUNTERPTY_CD.elementAt(i));
				stmt.setString(4, ""+VFINANCIAL_YEAR.elementAt(i));
				stmt.setString(5, ""+VAGMT_NO.elementAt(i));
				stmt.setString(6, ""+VAGMT_REV_NO.elementAt(i));
				stmt.setString(7, ""+VCONT_NO.elementAt(i));
				stmt.setString(8, ""+VCONT_REV_NO.elementAt(i));
				stmt.setString(9, ""+VCONTRACT_TYPE.elementAt(i));
				stmt.setString(10, ""+VBU_PLANT_SEQ.elementAt(i));
				stmt.setString(11, ""+VPLANT_SEQ.elementAt(i));
				stmt.setString(12, ""+VINVOICE_DUE_DT.elementAt(i));
				stmt.setString(13, ""+prod_month);
				stmt.setString(14, ""+VBILLING_FREQ_FLAG.elementAt(i));
				stmt.setString(15, ""+VPERIOD_START_DT.elementAt(i));
				stmt.setString(16, ""+VPERIOD_END_DT.elementAt(i));
				stmt.setString(17, ""+VACCRUAL_QTY.elementAt(i));
				stmt.setString(18, ""+VACCRUAL_AMT.elementAt(i));
				stmt.setString(19, ""+VSALES_PRICE_CD.elementAt(i));
				stmt.setString(20, ""+VINVOICE_RAISED_IN.elementAt(i));
				stmt.setString(21, ""+VGROSS_AMT.elementAt(i));
				stmt.setString(22, ""+VEXCHNG_RATE_CD.elementAt(i));
				stmt.setString(23, ""+VEXCHNG_RATE_DT.elementAt(i));
				stmt.setString(24, ""+VEXCHNG_RATE.elementAt(i));
				stmt.setString(25, ""+VCONT_REF_NO.elementAt(i));
				stmt.setString(26, ""+VCASH_FLOW.elementAt(i));
				stmt.setString(27, ""+VSTART_DT.elementAt(i));
				stmt.setString(28, ""+VEND_DT.elementAt(i));
				stmt.setString(29, ""+VSPLIT_FLAG.elementAt(i));
				stmt.setString(30, ""+VSPLIT_VALUE.elementAt(i));
				stmt.setString(31, ""+emp_cd);
				stmt.setString(32, ""+VCARGO_NO.elementAt(i));
				stmt.setString(33, ""+VBOE_NO.elementAt(i));
				stmt.setString(34, ""+VINV_FLAG.elementAt(i));
				stmt.executeUpdate();
				
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void generatePurchaseAccrualXML()
	{
		String function_nm="generatePurchaseAccrualXML()";
		try
		{
			String sysdate=utilDate.getSysdate();
			String sysdateWithTime=utilDate.getSysdateWithTime24hr();
			String xml_sysdate="";
			String postingMonth="";
			String[] split=sysdate.split("/");
			xml_sysdate=split[2]+""+split[1]+""+split[0];
			postingMonth=split[2]+""+split[1];
			
			String[] splitSys = sysdateWithTime.split(" ");
			String date_timestamp=xml_sysdate+" "+splitSys[1];
			
			String fms_MessageId = "";
			String accountingPeriodMonth="";
			String accountingPeriodYear="";
			
			String documentDate="";
			if(!report_dt.equals(""))
			{
				String[] temp_split=report_dt.split("/");
				accountingPeriodMonth=temp_split[1];
				accountingPeriodYear=temp_split[2];	
				
				documentDate=temp_split[2]+""+temp_split[1]+""+temp_split[0];
			}
			
			String invoice_prefix=utilBean.getInvoicePrefix(conn,comp_cd);
			
			queryString="SELECT DISTINCT COUNTERPARTY_CD,AGMT_NO,CONT_NO,CONTRACT_TYPE,PLANT_SEQ,BU_UNIT,"
					+ "FINANCIAL_YEAR,TO_CHAR(PROD_MONTH,'DD/MM/YYYY'),FREQ,CARGO_NO,BOE_NO,INV_FLAG "
					+ "FROM FMS_TRADER_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? "
					+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY')";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, report_dt);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String counterpty_cd=rset.getString(1)==null?"":rset.getString(1);
				String agmtno=rset.getString(2)==null?"":rset.getString(2);
				String contno=rset.getString(3)==null?"":rset.getString(3);
				String cont_type=rset.getString(4)==null?"":rset.getString(4);
				String plant_seq=rset.getString(5)==null?"":rset.getString(5);
				String buSeq=rset.getString(6)==null?"":rset.getString(6);
				String financialYear=rset.getString(7)==null?"":rset.getString(7);
				String prod_month=rset.getString(8)==null?"":rset.getString(8);
				String bill_freq=rset.getString(9)==null?"":rset.getString(9);
				String cargoNo=rset.getString(10)==null?"":rset.getString(10);
				String boeNo=rset.getString(11)==null?"":rset.getString(11);
				String invFlag=rset.getString(12)==null?"":rset.getString(12);
				
				String buStateNm="";
				String buAbbr="";
				queryString1 = "SELECT PLANT_STATE,PLANT_ABBR "
						+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
						+ "WHERE COUNTERPARTY_CD=? AND ENTITY='B' AND COMPANY_CD=? AND SEQ_NO=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, comp_cd);
				stmt1.setString(3, buSeq);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					buStateNm=rset1.getString(1)==null?"":rset1.getString(1);
					buAbbr=rset1.getString(2)==null?"":rset1.getString(2);
				}
				rset1.close();
				stmt1.close();
				docHeaderText=buStateNm+"/"+buAbbr+" - BU";
				//fms_MessageId=comp_cd+"O"+buSeq+"T"+counterpty_cd+""+cont_type+""+contno+"P"+accountingPeriodMonth+""+accountingPeriodYear;
				// DONT CHABGE fms_MessageId logic - Multiple dependency like file Name, Unique ID, Remittance#, etc
				
				String xml_seq="1";
				String xml_num="";
				queryString1="SELECT NVL(MAX(XML_SEQ),0) "
						+ "FROM FMS_TRADER_ACCRUAL_DTL "
						+ "WHERE COMPANY_CD=? "
						+ "AND TO_DATE(TO_CHAR(REPORT_DT,'MM/YYYY'),'MM/YYYY')=TO_DATE(?,'MM/YYYY') ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, accountingPeriodMonth+"/"+accountingPeriodYear);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					xml_seq=""+(rset1.getInt(1)+1);
				}
				rset1.close();
				stmt1.close();
				xml_num=invoice_prefix+"T"+utilBean.PrePaddingZero(xml_seq, 4)+"/"+accountingPeriodMonth+""+accountingPeriodYear;
				
				fms_MessageId=xml_num.replaceAll("/", "-");
				
				DocumentBuilderFactory docFactory = xmlUtil.dcoumentBuilderFactory();
			    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
			    Document doc = docBuilder.newDocument();
			    
			    //root fmsng
			    Element fmsng = doc.createElement("EmsSAPGeneralLedgerMessage");
			    doc.appendChild(fmsng);
		
			    //root elements
			    Element Header = doc.createElement("Header");
			    fmsng.appendChild(Header);
			    Element Subledger = doc.createElement("Subledger");
			    fmsng.appendChild(Subledger);
			    
			    //Header elements
			    Element MessageId = doc.createElement("MessageId");
			    Element Scope = doc.createElement("Scope");
			    Element DateTimeStamp = doc.createElement("DateTimeStamp");
			    Element DataSource = doc.createElement("DataSource");
			    
			    Header.appendChild(MessageId);
			    Header.appendChild(Scope);
			    Header.appendChild(DateTimeStamp);
			    Header.appendChild(DataSource);
			    
			    MessageId.appendChild(doc.createTextNode(fms_MessageId));
			    Scope.appendChild(doc.createTextNode(""+utilBean.getCompanySAPcode(conn, comp_cd)+"Accounting"));
			    DateTimeStamp.appendChild(doc.createTextNode(date_timestamp));
			    //DataSource.appendChild(doc.createTextNode("FMSng"));
			    DataSource.appendChild(doc.createTextNode(CommonVariable.app_name));
			    
			    //Subledger elements
			    Element SubledgerHeader = doc.createElement("SubledgerHeader");
			    Subledger.appendChild(SubledgerHeader);
			    
			    Element BusinessActivity = doc.createElement("BusinessActivity");
			    Element DocumentType = doc.createElement("DocumentType"); 
			    Element DocumentDate = doc.createElement("DocumentDate"); 
			    Element PostingDate = doc.createElement("PostingDate"); 
			    Element AccountingPeriodMonth = doc.createElement("AccountingPeriodMonth");
			    Element AccountingPeriodYear = doc.createElement("AccountingPeriodYear");
			    Element InternalLegalEntity = doc.createElement("InternalLegalEntity"); 
			    Element DocHeaderText = doc.createElement("DocHeaderText"); 
			    Element RefNum = doc.createElement("RefNum"); 
			    Element EmsRefNum = doc.createElement("EmsRefNum"); 
			    Element Currency = doc.createElement("Currency"); 
			    Element LocalCurrency = doc.createElement("LocalCurrency"); 
			    Element TranslationDate = doc.createElement("TranslationDate");

			    //SubledgerHeader element
			    SubledgerHeader.appendChild(BusinessActivity);
			    SubledgerHeader.appendChild(DocumentType);
			    SubledgerHeader.appendChild(DocumentDate);
			    SubledgerHeader.appendChild(PostingDate);
			    SubledgerHeader.appendChild(AccountingPeriodMonth);
			    SubledgerHeader.appendChild(AccountingPeriodYear);
			    SubledgerHeader.appendChild(InternalLegalEntity);
			    SubledgerHeader.appendChild(DocHeaderText);
			    SubledgerHeader.appendChild(RefNum);
			    SubledgerHeader.appendChild(EmsRefNum);
			    SubledgerHeader.appendChild(Currency);
			    SubledgerHeader.appendChild(LocalCurrency);
			    SubledgerHeader.appendChild(TranslationDate);
			    
			    BusinessActivity.appendChild(doc.createTextNode("RFBU")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
			    DocumentType.appendChild(doc.createTextNode("X3"));
			    DocumentDate.appendChild(doc.createTextNode(documentDate));
			    PostingDate.appendChild(doc.createTextNode(xml_sysdate));
			    AccountingPeriodMonth.appendChild(doc.createTextNode(accountingPeriodMonth));
			    AccountingPeriodYear.appendChild(doc.createTextNode(accountingPeriodYear));
			    InternalLegalEntity.appendChild(doc.createTextNode(""+utilBean.getCompanySAPcode(conn, comp_cd)));
			    DocHeaderText.appendChild(doc.createTextNode(docHeaderText));
			    RefNum.appendChild(doc.createTextNode(xml_num));
			    EmsRefNum.appendChild(doc.createTextNode(xml_num));
			    if(cont_type.equals("N"))
			    {
			    	Currency.appendChild(doc.createTextNode("USD"));
			    }
			    else
			    {
			    	Currency.appendChild(doc.createTextNode("INR")); //NO NEED TO SEND OTHER TYPE OF CURRENCY - JD
			    }
				
			    String account=utilBean.getCounterpartySAPcode(conn,counterpty_cd);
			    String countpty_category=utilBean.getCounterpartyCategory(conn, counterpty_cd);
		    	
			    String tempAccount="";
		    	String tempAccount2="";//For even line seq
		    	String pk="31"; 
		    	String pk2="40";		    	
		    	String sign = "-";
		    	String sign2 = "";
		    	
		    	tempAccount=account; // 05-09-2023: Counterparty SAP CODE Will hit for both NG and IG
		    	if(cont_type.equals("G") || cont_type.equals("P"))
		    	{
		    		tempAccount2="7340450";
		    	}
		    	else
		    	{	
			    	if(countpty_category.equals("Group"))
			    	{
			    		//tempAccount=account; // Counterparty SAP CODE
			    		tempAccount2="6301400";
			    	}
			    	else
			    	{
			    		//tempAccount="3180720"; // PURCHASE ETRM ACCRUALS
			    		//String pk="50";// PURCHASE ETRM ACCRUALS 
			    		tempAccount2="6300400";
			    	}
		    	}	
		    	
		    	String plantNm=utilBean.getCounterpartyPlantName(conn,counterpty_cd, comp_cd, plant_seq, "T");
		    	
		    	String material_code="1168001";
		    	//String assignmentNo="T"+counterpty_cd+cont_type+contno;
		    	String assignmentNo=utilBean.NewDealMappingId(comp_cd, counterpty_cd, agmtno, "", contno, "", cont_type, cargoNo);
		    	if(cont_type.equals("N")) 
		    	{
		    		//assignmentNo="T"+counterpty_cd+cont_type+""+agmtno+"-"+contno+"-"+cargoNo;
		    		material_code="1168000";
		    	}
		    	else if(cont_type.equals("G") || cont_type.equals("P")) 
		    	{
		    		//assignmentNo="T"+counterpty_cd+cont_type+""+agmtno+"-"+contno+"-"+cargoNo;
		    		material_code="3344036";
		    	}
		    	String businessAreaCode=utilBean.getBusinessAreaSAPcode(conn, comp_cd, "T", cont_type, report_dt);
		    	
		    	int i=0;
				queryString1="SELECT TO_CHAR(INVOICE_DUE_DT,'DD/MM/YYYY'),ACCRUAL_QTY,ACCRUAL_AMT,GROSS_AMT,CASH_FLOW "
						+ "FROM FMS_TRADER_ACCRUAL_DTL "
						+ "WHERE COMPANY_CD=? "
						+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') AND PROD_MONTH=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
						+ "AND CONTRACT_TYPE=? AND PLANT_SEQ=? AND BU_UNIT=? "
						+ "AND FINANCIAL_YEAR=? AND FREQ=? AND CARGO_NO=? AND BOE_NO=? AND INV_FLAG=? ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, report_dt);
				stmt1.setString(3, prod_month);
				stmt1.setString(4, counterpty_cd);
				stmt1.setString(5, agmtno);
				stmt1.setString(6, contno);
				stmt1.setString(7, cont_type);
				stmt1.setString(8, plant_seq);
				stmt1.setString(9, buSeq);
				stmt1.setString(10, financialYear);
				stmt1.setString(11, bill_freq);
				stmt1.setString(12, cargoNo);
				stmt1.setString(13, boeNo);
				stmt1.setString(14, invFlag);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					String invoice_dueDt=rset1.getString(1)==null?"":rset1.getString(1);
					String accrual_qty=nf.format(rset1.getDouble(2));
					//3
					String grossAmt=nf.format(rset1.getDouble(4));
					String cashFlow=rset1.getString(5)==null?"":rset1.getString(5);
					if(cont_type.equals("N")) //SENDING ACCRUAL AMT FOR CARGO
					{
						grossAmt=nf.format(rset1.getDouble(3));
					}
					
					String monthNm = utilDate.getShortMonthName(prod_month);
			    	String monthId="";
					String yearNm = "";
			    	if(!prod_month.equals(""))
					{
						String[] temp_split=prod_month.split("/");
						monthId=temp_split[1];
						yearNm=temp_split[2];		
					}
							
			    	String itemText="";
			    	String itemText2=cashFlow+" "+monthNm+" "+yearNm;
			    	
					if(!invoice_dueDt.equals(""))
					{
						String[] temp_split=invoice_dueDt.split("/");
						invoice_dueDt=temp_split[2]+""+temp_split[1]+""+temp_split[0];
					}
					
					for(int j=0; j<2;j++)
					{	    
				    	i+=1;
				    	Element SubledgerEntry = doc.createElement("SubledgerEntry");
				    	Subledger.appendChild(SubledgerEntry);
				    	
				    	Element VendorId  = doc.createElement("VendorId");//
				    	Element LineSeqNo = doc.createElement("LineSeqNo");//
					    Element PostingKey = doc.createElement("PostingKey");//
					    Element Account = doc.createElement("Account");//
					    Element CurrencyAmount = doc.createElement("CurrencyAmount"); //
					    Element LocalCurrencyAmount = doc.createElement("LocalCurrencyAmount");//
					    Element Material = doc.createElement("Material");//
					    Element BusinessArea = doc.createElement("BusinessArea");//
					    Element ItemText = doc.createElement("ItemText");//
					    Element Volume = doc.createElement("Volume");//
					    Element VolumeUnit = doc.createElement("VolumeUnit");//
					    Element ReferenceKey1 = doc.createElement("ReferenceKey1");//
					    Element ReferenceKey2 = doc.createElement("ReferenceKey2");//
					    Element ProductionPeriod = doc.createElement("ProductionPeriod");//
					    Element AssignmentNumber = doc.createElement("AssignmentNumber");//
					    Element PaymentTerms = doc.createElement("PaymentTerms");//
					    Element PaymentBlock = doc.createElement("PaymentBlock");//
					    Element PaymentDueDate = doc.createElement("PaymentDueDate");//
					    Element Plant = doc.createElement("Plant");//
					    
				    	// SubledgerEntry elements
					    SubledgerEntry.appendChild(VendorId);//
					    SubledgerEntry.appendChild(LineSeqNo);//
					    SubledgerEntry.appendChild(PostingKey);//
					    SubledgerEntry.appendChild(Account);//
					    SubledgerEntry.appendChild(CurrencyAmount);//
					    SubledgerEntry.appendChild(LocalCurrencyAmount);//--
					    SubledgerEntry.appendChild(Material);//
					    SubledgerEntry.appendChild(BusinessArea);//
					    SubledgerEntry.appendChild(ItemText);//
					    SubledgerEntry.appendChild(Volume);//
					    SubledgerEntry.appendChild(VolumeUnit);//
					    SubledgerEntry.appendChild(ReferenceKey1);//
					    SubledgerEntry.appendChild(ReferenceKey2);
					    SubledgerEntry.appendChild(ProductionPeriod);//
					    SubledgerEntry.appendChild(AssignmentNumber);//
					    SubledgerEntry.appendChild(PaymentTerms);//
					    SubledgerEntry.appendChild(PaymentBlock);//
					    SubledgerEntry.appendChild(PaymentDueDate);//
					    SubledgerEntry.appendChild(Plant);					    
					   
					    if (i%2 == 0) 
					    {
					    	//VendorId.appendChild(doc.createTextNode(utilBean.PrePaddingZero(tempAccount2, 10)));
					    	PostingKey.appendChild(doc.createTextNode(pk2));
					    	Account.appendChild(doc.createTextNode(utilBean.PrePaddingZero(tempAccount2, 10)));
					    	ItemText.appendChild(doc.createTextNode(itemText2));
					    	CurrencyAmount.appendChild(doc.createTextNode(sign2+""+grossAmt));
					    }
					    else
					    {
					    	VendorId.appendChild(doc.createTextNode(tempAccount));//PADDING ZERO NOT REQUIRED FOR VENDOR ID
					    	PostingKey.appendChild(doc.createTextNode(pk));
					    	//Account.appendChild(doc.createTextNode(utilBean.PrePaddingZero(tempAccount, 10))); // PURCHASE ETRM ACCRUALS
					    	ItemText.appendChild(doc.createTextNode(itemText));
					    	CurrencyAmount.appendChild(doc.createTextNode(sign+""+grossAmt));
					    }
				    	LineSeqNo.appendChild(doc.createTextNode(""+i));
				    	
				    	Material.appendChild(doc.createTextNode(utilBean.PrePaddingZero(material_code, 18))); //NATURAL GAS MATERIAL CODE
				    	BusinessArea.appendChild(doc.createTextNode(businessAreaCode));
				    	ReferenceKey1.appendChild(doc.createTextNode(assignmentNo));
				    	ProductionPeriod.appendChild(doc.createTextNode(yearNm+""+monthId));
				    	AssignmentNumber.appendChild(doc.createTextNode(assignmentNo));
				    	Volume.appendChild(doc.createTextNode(accrual_qty));
				    	VolumeUnit.appendChild(doc.createTextNode("MMB"));
				    	PaymentTerms.appendChild(doc.createTextNode("ZB00")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
				    	PaymentDueDate.appendChild(doc.createTextNode(invoice_dueDt)); //AS PER SUNIDHI MAIL 16/08/2023
				    	PaymentBlock.appendChild(doc.createTextNode("A"));
				    	Plant.appendChild(doc.createTextNode(plantNm+" - Plant"));
					}
				}
				rset1.close();
				stmt1.close();
				
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD","");
				transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet","");
				
			    Transformer transformer = transformerFactory.newTransformer();
			    DOMSource source = new DOMSource(doc);
			    
			    String xmlFileNm="";
			    String datetime="";
			    datetime=splitSys[0].replaceAll("/", "")+""+splitSys[1].replaceAll(":", "");
			    
			    if(!fms_MessageId.equals(""))
			    {
			    	xmlFileNm="APGL_"+fms_MessageId+"_"+datetime+".xml";
			    }
				
			    if(!xmlFileNm.equals(""))
			    {
			    	StreamResult result =  new StreamResult(new File(file_path+""+xmlFileNm));
				    transformer.transform(source, result);
				    
				    xmlfile_name=xmlFileNm;
				    
				    if(!xml_seq.equals("") && !xml_num.equals(""))
					{
						queryString1="UPDATE FMS_TRADER_ACCRUAL_DTL SET XML_SEQ=?,XML_NUM=? "
								+ "WHERE COMPANY_CD=? "
								+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') AND PROD_MONTH=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
								+ "AND CONTRACT_TYPE=? AND PLANT_SEQ=? AND BU_UNIT=? "
								+ "AND FINANCIAL_YEAR=? AND FREQ=? AND CARGO_NO=? AND BOE_NO=? AND INV_FLAG=? ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, xml_seq);
						stmt1.setString(2, xml_num);
						stmt1.setString(3, comp_cd);
						stmt1.setString(4, report_dt);
						stmt1.setString(5, prod_month);
						stmt1.setString(6, counterpty_cd);
						stmt1.setString(7, agmtno);
						stmt1.setString(8, contno);
						stmt1.setString(9, cont_type);
						stmt1.setString(10, plant_seq);
						stmt1.setString(11, buSeq);
						stmt1.setString(12, financialYear);
						stmt1.setString(13, bill_freq);
						stmt1.setString(14, cargoNo);
						stmt1.setString(15, boeNo);
						stmt1.setString(16, invFlag);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
				}
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void generateGTAAccrualXML()
	{
		String function_nm="generateGTAAccrualXML()";
		try
		{
			String sysdate=utilDate.getSysdate();
			String sysdateWithTime=utilDate.getSysdateWithTime24hr();
			String xml_sysdate="";
			String postingMonth="";
			String[] split=sysdate.split("/");
			xml_sysdate=split[2]+""+split[1]+""+split[0];
			postingMonth=split[2]+""+split[1];
			
			String[] splitSys = sysdateWithTime.split(" ");
			String date_timestamp=xml_sysdate+" "+splitSys[1];
			
			String fms_MessageId = "";
			String accountingPeriodMonth="";
			String accountingPeriodYear="";
			
			String documentDate="";
			if(!report_dt.equals(""))
			{
				String[] temp_split=report_dt.split("/");
				accountingPeriodMonth=temp_split[1];
				accountingPeriodYear=temp_split[2];	
				
				documentDate=temp_split[2]+""+temp_split[1]+""+temp_split[0];
			}
			
			String invoice_prefix=utilBean.getInvoicePrefix(conn,comp_cd);
			
			queryString="SELECT DISTINCT COUNTERPARTY_CD,AGMT_NO,CONT_NO,CONTRACT_TYPE,TRANS_BU_UNIT,BU_UNIT,"
					+ "FINANCIAL_YEAR,CASH_FLOW,TO_CHAR(PROD_MONTH,'DD/MM/YYYY'),FREQ,INV_COMPONENT "
					+ "FROM FMS_GTA_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? "
					+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY')";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, report_dt);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String counterpty_cd=rset.getString(1)==null?"":rset.getString(1);
				String agmtno=rset.getString(2)==null?"":rset.getString(2);
				String contno=rset.getString(3)==null?"":rset.getString(3);
				String cont_type=rset.getString(4)==null?"":rset.getString(4);
				String trans_bu_seq=rset.getString(5)==null?"":rset.getString(5);
				String buSeq=rset.getString(6)==null?"":rset.getString(6);
				String financialYear=rset.getString(7)==null?"":rset.getString(7);
				String cashFlow=rset.getString(8)==null?"":rset.getString(8);
				String prod_month=rset.getString(9)==null?"":rset.getString(9);
				String bill_freq=rset.getString(10)==null?"":rset.getString(10);
				String invComponent=rset.getString(11)==null?"":rset.getString(11);
				
				String buStateNm="";
				String buAbbr="";
				queryString1 = "SELECT PLANT_STATE,PLANT_ABBR "
						+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
						+ "WHERE COUNTERPARTY_CD=? AND ENTITY='B' AND COMPANY_CD=? AND SEQ_NO=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, comp_cd);
				stmt1.setString(3, buSeq);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					buStateNm=rset1.getString(1)==null?"":rset1.getString(1);
					buAbbr=rset1.getString(2)==null?"":rset1.getString(2);
				}
				rset1.close();
				stmt1.close();
				docHeaderText=buStateNm+"/"+buAbbr+" - BU";
				
				String xml_seq="1";
				String xml_num="";
				queryString1="SELECT NVL(MAX(XML_SEQ),0) "
						+ "FROM FMS_GTA_ACCRUAL_DTL "
						+ "WHERE COMPANY_CD=? "
						+ "AND TO_DATE(TO_CHAR(REPORT_DT,'MM/YYYY'),'MM/YYYY')=TO_DATE(?,'MM/YYYY') ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, accountingPeriodMonth+"/"+accountingPeriodYear);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					xml_seq=""+(rset1.getInt(1)+1);
				}
				rset1.close();
				stmt1.close();
				xml_num=invoice_prefix+"R"+utilBean.PrePaddingZero(xml_seq, 4)+"/"+accountingPeriodMonth+""+accountingPeriodYear;
				
				fms_MessageId=xml_num.replaceAll("/", "-");
		    	
				DocumentBuilderFactory docFactory = xmlUtil.dcoumentBuilderFactory();
			    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
			    Document doc = docBuilder.newDocument();
			    
			    //root fmsng
			    Element fmsng = doc.createElement("EmsSAPGeneralLedgerMessage");
			    doc.appendChild(fmsng);
		
			    //root elements
			    Element Header = doc.createElement("Header");
			    fmsng.appendChild(Header);
			    Element Subledger = doc.createElement("Subledger");
			    fmsng.appendChild(Subledger);
			    
			    //Header elements
			    Element MessageId = doc.createElement("MessageId");
			    Element Scope = doc.createElement("Scope");
			    Element DateTimeStamp = doc.createElement("DateTimeStamp");
			    Element DataSource = doc.createElement("DataSource");
			    
			    Header.appendChild(MessageId);
			    Header.appendChild(Scope);
			    Header.appendChild(DateTimeStamp);
			    Header.appendChild(DataSource);
			    
			    MessageId.appendChild(doc.createTextNode(fms_MessageId));
			    Scope.appendChild(doc.createTextNode(""+utilBean.getCompanySAPcode(conn, comp_cd)+"Accounting"));
			    DateTimeStamp.appendChild(doc.createTextNode(date_timestamp));
			    //DataSource.appendChild(doc.createTextNode("FMSng"));
			    DataSource.appendChild(doc.createTextNode(CommonVariable.app_name));
			    
			    //Subledger elements
			    Element SubledgerHeader = doc.createElement("SubledgerHeader");
			    Subledger.appendChild(SubledgerHeader);
			    
			    Element BusinessActivity = doc.createElement("BusinessActivity");
			    Element DocumentType = doc.createElement("DocumentType"); 
			    Element DocumentDate = doc.createElement("DocumentDate"); 
			    Element PostingDate = doc.createElement("PostingDate"); 
			    Element AccountingPeriodMonth = doc.createElement("AccountingPeriodMonth");
			    Element AccountingPeriodYear = doc.createElement("AccountingPeriodYear");
			    Element InternalLegalEntity = doc.createElement("InternalLegalEntity"); 
			    Element DocHeaderText = doc.createElement("DocHeaderText"); 
			    Element RefNum = doc.createElement("RefNum"); 
			    Element EmsRefNum = doc.createElement("EmsRefNum"); 
			    Element Currency = doc.createElement("Currency"); 
			    Element LocalCurrency = doc.createElement("LocalCurrency"); 
			    Element TranslationDate = doc.createElement("TranslationDate");

			    
			    //SubledgerHeader element
			    SubledgerHeader.appendChild(BusinessActivity);
			    SubledgerHeader.appendChild(DocumentType);
			    SubledgerHeader.appendChild(DocumentDate);
			    SubledgerHeader.appendChild(PostingDate);
			    SubledgerHeader.appendChild(AccountingPeriodMonth);
			    SubledgerHeader.appendChild(AccountingPeriodYear);
			    SubledgerHeader.appendChild(InternalLegalEntity);
			    SubledgerHeader.appendChild(DocHeaderText);
			    SubledgerHeader.appendChild(RefNum);
			    SubledgerHeader.appendChild(EmsRefNum);
			    SubledgerHeader.appendChild(Currency);
			    SubledgerHeader.appendChild(LocalCurrency);
			    SubledgerHeader.appendChild(TranslationDate);
			    
			    BusinessActivity.appendChild(doc.createTextNode("RFBU")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
			    DocumentType.appendChild(doc.createTextNode("X3"));
			    DocumentDate.appendChild(doc.createTextNode(documentDate));
			    PostingDate.appendChild(doc.createTextNode(xml_sysdate));
			    AccountingPeriodMonth.appendChild(doc.createTextNode(accountingPeriodMonth));
			    AccountingPeriodYear.appendChild(doc.createTextNode(accountingPeriodYear));
			    InternalLegalEntity.appendChild(doc.createTextNode(utilBean.getCompanySAPcode(conn, comp_cd)));
			    DocHeaderText.appendChild(doc.createTextNode(docHeaderText));
			    RefNum.appendChild(doc.createTextNode(xml_num));
			    EmsRefNum.appendChild(doc.createTextNode(xml_num));
			    Currency.appendChild(doc.createTextNode("INR")); //NO NEED TO SEND OTHER TYPE OF CURRENCY - JD
				
			    String account=utilBean.getCounterpartySAPcode(conn,counterpty_cd);
			    String countpty_category=utilBean.getCounterpartyCategory(conn, counterpty_cd);
		    	
		    	String tempAccount="";
		    	String tempAccount2="";//For even line seq
		    	String pk2="40";
		    	//String pk="50"; //For even line seq
		    	String pk="31"; //For even line seq
		    	String sign = "-";
		    	String sign2 = "";
		    	
		    	tempAccount=account; // 05-09-2023: Counterparty SAP CODE Will hit for both NG and IG 
		    	if(cashFlow.equals("IC"))
		    	{
			    	//INCIDENT#2310112 DIVAY HAS ASKED TO PASS GL CODE 6318400	Pipeline Imbalance  
			    	tempAccount2="6318400";
		    	}
		    	else
		    	{
		    		if(countpty_category.equals("Group"))
			    	{
			    		//tempAccount=account; // Counterparty SAP CODE
			    		tempAccount2="6301400";
			    	}
			    	else
			    	{
			    		//tempAccount="3180720"; // PURCHASE ETRM ACCRUALS
			    		//pk="50";
			    		tempAccount2="6300400";
			    	}
		    	}
		    	
		    	
		    	String transBuNm=utilBean.getCounterpartyBuABBR(conn,counterpty_cd, comp_cd, trans_bu_seq, "R");
		    	
		    	//String assignmentNo="R"+counterpty_cd+cont_type+agmtno+"-"+contno;
		    	String assignmentNo=utilBean.NewDealMappingId(comp_cd, counterpty_cd, agmtno, "", contno, "", cont_type, "");
		    	String businessAreaCode=utilBean.getBusinessAreaSAPcode(conn, comp_cd, "R", cont_type, report_dt);
		    	
		    	int i=0;
				queryString1="SELECT TO_CHAR(INVOICE_DUE_DT,'DD/MM/YYYY'),ACCRUAL_QTY,ACCRUAL_AMT,GROSS_AMT "
						+ "FROM FMS_GTA_ACCRUAL_DTL "
						+ "WHERE COMPANY_CD=? "
						+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') AND PROD_MONTH=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
						+ "AND CONTRACT_TYPE=? AND TRANS_BU_UNIT=? AND BU_UNIT=? "
						+ "AND FINANCIAL_YEAR=? AND CASH_FLOW=? AND FREQ=? AND INV_COMPONENT=? ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, report_dt);
				stmt1.setString(3, prod_month);
				stmt1.setString(4, counterpty_cd);
				stmt1.setString(5, agmtno);
				stmt1.setString(6, contno);
				stmt1.setString(7, cont_type);
				stmt1.setString(8, trans_bu_seq);
				stmt1.setString(9, buSeq);
				stmt1.setString(10, financialYear);
				stmt1.setString(11, cashFlow);
				stmt1.setString(12, bill_freq);
				stmt1.setString(13, invComponent);
				rset1 = stmt1.executeQuery();
				while(rset1.next())
				{
					String invoice_dueDt=rset1.getString(1)==null?"":rset1.getString(1);
					String accrual_qty=nf.format(rset1.getDouble(2));
					//3
					String grossAmt=nf.format(rset1.getDouble(4));
					
					String monthNm = utilDate.getShortMonthName(prod_month);
			    	String monthId="";
					String yearNm = "";
			    	if(!prod_month.equals(""))
					{
						String[] temp_split=prod_month.split("/");
						monthId=temp_split[1];
						yearNm=temp_split[2];		
					}
							
			    	String itemText="";
			    	String itemText2="Transport Services "+monthNm+" "+yearNm;
			    	
					if(!invoice_dueDt.equals(""))
					{
						String[] temp_split=invoice_dueDt.split("/");
						invoice_dueDt=temp_split[2]+""+temp_split[1]+""+temp_split[0];
					}
					
					for(int j=0; j<2;j++)
					{	    
				    	i+=1;
				    	Element SubledgerEntry = doc.createElement("SubledgerEntry");
				    	Subledger.appendChild(SubledgerEntry);
				    	
				    	Element VendorId  = doc.createElement("VendorId");//
				    	Element LineSeqNo = doc.createElement("LineSeqNo");//
					    Element PostingKey = doc.createElement("PostingKey");//
					    Element Account = doc.createElement("Account");//
					    Element CurrencyAmount = doc.createElement("CurrencyAmount"); //
					    Element LocalCurrencyAmount = doc.createElement("LocalCurrencyAmount");//
					    Element Material = doc.createElement("Material");//
					    Element BusinessArea = doc.createElement("BusinessArea");//
					    Element ItemText = doc.createElement("ItemText");//
					    Element Volume = doc.createElement("Volume");//
					    Element VolumeUnit = doc.createElement("VolumeUnit");//
					    Element ReferenceKey1 = doc.createElement("ReferenceKey1");//
					    Element ReferenceKey2 = doc.createElement("ReferenceKey2");//
					    Element ProductionPeriod = doc.createElement("ProductionPeriod");//
					    Element AssignmentNumber = doc.createElement("AssignmentNumber");//
					    Element PaymentTerms = doc.createElement("PaymentTerms");//
					    Element PaymentBlock = doc.createElement("PaymentBlock");//
					    Element PaymentDueDate = doc.createElement("PaymentDueDate");//
					    Element Plant = doc.createElement("Plant");//
					    
				    	// SubledgerEntry elements
					    SubledgerEntry.appendChild(VendorId);//
					    SubledgerEntry.appendChild(LineSeqNo);//
					    SubledgerEntry.appendChild(PostingKey);//
					    SubledgerEntry.appendChild(Account);//
					    SubledgerEntry.appendChild(CurrencyAmount);//
					    SubledgerEntry.appendChild(LocalCurrencyAmount);//--
					    SubledgerEntry.appendChild(Material);//
					    SubledgerEntry.appendChild(BusinessArea);//
					    SubledgerEntry.appendChild(ItemText);//
					    SubledgerEntry.appendChild(Volume);//
					    SubledgerEntry.appendChild(VolumeUnit);//
					    SubledgerEntry.appendChild(ReferenceKey1);//
					    SubledgerEntry.appendChild(ReferenceKey2);
					    SubledgerEntry.appendChild(ProductionPeriod);//
					    SubledgerEntry.appendChild(AssignmentNumber);//
					    SubledgerEntry.appendChild(PaymentTerms);//
					    SubledgerEntry.appendChild(PaymentBlock);//
					    SubledgerEntry.appendChild(PaymentDueDate);//
					    SubledgerEntry.appendChild(Plant);					    
					   
					    if (i%2 == 0) 
					    {
					    	//VendorId.appendChild(doc.createTextNode(utilBean.PrePaddingZero(tempAccount2, 10)));
					    	PostingKey.appendChild(doc.createTextNode(pk2));
					    	Account.appendChild(doc.createTextNode(utilBean.PrePaddingZero(tempAccount2, 10)));
					    	ItemText.appendChild(doc.createTextNode(itemText2));
					    	CurrencyAmount.appendChild(doc.createTextNode(sign2+""+grossAmt));
					    }
					    else
					    {
					    	VendorId.appendChild(doc.createTextNode(tempAccount));
					    	PostingKey.appendChild(doc.createTextNode(pk));
					    	//Account.appendChild(doc.createTextNode(utilBean.PrePaddingZero(tempAccount, 10)));
					    	ItemText.appendChild(doc.createTextNode(itemText));
					    	CurrencyAmount.appendChild(doc.createTextNode(sign+""+grossAmt));
					    }
				    	LineSeqNo.appendChild(doc.createTextNode(""+i));
				    	
				    	Material.appendChild(doc.createTextNode(utilBean.PrePaddingZero("3344036", 18))); //NATURAL GAS MATERIAL CODE
				    	BusinessArea.appendChild(doc.createTextNode(businessAreaCode));
				    	ReferenceKey1.appendChild(doc.createTextNode(assignmentNo));
				    	ProductionPeriod.appendChild(doc.createTextNode(yearNm+""+monthId));
				    	AssignmentNumber.appendChild(doc.createTextNode(assignmentNo));
				    	Volume.appendChild(doc.createTextNode(accrual_qty));
				    	VolumeUnit.appendChild(doc.createTextNode("MMB"));
				    	PaymentTerms.appendChild(doc.createTextNode("ZB00")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
				    	PaymentDueDate.appendChild(doc.createTextNode(invoice_dueDt)); //AS PER SUNIDHI MAIL 16/08/2023
				    	PaymentBlock.appendChild(doc.createTextNode("A"));
				    	Plant.appendChild(doc.createTextNode(transBuNm+" - Plant"));
					}
				}
				rset1.close();
				stmt1.close();
				
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD","");
				transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet","");
				
			    Transformer transformer = transformerFactory.newTransformer();
			    DOMSource source = new DOMSource(doc);
			    
			    String xmlFileNm="";
			    String datetime="";
			    datetime=splitSys[0].replaceAll("/", "")+""+splitSys[1].replaceAll(":", "");
			    
			    if(!fms_MessageId.equals(""))
			    {
			    	xmlFileNm="APGL_"+fms_MessageId+"_"+datetime+".xml";
			    }
				
			    if(!xmlFileNm.equals(""))
			    {
				    StreamResult result =  new StreamResult(new File(file_path+""+xmlFileNm));
				    transformer.transform(source, result);
				    
				    xmlfile_name=xmlFileNm;
				    
				    if(!xml_seq.equals("") && !xml_num.equals(""))
					{
						queryString1="UPDATE FMS_GTA_ACCRUAL_DTL SET XML_SEQ=?,XML_NUM=? "
								+ "WHERE COMPANY_CD=? "
								+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') AND PROD_MONTH=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
								+ "AND CONTRACT_TYPE=? AND TRANS_BU_UNIT=? AND BU_UNIT=? "
								+ "AND FINANCIAL_YEAR=? AND CASH_FLOW=? AND FREQ=? AND INV_COMPONENT=?";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, xml_seq);
						stmt1.setString(2, xml_num);
						stmt1.setString(3, comp_cd);
						stmt1.setString(4, report_dt);
						stmt1.setString(5, prod_month);
						stmt1.setString(6, counterpty_cd);
						stmt1.setString(7, agmtno);
						stmt1.setString(8, contno);
						stmt1.setString(9, cont_type);
						stmt1.setString(10, trans_bu_seq);
						stmt1.setString(11, buSeq);
						stmt1.setString(12, financialYear);
						stmt1.setString(13, cashFlow);
						stmt1.setString(14, bill_freq);
						stmt1.setString(15, invComponent);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
				}
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void generateSalesAccrualXML()
	{
		String function_nm="generateSalesAccrualXML()";
		try
		{
			String sysdate=utilDate.getSysdate();
			String sysdateWithTime=utilDate.getSysdateWithTime24hr();
			String xml_sysdate="";
			String postingMonth="";
			String[] split=sysdate.split("/");
			xml_sysdate=split[2]+""+split[1]+""+split[0];
			postingMonth=split[2]+""+split[1];
			
			String[] splitSys = sysdateWithTime.split(" ");
			String date_timestamp=xml_sysdate+" "+splitSys[1];
			
			String fms_MessageId = "";
			String accountingPeriodMonth="";
			String accountingPeriodYear="";
			
			String documentDate="";
			if(!report_dt.equals(""))
			{
				String[] temp_split=report_dt.split("/");
				accountingPeriodMonth=temp_split[1];
				accountingPeriodYear=temp_split[2];	
				
				documentDate=temp_split[2]+""+temp_split[1]+""+temp_split[0];
			}
			
			String invoice_prefix=utilBean.getInvoicePrefix(conn,comp_cd);
			
			queryString="SELECT DISTINCT COUNTERPARTY_CD,AGMT_NO,CONT_NO,CONTRACT_TYPE,PLANT_SEQ,BU_UNIT,"
					+ "FINANCIAL_YEAR,TO_CHAR(PROD_MONTH,'DD/MM/YYYY'),FREQ,CARGO_NO "
					+ "FROM FMS_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? "
					+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY')";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, report_dt);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String counterpty_cd=rset.getString(1)==null?"":rset.getString(1);
				String agmtno=rset.getString(2)==null?"":rset.getString(2);
				String contno=rset.getString(3)==null?"":rset.getString(3);
				String cont_type=rset.getString(4)==null?"":rset.getString(4);
				String plant_seq=rset.getString(5)==null?"":rset.getString(5);
				String buSeq=rset.getString(6)==null?"":rset.getString(6);
				String financialYear=rset.getString(7)==null?"":rset.getString(7);
				String prod_month=rset.getString(8)==null?"":rset.getString(8);
				String bill_freq=rset.getString(9)==null?"":rset.getString(9);
				String cargo_no=rset.getString(10)==null?"":rset.getString(10);
				
				String buStateNm="";
				String buAbbr="";
				queryString1 = "SELECT PLANT_STATE,PLANT_ABBR "
						+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
						+ "WHERE COUNTERPARTY_CD=? AND ENTITY='B' AND COMPANY_CD=? AND SEQ_NO=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, comp_cd);
				stmt1.setString(3, buSeq);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					buStateNm=rset1.getString(1)==null?"":rset1.getString(1);
					buAbbr=rset1.getString(2)==null?"":rset1.getString(2);
				}
				rset1.close();
				stmt1.close();
				docHeaderText=buStateNm+"/"+buAbbr+" - BU";
				
				String xml_seq="1";
				String xml_num="";
				queryString1="SELECT NVL(MAX(XML_SEQ),0) "
						+ "FROM FMS_ACCRUAL_DTL "
						+ "WHERE COMPANY_CD=? "
						+ "AND TO_DATE(TO_CHAR(REPORT_DT,'MM/YYYY'),'MM/YYYY')=TO_DATE(?,'MM/YYYY') ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, accountingPeriodMonth+"/"+accountingPeriodYear);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					xml_seq=""+(rset1.getInt(1)+1);
				}
				rset1.close();
				stmt1.close();
				xml_num=invoice_prefix+"C"+utilBean.PrePaddingZero(xml_seq, 4)+"/"+accountingPeriodMonth+""+accountingPeriodYear;
				
				fms_MessageId=xml_num.replaceAll("/", "-");
				
				DocumentBuilderFactory docFactory = xmlUtil.dcoumentBuilderFactory();
			    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
			    Document doc = docBuilder.newDocument();
			    
			    //root fmsng
			    Element fmsng = doc.createElement("EmsSAPGeneralLedgerMessage");
			    doc.appendChild(fmsng);
		
			    //root elements
			    Element Header = doc.createElement("Header");
			    fmsng.appendChild(Header);
			    Element Subledger = doc.createElement("Subledger");
			    fmsng.appendChild(Subledger);
			    
			    //Header elements
			    Element MessageId = doc.createElement("MessageId");
			    Element Scope = doc.createElement("Scope");
			    Element DateTimeStamp = doc.createElement("DateTimeStamp");
			    Element DataSource = doc.createElement("DataSource");
			    
			    Header.appendChild(MessageId);
			    Header.appendChild(Scope);
			    Header.appendChild(DateTimeStamp);
			    Header.appendChild(DataSource);
			    
			    MessageId.appendChild(doc.createTextNode(fms_MessageId));
			    Scope.appendChild(doc.createTextNode(""+utilBean.getCompanySAPcode(conn, comp_cd)+"Accounting"));
			    DateTimeStamp.appendChild(doc.createTextNode(date_timestamp));
			    //DataSource.appendChild(doc.createTextNode("FMSng"));
			    DataSource.appendChild(doc.createTextNode(CommonVariable.app_name));
			    
			    //Subledger elements
			    Element SubledgerHeader = doc.createElement("SubledgerHeader");
			    Subledger.appendChild(SubledgerHeader);
			    
			    Element BusinessActivity = doc.createElement("BusinessActivity");
			    Element DocumentType = doc.createElement("DocumentType"); 
			    Element DocumentDate = doc.createElement("DocumentDate"); 
			    Element PostingDate = doc.createElement("PostingDate"); 
			    Element AccountingPeriodMonth = doc.createElement("AccountingPeriodMonth");
			    Element AccountingPeriodYear = doc.createElement("AccountingPeriodYear");
			    Element InternalLegalEntity = doc.createElement("InternalLegalEntity"); 
			    Element DocHeaderText = doc.createElement("DocHeaderText"); 
			    Element RefNum = doc.createElement("RefNum"); 
			    Element EmsRefNum = doc.createElement("EmsRefNum"); 
			    Element Currency = doc.createElement("Currency"); 
			    Element LocalCurrency = doc.createElement("LocalCurrency"); 
			    Element TranslationDate = doc.createElement("TranslationDate");

			    
			    //SubledgerHeader element
			    SubledgerHeader.appendChild(BusinessActivity);
			    SubledgerHeader.appendChild(DocumentType);
			    SubledgerHeader.appendChild(DocumentDate);
			    SubledgerHeader.appendChild(PostingDate);
			    SubledgerHeader.appendChild(AccountingPeriodMonth);
			    SubledgerHeader.appendChild(AccountingPeriodYear);
			    SubledgerHeader.appendChild(InternalLegalEntity);
			    SubledgerHeader.appendChild(DocHeaderText);
			    SubledgerHeader.appendChild(RefNum);
			    SubledgerHeader.appendChild(EmsRefNum);
			    SubledgerHeader.appendChild(Currency);
			    SubledgerHeader.appendChild(LocalCurrency);
			    SubledgerHeader.appendChild(TranslationDate);
			    
			    BusinessActivity.appendChild(doc.createTextNode("RFBU")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
			    DocumentType.appendChild(doc.createTextNode("X4"));
			    DocumentDate.appendChild(doc.createTextNode(documentDate));
			    PostingDate.appendChild(doc.createTextNode(xml_sysdate));
			    AccountingPeriodMonth.appendChild(doc.createTextNode(accountingPeriodMonth));
			    AccountingPeriodYear.appendChild(doc.createTextNode(accountingPeriodYear));
			    InternalLegalEntity.appendChild(doc.createTextNode(utilBean.getCompanySAPcode(conn, comp_cd)));
			    DocHeaderText.appendChild(doc.createTextNode(docHeaderText));
			    RefNum.appendChild(doc.createTextNode(xml_num));
			    EmsRefNum.appendChild(doc.createTextNode(xml_num));
			    Currency.appendChild(doc.createTextNode("INR")); //NO NEED TO SEND OTHER TYPE OF CURRENCY - JD
				
			    String account=utilBean.getCounterpartySAPcode(conn,counterpty_cd);
			    String countpty_category=utilBean.getCounterpartyCategory(conn, counterpty_cd);
				
		    	String tempAccount="";
		    	String tempAccount2="";//For even line seq
		    	String pk="01";    	
		    	String pk2="50"; //For even line seq
		    	String sign2 = "-";
		    	
		    	tempAccount=account; // 05-09-2023: Counterparty SAP CODE Will hit for both NG and IG 
		    	if(cont_type.equals("O") || cont_type.equals("Q"))
		    	{
		    		if(countpty_category.equals("Group"))
			    	{
			    		//tempAccount=account; // Counterparty SAP CODE
			    		tempAccount2="6000450";
			    	}
			    	else
			    	{
			    		//tempAccount="2781720"; // SALES ETRM ACCRUALS
			    		//String pk="40"; // SALES ETRM ACCRUALS
			    		tempAccount2="6000500";
			    	}
		    	}
		    	else
		    	{
			    	if(countpty_category.equals("Group"))
			    	{
			    		//tempAccount=account; // Counterparty SAP CODE
			    		tempAccount2="6001400";
			    	}
			    	else
			    	{
			    		//tempAccount="2781720"; // SALES ETRM ACCRUALS
			    		//String pk="40"; // SALES ETRM ACCRUALS
			    		tempAccount2="6000400";
			    	}
		    	}
		    	
		    	String plantNm=utilBean.getCounterpartyPlantName(conn,counterpty_cd, comp_cd, plant_seq, "C");
		    	
		    	/*String assignmentNo="";
		    	if(cont_type.equals("S"))
		    	{
		    		assignmentNo="C"+counterpty_cd+cont_type+agmtno+"-"+contno;
		    	}
		    	else
		    	{
		    		assignmentNo="C"+counterpty_cd+cont_type+contno;
		    	}*/
		    	
		    	String assignmentNo=utilBean.NewDealMappingId(comp_cd, counterpty_cd, agmtno, "", contno, "", cont_type, cargo_no);
		    	String material_code="1168001";
		    	if(cont_type.equals("Q") || cont_type.equals("O")) 
		    	{
		    		material_code="3344036";
		    	}
		    	String businessAreaCode=utilBean.getBusinessAreaSAPcode(conn, comp_cd, "C", cont_type, report_dt);
		    	
		    	int i=0;
				queryString1="SELECT TO_CHAR(INVOICE_DUE_DT,'DD/MM/YYYY'),ACCRUAL_QTY,ACCRUAL_AMT,GROSS_AMT,CASH_FLOW "
						+ "FROM FMS_ACCRUAL_DTL "
						+ "WHERE COMPANY_CD=? "
						+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') AND PROD_MONTH=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
						+ "AND CONTRACT_TYPE=? AND PLANT_SEQ=? AND BU_UNIT=? "
						+ "AND FINANCIAL_YEAR=? AND FREQ=? AND CARGO_NO=? ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, report_dt);
				stmt1.setString(3, prod_month);
				stmt1.setString(4, counterpty_cd);
				stmt1.setString(5, agmtno);
				stmt1.setString(6, contno);
				stmt1.setString(7, cont_type);
				stmt1.setString(8, plant_seq);
				stmt1.setString(9, buSeq);
				stmt1.setString(10, financialYear);
				stmt1.setString(11, bill_freq);
				stmt1.setString(12, cargo_no);
				rset1 = stmt1.executeQuery();
				while(rset1.next())
				{
					String invoice_dueDt=rset1.getString(1)==null?"":rset1.getString(1);
					String accrual_qty=nf.format(rset1.getDouble(2));
					//3
					String grossAmt=nf.format(rset1.getDouble(4));
					String cashFlow=rset1.getString(5)==null?"":rset1.getString(5);
					
					String monthNm = utilDate.getShortMonthName(prod_month);
			    	String yearNm = "";
			    	String monthId = "";
			    	if(!prod_month.equals(""))
					{
						String[] temp_split=prod_month.split("/");
						monthId=temp_split[1];
						yearNm=temp_split[2];		
					}
							
			    	String itemText="";
			    	String itemText2=cashFlow+" "+monthNm+" "+yearNm;
			    	
					if(!invoice_dueDt.equals(""))
					{
						String[] temp_split=invoice_dueDt.split("/");
						invoice_dueDt=temp_split[2]+""+temp_split[1]+""+temp_split[0];
					}
					
					for(int j=0; j<2;j++)
					{	    
				    	i+=1;
				    	Element SubledgerEntry = doc.createElement("SubledgerEntry");
				    	Subledger.appendChild(SubledgerEntry);
				    	
				    	Element VendorId  = doc.createElement("CustomerId");//
				    	Element LineSeqNo = doc.createElement("LineSeqNo");//
					    Element PostingKey = doc.createElement("PostingKey");//
					    Element Account = doc.createElement("Account");//
					    Element CurrencyAmount = doc.createElement("CurrencyAmount"); //
					    Element LocalCurrencyAmount = doc.createElement("LocalCurrencyAmount");//
					    Element Material = doc.createElement("Material");//
					    Element BusinessArea = doc.createElement("BusinessArea");//
					    Element ItemText = doc.createElement("ItemText");//
					    Element Volume = doc.createElement("Volume");//
					    Element VolumeUnit = doc.createElement("VolumeUnit");//
					    Element ReferenceKey1 = doc.createElement("ReferenceKey1");//
					    Element ReferenceKey2 = doc.createElement("ReferenceKey2");//
					    Element ProductionPeriod = doc.createElement("ProductionPeriod");//
					    Element AssignmentNumber = doc.createElement("AssignmentNumber");//
					    Element PaymentTerms = doc.createElement("PaymentTerms");//
					    Element PaymentBlock = doc.createElement("PaymentBlock");//
					    Element PaymentDueDate = doc.createElement("PaymentDueDate");//
					    Element Plant = doc.createElement("Plant");//
					    
				    	// SubledgerEntry elements
					    SubledgerEntry.appendChild(VendorId);//
					    SubledgerEntry.appendChild(LineSeqNo);//
					    SubledgerEntry.appendChild(PostingKey);//
					    SubledgerEntry.appendChild(Account);//
					    SubledgerEntry.appendChild(CurrencyAmount);//
					    SubledgerEntry.appendChild(LocalCurrencyAmount);//--
					    SubledgerEntry.appendChild(Material);//
					    SubledgerEntry.appendChild(BusinessArea);//
					    SubledgerEntry.appendChild(ItemText);//
					    SubledgerEntry.appendChild(Volume);//
					    SubledgerEntry.appendChild(VolumeUnit);//
					    SubledgerEntry.appendChild(ReferenceKey1);//
					    SubledgerEntry.appendChild(ReferenceKey2);
					    SubledgerEntry.appendChild(ProductionPeriod);//
					    SubledgerEntry.appendChild(AssignmentNumber);//
					    SubledgerEntry.appendChild(PaymentTerms);//
					    SubledgerEntry.appendChild(PaymentBlock);//
					    SubledgerEntry.appendChild(PaymentDueDate);//
					    SubledgerEntry.appendChild(Plant);					    
					   
					    if (i%2 == 0) 
					    {
					    	//VendorId.appendChild(doc.createTextNode(utilBean.PrePaddingZero(tempAccount2, 10)));
					    	PostingKey.appendChild(doc.createTextNode(pk2));
					    	Account.appendChild(doc.createTextNode(utilBean.PrePaddingZero(tempAccount2, 10)));
					    	ItemText.appendChild(doc.createTextNode(itemText2));
					    	CurrencyAmount.appendChild(doc.createTextNode(sign2+""+grossAmt));
					    }
					    else
					    {
					    	VendorId.appendChild(doc.createTextNode(tempAccount));
					    	PostingKey.appendChild(doc.createTextNode(pk));
					    	//Account.appendChild(doc.createTextNode(utilBean.PrePaddingZero(tempAccount, 10)));
					    	ItemText.appendChild(doc.createTextNode(itemText));
					    	CurrencyAmount.appendChild(doc.createTextNode(grossAmt));
					    }
				    	LineSeqNo.appendChild(doc.createTextNode(""+i));
				    	
				    	//Material.appendChild(doc.createTextNode(utilBean.PrePaddingZero("1168001", 18))); //NATURAL GAS MATERIAL CODE
				    	Material.appendChild(doc.createTextNode(utilBean.PrePaddingZero(material_code, 18)));
				    	BusinessArea.appendChild(doc.createTextNode(businessAreaCode));
				    	ReferenceKey1.appendChild(doc.createTextNode(assignmentNo));
				    	ProductionPeriod.appendChild(doc.createTextNode(yearNm+""+monthId));
				    	AssignmentNumber.appendChild(doc.createTextNode(assignmentNo));
				    	Volume.appendChild(doc.createTextNode(accrual_qty));
				    	VolumeUnit.appendChild(doc.createTextNode("MMB"));
				    	PaymentTerms.appendChild(doc.createTextNode("ZB00")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
				    	PaymentDueDate.appendChild(doc.createTextNode(invoice_dueDt)); //AS PER SUNIDHI MAIL 16/08/2023
				    	PaymentBlock.appendChild(doc.createTextNode("A"));
				    	Plant.appendChild(doc.createTextNode(plantNm+" - Plant"));
					}
				}
				rset1.close();
				stmt1.close();
				
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD","");
				transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet","");
				
			    Transformer transformer = transformerFactory.newTransformer();
			    DOMSource source = new DOMSource(doc);
			    
			    String xmlFileNm="";
			    String datetime="";
			    datetime=splitSys[0].replaceAll("/", "")+""+splitSys[1].replaceAll(":", "");
			    
			    if(!fms_MessageId.equals(""))
			    {
			    	xmlFileNm="ARGL_"+fms_MessageId+"_"+datetime+".xml";
			    	//xmlFileNm="ARGL_"+fms_MessageId+".xml";
			    }
				
			    if(!xmlFileNm.equals(""))
			    {
			    	StreamResult result =  new StreamResult(new File(file_path+""+xmlFileNm));
				    transformer.transform(source, result);
				    
				    xmlfile_name=xmlFileNm;
				    
				    queryString1="UPDATE FMS_ACCRUAL_DTL SET XML_SEQ=?,XML_NUM=? "
						    + "WHERE COMPANY_CD=? "
							+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') AND PROD_MONTH=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
							+ "AND CONTRACT_TYPE=? AND PLANT_SEQ=? AND BU_UNIT=? "
							+ "AND FINANCIAL_YEAR=? AND FREQ=? AND CARGO_NO=? ";
				    stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, xml_seq);
					stmt1.setString(2, xml_num);
					stmt1.setString(3, comp_cd);
					stmt1.setString(4, report_dt);
					stmt1.setString(5, prod_month);
					stmt1.setString(6, counterpty_cd);
					stmt1.setString(7, agmtno);
					stmt1.setString(8, contno);
					stmt1.setString(9, cont_type);
					stmt1.setString(10, plant_seq);
					stmt1.setString(11, buSeq);
					stmt1.setString(12, financialYear);
					stmt1.setString(13, bill_freq);
					stmt1.setString(14, cargo_no);
					stmt1.executeUpdate();
					
					stmt1.close();
				}
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getImbalanceQty(String counterparty_cd, String contract_type, String agmt_no, String cont_no, String from_dt, String to_dt, String bu_plant_seq)
	{
		String function_nm="getImbalanceQty()";
		try
		{
			double cumulative_imbalance=0;
			double ship_or_pay_percentage=0; 
			
			String cont_start_dt="";
			String mdq="";
			String sip_pay_percent="";
			String sip_pay_freq="";
			
			queryString="SELECT MDQ,MDQ_UNIT,SIP_PAY_RATE,SIP_PAY_FREQ,SIP_PAY_PERCENT,"
					+ "TO_CHAR(START_DT,'DD/MM/YYYY') "
					+ "FROM FMS_GTA_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONTRACT_TYPE=? AND AGMT_NO=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt_temp = conn.prepareStatement(queryString);
			stmt_temp.setString(1, comp_cd);
			stmt_temp.setString(2, counterparty_cd);
			stmt_temp.setString(3, cont_no);
			stmt_temp.setString(4, contract_type);
			stmt_temp.setString(5, agmt_no);
			rset_temp = stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				mdq=rset_temp.getString(1)==null?"":rset_temp.getString(1);
				sip_pay_freq=rset_temp.getString(4)==null?"":rset_temp.getString(4);
				sip_pay_percent=rset_temp.getString(5)==null?"":rset_temp.getString(5);
				cont_start_dt=rset_temp.getString(6)==null?"":rset_temp.getString(6);
			}
			rset_temp.close();
			stmt_temp.close();
			
			if(!sip_pay_percent.equals("")) 
			{
			  ship_or_pay_percentage=Double.parseDouble(sip_pay_percent); 
			}
			  
			if(mdq.equals(""))
			{ 
				mdq="0"; 
			}
			
			int count_day=utilDate.getDays(from_dt, cont_start_dt);
			if(count_day > 1)
			{
				//queryString="SELECT SUM(QTY_MMBTU-EXIT_QTY_MMBTU) "
				queryString="SELECT SUM(((NVL(QTY_MMBTU,0)-NVL(EXIT_QTY_MMBTU,0)) + NVL(ADJ_IMBALANCE,0))) "
		  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? "
						+ "AND BU_SEQ=? "
						+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt_temp = conn.prepareStatement(queryString);
				stmt_temp.setString(1, comp_cd);
				stmt_temp.setString(2, counterparty_cd);
				stmt_temp.setString(3, contract_type);
				stmt_temp.setString(4, cont_start_dt);
				stmt_temp.setString(5, from_dt);
				stmt_temp.setString(6, agmt_no);
				stmt_temp.setString(7, cont_no);
				stmt_temp.setString(8, bu_plant_seq);
				rset_temp = stmt_temp.executeQuery();
				if(rset_temp.next())
				{
					cumulative_imbalance=rset_temp.getDouble(1);
				}
				rset_temp.close();
				stmt_temp.close();
			}
			
			double tot_chargeable_overrun=0;
			double tot_positive_imbalance=0;
			double tot_negitive_imbalance=0;
			
			queryString1="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
					+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
					+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
					+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
			stmt_temp1 = conn.prepareStatement(queryString1);
			stmt_temp1.setString(1, from_dt);
			stmt_temp1.setString(2, to_dt);
			rset_temp1 = stmt_temp1.executeQuery();
			while(rset_temp1.next())
			{
				String gas_dt=rset_temp1.getString(1)==null?"":rset_temp1.getString(1);
				
				double nom_entry_qty=0;
				double nom_exit_qty=0;
				double sch_entry_qty=0;
				double sch_exit_qty=0;
				double alloc_entry_qty=0;
				double alloc_exit_qty=0;
				double var_mdq=0;
				double adj_imbalance=0;
				
				queryString="SELECT MDQ,MDQ_UNIT "
		  				+ "FROM FMS_DAILY_TRANSPORTER_NOM A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? "
						+ "AND BU_SEQ=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_TRANSPORTER_NOM B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt_temp = conn.prepareStatement(queryString);
				stmt_temp.setString(1, comp_cd);
				stmt_temp.setString(2, counterparty_cd);
				stmt_temp.setString(3, contract_type);
				stmt_temp.setString(4, gas_dt);
				stmt_temp.setString(5, agmt_no);
				stmt_temp.setString(6, cont_no);
				stmt_temp.setString(7, bu_plant_seq);
				rset_temp = stmt_temp.executeQuery();
				if(rset_temp.next())
				{
					var_mdq=rset_temp.getDouble(1);
				}
				else
				{
					var_mdq=Double.parseDouble(mdq);
				}
				rset_temp.close();
				stmt_temp.close();
				
				queryString="SELECT QTY_MMBTU,EXIT_QTY_MMBTU "
		  				+ "FROM FMS_DAILY_TRANSPORTER_SCH A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND AGMT_NO=? AND CONT_NO=? "
						+ "AND BU_SEQ=? "
						+ "AND SCH_REV_NO=(SELECT MAX(SCH_REV_NO) FROM FMS_DAILY_TRANSPORTER_SCH B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt_temp = conn.prepareStatement(queryString);
				stmt_temp.setString(1, comp_cd);
				stmt_temp.setString(2, counterparty_cd);
				stmt_temp.setString(3, contract_type);
				stmt_temp.setString(4, gas_dt);
				stmt_temp.setString(5, agmt_no);
				stmt_temp.setString(6, cont_no);
				stmt_temp.setString(7, bu_plant_seq);
				rset_temp = stmt_temp.executeQuery();
				if(rset_temp.next())
				{
					sch_entry_qty=rset_temp.getDouble(1);
					sch_exit_qty=rset_temp.getDouble(2);
				}
				rset_temp.close();
				stmt_temp.close();
				
				queryString="SELECT QTY_MMBTU,EXIT_QTY_MMBTU,ADJ_IMBALANCE "
		  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
		  				+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? "
						+ "AND BU_SEQ=? "
						+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt_temp = conn.prepareStatement(queryString);
				stmt_temp.setString(1, comp_cd);
				stmt_temp.setString(2, counterparty_cd);
				stmt_temp.setString(3, contract_type);
				stmt_temp.setString(4, gas_dt);
				stmt_temp.setString(5, agmt_no);
				stmt_temp.setString(6, cont_no);
				stmt_temp.setString(7, bu_plant_seq);
				rset_temp = stmt_temp.executeQuery();
				if(rset_temp.next())
				{
					alloc_entry_qty=rset_temp.getDouble(1);
					alloc_exit_qty=rset_temp.getDouble(2);
					adj_imbalance=rset_temp.getDouble(3);
				}
				rset_temp.close();
				stmt_temp.close();
				//DAILY IMBALANCE CALCULATION
				double daily_imbalance=alloc_entry_qty-alloc_exit_qty;
				
				//CUMULATIVE IMBALANCE CALCULATION
				cumulative_imbalance+=(daily_imbalance + adj_imbalance);
				
				/*double unauthorized_overrun=alloc_exit_qty-var_mdq;
				if(unauthorized_overrun<=0)
				{
					unauthorized_overrun=0;
				}*/
				
				//UNAUTHORIZED OVERRUN CALCULATION
				double unauthorized_overrun=0;
				if(sch_exit_qty > var_mdq)
				{
					if(alloc_exit_qty > sch_exit_qty)
					{
						unauthorized_overrun=alloc_exit_qty-sch_exit_qty;
					}
				}
				else
				{
					if(alloc_exit_qty > var_mdq)
					{
						unauthorized_overrun=alloc_exit_qty-var_mdq;
					}
				}
				
				//CHARGEABLE OVERRUN CALCULATION
				double chargeable_overrun = 0;//unauthorized_overrun - (var_mdq * 0.1);
				/*
				 * if(chargeable_overrun<=0) { chargeable_overrun=0; }
				 */
				
				if(sch_exit_qty > var_mdq)
				{
					if(alloc_exit_qty >  1.10 * sch_exit_qty)
					{
						chargeable_overrun=alloc_exit_qty-(1.10 *sch_exit_qty);
					}
				}
				else
				{
					if(alloc_exit_qty >  1.10 * sch_exit_qty)
					{
						chargeable_overrun=alloc_exit_qty-(var_mdq + (0.1 * sch_exit_qty));
					}
					else if(alloc_exit_qty > 1.10 * var_mdq)
					{
						chargeable_overrun=alloc_exit_qty-(1.10 * var_mdq);
					}
				}
				
				if(chargeable_overrun<=0) 
				{ 
					chargeable_overrun=0; 
				}
				
				//POSITIVE IMBALANCE CALCULATION
				double positive_imbalance=cumulative_imbalance - (var_mdq * 0.1);
				if(positive_imbalance<=0)
				{
					positive_imbalance=0;
				}
				
				//NEGITIVE IMBALANCE CALCULATION
				double negitive_imbalance=cumulative_imbalance + (var_mdq * 0.05);
				if(negitive_imbalance>=0)
				{
					negitive_imbalance=0;
				}
				else
				{
					negitive_imbalance=negitive_imbalance*(-1);
				}
				
				tot_positive_imbalance+=positive_imbalance;
				tot_negitive_imbalance+=negitive_imbalance;
				tot_chargeable_overrun+=chargeable_overrun;
			}
			rset_temp1.close();
			stmt_temp1.close();
			
			positive_imbalance_qty=nf3.format(tot_positive_imbalance);
			negative_imbalance_qty=nf3.format(tot_negitive_imbalance);
			unauthorized_overrun_qty=nf3.format(tot_chargeable_overrun);
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public Double getAccrualTransmissionQty(String counterparty_cd, String contract_type, String agmt_no, String cont_no, String from_dt, String to_dt, String bu_plant_seq)
	{
		String function_nm="getAccrualTransmissionQty()";
		double qty=0;
		try
		{
			double ship_or_pay_percentage=0; 
			
			String mdq="";
			String sip_pay_percent="";
			String sip_pay_freq="";
			
			queryString="SELECT MDQ,MDQ_UNIT,SIP_PAY_RATE,SIP_PAY_FREQ,SIP_PAY_PERCENT "
					+ "FROM FMS_GTA_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONTRACT_TYPE=? AND AGMT_NO=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt_temp=conn.prepareStatement(queryString);
			stmt_temp.setString(1, comp_cd);
			stmt_temp.setString(2, counterparty_cd);
			stmt_temp.setString(3, cont_no);
			stmt_temp.setString(4, contract_type);
			stmt_temp.setString(5, agmt_no);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				mdq=rset_temp.getString(1)==null?"":rset_temp.getString(1);
				sip_pay_freq=rset_temp.getString(4)==null?"":rset_temp.getString(4);
				sip_pay_percent=rset_temp.getString(5)==null?"":rset_temp.getString(5);
			}
			rset_temp.close();
			stmt_temp.close();
			
			if(!sip_pay_percent.equals("")) 
			{
			  ship_or_pay_percentage=Double.parseDouble(sip_pay_percent); 
			}
			  
			if(mdq.equals(""))
			{ 
				mdq="0"; 
			}
			
			queryString1="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
					+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
					+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
					+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
			stmt_temp1=conn.prepareStatement(queryString1);
			stmt_temp1.setString(1, from_dt);
			stmt_temp1.setString(2, to_dt);
			rset_temp1=stmt_temp1.executeQuery();
			while(rset_temp1.next())
			{
				String gas_dt=rset_temp1.getString(1)==null?"":rset_temp1.getString(1);
				
				double alloc_entry_qty=0;
				double alloc_exit_qty=0;
				double var_mdq=0;
				
				queryString="SELECT MDQ,MDQ_UNIT "
		  				+ "FROM FMS_DAILY_TRANSPORTER_NOM A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? "
						+ "AND BU_SEQ=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_TRANSPORTER_NOM B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt_temp2=conn.prepareStatement(queryString);
				stmt_temp2.setString(1, comp_cd);
				stmt_temp2.setString(2, counterparty_cd);
				stmt_temp2.setString(3, contract_type);
				stmt_temp2.setString(4, gas_dt);
				stmt_temp2.setString(5, agmt_no);
				stmt_temp2.setString(6, cont_no);
				stmt_temp2.setString(7, bu_plant_seq);
				rset_temp2=stmt_temp2.executeQuery();
				if(rset_temp2.next())
				{
					var_mdq=rset_temp2.getDouble(1);
				}
				else
				{
					var_mdq=Double.parseDouble(mdq);
				}
				rset_temp2.close();
				stmt_temp2.close();
				
				queryString1="SELECT COALESCE(ALLOCATION, SCHEDULING, NOMINATION, MDCQ, 0) "
						+ "FROM "
						+ "(SELECT (SELECT SUM(EXIT_QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
		  				+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? AND BU_SEQ=? "
						+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID)) ALLOCATION, "
						+ "(SELECT SUM(EXIT_QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_TRANSPORTER_SCH A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
		  				+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? AND BU_SEQ=? "
						+ "AND SCH_REV_NO=(SELECT MAX(SCH_REV_NO) FROM FMS_DAILY_TRANSPORTER_SCH B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID)) SCHEDULING, "
						+ "(SELECT SUM(EXIT_QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_TRANSPORTER_NOM A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
		  				+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? AND BU_SEQ=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_TRANSPORTER_NOM B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID)) NOMINATION, "
						+ "(NVL("+var_mdq+",0)) MDCQ FROM DUAL)";
				 
				/*queryString1="SELECT QTY_MMBTU,EXIT_QTY_MMBTU "
		  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
		  				+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? "
						+ "AND BU_SEQ=? "
						+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";*/
				stmt_temp3=conn.prepareStatement(queryString1);
				stmt_temp3.setString(1, comp_cd);
				stmt_temp3.setString(2, counterparty_cd);
				stmt_temp3.setString(3, contract_type);
				stmt_temp3.setString(4, gas_dt);
				stmt_temp3.setString(5, agmt_no);
				stmt_temp3.setString(6, cont_no);
				stmt_temp3.setString(7, bu_plant_seq);
				stmt_temp3.setString(8, comp_cd);
				stmt_temp3.setString(9, counterparty_cd);
				stmt_temp3.setString(10, contract_type);
				stmt_temp3.setString(11, gas_dt);
				stmt_temp3.setString(12, agmt_no);
				stmt_temp3.setString(13, cont_no);
				stmt_temp3.setString(14, bu_plant_seq);
				stmt_temp3.setString(15, comp_cd);
				stmt_temp3.setString(16, counterparty_cd);
				stmt_temp3.setString(17, contract_type);
				stmt_temp3.setString(18, gas_dt);
				stmt_temp3.setString(19, agmt_no);
				stmt_temp3.setString(20, cont_no);
				stmt_temp3.setString(21, bu_plant_seq);
				rset_temp3=stmt_temp3.executeQuery();
				if(rset_temp3.next())
				{
					//alloc_entry_qty=rset1.getDouble(1);
					alloc_exit_qty=rset_temp3.getDouble(1);
				}
				rset_temp3.close();
				stmt_temp3.close();
				
				//TRANSMISSION MMBTU CALCULATION
				double transmission_qty=0;
				
				if(sip_pay_freq.equals("D")) //SHIP OR PAY = DAILY
				{
					double var_mdq_with_ship_or_pay= var_mdq * (ship_or_pay_percentage / 100);
					
					if(alloc_exit_qty >= var_mdq_with_ship_or_pay)
					{
						transmission_qty=alloc_exit_qty;
					}
					else
					{
						transmission_qty=var_mdq_with_ship_or_pay;
					}
				}
				else //SHIP OR PAY = MONTHLY
				{
					transmission_qty=alloc_exit_qty;
				}
				
				qty+=transmission_qty;
			}
			rset_temp1.close();
			stmt_temp1.close();
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return qty;
	}	
	
	public Double getAccrualDeficiencyQty(String counterparty_cd, String contract_type, String agmt_no, String cont_no, String from_dt, String to_dt, String bu_plant_seq)
	{
		String function_nm="getAccrualDeficiencyQty()";
		double qty=0;
		try
		{
			double ship_or_pay_percentage=0; 
			
			String mdq="";
			String sip_pay_percent="";
			String sip_pay_freq="";
			
			queryString="SELECT MDQ,MDQ_UNIT,SIP_PAY_RATE,SIP_PAY_FREQ,SIP_PAY_PERCENT "
					+ "FROM FMS_GTA_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONTRACT_TYPE=? AND AGMT_NO=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt_temp=conn.prepareStatement(queryString);
			stmt_temp.setString(1, comp_cd);
			stmt_temp.setString(2, counterparty_cd);
			stmt_temp.setString(3, cont_no);
			stmt_temp.setString(4, contract_type);
			stmt_temp.setString(5, agmt_no);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				mdq=rset_temp.getString(1)==null?"":rset_temp.getString(1);
				sip_pay_freq=rset_temp.getString(4)==null?"":rset_temp.getString(4);
				sip_pay_percent=rset_temp.getString(5)==null?"":rset_temp.getString(5);
			}
			rset_temp.close();
			stmt_temp.close();
			
			if(!sip_pay_percent.equals("")) 
			{
			  ship_or_pay_percentage=Double.parseDouble(sip_pay_percent); 
			}
			  
			if(mdq.equals(""))
			{ 
				mdq="0"; 
			}
			
			double alloc_entry_qty=0;
			double alloc_exit_qty=0;
			double var_mdq=0;
			
			queryString1="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
					+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
					+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
					+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
			stmt_temp1=conn.prepareStatement(queryString1);
			stmt_temp1.setString(1, from_dt);
			stmt_temp1.setString(2, to_dt);
			rset_temp1=stmt_temp1.executeQuery();
			while(rset_temp1.next())
			{
				String gas_dt=rset_temp1.getString(1)==null?"":rset_temp1.getString(1);
				
				queryString="SELECT MDQ,MDQ_UNIT "
		  				+ "FROM FMS_DAILY_TRANSPORTER_NOM A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? "
						+ "AND BU_SEQ=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_TRANSPORTER_NOM B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt_temp=conn.prepareStatement(queryString);
				stmt_temp.setString(1, comp_cd);
				stmt_temp.setString(2, counterparty_cd);
				stmt_temp.setString(3, contract_type);
				stmt_temp.setString(4, gas_dt);
				stmt_temp.setString(5, agmt_no);
				stmt_temp.setString(6, cont_no);
				stmt_temp.setString(7, bu_plant_seq);
				rset_temp=stmt_temp.executeQuery();
				if(rset_temp.next())
				{
					var_mdq+=rset_temp.getDouble(1);
				}
				else
				{
					var_mdq+=Double.parseDouble(mdq);
				}
				rset_temp.close();
				stmt_temp.close();
				
				queryString1="SELECT COALESCE(ALLOCATION, SCHEDULING, NOMINATION, MDCQ, 0) "
						+ "FROM "
						+ "(SELECT (SELECT SUM(EXIT_QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
		  				+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? AND BU_SEQ=? "
						+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID)) ALLOCATION, "
						+ "(SELECT SUM(EXIT_QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_TRANSPORTER_SCH A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
		  				+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? AND BU_SEQ=? "
						+ "AND SCH_REV_NO=(SELECT MAX(SCH_REV_NO) FROM FMS_DAILY_TRANSPORTER_SCH B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID)) SCHEDULING, "
						+ "(SELECT SUM(EXIT_QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_TRANSPORTER_NOM A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
		  				+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? AND BU_SEQ=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_TRANSPORTER_NOM B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID)) NOMINATION, "
						+ "(NVL("+var_mdq+",0)) MDCQ FROM DUAL)";
				stmt_temp=conn.prepareStatement(queryString1);
				stmt_temp.setString(1, comp_cd);
				stmt_temp.setString(2, counterparty_cd);
				stmt_temp.setString(3, contract_type);
				stmt_temp.setString(4, gas_dt);
				stmt_temp.setString(5, agmt_no);
				stmt_temp.setString(6, cont_no);
				stmt_temp.setString(7, bu_plant_seq);
				stmt_temp.setString(8, comp_cd);
				stmt_temp.setString(9, counterparty_cd);
				stmt_temp.setString(10, contract_type);
				stmt_temp.setString(11, gas_dt);
				stmt_temp.setString(12, agmt_no);
				stmt_temp.setString(13, cont_no);
				stmt_temp.setString(14, bu_plant_seq);
				stmt_temp.setString(15, comp_cd);
				stmt_temp.setString(16, counterparty_cd);
				stmt_temp.setString(17, contract_type);
				stmt_temp.setString(18, gas_dt);
				stmt_temp.setString(19, agmt_no);
				stmt_temp.setString(20, cont_no);
				stmt_temp.setString(21, bu_plant_seq);
				rset_temp=stmt_temp.executeQuery();
				if(rset_temp.next())
				{
					//alloc_entry_qty=rset_temp.getDouble(1);
					alloc_exit_qty+=rset_temp.getDouble(1);
				}
				rset_temp.close();
				stmt_temp.close();
			}
			rset_temp1.close();
			stmt_temp1.close();
			
			/*queryString="SELECT SUM(QTY_MMBTU),SUM(EXIT_QTY_MMBTU) "
	  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
	  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
	  				+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND AGMT_NO=? AND CONT_NO=? "
					+ "AND BU_SEQ=? "
					+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
					+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
			stmt_temp=conn.prepareStatement(queryString);
			stmt_temp.setString(1, comp_cd);
			stmt_temp.setString(2, counterparty_cd);
			stmt_temp.setString(3, contract_type);
			stmt_temp.setString(4, from_dt);
			stmt_temp.setString(5, to_dt);
			stmt_temp.setString(6, agmt_no);
			stmt_temp.setString(7, cont_no);
			stmt_temp.setString(8, bu_plant_seq);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				alloc_entry_qty=rset_temp.getDouble(1);
				alloc_exit_qty=rset_temp.getDouble(2);
			}
			rset_temp.close();
			stmt_temp.close();*/
			
			double ship_or_pay_qty= var_mdq * (ship_or_pay_percentage/100);
			
			qty = ship_or_pay_qty - alloc_exit_qty;
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return qty;
	}

	public void getInvoiceType()
	{
		String function_nm="getInvoiceType()";
		try
		{
			VINVOICE_TITLE.add("GTA Transmission Charge Remittance");
			VINVOICE_TITLE.add("GTA Imbalance Charge Remittance");
			VINVOICE_TITLE.add("Parking Charge Remittance");
			
			VINVOICE_TYPE.add("TC");
			VINVOICE_TYPE.add("IC");
			VINVOICE_TYPE.add("PC");
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void GtaFreezAccrualData()
	{
		String function_nm="GtaFreezAccrualData()";
		try
		{
			int count=0;
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_GTA_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, report_dt);
			rset = stmt.executeQuery();
			if(rset.next())
			{
				count=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			if(count>0)
			{
				queryString="DELETE FROM FMS_GTA_ACCRUAL_DTL "
						+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, report_dt);
				stmt.executeUpdate();
				
				stmt.close();
			}
			for(int i=0;i<VCOUNTERPTY_CD.size();i++)
			{		
				String prod_month="01/"+VPRODUCTION_MONTH.elementAt(i);
				
				queryString="INSERT INTO FMS_GTA_ACCRUAL_DTL(COMPANY_CD,REPORT_DT,COUNTERPARTY_CD,FINANCIAL_YEAR,"
						+ "AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
						+ "CONTRACT_TYPE,BU_UNIT,TRANS_BU_UNIT,CASH_FLOW,"
						+ "INVOICE_DUE_DT,PROD_MONTH,FREQ,"
						+ "PERIOD_START_DT,PERIOD_END_DT,"
						+ "ACCRUAL_QTY,ACCRUAL_AMT,RATE_IN,INVOICE_RAISED_IN,"
						+ "GROSS_AMT,CONT_REF_NO,CONT_START_DT,CONT_END_DT,"
						+ "ENT_BY,ENT_DT,INV_COMPONENT) "
						+ "VALUES(?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
						+ "?,?,?,?,"
						+ "?,?,?,?,"
						+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,"
						+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
						+ "?,?,?,?,"
						+ "?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
						+ "?,SYSDATE,?)";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, report_dt);
				stmt.setString(3, ""+VCOUNTERPTY_CD.elementAt(i));
				stmt.setString(4, ""+VFINANCIAL_YEAR.elementAt(i));
				stmt.setString(5, ""+VAGMT_NO.elementAt(i));
				stmt.setString(6, ""+VAGMT_REV_NO.elementAt(i));
				stmt.setString(7, ""+VCONT_NO.elementAt(i));
				stmt.setString(8, ""+VCONT_REV_NO.elementAt(i));
				stmt.setString(9, ""+VCONTRACT_TYPE.elementAt(i));
				stmt.setString(10, ""+VBU_PLANT_SEQ.elementAt(i));
				stmt.setString(11, ""+VTRANS_BU_SEQ.elementAt(i));
				stmt.setString(12, ""+VCASH_FLOW.elementAt(i));
				stmt.setString(13, ""+VINVOICE_DUE_DT.elementAt(i));
				stmt.setString(14, ""+prod_month);
				stmt.setString(15, ""+VBILLING_FREQ_FLAG.elementAt(i));
				stmt.setString(16, ""+VPERIOD_START_DT.elementAt(i));
				stmt.setString(17, ""+VPERIOD_END_DT.elementAt(i));
				stmt.setString(18, ""+VACCRUAL_QTY.elementAt(i));
				stmt.setString(19, ""+VACCRUAL_AMT.elementAt(i));
				stmt.setString(20, "");
				stmt.setString(21, ""+VINVOICE_RAISED_IN.elementAt(i));
				stmt.setString(22, ""+VGROSS_AMT.elementAt(i));
				stmt.setString(23, ""+VCONT_REF_NO.elementAt(i));
				stmt.setString(24, ""+VSTART_DT.elementAt(i));
				stmt.setString(25, ""+VEND_DT.elementAt(i));
				stmt.setString(26, ""+emp_cd);
				stmt.setString(27, ""+VINV_COMPONENTS.elementAt(i));
				stmt.executeUpdate();
				
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void SalesFreezAccrualData()
	{
		String function_nm="SalesFreezAccrualData()";
		try
		{
			int count=0;
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, report_dt);
			rset = stmt.executeQuery();
			if(rset.next())
			{
				count=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			if(count>0)
			{
				queryString="DELETE FROM FMS_ACCRUAL_DTL "
						+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, report_dt);
				stmt.executeUpdate();
				
				stmt.close();
			}
			for(int i=0;i<VCOUNTERPTY_CD.size();i++)
			{		
				String prod_month="01/"+VPRODUCTION_MONTH.elementAt(i);
				
				queryString="INSERT INTO FMS_ACCRUAL_DTL(COMPANY_CD,REPORT_DT,COUNTERPARTY_CD,FINANCIAL_YEAR,"
						+ "AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
						+ "CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,PLANT_SEQ,"
						+ "INVOICE_DUE_DT,PROD_MONTH,FREQ,"
						+ "PERIOD_START_DT,PERIOD_END_DT,"
						+ "ACCRUAL_QTY,ACCRUAL_AMT,RATE_IN,INVOICE_RAISED_IN,"
						+ "GROSS_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,"
						+ "CONT_REF_NO,CASH_FLOW,CONT_START_DT,CONT_END_DT,"
						+ "AGMT_BASE,ENT_BY,ENT_DT,CARGO_NO) "
						+ "VALUES(?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,?,"
						+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
						+ "?,?,?,?,"
						+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
						+ "?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
						+ "?,?,SYSDATE,?)";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, report_dt);
				stmt.setString(3, ""+VCOUNTERPTY_CD.elementAt(i));
				stmt.setString(4, ""+VFINANCIAL_YEAR.elementAt(i));
				stmt.setString(5, ""+VAGMT_NO.elementAt(i));
				stmt.setString(6, ""+VAGMT_REV_NO.elementAt(i));
				stmt.setString(7, ""+VCONT_NO.elementAt(i));
				stmt.setString(8, ""+VCONT_REV_NO.elementAt(i));
				stmt.setString(9, ""+VCONTRACT_TYPE.elementAt(i));
				stmt.setString(10, ""+VBU_PLANT_SEQ.elementAt(i));
				
				stmt.setString(11, ""+VBU_STATE_TIN.elementAt(i));
				stmt.setString(12, ""+VPLANT_SEQ.elementAt(i));
				stmt.setString(13, ""+VINVOICE_DUE_DT.elementAt(i));
				stmt.setString(14, ""+prod_month);
				stmt.setString(15, ""+VBILLING_FREQ_FLAG.elementAt(i));
				stmt.setString(16, ""+VPERIOD_START_DT.elementAt(i));
				stmt.setString(17, ""+VPERIOD_END_DT.elementAt(i));
				stmt.setString(18, ""+VACCRUAL_QTY.elementAt(i));
				stmt.setString(19, ""+VACCRUAL_AMT.elementAt(i));
				stmt.setString(20, ""+VSALES_PRICE_CD.elementAt(i));
				stmt.setString(21, ""+VINVOICE_RAISED_IN.elementAt(i));
				stmt.setString(22, ""+VGROSS_AMT.elementAt(i));
				stmt.setString(23, ""+VEXCHNG_RATE_CD.elementAt(i));
				stmt.setString(24, ""+VEXCHNG_RATE_DT.elementAt(i));
				stmt.setString(25, ""+VEXCHNG_RATE.elementAt(i));
				
				stmt.setString(26, ""+VCONT_REF_NO.elementAt(i));
				stmt.setString(27, ""+VCASH_FLOW.elementAt(i));
				stmt.setString(28, ""+VSTART_DT.elementAt(i));
				stmt.setString(29, ""+VEND_DT.elementAt(i));
				stmt.setString(30, ""+VAGMT_BASE.elementAt(i));
				stmt.setString(31, ""+emp_cd);
				stmt.setString(32, ""+VCARGO_NO.elementAt(i));
				stmt.executeUpdate();
				
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getGxTransactionAccrualList()
	{
		String function_nm="getGxTransactionAccrualList()";
		try
		{
			for(int i=0;i<VSELL_BUY_NM.size();i++)
			{
				String sellBuy=""+VSELL_BUY_FLAG.elementAt(i);
				String inv_title="";
				if(sellBuy.equals("S"))
				{
					queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
							+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_REF_NO,CONTRACT_TYPE,"
							+ "TRADE_REF_NO,AGMT_BASE,FCC_FLAG,TCQ,TXN_CHARGE,TXN_UNIT "
							+ "FROM FMS_SUPPLY_CONT_MST A "
							+ "WHERE COMPANY_CD=? "
							+ "AND CONTRACT_TYPE IN ('X', 'W') AND FCC_FLAG='Y' "
							+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
							+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				}
				else if(sellBuy.equals("B"))
				{
					queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
							+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_REF_NO,CONTRACT_TYPE,"
							+ "TRADE_REF_NO,AGMT_BASE,FCC_FLAG,TCQ,TXN_CHARGE,TXN_UNIT "
							+ "FROM FMS_TRADER_CONT_MST A "
							+ "WHERE COMPANY_CD=? "
							+ "AND CONTRACT_TYPE='I' AND FCC_FLAG='Y' "
							+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
							+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				}
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, to_dt);
				stmt.setString(3, from_dt);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					String start_dt=rset.getString(7)==null?"":rset.getString(7);
					String end_dt=rset.getString(8)==null?"":rset.getString(8);
					String cont_nm=rset.getString(9)==null?"":rset.getString(9);
					String contRef=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					String tradeRef=rset.getString(12)==null?"":rset.getString(12);
					String deal_no="";
					
					deal_no=cont_type+""+contno;
					deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, "");
					//if(cont_type.equals("X") || cont_type.equals("I"))
					{
						contRef=tradeRef;
					}
					String agmt_base=rset.getString(13)==null?"":rset.getString(13);
					String fcc=rset.getString(14)==null?"":rset.getString(14);
					
					String tcq=nf.format(rset.getDouble(15));
					String txn_charge = nf.format(rset.getDouble(16));
					String txn_unit = rset.getString(17)==null?"1":rset.getString(17);
					
					if(sellBuy.equals("B"))
					{
						queryString1="SELECT GX_COUNTERPARTY_CD,GX_BU_SEQ_NO "
								+ "FROM FMS_TRADER_CONT_GX_BU "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
					}
					else
					{
						queryString1="SELECT GX_COUNTERPARTY_CD,GX_BU_SEQ_NO "
								+ "FROM FMS_SUPPLY_CONT_GX_BU "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
					}
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, countpty_cd);
					stmt1.setString(3, contno);
					stmt1.setString(4, contrev);
					stmt1.setString(5, agmtno);
					stmt1.setString(6, agmtrev);
					stmt1.setString(7, cont_type);
					rset1 = stmt1.executeQuery();
					while(rset1.next())
					{
						String gx_countpty_cd = rset1.getString(1)==null?"":rset1.getString(1);
						String gx_bu_plant_seq = rset1.getString(2)==null?"":rset1.getString(2);
						String gx_bu_plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,gx_countpty_cd, own_cd, gx_bu_plant_seq, "G");
						
						String gx_countpty_abbr = utilBean.getGasExchangeAbbr(conn,gx_countpty_cd); 
						
						if(sellBuy.equals("B"))
						{
							queryString2="SELECT PLANT_SEQ_NO "
									+ "FROM FMS_TRADER_CONT_BU "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
						}
						else
						{
							queryString2="SELECT PLANT_SEQ_NO "
									+ "FROM FMS_SUPPLY_CONT_BU "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
						}
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, countpty_cd);
						stmt2.setString(3, contno);
						stmt2.setString(4, contrev);
						stmt2.setString(5, agmtno);
						stmt2.setString(6, agmtrev);
						stmt2.setString(7, cont_type);
						rset2 = stmt2.executeQuery();
						while(rset2.next())
						{
							String bu_plant_seq = rset2.getString(1)==null?"":rset2.getString(1);
							String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
							
							String invType="TX";
							String fin_yr = utilDate.getFinancialYear(start_dt);
							
							int isInvExist=0;
							
							queryString3="SELECT COUNT(*) "
									+ "FROM FMS_GX_TXN_SG_INV_MST "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND AGMT_NO=? AND GX_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
									+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? AND PDF_INV_DTL IS NOT NULL";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, own_cd);
							stmt3.setString(2, countpty_cd);
							stmt3.setString(3, contno);
							stmt3.setString(4, agmtno);
							stmt3.setString(5, gx_bu_plant_seq);
							stmt3.setString(6, bu_plant_seq);
							stmt3.setString(7, cont_type);
							stmt3.setString(8, gx_countpty_cd);
							stmt3.setString(9, invType);
							rset3 = stmt3.executeQuery();
							if(rset3.next())
							{
								isInvExist+=rset3.getInt(1);
							}
							rset3.close();
							stmt3.close();
							
							queryString3="SELECT COUNT(*) "
									+ "FROM FMS_GX_TXN_PG_INV_MST "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND AGMT_NO=? AND GX_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
									+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? AND PDF_INV_DTL IS NOT NULL";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, own_cd);
							stmt3.setString(2, countpty_cd);
							stmt3.setString(3, contno);
							stmt3.setString(4, agmtno);
							stmt3.setString(5, gx_bu_plant_seq);
							stmt3.setString(6, bu_plant_seq);
							stmt3.setString(7, cont_type);
							stmt3.setString(8, gx_countpty_cd);
							stmt3.setString(9, invType);
							rset3 = stmt3.executeQuery();
							if(rset3.next())
							{
								isInvExist+=rset3.getInt(1);
							}
							rset3.close();
							stmt3.close();
							
							double accrual_amt=Double.parseDouble(tcq) * Double.parseDouble(txn_charge);
							
							if(isInvExist==0 && accrual_amt > 0)
							{
								VCOUNTERPTY_CD.add(countpty_cd);
								VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
								VCOUNTERPTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
								VGX_COUNTERPTY_CD.add(gx_countpty_cd);
								VGX_COUNTERPTY_ABBR.add(gx_countpty_abbr);
								VAGMT_NO.add(agmtno);
								VAGMT_REV_NO.add(agmtrev);
								VCONT_NO.add(contno);
								VCONT_REV_NO.add(contrev);
								VSTART_DT.add(start_dt);
								VEND_DT.add(end_dt);
								VCONT_NAME.add(cont_nm);
								VCONT_REF_NO.add(contRef);
								VCONTRACT_TYPE.add(cont_type);
								VDIS_CONT_MAPPING.add(deal_no);
								
								VGX_BU_PLANT_SEQ.add(gx_bu_plant_seq);
								VGX_BU_PLANT_ABBR.add(gx_bu_plant_abbr);
								VBU_PLANT_SEQ.add(bu_plant_seq);
								VBU_PLANT_ABBR.add(bu_plant_abbr);
								VINVOICE_TYPE.add(invType);
								VFINANCIAL_YEAR.add(fin_yr);
								VPRODUCTION_MONTH.add(start_dt.substring(3,start_dt.length()));
								
								VTXN_CHARGE_UNIT.add(txn_unit);
								VINVOICE_DUE_DT.add(""+utilDate.getDate(start_dt, "2"));
								VACCRUAL_QTY.add(tcq);
								
								VACCRUAL_AMT.add(nf.format(accrual_amt));
								VGROSS_AMT.add(nf.format(accrual_amt));
								VINVOICE_RAISED_IN.add("1");
								VBUY_SELL.add(VSELL_BUY_NM.elementAt(i));
							}
						}
						rset2.close();
						stmt2.close();
					}
					rset1.close();
					stmt1.close();
				}
				rset.close();
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void GxFreezAccrualData()
	{
		String function_nm="GxFreezAccrualData()";
		try
		{
			int count=0;
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_GX_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, report_dt);
			rset = stmt.executeQuery();
			if(rset.next())
			{
				count=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			if(count>0)
			{
				queryString="DELETE FROM FMS_GX_ACCRUAL_DTL "
						+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, report_dt);
				stmt.executeUpdate();
			}
			for(int i=0;i<VCOUNTERPTY_CD.size();i++)
			{		
				String prod_month="01/"+VPRODUCTION_MONTH.elementAt(i);
				
				queryString="INSERT INTO FMS_GX_ACCRUAL_DTL(COMPANY_CD,REPORT_DT,COUNTERPARTY_CD,FINANCIAL_YEAR,"
						+ "AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
						+ "CONTRACT_TYPE,BU_UNIT,GX_COUNTERPARTY_CD,GX_BU_UNIT,"
						+ "INVOICE_DUE_DT,PROD_MONTH,"
						+ "ACCRUAL_QTY,ACCRUAL_AMT,RATE_IN,INVOICE_RAISED_IN,"
						+ "GROSS_AMT,CONT_REF_NO,CONT_START_DT,CONT_END_DT,"
						+ "ENT_BY,ENT_DT) "
						+ "VALUES(?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
						+ "?,?,?,?,"
						+ "?,?,?,?,"
						+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
						+ "?,?,?,?,"
						+ "?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
						+ "?,SYSDATE)";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, report_dt);
				stmt.setString(3, ""+VCOUNTERPTY_CD.elementAt(i));
				stmt.setString(4, ""+VFINANCIAL_YEAR.elementAt(i));
				stmt.setString(5, ""+VAGMT_NO.elementAt(i));
				stmt.setString(6, ""+VAGMT_REV_NO.elementAt(i));
				stmt.setString(7, ""+VCONT_NO.elementAt(i));
				stmt.setString(8, ""+VCONT_REV_NO.elementAt(i));
				stmt.setString(9, ""+VCONTRACT_TYPE.elementAt(i));
				stmt.setString(10, ""+VBU_PLANT_SEQ.elementAt(i));
				
				stmt.setString(11, ""+VGX_COUNTERPTY_CD.elementAt(i));
				stmt.setString(12, ""+VGX_BU_PLANT_SEQ.elementAt(i));
				stmt.setString(13, ""+VINVOICE_DUE_DT.elementAt(i));
				stmt.setString(14, ""+prod_month);
				stmt.setString(15, ""+VACCRUAL_QTY.elementAt(i));
				stmt.setString(16, ""+VACCRUAL_AMT.elementAt(i));
				stmt.setString(17, ""+VTXN_CHARGE_UNIT.elementAt(i));
				stmt.setString(18, ""+VINVOICE_RAISED_IN.elementAt(i));
				stmt.setString(19, ""+VGROSS_AMT.elementAt(i));
				stmt.setString(20, ""+VCONT_REF_NO.elementAt(i));
				stmt.setString(21, ""+VSTART_DT.elementAt(i));
				stmt.setString(22, ""+VEND_DT.elementAt(i));
				stmt.setString(23, ""+emp_cd);
				stmt.executeUpdate();
				
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void generateGXAccrualXML()
	{
		String function_nm="generateGXAccrualXML()";
		try
		{
			String sysdate=utilDate.getSysdate();
			String sysdateWithTime=utilDate.getSysdateWithTime24hr();
			String xml_sysdate="";
			String postingMonth="";
			String[] split=sysdate.split("/");
			xml_sysdate=split[2]+""+split[1]+""+split[0];
			postingMonth=split[2]+""+split[1];
			
			String[] splitSys = sysdateWithTime.split(" ");
			String date_timestamp=xml_sysdate+" "+splitSys[1];
			
			String fms_MessageId = "";
			String accountingPeriodMonth="";
			String accountingPeriodYear="";
			
			String documentDate="";
			if(!report_dt.equals(""))
			{
				String[] temp_split=report_dt.split("/");
				accountingPeriodMonth=temp_split[1];
				accountingPeriodYear=temp_split[2];	
				
				documentDate=temp_split[2]+""+temp_split[1]+""+temp_split[0];
			}
			
			String invoice_prefix=utilBean.getInvoicePrefix(conn,comp_cd);
			
			queryString="SELECT DISTINCT COUNTERPARTY_CD,AGMT_NO,CONT_NO,CONTRACT_TYPE,GX_BU_UNIT,BU_UNIT,"
					+ "FINANCIAL_YEAR,GX_COUNTERPARTY_CD "
					+ "FROM FMS_GX_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? "
					+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY')";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, report_dt);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				//String prod_month=rset.getString(1)==null?"":rset.getString(1);
				String counterpty_cd=rset.getString(1)==null?"":rset.getString(1);
				String agmtno=rset.getString(2)==null?"":rset.getString(2);
				String contno=rset.getString(3)==null?"":rset.getString(3);
				String cont_type=rset.getString(4)==null?"":rset.getString(4);
				String gx_bu_seq=rset.getString(5)==null?"":rset.getString(5);
				String buSeq=rset.getString(6)==null?"":rset.getString(6);
				String financialYear=rset.getString(7)==null?"":rset.getString(7);
				String gx_counterptyCd=rset.getString(8)==null?"":rset.getString(8);
				
				String buStateNm="";
				String buAbbr="";
				queryString1 = "SELECT PLANT_STATE,PLANT_ABBR "
						+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
						+ "WHERE COUNTERPARTY_CD=? AND ENTITY='B' AND COMPANY_CD=? AND SEQ_NO=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, comp_cd);
				stmt1.setString(3, buSeq);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					buStateNm=rset1.getString(1)==null?"":rset1.getString(1);
					buAbbr=rset1.getString(2)==null?"":rset1.getString(2);
				}
				rset1.close();
				stmt1.close();
				
				docHeaderText=buStateNm+"/"+buAbbr+" - BU";
				
				String entityType="";
				if(cont_type.equals("I"))
				{
					entityType="T";
				}
				else
				{
					entityType="C";
				}
				String xml_seq="1";
				String xml_num="";
				queryString1="SELECT NVL(MAX(XML_SEQ),0) "
						+ "FROM FMS_GX_ACCRUAL_DTL "
						+ "WHERE COMPANY_CD=? "
						+ "AND TO_DATE(TO_CHAR(REPORT_DT,'MM/YYYY'),'MM/YYYY')=TO_DATE(?,'MM/YYYY') ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, accountingPeriodMonth+"/"+accountingPeriodYear);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					xml_seq=""+(rset1.getInt(1)+1);
				}
				rset1.close();
				stmt1.close();
				
				xml_num=invoice_prefix+"G"+utilBean.PrePaddingZero(xml_seq, 4)+"/"+accountingPeriodMonth+""+accountingPeriodYear;
				
				fms_MessageId=xml_num.replaceAll("/", "-");
				
				DocumentBuilderFactory docFactory = xmlUtil.dcoumentBuilderFactory();
			    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
			    Document doc = docBuilder.newDocument();
			    
			    //root fmsng
			    Element fmsng = doc.createElement("EmsSAPGeneralLedgerMessage");
			    doc.appendChild(fmsng);
		
			    //root elements
			    Element Header = doc.createElement("Header");
			    fmsng.appendChild(Header);
			    Element Subledger = doc.createElement("Subledger");
			    fmsng.appendChild(Subledger);
			    
			    //Header elements
			    Element MessageId = doc.createElement("MessageId");
			    Element Scope = doc.createElement("Scope");
			    Element DateTimeStamp = doc.createElement("DateTimeStamp");
			    Element DataSource = doc.createElement("DataSource");
			    
			    Header.appendChild(MessageId);
			    Header.appendChild(Scope);
			    Header.appendChild(DateTimeStamp);
			    Header.appendChild(DataSource);
			    
			    MessageId.appendChild(doc.createTextNode(fms_MessageId));
			    Scope.appendChild(doc.createTextNode(""+utilBean.getCompanySAPcode(conn, comp_cd)+"Accounting"));
			    DateTimeStamp.appendChild(doc.createTextNode(date_timestamp));
			    //DataSource.appendChild(doc.createTextNode("FMSng"));
			    DataSource.appendChild(doc.createTextNode(CommonVariable.app_name));
			    
			    //Subledger elements
			    Element SubledgerHeader = doc.createElement("SubledgerHeader");
			    Subledger.appendChild(SubledgerHeader);
			    
			    Element BusinessActivity = doc.createElement("BusinessActivity");
			    Element DocumentType = doc.createElement("DocumentType"); 
			    Element DocumentDate = doc.createElement("DocumentDate"); 
			    Element PostingDate = doc.createElement("PostingDate"); 
			    Element AccountingPeriodMonth = doc.createElement("AccountingPeriodMonth");
			    Element AccountingPeriodYear = doc.createElement("AccountingPeriodYear");
			    Element InternalLegalEntity = doc.createElement("InternalLegalEntity"); 
			    Element DocHeaderText = doc.createElement("DocHeaderText"); 
			    Element RefNum = doc.createElement("RefNum"); 
			    Element EmsRefNum = doc.createElement("EmsRefNum"); 
			    Element Currency = doc.createElement("Currency"); 
			    Element LocalCurrency = doc.createElement("LocalCurrency"); 
			    Element TranslationDate = doc.createElement("TranslationDate");

			    
			    //SubledgerHeader element
			    SubledgerHeader.appendChild(BusinessActivity);
			    SubledgerHeader.appendChild(DocumentType);
			    SubledgerHeader.appendChild(DocumentDate);
			    SubledgerHeader.appendChild(PostingDate);
			    SubledgerHeader.appendChild(AccountingPeriodMonth);
			    SubledgerHeader.appendChild(AccountingPeriodYear);
			    SubledgerHeader.appendChild(InternalLegalEntity);
			    SubledgerHeader.appendChild(DocHeaderText);
			    SubledgerHeader.appendChild(RefNum);
			    SubledgerHeader.appendChild(EmsRefNum);
			    SubledgerHeader.appendChild(Currency);
			    SubledgerHeader.appendChild(LocalCurrency);
			    SubledgerHeader.appendChild(TranslationDate);
			    
			    BusinessActivity.appendChild(doc.createTextNode("RFBU")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
			    DocumentType.appendChild(doc.createTextNode("X3"));
			    DocumentDate.appendChild(doc.createTextNode(documentDate));
			    PostingDate.appendChild(doc.createTextNode(xml_sysdate));
			    AccountingPeriodMonth.appendChild(doc.createTextNode(accountingPeriodMonth));
			    AccountingPeriodYear.appendChild(doc.createTextNode(accountingPeriodYear));
			    InternalLegalEntity.appendChild(doc.createTextNode(utilBean.getCompanySAPcode(conn, comp_cd)));
			    DocHeaderText.appendChild(doc.createTextNode(docHeaderText));
			    RefNum.appendChild(doc.createTextNode(xml_num));
			    EmsRefNum.appendChild(doc.createTextNode(xml_num));
			    Currency.appendChild(doc.createTextNode("INR")); //NO NEED TO SEND OTHER TYPE OF CURRENCY - JD
				
			    String account=utilBean.getGasExchangeSAPcode(conn, gx_counterptyCd);
			    String countpty_category=utilBean.getCounterpartyCategory(conn, counterpty_cd);
		    	
		    	String tempAccount="";
		    	String tempAccount2="";//For even line seq
		    	String pk2="40";
		    	//String pk="50"; //For even line seq
		    	String pk="31"; //For even line seq
		    	String sign = "-";
		    	String sign2 = "";
		    	
		    	tempAccount=account; // 05-09-2023: Counterparty SAP CODE Will hit for both NG and IG 
		    	if(countpty_category.equals("Group"))
		    	{
		    		//tempAccount=account; // Counterparty SAP CODE
		    		tempAccount2="6301400";
		    	}
		    	else
		    	{
		    		//tempAccount="3180720"; // PURCHASE ETRM ACCRUALS
		    		//pk="50"
		    		tempAccount2="6300400";
		    	}
		    	
		    	tempAccount2="7220300";//Brokerage
		    	
		    	String gx_buNm=utilBean.getCounterpartyBuPlantABBR(conn,gx_counterptyCd, comp_cd, gx_bu_seq, "G");
		    	
		    	//String assignmentNo=entityType+""+counterpty_cd+cont_type+contno;
		    	String assignmentNo="G"+gx_counterptyCd+"-"+utilBean.NewDealMappingId(comp_cd, counterpty_cd, agmtno, "", contno, "", cont_type, "");
		    	String businessAreaCode=utilBean.getBusinessAreaSAPcode(conn, comp_cd, entityType, cont_type, report_dt);
		    	
		    	int i=0;
				queryString1="SELECT TO_CHAR(INVOICE_DUE_DT,'DD/MM/YYYY'),ACCRUAL_QTY,ACCRUAL_AMT,GROSS_AMT,"
						+ "TO_CHAR(PROD_MONTH,'DD/MM/YYYY') "
						+ "FROM FMS_GX_ACCRUAL_DTL "
						+ "WHERE COMPANY_CD=? "
						+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') "//AND PROD_MONTH=TO_DATE('"+prod_month+"','DD/MM/YYYY') "
						+ "AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
						+ "AND CONTRACT_TYPE=? AND GX_BU_UNIT=? AND BU_UNIT=? "
						+ "AND FINANCIAL_YEAR=? AND GX_COUNTERPARTY_CD=?";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, report_dt);
				stmt1.setString(3, counterpty_cd);
				stmt1.setString(4, agmtno);
				stmt1.setString(5, contno);
				stmt1.setString(6, cont_type);
				stmt1.setString(7, gx_bu_seq);
				stmt1.setString(8, buSeq);
				stmt1.setString(9, financialYear);
				stmt1.setString(10, gx_counterptyCd);
				rset1 = stmt1.executeQuery();
				while(rset1.next())
				{
					String invoice_dueDt=rset1.getString(1)==null?"":rset1.getString(1);
					String accrual_qty=nf.format(rset1.getDouble(2));
					//3
					String grossAmt=nf.format(rset1.getDouble(4));
					String prod_month=rset1.getString(5)==null?"":rset1.getString(5);
					
					String monthNm = utilDate.getShortMonthName(prod_month);
			    	String monthId="";
					String yearNm = "";
			    	if(!prod_month.equals(""))
					{
						String[] temp_split=prod_month.split("/");
						monthId=temp_split[1];
						yearNm=temp_split[2];		
					}
							
			    	String itemText="";
			    	String itemText2="Brokerage/Commission "+monthNm+" "+yearNm;
			    	
					if(!invoice_dueDt.equals(""))
					{
						String[] temp_split=invoice_dueDt.split("/");
						invoice_dueDt=temp_split[2]+""+temp_split[1]+""+temp_split[0];
					}
					
					for(int j=0; j<2;j++)
					{	    
				    	i+=1;
				    	Element SubledgerEntry = doc.createElement("SubledgerEntry");
				    	Subledger.appendChild(SubledgerEntry);
				    	
				    	Element VendorId  = doc.createElement("VendorId");//
				    	Element LineSeqNo = doc.createElement("LineSeqNo");//
					    Element PostingKey = doc.createElement("PostingKey");//
					    Element Account = doc.createElement("Account");//
					    Element CurrencyAmount = doc.createElement("CurrencyAmount"); //
					    Element LocalCurrencyAmount = doc.createElement("LocalCurrencyAmount");//
					    Element Material = doc.createElement("Material");//
					    Element BusinessArea = doc.createElement("BusinessArea");//
					    Element ItemText = doc.createElement("ItemText");//
					    Element Volume = doc.createElement("Volume");//
					    Element VolumeUnit = doc.createElement("VolumeUnit");//
					    Element ReferenceKey1 = doc.createElement("ReferenceKey1");//
					    Element ReferenceKey2 = doc.createElement("ReferenceKey2");//
					    Element ProductionPeriod = doc.createElement("ProductionPeriod");//
					    Element AssignmentNumber = doc.createElement("AssignmentNumber");//
					    Element PaymentTerms = doc.createElement("PaymentTerms");//
					    Element PaymentBlock = doc.createElement("PaymentBlock");//
					    Element PaymentDueDate = doc.createElement("PaymentDueDate");//
					    Element Plant = doc.createElement("Plant");//
					    
				    	// SubledgerEntry elements
					    SubledgerEntry.appendChild(VendorId);//
					    SubledgerEntry.appendChild(LineSeqNo);//
					    SubledgerEntry.appendChild(PostingKey);//
					    SubledgerEntry.appendChild(Account);//
					    SubledgerEntry.appendChild(CurrencyAmount);//
					    SubledgerEntry.appendChild(LocalCurrencyAmount);//--
					    SubledgerEntry.appendChild(Material);//
					    SubledgerEntry.appendChild(BusinessArea);//
					    SubledgerEntry.appendChild(ItemText);//
					    SubledgerEntry.appendChild(Volume);//
					    SubledgerEntry.appendChild(VolumeUnit);//
					    SubledgerEntry.appendChild(ReferenceKey1);//
					    SubledgerEntry.appendChild(ReferenceKey2);
					    SubledgerEntry.appendChild(ProductionPeriod);//
					    SubledgerEntry.appendChild(AssignmentNumber);//
					    SubledgerEntry.appendChild(PaymentTerms);//
					    SubledgerEntry.appendChild(PaymentBlock);//
					    SubledgerEntry.appendChild(PaymentDueDate);//
					    SubledgerEntry.appendChild(Plant);					    
					   
					    if (i%2 == 0) 
					    {
					    	//VendorId.appendChild(doc.createTextNode(utilBean.PrePaddingZero(tempAccount2, 10)));
					    	PostingKey.appendChild(doc.createTextNode(pk2));
					    	Account.appendChild(doc.createTextNode(utilBean.PrePaddingZero(tempAccount2, 10)));
					    	ItemText.appendChild(doc.createTextNode(itemText2));
					    	CurrencyAmount.appendChild(doc.createTextNode(sign2+""+grossAmt));
					    }
					    else
					    {
					    	VendorId.appendChild(doc.createTextNode(tempAccount));
					    	PostingKey.appendChild(doc.createTextNode(pk));
					    	//Account.appendChild(doc.createTextNode(utilBean.PrePaddingZero(tempAccount, 10)));
					    	ItemText.appendChild(doc.createTextNode(itemText));
					    	CurrencyAmount.appendChild(doc.createTextNode(sign+""+grossAmt));
					    }
				    	LineSeqNo.appendChild(doc.createTextNode(""+i));
				    	
				    	//Material.appendChild(doc.createTextNode(utilBean.PrePaddingZero("3344036", 18))); //SERVICE PURCHASE ADDED for now
				    	Material.appendChild(doc.createTextNode(utilBean.PrePaddingZero("1168001", 18)));
				    	BusinessArea.appendChild(doc.createTextNode(businessAreaCode));
				    	ReferenceKey1.appendChild(doc.createTextNode(assignmentNo));
				    	ProductionPeriod.appendChild(doc.createTextNode(yearNm+""+monthId));
				    	AssignmentNumber.appendChild(doc.createTextNode(assignmentNo));
				    	Volume.appendChild(doc.createTextNode(accrual_qty));
				    	VolumeUnit.appendChild(doc.createTextNode("MMB"));
				    	PaymentTerms.appendChild(doc.createTextNode("ZB00")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
				    	PaymentDueDate.appendChild(doc.createTextNode(invoice_dueDt)); //AS PER SUNIDHI MAIL 16/08/2023
				    	PaymentBlock.appendChild(doc.createTextNode("A"));
				    	Plant.appendChild(doc.createTextNode(gx_buNm+" - Plant"));
					}
				}
				rset1.close();
				stmt1.close();
				
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD","");
				transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet","");
				
			    Transformer transformer = transformerFactory.newTransformer();
			    DOMSource source = new DOMSource(doc);
			    
			    String xmlFileNm="";
			    String datetime="";
			    datetime=splitSys[0].replaceAll("/", "")+""+splitSys[1].replaceAll(":", "");
			    
			    if(!fms_MessageId.equals(""))
			    {
			    	xmlFileNm="APGL_"+fms_MessageId+"_"+datetime+".xml";
			    }
				
			    if(!xmlFileNm.equals(""))
			    {
			    	StreamResult result =  new StreamResult(new File(file_path+""+xmlFileNm));
				    transformer.transform(source, result);
				    
				    xmlfile_name=xmlFileNm;
				    
				    if(!xml_seq.equals("") && !xml_num.equals(""))
					{
						queryString1="UPDATE FMS_GX_ACCRUAL_DTL SET XML_SEQ=?,XML_NUM=? "
								+ "WHERE COMPANY_CD=? "
								+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
								+ "AND CONTRACT_TYPE=? AND GX_BU_UNIT=? AND BU_UNIT=? "
								+ "AND FINANCIAL_YEAR=? AND GX_COUNTERPARTY_CD=?";
						
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, xml_seq);
						stmt1.setString(2, xml_num);
						stmt1.setString(3, comp_cd);
						stmt1.setString(4, report_dt);
						stmt1.setString(5, counterpty_cd);
						stmt1.setString(6, agmtno);
						stmt1.setString(7, contno);
						stmt1.setString(8, cont_type);
						stmt1.setString(9, gx_bu_seq);
						stmt1.setString(10, buSeq);
						stmt1.setString(11, financialYear);
						stmt1.setString(12, gx_counterptyCd);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
				}
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void sendAccrualEODConfirmationMailForPurchase()
	{
		String function_nm="sendAccrualEODConfirmationMailForPurchase()";
		int Count=0;
		String MailBody="";
		try
		{
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_TRADER_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, report_dt);
			rset = stmt.executeQuery();
			if(rset.next())
			{
				Count = rset.getInt(1);
			}
			rset.close();
			stmt.close();
			if(Count > 0)
			{
				MailBody="<html>"
						+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";							
				MailBody += CommonVariable.app_name+" Purchase Accrual EoD Process for "+report_dt+" completed at "+utilDate.getSysdateWithTime24hr()+".</font>";
			}
			else
			{
				MailBody="<html>"
						+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";								
				MailBody += CommonVariable.app_name+" Purchase Accrual EoD Process for "+report_dt+" NOT completed at "+utilDate.getSysdateWithTime24hr()+".</font>";	
			}
			
			Count=0;
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_TRADER_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND XML_NUM IS NOT NULL";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, report_dt);
			rset = stmt.executeQuery();
			if(rset.next())
			{
				Count = rset.getInt(1);
			}
			rset.close();
			stmt.close();
			if(Count > 0)
			{	
				MailBody += "<br><br><font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>"+CommonVariable.app_name+" Purchase Accrual XMLs generated for SAP Posting at "+utilDate.getSysdateWithTime24hr()+".</font>";
			}
			
			MailBody+=CommonVariable.mail_disclaimer;
			MailBody+="</html>";
			
			String subject=CommonVariable.app_name_sub+" "+context+": Purchase Accrual EoD Process Status for "+report_dt;
			String to_list=utilBean.getToMailReceipentList(conn, comp_cd, "Accrual Notification", "Purchase", "Daily", "Auto");
			String cc_list=utilBean.getCcMailReceipentList(conn, comp_cd, "Accrual Notification", "Purchase", "Daily", "Auto");
			
			if(!to_list.equals(""))
			{
				mail.sendMail(conn, to_list, subject, MailBody, "", cc_list, "");
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void sendAccrualEODConfirmationMailForGta()
	{
		String function_nm="sendAccrualEODConfirmationMailForGta()";
		int Count=0;
		String MailBody="";
		try
		{
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_GTA_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, report_dt);
			rset = stmt.executeQuery();
			if(rset.next())
			{
				Count = rset.getInt(1);
			}
			rset.close();
			stmt.close();
			if(Count > 0)
			{
				MailBody="<html>"
						+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";				
				MailBody +=CommonVariable.app_name+" GTA Accrual EoD Process for "+report_dt+" completed at "+utilDate.getSysdateWithTime24hr()+".</font>";
			}
			else
			{
				MailBody="<html>"
						+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";				
				MailBody +=CommonVariable.app_name+" GTA Accrual EoD Process for "+report_dt+" NOT completed at "+utilDate.getSysdateWithTime24hr()+".</font>";	
			}
			
			Count=0;
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_GTA_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND XML_NUM IS NOT NULL";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, report_dt);
			rset = stmt.executeQuery();
			if(rset.next())
			{
				Count = rset.getInt(1);
			}
			rset.close();
			stmt.close();
			if(Count > 0)
			{
				MailBody  += "<br><br><font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>"+CommonVariable.app_name+" GTA Accrual XMLs generated for SAP Posting at "+utilDate.getSysdateWithTime24hr()+".</font>";
			}
			
			MailBody+=CommonVariable.mail_disclaimer;
			MailBody+="</html>";
			
			String subject=CommonVariable.app_name_sub+" "+context+": GTA Accrual EoD Process Status for "+report_dt;
			String to_list=utilBean.getToMailReceipentList(conn, comp_cd, "Accrual Notification", "GTA", "Daily", "Auto");
			String cc_list=utilBean.getCcMailReceipentList(conn, comp_cd, "Accrual Notification", "GTA", "Daily", "Auto");
			
			if(!to_list.equals(""))
			{
				mail.sendMail(conn,to_list, subject, MailBody, "", cc_list, "");
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void sendAccrualEODConfirmationMailForGx()
	{
		String function_nm="sendAccrualEODConfirmationMailForGx()";
		int Count=0;
		String MailBody="";
		try
		{
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_GX_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, report_dt);
			rset = stmt.executeQuery();
			if(rset.next())
			{
				Count = rset.getInt(1);
			}
			rset.close();
			stmt.close();
			if(Count > 0)
			{
				MailBody="<html>"
						+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";				
				MailBody +=CommonVariable.app_name+" GX Accrual EoD Process for "+report_dt+" completed at "+utilDate.getSysdateWithTime24hr()+".</font>";
			}
			else
			{
				MailBody="<html>"
						+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";				
				MailBody +=CommonVariable.app_name+" GX Accrual EoD Process for "+report_dt+" NOT completed at "+utilDate.getSysdateWithTime24hr()+".</font>";	
			}
			
			Count=0;
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_GX_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND XML_NUM IS NOT NULL";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, report_dt);
			rset = stmt.executeQuery();
			if(rset.next())
			{
				Count = rset.getInt(1);
			}
			rset.close();
			stmt.close();
			if(Count > 0)
			{
				MailBody += "<br><br><font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>"+CommonVariable.app_name+" GX Accrual XMLs generated for SAP Posting at "+utilDate.getSysdateWithTime24hr()+".</font>";
			}
			
			MailBody+=CommonVariable.mail_disclaimer;
			MailBody+="</html>";
			
			String subject=CommonVariable.app_name_sub+" "+context+": GX Accrual EoD Process Status for "+report_dt;
			String to_list=utilBean.getToMailReceipentList(conn, comp_cd, "Accrual Notification", "GX", "Daily", "Auto");
			String cc_list=utilBean.getCcMailReceipentList(conn, comp_cd, "Accrual Notification", "GX", "Daily", "Auto");
			
			if(!to_list.equals(""))
			{
				mail.sendMail(conn,to_list, subject, MailBody, "", cc_list, "");
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void sendAccrualEODConfirmationMailForSales()
	{
		String function_nm="sendAccrualEODConfirmationMailForSales()";
		int Count=0;
		String MailBody="";
		try
		{
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, report_dt);
			rset = stmt.executeQuery();
			if(rset.next())
			{
				Count = rset.getInt(1);
			}
			rset.close();
			stmt.close();
			if(Count > 0)
			{
				MailBody="<html>"
						+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";				
				MailBody += CommonVariable.app_name+" Sell Accrual EoD Process for "+report_dt+" completed at "+utilDate.getSysdateWithTime24hr()+".</font>";
			}
			else
			{
				MailBody="<html>"
						+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";								
				MailBody += CommonVariable.app_name+" Sell Accrual EoD Process for "+report_dt+" NOT completed at "+utilDate.getSysdateWithTime24hr()+".</font>";	
			}
			
			Count=0;
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND XML_NUM IS NOT NULL";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, report_dt);
			rset = stmt.executeQuery();
			if(rset.next())
			{
				Count = rset.getInt(1);
			}
			rset.close();
			stmt.close();
			if(Count > 0)
			{
				MailBody += "<br><br><font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>"+CommonVariable.app_name+" Sell Accrual XMLs generated for SAP Posting at "+utilDate.getSysdateWithTime24hr()+".</font>";
			}
			
			MailBody+=CommonVariable.mail_disclaimer;
			MailBody+="</html>";
			
			String subject=CommonVariable.app_name_sub+" "+context+": Sell Accrual EoD Process Status for "+report_dt;
			String to_list=utilBean.getToMailReceipentList(conn, comp_cd, "Accrual Notification", "Sell", "Daily", "Auto");
			String cc_list=utilBean.getCcMailReceipentList(conn, comp_cd, "Accrual Notification", "Sell", "Daily", "Auto");
			
			if(!to_list.equals(""))
			{
				mail.sendMail(conn, to_list, subject, MailBody, "", cc_list, "");
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void doClear()
	{
		invoice_type="";
	    month = "";
		year = "";
		report_start_dt="";
		report_end_dt="";
		billing_cycle = "";
		billing_freq="";
		billing_freq_nm="";
		period_start_dt="";
		period_end_dt="";
		financial_year="";
		docHeaderText="";
		xmlfile_name="";
		negative_imbalance_qty="";
		positive_imbalance_qty="";
		unauthorized_overrun_qty="";
		deficiency_qty="";
		
		VPERIOD_START_DT.clear();
		VPERIOD_END_DT.clear();
		VINVOICE_DUE_DT.clear();
		VGROSS_AMT.clear();
		VCOUNTERPTY_CD.clear();
		VCOUNTERPTY_ABBR.clear();
		VINVOICE_RAISED_IN.clear();
		VFINANCIAL_YEAR.clear();
		VCONTRACT_TYPE.clear();
		VCOUNTERPTY_NM.clear();
		VSTART_DT.clear();
		VEND_DT.clear();
		VPLANT_SEQ.clear();
		VPLANT_ABBR.clear();
		VBU_PLANT_SEQ.clear();
		VBU_PLANT_ABBR.clear();
		VBILLING_FREQ_FLAG.clear();
		VBILLING_FREQ_NM.clear();
		VACCRUAL_QTY.clear();
		VACCRUAL_AMT.clear();
		VCONT_REF_NO.clear();
		VPRODUCTION_MONTH.clear();
		VCONT_MAP_LIST.clear();
		VDIS_CONT_MAP_LIST.clear();
		VEXCHNG_RATE.clear();
		VEXCHNG_RATE_CD.clear();
		VEXCHNG_RATE_DT.clear();
		VSALES_PRICE_CD.clear();
		VSALES_PRICE_NM.clear();
		VCONT_NO.clear();
		VCONT_REV_NO.clear();
		VAGMT_NO.clear();
		VAGMT_REV_NO.clear();
		VDIS_CONT_MAPPING.clear();
		VSPLIT_FLAG.clear();
		VSPLIT_VALUE.clear();
		VCASH_FLOW.clear();
		VINVOICE_TITLE.clear();
		VINVOICE_TYPE.clear();
		VTEMP_QTY_MMBTU.clear();
		VQTY_MMBTU.clear();
		VTRANS_BU_SEQ.clear();
		VTRANS_BU_ABBR.clear();
		VCASH_FLOW_NM.clear();
		VBU_STATE_TIN.clear();
		VAGMT_BASE.clear();
		VSELL_BUY_NM.clear();
		VSELL_BUY_FLAG.clear();
		VGX_COUNTERPTY_CD.clear();
		VGX_COUNTERPTY_ABBR.clear();
		VCONT_NAME.clear();
		VGX_BU_PLANT_SEQ.clear();
		VGX_BU_PLANT_ABBR.clear();
		VTXN_CHARGE_UNIT.clear();
		VBUY_SELL.clear();
		VCARGO_NO.clear();
		VBOE_NO.clear();
		VBOE_NM.clear();
		VINV_FLAG.clear();
		VINV_COMPONENTS.clear();
	}
	
	String report_dt="";
	String invoice_type="";
    String month = "";
	String year = "";
	String report_start_dt="";
	String report_end_dt="";
	String billing_cycle = "";
	String billing_freq="";
	String billing_freq_nm="";
	String period_start_dt="";
	String period_end_dt="";
	String financial_year="";
	String docHeaderText="";
	String xmlfile_name="";
	String negative_imbalance_qty="";
	String positive_imbalance_qty="";
	String unauthorized_overrun_qty="";
	String deficiency_qty="";
	String to_dt="";
	String from_dt="";
	
	Vector VPERIOD_START_DT = new Vector();
	Vector VPERIOD_END_DT = new Vector();
	Vector VINVOICE_DUE_DT = new Vector();
	Vector VGROSS_AMT = new Vector();
	Vector VCOUNTERPTY_CD = new Vector();
	Vector VCOUNTERPTY_ABBR = new Vector();
	Vector VINVOICE_RAISED_IN = new Vector();
	Vector VFINANCIAL_YEAR = new Vector();
	Vector VCONTRACT_TYPE = new Vector();
	Vector VCOUNTERPTY_NM = new Vector();
	Vector VSTART_DT = new Vector();
	Vector VEND_DT = new Vector();
	Vector VPLANT_SEQ = new Vector();
	Vector VPLANT_ABBR = new Vector();
	Vector VBU_PLANT_SEQ = new Vector();
	Vector VBU_PLANT_ABBR = new Vector();
	Vector VBILLING_FREQ_FLAG = new Vector();
	Vector VBILLING_FREQ_NM = new Vector();
	Vector VACCRUAL_QTY = new Vector();
	Vector VACCRUAL_AMT = new Vector();
	Vector VCONT_REF_NO = new Vector();
	Vector VPRODUCTION_MONTH = new Vector();
	Vector VCONT_MAP_LIST = new Vector();
	Vector VDIS_CONT_MAP_LIST = new Vector();
	Vector VEXCHNG_RATE = new Vector();
	Vector VEXCHNG_RATE_CD = new Vector();
	Vector VEXCHNG_RATE_DT = new Vector();
	Vector VSALES_PRICE_CD = new Vector();
	Vector VSALES_PRICE_NM = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VCONT_REV_NO = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VAGMT_REV_NO = new Vector();
	Vector VDIS_CONT_MAPPING = new Vector();
	Vector VSPLIT_FLAG = new Vector();
	Vector VSPLIT_VALUE = new Vector();
	Vector VCASH_FLOW = new Vector();
	Vector VINVOICE_TITLE = new Vector();
	Vector VINVOICE_TYPE = new Vector();
	Vector VTEMP_QTY_MMBTU = new Vector();
	Vector VQTY_MMBTU = new Vector();
	Vector VTRANS_BU_SEQ = new Vector();
	Vector VTRANS_BU_ABBR = new Vector();
	Vector VCASH_FLOW_NM = new Vector();
	Vector VBU_STATE_TIN = new Vector();
	Vector VAGMT_BASE = new Vector();
	Vector VSELL_BUY_NM = new Vector();
	Vector VSELL_BUY_FLAG = new Vector();
	Vector VGX_COUNTERPTY_CD = new Vector();
	Vector VGX_COUNTERPTY_ABBR = new Vector();
	Vector VCONT_NAME = new Vector();
	Vector VGX_BU_PLANT_SEQ = new Vector();
	Vector VGX_BU_PLANT_ABBR = new Vector();
	Vector VTXN_CHARGE_UNIT = new Vector();
	Vector VBUY_SELL = new Vector();
	Vector VCARGO_NO = new Vector();
	Vector VBOE_NO = new Vector();
	Vector VBOE_NM = new Vector();
	Vector VINV_FLAG = new Vector();
	Vector VINV_COMPONENTS = new Vector();
}

