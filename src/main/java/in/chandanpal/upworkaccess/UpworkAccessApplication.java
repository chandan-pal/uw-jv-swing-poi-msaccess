package in.chandanpal.upworkaccess;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class UpworkAccessApplication {
    
    

	public static void main(String[] args) {
	    ConfigurableApplicationContext context = new SpringApplicationBuilder(UpworkAccessApplication.class)
                .headless(false).run(args);
		
		Application app = context.getBean(Application.class);
		app.run();
	}

}
