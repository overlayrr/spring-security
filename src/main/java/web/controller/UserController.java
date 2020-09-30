package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import web.models.Role;
import web.models.User;
import web.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/")
public class UserController {

    @Autowired private UserService userService;

    @RequestMapping(value = "admin")
    public String getUsers(ModelMap model) {
        List<User> list = userService.getAllUsers();
        model.addAttribute("list", list);
        return "users";
    }

    @PostMapping("admin/add")
    public String addUser(User user) {
        Set<Role> tempRolesSet = new HashSet<>();
        for (Role role : user.getRoles()) {
            tempRolesSet.add(new Role(Long.parseLong(role.getName()), role.getName()));
        }
        user.setRoles(tempRolesSet);
        userService.addUser(user);
        return "redirect:/admin";
    }

    @RequestMapping(path = "admin/delete/{id}")
    public String deleteEmployeeById(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @RequestMapping(path = {"admin/edit", "admin/edit/{id}"})
    public String editEmployeeById(Model model, @PathVariable("id") Optional<Long> id) {
        if (id.isPresent()) {
            User entity = userService.getUserById(id.get());
            model.addAttribute("employee", entity);
        } else {
            model.addAttribute("employee", new User());
        }
        return "updateUser";
    }

    @RequestMapping(path = "/createEmployee", method = RequestMethod.POST)
    public String createOrUpdateEmployee(User employee){
        Set<Role> tempRolesSet = new HashSet<>();
        for (Role role : employee.getRoles()) {
            tempRolesSet.add(new Role(Long.parseLong(role.getName()), role.getName()));
        }
        employee.setRoles(tempRolesSet);
        if (employee.getId() == null) {
            userService.addUser(employee);
        } else userService.updateUser(employee);
        return "redirect:/admin";
    }

    @GetMapping("admin/update")
    public String updateUserForm(ModelMap model, User user) {
        model.addAttribute("user", userService.getUserById(user.getId()));
        return "updateUser";
    }

    @PostMapping("admin/update")
    public String updateUser(User user) {
        Set<Role> tempRolesSet = new HashSet<>();
        for (Role role : user.getRoles()) {
            tempRolesSet.add(new Role(Long.parseLong(role.getName()), role.getName()));
        }
        user.setRoles(tempRolesSet);
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @RequestMapping(value = "user")
    public String userDataGet(ModelMap model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute(
                "user", userService.getUserById(userService.getUserIdByLogin(auth.getName())));
        return "userData";
    }

    @GetMapping("login")
    public String loginPage() {
        return "login";
    }


}
