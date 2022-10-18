# ErgaSales
## _A service oriented sales management system with Swing UI_

A service oriented sales management system where the price of the sale is not dependent on the price of the products but the overall service.
An example would be an electronics technician that replaces some capacitors and resistors in a power supply. The price of the sale is the price that the technician will charge for the service, not the price of the products/consumables.

## Features

- Statistics for each customer (sales, revenue, dates)
- Statistics for each product (sales, quantities)
- Export and import saves to XML files
- Summaries in TXT and XML

## Installation

- Requires JRE Version 8 (Java 8) to run the application
- Requires JDK Version 1.8 (Java 8) to develop

- To run, download a jar from the releases and run it in any folder.
- To develop, import the repo in your IDE as Gradle project. In Eclipse you must first set JDK 8 as default gradle java home:
```
Window -> Preferences -> Gradle -> Advanced Options -> Java Home
```
and select the folder where JDK 8 is installed on your computer (you may need to do this on other IDEs too).


## Libraries

Libraries used in this project, their use case and their license

| Library | Usage | License |
| ------ | ------ | ------ |
| [LGoodDatePicker][lgooddatepicker] | Datepicker in Sale creation/edit | MIT |
| [ThreeTen-JAXB][threeten-jaxb] | XML Adapter for LocalDate | Apache 2.0 |
| [Apache Commons Text][commons-text] | Text handling for XML Export | Apache 2.0 |
| [Spotless][spotless] with [google-java-format][google-java-format] | Gradle plugin  (task) for code formatting | Apache 2.0 |

## License

Apache 2.0


   [lgooddatepicker]: https://github.com/LGoodDatePicker/LGoodDatePicker
   [threeten-jaxb]: https://github.com/threeten-jaxb/threeten-jaxb
   [commons-text]: https://github.com/apache/commons-text
   [spotless]: https://github.com/diffplug/spotless
   [google-java-format]: https://github.com/google/google-java-format
   [mit]: https://www.mit.edu/~amini/LICENSE.md
   [apache2]: https://www.apache.org/licenses/LICENSE-2.0.txt
