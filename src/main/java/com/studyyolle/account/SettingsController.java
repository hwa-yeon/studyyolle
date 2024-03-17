package com.studyyolle.account;

import com.studyyolle.account.form.NicknameForm;
import com.studyyolle.account.form.Notifications;
import com.studyyolle.account.form.PasswordForm;
import com.studyyolle.account.form.Profile;
import com.studyyolle.account.validator.NicknameValidator;
import com.studyyolle.account.validator.PasswordFormValidator;
import com.studyyolle.domain.Account;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class SettingsController {

    private static final String SETTINGS_PROFILE_VIEW_NAME = "settings/profile";
    private static final String SETTINGS_PASSWORD_VIEW_NAME = "settings/password";
    private static final String SETTINGS_NOTIFICATIONS_VIEW_NAME = "settings/notifications";
    private static final String SETTINGS_ACCOUNT_VIEW_NAME = "settings/account";

    private final NicknameValidator nicknameValidator;
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    @InitBinder("passwordForm")
    public void passwordFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new PasswordFormValidator());
    }

    @InitBinder("nicknameForm")
    public void nicknameFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(nicknameValidator);
    }

    @GetMapping("/settings/profile")
    public String updateProfileForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, Profile.class));
        return SETTINGS_PROFILE_VIEW_NAME;
    }

    @PostMapping("/settings/profile")
    public String updateProfile(@CurrentAccount Account account,
                                @Validated @ModelAttribute Profile profile,
                                Errors errors,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if(errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS_PROFILE_VIEW_NAME;
        }
        accountService.updateProfile(account, profile);
        redirectAttributes.addFlashAttribute("message", "프로필을 수정했습니다.");

        return "redirect:/" + SETTINGS_PROFILE_VIEW_NAME;
    }

    @GetMapping("/settings/password")
    public String updatePasswordForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new PasswordForm());
        return SETTINGS_PASSWORD_VIEW_NAME;
    }

    @PostMapping("/settings/password")
    public String updatePassword(@CurrentAccount Account account,
                                 @Validated PasswordForm passwordForm,
                                 Errors errors,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if(errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS_PASSWORD_VIEW_NAME;
        }
        accountService.updatePassword(account, passwordForm.getNewPassword());
        redirectAttributes.addFlashAttribute("message", "패스워드를 변경했습니다.");
        return "redirect:/" + SETTINGS_PASSWORD_VIEW_NAME;
    }

    @GetMapping("/settings/notifications")
    public String updateNotificationsForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, Notifications.class));
        return SETTINGS_NOTIFICATIONS_VIEW_NAME;
    }

    @PostMapping("/settings/notifications")
    public String updateNotifications(@CurrentAccount Account account,
                                      @Validated Notifications notifications,
                                      Errors errors,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        if(errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS_NOTIFICATIONS_VIEW_NAME;
        }
        accountService.updateNotifications(account, notifications);
        redirectAttributes.addFlashAttribute("message", "알림 설정을 변경했습니다.");
        return "redirect:/" + SETTINGS_NOTIFICATIONS_VIEW_NAME;
    }

    @GetMapping("/settings/account")
    public String updateAccountForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, NicknameForm.class));
        return SETTINGS_ACCOUNT_VIEW_NAME;
    }

    @PostMapping("/settings/account")
    public String updateAccount(@CurrentAccount Account account,
                                @Validated NicknameForm nicknameForm,
                                Errors errors,
                                Model model,
                                RedirectAttributes redirectAttributes,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        if(errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS_ACCOUNT_VIEW_NAME;
        }
        accountService.updateNickname(account, nicknameForm.getNickname(), request, response);
        redirectAttributes.addFlashAttribute("message", "닉네임을 수정했습니다.");
        return "redirect:/" + SETTINGS_ACCOUNT_VIEW_NAME;
    }
}
