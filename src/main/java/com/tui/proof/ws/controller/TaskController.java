package com.tui.proof.ws.controller;

import com.tui.proof.ws.model.BookingResponse;
import com.tui.proof.ws.model.Task;
import com.tui.proof.ws.services.TaskService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

import static com.tui.proof.ws.utils.Constants.*;

@Log4j2
@RestController
public class TaskController {

  @Autowired
  private TaskService taskService;

  @PostMapping(TASK_PATH + "/{id}")
  public Task getTaskStatus(@PathVariable Integer id) {
    log.debug("getTaskStatus");
    return taskService.getTaskStatus(id);
  }

  @GetMapping(TASK_PATH+"/{id}"+RESULTS_PATH+BOOKINGS_PATH)
  public BookingResponse getBookingDetailsResult(@PathVariable @Min(0) Integer id) {
    log.debug("getBookingDetailsResult");
    return taskService.getEventResult(id);
  }
}
