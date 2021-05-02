package com.swlab.dashboard.controller;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.swlab.dashboard.dto.UserDto;
import com.swlab.dashboard.model.user.SecurityUser;
import com.swlab.dashboard.model.user.UserRole;
import com.swlab.dashboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class LoginController {

    private final UserService userService;

    @RequestMapping(value = {"", "/login"})
    public String getLogin(@AuthenticationPrincipal SecurityUser user, HttpServletRequest req) {
        if (user != null && user.getRoleType().contains(UserRole.RoleType.USER)) {
            return "redirect:/home";
        }
        return "login";
    }

    @GetMapping("/join")
    public String getJoin() {
        return "join";
    }

    @ResponseBody
    @PostMapping(path = "/join", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> postJoin(@RequestBody UserDto userDto) {
        Map<String, Object> res = new HashMap<>();

        if (userService.findByEmail(userDto.getEmail()).isPresent()) {
            res.put("duplicate", true);
            return res;
        }

        res.put("success", userService.join(userDto) != null ? true : false);
        return res;
    }

    @GetMapping("/denied")
    public String accessDenied() {
        return "denied";
    }
}
