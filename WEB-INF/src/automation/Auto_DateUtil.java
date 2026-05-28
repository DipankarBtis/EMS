package automation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Auto_DateUtil 
{
	String db_src_file_name="Auto_DateUtil.java";
	Connection conn;
	PreparedStatement stmtement,stmtement1,stmtement2,stmtement3;
	ResultSet resultset,resultset1,resultset2,resultset3;
	String query = "";
	String query1="";
	
	public String getSysdate(Connection conn)
	{
		String function_nm="getSysdate()";
		String date="";
		try
		{
			if(conn!=null)
			{
				query = "SELECT TO_CHAR(SYSDATE,'DD/MM/YYYY') FROM DUAL";
				stmtement = conn.prepareStatement(query);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					date = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return date;
	}
	
	public String getNextDate(Connection conn)
	{
		String function_nm="getNextDate()";
		String date="";
		try
		{
			if(conn!=null)
			{
				query = "SELECT TO_CHAR(SYSDATE+1,'DD/MM/YYYY') FROM DUAL";
				stmtement = conn.prepareStatement(query);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					date = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return date;
	}
	
	public String getPreviousDate(Connection conn)
	{
		String function_nm="getPreviousDate()";
		String date="";
		try
		{
			if(conn!=null)
			{
				query = "SELECT TO_CHAR(SYSDATE-1,'DD/MM/YYYY') FROM DUAL";
				stmtement = conn.prepareStatement(query);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					date = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return date;
	}

	public String getSysdateWithTime24hr(Connection conn)
	{
		String function_nm="getSysdateWithTime24hr()";
		String date="";
		try
		{
			if(conn!=null)
			{
				query = "SELECT TO_CHAR(SYSDATE,'DD/MM/YYYY HH24:MI') FROM DUAL";
				stmtement = conn.prepareStatement(query);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					date = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return date;
	}
	
	public String getSysdatePlusOneYear(Connection conn)
	{
		String function_nm="getSysdatePlusOneYear()";
		String date="";
		try
		{
			if(conn!=null)
			{
				query = "SELECT TO_CHAR(SYSDATE+365,'DD/MM/YYYY') FROM DUAL";
				stmtement = conn.prepareStatement(query);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					date = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return date;
	}
	
	public String getFirstDateOfMonth(Connection conn)
	{
		String function_nm="getFirstDateOfMonth()";
		String date="";
		try
		{
			if(conn!=null)
			{
				query ="SELECT TO_CHAR(TRUNC(TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) - (TO_NUMBER(TO_CHAR(TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'),'DD')) - 1),'DD/MM/YYYY') FROM DUAL";
				stmtement = conn.prepareStatement(query);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					date = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return date;
	}
	
	public String getFirstDateOfMonth(Connection conn, String month, String year)
	{
		String function_nm="getFirstDateOfMonth()";
		String date="";
		try
		{
			if(conn!=null)
			{
				query ="SELECT TO_CHAR(TRUNC(TO_DATE(?,'MM/YYYY')) - (TO_NUMBER(TO_CHAR(TO_DATE(?,'MM/YYYY'),'DD')) - 1),'DD/MM/YYYY') FROM DUAL";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, month+"/"+year);
				stmtement.setString(2, month+"/"+year);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					date = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return date;
	}
	
	public String getFirstDateOfMonth(Connection conn, String date)
	{
		String function_nm="getFirstDateOfMonth()";
		String dt="";
		try
		{
			if(conn!=null)
			{
				query ="SELECT TO_CHAR(TRUNC(TO_DATE(?,'DD/MM/YYYY')) - (TO_NUMBER(TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'DD')) - 1),'DD/MM/YYYY') FROM DUAL";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, date);
				stmtement.setString(2, date);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					dt = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return dt;
	}

	public String getLastDateOfMonth(Connection conn, String month, String year)
	{
		String function_nm="getLastDateOfMonth()";
		String date="";
		try
		{
			if(conn != null)
			{
				query ="SELECT TO_CHAR(LAST_DAY(TO_DATE(?,'MM/YYYY')),'DD/MM/YYYY') FROM DUAL";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, month+"/"+year);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					date = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return date;
	}
	
	public String getLastDateOfMonth(Connection conn, String date)
	{
		String function_nm="getLastDateOfMonth()";
		String dt="";
		try
		{
			if(conn != null)
			{
				query ="SELECT TO_CHAR(LAST_DAY(TO_DATE(?,'DD/MM/YYYY')),'DD/MM/YYYY') FROM DUAL";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, date);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					dt = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return dt;
	}

	public String getMonthName(Connection conn, String date)
	{
		String function_nm="getMonthName()";
		String mth_nm="";
		try
		{
			if(conn != null)
			{
				query ="SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'Month') FROM DUAL";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, date);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					mth_nm = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return mth_nm;
	}
	
	public String getShortMonthName(Connection conn, String date)
	{
		String function_nm="getShortMonthName()";
		String mth_nm="";
		try
		{
			if(conn != null)
			{
				query ="SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'Mon') FROM DUAL";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, date);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					mth_nm = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return mth_nm;
	}
	
	public String getMonthNameMON(Connection conn, String date)
	{
		String function_nm="getMonthNameMON()";
		String mth_nm="";
		try
		{
			if(conn!=null)
			{
				query ="SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'MON') FROM DUAL";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, date);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					mth_nm = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return mth_nm;
	}
	
	public int getDays(Connection conn, String date, String date1)
	{
		String function_nm="getDays()";
		int days=0;
		try
		{
			if(conn != null)
			{
				query ="SELECT (TO_DATE(?,'DD/MM/YYYY') - TO_DATE(?,'DD/MM/YYYY'))+1 FROM DUAL";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, date);
				stmtement.setString(2, date1);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					days = resultset.getInt(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return days;
	}
	
	public int getCurrentYear(Connection conn)
	{
		String function_nm="getCurrentYear()";
		int year=0;
		try
		{
			if(conn != null)
			{
				query ="SELECT TO_CHAR(SYSDATE,'YYYY') FROM DUAL";
				stmtement = conn.prepareStatement(query);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					year = resultset.getInt(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return year;
	}
	
	public int getCurrentMonth(Connection conn)
	{
		String function_nm="getCurrentMonth()";
		int month=0;
		try
		{
			if(conn != null)
			{
				query ="SELECT TO_CHAR(SYSDATE,'MM') FROM DUAL";
				stmtement = conn.prepareStatement(query);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					month = resultset.getInt(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return month;
	}
	
	public String getDate(Connection conn, String dt, String days)
	{
		String function_nm="getDate()";
		String date="";
		try
		{
			if(conn != null)
			{
				query = "SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+?,'DD/MM/YYYY') FROM DUAL";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, dt);
				stmtement.setString(2, days);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					date = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return date;
	}
	
	public String getDateFormatDD_MOM_YY(Connection conn, String dt)
	{
		String function_nm="getDateFormatDD_MOM_YY()";
		String date="";
		try
		{
			if(conn != null)
			{
				query="SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'DD-MON-YY') FROM DUAL";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, dt);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					date = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return date;
	}

	public String getFinancialYear(Connection conn, String dt)
	{
		String function_nm="getFinancialYear()";
		String financial="";
		try
		{
			if(conn != null)
			{
				query="SELECT EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY'), -3)) "
						+ " || '-' || "
						+ "EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY'), 9)) "
						+ "FROM DUAL";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, dt);
				stmtement.setString(2, dt);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					financial = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return financial;
	}
	
	public String getPreviousFinancialYear(Connection conn, String dt)
	{
		String function_nm="getPreviousFinancialYear()";
		String financial="";
		try
		{
			dt=getDate(conn,dt, "-365");
			
			if(conn != null)
			{
				query="SELECT EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY'), -3)) "
						+ " || '-' || "
						+ "EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY'), 9)) "
						+ "FROM DUAL";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, dt);
				stmtement.setString(2, dt);
				if(resultset.next())
				{
					financial = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return financial;
	}
	
	public int isDateWithinPeriod(Connection conn, String from_dt, String to_dt, String date)
	{
		String function_nm="isDateWithinPeriod()";
		int days=0;
		try
		{
			if(conn != null)
			{
				query ="SELECT COUNT(*) "
						+ "FROM DUAL "
						+ "WHERE TO_DATE(?,'DD/MM/YYYY') <= TO_DATE(?,'DD/MM/YYYY') "
						+ "AND TO_DATE(?,'DD/MM/YYYY') >= TO_DATE(?,'DD/MM/YYYY') ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, from_dt);
				stmtement.setString(2, date);
				stmtement.setString(3, to_dt);
				stmtement.setString(4, date);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					days = resultset.getInt(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return days;
	}
	
	public String getFullWeekDaysName(Connection conn, String dt)
	{
		String function_nm="getFullWeekDaysName()";
		String nm="";
		try
		{
			if(conn != null)
			{
				query = "SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'Day') FROM DUAL";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, dt);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					nm = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return nm;
	}
	
	public String getDaysName(Connection conn)
	{
		String function_nm="getDaysName()";
		String nm="";
		try
		{
			if(conn != null)
			{
				query = "SELECT TO_CHAR(SYSDATE,?) FROM DUAL";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, "Day");
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					nm = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return nm;
	}
}
