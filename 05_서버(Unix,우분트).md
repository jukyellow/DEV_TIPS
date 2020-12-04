# 서버(Unix,우분투) 관리 tip / 명령어  

### 1. /etc/hosts 파일수정 및 네트워크 재기동
- 문제: Java 프로세스에서 UnKnownHostException 발생시, DNS에서 host이름으로 IP를 못찾는것!
- 방법: 로컬 캐시(/etc/hosts에 정보 기입) 및 networking restart
```
$ cat /etc/hosts
127.0.0.1    localhost
127.0.1.1    storycompiler
```
```
$ sudo /etc/init.d/networking restart
[ ok ] Restarting networking (via systemctl): networking.service.
```
- 참고: https://storycompiler.tistory.com/118[아프니까 개발자다]  
<br>

### 2. SWAP 메모리 추가(1G->3G할때)
- 참고1: https://htst.tistory.com/32
- 크게 두가지 방식 partition(디스크 할당) or SWAPFILE(swap용 big file추가) 처리가 있음
- partition이 존재할때 swapfile을 추가하여 간단하게 스왑메모리를 추가할수 있음.
- 참고2: - https://steps4great.tistory.com/7
--swapfile 만들기
```
 sudo fallocate -l 2G /swapfile  → swapfile 생성
 sudo chmod 600 /swapfile → root 사용자만 사용할 수 있도록 권한 변경
 sudo mkswap /swapfile  → 스왑메모리로 변경
 sudo swapon /swapfile  → 스왑메모리 활성화
```
--재부팅시 swap메모리 남아있게 설정
```
sudo vi /etc/fstab
입력내용 → /swapfile swap swap default 0 0
```
--스왑메모리 제거
```
sudo vi /etc/fstab → 자동마운트 내용 제거 및 주석처리
sudo swapoff -v /swapfile
sudo swapoff on /swapfile
sudo rm -r /swapfile
```
<br>

