# 엑셀(Excel)

### 1. 엑셀 Power Pivot (100만건 이상 대용량 data 로딩)
- 200만건 이상을 한번에 로딩하여 pivot 테이블 생성한 예  
![image](https://user-images.githubusercontent.com/45334819/71748224-374d6a00-2eb5-11ea-956d-9123b83766dd.png)

- F1컬럼의 길이가 9이면, 왼쪽에 0을 하나 추가 및 앞3자리를 잘라붙이고, 길이가 9가 아니면 앞4자리를 추출
=if(len(hscode[F1])=9, REPT("0", 1) & left(hscode[F1], 3), left(hscode[F1], 4))


