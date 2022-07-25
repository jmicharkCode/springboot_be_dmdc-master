package bkav.com.springboot.controllers;

import bkav.com.springboot.ResHelper.ResponseHelper;

import bkav.com.springboot.models.Entities.Demo;
import bkav.com.springboot.models.Entities.Position;
import bkav.com.springboot.payload.util.PathResources;
import bkav.com.springboot.services.PositionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(PathResources.POSITION)

public class PositionController {

    @Autowired
    private PositionService positionService;

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @PreAuthorize("hasPermission('POSITION', 'READ')")
    @GetMapping(PathResources.GET_LIST)
    public Object findAll() {
        List<Position> positions = positionService.getList();
        return ResponseHelper.getResponse(positions, HttpStatus.OK);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @PostMapping(PathResources.SAVE)
    @PreAuthorize("hasPermission('POSITION', 'CREATE')")
    public ResponseEntity<?> insert(@RequestBody Position position, HttpServletRequest httpServletRequest) {
        return positionService.insert(position);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @PutMapping(PathResources.UPDATE)
    @PreAuthorize("hasPermission('POSITION', 'UPDATE')")
    public ResponseEntity<?> update(@RequestParam int id,
                                    @RequestBody Position position,
                                    HttpServletRequest httpServletRequest) {
        return positionService.update(position, id);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @DeleteMapping(PathResources.DELETE)
    @PreAuthorize("hasPermission('POSITION', 'DELETE')")
    public ResponseEntity<?> delete(@RequestParam int id,
                                    HttpServletRequest httpServletRequest) {
        return positionService.delete(id);
    }
}
