package io.jeongjaeeom.teams.domain;

import com.google.common.collect.Lists;
import io.jeongjaeeom.members.domain.Member;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Team {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  @Default
  @OneToMany(mappedBy = "team")
  private List<Member> members = Lists.newArrayList();

}
