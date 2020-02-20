package com.github.owl4soul.scheduler;

import org.springframework.scheduling.Trigger;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by lera.feoktistova on 20.02.2020.
 */
public interface TaskScheduler {

	// Запускается единожды в заданное время
	ScheduledFuture schedule(Runnable task, Date startTime);

	// Сперва запускается в заданное время, затем повторяется через заданный период
	ScheduledFuture scheduleAtFixedRate(Runnable task, Date startTime, long period);

	// Запускается сразу, затем повторяется через заданный период
	ScheduledFuture scheduleAtFixedRate(Runnable task, long period);

	// Сперва запускается в заданное время, затем повторяется после заданной задержки с последнего исполнения
	ScheduledFuture scheduleWithFixedDelay(Runnable task, Date startTime, long delay);

	// Запускается сразу, затем повторяется после заданной задержки с последнего исполнения
	ScheduledFuture scheduleWithFixedDelay(Runnable task, long delay);

	// Запускается в соответствии с условиями триггера
	ScheduledFuture schedule(Runnable task, Trigger trigger);

}