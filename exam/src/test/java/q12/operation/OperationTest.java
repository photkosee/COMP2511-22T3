package q12.operation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class OperationTest {
    @Nested
    public class PartABasicOperationsTests {
        @Test
        public void testBasic() {
            Operation<String, Integer> p = new Operation<String, Integer>(s -> Integer.parseInt(s));
            assertEquals(2, p.resolve("2"));
        }

        @Nested
        public class CacheTest {
            private int count = 0;
            @Test
            public void testCaching() {
                Operation<String, Integer> p = new Operation<String, Integer>(s -> {
                    count += 1;
                    return Integer.parseInt(s);
                });

                assertFalse(p.hasCachedResult("2"));
                p.resolve("2"); // Resolve the Operation, count should increment
                assertEquals(1, count);
                assertTrue(p.hasCachedResult("2"));

                p.resolve("2"); // Resolve again, result is cached so count does not increment
                assertEquals(1, count);
            }
        }
    }

    @Nested
    public class PartBDecorationOperationsTests {
        @Test
        public void testThen() {
            Operation<Integer, String> p = new Operation<Integer, String>(i -> i.toString());
            Operation<Integer, Integer> p2 = p.then(s -> Integer.parseInt(s) * 2);

            assertEquals("5", p.resolve(5));
            assertEquals(10, p2.resolve(5));
        }

        @Test
        public void testThenChain() {
            Operation<Integer, Integer> p = new Operation<Integer, Integer>(i -> i * 2)
                .then(i -> i + 3)
                .then(i -> i - 4)
                .then(i -> Math.floorDiv(i, 5));
            
            assertEquals(3, p.resolve(10)); // (((10 * 2) + 3) - 4) // 5 == 3
        }

        @Test
        public void testExcept() {
            Operation<Integer, Integer> p = new Operation<Integer, Integer>(i -> {
                throw new RuntimeException("uh oh!");
            });
            Operation<Integer, Integer> p2 = p.except(() -> -1);

            assertEquals(-1, p2.resolve(1));
        }
    }
    
    @Nested
    public class PartCFactoryAllTests {
        @Test
        public void testAll() {
            Operation<Integer, Integer> p1 = new Operation<Integer, Integer>(i -> i * 2);
            Operation<Integer, Integer> p2 = new Operation<Integer, Integer>(i -> i + 3);
            Operation<Integer, Integer> p3 = new Operation<Integer, Integer>(i -> i - 1);
            
            Operation<List<Integer>, List<Integer>> p4 = OperationFactory.all(List.of(p1, p2, p3));
            assertEquals(List.of(2, 4, 0), p4.resolve(List.of(1, 1, 1))); // [1 * 2 = 2, 1 + 3 = 4, 1 - 1 = 0]
        }

    }

    @Nested
    public class PartDFactoryChainTests {
        @Test
        public void testChain() {
            Operation<Integer, Integer> p1 = new Operation<Integer, Integer>(i -> i * 2);
            Operation<Integer, Integer> p2 = new Operation<Integer, Integer>(i -> i + 3);
            Operation<Integer, Integer> p3 = new Operation<Integer, Integer>(i -> i - 1);
    
            Operation<Integer, Integer> p4 = OperationFactory.chain(List.of(p1, p2, p3));
            assertEquals(4, p4.resolve(1)); // 1 * 2 + 3 - 1 = 4
        }
    }
}