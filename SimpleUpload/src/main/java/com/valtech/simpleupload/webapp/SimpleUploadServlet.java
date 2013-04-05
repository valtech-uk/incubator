// Copyright (c) 2013 Valtech UK Ltd.
package com.valtech.simpleupload.webapp;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Servlet for handling incoming upload requests.
 */
public class SimpleUploadServlet extends HttpServlet {
	private static final int MAX_UPLOADS_TRACKED = 100;

	private final Map<String, Progress> progressMap = Collections.synchronizedMap(new LRUMap(MAX_UPLOADS_TRACKED));

	private final SimpleUploadConfig config = SimpleUploadConfig.getSharedInstance();

	@Override
	public void init() {
	}

	@Override
	public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		if (ServletFileUpload.isMultipartContent(request)) {
			try {
				handleFileUpload(request, response);
			} catch (Exception exception) {
				final String uploadId = request.getParameter("uploadId");
				if (uploadId != null && !uploadId.isEmpty()) {
					// Pass errors to the progress listener
					final String errorMessage = "Error during file upload - " + exception;
					progressMap.put(uploadId, new Progress(errorMessage));
				} else {
					throw new IOException("Error during file upload.", exception);
				}
			}
		} else {
			response.sendError(400, "No file uploaded.");
		}
	}

	@Override
	public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		final String requestUploadId = request.getParameter("uploadId");
		final Progress progress = progressMap.get(requestUploadId);
		if (progress != null) {
			response.setContentType("application/json");
			final PrintWriter responseWriter = response.getWriter();
			responseWriter.print("{");
			if (progress.getErrorMessage() != null) {
				// Escape JSON characters
				final String errorMessage = progress.getErrorMessage().replace("\\", "\\\\").replace("\"", "\\\"");
				responseWriter.print("\"errorMessage\":\"" + errorMessage + "\",");
			}
			responseWriter.print("\"bytesRead\":" + progress.getBytesRead() + ",");
			responseWriter.print("\"totalBytes\":" + progress.getTotalBytes());
			responseWriter.println("}");
			response.flushBuffer();
		} else {
			response.sendError(400, "No matching file upload in progress.");
		}
	}

	private void handleFileUpload(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		// Set the file upload settings
		final DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
		fileItemFactory.setSizeThreshold(config.getMaxInMemoryUploadSize());

		// Configure a repository (to ensure a secure temp location is used)
		final ServletContext servletContext = getServletConfig().getServletContext();
		final File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
		fileItemFactory.setRepository(repository);

		// Handle the uploaded items
		final ServletFileUpload upload = new ServletFileUpload(fileItemFactory);
		upload.setSizeMax(config.getMaxUploadSize());

		// Allow progress tracking, if an upload ID was provided
		final String uploadId = request.getParameter("uploadId");
		if (uploadId != null && !uploadId.isEmpty()) {
			final UploadProgressListener progressListener = new UploadProgressListener(uploadId);
			upload.setProgressListener(progressListener);
		}

		final List<FileItem> fileItems = upload.parseRequest(request);

		for (final FileItem fileItem : fileItems) {
			handleFileUploadItem(fileItem);
		}

		// File upload is completed so return to the starting page
		response.sendRedirect("upload.jsp");
	}

	private void handleFileUploadItem(final FileItem fileItem) throws Exception {
		if (!fileItem.isFormField() && fileItem.getName() != null && !fileItem.getName().isEmpty()) {
			final String fileName = FilenameUtils.getName(fileItem.getName());
			final String sanitisedFileName = fileName.replaceAll("[<>:\"/|?*\\\\]+", "_").replaceAll("^\\.", "_");
			final File savedFile = new File(config.getUploadFolder(), sanitisedFileName);

			// TODO: Better logging
			log("Uploading file to: " + savedFile.getPath());
			fileItem.write(savedFile);
		}
	}

	/**
	 * Progress listener to allow uploads to be monitored.
	 */
	private class UploadProgressListener implements ProgressListener {
		private String uploadId;

		private static final int CHUNK_SIZE = 1024 * 50;

		private long lastTotalBytes = -1;

		public UploadProgressListener(final String uploadId) {
			this.uploadId = uploadId;

			progressMap.put(uploadId, new Progress(0, null));
		}

		// TODO: Handle multiple uploads
		@Override
		public void update(final long bytesRead, final long contentLength, final int items) {
			if (lastTotalBytes == -1 || bytesRead >= lastTotalBytes + CHUNK_SIZE || bytesRead == contentLength) {
				lastTotalBytes = bytesRead;
				final Progress progress = new Progress(bytesRead, (contentLength != -1) ? contentLength : null);
				progressMap.put(uploadId, progress);
			}
		}
	}
}
