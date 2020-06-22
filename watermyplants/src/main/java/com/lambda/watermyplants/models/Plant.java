package com.lambda.watermyplants.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "plants")
public class Plant extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long plantid;

    private String nickname;
    private String species;
    private String h2ofrequency;
    private String image;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    @JsonIgnoreProperties(value = "plants", allowSetters = true)
    private User user;

    public Plant() {
    }

    public Plant(String nickname, String species, String h2ofrequency, String image) {
        this.nickname = nickname;
        this.species = species;
        this.h2ofrequency = h2ofrequency;
        this.image = image;
    }

    public long getPlantid() {
        return plantid;
    }

    public void setPlantid(long plantid) {
        this.plantid = plantid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getH2ofrequency() {
        return h2ofrequency;
    }

    public void setH2ofrequency(String h2ofrequency) {
        this.h2ofrequency = h2ofrequency;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
