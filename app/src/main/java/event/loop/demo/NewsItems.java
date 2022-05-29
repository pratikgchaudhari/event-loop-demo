package event.loop.demo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsItems {
    public final List<NewsItem> results;

    public NewsItems(@JsonProperty("results") List<NewsItem> results) {
        this.results = results;
    }
}
