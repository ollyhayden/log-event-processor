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
* To test using sample files, try the following.
```
./gradlew bootRun -Pargs=src/test/resources/sample-small.log
./gradlew bootRun -Pargs=src/test/resources/sample-100-event-pairs.log
./gradlew bootRun -Pargs=src/test/resources/sample-100000-event-pairs.log
```
* You can generate more test log files using TestDataGenerator in the code base

## Test runs
* Running with 100,000 event pairs (17.2MB) yielded following results for different implementations:
```
StreamingGroupByLogEventProcessor
- Processing took 19.59secs for 100,000 events
- Processing took 19.46secs for 100,000 events
- Processing took 20.72secs for 100,000 events
- Processing took 17.86secs for 100,000 events
- Processing took 19.48secs for 100,000 events
```
```
HashMapCacheLogEventProcessor (default)
- Processing took 21.12secs for 100,000 events
- Processing took 20.64secs for 100,000 events
- Processing took 21.66secs for 100,000 events
- Processing took 19.76secs for 100,000 events
- Processing took 18.98secs for 100,000 events
```
```
ParallelHashMapCacheLogEventProcessor
- Processing took 21.21secs for 100,000 events
- Processing took 21.43secs for 100,000 events
- Processing took 23.26secs for 100,000 events
```
```
OnlyAlertsHashMapCacheLogEventProcessor - just logs alerts (quicker as less to write to DB)
- Processing took 12.87secs for 27,660 events
- Processing took 12.2secs for 27,660 events
- Processing took 13.1secs for 27,660 events
```
```
ParallelOnlyAlertsHashMapCacheLogEventProcessor - same as above but parallel stream improves due not every event needing to be written to DB
- Processing took 10.84secs for 27,660 events
- Processing took 10.78secs for 27,660 events
- Processing took 10.87secs for 27,660 events
```
```
CountLogEventLineProcessor just converts to LogEventLine and counts (no pairing or persistance)
- Processing took 1.15secs
- Processing took 0.77secs
- Processing took 0.68secs
```

## Observations
* The HSQLDB database looks to be a bottleneck as N/2 events are written to it. Maybe we only care about persisting alerted events to reduce the data?
* Reading from the single file input means we can really just process is a single stream. Potentially this could be broken and processed in distributed/parallel using Apache Hadoop or equivalent.
* For production grade approach, we should consider injesting events using something like Kafka or Azure Event Hubs and reporting off them
