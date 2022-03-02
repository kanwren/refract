package com.github.nprindle.refract;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestMain {
    @Test
    public void testIdentityFunctorInstance() throws Exception {
        Identity<Integer> i = new Identity<>(5);

        int a = i.value();

        Identity<Integer> res = Identity.unbox(Identity.Instances.functor().map(n -> n + 1, i));

        int b = res.value();

        assertEquals("map produces the correct value", b, a + 1);
    }

    @Test
    public void testIdentityApplicativeInstance() throws Exception {
        int a = 3;
        int b = 5;

        Identity<Integer> ia = new Identity<>(a);
        Identity<Integer> ib = new Identity<>(b);

        Identity<Integer> res = Identity.unbox(Identity.Instances.applicative().apply2((x, y) -> x + y, ia, ib));

        int c = res.value();

        assertEquals("apply2 produces the correct value", c, a + b);
    }
}
