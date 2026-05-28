<%@page import="com.etrm.fms.util.EncryptTest"%>
<jsp:useBean class="com.etrm.fms.util.DataBean_Menu" id="menu_access" scope="page"></jsp:useBean>
<%
String formCd="";
String mod_cd="";
String u=request.getParameter("u")==null?"":request.getParameter("u");

String msg=request.getParameter("msg")==null?"":request.getParameter("msg");
String msg_type=request.getParameter("msg_type")==null?"":request.getParameter("msg_type");

String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

String owner_abbr="";
if(session.getAttribute("comp_abbr")==null||session.getAttribute("comp_abbr")==""||session.getAttribute("comp_abbr").toString().equals("null"))
{
	owner_abbr="";
}  
else
{
	owner_abbr=""+session.getAttribute("comp_abbr");
}

String emp_cd="";
if(session.getAttribute("emp_cd")==null||session.getAttribute("emp_cd")==""||session.getAttribute("emp_cd").toString().equals("null"))
{
	emp_cd="";
}  
else
{
	emp_cd=""+session.getAttribute("emp_cd");
}

if(!u.equals(""))
{
	StringBuffer decryt=new EncryptTest().URLdecrypt(u);
	String dec=""+decryt;
	if(!dec.equals("") && dec.contains("-"))
	{
		String temp[]=dec.split("-");
		if(temp.length==2)
		{
			formCd=temp[0];
			mod_cd=temp[1];
		}
	}
}

menu_access.setCallFlag("MENU_ACCESS");
menu_access.setEmpCd(emp_cd);
menu_access.setComp_cd(owner_cd);
menu_access.setModule_cd(mod_cd);
menu_access.setForm_cd(formCd);
menu_access.init();


String formNm = menu_access.getForm_nm();
String mod_nm = menu_access.getModule_nm();
String read_access = menu_access.getRead_access();
String write_access = menu_access.getWrite_access();
String check_access = menu_access.getCheck_access();
String print_access = menu_access.getPrint_access();
String delete_access = menu_access.getDelete_access();
String audit_access = menu_access.getAudit_access();
String authorize_access = menu_access.getAuthorize_access();
String approve_access = menu_access.getApprove_access();
String execute_access = menu_access.getExecute_access();

%>