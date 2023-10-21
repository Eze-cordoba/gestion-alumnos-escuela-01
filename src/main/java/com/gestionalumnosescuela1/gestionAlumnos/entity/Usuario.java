package com.gestionalumnosescuela1.gestionAlumnos.entity;

import com.gestionalumnosescuela1.gestionAlumnos.security.token.Token;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


@Getter
@Setter//getter and setter
@Builder//no hace falta el @autowired
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario implements UserDetails , Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private String firstname;
    private String lastname;
    @Column(unique = true) // Indica que el campo "email" debe ser único
    private String email;
    private String password;

    //ACA ESTA EL ERROR  EN CASCADE TIPO ALL
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "usuario_materia", joinColumns = {@JoinColumn(name = "usuario_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "materia_id", referencedColumnName = "id")})
    private List<Materia> materias;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE)
    private List<Token> tokens;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return email;
    }

    @Override //ese es de userservice
    public String getPassword() {
        // TODO Auto-generated method stub
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }


}
