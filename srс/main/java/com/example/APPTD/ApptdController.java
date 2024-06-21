package com.example.APPTD;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
public class ApptdController {
    private final ApptdService miniAppService;

    @Autowired
    public ApptdController(ApptdService miniAppService) {
        this.miniAppService = miniAppService;
    }

    @GetMapping("/role")
    public String getRole() throws IOException {
        return miniAppService.getRole();
    }

    @GetMapping("/set-status")
    public String setStatus(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, @RequestParam("email") String email, @RequestParam("role") String role) throws IOException {
        miniAppService.registerUser(lastName, firstName, email, role);
        return miniAppService.setStatus(miniAppService.encodeToBase64(email, miniAppService.codeEmail(email)));

    }

    @GetMapping("/get-code")
    public String getCode(@RequestParam("email") String email) throws IOException {
        return miniAppService.codeEmail(email);
    }
}
