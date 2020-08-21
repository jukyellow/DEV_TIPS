
//번호 추출기
//MAX_NUM: 추천 인원
//MAX_ITER: 한번에 추출하는 숫자
function publishRandNum(MAX_NUM, MAX_ITER){
var randNumList = "";
for(var i=0; i<MAX_ITER; ++i){
jRandNum = Math.random()
rand_digit = Math.floor(jRandNum * MAX_NUM + 1)
//alert("jRandNum:"+ jRandNum + ' ,rand_digit:' + rand_digit)

randNumList += "Seq:" + (i+1) + " ,Num:" +rand_digit+ "<br>";
}
return randNumList;
}

document.write( '<p>' + "행운권 추첨기" + '</p>' );
document.write( '<p>' + publishRandNum(150, 5) + '</p>' );

/* 설정 publishRandNum(150, 5) 인 경우
행운권 추첨기
Seq:1 ,Num:18
Seq:2 ,Num:29
Seq:3 ,Num:20
Seq:4 ,Num:141
Seq:5 ,Num:31
*/
