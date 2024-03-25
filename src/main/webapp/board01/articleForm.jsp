<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

 <script src="https://code.jquery.com/jquery-3.6.4.js"></script>

<script>
function readURL(object){
	/* console.log(object);
	console.log(object.files); */
	if (object.files && object.files[0]) {
		console.log(object.files[0]);
		var reader = new FileReader();
		reader.onload = function(event){
			
			 $("#preview").attr("src", event.target.result);
			
			console.log("이벤트 대상", event.target)
			console.log("이벤트 대상 결과", event.target.result)
		}
		
		reader.readAsDataURL(object.files[0]);
		
	}
}


function backToList(object){
	console.log(object);
	console.log(object.form);
	
	object.form.action="http://localhost:8080/pro17/board/listArticles.do"
	object.form.submit();
}



 
</script>

</head>
<body>
<h1 style="text-align:center">새글 쓰기</h1>
 <form name="articleForm" method="post" action="http://localhost:8080/pro17/board/addArticle.do" enctype="multipart/form-data">
     <table border=0 align="center">
     <tr>
	   <td align="right">글제목: </td>
	   <td colspan="2"><input type="text" size="67"  maxlength="500" name="title" /></td>
	 </tr>
	 <tr>
		<td align="right" valign="top"><br>글내용: </td>
		<td colspan=2><textarea name="content" rows="10" cols="65" maxlength="4000"></textarea> </td>
     </tr>
     <tr>
        <td align="right">이미지파일 첨부:  </td>
	     <td> <input type="file" name="imageFileName"  onchange="readURL(this)" /></td>
         <td><img  id="preview" src="#"   width=200 height=200/></td>
	 </tr>
	 <tr>
	    <td align="right"> </td>
	    <td colspan="2">
	       <input type="submit" value="글쓰기" />
	       <input type=button value="목록보기"onClick="backToList(this)" />
	    </td>
     </tr>
    </table>
 </form>
</body>
</html>