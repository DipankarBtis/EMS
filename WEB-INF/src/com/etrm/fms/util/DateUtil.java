package com.etrm.fms.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class DateUtil 
{
	String db_src_file_name="DateUtil.java";
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	DateTimeFormatter formatterDD_MON_YY = DateTimeFormatter.ofPattern("dd-MMM-yy");
	DateTimeFormatter formatterYYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	DateTimeFormatter formatterTimeSS = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	DateTimeFormatter formatterMonthName = DateTimeFormatter.ofPattern("MMMM");
	DateTimeFormatter formatterMonthABBR = DateTimeFormatter.ofPattern("MMM");
	
	DateTimeFormatter formatterYYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
	
	public String getSysdate()
	{
		String function_nm="getSysdate()";
		String date="";
		try
		{
			LocalDate dateObj = LocalDate.now();
			date = dateObj.format(formatter);
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return date;
	}
	
	public String getNextDate()
	{
		String function_nm="getNextDate()";
		String date="";
		try
		{
			LocalDate dateObj = LocalDate.now().plusDays(1);
			date = dateObj.format(formatter);
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return date;
	}
	
	public String getPreviousDate()
	{
		String function_nm="getPreviousDate()";
		String date="";
		try
		{
			LocalDate dateObj = LocalDate.now().minusDays(1);
			date = dateObj.format(formatter);
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return date;
	}

	public String getSysdateWithTime24hr()
	{
		String function_nm="getSysdateWithTime24hr()";
		String date="";
		try
		{
			LocalDateTime dateObj = LocalDateTime.now();
			date = dateObj.format(formatterTime);
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return date;
	}
	
	public String getSysdateWithTime24hrWithSS()
	{
		String function_nm="getSysdateWithTime24hrWithSS()";
		String date="";
		try
		{
			LocalDateTime dateObj = LocalDateTime.now();
			date = dateObj.format(formatterTimeSS);
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return date;
	}
	
	public String getSysdatePlusOneYear()
	{
		String function_nm="getSysdatePlusOneYear()";
		String date="";
		try
		{
			LocalDate dateObj = LocalDate.now().plusDays(365);
			date = dateObj.format(formatter);
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return date;
	}
	
	public String getFirstDateOfMonth()
	{
		String function_nm="getFirstDateOfMonth()";
		String date="";
		try
		{
			LocalDate dateObj = LocalDate.now().withDayOfMonth(1);
			date = dateObj.format(formatter);
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return date;
	}
	
	public String getFirstDateOfMonth(String month, String year)
	{
		String function_nm="getFirstDateOfMonth()";
		String date="";
		try
		{
			int Year=0;
			int Month=0;
			if(!year.equals("") && !month.equals(""))
			{
				Year=Integer.parseInt(year);
				Month=Integer.parseInt(month);
				LocalDate dateObj = LocalDate.of(Year, Month, 1);
				date = dateObj.format(formatter);
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return date;
	}
	
	public String getFirstDateOfMonth(String date)
	{
		String function_nm="getFirstDateOfMonth()";
		String dt="";
		try
		{
			if(!date.equals(""))
			{
				LocalDate dateObj = LocalDate.parse(date,formatter).withDayOfMonth(1);
				dt = dateObj.format(formatter);
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return dt;
	}

	public String getLastDateOfMonth(String month, String year)
	{
		String function_nm="getLastDateOfMonth()";
		String date="";
		try
		{
			int Year=0;
			int Month=0;
			if(!year.equals("") && !month.equals(""))
			{
				Year=Integer.parseInt(year);
				Month=Integer.parseInt(month);
				YearMonth dateObj = YearMonth.of(Year, Month);
				LocalDate nextDate = dateObj.atEndOfMonth();
				date = nextDate.format(formatter);
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return date;
	}
	
	public String getFirstDateOfPreviousMonth(String date)
	{
		String function_nm="getFirstDateOfPreviousMonth()";
		String dt="";
		try
		{
			if(!date.equals(""))
			{
				LocalDate dateObj = LocalDate.parse(date,formatter);
				LocalDate previousMonthDate = dateObj.minusMonths(1).withDayOfMonth(1);
				dt = previousMonthDate.format(formatter);
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return dt;
	}
	
	public String getLastDateOfMonth(String date)
	{
		String function_nm="getLastDateOfMonth()";
		String dt="";
		try
		{
			if(!date.equals(""))
			{
				YearMonth dateObj = YearMonth.parse(date, formatter);
				LocalDate nextDate = dateObj.atEndOfMonth();
				dt = nextDate.format(formatter);
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return dt;
	}
	
	public String getFirstDateOfYear(String year) 
	{
	    String function_nm = "getFirstDateOfYear()";
	    String dt = "";
	    try 
	    {
	        if (year != null && !year.trim().isEmpty()) 
	        {
	            int yr = Integer.parseInt(year);
	            LocalDate dateObj = LocalDate.of(yr, 1, 1);
	            dt = dateObj.format(formatter);
	        }
	    } 
	    catch (Exception e) 
	    {
	        throw e;
	    }
	    return dt;
	}

	public String getMonthName(String date)
	{
		String function_nm="getMonthName()";
		String mth_nm="";
		try
		{
			if(!date.equals(""))
			{
				LocalDate dateObj = LocalDate.parse(date, formatter);
				mth_nm = dateObj.format(formatterMonthName);
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return mth_nm;
	}
	
	public String getShortMonthName(String date)
	{
		String function_nm="getShortMonthName()";
		String mth_nm="";
		try
		{
			if(!date.equals(""))
			{
				LocalDate dateObj = LocalDate.parse(date, formatter);
				mth_nm = dateObj.format(formatterMonthABBR);
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return mth_nm;
	}
	
	public String getMonthNameMON(String date)
	{
		String function_nm="getMonthNameMON()";
		String mth_nm="";
		try
		{
			mth_nm = getShortMonthName(date).toUpperCase();
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return mth_nm;
	}
	
	public int getDays(String date, String date1)
	{
		String function_nm="getDays()";
		int days=0;
		try
		{
			if(!date.equals("") && !date1.equals(""))
			{
				LocalDate dateObj = LocalDate.parse(date,formatter);
				LocalDate dateObj1 = LocalDate.parse(date1, formatter);
				days = 1 + (int) ChronoUnit.DAYS.between(dateObj1, dateObj);
			}

			/*if(init())
			{
				query ="SELECT (TO_DATE(?,'DD/MM/YYYY') - TO_DATE(?,'DD/MM/YYYY'))+1 FROM DUAL";
				System.out.println(query);
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
				conn.close();
			}*/
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return days;
	}
	
	public int getCurrentYear()
	{
		String function_nm="getCurrentYear()";
		int year=0;
		try
		{
			LocalDate dateObj = LocalDate.now();
			year=dateObj.getYear();
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return year;
	}
	
	public int getCurrentYear(String date)
	{
		String function_nm="getCurrentYear()";
		int year=0;
		try
		{
			LocalDate dateObj = LocalDate.parse(date,formatter);
			year=dateObj.getYear();
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return year;
	}
	
	public int getCurrentMonth()
	{
		String function_nm="getCurrentMonth()";
		int month=0;
		try
		{
			LocalDate dateObj = LocalDate.now();
			month=dateObj.getMonthValue();
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return month;
	}
	
	public String getDate(String dt, String days)
	{
		String function_nm="getDate()";
		String date="";
		try
		{
			int noOfDys=0;
			if(!dt.equals("") && !days.equals(""))
			{
				noOfDys=Integer.parseInt(days);
				
				LocalDate dateObj = LocalDate.parse(dt, formatter).plusDays(noOfDys);
				date = dateObj.format(formatter);
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return date;
	}
	
	public String getDateFormatDD_MOM_YY(String dt)
	{
		String function_nm="getDateFormatDD_MOM_YY()";
		String date="";
		try
		{
			if(!dt.equals(""))
			{
				LocalDate dateObj = LocalDate.parse(dt,formatter);
				date = dateObj.format(formatterDD_MON_YY).toUpperCase();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return date;
	}
	
	public String getDateFormatYYYY_MM_DD(String dt)
	{
		String function_nm="getDateFormatYYYY_MM_DD()";
		String date="";
		try
		{
			if(!dt.equals(""))
			{
				LocalDate dateObj = LocalDate.parse(dt,formatter);
				date = dateObj.format(formatterYYYY_MM_DD).toUpperCase();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return date;
	}

	public String getFinancialYear(String dt)
	{
		String function_nm="getFinancialYear()";
		String financial="";
		try
		{
			int Year=0;
			int Month=0;
			//int Day=0;
			if(!dt.equals("") && dt.contains("/"))
			{
				String temp[] = dt.split("/");
				Year=Integer.parseInt(temp[2]);
				Month=Integer.parseInt(temp[1]);
				//Day=Integer.parseInt(temp[0]);
				
				if(Month < 4) 
				{
		            Year -= 1;
		        }
				financial=Year+"-"+(Year+1);
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return financial;
	}
	
	public String getPreviousFinancialYear(String dt)
	{
		String function_nm="getPreviousFinancialYear()";
		String financial="";
		try
		{
			dt=getDate(dt, "-365");
			
			int Year=0;
			int Month=0;
			//int Day=0;
			if(!dt.equals("") && dt.contains("/"))
			{
				String temp[] = dt.split("/");
				Year=Integer.parseInt(temp[2]);
				Month=Integer.parseInt(temp[1]);
				//Day=Integer.parseInt(temp[0]);
				
				if(Month < 4) 
				{
		            Year -= 1;
		        }
				financial=Year+"-"+(Year+1);
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return financial;
	}
	
	public int isDateWithinPeriod(String from_dt, String to_dt, String date)
	{
		String function_nm="isDateWithinPeriod()";
		int days=0;
		try
		{
			if(!from_dt.equals("") && !to_dt.equals("") && !date.equals(""))
			{
				LocalDate from_dateObj = LocalDate.parse(from_dt,formatter);
				LocalDate to_dateObj = LocalDate.parse(to_dt,formatter);
				LocalDate dateObj = LocalDate.parse(date,formatter);
				
				boolean isActive = (dateObj.isEqual(from_dateObj) || dateObj.isAfter(from_dateObj)) &&
                        (dateObj.isEqual(to_dateObj) || dateObj.isBefore(to_dateObj));
				if(isActive) {
					days=1;
				}else {
					days=0;
				}
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return days;
	}
	
	public String getFullWeekDaysName(String dt)
	{
		String function_nm="getFullWeekDaysName()";
		String nm="";
		try
		{
			LocalDate dateObj = LocalDate.parse(dt,formatter);
			nm = dateObj.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return nm;
	}
	
	public String getShortWeekDaysName(String dt)
	{
		String function_nm="getShortWeekDaysName()";
		String nm="";
		try
		{
			LocalDate dateObj = LocalDate.parse(dt,formatter);
			nm = dateObj.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return nm;
	}
	
	public String getDaysName()
	{
		String function_nm="getDaysName()";
		String nm="";
		try
		{
			LocalDate dateObj = LocalDate.now();
			nm = dateObj.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return nm;
	}
	
	public String getShortDaysName()
	{
		String function_nm="getShortDaysName()";
		String nm="";
		try
		{
			LocalDate dateObj = LocalDate.now();
			nm = dateObj.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return nm;
	}
	
	public int getNumberOfSaturdayForMonth(String dt)
	{
		String function_nm="getNumberOfSaturdayForMonth()";
		int saturdayNumber=0;
		try
		{
			LocalDate dateObj = LocalDate.parse(dt,formatter);
			if (dateObj.getDayOfWeek() == DayOfWeek.SATURDAY) 
			{
	            // First day of the month
	            LocalDate firstDayOfMonth = dateObj.withDayOfMonth(1);

	            // Calculate the day of the first Saturday
	            int dayOfWeekValue = firstDayOfMonth.getDayOfWeek().getValue(); // 1 (Mon) to 7 (Sun)
	            int daysToAdd = (DayOfWeek.SATURDAY.getValue() - dayOfWeekValue + 7) % 7;

	            // Date of the first Saturday
	            LocalDate firstSaturday = firstDayOfMonth.plusDays(daysToAdd);

	            // Calculate the Saturday number directly
	            saturdayNumber = ((dateObj.getDayOfMonth() - firstSaturday.getDayOfMonth()) / 7) + 1;
	        }
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return saturdayNumber;
	}
	
	public String getLastDateofFortnight(String dt)
	{
		String function_nm="getLastDateofFortnight()";
		String date="";
		try
		{
			LocalDate referenceDate = LocalDate.parse(dt, formatter);
			
			int dayOfMonth = referenceDate.getDayOfMonth();

	        if (dayOfMonth <= 15) 
	        {
	            // 1st fortnight of current month → return previous month's 2nd fortnight
	            YearMonth prevMonth = YearMonth.from(referenceDate).minusMonths(1);
	            //LocalDate start = prevMonth.atDay(16);
	            LocalDate end = prevMonth.atEndOfMonth();
	            date=end.format(formatter);
	        } 
	        else 
	        {
	            // 2nd fortnight of current month → return current month's 1st fortnight
	            YearMonth currentMonth = YearMonth.from(referenceDate);
	            //LocalDate start = currentMonth.atDay(1);
	            LocalDate end = currentMonth.atDay(15);
	            date=end.format(formatter);
	        }
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return date;
	}
	
	public String getDateFormatDD_MM_YYYY_from_YYYYMMDD(String dt)
	{
		String function_nm="getDateFormatDD_MM_YYYY_from_YYYYMMDD()";
		String date="";
		try
		{
			if(!dt.equals(""))
			{
				LocalDate dateObj = LocalDate.parse(dt, formatterYYYYMMDD);
				date = dateObj.format(formatter);
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return date;
	}
	
	public String getBillingPeriodEndDate(String billing_freq,String period_end_dt,String billing_days,String inv_dt)
	{
		String function_nm="getBillingPeriodEndDate()";
		String freq_end_dt="";
		try
		{
			String day="";
			String month="";
			String year="";
			if(!period_end_dt.equals(""))
			{
				String split[]=period_end_dt.split("/");
				day=split[0];
				month=split[1];
				year=split[2];
			}
			
			if(billing_freq.equals("F"))
			{
				
				if(Integer.parseInt(day) > 15)
				{
					freq_end_dt=getLastDateOfMonth(month, year);
				}
				else
				{
					freq_end_dt="15/"+month+"/"+year;
				}
			}
			else if(billing_freq.equals("W"))
			{
				if(Integer.parseInt(day) > 28)
				{
					freq_end_dt=getLastDateOfMonth(month, year);
				}
				else if(Integer.parseInt(day) > 21)
				{
					freq_end_dt="28/"+month+"/"+year;
				}
				else if(Integer.parseInt(day) > 14)
				{
					freq_end_dt="21/"+month+"/"+year;
				}
				else if(Integer.parseInt(day) > 7)
				{
					freq_end_dt="14/"+month+"/"+year;
				}
				else
				{
					freq_end_dt="07/"+month+"/"+year;
				}
			}
			else if(billing_freq.equals("M"))
			{
				freq_end_dt=getLastDateOfMonth(month, year);
			}
			else if(billing_freq.equals("O"))
			{
				freq_end_dt=getDate(inv_dt, billing_days);
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return freq_end_dt;
	}
	
	public String getBillingCyclePeriod(String billing_cycle,String month,String year)
	{
		String function_nm="getBillingCyclePeriod()";
		String period_end_dt = "";
		try
		{
			if(billing_cycle.equals("1") || billing_cycle.equals("2"))
			{
				if(billing_cycle.equals("1"))
				{
					period_end_dt="15/"+month+"/"+year;
				}
				else if(billing_cycle.equals("2"))
				{
					period_end_dt=getLastDateOfMonth(month, year);
				}
			}
			else if(billing_cycle.equals("3") || billing_cycle.equals("4") || billing_cycle.equals("5") || billing_cycle.equals("6") || billing_cycle.equals("9"))
			{
				if(billing_cycle.equals("3"))
				{
					period_end_dt="07/"+month+"/"+year;
				}
				else if(billing_cycle.equals("4")) 
				{
					period_end_dt="14/"+month+"/"+year;
				}
				else if(billing_cycle.equals("5")) 
				{
					period_end_dt="21/"+month+"/"+year;
				} 
				else if(billing_cycle.equals("6")) 
				{
					period_end_dt="28/"+month+"/"+year;
				} 
				else if(billing_cycle.equals("9"))
				{
					if(month.equals("02"))
					{
						int days=getDays(getLastDateOfMonth(month, year), getFirstDateOfMonth(month, year));
						if(days==29)
						{
							period_end_dt=getLastDateOfMonth(month, year);
						}
					}
					else
					{
						period_end_dt=getLastDateOfMonth(month, year);
					}
				}
			}
			else if(billing_cycle.equals("7"))
			{
				period_end_dt=getLastDateOfMonth(month, year);
			}
			else if(billing_cycle.equals("8"))
			{
				period_end_dt=getLastDateOfMonth(month, year);
			}
			else if(billing_cycle.equals("11")) //FOR TCQ Completion
			{
				period_end_dt=getLastDateOfMonth(month, year);
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		return period_end_dt;
	}
}
