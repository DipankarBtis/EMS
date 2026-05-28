<%@page import="com.etrm.fms.gx.DataBean_Gx_Invoice"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh(clearance)
{
	var prev_clearance = document.forms[0].prev_clearance.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	if(prev_clearance != clearance)
	{
		counterparty_cd="";
	}
	
	var u = document.forms[0].u.value;
		
	var url = "frm_cash_collateral.jsp?counterparty_cd="+counterparty_cd+"&clearance="+clearance+"&u="+u;
			
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

var newWindow;
function openTcsTdsStructMst(tax_app)
{
	var type="P";
		
	var newWindow;
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open("frm_tcs_tds_tax_structure_list.jsp?type="+type+"&tax_app="+tax_app,"Tax Structure List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open("frm_tcs_tds_tax_structure_list.jsp?type="+type+"&tax_app="+tax_app,"Tax Structure List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function setTcsTdsStructDetail(tax_struct_cd,tax_struct_nm,tax_struct_eff_dt,tax_app)
{
	if(tax_app=="TCS")
	{
		var info = "TCS ("+tax_struct_nm+")";
		document.forms[0].tcs_struct_info.value=info
		document.forms[0].tcs_cd.value=tax_struct_cd
		document.forms[0].tcs_dt.value=tax_struct_eff_dt
	}
	else
	{
		var info = "TDS ("+tax_struct_nm+")";
		document.forms[0].tds_struct_info.value=info
		document.forms[0].tds_cd.value=tax_struct_cd
	}
}

function doGenXML(couterpty_cd,clearance,seq_no, seq_rev_no, sap_approval_flag,sec_ref_no,isReversal, isPrintPdf,sec_int_ref)
{
	var u = document.forms[0].u.value;
	
	var url = "rpt_view_advance_sap_approval.jsp?&counterparty_cd="+couterpty_cd+
			"&clearance="+clearance+"&seq_no="+seq_no+"&seq_rev_no="+seq_rev_no+"&sap_approval_flag="+sap_approval_flag+
			"&sec_ref_no="+sec_ref_no+"&isReversal="+isReversal+"&isPrintPdf="+isPrintPdf+"&sec_int_ref="+sec_int_ref+"&u="+u;
			

	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Advance SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Advance SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function printPDF(counterparty_cd,seq_no, seq_rev_no, clearance, isReversal)
{
	var u = document.forms[0].u.value;
	//JD: We we need to change file name here?
	var url = "view_advance_remittance.jsp?counterparty_cd="+counterparty_cd+"&seq_no="+seq_no+
		"&seq_rev_no="+seq_rev_no+"&gx="+clearance+"&isReversal="+isReversal+"&u="+u;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Generate Advance Remittance PDF","top=10,left=10,width=1200,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Generate Advance Remittance PDF","top=10,left=10,width=1200,height=600,scrollbars=1");
	}
}

function openPdfFile(url)
{
	window.open(url);
}

function validateCP()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	if(trim(counterparty_cd)=="")
	{
		alert("Select Counterparty!");
	}
	else
	{
		doClear();
		document.forms[0].counterparty.value=counterparty_cd;
		document.forms[0].counterparty.style.pointerEvents='none';
		$("#AddNewSecurity").modal("show");
	}
}

function doSubmitDealNo()
{
	var sec_type = document.forms[0].sec_type.value;
	var crdr = document.forms[0].crdr.value
	var value = document.forms[0].value.value;
	var clearance = document.forms[0].clearance.value;
	
	var chk_deal = document.forms[0].chk_deal;
	var deal_dtl = document.forms[0].deal_dtl;
	var display_deal_dtl = document.forms[0].display_deal_dtl;
	var cont_deal_type = document.forms[0].cont_deal_type;
	var buy_sell = document.forms[0].temp_buy_sell;
	
	var split_by = document.forms[0].split_by;
	var split_value = document.forms[0].split_value;
	
	const openBtn = document.getElementById('md_openBtn');
	
	var display ="";
	var cont_mapping="";
	var chk_count=parseInt("0");
	var sum_spli_value=parseFloat("0");
	
	var msg="";
	var flag=true;
	
	var prevContDealType="";
	var prevBuySell="";
	
	var DealTypeFlag=true;
	var BuySellFlag=true;
	
	var ltcoraDealType="";
	var tempSplitValue="";
	
	if(chk_deal!=null && chk_deal.length!=undefined)
 	{
  		for(var i=0;i<chk_deal.length;i++)
  		{
   			if(chk_deal[i].checked)
   			{
   				chk_count++;
   				
   				if(chk_count>1)
   				{
   					if(prevContDealType!=cont_deal_type[i].value)
   					{
   						DealTypeFlag=false;
   					}
   					
   					if(prevBuySell!=buy_sell[i].value)
   					{
   						BuySellFlag=false;
   					}
   				}
   				prevContDealType=cont_deal_type[i].value;
   				prevBuySell=buy_sell[i].value;
   				
   				if(cont_deal_type[i].value == "LTCORA")
   				{
   					ltcoraDealType=cont_deal_type[i].value;
   				}
   				
   				deal_dtl[i].disabled=false;
   				cont_mapping=deal_dtl[i].value;
   				document.forms[0].deal_type.value=cont_deal_type[i].value;
   				if(display!="")
   				{
   					display+=", "+display_deal_dtl[i].value;
   				}
   				else
   				{
   					display+=display_deal_dtl[i].value;
   				}
   				
   				if(split_value[i].value!="")
   				{
   					display+=" <font style='background:#ff99ff'>("+split_value[i].value+""+(split_by[1].checked?"%":"")+")</font>";
   				}
   				
   				if(trim(split_value[i].value)!="")
   				{
   					tempSplitValue=split_value[i].value;
   					sum_spli_value = sum_spli_value + parseFloat(split_value[i].value);
   				}
   				else
   				{
   					msg+="Enter Split(Value/Percentage) for "+display_deal_dtl[i].value+"!\n";
   					flag=false;
   				}
   			}
   			else
   			{
   				deal_dtl[i].disabled=true;
			}
  		} 
 	}
 	else if(chk_deal!=null)
 	{
   		if(chk_deal.checked)
     	{	
   			chk_count++;
   			
   			if(cont_deal_type.value == "LTCORA")
			{
				ltcoraDealType=cont_deal_type.value;
			}
   			
   			deal_dtl.disabled=false;
   			cont_mapping=deal_dtl.value;
   			document.forms[0].deal_type.value=cont_deal_type.value;
   			if(display!="")
			{
				display+=", "+display_deal_dtl.value;
			}
			else
			{
				display+=display_deal_dtl.value;
			}
   			
   			if(split_value.value!="")
			{
				display+=" <font style='background:#ff99ff'>("+split_value.value+""+(split_by[1].checked?"%":"")+")</font>";
			}
   			
   			if(trim(split_value.value)!="")
			{
   				tempSplitValue=split_value.value;
				sum_spli_value = sum_spli_value + parseFloat(split_value.value);
			}
   			else
			{
				msg+="Enter Split(Value/Percentage) for "+display_deal_dtl.value+"!\n";
				flag=false;
			}
   		}
   		else
   		{
   			deal_dtl.disabled=true;
		}
 	}
	
	if(!flag && chk_count == 1)
	{
		msg="";
		flag=true;
	}
	
	if(clearance=="I" && chk_count > 1)
	{
		msg+="Please Select Only Single IGX Deal!\n";
		flag=false;
	}
	
	if(!DealTypeFlag)
	{
		msg+="Multiple Deal Type Selection not allowed in single Advance Entry, Please Select Single Deal Type!\n";
		flag=false;
	}
	
	if(!BuySellFlag)
	{
		msg+="You cann't select Buy & Sell Deal in single Advance Entry, Please Select either Sell or Buy Deals!\n";
		flag=false;
	}
	
	if(ltcoraDealType=="LTCORA" && chk_count > 1)
	{
		msg+="Please Select Only Single LTCORA Deal!\n";
		flag=false;
	}
	
	if(flag)
	{
		msg="";
		flag=true;
		
		if(chk_count > 1)
		{
			if(split_by[0].checked)
			{
				if(trim(value)!="")
				{
					if(sum_spli_value != parseFloat(value))
					{
						msg="Sum of Split Value : "+round(sum_spli_value,2)+"\nOriginal Amount : "+value+"\n\nBoth value should be same!";
						flag=false;
					}
				}
				else
				{
					msg="Enter Original Amount!";
					flag=false;
				}
			}
			else if(split_by[1].checked)
			{
				if(sum_spli_value != 100)
				{
					msg="Sum of Split Pecentage : "+sum_spli_value+" should be = 100!";
					flag=false;
				}
			}
		}
		else
		{
			split_by[0].checked=false;
			split_by[1].checked=false;
			
			if(chk_deal!=null && chk_deal.length!=undefined)
		 	{
		  		for(var i=0;i<chk_deal.length;i++)
		  		{
		   			split_value[i].value="";
		  		} 
		 	}
		 	else if(chk_deal!=null)
		 	{
		   		split_value.value="";
		 	}
		}
	}
	
	calcRemBalance();
	
	if(!flag)
	{
		//e.preventDefault();
		//e.stopPropagation();
		//$('#DealNoModel').modal('show');
		alert(msg)
	}
	else
	{
		document.getElementById("DealNoDisplay").innerHTML=display;
		if(display != "")
		{
			document.getElementById("DealNoDisplay").style.display="inline";
		}
		
		if(cont_mapping!="")
		{	
			if((cont_mapping.includes("O") || cont_mapping.includes("Q")) && sec_type=="ADV")
			{
				fetchContPlantDtl(cont_mapping);
				document.getElementById("taxSegment").style.display="";	
				
				if(crdr=="DR")
				{
					document.getElementById("lblVocher").style.display="";	
					document.getElementById("dpVocher").style.display="";
					
					fetchReceiptVoucher(cont_mapping,"");
					
					document.forms[0].crdr.style.pointerEvents="none";
					document.forms[0].crdr.style.background="#e9ecef";
					
					document.forms[0].plant_seq.style.pointerEvents="none";
					document.forms[0].bu_unit.style.pointerEvents="none";
					
					document.forms[0].plant_seq.style.background="#e9ecef";
					document.forms[0].bu_unit.style.background="#e9ecef";
					
					document.getElementById("taxRefesh").style.display="none";
				}
				else
				{
					document.getElementById("lblVocher").style.display="none";	
					document.getElementById("dpVocher").style.display="none";
					
					document.forms[0].crdr.style.pointerEvents="auto";
					document.forms[0].crdr.style.background="";
					
					document.forms[0].plant_seq.style.pointerEvents="auto";
					document.forms[0].bu_unit.style.pointerEvents="auto";
					
					document.forms[0].plant_seq.style.background="";
					document.forms[0].bu_unit.style.background="";	
				}
			}
			else
			{
				if(cont_mapping.includes("X") || cont_mapping.includes("I") || cont_mapping.includes("W"))
				{
					fetchContPlantDtl(cont_mapping);
				}
				document.getElementById("taxSegment").style.display="none";
				
				document.getElementById("taxInfo").innerHTML="";
				document.forms[0].gross_amt.value="";
				document.forms[0].tax_amt.value="";
				document.forms[0].tax_struct_cd.value="";
				document.forms[0].tax_struct_dtl.value="";
				document.getElementById("subTaxSegment").innerHTML="";
				
				document.getElementById("lblVocher").style.display="none";	
				document.getElementById("dpVocher").style.display="none";	
			}
		}
		else
		{
			document.getElementById("taxSegment").style.display="none";
			
			document.getElementById("taxInfo").innerHTML="";
			document.forms[0].gross_amt.value="";
			document.forms[0].tax_amt.value="";
			document.forms[0].tax_struct_cd.value="";
			document.forms[0].tax_struct_dtl.value="";
			document.getElementById("subTaxSegment").innerHTML="";
			
			document.getElementById("lblVocher").style.display="none";	
			document.getElementById("dpVocher").style.display="none";	
		}
		
		
		//$("#DealNoModel").modal("hide");

		// OPEN NEXT MODAL ONLY WHEN VALID
		//const nextModal = new bootstrap.Modal(document.getElementById('AddNewSecurity'));
		//nextModal.show();
		

		// Elements
		const dealEl = document.getElementById('DealNoModel');
		const nextEl = document.getElementById('AddNewSecurity');

		// Get or create instances
		let dealModal = bootstrap.Modal.getInstance(dealEl) || new bootstrap.Modal(dealEl);
		let nextModal = bootstrap.Modal.getInstance(nextEl) || new bootstrap.Modal(nextEl, {
		  backdrop: true,   // or 'static' if you want to prevent backdrop click
		  keyboard: true
		});

		// Chain: show next only after first is completely hidden
		const onHidden = () => {
		  dealEl.removeEventListener('hidden.bs.modal', onHidden);
		  nextModal.show();
		};

		dealEl.addEventListener('hidden.bs.modal', onHidden);
		dealModal.hide();   // <-- triggers closing animation & backdrop removal

	}
}

function doClear()
{
	document.forms[0].counterparty.value="";
	document.forms[0].ref_no.value="";
	document.forms[0].seq_no.value="";
	document.forms[0].seq_rev_no.value="";
	document.forms[0].sec_type.value="";
	document.forms[0].crdr.value="";
	document.forms[0].value.value="";
	document.forms[0].tds_cd.value="";
	document.forms[0].tds_struct_info.value="";
	document.forms[0].tds_amt.value="";
	document.forms[0].received_dt.value="";
	document.forms[0].pg_ref.value="";
	document.forms[0].deal_type.value="";
	document.forms[0].status.value="";
	document.forms[0].remark.value="";
	document.forms[0].plant_seq.value="";
	document.forms[0].bu_unit.value="";
	
	document.getElementById("taxInfo").innerHTML="";
	document.forms[0].gross_amt.value="";
	document.forms[0].tax_amt.value="";
	document.forms[0].tax_struct_cd.value="";
	document.forms[0].tax_struct_dtl.value="";
	document.getElementById("subTaxSegment").innerHTML="";
	
	document.getElementById("lblVocher").style.display="none";	
	document.getElementById("dpVocher").style.display="none";	
	
	document.getElementById("model_hder_nm").innerHTML="Add Cash Collateral";
	document.getElementById("DealNoDisplay").style.display="none";
	
	document.getElementById("modelBodyParam").style.pointerEvents='auto'
	document.getElementById("attch_cont_btn").style.pointerEvents='auto';
	document.getElementById("contPlantBu").style.pointerEvents='auto';
	document.getElementById("taxSegment").style.pointerEvents='auto';
	document.forms[0].status.style.pointerEvents='auto';
	document.forms[0].remark.style.pointerEvents='auto';
	document.getElementById("div_id_btn").style.display="";
	
	document.getElementById("opt1").style.display='none';
	document.getElementById("opt2").style.display='block';
	document.getElementById("opt3").style.display='none';
	
	document.forms[0].crdr.style.pointerEvents="auto";
	
	var chk_deal = document.forms[0].chk_deal;
	var deal_dtl = document.forms[0].deal_dtl;
	var deal_exp = document.forms[0].deal_exp;
	var split_value = document.forms[0].split_value;
	
	var a="0";
	var b="0";
	
	document.getElementById("span_bal_percent_id").style.display="none";
	document.getElementById("span_bal_inr_id").style.display="none";
	
	if(chk_deal!=null && chk_deal.length!=undefined)
 	{
  		for(var i=0;i<chk_deal.length;i++)
  		{
   			chk_deal[i].checked=false
   			deal_dtl[i].disabled=true;
   			
   			if(deal_exp[i].value=="Y")
   			{
   				document.getElementById("tbtr_"+i).style.display="none";
			}
   			
   			document.getElementById("span_percent_id_"+i).style.display="none";
			document.getElementById("span_inr_id_"+i).style.display="none";
			
			split_value[i].setAttribute("onblur","negNumber(this);checkNumber1(this,"+a+","+b+");");
  			split_value[i].value="";
   			if(chk_deal[i].checked)
   			{
   				split_value[i].disabled=false;
   			}
   			else
   			{
   				split_value[i].disabled=true;
   			}
   		} 
 	}
 	else if(chk_deal!=null)
 	{
   		chk_deal.checked=false
   		deal_dtl.disabled=true;
   		
   		if(deal_exp.value=="Y")
		{
			document.getElementById("tbtr_0").style.display="none";
		}
   		
   		document.getElementById("span_percent_id_0").style.display="none";
		document.getElementById("span_inr_id_0").style.display="none";
		
		split_value.setAttribute("onblur","negNumber(this);checkNumber1(this,"+a+","+b+");");
 		split_value.value="";
 		if(chk_deal.checked)
		{
			split_value.disabled=false;
		}
		else
		{
			split_value.disabled=true;
		}
 	}
	
	doSubmitDealNo();
	
	document.getElementById("contPlantBu").style.display = "none";
	document.getElementById("taxSegment").style.display="none";
	
	document.forms[0].crdr.style.background="";
	document.forms[0].plant_seq.style.background="";
	document.forms[0].bu_unit.style.background="";
	
	document.getElementById("taxRefesh").style.display="";
	
	document.forms[0].temp_operation.value="";
	document.forms[0].opration.value="INSERT";
}

async function doModify(counterparty,ref_no,seq_no,seq_rev_no,sec_type,deal_no,crdr,value,currency,tds_cd,tds_struct_info,tds_amt,received_dt,pg_ref,deal_type,status,remark,operation,
		bu_unit,plant_seq,tax_amt,tax_struct_cd,tax_struct_dtl,gross_amt,att_receipt_voucher,splitValue,splitBy)
{
	doClear()
	
	document.forms[0].counterparty.value=counterparty;
	document.forms[0].counterparty.style.pointerEvents='none';
	document.forms[0].ref_no.value=ref_no;
	document.forms[0].seq_no.value=seq_no;
	document.forms[0].seq_rev_no.value=seq_rev_no;
	document.forms[0].sec_type.value=sec_type;
	document.forms[0].crdr.value=crdr;
	document.forms[0].value.value=value;
	document.forms[0].currency.value=currency;
	document.forms[0].tds_cd.value=tds_cd;
	document.forms[0].tds_struct_info.value=tds_struct_info;
	document.forms[0].tds_amt.value=tds_amt;
	document.forms[0].received_dt.value=received_dt;
	document.forms[0].pg_ref.value=pg_ref;
	document.forms[0].deal_type.value=deal_type;
	document.forms[0].status.value=status;
	document.forms[0].remark.value=remark;
	
	document.getElementById("taxInfo").innerHTML=tax_struct_dtl;
	document.forms[0].gross_amt.value=gross_amt;
	document.forms[0].tax_amt.value=tax_amt;
	document.forms[0].tax_struct_cd.value=tax_struct_cd;
	document.forms[0].tax_struct_dtl.value=tax_struct_dtl;
	if(tax_struct_cd!="")
	{
		fetchSubTax(deal_no);	
	}
	
	if(operation=="MODIFY")
	{
		document.getElementById("model_hder_nm").innerHTML="Modify Cash Collateral";
		document.getElementById("modelBodyParam").style.pointerEvents='auto';
		document.getElementById("attch_cont_btn").style.pointerEvents='auto';
		document.getElementById("contPlantBu").style.pointerEvents='auto';
		document.getElementById("taxSegment").style.pointerEvents='auto';
		document.forms[0].status.style.pointerEvents='auto';
		document.forms[0].remark.style.pointerEvents='auto';
		document.getElementById("div_id_btn").style.display="";
		
		document.getElementById("opt1").style.display='none';
		document.getElementById("opt2").style.display='block';
		document.getElementById("opt3").style.display='block';
		
		if((deal_no.includes("O") || deal_no.includes("Q")) && sec_type=="ADV" && crdr=="DR")
		{
			document.forms[0].crdr.style.pointerEvents="none";
			document.forms[0].crdr.style.background="#e9ecef";
			
			document.forms[0].plant_seq.style.pointerEvents="none";
			document.forms[0].bu_unit.style.pointerEvents="none";
			
			document.forms[0].plant_seq.style.background="#e9ecef";
			document.forms[0].bu_unit.style.background="#e9ecef";
			
			document.getElementById("taxRefesh").style.display="none";
		}
	}
	else if(operation=="CHECK")
	{
		document.getElementById("model_hder_nm").innerHTML="Check(Deal) Cash Collateral";
		document.getElementById("modelBodyParam").style.pointerEvents='none';
		document.getElementById("attch_cont_btn").style.pointerEvents='auto';
		document.getElementById("contPlantBu").style.pointerEvents='auto';
		document.getElementById("taxSegment").style.pointerEvents='auto';
		document.forms[0].status.style.pointerEvents='none';
		document.forms[0].remark.style.pointerEvents='auto';
		document.getElementById("div_id_btn").style.display="";
		
		document.getElementById("opt1").style.display='block';
		document.getElementById("opt2").style.display='block';
		document.getElementById("opt3").style.display='block';
		
		if((deal_no.includes("O") || deal_no.includes("Q")) && sec_type=="ADV" && crdr=="DR")
		{
			document.getElementById("attch_cont_btn").style.pointerEvents='none'; //special
			
			document.forms[0].crdr.style.pointerEvents="none";
			document.forms[0].crdr.style.background="#e9ecef";
			
			document.forms[0].plant_seq.style.pointerEvents="none";
			document.forms[0].bu_unit.style.pointerEvents="none";
			
			document.forms[0].plant_seq.style.background="#e9ecef";
			document.forms[0].bu_unit.style.background="#e9ecef";
			
			document.getElementById("taxRefesh").style.display="none";
		}
	}
	else if(operation=="APPROVE")
	{
		document.getElementById("model_hder_nm").innerHTML="Approve Cash Collateral";
		document.getElementById("modelBodyParam").style.pointerEvents='none';
		document.getElementById("attch_cont_btn").style.pointerEvents='none';
		document.getElementById("contPlantBu").style.pointerEvents='none';
		document.getElementById("taxSegment").style.pointerEvents='none';
		document.forms[0].status.style.pointerEvents='auto';
		document.forms[0].remark.style.pointerEvents='auto';
		document.getElementById("div_id_btn").style.display="";
		
		document.getElementById("opt1").style.display='block';
		document.getElementById("opt2").style.display='none';
		document.getElementById("opt3").style.display='none';
	}
	else
	{
		document.getElementById("model_hder_nm").innerHTML="View Cash Collateral";
		document.getElementById("modelBodyParam").style.pointerEvents='none'
		document.getElementById("attch_cont_btn").style.pointerEvents='none';
		document.getElementById("contPlantBu").style.pointerEvents='none';
		document.getElementById("taxSegment").style.pointerEvents='none';
		document.forms[0].status.style.pointerEvents='none';
		document.forms[0].remark.style.pointerEvents='none';
		document.getElementById("div_id_btn").style.display="none";
		
		document.getElementById("opt1").style.display='block';
		document.getElementById("opt2").style.display='block';
		document.getElementById("opt3").style.display='block';
	}
	
	var chk_deal = document.forms[0].chk_deal;
	var deal_dtl = document.forms[0].deal_dtl;
	var deal_exp = document.forms[0].deal_exp;
	var split_by = document.forms[0].split_by;
	var split_value = document.forms[0].split_value;
	
	if(splitBy=="V")
	{
		split_by[0].checked=true;
		
		enableSplitInput(split_by[0]);
	}
	else if(splitBy=="P")
	{
		split_by[1].checked=true;
		
		enableSplitInput(split_by[1]);
	}
	
	if(chk_deal!=null && chk_deal.length!=undefined)
 	{
		var multiSplitBy = splitBy.split("@");
  		for(var i=0;i<chk_deal.length;i++)
  		{
			chk_deal[i].checked=false
			deal_dtl[i].disabled=true;
			
			if(deal_exp[i].value=="Y")
   			{
   				document.getElementById("tbtr_"+i).style.display="none";
   			}
   		} 
 	}
 	else if(chk_deal!=null)
 	{
		chk_deal.checked=false
		deal_dtl.disabled=true;
		
		if(deal_exp.value=="Y")
  		{
  			document.getElementById("tbtr_0").style.display="none";
		}
 	}
	
	if(chk_deal!=null && chk_deal.length!=undefined)
 	{
  		for(var i=0;i<chk_deal.length;i++)
  		{
  			var multiDealNo = deal_no.split("@");
  			var multiSplitValue = splitValue.split("@");
  			
  			for(var j=0;j<multiDealNo.length;j++)
   			{
				if(multiDealNo[j]==deal_dtl[i].value)
				{
					chk_deal[i].checked=true
	   				deal_dtl[i].disabled=false;
					split_value[i].value = multiSplitValue[j];
					split_value[i].disabled=false;
					
					if(deal_exp[i].value=="Y")
		   			{
		   				document.getElementById("tbtr_"+i).style.display="";
					}
				}
				/*else
				{
	   				chk_deal[i].checked=false
	   				deal_dtl[i].disabled=true;
	   				
	   				if(deal_exp[i].value=="Y")
	   	   			{
	   	   				document.getElementById("tbtr_"+i).style.display="none";
	   				}
				}*/
   			}
   		} 
 	}
 	else if(chk_deal!=null)
 	{
 		var multiDealNo = deal_no.split("@");
 		var multiSplitValue = splitValue.split("@");
 		
		for(var j=0;j<multiDealNo.length;j++)
		{
	 		if(multiDealNo[j]==deal_dtl.value)
			{
				chk_deal.checked=true
				deal_dtl.disabled=false;
				split_value.value = multiSplitValue[j];
				split_value.disabled=false;
				
				if(deal_exp.value=="Y")
	   			{
	   				document.getElementById("tbtr_0").style.display="";
				}
			}
			/*else
			{
				chk_deal.checked=false
				deal_dtl.disabled=true;
				
				if(deal_exp.value=="Y")
	   			{
	   				document.getElementById("tbtr_0").style.display="none";
				}
			}*/
		}
 	}
		
	doSubmitDealNo()
	
	window.setTimeout(function() 
	{
		if(att_receipt_voucher != "")
		{
			if(operation=="MODIFY")
			{
				fetchReceiptVoucher(deal_no,att_receipt_voucher);
			}
			else
			{
				var option = "<option value=''>---Select---</option>"
				option+="<option value="+att_receipt_voucher+">"+att_receipt_voucher+"</option>"
				document.forms[0].receipt_voucher.innerHTML=option;
			}
		}
	}, 500);
	
	window.setTimeout(function() 
	{
		document.forms[0].plant_seq.value=plant_seq;
		document.forms[0].bu_unit.value=bu_unit;
		
		if(att_receipt_voucher != "")
		{
			document.forms[0].receipt_voucher.value=att_receipt_voucher;
			//fetchSelectedReceiptVoucherDtl(document.forms[0].receipt_voucher);
		}
	
	}, 800);
	
	document.forms[0].temp_operation.value=operation;
	document.forms[0].opration.value="MODIFY";
}

enableButton=true;
function doSubmit()
{
	var sysdate = document.forms[0].sysdate.value;
	
	var counterparty = document.forms[0].counterparty.value;
	var sec_type = document.forms[0].sec_type.value;
	var received_dt = document.forms[0].received_dt.value;
	var currency = document.forms[0].currency.value;
	var value = document.forms[0].value.value;
	var remark = document.forms[0].remark.value;
	var pg_ref = document.forms[0].pg_ref.value;
	var status = document.forms[0].status.value;
	var crdr = document.forms[0].crdr.value;
	var tds_cd = document.forms[0].tds_cd.value;
	var tds_amt = document.forms[0].tds_amt.value;
	var plant_seq = document.forms[0].plant_seq.value;
	var bu_unit = document.forms[0].bu_unit.value;
	var tax_amt = document.forms[0].tax_amt.value;
	var gross_amt = document.forms[0].gross_amt.value;
	
	var chk_deal = document.forms[0].chk_deal;
	var deal_dtl = document.forms[0].deal_dtl;
	var temp_buy_sell = document.forms[0].temp_buy_sell;
	
	var opration = document.forms[0].opration.value;
	var temp_operation = document.forms[0].temp_operation.value
	
	var receipt_voucher = document.forms[0].receipt_voucher.value
	
	var split_by = document.forms[0].split_by;
	
	var msg="";
	var flag=true;
	
	if(trim(counterparty)=="")
	{
		msg+="Please Select Counterparty!\n";
		flag=false;
	}
	if(trim(sec_type)=="")
	{
		msg+="Please Select Cash Collateral Type!\n";
		flag=false;
	}
	if(trim(crdr)=="")
	{
		msg+="Please Select Credit | Debit!\n";
		flag=false;
	}
	if(trim(value)=="")
	{
		msg+="Please Enter Amount!\n";
		flag=false;
	}
	if(trim(currency)=="")
	{
		msg+="Please Select Currency!\n";
		flag=false;
	}
	if(trim(tds_amt)!="")
	{
		if(parseFloat(tds_amt) > 0)
		{
			var cnt=0;
			if(trim(tds_cd)=="")
			{
				cnt++;
			}
			
			if(parseInt(cnt) > 0)
			{
				msg+="Please Config TDS Structure Detail!\n";
				flag=false;
			}
		}
	}
	else
	{
		msg+="Please Enter TDS Amount!\n";
		flag=false;
	}
	if(trim(received_dt)=="")
	{
		msg+="Please Enter Pay/Received Date!\n";
		flag=false;
	}
	if(trim(status)=="O" || temp_operation == "CHECK")
	{
		var cnt=0;
		var temp_del="";
		if(chk_deal!=null && chk_deal.length!=undefined)
	 	{
	  		for(var i=0;i<chk_deal.length;i++)
	  		{
	   			if(chk_deal[i].checked)
	   			{
					cnt++;
					temp_del=deal_dtl[i].value;
				}
	   		} 
	 	}
	 	else if(chk_deal!=null)
	 	{
	 		if(chk_deal.checked)
   			{
				cnt++;
				temp_del=deal_dtl.value;
			}
	 	}
		
		if(parseInt(cnt) == 0)
		{
			msg+="Please Select atleast one Contract!\n";
			flag=false;
		}
		
		if((temp_del.includes("O") || temp_del.includes("Q")) && sec_type=="ADV")
		{
			if(trim(bu_unit)=="")
			{
				msg+="Select Business Unit!\n";
				flag=false;
			}
			if(trim(plant_seq)=="")
			{
				msg+="Select Plant!\n";
				flag=false;
			}
		}
	}
	if(trim(pg_ref)=="")
	{
		msg+="Please Enter Transaction Ref!\n";
		flag=false;
	}
	
	var cont_mapping="";
	var buySell="";
	if(chk_deal!=null && chk_deal.length!=undefined)
 	{
  		for(var i=0;i<chk_deal.length;i++)
  		{
   			if(chk_deal[i].checked)
   			{
   				cont_mapping=deal_dtl[i].value;
   				buySell=temp_buy_sell[i].value;
   			}
   		} 
 	}
 	else if(chk_deal!=null)
 	{
   		if(chk_deal.checked)
     	{	
   			cont_mapping=deal_dtl.value;
   			buySell=temp_buy_sell.value;
   		}
 	}
	
	if((cont_mapping.includes("O") || cont_mapping.includes("Q")) && sec_type=="ADV")
	{
		var tot=parseFloat("0");
		if(trim(gross_amt)=="")
		{
			msg+="Gross Amount missing!\n";
			flag=false;
		}
		else
		{
			tot+=parseFloat(gross_amt);
		}
		if(trim(tax_amt)=="")
		{
			msg+="Tax Amount missing!\n";
			flag=false;
		}
		else
		{
			tot+=parseFloat(tax_amt);
		}	
		
		if(trim(value)!="")
		{
			if(parseFloat(value) != round(parseFloat(tot),2))
			{
				msg+="Gross + Tax Amount("+round(tot,2)+") is not matched with Amount("+value+")!\n";
				flag=false;
			}
		}
		
		if(crdr=="DR")
		{
			if(receipt_voucher=="")
			{
				msg+="Select Receipt Voucher aginst which Refund to be Generated!\n";
				flag=false;
			}
		}
	}
	
	if(trim(status)=="")
	{
		msg+="Please Select Status!\n";
		flag=false;
	}
	
	if(temp_operation=="APPROVE" && buySell=="Sell")
	{
		if(trim(received_dt)!="")
		{
			var value_0 = compareDate(received_dt,sysdate);
			if(value_0=="1")
			{
				msg += "Received Date "+received_dt+" shouldn't be > System Date "+sysdate+"!\n";
				flag = false;
			}
		}
	}
	
	if(split_by[0].checked)
	{
		document.forms[0].temp_split_by.value=split_by[0].value;
	}
	
	if(split_by[1].checked)
	{
		document.forms[0].temp_split_by.value=split_by[1].value;
	}
	
	if(flag)
	{
		//var a = confirm("Do you want to "+opration+" the Cash Collateral Details?");
		var a = confirm("Do you want to "+temp_operation+" the Cash Collateral Details?");
		temp_operation
		if(a)
		{
			editAllowedOnCpStatus = true;
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function taxCalc(input)
{
	var tax_amt = document.forms[0].tax_amt
	//var invoice_amt = document.forms[0].invoice_amt
	//var net_payable = document.forms[0].net_payable
	
	var sub_tax_amt = document.forms[0].sub_tax_amt
	
	var total_tax=parseFloat(0);
	
	if(sub_tax_amt!=null && sub_tax_amt!=undefined)
	{
		if(sub_tax_amt.length!=undefined)
		{
			for(var i=0;i<sub_tax_amt.length;i++)
			{
				if(trim(sub_tax_amt[i].value)!="")
				{
					var temp = parseFloat(sub_tax_amt[i].value)
					if(!isNaN(temp))
					{
						total_tax+=parseFloat(temp);
					}
				}
			}
		}
		else
		{

			if(input=="tax" && trim(tax_amt.value)!="")
			{
				var temp = parseFloat(tax_amt.value)
				if(!isNaN(temp))
				{
					total_tax=parseFloat(temp);
					sub_tax_amt.value=round(parseFloat(total_tax),2);
				}
			}
		}
	}
	
	if(input=="")
	{
		tax_amt.value=round(parseFloat(total_tax),2);
	}
	//invoice_amt.value=tax_amt.value
	//net_payable.value=invoice_amt.value
	
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.credit_risk.DataBean_Advance" id="advance" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utilDate" scope="request"></jsp:useBean>
<%
String sysdate=utilDate.getSysdate();

String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String clearance = request.getParameter("clearance")==null?"K":request.getParameter("clearance");

advance.setCallFlag("CASH_COLLATERAL_MANAGEMENT");
advance.setCounterparty_cd(counterparty_cd);
advance.setClearance(clearance);
advance.setComp_cd(owner_cd);
advance.init();

if(counterparty_cd.equals(""))
{
	counterparty_cd=advance.getCounterparty_cd();
}

Vector VMST_COUNTERPARTY_CD = advance.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = advance.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = advance.getVMST_COUNTERPARTY_ABBR();

Vector VCOUNTERPARTY_CD = advance.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NAME = advance.getVCOUNTERPARTY_NAME();
Vector VCOUNTERPARTY_ABBR = advance.getVCOUNTERPARTY_ABBR();
Vector VSEC_TYPE = advance.getVSEC_TYPE();
Vector VSEC_REF_NO = advance.getVSEC_REF_NO();
Vector VSTATUS = advance.getVSTATUS();
Vector VCURRENCY = advance.getVCURRENCY();
Vector VCURRENCY_NM = advance.getVCURRENCY_NM();
Vector VCRDR = advance.getVCRDR();
Vector VCRDR_NM = advance.getVCRDR_NM();
Vector VVALUE = advance.getVVALUE();
Vector VTOTAL_VALUE = advance.getVTOTAL_VALUE();
Vector VRECEIVED_DATE = advance.getVRECEIVED_DATE();
Vector VSEQ_NO = advance.getVSEQ_NO();
Vector VSEQ_REV_NO = advance.getVSEQ_REV_NO();
Vector VADV_PG_REF = advance.getVADV_PG_REF();
Vector VDEAL_TYPE = advance.getVDEAL_TYPE();
Vector VDEAL_NO = advance.getVDEAL_NO();
Vector VREMARK = advance.getVREMARK();
Vector VPDF_GENERATED = advance.getVPDF_GENERATED();
Vector VPDF_FILE_NAME = advance.getVPDF_FILE_NAME();
Vector VPDF_FILE_PATH = advance.getVPDF_FILE_PATH();
Vector VSAP_APPROVAL_FLAG = advance.getVSAP_APPROVAL_FLAG();
Vector VTDS_AMT = advance.getVTDS_AMT();
Vector VTDS_STRUCT_CD = advance.getVTDS_STRUCT_CD();
Vector VTDS_STRUCT_INFO = advance.getVTDS_STRUCT_INFO();
Vector VTEMP_SEC_REF_NO = advance.getVTEMP_SEC_REF_NO();
Vector VDEAL_MAPPING = advance.getVDEAL_MAPPING();
Vector VBUY_SELL = advance.getVBUY_SELL();
Vector VBU_UNIT = advance.getVBU_UNIT();
Vector VPLANT_SEQ = advance.getVPLANT_SEQ();
Vector VTAX_AMT = advance.getVTAX_AMT();
Vector VTAX_STRUCT_CD = advance.getVTAX_STRUCT_CD();
Vector VTAX_STRUCT_DTL = advance.getVTAX_STRUCT_DTL();
Vector VGROSS_AMT = advance.getVGROSS_AMT();
Vector VDEAL_CONT_REF = advance.getVDEAL_CONT_REF();
Vector VATT_RECEIPT_VOUCHER = advance.getVATT_RECEIPT_VOUCHER();
Vector VSEC_INT_REF = advance.getVSEC_INT_REF();
Vector VSPLIT_VALUE = advance.getVSPLIT_VALUE();
Vector VSPLIT_BY = advance.getVSPLIT_BY();

Vector VAGMT_NO = advance.getVAGMT_NO();
Vector VAGMT_REV_NO = advance.getVAGMT_REV_NO();
Vector VCONT_NO = advance.getVCONT_NO();
Vector VCONT_REV_NO = advance.getVCONT_REV_NO();
Vector VCONTRACT_TYPE = advance.getVCONTRACT_TYPE();
Vector VCONTRACT_MAPPING = advance.getVCONTRACT_MAPPING();
Vector VCONTRACT_MAPPING_DIS = advance.getVCONTRACT_MAPPING_DIS();
Vector VCONTRACT_MAPPING_DIS_1 = advance.getVCONTRACT_MAPPING_DIS_1();
Vector VCONTRACT_EXPIRED = advance.getVCONTRACT_EXPIRED();
Vector VCONTRACT_DEAL_TYPE = advance.getVCONTRACT_DEAL_TYPE();
Vector VCONT_BUY_SELL = advance.getVCONT_BUY_SELL();
Vector VCONTRACT_TYPE_NM = advance.getVCONTRACT_TYPE_NM();

String context_nm = request.getContextPath();
String server_nm = request.getServerName();
String server_port = ""+request.getServerPort();
String url=CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//";
String file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//"+CommonVariable.work_dir+owner_cd;


%>
<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_Advance">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<%if(!msg.equals("")){
				if(msg_type.equals("S")){%>
					<div class="fadealert"><%=utilmsg.successMessage(msg)%></div>
				<%}else if(msg_type.equals("E")){%>
					<div class="fadealert"><%= utilmsg.errorMessage(msg)%></div>
				<%}
			} %>
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="d-flex justify-content-between">
						<div class="topheader">
				    		Cash Collateral Management  
	   	 				</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5" >
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div align="center">
								<div class="btn-group" >
									<label class="btn btn-outline-secondary subbtngrp1 <%if(clearance.equals("K")){%>btnactive<%}%>" onclick="refresh('K')">KYC</label>
									<label class="btn btn-outline-secondary subbtngrp1 <%if(clearance.equals("I")){%>btnactive<%}%>" onclick="refresh('I')">IGX</label>
								</div>
							</div>
						</div>
					</div>					
					<div class="row m-b-5">
						<div class="col-sm-6 col-xs-6 col-md-6" align="left">
							<div class="form-group row m-b-5	">
								<div class="col-auto">
									<label class="form-label"><b>Counterparty</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh('<%=clearance%>')">
										<option value="">--Select--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
											<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %> 
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div> 
							</div>
						</div>
						
					  	<div class="col-sm-6 col-xs-6 col-md-6" align="right">
					  		<div class="d-flex justify-content-end">
					  			<div class="form-group row">
					  				<div class="col-auto">
										<div class="btn-group">
											<label class="btn btn-outline-secondary subbtngrp1" onclick="validateCP();">Add Cash Collateral</label>
										</div>
									</div>
									<div class="col-auto">
										<div class="btn-group">
											<a href="<%=url%>pdf_signer//PDFSigner.jar" download>
												<label class="btn btn-outline-secondary subbtngrp1" onclick="downloadPdfSigner('<%=url%>');">
													<i class="fa fa-pencil-square-o"></i>&nbsp;Sign PDF
												</label>
											</a>
										</div>
									</div>
									<div class="col-auto">
										<div class="btn-group">
											<label style="color:blue; font-size: 20px;" title="Click to Refresh" onclick="location.reload();"><i class="fa fa-refresh"></i></label>
										</div>
									</div>
								</div>
							</div>
					  	</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						<%if(counterparty_cd.equals("")){ %>
							<div align="center"><%=utilmsg.infoMessage("<b>Please Select Counterparty!</b>") %></div>
						<%}else{ %>
							<div class="table-responsive">
								<table class="table table-bordered serchtbl" id="example0">
									<thead id="tbsearch0">
										<tr>
											<th>SR#</th>
											<!-- <th>Counterparty</th> -->
											<th class="tbser0">Deal Type</th>
											<th class="tbser0">Buy | Sell</th>
											<th class="tbser0">Cash Collateral Ref.</th>
											<th class="tbser0">Transaction Ref.</th>
											<th class="tbser0">Cash Collateral Type</th>
											<th class="tbser0">Credit | Debit</th>
											<th>Value (Amount + TDS)</th>
											<th class="tbser0">Currency</th>
											<th class="tbser0">Pay | Received Date</th>
											<th class="tbser0">Status</th>
											<th class="tbser0">Contract#</th>
											<th class="tbser0">Contract/Trade Ref#</th>
											<th class="tbser0">Receipt/Refund No</th>
											<th>View</th>
											<th>Modify</th>
											<th>Check (Deal)</th>
											<th>Approve</th>
											<th>View | Print PDF</th>
											<th>SAP Approval</th>
											<!-- <th>Generate Reversal</th> -->
										</tr>
									</thead>
								<% if(VSEC_REF_NO.size()>0){  %>
									<% for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
									<tbody>
										<tr>
											<td align="right"><%=i+1%></td>
											<%-- <td><%=VCOUNTERPARTY_NAME.elementAt(i) %></td> --%>
											<td align="center"><%=VDEAL_TYPE.elementAt(i) %></td>
											<td align="center">
				       							<span
													<%if(VBUY_SELL.elementAt(i).equals("Buy")){ %>
				   										class="alert" style="background: #ffccff; color: #cc00cc;"
				   									<%}else if(VBUY_SELL.elementAt(i).equals("Sell")){ %>
				   										class="alert alert-primary"
				   									<%}%>><b><%=VBUY_SELL.elementAt(i)%></b>
				  								</span>
				       						</td>
											<td align="center"><%=VSEC_REF_NO.elementAt(i) %></td>
											<td align="center"><%=VADV_PG_REF.elementAt(i) %></td>
											<td align="center">
												<span 
												<%if(VSEC_TYPE.elementAt(i).equals("ADV")){ %>
		    										class="alert alert-info"
		    									<%}else if(VSEC_TYPE.elementAt(i).equals("DPT")){ %>
		    										class="alert alert-warning" <%}%>><%=VSEC_TYPE.elementAt(i) %></span></td>
											<td align="center"><%=VCRDR_NM.elementAt(i) %></td>
											<td align="right"><%=VTOTAL_VALUE.elementAt(i) %></td>
											<td align="center"><%=VCURRENCY_NM.elementAt(i) %></td>
											<td align="center"><%=VRECEIVED_DATE.elementAt(i) %></td>
											<td align="center">
												<%if(VSTATUS.elementAt(i).equals("P")){ %>
													<span class="alert alert-primary">Pending</span>
												<%}else if(VSTATUS.elementAt(i).equals("O")){  %>
													<span class="alert alert-success">In Order</span>
												<%}else if(VSTATUS.elementAt(i).equals("C")){  %>
													<span class="alert alert-danger">Cancelled</span>
												<%}else if(VSTATUS.elementAt(i).equals("A")){  %>
													<span class="alert alert-secondary">Pending For Amendment</span>
												<%}else if(VSTATUS.elementAt(i).equals("R")){  %>
													<span class="alert alert-warning">Restated</span>
												<%} %>
											</td>
											<td align="center"><%=VDEAL_NO.elementAt(i) %></td>
											<td align="center"><%=VDEAL_CONT_REF.elementAt(i) %></td>
											<td align="center"><%=VSEC_INT_REF.elementAt(i) %></td>
											<td align="center">
												<i class="fa fa-eye fa-2x" data-bs-toggle="modal" data-bs-target="#AddNewSecurity" 
													onclick="doModify('<%=VCOUNTERPARTY_CD.elementAt(i) %>','<%=VTEMP_SEC_REF_NO.elementAt(i)%>','<%=VSEQ_NO.elementAt(i)%>','<%=VSEQ_REV_NO.elementAt(i)%>','<%=VSEC_TYPE.elementAt(i) %>',
													'<%=VDEAL_MAPPING.elementAt(i)%>','<%=VCRDR.elementAt(i)%>','<%=VVALUE.elementAt(i)%>','<%=VCURRENCY.elementAt(i)%>',
													'<%=VTDS_STRUCT_CD.elementAt(i)%>','<%=VTDS_STRUCT_INFO.elementAt(i)%>','<%=VTDS_AMT.elementAt(i)%>',
													'<%=VRECEIVED_DATE.elementAt(i)%>','<%=VADV_PG_REF.elementAt(i) %>','<%=VDEAL_TYPE.elementAt(i)%>','<%=VSTATUS.elementAt(i)%>','<%=VREMARK.elementAt(i)%>','',
													'<%=VBU_UNIT.elementAt(i)%>','<%=VPLANT_SEQ.elementAt(i)%>','<%=VTAX_AMT.elementAt(i)%>','<%=VTAX_STRUCT_CD.elementAt(i)%>','<%=VTAX_STRUCT_DTL.elementAt(i)%>',
													'<%=VGROSS_AMT.elementAt(i)%>','<%=VATT_RECEIPT_VOUCHER.elementAt(i)%>','<%=VSPLIT_VALUE.elementAt(i)%>','<%=VSPLIT_BY.elementAt(i)%>');">
												</i>
											</td>
											<td align="center">
												<i class="fa fa-pencil fa-2x" data-bs-toggle="modal" data-bs-target="#AddNewSecurity" 
													onclick="doModify('<%=VCOUNTERPARTY_CD.elementAt(i) %>','<%=VTEMP_SEC_REF_NO.elementAt(i)%>','<%=VSEQ_NO.elementAt(i)%>','<%=VSEQ_REV_NO.elementAt(i)%>','<%=VSEC_TYPE.elementAt(i) %>',
													'<%=VDEAL_MAPPING.elementAt(i)%>','<%=VCRDR.elementAt(i)%>','<%=VVALUE.elementAt(i)%>','<%=VCURRENCY.elementAt(i)%>',
													'<%=VTDS_STRUCT_CD.elementAt(i)%>','<%=VTDS_STRUCT_INFO.elementAt(i)%>','<%=VTDS_AMT.elementAt(i)%>',
													'<%=VRECEIVED_DATE.elementAt(i)%>','<%=VADV_PG_REF.elementAt(i) %>','<%=VDEAL_TYPE.elementAt(i)%>','<%=VSTATUS.elementAt(i)%>','<%=VREMARK.elementAt(i)%>','MODIFY',
													'<%=VBU_UNIT.elementAt(i)%>','<%=VPLANT_SEQ.elementAt(i)%>','<%=VTAX_AMT.elementAt(i)%>','<%=VTAX_STRUCT_CD.elementAt(i)%>','<%=VTAX_STRUCT_DTL.elementAt(i)%>',
													'<%=VGROSS_AMT.elementAt(i)%>','<%=VATT_RECEIPT_VOUCHER.elementAt(i)%>','<%=VSPLIT_VALUE.elementAt(i)%>','<%=VSPLIT_BY.elementAt(i)%>');"
													style="<%if(VSTATUS.elementAt(i).equals("C") || VSTATUS.elementAt(i).equals("O")){ %>
														pointer-events: none; opacity: .65; color: gray;
														<%} else {%>
															color:var(--header_color);
														<%}%>">
												</i>
											</td>
											<td align="center">
												<i class="fa fa-handshake-o fa-2x" data-bs-toggle="modal" data-bs-target="#AddNewSecurity" 
													onclick="doModify('<%=VCOUNTERPARTY_CD.elementAt(i) %>','<%=VTEMP_SEC_REF_NO.elementAt(i)%>','<%=VSEQ_NO.elementAt(i)%>','<%=VSEQ_REV_NO.elementAt(i)%>','<%=VSEC_TYPE.elementAt(i) %>',
													'<%=VDEAL_MAPPING.elementAt(i)%>','<%=VCRDR.elementAt(i)%>','<%=VVALUE.elementAt(i)%>','<%=VCURRENCY.elementAt(i)%>',
													'<%=VTDS_STRUCT_CD.elementAt(i)%>','<%=VTDS_STRUCT_INFO.elementAt(i)%>','<%=VTDS_AMT.elementAt(i)%>',
													'<%=VRECEIVED_DATE.elementAt(i)%>','<%=VADV_PG_REF.elementAt(i) %>','<%=VDEAL_TYPE.elementAt(i)%>','<%=VSTATUS.elementAt(i)%>','<%=VREMARK.elementAt(i)%>','CHECK',
													'<%=VBU_UNIT.elementAt(i)%>','<%=VPLANT_SEQ.elementAt(i)%>','<%=VTAX_AMT.elementAt(i)%>','<%=VTAX_STRUCT_CD.elementAt(i)%>','<%=VTAX_STRUCT_DTL.elementAt(i)%>',
													'<%=VGROSS_AMT.elementAt(i)%>','<%=VATT_RECEIPT_VOUCHER.elementAt(i)%>','<%=VSPLIT_VALUE.elementAt(i)%>','<%=VSPLIT_BY.elementAt(i)%>');"
													style="<%if(VSTATUS.elementAt(i).equals("C") || VSTATUS.elementAt(i).equals("O")){ %>
														pointer-events: none; opacity: .65; color: gray;
														<%} else {%>
															color:#ff3399;
														<%}%>">
												</i>
											</td>
											<td align="center">
												<i class="fa fa-flag fa-2x" data-bs-toggle="modal" data-bs-target="#AddNewSecurity" 
													onclick="doModify('<%=VCOUNTERPARTY_CD.elementAt(i) %>','<%=VTEMP_SEC_REF_NO.elementAt(i)%>','<%=VSEQ_NO.elementAt(i)%>','<%=VSEQ_REV_NO.elementAt(i)%>','<%=VSEC_TYPE.elementAt(i) %>',
													'<%=VDEAL_MAPPING.elementAt(i)%>','<%=VCRDR.elementAt(i)%>','<%=VVALUE.elementAt(i)%>','<%=VCURRENCY.elementAt(i)%>',
													'<%=VTDS_STRUCT_CD.elementAt(i)%>','<%=VTDS_STRUCT_INFO.elementAt(i)%>','<%=VTDS_AMT.elementAt(i)%>',
													'<%=VRECEIVED_DATE.elementAt(i)%>','<%=VADV_PG_REF.elementAt(i) %>','<%=VDEAL_TYPE.elementAt(i)%>','<%=VSTATUS.elementAt(i)%>','<%=VREMARK.elementAt(i)%>','APPROVE',
													'<%=VBU_UNIT.elementAt(i)%>','<%=VPLANT_SEQ.elementAt(i)%>','<%=VTAX_AMT.elementAt(i)%>','<%=VTAX_STRUCT_CD.elementAt(i)%>','<%=VTAX_STRUCT_DTL.elementAt(i)%>',
													'<%=VGROSS_AMT.elementAt(i)%>','<%=VATT_RECEIPT_VOUCHER.elementAt(i)%>','<%=VSPLIT_VALUE.elementAt(i)%>','<%=VSPLIT_BY.elementAt(i)%>');"
													style="<%if(VSTATUS.elementAt(i).equals("C") || VSTATUS.elementAt(i).equals("O")){ %>
														pointer-events: none; opacity: .65; color: gray;
														<%} else {%>
															color:#00cc00;
														<%}%>">
												</i>
											</td>
											<td align="center">
											<%if(!VSAP_APPROVAL_FLAG.elementAt(i).equals("X")){ %>
												<%if(VPDF_GENERATED.elementAt(i).equals("X")) {%>
													<span class="fa-stack fa-lg">
													  <i class="fa fa-print fa-stack-1x"></i>
													  <i class="fa fa-ban fa-stack-2x" style="color:grey;"></i>
													</span>
												<%}else if(!VPDF_GENERATED.elementAt(i).equals("Y")){ %>
													<i class="fa fa-print fa-2x"
														onclick="printPDF('<%=VCOUNTERPARTY_CD.elementAt(i)%>', '<%=VSEQ_NO.elementAt(i) %>', 
																			'<%=VSEQ_REV_NO.elementAt(i) %>', '<%=clearance%>','N');" 
														style="<%if(VSTATUS.elementAt(i).equals("O")){ %>
															color:#800000;
														<%} else {%>
															pointer-events: none; opacity: .65; color: gray;												
														<%}%>">											
													</i>		
												<%} else {%>
													<i class="fa fa-file-pdf-o fa-2x"
														onclick="openPdfFile('<%=file_url%><%=VPDF_FILE_PATH.elementAt(i)%><%=VPDF_FILE_NAME.elementAt(i)%>');" 
														style="color:red;">												
													</i>
												<%} %>												
											<%} %>	
											</td>
											<td align="center">
											<%if(!owner_cd.equals("1")){ %>
												<span class="fa-stack fa-lg">
												<i class="fa fa-file-code-o fa-stack-1x"></i>
												<i class="fa fa-ban fa-stack-2x" style="color:grey;"></i>
												</span>
											<%}else{%>		
												<%if(!VSAP_APPROVAL_FLAG.elementAt(i).equals("X")){ %>
													<%if(VSAP_APPROVAL_FLAG.elementAt(i).equals("Z")){ %>
													<span class="fa-stack fa-lg">
													  <i class="fa fa-file-code-o fa-stack-1x"></i>
													  <i class="fa fa-ban fa-stack-2x" style="color:grey;"></i>
													</span>
													<%}else{ %>
													<i class="fa <%if(!VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>fa-file-code-o<%}else{%>fa-eye<%} %> fa-2x" 
															onclick="doGenXML('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=clearance%>',
														 '<%=VSEQ_NO.elementAt(i)%>',<%=VSEQ_REV_NO.elementAt(i) %>,'<%=VSAP_APPROVAL_FLAG.elementAt(i)%>',
														 '<%=VSEC_REF_NO.elementAt(i)%>','N', '<%=VPDF_GENERATED.elementAt(i)%>','<%=VSEC_INT_REF.elementAt(i)%>');"
														style="<%if(!VSTATUS.elementAt(i).equals("")){ %>
															color: orange;
														<%} else {%>
																pointer-events: none; opacity: .65; color: gray;
														<%}%>">
													</i>
													<%} %>
												<%}%>
											<%} %>
											</td>
											<%-- <td align="center">
											<%if(!VSAP_APPROVAL_FLAG.elementAt(i).equals("X")){ %>	
												<i class="fa fa-retweet fa-2x" 
														onclick="doGenXML('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=clearance%>',
													 '<%=VSEQ_NO.elementAt(i)%>',<%=VSEQ_REV_NO.elementAt(i) %>,'<%=VSAP_APPROVAL_FLAG.elementAt(i)%>',
													 '<%=VSEC_REF_NO.elementAt(i)%>','N', '<%=VPDF_GENERATED.elementAt(i)%>');"
													style="<%if(VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>
														color: blue;
													<%} else {%>
															pointer-events: none; opacity: .65; color: gray;
													<%}%>">
												</i>
											<%}%>
											</td> --%>
										</tr>
									</tbody>
									<%} %>
								<%}else{ %>
									<tr><td colspan="20" align="center"><%=utilmsg.infoMessage("<b>No Cash Collateral Available!</b>") %></td></tr>
								<%} %>
								</table>
							</div>
						<%} %>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="AddNewSecurity" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader" id="model_hder_nm">
        			Add Cash Collateral
        		</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
        	</div>
        	<div class="modal-body mdbody">
      			<div class="cdbody" id="modelBodyParam">
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Counterparty<span class="s-red">*</span> </b></label>
							</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<select class="form-select form-select-sm" name="counterparty" >
										<option value="">--Select--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
											<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<input type="hidden" name="ref_no" value="">
									<input type="hidden" name="seq_no" value="">
									<input type="hidden" name="seq_rev_no" value="">
								</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Cash Collateral Type<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="sec_type">
				      					<option value="">--Select--</option>
				      					<option value="ADV">ADV - Advance Payment</option>
				      					<option value="DPT">DPT - Cash Deposit</option>
				      				</select>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Credit | Debit<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<select class="form-select form-select-sm" name="crdr">
			      							<option value="">--Select--</option>
					      					<option value="CR">Credit</option>
					      					<option value="DR">Debit</option>
				      					</select>
		      						</div>
				    			</div>
				  			</div>
						</div>
      				</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
				    			<label class="form-label"><b>Amount<span class="s-red">*</span></b></label>
				  			</div>
						</div>
				  		<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-8">							
				      				<input type="text" class="form-control form-control-sm" name="value" onblur="negNumber(this);checkNumber1(this,12,2);">	
				      			</div>
				      			<div class="col-4">				    			
				      				<select class="form-select form-select-sm" name="currency">
				      					<option value="1">INR</option>
				      					<!-- <option value="2">USD</option> -->
				      				</select>
				    			</div>
				  			</div>
				  		</div>
				   		<div class="col-sm-2 col-xs-2 col-md-2" id="row_tds">  
			    			<input type="button" class="btn btn-sm config_btn" value="TDS Config" onclick="openTcsTdsStructMst('TDS');">
							<input type="hidden" name="tds_cd" value="">
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4" id="row_tds1">  
							<div class="form-group row">
								<div class="col-4">
									<textarea class="form-control form-control-sm" name="tds_struct_info" cols="75" rows="1" style="font-weight: bold;" readOnly></textarea>
				    			</div>
				    			<div class="col-5">				    				
			    					<input type="text" class="form-control form-control-sm" name="tds_amt" value="" onblur="negNumber(this);checkNumber1(this,8,2);">
				    			</div>
				    			<div class="col-3">	
			    					<select class="form-select form-select-sm" name="tds_amt_unit">
					  					<option value="1">INR</option>
										<!-- <option value="2">USD</option> -->
									</select>
								</div>
				    		</div>
				    	</div>			    	 	
				  	</div>
      				<div class="row m-b-5">	
				  		<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Pay/Received Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="received_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Transaction Ref#<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
		      						<input type="text" class="form-control form-control-sm" name="pg_ref" maxlength="12">
				    			</div>
				  			</div>
						</div>
					</div>	
					<div class="row m-b-5">	
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="button" class="btn btn-sm config_btn" value="Attach Contract#" id="attch_cont_btn"; 
					    			data-bs-toggle="modal" data-bs-target="#DealNoModel">
					    			
					    			<select class="form-select form-select-sm" name="deal_type" style="display: none;">
				      					<option value="">--Select--</option>
				      					<option value="GAS" >GAS</option>
				      					<option value="LTCORA" >LTCORA</option>
				      				</select>
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<label class="form-label" id="DealNoDisplay" style="display:none;"></label>
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2" id="lblVocher" style="display:none;">  
							<div class="form-group row">
				    			<label class="form-label"><b>Receipt Voucher<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4" id="dpVocher" style="display:none;">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="receipt_voucher" onchange="fetchSelectedReceiptVoucherDtl(this);">
				      				</select>				      				
				      			</div>
				  			</div>
						</div>	
					</div>
					<div class="row m-b-5" id="contPlantBu" style="display:none;">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Business Unit<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="bu_unit">
				      				</select>				      				
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b><%if(clearance.equals("I")) {%>Gx Business Unit<%}else{ %>Plant<%}%><span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="plant_seq">
				      				</select>				      				
				      			</div>
				  			</div>
						</div>							
					</div>
					<div class="row m-b-5" id="taxSegment">	
				  		<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Gross Amount<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
		      						<input type='text' class='form-control form-control-sm' name='gross_amt' value='' onblur="negNumber(this);checkNumber1(this,12,2);" readOnly>
		      					</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Tax (<span id="taxInfo"></span>)<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-10 col-xs-10 col-md-10">
		      						<input type='text' class='form-control form-control-sm' name='tax_amt' value='' onblur="negNumber(this);checkNumber1(this,12,2);taxCalc('tax');" readOnly>
		      						<input type='hidden' class='form-control form-control-sm' name='tax_struct_cd' value='' readOnly>
									<input type='hidden' class='form-control form-control-sm' name='tax_struct_dtl' value="" readOnly>
				    			</div>
				    			<div class="col-sm-2 col-xs-2 col-md-2" id="taxRefesh">
				    				<i class="fa fa-refresh fa-2x" style="color:blue;" onclick="fetchTaxtDtl();" title="Click to Calculate Tax!"></i>
				    			</div>
				  			</div>
						</div>
					</div>
					<div id="subTaxSegment">
							
					</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Status<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="status">
				      					<option id="opt0" value="">--Select--</option>
				      					<option id="opt1" value="O" style="pointer-events: none;">In order</option>
				      					<option id="opt2" value="A">Pending for Amendment</option>
				      					<option id="opt3" value="C">Cancelled</option>
				      				</select>				      				
				      			</div>
				  			</div>
						</div>							
					</div>				
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Remark</b></label>
				  			</div>
						</div>
						<div class="col-sm-12 col-xs-12 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="remark">
				    			</div>
				  			</div>
						</div>
					</div>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter">
        		<div align="right" id="div_id_btn">
        			<%if(write_access.equals("Y")){ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();">
					<%}else{ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
					<%} %>
				</div>
      		</div>
        </div>
	</div>
