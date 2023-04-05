package com.kumar.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class FirstItemProcessor implements ItemProcessor<Integer, Long> {

	@Override
	public Long process(Integer item) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("inside item processor");
		return Long.valueOf(item + 20);
	}

}
