package com.tui.proof.ws.controller;

import com.tui.proof.ws.model.FlightAvailabilityRequest;
import com.tui.proof.ws.model.FlightAvailabilityResponse;
import com.tui.proof.ws.services.AvailabilityService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;

import static com.tui.proof.ws.utils.Constants.AVAILABILITY_PATH;

@Log4j2
@RestController
public class FlightAvailabilityController {

  @Autowired
  private AvailabilityService availabilityService;

  @GetMapping(AVAILABILITY_PATH)
  public FlightAvailabilityResponse searchFlightAvailability(@RequestBody @Valid FlightAvailabilityRequest flightAvailability, HttpServletResponse response) throws IOException {
    if (!isValid(flightAvailability)) {
      response.setStatus(400);
      response.getWriter().write("Validation fields error, please fill all the fields in correctly and mind that adults must be at least 1");
      response.getWriter().flush();
      return null;
    }
    log.debug("searchFlightAvailability");
    return availabilityService.getFlightAvailability(flightAvailability);
  }

  private boolean isValid(FlightAvailabilityRequest flightAvailability) {
    return flightAvailability != null && flightAvailability.getDateFrom() != null && flightAvailability.getDateTo() != null
            && flightAvailability.getDestinationAirport() != null && flightAvailability.getOriginAirport() != null
            && flightAvailability.getPaxes() != null && flightAvailability.getPaxes().getAdults() > 0;
  }
}
