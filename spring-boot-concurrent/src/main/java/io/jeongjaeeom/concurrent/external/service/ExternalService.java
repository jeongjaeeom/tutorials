package io.jeongjaeeom.concurrent.external.service;

import com.google.common.collect.Lists;
import io.jeongjaeeom.concurrent.posts.domain.Post;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ExternalService {

  private final List<Post> data = Lists.newArrayList(
      Post.builder().category("재테크").title("부동산").content("부동산 내용").build(),
      Post.builder().category("재테크").title("주식").content("주식 내용").build(),
      Post.builder().category("재테크").title("펀드").content("펀드 내용").build(),
      Post.builder().category("재테크").title("목돈 마련").content("목돈 마련 내용").build(),
      Post.builder().category("인문학").title("삼국지").content("삼국지 내용").build(),
      Post.builder().category("인문학").title("세종대왕").content("세종대왕 내용").build(),
      Post.builder().category("책").title("총균쇠").content("총균쇠 내용").build(),
      Post.builder().category("책").title("지적 대화를 위한 넓고 얕은 지식").content("지적 대화를 위한 넓고 얕은 지식 내용").build(),
      Post.builder().category("책").title("자기관리론").content("자기관리론 내용").build()
  );

  public List<Post> getPostsByCategory(final String category) {
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return data.stream()
        .filter(post -> post.getCategory().equals(category))
        .collect(Collectors.toList());
  }

}
