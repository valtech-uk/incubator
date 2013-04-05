<%@ page import="com.valtech.simpleupload.webapp.SimpleUploadConfig" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- Copyright (c) 2013 Valtech UK Ltd. --%>
<html>
<head>
	<title><%=SimpleUploadConfig.getSharedInstance().getTitle()%></title>
	<link rel="stylesheet" type="text/css" href="css/main.css"/>
	<meta name="viewport" content="width=device-width">
	<script type="text/javascript" src="js/jquery-1.9.1.js"></script>
	<script type="text/javascript">
		$(function() {
			window.resetUploadMonitor = function(uploadId) {
				clearTimeout(window.timeoutHandle);
				clearInterval(window.intervalHandle);
				updateProgress(1, 1);

				window.timeoutHandle = setTimeout(function() {
					$('#uploadReady').show();
					$('#uploadInProgress').hide();
				}, 1000);
			};

			window.startUploadMonitor = function(uploadId) {
				clearTimeout(window.timeoutHandle);
				clearInterval(window.intervalHandle);

				$('#uploadReady').hide();
				updateProgress(0, 1);
				$('#uploadInProgress').show();

				window.intervalHandle = setInterval(function() {
					$.ajax({
						'url': 'handler?uploadId=' + uploadId,
						'type': 'GET',
						'cache': false,
						'dataType': 'json',
						'success': function(response, status, jqXHR) {
							updateProgress(response['bytesRead'], response['totalBytes'], response['errorMessage']);
						}
					});
				}, 400);
			};

			window.updateProgress = function(bytesRead, totalBytes, errorMessage) {
				var $uploadProgressDetail = $('#uploadInProgress').find('.progress');
				var progressText;
				if (errorMessage) {
					progressText = errorMessage;
					$uploadProgressDetail.addClass('error');
				} else if (totalBytes) {
					$uploadProgressDetail.removeClass('error');
					progressText = (Math.ceil((bytesRead / totalBytes) * 1000) / 10) + '%';

					if (bytesRead == totalBytes && totalBytes > 1) {
						// Force the iFrame to reload if it does not do so by itself
						window.timeoutHandle = setTimeout(function() {
							$('#uploadFrame').attr('src', $('#currentElement').attr('src'));
						}, 2000);
					}
				} else {
					$uploadProgressDetail.removeClass('error');
					progressText = bytesRead + ' bytes';
				}
				$uploadProgressDetail.text(progressText);
			}
		});
	</script>
</head>
<body>
	<div id="mainPanel">
		<h1><%=SimpleUploadConfig.getSharedInstance().getTitle()%></h1>
		<iframe id="uploadFrame" src="upload.jsp" width="280" height="180" seamless="seamless" scrolling="no" border="0" frameborder="0"></iframe>
		<div id="statusPanel">
			<div id="uploadReady">
				<p>Ready to upload.</p>
				<p class="note">Maximum upload size is <%=SimpleUploadConfig.getSharedInstance().getMaxUploadSizeAsText()%></p>
			</div>
			<div id="uploadInProgress">
				Uploading: <span class="progress">0%</span>
			</div>
		</div>
	</div>
</body>
</html>
