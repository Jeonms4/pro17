<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<script src="http://code.jquery.com/jquery-latest.min.js"></script>

<script>
      function backToList(obj){
	    obj.action="${contextPath}/board/listArticles.do";
	    obj.submit();
	  }
      
      
      
      //수정할 내용 활성화
      function fn_enable(obj){
    	  //console.log(obj);
    	  document.getElementById("i_title").disabled=false;
 		 document.getElementById("i_content").disabled=false;
 		 document.getElementById("i_imageFileName").disabled=false;
 		 document.getElementById("tr_btn_modify").style.display="block";
 		 document.getElementById("tr_btn").style.display="none";
      }
      
   
      //이미지 표시
      function readURL(input) {
 	     if (input.files && input.files[0]) {
 	         var reader = new FileReader();
 	         reader.onload = function (e) {
 	             $('#preview').attr('src', e.target.result);
 	         }
 	         reader.readAsDataURL(input.files[0]);
 	     }
 	 }  
      
      
      //수정 반영
      function fn_modify_article(obj){
 		 obj.action="${contextPath}/board/modArticle.do";
 		 obj.submit();
 	 }
      
      //글 삭제
      function fn_remove_article(url,articleNO){
    	  // post방식으로 삭제해야하는데 폼이 없으므로
    	  //폼을 만든 후에 속성에 메서드와  액션 설정
    	  //인풋 태그 만들어서 속성음 숨김속성으로 속성명(name) 속성 값(글번호로)
    	  	console.log(url,articleNO);
    		 var form = document.createElement("form");
    	  
    	  	//console.log(form);
    	  	form.setAttribute("method", "post");
   		    form.setAttribute("action", url);
   		    //console.log(form);   		    
   		 var articleNOInput = document.createElement("input");
   		//console.log(articleNOInput);
   		articleNOInput.setAttribute("type","hidden");
	     articleNOInput.setAttribute("name","articleNO");
	     articleNOInput.setAttribute("value", articleNO);
	     //console.log(articleNOInput);
	     form.appendChild(articleNOInput);
	     document.body.appendChild(form);
	     console.log(form);
	     form.submit();
      }
      
   </script>

</head>
<body>
	<form name="frmArticle" method="post" enctype="multipart/form-data">
		<table border="0" align="center">
			<tr>
				<td width="150" align="center" bgcolor="#FF9933">글번호</td>
				<td><input type="text" value="${article.articleNO }" disabled />
					<input type="hidden" name="articleNO" value="${article.articleNO}" />
				</td>
			</tr>
			<tr>
				<td width="150" align="center" bgcolor="#FF9933">작성자 아이디</td>
				<td><input type="text" value="${article.id }" name="id"
					disabled /></td>
			</tr>
			<tr>
				<td width="150" align="center" bgcolor="#FF9933">제목</td>
				<td><input type="text" value="${article.title }" name="title"
					id="i_title" disabled /></td>
			</tr>
			<tr>
				<td width="150" align="center" bgcolor="#FF9933">내용</td>
				<td><textarea rows="5" cols="20" name="content" id="i_content"
						disabled />${article.content }</textarea></td>
			</tr>

			<c:if
				test="${not empty article.imageFileName && article.imageFileName!='null' }">
				<tr>
					<td width="20%" align="center" bgcolor="#FF9933" rowspan="2">
						이미지</td>
					<td><input type="hidden" name="originalFileName"
						value="${article.imageFileName }" /> <img
						src="${contextPath}/download.do?imageFileName=${article.imageFileName}&articleNO=${article.articleNO }"
						id="preview" width="120" height="140" /><br></td>
				</tr>
				<tr>
					<td><input type="file" name="imageFileName "
						id="i_imageFileName" disabled onchange="readURL(this)" /></td>
				</tr>
			</c:if>
			<tr>
				<td width="20%" align="center" bgcolor="#FF9933">등록일자</td>
				<td><input type=text
					value="<fmt:formatDate value="${article.writeDate}" />" disabled />
				</td>
			</tr>
			<tr id="tr_btn_modify">
				<td colspan="2" align="center"><input type=button
					value="수정반영하기" onClick="fn_modify_article(frmArticle)"> <input
					type=button value="취소" onClick="backToList(frmArticle)"></td>
			</tr>


			<tr id="tr_btn">

				<td colspan=2 align="center">
					<%--  <c:if test="${member.id == article.id }">
	    <input type=button value="수정할 내용 활성화" onClick="fn_enable(this.form)">
	    <input type=button value="삭제하기" onClick="fn_remove_article('${contextPath}/board/removeArticle.do', ${article.articleNO})">
	    <input type=button value="리스트로 돌아가기"  onClick="backToList(this.form)">
	  </c:if>  --%> <input type=button value="수정할 내용 활성화"
					onClick="fn_enable(this.form)"> <input type=button
					value="삭제하기"
					onClick="fn_remove_article('${contextPath}/board/removeArticle.do', ${article.articleNO})">
					<input type=button value="리스트로 돌아가기"
					onClick="backToList(this.form)"> <input type=button
					value="답글쓰기"
					onClick="fn_reply_form('${contextPath}/board/replyForm.do', ${article.articleNO})">

				</td>
			</tr>
		</table>
	</form>
</body>
</html>