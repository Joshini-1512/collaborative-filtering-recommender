import java.util.*;
public class CollaborativeFilteringRecommender {
    private static Map<String, Map<String, Integer>> userRatings = new HashMap<>();
    static {
        Map<String, Integer> user1Ratings = new HashMap<>();
        user1Ratings.put("Movie A", 5);
        user1Ratings.put("Movie B", 3);
        user1Ratings.put("Movie C", 4);
        userRatings.put("User 1", user1Ratings);
        Map<String, Integer> user2Ratings = new HashMap<>();
        user2Ratings.put("Movie A", 4);
        user2Ratings.put("Movie B", 2);
        user2Ratings.put("Movie D", 5);
        userRatings.put("User 2", user2Ratings);
        Map<String, Integer> user3Ratings = new HashMap<>();
        user3Ratings.put("Movie A", 3);
        user3Ratings.put("Movie B", 5);
        user3Ratings.put("Movie C", 4);
        user3Ratings.put("Movie D", 2);
        userRatings.put("User 3", user3Ratings);
    }
    public static void main(String[] args) {
        List<String> recommendations = recommendMovies("User 1");
        System.out.println("Recommended movies for User 1: " + recommendations);
    }
    private static List<String> recommendMovies(String user) {
        Map<String, Integer> targetUserRatings = userRatings.get(user);
        Map<String, Double> similarityScores = new HashMap<>();
        for (String otherUser : userRatings.keySet()) {
            if (!otherUser.equals(user)) {
                double similarity = calculateSimilarity(targetUserRatings, userRatings.get(otherUser));
                similarityScores.put(otherUser, similarity);
            }
        }
        String mostSimilarUser = Collections.max(similarityScores.entrySet(), Map.Entry.comparingByValue()).getKey();
        List<String> recommendations = new ArrayList<>();
        Map<String, Integer> mostSimilarUserRatings = userRatings.get(mostSimilarUser);

        for (String movie : mostSimilarUserRatings.keySet()) {
            if (!targetUserRatings.containsKey(movie) && mostSimilarUserRatings.get(movie) >= 4) {
                recommendations.add(movie);
            }
        }

        return recommendations;
    }
    private static double calculateSimilarity(Map<String, Integer> ratings1, Map<String, Integer> ratings2) {
        Set<String> commonMovies = new HashSet<>(ratings1.keySet());
        commonMovies.retainAll(ratings2.keySet());

        if (commonMovies.isEmpty()) {
            return 0;
        }

        int dotProduct = 0;
        int norm1 = 0;
        int norm2 = 0;

        for (String movie : commonMovies) {
            int rating1 = ratings1.get(movie);
            int rating2 = ratings2.get(movie);

            dotProduct += rating1 * rating2;
            norm1 += rating1 * rating1;
            norm2 += rating2 * rating2;
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}
