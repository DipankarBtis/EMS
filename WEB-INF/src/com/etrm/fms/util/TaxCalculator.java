package com.etrm.fms.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;

public class TaxCalculator {
	
	String db_src_file_name="TaxCalculator.java";
	//Connection conn;
	PreparedStatement stmtement,stmtement1,stmtement2,stmtement3;
	ResultSet resultset,resultset1,resultset2,resultset3;
	String query = "";
	String query1="";
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	
	//NOT IN USED
	/*public double TaxAmountCalculation(String comp_cd, String counterparty_cd, String entity, String seq, String date, String amount)
	{
		String function_nm="TaxAmountCalculation()";
		double amt = 0;
		try
		{
			if(init())
			{
				query="SELECT FACTOR, TAX_ON, TAX_ON_CD, TAX_STR_CD, TO_CHAR(APP_DATE,'DD/MM/YYYY'),TAX_CODE "
						+ "FROM FMS_TAX_STRUCTURE_DTL A "
						+ "WHERE A.COMPANY_CD=? "
						+ "AND TAX_STR_CD=(SELECT TAX_STRUCT_CD FROM FMS_ENTITY_TAX_STRUCT_DTL C WHERE C.COMPANY_CD=? "
						+ "AND C.ENTITY=? AND C.COUNTERPARTY_CD=? AND C.PLANT_SEQ_NO=? "
						+ "AND C.TAX_STRUCT_DT=(SELECT MAX(D.TAX_STRUCT_DT) FROM FMS_ENTITY_TAX_STRUCT_DTL D WHERE C.COMPANY_CD=D.COMPANY_CD "
						+ "AND C.ENTITY=D.ENTITY AND C.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND C.PLANT_SEQ_NO=D.PLANT_SEQ_NO "
						+ "AND D.TAX_STRUCT_DT <= TO_DATE(?,'DD/MM/YYYY'))) "
						+ "AND APP_DATE=(SELECT MAX(C.TAX_STRUCT_DT) FROM FMS_ENTITY_TAX_STRUCT_DTL C WHERE C.COMPANY_CD=? "
						+ "AND C.ENTITY=? AND C.COUNTERPARTY_CD=? AND C.PLANT_SEQ_NO=? "
						+ "AND C.TAX_STRUCT_DT <= TO_DATE(?,'DD/MM/YYYY')) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, comp_cd);
				stmtement.setString(2, comp_cd);
				stmtement.setString(3, entity);
				stmtement.setString(4, counterparty_cd);
				stmtement.setString(5, seq);
				stmtement.setString(6, date);
				stmtement.setString(7, comp_cd);
				stmtement.setString(8, entity);
				stmtement.setString(9, counterparty_cd);
				stmtement.setString(10, seq);
				stmtement.setString(11, date);
				resultset = stmtement.executeQuery();
				while(resultset.next())
				{
					double gross_amt = Double.parseDouble(""+amount);
					double factor = resultset.getDouble(1);
					String tax_on = resultset.getString(2)==null?"":resultset.getString(2);
					String tax_on_cd = resultset.getString(3)==null?"":resultset.getString(3);
					String tax_str_cd = resultset.getString(4)==null?"":resultset.getString(4);
					String tax_str_dt = resultset.getString(5)==null?"":resultset.getString(5);
					
					if(tax_on.equals("2"))
					{
						double tax_on_factor = 0;
						query="SELECT FACTOR "
								+ "FROM FMS_TAX_STRUCTURE_DTL A "
								+ "WHERE COMPANY_CD=? AND TAX_STR_CD=? "
								+ "AND APP_DATE=TO_DATE(?,'DD/MM/YYYY') AND TAX_CODE=?";
						stmtement1 = conn.prepareStatement(query);
						stmtement1.setString(1, comp_cd);
						stmtement1.setString(2, tax_str_cd);
						stmtement1.setString(3, tax_str_dt);
						stmtement1.setString(4, tax_on_cd);
						resultset1 = stmtement1.executeQuery();
						if(resultset1.next())
						{
							tax_on_factor = resultset1.getDouble(1);
						}
						stmtement1.close();
						resultset1.close();
						
						double temp = (gross_amt * tax_on_factor)/100;
						double tax_on_amt = (temp * factor)/100;
						
						amt += tax_on_amt;
					}
					else
					{
						double temp = (gross_amt * factor)/100;
						amt += temp;
					}
				}
				stmtement.close();
				resultset.close();
				conn.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return amt;
	}*/
	
	public Vector TaxAmountCalculationWithInfo(Connection conn,String comp_cd, String counterparty_cd, String entity, String seq, String bu, String date, String amount) throws Exception
	{
		String function_nm="TaxAmountCalculationWithInfo()";
		double amt = 0;
		double tax_factor=0;
		Vector info = new Vector();
		String information = "";
		String tax_str_cd="";
		String tax_str_dt="";
		String tax_struct_dtl="";
		
		Vector VTAX_CODE = new Vector();
		Vector VTAX_DESCR = new Vector();
		Vector VTAX_AMT = new Vector();
		Vector VTAX_BASE_AMT = new Vector();
		try
		{
			if(conn!=null)
			{
				query="SELECT A.FACTOR, A.TAX_ON, A.TAX_ON_CD, A.TAX_STR_CD, TO_CHAR(A.APP_DATE,'DD/MM/YYYY'),A.TAX_CODE,B.TAX_STRUCT_DTL "
						+ "FROM FMS_TAX_STRUCTURE_DTL A, FMS_ENTITY_TAX_STRUCT_DTL B "
						//+ "WHERE A.COMPANY_CD=? AND A.COMPANY_CD=B.COMPANY_CD "
						+ "WHERE B.COMPANY_CD=? "
						+ "AND A.TAX_STR_CD=B.TAX_STRUCT_CD "//AND A.APP_DATE=B.TAX_STRUCT_DT "
						+ "AND B.ENTITY=? AND B.COUNTERPARTY_CD=? AND B.PLANT_SEQ_NO=? AND B.BU_UNIT=? "
						+ "AND B.EFF_DT=(SELECT MAX(C.EFF_DT) FROM FMS_ENTITY_TAX_STRUCT_DTL C "
						+ "WHERE C.ENTITY=B.ENTITY AND C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND C.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND C.BU_UNIT=B.BU_UNIT AND C.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, comp_cd);
				stmtement.setString(2, entity);
				stmtement.setString(3, counterparty_cd);
				stmtement.setString(4, seq);
				stmtement.setString(5, bu);
				stmtement.setString(6, date);
				resultset = stmtement.executeQuery();
				while(resultset.next())
				{
					double gross_amt = Double.parseDouble(""+amount);
					double factor = resultset.getDouble(1);
					String temp_factor=resultset.getString(1)==null?"":resultset.getString(1);
					String tax_on = resultset.getString(2)==null?"":resultset.getString(2);
					String tax_on_cd = resultset.getString(3)==null?"":resultset.getString(3);
					tax_str_cd = resultset.getString(4)==null?"":resultset.getString(4);
					tax_str_dt = resultset.getString(5)==null?"":resultset.getString(5);
					String tax_code = resultset.getString(6)==null?"":resultset.getString(6);
					tax_struct_dtl = resultset.getString(7)==null?"":resultset.getString(7);
					
					String tax_sht_nm = "";
					query="SELECT TAX_ALIAS_CODE "
							+ "FROM FMS_TAX_MST A "
							//+ "WHERE COMPANY_CD=? AND TAX_CODE=? ";
							+ "WHERE TAX_CODE=? ";
					stmtement1 = conn.prepareStatement(query);
					//stmtement1.setString(1, comp_cd);
					stmtement1.setString(1, tax_code);
					resultset1 = stmtement1.executeQuery();
					if(resultset1.next())
					{
						tax_sht_nm=resultset1.getString(1)==null?"":resultset1.getString(1);
					}
					stmtement1.close();
					resultset1.close();
					
					tax_sht_nm+=" "+temp_factor+"%";
					
					double base_amt=0;
					double temp_info=0;
					
					if(tax_on.equals("2"))
					{
						double tax_on_factor = 0;
						query="SELECT FACTOR "
								+ "FROM FMS_TAX_STRUCTURE_DTL A "
								//+ "WHERE COMPANY_CD=? AND TAX_STR_CD=? "
								+ "WHERE TAX_STR_CD=? "
								+ "AND APP_DATE=TO_DATE(?,'DD/MM/YYYY') AND TAX_CODE=?";
						stmtement1 = conn.prepareStatement(query);
						//stmtement1.setString(1, comp_cd);
						stmtement1.setString(1, tax_str_cd);
						stmtement1.setString(2, tax_str_dt);
						stmtement1.setString(3, tax_on_cd);
						resultset1 = stmtement1.executeQuery();
						if(resultset1.next())
						{
							tax_on_factor = resultset1.getDouble(1);
						}
						stmtement1.close();
						resultset1.close();
						
						query="SELECT TAX_ALIAS_CODE "
								+ "FROM FMS_TAX_MST A "
								//+ "WHERE COMPANY_CD=? AND TAX_CODE=? ";
								+ "WHERE TAX_CODE=? ";
						stmtement1 = conn.prepareStatement(query);
						//stmtement1.setString(1, comp_cd);
						stmtement1.setString(1, tax_on_cd);
						resultset1 = stmtement1.executeQuery();
						if(resultset1.next())
						{
							tax_sht_nm+=" on "+(resultset1.getString(1)==null?"":resultset1.getString(1));
						}
						stmtement1.close();
						resultset1.close();
						
						tax_factor+=((tax_on_factor/100) * (factor/100)) * 100;  // NOT IN EFFECTIVE USE IN TAX CALCULATION
						
						double temp = (gross_amt * tax_on_factor)/100;
						double tax_on_amt = (temp * factor)/100;
						
						base_amt=temp;
						//amt += tax_on_amt;
						amt += Double.parseDouble(nf.format(tax_on_amt));
						temp_info = tax_on_amt;
					}
					else
					{
						tax_factor+=factor;  // NOT IN EFFECTIVE USE IN TAX CALCULATION
						
						double temp = (gross_amt * factor)/100;
						base_amt=gross_amt;
						//amt += temp;
						amt += Double.parseDouble(nf.format(temp));
						temp_info = temp;
					}
					
					if(!information.equals(""))
					{
						information +="\n"+tax_sht_nm+" = "+nf.format(temp_info);
					}
					else
					{
						information +=tax_sht_nm+" = "+nf.format(temp_info);
					}
					
					VTAX_CODE.add(tax_code);
					VTAX_AMT.add(nf.format(temp_info));
					VTAX_BASE_AMT.add(nf.format(base_amt));
					VTAX_DESCR.add(tax_sht_nm);
				}
				stmtement.close();
				resultset.close();
				//conn.close();
				
				if(!information.equals(""))
				{
					information += "\nTotal Tax = "+nf.format(amt);
				}
				else
				{
					information += "Total Tax = "+nf.format(amt);
				}
			}
			
			Vector VTEMP_TAX_DTL = new Vector();
			
			VTEMP_TAX_DTL.add(VTAX_CODE);
			VTEMP_TAX_DTL.add(VTAX_DESCR);
			VTEMP_TAX_DTL.add(VTAX_AMT);
			VTEMP_TAX_DTL.add(VTAX_BASE_AMT);
			
			info.add(amt);
			info.add(tax_str_cd);
			info.add(tax_str_dt);
			info.add(information);
			info.add(tax_struct_dtl);
			info.add(nf.format(tax_factor));
			info.add(VTEMP_TAX_DTL);
		}
		catch(Exception e)
		{
			throw e;
		}
		return info;
	}
	
