package pl.pedyk.resourceprocessor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.pedyk.resourceprocessor.model.SongMetadata;

@Service
public class ResourceProcessorService {

    private final RestTemplate restTemplate;

    public ResourceProcessorService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${custom.song-service-url}")
    private String songUrl;

    public void fetchSongMetadata(String id) {
        SongMetadata songMetadata = restTemplate.getForObject(
                songUrl + id, SongMetadata.class);
    }
}
