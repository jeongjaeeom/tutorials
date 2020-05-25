package io.jeongjaeeom.concurrent.posts.component;

import io.jeongjaeeom.concurrent.external.service.ExternalService;
import io.jeongjaeeom.concurrent.posts.domain.Post;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PostFetchComponent {

  private final ExternalService externalService;
  private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

  public List<Post> getPosts(final String category) {
    log.info("동기 호출 방식으로 포스트 조회.");
    return externalService.getPostsByCategory(category);
  }

  public CompletableFuture<List<Post>> getPostsAsyncByThread(final String category) {
    log.info("비동기 호출 방식으로 포스트 조회.");
    CompletableFuture<List<Post>> future = new CompletableFuture<>();

    new Thread(() -> {
      log.info("새로운 쓰레드로 작업 시작");
      List<Post> posts = externalService.getPostsByCategory(category);
      future.complete(posts);
    }).start();

    return future;
  }

}
