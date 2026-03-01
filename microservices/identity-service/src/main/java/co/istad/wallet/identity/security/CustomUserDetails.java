package co.istad.wallet.identity.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomUserDetails implements UserDetails {

    private final String uuid;

    private final String username;

    private final String email;

    private final String password;

    private final String familyName;

    private final String givenName;

    private final String phoneNumber;

    private final String gender;

    private final LocalDate dob;

    private final String profileImage;

    private final String coverImage;

    private final Boolean accountNonExpired;

    private final Boolean accountNonLocked;

    private final Boolean credentialsNonExpired;

    private final Boolean isEnabled;

    private final Set<String> roles;

    private final Set<String> permissions;

    public CustomUserDetails(
            @JsonProperty("uuid") String uuid,
            @JsonProperty("username") String username,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("familyName") String familyName,
            @JsonProperty("givenName") String givenName,
            @JsonProperty("phoneNumber") String phoneNumber,
            @JsonProperty("gender") String gender,
            @JsonProperty("dob") LocalDate dob,
            @JsonProperty("profileImage") String profileImage,
            @JsonProperty("coverImage") String coverImage,
            @JsonProperty("accountNonExpired") Boolean accountNonExpired,
            @JsonProperty("accountNonLocked") Boolean accountNonLocked,
            @JsonProperty("credentialsNonExpired") Boolean credentialsNonExpired,
            @JsonProperty("isEnabled") Boolean isEnabled,
            @JsonProperty("roles") Set<String> roles,
            @JsonProperty("permission") Set<String> permissions
    ) {
        this.uuid = uuid;
        this.username = username;
        this.email = email;
        this.password = password;
        this.familyName = familyName;
        this.givenName = givenName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.dob = dob;
        this.profileImage = profileImage;
        this.coverImage = coverImage;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.isEnabled = isEnabled;
        this.roles = roles;
        this.permissions = permissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomUserDetails that = (CustomUserDetails) o;
        return Objects.equals(getUsername(), that.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
