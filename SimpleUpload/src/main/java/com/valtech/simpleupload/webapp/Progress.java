// Copyright (c) 2013 Valtech UK Ltd.
package com.valtech.simpleupload.webapp;

/**
 * Stores file-upload progress data.
 */
class Progress {
	private final long bytesRead;

	private final Long totalBytes;

	private String errorMessage;

	/**
	 * Sets up a standard progress update.
	 * @param bytesRead The number of bytes read so far.
	 * @param totalBytes The total number of bytes for the upload, or null if not known.
	 */
	Progress(final long bytesRead, final Long totalBytes) {
		this.bytesRead = bytesRead;
		this.totalBytes = totalBytes;
		this.errorMessage = null;
	}

	/**
	 * Sets up an error progress update.
	 * @param errorMessage The error message.
	 */
	Progress(final String errorMessage) {
		this.bytesRead = 0;
		this.totalBytes = null;
		this.errorMessage = errorMessage;
	}

	public long getBytesRead() {
		return bytesRead;
	}

	public Long getTotalBytes() {
		return totalBytes;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
