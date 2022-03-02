package com.github.nprindle.refract;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestMain {
    @Test
    public void testIdentityFunctorInstance() throws Exception {
        Identity<Integer> i = new Identity<>(5);

        int a = i.value();

        Identity<Integer> res = Identity.unbox(Identity.FunctorI.INSTANCE.map(n -> n + 1, i));

        int b = res.value();

        assertEquals("map produces the correct value", b, a + 1);
    }
}
