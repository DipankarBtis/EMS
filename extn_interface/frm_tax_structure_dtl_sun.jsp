<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>

function enable_disable(index,size)
{
	var chk = document.forms[0].chk;
	
	size = parseInt(size)>1?parseInt(size):0;
	var bu_seq_vec = document.forms[0].bu_seq.value;
	
	var bu_seq = bu_seq_vec.substring(1,bu_seq_vec.length-1);
	var bu_seq_arr = bu_seq.split(",");
	
	if(chk!=null && chk!=undefined)
	{
		if(chk.length!=undefined)
		{
			for(j=0;j<bu_seq_arr.length;j++)
			{
				var t = bu_seq_arr[j].trim();
				if(chk[index].checked)
				{
					if(size>0)
					{
						for(i=0;i<size;i++)
						{
							document.getElementById("sun_cd_"+index+"_"+t+"_"+size+"_"+(i+1)).disabled=false;
							document.getElementById("sug_cd_"+index+"_"+t+"_"+size+"_"+(i+1)).disabled=false;
							var sun_cd = document.getElementById("sun_cd_"+index+"_"+t+"_"+size+"_"+(i+1));
						}
					}
					else
					{
						document.getElementById("sun_cd_"+index+"_"+t+"_"+size+"_"+0).disabled=false;
						document.getElementById("sug_cd_"+index+"_"+t+"_"+size+"_"+0).disabled=false;
						var sun_cd = document.getElementById("sun_cd_"+index+"_"+t+"_"+size+"_"+0);
					}
				}
				else
				{
					if(size>0)
					{
						for(i=0;i<size;i++)
						{
							document.getElementById("sun_cd_"+index+"_"+t+"_"+size+"_"+(i+1)).disabled=true;
							document.getElementById("sug_cd_"+index+"_"+t+"_"+size+"_"+(i+1)).disabled=true;
						}
					}
					else
					{
						document.getElementById("sun_cd_"+index+"_"+t+"_"+size+"_"+0).disabled=true;
						document.getElementById("sug_cd_"+index+"_"+t+"_"+size+"_"+0).disabled=true;
					}
				}
			}
		}
		else
		{
			for(j=0;j<bu_seq_arr.length;j++)
			{
				var t = bu_seq_arr[j].trim();
				if(chk.checked)
				{
					if(size>0)
					{
						for(i=0;i<size;i++)
						{
							document.getElementById("sun_cd_"+index+"_"+t+"_"+size+"_"+(i+1)).disabled=false;
							document.getElementById("sug_cd_"+index+"_"+t+"_"+size+"_"+(i+1)).disabled=false;
							var sun_cd = document.getElementById("sun_cd_"+index+"_"+t+"_"+size+"_"+(i+1));
						}
					}
					else
					{
						document.getElementById("sun_cd_"+index+"_"+t+"_"+size+"_"+0).disabled=false;
						document.getElementById("sug_cd_"+index+"_"+t+"_"+size+"_"+0).disabled=false;
						var sun_cd = document.getElementById("sun_cd_"+index+"_"+t+"_"+size+"_"+0);
					}
				}
				else
				{
					if(size>0)
					{
						for(i=0;i<size;i++)
						{
							document.getElementById("sun_cd_"+index+"_"+t+"_"+size+"_"+(i+1)).disabled=true;
							document.getElementById("sug_cd_"+index+"_"+t+"_"+size+"_"+(i+1)).disabled=true;
						}
					}
					else
					{
						document.getElementById("sun_cd_"+index+"_"+t+"_"+size+"_"+0).disabled=true;
						document.getElementById("sug_cd_"+index+"_"+t+"_"+size+"_"+0).disabled=true;
					}
				}
			}
		}
	}
	
	
}

function hide_show(id1,id2)
{	
	if(document.getElementById(id1).style.display=='none')
	{
		document.getElementById(id1).style.display='table-row-group';
		document.getElementById(id2).className='fa fa-compress';
	}
	else
	{
		document.getElementById(id1).style.display='none';
		document.getElementById(id2).className='fa fa-expand';
	}
} 