</div>

<div class="modal fade" id="DealNoModel" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
        			Deal No
        		</div>
        	</div>
        	<div class="modal-body mdbody">
        		<div class="cdbody">
        			<div class="row justify-content-center">
						<div class="col-auto">
							<label class="form-label"><b>Split By<span class="s-red">*</span></b></label>
						</div>
						<div class="col-auto">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="radio" name="split_by" value="V" onclick="enableSplitInput(this)">&nbsp;Value&nbsp;&nbsp;
			      					<input type="radio" name="split_by" value="P" onclick="enableSplitInput(this)">&nbsp;Percentage&nbsp;&nbsp;
			      					<input type="hidden"  name="temp_split_by" value="">
				    			</div>
				  			</div>
						</div>
					</div>
				</div>
				<div class="cdbody">
        			<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered">
									<thead>
			        					<tr>
				        					<th>Select</th>
				        					<th>Buy/Sell</th>
				        					<th>Deal Type</th>
				        					<th>Contract Type</th>
				        					<th>Contract# [Cont/Trade Ref#] (Contract Duration)</th>
				        					<th>Split By</th>
			        					</tr>
			        				</thead>
									<tbody>
									<%if(VCONTRACT_MAPPING.size() > 0){ %>
				        				<%for(int i=0; i<VCONTRACT_MAPPING.size(); i++){%>
			        					<tr id="tbtr_<%=i%>" <%if(VCONTRACT_EXPIRED.elementAt(i).equals("Y")){ %>style="display: none"<%} %>>
			        						<td align="center">
			        							<input type="checkbox" class="form-check-input" name="chk_deal" value="<%=VCONTRACT_MAPPING.elementAt(i)%>" onclick="doDealEnabled(this,'<%=i%>')">
							    			</td>
							    			<td align="center">
				       							<span
													<%if(VCONT_BUY_SELL.elementAt(i).equals("Buy")){ %>
				   										class="alert" style="background: #ffccff; color: #cc00cc;"
				   									<%}else if(VCONT_BUY_SELL.elementAt(i).equals("Sell")){ %>
				   										class="alert alert-primary"
				   									<%}%>><b><%=VCONT_BUY_SELL.elementAt(i)%></b>
				  								</span>
				       						</td>
							    			<td align="center"><%=VCONTRACT_DEAL_TYPE.elementAt(i)%></td>
							    			<td align="center"><%=VCONTRACT_TYPE_NM.elementAt(i) %></td>
				        					<td>
				        						<%=VCONTRACT_MAPPING_DIS.elementAt(i) %>
				        						<input type="hidden"  name="deal_dtl" id="deal_dtl_<%=i%>" value="<%=VCONTRACT_MAPPING.elementAt(i)%>" disabled>
				        						<input type="hidden"  name="deal_exp" id="deal_exp_<%=i%>" value="<%=VCONTRACT_EXPIRED.elementAt(i)%>">
				        						<input type="hidden"  name="cont_deal_type" id="cont_deal_type_<%=i%>" value="<%=VCONTRACT_DEAL_TYPE.elementAt(i)%>">
							    				<input type="hidden"  name="display_deal_dtl" value="<%=VCONTRACT_MAPPING_DIS_1.elementAt(i)%>">
							    				<input type="hidden"  name="temp_buy_sell" id="temp_buy_sell_<%=i%>" value="<%=VCONT_BUY_SELL.elementAt(i)%>"> <!-- required only for validation  -->
							    			</td>
							    			<td align="center">
							    				<div style="width:130px;">
						    						<div class="input-group input-group-sm" >
							      						<input type="text" class="form-control form-control-sm" name="split_value" id="split_value_<%=i%>" 
							      						value="" onblur="negNumber(this),checkNumber1(this,5,2);" onKeyUp="calcRemBalance();" disabled style="text-align:right;">	
							      						<span class="input-group-text" style="display: none;" id="span_percent_id_<%=i%>"><i class="fa fa-percent fa-lg"></i></span>
							      						<span class="input-group-text" style="display: none;" id="span_inr_id_<%=i%>"><i class="fa fa-lg"><b>INR</b></i></span>
						      						</div>
						      					</div>
				        					</td>
				        				</tr>
			        					<%} %>
			        				<%}else{ %>
			        					<tr>
			        						<td colspan="6" align="center"><%=utilmsg.infoMessage("<b>No Active/Upcoming Contracts Detail are Avaliable!</b>") %></td>
			        					</tr>
			        				<%} %>
			        					<tr style="font-weight: bold;">
			        						<td colspan="5" align="right">Remaining Balance : </td>
			        						<td align="center">
							    				<div style="width:130px;">
						    						<div class="input-group input-group-sm" >
							      						<input type="text" class="form-control form-control-sm" name="bal_split_value" id="bal_split_value" 
							      						value="" disabled style="text-align:right;">	
							      						<span class="input-group-text" style="display: none;" id="span_bal_percent_id"><i class="fa fa-percent fa-lg"></i></span>
							      						<span class="input-group-text" style="display: none;" id="span_bal_inr_id"><i class="fa fa-lg"><b>INR</b></i></span>
						      						</div>
						      					</div>
				        					</td>
			        					</tr>
		        					</tbody>
	        					</table>
	        				</div>
	        			</div>
        			</div>
        			<div class="row">
        				<div class="col-sm-12 col-xs-12 col-md-12">
        					<%=utilmsg.infoMessage("<b>Note :<br>1) Contract List Populated considering <b>END DATE + 180 Days</b>.<br>2) Split Option is Mandetory for Multiple Deal Selection.<br>3) for Single Deal Selection, Split Option is NOT Mandetory.</b>")%>
        				</div>
        			</div>
        			<div align="center" id="new_msg" style="display: none;"></div>
      			</div>
        	</div>
        	<div class="modal-footer cdfooter">
        		<div class="d-flex justify-content-between">
					<input type="button" class="btn btn-warning com-btn" value="Back" data-bs-target="#AddNewSecurity" data-bs-toggle="modal" data-bs-dismiss="modal">
					<input type="button" class="btn btn-warning com-btn" value="Submit" id="md_openBtn" onclick="doSubmitDealNo()">
						<!-- data-bs-target="#AddNewSecurity" data-bs-toggle="modal" data-bs-dismiss="modal"> -->
				</div>
      		</div>
        </div>
    </div>
