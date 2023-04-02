package easy.tuto.myquizapplication;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class DestinationGenerator {

    public static String getBestDestination(String[] userAnswers) {

        // Load destinations from JSON file
        JSONObject destinations = loadDestinationsFromJSON();

        // Check if destinations is null
        if (destinations == null) {
            return "Error: Could not load destinations from JSON file";
        }

        JSONArray countries = (JSONArray) destinations.get("countries");

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

        // Get index of highest score
        int maxIndex = 0;

        for (int i = 1; i < scores.length; i++) {
            if (scores[i] > scores[maxIndex]) {
                maxIndex = i;
            }
        }

        // Return name of country with highest score
        JSONObject bestCountry = (JSONObject) countries.get(maxIndex);
        return (String) bestCountry.get("name");
    }


    private static JSONObject loadDestinationsFromJSON() {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("Destinations.json"));

            return (JSONObject) obj;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }


    private static int getAnswerScore(long score, String userAnswer) {
        return (int) (score * Arrays.asList(QuestionAnswer.choices[0]).indexOf(userAnswer));
    }
}
