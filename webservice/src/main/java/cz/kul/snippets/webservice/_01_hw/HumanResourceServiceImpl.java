package cz.kul.snippets.webservice._01_hw;

import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class HumanResourceServiceImpl implements HumanResourceService {

	@Override
	public void bookHoliday(Date startDate, Date endDate, String name) {
		System.out.println("Booking holiday for [" + startDate + "-" + endDate + "] for [" + name + "] ");
	}

}
