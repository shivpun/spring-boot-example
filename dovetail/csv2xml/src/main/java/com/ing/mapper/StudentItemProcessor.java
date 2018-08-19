package com.ing.mapper;

import java.util.ArrayList;
import java.util.List;

import org.dovetail.model.Student;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class StudentItemProcessor implements ItemProcessor<Student, List<Student>> {
	
	private List<Student> students = new ArrayList<Student>();

	@Override
	public List<Student> process(Student item) throws Exception {
		students.add(item);
		System.out.println("StudentItemProcessor | process | item:"+item);
		return students;
	}

}
