# MyJUnit

MyJUnit is a simple unit testing framework for Java. I wrote this project to learn how JUnit works.

# Demo

```java
import com.pzy.junit.*;
import com.pzy.junit.runner.JUnitCore;

import static com.pzy.junit.Assertions.*;

public class Main {

    public static void main(String[] args) {
        new JUnitCore().run(Main.class);
    }

    @BeforeClass
    static void beforeClass() {
        System.out.println("beforeClass");
    }

    @AfterClass
    static void afterClass() {
        System.out.println("afterClass");
    }

    @Before
    public void before() {
        System.out.println("before");
    }

    @After
    private void after() {
        System.out.println("after");
    }

    @Test
    static void test() {
        assertEquals(3000, 3000);
    }

    @Test(timeout = 1)
    void test2() {
        assertEquals("Hello World!", Demo.get());
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("all")
    void test3() {
        String a = null;
        a.length();
    }

    @Ignore
    @Test
    void test4() {
        throw new IllegalStateException("This test should be ignored.");
    }

    @Test
    void test5() {
        assertThrows(IllegalStateException.class, () -> {
            throw new IllegalStateException("This lambda should throw an exception.");
        });
    }

    private static class Demo {

        public static String get() {
            return "Hello World!";
        }

    }

}
```
