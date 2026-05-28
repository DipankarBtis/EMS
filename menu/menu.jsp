<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@page import="java.util.Vector"%>
<%@ page import="java.util.Set" %>
<script>
function changePage(form_path,u)
{
	document.getElementById("loading").style.visibility = "visible";
	var url = form_path+"?u="+u;
			
	location.replace(url);
}

function logout()
{
	var url = "../home/logout.jsp";
	location.replace(url);
	
}

function homepage()
{
	var url = "../home/fms.jsp";
	location.replace(url);
	
}

async function changeProfile(comp_cd, exists_comp_abbr, comp_abbr)
{
    const activeTabsCnt = await fetchActiveTabCount(); 
	if((parseInt(activeTabsCnt))>1)
	{
		alert("Multiple Tabs "+(parseInt(activeTabsCnt))+" are open for the Application!\nProfile switching is permitted only when a single tab is open!");
	}
	else
	{
		var a = confirm("Do you want to Switch Profile "+exists_comp_abbr+" to "+comp_abbr+"?");
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			var url = "../home/profile_selection.jsp?comp_profile="+comp_cd;
				
			location.replace(url);
		}
	}
}

function fetchActiveTabCount() {
	const servletURL = '<%=request.getContextPath()%>/TabTrackerServlet';
    return fetch(servletURL, {
        method: 'POST',
        body: new URLSearchParams({ action: 'getCount' })
    })
    .then(res => res.json())
    .then(data => {
        const tabCount = data.tabCount;   //activeTabs.size()
        console.log('Active tabs:', tabCount);
        return tabCount;
    });
}
</script>

<style type="text/css">
.navbar .megamenu{ padding: 1rem; }
/* ============ desktop view ============ */
@media all and (min-width: 992px) {
	.navbar .has-megamenu{position:static!important;}
	.navbar .megamenu{left:15%; right:15%; width:auto; margin-top:0;  }
}	
/* ============ desktop view .end// ============ */
/* ============ mobile view ============ */
@media(max-width: 991px){
	.navbar.fixed-top .navbar-collapse, .navbar.sticky-top .navbar-collapse{
		overflow-y: auto;
	    max-height: 90vh;
	    margin-top:10px;
	}
}
/* ============ mobile view .end// ============ */
</style>

<jsp:useBean class="com.etrm.fms.util.DataBean_Menu" id="menu" scope="page"></jsp:useBean>
<%
String username = "";
String menu_emp_cd = "";
String comp_cd="";
String comp_logo="";
int activeTabsCnt=0;
if(session.getAttribute("emp_uid")==null||session.getAttribute("emp_uid")==""||session.getAttribute("emp_uid").toString().equals("null"))
{
	username="";
}  
else
{
	username=""+session.getAttribute("emp_uid");
}

if(session.getAttribute("emp_cd")==null||session.getAttribute("emp_cd")==""||session.getAttribute("emp_cd").toString().equals("null"))
{
	menu_emp_cd="";
}  
else
{
	menu_emp_cd=""+session.getAttribute("emp_cd");
}

if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	comp_cd="";
}  
else
{
	comp_cd=""+session.getAttribute("comp_cd");
}

if(session.getAttribute("comp_logo")==null||session.getAttribute("comp_logo")==""||session.getAttribute("comp_logo").toString().equals("null"))
{
	comp_logo="";
}  
else
{
	comp_logo=""+session.getAttribute("comp_logo");
}

//Set<String> activeTabs = (Set<String>) session.getAttribute("activeTabs");
//activeTabsCnt = activeTabs.size();

menu.setCallFlag("MODULE_MST");
menu.setUsername(username);
menu.setComp_cd(comp_cd);
menu.setEmpCd(menu_emp_cd);
menu.init();

Vector VMENU_MODULE_CD = menu.getVMENU_MODULE_CD();
Vector VMENU_MODULE_NM = menu.getVMENU_MODULE_NM();

