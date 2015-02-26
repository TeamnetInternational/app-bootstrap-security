package ro.teamnet.bootstrap.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.teamnet.bootstrap.domain.Role;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageable;
import ro.teamnet.bootstrap.service.RoleService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * REST controller for managing role.
 */
@RestController
@RequestMapping("/role")
public class RoleResource {

    private final Logger log = LoggerFactory.getLogger(RoleResource.class);

    @Inject
    RoleService roleService;

    /**
     * POST  /rest/roles/:id -> create a new role.
     */
    @RequestMapping(value = "/rest/roles",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> create(@RequestBody Role role, HttpServletRequest request,
                       HttpServletResponse response){
        log.debug("REST request to save Role : {}", role);
        if(roleService.getOne(role.getId()) != null){
            return new ResponseEntity<String>("Role allready exists", HttpStatus.BAD_REQUEST);
        }
        roleService.save(role);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * GET  /rest/roles/:id -> get all the roles.
     */
    @RequestMapping(value = "/rest/roles",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public  AppPage<Role> getAll(AppPageable appPageable){
        log.debug("REST request to get all roles");
        return roleService.findAll(appPageable);
    }

    /**
     * GET  /rest/roles -> get the "name" role.
     */
    @RequestMapping(value = "/rest/roles/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Role> get(@PathVariable Long id, HttpServletResponse response){
        log.debug("REST request to get the role : {}", id);
        Role role = roleService.getOne(id);
        if(role == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    /**
     * DELETE   /rest/roles/:id -> delete the "name" role
     */
    @RequestMapping(value = "/rest/roles/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable Long id){
        log.debug("REST request to delete Role : {}",id);
        roleService.delete(id);
    }

}