// Copyright Red Energy Limited 2017

package simplenem12;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.util.Collection;

public class SimpleNem12ParserImplTest {

    private File simpleNem12File;
    private SimpleNem12Parser simpleNem12Parser;

    @Before
    public void runBeforeEveryTest() {
        simpleNem12Parser = new SimpleNem12ParserImpl();
        ClassLoader classLoader = getClass().getClassLoader();
        simpleNem12File = new File(classLoader.getResource("SimpleNem12.csv").getFile());
    }

    @After
    public void runAfterEveryTest() {
        simpleNem12Parser = null;
    }


    @Test
    public void read6123456789()
    {
        Collection<MeterRead> meterReads = simpleNem12Parser.parseSimpleNem12(simpleNem12File);
        MeterRead read6123456789 = meterReads.stream().filter(mr -> mr.getNmi().equals("6123456789")).findFirst().get();
        assertThat(read6123456789.getTotalVolume().doubleValue()).isEqualTo(-36.84);
    }

    @Test
    public void read6987654321()
    {
        Collection<MeterRead> meterReads = simpleNem12Parser.parseSimpleNem12(simpleNem12File);
        MeterRead read6987654321 = meterReads.stream().filter(mr -> mr.getNmi().equals("6987654321")).findFirst().get();
        assertThat(read6987654321.getTotalVolume().doubleValue()).isEqualTo(14.33);
    }
}