</div>

<input type="hidden" name="option" value="CASH_COLLATERAL_MST">
<input type="hidden" name="opration" value="INSERT">
<input type="hidden" name="prev_clearance" value="<%=clearance%>">
<input type="hidden" name="clearance" value="<%=clearance%>">
<input type="hidden" name="temp_operation" value="">
<input type="hidden" name="sysdate" value="<%=sysdate%>">

<input type="hidden" name="form_cd" value="<%=formCd%>">
<input type="hidden" name="form_nm" value="<%=formNm%>">
<input type="hidden" name="mod_cd" value="<%=mod_cd%>">
<input type="hidden" name="mod_nm" value="<%=mod_nm%>">	
<input type="hidden" name="u" value="<%=u%>">		

</form>

<script>
function doDealEnabled(obj,index)
{
	var split_by = document.forms[0].split_by;
	
	if(obj.checked)
	{
		document.getElementById("deal_dtl_"+index).disabled=false;
		
		if(split_by[0].checked || split_by[1].checked)
		{
			document.getElementById("split_value_"+index).disabled=false;
			document.getElementById("split_value_"+index).value="";
		}
		else
		{
			document.getElementById("split_value_"+index).disabled=true;
			document.getElementById("split_value_"+index).value="";
		}
	}
	else
	{
		document.getElementById("deal_dtl_"+index).disabled=true;
		
		document.getElementById("split_value_"+index).disabled=true;
		document.getElementById("split_value_"+index).value="";
	}
	
	calcRemBalance();
}

