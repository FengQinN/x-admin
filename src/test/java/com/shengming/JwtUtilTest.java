package com.shengming;

import com.shengming.common.utils.JwtUtil;
import com.shengming.sys.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtUtilTest {
    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void testCreateJwt(){
        User user = new User();
        user.setUsername("admin");
        user.setPassword("123456");
        String jwtToken = jwtUtil.createToken(user);
        System.out.println(jwtToken);
    }
}
