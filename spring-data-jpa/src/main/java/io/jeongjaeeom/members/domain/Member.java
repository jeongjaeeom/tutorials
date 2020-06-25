package io.jeongjaeeom.members.domain;

import io.jeongjaeeom.teams.domain.Team;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Member {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id")
  private Team team;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Profile profile;

  @Id
  @GeneratedValue
  @Include
  private Long id;

  private String username;

  private int age;

  public void join() {
    if (this.team != null) {
      changeTeam(this.team);
    }

    if (this.profile != null) {
      changeLocker(this.profile);
    }
  }

  public void changeTeam(Team team) {
    this.team = team;
    team.getMembers().add(this);
  }

  public void changeLocker(Profile profile) {
    this.profile = profile;
  }

}