function enableSplitInput(obj)
{
	var a="0";
	var b="0";
	
	var chk_deal = document.forms[0].chk_deal;
	var split_value = document.forms[0].split_value;
		
	if(obj.value=="P")
	{
		a="5"
		b="2"
	}
	else if(obj.value=="V")
	{
		a="12"
		b="2"
	}
	else
	{
		a="0"
		b="0"
	}
	
	if(obj.value=="P")
	{
		document.getElementById("span_bal_percent_id").style.display="";
		document.getElementById("span_bal_inr_id").style.display="none";
	}
	else if(obj.value=="V")
	{
		document.getElementById("span_bal_percent_id").style.display="none";
		document.getElementById("span_bal_inr_id").style.display="";
	}
	else
	{
		document.getElementById("span_bal_percent_id").style.display="none";
		document.getElementById("span_bal_inr_id").style.display="none";
	}
	
	
	if(chk_deal!=null && chk_deal.length!=undefined)
 	{
  		for(var i=0;i<chk_deal.length;i++)
  		{
  			if(obj.value=="P")
			{
				document.getElementById("span_percent_id_"+i).style.display="";
				document.getElementById("span_inr_id_"+i).style.display="none";
			}
			else if(obj.value=="V")
			{
				document.getElementById("span_percent_id_"+i).style.display="none";
				document.getElementById("span_inr_id_"+i).style.display="";
			}
			else
			{
				document.getElementById("span_percent_id_"+i).style.display="none";
				document.getElementById("span_inr_id_"+i).style.display="none";
			}
  			
  			split_value[i].setAttribute("onblur","negNumber(this);checkNumber1(this,"+a+","+b+");");
  			split_value[i].value="";
   			if(chk_deal[i].checked)
   			{
   				split_value[i].disabled=false;
   			}
   			else
   			{
   				split_value[i].disabled=true;
   			}
   		} 
 	}
 	else if(chk_deal!=null)
 	{
 		if(obj.value=="P")
		{
			document.getElementById("span_percent_id_0").style.display="";
			document.getElementById("span_inr_id_0").style.display="none";
		}
		else if(obj.value=="V")
		{
			document.getElementById("span_percent_id_0").style.display="none";
			document.getElementById("span_inr_id_0").style.display="";
		}
		else
		{
			document.getElementById("span_percent_id_0").style.display="none";
			document.getElementById("span_inr_id_0").style.display="none";
		}
 		
 		split_value.setAttribute("onblur","negNumber(this);checkNumber1(this,"+a+","+b+");");
 		split_value.value="";
 		if(chk_deal.checked)
		{
			split_value.disabled=false;
		}
		else
		{
			split_value.disabled=true;
		}
 	}
	
	calcRemBalance();
}

