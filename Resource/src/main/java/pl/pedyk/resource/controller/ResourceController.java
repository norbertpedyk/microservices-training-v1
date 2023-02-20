package pl.pedyk.resource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.pedyk.resource.service.ResourceService;

import java.util.Map;

@RestController
public class ResourceController {

    private final ResourceService resourceService;

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping("/resources")
    public ResponseEntity<Map<String, Long>> uploadResource(@RequestBody byte[] audioData) {
        Map<String, Long> response = resourceService.saveResource(audioData);
        return ResponseEntity.ok().body(response);
    }
}
