package co.lecompany.app.controller;

import co.lecompany.app.data.entity.SamplePerson;
import co.lecompany.app.data.service.SamplePersonService;
import co.lecompany.app.dto.BaseResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${base-path}/users")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final SamplePersonService samplePersonService;

    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;

    @GetMapping("")
    public ResponseEntity<BaseResponse> getAll(@RequestParam(required = false) Map<String, String> params) throws JsonProcessingException {
        long startTime = System.currentTimeMillis();
        log.info("START GET----/api/v1/users?{}", request.getQueryString());
        BaseResponse baseResponse = new BaseResponse();
        try {
            List<SamplePerson> samplePersonList = samplePersonService.getWithMultipleConditions(params);
            baseResponse.setStatus(0);
            baseResponse.setMessage("Success");
            baseResponse.setData(samplePersonList);
        } catch (Exception ex) {
            log.error("Get user catch exception: {}", ex.getMessage(), ex);
        }
        Long duration = System.currentTimeMillis() - startTime;
        log.info("END GET----/api/v1/users?{} with response {} after `{}`ms", request.getQueryString(), objectMapper.writeValueAsString(baseResponse), duration);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Object> add(@RequestBody SamplePerson samplePerson) throws JsonProcessingException {
        long startTime = System.currentTimeMillis();
        log.info("START POST----/api/v1/users with data: {}", objectMapper.writeValueAsString(samplePerson));
        BaseResponse baseResponse = new BaseResponse();
        try {
            samplePersonService.update(samplePerson);
            baseResponse.setStatus(0);
            baseResponse.setMessage("Success");
        } catch (Exception ex) {
            log.error("Get user catch exception: {}", ex.getMessage(), ex);
        }
        Long duration = System.currentTimeMillis() - startTime;
        log.info("END POST----/api/v1/users with response {} after `{}`ms", objectMapper.writeValueAsString(baseResponse), duration);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Object> deleteByEmail(@PathVariable(value = "email") String email) throws JsonProcessingException {
        long startTime = System.currentTimeMillis();
        log.info("START DELETE----/api/v1/users/{}", email);
        BaseResponse baseResponse = new BaseResponse();
        try {
            if (samplePersonService.deleteByEmail(email)) {
                baseResponse.setStatus(0);
                baseResponse.setMessage("Success");
            } else {
                baseResponse.setStatus(-3);
                baseResponse.setMessage("Email does not exit");
            }
        } catch (Exception ex) {
            log.error("Get user catch exception: {}", ex.getMessage(), ex);
        }
        Long duration = System.currentTimeMillis() - startTime;
        log.info("END POST----/api/v1/users with response {} after `{}`ms", objectMapper.writeValueAsString(baseResponse), duration);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}