function calcRemBalance()
{
	var value = document.forms[0].value.value;
	if(trim(value)=="")
	{
		value=parseFloat("0");
	}
	var totPercent=parseFloat("100");
	
	var chk_deal = document.forms[0].chk_deal;
	var split_value = document.forms[0].split_value;
	
	var remBalance=parseFloat("0");
	var total=parseFloat("0");
	if(chk_deal!=null && chk_deal.length!=undefined)
 	{
  		for(var i=0;i<chk_deal.length;i++)
  		{
  			if(trim(split_value[i].value) != "")
  			{
  				total=total+parseFloat(split_value[i].value);
  			}
  		}
 	}
	else if(chk_deal!=null)
	{
		if(trim(split_value.value) != "")
		{
			total=total+parseFloat(split_value.value);
		}
	}
	
	if(document.forms[0].split_by[0].checked)
	{
		remBalance=round(parseFloat(value)-parseFloat(total),2);
	}
	else if(document.forms[0].split_by[1].checked)
	{
		remBalance=round(parseFloat(totPercent)-parseFloat(total),2);
	}
	
	document.forms[0].bal_split_value.value=remBalance;
}

$(document).ready(function() {	
	$('.serchtbl').each(function(k){
		$('#tbsearch'+k).each(function(j){						
			$('#tbsearch'+k+' th').each(function(i){
				var title = $(this).text();
				if($(this).hasClass('tbser'+k))
				{
					$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+''+k+'" onkeyup="Search(this,'+i+','+k+');" placeholder="Search '+title+'" style="width:100px"/></div>');
				}
			});		
		});
	});
});
	
