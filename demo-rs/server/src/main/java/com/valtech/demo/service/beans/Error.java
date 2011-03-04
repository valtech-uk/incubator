package com.valtech.demo.service.beans;

/**
 * <p>Represents a problem with the business-link.</p>
 */
public class Error {
  /**
   * Enumerates the different error conditions.
   */
  public enum Type {
    ACCOUNT_NOT_FOUND, INVALID_ACCOUNT, INVALID_USERNAME, ACCOUNT_ALREADY_EXISTS
  }

  /**
   * Indicates the broad category of the error.
   */
  private Type type;

  /**
   * Extra details of the error that might be useful when investigating or logging the error.
   */
  private String logMessage;

  /**
   * Any exception that was thrown.
   */
  private String exception;

  /**
   * Creates an empty error.
   */
  public Error() {
  }

  /**
   * Creates a new error with its type.
   * @param type The error category.
   */
  public Error(final Type type) {
    this(type, null, null);
  }

  /**
   * Creates a new error with its type and additional details.
   * @param type The error category.
   * @param logMessage The additional details.
   */
  public Error(final Type type, final String logMessage) {
    this(type, logMessage, null);
  }

  /**
   * Creates a new error with its type and additional details.
   * @param type The error category.
   * @param logMessage The additional details.
   * @param exception The exception details.
   */
  public Error(final Type type, final String logMessage, final Exception exception) {
    setType(type);
    setLogMessage(logMessage);
    setException(exception);
  }

  public void setType(final Type type) {
    this.type = type;
  }

  public Type getType() {
    return type;
  }

  public void setLogMessage(final String logMessage) {
    this.logMessage = logMessage;
  }

  public String getLogMessage() {
    return logMessage;
  }

  public void setException(final Exception exception) {
    this.exception = (exception == null) ? null : exception.toString();
  }

  public void setException(final String exception) {
    this.exception = exception;
  }

  public String getException() {
    return exception;
  }
}
