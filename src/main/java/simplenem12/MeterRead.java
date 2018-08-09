// Copyright Red Energy Limited 2017

package simplenem12;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Represents a meter read, corresponds to RecordType 200 in SimpleNem12
 *
 * The volumes property is a map that holds the date and associated <code>MeterVolume</code> on that date, values come from RecordType 300.
 */
public class MeterRead {

  private String nmi;
  private EnergyUnit energyUnit;
  private SortedMap<LocalDate, MeterVolume> volumes = new TreeMap<>();

  public MeterRead(String nmi, EnergyUnit energyUnit) {
    this.nmi = nmi;
    this.energyUnit = energyUnit;
  }

  public String getNmi() {
    return nmi;
  }

  public void setNmi(String nmi) {
    this.nmi = nmi;
  }

  public EnergyUnit getEnergyUnit() {
    return energyUnit;
  }

  public void setEnergyUnit(EnergyUnit energyUnit) {
    this.energyUnit = energyUnit;
  }

  public SortedMap<LocalDate, MeterVolume> getVolumes() {
    return volumes;
  }

  public void setVolumes(SortedMap<LocalDate, MeterVolume> volumes) {
    this.volumes = volumes;
  }

  MeterVolume getMeterVolume(LocalDate localDate) {
    return volumes.get(localDate);
  }

  public void appendVolume(LocalDate localDate, MeterVolume meterVolume) {
    volumes.put(localDate, meterVolume);
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MeterRead meterRead = (MeterRead) o;
    return Objects.equals(getNmi(), meterRead.getNmi());
  }

  public int hashCode() {
    return Objects.hash(getNmi());
  }

  BigDecimal getTotalVolume() {
    return volumes.values().stream()
      .map(mr -> mr.getVolume())
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
