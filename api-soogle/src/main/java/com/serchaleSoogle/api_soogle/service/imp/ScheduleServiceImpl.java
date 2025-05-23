package com.serchaleSoogle.api_soogle.service.imp;

import com.serchaleSoogle.api_soogle.service.ScheduleService;

import com.serchaleSoogle.api_soogle.service.SpiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private final SpiderService spiderService;
    /**
     * This method is scheduled to run every day at midnight (00:00).
     * It calls the indexWebPage method of the SpiderService to index the specified web page.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public void scheduleIndexWebPages() {
     spiderService.indexWebPage("https://www.infobae.com/deportes/");
    }
}
