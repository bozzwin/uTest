package com.applause.uTest.config.listener;

import com.applause.uTest.config.model.DeviceDTO;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DeviceJobCompletionNotificationListener extends JobExecutionListenerSupport {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DeviceJobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! DEVICE JOB FINISHED!");

            jdbcTemplate.query("SELECT id, description FROM devices",
                (rs, row) -> new DeviceDTO(
                    rs.getInt(1),
                    rs.getString(2))
            ).forEach(deviceDTO -> log.debug("Found <{}> in the database.", deviceDTO));
        }
    }
}
