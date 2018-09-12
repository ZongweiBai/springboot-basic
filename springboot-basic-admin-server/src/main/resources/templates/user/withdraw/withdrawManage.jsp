﻿<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <%@include file="/assets/page/base.jsp"%>
    <link href="<%=request.getContextPath()%>/assets/lib/bootstrap-table/bootstrap.min.css" rel="stylesheet" type="text/css" >
    <link href="<%=request.getContextPath()%>/assets/lib/bootstrap-table/bootstrap-table.min.css" rel="stylesheet" type="text/css" >
    <script type="text/javascript" src="<%=request.getContextPath()%>/assets/lib/My97DatePicker/WdatePicker.js"></script>
<title>佣金提现管理</title>
</head>
<body>
<nav class="breadcrumb"><i style="font-size: 18px;" class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 用户管理 <span class="c-gray en">&gt;</span> 佣金提现管理</nav>
<div class="pd-20">

	<form id="menuForm" name="menuForm" onsubmit="$('#menuTable').bootstrapTable('refresh');return false;" method="post">
		<div style="margin-top: 10px;margin-bottom: 10px;">
			手机号码：
            <input type="text" class="input-text" style="width:120px" placeholder="输入手机号码" id="userAccount" name="userAccount">
			申请时间：
			<input type="text" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}'})" id="datemin" class="input-text Wdate" style="width:120px;">
			-
			<input type="text" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'datemin\')}',maxDate:'%y-%M-%d'})" id="datemax" class="input-text Wdate" style="width:120px;">
			提现状态：
			<select class="input-text" style="width: 120px;" id="result">
				<option value="">选择提现状态</option>
				<option value="UN_DEAL">未处理</option>
				<option value="SUCCESS">已拨款</option>
				<option value="DENY">拒绝提现</option>
			</select>
			<button type="submit" class="btn btn-success" id="" name=""><i style="font-size: 18px;" class="Hui-iconfont">&#xe665;</i>搜索</button>
		</div>
	</form>
	<table id="menuTable" class="table table-border table-bordered table-bg">

	</table>
</div>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/lib/bootstrap-table/bootstrap-table.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/lib/bootstrap-table/bootstrap-table-zh-CN.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/DateFormat.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/user/withdraw/js/withdrawManage.js"></script>
</body>
</html>