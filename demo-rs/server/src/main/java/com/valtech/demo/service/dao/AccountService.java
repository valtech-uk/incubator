package com.valtech.demo.service.dao;

import com.valtech.demo.service.beans.Account;
import com.valtech.demo.service.beans.Result;
import com.valtech.demo.service.beans.Error;
import com.valtech.demo.service.rs.AccountServiceInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * <p>Executes the business logic for checking and updating a user's account.</p>
 */
public class AccountService extends AccountServiceInterface {
  /**
   * The regular expression used to perform basic validation on account numbers.
   */
  private static final Pattern ACCOUNT_NUMBER_REGEXP = Pattern.compile("0*[0-9]{1,10}");

  /**
   * The regular expression used to perform basic validation on usernames.
   */
  private static final Pattern USERNAME_REGEXP = Pattern.compile("[^\\s]+@[^\\s]+\\.[^\\s]+");

  /**
   * Dummy accounts 'database' for demo purposes.
   */
  private static final Collection<Account> ACCOUNTS_DB = Collections.synchronizedList(new ArrayList<Account>());

  /**
   * Lists all of the known accounts.
   * @return The accounts results.
   */
  @Override
  public Result listAccounts() {
    final Result result = new Result();
    result.setAccountInfo(ACCOUNTS_DB);
    return result;
  }

  /**
   * Creates a new account.
   * @param username The username.
   * @param accountNumber The account number.
   * @return The results of the transaction.
   */
  @Override
  public Result createAccount(final String username, final String accountNumber) {
    final Result result;

    final Account newAccount = new Account(username, accountNumber);
    if (username == null || username.isEmpty()) {
      result = new Result(newAccount, new Error(Error.Type.INVALID_USERNAME, "No username specified"));
    } else if (!USERNAME_REGEXP.matcher(username).matches()) {
      result = new Result(newAccount, new Error(Error.Type.INVALID_USERNAME));
    } else if (accountNumber == null || accountNumber.isEmpty()) {
      result = new Result(newAccount, new Error(Error.Type.INVALID_ACCOUNT, "No account specified"));
    } else if (!ACCOUNT_NUMBER_REGEXP.matcher(accountNumber).matches()) {
      result = new Result(newAccount, new Error(Error.Type.INVALID_ACCOUNT));
    } else if (findAccountByUsername(username) != null || findAccountByAccountNumber(accountNumber) != null) {
      result = new Result(newAccount, new Error(Error.Type.ACCOUNT_ALREADY_EXISTS));
    } else {
      // Nothing wrong, so we can go ahead and add the account
      ACCOUNTS_DB.add(newAccount);
      result = new Result(newAccount);
    }

    return result;
  }

  /**
   * Deletes an account.
   * @param accountNumber The account number.
   * @return The results of the transaction.
   */
  @Override
  public Result deleteAccount(final String accountNumber) {
    final Result result;

    boolean removed = false;
    final Iterator<Account> iterator = ACCOUNTS_DB.iterator();
    while (iterator.hasNext()) {
      final Account account = iterator.next();
      if (accountNumber.equals(account.getAccountNumber())) {
        iterator.remove();
        removed = true;
      }
    }

    if (removed) {
      result = new Result("Account #" + accountNumber + " deleted");
    } else {
      result = new Result(new Error(Error.Type.ACCOUNT_NOT_FOUND));
    }

    return result;
  }

  /**
   * Fetches an account via its username.
   * @param username The username.
   * @return The matched account results.
   */
  @Override
  public Result getAccountByUsername(final String username) {
    final Result result;

    final Account account = findAccountByUsername(username);
    if (account == null) {
      result = new Result(new Error(Error.Type.ACCOUNT_NOT_FOUND));
    } else {
      result = new Result(account);
    }

    return result;
  }

  /**
   * Fetches an account via its username.
   * @param username The username.
   * @return The matched account, or null if none was found.
   */
  private Account findAccountByUsername(final String username) {
    if (username != null) {
      for (final Account account : ACCOUNTS_DB) {
        if (username.equals(account.getUsername())) {
          return account;
        }
      }
    }
    return null;
  }

  /**
   * Fetches an account via its account number.
   * @param accountNumber The account number.
   * @return The matched account results.
   */
  @Override
  public Result getAccountByAccountNumber(final String accountNumber) {
    final Result result;

    final Account account = findAccountByAccountNumber(accountNumber);
    if (account == null) {
      result = new Result(new Error(Error.Type.ACCOUNT_NOT_FOUND));
    } else {
      result = new Result(account);
    }

    return result;
  }

  /**
   * Fetches an account via its account number.
   * @param accountNumber The account number.
   * @return The matched account, or null if none was found.
   */
  private Account findAccountByAccountNumber(final String accountNumber) {
    if (accountNumber != null) {
      for (final Account account : ACCOUNTS_DB) {
        if (accountNumber.equals(account.getAccountNumber())) {
          return account;
        }
      }
    }
    return null;
  }
}
