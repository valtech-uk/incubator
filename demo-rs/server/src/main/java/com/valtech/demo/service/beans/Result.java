package com.valtech.demo.service.beans;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>Generic container for service messages.</p>
 */
public class Result {
  /**
   * The account details.
   */
  private Collection<Account> accounts = new ArrayList<Account>();

  /**
   * The transaction error details, or null if everything was successful.
   */
  private Error error;

  /**
   * An optional result message.
   */
  private String message;

  /**
   * Creates an empty result.
   */
  public Result() {
  }

  /**
   * Creates a result with account information and no errors.
   * @param account The account details.
   */
  public Result(final Account account) {
    this(account, null);
  }

  /**
   * Creates a result with error details and no account information.
   * @param error The error details.
   */
  public Result(final Error error) {
    setError(error);
  }

  /**
   * Creates a result with account information and error details.
   * @param account The account details.
   * @param error The error details, or null if everything was successful.
   */
  public Result(final Account account, final Error error) {
    addAccount(account);
    setError(error);
  }

  /**
   * Creates a result with an info message.
   * @param message The info message.
   */
  public Result(final String message) {
    setMessage(message);
  }

  /**
   * Sets the accounts details.
   * @param accounts The accounts details.
   */
  public void setAccountInfo(final Collection<Account> accounts) {
    this.accounts = (accounts == null) ? new ArrayList<Account>() : accounts;
  }

  /**
   * Fetches the accounts details.
   * @return The accounts details.
   */
  public Collection<Account> getAccountInfo() {
    return accounts;
  }

  /**
   * Adds an account to the accounts details.
   * @param account The new account details.
   */
  public void addAccount(final Account account) {
    this.accounts.add(account);
  }

  /**
   * Sets the error details.
   * @param error The error details, or null if everything was successful.
   */
  public void setError(final Error error) {
    this.error = error;
  }

  /**
   * Fetches the error details.
   * @return The error details, or null if everything was successful.
   */
  public Error getError() {
    return error;
  }

  /**
   * Checks whether this result indicates a successful operation.
   * @return True if there are no errors, false otherwise.
   */
  public boolean isSuccess() {
    return (this.error == null);
  }

  /**
   * Sets the optional result info message.
   * @param message The message.
   */
  public void setMessage(final String message) {
    this.message = message;
  }

  /**
   * Fetches the optional result info message.
   * @return The message.
   */
  public String getMessage() {
    return message;
  }
}
