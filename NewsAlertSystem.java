import java.util.*;

// Subscriber interface
interface Subscriber {
    void notify(String category, String message);
}

// Concrete Subscriber
class User implements Subscriber {
    private String name;

    public User(String name) {
        this.name = name;
    }

    @Override
    public void notify(String category, String message) {
        System.out.println("Notification for " + name + " [" + category + "]: " + message);
    }
}

// Broker / Event Manager (acts as message hub)
class NewsBroker {
    private Map<String, List<Subscriber>> subscribers = new HashMap<>();

    // Subscribe user to category
    public void subscribe(String category, Subscriber subscriber) {
        subscribers
            .computeIfAbsent(category, k -> new ArrayList<>())
            .add(subscriber);
        System.out.println("Subscribed to " + category);
    }

    // Unsubscribe
    public void unsubscribe(String category, Subscriber subscriber) {
        if (subscribers.containsKey(category)) {
            subscribers.get(category).remove(subscriber);
        }
    }

    // Publish message
    public void publish(String category, String message) {
        System.out.println("\nPublisher posted [" + category + "]: " + message);

        if (subscribers.containsKey(category)) {
            for (Subscriber s : subscribers.get(category)) {
                s.notify(category, message);
            }
        }
    }
}

// Publisher
class Publisher {
    private NewsBroker broker;

    public Publisher(NewsBroker broker) {
        this.broker = broker;
    }

    public void publishNews(String category, String message) {
        broker.publish(category, message);
    }
}

// Main class (Demo)
public class NewsAlertSystem {
    public static void main(String[] args) {

        NewsBroker broker = new NewsBroker();

        // Create users (subscribers)
        User u1 = new User("Alice");
        User u2 = new User("Bob");
        User u3 = new User("Charlie");

        // Subscribe users to categories
        broker.subscribe("sports", u1);
        broker.subscribe("technology", u1);

        broker.subscribe("sports", u2);
        broker.subscribe("politics", u2);

        broker.subscribe("technology", u3);

        // Create publisher
        Publisher publisher = new Publisher(broker);

        // Publishers send updates
        publisher.publishNews("sports", "India wins the match!");
        publisher.publishNews("technology", "New AI model released.");
        publisher.publishNews("politics", "Election results announced.");
    }
}