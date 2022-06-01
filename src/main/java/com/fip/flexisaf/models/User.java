package com.fip.flexisaf.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@ToString
@Table(name="Users")
@Entity
@EqualsAndHashCode
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class User implements UserDetails {
    
    @Id
    @Getter
    @Setter
    @SequenceGenerator(
            name="user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1,
            schema = "public"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;
    
    @Setter
    @Column(unique=true)
    @JsonProperty("email")
    private String email;
    
    @Getter
    @Setter
    private String name;
    
    @Setter
    private String password;
    
    @Setter
    private boolean isEnabled;
    
    @Getter
    @CreatedDate
    private LocalDateTime dateAdded;
    
    @Getter
    @Setter
//    @ManyToMany(fetch=FetchType.EAGER)
//    @JoinColumn(name="roles_id")
    private Role role;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public String getUsername() {
        return email;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return isEnabled;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return isEnabled;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return isEnabled;
    }
    
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
