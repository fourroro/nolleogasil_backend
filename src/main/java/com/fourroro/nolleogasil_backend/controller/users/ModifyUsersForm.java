package com.fourroro.nolleogasil_backend.controller.users;

import com.fourroro.nolleogasil_backend.entity.users.Users;
import lombok.Getter;

@Getter
public class ModifyUsersForm {
    private Users account;

    public ModifyUsersForm(Users account){
        this.account = account;
    }

    public void setAccount(Users account){
        this.account = account;
    }
}
