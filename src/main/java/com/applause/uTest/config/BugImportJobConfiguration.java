package com.applause.uTest.config;

import com.applause.uTest.config.listener.BugJobCompletionNotificationListener;
import com.applause.uTest.config.model.BugDTO;
import com.applause.uTest.config.processor.BugsProcessor;

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
public class BugImportJobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean(name = "importBugReader")
    public FlatFileItemReader<BugDTO> reader() {
        return new FlatFileItemReaderBuilder<BugDTO>()
            .name("importBugReader")
            .linesToSkip(1)
            .recordSeparatorPolicy(new BlankLineRecordSeparatorPolicy())
            .resource(new ClassPathResource("data/bugs.csv"))
            .delimited()
            .names(new String[]{"bugId", "deviceId", "testerId"})
            .fieldSetMapper(new BeanWrapperFieldSetMapper<BugDTO>() {{
                setTargetType(BugDTO.class);
            }})
            .build();
    }

    @Bean(name = "importBugProcessor")
    public BugsProcessor processor() {
        return new BugsProcessor();
    }

    @Bean(name = "importBugWriter")
    public JdbcBatchItemWriter<BugDTO> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<BugDTO>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql("INSERT INTO bugs (bugId, deviceId, testerId) VALUES (:bugId, :deviceId, :testerId)")
            .dataSource(dataSource)
            .build();
    }

    @Bean(name = "importBugStep")
    public Step step(JdbcBatchItemWriter<BugDTO> writer) {
        return stepBuilderFactory.get("importBugStep")
            .<BugDTO, BugDTO>chunk(10)
            .reader(reader())
            .processor(processor())
            .writer(writer)
            .build();
    }

    @Bean(name = "importBugJob")
    public Job importBugJob(BugJobCompletionNotificationListener listener, Step importBugStep) {
        return jobBuilderFactory.get("importBugJob")
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(importBugStep)
            .end()
            .build();
    }
}
