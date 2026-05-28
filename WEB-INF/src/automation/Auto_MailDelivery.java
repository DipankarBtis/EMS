package automation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Auto_MailDelivery 
{
	String db_src_file_name="Auto_MailDelivery.java";
	Connection conn;
	PreparedStatement stmtement,stmtement1,stmtement2,stmtement3;
	ResultSet resultset,resultset1,resultset2,resultset3;
	String query = "";
	String query1="";
	
	String smtp_host="mail.barodainformatics.com";
	String smtp_auth="true";
	String smtp_port="25";
	
	String mail_id="fmsng@barodainformatics.com";
	String mail_identifier="Bipl@304";
	
	//String mail_id="SEMTIPL-FMSng-App-Support@shell.com";
	//String mail_identifier="Plsem45ti";
	
	public void getMailAuthDetail(Connection conn)
	{
		String function_nm="getMailAuthDetail()";
		try
		{
			if(conn!=null)
			{
				query = "SELECT KEY_NM,KEY_VALUE "
						+ "FROM FMS_SRV_SETTING "
						+ "WHERE KEY_NM IN (?,?,?,?,?)";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, "SMTP_HOST");
				stmtement.setString(2, "SMTP_PORT");
				stmtement.setString(3, "SMTP_AUTH");
				stmtement.setString(4, "EMAIL_FROM");
				stmtement.setString(5, "EMAIL_PASSWD");
				resultset = stmtement.executeQuery();
				while(resultset.next())
				{
					String key_nm=resultset.getString(1)==null?"":resultset.getString(1);
					String key_value=resultset.getString(2)==null?"":resultset.getString(2);
					
					if(key_nm.toUpperCase().equals("SMTP_HOST"))
					{
						smtp_host=key_value;
					}
					else if(key_nm.toUpperCase().equals("SMTP_PORT"))
					{
						smtp_port=key_value;
					}
					else if(key_nm.toUpperCase().equals("SMTP_AUTH"))
					{
						smtp_auth=key_value;
					}
					else if(key_nm.toUpperCase().equals("EMAIL_FROM"))
					{
						mail_id=key_value;
					}
					else if(key_nm.toUpperCase().equals("EMAIL_PASSWD"))
					{
						mail_identifier=key_value;
					}
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public boolean sendMail(Connection conn, String recipientsTo, String subject, String message, String fileName,String recipientsCC,String recipientsBCC)
			throws MessagingException ,AuthenticationFailedException 
	{
		String function_nm="sendMail()";
		try 
		{
			getMailAuthDetail(conn); //TO GET MAIL AUTH DETAIL
			
			Properties props = new Properties();
			
			props.put("mail.smtp.host",smtp_host);
			props.put("mail.smtp.auth",smtp_auth);
			props.put("mail.smtp.port",smtp_port);
			
			Authenticator auth = new Authenticator() 
			{
				protected PasswordAuthentication getPasswordAuthentication() 
				{
					return new PasswordAuthentication(mail_id, mail_identifier);
				}
			};
			
			Session session = Session.getDefaultInstance(props, auth);
			MimeMessage msg = new MimeMessage(session);
				
			InternetAddress addressFrom = new InternetAddress(mail_id);
			msg.setFrom(addressFrom);
							
			msg.setRecipients(Message.RecipientType.TO, recipientsTo);
			msg.setRecipients(Message.RecipientType.CC, recipientsCC);
			
			msg.setSubject(subject);
				
			Multipart multipart = new MimeMultipart();
			BodyPart mbp_file=new MimeBodyPart();
			mbp_file.setContent(message, "text/html");
			msg.saveChanges();
			multipart.addBodyPart(mbp_file);
			msg.setContent(multipart);
				
			if(!fileName.trim().equalsIgnoreCase(""))
			{
				mbp_file = new MimeBodyPart(); 
				FileDataSource fds = new FileDataSource(fileName);
				mbp_file.setDataHandler(new DataHandler(fds));
				mbp_file.setFileName(fds.getName());
				multipart.addBodyPart(mbp_file);
				msg.setContent(multipart);
			}			
			msg.setSentDate(new Date());
			msg.saveChanges();
			Transport.send(msg,msg.getAllRecipients());
			
			return true;
		}		
		catch (Exception ex) 
		{			
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, ex);
			return false;
		}
	}
	
	public boolean sendMailWithMultipleAttachment(Connection conn, String company_cd, String recipientsTo, String subject, String message, String[] fileName,String recipientsCC,String recipientsBCC)
			throws MessagingException ,AuthenticationFailedException 
	{
		String function_nm="sendMailWithMultipleAttachment()";
		try 
		{
			getMailAuthDetail(conn); //TO GET MAIL AUTH DETAIL
			
			Properties props = new Properties();
			
			props.put("mail.smtp.host",smtp_host);
			props.put("mail.smtp.auth",smtp_auth);
			props.put("mail.smtp.port",smtp_port);
			
			Authenticator auth = new Authenticator() 
			{
				protected PasswordAuthentication getPasswordAuthentication() 
				{
					return new PasswordAuthentication(mail_id, mail_identifier);
				}
			};
			
			Session session = Session.getDefaultInstance(props, auth);
			MimeMessage msg = new MimeMessage(session);
				
			InternetAddress addressFrom = new InternetAddress(mail_id);
			msg.setFrom(addressFrom);
							
			msg.setRecipients(Message.RecipientType.TO, recipientsTo);
			msg.setRecipients(Message.RecipientType.CC, recipientsCC);
			
			msg.setSubject(subject);
				
			Multipart multipart = new MimeMultipart();
			BodyPart mbp_file=new MimeBodyPart();
			mbp_file.setContent(message, "text/html");
			msg.saveChanges();
			multipart.addBodyPart(mbp_file);
				
			if(fileName!=null)
	        {
				for(int i=0;i<fileName.length;i++)
	            {
					mbp_file = new MimeBodyPart(); 
					FileDataSource fds = new FileDataSource(fileName[i]);
					mbp_file.setDataHandler(new DataHandler(fds));
					mbp_file.setFileName(fds.getName());
					multipart.addBodyPart(mbp_file);
					msg.setContent(multipart);
	            }
	        }
			
			msg.setSentDate(new Date());
			msg.saveChanges();
			Transport.send(msg,msg.getAllRecipients());
			
			return true;
		}		
		catch (Exception ex) 
		{			
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, ex);
			return false;
		}
	}
}
