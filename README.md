# log-event-processor
Parses and persists log event files into database

## How to run
* Clone the repo: https://github.com/ollyhayden/log-event-processor.git
* Navigate to the log-event-processor directory and run:
```
./gradlew bootRun -Pargs=<path to event log file>
```
## Run tests
* Run tests using
```
./gradlew test
```
* Test report is also viewable at **build/reports/tests/test/index.html**

## Demos
* To test using sample files, try the following:
```
./gradlew bootRun -Pargs=src/test/resources/sample-small.log
```

## Test runs
* Running with 100,000 event pairs (17.2MB) yielded following results for 5 runs:
```
Using impl StreamingGroupByLogEventProcessor
- Processing took 19.59secs for 100,000 events
- Processing took 19.46secs for 100,000 events
- Processing took 20.72secs for 100,000 events
- Processing took 17.86secs for 100,000 events
- Processing took 19.48secs for 100,000 events
```
```
Using impl HashMapCacheLogEventProcessor (should be more memory efficient)
- Processing took 21.12secs for 100,000 events
- Processing took 20.64secs for 100,000 events
- Processing took 21.66secs for 100,000 events
- Processing took 19.76secs for 100,000 events
- Processing took 18.98secs for 100,000 events
```
```
For reference, CountLogEventLineProcessor converts to LogEventLine and counts
- Processing took 1.15secs
- Processing took 0.77secs
- Processing took 0.68secs
```

## Observations
* The HSQLDB database looks to be a bottleneck as N/2 events are written to it. Maybe we only care about alerted events and just those should be written?
* Reading from the single file input means we can really just process is a single stream. Potentially this could be broken and processed in distributed/parallel using Apache Hadoop or equivalent.
* For production grade approach, we should consider injesting events using something like Kafka or Azure Event Hubs and reporting off them