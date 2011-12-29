package com.k_int.feedmanager

class ShiroUser 
{
    String username
	String name
	String email
	String institution
    String passwordHash
	Boolean verified = Boolean.FALSE; 	/* verified = true when user clicks their activation email link */
	Boolean active = Boolean.FALSE; 	/* active = true when enabled by an administrator */
    
    static hasMany = [ roles: ShiroRole, permissions: String ]

    static constraints = {
        username(nullable: false, blank: false)
		name(nullable: false, blank: false)
		email(email: true, nullable: false, blank: false)
		institution(nullable: true, blank: true)
    }
}