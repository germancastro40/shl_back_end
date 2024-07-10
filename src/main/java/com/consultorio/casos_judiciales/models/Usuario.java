package com.consultorio.casos_judiciales.models;

import com.consultorio.casos_judiciales.utils.UserRole;
import com.consultorio.casos_judiciales.utils.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
@Entity(name = "usuarios")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$",
            message = "The email must be a valid email"
    )
    @Column( unique = true )
    private String email;

    @Column(nullable = false)
    @NotNull(message = "the name can't be null")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "the lastname can't be null")
    private String lastname;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "the lastname can't be null")
    @Min(value = 6, message = "The password min length requires is 6 characters")
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UserRole role;

    @Column(nullable = false)
    @NotNull(message = "the phoneContact can't be null")
    @Pattern(regexp = "^[3][0-9]{9}$", message = "The phoneContact isn't valid")
    private BigInteger phoneContact;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Status status;

    @JsonIgnore
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private Set<Caso> casoAsCliente;

    @JsonIgnore
    @OneToMany(mappedBy = "abogado", cascade = CascadeType.ALL)
    private Set<Caso> casoAsAbogado;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Comentario> comentarios;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

}

