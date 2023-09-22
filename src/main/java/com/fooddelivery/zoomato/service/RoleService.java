package com.fooddelivery.zoomato.service;

import com.fooddelivery.zoomato.repository.RoleRepository;
import com.fooddelivery.zoomato.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface RoleService {

    public Role createNewRole(Role role);
}
