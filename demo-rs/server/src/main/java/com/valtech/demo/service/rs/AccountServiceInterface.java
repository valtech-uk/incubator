package com.valtech.demo.service.rs;

import com.valtech.demo.service.beans.Result;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * <p>Defines the JAX-RS webservice for checking and updating a user's account.</p>
 * <p>Default format is XML, but JSON is also available.</p>
 */
@Path("/")
@Produces("application/xml")
public abstract class AccountServiceInterface {
  @GET
  @Path("/")
  public Result defaultAction() {
    return listAccounts();
  }

  @GET
  @Path("/all.xml")
  public abstract Result listAccounts();

  @GET
  @Path("/all.json")
  @Produces("application/json")
  public Result listAccountsAsJSON() {
    return listAccounts();
  }

  @GET
  @Path("/create/{username}/{accountNumber}.xml")
  public abstract Result createAccount(@PathParam("username") final String username, @PathParam("accountNumber") final String accountNumber);

  @GET
  @Path("/create/{username}/{accountNumber}.json")
  @Produces("application/json")
  public Result createAccountAsJSON(@PathParam("username") final String username, @PathParam("accountNumber") final String accountNumber) {
    return createAccount(username, accountNumber);
  }

  @GET
  @Path("/delete/{accountNumber}.xml")
  public abstract Result deleteAccount(@PathParam("accountNumber") final String accountNumber);

  @GET
  @Path("/delete/{accountNumber}.json")
  @Produces("application/json")
  public Result deleteAccountAsJSON(@PathParam("accountNumber") final String accountNumber) {
    return deleteAccount(accountNumber);
  }

  @GET
  @Path("/find/user/{username}.xml")
  public abstract Result getAccountByUsername(@PathParam("username") final String username);

  @GET
  @Path("/find/user/{username}.json")
  @Produces("application/json")
  public Result getAccountByUsernameAsJSON(@PathParam("username") final String username) {
    return getAccountByUsername(username);
  }

  @GET
  @Path("/find/account/{accountNumber}.xml")
  public abstract Result getAccountByAccountNumber(@PathParam("accountNumber") final String accountNumber);

  @GET
  @Path("/find/account/{accountNumber}.json")
  @Produces("application/json")
  public Result getAccountByAccountNumberAsJSON(@PathParam("accountNumber") final String accountNumber) {
    return getAccountByAccountNumber(accountNumber);
  }
}
