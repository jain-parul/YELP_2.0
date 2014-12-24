package com.kaizen.yelp.ui.resources;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.kaizen.yelp.domain.UserLogin;
import com.kaizen.yelp.repository.UserRepository;
import com.kaizen.yelp.ui.views.LoginView;




@Path("/kaizen")
@Produces(MediaType.TEXT_HTML)
public class LoginResource {
	
	
	private final UserRepository userRepository;
	
	
	public LoginResource(UserRepository userRepository) {
	
		this.userRepository = userRepository;
	}

	@GET
	public LoginView getHome() {
		return new LoginView();
	}
	
	@POST
	@Path("/register/")
	public void register(@FormParam("registername") String userName, @FormParam("pass") String password, @FormParam("email") String email ) {
		
		UserLogin user = new UserLogin();
		
		user.setPassword(password);
		user.set_id(userName.toLowerCase());
		user.setEmail(email);
		
		if(!(userName.isEmpty()||password.isEmpty()))
			userRepository.saveUser(user);
	}
	
	@POST
	@Path("/authenticate/")
	public Response login(@FormParam("username") String userName,  @FormParam("password") String password) {
		
		UserLogin user = userRepository.getUserbyUserName(userName);
		
		if (!(user.get_id() == null))
			if(user.getPassword().equals(password)) 
				return Response.ok().build();
		return Response.status(Status.UNAUTHORIZED).build();		
	}

}
