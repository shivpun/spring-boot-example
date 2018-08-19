package com.ing.csv;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Component
public class DoveTailCsv {
	
	@Value(value="${dovetail.csv.location}")
	private Resource[] csvFile;
	
	@Value(value="${dovetail.csv.field}")
	private String field;
	
	
	public Resource[] getCsvFile() {
		return csvFile;
	}

	public void setCsvFile(Resource[] csvFile) {
		this.csvFile = csvFile;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
	
	public String[] getFieldArray() {
		if(getField()==null) {
			return new String[0]; 
		}
		String fields = StringUtils.trimAllWhitespace(getField());
		return StringUtils.delimitedListToStringArray(fields, ",");
	}
	
}
