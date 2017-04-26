package ru.stqa.heroku.selenium;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class FirstTest {

  @Rule
  public TestRule notification = new NotificationRule();

  @Test
  public void goodTest() {
    assertThat(2*2, is(4));
  }

  @Test
  public void badTest() {
    assertThat(2*2, is(4));
  }

  @Test
  public void collapse() {
    System.out.println("!!!" + Arrays.stream("ru.stqa.heroku.selenium.FirstTest".split("\\."))
      .reduce(new ArrayList<String>(),
        (list, element) -> {
          if (list.size() > 0) list.set(list.size()-1, list.get(list.size()-1).substring(0,1));
          list.add(element);
          return list;
        },
        (l1, l2) -> l1).stream().collect(Collectors.joining(".")));
  }

}
