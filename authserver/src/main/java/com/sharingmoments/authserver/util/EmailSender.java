package com.sharingmoments.authserver.util;

import java.io.IOException;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.sharingmoments.authserver.config.FreemarkerTemplateConfig;

import freemarker.template.TemplateException;

@Component
@PropertySource("classpath:email.properties")
public class EmailSender {
	
	@Autowired
	@Qualifier("MailSender")
    private JavaMailSender sender;
	
	@Autowired
    private Environment env;
	
	@Autowired
	private FreemarkerTemplateConfig freemarkerConfiguration;

	static Logger logger = Logger.getLogger(EmailSender.class.getName());
	
	
	public Boolean sendAsHTML(String to, String subject, ModelMap model, String template) {
		try {
            MimeMessage mail = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setSubject(subject);
            
            try {
            	String htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.freemarkerConfig().createConfiguration().getTemplate(template, "UTF-8"), model);

                helper.setText(htmlText, true);
                helper.setFrom(env.getProperty("mail.from"));
                
                sender.send(mail);
                
                return true;
            } catch (IOException | TemplateException e) {
            	logger.error(e);
    			return false;
    		}
        } catch (Exception e) {
        	logger.error(e);
            return false;
        }
	}
	
	public Boolean sendAsPlain(String to, String subject, String text) {
		try {
			final SimpleMailMessage mail = new SimpleMailMessage();
			mail.setTo(to);
	        mail.setSubject(subject);
	        mail.setText(text);
	        mail.setFrom(env.getProperty("mail.from"));
            
            sender.send(mail);
            
            return true;
        } catch (Exception e) {
            return false;
        }
	}
}
