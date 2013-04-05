<%@ page import="java.util.UUID" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- Copyright (c) 2013 Valtech UK Ltd. --%>
<html>
<head>
	<title>%=SimpleUploadConfig.getSharedInstance().getTitle()%></title>
	<link rel="stylesheet" type="text/css" href="css/upload.css"/>
	<script type="text/javascript" src="js/jquery-1.9.1.min.js"></script>
	<script type="text/javascript">
		$(function() {
			var uploadId = '<%=UUID.randomUUID().toString().replace("-", "")%>';
			var actionUrl = $('#uploader').attr('action').replace(/\?.*/, '') + '?uploadId=' + uploadId;
			$('#uploader').attr('action', actionUrl);

			if (window.parent && window.parent.resetUploadMonitor) {
				window.parent.resetUploadMonitor(uploadId);
			}

			$('#uploader').submit(function() {
				if ($('#uploadedFile').val()) {
					$('.filePanel .mandatory').hide();
					if (window.parent && window.parent.startUploadMonitor) {
						window.parent.startUploadMonitor(uploadId);
					}
					return true;
				} else {
					$('.filePanel .mandatory').show();
					return false;
				}
			});
		});
	</script>
</head>
<body>
	<form id="uploader" method="POST" enctype="multipart/form-data" action="handler">
		<div class="filePanel">
			<input id="uploadedFile" type="file" name="uploadedFile">
			<div class="mandatory">You must select a file to upload.</div>
		</div>
		<div class="submitPanel">
			<input type="submit" name="submit" id="uploadSubmit" value="Upload to server">
		</div>
	</form>
</body>
</html>
