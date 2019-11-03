package com.applause.uTest.config;

import com.applause.uTest.config.listener.DeviceJobCompletionNotificationListener;
import com.applause.uTest.config.processor.DevicesProcessor;
import com.applause.uTest.config.model.DeviceDTO;

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
public class DeviceImportJobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean(name = "importDeviceReader")
    public FlatFileItemReader<DeviceDTO> reader() {
        return new FlatFileItemReaderBuilder<DeviceDTO>()
            .name("importDeviceReader")
            .linesToSkip(1)
            .recordSeparatorPolicy(new BlankLineRecordSeparatorPolicy())
            .resource(new ClassPathResource("data/devices.csv"))
            .delimited()
            .names(new String[]{"id", "description"})
            .fieldSetMapper(new BeanWrapperFieldSetMapper<DeviceDTO>() {{
                setTargetType(DeviceDTO.class);
            }})
            .build();
    }

    @Bean(name = "importDeviceProcessor")
    public DevicesProcessor processor() {
        return new DevicesProcessor();
    }

    @Bean(name = "importDeviceWriter")
    public JdbcBatchItemWriter<DeviceDTO> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<DeviceDTO>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql("INSERT INTO devices (id, description) VALUES (:id, :description)")
            .dataSource(dataSource)
            .build();
    }

    @Bean(name = "importDeviceStep")
    public Step step(JdbcBatchItemWriter<DeviceDTO> writer) {
        return stepBuilderFactory.get("importDeviceStep")
            .<DeviceDTO, DeviceDTO>chunk(10)
            .reader(reader())
            .processor(processor())
            .writer(writer)
            .build();
    }

    @Bean(name = "importDeviceJob")
    public Job importDeviceJob(DeviceJobCompletionNotificationListener listener, Step importDeviceStep) {
        return jobBuilderFactory.get("importDeviceJob")
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(importDeviceStep)
            .end()
            .build();
    }
}
