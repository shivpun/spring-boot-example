package com.ing.mapper;

import org.dovetail.model.Student;
import org.dovetail.model.Subject;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class StudentFieldSetMapper implements FieldSetMapper<Student> {

	@Override
	public Student mapFieldSet(FieldSet fs) throws BindException {
		Student student = student(fs);
		Subject subject = subject(fs);
		student.addSubject(subject);
		return student;
	}
	
	private Student student(FieldSet fs) {
		Student student = new Student();
		student.setId(fs.readString("student.id"));
		student.setName(fs.readString("student.name"));
		return student;
	}
	
	private Subject subject(FieldSet fs) {
		Subject subject = new Subject();
		subject.setId(fs.readInt("subject.id"));
		subject.setName(fs.readString("subject.name"));
		subject.setMark(fs.readInt("subject.mark"));
		subject.setTotal(fs.readInt("subject.total"));
		return subject;
	}
}
