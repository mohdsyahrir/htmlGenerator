package com.irays.htmlGenerator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	public HomeController() {
		// TODO Auto-generated constructor stub
		logger.info("System is running");
		final File folder = new File("C:\\mcsgenerator\\mcsjsp");
		  for (final File fileEntry : folder.listFiles()) {
		        if (fileEntry.isDirectory()) {
		        	logger.info("is directory");
		        } else {
		        	//logger.info(fileEntry.getName());
		        	generateHtml(fileEntry.getName());
		        	logger.info("Html has been generate");
		        }
		    }
	}
	
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		
		final File folder = new File("C:\\mcsgenerator\\mcsjsp");
		  for (final File fileEntry : folder.listFiles()) {
		        if (fileEntry.isDirectory()) {
		        	logger.info("is directory");
		        } else {
		        	//logger.info(fileEntry.getName());
		        	generateHtml(fileEntry.getName());
		        	logger.info("Html has been generate");
		        }
		    }
		
		return "home";
	}

	private void generateHtml(String name) {

		List<String> titleAndcontentInBody = getTitle(name);
		String button = "";
		if(titleAndcontentInBody.size() == 3){
			 button = titleAndcontentInBody.get(2);	
		}
		
		
		String html = "<html xmlns:th="+"\"http://www.thymeleaf.org\""+"\n" + 
						"\t xmlns:layout="+"\"http://www.ultraq.net.nz/web/thymeleaf/layout\""+"\n" +
						"\t xmlns:sec="+"\"http://www.thymeleaf.org/thymeleaf-extras-springsecurity3\""+" layout:decorator="+"\"decorators/theme\""+">\n" +
							"\t\t <head>\n" +
								"\t\t\t <title><span th:text="+"\"#{cocMohon.prompt.title}\""+" title="+"\""+titleAndcontentInBody.get(0)+"\""+"></span></title>\n" +
								"\t\t\t <meta charset="+"\"utf-8\""+" />\n" +
								"\t\t\t <meta http-equiv="+"\"X-UA-Compatible\""+" content="+"\"IE=edge\""+" />\n" +
								"\t\t\t <meta name="+"\"viewport\""+" content="+"\"width=device-width, initial-scale=1\""+" />\n" +
								"\t\t\t <meta http-equiv="+"\"Content-Type\""+" content="+"\"text/html; charset=utf-8\""+" />\n" +
								"\t\t\t <link rel="+"\"stylesheet\""+" th:href="+"\"@{#{resource.bootstrapvalidator.css}}\""+" />\n" +
							"\t\t </head>\n" +
							"\t\t <body>\n " + 
								"\t\t\t <div layout:fragment="+"\"content\""+">\n" +
									"\t\t\t\t <div class="+"\"content-wrapper\""+">\n" +
										""+titleAndcontentInBody.get(1)+""
										  +""+button+""+
									"\t\t\t\t </div>\n"+	
								"\t\t\t </div>\n"+ 
								"\t\t\t\t\t <div layout:fragment="+"\"javascript_endpage\""+">\n" +
								"\t\t\t\t\t <script th:src="+"\"@{/resources/lib/sprflat/plugins/forms/datetimepicker/bootstrap-datepicker.min.js}\""+"></script>\n" +
								"\t\t\t\t\t <script type="+"\"text/javascript"+" th:src="+"@{#{resource.bootstrapvalidator.js}}\""+"></script>\n" +
								"\t\t\t\t\t <script th:src="+"\"@{/javascript/common/select-option.js}\""+"></script>\n" +
								"\t\t\t\t\t <script th:src="+"\"@{/javascript/grader/update-profile/form.js}\""+"></script>\n" +
								"\t\t\t\t\t </div>\n" +	
							"\t\t</body>\n" +
					"</html>";
		
		String htmlname = name.replace("jsp", "html");
		File f = new File("C:\\mcsgenerator\\mcshtml\\"+htmlname+"");
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(html);
			bw.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private List<String> getTitle(String name) {
		String htmlname = name.replace("jsp", "html");
		String title = null;
		StringBuilder contentInBody = new StringBuilder();
		StringBuilder everything = new StringBuilder();
		List<String> titleAndcontentInBody = new ArrayList<String>();
		String button ="";
		Pattern pattern = Pattern.compile("<TITLE>(.*?)</TITLE>");
		Pattern pattern1 = Pattern.compile("<TABLE CLASS="+"\"h1\""+">(.*?)</TABLE>");
		Pattern pattern3 = Pattern.compile("<TABLE CLASS="+"\"h2\""+">(.*?)</TABLE>");
		Pattern pattern2 = Pattern.compile("<bean:message key="+"\"(.*?)\"/>");

		try (BufferedReader br = new BufferedReader(new FileReader("C:\\mcsgenerator\\mcsjsp\\"+name+"")))////dfgdfg
		{
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				everything.append(sCurrentLine);
			}
			
			Matcher matcher = pattern.matcher(everything.toString());
		    while (matcher.find()) {
		        //logger.info(matcher.group(1));
		        title = matcher.group(1);
		    }
		    
		    Matcher matcher1 = pattern1.matcher(everything.toString());
		    while (matcher1.find()) {
		        //logger.info(matcher1.group(1));
		        Matcher matcher2 = pattern2.matcher(matcher1.group(1));
		        if(matcher2.find()){
		        	h1(matcher2.group(1),contentInBody,htmlname);
		        }
		        break;
		    }
		    
			Matcher matcher3 = pattern3.matcher(everything.toString());
		    while (matcher3.find()) {
		    	h2(contentInBody,matcher3.group(1),pattern2,everything.toString());
		    }
		    
		    button = button(everything.toString());
		    

		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		titleAndcontentInBody.add(title);
		titleAndcontentInBody.add(contentInBody.toString());
		if(button != null)
		titleAndcontentInBody.add(button);
		
		return titleAndcontentInBody;
	}

	private String button(String jsp) {
		StringBuilder sbButton = new StringBuilder();
		Pattern pattern6 = Pattern.compile("<html:submit(.*?)</html:submit>");
		Pattern pattern7 = Pattern.compile("<html:reset(.*?)</html:reset>");
		Pattern pattern8 = Pattern.compile("<html:cancel(.*?)</html:cancel>");
		
        Matcher matcher9 = pattern6.matcher(jsp);
		if(matcher9.find()){
			String bttn = "\t\t\t\t\t\t\t\t\t\t\t <button class="+"\"btn btn-primary\""+" id="+"\"submit-btn\""+" type="+"\"button\""+"><bean:message key="+"\"button.submit\""+"/></button>\n";
			sbButton.append(bttn);
        }
		
		Matcher matcher10 = pattern7.matcher(jsp);
		if(matcher10.find()){
			String bttn = "\t\t\t\t\t\t\t\t\t\t\t <button class="+"\"btn btn-primary\""+" id="+"\"reset-btn\""+" type="+"\"button\""+"><bean:message key="+"\"button.reset\""+"/></button>\n";
			sbButton.append(bttn);
        }
		
		Matcher matcher11 = pattern8.matcher(jsp);
		if(matcher11.find()){
			String bttn = "\t\t\t\t\t\t\t\t\t\t\t <button class="+"\"btn btn-primary\""+" id="+"\"cancel-btn\""+" type="+"\"button\""+"><bean:message key="+"\"button.cancel\""+"/></button>\n";
			sbButton.append(bttn);
        }
        
        
        String button = "\t\t\t\t\t <div class="+"\"col-lg-12\""+">\n"
        		+ "\t\t\t\t\t\t <div class="+"\"panel panel-primary\""+">\n"
        				+ "\t\t\t\t\t\t\t <div class="+"\"panel-body\""+">\n"
        						+ "\t\t\t\t\t\t\t\t <div class="+"\"form-group\""+">\n"
        								+ "\t\t\t\t\t\t\t\t\t <div class="+"\"form-group\""+">\n"
        										+ "\t\t\t\t\t\t\t\t\t\t <div class="+"\"col-lg-12 cold-md-12 col-sm-12\""+">\n"
        												+ "\t\t\t\t\t\t\t\t\t\t\t <div class="+"\"pull-right\""+">\n"
        														+ ""+sbButton.toString()+""
														+ "\t\t\t\t\t\t\t\t\t\t\t </div>\n"
												+ "\t\t\t\t\t\t\t\t\t\t </div>\n"
										+ "\t\t\t\t\t\t\t\t\t </div>\n"
								+ "\t\t\t\t\t\t\t\t </div>\n"
						+ "\t\t\t\t\t\t\t </div>\n"
				+ "\t\t\t\t\t\t </div>\n"
				+ "\t\t\t\t\t </div>\n";
        
        if(sbButton.length() == 0){
        	return null;
        }
		
		return button;
	}

	private void h2(StringBuilder contentInBody, String group,Pattern pattern, String everything) {
		String text = "";
		String panelBody = "";
		String input = "";
		String inputDate = "";
		StringBuilder inputForm = new StringBuilder();
		Matcher matcher4 = pattern.matcher(group);
        if(matcher4.find()){
        	text = matcher4.group(1);
        }
        
        Pattern pattern4 = Pattern.compile("<TABLE CLASS="+"\"h2\""+">"+group+"</TABLE>(.*?)</TABLE>");
        logger.info("<TABLE CLASS="+"\"h2\""+">"+group+"</TABLE><TABLE class="+"\"input\""+">(.*?)</TABLE>");
        Matcher matcher5 = pattern4.matcher(everything);
        if(matcher5.find()){
        	panelBody = matcher5.group(1);
        }
        
        Pattern pattern5 = Pattern.compile("<TR>(.*?)</TR>");
        Pattern pattern9 = Pattern.compile("<img(.*?)javascript:setDateFieldParam");
        Matcher matcher6 = pattern5.matcher(panelBody);
	    while (matcher6.find()) {
	    	Matcher matcher12 = pattern9.matcher(matcher6.group(1));
	    	if(matcher12.find()){
	    		String method = "inputDate";
	    		inputDate = input(matcher6.group(1),pattern,method);
	    		inputForm.append(inputDate);
	    	}else{
	    		String method = "input";
	    		input = input(matcher6.group(1),pattern,method);
		    	inputForm.append(input);
	    	}
	    }
	    
		String content = "\t\t\t\t\t <div class="+"\"col-lg-12\""+">\n "+ 
						 "\t\t\t\t\t <div class="+"\"panel panel-primary\""+">\n "+ 
						 "\t\t\t\t\t\t <div class="+"\"panel-heading\""+">\n "+ 
						 "\t\t\t\t\t\t\t <h3 class="+"\"panel-title\""+"><span th:text="+"\"#{"+text+"}\""+"></span></h3>\n "+ 
						 "\t\t\t\t\t\t </div>\n"+ 
						 "\t\t\t\t\t\t <div class="+"\"panel-body\""+">\n "+ 
						 "\t\t\t\t\t\t\t <form class="+"\"form-horizontal\""+" method="+"\"post\""+" id="+"\"\""+" name="+"\"\""+" th:object="+"\"\""+" th:action="+"\"\""+">\n "+ 
						 ""+inputForm+""+ 
						 "\t\t\t\t\t\t\t </form>\n "+ 
						 "\t\t\t\t\t\t </div>\n"+ 
						 "\t\t\t\t\t </div>\n" + 
						 "\t\t\t\t\t </div>\n";
		
		String row = "\t\t\t\t\t <div class="+"\"row\""+">\n "+ 
					 ""+content+"\n"+ 
					 "\t\t\t\t\t </div>\n";
		
		contentInBody.append(row);
		
	}

	private String input(String group,Pattern pattern,String method) {
		
		Pattern pattern6 = Pattern.compile("property="+"\"(.*?)\"");
        Matcher matcher7 = pattern.matcher(group);
        Matcher matcher8 = pattern6.matcher(group);
        String text = "";
        String id = "";
        if(matcher7.find()){
        	text = matcher7.group(1);
        }
        if(matcher8.find()){
        	id = matcher8.group(1);
        }
        
		if(method == "inputDate"){
			String content = "\t\t\t\t\t\t\t\t <div class="+"\"form-group\""+">\n "+ 
					 "\t\t\t\t\t\t\t\t\t <label class="+"\"col-lg-2 col-md-2 col-sm-12 control-label\""+">\n "+ 
					 "\t\t\t\t\t\t\t\t\t\t <span th:text="+"\"#{"+text+"}\""+"></span>\n "+ 
					 "\t\t\t\t\t\t\t\t\t </label>\n "+ 
					 "\t\t\t\t\t\t\t\t\t <div class="+"\"col-lg-5 col-md-10\""+">\n "+ 
					 "\t\t\t\t\t\t\t\t\t\t <input type="+"\"text\""+" class="+"\"form-control input-medium datetime-picker2\""+" id="+"\""+id+"\""+" th:field="+"\"*{"+id+"}\""+" />\n "
			 		+ "\t\t\t\t\t\t\t\t\t\t <span class="+"\"input-group-addon\""+"><i class="+"\"fa-calendar\""+"></i></span>\n"+ 
					 "\t\t\t\t\t\t\t\t\t </div>\n "+ 
					 "\t\t\t\t\t\t\t\t </div>\n";
			
			return content;
		}else{
			String content = "\t\t\t\t\t\t\t\t <div class="+"\"form-group\""+">\n "+ 
					 "\t\t\t\t\t\t\t\t\t <label class="+"\"col-lg-2 col-md-2 col-sm-12 control-label\""+">\n "+ 
					 "\t\t\t\t\t\t\t\t\t\t <span th:text="+"\"#{"+text+"}\""+"></span>\n "+ 
					 "\t\t\t\t\t\t\t\t\t </label>\n "+ 
					 "\t\t\t\t\t\t\t\t\t <div class="+"\"col-lg-5 col-md-10\""+">\n "+ 
					 "\t\t\t\t\t\t\t\t\t\t <input type="+"\"text\""+" class="+"\"form-control\""+" id="+"\""+id+"\""+" th:field="+"\"*{"+id+"}\""+" />\n "+ 
					 "\t\t\t\t\t\t\t\t\t </div>\n "+ 
					 "\t\t\t\t\t\t\t\t </div>\n";
			return content;
		}
	}

	private void h1(String text, StringBuilder contentInBody, String nama) {
		String content = "\t\t\t\t\t <div class="+"\"row\""+">\n " + 
						 "\t\t\t\t\t\t <div class="+"\"col-lg-12 heading\""+">\n " + 
						 "\t\t\t\t\t\t\t <h1 class="+"\"page-header\""+"><i class="+"\"im-play2\""+"></i> <span th:text="+"\"#{"+text+"}\""+"></span></h1>\n " + 
						 "\t\t\t\t\t\t\t <ul class="+"\"breadcrumb\""+">\n " + 
						 "\t\t\t\t\t\t\t\t <li><i class="+"\"im-home\""+"></i><a href="+"\""+nama+"\""+">Home</a><i class="+"\"en-arrow-right7\""+"></i></li>\n " + 
						 "\t\t\t\t\t\t\t\t <li><i class="+"\"im-paragraph-justify\""+"></i><a href="+"\"#\""+"><span th:text="+"\"#{"+text+"}\""+"></span></a><i class="+"\"en-arrow-right7\""+"></i></li>\n " + 
						 "\t\t\t\t\t\t\t </ul>\n " + 
						 "\t\t\t\t\t\t </div>\n " + 
						 "\t\t\t\t\t</div>\n";
		contentInBody.append(content);			
	}
}