	public Vector ServiceTaxAmountCalculationWithInfo(Connection conn, String comp_cd, String counterparty_cd, String entity, String seq, String bu, String date, String inv_type, String amount) throws Exception
	{
		String function_nm="ServiceTaxAmountCalculationWithInfo()";
		double amt = 0;
		double tax_factor=0;
		Vector info = new Vector();
		String information = "";
		String tax_str_cd="";
		String tax_str_dt="";
		String tax_struct_dtl="";
		
		Vector VTAX_CODE = new Vector();
		Vector VTAX_DESCR = new Vector();
		Vector VTAX_AMT = new Vector();
		Vector VTAX_BASE_AMT = new Vector();
		Vector VTAX_FACTOR = new Vector();
		try
		{
			if(conn!=null)
			{
				query="SELECT A.FACTOR, A.TAX_ON, A.TAX_ON_CD, A.TAX_STR_CD, TO_CHAR(A.APP_DATE,'DD/MM/YYYY'),A.TAX_CODE,B.TAX_STRUCT_DTL "
						+ "FROM FMS_TAX_STRUCTURE_DTL A, FMS_ENTITY_SERVICE_TAX_DTL B "
						//+ "WHERE A.COMPANY_CD=? AND A.COMPANY_CD=B.COMPANY_CD "
						+ "WHERE B.COMPANY_CD=? "
						+ "AND A.TAX_STR_CD=B.TAX_STRUCT_CD "//AND A.APP_DATE=B.TAX_STRUCT_DT "
						+ "AND B.ENTITY=? AND B.COUNTERPARTY_CD=? AND B.PLANT_SEQ_NO=? "
						+ "AND B.BU_UNIT=? AND B.INVOICE_TYPE=? "
						+ "AND B.EFF_DT=(SELECT MAX(C.EFF_DT) FROM FMS_ENTITY_SERVICE_TAX_DTL C "
						+ "WHERE C.ENTITY=B.ENTITY AND C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND C.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND C.BU_UNIT=B.BU_UNIT AND C.INVOICE_TYPE=B.INVOICE_TYPE AND C.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, comp_cd);
				stmtement.setString(2, entity);
				stmtement.setString(3, counterparty_cd);
				stmtement.setString(4, seq);
				stmtement.setString(5, bu);
				stmtement.setString(6, inv_type);
				stmtement.setString(7, date);
				resultset = stmtement.executeQuery();
				while(resultset.next())
				{
					double gross_amt = Double.parseDouble(""+amount);
					double factor = resultset.getDouble(1);
					String temp_factor=resultset.getString(1)==null?"":resultset.getString(1);
					String tax_on = resultset.getString(2)==null?"":resultset.getString(2);
					String tax_on_cd = resultset.getString(3)==null?"":resultset.getString(3);
					tax_str_cd = resultset.getString(4)==null?"":resultset.getString(4);
					tax_str_dt = resultset.getString(5)==null?"":resultset.getString(5);
					String tax_code = resultset.getString(6)==null?"":resultset.getString(6);
					tax_struct_dtl = resultset.getString(7)==null?"":resultset.getString(7);
					
					String tax_sht_nm = "";
					query="SELECT TAX_ALIAS_CODE "
							+ "FROM FMS_TAX_MST A "
							//+ "WHERE COMPANY_CD=? AND TAX_CODE=? ";
							+ "WHERE TAX_CODE=? ";
					stmtement1 = conn.prepareStatement(query);
					//stmtement1.setString(1, comp_cd);
					stmtement1.setString(1, tax_code);
					resultset1 = stmtement1.executeQuery();
					if(resultset1.next())
					{
						tax_sht_nm=resultset1.getString(1)==null?"":resultset1.getString(1);
					}
					stmtement1.close();
					resultset1.close();
					
					tax_sht_nm+=" "+temp_factor+"%";
					
					double base_amt=0;
					double temp_info=0;
					
					if(tax_on.equals("2"))
					{
						double tax_on_factor = 0;
						query="SELECT FACTOR "
								+ "FROM FMS_TAX_STRUCTURE_DTL A "
								//+ "WHERE COMPANY_CD=? AND TAX_STR_CD=? "
								+ "WHERE TAX_STR_CD=? "
								+ "AND APP_DATE=TO_DATE(?,'DD/MM/YYYY') AND TAX_CODE=?";
						stmtement1 = conn.prepareStatement(query);
						//stmtement1.setString(1, comp_cd);
						stmtement1.setString(1, tax_str_cd);
						stmtement1.setString(2, tax_str_dt);
						stmtement1.setString(3, tax_on_cd);
						resultset1 = stmtement1.executeQuery();
						if(resultset1.next())
						{
							tax_on_factor = resultset1.getDouble(1);
						}
						stmtement1.close();
						resultset1.close();
						
						query="SELECT TAX_ALIAS_CODE "
								+ "FROM FMS_TAX_MST A "
								//+ "WHERE COMPANY_CD=? AND TAX_CODE=? ";
								+ "WHERE TAX_CODE=? ";
						stmtement1 = conn.prepareStatement(query);
						//stmtement1.setString(1, comp_cd);
						stmtement1.setString(1, tax_on_cd);
						resultset1 = stmtement1.executeQuery();
						if(resultset1.next())
						{
							tax_sht_nm+=" on "+(resultset1.getString(1)==null?"":resultset1.getString(1));
						}
						stmtement1.close();
						resultset1.close();
						
						tax_factor+=((tax_on_factor/100) * (factor/100)) * 100;  // NOT IN EFFECTIVE USE IN TAX CALCULATION
						
						double temp = (gross_amt * tax_on_factor)/100;
						double tax_on_amt = (temp * factor)/100;
						
						base_amt=temp;
						//amt += tax_on_amt;
						amt += Double.parseDouble(nf.format(tax_on_amt));
						temp_info = tax_on_amt;
					}
					else
					{
						tax_factor+=factor;  // NOT IN EFFECTIVE USE IN TAX CALCULATION
						
						double temp = (gross_amt * factor)/100;
						base_amt=gross_amt;
						//amt += temp;
						amt += Double.parseDouble(nf.format(temp));
						temp_info = temp;
					}
					
					if(!information.equals(""))
					{
						information +="\n"+tax_sht_nm+" = "+nf.format(temp_info);
					}
					else
					{
						information +=tax_sht_nm+" = "+nf.format(temp_info);
					}
					
					VTAX_CODE.add(tax_code);
					VTAX_AMT.add(nf.format(temp_info));
					VTAX_BASE_AMT.add(nf.format(base_amt));
					VTAX_DESCR.add(tax_sht_nm);
					VTAX_FACTOR.add(factor);
				}
				stmtement.close();
				resultset.close();
				//conn.close();
				
				if(!information.equals(""))
				{
					information += "\nTotal Tax = "+nf.format(amt);
				}
				else
				{
					information += "Total Tax = "+nf.format(amt);
				}
			}
			
			Vector VTEMP_TAX_DTL = new Vector();
			
			VTEMP_TAX_DTL.add(VTAX_CODE);
			VTEMP_TAX_DTL.add(VTAX_DESCR);
			VTEMP_TAX_DTL.add(VTAX_AMT);
			VTEMP_TAX_DTL.add(VTAX_BASE_AMT);
			VTEMP_TAX_DTL.add(VTAX_FACTOR);
			
			info.add(amt);
			info.add(tax_str_cd);
			info.add(tax_str_dt);
			info.add(information);
			info.add(tax_struct_dtl);
			info.add(nf.format(tax_factor));
			info.add(VTEMP_TAX_DTL);
		}
		catch(Exception e)
		{
			throw e;
		}
		return info;
	}
	
