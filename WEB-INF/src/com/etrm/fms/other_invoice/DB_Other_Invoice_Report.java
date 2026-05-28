package com.etrm.fms.other_invoice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.etrm.fms.util.CurrencyUtil;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

public class DB_Other_Invoice_Report 
{
	String db_src_file_name="DB_Other_Invoice_Report.java";
	
	Connection conn; 
	PreparedStatement stmt;
	PreparedStatement stmt0;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt5;
	PreparedStatement stmt6;
	PreparedStatement stmt7;
	PreparedStatement stmt8;
	ResultSet rset;
	ResultSet rset0; 
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
	ResultSet rset6;
	ResultSet rset7;
	ResultSet rset8; 
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	NumberFormat nf0 = new DecimalFormat("###########0.0");
	
	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();
	CurrencyUtil es=new CurrencyUtil();
	
	public void init()
	{
		String function_nm="init()";
		
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
	    			if(callFlag.equalsIgnoreCase("OTH_INV_GST_REGISTER")) 
	    			{
	    				getSegment();
	    				getOtherInvoiceDtl();
	    			}
	    			if(callFlag.equalsIgnoreCase("OTH_INV_SUMMARY"))
	    			{
	    				getSegment();
	    				getOtherInvoiceSumary();
	    			}
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
	    	if(rset0 != null){try{rset0.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset1 != null){try{rset1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset2 != null){try{rset2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset3 != null){try{rset3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset4 != null){try{rset4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset5 != null){try{rset5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset6 != null){try{rset6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset7 != null){try{rset7.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset8 != null){try{rset8.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt0 != null){try{stmt0.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt6 != null){try{stmt6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt7 != null){try{stmt7.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt8 != null){try{stmt8.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}

	public void getSegment()
	{
		String function_nm="getSegment()";
		try
		{
			if(callFlag.equalsIgnoreCase("OTH_INV_SUMMARY"))
			{
				VSEGMENT_NM.add("RCM Invoice(Cost Recharge)");
				VSEGMENT_NM.add("NPR");
				VSEGMENT_NM.add("AHPL Invoice (AHPL Revenue Share)");

				VSEGMENT_TYPE.add("COSTR");
				VSEGMENT_TYPE.add("NPR");
				VSEGMENT_TYPE.add("AHPL");
				
			}
			VSEGMENT_NM.add("Berthing Invoice (HPPL Shipping Agent)");
			VSEGMENT_NM.add("PFA Fees Invoice (HPPL-SEIPL)");
			VSEGMENT_NM.add("Cost Recharge HPPL");
			VSEGMENT_NM.add("Scrap Fixed Asset");
			VSEGMENT_NM.add("GNA Invoice");
			VSEGMENT_NM.add("ReExport Invoice");
			
			VSEGMENT_TYPE.add("HSA");
			VSEGMENT_TYPE.add("HS");
			VSEGMENT_TYPE.add("COSTRH");
			VSEGMENT_TYPE.add("SFA");
			VSEGMENT_TYPE.add("GA");
			VSEGMENT_TYPE.add("RXP");
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getOtherInvoiceDtl()
	{
		String function_nm="getOtherInvoiceDtl()";
		try
		{
			String from_dt="01/"+month+"/"+year;
			String to_dt=utilDate.getLastDateOfMonth(month_to, year_to);
			
			if(invoice_type.equals("0"))
			{
				VDISP_SEGMENT_NM=VSEGMENT_NM;
				VDISP_SEGMENT_TYPE=VSEGMENT_TYPE;
			}
			else if(invoice_type.equals("COSTR"))
			{
				VDISP_SEGMENT_NM.add("RCM Invoice(Cost Recharge)");
				VDISP_SEGMENT_TYPE.add("COSTR");
			}
			else if(invoice_type.equals("NPR"))
			{
				VDISP_SEGMENT_NM.add("NPR");
				VDISP_SEGMENT_TYPE.add("NPR");
			}
			else if(invoice_type.equals("HSA"))
			{
				VDISP_SEGMENT_NM.add("Berthing Invoice (HPPL Shipping Agent)");
				VDISP_SEGMENT_TYPE.add("HSA");
			}
			else if(invoice_type.equals("HS"))
			{
				VDISP_SEGMENT_NM.add("PFA Fees Invoice (HPPL-SEIPL)");
				VDISP_SEGMENT_TYPE.add("HS");
			}
			else if(invoice_type.equals("COSTRH"))
			{
				VDISP_SEGMENT_NM.add("Cost Recharge HPPL");
				VDISP_SEGMENT_TYPE.add("COSTRH");
			}
			else if(invoice_type.equals("SFA"))
			{
				VDISP_SEGMENT_NM.add("Scrap Fixed Asset");
				VDISP_SEGMENT_TYPE.add("SFA");
			}
			else if(invoice_type.equals("GA"))
			{
				VDISP_SEGMENT_NM.add("GNA Invoice");
				VDISP_SEGMENT_TYPE.add("GA");
			}
			else if(invoice_type.equals("RXP"))
			{
				VDISP_SEGMENT_NM.add("ReExport Invoice");
				VDISP_SEGMENT_TYPE.add("RXP");
			}
			
			for(int i=0;i<VDISP_SEGMENT_TYPE.size();i++)
			{
				int index=0;
				String query="SELECT SUPPLIER_CD, VENDOR_CD,FINANCIAL_YEAR,INVOICE_SEQ,INVOICE_NO, "
						+ "TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY') ,SALE_PRICE ,"
						+ "SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_VALUE,GROSS_AMT,TAX_AMT_INR ,"		//TAX_AMT_INR STORES BOTH USD AND INR VALUES
						+ "INVOICE_RAISED_IN,NET_PAYABLE,INVOICE_ID_SEQ,INVOICE_CATEGORY,VENDOR_TYPE , "
						+ "INV_FLAG,REF_NO,CRITERIA "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? "
						+ "AND INVOICE_DT>=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND PDF_INV_DTL IS NOT NULL";
				stmt=conn.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, ""+VDISP_SEGMENT_TYPE.elementAt(i));
				stmt.setString(3, from_dt);
				stmt.setString(4, to_dt);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					
					String supplier_cd=rset.getString(1)==null?"":rset.getString(1);
					String vendor_cd=rset.getString(2)==null?"":rset.getString(2);
					String financial_year=rset.getString(3)==null?"":rset.getString(3);
					String inv_seq_no=rset.getString(4)==null?"":rset.getString(4);
					String invoice_no=rset.getString(5)==null?"":rset.getString(5);
					String invoice_dt=rset.getString(6)==null?"":rset.getString(6);
					String inv_due_dt=rset.getString(7)==null?"":rset.getString(7);
					double sale_price = rset.getDouble(8);
					String sales_price_unit=rset.getString(9)==null?"":rset.getString(9);
					double sale_amt = rset.getDouble(10);
					String exchng_rate_cd=rset.getString(11)==null?"":rset.getString(11);
					double exchng_raste = rset.getDouble(12); 
					double gross_amt = rset.getDouble(13); 
					double tax_amt = rset.getDouble(14); 
					String invoice_raised_in=rset.getString(15)==null?"":rset.getString(15);
					double net_payable_amt = rset.getDouble(16);
					String inv_id=rset.getString(17)==null?"":rset.getString(17);
					String inv_category=rset.getString(18)==null?"":rset.getString(18);
					String vendor_type=rset.getString(19)==null?"":rset.getString(19);
					String inv_flag=rset.getString(20)==null?"":rset.getString(20);
					String ref_no=rset.getString(21)==null?"":rset.getString(21);
					String criteria=rset.getString(22)==null?"":rset.getString(22);
//					if(VDISP_SEGMENT_TYPE.elementAt(i).equals("RXP"))
//					{
//						System.out.println(VDISP_SEGMENT_TYPE.elementAt(i)+"--"+VSEGMENT_TYPE.elementAt(i)+"--"+invoice_no);
//					}
					
					String tax_cd="";
					String tax_struct_cd="";
					String sac_cd="";
					String vendor_supp_inv_ref_no="";
					String pacer_no="";
					String vessel_cd="";
					String vessel_flag="";
					String m3_qty="";
					String grt="";
					String importer="";
					String berth_time_slot="";
					String hsn_cd="";
					String sale_no="";
					String gate_pass_no="";
					String purchase_no="";
					String pur_ref="";
					String uam_no="";
					
					String query1="SELECT TAX_STRUCT_CD, HSN_CODE, SAC_CODE,PURCHASE_NO,REFERENCE_NO, "
							+ "VESSEL_CD, VESSEL_AGENT,GRT,TAX_CD,SALE_NO,PACER_NO, VENDOR_SUPP_INV_REF,UAM_NO, "
							+ "VESSEL_FLAG,QUANTITY,IMPORTER,TIME_SLOTS_BERTHING,GATE_PASS_NO "
							+ "FROM FMS_OTH_INVOICE_DTL "
							+ "WHERE COMPANY_CD=? AND VENDOR_CD=? AND SUPPLIER_CD=? AND INVOICE_TYPE=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? ";
					stmt1=conn.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, vendor_cd);
					stmt1.setString(3, supplier_cd);
					stmt1.setString(4, ""+VDISP_SEGMENT_TYPE.elementAt(i));
					stmt1.setString(5,inv_seq_no);
					stmt1.setString(6, financial_year);
					rset1=stmt1.executeQuery();
					if(VDISP_SEGMENT_TYPE.elementAt(i).equals("SFA") || VDISP_SEGMENT_TYPE.elementAt(i).equals("GA") || VDISP_SEGMENT_TYPE.elementAt(i).equals("RXP"))
					{
						while(rset1.next())
						{
							index+=1;
							tax_struct_cd=rset1.getString(1)==null?"":rset1.getString(1);
							hsn_cd=rset1.getString(2)==null?"":rset1.getString(2);
							sac_cd=rset1.getString(3)==null?"":rset1.getString(3);
							purchase_no=rset1.getString(4)==null?"":rset1.getString(4);
							pur_ref=rset1.getString(5)==null?"":rset1.getString(5);
							vessel_cd=rset1.getString(6)==null?"":rset1.getString(6);
							String vessel_agent=rset1.getString(7)==null?"":rset1.getString(7);
							grt=rset1.getString(8)==null?"":rset1.getString(8);
							tax_cd=rset1.getString(9)==null?"":rset1.getString(9);
							sale_no=rset1.getString(10)==null?"":rset1.getString(10);
							pacer_no=rset1.getString(11)==null?"":rset1.getString(11);
							vendor_supp_inv_ref_no=rset1.getString(12)==null?"":rset1.getString(12);
							uam_no=rset1.getString(13)==null?"":rset1.getString(13);
							vessel_flag=rset1.getString(14)==null?"":rset1.getString(14);
							m3_qty=rset1.getString(15)==null?"":nf.format(rset1.getDouble(15));
							importer=rset1.getString(16)==null?"":rset1.getString(16);
							berth_time_slot=rset1.getString(17)==null?"":rset1.getString(17);
							gate_pass_no=rset1.getString(18)==null?"":rset1.getString(18);
							
							String gst_tin_no = getEntity_Gst_Tin_No(vendor_cd, "V");
							String sac_code=getSac_Code(sac_cd);
							String factor=getTaxFactor(tax_struct_cd);
							String vessel_nm=utilBean.getShipName(conn, vessel_cd);
							
							double igst_amt=0;
							double cgst_amt=0;		//cgst_amt=sgst_amt=(tax_amt/2)
							String igst_factor="";
							String cgst_factor="";
							
							if(tax_cd.equals("I"))
							{
								igst_amt=tax_amt;
								igst_factor=factor;
							}
							else
							{
								
								cgst_amt=(tax_amt)/2;
								cgst_factor=factor;
							}
							
							if(invoice_raised_in.equals("2"))
							{
								VGROSS_AMT_USD.add(nf.format(Math.abs(gross_amt)));
								VIGST_AMT_USD.add(nf.format(Math.abs(igst_amt)));
								VCGST_AMT_USD.add(nf.format(Math.abs(cgst_amt)));
								VSGST_AMT_USD.add(nf.format(Math.abs(cgst_amt)));
								
								VIGST_FACTOR_USD.add(igst_factor);
								VCGST_FACTOR_USD.add(cgst_factor);
								VSGST_FACTOR_USD.add(cgst_factor);
								
								VGROSS_AMT_INR.add("");
								VIGST_AMT_INR.add("");
								VCGST_AMT_INR.add("");
								VSGST_AMT_INR.add("");
								VIGST_FACTOR_INR.add("");
								VCGST_FACTOR_INR.add("");
								VSGST_FACTOR_INR.add("");
							}
							else
							{
								VGROSS_AMT_INR.add(nf.format(Math.abs(gross_amt)));
								VIGST_AMT_INR.add(nf.format(Math.abs(igst_amt)));
								VCGST_AMT_INR.add(nf.format(Math.abs(cgst_amt)));
								VSGST_AMT_INR.add(nf.format(Math.abs(cgst_amt)));
								
								VIGST_FACTOR_INR.add(igst_factor);
								VCGST_FACTOR_INR.add(cgst_factor);
								VSGST_FACTOR_INR.add(cgst_factor);

								VGROSS_AMT_USD.add("");
								VIGST_AMT_USD.add("");
								VCGST_AMT_USD.add("");
								VSGST_AMT_USD.add("");
								VIGST_FACTOR_USD.add("");
								VCGST_FACTOR_USD.add("");
								VSGST_FACTOR_USD.add("");
							}
							
							VSUPPLIER_CD.add(supplier_cd);
							VSUPPLIER_NM.add(""+getEntity_abbr(supplier_cd,"S"));
							VVENDOR_CD.add(vendor_cd);
							if(vendor_type.equals("S"))
							{
								VVENDOR_NM.add(""+getEntity_nm(vendor_cd,"S"));
							}
							else
							{
								VVENDOR_NM.add(""+getEntity_nm(vendor_cd,"V"));
							}
							VGST_TIN_NO.add(gst_tin_no);
							VINVOICE_NO.add(invoice_no);
							VINVOICE_DT.add(invoice_dt);
							VSAC_CD.add(sac_code);
							VVENDOR_SUPP_INV_REF_NO.add(vendor_supp_inv_ref_no);
							VPACER_NO.add(pacer_no);
							VVESSEL_FLG.add(vessel_flag);
							VVESSEL_NM.add(vessel_nm);
							VQTY.add(m3_qty);
							VGRT.add(grt);
							VIMPORTER.add(importer);
							VBERTH_TIME_SLOT.add(berth_time_slot);
							VINVOICE_DUE_DT.add(inv_due_dt);
							VSALES_PRICE.add(nf.format(Math.abs(sale_price)));
							VCHRG_AMT.add(nf.format(Math.abs(sale_amt)));
							VSALE_NO.add(sale_no);
							VHSN_CD.add(hsn_cd);
							VGATE_PASS_NO.add(gate_pass_no);
							VPUR_NO.add(purchase_no);
							VREFERENCE.add(pur_ref);
							VUAM.add(uam_no);
						}
						rset1.close();
						stmt1.close();
					}
					else
					{
						if(rset1.next())
						{
							index+=1;
							tax_struct_cd=rset1.getString(1)==null?"":rset1.getString(1);
							hsn_cd=rset1.getString(2)==null?"":rset1.getString(2);
							sac_cd=rset1.getString(3)==null?"":rset1.getString(3);
							purchase_no=rset1.getString(4)==null?"":rset1.getString(4);
							pur_ref=rset1.getString(5)==null?"":rset1.getString(5);
							vessel_cd=rset1.getString(6)==null?"":rset1.getString(6);
							String vessel_agent=rset1.getString(7)==null?"":rset1.getString(7);
							grt=rset1.getString(8)==null?"":rset1.getString(8);
							tax_cd=rset1.getString(9)==null?"":rset1.getString(9);
							sale_no=rset1.getString(10)==null?"":rset1.getString(10);
							pacer_no=rset1.getString(11)==null?"":rset1.getString(11);
							vendor_supp_inv_ref_no=rset1.getString(12)==null?"":rset1.getString(12);
							uam_no=rset1.getString(13)==null?"":rset1.getString(13);
							vessel_flag=rset1.getString(14)==null?"":rset1.getString(14);
							m3_qty=rset1.getString(15)==null?"":nf.format(rset1.getDouble(15));
							importer=rset1.getString(16)==null?"":rset1.getString(16);
							berth_time_slot=rset1.getString(17)==null?"":rset1.getString(17);
							gate_pass_no=rset1.getString(18)==null?"":rset1.getString(18);
						}
						rset1.close();
						stmt1.close();
						
						String gst_tin_no = getEntity_Gst_Tin_No(vendor_cd, "V");
						String sac_code=getSac_Code(sac_cd);
						String factor=getTaxFactor(tax_struct_cd);
						String vessel_nm=utilBean.getShipName(conn, vessel_cd);
						
						double igst_amt=0;
						double cgst_amt=0;		//cgst_amt=sgst_amt=(tax_amt/2)
						String igst_factor="";
						String cgst_factor="";
						
						if(tax_cd.equals("I"))
						{
							igst_amt=tax_amt;
							igst_factor=factor;
						}
						else
						{
							
							cgst_amt=(tax_amt)/2;
							cgst_factor=factor;
						}
						
						if(invoice_raised_in.equals("2"))
						{
							VGROSS_AMT_USD.add(nf.format(Math.abs(gross_amt)));
							VIGST_AMT_USD.add(nf.format(Math.abs(igst_amt)));
							VCGST_AMT_USD.add(nf.format(Math.abs(cgst_amt)));
							VSGST_AMT_USD.add(nf.format(Math.abs(cgst_amt)));
							
							VIGST_FACTOR_USD.add(igst_factor);
							VCGST_FACTOR_USD.add(cgst_factor);
							VSGST_FACTOR_USD.add(cgst_factor);
							
							VGROSS_AMT_INR.add("");
							VIGST_AMT_INR.add("");
							VCGST_AMT_INR.add("");
							VSGST_AMT_INR.add("");
							VIGST_FACTOR_INR.add("");
							VCGST_FACTOR_INR.add("");
							VSGST_FACTOR_INR.add("");
						}
						else
						{
							VGROSS_AMT_INR.add(nf.format(Math.abs(gross_amt)));
							VIGST_AMT_INR.add(nf.format(Math.abs(igst_amt)));
							VCGST_AMT_INR.add(nf.format(Math.abs(cgst_amt)));
							VSGST_AMT_INR.add(nf.format(Math.abs(cgst_amt)));
							
							VIGST_FACTOR_INR.add(igst_factor);
							VCGST_FACTOR_INR.add(cgst_factor);
							VSGST_FACTOR_INR.add(cgst_factor);

							VGROSS_AMT_USD.add("");
							VIGST_AMT_USD.add("");
							VCGST_AMT_USD.add("");
							VSGST_AMT_USD.add("");
							VIGST_FACTOR_USD.add("");
							VCGST_FACTOR_USD.add("");
							VSGST_FACTOR_USD.add("");
						}
						
						VSUPPLIER_CD.add(supplier_cd);
						VSUPPLIER_NM.add(""+getEntity_abbr(supplier_cd,"S"));
						VVENDOR_CD.add(vendor_cd);
						if(vendor_type.equals("S"))
						{
							VVENDOR_NM.add(""+getEntity_nm(vendor_cd,"S"));
						}
						else
						{
							VVENDOR_NM.add(""+getEntity_nm(vendor_cd,"V"));
						}
						VGST_TIN_NO.add(gst_tin_no);
						VINVOICE_NO.add(invoice_no);
						VINVOICE_DT.add(invoice_dt);
						VSAC_CD.add(sac_code);
						VVENDOR_SUPP_INV_REF_NO.add(vendor_supp_inv_ref_no);
						VPACER_NO.add(pacer_no);
						VVESSEL_FLG.add(vessel_flag);
						VVESSEL_NM.add(vessel_nm);
						VQTY.add(m3_qty);
						VGRT.add(grt);
						VIMPORTER.add(importer);
						VBERTH_TIME_SLOT.add(berth_time_slot);
						VINVOICE_DUE_DT.add(inv_due_dt);
						VSALES_PRICE.add(nf.format(Math.abs(sale_price)));
						VCHRG_AMT.add(nf.format(Math.abs(sale_amt)));
						VSALE_NO.add(sale_no);
						VHSN_CD.add(hsn_cd);
						VGATE_PASS_NO.add(gate_pass_no);
						VPUR_NO.add(purchase_no);
						VREFERENCE.add(pur_ref);
						VUAM.add(uam_no);
					}
				}
				rset.close();
				stmt.close();
				VINDEX.add(index);
			}
			
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String getEntity_nm(String cd,String type)
	{
		String function_nm="getEntity_nm()";
		String entity_nm="";
		try
		{
			String queryString0 = "SELECT A.ENTITY_NAME,A.ENTITY_ABBR "
					+ "FROM FMS_OTH_ENTITY_MST A "
					+ "WHERE A.ENTITY_CD=? AND A.ENTITY_TYPE = ? "
					+ "AND EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS_OTH_ENTITY_MST B WHERE A.ENTITY_TYPE = B.ENTITY_TYPE AND A.ENTITY_CD=B.ENTITY_CD) ";
			stmt0 = conn.prepareStatement(queryString0);
			stmt0.setString(1, cd);
			stmt0.setString(2, type);
			rset0 = stmt0.executeQuery();
			while(rset0.next()) 
			{
				entity_nm = rset0.getString(1)==null?"":rset0.getString(1);
			}
			rset0.close();
			stmt0.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return entity_nm;
	}
	
	public String getEntity_abbr(String cd,String type)
	{
		String function_nm="getEntity_abbr()";
		String entity_abbr="";
		try
		{
			String queryString0 = "SELECT A.ENTITY_NAME,A.ENTITY_ABBR "
					+ "FROM FMS_OTH_ENTITY_MST A "
					+ "WHERE A.ENTITY_CD=? AND A.ENTITY_TYPE = ? "
					+ "AND EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS_OTH_ENTITY_MST B WHERE A.ENTITY_TYPE = B.ENTITY_TYPE AND A.ENTITY_CD=B.ENTITY_CD) ";
			stmt0 = conn.prepareStatement(queryString0);
			stmt0.setString(1, cd);
			stmt0.setString(2, type);
			rset0 = stmt0.executeQuery();
			while(rset0.next()) 
			{
				entity_abbr = rset0.getString(2)==null?"":rset0.getString(2);
			}
			rset0.close();
			stmt0.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return entity_abbr;
	}
	
	public String getEntity_Gst_Tin_No(String cd,String type)
	{
		String function_nm="getEntity_Gst_Tin_No()";
		String gst_tin_no="";
		try
		{
			String queryString0 = "SELECT A.GSTIN_NO "
					+ "FROM FMS_OTH_ENTITY_MST A "
					+ "WHERE A.ENTITY_CD=? AND A.ENTITY_TYPE = ? "
					+ "AND EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS_OTH_ENTITY_MST B WHERE A.ENTITY_TYPE = B.ENTITY_TYPE AND A.ENTITY_CD=B.ENTITY_CD) ";
			stmt0 = conn.prepareStatement(queryString0);
			stmt0.setString(1, cd);
			stmt0.setString(2, type);
			rset0 = stmt0.executeQuery();
			while(rset0.next()) 
			{
				gst_tin_no = rset0.getString(1)==null?"":rset0.getString(1);
			}
			rset0.close();
			stmt0.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return gst_tin_no;
	}
	
	public String getSac_Code(String sac_cd)
	{
		String function_nm="getSac_Code()";
		String sac_code="";
		try
		{
			String query3="SELECT SAC_CODE FROM FMS_SAC_MST "
					+ "WHERE SAC_CD=? ";
			stmt0=conn.prepareStatement(query3);
			stmt0.setString(1, sac_cd);
			rset0=stmt0.executeQuery();
			if(rset0.next())
			{
				sac_code=rset0.getString(1)==null?"":rset0.getString(1);
			}
			rset0.close();
			stmt0.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return sac_code;
	}
	
	public String getTaxFactor(String tax_struct_cd)
	{
		String function_nm="getTaxFactor()";
		String factor="";
		try
		{
			String query2="SELECT FACTOR FROM FMS_TAX_STRUCTURE_DTL "
					+ "WHERE TAX_STR_CD=? ";
			stmt0=conn.prepareStatement(query2);
			stmt0.setString(1, tax_struct_cd);
			rset0=stmt0.executeQuery();
			if(rset0.next())
			{
				factor=rset0.getString(1)==null?"":rset0.getString(1);
			}
			rset0.close();
			stmt0.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return factor;
	}
	
	public void getOtherInvoiceSumary()
	{
		String function_nm="getOtherInvoiceSumary()";
		try
		{
			String from_dt="01/"+month+"/"+year;
			String to_dt=utilDate.getLastDateOfMonth(month, year);
			
			if(invoice_type.equals("0") || invoice_type.equals(""))
			{
				VDISP_SEGMENT_NM=VSEGMENT_NM;
				VDISP_SEGMENT_TYPE=VSEGMENT_TYPE;
			}
			else if(invoice_type.equals("COSTR"))
			{
				VDISP_SEGMENT_NM.add("RCM Invoice(Cost Recharge)");
				VDISP_SEGMENT_TYPE.add("COSTR");
			}
			else if(invoice_type.equals("NPR"))
			{
				VDISP_SEGMENT_NM.add("NPR");
				VDISP_SEGMENT_TYPE.add("NPR");
			}
			else if(invoice_type.equals("HSA"))
			{
				VDISP_SEGMENT_NM.add("Berthing Invoice (HPPL Shipping Agent)");
				VDISP_SEGMENT_TYPE.add("HSA");
			}
			else if(invoice_type.equals("HS"))
			{
				VDISP_SEGMENT_NM.add("PFA Fees Invoice (HPPL-SEIPL)");
				VDISP_SEGMENT_TYPE.add("HS");
			}
			else if(invoice_type.equals("COSTRH"))
			{
				VDISP_SEGMENT_NM.add("Cost Recharge HPPL");
				VDISP_SEGMENT_TYPE.add("COSTRH");
			}
			else if(invoice_type.equals("SFA"))
			{
				VDISP_SEGMENT_NM.add("Scrap Fixed Asset");
				VDISP_SEGMENT_TYPE.add("SFA");
			}
			else if(invoice_type.equals("AHPL"))
			{
				VDISP_SEGMENT_NM.add("AHPL Invoice (AHPL Revenue Share)");
				VDISP_SEGMENT_TYPE.add("AHPL");
			}
			else if(invoice_type.equals("GA"))
			{
				VDISP_SEGMENT_NM.add("GNA Invoice");
				VDISP_SEGMENT_TYPE.add("GA");
			}
			else if(invoice_type.equals("RXP"))
			{
				VDISP_SEGMENT_NM.add("ReExport Invoice");
				VDISP_SEGMENT_TYPE.add("RXP");
			}
			
			for(int i=0;i<VDISP_SEGMENT_TYPE.size();i++)
			{
				int index=0;
				String query="SELECT VENDOR_CD, INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'), "
						+ "EXCHG_RATE_VALUE, GROSS_AMT, TAX_AMT_INR, INVOICE_RAISED_IN, NET_PAYABLE,INV_FLAG, "
						+ "CHECKED_FLAG,CHECKED_BY,TO_CHAR(CHECKED_DT,'DD/MM/YYYY'),APPROVED_FLAG,APPROVED_BY,TO_CHAR(APPROVED_DT,'DD/MM/YYYY'),"
						+ "VENDOR_TYPE,CRITERIA "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD=? "
						+ "AND INVOICE_DT>=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND INVOICE_TYPE=? "; 
				stmt=conn.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, from_dt);
				stmt.setString(3, to_dt);
				stmt.setString(4, ""+VDISP_SEGMENT_TYPE.elementAt(i));
				rset=stmt.executeQuery();
				while(rset.next())
				{
					index+=1;
					String vendor_cd = rset.getString(1)==null?"":rset.getString(1);
					String invoice_no=rset.getString(2)==null?"":rset.getString(2);
					String invoice_dt=rset.getString(3)==null?"":rset.getString(3);
					String exchng_rate=rset.getString(4)==null?"":nf.format(rset.getDouble(4));
					double gross_amt=rset.getDouble(5);
					double tax_amt=rset.getDouble(6);
					String inv_raised_in=rset.getString(7)==null?"":rset.getString(7);
					double net_payable=rset.getDouble(8);
					String inv_flag=rset.getString(9)==null?"":rset.getString(9);
					String checked_flg=rset.getString(10)==null?"":rset.getString(10);
					String checked_by=rset.getString(11)==null?"":rset.getString(11);
					String checked_dt=rset.getString(12)==null?"":rset.getString(12);
					String approved_flg=rset.getString(13)==null?"":rset.getString(13);
					String approved_by=rset.getString(14)==null?"":rset.getString(14);
					String approved_dt=rset.getString(15)==null?"":rset.getString(15);
					String vendor_type=rset.getString(16)==null?"":rset.getString(16);
					String criteria=rset.getString(17)==null?"":rset.getString(17);
					
					VINVOICE_TYPE.add(""+VDISP_SEGMENT_NM.elementAt(i));
					VVENDOR_CD.add(vendor_cd);
					if(vendor_type.equals("S"))
					{
						VVENDOR_NM.add(""+getEntity_nm(vendor_cd,"S"));
					}
					else
					{
						VVENDOR_NM.add(""+getEntity_nm(vendor_cd,"V"));
					}
					VINVOICE_NO.add(invoice_no);
					VINVOICE_DT.add(invoice_dt);
					
					if(inv_raised_in.equals("2"))
					{
						VGROSS_AMT_USD.add(nf.format(Math.abs(gross_amt)));
						VTAX_AMT_USD.add(nf.format(Math.abs(tax_amt)));
						VNET_PAY_USD.add(nf.format(Math.abs(net_payable)));
						
						String gross_amt_inr="";
						String tax_amt_inr="";
						String net_pay_inr="";
						if(!exchng_rate.equals(""))
						{
							gross_amt_inr=nf.format(Math.abs(gross_amt*Double.parseDouble(exchng_rate)));
							tax_amt_inr=nf.format(Math.abs(tax_amt*Double.parseDouble(exchng_rate)));
							net_pay_inr=nf.format(Math.abs(net_payable*Double.parseDouble(exchng_rate)));
						}
						
						VGROSS_AMT_INR.add(gross_amt_inr);
						VTAX_AMT_INR.add(tax_amt_inr);
						VNET_PAY_INR.add(net_pay_inr);
					}
					else
					{
						VGROSS_AMT_INR.add(nf.format(Math.abs(gross_amt)));
						VTAX_AMT_INR.add(nf.format(Math.abs(tax_amt)));
						VNET_PAY_INR.add(nf.format(Math.abs(net_payable)));
						
						VGROSS_AMT_USD.add("");
						VTAX_AMT_USD.add("");
						VNET_PAY_USD.add("");
					}
					
					VCHECKED_FLAG.add(checked_flg);
					VCHECKED_BY.add(""+utilBean.getEmpName(conn,checked_by));
					VCHECKED_DT.add(checked_dt);
					
					VAPPROVED_FLAG.add(approved_flg);
					VAPPROVED_BY.add(""+utilBean.getEmpName(conn, approved_by));
					VAPPROVED_DT.add(approved_dt);
					
					if(inv_flag.equals("CR") || inv_flag.equals("DR"))
					{
						VINV_FLAG.add(inv_flag);
					}
					else
					{
						VINV_FLAG.add("");
					}
					VCRITERIA.add("");
				}
				rset.close();
				stmt.close();
				VINDEX.add(index);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	String comp_abbr = "";
	public void setComp_abbr(String comp_abbr) {this.comp_abbr = comp_abbr;}
	String invoice_type="";
	public void setInvoice_type(String invoice_type) {this.invoice_type=invoice_type;}
	String month="";
	public void setMonth(String month) {this.month=month;}
	String year="";
	public void setYear(String year) {this.year=year;}
	String month_to="";
	public void setMonth_to(String month_to) {this.month_to=month_to;}
	String year_to="";
	public void setYear_to(String year_to) {this.year_to=year_to;}
	
	Vector VSEGMENT_NM=new Vector();
	Vector VSEGMENT_TYPE=new Vector();
	Vector VDISP_SEGMENT_NM=new Vector();
	Vector VDISP_SEGMENT_TYPE=new Vector();
	
	Vector VSUPPLIER_CD=new Vector();
	Vector VSUPPLIER_NM = new Vector();
	Vector VVENDOR_CD = new Vector();
	Vector VVENDOR_NM = new Vector();
	Vector VGST_TIN_NO = new Vector();
	Vector VINVOICE_NO = new Vector();
	Vector VINVOICE_DT = new Vector();
	Vector VGROSS_AMT_INR = new Vector();
	Vector VGROSS_AMT_USD = new Vector();
	Vector VIGST_AMT_INR = new Vector();
	Vector VIGST_AMT_USD = new Vector();
	Vector VCGST_AMT_INR = new Vector();
	Vector VCGST_AMT_USD = new Vector();
	Vector VSGST_AMT_INR = new Vector();
	Vector VSGST_AMT_USD = new Vector();
	Vector VIGST_FACTOR_INR = new Vector();
	Vector VIGST_FACTOR_USD = new Vector();
	Vector VCGST_FACTOR_INR = new Vector();
	Vector VCGST_FACTOR_USD = new Vector();
	Vector VSGST_FACTOR_INR = new Vector();
	Vector VSGST_FACTOR_USD = new Vector();
	Vector VSAC_CD = new Vector();
	Vector VVENDOR_SUPP_INV_REF_NO = new Vector();
	Vector VPACER_NO = new Vector();
	Vector VVESSEL_NM = new Vector();
	Vector VVESSEL_FLG = new Vector();
	Vector VQTY = new Vector();
	Vector VGRT = new Vector();
	Vector VIMPORTER = new Vector();
	Vector VBERTH_TIME_SLOT = new Vector();
	Vector VSALES_PRICE = new Vector();
	Vector VINVOICE_DUE_DT = new Vector();
	Vector VCHRG_AMT = new Vector();
	Vector VSALE_NO = new Vector();
	Vector VHSN_CD = new Vector();
	Vector VGATE_PASS_NO = new Vector();
	Vector VPUR_NO = new Vector();
	Vector VREFERENCE = new Vector();
	Vector VUAM = new Vector();
	Vector VINDEX = new Vector();
	
	Vector VINVOICE_TYPE = new Vector();
	Vector VTAX_AMT_INR = new Vector();
	Vector VTAX_AMT_USD = new Vector();
	Vector VNET_PAY_INR = new Vector();
	Vector VNET_PAY_USD = new Vector();
	Vector VCHECKED_FLAG = new Vector();
	Vector VCHECKED_BY = new Vector();
	Vector VCHECKED_DT = new Vector();
	Vector VAPPROVED_FLAG = new Vector();
	Vector VAPPROVED_BY = new Vector();
	Vector VAPPROVED_DT = new Vector();
	Vector VINV_FLAG = new Vector();
	Vector VCRITERIA = new Vector();
	
	public Vector getVSEGMENT_NM() {return VSEGMENT_NM;}
	public Vector getVSEGMENT_TYPE() {return VSEGMENT_TYPE;} 
	public Vector getVDISP_SEGMENT_NM() {return VDISP_SEGMENT_NM;}
	public Vector getVDISP_SEGMENT_TYPE() {return VDISP_SEGMENT_TYPE;} 

	public Vector getVSUPPLIER_CD() {return VSUPPLIER_CD;}
	public Vector getVSUPPLIER_NM() {return VSUPPLIER_NM;}
	public Vector getVVENDOR_CD() {return VVENDOR_CD;}
	public Vector getVVENDOR_NM() {return VVENDOR_NM;}
	public Vector getVGST_TIN_NO() {return VGST_TIN_NO;}
	public Vector getVINVOICE_NO() {return VINVOICE_NO;}
	public Vector getVINVOICE_DT() {return VINVOICE_DT;}
	public Vector getVGROSS_AMT_INR() {return VGROSS_AMT_INR;}
	public Vector getVGROSS_AMT_USD() {return VGROSS_AMT_USD;}
	public Vector getVIGST_AMT_INR() {return VIGST_AMT_INR;}
	public Vector getVIGST_AMT_USD() {return VIGST_AMT_USD;}
	public Vector getVCGST_AMT_INR() {return VCGST_AMT_INR;}
	public Vector getVCGST_AMT_USD() {return VCGST_AMT_USD;}
	public Vector getVSGST_AMT_INR() {return VSGST_AMT_INR;}
	public Vector getVSGST_AMT_USD() {return VSGST_AMT_USD;}
	public Vector getVIGST_FACTOR_INR() {return VIGST_FACTOR_INR;}
	public Vector getVIGST_FACTOR_USD() {return VIGST_FACTOR_USD;}
	public Vector getVCGST_FACTOR_INR() {return VCGST_FACTOR_INR;}
	public Vector getVCGST_FACTOR_USD() {return VCGST_FACTOR_USD;}
	public Vector getVSGST_FACTOR_INR() {return VSGST_FACTOR_INR;}
	public Vector getVSGST_FACTOR_USD() {return VSGST_FACTOR_USD;}
	public Vector getVSAC_CD() {return VSAC_CD;}
	public Vector getVVENDOR_SUPP_INV_REF_NO() {return VVENDOR_SUPP_INV_REF_NO;}
	public Vector getVPACER_NO() {return VPACER_NO;}
	public Vector getVVESSEL_FLG() {return VVESSEL_FLG;}
	public Vector getVVESSEL_NM() {return VVESSEL_NM;}
	public Vector getVQTY() {return VQTY;}
	public Vector getVGRT() {return VGRT;}
	public Vector getVIMPORTER() {return VIMPORTER;}
	public Vector getVBERTH_TIME_SLOT() {return VBERTH_TIME_SLOT;}
	public Vector getVSALES_PRICE() {return VSALES_PRICE;}
	public Vector getVINVOICE_DUE_DT() {return VINVOICE_DUE_DT;}
	public Vector getVCHRG_AMT() {return VCHRG_AMT;}
	public Vector getVSALE_NO() {return VSALE_NO;}
	public Vector getVHSN_CD() {return VHSN_CD;}
	public Vector getVGATE_PASS_NO() {return VGATE_PASS_NO;}
	public Vector getVPUR_NO() {return VPUR_NO;}
	public Vector getVREFERENCE() {return VREFERENCE;}
	public Vector getVUAM() {return VUAM;}
	public Vector getVINDEX() {return VINDEX;}
	
	public Vector getVINVOICE_TYPE() {return VINVOICE_TYPE;}
	public Vector getVTAX_AMT_INR() {return VTAX_AMT_INR;}
	public Vector getVTAX_AMT_USD() {return VTAX_AMT_USD;}
	public Vector getVNET_PAY_INR() {return VNET_PAY_INR;}
	public Vector getVNET_PAY_USD() {return VNET_PAY_USD;}
	public Vector getVCHECKED_FLAG() {return VCHECKED_FLAG;}
	public Vector getVCHECKED_BY() {return VCHECKED_BY;}
	public Vector getVCHECKED_DT() {return VCHECKED_DT;}
	public Vector getVAPPROVED_FLAG() {return VAPPROVED_FLAG;}
	public Vector getVAPPROVED_BY() {return VAPPROVED_BY;}
	public Vector getVAPPROVED_DT() {return VAPPROVED_DT;}
	public Vector getVINV_FLAG() {return VINV_FLAG;}
	public Vector getVCRITERIA() {return VCRITERIA;}
}