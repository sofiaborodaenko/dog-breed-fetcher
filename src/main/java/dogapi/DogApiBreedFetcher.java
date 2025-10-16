package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private static String apiUrl = "https://dog.ceo/api/breed/";
    private final OkHttpClient client = new OkHttpClient();


    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedFetcher.BreedNotFoundException {
        String url = apiUrl + breed + "/list";
        final Request request = new Request.Builder().url(url).build();

        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            // gets the status of the call
            String status = responseBody.getString("status");

            // checks if its successful
            if (status.equals("success")) {
                // gets the array of the breeds
                JSONArray breeds = responseBody.getJSONArray("message");
                List<String> subBreeds = new ArrayList<>();

                // adds the breeds into a list
                for (int i = 0; i < breeds.length(); i++) {
                    subBreeds.add(breeds.getString(i));
                }

                return subBreeds;
            } else {
                throw new BreedFetcher.BreedNotFoundException(breed);
            }

        } catch (IOException | JSONException event) {
            throw new BreedFetcher.BreedNotFoundException(breed);
        }
    }
}