package com.ktnet.icnexp.signal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public class SigTERMHandler /*implements SignalHandler*/ implements Runnable{
	private static final Logger LOGGER = LoggerFactory.getLogger(SigTERMHandler.class);
	private volatile static SigTERMHandler instance;	
	private static boolean stop = false;
	
    public static void register(){
        if(instance == null){
            synchronized (SigTERMHandler.class) {
                if(instance == null)
                    instance = new SigTERMHandler();
            }
            
            Runtime.getRuntime().addShutdownHook(new Thread(instance));
        }
    }

	private SigTERMHandler() {
		 System.out.println(this.getClass() + "(SignalHandler) 등록됨");
	}
	
	@Override
	public void run() /*handle(Signal signal)*/ {
		try {
			stop();

			for (int i=0; i<5; ++i) {
				Thread.sleep(1000);
				System.out.println("처리중인 작업이 존재하여 리소스 해제 처리를 대기합니다."); //[" + dateForamt.format(Calendar.getInstance().getTime()) + "]");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("프로세스 종료가 요청되었습니다. 사용중 리소스를 모두 종료합니다.");
	}
	
	public static boolean getStop() {
		return stop;
	}
	
	public void stop() {
		stop = true;
		System.out.println(this.getClass() + "(SignalHandler) call stop!");
		
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		
		if (applicationContext.containsBean("rspFilePoller")) {
		    FilePoller poller = (FilePoller) applicationContext.getBean("rspFilePoller");
		    poller.setActive("N");
		}
		
		if (applicationContext.containsBean("cmdDbPoller")) {
			DbPoller poller = (DbPoller) applicationContext.getBean("cmdDbPoller");
		    poller.setActive("N");
		}
	}
}
