package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private int callsMade = 0;
    private BreedFetcher fetcher;
    private Map<String, List> cacheBreedMap = new HashMap<>();

    public CachingBreedFetcher(BreedFetcher fetcher) {
        // assigns the fetcher
        this.fetcher = fetcher;

    }

    @Override
    public List<String> getSubBreeds(String breed) {
        // checks if the map has the breed already
        if (cacheBreedMap.containsKey(breed)) {
            return cacheBreedMap.get(breed);
        } else {
            // incements the cache call
            callsMade++;
            // gets the breed list
            List<String> subBreeds = fetcher.getSubBreeds(breed);
            // puts it in the map
            cacheBreedMap.put(breed, subBreeds);
            // returns the list
            return subBreeds;
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}