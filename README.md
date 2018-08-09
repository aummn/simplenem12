Meter Read Record Parser
===========================================

Description
-----------

The meter read record parser is used to parse the meter read records in NEM12 format. 
The following is the format description:

### SimpleNem12.csv format specifications
* You can assume, for this exercise, that no quotes or extraneous commas appear in the CSV data.
* First item on every line is the RecordType
* RecordType 100 must be the first line in the file
* RecordType 900 must be the last line in the file
* RecordType 200 represents the start of a meter read block.  This record has the following subsequent items (after RecordType).
You can assume each file does not contain more than one RecordType 200 with the same NMI.
  * NMI - String ID representing the meter on the site, modelled in `MeterRead.nmi`.  Value should always be 10 chars long. 
  * EnergyUnit - Energy unit of the meter reads, modelled in `EnergyUnit` enum type and `MeterRead.energyUnit`.  Value should always be KWH.
* RecordType 300 represents the volume of the meter read for a particular date.  This record has the following subsequent items (after RecordType).
You can assume each file does not contain more than one RecordType 300 with the same date.
  * Date - In the format yyyyMMdd (e.g. 20170102 is 2nd Jan, 2017).  Modelled in `MeterRead.volumes` map key.
  * Volume - Signed number value.  Modelled in `MeterVolume.volume`.
  * Quality - Represents quality of the meter read, whether it's Actual (A) or Estimate (E).  Value should always be A or E.  Modelled in `MeterVolume.quality`
      

Project Structure
-----------------------------------------------

The project structure is created with Maven and follow the conventions. 

   
simplenem12 (project root directory)

    -- src   (source code)
       -- java
          -- simplenem12  (package name)
          
             -- RecordType.java                (the newly added record type model)
             -- SimpleNem12ParserImpl.java     (the parser implementation class)
             -- TestHarness.java               (the simple parser test tool)
             
    -- test  (unit tests)
    
       -- java
          -- simplenem12
          
             -- SimpleNem12ParserImplTest.java (parser test)
          
       -- resources
          -- SimpleNem12.csv                   (test data for meter reads)
          
    pom.xml           (the Maven pom file)
    
    README.md         (the project document file)
          


System Environment
-----------------------------------------------
* Java version: 1.8.0_172, vendor: Oracle Corporation
* IntelliJ IDEA 2018.1.6 (Ultimate Edition)
* Git version 2.18.0.windows.1
* Apache Maven 3.5.0
* OS name: "windows 7", version: "6.1", arch: "amd64", family: "windows"


Building
--------

To build, enter the project directory, for example "/d/dev/Code/simplenem12" and type the following command:

    mvn clean package

This command would trigger the maven build process and run unit tests.

To build without running the unit test, enter the project directory, for example "/d/dev/Code/simplenem12" and
 type the following commands:

    mvn clean package -Dmaven.test.skip=true


Running Test Harness from the command line
-------------------------------------------

To run the test harness, please use the following option to run the it.

Option 1:
Build the project, then enter the project build class directory, for example "/d/dev/Code/simplenem12/target/classes", 
type the following command, 

    java simplenem12.TestHarness  ./SimpleNem12.csv

for example:
    
    Administrator@AUMMN MINGW64 /d/dev/Code/simplenem12/target/classes
    $ java simplenem12.TestHarness  ./SimpleNem12.csv
    Total volume for NMI 6123456789 is -36.840000
    Total volume for NMI 6987654321 is 14.330000

This would run the test harness against the test data file "SimpleNem12.csv" to verify the parser function.


Version Control
-------
Git is used for version control system.



Contact
-------
If need more information, please contact me at  canglangke001@gmail.com




