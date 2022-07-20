package bkav.com.springboot.services.impl;

import bkav.com.springboot.models.Entities.ActivityLog;
import bkav.com.springboot.models.Entities.Demo;
import bkav.com.springboot.models.Entities.Position;
import bkav.com.springboot.models.Entities.User;
import bkav.com.springboot.payload.response.MessageResponse;
import bkav.com.springboot.payload.util.ActivityType;
import bkav.com.springboot.payload.util.Content;
import bkav.com.springboot.payload.util.Message;
import bkav.com.springboot.payload.util.Status;
import bkav.com.springboot.repository.PositionRepository;
import bkav.com.springboot.services.PositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PositionServiceImpl implements PositionService {
    @Autowired
    private PositionRepository positionRepository;

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    public List<Position> getList() {
        List<Position> positions = positionRepository.findAll();
        return positions;
    }

    @Override
    public ResponseEntity<?> insert(Position position) {
        try {
            positionRepository.save(position);
            logger.info("insert position success");
            return ResponseEntity.ok(new MessageResponse(Status.SUCCESS, Message.INSERT_SUCCESS, true, false));
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(Status.EXCEPTION, Message.EXCEPTION, false, true));
        }
    }

    @Override
    public ResponseEntity<?> update(Position position, int id) {
        try {
            Position position1 = positionRepository.getById(Long.valueOf(id));
            if (position1 == null) {
                return ResponseEntity.ok(new MessageResponse(Status.FAIL, Message.UPDATE_FAIL, true, false));
            } else {
                Position position2 = new Position();
                position2.setPositionId(position1.getPositionId());
                position2.setPositionName(position.getPositionName());
                position2.setPriorityLevel(position.getPriorityLevel());
                position2.setApproved(position.isApproved());
                position2.setCanSetDateOverdue(position.getCanSetDateOverdue());
                position2.setApiId(position.getApiId());
                position2.setPositionIdHRM(position.getPositionIdHRM());
                BeanUtils.copyProperties(position2, position1);
                positionRepository.saveAndFlush(position1);
                logger.info("update success position id: " + id);
                return ResponseEntity.ok(new MessageResponse(Status.SUCCESS, Message.UPDATE_SUCCESS, true, false));
            }
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(Status.EXCEPTION, Message.EXCEPTION, false, true));
        }
    }

    @Override
    public ResponseEntity<?> delete(int id) {
        try {
            if (!positionRepository.existsById(Long.valueOf(id))) {
                logger.info("position not exits id: " + id);
                return ResponseEntity.ok(new MessageResponse(Status.EXITS, Message.EXITS, true, false));
            } else {
                positionRepository.deleteById(Long.valueOf(id));
                logger.info("delete success position id: " + id);
                return ResponseEntity.ok(new MessageResponse(Status.SUCCESS, Message.DELETE_SUCCESS, true, false));
            }
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(Status.EXCEPTION, Message.EXCEPTION, false, true));
        }
    }
}
