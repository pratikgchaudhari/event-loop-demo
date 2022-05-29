package event.loop.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class App {
    public static void main(String[] args) throws IOException {
        EventLoop eventLoop = new EventLoop();

        try (var reader = new BufferedReader(new InputStreamReader(System.in))) {

            var eventId = 0;

            outer: do {
                System.out.println("What kind of task would you like to submit to the Event Loop?");
                System.out.println(" 1. Say Hello");
                System.out.println(" 2. Read the contents of a file named hello.txt");
                System.out.println(" 3. Fetch latest news from New York Times");
                System.out.println(" 4. Exit");
                System.out.print(" > ");

                var usersChoice = reader.readLine();
                var operationType = "1";

                if (userHasNotChosenToExit(usersChoice)) {

                    System.out.println("How would like to execute this operation?");
                    System.out.println(
                            " 1. Synchronously (this would block the Event Loop until the operation completes)");
                    System.out.println(" 2. Asynchronously (this won't block Event Loop in any way)");
                    System.out.print(" > ");

                    operationType = reader.readLine();
                }

                var uniqueEventKey = "";

                switch (usersChoice) {
                    case "1":
                        uniqueEventKey = generateUniqueEventKey("hello", eventId++);
                        eventLoop.on(uniqueEventKey, data -> String.format("Hello! %s", data));
                        eventLoop.dispatch(new Event(uniqueEventKey, "How are you doing today?",
                                isAsynchronous(operationType)));
                        break;
                    case "2":
                        uniqueEventKey = generateUniqueEventKey("read-file", eventId++);
                        eventLoop.on(uniqueEventKey, fileName -> readFile(fileName.toString()));
                        eventLoop.dispatch(
                                new Event(uniqueEventKey,
                                        "/Users/pratik/projects/personal/event-loop-demo/app/bin/main/hello.txt",
                                        isAsynchronous(operationType)));
                        break;
                    case "3":
                        uniqueEventKey = generateUniqueEventKey("fetch-latest-news", eventId++);
                        eventLoop.on(uniqueEventKey, apiKey -> fetchLatestNewsItemFromNewYorkTimes(apiKey));
                        eventLoop.dispatch(new Event(uniqueEventKey,
                                System.getenv("API_KEY"),
                                isAsynchronous(operationType)));
                        break;
                    case "4":
                    default:
                        break outer;
                }

                eventLoop.run();

            } while (true);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static boolean userHasNotChosenToExit(String userChoice) {
        return !userChoice.equals("4");
    }

    private static boolean isAsynchronous(String operationType) {
        return operationType.equals("2") ? true : false;
    }

    private static String readFile(String fileName) {
        var lines = new StringBuffer();
        try (Scanner file = new Scanner(new File(fileName))) {

            while (file.hasNextLine()) {
                lines.append(file.nextLine()).append(" ");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return lines.toString();
    }

    private static String fetchLatestNewsItemFromNewYorkTimes(String apiKey) {
        var latestNews = new StringBuffer();
        var httpClient = new OkHttpClient();

        var request = new Request.Builder()
                .url(String.format("https://api.nytimes.com/svc/topstories/v2/technology.json?api-key=%s", apiKey))
                .build();

        try {
            var response = httpClient.newCall(request).execute();

            NewsItems newsItems = new ObjectMapper().readValue(response.body().byteStream(), NewsItems.class);

            if (newsItemsAreAvailable(newsItems)) {
                latestNews.append(
                        String.format("%s - %s", newsItems.results.get(0).title, newsItems.results.get(0).byLine));
            }

        } catch (IOException e) {
            latestNews.append("Failed to get latest news");
            e.printStackTrace();
        }

        return latestNews.toString();
    }

    private static String generateUniqueEventKey(String humanReadableEventKey, int eventCount) {
        return String.format("%s-%s", humanReadableEventKey, eventCount);
    }

    private static boolean newsItemsAreAvailable(NewsItems newsItems) {
        return (newsItems != null && newsItems.results != null && !newsItems.results.isEmpty());
    }
}
