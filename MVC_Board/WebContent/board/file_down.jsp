<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("utf-8");
String file_name = request.getParameter("file_name");

String savePath = "/boardUpload";
ServletContext context = getServletContext();			// 물리적인 경로 가져오기
String sDownloadPath = context.getRealPath(savePath);	// "/upload"의 물리적인 경로 가져오기
String sFilePath = sDownloadPath + "\\" + file_name;
// === 동일 ===
// 파일 물리적인 경로
	// String uploadPath = request.getRealPath("/upload");
	// 물리적인 경로 + 파일 이름
// String sFilePath = uploadPath + "\\" + fileName;
 
System.out.println(sFilePath);

byte b[] = new byte[4096];
// 파일을 가져오기 => 자바에서 제어하기 위해서 File 객체 생성
File oFile = new File(sFilePath);

FileInputStream in = new FileInputStream(oFile);

// 물리적인 경로 + 파일 이름의 => 타입 확인
String sMimeType = getServletContext().getMimeType(sFilePath);
System.out.println("sMimeType>>>"+sMimeType );

// octet-stream은  8비트로 된 일련의 데이터를 뜻합니다. 지정되지 않은 파일 형식을 의미합니다.
// 파일이 없는 경우 기본으로 설정
if(sMimeType == null) sMimeType = "application/octet-stream";

// 서버에서 응답할 때 확인한 타입으로 설정
response.setContentType(sMimeType);

// 한글이 있을 경우 브라우저 별로 한글 처리
// 브라우저 타입 확인(익스플로러, 크롬, ...)	- 익스플로러 true / 나머지 false
String userAgent = request.getHeader("User-Agent");		
boolean ie = (userAgent.indexOf("MSIE") > -1)|| (userAgent.indexOf("Trident") > -1);
String sEncoding=null;
// 익스플로러일 경우
if(ie) {
	// 파일 이름만 한글 처리 => sEncoding 변수에 저장
    sEncoding = URLEncoder.encode(file_name, "utf-8").replaceAll("\\+", "%20");
    System.out.println(sEncoding);
// 나머지 브라우저
}else{
   sEncoding = new String(file_name.getBytes("utf-8"),"8859_1");
   System.out.println(sEncoding);
}


// 한글 업로드 (이 부분이 한글 파일명이 깨지는 것을 방지해 줍니다.)
// String sEncoding = new String(fileName.getBytes("utf-8"),"8859_1");
// System.out.println(sEncoding);

// 서버에서 클라이언트에 보낼 때 파일을 브라우저에서 바로 실행하지 않고 첨부파일로 '다운' 되게 설정
// 응답할때 Header의 설정 값을 '다운'으로 바꿈
response.setHeader("Content-Disposition", "attachment; filename= " + sEncoding);

// 응답받은 것을 출력하기 위한 객체생성
ServletOutputStream out2 = response.getOutputStream();
int numRead;

// 바이트 배열b의 0번 부터 numRead번 까지 브라우저로 출력
// 입력받은 파일(in)을 ServletOutputStream out2 객체를 사용해서 출력
while((numRead = in.read(b, 0, b.length)) != -1) {
	out2.write(b, 0, numRead);
}
out2.flush(); 
out2.close();
in.close();

out.clear();
out = pageContext.pushBody();
	// ServletUtils.returnFile( PdsMisc.UPLOAD_DIRECTORY + File.separator + StringMisc.uniToEuc( fileName ), response.getOutputStream() );
%>