
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
<br>

### 3. Jar파일 class 컴파일 버전확인
1. jar 파일 압축 풀기 : jar -xvf 파일명.jar)
2. 클래스 파일 버전확인: javap -verbose 클래스파일명(.class제외) | grep "version"
> javap -verbose CargoENVGenerator | grep "version"
<br>

### 4. Spring Log4j Logger 및 log4jdbc (정리중...)
#### 4.1 Log4j
- 생성 방법1: 커스텀 패키지 name 사용, 하위 패키지 전체에 로깅 적용
```
Logger logger = Logger.getLogger(com.company);
```
- 생성 방법2: 문자열 name 사용, 선언한 해당 Logger 인스턴스로 출력
```
Logger logger = Logger.getLogger("CustomLogger");
```
- 생성 방법3: thirdParty 패키지 사용, 해당 패키지에서 발생하는 로그 남김
```
<logger name="org.springframework.core">
    <level value="info" />
</logger>
```
#### 4.2 Log4jdbc
> 사전에 정의된 다양한 옵션 사용가능(조회 paramter 및 조회결과를 테이블 형태로도 출력하는 옵션 존재)  
- jdbc.sqlonly : SQL문만을 로그로 남기며, PreparedStatement일 경우 관련된 argument 값으로 대체된 SQL문이 보여진다. 
- jdbc.sqltiming : SQL문과 해당 SQL을 실행시키는데 수행된 시간 정보(milliseconds)를 포함한다. 
- jdbc.audit : ResultSet을 제외한 모든 JDBC 호출 정보를 로그로 남긴다. 많은 양의 로그가 생성되므로 특별히 JDBC 문제를 추적해야 할 필요가 있는 경우를 제외하고는 사용을 권장하지 않는다. 
- jdbc.resultset : ResultSet을 포함한 모든 JDBC 호출 정보를 로그로 남기므로 매우 방대한 양의 로그가 생성된다. 
- jdbc.resultsettable : SQL 결과 조회된 데이터의 table을 로그로 남긴다.
```
<logger name="jdbc.sqlonly" level="OFF"/>
<logger name="jdbc.sqltiming" level="DEBUG"/>
<logger name="jdbc.audit" level="OFF"/>
<logger name="jdbc.resultset" level="OFF"/>
<logger name="jdbc.resultsettable" level="DEBUG"/>
<logger name="jdbc.connection" level="OFF"/>
```

