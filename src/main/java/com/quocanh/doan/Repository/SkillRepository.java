package com.quocanh.doan.Repository;

import com.quocanh.doan.Model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    @Query("select s from Skill s where  s.nameSkill in :skillNames")
    List<Skill> findByNameSkills(@Param("skillNames") Set<String> skillNames);
}