Vector VMENU_COMPANY_CD = menu.getVMENU_COMPANY_CD();
Vector VMENU_COMPANY_NM = menu.getVMENU_COMPANY_NM();
Vector VMENU_COMPANY_ABBR = menu.getVMENU_COMPANY_ABBR();
String menu_default_prof=menu.getDefault_profile();
%>
<nav class="navbar navbar-expand-lg navbar-dark" style="background-color: var(--header_color);">
	<div class="container-fluid">
    	<a class="navbar-brand" href="#" style="padding-top: 2px;padding-left: 4px;padding-right: 2px;" onclick="homepage();">
    		<img src="<%if(!comp_logo.equals("")){%>../<%=CommonVariable.company_logo_path %>/<%=comp_logo%><%} %>" height="30px">&nbsp;<font style="color:var(--header_font_color); font-size:24px;"> <%=CommonVariable.app_name%></font>
    	</a>
    	<button class="navbar-toggler" style="border-color:var(--header_font_color);" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      		<!-- <span class="navbar-toggler-icon"></span> -->
      		<font><i class="fa fa-bars" style="color:var(--header_font_color);"></i></font>
    	</button>
    	<div class="collapse navbar-collapse" id="navbarSupportedContent">
      		<ul class="navbar-nav me-auto mb-2 mb-lg-0" style="flex-wrap: wrap;">
      		<%for(int i=0; i < VMENU_MODULE_CD.size(); i++){%>
        		<li class="nav-item dropdown has-megamenu" >
          			<a class="nav-link dropdown-toggle" style="padding: 3px;" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            		<%=VMENU_MODULE_NM.elementAt(i)%>
          			</a>
          			<div class="dropdown-menu megamenu" role="menu">
						<div class="row g-3">
							<div class="col-sm-12 col-xs-12 col-md-12">  
								<div class="form-group row">
					          	<%
					    		menu.setCallFlag("MENU_MST");
					    		menu.setUsername(username);
					    		menu.setComp_cd(comp_cd);
					    		menu.setModule_cd(""+VMENU_MODULE_CD.elementAt(i));
					    		menu.init();
					    		
					    		Vector VFORM_CD = menu.getVFORM_CD();
					    		Vector VFORM_NM = menu.getVFORM_NM();
					    		Vector VFORM_PATH = menu.getVFORM_PATH();
					    		Vector VREAD_ACCESS = menu.getVREAD_ACCESS();
					    		Vector VWRITE_ACCESS = menu.getVWRITE_ACCESS();
					    		Vector VCHECK_ACCESS = menu.getVCHECK_ACCESS();
					    		Vector VPRINT_ACCESS = menu.getVPRINT_ACCESS();
					    		Vector VDELETE_ACCESS = menu.getVDELETE_ACCESS();
					    		Vector VAUDIT_ACCESS = menu.getVAUDIT_ACCESS();
					    		Vector VAUTHORIZE_ACCESS = menu.getVAUTHORIZE_ACCESS();
					    		Vector VAPPROVE_ACCESS = menu.getVAPPROVE_ACCESS();
					    		Vector VEXECUTE_ACCESS = menu.getVEXECUTE_ACCESS();
					    		Vector VGRP_CD = menu.getVGRP_CD();
					    		Vector VU = menu.getVU();
					    		Vector VSEI_CENTRAL = menu.getVSEI_CENTRAL();
					    		
					    		Vector VSUB_MENU_CD = menu.getVSUB_MENU_CD();
					    		Vector VSUB_MENU_NM = menu.getVSUB_MENU_NM();
					    		
					    		for(int k=0; k<VSUB_MENU_CD.size(); k++)
					    		{%>
					    			<div class="col-auto"> 
										<div class="col-megamenu">
					            			<ul>
					              				<li><a class="dropdown-item"><%=VSUB_MENU_NM.elementAt(k)%></a></li>
								              	<%for(int j=0; j<VFORM_CD.size(); j++)
									    		{
									    			if(VSUB_MENU_CD.elementAt(k).equals(VGRP_CD.elementAt(j)))
									    			{%>
											    		<li>
											            	<a class="dropdown-item" href="#" onclick="changePage('<%=VFORM_PATH.elementAt(j)%>','<%=VU.elementAt(j)%>')">
																<%=VFORM_NM.elementAt(j)%><%if(VSEI_CENTRAL.elementAt(j).toString().equals("Y")){%>&nbsp;<span style="color:var(--top_header_font_color);"><i class="fa fa-universal-access" aria-hidden="true"></i></span><%}%>
															</a>
														</li>
											         <%} %>
											     <%} %>
					            			</ul>
					            		</div>
					            	</div>
			            		<%} 
			            		VFORM_CD.clear();
			            		VFORM_NM.clear();
			            		VFORM_PATH.clear();
			            		VREAD_ACCESS.clear();
			            		VWRITE_ACCESS.clear();
			            		VCHECK_ACCESS.clear();
			            		VPRINT_ACCESS.clear();
			            		VDELETE_ACCESS.clear();
			            		VAUDIT_ACCESS.clear();
			            		VAUTHORIZE_ACCESS.clear();
			            		VAPPROVE_ACCESS.clear();
			            		VEXECUTE_ACCESS.clear();
			            		VGRP_CD.clear();
			            		VU.clear();
			            		VSEI_CENTRAL.clear();
			            		VSUB_MENU_CD.clear();
			            		VSUB_MENU_NM.clear();
			            		%>
			            		</div>
			            	</div>
			            </div>
        			</div>
        		</li>
        		<%} %>
      		</ul>
      		<div class="d-flex navbar-brand">
	      		<label class="btn" onclick="" style="color:var(--header_font_color); padding-left:0px;"><b><span id = "min"></span>:<span id = "sec"></span></b></label>
	      		<label class="btn" onclick="" style="color:var(--header_font_color); padding-left:0px;">
	      			<b>
	      				<i class="fa fa-building-o fa-lg"></i>&nbsp;<%=session.getAttribute("comp_abbr")==null?"":session.getAttribute("comp_abbr")%>
	      			</b>
	      		</label>
	      		<div class="flex-shrink-0 dropdown">
			   		<a href="#" class="d-block link-dark text-decoration-none dropdown-toggle" id="dropdownUser2" data-bs-toggle="dropdown" aria-expanded="false" style="color:var(--header_font_color);">
			  			<!--  <img src="../<%=CommonVariable.company_logo_path %>/default_user.png" alt="mdo" width="32" height="32" class="rounded-circle"> -->
			            <i class="fa fa-user-circle-o fa-2x" aria-hidden="true" > </i>
			   		</a>
			   		<ul class="dropdown-menu profile_dp_menu dropdown-menu-end text-small shadow" aria-labelledby="dropdownUser2" data-popper-placement="bottom-end"> 
			   		<!-- style="position: absolute;inset: 0px auto auto 0px;margin: 0px;transform: translate(-80px, 34px);" -->
	            		<li><a class="dropdown-item" href="#"><i class="fa fa-user-circle fa-lg"></i>&nbsp;<%=session.getAttribute("emp_nm")==null?"":session.getAttribute("emp_nm")%></a></li>
	            		<li><hr class="dropdown-divider"></li>
	            		<li><a class="dropdown-item" href="#" style="font-weight: bold;font-size: var(--header_font_size);text-transform: uppercase;color: #dc3545; pointer-events: none;">Switch Profile</a></li>
			            <%for(int i=0; i<VMENU_COMPANY_CD.size(); i++){ %>
			            <li>
			            	<a class="dropdown-item" href="#" onclick="changeProfile('<%=VMENU_COMPANY_CD.elementAt(i)%>','<%=session.getAttribute("comp_abbr")==null?"":session.getAttribute("comp_abbr")%>','<%=VMENU_COMPANY_ABBR.elementAt(i)%>');"
			            	<%if(comp_cd.equals(VMENU_COMPANY_CD.elementAt(i).toString())) { %>
			            		style="background-color: #95c674;color:white;font-weight:bold;pointer-events: none;"
			            	<%} %>>
			            	<%=VMENU_COMPANY_ABBR.elementAt(i)%>&nbsp;<%if(menu_default_prof.equals(VMENU_COMPANY_CD.elementAt(i).toString())){ %><i class="fa fa-dot-circle-o"></i><%} %>
			            	</a>
			           	</li>
			            <%}%>
			            <li><hr class="dropdown-divider"></li>
			            <li><a class="dropdown-item" href="#" onclick="logout();"><b><i title="Logout" class="fa fa-sign-out fa-lg"></i>&nbsp;Sign Out</b></a></li>
					</ul>
				</div>
			</div>
    	</div>
  	</div>