	public Vector ServiceGrossAndTaxAmountCalculationWithInfo(Connection conn, String comp_cd, String counterparty_cd, String entity, String seq, String bu, String date, String inv_type, String amount) throws Exception
	{
		String function_nm="ServiceGrossAndTaxAmountCalculationWithInfo()";
		double amt = 0;
		double tax_factor=0;
		Vector info = new Vector();
		String information = "";
		String tax_str_cd="";
		String tax_str_dt="";
		String tax_struct_dtl="";
		
		double temp_gross=0;
		double temp_tax=0;
		double invoice_amt = Double.parseDouble(""+amount);
		
		Vector VTAX_CODE = new Vector();
		Vector VTAX_DESCR = new Vector();
		Vector VTAX_AMT = new Vector();
		Vector VTAX_BASE_AMT = new Vector();
		Vector VTAX_FACTOR = new Vector();
		try
		{
			if(conn!=null)
			{
				double temp_tot_factor=0;
				query="SELECT A.FACTOR, A.TAX_ON, A.TAX_ON_CD, A.TAX_STR_CD, TO_CHAR(A.APP_DATE,'DD/MM/YYYY'),A.TAX_CODE,B.TAX_STRUCT_DTL "
						+ "FROM FMS_TAX_STRUCTURE_DTL A, FMS_ENTITY_SERVICE_TAX_DTL B "
						+ "WHERE B.COMPANY_CD=? "
						+ "AND A.TAX_STR_CD=B.TAX_STRUCT_CD "
						+ "AND B.ENTITY=? AND B.COUNTERPARTY_CD=? AND B.PLANT_SEQ_NO=? "
						+ "AND B.BU_UNIT=? AND B.INVOICE_TYPE=? "
						+ "AND B.EFF_DT=(SELECT MAX(C.EFF_DT) FROM FMS_ENTITY_SERVICE_TAX_DTL C "
						+ "WHERE C.ENTITY=B.ENTITY AND C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND C.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND C.BU_UNIT=B.BU_UNIT AND C.INVOICE_TYPE=B.INVOICE_TYPE AND C.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, comp_cd);
				stmtement.setString(2, entity);
				stmtement.setString(3, counterparty_cd);
				stmtement.setString(4, seq);
				stmtement.setString(5, bu);
				stmtement.setString(6, inv_type);
				stmtement.setString(7, date);
				resultset = stmtement.executeQuery();
				while(resultset.next())
				{
					double factor = resultset.getDouble(1);
					String temp_factor=resultset.getString(1)==null?"":resultset.getString(1);
					String tax_on = resultset.getString(2)==null?"":resultset.getString(2);
					String tax_on_cd = resultset.getString(3)==null?"":resultset.getString(3);
					tax_str_cd = resultset.getString(4)==null?"":resultset.getString(4);
					tax_str_dt = resultset.getString(5)==null?"":resultset.getString(5);
					String tax_code = resultset.getString(6)==null?"":resultset.getString(6);
					tax_struct_dtl = resultset.getString(7)==null?"":resultset.getString(7);
					
					if(tax_on.equals("2"))
					{
						double tax_on_factor = 0;
						query="SELECT FACTOR "
								+ "FROM FMS_TAX_STRUCTURE_DTL A "
								+ "WHERE TAX_STR_CD=? "
								+ "AND APP_DATE=TO_DATE(?,'DD/MM/YYYY') AND TAX_CODE=?";
						stmtement1 = conn.prepareStatement(query);
						stmtement1.setString(1, tax_str_cd);
						stmtement1.setString(2, tax_str_dt);
						stmtement1.setString(3, tax_on_cd);
						resultset1 = stmtement1.executeQuery();
						if(resultset1.next())
						{
							tax_on_factor = resultset1.getDouble(1);
						}
						stmtement1.close();
						resultset1.close();
						
						
						temp_tot_factor+=((tax_on_factor/100) * (factor/100)) * 100;  // Effective Tax%
					}
					else
					{
						temp_tot_factor+=factor;  // Effectivev Tax%
					}
				}
				stmtement.close();
				resultset.close();
				
				temp_gross= invoice_amt - (invoice_amt * (temp_tot_factor/(100 + temp_tot_factor)));
				
				query="SELECT A.FACTOR, A.TAX_ON, A.TAX_ON_CD, A.TAX_STR_CD, TO_CHAR(A.APP_DATE,'DD/MM/YYYY'),A.TAX_CODE,B.TAX_STRUCT_DTL "
						+ "FROM FMS_TAX_STRUCTURE_DTL A, FMS_ENTITY_SERVICE_TAX_DTL B "
						+ "WHERE B.COMPANY_CD=? "
						+ "AND A.TAX_STR_CD=B.TAX_STRUCT_CD "
						+ "AND B.ENTITY=? AND B.COUNTERPARTY_CD=? AND B.PLANT_SEQ_NO=? "
						+ "AND B.BU_UNIT=? AND B.INVOICE_TYPE=? "
						+ "AND B.EFF_DT=(SELECT MAX(C.EFF_DT) FROM FMS_ENTITY_SERVICE_TAX_DTL C "
						+ "WHERE C.ENTITY=B.ENTITY AND C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND C.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND C.BU_UNIT=B.BU_UNIT AND C.INVOICE_TYPE=B.INVOICE_TYPE AND C.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, comp_cd);
				stmtement.setString(2, entity);
				stmtement.setString(3, counterparty_cd);
				stmtement.setString(4, seq);
				stmtement.setString(5, bu);
				stmtement.setString(6, inv_type);
				stmtement.setString(7, date);
				resultset = stmtement.executeQuery();
				while(resultset.next())
				{
					double gross_amt = temp_gross;
					double factor = resultset.getDouble(1);
					String temp_factor=resultset.getString(1)==null?"":resultset.getString(1);
					String tax_on = resultset.getString(2)==null?"":resultset.getString(2);
					String tax_on_cd = resultset.getString(3)==null?"":resultset.getString(3);
					tax_str_cd = resultset.getString(4)==null?"":resultset.getString(4);
					tax_str_dt = resultset.getString(5)==null?"":resultset.getString(5);
					String tax_code = resultset.getString(6)==null?"":resultset.getString(6);
					tax_struct_dtl = resultset.getString(7)==null?"":resultset.getString(7);
					
					String tax_sht_nm = "";
					query="SELECT TAX_ALIAS_CODE "
							+ "FROM FMS_TAX_MST A "
							//+ "WHERE COMPANY_CD=? AND TAX_CODE=? ";
							+ "WHERE TAX_CODE=? ";
					stmtement1 = conn.prepareStatement(query);
					//stmtement1.setString(1, comp_cd);
					stmtement1.setString(1, tax_code);
					resultset1 = stmtement1.executeQuery();
					if(resultset1.next())
					{
						tax_sht_nm=resultset1.getString(1)==null?"":resultset1.getString(1);
					}
					stmtement1.close();
					resultset1.close();
					
					tax_sht_nm+=" "+temp_factor+"%";
					
					double base_amt=0;
					double temp_info=0;
					
					if(tax_on.equals("2"))
					{
						double tax_on_factor = 0;
						query="SELECT FACTOR "
								+ "FROM FMS_TAX_STRUCTURE_DTL A "
								//+ "WHERE COMPANY_CD=? AND TAX_STR_CD=? "
								+ "WHERE TAX_STR_CD=? "
								+ "AND APP_DATE=TO_DATE(?,'DD/MM/YYYY') AND TAX_CODE=?";
						stmtement1 = conn.prepareStatement(query);
						//stmtement1.setString(1, comp_cd);
						stmtement1.setString(1, tax_str_cd);
						stmtement1.setString(2, tax_str_dt);
						stmtement1.setString(3, tax_on_cd);
						resultset1 = stmtement1.executeQuery();
						if(resultset1.next())
						{
							tax_on_factor = resultset1.getDouble(1);
						}
						stmtement1.close();
						resultset1.close();
						
						query="SELECT TAX_ALIAS_CODE "
								+ "FROM FMS_TAX_MST A "
								//+ "WHERE COMPANY_CD=? AND TAX_CODE=? ";
								+ "WHERE TAX_CODE=? ";
						stmtement1 = conn.prepareStatement(query);
						//stmtement1.setString(1, comp_cd);
						stmtement1.setString(1, tax_on_cd);
						resultset1 = stmtement1.executeQuery();
						if(resultset1.next())
						{
							tax_sht_nm+=" on "+(resultset1.getString(1)==null?"":resultset1.getString(1));
						}
						stmtement1.close();
						resultset1.close();
						
						tax_factor+=((tax_on_factor/100) * (factor/100)) * 100;  // NOT IN EFFECTIVE USE IN TAX CALCULATION
						
						double temp = (gross_amt * tax_on_factor)/100;
						double tax_on_amt = (temp * factor)/100;
						temp_tax+=tax_on_amt; //WITHOUT ROUNDING OFF
						base_amt=temp;
						//amt += tax_on_amt;
						amt += Double.parseDouble(nf.format(tax_on_amt));
						temp_info = tax_on_amt;
					}
					else
					{
						tax_factor+=factor;  // NOT IN EFFECTIVE USE IN TAX CALCULATION
						
						double temp = (gross_amt * factor)/100;
						temp_tax+=temp; //WITHOUT ROUNDING OFF
						base_amt=gross_amt;
						//amt += temp;
						amt += Double.parseDouble(nf.format(temp));
						temp_info = temp;
					}
					
					if(!information.equals(""))
					{
						information +="\n"+tax_sht_nm+" = "+nf.format(temp_info);
					}
					else
					{
						information +=tax_sht_nm+" = "+nf.format(temp_info);
					}
					
					VTAX_CODE.add(tax_code);
					VTAX_AMT.add(nf.format(temp_info));
					VTAX_BASE_AMT.add(nf.format(base_amt));
					VTAX_DESCR.add(tax_sht_nm);
					VTAX_FACTOR.add(factor);
				}
				stmtement.close();
				resultset.close();
				//conn.close();
				
				double diff = Double.parseDouble(nf.format(invoice_amt - (Double.parseDouble(nf.format(temp_gross)) + amt)));
				if(Double.doubleToRawLongBits(diff) != Double.doubleToRawLongBits(0))
				{
					temp_gross=Double.parseDouble(nf.format(temp_gross + diff));
				}
				
				if(!information.equals(""))
				{
					information += "\nTotal Tax = "+nf.format(amt);
				}
				else
				{
					information += "Total Tax = "+nf.format(amt);
				}
			}
			
			Vector VTEMP_TAX_DTL = new Vector();
			
			VTEMP_TAX_DTL.add(VTAX_CODE);
			VTEMP_TAX_DTL.add(VTAX_DESCR);
			VTEMP_TAX_DTL.add(VTAX_AMT);
			VTEMP_TAX_DTL.add(VTAX_BASE_AMT);
			VTEMP_TAX_DTL.add(VTAX_FACTOR);
			
			info.add(amt);
			info.add(tax_str_cd);
			info.add(tax_str_dt);
			info.add(information);
			info.add(tax_struct_dtl);
			info.add(nf.format(tax_factor));
			info.add(VTEMP_TAX_DTL);
			info.add(nf.format(temp_gross));
		}
		catch(Exception e)
		{
			throw e;
		}
		return info;
	}

