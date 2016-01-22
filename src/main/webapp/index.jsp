<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"></c:set>

<!DOCTYPE html">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>webuploader demo</title>
<link rel="stylesheet" href="${ctx}/resources/uploader/webuploader.css">
</head>
<body>

<div id="uploader" class="wu-example">
    <!--用来存放文件信息-->
    <div id="thelist" class="uploader-list"></div>
    <div class="btns">
        <div id="picker">选择文件</div>
        <button id="ctlBtn" class="btn btn-default">开始上传</button>
    </div>
</div>

<script src="${ctx}/resources/js/jquery.js"></script>
<script src="${ctx}/resources/uploader/webuploader.min.js"></script>

<script>
$(function(){
	
    var uploader = $.WebUploader({
    	swf : BASE_URL + '/resources/uploader/Uploader.swf',
    	server : BASE_URL + '/upload.json',
    	pick : '#picker',
    	fileVal : 'filenameUploader',
    	fileQueued : function(file) {
            var $list = $('#thelist');
            $list.append('<div id="' + file.id + '" class="item">'
                    + '<h4 class="info">' + file.name + '</h4>'
                    + '<p class="state">等待上传...</p>' + '</div>');
        },
        uploadProgress : function(file, percentage) {
            var $li = $('#' + file.id), $percent = $li.find('.progress .progress-bar');

            // 避免重复创建
            if (!$percent.length) {
                $percent = $('<div class="progress progress-striped active">'
                                + '<div class="progress-bar" role="progressbar" style="width: 0%">'
                                + '</div>' + '</div>')
                        .appendTo($li).find('.progress-bar');
            }
            $li.find('p.state').text('上传中');
            $percent.css('width', percentage * 100 + '%');
        },
        uploadSuccess : function(file) {
            $('#' + file.id).find('p.state').text('已上传');
        },
        uploadError : function(file) {
            $('#' + file.id).find('p.state').text('上传出错');
        },
        uploadComplete : function(file) {
            $('#' + file.id).find('.progress').fadeOut();
        }
    });
    
    $('#ctlBtn').on('click', function() {
        uploader.upload();
    });
});
</script>
<script>
var BASE_URL = '${ctx}';
</script>
</body>
</html>