package com.main.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.main.server.middleware.CamelCaseMiddleware;
import com.main.server.request.CheckObjRequest;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/get-single")
    public JSONObject getSingle() {
        JSONObject data = new JSONObject();
        data.put("param1", "rangnoduoc");
        data.put("param2", 20);
        data.put("param3", true);


        return data;
    };

    @GetMapping("/get-many")
    public JSONObject[] getMany() {
        JSONObject data = new JSONObject();
        data.put("param1", "rangnoduoc");
        data.put("param2", 37);
        data.put("param3", true);

        JSONObject data2 = new JSONObject();
        data2.put("param1", "saochaduoc");
        data2.put("param2", 29);
        data2.put("param3", true);

        JSONObject[] result = new JSONObject[2];

        result[0] = data;
        result[1] = data2;

        return result;
    };

    @PostMapping("/post-single")
    public JSONObject postSingle(@RequestBody JSONObject request) {
        return request;
    };

    @PostMapping("/post-many")
    public JSONObject[] postMany(@RequestBody JSONObject[] request) {
        return request;
    };

    @PutMapping("/put-single")
    public JSONObject putSingle(@RequestBody JSONObject request) {
        return request;
    };

    @PutMapping("/put-many")
    public JSONObject[] putMany(@RequestBody JSONObject[] request) {
        return request;
    };



}
