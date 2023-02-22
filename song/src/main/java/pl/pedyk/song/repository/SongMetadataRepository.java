package pl.pedyk.song.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pedyk.song.model.Song;

public interface SongMetadataRepository extends JpaRepository<Song, Long> {
}
