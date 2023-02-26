package pl.pedyk.song.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pedyk.song.model.Song;
import pl.pedyk.song.service.SongService;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/songs")
public class SongController {

    private final SongService songService;

    @PostMapping
    public ResponseEntity<Map<String, Long>> createSong(@RequestBody Song requestSong) {
        return ResponseEntity
                .ok()
                .body(songService.createSong(requestSong));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getResource(@PathVariable Long id) {
        return ResponseEntity.ok().body(songService.getSong(id));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, long[]>> deleteSongs(@RequestParam("id") String ids) {
        return ResponseEntity.ok().body(songService.deleteSongs(ids));
    }

}
