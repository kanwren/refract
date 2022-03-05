package com.github.nprindle.refract;

import static org.junit.Assert.assertEquals;

import com.github.nprindle.refract.classes.Semigroup;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.data.Identity;
import org.junit.Test;

public class TestMain {
  @Test
  public void testIdentityFunctorInstance() throws Exception {
    Identity<Integer> i = Identity.of(5);

    int a = i.value();

    Identity<Integer> res = Identity.resolve(Identity.Instances.functor().map(n -> n + 1, i));

    int b = res.value();

    assertEquals("map produces the correct value", b, a + 1);
  }

  @Test
  public void testIdentityApplicativeInstance() throws Exception {
    int a = 3;
    int b = 5;

    Identity<Integer> ia = Identity.of(a);
    Identity<Integer> ib = Identity.of(b);

    Identity<Integer> res =
        Identity.resolve(Identity.Instances.applicative().apply2((x, y) -> x + y, ia, ib));

    int c = res.value();

    assertEquals("apply2 produces the correct value", c, a + b);
  }

  @Test
  public void testTypeclassDefunctionalizationCompilation() throws Exception {
    // should be able to covariantly wildcard and get a usable instsance
    Semigroup<? extends Semigroup.Mu, Integer> s = Semigroup.first();
    // should be able to assign to fully saturated defunctionalization symbol
    A1<? extends Semigroup.Mu, Integer> s2 = s;
    // should be able to resolve a fully saturated defunctionalization symbol
    Semigroup<? extends Semigroup.Mu, Integer> s3 = Semigroup.resolve(s2);

    Integer a = 5;
    Integer b = 3;
    assertEquals("typeclass defunctionalization resolves correctly", a, s3.append(a, b));
  }
}
