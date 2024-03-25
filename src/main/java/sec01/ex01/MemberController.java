package sec01.ex01;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/mem/*")
public class MemberController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	MemberDAO memberDAO;

	@Override
	public void init() throws ServletException {
		System.out.println("MemberController 객체 생성");

		memberDAO = new MemberDAO();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("get 방식");
		doHandle(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("post 방식");
		doHandle(req, resp);

	}

	protected void doHandle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//		System.out.println("memberDAO : " + memberDAO);
		// URL Path정보에 따른 분기
		String action = req.getPathInfo();
		System.out.println("액션 : " + action);

		String nextPage = null; // 컨트롤에서 지정한 다음 페이지
		List<MemberVO> membersLists = null;
		if (action == null || action.equals("/listMembers.do") || action.equals("/")) {
			// 회원 가져옴
			membersLists = memberDAO.listMembers();
			// System.out.println(memberLists);
			// 화면에 회원 목록을 뿌려야 함, 뿌리는 페이지로 셋팅해서 보냄, 해당 request에 키, 밸류로 설정해서
			// 보내야함(RequestDispatcher)
			req.setAttribute("membersLists", membersLists);
			nextPage = "/test01/listMembers.jsp";

		} else if (action.equals("/addMember.do")) {
			System.out.println("회원 추가 메서드 동작");
			String id = req.getParameter("id");
			String pwd = req.getParameter("pwd");
			String name = req.getParameter("name");
			String email = req.getParameter("email");
			MemberVO memberVO = new MemberVO(id, pwd, name, email);
			memberDAO.addMember(memberVO);

			membersLists = memberDAO.listMembers();
			req.setAttribute("membersLists", membersLists);
			nextPage = "/test01/listMembers.jsp";
		} else if (action.equals("/modMemberForm.do")) {
			System.out.println("회원을 수정하는 화면");
			String id = req.getParameter("id");
			System.out.println(id);
			req.setAttribute("id", id);

			// 회원 수정화면에서 수정할 id를 먼저 찾은 후에
//			MemberVO memInfo = memberDAO.findMember(id);
//
//			System.out.println("수정하는 화면에서 " + memInfo);
//			// 해당 객체를 설정후에
//			req.setAttribute("memInfo", memInfo);

			nextPage = "/test01/modMembers.jsp";

		} else if (action.equals("/modMember.do")) {
			System.out.println("회원을 수정");
			String id = req.getParameter("id");
			String pwd = req.getParameter("pwd");
			String name = req.getParameter("name");
			String email = req.getParameter("email");
			System.out.println("/modMember.do 요청 후 가져온 파라미터" + id + pwd + name + email);

			MemberVO memberVO = new MemberVO(id, pwd, name, email);

			System.out.println("수정할 회원 : " + memberVO.toString());

			memberDAO.modMember(memberVO);

			membersLists = memberDAO.listMembers();
			req.setAttribute("membersLists", membersLists);
			nextPage = "/test01/listMembers.jsp";
		} else if (action.equals("/delMember.do")) {
			String id = req.getParameter("id");
			memberDAO.delMember(id);
			membersLists = memberDAO.listMembers();
			req.setAttribute("membersLists", membersLists);
			nextPage = "/test01/listMembers.jsp";
		}

		RequestDispatcher dispatcher = req.getRequestDispatcher(nextPage);
		dispatcher.forward(req, resp);
	}

	@Override
	public void destroy() {
		System.out.println("MemberController 객체 소멸");

	}

}
