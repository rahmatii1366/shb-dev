package com.shb.dev.server.role;

import com.shb.dev.server.role.ShbRoleType;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Mohammad Rahmati, 4/12/2017 2:30 PM
 */
@NameBinding
@Retention(RUNTIME)
@Target({METHOD, TYPE})
public @interface ShbRole {
    ShbRoleType roleType() default ShbRoleType.GUEST;
}