function doSubmit()
{
	var chk = document.forms[0].chk;
	var size = document.forms[0].size;
	var bu_seq_vec = document.forms[0].bu_seq.value;
	var index_arr=new Array();
	
	var bu_seq = bu_seq_vec.substring(1,bu_seq_vec.length-1);
	var bu_seq_arr = bu_seq.split(",");
	
	var chk_ctn=0;
	var m=0
	if(chk!=null && chk!=undefined)
	{
		if(chk.length!=undefined)
		{
			for(i=0;i<chk.length;i++)
			{
				for(j=0;j<bu_seq_arr.length;j++)
				{
					var c=0;
					var t = bu_seq_arr[j].trim();
					var sun_cd = document.getElementsByName("sun_cd_"+t);
					if(chk[i].checked)
					{
						chk_ctn++;
						index_arr[m]=i;
						m++;
						for(j=0;j<bu_seq_arr.length;j++)
						{
							var c=0;
							var t = bu_seq_arr[j].trim();
							var sun_cd = document.getElementsByName("sun_cd_"+t);
							if(size[i].value>1)
							{
								for(k=0;k<size[i].value;k++)
								{
									c=i+k+1;
								}
							}
							else
							{
								c+=1;
							}
						}
					}
						
				}
			} 
		}
		else
		{
			if(chk.checked)
			{
				for(j=0;j<bu_seq_arr.length;j++)
				{
					chk_ctn++;
					index_arr[m]=0;
				}
			}
		}
	}
	
	
	if(chk_ctn==0)
	{
		alert("Please check atleast one tax structure code!");
	}
	else
	{
		var a=confirm("Are you sure you want to submit?");
		if(a)
		{
			document.forms[0].index.value = index_arr;
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}	
}

function chkNumber(obj)
{
	if(isNaN(obj.value))
	{
		alert("SUN Code must be Numeric!");
		obj.value='';
		return false;
	}
}

function exportToXls()
{
	
	var url = "xls_tax_structure_dtl_sun.jsp?fileName=Tax_Sun_Account_Code.xls";

	location.replace(url);
}
</script>

</head>
<jsp:useBean class="com.etrm.fms.extn_interface.DataBean_sun_interface" id="sun_master" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
sun_master.setComp_cd(owner_cd);
sun_master.setCallFlag("TAX_STRUCTURE_SUN");
sun_master.init();

Vector VMASTER_TAX_CATEGORY = sun_master.getVMASTER_TAX_CATEGORY();
Vector VMASTER_TAX_CATEGORY_NM = sun_master.getVMASTER_TAX_CATEGORY_NM();

Vector VCO_CD = sun_master.getVCO_CD();
Vector VBU_SEQ = sun_master.getVBU_SEQ();
Vector VBU_ABBR = sun_master.getVBU_ABBR();
Vector VBU_STATE = sun_master.getVBU_STATE();

Vector VTAX_STRUCT_CD = sun_master.getVTAX_STRUCT_CD();
Vector VTAX_STRUCT_NM = sun_master.getVTAX_STRUCT_NM();
Vector VTAX_CATEGORY_NM = sun_master.getVTAX_CATEGORY_NM();
Vector VTAX_STRUCT_APP_DT = sun_master.getVTAX_STRUCT_APP_DT();
Vector VTAX_STRUCT_STATUS = sun_master.getVTAX_STRUCT_STATUS();
Vector VPAY_RECV_NM = sun_master.getVPAY_RECV_NM();
Vector VTAX_STRUCT_RMK = sun_master.getVTAX_STRUCT_RMK();
Vector VINDEX = sun_master.getVINDEX();
Vector VTAX_COUNT = sun_master.getVTAX_COUNT();
Vector VSUB_TAX_STRUCT_NM = sun_master.getVSUB_TAX_STRUCT_NM();
Vector VSUN_CD = sun_master.getVSUN_CD();
Vector VSUG_CD = sun_master.getVSUG_CD();
Vector VSUB_SUN_CD = sun_master.getVSUB_SUN_CD();
Vector VSUB_SUG_CD = sun_master.getVSUB_SUG_CD();
%>
<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_sun_interface">
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
							Tax Sun Account Code
						</div>
						<div>
					    	<div class="form-group row">
								<div class="col-auto">
				   	 				<span class="input-group-text">
									 	<i class="fa fa-file-excel-o fa-2x" style="color: green;" onclick="exportToXls();"></i>
									</span>	
								</div>
							</div> 					    
						</div>				  	
					</div>
				</div>
				<%int i=0,k=0,sub=0,c=0,p=0;
				for(int j=0; j<VMASTER_TAX_CATEGORY.size(); j++){ 
					int index = Integer.parseInt(""+VINDEX.elementAt(j));
				%>
					<div class="card-body cdbody">
						<div class="row m-b-5">
							<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=VMASTER_TAX_CATEGORY_NM.elementAt(j) %></label>
						</div>
						<div class="row">
							<div class="table-responsive">
								<table class="table table-bordered table-hover" id="example">
									<thead id="tbsearch">
										<tr>
											<th rowspan="2"></th>
											<th rowspan="2">Tax Structure<br>(Internal Code)</th>
											<th rowspan="2">Tax Structure</th>
											<th rowspan="2">Tax Category</th>
											<th rowspan="2">Commencement on</th>
											<th rowspan="2">Status</th>
											<th rowspan="2">Payable / Receivable</th>
											<th rowspan="2">Remark</th>
											<%-- <th colspan="<%=VBU_SEQ.size()%>">BU</th> --%>
											<%for(int bu_seq=0;bu_seq<VBU_SEQ.size();bu_seq++){%>
												<th <%if(j==0){%>colspan=1<%}else{%>colspan=2<%} %>><%=VCO_CD.elementAt(bu_seq)%>-<%=VBU_ABBR.elementAt(bu_seq)%><br>(<%=VBU_STATE.elementAt(bu_seq)%>)</th>
											<%} %>
										</tr>
											<%for(int bu_seq=0;bu_seq<VBU_SEQ.size();bu_seq++){%>
												<th>SUN Account Code</th>
												<th <%if(j==0){%>style="display:none;"<%} %>>SUG Account Code</th>
											<%} %>
										<tr>
										</tr>
									</thead>
									<tbody>
									<%k=0;
									if(index > 0){ %>
										<%for(i=i; i<VTAX_STRUCT_CD.size(); i++){ 
											k+=1;
											int size =Integer.parseInt(""+VTAX_COUNT.elementAt(i));
										%>
											<tr>
												<td align="center">
													<input type="checkbox" class="form-check-input" name="chk" id="chk<%=i%>" onchange="enable_disable('<%=i%>','<%=size%>')">
													<input type="hidden" name="size" value="<%=size %>">
													<input type="hidden" name="vtax_struct_cd" value="<%=VTAX_STRUCT_CD.elementAt(i) %>">
												</td>
												<td align="center"><%=VTAX_STRUCT_CD.elementAt(i)%></td>
												<%if(size>1){%>
													<td onclick="hide_show('tbody<%=i %>','hidCont<%=i%>');">
														<%=VTAX_STRUCT_NM.elementAt(i)%>
														&nbsp;&nbsp;&nbsp;<span id="hidCont<%=i%>" class="fa fa-expand" title="Click here to edit for different tax code!"></span>
													</td> 
												<%}else{%>
													<td><%=VTAX_STRUCT_NM.elementAt(i)%></td>
												<%}%>
												<td align="center"><%=VTAX_CATEGORY_NM.elementAt(i)%></td>
												<td align="center"><%=VTAX_STRUCT_APP_DT.elementAt(i)%></td>
												<td align="center">
													<div align="center">
														<font style="color:<%if(VTAX_STRUCT_STATUS.elementAt(i).equals("1")){%>#a6ff4d<%}else{%>red<%}%>">
															<i class="fa fa-circle fa-lg" ></i>
															&nbsp;
														</font>
														<%if(VTAX_STRUCT_STATUS.elementAt(i).equals("1")){%>
														Active
														<%}else{ %>
														In-Active
														<%} %>
													</div>
												</td>
												<td align="center"><%=VPAY_RECV_NM.elementAt(i) %></td>
												<td><%=VTAX_STRUCT_RMK.elementAt(i)%></td>
												<%for(int bu_seq=0;bu_seq<VBU_SEQ.size();bu_seq++){%>
													<td>
														<input type="text" class="form-control form-control-sm" name="sun_cd_<%=VBU_SEQ.elementAt(bu_seq)%>" id="sun_cd_<%=i%>_<%=VBU_SEQ.elementAt(bu_seq)%>_0_0" 
														value="<%=VSUN_CD.elementAt(p)%>" style="text-align:right;"  onchange="chkNumber(this);" disabled >
													</td>
													<td <%if(j==0){%>style="display:none;"<%} %>>
														<input type="text" <%if(j==0){%>style="display:none;"<%} %> class="form-control form-control-sm" name="sug_cd_<%=VBU_SEQ.elementAt(bu_seq)%>" id="sug_cd_<%=i%>_<%=VBU_SEQ.elementAt(bu_seq)%>_0_0" 
														value="<%=VSUG_CD.elementAt(p++) %>" style="text-align:right;"  onchange="chkNumber(this);" disabled >
													</td>
												<%} %>
											</tr>
											<%if(size>1){ %>
										    <tbody id="tbody<%=i%>" style="display:none;">
										        <%for(int n=0;n<size;n++){
										        %>
										            <tr>
										                <td colspan="8" rowspan="<%=size%>" align="right"><%=VSUB_TAX_STRUCT_NM.elementAt(sub) %></td>
										            </tr>
										            <tr>
										                <%for(int bu_seq=0; bu_seq<VBU_SEQ.size(); bu_seq++){%>
										                    <td>
										                        <input type="text" class="form-control form-control-sm" name="sun_cd_<%=VBU_SEQ.elementAt(bu_seq) %>" id="sun_cd_<%=i%>_<%=VBU_SEQ.elementAt(bu_seq)%>_<%=size%>_<%=n+1%>" 
										                        value="<%=VSUB_SUN_CD.elementAt(c) %>" onchange="chkNumber(this);"  style="text-align:right;" disabled>
										                    </td>
										                    <td <%= (j == 0) ? "style='display:none;'" : "" %>> 
										                        <input type="text" class="form-control form-control-sm" name="sug_cd_<%=VBU_SEQ.elementAt(bu_seq)%>" id="sug_cd_<%=i%>_<%=VBU_SEQ.elementAt(bu_seq)%>_<%=size%>_<%=n+1%>" 
										                        value="<%=VSUB_SUG_CD.elementAt(c++) %>" onchange="chkNumber(this);"  style="text-align:right;" <%if(j==0){%>style="display:none;"<%}%> disabled>
										                    </td>
										                <%} %>
										            </tr>
										        <%sub+=1;} %>
										    </tbody>
										<%} %>

										<%
											if(k==index)
											{
												i=i+1;
												break;
											}
										} %>
									<%}else{%>
										<tr>
											<td align="center" colspan="<%=(8+VBU_SEQ.size())%>"><%=utilmsg.infoMessage("<b>No Tax Structure is Configured!</b>") %></td>
										</tr>
									<%} %>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				<%}%>
				<div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
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
</div>
<input type="hidden" name="bu_seq" value="<%=VBU_SEQ%>">
<input type="hidden" name="option" value="TAX_STRUCTURE_SUN">
<input type="hidden" name="index" value="">


<input type="hidden" name="read_access" value="<%=read_access%>">
<input type="hidden" name="write_access" value="<%=write_access%>">
<input type="hidden" name="check_access" value="<%=check_access%>">
<input type="hidden" name="print_access" value="<%=print_access%>">
<input type="hidden" name="delete_access" value="<%=delete_access%>">  	
<input type="hidden" name="audit_access" value="<%=audit_access%>">
<input type="hidden" name="authorize_access" value="<%=authorize_access%>">
<input type="hidden" name="approve_access" value="<%=approve_access%>">
<input type="hidden" name="execute_access" value="<%=execute_access%>">
<input type="hidden" name="form_cd" value="<%=formCd%>">
<input type="hidden" name="form_nm" value="<%=formNm%>">
<input type="hidden" name="mod_cd" value="<%=mod_cd%>">
<input type="hidden" name="mod_nm" value="<%=mod_nm%>">
<input type="hidden" name="u" value="<%=u%>">
</form>

</body>

</html>