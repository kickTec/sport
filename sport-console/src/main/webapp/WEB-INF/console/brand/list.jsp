<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
    int totalNum = Integer.parseInt(request.getAttribute("totalNum").toString()); // 总条目
    int pageSize = Integer.parseInt(request.getAttribute("pageSize").toString()); // 每页大小
    int pageNo = Integer.parseInt(request.getAttribute("pageNo").toString()); // 当前页
    int totalPage = ( totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1); // 总页码
    request.setAttribute("totalPage", totalPage);

    // 中间显示页码
    int size = 5; // 最多显示前后5页
    List<String> showPageList = new ArrayList<String>();
    if(totalPage < size){ // 总页码少于5页
        for(int i=0;i<totalPage;i++){
            showPageList.add(i+1+"");
        }
    }else{
        if(pageNo < size/2+1){ // 当前页码小于size/2时，显示固定的前size页
            for(int i=1; i<=size; i++){
                showPageList.add(i+"");
            }
        }else if(pageNo > (totalPage - size/2)){ // 当前页码大于totalPage - size/2,显示固定的后size页
            for(int i=(totalPage-size+1); i<=totalPage; i++){
                showPageList.add(i+"");
            }
        }else{ // 中间部分，显示当前页码及前后size/2页
            int startNo = pageNo - size/2;
            for(int i=startNo; i<startNo+size; i++){
                showPageList.add(i+"");
            }
        }
    }
    request.setAttribute("showPageList", showPageList);

    // 上一页
    int previous = 1;
    if(pageNo > 1){
        previous = pageNo - 1;
    }
    request.setAttribute("previous", previous);

    // 下一页
    int nextPage = totalPage;
    if(pageNo < totalPage){
        nextPage = pageNo + 1;
    }
    request.setAttribute("nextPage", nextPage);
%>
<%@ include file="../head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>sport-list</title>
</head>
<body>
<div class="box-positon">
	<div class="rpos">当前位置: 品牌管理 - 列表</div>
	<form class="ropt" action="<%=path%>/brand/add.do" method="post">
        <input type="hidden" name="listName" value="${queryName}"/>
        <input type="hidden" name="listIsDisplay" value="${queryIsDisplay}" />
        <input type="hidden" name="pageNo" value="${pageNo}" />
        <input type="hidden" name="pageSize" value="${pageSize}" />
        <input type="submit" class="add" value="添加"/>
	</form>
	<div class="clear"></div>
</div>
<div class="body-box">
<form action="<%=path%>/brand/list.do" method="post" style="padding-top:5px;">
品牌名称: <input type="text" name="name" value="${queryName}"/>
可用:<select name="isDisplay">
        <option value="2" <c:if test="${queryIsDisplay == 2}">selected="selected"</c:if>>全部</option>
		<option value="1" <c:if test="${queryIsDisplay == 1}">selected="selected"</c:if>>是</option>
		<option value="0" <c:if test="${queryIsDisplay == 0}">selected="selected"</c:if>>否</option>
	</select>
    <input type="hidden" name="pageSize" value="${pageSize}"/>
	<input type="submit" class="query" value="查询"/>
