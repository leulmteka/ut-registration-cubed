package com.utreg.professor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProfessorService {
    public static final String UTDSchoolID = "U2Nob29sLTEyNzM=";
    public static final String RateMyProfessorSearchURL = "https://www.ratemyprofessors.com/search/professors/%s?q=%s";
    public static final String RateMyProfessorsGraphQLURL = "https://www.ratemyprofessors.com/graphql";

    public String getProfessorID(String professorName, String schoolID) throws IOException {
        String urlString = String.format(RateMyProfessorSearchURL, schoolID, URLEncoder.encode(professorName, StandardCharsets.UTF_8));
        URL url = new URL(urlString);
        HttpURLConnection conn = openConnection(url, "GET", null);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String body = reader.lines().reduce("", (acc, line) -> acc + line);
            Pattern pattern = Pattern.compile("\"legacyId\":(\\d+)");
            Matcher matcher = pattern.matcher(body);
            if (matcher.find()) {
                return matcher.group(1);
            }
            throw new IOException("Professor not found");
        } finally {
            conn.disconnect();
        }
    }

    public Professor getRMPInfo(String professorName) throws IOException, JSONException {
        String professorID = getProfessorID(professorName, UTDSchoolID);
        Map<String, String> headers = getHeaderQuery(professorID);
        String professorQuery = getProfessorQuery(professorID);
        String data = postData(RateMyProfessorsGraphQLURL, professorQuery, headers);

        JSONObject jsonObj = new JSONObject(data);
        JSONObject node = jsonObj.getJSONObject("data").getJSONObject("node");

        Professor professor = new Professor();
        professor.setID(node.optString("id"));
        String firstName = node.optString("firstName", "");
        String lastName = node.optString("lastName", "");
        professor.setName(firstName + " " + lastName);
        professor.setDepartment(node.optString("department"));
        professor.setDifficulty((float) node.optDouble("avgDifficulty", 0.0));
        professor.setRating((float) node.optDouble("avgRating", 0.0));

        // Check if wouldTakeAgainPercent is present and not null
        if (!node.isNull("wouldTakeAgainPercent")) {
            double wouldTakeAgainPercent = node.optDouble("wouldTakeAgainPercent", -1);
            professor.setWouldTakeAgain((int) Math.round(wouldTakeAgainPercent));
        } else {
            professor.setWouldTakeAgain(-1); // Default or error value, since it's not found
        }

        // Tags might not be present or could be an empty array
        List<String> tags = new LinkedList<>();
        if (node.has("tags") && !node.isNull("tags")) {
            JSONArray tagsArray = node.getJSONArray("tags");
            for (int i = 0; i < tagsArray.length(); i++) {
                tags.add(tagsArray.getString(i));
            }
        }
        professor.setTags(tags);

        return professor;
    }

    // Represents a method to POST data and return a response as String
    public String postData(String urlAddress, String jsonInputString, Map<String, String> requestProperties) throws IOException {
        URL url = new URL(urlAddress);
        HttpURLConnection conn = openConnection(url, "POST", requestProperties);
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        } finally {
            conn.disconnect();
        }
        return response.toString();
    }

    private HttpURLConnection openConnection(URL url, String method, Map<String, String> requestProperties) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        if (requestProperties != null) {
            for (Map.Entry<String, String> property : requestProperties.entrySet()) {
                conn.setRequestProperty(property.getKey(), property.getValue());
            }
        }
        if ("POST".equals(method)) {
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
        }
        return conn;
    }

    // Example method to construct JSON for a GraphQL query (simplified)
    public String getProfessorQuery(String professorID) throws JSONException {
        JSONObject variables = new JSONObject()
                .put("id", Base64.getEncoder().encodeToString(String.format("Teacher-%s", professorID).getBytes(StandardCharsets.UTF_8)));

        String query = "query RatingsListQuery($id: ID!) {node(id: $id) {... on Teacher {school {id} courseCodes {courseName courseCount} firstName lastName numRatings avgDifficulty avgRating department wouldTakeAgainPercent}}}";

        return new JSONObject()
                .put("query", query)
                .put("variables", variables)
                .toString();
    }

    public Map<String, String> getHeaderQuery(String professorID) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic dGVzdDp0ZXN0"); // Should be replaced with actual credentials
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
        headers.put("Content-Type", "application/json");
        headers.put("Referer", String.format("https://www.ratemyprofessors.com/ShowRatings.jsp?tid=%s", professorID));
        return headers;
    }

    public String getRatingsQuery(String professorID, int numRatings) throws JSONException {
        JSONObject variables = new JSONObject()
                .put("id", Base64.getEncoder().encodeToString(String.format("Teacher-%s", professorID).getBytes(StandardCharsets.UTF_8)))
                .put("count", numRatings);
        // Add additional parameters like `courseFilter` and `cursor` if needed, as per your Go code

        String query = "query RatingsListQuery($count: Int! $id: ID! $courseFilter: String $cursor: String) {node(id: $id) {... on Teacher {ratings(first: $count, after: $cursor, courseFilter: $courseFilter) {edges {node {ratingTags}}}}}}";

        return new JSONObject()
                .put("query", query)
                .put("variables", variables)
                .toString();
    }
}
