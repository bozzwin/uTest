package com.applause.uTest.config.listener;

import com.applause.uTest.config.model.BugDTO;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BugJobCompletionNotificationListener extends JobExecutionListenerSupport {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BugJobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! BUG JOB FINISHED!");

            jdbcTemplate.query("SELECT bugId, deviceId, testerId FROM bugs",
                (rs, row) -> new BugDTO(
                    rs.getInt(1),
                    rs.getInt(2),
                    rs.getInt(3))
            ).forEach(bugDTO -> log.debug("Found <{}> in the database.", bugDTO));
        }
    }
}
