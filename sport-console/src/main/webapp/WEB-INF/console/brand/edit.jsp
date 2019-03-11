<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.kenick.sport.pojo.product.Brand" %>
<%
	String path = request.getContextPath();
    Brand brand = (Brand) request.getAttribute("brand");
    if(brand.getImgUrl().contains("http")){
        request.setAttribute("picUrl",brand.getImgUrl());
    }else{
        request.setAttribute("picUrl",path+brand.getImgUrl());
    }
%>
<%@ include file="../head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>sport-edit</title>
</head>
<body>
<div class="box-positon">
	<div class="rpos">当前位置: 品牌管理 - 编辑</div>
	<form class="ropt">
        <input type="hidden" name="name" value="${name}"/>
        <input type="hidden" name="isDisplay" value="${isDisplay}"/>
        <input type="hidden" name="pageNo" value="${listPageNo}"/>
        <input type="hidden" name="pageSize" value="${listPageSize}"/>
		<input type="submit" onclick="this.form.action='<%=path%>/brand/list.do';" value="返回列表" class="return-button"/>
	</form>
	<div class="clear"></div>
</div>
<div class="body-box" style="float:right">
	<form id="jvForm" action="editSubmit.do" method="post" enctype="multipart/form-data">
        <input type="hidden" name="brandId" value="${brand.id}" />
        <input type="hidden" name="listName" value="${name}"/>
        <input type="hidden" name="listIsDisplay" value="${isDisplay}"/>
        <input type="hidden" name="pageNo" value="${listPageNo}"/>
        <input type="hidden" name="pageSize" value="${listPageSize}"/>
		<table cellspacing="1" cellpadding="2" width="100%" border="0" class="pn-ftable">
			<tbody>
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>
						品牌名称:
					</td>
					<td width="80%" class="pn-fcontent">
						<input id="brandName" type="text" class="required" name="name" maxlength="100" value="${brand.name}"/>
					</td>
				</tr>
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>
						上传商品图片(90x150尺寸):</td>
						<td width="80%" class="pn-fcontent">
						注:该尺寸图片必须为90x150。
					</td>
				</tr>
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h"></td>
						<td width="80%" class="pn-fcontent">
						<img width="100" height="100" id="allUrl" src="${picUrl}"/>
						<input type="hidden" name="imgUrl" id="imgUrl"/>
						<input id="previewPicId" type="file" name="pic" onchange="previewPic(this)"/>
					</td>
				</tr>
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						品牌描述:</td><td width="80%" class="pn-fcontent">
						<input id="brandDesc" type="text" class="required" name="description" maxlength="80"  size="60" value="${brand.description}"/>
					</td>
				</tr>
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						排序:</td><td width="80%" class="pn-fcontent">
						<input id="brandSort" type="text" class="required" name="sort" maxlength="80" value="${brand.sort}"/>
					</td>
				</tr>
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						是否可用:
                    </td>
                    <td width="80%" class="pn-fcontent">
						<input id="radio1" type="radio" name="isDisplay" value="1" <c:if test="${brand.isDisplay == 1 }">checked="checked"</c:if>/>可用
						<input id="radio0" type="radio" name="isDisplay" value="0" <c:if test="${brand.isDisplay == 0 }">checked="checked"</c:if>/>不可用
					</td>
				</tr>
			</tbody>
			<tbody>
				<tr>
					<td class="pn-fbutton" colspan="2">
						<input type="submit" class="submit" value="提交"/> &nbsp;
                        <input type="reset" class="reset" value="重置" onclick="resetJs()"/>
					</td>
				</tr>
			</tbody>
		</table>
	</form>
</div>
<script type="text/javascript">
    var local = window.location;
    var contextPath = local.pathname.split("/")[1];

    function previewPic(){
        var uploadFile = document.getElementById("previewPicId");
        var previewImg = document.getElementById("allUrl");
        // 设置预览
        if(uploadFile.files && uploadFile.files[0]){
            previewImg.style.display = "block";
            previewImg.src = window.URL.createObjectURL(uploadFile.files[0]);

            document.getElementById("uploadPicId").style.visibility = "visible";
        }
    }
    
    function resetJs() {
        // 从request域中获取原值
        var oldName = '<%=((Brand)request.getAttribute("brand")).getName()%>';
        var oldImgUrl = '<%=((Brand)request.getAttribute("brand")).getImgUrl()%>';
        var oldDescription = '<%=((Brand)request.getAttribute("brand")).getDescription()%>';
        var oldSort = '<%=((Brand)request.getAttribute("brand")).getSort()%>';
        var oldIsDisplay = '<%=((Brand)request.getAttribute("brand")).getIsDisplay()%>';

        // 将原值设置到对应组件中
        $("#brandName").val(oldName);
        $("#allUrl").val('<%=request.getContextPath()%>'+oldImgUrl);
        $("#brandDesc").val(oldDescription);
        $("#brandSort").val(oldSort);
        if(oldIsDisplay == 0){
            $("#radio0").attr("checked","checked");
            $("#radio1").attr("checked","");
        }
        if(oldIsDisplay == 1){
            $("#radio0").attr("checked","");
            $("#radio1").attr("checked","checked");
        }
    }
</script>
</body>
</html>