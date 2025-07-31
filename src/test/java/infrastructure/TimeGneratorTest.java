package infrastructure;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class TimeGneratorTest {

    @Test
    public void testTimeGnerator() {
        TimeGenerator timeGenerator = new TimeGenerator();
        Object returnValue = timeGenerator.generate();
        assertThat(returnValue, notNullValue());
    }
}
