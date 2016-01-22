<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"></c:set>

<!DOCTYPE html">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>webuploader image demo</title>

<link rel="stylesheet" href="${ctx}/resources/uploader/webuploader.css">

</head>
<body>

<!--dom结构部分-->
<div id="uploader-demo">
    <!--用来存放item-->
    <div id="fileList" class="uploader-list"></div>
    <div id="filePicker">选择图片</div>
</div>

<script src="${ctx}/resources/js/jquery.js"></script>
<script src="${ctx}/resources/uploader/webuploader.min.js"></script>

<script>
$(function(){
	$.WebUploader({
		auto : true,
        swf : BASE_URL + '/resources/uploader/Uploader.swf',
        server : BASE_URL + '/upload.json',
        pick : '#filePicker',
        isImage : true,
        fileVal : 'filenameUploader',
        fileQueued : function( file ) {
            var $li = $('<div id="' + file.id + '" class="file-item thumbnail">' +
                        '<img>' +
                        '<div class="info">' + file.name + '</div>' +
                    '</div>'),
                $img = $li.find('img');

            // $list为容器jQuery实例
            var $list = $('#fileList');
            $list.append( $li );

            // 创建缩略图
            // 如果为非图片文件，可以不用调用此方法。
            // thumbnailWidth x thumbnailHeight 为 100 x 100
            var thumbnailWidth = 200;
            var thumbnailHeight = 200;
            this.makeThumb(file, function(error, src) {
                if (error) {
                    $img.replaceWith('<span>不能预览</span>');
                    return;
                }
                $img.attr('src', src);
            }, thumbnailWidth, thumbnailHeight);
        },
        uploadProgress : function( file, percentage ) {
            var $li = $('#'+file.id ),
                $percent = $li.find('.progress span');
            // 避免重复创建
            if ( !$percent.length ) {
                $percent = $('<p class="progress"><span></span></p>')
                        .appendTo($li)
                        .find('span');
            }

            $percent.css( 'width', percentage * 100 + '%' );
        },
        uploadSuccess : function( file ) {
            $( '#'+file.id ).addClass('upload-state-done');
        },
        uploadError : function( file ) {
            var $li = $( '#'+file.id ),
                $error = $li.find('div.error');
            // 避免重复创建
            if ( !$error.length ) {
                $error = $('<div class="error"></div>').appendTo( $li );
            }
            $error.text('上传失败');
        },
        uploadComplete : function( file ) {
            $( '#'+file.id ).find('.progress').remove();
        }
	});
});
</script>

<script>
var BASE_URL = "${ctx}";
</script>

</body>
</html>