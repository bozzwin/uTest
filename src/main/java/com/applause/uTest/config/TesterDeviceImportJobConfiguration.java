package com.applause.uTest.config;

import com.applause.uTest.config.listener.TesterDeviceJobCompletionNotificationListener;
import com.applause.uTest.config.processor.TesterDeviceProcessor;
import com.applause.uTest.config.model.TesterDeviceDTO;

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
public class TesterDeviceImportJobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean(name = "importTesterDeviceReader")
    public FlatFileItemReader<TesterDeviceDTO> reader() {
        return new FlatFileItemReaderBuilder<TesterDeviceDTO>()
            .name("importTesterDeviceReader")
            .linesToSkip(1)
            .recordSeparatorPolicy(new BlankLineRecordSeparatorPolicy())
            .resource(new ClassPathResource("data/tester_device.csv"))
            .delimited()
            .names(new String[]{"testerId", "deviceId"})
            .fieldSetMapper(new BeanWrapperFieldSetMapper<TesterDeviceDTO>() {{
                setTargetType(TesterDeviceDTO.class);
            }})
            .build();
    }

    @Bean(name = "importTesterDeviceProcessor")
    public TesterDeviceProcessor processor() {
        return new TesterDeviceProcessor();
    }

    @Bean(name = "importTesterDeviceWriter")
    public JdbcBatchItemWriter<TesterDeviceDTO> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<TesterDeviceDTO>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql("INSERT INTO tester_device (testerId, deviceId) VALUES (:testerId, :deviceId)")
            .dataSource(dataSource)
            .build();
    }

    @Bean(name = "importTesterDeviceStep")
    public Step step(JdbcBatchItemWriter<TesterDeviceDTO> writer) {
        return stepBuilderFactory.get("importTesterDeviceStep")
            .<TesterDeviceDTO, TesterDeviceDTO>chunk(10)
            .reader(reader())
            .processor(processor())
            .writer(writer)
            .build();
    }

    @Bean(name = "importTesterDeviceJob")
    public Job importTesterDeviceJob(TesterDeviceJobCompletionNotificationListener listener, Step importTesterDeviceStep) {
        return jobBuilderFactory.get("importTesterDeviceJob")
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(importTesterDeviceStep)
            .end()
            .build();
    }
}
