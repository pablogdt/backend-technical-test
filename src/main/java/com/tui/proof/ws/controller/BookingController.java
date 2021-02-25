package com.tui.proof.ws.controller;

import com.tui.proof.ws.events.*;
import com.tui.proof.ws.model.*;
import com.tui.proof.ws.services.EventMediatorService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;

import static com.tui.proof.ws.utils.Constants.*;

@Log4j2
@RestController
public class BookingController {

  @Autowired
  private EventMediatorService eventMediatorService;

  @PostMapping(BOOKINGS_PATH)
  public void createBooking(HttpServletResponse response) {
    log.debug("createBooking");
    Task task = eventMediatorService.publishEvent(new BookingCreationEvent());
    String endpoint = BOOKINGS_PATH+"/"+task.getId();
    response.addHeader(LOCATION, endpoint);
    response.setStatus(HttpServletResponse.SC_ACCEPTED);
  }

  @PatchMapping(BOOKINGS_PATH+"/{id}/flights/")
  public void addFlightToBooking(@RequestBody @Valid AddFlightRequest addFlightRequest, @PathVariable Integer id, HttpServletResponse response) throws IOException {
    if (addFlightRequest.getToken() == null || addFlightRequest.getToken().isEmpty()) {
      response.setStatus(400);
      response.getWriter().write("Validation fields error, please fill the flight token in correctly");
      response.getWriter().flush();
      return;
    }
    log.debug("addFlightToBooking");
    Task task = eventMediatorService.publishEvent(new FlightAddToBookingEvent(id, addFlightRequest.getToken()));
    String endpoint = TASK_PATH + "/"+task.getId();
    response.addHeader(LOCATION, endpoint);
    response.setStatus(HttpServletResponse.SC_ACCEPTED);
  }

  @DeleteMapping(BOOKINGS_PATH+"/{id}"+FLIGHTS_PATH+"/{flightToken}")
  public void deleteFlightFromBooking(@PathVariable Integer id, @PathVariable String flightToken, HttpServletResponse response) {
    log.debug("deleteFlightFromBooking");
    Task task = eventMediatorService.publishEvent(new FlightDeleteFromBookingEvent(id, flightToken));
    String endpoint = TASK_PATH +"/"+ task.getId();
    response.addHeader(LOCATION, endpoint);
    response.setStatus(HttpServletResponse.SC_ACCEPTED);
  }

  @GetMapping(BOOKINGS_PATH+"/{id}")
  public void getBookingDetails(@PathVariable Integer id, HttpServletResponse response) {
    log.debug("getBookingDetails");
    Task task = eventMediatorService.publishEvent(new BookingQueryEvent(id));
    String endpoint = TASK_PATH +"/"+ task.getId();
    response.addHeader(LOCATION, endpoint);
    response.setStatus(HttpServletResponse.SC_ACCEPTED);
  }

  @DeleteMapping(BOOKINGS_PATH+"/{id}")
  public void deleteBooking(@PathVariable Integer id, HttpServletResponse response) {
    log.debug("deleteBooking");
    Task task = eventMediatorService.publishEvent(new BookingDeleteEvent(id));
    String endpoint = TASK_PATH+ "/" + task.getId();
    response.addHeader(LOCATION, endpoint);
    response.setStatus(HttpServletResponse.SC_ACCEPTED);
  }

  @PostMapping(BOOKINGS_PATH+"/{id}")
  public void confirmBooking(@PathVariable Integer id, HttpServletResponse response) {
    log.debug("confirmBooking");
    Task task = eventMediatorService.publishEvent(new BookingConfirmEvent(id));
    String endpoint = TASK_PATH+"/"+task.getId();
    response.addHeader(LOCATION, endpoint);
    response.setStatus(HttpServletResponse.SC_ACCEPTED);
  }

}
