package telsoft.scheduler.quartz.manager.enums;


public class TriggerContains {
    // Simple trigger
    public static final Integer SIMPLE_MISFIRE_INSTRUCTION_FIRE_NOW = 1;
    public static final Integer SIMPLE_MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_REPEAT_COUNT = 2;
    public static final Integer SIMPLE_MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT = 3;
    public static final Integer SIMPLE_MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT = 4;
    public static final Integer SIMPLE_MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_EXISTING_COUNT = 5;
    public static final Integer SIMPLE_REPEAT_INDEFINITELY = -1;


    // Cron trigger
    public static final Integer CRON_MISFIRE_INSTRUCTION_FIRE_ONCE_NOW = 1;
    public static final Integer CRON_MISFIRE_INSTRUCTION_DO_NOTHING = 2;
}