function Search(obj, indx, tblid) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example"+tblid);
  	
  	tr = table.getElementsByTagName("tr");
  	for (i = 1; i < tr.length; i++) 
  	{
    	td = tr[i].getElementsByTagName("td")[indx];
    	//tbody=tr[i].getElementsByTagName("tbody");alert(tbody)
    	if (td) 
    	{
      		txtValue = td.textContent || td.innerText;
      		if (txtValue.toLocaleLowerCase().indexOf(filter) > -1) {
        		tr[i].style.display = "";
        		count++;
      		} else {
      			tr[i].style.display = "none";
      		}
    	}       
  	}
}

function fetchContPlantDtl(cont_mapping)
{
	var clearance = document.forms[0].clearance.value
	//document.getElementById("loading").style.visibility = "visible";
	//console.log("1")
	$.post("../servlet/DB_CreditRisk_Ajax?setCallType=CONTRACT_PLANT_BU_DTL&cont_mapping="+cont_mapping+"&clearance="+clearance, function(responseJson) {
		console.log(responseJson);
		var option = "<option value=''>---Select---</option>"
		var option1 = "<option value=''>---Select---</option>"
		
		var v1="";
		var v2="";
		
		$.each(responseJson, function(index, json) {
			$.each(json.PLANT_DTL, function(index_1, json_1) {
				option+="<option value="+json_1.SEQ_NO+">"+json_1.PLANT_NM+"</option>"
				
				v1=json_1.SEQ_NO;
			});
			
			$.each(json.BU_DTL, function(index_1, json_1) {
				option1+="<option value="+json_1.SEQ_NO+">"+json_1.PLANT_NM+"</option>"
				
				v2=json_1.SEQ_NO;
			});
		});
		//console.log("2")
		document.getElementById("contPlantBu").style.display = "";
		document.forms[0].plant_seq.innerHTML=option;
		document.forms[0].bu_unit.innerHTML=option1;
		//console.log("3")
		
		if(clearance=="I")
		{
			document.forms[0].plant_seq.value=v1
			document.forms[0].bu_unit.value=v2
		}
		
		document.getElementById("loading").style.visibility = "hidden";
		//console.log("5")
	});
}

