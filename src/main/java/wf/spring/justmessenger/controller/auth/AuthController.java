package wf.spring.justmessenger.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import wf.spring.justmessenger.dto.auth.*;
import wf.spring.justmessenger.model.exception.basic.BadRequestException;
import wf.spring.justmessenger.service.security.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final PersonRegisterService personRegisterService;
    private final AuthenticationService authenticationService;


    @PostMapping("/login")
    public JwtAndRtTokenRsDTO login(@RequestBody @Valid AuthenticationRqDTO authenticationRqDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        return authenticationService.authenticate(authenticationRqDTO);
    }

    @PostMapping("/signup")
    public void register(@RequestBody @Valid RegisterRqDTO registerRqDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        personRegisterService.register(registerRqDTO);
    }

    @PostMapping("/refresh")
    public JwtAndRtTokenRsDTO refreshToken(@RequestBody @Valid RefreshTokenRqDTO refreshTokenRqDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        return authenticationService.refreshToken(refreshTokenRqDto);
    }


}
