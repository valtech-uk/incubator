// Copyright (c) 2013 Valtech UK Ltd.
package com.valtech.simpleupload.webapp;

import org.apache.commons.configuration.*;

/**
 * Fetches the current configuration for the application.
 */
public class SimpleUploadConfig {
	private static final int KILOBYTE = 1024;

	private static final int MEGABYTE = 1024 * KILOBYTE;

	private static final String DEFAULT_UPLOAD_FOLDER = System.getProperty("java.io.tmpdir");

	private static final int DEFAULT_MAX_IN_MEMORY_UPLOAD_SIZE = 100 * KILOBYTE;

	private static final long DEFAULT_MAX_UPLOAD_SIZE = 200 * MEGABYTE;

	private static final String DEFAULT_TITLE = "Simple Upload";

	private static final String DEFAULT_PROPERTIES_FILE = "simpleupload.properties";

	private static SimpleUploadConfig singletonInstance;

	private final Configuration config;

	/**
	 * Sets up the configuration based on available settings in the default properties file.
	 */
	public SimpleUploadConfig() {
		this(DEFAULT_PROPERTIES_FILE);
	}

	/**
	 * Sets up the configuration based on available settings in the properties file.
	 * @param propertiesFileLocation The location of the properties file.
	 */
	public SimpleUploadConfig(final String propertiesFileLocation) {
		final CompositeConfiguration compositeConfiguration = new CompositeConfiguration();
		compositeConfiguration.addConfiguration(new SystemConfiguration());

		try {
			compositeConfiguration.addConfiguration(new PropertiesConfiguration(propertiesFileLocation));
		} catch (ConfigurationException exception) {
			// TODO: Better logging
			System.out.println("Error while loading properties file: " + propertiesFileLocation + " - " + exception);
		}

		config = compositeConfiguration;
	}

	/**
	 * Fetches the path to the folder where uploaded files will be stored.
	 * @return The path.
	 */
	public String getUploadFolder() {
		return config.getString("upload.folder", DEFAULT_UPLOAD_FOLDER);
	}

	/**
	 * Fetches the maximum sized file that will be stored in memory during the upload.
	 * @return The maximum size, in bytes.
	 */
	public int getMaxInMemoryUploadSize() {
		return config.getInt("upload.inMemory.maxBytes", DEFAULT_MAX_IN_MEMORY_UPLOAD_SIZE);
	}

	/**
	 * Fetches the maximum sized file that is allowed to be uploaded.
	 * @return The maximum size, in bytes.
	 */
	public long getMaxUploadSize() {
		return config.getLong("upload.maxBytes", DEFAULT_MAX_UPLOAD_SIZE);
	}

	/**
	 * Fetches the maximum sized file that is allowed to be uploaded, in friendly text format.
	 * @return The maximum size, in bytes.
	 */
	public String getMaxUploadSizeAsText() {
		final long maxUploadSize = getMaxUploadSize();
		if (maxUploadSize < KILOBYTE) {
			return maxUploadSize + " bytes";
		} else if (maxUploadSize < MEGABYTE) {
			return (maxUploadSize / KILOBYTE) + " KB";
		} else {
			return (maxUploadSize / MEGABYTE) + " MB";
		}
	}

	/**
	 * Fetches the title of the upload web page.
	 * @return The title.
	 */
	public String getTitle() {
		return config.getString("upload.title", DEFAULT_TITLE);
	}

	/**
	 * Fetches a global shared instance of the config settings.
	 * @return The shared instance.
	 */
	public synchronized static SimpleUploadConfig getSharedInstance() {
		if (singletonInstance == null) {
			singletonInstance = new SimpleUploadConfig();
		}
		return singletonInstance;
	}
}