	public Vector ServiceTaxAmountCalculationInRupeesWithInfo(Connection conn, String comp_cd, String counterparty_cd, String entity, String seq, String bu, String date, String inv_type, String amount) throws Exception
	{
		String function_nm="ServiceTaxAmountCalculationInRupeesWithInfo()";
		double amt = 0;
		double tax_factor=0;
		Vector info = new Vector();
		String information = "";
		String tax_str_cd="";
		String tax_str_dt="";
		String tax_struct_dtl="";
		
		Vector VTAX_CODE = new Vector();
		Vector VTAX_DESCR = new Vector();
		Vector VTAX_AMT = new Vector();
		Vector VTAX_BASE_AMT = new Vector();
		try
		{
			if(conn!=null)
			{
				query="SELECT A.FACTOR, A.TAX_ON, A.TAX_ON_CD, A.TAX_STR_CD, TO_CHAR(A.APP_DATE,'DD/MM/YYYY'),A.TAX_CODE,B.TAX_STRUCT_DTL "
						+ "FROM FMS_TAX_STRUCTURE_DTL A, FMS_ENTITY_SERVICE_TAX_DTL B "
						//+ "WHERE A.COMPANY_CD=? AND A.COMPANY_CD=B.COMPANY_CD "
						+ "WHERE B.COMPANY_CD=? "
						+ "AND A.TAX_STR_CD=B.TAX_STRUCT_CD "//AND A.APP_DATE=B.TAX_STRUCT_DT "
						+ "AND B.ENTITY=? AND B.COUNTERPARTY_CD=? AND B.PLANT_SEQ_NO=? "
						+ "AND B.BU_UNIT=? AND B.INVOICE_TYPE=? "
						+ "AND B.EFF_DT=(SELECT MAX(C.EFF_DT) FROM FMS_ENTITY_SERVICE_TAX_DTL C "
						+ "WHERE C.ENTITY=B.ENTITY AND C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND C.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND C.BU_UNIT=B.BU_UNIT AND C.INVOICE_TYPE=B.INVOICE_TYPE AND C.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, comp_cd);
				stmtement.setString(2, entity);
				stmtement.setString(3, counterparty_cd);
				stmtement.setString(4, seq);
				stmtement.setString(5, bu);
				stmtement.setString(6, inv_type);
				stmtement.setString(7, date);
				resultset = stmtement.executeQuery();
				while(resultset.next())
				{
					double gross_amt = Double.parseDouble(""+amount);
					double factor = resultset.getDouble(1);
					String temp_factor=resultset.getString(1)==null?"":resultset.getString(1);
					String tax_on = resultset.getString(2)==null?"":resultset.getString(2);
					String tax_on_cd = resultset.getString(3)==null?"":resultset.getString(3);
					tax_str_cd = resultset.getString(4)==null?"":resultset.getString(4);
					tax_str_dt = resultset.getString(5)==null?"":resultset.getString(5);
					String tax_code = resultset.getString(6)==null?"":resultset.getString(6);
					tax_struct_dtl = resultset.getString(7)==null?"":resultset.getString(7);
					
					String tax_sht_nm = "";
					query="SELECT TAX_ALIAS_CODE "
							+ "FROM FMS_TAX_MST A "
							//+ "WHERE COMPANY_CD=? AND TAX_CODE=? ";
							+ "WHERE TAX_CODE=? ";
					stmtement1 = conn.prepareStatement(query);
					//stmtement1.setString(1, comp_cd);
					stmtement1.setString(1, tax_code);
					resultset1 = stmtement1.executeQuery();
					if(resultset1.next())
					{
						tax_sht_nm=resultset1.getString(1)==null?"":resultset1.getString(1);
					}
					stmtement1.close();
					resultset1.close();
					
					tax_sht_nm+=" "+temp_factor+"%";
					
					double base_amt=0;
					double temp_info=0;
					
					if(tax_on.equals("2"))
					{
						double tax_on_factor = 0;
						query="SELECT FACTOR "
								+ "FROM FMS_TAX_STRUCTURE_DTL A "
								//+ "WHERE COMPANY_CD=? AND TAX_STR_CD=? "
								+ "WHERE TAX_STR_CD=? "
								+ "AND APP_DATE=TO_DATE(?,'DD/MM/YYYY') AND TAX_CODE=?";
						stmtement1 = conn.prepareStatement(query);
						//stmtement1.setString(1, comp_cd);
						stmtement1.setString(1, tax_str_cd);
						stmtement1.setString(2, tax_str_dt);
						stmtement1.setString(3, tax_on_cd);
						resultset1 = stmtement1.executeQuery();
						if(resultset1.next())
						{
							tax_on_factor = resultset1.getDouble(1);
						}
						stmtement1.close();
						resultset1.close();
						
						query="SELECT TAX_ALIAS_CODE "
								+ "FROM FMS_TAX_MST A "
								//+ "WHERE COMPANY_CD=? AND TAX_CODE=? ";
								+ "WHERE TAX_CODE=? ";
						stmtement1 = conn.prepareStatement(query);
						//stmtement1.setString(1, comp_cd);
						stmtement1.setString(1, tax_on_cd);
						resultset1 = stmtement1.executeQuery();
						if(resultset1.next())
						{
							tax_sht_nm+=" on "+(resultset1.getString(1)==null?"":resultset1.getString(1));
						}
						stmtement1.close();
						resultset1.close();
						
						tax_factor+=((tax_on_factor/100) * (factor/100)) * 100;  // NOT IN EFFECTIVE USE IN TAX CALCULATION
						
						double temp = (gross_amt * tax_on_factor)/100;
						double tax_on_amt = (temp * factor)/100;
						
						base_amt=temp;
						//amt += tax_on_amt;
						amt += Double.parseDouble(nf.format(tax_on_amt));
						temp_info = tax_on_amt;
					}
					else
					{
						tax_factor+=factor;  // NOT IN EFFECTIVE USE IN TAX CALCULATION
						
						double temp = (gross_amt * factor)/100;
						base_amt=gross_amt;
						//amt += temp;
						amt += Double.parseDouble(nf.format(temp));
						temp_info = temp;
					}
					
					if(!information.equals(""))
					{
						information +="\n"+tax_sht_nm+" = "+nf.format(temp_info);
					}
					else
					{
						information +=tax_sht_nm+" = "+nf.format(temp_info);
					}
					
					VTAX_CODE.add(tax_code);
					VTAX_AMT.add(nf.format(Math.round(temp_info)));
					VTAX_BASE_AMT.add(nf.format(Math.round(base_amt)));
					VTAX_DESCR.add(tax_sht_nm);
				}
				stmtement.close();
				resultset.close();
				//conn.close();
				
				if(!information.equals(""))
				{
					information += "\nTotal Tax = "+nf.format(amt);
				}
				else
				{
					information += "Total Tax = "+nf.format(amt);
				}
			}
			
			Vector VTEMP_TAX_DTL = new Vector();
			
			VTEMP_TAX_DTL.add(VTAX_CODE);
			VTEMP_TAX_DTL.add(VTAX_DESCR);
			VTEMP_TAX_DTL.add(VTAX_AMT);
			VTEMP_TAX_DTL.add(VTAX_BASE_AMT);
			
			info.add(Math.round(amt));
			info.add(tax_str_cd);
			info.add(tax_str_dt);
			info.add(information);
			info.add(tax_struct_dtl);
			info.add(nf.format(tax_factor));
			info.add(VTEMP_TAX_DTL);
		}
		catch(Exception e)
		{
			throw e;
		}
		return info;
	}

