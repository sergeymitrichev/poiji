package com.poiji.deserialize;

import com.poiji.bind.Poiji;
import com.poiji.deserialize.model.byname.ListHolderByName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static com.poiji.util.Data.unmarshallingListHolders;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public class DeserializersListByNameTest {
    private String path;
    private List<ListHolderByName> expectedListHolders;
    private Class<?> expectedException;

    public DeserializersListByNameTest(String path, List<ListHolderByName> expectedListHolders, Class<?> expectedException) {
        this.path = path;
        this.expectedListHolders = expectedListHolders;
        this.expectedException = expectedException;
    }
    @Parameterized.Parameters(name = "{index}: ({0})={1}")
    public static Iterable<Object[]> queries() {
        return Arrays.asList(new Object[][]{
                {"src/test/resources/list.xls", unmarshallingListHolders(), null},
        });
    }

    @Test
    public void shouldMapExcelToJava() {

        try {
            List<ListHolderByName> actualHolders = Poiji.fromExcel(new File(path), ListHolderByName.class);

            assertThat(actualHolders, notNullValue());
            assertThat(actualHolders.size(), not(0));
            assertThat(actualHolders.size(), is(expectedListHolders.size()));

            ListHolderByName actualHolder1 = actualHolders.get(0);

            ListHolderByName expectedHolder1 = expectedListHolders.get(0);

            assertThat(actualHolder1.toString(), is(expectedHolder1.toString()));

        } catch (Exception e) {
            if (expectedException == null) {
                fail(e.getMessage());
            } else {
                assertThat(e, instanceOf(expectedException));
            }
        }
    }
}
