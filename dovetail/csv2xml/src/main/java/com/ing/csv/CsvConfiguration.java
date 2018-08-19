package com.ing.csv;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.dovetail.model.Student;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.ing.mapper.StudentFieldSetMapper;
import com.ing.mapper.StudentItemProcessor;

@Configuration
@EnableBatchProcessing
public class CsvConfiguration {
	
	@Autowired
	private DoveTailCsv doveTailCsv;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private SimpleJobLauncher jobLauncher;
	
	@Autowired
	private StudentItemProcessor sip;
	
	int count = 0;
	
	@Bean
	public ResourcelessTransactionManager transactionManager() {
		return new ResourcelessTransactionManager();
	}
	
	@Bean
	public MapJobRepositoryFactoryBean mapJobRepositoryFactory(ResourcelessTransactionManager txManager) throws Exception {
		MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean(txManager);
		factory.afterPropertiesSet();
		return factory;
	}

	@Bean
	public JobRepository jobRepository(MapJobRepositoryFactoryBean factory) throws Exception {
		return factory.getObject();
	}
	
	@Bean
	public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
		SimpleJobLauncher launcher = new SimpleJobLauncher();
		launcher.setJobRepository(jobRepository);
		return launcher;
	}
		
	@Bean
	public LineMapper<Student> getBeanWrapperFieldExtractor() {
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames(doveTailCsv.getFieldArray());
		
		BeanWrapperFieldSetMapper<Student> fieldSetMapper = new BeanWrapperFieldSetMapper<Student>();
		fieldSetMapper.setTargetType(Student.class);

		DefaultLineMapper<Student> lineMapper = new DefaultLineMapper<Student>();
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(studentFieldSetMapper());
		return lineMapper;
	}

	private FieldSetMapper<Student> studentFieldSetMapper() {
		return new StudentFieldSetMapper();
	}
	
	@Bean
    public Partitioner partitioner(Resource[] location) {
        MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
        ClassLoader cl = this.getClass().getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
        partitioner.setResources(location);
        partitioner.partition(10);      
        return partitioner;
    }
	
	  @Bean
	    @Qualifier("masterStep")
	    public Step masterStep(@Value(value="${dovetail.csv.location}") Resource[] location) {
	        return stepBuilderFactory.get("masterStep")
	              //  .partitioner(ProcessDataStep())
	                .partitioner("ProcessDataStep",partitioner(location))   
	               // .taskExecutor(taskExecutor())
	                //.listener(pcStressStepListener)
	                .build();
	    }
	
	@Bean(destroyMethod="")
	public ItemReader<Student> reader(String filename) throws IOException{
		FlatFileItemReader<Student> reader = new FlatFileItemReader<Student>();
		reader.setLinesToSkip(1);//first line is title definition
		/*for(Resource resource:doveTailCsv.getCsvFile()) {
			System.out.println("FILE-FETCH:"+resource.getFile().getAbsolutePath());
			reader.setResource(resource);
		}*/
		reader.setLineMapper(getBeanWrapperFieldExtractor());
		return reader;
	}
	
	@Bean
	public Job addNewPodcastJob(@Value(value="${dovetail.csv.archive}") Resource location, @Value("#{stepExecutionContext['fileName']}") String filename) throws IOException{
		System.out.println("ARCHIEVE:"+location.getFile().getAbsolutePath());
		return jobBuilderFactory.get("addNewPodcastJob")
			//	.listener(protocolListener())
				.start(step(location, filename))
				.build();
	}
	
	@Bean(name= {"step"})
	public Step step(Resource location, String filename) throws IOException{
		return stepBuilderFactory.get("step")
				.<Student, List<Student>>chunk(1) //important to be one in this case to commit after every line read
				.reader(reader(filename))
				.processor(sip)
				.writer(itemWriter(location))
				//.listener(logProcessListener())
				.faultTolerant()
				//.skipLimit(10) //default is set to 0
				//.skip(MySQLIntegrityConstraintViolationException.class)
				.build();
	}
	
	@Bean(destroyMethod="")
	public StaxEventItemWriter<List<Student>> itemWriter(Resource location) throws MalformedURLException {
		StaxEventItemWriter<List<Student>> sei = new StaxEventItemWriter<List<Student>>();
		sei.setResource(location);
		sei.setMarshaller(marshaller());
		return sei;
	}
	
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller jm = new Jaxb2Marshaller();
		jm.setClassesToBeBound(Student.class);
		return jm;
	}
	
	
}
