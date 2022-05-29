package event.loop.demo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsItem {
    public final String title;
    public final String byLine;

    public NewsItem(@JsonProperty("title") String title, @JsonProperty("byline") String byLine) {
        this.title = title;
        this.byLine = byLine;
    }
}
