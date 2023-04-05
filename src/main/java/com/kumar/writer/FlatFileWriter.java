package com.kumar.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.kumar.model.StudentCsv;
import com.kumar.model.StudentJson;

@Component
public class FlatFileWriter implements ItemWriter<StudentJson>{
	
	@Override
	public void write(List<? extends StudentJson> items) throws Exception {
		System.out.println("inside item Writer");
		for (StudentJson long1 : items) {
			System.out.println(long1);
		}
	}
}
