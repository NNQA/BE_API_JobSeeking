package com.quocanh.doan.Controller.User;

import com.quocanh.doan.Service.ImplementService.User.UserPrincipal;
import com.quocanh.doan.Service.ImplementService.User.UserService;
import com.quocanh.doan.dto.request.UserUpdate;
import com.quocanh.doan.dto.response.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getuser")
    public ResponseEntity GetUser() {
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @GetMapping("/getCurrentUser")
    public ResponseEntity<UserResponse> GetCurrentUser() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok().body(
                new UserResponse(
                        userPrincipal.getId(),
                        userPrincipal.getName(),
                        userPrincipal.getEmail(),
                        userPrincipal.getAuthorities(),
                        userPrincipal.getIsNewUser(),
                        userPrincipal.getPhone(),
                        userPrincipal.getUniversity(),
                        userPrincipal.getExperiencelevel()
                )
        );
    }

    @PutMapping("/updateNewUser")
    public  ResponseEntity<?> UpdateNewUser(@RequestBody UserUpdate userUpdate) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.updateNewUser(userPrincipal, userUpdate);

        return ResponseEntity.ok().body(
                HttpStatus.OK
        );
    }
}
