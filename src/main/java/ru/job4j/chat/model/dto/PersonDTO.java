package ru.job4j.chat.model.dto;

import ru.job4j.chat.model.validation.Operation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class PersonDTO {

    @NotBlank(message = "Name must be not empty",
            groups = {Operation.OnCreate.class, Operation.OnUpdate.class})
    private String name;

    @NotBlank(message = "Email must be not empty",
            groups = {Operation.OnCreate.class, Operation.OnUpdate.class})
    private String email;

    @NotBlank(message = "Password must be not empty",
            groups = {Operation.OnCreate.class, Operation.OnUpdate.class})
    private String password;

    @Min(value = 1, message = "Year must be more than 0",
            groups = {Operation.OnCreate.class, Operation.OnUpdate.class})
    private int roleId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
