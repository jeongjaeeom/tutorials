package io.jeongjaeeom.concurrent.posts.component;

import io.jeongjaeeom.concurrent.external.service.ExternalService;
import io.jeongjaeeom.concurrent.posts.domain.Post;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PostFetchComponent {

  private final ExternalService externalService;
  private final ThreadPoolTaskExecutor executor;

  public List<Post> getPosts(final String category) {
    log.info("동기 호출 방식으로 포스트 조회.");
    return externalService.getPostsByCategory(category);
  }

  public CompletableFuture<List<Post>> getPostsAsync(final String category) {
    log.info("비동기 호출 방식으로 포스트 조회.");
    return CompletableFuture.supplyAsync(() -> {
          log.info("supplyAsync");
          return externalService.getPostsByCategory(category);
        },
        executor
    );
  }

}
