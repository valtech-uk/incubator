package com.valtech.demo.service.rs;

import com.valtech.demo.service.beans.Result;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * <p>Defines the JAX-RS webservice for checking and updating a user's account (returns JSON).</p>
 */
@Path("/")
public interface AccountServiceJSON {
  @GET
  @Path("/create/{username}/{accountNumber}.json")
  @Produces("application/json")
  public Result createAccount(@PathParam("username") final String username, @PathParam("accountNumber") final String accountNumber);

  @GET
  @Path("/delete/{accountNumber}.json")
  @Produces("application/json")
  public Result deleteAccount(@PathParam("accountNumber") final String accountNumber);

  @GET
  @Path("/find/user/{username}.json")
  @Produces("application/json")
  public Result getAccountByUsername(@PathParam("username") final String username);

  @GET
  @Path("/find/account/{accountNumber}.json")
  @Produces("application/json")
  public Result getAccountByAccountNumber(@PathParam("accountNumber") final String accountNumber);
}