	public Vector BuServiceTaxAmountCalculationWithInfo(Connection conn, String comp_cd, String counterparty_cd, String entity, String seq, String bu, String date, String inv_type, String amount) throws Exception
	{
		String function_nm="BuServiceTaxAmountCalculationWithInfo()";
		double amt = 0;
		double tax_factor=0;
		Vector info = new Vector();
		String information = "";
		String tax_str_cd="";
		String tax_str_dt="";
		String tax_struct_dtl="";
		
		Vector VTAX_CODE = new Vector();
		Vector VTAX_DESCR = new Vector();
		Vector VTAX_AMT = new Vector();
		Vector VTAX_BASE_AMT = new Vector();
		try
		{
			if(conn!=null)
			{
				query="SELECT A.FACTOR, A.TAX_ON, A.TAX_ON_CD, A.TAX_STR_CD, TO_CHAR(A.APP_DATE,'DD/MM/YYYY'),A.TAX_CODE,B.TAX_STRUCT_DTL "
						+ "FROM FMS_TAX_STRUCTURE_DTL A, FMS_ENTITY_BU_SVC_TAX_DTL B "
						//+ "WHERE A.COMPANY_CD=? AND A.COMPANY_CD=B.COMPANY_CD "
						+ "WHERE B.COMPANY_CD=? "
						+ "AND A.TAX_STR_CD=B.TAX_STRUCT_CD "//AND A.APP_DATE=B.TAX_STRUCT_DT "
						+ "AND B.ENTITY=? AND B.COUNTERPARTY_CD=? AND B.PLANT_SEQ_NO=? "
						+ "AND B.BU_UNIT=? AND B.INVOICE_TYPE=? "
						+ "AND B.EFF_DT=(SELECT MAX(C.EFF_DT) FROM FMS_ENTITY_BU_SVC_TAX_DTL C "
						+ "WHERE C.ENTITY=B.ENTITY AND C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND C.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND C.BU_UNIT=B.BU_UNIT AND C.INVOICE_TYPE=B.INVOICE_TYPE AND C.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, comp_cd);
				stmtement.setString(2, entity);
				stmtement.setString(3, counterparty_cd);
				stmtement.setString(4, seq);
				stmtement.setString(5, bu);
				stmtement.setString(6, inv_type);
				stmtement.setString(7, date);
				resultset = stmtement.executeQuery();
				while(resultset.next())
				{
					double gross_amt = Double.parseDouble(""+amount);
					double factor = resultset.getDouble(1);
					String temp_factor=resultset.getString(1)==null?"":resultset.getString(1);
					String tax_on = resultset.getString(2)==null?"":resultset.getString(2);
					String tax_on_cd = resultset.getString(3)==null?"":resultset.getString(3);
					tax_str_cd = resultset.getString(4)==null?"":resultset.getString(4);
					tax_str_dt = resultset.getString(5)==null?"":resultset.getString(5);
					String tax_code = resultset.getString(6)==null?"":resultset.getString(6);
					tax_struct_dtl = resultset.getString(7)==null?"":resultset.getString(7);
					
					String tax_sht_nm = "";
					query="SELECT TAX_ALIAS_CODE "
							+ "FROM FMS_TAX_MST A "
							//+ "WHERE COMPANY_CD=? AND TAX_CODE=? ";
							+ "WHERE TAX_CODE=? ";
					stmtement1 = conn.prepareStatement(query);
					//stmtement1.setString(1, comp_cd);
					stmtement1.setString(1, tax_code);
					resultset1 = stmtement1.executeQuery();
					if(resultset1.next())
					{
						tax_sht_nm=resultset1.getString(1)==null?"":resultset1.getString(1);
					}
					stmtement1.close();
					resultset1.close();
					
					tax_sht_nm+=" "+temp_factor+"%";
					
					double base_amt=0;
					double temp_info=0;
					
					if(tax_on.equals("2"))
					{
						double tax_on_factor = 0;
						query="SELECT FACTOR "
								+ "FROM FMS_TAX_STRUCTURE_DTL A "
								//+ "WHERE COMPANY_CD=? AND TAX_STR_CD=? "
								+ "WHERE TAX_STR_CD=? "
								+ "AND APP_DATE=TO_DATE(?,'DD/MM/YYYY') AND TAX_CODE=?";
						stmtement1 = conn.prepareStatement(query);
						//stmtement1.setString(1, comp_cd);
						stmtement1.setString(1, tax_str_cd);
						stmtement1.setString(2, tax_str_dt);
						stmtement1.setString(3, tax_on_cd);
						resultset1 = stmtement1.executeQuery();
						if(resultset1.next())
						{
							tax_on_factor = resultset1.getDouble(1);
						}
						stmtement1.close();
						resultset1.close();
						
						query="SELECT TAX_ALIAS_CODE "
								+ "FROM FMS_TAX_MST A "
								//+ "WHERE COMPANY_CD=? AND TAX_CODE=? ";
								+ "WHERE TAX_CODE=? ";
						stmtement1 = conn.prepareStatement(query);
						//stmtement1.setString(1, comp_cd);
						stmtement1.setString(1, tax_on_cd);
						resultset1 = stmtement1.executeQuery();
						if(resultset1.next())
						{
							tax_sht_nm+=" on "+(resultset1.getString(1)==null?"":resultset1.getString(1));
						}
						stmtement1.close();
						resultset1.close();
						
						tax_factor+=((tax_on_factor/100) * (factor/100)) * 100;  // NOT IN EFFECTIVE USE IN TAX CALCULATION
						
						double temp = (gross_amt * tax_on_factor)/100;
						double tax_on_amt = (temp * factor)/100;
						
						base_amt=temp;
						//amt += tax_on_amt;
						amt += Double.parseDouble(nf.format(tax_on_amt));
						temp_info = tax_on_amt;
					}
					else
					{
						tax_factor+=factor;  // NOT IN EFFECTIVE USE IN TAX CALCULATION
						
						double temp = (gross_amt * factor)/100;
						base_amt=gross_amt;
						//amt += temp;
						amt += Double.parseDouble(nf.format(temp));
						temp_info = temp;
					}
					
					if(!information.equals(""))
					{
						information +="\n"+tax_sht_nm+" = "+nf.format(temp_info);
					}
					else
					{
						information +=tax_sht_nm+" = "+nf.format(temp_info);
					}
					
					VTAX_CODE.add(tax_code);
					VTAX_AMT.add(nf.format(temp_info));
					VTAX_BASE_AMT.add(nf.format(base_amt));
					VTAX_DESCR.add(tax_sht_nm);
				}
				stmtement.close();
				resultset.close();
				//conn.close();
				
				if(!information.equals(""))
				{
					information += "\nTotal Tax = "+nf.format(amt);
				}
				else
				{
					information += "Total Tax = "+nf.format(amt);
				}
			}
			
			Vector VTEMP_TAX_DTL = new Vector();
			
			VTEMP_TAX_DTL.add(VTAX_CODE);
			VTEMP_TAX_DTL.add(VTAX_DESCR);
			VTEMP_TAX_DTL.add(VTAX_AMT);
			VTEMP_TAX_DTL.add(VTAX_BASE_AMT);
			
			info.add(amt);
			info.add(tax_str_cd);
			info.add(tax_str_dt);
			info.add(information);
			info.add(tax_struct_dtl);
			info.add(nf.format(tax_factor));
			info.add(VTEMP_TAX_DTL);
		}
		catch(Exception e)
		{
			throw e;
		}
		return info;
	}

