package sec03.brd01;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;


@WebServlet("/board/*")
public class BoardController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static String ARTICLE_IMAGE_REPO = "C:\\board\\article_image";

	BoardService boardService;
	ArticleVO articleVO;
	
	public void init(ServletConfig config) throws ServletException {
		boardService = new BoardService();
		 System.out.println("BoardController 객체 만들어진 후 boardService 객체 : " + boardService);
	}

	
	public void destroy() {
		
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}
	
	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action=request.getPathInfo();
		System.out.println("action : "  + action);
		
		List<ArticleVO> articlesList = new ArrayList<ArticleVO>();	
		
		
		String nextPage = "";
		
		if(action==null) {
			articlesList=boardService.listArticles();
			request.setAttribute("articlesList", articlesList);
			nextPage = "/board01/listArticles.jsp";
		} else if (action.equals("/listArticles.do")) {
			
			articlesList=boardService.listArticles();			
			request.setAttribute("articlesList", articlesList);
			nextPage = "/board01/listArticles.jsp";
		}else if(action.equals("/articleForm.do")) {
			nextPage = "/board01/articleForm.jsp";
		}else if(action.equals("/addArticle.do")){
			int articleNO=0;
			System.out.println("글쓰기 버튼 누름");
			
			//파일도 올리고 새글의 내용도 같이 올리는 저장 형태 : 맵(키, 밸류)
			//내용이 많아 upload라는 함수로 새로 작성(매개변수는 해당 request, response)
			Map<String, String>  articleMap=upload(request, response);
			
			System.out.println("articleMap : " + articleMap);
			
			String title = articleMap.get("title");
			String content = articleMap.get("content");
			String imageFileName = articleMap.get("imageFileName");

			articleVO=new ArticleVO();
			articleVO.setParentNO(0);
			articleVO.setId("hong");
			articleVO.setTitle(title);
			articleVO.setContent(content);
			articleVO.setImageFileName(imageFileName);	
			
			
			articleNO=boardService.addArticle(articleVO);
			
			System.out.println("추가한 글 번호 : "  + articleNO);
			
			
			//파일을 첨부한 경우에만 임시 폴더에 새글 번호로 폴더명 만들기
			if(imageFileName!=null && imageFileName.length()!=0) {
				
				File srcFile = new 	File(ARTICLE_IMAGE_REPO +"\\"+"temp"+"\\"+imageFileName);
				File destDir = new File(ARTICLE_IMAGE_REPO +"\\"+articleNO);
				destDir.mkdirs();
				FileUtils.moveFileToDirectory(srcFile, destDir, true);
			}
			
			
			PrintWriter pw = response.getWriter();
			pw.print("<script>" 
			         +"  alert('새글을 추가했습니다.');" 
					 +" location.href='"+request.getContextPath()+"/board/listArticles.do';"
			         +"</script>");

			return;
			
		}else if(action.equals("/viewArticle.do")) {
			String articleNO = request.getParameter("articleNO");
			System.out.println("가져온 글 번호"+articleNO);
			
			articleVO=boardService.viewArticle(Integer.parseInt(articleNO));
			request.setAttribute("article",articleVO);
			nextPage = "/board01/viewArticle.jsp";
		}else if(action.equals("/modArticle.do")) {
			Map<String, String> articleMap = upload(request, response);
			int articleNO = Integer.parseInt(articleMap.get("articleNO"));
			articleVO.setArticleNO(articleNO);
			
			
			String title = articleMap.get("title");
			String content = articleMap.get("content");
			String imageFileName = articleMap.get("imageFileName");
			articleVO.setParentNO(0);
			articleVO.setId("hong");
			articleVO.setTitle(title);
			articleVO.setContent(content);
			articleVO.setImageFileName(imageFileName);
			
			
			boardService.modArticle(articleVO);
			
			//기존 이미지 덮어쓰기
			
			if (imageFileName != null && imageFileName.length() != 0) {
				String originalFileName = articleMap.get("originalFileName");
				File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
				File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + (articleNO));
				destDir.mkdirs();
				FileUtils.moveFileToDirectory(srcFile, destDir, true);
				
				File oldFile = new File(ARTICLE_IMAGE_REPO + "\\" + (articleNO) + "\\" + originalFileName);
				oldFile.delete();
			}
			
			PrintWriter pw = response.getWriter();
			pw.print("<script>" + "  alert('글을 수정했습니다.');" + " location.href='" + request.getContextPath()
					+ "/board/viewArticle.do?articleNO=" + articleNO + "';" + "</script>");
			return;
			
			
			
		}else if(action.equals("/removeArticle.do")) {
			
			int articleNO = Integer.parseInt(request.getParameter("articleNO"));
			System.out.println("가져온 글번호" + articleNO);
			//자식 글이 있으므로 삭제 할 경우 자식글 목록으로 리턴을 받음
			List<Integer> articleNOList = boardService.removeArticle(articleNO+1);
			
			
			//해당 글 목록에 있는 이미지폴더 삭제
			
			for (int _articleNO : articleNOList) {
				File imgDir = new File(ARTICLE_IMAGE_REPO + "\\" + (_articleNO));
				if (imgDir.exists()) {
					FileUtils.deleteDirectory(imgDir);
				}
			}
			
			
			PrintWriter pw = response.getWriter();
			pw.print("<script>" + "  alert('글을 삭제했습니다.');" + " location.href='" + request.getContextPath()
					+ "/board/listArticles.do';" + "</script>");
			return;

		}
		
		
		else {
			nextPage = "/board01/listArticles.jsp";
		}
		
		
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);
		
		
	}
	
	
	//글의 내용과 파일의 내용을 같이 올리는 함수
	Map<String, String> upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		Map<String, String> articleMap = new HashMap<String, String>();
		String encoding = "utf-8";
		
		File currentDirPath = new File(ARTICLE_IMAGE_REPO);
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(currentDirPath);
		factory.setSizeThreshold(1024 * 1024);
		
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List items = upload.parseRequest(request);
			for (int i = 0; i < items.size(); i++) {
				
				FileItem fileItem = (FileItem) items.get(i);
				if (fileItem.isFormField()) {
					System.out.println(fileItem.getFieldName() + "=" + fileItem.getString(encoding));
					articleMap.put(fileItem.getFieldName(), fileItem.getString(encoding));
				}else {
					System.out.println("파라미터명:" + fileItem.getFieldName());
					System.out.println("파일크기:" + fileItem.getSize() + "bytes");
					
					if (fileItem.getSize() > 0) {
						int idx = fileItem.getName().lastIndexOf("\\");
						
						if (idx == -1) {
							idx = fileItem.getName().lastIndexOf("/");
						}
						
						String fileName = fileItem.getName().substring(idx + 1);
						System.out.println("파일명:" + fileName);
						articleMap.put(fileItem.getFieldName(), fileName);  //익스플로러에서 업로드 파일의 경로 제거 후 map에 파일명 저장
						File uploadFile = new File(currentDirPath + "\\temp\\" + fileName);
						fileItem.write(uploadFile);
					}
					
				}
				
			}
			
			
			
			
		} catch (Exception e) {
			System.out.println("파일 업로드 중 예외 발생");
		}
		return articleMap;
	};
	
	
	

}
