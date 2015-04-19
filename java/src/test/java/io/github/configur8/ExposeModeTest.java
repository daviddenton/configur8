package io.github.configur8;

import org.junit.Test;

import static io.github.configur8.ExposeMode.*;
import static io.github.configur8.ExposeMode.Private;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class ExposeModeTest {

    @Test
    public void publicDisplaysValue() throws Exception {
        assertThat(Public.display("hello"), equalTo("hello"));
    }

    @Test
    public void privateMasksValue() throws Exception {
        assertThat(Private.display("hello"), equalTo("*********"));
    }
}
