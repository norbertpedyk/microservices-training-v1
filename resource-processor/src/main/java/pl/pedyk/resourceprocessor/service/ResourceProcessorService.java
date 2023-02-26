package pl.pedyk.resourceprocessor.service;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.pedyk.resourceprocessor.model.SongMetadata;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class ResourceProcessorService {

    private final RestTemplate restTemplate;

    public ResourceProcessorService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${custom.song-service-url}")
    private String songUrl;

    @Value("${custom.resource-service-url}")
    private String resourceUrl;


    @KafkaListener(topics = "resourceAddedTopic", groupId = "1")
    void listener(String id) throws IOException, CannotReadException, TagException, InvalidAudioFrameException, ReadOnlyFileException {

        byte[] file = restTemplate.getForObject(resourceUrl + id, byte[].class);
        File tempFile = Files.createTempFile("temp", ".mp3").toFile();
        tempFile.deleteOnExit();
        assert file != null;
        Files.write(tempFile.toPath(), file);
        AudioFile audioFile = AudioFileIO.read(tempFile);
        Tag tag = audioFile.getTag();

        postFromTikaMetadata(tag, id, audioFile);
    }


    private void postFromTikaMetadata(Tag tag, String id, AudioFile audioFile) {
        restTemplate.postForObject(songUrl, SongMetadata.builder()
                        .resourceId(id)
                        .album(tag.getFirst(FieldKey.ALBUM))
                        .artist(tag.getFirst(FieldKey.ARTIST))
                        .name(tag.getFirst(FieldKey.TITLE))
                        .length(String.valueOf(audioFile.getAudioHeader().getTrackLength()))
                        .year(tag.getFirst(FieldKey.YEAR))
                        .build(),
                SongMetadata.class);
    }

}