function fetchTaxtDtl()
{
	var plant_seq = document.forms[0].plant_seq.value;
	var bu_unit = document.forms[0].bu_unit.value;
	var counterparty = document.forms[0].counterparty.value;
	var value=document.forms[0].value.value;
	var received_dt = document.forms[0].received_dt.value;
	var crdr = document.forms[0].crdr.value;
	
	var readFlag="readOnly";
	if(crdr=="DR")
	{
		readFlag="";
		document.forms[0].tax_amt.readOnly=false;
		document.forms[0].gross_amt.readOnly=false;
	}
	else
	{
		document.forms[0].tax_amt.readOnly=true;
		document.forms[0].gross_amt.readOnly=true;
	}
	
	if(plant_seq!="" && bu_unit!="" && counterparty!="" && value!="" && received_dt!="")
	{
		$.post("../servlet/DB_CreditRisk_Ajax?setCallType=TAX_DTL&counterparty_cd="+counterparty+"&bu_unit="+bu_unit+"&plant_seq="+plant_seq+"&value="+value+"&received_dt="+received_dt, function(responseJson) {
			console.log(responseJson);
			var tax_row="";
			$.each(responseJson, function(index, json) {
				$.each(json.TOTAL_TAX_DTL, function(index_1, json_1) {
					
					document.getElementById("taxInfo").innerHTML=json_1.TAX_STRUCT_DTL;
					document.forms[0].gross_amt.value=json_1.GROSS_AMT;
					document.forms[0].tax_amt.value=json_1.TAX_AMT;
					document.forms[0].tax_struct_cd.value=json_1.TAX_STRUCT_CD;
					document.forms[0].tax_struct_dtl.value=json_1.TAX_STRUCT_DTL;
				});
				
				$.each(json.SUB_TAX_DTL, function(index_1, subArray) {
					$.each(subArray, function(index_inner, json_1) {
						tax_row+="<div class='row m-b-5' "
						if(json.SUB_TAX_DTL.length == 1)
						{
							tax_row+="style='display:none;'"
						}
						tax_row+=">"
							+"<div class='col-sm-6 col-xs-6 col-md-6'>" 
							+"</div>"
							+"<div class='col-sm-2 col-xs-2 col-md-2'>" 
								+"<div class='form-group row' align='right'>"
									+"<label class='form-label'><b>"+json_1.SUB_TAX_STRUCT_DTL+"</b></label>"
								+"</div>"
							+"</div>"
							+"<div class='col-sm-4 col-xs-4 col-md-4'> " 
								+"<div class='form-group row'>"
									+"<div class='col-sm-12 col-xs-12 col-md-12'>"
										+"<input type='text' class='form-control form-control-sm' name='sub_tax_amt' value='"+json_1.SUB_TAX_AMT+"' onblur='negNumber(this);checkNumber1(this,11,2);taxCalc('');' "+readFlag+">"
										+"<input type='hidden' class='form-control form-control-sm' name='sub_tax_struct_cd' value='"+json_1.TAX_STRUCT_CD+"' readOnly>"
										+"<input type='hidden' class='form-control form-control-sm' name='sub_tax_struct_dtl' value='"+json_1.SUB_TAX_STRUCT_DTL+"' readOnly>"
										+"<input type='hidden' class='form-control form-control-sm' name='sub_tax_code' value='"+json_1.SUB_TAX_CODE+"' readOnly>"
										+"<input type='hidden' class='form-control form-control-sm' name='sub_tax_base_amt' value='"+json_1.SUB_TAX_BASE_AMT+"' readOnly>"
									+"</div>"
								+"</div>"
							+"</div>"
						+"</div>";
				    });
				});
			});
			document.getElementById("subTaxSegment").innerHTML=tax_row;
			document.getElementById("loading").style.visibility = "hidden";
		});
	}
	else
	{
		document.getElementById("loading").style.visibility = "hidden";
	}
}

