package com.valtech.demo.service.beans;

/**
 * <p>Represents a user's account information.</p>
 */
public class Account {
  /**
   * The username for the account.
   */
  private String username;

  /**
   * The account number.
   */
  private String accountNumber;

  /**
   * Creates an empty account.
   */
  public Account() {
  }

  /**
   * Creates an account.
   * @param username The username.
   * @param accountNumber The account number.
   */
  public Account(final String username, final String accountNumber) {
    setUsername(username);
    setAccountNumber(accountNumber);
  }

  /**
   * Sets the username for the account.
   * @param username The username.
   */
  public void setUsername(final String username) {
    this.username = username;
  }

  /**
   * Fetches the username for the account.
   * @return The username.
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets the account number.
   * @param accountNumber The account number.
   */
  public void setAccountNumber(final String accountNumber) {
    this.accountNumber = accountNumber;
  }

  /**
   * Fetches the account number.
   * @return The account number.
   */
  public String getAccountNumber() {
    return accountNumber;
  }
}
