package dev.mednikov.cashtracker.users.models;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users_user")
public class User implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "is_superuser")
    @ColumnDefault("0")
    private boolean superuser;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public boolean isSuperuser() {
        return superuser;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSuperuser(boolean superuser) {
        this.superuser = superuser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.superuser) {
            return List.of(new SimpleGrantedAuthority("SUPERUSER"));
        } else {
            return List.of(new SimpleGrantedAuthority("USER"));
        }
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public static final class UserBuilder {
        private Long id;
        private String email;
        private String password;
        private String name;
        private boolean superuser;

        public UserBuilder() {
        }

        public UserBuilder(User other) {
            this.id = other.id;
            this.email = other.email;
            this.password = other.password;
            this.name = other.name;
            this.superuser = other.superuser;
        }

        public static UserBuilder anUser() {
            return new UserBuilder();
        }

        public UserBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder withSuperuser(boolean superuser) {
            this.superuser = superuser;
            return this;
        }

        public User build() {
            User user = new User();
            user.password = this.password;
            user.superuser = this.superuser;
            user.id = this.id;
            user.email = this.email;
            user.name = this.name;
            return user;
        }
    }
}