</form>
<table cellspacing="1" cellpadding="0" border="0" width="100%" class="pn-ltable">
	<thead class="pn-lthead">
		<tr>
			<th width="20"><input type="checkbox" onclick="checkBoxFunc('ids',this.checked)"/></th>
			<th>品牌ID</th>
			<th>品牌名称</th>
			<th>品牌图片</th>
			<th>品牌描述</th>
			<th>排序</th>
			<th>是否可用</th>
			<th>操作选项</th>
		</tr>
	</thead>
	<tbody class="pn-ltbody">
		<c:forEach items="${brandList}" var="brand">
			<tr bgcolor="#ffffff" onmouseout="this.bgColor='#ffffff'" onmouseover="this.bgColor='#eeeeee'">
				<td><input title="勾选框" type="checkbox" value="${brand.id }" name="ids"/></td>
				<td align="center">${brand.id }</td>
				<td align="center">${brand.name }</td>
				<td align="center">
                    <c:choose>
                        <c:when test="${fn:contains(brand.imgUrl, 'http')}">
                            <img alt="暂缺" width="40" height="40" src="${brand.imgUrl }"/>
                        </c:when>
                        <c:otherwise>
                            <img alt="暂缺" width="40" height="40" src="<%=path%>${brand.imgUrl }"/>
                        </c:otherwise>
                    </c:choose>
                </td>
				<td align="center">${brand.description }</td>
				<td align="center">${brand.sort }</td>
				<td align="center">
                    <c:if test="${brand.isDisplay == 1}">可用</c:if>
                    <c:if test="${brand.isDisplay == 0}">不可用</c:if>
                </td>
				<td align="center">
					<a class="pn-opt"
                       href="<%=path%>/brand/edit.do?brandId=${brand.id }&listPageNo=${pageNo}&listPageSize=${pageSize}&name=${brand.name}&isDisplay=${brand.isDisplay}"
                    >修改</a>
                    |
                    <a class="pn-opt" onclick="if(!confirm('您确定删除吗？')) {return false;}" href="<%=path%>/brand/delete.do?brandId=${brand.id }&listPageNo=${pageNo}&listPageSize=${pageSize}&name=${queryName}&isDisplay=${queryIsDisplay}">删除</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div class="page pb15">
	<span class="r inb_a page_b">
	
		<a href="<%=path%>/brand/list.do?isDisplay=${queryIsDisplay}&amp;name=${queryName}&amp;pageNo=1&amp;pageSize=${pageSize}"><font size="2">首页</font></a>
	
		<a href="<%=path%>/brand/list.do?isDisplay=${queryIsDisplay}&amp;name=${queryName}&amp;pageNo=${previous}&amp;pageSize=${pageSize}"><font size="2">上一页</font></a>

        <c:forEach var="page" items="${showPageList}" >
            <a href="<%=path%>/brand/list.do?isDisplay=${queryIsDisplay}&amp;name=${queryName}&amp;pageNo=${page}&amp;pageSize=${pageSize}" <c:if test="${page == pageNo}">style="background-color: #3399CC"</c:if>>${page}</a>
        </c:forEach>
	
		<a href="<%=path%>/brand/list.do?isDisplay=${queryIsDisplay}&amp;name=${queryName}&amp;pageNo=${nextPage}&amp;pageSize=${pageSize}"><font size="2">下一页</font></a>
	
		<a href="<%=path%>/brand/list.do?isDisplay=${queryIsDisplay}&amp;name=${queryName}&amp;pageNo=${totalPage}&amp;pageSize=${pageSize}"><font size="2">尾页</font></a>
	
		共<c:out value="${totalPage}"/>页
		到第<input type="text" size="2" id="PAGENO" style="width: 20px;" />页 <input type="button" onclick="window.location.href = '<%=path%>/brand/list.do?isDisplay=${queryIsDisplay}&amp;name=${queryName}&amp;pageSize=${pageSize}&amp;pageNo=' + $('#PAGENO').val() " value="确定" class="hand btn60x20" id="skip"/>
        每页显示
        <select id="pageSize" onchange="window.location.href = '<%=path%>/brand/list.do?isDisplay=${queryIsDisplay}&amp;name=${queryName}&amp;pageNo=${pageNo}&amp;pageSize='+$('#pageSize').val()">
            <option value="5" <c:if test="${pageSize == 5}">selected="selected"</c:if>>5</option>
            <option value="10" <c:if test="${pageSize == 10}">selected="selected"</c:if>>10</option>
            <option value="15" <c:if test="${pageSize == 15}">selected="selected"</c:if>>15</option>
            <option value="20" <c:if test="${pageSize == 20}">selected="selected"</c:if>>20</option>
        </select>
	</span>
</div>
<div style="margin-top:15px;"><input class="del-button" type="button" value="删除" onclick="optDelete();"/></div>
</div>
<script type="text/javascript">
    function checkBoxFunc(id, checkState){
        var elementsByName = document.getElementsByName(id);
        for(var i=0;i<elementsByName.length;i++){
            var element = elementsByName.item(i);
            element.checked = checkState;
        }
    }

    function optDelete(){
        var ids = "";
        var idsEle = document.getElementsByName("ids");
        for(var i=idsEle.length-1;i>=0;i--){
            var ele1 = idsEle.item(i);
            if(ele1.checked){
                ids = ids + ele1.value+",";
            }
        }

        if(ids !== "") {
            $.post(
                "<%=path%>/brand/deleteList.do",
                {ids:ids},
                function(data, status){
                    console.log(data);
                    var elementsByName = document.getElementsByName("ids");
                    for(var j=elementsByName.length-1;j>=0;j--){
                        var element = elementsByName.item(j);
                        if(element.checked){
                            $(element).parent().parent().remove();
                        }
                    }
                }
            )
        }
    }
</script>
</body>
</html>