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
