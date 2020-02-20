package com.github.owl4soul.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by lera.feoktistova on 20.02.2020.
 */
@Component
public class TaskSchedulerImpl implements TaskScheduler {


	ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);

	@Override
	public ScheduledFuture schedule(Runnable task, Date startTime) {
		return null;
	}

	@Override
	public ScheduledFuture scheduleAtFixedRate(Runnable task, Date startTime, long period) {
		return null;
	}

	@Override
	public ScheduledFuture scheduleAtFixedRate(Runnable task, long period) {
		return null;
	}

	@Override
	public ScheduledFuture scheduleWithFixedDelay(Runnable task, Date startTime, long delay) {
		return null;
	}

	@Override
	public ScheduledFuture scheduleWithFixedDelay(Runnable task, long delay) {
		return null;
	}

	@Override
	public ScheduledFuture schedule(Runnable task, Trigger trigger){
		return Executors.newScheduledThreadPool(1).scheduleAtFixedRate(task, 5, 5, TimeUnit.SECONDS);
	}
}
