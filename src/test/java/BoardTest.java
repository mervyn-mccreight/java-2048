import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BoardTest {

    @Test
    public void emptyGrid_allFieldsAreEmpty() throws Exception {
        int dimension = 4;
        Board empty = Board.createEmpty(dimension);

        List<Object> empties = Arrays.asList(Spot.EMPTY, Spot.EMPTY, Spot.EMPTY, Spot.EMPTY);

        assertThat(empty.row(0), is(empties));
        assertThat(empty.row(1), is(empties));
        assertThat(empty.row(2), is(empties));
        assertThat(empty.row(3), is(empties));

        assertThat(empty.column(0), is(empties));
        assertThat(empty.column(1), is(empties));
        assertThat(empty.column(2), is(empties));
        assertThat(empty.column(3), is(empties));
    }
}
