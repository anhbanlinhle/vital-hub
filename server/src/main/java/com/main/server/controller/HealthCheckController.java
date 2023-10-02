package com.main.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.main.server.middleware.CamelCaseMiddleware;
import com.main.server.request.CheckObjRequest;
import com.nimbusds.jose.shaded.gson.JsonObject;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthCheckController {
    @GetMapping("/check-object")
    public ResponseEntity<?> checkObject(@RequestBody CheckObjRequest request) {

        String object = request.getObject();
        String className = CamelCaseMiddleware.toCamelCase(object);

        try {
            Class<?> clazz = Class.forName("com.main.server.entity." + className);
            Object objectInstance = clazz.newInstance();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.INDENT_OUTPUT);

            // return json
            JSONObject data = new JSONObject();
            for (var field : clazz.getDeclaredFields()) {
                if (field.getName().equals("serialVersionUID")) continue;
                field.setAccessible(true);
                data.put(field.getName(), field.get(objectInstance));
            }

            JSONObject result = new JSONObject();
            result.put(className, data);


            return ResponseEntity.ok(result);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Không tìm thấy lớp tương ứng với object");
        }

    };

}
