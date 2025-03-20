<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%

System.out.println("....aa11......."+request.getHeader("referer"));

System.out.println("..........."+request.getParameter("sido_cd"));

response.setContentType("application/json");



response.setHeader("Access-Control-Allow-Headers", "Content-Type");
//response.setHeader("Access-Control-Allow-Origin", "*");

response.setHeader("Access-Control-Allow-Origin", "https://www.osmb.go.kr");
//response.addHeader("Access-Control-Allow-Origin", "https://osmb.go.kr");
%>
{"result" : "ok"}