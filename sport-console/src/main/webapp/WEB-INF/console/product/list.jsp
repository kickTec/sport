<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../head.jsp" %>
<%
    String basePath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>babasport-list</title>
<script type="text/javascript">
//上架
function isShow(){
	//请至少选择一个
	var size = $("input[name='ids']:checked").size();
	if(size == 0){
		alert("请至少选择一个");
		return;
	}
	//你确定删除吗
	if(!confirm("你确定上架吗")){
		return;
	}
	//提交 Form表单
	$("#jvForm").attr("action","/brand/isShow.do");
	$("#jvForm").attr("method","post");
	$("#jvForm").submit();
	
}
</script>
</head>
<body>
<div class="box-positon">
	<div class="rpos">当前位置: 商品管理 - 列表</div>
	<form class="ropt" action="<%=basePath%>/product/add.do" method="post">
        <input type="hidden" name="queryProductName" value="${queryProductName}"/>
        <input type="hidden" name="queryBrandId" value="${queryBrandId}"/>
        <input type="hidden" name="queryIsShow" value="${queryIsShow}"/>
        <input type="hidden" name="pageNo" value="${pageNo}"/>
        <input type="hidden" name="pageSize" value="${pageSize}"/>
		<input class="add" type="submit" value="添加"/>
	</form>
	<div class="clear"></div>
</div>
<div class="body-box">
<form action="<%=basePath%>/product/list.do" method="post" style="padding-top:5px;">
名称: <input type="text" name="productName" value="${queryProductName}"/>
	<select name="brandId">
		<option value="">请选择品牌</option>
		<c:forEach items="${brandList}" var="brand">
            <option value="${brand.id}" <c:if test="${brand.id == queryBrandId}">selected="selected"</c:if>>${brand.name}</option>
		</c:forEach>
	</select>
	<select name="isShow">
        <option value="">全部</option>
		<option value="1" <c:if test="${queryIsShow == 1 }">selected="selected"</c:if>>上架</option>
		<option value="0" <c:if test="${queryIsShow == 0 }">selected="selected"</c:if>>下架</option>
	</select>
	<input type="submit" class="query" value="查询"/>
</form>
<form id="jvForm">
<table cellspacing="1" cellpadding="0" width="100%" border="0" class="pn-ltable">
	<thead class="pn-lthead">
		<tr>
			<th width="20"><input type="checkbox" onclick="Pn.checkbox('ids',this.checked)"/></th>
			<th>商品编号</th>
			<th>商品名称</th>
			<th>图片</th>
			<th width="4%">新品</th>
			<th width="4%">热卖</th>
			<th width="4%">推荐</th>
			<th width="4%">上下架</th>
			<th width="12%">操作选项</th>
		</tr>
	</thead>
	<tbody class="pn-ltbody">
        <c:forEach items="${productList}" var="product">
            <tr bgcolor="#ffffff" onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">
                <td><input type="checkbox" name="ids" value="${product.id}"/></td>
                <td>${product.id}</td>
                <td align="center">${product.name}</td>
                <td align="center">
                    <c:choose>
                        <c:when test="${fn:contains(fn:split(product.imgUrl,',')[0], 'http')}">
                            <img width="50" height="50" src="${fn:split(product.imgUrl,',')[0]}"/>
                        </c:when>
                        <c:otherwise>
                            <img width="50" height="50" src="<%=basePath%>${fn:split(product.imgUrl,',')[0]}"/>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td align="center">
                    <c:choose>
                        <c:when test="${product.isNew}">是</c:when>
                        <c:otherwise>否</c:otherwise>
                    </c:choose>
                </td>
                <td align="center">
                    <c:choose>
                        <c:when test="${product.isHot}">是</c:when>
                        <c:otherwise>否</c:otherwise>
                    </c:choose>
                </td>
                <td align="center">
                    <c:choose>
                        <c:when test="${product.isCommend}">是</c:when>
                        <c:otherwise>否</c:otherwise>
                    </c:choose>
                </td>
                <td align="center">
                    <c:choose>
                        <c:when test="${product.isShow}">上架</c:when>
                        <c:otherwise>下架</c:otherwise>
                    </c:choose>
                </td>
                <td align="center">
                    <a href="#" class="pn-opt">查看</a> |
                    <a href="#" class="pn-opt">修改</a> |
                    <a href="<%=basePath%>/product/deleteProduct.do?productId=${product.id}&pageNo=${pageNo}&pageSize=${pageSize}&queryProductName=${queryProductName}&queryBrandId=${queryBrandId}&queryIsShow=${queryIsShow}" onclick="if(!confirm('您确定删除吗？')) {return false;}" class="pn-opt">删除</a> |
                    <a href="<%=basePath%>/sku/list.do?productId=${product.id}&pageNo=${pageNo}&pageSize=${pageSize}&queryProductName=${queryProductName}&queryBrandId=${queryBrandId}&queryIsShow=${queryIsShow}" class="pn-opt">库存</a> |
                </td>
            </tr>
        </c:forEach>
	</tbody>
</table>
<div class="page pb15">
	<span class="r inb_a page_b">

        <a href="<%=basePath%>/product/list.do?productName=${queryProductName}&amp;brandId=${queryBrandId}&amp;isShow=${queryIsShow}&amp;pageNo=1&amp;pageSize=${pageSize}">首页</a>
	
        <a href="<%=basePath%>/product/list.do?productName=${queryProductName}&amp;brandId=${queryBrandId}&amp;isShow=${queryIsShow}&amp;pageNo=${prePage}&amp;pageSize=${pageSize}">上一页</a>

        <c:forEach items="${centerPageList}" var="centerPage">
            <a href="<%=basePath%>/product/list.do?productName=${queryProductName}&amp;brandId=${queryBrandId}&amp;isShow=${queryIsShow}&amp;pageNo=${centerPage}&amp;pageSize=${pageSize}">${centerPage}</a>
        </c:forEach>
	
        <a href="<%=basePath%>/product/list.do?productName=${queryProductName}&amp;brandId=${queryBrandId}&amp;isShow=${queryIsShow}&amp;pageNo=${nextPage}&amp;pageSize=${pageSize}">下一页</a>
	
        <a href="<%=basePath%>/product/list.do?productName=${queryProductName}&amp;brandId=${queryBrandId}&amp;isShow=${queryIsShow}&amp;pageNo=${lastPage}&amp;pageSize=${pageSize}">尾页</a>
	
		共<var>${lastPage}</var>页 到第<input type="text" size="3" id="PAGENO"/>页 <input type="button" onclick="javascript:window.location.href = '/product/list.do?&amp;isShow=0&amp;pageNo=' + $('#PAGENO').val() " value="确定" class="hand btn60x20" id="skip"/>
	
	</span>
</div>
<div style="margin-top:15px;"><input class="del-button" type="button" value="删除" onclick="optDelete();"/><input class="add" type="button" value="上架" onclick="isShow();"/><input class="del-button" type="button" value="下架" onclick="isHide();"/></div>
</form>
</div>
</body>
</html>