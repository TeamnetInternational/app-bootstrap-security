package ro.teamnet.bootstrap.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageImpl;
import ro.teamnet.bootstrap.extend.AppPageRequest;
import ro.teamnet.bootstrap.extend.Filter;
import ro.teamnet.bootstrap.repository.ModuleRightRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;


public class ModuleRightServiceImplTest {
    /**
     * ModuleRight Service
     */
    @InjectMocks
    private ModuleRightServiceImpl service;

    @Mock
    private ModuleRightRepository moduleRightRepository;

    @Before
    public void init(){
        service = new ModuleRightServiceImpl();
        initMocks(this);
    }

    /**
     * Method: {@link ModuleRightServiceImpl#save(ro.teamnet.bootstrap.domain.ModuleRight)} .
     * When: A Role is saved
     * Then:
     */
    @Test
    public void createUserInformationTest(){

        final Long id = 1l;
        final Short right = 1;
        final ModuleRight moduleRight = new ModuleRight();
        moduleRight.setId(id);
        moduleRight.setRight(right);

        service.save(moduleRight);
        verify(moduleRightRepository, times(1)).save(moduleRight);

    }

    /**
     * Method: {@link ModuleRightServiceImpl#findAll(ro.teamnet.bootstrap.extend.AppPageable)} .
     * When: find all roles
     * Then:
     */
    @Test
    public void findAllTest(){

        final Long id = 1l;
        final Short right = 1;
        final ModuleRight moduleRight = new ModuleRight();
        moduleRight.setId(id);
        moduleRight.setRight(right);
        List<ModuleRight> moduleRights = new ArrayList<>();
        moduleRights.add(moduleRight);

        AppPage appPage = new AppPageImpl(moduleRights);

        AppPageRequest appPageRequest = new AppPageRequest(0,1,new ArrayList<Filter>());

        when(moduleRightRepository.findAll(appPageRequest)).thenReturn(appPage);
        assertEquals(appPage,service.findAll(appPageRequest) );

    }

    /**
     * Method: {@link ModuleRightServiceImpl#getOne(Long)} .
     * When: find one Role
     * Then:
     */
    @Test
    public void getOneTest(){

        final Long id = 1l;
        final Short right = 1;
        final ModuleRight moduleRight = new ModuleRight();
        moduleRight.setId(id);
        moduleRight.setRight(right);

        when(moduleRightRepository.findOne(anyLong())).thenReturn(moduleRight);
        assertEquals(moduleRight,service.getOne(id));

    }

    /**
     * Method: {@link ModuleRightServiceImpl#getOne(Long)} .
     * When: Delete one Role
     * Then: Id Role is not found throws {@Link EmptyResultDataAccessException}
     */
    @Test
    public void deleteTest(){

        final Long id = 1l;
        final Short right = 1;
        final ModuleRight moduleRight = new ModuleRight();
        moduleRight.setId(id);
        moduleRight.setRight(right);

        when(moduleRightRepository.findOne(id)).thenReturn(moduleRight);
        service.delete(id);
        verify(moduleRightRepository, times(1)).delete(id);

    }
}
