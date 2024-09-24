package com.quocanh.doan.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "skills", indexes = @Index(columnList = "name_skill"))
@Data
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Skill {

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "skills")
    private Set<Job> listJobs = new HashSet<>();
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Property must be provided")
    @Column(name = "name_skill", unique = true, nullable = false)
    private String nameSkill;
    public Skill() {}
    public Skill(String nameSkill) {
        this.nameSkill = nameSkill;
    }
}
