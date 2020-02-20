package com.github.owl4soul.scheduler;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by lera.feoktistova on 20.02.2020.
 */
@Component
public class TaskSchedulerImpl implements TaskScheduler {



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

		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(5);
		threadPoolTaskScheduler.initialize();

		return threadPoolTaskScheduler.schedule(task, trigger);

	}
}
