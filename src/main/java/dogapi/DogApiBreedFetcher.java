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
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        final Request request = new Request.Builder()
                .url("https://dog.ceo/api/breed/" + breed + "/list").build();

        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responsebody = new JSONObject(response.body().string());

            if(responsebody.getString("status").equals("success")) {
                final JSONArray breeds = responsebody.getJSONArray("message");
                final List<String> breedsList = new ArrayList<>();
                for(int i = 0; i < breeds.length(); i++){
                    breedsList.add(breeds.getString(i));
                }

                return breedsList;
            } else {
                throw new BreedNotFoundException(responsebody.getString("message"));
            }
        } catch(IOException | JSONException e) {
            throw new BreedNotFoundException(e.getMessage());
        }
    }
}