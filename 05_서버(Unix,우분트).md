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



