package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/*
 * 
 * 필터는 리소스(서블릿 또는 정적 콘텐츠)에 대한 요청이나 리소스의 응답, 또는 둘 다에 대해 필터링 작업을 수행하는 개체입니다.

필터는 doFilter 메소드에서 필터링을 수행합니다. 모든 필터는 초기화 매개변수를 얻을 수 있는 FilterConfig 객체와 필터링 작업에 필요한 리소스를 로드하는 데 사용할 수 있는 ServletContext에 대한 참조에 액세스할 수 있습니다.

필터는 웹 애플리케이션의 배포 설명자에서 구성됩니다.

이 디자인에 대해 확인된 예는 다음과 같습니다.
1.인증, 인코딩 필터
2. 로깅 및 감사 필터
3.이미지 변환 필터
4.데이터 압축 필터
5.암호화 필터
6.토큰화 필터
7. 리소스 액세스 이벤트를 트리거하는 필터
8.XSL/T 필터
9.마임형 체인 필터
 
 */
@WebFilter(urlPatterns = "/*")
public class EncoderFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("인코딩 필터 초기화 함");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)

			// FilterChain은 서블릿 컨테이너가 개발자에게 리소스에 대한 필터링된 요청의 호출 체인에 대한 보기를 제공하는 개체입니다. 필터는
			// FilterChain을 사용하여 체인의 다음 필터를 호출하거나 호출 필터가 체인의 마지막 필터인 경우 체인 끝에 있는 리소스를 호출합니다.

			throws IOException, ServletException {
		System.out.println("인코딩 필터 작동 중");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		chain.doFilter(request, response);

	}

}
