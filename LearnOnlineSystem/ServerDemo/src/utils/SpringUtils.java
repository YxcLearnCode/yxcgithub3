package utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SpringUtils {	
	
	private static ApplicationContext ctx;  //»ñÈ¡spring µÄ ApplicationContext
	
	public static Object getBean(String beanName) {
		if(null == ctx)
    		ctx = new FileSystemXmlApplicationContext("WebContent/WEB-INF/springMVC-servlet.xml");
		Object obj = null;
		if (beanName != null && !beanName.equals("")) {
		   obj = ctx.getBean(beanName);
		}
		return obj;		 
	}
}
