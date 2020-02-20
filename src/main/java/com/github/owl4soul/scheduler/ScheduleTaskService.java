package com.github.owl4soul.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by lera.feoktistova on 20.02.2020.
 */
@Service
public class ScheduleTaskService {

	// Интерфейс шедулера
	@Resource
	private TaskScheduler taskScheduler;

	// Мапа для хранения запланированных задач
	Map<String, ScheduledFuture<?>> scheduledFutureMap = new HashMap<>();
//
//	// Конструктор, принимающий на вход реализацию taskScheduler для инициализации вложенного интерфейса
//	@Autowired
//	public ScheduleTaskService(TaskScheduler taskScheduler) {
//		this.taskScheduler = taskScheduler;
//	}

	// Добавление задачи в мапу запланированных задач
	// Schedule Task to be executed every night at 00 or 12 am
	public void addTaskToScheduler(String id, Runnable task) {
		ScheduledFuture<?> scheduledTask = taskScheduler.schedule(task, new CronTrigger("0 0 0 * * ?", TimeZone
				.getTimeZone(TimeZone.getDefault().getID())));
		scheduledFutureMap.put(id, scheduledTask);
	}

	// Добавление задачи в мапу запланированных задач
	// Schedule Task to be executed every night at 00 or 12 am
	public void addTaskToScheduler(String id, Runnable task, CronTrigger cronTrigger) {
		ScheduledFuture<?> scheduledTask = taskScheduler.schedule(task, cronTrigger);
		scheduledFutureMap.put(id, scheduledTask);
	}

	// Слушатель обновления контекста
	@EventListener({ContextRefreshedEvent.class })
	void contextRefreshedEvent() {
		// Здесь надо загрузить из дб таски в мапу.
		System.out.println("Context has been refreshed");

		Runnable task = new Taska();

		addTaskToScheduler("Test_task_at_" + Instant.now(), task, new CronTrigger("*/10 * * * * *"));
	}

	private static class Taska implements Runnable {
		@Override
		public void run() {
			System.out.println("Я - таска, сработавшая по расписанию!");
		}
	}


}
