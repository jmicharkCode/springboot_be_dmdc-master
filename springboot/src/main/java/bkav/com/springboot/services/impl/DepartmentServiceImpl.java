package bkav.com.springboot.services.impl;


import bkav.com.springboot.models.Entities.Department;

import bkav.com.springboot.repository.DepartmentRepository;
import bkav.com.springboot.services.DepartmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository repository;

    @Override
    public List<Department> findAll() {
        return repository.findAll();
    }

    @Override
    public Department findById(String id) {
        Optional<Department> depOpt = repository.findById(Long.parseLong(id));
        return depOpt.orElse(null);
    }

    @Override
    public Department update(String id, Department dep) {
        Optional<Department> depOpt = repository.findById(Long.parseLong(id));

        if(!depOpt.isPresent()) {
            return null;
        }

        Department currentDep= depOpt.get();
        if(!currentDep.getId().equals(dep.getId())) {
            Optional<Department> existedDep = repository.findById(dep.getId());

            if(existedDep.isPresent()) {
                return null;
            }
            currentDep.setId(dep.getId());
        }
        currentDep.setDepartmentName(dep.getDepartmentName());
        currentDep.setApiId(dep.getApiId());
        currentDep.setDepartmentIdHRM(dep.getDepartmentIdHRM());
        currentDep.setDepartmentIdExt(dep.getDepartmentIdExt());
        currentDep.setEmails(dep.getEmails());
        currentDep.setParentId(dep.getParentId());
        return repository.save(currentDep);
    }

    /*public DepartmentDto createNewDepartment(DepartmentDto dto) {
        Department dep = DepartmentMapper.INSTANCE.toModel(dto);
        Department newDep = repository.save(dep);
        return DepartmentMapper.INSTANCE.toDTO(newDep);
    }*/
    @Override
    public Department createNewDepartment(Department dep) {
        /*if (dep.getParentId() != 0) {
            Optional<Department> depParentOpt = repository.findById(dep.getParentId());
            if (!depParentOpt.isPresent())
                return null;

            Department currentDepParent = depParentOpt.get();
            // Set up DepartmentIdExt
            String departmentIdExt = currentDepParent.getDepartmentIdExt() + "." + dep.getId();
            dep.setDepartmentIdExt(departmentIdExt);

            // Set up DepartmentPath
            String departmentPath = currentDepParent.getDepartmentPath() + "\\" + dep.getDepartmentName();
            dep.setDepartmentPath(departmentPath);
        } else {
        }
        return repository.save(dep);*/
        Department depSaved = repository.save(dep);
        if (dep.getParentId() != 0) {
            Optional<Department> depParentOpt = repository.findById(dep.getParentId());
            if (!depParentOpt.isPresent())
                return null;

            Department currentDepParent = depParentOpt.get();
            // Set up DepartmentIdExt
            String departmentIdExt = currentDepParent.getDepartmentIdExt() + "." + depSaved.getId();
            depSaved.setDepartmentIdExt(departmentIdExt);

            // Set up DepartmentPath
            String departmentPath = currentDepParent.getDepartmentPath() + "\\" + depSaved.getDepartmentName();
            depSaved.setDepartmentPath(departmentPath);
        } else {
        }
        return repository.save(depSaved);

       /* depSaved.setParentId(dep.getParentId());
        depSaved.setActivated(dep.isActivated());
        depSaved.setOrder(dep.getOrder());
        depSaved.setLevel(dep.getLevel());
        depSaved.setCreatedByUserId(dep.getCreatedByUserId());
        depSaved.setCreateTime(dep.getCreateTime());
        depSaved.setLastModifiedOnDate(dep.getLastModifiedOnDate());
        depSaved.setLastModifiedByUserId(dep.getLastModifiedByUserId());
        depSaved.setVersion(dep.getVersion());
        depSaved.setHasCalendar(dep.getHasCalendar());
        depSaved.setHasReceiveWarning(dep.getHasReceiveWarning());
        depSaved.setEmails(dep.getEmails());
        depSaved.setDomain(dep.getDomain());
        depSaved.setApiId(dep.getApiId());
        depSaved.setDepartmentIdHRM(dep.getDepartmentIdHRM());
        depSaved.setParentIdHRM(dep.getDepartmentIdHRM());
*/
   //     repository.update(depSaved.getId(), depSaved.getDepartmentIdExt());





       /* Department depSaved = repository.save(dep);
        Optional<Department> depOpt = repository.findById(depSaved.getId());
        if (!depOpt.isPresent())
            return null;

        if (dep.getParentId() != 0) {
            Optional<Department> depParentOpt = repository.findById(dep.getParentId());
            if (!depParentOpt.isPresent())
                return null;

            Department currentDepParent = depParentOpt.get();
            // Set up DepartmentIdExt
            String departmentIdExt = currentDepParent.getDepartmentIdExt() + "." + depOpt.get().getId();
            depOpt.get().setDepartmentIdExt(departmentIdExt);

            // Set up DepartmentPath
            String departmentPath = currentDepParent.getDepartmentPath() + "\\" + depOpt.get().getDepartmentName();
            depOpt.get().setDepartmentPath(departmentPath);
        } else {
            repository.save(dep);
            depOpt.get().setDepartmentIdExt(depOpt.get().getId().toString());
            depOpt.get().setDepartmentPath("\\" + depOpt.get().getDepartmentName());
        }
        repository.update(depSaved.getId(), depSaved.getDepartmentIdExt());
        return repository.findById(depSaved.getId()).get();*/
    }

    @Override
    public boolean delete(String id) {
        try {
             repository.getById(Long.parseLong(id));
        } catch (EntityNotFoundException ex) {
            return false;
        }
        repository.deleteById(Long.parseLong(id));
        return true;
    }
}
