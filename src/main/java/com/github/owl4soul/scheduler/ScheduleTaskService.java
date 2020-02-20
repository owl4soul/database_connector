package com.github.owl4soul.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
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

	// Добавление задачи в мапу запланированных задач
	public void addTaskToScheduler(String id, Runnable task, CronTrigger cronTrigger) {
		ScheduledFuture<?> scheduledTask = taskScheduler.schedule(task, cronTrigger);
		scheduledFutureMap.put(id, scheduledTask);
	}

	// Удаление запланированной задачи
	public void removeTaskFromScheduler(String id) {
		ScheduledFuture<?> scheduledTask = scheduledFutureMap.get(id);
		if(scheduledTask != null) {
			scheduledTask.cancel(true);
			scheduledFutureMap.remove(id);
		}
	}

	// Слушатель обновления контекста
	@EventListener({ContextRefreshedEvent.class })
	void contextRefreshedEvent() {
		// Здесь надо загрузить из дб таски в мапу.
		System.out.println("Context has been refreshed");

		// Здесь мы считываем сохраненные параметры запланированных задач:
		//  - id (не обязательно),
		//  - cron-выражение и/или некоторые параметры, указывающие на время и интервал и т.п.

		// Затем по полученным данным инициализируем все задачи заново, как здесь:
		Runnable task = new Taska();
		addTaskToScheduler("Test_task_at", task, new CronTrigger("*/10 * * * * *"));
	}

	private class Taska implements Runnable {
		@Override
		public void run() {
			System.out.println("Я - таска, сработавшая по расписанию!");
			removeTaskFromScheduler("Test_task_at");
			System.out.println("Я - таска, решившая отменить себя, больше не сработаю!");
		}
	}


}
