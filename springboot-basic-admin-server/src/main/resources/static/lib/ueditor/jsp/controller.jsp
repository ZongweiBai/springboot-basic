<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.baidu.ueditor.ActionEnter"
    pageEncoding="UTF-8"%>
<%@ page import="com.guotion.arrive.common.constant.Constant" %>
<%@ page import="java.io.File" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%

    request.setCharacterEncoding( "utf-8" );
	response.setHeader("Content-Type" , "text/html");

	//保存图片文件
	String filePath = Constant.FileConstant.IMG_FILE_PATH;
	File imageFile = new File(filePath);
	if (!imageFile.exists()) {
		imageFile.mkdirs();
	}
	
	String rootPath = application.getRealPath( "/" );
	String physicsPath = filePath;
	String imageUrlPrefix = Constant.FileURLConstant.IMG_FILE_URL ;

//	out.write( new ActionEnter( request, rootPath ).exec() );
	out.write( new ActionEnter( request, rootPath, physicsPath, imageUrlPrefix ).exec() );

%>