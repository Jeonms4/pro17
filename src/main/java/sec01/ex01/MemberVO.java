package sec01.ex01;

import java.sql.Date;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data

public class MemberVO {

	private String id;
	private String pwd;
	private String name;
	private String email;
	private Date joinDate;
	
	MemberVO(){
		
	}

	public MemberVO(String id, String pwd, String name, String email) {
		super();
		this.id = id;
		this.pwd = pwd;
		this.name = name;
		this.email = email;
	}

	public MemberVO(String id, String pwd, String name, String email, Date joinDate) {
		super();
		this.id = id;
		this.pwd = pwd;
		this.name = name;
		this.email = email;
		this.joinDate = joinDate;
	}
	
	
	
	
	
	
	
	
	
}
