package com.wakati.controller;

import com.wakati.enums.Status;
import com.wakati.enums.UserType;
import com.wakati.model.request.ChangePasswordRequest;
import com.wakati.model.request.DTO.FetchRequestDTO;
import com.wakati.model.request.UserRegistrationRequest;
import com.wakati.service.GetUserService;
import com.wakati.service.UserRegistrationService;
import com.wakati.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRegistrationService service;

    @Autowired
    private GetUserService getUserService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @GetMapping("/users_list")
    public ResponseEntity<?> getUsers(
            @RequestParam(required = false) Status status

    ) {
        List<UserType> types = List.of(
                UserType.CUSTOMER,
                UserType.FRONT_DESK,
                UserType.RECEIVER,
                UserType.AGENT,
                UserType.ADMIN,
                UserType.CFO,
                UserType.ADJUDICATOR
        );

        return ResponseEntity.ok(userService.getUsers(status, types));
    }

    @GetMapping("customers_list")
    public ResponseEntity<?> getCustomers(
    ) {
        return ResponseEntity.ok(userService.getUsers(Status.APPROVED, List.of(UserType.CUSTOMER)));
    }

    @PostMapping("/fetch_list")
    public ResponseEntity<?> fetchUsers(@RequestBody FetchRequestDTO request ) {
        return ResponseEntity.ok(userService.getUsersList(request.getPageSize(), request.getPageNumber()));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(userService.changePassword(request));
    }

    @GetMapping("/get_user_mobile_no")
    public ResponseEntity<?> getUserByMobileNo(@RequestParam(value="mobile_no") String mobile_no) {
        return ResponseEntity.ok(getUserService.getUserByMobileNo(mobile_no));
    }

    @GetMapping("/get_profile_status")
    public ResponseEntity<?> getProfileStatus(@RequestParam(value="mobile_no") String mobile_no) {
        return ResponseEntity.ok(getUserService.getProfileStatus(mobile_no));
    }
}