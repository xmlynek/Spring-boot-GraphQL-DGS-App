package com.example.products.rest;

import com.course.graphql.generated.types.ModelSimple;
import com.example.products.service.ModelQueryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/models")
@AllArgsConstructor
public class ModelsController {

    private final ModelQueryService modelQueryService;

    @GetMapping("/simple")
    public List<ModelSimple> getSimpleModels(@RequestParam(name = "modelUuids") String modelUuidsWithCommaSeparator) {
        var modelUuids = Arrays.asList(
                modelUuidsWithCommaSeparator.split(",")
        );
        return modelQueryService.findSimpleModels(modelUuids);
    }
}
