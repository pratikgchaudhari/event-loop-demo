package event.loop.demo;

import utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {
    public static void main(String[] args) throws IOException {
        EventLoop eventLoop = new EventLoop();

        var utils = new Utils();

        try (var reader = new BufferedReader(new InputStreamReader(System.in))) {

            var eventId = 0;

            outer:
            do {
                System.out.println("What kind of task would you like to submit to the Event Loop?");
                System.out.println(" 1. Say Hello");
                System.out.println(" 2. Read the contents of a file named hello.txt");
                System.out.println(" 3. Fetch latest news from New York Times");
                System.out.println(" 4. Exit");
                System.out.print(" > ");

                var usersChoice = reader.readLine();
                var operationType = "1";

                if (utils.userHasNotChosenToExit(usersChoice)) {

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
                        uniqueEventKey = utils.generateUniqueEventKey("hello", eventId++);
                        eventLoop.on(uniqueEventKey, data -> String.format("Hello! %s", data));
                        eventLoop.dispatch(new Event(uniqueEventKey, "How are you doing today?",
                                utils.isAsynchronous(operationType)));
                        break;
                    case "2":
                        uniqueEventKey = utils.generateUniqueEventKey("read-file", eventId++);
                        eventLoop.on(uniqueEventKey, utils::readFile);
                        eventLoop.dispatch(
                                new Event(uniqueEventKey,
                                        "hello.txt",
                                        utils.isAsynchronous(operationType)));
                        break;
                    case "3":
                        uniqueEventKey = utils.generateUniqueEventKey("fetch-latest-news", eventId++);
                        eventLoop.on(uniqueEventKey, utils::fetchLatestNewsItemFromNewYorkTimes);
                        eventLoop.dispatch(new Event(uniqueEventKey,
                                System.getenv("API_KEY"),
                                utils.isAsynchronous(operationType)));
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
}
