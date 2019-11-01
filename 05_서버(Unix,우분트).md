# 서버(Unix,우분투) 관리 tip / 명령어  

### 1. /etc/hosts 파일수정 및 네트워크 재기동
- 문제: Java 프로세스에서 UnKnownHostException 발생시, DNS에서 host이름으로 IP를 못찾는것!
- 방법: 로컬 캐시(/etc/hosts에 정보 기입) 및 networking restart
---
$ cat /etc/hosts
127.0.0.1    localhost
127.0.1.1    storycompiler
---
---
$ sudo /etc/init.d/networking restart
[ ok ] Restarting networking (via systemctl): networking.service.
---
- 참고: https://storycompiler.tistory.com/118[아프니까 개발자다]  


