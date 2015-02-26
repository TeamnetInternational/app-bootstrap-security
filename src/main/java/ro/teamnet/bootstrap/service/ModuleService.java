package ro.teamnet.bootstrap.service;


import ro.teamnet.bootstrap.domain.Module;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageable;

public interface ModuleService {

    public Boolean save(Module module);

    public AppPage<Module> findAll(AppPageable appPageable);

    public Module getOne(Long id);

    public void delete(Long id);

}
