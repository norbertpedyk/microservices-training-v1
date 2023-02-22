package pl.pedyk.resourceprocessor.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SongMetadata {


    private Long id;

    private String name;

    private String artist;

    private String album;

    private String length;

    private String resourceId;

    private String year;
}
