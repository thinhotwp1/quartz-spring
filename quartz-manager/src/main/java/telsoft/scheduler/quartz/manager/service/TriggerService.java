package telsoft.scheduler.quartz.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telsoft.scheduler.quartz.manager.entity.CronTrigger;
import telsoft.scheduler.quartz.manager.entity.SimpleTrigger;
import telsoft.scheduler.quartz.manager.entity.Trigger;
import telsoft.scheduler.quartz.manager.repository.CronTriggerRepository;
import telsoft.scheduler.quartz.manager.repository.SimpleTriggerRepository;
import telsoft.scheduler.quartz.manager.repository.TriggerRepository;

import java.util.Collection;
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

    public Set<Trigger> getTriggersDetail(String jobId) {
        Set<Trigger> triggers = getTriggersListByJobName(jobId);

        for (Trigger trigger : triggers) {
            trigger.setSimpleTriggers(simpleTriggerRepository.findByTriggerName(trigger.getTriggerName()));
            trigger.setCronTriggers(cronTriggerRepository.findAllByTriggerName(trigger.getTriggerName()));
        }

        return triggers;
    }
    public void saveTrigger(Trigger trigger){
        triggerRepository.saveAndFlush(trigger);
    }
    public void saveAllTriggers(Collection<Trigger> trigger){
        triggerRepository.saveAllAndFlush(trigger);
    }
    public void saveSimpleTrigger(SimpleTrigger simpleTrigger){
        simpleTriggerRepository.saveAndFlush(simpleTrigger);
    }
    public void saveCronTrigger(CronTrigger cronTrigger){
        cronTriggerRepository.saveAndFlush(cronTrigger);
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