	public Vector TCS_TDSAmountCalculationWithInfo(Connection conn,String comp_cd, String counterparty_cd, String entity, String tcs_app, String invoice_type, String tax_category,String date, String amount) throws Exception
	{
		String function_nm="TCS_TDSAmountCalculationWithInfo()";
		double amt = 0;
		double tax_factor=0;
		Vector info = new Vector();
		String information = "";
		String tax_str_cd="";
		String tax_str_dt="";
		String tax_struct_dtl="";
		try
		{
			if(conn!=null)
			{
				query="SELECT A.FACTOR, A.TAX_ON, A.TAX_ON_CD, A.TAX_STR_CD, TO_CHAR(A.APP_DATE,'DD/MM/YYYY'),A.TAX_CODE,B.TAX_STRUCT_DTL "
						+ "FROM FMS_TAX_STRUCTURE_DTL A, FMS_ENTITY_TCS_TDS_MST B "
						//+ "WHERE A.COMPANY_CD=? AND A.COMPANY_CD=B.COMPANY_CD "
						+ "WHERE B.COMPANY_CD=? "
						+ "AND A.TAX_STR_CD=B.TAX_STRUCT_CD "//AND A.APP_DATE=B.TAX_STRUCT_DT "
						+ "AND B.ENTITY=? AND B.COUNTERPARTY_CD=? "
						+ "AND B.TAX_APP=? AND B.TAX_CATEGORY=? AND B.INVOICE_TYPE=? "
						+ "AND B.EFF_DT=(SELECT MAX(C.EFF_DT) FROM FMS_ENTITY_TCS_TDS_MST C "
						+ "WHERE C.ENTITY=B.ENTITY AND C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND C.TAX_CATEGORY=B.TAX_CATEGORY AND C.TAX_APP=B.TAX_APP AND C.INVOICE_TYPE=B.INVOICE_TYPE "
						+ "AND C.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, comp_cd);
				stmtement.setString(2, entity);
				stmtement.setString(3, counterparty_cd);
				stmtement.setString(4, tcs_app);
				stmtement.setString(5, tax_category);
				stmtement.setString(6, invoice_type);
				stmtement.setString(7, date);
				resultset = stmtement.executeQuery();
				while(resultset.next())
				{
					double gross_amt = Double.parseDouble(""+amount);
					double factor = resultset.getDouble(1);
					String tax_on = resultset.getString(2)==null?"":resultset.getString(2);
					String tax_on_cd = resultset.getString(3)==null?"":resultset.getString(3);
					tax_str_cd = resultset.getString(4)==null?"":resultset.getString(4);
					tax_str_dt = resultset.getString(5)==null?"":resultset.getString(5);
					String tax_code = resultset.getString(6)==null?"":resultset.getString(6);
					tax_struct_dtl = resultset.getString(7)==null?"":resultset.getString(7);
					
					double temp_info=0;
					if(tax_on.equals("2"))
					{
						double tax_on_factor = 0;
						query="SELECT FACTOR "
								+ "FROM FMS_TAX_STRUCTURE_DTL A "
								//+ "WHERE COMPANY_CD=? AND TAX_STR_CD=? "
								+ "WHERE TAX_STR_CD=? "
								+ "AND APP_DATE=TO_DATE(?,'DD/MM/YYYY') AND TAX_CODE=?";
						stmtement1 = conn.prepareStatement(query);
						//stmtement1.setString(1, comp_cd);
						stmtement1.setString(1, tax_str_cd);
						stmtement1.setString(2, tax_str_dt);
						stmtement1.setString(3, tax_on_cd);
						resultset1 = stmtement1.executeQuery();
						if(resultset1.next())
						{
							tax_on_factor = resultset1.getDouble(1);
						}
						stmtement1.close();
						resultset1.close();
						tax_factor+=((tax_on_factor/100) * (factor/100)) * 100;  // NOT IN EFFECTIVE USE IN TAX CALCULATION
						
						double temp = (gross_amt * tax_on_factor)/100;
						double tax_on_amt = (temp * factor)/100;
						
						amt += tax_on_amt;
						
						temp_info = tax_on_amt;
					}
					else
					{
						tax_factor+=factor;  // NOT IN EFFECTIVE USE IN TAX CALCULATION
						
						double temp = (gross_amt * factor)/100;
						amt += temp;
						
						temp_info = temp;
					}
					
					String tax_sht_nm = "";
					query="SELECT TAX_ALIAS_CODE "
							+ "FROM FMS_TAX_MST A "
							//+ "WHERE COMPANY_CD=? AND TAX_CODE=? ";
							+ "WHERE TAX_CODE=? ";
					stmtement1 = conn.prepareStatement(query);
					//stmtement1.setString(1, comp_cd);
					stmtement1.setString(1, tax_code);
					resultset1 = stmtement1.executeQuery();
					if(resultset1.next())
					{
						tax_sht_nm=resultset1.getString(1)==null?"":resultset1.getString(1);
					}
					
					if(!information.equals(""))
					{
						information +="\n"+tax_sht_nm+" : "+nf.format(factor)+"% = "+nf.format(temp_info);
					}
					else
					{
						information +=tax_sht_nm+" : "+nf.format(factor)+"% = "+nf.format(temp_info);
					}
					
				}
				stmtement.close();
				resultset.close();
				//conn.close();
				
				if(!information.equals(""))
				{
					information += "\nTotal Tax = "+nf.format(amt);
				}
				else
				{
					information += "Total Tax = "+nf.format(amt);
				}
			}
			
			info.add(amt);
			info.add(tax_str_cd);
			info.add(tax_str_dt);
			info.add(information);
			info.add(tax_struct_dtl);
			info.add(nf3.format(tax_factor));
		}
		catch(Exception e)
		{
			throw e;
		}
		return info;
	}
	
