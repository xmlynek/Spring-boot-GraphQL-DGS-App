package com.example.sales.client.feign;

import com.course.graphql.generated.types.ModelSimple;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "models", url = "http://localhost:8081", path = "api/v1/models")
public interface ModelsClient {

    @GetMapping("/simple")
    List<ModelSimple> getSimpleModels(@RequestParam(name = "modelUuids") String modelUuidsWithCommaSeparator);
}
