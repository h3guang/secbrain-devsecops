package com.example.demo;

import org.springframework.web.bind.annotation.*;
import java.sql.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    // 漏洞1：SQL注入漏洞
    @GetMapping("/search")
    public String searchUser(@RequestParam String keyword) {
        // 危险：直接拼接用户输入到SQL语句
        String query = "SELECT * FROM users WHERE name LIKE '%" + keyword + "%'";
        try {
            Statement stmt = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/db", "root", "password123"
            ).createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return "查询成功";
        } catch (SQLException e) {
            return "查询失败";
        }
    }

    // 漏洞2：硬编码密钥
    private static final String API_KEY = "sk-1234567890abcdef";
    private static final String SECRET = "my-secret-key-2024";

    @GetMapping("/data")
    public String getSensitiveData() {
        // 危险：硬编码密钥直接暴露在代码中
        return "使用密钥 " + API_KEY + " 获取敏感数据";
    }

    // 漏洞3：日志注入风险
    @PostMapping("/log")
    public String logMessage(@RequestBody String message) {
        // 危险：未经处理的用户输入直接写入日志
        System.out.println("用户消息：" + message);
        return "日志已记录";
    }
}