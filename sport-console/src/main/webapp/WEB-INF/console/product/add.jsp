<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../head.jsp" %>
<%
	String basePath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>babasport-add</title>
<style type="">
.h2_ch a:hover, .h2_ch a.here {
    color: #FFF;
    font-weight: bold;
    background-position: 0px -32px;
}
.h2_ch a {
    float: left;
    height: 32px;
    margin-right: -1px;
    padding: 0px 16px;
    line-height: 32px;
    font-size: 14px;
    font-weight: normal;
    border: 1px solid #C5C5C5;
    background: url('<%=basePath%>/images/admin/bg_ch.gif') repeat-x scroll 0% 0% transparent;
}
a {
    color: #06C;
    text-decoration: none;
}
</style>
<script type="text/javascript">
$(function(){
    // 页面上方选项卡点击切换
    var selectedItem;
    $('#tabs a').each(function(index,item){
        // 获取初始化显示的A标签

        var classAttr = $(item).attr('class');
        if(classAttr === 'here'){
            selectedItem = item;
        };

        // 每个A标签绑定点击事件
        $(item).click(function(){
            // 切换面板
            var refAttr = $(item).attr('ref');
            $('tbody').each(function(index,item){
                var tbodyId = '#'+$(item).attr('id');
                if(refAttr === tbodyId){
                    $(item).show();
                }else{
                    if($(item).attr('id') !== ''){
                        $(item).hide();
                    }
                }
            });

            // 切换标记
            $(item).attr('class','here');
            $(selectedItem).attr('class','nor');
            selectedItem = item;

            // 将productdesc文本域转换为kindeditor
            if(refAttr == '#tab_3'){
                // 编辑器参数
                var kingEditorParams = {
                    //指定上传文件参数名称
                    filePostName  : "uploadFile",
                    //指定上传文件请求的url
                    uploadJson : '<%=basePath%>/upload/uploadFck.do',
                    //上传类型，分别为image、flash、media、file
                    dir : "image"
                };
                KindEditor.create('#productdesc',kingEditorParams);
                KindEditor.sync();
            }
        });
    });
});

function uploadPic(){
    var uploadFile = document.getElementById("uploadPicId"); // 上传文件元素
    if(uploadFile.files){ // 上传文件存在
        // 遍历所有上传文件，并生成对应的img标签
        var html = '<tr>';
        for(var i=0;i<uploadFile.files.length;i++){
            var picUrl = window.URL.createObjectURL(uploadFile.files[i]);
            html += '<td><img height="150" width="200" style="block" src="';
            html += picUrl;
            html += '"/></td>';
        }
        html += '</tr>';
        // 将所有img标签添加到容器中,用于展示
        $("#tab_2").append(html);
    }
}
</script>
</head>
<body>
<div class="box-positon">
	<div class="rpos">当前位置: 商品管理 - 添加</div>
	<form class="ropt" action="<%=basePath%>/product/list.do">
		<input type="hidden" name="productName" value="${queryProductName}"/>
		<input type="hidden" name="brandId" value="${queryBrandId}"/>
		<input type="hidden" name="isShow" value="${queryIsShow}"/>
		<input type="hidden" name="pageNo" value="${pageNo}"/>
		<input type="hidden" name="pageSize" value="${pageSize}"/>
		<input type="submit" value="返回列表" class="return-button"/>
	</form>
	<div class="clear"></div>
</div>
<h2 class="h2_ch">
    <span id="tabs">
        <a href="javascript:void(0);" ref="#tab_1" title="基本信息" class="here">基本信息</a>
        <a href="javascript:void(0);" ref="#tab_2" title="商品图片" class="nor">商品图片</a>
        <a href="javascript:void(0);" ref="#tab_3" title="商品描述" class="nor">商品描述</a>
        <a href="javascript:void(0);" ref="#tab_4" title="包装清单" class="nor">包装清单</a>
    </span>
