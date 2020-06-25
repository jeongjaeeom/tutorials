package io.jeongjaeeom.members.domain;

import io.jeongjaeeom.teams.domain.Team;
import io.jeongjaeeom.teams.domain.TeamRepository;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private TeamRepository teamRepository;

  @Autowired
  private EntityManager em;

  @AfterEach
  void tearDown() {
  }

  @Test
  public void createMemberTest() {
    Team teamA = Team.builder().name("teamA").build();
    Team savedTeamA = teamRepository.save(teamA);

    Member member1 = Member.builder()
        .username("member1")
        .age(34)
        .profile(Profile.builder().tall(183).build())
        .team(savedTeamA)
        .build();

    Member savedMember1 = memberRepository.save(member1);

    em.flush();
    em.clear();

    Member memberA = memberRepository.findById(savedMember1.getId()).orElseThrow();
    // int tall = memberA.getProfile().getTall();

    Member memberClone = Member.builder()
        .username(memberA.getUsername())
        .profile(
            Profile.builder()
                .tall(memberA.getProfile().getTall())
                .build()

        )
        .build();

    int tall = memberClone.getProfile().getTall();


  }
}
