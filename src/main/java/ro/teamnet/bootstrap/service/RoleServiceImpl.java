package ro.teamnet.bootstrap.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.domain.Role;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageable;
import ro.teamnet.bootstrap.repository.RoleRepository;
import ro.teamnet.bootstrap.web.rest.dto.ModuleRightDTO;
import ro.teamnet.bootstrap.web.rest.dto.RoleDTO;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing  ModuleRights.
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Inject
    RoleRepository roleRepository;

    @Inject
    ModuleRightService moduleRightService;

    @Override
    public void save(Role role) {
         roleRepository.save(role);
    }

    @Override
    public AppPage<Role> findAll(AppPageable appPageable){
        return roleRepository.findAll(appPageable);
    }

    @Override
    public Role getOne(Long id) {
        return roleRepository.getOne(id);
    }

    @Override
    public void delete(Long id) {
        roleRepository.delete(id);
    }

    @Override
    public void update(Role role, RoleDTO roleDTO){

        role.setCode(roleDTO.getCode());
        role.setDescription(roleDTO.getDescription());
        role.setOrder(roleDTO.getOrder());
        role.setValidFrom(roleDTO.getValidFrom());
        role.setValidTo(roleDTO.getValidTo());
        role.setActive(roleDTO.getActive());
        role.setLocal(roleDTO.getLocal());

        //update moduleRights for Role
        List<ModuleRight> moduleRights = new ArrayList<>();
        for(ModuleRightDTO moduleRightDTO : roleDTO.getModuleRights()){
            moduleRights.add(moduleRightService.getOne(moduleRightDTO.getId()));
        }

        role.setModuleRights(moduleRights);
    }

}
