package no.hvl.dat152.rest.ws.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.access.prepost.PreAuthorize;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and #userid == authentication.details.userid")
public @interface IsAdminOrThisUser {

}
