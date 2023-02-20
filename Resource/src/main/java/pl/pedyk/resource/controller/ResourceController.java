package pl.pedyk.resource.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pedyk.resource.service.ResourceService;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService resourceService;


    @PostMapping
    public ResponseEntity<Map<String, Long>> uploadResource(@RequestBody byte[] audioData) {
        return ResponseEntity.ok().body(resourceService.saveResource(audioData));
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getResource(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok().body(resourceService.getResource(id));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, long[]>> deleteResources(@RequestParam("id") String ids) {
        return ResponseEntity.ok().body(resourceService.deleteResources(ids));
    }
}
