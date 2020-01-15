package com.github.owl4soul.interfaces;

/**
 * Абстрактный интерфейс Исполнителя задач.
 * Предоставляет контракт performTask() в виде возможности выполнения некоторой задачи классами, имплементирующими данный интерфейс.
 */
public interface TaskPerformer {

	/**
	 * Выполнение некоторой задачи.
	 */
	void performTask();
}
