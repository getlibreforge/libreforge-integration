package com.libreforge.integration.security.api;

import com.libreforge.integration.common.ApplicationException;
import com.libreforge.integration.data.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/count", produces = "application/json")
    public Integer getUsersCount() throws ApplicationException {

        return userRepository.findAll().size();
    }
}
