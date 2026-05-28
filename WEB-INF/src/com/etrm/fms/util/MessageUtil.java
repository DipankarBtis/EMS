package com.etrm.fms.util;

public class MessageUtil {
	
	public String errorMessage(String value)
	{
		String msg="";
		
		if(value != null && !value.equals(""))
		{
			msg="<div class='container'><div class='alert alert-danger'><i class='fa fa-exclamation-triangle fa-lg'></i>&nbsp;"+value+"</div></div>";
		}
		return msg;
	}
	
	public String successMessage(String value)
	{
		String msg="";
		
		if(value != null && !value.equals(""))
		{
			msg="<div class='container'><div class='alert alert-success'><i class='fa fa-check-circle fa-lg'></i>&nbsp;"+value+"</div></div>";
		}
		return msg;
	}
	
	public String infoMessage(String value)
	{
		String msg="";
		
		if(value != null && !value.equals(""))
		{
			msg="<div class='container'><div class='alert alert-info'><i class='fa fa-info-circle fa-lg'></i>&nbsp;"+value+"</div></div>";
		}
		return msg;
	}
	
	public String warningMessage(String value)
	{
		String msg="";
		
		if(value != null && !value.equals(""))
		{
			msg="<div class='container'><div class='alert alert-warning'><i class='fa fa-exclamation-triangle fa-lg'></i>&nbsp;"+value+"</div></div>";
		}
		return msg;
	}
	
	public String primaryMessage(String value)
	{
		String msg="";
		
		if(value != null && !value.equals(""))
		{
			msg="<div class='container'><div class='alert alert-primary'><i class='fa fa-info-circle fa-lg'></i>&nbsp;"+value+"</div></div>";
		}
		return msg;
	}
	
	public String secondaryMessage(String value)
	{
		String msg="";
		
		if(value != null && !value.equals(""))
		{
			msg="<div class='container'><div class='alert alert-secondary'>"+value+"</div></div>";
		}
		return msg;
	}
	
	public String darkMessage(String value)
	{
		String msg="";
		
		if(value != null && !value.equals(""))
		{
			msg="<div class='container'><div class='alert alert-dark'>"+value+"</div></div>";
		}
		return msg;
	}
}
