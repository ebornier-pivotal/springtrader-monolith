package io.pivotal.springtrader.controllers;


import io.pivotal.springtrader.services.AccountService;
import io.pivotal.springtrader.domain.AuthenticationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//import org.springframework.security.core.context.SecurityContextHolder;

/**
 * REST controller for the io.pivotal.springtrader.accounts.accounts microservice.
 * Provides the following endpoints:
 * <p><ul>
 * <li>POST <code>/login</code> login request.
 * <li>GET <code>/logout/{userId}</code> logs out the account with given user id.
 * </ul><p>
 * @author David Ferreira Pinto
 *
 */
@RestController
public class AuthenticationController {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
	/**
	 * the accountService to delegate to.
	 */
	@Autowired
	private AccountService accountService;
	
	/**
	 * Logins in the user from the authentication request passed in body.
	 * 
	 * @param authenticationRequest The request with username and password.
	 * @return HTTP status CREATED if successful.
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseStatus( HttpStatus.CREATED )
	@ResponseBody
	public Map<String, Object> login(@RequestBody AuthenticationRequest authenticationRequest) {
		logger.debug("AuthenticationController.login: login request for username: " + authenticationRequest.getUsername());
		Map<String, Object> authenticationResponse = accountService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		return authenticationResponse;// authToken and accountId;
	}

	/**
	 * Logs out the user.
	 * 
	 * @param userId The user id to log out.
	 */
	@RequestMapping(value = "/logout/{user}", method = RequestMethod.GET)
	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	public void logout(@PathVariable("user") final String userId) {
		logger.debug("AuthenticationController.logout: logout request for userid: " + userId);
		accountService.logout(userId);
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String getLogin(Model model, @ModelAttribute(value="login") AuthenticationRequest login) {
		logger.info("Logging in GET, user: " + login.getUsername());
		if(login.getUsername()==null) {
            if (!model.containsAttribute("login")) {
                model.addAttribute("login", new AuthenticationRequest());
            }
        }
		return "index";
	}
}
