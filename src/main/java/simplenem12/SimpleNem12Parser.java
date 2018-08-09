// Copyright Red Energy Limited 2017

package simplenem12;

import java.io.File;
import java.util.Collection;

public interface SimpleNem12Parser {

  /**
   * Parses Simple NEM12 file.
   * 
   * @param simpleNem12File file in Simple NEM12 format
   * @return Collection of <code>MeterRead</code> that represents the data in the given file.
   */
  Collection<MeterRead> parseSimpleNem12(File simpleNem12File);

}
