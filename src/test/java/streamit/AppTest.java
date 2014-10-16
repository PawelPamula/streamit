package streamit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AppTest{

    @Test
    public void testF() {
        assertEquals(App.f(0), 1);
        assertEquals(App.f(1), 1);
        assertEquals(App.f(2), 2);
        assertEquals(App.f(5), 120);
        assertEquals(App.f(10), 3628800);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFException() {
        App.f(-10);
    }
}