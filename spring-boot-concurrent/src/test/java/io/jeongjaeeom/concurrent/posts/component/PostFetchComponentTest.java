package io.jeongjaeeom.concurrent.posts.component;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Lists;
import io.jeongjaeeom.concurrent.posts.domain.Post;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
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

  @Test
  @DisplayName("포스트 조회 비동기/블록킹 호출 테스트 전체 완료 후 머지")
  void getPostsAsyncByThread_AllOf() {
    Instant start = Instant.now();

    ArrayList<String> categories = Lists.newArrayList("재테크", "인문학", "책");
    List<CompletableFuture<List<Post>>> collect = categories.stream()
        .map(category -> postFetchComponent.getPostsAsync(category))
        .collect(Collectors.toList());

    CompletableFuture<Void> completableFuture = CompletableFuture
        .allOf(collect.toArray(new CompletableFuture[collect.size()]));

    completableFuture.join();

    List<Post> collect1 = collect.stream()
        .map(future -> future.join())
        .flatMap(x -> x.stream())
        .collect(Collectors.toList());

    Instant finish = Instant.now();
    long timeElapsed = Duration.between(start, finish).toSeconds();
    log.info("timeElapsed: {}", timeElapsed);

    assertThat(collect1.size()).isEqualTo(9);
  }

}
