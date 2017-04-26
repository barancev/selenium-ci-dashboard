package ru.stqa.heroku.selenium;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

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
    assertThat(2*2, is(5));
  }

}
