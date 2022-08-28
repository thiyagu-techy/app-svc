package com.thiyagu.appsvc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thiyagu.appsvc.entity.User;
import com.thiyagu.appsvc.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/user")
@Api(value = "User", description = "User Services")
public class UserApiController {
	@Autowired
	UserService userService;

	@ApiOperation(value = "Service Health Check")
	@GetMapping("/ping")
	public String ping() {
		System.out.println("pong");
		return "pong";
	}

	@ApiOperation(value = "Create new User")
	@PostMapping(value = "/create")
	public ResponseEntity<?> createUser(@RequestBody User user) {
		if (userService.isUserExist(user)) {
			return new ResponseEntity<String>(
					"Unable to create. A User with name " + user.getName() + " already exist.", HttpStatus.CONFLICT);
		}
		userService.saveUser(user);

		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

	@ApiOperation(value = "User list")
	@GetMapping(value = "/getAll")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = userService.findAllUsers();
		if (users.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	@ApiOperation(value = "Get User by id")
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getUser(@PathVariable("id") long id) {
		User user = userService.findById(id);
		if (user == null) {
			return new ResponseEntity<String>("User with id " + id + " not found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@ApiOperation(value = "Update User")
	@PutMapping(value = "/update")
	public ResponseEntity<?> updateUser(@RequestBody User user) {
		User currentUser = userService.findById(user.getId());

		if (currentUser == null) {
			return new ResponseEntity<String>("Unable to upate. User with id " + user.getId() + " not found.",
					HttpStatus.NOT_FOUND);
		}

		currentUser.setName(user.getName());
		currentUser.setAge(user.getAge());
		currentUser.setSalary(user.getSalary());

		userService.updateUser(currentUser);
		return new ResponseEntity<User>(currentUser, HttpStatus.OK);
	}

	@ApiOperation(value = "Delete User by id")
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
		User user = userService.findById(id);
		if (user == null) {
			return new ResponseEntity<String>("Unable to delete. User with id " + id + " not found.",
					HttpStatus.NOT_FOUND);
		}
		userService.deleteUserById(id);
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "Delete all Users")
	@DeleteMapping(value = "/deleteAll")
	public ResponseEntity<User> deleteAllUsers() {
		userService.deleteAllUsers();
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}
}
