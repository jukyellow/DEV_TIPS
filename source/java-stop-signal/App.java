package com.ktnet.icnexp;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public App() {
        LOGGER.info("Start server ");
        
        ArrayList<String> list = new ArrayList<String>();
        list.add("spring/application-context.xml");

        new ClassPathXmlApplicationContext(list.toArray(new String[list.size()]));
        SigTERMHandler.register();
    }

	public static void main(String[] args) {
        new App();
    }
}
