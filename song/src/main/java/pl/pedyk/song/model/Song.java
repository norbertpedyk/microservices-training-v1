package pl.pedyk.song.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "songs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "song_gen")
    @SequenceGenerator(name = "song_gen", sequenceName = "SONG_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "artist")
    private String artist;

    @Column(name = "album")
    private String album;

    @Column(name = "length")
    private String length;

    @Column(name = "resource_id")
    private String resourceId;

    @Column(name = "year")
    private String year;
}