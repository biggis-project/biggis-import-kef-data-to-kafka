# biggis-import-kef-data-to-kafka
Kafka Producer of data for the stream processing of vector data

This repository contains a Kafka producer for the stream processing prototype in [https://github.com/DisyInformationssysteme/biggis-streaming-vector-data](https://github.com/DisyInformationssysteme/biggis-streaming-vector-data).
It requires the [BigGIS Infrastructure](https://github.com/DisyInformationssysteme/biggis-infrastructure) to be present and running.
Data is read from the JSON files created by the [Http download client](https://github.com/DisyInformationssysteme/biggis-download-kef-data).

The producer is implemented using Apache Camel and Spring Boot.

This work was done for the [BigGIS project](http://biggis-project.eu/) funded by the [German Federal Ministry of Education and Research (BMBF)](https://www.bmbf.de).
