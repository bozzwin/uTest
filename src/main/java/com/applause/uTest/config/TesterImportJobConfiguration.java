package com.applause.uTest.config;

import com.applause.uTest.config.listener.TestersJobCompletionNotificationListener;
import com.applause.uTest.config.processor.TestersProcessor;
import com.applause.uTest.config.model.TesterDTO;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class TesterImportJobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;


    @Bean(name = "importTesterReader")
    public FlatFileItemReader<TesterDTO> reader() {
        return new FlatFileItemReaderBuilder<TesterDTO>()
            .name("importTesterReader")
            .linesToSkip(1)
            .recordSeparatorPolicy(new BlankLineRecordSeparatorPolicy())
            .resource(new ClassPathResource("data/testers.csv"))
            .delimited()
            .names(new String[]{"id", "firstName", "lastName", "country", "lastLogin"})
            .fieldSetMapper(new BeanWrapperFieldSetMapper<TesterDTO>() {{
                setTargetType(TesterDTO.class);
            }})
            .build();
    }

    @Bean(name = "importTesterProcessor")
    public TestersProcessor processor() {
        return new TestersProcessor();
    }

    @Bean(name = "importTesterWriter")
    public JdbcBatchItemWriter<TesterDTO> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<TesterDTO>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql("INSERT INTO testers (id, first_name, last_name, country, last_login) VALUES (:id, :firstName, :lastName, :country, :lastLogin)")
            .dataSource(dataSource)
            .build();
    }

    @Bean(name = "importTesterStep")
    public Step step(JdbcBatchItemWriter<TesterDTO> writer) {
        return stepBuilderFactory.get("importTesterStep")
            .<TesterDTO, TesterDTO> chunk(10)
            .reader(reader())
            .processor(processor())
            .writer(writer)
            .build();
    }

    @Bean(name = "importTesterJob")
    public Job importTesterJob(TestersJobCompletionNotificationListener listener, Step importTesterStep) {
        return jobBuilderFactory.get("importTesterJob")
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(importTesterStep)
            .end()
            .build();
    }
}
