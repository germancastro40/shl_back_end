package com.consultorio.casos_judiciales.services;

import com.consultorio.casos_judiciales.enums.CodeTypes;
import com.consultorio.casos_judiciales.enums.Status;
import com.consultorio.casos_judiciales.models.UsersCodes;
import com.consultorio.casos_judiciales.models.Usuarios;
import com.consultorio.casos_judiciales.repositories.UserCodeRepository;
import com.consultorio.casos_judiciales.repositories.UsuariosRepository;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class UsersCodeService {
    @Autowired
    private UserCodeRepository userCodeRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private EmailService emailService;

    private String generateCode(String email, CodeTypes codeTypes) throws BadRequestException, MessagingException {
        Optional<Usuarios> usuarios;

        if (codeTypes == CodeTypes.ACTIVATION_USER_CODE) {
            usuarios = usuariosRepository.findByEmailAndStatus(email, Status.DISABLE);
        } else {
            usuarios = usuariosRepository.findByEmailAndStatus(email, Status.ACTIVE);
        }
        if (usuarios.isEmpty()){
            throw new BadRequestException("The user wasn't found");
        };

        int code = (int)(Math.random() * 900000) + 100000;

        if (codeTypes == CodeTypes.RESET_PASSWORD_CODE){
            emailService.sendEmailWithCodeToResetPassword(email, String.valueOf(code));
        }
        emailService.sendEmailToActivateAccount(email,String.valueOf(code));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 30);
        Date expirationDate = calendar.getTime();

        UsersCodes usersCodes = UsersCodes.builder()
                .code(String.valueOf(code))
                .status(Status.ACTIVE)
                .createdAt(new Date())
                .codeTypes(codeTypes)
                .usuarios(usuarios.get())
                .expiredAt(expirationDate)
                .build();
        userCodeRepository.save(usersCodes);

        return "A code has been sent to the email provided";
    }

    public String generateCodeForResetPassword(String email) throws BadRequestException, MessagingException {
        return generateCode(email, CodeTypes.RESET_PASSWORD_CODE);
    }
    public String generateCodeForActivateUser(String email) throws BadRequestException, MessagingException {
        return generateCode(email, CodeTypes.ACTIVATION_USER_CODE);
    }

    public Optional<UsersCodes> findCodeToActivateUser(String code){
        return userCodeRepository.findByCodeAndStatusAndCodeTypes(code, Status.ACTIVE, CodeTypes.ACTIVATION_USER_CODE);
    }

    public Optional<UsersCodes> findCodeToResetPassword(String code){
        return userCodeRepository.findByCodeAndStatusAndCodeTypes(code, Status.ACTIVE, CodeTypes.RESET_PASSWORD_CODE);
    }

    public void invalidateCode(UsersCodes usersCodes){
        usersCodes.setStatus(Status.DISABLE);
        usersCodes.setExpiredAt(new Date());

        userCodeRepository.save(usersCodes);
    }
}
