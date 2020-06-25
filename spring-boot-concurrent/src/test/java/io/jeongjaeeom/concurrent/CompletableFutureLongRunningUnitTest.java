package io.jeongjaeeom.concurrent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class CompletableFutureLongRunningUnitTest {

  @Test
  public void test() {
    int totalAmount = 727000;

    for (int i = 5000; i <= 10000; i++) {
      if (totalAmount % i == 0) {
        System.out.println(i);

      }
    }

    System.out.println(727000/5816);
    System.out.println(727000/7270);

  }

  @Test
  public void whenRunningCompletableFutureAsynchronously_thenGetMethodWaitsForResult()
      throws InterruptedException, ExecutionException {
    Future<String> completableFuture = calculateAsync();

    String result = completableFuture.get();
    assertEquals("Hello", result);
  }

  @Test
  public void whenRunningCompletableFutureAsynchronously_then()
      throws InterruptedException, ExecutionException {
    CompletableFuture<String> completableFuture = calculateAsync();
    CompletableFuture<Void> future = completableFuture
        .thenAccept(s -> log.info("s: {}", s));

    future.get();
  }

  private CompletableFuture<String> calculateAsync() throws InterruptedException {
    CompletableFuture<String> completableFuture = new CompletableFuture<>();

    Executors.newCachedThreadPool()
        .submit(() -> {
          Thread.sleep(5000);
          completableFuture.complete("Hello");
          return null;
        });

    return completableFuture;
  }

  @Test
  public void whenRunningCompletableFutureWithResult_thenGetMethodReturnsImmediately()
      throws InterruptedException, ExecutionException {
    Future<String> completableFuture = CompletableFuture.completedFuture("Hello");

    String result = completableFuture.get();
    assertEquals("Hello", result);
  }

  private Future<String> calculateAsyncWithCancellation() throws InterruptedException {
    CompletableFuture<String> completableFuture = new CompletableFuture<>();

    Executors.newCachedThreadPool()
        .submit(() -> {
          Thread.sleep(500);
          completableFuture.cancel(false);
          return null;
        });

    return completableFuture;
  }

  @Test
  public void whenCancelingTheFuture_thenThrowsCancellationException()
      throws ExecutionException, InterruptedException {
    Future<String> future = calculateAsyncWithCancellation();
    Assertions.assertThrows(NumberFormatException.class, () -> future.get());
  }

  @Test
  public void whenCreatingCompletableFutureWithSupplyAsync_thenFutureReturnsValue()
      throws ExecutionException, InterruptedException {
    CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello");

    assertEquals("Hello", future.get());
  }

  @Test
  public void whenAddingThenAcceptToFuture_thenFunctionExecutesAfterComputationIsFinished()
      throws ExecutionException, InterruptedException {
    CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");

    CompletableFuture<Void> future = completableFuture
        .thenAccept(s -> log.debug("Computation returned: " + s));

    future.get();
  }

  @Test
  public void whenAddingThenRunToFuture_thenFunctionExecutesAfterComputationIsFinished()
      throws ExecutionException, InterruptedException {
    CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");

    CompletableFuture<Void> future = completableFuture
        .thenRun(() -> log.debug("Computation finished."));

    future.get();
  }

  @Test
  public void whenAddingThenApplyToFuture_thenFunctionExecutesAfterComputationIsFinished()
      throws ExecutionException, InterruptedException {
    CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");

    CompletableFuture<String> future = completableFuture.thenApply(s -> s + " World");

    assertEquals("Hello World", future.get());
  }

  @Test
  public void whenUsingThenCompose_thenFuturesExecuteSequentially()
      throws ExecutionException, InterruptedException {
    CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello")
        .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " World"));

    assertEquals("Hello World", completableFuture.get());
  }

  @Test
  public void whenUsingThenCombine_thenWaitForExecutionOfBothFutures()
      throws ExecutionException, InterruptedException {
    CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello")
        .thenCombine(CompletableFuture.supplyAsync(() -> " World"), (s1, s2) -> s1 + s2)
        .thenApply(s -> {
          log.info("s: {}", s);
          return s;
        });

    assertEquals("Hello World", completableFuture.get());
  }

  @Test
  public void whenUsingThenAcceptBoth_thenWaitForExecutionOfBothFutures()
      throws ExecutionException, InterruptedException {
    CompletableFuture.supplyAsync(() -> "Hello")
        .thenAcceptBoth(CompletableFuture.supplyAsync(() -> " World"),
            (s1, s2) -> log.debug(s1 + s2));
  }

  @Test
  public void whenFutureCombinedWithAllOfCompletes_thenAllFuturesAreDone()
      throws ExecutionException, InterruptedException {
    CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
    CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "Beautiful");
    CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> "World");

    CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(future1, future2, future3);

    // ...

    combinedFuture.get();

    assertTrue(future1.isDone());
    assertTrue(future2.isDone());
    assertTrue(future3.isDone());

    String combined = Stream.of(future1, future2, future3)
        .map(CompletableFuture::join)
        .collect(Collectors.joining(" "));

    List<String> collect = Stream.of(future1, future2, future3)
        .map(CompletableFuture::join)
        .collect(Collectors.toList());

    assertEquals(3, collect.size());
    assertEquals("Hello Beautiful World", combined);
  }

  @Test
  public void whenFutureThrows_thenHandleMethodReceivesException()
      throws ExecutionException, InterruptedException {
    String name = null;

    // ...

    CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
      if (name == null) {
        throw new RuntimeException("Computation error!");
      }
      return "Hello, " + name;
    })
        .handle((s, t) -> s != null ? s : "Hello, Stranger!");

    assertEquals("Hello, Stranger!", completableFuture.get());
  }

  @Test
  public void whenCompletingFutureExceptionally_thenGetMethodThrows()
      throws ExecutionException, InterruptedException {
    CompletableFuture<String> completableFuture = new CompletableFuture<>();

    // ...

    completableFuture.completeExceptionally(new RuntimeException("Calculation failed!"));

    // ...
    Assertions.assertThrows(NumberFormatException.class, () -> completableFuture.get());
  }

  @Test
  public void whenAddingThenApplyAsyncToFuture_thenFunctionExecutesAfterComputationIsFinished()
      throws ExecutionException, InterruptedException {
    CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");

    CompletableFuture<String> future = completableFuture.thenApplyAsync(s -> s + " World");

    assertEquals("Hello World", future.get());
  }

  @Test
  public void whenPassingTransformation_thenFunctionExecutionWithThenApply()
      throws InterruptedException, ExecutionException {
    CompletableFuture<Integer> finalResult = compute().thenApply(s -> s + 1);
    assertTrue(finalResult.get() == 11);
  }

  @Test
  public void whenPassingPreviousStage_thenFunctionExecutionWithThenCompose()
      throws InterruptedException, ExecutionException {
    CompletableFuture<Integer> finalResult = compute().thenCompose(this::computeAnother);
    assertTrue(finalResult.get() == 20);
  }

  public CompletableFuture<Integer> compute() {
    return CompletableFuture.supplyAsync(() -> 10);
  }

  public CompletableFuture<Integer> computeAnother(Integer i) {
    return CompletableFuture.supplyAsync(() -> 10 + i);
  }

}