function fetchSubTax(deal_no)
{
	var counterparty = document.forms[0].counterparty.value;
	var seq_no = document.forms[0].seq_no.value;
	var seq_rev_no = document.forms[0].seq_rev_no.value;
	var clearance = document.forms[0].clearance.value;
	
	var crdr = document.forms[0].crdr.value;
	var sec_type = document.forms[0].sec_type.value
	
	var readFlag="readOnly";
	if((deal_no.includes("O") || deal_no.includes("Q")) && sec_type=="ADV" && crdr=="DR")
	{
		document.forms[0].tax_amt.readOnly=true;
		document.forms[0].gross_amt.readOnly=true;
	}
	else if(crdr=="DR")
	{
		readFlag="";
		document.forms[0].tax_amt.readOnly=false;
		document.forms[0].gross_amt.readOnly=false;
	}
	else
	{
		document.forms[0].tax_amt.readOnly=true;
		document.forms[0].gross_amt.readOnly=true;
	}
	
	if(counterparty!="" && seq_no!="" && seq_rev_no!="" && clearance!="")
	{
		$.post("../servlet/DB_CreditRisk_Ajax?setCallType=SUB_TAX_DTL&counterparty_cd="+counterparty+"&seq_no="+seq_no+"&seq_rev_no="+seq_rev_no+"&clearance="+clearance, function(responseJson) {
			console.log(responseJson);
			var tax_row="";
			$.each(responseJson, function(index, json) {
				$.each(json.SUB_TAX_DTL, function(index_1, subArray) {
					$.each(subArray, function(index_inner, json_1) {
						tax_row+="<div class='row m-b-5' "
						if(json.SUB_TAX_DTL.length == 1)
						{
							tax_row+="style='display:none;'"
						}
						tax_row+=">"
							+"<div class='col-sm-6 col-xs-6 col-md-6'>" 
							+"</div>"
							+"<div class='col-sm-2 col-xs-2 col-md-2'>" 
								+"<div class='form-group row' align='right'>"
									+"<label class='form-label'><b>"+json_1.SUB_TAX_STRUCT_DTL+"</b></label>"
								+"</div>"
							+"</div>"
							+"<div class='col-sm-4 col-xs-4 col-md-4'> " 
								+"<div class='form-group row'>"
									+"<div class='col-sm-12 col-xs-12 col-md-12'>"
										+"<input type='text' class='form-control form-control-sm' name='sub_tax_amt' value='"+json_1.SUB_TAX_AMT+"' onblur='negNumber(this);checkNumber1(this,11,2);taxCalc('');' "+readFlag+">"
										+"<input type='hidden' class='form-control form-control-sm' name='sub_tax_struct_cd' value='"+json_1.TAX_STRUCT_CD+"' readOnly>"
										+"<input type='hidden' class='form-control form-control-sm' name='sub_tax_struct_dtl' value='"+json_1.SUB_TAX_STRUCT_DTL+"' readOnly>"
										+"<input type='hidden' class='form-control form-control-sm' name='sub_tax_code' value='"+json_1.SUB_TAX_CODE+"' readOnly>"
										+"<input type='hidden' class='form-control form-control-sm' name='sub_tax_base_amt' value='"+json_1.SUB_TAX_BASE_AMT+"' readOnly>"
									+"</div>"
								+"</div>"
							+"</div>"
						+"</div>";
				    });
				});
			});
			document.getElementById("subTaxSegment").innerHTML=tax_row;
			document.getElementById("loading").style.visibility = "hidden";
		});
	}
	else
	{
		document.getElementById("loading").style.visibility = "hidden";
	}
}

function fetchReceiptVoucher(cont_mapping,att_voucher)
{
	var clearance = document.forms[0].clearance.value
	var count=parseInt("0");
	//document.getElementById("loading").style.visibility = "visible";
	
	$.post("../servlet/DB_CreditRisk_Ajax?setCallType=FETCH_RECEIPT_VOUCHER_DTL&cont_mapping="+cont_mapping+"&clearance="+clearance, function(responseJson) {
		console.log(responseJson);
		var option = "<option value=''>---Select---</option>"
		
		$.each(responseJson, function(index, json) {
			$.each(json.RECEIPT_VOUCHER_DTL, function(index_1, json_1) {
				option+="<option value="+json_1.RECEIPT_VOUCHER+">"+json_1.RECEIPT_VOUCHER+"</option>"
				if(att_voucher==json_1.RECEIPT_VOUCHER)
				{
					count++;
				}
			});
		});
		
		if(parseInt(count) == 0 && att_voucher!="")
		{
			option+="<option value="+att_voucher+">"+att_voucher+"</option>"
		}
		document.forms[0].receipt_voucher.innerHTML=option;
		
		document.getElementById("loading").style.visibility = "hidden";
		//console.log("5")
	});
}

function fetchSelectedReceiptVoucherDtl(obj)
{
	var clearance = document.forms[0].clearance.value
	var receipt_voucher=obj.value;
	var readFlag="readOnly";
	var taxAmt=parseFloat("0");
	var tax_struct_cd="";
	var tax_struct_dtl="";
	var totalVal=parseFloat("0");
	
	if(obj.value!="")
	{
		document.forms[0].crdr.style.pointerEvents="none";
		document.forms[0].crdr.style.background="#e9ecef";
		
		$.post("../servlet/DB_CreditRisk_Ajax?setCallType=FETCH_SELECTED_RECEIPT_VOUCHER_DTL&receipt_voucher="+receipt_voucher+"&clearance="+clearance, function(responseJson) {
			console.log(responseJson);
			var tax_row="";
			$.each(responseJson, function(index, json) {
				$.each(json.PLANT_DTL, function(index_1, json_1) {
					
					document.forms[0].plant_seq.value=json_1.PLANT_SEQ
					document.forms[0].bu_unit.value=json_1.BU_SEQ
					
					document.forms[0].plant_seq.style.pointerEvents="none";
					document.forms[0].bu_unit.style.pointerEvents="none";
					
					document.forms[0].plant_seq.style.background="#e9ecef";
					document.forms[0].bu_unit.style.background="#e9ecef";
					
					document.forms[0].gross_amt.value=json_1.BALANCE;
					
					totalVal+=parseFloat(json_1.BALANCE);
					
				});
				
				$.each(json.TAX_DTL, function(index_1, subArray) {
					$.each(subArray, function(index_inner, json_1) {
						
						if(json_1.BALANCE != "")
						{
							taxAmt+=parseFloat(json_1.BALANCE);
						}
						
						totalVal+=parseFloat(json_1.BALANCE);
						
						tax_struct_cd=json_1.TAX_STRUCT_CD;
						tax_struct_dtl=json_1.TAX_STRUCT_DTL;
						
						tax_row+="<div class='row m-b-5' "
						if(json.TAX_DTL.length == 1)
						{
							tax_row+="style='display:none;'"
						}
						tax_row+=">"
							+"<div class='col-sm-6 col-xs-6 col-md-6'>" 
							+"</div>"
							+"<div class='col-sm-2 col-xs-2 col-md-2'>" 
								+"<div class='form-group row' align='right'>"
									+"<label class='form-label'><b>"+json_1.SUB_TAX_STRUCT_DTL+"</b></label>"
								+"</div>"
							+"</div>"
							+"<div class='col-sm-4 col-xs-4 col-md-4'> " 
								+"<div class='form-group row'>"
									+"<div class='col-sm-12 col-xs-12 col-md-12'>"
										+"<input type='text' class='form-control form-control-sm' name='sub_tax_amt' value='"+json_1.BALANCE+"' onblur='negNumber(this);checkNumber1(this,11,2);taxCalc('');' "+readFlag+">"
										+"<input type='hidden' class='form-control form-control-sm' name='sub_tax_struct_cd' value='"+json_1.TAX_STRUCT_CD+"' readOnly>"
										+"<input type='hidden' class='form-control form-control-sm' name='sub_tax_struct_dtl' value='"+json_1.SUB_TAX_STRUCT_DTL+"' readOnly>"
										+"<input type='hidden' class='form-control form-control-sm' name='sub_tax_code' value='"+json_1.SUB_TAX_CODE+"' readOnly>"
										+"<input type='hidden' class='form-control form-control-sm' name='sub_tax_base_amt' value='"+json_1.SUB_TAX_BASE_AMT+"' readOnly>"
									+"</div>"
								+"</div>"
							+"</div>"
						+"</div>";
				    });
				});
				
				document.forms[0].tax_amt.value=round(parseFloat(taxAmt),2);
				document.forms[0].tax_struct_cd.value=tax_struct_cd;
				document.forms[0].tax_struct_dtl.value=tax_struct_dtl;
				document.getElementById("taxInfo").innerHTML=tax_struct_dtl;
			});
			document.getElementById("subTaxSegment").innerHTML=tax_row;
			document.getElementById("loading").style.visibility = "hidden";
			
			document.getElementById("taxRefesh").style.display="none";
			document.forms[0].value.value=round(parseFloat(totalVal),2);
	
		});	
	}
	else
	{
		document.forms[0].crdr.style.pointerEvents="auto";
		document.forms[0].crdr.style.background="";
		
		document.forms[0].plant_seq.style.pointerEvents="auto";
		document.forms[0].bu_unit.style.pointerEvents="auto";
		
		document.forms[0].plant_seq.style.background="";
		document.forms[0].bu_unit.style.background="";
		
		document.forms[0].gross_amt.value="";
		
		document.forms[0].tax_amt.value="";
		document.forms[0].tax_struct_cd.value="";
		document.forms[0].tax_struct_dtl.value="";
		document.getElementById("taxInfo").innerHTML="";
		
		document.getElementById("subTaxSegment").innerHTML="";
		
		document.getElementById("taxRefesh").style.display="";
		document.forms[0].value.value="";
	}
}
</script>
</body>
</html>