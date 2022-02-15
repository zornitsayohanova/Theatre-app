package com.theatreapp.theatre.data.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends BaseEntity implements UserDetails {
    @Column
    private String username;

    @Column
    private String email;

    @Column
    private String password;

    @OneToMany
    private List<Actor> favouriteActors;

    @OneToMany
    private List<Theatre> favouriteTheatres;

    @OneToMany
    private List<Play> favouritePlays;

    @OneToMany
    private List<Actor> targetedActors;

    @OneToMany
    private List<Theatre> targetedTheatres;

    @OneToMany
    private List<Play> targetedPlays;
    
    @OneToMany
    private List<Play> watchedPlays;

    @OneToMany
    private List<Actor> watchedActors;

    @OneToMany
    private List<Theatre> watchedTheatres;

    @Column
    private int totalActivityScore;

    @Column
    private Boolean isGameEligible;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private Boolean hasWonTicket;

    @Column
    private boolean isAccountNonExpired;

    @Column
    private boolean isAccountNonLocked;

    @Column
    private boolean isCredentialsNonExpired;

    @Column
    private boolean isEnabled;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> authorities;
}