</nav>

<%-- <nav aria-label="breadcrumb">
  <ol class="breadcrumb">
    <li class="breadcrumb-item">Home</li>
    <li class="breadcrumb-item"><%=mod_nm%></li>
    <li class="breadcrumb-item active" aria-current="page"><%=formNm%></li>
  </ol>
</nav> --%>

<!-- Alert Model -->
<div class="modal fade" id="myAlertModal" style="z-index: 3055;" data-bs-backdrop="static" data-bs-keyboard="false"> 
	<div class="modal-dialog modal-dialog-scrollable">
    	<div class="modal-content">
    		<div class="modal-header">
        		<div class="topheader">
					Alert
				</div>
      		</div>
      		<div class="modal-body">
      			<p id="alertMsg"></p>
      		</div>
      		<div class="modal-footer" align="right">
        		<input type="button" class="btn btn-primary" id="alertok" value="Ok" onclick="alertHide();">
        		<!-- <input type="button" class="btn btn-danger" value="Cancel"> -->
      		</div>
      	</div>
	</div>
</div>  

<script>
function alertShow(msg)
{
	$("#myAlertModal").modal("show");
	document.getElementById("alertMsg").innerHTML=msg;
}
function alertHide()
{
	$("#myAlertModal").modal("hide");
}
</script>   