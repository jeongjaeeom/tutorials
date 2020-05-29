package io.jeongjaeeom.concurrent.posts.component;

import static org.assertj.core.api.Assertions.assertThat;

import io.jeongjaeeom.concurrent.posts.domain.Post;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class PostFetchComponentTest {

  @Autowired
  private PostFetchComponent postFetchComponent;

  @Test
  @DisplayName("포스트 조회 동기/블록킹 호출 테스트")
  void getPosts() {
    List<Post> posts = postFetchComponent.getPosts("재테크");
    assertThat(posts.size()).isEqualTo(4);
  }

  @Test
  @DisplayName("포스트 조회 비동기/블록킹 호출 테스트")
  void getPostsAsyncByThread() {
    CompletableFuture<List<Post>> future = postFetchComponent.getPostsAsync("책");
    log.info("아직 최종 데이터를 전달 받지는 않았지만, 다른 작업 수행 가능");
    List<Post> posts = future.join(); // 블록킹
    log.info("최종 데이터 응답 받음");
    assertThat(posts.size()).isEqualTo(3);
  }
}