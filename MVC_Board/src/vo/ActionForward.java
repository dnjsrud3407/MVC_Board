package vo;

public class ActionForward {
	// Servlet에서 클라이언트로 부터 요청을 받아 처리한 후 View페이지로 이동할 때
	// 이동할 url과 포워딩할 방식을 다루기 위한 클래스
	private String path;	// url
	private boolean isRedirect; // true면 Redirect 방식 / false면 Dispatch 방식
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public boolean isRedirect() {
		return isRedirect;
	}
	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}
	
	
}
