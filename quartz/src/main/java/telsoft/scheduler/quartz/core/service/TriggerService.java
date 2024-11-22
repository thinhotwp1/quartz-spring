package telsoft.scheduler.quartz.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telsoft.scheduler.quartz.core.entity.Trigger;
import telsoft.scheduler.quartz.core.repository.CronTriggerRepository;
import telsoft.scheduler.quartz.core.repository.SimpleTriggerRepository;
import telsoft.scheduler.quartz.core.repository.TriggerRepository;

import java.util.Optional;
import java.util.Set;

@Service
public class TriggerService {

    @Autowired
    private TriggerRepository triggerRepository;

    @Autowired
    private CronTriggerRepository cronTriggerRepository;

    @Autowired
    SimpleTriggerRepository simpleTriggerRepository;

    public Set<Trigger> getTriggersListByJobName(String jobName) {
        return triggerRepository.findTriggersByJobName(jobName);
    }

    public Optional<Trigger> getTriggerById(String triggerId) {
        Optional<Trigger> trigger = triggerRepository.findById(triggerId);

        trigger.ifPresent(value -> value.setSimpleTriggers(simpleTriggerRepository.findByTriggerName(triggerId)));
        trigger.ifPresent(value -> value.setCronTriggers(cronTriggerRepository.findAllByTriggerName(triggerId)));

        return trigger;
    }

    public Set<telsoft.scheduler.quartz.core.entity.Trigger> getTriggersDetail(String jobId) {
        Set<telsoft.scheduler.quartz.core.entity.Trigger> triggers = getTriggersListByJobName(jobId);

        for (telsoft.scheduler.quartz.core.entity.Trigger trigger : triggers) {
            trigger.setSimpleTriggers(simpleTriggerRepository.findByTriggerName(trigger.getTriggerName()));
            trigger.setCronTriggers(cronTriggerRepository.findAllByTriggerName(trigger.getTriggerName()));
        }

        return triggers;
    }

    public void deleteSimpleTriggerById(String triggerName) {
        simpleTriggerRepository.deleteById(triggerName);
    }

    public void deleteCronTriggerById(String triggerName) {
        cronTriggerRepository.deleteById(triggerName);
    }

    public void deleteById(String triggerName) {
        triggerRepository.deleteById(triggerName);
    }
}
