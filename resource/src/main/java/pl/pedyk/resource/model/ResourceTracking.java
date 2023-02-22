package pl.pedyk.resource.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "resource_tracking_gen")
    @SequenceGenerator(name = "resource_tracking_gen", sequenceName = "RESOURCE_TRACKING_SEQ", allocationSize = 1)
    private Long id;
    private String bucketName;

    public ResourceTracking(String bucketName) {
        this.bucketName = bucketName;
    }
}
