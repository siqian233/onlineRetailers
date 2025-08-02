package com.lightning.service;

import com.lightning.web.bean.UserDTO;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    @Transactional
    UserDTO registerUser(UserDTO userDTO);
}
