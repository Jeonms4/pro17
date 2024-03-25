package sec03.brd01;

import java.util.List;

public class BoardService {
	
	BoardDAO boardDAO;
	
	
	public BoardService() {
		System.out.println("BoardService 객체 생성");
		boardDAO=new BoardDAO();
	}

	
	List<ArticleVO> listArticles(){
		 
		return  boardDAO.selectAllArticles();
	 }
	
	//새 글 추가
	int addArticle(ArticleVO articleVO){
		System.out.println("서비스에서의 글 추가");
		boardDAO.insertNewArticle(articleVO);
		
		return boardDAO.getNewArticleNO();
	}
	
	
	//글 보기
	public ArticleVO viewArticle(int articleNO) {
		ArticleVO article = null;
		article = boardDAO.selectArticle(articleNO);
		return article;
	}

	
	
	//글 수정
	void modArticle(ArticleVO article){
		boardDAO.updateArticle(article);
	}
	
	
	//글 삭제(자식글까지 삭제, 결과를 글 목록(리스트)으로 나가게)
	public List<Integer> removeArticle(int  articleNO) {
		List<Integer> articleNOList = boardDAO.selectRemovedArticles(articleNO);
		
		boardDAO.deleteArticle(articleNO);
		
		return articleNOList;
	}
}
