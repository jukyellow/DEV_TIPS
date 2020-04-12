
### 1. 이클립스 환경에서 서버(유닉스,리눅스) 구동 프로세스 실행하기
1. HOME 환경변수 설정
2. java 실행시 arguments(페라미터) 설정
3. java VM argument설정
4. 프로젝트 class path(source 디렉토리)에 conf 디렉토리 설정 및 로켈에 queue/log 디렉토리 생성
5. 로컬 운영체제 설정
6. classpath문제 해결(resource bunddle)

![image](https://user-images.githubusercontent.com/45334819/61463196-e1d8db80-a9ae-11e9-9a7e-5bc4d7ddf134.png)  
![image](https://user-images.githubusercontent.com/45334819/61463174-d8e80a00-a9ae-11e9-8901-49110509f090.png)  
![image](https://user-images.githubusercontent.com/45334819/61463210-e7362600-a9ae-11e9-8403-75d27b40cb29.png)  
![image](https://user-images.githubusercontent.com/45334819/61463218-ec937080-a9ae-11e9-8c9b-2c398b348fde.png)  
![image](https://user-images.githubusercontent.com/45334819/61463267-01700400-a9af-11e9-8e17-fda4cefe699c.png)  
![image](https://user-images.githubusercontent.com/45334819/61463307-164c9780-a9af-11e9-8eb6-4021c9dd76a1.png)  

<br>
<hr />
<br>

### 2. VM Options -javaagent

- 참고1: https://jrebel.com/rebellabs/how-to-inspect-classes-in-your-jvm/  
- 참고2: https://docs.oracle.com/javase/7/docs/api/java/lang/instrument/package-summary.html  

-sample: MSA 구조에서 micro-service를 모니터링하기 위해, Elastic Search의 APM Agent를 구동하는 예제  
![image](https://user-images.githubusercontent.com/45334819/61463821-29139c00-a9b0-11e9-826e-1d32c6b9f1ba.png)  

1. apm agent 다운로드  
- 브라우저에서 아래 url에 접속해서 오른쪽 다운로드에서 jar 다운로드  
: https://search.maven.org/search?q=a:elastic-apm-agent  
- 서버에서 다운받을 경우 wget으로 다운로드 가능  
: wget "https://search.maven.org/remotecontent?filepath=co/elastic/apm/elastic-apm-agent/1.7.0/elastic-apm-agent-1.7.0.jar"  
* 다운로드 후 파일명 rename : elastic-apm-agent-1.7.0.jar  

2. Application과 동일한 위치에 복사후 아래 도커파일을 이용해서 이미지 생성  
``` dockerfile
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY air-cargo-trace-0.1.0.war app.war
ADD elastic-apm-agent-1.7.0.jar apm-agent.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=dev", "-javaagent:/apm-agent.jar","-Delastic.apm.service_name=air-cargo-trace","-Delastic.apm.server_url=http://***.102.77.***:8911", "-jar","/app.war"]
```

* javaagent 구동시, 전달parameter는 반드시 실행시 VM옵션으로 전달해야 정상동작 가능함  
(proerties 속성정보로 전달시 APM구동은 되지만, 로그정보가 APM서버로 전달안됨)  

3. host 모드로 실행  
* 로컬에서 이클립스로 실행시(첨부파일 참고)  

<br>

### 3. Java 프로세스 안전하게 종료시키기
- 서버 프로그램의 경우 할당한 자원이나 상태 정보등을 서버 종료시에 올바르게 처리해주어야 하는데, 강제 종료시 이런 것들을 올바르게 처리할 수 없는 문제가 발생한다.  
- 자바는 이런 문제를 방지하기 위해서 1.3 버전부터 '셧다운 후크(Shutdown Hook)'라는 기능을 도입하였다. 셧다운 후크는 JVM이 셧다운될 때에 셧다운 이벤트를 가로채서 어떤 코드를 수행한 후 최종적으로 셧다운 되도록 한다. 참고로, JVM은 다음과 같은 상황인 경우 셧다운 이벤트를 발생시킨다.  
> 정상적으로 종료될 때. 예) 마지막 논데몬 쓰레드가 종료되었거나 System.exit()가 호출될 때.  
> JVM이 강제적으로 종료될 때. 예) 시스템 셧다운, 사용자 로그아웃, Ctrl+C 누름  
```
Runtime.getRuntime().addShutdownHook(new Thread(instance));

@Override
public void run() {
  //stop 처리...
}
```
- 샘플(spring framework) 소스: https://github.com/jukyellow/dev-tips/tree/master/source/java-stop-signal  
