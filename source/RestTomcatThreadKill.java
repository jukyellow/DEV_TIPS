package tomcat.thread.stop;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@org.springframework.web.bind.annotation.RestController
public class RestTomcatThreadKill { 
	//Thread죽으면 아래 로그가 남으면서 servlet doService에서 죽음(org.springframework.web.servlet.DispatcherServlet.doService)
	//java.lang.ThreadDeath: null
	//at java.lang.Thread.stop(Unknown Source) [na:1.8.0_201]
	//at tomcat.thread.stop.RestController.agentmain(RestController.java:85) ~[main/:na]
	@GetMapping("/th")
	public String printThreadId() {
		try {
			String name = ManagementFactory.getRuntimeMXBean().getName(); 
			String pidNumber = name.substring(0, name.indexOf("@"));	 // PID 번호 : 윈도/유닉스 공통
			System.out.println("p-name:"+name+",pidNumber:"+pidNumber);
			
			long threadId = Thread.currentThread().getId();
			String threadName = Thread.currentThread().getName();
			System.out.println("threadId:"+threadId+",threadName:"+threadName);
		
	        ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
	        long[] threadIds = mxBean.getAllThreadIds();  
	        ThreadInfo[] threadInfos =  mxBean.getThreadInfo(threadIds);  
	        for (ThreadInfo threadInfo : threadInfos) {
	        	if(threadInfo.getThreadId()==threadId) {
	        		System.out.print("threadInfo:"+threadInfo.toString());
	        		
	        	}
	        }
	        
	        for(int i=0; i<10; ++i) {
	        	try {
					Thread.sleep(3000);
					System.out.println("sleep("+(i+1)+"): "+threadName);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        }
	        
			return String.valueOf(threadId);
		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
		
		return "fail";
	}
	
	@GetMapping("/kill/{tid}")
	public String killThread(@PathVariable("tid") String tid) {
		return agentmain(tid);
	}
	
	//http://wiki.sauru.so/doc:kill-thread-of
	//https://github.com/jglick/jkillthread
	public String agentmain(String tid) {
		try {
	        ThreadGroup g = Thread.currentThread().getThreadGroup();
	        while (true) {
	            ThreadGroup g2 = g.getParent();
	            if (g2 == null) {
	                break;
	            } else {
	                g = g2;
	            }
	        }
	        Thread[] threads;
	        int size = 256;
	        while (true) {
	            threads = new Thread[size];
	            if (g.enumerate(threads) < size) {
	                break;
	            } else {
	                size *= 2;
	            }
	        }
	        boolean found = false;
	        for (Thread thread : threads) {
	            if (thread == null) {
	                continue;
	            }
	            String name = thread.getName();
	            if (name.equals(tid)) {
	                System.err.printf("Killing \"%s\"%n", name);
	                found = true;
	                thread.stop();
	            }
	        }
	        if (!found) {
	            System.err.printf("Did not find \"%s\"%n", tid);
	        }
		}catch(Exception e) {
			System.err.printf(e.getMessage());
		}
		
		return "success";
    }

}
