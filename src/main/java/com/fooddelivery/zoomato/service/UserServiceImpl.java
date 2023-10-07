package com.fooddelivery.zoomato.service;

import com.fooddelivery.zoomato.configuration.JwtRequestFilter;
import com.fooddelivery.zoomato.entity.Restaurant;
import com.fooddelivery.zoomato.entity.Role;
import com.fooddelivery.zoomato.entity.User;
import com.fooddelivery.zoomato.repository.RestaurantRepository;
import com.fooddelivery.zoomato.repository.RoleRepository;
import com.fooddelivery.zoomato.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RestaurantRepository restaurantRepository;

    public void initRoleAndUser() {

        Role adminRole = new Role();
        adminRole.setRoleName("Admin");
        adminRole.setRoleDescription("Admin role");
        roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("User");
        userRole.setRoleDescription("Default Role for newly created record");
        roleRepository.save(userRole);

        Role restaurantRole=new Role();
        restaurantRole.setRoleName("Restaurant");
        restaurantRole.setRoleDescription("Default Role for Restaurant");
        roleRepository.save(restaurantRole);


        User adminUser = new User();
        adminUser.setUserName("admin");
        adminUser.setUserPassword(getEncodedPassword("admin@pass"));
        adminUser.setUserFirstName("admin");
        adminUser.setUserLastName("admin");
        adminUser.setPhoneNumber("9876543234");
        adminUser.setEmailId("admin@gmail.com");
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRole(adminRoles);
        userRepository.save(adminUser);

        User restaurantUser = new User();
        restaurantUser.setUserName("gharkakhana");
        restaurantUser.setUserPassword(getEncodedPassword("gharkakhana@pass"));
        restaurantUser.setUserFirstName("Restaurant");
        restaurantUser.setUserLastName("Restaurant");
        restaurantUser.setPhoneNumber("1234567890");
        restaurantUser.setEmailId("gharkakhana@gmail.com");
        Set<Role> restaurantRoles = new HashSet<>();
        restaurantRoles.add(restaurantRole);
        restaurantUser.setRole(restaurantRoles);
        userRepository.save(restaurantUser);

        User user = new User();
        user.setUserName("talha26");
        user.setUserPassword(getEncodedPassword("talha26@pass"));
        user.setUserFirstName("Mohammed");
        user.setUserLastName("Talha");
        user.setPhoneNumber("9876512345");
        user.setEmailId("talha26@gmail.com");
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setRole(userRoles);
        userRepository.save(user);
    }


    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public User registerNewUser(User user){
        Role role = roleRepository.findById("User").get();
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role);
        user.setRole(userRoles);
        user.setUserPassword(getEncodedPassword(user.getUserPassword()));
        Optional<User> existingUser=userRepository.findById(user.getUserName());
        Boolean userIsPresent=existingUser.isPresent();
        if(userIsPresent==true){
            throw new RuntimeException("UserName Already Exists, Please login or Choose another UserName");
        }else {
            return userRepository.save(user);
        }


        }


    public User registerNewRestaurant(User user) {
        Role role = roleRepository.findById("Restaurant").get();
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role);
        user.setRole(userRoles);
        user.setUserPassword(getEncodedPassword(user.getUserPassword()));
        Optional<User> existingUser=userRepository.findById(user.getUserName());
        Boolean userIsPresent=existingUser.isPresent();
        if(userIsPresent==true){
            throw new RuntimeException("UserName Already Exists, Please login or Choose another UserName");
        }else {
            return userRepository.save(user);
        }

    }

    public User getMyDetails() {
        String username = JwtRequestFilter.CURRENT_USER;
        return userRepository.findById(username).get();
    }

}
