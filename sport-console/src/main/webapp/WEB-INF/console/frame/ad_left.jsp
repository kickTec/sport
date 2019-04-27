<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="<%=path%>/css/admin.css" rel="stylesheet" type="text/css"/>
<link href="<%=path%>/css/theme.css" rel="stylesheet" type="text/css"/>
<link href="<%=path%>/css/jquery.treeview.css" rel="stylesheet" type="text/css"/>
<script src="<%=path%>/js/jquery.js" type="text/javascript"></script>

<script type="text/javascript">
    // treeview初始化
    $(document).ready(function(){
        $("#browser").treeview({
            toggle: function() {
                console.log("%s was toggled.", $(this).find(">span").text());
            }
        });
    });
</script>
</head>
<body>
<div class="left">
	<%@ include file="../date.jsp" %>
	<div class="fresh">
		 <table width="100%" border="0" cellspacing="0" cellpadding="0">
	          <tr>
	            <td height="35" align="center">
                    <img src="<%=path%>/images/admin/refresh-icon.png" />&nbsp;&nbsp;<a href="javascript:location.href=location.href">刷新</a>
                </td>
	      	 </tr>
	     </table>
	</div>
</div>
<ul class="filetree treeview" id="browser">
    <li class="collapsable lastCollapsable" id="30">
        <div class="hitarea hasChildren-hitarea collapsable-hitarea lastCollapsable-hitarea"></div>
        <span class="folder">
            <a class="" href="<%=path%>/position/list.do?root=30" onclick="queryNode(30)" target="rightFrame">新巴巴商城</a>
        </span>
    </li>
</ul>
<script type="text/javascript">
    // 向后台查询并动态生成子节点
    function queryNode(parentId){
        $.post(
            "<%=path%>/position/querySubNode.do",
            {root:parentId},
            function(data){
                var positionObj = eval('(' + data + ')');
                // 清除子节点
                $("#"+parentId+" li").remove();

                // 添加子节点
                var treeNodeStr = "";
                if(positionObj.length > 0){
                    positionObj.map(function(obj){
						if(obj.isParent){
							treeNodeStr = treeNodeStr + "<li id='"+obj.id+"'><span class='folder'>";
                            treeNodeStr = treeNodeStr + "<a href='<%=path%>/position/list.do?root="+obj.id+"' onclick='queryNode("+obj.id+")' target='rightFrame'>"+obj.name+"</a>";
						}else{
							treeNodeStr = treeNodeStr + "<li id='"+obj.id+"'><span class='file'>";
                            treeNodeStr = treeNodeStr + "<a href='<%=path%>/ad/list.do?positionId="+obj.id+"' target='rightFrame'>"+obj.name+"</a>";
						}
						treeNodeStr = treeNodeStr +"</span></li>";
                    });
                }
                var branches = $(treeNodeStr).appendTo("#"+parentId);
                $("#browser").treeview({
                    add: branches
                });
            }
        )
    }
</script>
</body>
</html>