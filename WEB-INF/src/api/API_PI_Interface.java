package api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import automation.Auto_DB_Connection;

public class API_PI_Interface 
{	
	public static void main(String[] args) 
	{
		try
		{
			NumberFormat nf = new DecimalFormat("###########0.00");
			
			TrustManager[] trustAllCerts = new TrustManager[] 
	        {
	            new X509TrustManager() 
	            {
	                public java.security.cert.X509Certificate[] getAcceptedIssuers() 
	                {
	                    return null;
	                }
	                public void checkClientTrusted(X509Certificate[] certs, String authType) 
	                {
	
	                }
	                public void checkServerTrusted(X509Certificate[] certs, String authType) 
	                {
	
	                }
	            }
	        };
	        
			SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	
	        HostnameVerifier allHostsValid = new HostnameVerifier() 
	        {
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }
	        };
	        
	        String urlString="https://piwebapiap.shell.com/piwebapi/streamsets/end?WebId=F1DPjFOakldGMUu0YR_7zapK7wKlAJAARFNBUFBJQ09MTFxIQVo6RFBSLkxORy5DTE9TSU5HU1RPQ0s";
	        
	        URL url = new URL(urlString);
	        HttpsURLConnection httpConn= (HttpsURLConnection) url.openConnection();
	        httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("User-Agent", "Mozilla/5.0");
            httpConn.setRequestProperty("Authorization", "Basic QVNJQS1QQUNcRk1TLUJhdGNoSGF6aXJhLUFQLVM6ajRCOE5tWmMybzdZUWc5");
            
            int responseCode = httpConn.getResponseCode();
            
            //System.out.println("----->"+responseCode);
            if (responseCode == httpConn.HTTP_OK)
            {
            	BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                String response;
                StringBuffer inputLine = new StringBuffer();
                
                while((response = in.readLine()) != null) 
                {
                	inputLine.append(response); 
                }
                in.close();
                
                response = inputLine.toString();
                //System.out.println("Response ==> "+response);
                response=response.replace("null", "");
                
                //System.out.println("Response1 ==> "+response);
                
                JSONParser parse = new JSONParser(); 
    			JSONObject jobj = (JSONObject)parse.parse(response);
    			//System.out.println("\nJSON Parse :"+ jobj);
    			JSONArray jsonarr_1 = (JSONArray) jobj.get("Items");
    			JSONObject nominatedItemList = new JSONObject();
    			Iterator ii = jsonarr_1.iterator();
    			String jsonvalue="";
    			while(ii.hasNext())
    			{
    				JSONObject jsonObj = (JSONObject) ii.next();
    				jsonvalue = nominatedItemList.toJSONString((Map) jsonObj.get("Value")).toString();
    				//System.out.println(jsonvalue);
    				
    			}
    			String Value="0";
    			String Timestamp="";
    			if(!jsonvalue.equals(""))
    			{
    				String split[] = jsonvalue.split(",");
    				//System.out.println(split.length);
    				int i=0;
    				for(i=0; i < split.length; i++)
    				{
    					String v = split[i];
    					String v1=v.replaceAll("\"", "");
    					//System.out.println(v1);
    					String split2[] = v1.split(":");
    					if(split2[0].trim().equals("Value"))
    					{
    						Value=split2[1].trim();
    					}
    					if(split2[0].trim().equals("Timestamp"))
    					{
    						Timestamp=split2[1].trim();
    					}
    				}
    			}
    			//System.out.println("Value : "+Value);
    			//System.out.println("Timestamp : "+Timestamp);
    			
    			String date="";
    			if(!Timestamp.equals(""))
    			{
    				String str_date[] = Timestamp.split("T");
    				date = str_date[0].substring(8,str_date[0].length())+"/"+str_date[0].substring(5,7)+"/"+str_date[0].substring(0,4);
    			}
    			//System.out.println("Date : "+date);
    			
    			double inv_value = Double.parseDouble(Value);
    			double con_factor_mmscm = 600;
    			double con_factor_mmbtu = 38900;
    			
    			double mmscm= (inv_value * con_factor_mmscm) / 1000000;
    			double mmbtu= mmscm * con_factor_mmbtu;
    			
    			String str_mmscm = nf.format(mmscm);
    			String str_mmbtu = nf.format(mmbtu);
    			//System.out.println("MMSCM : "+nf.format(mmscm));
    			//System.out.println("MMBTU : "+nf.format(mmbtu));
    			
    			if(!Value.equals("") && !date.equals(""))
    			{
    				Connection conn=new Auto_DB_Connection().db_conn();
    				if(conn != null)
    	            {
    					conn.setAutoCommit(false);
    					String comp_cd="2";
    					String tank_cd="1";
    					
    					int selCnt=0;
    					String query1="SELECT COUNT(*) "
								+ "FROM FMS_TANK_INVENTORY_DTL "
								+ "WHERE COMPANY_CD=? AND INV_LEVEL_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND TANK_CD=? ";
						PreparedStatement stmt1 = conn.prepareStatement(query1);
						stmt1.setString(++selCnt, comp_cd);
						stmt1.setString(++selCnt, date);
						stmt1.setString(++selCnt, tank_cd);
						ResultSet rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							int count=rset1.getInt(1);
							
							if(count>0)
							{
								int st_count=0;
								
								String query ="UPDATE FMS_TANK_INVENTORY_DTL SET TANK_VOLUME=?,TANK_HEIGHT=?,TANK_MMSCM=?,"
										+ "TANK_CONV_FACTOR_1=?,TANK_CONV_FACTOR_2=?,TANK_MMBTU=?,MODIFY_DT=SYSDATE,MODIFY_BY=? "
										+ "WHERE COMPANY_CD=? AND INV_LEVEL_DT=TO_DATE(?,'DD/MM/YYYY') "
										+ "AND TANK_CD=? ";
								PreparedStatement stmt0 = conn.prepareStatement(query);
								stmt0.setString(++st_count, Value);
								stmt0.setString(++st_count, "");
								stmt0.setString(++st_count, str_mmscm);
								stmt0.setString(++st_count, ""+con_factor_mmscm);
								stmt0.setString(++st_count, ""+con_factor_mmbtu);
								stmt0.setString(++st_count, str_mmbtu);
								stmt0.setString(++st_count, "0");
								stmt0.setString(++st_count, comp_cd);
								stmt0.setString(++st_count, date);
								stmt0.setString(++st_count, tank_cd);
								stmt0.executeUpdate();
								
								stmt0.close();
							}
							else 
							{
								int insCnt=0;
		    					String query = "INSERT INTO FMS_TANK_INVENTORY_DTL (COMPANY_CD, "
										+ "INV_LEVEL_DT, "
										+ "TANK_CD, "
										+ "TANK_VOLUME, "
										+ "TANK_HEIGHT, "
										+ "TANK_MMSCM, "
										+ "TANK_CONV_FACTOR_1, "
										+ "TANK_CONV_FACTOR_2, "
										+ "TANK_MMBTU, "
										+ "ENT_BY, "
										+ "ENT_DT)"
										+ "VALUES(?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,?,SYSDATE)";
								PreparedStatement stmt2 = conn.prepareStatement(query);
								stmt2.setString(++insCnt, "2");
								stmt2.setString(++insCnt, date);
								stmt2.setString(++insCnt, "1");
								stmt2.setString(++insCnt, Value);
								stmt2.setString(++insCnt, "");
								stmt2.setString(++insCnt, str_mmscm);
								stmt2.setString(++insCnt, ""+con_factor_mmscm);
								stmt2.setString(++insCnt, ""+con_factor_mmbtu);
								stmt2.setString(++insCnt, str_mmbtu);
								stmt2.setString(++insCnt, "0");
								stmt2.executeUpdate();
								
								stmt2.close();
							}
							
							conn.commit();
						}
						rset1.close();
						stmt1.close();
    	            }
    				conn.close();
    				conn=null;
    			}
            }
            httpConn.disconnect();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
