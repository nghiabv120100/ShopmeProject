package com.shopme.admin.user;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AccountController {
    @Autowired
    private UserService userService;

    @GetMapping("account")
    public String viewDetails(@AuthenticationPrincipal ShopmeUserDetails loggedUser, Model model) {
        String email = loggedUser.getUsername();
        User user = userService.getByEmail(email);
        model.addAttribute("user", user);
        return "account_form";
    }

    @PostMapping("/account/update")
    public String saveDetail(User user, RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal ShopmeUserDetails loggedUser,
                             @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User userSaved = userService.updateAccount(user);
            String uploadDir = "user-photos/" + userSaved.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (user.getPhotos() != null && user.getPhotos().isBlank()) {user.setPhotos(null);}
            userService.updateAccount(user);
        }
        loggedUser.setLastName(user.getLastName());
        loggedUser.setFirstName(user.getFirstName());
        redirectAttributes.addFlashAttribute("message", "The account details has been updated successfully");
        return "redirect:/account";
    }

}
