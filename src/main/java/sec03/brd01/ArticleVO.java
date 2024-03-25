package sec03.brd01;

import java.sql.Date;

import lombok.Data;

@Data
public class ArticleVO {
	
	
	private int level;
	private int articleNO;
	private int parentNO;
	private String title;
	private String content;
	private String imageFileName;
	private String id;
	private Date writeDate;
	
	public ArticleVO() {
	
	}
	
	

}
