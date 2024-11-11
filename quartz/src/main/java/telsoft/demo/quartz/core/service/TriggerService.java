package telsoft.demo.quartz.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telsoft.demo.quartz.core.entity.Trigger;
import telsoft.demo.quartz.core.repository.TriggerRepository;

import java.util.Set;

@Service
public class TriggerService {

    @Autowired
    private TriggerRepository triggerRepository;

    public Set<Trigger> getTriggersListByJobName(String jobName){
        return triggerRepository.findTriggersByJobName(jobName);
    }

}
