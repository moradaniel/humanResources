package org.springframework.samples.travel;

import java.util.List;

import javax.inject.Inject;

import org.dpi.reparticion.ReparticionService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//@Controller
public class HotelSearchController {

	private BookingService bookingService;
	private ReparticionService reparticionService;


	@Inject
	public HotelSearchController(BookingService bookingService) {
		this.bookingService = bookingService;
	}
	
	
	public ReparticionService getReparticionService() {
		return reparticionService;
	}

	@Inject
	public void setReparticionService(
			ReparticionService ReparticionService) {
		this.reparticionService = ReparticionService;
	}
	
	@RequestMapping(value = "/hotels/main", method = RequestMethod.GET)
	public void main(SearchCriteria searchCriteria) {
	}

	@RequestMapping(value = "/hotels/search", method = RequestMethod.GET)
	public void search(SearchCriteria searchCriteria) {
	}

	@RequestMapping(value = "/hotels", method = RequestMethod.GET)
	public String list(SearchCriteria criteria, Model model) {
		List<Hotel> hotels = bookingService.findHotels(criteria);
		model.addAttribute(hotels);
		return "hotels/list";
	}

}