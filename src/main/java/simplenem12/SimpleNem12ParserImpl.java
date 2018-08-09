// Copyright Red Energy Limited 2017

package simplenem12;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

public class SimpleNem12ParserImpl implements SimpleNem12Parser {

  /**
   * Parses Simple NEM12 file.
   * 
   * @param simpleNem12File a file in Simple NEM12 format
   * @return Collection of <code>MeterRead</code> that represents the data in the given file.
   */
  public Collection<MeterRead> parseSimpleNem12(File simpleNem12File) {
      List<MeterRead> meterReadList = new ArrayList<>();
      if(simpleNem12File == null || !simpleNem12File.exists()) return meterReadList;
      try {
        BufferedReader br = Files.newBufferedReader(simpleNem12File.toPath());
        Stream<String> lines = br.lines();
        lines.forEach(line -> {
            // process meter read start record
            if(line.startsWith(RecordType.METER_READ_START_RECORD_TYPE.value()))
                meterReadList.add(processMeterRead200Record(line));
            // process following meter read record
            if(line.startsWith(RecordType.METER_READ_RECORD_TYPE.value()))
                processMeterRead300Record(meterReadList.get(meterReadList.size()-1), line);
        });
        lines.close();
      } catch (IOException io) {
        io.printStackTrace();
      }
      return meterReadList;
  }

  /**
   * Parses the meter read start record.
   *
   * @param meterRead200Record the string containing meter read start data
   * @return a <code>MeterRead</code> that represents the data in the given file.
   */
  private MeterRead processMeterRead200Record(String meterRead200Record) {
    String[] items = meterRead200Record.split(",");
    return new MeterRead(items[1], EnergyUnit.valueOf(items[2]));
  }

  /**
   * Parses the meter read record and attach data to meter read object.
   *
   * @param meterRead the object representing the meter on site
   * @param meterRead300Record the string containing meter read data
   *
   */
  private void processMeterRead300Record(MeterRead meterRead, String meterRead300Record) {
    String[] items = meterRead300Record.split(",");
    MeterVolume meterVolume = new MeterVolume(new BigDecimal(items[2]), Quality.valueOf(items[3]));
    meterRead.getVolumes().put(LocalDate.parse(items[1], DateTimeFormatter.ofPattern("yyyyMMdd")), meterVolume);
  }

}
