package io.jeongjaeeom.concurrent.posts.component;

import static org.junit.jupiter.api.Assertions.*;

import io.jeongjaeeom.concurrent.posts.domain.Post;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostFetchComponentTest {

  @Autowired
  private PostFetchComponent postFetchComponent;

  @Test
  void getPosts() {
    List<Post> posts = postFetchComponent.getPosts("재테크");
  }

  @Test
  void getPostsAsyncByThread() {
  }
}