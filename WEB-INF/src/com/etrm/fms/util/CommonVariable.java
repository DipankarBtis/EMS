package com.etrm.fms.util;

//Coded By          : Harsh Patel
//Code Reviewed by	:  
//CR Date			: 17/10/2022 
//Status	  		: Developing
public class CommonVariable 
{
	final public static String app_name_sub = "EMS";
	final public static String app_name = "EMS";
	
	final public static String app_protocol = "http";
	//final public static String app_protocol = "https";
	final public static int filter_start_year=2004;
	
	final public static double receivable_tolerance=2; //INR : Only hardcoded in Recievable report View Query
	
	final public static String company_logo_path = "images";
	
	final public static String errorpage_url = "../home/errorpage.jsp";
	final public static String incident_dir = "incident_attachment";
	
	final public static String work_dir = "work_data";
	final public static String market_risk_dir = "market_risk";
	
	final public static String dlng_dir = "DLNG"; // added By AP 20241226
	
	final public static String mail_font_size="x-small";
	final public static String mail_font_family="Calibri";
	
	final public static String sales_inv_path="//unsigned_pdf//sales_invoice//";
	final public static String freeflow_inv_path="//unsigned_pdf//freeflow_invoice//";
	final public static String freeflow_annexure_path="//unsigned_pdf//freeflow_annexure//";
	final public static String crdr_annexure_path="//unsigned_pdf//crdr_annexure//";

	final public static String signed_sales_inv_path="//signed_pdf//sales_invoice//";
	final public static String signed_freeflow_inv_path="//signed_pdf//freeflow_invoice//";
	
	final public static String join_ticket_pdf_path="//join_ticket//";
	final public static String alloc_to_trans_pdf_path="//allocation_to_transporter//";
	
	final public static String nom_to_customer_pdf_path="//daily_nomination//";
	final public static String nom_to_transporter_pdf_path="//daily_nomination//";
	final public static String nom_to_control_room_pdf_path="//daily_nomination//";
	
	final public static String hedge_compliance_pdf_path="//derivatives_reports//";
	
	final public static String ltcora_report_pdf_path="//ltcora_reports//"; //PB
	final public static String sun_xml="//sun_xml//"; //PB
	
	final public static String gta_inv_path="//gta//";
	
	final public static String purchase_inv_path="//purchase//invoice//";
	final public static String purchase_freeflow_inv_path="//purchase//f_flow_invoice//";
	
	final public static String gx_inv_path="//gx//invoice//";
	final public static String gx_freeflow_inv_path="//gx//f_flow_invoice//";
	
	final public static String form_402_dir="FORM_402";
	
	final public static String dlng_sales_inv_path="//unsigned_pdf//dlng_sales_invoice//";
	final public static String dlng_freeflow_inv_path="//unsigned_pdf//dlng_freeflow_invoice//";
	final public static String signed_dlng_sales_inv_path="//signed_pdf//dlng_sales_invoice//";
	final public static String signed_dlng_freeflow_inv_path="//signed_pdf//dlng_freeflow_invoice//";
	
	final public static String dlng_service_inv_path="//unsigned_pdf//dlng_service_invoice//";
	final public static String signed_dlng_service_inv_path="//signed_pdf//dlng_service_invoice//";
	
	final public static String sap_xml = "sap_xml";
	final public static String in_sap_xml = "in_sap_xml";
	final public static String temp_sap_xml = "temp_sap_xml";
	final public static String sap_xml_success = "SUCCESS_FILES";
	
	final public static String derv_inv_path="//unsigned_pdf//derivatives_invoice//"; //added by AP
	final public static String signed_derv_inv_path="//signed_pdf//derivatives_invoice//"; //added by AP
	final public static String derv_remittance_path="//derivatives//remittance//"; //added by AP
	
	final public static String ig_inv_path="//unsigned_pdf//ig_invoice//"; //added by DT
	final public static String signed_ig_inv_path="//signed_pdf//ig_invoice//"; //added by AP 20251125
	
	final public static String security_adv_path="//unsigned_pdf//security//adv//";
	final public static String signed_security_adv_path="//signed_pdf//security//adv//";
	
	final public static String mail_signature="<br><br><font style='font-size:"+mail_font_size+";font-family:"+mail_font_family+";'>Thank You."
			+ "<br><br><b>"+CommonVariable.app_name+" Application Support Team</b></font><br>";
	final public static String mail_disclaimer="<br><br><br><font style='font-size:"+mail_font_size+";font-family:"+mail_font_family+";'>Please maintain confidentiality."
			+ "<br><b>NOTE:</b><i> System Generated Notification - Please do not Reply.</i>";
	
	//following variables are used for GCV NCV calculation HP20240802;
	final public static double multiplying_factor=0.252*1000000;
	final public static double dividing_factor=1.11;
	
	final public static String sf_xml = "SF_XML";
	final public static String sf_xml_outbound = "OUTBOUND";
	
	final public static String sap_reports = "//sap_reports//";		//added by PB
	final public static String dump_sap_xls = sap_reports+"//dump_sap_xls//";	//added by PB
}
