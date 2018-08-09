package simplenem12;

public enum RecordType {

    METER_READ_START_RECORD_TYPE("200"),
    METER_READ_RECORD_TYPE("300");

    private final String recordType;

    RecordType(String recordType) { this.recordType = recordType; }

    public String value() {
        return this.recordType;
    }

}
