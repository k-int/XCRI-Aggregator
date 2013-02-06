package com.k_int.feedmanager

class PasswordReset {

    String username
    String password
    
    static constraints = {
        username(nullable: false, blank: false)
    }
}
