package easy.tuto.myquizapplication;

import com.google.firebase.database.DatabaseReference;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;


public class DestinationGenerator {

    public static String getBestDestination(String[] userAnswers) {

        if (userAnswers == null) {
            return "Error: userAnswers array is null";
        }

        // Load destinations from JSON file
        JSONObject destinations = loadDestinationsFromJSON();


        // Check if destinations is null
        if (destinations == null) {
            return "Error: Could not load destinations from JSON file";
        }

        JSONArray countries = (JSONArray) destinations.get("countries");

        // Calculate sum of user's answers
        int userSum = 0;
        for (String answer : userAnswers) {
            userSum += Arrays.asList(QuestionAnswer.choices[0]).indexOf(answer);
        }

        // Calculate score for each country based on user's answers
        int[] scores = new int[countries.size()];

        for (int i = 0; i < countries.size(); i++) {
            JSONObject country = (JSONObject) countries.get(i);
            JSONObject countryScore = (JSONObject) country.get("score");

            int locationScore = getAnswerScore((long) countryScore.get("location"), userAnswers[0]);
            int weatherScore = getAnswerScore((long) countryScore.get("weather"), userAnswers[1]);
            int beachScore = getAnswerScore((long) countryScore.get("beach"), userAnswers[2]);
            int budgetScore = getAnswerScore((long) countryScore.get("budget"), userAnswers[3]);
            int transportationScore = getAnswerScore((long) countryScore.get("transportation"), userAnswers[4]);
            int accommodationScore = getAnswerScore((long) countryScore.get("accommodation"), userAnswers[5]);
            int durationScore = getAnswerScore((long) countryScore.get("duration"), userAnswers[6]);
            int foodScore = getAnswerScore((long) countryScore.get("food"), userAnswers[7]);
            int cultureScore = getAnswerScore((long) countryScore.get("culture"), userAnswers[8]);
            int outdoorActivitiesScore = getAnswerScore((long) countryScore.get("outdoor activities"), userAnswers[9]);

            scores[i] = locationScore + weatherScore + beachScore + budgetScore + transportationScore +
                    accommodationScore + durationScore + foodScore + cultureScore + outdoorActivitiesScore;
        }

        // Get the index of the country with a score closest to the user's sum
        int closestIndex = 0;
        int closestDiff = Math.abs(userSum - scores[0]);

        for (int i = 1; i < scores.length; i++) {
            int diff = Math.abs(userSum - scores[i]);
            if (diff < closestDiff) {
                closestIndex = i;
                closestDiff = diff;
            }
        }

        // Return name of country with score closest to user's sum
        JSONObject bestCountry = (JSONObject) countries.get(closestIndex);
        return (String) bestCountry.get("name");
    }
    private static JSONObject loadDestinationsFromJSON() {
        String jsonString = "{\"countries\":[{\"name\":\"Morocco\",\"score\":{\"location\":8,\"weather\":9,\"beach\":6,\"budget\":8,\"transportation\":7,\"accommodation\":8,\"duration\":9,\"food\":9,\"culture\":9,\"outdoor activities\":7}},{\"name\":\"Canada\",\"score\":{\"location\":9,\"weather\":7,\"beach\":4,\"budget\":6,\"transportation\":8,\"accommodation\":9,\"duration\":10,\"food\":7,\"culture\":8,\"outdoor activities\":10}},{\"name\":\"Belgium\",\"score\":{\"location\":7,\"weather\":6,\"beach\":2,\"budget\":7,\"transportation\":8,\"accommodation\":7,\"duration\":7,\"food\":10,\"culture\":10,\"outdoor activities\":5}},{\"name\":\"Philippines\",\"score\":{\"location\":10,\"weather\":8,\"beach\":9,\"budget\":9,\"transportation\":6,\"accommodation\":8,\"duration\":8,\"food\":8,\"culture\":7,\"outdoor activities\":9}}]}";

        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(jsonString);

            return (JSONObject) obj;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }



    private static int getAnswerScore(long score, String userAnswer) {
        return (int) (score * Arrays.asList(QuestionAnswer.choices[0]).indexOf(userAnswer));
    }
}
