# RMFixer

Fix the packet timestamps of `.tmcpr` [Replay Mod](https://www.replaymod.com/) Recordings.

See also [RMReader](https://github.com/olillin/RMReader).

## What does it do?

- Compares each timestamp to previous timestamp, if timestamp is less than previous set it to previous + 1 
- Ensures that following timestamps keep the same difference to minimize cuts in recording

## Output files

**RMFixer** creates several files in the current directory as its output.
These are outlined below:

| filename           | description                                           |
|--------------------|-------------------------------------------------------|
| `fixed.tmcpr`      | the replay recording with fixed timestamps.           |
| `fixed_timestamps` | binary array of integers of all timestamps after fix. |

## Usage


```bash
# Build (jar file is created in build/libs)
> ./gradlew build

# Run
> java -jar "RMFixer-1.0-SNAPSHOT.jar" "<.tmcpr file path>"
```