### 3. Unix서버 변천사
```
                               유닉스(커널)
                                                 SunOS(커널)
                                                     솔라리스(배포판, 상용)
             리눅스(커널)
     리눅스(무료)        레드헷 리눅스?(상용)
우분투(무료)          페도라      센토스(무료)
```
![image](https://user-images.githubusercontent.com/45334819/72932695-9828dd80-3da3-11ea-99b6-4e9f8a50b310.png)  
<br>

### 4. 유닉스 명령어 df, du 
- 디스크 잔여 용량(disk free)
```
'df .'    : 현재 디렉토리가 포함된 파티션의 남은 공간을 보여준다.
'df -k'  : Kilobyte 단위로 현재 마운트된 파티션들의 남은 공간을 보여준다.
'df -h'  : KB, MB, GB
```
![image](https://user-images.githubusercontent.com/45334819/73023593-3635aa00-3e6f-11ea-9642-28f6d4131a6f.png)

- 디스크 현재 사용량(disk Usage)
```
'du -k'
'du -h': 현재 디렉토리의 사용용량(사용자가 보기쉬운값으로)
'du -sh *': 현재 디렉토리 사용용량(하위폴더 포함)
* 특정용량 이상조회:  $ du -s * | awk '$1 > 100000'
```
![image](https://user-images.githubusercontent.com/45334819/73023620-48174d00-3e6f-11ea-856d-a4c413b09249.png)
- 참고: https://ko.wikipedia.org/wiki/Du_(%EC%9C%A0%EB%8B%89%EC%8A%A4)  
<br>

### 5. 터미널 ssh 접속
```
ssh -p 15022 user@123.123.123.123
```
<br>

### 6. Shell 특수문자와 2>&1의 의미
6-1) $! : 최근 백그라운드 작업의 프로세스 번호
- 활용: 프로세스 pid를 저장해두고 stop shell에서 kill pid로 죽일때 사용가능
```
echo $! > $HOME/bin/pid/stop_pid.sh
```
6-2) 2>&1 : 2(standard err)를 &1(standard output과 같은 파일로) >(redirect한다) 
- 설명: 표준에러를 표준출력 파일과 같은 stream(파일)로 write한다.
```
 java -Dtype=TEST com.test.java test.cfg > test.out 2>&1 &  
 #java프로세스를 실행하는데 test.out파일로 표준출력을 write함. 그리고 표준에러도 표준출력과 같은 파일로 쓰고, java프로세스는 백그라운드(&)로 실행 
 #표준에러를 파일에 쓰도록 했기때문에, 화면(screen)에 Exception내용이 찍히지 않음  
```
<br>

### 7. 파일 생성시간 기준으로 찾기/삭제하기
- 참고: https://joont.tistory.com/129
- 방법: find의 -newer 옵션(지정한 파일의 날짜보다 이후에 수정된 파일을 찾아주는 옵션) 활용  
```
1) touch -t 20160501 begin  (우분투는 yyyyMMddhh24mi 까지 입력해야 됨?)
2-1) find . -newer begin -print
> begin 파일보다 이후에 수정된 파일을 검색합니다. 즉, 2016년 5월 1일 이후의 파일을 검색하게 되는것이죠.
2-2) find . ! -newer begin -print
> find의 부정연산자를 사용하면 2016년 5월 1일 이전의 파일도 검색 가능합니다.
2-3) touch -t 20160530 end, find . -newer begin -a ! -newer end -print
>2016년 5월 1일부터 2016년 5월 30일 사이에 수정된 파일을 검색하는 방법입니다.
3) find . -newer begin -a ! -newer end | wc -l
> 개수 구하기
4) find . -newer begin -a ! -newer end -exec rm -f {} \;
> 삭제하기 (*주의: 반드시 . 현재디렉토리 기준으로 찾기바람, /를 쓰면 최상위 경로가 됨)  
```
<br>  

### 8. 웹서버 설정(Sun ONE)과 WAS Timeout설정
#### 8.1 설정파일
- /opt/sunone61/https-xxx/config/object.conf : URI 패턴을 등록하고 해당하는 요청일때 필터링 기능 동작  
``` xml
<Object name="weblogic_do" ppath="*.do">
 Service fn="wl_proxy" WebLogicCluster="xxx.xxx.xxx.xxx:9820,xxx.xxx.xxx.xxx:9820" DynamicServerList="OFF" Idempotent="ON" WLIOTimeoutSecs="3600" KeepAliveEnabled="false" CookieName="NLPS_ADMIN_JSESSIONID"
</Object>
```
- /opt/sunone61/https-xxx/config/server.xml : 웹서버 포트등 설정  
#### 8.2 설정파일(object.conf) 옵션 설명  
- WLIOTimeoutSecs (HungServerRecoverSecs) : WLS로 request를 보내고 response를 받기 위해 대기하는 시간, default 300초  
- KeepAliveEnabled : Plug-in과 WLS의 연결을 지속할 것인지 여부를 결정  
> client request를 처리한 후 WLS와의 연결을 닫아버릴 것인지 연결된 상태로 두었다가 다음 요청이 들어왔을 때 재사용할 것인지 설정  
> Default => true(Netscape and Microsoft IIS) & ON (Apache)  
- Idempotent (ON/OFF) : 
> WebLogic서버로 부터 request전송시 에러가 발생하거나, 서버로부터 결과를 기다리는 중에 위에 정의된 WLIOTimeoutSecs 시간 초과되어서 에러 발생시 요청을 다시 보낼 것인가를 지정  
> 서버와 연결은 되었는데 그 이후에 에러가 발생 하였을 경우 해당 옵션이 ON이면 다시 연결을 시도하고, 요청을 보내게 되므로 중복 요청의 가능성이 있다. OFF권장  
#### 8.3 WAS(web application server) Timeout 설정  
- 세션 Timeout 설정: 
1) 웹콘솔: 배치->nlps->구성 >  "세션시간초과" 항목
2) 서버 xml파일
- web.xml : /WEB-INF 하위, web.xml의 경우에 단위는 분이다.  
``` xml
<session-config>
   <session-timeout>60</session-timeout>
</session-config>
```
- weblogic.xml : 단위는 초이다.  
``` xml 
<session-descriptor>
   <session-param>
      <param-name>TimeoutSecs</param-name>
      <param-value>3600</param-value>
   </session-param>
</session-descriptor>
```


