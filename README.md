# log-event-processor
Parses and persists log event files into database

## How to run
* Clone the repo: https://github.com/ollyhayden/log-event-processor.git
* Navigate to the log-event-processor directory and run:
```
./gradlew bootRun -Pargs=<path to event log file>
```

## Demos
* To test using sample files, try the following:
```
./gradlew bootRun -Pargs=src/test/resources/sample-small.log
```
