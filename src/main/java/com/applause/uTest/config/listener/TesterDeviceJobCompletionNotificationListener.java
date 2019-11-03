package com.applause.uTest.config.listener;

import com.applause.uTest.config.model.TesterDeviceDTO;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TesterDeviceJobCompletionNotificationListener extends JobExecutionListenerSupport {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TesterDeviceJobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! TESTER DEVICE JOB FINISHED!");

            jdbcTemplate.query("SELECT testerId, deviceId FROM tester_device",
                (rs, row) -> {
                    return new TesterDeviceDTO(
                        rs.getInt(1),
                        rs.getInt(2));
                }
            ).forEach(testerDeviceDTO -> log.debug("Found <{}> in the database.", testerDeviceDTO));

        }
    }
}
