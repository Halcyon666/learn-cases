package com.whalefall.learncases.valid.standvalid;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;


/**
 * @author halcyon
 * @date 2022-05-22 21:42
 */
@Data
@ToString
public class UserAccount {

    @NotBlank(message = "用户名不能为空")
    private String name;

    @NotNull(message = "密码不能为空")
    @Size(min = 6, max = 15, message = "密码位数在6到15位之间")
    private String password;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    @NotNull(message = "邮件不能为空")
    private String email;
}
