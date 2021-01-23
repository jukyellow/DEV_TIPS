---
theme: gaia
_class: lead
paginate: true
backgroundColor: #fff
marp: true
backgroundImage: url('https://marp.app/assets/hero-background.jpg')
---

# Hello Marp
$~$
### 간단한 Marp 설치/작성법 설명 (2021/01/24)

---

## 1.설치

1. 홈페이지: https://marp.app/#get-started
2. 사용버전: GUI/CLI 두가지 버전이 있는데, 오늘(2021/01/24) 시도해보니 GUI버전은 설치가 먹통이고, CLI는 바로 설치할수 있어
```
npm install -g @marp-team/marp-cli
```
- 자세한 설치법은 여길 참고해: https://www.npmjs.com/package/@marp-team/marp-cli
- Node는 기본 설치되어 있어야해(구글링하면 금방 따라할수 있어~)

---

## 2. 작성법

### 1. md 파일 만들기

1. 메모장/notepad++/atom등을 이용해 test.md 파일 생성
2. marp test.md 를 cmd창에서 실행
3. 기본 html로 변환되서 show됨.
```
marp test.md
```
4. '--watch'란 명령어를 붙여서, 변경 저장후 바로바로 확인 가능해

---

### 2. 기본 배경 탬플릿 적용방법

- 아래와 같이 백그라운드 탬플릿을 적용할 수 있어!(md파일 젤 처음 부분에 입력)  
```
---
theme: gaia
_class: lead
paginate: true
backgroundColor: #fff
marp: true
backgroundImage: url('https://marp.app/assets/hero-background.jpg')
---
```

---

### 3. Marp 간단한 문법 설명

1. 기본 Markdown 문법을 사용가능해
2. 줄바꿈은 역슬러쉬('\') or 공백 or HTML 출력인경우 <br>을 사용하면되
```
\
$~$
<br>
```

3. 페이지구분
```
---
```

---

4. 이미지 입력
```
![](image.jpg)
```
<br>

4-1. local 경로 이미지 (로컬경로/이미지파일명)
- pdf등으로 변환하면 이미지 로딩이 잘안되니, 기본 HTML모드로 만드는게 좋을거 같아.
![](./images/juk.jpg)
```
![](./images/juk.jpg)
```

---

4-2. 웹 경로 이미지 (웹경로)
![](https://miro.medium.com/fit/c/32/32/1*-y0tVQWEmtqOn4RTqjmawQ.jpeg)
```
![](https://miro.medium.com/fit/c/32/32/1*-y0tVQWEmtqOn4RTqjmawQ.jpeg)
```

---

### 5. 파일변환 (PDF, PPT, HTML 등)
- '--pdf', '--pptx' 등을 실행명렁어에 붙이고, 안붙이면 기본 HTML변환이야
```
mapr --pdf test.md
```

