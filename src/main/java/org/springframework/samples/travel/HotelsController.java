package org.springframework.samples.travel;

import java.security.Principal;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.*;

@Controller
public class HotelsController {

	private BookingService bookingService;
	
	static int status = 0;

	@Inject
	public HotelsController(BookingService bookingService) {
		this.bookingService = bookingService;
	}

	@RequestMapping(value = "/hotels/search", method = RequestMethod.GET)
	public void search(SearchCriteria searchCriteria, Principal currentUser, Model model) {		
		if(status++ == 0){
			bookingService.createBasicData();
		}
		if (currentUser != null) {
			List<Booking> booking = bookingService.findBookings(currentUser.getName());
			model.addAttribute(booking);
		}
	}

	@RequestMapping(value = "/hotels", method = RequestMethod.GET)
	public String list(SearchCriteria criteria, Model model) {
		List<Hotel> hotels = bookingService.findHotels(criteria);		
		model.addAttribute(hotels);
		return "hotels/list";
	}

   /**
    * 
    * Hotel beanshould be injected in order to persist user choosen hotel
    */

/*	@RequestMapping(value = "/hotels/bookings", method = RequestMethod.GET)
	public String bookHotel(@RequestParam Long hotelId,Principal currentUser,Model model) {
		model.addAttribute(bookingService.findHotelById(hotelId));
		System.out.println("Hotel id is :" + hotelId);
		System.out.println("User logged in is "+ currentUser.getName());
		return "redirect:../hotels/show";
	}*/
	
	
		@RequestMapping(value = "/hotels/{id}", method = RequestMethod.GET)
		public String showHotel(@PathVariable Long id,Principal currentUser,Model model) {
		
		System.out.println("Hotel id is :" + id);
		model.addAttribute(bookingService.findHotelById(id));
		System.out.println("Hotel id is :" + id);
		System.out.println("User logged in is "+ currentUser.getName());
		return "hotels/show";
		}

	@RequestMapping(value = "/bookings/{id}", method = RequestMethod.DELETE)
	public String deleteBooking(@PathVariable Long id) {
		bookingService.cancelBooking(id);
		return "redirect:../hotels/search";
	}
	
	//http://11069081.au-syd.mybluemix.net/hotels/booking?hotelId=1
	@RequestMapping(value = "/hotels/bookings", method = RequestMethod.GET)
	public String enterBookingDetails(@RequestParam Long hotelId,Principal currentUser,Model model,Booking booking) {
		//bookingService.cancelBooking(id);
		Hotel hotel = bookingService.findHotelById(hotelId);
		booking.setHotel(hotel);
		model.addAttribute(booking);
		return "hotels/enterBookingDetails";
	}
	
//	@RequestMapping(value = "/hotels/bookings/{id}", method = RequestMethod.POST)
//	public String proceedBooking(@PathVariable Long id,Model model,Booking booking) {
//		//bookingService.cancelBooking(id);
//		model.addAttribute(booking);
//		System.out.println("Hotel id is :" + id);
//		model.addAttribute(bookingService.findHotelById(id));
//		System.out.println("Hotel id is :" + id);
//		System.out.println("User logged in is "+ currentUser.getName());
//		return "hotels/search";
//	}

}