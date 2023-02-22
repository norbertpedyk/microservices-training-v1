package pl.pedyk.song.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.pedyk.song.model.Song;
import pl.pedyk.song.repository.SongMetadataRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SongService {

    private final SongMetadataRepository songMetadataRepository;

    public Song getSong(Long id) {
        return songMetadataRepository.findById(id).orElse(null);
    }


    public Map<String, Long> createSong(Song requestSong) {
        return new HashMap<>() {{
            put("id", songMetadataRepository.save(requestSong).getId());
        }};
    }


    public Map<String, long[]> deleteSongs(String ids) {

        List<String> idsToRemove = Arrays.asList(ids.split(","));

        songMetadataRepository.deleteAllById(idsToRemove.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList()));
        return new HashMap<>() {{

            put("id", idsToRemove.stream()
                    .mapToLong(Long::parseLong)
                    .toArray());
        }};
    }
}