</h2>
<div class="body-box" style="float:right">
	<form id="jvForm" action="<%=basePath%>/product/addSubmit.do" method="post" enctype="multipart/form-data">
		<input type="hidden" name="queryProductName" value="${queryProductName}"/>
		<input type="hidden" name="queryBrandId" value="${queryBrandId}"/>
		<input type="hidden" name="queryIsShow" value="${queryIsShow}"/>
		<input type="hidden" name="pageNo" value="${pageNo}"/>
		<input type="hidden" name="pageSize" value="${pageSize}"/>
		<table cellspacing="1" cellpadding="2" width="100%" border="0" class="pn-ftable">
			<tbody id="tab_1">
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>
						商品类型:</td><td width="80%" class="pn-fcontent">
								<select name="typeId">
                                    <option value="">请选择</option>
                                    <option value="2">瑜珈服</option>
                                    <option value="3">瑜伽辅助</option>
                                    <option value="4">瑜伽铺巾</option>
                                    <option value="5">瑜伽垫</option>
                                    <option value="6">舞蹈鞋服</option>
                                    <option value="7">其它</option>
								</select>
					</td>
				</tr>
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>
						商品名称:</td><td width="80%" class="pn-fcontent">
						<input type="text" class="required" name="name" maxlength="100" size="100"/>
					</td>
				</tr>
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						商品品牌:</td><td width="80%" class="pn-fcontent">
						<select name="brandId">
							<option value="">请选择品牌</option>
                            <c:forEach items="${brandList}" var="brand">
                                <option value="${brand.id}">${brand.name}</option>
                            </c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						商品毛重:</td><td width="80%" class="pn-fcontent">
						<input type="text" value="0.6" class="required" name="weight" maxlength="10"/>KG
					</td>
				</tr>
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>
						颜色:</td><td width="80%" class="pn-fcontent">
							<c:forEach items="${colorList}" var="color">
                                <input type="checkbox" value="${color.id}" name="colors"/>${color.name}
                            </c:forEach>
					</td>
				</tr>
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>
						尺码:</td><td width="80%" class="pn-fcontent">
						<input type="checkbox" name="sizes" value="S"/>S
						<input type="checkbox" name="sizes" value="M"/>M
						<input type="checkbox" name="sizes" value="L"/>L
						<input type="checkbox" name="sizes" value="XL"/>XL
						<input type="checkbox" name="sizes" value="XXL"/>XXL
					</td>
				</tr>
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						状态:</td><td width="80%" class="pn-fcontent">
						<input type="checkbox" name="isNew" value="1"/>新品
						<input type="checkbox" name="isCommend" value="1"/>推荐
						<input type="checkbox" name="isHot" value="1"/>热卖
					</td>
				</tr>
			</tbody>
			<tbody id="tab_2" style="display: none">
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>
						上传商品图片(90x150尺寸):</td>
						<td width="80%" class="pn-fcontent">
						注:该尺寸图片必须为90x150。
				</tr>
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h"></td>
                    </td>
						<td width="80%" class="pn-fcontent">
						<input id="uploadPicId" type="file" onchange="uploadPic()" name="pics" multiple="multiple"/>
					</td>
				</tr>
			</tbody>
			<tbody id="tab_3" style="display: none">
				<tr>
					<td >
						<textarea rows="20" cols="180" id="productdesc" name="description"></textarea>
					</td>
				</tr>
			</tbody>
			<tbody id="tab_4" style="display: none">
				<tr>
					<td >
						<textarea rows="20" cols="180" id="productList" name="packageList"></textarea>
					</td>
				</tr>
			</tbody>
			<tbody>
				<tr>
					<td class="pn-fbutton" colspan="2">
						<input type="submit" class="submit" value="提交"/> &nbsp; <input type="reset" class="reset" value="重置"/>
					</td>
				</tr>
			</tbody>
		</table>
	</form>
</div>
</body>
</html>