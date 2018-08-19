package org.dovetail.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.dovetail.util.CollectionUtil;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter
@Getter
@ToString
@EqualsAndHashCode(of= {"id"})
@XmlRootElement(name = "student")
@XmlAccessorType(XmlAccessType.FIELD)
public class Student implements Serializable {
	
	@XmlAttribute(name = "id")
	private String id;
	
	@XmlElement(name = "name")
	private String name;
	
	@XmlElement(name = "subjects")
	private List<Subject> subjects;
	
	@XmlElement(name="percentage")
	private Float percentage;
	
	public void addSubject(Subject subject) {
		if(getSubjects()==null) {
			setSubjects(new ArrayList<Subject>());
		}
		CollectionUtil.add(getSubjects(), subject);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

	public Float getPercentage() {
		return percentage;
	}

	public void setPercentage(Float percentage) {
		this.percentage = percentage;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", subjects=" + subjects + ", percentage=" + percentage + "]";
	}
}