	public Vector CustomTaxAmountCalculationWithInfo(Connection conn,String comp_cd, String date, String amount) throws Exception
	{
		String function_nm="CustomTaxAmountCalculationWithInfo()";
		
		double amt = 0;
		double tax_factor=0;
		Vector info = new Vector();
		String information = "";
		String tax_str_cd="";
		String tax_str_dt="";
		String tax_struct_dtl="";
		
		Vector VTAX_CODE = new Vector();
		Vector VTAX_DESCR = new Vector();
		Vector VTAX_AMT = new Vector();
		Vector VTAX_BASE_AMT = new Vector();
		try
		{
			if(conn!=null)
			{
				query="SELECT A.FACTOR, A.TAX_ON, A.TAX_ON_CD, A.TAX_STR_CD, TO_CHAR(A.APP_DATE,'DD/MM/YYYY'),A.TAX_CODE,B.TAX_STRUCT_DTL "
						+ "FROM FMS_TAX_STRUCTURE_DTL A, FMS_CUSTOM_TAX_STRUCT_DTL B "
						+ "WHERE B.COMPANY_CD=? "
						+ "AND A.TAX_STR_CD=B.TAX_STRUCT_CD "//AND A.APP_DATE=B.TAX_STRUCT_DT "
						+ "AND B.EFF_DT=(SELECT MAX(C.EFF_DT) FROM FMS_CUSTOM_TAX_STRUCT_DTL C "
						+ "WHERE B.COMPANY_CD=C.COMPANY_CD AND C.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, comp_cd);
				stmtement.setString(2, date);
				resultset = stmtement.executeQuery();
				while(resultset.next())
				{
					double gross_amt = Double.parseDouble(""+amount);
					double factor = resultset.getDouble(1);
					String temp_factor=resultset.getString(1)==null?"":resultset.getString(1);
					String tax_on = resultset.getString(2)==null?"":resultset.getString(2);
					String tax_on_cd = resultset.getString(3)==null?"":resultset.getString(3);
					tax_str_cd = resultset.getString(4)==null?"":resultset.getString(4);
					tax_str_dt = resultset.getString(5)==null?"":resultset.getString(5);
					String tax_code = resultset.getString(6)==null?"":resultset.getString(6);
					tax_struct_dtl = resultset.getString(7)==null?"":resultset.getString(7);
					
					String tax_sht_nm = "";
					query="SELECT TAX_ALIAS_CODE "
							+ "FROM FMS_TAX_MST A "
							//+ "WHERE COMPANY_CD=? AND TAX_CODE=? ";
							+ "WHERE TAX_CODE=? ";
					stmtement1 = conn.prepareStatement(query);
					//stmtement1.setString(1, comp_cd);
					stmtement1.setString(1, tax_code);
					resultset1 = stmtement1.executeQuery();
					if(resultset1.next())
					{
						tax_sht_nm=resultset1.getString(1)==null?"":resultset1.getString(1);
					}
					stmtement1.close();
					resultset1.close();
					
					tax_sht_nm+=" "+temp_factor+"%";
					
					double base_amt=0;
					double temp_info=0;
					
					if(tax_on.equals("2"))
					{
						double tax_on_factor = 0;
						query="SELECT FACTOR "
								+ "FROM FMS_TAX_STRUCTURE_DTL A "
								//+ "WHERE COMPANY_CD=? AND TAX_STR_CD=? "
								+ "WHERE TAX_STR_CD=? "
								+ "AND APP_DATE=TO_DATE(?,'DD/MM/YYYY') AND TAX_CODE=?";
						stmtement1 = conn.prepareStatement(query);
						//stmtement1.setString(1, comp_cd);
						stmtement1.setString(1, tax_str_cd);
						stmtement1.setString(2, tax_str_dt);
						stmtement1.setString(3, tax_on_cd);
						resultset1 = stmtement1.executeQuery();
						if(resultset1.next())
						{
							tax_on_factor = resultset1.getDouble(1);
						}
						stmtement1.close();
						resultset1.close();
						
						query="SELECT TAX_ALIAS_CODE "
								+ "FROM FMS_TAX_MST A "
								//+ "WHERE COMPANY_CD=? AND TAX_CODE=? ";
								+ "WHERE TAX_CODE=? ";
						stmtement1 = conn.prepareStatement(query);
						//stmtement1.setString(1, comp_cd);
						stmtement1.setString(1, tax_on_cd);
						resultset1 = stmtement1.executeQuery();
						if(resultset1.next())
						{
							tax_sht_nm+=" on "+(resultset1.getString(1)==null?"":resultset1.getString(1));
						}
						stmtement1.close();
						resultset1.close();
						
						tax_factor+=((tax_on_factor/100) * (factor/100)) * 100;  // NOT IN EFFECTIVE USE IN TAX CALCULATION
						
						double temp = (gross_amt * tax_on_factor)/100;
						double tax_on_amt = (temp * factor)/100;
						
						base_amt=temp;
						//amt += tax_on_amt;
						amt += Double.parseDouble(nf.format(tax_on_amt));
						temp_info = tax_on_amt;
					}
					else
					{
						tax_factor+=factor;  // NOT IN EFFECTIVE USE IN TAX CALCULATION
						
						double temp = (gross_amt * factor)/100;
						base_amt=gross_amt;
						//amt += temp;
						amt += Double.parseDouble(nf.format(temp));
						temp_info = temp;
					}
					
					if(!information.equals(""))
					{
						information +="\n"+tax_sht_nm+" = "+nf.format(temp_info);
					}
					else
					{
						information +=tax_sht_nm+" = "+nf.format(temp_info);
					}
					
					VTAX_CODE.add(tax_code);
					VTAX_AMT.add(nf.format(temp_info));
					VTAX_BASE_AMT.add(nf.format(base_amt));
					VTAX_DESCR.add(tax_sht_nm);
				}
				stmtement.close();
				resultset.close();
				//conn.close();
				
				if(!information.equals(""))
				{
					information += "\nTotal Tax = "+nf.format(amt);
				}
				else
				{
					information += "Total Tax = "+nf.format(amt);
				}
			}
			
			Vector VTEMP_TAX_DTL = new Vector();
			
			VTEMP_TAX_DTL.add(VTAX_CODE);
			VTEMP_TAX_DTL.add(VTAX_DESCR);
			VTEMP_TAX_DTL.add(VTAX_AMT);
			VTEMP_TAX_DTL.add(VTAX_BASE_AMT);
			
			info.add(amt);
			info.add(tax_str_cd);
			info.add(tax_str_dt);
			info.add(information);
			info.add(tax_struct_dtl);
			info.add(nf.format(tax_factor));
			info.add(VTEMP_TAX_DTL);
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return info;
	}
	
	public Vector TaxAmountCalculation(Connection conn,String tax_struct_cd, String amount) throws Exception
	{
		String function_nm="TaxAmountCalculation()";
		double amt = 0;
		double tax_factor=0;
		Vector info = new Vector();
		String information = "";
		String tax_str_cd="";
		String tax_str_dt="";
		String tax_struct_dtl="";
		
		Vector VTAX_CODE = new Vector();
		Vector VTAX_DESCR = new Vector();
		Vector VTAX_AMT = new Vector();
		Vector VTAX_BASE_AMT = new Vector();
		try
		{
			if(conn!=null)
			{
				query="SELECT A.FACTOR, A.TAX_ON, A.TAX_ON_CD, A.TAX_STR_CD, TO_CHAR(A.APP_DATE,'DD/MM/YYYY'),A.TAX_CODE,B.DESCR "
						+ "FROM FMS_TAX_STRUCTURE_DTL A, FMS_TAX_STRUCTURE B "
						+ "WHERE B.TAX_STR_CD=? "
						+ "AND A.TAX_STR_CD=B.TAX_STR_CD ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, tax_struct_cd);
				resultset = stmtement.executeQuery();
				while(resultset.next())
				{
					double gross_amt = Double.parseDouble(""+amount);
					double factor = resultset.getDouble(1);
					String temp_factor=resultset.getString(1)==null?"":resultset.getString(1);
					String tax_on = resultset.getString(2)==null?"":resultset.getString(2);
					String tax_on_cd = resultset.getString(3)==null?"":resultset.getString(3);
					tax_str_cd = resultset.getString(4)==null?"":resultset.getString(4);
					tax_str_dt = resultset.getString(5)==null?"":resultset.getString(5);
					String tax_code = resultset.getString(6)==null?"":resultset.getString(6);
					tax_struct_dtl = resultset.getString(7)==null?"":resultset.getString(7);
					
					String tax_sht_nm = "";
					query="SELECT TAX_ALIAS_CODE "
							+ "FROM FMS_TAX_MST A "
							+ "WHERE TAX_CODE=? ";
					stmtement1 = conn.prepareStatement(query);
					stmtement1.setString(1, tax_code);
					resultset1 = stmtement1.executeQuery();
					if(resultset1.next())
					{
						tax_sht_nm=resultset1.getString(1)==null?"":resultset1.getString(1);
					}
					stmtement1.close();
					resultset1.close();
					
					tax_sht_nm+=" "+temp_factor+"%";
					
					double base_amt=0;
					double temp_info=0;
					
					if(tax_on.equals("2"))
					{
						double tax_on_factor = 0;
						query="SELECT FACTOR "
								+ "FROM FMS_TAX_STRUCTURE_DTL A "
								+ "WHERE TAX_STR_CD=? "
								+ "AND APP_DATE=TO_DATE(?,'DD/MM/YYYY') AND TAX_CODE=?";
						stmtement1 = conn.prepareStatement(query);
						stmtement1.setString(1, tax_str_cd);
						stmtement1.setString(2, tax_str_dt);
						stmtement1.setString(3, tax_on_cd);
						resultset1 = stmtement1.executeQuery();
						if(resultset1.next())
						{
							tax_on_factor = resultset1.getDouble(1);
						}
						stmtement1.close();
						resultset1.close();
						
						query="SELECT TAX_ALIAS_CODE "
								+ "FROM FMS_TAX_MST A "
								+ "WHERE TAX_CODE=? ";
						stmtement1 = conn.prepareStatement(query);
						stmtement1.setString(1, tax_on_cd);
						resultset1 = stmtement1.executeQuery();
						if(resultset1.next())
						{
							tax_sht_nm+=" on "+(resultset1.getString(1)==null?"":resultset1.getString(1));
						}
						stmtement1.close();
						resultset1.close();
						
						tax_factor+=((tax_on_factor/100) * (factor/100)) * 100;  // NOT IN EFFECTIVE USE IN TAX CALCULATION
						
						double temp = (gross_amt * tax_on_factor)/100;
						double tax_on_amt = (temp * factor)/100;
						
						base_amt=temp;
						//amt += tax_on_amt;
						amt += Double.parseDouble(nf.format(tax_on_amt));
						temp_info = tax_on_amt;
					}
					else
					{
						tax_factor+=factor;  // NOT IN EFFECTIVE USE IN TAX CALCULATION
						
						double temp = (gross_amt * factor)/100;
						base_amt=gross_amt;
						//amt += temp;
						amt += Double.parseDouble(nf.format(temp));
						temp_info = temp;
					}
					
					if(!information.equals(""))
					{
						information +="\n"+tax_sht_nm+" = "+nf.format(temp_info);
					}
					else
					{
						information +=tax_sht_nm+" = "+nf.format(temp_info);
					}
					
					VTAX_CODE.add(tax_code);
					VTAX_AMT.add(nf.format(temp_info));
					VTAX_BASE_AMT.add(nf.format(base_amt));
					VTAX_DESCR.add(tax_sht_nm);
				}
				stmtement.close();
				resultset.close();
				//conn.close();
				
				if(!information.equals(""))
				{
					information += "\nTotal Tax = "+nf.format(amt);
				}
				else
				{
					information += "Total Tax = "+nf.format(amt);
				}
			}
			
			Vector VTEMP_TAX_DTL = new Vector();
			
			VTEMP_TAX_DTL.add(VTAX_CODE);
			VTEMP_TAX_DTL.add(VTAX_DESCR);
			VTEMP_TAX_DTL.add(VTAX_AMT);
			VTEMP_TAX_DTL.add(VTAX_BASE_AMT);
			
			info.add(amt);
			info.add(tax_str_cd);
			info.add(tax_str_dt);
			info.add(information);
			info.add(tax_struct_dtl);
			info.add(nf.format(tax_factor));
			info.add(VTEMP_TAX_DTL);
		}
		catch(Exception e)
		{
			throw e;
		}
		return info;
	}
	
	public Double TaxFactor(Connection conn,String tax_struct_cd) throws Exception
	{
		String function_nm="TaxFactor()";
		double amt = 0;
		double tax_factor=0;
		String tax_str_cd="";
		String tax_str_dt="";
		String tax_struct_dtl="";
		
		try
		{
			if(conn!=null)
			{
				query="SELECT A.FACTOR, A.TAX_ON, A.TAX_ON_CD, A.TAX_STR_CD, TO_CHAR(A.APP_DATE,'DD/MM/YYYY'),A.TAX_CODE,B.DESCR "
						+ "FROM FMS_TAX_STRUCTURE_DTL A, FMS_TAX_STRUCTURE B "
						+ "WHERE B.TAX_STR_CD=? "
						+ "AND A.TAX_STR_CD=B.TAX_STR_CD ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, tax_struct_cd);
				resultset = stmtement.executeQuery();
				while(resultset.next())
				{
					double factor = resultset.getDouble(1);
					String temp_factor=resultset.getString(1)==null?"":resultset.getString(1);
					String tax_on = resultset.getString(2)==null?"":resultset.getString(2);
					String tax_on_cd = resultset.getString(3)==null?"":resultset.getString(3);
					tax_str_cd = resultset.getString(4)==null?"":resultset.getString(4);
					tax_str_dt = resultset.getString(5)==null?"":resultset.getString(5);
					String tax_code = resultset.getString(6)==null?"":resultset.getString(6);
					tax_struct_dtl = resultset.getString(7)==null?"":resultset.getString(7);
					
					if(tax_on.equals("2"))
					{
						double tax_on_factor = 0;
						query="SELECT FACTOR "
								+ "FROM FMS_TAX_STRUCTURE_DTL A "
								+ "WHERE TAX_STR_CD=? "
								+ "AND APP_DATE=TO_DATE(?,'DD/MM/YYYY') AND TAX_CODE=?";
						stmtement1 = conn.prepareStatement(query);
						stmtement1.setString(1, tax_str_cd);
						stmtement1.setString(2, tax_str_dt);
						stmtement1.setString(3, tax_on_cd);
						resultset1 = stmtement1.executeQuery();
						if(resultset1.next())
						{
							tax_on_factor = resultset1.getDouble(1);
						}
						stmtement1.close();
						resultset1.close();
						
						tax_factor+=((tax_on_factor/100) * (factor/100)) * 100;
						
					}
					else
					{
						tax_factor+=factor; 	
					}	
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		return tax_factor;
	}
	
	public Double TaxSubFactor(Connection conn,String tax_struct_cd, String tax_code) throws Exception
	{
		String function_nm="TaxFactor()";
		double amt = 0;
		double tax_factor=0;
		String tax_str_cd="";
		String tax_str_dt="";
		
		try
		{
			if(conn!=null)
			{
				query="SELECT A.FACTOR, A.TAX_ON, A.TAX_ON_CD, A.TAX_STR_CD, TO_CHAR(A.APP_DATE,'DD/MM/YYYY') "
						+ "FROM FMS_TAX_STRUCTURE_DTL A "
						+ "WHERE A.TAX_STR_CD=? AND A.TAX_CODE=? ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, tax_struct_cd);
				stmtement.setString(2, tax_code);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					double factor = resultset.getDouble(1);
					String temp_factor=resultset.getString(1)==null?"":resultset.getString(1);
					String tax_on = resultset.getString(2)==null?"":resultset.getString(2);
					String tax_on_cd = resultset.getString(3)==null?"":resultset.getString(3);
					tax_str_cd = resultset.getString(4)==null?"":resultset.getString(4);
					tax_str_dt = resultset.getString(5)==null?"":resultset.getString(5);
					
					if(tax_on.equals("2"))
					{
						double tax_on_factor = 0;
						query="SELECT FACTOR "
								+ "FROM FMS_TAX_STRUCTURE_DTL A "
								+ "WHERE TAX_STR_CD=? "
								+ "AND APP_DATE=TO_DATE(?,'DD/MM/YYYY') AND TAX_CODE=?";
						stmtement1 = conn.prepareStatement(query);
						stmtement1.setString(1, tax_str_cd);
						stmtement1.setString(2, tax_str_dt);
						stmtement1.setString(3, tax_on_cd);
						resultset1 = stmtement1.executeQuery();
						if(resultset1.next())
						{
							tax_on_factor = resultset1.getDouble(1);
						}
						stmtement1.close();
						resultset1.close();
						
						tax_factor=((tax_on_factor/100) * (factor/100)) * 100;
						
					}
					else
					{
						tax_factor=factor; 	
					}	
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		return tax_factor;
	}
}
