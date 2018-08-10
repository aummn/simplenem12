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
import java.util.stream.Collectors;

/**
 * This is the implementation of the interface SimpleNem12Parser.
 * It is used to parse the meter read record file in Simple NEM12 format.
 *
 * @author zhimeng
 * @version 1.0 10/8/2018
 * @since 1.0
 */
public class SimpleNem12ParserImpl implements SimpleNem12Parser {

  // the number of meter read start record fields
  private static final int METER_READ_START_RECORD_TYPE_FIELD_NUMBER = 3;

  // the number of meter read record fields
  private static final int METER_READ_RECORD_TYPE_FIELD_NUMBER = 4;

  /**
   * Parses Simple NEM12 file.
   * 
   * @param simpleNem12File a file in Simple NEM12 format
   * @return Collection of <code>MeterRead</code> that represents the data in the given file.
   */
  public Collection<MeterRead> parseSimpleNem12(File simpleNem12File) {
      List<MeterRead> meterReadList = new ArrayList<>();
      // return empty list if file does not exist
      if(!Optional.ofNullable(simpleNem12File).isPresent() || !simpleNem12File.exists())
          return meterReadList;

      try {
        BufferedReader br = Files.newBufferedReader(simpleNem12File.toPath());
        List<String> lines = br.lines().collect(Collectors.toList());

        // validate file format validity, start with 100, end with 900
        if(lines.isEmpty() ||
                !lines.get(0).trim().equals(RecordType.METER_READ_START.value()) ||
                !lines.get(lines.size()-1).trim().equals(RecordType.METER_READ_END.value())) {
            return meterReadList;
        }

        // read meter read records
        lines.stream()
                // filter 200, 300 records
                .filter(line -> line.startsWith(RecordType.METER_READ_START_RECORD_TYPE.value()) ||
                        line.startsWith(RecordType.METER_READ_RECORD_TYPE.value()))

                // process 200, 300 records
                .forEach(line -> {
                    // parse meter read start record - 200 record
                    if(line.startsWith(RecordType.METER_READ_START_RECORD_TYPE.value())) {
                        parseMeterRead200Record(line).ifPresent(meterReadList::add);
                    }

                    // parse meter read records - 300 records
                    if(line.startsWith(RecordType.METER_READ_RECORD_TYPE.value()) && (meterReadList.size() >= 1)) {
                        parseMeterRead300Record(meterReadList.get(meterReadList.size()-1), line);
                    }

        });
        br.close();
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
  private Optional<MeterRead> parseMeterRead200Record(String meterRead200Record) {
    if (Optional.ofNullable(meterRead200Record).isPresent()) {
        String[] items = meterRead200Record.split(",");
        if(items.length == METER_READ_START_RECORD_TYPE_FIELD_NUMBER) {
            return Optional.of(new MeterRead(items[1], EnergyUnit.valueOf(items[2])));
        } else {
            return Optional.empty();
        }
    } else {
        return Optional.empty();
    }
  }

  /**
   * Parses the meter read record and attach data to meter read object.
   *
   * @param meterRead the object representing the meter on site
   * @param meterRead300Record the string containing meter read data
   *
   */
  private void parseMeterRead300Record(MeterRead meterRead, String meterRead300Record) {
    if (Optional.ofNullable(meterRead300Record).isPresent() &&
            Optional.ofNullable(meterRead).isPresent()) {

        String[] items = meterRead300Record.split(",");
        if(items.length == METER_READ_RECORD_TYPE_FIELD_NUMBER) {
            MeterVolume meterVolume = new MeterVolume(new BigDecimal(items[2]), Quality.valueOf(items[3]));
            meterRead.getVolumes().put(LocalDate.parse(items[1], DateTimeFormatter.ofPattern("yyyyMMdd")), meterVolume);
        }
    }
  }

}
