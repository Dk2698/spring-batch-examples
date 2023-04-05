package com.kumar.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class FirstItemWriter implements ItemWriter<Long> {

	@Override
	public void write(List<? extends Long> items) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("inside item Writer");
		
//		items.stream().forEach(System.out::println);
		for (Long long1 : items) {
			System.out.println(long1);
		}
	}

}
