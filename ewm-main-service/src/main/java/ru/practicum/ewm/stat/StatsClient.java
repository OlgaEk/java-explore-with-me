package ru.practicum.ewm.stat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.stat.model.EndpointHit;

import java.util.List;
import java.util.Map;

@Service
public class StatsClient extends HttpClient {

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public void hit(EndpointHit hitDto) {
        post("/hit", hitDto);
    }

    public ResponseEntity<Object> get(String start, String end, List<String> uris, Boolean unique) {
        StringBuilder uriSB = new StringBuilder();
        if (uris != null) {
            uris.forEach(s -> uriSB.append(s).append(","));
            uriSB.delete(uriSB.length() - ",".length(), uriSB.length());
        }
        String uri = uriSB.toString();
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uri,
                "unique", unique
        );
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }


}
